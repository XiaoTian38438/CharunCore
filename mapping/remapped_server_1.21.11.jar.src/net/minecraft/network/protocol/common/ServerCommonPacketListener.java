package net.minecraft.network.protocol.common;

import net.minecraft.network.protocol.cookie.ServerCookiePacketListener;

public interface ServerCommonPacketListener extends ServerCookiePacketListener {
  void handleKeepAlive(ServerboundKeepAlivePacket paramServerboundKeepAlivePacket);
  
  void handlePong(ServerboundPongPacket paramServerboundPongPacket);
  
  void handleCustomPayload(ServerboundCustomPayloadPacket paramServerboundCustomPayloadPacket);
  
  void handleResourcePackResponse(ServerboundResourcePackPacket paramServerboundResourcePackPacket);
  
  void handleClientInformation(ServerboundClientInformationPacket paramServerboundClientInformationPacket);
  
  void handleCustomClickAction(ServerboundCustomClickActionPacket paramServerboundCustomClickActionPacket);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\common\ServerCommonPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */