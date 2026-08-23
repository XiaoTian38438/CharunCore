package com.CharunCore.server.worldgen;

import java.util.Map;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;
import com.CharunCore.server.worldgen.density.functions.*;

public final class TraceDiagnostic {
    public static void main(String[] args) {
        NoiseHolder.setWorldSeed(1234567L, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);

        DensityFunction slopedCheese = map.get("minecraft:overworld/sloped_cheese");
        DensityFunction entrances = map.get("minecraft:overworld/caves/entrances");
        DensityFunction noodle = map.get("minecraft:overworld/caves/noodle");
        DensityFunction base3d = map.get("minecraft:overworld/base_3d_noise");

        // Compute at transition points
        int[][] points = {
            {0, 64, 0},    // just before transition
            {0, 68, 0},    // just after transition (df14)
            {0, 256, 0},   // high up (should be -0.0073)
        };

        for (int[] p : points) {
            int x = p[0], y = p[1], z = p[2];
            SinglePointContext ctx = new SinglePointContext(x, y, z);
            double sc = slopedCheese.compute(ctx);
            double en = entrances.compute(ctx);
            double nd = noodle.compute(ctx);
            double b3 = base3d.compute(ctx);

            // RangeChoice evaluation
            boolean useDF14 = (sc >= -1000000.0 && sc < 1.5625);
            double rangeResult = useDF14 ? Math.min(sc, 5.0 * en) : -999; // underground placeholder
            String branch = useDF14 ? "df14" : "underground";

            // Compute what slide would produce (approximate)
            double bottomSlide = 0.1171875;
            double topSlideVal = -0.078125;
            double yClampBot = Math.max(0, Math.min(1, (double)(y + 64) / 24.0));
            double yClampTop = Math.max(0, Math.min(1, (double)(320 - y) / 80.0));
            double slid = rangeResult == -999 ? rangeResult : rangeResult;
            if (useDF14) {
                slid = rangeResult;
                // top slide: lerp(clampTop, -0.078125, slid)
                slid = -0.078125 + yClampTop * (slid + 0.078125);
                // bottom slide: lerp(clampBot, 0.1171875, slid)
                slid = 0.1171875 + yClampBot * (slid - 0.1171875);
            }

            double fd = router.finalDensity().compute(ctx);

            System.out.printf("(%d,%d,%d): sc=%+.4f ent=%+.4f noodle=%+.4f base3d=%+.4f branch=%s rangeRes=%.4f fd=%+.4f%n",
                x, y, z, sc, en, nd, b3, branch, rangeResult, fd);
        }
    }
}
