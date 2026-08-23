package com.CharunCore.server.worldgen;

import java.util.Map;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.DensityFunctions;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;
import com.CharunCore.server.worldgen.density.functions.*;

public final class FineScanDiagnostic {
    public static void main(String[] args) {
        NoiseHolder.setWorldSeed(1234567L, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);

        DensityFunction slopedCheese = map.get("minecraft:overworld/sloped_cheese");
        DensityFunction entrances = map.get("minecraft:overworld/caves/entrances");
        DensityFunction noodle = map.get("minecraft:overworld/caves/noodle");
        DensityFunction offset = map.get("minecraft:overworld/offset");

        DensityFunction df14 = DensityFunctions.min(slopedCheese,
            DensityFunctions.mul(DensityFunctions.constant(5.0), entrances));
        DensityFunction df15 = DensityFunctions.rangeChoice(slopedCheese,
            -1000000.0, 1.5625, df14,
            DensityFunctions.constant(999));

        int bx = 0, bz = 0;
        System.out.println("=== Fine scan at (0, y, 0) y=200 to 320 ===");
        System.out.println(" y\t sc\t entrance\t df14\t df15\t realFd");
        for (int y = 200; y <= 320; y++) {
            SinglePointContext ctx = new SinglePointContext(bx, y, bz);
            double sc = slopedCheese.compute(ctx);
            double en = entrances.compute(ctx);
            double d14 = df14.compute(ctx);
            double d15 = df15.compute(ctx);
            double rf = router.finalDensity().compute(ctx);
            System.out.printf("%3d\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f%n",
                y, sc, en, d14, d15, rf);
        }
    }
}
