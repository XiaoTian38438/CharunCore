package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/** 原版 DensityFunctions.Constant */
public final class Constant implements DensityFunction {
    public static final Constant ZERO = new Constant(0.0, 0.0, 0.0);
    public static final Constant ONE = new Constant(1.0, 0.0, 1.0);

    private final double value;
    private final double minValue;
    private final double maxValue;

    public Constant(double value) {
        this(value, value, value);
    }

    public Constant(double value, double minValue, double maxValue) {
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public double value() { return value; }

    @Override public double compute(FunctionContext ctx) { return value; }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        java.util.Arrays.fill(array, value);
    }

    @Override public DensityFunction mapAll(Visitor visitor) { return visitor.apply(this); }

    @Override public double minValue() { return minValue; }
    @Override public double maxValue() { return maxValue; }
}
