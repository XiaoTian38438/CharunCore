/*     */ package net.minecraft.data.worldgen.features;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TwistingVinesConfig;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NetherFeatures
/*     */ {
/*  30 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DELTA = FeatureUtils.createKey("delta");
/*  31 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_BASALT_COLUMNS = FeatureUtils.createKey("small_basalt_columns");
/*  32 */   public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_BASALT_COLUMNS = FeatureUtils.createKey("large_basalt_columns");
/*  33 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BASALT_BLOBS = FeatureUtils.createKey("basalt_blobs");
/*  34 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BLACKSTONE_BLOBS = FeatureUtils.createKey("blackstone_blobs");
/*  35 */   public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWSTONE_EXTRA = FeatureUtils.createKey("glowstone_extra");
/*     */   
/*  37 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_FOREST_VEGETATION = FeatureUtils.createKey("crimson_forest_vegetation");
/*  38 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_FOREST_VEGETATION_BONEMEAL = FeatureUtils.createKey("crimson_forest_vegetation_bonemeal");
/*     */   
/*  40 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WARPED_FOREST_VEGETION = FeatureUtils.createKey("warped_forest_vegetation");
/*  41 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WARPED_FOREST_VEGETATION_BONEMEAL = FeatureUtils.createKey("warped_forest_vegetation_bonemeal");
/*     */   
/*  43 */   public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_SPROUTS = FeatureUtils.createKey("nether_sprouts");
/*  44 */   public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_SPROUTS_BONEMEAL = FeatureUtils.createKey("nether_sprouts_bonemeal");
/*     */   
/*  46 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TWISTING_VINES = FeatureUtils.createKey("twisting_vines");
/*  47 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TWISTING_VINES_BONEMEAL = FeatureUtils.createKey("twisting_vines_bonemeal");
/*  48 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WEEPING_VINES = FeatureUtils.createKey("weeping_vines");
/*  49 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CRIMSON_ROOTS = FeatureUtils.createKey("patch_crimson_roots");
/*     */   
/*  51 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BASALT_PILLAR = FeatureUtils.createKey("basalt_pillar");
/*  52 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_LAVA_NETHER = FeatureUtils.createKey("spring_lava_nether");
/*  53 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_NETHER_CLOSED = FeatureUtils.createKey("spring_nether_closed");
/*  54 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_NETHER_OPEN = FeatureUtils.createKey("spring_nether_open");
/*     */   
/*  56 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIRE = FeatureUtils.createKey("patch_fire");
/*  57 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SOUL_FIRE = FeatureUtils.createKey("patch_soul_fire");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> paramBootstrapContext) {
/*  60 */     FeatureUtils.register(paramBootstrapContext, DELTA, Feature.DELTA_FEATURE, new DeltaFeatureConfiguration(Blocks.LAVA
/*  61 */           .defaultBlockState(), Blocks.MAGMA_BLOCK
/*  62 */           .defaultBlockState(), 
/*  63 */           (IntProvider)UniformInt.of(3, 7), 
/*  64 */           (IntProvider)UniformInt.of(0, 2)));
/*     */     
/*  66 */     FeatureUtils.register(paramBootstrapContext, SMALL_BASALT_COLUMNS, Feature.BASALT_COLUMNS, new ColumnFeatureConfiguration(
/*  67 */           (IntProvider)ConstantInt.of(1), 
/*  68 */           (IntProvider)UniformInt.of(1, 4)));
/*     */     
/*  70 */     FeatureUtils.register(paramBootstrapContext, LARGE_BASALT_COLUMNS, Feature.BASALT_COLUMNS, new ColumnFeatureConfiguration(
/*  71 */           (IntProvider)UniformInt.of(2, 3), 
/*  72 */           (IntProvider)UniformInt.of(5, 10)));
/*     */     
/*  74 */     FeatureUtils.register(paramBootstrapContext, BASALT_BLOBS, Feature.REPLACE_BLOBS, new ReplaceSphereConfiguration(Blocks.NETHERRACK
/*  75 */           .defaultBlockState(), Blocks.BASALT
/*  76 */           .defaultBlockState(), 
/*  77 */           (IntProvider)UniformInt.of(3, 7)));
/*     */     
/*  79 */     FeatureUtils.register(paramBootstrapContext, BLACKSTONE_BLOBS, Feature.REPLACE_BLOBS, new ReplaceSphereConfiguration(Blocks.NETHERRACK
/*  80 */           .defaultBlockState(), Blocks.BLACKSTONE
/*  81 */           .defaultBlockState(), 
/*  82 */           (IntProvider)UniformInt.of(3, 7)));
/*     */     
/*  84 */     FeatureUtils.register(paramBootstrapContext, GLOWSTONE_EXTRA, Feature.GLOWSTONE_BLOB);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     WeightedStateProvider weightedStateProvider1 = new WeightedStateProvider(WeightedList.builder().add(Blocks.CRIMSON_ROOTS.defaultBlockState(), 87).add(Blocks.CRIMSON_FUNGUS.defaultBlockState(), 11).add(Blocks.WARPED_FUNGUS.defaultBlockState(), 1));
/*     */     
/*  91 */     FeatureUtils.register(paramBootstrapContext, CRIMSON_FOREST_VEGETATION, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig((BlockStateProvider)weightedStateProvider1, 8, 4));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     FeatureUtils.register(paramBootstrapContext, CRIMSON_FOREST_VEGETATION_BONEMEAL, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig((BlockStateProvider)weightedStateProvider1, 3, 1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     WeightedStateProvider weightedStateProvider2 = new WeightedStateProvider(WeightedList.builder().add(Blocks.WARPED_ROOTS.defaultBlockState(), 85).add(Blocks.CRIMSON_ROOTS.defaultBlockState(), 1).add(Blocks.WARPED_FUNGUS.defaultBlockState(), 13).add(Blocks.CRIMSON_FUNGUS.defaultBlockState(), 1));
/*     */     
/* 108 */     FeatureUtils.register(paramBootstrapContext, WARPED_FOREST_VEGETION, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig((BlockStateProvider)weightedStateProvider2, 8, 4));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     FeatureUtils.register(paramBootstrapContext, WARPED_FOREST_VEGETATION_BONEMEAL, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig((BlockStateProvider)weightedStateProvider2, 3, 1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     FeatureUtils.register(paramBootstrapContext, NETHER_SPROUTS, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(
/* 120 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.NETHER_SPROUTS), 8, 4));
/*     */ 
/*     */ 
/*     */     
/* 124 */     FeatureUtils.register(paramBootstrapContext, NETHER_SPROUTS_BONEMEAL, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(
/* 125 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.NETHER_SPROUTS), 3, 1));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     FeatureUtils.register(paramBootstrapContext, TWISTING_VINES, Feature.TWISTING_VINES, new TwistingVinesConfig(8, 4, 8));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     FeatureUtils.register(paramBootstrapContext, TWISTING_VINES_BONEMEAL, Feature.TWISTING_VINES, new TwistingVinesConfig(3, 1, 2));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     FeatureUtils.register(paramBootstrapContext, WEEPING_VINES, Feature.WEEPING_VINES);
/* 141 */     FeatureUtils.register(paramBootstrapContext, PATCH_CRIMSON_ROOTS, Feature.RANDOM_PATCH, 
/*     */         
/* 143 */         FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 144 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.CRIMSON_ROOTS))));
/*     */ 
/*     */ 
/*     */     
/* 148 */     FeatureUtils.register(paramBootstrapContext, BASALT_PILLAR, Feature.BASALT_PILLAR);
/* 149 */     FeatureUtils.register(paramBootstrapContext, SPRING_LAVA_NETHER, Feature.SPRING, new SpringConfiguration(Fluids.LAVA
/* 150 */           .defaultFluidState(), true, 4, 1, 
/*     */ 
/*     */ 
/*     */           
/* 154 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.GRAVEL, Blocks.MAGMA_BLOCK, Blocks.BLACKSTONE })));
/*     */     
/* 156 */     FeatureUtils.register(paramBootstrapContext, SPRING_NETHER_CLOSED, Feature.SPRING, new SpringConfiguration(Fluids.LAVA
/* 157 */           .defaultFluidState(), false, 5, 0, 
/*     */ 
/*     */ 
/*     */           
/* 161 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.NETHERRACK })));
/*     */     
/* 163 */     FeatureUtils.register(paramBootstrapContext, SPRING_NETHER_OPEN, Feature.SPRING, new SpringConfiguration(Fluids.LAVA
/* 164 */           .defaultFluidState(), false, 4, 1, 
/*     */ 
/*     */ 
/*     */           
/* 168 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.NETHERRACK })));
/*     */ 
/*     */     
/* 171 */     FeatureUtils.register(paramBootstrapContext, PATCH_FIRE, Feature.RANDOM_PATCH, 
/*     */         
/* 173 */         FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/*     */             
/* 175 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.FIRE)), 
/* 176 */           List.of(Blocks.NETHERRACK)));
/*     */ 
/*     */     
/* 179 */     FeatureUtils.register(paramBootstrapContext, PATCH_SOUL_FIRE, Feature.RANDOM_PATCH, 
/*     */         
/* 181 */         FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 182 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.SOUL_FIRE)), 
/* 183 */           List.of(Blocks.SOUL_SOIL)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\features\NetherFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */