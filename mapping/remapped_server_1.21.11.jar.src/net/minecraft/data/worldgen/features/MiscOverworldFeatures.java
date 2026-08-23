/*     */ package net.minecraft.data.worldgen.features;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.LakeFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class MiscOverworldFeatures {
/*  24 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_SPIKE = FeatureUtils.createKey("ice_spike");
/*  25 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_PATCH = FeatureUtils.createKey("ice_patch");
/*  26 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_ROCK = FeatureUtils.createKey("forest_rock");
/*  27 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ICEBERG_PACKED = FeatureUtils.createKey("iceberg_packed");
/*  28 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ICEBERG_BLUE = FeatureUtils.createKey("iceberg_blue");
/*  29 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_ICE = FeatureUtils.createKey("blue_ice");
/*     */ 
/*     */ 
/*     */   
/*  33 */   public static final ResourceKey<ConfiguredFeature<?, ?>> LAKE_LAVA = FeatureUtils.createKey("lake_lava");
/*     */   
/*  35 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DISK_CLAY = FeatureUtils.createKey("disk_clay");
/*  36 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DISK_GRAVEL = FeatureUtils.createKey("disk_gravel");
/*  37 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DISK_SAND = FeatureUtils.createKey("disk_sand");
/*  38 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FREEZE_TOP_LAYER = FeatureUtils.createKey("freeze_top_layer");
/*     */   
/*  40 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DISK_GRASS = FeatureUtils.createKey("disk_grass");
/*     */   
/*  42 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BONUS_CHEST = FeatureUtils.createKey("bonus_chest");
/*  43 */   public static final ResourceKey<ConfiguredFeature<?, ?>> VOID_START_PLATFORM = FeatureUtils.createKey("void_start_platform");
/*  44 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DESERT_WELL = FeatureUtils.createKey("desert_well");
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_LAVA_OVERWORLD = FeatureUtils.createKey("spring_lava_overworld");
/*  49 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_LAVA_FROZEN = FeatureUtils.createKey("spring_lava_frozen");
/*  50 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_WATER = FeatureUtils.createKey("spring_water");
/*     */ 
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> paramBootstrapContext) {
/*  54 */     FeatureUtils.register(paramBootstrapContext, ICE_SPIKE, Feature.ICE_SPIKE);
/*  55 */     FeatureUtils.register(paramBootstrapContext, ICE_PATCH, Feature.DISK, new DiskConfiguration(
/*  56 */           RuleBasedBlockStateProvider.simple(Blocks.PACKED_ICE), 
/*  57 */           BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.MYCELIUM, Blocks.SNOW_BLOCK, Blocks.ICE)), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  66 */           (IntProvider)UniformInt.of(2, 3), 1));
/*     */ 
/*     */     
/*  69 */     FeatureUtils.register(paramBootstrapContext, FOREST_ROCK, Feature.FOREST_ROCK, new BlockStateConfiguration(Blocks.MOSSY_COBBLESTONE
/*  70 */           .defaultBlockState()));
/*     */     
/*  72 */     FeatureUtils.register(paramBootstrapContext, ICEBERG_PACKED, Feature.ICEBERG, new BlockStateConfiguration(Blocks.PACKED_ICE
/*  73 */           .defaultBlockState()));
/*     */     
/*  75 */     FeatureUtils.register(paramBootstrapContext, ICEBERG_BLUE, Feature.ICEBERG, new BlockStateConfiguration(Blocks.BLUE_ICE
/*  76 */           .defaultBlockState()));
/*     */     
/*  78 */     FeatureUtils.register(paramBootstrapContext, BLUE_ICE, Feature.BLUE_ICE);
/*     */ 
/*     */ 
/*     */     
/*  82 */     FeatureUtils.register(paramBootstrapContext, LAKE_LAVA, Feature.LAKE, new LakeFeature.Configuration(
/*  83 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.LAVA.defaultBlockState()), 
/*  84 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.STONE.defaultBlockState())));
/*     */ 
/*     */     
/*  87 */     FeatureUtils.register(paramBootstrapContext, DISK_CLAY, Feature.DISK, new DiskConfiguration(
/*  88 */           RuleBasedBlockStateProvider.simple(Blocks.CLAY), 
/*  89 */           BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.CLAY)), 
/*  90 */           (IntProvider)UniformInt.of(2, 3), 1));
/*     */ 
/*     */     
/*  93 */     FeatureUtils.register(paramBootstrapContext, DISK_GRAVEL, Feature.DISK, new DiskConfiguration(
/*  94 */           RuleBasedBlockStateProvider.simple(Blocks.GRAVEL), 
/*  95 */           BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK)), 
/*  96 */           (IntProvider)UniformInt.of(2, 5), 2));
/*     */ 
/*     */     
/*  99 */     FeatureUtils.register(paramBootstrapContext, DISK_SAND, Feature.DISK, new DiskConfiguration(new RuleBasedBlockStateProvider(
/* 100 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.SAND), 
/* 101 */             List.of(new RuleBasedBlockStateProvider.Rule(
/* 102 */                 BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.AIR
/* 103 */                   }), (BlockStateProvider)BlockStateProvider.simple(Blocks.SANDSTONE)))), 
/*     */ 
/*     */           
/* 106 */           BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK)), 
/* 107 */           (IntProvider)UniformInt.of(2, 6), 2));
/*     */ 
/*     */     
/* 110 */     FeatureUtils.register(paramBootstrapContext, FREEZE_TOP_LAYER, Feature.FREEZE_TOP_LAYER);
/*     */     
/* 112 */     FeatureUtils.register(paramBootstrapContext, DISK_GRASS, Feature.DISK, new DiskConfiguration(new RuleBasedBlockStateProvider(
/* 113 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.DIRT), 
/* 114 */             List.of(new RuleBasedBlockStateProvider.Rule(
/* 115 */                 BlockPredicate.not(BlockPredicate.anyOf(
/* 116 */                     BlockPredicate.solid(Direction.UP.getUnitVec3i()), 
/* 117 */                     BlockPredicate.matchesFluids(Direction.UP.getUnitVec3i(), new Fluid[] { (Fluid)Fluids.WATER
/*     */                       
/* 119 */                       }))), (BlockStateProvider)BlockStateProvider.simple(Blocks.GRASS_BLOCK)))), 
/*     */ 
/*     */           
/* 122 */           BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.MUD)), 
/* 123 */           (IntProvider)UniformInt.of(2, 6), 2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     FeatureUtils.register(paramBootstrapContext, BONUS_CHEST, (Feature<NoneFeatureConfiguration>)Feature.BONUS_CHEST);
/* 130 */     FeatureUtils.register(paramBootstrapContext, VOID_START_PLATFORM, Feature.VOID_START_PLATFORM);
/* 131 */     FeatureUtils.register(paramBootstrapContext, DESERT_WELL, Feature.DESERT_WELL);
/*     */ 
/*     */ 
/*     */     
/* 135 */     FeatureUtils.register(paramBootstrapContext, SPRING_LAVA_OVERWORLD, Feature.SPRING, new SpringConfiguration(Fluids.LAVA
/* 136 */           .defaultFluidState(), true, 4, 1, 
/*     */ 
/*     */ 
/*     */           
/* 140 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DEEPSLATE, Blocks.TUFF, Blocks.CALCITE, Blocks.DIRT })));
/*     */     
/* 142 */     FeatureUtils.register(paramBootstrapContext, SPRING_LAVA_FROZEN, Feature.SPRING, new SpringConfiguration(Fluids.LAVA
/* 143 */           .defaultFluidState(), true, 4, 1, 
/*     */ 
/*     */ 
/*     */           
/* 147 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW, Blocks.PACKED_ICE })));
/*     */     
/* 149 */     FeatureUtils.register(paramBootstrapContext, SPRING_WATER, Feature.SPRING, new SpringConfiguration(Fluids.WATER
/* 150 */           .defaultFluidState(), true, 4, 1, 
/*     */ 
/*     */ 
/*     */           
/* 154 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { 
/*     */               Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DEEPSLATE, Blocks.TUFF, Blocks.CALCITE, Blocks.DIRT, Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW, 
/*     */               Blocks.PACKED_ICE })));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\features\MiscOverworldFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */