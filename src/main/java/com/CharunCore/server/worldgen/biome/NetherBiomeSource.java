package com.CharunCore.server.worldgen.biome;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import com.CharunCore.server.worldgen.density.NoiseRouter;

/**
 * 原版 MultiNoiseBiomeSourceParameterList.Preset.NETHER 逐点移植：
 * 五个下界群系由 (temperature, humidity, offset) 最近邻决定（其余参数恒 0），
 * 采样自 nether 路由的 shifted_noise(temperature/vegetation, xz_scale 0.25)。
 */
public final class NetherBiomeSource {

    public static final int B_BASALT_DELTAS = 2;
    public static final int B_CRIMSON_FOREST = 7;
    public static final int B_NETHER_WASTES = 34;
    public static final int B_SOUL_SAND_VALLEY = 49;
    public static final int B_WARPED_FOREST = 59;

    private final Climate.ParameterList<Integer> parameters;

    public NetherBiomeSource() {
        this.parameters = new Climate.ParameterList<>(List.of(
            entry(Climate.parameters(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), B_NETHER_WASTES),
            entry(Climate.parameters(0.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), B_SOUL_SAND_VALLEY),
            entry(Climate.parameters(0.4f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), B_CRIMSON_FOREST),
            entry(Climate.parameters(0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.375f), B_WARPED_FOREST),
            entry(Climate.parameters(-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.175f), B_BASALT_DELTAS)));
    }

    private static SimpleEntry<Climate.ParameterPoint, Integer> entry(Climate.ParameterPoint p, int id) {
        return new SimpleEntry<>(p, id);
    }

    /** quart 坐标采样（供 BiomeManager 解析器使用）。 */
    public int getBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
        return parameters.findValue(sampler.sample(quartX, quartY, quartZ));
    }

    public Climate.Sampler createSampler(NoiseRouter router) {
        return new Climate.Sampler(
            router.temperature(), router.vegetation(),
            router.continents(), router.erosion(), router.depth(), router.ridges());
    }

    /** 方块坐标便捷采样（表面规则/特征用）。 */
    public int getBiomeAt(NoiseRouter router, int blockX, int blockY, int blockZ) {
        return getBiome(blockX >> 2, blockY >> 2, blockZ >> 2, createSampler(router));
    }
}
