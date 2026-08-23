/*     */ package net.minecraft.data.worldgen.features;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OreFeatures
/*     */ {
/*  21 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MAGMA = FeatureUtils.createKey("ore_magma");
/*  22 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SOUL_SAND = FeatureUtils.createKey("ore_soul_sand");
/*  23 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_GOLD = FeatureUtils.createKey("ore_nether_gold");
/*  24 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_QUARTZ = FeatureUtils.createKey("ore_quartz");
/*  25 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GRAVEL_NETHER = FeatureUtils.createKey("ore_gravel_nether");
/*  26 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_BLACKSTONE = FeatureUtils.createKey("ore_blackstone");
/*     */ 
/*     */ 
/*     */   
/*  30 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIRT = FeatureUtils.createKey("ore_dirt");
/*     */   
/*  32 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GRAVEL = FeatureUtils.createKey("ore_gravel");
/*     */   
/*  34 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GRANITE = FeatureUtils.createKey("ore_granite");
/*     */   
/*  36 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIORITE = FeatureUtils.createKey("ore_diorite");
/*     */   
/*  38 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ANDESITE = FeatureUtils.createKey("ore_andesite");
/*     */   
/*  40 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_TUFF = FeatureUtils.createKey("ore_tuff");
/*     */   
/*  42 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COAL = FeatureUtils.createKey("ore_coal");
/*  43 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COAL_BURIED = FeatureUtils.createKey("ore_coal_buried");
/*     */   
/*  45 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON = FeatureUtils.createKey("ore_iron");
/*  46 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_SMALL = FeatureUtils.createKey("ore_iron_small");
/*     */   
/*  48 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD = FeatureUtils.createKey("ore_gold");
/*  49 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_BURIED = FeatureUtils.createKey("ore_gold_buried");
/*     */   
/*  51 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_REDSTONE = FeatureUtils.createKey("ore_redstone");
/*     */   
/*  53 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_SMALL = FeatureUtils.createKey("ore_diamond_small");
/*  54 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_MEDIUM = FeatureUtils.createKey("ore_diamond_medium");
/*  55 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_LARGE = FeatureUtils.createKey("ore_diamond_large");
/*  56 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_BURIED = FeatureUtils.createKey("ore_diamond_buried");
/*     */   
/*  58 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LAPIS = FeatureUtils.createKey("ore_lapis");
/*  59 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LAPIS_BURIED = FeatureUtils.createKey("ore_lapis_buried");
/*     */   
/*  61 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_INFESTED = FeatureUtils.createKey("ore_infested");
/*     */   
/*  63 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EMERALD = FeatureUtils.createKey("ore_emerald");
/*     */   
/*  65 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ANCIENT_DEBRIS_LARGE = FeatureUtils.createKey("ore_ancient_debris_large");
/*  66 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ANCIENT_DEBRIS_SMALL = FeatureUtils.createKey("ore_ancient_debris_small");
/*     */   
/*  68 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COPPPER_SMALL = FeatureUtils.createKey("ore_copper_small");
/*  69 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COPPER_LARGE = FeatureUtils.createKey("ore_copper_large");
/*     */   
/*  71 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CLAY = FeatureUtils.createKey("ore_clay");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> paramBootstrapContext) {
/*  74 */     TagMatchTest tagMatchTest1 = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
/*  75 */     TagMatchTest tagMatchTest2 = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
/*  76 */     TagMatchTest tagMatchTest3 = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
/*  77 */     BlockMatchTest blockMatchTest = new BlockMatchTest(Blocks.NETHERRACK);
/*  78 */     TagMatchTest tagMatchTest4 = new TagMatchTest(BlockTags.BASE_STONE_NETHER);
/*     */     
/*  80 */     List<OreConfiguration.TargetBlockState> list1 = List.of(
/*  81 */         OreConfiguration.target((RuleTest)tagMatchTest2, Blocks.IRON_ORE.defaultBlockState()), 
/*  82 */         OreConfiguration.target((RuleTest)tagMatchTest3, Blocks.DEEPSLATE_IRON_ORE.defaultBlockState()));
/*     */     
/*  84 */     List<OreConfiguration.TargetBlockState> list2 = List.of(
/*  85 */         OreConfiguration.target((RuleTest)tagMatchTest2, Blocks.GOLD_ORE.defaultBlockState()), 
/*  86 */         OreConfiguration.target((RuleTest)tagMatchTest3, Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState()));
/*     */     
/*  88 */     List<OreConfiguration.TargetBlockState> list3 = List.of(
/*  89 */         OreConfiguration.target((RuleTest)tagMatchTest2, Blocks.DIAMOND_ORE.defaultBlockState()), 
/*  90 */         OreConfiguration.target((RuleTest)tagMatchTest3, Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState()));
/*     */     
/*  92 */     List<OreConfiguration.TargetBlockState> list4 = List.of(
/*  93 */         OreConfiguration.target((RuleTest)tagMatchTest2, Blocks.LAPIS_ORE.defaultBlockState()), 
/*  94 */         OreConfiguration.target((RuleTest)tagMatchTest3, Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState()));
/*     */     
/*  96 */     List<OreConfiguration.TargetBlockState> list5 = List.of(
/*  97 */         OreConfiguration.target((RuleTest)tagMatchTest2, Blocks.COPPER_ORE.defaultBlockState()), 
/*  98 */         OreConfiguration.target((RuleTest)tagMatchTest3, Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState()));
/*     */     
/* 100 */     List<OreConfiguration.TargetBlockState> list6 = List.of(
/* 101 */         OreConfiguration.target((RuleTest)tagMatchTest2, Blocks.COAL_ORE.defaultBlockState()), 
/* 102 */         OreConfiguration.target((RuleTest)tagMatchTest3, Blocks.DEEPSLATE_COAL_ORE.defaultBlockState()));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     FeatureUtils.register(paramBootstrapContext, ORE_MAGMA, Feature.ORE, new OreConfiguration((RuleTest)blockMatchTest, Blocks.MAGMA_BLOCK
/*     */           
/* 109 */           .defaultBlockState(), 33));
/*     */ 
/*     */     
/* 112 */     FeatureUtils.register(paramBootstrapContext, ORE_SOUL_SAND, Feature.ORE, new OreConfiguration((RuleTest)blockMatchTest, Blocks.SOUL_SAND
/*     */           
/* 114 */           .defaultBlockState(), 12));
/*     */ 
/*     */     
/* 117 */     FeatureUtils.register(paramBootstrapContext, ORE_NETHER_GOLD, Feature.ORE, new OreConfiguration((RuleTest)blockMatchTest, Blocks.NETHER_GOLD_ORE
/*     */           
/* 119 */           .defaultBlockState(), 10));
/*     */ 
/*     */     
/* 122 */     FeatureUtils.register(paramBootstrapContext, ORE_QUARTZ, Feature.ORE, new OreConfiguration((RuleTest)blockMatchTest, Blocks.NETHER_QUARTZ_ORE
/*     */           
/* 124 */           .defaultBlockState(), 14));
/*     */ 
/*     */     
/* 127 */     FeatureUtils.register(paramBootstrapContext, ORE_GRAVEL_NETHER, Feature.ORE, new OreConfiguration((RuleTest)blockMatchTest, Blocks.GRAVEL
/*     */           
/* 129 */           .defaultBlockState(), 33));
/*     */ 
/*     */     
/* 132 */     FeatureUtils.register(paramBootstrapContext, ORE_BLACKSTONE, Feature.ORE, new OreConfiguration((RuleTest)blockMatchTest, Blocks.BLACKSTONE
/*     */           
/* 134 */           .defaultBlockState(), 33));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     FeatureUtils.register(paramBootstrapContext, ORE_DIRT, Feature.ORE, new OreConfiguration((RuleTest)tagMatchTest1, Blocks.DIRT
/*     */           
/* 142 */           .defaultBlockState(), 33));
/*     */ 
/*     */ 
/*     */     
/* 146 */     FeatureUtils.register(paramBootstrapContext, ORE_GRAVEL, Feature.ORE, new OreConfiguration((RuleTest)tagMatchTest1, Blocks.GRAVEL
/*     */           
/* 148 */           .defaultBlockState(), 33));
/*     */ 
/*     */ 
/*     */     
/* 152 */     FeatureUtils.register(paramBootstrapContext, ORE_GRANITE, Feature.ORE, new OreConfiguration((RuleTest)tagMatchTest1, Blocks.GRANITE
/*     */           
/* 154 */           .defaultBlockState(), 64));
/*     */ 
/*     */ 
/*     */     
/* 158 */     FeatureUtils.register(paramBootstrapContext, ORE_DIORITE, Feature.ORE, new OreConfiguration((RuleTest)tagMatchTest1, Blocks.DIORITE
/*     */           
/* 160 */           .defaultBlockState(), 64));
/*     */ 
/*     */ 
/*     */     
/* 164 */     FeatureUtils.register(paramBootstrapContext, ORE_ANDESITE, Feature.ORE, new OreConfiguration((RuleTest)tagMatchTest1, Blocks.ANDESITE
/*     */           
/* 166 */           .defaultBlockState(), 64));
/*     */ 
/*     */ 
/*     */     
/* 170 */     FeatureUtils.register(paramBootstrapContext, ORE_TUFF, Feature.ORE, new OreConfiguration((RuleTest)tagMatchTest1, Blocks.TUFF
/*     */           
/* 172 */           .defaultBlockState(), 64));
/*     */ 
/*     */ 
/*     */     
/* 176 */     FeatureUtils.register(paramBootstrapContext, ORE_COAL, Feature.ORE, new OreConfiguration(list6, 17));
/*     */ 
/*     */ 
/*     */     
/* 180 */     FeatureUtils.register(paramBootstrapContext, ORE_COAL_BURIED, Feature.ORE, new OreConfiguration(list6, 17, 0.5F));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     FeatureUtils.register(paramBootstrapContext, ORE_IRON, Feature.ORE, new OreConfiguration(list1, 9));
/*     */ 
/*     */     
/* 189 */     FeatureUtils.register(paramBootstrapContext, ORE_IRON_SMALL, Feature.ORE, new OreConfiguration(list1, 4));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     FeatureUtils.register(paramBootstrapContext, ORE_GOLD, Feature.ORE, new OreConfiguration(list2, 9));
/*     */ 
/*     */ 
/*     */     
/* 198 */     FeatureUtils.register(paramBootstrapContext, ORE_GOLD_BURIED, Feature.ORE, new OreConfiguration(list2, 9, 0.5F));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     FeatureUtils.register(paramBootstrapContext, ORE_REDSTONE, Feature.ORE, new OreConfiguration(
/* 205 */           List.of(
/* 206 */             OreConfiguration.target((RuleTest)tagMatchTest2, Blocks.REDSTONE_ORE.defaultBlockState()), 
/* 207 */             OreConfiguration.target((RuleTest)tagMatchTest3, Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState())), 8));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     FeatureUtils.register(paramBootstrapContext, ORE_DIAMOND_SMALL, Feature.ORE, new OreConfiguration(list3, 4, 0.5F));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     FeatureUtils.register(paramBootstrapContext, ORE_DIAMOND_LARGE, Feature.ORE, new OreConfiguration(list3, 12, 0.7F));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     FeatureUtils.register(paramBootstrapContext, ORE_DIAMOND_BURIED, Feature.ORE, new OreConfiguration(list3, 8, 1.0F));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 227 */     FeatureUtils.register(paramBootstrapContext, ORE_DIAMOND_MEDIUM, Feature.ORE, new OreConfiguration(list3, 8, 0.5F));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     FeatureUtils.register(paramBootstrapContext, ORE_LAPIS, Feature.ORE, new OreConfiguration(list4, 7));
/*     */ 
/*     */ 
/*     */     
/* 237 */     FeatureUtils.register(paramBootstrapContext, ORE_LAPIS_BURIED, Feature.ORE, new OreConfiguration(list4, 7, 1.0F));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 243 */     FeatureUtils.register(paramBootstrapContext, ORE_INFESTED, Feature.ORE, new OreConfiguration(
/* 244 */           List.of(
/* 245 */             OreConfiguration.target((RuleTest)tagMatchTest2, Blocks.INFESTED_STONE.defaultBlockState()), 
/* 246 */             OreConfiguration.target((RuleTest)tagMatchTest3, Blocks.INFESTED_DEEPSLATE.defaultBlockState())), 9));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     FeatureUtils.register(paramBootstrapContext, ORE_EMERALD, Feature.ORE, new OreConfiguration(
/* 252 */           List.of(
/* 253 */             OreConfiguration.target((RuleTest)tagMatchTest2, Blocks.EMERALD_ORE.defaultBlockState()), 
/* 254 */             OreConfiguration.target((RuleTest)tagMatchTest3, Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState())), 3));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     FeatureUtils.register(paramBootstrapContext, ORE_ANCIENT_DEBRIS_LARGE, Feature.SCATTERED_ORE, new OreConfiguration((RuleTest)tagMatchTest4, Blocks.ANCIENT_DEBRIS
/*     */           
/* 261 */           .defaultBlockState(), 3, 1.0F));
/*     */ 
/*     */ 
/*     */     
/* 265 */     FeatureUtils.register(paramBootstrapContext, ORE_ANCIENT_DEBRIS_SMALL, Feature.SCATTERED_ORE, new OreConfiguration((RuleTest)tagMatchTest4, Blocks.ANCIENT_DEBRIS
/*     */           
/* 267 */           .defaultBlockState(), 2, 1.0F));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     FeatureUtils.register(paramBootstrapContext, ORE_COPPPER_SMALL, Feature.ORE, new OreConfiguration(list5, 10));
/*     */ 
/*     */ 
/*     */     
/* 276 */     FeatureUtils.register(paramBootstrapContext, ORE_COPPER_LARGE, Feature.ORE, new OreConfiguration(list5, 20));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     FeatureUtils.register(paramBootstrapContext, ORE_CLAY, Feature.ORE, new OreConfiguration((RuleTest)tagMatchTest1, Blocks.CLAY
/*     */           
/* 283 */           .defaultBlockState(), 33));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\features\OreFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */