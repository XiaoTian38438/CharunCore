package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.DensityFunction.FunctionContext;

/**
 * 原版 DensityFunctions$ShiftA — 对常量 0.25 缩放后采样 OffsetNoise；
 * compute(ctx) = noise.getValue((blockX * 0.25), 0, (blockZ * 0.25)) * 4
 */
public abstract class ShiftNoise {
    private ShiftNoise() {}

    public static double compute(DensityFunction.NoiseHolder noise, FunctionContext ctx,
                                 boolean swapCoordOrder) {
        int bx = ctx.blockX();
        int bz = ctx.blockZ();
        double x, z;
        if (swapCoordOrder) {
            x = bz * 0.25;
            z = bx * 0.25;
        } else {
            x = bx * 0.25;
            z = bz * 0.25;
        }
        return noise.getValue(x, 0.0, z) * 4.0;
    }
}
