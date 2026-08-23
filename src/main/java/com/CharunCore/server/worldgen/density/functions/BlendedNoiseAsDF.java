package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.world.gen.BlendedNoise;
import com.CharunCore.server.worldgen.density.DensityFunction;

public final class BlendedNoiseAsDF implements DensityFunction {
    private final String key;
    private final double xzScale;
    private final double yScale;
    private final double xzFactor;
    private final double yFactor;
    private final double smearScaleMultiplier;
    private BlendedNoise noise;
    private double minValue;
    private double maxValue;
    private boolean initialized;

    public BlendedNoiseAsDF(String key, double xzScale, double yScale, double xzFactor, double yFactor, double smearScaleMultiplier) {
        this.key = key;
        this.xzScale = xzScale;
        this.yScale = yScale;
        this.xzFactor = xzFactor;
        this.yFactor = yFactor;
        this.smearScaleMultiplier = smearScaleMultiplier;
    }

    private BlendedNoise noise() {
        if (!initialized) {
            BlendedNoise n = new BlendedNoise(
                DensityFunction.NoiseHolder.sharedFactory().fromHashOf(key),
                xzScale, yScale, xzFactor, yFactor, smearScaleMultiplier);
            this.noise = n;
            this.minValue = -n.maxValue();
            this.maxValue = n.maxValue();
            this.initialized = true;
        }
        return noise;
    }

    @Override public double compute(FunctionContext ctx) {
        return noise().compute(ctx.blockX(), ctx.blockY(), ctx.blockZ());
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override public BlendedNoiseAsDF mapAll(Visitor visitor) {
        return this;
    }

    @Override public double minValue() {
        if (!initialized) noise();
        return minValue;
    }
    @Override public double maxValue() {
        if (!initialized) noise();
        return maxValue;
    }
}
