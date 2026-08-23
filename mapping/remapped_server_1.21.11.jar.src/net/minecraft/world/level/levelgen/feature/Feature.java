/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RootSystemConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TwistingVinesConfig;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.UnderwaterMagmaConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
/*     */ 
/*     */ public abstract class Feature<FC extends FeatureConfiguration> {
/*  60 */   public static final Feature<NoneFeatureConfiguration> NO_OP = register("no_op", new NoOpFeature(NoneFeatureConfiguration.CODEC));
/*  61 */   public static final Feature<TreeConfiguration> TREE = register("tree", new TreeFeature(TreeConfiguration.CODEC));
/*  62 */   public static final Feature<FallenTreeConfiguration> FALLEN_TREE = register("fallen_tree", new FallenTreeFeature(FallenTreeConfiguration.CODEC));
/*     */   
/*  64 */   public static final Feature<RandomPatchConfiguration> FLOWER = register("flower", new RandomPatchFeature(RandomPatchConfiguration.CODEC));
/*  65 */   public static final Feature<RandomPatchConfiguration> NO_BONEMEAL_FLOWER = register("no_bonemeal_flower", new RandomPatchFeature(RandomPatchConfiguration.CODEC));
/*  66 */   public static final Feature<RandomPatchConfiguration> RANDOM_PATCH = register("random_patch", new RandomPatchFeature(RandomPatchConfiguration.CODEC));
/*  67 */   public static final Feature<BlockPileConfiguration> BLOCK_PILE = register("block_pile", new BlockPileFeature(BlockPileConfiguration.CODEC));
/*  68 */   public static final Feature<SpringConfiguration> SPRING = register("spring_feature", new SpringFeature(SpringConfiguration.CODEC));
/*     */   
/*  70 */   public static final Feature<NoneFeatureConfiguration> CHORUS_PLANT = register("chorus_plant", new ChorusPlantFeature(NoneFeatureConfiguration.CODEC));
/*  71 */   public static final Feature<ReplaceBlockConfiguration> REPLACE_SINGLE_BLOCK = register("replace_single_block", new ReplaceBlockFeature(ReplaceBlockConfiguration.CODEC));
/*     */   
/*  73 */   public static final Feature<NoneFeatureConfiguration> VOID_START_PLATFORM = register("void_start_platform", new VoidStartPlatformFeature(NoneFeatureConfiguration.CODEC));
/*  74 */   public static final Feature<NoneFeatureConfiguration> DESERT_WELL = register("desert_well", new DesertWellFeature(NoneFeatureConfiguration.CODEC));
/*  75 */   public static final Feature<FossilFeatureConfiguration> FOSSIL = register("fossil", new FossilFeature(FossilFeatureConfiguration.CODEC));
/*  76 */   public static final Feature<HugeMushroomFeatureConfiguration> HUGE_RED_MUSHROOM = register("huge_red_mushroom", new HugeRedMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
/*  77 */   public static final Feature<HugeMushroomFeatureConfiguration> HUGE_BROWN_MUSHROOM = register("huge_brown_mushroom", new HugeBrownMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
/*  78 */   public static final Feature<NoneFeatureConfiguration> ICE_SPIKE = register("ice_spike", new IceSpikeFeature(NoneFeatureConfiguration.CODEC));
/*  79 */   public static final Feature<NoneFeatureConfiguration> GLOWSTONE_BLOB = register("glowstone_blob", new GlowstoneFeature(NoneFeatureConfiguration.CODEC));
/*  80 */   public static final Feature<NoneFeatureConfiguration> FREEZE_TOP_LAYER = register("freeze_top_layer", new SnowAndFreezeFeature(NoneFeatureConfiguration.CODEC));
/*  81 */   public static final Feature<NoneFeatureConfiguration> VINES = register("vines", new VinesFeature(NoneFeatureConfiguration.CODEC));
/*  82 */   public static final Feature<BlockColumnConfiguration> BLOCK_COLUMN = register("block_column", new BlockColumnFeature(BlockColumnConfiguration.CODEC));
/*  83 */   public static final Feature<VegetationPatchConfiguration> VEGETATION_PATCH = register("vegetation_patch", new VegetationPatchFeature(VegetationPatchConfiguration.CODEC));
/*  84 */   public static final Feature<VegetationPatchConfiguration> WATERLOGGED_VEGETATION_PATCH = register("waterlogged_vegetation_patch", new WaterloggedVegetationPatchFeature(VegetationPatchConfiguration.CODEC));
/*  85 */   public static final Feature<RootSystemConfiguration> ROOT_SYSTEM = register("root_system", new RootSystemFeature(RootSystemConfiguration.CODEC));
/*  86 */   public static final Feature<MultifaceGrowthConfiguration> MULTIFACE_GROWTH = register("multiface_growth", new MultifaceGrowthFeature(MultifaceGrowthConfiguration.CODEC));
/*  87 */   public static final Feature<UnderwaterMagmaConfiguration> UNDERWATER_MAGMA = register("underwater_magma", new UnderwaterMagmaFeature(UnderwaterMagmaConfiguration.CODEC));
/*  88 */   public static final Feature<NoneFeatureConfiguration> MONSTER_ROOM = register("monster_room", new MonsterRoomFeature(NoneFeatureConfiguration.CODEC));
/*  89 */   public static final Feature<NoneFeatureConfiguration> BLUE_ICE = register("blue_ice", new BlueIceFeature(NoneFeatureConfiguration.CODEC));
/*  90 */   public static final Feature<BlockStateConfiguration> ICEBERG = register("iceberg", new IcebergFeature(BlockStateConfiguration.CODEC));
/*  91 */   public static final Feature<BlockStateConfiguration> FOREST_ROCK = register("forest_rock", new BlockBlobFeature(BlockStateConfiguration.CODEC));
/*  92 */   public static final Feature<DiskConfiguration> DISK = register("disk", new DiskFeature(DiskConfiguration.CODEC));
/*  93 */   public static final Feature<LakeFeature.Configuration> LAKE = register("lake", new LakeFeature(LakeFeature.Configuration.CODEC));
/*  94 */   public static final Feature<OreConfiguration> ORE = register("ore", new OreFeature(OreConfiguration.CODEC));
/*  95 */   public static final Feature<NoneFeatureConfiguration> END_PLATFORM = register("end_platform", new EndPlatformFeature(NoneFeatureConfiguration.CODEC));
/*  96 */   public static final Feature<SpikeConfiguration> END_SPIKE = register("end_spike", new SpikeFeature(SpikeConfiguration.CODEC));
/*  97 */   public static final Feature<NoneFeatureConfiguration> END_ISLAND = register("end_island", new EndIslandFeature(NoneFeatureConfiguration.CODEC));
/*  98 */   public static final Feature<EndGatewayConfiguration> END_GATEWAY = register("end_gateway", new EndGatewayFeature(EndGatewayConfiguration.CODEC));
/*  99 */   public static final SeagrassFeature SEAGRASS = register("seagrass", new SeagrassFeature(ProbabilityFeatureConfiguration.CODEC));
/* 100 */   public static final Feature<NoneFeatureConfiguration> KELP = register("kelp", new KelpFeature(NoneFeatureConfiguration.CODEC));
/* 101 */   public static final Feature<NoneFeatureConfiguration> CORAL_TREE = register("coral_tree", new CoralTreeFeature(NoneFeatureConfiguration.CODEC));
/* 102 */   public static final Feature<NoneFeatureConfiguration> CORAL_MUSHROOM = register("coral_mushroom", new CoralMushroomFeature(NoneFeatureConfiguration.CODEC));
/* 103 */   public static final Feature<NoneFeatureConfiguration> CORAL_CLAW = register("coral_claw", new CoralClawFeature(NoneFeatureConfiguration.CODEC));
/* 104 */   public static final Feature<CountConfiguration> SEA_PICKLE = register("sea_pickle", new SeaPickleFeature(CountConfiguration.CODEC));
/* 105 */   public static final Feature<SimpleBlockConfiguration> SIMPLE_BLOCK = register("simple_block", new SimpleBlockFeature(SimpleBlockConfiguration.CODEC));
/* 106 */   public static final Feature<ProbabilityFeatureConfiguration> BAMBOO = register("bamboo", new BambooFeature(ProbabilityFeatureConfiguration.CODEC));
/*     */   
/* 108 */   public static final Feature<HugeFungusConfiguration> HUGE_FUNGUS = register("huge_fungus", new HugeFungusFeature(HugeFungusConfiguration.CODEC));
/* 109 */   public static final Feature<NetherForestVegetationConfig> NETHER_FOREST_VEGETATION = register("nether_forest_vegetation", new NetherForestVegetationFeature(NetherForestVegetationConfig.CODEC));
/* 110 */   public static final Feature<NoneFeatureConfiguration> WEEPING_VINES = register("weeping_vines", new WeepingVinesFeature(NoneFeatureConfiguration.CODEC));
/* 111 */   public static final Feature<TwistingVinesConfig> TWISTING_VINES = register("twisting_vines", new TwistingVinesFeature(TwistingVinesConfig.CODEC));
/*     */   
/* 113 */   public static final Feature<ColumnFeatureConfiguration> BASALT_COLUMNS = register("basalt_columns", new BasaltColumnsFeature(ColumnFeatureConfiguration.CODEC));
/* 114 */   public static final Feature<DeltaFeatureConfiguration> DELTA_FEATURE = register("delta_feature", new DeltaFeature(DeltaFeatureConfiguration.CODEC));
/* 115 */   public static final Feature<ReplaceSphereConfiguration> REPLACE_BLOBS = register("netherrack_replace_blobs", new ReplaceBlobsFeature(ReplaceSphereConfiguration.CODEC));
/*     */   
/* 117 */   public static final Feature<LayerConfiguration> FILL_LAYER = register("fill_layer", new FillLayerFeature(LayerConfiguration.CODEC));
/* 118 */   public static final BonusChestFeature BONUS_CHEST = register("bonus_chest", new BonusChestFeature(NoneFeatureConfiguration.CODEC));
/* 119 */   public static final Feature<NoneFeatureConfiguration> BASALT_PILLAR = register("basalt_pillar", new BasaltPillarFeature(NoneFeatureConfiguration.CODEC));
/* 120 */   public static final Feature<OreConfiguration> SCATTERED_ORE = register("scattered_ore", new ScatteredOreFeature(OreConfiguration.CODEC));
/*     */   
/* 122 */   public static final Feature<RandomFeatureConfiguration> RANDOM_SELECTOR = register("random_selector", new RandomSelectorFeature(RandomFeatureConfiguration.CODEC));
/* 123 */   public static final Feature<SimpleRandomFeatureConfiguration> SIMPLE_RANDOM_SELECTOR = register("simple_random_selector", new SimpleRandomSelectorFeature(SimpleRandomFeatureConfiguration.CODEC));
/* 124 */   public static final Feature<RandomBooleanFeatureConfiguration> RANDOM_BOOLEAN_SELECTOR = register("random_boolean_selector", new RandomBooleanSelectorFeature(RandomBooleanFeatureConfiguration.CODEC));
/*     */   
/* 126 */   public static final Feature<GeodeConfiguration> GEODE = register("geode", new GeodeFeature(GeodeConfiguration.CODEC));
/*     */   
/* 128 */   public static final Feature<DripstoneClusterConfiguration> DRIPSTONE_CLUSTER = register("dripstone_cluster", new DripstoneClusterFeature(DripstoneClusterConfiguration.CODEC));
/* 129 */   public static final Feature<LargeDripstoneConfiguration> LARGE_DRIPSTONE = register("large_dripstone", new LargeDripstoneFeature(LargeDripstoneConfiguration.CODEC));
/* 130 */   public static final Feature<PointedDripstoneConfiguration> POINTED_DRIPSTONE = register("pointed_dripstone", new PointedDripstoneFeature(PointedDripstoneConfiguration.CODEC));
/*     */   
/* 132 */   public static final Feature<SculkPatchConfiguration> SCULK_PATCH = register("sculk_patch", new SculkPatchFeature(SculkPatchConfiguration.CODEC)); private final MapCodec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec;
/*     */   
/*     */   private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String paramString, F paramF) {
/* 135 */     return (F)Registry.register(BuiltInRegistries.FEATURE, paramString, paramF);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Feature(Codec<FC> paramCodec) {
/* 141 */     this.configuredCodec = paramCodec.fieldOf("config").xmap(paramFeatureConfiguration -> new ConfiguredFeature<>(this, paramFeatureConfiguration), ConfiguredFeature::config);
/*     */   }
/*     */   
/*     */   public MapCodec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec() {
/* 145 */     return this.configuredCodec;
/*     */   }
/*     */   
/*     */   protected void setBlock(LevelWriter paramLevelWriter, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 149 */     paramLevelWriter.setBlock(paramBlockPos, paramBlockState, 3);
/*     */   }
/*     */   
/*     */   public static Predicate<BlockState> isReplaceable(TagKey<Block> paramTagKey) {
/* 153 */     return paramBlockState -> !paramBlockState.is(paramTagKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void safeSetBlock(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos, BlockState paramBlockState, Predicate<BlockState> paramPredicate) {
/* 161 */     if (paramPredicate.test(paramWorldGenLevel.getBlockState(paramBlockPos))) {
/* 162 */       paramWorldGenLevel.setBlock(paramBlockPos, paramBlockState, 2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FC paramFC, WorldGenLevel paramWorldGenLevel, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 169 */     if (paramWorldGenLevel.ensureCanWrite(paramBlockPos)) {
/* 170 */       return place(new FeaturePlaceContext<>(Optional.empty(), paramWorldGenLevel, paramChunkGenerator, paramRandomSource, paramBlockPos, paramFC));
/*     */     }
/* 172 */     return false;
/*     */   }
/*     */   
/*     */   protected static boolean isStone(BlockState paramBlockState) {
/* 176 */     return paramBlockState.is(BlockTags.BASE_STONE_OVERWORLD);
/*     */   }
/*     */   
/*     */   public static boolean isDirt(BlockState paramBlockState) {
/* 180 */     return paramBlockState.is(BlockTags.DIRT);
/*     */   }
/*     */   
/*     */   public static boolean isGrassOrDirt(LevelSimulatedReader paramLevelSimulatedReader, BlockPos paramBlockPos) {
/* 184 */     return paramLevelSimulatedReader.isStateAtPosition(paramBlockPos, Feature::isDirt);
/*     */   }
/*     */   
/*     */   public static boolean checkNeighbors(Function<BlockPos, BlockState> paramFunction, BlockPos paramBlockPos, Predicate<BlockState> paramPredicate) {
/* 188 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 189 */     for (Direction direction : Direction.values()) {
/* 190 */       mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, direction);
/* 191 */       if (paramPredicate.test(paramFunction.apply(mutableBlockPos))) {
/* 192 */         return true;
/*     */       }
/*     */     } 
/* 195 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isAdjacentToAir(Function<BlockPos, BlockState> paramFunction, BlockPos paramBlockPos) {
/* 199 */     return checkNeighbors(paramFunction, paramBlockPos, BlockBehaviour.BlockStateBase::isAir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markAboveForPostProcessing(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos) {
/* 207 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 208 */     for (byte b = 0; b < 2; b++) {
/* 209 */       mutableBlockPos.move(Direction.UP);
/* 210 */       if (paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos).isAir()) {
/*     */         return;
/*     */       }
/* 213 */       paramWorldGenLevel.getChunk((BlockPos)mutableBlockPos).markPosForPostprocessing((BlockPos)mutableBlockPos);
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract boolean place(FeaturePlaceContext<FC> paramFeaturePlaceContext);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\Feature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */