/*     */ package net.minecraft.data.worldgen.features;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*     */ import net.minecraft.data.worldgen.placement.TreePlacements;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.InclusiveRange;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.BiasedToBottomInt;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.util.valueproviders.WeightedListInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FlowerBedBlock;
/*     */ import net.minecraft.world.level.block.LeafLitterBlock;
/*     */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.DualNoiseProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class VegetationFeatures {
/*  58 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO_NO_PODZOL = FeatureUtils.createKey("bamboo_no_podzol");
/*  59 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO_SOME_PODZOL = FeatureUtils.createKey("bamboo_some_podzol");
/*  60 */   public static final ResourceKey<ConfiguredFeature<?, ?>> VINES = FeatureUtils.createKey("vines");
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BROWN_MUSHROOM = FeatureUtils.createKey("patch_brown_mushroom");
/*  65 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_RED_MUSHROOM = FeatureUtils.createKey("patch_red_mushroom");
/*  66 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SUNFLOWER = FeatureUtils.createKey("patch_sunflower");
/*  67 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PUMPKIN = FeatureUtils.createKey("patch_pumpkin");
/*     */   
/*  69 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BERRY_BUSH = FeatureUtils.createKey("patch_berry_bush");
/*     */ 
/*     */ 
/*     */   
/*     */   private static RandomPatchConfiguration grassPatch(BlockStateProvider paramBlockStateProvider, int paramInt) {
/*  74 */     return FeatureUtils.simpleRandomPatchConfiguration(paramInt, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration(paramBlockStateProvider)));
/*     */   }
/*     */   
/*  77 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_TAIGA_GRASS = FeatureUtils.createKey("patch_taiga_grass");
/*     */   
/*  79 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GRASS = FeatureUtils.createKey("patch_grass");
/*  80 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GRASS_MEADOW = FeatureUtils.createKey("patch_grass_meadow");
/*  81 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GRASS_JUNGLE = FeatureUtils.createKey("patch_grass_jungle");
/*  82 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_PIECE_OF_GRASS = FeatureUtils.createKey("single_piece_of_grass");
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DEAD_BUSH = FeatureUtils.createKey("patch_dead_bush");
/*  87 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DRY_GRASS = FeatureUtils.createKey("patch_dry_grass");
/*  88 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_MELON = FeatureUtils.createKey("patch_melon");
/*  89 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_WATERLILY = FeatureUtils.createKey("patch_waterlily");
/*  90 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_TALL_GRASS = FeatureUtils.createKey("patch_tall_grass");
/*  91 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_LARGE_FERN = FeatureUtils.createKey("patch_large_fern");
/*  92 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BUSH = FeatureUtils.createKey("patch_bush");
/*  93 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_LEAF_LITTER = FeatureUtils.createKey("patch_leaf_litter");
/*  94 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIREFLY_BUSH = FeatureUtils.createKey("patch_firefly_bush");
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CACTUS = FeatureUtils.createKey("patch_cactus");
/*  99 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SUGAR_CANE = FeatureUtils.createKey("patch_sugar_cane");
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_DEFAULT = FeatureUtils.createKey("flower_default");
/* 104 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_FLOWER_FOREST = FeatureUtils.createKey("flower_flower_forest");
/* 105 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_SWAMP = FeatureUtils.createKey("flower_swamp");
/* 106 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PLAIN = FeatureUtils.createKey("flower_plain");
/* 107 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_MEADOW = FeatureUtils.createKey("flower_meadow");
/* 108 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_CHERRY = FeatureUtils.createKey("flower_cherry");
/* 109 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PALE_GARDEN = FeatureUtils.createKey("flower_pale_garden");
/* 110 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWERS_BIRCH_FOREST = FeatureUtils.createKey("wildflowers_birch_forest");
/* 111 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWERS_MEADOW = FeatureUtils.createKey("wildflowers_meadow");
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_FLOWERS = FeatureUtils.createKey("forest_flowers");
/* 116 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_FOREST_FLOWERS = FeatureUtils.createKey("pale_forest_flowers");
/*     */   
/* 118 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FOREST_VEGETATION = FeatureUtils.createKey("dark_forest_vegetation");
/*     */   
/* 120 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = FeatureUtils.createKey("pale_garden_vegetation");
/* 121 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_VEGETATION = FeatureUtils.createKey("pale_moss_vegetation");
/* 122 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH = FeatureUtils.createKey("pale_moss_patch");
/* 123 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH_BONEMEAL = FeatureUtils.createKey("pale_moss_patch_bonemeal");
/*     */   
/* 125 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_FLOWER_FOREST = FeatureUtils.createKey("trees_flower_forest");
/*     */   
/* 127 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MEADOW_TREES = FeatureUtils.createKey("meadow_trees");
/*     */   
/* 129 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_TAIGA = FeatureUtils.createKey("trees_taiga");
/*     */   
/* 131 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BADLANDS = FeatureUtils.createKey("trees_badlands");
/*     */   
/* 133 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_GROVE = FeatureUtils.createKey("trees_grove");
/*     */   
/* 135 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_SAVANNA = FeatureUtils.createKey("trees_savanna");
/*     */   
/* 137 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_SNOWY = FeatureUtils.createKey("trees_snowy");
/*     */   
/* 139 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BIRCH = FeatureUtils.createKey("trees_birch");
/*     */   
/* 141 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_TALL = FeatureUtils.createKey("birch_tall");
/*     */   
/* 143 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_WINDSWEPT_HILLS = FeatureUtils.createKey("trees_windswept_hills");
/*     */   
/* 145 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_WATER = FeatureUtils.createKey("trees_water");
/*     */   
/* 147 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BIRCH_AND_OAK_LEAF_LITTER = FeatureUtils.createKey("trees_birch_and_oak_leaf_litter");
/*     */   
/* 149 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PLAINS = FeatureUtils.createKey("trees_plains");
/*     */   
/* 151 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_SPARSE_JUNGLE = FeatureUtils.createKey("trees_sparse_jungle");
/*     */   
/* 153 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_OLD_GROWTH_SPRUCE_TAIGA = FeatureUtils.createKey("trees_old_growth_spruce_taiga");
/*     */   
/* 155 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_OLD_GROWTH_PINE_TAIGA = FeatureUtils.createKey("trees_old_growth_pine_taiga");
/*     */   
/* 157 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_JUNGLE = FeatureUtils.createKey("trees_jungle");
/*     */   
/* 159 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO_VEGETATION = FeatureUtils.createKey("bamboo_vegetation");
/*     */   
/* 161 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MUSHROOM_ISLAND_VEGETATION = FeatureUtils.createKey("mushroom_island_vegetation");
/*     */   
/* 163 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MANGROVE_VEGETATION = FeatureUtils.createKey("mangrove_vegetation");
/*     */   
/*     */   private static final float FALLEN_TREE_ONE_IN_CHANCE = 80.0F;
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> paramBootstrapContext) {
/* 168 */     HolderGetter holderGetter1 = paramBootstrapContext.lookup(Registries.CONFIGURED_FEATURE);
/* 169 */     Holder.Reference reference1 = holderGetter1.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM);
/* 170 */     Holder.Reference reference2 = holderGetter1.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM);
/* 171 */     Holder.Reference reference3 = holderGetter1.getOrThrow(TreeFeatures.FANCY_OAK_BEES_005);
/* 172 */     Holder.Reference reference4 = holderGetter1.getOrThrow(TreeFeatures.OAK_BEES_005);
/* 173 */     Holder.Reference reference5 = holderGetter1.getOrThrow(PATCH_GRASS_JUNGLE);
/*     */     
/* 175 */     HolderGetter holderGetter2 = paramBootstrapContext.lookup(Registries.PLACED_FEATURE);
/* 176 */     Holder.Reference reference6 = holderGetter2.getOrThrow(TreePlacements.PALE_OAK_CHECKED);
/* 177 */     Holder.Reference reference7 = holderGetter2.getOrThrow(TreePlacements.PALE_OAK_CREAKING_CHECKED);
/* 178 */     Holder.Reference reference8 = holderGetter2.getOrThrow(TreePlacements.FANCY_OAK_CHECKED);
/* 179 */     Holder.Reference reference9 = holderGetter2.getOrThrow(TreePlacements.BIRCH_BEES_002);
/* 180 */     Holder.Reference reference10 = holderGetter2.getOrThrow(TreePlacements.FANCY_OAK_BEES_002);
/* 181 */     Holder.Reference reference11 = holderGetter2.getOrThrow(TreePlacements.FANCY_OAK_BEES);
/* 182 */     Holder.Reference reference12 = holderGetter2.getOrThrow(TreePlacements.PINE_CHECKED);
/* 183 */     Holder.Reference reference13 = holderGetter2.getOrThrow(TreePlacements.SPRUCE_CHECKED);
/* 184 */     Holder.Reference reference14 = holderGetter2.getOrThrow(TreePlacements.PINE_ON_SNOW);
/* 185 */     Holder.Reference reference15 = holderGetter2.getOrThrow(TreePlacements.ACACIA_CHECKED);
/* 186 */     Holder.Reference reference16 = holderGetter2.getOrThrow(TreePlacements.SUPER_BIRCH_BEES_0002);
/* 187 */     Holder.Reference reference17 = holderGetter2.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED);
/* 188 */     Holder.Reference reference18 = holderGetter2.getOrThrow(TreePlacements.BIRCH_BEES_0002_LEAF_LITTER);
/* 189 */     Holder.Reference reference19 = holderGetter2.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002_LEAF_LITTER);
/* 190 */     Holder.Reference reference20 = holderGetter2.getOrThrow(TreePlacements.JUNGLE_BUSH);
/* 191 */     Holder.Reference reference21 = holderGetter2.getOrThrow(TreePlacements.MEGA_SPRUCE_CHECKED);
/* 192 */     Holder.Reference reference22 = holderGetter2.getOrThrow(TreePlacements.MEGA_PINE_CHECKED);
/* 193 */     Holder.Reference reference23 = holderGetter2.getOrThrow(TreePlacements.MEGA_JUNGLE_TREE_CHECKED);
/* 194 */     Holder.Reference reference24 = holderGetter2.getOrThrow(TreePlacements.TALL_MANGROVE_CHECKED);
/* 195 */     Holder.Reference reference25 = holderGetter2.getOrThrow(TreePlacements.OAK_CHECKED);
/* 196 */     Holder.Reference reference26 = holderGetter2.getOrThrow(TreePlacements.OAK_BEES_002);
/* 197 */     Holder.Reference reference27 = holderGetter2.getOrThrow(TreePlacements.SUPER_BIRCH_BEES);
/* 198 */     Holder.Reference reference28 = holderGetter2.getOrThrow(TreePlacements.SPRUCE_ON_SNOW);
/* 199 */     Holder.Reference reference29 = holderGetter2.getOrThrow(TreePlacements.OAK_BEES_0002_LEAF_LITTER);
/* 200 */     Holder.Reference reference30 = holderGetter2.getOrThrow(TreePlacements.JUNGLE_TREE_CHECKED);
/* 201 */     Holder.Reference reference31 = holderGetter2.getOrThrow(TreePlacements.MANGROVE_CHECKED);
/* 202 */     Holder.Reference reference32 = holderGetter2.getOrThrow(TreePlacements.OAK_LEAF_LITTER);
/* 203 */     Holder.Reference reference33 = holderGetter2.getOrThrow(TreePlacements.DARK_OAK_LEAF_LITTER);
/* 204 */     Holder.Reference reference34 = holderGetter2.getOrThrow(TreePlacements.BIRCH_LEAF_LITTER);
/* 205 */     Holder.Reference reference35 = holderGetter2.getOrThrow(TreePlacements.FANCY_OAK_LEAF_LITTER);
/*     */     
/* 207 */     Holder.Reference reference36 = holderGetter2.getOrThrow(TreePlacements.FALLEN_OAK_TREE);
/* 208 */     Holder.Reference reference37 = holderGetter2.getOrThrow(TreePlacements.FALLEN_BIRCH_TREE);
/* 209 */     Holder.Reference reference38 = holderGetter2.getOrThrow(TreePlacements.FALLEN_SUPER_BIRCH_TREE);
/* 210 */     Holder.Reference reference39 = holderGetter2.getOrThrow(TreePlacements.FALLEN_JUNGLE_TREE);
/* 211 */     Holder.Reference reference40 = holderGetter2.getOrThrow(TreePlacements.FALLEN_SPRUCE_TREE);
/*     */ 
/*     */ 
/*     */     
/* 215 */     FeatureUtils.register(paramBootstrapContext, BAMBOO_NO_PODZOL, Feature.BAMBOO, new ProbabilityFeatureConfiguration(0.0F));
/*     */ 
/*     */     
/* 218 */     FeatureUtils.register(paramBootstrapContext, BAMBOO_SOME_PODZOL, Feature.BAMBOO, new ProbabilityFeatureConfiguration(0.2F));
/*     */ 
/*     */     
/* 221 */     FeatureUtils.register(paramBootstrapContext, VINES, Feature.VINES);
/*     */ 
/*     */ 
/*     */     
/* 225 */     FeatureUtils.register(paramBootstrapContext, PATCH_BROWN_MUSHROOM, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 226 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.BROWN_MUSHROOM))));
/*     */     
/* 228 */     FeatureUtils.register(paramBootstrapContext, PATCH_RED_MUSHROOM, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 229 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.RED_MUSHROOM))));
/*     */     
/* 231 */     FeatureUtils.register(paramBootstrapContext, PATCH_SUNFLOWER, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 232 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.SUNFLOWER))));
/*     */     
/* 234 */     FeatureUtils.register(paramBootstrapContext, PATCH_PUMPKIN, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 235 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.PUMPKIN)), 
/* 236 */           List.of(Blocks.GRASS_BLOCK)));
/*     */ 
/*     */     
/* 239 */     FeatureUtils.register(paramBootstrapContext, PATCH_BERRY_BUSH, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/*     */             
/* 241 */             (BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(3)))), 
/*     */           
/* 243 */           List.of(Blocks.GRASS_BLOCK)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 248 */     FeatureUtils.register(paramBootstrapContext, PATCH_TAIGA_GRASS, Feature.RANDOM_PATCH, 
/* 249 */         grassPatch((BlockStateProvider)new WeightedStateProvider(
/* 250 */             WeightedList.builder().add(Blocks.SHORT_GRASS.defaultBlockState(), 1).add(Blocks.FERN.defaultBlockState(), 4)), 32));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     FeatureUtils.register(paramBootstrapContext, PATCH_GRASS, Feature.RANDOM_PATCH, 
/* 256 */         grassPatch(
/* 257 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SHORT_GRASS), 32));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     FeatureUtils.register(paramBootstrapContext, PATCH_GRASS_MEADOW, Feature.RANDOM_PATCH, 
/* 263 */         grassPatch(
/* 264 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SHORT_GRASS), 16));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 269 */     FeatureUtils.register(paramBootstrapContext, PATCH_LEAF_LITTER, Feature.RANDOM_PATCH, 
/* 270 */         FeatureUtils.simpleRandomPatchConfiguration(32, 
/* 271 */           PlacementUtils.filtered(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/*     */                 
/* 273 */                 leafLitterPatchBuilder(1, 3))), 
/* 274 */             BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */               
/* 276 */               BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.GRASS_BLOCK })))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 282 */     FeatureUtils.register(paramBootstrapContext, PATCH_GRASS_JUNGLE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(32, 7, 3, 
/*     */ 
/*     */ 
/*     */           
/* 286 */           PlacementUtils.filtered(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/*     */ 
/*     */                 
/* 289 */                 WeightedList.builder().add(Blocks.SHORT_GRASS.defaultBlockState(), 3).add(Blocks.FERN.defaultBlockState(), 1))), 
/*     */             
/* 291 */             BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */               
/* 293 */               BlockPredicate.not(
/* 294 */                 BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.PODZOL }))))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 299 */     FeatureUtils.register(paramBootstrapContext, SINGLE_PIECE_OF_GRASS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 300 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SHORT_GRASS.defaultBlockState())));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 305 */     FeatureUtils.register(paramBootstrapContext, PATCH_DEAD_BUSH, Feature.RANDOM_PATCH, 
/* 306 */         grassPatch(
/* 307 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.DEAD_BUSH), 4));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     FeatureUtils.register(paramBootstrapContext, PATCH_DRY_GRASS, Feature.RANDOM_PATCH, 
/* 313 */         grassPatch((BlockStateProvider)new WeightedStateProvider(
/* 314 */             WeightedList.builder()
/* 315 */             .add(Blocks.SHORT_DRY_GRASS.defaultBlockState(), 1)
/* 316 */             .add(Blocks.TALL_DRY_GRASS.defaultBlockState(), 1)), 64));
/*     */ 
/*     */ 
/*     */     
/* 320 */     FeatureUtils.register(paramBootstrapContext, PATCH_MELON, Feature.RANDOM_PATCH, new RandomPatchConfiguration(64, 7, 3, 
/*     */ 
/*     */ 
/*     */           
/* 324 */           PlacementUtils.filtered(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration(
/*     */ 
/*     */               
/* 327 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.MELON)), 
/*     */             
/* 329 */             BlockPredicate.allOf(new BlockPredicate[] {
/* 330 */                 BlockPredicate.replaceable(), 
/* 331 */                 BlockPredicate.noFluid(), 
/* 332 */                 BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.GRASS_BLOCK })
/*     */               }))));
/*     */ 
/*     */     
/* 336 */     FeatureUtils.register(paramBootstrapContext, PATCH_WATERLILY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(10, 7, 3, 
/*     */ 
/*     */ 
/*     */           
/* 340 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.LILY_PAD)))));
/*     */     
/* 342 */     FeatureUtils.register(paramBootstrapContext, PATCH_TALL_GRASS, Feature.RANDOM_PATCH, 
/* 343 */         FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 344 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.TALL_GRASS))));
/*     */ 
/*     */     
/* 347 */     FeatureUtils.register(paramBootstrapContext, PATCH_LARGE_FERN, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/*     */             
/* 349 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.LARGE_FERN))));
/*     */ 
/*     */     
/* 352 */     FeatureUtils.register(paramBootstrapContext, PATCH_BUSH, Feature.RANDOM_PATCH, new RandomPatchConfiguration(24, 5, 3, 
/*     */ 
/*     */ 
/*     */           
/* 356 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.BUSH)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 361 */     FeatureUtils.register(paramBootstrapContext, PATCH_CACTUS, Feature.RANDOM_PATCH, 
/* 362 */         FeatureUtils.simpleRandomPatchConfiguration(10, 
/*     */           
/* 364 */           PlacementUtils.inlinePlaced(Feature.BLOCK_COLUMN, (FeatureConfiguration)new BlockColumnConfiguration(
/*     */ 
/*     */               
/* 367 */               List.of(
/* 368 */                 BlockColumnConfiguration.layer(
/* 369 */                   (IntProvider)BiasedToBottomInt.of(1, 3), 
/* 370 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.CACTUS)), 
/*     */                 
/* 372 */                 BlockColumnConfiguration.layer((IntProvider)new WeightedListInt(
/* 373 */                     WeightedList.builder()
/* 374 */                     .add(ConstantInt.of(0), 3)
/* 375 */                     .add(ConstantInt.of(1), 1)
/* 376 */                     .build()), 
/* 377 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.CACTUS_FLOWER))), Direction.UP, BlockPredicate.ONLY_IN_AIR_PREDICATE, false), new PlacementModifier[] {
/*     */ 
/*     */ 
/*     */               
/* 381 */               (PlacementModifier)BlockPredicateFilter.forPredicate(
/* 382 */                 BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */                   
/* 384 */                   BlockPredicate.wouldSurvive(Blocks.CACTUS.defaultBlockState(), (Vec3i)BlockPos.ZERO)))
/*     */             })));
/*     */ 
/*     */ 
/*     */     
/* 389 */     FeatureUtils.register(paramBootstrapContext, PATCH_SUGAR_CANE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(20, 4, 0, 
/*     */ 
/*     */ 
/*     */           
/* 393 */           PlacementUtils.inlinePlaced(Feature.BLOCK_COLUMN, 
/*     */             
/* 395 */             (FeatureConfiguration)BlockColumnConfiguration.simple(
/* 396 */               (IntProvider)BiasedToBottomInt.of(2, 4), 
/* 397 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.SUGAR_CANE)), new PlacementModifier[] {
/* 398 */               (PlacementModifier)nearWaterPredicate(Blocks.SUGAR_CANE)
/*     */             })));
/* 400 */     FeatureUtils.register(paramBootstrapContext, PATCH_FIREFLY_BUSH, Feature.RANDOM_PATCH, new RandomPatchConfiguration(20, 4, 3, 
/*     */ 
/*     */ 
/*     */           
/* 404 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.FIREFLY_BUSH)))));
/*     */ 
/*     */ 
/*     */     
/* 408 */     FeatureUtils.register(paramBootstrapContext, FLOWER_DEFAULT, Feature.FLOWER, 
/* 409 */         grassPatch((BlockStateProvider)new WeightedStateProvider(
/* 410 */             WeightedList.builder()
/* 411 */             .add(Blocks.POPPY.defaultBlockState(), 2)
/* 412 */             .add(Blocks.DANDELION.defaultBlockState(), 1)), 64));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 417 */     FeatureUtils.register(paramBootstrapContext, FLOWER_FLOWER_FOREST, Feature.FLOWER, new RandomPatchConfiguration(96, 6, 2, 
/*     */ 
/*     */ 
/*     */           
/* 421 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)new NoiseProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D, new double[0]), 0.020833334F, 
/*     */ 
/*     */                 
/* 424 */                 List.of(new BlockState[] { 
/* 425 */                     Blocks.DANDELION.defaultBlockState(), Blocks.POPPY
/* 426 */                     .defaultBlockState(), Blocks.ALLIUM
/* 427 */                     .defaultBlockState(), Blocks.AZURE_BLUET
/* 428 */                     .defaultBlockState(), Blocks.RED_TULIP
/* 429 */                     .defaultBlockState(), Blocks.ORANGE_TULIP
/* 430 */                     .defaultBlockState(), Blocks.WHITE_TULIP
/* 431 */                     .defaultBlockState(), Blocks.PINK_TULIP
/* 432 */                     .defaultBlockState(), Blocks.OXEYE_DAISY
/* 433 */                     .defaultBlockState(), Blocks.CORNFLOWER
/* 434 */                     .defaultBlockState(), Blocks.LILY_OF_THE_VALLEY
/* 435 */                     .defaultBlockState() }))))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 440 */     FeatureUtils.register(paramBootstrapContext, FLOWER_SWAMP, Feature.FLOWER, new RandomPatchConfiguration(64, 6, 2, 
/*     */ 
/*     */ 
/*     */           
/* 444 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.BLUE_ORCHID)))));
/*     */     
/* 446 */     FeatureUtils.register(paramBootstrapContext, FLOWER_PLAIN, Feature.FLOWER, new RandomPatchConfiguration(64, 6, 2, 
/*     */ 
/*     */ 
/*     */           
/* 450 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D, new double[0]), 0.005F, -0.8F, 0.33333334F, Blocks.DANDELION
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 457 */                 .defaultBlockState(), 
/* 458 */                 List.of(Blocks.ORANGE_TULIP
/* 459 */                   .defaultBlockState(), Blocks.RED_TULIP
/* 460 */                   .defaultBlockState(), Blocks.PINK_TULIP
/* 461 */                   .defaultBlockState(), Blocks.WHITE_TULIP
/* 462 */                   .defaultBlockState()), 
/*     */                 
/* 464 */                 List.of(Blocks.POPPY
/* 465 */                   .defaultBlockState(), Blocks.AZURE_BLUET
/* 466 */                   .defaultBlockState(), Blocks.OXEYE_DAISY
/* 467 */                   .defaultBlockState(), Blocks.CORNFLOWER
/* 468 */                   .defaultBlockState()))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 475 */     FeatureUtils.register(paramBootstrapContext, FLOWER_MEADOW, Feature.FLOWER, new RandomPatchConfiguration(96, 6, 2, 
/*     */ 
/*     */ 
/*     */           
/* 479 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)new DualNoiseProvider(new InclusiveRange(
/*     */ 
/*     */                   
/* 482 */                   Integer.valueOf(1), Integer.valueOf(3)), new NormalNoise.NoiseParameters(-10, 1.0D, new double[0]), 1.0F, 2345L, new NormalNoise.NoiseParameters(-3, 1.0D, new double[0]), 1.0F, List.of(Blocks.TALL_GRASS
/* 483 */                   .defaultBlockState(), Blocks.ALLIUM
/* 484 */                   .defaultBlockState(), Blocks.POPPY
/* 485 */                   .defaultBlockState(), Blocks.AZURE_BLUET
/* 486 */                   .defaultBlockState(), Blocks.DANDELION
/* 487 */                   .defaultBlockState(), Blocks.CORNFLOWER
/* 488 */                   .defaultBlockState(), Blocks.OXEYE_DAISY
/* 489 */                   .defaultBlockState(), Blocks.SHORT_GRASS
/* 490 */                   .defaultBlockState()))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 496 */     FeatureUtils.register(paramBootstrapContext, FLOWER_CHERRY, Feature.FLOWER, new RandomPatchConfiguration(96, 6, 2, 
/*     */ 
/*     */ 
/*     */           
/* 500 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/*     */                 
/* 502 */                 flowerBedPatchBuilder(Blocks.PINK_PETALS))))));
/*     */ 
/*     */ 
/*     */     
/* 506 */     FeatureUtils.register(paramBootstrapContext, WILDFLOWERS_BIRCH_FOREST, Feature.FLOWER, new RandomPatchConfiguration(64, 6, 2, 
/*     */ 
/*     */ 
/*     */           
/* 510 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/*     */                 
/* 512 */                 flowerBedPatchBuilder(Blocks.WILDFLOWERS))))));
/*     */ 
/*     */ 
/*     */     
/* 516 */     FeatureUtils.register(paramBootstrapContext, WILDFLOWERS_MEADOW, Feature.FLOWER, new RandomPatchConfiguration(8, 6, 2, 
/*     */ 
/*     */ 
/*     */           
/* 520 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/*     */                 
/* 522 */                 flowerBedPatchBuilder(Blocks.WILDFLOWERS))))));
/*     */ 
/*     */ 
/*     */     
/* 526 */     FeatureUtils.register(paramBootstrapContext, FLOWER_PALE_GARDEN, Feature.FLOWER, new RandomPatchConfiguration(1, 0, 0, 
/* 527 */           PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration(
/* 528 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.CLOSED_EYEBLOSSOM), true))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 534 */     FeatureUtils.register(paramBootstrapContext, FOREST_FLOWERS, Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(
/* 535 */           (HolderSet)HolderSet.direct(new Holder[] {
/* 536 */               PlacementUtils.inlinePlaced(Feature.RANDOM_PATCH, 
/*     */                 
/* 538 */                 (FeatureConfiguration)FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 539 */                     (BlockStateProvider)BlockStateProvider.simple(Blocks.LILAC))), new PlacementModifier[0]), 
/*     */ 
/*     */               
/* 542 */               PlacementUtils.inlinePlaced(Feature.RANDOM_PATCH, 
/*     */                 
/* 544 */                 (FeatureConfiguration)FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 545 */                     (BlockStateProvider)BlockStateProvider.simple(Blocks.ROSE_BUSH))), new PlacementModifier[0]), 
/*     */ 
/*     */               
/* 548 */               PlacementUtils.inlinePlaced(Feature.RANDOM_PATCH, 
/*     */                 
/* 550 */                 (FeatureConfiguration)FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 551 */                     (BlockStateProvider)BlockStateProvider.simple(Blocks.PEONY))), new PlacementModifier[0]), 
/*     */ 
/*     */               
/* 554 */               PlacementUtils.inlinePlaced(Feature.NO_BONEMEAL_FLOWER, 
/*     */                 
/* 556 */                 (FeatureConfiguration)FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 557 */                     (BlockStateProvider)BlockStateProvider.simple(Blocks.LILY_OF_THE_VALLEY))), new PlacementModifier[0])
/*     */             })));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 563 */     FeatureUtils.register(paramBootstrapContext, PALE_FOREST_FLOWERS, Feature.RANDOM_PATCH, 
/* 564 */         FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.CLOSED_EYEBLOSSOM), true)));
/*     */ 
/*     */     
/* 567 */     FeatureUtils.register(paramBootstrapContext, DARK_FOREST_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 568 */           List.of(new WeightedPlacedFeature(
/* 569 */               PlacementUtils.inlinePlaced((Holder)reference1, new PlacementModifier[0]), 0.025F), new WeightedPlacedFeature(
/* 570 */               PlacementUtils.inlinePlaced((Holder)reference2, new PlacementModifier[0]), 0.05F), new WeightedPlacedFeature((Holder)reference33, 0.6666667F), new WeightedPlacedFeature((Holder)reference37, 0.0025F), new WeightedPlacedFeature((Holder)reference34, 0.2F), new WeightedPlacedFeature((Holder)reference36, 0.0125F), new WeightedPlacedFeature((Holder)reference35, 0.1F)), (Holder)reference32));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 580 */     FeatureUtils.register(paramBootstrapContext, PALE_GARDEN_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 581 */           List.of(new WeightedPlacedFeature((Holder)reference7, 0.1F), new WeightedPlacedFeature((Holder)reference6, 0.9F)), (Holder)reference6));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 588 */     FeatureUtils.register(paramBootstrapContext, PALE_MOSS_VEGETATION, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/* 589 */             WeightedList.builder()
/* 590 */             .add(Blocks.PALE_MOSS_CARPET.defaultBlockState(), 25)
/* 591 */             .add(Blocks.SHORT_GRASS.defaultBlockState(), 25)
/* 592 */             .add(Blocks.TALL_GRASS.defaultBlockState(), 10))));
/*     */ 
/*     */ 
/*     */     
/* 596 */     FeatureUtils.register(paramBootstrapContext, PALE_MOSS_PATCH, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.MOSS_REPLACEABLE, 
/*     */           
/* 598 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_MOSS_BLOCK), 
/* 599 */           PlacementUtils.inlinePlaced((Holder)holderGetter1.getOrThrow(PALE_MOSS_VEGETATION), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 601 */           (IntProvider)ConstantInt.of(1), 0.0F, 5, 0.3F, 
/*     */ 
/*     */ 
/*     */           
/* 605 */           (IntProvider)UniformInt.of(2, 4), 0.75F));
/*     */ 
/*     */ 
/*     */     
/* 609 */     FeatureUtils.register(paramBootstrapContext, PALE_MOSS_PATCH_BONEMEAL, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.MOSS_REPLACEABLE, 
/*     */           
/* 611 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_MOSS_BLOCK), 
/* 612 */           PlacementUtils.inlinePlaced((Holder)holderGetter1.getOrThrow(PALE_MOSS_VEGETATION), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 614 */           (IntProvider)ConstantInt.of(1), 0.0F, 5, 0.6F, 
/*     */ 
/*     */ 
/*     */           
/* 618 */           (IntProvider)UniformInt.of(1, 2), 0.75F));
/*     */ 
/*     */ 
/*     */     
/* 622 */     FeatureUtils.register(paramBootstrapContext, TREES_FLOWER_FOREST, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 623 */           List.of(new WeightedPlacedFeature((Holder)reference37, 0.0025F), new WeightedPlacedFeature((Holder)reference9, 0.2F), new WeightedPlacedFeature((Holder)reference10, 0.1F)), (Holder)reference26));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 631 */     FeatureUtils.register(paramBootstrapContext, MEADOW_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 632 */           List.of(new WeightedPlacedFeature((Holder)reference11, 0.5F)), (Holder)reference27));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 638 */     FeatureUtils.register(paramBootstrapContext, TREES_TAIGA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 639 */           List.of(new WeightedPlacedFeature((Holder)reference12, 0.33333334F), new WeightedPlacedFeature((Holder)reference40, 0.0125F)), (Holder)reference13));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 646 */     FeatureUtils.register(paramBootstrapContext, TREES_BADLANDS, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 647 */           List.of(new WeightedPlacedFeature((Holder)reference36, 0.0125F)), (Holder)reference32));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 653 */     FeatureUtils.register(paramBootstrapContext, TREES_GROVE, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 654 */           List.of(new WeightedPlacedFeature((Holder)reference14, 0.33333334F)), (Holder)reference28));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 660 */     FeatureUtils.register(paramBootstrapContext, TREES_SAVANNA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 661 */           List.of(new WeightedPlacedFeature((Holder)reference15, 0.8F), new WeightedPlacedFeature((Holder)reference36, 0.0125F)), (Holder)reference25));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 666 */     FeatureUtils.register(paramBootstrapContext, TREES_SNOWY, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 667 */           List.of(new WeightedPlacedFeature((Holder)reference40, 0.0125F)), (Holder)reference13));
/*     */ 
/*     */ 
/*     */     
/* 671 */     FeatureUtils.register(paramBootstrapContext, TREES_BIRCH, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 672 */           List.of(new WeightedPlacedFeature((Holder)reference37, 0.0125F)), (Holder)reference17));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 677 */     FeatureUtils.register(paramBootstrapContext, BIRCH_TALL, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 678 */           List.of(new WeightedPlacedFeature((Holder)reference38, 0.00625F), new WeightedPlacedFeature((Holder)reference16, 0.5F), new WeightedPlacedFeature((Holder)reference37, 0.0125F)), (Holder)reference17));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 685 */     FeatureUtils.register(paramBootstrapContext, TREES_WINDSWEPT_HILLS, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 686 */           List.of(new WeightedPlacedFeature((Holder)reference40, 0.008325F), new WeightedPlacedFeature((Holder)reference13, 0.666F), new WeightedPlacedFeature((Holder)reference8, 0.1F), new WeightedPlacedFeature((Holder)reference36, 0.0125F)), (Holder)reference25));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 695 */     FeatureUtils.register(paramBootstrapContext, TREES_WATER, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 696 */           List.of(new WeightedPlacedFeature((Holder)reference8, 0.1F)), (Holder)reference25));
/*     */ 
/*     */ 
/*     */     
/* 700 */     FeatureUtils.register(paramBootstrapContext, TREES_BIRCH_AND_OAK_LEAF_LITTER, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 701 */           List.of(new WeightedPlacedFeature((Holder)reference37, 0.0025F), new WeightedPlacedFeature((Holder)reference18, 0.2F), new WeightedPlacedFeature((Holder)reference19, 0.1F), new WeightedPlacedFeature((Holder)reference36, 0.0125F)), (Holder)reference29));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 710 */     FeatureUtils.register(paramBootstrapContext, TREES_PLAINS, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 711 */           List.of(new WeightedPlacedFeature(
/* 712 */               PlacementUtils.inlinePlaced((Holder)reference3, new PlacementModifier[0]), 0.33333334F), new WeightedPlacedFeature((Holder)reference36, 0.0125F)), 
/*     */           
/* 714 */           PlacementUtils.inlinePlaced((Holder)reference4, new PlacementModifier[0])));
/*     */ 
/*     */     
/* 717 */     FeatureUtils.register(paramBootstrapContext, TREES_SPARSE_JUNGLE, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 718 */           List.of(new WeightedPlacedFeature((Holder)reference8, 0.1F), new WeightedPlacedFeature((Holder)reference20, 0.5F), new WeightedPlacedFeature((Holder)reference39, 0.0125F)), (Holder)reference30));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 726 */     FeatureUtils.register(paramBootstrapContext, TREES_OLD_GROWTH_SPRUCE_TAIGA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 727 */           List.of(new WeightedPlacedFeature((Holder)reference21, 0.33333334F), new WeightedPlacedFeature((Holder)reference12, 0.33333334F), new WeightedPlacedFeature((Holder)reference40, 0.0125F)), (Holder)reference13));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 735 */     FeatureUtils.register(paramBootstrapContext, TREES_OLD_GROWTH_PINE_TAIGA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 736 */           List.of(new WeightedPlacedFeature((Holder)reference21, 0.025641026F), new WeightedPlacedFeature((Holder)reference22, 0.30769232F), new WeightedPlacedFeature((Holder)reference12, 0.33333334F), new WeightedPlacedFeature((Holder)reference40, 0.0125F)), (Holder)reference13));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 745 */     FeatureUtils.register(paramBootstrapContext, TREES_JUNGLE, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 746 */           List.of(new WeightedPlacedFeature((Holder)reference8, 0.1F), new WeightedPlacedFeature((Holder)reference20, 0.5F), new WeightedPlacedFeature((Holder)reference23, 0.33333334F), new WeightedPlacedFeature((Holder)reference39, 0.0125F)), (Holder)reference30));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 755 */     FeatureUtils.register(paramBootstrapContext, BAMBOO_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 756 */           List.of(new WeightedPlacedFeature((Holder)reference8, 0.05F), new WeightedPlacedFeature((Holder)reference20, 0.15F), new WeightedPlacedFeature((Holder)reference23, 0.7F)), 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 761 */           PlacementUtils.inlinePlaced((Holder)reference5, new PlacementModifier[0])));
/*     */ 
/*     */     
/* 764 */     FeatureUtils.register(paramBootstrapContext, MUSHROOM_ISLAND_VEGETATION, Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(
/* 765 */           PlacementUtils.inlinePlaced((Holder)reference2, new PlacementModifier[0]), 
/* 766 */           PlacementUtils.inlinePlaced((Holder)reference1, new PlacementModifier[0])));
/*     */ 
/*     */     
/* 769 */     FeatureUtils.register(paramBootstrapContext, MANGROVE_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 770 */           List.of(new WeightedPlacedFeature((Holder)reference24, 0.85F)), (Holder)reference31));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static WeightedList.Builder<BlockState> flowerBedPatchBuilder(Block paramBlock) {
/* 776 */     return segmentedBlockPatchBuilder(paramBlock, 1, 4, FlowerBedBlock.AMOUNT, FlowerBedBlock.FACING);
/*     */   }
/*     */   
/*     */   public static WeightedList.Builder<BlockState> leafLitterPatchBuilder(int paramInt1, int paramInt2) {
/* 780 */     return segmentedBlockPatchBuilder(Blocks.LEAF_LITTER, paramInt1, paramInt2, LeafLitterBlock.AMOUNT, LeafLitterBlock.FACING);
/*     */   }
/*     */   
/*     */   private static WeightedList.Builder<BlockState> segmentedBlockPatchBuilder(Block paramBlock, int paramInt1, int paramInt2, IntegerProperty paramIntegerProperty, EnumProperty<Direction> paramEnumProperty) {
/* 784 */     WeightedList.Builder<BlockState> builder = WeightedList.builder();
/* 785 */     for (int i = paramInt1; i <= paramInt2; i++) {
/* 786 */       for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 787 */         builder.add(((BlockState)paramBlock.defaultBlockState().setValue((Property)paramIntegerProperty, Integer.valueOf(i))).setValue((Property)paramEnumProperty, (Comparable)direction), 1);
/*     */       }
/*     */     } 
/* 790 */     return builder;
/*     */   }
/*     */   
/*     */   public static BlockPredicateFilter nearWaterPredicate(Block paramBlock) {
/* 794 */     return BlockPredicateFilter.forPredicate(
/* 795 */         BlockPredicate.allOf(new BlockPredicate[] {
/*     */             
/* 797 */             BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.wouldSurvive(paramBlock.defaultBlockState(), (Vec3i)BlockPos.ZERO), 
/* 798 */             BlockPredicate.anyOf(new BlockPredicate[] {
/* 799 */                 BlockPredicate.matchesFluids((Vec3i)new BlockPos(1, -1, 0), new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER
/* 800 */                   }), BlockPredicate.matchesFluids((Vec3i)new BlockPos(-1, -1, 0), new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER
/* 801 */                   }), BlockPredicate.matchesFluids((Vec3i)new BlockPos(0, -1, 1), new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER
/* 802 */                   }), BlockPredicate.matchesFluids((Vec3i)new BlockPos(0, -1, -1), new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER })
/*     */               })
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\features\VegetationFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */