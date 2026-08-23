/*    */ package net.minecraft.data.worldgen.placement;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.features.AquaticFeatures;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*    */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*    */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AquaticPlacements
/*    */ {
/*    */   private static List<PlacementModifier> seagrassPlacement(int paramInt) {
/* 24 */     return (List)List.of(
/* 25 */         InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*    */         
/* 27 */         CountPlacement.of(paramInt), 
/* 28 */         BiomeFilter.biome());
/*    */   }
/*    */ 
/*    */   
/* 32 */   public static final ResourceKey<PlacedFeature> SEAGRASS_WARM = PlacementUtils.createKey("seagrass_warm");
/* 33 */   public static final ResourceKey<PlacedFeature> SEAGRASS_NORMAL = PlacementUtils.createKey("seagrass_normal");
/* 34 */   public static final ResourceKey<PlacedFeature> SEAGRASS_COLD = PlacementUtils.createKey("seagrass_cold");
/* 35 */   public static final ResourceKey<PlacedFeature> SEAGRASS_RIVER = PlacementUtils.createKey("seagrass_river");
/* 36 */   public static final ResourceKey<PlacedFeature> SEAGRASS_SWAMP = PlacementUtils.createKey("seagrass_swamp");
/* 37 */   public static final ResourceKey<PlacedFeature> SEAGRASS_DEEP_WARM = PlacementUtils.createKey("seagrass_deep_warm");
/* 38 */   public static final ResourceKey<PlacedFeature> SEAGRASS_DEEP = PlacementUtils.createKey("seagrass_deep");
/* 39 */   public static final ResourceKey<PlacedFeature> SEAGRASS_DEEP_COLD = PlacementUtils.createKey("seagrass_deep_cold");
/* 40 */   public static final ResourceKey<PlacedFeature> SEA_PICKLE = PlacementUtils.createKey("sea_pickle");
/* 41 */   public static final ResourceKey<PlacedFeature> KELP_COLD = PlacementUtils.createKey("kelp_cold");
/* 42 */   public static final ResourceKey<PlacedFeature> KELP_WARM = PlacementUtils.createKey("kelp_warm");
/* 43 */   public static final ResourceKey<PlacedFeature> WARM_OCEAN_VEGETATION = PlacementUtils.createKey("warm_ocean_vegetation");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<PlacedFeature> paramBootstrapContext) {
/* 46 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.CONFIGURED_FEATURE);
/* 47 */     Holder.Reference reference1 = holderGetter.getOrThrow(AquaticFeatures.SEAGRASS_SHORT);
/* 48 */     Holder.Reference reference2 = holderGetter.getOrThrow(AquaticFeatures.SEAGRASS_SLIGHTLY_LESS_SHORT);
/* 49 */     Holder.Reference reference3 = holderGetter.getOrThrow(AquaticFeatures.SEAGRASS_MID);
/* 50 */     Holder.Reference reference4 = holderGetter.getOrThrow(AquaticFeatures.SEAGRASS_TALL);
/* 51 */     Holder.Reference reference5 = holderGetter.getOrThrow(AquaticFeatures.SEA_PICKLE);
/* 52 */     Holder.Reference reference6 = holderGetter.getOrThrow(AquaticFeatures.KELP);
/* 53 */     Holder.Reference reference7 = holderGetter.getOrThrow(AquaticFeatures.WARM_OCEAN_VEGETATION);
/*    */     
/* 55 */     PlacementUtils.register(paramBootstrapContext, SEAGRASS_WARM, (Holder<ConfiguredFeature<?, ?>>)reference1, seagrassPlacement(80));
/* 56 */     PlacementUtils.register(paramBootstrapContext, SEAGRASS_NORMAL, (Holder<ConfiguredFeature<?, ?>>)reference1, seagrassPlacement(48));
/* 57 */     PlacementUtils.register(paramBootstrapContext, SEAGRASS_COLD, (Holder<ConfiguredFeature<?, ?>>)reference1, seagrassPlacement(32));
/* 58 */     PlacementUtils.register(paramBootstrapContext, SEAGRASS_RIVER, (Holder<ConfiguredFeature<?, ?>>)reference2, seagrassPlacement(48));
/* 59 */     PlacementUtils.register(paramBootstrapContext, SEAGRASS_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference3, seagrassPlacement(64));
/* 60 */     PlacementUtils.register(paramBootstrapContext, SEAGRASS_DEEP_WARM, (Holder<ConfiguredFeature<?, ?>>)reference4, seagrassPlacement(80));
/* 61 */     PlacementUtils.register(paramBootstrapContext, SEAGRASS_DEEP, (Holder<ConfiguredFeature<?, ?>>)reference4, seagrassPlacement(48));
/* 62 */     PlacementUtils.register(paramBootstrapContext, SEAGRASS_DEEP_COLD, (Holder<ConfiguredFeature<?, ?>>)reference4, seagrassPlacement(40));
/*    */     
/* 64 */     PlacementUtils.register(paramBootstrapContext, SEA_PICKLE, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[] {
/* 65 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(16), 
/* 66 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*    */           
/* 68 */           (PlacementModifier)BiomeFilter.biome()
/*    */         });
/*    */     
/* 71 */     PlacementUtils.register(paramBootstrapContext, KELP_COLD, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] {
/* 72 */           (PlacementModifier)NoiseBasedCountPlacement.of(120, 80.0D, 0.0D), 
/* 73 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*    */           
/* 75 */           (PlacementModifier)BiomeFilter.biome()
/*    */         });
/*    */     
/* 78 */     PlacementUtils.register(paramBootstrapContext, KELP_WARM, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] {
/* 79 */           (PlacementModifier)NoiseBasedCountPlacement.of(80, 80.0D, 0.0D), 
/* 80 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*    */           
/* 82 */           (PlacementModifier)BiomeFilter.biome()
/*    */         });
/*    */     
/* 85 */     PlacementUtils.register(paramBootstrapContext, WARM_OCEAN_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] {
/* 86 */           (PlacementModifier)NoiseBasedCountPlacement.of(20, 400.0D, 0.0D), 
/* 87 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*    */           
/* 89 */           (PlacementModifier)BiomeFilter.biome()
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\placement\AquaticPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */