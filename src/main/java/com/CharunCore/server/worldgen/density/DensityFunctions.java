package com.CharunCore.server.worldgen.density;

import com.CharunCore.server.world.gen.CubicSpline;
import com.CharunCore.server.worldgen.density.functions.*;
import com.CharunCore.server.world.gen.NormalNoise;
import com.CharunCore.server.worldgen.density.functions.*;

/**
 * 原版 net.minecraft.world.level.levelgen.DensityFunctions 工厂集合。
 * 与原版 API 1:1 对应；删除 Codec/Holder 设施，可在没有 Mojang registries 的环境下构建 DensityFunction 树。
 */
public final class DensityFunctions {
    private DensityFunctions() {}

    // ===================== 常量 / 单例 =====================

    public static DensityFunction zero() {
        return Constant.ZERO;
    }

    public static DensityFunction constant(double value) {
        return new Constant(value);
    }

    public static DensityFunction yClampedGradient(int fromY, int toY, double fromValue, double toValue) {
        return new YClampedGradient(fromY, toY, fromValue, toValue);
    }

    public static DensityFunction noise(NormalNoise.NoiseParameters params) {
        return new Noise(new DensityFunction.NoiseHolder("", params), 1.0, 0.0);
    }

    public static DensityFunction noise(String key, NormalNoise.NoiseParameters params) {
        return new Noise(new DensityFunction.NoiseHolder(key, params), 1.0, 0.0);
    }

    /** 2-arg 原版签名：xzScale 和 yScale 都是 scaleFactor */
    public static DensityFunction noise(String key, NormalNoise.NoiseParameters params, double xzFactor) {
        return new Noise(new DensityFunction.NoiseHolder(key, params), xzFactor, 0.0);
    }

    public static DensityFunction noise(String key, NormalNoise.NoiseParameters params, double xzFactor, double yFactor) {
        return new Noise(new DensityFunction.NoiseHolder(key, params), xzFactor, yFactor);
    }

    public static DensityFunction noise(NormalNoise.NoiseParameters params, double xzFactor) {
        return new Noise(new DensityFunction.NoiseHolder("", params), xzFactor, 0.0);
    }

    public static DensityFunction noise(NormalNoise.NoiseParameters params, double xzFactor, double yFactor) {
        return new Noise(new DensityFunction.NoiseHolder("", params), xzFactor, yFactor);
    }

    public static DensityFunction noise(DensityFunction.NoiseHolder holder, double xzFactor, double yFactor) {
        return new Noise(holder, xzFactor, yFactor);
    }

    public static DensityFunction mappedNoise(NormalNoise.NoiseParameters params) {
        return new MappedNoise(new DensityFunction.NoiseHolder("", params), 1.0, 1.0, 0.0, 1.0);
    }

    /** 原版 2-arg：(params, fromFactor, toFactor) — xzFactor=yFactor=1.0 */
    public static DensityFunction mappedNoise(NormalNoise.NoiseParameters params, double fromFactor, double toFactor) {
        return new MappedNoise(new DensityFunction.NoiseHolder("", params), 1.0, 1.0, fromFactor, toFactor);
    }

    /** 原版 3-arg：(params, yScale, fromFactor, toFactor) — xzFactor=1.0 */
    public static DensityFunction mappedNoise(NormalNoise.NoiseParameters params, double yScale,
                                              double fromFactor, double toFactor) {
        return new MappedNoise(new DensityFunction.NoiseHolder("", params), 1.0, yScale, fromFactor, toFactor);
    }

    public static DensityFunction mappedNoise(NormalNoise.NoiseParameters params, double xzFactor, double yFactor,
                                              double fromFactor, double toFactor) {
        return new MappedNoise(new DensityFunction.NoiseHolder("", params), xzFactor, yFactor, fromFactor, toFactor);
    }

    public static DensityFunction mappedNoise(String key, NormalNoise.NoiseParameters params, double fromFactor, double toFactor) {
        return new MappedNoise(new DensityFunction.NoiseHolder(key, params), 1.0, 1.0, fromFactor, toFactor);
    }
    public static DensityFunction mappedNoise(String key, NormalNoise.NoiseParameters params, double yScale,
                                              double fromFactor, double toFactor) {
        return new MappedNoise(new DensityFunction.NoiseHolder(key, params), 1.0, yScale, fromFactor, toFactor);
    }
    public static DensityFunction mappedNoise(String key, NormalNoise.NoiseParameters params, double xzFactor, double yFactor,
                                              double fromFactor, double toFactor) {
        return new MappedNoise(new DensityFunction.NoiseHolder(key, params), xzFactor, yFactor, fromFactor, toFactor);
    }


    public static DensityFunction shiftedNoise2d(DensityFunction shiftX, DensityFunction shiftZ,
                                                 double xzFactor, NormalNoise.NoiseParameters params) {
        return new ShiftedNoise2D(shiftX, shiftZ, xzFactor, params);
    }

    public static DensityFunction shiftedNoise2d(String key, DensityFunction shiftX, DensityFunction shiftZ,
                                                 double xzFactor, NormalNoise.NoiseParameters params) {
        return new ShiftedNoise2D(shiftX, shiftZ, xzFactor, new DensityFunction.NoiseHolder(key, params));
    }

    public static DensityFunction shiftA(String key, NormalNoise.NoiseParameters params) {
        return new ShiftA(new DensityFunction.NoiseHolder(key, params));
    }

    public static DensityFunction shiftB(String key, NormalNoise.NoiseParameters params) {
        return new ShiftB(new DensityFunction.NoiseHolder(key, params));
    }

    public static DensityFunction shiftA(NormalNoise.NoiseParameters params) {
        return new ShiftA(new DensityFunction.NoiseHolder("", params));
    }

    public static DensityFunction shiftB(NormalNoise.NoiseParameters params) {
        return new ShiftB(new DensityFunction.NoiseHolder("", params));
    }

    // ===================== 算术组合 =====================

    public static DensityFunction add(DensityFunction a, DensityFunction b) {
        return TwoArgumentSimpleFunction.of(TwoArgumentSimpleFunction.Type.ADD, a, b);
    }

    public static DensityFunction mul(DensityFunction a, DensityFunction b) {
        return TwoArgumentSimpleFunction.of(TwoArgumentSimpleFunction.Type.MUL, a, b);
    }

    public static DensityFunction min(DensityFunction a, DensityFunction b) {
        return TwoArgumentSimpleFunction.of(TwoArgumentSimpleFunction.Type.MIN, a, b);
    }

    public static DensityFunction max(DensityFunction a, DensityFunction b) {
        return TwoArgumentSimpleFunction.of(TwoArgumentSimpleFunction.Type.MAX, a, b);
    }

    public static DensityFunction lerp(DensityFunction factor, DensityFunction from, DensityFunction to) {
        return new Lerp(factor, from, to);
    }

    /** 原版 NoiseRouterData.slide 调用 lerp(DF, double, DF) — 中间参数取常量 from */
    public static DensityFunction lerp(DensityFunction factor, double fromValue, DensityFunction to) {
        return new Lerp(factor, new Constant(fromValue), to);
    }

    // ===================== 控制流 =====================

    public static DensityFunction rangeChoice(DensityFunction input, double minRange, double maxRange,
                                              DensityFunction whenInside, DensityFunction whenOutside) {
        return new RangeChoice(input, minRange, maxRange, whenInside, whenOutside);
    }

    public static DensityFunction spline(CubicSpline spline,
                                         DensityFunction[] coordinateFunctions) {
        return new SplineAdapter(spline, coordinateFunctions);
    }

    // ===================== 缓存包装（Markers） =====================

    public static DensityFunction cache2d(DensityFunction function) {
        return new Marker(Marker.Type.CACHE2D, function);
    }

    public static DensityFunction flatCache(DensityFunction function) {
        return new Marker(Marker.Type.FLAT_CACHE, function);
    }

    public static DensityFunction cacheOnce(DensityFunction function) {
        return new Marker(Marker.Type.CACHE_ONCE, function);
    }

    public static DensityFunction interpolated(DensityFunction function) {
        return new Marker(Marker.Type.INTERPOLATED, function);
    }

    public static DensityFunction cacheAllInCell(DensityFunction function) {
        return new Marker(Marker.Type.CACHE_ALL_IN_CELL, function);
    }

    // ===================== Blend（地形过渡区） =====================

    public static DensityFunction blendAlpha() {
        return new BlendAlpha();
    }

    public static DensityFunction blendOffset() {
        return new BlendOffset();
    }

    public static DensityFunction blendDensity(DensityFunction function) {
        return new BlendDensity(function);
    }

    // ===================== Misc =====================

    public static DensityFunction weirdScaledSampler(String key, DensityFunction input, NormalNoise.NoiseParameters params,
                                                    WeirdScaledSampler.RarityValueMapper type) {
        return new WeirdScaledSampler(input, new DensityFunction.NoiseHolder(key, params), type);
    }

    public static DensityFunction weirdScaledSampler(DensityFunction input, NormalNoise.NoiseParameters params,
                                                    WeirdScaledSampler.RarityValueMapper type) {
        return new WeirdScaledSampler(input, new DensityFunction.NoiseHolder("", params), type);
    }

    public static DensityFunction endIslands(long seedImpl) {
        return new EndIslandDensityFunction(seedImpl);
    }

    public static DensityFunction findTopSurface(DensityFunction density, DensityFunction preliminarySurface,
                                                 int minY, int cellHeight) {
        return new FindTopSurface(density, preliminarySurface, minY, cellHeight);
    }
}
