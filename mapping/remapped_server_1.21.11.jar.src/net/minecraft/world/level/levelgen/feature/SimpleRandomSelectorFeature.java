/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class SimpleRandomSelectorFeature extends Feature<SimpleRandomFeatureConfiguration> {
/*    */   public SimpleRandomSelectorFeature(Codec<SimpleRandomFeatureConfiguration> paramCodec) {
/* 13 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SimpleRandomFeatureConfiguration> paramFeaturePlaceContext) {
/* 18 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 19 */     SimpleRandomFeatureConfiguration simpleRandomFeatureConfiguration = paramFeaturePlaceContext.config();
/* 20 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 21 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 22 */     ChunkGenerator chunkGenerator = paramFeaturePlaceContext.chunkGenerator();
/* 23 */     int i = randomSource.nextInt(simpleRandomFeatureConfiguration.features.size());
/* 24 */     PlacedFeature placedFeature = (PlacedFeature)simpleRandomFeatureConfiguration.features.get(i).value();
/* 25 */     return placedFeature.place(worldGenLevel, chunkGenerator, randomSource, blockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\SimpleRandomSelectorFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */