/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.GrowingPlantHeadBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TwistingVinesConfig;
/*    */ 
/*    */ public class TwistingVinesFeature extends Feature<TwistingVinesConfig> {
/*    */   public TwistingVinesFeature(Codec<TwistingVinesConfig> paramCodec) {
/* 18 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<TwistingVinesConfig> paramFeaturePlaceContext) {
/* 23 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 24 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 25 */     if (isInvalidPlacementLocation((LevelAccessor)worldGenLevel, blockPos)) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 30 */     TwistingVinesConfig twistingVinesConfig = paramFeaturePlaceContext.config();
/* 31 */     int i = twistingVinesConfig.spreadWidth();
/* 32 */     int j = twistingVinesConfig.spreadHeight();
/* 33 */     int k = twistingVinesConfig.maxHeight();
/*    */     
/* 35 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*    */     
/* 37 */     for (byte b = 0; b < i * i; b++) {
/* 38 */       mutableBlockPos.set((Vec3i)blockPos).move(
/* 39 */           Mth.nextInt(randomSource, -i, i), 
/* 40 */           Mth.nextInt(randomSource, -j, j), 
/* 41 */           Mth.nextInt(randomSource, -i, i));
/*    */ 
/*    */       
/* 44 */       if (findFirstAirBlockAboveGround((LevelAccessor)worldGenLevel, mutableBlockPos))
/*    */       {
/*    */ 
/*    */         
/* 48 */         if (!isInvalidPlacementLocation((LevelAccessor)worldGenLevel, (BlockPos)mutableBlockPos)) {
/*    */ 
/*    */ 
/*    */           
/* 52 */           int m = Mth.nextInt(randomSource, 1, k);
/* 53 */           if (randomSource.nextInt(6) == 0) {
/* 54 */             m *= 2;
/*    */           }
/* 56 */           if (randomSource.nextInt(5) == 0) {
/* 57 */             m = 1;
/*    */           }
/*    */           
/* 60 */           byte b1 = 17;
/* 61 */           byte b2 = 25;
/* 62 */           placeWeepingVinesColumn((LevelAccessor)worldGenLevel, randomSource, mutableBlockPos, m, 17, 25);
/*    */         }  } 
/* 64 */     }  return true;
/*    */   }
/*    */   
/*    */   private static boolean findFirstAirBlockAboveGround(LevelAccessor paramLevelAccessor, BlockPos.MutableBlockPos paramMutableBlockPos) {
/*    */     while (true) {
/* 69 */       paramMutableBlockPos.move(0, -1, 0);
/* 70 */       if (paramLevelAccessor.isOutsideBuildHeight((BlockPos)paramMutableBlockPos)) {
/* 71 */         return false;
/*    */       }
/* 73 */       if (!paramLevelAccessor.getBlockState((BlockPos)paramMutableBlockPos).isAir()) {
/* 74 */         paramMutableBlockPos.move(0, 1, 0);
/* 75 */         return true;
/*    */       } 
/*    */     } 
/*    */   } public static void placeWeepingVinesColumn(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt1, int paramInt2, int paramInt3) {
/* 79 */     for (byte b = 1; b <= paramInt1; b++) {
/* 80 */       if (paramLevelAccessor.isEmptyBlock((BlockPos)paramMutableBlockPos)) {
/* 81 */         if (b == paramInt1 || !paramLevelAccessor.isEmptyBlock(paramMutableBlockPos.above())) {
/* 82 */           paramLevelAccessor.setBlock((BlockPos)paramMutableBlockPos, (BlockState)Blocks.TWISTING_VINES.defaultBlockState().setValue((Property)GrowingPlantHeadBlock.AGE, Integer.valueOf(Mth.nextInt(paramRandomSource, paramInt2, paramInt3))), 2);
/*    */           break;
/*    */         } 
/* 85 */         paramLevelAccessor.setBlock((BlockPos)paramMutableBlockPos, Blocks.TWISTING_VINES_PLANT.defaultBlockState(), 2);
/*    */       } 
/*    */ 
/*    */       
/* 89 */       paramMutableBlockPos.move(Direction.UP);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static boolean isInvalidPlacementLocation(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 94 */     if (!paramLevelAccessor.isEmptyBlock(paramBlockPos)) {
/* 95 */       return true;
/*    */     }
/*    */     
/* 98 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos.below());
/* 99 */     return (!blockState.is(Blocks.NETHERRACK) && !blockState.is(Blocks.WARPED_NYLIUM) && !blockState.is(Blocks.WARPED_WART_BLOCK));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\TwistingVinesFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */