/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class BasaltPillarFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public BasaltPillarFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 16 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 21 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 22 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 23 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 24 */     if (!worldGenLevel.isEmptyBlock(blockPos) || worldGenLevel.isEmptyBlock(blockPos.above())) {
/* 25 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 29 */     BlockPos.MutableBlockPos mutableBlockPos1 = blockPos.mutable();
/* 30 */     BlockPos.MutableBlockPos mutableBlockPos2 = blockPos.mutable();
/* 31 */     boolean bool1 = true;
/* 32 */     boolean bool2 = true;
/* 33 */     boolean bool3 = true;
/* 34 */     boolean bool4 = true;
/*    */     
/* 36 */     while (worldGenLevel.isEmptyBlock((BlockPos)mutableBlockPos1)) {
/* 37 */       if (worldGenLevel.isOutsideBuildHeight((BlockPos)mutableBlockPos1)) {
/* 38 */         return true;
/*    */       }
/*    */       
/* 41 */       worldGenLevel.setBlock((BlockPos)mutableBlockPos1, Blocks.BASALT.defaultBlockState(), 2);
/*    */       
/* 43 */       bool1 = (bool1 && placeHangOff((LevelAccessor)worldGenLevel, randomSource, (BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos1, Direction.NORTH))) ? true : false;
/* 44 */       bool2 = (bool2 && placeHangOff((LevelAccessor)worldGenLevel, randomSource, (BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos1, Direction.SOUTH))) ? true : false;
/* 45 */       bool3 = (bool3 && placeHangOff((LevelAccessor)worldGenLevel, randomSource, (BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos1, Direction.WEST))) ? true : false;
/* 46 */       bool4 = (bool4 && placeHangOff((LevelAccessor)worldGenLevel, randomSource, (BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos1, Direction.EAST))) ? true : false;
/*    */       
/* 48 */       mutableBlockPos1.move(Direction.DOWN);
/*    */     } 
/*    */ 
/*    */     
/* 52 */     mutableBlockPos1.move(Direction.UP);
/* 53 */     placeBaseHangOff((LevelAccessor)worldGenLevel, randomSource, (BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos1, Direction.NORTH));
/* 54 */     placeBaseHangOff((LevelAccessor)worldGenLevel, randomSource, (BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos1, Direction.SOUTH));
/* 55 */     placeBaseHangOff((LevelAccessor)worldGenLevel, randomSource, (BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos1, Direction.WEST));
/* 56 */     placeBaseHangOff((LevelAccessor)worldGenLevel, randomSource, (BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos1, Direction.EAST));
/* 57 */     mutableBlockPos1.move(Direction.DOWN);
/*    */     
/* 59 */     BlockPos.MutableBlockPos mutableBlockPos3 = new BlockPos.MutableBlockPos();
/* 60 */     for (byte b = -3; b < 4; b++) {
/* 61 */       for (byte b1 = -3; b1 < 4; b1++) {
/* 62 */         int i = Mth.abs(b) * Mth.abs(b1);
/* 63 */         if (randomSource.nextInt(10) < 10 - i) {
/*    */ 
/*    */ 
/*    */           
/* 67 */           mutableBlockPos3.set((Vec3i)mutableBlockPos1.offset(b, 0, b1));
/* 68 */           byte b2 = 3;
/* 69 */           while (worldGenLevel.isEmptyBlock((BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos3, Direction.DOWN))) {
/* 70 */             mutableBlockPos3.move(Direction.DOWN);
/* 71 */             b2--;
/* 72 */             if (b2 <= 0) {
/*    */               break;
/*    */             }
/*    */           } 
/*    */           
/* 77 */           if (!worldGenLevel.isEmptyBlock((BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos3, Direction.DOWN))) {
/* 78 */             worldGenLevel.setBlock((BlockPos)mutableBlockPos3, Blocks.BASALT.defaultBlockState(), 2);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 83 */     return true;
/*    */   }
/*    */   
/*    */   private void placeBaseHangOff(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 87 */     if (paramRandomSource.nextBoolean()) {
/* 88 */       paramLevelAccessor.setBlock(paramBlockPos, Blocks.BASALT.defaultBlockState(), 2);
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean placeHangOff(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 93 */     if (paramRandomSource.nextInt(10) != 0) {
/* 94 */       paramLevelAccessor.setBlock(paramBlockPos, Blocks.BASALT.defaultBlockState(), 2);
/* 95 */       return true;
/*    */     } 
/*    */     
/* 98 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\BasaltPillarFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */