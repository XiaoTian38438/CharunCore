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
        // 原版 ShiftNoise.compute(d,d2,d3) = offset.getValue(d*0.25, d2*0.25, d3*0.25) * 4
        // ShiftA: compute(blockX, 0, blockZ)；ShiftB: compute(blockZ, blockX, 0)
        // —— B 把交换坐标放进 X/Y 槽（Z=0），不是 X/Z 槽，否则采样的是噪声的另一切片。
        int bx = ctx.blockX();
        int bz = ctx.blockZ();
        if (swapCoordOrder) {
            return noise.getValue(bz * 0.25, bx * 0.25, 0.0) * 4.0;
        }
        return noise.getValue(bx * 0.25, 0.0, bz * 0.25) * 4.0;
    }
}
