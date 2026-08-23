/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RandomFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.apply2(RandomFeatureConfiguration::new, (App)WeightedPlacedFeature.CODEC.listOf().fieldOf("features").forGetter(()), (App)PlacedFeature.CODEC.fieldOf("default").forGetter(())));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<RandomFeatureConfiguration> CODEC;
/*    */   
/*    */   public final List<WeightedPlacedFeature> features;
/*    */   public final Holder<PlacedFeature> defaultFeature;
/*    */   
/*    */   public RandomFeatureConfiguration(List<WeightedPlacedFeature> paramList, Holder<PlacedFeature> paramHolder) {
/* 25 */     this.features = paramList;
/* 26 */     this.defaultFeature = paramHolder;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<ConfiguredFeature<?, ?>> getFeatures() {
/* 31 */     return Stream.concat(this.features.stream().flatMap(paramWeightedPlacedFeature -> ((PlacedFeature)paramWeightedPlacedFeature.feature.value()).getFeatures()), ((PlacedFeature)this.defaultFeature.value()).getFeatures());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\RandomFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */