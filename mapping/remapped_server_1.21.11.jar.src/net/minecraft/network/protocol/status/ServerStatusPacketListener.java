/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.protocol.game.ServerPacketListener;
/*    */ import net.minecraft.network.protocol.ping.ServerPingPacketListener;
/*    */ 
/*    */ public interface ServerStatusPacketListener
/*    */   extends ServerPacketListener, ServerPingPacketListener {
/*    */   default ConnectionProtocol protocol() {
/* 10 */     return ConnectionProtocol.STATUS;
/*    */   }
/*    */   
/*    */   void handleStatusRequest(ServerboundStatusRequestPacket paramServerboundStatusRequestPacket);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\status\ServerStatusPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */