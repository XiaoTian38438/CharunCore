/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FeaturePlaceContext<FC extends FeatureConfiguration>
/*    */ {
/*    */   private final Optional<ConfiguredFeature<?, ?>> topFeature;
/*    */   private final WorldGenLevel level;
/*    */   private final ChunkGenerator chunkGenerator;
/*    */   private final RandomSource random;
/*    */   private final BlockPos origin;
/*    */   private final FC config;
/*    */   
/*    */   public FeaturePlaceContext(Optional<ConfiguredFeature<?, ?>> paramOptional, WorldGenLevel paramWorldGenLevel, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BlockPos paramBlockPos, FC paramFC) {
/* 23 */     this.topFeature = paramOptional;
/* 24 */     this.level = paramWorldGenLevel;
/* 25 */     this.chunkGenerator = paramChunkGenerator;
/* 26 */     this.random = paramRandomSource;
/* 27 */     this.origin = paramBlockPos;
/* 28 */     this.config = paramFC;
/*    */   }
/*    */   
/*    */   public Optional<ConfiguredFeature<?, ?>> topFeature() {
/* 32 */     return this.topFeature;
/*    */   }
/*    */   
/*    */   public WorldGenLevel level() {
/* 36 */     return this.level;
/*    */   }
/*    */   
/*    */   public ChunkGenerator chunkGenerator() {
/* 40 */     return this.chunkGenerator;
/*    */   }
/*    */   
/*    */   public RandomSource random() {
/* 44 */     return this.random;
/*    */   }
/*    */   
/*    */   public BlockPos origin() {
/* 48 */     return this.origin;
/*    */   }
/*    */   
/*    */   public FC config() {
/* 52 */     return this.config;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\FeaturePlaceContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */