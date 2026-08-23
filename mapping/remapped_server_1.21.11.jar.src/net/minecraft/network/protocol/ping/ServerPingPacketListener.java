package net.minecraft.network.protocol.ping;

import net.minecraft.network.PacketListener;

public interface ServerPingPacketListener extends PacketListener {
  void handlePingRequest(ServerboundPingRequestPacket paramServerboundPingRequestPacket);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\ping\ServerPingPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */