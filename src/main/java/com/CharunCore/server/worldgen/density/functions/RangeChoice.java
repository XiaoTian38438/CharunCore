package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 原版 DensityFunctions$RangeChoice — 根据 input.compute 是否落在 [min, max] 范围选择 inside/outside 子树。
 */
public final class RangeChoice implements DensityFunction {
    private final DensityFunction input;
    private final double minInclusive;
    private final double maxExclusive;
    private final DensityFunction whenInRange;
    private final DensityFunction whenOutOfRange;

    public RangeChoice(DensityFunction input, double minInclusive, double maxExclusive,
                       DensityFunction whenInRange, DensityFunction whenOutOfRange) {
        this.input = input;
        this.minInclusive = minInclusive;
        this.maxExclusive = maxExclusive;
        this.whenInRange = whenInRange;
        this.whenOutOfRange = whenOutOfRange;
    }

    @Override public double compute(FunctionContext ctx) {
        double v = input.compute(ctx);
        return (v >= minInclusive && v < maxExclusive) ? whenInRange.compute(ctx) : whenOutOfRange.compute(ctx);
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        double[] inp = new double[array.length];
        input.fillArray(inp, provider);
        for (int i = 0; i < array.length; i++) {
            array[i] = (inp[i] >= minInclusive && inp[i] < maxExclusive)
                    ? whenInRange.compute(provider.forIndex(i)) : whenOutOfRange.compute(provider.forIndex(i));
        }
    }

    @Override public RangeChoice mapAll(Visitor visitor) {
        return new RangeChoice(input.mapAll(visitor), minInclusive, maxExclusive,
                whenInRange.mapAll(visitor), whenOutOfRange.mapAll(visitor));
    }

    @Override public double minValue() { return Math.min(whenInRange.minValue(), whenOutOfRange.minValue()); }
    @Override public double maxValue() { return Math.max(whenInRange.maxValue(), whenOutOfRange.maxValue()); }

    public DensityFunction input() { return input; }
    public double minInclusive() { return minInclusive; }
    public double maxExclusive() { return maxExclusive; }
    public DensityFunction whenInRange() { return whenInRange; }
    public DensityFunction whenOutOfRange() { return whenOutOfRange; }
}
