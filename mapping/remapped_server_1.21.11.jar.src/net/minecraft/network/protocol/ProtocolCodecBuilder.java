/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.IdDispatchCodec;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class ProtocolCodecBuilder<B extends ByteBuf, L extends PacketListener> {
/*    */   private final IdDispatchCodec.Builder<B, Packet<? super L>, PacketType<? extends Packet<? super L>>> dispatchBuilder;
/*    */   private final PacketFlow flow;
/*    */   
/*    */   public ProtocolCodecBuilder(PacketFlow paramPacketFlow) {
/* 13 */     this.dispatchBuilder = IdDispatchCodec.builder(Packet::type);
/* 14 */     this.flow = paramPacketFlow;
/*    */   }
/*    */   
/*    */   public <T extends Packet<? super L>> ProtocolCodecBuilder<B, L> add(PacketType<T> paramPacketType, StreamCodec<? super B, T> paramStreamCodec) {
/* 18 */     if (paramPacketType.flow() != this.flow) {
/* 19 */       throw new IllegalArgumentException("Invalid packet flow for packet " + String.valueOf(paramPacketType) + ", expected " + this.flow.name());
/*    */     }
/* 21 */     this.dispatchBuilder.add(paramPacketType, paramStreamCodec);
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public StreamCodec<B, Packet<? super L>> build() {
/* 26 */     return (StreamCodec<B, Packet<? super L>>)this.dispatchBuilder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\ProtocolCodecBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */