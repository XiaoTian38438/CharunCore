/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.TreeFeatures;
/*     */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.ClampedInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
/*     */ 
/*     */ public class VegetationPlacements {
/*  35 */   public static final ResourceKey<PlacedFeature> BAMBOO_LIGHT = PlacementUtils.createKey("bamboo_light");
/*  36 */   public static final ResourceKey<PlacedFeature> BAMBOO = PlacementUtils.createKey("bamboo");
/*  37 */   public static final ResourceKey<PlacedFeature> VINES = PlacementUtils.createKey("vines");
/*     */   
/*  39 */   public static final ResourceKey<PlacedFeature> PATCH_SUNFLOWER = PlacementUtils.createKey("patch_sunflower");
/*  40 */   public static final ResourceKey<PlacedFeature> PATCH_PUMPKIN = PlacementUtils.createKey("patch_pumpkin");
/*     */   
/*  42 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_PLAIN = PlacementUtils.createKey("patch_grass_plain");
/*  43 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_MEADOW = PlacementUtils.createKey("patch_grass_meadow");
/*  44 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_FOREST = PlacementUtils.createKey("patch_grass_forest");
/*  45 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_BADLANDS = PlacementUtils.createKey("patch_grass_badlands");
/*  46 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_SAVANNA = PlacementUtils.createKey("patch_grass_savanna");
/*  47 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_NORMAL = PlacementUtils.createKey("patch_grass_normal");
/*  48 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_TAIGA_2 = PlacementUtils.createKey("patch_grass_taiga_2");
/*  49 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_TAIGA = PlacementUtils.createKey("patch_grass_taiga");
/*  50 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_JUNGLE = PlacementUtils.createKey("patch_grass_jungle");
/*     */   
/*  52 */   public static final ResourceKey<PlacedFeature> GRASS_BONEMEAL = PlacementUtils.createKey("grass_bonemeal");
/*     */   
/*  54 */   public static final ResourceKey<PlacedFeature> PATCH_DEAD_BUSH_2 = PlacementUtils.createKey("patch_dead_bush_2");
/*  55 */   public static final ResourceKey<PlacedFeature> PATCH_DEAD_BUSH = PlacementUtils.createKey("patch_dead_bush");
/*  56 */   public static final ResourceKey<PlacedFeature> PATCH_DEAD_BUSH_BADLANDS = PlacementUtils.createKey("patch_dead_bush_badlands");
/*  57 */   public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_BADLANDS = PlacementUtils.createKey("patch_dry_grass_badlands");
/*  58 */   public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_DESERT = PlacementUtils.createKey("patch_dry_grass_desert");
/*     */   
/*  60 */   public static final ResourceKey<PlacedFeature> PATCH_MELON = PlacementUtils.createKey("patch_melon");
/*     */   
/*  62 */   public static final ResourceKey<PlacedFeature> PATCH_MELON_SPARSE = PlacementUtils.createKey("patch_melon_sparse");
/*     */   
/*  64 */   public static final ResourceKey<PlacedFeature> PATCH_BERRY_COMMON = PlacementUtils.createKey("patch_berry_common");
/*  65 */   public static final ResourceKey<PlacedFeature> PATCH_BERRY_RARE = PlacementUtils.createKey("patch_berry_rare");
/*     */   
/*  67 */   public static final ResourceKey<PlacedFeature> PATCH_WATERLILY = PlacementUtils.createKey("patch_waterlily");
/*     */   
/*  69 */   public static final ResourceKey<PlacedFeature> PATCH_TALL_GRASS_2 = PlacementUtils.createKey("patch_tall_grass_2");
/*  70 */   public static final ResourceKey<PlacedFeature> PATCH_TALL_GRASS = PlacementUtils.createKey("patch_tall_grass");
/*  71 */   public static final ResourceKey<PlacedFeature> PATCH_LARGE_FERN = PlacementUtils.createKey("patch_large_fern");
/*  72 */   public static final ResourceKey<PlacedFeature> PATCH_BUSH = PlacementUtils.createKey("patch_bush");
/*  73 */   public static final ResourceKey<PlacedFeature> PATCH_LEAF_LITTER = PlacementUtils.createKey("patch_leaf_litter");
/*     */   
/*  75 */   public static final ResourceKey<PlacedFeature> PATCH_CACTUS_DESERT = PlacementUtils.createKey("patch_cactus_desert");
/*  76 */   public static final ResourceKey<PlacedFeature> PATCH_CACTUS_DECORATED = PlacementUtils.createKey("patch_cactus_decorated");
/*     */   
/*  78 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_SWAMP = PlacementUtils.createKey("patch_sugar_cane_swamp");
/*  79 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_DESERT = PlacementUtils.createKey("patch_sugar_cane_desert");
/*  80 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_BADLANDS = PlacementUtils.createKey("patch_sugar_cane_badlands");
/*  81 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE = PlacementUtils.createKey("patch_sugar_cane");
/*     */   
/*  83 */   public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_SWAMP = PlacementUtils.createKey("patch_firefly_bush_swamp");
/*  84 */   public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP = PlacementUtils.createKey("patch_firefly_bush_near_water_swamp");
/*  85 */   public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER = PlacementUtils.createKey("patch_firefly_bush_near_water");
/*     */   
/*  87 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_NETHER = PlacementUtils.createKey("brown_mushroom_nether");
/*  88 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_NETHER = PlacementUtils.createKey("red_mushroom_nether");
/*  89 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_NORMAL = PlacementUtils.createKey("brown_mushroom_normal");
/*  90 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_NORMAL = PlacementUtils.createKey("red_mushroom_normal");
/*  91 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_TAIGA = PlacementUtils.createKey("brown_mushroom_taiga");
/*  92 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_TAIGA = PlacementUtils.createKey("red_mushroom_taiga");
/*  93 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_OLD_GROWTH = PlacementUtils.createKey("brown_mushroom_old_growth");
/*  94 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_OLD_GROWTH = PlacementUtils.createKey("red_mushroom_old_growth");
/*  95 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_SWAMP = PlacementUtils.createKey("brown_mushroom_swamp");
/*  96 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_SWAMP = PlacementUtils.createKey("red_mushroom_swamp");
/*     */   
/*  98 */   public static final ResourceKey<PlacedFeature> FLOWER_WARM = PlacementUtils.createKey("flower_warm");
/*  99 */   public static final ResourceKey<PlacedFeature> FLOWER_DEFAULT = PlacementUtils.createKey("flower_default");
/* 100 */   public static final ResourceKey<PlacedFeature> FLOWER_FLOWER_FOREST = PlacementUtils.createKey("flower_flower_forest");
/* 101 */   public static final ResourceKey<PlacedFeature> FLOWER_SWAMP = PlacementUtils.createKey("flower_swamp");
/* 102 */   public static final ResourceKey<PlacedFeature> FLOWER_PLAINS = PlacementUtils.createKey("flower_plains");
/* 103 */   public static final ResourceKey<PlacedFeature> FLOWER_MEADOW = PlacementUtils.createKey("flower_meadow");
/* 104 */   public static final ResourceKey<PlacedFeature> FLOWER_CHERRY = PlacementUtils.createKey("flower_cherry");
/* 105 */   public static final ResourceKey<PlacedFeature> FLOWER_PALE_GARDEN = PlacementUtils.createKey("flower_pale_garden");
/* 106 */   public static final ResourceKey<PlacedFeature> WILDFLOWERS_BIRCH_FOREST = PlacementUtils.createKey("wildflowers_birch_forest");
/* 107 */   public static final ResourceKey<PlacedFeature> WILDFLOWERS_MEADOW = PlacementUtils.createKey("wildflowers_meadow");
/*     */   
/* 109 */   public static final ResourceKey<PlacedFeature> TREES_PLAINS = PlacementUtils.createKey("trees_plains");
/* 110 */   public static final ResourceKey<PlacedFeature> DARK_FOREST_VEGETATION = PlacementUtils.createKey("dark_forest_vegetation");
/* 111 */   public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = PlacementUtils.createKey("pale_garden_vegetation");
/* 112 */   public static final ResourceKey<PlacedFeature> FLOWER_FOREST_FLOWERS = PlacementUtils.createKey("flower_forest_flowers");
/* 113 */   public static final ResourceKey<PlacedFeature> FOREST_FLOWERS = PlacementUtils.createKey("forest_flowers");
/* 114 */   public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = PlacementUtils.createKey("pale_garden_flowers");
/* 115 */   public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = PlacementUtils.createKey("pale_moss_patch");
/*     */   
/* 117 */   public static final ResourceKey<PlacedFeature> TREES_FLOWER_FOREST = PlacementUtils.createKey("trees_flower_forest");
/* 118 */   public static final ResourceKey<PlacedFeature> TREES_MEADOW = PlacementUtils.createKey("trees_meadow");
/* 119 */   public static final ResourceKey<PlacedFeature> TREES_CHERRY = PlacementUtils.createKey("trees_cherry");
/* 120 */   public static final ResourceKey<PlacedFeature> TREES_TAIGA = PlacementUtils.createKey("trees_taiga");
/* 121 */   public static final ResourceKey<PlacedFeature> TREES_GROVE = PlacementUtils.createKey("trees_grove");
/* 122 */   public static final ResourceKey<PlacedFeature> TREES_BADLANDS = PlacementUtils.createKey("trees_badlands");
/* 123 */   public static final ResourceKey<PlacedFeature> TREES_SNOWY = PlacementUtils.createKey("trees_snowy");
/* 124 */   public static final ResourceKey<PlacedFeature> TREES_SWAMP = PlacementUtils.createKey("trees_swamp");
/* 125 */   public static final ResourceKey<PlacedFeature> TREES_WINDSWEPT_SAVANNA = PlacementUtils.createKey("trees_windswept_savanna");
/* 126 */   public static final ResourceKey<PlacedFeature> TREES_SAVANNA = PlacementUtils.createKey("trees_savanna");
/* 127 */   public static final ResourceKey<PlacedFeature> BIRCH_TALL = PlacementUtils.createKey("birch_tall");
/* 128 */   public static final ResourceKey<PlacedFeature> TREES_BIRCH = PlacementUtils.createKey("trees_birch");
/* 129 */   public static final ResourceKey<PlacedFeature> TREES_WINDSWEPT_FOREST = PlacementUtils.createKey("trees_windswept_forest");
/* 130 */   public static final ResourceKey<PlacedFeature> TREES_WINDSWEPT_HILLS = PlacementUtils.createKey("trees_windswept_hills");
/* 131 */   public static final ResourceKey<PlacedFeature> TREES_WATER = PlacementUtils.createKey("trees_water");
/* 132 */   public static final ResourceKey<PlacedFeature> TREES_BIRCH_AND_OAK_LEAF_LITTER = PlacementUtils.createKey("trees_birch_and_oak_leaf_litter");
/* 133 */   public static final ResourceKey<PlacedFeature> TREES_SPARSE_JUNGLE = PlacementUtils.createKey("trees_sparse_jungle");
/* 134 */   public static final ResourceKey<PlacedFeature> TREES_OLD_GROWTH_SPRUCE_TAIGA = PlacementUtils.createKey("trees_old_growth_spruce_taiga");
/* 135 */   public static final ResourceKey<PlacedFeature> TREES_OLD_GROWTH_PINE_TAIGA = PlacementUtils.createKey("trees_old_growth_pine_taiga");
/* 136 */   public static final ResourceKey<PlacedFeature> TREES_JUNGLE = PlacementUtils.createKey("trees_jungle");
/* 137 */   public static final ResourceKey<PlacedFeature> BAMBOO_VEGETATION = PlacementUtils.createKey("bamboo_vegetation");
/*     */   
/* 139 */   public static final ResourceKey<PlacedFeature> MUSHROOM_ISLAND_VEGETATION = PlacementUtils.createKey("mushroom_island_vegetation");
/*     */   
/* 141 */   public static final ResourceKey<PlacedFeature> TREES_MANGROVE = PlacementUtils.createKey("trees_mangrove");
/*     */   
/* 143 */   private static final PlacementModifier TREE_THRESHOLD = (PlacementModifier)SurfaceWaterDepthFilter.forMaxDepth(0);
/*     */   
/*     */   public static List<PlacementModifier> worldSurfaceSquaredWithCount(int paramInt) {
/* 146 */     return (List)List.of(
/* 147 */         CountPlacement.of(paramInt), 
/* 148 */         InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */         
/* 150 */         BiomeFilter.biome());
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<PlacementModifier> getMushroomPlacement(int paramInt, PlacementModifier paramPlacementModifier) {
/* 155 */     ImmutableList.Builder builder = ImmutableList.builder();
/* 156 */     if (paramPlacementModifier != null) {
/* 157 */       builder.add(paramPlacementModifier);
/*     */     }
/* 159 */     if (paramInt != 0) {
/* 160 */       builder.add(RarityFilter.onAverageOnceEvery(paramInt));
/*     */     }
/* 162 */     builder.add(InSquarePlacement.spread());
/* 163 */     builder.add(PlacementUtils.HEIGHTMAP);
/* 164 */     builder.add(BiomeFilter.biome());
/* 165 */     return (List<PlacementModifier>)builder.build();
/*     */   }
/*     */   
/*     */   private static ImmutableList.Builder<PlacementModifier> treePlacementBase(PlacementModifier paramPlacementModifier) {
/* 169 */     return ImmutableList.builder()
/* 170 */       .add(paramPlacementModifier)
/* 171 */       .add(InSquarePlacement.spread())
/* 172 */       .add(TREE_THRESHOLD)
/* 173 */       .add(PlacementUtils.HEIGHTMAP_OCEAN_FLOOR)
/* 174 */       .add(BiomeFilter.biome());
/*     */   }
/*     */   
/*     */   public static List<PlacementModifier> treePlacement(PlacementModifier paramPlacementModifier) {
/* 178 */     return (List<PlacementModifier>)treePlacementBase(paramPlacementModifier).build();
/*     */   }
/*     */   
/*     */   public static List<PlacementModifier> treePlacement(PlacementModifier paramPlacementModifier, Block paramBlock) {
/* 182 */     return (List<PlacementModifier>)treePlacementBase(paramPlacementModifier)
/* 183 */       .add(BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(paramBlock.defaultBlockState(), (Vec3i)BlockPos.ZERO)))
/* 184 */       .build();
/*     */   }
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> paramBootstrapContext) {
/* 188 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.CONFIGURED_FEATURE);
/* 189 */     Holder.Reference reference1 = holderGetter.getOrThrow(VegetationFeatures.BAMBOO_NO_PODZOL);
/* 190 */     Holder.Reference reference2 = holderGetter.getOrThrow(VegetationFeatures.BAMBOO_SOME_PODZOL);
/* 191 */     Holder.Reference reference3 = holderGetter.getOrThrow(VegetationFeatures.VINES);
/* 192 */     Holder.Reference reference4 = holderGetter.getOrThrow(VegetationFeatures.PATCH_SUNFLOWER);
/* 193 */     Holder.Reference reference5 = holderGetter.getOrThrow(VegetationFeatures.PATCH_PUMPKIN);
/* 194 */     Holder.Reference reference6 = holderGetter.getOrThrow(VegetationFeatures.PATCH_GRASS);
/* 195 */     Holder.Reference reference7 = holderGetter.getOrThrow(VegetationFeatures.PATCH_GRASS_MEADOW);
/* 196 */     Holder.Reference reference8 = holderGetter.getOrThrow(VegetationFeatures.PATCH_LEAF_LITTER);
/* 197 */     Holder.Reference reference9 = holderGetter.getOrThrow(VegetationFeatures.PATCH_TAIGA_GRASS);
/* 198 */     Holder.Reference reference10 = holderGetter.getOrThrow(VegetationFeatures.PATCH_GRASS_JUNGLE);
/* 199 */     Holder.Reference reference11 = holderGetter.getOrThrow(VegetationFeatures.SINGLE_PIECE_OF_GRASS);
/* 200 */     Holder.Reference reference12 = holderGetter.getOrThrow(VegetationFeatures.PATCH_DEAD_BUSH);
/* 201 */     Holder.Reference reference13 = holderGetter.getOrThrow(VegetationFeatures.PATCH_DRY_GRASS);
/* 202 */     Holder.Reference reference14 = holderGetter.getOrThrow(VegetationFeatures.PATCH_FIREFLY_BUSH);
/* 203 */     Holder.Reference reference15 = holderGetter.getOrThrow(VegetationFeatures.PATCH_MELON);
/* 204 */     Holder.Reference reference16 = holderGetter.getOrThrow(VegetationFeatures.PATCH_BERRY_BUSH);
/* 205 */     Holder.Reference reference17 = holderGetter.getOrThrow(VegetationFeatures.PATCH_WATERLILY);
/* 206 */     Holder.Reference reference18 = holderGetter.getOrThrow(VegetationFeatures.PATCH_TALL_GRASS);
/* 207 */     Holder.Reference reference19 = holderGetter.getOrThrow(VegetationFeatures.PATCH_LARGE_FERN);
/* 208 */     Holder.Reference reference20 = holderGetter.getOrThrow(VegetationFeatures.PATCH_BUSH);
/* 209 */     Holder.Reference reference21 = holderGetter.getOrThrow(VegetationFeatures.PATCH_CACTUS);
/* 210 */     Holder.Reference reference22 = holderGetter.getOrThrow(VegetationFeatures.PATCH_SUGAR_CANE);
/* 211 */     Holder.Reference reference23 = holderGetter.getOrThrow(VegetationFeatures.PATCH_BROWN_MUSHROOM);
/* 212 */     Holder.Reference reference24 = holderGetter.getOrThrow(VegetationFeatures.PATCH_RED_MUSHROOM);
/* 213 */     Holder.Reference reference25 = holderGetter.getOrThrow(VegetationFeatures.FLOWER_DEFAULT);
/* 214 */     Holder.Reference reference26 = holderGetter.getOrThrow(VegetationFeatures.FLOWER_FLOWER_FOREST);
/* 215 */     Holder.Reference reference27 = holderGetter.getOrThrow(VegetationFeatures.FLOWER_SWAMP);
/* 216 */     Holder.Reference reference28 = holderGetter.getOrThrow(VegetationFeatures.FLOWER_PLAIN);
/* 217 */     Holder.Reference reference29 = holderGetter.getOrThrow(VegetationFeatures.FLOWER_MEADOW);
/* 218 */     Holder.Reference reference30 = holderGetter.getOrThrow(VegetationFeatures.FLOWER_CHERRY);
/* 219 */     Holder.Reference reference31 = holderGetter.getOrThrow(VegetationFeatures.FLOWER_PALE_GARDEN);
/* 220 */     Holder.Reference reference32 = holderGetter.getOrThrow(VegetationFeatures.WILDFLOWERS_BIRCH_FOREST);
/* 221 */     Holder.Reference reference33 = holderGetter.getOrThrow(VegetationFeatures.WILDFLOWERS_MEADOW);
/* 222 */     Holder.Reference reference34 = holderGetter.getOrThrow(VegetationFeatures.TREES_PLAINS);
/* 223 */     Holder.Reference reference35 = holderGetter.getOrThrow(VegetationFeatures.DARK_FOREST_VEGETATION);
/* 224 */     Holder.Reference reference36 = holderGetter.getOrThrow(VegetationFeatures.PALE_GARDEN_VEGETATION);
/* 225 */     Holder.Reference reference37 = holderGetter.getOrThrow(VegetationFeatures.FOREST_FLOWERS);
/* 226 */     Holder.Reference reference38 = holderGetter.getOrThrow(VegetationFeatures.PALE_FOREST_FLOWERS);
/* 227 */     Holder.Reference reference39 = holderGetter.getOrThrow(VegetationFeatures.PALE_MOSS_PATCH);
/* 228 */     Holder.Reference reference40 = holderGetter.getOrThrow(VegetationFeatures.TREES_FLOWER_FOREST);
/* 229 */     Holder.Reference reference41 = holderGetter.getOrThrow(VegetationFeatures.MEADOW_TREES);
/* 230 */     Holder.Reference reference42 = holderGetter.getOrThrow(VegetationFeatures.TREES_TAIGA);
/* 231 */     Holder.Reference reference43 = holderGetter.getOrThrow(VegetationFeatures.TREES_BADLANDS);
/* 232 */     Holder.Reference reference44 = holderGetter.getOrThrow(VegetationFeatures.TREES_GROVE);
/* 233 */     Holder.Reference reference45 = holderGetter.getOrThrow(VegetationFeatures.TREES_SNOWY);
/* 234 */     Holder.Reference reference46 = holderGetter.getOrThrow(TreeFeatures.CHERRY_BEES_005);
/* 235 */     Holder.Reference reference47 = holderGetter.getOrThrow(TreeFeatures.SWAMP_OAK);
/* 236 */     Holder.Reference reference48 = holderGetter.getOrThrow(VegetationFeatures.TREES_SAVANNA);
/* 237 */     Holder.Reference reference49 = holderGetter.getOrThrow(VegetationFeatures.BIRCH_TALL);
/* 238 */     Holder.Reference reference50 = holderGetter.getOrThrow(VegetationFeatures.TREES_BIRCH);
/* 239 */     Holder.Reference reference51 = holderGetter.getOrThrow(VegetationFeatures.TREES_WINDSWEPT_HILLS);
/* 240 */     Holder.Reference reference52 = holderGetter.getOrThrow(VegetationFeatures.TREES_WATER);
/* 241 */     Holder.Reference reference53 = holderGetter.getOrThrow(VegetationFeatures.TREES_BIRCH_AND_OAK_LEAF_LITTER);
/* 242 */     Holder.Reference reference54 = holderGetter.getOrThrow(VegetationFeatures.TREES_SPARSE_JUNGLE);
/* 243 */     Holder.Reference reference55 = holderGetter.getOrThrow(VegetationFeatures.TREES_OLD_GROWTH_SPRUCE_TAIGA);
/* 244 */     Holder.Reference reference56 = holderGetter.getOrThrow(VegetationFeatures.TREES_OLD_GROWTH_PINE_TAIGA);
/* 245 */     Holder.Reference reference57 = holderGetter.getOrThrow(VegetationFeatures.TREES_JUNGLE);
/* 246 */     Holder.Reference reference58 = holderGetter.getOrThrow(VegetationFeatures.BAMBOO_VEGETATION);
/* 247 */     Holder.Reference reference59 = holderGetter.getOrThrow(VegetationFeatures.MUSHROOM_ISLAND_VEGETATION);
/* 248 */     Holder.Reference reference60 = holderGetter.getOrThrow(VegetationFeatures.MANGROVE_VEGETATION);
/*     */     
/* 250 */     PlacementUtils.register(paramBootstrapContext, BAMBOO_LIGHT, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] {
/* 251 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(4), 
/* 252 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 254 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 256 */     PlacementUtils.register(paramBootstrapContext, BAMBOO, (Holder<ConfiguredFeature<?, ?>>)reference2, new PlacementModifier[] {
/* 257 */           (PlacementModifier)NoiseBasedCountPlacement.of(160, 80.0D, 0.3D), 
/* 258 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 260 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 262 */     PlacementUtils.register(paramBootstrapContext, VINES, (Holder<ConfiguredFeature<?, ?>>)reference3, new PlacementModifier[] {
/* 263 */           (PlacementModifier)CountPlacement.of(127), 
/* 264 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 265 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(100)), 
/* 266 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 269 */     PlacementUtils.register(paramBootstrapContext, PATCH_SUNFLOWER, (Holder<ConfiguredFeature<?, ?>>)reference4, new PlacementModifier[] {
/* 270 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(3), 
/* 271 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 273 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 275 */     PlacementUtils.register(paramBootstrapContext, PATCH_PUMPKIN, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[] {
/* 276 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(300), 
/* 277 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 279 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 282 */     PlacementUtils.register(paramBootstrapContext, PATCH_GRASS_PLAIN, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] {
/* 283 */           (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 284 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 286 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 288 */     PlacementUtils.register(paramBootstrapContext, PATCH_GRASS_MEADOW, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] {
/* 289 */           (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 290 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 292 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 294 */     PlacementUtils.register(paramBootstrapContext, PATCH_GRASS_FOREST, (Holder<ConfiguredFeature<?, ?>>)reference6, 
/* 295 */         worldSurfaceSquaredWithCount(2));
/*     */     
/* 297 */     PlacementUtils.register(paramBootstrapContext, PATCH_LEAF_LITTER, (Holder<ConfiguredFeature<?, ?>>)reference8, 
/* 298 */         worldSurfaceSquaredWithCount(2));
/*     */     
/* 300 */     PlacementUtils.register(paramBootstrapContext, PATCH_GRASS_BADLANDS, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] {
/* 301 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 303 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 305 */     PlacementUtils.register(paramBootstrapContext, PATCH_GRASS_SAVANNA, (Holder<ConfiguredFeature<?, ?>>)reference6, 
/* 306 */         worldSurfaceSquaredWithCount(20));
/*     */     
/* 308 */     PlacementUtils.register(paramBootstrapContext, PATCH_GRASS_NORMAL, (Holder<ConfiguredFeature<?, ?>>)reference6, 
/* 309 */         worldSurfaceSquaredWithCount(5));
/*     */     
/* 311 */     PlacementUtils.register(paramBootstrapContext, PATCH_GRASS_TAIGA_2, (Holder<ConfiguredFeature<?, ?>>)reference9, new PlacementModifier[] {
/* 312 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 314 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 316 */     PlacementUtils.register(paramBootstrapContext, PATCH_GRASS_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference9, 
/* 317 */         worldSurfaceSquaredWithCount(7));
/*     */     
/* 319 */     PlacementUtils.register(paramBootstrapContext, PATCH_GRASS_JUNGLE, (Holder<ConfiguredFeature<?, ?>>)reference10, 
/* 320 */         worldSurfaceSquaredWithCount(25));
/*     */ 
/*     */     
/* 323 */     PlacementUtils.register(paramBootstrapContext, GRASS_BONEMEAL, (Holder<ConfiguredFeature<?, ?>>)reference11, new PlacementModifier[] { (PlacementModifier)PlacementUtils.isEmpty() });
/*     */     
/* 325 */     PlacementUtils.register(paramBootstrapContext, PATCH_DEAD_BUSH_2, (Holder<ConfiguredFeature<?, ?>>)reference12, 
/* 326 */         worldSurfaceSquaredWithCount(2));
/*     */     
/* 328 */     PlacementUtils.register(paramBootstrapContext, PATCH_DEAD_BUSH, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] {
/* 329 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 331 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 333 */     PlacementUtils.register(paramBootstrapContext, PATCH_DEAD_BUSH_BADLANDS, (Holder<ConfiguredFeature<?, ?>>)reference12, 
/* 334 */         worldSurfaceSquaredWithCount(20));
/*     */     
/* 336 */     PlacementUtils.register(paramBootstrapContext, PATCH_DRY_GRASS_BADLANDS, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] {
/* 337 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(6), 
/* 338 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 340 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 342 */     PlacementUtils.register(paramBootstrapContext, PATCH_DRY_GRASS_DESERT, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] {
/* 343 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(3), 
/* 344 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 346 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 348 */     PlacementUtils.register(paramBootstrapContext, PATCH_MELON, (Holder<ConfiguredFeature<?, ?>>)reference15, new PlacementModifier[] {
/* 349 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(6), 
/* 350 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 352 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 355 */     PlacementUtils.register(paramBootstrapContext, PATCH_MELON_SPARSE, (Holder<ConfiguredFeature<?, ?>>)reference15, new PlacementModifier[] {
/* 356 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(64), 
/* 357 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 359 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 362 */     PlacementUtils.register(paramBootstrapContext, PATCH_BERRY_COMMON, (Holder<ConfiguredFeature<?, ?>>)reference16, new PlacementModifier[] {
/* 363 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 364 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 366 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 368 */     PlacementUtils.register(paramBootstrapContext, PATCH_BERRY_RARE, (Holder<ConfiguredFeature<?, ?>>)reference16, new PlacementModifier[] {
/* 369 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(384), 
/* 370 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 372 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 375 */     PlacementUtils.register(paramBootstrapContext, PATCH_WATERLILY, (Holder<ConfiguredFeature<?, ?>>)reference17, 
/* 376 */         worldSurfaceSquaredWithCount(4));
/*     */ 
/*     */     
/* 379 */     PlacementUtils.register(paramBootstrapContext, PATCH_TALL_GRASS_2, (Holder<ConfiguredFeature<?, ?>>)reference18, new PlacementModifier[] {
/* 380 */           (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 0, 7), 
/* 381 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 382 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 384 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 386 */     PlacementUtils.register(paramBootstrapContext, PATCH_TALL_GRASS, (Holder<ConfiguredFeature<?, ?>>)reference18, new PlacementModifier[] {
/* 387 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(5), 
/* 388 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 390 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 392 */     PlacementUtils.register(paramBootstrapContext, PATCH_LARGE_FERN, (Holder<ConfiguredFeature<?, ?>>)reference19, new PlacementModifier[] {
/* 393 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(5), 
/* 394 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 396 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 399 */     PlacementUtils.register(paramBootstrapContext, PATCH_BUSH, (Holder<ConfiguredFeature<?, ?>>)reference20, new PlacementModifier[] {
/* 400 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(4), 
/* 401 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 403 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 406 */     PlacementUtils.register(paramBootstrapContext, PATCH_CACTUS_DESERT, (Holder<ConfiguredFeature<?, ?>>)reference21, new PlacementModifier[] {
/* 407 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(6), 
/* 408 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 410 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 412 */     PlacementUtils.register(paramBootstrapContext, PATCH_CACTUS_DECORATED, (Holder<ConfiguredFeature<?, ?>>)reference21, new PlacementModifier[] {
/* 413 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(13), 
/* 414 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 416 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 419 */     PlacementUtils.register(paramBootstrapContext, PATCH_SUGAR_CANE_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference22, new PlacementModifier[] {
/* 420 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(3), 
/* 421 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 423 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 425 */     PlacementUtils.register(paramBootstrapContext, PATCH_SUGAR_CANE_DESERT, (Holder<ConfiguredFeature<?, ?>>)reference22, new PlacementModifier[] {
/* 426 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 428 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 430 */     PlacementUtils.register(paramBootstrapContext, PATCH_SUGAR_CANE_BADLANDS, (Holder<ConfiguredFeature<?, ?>>)reference22, new PlacementModifier[] {
/* 431 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(5), 
/* 432 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 434 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 436 */     PlacementUtils.register(paramBootstrapContext, PATCH_SUGAR_CANE, (Holder<ConfiguredFeature<?, ?>>)reference22, new PlacementModifier[] {
/* 437 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(6), 
/* 438 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 440 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 442 */     PlacementUtils.register(paramBootstrapContext, PATCH_FIREFLY_BUSH_NEAR_WATER, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] {
/* 443 */           (PlacementModifier)CountPlacement.of(2), 
/* 444 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_NO_LEAVES, 
/*     */           
/* 446 */           (PlacementModifier)BiomeFilter.biome(), 
/* 447 */           (PlacementModifier)VegetationFeatures.nearWaterPredicate(Blocks.FIREFLY_BUSH)
/*     */         });
/* 449 */     PlacementUtils.register(paramBootstrapContext, PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] {
/* 450 */           (PlacementModifier)CountPlacement.of(3), 
/* 451 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 453 */           (PlacementModifier)BiomeFilter.biome(), 
/* 454 */           (PlacementModifier)VegetationFeatures.nearWaterPredicate(Blocks.FIREFLY_BUSH)
/*     */         });
/* 456 */     PlacementUtils.register(paramBootstrapContext, PATCH_FIREFLY_BUSH_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] {
/* 457 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(8), 
/* 458 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 460 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 462 */     PlacementUtils.register(paramBootstrapContext, BROWN_MUSHROOM_NETHER, (Holder<ConfiguredFeature<?, ?>>)reference23, new PlacementModifier[] {
/* 463 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(2), 
/* 464 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 466 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 468 */     PlacementUtils.register(paramBootstrapContext, RED_MUSHROOM_NETHER, (Holder<ConfiguredFeature<?, ?>>)reference24, new PlacementModifier[] {
/* 469 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(2), 
/* 470 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 472 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 474 */     PlacementUtils.register(paramBootstrapContext, BROWN_MUSHROOM_NORMAL, (Holder<ConfiguredFeature<?, ?>>)reference23, 
/* 475 */         getMushroomPlacement(256, null));
/*     */     
/* 477 */     PlacementUtils.register(paramBootstrapContext, RED_MUSHROOM_NORMAL, (Holder<ConfiguredFeature<?, ?>>)reference24, 
/* 478 */         getMushroomPlacement(512, null));
/*     */     
/* 480 */     PlacementUtils.register(paramBootstrapContext, BROWN_MUSHROOM_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference23, 
/* 481 */         getMushroomPlacement(4, null));
/*     */     
/* 483 */     PlacementUtils.register(paramBootstrapContext, RED_MUSHROOM_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference24, 
/* 484 */         getMushroomPlacement(256, null));
/*     */     
/* 486 */     PlacementUtils.register(paramBootstrapContext, BROWN_MUSHROOM_OLD_GROWTH, (Holder<ConfiguredFeature<?, ?>>)reference23, 
/* 487 */         getMushroomPlacement(4, (PlacementModifier)CountPlacement.of(3)));
/*     */     
/* 489 */     PlacementUtils.register(paramBootstrapContext, RED_MUSHROOM_OLD_GROWTH, (Holder<ConfiguredFeature<?, ?>>)reference24, 
/* 490 */         getMushroomPlacement(171, null));
/*     */     
/* 492 */     PlacementUtils.register(paramBootstrapContext, BROWN_MUSHROOM_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference23, 
/* 493 */         getMushroomPlacement(0, (PlacementModifier)CountPlacement.of(2)));
/*     */     
/* 495 */     PlacementUtils.register(paramBootstrapContext, RED_MUSHROOM_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference24, 
/* 496 */         getMushroomPlacement(64, null));
/*     */ 
/*     */     
/* 499 */     PlacementUtils.register(paramBootstrapContext, FLOWER_WARM, (Holder<ConfiguredFeature<?, ?>>)reference25, new PlacementModifier[] {
/* 500 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(16), 
/* 501 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 503 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 505 */     PlacementUtils.register(paramBootstrapContext, FLOWER_DEFAULT, (Holder<ConfiguredFeature<?, ?>>)reference25, new PlacementModifier[] {
/* 506 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 507 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 509 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 511 */     PlacementUtils.register(paramBootstrapContext, FLOWER_FLOWER_FOREST, (Holder<ConfiguredFeature<?, ?>>)reference26, new PlacementModifier[] {
/* 512 */           (PlacementModifier)CountPlacement.of(3), 
/* 513 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(2), 
/* 514 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 516 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 518 */     PlacementUtils.register(paramBootstrapContext, FLOWER_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference27, new PlacementModifier[] {
/* 519 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 520 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 522 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 524 */     PlacementUtils.register(paramBootstrapContext, FLOWER_PLAINS, (Holder<ConfiguredFeature<?, ?>>)reference28, new PlacementModifier[] {
/* 525 */           (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 15, 4), 
/* 526 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 527 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 529 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 531 */     PlacementUtils.register(paramBootstrapContext, FLOWER_CHERRY, (Holder<ConfiguredFeature<?, ?>>)reference30, new PlacementModifier[] {
/* 532 */           (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 533 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 535 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 537 */     PlacementUtils.register(paramBootstrapContext, FLOWER_MEADOW, (Holder<ConfiguredFeature<?, ?>>)reference29, new PlacementModifier[] {
/* 538 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 540 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 542 */     PlacementUtils.register(paramBootstrapContext, FLOWER_PALE_GARDEN, (Holder<ConfiguredFeature<?, ?>>)reference31, new PlacementModifier[] {
/* 543 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 544 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 546 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 548 */     PlacementUtils.register(paramBootstrapContext, WILDFLOWERS_BIRCH_FOREST, (Holder<ConfiguredFeature<?, ?>>)reference32, new PlacementModifier[] {
/* 549 */           (PlacementModifier)CountPlacement.of(3), 
/* 550 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(2), 
/* 551 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 553 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 556 */     PlacementUtils.register(paramBootstrapContext, WILDFLOWERS_MEADOW, (Holder<ConfiguredFeature<?, ?>>)reference33, new PlacementModifier[] {
/* 557 */           (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 558 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 560 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 562 */     SurfaceWaterDepthFilter surfaceWaterDepthFilter = SurfaceWaterDepthFilter.forMaxDepth(0);
/* 563 */     PlacementUtils.register(paramBootstrapContext, TREES_PLAINS, (Holder<ConfiguredFeature<?, ?>>)reference34, new PlacementModifier[] {
/* 564 */           PlacementUtils.countExtra(0, 0.05F, 1), 
/* 565 */           (PlacementModifier)InSquarePlacement.spread(), (PlacementModifier)surfaceWaterDepthFilter, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */ 
/*     */           
/* 568 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), (Vec3i)BlockPos.ZERO)), 
/* 569 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 571 */     PlacementUtils.register(paramBootstrapContext, DARK_FOREST_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference35, new PlacementModifier[] {
/* 572 */           (PlacementModifier)CountPlacement.of(16), 
/* 573 */           (PlacementModifier)InSquarePlacement.spread(), (PlacementModifier)surfaceWaterDepthFilter, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */ 
/*     */           
/* 576 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 579 */     PlacementUtils.register(paramBootstrapContext, PALE_GARDEN_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference36, new PlacementModifier[] {
/* 580 */           (PlacementModifier)CountPlacement.of(16), 
/* 581 */           (PlacementModifier)InSquarePlacement.spread(), (PlacementModifier)surfaceWaterDepthFilter, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */ 
/*     */           
/* 584 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 587 */     PlacementUtils.register(paramBootstrapContext, FLOWER_FOREST_FLOWERS, (Holder<ConfiguredFeature<?, ?>>)reference37, new PlacementModifier[] {
/* 588 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(7), 
/* 589 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 591 */           (PlacementModifier)CountPlacement.of((IntProvider)ClampedInt.of((IntProvider)UniformInt.of(-1, 3), 0, 3)), 
/* 592 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 594 */     PlacementUtils.register(paramBootstrapContext, FOREST_FLOWERS, (Holder<ConfiguredFeature<?, ?>>)reference37, new PlacementModifier[] {
/* 595 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(7), 
/* 596 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 598 */           (PlacementModifier)CountPlacement.of((IntProvider)ClampedInt.of((IntProvider)UniformInt.of(-3, 1), 0, 1)), 
/* 599 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 601 */     PlacementUtils.register(paramBootstrapContext, PALE_GARDEN_FLOWERS, (Holder<ConfiguredFeature<?, ?>>)reference38, new PlacementModifier[] {
/* 602 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(8), 
/* 603 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_NO_LEAVES, 
/*     */           
/* 605 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 608 */     PlacementUtils.register(paramBootstrapContext, PALE_MOSS_PATCH, (Holder<ConfiguredFeature<?, ?>>)reference39, new PlacementModifier[] {
/* 609 */           (PlacementModifier)CountPlacement.of(1), 
/* 610 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_NO_LEAVES, 
/*     */           
/* 612 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 615 */     PlacementUtils.register(paramBootstrapContext, TREES_FLOWER_FOREST, (Holder<ConfiguredFeature<?, ?>>)reference40, 
/* 616 */         treePlacement(PlacementUtils.countExtra(6, 0.1F, 1)));
/*     */     
/* 618 */     PlacementUtils.register(paramBootstrapContext, TREES_MEADOW, (Holder<ConfiguredFeature<?, ?>>)reference41, 
/* 619 */         treePlacement((PlacementModifier)RarityFilter.onAverageOnceEvery(100)));
/*     */     
/* 621 */     PlacementUtils.register(paramBootstrapContext, TREES_CHERRY, (Holder<ConfiguredFeature<?, ?>>)reference46, 
/* 622 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1), Blocks.CHERRY_SAPLING));
/*     */     
/* 624 */     PlacementUtils.register(paramBootstrapContext, TREES_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference42, 
/* 625 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 627 */     PlacementUtils.register(paramBootstrapContext, TREES_GROVE, (Holder<ConfiguredFeature<?, ?>>)reference44, 
/* 628 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 630 */     PlacementUtils.register(paramBootstrapContext, TREES_BADLANDS, (Holder<ConfiguredFeature<?, ?>>)reference43, 
/* 631 */         treePlacement(PlacementUtils.countExtra(5, 0.1F, 1), Blocks.OAK_SAPLING));
/*     */     
/* 633 */     PlacementUtils.register(paramBootstrapContext, TREES_SNOWY, (Holder<ConfiguredFeature<?, ?>>)reference45, 
/* 634 */         treePlacement(PlacementUtils.countExtra(0, 0.1F, 1), Blocks.SPRUCE_SAPLING));
/*     */     
/* 636 */     PlacementUtils.register(paramBootstrapContext, TREES_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference47, new PlacementModifier[] {
/* 637 */           PlacementUtils.countExtra(2, 0.1F, 1), 
/* 638 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 639 */           (PlacementModifier)SurfaceWaterDepthFilter.forMaxDepth(2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */           
/* 641 */           (PlacementModifier)BiomeFilter.biome(), 
/* 642 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), (Vec3i)BlockPos.ZERO))
/*     */         });
/* 644 */     PlacementUtils.register(paramBootstrapContext, TREES_WINDSWEPT_SAVANNA, (Holder<ConfiguredFeature<?, ?>>)reference48, 
/* 645 */         treePlacement(PlacementUtils.countExtra(2, 0.1F, 1)));
/*     */     
/* 647 */     PlacementUtils.register(paramBootstrapContext, TREES_SAVANNA, (Holder<ConfiguredFeature<?, ?>>)reference48, 
/* 648 */         treePlacement(PlacementUtils.countExtra(1, 0.1F, 1)));
/*     */     
/* 650 */     PlacementUtils.register(paramBootstrapContext, BIRCH_TALL, (Holder<ConfiguredFeature<?, ?>>)reference49, 
/* 651 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 653 */     PlacementUtils.register(paramBootstrapContext, TREES_BIRCH, (Holder<ConfiguredFeature<?, ?>>)reference50, 
/* 654 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1), Blocks.BIRCH_SAPLING));
/*     */     
/* 656 */     PlacementUtils.register(paramBootstrapContext, TREES_WINDSWEPT_FOREST, (Holder<ConfiguredFeature<?, ?>>)reference51, 
/* 657 */         treePlacement(PlacementUtils.countExtra(3, 0.1F, 1)));
/*     */     
/* 659 */     PlacementUtils.register(paramBootstrapContext, TREES_WINDSWEPT_HILLS, (Holder<ConfiguredFeature<?, ?>>)reference51, 
/* 660 */         treePlacement(PlacementUtils.countExtra(0, 0.1F, 1)));
/*     */     
/* 662 */     PlacementUtils.register(paramBootstrapContext, TREES_WATER, (Holder<ConfiguredFeature<?, ?>>)reference52, 
/* 663 */         treePlacement(PlacementUtils.countExtra(0, 0.1F, 1)));
/*     */     
/* 665 */     PlacementUtils.register(paramBootstrapContext, TREES_BIRCH_AND_OAK_LEAF_LITTER, (Holder<ConfiguredFeature<?, ?>>)reference53, 
/* 666 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 668 */     PlacementUtils.register(paramBootstrapContext, TREES_SPARSE_JUNGLE, (Holder<ConfiguredFeature<?, ?>>)reference54, 
/* 669 */         treePlacement(PlacementUtils.countExtra(2, 0.1F, 1)));
/*     */     
/* 671 */     PlacementUtils.register(paramBootstrapContext, TREES_OLD_GROWTH_SPRUCE_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference55, 
/* 672 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 674 */     PlacementUtils.register(paramBootstrapContext, TREES_OLD_GROWTH_PINE_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference56, 
/* 675 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 677 */     PlacementUtils.register(paramBootstrapContext, TREES_JUNGLE, (Holder<ConfiguredFeature<?, ?>>)reference57, 
/* 678 */         treePlacement(PlacementUtils.countExtra(50, 0.1F, 1)));
/*     */     
/* 680 */     PlacementUtils.register(paramBootstrapContext, BAMBOO_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference58, 
/* 681 */         treePlacement(PlacementUtils.countExtra(30, 0.1F, 1)));
/*     */ 
/*     */     
/* 684 */     PlacementUtils.register(paramBootstrapContext, MUSHROOM_ISLAND_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference59, new PlacementModifier[] {
/* 685 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 687 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 690 */     PlacementUtils.register(paramBootstrapContext, TREES_MANGROVE, (Holder<ConfiguredFeature<?, ?>>)reference60, new PlacementModifier[] {
/* 691 */           (PlacementModifier)CountPlacement.of(25), 
/* 692 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 693 */           (PlacementModifier)SurfaceWaterDepthFilter.forMaxDepth(5), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */           
/* 695 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\placement\VegetationPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */