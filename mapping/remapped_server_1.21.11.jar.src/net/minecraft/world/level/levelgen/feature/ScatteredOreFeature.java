/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*    */ 
/*    */ public class ScatteredOreFeature
/*    */   extends Feature<OreConfiguration>
/*    */ {
/*    */   private static final int MAX_DIST_FROM_ORIGIN = 7;
/*    */   
/*    */   ScatteredOreFeature(Codec<OreConfiguration> paramCodec) {
/* 18 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<OreConfiguration> paramFeaturePlaceContext) {
/* 26 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 27 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 28 */     OreConfiguration oreConfiguration = paramFeaturePlaceContext.config();
/* 29 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 30 */     int i = randomSource.nextInt(oreConfiguration.size + 1);
/* 31 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*    */     
/* 33 */     for (byte b = 0; b < i; b++) {
/*    */       
/* 35 */       offsetTargetPos(mutableBlockPos, randomSource, blockPos, Math.min(b, 7));
/*    */       
/* 37 */       BlockState blockState = worldGenLevel.getBlockState((BlockPos)mutableBlockPos);
/* 38 */       for (OreConfiguration.TargetBlockState targetBlockState : oreConfiguration.targetStates) {
/* 39 */         Objects.requireNonNull(worldGenLevel); if (OreFeature.canPlaceOre(blockState, worldGenLevel::getBlockState, randomSource, oreConfiguration, targetBlockState, mutableBlockPos)) {
/* 40 */           worldGenLevel.setBlock((BlockPos)mutableBlockPos, targetBlockState.state, 2);
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   private void offsetTargetPos(BlockPos.MutableBlockPos paramMutableBlockPos, RandomSource paramRandomSource, BlockPos paramBlockPos, int paramInt) {
/* 49 */     int i = getRandomPlacementInOneAxisRelativeToOrigin(paramRandomSource, paramInt);
/* 50 */     int j = getRandomPlacementInOneAxisRelativeToOrigin(paramRandomSource, paramInt);
/* 51 */     int k = getRandomPlacementInOneAxisRelativeToOrigin(paramRandomSource, paramInt);
/* 52 */     paramMutableBlockPos.setWithOffset((Vec3i)paramBlockPos, i, j, k);
/*    */   }
/*    */   
/*    */   private int getRandomPlacementInOneAxisRelativeToOrigin(RandomSource paramRandomSource, int paramInt) {
/* 56 */     return Math.round((paramRandomSource.nextFloat() - paramRandomSource.nextFloat()) * paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\ScatteredOreFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */