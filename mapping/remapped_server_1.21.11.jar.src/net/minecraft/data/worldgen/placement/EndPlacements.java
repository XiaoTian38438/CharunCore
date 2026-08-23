/*    */ package net.minecraft.data.worldgen.placement;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.features.EndFeatures;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*    */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.FixedPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*    */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*    */ 
/*    */ public class EndPlacements {
/* 23 */   public static final ResourceKey<PlacedFeature> END_PLATFORM = PlacementUtils.createKey("end_platform");
/* 24 */   public static final ResourceKey<PlacedFeature> END_SPIKE = PlacementUtils.createKey("end_spike");
/* 25 */   public static final ResourceKey<PlacedFeature> END_GATEWAY_RETURN = PlacementUtils.createKey("end_gateway_return");
/* 26 */   public static final ResourceKey<PlacedFeature> CHORUS_PLANT = PlacementUtils.createKey("chorus_plant");
/* 27 */   public static final ResourceKey<PlacedFeature> END_ISLAND_DECORATED = PlacementUtils.createKey("end_island_decorated");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<PlacedFeature> paramBootstrapContext) {
/* 30 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.CONFIGURED_FEATURE);
/* 31 */     Holder.Reference reference1 = holderGetter.getOrThrow(EndFeatures.END_PLATFORM);
/* 32 */     Holder.Reference reference2 = holderGetter.getOrThrow(EndFeatures.END_SPIKE);
/* 33 */     Holder.Reference reference3 = holderGetter.getOrThrow(EndFeatures.END_GATEWAY_RETURN);
/* 34 */     Holder.Reference reference4 = holderGetter.getOrThrow(EndFeatures.CHORUS_PLANT);
/* 35 */     Holder.Reference reference5 = holderGetter.getOrThrow(EndFeatures.END_ISLAND);
/*    */     
/* 37 */     PlacementUtils.register(paramBootstrapContext, END_PLATFORM, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] {
/* 38 */           (PlacementModifier)FixedPlacement.of(new BlockPos[] { ServerLevel.END_SPAWN_POINT.below()
/* 39 */             }), (PlacementModifier)BiomeFilter.biome()
/*    */         });
/* 41 */     PlacementUtils.register(paramBootstrapContext, END_SPIKE, (Holder<ConfiguredFeature<?, ?>>)reference2, new PlacementModifier[] {
/* 42 */           (PlacementModifier)BiomeFilter.biome()
/*    */         });
/* 44 */     PlacementUtils.register(paramBootstrapContext, END_GATEWAY_RETURN, (Holder<ConfiguredFeature<?, ?>>)reference3, new PlacementModifier[] {
/* 45 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(700), 
/* 46 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*    */           
/* 48 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)UniformInt.of(3, 9)), 
/* 49 */           (PlacementModifier)BiomeFilter.biome()
/*    */         });
/* 51 */     PlacementUtils.register(paramBootstrapContext, CHORUS_PLANT, (Holder<ConfiguredFeature<?, ?>>)reference4, new PlacementModifier[] {
/* 52 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(0, 4)), 
/* 53 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*    */           
/* 55 */           (PlacementModifier)BiomeFilter.biome()
/*    */         });
/* 57 */     PlacementUtils.register(paramBootstrapContext, END_ISLAND_DECORATED, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[] {
/* 58 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(14), 
/* 59 */           PlacementUtils.countExtra(1, 0.25F, 1), 
/* 60 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 61 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(55), VerticalAnchor.absolute(70)), 
/* 62 */           (PlacementModifier)BiomeFilter.biome()
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\placement\EndPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */