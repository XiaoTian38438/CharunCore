/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class SimpleRandomFeatureConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<SimpleRandomFeatureConfiguration> CODEC;
/*    */   
/*    */   static {
/* 13 */     CODEC = ExtraCodecs.nonEmptyHolderSet(PlacedFeature.LIST_CODEC).fieldOf("features").xmap(SimpleRandomFeatureConfiguration::new, paramSimpleRandomFeatureConfiguration -> paramSimpleRandomFeatureConfiguration.features).codec();
/*    */   }
/*    */   public final HolderSet<PlacedFeature> features;
/*    */   
/*    */   public SimpleRandomFeatureConfiguration(HolderSet<PlacedFeature> paramHolderSet) {
/* 18 */     this.features = paramHolderSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<ConfiguredFeature<?, ?>> getFeatures() {
/* 23 */     return this.features.stream().flatMap(paramHolder -> ((PlacedFeature)paramHolder.value()).getFeatures());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\SimpleRandomFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */