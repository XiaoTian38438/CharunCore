package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 原版 DensityFunctions$BlendDensity — 在 1.21 中地形混合(blending)特性已被淡化。
 * 简化版：直接返回原 function（不混合），等价 transform identity。
 */
public final class BlendDensity implements DensityFunction {
    private final DensityFunction function;
    public BlendDensity(DensityFunction function) { this.function = function; }
    public DensityFunction function() { return function; }

    @Override public double compute(FunctionContext ctx) { return function.compute(ctx); }
    @Override public void fillArray(double[] array, ContextProvider provider) { function.fillArray(array, provider); }
    @Override public BlendDensity mapAll(Visitor visitor) {
        return new BlendDensity(function.mapAll(visitor));
    }
    @Override public double minValue() { return function.minValue(); }
    @Override public double maxValue() { return function.maxValue(); }
}
