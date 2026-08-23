/*     */ package net.minecraft.data.worldgen.features;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.util.valueproviders.WeightedListInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.HugeMushroomBlock;
/*     */ import net.minecraft.world.level.block.MangrovePropaguleBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.BushFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaPineFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.AboveRootPlacement;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.MangroveRootPlacement;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.MangroveRootPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLogsDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.CreakingHeartDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.PaleMossDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.PlaceOnGroundDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.UpwardsBranchingTrunkPlacer;
/*     */ 
/*     */ public class TreeFeatures {
/*  74 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_FUNGUS = FeatureUtils.createKey("crimson_fungus");
/*  75 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_FUNGUS_PLANTED = FeatureUtils.createKey("crimson_fungus_planted");
/*  76 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WARPED_FUNGUS = FeatureUtils.createKey("warped_fungus");
/*  77 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WARPED_FUNGUS_PLANTED = FeatureUtils.createKey("warped_fungus_planted");
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_BROWN_MUSHROOM = FeatureUtils.createKey("huge_brown_mushroom");
/*  82 */   public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_RED_MUSHROOM = FeatureUtils.createKey("huge_red_mushroom");
/*     */ 
/*     */ 
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(Block paramBlock1, Block paramBlock2, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  87 */     return new TreeConfiguration.TreeConfigurationBuilder(
/*  88 */         (BlockStateProvider)BlockStateProvider.simple(paramBlock1), (TrunkPlacer)new StraightTrunkPlacer(paramInt1, paramInt2, paramInt3), 
/*     */         
/*  90 */         (BlockStateProvider)BlockStateProvider.simple(paramBlock2), (FoliagePlacer)new BlobFoliagePlacer(
/*  91 */           (IntProvider)ConstantInt.of(paramInt4), (IntProvider)ConstantInt.of(0), 3), (FeatureSize)new TwoLayersFeatureSize(1, 0, 1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createOak() {
/*  97 */     return createStraightBlobTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 4, 2, 0, 2).ignoreVines();
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createDarkOak() {
/* 101 */     return new TreeConfiguration.TreeConfigurationBuilder(
/* 102 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.DARK_OAK_LOG), (TrunkPlacer)new DarkOakTrunkPlacer(6, 2, 1), 
/*     */         
/* 104 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES), (FoliagePlacer)new DarkOakFoliagePlacer(
/* 105 */           (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0)), (FeatureSize)new ThreeLayersFeatureSize(1, 1, 0, 1, 2, 
/* 106 */           OptionalInt.empty()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenOak() {
/* 111 */     return createFallenTrees(Blocks.OAK_LOG, 4, 7)
/* 112 */       .stumpDecorators((List)ImmutableList.of(TrunkVineDecorator.INSTANCE));
/*     */   }
/*     */ 
/*     */   
/*     */   private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenBirch(int paramInt) {
/* 117 */     return createFallenTrees(Blocks.BIRCH_LOG, 5, paramInt);
/*     */   }
/*     */   
/*     */   private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenJungle() {
/* 121 */     return createFallenTrees(Blocks.JUNGLE_LOG, 4, 11)
/* 122 */       .stumpDecorators((List)ImmutableList.of(TrunkVineDecorator.INSTANCE));
/*     */   }
/*     */ 
/*     */   
/*     */   private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenSpruce() {
/* 127 */     return createFallenTrees(Blocks.SPRUCE_LOG, 6, 10);
/*     */   }
/*     */   
/*     */   private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenTrees(Block paramBlock, int paramInt1, int paramInt2) {
/* 131 */     return (new FallenTreeConfiguration.FallenTreeConfigurationBuilder((BlockStateProvider)BlockStateProvider.simple(paramBlock), (IntProvider)UniformInt.of(paramInt1, paramInt2)))
/* 132 */       .logDecorators((List)ImmutableList.of(new AttachedToLogsDecorator(0.1F, (BlockStateProvider)new WeightedStateProvider(
/*     */               
/* 134 */               WeightedList.builder()
/* 135 */               .add(Blocks.RED_MUSHROOM.defaultBlockState(), 2)
/* 136 */               .add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1)), 
/* 137 */             List.of(Direction.UP))));
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createBirch() {
/* 141 */     return createStraightBlobTree(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 5, 2, 0, 2).ignoreVines();
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createSuperBirch() {
/* 145 */     return createStraightBlobTree(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 5, 2, 6, 2).ignoreVines();
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createJungleTree() {
/* 149 */     return createStraightBlobTree(Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES, 4, 8, 0, 2);
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createFancyOak() {
/* 153 */     return (new TreeConfiguration.TreeConfigurationBuilder(
/* 154 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.OAK_LOG), (TrunkPlacer)new FancyTrunkPlacer(3, 11, 0), 
/*     */         
/* 156 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.OAK_LEAVES), (FoliagePlacer)new FancyFoliagePlacer(
/* 157 */           (IntProvider)ConstantInt.of(2), (IntProvider)ConstantInt.of(4), 4), (FeatureSize)new TwoLayersFeatureSize(0, 0, 0, 
/* 158 */           OptionalInt.of(4))))
/* 159 */       .ignoreVines();
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder cherry() {
/* 163 */     return (new TreeConfiguration.TreeConfigurationBuilder(
/* 164 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.CHERRY_LOG), (TrunkPlacer)new CherryTrunkPlacer(7, 1, 0, (IntProvider)new WeightedListInt(
/*     */             
/* 166 */             WeightedList.builder()
/* 167 */             .add(ConstantInt.of(1), 1)
/* 168 */             .add(ConstantInt.of(2), 1)
/* 169 */             .add(ConstantInt.of(3), 1)
/* 170 */             .build()), 
/* 171 */           (IntProvider)UniformInt.of(2, 4), 
/* 172 */           UniformInt.of(-4, -3), 
/* 173 */           (IntProvider)UniformInt.of(-1, 0)), 
/*     */         
/* 175 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.CHERRY_LEAVES), (FoliagePlacer)new CherryFoliagePlacer(
/* 176 */           (IntProvider)ConstantInt.of(4), (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(5), 0.25F, 0.5F, 0.16666667F, 0.33333334F), (FeatureSize)new TwoLayersFeatureSize(1, 0, 2)))
/*     */       
/* 178 */       .ignoreVines();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 183 */   public static final ResourceKey<ConfiguredFeature<?, ?>> OAK = FeatureUtils.createKey("oak");
/* 184 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK = FeatureUtils.createKey("dark_oak");
/* 185 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK = FeatureUtils.createKey("pale_oak");
/* 186 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_BONEMEAL = FeatureUtils.createKey("pale_oak_bonemeal");
/* 187 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_CREAKING = FeatureUtils.createKey("pale_oak_creaking");
/* 188 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH = FeatureUtils.createKey("birch");
/*     */   
/* 190 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ACACIA = FeatureUtils.createKey("acacia");
/* 191 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRUCE = FeatureUtils.createKey("spruce");
/* 192 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PINE = FeatureUtils.createKey("pine");
/* 193 */   public static final ResourceKey<ConfiguredFeature<?, ?>> JUNGLE_TREE = FeatureUtils.createKey("jungle_tree");
/* 194 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK = FeatureUtils.createKey("fancy_oak");
/*     */   
/* 196 */   public static final ResourceKey<ConfiguredFeature<?, ?>> JUNGLE_TREE_NO_VINE = FeatureUtils.createKey("jungle_tree_no_vine");
/*     */   
/* 198 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_JUNGLE_TREE = FeatureUtils.createKey("mega_jungle_tree");
/* 199 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_SPRUCE = FeatureUtils.createKey("mega_spruce");
/* 200 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_PINE = FeatureUtils.createKey("mega_pine");
/* 201 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SUPER_BIRCH_BEES_0002 = FeatureUtils.createKey("super_birch_bees_0002");
/* 202 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SUPER_BIRCH_BEES = FeatureUtils.createKey("super_birch_bees");
/*     */   
/* 204 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SWAMP_OAK = FeatureUtils.createKey("swamp_oak");
/* 205 */   public static final ResourceKey<ConfiguredFeature<?, ?>> JUNGLE_BUSH = FeatureUtils.createKey("jungle_bush");
/* 206 */   public static final ResourceKey<ConfiguredFeature<?, ?>> AZALEA_TREE = FeatureUtils.createKey("azalea_tree");
/*     */   
/* 208 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MANGROVE = FeatureUtils.createKey("mangrove");
/*     */   
/* 210 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_MANGROVE = FeatureUtils.createKey("tall_mangrove");
/*     */   
/* 212 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CHERRY = FeatureUtils.createKey("cherry");
/*     */   
/* 214 */   public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_BEES_0002_LEAF_LITTER = FeatureUtils.createKey("oak_bees_0002_leaf_litter");
/* 215 */   public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_BEES_002 = FeatureUtils.createKey("oak_bees_002");
/* 216 */   public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_BEES_005 = FeatureUtils.createKey("oak_bees_005");
/* 217 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_BEES_0002 = FeatureUtils.createKey("birch_bees_0002");
/* 218 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_BEES_0002_LEAF_LITTER = FeatureUtils.createKey("birch_bees_0002_leaf_litter");
/* 219 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_BEES_002 = FeatureUtils.createKey("birch_bees_002");
/* 220 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_BEES_005 = FeatureUtils.createKey("birch_bees_005");
/* 221 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_BEES_0002_LEAF_LITTER = FeatureUtils.createKey("fancy_oak_bees_0002_leaf_litter");
/* 222 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_BEES_002 = FeatureUtils.createKey("fancy_oak_bees_002");
/* 223 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_BEES_005 = FeatureUtils.createKey("fancy_oak_bees_005");
/* 224 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_BEES = FeatureUtils.createKey("fancy_oak_bees");
/* 225 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CHERRY_BEES_005 = FeatureUtils.createKey("cherry_bees_005");
/*     */   
/* 227 */   public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_LEAF_LITTER = FeatureUtils.createKey("oak_leaf_litter");
/* 228 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK_LEAF_LITTER = FeatureUtils.createKey("dark_oak_leaf_litter");
/* 229 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_LEAF_LITTER = FeatureUtils.createKey("birch_leaf_litter");
/* 230 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_LEAF_LITTER = FeatureUtils.createKey("fancy_oak_leaf_litter");
/*     */   
/* 232 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_OAK_TREE = FeatureUtils.createKey("fallen_oak_tree");
/* 233 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_JUNGLE_TREE = FeatureUtils.createKey("fallen_jungle_tree");
/* 234 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SPRUCE_TREE = FeatureUtils.createKey("fallen_spruce_tree");
/* 235 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_BIRCH_TREE = FeatureUtils.createKey("fallen_birch_tree");
/* 236 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SUPER_BIRCH_TREE = FeatureUtils.createKey("fallen_super_birch_tree");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> paramBootstrapContext) {
/* 239 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.BLOCK);
/*     */ 
/*     */     
/* 242 */     BlockPredicate blockPredicate = BlockPredicate.matchesBlocks(new Block[] { Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.CHERRY_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.PALE_OAK_SAPLING, Blocks.MANGROVE_PROPAGULE, Blocks.DANDELION, Blocks.TORCHFLOWER, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.WITHER_ROSE, Blocks.LILY_OF_THE_VALLEY, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.WHEAT, Blocks.SUGAR_CANE, Blocks.ATTACHED_PUMPKIN_STEM, Blocks.ATTACHED_MELON_STEM, Blocks.PUMPKIN_STEM, Blocks.MELON_STEM, Blocks.LILY_PAD, Blocks.NETHER_WART, Blocks.COCOA, Blocks.CARROTS, Blocks.POTATOES, Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER, Blocks.TORCHFLOWER_CROP, Blocks.PITCHER_CROP, Blocks.BEETROOTS, Blocks.SWEET_BERRY_BUSH, Blocks.WARPED_FUNGUS, Blocks.CRIMSON_FUNGUS, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT, Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.SPORE_BLOSSOM, Blocks.AZALEA, Blocks.FLOWERING_AZALEA, Blocks.MOSS_CARPET, Blocks.PINK_PETALS, Blocks.WILDFLOWERS, Blocks.BIG_DRIPLEAF, Blocks.BIG_DRIPLEAF_STEM, Blocks.SMALL_DRIPLEAF });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 304 */     FeatureUtils.register(paramBootstrapContext, CRIMSON_FUNGUS, Feature.HUGE_FUNGUS, new HugeFungusConfiguration(Blocks.CRIMSON_NYLIUM
/* 305 */           .defaultBlockState(), Blocks.CRIMSON_STEM
/* 306 */           .defaultBlockState(), Blocks.NETHER_WART_BLOCK
/* 307 */           .defaultBlockState(), Blocks.SHROOMLIGHT
/* 308 */           .defaultBlockState(), blockPredicate, false));
/*     */ 
/*     */ 
/*     */     
/* 312 */     FeatureUtils.register(paramBootstrapContext, CRIMSON_FUNGUS_PLANTED, Feature.HUGE_FUNGUS, new HugeFungusConfiguration(Blocks.CRIMSON_NYLIUM
/* 313 */           .defaultBlockState(), Blocks.CRIMSON_STEM
/* 314 */           .defaultBlockState(), Blocks.NETHER_WART_BLOCK
/* 315 */           .defaultBlockState(), Blocks.SHROOMLIGHT
/* 316 */           .defaultBlockState(), blockPredicate, true));
/*     */ 
/*     */ 
/*     */     
/* 320 */     FeatureUtils.register(paramBootstrapContext, WARPED_FUNGUS, Feature.HUGE_FUNGUS, new HugeFungusConfiguration(Blocks.WARPED_NYLIUM
/* 321 */           .defaultBlockState(), Blocks.WARPED_STEM
/* 322 */           .defaultBlockState(), Blocks.WARPED_WART_BLOCK
/* 323 */           .defaultBlockState(), Blocks.SHROOMLIGHT
/* 324 */           .defaultBlockState(), blockPredicate, false));
/*     */ 
/*     */ 
/*     */     
/* 328 */     FeatureUtils.register(paramBootstrapContext, WARPED_FUNGUS_PLANTED, Feature.HUGE_FUNGUS, new HugeFungusConfiguration(Blocks.WARPED_NYLIUM
/* 329 */           .defaultBlockState(), Blocks.WARPED_STEM
/* 330 */           .defaultBlockState(), Blocks.WARPED_WART_BLOCK
/* 331 */           .defaultBlockState(), Blocks.SHROOMLIGHT
/* 332 */           .defaultBlockState(), blockPredicate, true));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 339 */     FeatureUtils.register(paramBootstrapContext, HUGE_BROWN_MUSHROOM, Feature.HUGE_BROWN_MUSHROOM, new HugeMushroomFeatureConfiguration(
/* 340 */           (BlockStateProvider)BlockStateProvider.simple((BlockState)((BlockState)Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState().setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf(true))).setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false))), 
/* 341 */           (BlockStateProvider)BlockStateProvider.simple((BlockState)((BlockState)Blocks.MUSHROOM_STEM.defaultBlockState().setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf(false))).setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false))), 3));
/*     */ 
/*     */     
/* 344 */     FeatureUtils.register(paramBootstrapContext, HUGE_RED_MUSHROOM, Feature.HUGE_RED_MUSHROOM, new HugeMushroomFeatureConfiguration(
/* 345 */           (BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.RED_MUSHROOM_BLOCK.defaultBlockState().setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false))), 
/* 346 */           (BlockStateProvider)BlockStateProvider.simple((BlockState)((BlockState)Blocks.MUSHROOM_STEM.defaultBlockState().setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf(false))).setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false))), 2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 352 */     BeehiveDecorator beehiveDecorator1 = new BeehiveDecorator(0.002F);
/* 353 */     BeehiveDecorator beehiveDecorator2 = new BeehiveDecorator(0.01F);
/* 354 */     BeehiveDecorator beehiveDecorator3 = new BeehiveDecorator(0.02F);
/* 355 */     BeehiveDecorator beehiveDecorator4 = new BeehiveDecorator(0.05F);
/* 356 */     BeehiveDecorator beehiveDecorator5 = new BeehiveDecorator(1.0F);
/*     */ 
/*     */ 
/*     */     
/* 360 */     PlaceOnGroundDecorator placeOnGroundDecorator1 = new PlaceOnGroundDecorator(96, 4, 2, (BlockStateProvider)new WeightedStateProvider(VegetationFeatures.leafLitterPatchBuilder(1, 3)));
/* 361 */     PlaceOnGroundDecorator placeOnGroundDecorator2 = new PlaceOnGroundDecorator(150, 2, 2, (BlockStateProvider)new WeightedStateProvider(VegetationFeatures.leafLitterPatchBuilder(1, 4)));
/*     */ 
/*     */ 
/*     */     
/* 365 */     FeatureUtils.register(paramBootstrapContext, OAK, Feature.TREE, 
/* 366 */         createOak()
/* 367 */         .build());
/*     */     
/* 369 */     FeatureUtils.register(paramBootstrapContext, DARK_OAK, Feature.TREE, 
/* 370 */         createDarkOak()
/* 371 */         .ignoreVines()
/* 372 */         .build());
/*     */     
/* 374 */     FeatureUtils.register(paramBootstrapContext, PALE_OAK, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 375 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LOG), (TrunkPlacer)new DarkOakTrunkPlacer(6, 2, 1), 
/*     */           
/* 377 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LEAVES), (FoliagePlacer)new DarkOakFoliagePlacer(
/* 378 */             (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0)), (FeatureSize)new ThreeLayersFeatureSize(1, 1, 0, 1, 2, 
/* 379 */             OptionalInt.empty())))
/*     */         
/* 381 */         .decorators((List)ImmutableList.of(new PaleMossDecorator(0.15F, 0.4F, 0.8F)))
/*     */ 
/*     */         
/* 384 */         .ignoreVines()
/* 385 */         .build());
/*     */     
/* 387 */     FeatureUtils.register(paramBootstrapContext, PALE_OAK_BONEMEAL, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 388 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LOG), (TrunkPlacer)new DarkOakTrunkPlacer(6, 2, 1), 
/*     */           
/* 390 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LEAVES), (FoliagePlacer)new DarkOakFoliagePlacer(
/* 391 */             (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0)), (FeatureSize)new ThreeLayersFeatureSize(1, 1, 0, 1, 2, 
/* 392 */             OptionalInt.empty())))
/*     */         
/* 394 */         .ignoreVines()
/* 395 */         .build());
/*     */     
/* 397 */     FeatureUtils.register(paramBootstrapContext, PALE_OAK_CREAKING, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 398 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LOG), (TrunkPlacer)new DarkOakTrunkPlacer(6, 2, 1), 
/*     */           
/* 400 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LEAVES), (FoliagePlacer)new DarkOakFoliagePlacer(
/* 401 */             (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0)), (FeatureSize)new ThreeLayersFeatureSize(1, 1, 0, 1, 2, 
/* 402 */             OptionalInt.empty())))
/*     */         
/* 404 */         .decorators((List)ImmutableList.of(new PaleMossDecorator(0.15F, 0.4F, 0.8F), new CreakingHeartDecorator(1.0F)))
/*     */ 
/*     */ 
/*     */         
/* 408 */         .ignoreVines()
/* 409 */         .build());
/*     */     
/* 411 */     FeatureUtils.register(paramBootstrapContext, BIRCH, Feature.TREE, 
/* 412 */         createBirch()
/* 413 */         .build());
/*     */ 
/*     */     
/* 416 */     FeatureUtils.register(paramBootstrapContext, ACACIA, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 417 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.ACACIA_LOG), (TrunkPlacer)new ForkingTrunkPlacer(5, 2, 2), 
/*     */           
/* 419 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.ACACIA_LEAVES), (FoliagePlacer)new AcaciaFoliagePlacer(
/* 420 */             (IntProvider)ConstantInt.of(2), (IntProvider)ConstantInt.of(0)), (FeatureSize)new TwoLayersFeatureSize(1, 0, 2)))
/*     */ 
/*     */         
/* 423 */         .ignoreVines()
/* 424 */         .build());
/*     */     
/* 426 */     FeatureUtils.register(paramBootstrapContext, CHERRY, Feature.TREE, cherry()
/* 427 */         .build());
/*     */     
/* 429 */     FeatureUtils.register(paramBootstrapContext, CHERRY_BEES_005, Feature.TREE, cherry()
/* 430 */         .decorators(List.of(beehiveDecorator4))
/* 431 */         .build());
/*     */     
/* 433 */     FeatureUtils.register(paramBootstrapContext, SPRUCE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 434 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LOG), (TrunkPlacer)new StraightTrunkPlacer(5, 2, 1), 
/*     */           
/* 436 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), (FoliagePlacer)new SpruceFoliagePlacer(
/* 437 */             (IntProvider)UniformInt.of(2, 3), (IntProvider)UniformInt.of(0, 2), (IntProvider)UniformInt.of(1, 2)), (FeatureSize)new TwoLayersFeatureSize(2, 0, 2)))
/*     */ 
/*     */         
/* 440 */         .ignoreVines()
/* 441 */         .build());
/*     */     
/* 443 */     FeatureUtils.register(paramBootstrapContext, PINE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 444 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LOG), (TrunkPlacer)new StraightTrunkPlacer(6, 4, 0), 
/*     */           
/* 446 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), (FoliagePlacer)new PineFoliagePlacer(
/* 447 */             (IntProvider)ConstantInt.of(1), (IntProvider)ConstantInt.of(1), (IntProvider)UniformInt.of(3, 4)), (FeatureSize)new TwoLayersFeatureSize(2, 0, 2)))
/*     */ 
/*     */         
/* 450 */         .ignoreVines()
/* 451 */         .build());
/*     */     
/* 453 */     FeatureUtils.register(paramBootstrapContext, JUNGLE_TREE, Feature.TREE, 
/* 454 */         createJungleTree()
/* 455 */         .decorators((List)ImmutableList.of(new CocoaDecorator(0.2F), TrunkVineDecorator.INSTANCE, new LeaveVineDecorator(0.25F)))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 460 */         .ignoreVines()
/* 461 */         .build());
/*     */     
/* 463 */     FeatureUtils.register(paramBootstrapContext, FANCY_OAK, Feature.TREE, 
/* 464 */         createFancyOak()
/* 465 */         .build());
/*     */     
/* 467 */     FeatureUtils.register(paramBootstrapContext, JUNGLE_TREE_NO_VINE, Feature.TREE, 
/* 468 */         createJungleTree()
/* 469 */         .ignoreVines()
/* 470 */         .build());
/*     */ 
/*     */     
/* 473 */     FeatureUtils.register(paramBootstrapContext, MEGA_JUNGLE_TREE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 474 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.JUNGLE_LOG), (TrunkPlacer)new MegaJungleTrunkPlacer(10, 2, 19), 
/*     */           
/* 476 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.JUNGLE_LEAVES), (FoliagePlacer)new MegaJungleFoliagePlacer(
/* 477 */             (IntProvider)ConstantInt.of(2), (IntProvider)ConstantInt.of(0), 2), (FeatureSize)new TwoLayersFeatureSize(1, 1, 2)))
/*     */ 
/*     */         
/* 480 */         .decorators((List)ImmutableList.of(TrunkVineDecorator.INSTANCE, new LeaveVineDecorator(0.25F)))
/*     */ 
/*     */ 
/*     */         
/* 484 */         .build());
/*     */     
/* 486 */     FeatureUtils.register(paramBootstrapContext, MEGA_SPRUCE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 487 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LOG), (TrunkPlacer)new GiantTrunkPlacer(13, 2, 14), 
/*     */           
/* 489 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), (FoliagePlacer)new MegaPineFoliagePlacer(
/* 490 */             (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0), (IntProvider)UniformInt.of(13, 17)), (FeatureSize)new TwoLayersFeatureSize(1, 1, 2)))
/*     */ 
/*     */         
/* 493 */         .decorators((List)ImmutableList.of(new AlterGroundDecorator(
/* 494 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.PODZOL))))
/*     */         
/* 496 */         .build());
/*     */     
/* 498 */     FeatureUtils.register(paramBootstrapContext, MEGA_PINE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 499 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LOG), (TrunkPlacer)new GiantTrunkPlacer(13, 2, 14), 
/*     */           
/* 501 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), (FoliagePlacer)new MegaPineFoliagePlacer(
/* 502 */             (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0), (IntProvider)UniformInt.of(3, 7)), (FeatureSize)new TwoLayersFeatureSize(1, 1, 2)))
/*     */ 
/*     */         
/* 505 */         .decorators((List)ImmutableList.of(new AlterGroundDecorator(
/* 506 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.PODZOL))))
/*     */         
/* 508 */         .build());
/*     */     
/* 510 */     FeatureUtils.register(paramBootstrapContext, SUPER_BIRCH_BEES_0002, Feature.TREE, 
/* 511 */         createSuperBirch()
/* 512 */         .decorators((List)ImmutableList.of(beehiveDecorator1))
/* 513 */         .build());
/*     */     
/* 515 */     FeatureUtils.register(paramBootstrapContext, SUPER_BIRCH_BEES, Feature.TREE, 
/* 516 */         createSuperBirch()
/* 517 */         .decorators((List)ImmutableList.of(beehiveDecorator5))
/* 518 */         .build());
/*     */ 
/*     */     
/* 521 */     FeatureUtils.register(paramBootstrapContext, SWAMP_OAK, Feature.TREE, 
/* 522 */         createStraightBlobTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 5, 3, 0, 3)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 530 */         .decorators((List)ImmutableList.of(new LeaveVineDecorator(0.25F)))
/* 531 */         .build());
/*     */     
/* 533 */     FeatureUtils.register(paramBootstrapContext, JUNGLE_BUSH, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 534 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.JUNGLE_LOG), (TrunkPlacer)new StraightTrunkPlacer(1, 0, 0), 
/*     */           
/* 536 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.OAK_LEAVES), (FoliagePlacer)new BushFoliagePlacer(
/* 537 */             (IntProvider)ConstantInt.of(2), (IntProvider)ConstantInt.of(1), 2), (FeatureSize)new TwoLayersFeatureSize(0, 0, 0)))
/*     */ 
/*     */         
/* 540 */         .build());
/*     */     
/* 542 */     FeatureUtils.register(paramBootstrapContext, AZALEA_TREE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 543 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.OAK_LOG), (TrunkPlacer)new BendingTrunkPlacer(4, 2, 0, 3, 
/* 544 */             (IntProvider)UniformInt.of(1, 2)), (BlockStateProvider)new WeightedStateProvider(
/* 545 */             WeightedList.builder().add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3).add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)), (FoliagePlacer)new RandomSpreadFoliagePlacer(
/* 546 */             (IntProvider)ConstantInt.of(3), (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(2), 50), (FeatureSize)new TwoLayersFeatureSize(1, 0, 1)))
/*     */ 
/*     */         
/* 549 */         .dirt((BlockStateProvider)BlockStateProvider.simple(Blocks.ROOTED_DIRT))
/* 550 */         .forceDirt()
/* 551 */         .build());
/*     */ 
/*     */     
/* 554 */     FeatureUtils.register(paramBootstrapContext, MANGROVE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 555 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_LOG), (TrunkPlacer)new UpwardsBranchingTrunkPlacer(2, 1, 4, 
/* 556 */             (IntProvider)UniformInt.of(1, 4), 0.5F, (IntProvider)UniformInt.of(0, 1), (HolderSet)holderGetter.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)), 
/* 557 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_LEAVES), (FoliagePlacer)new RandomSpreadFoliagePlacer(
/* 558 */             (IntProvider)ConstantInt.of(3), (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(2), 70), 
/* 559 */           Optional.of(new MangroveRootPlacer(
/* 560 */               (IntProvider)UniformInt.of(1, 3), 
/* 561 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_ROOTS), 
/* 562 */               Optional.of(new AboveRootPlacement(
/* 563 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.MOSS_CARPET), 0.5F)), new MangroveRootPlacement((HolderSet)holderGetter
/*     */ 
/*     */ 
/*     */                 
/* 567 */                 .getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH), 
/* 568 */                 (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS
/* 569 */                   }), (BlockStateProvider)BlockStateProvider.simple(Blocks.MUDDY_MANGROVE_ROOTS), 8, 15, 0.2F))), (FeatureSize)new TwoLayersFeatureSize(2, 0, 2)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 577 */         .decorators(List.of(new LeaveVineDecorator(0.125F), new AttachedToLeavesDecorator(0.14F, 1, 0, (BlockStateProvider)new RandomizedIntStateProvider(
/*     */ 
/*     */ 
/*     */                 
/* 581 */                 (BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.MANGROVE_PROPAGULE.defaultBlockState().setValue((Property)MangrovePropaguleBlock.HANGING, Boolean.valueOf(true))), MangrovePropaguleBlock.AGE, 
/*     */                 
/* 583 */                 (IntProvider)UniformInt.of(0, 4)), 2, 
/* 584 */               List.of(Direction.DOWN)), beehiveDecorator2))
/*     */ 
/*     */         
/* 587 */         .ignoreVines()
/* 588 */         .build());
/*     */ 
/*     */     
/* 591 */     FeatureUtils.register(paramBootstrapContext, TALL_MANGROVE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 592 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_LOG), (TrunkPlacer)new UpwardsBranchingTrunkPlacer(4, 1, 9, 
/* 593 */             (IntProvider)UniformInt.of(1, 6), 0.5F, (IntProvider)UniformInt.of(0, 1), (HolderSet)holderGetter.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)), 
/* 594 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_LEAVES), (FoliagePlacer)new RandomSpreadFoliagePlacer(
/* 595 */             (IntProvider)ConstantInt.of(3), (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(2), 70), 
/* 596 */           Optional.of(new MangroveRootPlacer(
/* 597 */               (IntProvider)UniformInt.of(3, 7), 
/* 598 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_ROOTS), 
/* 599 */               Optional.of(new AboveRootPlacement(
/* 600 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.MOSS_CARPET), 0.5F)), new MangroveRootPlacement((HolderSet)holderGetter
/*     */ 
/*     */ 
/*     */                 
/* 604 */                 .getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH), 
/* 605 */                 (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS
/* 606 */                   }), (BlockStateProvider)BlockStateProvider.simple(Blocks.MUDDY_MANGROVE_ROOTS), 8, 15, 0.2F))), (FeatureSize)new TwoLayersFeatureSize(3, 0, 2)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 614 */         .decorators(List.of(new LeaveVineDecorator(0.125F), new AttachedToLeavesDecorator(0.14F, 1, 0, (BlockStateProvider)new RandomizedIntStateProvider(
/*     */ 
/*     */ 
/*     */                 
/* 618 */                 (BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.MANGROVE_PROPAGULE.defaultBlockState().setValue((Property)MangrovePropaguleBlock.HANGING, Boolean.valueOf(true))), MangrovePropaguleBlock.AGE, 
/*     */                 
/* 620 */                 (IntProvider)UniformInt.of(0, 4)), 2, 
/* 621 */               List.of(Direction.DOWN)), beehiveDecorator2))
/*     */ 
/*     */         
/* 624 */         .ignoreVines()
/* 625 */         .build());
/*     */     
/* 627 */     FeatureUtils.register(paramBootstrapContext, OAK_BEES_0002_LEAF_LITTER, Feature.TREE, 
/* 628 */         createOak()
/* 629 */         .decorators(List.of(beehiveDecorator1, placeOnGroundDecorator1, placeOnGroundDecorator2))
/* 630 */         .build());
/*     */     
/* 632 */     FeatureUtils.register(paramBootstrapContext, OAK_BEES_002, Feature.TREE, 
/* 633 */         createOak()
/* 634 */         .decorators(List.of(beehiveDecorator3))
/* 635 */         .build());
/*     */     
/* 637 */     FeatureUtils.register(paramBootstrapContext, OAK_BEES_005, Feature.TREE, 
/* 638 */         createOak()
/* 639 */         .decorators(List.of(beehiveDecorator4))
/* 640 */         .build());
/*     */     
/* 642 */     FeatureUtils.register(paramBootstrapContext, BIRCH_BEES_0002, Feature.TREE, 
/* 643 */         createBirch()
/* 644 */         .decorators(List.of(beehiveDecorator1))
/* 645 */         .build());
/*     */     
/* 647 */     FeatureUtils.register(paramBootstrapContext, BIRCH_BEES_0002_LEAF_LITTER, Feature.TREE, 
/* 648 */         createBirch()
/* 649 */         .decorators(List.of(beehiveDecorator1, placeOnGroundDecorator1, placeOnGroundDecorator2))
/* 650 */         .build());
/*     */     
/* 652 */     FeatureUtils.register(paramBootstrapContext, BIRCH_BEES_002, Feature.TREE, 
/* 653 */         createBirch()
/* 654 */         .decorators(List.of(beehiveDecorator3))
/* 655 */         .build());
/*     */     
/* 657 */     FeatureUtils.register(paramBootstrapContext, BIRCH_BEES_005, Feature.TREE, 
/* 658 */         createBirch()
/* 659 */         .decorators(List.of(beehiveDecorator4))
/* 660 */         .build());
/*     */     
/* 662 */     FeatureUtils.register(paramBootstrapContext, FANCY_OAK_BEES_0002_LEAF_LITTER, Feature.TREE, 
/* 663 */         createFancyOak()
/* 664 */         .decorators(List.of(beehiveDecorator1, placeOnGroundDecorator1, placeOnGroundDecorator2))
/* 665 */         .build());
/*     */     
/* 667 */     FeatureUtils.register(paramBootstrapContext, FANCY_OAK_BEES_002, Feature.TREE, 
/* 668 */         createFancyOak()
/* 669 */         .decorators(List.of(beehiveDecorator3))
/* 670 */         .build());
/*     */     
/* 672 */     FeatureUtils.register(paramBootstrapContext, FANCY_OAK_BEES_005, Feature.TREE, 
/* 673 */         createFancyOak()
/* 674 */         .decorators(List.of(beehiveDecorator4))
/* 675 */         .build());
/*     */     
/* 677 */     FeatureUtils.register(paramBootstrapContext, FANCY_OAK_BEES, Feature.TREE, 
/* 678 */         createFancyOak()
/* 679 */         .decorators(List.of(beehiveDecorator5))
/* 680 */         .build());
/*     */ 
/*     */     
/* 683 */     FeatureUtils.register(paramBootstrapContext, OAK_LEAF_LITTER, Feature.TREE, 
/* 684 */         createOak()
/* 685 */         .decorators((List)ImmutableList.of(placeOnGroundDecorator1, placeOnGroundDecorator2))
/* 686 */         .build());
/*     */ 
/*     */     
/* 689 */     FeatureUtils.register(paramBootstrapContext, DARK_OAK_LEAF_LITTER, Feature.TREE, 
/* 690 */         createDarkOak()
/* 691 */         .ignoreVines()
/* 692 */         .decorators((List)ImmutableList.of(placeOnGroundDecorator1, placeOnGroundDecorator2))
/* 693 */         .build());
/*     */ 
/*     */     
/* 696 */     FeatureUtils.register(paramBootstrapContext, BIRCH_LEAF_LITTER, Feature.TREE, 
/* 697 */         createBirch()
/* 698 */         .decorators((List)ImmutableList.of(placeOnGroundDecorator1, placeOnGroundDecorator2))
/* 699 */         .build());
/*     */ 
/*     */     
/* 702 */     FeatureUtils.register(paramBootstrapContext, FANCY_OAK_LEAF_LITTER, Feature.TREE, 
/* 703 */         createFancyOak()
/* 704 */         .decorators(List.of(placeOnGroundDecorator1, placeOnGroundDecorator2))
/* 705 */         .build());
/*     */ 
/*     */     
/* 708 */     FeatureUtils.register(paramBootstrapContext, FALLEN_OAK_TREE, Feature.FALLEN_TREE, 
/* 709 */         createFallenOak()
/* 710 */         .build());
/*     */ 
/*     */     
/* 713 */     FeatureUtils.register(paramBootstrapContext, FALLEN_BIRCH_TREE, Feature.FALLEN_TREE, 
/* 714 */         createFallenBirch(8)
/* 715 */         .build());
/*     */ 
/*     */     
/* 718 */     FeatureUtils.register(paramBootstrapContext, FALLEN_SUPER_BIRCH_TREE, Feature.FALLEN_TREE, 
/* 719 */         createFallenBirch(15)
/* 720 */         .build());
/*     */ 
/*     */     
/* 723 */     FeatureUtils.register(paramBootstrapContext, FALLEN_JUNGLE_TREE, Feature.FALLEN_TREE, 
/* 724 */         createFallenJungle()
/* 725 */         .build());
/*     */ 
/*     */     
/* 728 */     FeatureUtils.register(paramBootstrapContext, FALLEN_SPRUCE_TREE, Feature.FALLEN_TREE, 
/* 729 */         createFallenSpruce()
/* 730 */         .build());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\features\TreeFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */