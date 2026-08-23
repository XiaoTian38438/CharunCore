/*    */ package net.minecraft.data.worldgen.features;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ 
/*    */ public class FeatureUtils
/*    */ {
/*    */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> paramBootstrapContext) {
/* 24 */     AquaticFeatures.bootstrap(paramBootstrapContext);
/* 25 */     CaveFeatures.bootstrap(paramBootstrapContext);
/* 26 */     EndFeatures.bootstrap(paramBootstrapContext);
/* 27 */     MiscOverworldFeatures.bootstrap(paramBootstrapContext);
/* 28 */     NetherFeatures.bootstrap(paramBootstrapContext);
/* 29 */     OreFeatures.bootstrap(paramBootstrapContext);
/* 30 */     PileFeatures.bootstrap(paramBootstrapContext);
/* 31 */     TreeFeatures.bootstrap(paramBootstrapContext);
/* 32 */     VegetationFeatures.bootstrap(paramBootstrapContext);
/*    */   }
/*    */   
/*    */   private static BlockPredicate simplePatchPredicate(List<Block> paramList) {
/*    */     BlockPredicate blockPredicate;
/* 37 */     if (!paramList.isEmpty()) {
/* 38 */       blockPredicate = BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), paramList));
/*    */     } else {
/* 40 */       blockPredicate = BlockPredicate.ONLY_IN_AIR_PREDICATE;
/*    */     } 
/* 42 */     return blockPredicate;
/*    */   }
/*    */   
/*    */   public static RandomPatchConfiguration simpleRandomPatchConfiguration(int paramInt, Holder<PlacedFeature> paramHolder) {
/* 46 */     return new RandomPatchConfiguration(paramInt, 7, 3, paramHolder);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F paramF, FC paramFC, List<Block> paramList, int paramInt) {
/* 55 */     return simpleRandomPatchConfiguration(paramInt, PlacementUtils.filtered((Feature)paramF, (FeatureConfiguration)paramFC, simplePatchPredicate(paramList)));
/*    */   }
/*    */   
/*    */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F paramF, FC paramFC, List<Block> paramList) {
/* 59 */     return simplePatchConfiguration(paramF, paramFC, paramList, 96);
/*    */   }
/*    */   
/*    */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F paramF, FC paramFC) {
/* 63 */     return simplePatchConfiguration(paramF, paramFC, List.of(), 96);
/*    */   }
/*    */   
/*    */   public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String paramString) {
/* 67 */     return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   public static void register(BootstrapContext<ConfiguredFeature<?, ?>> paramBootstrapContext, ResourceKey<ConfiguredFeature<?, ?>> paramResourceKey, Feature<NoneFeatureConfiguration> paramFeature) {
/* 71 */     register(paramBootstrapContext, paramResourceKey, paramFeature, FeatureConfiguration.NONE);
/*    */   }
/*    */   
/*    */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> paramBootstrapContext, ResourceKey<ConfiguredFeature<?, ?>> paramResourceKey, F paramF, FC paramFC) {
/* 75 */     paramBootstrapContext.register(paramResourceKey, new ConfiguredFeature((Feature)paramF, (FeatureConfiguration)paramFC));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\features\FeatureUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */