/*     */ package net.minecraft.data.worldgen.placement;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.VeryBiasedToBottomHeight;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class MiscOverworldPlacements {
/*  33 */   public static final ResourceKey<PlacedFeature> ICE_SPIKE = PlacementUtils.createKey("ice_spike");
/*  34 */   public static final ResourceKey<PlacedFeature> ICE_PATCH = PlacementUtils.createKey("ice_patch");
/*  35 */   public static final ResourceKey<PlacedFeature> FOREST_ROCK = PlacementUtils.createKey("forest_rock");
/*  36 */   public static final ResourceKey<PlacedFeature> ICEBERG_PACKED = PlacementUtils.createKey("iceberg_packed");
/*  37 */   public static final ResourceKey<PlacedFeature> ICEBERG_BLUE = PlacementUtils.createKey("iceberg_blue");
/*  38 */   public static final ResourceKey<PlacedFeature> BLUE_ICE = PlacementUtils.createKey("blue_ice");
/*     */   
/*  40 */   public static final ResourceKey<PlacedFeature> LAKE_LAVA_UNDERGROUND = PlacementUtils.createKey("lake_lava_underground");
/*  41 */   public static final ResourceKey<PlacedFeature> LAKE_LAVA_SURFACE = PlacementUtils.createKey("lake_lava_surface");
/*     */   
/*  43 */   public static final ResourceKey<PlacedFeature> DISK_CLAY = PlacementUtils.createKey("disk_clay");
/*  44 */   public static final ResourceKey<PlacedFeature> DISK_GRAVEL = PlacementUtils.createKey("disk_gravel");
/*  45 */   public static final ResourceKey<PlacedFeature> DISK_SAND = PlacementUtils.createKey("disk_sand");
/*     */   
/*  47 */   public static final ResourceKey<PlacedFeature> DISK_GRASS = PlacementUtils.createKey("disk_grass");
/*     */   
/*  49 */   public static final ResourceKey<PlacedFeature> FREEZE_TOP_LAYER = PlacementUtils.createKey("freeze_top_layer");
/*  50 */   public static final ResourceKey<PlacedFeature> VOID_START_PLATFORM = PlacementUtils.createKey("void_start_platform");
/*     */   
/*  52 */   public static final ResourceKey<PlacedFeature> DESERT_WELL = PlacementUtils.createKey("desert_well");
/*     */   
/*  54 */   public static final ResourceKey<PlacedFeature> SPRING_LAVA = PlacementUtils.createKey("spring_lava");
/*  55 */   public static final ResourceKey<PlacedFeature> SPRING_LAVA_FROZEN = PlacementUtils.createKey("spring_lava_frozen");
/*  56 */   public static final ResourceKey<PlacedFeature> SPRING_WATER = PlacementUtils.createKey("spring_water");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> paramBootstrapContext) {
/*  59 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.CONFIGURED_FEATURE);
/*  60 */     Holder.Reference reference1 = holderGetter.getOrThrow(MiscOverworldFeatures.ICE_SPIKE);
/*  61 */     Holder.Reference reference2 = holderGetter.getOrThrow(MiscOverworldFeatures.ICE_PATCH);
/*  62 */     Holder.Reference reference3 = holderGetter.getOrThrow(MiscOverworldFeatures.FOREST_ROCK);
/*  63 */     Holder.Reference reference4 = holderGetter.getOrThrow(MiscOverworldFeatures.ICEBERG_PACKED);
/*  64 */     Holder.Reference reference5 = holderGetter.getOrThrow(MiscOverworldFeatures.ICEBERG_BLUE);
/*  65 */     Holder.Reference reference6 = holderGetter.getOrThrow(MiscOverworldFeatures.BLUE_ICE);
/*  66 */     Holder.Reference reference7 = holderGetter.getOrThrow(MiscOverworldFeatures.LAKE_LAVA);
/*  67 */     Holder.Reference reference8 = holderGetter.getOrThrow(MiscOverworldFeatures.DISK_CLAY);
/*  68 */     Holder.Reference reference9 = holderGetter.getOrThrow(MiscOverworldFeatures.DISK_GRAVEL);
/*  69 */     Holder.Reference reference10 = holderGetter.getOrThrow(MiscOverworldFeatures.DISK_SAND);
/*  70 */     Holder.Reference reference11 = holderGetter.getOrThrow(MiscOverworldFeatures.DISK_GRASS);
/*  71 */     Holder.Reference reference12 = holderGetter.getOrThrow(MiscOverworldFeatures.FREEZE_TOP_LAYER);
/*  72 */     Holder.Reference reference13 = holderGetter.getOrThrow(MiscOverworldFeatures.VOID_START_PLATFORM);
/*  73 */     Holder.Reference reference14 = holderGetter.getOrThrow(MiscOverworldFeatures.DESERT_WELL);
/*  74 */     Holder.Reference reference15 = holderGetter.getOrThrow(MiscOverworldFeatures.SPRING_LAVA_OVERWORLD);
/*  75 */     Holder.Reference reference16 = holderGetter.getOrThrow(MiscOverworldFeatures.SPRING_LAVA_FROZEN);
/*  76 */     Holder.Reference reference17 = holderGetter.getOrThrow(MiscOverworldFeatures.SPRING_WATER);
/*     */     
/*  78 */     PlacementUtils.register(paramBootstrapContext, ICE_SPIKE, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] {
/*  79 */           (PlacementModifier)CountPlacement.of(3), 
/*  80 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/*  82 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*  84 */     PlacementUtils.register(paramBootstrapContext, ICE_PATCH, (Holder<ConfiguredFeature<?, ?>>)reference2, new PlacementModifier[] {
/*  85 */           (PlacementModifier)CountPlacement.of(2), 
/*  86 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/*  88 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/*  89 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(new Block[] { Blocks.SNOW_BLOCK
/*  90 */               })), (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*  92 */     PlacementUtils.register(paramBootstrapContext, FOREST_ROCK, (Holder<ConfiguredFeature<?, ?>>)reference3, new PlacementModifier[] {
/*  93 */           (PlacementModifier)CountPlacement.of(2), 
/*  94 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/*  96 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*  98 */     PlacementUtils.register(paramBootstrapContext, ICEBERG_BLUE, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[] {
/*  99 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(200), 
/* 100 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 101 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 103 */     PlacementUtils.register(paramBootstrapContext, ICEBERG_PACKED, (Holder<ConfiguredFeature<?, ?>>)reference4, new PlacementModifier[] {
/* 104 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(16), 
/* 105 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 106 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 108 */     PlacementUtils.register(paramBootstrapContext, BLUE_ICE, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] {
/* 109 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(0, 19)), 
/* 110 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 111 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(30), VerticalAnchor.absolute(61)), 
/* 112 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 115 */     PlacementUtils.register(paramBootstrapContext, LAKE_LAVA_UNDERGROUND, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] {
/* 116 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(9), 
/* 117 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 118 */           (PlacementModifier)HeightRangePlacement.of((HeightProvider)UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.top())), 
/* 119 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.DOWN, 
/*     */             
/* 121 */             BlockPredicate.allOf(
/* 122 */               BlockPredicate.not(BlockPredicate.ONLY_IN_AIR_PREDICATE), 
/* 123 */               BlockPredicate.insideWorld((Vec3i)new BlockPos(0, -5, 0))), 32), 
/*     */ 
/*     */ 
/*     */           
/* 127 */           (PlacementModifier)SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, -2147483648, -5), 
/* 128 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 131 */     PlacementUtils.register(paramBootstrapContext, LAKE_LAVA_SURFACE, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] {
/* 132 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(200), 
/* 133 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 135 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 138 */     PlacementUtils.register(paramBootstrapContext, DISK_CLAY, (Holder<ConfiguredFeature<?, ?>>)reference8, new PlacementModifier[] {
/* 139 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 141 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(new Fluid[] { (Fluid)Fluids.WATER
/* 142 */               })), (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 144 */     PlacementUtils.register(paramBootstrapContext, DISK_GRAVEL, (Holder<ConfiguredFeature<?, ?>>)reference9, new PlacementModifier[] {
/* 145 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 147 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(new Fluid[] { (Fluid)Fluids.WATER
/* 148 */               })), (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 150 */     PlacementUtils.register(paramBootstrapContext, DISK_SAND, (Holder<ConfiguredFeature<?, ?>>)reference10, new PlacementModifier[] {
/* 151 */           (PlacementModifier)CountPlacement.of(3), 
/* 152 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 154 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(new Fluid[] { (Fluid)Fluids.WATER
/* 155 */               })), (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 158 */     PlacementUtils.register(paramBootstrapContext, DISK_GRASS, (Holder<ConfiguredFeature<?, ?>>)reference11, new PlacementModifier[] {
/* 159 */           (PlacementModifier)CountPlacement.of(1), 
/* 160 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 162 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 163 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(new Block[] { Blocks.MUD
/* 164 */               })), (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 167 */     PlacementUtils.register(paramBootstrapContext, FREEZE_TOP_LAYER, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] {
/* 168 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 170 */     PlacementUtils.register(paramBootstrapContext, VOID_START_PLATFORM, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] {
/* 171 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 174 */     PlacementUtils.register(paramBootstrapContext, DESERT_WELL, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] {
/* 175 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(1000), 
/* 176 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 178 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 181 */     PlacementUtils.register(paramBootstrapContext, SPRING_LAVA, (Holder<ConfiguredFeature<?, ?>>)reference15, new PlacementModifier[] {
/* 182 */           (PlacementModifier)CountPlacement.of(20), 
/* 183 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 184 */           (PlacementModifier)HeightRangePlacement.of((HeightProvider)VeryBiasedToBottomHeight.of(VerticalAnchor.bottom(), VerticalAnchor.belowTop(8), 8)), 
/* 185 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 187 */     PlacementUtils.register(paramBootstrapContext, SPRING_LAVA_FROZEN, (Holder<ConfiguredFeature<?, ?>>)reference16, new PlacementModifier[] {
/* 188 */           (PlacementModifier)CountPlacement.of(20), 
/* 189 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 190 */           (PlacementModifier)HeightRangePlacement.of((HeightProvider)VeryBiasedToBottomHeight.of(VerticalAnchor.bottom(), VerticalAnchor.belowTop(8), 8)), 
/* 191 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 193 */     PlacementUtils.register(paramBootstrapContext, SPRING_WATER, (Holder<ConfiguredFeature<?, ?>>)reference17, new PlacementModifier[] {
/* 194 */           (PlacementModifier)CountPlacement.of(25), 
/* 195 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 196 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(192)), 
/* 197 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\placement\MiscOverworldPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */