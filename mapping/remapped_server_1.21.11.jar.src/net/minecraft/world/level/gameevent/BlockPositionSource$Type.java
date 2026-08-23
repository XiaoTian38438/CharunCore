/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
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
/*    */ public class Type
/*    */   implements PositionSourceType<BlockPositionSource>
/*    */ {
/*    */   public MapCodec<BlockPositionSource> codec() {
/* 36 */     return BlockPositionSource.CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamCodec<ByteBuf, BlockPositionSource> streamCodec() {
/* 41 */     return BlockPositionSource.STREAM_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\BlockPositionSource$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */