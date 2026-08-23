/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ 
/*    */ public interface TradeRebalanceEnchantmentProviders {
/* 12 */   public static final ResourceKey<EnchantmentProvider> TRADES_DESERT_ARMORER_BOOTS_4 = VanillaEnchantmentProviders.create("trades/desert_armorer_boots_4");
/* 13 */   public static final ResourceKey<EnchantmentProvider> TRADES_DESERT_ARMORER_LEGGINGS_4 = VanillaEnchantmentProviders.create("trades/desert_armorer_leggings_4");
/* 14 */   public static final ResourceKey<EnchantmentProvider> TRADES_DESERT_ARMORER_CHESTPLATE_4 = VanillaEnchantmentProviders.create("trades/desert_armorer_chestplate_4");
/* 15 */   public static final ResourceKey<EnchantmentProvider> TRADES_DESERT_ARMORER_HELMET_4 = VanillaEnchantmentProviders.create("trades/desert_armorer_helmet_4");
/* 16 */   public static final ResourceKey<EnchantmentProvider> TRADES_DESERT_ARMORER_LEGGINGS_5 = VanillaEnchantmentProviders.create("trades/desert_armorer_leggings_5");
/* 17 */   public static final ResourceKey<EnchantmentProvider> TRADES_DESERT_ARMORER_CHESTPLATE_5 = VanillaEnchantmentProviders.create("trades/desert_armorer_chestplate_5");
/*    */   
/* 19 */   public static final ResourceKey<EnchantmentProvider> TRADES_PLAINS_ARMORER_BOOTS_4 = VanillaEnchantmentProviders.create("trades/plains_armorer_boots_4");
/* 20 */   public static final ResourceKey<EnchantmentProvider> TRADES_PLAINS_ARMORER_LEGGINGS_4 = VanillaEnchantmentProviders.create("trades/plains_armorer_leggings_4");
/* 21 */   public static final ResourceKey<EnchantmentProvider> TRADES_PLAINS_ARMORER_CHESTPLATE_4 = VanillaEnchantmentProviders.create("trades/plains_armorer_chestplate_4");
/* 22 */   public static final ResourceKey<EnchantmentProvider> TRADES_PLAINS_ARMORER_HELMET_4 = VanillaEnchantmentProviders.create("trades/plains_armorer_helmet_4");
/* 23 */   public static final ResourceKey<EnchantmentProvider> TRADES_PLAINS_ARMORER_BOOTS_5 = VanillaEnchantmentProviders.create("trades/plains_armorer_boots_5");
/* 24 */   public static final ResourceKey<EnchantmentProvider> TRADES_PLAINS_ARMORER_LEGGINGS_5 = VanillaEnchantmentProviders.create("trades/plains_armorer_leggings_5");
/*    */   
/* 26 */   public static final ResourceKey<EnchantmentProvider> TRADES_SAVANNA_ARMORER_BOOTS_4 = VanillaEnchantmentProviders.create("trades/savanna_armorer_boots_4");
/* 27 */   public static final ResourceKey<EnchantmentProvider> TRADES_SAVANNA_ARMORER_LEGGINGS_4 = VanillaEnchantmentProviders.create("trades/savanna_armorer_leggings_4");
/* 28 */   public static final ResourceKey<EnchantmentProvider> TRADES_SAVANNA_ARMORER_CHESTPLATE_4 = VanillaEnchantmentProviders.create("trades/savanna_armorer_chestplate_4");
/* 29 */   public static final ResourceKey<EnchantmentProvider> TRADES_SAVANNA_ARMORER_HELMET_4 = VanillaEnchantmentProviders.create("trades/savanna_armorer_helmet_4");
/* 30 */   public static final ResourceKey<EnchantmentProvider> TRADES_SAVANNA_ARMORER_CHESTPLATE_5 = VanillaEnchantmentProviders.create("trades/savanna_armorer_chestplate_5");
/* 31 */   public static final ResourceKey<EnchantmentProvider> TRADES_SAVANNA_ARMORER_HELMET_5 = VanillaEnchantmentProviders.create("trades/savanna_armorer_helmet_5");
/*    */   
/* 33 */   public static final ResourceKey<EnchantmentProvider> TRADES_SNOW_ARMORER_BOOTS_4 = VanillaEnchantmentProviders.create("trades/snow_armorer_boots_4");
/* 34 */   public static final ResourceKey<EnchantmentProvider> TRADES_SNOW_ARMORER_HELMET_4 = VanillaEnchantmentProviders.create("trades/snow_armorer_helmet_4");
/* 35 */   public static final ResourceKey<EnchantmentProvider> TRADES_SNOW_ARMORER_BOOTS_5 = VanillaEnchantmentProviders.create("trades/snow_armorer_boots_5");
/* 36 */   public static final ResourceKey<EnchantmentProvider> TRADES_SNOW_ARMORER_HELMET_5 = VanillaEnchantmentProviders.create("trades/snow_armorer_helmet_5");
/*    */   
/* 38 */   public static final ResourceKey<EnchantmentProvider> TRADES_JUNGLE_ARMORER_BOOTS_4 = VanillaEnchantmentProviders.create("trades/jungle_armorer_boots_4");
/* 39 */   public static final ResourceKey<EnchantmentProvider> TRADES_JUNGLE_ARMORER_LEGGINGS_4 = VanillaEnchantmentProviders.create("trades/jungle_armorer_leggings_4");
/* 40 */   public static final ResourceKey<EnchantmentProvider> TRADES_JUNGLE_ARMORER_CHESTPLATE_4 = VanillaEnchantmentProviders.create("trades/jungle_armorer_chestplate_4");
/* 41 */   public static final ResourceKey<EnchantmentProvider> TRADES_JUNGLE_ARMORER_HELMET_4 = VanillaEnchantmentProviders.create("trades/jungle_armorer_helmet_4");
/* 42 */   public static final ResourceKey<EnchantmentProvider> TRADES_JUNGLE_ARMORER_BOOTS_5 = VanillaEnchantmentProviders.create("trades/jungle_armorer_boots_5");
/* 43 */   public static final ResourceKey<EnchantmentProvider> TRADES_JUNGLE_ARMORER_HELMET_5 = VanillaEnchantmentProviders.create("trades/jungle_armorer_helmet_5");
/*    */   
/* 45 */   public static final ResourceKey<EnchantmentProvider> TRADES_SWAMP_ARMORER_BOOTS_4 = VanillaEnchantmentProviders.create("trades/swamp_armorer_boots_4");
/* 46 */   public static final ResourceKey<EnchantmentProvider> TRADES_SWAMP_ARMORER_LEGGINGS_4 = VanillaEnchantmentProviders.create("trades/swamp_armorer_leggings_4");
/* 47 */   public static final ResourceKey<EnchantmentProvider> TRADES_SWAMP_ARMORER_CHESTPLATE_4 = VanillaEnchantmentProviders.create("trades/swamp_armorer_chestplate_4");
/* 48 */   public static final ResourceKey<EnchantmentProvider> TRADES_SWAMP_ARMORER_HELMET_4 = VanillaEnchantmentProviders.create("trades/swamp_armorer_helmet_4");
/* 49 */   public static final ResourceKey<EnchantmentProvider> TRADES_SWAMP_ARMORER_BOOTS_5 = VanillaEnchantmentProviders.create("trades/swamp_armorer_boots_5");
/* 50 */   public static final ResourceKey<EnchantmentProvider> TRADES_SWAMP_ARMORER_HELMET_5 = VanillaEnchantmentProviders.create("trades/swamp_armorer_helmet_5");
/*    */   
/* 52 */   public static final ResourceKey<EnchantmentProvider> TRADES_TAIGA_ARMORER_LEGGINGS_5 = VanillaEnchantmentProviders.create("trades/taiga_armorer_leggings_5");
/* 53 */   public static final ResourceKey<EnchantmentProvider> TRADES_TAIGA_ARMORER_CHESTPLATE_5 = VanillaEnchantmentProviders.create("trades/taiga_armorer_chestplate_5");
/*    */   
/*    */   static void bootstrap(BootstrapContext<EnchantmentProvider> paramBootstrapContext) {
/* 56 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.ENCHANTMENT);
/*    */     
/* 58 */     paramBootstrapContext.register(TRADES_DESERT_ARMORER_BOOTS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.THORNS), (IntProvider)ConstantInt.of(1)));
/* 59 */     paramBootstrapContext.register(TRADES_DESERT_ARMORER_LEGGINGS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.THORNS), (IntProvider)ConstantInt.of(1)));
/* 60 */     paramBootstrapContext.register(TRADES_DESERT_ARMORER_CHESTPLATE_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.THORNS), (IntProvider)ConstantInt.of(1)));
/* 61 */     paramBootstrapContext.register(TRADES_DESERT_ARMORER_HELMET_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.THORNS), (IntProvider)ConstantInt.of(1)));
/* 62 */     paramBootstrapContext.register(TRADES_DESERT_ARMORER_LEGGINGS_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.THORNS), (IntProvider)ConstantInt.of(1)));
/* 63 */     paramBootstrapContext.register(TRADES_DESERT_ARMORER_CHESTPLATE_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.THORNS), (IntProvider)ConstantInt.of(1)));
/*    */     
/* 65 */     paramBootstrapContext.register(TRADES_PLAINS_ARMORER_BOOTS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.PROTECTION), (IntProvider)ConstantInt.of(1)));
/* 66 */     paramBootstrapContext.register(TRADES_PLAINS_ARMORER_LEGGINGS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.PROTECTION), (IntProvider)ConstantInt.of(1)));
/* 67 */     paramBootstrapContext.register(TRADES_PLAINS_ARMORER_CHESTPLATE_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.PROTECTION), (IntProvider)ConstantInt.of(1)));
/* 68 */     paramBootstrapContext.register(TRADES_PLAINS_ARMORER_HELMET_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.PROTECTION), (IntProvider)ConstantInt.of(1)));
/* 69 */     paramBootstrapContext.register(TRADES_PLAINS_ARMORER_BOOTS_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.PROTECTION), (IntProvider)ConstantInt.of(1)));
/* 70 */     paramBootstrapContext.register(TRADES_PLAINS_ARMORER_LEGGINGS_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.PROTECTION), (IntProvider)ConstantInt.of(1)));
/*    */     
/* 72 */     paramBootstrapContext.register(TRADES_SAVANNA_ARMORER_BOOTS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.BINDING_CURSE), (IntProvider)ConstantInt.of(1)));
/* 73 */     paramBootstrapContext.register(TRADES_SAVANNA_ARMORER_LEGGINGS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.BINDING_CURSE), (IntProvider)ConstantInt.of(1)));
/* 74 */     paramBootstrapContext.register(TRADES_SAVANNA_ARMORER_CHESTPLATE_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.BINDING_CURSE), (IntProvider)ConstantInt.of(1)));
/* 75 */     paramBootstrapContext.register(TRADES_SAVANNA_ARMORER_HELMET_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.BINDING_CURSE), (IntProvider)ConstantInt.of(1)));
/* 76 */     paramBootstrapContext.register(TRADES_SAVANNA_ARMORER_CHESTPLATE_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.BINDING_CURSE), (IntProvider)ConstantInt.of(1)));
/* 77 */     paramBootstrapContext.register(TRADES_SAVANNA_ARMORER_HELMET_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.BINDING_CURSE), (IntProvider)ConstantInt.of(1)));
/*    */     
/* 79 */     paramBootstrapContext.register(TRADES_SNOW_ARMORER_BOOTS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.FROST_WALKER), (IntProvider)ConstantInt.of(1)));
/* 80 */     paramBootstrapContext.register(TRADES_SNOW_ARMORER_HELMET_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.AQUA_AFFINITY), (IntProvider)ConstantInt.of(1)));
/* 81 */     paramBootstrapContext.register(TRADES_SNOW_ARMORER_BOOTS_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.FROST_WALKER), (IntProvider)ConstantInt.of(1)));
/* 82 */     paramBootstrapContext.register(TRADES_SNOW_ARMORER_HELMET_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.AQUA_AFFINITY), (IntProvider)ConstantInt.of(1)));
/*    */     
/* 84 */     paramBootstrapContext.register(TRADES_JUNGLE_ARMORER_BOOTS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.UNBREAKING), (IntProvider)ConstantInt.of(1)));
/* 85 */     paramBootstrapContext.register(TRADES_JUNGLE_ARMORER_LEGGINGS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.UNBREAKING), (IntProvider)ConstantInt.of(1)));
/* 86 */     paramBootstrapContext.register(TRADES_JUNGLE_ARMORER_CHESTPLATE_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.UNBREAKING), (IntProvider)ConstantInt.of(1)));
/* 87 */     paramBootstrapContext.register(TRADES_JUNGLE_ARMORER_HELMET_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.UNBREAKING), (IntProvider)ConstantInt.of(1)));
/* 88 */     paramBootstrapContext.register(TRADES_JUNGLE_ARMORER_BOOTS_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.FEATHER_FALLING), (IntProvider)ConstantInt.of(1)));
/* 89 */     paramBootstrapContext.register(TRADES_JUNGLE_ARMORER_HELMET_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.PROJECTILE_PROTECTION), (IntProvider)ConstantInt.of(1)));
/*    */     
/* 91 */     paramBootstrapContext.register(TRADES_SWAMP_ARMORER_BOOTS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.MENDING), (IntProvider)ConstantInt.of(1)));
/* 92 */     paramBootstrapContext.register(TRADES_SWAMP_ARMORER_LEGGINGS_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.MENDING), (IntProvider)ConstantInt.of(1)));
/* 93 */     paramBootstrapContext.register(TRADES_SWAMP_ARMORER_CHESTPLATE_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.MENDING), (IntProvider)ConstantInt.of(1)));
/* 94 */     paramBootstrapContext.register(TRADES_SWAMP_ARMORER_HELMET_4, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.MENDING), (IntProvider)ConstantInt.of(1)));
/* 95 */     paramBootstrapContext.register(TRADES_SWAMP_ARMORER_BOOTS_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.DEPTH_STRIDER), (IntProvider)ConstantInt.of(1)));
/* 96 */     paramBootstrapContext.register(TRADES_SWAMP_ARMORER_HELMET_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.RESPIRATION), (IntProvider)ConstantInt.of(1)));
/*    */     
/* 98 */     paramBootstrapContext.register(TRADES_TAIGA_ARMORER_LEGGINGS_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.BLAST_PROTECTION), (IntProvider)ConstantInt.of(1)));
/* 99 */     paramBootstrapContext.register(TRADES_TAIGA_ARMORER_CHESTPLATE_5, new SingleEnchantment((Holder<Enchantment>)holderGetter.getOrThrow(Enchantments.BLAST_PROTECTION), (IntProvider)ConstantInt.of(1)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\providers\TradeRebalanceEnchantmentProviders.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */