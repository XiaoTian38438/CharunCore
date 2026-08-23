/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */   extends BiomeGenerationSettings.PlainBuilder
/*     */ {
/*     */   private final HolderGetter<PlacedFeature> placedFeatures;
/*     */   private final HolderGetter<ConfiguredWorldCarver<?>> worldCarvers;
/*     */   
/*     */   public Builder(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/* 112 */     this.placedFeatures = paramHolderGetter;
/* 113 */     this.worldCarvers = paramHolderGetter1;
/*     */   }
/*     */   
/*     */   public Builder addFeature(GenerationStep.Decoration paramDecoration, ResourceKey<PlacedFeature> paramResourceKey) {
/* 117 */     addFeature(paramDecoration.ordinal(), (Holder<PlacedFeature>)this.placedFeatures.getOrThrow(paramResourceKey));
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addCarver(ResourceKey<ConfiguredWorldCarver<?>> paramResourceKey) {
/* 122 */     addCarver((Holder<ConfiguredWorldCarver<?>>)this.worldCarvers.getOrThrow(paramResourceKey));
/* 123 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\BiomeGenerationSettings$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */