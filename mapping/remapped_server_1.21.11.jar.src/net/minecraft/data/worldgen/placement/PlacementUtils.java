/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.WeightedListInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ 
/*     */ public class PlacementUtils
/*     */ {
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> paramBootstrapContext) {
/*  33 */     AquaticPlacements.bootstrap(paramBootstrapContext);
/*  34 */     CavePlacements.bootstrap(paramBootstrapContext);
/*  35 */     EndPlacements.bootstrap(paramBootstrapContext);
/*  36 */     MiscOverworldPlacements.bootstrap(paramBootstrapContext);
/*  37 */     NetherPlacements.bootstrap(paramBootstrapContext);
/*  38 */     OrePlacements.bootstrap(paramBootstrapContext);
/*  39 */     TreePlacements.bootstrap(paramBootstrapContext);
/*  40 */     VegetationPlacements.bootstrap(paramBootstrapContext);
/*  41 */     VillagePlacements.bootstrap(paramBootstrapContext);
/*     */   }
/*     */   
/*  44 */   public static final PlacementModifier HEIGHTMAP = (PlacementModifier)HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING);
/*  45 */   public static final PlacementModifier HEIGHTMAP_NO_LEAVES = (PlacementModifier)HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
/*  46 */   public static final PlacementModifier HEIGHTMAP_TOP_SOLID = (PlacementModifier)HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG);
/*  47 */   public static final PlacementModifier HEIGHTMAP_WORLD_SURFACE = (PlacementModifier)HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG);
/*  48 */   public static final PlacementModifier HEIGHTMAP_OCEAN_FLOOR = (PlacementModifier)HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR);
/*     */   
/*  50 */   public static final PlacementModifier FULL_RANGE = (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top());
/*  51 */   public static final PlacementModifier RANGE_10_10 = (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10));
/*  52 */   public static final PlacementModifier RANGE_8_8 = (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(8), VerticalAnchor.belowTop(8));
/*  53 */   public static final PlacementModifier RANGE_4_4 = (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(4), VerticalAnchor.belowTop(4));
/*  54 */   public static final PlacementModifier RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT = (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256));
/*     */   
/*     */   public static ResourceKey<PlacedFeature> createKey(String paramString) {
/*  57 */     return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */   
/*     */   public static void register(BootstrapContext<PlacedFeature> paramBootstrapContext, ResourceKey<PlacedFeature> paramResourceKey, Holder<ConfiguredFeature<?, ?>> paramHolder, List<PlacementModifier> paramList) {
/*  61 */     paramBootstrapContext.register(paramResourceKey, new PlacedFeature(paramHolder, List.copyOf(paramList)));
/*     */   }
/*     */   
/*     */   public static void register(BootstrapContext<PlacedFeature> paramBootstrapContext, ResourceKey<PlacedFeature> paramResourceKey, Holder<ConfiguredFeature<?, ?>> paramHolder, PlacementModifier... paramVarArgs) {
/*  65 */     register(paramBootstrapContext, paramResourceKey, paramHolder, List.of(paramVarArgs));
/*     */   }
/*     */   
/*     */   public static PlacementModifier countExtra(int paramInt1, float paramFloat, int paramInt2) {
/*  69 */     float f = 1.0F / paramFloat;
/*  70 */     if (Math.abs(f - (int)f) > 1.0E-5F) {
/*  71 */       throw new IllegalStateException("Chance data cannot be represented as list weight");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  76 */     WeightedList weightedList = WeightedList.builder().add(ConstantInt.of(paramInt1), (int)f - 1).add(ConstantInt.of(paramInt1 + paramInt2), 1).build();
/*  77 */     return (PlacementModifier)CountPlacement.of((IntProvider)new WeightedListInt(weightedList));
/*     */   }
/*     */   
/*     */   public static PlacementFilter isEmpty() {
/*  81 */     return (PlacementFilter)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE);
/*     */   }
/*     */   
/*     */   public static BlockPredicateFilter filteredByBlockSurvival(Block paramBlock) {
/*  85 */     return BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(paramBlock.defaultBlockState(), (Vec3i)BlockPos.ZERO));
/*     */   }
/*     */   
/*     */   public static Holder<PlacedFeature> inlinePlaced(Holder<ConfiguredFeature<?, ?>> paramHolder, PlacementModifier... paramVarArgs) {
/*  89 */     return Holder.direct(new PlacedFeature(paramHolder, List.of(paramVarArgs)));
/*     */   }
/*     */   
/*     */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> inlinePlaced(F paramF, FC paramFC, PlacementModifier... paramVarArgs) {
/*  93 */     return inlinePlaced(Holder.direct(new ConfiguredFeature((Feature)paramF, (FeatureConfiguration)paramFC)), paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> onlyWhenEmpty(F paramF, FC paramFC) {
/* 100 */     return filtered(paramF, paramFC, BlockPredicate.ONLY_IN_AIR_PREDICATE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> filtered(F paramF, FC paramFC, BlockPredicate paramBlockPredicate) {
/* 107 */     return inlinePlaced(paramF, paramFC, new PlacementModifier[] { (PlacementModifier)BlockPredicateFilter.forPredicate(paramBlockPredicate) });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\placement\PlacementUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */