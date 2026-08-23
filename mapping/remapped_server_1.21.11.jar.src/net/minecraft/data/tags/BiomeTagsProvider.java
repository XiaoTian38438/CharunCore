/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ 
/*     */ public class BiomeTagsProvider
/*     */   extends KeyTagProvider<Biome> {
/*     */   public BiomeTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/*  17 */     super(paramPackOutput, Registries.BIOME, paramCompletableFuture);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addTags(HolderLookup.Provider paramProvider) {
/*  22 */     tag(BiomeTags.IS_DEEP_OCEAN)
/*  23 */       .add(Biomes.DEEP_FROZEN_OCEAN)
/*  24 */       .add(Biomes.DEEP_COLD_OCEAN)
/*  25 */       .add(Biomes.DEEP_OCEAN)
/*  26 */       .add(Biomes.DEEP_LUKEWARM_OCEAN);
/*     */ 
/*     */     
/*  29 */     tag(BiomeTags.IS_OCEAN)
/*  30 */       .addTag(BiomeTags.IS_DEEP_OCEAN)
/*  31 */       .add(Biomes.FROZEN_OCEAN)
/*  32 */       .add(Biomes.OCEAN)
/*  33 */       .add(Biomes.COLD_OCEAN)
/*  34 */       .add(Biomes.LUKEWARM_OCEAN)
/*  35 */       .add(Biomes.WARM_OCEAN);
/*     */ 
/*     */     
/*  38 */     tag(BiomeTags.IS_BEACH)
/*  39 */       .add(Biomes.BEACH)
/*  40 */       .add(Biomes.SNOWY_BEACH);
/*     */ 
/*     */     
/*  43 */     tag(BiomeTags.IS_RIVER)
/*  44 */       .add(Biomes.RIVER)
/*  45 */       .add(Biomes.FROZEN_RIVER);
/*     */ 
/*     */     
/*  48 */     tag(BiomeTags.IS_MOUNTAIN)
/*  49 */       .add(Biomes.MEADOW)
/*  50 */       .add(Biomes.FROZEN_PEAKS)
/*  51 */       .add(Biomes.JAGGED_PEAKS)
/*  52 */       .add(Biomes.STONY_PEAKS)
/*  53 */       .add(Biomes.SNOWY_SLOPES)
/*  54 */       .add(Biomes.CHERRY_GROVE);
/*     */ 
/*     */     
/*  57 */     tag(BiomeTags.IS_BADLANDS)
/*  58 */       .add(Biomes.BADLANDS)
/*  59 */       .add(Biomes.ERODED_BADLANDS)
/*  60 */       .add(Biomes.WOODED_BADLANDS);
/*     */ 
/*     */     
/*  63 */     tag(BiomeTags.IS_HILL)
/*  64 */       .add(Biomes.WINDSWEPT_HILLS)
/*  65 */       .add(Biomes.WINDSWEPT_FOREST)
/*  66 */       .add(Biomes.WINDSWEPT_GRAVELLY_HILLS);
/*     */ 
/*     */     
/*  69 */     tag(BiomeTags.IS_TAIGA)
/*  70 */       .add(Biomes.TAIGA)
/*  71 */       .add(Biomes.SNOWY_TAIGA)
/*  72 */       .add(Biomes.OLD_GROWTH_PINE_TAIGA)
/*  73 */       .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
/*     */ 
/*     */     
/*  76 */     tag(BiomeTags.IS_JUNGLE)
/*  77 */       .add(Biomes.BAMBOO_JUNGLE)
/*  78 */       .add(Biomes.JUNGLE)
/*  79 */       .add(Biomes.SPARSE_JUNGLE);
/*     */ 
/*     */     
/*  82 */     tag(BiomeTags.IS_FOREST)
/*  83 */       .add(Biomes.FOREST)
/*  84 */       .add(Biomes.FLOWER_FOREST)
/*  85 */       .add(Biomes.BIRCH_FOREST)
/*  86 */       .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
/*  87 */       .add(Biomes.DARK_FOREST)
/*  88 */       .add(Biomes.PALE_GARDEN)
/*  89 */       .add(Biomes.GROVE);
/*     */ 
/*     */     
/*  92 */     tag(BiomeTags.IS_SAVANNA)
/*  93 */       .add(Biomes.SAVANNA)
/*  94 */       .add(Biomes.SAVANNA_PLATEAU)
/*  95 */       .add(Biomes.WINDSWEPT_SAVANNA);
/*     */ 
/*     */     
/*  98 */     tag(BiomeTags.IS_NETHER).addAll(MultiNoiseBiomeSourceParameterList.Preset.NETHER.usedBiomes());
/*     */     
/* 100 */     List<ResourceKey<Biome>> list = MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD.usedBiomes().toList();
/* 101 */     tag(BiomeTags.IS_OVERWORLD).addAll(list);
/*     */     
/* 103 */     tag(BiomeTags.IS_END)
/* 104 */       .add(Biomes.THE_END)
/* 105 */       .add(Biomes.END_HIGHLANDS)
/* 106 */       .add(Biomes.END_MIDLANDS)
/* 107 */       .add(Biomes.SMALL_END_ISLANDS)
/* 108 */       .add(Biomes.END_BARRENS);
/*     */ 
/*     */ 
/*     */     
/* 112 */     tag(BiomeTags.HAS_BURIED_TREASURE)
/* 113 */       .addTag(BiomeTags.IS_BEACH);
/*     */ 
/*     */     
/* 116 */     tag(BiomeTags.HAS_DESERT_PYRAMID)
/* 117 */       .add(Biomes.DESERT);
/*     */ 
/*     */     
/* 120 */     tag(BiomeTags.HAS_IGLOO)
/* 121 */       .add(Biomes.SNOWY_TAIGA)
/* 122 */       .add(Biomes.SNOWY_PLAINS)
/* 123 */       .add(Biomes.SNOWY_SLOPES);
/*     */ 
/*     */     
/* 126 */     tag(BiomeTags.HAS_JUNGLE_TEMPLE)
/* 127 */       .add(Biomes.BAMBOO_JUNGLE)
/* 128 */       .add(Biomes.JUNGLE);
/*     */ 
/*     */     
/* 131 */     tag(BiomeTags.HAS_MINESHAFT)
/* 132 */       .addTag(BiomeTags.IS_OCEAN)
/* 133 */       .addTag(BiomeTags.IS_RIVER)
/* 134 */       .addTag(BiomeTags.IS_BEACH)
/* 135 */       .addTag(BiomeTags.IS_MOUNTAIN)
/* 136 */       .addTag(BiomeTags.IS_HILL)
/* 137 */       .addTag(BiomeTags.IS_TAIGA)
/* 138 */       .addTag(BiomeTags.IS_JUNGLE)
/* 139 */       .addTag(BiomeTags.IS_FOREST)
/* 140 */       .add(Biomes.STONY_SHORE)
/* 141 */       .add(Biomes.MUSHROOM_FIELDS)
/* 142 */       .add(Biomes.ICE_SPIKES)
/* 143 */       .add(Biomes.WINDSWEPT_SAVANNA)
/* 144 */       .add(Biomes.DESERT)
/* 145 */       .add(Biomes.SAVANNA)
/* 146 */       .add(Biomes.SNOWY_PLAINS)
/* 147 */       .add(Biomes.PLAINS)
/* 148 */       .add(Biomes.SUNFLOWER_PLAINS)
/* 149 */       .add(Biomes.SWAMP)
/* 150 */       .add(Biomes.MANGROVE_SWAMP)
/* 151 */       .add(Biomes.SAVANNA_PLATEAU)
/* 152 */       .add(Biomes.DRIPSTONE_CAVES)
/* 153 */       .add(Biomes.LUSH_CAVES);
/*     */ 
/*     */     
/* 156 */     tag(BiomeTags.HAS_MINESHAFT_MESA)
/* 157 */       .addTag(BiomeTags.IS_BADLANDS);
/*     */ 
/*     */     
/* 160 */     tag(BiomeTags.MINESHAFT_BLOCKING)
/* 161 */       .add(Biomes.DEEP_DARK);
/*     */ 
/*     */     
/* 164 */     tag(BiomeTags.HAS_OCEAN_MONUMENT)
/* 165 */       .addTag(BiomeTags.IS_DEEP_OCEAN);
/*     */ 
/*     */     
/* 168 */     tag(BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING)
/* 169 */       .addTag(BiomeTags.IS_OCEAN)
/* 170 */       .addTag(BiomeTags.IS_RIVER);
/*     */ 
/*     */     
/* 173 */     tag(BiomeTags.HAS_OCEAN_RUIN_COLD)
/* 174 */       .add(Biomes.FROZEN_OCEAN)
/* 175 */       .add(Biomes.COLD_OCEAN)
/* 176 */       .add(Biomes.OCEAN)
/* 177 */       .add(Biomes.DEEP_FROZEN_OCEAN)
/* 178 */       .add(Biomes.DEEP_COLD_OCEAN)
/* 179 */       .add(Biomes.DEEP_OCEAN);
/*     */ 
/*     */     
/* 182 */     tag(BiomeTags.HAS_OCEAN_RUIN_WARM)
/* 183 */       .add(Biomes.LUKEWARM_OCEAN)
/* 184 */       .add(Biomes.WARM_OCEAN)
/* 185 */       .add(Biomes.DEEP_LUKEWARM_OCEAN);
/*     */ 
/*     */     
/* 188 */     tag(BiomeTags.HAS_PILLAGER_OUTPOST)
/* 189 */       .add(Biomes.DESERT)
/* 190 */       .add(Biomes.PLAINS)
/* 191 */       .add(Biomes.SAVANNA)
/* 192 */       .add(Biomes.SNOWY_PLAINS)
/* 193 */       .add(Biomes.TAIGA)
/* 194 */       .addTag(BiomeTags.IS_MOUNTAIN)
/* 195 */       .add(Biomes.GROVE);
/*     */ 
/*     */     
/* 198 */     tag(BiomeTags.HAS_RUINED_PORTAL_DESERT)
/* 199 */       .add(Biomes.DESERT);
/*     */ 
/*     */     
/* 202 */     tag(BiomeTags.HAS_RUINED_PORTAL_JUNGLE)
/* 203 */       .addTag(BiomeTags.IS_JUNGLE);
/*     */ 
/*     */     
/* 206 */     tag(BiomeTags.HAS_RUINED_PORTAL_OCEAN)
/* 207 */       .addTag(BiomeTags.IS_OCEAN);
/*     */ 
/*     */     
/* 210 */     tag(BiomeTags.HAS_RUINED_PORTAL_SWAMP)
/* 211 */       .add(Biomes.SWAMP)
/* 212 */       .add(Biomes.MANGROVE_SWAMP);
/*     */ 
/*     */     
/* 215 */     tag(BiomeTags.HAS_RUINED_PORTAL_MOUNTAIN)
/* 216 */       .addTag(BiomeTags.IS_BADLANDS)
/* 217 */       .addTag(BiomeTags.IS_HILL)
/* 218 */       .add(Biomes.SAVANNA_PLATEAU)
/* 219 */       .add(Biomes.WINDSWEPT_SAVANNA)
/* 220 */       .add(Biomes.STONY_SHORE)
/* 221 */       .addTag(BiomeTags.IS_MOUNTAIN);
/*     */ 
/*     */     
/* 224 */     tag(BiomeTags.HAS_RUINED_PORTAL_STANDARD)
/* 225 */       .addTag(BiomeTags.IS_BEACH)
/* 226 */       .addTag(BiomeTags.IS_RIVER)
/* 227 */       .addTag(BiomeTags.IS_TAIGA)
/* 228 */       .addTag(BiomeTags.IS_FOREST)
/* 229 */       .add(Biomes.MUSHROOM_FIELDS)
/* 230 */       .add(Biomes.ICE_SPIKES)
/* 231 */       .add(Biomes.DRIPSTONE_CAVES)
/* 232 */       .add(Biomes.LUSH_CAVES)
/* 233 */       .add(Biomes.SAVANNA)
/* 234 */       .add(Biomes.SNOWY_PLAINS)
/* 235 */       .add(Biomes.PLAINS)
/* 236 */       .add(Biomes.SUNFLOWER_PLAINS);
/*     */ 
/*     */     
/* 239 */     tag(BiomeTags.HAS_SHIPWRECK_BEACHED)
/* 240 */       .addTag(BiomeTags.IS_BEACH);
/*     */ 
/*     */     
/* 243 */     tag(BiomeTags.HAS_SHIPWRECK)
/* 244 */       .addTag(BiomeTags.IS_OCEAN);
/*     */ 
/*     */     
/* 247 */     tag(BiomeTags.HAS_SWAMP_HUT)
/* 248 */       .add(Biomes.SWAMP);
/*     */ 
/*     */     
/* 251 */     tag(BiomeTags.HAS_VILLAGE_DESERT)
/* 252 */       .add(Biomes.DESERT);
/*     */ 
/*     */     
/* 255 */     tag(BiomeTags.HAS_VILLAGE_PLAINS)
/* 256 */       .add(Biomes.PLAINS)
/* 257 */       .add(Biomes.MEADOW);
/*     */ 
/*     */     
/* 260 */     tag(BiomeTags.HAS_VILLAGE_SAVANNA)
/* 261 */       .add(Biomes.SAVANNA);
/*     */ 
/*     */     
/* 264 */     tag(BiomeTags.HAS_VILLAGE_SNOWY)
/* 265 */       .add(Biomes.SNOWY_PLAINS);
/*     */ 
/*     */     
/* 268 */     tag(BiomeTags.HAS_VILLAGE_TAIGA)
/* 269 */       .add(Biomes.TAIGA);
/*     */ 
/*     */     
/* 272 */     tag(BiomeTags.HAS_TRAIL_RUINS)
/* 273 */       .add(Biomes.TAIGA)
/* 274 */       .add(Biomes.SNOWY_TAIGA)
/* 275 */       .add(Biomes.OLD_GROWTH_PINE_TAIGA)
/* 276 */       .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
/* 277 */       .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
/* 278 */       .add(Biomes.JUNGLE);
/*     */ 
/*     */     
/* 281 */     tag(BiomeTags.HAS_WOODLAND_MANSION)
/* 282 */       .add(Biomes.DARK_FOREST)
/* 283 */       .add(Biomes.PALE_GARDEN);
/*     */ 
/*     */     
/* 286 */     tag(BiomeTags.STRONGHOLD_BIASED_TO)
/* 287 */       .add(Biomes.PLAINS)
/* 288 */       .add(Biomes.SUNFLOWER_PLAINS)
/* 289 */       .add(Biomes.SNOWY_PLAINS)
/* 290 */       .add(Biomes.ICE_SPIKES)
/* 291 */       .add(Biomes.DESERT)
/* 292 */       .add(Biomes.FOREST)
/* 293 */       .add(Biomes.FLOWER_FOREST)
/* 294 */       .add(Biomes.BIRCH_FOREST)
/* 295 */       .add(Biomes.DARK_FOREST)
/* 296 */       .add(Biomes.PALE_GARDEN)
/* 297 */       .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
/* 298 */       .add(Biomes.OLD_GROWTH_PINE_TAIGA)
/* 299 */       .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
/* 300 */       .add(Biomes.TAIGA)
/* 301 */       .add(Biomes.SNOWY_TAIGA)
/* 302 */       .add(Biomes.SAVANNA)
/* 303 */       .add(Biomes.SAVANNA_PLATEAU)
/* 304 */       .add(Biomes.WINDSWEPT_HILLS)
/* 305 */       .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
/* 306 */       .add(Biomes.WINDSWEPT_FOREST)
/* 307 */       .add(Biomes.WINDSWEPT_SAVANNA)
/* 308 */       .add(Biomes.JUNGLE)
/* 309 */       .add(Biomes.SPARSE_JUNGLE)
/* 310 */       .add(Biomes.BAMBOO_JUNGLE)
/* 311 */       .add(Biomes.BADLANDS)
/* 312 */       .add(Biomes.ERODED_BADLANDS)
/* 313 */       .add(Biomes.WOODED_BADLANDS)
/* 314 */       .add(Biomes.MEADOW)
/* 315 */       .add(Biomes.CHERRY_GROVE)
/* 316 */       .add(Biomes.GROVE)
/* 317 */       .add(Biomes.SNOWY_SLOPES)
/* 318 */       .add(Biomes.FROZEN_PEAKS)
/* 319 */       .add(Biomes.JAGGED_PEAKS)
/* 320 */       .add(Biomes.STONY_PEAKS)
/* 321 */       .add(Biomes.MUSHROOM_FIELDS)
/* 322 */       .add(Biomes.DRIPSTONE_CAVES)
/* 323 */       .add(Biomes.LUSH_CAVES);
/*     */ 
/*     */     
/* 326 */     tag(BiomeTags.HAS_STRONGHOLD)
/* 327 */       .addTag(BiomeTags.IS_OVERWORLD);
/*     */     
/* 329 */     tag(BiomeTags.HAS_TRIAL_CHAMBERS).addAll(list.stream().filter(paramResourceKey -> (paramResourceKey != Biomes.DEEP_DARK)));
/*     */ 
/*     */     
/* 332 */     tag(BiomeTags.HAS_NETHER_FORTRESS)
/* 333 */       .addTag(BiomeTags.IS_NETHER);
/*     */ 
/*     */     
/* 336 */     tag(BiomeTags.HAS_NETHER_FOSSIL)
/* 337 */       .add(Biomes.SOUL_SAND_VALLEY);
/*     */ 
/*     */     
/* 340 */     tag(BiomeTags.HAS_BASTION_REMNANT)
/* 341 */       .add(Biomes.CRIMSON_FOREST)
/* 342 */       .add(Biomes.NETHER_WASTES)
/* 343 */       .add(Biomes.SOUL_SAND_VALLEY)
/* 344 */       .add(Biomes.WARPED_FOREST);
/*     */ 
/*     */     
/* 347 */     tag(BiomeTags.HAS_ANCIENT_CITY)
/* 348 */       .add(Biomes.DEEP_DARK);
/*     */ 
/*     */     
/* 351 */     tag(BiomeTags.HAS_RUINED_PORTAL_NETHER)
/* 352 */       .addTag(BiomeTags.IS_NETHER);
/*     */ 
/*     */ 
/*     */     
/* 356 */     tag(BiomeTags.HAS_END_CITY)
/* 357 */       .add(Biomes.END_HIGHLANDS)
/* 358 */       .add(Biomes.END_MIDLANDS);
/*     */ 
/*     */ 
/*     */     
/* 362 */     tag(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)
/* 363 */       .add(Biomes.WARM_OCEAN);
/*     */ 
/*     */ 
/*     */     
/* 367 */     tag(BiomeTags.WATER_ON_MAP_OUTLINES)
/* 368 */       .addTag(BiomeTags.IS_OCEAN)
/* 369 */       .addTag(BiomeTags.IS_RIVER)
/* 370 */       .add(Biomes.SWAMP)
/* 371 */       .add(Biomes.MANGROVE_SWAMP);
/*     */ 
/*     */ 
/*     */     
/* 375 */     tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES)
/* 376 */       .add(Biomes.MUSHROOM_FIELDS);
/*     */ 
/*     */     
/* 379 */     tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)
/* 380 */       .add(Biomes.THE_VOID);
/*     */ 
/*     */     
/* 383 */     tag(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)
/* 384 */       .add(Biomes.SNOWY_PLAINS)
/* 385 */       .add(Biomes.ICE_SPIKES)
/* 386 */       .add(Biomes.FROZEN_PEAKS)
/* 387 */       .add(Biomes.JAGGED_PEAKS)
/* 388 */       .add(Biomes.SNOWY_SLOPES)
/* 389 */       .add(Biomes.FROZEN_OCEAN)
/* 390 */       .add(Biomes.DEEP_FROZEN_OCEAN)
/* 391 */       .add(Biomes.GROVE)
/* 392 */       .add(Biomes.DEEP_DARK)
/* 393 */       .add(Biomes.FROZEN_RIVER)
/* 394 */       .add(Biomes.SNOWY_TAIGA)
/* 395 */       .add(Biomes.SNOWY_BEACH)
/* 396 */       .addTag(BiomeTags.IS_END);
/*     */ 
/*     */     
/* 399 */     tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)
/* 400 */       .add(Biomes.DESERT)
/* 401 */       .add(Biomes.WARM_OCEAN)
/* 402 */       .addTag(BiomeTags.IS_JUNGLE)
/* 403 */       .addTag(BiomeTags.IS_SAVANNA)
/* 404 */       .addTag(BiomeTags.IS_NETHER)
/* 405 */       .addTag(BiomeTags.IS_BADLANDS)
/* 406 */       .add(Biomes.MANGROVE_SWAMP);
/*     */ 
/*     */     
/* 409 */     tag(BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS)
/* 410 */       .add(Biomes.SNOWY_PLAINS)
/* 411 */       .add(Biomes.ICE_SPIKES)
/* 412 */       .add(Biomes.FROZEN_PEAKS)
/* 413 */       .add(Biomes.JAGGED_PEAKS)
/* 414 */       .add(Biomes.SNOWY_SLOPES)
/* 415 */       .add(Biomes.FROZEN_OCEAN)
/* 416 */       .add(Biomes.DEEP_FROZEN_OCEAN)
/* 417 */       .add(Biomes.GROVE)
/* 418 */       .add(Biomes.DEEP_DARK)
/* 419 */       .add(Biomes.FROZEN_RIVER)
/* 420 */       .add(Biomes.SNOWY_TAIGA)
/* 421 */       .add(Biomes.SNOWY_BEACH)
/* 422 */       .addTag(BiomeTags.IS_END)
/* 423 */       .add(Biomes.COLD_OCEAN)
/* 424 */       .add(Biomes.DEEP_COLD_OCEAN)
/* 425 */       .add(Biomes.OLD_GROWTH_PINE_TAIGA)
/* 426 */       .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
/* 427 */       .add(Biomes.TAIGA)
/* 428 */       .add(Biomes.WINDSWEPT_FOREST)
/* 429 */       .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
/* 430 */       .add(Biomes.WINDSWEPT_HILLS)
/* 431 */       .add(Biomes.STONY_PEAKS);
/*     */ 
/*     */     
/* 434 */     tag(BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS)
/* 435 */       .add(Biomes.DESERT)
/* 436 */       .add(Biomes.WARM_OCEAN)
/* 437 */       .addTag(BiomeTags.IS_JUNGLE)
/* 438 */       .addTag(BiomeTags.IS_SAVANNA)
/* 439 */       .addTag(BiomeTags.IS_NETHER)
/* 440 */       .addTag(BiomeTags.IS_BADLANDS)
/* 441 */       .add(Biomes.MANGROVE_SWAMP)
/* 442 */       .add(Biomes.DEEP_LUKEWARM_OCEAN)
/* 443 */       .add(Biomes.LUKEWARM_OCEAN);
/*     */ 
/*     */     
/* 446 */     tag(BiomeTags.SPAWNS_GOLD_RABBITS)
/* 447 */       .add(Biomes.DESERT);
/*     */ 
/*     */     
/* 450 */     tag(BiomeTags.SPAWNS_WHITE_RABBITS)
/* 451 */       .add(Biomes.SNOWY_PLAINS)
/* 452 */       .add(Biomes.ICE_SPIKES)
/* 453 */       .add(Biomes.FROZEN_OCEAN)
/* 454 */       .add(Biomes.SNOWY_TAIGA)
/* 455 */       .add(Biomes.FROZEN_RIVER)
/* 456 */       .add(Biomes.SNOWY_BEACH)
/* 457 */       .add(Biomes.FROZEN_PEAKS)
/* 458 */       .add(Biomes.JAGGED_PEAKS)
/* 459 */       .add(Biomes.SNOWY_SLOPES)
/* 460 */       .add(Biomes.GROVE);
/*     */ 
/*     */     
/* 463 */     tag(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS)
/* 464 */       .addTag(BiomeTags.IS_RIVER);
/*     */ 
/*     */     
/* 467 */     tag(BiomeTags.ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT)
/* 468 */       .add(Biomes.LUSH_CAVES);
/*     */ 
/*     */     
/* 471 */     tag(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS)
/* 472 */       .add(Biomes.FROZEN_OCEAN)
/* 473 */       .add(Biomes.DEEP_FROZEN_OCEAN);
/*     */ 
/*     */     
/* 476 */     tag(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)
/* 477 */       .addTag(BiomeTags.IS_RIVER);
/*     */ 
/*     */     
/* 480 */     tag(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS)
/* 481 */       .add(Biomes.SWAMP)
/* 482 */       .add(Biomes.MANGROVE_SWAMP);
/*     */ 
/*     */     
/* 485 */     tag(BiomeTags.SPAWNS_SNOW_FOXES)
/* 486 */       .add(Biomes.SNOWY_PLAINS)
/* 487 */       .add(Biomes.ICE_SPIKES)
/* 488 */       .add(Biomes.FROZEN_OCEAN)
/* 489 */       .add(Biomes.SNOWY_TAIGA)
/* 490 */       .add(Biomes.FROZEN_RIVER)
/* 491 */       .add(Biomes.SNOWY_BEACH)
/* 492 */       .add(Biomes.FROZEN_PEAKS)
/* 493 */       .add(Biomes.JAGGED_PEAKS)
/* 494 */       .add(Biomes.SNOWY_SLOPES)
/* 495 */       .add(Biomes.GROVE);
/*     */ 
/*     */     
/* 498 */     tag(BiomeTags.SPAWNS_CORAL_VARIANT_ZOMBIE_NAUTILUS)
/* 499 */       .add(Biomes.WARM_OCEAN);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\BiomeTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */