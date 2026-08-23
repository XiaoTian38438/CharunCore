/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
/*     */ 
/*     */ public class BasaltColumnsFeature extends Feature<ColumnFeatureConfiguration> {
/*  17 */   private static final ImmutableList<Block> CANNOT_PLACE_ON = ImmutableList.of(Blocks.LAVA, Blocks.BEDROCK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);
/*     */ 
/*     */   
/*     */   private static final int CLUSTERED_REACH = 5;
/*     */ 
/*     */   
/*     */   private static final int CLUSTERED_SIZE = 50;
/*     */   
/*     */   private static final int UNCLUSTERED_REACH = 8;
/*     */   
/*     */   private static final int UNCLUSTERED_SIZE = 15;
/*     */ 
/*     */   
/*     */   public BasaltColumnsFeature(Codec<ColumnFeatureConfiguration> paramCodec) {
/*  31 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<ColumnFeatureConfiguration> paramFeaturePlaceContext) {
/*  36 */     int i = paramFeaturePlaceContext.chunkGenerator().getSeaLevel();
/*  37 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/*  38 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  39 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*  40 */     ColumnFeatureConfiguration columnFeatureConfiguration = paramFeaturePlaceContext.config();
/*  41 */     if (!canPlaceAt((LevelAccessor)worldGenLevel, i, blockPos.mutable())) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     int j = columnFeatureConfiguration.height().sample(randomSource);
/*     */     
/*  47 */     boolean bool = (randomSource.nextFloat() < 0.9F) ? true : false;
/*  48 */     int k = Math.min(j, bool ? 5 : 8);
/*  49 */     byte b = bool ? 50 : 15;
/*     */ 
/*     */     
/*  52 */     boolean bool1 = false;
/*  53 */     for (BlockPos blockPos1 : BlockPos.randomBetweenClosed(randomSource, b, blockPos.getX() - k, blockPos.getY(), blockPos.getZ() - k, blockPos.getX() + k, blockPos.getY(), blockPos.getZ() + k)) {
/*  54 */       int m = j - blockPos1.distManhattan((Vec3i)blockPos);
/*  55 */       if (m >= 0) {
/*  56 */         bool1 |= placeColumn((LevelAccessor)worldGenLevel, i, blockPos1, m, columnFeatureConfiguration.reach().sample(randomSource));
/*     */       }
/*     */     } 
/*     */     
/*  60 */     return bool1;
/*     */   }
/*     */   
/*     */   private boolean placeColumn(LevelAccessor paramLevelAccessor, int paramInt1, BlockPos paramBlockPos, int paramInt2, int paramInt3) {
/*  64 */     boolean bool = false;
/*     */     
/*  66 */     for (BlockPos blockPos1 : BlockPos.betweenClosed(paramBlockPos.getX() - paramInt3, paramBlockPos.getY(), paramBlockPos.getZ() - paramInt3, paramBlockPos.getX() + paramInt3, paramBlockPos.getY(), paramBlockPos.getZ() + paramInt3)) {
/*  67 */       int i = blockPos1.distManhattan((Vec3i)paramBlockPos);
/*     */ 
/*     */ 
/*     */       
/*  71 */       BlockPos blockPos2 = isAirOrLavaOcean(paramLevelAccessor, paramInt1, blockPos1) ? findSurface(paramLevelAccessor, paramInt1, blockPos1.mutable(), i) : findAir(paramLevelAccessor, blockPos1.mutable(), i);
/*  72 */       if (blockPos2 == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  76 */       int j = paramInt2 - i / 2;
/*  77 */       BlockPos.MutableBlockPos mutableBlockPos = blockPos2.mutable();
/*  78 */       while (j >= 0) {
/*  79 */         if (isAirOrLavaOcean(paramLevelAccessor, paramInt1, (BlockPos)mutableBlockPos)) {
/*  80 */           setBlock((LevelWriter)paramLevelAccessor, (BlockPos)mutableBlockPos, Blocks.BASALT.defaultBlockState());
/*  81 */           mutableBlockPos.move(Direction.UP);
/*  82 */           bool = true;
/*  83 */         } else if (paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos).is(Blocks.BASALT)) {
/*  84 */           mutableBlockPos.move(Direction.UP);
/*     */         } else {
/*     */           break;
/*     */         } 
/*     */         
/*  89 */         j--;
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     return bool;
/*     */   }
/*     */   
/*     */   private static BlockPos findSurface(LevelAccessor paramLevelAccessor, int paramInt1, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt2) {
/*  97 */     while (paramMutableBlockPos.getY() > paramLevelAccessor.getMinY() + 1 && paramInt2 > 0) {
/*  98 */       paramInt2--;
/*  99 */       if (canPlaceAt(paramLevelAccessor, paramInt1, paramMutableBlockPos)) {
/* 100 */         return (BlockPos)paramMutableBlockPos;
/*     */       }
/* 102 */       paramMutableBlockPos.move(Direction.DOWN);
/*     */     } 
/* 104 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean canPlaceAt(LevelAccessor paramLevelAccessor, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 108 */     if (isAirOrLavaOcean(paramLevelAccessor, paramInt, (BlockPos)paramMutableBlockPos)) {
/* 109 */       BlockState blockState = paramLevelAccessor.getBlockState((BlockPos)paramMutableBlockPos.move(Direction.DOWN));
/* 110 */       paramMutableBlockPos.move(Direction.UP);
/* 111 */       return (!blockState.isAir() && !CANNOT_PLACE_ON.contains(blockState.getBlock()));
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */   
/*     */   private static BlockPos findAir(LevelAccessor paramLevelAccessor, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt) {
/* 117 */     while (paramMutableBlockPos.getY() <= paramLevelAccessor.getMaxY() && paramInt > 0) {
/* 118 */       paramInt--;
/*     */       
/* 120 */       BlockState blockState = paramLevelAccessor.getBlockState((BlockPos)paramMutableBlockPos);
/* 121 */       if (CANNOT_PLACE_ON.contains(blockState.getBlock())) {
/* 122 */         return null;
/*     */       }
/*     */       
/* 125 */       if (blockState.isAir()) {
/* 126 */         return (BlockPos)paramMutableBlockPos;
/*     */       }
/*     */       
/* 129 */       paramMutableBlockPos.move(Direction.UP);
/*     */     } 
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean isAirOrLavaOcean(LevelAccessor paramLevelAccessor, int paramInt, BlockPos paramBlockPos) {
/* 135 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos);
/* 136 */     return (blockState.isAir() || (blockState.is(Blocks.LAVA) && paramBlockPos.getY() <= paramInt));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\BasaltColumnsFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */