/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*    */ 
/*    */ public abstract class AbstractHugeMushroomFeature extends Feature<HugeMushroomFeatureConfiguration> {
/*    */   public AbstractHugeMushroomFeature(Codec<HugeMushroomFeatureConfiguration> paramCodec) {
/* 15 */     super(paramCodec);
/*    */   }
/*    */   
/*    */   protected void placeTrunk(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, HugeMushroomFeatureConfiguration paramHugeMushroomFeatureConfiguration, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 19 */     for (byte b = 0; b < paramInt; b++) {
/* 20 */       paramMutableBlockPos.set((Vec3i)paramBlockPos).move(Direction.UP, b);
/* 21 */       placeMushroomBlock(paramLevelAccessor, paramMutableBlockPos, paramHugeMushroomFeatureConfiguration.stemProvider.getState(paramRandomSource, paramBlockPos));
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void placeMushroomBlock(LevelAccessor paramLevelAccessor, BlockPos.MutableBlockPos paramMutableBlockPos, BlockState paramBlockState) {
/* 26 */     BlockState blockState = paramLevelAccessor.getBlockState((BlockPos)paramMutableBlockPos);
/* 27 */     if (blockState.isAir() || blockState.is(BlockTags.REPLACEABLE_BY_MUSHROOMS)) {
/* 28 */       setBlock((LevelWriter)paramLevelAccessor, (BlockPos)paramMutableBlockPos, paramBlockState);
/*    */     }
/*    */   }
/*    */   
/*    */   protected int getTreeHeight(RandomSource paramRandomSource) {
/* 33 */     int i = paramRandomSource.nextInt(3) + 4;
/* 34 */     if (paramRandomSource.nextInt(12) == 0) {
/* 35 */       i *= 2;
/*    */     }
/* 37 */     return i;
/*    */   }
/*    */   
/*    */   protected boolean isValidPosition(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos, HugeMushroomFeatureConfiguration paramHugeMushroomFeatureConfiguration) {
/* 41 */     int i = paramBlockPos.getY();
/* 42 */     if (i < paramLevelAccessor.getMinY() + 1 || i + paramInt + 1 > paramLevelAccessor.getMaxY()) {
/* 43 */       return false;
/*    */     }
/*    */     
/* 46 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos.below());
/* 47 */     if (!isDirt(blockState) && !blockState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     for (byte b = 0; b <= paramInt; b++) {
/* 52 */       int j = getTreeRadiusForHeight(-1, -1, paramHugeMushroomFeatureConfiguration.foliageRadius, b);
/* 53 */       for (int k = -j; k <= j; k++) {
/* 54 */         for (int m = -j; m <= j; m++) {
/* 55 */           BlockState blockState1 = paramLevelAccessor.getBlockState((BlockPos)paramMutableBlockPos.setWithOffset((Vec3i)paramBlockPos, k, b, m));
/* 56 */           if (!blockState1.isAir() && !blockState1.is(BlockTags.LEAVES)) {
/* 57 */             return false;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<HugeMushroomFeatureConfiguration> paramFeaturePlaceContext) {
/* 67 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 68 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 69 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 70 */     HugeMushroomFeatureConfiguration hugeMushroomFeatureConfiguration = paramFeaturePlaceContext.config();
/* 71 */     int i = getTreeHeight(randomSource);
/*    */     
/* 73 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 74 */     if (!isValidPosition((LevelAccessor)worldGenLevel, blockPos, i, mutableBlockPos, hugeMushroomFeatureConfiguration)) {
/* 75 */       return false;
/*    */     }
/*    */     
/* 78 */     makeCap((LevelAccessor)worldGenLevel, randomSource, blockPos, i, mutableBlockPos, hugeMushroomFeatureConfiguration);
/* 79 */     placeTrunk((LevelAccessor)worldGenLevel, randomSource, blockPos, hugeMushroomFeatureConfiguration, i, mutableBlockPos);
/* 80 */     return true;
/*    */   }
/*    */   
/*    */   protected abstract int getTreeRadiusForHeight(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */   
/*    */   protected abstract void makeCap(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos, HugeMushroomFeatureConfiguration paramHugeMushroomFeatureConfiguration);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\AbstractHugeMushroomFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */