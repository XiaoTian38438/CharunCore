package com.CharunCore.server.worldgen;

import java.util.Map;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;

/**
 * Debug floating islands: compare chunk blocks vs direct density at (8,8).
 */
public final class FloatingIslandDebug {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;

        // Generate chunk
        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(seed);
        Chunk chunk = gen.generate(0, 0);
        int stoneId = BlockStateHelper.getDefault("stone");

        // Direct density router
        NoiseHolder.setWorldSeed(seed, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);
        DensityFunction finalDensity = router.finalDensity();
        DensityFunction slopedCheese = map.get(NoiseRouterData.SLOPED_CHEESE);
        DensityFunction entrances = map.get(NoiseRouterData.ENTRANCES);
        DensityFunction offsetSpline = map.get(NoiseRouterData.OFFSET);
        DensityFunction factorSpline = map.get(NoiseRouterData.FACTOR);
        DensityFunction depth = map.get(NoiseRouterData.DEPTH);
        DensityFunction continents = router.continents();
        DensityFunction erosion = router.erosion();
        DensityFunction ridgesFolded = map.get(NoiseRouterData.RIDGES_FOLDED);
        DensityFunction base3d = map.get(NoiseRouterData.BASE_3D_NOISE_OVERWORLD);
        DensityFunction noodle = map.get(NoiseRouterData.NOODLE);

        int bx = 8, bz = 8; // block (8,8) in chunk (0,0)

        System.out.println("=== Floating island diagnosis at (" + bx + ", y, " + bz + ") ===");
        System.out.println(String.format("%4s %6s %10s %10s %10s %10s %10s %10s %10s %10s %10s",
            "y", "blk", "finalDens", "slopedCh", "entrance", "base3d", 
            "offset", "factor", "depth", "cont", "ero"));

        for (int y = -64; y <= 320; y++) {
            int blk = chunk.getBlock(bx, y, bz);
            double df = finalDensity.compute(new SinglePointContext(bx, y, bz));
            double sc = slopedCheese.compute(new SinglePointContext(bx, y, bz));
            double ent = entrances.compute(new SinglePointContext(bx, y, bz));
            double b3 = base3d.compute(new SinglePointContext(bx, y, bz));
            double off = offsetSpline.compute(new SinglePointContext(bx, y, bz));
            double fac = factorSpline.compute(new SinglePointContext(bx, y, bz));
            double dep = depth.compute(new SinglePointContext(bx, y, bz));
            double cont = continents.compute(new SinglePointContext(bx, y, bz));
            double ero = erosion.compute(new SinglePointContext(bx, y, bz));

            // Print interesting rows only (non-air or threshold crossing)
            boolean print = (blk != 0) || 
                (y >= -4 && y <= 44) ||
                (y >= -28 && y <= -12) ||
                (y >= 60 && y <= 76) ||
                (df > -0.01 && df < 0.01 && df != 0) ||
                (y % 16 == 0);

            if (print) {
                String blkName = blk == 0 ? "a" : 
                    (blk == stoneId ? "S" : BlockStateHelper.getName(blk));
                System.out.println(String.format("%4d %6s %10.6f %10.6f %10.6f %10.6f %10.6f %10.6f %10.6f %10.6f %10.6f",
                    y, blkName, df, sc, ent, b3, off, fac, dep, cont, ero));
            }
        }
    }
}
