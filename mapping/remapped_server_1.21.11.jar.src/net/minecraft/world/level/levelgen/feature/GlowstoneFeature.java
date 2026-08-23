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
/*    */ public class GlowstoneFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/*    */   public GlowstoneFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 15 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 20 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 21 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 22 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 23 */     if (!worldGenLevel.isEmptyBlock(blockPos)) {
/* 24 */       return false;
/*    */     }
/*    */     
/* 27 */     BlockState blockState = worldGenLevel.getBlockState(blockPos.above());
/* 28 */     if (!blockState.is(Blocks.NETHERRACK) && !blockState.is(Blocks.BASALT) && !blockState.is(Blocks.BLACKSTONE)) {
/* 29 */       return false;
/*    */     }
/*    */     
/* 32 */     worldGenLevel.setBlock(blockPos, Blocks.GLOWSTONE.defaultBlockState(), 2);
/*    */     
/* 34 */     for (byte b = 0; b < 'ל'; b++) {
/* 35 */       BlockPos blockPos1 = blockPos.offset(randomSource.nextInt(8) - randomSource.nextInt(8), -randomSource.nextInt(12), randomSource.nextInt(8) - randomSource.nextInt(8));
/* 36 */       if (worldGenLevel.getBlockState(blockPos1).isAir()) {
/*    */ 
/*    */ 
/*    */         
/* 40 */         byte b1 = 0;
/* 41 */         for (Direction direction : Direction.values()) {
/* 42 */           if (worldGenLevel.getBlockState(blockPos1.relative(direction)).is(Blocks.GLOWSTONE)) {
/* 43 */             b1++;
/*    */           }
/*    */           
/* 46 */           if (b1 > 1) {
/*    */             break;
/*    */           }
/*    */         } 
/*    */         
/* 51 */         if (b1 == 1) {
/* 52 */           worldGenLevel.setBlock(blockPos1, Blocks.GLOWSTONE.defaultBlockState(), 2);
/*    */         }
/*    */       } 
/*    */     } 
/* 56 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\GlowstoneFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */