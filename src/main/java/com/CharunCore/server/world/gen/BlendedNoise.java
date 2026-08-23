package com.CharunCore.server.world.gen;

public class BlendedNoise {
    private final PerlinNoise minLimitNoise;
    private final PerlinNoise maxLimitNoise;
    private final PerlinNoise mainNoise;
    private final double xzMultiplier;
    private final double yMultiplier;
    private final double xzFactor;
    private final double yFactor;
    private final double smearScaleMultiplier;
    private final double maxValue;
    private final double xzScale;
    private final double yScale;

    public static BlendedNoise createUnseeded(double xzScale, double yScale, double xzFactor, double yFactor, double smearScaleMultiplier) {
        return new BlendedNoise(new XoroshiroRandomSource(0L), xzScale, yScale, xzFactor, yFactor, smearScaleMultiplier);
    }

    public BlendedNoise(RandomSource random, double xzScale, double yScale, double xzFactor, double yFactor, double smearScaleMultiplier) {
        this.xzScale = xzScale;
        this.yScale = yScale;
        this.xzFactor = xzFactor;
        this.yFactor = yFactor;
        this.smearScaleMultiplier = smearScaleMultiplier;
        this.xzMultiplier = 684.412D * xzScale;
        this.yMultiplier = 684.412D * yScale;

        int[] octavesMin = new int[16];
        for (int i = 0; i < 16; i++) octavesMin[i] = -15 + i;
        double[] ampsMin = new double[16];
        java.util.Arrays.fill(ampsMin, 1.0D);
        this.minLimitNoise = new PerlinNoise(random, -15, ampsMin, false);

        int[] octavesMax = new int[16];
        for (int i = 0; i < 16; i++) octavesMax[i] = -15 + i;
        double[] ampsMax = new double[16];
        java.util.Arrays.fill(ampsMax, 1.0D);
        this.maxLimitNoise = new PerlinNoise(random, -15, ampsMax, false);

        double[] ampsMain = new double[8];
        java.util.Arrays.fill(ampsMain, 1.0D);
        this.mainNoise = new PerlinNoise(random, -7, ampsMain, false);

        this.maxValue = this.minLimitNoise.maxBrokenValue(this.yMultiplier);
    }

    public double compute(int blockX, int blockY, int blockZ) {
        double d1 = blockX * this.xzMultiplier;
        double d2 = blockY * this.yMultiplier;
        double d3 = blockZ * this.xzMultiplier;

        double d4 = d1 / this.xzFactor;
        double d5 = d2 / this.yFactor;
        double d6 = d3 / this.xzFactor;

        double d7 = this.yMultiplier * this.smearScaleMultiplier;
        double d8 = d7 / this.yFactor;

        double d11 = 0.0D;
        double d12 = 1.0D;

        for (int b1 = 0; b1 < 8; b1++) {
            ImprovedNoise improvedNoise = this.mainNoise.getOctaveNoise(b1);
            if (improvedNoise != null) {
                d11 += improvedNoise.noise(
                    PerlinNoise.wrap(d4 * d12),
                    PerlinNoise.wrap(d5 * d12),
                    PerlinNoise.wrap(d6 * d12),
                    d8 * d12,
                    d5 * d12
                ) / d12;
            }
            d12 /= 2.0D;
        }

        double d13 = (d11 / 10.0D + 1.0D) / 2.0D;

        boolean skipMin = d13 >= 1.0D;
        boolean skipMax = d13 <= 0.0D;

        double d9 = 0.0D;
        double d10 = 0.0D;

        d12 = 1.0D;
        for (int b2 = 0; b2 < 16; b2++) {
            double sx = PerlinNoise.wrap(d1 * d12);
            double sy = PerlinNoise.wrap(d2 * d12);
            double sz = PerlinNoise.wrap(d3 * d12);
            double syStretch = d7 * d12;

            if (!skipMin) {
                ImprovedNoise n = this.minLimitNoise.getOctaveNoise(b2);
                if (n != null) {
                    d9 += n.noise(sx, sy, sz, syStretch, d2 * d12) / d12;
                }
            }
            if (!skipMax) {
                ImprovedNoise n = this.maxLimitNoise.getOctaveNoise(b2);
                if (n != null) {
                    d10 += n.noise(sx, sy, sz, syStretch, d2 * d12) / d12;
                }
            }
            d12 /= 2.0D;
        }

        return Mth.clampedLerp(d13, d9 / 512.0D, d10 / 512.0D) / 128.0D;
    }

    public double maxValue() {
        return this.maxValue;
    }
}
