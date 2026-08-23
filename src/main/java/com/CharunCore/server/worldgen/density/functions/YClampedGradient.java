package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/** 原版 DensityFunctions$YClampedGradient — 把 y 坐标线性映射为密度 */
public final class YClampedGradient implements DensityFunction {
    private final int fromY, toY;
    private final double fromValue, toValue;
    private final double min, max;

    public YClampedGradient(int fromY, int toY, double fromValue, double toValue) {
        this.fromY = fromY;
        this.toY = toY;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.min = Math.min(fromValue, toValue);
        this.max = Math.max(fromValue, toValue);
    }

    @Override public double compute(FunctionContext ctx) {
        int y = ctx.blockY();
        if (y <= fromY) return fromValue;
        if (y >= toY) return toValue;
        return fromValue + (double)(y - fromY) / (double)(toY - fromY) * (toValue - fromValue);
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override public DensityFunction mapAll(Visitor visitor) { return visitor.apply(this); }

    @Override public double minValue() { return min; }
    @Override public double maxValue() { return max; }
}
