package com.CharunCore.server.worldgen;

import java.util.Map;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;
import com.CharunCore.server.worldgen.density.functions.*;

public final class PreciseDiagnostic {
    public static void main(String[] args) {
        NoiseHolder.setWorldSeed(1234567L, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);

        // Get the base3DNoise directly
        DensityFunction base3d = map.get("minecraft:overworld/base_3d_noise");
        DensityFunction depth = map.get("minecraft:overworld/depth");
        DensityFunction factor = map.get("minecraft:overworld/factor");
        DensityFunction offset = map.get("minecraft:overworld/offset");
        DensityFunction slopedCheese = map.get("minecraft:overworld/sloped_cheese");
        DensityFunction entrances = map.get("minecraft:overworld/caves/entrances");

        int bx = 0, bz = 0;
        System.out.println("=== Precise breakdown at (0, 0) ===");
        System.out.println(" y\t slopedCheese\t depth\t factor\t offset\t base3d\t entrances\t finalDensity");

        // Focus on the transition zone
        int[] yVals = {-64, -16, 0, 48, 56, 64, 68, 72, 80, 128, 192, 208, 248, 256, 264, 320};
        for (int y : yVals) {
            SinglePointContext ctx = new SinglePointContext(bx, y, bz);
            double sc = slopedCheese.compute(ctx);
            double d = depth.compute(ctx);
            double f = factor.compute(ctx);
            double o = offset.compute(ctx);
            double b = base3d.compute(ctx);
            double e = entrances.compute(ctx);
            double fd = router.finalDensity().compute(ctx);

            // Determine which rangeChoice branch
            String branch;
            if (sc >= -1000000 && sc < 1.5625) {
                double df14 = Math.min(sc, 5.0 * e);
                branch = "df14(min=" + String.format("%.2f", df14) + ")";
            } else {
                branch = "underground";
            }

            System.out.printf("%3d\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f  [%s]%n",
                y, sc, d, f, o, b, e, fd, branch);
        }
    }
}
