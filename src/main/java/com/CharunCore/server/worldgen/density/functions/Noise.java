package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 原版 DensityFunctions$Noise — 噪声密度函数。
 * 取 NormalNoise 实例（经 NoiseHolder 包装）按 (xzScale, yScale) 缩放坐标后求值。
 * 原版 compute = noise.getValue(x * xzScale, y * yScale, z * xzScale) * inputFactor + offsetFactor
 * 其中 inputFactor=1.0, offsetFactor=0.0（默认版本；MappedNoise 才是自定义的）。
 */
public final class Noise implements DensityFunction {
    private final DensityFunction.NoiseHolder noise;
    private final double xzScale;
    private final double yScale;
    private final double minValue, maxValue;

    public Noise(DensityFunction.NoiseHolder noise, double xzScale, double yScale) {
        this.noise = noise;
        this.xzScale = xzScale;
        this.yScale = yScale;
        double mv = noise.maxValue();
        this.minValue = -mv;
        this.maxValue =  mv;
    }

    public DensityFunction.NoiseHolder noise() { return noise; }
    public double xzScale() { return xzScale; }
    public double yScale() { return yScale; }

    @Override public double compute(FunctionContext ctx) {
        double x = ctx.blockX() * xzScale;
        double y = ctx.blockY() * yScale;
        double z = ctx.blockZ() * xzScale;
        return noise.getValue(x, y, z);
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override public Noise mapAll(Visitor visitor) {
        return new Noise(noise, xzScale, yScale);
    }

    @Override public double minValue() { return minValue; }
    @Override public double maxValue() { return maxValue; }
}
