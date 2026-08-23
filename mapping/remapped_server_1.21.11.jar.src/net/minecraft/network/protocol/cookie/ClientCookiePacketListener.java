package net.minecraft.network.protocol.cookie;

import net.minecraft.network.ClientboundPacketListener;

public interface ClientCookiePacketListener extends ClientboundPacketListener {
  void handleRequestCookie(ClientboundCookieRequestPacket paramClientboundCookieRequestPacket);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\cookie\ClientCookiePacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */