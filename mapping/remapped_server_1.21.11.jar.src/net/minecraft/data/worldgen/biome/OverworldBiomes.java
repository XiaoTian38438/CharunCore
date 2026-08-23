/*      */ package net.minecraft.data.worldgen.biome;
/*      */ 
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderGetter;
/*      */ import net.minecraft.data.worldgen.BiomeDefaultFeatures;
/*      */ import net.minecraft.data.worldgen.Carvers;
/*      */ import net.minecraft.data.worldgen.placement.AquaticPlacements;
/*      */ import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
/*      */ import net.minecraft.data.worldgen.placement.VegetationPlacements;
/*      */ import net.minecraft.sounds.Musics;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.attribute.BackgroundMusic;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*      */ import net.minecraft.world.attribute.modifier.FloatModifier;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.MobCategory;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*      */ import net.minecraft.world.level.biome.BiomeSpecialEffects;
/*      */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*      */ import net.minecraft.world.level.levelgen.GenerationStep;
/*      */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*      */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*      */ 
/*      */ public class OverworldBiomes {
/*      */   protected static final int NORMAL_WATER_COLOR = 4159204;
/*      */   
/*      */   public static int calculateSkyColor(float paramFloat) {
/*   33 */     float f = paramFloat;
/*   34 */     f /= 3.0F;
/*   35 */     f = Mth.clamp(f, -1.0F, 1.0F);
/*   36 */     return ARGB.opaque(Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F));
/*      */   }
/*      */   private static final int DARK_DRY_FOLIAGE_COLOR = 8082228; public static final int SWAMP_SKELETON_WEIGHT = 70;
/*      */   private static Biome.BiomeBuilder baseBiome(float paramFloat1, float paramFloat2) {
/*   40 */     return (new Biome.BiomeBuilder())
/*   41 */       .hasPrecipitation(true)
/*   42 */       .temperature(paramFloat1)
/*   43 */       .downfall(paramFloat2)
/*   44 */       .setAttribute(EnvironmentAttributes.SKY_COLOR, Integer.valueOf(calculateSkyColor(paramFloat1)))
/*   45 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*   46 */         .waterColor(4159204)
/*   47 */         .build());
/*      */   }
/*      */ 
/*      */   
/*      */   private static void globalOverworldGeneration(BiomeGenerationSettings.Builder paramBuilder) {
/*   52 */     BiomeDefaultFeatures.addDefaultCarversAndLakes(paramBuilder);
/*   53 */     BiomeDefaultFeatures.addDefaultCrystalFormations(paramBuilder);
/*   54 */     BiomeDefaultFeatures.addDefaultMonsterRoom(paramBuilder);
/*   55 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(paramBuilder);
/*   56 */     BiomeDefaultFeatures.addDefaultSprings(paramBuilder);
/*   57 */     BiomeDefaultFeatures.addSurfaceFreezing(paramBuilder);
/*      */   }
/*      */   
/*      */   public static Biome oldGrowthTaiga(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*   61 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*   62 */     BiomeDefaultFeatures.farmAnimals(builder);
/*   63 */     builder.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 4));
/*   64 */     builder.addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3));
/*   65 */     builder.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.FOX, 2, 4));
/*   66 */     if (paramBoolean) {
/*   67 */       BiomeDefaultFeatures.commonSpawns(builder);
/*      */     } else {
/*   69 */       BiomeDefaultFeatures.caveSpawns(builder);
/*   70 */       BiomeDefaultFeatures.monsters(builder, 100, 25, 0, 100, false);
/*      */     } 
/*      */     
/*   73 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*   75 */     globalOverworldGeneration(builder1);
/*   76 */     BiomeDefaultFeatures.addMossyStoneBlock(builder1);
/*   77 */     BiomeDefaultFeatures.addFerns(builder1);
/*   78 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*   79 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*   80 */     builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, paramBoolean ? VegetationPlacements.TREES_OLD_GROWTH_SPRUCE_TAIGA : VegetationPlacements.TREES_OLD_GROWTH_PINE_TAIGA);
/*   81 */     BiomeDefaultFeatures.addDefaultFlowers(builder1);
/*   82 */     BiomeDefaultFeatures.addGiantTaigaVegetation(builder1);
/*   83 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/*   84 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder1, true);
/*   85 */     BiomeDefaultFeatures.addCommonBerryBushes(builder1);
/*      */     
/*   87 */     return baseBiome(paramBoolean ? 0.25F : 0.3F, 0.8F)
/*   88 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_OLD_GROWTH_TAIGA))
/*   89 */       .mobSpawnSettings(builder.build())
/*   90 */       .generationSettings(builder1.build())
/*   91 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome sparseJungle(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*   95 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*   96 */     BiomeDefaultFeatures.baseJungleSpawns(builder);
/*   97 */     builder.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 2, 4));
/*      */     
/*   99 */     return baseJungle(paramHolderGetter, paramHolderGetter1, 0.8F, false, true, false)
/*  100 */       .mobSpawnSettings(builder.build())
/*  101 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_SPARSE_JUNGLE))
/*  102 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome jungle(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  106 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  107 */     BiomeDefaultFeatures.baseJungleSpawns(builder);
/*  108 */     builder.addSpawn(MobCategory.CREATURE, 40, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 1, 2))
/*  109 */       .addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 1, 3))
/*  110 */       .addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.PANDA, 1, 2));
/*      */     
/*  112 */     return baseJungle(paramHolderGetter, paramHolderGetter1, 0.9F, false, false, true)
/*  113 */       .mobSpawnSettings(builder.build())
/*  114 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_JUNGLE))
/*  115 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  116 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome bambooJungle(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  120 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  121 */     BiomeDefaultFeatures.baseJungleSpawns(builder);
/*  122 */     builder.addSpawn(MobCategory.CREATURE, 40, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 1, 2))
/*  123 */       .addSpawn(MobCategory.CREATURE, 80, new MobSpawnSettings.SpawnerData(EntityType.PANDA, 1, 2))
/*  124 */       .addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 1, 1));
/*      */     
/*  126 */     return baseJungle(paramHolderGetter, paramHolderGetter1, 0.9F, true, false, true)
/*  127 */       .mobSpawnSettings(builder.build())
/*  128 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_BAMBOO_JUNGLE))
/*  129 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  130 */       .build();
/*      */   }
/*      */   
/*      */   private static Biome.BiomeBuilder baseJungle(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, float paramFloat, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*  134 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  136 */     globalOverworldGeneration(builder);
/*  137 */     BiomeDefaultFeatures.addDefaultOres(builder);
/*  138 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder);
/*  139 */     if (paramBoolean1) {
/*  140 */       BiomeDefaultFeatures.addBambooVegetation(builder);
/*      */     } else {
/*  142 */       if (paramBoolean3) {
/*  143 */         BiomeDefaultFeatures.addLightBambooVegetation(builder);
/*      */       }
/*  145 */       if (paramBoolean2) {
/*  146 */         BiomeDefaultFeatures.addSparseJungleTrees(builder);
/*      */       } else {
/*  148 */         BiomeDefaultFeatures.addJungleTrees(builder);
/*      */       } 
/*      */     } 
/*  151 */     BiomeDefaultFeatures.addWarmFlowers(builder);
/*  152 */     BiomeDefaultFeatures.addJungleGrass(builder);
/*  153 */     BiomeDefaultFeatures.addDefaultMushrooms(builder);
/*  154 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder, true);
/*  155 */     BiomeDefaultFeatures.addJungleVines(builder);
/*  156 */     if (paramBoolean2) {
/*  157 */       BiomeDefaultFeatures.addSparseJungleMelons(builder);
/*      */     } else {
/*  159 */       BiomeDefaultFeatures.addJungleMelons(builder);
/*      */     } 
/*      */     
/*  162 */     return baseBiome(0.95F, paramFloat)
/*  163 */       .generationSettings(builder.build());
/*      */   }
/*      */   
/*      */   public static Biome windsweptHills(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*  167 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  168 */     BiomeDefaultFeatures.farmAnimals(builder);
/*  169 */     builder.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 4, 6));
/*  170 */     BiomeDefaultFeatures.commonSpawns(builder);
/*      */     
/*  172 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  174 */     globalOverworldGeneration(builder1);
/*  175 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  176 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*  177 */     if (paramBoolean) {
/*  178 */       BiomeDefaultFeatures.addMountainForestTrees(builder1);
/*      */     } else {
/*  180 */       BiomeDefaultFeatures.addMountainTrees(builder1);
/*      */     } 
/*  182 */     BiomeDefaultFeatures.addBushes(builder1);
/*  183 */     BiomeDefaultFeatures.addDefaultFlowers(builder1);
/*  184 */     BiomeDefaultFeatures.addDefaultGrass(builder1);
/*  185 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/*  186 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder1, true);
/*  187 */     BiomeDefaultFeatures.addExtraEmeralds(builder1);
/*  188 */     BiomeDefaultFeatures.addInfestedStone(builder1);
/*      */     
/*  190 */     return baseBiome(0.2F, 0.3F)
/*  191 */       .mobSpawnSettings(builder.build())
/*  192 */       .generationSettings(builder1.build())
/*  193 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome desert(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  197 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  198 */     BiomeDefaultFeatures.desertSpawns(builder);
/*      */     
/*  200 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*  201 */     BiomeDefaultFeatures.addFossilDecoration(builder1);
/*      */     
/*  203 */     globalOverworldGeneration(builder1);
/*  204 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  205 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*  206 */     BiomeDefaultFeatures.addDefaultFlowers(builder1);
/*  207 */     BiomeDefaultFeatures.addDefaultGrass(builder1);
/*  208 */     BiomeDefaultFeatures.addDesertVegetation(builder1);
/*  209 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/*  210 */     BiomeDefaultFeatures.addDesertExtraVegetation(builder1);
/*  211 */     BiomeDefaultFeatures.addDesertExtraDecoration(builder1);
/*      */     
/*  213 */     return baseBiome(2.0F, 0.0F)
/*  214 */       .hasPrecipitation(false)
/*  215 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_DESERT))
/*  216 */       .setAttribute(EnvironmentAttributes.SNOW_GOLEM_MELTS, Boolean.valueOf(true))
/*  217 */       .mobSpawnSettings(builder.build())
/*  218 */       .generationSettings(builder1.build())
/*  219 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome plains(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*  223 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  224 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  226 */     globalOverworldGeneration(builder1);
/*      */     
/*  228 */     if (paramBoolean2) {
/*  229 */       builder.creatureGenerationProbability(0.07F);
/*  230 */       BiomeDefaultFeatures.snowySpawns(builder, !paramBoolean3);
/*      */       
/*  232 */       if (paramBoolean3) {
/*  233 */         builder1.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.ICE_SPIKE);
/*  234 */         builder1.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.ICE_PATCH);
/*      */       } 
/*      */     } else {
/*  237 */       BiomeDefaultFeatures.plainsSpawns(builder);
/*  238 */       BiomeDefaultFeatures.addPlainGrass(builder1);
/*  239 */       if (paramBoolean1) {
/*  240 */         builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUNFLOWER);
/*      */       } else {
/*  242 */         BiomeDefaultFeatures.addBushes(builder1);
/*      */       } 
/*      */     } 
/*      */     
/*  246 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  247 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*      */     
/*  249 */     if (paramBoolean2) {
/*  250 */       BiomeDefaultFeatures.addSnowyTrees(builder1);
/*  251 */       BiomeDefaultFeatures.addDefaultFlowers(builder1);
/*  252 */       BiomeDefaultFeatures.addDefaultGrass(builder1);
/*      */     } else {
/*  254 */       BiomeDefaultFeatures.addPlainVegetation(builder1);
/*      */     } 
/*      */     
/*  257 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/*  258 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder1, true);
/*      */     
/*  260 */     return baseBiome(paramBoolean2 ? 0.0F : 0.8F, paramBoolean2 ? 0.5F : 0.4F)
/*  261 */       .mobSpawnSettings(builder.build())
/*  262 */       .generationSettings(builder1.build())
/*  263 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome mushroomFields(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  267 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  268 */     BiomeDefaultFeatures.mooshroomSpawns(builder);
/*      */     
/*  270 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  272 */     globalOverworldGeneration(builder1);
/*  273 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  274 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*  275 */     BiomeDefaultFeatures.addMushroomFieldVegetation(builder1);
/*  276 */     BiomeDefaultFeatures.addNearWaterVegetation(builder1);
/*      */     
/*  278 */     return baseBiome(0.9F, 1.0F)
/*  279 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  280 */       .setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, Boolean.valueOf(false))
/*  281 */       .mobSpawnSettings(builder.build())
/*  282 */       .generationSettings(builder1.build())
/*  283 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome savanna(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean1, boolean paramBoolean2) {
/*  287 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  289 */     globalOverworldGeneration(builder);
/*  290 */     if (!paramBoolean1) {
/*  291 */       BiomeDefaultFeatures.addSavannaGrass(builder);
/*      */     }
/*  293 */     BiomeDefaultFeatures.addDefaultOres(builder);
/*  294 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder);
/*  295 */     if (paramBoolean1) {
/*  296 */       BiomeDefaultFeatures.addShatteredSavannaTrees(builder);
/*  297 */       BiomeDefaultFeatures.addDefaultFlowers(builder);
/*  298 */       BiomeDefaultFeatures.addShatteredSavannaGrass(builder);
/*      */     } else {
/*  300 */       BiomeDefaultFeatures.addSavannaTrees(builder);
/*  301 */       BiomeDefaultFeatures.addWarmFlowers(builder);
/*  302 */       BiomeDefaultFeatures.addSavannaExtraGrass(builder);
/*      */     } 
/*  304 */     BiomeDefaultFeatures.addDefaultMushrooms(builder);
/*  305 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder, true);
/*      */     
/*  307 */     MobSpawnSettings.Builder builder1 = new MobSpawnSettings.Builder();
/*  308 */     BiomeDefaultFeatures.farmAnimals(builder1);
/*  309 */     builder1.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 2, 6))
/*  310 */       .addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1))
/*  311 */       .addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.ARMADILLO, 2, 3));
/*      */     
/*  313 */     BiomeDefaultFeatures.commonSpawnWithZombieHorse(builder1);
/*      */     
/*  315 */     if (paramBoolean2) {
/*  316 */       builder1.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 4, 4));
/*  317 */       builder1.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 8));
/*      */     } 
/*      */     
/*  320 */     return baseBiome(2.0F, 0.0F)
/*  321 */       .hasPrecipitation(false)
/*  322 */       .setAttribute(EnvironmentAttributes.SNOW_GOLEM_MELTS, Boolean.valueOf(true))
/*  323 */       .mobSpawnSettings(builder1.build())
/*  324 */       .generationSettings(builder.build())
/*  325 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome badlands(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*  329 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  330 */     BiomeDefaultFeatures.farmAnimals(builder);
/*  331 */     BiomeDefaultFeatures.commonSpawns(builder);
/*  332 */     builder.addSpawn(MobCategory.CREATURE, 6, new MobSpawnSettings.SpawnerData(EntityType.ARMADILLO, 1, 2));
/*  333 */     builder.creatureGenerationProbability(0.03F);
/*  334 */     if (paramBoolean) {
/*  335 */       builder.addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 8));
/*  336 */       builder.creatureGenerationProbability(0.04F);
/*      */     } 
/*  338 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  340 */     globalOverworldGeneration(builder1);
/*  341 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  342 */     BiomeDefaultFeatures.addExtraGold(builder1);
/*  343 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*  344 */     if (paramBoolean) {
/*  345 */       BiomeDefaultFeatures.addBadlandsTrees(builder1);
/*      */     }
/*  347 */     BiomeDefaultFeatures.addBadlandGrass(builder1);
/*  348 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/*  349 */     BiomeDefaultFeatures.addBadlandExtraVegetation(builder1);
/*  350 */     return baseBiome(2.0F, 0.0F)
/*  351 */       .hasPrecipitation(false)
/*  352 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_BADLANDS))
/*  353 */       .setAttribute(EnvironmentAttributes.SNOW_GOLEM_MELTS, Boolean.valueOf(true))
/*  354 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  355 */         .waterColor(4159204)
/*  356 */         .foliageColorOverride(10387789)
/*  357 */         .grassColorOverride(9470285)
/*  358 */         .build())
/*      */       
/*  360 */       .mobSpawnSettings(builder.build())
/*  361 */       .generationSettings(builder1.build())
/*  362 */       .build();
/*      */   }
/*      */   
/*      */   private static Biome.BiomeBuilder baseOcean() {
/*  366 */     return baseBiome(0.5F, 0.5F)
/*  367 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD.withUnderwater(Musics.UNDER_WATER));
/*      */   }
/*      */   
/*      */   private static BiomeGenerationSettings.Builder baseOceanGeneration(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  371 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  373 */     globalOverworldGeneration(builder);
/*  374 */     BiomeDefaultFeatures.addDefaultOres(builder);
/*  375 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder);
/*  376 */     BiomeDefaultFeatures.addWaterTrees(builder);
/*  377 */     BiomeDefaultFeatures.addDefaultFlowers(builder);
/*  378 */     BiomeDefaultFeatures.addDefaultGrass(builder);
/*  379 */     BiomeDefaultFeatures.addDefaultMushrooms(builder);
/*  380 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder, true);
/*  381 */     return builder;
/*      */   }
/*      */   
/*      */   public static Biome coldOcean(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*  385 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  386 */     BiomeDefaultFeatures.oceanSpawns(builder, 3, 4, 15);
/*  387 */     builder.addSpawn(MobCategory.WATER_AMBIENT, 15, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 1, 5));
/*  388 */     builder.addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.NAUTILUS, 1, 1));
/*      */     
/*  390 */     BiomeGenerationSettings.Builder builder1 = baseOceanGeneration(paramHolderGetter, paramHolderGetter1);
/*  391 */     builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, paramBoolean ? AquaticPlacements.SEAGRASS_DEEP_COLD : AquaticPlacements.SEAGRASS_COLD);
/*  392 */     BiomeDefaultFeatures.addColdOceanExtraVegetation(builder1);
/*      */     
/*  394 */     return baseOcean()
/*  395 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  396 */         .waterColor(4020182)
/*  397 */         .build())
/*      */       
/*  399 */       .mobSpawnSettings(builder.build())
/*  400 */       .generationSettings(builder1.build())
/*  401 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome ocean(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*  405 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  406 */     BiomeDefaultFeatures.oceanSpawns(builder, 1, 4, 10);
/*  407 */     builder.addSpawn(MobCategory.WATER_CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 1, 2))
/*  408 */       .addSpawn(MobCategory.WATER_CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.NAUTILUS, 1, 1));
/*      */     
/*  410 */     BiomeGenerationSettings.Builder builder1 = baseOceanGeneration(paramHolderGetter, paramHolderGetter1);
/*  411 */     builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, paramBoolean ? AquaticPlacements.SEAGRASS_DEEP : AquaticPlacements.SEAGRASS_NORMAL);
/*  412 */     BiomeDefaultFeatures.addColdOceanExtraVegetation(builder1);
/*      */     
/*  414 */     return baseOcean()
/*  415 */       .mobSpawnSettings(builder.build())
/*  416 */       .generationSettings(builder1.build())
/*  417 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome lukeWarmOcean(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*  421 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  422 */     if (paramBoolean) {
/*  423 */       BiomeDefaultFeatures.oceanSpawns(builder, 8, 4, 8);
/*      */     } else {
/*  425 */       BiomeDefaultFeatures.oceanSpawns(builder, 10, 2, 15);
/*      */     } 
/*  427 */     builder.addSpawn(MobCategory.WATER_AMBIENT, 5, new MobSpawnSettings.SpawnerData(EntityType.PUFFERFISH, 1, 3))
/*  428 */       .addSpawn(MobCategory.WATER_AMBIENT, 25, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 8, 8))
/*  429 */       .addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 1, 2))
/*  430 */       .addSpawn(MobCategory.WATER_CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.NAUTILUS, 1, 1));
/*      */     
/*  432 */     BiomeGenerationSettings.Builder builder1 = baseOceanGeneration(paramHolderGetter, paramHolderGetter1);
/*  433 */     builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, paramBoolean ? AquaticPlacements.SEAGRASS_DEEP_WARM : AquaticPlacements.SEAGRASS_WARM);
/*  434 */     BiomeDefaultFeatures.addLukeWarmKelp(builder1);
/*      */     
/*  436 */     return baseOcean()
/*  437 */       .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-16509389))
/*  438 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  439 */         .waterColor(4566514)
/*  440 */         .build())
/*      */       
/*  442 */       .mobSpawnSettings(builder.build())
/*  443 */       .generationSettings(builder1.build())
/*  444 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome warmOcean(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  450 */     MobSpawnSettings.Builder builder = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.WATER_AMBIENT, 15, new MobSpawnSettings.SpawnerData(EntityType.PUFFERFISH, 1, 3)).addSpawn(MobCategory.WATER_CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.NAUTILUS, 1, 1));
/*  451 */     BiomeDefaultFeatures.warmOceanSpawns(builder, 10, 4);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  456 */     BiomeGenerationSettings.Builder builder1 = baseOceanGeneration(paramHolderGetter, paramHolderGetter1).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.WARM_OCEAN_VEGETATION).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_WARM).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEA_PICKLE);
/*      */     
/*  458 */     return baseOcean()
/*  459 */       .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-16507085))
/*  460 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  461 */         .waterColor(4445678)
/*  462 */         .build())
/*      */       
/*  464 */       .mobSpawnSettings(builder.build())
/*  465 */       .generationSettings(builder1.build())
/*  466 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome frozenOcean(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*  474 */     MobSpawnSettings.Builder builder = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.WATER_CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 4)).addSpawn(MobCategory.WATER_AMBIENT, 15, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 1, 5)).addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 2)).addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.NAUTILUS, 1, 1));
/*  475 */     BiomeDefaultFeatures.commonSpawns(builder);
/*  476 */     builder.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 1, 1));
/*      */     
/*  478 */     float f = paramBoolean ? 0.5F : 0.0F;
/*  479 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  481 */     BiomeDefaultFeatures.addIcebergs(builder1);
/*      */     
/*  483 */     globalOverworldGeneration(builder1);
/*  484 */     BiomeDefaultFeatures.addBlueIce(builder1);
/*  485 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  486 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*  487 */     BiomeDefaultFeatures.addWaterTrees(builder1);
/*  488 */     BiomeDefaultFeatures.addDefaultFlowers(builder1);
/*  489 */     BiomeDefaultFeatures.addDefaultGrass(builder1);
/*  490 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/*  491 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder1, true);
/*      */     
/*  493 */     return baseBiome(f, 0.5F)
/*  494 */       .temperatureAdjustment(Biome.TemperatureModifier.FROZEN)
/*  495 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  496 */         .waterColor(3750089)
/*  497 */         .build())
/*      */       
/*  499 */       .mobSpawnSettings(builder.build())
/*  500 */       .generationSettings(builder1.build())
/*  501 */       .build();
/*      */   }
/*      */   public static Biome forest(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*      */     BackgroundMusic backgroundMusic;
/*  505 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  507 */     globalOverworldGeneration(builder);
/*      */ 
/*      */     
/*  510 */     if (paramBoolean3) {
/*  511 */       backgroundMusic = new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_FLOWER_FOREST);
/*  512 */       builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FOREST_FLOWERS);
/*      */     } else {
/*  514 */       backgroundMusic = new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_FOREST);
/*  515 */       BiomeDefaultFeatures.addForestFlowers(builder);
/*      */     } 
/*      */     
/*  518 */     BiomeDefaultFeatures.addDefaultOres(builder);
/*  519 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder);
/*      */     
/*  521 */     if (paramBoolean3) {
/*  522 */       builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_FLOWER_FOREST);
/*  523 */       builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FLOWER_FOREST);
/*  524 */       BiomeDefaultFeatures.addDefaultGrass(builder);
/*      */     } else {
/*  526 */       if (paramBoolean1) {
/*  527 */         BiomeDefaultFeatures.addBirchForestFlowers(builder);
/*  528 */         if (paramBoolean2) {
/*  529 */           BiomeDefaultFeatures.addTallBirchTrees(builder);
/*      */         } else {
/*  531 */           BiomeDefaultFeatures.addBirchTrees(builder);
/*      */         } 
/*      */       } else {
/*  534 */         BiomeDefaultFeatures.addOtherBirchTrees(builder);
/*      */       } 
/*  536 */       BiomeDefaultFeatures.addBushes(builder);
/*  537 */       BiomeDefaultFeatures.addDefaultFlowers(builder);
/*  538 */       BiomeDefaultFeatures.addForestGrass(builder);
/*      */     } 
/*      */     
/*  541 */     BiomeDefaultFeatures.addDefaultMushrooms(builder);
/*  542 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder, true);
/*      */     
/*  544 */     MobSpawnSettings.Builder builder1 = new MobSpawnSettings.Builder();
/*  545 */     BiomeDefaultFeatures.farmAnimals(builder1);
/*  546 */     BiomeDefaultFeatures.commonSpawns(builder1);
/*      */     
/*  548 */     if (paramBoolean3) {
/*  549 */       builder1.addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3));
/*  550 */     } else if (!paramBoolean1) {
/*  551 */       builder1.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 4));
/*      */     } 
/*      */     
/*  554 */     return baseBiome(paramBoolean1 ? 0.6F : 0.7F, paramBoolean1 ? 0.6F : 0.8F)
/*  555 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, backgroundMusic)
/*  556 */       .mobSpawnSettings(builder1.build())
/*  557 */       .generationSettings(builder.build())
/*  558 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome taiga(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*  562 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  563 */     BiomeDefaultFeatures.farmAnimals(builder);
/*  564 */     builder.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 4))
/*  565 */       .addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3))
/*  566 */       .addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.FOX, 2, 4));
/*      */     
/*  568 */     BiomeDefaultFeatures.commonSpawns(builder);
/*      */     
/*  570 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  572 */     globalOverworldGeneration(builder1);
/*  573 */     BiomeDefaultFeatures.addFerns(builder1);
/*  574 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  575 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*  576 */     BiomeDefaultFeatures.addTaigaTrees(builder1);
/*  577 */     BiomeDefaultFeatures.addDefaultFlowers(builder1);
/*  578 */     BiomeDefaultFeatures.addTaigaGrass(builder1);
/*  579 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder1, true);
/*  580 */     if (paramBoolean) {
/*  581 */       BiomeDefaultFeatures.addRareBerryBushes(builder1);
/*      */     } else {
/*  583 */       BiomeDefaultFeatures.addCommonBerryBushes(builder1);
/*      */     } 
/*      */     
/*  586 */     int i = paramBoolean ? 4020182 : 4159204;
/*      */     
/*  588 */     return baseBiome(paramBoolean ? -0.5F : 0.25F, paramBoolean ? 0.4F : 0.8F)
/*  589 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  590 */         .waterColor(i)
/*  591 */         .build())
/*  592 */       .mobSpawnSettings(builder.build())
/*  593 */       .generationSettings(builder1.build())
/*  594 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome darkForest(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*  598 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  599 */     if (!paramBoolean) {
/*  600 */       BiomeDefaultFeatures.farmAnimals(builder);
/*      */     }
/*  602 */     BiomeDefaultFeatures.commonSpawns(builder);
/*      */     
/*  604 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  606 */     globalOverworldGeneration(builder1);
/*  607 */     builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, paramBoolean ? VegetationPlacements.PALE_GARDEN_VEGETATION : VegetationPlacements.DARK_FOREST_VEGETATION);
/*  608 */     if (!paramBoolean) {
/*  609 */       BiomeDefaultFeatures.addForestFlowers(builder1);
/*      */     } else {
/*  611 */       builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PALE_MOSS_PATCH);
/*  612 */       builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PALE_GARDEN_FLOWERS);
/*      */     } 
/*  614 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  615 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*  616 */     if (!paramBoolean) {
/*  617 */       BiomeDefaultFeatures.addDefaultFlowers(builder1);
/*      */     } else {
/*  619 */       builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_PALE_GARDEN);
/*      */     } 
/*  621 */     BiomeDefaultFeatures.addForestGrass(builder1);
/*  622 */     if (!paramBoolean) {
/*  623 */       BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/*  624 */       BiomeDefaultFeatures.addLeafLitterPatch(builder1);
/*      */     } 
/*  626 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  634 */     EnvironmentAttributeMap environmentAttributeMap1 = EnvironmentAttributeMap.builder().set(EnvironmentAttributes.SKY_COLOR, Integer.valueOf(-4605511)).set(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-8292496)).set(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-11179648)).set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.EMPTY).set(EnvironmentAttributes.MUSIC_VOLUME, Float.valueOf(0.0F)).build();
/*      */ 
/*      */     
/*  637 */     EnvironmentAttributeMap environmentAttributeMap2 = EnvironmentAttributeMap.builder().set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_FOREST)).build();
/*      */     
/*  639 */     return baseBiome(0.7F, 0.8F)
/*  640 */       .putAttributes(paramBoolean ? environmentAttributeMap1 : environmentAttributeMap2)
/*  641 */       .specialEffects(paramBoolean ? (
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  647 */         new BiomeSpecialEffects.Builder()).waterColor(7768221).grassColorOverride(7832178).foliageColorOverride(8883574).dryFoliageColorOverride(10528412).build() : (
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  652 */         new BiomeSpecialEffects.Builder()).waterColor(4159204).dryFoliageColorOverride(8082228).grassColorModifier(BiomeSpecialEffects.GrassColorModifier.DARK_FOREST).build())
/*  653 */       .mobSpawnSettings(builder.build())
/*  654 */       .generationSettings(builder1.build())
/*  655 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome swamp(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  659 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*      */     
/*  661 */     BiomeDefaultFeatures.farmAnimals(builder);
/*  662 */     BiomeDefaultFeatures.swampSpawns(builder, 70);
/*      */     
/*  664 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  666 */     BiomeDefaultFeatures.addFossilDecoration(builder1);
/*      */     
/*  668 */     globalOverworldGeneration(builder1);
/*  669 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*      */     
/*  671 */     BiomeDefaultFeatures.addSwampClayDisk(builder1);
/*  672 */     BiomeDefaultFeatures.addSwampVegetation(builder1);
/*  673 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/*  674 */     BiomeDefaultFeatures.addSwampExtraVegetation(builder1);
/*  675 */     builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP);
/*      */     
/*  677 */     return baseBiome(0.8F, 0.9F)
/*  678 */       .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-14474473))
/*  679 */       .modifyAttribute(EnvironmentAttributes.WATER_FOG_END_DISTANCE, (AttributeModifier)FloatModifier.MULTIPLY, Float.valueOf(0.85F))
/*  680 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_SWAMP))
/*  681 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  682 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  683 */         .waterColor(6388580)
/*  684 */         .foliageColorOverride(6975545)
/*  685 */         .dryFoliageColorOverride(8082228)
/*  686 */         .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.SWAMP)
/*  687 */         .build())
/*      */       
/*  689 */       .mobSpawnSettings(builder.build())
/*  690 */       .generationSettings(builder1.build())
/*  691 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome mangroveSwamp(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  695 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  696 */     BiomeDefaultFeatures.swampSpawns(builder, 70);
/*  697 */     builder.addSpawn(MobCategory.WATER_AMBIENT, 25, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 8, 8));
/*      */     
/*  699 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  701 */     BiomeDefaultFeatures.addFossilDecoration(builder1);
/*      */     
/*  703 */     globalOverworldGeneration(builder1);
/*  704 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  705 */     BiomeDefaultFeatures.addMangroveSwampDisks(builder1);
/*  706 */     BiomeDefaultFeatures.addMangroveSwampVegetation(builder1);
/*  707 */     BiomeDefaultFeatures.addMangroveSwampExtraVegetation(builder1);
/*      */     
/*  709 */     return baseBiome(0.8F, 0.9F)
/*  710 */       .setAttribute(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-4138753))
/*  711 */       .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-11699616))
/*  712 */       .modifyAttribute(EnvironmentAttributes.WATER_FOG_END_DISTANCE, (AttributeModifier)FloatModifier.MULTIPLY, Float.valueOf(0.85F))
/*  713 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_SWAMP))
/*  714 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  715 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  716 */         .waterColor(3832426)
/*  717 */         .foliageColorOverride(9285927)
/*  718 */         .dryFoliageColorOverride(8082228)
/*  719 */         .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.SWAMP)
/*  720 */         .build())
/*      */       
/*  722 */       .mobSpawnSettings(builder.build())
/*  723 */       .generationSettings(builder1.build())
/*  724 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome river(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*  730 */     MobSpawnSettings.Builder builder = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 4)).addSpawn(MobCategory.WATER_AMBIENT, 5, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 1, 5));
/*  731 */     BiomeDefaultFeatures.commonSpawns(builder);
/*  732 */     builder.addSpawn(MobCategory.MONSTER, paramBoolean ? 1 : 100, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 1, 1));
/*      */     
/*  734 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  736 */     globalOverworldGeneration(builder1);
/*  737 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  738 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*  739 */     BiomeDefaultFeatures.addWaterTrees(builder1);
/*  740 */     BiomeDefaultFeatures.addBushes(builder1);
/*  741 */     BiomeDefaultFeatures.addDefaultFlowers(builder1);
/*  742 */     BiomeDefaultFeatures.addDefaultGrass(builder1);
/*  743 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/*  744 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder1, true);
/*      */     
/*  746 */     if (!paramBoolean) {
/*  747 */       builder1.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_RIVER);
/*      */     }
/*      */     
/*  750 */     return baseBiome(paramBoolean ? 0.0F : 0.5F, 0.5F)
/*  751 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD.withUnderwater(Musics.UNDER_WATER))
/*  752 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  753 */         .waterColor(paramBoolean ? 3750089 : 4159204)
/*  754 */         .build())
/*  755 */       .mobSpawnSettings(builder.build())
/*  756 */       .generationSettings(builder1.build())
/*  757 */       .build();
/*      */   }
/*      */   public static Biome beach(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean1, boolean paramBoolean2) {
/*      */     float f;
/*  761 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  762 */     boolean bool = (!paramBoolean2 && !paramBoolean1) ? true : false;
/*  763 */     if (bool) {
/*  764 */       builder.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.TURTLE, 2, 5));
/*      */     }
/*  766 */     BiomeDefaultFeatures.commonSpawns(builder);
/*      */     
/*  768 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  770 */     globalOverworldGeneration(builder1);
/*  771 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  772 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*  773 */     BiomeDefaultFeatures.addDefaultFlowers(builder1);
/*  774 */     BiomeDefaultFeatures.addDefaultGrass(builder1);
/*  775 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/*  776 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder1, true);
/*      */ 
/*      */     
/*  779 */     if (paramBoolean1) {
/*  780 */       f = 0.05F;
/*  781 */     } else if (paramBoolean2) {
/*  782 */       f = 0.2F;
/*      */     } else {
/*  784 */       f = 0.8F;
/*      */     } 
/*      */     
/*  787 */     int i = paramBoolean1 ? 4020182 : 4159204;
/*      */     
/*  789 */     return baseBiome(f, bool ? 0.4F : 0.3F)
/*  790 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  791 */         .waterColor(i)
/*  792 */         .build())
/*  793 */       .mobSpawnSettings(builder.build())
/*  794 */       .generationSettings(builder1.build())
/*  795 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome theVoid(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  799 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*  800 */     builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.VOID_START_PLATFORM);
/*      */     
/*  802 */     return baseBiome(0.5F, 0.5F)
/*  803 */       .hasPrecipitation(false)
/*  804 */       .mobSpawnSettings((new MobSpawnSettings.Builder()).build())
/*  805 */       .generationSettings(builder.build())
/*  806 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome meadowOrCherryGrove(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1, boolean paramBoolean) {
/*  810 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  812 */     MobSpawnSettings.Builder builder1 = new MobSpawnSettings.Builder();
/*  813 */     builder1.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(paramBoolean ? EntityType.PIG : EntityType.DONKEY, 1, 2))
/*  814 */       .addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 6))
/*  815 */       .addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 2, 4));
/*  816 */     BiomeDefaultFeatures.commonSpawns(builder1);
/*      */     
/*  818 */     globalOverworldGeneration(builder);
/*  819 */     BiomeDefaultFeatures.addPlainGrass(builder);
/*      */     
/*  821 */     BiomeDefaultFeatures.addDefaultOres(builder);
/*  822 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder);
/*      */     
/*  824 */     if (paramBoolean) {
/*  825 */       BiomeDefaultFeatures.addCherryGroveVegetation(builder);
/*      */     } else {
/*  827 */       BiomeDefaultFeatures.addMeadowVegetation(builder);
/*      */     } 
/*      */     
/*  830 */     BiomeDefaultFeatures.addExtraEmeralds(builder);
/*  831 */     BiomeDefaultFeatures.addInfestedStone(builder);
/*      */     
/*  833 */     if (paramBoolean) {
/*      */ 
/*      */ 
/*      */       
/*  837 */       BiomeSpecialEffects.Builder builder2 = (new BiomeSpecialEffects.Builder()).waterColor(6141935).grassColorOverride(11983713).foliageColorOverride(11983713);
/*      */       
/*  839 */       return baseBiome(0.5F, 0.8F)
/*  840 */         .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-10635281))
/*  841 */         .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_CHERRY_GROVE))
/*  842 */         .specialEffects(builder2.build())
/*  843 */         .mobSpawnSettings(builder1.build())
/*  844 */         .generationSettings(builder.build())
/*  845 */         .build();
/*      */     } 
/*      */     
/*  848 */     return baseBiome(0.5F, 0.8F)
/*  849 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_MEADOW))
/*  850 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  851 */         .waterColor(937679)
/*  852 */         .build())
/*  853 */       .mobSpawnSettings(builder1.build())
/*  854 */       .generationSettings(builder.build())
/*  855 */       .build();
/*      */   }
/*      */   
/*      */   private static Biome.BiomeBuilder basePeaks(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  859 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  861 */     MobSpawnSettings.Builder builder1 = new MobSpawnSettings.Builder();
/*  862 */     builder1.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 1, 3));
/*  863 */     BiomeDefaultFeatures.commonSpawns(builder1);
/*      */     
/*  865 */     globalOverworldGeneration(builder);
/*  866 */     BiomeDefaultFeatures.addFrozenSprings(builder);
/*  867 */     BiomeDefaultFeatures.addDefaultOres(builder);
/*  868 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder);
/*      */     
/*  870 */     BiomeDefaultFeatures.addExtraEmeralds(builder);
/*  871 */     BiomeDefaultFeatures.addInfestedStone(builder);
/*      */     
/*  873 */     return baseBiome(-0.7F, 0.9F)
/*  874 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  875 */       .mobSpawnSettings(builder1.build())
/*  876 */       .generationSettings(builder.build());
/*      */   }
/*      */   
/*      */   public static Biome frozenPeaks(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  880 */     return basePeaks(paramHolderGetter, paramHolderGetter1)
/*  881 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_FROZEN_PEAKS))
/*  882 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome jaggedPeaks(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  886 */     return basePeaks(paramHolderGetter, paramHolderGetter1)
/*  887 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_JAGGED_PEAKS))
/*  888 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome stonyPeaks(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  892 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  894 */     MobSpawnSettings.Builder builder1 = new MobSpawnSettings.Builder();
/*  895 */     BiomeDefaultFeatures.commonSpawns(builder1);
/*      */     
/*  897 */     globalOverworldGeneration(builder);
/*  898 */     BiomeDefaultFeatures.addDefaultOres(builder);
/*  899 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder);
/*      */     
/*  901 */     BiomeDefaultFeatures.addExtraEmeralds(builder);
/*  902 */     BiomeDefaultFeatures.addInfestedStone(builder);
/*      */     
/*  904 */     return baseBiome(1.0F, 0.3F)
/*  905 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_STONY_PEAKS))
/*  906 */       .mobSpawnSettings(builder1.build())
/*  907 */       .generationSettings(builder.build())
/*  908 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome snowySlopes(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  912 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  914 */     MobSpawnSettings.Builder builder1 = new MobSpawnSettings.Builder();
/*  915 */     builder1.addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3))
/*  916 */       .addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 1, 3));
/*  917 */     BiomeDefaultFeatures.commonSpawns(builder1);
/*      */     
/*  919 */     globalOverworldGeneration(builder);
/*  920 */     BiomeDefaultFeatures.addFrozenSprings(builder);
/*  921 */     BiomeDefaultFeatures.addDefaultOres(builder);
/*  922 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder);
/*      */     
/*  924 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder, false);
/*  925 */     BiomeDefaultFeatures.addExtraEmeralds(builder);
/*  926 */     BiomeDefaultFeatures.addInfestedStone(builder);
/*      */     
/*  928 */     return baseBiome(-0.3F, 0.9F)
/*  929 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_SNOWY_SLOPES))
/*  930 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  931 */       .mobSpawnSettings(builder1.build())
/*  932 */       .generationSettings(builder.build())
/*  933 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome grove(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  937 */     BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  939 */     MobSpawnSettings.Builder builder1 = new MobSpawnSettings.Builder();
/*  940 */     builder1.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 1, 1))
/*  941 */       .addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3))
/*  942 */       .addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityType.FOX, 2, 4));
/*  943 */     BiomeDefaultFeatures.commonSpawns(builder1);
/*      */     
/*  945 */     globalOverworldGeneration(builder);
/*  946 */     BiomeDefaultFeatures.addFrozenSprings(builder);
/*  947 */     BiomeDefaultFeatures.addDefaultOres(builder);
/*  948 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder);
/*      */     
/*  950 */     BiomeDefaultFeatures.addGroveTrees(builder);
/*      */     
/*  952 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder, false);
/*  953 */     BiomeDefaultFeatures.addExtraEmeralds(builder);
/*  954 */     BiomeDefaultFeatures.addInfestedStone(builder);
/*      */     
/*  956 */     return baseBiome(-0.2F, 0.8F)
/*  957 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_GROVE))
/*  958 */       .mobSpawnSettings(builder1.build())
/*  959 */       .generationSettings(builder.build())
/*  960 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome lushCaves(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  964 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  965 */     builder.addSpawn(MobCategory.AXOLOTLS, 10, new MobSpawnSettings.SpawnerData(EntityType.AXOLOTL, 4, 6));
/*  966 */     builder.addSpawn(MobCategory.WATER_AMBIENT, 25, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 8, 8));
/*  967 */     BiomeDefaultFeatures.commonSpawns(builder);
/*      */     
/*  969 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  971 */     globalOverworldGeneration(builder1);
/*  972 */     BiomeDefaultFeatures.addPlainGrass(builder1);
/*      */     
/*  974 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/*  975 */     BiomeDefaultFeatures.addLushCavesSpecialOres(builder1);
/*  976 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*      */     
/*  978 */     BiomeDefaultFeatures.addLushCavesVegetationFeatures(builder1);
/*      */     
/*  980 */     return baseBiome(0.5F, 0.5F)
/*  981 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_LUSH_CAVES))
/*  982 */       .mobSpawnSettings(builder.build())
/*  983 */       .generationSettings(builder1.build())
/*  984 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome dripstoneCaves(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  988 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*  989 */     BiomeDefaultFeatures.dripstoneCavesSpawns(builder);
/*      */     
/*  991 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */     
/*  993 */     globalOverworldGeneration(builder1);
/*  994 */     BiomeDefaultFeatures.addPlainGrass(builder1);
/*      */ 
/*      */     
/*  997 */     BiomeDefaultFeatures.addDefaultOres(builder1, true);
/*  998 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/*  999 */     BiomeDefaultFeatures.addPlainVegetation(builder1);
/* 1000 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/* 1001 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder1, false);
/*      */     
/* 1003 */     BiomeDefaultFeatures.addDripstone(builder1);
/*      */     
/* 1005 */     return baseBiome(0.8F, 0.4F)
/* 1006 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES))
/* 1007 */       .mobSpawnSettings(builder.build())
/* 1008 */       .generationSettings(builder1.build())
/* 1009 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome deepDark(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/* 1013 */     MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
/*      */     
/* 1015 */     BiomeGenerationSettings.Builder builder1 = new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1);
/*      */ 
/*      */     
/* 1018 */     builder1.addCarver(Carvers.CAVE);
/* 1019 */     builder1.addCarver(Carvers.CAVE_EXTRA_UNDERGROUND);
/* 1020 */     builder1.addCarver(Carvers.CANYON);
/*      */     
/* 1022 */     BiomeDefaultFeatures.addDefaultCrystalFormations(builder1);
/* 1023 */     BiomeDefaultFeatures.addDefaultMonsterRoom(builder1);
/* 1024 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(builder1);
/* 1025 */     BiomeDefaultFeatures.addSurfaceFreezing(builder1);
/*      */     
/* 1027 */     BiomeDefaultFeatures.addPlainGrass(builder1);
/*      */     
/* 1029 */     BiomeDefaultFeatures.addDefaultOres(builder1);
/* 1030 */     BiomeDefaultFeatures.addDefaultSoftDisks(builder1);
/* 1031 */     BiomeDefaultFeatures.addPlainVegetation(builder1);
/* 1032 */     BiomeDefaultFeatures.addDefaultMushrooms(builder1);
/* 1033 */     BiomeDefaultFeatures.addDefaultExtraVegetation(builder1, false);
/*      */     
/* 1035 */     BiomeDefaultFeatures.addSculk(builder1);
/*      */     
/* 1037 */     return baseBiome(0.8F, 0.4F)
/* 1038 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_DEEP_DARK))
/* 1039 */       .mobSpawnSettings(builder.build())
/* 1040 */       .generationSettings(builder1.build())
/* 1041 */       .build();
/*      */   }
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\biome\OverworldBiomes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */