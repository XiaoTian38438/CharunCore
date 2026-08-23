/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements NumberFormatType<FixedFormat>
/*    */ {
/* 14 */   private static final MapCodec<FixedFormat> CODEC = ComponentSerialization.CODEC.fieldOf("value").xmap(FixedFormat::new, FixedFormat::value);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   private static final StreamCodec<RegistryFriendlyByteBuf, FixedFormat> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, FixedFormat::value, FixedFormat::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MapCodec<FixedFormat> mapCodec() {
/* 26 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, FixedFormat> streamCodec() {
/* 31 */     return STREAM_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\numbers\FixedFormat$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */