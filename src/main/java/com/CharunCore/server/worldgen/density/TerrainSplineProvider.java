package com.CharunCore.server.worldgen.density;

import com.CharunCore.server.world.gen.CubicSpline;
import com.CharunCore.server.world.gen.TerrainProvider;
import com.CharunCore.server.worldgen.density.functions.SplineAdapter;

/**
 * TerrainSplineProvider — 包装项目已有的 TerrainProvider，把它的 3 个 CubicSpline
 * （offset/factor/jaggedness）暴露为 DensityFunction 形式。
 *
 * 原 TerrainProvider.overworldOffset(coord1, coord2, coord4, amplified) 接收
 * `BoundedFloatFunction<float[]>`（4 元素：continentalness/erosion/ridges/ridges_folded）。
 * 我们用对应的 DensityFunction 作坐标源包装为 SplineAdapter。
 *
 * Coordinate 内嵌类的语义也保留：原版 DensityFunctions.Spline.Coordinate 是密度树节点引用哨兵，
 * 在我们的简化版里就是直接的 DensityFunction。
 */
public final class TerrainSplineProvider {
    private TerrainSplineProvider() {}

    public static DensityFunction offsetSpline(DensityFunction continentsDensity,
                                                DensityFunction erosionDensity,
                                                DensityFunction ridgesFoldedDensity,
                                                boolean amplified) {
        CubicSpline spline = TerrainProvider.overworldOffset(
                TerrainProvider.coordinate(0),
                TerrainProvider.coordinate(1),
                TerrainProvider.coordinate(3),
                amplified);
        return new SplineAdapter(spline, new DensityFunction[] { continentsDensity, erosionDensity, ridgesFoldedDensity, ridgesFoldedDensity });
    }

    public static DensityFunction factorSpline(DensityFunction continentsDensity,
                                               DensityFunction erosionDensity,
                                               DensityFunction ridgesDensity,
                                               DensityFunction ridgesFoldedDensity,
                                               boolean amplified) {
        CubicSpline spline = TerrainProvider.overworldFactor(
                TerrainProvider.coordinate(0),
                TerrainProvider.coordinate(1),
                TerrainProvider.coordinate(2),
                TerrainProvider.coordinate(3),
                amplified);
        return new SplineAdapter(spline, new DensityFunction[] { continentsDensity, erosionDensity, ridgesDensity, ridgesFoldedDensity });
    }

    public static DensityFunction jaggednessSpline(DensityFunction continentsDensity,
                                                   DensityFunction erosionDensity,
                                                   DensityFunction ridgesDensity,
                                                   DensityFunction ridgesFoldedDensity,
                                                   boolean amplified) {
        CubicSpline spline = TerrainProvider.overworldJaggedness(
                TerrainProvider.coordinate(0),
                TerrainProvider.coordinate(1),
                TerrainProvider.coordinate(2),
                TerrainProvider.coordinate(3),
                amplified);
        return new SplineAdapter(spline, new DensityFunction[] { continentsDensity, erosionDensity, ridgesDensity, ridgesFoldedDensity });
    }

    /** 原版 NoiseRouterData.peaksAndValleys(df) — 用 DensityFunction 表达 */
    public static DensityFunction peaksAndValleysDF(DensityFunction ridges) {
        DensityFunction abs = ridges.abs();
        DensityFunction offset = DensityFunctions.add(abs, DensityFunctions.constant(-0.6666666666666666)).abs();
        return DensityFunctions.mul(DensityFunctions.add(offset, DensityFunctions.constant(-0.3333333333333333)), DensityFunctions.constant(-3.0));
    }
}
