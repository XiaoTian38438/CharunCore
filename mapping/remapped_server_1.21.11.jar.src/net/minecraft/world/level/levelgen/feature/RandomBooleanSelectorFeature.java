/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RandomBooleanSelectorFeature extends Feature<RandomBooleanFeatureConfiguration> {
/*    */   public RandomBooleanSelectorFeature(Codec<RandomBooleanFeatureConfiguration> paramCodec) {
/* 12 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<RandomBooleanFeatureConfiguration> paramFeaturePlaceContext) {
/* 17 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 18 */     RandomBooleanFeatureConfiguration randomBooleanFeatureConfiguration = paramFeaturePlaceContext.config();
/* 19 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 20 */     ChunkGenerator chunkGenerator = paramFeaturePlaceContext.chunkGenerator();
/* 21 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 22 */     boolean bool = randomSource.nextBoolean();
/* 23 */     return ((PlacedFeature)(bool ? randomBooleanFeatureConfiguration.featureTrue : randomBooleanFeatureConfiguration.featureFalse).value()).place(worldGenLevel, chunkGenerator, randomSource, blockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\RandomBooleanSelectorFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */