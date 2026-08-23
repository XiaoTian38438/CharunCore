/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public class Noises {
/* 11 */   public static final ResourceKey<NormalNoise.NoiseParameters> TEMPERATURE = createKey("temperature");
/* 12 */   public static final ResourceKey<NormalNoise.NoiseParameters> VEGETATION = createKey("vegetation");
/* 13 */   public static final ResourceKey<NormalNoise.NoiseParameters> CONTINENTALNESS = createKey("continentalness");
/* 14 */   public static final ResourceKey<NormalNoise.NoiseParameters> EROSION = createKey("erosion");
/*    */   
/* 16 */   public static final ResourceKey<NormalNoise.NoiseParameters> TEMPERATURE_LARGE = createKey("temperature_large");
/* 17 */   public static final ResourceKey<NormalNoise.NoiseParameters> VEGETATION_LARGE = createKey("vegetation_large");
/* 18 */   public static final ResourceKey<NormalNoise.NoiseParameters> CONTINENTALNESS_LARGE = createKey("continentalness_large");
/* 19 */   public static final ResourceKey<NormalNoise.NoiseParameters> EROSION_LARGE = createKey("erosion_large");
/*    */   
/* 21 */   public static final ResourceKey<NormalNoise.NoiseParameters> RIDGE = createKey("ridge");
/* 22 */   public static final ResourceKey<NormalNoise.NoiseParameters> SHIFT = createKey("offset");
/*    */   
/* 24 */   public static final ResourceKey<NormalNoise.NoiseParameters> AQUIFER_BARRIER = createKey("aquifer_barrier");
/* 25 */   public static final ResourceKey<NormalNoise.NoiseParameters> AQUIFER_FLUID_LEVEL_FLOODEDNESS = createKey("aquifer_fluid_level_floodedness");
/* 26 */   public static final ResourceKey<NormalNoise.NoiseParameters> AQUIFER_LAVA = createKey("aquifer_lava");
/* 27 */   public static final ResourceKey<NormalNoise.NoiseParameters> AQUIFER_FLUID_LEVEL_SPREAD = createKey("aquifer_fluid_level_spread");
/*    */   
/* 29 */   public static final ResourceKey<NormalNoise.NoiseParameters> PILLAR = createKey("pillar");
/* 30 */   public static final ResourceKey<NormalNoise.NoiseParameters> PILLAR_RARENESS = createKey("pillar_rareness");
/* 31 */   public static final ResourceKey<NormalNoise.NoiseParameters> PILLAR_THICKNESS = createKey("pillar_thickness");
/*    */   
/* 33 */   public static final ResourceKey<NormalNoise.NoiseParameters> SPAGHETTI_2D = createKey("spaghetti_2d");
/* 34 */   public static final ResourceKey<NormalNoise.NoiseParameters> SPAGHETTI_2D_ELEVATION = createKey("spaghetti_2d_elevation");
/* 35 */   public static final ResourceKey<NormalNoise.NoiseParameters> SPAGHETTI_2D_MODULATOR = createKey("spaghetti_2d_modulator");
/* 36 */   public static final ResourceKey<NormalNoise.NoiseParameters> SPAGHETTI_2D_THICKNESS = createKey("spaghetti_2d_thickness");
/*    */   
/* 38 */   public static final ResourceKey<NormalNoise.NoiseParameters> SPAGHETTI_3D_1 = createKey("spaghetti_3d_1");
/* 39 */   public static final ResourceKey<NormalNoise.NoiseParameters> SPAGHETTI_3D_2 = createKey("spaghetti_3d_2");
/* 40 */   public static final ResourceKey<NormalNoise.NoiseParameters> SPAGHETTI_3D_RARITY = createKey("spaghetti_3d_rarity");
/* 41 */   public static final ResourceKey<NormalNoise.NoiseParameters> SPAGHETTI_3D_THICKNESS = createKey("spaghetti_3d_thickness");
/*    */   
/* 43 */   public static final ResourceKey<NormalNoise.NoiseParameters> SPAGHETTI_ROUGHNESS = createKey("spaghetti_roughness");
/* 44 */   public static final ResourceKey<NormalNoise.NoiseParameters> SPAGHETTI_ROUGHNESS_MODULATOR = createKey("spaghetti_roughness_modulator");
/*    */   
/* 46 */   public static final ResourceKey<NormalNoise.NoiseParameters> CAVE_ENTRANCE = createKey("cave_entrance");
/* 47 */   public static final ResourceKey<NormalNoise.NoiseParameters> CAVE_LAYER = createKey("cave_layer");
/*    */   
/* 49 */   public static final ResourceKey<NormalNoise.NoiseParameters> CAVE_CHEESE = createKey("cave_cheese");
/*    */   
/* 51 */   public static final ResourceKey<NormalNoise.NoiseParameters> ORE_VEININESS = createKey("ore_veininess");
/* 52 */   public static final ResourceKey<NormalNoise.NoiseParameters> ORE_VEIN_A = createKey("ore_vein_a");
/* 53 */   public static final ResourceKey<NormalNoise.NoiseParameters> ORE_VEIN_B = createKey("ore_vein_b");
/* 54 */   public static final ResourceKey<NormalNoise.NoiseParameters> ORE_GAP = createKey("ore_gap");
/*    */   
/* 56 */   public static final ResourceKey<NormalNoise.NoiseParameters> NOODLE = createKey("noodle");
/* 57 */   public static final ResourceKey<NormalNoise.NoiseParameters> NOODLE_THICKNESS = createKey("noodle_thickness");
/* 58 */   public static final ResourceKey<NormalNoise.NoiseParameters> NOODLE_RIDGE_A = createKey("noodle_ridge_a");
/* 59 */   public static final ResourceKey<NormalNoise.NoiseParameters> NOODLE_RIDGE_B = createKey("noodle_ridge_b");
/*    */   
/* 61 */   public static final ResourceKey<NormalNoise.NoiseParameters> JAGGED = createKey("jagged");
/*    */   
/* 63 */   public static final ResourceKey<NormalNoise.NoiseParameters> SURFACE = createKey("surface");
/* 64 */   public static final ResourceKey<NormalNoise.NoiseParameters> SURFACE_SECONDARY = createKey("surface_secondary");
/*    */   
/* 66 */   public static final ResourceKey<NormalNoise.NoiseParameters> CLAY_BANDS_OFFSET = createKey("clay_bands_offset");
/* 67 */   public static final ResourceKey<NormalNoise.NoiseParameters> BADLANDS_PILLAR = createKey("badlands_pillar");
/* 68 */   public static final ResourceKey<NormalNoise.NoiseParameters> BADLANDS_PILLAR_ROOF = createKey("badlands_pillar_roof");
/* 69 */   public static final ResourceKey<NormalNoise.NoiseParameters> BADLANDS_SURFACE = createKey("badlands_surface");
/*    */   
/* 71 */   public static final ResourceKey<NormalNoise.NoiseParameters> ICEBERG_PILLAR = createKey("iceberg_pillar");
/* 72 */   public static final ResourceKey<NormalNoise.NoiseParameters> ICEBERG_PILLAR_ROOF = createKey("iceberg_pillar_roof");
/* 73 */   public static final ResourceKey<NormalNoise.NoiseParameters> ICEBERG_SURFACE = createKey("iceberg_surface");
/*    */ 
/*    */   
/* 76 */   public static final ResourceKey<NormalNoise.NoiseParameters> SWAMP = createKey("surface_swamp");
/*    */   
/* 78 */   public static final ResourceKey<NormalNoise.NoiseParameters> CALCITE = createKey("calcite");
/* 79 */   public static final ResourceKey<NormalNoise.NoiseParameters> GRAVEL = createKey("gravel");
/* 80 */   public static final ResourceKey<NormalNoise.NoiseParameters> POWDER_SNOW = createKey("powder_snow");
/* 81 */   public static final ResourceKey<NormalNoise.NoiseParameters> PACKED_ICE = createKey("packed_ice");
/* 82 */   public static final ResourceKey<NormalNoise.NoiseParameters> ICE = createKey("ice");
/*    */   
/* 84 */   public static final ResourceKey<NormalNoise.NoiseParameters> SOUL_SAND_LAYER = createKey("soul_sand_layer");
/* 85 */   public static final ResourceKey<NormalNoise.NoiseParameters> GRAVEL_LAYER = createKey("gravel_layer");
/* 86 */   public static final ResourceKey<NormalNoise.NoiseParameters> PATCH = createKey("patch");
/* 87 */   public static final ResourceKey<NormalNoise.NoiseParameters> NETHERRACK = createKey("netherrack");
/* 88 */   public static final ResourceKey<NormalNoise.NoiseParameters> NETHER_WART = createKey("nether_wart");
/* 89 */   public static final ResourceKey<NormalNoise.NoiseParameters> NETHER_STATE_SELECTOR = createKey("nether_state_selector");
/*    */   
/*    */   private static ResourceKey<NormalNoise.NoiseParameters> createKey(String paramString) {
/* 92 */     return ResourceKey.create(Registries.NOISE, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   public static NormalNoise instantiate(HolderGetter<NormalNoise.NoiseParameters> paramHolderGetter, PositionalRandomFactory paramPositionalRandomFactory, ResourceKey<NormalNoise.NoiseParameters> paramResourceKey) {
/* 96 */     Holder.Reference reference = paramHolderGetter.getOrThrow(paramResourceKey);
/* 97 */     return NormalNoise.create(paramPositionalRandomFactory.fromHashOf(((ResourceKey)reference.unwrapKey().orElseThrow()).identifier()), (NormalNoise.NoiseParameters)reference.value());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\Noises.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */