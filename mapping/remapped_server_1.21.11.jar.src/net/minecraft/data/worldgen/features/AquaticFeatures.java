/*    */ package net.minecraft.data.worldgen.features;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
/*    */ 
/*    */ public class AquaticFeatures {
/* 15 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SEAGRASS_SHORT = FeatureUtils.createKey("seagrass_short");
/* 16 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SEAGRASS_SLIGHTLY_LESS_SHORT = FeatureUtils.createKey("seagrass_slightly_less_short");
/* 17 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SEAGRASS_MID = FeatureUtils.createKey("seagrass_mid");
/* 18 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SEAGRASS_TALL = FeatureUtils.createKey("seagrass_tall");
/*    */   
/* 20 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SEA_PICKLE = FeatureUtils.createKey("sea_pickle");
/*    */   
/* 22 */   public static final ResourceKey<ConfiguredFeature<?, ?>> KELP = FeatureUtils.createKey("kelp");
/*    */   
/* 24 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WARM_OCEAN_VEGETATION = FeatureUtils.createKey("warm_ocean_vegetation");
/*    */ 
/*    */ 
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> paramBootstrapContext) {
/* 29 */     FeatureUtils.register(paramBootstrapContext, SEAGRASS_SHORT, Feature.SEAGRASS, new ProbabilityFeatureConfiguration(0.3F));
/* 30 */     FeatureUtils.register(paramBootstrapContext, SEAGRASS_SLIGHTLY_LESS_SHORT, Feature.SEAGRASS, new ProbabilityFeatureConfiguration(0.4F));
/* 31 */     FeatureUtils.register(paramBootstrapContext, SEAGRASS_MID, Feature.SEAGRASS, new ProbabilityFeatureConfiguration(0.6F));
/* 32 */     FeatureUtils.register(paramBootstrapContext, SEAGRASS_TALL, Feature.SEAGRASS, new ProbabilityFeatureConfiguration(0.8F));
/*    */     
/* 34 */     FeatureUtils.register(paramBootstrapContext, SEA_PICKLE, Feature.SEA_PICKLE, new CountConfiguration(20));
/*    */     
/* 36 */     FeatureUtils.register(paramBootstrapContext, KELP, Feature.KELP);
/*    */     
/* 38 */     FeatureUtils.register(paramBootstrapContext, WARM_OCEAN_VEGETATION, Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(
/* 39 */           (HolderSet)HolderSet.direct(new Holder[] {
/* 40 */               PlacementUtils.inlinePlaced(Feature.CORAL_TREE, (FeatureConfiguration)FeatureConfiguration.NONE, new net.minecraft.world.level.levelgen.placement.PlacementModifier[0]), 
/* 41 */               PlacementUtils.inlinePlaced(Feature.CORAL_CLAW, (FeatureConfiguration)FeatureConfiguration.NONE, new net.minecraft.world.level.levelgen.placement.PlacementModifier[0]), 
/* 42 */               PlacementUtils.inlinePlaced(Feature.CORAL_MUSHROOM, (FeatureConfiguration)FeatureConfiguration.NONE, new net.minecraft.world.level.levelgen.placement.PlacementModifier[0])
/*    */             })));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\features\AquaticFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */