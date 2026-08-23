/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements NumberFormatType<StyledFormat>
/*    */ {
/* 15 */   private static final MapCodec<StyledFormat> CODEC = Style.Serializer.MAP_CODEC.xmap(StyledFormat::new, StyledFormat::style);
/*    */   
/* 17 */   private static final StreamCodec<RegistryFriendlyByteBuf, StyledFormat> STREAM_CODEC = StreamCodec.composite(Style.Serializer.TRUSTED_STREAM_CODEC, StyledFormat::style, StyledFormat::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MapCodec<StyledFormat> mapCodec() {
/* 24 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, StyledFormat> streamCodec() {
/* 29 */     return STREAM_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\numbers\StyledFormat$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */