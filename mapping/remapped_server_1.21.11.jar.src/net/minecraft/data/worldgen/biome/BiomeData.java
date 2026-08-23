/*     */ package net.minecraft.data.worldgen.biome;
/*     */ 
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ public abstract class BiomeData {
/*     */   public static void bootstrap(BootstrapContext<Biome> paramBootstrapContext) {
/*  13 */     HolderGetter<PlacedFeature> holderGetter = paramBootstrapContext.lookup(Registries.PLACED_FEATURE);
/*  14 */     HolderGetter<ConfiguredWorldCarver<?>> holderGetter1 = paramBootstrapContext.lookup(Registries.CONFIGURED_CARVER);
/*     */     
/*  16 */     paramBootstrapContext.register(Biomes.THE_VOID, OverworldBiomes.theVoid(holderGetter, holderGetter1));
/*     */     
/*  18 */     paramBootstrapContext.register(Biomes.PLAINS, OverworldBiomes.plains(holderGetter, holderGetter1, false, false, false));
/*  19 */     paramBootstrapContext.register(Biomes.SUNFLOWER_PLAINS, OverworldBiomes.plains(holderGetter, holderGetter1, true, false, false));
/*     */     
/*  21 */     paramBootstrapContext.register(Biomes.SNOWY_PLAINS, OverworldBiomes.plains(holderGetter, holderGetter1, false, true, false));
/*  22 */     paramBootstrapContext.register(Biomes.ICE_SPIKES, OverworldBiomes.plains(holderGetter, holderGetter1, false, true, true));
/*     */     
/*  24 */     paramBootstrapContext.register(Biomes.DESERT, OverworldBiomes.desert(holderGetter, holderGetter1));
/*     */     
/*  26 */     paramBootstrapContext.register(Biomes.SWAMP, OverworldBiomes.swamp(holderGetter, holderGetter1));
/*  27 */     paramBootstrapContext.register(Biomes.MANGROVE_SWAMP, OverworldBiomes.mangroveSwamp(holderGetter, holderGetter1));
/*     */     
/*  29 */     paramBootstrapContext.register(Biomes.FOREST, OverworldBiomes.forest(holderGetter, holderGetter1, false, false, false));
/*  30 */     paramBootstrapContext.register(Biomes.FLOWER_FOREST, OverworldBiomes.forest(holderGetter, holderGetter1, false, false, true));
/*  31 */     paramBootstrapContext.register(Biomes.BIRCH_FOREST, OverworldBiomes.forest(holderGetter, holderGetter1, true, false, false));
/*     */     
/*  33 */     paramBootstrapContext.register(Biomes.DARK_FOREST, OverworldBiomes.darkForest(holderGetter, holderGetter1, false));
/*  34 */     paramBootstrapContext.register(Biomes.PALE_GARDEN, OverworldBiomes.darkForest(holderGetter, holderGetter1, true));
/*  35 */     paramBootstrapContext.register(Biomes.OLD_GROWTH_BIRCH_FOREST, OverworldBiomes.forest(holderGetter, holderGetter1, true, true, false));
/*  36 */     paramBootstrapContext.register(Biomes.OLD_GROWTH_PINE_TAIGA, OverworldBiomes.oldGrowthTaiga(holderGetter, holderGetter1, false));
/*  37 */     paramBootstrapContext.register(Biomes.OLD_GROWTH_SPRUCE_TAIGA, OverworldBiomes.oldGrowthTaiga(holderGetter, holderGetter1, true));
/*     */     
/*  39 */     paramBootstrapContext.register(Biomes.TAIGA, OverworldBiomes.taiga(holderGetter, holderGetter1, false));
/*  40 */     paramBootstrapContext.register(Biomes.SNOWY_TAIGA, OverworldBiomes.taiga(holderGetter, holderGetter1, true));
/*     */     
/*  42 */     paramBootstrapContext.register(Biomes.SAVANNA, OverworldBiomes.savanna(holderGetter, holderGetter1, false, false));
/*  43 */     paramBootstrapContext.register(Biomes.SAVANNA_PLATEAU, OverworldBiomes.savanna(holderGetter, holderGetter1, false, true));
/*     */     
/*  45 */     paramBootstrapContext.register(Biomes.WINDSWEPT_HILLS, OverworldBiomes.windsweptHills(holderGetter, holderGetter1, false));
/*  46 */     paramBootstrapContext.register(Biomes.WINDSWEPT_GRAVELLY_HILLS, OverworldBiomes.windsweptHills(holderGetter, holderGetter1, false));
/*  47 */     paramBootstrapContext.register(Biomes.WINDSWEPT_FOREST, OverworldBiomes.windsweptHills(holderGetter, holderGetter1, true));
/*  48 */     paramBootstrapContext.register(Biomes.WINDSWEPT_SAVANNA, OverworldBiomes.savanna(holderGetter, holderGetter1, true, false));
/*     */     
/*  50 */     paramBootstrapContext.register(Biomes.JUNGLE, OverworldBiomes.jungle(holderGetter, holderGetter1));
/*  51 */     paramBootstrapContext.register(Biomes.SPARSE_JUNGLE, OverworldBiomes.sparseJungle(holderGetter, holderGetter1));
/*  52 */     paramBootstrapContext.register(Biomes.BAMBOO_JUNGLE, OverworldBiomes.bambooJungle(holderGetter, holderGetter1));
/*     */     
/*  54 */     paramBootstrapContext.register(Biomes.BADLANDS, OverworldBiomes.badlands(holderGetter, holderGetter1, false));
/*  55 */     paramBootstrapContext.register(Biomes.ERODED_BADLANDS, OverworldBiomes.badlands(holderGetter, holderGetter1, false));
/*  56 */     paramBootstrapContext.register(Biomes.WOODED_BADLANDS, OverworldBiomes.badlands(holderGetter, holderGetter1, true));
/*     */     
/*  58 */     paramBootstrapContext.register(Biomes.MEADOW, OverworldBiomes.meadowOrCherryGrove(holderGetter, holderGetter1, false));
/*  59 */     paramBootstrapContext.register(Biomes.CHERRY_GROVE, OverworldBiomes.meadowOrCherryGrove(holderGetter, holderGetter1, true));
/*  60 */     paramBootstrapContext.register(Biomes.GROVE, OverworldBiomes.grove(holderGetter, holderGetter1));
/*     */     
/*  62 */     paramBootstrapContext.register(Biomes.SNOWY_SLOPES, OverworldBiomes.snowySlopes(holderGetter, holderGetter1));
/*  63 */     paramBootstrapContext.register(Biomes.FROZEN_PEAKS, OverworldBiomes.frozenPeaks(holderGetter, holderGetter1));
/*  64 */     paramBootstrapContext.register(Biomes.JAGGED_PEAKS, OverworldBiomes.jaggedPeaks(holderGetter, holderGetter1));
/*  65 */     paramBootstrapContext.register(Biomes.STONY_PEAKS, OverworldBiomes.stonyPeaks(holderGetter, holderGetter1));
/*     */     
/*  67 */     paramBootstrapContext.register(Biomes.RIVER, OverworldBiomes.river(holderGetter, holderGetter1, false));
/*  68 */     paramBootstrapContext.register(Biomes.FROZEN_RIVER, OverworldBiomes.river(holderGetter, holderGetter1, true));
/*     */     
/*  70 */     paramBootstrapContext.register(Biomes.BEACH, OverworldBiomes.beach(holderGetter, holderGetter1, false, false));
/*  71 */     paramBootstrapContext.register(Biomes.SNOWY_BEACH, OverworldBiomes.beach(holderGetter, holderGetter1, true, false));
/*  72 */     paramBootstrapContext.register(Biomes.STONY_SHORE, OverworldBiomes.beach(holderGetter, holderGetter1, false, true));
/*     */     
/*  74 */     paramBootstrapContext.register(Biomes.WARM_OCEAN, OverworldBiomes.warmOcean(holderGetter, holderGetter1));
/*  75 */     paramBootstrapContext.register(Biomes.LUKEWARM_OCEAN, OverworldBiomes.lukeWarmOcean(holderGetter, holderGetter1, false));
/*  76 */     paramBootstrapContext.register(Biomes.DEEP_LUKEWARM_OCEAN, OverworldBiomes.lukeWarmOcean(holderGetter, holderGetter1, true));
/*  77 */     paramBootstrapContext.register(Biomes.OCEAN, OverworldBiomes.ocean(holderGetter, holderGetter1, false));
/*  78 */     paramBootstrapContext.register(Biomes.DEEP_OCEAN, OverworldBiomes.ocean(holderGetter, holderGetter1, true));
/*  79 */     paramBootstrapContext.register(Biomes.COLD_OCEAN, OverworldBiomes.coldOcean(holderGetter, holderGetter1, false));
/*  80 */     paramBootstrapContext.register(Biomes.DEEP_COLD_OCEAN, OverworldBiomes.coldOcean(holderGetter, holderGetter1, true));
/*  81 */     paramBootstrapContext.register(Biomes.FROZEN_OCEAN, OverworldBiomes.frozenOcean(holderGetter, holderGetter1, false));
/*  82 */     paramBootstrapContext.register(Biomes.DEEP_FROZEN_OCEAN, OverworldBiomes.frozenOcean(holderGetter, holderGetter1, true));
/*     */     
/*  84 */     paramBootstrapContext.register(Biomes.MUSHROOM_FIELDS, OverworldBiomes.mushroomFields(holderGetter, holderGetter1));
/*     */     
/*  86 */     paramBootstrapContext.register(Biomes.DRIPSTONE_CAVES, OverworldBiomes.dripstoneCaves(holderGetter, holderGetter1));
/*  87 */     paramBootstrapContext.register(Biomes.LUSH_CAVES, OverworldBiomes.lushCaves(holderGetter, holderGetter1));
/*  88 */     paramBootstrapContext.register(Biomes.DEEP_DARK, OverworldBiomes.deepDark(holderGetter, holderGetter1));
/*     */     
/*  90 */     paramBootstrapContext.register(Biomes.NETHER_WASTES, NetherBiomes.netherWastes(holderGetter, holderGetter1));
/*  91 */     paramBootstrapContext.register(Biomes.WARPED_FOREST, NetherBiomes.warpedForest(holderGetter, holderGetter1));
/*  92 */     paramBootstrapContext.register(Biomes.CRIMSON_FOREST, NetherBiomes.crimsonForest(holderGetter, holderGetter1));
/*  93 */     paramBootstrapContext.register(Biomes.SOUL_SAND_VALLEY, NetherBiomes.soulSandValley(holderGetter, holderGetter1));
/*  94 */     paramBootstrapContext.register(Biomes.BASALT_DELTAS, NetherBiomes.basaltDeltas(holderGetter, holderGetter1));
/*     */     
/*  96 */     paramBootstrapContext.register(Biomes.THE_END, EndBiomes.theEnd(holderGetter, holderGetter1));
/*  97 */     paramBootstrapContext.register(Biomes.END_HIGHLANDS, EndBiomes.endHighlands(holderGetter, holderGetter1));
/*  98 */     paramBootstrapContext.register(Biomes.END_MIDLANDS, EndBiomes.endMidlands(holderGetter, holderGetter1));
/*  99 */     paramBootstrapContext.register(Biomes.SMALL_END_ISLANDS, EndBiomes.smallEndIslands(holderGetter, holderGetter1));
/* 100 */     paramBootstrapContext.register(Biomes.END_BARRENS, EndBiomes.endBarrens(holderGetter, holderGetter1));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\biome\BiomeData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */