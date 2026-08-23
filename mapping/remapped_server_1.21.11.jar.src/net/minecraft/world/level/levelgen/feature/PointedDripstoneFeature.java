/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;
/*    */ 
/*    */ public class PointedDripstoneFeature
/*    */   extends Feature<PointedDripstoneConfiguration> {
/*    */   public PointedDripstoneFeature(Codec<PointedDripstoneConfiguration> paramCodec) {
/* 15 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<PointedDripstoneConfiguration> paramFeaturePlaceContext) {
/* 20 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 21 */     BlockPos blockPos1 = paramFeaturePlaceContext.origin();
/* 22 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 23 */     PointedDripstoneConfiguration pointedDripstoneConfiguration = paramFeaturePlaceContext.config();
/* 24 */     Optional<Direction> optional = getTipDirection((LevelAccessor)worldGenLevel, blockPos1, randomSource);
/*    */     
/* 26 */     if (optional.isEmpty()) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     BlockPos blockPos2 = blockPos1.relative(((Direction)optional.get()).getOpposite());
/*    */     
/* 32 */     createPatchOfDripstoneBlocks((LevelAccessor)worldGenLevel, randomSource, blockPos2, pointedDripstoneConfiguration);
/*    */     
/* 34 */     boolean bool = (randomSource.nextFloat() < pointedDripstoneConfiguration.chanceOfTallerDripstone && DripstoneUtils.isEmptyOrWater(worldGenLevel.getBlockState(blockPos1.relative(optional.get())))) ? true : true;
/*    */     
/* 36 */     DripstoneUtils.growPointedDripstone((LevelAccessor)worldGenLevel, blockPos1, optional.get(), bool, false);
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   private static Optional<Direction> getTipDirection(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 41 */     boolean bool1 = DripstoneUtils.isDripstoneBase(paramLevelAccessor.getBlockState(paramBlockPos.above()));
/* 42 */     boolean bool2 = DripstoneUtils.isDripstoneBase(paramLevelAccessor.getBlockState(paramBlockPos.below()));
/*    */     
/* 44 */     if (bool1 && bool2) {
/* 45 */       return Optional.of(paramRandomSource.nextBoolean() ? Direction.DOWN : Direction.UP);
/*    */     }
/* 47 */     if (bool1) {
/* 48 */       return Optional.of(Direction.DOWN);
/*    */     }
/* 50 */     if (bool2) {
/* 51 */       return Optional.of(Direction.UP);
/*    */     }
/* 53 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   private static void createPatchOfDripstoneBlocks(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, PointedDripstoneConfiguration paramPointedDripstoneConfiguration) {
/* 57 */     DripstoneUtils.placeDripstoneBlockIfPossible(paramLevelAccessor, paramBlockPos);
/*    */     
/* 59 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 60 */       if (paramRandomSource.nextFloat() > paramPointedDripstoneConfiguration.chanceOfDirectionalSpread) {
/*    */         continue;
/*    */       }
/*    */       
/* 64 */       BlockPos blockPos1 = paramBlockPos.relative(direction);
/* 65 */       DripstoneUtils.placeDripstoneBlockIfPossible(paramLevelAccessor, blockPos1);
/* 66 */       if (paramRandomSource.nextFloat() > paramPointedDripstoneConfiguration.chanceOfSpreadRadius2) {
/*    */         continue;
/*    */       }
/* 69 */       BlockPos blockPos2 = blockPos1.relative(Direction.getRandom(paramRandomSource));
/* 70 */       DripstoneUtils.placeDripstoneBlockIfPossible(paramLevelAccessor, blockPos2);
/* 71 */       if (paramRandomSource.nextFloat() > paramPointedDripstoneConfiguration.chanceOfSpreadRadius3) {
/*    */         continue;
/*    */       }
/* 74 */       BlockPos blockPos3 = blockPos2.relative(Direction.getRandom(paramRandomSource));
/* 75 */       DripstoneUtils.placeDripstoneBlockIfPossible(paramLevelAccessor, blockPos3);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\PointedDripstoneFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */