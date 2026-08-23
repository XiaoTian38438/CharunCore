/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RandomSelectorFeature extends Feature<RandomFeatureConfiguration> {
/*    */   public RandomSelectorFeature(Codec<RandomFeatureConfiguration> paramCodec) {
/* 12 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<RandomFeatureConfiguration> paramFeaturePlaceContext) {
/* 17 */     RandomFeatureConfiguration randomFeatureConfiguration = paramFeaturePlaceContext.config();
/* 18 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 19 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 20 */     ChunkGenerator chunkGenerator = paramFeaturePlaceContext.chunkGenerator();
/* 21 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 22 */     for (WeightedPlacedFeature weightedPlacedFeature : randomFeatureConfiguration.features) {
/* 23 */       if (randomSource.nextFloat() < weightedPlacedFeature.chance) {
/* 24 */         return weightedPlacedFeature.place(worldGenLevel, chunkGenerator, randomSource, blockPos);
/*    */       }
/*    */     } 
/* 27 */     return ((PlacedFeature)randomFeatureConfiguration.defaultFeature.value()).place(worldGenLevel, chunkGenerator, randomSource, blockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\RandomSelectorFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */