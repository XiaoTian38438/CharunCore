package com.CharunCore.server.worldgen;

import java.util.Map;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;
import com.CharunCore.server.worldgen.density.NoiseRouterData;

public final class ShiftDiagnostic {
    public static final String SHIFT_X = "minecraft:shift_x";
    public static final String SHIFT_Z = "minecraft:shift_z";

    public static void main(String[] args) {
        NoiseHolder.setWorldSeed(1234567L, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();

        DensityFunction shiftX = map.get(SHIFT_X);
        DensityFunction shiftZ = map.get(SHIFT_Z);

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                SinglePointContext ctx = new SinglePointContext(x, 0, z);
                double sx = shiftX.compute(ctx);
                double sz = shiftZ.compute(ctx);
                System.out.printf("(%d,%d): shiftX=%.4f shiftZ=%.4f%n", x, z, sx, sz);
            }
        }

        // Now check offset noise values at shifted positions
        DensityFunction cont = map.get("minecraft:overworld/continents");
        DensityFunction ero = map.get("minecraft:overworld/erosion");
        DensityFunction ridges = map.get("minecraft:overworld/ridges");
        DensityFunction rFolded = map.get("minecraft:overworld/ridges_folded");

        System.out.println("\nClimate at nearby positions:");
        for (int x = -4; x <= 4; x+=4) {
            for (int z = -4; z <= 4; z+=4) {
                SinglePointContext ctx = new SinglePointContext(x, 0, z);
                double c = cont.compute(ctx);
                double e = ero.compute(ctx);
                double r = ridges.compute(ctx);
                double rf = rFolded.compute(ctx);
                System.out.printf("(%3d,%3d): cont=%+.6f ero=%+.6f ridges=%+.4f rFolded=%+.4f%n",
                    x, z, c, e, r, rf);
            }
        }
    }
}
