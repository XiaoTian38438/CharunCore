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
/*    */ public class HugeBrownMushroomFeature extends AbstractHugeMushroomFeature {
/*    */   public HugeBrownMushroomFeature(Codec<HugeMushroomFeatureConfiguration> paramCodec) {
/* 13 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void makeCap(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos, HugeMushroomFeatureConfiguration paramHugeMushroomFeatureConfiguration) {
/* 18 */     int i = paramHugeMushroomFeatureConfiguration.foliageRadius;
/* 19 */     for (int j = -i; j <= i; j++) {
/* 20 */       for (int k = -i; k <= i; k++) {
/* 21 */         boolean bool1 = (j == -i) ? true : false;
/* 22 */         boolean bool2 = (j == i) ? true : false;
/* 23 */         boolean bool3 = (k == -i) ? true : false;
/* 24 */         boolean bool4 = (k == i) ? true : false;
/*    */         
/* 26 */         boolean bool5 = (bool1 || bool2) ? true : false;
/* 27 */         boolean bool6 = (bool3 || bool4) ? true : false;
/* 28 */         if (!bool5 || !bool6) {
/*    */ 
/*    */ 
/*    */           
/* 32 */           paramMutableBlockPos.setWithOffset((Vec3i)paramBlockPos, j, paramInt, k);
/* 33 */           boolean bool7 = (bool1 || (bool6 && j == 1 - i)) ? true : false;
/* 34 */           boolean bool8 = (bool2 || (bool6 && j == i - 1)) ? true : false;
/* 35 */           boolean bool9 = (bool3 || (bool5 && k == 1 - i)) ? true : false;
/* 36 */           boolean bool10 = (bool4 || (bool5 && k == i - 1)) ? true : false;
/* 37 */           BlockState blockState = paramHugeMushroomFeatureConfiguration.capProvider.getState(paramRandomSource, paramBlockPos);
/* 38 */           if (blockState.hasProperty((Property)HugeMushroomBlock.WEST) && blockState
/* 39 */             .hasProperty((Property)HugeMushroomBlock.EAST) && blockState
/* 40 */             .hasProperty((Property)HugeMushroomBlock.NORTH) && blockState
/* 41 */             .hasProperty((Property)HugeMushroomBlock.SOUTH))
/*    */           {
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 47 */             blockState = (BlockState)((BlockState)((BlockState)((BlockState)blockState.setValue((Property)HugeMushroomBlock.WEST, Boolean.valueOf(bool7))).setValue((Property)HugeMushroomBlock.EAST, Boolean.valueOf(bool8))).setValue((Property)HugeMushroomBlock.NORTH, Boolean.valueOf(bool9))).setValue((Property)HugeMushroomBlock.SOUTH, Boolean.valueOf(bool10));
/*    */           }
/* 49 */           placeMushroomBlock(paramLevelAccessor, paramMutableBlockPos, blockState);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getTreeRadiusForHeight(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 56 */     return (paramInt4 <= 3) ? 0 : paramInt3;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\HugeBrownMushroomFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */