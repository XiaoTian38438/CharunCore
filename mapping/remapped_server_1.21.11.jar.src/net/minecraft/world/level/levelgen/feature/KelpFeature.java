/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.KelpBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class KelpFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public KelpFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 16 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 21 */     byte b = 0;
/* 22 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 23 */     BlockPos blockPos1 = paramFeaturePlaceContext.origin();
/* 24 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 25 */     int i = worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR, blockPos1.getX(), blockPos1.getZ());
/* 26 */     BlockPos blockPos2 = new BlockPos(blockPos1.getX(), i, blockPos1.getZ());
/*    */     
/* 28 */     if (worldGenLevel.getBlockState(blockPos2).is(Blocks.WATER)) {
/* 29 */       BlockState blockState1 = Blocks.KELP.defaultBlockState();
/* 30 */       BlockState blockState2 = Blocks.KELP_PLANT.defaultBlockState();
/* 31 */       int j = 1 + randomSource.nextInt(10);
/* 32 */       for (byte b1 = 0; b1 <= j; b1++) {
/* 33 */         if (worldGenLevel.getBlockState(blockPos2).is(Blocks.WATER) && worldGenLevel.getBlockState(blockPos2.above()).is(Blocks.WATER) && blockState2.canSurvive((LevelReader)worldGenLevel, blockPos2)) {
/* 34 */           if (b1 == j) {
/* 35 */             worldGenLevel.setBlock(blockPos2, (BlockState)blockState1.setValue((Property)KelpBlock.AGE, Integer.valueOf(randomSource.nextInt(4) + 20)), 2);
/* 36 */             b++;
/*    */           } else {
/* 38 */             worldGenLevel.setBlock(blockPos2, blockState2, 2);
/*    */           } 
/* 40 */         } else if (b1 > 0) {
/* 41 */           BlockPos blockPos = blockPos2.below();
/* 42 */           if (blockState1.canSurvive((LevelReader)worldGenLevel, blockPos) && !worldGenLevel.getBlockState(blockPos.below()).is(Blocks.KELP)) {
/* 43 */             worldGenLevel.setBlock(blockPos, (BlockState)blockState1.setValue((Property)KelpBlock.AGE, Integer.valueOf(randomSource.nextInt(4) + 20)), 2);
/* 44 */             b++;
/*    */           } 
/*    */           
/*    */           break;
/*    */         } 
/* 49 */         blockPos2 = blockPos2.above();
/*    */       } 
/*    */     } 
/*    */     
/* 53 */     return (b > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\KelpFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */