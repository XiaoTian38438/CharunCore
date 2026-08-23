/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.codec.StreamMemberEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Packet<T extends net.minecraft.network.PacketListener>
/*    */ {
/*    */   PacketType<? extends Packet<T>> type();
/*    */   
/*    */   void handle(T paramT);
/*    */   
/*    */   default boolean isSkippable() {
/* 21 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean isTerminal() {
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   static <B extends io.netty.buffer.ByteBuf, T extends Packet<?>> StreamCodec<B, T> codec(StreamMemberEncoder<B, T> paramStreamMemberEncoder, StreamDecoder<B, T> paramStreamDecoder) {
/* 34 */     return StreamCodec.ofMember(paramStreamMemberEncoder, paramStreamDecoder);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\Packet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */