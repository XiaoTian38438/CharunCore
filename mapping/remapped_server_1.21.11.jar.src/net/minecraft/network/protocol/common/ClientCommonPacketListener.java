package net.minecraft.network.protocol.common;

import net.minecraft.network.protocol.cookie.ClientCookiePacketListener;

public interface ClientCommonPacketListener extends ClientCookiePacketListener {
  void handleKeepAlive(ClientboundKeepAlivePacket paramClientboundKeepAlivePacket);
  
  void handlePing(ClientboundPingPacket paramClientboundPingPacket);
  
  void handleCustomPayload(ClientboundCustomPayloadPacket paramClientboundCustomPayloadPacket);
  
  void handleDisconnect(ClientboundDisconnectPacket paramClientboundDisconnectPacket);
  
  void handleResourcePackPush(ClientboundResourcePackPushPacket paramClientboundResourcePackPushPacket);
  
  void handleResourcePackPop(ClientboundResourcePackPopPacket paramClientboundResourcePackPopPacket);
  
  void handleUpdateTags(ClientboundUpdateTagsPacket paramClientboundUpdateTagsPacket);
  
  void handleStoreCookie(ClientboundStoreCookiePacket paramClientboundStoreCookiePacket);
  
  void handleTransfer(ClientboundTransferPacket paramClientboundTransferPacket);
  
  void handleCustomReportDetails(ClientboundCustomReportDetailsPacket paramClientboundCustomReportDetailsPacket);
  
  void handleServerLinks(ClientboundServerLinksPacket paramClientboundServerLinksPacket);
  
  void handleClearDialog(ClientboundClearDialogPacket paramClientboundClearDialogPacket);
  
  void handleShowDialog(ClientboundShowDialogPacket paramClientboundShowDialogPacket);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\common\ClientCommonPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */