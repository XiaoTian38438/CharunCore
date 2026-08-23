package com.CharunCore.server.worldgen;

import java.util.Map;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;

public final class DensityDiagnostic {
    public static void main(String[] args) {
        NoiseHolder.setWorldSeed(1234567L, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);

        DensityFunction slopedCheese = map.get("minecraft:overworld/sloped_cheese");
        DensityFunction offset = map.get("minecraft:overworld/offset");
        DensityFunction factor = map.get("minecraft:overworld/factor");
        DensityFunction depth = map.get("minecraft:overworld/depth");

        if (slopedCheese == null) { System.out.println("ERROR: sloped_cheese not found"); return; }

        int[] xzPairs = {0, 0, 8, 8, 8*16+8, 8*16+8};
        for (int pi = 0; pi < xzPairs.length; pi += 2) {
            int bx = xzPairs[pi], bz = xzPairs[pi+1];
            System.out.println("\n=== Column (" + bx + ", " + bz + ") ===");
            System.out.println(" y\tcont\t erosion\t ridges\t offset\t factor\t depth\t slopedCheese\t finalDensity");
            for (int y = -64; y <= 320; y += 8) {
                SinglePointContext ctx = new SinglePointContext(bx, y, bz);
                double c = router.continents().compute(ctx);
                double e = router.erosion().compute(ctx);
                double r = router.ridges().compute(ctx);
                double o = offset.compute(ctx);
                double f = factor.compute(ctx);
                double d = depth.compute(ctx);
                double sc = slopedCheese.compute(ctx);
                double fd = router.finalDensity().compute(ctx);
                System.out.printf("%3d\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f%n",
                    y, c, e, r, o, f, d, sc, fd);
            }
        }

        // Also scan more XZ positions to find where terrain is normal
        System.out.println("\n=== Surface scan at various XZ (y range with finalDensity crossing zero) ===");
        for (int bx = 0; bx <= 256; bx += 16) {
            for (int bz = 0; bz <= 256; bz += 16) {
                int surfaceY = -64;
                for (int y = -64; y < 320; y++) {
                    double d = router.finalDensity().compute(new SinglePointContext(bx, y, bz));
                    double d1 = router.finalDensity().compute(new SinglePointContext(bx, y+1, bz));
                    if (d > 0 && d1 <= 0) { surfaceY = y; break; }
                }
                double cont = router.continents().compute(new SinglePointContext(bx, 0, bz));
                double ero = router.erosion().compute(new SinglePointContext(bx, 0, bz));
                if (surfaceY > -64) {
                    System.out.printf("(%3d,%3d) surfaceY=%3d  cont=%+.4f erosion=%+.4f%n", bx, bz, surfaceY, cont, ero);
                }
            }
        }
    }
}
