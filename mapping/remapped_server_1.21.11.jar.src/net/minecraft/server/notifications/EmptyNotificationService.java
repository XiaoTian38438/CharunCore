package net.minecraft.server.notifications;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.IpBanListEntry;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.level.gamerules.GameRule;

public class EmptyNotificationService implements NotificationService {
  public void playerJoined(ServerPlayer paramServerPlayer) {}
  
  public void playerLeft(ServerPlayer paramServerPlayer) {}
  
  public void serverStarted() {}
  
  public void serverShuttingDown() {}
  
  public void serverSaveStarted() {}
  
  public void serverSaveCompleted() {}
  
  public void serverActivityOccured() {}
  
  public void playerOped(ServerOpListEntry paramServerOpListEntry) {}
  
  public void playerDeoped(ServerOpListEntry paramServerOpListEntry) {}
  
  public void playerAddedToAllowlist(NameAndId paramNameAndId) {}
  
  public void playerRemovedFromAllowlist(NameAndId paramNameAndId) {}
  
  public void ipBanned(IpBanListEntry paramIpBanListEntry) {}
  
  public void ipUnbanned(String paramString) {}
  
  public void playerBanned(UserBanListEntry paramUserBanListEntry) {}
  
  public void playerUnbanned(NameAndId paramNameAndId) {}
  
  public <T> void onGameRuleChanged(GameRule<T> paramGameRule, T paramT) {}
  
  public void statusHeartbeat() {}
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\notifications\EmptyNotificationService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */