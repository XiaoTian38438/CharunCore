package net.minecraft.server.jsonrpc.internalapi;

import java.util.Collection;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.players.IpBanListEntry;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserBanListEntry;

public interface MinecraftBanListService {
  void addUserBan(UserBanListEntry paramUserBanListEntry, ClientInfo paramClientInfo);
  
  void removeUserBan(NameAndId paramNameAndId, ClientInfo paramClientInfo);
  
  Collection<UserBanListEntry> getUserBanEntries();
  
  Collection<IpBanListEntry> getIpBanEntries();
  
  void addIpBan(IpBanListEntry paramIpBanListEntry, ClientInfo paramClientInfo);
  
  void clearIpBans(ClientInfo paramClientInfo);
  
  void removeIpBan(String paramString, ClientInfo paramClientInfo);
  
  void clearUserBans(ClientInfo paramClientInfo);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftBanListService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */