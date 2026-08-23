package com.CharunCore.server.worldgen;

import java.util.Map;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;

public final class TraceFunctionChain {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;

        // Direct density router
        NoiseHolder.setWorldSeed(seed, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);
        DensityFunction finalDensity = router.finalDensity();
        DensityFunction noodle = map.get(NoiseRouterData.NOODLE);

        int bx = 8, bz = 8;

        System.out.println("=== Direct density at mystery blocks (y=200-220) ===");
        System.out.println(String.format("%4s %12s %12s", "y", "finalDens", "noodle"));
        for (int y = 180; y <= 250; y++) {
            double df = finalDensity.compute(new SinglePointContext(bx, y, bz));
            double nd = noodle.compute(new SinglePointContext(bx, y, bz));
            boolean interesting = df > -0.9 || y == 214 || y == 202 || y == 203 || y == 204;
            if (interesting) {
                System.out.println(String.format("%4d %12.6f %12.6f", y, df, nd));
            }
        }

        // Also check cell corners around mystery zone
        System.out.println("\n=== Cell corners (every 8 blocks) near y=200-220 ===");
        for (int y = 184; y <= 248; y += 8) {
            double df = finalDensity.compute(new SinglePointContext(bx, y, bz));
            System.out.println(String.format("y=%3d finalDens=%12.6f", y, df));
        }
    }
}
