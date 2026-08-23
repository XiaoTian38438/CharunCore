/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RandomBooleanFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)PlacedFeature.CODEC.fieldOf("feature_true").forGetter(()), (App)PlacedFeature.CODEC.fieldOf("feature_false").forGetter(())).apply((Applicative)paramInstance, RandomBooleanFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<RandomBooleanFeatureConfiguration> CODEC;
/*    */   public final Holder<PlacedFeature> featureTrue;
/*    */   public final Holder<PlacedFeature> featureFalse;
/*    */   
/*    */   public RandomBooleanFeatureConfiguration(Holder<PlacedFeature> paramHolder1, Holder<PlacedFeature> paramHolder2) {
/* 21 */     this.featureTrue = paramHolder1;
/* 22 */     this.featureFalse = paramHolder2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<ConfiguredFeature<?, ?>> getFeatures() {
/* 27 */     return Stream.concat(((PlacedFeature)this.featureTrue.value()).getFeatures(), ((PlacedFeature)this.featureFalse.value()).getFeatures());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\RandomBooleanFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */