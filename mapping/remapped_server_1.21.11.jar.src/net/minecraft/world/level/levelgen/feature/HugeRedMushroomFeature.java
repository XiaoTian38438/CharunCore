/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.HugeMushroomBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*    */ 
/*    */ public class HugeRedMushroomFeature extends AbstractHugeMushroomFeature {
/*    */   public HugeRedMushroomFeature(Codec<HugeMushroomFeatureConfiguration> paramCodec) {
/* 13 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void makeCap(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos, HugeMushroomFeatureConfiguration paramHugeMushroomFeatureConfiguration) {
/* 18 */     for (int i = paramInt - 3; i <= paramInt; i++) {
/* 19 */       int j = (i < paramInt) ? paramHugeMushroomFeatureConfiguration.foliageRadius : (paramHugeMushroomFeatureConfiguration.foliageRadius - 1);
/* 20 */       int k = paramHugeMushroomFeatureConfiguration.foliageRadius - 2;
/*    */       
/* 22 */       for (int m = -j; m <= j; m++) {
/* 23 */         for (int n = -j; n <= j; n++) {
/* 24 */           boolean bool1 = (m == -j) ? true : false;
/* 25 */           boolean bool2 = (m == j) ? true : false;
/* 26 */           boolean bool3 = (n == -j) ? true : false;
/* 27 */           boolean bool4 = (n == j) ? true : false;
/*    */           
/* 29 */           boolean bool5 = (bool1 || bool2) ? true : false;
/* 30 */           boolean bool6 = (bool3 || bool4) ? true : false;
/*    */           
/* 32 */           if (i >= paramInt || bool5 != bool6) {
/*    */ 
/*    */ 
/*    */             
/* 36 */             paramMutableBlockPos.setWithOffset((Vec3i)paramBlockPos, m, i, n);
/* 37 */             BlockState blockState = paramHugeMushroomFeatureConfiguration.capProvider.getState(paramRandomSource, paramBlockPos);
/* 38 */             if (blockState.hasProperty((Property)HugeMushroomBlock.WEST) && blockState
/* 39 */               .hasProperty((Property)HugeMushroomBlock.EAST) && blockState
/* 40 */               .hasProperty((Property)HugeMushroomBlock.NORTH) && blockState
/* 41 */               .hasProperty((Property)HugeMushroomBlock.SOUTH) && blockState
/* 42 */               .hasProperty((Property)HugeMushroomBlock.UP))
/*    */             {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */               
/* 49 */               blockState = (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)blockState.setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf((i >= paramInt - 1)))).setValue((Property)HugeMushroomBlock.WEST, Boolean.valueOf((m < -k)))).setValue((Property)HugeMushroomBlock.EAST, Boolean.valueOf((m > k)))).setValue((Property)HugeMushroomBlock.NORTH, Boolean.valueOf((n < -k)))).setValue((Property)HugeMushroomBlock.SOUTH, Boolean.valueOf((n > k)));
/*    */             }
/* 51 */             placeMushroomBlock(paramLevelAccessor, paramMutableBlockPos, blockState);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getTreeRadiusForHeight(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 59 */     int i = 0;
/* 60 */     if (paramInt4 < paramInt2 && paramInt4 >= paramInt2 - 3) {
/* 61 */       i = paramInt3;
/* 62 */     } else if (paramInt4 == paramInt2) {
/* 63 */       i = paramInt3;
/*    */     } 
/* 65 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\HugeRedMushroomFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */