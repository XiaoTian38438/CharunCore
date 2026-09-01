package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/** 原版 DensityFunctions.Mapped — 单输入纯函数变换 */
public final class Mapped implements DensityFunction {
    public enum Type {
        ABS   (v -> Math.abs(v), -Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE),
        SQUARE(v -> v * v,        0.0, Double.MAX_VALUE,                               0.0, Double.MAX_VALUE),
        CUBE  (v -> v * v * v,   -Double.MAX_VALUE, Double.MAX_VALUE,                  -Double.MAX_VALUE, Double.MAX_VALUE),
        HALF_NEGATIVE(Mapped::halfNegative,  -Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE),
        QUARTER_NEGATIVE(Mapped::quarterNegative, -Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE),
        INVERT(v -> -v,          -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE),
        SQUEEZE (Mapped::squeeze, -1.0, 1.0, -1.0, 1.0);

        @FunctionalInterface
        public interface Transform { double apply(double v); }

        private final Transform transform;
        private final double minInput, maxInput;
        private final double minFactorForMin, minFactorForMax;

        Type(Transform t, double minIn, double maxIn, double fMin, double fMax) {
            this.transform = t;
            this.minInput = minIn; this.maxInput = maxIn;
            this.minFactorForMin = fMin; this.minFactorForMax = fMax;
        }
    }

    private final Type type;
    private final DensityFunction input;
    private final double minValue, maxValue;

    private Mapped(Type type, DensityFunction input, double minV, double maxV) {
        this.type = type;
        this.input = input;
        this.minValue = minV;
        this.maxValue = maxV;
    }

    public static Mapped map(DensityFunction input, Type type) {
        double inMin = input.minValue();
        double inMax = input.maxValue();
        double min = type.transform.apply(inMin);
        double max = type.transform.apply(inMax);
        return new Mapped(type, input, Math.min(min, max), Math.max(min, max));
    }

    public Type type() { return type; }
    public DensityFunction input() { return input; }

    @Override public double compute(FunctionContext ctx) {
        return type.transform.apply(input.compute(ctx));
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        input.fillArray(array, provider);
        for (int i = 0; i < array.length; i++) array[i] = type.transform.apply(array[i]);
    }

    @Override public Mapped mapAll(Visitor visitor) {
        return new Mapped(type, input.mapAll(visitor),
                type.transform.apply(input.minValue()),
                type.transform.apply(input.maxValue()));
    }

    @Override public double minValue() { return minValue; }
    @Override public double maxValue() { return maxValue; }

    // === 6 个特殊变换的数学实现 ===

    private static double halfNegative(double v) {
        return v < 0.0 ? v * 0.5 : v;
    }
    private static double quarterNegative(double v) {
        return v < 0.0 ? v * 0.25 : v;
    }
    private static double squeeze(double v) {
        // 原版 DensityFunctions.Mapped case SQUEEZE：clamp(d,-1,1)/2 - d^3/24
        double c = Math.clamp(v, -1.0, 1.0);
        return c / 2.0 - c * c * c / 24.0;
    }
}
