package com.CharunCore.server.worldgen;

import java.util.ArrayList;
import java.util.List;

import com.CharunCore.server.worldgen.feature.vanilla.BiomeFeatureLists;
import com.CharunCore.server.worldgen.feature.vanilla.FeaturePipeline;

/**
 * 原版 ChunkGenerator.applyBiomeDecoration 装饰循环移植：
 * decorationSeed = setDecorationSeed(levelSeed, chunkOriginX, chunkOriginZ)，
 * 每 step 内以「全局特征索引」（跨群系去重后的注册顺序）调用 setFeatureSeed(deco, globalIdx, step)。
 */
public final class FeatureDecoration {

    private static final int STEPS = BiomeFeatureLists.STEP_COUNT;
    private static volatile List<String>[] GLOBAL; // per step

    private FeatureDecoration() {}

    @SuppressWarnings("unchecked")
    private static List<String>[] global() {
        List<String>[] g = GLOBAL;
        if (g == null) {
            synchronized (FeatureDecoration.class) {
                if (GLOBAL == null) {
                    List<String>[] arr = new List[STEPS];
                    for (int s = 0; s < STEPS; s++) {
                        // 按群系注册表 id 升序（= vanilla bootstrap 注册顺序）首次出现去重
                        java.util.LinkedHashSet<String> dedup = new java.util.LinkedHashSet<>();
                        for (int b = 0; b <= 64; b++) {
                            dedup.addAll(BiomeFeatureLists.featuresForStep(b, s));
                        }
                        arr[s] = new ArrayList<>(dedup);
                    }
                    GLOBAL = arr;
                    g = arr;
                }
            }
        }
        return g;
    }

    /** 主世界装饰入口。biomeAt = 原始气候采样 (blockX, blockZ) -> biomeId。 */
    public static void decorate(WorldGenLevel level, int chunkX, int chunkZ, long levelSeed,
                                java.util.function.IntBinaryOperator biomeAt) {
        com.CharunCore.server.world.gen.WorldgenRandom wr =
            new com.CharunCore.server.world.gen.WorldgenRandom(
                new com.CharunCore.server.world.gen.XoroshiroRandomSource(0L));
        long decoSeed = wr.setDecorationSeed(levelSeed, chunkX * 16, chunkZ * 16);
        List<String>[] g = global();
        int n2 = Math.max(11, g.length);
        for (int step = 0; step < n2; step++) {
            if (step >= g.length) continue;
            List<String> stepList = g[step];
            for (int idx = 0; idx < stepList.size(); idx++) {
                String placedName = stepList.get(idx);
                wr.setFeatureSeed(decoSeed, idx, step);
                try {
                    FeaturePipeline.place(placedName, level, chunkX << 4, chunkZ << 4, wr, biomeAt);
                } catch (Throwable t) {
                    FeaturePipeline.unsupported("crash." + placedName + ":" + t.getClass().getSimpleName());
                }
            }
        }
    }

    /** 原版 applyCarvers（17×17 邻域与 seed+index 由 VanillaCarvers 内部处理）。
     *  诊断开关 -Dcharun.carvers=false 可关闭旧式雕刻器(分离噪声洞穴对照实验用)。 */
    public static final boolean CARVERS_ENABLED =
        Boolean.parseBoolean(System.getProperty("charun.carvers", "true"));

    public static void carveOverworld(com.CharunCore.server.worldgen.WorldGenLevel level,
                                      int chunkX, int chunkZ, long levelSeed) {
        if (!CARVERS_ENABLED) return;
        for (String id : new String[]{"cave", "cave_extra_underground", "canyon"}) {
            try {
                com.CharunCore.server.worldgen.feature.vanilla.VanillaCarvers
                    .carveChunk(id, levelSeed, chunkX, chunkZ, level);
            } catch (Throwable t) {
                FeaturePipeline.unsupported("carver_crash." + id + ":" + t.getClass().getSimpleName());
            }
        }
    }
}
