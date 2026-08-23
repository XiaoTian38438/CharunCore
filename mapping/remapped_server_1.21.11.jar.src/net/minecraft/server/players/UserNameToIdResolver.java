package net.minecraft.server.players;

import java.util.Optional;
import java.util.UUID;

public interface UserNameToIdResolver {
  void add(NameAndId paramNameAndId);
  
  Optional<NameAndId> get(String paramString);
  
  Optional<NameAndId> get(UUID paramUUID);
  
  void resolveOfflineUsers(boolean paramBoolean);
  
  void save();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\UserNameToIdResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */