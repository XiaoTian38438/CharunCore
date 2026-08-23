/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*    */ 
/*    */ public class SpringFeature
/*    */   extends Feature<SpringConfiguration> {
/*    */   public SpringFeature(Codec<SpringConfiguration> paramCodec) {
/* 12 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SpringConfiguration> paramFeaturePlaceContext) {
/* 17 */     SpringConfiguration springConfiguration = paramFeaturePlaceContext.config();
/* 18 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 19 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 20 */     if (!worldGenLevel.getBlockState(blockPos.above()).is(springConfiguration.validBlocks)) {
/* 21 */       return false;
/*    */     }
/* 23 */     if (springConfiguration.requiresBlockBelow && !worldGenLevel.getBlockState(blockPos.below()).is(springConfiguration.validBlocks)) {
/* 24 */       return false;
/*    */     }
/*    */     
/* 27 */     BlockState blockState = worldGenLevel.getBlockState(blockPos);
/* 28 */     if (!blockState.isAir() && !blockState.is(springConfiguration.validBlocks)) {
/* 29 */       return false;
/*    */     }
/*    */     
/* 32 */     byte b1 = 0;
/*    */     
/* 34 */     byte b2 = 0;
/* 35 */     if (worldGenLevel.getBlockState(blockPos.west()).is(springConfiguration.validBlocks)) {
/* 36 */       b2++;
/*    */     }
/* 38 */     if (worldGenLevel.getBlockState(blockPos.east()).is(springConfiguration.validBlocks)) {
/* 39 */       b2++;
/*    */     }
/* 41 */     if (worldGenLevel.getBlockState(blockPos.north()).is(springConfiguration.validBlocks)) {
/* 42 */       b2++;
/*    */     }
/* 44 */     if (worldGenLevel.getBlockState(blockPos.south()).is(springConfiguration.validBlocks)) {
/* 45 */       b2++;
/*    */     }
/* 47 */     if (worldGenLevel.getBlockState(blockPos.below()).is(springConfiguration.validBlocks)) {
/* 48 */       b2++;
/*    */     }
/*    */     
/* 51 */     byte b3 = 0;
/* 52 */     if (worldGenLevel.isEmptyBlock(blockPos.west())) {
/* 53 */       b3++;
/*    */     }
/* 55 */     if (worldGenLevel.isEmptyBlock(blockPos.east())) {
/* 56 */       b3++;
/*    */     }
/* 58 */     if (worldGenLevel.isEmptyBlock(blockPos.north())) {
/* 59 */       b3++;
/*    */     }
/* 61 */     if (worldGenLevel.isEmptyBlock(blockPos.south())) {
/* 62 */       b3++;
/*    */     }
/* 64 */     if (worldGenLevel.isEmptyBlock(blockPos.below())) {
/* 65 */       b3++;
/*    */     }
/*    */     
/* 68 */     if (b2 == springConfiguration.rockCount && b3 == springConfiguration.holeCount) {
/* 69 */       worldGenLevel.setBlock(blockPos, springConfiguration.state.createLegacyBlock(), 2);
/* 70 */       worldGenLevel.scheduleTick(blockPos, springConfiguration.state.getType(), 0);
/* 71 */       b1++;
/*    */     } 
/*    */     
/* 74 */     return (b1 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\SpringFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */