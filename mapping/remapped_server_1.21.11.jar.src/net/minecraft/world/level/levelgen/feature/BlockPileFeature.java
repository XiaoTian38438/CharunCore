/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
/*    */ 
/*    */ public class BlockPileFeature extends Feature<BlockPileConfiguration> {
/*    */   public BlockPileFeature(Codec<BlockPileConfiguration> paramCodec) {
/* 16 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<BlockPileConfiguration> paramFeaturePlaceContext) {
/* 21 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 22 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 23 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 24 */     BlockPileConfiguration blockPileConfiguration = paramFeaturePlaceContext.config();
/* 25 */     if (blockPos.getY() < worldGenLevel.getMinY() + 5) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     int i = 2 + randomSource.nextInt(2);
/* 30 */     int j = 2 + randomSource.nextInt(2);
/*    */     
/* 32 */     for (BlockPos blockPos1 : BlockPos.betweenClosed(blockPos.offset(-i, 0, -j), blockPos.offset(i, 1, j))) {
/* 33 */       int k = blockPos.getX() - blockPos1.getX();
/* 34 */       int m = blockPos.getZ() - blockPos1.getZ();
/* 35 */       if ((k * k + m * m) <= randomSource.nextFloat() * 10.0F - randomSource.nextFloat() * 6.0F) {
/* 36 */         tryPlaceBlock((LevelAccessor)worldGenLevel, blockPos1, randomSource, blockPileConfiguration); continue;
/* 37 */       }  if (randomSource.nextFloat() < 0.031D) {
/* 38 */         tryPlaceBlock((LevelAccessor)worldGenLevel, blockPos1, randomSource, blockPileConfiguration);
/*    */       }
/*    */     } 
/*    */     
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   private boolean mayPlaceOn(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 46 */     BlockPos blockPos = paramBlockPos.below();
/* 47 */     BlockState blockState = paramLevelAccessor.getBlockState(blockPos);
/* 48 */     if (blockState.is(Blocks.DIRT_PATH)) {
/* 49 */       return paramRandomSource.nextBoolean();
/*    */     }
/*    */     
/* 52 */     return blockState.isFaceSturdy((BlockGetter)paramLevelAccessor, blockPos, Direction.UP);
/*    */   }
/*    */   
/*    */   private void tryPlaceBlock(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource, BlockPileConfiguration paramBlockPileConfiguration) {
/* 56 */     if (paramLevelAccessor.isEmptyBlock(paramBlockPos) && mayPlaceOn(paramLevelAccessor, paramBlockPos, paramRandomSource))
/* 57 */       paramLevelAccessor.setBlock(paramBlockPos, paramBlockPileConfiguration.stateProvider.getState(paramRandomSource, paramBlockPos), 260); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\BlockPileFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */