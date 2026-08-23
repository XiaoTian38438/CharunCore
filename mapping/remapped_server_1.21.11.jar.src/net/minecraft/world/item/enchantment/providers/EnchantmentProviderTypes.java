/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public interface EnchantmentProviderTypes {
/*    */   static MapCodec<? extends EnchantmentProvider> bootstrap(Registry<MapCodec<? extends EnchantmentProvider>> paramRegistry) {
/*  8 */     Registry.register(paramRegistry, "by_cost", EnchantmentsByCost.CODEC);
/*  9 */     Registry.register(paramRegistry, "by_cost_with_difficulty", EnchantmentsByCostWithDifficulty.CODEC);
/* 10 */     return (MapCodec<? extends EnchantmentProvider>)Registry.register(paramRegistry, "single", SingleEnchantment.CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\providers\EnchantmentProviderTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */