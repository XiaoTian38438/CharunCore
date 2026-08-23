package com.CharunCore.server.worldgen;

import java.util.Map;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.DensityFunctions;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;
import com.CharunCore.server.world.gen.CubicSpline;
import com.CharunCore.server.world.gen.TerrainProvider;

public final class OffsetDiagnostic {
    public static final String OFFSET = "minecraft:overworld/offset";
    public static final String FACTOR = "minecraft:overworld/factor";
    public static final String CONTINENTALNESS = "minecraft:overworld/continents";
    public static final String EROSION = "minecraft:overworld/erosion";
    public static final String RIDGES = "minecraft:overworld/ridges";
    public static final String RIDGES_FOLDED = "minecraft:overworld/ridges_folded";
    public static final String DEPTH = "minecraft:overworld/depth";
    public static final String SLOPED_CHEESE = "minecraft:overworld/sloped_cheese";

    public static void main(String[] args) {
        NoiseHolder.setWorldSeed(1234567L, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);

        int bx = 0, bz = 0;

        // Get climate values (2D — same at any y)
        SinglePointContext climateCtx = new SinglePointContext(bx, 0, bz);
        double cont = map.get(CONTINENTALNESS).compute(climateCtx);
        double ero = map.get(EROSION).compute(climateCtx);
        double ridges = map.get(RIDGES).compute(climateCtx);
        double rFolded = map.get(RIDGES_FOLDED).compute(climateCtx);
        System.out.printf("Climate: cont=%.6f ero=%.6f ridges=%.6f rFolded=%.6f%n", cont, ero, ridges, rFolded);

        // Compute offset spline directly
        CubicSpline offsetSpline = TerrainProvider.overworldOffset(
            TerrainProvider.coordinate(0), TerrainProvider.coordinate(1),
            TerrainProvider.coordinate(3), false);
        float[] climateArr = {(float)cont, (float)ero, (float)ridges, (float)rFolded};
        float rawOffsetSpline = offsetSpline.apply(climateArr);
        System.out.printf("Raw offsetSpline output: %.6f%n", rawOffsetSpline);
        System.out.printf("Expected: -0.50375 + %.6f = %.6f%n",
            rawOffsetSpline, -0.50375 + rawOffsetSpline);

        // Compute factor spline directly
        DensityFunction factorFunc = map.get(FACTOR);
        double actualFactor = factorFunc.compute(climateCtx);
        System.out.printf("Factor (from map): %.6f%n", actualFactor);

        // Compute offset from map at various y values
        DensityFunction offsetFunc = map.get(OFFSET);
        DensityFunction depthFunc = map.get(DEPTH);
        DensityFunction scFunc = map.get(SLOPED_CHEESE);

        System.out.println("\ny\t\tslopedCheese\toffset\t\tdepth\t\toffsetSpline(rFolded)");
        for (int y = 60; y <= 140; y++) {
            SinglePointContext ctx = new SinglePointContext(bx, y, bz);
            double sc = scFunc.compute(ctx);
            double off = offsetFunc.compute(ctx);
            double dep = depthFunc.compute(ctx);
            double rFolded_val = map.get(RIDGES_FOLDED).compute(ctx);
            // offsetSpline from climate only (2D, same at all y)
            float rF = (float)rFolded_val;
            float[] cl = {(float)cont, (float)ero, (float)ridges, rF};
            double osVal = offsetSpline.apply(cl);
            System.out.printf("%d\t\t%+.6f\t%+.6f\t%+.6f\t%+.6f%n",
                y, sc, off, dep, osVal);
        }

        // Also test what the yClamp gives
        System.out.println("\nVerification of noiseGradient chain at various y:");
        DensityFunction yClampFunc = DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5);
        for (int y = 60; y <= 140; y += 10) {
            SinglePointContext ctx = new SinglePointContext(bx, y, bz);
            double sc = scFunc.compute(ctx);
            double off = offsetFunc.compute(ctx);
            double dep = depthFunc.compute(ctx);
            double yc = yClampFunc.compute(ctx);
            double base3d = sc - dep; // slopedCheese = noiseGradient + base3d, depth = noiseGradient
            System.out.printf("y=%d: yClamp=%.4f offset=%.4f depth=%.4f base3d≈%.4f sc=%.4f%n",
                y, yc, off, dep, base3d, sc);
        }
    }
}
