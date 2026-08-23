/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ 
/*    */ public abstract class BundlePacket<T extends PacketListener> implements Packet<T> {
/*    */   private final Iterable<Packet<? super T>> packets;
/*    */   
/*    */   protected BundlePacket(Iterable<Packet<? super T>> paramIterable) {
/*  9 */     this.packets = paramIterable;
/*    */   }
/*    */   
/*    */   public final Iterable<Packet<? super T>> subPackets() {
/* 13 */     return this.packets;
/*    */   }
/*    */   
/*    */   public abstract PacketType<? extends BundlePacket<T>> type();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\BundlePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */