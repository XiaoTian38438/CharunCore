/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*    */ 
/*    */ public class BlockBlobFeature extends Feature<BlockStateConfiguration> {
/*    */   public BlockBlobFeature(Codec<BlockStateConfiguration> paramCodec) {
/* 13 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<BlockStateConfiguration> paramFeaturePlaceContext) {
/* 18 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 19 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 20 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 21 */     BlockStateConfiguration blockStateConfiguration = paramFeaturePlaceContext.config();
/* 22 */     while (blockPos.getY() > worldGenLevel.getMinY() + 3) {
/* 23 */       if (!worldGenLevel.isEmptyBlock(blockPos.below())) {
/* 24 */         BlockState blockState = worldGenLevel.getBlockState(blockPos.below());
/* 25 */         if (isDirt(blockState) || isStone(blockState)) {
/*    */           break;
/*    */         }
/*    */       } 
/* 29 */       blockPos = blockPos.below();
/*    */     } 
/* 31 */     if (blockPos.getY() <= worldGenLevel.getMinY() + 3) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     byte b = 0;
/* 36 */     while (b < 3) {
/* 37 */       int i = randomSource.nextInt(2);
/* 38 */       int j = randomSource.nextInt(2);
/* 39 */       int k = randomSource.nextInt(2);
/* 40 */       float f = (i + j + k) * 0.333F + 0.5F;
/*    */       
/* 42 */       for (BlockPos blockPos1 : BlockPos.betweenClosed(blockPos.offset(-i, -j, -k), blockPos.offset(i, j, k))) {
/* 43 */         if (blockPos1.distSqr((Vec3i)blockPos) <= (f * f)) {
/* 44 */           worldGenLevel.setBlock(blockPos1, blockStateConfiguration.state, 3);
/*    */         }
/*    */       } 
/*    */       
/* 48 */       blockPos = blockPos.offset(-1 + randomSource.nextInt(2), -randomSource.nextInt(2), -1 + randomSource.nextInt(2));
/* 49 */       b++;
/*    */     } 
/*    */     
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\BlockBlobFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */