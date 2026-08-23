package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 原版 DensityFunctions.mappedNoise 工厂产生的 DensityFunction 形态。
 * 噪声值 [-1, 1] 被 lerp 到 [fromFactor, toFactor] 范围。
 * 注意 NoiseRouterData 用 mappedNoise(provider, xz, yz, -0.x, -0.y) 等所有 4-arg 调用，故需 4 个参数。
 */
public final class MappedNoise implements DensityFunction {
    private final DensityFunction.NoiseHolder noise;
    private final double xzScale, yScale;
    private final double fromFactor, toFactor;
    private final double minValue, maxValue;

    public MappedNoise(DensityFunction.NoiseHolder noise, double xzScale, double yScale,
                       double fromFactor, double toFactor) {
        this.noise = noise;
        this.xzScale = xzScale;
        this.yScale = yScale;
        this.fromFactor = fromFactor;
        this.toFactor = toFactor;
        double mv = noise.maxValue();
        this.minValue = Math.min(fromFactor, toFactor);  // 因为输入 [-1, 1] 线性映射头尾
        this.maxValue = Math.max(fromFactor, toFactor);
    }

    public DensityFunction.NoiseHolder noise() { return noise; }
    public double xzScale() { return xzScale; }
    public double yScale() { return yScale; }
    public double fromFactor() { return fromFactor; }
    public double toFactor() { return toFactor; }

    @Override public double compute(FunctionContext ctx) {
        double x = ctx.blockX() * xzScale;
        double y = ctx.blockY() * yScale;
        double z = ctx.blockZ() * xzScale;
        double raw = noise.getValue(x, y, z);
        return mapUnitRange(raw, fromFactor, toFactor);
    }

    // ========== 原版映射公式 ==========
    // 原版：compute = rawNoise => 从 [-1,1] 映射到 [fromFactor, toFactor]
    // lerp(raw[+1]/2, from, to). 注意 raw 范围 max=1.0/2 大量不会达 ±1，但映射公式照 lerp。
    public static double mapUnitRange(double raw, double fromFactor, double toFactor) {
        double t = (raw + 1.0) * 0.5; // [-1,1] -> [0,1]
        if (t < 0.0) t = 0.0;
        else if (t > 1.0) t = 1.0;
        return fromFactor + t * (toFactor - fromFactor);
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override public MappedNoise mapAll(Visitor visitor) {
        return new MappedNoise(noise, xzScale, yScale, fromFactor, toFactor);
    }

    @Override public double minValue() { return minValue; }
    @Override public double maxValue() { return maxValue; }
}
