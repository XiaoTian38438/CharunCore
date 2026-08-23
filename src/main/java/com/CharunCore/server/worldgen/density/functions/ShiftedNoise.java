package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 原版 DensityFunctions$ShiftedNoise — 跟原版有3个 shift 分量（shiftX, shiftY, shiftZ），但 overworld 实际
 * 仅用 2D 版（shiftX, shiftZ, 0 shiftY），NoiseRouterData 用 shiftedNoise2d 工厂。
 * 我们提供 3-arg 版以兼容 Spline 等其他用法。
 */
public final class ShiftedNoise implements DensityFunction {
    private final DensityFunction shiftX, shiftY, shiftZ;
    private final double xzScale, yScale;
    private final DensityFunction.NoiseHolder noise;
    private final double minValue, maxValue;

    public ShiftedNoise(DensityFunction shiftX, DensityFunction shiftY, DensityFunction shiftZ,
                        double xzScale, double yScale, DensityFunction.NoiseHolder noise) {
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.shiftZ = shiftZ;
        this.xzScale = xzScale;
        this.yScale = yScale;
        this.noise = noise;
        double mv = noise.maxValue();
        this.minValue = -mv; this.maxValue = mv;
    }

    public DensityFunction shiftX() { return shiftX; }
    public DensityFunction shiftY() { return shiftY; }
    public DensityFunction shiftZ() { return shiftZ; }
    public double xzScale() { return xzScale; }
    public double yScale() { return yScale; }
    public DensityFunction.NoiseHolder noise() { return noise; }

    @Override public double compute(FunctionContext ctx) {
        double x = ctx.blockX() * xzScale + shiftX.compute(ctx);
        double y = ctx.blockY() * yScale + shiftY.compute(ctx);
        double z = ctx.blockZ() * xzScale + shiftZ.compute(ctx);
        return noise.getValue(x, y, z);
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override public ShiftedNoise mapAll(Visitor visitor) {
        return new ShiftedNoise(shiftX.mapAll(visitor), shiftY.mapAll(visitor), shiftZ.mapAll(visitor),
                                xzScale, yScale, noise);
    }

    @Override public double minValue() { return minValue; }
    @Override public double maxValue() { return maxValue; }
}
