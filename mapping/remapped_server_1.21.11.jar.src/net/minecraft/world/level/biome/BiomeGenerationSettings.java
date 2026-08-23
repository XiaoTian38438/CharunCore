/*     */ package net.minecraft.world.level.biome;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BiomeGenerationSettings {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  30 */   public static final BiomeGenerationSettings EMPTY = new BiomeGenerationSettings(
/*  31 */       (HolderSet<ConfiguredWorldCarver<?>>)HolderSet.direct(new Holder[0]), 
/*  32 */       List.of()); public static final MapCodec<BiomeGenerationSettings> CODEC; private final HolderSet<ConfiguredWorldCarver<?>> carvers;
/*     */   
/*     */   static {
/*  35 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> {
/*     */           Objects.requireNonNull(LOGGER);
/*     */           Objects.requireNonNull(LOGGER);
/*     */           return (Function)paramInstance.group((App)ConfiguredWorldCarver.LIST_CODEC.promotePartial(Util.prefix("Carver: ", LOGGER::error)).fieldOf("carvers").forGetter(()), (App)PlacedFeature.LIST_OF_LISTS_CODEC.promotePartial(Util.prefix("Features: ", LOGGER::error)).fieldOf("features").forGetter(())).apply((Applicative)paramInstance, BiomeGenerationSettings::new);
/*     */         });
/*     */   }
/*     */   
/*     */   private final List<HolderSet<PlacedFeature>> features;
/*     */   private final Supplier<List<ConfiguredFeature<?, ?>>> flowerFeatures;
/*     */   private final Supplier<Set<PlacedFeature>> featureSet;
/*     */   
/*     */   BiomeGenerationSettings(HolderSet<ConfiguredWorldCarver<?>> paramHolderSet, List<HolderSet<PlacedFeature>> paramList) {
/*  47 */     this.carvers = paramHolderSet;
/*  48 */     this.features = paramList;
/*     */ 
/*     */     
/*  51 */     this.flowerFeatures = (Supplier<List<ConfiguredFeature<?, ?>>>)Suppliers.memoize(() -> (List)paramList.stream().flatMap(HolderSet::stream).map(Holder::value).flatMap(PlacedFeature::getFeatures).filter(()).collect(ImmutableList.toImmutableList()));
/*  52 */     this.featureSet = (Supplier<Set<PlacedFeature>>)Suppliers.memoize(() -> (Set)paramList.stream().flatMap(HolderSet::stream).map(Holder::value).collect(Collectors.toSet()));
/*     */   }
/*     */   
/*     */   public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers() {
/*  56 */     return (Iterable)this.carvers;
/*     */   }
/*     */   
/*     */   public List<ConfiguredFeature<?, ?>> getFlowerFeatures() {
/*  60 */     return this.flowerFeatures.get();
/*     */   }
/*     */   
/*     */   public List<HolderSet<PlacedFeature>> features() {
/*  64 */     return this.features;
/*     */   }
/*     */   
/*     */   public boolean hasFeature(PlacedFeature paramPlacedFeature) {
/*  68 */     return ((Set)this.featureSet.get()).contains(paramPlacedFeature);
/*     */   }
/*     */   
/*     */   public static class PlainBuilder {
/*  72 */     private final List<Holder<ConfiguredWorldCarver<?>>> carvers = new ArrayList<>();
/*  73 */     private final List<List<Holder<PlacedFeature>>> features = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PlainBuilder addFeature(GenerationStep.Decoration param1Decoration, Holder<PlacedFeature> param1Holder) {
/*  79 */       return addFeature(param1Decoration.ordinal(), param1Holder);
/*     */     }
/*     */     
/*     */     public PlainBuilder addFeature(int param1Int, Holder<PlacedFeature> param1Holder) {
/*  83 */       addFeatureStepsUpTo(param1Int);
/*  84 */       ((List<Holder<PlacedFeature>>)this.features.get(param1Int)).add(param1Holder);
/*  85 */       return this;
/*     */     }
/*     */     
/*     */     public PlainBuilder addCarver(Holder<ConfiguredWorldCarver<?>> param1Holder) {
/*  89 */       this.carvers.add(param1Holder);
/*  90 */       return this;
/*     */     }
/*     */     
/*     */     private void addFeatureStepsUpTo(int param1Int) {
/*  94 */       while (this.features.size() <= param1Int) {
/*  95 */         this.features.add(Lists.newArrayList());
/*     */       }
/*     */     }
/*     */     
/*     */     public BiomeGenerationSettings build() {
/* 100 */       return new BiomeGenerationSettings(
/* 101 */           (HolderSet<ConfiguredWorldCarver<?>>)HolderSet.direct(this.carvers), (List<HolderSet<PlacedFeature>>)this.features
/* 102 */           .stream().map(HolderSet::direct).collect(ImmutableList.toImmutableList()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */     extends PlainBuilder {
/*     */     private final HolderGetter<PlacedFeature> placedFeatures;
/*     */     private final HolderGetter<ConfiguredWorldCarver<?>> worldCarvers;
/*     */     
/*     */     public Builder(HolderGetter<PlacedFeature> param1HolderGetter, HolderGetter<ConfiguredWorldCarver<?>> param1HolderGetter1) {
/* 112 */       this.placedFeatures = param1HolderGetter;
/* 113 */       this.worldCarvers = param1HolderGetter1;
/*     */     }
/*     */     
/*     */     public Builder addFeature(GenerationStep.Decoration param1Decoration, ResourceKey<PlacedFeature> param1ResourceKey) {
/* 117 */       addFeature(param1Decoration.ordinal(), (Holder<PlacedFeature>)this.placedFeatures.getOrThrow(param1ResourceKey));
/* 118 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addCarver(ResourceKey<ConfiguredWorldCarver<?>> param1ResourceKey) {
/* 122 */       addCarver((Holder<ConfiguredWorldCarver<?>>)this.worldCarvers.getOrThrow(param1ResourceKey));
/* 123 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\BiomeGenerationSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */