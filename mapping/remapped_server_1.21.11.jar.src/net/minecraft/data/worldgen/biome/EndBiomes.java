/*    */ package net.minecraft.data.worldgen.biome;
/*    */ 
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.data.worldgen.BiomeDefaultFeatures;
/*    */ import net.minecraft.data.worldgen.placement.EndPlacements;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*    */ import net.minecraft.world.level.biome.BiomeSpecialEffects;
/*    */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*    */ import net.minecraft.world.level.levelgen.GenerationStep;
/*    */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class EndBiomes {
/*    */   private static Biome baseEndBiome(BiomeGenerationSettings.Builder paramBuilder) {
/* 16 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/* 17 */     BiomeDefaultFeatures.endSpawns(builder);
/*    */     
/* 19 */     return (new Biome.BiomeBuilder())
/* 20 */       .hasPrecipitation(false)
/* 21 */       .temperature(0.5F)
/* 22 */       .downfall(0.5F)
/* 23 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 24 */         .waterColor(4159204)
/* 25 */         .build())
/*    */       
/* 27 */       .mobSpawnSettings(builder.build())
/* 28 */       .generationSettings(paramBuilder.build())
/* 29 */       .build();
/*    */   }
/*    */   
/*    */   public static Biome endBarrens(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/* 33 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/* 34 */     return baseEndBiome(builder);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Biome theEnd(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/* 40 */     BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1)).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_SPIKE).addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, EndPlacements.END_PLATFORM);
/* 41 */     return baseEndBiome(builder);
/*    */   }
/*    */   
/*    */   public static Biome endMidlands(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/* 45 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/* 46 */     return baseEndBiome(builder);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Biome endHighlands(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/* 52 */     BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1)).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EndPlacements.CHORUS_PLANT);
/* 53 */     return baseEndBiome(builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Biome smallEndIslands(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/* 58 */     BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1)).addFeature(GenerationStep.Decoration.RAW_GENERATION, EndPlacements.END_ISLAND_DECORATED);
/* 59 */     return baseEndBiome(builder);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\biome\EndBiomes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */