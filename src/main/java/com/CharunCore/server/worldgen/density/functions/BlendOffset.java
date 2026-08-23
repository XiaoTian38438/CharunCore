package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/** 原版 DensityFunctions$BlendOffset — 地形过渡区偏移，1.21 单纯返回常量 0 */
public final class BlendOffset implements DensityFunction {
    @Override public double compute(FunctionContext ctx) { return 0.0; }
    @Override public void fillArray(double[] array, ContextProvider provider) { java.util.Arrays.fill(array, 0.0); }
    @Override public BlendOffset mapAll(Visitor visitor) { return this; }
    @Override public double minValue() { return 0.0; }
    @Override public double maxValue() { return 0.0; }
}
