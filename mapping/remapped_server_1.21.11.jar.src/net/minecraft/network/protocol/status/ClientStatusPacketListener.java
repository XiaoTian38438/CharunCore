/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import net.minecraft.network.ClientboundPacketListener;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.protocol.ping.ClientPongPacketListener;
/*    */ 
/*    */ public interface ClientStatusPacketListener
/*    */   extends ClientPongPacketListener, ClientboundPacketListener {
/*    */   default ConnectionProtocol protocol() {
/* 10 */     return ConnectionProtocol.STATUS;
/*    */   }
/*    */   
/*    */   void handleStatusResponse(ClientboundStatusResponsePacket paramClientboundStatusResponsePacket);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\status\ClientStatusPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */