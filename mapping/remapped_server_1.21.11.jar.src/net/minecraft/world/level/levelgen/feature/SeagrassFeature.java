/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.TallSeagrassBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ 
/*    */ public class SeagrassFeature extends Feature<ProbabilityFeatureConfiguration> {
/*    */   public SeagrassFeature(Codec<ProbabilityFeatureConfiguration> paramCodec) {
/* 17 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> paramFeaturePlaceContext) {
/* 22 */     boolean bool = false;
/* 23 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 24 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 25 */     BlockPos blockPos1 = paramFeaturePlaceContext.origin();
/* 26 */     ProbabilityFeatureConfiguration probabilityFeatureConfiguration = paramFeaturePlaceContext.config();
/* 27 */     int i = randomSource.nextInt(8) - randomSource.nextInt(8);
/* 28 */     int j = randomSource.nextInt(8) - randomSource.nextInt(8);
/* 29 */     int k = worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR, blockPos1.getX() + i, blockPos1.getZ() + j);
/* 30 */     BlockPos blockPos2 = new BlockPos(blockPos1.getX() + i, k, blockPos1.getZ() + j);
/*    */     
/* 32 */     if (worldGenLevel.getBlockState(blockPos2).is(Blocks.WATER)) {
/* 33 */       boolean bool1 = (randomSource.nextDouble() < probabilityFeatureConfiguration.probability) ? true : false;
/* 34 */       BlockState blockState = bool1 ? Blocks.TALL_SEAGRASS.defaultBlockState() : Blocks.SEAGRASS.defaultBlockState();
/* 35 */       if (blockState.canSurvive((LevelReader)worldGenLevel, blockPos2)) {
/* 36 */         if (bool1) {
/* 37 */           BlockState blockState1 = (BlockState)blockState.setValue((Property)TallSeagrassBlock.HALF, (Comparable)DoubleBlockHalf.UPPER);
/* 38 */           BlockPos blockPos = blockPos2.above();
/* 39 */           if (worldGenLevel.getBlockState(blockPos).is(Blocks.WATER)) {
/* 40 */             worldGenLevel.setBlock(blockPos2, blockState, 2);
/* 41 */             worldGenLevel.setBlock(blockPos, blockState1, 2);
/*    */           } 
/*    */         } else {
/* 44 */           worldGenLevel.setBlock(blockPos2, blockState, 2);
/*    */         } 
/* 46 */         bool = true;
/*    */       } 
/*    */     } 
/* 49 */     return bool;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\SeagrassFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */