/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistrySetBuilder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.NoiseData;
/*     */ import net.minecraft.data.worldgen.TerrainProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.BoundedFloatFunction;
/*     */ import net.minecraft.util.CubicSpline;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.DensityFunctions;
/*     */ import net.minecraft.world.level.levelgen.NoiseRouterData;
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
/*     */ public final class OverworldBiomeBuilder
/*     */ {
/*     */   private static final float VALLEY_SIZE = 0.05F;
/*     */   private static final float LOW_START = 0.26666668F;
/*     */   public static final float HIGH_START = 0.4F;
/*     */   private static final float HIGH_END = 0.93333334F;
/*     */   private static final float PEAK_SIZE = 0.1F;
/*     */   public static final float PEAK_START = 0.56666666F;
/*     */   private static final float PEAK_END = 0.7666667F;
/*     */   public static final float NEAR_INLAND_START = -0.11F;
/*     */   public static final float MID_INLAND_START = 0.03F;
/*     */   public static final float FAR_INLAND_START = 0.3F;
/*     */   public static final float EROSION_INDEX_1_START = -0.78F;
/*     */   public static final float EROSION_INDEX_2_START = -0.375F;
/*     */   private static final float EROSION_DEEP_DARK_DRYNESS_THRESHOLD = -0.225F;
/*     */   private static final float DEPTH_DEEP_DARK_DRYNESS_THRESHOLD = 0.9F;
/*  63 */   private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
/*     */   
/*  65 */   private final Climate.Parameter[] temperatures = new Climate.Parameter[] {
/*  66 */       Climate.Parameter.span(-1.0F, -0.45F), 
/*  67 */       Climate.Parameter.span(-0.45F, -0.15F), 
/*  68 */       Climate.Parameter.span(-0.15F, 0.2F), 
/*  69 */       Climate.Parameter.span(0.2F, 0.55F), 
/*  70 */       Climate.Parameter.span(0.55F, 1.0F)
/*     */     };
/*  72 */   private final Climate.Parameter[] humidities = new Climate.Parameter[] {
/*  73 */       Climate.Parameter.span(-1.0F, -0.35F), 
/*  74 */       Climate.Parameter.span(-0.35F, -0.1F), 
/*  75 */       Climate.Parameter.span(-0.1F, 0.1F), 
/*  76 */       Climate.Parameter.span(0.1F, 0.3F), 
/*  77 */       Climate.Parameter.span(0.3F, 1.0F)
/*     */     };
/*     */   
/*  80 */   private final Climate.Parameter[] erosions = new Climate.Parameter[] {
/*  81 */       Climate.Parameter.span(-1.0F, -0.78F), 
/*  82 */       Climate.Parameter.span(-0.78F, -0.375F), 
/*  83 */       Climate.Parameter.span(-0.375F, -0.2225F), 
/*  84 */       Climate.Parameter.span(-0.2225F, 0.05F), 
/*  85 */       Climate.Parameter.span(0.05F, 0.45F), 
/*  86 */       Climate.Parameter.span(0.45F, 0.55F), 
/*  87 */       Climate.Parameter.span(0.55F, 1.0F)
/*     */     };
/*     */   
/*  90 */   private final Climate.Parameter FROZEN_RANGE = this.temperatures[0];
/*  91 */   private final Climate.Parameter UNFROZEN_RANGE = Climate.Parameter.span(this.temperatures[1], this.temperatures[4]);
/*     */   
/*  93 */   private final Climate.Parameter mushroomFieldsContinentalness = Climate.Parameter.span(-1.2F, -1.05F);
/*  94 */   private final Climate.Parameter deepOceanContinentalness = Climate.Parameter.span(-1.05F, -0.455F);
/*  95 */   private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.455F, -0.19F);
/*  96 */   private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.11F);
/*  97 */   private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
/*     */   
/*  99 */   private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.11F, 0.03F);
/* 100 */   private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
/* 101 */   private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
/*     */ 
/*     */   
/* 104 */   private final ResourceKey<Biome>[][] OCEANS = new ResourceKey[][] { { Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN }, { Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][] { { Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA }, { Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA }, { Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST }, { Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE }, { Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT } };
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
/* 122 */   private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][] { { Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null }, { null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA }, { Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null }, { null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE }, { null, null, null, null, null } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][] { { Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA }, { Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA }, { Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.PALE_GARDEN }, { Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE }, { Biomes.BADLANDS, Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][] { { Biomes.ICE_SPIKES, null, null, null, null }, { Biomes.CHERRY_GROVE, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.OLD_GROWTH_PINE_TAIGA }, { Biomes.CHERRY_GROVE, Biomes.CHERRY_GROVE, Biomes.FOREST, Biomes.BIRCH_FOREST, null }, { null, null, null, null, null }, { Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null } };
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
/* 153 */   private final ResourceKey<Biome>[][] SHATTERED_BIOMES = new ResourceKey[][] { { Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST }, { Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST }, { Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST }, { null, null, null, null, null }, { null, null, null, null, null } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Climate.ParameterPoint> spawnTarget() {
/* 162 */     Climate.Parameter parameter = Climate.Parameter.point(0.0F);
/* 163 */     float f = 0.16F;
/* 164 */     return List.of(new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, 
/*     */ 
/*     */ 
/*     */           
/* 168 */           Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, parameter, 
/*     */ 
/*     */           
/* 171 */           Climate.Parameter.span(-1.0F, -0.16F), 0L), new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 177 */           Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, parameter, 
/*     */ 
/*     */           
/* 180 */           Climate.Parameter.span(0.16F, 1.0F), 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer) {
/* 187 */     if (SharedConstants.debugGenerateSquareTerrainWithoutNoise) {
/* 188 */       addDebugBiomes(paramConsumer);
/*     */       
/*     */       return;
/*     */     } 
/* 192 */     addOffCoastBiomes(paramConsumer);
/* 193 */     addInlandBiomes(paramConsumer);
/* 194 */     addUndergroundBiomes(paramConsumer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addDebugBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer) {
/* 201 */     HolderLookup.Provider provider = (new RegistrySetBuilder()).add(Registries.DENSITY_FUNCTION, NoiseRouterData::bootstrap).add(Registries.NOISE, NoiseData::bootstrap).build((RegistryAccess)RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
/*     */     
/* 203 */     HolderLookup.RegistryLookup registryLookup = provider.lookupOrThrow(Registries.DENSITY_FUNCTION);
/* 204 */     DensityFunctions.Spline.Coordinate coordinate1 = new DensityFunctions.Spline.Coordinate((Holder)registryLookup.getOrThrow(NoiseRouterData.CONTINENTS));
/* 205 */     DensityFunctions.Spline.Coordinate coordinate2 = new DensityFunctions.Spline.Coordinate((Holder)registryLookup.getOrThrow(NoiseRouterData.EROSION));
/* 206 */     DensityFunctions.Spline.Coordinate coordinate3 = new DensityFunctions.Spline.Coordinate((Holder)registryLookup.getOrThrow(NoiseRouterData.RIDGES_FOLDED));
/*     */     
/* 208 */     paramConsumer.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.01F), Biomes.PLAINS));
/*     */     
/* 210 */     CubicSpline cubicSpline1 = TerrainProvider.buildErosionOffsetSpline((BoundedFloatFunction)coordinate2, (BoundedFloatFunction)coordinate3, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, BoundedFloatFunction.IDENTITY);
/* 211 */     if (cubicSpline1 instanceof CubicSpline.Multipoint) { CubicSpline.Multipoint multipoint = (CubicSpline.Multipoint)cubicSpline1;
/* 212 */       ResourceKey<Biome> resourceKey = Biomes.DESERT;
/* 213 */       for (float f : multipoint.locations()) {
/* 214 */         paramConsumer.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point(f), Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.0F), resourceKey));
/* 215 */         resourceKey = (resourceKey == Biomes.DESERT) ? Biomes.BADLANDS : Biomes.DESERT;
/*     */       }  }
/*     */ 
/*     */     
/* 219 */     CubicSpline cubicSpline2 = TerrainProvider.overworldOffset((BoundedFloatFunction)coordinate1, (BoundedFloatFunction)coordinate2, (BoundedFloatFunction)coordinate3, false);
/* 220 */     if (cubicSpline2 instanceof CubicSpline.Multipoint) { CubicSpline.Multipoint multipoint = (CubicSpline.Multipoint)cubicSpline2;
/* 221 */       for (float f : multipoint.locations()) {
/* 222 */         paramConsumer.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point(f), this.FULL_RANGE, Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.0F), Biomes.SNOWY_TAIGA));
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer) {
/* 231 */     addSurfaceBiome(paramConsumer, this.FULL_RANGE, this.FULL_RANGE, this.mushroomFieldsContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.MUSHROOM_FIELDS);
/* 232 */     for (byte b = 0; b < this.temperatures.length; b++) {
/* 233 */       Climate.Parameter parameter = this.temperatures[b];
/*     */       
/* 235 */       addSurfaceBiome(paramConsumer, parameter, this.FULL_RANGE, this.deepOceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[0][b]);
/* 236 */       addSurfaceBiome(paramConsumer, parameter, this.FULL_RANGE, this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[1][b]);
/*     */     } 
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
/*     */ 
/*     */   
/*     */   private void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer) {
/* 256 */     addMidSlice(paramConsumer, Climate.Parameter.span(-1.0F, -0.93333334F));
/*     */ 
/*     */     
/* 259 */     addHighSlice(paramConsumer, Climate.Parameter.span(-0.93333334F, -0.7666667F));
/* 260 */     addPeaks(paramConsumer, Climate.Parameter.span(-0.7666667F, -0.56666666F));
/* 261 */     addHighSlice(paramConsumer, Climate.Parameter.span(-0.56666666F, -0.4F));
/*     */ 
/*     */     
/* 264 */     addMidSlice(paramConsumer, Climate.Parameter.span(-0.4F, -0.26666668F));
/*     */ 
/*     */     
/* 267 */     addLowSlice(paramConsumer, Climate.Parameter.span(-0.26666668F, -0.05F));
/* 268 */     addValleys(paramConsumer, Climate.Parameter.span(-0.05F, 0.05F));
/* 269 */     addLowSlice(paramConsumer, Climate.Parameter.span(0.05F, 0.26666668F));
/*     */ 
/*     */     
/* 272 */     addMidSlice(paramConsumer, Climate.Parameter.span(0.26666668F, 0.4F));
/*     */ 
/*     */     
/* 275 */     addHighSlice(paramConsumer, Climate.Parameter.span(0.4F, 0.56666666F));
/* 276 */     addPeaks(paramConsumer, Climate.Parameter.span(0.56666666F, 0.7666667F));
/* 277 */     addHighSlice(paramConsumer, Climate.Parameter.span(0.7666667F, 0.93333334F));
/*     */ 
/*     */     
/* 280 */     addMidSlice(paramConsumer, Climate.Parameter.span(0.93333334F, 1.0F));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer, Climate.Parameter paramParameter) {
/* 288 */     for (byte b = 0; b < this.temperatures.length; b++) {
/* 289 */       Climate.Parameter parameter = this.temperatures[b];
/* 290 */       for (byte b1 = 0; b1 < this.humidities.length; b1++) {
/* 291 */         Climate.Parameter parameter1 = this.humidities[b1];
/*     */         
/* 293 */         ResourceKey<Biome> resourceKey1 = pickMiddleBiome(b, b1, paramParameter);
/* 294 */         ResourceKey<Biome> resourceKey2 = pickMiddleBiomeOrBadlandsIfHot(b, b1, paramParameter);
/* 295 */         ResourceKey<Biome> resourceKey3 = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(b, b1, paramParameter);
/* 296 */         ResourceKey<Biome> resourceKey4 = pickPlateauBiome(b, b1, paramParameter);
/* 297 */         ResourceKey<Biome> resourceKey5 = pickShatteredBiome(b, b1, paramParameter);
/* 298 */         ResourceKey<Biome> resourceKey6 = maybePickWindsweptSavannaBiome(b, b1, paramParameter, resourceKey5);
/* 299 */         ResourceKey<Biome> resourceKey7 = pickPeakBiome(b, b1, paramParameter);
/*     */         
/* 301 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[0], paramParameter, 0.0F, resourceKey7);
/*     */         
/* 303 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[1], paramParameter, 0.0F, resourceKey3);
/*     */         
/* 305 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], paramParameter, 0.0F, resourceKey7);
/*     */         
/* 307 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), paramParameter, 0.0F, resourceKey1);
/* 308 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], paramParameter, 0.0F, resourceKey4);
/*     */         
/* 310 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.midInlandContinentalness, this.erosions[3], paramParameter, 0.0F, resourceKey2);
/* 311 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.farInlandContinentalness, this.erosions[3], paramParameter, 0.0F, resourceKey4);
/*     */         
/* 313 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], paramParameter, 0.0F, resourceKey1);
/*     */         
/* 315 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[5], paramParameter, 0.0F, resourceKey6);
/* 316 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], paramParameter, 0.0F, resourceKey5);
/*     */         
/* 318 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, resourceKey1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addHighSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer, Climate.Parameter paramParameter) {
/* 329 */     for (byte b = 0; b < this.temperatures.length; b++) {
/* 330 */       Climate.Parameter parameter = this.temperatures[b];
/* 331 */       for (byte b1 = 0; b1 < this.humidities.length; b1++) {
/* 332 */         Climate.Parameter parameter1 = this.humidities[b1];
/*     */         
/* 334 */         ResourceKey<Biome> resourceKey1 = pickMiddleBiome(b, b1, paramParameter);
/* 335 */         ResourceKey<Biome> resourceKey2 = pickMiddleBiomeOrBadlandsIfHot(b, b1, paramParameter);
/* 336 */         ResourceKey<Biome> resourceKey3 = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(b, b1, paramParameter);
/* 337 */         ResourceKey<Biome> resourceKey4 = pickPlateauBiome(b, b1, paramParameter);
/* 338 */         ResourceKey<Biome> resourceKey5 = pickShatteredBiome(b, b1, paramParameter);
/* 339 */         ResourceKey<Biome> resourceKey6 = maybePickWindsweptSavannaBiome(b, b1, paramParameter, resourceKey1);
/* 340 */         ResourceKey<Biome> resourceKey7 = pickSlopeBiome(b, b1, paramParameter);
/* 341 */         ResourceKey<Biome> resourceKey8 = pickPeakBiome(b, b1, paramParameter);
/*     */         
/* 343 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), paramParameter, 0.0F, resourceKey1);
/* 344 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.nearInlandContinentalness, this.erosions[0], paramParameter, 0.0F, resourceKey7);
/* 345 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[0], paramParameter, 0.0F, resourceKey8);
/*     */         
/* 347 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.nearInlandContinentalness, this.erosions[1], paramParameter, 0.0F, resourceKey3);
/* 348 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], paramParameter, 0.0F, resourceKey7);
/*     */         
/* 350 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), paramParameter, 0.0F, resourceKey1);
/* 351 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], paramParameter, 0.0F, resourceKey4);
/*     */         
/* 353 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.midInlandContinentalness, this.erosions[3], paramParameter, 0.0F, resourceKey2);
/* 354 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.farInlandContinentalness, this.erosions[3], paramParameter, 0.0F, resourceKey4);
/*     */         
/* 356 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], paramParameter, 0.0F, resourceKey1);
/*     */         
/* 358 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[5], paramParameter, 0.0F, resourceKey6);
/* 359 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], paramParameter, 0.0F, resourceKey5);
/*     */         
/* 361 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, resourceKey1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer, Climate.Parameter paramParameter) {
/* 371 */     addSurfaceBiome(paramConsumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), paramParameter, 0.0F, Biomes.STONY_SHORE);
/* 372 */     addSurfaceBiome(paramConsumer, Climate.Parameter.span(this.temperatures[1], this.temperatures[2]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, Biomes.SWAMP);
/* 373 */     addSurfaceBiome(paramConsumer, Climate.Parameter.span(this.temperatures[3], this.temperatures[4]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, Biomes.MANGROVE_SWAMP);
/*     */     
/* 375 */     for (byte b = 0; b < this.temperatures.length; b++) {
/* 376 */       Climate.Parameter parameter = this.temperatures[b];
/* 377 */       for (byte b1 = 0; b1 < this.humidities.length; b1++) {
/* 378 */         Climate.Parameter parameter1 = this.humidities[b1];
/*     */         
/* 380 */         ResourceKey<Biome> resourceKey1 = pickMiddleBiome(b, b1, paramParameter);
/* 381 */         ResourceKey<Biome> resourceKey2 = pickMiddleBiomeOrBadlandsIfHot(b, b1, paramParameter);
/* 382 */         ResourceKey<Biome> resourceKey3 = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(b, b1, paramParameter);
/* 383 */         ResourceKey<Biome> resourceKey4 = pickShatteredBiome(b, b1, paramParameter);
/* 384 */         ResourceKey<Biome> resourceKey5 = pickPlateauBiome(b, b1, paramParameter);
/* 385 */         ResourceKey<Biome> resourceKey6 = pickBeachBiome(b, b1);
/* 386 */         ResourceKey<Biome> resourceKey7 = maybePickWindsweptSavannaBiome(b, b1, paramParameter, resourceKey1);
/* 387 */         ResourceKey<Biome> resourceKey8 = pickShatteredCoastBiome(b, b1, paramParameter);
/* 388 */         ResourceKey<Biome> resourceKey9 = pickSlopeBiome(b, b1, paramParameter);
/*     */         
/* 390 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[0], paramParameter, 0.0F, resourceKey9);
/*     */         
/* 392 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.midInlandContinentalness), this.erosions[1], paramParameter, 0.0F, resourceKey3);
/* 393 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.farInlandContinentalness, this.erosions[1], paramParameter, 0.0F, (b == 0) ? resourceKey9 : resourceKey5);
/*     */         
/* 395 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.nearInlandContinentalness, this.erosions[2], paramParameter, 0.0F, resourceKey1);
/* 396 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.midInlandContinentalness, this.erosions[2], paramParameter, 0.0F, resourceKey2);
/* 397 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.farInlandContinentalness, this.erosions[2], paramParameter, 0.0F, resourceKey5);
/*     */         
/* 399 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[3], paramParameter, 0.0F, resourceKey1);
/* 400 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[3], paramParameter, 0.0F, resourceKey2);
/*     */         
/* 402 */         if (paramParameter.max() < 0L) {
/* 403 */           addSurfaceBiome(paramConsumer, parameter, parameter1, this.coastContinentalness, this.erosions[4], paramParameter, 0.0F, resourceKey6);
/* 404 */           addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], paramParameter, 0.0F, resourceKey1);
/*     */         } else {
/* 406 */           addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], paramParameter, 0.0F, resourceKey1);
/*     */         } 
/*     */         
/* 409 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.coastContinentalness, this.erosions[5], paramParameter, 0.0F, resourceKey8);
/* 410 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.nearInlandContinentalness, this.erosions[5], paramParameter, 0.0F, resourceKey7);
/* 411 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], paramParameter, 0.0F, resourceKey4);
/*     */         
/* 413 */         if (paramParameter.max() < 0L) {
/* 414 */           addSurfaceBiome(paramConsumer, parameter, parameter1, this.coastContinentalness, this.erosions[6], paramParameter, 0.0F, resourceKey6);
/*     */         } else {
/* 416 */           addSurfaceBiome(paramConsumer, parameter, parameter1, this.coastContinentalness, this.erosions[6], paramParameter, 0.0F, resourceKey1);
/*     */         } 
/*     */ 
/*     */         
/* 420 */         if (b == 0) {
/* 421 */           addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, resourceKey1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer, Climate.Parameter paramParameter) {
/* 432 */     addSurfaceBiome(paramConsumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), paramParameter, 0.0F, Biomes.STONY_SHORE);
/* 433 */     addSurfaceBiome(paramConsumer, Climate.Parameter.span(this.temperatures[1], this.temperatures[2]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, Biomes.SWAMP);
/* 434 */     addSurfaceBiome(paramConsumer, Climate.Parameter.span(this.temperatures[3], this.temperatures[4]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, Biomes.MANGROVE_SWAMP);
/*     */     
/* 436 */     for (byte b = 0; b < this.temperatures.length; b++) {
/* 437 */       Climate.Parameter parameter = this.temperatures[b];
/* 438 */       for (byte b1 = 0; b1 < this.humidities.length; b1++) {
/* 439 */         Climate.Parameter parameter1 = this.humidities[b1];
/*     */         
/* 441 */         ResourceKey<Biome> resourceKey1 = pickMiddleBiome(b, b1, paramParameter);
/* 442 */         ResourceKey<Biome> resourceKey2 = pickMiddleBiomeOrBadlandsIfHot(b, b1, paramParameter);
/* 443 */         ResourceKey<Biome> resourceKey3 = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(b, b1, paramParameter);
/* 444 */         ResourceKey<Biome> resourceKey4 = pickBeachBiome(b, b1);
/* 445 */         ResourceKey<Biome> resourceKey5 = maybePickWindsweptSavannaBiome(b, b1, paramParameter, resourceKey1);
/* 446 */         ResourceKey<Biome> resourceKey6 = pickShatteredCoastBiome(b, b1, paramParameter);
/*     */         
/* 448 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), paramParameter, 0.0F, resourceKey2);
/* 449 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), paramParameter, 0.0F, resourceKey3);
/*     */         
/* 451 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[2], this.erosions[3]), paramParameter, 0.0F, resourceKey1);
/* 452 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), paramParameter, 0.0F, resourceKey2);
/*     */         
/* 454 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.coastContinentalness, Climate.Parameter.span(this.erosions[3], this.erosions[4]), paramParameter, 0.0F, resourceKey4);
/*     */         
/* 456 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], paramParameter, 0.0F, resourceKey1);
/*     */         
/* 458 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.coastContinentalness, this.erosions[5], paramParameter, 0.0F, resourceKey6);
/* 459 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.nearInlandContinentalness, this.erosions[5], paramParameter, 0.0F, resourceKey5);
/* 460 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], paramParameter, 0.0F, resourceKey1);
/*     */         
/* 462 */         addSurfaceBiome(paramConsumer, parameter, parameter1, this.coastContinentalness, this.erosions[6], paramParameter, 0.0F, resourceKey4);
/*     */ 
/*     */         
/* 465 */         if (b == 0) {
/* 466 */           addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, resourceKey1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer, Climate.Parameter paramParameter) {
/* 477 */     addSurfaceBiome(paramConsumer, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), paramParameter, 0.0F, (paramParameter.max() < 0L) ? Biomes.STONY_SHORE : Biomes.FROZEN_RIVER);
/* 478 */     addSurfaceBiome(paramConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), paramParameter, 0.0F, (paramParameter.max() < 0L) ? Biomes.STONY_SHORE : Biomes.RIVER);
/*     */     
/* 480 */     addSurfaceBiome(paramConsumer, this.FROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), paramParameter, 0.0F, Biomes.FROZEN_RIVER);
/* 481 */     addSurfaceBiome(paramConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), paramParameter, 0.0F, Biomes.RIVER);
/*     */     
/* 483 */     addSurfaceBiome(paramConsumer, this.FROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), paramParameter, 0.0F, Biomes.FROZEN_RIVER);
/* 484 */     addSurfaceBiome(paramConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), paramParameter, 0.0F, Biomes.RIVER);
/*     */     
/* 486 */     addSurfaceBiome(paramConsumer, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], paramParameter, 0.0F, Biomes.FROZEN_RIVER);
/* 487 */     addSurfaceBiome(paramConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], paramParameter, 0.0F, Biomes.RIVER);
/*     */     
/* 489 */     addSurfaceBiome(paramConsumer, Climate.Parameter.span(this.temperatures[1], this.temperatures[2]), this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, Biomes.SWAMP);
/* 490 */     addSurfaceBiome(paramConsumer, Climate.Parameter.span(this.temperatures[3], this.temperatures[4]), this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, Biomes.MANGROVE_SWAMP);
/* 491 */     addSurfaceBiome(paramConsumer, this.FROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], paramParameter, 0.0F, Biomes.FROZEN_RIVER);
/*     */     
/* 493 */     for (byte b = 0; b < this.temperatures.length; b++) {
/* 494 */       Climate.Parameter parameter = this.temperatures[b];
/* 495 */       for (byte b1 = 0; b1 < this.humidities.length; b1++) {
/* 496 */         Climate.Parameter parameter1 = this.humidities[b1];
/* 497 */         ResourceKey<Biome> resourceKey = pickMiddleBiomeOrBadlandsIfHot(b, b1, paramParameter);
/*     */         
/* 499 */         addSurfaceBiome(paramConsumer, parameter, parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), paramParameter, 0.0F, resourceKey);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer) {
/* 506 */     addUndergroundBiome(paramConsumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES);
/*     */     
/* 508 */     addUndergroundBiome(paramConsumer, this.FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.LUSH_CAVES);
/*     */     
/* 510 */     addBottomBiome(paramConsumer, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.erosions[0], this.erosions[1]), this.FULL_RANGE, 0.0F, Biomes.DEEP_DARK);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickMiddleBiome(int paramInt1, int paramInt2, Climate.Parameter paramParameter) {
/* 514 */     if (paramParameter.max() < 0L) {
/* 515 */       return this.MIDDLE_BIOMES[paramInt1][paramInt2];
/*     */     }
/* 517 */     ResourceKey<Biome> resourceKey = this.MIDDLE_BIOMES_VARIANT[paramInt1][paramInt2];
/* 518 */     return (resourceKey == null) ? this.MIDDLE_BIOMES[paramInt1][paramInt2] : resourceKey;
/*     */   }
/*     */ 
/*     */   
/*     */   private ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHot(int paramInt1, int paramInt2, Climate.Parameter paramParameter) {
/* 523 */     return (paramInt1 == 4) ? pickBadlandsBiome(paramInt2, paramParameter) : pickMiddleBiome(paramInt1, paramInt2, paramParameter);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(int paramInt1, int paramInt2, Climate.Parameter paramParameter) {
/* 527 */     return (paramInt1 == 0) ? pickSlopeBiome(paramInt1, paramInt2, paramParameter) : pickMiddleBiomeOrBadlandsIfHot(paramInt1, paramInt2, paramParameter);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> maybePickWindsweptSavannaBiome(int paramInt1, int paramInt2, Climate.Parameter paramParameter, ResourceKey<Biome> paramResourceKey) {
/* 531 */     if (paramInt1 > 1 && paramInt2 < 4 && paramParameter.max() >= 0L) {
/* 532 */       return Biomes.WINDSWEPT_SAVANNA;
/*     */     }
/* 534 */     return paramResourceKey;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickShatteredCoastBiome(int paramInt1, int paramInt2, Climate.Parameter paramParameter) {
/* 538 */     ResourceKey<Biome> resourceKey = (paramParameter.max() >= 0L) ? pickMiddleBiome(paramInt1, paramInt2, paramParameter) : pickBeachBiome(paramInt1, paramInt2);
/* 539 */     return maybePickWindsweptSavannaBiome(paramInt1, paramInt2, paramParameter, resourceKey);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickBeachBiome(int paramInt1, int paramInt2) {
/* 543 */     if (paramInt1 == 0) {
/* 544 */       return Biomes.SNOWY_BEACH;
/*     */     }
/* 546 */     if (paramInt1 == 4)
/*     */     {
/* 548 */       return Biomes.DESERT;
/*     */     }
/* 550 */     return Biomes.BEACH;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickBadlandsBiome(int paramInt, Climate.Parameter paramParameter) {
/* 554 */     if (paramInt < 2)
/* 555 */       return (paramParameter.max() < 0L) ? Biomes.BADLANDS : Biomes.ERODED_BADLANDS; 
/* 556 */     if (paramInt < 3) {
/* 557 */       return Biomes.BADLANDS;
/*     */     }
/* 559 */     return Biomes.WOODED_BADLANDS;
/*     */   }
/*     */ 
/*     */   
/*     */   private ResourceKey<Biome> pickPlateauBiome(int paramInt1, int paramInt2, Climate.Parameter paramParameter) {
/* 564 */     if (paramParameter.max() >= 0L) {
/* 565 */       ResourceKey<Biome> resourceKey = this.PLATEAU_BIOMES_VARIANT[paramInt1][paramInt2];
/* 566 */       if (resourceKey != null) {
/* 567 */         return resourceKey;
/*     */       }
/*     */     } 
/* 570 */     return this.PLATEAU_BIOMES[paramInt1][paramInt2];
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickPeakBiome(int paramInt1, int paramInt2, Climate.Parameter paramParameter) {
/* 574 */     if (paramInt1 <= 2)
/*     */     {
/* 576 */       return (paramParameter.max() < 0L) ? Biomes.JAGGED_PEAKS : Biomes.FROZEN_PEAKS;
/*     */     }
/* 578 */     if (paramInt1 == 3) {
/* 579 */       return Biomes.STONY_PEAKS;
/*     */     }
/* 581 */     return pickBadlandsBiome(paramInt2, paramParameter);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickSlopeBiome(int paramInt1, int paramInt2, Climate.Parameter paramParameter) {
/* 585 */     if (paramInt1 >= 3) {
/* 586 */       return pickPlateauBiome(paramInt1, paramInt2, paramParameter);
/*     */     }
/* 588 */     if (paramInt2 <= 1) {
/* 589 */       return Biomes.SNOWY_SLOPES;
/*     */     }
/* 591 */     return Biomes.GROVE;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickShatteredBiome(int paramInt1, int paramInt2, Climate.Parameter paramParameter) {
/* 595 */     ResourceKey<Biome> resourceKey = this.SHATTERED_BIOMES[paramInt1][paramInt2];
/* 596 */     return (resourceKey == null) ? pickMiddleBiome(paramInt1, paramInt2, paramParameter) : resourceKey;
/*     */   }
/*     */ 
/*     */   
/*     */   private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer, Climate.Parameter paramParameter1, Climate.Parameter paramParameter2, Climate.Parameter paramParameter3, Climate.Parameter paramParameter4, Climate.Parameter paramParameter5, float paramFloat, ResourceKey<Biome> paramResourceKey) {
/* 601 */     paramConsumer.accept(Pair.of(Climate.parameters(paramParameter1, paramParameter2, paramParameter3, paramParameter4, Climate.Parameter.point(0.0F), paramParameter5, paramFloat), paramResourceKey));
/*     */     
/* 603 */     paramConsumer.accept(Pair.of(Climate.parameters(paramParameter1, paramParameter2, paramParameter3, paramParameter4, Climate.Parameter.point(1.0F), paramParameter5, paramFloat), paramResourceKey));
/*     */   }
/*     */   
/*     */   private void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer, Climate.Parameter paramParameter1, Climate.Parameter paramParameter2, Climate.Parameter paramParameter3, Climate.Parameter paramParameter4, Climate.Parameter paramParameter5, float paramFloat, ResourceKey<Biome> paramResourceKey) {
/* 607 */     paramConsumer.accept(Pair.of(Climate.parameters(paramParameter1, paramParameter2, paramParameter3, paramParameter4, Climate.Parameter.span(0.2F, 0.9F), paramParameter5, paramFloat), paramResourceKey));
/*     */   }
/*     */   
/*     */   private void addBottomBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> paramConsumer, Climate.Parameter paramParameter1, Climate.Parameter paramParameter2, Climate.Parameter paramParameter3, Climate.Parameter paramParameter4, Climate.Parameter paramParameter5, float paramFloat, ResourceKey<Biome> paramResourceKey) {
/* 611 */     paramConsumer.accept(Pair.of(Climate.parameters(paramParameter1, paramParameter2, paramParameter3, paramParameter4, Climate.Parameter.point(1.1F), paramParameter5, paramFloat), paramResourceKey));
/*     */   }
/*     */   
/*     */   public static boolean isDeepDarkRegion(DensityFunction paramDensityFunction1, DensityFunction paramDensityFunction2, DensityFunction.FunctionContext paramFunctionContext) {
/* 615 */     return (paramDensityFunction1.compute(paramFunctionContext) < -0.22499999403953552D && paramDensityFunction2.compute(paramFunctionContext) > 0.8999999761581421D);
/*     */   }
/*     */   
/*     */   public static String getDebugStringForPeaksAndValleys(double paramDouble) {
/* 619 */     if (paramDouble < NoiseRouterData.peaksAndValleys(0.05F))
/* 620 */       return "Valley"; 
/* 621 */     if (paramDouble < NoiseRouterData.peaksAndValleys(0.26666668F))
/* 622 */       return "Low"; 
/* 623 */     if (paramDouble < NoiseRouterData.peaksAndValleys(0.4F))
/* 624 */       return "Mid"; 
/* 625 */     if (paramDouble < NoiseRouterData.peaksAndValleys(0.56666666F)) {
/* 626 */       return "High";
/*     */     }
/* 628 */     return "Peak";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDebugStringForContinentalness(double paramDouble) {
/* 633 */     double d = Climate.quantizeCoord((float)paramDouble);
/* 634 */     if (d < this.mushroomFieldsContinentalness.max())
/* 635 */       return "Mushroom fields"; 
/* 636 */     if (d < this.deepOceanContinentalness.max())
/* 637 */       return "Deep ocean"; 
/* 638 */     if (d < this.oceanContinentalness.max())
/* 639 */       return "Ocean"; 
/* 640 */     if (d < this.coastContinentalness.max())
/* 641 */       return "Coast"; 
/* 642 */     if (d < this.nearInlandContinentalness.max())
/* 643 */       return "Near inland"; 
/* 644 */     if (d < this.midInlandContinentalness.max()) {
/* 645 */       return "Mid inland";
/*     */     }
/* 647 */     return "Far inland";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDebugStringForErosion(double paramDouble) {
/* 652 */     return getDebugStringForNoiseValue(paramDouble, this.erosions);
/*     */   }
/*     */   
/*     */   public String getDebugStringForTemperature(double paramDouble) {
/* 656 */     return getDebugStringForNoiseValue(paramDouble, this.temperatures);
/*     */   }
/*     */   
/*     */   public String getDebugStringForHumidity(double paramDouble) {
/* 660 */     return getDebugStringForNoiseValue(paramDouble, this.humidities);
/*     */   }
/*     */   
/*     */   private static String getDebugStringForNoiseValue(double paramDouble, Climate.Parameter[] paramArrayOfParameter) {
/* 664 */     double d = Climate.quantizeCoord((float)paramDouble);
/* 665 */     for (byte b = 0; b < paramArrayOfParameter.length; b++) {
/* 666 */       if (d < paramArrayOfParameter[b].max()) {
/* 667 */         return "" + b;
/*     */       }
/*     */     } 
/* 670 */     return "?";
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getTemperatureThresholds() {
/* 675 */     return this.temperatures;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getHumidityThresholds() {
/* 680 */     return this.humidities;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getErosionThresholds() {
/* 685 */     return this.erosions;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getContinentalnessThresholds() {
/* 690 */     return new Climate.Parameter[] { this.mushroomFieldsContinentalness, this.deepOceanContinentalness, this.oceanContinentalness, this.coastContinentalness, this.nearInlandContinentalness, this.midInlandContinentalness, this.farInlandContinentalness };
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
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getPeaksAndValleysThresholds() {
/* 703 */     return new Climate.Parameter[] {
/* 704 */         Climate.Parameter.span(-2.0F, NoiseRouterData.peaksAndValleys(0.05F)), 
/* 705 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.05F), NoiseRouterData.peaksAndValleys(0.26666668F)), 
/* 706 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.26666668F), NoiseRouterData.peaksAndValleys(0.4F)), 
/* 707 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.4F), NoiseRouterData.peaksAndValleys(0.56666666F)), 
/* 708 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.56666666F), 2.0F)
/*     */       };
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getWeirdnessThresholds() {
/* 714 */     return new Climate.Parameter[] {
/* 715 */         Climate.Parameter.span(-2.0F, 0.0F), 
/* 716 */         Climate.Parameter.span(0.0F, 2.0F)
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\OverworldBiomeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */