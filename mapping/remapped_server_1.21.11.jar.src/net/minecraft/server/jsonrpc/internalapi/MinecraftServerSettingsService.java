package net.minecraft.server.jsonrpc.internalapi;

import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;

public interface MinecraftServerSettingsService {
  boolean isAutoSave();
  
  boolean setAutoSave(boolean paramBoolean, ClientInfo paramClientInfo);
  
  Difficulty getDifficulty();
  
  Difficulty setDifficulty(Difficulty paramDifficulty, ClientInfo paramClientInfo);
  
  boolean isEnforceWhitelist();
  
  boolean setEnforceWhitelist(boolean paramBoolean, ClientInfo paramClientInfo);
  
  boolean isUsingWhitelist();
  
  boolean setUsingWhitelist(boolean paramBoolean, ClientInfo paramClientInfo);
  
  int getMaxPlayers();
  
  int setMaxPlayers(int paramInt, ClientInfo paramClientInfo);
  
  int getPauseWhenEmptySeconds();
  
  int setPauseWhenEmptySeconds(int paramInt, ClientInfo paramClientInfo);
  
  int getPlayerIdleTimeout();
  
  int setPlayerIdleTimeout(int paramInt, ClientInfo paramClientInfo);
  
  boolean allowFlight();
  
  boolean setAllowFlight(boolean paramBoolean, ClientInfo paramClientInfo);
  
  int getSpawnProtectionRadius();
  
  int setSpawnProtectionRadius(int paramInt, ClientInfo paramClientInfo);
  
  String getMotd();
  
  String setMotd(String paramString, ClientInfo paramClientInfo);
  
  boolean forceGameMode();
  
  boolean setForceGameMode(boolean paramBoolean, ClientInfo paramClientInfo);
  
  GameType getGameMode();
  
  GameType setGameMode(GameType paramGameType, ClientInfo paramClientInfo);
  
  int getViewDistance();
  
  int setViewDistance(int paramInt, ClientInfo paramClientInfo);
  
  int getSimulationDistance();
  
  int setSimulationDistance(int paramInt, ClientInfo paramClientInfo);
  
  boolean acceptsTransfers();
  
  boolean setAcceptsTransfers(boolean paramBoolean, ClientInfo paramClientInfo);
  
  int getStatusHeartbeatInterval();
  
  int setStatusHeartbeatInterval(int paramInt, ClientInfo paramClientInfo);
  
  LevelBasedPermissionSet getOperatorUserPermissions();
  
  LevelBasedPermissionSet setOperatorUserPermissions(LevelBasedPermissionSet paramLevelBasedPermissionSet, ClientInfo paramClientInfo);
  
  boolean hidesOnlinePlayers();
  
  boolean setHidesOnlinePlayers(boolean paramBoolean, ClientInfo paramClientInfo);
  
  boolean repliesToStatus();
  
  boolean setRepliesToStatus(boolean paramBoolean, ClientInfo paramClientInfo);
  
  int getEntityBroadcastRangePercentage();
  
  int setEntityBroadcastRangePercentage(int paramInt, ClientInfo paramClientInfo);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftServerSettingsService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */