/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class NumberFormatTypes
/*    */ {
/* 15 */   public static final MapCodec<NumberFormat> MAP_CODEC = BuiltInRegistries.NUMBER_FORMAT_TYPE.byNameCodec().dispatchMap(NumberFormat::type, NumberFormatType::mapCodec);
/* 16 */   public static final Codec<NumberFormat> CODEC = MAP_CODEC.codec();
/*    */   
/* 18 */   public static final StreamCodec<RegistryFriendlyByteBuf, NumberFormat> STREAM_CODEC = ByteBufCodecs.registry(Registries.NUMBER_FORMAT_TYPE)
/* 19 */     .dispatch(NumberFormat::type, NumberFormatType::streamCodec);
/*    */   
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<NumberFormat>> OPTIONAL_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs::optional);
/*    */   
/*    */   public static NumberFormatType<?> bootstrap(Registry<NumberFormatType<?>> paramRegistry) {
/* 24 */     Registry.register(paramRegistry, "blank", BlankFormat.TYPE);
/* 25 */     Registry.register(paramRegistry, "styled", StyledFormat.TYPE);
/* 26 */     return (NumberFormatType)Registry.register(paramRegistry, "fixed", FixedFormat.TYPE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\numbers\NumberFormatTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */