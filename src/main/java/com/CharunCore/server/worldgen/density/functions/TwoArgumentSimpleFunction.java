package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 原版 DensityFunctions$TwoArgumentSimpleFunction — ADD/MUL/MIN/MAX 四种二元算术。
 * Lerp 是单独 3 输入类。本类采用普通 class 实现，避免原版 interface 反编译复杂。
 */
public final class TwoArgumentSimpleFunction implements DensityFunction {
    public enum Type {
        ADD, MUL, MIN, MAX;
        public double apply(double a, double b) {
            return switch (this) {
                case ADD -> a + b;
                case MUL -> a * b;
                case MIN -> Math.min(a, b);
                case MAX -> Math.max(a, b);
            };
        }
    }

    private final Type type;
    private final DensityFunction arg1, arg2;
    private final double minV, maxV;

    private TwoArgumentSimpleFunction(Type type, DensityFunction a1, DensityFunction a2, double minV, double maxV) {
        this.type = type;
        this.arg1 = a1;
        this.arg2 = a2;
        this.minV = minV;
        this.maxV = maxV;
    }

    public static TwoArgumentSimpleFunction of(Type type, DensityFunction a, DensityFunction b) {
        return new TwoArgumentSimpleFunction(type, a, b,
                type == Type.MIN ? Math.min(a.minValue(), b.minValue()) :
                type == Type.MAX ? Math.max(a.minValue(), b.minValue()) :
                type == Type.ADD ? a.minValue() + b.minValue() : a.minValue() * b.minValue(),
                type == Type.MIN ? Math.min(a.maxValue(), b.maxValue()) :
                type == Type.MAX ? Math.max(a.maxValue(), b.maxValue()) :
                type == Type.ADD ? a.maxValue() + b.maxValue() : a.maxValue() * b.maxValue());
    }

    public Type type() { return type; }
    public DensityFunction argument1() { return arg1; }
    public DensityFunction argument2() { return arg2; }

    @Override public double compute(FunctionContext ctx) {
        return type.apply(arg1.compute(ctx), arg2.compute(ctx));
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        arg1.fillArray(array, provider);
        double[] tmp = new double[array.length];
        arg2.fillArray(tmp, provider);
        for (int i = 0; i < array.length; i++) array[i] = type.apply(array[i], tmp[i]);
    }

    @Override public DensityFunction mapAll(Visitor visitor) {
        return new TwoArgumentSimpleFunction(type, arg1.mapAll(visitor), arg2.mapAll(visitor),
                minV, maxV); // 简化：保持 min/max，原版会重算
    }

    @Override public double minValue() { return minV; }
    @Override public double maxValue() { return maxV; }
}
