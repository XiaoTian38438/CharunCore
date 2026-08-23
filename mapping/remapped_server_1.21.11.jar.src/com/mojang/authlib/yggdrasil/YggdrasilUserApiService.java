/*     */ package com.mojang.authlib.yggdrasil;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.authlib.Environment;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.MinecraftClientException;
/*     */ import com.mojang.authlib.exceptions.MinecraftClientHttpException;
/*     */ import com.mojang.authlib.minecraft.BanDetails;
/*     */ import com.mojang.authlib.minecraft.TelemetrySession;
/*     */ import com.mojang.authlib.minecraft.UserApiService;
/*     */ import com.mojang.authlib.minecraft.client.MinecraftClient;
/*     */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*     */ import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
/*     */ import com.mojang.authlib.yggdrasil.response.BlockListResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.KeyPairResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.UserAttributesResponse;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.time.Instant;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public class YggdrasilUserApiService implements UserApiService {
/*     */   private static final long BLOCKLIST_REQUEST_COOLDOWN_SECONDS = 120L;
/*  30 */   private static final UUID ZERO_UUID = new UUID(0L, 0L);
/*     */   
/*     */   private final URL routePrivileges;
/*     */   
/*     */   private final URL routeBlocklist;
/*     */   
/*     */   private final URL routeKeyPair;
/*     */   private final URL routeAbuseReport;
/*     */   private final MinecraftClient minecraftClient;
/*     */   private final Environment environment;
/*     */   @Nullable
/*     */   private Instant nextAcceptableBlockRequest;
/*     */   @Nullable
/*     */   private Set<UUID> blockList;
/*     */   
/*     */   public YggdrasilUserApiService(String paramString, Proxy paramProxy, Environment paramEnvironment) {
/*  46 */     this.minecraftClient = new MinecraftClient(paramString, paramProxy);
/*  47 */     this.environment = paramEnvironment;
/*  48 */     this.routePrivileges = HttpAuthenticationService.constantURL(paramEnvironment.servicesHost() + "/player/attributes");
/*  49 */     this.routeBlocklist = HttpAuthenticationService.constantURL(paramEnvironment.servicesHost() + "/privacy/blocklist");
/*  50 */     this.routeKeyPair = HttpAuthenticationService.constantURL(paramEnvironment.servicesHost() + "/player/certificates");
/*  51 */     this.routeAbuseReport = HttpAuthenticationService.constantURL(paramEnvironment.servicesHost() + "/player/report");
/*     */   }
/*     */ 
/*     */   
/*     */   public TelemetrySession newTelemetrySession(Executor paramExecutor) {
/*  56 */     return new YggdrassilTelemetrySession(this.minecraftClient, this.environment, paramExecutor);
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyPairResponse getKeyPair() {
/*  61 */     return (KeyPairResponse)this.minecraftClient.post(this.routeKeyPair, KeyPairResponse.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBlockedPlayer(UUID paramUUID) {
/*  66 */     if (paramUUID.equals(ZERO_UUID)) {
/*  67 */       return false;
/*     */     }
/*     */     
/*  70 */     if (this.blockList == null) {
/*  71 */       this.blockList = fetchBlockList();
/*  72 */       if (this.blockList == null) {
/*  73 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  77 */     return this.blockList.contains(paramUUID);
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshBlockList() {
/*  82 */     if (this.blockList == null || canMakeBlockListRequest()) {
/*  83 */       this.blockList = forceFetchBlockList();
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Set<UUID> fetchBlockList() {
/*  89 */     if (!canMakeBlockListRequest()) {
/*  90 */       return null;
/*     */     }
/*  92 */     return forceFetchBlockList();
/*     */   }
/*     */   
/*     */   private boolean canMakeBlockListRequest() {
/*  96 */     return (this.nextAcceptableBlockRequest == null || Instant.now().isAfter(this.nextAcceptableBlockRequest));
/*     */   }
/*     */   
/*     */   private Set<UUID> forceFetchBlockList() {
/* 100 */     this.nextAcceptableBlockRequest = Instant.now().plusSeconds(120L);
/*     */     try {
/* 102 */       BlockListResponse blockListResponse = (BlockListResponse)this.minecraftClient.get(this.routeBlocklist, BlockListResponse.class);
/* 103 */       if (blockListResponse == null) {
/* 104 */         return Set.of();
/*     */       }
/* 106 */       return blockListResponse.blockedProfiles();
/* 107 */     } catch (MinecraftClientHttpException minecraftClientHttpException) {
/*     */ 
/*     */       
/* 110 */       return null;
/* 111 */     } catch (MinecraftClientException minecraftClientException) {
/*     */ 
/*     */       
/* 114 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public UserApiService.UserProperties fetchProperties() throws AuthenticationException {
/*     */     try {
/* 121 */       UserAttributesResponse userAttributesResponse = (UserAttributesResponse)this.minecraftClient.get(this.routePrivileges, UserAttributesResponse.class);
/* 122 */       ImmutableSet.Builder<UserApiService.UserFlag> builder = ImmutableSet.builder();
/* 123 */       ImmutableMap.Builder builder1 = ImmutableMap.builder();
/*     */       
/* 125 */       if (userAttributesResponse != null) {
/* 126 */         UserAttributesResponse.Privileges privileges = userAttributesResponse.privileges();
/* 127 */         if (privileges != null) {
/* 128 */           addFlagIfUserHasPrivilege(privileges.getOnlineChat(), UserApiService.UserFlag.CHAT_ALLOWED, builder);
/* 129 */           addFlagIfUserHasPrivilege(privileges.getMultiplayerServer(), UserApiService.UserFlag.SERVERS_ALLOWED, builder);
/* 130 */           addFlagIfUserHasPrivilege(privileges.getMultiplayerRealms(), UserApiService.UserFlag.REALMS_ALLOWED, builder);
/* 131 */           addFlagIfUserHasPrivilege(privileges.getTelemetry(), UserApiService.UserFlag.TELEMETRY_ENABLED, builder);
/* 132 */           addFlagIfUserHasPrivilege(privileges.getOptionalTelemetry(), UserApiService.UserFlag.OPTIONAL_TELEMETRY_AVAILABLE, builder);
/*     */         } 
/*     */         
/* 135 */         UserAttributesResponse.ProfanityFilterPreferences profanityFilterPreferences = userAttributesResponse.profanityFilterPreferences();
/* 136 */         if (profanityFilterPreferences != null && profanityFilterPreferences.enabled()) {
/* 137 */           builder.add(UserApiService.UserFlag.PROFANITY_FILTER_ENABLED);
/*     */         }
/*     */         
/* 140 */         if (userAttributesResponse.banStatus() != null) {
/* 141 */           userAttributesResponse.banStatus().bannedScopes().forEach((paramString, paramBannedScope) -> paramBuilder.put(paramString, new BanDetails(paramBannedScope.banId(), paramBannedScope.expires(), paramBannedScope.reason(), paramBannedScope.reasonMessage())));
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 147 */       return new UserApiService.UserProperties((Set)builder.build(), (Map)builder1.build());
/* 148 */     } catch (MinecraftClientHttpException minecraftClientHttpException) {
/*     */       
/* 150 */       throw minecraftClientHttpException.toAuthenticationException();
/* 151 */     } catch (MinecraftClientException minecraftClientException) {
/*     */ 
/*     */       
/* 154 */       throw minecraftClientException.toAuthenticationException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void addFlagIfUserHasPrivilege(boolean paramBoolean, UserApiService.UserFlag paramUserFlag, ImmutableSet.Builder<UserApiService.UserFlag> paramBuilder) {
/* 159 */     if (paramBoolean) {
/* 160 */       paramBuilder.add(paramUserFlag);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void reportAbuse(AbuseReportRequest paramAbuseReportRequest) {
/* 166 */     this.minecraftClient.post(this.routeAbuseReport, paramAbuseReportRequest, Void.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSendReports() {
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbuseReportLimits getAbuseReportLimits() {
/* 176 */     return AbuseReportLimits.DEFAULTS;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\YggdrasilUserApiService.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */