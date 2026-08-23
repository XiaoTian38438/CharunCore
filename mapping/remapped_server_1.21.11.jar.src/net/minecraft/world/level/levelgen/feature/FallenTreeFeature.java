/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ 
/*     */ public class FallenTreeFeature
/*     */   extends Feature<FallenTreeConfiguration>
/*     */ {
/*     */   private static final int STUMP_HEIGHT = 1;
/*     */   private static final int STUMP_HEIGHT_PLUS_EMPTY_SPACE = 2;
/*     */   
/*     */   public FallenTreeFeature(Codec<FallenTreeConfiguration> paramCodec) {
/*  29 */     super(paramCodec);
/*     */   }
/*     */   private static final int FALLEN_LOG_MAX_FALL_HEIGHT_TO_GROUND = 5; private static final int FALLEN_LOG_MAX_GROUND_GAP = 2; private static final int FALLEN_LOG_MAX_SPACE_FROM_STUMP = 2;
/*     */   
/*     */   public boolean place(FeaturePlaceContext<FallenTreeConfiguration> paramFeaturePlaceContext) {
/*  34 */     placeFallenTree(paramFeaturePlaceContext.config(), paramFeaturePlaceContext.origin(), paramFeaturePlaceContext.level(), paramFeaturePlaceContext.random());
/*  35 */     return true;
/*     */   }
/*     */   
/*     */   private void placeFallenTree(FallenTreeConfiguration paramFallenTreeConfiguration, BlockPos paramBlockPos, WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource) {
/*  39 */     placeStump(paramFallenTreeConfiguration, paramWorldGenLevel, paramRandomSource, paramBlockPos.mutable());
/*     */     
/*  41 */     Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/*  42 */     int i = paramFallenTreeConfiguration.logLength.sample(paramRandomSource) - 2;
/*  43 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.relative(direction, 2 + paramRandomSource.nextInt(2)).mutable();
/*  44 */     setGroundHeightForFallenLogStartPos(paramWorldGenLevel, mutableBlockPos);
/*  45 */     if (canPlaceEntireFallenLog(paramWorldGenLevel, i, mutableBlockPos, direction)) {
/*  46 */       placeFallenLog(paramFallenTreeConfiguration, paramWorldGenLevel, paramRandomSource, i, mutableBlockPos, direction);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setGroundHeightForFallenLogStartPos(WorldGenLevel paramWorldGenLevel, BlockPos.MutableBlockPos paramMutableBlockPos) {
/*  51 */     paramMutableBlockPos.move(Direction.UP, 1);
/*  52 */     for (byte b = 0; b < 6; b++) {
/*  53 */       if (mayPlaceOn((LevelAccessor)paramWorldGenLevel, (BlockPos)paramMutableBlockPos)) {
/*     */         return;
/*     */       }
/*  56 */       paramMutableBlockPos.move(Direction.DOWN);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeStump(FallenTreeConfiguration paramFallenTreeConfiguration, WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BlockPos.MutableBlockPos paramMutableBlockPos) {
/*  61 */     BlockPos blockPos = placeLogBlock(paramFallenTreeConfiguration, paramWorldGenLevel, paramRandomSource, paramMutableBlockPos, Function.identity());
/*  62 */     decorateLogs(paramWorldGenLevel, paramRandomSource, Set.of(blockPos), paramFallenTreeConfiguration.stumpDecorators);
/*     */   }
/*     */   
/*     */   private boolean canPlaceEntireFallenLog(WorldGenLevel paramWorldGenLevel, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos, Direction paramDirection) {
/*  66 */     byte b1 = 0;
/*  67 */     for (byte b2 = 0; b2 < paramInt; b2++) {
/*  68 */       if (!TreeFeature.validTreePos((LevelSimulatedReader)paramWorldGenLevel, (BlockPos)paramMutableBlockPos)) {
/*  69 */         return false;
/*     */       }
/*     */       
/*  72 */       if (!isOverSolidGround((LevelAccessor)paramWorldGenLevel, (BlockPos)paramMutableBlockPos)) {
/*  73 */         b1++;
/*  74 */         if (b1 > 2) {
/*  75 */           return false;
/*     */         }
/*     */       } else {
/*  78 */         b1 = 0;
/*     */       } 
/*     */       
/*  81 */       paramMutableBlockPos.move(paramDirection);
/*     */     } 
/*  83 */     paramMutableBlockPos.move(paramDirection.getOpposite(), paramInt);
/*  84 */     return true;
/*     */   }
/*     */   
/*     */   private void placeFallenLog(FallenTreeConfiguration paramFallenTreeConfiguration, WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos, Direction paramDirection) {
/*  88 */     HashSet<BlockPos> hashSet = new HashSet();
/*  89 */     for (byte b = 0; b < paramInt; b++) {
/*  90 */       hashSet.add(placeLogBlock(paramFallenTreeConfiguration, paramWorldGenLevel, paramRandomSource, paramMutableBlockPos, getSidewaysStateModifier(paramDirection)));
/*  91 */       paramMutableBlockPos.move(paramDirection);
/*     */     } 
/*     */     
/*  94 */     decorateLogs(paramWorldGenLevel, paramRandomSource, hashSet, paramFallenTreeConfiguration.logDecorators);
/*     */   }
/*     */   
/*     */   private boolean mayPlaceOn(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/*  98 */     return (TreeFeature.validTreePos((LevelSimulatedReader)paramLevelAccessor, paramBlockPos) && isOverSolidGround(paramLevelAccessor, paramBlockPos));
/*     */   }
/*     */   
/*     */   private boolean isOverSolidGround(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 102 */     return paramLevelAccessor.getBlockState(paramBlockPos.below()).isFaceSturdy((BlockGetter)paramLevelAccessor, paramBlockPos, Direction.UP);
/*     */   }
/*     */   
/*     */   private BlockPos placeLogBlock(FallenTreeConfiguration paramFallenTreeConfiguration, WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BlockPos.MutableBlockPos paramMutableBlockPos, Function<BlockState, BlockState> paramFunction) {
/* 106 */     paramWorldGenLevel.setBlock((BlockPos)paramMutableBlockPos, paramFunction.apply(paramFallenTreeConfiguration.trunkProvider.getState(paramRandomSource, (BlockPos)paramMutableBlockPos)), 3);
/* 107 */     markAboveForPostProcessing(paramWorldGenLevel, (BlockPos)paramMutableBlockPos);
/* 108 */     return paramMutableBlockPos.immutable();
/*     */   }
/*     */   
/*     */   private void decorateLogs(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, Set<BlockPos> paramSet, List<TreeDecorator> paramList) {
/* 112 */     if (!paramList.isEmpty()) {
/* 113 */       TreeDecorator.Context context = new TreeDecorator.Context((LevelSimulatedReader)paramWorldGenLevel, getDecorationSetter(paramWorldGenLevel), paramRandomSource, paramSet, Set.of(), Set.of());
/* 114 */       paramList.forEach(paramTreeDecorator -> paramTreeDecorator.place(paramContext));
/*     */     } 
/*     */   }
/*     */   
/*     */   private BiConsumer<BlockPos, BlockState> getDecorationSetter(WorldGenLevel paramWorldGenLevel) {
/* 119 */     return (paramBlockPos, paramBlockState) -> paramWorldGenLevel.setBlock(paramBlockPos, paramBlockState, 19);
/*     */   }
/*     */   
/*     */   private static Function<BlockState, BlockState> getSidewaysStateModifier(Direction paramDirection) {
/* 123 */     return paramBlockState -> (BlockState)paramBlockState.trySetValue((Property)RotatedPillarBlock.AXIS, (Comparable)paramDirection.getAxis());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\FallenTreeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */