/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.TerrainProvider;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.synth.BlendedNoise;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
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
/*     */ public class NoiseRouterData
/*     */ {
/*     */   public static final float GLOBAL_OFFSET = -0.50375F;
/*     */   private static final float ORE_THICKNESS = 0.08F;
/*     */   private static final double VEININESS_FREQUENCY = 1.5D;
/*     */   private static final double NOODLE_SPACING_AND_STRAIGHTNESS = 1.5D;
/*     */   private static final double SURFACE_DENSITY_THRESHOLD = 1.5625D;
/*     */   private static final double CHEESE_NOISE_TARGET = -0.703125D;
/*     */   public static final double NOISE_ZERO = 0.390625D;
/*     */   public static final int ISLAND_CHUNK_DISTANCE = 64;
/*     */   public static final long ISLAND_CHUNK_DISTANCE_SQR = 4096L;
/*     */   private static final int DENSITY_Y_ANCHOR_BOTTOM = -64;
/*     */   private static final int DENSITY_Y_ANCHOR_TOP = 320;
/*     */   private static final double DENSITY_Y_BOTTOM = 1.5D;
/*     */   private static final double DENSITY_Y_TOP = -1.5D;
/*     */   private static final int OVERWORLD_BOTTOM_SLIDE_HEIGHT = 24;
/*     */   private static final double BASE_DENSITY_MULTIPLIER = 4.0D;
/*  93 */   private static final DensityFunction BLENDING_FACTOR = DensityFunctions.constant(10.0D);
/*  94 */   private static final DensityFunction BLENDING_JAGGEDNESS = DensityFunctions.zero();
/*     */   
/*  96 */   private static final ResourceKey<DensityFunction> ZERO = createKey("zero");
/*  97 */   private static final ResourceKey<DensityFunction> Y = createKey("y");
/*  98 */   private static final ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");
/*  99 */   private static final ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");
/*     */   
/* 101 */   private static final ResourceKey<DensityFunction> BASE_3D_NOISE_OVERWORLD = createKey("overworld/base_3d_noise");
/* 102 */   private static final ResourceKey<DensityFunction> BASE_3D_NOISE_NETHER = createKey("nether/base_3d_noise");
/* 103 */   private static final ResourceKey<DensityFunction> BASE_3D_NOISE_END = createKey("end/base_3d_noise");
/*     */   
/* 105 */   public static final ResourceKey<DensityFunction> CONTINENTS = createKey("overworld/continents");
/* 106 */   public static final ResourceKey<DensityFunction> EROSION = createKey("overworld/erosion");
/* 107 */   public static final ResourceKey<DensityFunction> RIDGES = createKey("overworld/ridges");
/* 108 */   public static final ResourceKey<DensityFunction> RIDGES_FOLDED = createKey("overworld/ridges_folded");
/*     */   
/* 110 */   public static final ResourceKey<DensityFunction> OFFSET = createKey("overworld/offset");
/* 111 */   public static final ResourceKey<DensityFunction> FACTOR = createKey("overworld/factor");
/* 112 */   public static final ResourceKey<DensityFunction> JAGGEDNESS = createKey("overworld/jaggedness");
/* 113 */   public static final ResourceKey<DensityFunction> DEPTH = createKey("overworld/depth");
/* 114 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("overworld/sloped_cheese");
/*     */   
/* 116 */   public static final ResourceKey<DensityFunction> CONTINENTS_LARGE = createKey("overworld_large_biomes/continents");
/* 117 */   public static final ResourceKey<DensityFunction> EROSION_LARGE = createKey("overworld_large_biomes/erosion");
/*     */   
/* 119 */   private static final ResourceKey<DensityFunction> OFFSET_LARGE = createKey("overworld_large_biomes/offset");
/* 120 */   private static final ResourceKey<DensityFunction> FACTOR_LARGE = createKey("overworld_large_biomes/factor");
/* 121 */   private static final ResourceKey<DensityFunction> JAGGEDNESS_LARGE = createKey("overworld_large_biomes/jaggedness");
/* 122 */   private static final ResourceKey<DensityFunction> DEPTH_LARGE = createKey("overworld_large_biomes/depth");
/* 123 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE_LARGE = createKey("overworld_large_biomes/sloped_cheese");
/*     */   
/* 125 */   private static final ResourceKey<DensityFunction> OFFSET_AMPLIFIED = createKey("overworld_amplified/offset");
/* 126 */   private static final ResourceKey<DensityFunction> FACTOR_AMPLIFIED = createKey("overworld_amplified/factor");
/* 127 */   private static final ResourceKey<DensityFunction> JAGGEDNESS_AMPLIFIED = createKey("overworld_amplified/jaggedness");
/* 128 */   private static final ResourceKey<DensityFunction> DEPTH_AMPLIFIED = createKey("overworld_amplified/depth");
/* 129 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE_AMPLIFIED = createKey("overworld_amplified/sloped_cheese");
/*     */   
/* 131 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE_END = createKey("end/sloped_cheese");
/*     */   
/* 133 */   private static final ResourceKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = createKey("overworld/caves/spaghetti_roughness_function");
/* 134 */   private static final ResourceKey<DensityFunction> ENTRANCES = createKey("overworld/caves/entrances");
/* 135 */   private static final ResourceKey<DensityFunction> NOODLE = createKey("overworld/caves/noodle");
/* 136 */   private static final ResourceKey<DensityFunction> PILLARS = createKey("overworld/caves/pillars");
/* 137 */   private static final ResourceKey<DensityFunction> SPAGHETTI_2D_THICKNESS_MODULATOR = createKey("overworld/caves/spaghetti_2d_thickness_modulator");
/* 138 */   private static final ResourceKey<DensityFunction> SPAGHETTI_2D = createKey("overworld/caves/spaghetti_2d");
/*     */   
/*     */   private static ResourceKey<DensityFunction> createKey(String paramString) {
/* 141 */     return ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */   
/*     */   public static Holder<? extends DensityFunction> bootstrap(BootstrapContext<DensityFunction> paramBootstrapContext) {
/* 145 */     HolderGetter<NormalNoise.NoiseParameters> holderGetter = paramBootstrapContext.lookup(Registries.NOISE);
/* 146 */     HolderGetter<DensityFunction> holderGetter1 = paramBootstrapContext.lookup(Registries.DENSITY_FUNCTION);
/*     */     
/* 148 */     paramBootstrapContext.register(ZERO, DensityFunctions.zero());
/*     */     
/* 150 */     int i = DimensionType.MIN_Y * 2;
/* 151 */     int j = DimensionType.MAX_Y * 2;
/* 152 */     paramBootstrapContext.register(Y, DensityFunctions.yClampedGradient(i, j, i, j));
/*     */     
/* 154 */     DensityFunction densityFunction1 = registerAndWrap(paramBootstrapContext, SHIFT_X, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA((Holder<NormalNoise.NoiseParameters>)holderGetter.getOrThrow(Noises.SHIFT)))));
/* 155 */     DensityFunction densityFunction2 = registerAndWrap(paramBootstrapContext, SHIFT_Z, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB((Holder<NormalNoise.NoiseParameters>)holderGetter.getOrThrow(Noises.SHIFT)))));
/*     */     
/* 157 */     paramBootstrapContext.register(BASE_3D_NOISE_OVERWORLD, BlendedNoise.createUnseeded(0.25D, 0.125D, 80.0D, 160.0D, 8.0D));
/* 158 */     paramBootstrapContext.register(BASE_3D_NOISE_NETHER, BlendedNoise.createUnseeded(0.25D, 0.375D, 80.0D, 60.0D, 8.0D));
/* 159 */     paramBootstrapContext.register(BASE_3D_NOISE_END, BlendedNoise.createUnseeded(0.25D, 0.25D, 80.0D, 160.0D, 4.0D));
/*     */     
/* 161 */     Holder.Reference reference1 = paramBootstrapContext.register(CONTINENTS, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(densityFunction1, densityFunction2, 0.25D, (Holder<NormalNoise.NoiseParameters>)holderGetter.getOrThrow(Noises.CONTINENTALNESS))));
/* 162 */     Holder.Reference reference2 = paramBootstrapContext.register(EROSION, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(densityFunction1, densityFunction2, 0.25D, (Holder<NormalNoise.NoiseParameters>)holderGetter.getOrThrow(Noises.EROSION))));
/* 163 */     DensityFunction densityFunction3 = registerAndWrap(paramBootstrapContext, RIDGES, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(densityFunction1, densityFunction2, 0.25D, (Holder<NormalNoise.NoiseParameters>)holderGetter.getOrThrow(Noises.RIDGE))));
/* 164 */     paramBootstrapContext.register(RIDGES_FOLDED, peaksAndValleys(densityFunction3));
/*     */     
/* 166 */     DensityFunction densityFunction4 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)holderGetter.getOrThrow(Noises.JAGGED), 1500.0D, 0.0D);
/*     */     
/* 168 */     registerTerrainNoises(paramBootstrapContext, holderGetter1, densityFunction4, (Holder<DensityFunction>)reference1, (Holder<DensityFunction>)reference2, OFFSET, FACTOR, JAGGEDNESS, DEPTH, SLOPED_CHEESE, false);
/*     */     
/* 170 */     Holder.Reference reference3 = paramBootstrapContext.register(CONTINENTS_LARGE, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(densityFunction1, densityFunction2, 0.25D, (Holder<NormalNoise.NoiseParameters>)holderGetter.getOrThrow(Noises.CONTINENTALNESS_LARGE))));
/* 171 */     Holder.Reference reference4 = paramBootstrapContext.register(EROSION_LARGE, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(densityFunction1, densityFunction2, 0.25D, (Holder<NormalNoise.NoiseParameters>)holderGetter.getOrThrow(Noises.EROSION_LARGE))));
/*     */     
/* 173 */     registerTerrainNoises(paramBootstrapContext, holderGetter1, densityFunction4, (Holder<DensityFunction>)reference3, (Holder<DensityFunction>)reference4, OFFSET_LARGE, FACTOR_LARGE, JAGGEDNESS_LARGE, DEPTH_LARGE, SLOPED_CHEESE_LARGE, false);
/* 174 */     registerTerrainNoises(paramBootstrapContext, holderGetter1, densityFunction4, (Holder<DensityFunction>)reference1, (Holder<DensityFunction>)reference2, OFFSET_AMPLIFIED, FACTOR_AMPLIFIED, JAGGEDNESS_AMPLIFIED, DEPTH_AMPLIFIED, SLOPED_CHEESE_AMPLIFIED, true);
/*     */     
/* 176 */     paramBootstrapContext.register(SLOPED_CHEESE_END, DensityFunctions.add(DensityFunctions.endIslands(0L), getFunction(holderGetter1, BASE_3D_NOISE_END)));
/*     */     
/* 178 */     paramBootstrapContext.register(SPAGHETTI_ROUGHNESS_FUNCTION, spaghettiRoughnessFunction(holderGetter));
/* 179 */     paramBootstrapContext.register(SPAGHETTI_2D_THICKNESS_MODULATOR, DensityFunctions.cacheOnce(DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)holderGetter.getOrThrow(Noises.SPAGHETTI_2D_THICKNESS), 2.0D, 1.0D, -0.6D, -1.3D)));
/* 180 */     paramBootstrapContext.register(SPAGHETTI_2D, spaghetti2D(holderGetter1, holderGetter));
/* 181 */     paramBootstrapContext.register(ENTRANCES, entrances(holderGetter1, holderGetter));
/* 182 */     paramBootstrapContext.register(NOODLE, noodle(holderGetter1, holderGetter));
/* 183 */     return (Holder<? extends DensityFunction>)paramBootstrapContext.register(PILLARS, pillars(holderGetter));
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
/*     */   private static void registerTerrainNoises(BootstrapContext<DensityFunction> paramBootstrapContext, HolderGetter<DensityFunction> paramHolderGetter, DensityFunction paramDensityFunction, Holder<DensityFunction> paramHolder1, Holder<DensityFunction> paramHolder2, ResourceKey<DensityFunction> paramResourceKey1, ResourceKey<DensityFunction> paramResourceKey2, ResourceKey<DensityFunction> paramResourceKey3, ResourceKey<DensityFunction> paramResourceKey4, ResourceKey<DensityFunction> paramResourceKey5, boolean paramBoolean) {
/* 199 */     DensityFunctions.Spline.Coordinate coordinate1 = new DensityFunctions.Spline.Coordinate(paramHolder1);
/* 200 */     DensityFunctions.Spline.Coordinate coordinate2 = new DensityFunctions.Spline.Coordinate(paramHolder2);
/* 201 */     DensityFunctions.Spline.Coordinate coordinate3 = new DensityFunctions.Spline.Coordinate((Holder<DensityFunction>)paramHolderGetter.getOrThrow(RIDGES));
/* 202 */     DensityFunctions.Spline.Coordinate coordinate4 = new DensityFunctions.Spline.Coordinate((Holder<DensityFunction>)paramHolderGetter.getOrThrow(RIDGES_FOLDED));
/*     */ 
/*     */     
/* 205 */     DensityFunction densityFunction1 = registerAndWrap(paramBootstrapContext, paramResourceKey1, splineWithBlending(
/* 206 */           DensityFunctions.add(DensityFunctions.constant(-0.5037500262260437D), DensityFunctions.spline(TerrainProvider.overworldOffset(coordinate1, coordinate2, coordinate4, paramBoolean))), 
/* 207 */           DensityFunctions.blendOffset()));
/*     */     
/* 209 */     DensityFunction densityFunction2 = registerAndWrap(paramBootstrapContext, paramResourceKey2, splineWithBlending(
/* 210 */           DensityFunctions.spline(TerrainProvider.overworldFactor(coordinate1, coordinate2, coordinate3, coordinate4, paramBoolean)), BLENDING_FACTOR));
/*     */ 
/*     */     
/* 213 */     DensityFunction densityFunction3 = registerAndWrap(paramBootstrapContext, paramResourceKey4, offsetToDepth(densityFunction1));
/* 214 */     DensityFunction densityFunction4 = registerAndWrap(paramBootstrapContext, paramResourceKey3, splineWithBlending(
/* 215 */           DensityFunctions.spline(TerrainProvider.overworldJaggedness(coordinate1, coordinate2, coordinate3, coordinate4, paramBoolean)), BLENDING_JAGGEDNESS));
/*     */ 
/*     */ 
/*     */     
/* 219 */     DensityFunction densityFunction5 = DensityFunctions.mul(densityFunction4, paramDensityFunction.halfNegative());
/* 220 */     DensityFunction densityFunction6 = noiseGradientDensity(densityFunction2, DensityFunctions.add(densityFunction3, densityFunction5));
/* 221 */     paramBootstrapContext.register(paramResourceKey5, DensityFunctions.add(densityFunction6, getFunction(paramHolderGetter, BASE_3D_NOISE_OVERWORLD)));
/*     */   }
/*     */   
/*     */   private static DensityFunction offsetToDepth(DensityFunction paramDensityFunction) {
/* 225 */     return DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5D, -1.5D), paramDensityFunction);
/*     */   }
/*     */   
/*     */   private static DensityFunction registerAndWrap(BootstrapContext<DensityFunction> paramBootstrapContext, ResourceKey<DensityFunction> paramResourceKey, DensityFunction paramDensityFunction) {
/* 229 */     return new DensityFunctions.HolderHolder((Holder<DensityFunction>)paramBootstrapContext.register(paramResourceKey, paramDensityFunction));
/*     */   }
/*     */   
/*     */   private static DensityFunction getFunction(HolderGetter<DensityFunction> paramHolderGetter, ResourceKey<DensityFunction> paramResourceKey) {
/* 233 */     return new DensityFunctions.HolderHolder((Holder<DensityFunction>)paramHolderGetter.getOrThrow(paramResourceKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DensityFunction peaksAndValleys(DensityFunction paramDensityFunction) {
/* 243 */     return DensityFunctions.mul(DensityFunctions.add(DensityFunctions.add(paramDensityFunction.abs(), DensityFunctions.constant(-0.6666666666666666D)).abs(), DensityFunctions.constant(-0.3333333333333333D)), DensityFunctions.constant(-3.0D));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float peaksAndValleys(float paramFloat) {
/* 250 */     return -(Math.abs(Math.abs(paramFloat) - 0.6666667F) - 0.33333334F) * 3.0F;
/*     */   }
/*     */   
/*     */   private static DensityFunction spaghettiRoughnessFunction(HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter) {
/* 254 */     DensityFunction densityFunction1 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter.getOrThrow(Noises.SPAGHETTI_ROUGHNESS));
/*     */     
/* 256 */     DensityFunction densityFunction2 = DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter.getOrThrow(Noises.SPAGHETTI_ROUGHNESS_MODULATOR), 0.0D, -0.1D);
/*     */     
/* 258 */     return DensityFunctions.cacheOnce(DensityFunctions.mul(densityFunction2, 
/*     */           
/* 260 */           DensityFunctions.add(densityFunction1.abs(), DensityFunctions.constant(-0.4D))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction entrances(HolderGetter<DensityFunction> paramHolderGetter, HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter1) {
/* 265 */     DensityFunction densityFunction1 = DensityFunctions.cacheOnce(DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.SPAGHETTI_3D_RARITY), 2.0D, 1.0D));
/*     */ 
/*     */     
/* 268 */     DensityFunction densityFunction2 = DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.SPAGHETTI_3D_THICKNESS), -0.065D, -0.088D);
/*     */     
/* 270 */     DensityFunction densityFunction3 = DensityFunctions.weirdScaledSampler(densityFunction1, (Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.SPAGHETTI_3D_1), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE1);
/* 271 */     DensityFunction densityFunction4 = DensityFunctions.weirdScaledSampler(densityFunction1, (Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.SPAGHETTI_3D_2), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 277 */     DensityFunction densityFunction5 = DensityFunctions.add(DensityFunctions.max(densityFunction3, densityFunction4), densityFunction2).clamp(-1.0D, 1.0D);
/*     */     
/* 279 */     DensityFunction densityFunction6 = getFunction(paramHolderGetter, SPAGHETTI_ROUGHNESS_FUNCTION);
/*     */     
/* 281 */     DensityFunction densityFunction7 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.CAVE_ENTRANCE), 0.75D, 0.5D);
/*     */     
/* 283 */     DensityFunction densityFunction8 = DensityFunctions.add(
/* 284 */         DensityFunctions.add(densityFunction7, DensityFunctions.constant(0.37D)), 
/*     */ 
/*     */         
/* 287 */         DensityFunctions.yClampedGradient(-10, 30, 0.3D, 0.0D));
/*     */ 
/*     */ 
/*     */     
/* 291 */     return DensityFunctions.cacheOnce(DensityFunctions.min(densityFunction8, DensityFunctions.add(densityFunction6, densityFunction5)));
/*     */   }
/*     */   
/*     */   private static DensityFunction noodle(HolderGetter<DensityFunction> paramHolderGetter, HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter1) {
/* 295 */     DensityFunction densityFunction1 = getFunction(paramHolderGetter, Y);
/*     */     
/* 297 */     byte b1 = -64;
/*     */ 
/*     */     
/* 300 */     byte b2 = -60;
/* 301 */     char c = 'ŀ';
/*     */     
/* 303 */     DensityFunction densityFunction2 = yLimitedInterpolatable(densityFunction1, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.NOODLE), 1.0D, 1.0D), -60, 320, -1);
/*     */ 
/*     */ 
/*     */     
/* 307 */     DensityFunction densityFunction3 = yLimitedInterpolatable(densityFunction1, DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.NOODLE_THICKNESS), 1.0D, 1.0D, -0.05D, -0.1D), -60, 320, 0);
/*     */     
/* 309 */     double d = 2.6666666666666665D;
/* 310 */     DensityFunction densityFunction4 = yLimitedInterpolatable(densityFunction1, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.NOODLE_RIDGE_A), 2.6666666666666665D, 2.6666666666666665D), -60, 320, 0);
/* 311 */     DensityFunction densityFunction5 = yLimitedInterpolatable(densityFunction1, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.NOODLE_RIDGE_B), 2.6666666666666665D, 2.6666666666666665D), -60, 320, 0);
/*     */     
/* 313 */     DensityFunction densityFunction6 = DensityFunctions.mul(
/* 314 */         DensityFunctions.constant(1.5D), 
/* 315 */         DensityFunctions.max(densityFunction4.abs(), densityFunction5.abs()));
/*     */ 
/*     */     
/* 318 */     return DensityFunctions.rangeChoice(densityFunction2, -1000000.0D, 0.0D, 
/*     */ 
/*     */ 
/*     */         
/* 322 */         DensityFunctions.constant(64.0D), 
/* 323 */         DensityFunctions.add(densityFunction3, densityFunction6));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DensityFunction pillars(HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter) {
/* 331 */     double d1 = 25.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 336 */     double d2 = 0.3D;
/*     */     
/* 338 */     DensityFunction densityFunction1 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter.getOrThrow(Noises.PILLAR), 25.0D, 0.3D);
/*     */ 
/*     */ 
/*     */     
/* 342 */     DensityFunction densityFunction2 = DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter.getOrThrow(Noises.PILLAR_RARENESS), 0.0D, -2.0D);
/*     */ 
/*     */ 
/*     */     
/* 346 */     DensityFunction densityFunction3 = DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter.getOrThrow(Noises.PILLAR_THICKNESS), 0.0D, 1.1D);
/*     */     
/* 348 */     DensityFunction densityFunction4 = DensityFunctions.add(
/* 349 */         DensityFunctions.mul(densityFunction1, DensityFunctions.constant(2.0D)), densityFunction2);
/*     */ 
/*     */ 
/*     */     
/* 353 */     return DensityFunctions.cacheOnce(DensityFunctions.mul(densityFunction4, densityFunction3
/*     */ 
/*     */           
/* 356 */           .cube()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction spaghetti2D(HolderGetter<DensityFunction> paramHolderGetter, HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter1) {
/* 361 */     DensityFunction densityFunction1 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.SPAGHETTI_2D_MODULATOR), 2.0D, 1.0D);
/* 362 */     DensityFunction densityFunction2 = DensityFunctions.weirdScaledSampler(densityFunction1, (Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.SPAGHETTI_2D), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE2);
/*     */     
/* 364 */     DensityFunction densityFunction3 = DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.SPAGHETTI_2D_ELEVATION), 0.0D, Math.floorDiv(-64, 8), 8.0D);
/*     */     
/* 366 */     DensityFunction densityFunction4 = getFunction(paramHolderGetter, SPAGHETTI_2D_THICKNESS_MODULATOR);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     DensityFunction densityFunction5 = DensityFunctions.add(densityFunction3, DensityFunctions.yClampedGradient(-64, 320, 8.0D, -40.0D)).abs();
/*     */     
/* 373 */     DensityFunction densityFunction6 = DensityFunctions.add(densityFunction5, densityFunction4).cube();
/*     */     
/* 375 */     double d = 0.083D;
/* 376 */     DensityFunction densityFunction7 = DensityFunctions.add(densityFunction2, DensityFunctions.mul(DensityFunctions.constant(0.083D), densityFunction4));
/*     */ 
/*     */     
/* 379 */     return DensityFunctions.max(densityFunction7, densityFunction6).clamp(-1.0D, 1.0D);
/*     */   }
/*     */   
/*     */   private static DensityFunction underground(HolderGetter<DensityFunction> paramHolderGetter, HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter1, DensityFunction paramDensityFunction) {
/* 383 */     DensityFunction densityFunction1 = getFunction(paramHolderGetter, SPAGHETTI_2D);
/*     */     
/* 385 */     DensityFunction densityFunction2 = getFunction(paramHolderGetter, SPAGHETTI_ROUGHNESS_FUNCTION);
/*     */     
/* 387 */     DensityFunction densityFunction3 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.CAVE_LAYER), 8.0D);
/*     */     
/* 389 */     DensityFunction densityFunction4 = DensityFunctions.mul(DensityFunctions.constant(4.0D), densityFunction3.square());
/*     */     
/* 391 */     DensityFunction densityFunction5 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.CAVE_CHEESE), 0.6666666666666666D);
/*     */     
/* 393 */     DensityFunction densityFunction6 = DensityFunctions.add(
/*     */         
/* 395 */         DensityFunctions.add(DensityFunctions.constant(0.27D), densityFunction5).clamp(-1.0D, 1.0D), 
/*     */         
/* 397 */         DensityFunctions.add(DensityFunctions.constant(1.5D), DensityFunctions.mul(DensityFunctions.constant(-0.64D), paramDensityFunction)).clamp(0.0D, 0.5D));
/*     */ 
/*     */     
/* 400 */     DensityFunction densityFunction7 = DensityFunctions.add(densityFunction4, densityFunction6);
/*     */     
/* 402 */     DensityFunction densityFunction8 = DensityFunctions.min(DensityFunctions.min(densityFunction7, getFunction(paramHolderGetter, ENTRANCES)), DensityFunctions.add(densityFunction1, densityFunction2));
/*     */     
/* 404 */     DensityFunction densityFunction9 = getFunction(paramHolderGetter, PILLARS);
/*     */     
/* 406 */     DensityFunction densityFunction10 = DensityFunctions.rangeChoice(densityFunction9, -1000000.0D, 0.03D, DensityFunctions.constant(-1000000.0D), densityFunction9);
/*     */     
/* 408 */     return DensityFunctions.max(densityFunction8, densityFunction10);
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction postProcess(DensityFunction paramDensityFunction) {
/* 413 */     DensityFunction densityFunction = DensityFunctions.blendDensity(paramDensityFunction);
/* 414 */     return DensityFunctions.mul(DensityFunctions.interpolated(densityFunction), DensityFunctions.constant(0.64D)).squeeze();
/*     */   }
/*     */   
/*     */   private static DensityFunction remap(DensityFunction paramDensityFunction, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 418 */     double d1 = (paramDouble4 - paramDouble3) / (paramDouble2 - paramDouble1);
/* 419 */     double d2 = paramDouble3 - paramDouble1 * d1;
/* 420 */     return DensityFunctions.add(DensityFunctions.mul(paramDensityFunction, DensityFunctions.constant(d1)), DensityFunctions.constant(d2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static NoiseRouter overworld(HolderGetter<DensityFunction> paramHolderGetter, HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter1, boolean paramBoolean1, boolean paramBoolean2) {
/* 425 */     DensityFunction densityFunction1 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.AQUIFER_BARRIER), 0.5D);
/* 426 */     DensityFunction densityFunction2 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67D);
/* 427 */     DensityFunction densityFunction3 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143D);
/* 428 */     DensityFunction densityFunction4 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.AQUIFER_LAVA));
/*     */     
/* 430 */     DensityFunction densityFunction5 = getFunction(paramHolderGetter, SHIFT_X);
/* 431 */     DensityFunction densityFunction6 = getFunction(paramHolderGetter, SHIFT_Z);
/*     */ 
/*     */     
/* 434 */     DensityFunction densityFunction7 = DensityFunctions.shiftedNoise2d(densityFunction5, densityFunction6, 0.25D, (Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(paramBoolean1 ? Noises.TEMPERATURE_LARGE : Noises.TEMPERATURE));
/* 435 */     DensityFunction densityFunction8 = DensityFunctions.shiftedNoise2d(densityFunction5, densityFunction6, 0.25D, (Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(paramBoolean1 ? Noises.VEGETATION_LARGE : Noises.VEGETATION));
/*     */     
/* 437 */     DensityFunction densityFunction9 = getFunction(paramHolderGetter, paramBoolean1 ? OFFSET_LARGE : (paramBoolean2 ? OFFSET_AMPLIFIED : OFFSET));
/* 438 */     DensityFunction densityFunction10 = getFunction(paramHolderGetter, paramBoolean1 ? FACTOR_LARGE : (paramBoolean2 ? FACTOR_AMPLIFIED : FACTOR));
/* 439 */     DensityFunction densityFunction11 = getFunction(paramHolderGetter, paramBoolean1 ? DEPTH_LARGE : (paramBoolean2 ? DEPTH_AMPLIFIED : DEPTH));
/*     */     
/* 441 */     DensityFunction densityFunction12 = preliminarySurfaceLevel(densityFunction9, densityFunction10, paramBoolean2);
/*     */     
/* 443 */     DensityFunction densityFunction13 = getFunction(paramHolderGetter, paramBoolean1 ? SLOPED_CHEESE_LARGE : (paramBoolean2 ? SLOPED_CHEESE_AMPLIFIED : SLOPED_CHEESE));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 448 */     DensityFunction densityFunction14 = DensityFunctions.min(densityFunction13, DensityFunctions.mul(DensityFunctions.constant(5.0D), getFunction(paramHolderGetter, ENTRANCES)));
/* 449 */     DensityFunction densityFunction15 = DensityFunctions.rangeChoice(densityFunction13, -1000000.0D, 1.5625D, densityFunction14, underground(paramHolderGetter, paramHolderGetter1, densityFunction13));
/*     */     
/* 451 */     DensityFunction densityFunction16 = DensityFunctions.min(postProcess(slideOverworld(paramBoolean2, densityFunction15)), getFunction(paramHolderGetter, NOODLE));
/*     */     
/* 453 */     DensityFunction densityFunction17 = getFunction(paramHolderGetter, Y);
/*     */     
/* 455 */     int i = Stream.<OreVeinifier.VeinType>of(OreVeinifier.VeinType.values()).mapToInt(paramVeinType -> paramVeinType.minY).min().orElse(-DimensionType.MIN_Y * 2);
/* 456 */     int j = Stream.<OreVeinifier.VeinType>of(OreVeinifier.VeinType.values()).mapToInt(paramVeinType -> paramVeinType.maxY).max().orElse(-DimensionType.MIN_Y * 2);
/*     */ 
/*     */     
/* 459 */     DensityFunction densityFunction18 = yLimitedInterpolatable(densityFunction17, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.ORE_VEININESS), 1.5D, 1.5D), i, j, 0);
/*     */     
/* 461 */     float f = 4.0F;
/*     */ 
/*     */     
/* 464 */     DensityFunction densityFunction19 = yLimitedInterpolatable(densityFunction17, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.ORE_VEIN_A), 4.0D, 4.0D), i, j, 0).abs();
/* 465 */     DensityFunction densityFunction20 = yLimitedInterpolatable(densityFunction17, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.ORE_VEIN_B), 4.0D, 4.0D), i, j, 0).abs();
/*     */     
/* 467 */     DensityFunction densityFunction21 = DensityFunctions.add(DensityFunctions.constant(-0.07999999821186066D), DensityFunctions.max(densityFunction19, densityFunction20));
/*     */     
/* 469 */     DensityFunction densityFunction22 = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.ORE_GAP));
/*     */ 
/*     */ 
/*     */     
/* 473 */     return new NoiseRouter(densityFunction1, densityFunction2, densityFunction3, densityFunction4, densityFunction7, densityFunction8, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 480 */         getFunction(paramHolderGetter, paramBoolean1 ? CONTINENTS_LARGE : CONTINENTS), 
/* 481 */         getFunction(paramHolderGetter, paramBoolean1 ? EROSION_LARGE : EROSION), densityFunction11, 
/*     */         
/* 483 */         getFunction(paramHolderGetter, RIDGES), densityFunction12, densityFunction16, densityFunction18, densityFunction21, densityFunction22);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static NoiseRouter noNewCaves(HolderGetter<DensityFunction> paramHolderGetter, HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter1, DensityFunction paramDensityFunction) {
/* 493 */     DensityFunction densityFunction1 = getFunction(paramHolderGetter, SHIFT_X);
/* 494 */     DensityFunction densityFunction2 = getFunction(paramHolderGetter, SHIFT_Z);
/*     */     
/* 496 */     DensityFunction densityFunction3 = DensityFunctions.shiftedNoise2d(densityFunction1, densityFunction2, 0.25D, (Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.TEMPERATURE));
/* 497 */     DensityFunction densityFunction4 = DensityFunctions.shiftedNoise2d(densityFunction1, densityFunction2, 0.25D, (Holder<NormalNoise.NoiseParameters>)paramHolderGetter1.getOrThrow(Noises.VEGETATION));
/*     */     
/* 499 */     DensityFunction densityFunction5 = postProcess(paramDensityFunction);
/*     */ 
/*     */ 
/*     */     
/* 503 */     return new NoiseRouter(
/* 504 */         DensityFunctions.zero(), 
/* 505 */         DensityFunctions.zero(), 
/* 506 */         DensityFunctions.zero(), 
/* 507 */         DensityFunctions.zero(), densityFunction3, densityFunction4, 
/*     */ 
/*     */         
/* 510 */         DensityFunctions.zero(), 
/* 511 */         DensityFunctions.zero(), 
/* 512 */         DensityFunctions.zero(), 
/* 513 */         DensityFunctions.zero(), 
/* 514 */         DensityFunctions.zero(), densityFunction5, 
/*     */         
/* 516 */         DensityFunctions.zero(), 
/* 517 */         DensityFunctions.zero(), 
/* 518 */         DensityFunctions.zero());
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction slideOverworld(boolean paramBoolean, DensityFunction paramDensityFunction) {
/* 523 */     return slide(paramDensityFunction, -64, 384, 
/*     */ 
/*     */ 
/*     */         
/* 527 */         paramBoolean ? 16 : 80, 
/* 528 */         paramBoolean ? 0 : 64, -0.078125D, 0, 24, 
/*     */ 
/*     */ 
/*     */         
/* 532 */         paramBoolean ? 0.4D : 0.1171875D);
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction slideNetherLike(HolderGetter<DensityFunction> paramHolderGetter, int paramInt1, int paramInt2) {
/* 537 */     return slide(
/* 538 */         getFunction(paramHolderGetter, BASE_3D_NOISE_NETHER), paramInt1, paramInt2, 24, 0, 0.9375D, -8, 24, 2.5D);
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
/*     */   private static DensityFunction slideEndLike(DensityFunction paramDensityFunction, int paramInt1, int paramInt2) {
/* 551 */     return slide(paramDensityFunction, paramInt1, paramInt2, 72, -184, -23.4375D, 4, 32, -0.234375D);
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
/*     */   protected static NoiseRouter nether(HolderGetter<DensityFunction> paramHolderGetter, HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter1) {
/* 565 */     return noNewCaves(paramHolderGetter, paramHolderGetter1, slideNetherLike(paramHolderGetter, 0, 128));
/*     */   }
/*     */   
/*     */   protected static NoiseRouter caves(HolderGetter<DensityFunction> paramHolderGetter, HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter1) {
/* 569 */     return noNewCaves(paramHolderGetter, paramHolderGetter1, slideNetherLike(paramHolderGetter, -64, 192));
/*     */   }
/*     */   
/*     */   protected static NoiseRouter floatingIslands(HolderGetter<DensityFunction> paramHolderGetter, HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter1) {
/* 573 */     return noNewCaves(paramHolderGetter, paramHolderGetter1, slideEndLike(getFunction(paramHolderGetter, BASE_3D_NOISE_END), 0, 256));
/*     */   }
/*     */   
/*     */   private static DensityFunction slideEnd(DensityFunction paramDensityFunction) {
/* 577 */     return slideEndLike(paramDensityFunction, 0, 128);
/*     */   }
/*     */   
/*     */   protected static NoiseRouter end(HolderGetter<DensityFunction> paramHolderGetter) {
/* 581 */     DensityFunction densityFunction1 = DensityFunctions.cache2d(DensityFunctions.endIslands(0L));
/* 582 */     DensityFunction densityFunction2 = postProcess(slideEnd(getFunction(paramHolderGetter, SLOPED_CHEESE_END)));
/*     */ 
/*     */     
/* 585 */     return new NoiseRouter(
/* 586 */         DensityFunctions.zero(), 
/* 587 */         DensityFunctions.zero(), 
/* 588 */         DensityFunctions.zero(), 
/* 589 */         DensityFunctions.zero(), 
/* 590 */         DensityFunctions.zero(), 
/* 591 */         DensityFunctions.zero(), 
/* 592 */         DensityFunctions.zero(), densityFunction1, 
/*     */         
/* 594 */         DensityFunctions.zero(), 
/* 595 */         DensityFunctions.zero(), 
/* 596 */         DensityFunctions.zero(), densityFunction2, 
/*     */         
/* 598 */         DensityFunctions.zero(), 
/* 599 */         DensityFunctions.zero(), 
/* 600 */         DensityFunctions.zero());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static NoiseRouter none() {
/* 606 */     return new NoiseRouter(
/* 607 */         DensityFunctions.zero(), 
/* 608 */         DensityFunctions.zero(), 
/* 609 */         DensityFunctions.zero(), 
/* 610 */         DensityFunctions.zero(), 
/* 611 */         DensityFunctions.zero(), 
/* 612 */         DensityFunctions.zero(), 
/* 613 */         DensityFunctions.zero(), 
/* 614 */         DensityFunctions.zero(), 
/* 615 */         DensityFunctions.zero(), 
/* 616 */         DensityFunctions.zero(), 
/* 617 */         DensityFunctions.zero(), 
/* 618 */         DensityFunctions.zero(), 
/* 619 */         DensityFunctions.zero(), 
/* 620 */         DensityFunctions.zero(), 
/* 621 */         DensityFunctions.zero());
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction splineWithBlending(DensityFunction paramDensityFunction1, DensityFunction paramDensityFunction2) {
/* 626 */     DensityFunction densityFunction = DensityFunctions.lerp(DensityFunctions.blendAlpha(), paramDensityFunction2, paramDensityFunction1);
/* 627 */     return DensityFunctions.flatCache(DensityFunctions.cache2d(densityFunction));
/*     */   }
/*     */   
/*     */   private static DensityFunction noiseGradientDensity(DensityFunction paramDensityFunction1, DensityFunction paramDensityFunction2) {
/* 631 */     DensityFunction densityFunction = DensityFunctions.mul(paramDensityFunction2, paramDensityFunction1);
/*     */ 
/*     */     
/* 634 */     return DensityFunctions.mul(DensityFunctions.constant(4.0D), densityFunction.quarterNegative());
/*     */   }
/*     */   
/*     */   private static DensityFunction preliminarySurfaceLevel(DensityFunction paramDensityFunction1, DensityFunction paramDensityFunction2, boolean paramBoolean) {
/* 638 */     DensityFunction densityFunction1 = DensityFunctions.cache2d(paramDensityFunction2);
/* 639 */     DensityFunction densityFunction2 = DensityFunctions.cache2d(paramDensityFunction1);
/* 640 */     DensityFunction densityFunction3 = remap(
/* 641 */         DensityFunctions.add(
/* 642 */           DensityFunctions.mul(
/* 643 */             DensityFunctions.constant(0.2734375D), densityFunction1
/* 644 */             .invert()), 
/*     */           
/* 646 */           DensityFunctions.mul(DensityFunctions.constant(-1.0D), densityFunction2)), 1.5D, -1.5D, -64.0D, 320.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 651 */     densityFunction3 = densityFunction3.clamp(-40.0D, 320.0D);
/* 652 */     DensityFunction densityFunction4 = DensityFunctions.add(
/* 653 */         slideOverworld(paramBoolean, 
/*     */           
/* 655 */           DensityFunctions.add(
/* 656 */             noiseGradientDensity(densityFunction1, offsetToDepth(densityFunction2)), 
/* 657 */             DensityFunctions.constant(-0.703125D))
/* 658 */           .clamp(-64.0D, 64.0D)), 
/* 659 */         DensityFunctions.constant(-0.390625D));
/*     */     
/* 661 */     return DensityFunctions.findTopSurface(densityFunction4, densityFunction3, -64, NoiseSettings.OVERWORLD_NOISE_SETTINGS.getCellHeight());
/*     */   }
/*     */   
/*     */   private static DensityFunction yLimitedInterpolatable(DensityFunction paramDensityFunction1, DensityFunction paramDensityFunction2, int paramInt1, int paramInt2, int paramInt3) {
/* 665 */     return DensityFunctions.interpolated(DensityFunctions.rangeChoice(paramDensityFunction1, paramInt1, (paramInt2 + 1), paramDensityFunction2, DensityFunctions.constant(paramInt3)));
/*     */   }
/*     */   
/*     */   private static DensityFunction slide(DensityFunction paramDensityFunction, int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble1, int paramInt5, int paramInt6, double paramDouble2) {
/* 669 */     DensityFunction densityFunction1 = paramDensityFunction;
/*     */     
/* 671 */     DensityFunction densityFunction2 = DensityFunctions.yClampedGradient(paramInt1 + paramInt2 - paramInt3, paramInt1 + paramInt2 - paramInt4, 1.0D, 0.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 677 */     densityFunction1 = DensityFunctions.lerp(densityFunction2, paramDouble1, densityFunction1);
/*     */     
/* 679 */     DensityFunction densityFunction3 = DensityFunctions.yClampedGradient(paramInt1 + paramInt5, paramInt1 + paramInt6, 0.0D, 1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 685 */     densityFunction1 = DensityFunctions.lerp(densityFunction3, paramDouble2, densityFunction1);
/*     */     
/* 687 */     return densityFunction1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final class QuantizedSpaghettiRarity
/*     */   {
/*     */     protected static double getSphaghettiRarity2D(double param1Double) {
/* 699 */       if (param1Double < -0.75D)
/* 700 */         return 0.5D; 
/* 701 */       if (param1Double < -0.5D)
/* 702 */         return 0.75D; 
/* 703 */       if (param1Double < 0.5D)
/* 704 */         return 1.0D; 
/* 705 */       if (param1Double < 0.75D) {
/* 706 */         return 2.0D;
/*     */       }
/* 708 */       return 3.0D;
/*     */     }
/*     */ 
/*     */     
/*     */     protected static double getSpaghettiRarity3D(double param1Double) {
/* 713 */       if (param1Double < -0.5D)
/* 714 */         return 0.75D; 
/* 715 */       if (param1Double < 0.0D)
/* 716 */         return 1.0D; 
/* 717 */       if (param1Double < 0.5D) {
/* 718 */         return 1.5D;
/*     */       }
/* 720 */       return 2.0D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\NoiseRouterData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */