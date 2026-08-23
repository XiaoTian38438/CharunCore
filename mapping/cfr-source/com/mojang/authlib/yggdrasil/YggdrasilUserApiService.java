/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 *  javax.annotation.Nullable
 */
package com.mojang.authlib.yggdrasil;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.Environment;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import com.mojang.authlib.minecraft.BanDetails;
import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.authlib.yggdrasil.YggdrassilTelemetrySession;
import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
import com.mojang.authlib.yggdrasil.response.BlockListResponse;
import com.mojang.authlib.yggdrasil.response.KeyPairResponse;
import com.mojang.authlib.yggdrasil.response.UserAttributesResponse;
import java.net.Proxy;
import java.net.URL;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class YggdrasilUserApiService
implements UserApiService {
    private static final long BLOCKLIST_REQUEST_COOLDOWN_SECONDS = 120L;
    private static final UUID ZERO_UUID = new UUID(0L, 0L);
    private final URL routePrivileges;
    private final URL routeBlocklist;
    private final URL routeKeyPair;
    private final URL routeAbuseReport;
    private final MinecraftClient minecraftClient;
    private final Environment environment;
    @Nullable
    private Instant nextAcceptableBlockRequest;
    @Nullable
    private Set<UUID> blockList;

    public YggdrasilUserApiService(String string, Proxy proxy, Environment environment) {
        this.minecraftClient = new MinecraftClient(string, proxy);
        this.environment = environment;
        this.routePrivileges = HttpAuthenticationService.constantURL(environment.servicesHost() + "/player/attributes");
        this.routeBlocklist = HttpAuthenticationService.constantURL(environment.servicesHost() + "/privacy/blocklist");
        this.routeKeyPair = HttpAuthenticationService.constantURL(environment.servicesHost() + "/player/certificates");
        this.routeAbuseReport = HttpAuthenticationService.constantURL(environment.servicesHost() + "/player/report");
    }

    @Override
    public TelemetrySession newTelemetrySession(Executor executor) {
        return new YggdrassilTelemetrySession(this.minecraftClient, this.environment, executor);
    }

    @Override
    public KeyPairResponse getKeyPair() {
        return this.minecraftClient.post(this.routeKeyPair, KeyPairResponse.class);
    }

    @Override
    public boolean isBlockedPlayer(UUID uUID) {
        if (uUID.equals(ZERO_UUID)) {
            return false;
        }
        if (this.blockList == null) {
            this.blockList = this.fetchBlockList();
            if (this.blockList == null) {
                return false;
            }
        }
        return this.blockList.contains(uUID);
    }

    @Override
    public void refreshBlockList() {
        if (this.blockList == null || this.canMakeBlockListRequest()) {
            this.blockList = this.forceFetchBlockList();
        }
    }

    @Nullable
    private Set<UUID> fetchBlockList() {
        if (!this.canMakeBlockListRequest()) {
            return null;
        }
        return this.forceFetchBlockList();
    }

    private boolean canMakeBlockListRequest() {
        return this.nextAcceptableBlockRequest == null || Instant.now().isAfter(this.nextAcceptableBlockRequest);
    }

    private Set<UUID> forceFetchBlockList() {
        this.nextAcceptableBlockRequest = Instant.now().plusSeconds(120L);
        try {
            BlockListResponse blockListResponse = this.minecraftClient.get(this.routeBlocklist, BlockListResponse.class);
            if (blockListResponse == null) {
                return Set.of();
            }
            return blockListResponse.blockedProfiles();
        }
        catch (MinecraftClientHttpException minecraftClientHttpException) {
            return null;
        }
        catch (MinecraftClientException minecraftClientException) {
            return null;
        }
    }

    @Override
    public UserApiService.UserProperties fetchProperties() throws AuthenticationException {
        try {
            UserAttributesResponse userAttributesResponse = this.minecraftClient.get(this.routePrivileges, UserAttributesResponse.class);
            ImmutableSet.Builder builder = ImmutableSet.builder();
            ImmutableMap.Builder builder2 = ImmutableMap.builder();
            if (userAttributesResponse != null) {
                UserAttributesResponse.ProfanityFilterPreferences profanityFilterPreferences;
                UserAttributesResponse.Privileges privileges = userAttributesResponse.privileges();
                if (privileges != null) {
                    YggdrasilUserApiService.addFlagIfUserHasPrivilege(privileges.getOnlineChat(), UserApiService.UserFlag.CHAT_ALLOWED, (ImmutableSet.Builder<UserApiService.UserFlag>)builder);
                    YggdrasilUserApiService.addFlagIfUserHasPrivilege(privileges.getMultiplayerServer(), UserApiService.UserFlag.SERVERS_ALLOWED, (ImmutableSet.Builder<UserApiService.UserFlag>)builder);
                    YggdrasilUserApiService.addFlagIfUserHasPrivilege(privileges.getMultiplayerRealms(), UserApiService.UserFlag.REALMS_ALLOWED, (ImmutableSet.Builder<UserApiService.UserFlag>)builder);
                    YggdrasilUserApiService.addFlagIfUserHasPrivilege(privileges.getTelemetry(), UserApiService.UserFlag.TELEMETRY_ENABLED, (ImmutableSet.Builder<UserApiService.UserFlag>)builder);
                    YggdrasilUserApiService.addFlagIfUserHasPrivilege(privileges.getOptionalTelemetry(), UserApiService.UserFlag.OPTIONAL_TELEMETRY_AVAILABLE, (ImmutableSet.Builder<UserApiService.UserFlag>)builder);
                }
                if ((profanityFilterPreferences = userAttributesResponse.profanityFilterPreferences()) != null && profanityFilterPreferences.enabled()) {
                    builder.add((Object)UserApiService.UserFlag.PROFANITY_FILTER_ENABLED);
                }
                if (userAttributesResponse.banStatus() != null) {
                    userAttributesResponse.banStatus().bannedScopes().forEach((string, bannedScope) -> builder2.put(string, (Object)new BanDetails(bannedScope.banId(), bannedScope.expires(), bannedScope.reason(), bannedScope.reasonMessage())));
                }
            }
            return new UserApiService.UserProperties((Set<UserApiService.UserFlag>)builder.build(), (Map<String, BanDetails>)builder2.build());
        }
        catch (MinecraftClientHttpException minecraftClientHttpException) {
            throw minecraftClientHttpException.toAuthenticationException();
        }
        catch (MinecraftClientException minecraftClientException) {
            throw minecraftClientException.toAuthenticationException();
        }
    }

    private static void addFlagIfUserHasPrivilege(boolean bl, UserApiService.UserFlag userFlag, ImmutableSet.Builder<UserApiService.UserFlag> builder) {
        if (bl) {
            builder.add((Object)userFlag);
        }
    }

    @Override
    public void reportAbuse(AbuseReportRequest abuseReportRequest) {
        this.minecraftClient.post(this.routeAbuseReport, abuseReportRequest, Void.class);
    }

    @Override
    public boolean canSendReports() {
        return true;
    }

    @Override
    public AbuseReportLimits getAbuseReportLimits() {
        return AbuseReportLimits.DEFAULTS;
    }
}

