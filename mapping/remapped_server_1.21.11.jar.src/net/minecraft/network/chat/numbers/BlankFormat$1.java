/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements NumberFormatType<BlankFormat>
/*    */ {
/* 13 */   private static final MapCodec<BlankFormat> CODEC = MapCodec.unit(BlankFormat.INSTANCE);
/*    */   
/* 15 */   private static final StreamCodec<RegistryFriendlyByteBuf, BlankFormat> STREAM_CODEC = StreamCodec.unit(BlankFormat.INSTANCE);
/*    */ 
/*    */   
/*    */   public MapCodec<BlankFormat> mapCodec() {
/* 19 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, BlankFormat> streamCodec() {
/* 24 */     return STREAM_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\numbers\BlankFormat$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */