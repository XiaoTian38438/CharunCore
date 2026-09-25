package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.gen.NoiseParameters;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;
import com.CharunCore.server.worldgen.density.functions.BlendedNoiseAsDF;
import com.CharunCore.server.worldgen.density.functions.EndIslandDensityFunction;

import java.util.HashMap;
import java.util.Map;

/** 快速探针：nether 群系大范围分布 + end 密度成分拆解。 */
public final class ProbeDiag {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;

        // ── 1) nether 群系大范围采样（无需生成区块）
        DensityRouterChunkGenerator netGen = new DensityRouterChunkGenerator(seed, DimensionType.THE_NETHER);
        Map<Integer, Integer> dist = new HashMap<>();
        String[] names = {"?", "?", "basalt_deltas", "?", "?", "?", "?", "crimson_forest"};
        for (int x = -3000; x <= 3000; x += 128) {
            for (int z = -3000; z <= 3000; z += 128) {
                int b = netGen.getColumnBiome(x, z);
                dist.merge(b, 1, Integer::sum);
            }
        }
        System.out.println("nether biome scan (±3000, step 128): " + dist);

        // ── 2) end 密度成分
        DensityFunction.NoiseHolder.setWorldSeed(seed, 0L);
        EndIslandDensityFunction islands = new EndIslandDensityFunction(0L);
        BlendedNoiseAsDF base3d = new BlendedNoiseAsDF(
            "minecraft:end/base_3d_noise", 0.25, 0.25, 80.0, 160.0, 4.0);

        System.out.println();
        System.out.println("end components at chunk(80,80) center col (solid terrain reported there):");
        int bx = 80 * 16 + 8, bz = 80 * 16 + 8;
        double isl = islands.compute(new SinglePointContext(bx, 57, bz));
        System.out.printf("  endIslands = %.4f%n", isl);
        double minB = Double.MAX_VALUE, maxB = -Double.MAX_VALUE, sumB = 0;
        int n = 0;
        for (int y = 30; y <= 100; y++) {
            double v = base3d.compute(new SinglePointContext(bx, y, bz));
            minB = Math.min(minB, v); maxB = Math.max(maxB, v); sumB += v; n++;
        }
        System.out.printf("  base3d y=30..100: min=%.4f max=%.4f avg=%.4f%n", minB, maxB, sumB / n);

        System.out.println();
        System.out.println("islands value scan along +X (z=8), y ignored:");
        for (int x = 0; x <= 24000; x += 800) {
            double v = islands.compute(new SinglePointContext(x + 8, 0, 8));
            System.out.printf("  x=%6d islands=%7.4f -> heightF=%7.1f%n", x, v, v * 128.0 + 8.0);
        }
    }
}
