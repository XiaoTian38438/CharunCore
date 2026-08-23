package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.world.gen.CubicSpline;
import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 原版 DensityFunctions$Spline — 通过 CubicSpline 计算密度。
 * 我们的 CubicSpline 已经在 com.CharunCore.server.world.gen 包，但那个版本对接是 float[] 上下文。
 * 为了对接 DensityFunction，CubicSplineFunction 是单独的接口包装。
 */
public interface CubicSplineFunction extends DensityFunction {

    /**
     * spline 函数需要从 DensityFunction.FunctionContext 提取 4 维坐标 (continentalness, erosion, ridges, ridges_folded)。
     * 子类实现决定如何提取 — 通常用 4 个 child DensityFunction 作为 coordinate。
     */
    double compute(FunctionContext ctx);

    /** 让 TerrainProvider 直接返回 CubicSpline 用于构造 */
    default CubicSpline asCubicSpline() {
        throw new UnsupportedOperationException("not bound to legacy CubicSpline");
    }
}
