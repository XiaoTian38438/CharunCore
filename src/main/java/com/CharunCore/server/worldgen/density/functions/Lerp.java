package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/** Lerp — 3 输入算术：from + factor * (to - from)，clamp factor 到 [0,1] */
public final class Lerp implements DensityFunction {
    private final DensityFunction factor, from, to;
    private final double minV, maxV;

    public Lerp(DensityFunction factor, DensityFunction from, DensityFunction to) {
        this.factor = factor;
        this.from = from;
        this.to = to;
        this.minV = Math.min(from.minValue(), to.minValue());
        this.maxV = Math.max(from.maxValue(), to.maxValue());
    }

    public DensityFunction factor() { return factor; }
    public DensityFunction from() { return from; }
    public DensityFunction to() { return to; }

    @Override public double compute(FunctionContext ctx) {
        double f = factor.compute(ctx);
        if (f <= 0.0) return from.compute(ctx);
        if (f >= 1.0) return to.compute(ctx);
        double a = from.compute(ctx);
        double b = to.compute(ctx);
        return a + f * (b - a);
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override public Lerp mapAll(Visitor visitor) {
        return new Lerp(factor.mapAll(visitor), from.mapAll(visitor), to.mapAll(visitor));
    }

    @Override public double minValue() { return minV; }
    @Override public double maxValue() { return maxV; }
}
