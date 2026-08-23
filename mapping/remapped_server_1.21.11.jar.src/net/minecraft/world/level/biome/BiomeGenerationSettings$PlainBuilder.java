/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
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
/*     */ public class PlainBuilder
/*     */ {
/*  72 */   private final List<Holder<ConfiguredWorldCarver<?>>> carvers = new ArrayList<>();
/*  73 */   private final List<List<Holder<PlacedFeature>>> features = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlainBuilder addFeature(GenerationStep.Decoration paramDecoration, Holder<PlacedFeature> paramHolder) {
/*  79 */     return addFeature(paramDecoration.ordinal(), paramHolder);
/*     */   }
/*     */   
/*     */   public PlainBuilder addFeature(int paramInt, Holder<PlacedFeature> paramHolder) {
/*  83 */     addFeatureStepsUpTo(paramInt);
/*  84 */     ((List<Holder<PlacedFeature>>)this.features.get(paramInt)).add(paramHolder);
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public PlainBuilder addCarver(Holder<ConfiguredWorldCarver<?>> paramHolder) {
/*  89 */     this.carvers.add(paramHolder);
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   private void addFeatureStepsUpTo(int paramInt) {
/*  94 */     while (this.features.size() <= paramInt) {
/*  95 */       this.features.add(Lists.newArrayList());
/*     */     }
/*     */   }
/*     */   
/*     */   public BiomeGenerationSettings build() {
/* 100 */     return new BiomeGenerationSettings(
/* 101 */         (HolderSet<ConfiguredWorldCarver<?>>)HolderSet.direct(this.carvers), (List<HolderSet<PlacedFeature>>)this.features
/* 102 */         .stream().map(HolderSet::direct).collect(ImmutableList.toImmutableList()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\BiomeGenerationSettings$PlainBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */