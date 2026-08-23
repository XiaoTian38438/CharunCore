package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/** 原版 DensityFunctions$Clamp */
public final class Clamp implements DensityFunction {
    private final DensityFunction input;
    private final double min, max;

    public Clamp(DensityFunction input, double min, double max) {
        this.input = input;
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
    }

    @Override public double compute(FunctionContext ctx) {
        return Math.max(min, Math.min(max, input.compute(ctx)));
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        input.fillArray(array, provider);
        for (int i = 0; i < array.length; i++) {
            double v = array[i];
            if (v < min) v = min;
            else if (v > max) v = max;
            array[i] = v;
        }
    }

    @Override public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new Clamp(input.mapAll(visitor), min, max));
    }

    @Override public double minValue() { return min; }
    @Override public double maxValue() { return max; }
}
