package com.CharunCore.server.worldgen;

import java.util.Map;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.NoiseRouter;

public final class TerrainHeightScan {
    public static void main(String[] args) {
        NoiseHolder.setWorldSeed(1234567L, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);
        DensityFunction finalDensity = router.finalDensity();

        // Check for high-altitude terrain (bug: surface at y=200+)
        int highCount = 0, totalCount = 0;
        for (int x = -32; x <= 32; x += 8) {
            for (int z = -32; z <= 32; z += 8) {
                totalCount++;
                boolean highTerrain = false;
                for (int y = 256; y >= 120; y--) {
                    double d = finalDensity.compute(new SinglePointContext(x, y, z));
                    if (d > 0) {
                        highTerrain = true;
                        highCount++;
                        break;
                    }
                }
            }
        }
        System.out.printf("High terrain (y>=120): %d/%d positions%n", highCount, totalCount);

        // Normal surface scan (y=64 to y=120)
        for (int x = -32; x <= 32; x += 16) {
            for (int z = -32; z <= 32; z += 16) {
                int surfaceY = 120;
                for (int y = 120; y >= 64; y--) {
                    double d = finalDensity.compute(new SinglePointContext(x, y, z));
                    if (d > 0) {
                        surfaceY = y;
                        break;
                    }
                }
                if (surfaceY < 120) {
                    System.out.printf("(%3d,%3d): surface at y=%d%n", x, z, surfaceY);
                } else {
                    double d = finalDensity.compute(new SinglePointContext(x, 64, z));
                    System.out.printf("(%3d,%3d): no surface (y=64->%.2f)%n", x, z, d);
                }
            }
        }
        
        // Also check density profile at (0,0)
        System.out.printf("%nDensity profile at (0,0):%n");
        for (int y = 256; y >= 64; y -= 16) {
            double d = finalDensity.compute(new SinglePointContext(0, y, 0));
            System.out.printf("  y=%d: %.4f%n", y, d);
        }
    }
}
