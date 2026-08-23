package com.CharunCore.server.worldgen;

import java.util.Map;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.DensityFunctions;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;

public final class FinalCheckDiagnostic {
    public static void main(String[] args) {
        NoiseHolder.setWorldSeed(1234567L, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);

        // Build the equivalent of postProcess(slideOverworld(df15)) manually
        DensityFunction slopedCheese = map.get("minecraft:overworld/sloped_cheese");
        DensityFunction entrances = map.get("minecraft:overworld/caves/entrances");
        DensityFunction noodle = map.get("minecraft:overworld/caves/noodle");

        // Build the df14 equivalent
        DensityFunction df14 = DensityFunctions.min(slopedCheese,
            DensityFunctions.mul(DensityFunctions.constant(5.0), entrances));

        // Build rangeChoice equivalent  
        DensityFunction df15 = DensityFunctions.rangeChoice(slopedCheese,
            -1000000.0, 1.5625, df14,
            DensityFunctions.constant(999));

        // Build the slide + postProcess chain
        DensityFunction topGradient = DensityFunctions.yClampedGradient(-64+384-80, -64+384-0, 1.0, 0.0);
        DensityFunction slide1 = DensityFunctions.lerp(topGradient, -0.078125, df15);
        DensityFunction bottomGradient = DensityFunctions.yClampedGradient(-64+0, -64+24, 0.0, 1.0);
        DensityFunction slide2 = DensityFunctions.lerp(bottomGradient, 0.1171875, slide1);
        DensityFunction post = DensityFunctions.mul(DensityFunctions.interpolated(slide2), DensityFunctions.constant(0.64)).squeeze();
        DensityFunction finalSim = DensityFunctions.min(post, noodle);

        int bx = 0, bz = 0;
        System.out.println("=== Direct chain computation at (0, y, 0) ===");
        System.out.println(" y\t slopedCheese\t entrances\t df14\t df15\t post\t noodle\t finalSim\t realFd");
        int[] ys = {-64, 64, 68, 208, 240, 248, 256, 264, 280, 320};
        for (int y : ys) {
            SinglePointContext ctx = new SinglePointContext(bx, y, bz);
            double sc = slopedCheese.compute(ctx);
            double en = entrances.compute(ctx);
            double d14 = df14.compute(ctx);
            double d15 = df15.compute(ctx);
            double p = post.compute(ctx);
            double nd = noodle.compute(ctx);
            double fs = finalSim.compute(ctx);
            double rf = router.finalDensity().compute(ctx);
            System.out.printf("%3d\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f\t%+.4f%n",
                y, sc, en, d14, d15, p, nd, fs, rf);
        }
    }
}
