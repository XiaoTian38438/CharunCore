/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class BlueIceFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/*    */   public BlueIceFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 15 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 20 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 21 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 22 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 23 */     if (blockPos.getY() > worldGenLevel.getSeaLevel() - 1) {
/* 24 */       return false;
/*    */     }
/* 26 */     if (!worldGenLevel.getBlockState(blockPos).is(Blocks.WATER) && !worldGenLevel.getBlockState(blockPos.below()).is(Blocks.WATER)) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     boolean bool = false;
/* 31 */     for (Direction direction : Direction.values()) {
/* 32 */       if (direction != Direction.DOWN)
/*    */       {
/*    */         
/* 35 */         if (worldGenLevel.getBlockState(blockPos.relative(direction)).is(Blocks.PACKED_ICE)) {
/* 36 */           bool = true;
/*    */           break;
/*    */         }  } 
/*    */     } 
/* 40 */     if (!bool) {
/* 41 */       return false;
/*    */     }
/*    */     
/* 44 */     worldGenLevel.setBlock(blockPos, Blocks.BLUE_ICE.defaultBlockState(), 2);
/*    */     
/* 46 */     for (byte b = 0; b < 'È'; b++) {
/* 47 */       int i = randomSource.nextInt(5) - randomSource.nextInt(6);
/* 48 */       int j = 3;
/* 49 */       if (i < 2) {
/* 50 */         j += i / 2;
/*    */       }
/* 52 */       if (j >= 1) {
/*    */ 
/*    */ 
/*    */         
/* 56 */         BlockPos blockPos1 = blockPos.offset(randomSource.nextInt(j) - randomSource.nextInt(j), i, randomSource.nextInt(j) - randomSource.nextInt(j));
/* 57 */         BlockState blockState = worldGenLevel.getBlockState(blockPos1);
/* 58 */         if (blockState.isAir() || blockState.is(Blocks.WATER) || blockState.is(Blocks.PACKED_ICE) || blockState.is(Blocks.ICE))
/*    */         {
/*    */ 
/*    */           
/* 62 */           for (Direction direction : Direction.values()) {
/* 63 */             BlockState blockState1 = worldGenLevel.getBlockState(blockPos1.relative(direction));
/* 64 */             if (blockState1.is(Blocks.BLUE_ICE)) {
/* 65 */               worldGenLevel.setBlock(blockPos1, Blocks.BLUE_ICE.defaultBlockState(), 2);
/*    */               break;
/*    */             } 
/*    */           }  } 
/*    */       } 
/*    */     } 
/* 71 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\BlueIceFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */