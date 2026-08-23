/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import net.minecraft.data.worldgen.placement.AquaticPlacements;
/*     */ import net.minecraft.data.worldgen.placement.CavePlacements;
/*     */ import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
/*     */ import net.minecraft.data.worldgen.placement.OrePlacements;
/*     */ import net.minecraft.data.worldgen.placement.VegetationPlacements;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ 
/*     */ public class BiomeDefaultFeatures {
/*     */   public static void addDefaultCarversAndLakes(BiomeGenerationSettings.Builder paramBuilder) {
/*  16 */     paramBuilder.addCarver(Carvers.CAVE);
/*  17 */     paramBuilder.addCarver(Carvers.CAVE_EXTRA_UNDERGROUND);
/*  18 */     paramBuilder.addCarver(Carvers.CANYON);
/*  19 */     paramBuilder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND);
/*  20 */     paramBuilder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_SURFACE);
/*     */   }
/*     */   
/*     */   public static void addDefaultMonsterRoom(BiomeGenerationSettings.Builder paramBuilder) {
/*  24 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM);
/*  25 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM_DEEP);
/*     */   }
/*     */   
/*     */   public static void addDefaultUndergroundVariety(BiomeGenerationSettings.Builder paramBuilder) {
/*  29 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIRT);
/*  30 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRAVEL);
/*  31 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRANITE_UPPER);
/*  32 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRANITE_LOWER);
/*  33 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIORITE_UPPER);
/*  34 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIORITE_LOWER);
/*  35 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_UPPER);
/*  36 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_LOWER);
/*  37 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_TUFF);
/*  38 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.GLOW_LICHEN);
/*     */   }
/*     */   
/*     */   public static void addDripstone(BiomeGenerationSettings.Builder paramBuilder) {
/*  42 */     paramBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, CavePlacements.LARGE_DRIPSTONE);
/*  43 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.DRIPSTONE_CLUSTER);
/*  44 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.POINTED_DRIPSTONE);
/*     */   }
/*     */   
/*     */   public static void addSculk(BiomeGenerationSettings.Builder paramBuilder) {
/*  48 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.SCULK_VEIN);
/*  49 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.SCULK_PATCH_DEEP_DARK);
/*     */   }
/*     */   
/*     */   public static void addDefaultOres(BiomeGenerationSettings.Builder paramBuilder) {
/*  53 */     addDefaultOres(paramBuilder, false);
/*     */   }
/*     */   
/*     */   public static void addDefaultOres(BiomeGenerationSettings.Builder paramBuilder, boolean paramBoolean) {
/*  57 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_COAL_UPPER);
/*  58 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_COAL_LOWER);
/*     */     
/*  60 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_UPPER);
/*  61 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_MIDDLE);
/*  62 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_SMALL);
/*     */     
/*  64 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD);
/*  65 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD_LOWER);
/*     */     
/*  67 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_REDSTONE);
/*  68 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_REDSTONE_LOWER);
/*     */     
/*  70 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND);
/*  71 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND_MEDIUM);
/*  72 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND_LARGE);
/*  73 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND_BURIED);
/*     */     
/*  75 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_LAPIS);
/*  76 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_LAPIS_BURIED);
/*     */     
/*  78 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, paramBoolean ? OrePlacements.ORE_COPPER_LARGE : OrePlacements.ORE_COPPER);
/*     */     
/*  80 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CavePlacements.UNDERWATER_MAGMA);
/*     */   }
/*     */   
/*     */   public static void addExtraGold(BiomeGenerationSettings.Builder paramBuilder) {
/*  84 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD_EXTRA);
/*     */   }
/*     */   
/*     */   public static void addExtraEmeralds(BiomeGenerationSettings.Builder paramBuilder) {
/*  88 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_EMERALD);
/*     */   }
/*     */   
/*     */   public static void addInfestedStone(BiomeGenerationSettings.Builder paramBuilder) {
/*  92 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_INFESTED);
/*     */   }
/*     */   
/*     */   public static void addDefaultSoftDisks(BiomeGenerationSettings.Builder paramBuilder) {
/*  96 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_SAND);
/*  97 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY);
/*  98 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_GRAVEL);
/*     */   }
/*     */   
/*     */   public static void addSwampClayDisk(BiomeGenerationSettings.Builder paramBuilder) {
/* 102 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY);
/*     */   }
/*     */   
/*     */   public static void addMangroveSwampDisks(BiomeGenerationSettings.Builder paramBuilder) {
/* 106 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_GRASS);
/* 107 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY);
/*     */   }
/*     */   
/*     */   public static void addMossyStoneBlock(BiomeGenerationSettings.Builder paramBuilder) {
/* 111 */     paramBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.FOREST_ROCK);
/*     */   }
/*     */   
/*     */   public static void addFerns(BiomeGenerationSettings.Builder paramBuilder) {
/* 115 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN);
/*     */   }
/*     */   
/*     */   public static void addBushes(BiomeGenerationSettings.Builder paramBuilder) {
/* 119 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_BUSH);
/*     */   }
/*     */   
/*     */   public static void addRareBerryBushes(BiomeGenerationSettings.Builder paramBuilder) {
/* 123 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_BERRY_RARE);
/*     */   }
/*     */   
/*     */   public static void addCommonBerryBushes(BiomeGenerationSettings.Builder paramBuilder) {
/* 127 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_BERRY_COMMON);
/*     */   }
/*     */   
/*     */   public static void addLightBambooVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 131 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BAMBOO_LIGHT);
/*     */   }
/*     */   
/*     */   public static void addBambooVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 135 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BAMBOO);
/* 136 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BAMBOO_VEGETATION);
/*     */   }
/*     */   
/*     */   public static void addTaigaTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 140 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_TAIGA);
/*     */   }
/*     */   
/*     */   public static void addGroveTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 144 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_GROVE);
/*     */   }
/*     */   
/*     */   public static void addWaterTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 148 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WATER);
/*     */   }
/*     */   
/*     */   public static void addBirchTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 152 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BIRCH);
/*     */   }
/*     */   
/*     */   public static void addOtherBirchTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 156 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BIRCH_AND_OAK_LEAF_LITTER);
/*     */   }
/*     */   
/*     */   public static void addTallBirchTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 160 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BIRCH_TALL);
/*     */   }
/*     */   
/*     */   public static void addBirchForestFlowers(BiomeGenerationSettings.Builder paramBuilder) {
/* 164 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
/*     */   }
/*     */   
/*     */   public static void addSavannaTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 168 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SAVANNA);
/*     */   }
/*     */   
/*     */   public static void addShatteredSavannaTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 172 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WINDSWEPT_SAVANNA);
/*     */   }
/*     */   
/*     */   public static void addLushCavesVegetationFeatures(BiomeGenerationSettings.Builder paramBuilder) {
/* 176 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CEILING_VEGETATION);
/* 177 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CAVE_VINES);
/* 178 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CLAY);
/* 179 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_VEGETATION);
/* 180 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.ROOTED_AZALEA_TREE);
/* 181 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.SPORE_BLOSSOM);
/* 182 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CLASSIC_VINES);
/*     */   }
/*     */   
/*     */   public static void addLushCavesSpecialOres(BiomeGenerationSettings.Builder paramBuilder) {
/* 186 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_CLAY);
/*     */   }
/*     */   
/*     */   public static void addMountainTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 190 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WINDSWEPT_HILLS);
/*     */   }
/*     */   
/*     */   public static void addMountainForestTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 194 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WINDSWEPT_FOREST);
/*     */   }
/*     */   
/*     */   public static void addJungleTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 198 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_JUNGLE);
/*     */   }
/*     */   
/*     */   public static void addSparseJungleTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 202 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SPARSE_JUNGLE);
/*     */   }
/*     */   
/*     */   public static void addBadlandsTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 206 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BADLANDS);
/*     */   }
/*     */   
/*     */   public static void addSnowyTrees(BiomeGenerationSettings.Builder paramBuilder) {
/* 210 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SNOWY);
/*     */   }
/*     */   
/*     */   public static void addJungleGrass(BiomeGenerationSettings.Builder paramBuilder) {
/* 214 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_JUNGLE);
/*     */   }
/*     */   
/*     */   public static void addSavannaGrass(BiomeGenerationSettings.Builder paramBuilder) {
/* 218 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS);
/*     */   }
/*     */   
/*     */   public static void addShatteredSavannaGrass(BiomeGenerationSettings.Builder paramBuilder) {
/* 222 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
/*     */   }
/*     */   
/*     */   public static void addSavannaExtraGrass(BiomeGenerationSettings.Builder paramBuilder) {
/* 226 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_SAVANNA);
/*     */   }
/*     */   
/*     */   public static void addBadlandGrass(BiomeGenerationSettings.Builder paramBuilder) {
/* 230 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_BADLANDS);
/* 231 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DRY_GRASS_BADLANDS);
/* 232 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_BADLANDS);
/*     */   }
/*     */   
/*     */   public static void addForestFlowers(BiomeGenerationSettings.Builder paramBuilder) {
/* 236 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FOREST_FLOWERS);
/*     */   }
/*     */   
/*     */   public static void addForestGrass(BiomeGenerationSettings.Builder paramBuilder) {
/* 240 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);
/*     */   }
/*     */   
/*     */   public static void addSwampVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 244 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SWAMP);
/* 245 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_SWAMP);
/* 246 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
/* 247 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
/* 248 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);
/* 249 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_SWAMP);
/* 250 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_SWAMP);
/*     */   }
/*     */   
/*     */   public static void addMangroveSwampVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 254 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_MANGROVE);
/* 255 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
/* 256 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
/* 257 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);
/*     */   }
/*     */   
/*     */   public static void addMushroomFieldVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 261 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.MUSHROOM_ISLAND_VEGETATION);
/* 262 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_TAIGA);
/* 263 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_TAIGA);
/*     */   }
/*     */   
/*     */   public static void addPlainVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 267 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS);
/* 268 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_PLAINS);
/* 269 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
/*     */   }
/*     */   
/*     */   public static void addDesertVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 273 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DRY_GRASS_DESERT);
/* 274 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_2);
/*     */   }
/*     */   
/*     */   public static void addGiantTaigaVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 278 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA);
/* 279 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
/* 280 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_OLD_GROWTH);
/* 281 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_OLD_GROWTH);
/*     */   }
/*     */   
/*     */   public static void addDefaultFlowers(BiomeGenerationSettings.Builder paramBuilder) {
/* 285 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_DEFAULT);
/*     */   }
/*     */   
/*     */   public static void addCherryGroveVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 289 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
/* 290 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_CHERRY);
/* 291 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_CHERRY);
/*     */   }
/*     */   
/*     */   public static void addMeadowVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 295 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_MEADOW);
/* 296 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_MEADOW);
/* 297 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_MEADOW);
/* 298 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.WILDFLOWERS_MEADOW);
/*     */   }
/*     */   
/*     */   public static void addWarmFlowers(BiomeGenerationSettings.Builder paramBuilder) {
/* 302 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_WARM);
/*     */   }
/*     */   
/*     */   public static void addDefaultGrass(BiomeGenerationSettings.Builder paramBuilder) {
/* 306 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_BADLANDS);
/*     */   }
/*     */   
/*     */   public static void addTaigaGrass(BiomeGenerationSettings.Builder paramBuilder) {
/* 310 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA_2);
/* 311 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_TAIGA);
/* 312 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_TAIGA);
/*     */   }
/*     */   
/*     */   public static void addPlainGrass(BiomeGenerationSettings.Builder paramBuilder) {
/* 316 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS_2);
/*     */   }
/*     */   
/*     */   public static void addDefaultMushrooms(BiomeGenerationSettings.Builder paramBuilder) {
/* 320 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NORMAL);
/* 321 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_NORMAL);
/*     */   }
/*     */   
/*     */   public static void addDefaultExtraVegetation(BiomeGenerationSettings.Builder paramBuilder, boolean paramBoolean) {
/* 325 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
/* 326 */     if (paramBoolean) {
/* 327 */       addNearWaterVegetation(paramBuilder);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addNearWaterVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 332 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE);
/* 333 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER);
/*     */   }
/*     */   
/*     */   public static void addLeafLitterPatch(BiomeGenerationSettings.Builder paramBuilder) {
/* 337 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LEAF_LITTER);
/*     */   }
/*     */   
/*     */   public static void addBadlandExtraVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 341 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_BADLANDS);
/* 342 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
/* 343 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
/* 344 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER);
/*     */   }
/*     */   
/*     */   public static void addJungleMelons(BiomeGenerationSettings.Builder paramBuilder) {
/* 348 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_MELON);
/*     */   }
/*     */   
/*     */   public static void addSparseJungleMelons(BiomeGenerationSettings.Builder paramBuilder) {
/* 352 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_MELON_SPARSE);
/*     */   }
/*     */   
/*     */   public static void addJungleVines(BiomeGenerationSettings.Builder paramBuilder) {
/* 356 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.VINES);
/*     */   }
/*     */   
/*     */   public static void addDesertExtraVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 360 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_DESERT);
/* 361 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
/* 362 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DESERT);
/*     */   }
/*     */   
/*     */   public static void addSwampExtraVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 366 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_SWAMP);
/* 367 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
/* 368 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_SWAMP);
/* 369 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP);
/*     */   }
/*     */   
/*     */   public static void addMangroveSwampExtraVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 373 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP);
/* 374 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER);
/*     */   }
/*     */   
/*     */   public static void addDesertExtraDecoration(BiomeGenerationSettings.Builder paramBuilder) {
/* 378 */     paramBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.DESERT_WELL);
/*     */   }
/*     */   
/*     */   public static void addFossilDecoration(BiomeGenerationSettings.Builder paramBuilder) {
/* 382 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.FOSSIL_UPPER);
/* 383 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.FOSSIL_LOWER);
/*     */   }
/*     */   
/*     */   public static void addColdOceanExtraVegetation(BiomeGenerationSettings.Builder paramBuilder) {
/* 387 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.KELP_COLD);
/*     */   }
/*     */   
/*     */   public static void addLukeWarmKelp(BiomeGenerationSettings.Builder paramBuilder) {
/* 391 */     paramBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.KELP_WARM);
/*     */   }
/*     */   
/*     */   public static void addDefaultSprings(BiomeGenerationSettings.Builder paramBuilder) {
/* 395 */     paramBuilder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_WATER);
/* 396 */     paramBuilder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_LAVA);
/*     */   }
/*     */   
/*     */   public static void addFrozenSprings(BiomeGenerationSettings.Builder paramBuilder) {
/* 400 */     paramBuilder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_LAVA_FROZEN);
/*     */   }
/*     */   
/*     */   public static void addIcebergs(BiomeGenerationSettings.Builder paramBuilder) {
/* 404 */     paramBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.ICEBERG_PACKED);
/* 405 */     paramBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.ICEBERG_BLUE);
/*     */   }
/*     */   
/*     */   public static void addBlueIce(BiomeGenerationSettings.Builder paramBuilder) {
/* 409 */     paramBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.BLUE_ICE);
/*     */   }
/*     */   
/*     */   public static void addSurfaceFreezing(BiomeGenerationSettings.Builder paramBuilder) {
/* 413 */     paramBuilder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.FREEZE_TOP_LAYER);
/*     */   }
/*     */   
/*     */   public static void addNetherDefaultOres(BiomeGenerationSettings.Builder paramBuilder) {
/* 417 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_GRAVEL_NETHER);
/* 418 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_BLACKSTONE);
/*     */     
/* 420 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_GOLD_NETHER);
/* 421 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_QUARTZ_NETHER);
/* 422 */     addAncientDebris(paramBuilder);
/*     */   }
/*     */   
/*     */   public static void addAncientDebris(BiomeGenerationSettings.Builder paramBuilder) {
/* 426 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_ANCIENT_DEBRIS_LARGE);
/* 427 */     paramBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_ANCIENT_DEBRIS_SMALL);
/*     */   }
/*     */   
/*     */   public static void addDefaultCrystalFormations(BiomeGenerationSettings.Builder paramBuilder) {
/* 431 */     paramBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, CavePlacements.AMETHYST_GEODE);
/*     */   }
/*     */   
/*     */   public static void farmAnimals(MobSpawnSettings.Builder paramBuilder) {
/* 435 */     paramBuilder.addSpawn(MobCategory.CREATURE, 12, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 4, 4));
/* 436 */     paramBuilder.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.PIG, 4, 4));
/* 437 */     paramBuilder.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 4, 4));
/* 438 */     paramBuilder.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.COW, 4, 4));
/*     */   }
/*     */   
/*     */   public static void caveSpawns(MobSpawnSettings.Builder paramBuilder) {
/* 442 */     paramBuilder.addSpawn(MobCategory.AMBIENT, 10, new MobSpawnSettings.SpawnerData(EntityType.BAT, 8, 8));
/* 443 */     paramBuilder.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 4, 6));
/*     */   }
/*     */   
/*     */   public static void commonSpawns(MobSpawnSettings.Builder paramBuilder) {
/* 447 */     commonSpawns(paramBuilder, 100);
/*     */   }
/*     */   
/*     */   public static void commonSpawns(MobSpawnSettings.Builder paramBuilder, int paramInt) {
/* 451 */     caveSpawns(paramBuilder);
/* 452 */     monsters(paramBuilder, 95, 5, 0, paramInt, false);
/*     */   }
/*     */   
/*     */   public static void commonSpawnWithZombieHorse(MobSpawnSettings.Builder paramBuilder) {
/* 456 */     caveSpawns(paramBuilder);
/* 457 */     monsters(paramBuilder, 90, 5, 5, 100, false);
/*     */   }
/*     */   
/*     */   public static void swampSpawns(MobSpawnSettings.Builder paramBuilder, int paramInt) {
/* 461 */     commonSpawns(paramBuilder, paramInt);
/* 462 */     paramBuilder.addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 1, 1));
/* 463 */     paramBuilder.addSpawn(MobCategory.MONSTER, 30, new MobSpawnSettings.SpawnerData(EntityType.BOGGED, 4, 4));
/* 464 */     paramBuilder.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.FROG, 2, 5));
/*     */   }
/*     */   
/*     */   public static void oceanSpawns(MobSpawnSettings.Builder paramBuilder, int paramInt1, int paramInt2, int paramInt3) {
/* 468 */     paramBuilder.addSpawn(MobCategory.WATER_CREATURE, paramInt1, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, paramInt2));
/* 469 */     paramBuilder.addSpawn(MobCategory.WATER_AMBIENT, paramInt3, new MobSpawnSettings.SpawnerData(EntityType.COD, 3, 6));
/* 470 */     commonSpawns(paramBuilder);
/* 471 */     paramBuilder.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 1, 1));
/*     */   }
/*     */   
/*     */   public static void warmOceanSpawns(MobSpawnSettings.Builder paramBuilder, int paramInt1, int paramInt2) {
/* 475 */     paramBuilder.addSpawn(MobCategory.WATER_CREATURE, paramInt1, new MobSpawnSettings.SpawnerData(EntityType.SQUID, paramInt2, 4));
/* 476 */     paramBuilder.addSpawn(MobCategory.WATER_AMBIENT, 25, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 8, 8));
/* 477 */     paramBuilder.addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 1, 2));
/* 478 */     paramBuilder.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 1, 1));
/* 479 */     commonSpawns(paramBuilder);
/*     */   }
/*     */   
/*     */   public static void plainsSpawns(MobSpawnSettings.Builder paramBuilder) {
/* 483 */     farmAnimals(paramBuilder);
/* 484 */     paramBuilder.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 2, 6));
/* 485 */     paramBuilder.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 3));
/* 486 */     commonSpawnWithZombieHorse(paramBuilder);
/*     */   }
/*     */   
/*     */   public static void snowySpawns(MobSpawnSettings.Builder paramBuilder, boolean paramBoolean) {
/* 490 */     paramBuilder.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3));
/* 491 */     paramBuilder.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 2));
/* 492 */     caveSpawns(paramBuilder);
/* 493 */     monsters(paramBuilder, paramBoolean ? 90 : 95, 5, paramBoolean ? 5 : 0, 20, false);
/* 494 */     paramBuilder.addSpawn(MobCategory.MONSTER, 80, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 4, 4));
/*     */   }
/*     */   
/*     */   public static void desertSpawns(MobSpawnSettings.Builder paramBuilder) {
/* 498 */     paramBuilder.addSpawn(MobCategory.CREATURE, 12, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3));
/* 499 */     paramBuilder.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.CAMEL, 1, 1));
/* 500 */     caveSpawns(paramBuilder);
/* 501 */     monsters(paramBuilder, 19, 1, 0, 50, false);
/* 502 */     paramBuilder.addSpawn(MobCategory.MONSTER, 80, new MobSpawnSettings.SpawnerData(EntityType.HUSK, 4, 4));
/* 503 */     paramBuilder.addSpawn(MobCategory.MONSTER, 50, new MobSpawnSettings.SpawnerData(EntityType.PARCHED, 4, 4));
/*     */   }
/*     */   
/*     */   public static void dripstoneCavesSpawns(MobSpawnSettings.Builder paramBuilder) {
/* 507 */     caveSpawns(paramBuilder);
/* 508 */     byte b = 95;
/* 509 */     monsters(paramBuilder, 95, 5, 0, 100, false);
/* 510 */     paramBuilder.addSpawn(MobCategory.MONSTER, 95, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 4, 4));
/*     */   }
/*     */   
/*     */   public static void monsters(MobSpawnSettings.Builder paramBuilder, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 514 */     paramBuilder.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 4, 4));
/* 515 */     paramBuilder.addSpawn(MobCategory.MONSTER, paramInt1, new MobSpawnSettings.SpawnerData(paramBoolean ? EntityType.DROWNED : EntityType.ZOMBIE, 4, 4));
/* 516 */     paramBuilder.addSpawn(MobCategory.MONSTER, paramInt2, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 1, 1));
/* 517 */     if (paramInt3 > 0) {
/* 518 */       paramBuilder.addSpawn(MobCategory.MONSTER, paramInt3, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_HORSE, 1, 1));
/*     */     }
/* 520 */     paramBuilder.addSpawn(MobCategory.MONSTER, paramInt4, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 4, 4));
/* 521 */     paramBuilder.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 4, 4));
/* 522 */     paramBuilder.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 4, 4));
/* 523 */     paramBuilder.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4));
/* 524 */     paramBuilder.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1));
/*     */   }
/*     */   
/*     */   public static void mooshroomSpawns(MobSpawnSettings.Builder paramBuilder) {
/* 528 */     paramBuilder.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.MOOSHROOM, 4, 8));
/* 529 */     caveSpawns(paramBuilder);
/*     */   }
/*     */   
/*     */   public static void baseJungleSpawns(MobSpawnSettings.Builder paramBuilder) {
/* 533 */     farmAnimals(paramBuilder);
/* 534 */     paramBuilder.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 4, 4));
/* 535 */     commonSpawns(paramBuilder);
/*     */   }
/*     */   
/*     */   public static void endSpawns(MobSpawnSettings.Builder paramBuilder) {
/* 539 */     paramBuilder.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 4, 4));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\BiomeDefaultFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */