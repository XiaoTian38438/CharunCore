/*     */ package net.minecraft.server.jsonrpc.methods;
/*     */ 
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class ServerSettingsService
/*     */ {
/*     */   public static boolean autosave(MinecraftApi paramMinecraftApi) {
/*  12 */     return paramMinecraftApi.serverSettingsService().isAutoSave();
/*     */   }
/*     */   
/*     */   public static boolean setAutosave(MinecraftApi paramMinecraftApi, boolean paramBoolean, ClientInfo paramClientInfo) {
/*  16 */     return paramMinecraftApi.serverSettingsService().setAutoSave(paramBoolean, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static Difficulty difficulty(MinecraftApi paramMinecraftApi) {
/*  20 */     return paramMinecraftApi.serverSettingsService().getDifficulty();
/*     */   }
/*     */   
/*     */   public static Difficulty setDifficulty(MinecraftApi paramMinecraftApi, Difficulty paramDifficulty, ClientInfo paramClientInfo) {
/*  24 */     return paramMinecraftApi.serverSettingsService().setDifficulty(paramDifficulty, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static boolean enforceAllowlist(MinecraftApi paramMinecraftApi) {
/*  28 */     return paramMinecraftApi.serverSettingsService().isEnforceWhitelist();
/*     */   }
/*     */   
/*     */   public static boolean setEnforceAllowlist(MinecraftApi paramMinecraftApi, boolean paramBoolean, ClientInfo paramClientInfo) {
/*  32 */     return paramMinecraftApi.serverSettingsService().setEnforceWhitelist(paramBoolean, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static boolean usingAllowlist(MinecraftApi paramMinecraftApi) {
/*  36 */     return paramMinecraftApi.serverSettingsService().isUsingWhitelist();
/*     */   }
/*     */   
/*     */   public static boolean setUsingAllowlist(MinecraftApi paramMinecraftApi, boolean paramBoolean, ClientInfo paramClientInfo) {
/*  40 */     return paramMinecraftApi.serverSettingsService().setUsingWhitelist(paramBoolean, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static int maxPlayers(MinecraftApi paramMinecraftApi) {
/*  44 */     return paramMinecraftApi.serverSettingsService().getMaxPlayers();
/*     */   }
/*     */   
/*     */   public static int setMaxPlayers(MinecraftApi paramMinecraftApi, int paramInt, ClientInfo paramClientInfo) {
/*  48 */     return paramMinecraftApi.serverSettingsService().setMaxPlayers(paramInt, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static int pauseWhenEmpty(MinecraftApi paramMinecraftApi) {
/*  52 */     return paramMinecraftApi.serverSettingsService().getPauseWhenEmptySeconds();
/*     */   }
/*     */   
/*     */   public static int setPauseWhenEmpty(MinecraftApi paramMinecraftApi, int paramInt, ClientInfo paramClientInfo) {
/*  56 */     return paramMinecraftApi.serverSettingsService().setPauseWhenEmptySeconds(paramInt, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static int playerIdleTimeout(MinecraftApi paramMinecraftApi) {
/*  60 */     return paramMinecraftApi.serverSettingsService().getPlayerIdleTimeout();
/*     */   }
/*     */   
/*     */   public static int setPlayerIdleTimeout(MinecraftApi paramMinecraftApi, int paramInt, ClientInfo paramClientInfo) {
/*  64 */     return paramMinecraftApi.serverSettingsService().setPlayerIdleTimeout(paramInt, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static boolean allowFlight(MinecraftApi paramMinecraftApi) {
/*  68 */     return paramMinecraftApi.serverSettingsService().allowFlight();
/*     */   }
/*     */   
/*     */   public static boolean setAllowFlight(MinecraftApi paramMinecraftApi, boolean paramBoolean, ClientInfo paramClientInfo) {
/*  72 */     return paramMinecraftApi.serverSettingsService().setAllowFlight(paramBoolean, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static int spawnProtection(MinecraftApi paramMinecraftApi) {
/*  76 */     return paramMinecraftApi.serverSettingsService().getSpawnProtectionRadius();
/*     */   }
/*     */   
/*     */   public static int setSpawnProtection(MinecraftApi paramMinecraftApi, int paramInt, ClientInfo paramClientInfo) {
/*  80 */     return paramMinecraftApi.serverSettingsService().setSpawnProtectionRadius(paramInt, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static String motd(MinecraftApi paramMinecraftApi) {
/*  84 */     return paramMinecraftApi.serverSettingsService().getMotd();
/*     */   }
/*     */   
/*     */   public static String setMotd(MinecraftApi paramMinecraftApi, String paramString, ClientInfo paramClientInfo) {
/*  88 */     return paramMinecraftApi.serverSettingsService().setMotd(paramString, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static boolean forceGameMode(MinecraftApi paramMinecraftApi) {
/*  92 */     return paramMinecraftApi.serverSettingsService().forceGameMode();
/*     */   }
/*     */   
/*     */   public static boolean setForceGameMode(MinecraftApi paramMinecraftApi, boolean paramBoolean, ClientInfo paramClientInfo) {
/*  96 */     return paramMinecraftApi.serverSettingsService().setForceGameMode(paramBoolean, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static GameType gameMode(MinecraftApi paramMinecraftApi) {
/* 100 */     return paramMinecraftApi.serverSettingsService().getGameMode();
/*     */   }
/*     */   
/*     */   public static GameType setGameMode(MinecraftApi paramMinecraftApi, GameType paramGameType, ClientInfo paramClientInfo) {
/* 104 */     return paramMinecraftApi.serverSettingsService().setGameMode(paramGameType, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static int viewDistance(MinecraftApi paramMinecraftApi) {
/* 108 */     return paramMinecraftApi.serverSettingsService().getViewDistance();
/*     */   }
/*     */   
/*     */   public static int setViewDistance(MinecraftApi paramMinecraftApi, int paramInt, ClientInfo paramClientInfo) {
/* 112 */     return paramMinecraftApi.serverSettingsService().setViewDistance(paramInt, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static int simulationDistance(MinecraftApi paramMinecraftApi) {
/* 116 */     return paramMinecraftApi.serverSettingsService().getSimulationDistance();
/*     */   }
/*     */   
/*     */   public static int setSimulationDistance(MinecraftApi paramMinecraftApi, int paramInt, ClientInfo paramClientInfo) {
/* 120 */     return paramMinecraftApi.serverSettingsService().setSimulationDistance(paramInt, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static boolean acceptTransfers(MinecraftApi paramMinecraftApi) {
/* 124 */     return paramMinecraftApi.serverSettingsService().acceptsTransfers();
/*     */   }
/*     */   
/*     */   public static boolean setAcceptTransfers(MinecraftApi paramMinecraftApi, boolean paramBoolean, ClientInfo paramClientInfo) {
/* 128 */     return paramMinecraftApi.serverSettingsService().setAcceptsTransfers(paramBoolean, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static int statusHeartbeatInterval(MinecraftApi paramMinecraftApi) {
/* 132 */     return paramMinecraftApi.serverSettingsService().getStatusHeartbeatInterval();
/*     */   }
/*     */   
/*     */   public static int setStatusHeartbeatInterval(MinecraftApi paramMinecraftApi, int paramInt, ClientInfo paramClientInfo) {
/* 136 */     return paramMinecraftApi.serverSettingsService().setStatusHeartbeatInterval(paramInt, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static PermissionLevel operatorUserPermissionLevel(MinecraftApi paramMinecraftApi) {
/* 140 */     return paramMinecraftApi.serverSettingsService().getOperatorUserPermissions().level();
/*     */   }
/*     */   
/*     */   public static PermissionLevel setOperatorUserPermissionLevel(MinecraftApi paramMinecraftApi, PermissionLevel paramPermissionLevel, ClientInfo paramClientInfo) {
/* 144 */     return paramMinecraftApi.serverSettingsService().setOperatorUserPermissions(LevelBasedPermissionSet.forLevel(paramPermissionLevel), paramClientInfo).level();
/*     */   }
/*     */   
/*     */   public static boolean hidesOnlinePlayers(MinecraftApi paramMinecraftApi) {
/* 148 */     return paramMinecraftApi.serverSettingsService().hidesOnlinePlayers();
/*     */   }
/*     */   
/*     */   public static boolean setHidesOnlinePlayers(MinecraftApi paramMinecraftApi, boolean paramBoolean, ClientInfo paramClientInfo) {
/* 152 */     return paramMinecraftApi.serverSettingsService().setHidesOnlinePlayers(paramBoolean, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static boolean repliesToStatus(MinecraftApi paramMinecraftApi) {
/* 156 */     return paramMinecraftApi.serverSettingsService().repliesToStatus();
/*     */   }
/*     */   
/*     */   public static boolean setRepliesToStatus(MinecraftApi paramMinecraftApi, boolean paramBoolean, ClientInfo paramClientInfo) {
/* 160 */     return paramMinecraftApi.serverSettingsService().setRepliesToStatus(paramBoolean, paramClientInfo);
/*     */   }
/*     */   
/*     */   public static int entityBroadcastRangePercentage(MinecraftApi paramMinecraftApi) {
/* 164 */     return paramMinecraftApi.serverSettingsService().getEntityBroadcastRangePercentage();
/*     */   }
/*     */   
/*     */   public static int setEntityBroadcastRangePercentage(MinecraftApi paramMinecraftApi, int paramInt, ClientInfo paramClientInfo) {
/* 168 */     return paramMinecraftApi.serverSettingsService().setEntityBroadcastRangePercentage(paramInt, paramClientInfo);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\methods\ServerSettingsService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */