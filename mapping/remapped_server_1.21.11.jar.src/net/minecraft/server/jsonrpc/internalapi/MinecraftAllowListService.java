package net.minecraft.server.jsonrpc.internalapi;

import java.util.Collection;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserWhiteListEntry;

public interface MinecraftAllowListService {
  Collection<UserWhiteListEntry> getEntries();
  
  boolean add(UserWhiteListEntry paramUserWhiteListEntry, ClientInfo paramClientInfo);
  
  void clear(ClientInfo paramClientInfo);
  
  void remove(NameAndId paramNameAndId, ClientInfo paramClientInfo);
  
  void kickUnlistedPlayers(ClientInfo paramClientInfo);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftAllowListService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */