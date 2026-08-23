/*    */ package net.minecraft.data.worldgen.features;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*    */ 
/*    */ public class EndFeatures {
/* 13 */   public static final ResourceKey<ConfiguredFeature<?, ?>> END_PLATFORM = FeatureUtils.createKey("end_platform");
/* 14 */   public static final ResourceKey<ConfiguredFeature<?, ?>> END_SPIKE = FeatureUtils.createKey("end_spike");
/* 15 */   public static final ResourceKey<ConfiguredFeature<?, ?>> END_GATEWAY_RETURN = FeatureUtils.createKey("end_gateway_return");
/* 16 */   public static final ResourceKey<ConfiguredFeature<?, ?>> END_GATEWAY_DELAYED = FeatureUtils.createKey("end_gateway_delayed");
/* 17 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CHORUS_PLANT = FeatureUtils.createKey("chorus_plant");
/* 18 */   public static final ResourceKey<ConfiguredFeature<?, ?>> END_ISLAND = FeatureUtils.createKey("end_island");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> paramBootstrapContext) {
/* 21 */     FeatureUtils.register(paramBootstrapContext, END_PLATFORM, Feature.END_PLATFORM);
/* 22 */     FeatureUtils.register(paramBootstrapContext, END_SPIKE, Feature.END_SPIKE, new SpikeConfiguration(false, 
/*    */           
/* 24 */           (List)ImmutableList.of(), null));
/*    */ 
/*    */     
/* 27 */     FeatureUtils.register(paramBootstrapContext, END_GATEWAY_RETURN, Feature.END_GATEWAY, 
/* 28 */         EndGatewayConfiguration.knownExit(ServerLevel.END_SPAWN_POINT, true));
/*    */     
/* 30 */     FeatureUtils.register(paramBootstrapContext, END_GATEWAY_DELAYED, Feature.END_GATEWAY, EndGatewayConfiguration.delayedExitSearch());
/* 31 */     FeatureUtils.register(paramBootstrapContext, CHORUS_PLANT, Feature.CHORUS_PLANT);
/* 32 */     FeatureUtils.register(paramBootstrapContext, END_ISLAND, Feature.END_ISLAND);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\features\EndFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */