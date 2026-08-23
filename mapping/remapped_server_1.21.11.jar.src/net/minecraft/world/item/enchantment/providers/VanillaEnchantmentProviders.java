/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ 
/*    */ public interface VanillaEnchantmentProviders {
/* 15 */   public static final ResourceKey<EnchantmentProvider> MOB_SPAWN_EQUIPMENT = create("mob_spawn_equipment");
/* 16 */   public static final ResourceKey<EnchantmentProvider> PILLAGER_SPAWN_CROSSBOW = create("pillager_spawn_crossbow");
/*    */   
/* 18 */   public static final ResourceKey<EnchantmentProvider> RAID_PILLAGER_POST_WAVE_3 = create("raid/pillager_post_wave_3");
/* 19 */   public static final ResourceKey<EnchantmentProvider> RAID_PILLAGER_POST_WAVE_5 = create("raid/pillager_post_wave_5");
/* 20 */   public static final ResourceKey<EnchantmentProvider> RAID_VINDICATOR = create("raid/vindicator");
/* 21 */   public static final ResourceKey<EnchantmentProvider> RAID_VINDICATOR_POST_WAVE_5 = create("raid/vindicator_post_wave_5");
/*    */   
/* 23 */   public static final ResourceKey<EnchantmentProvider> ENDERMAN_LOOT_DROP = create("enderman_loot_drop");
/*    */   
/*    */   static void bootstrap(BootstrapContext<EnchantmentProvider> paramBootstrapContext) {
/* 26 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.ENCHANTMENT);
/* 27 */     paramBootstrapContext.register(MOB_SPAWN_EQUIPMENT, new EnchantmentsByCostWithDifficulty((HolderSet<Enchantment>)holderGetter
/*    */ 
/*    */           
/* 30 */           .getOrThrow(EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT), 5, 17));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     paramBootstrapContext.register(PILLAGER_SPAWN_CROSSBOW, new SingleEnchantment((Holder<Enchantment>)holderGetter
/*    */ 
/*    */           
/* 38 */           .getOrThrow(Enchantments.PIERCING), 
/* 39 */           (IntProvider)ConstantInt.of(1)));
/*    */ 
/*    */ 
/*    */     
/* 43 */     paramBootstrapContext.register(RAID_PILLAGER_POST_WAVE_3, new SingleEnchantment((Holder<Enchantment>)holderGetter
/*    */ 
/*    */           
/* 46 */           .getOrThrow(Enchantments.QUICK_CHARGE), 
/* 47 */           (IntProvider)ConstantInt.of(1)));
/*    */ 
/*    */     
/* 50 */     paramBootstrapContext.register(RAID_PILLAGER_POST_WAVE_5, new SingleEnchantment((Holder<Enchantment>)holderGetter
/*    */ 
/*    */           
/* 53 */           .getOrThrow(Enchantments.QUICK_CHARGE), 
/* 54 */           (IntProvider)ConstantInt.of(2)));
/*    */ 
/*    */     
/* 57 */     paramBootstrapContext.register(RAID_VINDICATOR, new SingleEnchantment((Holder<Enchantment>)holderGetter
/*    */ 
/*    */           
/* 60 */           .getOrThrow(Enchantments.SHARPNESS), 
/* 61 */           (IntProvider)ConstantInt.of(1)));
/*    */ 
/*    */     
/* 64 */     paramBootstrapContext.register(RAID_VINDICATOR_POST_WAVE_5, new SingleEnchantment((Holder<Enchantment>)holderGetter
/*    */ 
/*    */           
/* 67 */           .getOrThrow(Enchantments.SHARPNESS), 
/* 68 */           (IntProvider)ConstantInt.of(2)));
/*    */ 
/*    */ 
/*    */     
/* 72 */     paramBootstrapContext.register(ENDERMAN_LOOT_DROP, new SingleEnchantment((Holder<Enchantment>)holderGetter
/*    */ 
/*    */           
/* 75 */           .getOrThrow(Enchantments.SILK_TOUCH), 
/* 76 */           (IntProvider)ConstantInt.of(1)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static ResourceKey<EnchantmentProvider> create(String paramString) {
/* 82 */     return ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\providers\VanillaEnchantmentProviders.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */