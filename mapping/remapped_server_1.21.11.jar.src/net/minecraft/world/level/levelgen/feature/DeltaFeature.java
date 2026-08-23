/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
/*    */ 
/*    */ public class DeltaFeature extends Feature<DeltaFeatureConfiguration> {
/* 16 */   private static final ImmutableList<Block> CANNOT_REPLACE = ImmutableList.of(Blocks.BEDROCK, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   private static final Direction[] DIRECTIONS = Direction.values();
/*    */   private static final double RIM_SPAWN_CHANCE = 0.9D;
/*    */   
/*    */   public DeltaFeature(Codec<DeltaFeatureConfiguration> paramCodec) {
/* 27 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<DeltaFeatureConfiguration> paramFeaturePlaceContext) {
/* 32 */     boolean bool1 = false;
/* 33 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 34 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 35 */     DeltaFeatureConfiguration deltaFeatureConfiguration = paramFeaturePlaceContext.config();
/* 36 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 37 */     boolean bool2 = (randomSource.nextDouble() < 0.9D) ? true : false;
/* 38 */     boolean bool3 = bool2 ? deltaFeatureConfiguration.rimSize().sample(randomSource) : false;
/* 39 */     boolean bool4 = bool2 ? deltaFeatureConfiguration.rimSize().sample(randomSource) : false;
/* 40 */     boolean bool5 = (bool2 && bool3 && bool4) ? true : false;
/*    */     
/* 42 */     int i = deltaFeatureConfiguration.size().sample(randomSource);
/* 43 */     int j = deltaFeatureConfiguration.size().sample(randomSource);
/* 44 */     int k = Math.max(i, j);
/* 45 */     for (BlockPos blockPos1 : BlockPos.withinManhattan(blockPos, i, 0, j)) {
/* 46 */       if (blockPos1.distManhattan((Vec3i)blockPos) > k) {
/*    */         break;
/*    */       }
/*    */       
/* 50 */       if (isClear((LevelAccessor)worldGenLevel, blockPos1, deltaFeatureConfiguration)) {
/* 51 */         if (bool5) {
/* 52 */           bool1 = true;
/* 53 */           setBlock((LevelWriter)worldGenLevel, blockPos1, deltaFeatureConfiguration.rim());
/*    */         } 
/*    */         
/* 56 */         BlockPos blockPos2 = blockPos1.offset(bool3, 0, bool4);
/* 57 */         if (isClear((LevelAccessor)worldGenLevel, blockPos2, deltaFeatureConfiguration)) {
/* 58 */           bool1 = true;
/* 59 */           setBlock((LevelWriter)worldGenLevel, blockPos2, deltaFeatureConfiguration.contents());
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     return bool1;
/*    */   }
/*    */   
/*    */   private static boolean isClear(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, DeltaFeatureConfiguration paramDeltaFeatureConfiguration) {
/* 68 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos);
/* 69 */     if (blockState.is(paramDeltaFeatureConfiguration.contents().getBlock())) {
/* 70 */       return false;
/*    */     }
/*    */     
/* 73 */     if (CANNOT_REPLACE.contains(blockState.getBlock())) {
/* 74 */       return false;
/*    */     }
/*    */     
/* 77 */     for (Direction direction : DIRECTIONS) {
/* 78 */       boolean bool = paramLevelAccessor.getBlockState(paramBlockPos.relative(direction)).isAir();
/* 79 */       if ((bool && direction != Direction.UP) || (!bool && direction == Direction.UP)) {
/* 80 */         return false;
/*    */       }
/*    */     } 
/* 83 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\DeltaFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */