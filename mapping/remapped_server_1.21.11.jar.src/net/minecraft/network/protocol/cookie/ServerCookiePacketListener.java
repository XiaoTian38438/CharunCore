package net.minecraft.network.protocol.cookie;

import net.minecraft.network.protocol.game.ServerPacketListener;

public interface ServerCookiePacketListener extends ServerPacketListener {
  void handleCookieResponse(ServerboundCookieResponsePacket paramServerboundCookieResponsePacket);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\cookie\ServerCookiePacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */