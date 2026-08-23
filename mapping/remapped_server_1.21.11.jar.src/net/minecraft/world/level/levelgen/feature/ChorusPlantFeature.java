/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.ChorusFlowerBlock;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class ChorusPlantFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public ChorusPlantFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 13 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 18 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 19 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 20 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 21 */     if (worldGenLevel.isEmptyBlock(blockPos) && worldGenLevel.getBlockState(blockPos.below()).is(Blocks.END_STONE)) {
/* 22 */       ChorusFlowerBlock.generatePlant((LevelAccessor)worldGenLevel, blockPos, randomSource, 8);
/* 23 */       return true;
/*    */     } 
/* 25 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\ChorusPlantFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */