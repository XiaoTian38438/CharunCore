/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RootSystemConfiguration;
/*     */ 
/*     */ public class RootSystemFeature extends Feature<RootSystemConfiguration> {
/*     */   public RootSystemFeature(Codec<RootSystemConfiguration> paramCodec) {
/*  18 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<RootSystemConfiguration> paramFeaturePlaceContext) {
/*  23 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  24 */     BlockPos blockPos1 = paramFeaturePlaceContext.origin();
/*  25 */     if (!worldGenLevel.getBlockState(blockPos1).isAir()) {
/*  26 */       return false;
/*     */     }
/*     */     
/*  29 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*  30 */     BlockPos blockPos2 = paramFeaturePlaceContext.origin();
/*  31 */     RootSystemConfiguration rootSystemConfiguration = paramFeaturePlaceContext.config();
/*  32 */     BlockPos.MutableBlockPos mutableBlockPos = blockPos2.mutable();
/*  33 */     if (placeDirtAndTree(worldGenLevel, paramFeaturePlaceContext.chunkGenerator(), rootSystemConfiguration, randomSource, mutableBlockPos, blockPos2)) {
/*  34 */       placeRoots(worldGenLevel, rootSystemConfiguration, randomSource, blockPos2, mutableBlockPos);
/*     */     }
/*     */     
/*  37 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean spaceForTree(WorldGenLevel paramWorldGenLevel, RootSystemConfiguration paramRootSystemConfiguration, BlockPos paramBlockPos) {
/*  41 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*  42 */     for (byte b = 1; b <= paramRootSystemConfiguration.requiredVerticalSpaceForTree; b++) {
/*  43 */       mutableBlockPos.move(Direction.UP);
/*  44 */       BlockState blockState = paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos);
/*  45 */       if (!isAllowedTreeSpace(blockState, b, paramRootSystemConfiguration.allowedVerticalWaterForTree)) {
/*  46 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  50 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isAllowedTreeSpace(BlockState paramBlockState, int paramInt1, int paramInt2) {
/*  54 */     if (paramBlockState.isAir()) {
/*  55 */       return true;
/*     */     }
/*  57 */     int i = paramInt1 + 1;
/*  58 */     return (i <= paramInt2 && paramBlockState.getFluidState().is(FluidTags.WATER));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean placeDirtAndTree(WorldGenLevel paramWorldGenLevel, ChunkGenerator paramChunkGenerator, RootSystemConfiguration paramRootSystemConfiguration, RandomSource paramRandomSource, BlockPos.MutableBlockPos paramMutableBlockPos, BlockPos paramBlockPos) {
/*  65 */     for (byte b = 0; b < paramRootSystemConfiguration.rootColumnMaxHeight; b++) {
/*  66 */       paramMutableBlockPos.move(Direction.UP);
/*     */       
/*  68 */       if (paramRootSystemConfiguration.allowedTreePosition.test(paramWorldGenLevel, paramMutableBlockPos) && 
/*  69 */         spaceForTree(paramWorldGenLevel, paramRootSystemConfiguration, (BlockPos)paramMutableBlockPos)) {
/*  70 */         BlockPos blockPos = paramMutableBlockPos.below();
/*  71 */         if (paramWorldGenLevel.getFluidState(blockPos).is(FluidTags.LAVA) || !paramWorldGenLevel.getBlockState(blockPos).isSolid()) {
/*  72 */           return false;
/*     */         }
/*     */         
/*  75 */         if (((PlacedFeature)paramRootSystemConfiguration.treeFeature.value()).place(paramWorldGenLevel, paramChunkGenerator, paramRandomSource, (BlockPos)paramMutableBlockPos)) {
/*  76 */           placeDirt(paramBlockPos, paramBlockPos.getY() + b, paramWorldGenLevel, paramRootSystemConfiguration, paramRandomSource);
/*  77 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   private static void placeDirt(BlockPos paramBlockPos, int paramInt, WorldGenLevel paramWorldGenLevel, RootSystemConfiguration paramRootSystemConfiguration, RandomSource paramRandomSource) {
/*  86 */     int i = paramBlockPos.getX();
/*  87 */     int j = paramBlockPos.getZ();
/*  88 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*  89 */     for (int k = paramBlockPos.getY(); k < paramInt; k++) {
/*  90 */       placeRootedDirt(paramWorldGenLevel, paramRootSystemConfiguration, paramRandomSource, i, j, mutableBlockPos.set(i, k, j));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void placeRootedDirt(WorldGenLevel paramWorldGenLevel, RootSystemConfiguration paramRootSystemConfiguration, RandomSource paramRandomSource, int paramInt1, int paramInt2, BlockPos.MutableBlockPos paramMutableBlockPos) {
/*  95 */     int i = paramRootSystemConfiguration.rootRadius;
/*  96 */     Predicate<BlockState> predicate = paramBlockState -> paramBlockState.is(paramRootSystemConfiguration.rootReplaceable);
/*  97 */     for (byte b = 0; b < paramRootSystemConfiguration.rootPlacementAttempts; b++) {
/*  98 */       paramMutableBlockPos.setWithOffset((Vec3i)paramMutableBlockPos, paramRandomSource.nextInt(i) - paramRandomSource.nextInt(i), 0, paramRandomSource.nextInt(i) - paramRandomSource.nextInt(i));
/*  99 */       if (predicate.test(paramWorldGenLevel.getBlockState((BlockPos)paramMutableBlockPos))) {
/* 100 */         paramWorldGenLevel.setBlock((BlockPos)paramMutableBlockPos, paramRootSystemConfiguration.rootStateProvider.getState(paramRandomSource, (BlockPos)paramMutableBlockPos), 2);
/*     */       }
/*     */       
/* 103 */       paramMutableBlockPos.setX(paramInt1);
/* 104 */       paramMutableBlockPos.setZ(paramInt2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void placeRoots(WorldGenLevel paramWorldGenLevel, RootSystemConfiguration paramRootSystemConfiguration, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 112 */     int i = paramRootSystemConfiguration.hangingRootRadius;
/* 113 */     int j = paramRootSystemConfiguration.hangingRootsVerticalSpan;
/* 114 */     for (byte b = 0; b < paramRootSystemConfiguration.hangingRootPlacementAttempts; b++) {
/* 115 */       paramMutableBlockPos.setWithOffset((Vec3i)paramBlockPos, paramRandomSource.nextInt(i) - paramRandomSource.nextInt(i), paramRandomSource.nextInt(j) - paramRandomSource.nextInt(j), paramRandomSource.nextInt(i) - paramRandomSource.nextInt(i));
/* 116 */       if (paramWorldGenLevel.isEmptyBlock((BlockPos)paramMutableBlockPos)) {
/* 117 */         BlockState blockState = paramRootSystemConfiguration.hangingRootStateProvider.getState(paramRandomSource, (BlockPos)paramMutableBlockPos);
/* 118 */         if (blockState.canSurvive((LevelReader)paramWorldGenLevel, (BlockPos)paramMutableBlockPos) && paramWorldGenLevel.getBlockState(paramMutableBlockPos.above()).isFaceSturdy((BlockGetter)paramWorldGenLevel, (BlockPos)paramMutableBlockPos, Direction.DOWN))
/* 119 */           paramWorldGenLevel.setBlock((BlockPos)paramMutableBlockPos, blockState, 2); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\RootSystemFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */