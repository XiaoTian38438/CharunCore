/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ public interface EnchantmentValueEffect
/*    */ {
/*    */   static MapCodec<? extends EnchantmentValueEffect> bootstrap(Registry<MapCodec<? extends EnchantmentValueEffect>> paramRegistry) {
/* 14 */     Registry.register(paramRegistry, "add", AddValue.CODEC);
/* 15 */     Registry.register(paramRegistry, "all_of", AllOf.ValueEffects.CODEC);
/* 16 */     Registry.register(paramRegistry, "multiply", MultiplyValue.CODEC);
/* 17 */     Registry.register(paramRegistry, "remove_binomial", RemoveBinomial.CODEC);
/* 18 */     Registry.register(paramRegistry, "exponential", ScaleExponentially.CODEC);
/* 19 */     return (MapCodec<? extends EnchantmentValueEffect>)Registry.register(paramRegistry, "set", SetValue.CODEC);
/*    */   }
/* 21 */   public static final Codec<EnchantmentValueEffect> CODEC = BuiltInRegistries.ENCHANTMENT_VALUE_EFFECT_TYPE.byNameCodec().dispatch(EnchantmentValueEffect::codec, Function.identity());
/*    */   
/*    */   float process(int paramInt, RandomSource paramRandomSource, float paramFloat);
/*    */   
/*    */   MapCodec<? extends EnchantmentValueEffect> codec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\effects\EnchantmentValueEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */