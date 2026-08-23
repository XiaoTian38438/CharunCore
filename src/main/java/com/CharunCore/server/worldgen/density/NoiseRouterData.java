package com.CharunCore.server.worldgen.density;

import com.CharunCore.server.worldgen.density.functions.BlendedNoiseAsDF;
import com.CharunCore.server.world.gen.NoiseParameters;
import com.CharunCore.server.worldgen.density.functions.WeirdScaledSampler;
import com.CharunCore.server.worldgen.density.functions.*;

/**
 * 原版 net.minecraft.world.level.levelgen.NoiseRouterData 移植版。
 * 与原版 1:1 对应（删除 HolderGetter/RegistryCodec 引用，用直接 DensityFunction 对象引用替代）。
 *
 * 关键改动：
 * - 原 `Holder<NormalNoise.NoiseParameters> holderGetter.getOrThrow(Noises.X)` → `NoiseParameters.get("minecraft:X")`
 * - 原 `DensityFunctions.Spline.Coordinate` (哨兵) → 直接传入对应的 DensityFunction 对象
 * - 原 `registerAndWrap` 注册 DensityFunction 到 `BootstrapContext` 并返回 `HolderHolder` 包装 →
 *   我们改为存入 `Map<String, DensityFunction>`（densityFunctions 字典）并直接返回原对象（不需要 HolderHolder）
 * - 原 `getFunction(holderGetter, KEY)` → `densityFunctions.get("minecraft:KEY_NAME")`
 *
 * 所有数值常量与原版 727 行完全一致。
 */
public final class NoiseRouterData {

    public static final float GLOBAL_OFFSET = -0.50375F;
    public static final float ORE_THICKNESS = 0.08F;
    private static final double VEININESS_FREQUENCY = 1.5D;
    private static final double NOODLE_SPACING_AND_STRAIGHTNESS = 1.5D;
    public static final double SURFACE_DENSITY_THRESHOLD = 1.5625D;
    public static final double CHEESE_NOISE_TARGET = -0.703125D;
    public static final double NOISE_ZERO = 0.390625D;
    public static final int ISLAND_CHUNK_DISTANCE = 64;
    public static final long ISLAND_CHUNK_DISTANCE_SQR = 4096L;
    private static final int DENSITY_Y_ANCHOR_BOTTOM = -64;
    private static final int DENSITY_Y_ANCHOR_TOP = 320;
    private static final double DENSITY_Y_BOTTOM = 1.5D;
    private static final double DENSITY_Y_TOP = -1.5D;

    // DensityFunction 注册键（与原版 ResourceKey 相同）
    public static final String Y = "minecraft:y";
    public static final String SHIFT_X = "minecraft:shift_x";
    public static final String SHIFT_Z = "minecraft:shift_z";
    public static final String BASE_3D_NOISE_OVERWORLD = "minecraft:overworld/base_3d_noise";
    public static final String CONTINENTS = "minecraft:overworld/continents";
    public static final String EROSION = "minecraft:overworld/erosion";
    public static final String RIDGES = "minecraft:overworld/ridges";
    public static final String RIDGES_FOLDED = "minecraft:overworld/ridges_folded";
    public static final String OFFSET = "minecraft:overworld/offset";
    public static final String FACTOR = "minecraft:overworld/factor";
    public static final String JAGGEDNESS = "minecraft:overworld/jaggedness";
    public static final String DEPTH = "minecraft:overworld/depth";
    public static final String SLOPED_CHEESE = "minecraft:overworld/sloped_cheese";
    public static final String CONTINENTS_LARGE = "minecraft:overworld_large_biomes/continents";
    public static final String EROSION_LARGE = "minecraft:overworld_large_biomes/erosion";
    public static final String OFFSET_LARGE = "minecraft:overworld_large_biomes/offset";
    public static final String FACTOR_LARGE = "minecraft:overworld_large_biomes/factor";
    public static final String JAGGEDNESS_LARGE = "minecraft:overworld_large_biomes/jaggedness";
    public static final String DEPTH_LARGE = "minecraft:overworld_large_biomes/depth";
    public static final String SLOPED_CHEESE_LARGE = "minecraft:overworld_large_biomes/sloped_cheese";
    public static final String OFFSET_AMPLIFIED = "minecraft:overworld_amplified/offset";
    public static final String FACTOR_AMPLIFIED = "minecraft:overworld_amplified/factor";
    public static final String JAGGEDNESS_AMPLIFIED = "minecraft:overworld_amplified/jaggedness";
    public static final String DEPTH_AMPLIFIED = "minecraft:overworld_amplified/depth";
    public static final String SLOPED_CHEESE_AMPLIFIED = "minecraft:overworld_amplified/sloped_cheese";
    public static final String SPAGHETTI_ROUGHNESS_FUNCTION = "minecraft:overworld/caves/spaghetti_roughness_function";
    public static final String ENTRANCES = "minecraft:overworld/caves/entrances";
    public static final String NOODLE = "minecraft:overworld/caves/noodle";
    public static final String PILLARS = "minecraft:overworld/caves/pillars";
    public static final String SPAGHETTI_2D_THICKNESS_MODULATOR = "minecraft:overworld/caves/spaghetti_2d_thickness_modulator";
    public static final String SPAGHETTI_2D = "minecraft:overworld/caves/spaghetti_2d";

    private static final DensityFunction BLENDING_FACTOR = DensityFunctions.constant(10.0);
    private static final DensityFunction BLENDING_JAGGEDNESS = DensityFunctions.zero();

    /** DensityFunction 注册表 — 简化版替代 BootstrapContext */
    public static java.util.Map<String, DensityFunction> bootstrap() {
        java.util.Map<String, DensityFunction> map = new java.util.HashMap<>();
        map.put("minecraft:zero", DensityFunctions.zero());

        // i/j = DimensionType.MIN_Y/MAX_Y*2 = -64*2 = -128, 320*2 = 640
        int i = -128;
        int j = 640;
        DensityFunction y = DensityFunctions.yClampedGradient(i, j, i, j);
        map.put(Y, y);

        // SHIFT_X / SHIFT_Z
        DensityFunction densityFunction1 = registerAndWrap(map, SHIFT_X,
                DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA("minecraft:offset", NoiseParameters.get("minecraft:offset")))));
        DensityFunction densityFunction2 = registerAndWrap(map, SHIFT_Z,
                DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB("minecraft:offset", NoiseParameters.get("minecraft:offset")))));

        // BASE_3D_NOISE for overworld/nether/end — 注：原 BlendedNoise.createUnseeded 是静态工厂
        map.put(BASE_3D_NOISE_OVERWORLD, new BlendedNoiseAsDF(
                "minecraft:overworld/base_3d_noise", 0.25, 0.125, 80.0, 160.0, 8.0));
        map.put("minecraft:nether/base_3d_noise", new BlendedNoiseAsDF(
                "minecraft:nether/base_3d_noise", 0.25, 0.375, 80.0, 60.0, 8.0));
        map.put("minecraft:end/base_3d_noise", new BlendedNoiseAsDF(
                "minecraft:end/base_3d_noise", 0.25, 0.25, 80.0, 160.0, 4.0));

        // CONTINENTS / EROSION / RIDGES / RIDGES_FOLDED
        DensityFunction continents = registerAndWrap(map, CONTINENTS,
                DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d("minecraft:continentalness", densityFunction1, densityFunction2, 0.25, NoiseParameters.get("minecraft:continentalness"))));
        DensityFunction erosion = registerAndWrap(map, EROSION,
                DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d("minecraft:erosion", densityFunction1, densityFunction2, 0.25, NoiseParameters.get("minecraft:erosion"))));
        DensityFunction ridges = registerAndWrap(map, RIDGES,
                DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d("minecraft:ridge", densityFunction1, densityFunction2, 0.25, NoiseParameters.get("minecraft:ridge"))));
        map.put(RIDGES_FOLDED, TerrainSplineProvider.peaksAndValleysDF(ridges));

        // JAGGED noise density
        DensityFunction densityFunction4 = DensityFunctions.noise("minecraft:jagged", NoiseParameters.get("minecraft:jagged"), 1500.0, 0.0);

        registerTerrainNoises(map, densityFunction4, continents, erosion, OFFSET, FACTOR, JAGGEDNESS, DEPTH, SLOPED_CHEESE, false);

        // large biomes variant — 复用现有 noise 但 large 变种
        DensityFunction continentsLarge = registerAndWrap(map, CONTINENTS_LARGE,
                DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d("minecraft:continentalness_large", densityFunction1, densityFunction2, 0.25, NoiseParameters.get("minecraft:continentalness_large"))));
        DensityFunction erosionLarge = registerAndWrap(map, EROSION_LARGE,
                DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d("minecraft:erosion_large", densityFunction1, densityFunction2, 0.25, NoiseParameters.get("minecraft:erosion_large"))));

        registerTerrainNoises(map, densityFunction4, continentsLarge, erosionLarge, OFFSET_LARGE, FACTOR_LARGE, JAGGEDNESS_LARGE, DEPTH_LARGE, SLOPED_CHEESE_LARGE, false);
        registerTerrainNoises(map, densityFunction4, continents, erosion, OFFSET_AMPLIFIED, FACTOR_AMPLIFIED, JAGGEDNESS_AMPLIFIED, DEPTH_AMPLIFIED, SLOPED_CHEESE_AMPLIFIED, true);

        map.put("minecraft:end/sloped_cheese", DensityFunctions.add(DensityFunctions.endIslands(0L), getFunction(map, "minecraft:end/base_3d_noise")));

        map.put(SPAGHETTI_ROUGHNESS_FUNCTION, spaghettiRoughnessFunction());
        map.put(SPAGHETTI_2D_THICKNESS_MODULATOR,
                DensityFunctions.cacheOnce(DensityFunctions.mappedNoise("minecraft:spaghetti_2d_thickness", NoiseParameters.get("minecraft:spaghetti_2d_thickness"), 2.0, 1.0, -0.6, -1.3)));
        map.put(SPAGHETTI_2D, spaghetti2D(map));
        map.put(ENTRANCES, entrances(map));
        map.put(NOODLE, noodle(map));
        map.put(PILLARS, pillars());

        return map;
    }

    // ============== overworld() 入口 — 主制造 NoiseRouter ==============

    /** 实例化 overworld NoiseRouter。largeBiomes / amplified 同效果控制偏移与样条。 */
    public static NoiseRouter overworld(java.util.Map<String, DensityFunction> map,
                                        boolean largeBiomes, boolean amplified) {
        DensityFunction df1 = DensityFunctions.noise("minecraft:aquifer_barrier", NoiseParameters.get("minecraft:aquifer_barrier"), 0.5);
        DensityFunction df2 = DensityFunctions.noise("minecraft:aquifer_fluid_level_floodedness", NoiseParameters.get("minecraft:aquifer_fluid_level_floodedness"), 0.67);
        DensityFunction df3 = DensityFunctions.noise("minecraft:aquifer_fluid_level_spread", NoiseParameters.get("minecraft:aquifer_fluid_level_spread"), 0.7142857142857143);
        DensityFunction df4 = DensityFunctions.noise("minecraft:aquifer_lava", NoiseParameters.get("minecraft:aquifer_lava"));

        DensityFunction df5 = getFunction(map, SHIFT_X);
        DensityFunction df6 = getFunction(map, SHIFT_Z);

        String tempKey = largeBiomes ? "minecraft:temperature_large" : "minecraft:temperature";
        String vegKey = largeBiomes ? "minecraft:vegetation_large" : "minecraft:vegetation";
        DensityFunction df7 = DensityFunctions.shiftedNoise2d(tempKey, df5, df6, 0.25,
                NoiseParameters.get(tempKey));
        DensityFunction df8 = DensityFunctions.shiftedNoise2d(vegKey, df5, df6, 0.25,
                NoiseParameters.get(vegKey));

        DensityFunction df9 = getFunction(map, largeBiomes ? OFFSET_LARGE : (amplified ? OFFSET_AMPLIFIED : OFFSET));
        DensityFunction df10 = getFunction(map, largeBiomes ? FACTOR_LARGE : (amplified ? FACTOR_AMPLIFIED : FACTOR));
        DensityFunction df11 = getFunction(map, largeBiomes ? DEPTH_LARGE : (amplified ? DEPTH_AMPLIFIED : DEPTH));

        DensityFunction df12 = preliminarySurfaceLevel(map, df9, df10, amplified);
        DensityFunction df13 = getFunction(map, largeBiomes ? SLOPED_CHEESE_LARGE : (amplified ? SLOPED_CHEESE_AMPLIFIED : SLOPED_CHEESE));

        DensityFunction df14 = DensityFunctions.min(df13, DensityFunctions.mul(DensityFunctions.constant(5.0), getFunction(map, ENTRANCES)));
        DensityFunction df15 = DensityFunctions.rangeChoice(df13, -1000000.0, 1.5625, df14, underground(map, df13));
        DensityFunction df16 = DensityFunctions.min(postProcess(slideOverworld(amplified, df15)), getFunction(map, NOODLE));

        DensityFunction df17 = getFunction(map, Y);

        int minYVein = -60;
        int maxYVein = 50;
        DensityFunction df18 = yLimitedInterpolatable(df17, DensityFunctions.noise("minecraft:ore_veininess", NoiseParameters.get("minecraft:ore_veininess"), 1.5, 1.5), minYVein, maxYVein, 0);
        DensityFunction df19 = yLimitedInterpolatable(df17, DensityFunctions.noise("minecraft:ore_vein_a", NoiseParameters.get("minecraft:ore_vein_a"), 4.0, 4.0), minYVein, maxYVein, 0).abs();
        DensityFunction df20 = yLimitedInterpolatable(df17, DensityFunctions.noise("minecraft:ore_vein_b", NoiseParameters.get("minecraft:ore_vein_b"), 4.0, 4.0), minYVein, maxYVein, 0).abs();
        DensityFunction df21 = DensityFunctions.add(DensityFunctions.constant(-0.07999999821186066), DensityFunctions.max(df19, df20));
        DensityFunction df22 = DensityFunctions.noise("minecraft:ore_gap", NoiseParameters.get("minecraft:ore_gap"));

        return new NoiseRouter(df1, df2, df3, df4, df7, df8,
                getFunction(map, largeBiomes ? CONTINENTS_LARGE : CONTINENTS),
                getFunction(map, largeBiomes ? EROSION_LARGE : EROSION),
                df11,
                getFunction(map, RIDGES), df12, df16, df18, df21, df22);
    }

    /** nether 镜像 — 主世界不用，但保留接口 */
    public static NoiseRouter nether(java.util.Map<String, DensityFunction> map) {
        return noNewCaves(map, slideNetherLike(map, 0, 128));
    }

    public static NoiseRouter caves(java.util.Map<String, DensityFunction> map) {
        return noNewCaves(map, slideNetherLike(map, -64, 192));
    }

    public static NoiseRouter floatingIslands(java.util.Map<String, DensityFunction> map) {
        return noNewCaves(map, slideEndLike(getFunction(map, "minecraft:end/base_3d_noise"), 0, 256));
    }

    public static NoiseRouter end(java.util.Map<String, DensityFunction> map) {
        DensityFunction densityFunction1 = DensityFunctions.cache2d(DensityFunctions.endIslands(0L));
        DensityFunction densityFunction2 = postProcess(slideEnd(getFunction(map, "minecraft:end/sloped_cheese")));
        return new NoiseRouter(
                DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(),
                DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), densityFunction1,
                DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), densityFunction2,
                DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    public static NoiseRouter none() {
        DensityFunction z = DensityFunctions.zero();
        return new NoiseRouter(z, z, z, z, z, z, z, z, z, z, z, z, z, z, z);
    }

    // ============== 内部辅助方法 (镜像原版 NoiseRouterData 内部静态方法) ==============

    @SuppressWarnings("DuplicatedCode")
    private static void registerTerrainNoises(java.util.Map<String, DensityFunction> map,
                                              DensityFunction jaggedNoiseDensity,
                                              DensityFunction continentsDensity,
                                              DensityFunction erosionDensity,
                                              String offsetKey, String factorKey, String jaggednessKey,
                                              String depthKey, String slopedCheeseKey, boolean amplified) {
        // 这里 recurrence continentsDensity 是 DensityFunction 直接引用，类比原版 Spline.Coordinate
        DensityFunction offsetSpline = TerrainSplineProvider.offsetSpline(continentsDensity, erosionDensity,
                // ridges_folded 等价从 ridges 计算（用移植物提供峰值函数），但原版直接拿 RIDGES_FOLDED holder
                TerrainSplineProvider.peaksAndValleysDF(getFunction(map, RIDGES)),
                amplified);
        DensityFunction offsetDF = splineWithBlending(
                DensityFunctions.add(DensityFunctions.constant(-0.5037500262260437), offsetSpline),
                DensityFunctions.blendOffset());
        registerAndWrap(map, offsetKey, offsetDF);

        DensityFunction factorDF = splineWithBlending(
                TerrainSplineProvider.factorSpline(continentsDensity, erosionDensity,
                        getFunction(map, RIDGES),
                        TerrainSplineProvider.peaksAndValleysDF(getFunction(map, RIDGES)),
                        amplified),
                BLENDING_FACTOR);
        registerAndWrap(map, factorKey, factorDF);

        DensityFunction depthDF = offsetToDepth(offsetDF);
        registerAndWrap(map, depthKey, depthDF);

        DensityFunction jaggednessDF = splineWithBlending(
                TerrainSplineProvider.jaggednessSpline(continentsDensity, erosionDensity,
                        getFunction(map, RIDGES),
                        TerrainSplineProvider.peaksAndValleysDF(getFunction(map, RIDGES)),
                        amplified),
                BLENDING_JAGGEDNESS);
        registerAndWrap(map, jaggednessKey, jaggednessDF);

        DensityFunction jagMul = DensityFunctions.mul(jaggednessDF, jaggedNoiseDensity.halfNegative());
        DensityFunction noiseGradient = noiseGradientDensity(factorDF, DensityFunctions.add(depthDF, jagMul));
        map.put(slopedCheeseKey, DensityFunctions.add(noiseGradient, getFunction(map, BASE_3D_NOISE_OVERWORLD)));
    }

    private static DensityFunction offsetToDepth(DensityFunction offsetDF) {
        return DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5), offsetDF);
    }

    /** 原版 registerAndWrap 返回 new HolderHolder(holder) — 这里无 Holder 概念，直接返回原对象。 */
    private static DensityFunction registerAndWrap(java.util.Map<String, DensityFunction> map,
                                                   String key, DensityFunction df) {
        map.put(key, df);
        return df;
    }

    private static DensityFunction getFunction(java.util.Map<String, DensityFunction> map, String key) {
        DensityFunction df = map.get(key);
        if (df == null) throw new IllegalStateException("DensityFunction not registered: " + key);
        return df;
    }

    private static DensityFunction spaghettiRoughnessFunction() {
        DensityFunction roughness = DensityFunctions.noise("minecraft:spaghetti_roughness", NoiseParameters.get("minecraft:spaghetti_roughness"));
        DensityFunction mod = DensityFunctions.mappedNoise("minecraft:spaghetti_roughness_modulator", NoiseParameters.get("minecraft:spaghetti_roughness_modulator"), 0.0, -0.1);
        return DensityFunctions.cacheOnce(DensityFunctions.mul(mod, DensityFunctions.add(roughness.abs(), DensityFunctions.constant(-0.4))));
    }

    private static DensityFunction entrances(java.util.Map<String, DensityFunction> map) {
        DensityFunction rarity3d = DensityFunctions.cacheOnce(DensityFunctions.noise("minecraft:spaghetti_3d_rarity", NoiseParameters.get("minecraft:spaghetti_3d_rarity"), 2.0, 1.0));
        DensityFunction thickness3d = DensityFunctions.mappedNoise("minecraft:spaghetti_3d_thickness", NoiseParameters.get("minecraft:spaghetti_3d_thickness"), -0.065, -0.088);
        DensityFunction s1 = DensityFunctions.weirdScaledSampler("minecraft:spaghetti_3d_1", rarity3d, NoiseParameters.get("minecraft:spaghetti_3d_1"), WeirdScaledSampler.RarityValueMapper.TYPE1);
        DensityFunction s2 = DensityFunctions.weirdScaledSampler("minecraft:spaghetti_3d_2", rarity3d, NoiseParameters.get("minecraft:spaghetti_3d_2"), WeirdScaledSampler.RarityValueMapper.TYPE1);
        DensityFunction combined = DensityFunctions.add(DensityFunctions.max(s1, s2), thickness3d).clamp(-1.0, 1.0);
        DensityFunction roughFunc = getFunction(map, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction entranceNoise = DensityFunctions.noise("minecraft:cave_entrance", NoiseParameters.get("minecraft:cave_entrance"), 0.75, 0.5);
        DensityFunction entrance = DensityFunctions.add(
                DensityFunctions.add(entranceNoise, DensityFunctions.constant(0.37)),
                DensityFunctions.yClampedGradient(-10, 30, 0.3, 0.0));
        return DensityFunctions.cacheOnce(DensityFunctions.min(entrance, DensityFunctions.add(roughFunc, combined)));
    }

    private static DensityFunction noodle(java.util.Map<String, DensityFunction> map) {
        DensityFunction y = getFunction(map, Y);
        DensityFunction main = yLimitedInterpolatable(y, DensityFunctions.noise("minecraft:noodle", NoiseParameters.get("minecraft:noodle"), 1.0, 1.0), -60, 320, -1);
        DensityFunction thickness = yLimitedInterpolatable(y, DensityFunctions.mappedNoise("minecraft:noodle_thickness", NoiseParameters.get("minecraft:noodle_thickness"), 1.0, 1.0, -0.05, -0.1), -60, 320, 0);
        DensityFunction ridgeA = yLimitedInterpolatable(y, DensityFunctions.noise("minecraft:noodle_ridge_a", NoiseParameters.get("minecraft:noodle_ridge_a"), 2.6666666666666665, 2.6666666666666665), -60, 320, 0);
        DensityFunction ridgeB = yLimitedInterpolatable(y, DensityFunctions.noise("minecraft:noodle_ridge_b", NoiseParameters.get("minecraft:noodle_ridge_b"), 2.6666666666666665, 2.6666666666666665), -60, 320, 0);
        DensityFunction ridge = DensityFunctions.mul(DensityFunctions.constant(1.5), DensityFunctions.max(ridgeA.abs(), ridgeB.abs()));
        return DensityFunctions.rangeChoice(main, -1000000.0, 0.0, DensityFunctions.constant(64.0), DensityFunctions.add(thickness, ridge));
    }

    private static DensityFunction pillars() {
        DensityFunction pillar = DensityFunctions.noise("minecraft:pillar", NoiseParameters.get("minecraft:pillar"), 25.0, 0.3);
        DensityFunction rareness = DensityFunctions.mappedNoise("minecraft:pillar_rareness", NoiseParameters.get("minecraft:pillar_rareness"), 0.0, -2.0);
        DensityFunction thickness = DensityFunctions.mappedNoise("minecraft:pillar_thickness", NoiseParameters.get("minecraft:pillar_thickness"), 0.0, 1.1);
        DensityFunction combined = DensityFunctions.add(DensityFunctions.mul(pillar, DensityFunctions.constant(2.0)), rareness);
        return DensityFunctions.cacheOnce(DensityFunctions.mul(combined, thickness.cube()));
    }

    private static DensityFunction spaghetti2D(java.util.Map<String, DensityFunction> map) {
        DensityFunction modulator = DensityFunctions.noise("minecraft:spaghetti_2d_modulator", NoiseParameters.get("minecraft:spaghetti_2d_modulator"), 2.0, 1.0);
        DensityFunction spaghetti = DensityFunctions.weirdScaledSampler("minecraft:spaghetti_2d", modulator, NoiseParameters.get("minecraft:spaghetti_2d"), WeirdScaledSampler.RarityValueMapper.TYPE2);
        DensityFunction elevation = DensityFunctions.mappedNoise("minecraft:spaghetti_2d_elevation", NoiseParameters.get("minecraft:spaghetti_2d_elevation"), 0.0, Math.floorDiv(-64, 8), 8.0);
        DensityFunction thicknessMod = getFunction(map, SPAGHETTI_2D_THICKNESS_MODULATOR);
        DensityFunction dist = DensityFunctions.add(elevation, DensityFunctions.yClampedGradient(-64, 320, 8.0, -40.0)).abs();
        DensityFunction distCubed = DensityFunctions.add(dist, thicknessMod).cube();
        DensityFunction contrib = DensityFunctions.add(spaghetti, DensityFunctions.mul(DensityFunctions.constant(0.083), thicknessMod));
        return DensityFunctions.max(contrib, distCubed).clamp(-1.0, 1.0);
    }

    private static DensityFunction underground(java.util.Map<String, DensityFunction> map, DensityFunction slopedCheese) {
        DensityFunction sp2d = getFunction(map, SPAGHETTI_2D);
        DensityFunction rough = getFunction(map, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction cacheLayer = DensityFunctions.noise("minecraft:cave_layer", NoiseParameters.get("minecraft:cave_layer"), 8.0);
        DensityFunction layerSq = DensityFunctions.mul(DensityFunctions.constant(4.0), cacheLayer.square());
        DensityFunction cheese = DensityFunctions.noise("minecraft:cave_cheese", NoiseParameters.get("minecraft:cave_cheese"), 0.6666666666666666);
        DensityFunction cheesePart = DensityFunctions.add(
                DensityFunctions.add(DensityFunctions.constant(0.27), cheese).clamp(-1.0, 1.0),
                DensityFunctions.add(DensityFunctions.constant(1.5), DensityFunctions.mul(DensityFunctions.constant(-0.64), slopedCheese)).clamp(0.0, 0.5));
        DensityFunction underground1 = DensityFunctions.add(layerSq, cheesePart);
        DensityFunction underground2 = DensityFunctions.min(DensityFunctions.min(underground1, getFunction(map, ENTRANCES)), DensityFunctions.add(sp2d, rough));
        DensityFunction pillars = getFunction(map, PILLARS);
        DensityFunction pillarGate = DensityFunctions.rangeChoice(pillars, -1000000.0, 0.03, DensityFunctions.constant(-1000000.0), pillars);
        return DensityFunctions.max(underground2, pillarGate);
    }

    private static DensityFunction postProcess(DensityFunction df) {
        DensityFunction blended = DensityFunctions.blendDensity(df);
        return DensityFunctions.mul(DensityFunctions.interpolated(blended), DensityFunctions.constant(0.64)).squeeze();
    }

    private static DensityFunction remap(DensityFunction df, double inMin, double inMax, double outMin, double outMax) {
        double d1 = (outMax - outMin) / (inMax - inMin);
        double d2 = outMin - inMin * d1;
        return DensityFunctions.add(DensityFunctions.mul(df, DensityFunctions.constant(d1)), DensityFunctions.constant(d2));
    }

    private static DensityFunction slideOverworld(boolean amplified, DensityFunction df) {
        return slide(df, -64, 384, amplified ? 16 : 80, amplified ? 0 : 64, -0.078125, 0, 24, amplified ? 0.4 : 0.1171875);
    }

    private static DensityFunction slideNetherLike(java.util.Map<String, DensityFunction> map, int minY, int height) {
        return slide(getFunction(map, "minecraft:nether/base_3d_noise"), minY, height, 24, 0, 0.9375, -8, 24, 2.5);
    }

    private static DensityFunction slideEndLike(DensityFunction df, int minY, int height) {
        return slide(df, minY, height, 72, -184, -23.4375, 4, 32, -0.234375);
    }

    private static DensityFunction slideEnd(DensityFunction df) {
        return slideEndLike(df, 0, 128);
    }

    @SuppressWarnings("SameParameterValue")
    private static DensityFunction slide(DensityFunction df, int minY, int height,
                                         int topFrom, int topTo, double topValue,
                                         int bottomFrom, int bottomTo, double bottomValue) {
        DensityFunction densityFunction1 = df;
        DensityFunction densityFunction2 = DensityFunctions.yClampedGradient(minY + height - topFrom, minY + height - topTo, 1.0, 0.0);
        densityFunction1 = DensityFunctions.lerp(densityFunction2, topValue, densityFunction1);
        DensityFunction densityFunction3 = DensityFunctions.yClampedGradient(minY + bottomFrom, minY + bottomTo, 0.0, 1.0);
        densityFunction1 = DensityFunctions.lerp(densityFunction3, bottomValue, densityFunction1);
        return densityFunction1;
    }

    private static DensityFunction splineWithBlending(DensityFunction df1, DensityFunction df2) {
        DensityFunction blended = DensityFunctions.lerp(DensityFunctions.blendAlpha(), df2, df1);
        return DensityFunctions.flatCache(DensityFunctions.cache2d(blended));
    }

    private static DensityFunction noiseGradientDensity(DensityFunction factor, DensityFunction depth) {
        DensityFunction multiplied = DensityFunctions.mul(depth, factor);
        return DensityFunctions.mul(DensityFunctions.constant(4.0), multiplied.quarterNegative());
    }

    private static DensityFunction preliminarySurfaceLevel(java.util.Map<String, DensityFunction> map,
                                                          DensityFunction offset, DensityFunction factor, boolean amplified) {
        DensityFunction cachedFactor = DensityFunctions.cache2d(factor);
        DensityFunction cachedOffset = DensityFunctions.cache2d(offset);
        DensityFunction remapped = remap(
                DensityFunctions.add(
                        DensityFunctions.mul(DensityFunctions.constant(0.2734375), cachedFactor.invert()),
                        DensityFunctions.mul(DensityFunctions.constant(-1.0), cachedOffset)),
                1.5, -1.5, -64.0, 320.0);
        DensityFunction clamped = remapped.clamp(-40.0, 320.0);

        // OVERWORLD cellHeight = 8（原版 NoiseSettings.OVERWORLD_NOISE_SETTINGS.getCellHeight()）
        DensityFunction surface = DensityFunctions.add(
                slideOverworld(amplified, DensityFunctions.add(
                        noiseGradientDensity(cachedFactor, offsetToDepth(cachedOffset)),
                        DensityFunctions.constant(-0.703125)).clamp(-64.0, 64.0)),
                DensityFunctions.constant(-0.390625));

        return DensityFunctions.findTopSurface(surface, clamped, -64, 8);
    }

    @SuppressWarnings("SameParameterValue")
    private static DensityFunction yLimitedInterpolatable(DensityFunction y, DensityFunction df,
                                                          int minY, int maxY, int elseReturn) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(y, minY, (maxY + 1), df, DensityFunctions.constant(elseReturn)));
    }

    private static NoiseRouter noNewCaves(java.util.Map<String, DensityFunction> map, DensityFunction df) {
        DensityFunction shiftX = getFunction(map, SHIFT_X);
        DensityFunction shiftZ = getFunction(map, SHIFT_Z);
        DensityFunction temperature = DensityFunctions.shiftedNoise2d("minecraft:temperature", shiftX, shiftZ, 0.25, NoiseParameters.get("minecraft:temperature"));
        DensityFunction vegetation = DensityFunctions.shiftedNoise2d("minecraft:vegetation", shiftX, shiftZ, 0.25, NoiseParameters.get("minecraft:vegetation"));
        DensityFunction postProcessed = postProcess(df);
        DensityFunction z = DensityFunctions.zero();
        return new NoiseRouter(z, z, z, z, temperature, vegetation,
                z, z, z, z, z, postProcessed, z, z, z);
    }

    /** 原版 NoiseRouterData$QuantizedSpaghettiRarity — 稀有度分桶 */
    protected static final class QuantizedSpaghettiRarity {
        protected static double getSphaghettiRarity2D(double v) {
            if (v < -0.75) return 0.5;
            if (v < -0.5)  return 0.75;
            if (v < 0.5)   return 1.0;
            if (v < 0.75)  return 2.0;
            return 3.0;
        }
        protected static double getSpaghettiRarity3D(double v) {
            if (v < -0.5) return 0.75;
            if (v < 0.0)  return 1.0;
            if (v < 0.5)   return 1.5;
            return 2.0;
        }
    }
}
