/*     */ package net.minecraft.server.jsonrpc.internalapi;
/*     */ 
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class MinecraftServerSettingsServiceImpl
/*     */   implements MinecraftServerSettingsService {
/*     */   private final DedicatedServer server;
/*     */   private final JsonRpcLogger jsonrpcLogger;
/*     */   
/*     */   public MinecraftServerSettingsServiceImpl(DedicatedServer paramDedicatedServer, JsonRpcLogger paramJsonRpcLogger) {
/*  16 */     this.server = paramDedicatedServer;
/*  17 */     this.jsonrpcLogger = paramJsonRpcLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAutoSave() {
/*  22 */     return this.server.isAutoSave();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setAutoSave(boolean paramBoolean, ClientInfo paramClientInfo) {
/*  27 */     this.jsonrpcLogger.log(paramClientInfo, "Update autosave from {} to {}", new Object[] { Boolean.valueOf(isAutoSave()), Boolean.valueOf(paramBoolean) });
/*  28 */     this.server.setAutoSave(paramBoolean);
/*  29 */     return isAutoSave();
/*     */   }
/*     */ 
/*     */   
/*     */   public Difficulty getDifficulty() {
/*  34 */     return this.server.getWorldData().getDifficulty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Difficulty setDifficulty(Difficulty paramDifficulty, ClientInfo paramClientInfo) {
/*  39 */     this.jsonrpcLogger.log(paramClientInfo, "Update difficulty from '{}' to '{}'", new Object[] { getDifficulty(), paramDifficulty });
/*  40 */     this.server.setDifficulty(paramDifficulty);
/*  41 */     return getDifficulty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnforceWhitelist() {
/*  46 */     return this.server.isEnforceWhitelist();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setEnforceWhitelist(boolean paramBoolean, ClientInfo paramClientInfo) {
/*  51 */     this.jsonrpcLogger.log(paramClientInfo, "Update enforce allowlist from {} to {}", new Object[] { Boolean.valueOf(isEnforceWhitelist()), Boolean.valueOf(paramBoolean) });
/*  52 */     this.server.setEnforceWhitelist(paramBoolean);
/*  53 */     this.server.kickUnlistedPlayers();
/*  54 */     return isEnforceWhitelist();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsingWhitelist() {
/*  59 */     return this.server.isUsingWhitelist();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setUsingWhitelist(boolean paramBoolean, ClientInfo paramClientInfo) {
/*  64 */     this.jsonrpcLogger.log(paramClientInfo, "Update using allowlist from {} to {}", new Object[] { Boolean.valueOf(isUsingWhitelist()), Boolean.valueOf(paramBoolean) });
/*  65 */     this.server.setUsingWhitelist(paramBoolean);
/*  66 */     this.server.kickUnlistedPlayers();
/*  67 */     return isUsingWhitelist();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPlayers() {
/*  72 */     return this.server.getMaxPlayers();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setMaxPlayers(int paramInt, ClientInfo paramClientInfo) {
/*  77 */     this.jsonrpcLogger.log(paramClientInfo, "Update max players from {} to {}", new Object[] { Integer.valueOf(getMaxPlayers()), Integer.valueOf(paramInt) });
/*  78 */     this.server.setMaxPlayers(paramInt);
/*  79 */     return getMaxPlayers();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPauseWhenEmptySeconds() {
/*  84 */     return this.server.pauseWhenEmptySeconds();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setPauseWhenEmptySeconds(int paramInt, ClientInfo paramClientInfo) {
/*  89 */     this.jsonrpcLogger.log(paramClientInfo, "Update pause when empty from {} seconds to {} seconds", new Object[] { Integer.valueOf(getPauseWhenEmptySeconds()), Integer.valueOf(paramInt) });
/*  90 */     this.server.setPauseWhenEmptySeconds(paramInt);
/*  91 */     return getPauseWhenEmptySeconds();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPlayerIdleTimeout() {
/*  96 */     return this.server.playerIdleTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setPlayerIdleTimeout(int paramInt, ClientInfo paramClientInfo) {
/* 101 */     this.jsonrpcLogger.log(paramClientInfo, "Update player idle timeout from {} minutes to {} minutes", new Object[] { Integer.valueOf(getPlayerIdleTimeout()), Integer.valueOf(paramInt) });
/* 102 */     this.server.setPlayerIdleTimeout(paramInt);
/* 103 */     return getPlayerIdleTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowFlight() {
/* 108 */     return this.server.allowFlight();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setAllowFlight(boolean paramBoolean, ClientInfo paramClientInfo) {
/* 113 */     this.jsonrpcLogger.log(paramClientInfo, "Update allow flight from {} to {}", new Object[] { Boolean.valueOf(allowFlight()), Boolean.valueOf(paramBoolean) });
/* 114 */     this.server.setAllowFlight(paramBoolean);
/* 115 */     return allowFlight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpawnProtectionRadius() {
/* 120 */     return this.server.spawnProtectionRadius();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setSpawnProtectionRadius(int paramInt, ClientInfo paramClientInfo) {
/* 125 */     this.jsonrpcLogger.log(paramClientInfo, "Update spawn protection radius from {} to {}", new Object[] { Integer.valueOf(getSpawnProtectionRadius()), Integer.valueOf(paramInt) });
/* 126 */     this.server.setSpawnProtectionRadius(paramInt);
/* 127 */     return getSpawnProtectionRadius();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMotd() {
/* 132 */     return this.server.getMotd();
/*     */   }
/*     */ 
/*     */   
/*     */   public String setMotd(String paramString, ClientInfo paramClientInfo) {
/* 137 */     this.jsonrpcLogger.log(paramClientInfo, "Update MOTD from '{}' to '{}'", new Object[] { getMotd(), paramString });
/* 138 */     this.server.setMotd(paramString);
/* 139 */     return getMotd();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean forceGameMode() {
/* 144 */     return this.server.forceGameMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setForceGameMode(boolean paramBoolean, ClientInfo paramClientInfo) {
/* 149 */     this.jsonrpcLogger.log(paramClientInfo, "Update force game mode from {} to {}", new Object[] { Boolean.valueOf(forceGameMode()), Boolean.valueOf(paramBoolean) });
/* 150 */     this.server.setForceGameMode(paramBoolean);
/* 151 */     return forceGameMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType getGameMode() {
/* 156 */     return this.server.gameMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType setGameMode(GameType paramGameType, ClientInfo paramClientInfo) {
/* 161 */     this.jsonrpcLogger.log(paramClientInfo, "Update game mode from '{}' to '{}'", new Object[] { getGameMode(), paramGameType });
/* 162 */     this.server.setGameMode(paramGameType);
/* 163 */     return getGameMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getViewDistance() {
/* 168 */     return this.server.viewDistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setViewDistance(int paramInt, ClientInfo paramClientInfo) {
/* 173 */     this.jsonrpcLogger.log(paramClientInfo, "Update view distance from {} to {}", new Object[] { Integer.valueOf(getViewDistance()), Integer.valueOf(paramInt) });
/* 174 */     this.server.setViewDistance(paramInt);
/* 175 */     return getViewDistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSimulationDistance() {
/* 180 */     return this.server.simulationDistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setSimulationDistance(int paramInt, ClientInfo paramClientInfo) {
/* 185 */     this.jsonrpcLogger.log(paramClientInfo, "Update simulation distance from {} to {}", new Object[] { Integer.valueOf(getSimulationDistance()), Integer.valueOf(paramInt) });
/* 186 */     this.server.setSimulationDistance(paramInt);
/* 187 */     return getSimulationDistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsTransfers() {
/* 192 */     return this.server.acceptsTransfers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setAcceptsTransfers(boolean paramBoolean, ClientInfo paramClientInfo) {
/* 197 */     this.jsonrpcLogger.log(paramClientInfo, "Update accepts transfers from {} to {}", new Object[] { Boolean.valueOf(acceptsTransfers()), Boolean.valueOf(paramBoolean) });
/* 198 */     this.server.setAcceptsTransfers(paramBoolean);
/* 199 */     return acceptsTransfers();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStatusHeartbeatInterval() {
/* 204 */     return this.server.statusHeartbeatInterval();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setStatusHeartbeatInterval(int paramInt, ClientInfo paramClientInfo) {
/* 209 */     this.jsonrpcLogger.log(paramClientInfo, "Update status heartbeat interval from {} to {}", new Object[] { Integer.valueOf(getStatusHeartbeatInterval()), Integer.valueOf(paramInt) });
/* 210 */     this.server.setStatusHeartbeatInterval(paramInt);
/* 211 */     return getStatusHeartbeatInterval();
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelBasedPermissionSet getOperatorUserPermissions() {
/* 216 */     return this.server.operatorUserPermissions();
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelBasedPermissionSet setOperatorUserPermissions(LevelBasedPermissionSet paramLevelBasedPermissionSet, ClientInfo paramClientInfo) {
/* 221 */     this.jsonrpcLogger.log(paramClientInfo, "Update operator user permission level from {} to {}", new Object[] { getOperatorUserPermissions(), paramLevelBasedPermissionSet.level() });
/* 222 */     this.server.setOperatorUserPermissions(paramLevelBasedPermissionSet);
/* 223 */     return getOperatorUserPermissions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hidesOnlinePlayers() {
/* 228 */     return this.server.hidesOnlinePlayers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setHidesOnlinePlayers(boolean paramBoolean, ClientInfo paramClientInfo) {
/* 233 */     this.jsonrpcLogger.log(paramClientInfo, "Update hides online players from {} to {}", new Object[] { Boolean.valueOf(hidesOnlinePlayers()), Boolean.valueOf(paramBoolean) });
/* 234 */     this.server.setHidesOnlinePlayers(paramBoolean);
/* 235 */     return hidesOnlinePlayers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean repliesToStatus() {
/* 240 */     return this.server.repliesToStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setRepliesToStatus(boolean paramBoolean, ClientInfo paramClientInfo) {
/* 245 */     this.jsonrpcLogger.log(paramClientInfo, "Update replies to status from {} to {}", new Object[] { Boolean.valueOf(repliesToStatus()), Boolean.valueOf(paramBoolean) });
/* 246 */     this.server.setRepliesToStatus(paramBoolean);
/* 247 */     return repliesToStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityBroadcastRangePercentage() {
/* 252 */     return this.server.entityBroadcastRangePercentage();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setEntityBroadcastRangePercentage(int paramInt, ClientInfo paramClientInfo) {
/* 257 */     this.jsonrpcLogger.log(paramClientInfo, "Update entity broadcast range percentage from {}% to {}%", new Object[] { Integer.valueOf(getEntityBroadcastRangePercentage()), Integer.valueOf(paramInt) });
/* 258 */     this.server.setEntityBroadcastRangePercentage(paramInt);
/* 259 */     return getEntityBroadcastRangePercentage();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftServerSettingsServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */