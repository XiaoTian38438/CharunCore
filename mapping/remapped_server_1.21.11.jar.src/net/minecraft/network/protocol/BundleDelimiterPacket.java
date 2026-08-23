/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BundleDelimiterPacket<T extends PacketListener>
/*    */   implements Packet<T>
/*    */ {
/*    */   public final void handle(T paramT) {
/* 11 */     throw new AssertionError("This packet should be handled by pipeline");
/*    */   }
/*    */   
/*    */   public abstract PacketType<? extends BundleDelimiterPacket<T>> type();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\BundleDelimiterPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */