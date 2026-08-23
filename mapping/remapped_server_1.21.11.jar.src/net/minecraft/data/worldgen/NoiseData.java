/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.Noises;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoiseData
/*     */ {
/*     */   @Deprecated
/*  12 */   public static final NormalNoise.NoiseParameters DEFAULT_SHIFT = new NormalNoise.NoiseParameters(-3, 1.0D, new double[] { 1.0D, 1.0D, 0.0D });
/*     */   
/*     */   public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> paramBootstrapContext) {
/*  15 */     registerBiomeNoises(paramBootstrapContext, 0, Noises.TEMPERATURE, Noises.VEGETATION, Noises.CONTINENTALNESS, Noises.EROSION);
/*  16 */     registerBiomeNoises(paramBootstrapContext, -2, Noises.TEMPERATURE_LARGE, Noises.VEGETATION_LARGE, Noises.CONTINENTALNESS_LARGE, Noises.EROSION_LARGE);
/*     */     
/*  18 */     register(paramBootstrapContext, Noises.RIDGE, -7, 1.0D, new double[] { 2.0D, 1.0D, 0.0D, 0.0D, 0.0D });
/*  19 */     paramBootstrapContext.register(Noises.SHIFT, DEFAULT_SHIFT);
/*     */     
/*  21 */     register(paramBootstrapContext, Noises.AQUIFER_BARRIER, -3, 1.0D, new double[0]);
/*  22 */     register(paramBootstrapContext, Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS, -7, 1.0D, new double[0]);
/*  23 */     register(paramBootstrapContext, Noises.AQUIFER_LAVA, -1, 1.0D, new double[0]);
/*  24 */     register(paramBootstrapContext, Noises.AQUIFER_FLUID_LEVEL_SPREAD, -5, 1.0D, new double[0]);
/*     */     
/*  26 */     register(paramBootstrapContext, Noises.PILLAR, -7, 1.0D, new double[] { 1.0D });
/*  27 */     register(paramBootstrapContext, Noises.PILLAR_RARENESS, -8, 1.0D, new double[0]);
/*  28 */     register(paramBootstrapContext, Noises.PILLAR_THICKNESS, -8, 1.0D, new double[0]);
/*     */     
/*  30 */     register(paramBootstrapContext, Noises.SPAGHETTI_2D, -7, 1.0D, new double[0]);
/*  31 */     register(paramBootstrapContext, Noises.SPAGHETTI_2D_ELEVATION, -8, 1.0D, new double[0]);
/*  32 */     register(paramBootstrapContext, Noises.SPAGHETTI_2D_MODULATOR, -11, 1.0D, new double[0]);
/*  33 */     register(paramBootstrapContext, Noises.SPAGHETTI_2D_THICKNESS, -11, 1.0D, new double[0]);
/*     */     
/*  35 */     register(paramBootstrapContext, Noises.SPAGHETTI_3D_1, -7, 1.0D, new double[0]);
/*  36 */     register(paramBootstrapContext, Noises.SPAGHETTI_3D_2, -7, 1.0D, new double[0]);
/*  37 */     register(paramBootstrapContext, Noises.SPAGHETTI_3D_RARITY, -11, 1.0D, new double[0]);
/*  38 */     register(paramBootstrapContext, Noises.SPAGHETTI_3D_THICKNESS, -8, 1.0D, new double[0]);
/*     */     
/*  40 */     register(paramBootstrapContext, Noises.SPAGHETTI_ROUGHNESS, -5, 1.0D, new double[0]);
/*  41 */     register(paramBootstrapContext, Noises.SPAGHETTI_ROUGHNESS_MODULATOR, -8, 1.0D, new double[0]);
/*     */     
/*  43 */     register(paramBootstrapContext, Noises.CAVE_ENTRANCE, -7, 0.4D, new double[] { 0.5D, 1.0D });
/*  44 */     register(paramBootstrapContext, Noises.CAVE_LAYER, -8, 1.0D, new double[0]);
/*     */     
/*  46 */     register(paramBootstrapContext, Noises.CAVE_CHEESE, -8, 0.5D, new double[] { 1.0D, 2.0D, 1.0D, 2.0D, 1.0D, 0.0D, 2.0D, 0.0D });
/*     */     
/*  48 */     register(paramBootstrapContext, Noises.ORE_VEININESS, -8, 1.0D, new double[0]);
/*  49 */     register(paramBootstrapContext, Noises.ORE_VEIN_A, -7, 1.0D, new double[0]);
/*  50 */     register(paramBootstrapContext, Noises.ORE_VEIN_B, -7, 1.0D, new double[0]);
/*  51 */     register(paramBootstrapContext, Noises.ORE_GAP, -5, 1.0D, new double[0]);
/*     */     
/*  53 */     register(paramBootstrapContext, Noises.NOODLE, -8, 1.0D, new double[0]);
/*  54 */     register(paramBootstrapContext, Noises.NOODLE_THICKNESS, -8, 1.0D, new double[0]);
/*  55 */     register(paramBootstrapContext, Noises.NOODLE_RIDGE_A, -7, 1.0D, new double[0]);
/*  56 */     register(paramBootstrapContext, Noises.NOODLE_RIDGE_B, -7, 1.0D, new double[0]);
/*     */     
/*  58 */     register(paramBootstrapContext, Noises.JAGGED, -16, 1.0D, new double[] { 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     register(paramBootstrapContext, Noises.SURFACE, -6, 1.0D, new double[] { 1.0D, 1.0D });
/*  66 */     register(paramBootstrapContext, Noises.SURFACE_SECONDARY, -6, 1.0D, new double[] { 1.0D, 0.0D, 1.0D });
/*     */     
/*  68 */     register(paramBootstrapContext, Noises.CLAY_BANDS_OFFSET, -8, 1.0D, new double[0]);
/*  69 */     register(paramBootstrapContext, Noises.BADLANDS_PILLAR, -2, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  70 */     register(paramBootstrapContext, Noises.BADLANDS_PILLAR_ROOF, -8, 1.0D, new double[0]);
/*  71 */     register(paramBootstrapContext, Noises.BADLANDS_SURFACE, -6, 1.0D, new double[] { 1.0D, 1.0D });
/*     */     
/*  73 */     register(paramBootstrapContext, Noises.ICEBERG_PILLAR, -6, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  74 */     register(paramBootstrapContext, Noises.ICEBERG_PILLAR_ROOF, -3, 1.0D, new double[0]);
/*  75 */     register(paramBootstrapContext, Noises.ICEBERG_SURFACE, -6, 1.0D, new double[] { 1.0D, 1.0D });
/*     */     
/*  77 */     register(paramBootstrapContext, Noises.SWAMP, -2, 1.0D, new double[0]);
/*     */     
/*  79 */     register(paramBootstrapContext, Noises.CALCITE, -9, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  80 */     register(paramBootstrapContext, Noises.GRAVEL, -8, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  81 */     register(paramBootstrapContext, Noises.POWDER_SNOW, -6, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  82 */     register(paramBootstrapContext, Noises.PACKED_ICE, -7, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  83 */     register(paramBootstrapContext, Noises.ICE, -4, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*     */     
/*  85 */     register(paramBootstrapContext, Noises.SOUL_SAND_LAYER, -8, 1.0D, new double[] { 1.0D, 1.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.013333333333333334D });
/*  86 */     register(paramBootstrapContext, Noises.GRAVEL_LAYER, -8, 1.0D, new double[] { 1.0D, 1.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.013333333333333334D });
/*  87 */     register(paramBootstrapContext, Noises.PATCH, -5, 1.0D, new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 0.013333333333333334D });
/*  88 */     register(paramBootstrapContext, Noises.NETHERRACK, -3, 1.0D, new double[] { 0.0D, 0.0D, 0.35D });
/*  89 */     register(paramBootstrapContext, Noises.NETHER_WART, -3, 1.0D, new double[] { 0.0D, 0.0D, 0.9D });
/*  90 */     register(paramBootstrapContext, Noises.NETHER_STATE_SELECTOR, -4, 1.0D, new double[0]);
/*     */   }
/*     */   
/*     */   private static void registerBiomeNoises(BootstrapContext<NormalNoise.NoiseParameters> paramBootstrapContext, int paramInt, ResourceKey<NormalNoise.NoiseParameters> paramResourceKey1, ResourceKey<NormalNoise.NoiseParameters> paramResourceKey2, ResourceKey<NormalNoise.NoiseParameters> paramResourceKey3, ResourceKey<NormalNoise.NoiseParameters> paramResourceKey4) {
/*  94 */     register(paramBootstrapContext, paramResourceKey1, -10 + paramInt, 1.5D, new double[] { 0.0D, 1.0D, 0.0D, 0.0D, 0.0D });
/*  95 */     register(paramBootstrapContext, paramResourceKey2, -8 + paramInt, 1.0D, new double[] { 1.0D, 0.0D, 0.0D, 0.0D, 0.0D });
/*  96 */     register(paramBootstrapContext, paramResourceKey3, -9 + paramInt, 1.0D, new double[] { 1.0D, 2.0D, 2.0D, 2.0D, 1.0D, 1.0D, 1.0D, 1.0D });
/*  97 */     register(paramBootstrapContext, paramResourceKey4, -9 + paramInt, 1.0D, new double[] { 1.0D, 0.0D, 1.0D, 1.0D });
/*     */   }
/*     */   
/*     */   private static void register(BootstrapContext<NormalNoise.NoiseParameters> paramBootstrapContext, ResourceKey<NormalNoise.NoiseParameters> paramResourceKey, int paramInt, double paramDouble, double... paramVarArgs) {
/* 101 */     paramBootstrapContext.register(paramResourceKey, new NormalNoise.NoiseParameters(paramInt, paramDouble, paramVarArgs));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\NoiseData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */