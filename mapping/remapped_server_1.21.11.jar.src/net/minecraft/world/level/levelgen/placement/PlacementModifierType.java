/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface PlacementModifierType<P extends PlacementModifier>
/*    */ {
/*  9 */   public static final PlacementModifierType<BlockPredicateFilter> BLOCK_PREDICATE_FILTER = register("block_predicate_filter", BlockPredicateFilter.CODEC);
/* 10 */   public static final PlacementModifierType<RarityFilter> RARITY_FILTER = register("rarity_filter", RarityFilter.CODEC);
/* 11 */   public static final PlacementModifierType<SurfaceRelativeThresholdFilter> SURFACE_RELATIVE_THRESHOLD_FILTER = register("surface_relative_threshold_filter", SurfaceRelativeThresholdFilter.CODEC);
/* 12 */   public static final PlacementModifierType<SurfaceWaterDepthFilter> SURFACE_WATER_DEPTH_FILTER = register("surface_water_depth_filter", SurfaceWaterDepthFilter.CODEC);
/* 13 */   public static final PlacementModifierType<BiomeFilter> BIOME_FILTER = register("biome", BiomeFilter.CODEC);
/*    */ 
/*    */   
/* 16 */   public static final PlacementModifierType<CountPlacement> COUNT = register("count", CountPlacement.CODEC);
/* 17 */   public static final PlacementModifierType<NoiseBasedCountPlacement> NOISE_BASED_COUNT = register("noise_based_count", NoiseBasedCountPlacement.CODEC);
/* 18 */   public static final PlacementModifierType<NoiseThresholdCountPlacement> NOISE_THRESHOLD_COUNT = register("noise_threshold_count", NoiseThresholdCountPlacement.CODEC);
/*    */ 
/*    */   
/* 21 */   public static final PlacementModifierType<CountOnEveryLayerPlacement> COUNT_ON_EVERY_LAYER = register("count_on_every_layer", CountOnEveryLayerPlacement.CODEC);
/*    */ 
/*    */   
/* 24 */   public static final PlacementModifierType<EnvironmentScanPlacement> ENVIRONMENT_SCAN = register("environment_scan", EnvironmentScanPlacement.CODEC);
/* 25 */   public static final PlacementModifierType<HeightmapPlacement> HEIGHTMAP = register("heightmap", HeightmapPlacement.CODEC);
/* 26 */   public static final PlacementModifierType<HeightRangePlacement> HEIGHT_RANGE = register("height_range", HeightRangePlacement.CODEC);
/*    */ 
/*    */   
/* 29 */   public static final PlacementModifierType<InSquarePlacement> IN_SQUARE = register("in_square", InSquarePlacement.CODEC);
/*    */ 
/*    */   
/* 32 */   public static final PlacementModifierType<RandomOffsetPlacement> RANDOM_OFFSET = register("random_offset", RandomOffsetPlacement.CODEC);
/*    */ 
/*    */   
/* 35 */   public static final PlacementModifierType<FixedPlacement> FIXED_PLACEMENT = register("fixed_placement", FixedPlacement.CODEC);
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   private static <P extends PlacementModifier> PlacementModifierType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 40 */     return (PlacementModifierType<P>)Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, paramString, () -> paramMapCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\PlacementModifierType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */