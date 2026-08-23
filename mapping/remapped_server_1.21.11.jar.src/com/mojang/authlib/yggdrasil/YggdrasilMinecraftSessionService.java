/*     */ package com.mojang.authlib.yggdrasil;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.authlib.Environment;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.SignatureState;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.exceptions.MinecraftClientException;
/*     */ import com.mojang.authlib.minecraft.InsecurePublicKeyException;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTextures;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import com.mojang.authlib.minecraft.client.MinecraftClient;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.authlib.yggdrasil.request.JoinMinecraftServerRequest;
/*     */ import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
/*     */ import com.mojang.authlib.yggdrasil.response.ProfileAction;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Base64;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class YggdrasilMinecraftSessionService
/*     */   implements MinecraftSessionService
/*     */ {
/*  50 */   private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilMinecraftSessionService.class);
/*     */   
/*     */   private final MinecraftClient client;
/*     */   private final ServicesKeySet servicesKeySet;
/*     */   private final String baseUrl;
/*     */   private final URL joinUrl;
/*     */   private final URL checkUrl;
/*  57 */   private final Gson gson = (new GsonBuilder()).registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
/*     */   
/*  59 */   private final LoadingCache<UUID, Optional<ProfileResult>> insecureProfiles = CacheBuilder.newBuilder()
/*  60 */     .expireAfterWrite(6L, TimeUnit.HOURS)
/*  61 */     .build(new CacheLoader<UUID, Optional<ProfileResult>>()
/*     */       {
/*     */         public Optional<ProfileResult> load(UUID param1UUID) {
/*  64 */           return Optional.ofNullable(YggdrasilMinecraftSessionService.this.fetchProfileUncached(param1UUID, false));
/*     */         }
/*     */       });
/*     */   
/*     */   protected YggdrasilMinecraftSessionService(ServicesKeySet paramServicesKeySet, Proxy paramProxy, Environment paramEnvironment) {
/*  69 */     this.client = MinecraftClient.unauthenticated(paramProxy);
/*  70 */     this.servicesKeySet = paramServicesKeySet;
/*  71 */     this.baseUrl = paramEnvironment.sessionHost() + "/session/minecraft/";
/*     */     
/*  73 */     this.joinUrl = HttpAuthenticationService.constantURL(this.baseUrl + "join");
/*  74 */     this.checkUrl = HttpAuthenticationService.constantURL(this.baseUrl + "hasJoined");
/*     */   }
/*     */ 
/*     */   
/*     */   public void joinServer(UUID paramUUID, String paramString1, String paramString2) throws AuthenticationException {
/*  79 */     JoinMinecraftServerRequest joinMinecraftServerRequest = new JoinMinecraftServerRequest(paramString1, paramUUID, paramString2);
/*     */     try {
/*  81 */       this.client.post(this.joinUrl, joinMinecraftServerRequest, Void.class);
/*  82 */     } catch (MinecraftClientException minecraftClientException) {
/*  83 */       throw minecraftClientException.toAuthenticationException();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ProfileResult hasJoinedServer(String paramString1, String paramString2, @Nullable InetAddress paramInetAddress) throws AuthenticationUnavailableException {
/*  90 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/*  92 */     hashMap.put("username", paramString1);
/*  93 */     hashMap.put("serverId", paramString2);
/*     */     
/*  95 */     if (paramInetAddress != null) {
/*  96 */       hashMap.put("ip", paramInetAddress.getHostAddress());
/*     */     }
/*     */     
/*  99 */     URL uRL = HttpAuthenticationService.concatenateURL(this.checkUrl, HttpAuthenticationService.buildQuery(hashMap));
/*     */     
/*     */     try {
/* 102 */       HasJoinedMinecraftServerResponse hasJoinedMinecraftServerResponse = (HasJoinedMinecraftServerResponse)this.client.get(uRL, HasJoinedMinecraftServerResponse.class);
/* 103 */       if (hasJoinedMinecraftServerResponse != null && hasJoinedMinecraftServerResponse.id() != null) {
/* 104 */         GameProfile gameProfile = new GameProfile(hasJoinedMinecraftServerResponse.id(), paramString1, Objects.<PropertyMap>requireNonNullElse(hasJoinedMinecraftServerResponse.properties(), PropertyMap.EMPTY));
/*     */         
/* 106 */         Set<ProfileActionType> set = extractProfileActionTypes(hasJoinedMinecraftServerResponse.profileActions());
/* 107 */         return new ProfileResult(gameProfile, set);
/*     */       } 
/* 109 */       return null;
/*     */     }
/* 111 */     catch (MinecraftClientException minecraftClientException) {
/* 112 */       AuthenticationException authenticationException = minecraftClientException.toAuthenticationException(); if (authenticationException instanceof AuthenticationUnavailableException) { AuthenticationUnavailableException authenticationUnavailableException = (AuthenticationUnavailableException)authenticationException;
/* 113 */         throw authenticationUnavailableException; }
/*     */       
/* 115 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Property getPackedTextures(GameProfile paramGameProfile) {
/* 122 */     return (Property)Iterables.getFirst(paramGameProfile.properties().get("textures"), null);
/*     */   }
/*     */   
/*     */   public MinecraftProfileTextures unpackTextures(Property paramProperty) {
/*     */     MinecraftTexturesPayload minecraftTexturesPayload;
/* 127 */     String str = paramProperty.value();
/* 128 */     SignatureState signatureState = getPropertySignatureState(paramProperty);
/*     */ 
/*     */     
/*     */     try {
/* 132 */       String str1 = new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
/* 133 */       minecraftTexturesPayload = (MinecraftTexturesPayload)this.gson.fromJson(str1, MinecraftTexturesPayload.class);
/* 134 */     } catch (JsonParseException|IllegalArgumentException jsonParseException) {
/* 135 */       LOGGER.error("Could not decode textures payload", (Throwable)jsonParseException);
/* 136 */       return MinecraftProfileTextures.EMPTY;
/*     */     } 
/*     */     
/* 139 */     if (minecraftTexturesPayload == null || minecraftTexturesPayload.textures() == null || minecraftTexturesPayload.textures().isEmpty()) {
/* 140 */       return MinecraftProfileTextures.EMPTY;
/*     */     }
/*     */     
/* 143 */     Map map = minecraftTexturesPayload.textures();
/* 144 */     for (Map.Entry entry : map.entrySet()) {
/* 145 */       String str1 = ((MinecraftProfileTexture)entry.getValue()).getUrl();
/* 146 */       if (str1 == null || !TextureUrlChecker.isAllowedTextureDomain(str1)) {
/* 147 */         LOGGER.error("Textures payload url is invalid: {}", str1);
/* 148 */         return MinecraftProfileTextures.EMPTY;
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     return new MinecraftProfileTextures((MinecraftProfileTexture)map
/* 153 */         .get(MinecraftProfileTexture.Type.SKIN), (MinecraftProfileTexture)map
/* 154 */         .get(MinecraftProfileTexture.Type.CAPE), (MinecraftProfileTexture)map
/* 155 */         .get(MinecraftProfileTexture.Type.ELYTRA), signatureState);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ProfileResult fetchProfile(UUID paramUUID, boolean paramBoolean) {
/* 163 */     if (!paramBoolean) {
/* 164 */       return ((Optional<ProfileResult>)this.insecureProfiles.getUnchecked(paramUUID)).orElse(null);
/*     */     }
/*     */     
/* 167 */     return fetchProfileUncached(paramUUID, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSecurePropertyValue(Property paramProperty) throws InsecurePublicKeyException {
/* 172 */     switch (getPropertySignatureState(paramProperty)) { default: throw new IncompatibleClassChangeError();
/*     */       case UNSIGNED:
/* 174 */         throw new InsecurePublicKeyException.MissingException("Missing signature from \"" + paramProperty.name() + "\"");
/*     */       case INVALID:
/* 176 */         throw new InsecurePublicKeyException.InvalidException("Property \"" + paramProperty.name() + "\" has been tampered with (signature invalid)");
/* 177 */       case SIGNED: break; }  return paramProperty.value();
/*     */   }
/*     */ 
/*     */   
/*     */   private SignatureState getPropertySignatureState(Property paramProperty) {
/* 182 */     if (!paramProperty.hasSignature()) {
/* 183 */       return SignatureState.UNSIGNED;
/*     */     }
/* 185 */     if (this.servicesKeySet.keys(ServicesKeyType.PROFILE_PROPERTY).stream().noneMatch(paramServicesKeyInfo -> paramServicesKeyInfo.validateProperty(paramProperty))) {
/* 186 */       return SignatureState.INVALID;
/*     */     }
/* 188 */     return SignatureState.SIGNED;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private ProfileResult fetchProfileUncached(UUID paramUUID, boolean paramBoolean) {
/*     */     try {
/* 194 */       URL uRL = HttpAuthenticationService.constantURL(this.baseUrl + "profile/" + this.baseUrl);
/* 195 */       uRL = HttpAuthenticationService.concatenateURL(uRL, "unsigned=" + (!paramBoolean ? 1 : 0));
/*     */       
/* 197 */       MinecraftProfilePropertiesResponse minecraftProfilePropertiesResponse = (MinecraftProfilePropertiesResponse)this.client.get(uRL, MinecraftProfilePropertiesResponse.class);
/* 198 */       if (minecraftProfilePropertiesResponse == null) {
/* 199 */         LOGGER.debug("Couldn't fetch profile properties for {} as the profile does not exist", paramUUID);
/* 200 */         return null;
/*     */       } 
/*     */       
/* 203 */       GameProfile gameProfile = minecraftProfilePropertiesResponse.profile();
/* 204 */       Set<ProfileActionType> set = extractProfileActionTypes(minecraftProfilePropertiesResponse.profileActions());
/*     */       
/* 206 */       LOGGER.debug("Successfully fetched profile properties for {}", gameProfile);
/* 207 */       return new ProfileResult(gameProfile, set);
/* 208 */     } catch (MinecraftClientException|IllegalArgumentException minecraftClientException) {
/* 209 */       LOGGER.warn("Couldn't look up profile properties for {}", paramUUID, minecraftClientException);
/* 210 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Set<ProfileActionType> extractProfileActionTypes(Set<ProfileAction> paramSet) {
/* 215 */     return (Set<ProfileActionType>)paramSet.stream()
/* 216 */       .map(ProfileAction::type)
/* 217 */       .collect(Collectors.toSet());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\YggdrasilMinecraftSessionService.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */