package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/** 原版 DensityFunctions$BlendAlpha — 空 blender 时返回常量 1.0（dconst_1 验证自字节码） */
public final class BlendAlpha implements DensityFunction {
    @Override public double compute(FunctionContext ctx) { return 1.0; }
    @Override public void fillArray(double[] array, ContextProvider provider) { java.util.Arrays.fill(array, 1.0); }
    @Override public BlendAlpha mapAll(Visitor visitor) { return this; }
    @Override public double minValue() { return 1.0; }
    @Override public double maxValue() { return 1.0; }
}
