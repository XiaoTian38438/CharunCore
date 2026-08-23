/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements ProtocolInfo.Details
/*    */ {
/*    */   public ConnectionProtocol id() {
/* 78 */     return protocol;
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketFlow flow() {
/* 83 */     return flow;
/*    */   }
/*    */ 
/*    */   
/*    */   public void listPackets(ProtocolInfo.Details.PacketVisitor paramPacketVisitor) {
/* 88 */     for (byte b = 0; b < codecs.size(); b++) {
/* 89 */       ProtocolInfoBuilder.CodecEntry codecEntry = codecs.get(b);
/* 90 */       paramPacketVisitor.accept(codecEntry.type, b);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\ProtocolInfoBuilder$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */