package net.minecraft.server.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;

public interface ServerPlayerConnection {
  ServerPlayer getPlayer();
  
  void send(Packet<?> paramPacket);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\ServerPlayerConnection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */