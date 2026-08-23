/*   */ package net.minecraft.network;
/*   */ 
/*   */ import net.minecraft.network.protocol.PacketFlow;
/*   */ 
/*   */ public interface ClientboundPacketListener
/*   */   extends PacketListener {
/*   */   default PacketFlow flow() {
/* 8 */     return PacketFlow.CLIENTBOUND;
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\ClientboundPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */