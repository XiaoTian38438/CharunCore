package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.world.gen.CubicSpline;
import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 一个把现有 gen.world.com.CharunCore.server.CubicSpline（基于 float[] 上下文）适配到
 * DensityFunction 体系的桥接器。coordinateFunction 数组的元素顺序与 TerrainProvider 约定一致：
 *   index 0 = continentalness
 *   index 1 = erosion
 *   index 2 = ridges
 *   index 3 = ridges_folded
 */
public final class SplineAdapter implements CubicSplineFunction {
    private final CubicSpline spline;
    private final DensityFunction[] coordinateFunctions;
    private final float minValue, maxValue;

    public SplineAdapter(CubicSpline spline, DensityFunction[] coordinateFunctions) {
        this.spline = spline;
        this.coordinateFunctions = coordinateFunctions;
        this.minValue = spline.minValue();
        this.maxValue = spline.maxValue();
    }

    @Override public double compute(FunctionContext ctx) {
        float[] climate = new float[4];
        for (int i = 0; i < coordinateFunctions.length && i < 4; i++) {
            climate[i] = (float) coordinateFunctions[i].compute(ctx);
        }
        return spline.apply(climate);
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override public DensityFunction mapAll(Visitor visitor) {
        DensityFunction[] mapped = new DensityFunction[coordinateFunctions.length];
        for (int i = 0; i < mapped.length; i++) mapped[i] = coordinateFunctions[i].mapAll(visitor);
        return new SplineAdapter(spline, mapped);
    }

    @Override public double minValue() { return minValue; }
    @Override public double maxValue() { return maxValue; }

    @Override public CubicSpline asCubicSpline() { return spline; }

    public CubicSpline spline() { return spline; }
    public DensityFunction[] coordinateFunctions() { return coordinateFunctions; }
}
