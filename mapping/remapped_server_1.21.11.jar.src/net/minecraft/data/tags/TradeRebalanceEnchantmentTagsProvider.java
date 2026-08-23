/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ 
/*    */ public class TradeRebalanceEnchantmentTagsProvider extends KeyTagProvider<Enchantment> {
/*    */   public TradeRebalanceEnchantmentTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 14 */     super(paramPackOutput, Registries.ENCHANTMENT, paramCompletableFuture);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider paramProvider) {
/* 19 */     tag(EnchantmentTags.TRADES_DESERT_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.FIRE_PROTECTION, Enchantments.THORNS, Enchantments.INFINITY });
/* 20 */     tag(EnchantmentTags.TRADES_JUNGLE_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.FEATHER_FALLING, Enchantments.PROJECTILE_PROTECTION, Enchantments.POWER });
/* 21 */     tag(EnchantmentTags.TRADES_PLAINS_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.PUNCH, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS });
/* 22 */     tag(EnchantmentTags.TRADES_SAVANNA_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.KNOCKBACK, Enchantments.BINDING_CURSE, Enchantments.SWEEPING_EDGE });
/* 23 */     tag(EnchantmentTags.TRADES_SNOW_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.AQUA_AFFINITY, Enchantments.LOOTING, Enchantments.FROST_WALKER });
/* 24 */     tag(EnchantmentTags.TRADES_SWAMP_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.DEPTH_STRIDER, Enchantments.RESPIRATION, Enchantments.VANISHING_CURSE });
/* 25 */     tag(EnchantmentTags.TRADES_TAIGA_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.BLAST_PROTECTION, Enchantments.FIRE_ASPECT, Enchantments.FLAME });
/*    */     
/* 27 */     tag(EnchantmentTags.TRADES_DESERT_SPECIAL).add(Enchantments.EFFICIENCY);
/* 28 */     tag(EnchantmentTags.TRADES_JUNGLE_SPECIAL).add(Enchantments.UNBREAKING);
/* 29 */     tag(EnchantmentTags.TRADES_PLAINS_SPECIAL).add(Enchantments.PROTECTION);
/* 30 */     tag(EnchantmentTags.TRADES_SAVANNA_SPECIAL).add(Enchantments.SHARPNESS);
/* 31 */     tag(EnchantmentTags.TRADES_SNOW_SPECIAL).add(Enchantments.SILK_TOUCH);
/* 32 */     tag(EnchantmentTags.TRADES_SWAMP_SPECIAL).add(Enchantments.MENDING);
/* 33 */     tag(EnchantmentTags.TRADES_TAIGA_SPECIAL).add(Enchantments.FORTUNE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\TradeRebalanceEnchantmentTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */