/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
/*    */ 
/*    */ public class MultifaceGrowthFeature extends Feature<MultifaceGrowthConfiguration> {
/*    */   public MultifaceGrowthFeature(Codec<MultifaceGrowthConfiguration> paramCodec) {
/* 17 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<MultifaceGrowthConfiguration> paramFeaturePlaceContext) {
/* 22 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 23 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 24 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 25 */     MultifaceGrowthConfiguration multifaceGrowthConfiguration = paramFeaturePlaceContext.config();
/* 26 */     if (!isAirOrWater(worldGenLevel.getBlockState(blockPos))) {
/* 27 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 31 */     List<Direction> list = multifaceGrowthConfiguration.getShuffledDirections(randomSource);
/* 32 */     if (placeGrowthIfPossible(worldGenLevel, blockPos, worldGenLevel.getBlockState(blockPos), multifaceGrowthConfiguration, randomSource, list)) {
/* 33 */       return true;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 38 */     BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
/* 39 */     for (Direction direction : list) {
/* 40 */       mutableBlockPos.set((Vec3i)blockPos);
/* 41 */       List<Direction> list1 = multifaceGrowthConfiguration.getShuffledDirectionsExcept(randomSource, direction.getOpposite());
/* 42 */       for (byte b = 0; b < multifaceGrowthConfiguration.searchRange; b++) {
/* 43 */         mutableBlockPos.setWithOffset((Vec3i)blockPos, direction);
/* 44 */         BlockState blockState = worldGenLevel.getBlockState((BlockPos)mutableBlockPos);
/* 45 */         if (!isAirOrWater(blockState) && !blockState.is((Block)multifaceGrowthConfiguration.placeBlock)) {
/*    */           break;
/*    */         }
/*    */         
/* 49 */         if (placeGrowthIfPossible(worldGenLevel, (BlockPos)mutableBlockPos, blockState, multifaceGrowthConfiguration, randomSource, list1)) {
/* 50 */           return true;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 55 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean placeGrowthIfPossible(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos, BlockState paramBlockState, MultifaceGrowthConfiguration paramMultifaceGrowthConfiguration, RandomSource paramRandomSource, List<Direction> paramList) {
/* 59 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 60 */     for (Direction direction : paramList) {
/* 61 */       BlockState blockState = paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, direction));
/* 62 */       if (blockState.is(paramMultifaceGrowthConfiguration.canBePlacedOn)) {
/* 63 */         BlockState blockState1 = paramMultifaceGrowthConfiguration.placeBlock.getStateForPlacement(paramBlockState, (BlockGetter)paramWorldGenLevel, paramBlockPos, direction);
/* 64 */         if (blockState1 == null) {
/* 65 */           return false;
/*    */         }
/* 67 */         paramWorldGenLevel.setBlock(paramBlockPos, blockState1, 3);
/* 68 */         paramWorldGenLevel.getChunk(paramBlockPos).markPosForPostprocessing(paramBlockPos);
/* 69 */         if (paramRandomSource.nextFloat() < paramMultifaceGrowthConfiguration.chanceOfSpreading) {
/* 70 */           paramMultifaceGrowthConfiguration.placeBlock.getSpreader().spreadFromFaceTowardRandomDirection(blockState1, (LevelAccessor)paramWorldGenLevel, paramBlockPos, direction, paramRandomSource, true);
/*    */         }
/* 72 */         return true;
/*    */       } 
/*    */     } 
/* 75 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean isAirOrWater(BlockState paramBlockState) {
/* 79 */     return (paramBlockState.isAir() || paramBlockState.is(Blocks.WATER));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\MultifaceGrowthFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */