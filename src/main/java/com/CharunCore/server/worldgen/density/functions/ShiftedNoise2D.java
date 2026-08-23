package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.world.gen.NormalNoise;
import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 原版 DensityFunctions.shiftedNoise2d 工厂产生的形态。
 * compute = noise.getValue(x * xzScale + shiftX, 0, z * xzScale + shiftZ)
 * shiftY 固定为 0（这是 2D shift 的特征）。
 */
public final class ShiftedNoise2D implements DensityFunction {
    private final DensityFunction shiftX, shiftZ;
    private final double xzScale;
    private final DensityFunction.NoiseHolder noise;
    private final double minValue, maxValue;

    public ShiftedNoise2D(DensityFunction shiftX, DensityFunction shiftZ, double xzScale,
                          NormalNoise.NoiseParameters params) {
        this(shiftX, shiftZ, xzScale, new DensityFunction.NoiseHolder("", params));
    }

    public ShiftedNoise2D(DensityFunction shiftX, DensityFunction shiftZ, double xzScale,
                         DensityFunction.NoiseHolder noise) {
        this.shiftX = shiftX;
        this.shiftZ = shiftZ;
        this.xzScale = xzScale;
        this.noise = noise;
        double mv = noise.maxValue();
        this.minValue = -mv; this.maxValue = mv;
    }

    public DensityFunction shiftX() { return shiftX; }
    public DensityFunction shiftZ() { return shiftZ; }
    public double xzScale() { return xzScale; }
    public DensityFunction.NoiseHolder noise() { return noise; }

    @Override public double compute(FunctionContext ctx) {
        double x = ctx.blockX() * xzScale + shiftX.compute(ctx);
        double z = ctx.blockZ() * xzScale + shiftZ.compute(ctx);
        return noise.getValue(x, 0.0, z);
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override public ShiftedNoise2D mapAll(Visitor visitor) {
        return new ShiftedNoise2D(shiftX.mapAll(visitor), shiftZ.mapAll(visitor), xzScale, noise);
    }

    @Override public double minValue() { return minValue; }
    @Override public double maxValue() { return maxValue; }
}
