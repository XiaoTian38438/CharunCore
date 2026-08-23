/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
/*    */ 
/*    */ public class NetherForestVegetationFeature extends Feature<NetherForestVegetationConfig> {
/*    */   public NetherForestVegetationFeature(Codec<NetherForestVegetationConfig> paramCodec) {
/* 14 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NetherForestVegetationConfig> paramFeaturePlaceContext) {
/* 19 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 20 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 21 */     BlockState blockState = worldGenLevel.getBlockState(blockPos.below());
/* 22 */     NetherForestVegetationConfig netherForestVegetationConfig = paramFeaturePlaceContext.config();
/* 23 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*    */     
/* 25 */     if (!blockState.is(BlockTags.NYLIUM)) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     int i = blockPos.getY();
/*    */     
/* 31 */     if (i < worldGenLevel.getMinY() + 1 || i + 1 > worldGenLevel.getMaxY()) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     byte b1 = 0;
/*    */ 
/*    */     
/* 38 */     for (byte b2 = 0; b2 < netherForestVegetationConfig.spreadWidth * netherForestVegetationConfig.spreadWidth; b2++) {
/* 39 */       BlockPos blockPos1 = blockPos.offset(randomSource.nextInt(netherForestVegetationConfig.spreadWidth) - randomSource.nextInt(netherForestVegetationConfig.spreadWidth), randomSource.nextInt(netherForestVegetationConfig.spreadHeight) - randomSource.nextInt(netherForestVegetationConfig.spreadHeight), randomSource.nextInt(netherForestVegetationConfig.spreadWidth) - randomSource.nextInt(netherForestVegetationConfig.spreadWidth));
/* 40 */       BlockState blockState1 = netherForestVegetationConfig.stateProvider.getState(randomSource, blockPos1);
/* 41 */       if (worldGenLevel.isEmptyBlock(blockPos1) && blockPos1.getY() > worldGenLevel.getMinY() && 
/* 42 */         blockState1.canSurvive((LevelReader)worldGenLevel, blockPos1)) {
/* 43 */         worldGenLevel.setBlock(blockPos1, blockState1, 2);
/* 44 */         b1++;
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 49 */     return (b1 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\NetherForestVegetationFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */