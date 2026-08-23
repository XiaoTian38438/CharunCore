/*   */ package net.minecraft.network.protocol.login;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.cookie.ClientCookiePacketListener;
/*   */ 
/*   */ public interface ClientLoginPacketListener
/*   */   extends ClientCookiePacketListener {
/*   */   default ConnectionProtocol protocol() {
/* 9 */     return ConnectionProtocol.LOGIN;
/*   */   }
/*   */   
/*   */   void handleHello(ClientboundHelloPacket paramClientboundHelloPacket);
/*   */   
/*   */   void handleLoginFinished(ClientboundLoginFinishedPacket paramClientboundLoginFinishedPacket);
/*   */   
/*   */   void handleDisconnect(ClientboundLoginDisconnectPacket paramClientboundLoginDisconnectPacket);
/*   */   
/*   */   void handleCompression(ClientboundLoginCompressionPacket paramClientboundLoginCompressionPacket);
/*   */   
/*   */   void handleCustomQuery(ClientboundCustomQueryPacket paramClientboundCustomQueryPacket);
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\login\ClientLoginPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */