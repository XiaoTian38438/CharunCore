/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.ClampedNormalFloat;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Column;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DripstoneClusterFeature
/*     */   extends Feature<DripstoneClusterConfiguration>
/*     */ {
/*     */   public DripstoneClusterFeature(Codec<DripstoneClusterConfiguration> paramCodec) {
/*  30 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<DripstoneClusterConfiguration> paramFeaturePlaceContext) {
/*  35 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  36 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/*  37 */     DripstoneClusterConfiguration dripstoneClusterConfiguration = paramFeaturePlaceContext.config();
/*  38 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*     */     
/*  40 */     if (!DripstoneUtils.isEmptyOrWater((LevelAccessor)worldGenLevel, blockPos)) {
/*  41 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  45 */     int i = dripstoneClusterConfiguration.height.sample(randomSource);
/*     */     
/*  47 */     float f1 = dripstoneClusterConfiguration.wetness.sample(randomSource);
/*  48 */     float f2 = dripstoneClusterConfiguration.density.sample(randomSource);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     int j = dripstoneClusterConfiguration.radius.sample(randomSource);
/*  54 */     int k = dripstoneClusterConfiguration.radius.sample(randomSource);
/*  55 */     for (int m = -j; m <= j; m++) {
/*  56 */       for (int n = -k; n <= k; n++) {
/*  57 */         double d = getChanceOfStalagmiteOrStalactite(j, k, m, n, dripstoneClusterConfiguration);
/*  58 */         BlockPos blockPos1 = blockPos.offset(m, 0, n);
/*  59 */         placeColumn(worldGenLevel, randomSource, blockPos1, m, n, f1, d, i, f2, dripstoneClusterConfiguration);
/*     */       } 
/*     */     } 
/*  62 */     return true;
/*     */   }
/*     */   private void placeColumn(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, int paramInt1, int paramInt2, float paramFloat1, double paramDouble, int paramInt3, float paramFloat2, DripstoneClusterConfiguration paramDripstoneClusterConfiguration) {
/*     */     Column column;
/*     */     byte b1, b2, b3, b4;
/*  67 */     Optional<Column> optional = Column.scan((LevelSimulatedReader)paramWorldGenLevel, paramBlockPos, paramDripstoneClusterConfiguration.floorToCeilingSearchRange, DripstoneUtils::isEmptyOrWater, DripstoneUtils::isNeitherEmptyNorWater);
/*  68 */     if (optional.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     OptionalInt optionalInt1 = ((Column)optional.get()).getCeiling();
/*  73 */     OptionalInt optionalInt2 = ((Column)optional.get()).getFloor();
/*     */     
/*  75 */     if (optionalInt1.isEmpty() && optionalInt2.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  81 */     boolean bool1 = (paramRandomSource.nextFloat() < paramFloat1) ? true : false;
/*     */     
/*  83 */     if (bool1 && optionalInt2.isPresent() && canPlacePool(paramWorldGenLevel, paramBlockPos.atY(optionalInt2.getAsInt()))) {
/*     */       
/*  85 */       int i = optionalInt2.getAsInt();
/*  86 */       column = ((Column)optional.get()).withFloor(OptionalInt.of(i - 1));
/*  87 */       paramWorldGenLevel.setBlock(paramBlockPos.atY(i), Blocks.WATER.defaultBlockState(), 2);
/*     */     } else {
/*  89 */       column = optional.get();
/*     */     } 
/*     */     
/*  92 */     OptionalInt optionalInt3 = column.getFloor();
/*     */ 
/*     */ 
/*     */     
/*  96 */     boolean bool2 = (paramRandomSource.nextDouble() < paramDouble) ? true : false;
/*  97 */     if (optionalInt1.isPresent() && bool2 && !isLava((LevelReader)paramWorldGenLevel, paramBlockPos.atY(optionalInt1.getAsInt()))) {
/*  98 */       int i; b2 = paramDripstoneClusterConfiguration.dripstoneBlockLayerThickness.sample(paramRandomSource);
/*  99 */       replaceBlocksWithDripstoneBlocks(paramWorldGenLevel, paramBlockPos.atY(optionalInt1.getAsInt()), b2, Direction.UP);
/*     */       
/* 101 */       if (optionalInt3.isPresent()) {
/* 102 */         i = Math.min(paramInt3, optionalInt1.getAsInt() - optionalInt3.getAsInt());
/*     */       } else {
/* 104 */         i = paramInt3;
/*     */       } 
/* 106 */       b1 = getDripstoneHeight(paramRandomSource, paramInt1, paramInt2, paramFloat2, i, paramDripstoneClusterConfiguration);
/*     */     } else {
/* 108 */       b1 = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 113 */     boolean bool3 = (paramRandomSource.nextDouble() < paramDouble) ? true : false;
/* 114 */     if (optionalInt3.isPresent() && bool3 && !isLava((LevelReader)paramWorldGenLevel, paramBlockPos.atY(optionalInt3.getAsInt()))) {
/* 115 */       b3 = paramDripstoneClusterConfiguration.dripstoneBlockLayerThickness.sample(paramRandomSource);
/* 116 */       replaceBlocksWithDripstoneBlocks(paramWorldGenLevel, paramBlockPos.atY(optionalInt3.getAsInt()), b3, Direction.DOWN);
/*     */       
/* 118 */       if (optionalInt1.isPresent()) {
/* 119 */         b2 = Math.max(0, b1 + Mth.randomBetweenInclusive(paramRandomSource, -paramDripstoneClusterConfiguration.maxStalagmiteStalactiteHeightDiff, paramDripstoneClusterConfiguration.maxStalagmiteStalactiteHeightDiff));
/*     */       } else {
/*     */         
/* 122 */         b2 = getDripstoneHeight(paramRandomSource, paramInt1, paramInt2, paramFloat2, paramInt3, paramDripstoneClusterConfiguration);
/*     */       } 
/*     */     } else {
/* 125 */       b2 = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     if (optionalInt1.isPresent() && optionalInt3.isPresent() && optionalInt1.getAsInt() - b1 <= optionalInt3.getAsInt() + b2) {
/*     */ 
/*     */       
/* 134 */       int i = optionalInt3.getAsInt();
/* 135 */       int j = optionalInt1.getAsInt();
/* 136 */       int k = Math.max(j - b1, i + 1);
/* 137 */       int m = Math.min(i + b2, j - 1);
/* 138 */       int n = Mth.randomBetweenInclusive(paramRandomSource, k, m + 1);
/* 139 */       int i1 = n - 1;
/* 140 */       b3 = j - n;
/* 141 */       b4 = i1 - i;
/*     */     } else {
/* 143 */       b3 = b1;
/* 144 */       b4 = b2;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     boolean bool4 = (paramRandomSource.nextBoolean() && b3 > 0 && b4 > 0 && column.getHeight().isPresent() && b3 + b4 == column.getHeight().getAsInt()) ? true : false;
/*     */     
/* 153 */     if (optionalInt1.isPresent()) {
/* 154 */       DripstoneUtils.growPointedDripstone((LevelAccessor)paramWorldGenLevel, paramBlockPos.atY(optionalInt1.getAsInt() - 1), Direction.DOWN, b3, bool4);
/*     */     }
/* 156 */     if (optionalInt3.isPresent()) {
/* 157 */       DripstoneUtils.growPointedDripstone((LevelAccessor)paramWorldGenLevel, paramBlockPos.atY(optionalInt3.getAsInt() + 1), Direction.UP, b4, bool4);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isLava(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 162 */     return paramLevelReader.getBlockState(paramBlockPos).is(Blocks.LAVA);
/*     */   }
/*     */   
/*     */   private int getDripstoneHeight(RandomSource paramRandomSource, int paramInt1, int paramInt2, float paramFloat, int paramInt3, DripstoneClusterConfiguration paramDripstoneClusterConfiguration) {
/* 166 */     if (paramRandomSource.nextFloat() > paramFloat) {
/* 167 */       return 0;
/*     */     }
/*     */     
/* 170 */     int i = Math.abs(paramInt1) + Math.abs(paramInt2);
/*     */ 
/*     */     
/* 173 */     float f = (float)Mth.clampedMap(i, 0.0D, paramDripstoneClusterConfiguration.maxDistanceFromCenterAffectingHeightBias, paramInt3 / 2.0D, 0.0D);
/* 174 */     return (int)randomBetweenBiased(paramRandomSource, 0.0F, paramInt3, f, paramDripstoneClusterConfiguration.heightDeviation);
/*     */   }
/*     */   
/*     */   private boolean canPlacePool(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos) {
/* 178 */     BlockState blockState = paramWorldGenLevel.getBlockState(paramBlockPos);
/* 179 */     if (blockState.is(Blocks.WATER) || blockState.is(Blocks.DRIPSTONE_BLOCK) || blockState.is(Blocks.POINTED_DRIPSTONE)) {
/* 180 */       return false;
/*     */     }
/* 182 */     if (paramWorldGenLevel.getBlockState(paramBlockPos.above()).getFluidState().is(FluidTags.WATER)) {
/* 183 */       return false;
/*     */     }
/*     */     
/* 186 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 187 */       if (!canBeAdjacentToWater((LevelAccessor)paramWorldGenLevel, paramBlockPos.relative(direction))) {
/* 188 */         return false;
/*     */       }
/*     */     } 
/* 191 */     return canBeAdjacentToWater((LevelAccessor)paramWorldGenLevel, paramBlockPos.below());
/*     */   }
/*     */   
/*     */   private boolean canBeAdjacentToWater(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 195 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos);
/* 196 */     return (blockState.is(BlockTags.BASE_STONE_OVERWORLD) || blockState.getFluidState().is(FluidTags.WATER));
/*     */   }
/*     */   
/*     */   private void replaceBlocksWithDripstoneBlocks(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos, int paramInt, Direction paramDirection) {
/* 200 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 201 */     for (byte b = 0; b < paramInt; b++) {
/* 202 */       if (!DripstoneUtils.placeDripstoneBlockIfPossible((LevelAccessor)paramWorldGenLevel, (BlockPos)mutableBlockPos)) {
/*     */         return;
/*     */       }
/* 205 */       mutableBlockPos.move(paramDirection);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double getChanceOfStalagmiteOrStalactite(int paramInt1, int paramInt2, int paramInt3, int paramInt4, DripstoneClusterConfiguration paramDripstoneClusterConfiguration) {
/* 213 */     int i = paramInt1 - Math.abs(paramInt3);
/* 214 */     int j = paramInt2 - Math.abs(paramInt4);
/* 215 */     int k = Math.min(i, j);
/*     */     
/* 217 */     return Mth.clampedMap(k, 0.0F, paramDripstoneClusterConfiguration.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn, paramDripstoneClusterConfiguration.chanceOfDripstoneColumnAtMaxDistanceFromCenter, 1.0F);
/*     */   }
/*     */   
/*     */   private static float randomBetweenBiased(RandomSource paramRandomSource, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 221 */     return ClampedNormalFloat.sample(paramRandomSource, paramFloat3, paramFloat4, paramFloat1, paramFloat2);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\DripstoneClusterFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */