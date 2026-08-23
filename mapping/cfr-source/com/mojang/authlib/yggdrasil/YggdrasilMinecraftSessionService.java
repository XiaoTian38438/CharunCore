/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.cache.CacheLoader
 *  com.google.common.cache.LoadingCache
 *  com.google.common.collect.Iterables
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonParseException
 *  javax.annotation.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mojang.authlib.yggdrasil;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.SignatureState;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.minecraft.InsecurePublicKeyException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTextures;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.ProfileActionType;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import com.mojang.authlib.yggdrasil.TextureUrlChecker;
import com.mojang.authlib.yggdrasil.request.JoinMinecraftServerRequest;
import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.mojang.authlib.yggdrasil.response.ProfileAction;
import com.mojang.util.UUIDTypeAdapter;
import com.mojang.util.UndashedUuid;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YggdrasilMinecraftSessionService
implements MinecraftSessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilMinecraftSessionService.class);
    private final MinecraftClient client;
    private final ServicesKeySet servicesKeySet;
    private final String baseUrl;
    private final URL joinUrl;
    private final URL checkUrl;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, (Object)new UUIDTypeAdapter()).create();
    private final LoadingCache<UUID, Optional<ProfileResult>> insecureProfiles = CacheBuilder.newBuilder().expireAfterWrite(6L, TimeUnit.HOURS).build((CacheLoader)new CacheLoader<UUID, Optional<ProfileResult>>(){

        public Optional<ProfileResult> load(UUID uUID) {
            return Optional.ofNullable(YggdrasilMinecraftSessionService.this.fetchProfileUncached(uUID, false));
        }
    });

    protected YggdrasilMinecraftSessionService(ServicesKeySet servicesKeySet, Proxy proxy, Environment environment) {
        this.client = MinecraftClient.unauthenticated(proxy);
        this.servicesKeySet = servicesKeySet;
        this.baseUrl = environment.sessionHost() + "/session/minecraft/";
        this.joinUrl = HttpAuthenticationService.constantURL(this.baseUrl + "join");
        this.checkUrl = HttpAuthenticationService.constantURL(this.baseUrl + "hasJoined");
    }

    @Override
    public void joinServer(UUID uUID, String string, String string2) throws AuthenticationException {
        JoinMinecraftServerRequest joinMinecraftServerRequest = new JoinMinecraftServerRequest(string, uUID, string2);
        try {
            this.client.post(this.joinUrl, joinMinecraftServerRequest, Void.class);
        }
        catch (MinecraftClientException minecraftClientException) {
            throw minecraftClientException.toAuthenticationException();
        }
    }

    @Override
    @Nullable
    public ProfileResult hasJoinedServer(String string, String string2, @Nullable InetAddress inetAddress) throws AuthenticationUnavailableException {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("username", string);
        hashMap.put("serverId", string2);
        if (inetAddress != null) {
            hashMap.put("ip", inetAddress.getHostAddress());
        }
        URL uRL = HttpAuthenticationService.concatenateURL(this.checkUrl, HttpAuthenticationService.buildQuery(hashMap));
        try {
            HasJoinedMinecraftServerResponse hasJoinedMinecraftServerResponse = this.client.get(uRL, HasJoinedMinecraftServerResponse.class);
            if (hasJoinedMinecraftServerResponse != null && hasJoinedMinecraftServerResponse.id() != null) {
                GameProfile gameProfile = new GameProfile(hasJoinedMinecraftServerResponse.id(), string, Objects.requireNonNullElse(hasJoinedMinecraftServerResponse.properties(), PropertyMap.EMPTY));
                Set<ProfileActionType> set = YggdrasilMinecraftSessionService.extractProfileActionTypes(hasJoinedMinecraftServerResponse.profileActions());
                return new ProfileResult(gameProfile, set);
            }
            return null;
        }
        catch (MinecraftClientException minecraftClientException) {
            AuthenticationException authenticationException = minecraftClientException.toAuthenticationException();
            if (authenticationException instanceof AuthenticationUnavailableException) {
                AuthenticationUnavailableException authenticationUnavailableException = (AuthenticationUnavailableException)authenticationException;
                throw authenticationUnavailableException;
            }
            return null;
        }
    }

    @Override
    @Nullable
    public Property getPackedTextures(GameProfile gameProfile) {
        return (Property)Iterables.getFirst((Iterable)gameProfile.properties().get("textures"), null);
    }

    @Override
    public MinecraftProfileTextures unpackTextures(Property property) {
        MinecraftTexturesPayload minecraftTexturesPayload;
        Object object;
        String string = property.value();
        SignatureState signatureState = this.getPropertySignatureState(property);
        try {
            object = new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8);
            minecraftTexturesPayload = (MinecraftTexturesPayload)this.gson.fromJson((String)object, MinecraftTexturesPayload.class);
        }
        catch (JsonParseException | IllegalArgumentException throwable) {
            LOGGER.error("Could not decode textures payload", throwable);
            return MinecraftProfileTextures.EMPTY;
        }
        if (minecraftTexturesPayload == null || minecraftTexturesPayload.textures() == null || minecraftTexturesPayload.textures().isEmpty()) {
            return MinecraftProfileTextures.EMPTY;
        }
        object = minecraftTexturesPayload.textures();
        for (Map.Entry entry : object.entrySet()) {
            String string2 = ((MinecraftProfileTexture)entry.getValue()).getUrl();
            if (string2 != null && TextureUrlChecker.isAllowedTextureDomain(string2)) continue;
            LOGGER.error("Textures payload url is invalid: {}", (Object)string2);
            return MinecraftProfileTextures.EMPTY;
        }
        return new MinecraftProfileTextures((MinecraftProfileTexture)object.get((Object)MinecraftProfileTexture.Type.SKIN), (MinecraftProfileTexture)object.get((Object)MinecraftProfileTexture.Type.CAPE), (MinecraftProfileTexture)object.get((Object)MinecraftProfileTexture.Type.ELYTRA), signatureState);
    }

    @Override
    @Nullable
    public ProfileResult fetchProfile(UUID uUID, boolean bl) {
        if (!bl) {
            return ((Optional)this.insecureProfiles.getUnchecked((Object)uUID)).orElse(null);
        }
        return this.fetchProfileUncached(uUID, true);
    }

    @Override
    public String getSecurePropertyValue(Property property) throws InsecurePublicKeyException {
        switch (this.getPropertySignatureState(property)) {
            default: {
                throw new IncompatibleClassChangeError();
            }
            case UNSIGNED: {
                throw new InsecurePublicKeyException.MissingException("Missing signature from \"" + property.name() + "\"");
            }
            case INVALID: {
                throw new InsecurePublicKeyException.InvalidException("Property \"" + property.name() + "\" has been tampered with (signature invalid)");
            }
            case SIGNED: 
        }
        return property.value();
    }

    private SignatureState getPropertySignatureState(Property property) {
        if (!property.hasSignature()) {
            return SignatureState.UNSIGNED;
        }
        if (this.servicesKeySet.keys(ServicesKeyType.PROFILE_PROPERTY).stream().noneMatch(servicesKeyInfo -> servicesKeyInfo.validateProperty(property))) {
            return SignatureState.INVALID;
        }
        return SignatureState.SIGNED;
    }

    @Nullable
    private ProfileResult fetchProfileUncached(UUID uUID, boolean bl) {
        try {
            URL uRL = HttpAuthenticationService.constantURL(this.baseUrl + "profile/" + UndashedUuid.toString(uUID));
            uRL = HttpAuthenticationService.concatenateURL(uRL, "unsigned=" + !bl);
            MinecraftProfilePropertiesResponse minecraftProfilePropertiesResponse = this.client.get(uRL, MinecraftProfilePropertiesResponse.class);
            if (minecraftProfilePropertiesResponse == null) {
                LOGGER.debug("Couldn't fetch profile properties for {} as the profile does not exist", (Object)uUID);
                return null;
            }
            GameProfile gameProfile = minecraftProfilePropertiesResponse.profile();
            Set<ProfileActionType> set = YggdrasilMinecraftSessionService.extractProfileActionTypes(minecraftProfilePropertiesResponse.profileActions());
            LOGGER.debug("Successfully fetched profile properties for {}", (Object)gameProfile);
            return new ProfileResult(gameProfile, set);
        }
        catch (MinecraftClientException | IllegalArgumentException runtimeException) {
            LOGGER.warn("Couldn't look up profile properties for {}", (Object)uUID, (Object)runtimeException);
            return null;
        }
    }

    private static Set<ProfileActionType> extractProfileActionTypes(Set<ProfileAction> set) {
        return set.stream().map(ProfileAction::type).collect(Collectors.toSet());
    }
}

