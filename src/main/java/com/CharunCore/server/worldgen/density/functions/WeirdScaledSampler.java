package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 原版 DensityFunctions$WeirdScaledSampler — 用 input.compute 选最初 rarity，然后对噪声按 rarity 来缩放采样并取绝对值。
 * 两种 rarity 映射：
 *   TYPE1: noise_impl 是 SpaghettiRarity3D 范围 (0.75/1.0/1.5/2.0)
 *   TYPE2: noise_impl 是 SpaghettiRarity2D 范围 (0.5/0.75/1.0/2.0/3.0)
 * compute = rarity * |noise.value((x*2/rarity, y*2/rarity, z*2/rarity))|
 */
public final class WeirdScaledSampler implements DensityFunction {
    public enum RarityValueMapper { TYPE1, TYPE2 }

    private final DensityFunction input;
    private final DensityFunction.NoiseHolder noise;
    private final RarityValueMapper mapper;

    public WeirdScaledSampler(DensityFunction input, DensityFunction.NoiseHolder noise, RarityValueMapper mapper) {
        this.input = input;
        this.noise = noise;
        this.mapper = mapper;
    }

    public DensityFunction input() { return input; }
    public DensityFunction.NoiseHolder noise() { return noise; }
    public RarityValueMapper rarityValueMapper() { return mapper; }

    private double rarity(double v) {
        if (mapper == RarityValueMapper.TYPE1) {
            if (v < -0.5) return 0.75;
            if (v < 0.0)  return 1.0;
            if (v < 0.5)  return 1.5;
            return 2.0;
        } else { // TYPE2
            if (v < -0.75) return 0.5;
            if (v < -0.5)  return 0.75;
            if (v < 0.5)   return 1.0;
            if (v < 0.75)  return 2.0;
            return 3.0;
        }
    }

    @Override public double compute(FunctionContext ctx) {
        double inp = input.compute(ctx);
        double r = rarity(inp);
        double x = ctx.blockX() * 2.0 / r;
        double y = ctx.blockY() * 2.0 / r;
        double z = ctx.blockZ() * 2.0 / r;
        return Math.abs(noise.getValue(x, y, z)) * r;
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override public WeirdScaledSampler mapAll(Visitor visitor) {
        return new WeirdScaledSampler(input.mapAll(visitor), noise, mapper);
    }

    @Override public double minValue() { return 0.0; }
    @Override public double maxValue() { return 2.0; } // 大致估算
}
