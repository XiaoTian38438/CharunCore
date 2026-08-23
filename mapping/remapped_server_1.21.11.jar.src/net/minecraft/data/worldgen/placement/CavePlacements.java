/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.CaveFeatures;
/*     */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.ClampedNormalInt;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
/*     */ 
/*     */ public class CavePlacements {
/*  31 */   public static final ResourceKey<PlacedFeature> MONSTER_ROOM = PlacementUtils.createKey("monster_room");
/*  32 */   public static final ResourceKey<PlacedFeature> MONSTER_ROOM_DEEP = PlacementUtils.createKey("monster_room_deep");
/*     */ 
/*     */   
/*  35 */   public static final ResourceKey<PlacedFeature> FOSSIL_UPPER = PlacementUtils.createKey("fossil_upper");
/*  36 */   public static final ResourceKey<PlacedFeature> FOSSIL_LOWER = PlacementUtils.createKey("fossil_lower");
/*     */ 
/*     */   
/*  39 */   public static final ResourceKey<PlacedFeature> DRIPSTONE_CLUSTER = PlacementUtils.createKey("dripstone_cluster");
/*  40 */   public static final ResourceKey<PlacedFeature> LARGE_DRIPSTONE = PlacementUtils.createKey("large_dripstone");
/*  41 */   public static final ResourceKey<PlacedFeature> POINTED_DRIPSTONE = PlacementUtils.createKey("pointed_dripstone");
/*  42 */   public static final ResourceKey<PlacedFeature> UNDERWATER_MAGMA = PlacementUtils.createKey("underwater_magma");
/*  43 */   public static final ResourceKey<PlacedFeature> GLOW_LICHEN = PlacementUtils.createKey("glow_lichen");
/*  44 */   public static final ResourceKey<PlacedFeature> ROOTED_AZALEA_TREE = PlacementUtils.createKey("rooted_azalea_tree");
/*  45 */   public static final ResourceKey<PlacedFeature> CAVE_VINES = PlacementUtils.createKey("cave_vines");
/*  46 */   public static final ResourceKey<PlacedFeature> LUSH_CAVES_VEGETATION = PlacementUtils.createKey("lush_caves_vegetation");
/*  47 */   public static final ResourceKey<PlacedFeature> LUSH_CAVES_CLAY = PlacementUtils.createKey("lush_caves_clay");
/*  48 */   public static final ResourceKey<PlacedFeature> LUSH_CAVES_CEILING_VEGETATION = PlacementUtils.createKey("lush_caves_ceiling_vegetation");
/*  49 */   public static final ResourceKey<PlacedFeature> SPORE_BLOSSOM = PlacementUtils.createKey("spore_blossom");
/*  50 */   public static final ResourceKey<PlacedFeature> CLASSIC_VINES = PlacementUtils.createKey("classic_vines_cave_feature");
/*  51 */   public static final ResourceKey<PlacedFeature> AMETHYST_GEODE = PlacementUtils.createKey("amethyst_geode");
/*     */ 
/*     */   
/*  54 */   public static final ResourceKey<PlacedFeature> SCULK_PATCH_DEEP_DARK = PlacementUtils.createKey("sculk_patch_deep_dark");
/*  55 */   public static final ResourceKey<PlacedFeature> SCULK_PATCH_ANCIENT_CITY = PlacementUtils.createKey("sculk_patch_ancient_city");
/*  56 */   public static final ResourceKey<PlacedFeature> SCULK_VEIN = PlacementUtils.createKey("sculk_vein");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> paramBootstrapContext) {
/*  59 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.CONFIGURED_FEATURE);
/*  60 */     Holder.Reference reference1 = holderGetter.getOrThrow(CaveFeatures.MONSTER_ROOM);
/*  61 */     Holder.Reference reference2 = holderGetter.getOrThrow(CaveFeatures.FOSSIL_COAL);
/*  62 */     Holder.Reference reference3 = holderGetter.getOrThrow(CaveFeatures.FOSSIL_DIAMONDS);
/*  63 */     Holder.Reference reference4 = holderGetter.getOrThrow(CaveFeatures.DRIPSTONE_CLUSTER);
/*  64 */     Holder.Reference reference5 = holderGetter.getOrThrow(CaveFeatures.LARGE_DRIPSTONE);
/*  65 */     Holder.Reference reference6 = holderGetter.getOrThrow(CaveFeatures.POINTED_DRIPSTONE);
/*  66 */     Holder.Reference reference7 = holderGetter.getOrThrow(CaveFeatures.UNDERWATER_MAGMA);
/*  67 */     Holder.Reference reference8 = holderGetter.getOrThrow(CaveFeatures.GLOW_LICHEN);
/*  68 */     Holder.Reference reference9 = holderGetter.getOrThrow(CaveFeatures.ROOTED_AZALEA_TREE);
/*  69 */     Holder.Reference reference10 = holderGetter.getOrThrow(CaveFeatures.CAVE_VINE);
/*  70 */     Holder.Reference reference11 = holderGetter.getOrThrow(CaveFeatures.MOSS_PATCH);
/*  71 */     Holder.Reference reference12 = holderGetter.getOrThrow(CaveFeatures.LUSH_CAVES_CLAY);
/*  72 */     Holder.Reference reference13 = holderGetter.getOrThrow(CaveFeatures.MOSS_PATCH_CEILING);
/*  73 */     Holder.Reference reference14 = holderGetter.getOrThrow(CaveFeatures.SPORE_BLOSSOM);
/*  74 */     Holder.Reference reference15 = holderGetter.getOrThrow(VegetationFeatures.VINES);
/*  75 */     Holder.Reference reference16 = holderGetter.getOrThrow(CaveFeatures.AMETHYST_GEODE);
/*  76 */     Holder.Reference reference17 = holderGetter.getOrThrow(CaveFeatures.SCULK_PATCH_DEEP_DARK);
/*  77 */     Holder.Reference reference18 = holderGetter.getOrThrow(CaveFeatures.SCULK_PATCH_ANCIENT_CITY);
/*  78 */     Holder.Reference reference19 = holderGetter.getOrThrow(CaveFeatures.SCULK_VEIN);
/*     */ 
/*     */     
/*  81 */     PlacementUtils.register(paramBootstrapContext, MONSTER_ROOM, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] {
/*  82 */           (PlacementModifier)CountPlacement.of(10), 
/*  83 */           (PlacementModifier)InSquarePlacement.spread(), 
/*  84 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.top()), 
/*  85 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*  87 */     PlacementUtils.register(paramBootstrapContext, MONSTER_ROOM_DEEP, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] {
/*  88 */           (PlacementModifier)CountPlacement.of(4), 
/*  89 */           (PlacementModifier)InSquarePlacement.spread(), 
/*  90 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(-1)), 
/*  91 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */ 
/*     */ 
/*     */     
/*  96 */     PlacementUtils.register(paramBootstrapContext, FOSSIL_UPPER, (Holder<ConfiguredFeature<?, ?>>)reference2, new PlacementModifier[] {
/*  97 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(64), 
/*  98 */           (PlacementModifier)InSquarePlacement.spread(), 
/*  99 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.top()), 
/* 100 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 102 */     PlacementUtils.register(paramBootstrapContext, FOSSIL_LOWER, (Holder<ConfiguredFeature<?, ?>>)reference3, new PlacementModifier[] {
/* 103 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(64), 
/* 104 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 105 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(-8)), 
/* 106 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 111 */     PlacementUtils.register(paramBootstrapContext, DRIPSTONE_CLUSTER, (Holder<ConfiguredFeature<?, ?>>)reference4, new PlacementModifier[] {
/* 112 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(48, 96)), 
/* 113 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 115 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 117 */     PlacementUtils.register(paramBootstrapContext, LARGE_DRIPSTONE, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[] {
/* 118 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(10, 48)), 
/* 119 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 121 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 123 */     PlacementUtils.register(paramBootstrapContext, POINTED_DRIPSTONE, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] {
/* 124 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(192, 256)), 
/* 125 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 127 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(1, 5)), 
/* 128 */           (PlacementModifier)RandomOffsetPlacement.of(
/* 129 */             (IntProvider)ClampedNormalInt.of(0.0F, 3.0F, -10, 10), 
/* 130 */             (IntProvider)ClampedNormalInt.of(0.0F, 0.6F, -2, 2)), 
/*     */           
/* 132 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 134 */     PlacementUtils.register(paramBootstrapContext, UNDERWATER_MAGMA, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] {
/* 135 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(44, 52)), 
/* 136 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 138 */           (PlacementModifier)SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, -2147483648, -2), 
/* 139 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 141 */     PlacementUtils.register(paramBootstrapContext, GLOW_LICHEN, (Holder<ConfiguredFeature<?, ?>>)reference8, new PlacementModifier[] {
/* 142 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(104, 157)), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 144 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 145 */           (PlacementModifier)SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, -2147483648, -13), 
/* 146 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 148 */     PlacementUtils.register(paramBootstrapContext, ROOTED_AZALEA_TREE, (Holder<ConfiguredFeature<?, ?>>)reference9, new PlacementModifier[] {
/* 149 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(1, 2)), 
/* 150 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 152 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 153 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 154 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 156 */     PlacementUtils.register(paramBootstrapContext, CAVE_VINES, (Holder<ConfiguredFeature<?, ?>>)reference10, new PlacementModifier[] {
/* 157 */           (PlacementModifier)CountPlacement.of(188), 
/* 158 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 160 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 161 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 162 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 164 */     PlacementUtils.register(paramBootstrapContext, LUSH_CAVES_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference11, new PlacementModifier[] {
/* 165 */           (PlacementModifier)CountPlacement.of(125), 
/* 166 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 168 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 169 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(1)), 
/* 170 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 172 */     PlacementUtils.register(paramBootstrapContext, LUSH_CAVES_CLAY, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] {
/* 173 */           (PlacementModifier)CountPlacement.of(62), 
/* 174 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 176 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 177 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(1)), 
/* 178 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 180 */     PlacementUtils.register(paramBootstrapContext, LUSH_CAVES_CEILING_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] {
/* 181 */           (PlacementModifier)CountPlacement.of(125), 
/* 182 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 184 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 185 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 186 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 188 */     PlacementUtils.register(paramBootstrapContext, SPORE_BLOSSOM, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] {
/* 189 */           (PlacementModifier)CountPlacement.of(25), 
/* 190 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 192 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 193 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 194 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 196 */     PlacementUtils.register(paramBootstrapContext, CLASSIC_VINES, (Holder<ConfiguredFeature<?, ?>>)reference15, new PlacementModifier[] {
/* 197 */           (PlacementModifier)CountPlacement.of(256), 
/* 198 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 200 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 202 */     PlacementUtils.register(paramBootstrapContext, AMETHYST_GEODE, (Holder<ConfiguredFeature<?, ?>>)reference16, new PlacementModifier[] {
/* 203 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(24), 
/* 204 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 205 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30)), 
/* 206 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 209 */     PlacementUtils.register(paramBootstrapContext, SCULK_PATCH_DEEP_DARK, (Holder<ConfiguredFeature<?, ?>>)reference17, new PlacementModifier[] {
/* 210 */           (PlacementModifier)CountPlacement.of((IntProvider)ConstantInt.of(256)), 
/* 211 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 213 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 216 */     PlacementUtils.register(paramBootstrapContext, SCULK_PATCH_ANCIENT_CITY, (Holder<ConfiguredFeature<?, ?>>)reference18, new PlacementModifier[0]);
/*     */     
/* 218 */     PlacementUtils.register(paramBootstrapContext, SCULK_VEIN, (Holder<ConfiguredFeature<?, ?>>)reference19, new PlacementModifier[] {
/* 219 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(204, 250)), 
/* 220 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 222 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\placement\CavePlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */