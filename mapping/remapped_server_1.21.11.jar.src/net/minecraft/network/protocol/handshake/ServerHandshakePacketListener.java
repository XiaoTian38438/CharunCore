/*   */ package net.minecraft.network.protocol.handshake;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.game.ServerPacketListener;
/*   */ 
/*   */ public interface ServerHandshakePacketListener
/*   */   extends ServerPacketListener {
/*   */   default ConnectionProtocol protocol() {
/* 9 */     return ConnectionProtocol.HANDSHAKING;
/*   */   }
/*   */   
/*   */   void handleIntention(ClientIntentionPacket paramClientIntentionPacket);
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\handshake\ServerHandshakePacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */