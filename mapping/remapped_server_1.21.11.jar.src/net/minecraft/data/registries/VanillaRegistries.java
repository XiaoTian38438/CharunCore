/*     */ package net.minecraft.data.registries;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistrySetBuilder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.Carvers;
/*     */ import net.minecraft.data.worldgen.DimensionTypes;
/*     */ import net.minecraft.data.worldgen.NoiseData;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.data.worldgen.ProcessorLists;
/*     */ import net.minecraft.data.worldgen.StructureSets;
/*     */ import net.minecraft.data.worldgen.Structures;
/*     */ import net.minecraft.data.worldgen.biome.BiomeData;
/*     */ import net.minecraft.data.worldgen.features.FeatureUtils;
/*     */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*     */ import net.minecraft.gametest.framework.GameTestEnvironments;
/*     */ import net.minecraft.gametest.framework.GameTestInstances;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.dialog.Dialogs;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariants;
/*     */ import net.minecraft.world.entity.animal.cow.CowVariants;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariants;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariants;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariants;
/*     */ import net.minecraft.world.entity.animal.pig.PigVariants;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariants;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariants;
/*     */ import net.minecraft.world.item.Instruments;
/*     */ import net.minecraft.world.item.JukeboxSongs;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterials;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPatterns;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
/*     */ import net.minecraft.world.level.block.entity.BannerPatterns;
/*     */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfigs;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.NoiseRouterData;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.timeline.Timelines;
/*     */ 
/*     */ public class VanillaRegistries {
/*  58 */   private static final RegistrySetBuilder BUILDER = (new RegistrySetBuilder())
/*  59 */     .add(Registries.DIMENSION_TYPE, DimensionTypes::bootstrap)
/*  60 */     .add(Registries.CONFIGURED_CARVER, Carvers::bootstrap)
/*  61 */     .add(Registries.CONFIGURED_FEATURE, FeatureUtils::bootstrap)
/*  62 */     .add(Registries.PLACED_FEATURE, PlacementUtils::bootstrap)
/*  63 */     .add(Registries.STRUCTURE, Structures::bootstrap)
/*  64 */     .add(Registries.STRUCTURE_SET, StructureSets::bootstrap)
/*  65 */     .add(Registries.PROCESSOR_LIST, ProcessorLists::bootstrap)
/*  66 */     .add(Registries.TEMPLATE_POOL, Pools::bootstrap)
/*  67 */     .add(Registries.BIOME, BiomeData::bootstrap)
/*  68 */     .add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterLists::bootstrap)
/*  69 */     .add(Registries.NOISE, NoiseData::bootstrap)
/*  70 */     .add(Registries.DENSITY_FUNCTION, NoiseRouterData::bootstrap)
/*  71 */     .add(Registries.NOISE_SETTINGS, NoiseGeneratorSettings::bootstrap)
/*  72 */     .add(Registries.WORLD_PRESET, WorldPresets::bootstrap)
/*  73 */     .add(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPresets::bootstrap)
/*  74 */     .add(Registries.CHAT_TYPE, ChatType::bootstrap)
/*  75 */     .add(Registries.TRIM_PATTERN, TrimPatterns::bootstrap)
/*  76 */     .add(Registries.TRIM_MATERIAL, TrimMaterials::bootstrap)
/*  77 */     .add(Registries.TRIAL_SPAWNER_CONFIG, TrialSpawnerConfigs::bootstrap)
/*  78 */     .add(Registries.WOLF_VARIANT, WolfVariants::bootstrap)
/*  79 */     .add(Registries.WOLF_SOUND_VARIANT, WolfSoundVariants::bootstrap)
/*  80 */     .add(Registries.PAINTING_VARIANT, PaintingVariants::bootstrap)
/*  81 */     .add(Registries.DAMAGE_TYPE, DamageTypes::bootstrap)
/*  82 */     .add(Registries.BANNER_PATTERN, BannerPatterns::bootstrap)
/*  83 */     .add(Registries.ENCHANTMENT, Enchantments::bootstrap)
/*  84 */     .add(Registries.ENCHANTMENT_PROVIDER, VanillaEnchantmentProviders::bootstrap)
/*  85 */     .add(Registries.JUKEBOX_SONG, JukeboxSongs::bootstrap)
/*  86 */     .add(Registries.INSTRUMENT, Instruments::bootstrap)
/*  87 */     .add(Registries.PIG_VARIANT, PigVariants::bootstrap)
/*  88 */     .add(Registries.COW_VARIANT, CowVariants::bootstrap)
/*  89 */     .add(Registries.CHICKEN_VARIANT, ChickenVariants::bootstrap)
/*  90 */     .add(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariants::bootstrap)
/*  91 */     .add(Registries.TEST_ENVIRONMENT, GameTestEnvironments::bootstrap)
/*  92 */     .add(Registries.TEST_INSTANCE, GameTestInstances::bootstrap)
/*  93 */     .add(Registries.FROG_VARIANT, FrogVariants::bootstrap)
/*  94 */     .add(Registries.CAT_VARIANT, CatVariants::bootstrap)
/*  95 */     .add(Registries.DIALOG, Dialogs::bootstrap)
/*  96 */     .add(Registries.TIMELINE, Timelines::bootstrap);
/*     */   
/*     */   private static void validateThatAllBiomeFeaturesHaveBiomeFilter(HolderLookup.Provider paramProvider) {
/*  99 */     validateThatAllBiomeFeaturesHaveBiomeFilter((HolderGetter<PlacedFeature>)paramProvider.lookupOrThrow(Registries.PLACED_FEATURE), (HolderLookup<Biome>)paramProvider.lookupOrThrow(Registries.BIOME));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void validateThatAllBiomeFeaturesHaveBiomeFilter(HolderGetter<PlacedFeature> paramHolderGetter, HolderLookup<Biome> paramHolderLookup) {
/* 108 */     paramHolderLookup.listElements().forEach(paramReference -> {
/*     */           Identifier identifier = paramReference.key().identifier();
/*     */           List list = ((Biome)paramReference.value()).getGenerationSettings().features();
/*     */           list.stream().flatMap(HolderSet::stream).forEach(());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validatePlacedFeature(PlacedFeature paramPlacedFeature) {
/* 129 */     return paramPlacedFeature.placement().contains(BiomeFilter.biome());
/*     */   }
/*     */   
/*     */   public static HolderLookup.Provider createLookup() {
/* 133 */     RegistryAccess.Frozen frozen = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
/* 134 */     HolderLookup.Provider provider = BUILDER.build((RegistryAccess)frozen);
/* 135 */     validateThatAllBiomeFeaturesHaveBiomeFilter(provider);
/* 136 */     return provider;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\registries\VanillaRegistries.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */