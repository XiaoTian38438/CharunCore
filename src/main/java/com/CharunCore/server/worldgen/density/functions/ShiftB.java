package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/** 原版 DensityFunctions$ShiftB — (blockZ * 0.25, 0, blockX * 0.25) * 4（X 和 Z 坐标交换） */
public final class ShiftB implements DensityFunction {
    private final DensityFunction.NoiseHolder offsetNoise;
    private final double minValue, maxValue;

    public ShiftB(DensityFunction.NoiseHolder offsetNoise) {
        this.offsetNoise = offsetNoise;
        double mv = offsetNoise.maxValue() * 4.0;
        this.minValue = -mv; this.maxValue = mv;
    }

    public DensityFunction.NoiseHolder offsetNoise() { return offsetNoise; }

    @Override public double compute(FunctionContext ctx) {
        return ShiftNoise.compute(offsetNoise, ctx, true);
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override public ShiftB mapAll(Visitor visitor) { return this; }

    @Override public double minValue() { return minValue; }
    @Override public double maxValue() { return maxValue; }
}
