/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.DoublePlantBlock;
/*    */ import net.minecraft.world.level.block.MossyCarpetBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*    */ 
/*    */ public class SimpleBlockFeature extends Feature<SimpleBlockConfiguration> {
/*    */   public SimpleBlockFeature(Codec<SimpleBlockConfiguration> paramCodec) {
/* 14 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> paramFeaturePlaceContext) {
/* 19 */     SimpleBlockConfiguration simpleBlockConfiguration = paramFeaturePlaceContext.config();
/* 20 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 21 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 22 */     BlockState blockState = simpleBlockConfiguration.toPlace().getState(paramFeaturePlaceContext.random(), blockPos);
/*    */     
/* 24 */     if (blockState.canSurvive((LevelReader)worldGenLevel, blockPos)) {
/* 25 */       if (blockState.getBlock() instanceof DoublePlantBlock) {
/* 26 */         if (worldGenLevel.isEmptyBlock(blockPos.above())) {
/* 27 */           DoublePlantBlock.placeAt((LevelAccessor)worldGenLevel, blockState, blockPos, 2);
/*    */         } else {
/* 29 */           return false;
/*    */         } 
/* 31 */       } else if (blockState.getBlock() instanceof MossyCarpetBlock) {
/* 32 */         MossyCarpetBlock.placeAt((LevelAccessor)worldGenLevel, blockPos, worldGenLevel.getRandom(), 2);
/*    */       } else {
/* 34 */         worldGenLevel.setBlock(blockPos, blockState, 2);
/*    */       } 
/* 36 */       if (simpleBlockConfiguration.scheduleTick()) {
/* 37 */         worldGenLevel.scheduleTick(blockPos, worldGenLevel.getBlockState(blockPos).getBlock(), 1);
/*    */       }
/* 39 */       return true;
/*    */     } 
/* 41 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\SimpleBlockFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */