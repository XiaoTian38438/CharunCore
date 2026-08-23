package com.CharunCore.server.worldgen;

import java.util.Map;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;

public final class UndergroundDiagnostic {
    public static void main(String[] args) {
        NoiseHolder.setWorldSeed(1234567L, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);

        String[] keys = {
            "minecraft:overworld/sloped_cheese",
            "minecraft:overworld/caves/spaghetti_roughness_function",
            "minecraft:overworld/caves/spaghetti_2d",
            "minecraft:overworld/caves/entrances",
            "minecraft:overworld/caves/noodle",
            "minecraft:overworld/caves/pillars",
        };
        DensityFunction[] funcs = new DensityFunction[keys.length];
        for (int i = 0; i < keys.length; i++) {
            funcs[i] = map.get(keys[i]);
            if (funcs[i] == null) System.out.println("MISSING: " + keys[i]);
        }

        int[] xzPairs = {0, 0, 136, 136};
        for (int pi = 0; pi < xzPairs.length; pi += 2) {
            int bx = xzPairs[pi], bz = xzPairs[pi+1];
            System.out.println("\n=== Underground components at (" + bx + ", " + bz + ") ===");
            System.out.println(" y\t slopedCheese\t roughness\t spaghetti2d\t entrances\t noodle\t\t pillars\t finalDensity");
            for (int y = -64; y <= 320; y += 8) {
                SinglePointContext ctx = new SinglePointContext(bx, y, bz);
                double sc = funcs[0].compute(ctx);
                double rough = funcs[1].compute(ctx);
                double sp2d = funcs[2].compute(ctx);
                double entr = funcs[3].compute(ctx);
                double nood = funcs[4].compute(ctx);
                double pill = funcs[5].compute(ctx);
                double fd = router.finalDensity().compute(ctx);
                System.out.printf("%3d\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f%n",
                    y, sc, rough, sp2d, entr, nood, pill, fd);
            }
        }
    }
}
