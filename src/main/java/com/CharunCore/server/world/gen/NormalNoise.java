package com.CharunCore.server.world.gen;

public class NormalNoise {
    private static final double INPUT_FACTOR = 1.0181268882175227;
    private static final double TARGET_DEVIATION = 0.16666666666666666;

    private final PerlinNoise first;
    private final PerlinNoise second;
    private final double valueFactor;
    private final double maxValue;
    private final NoiseParameters parameters;

    public record NoiseParameters(int firstOctave, double[] amplitudes) {}

    public NormalNoise(RandomSource random, NoiseParameters params) {
        this(random, params, true);
    }

    public NormalNoise(RandomSource random, NoiseParameters params, boolean isNew) {
        this.parameters = params;
        if (isNew) {
            this.first = PerlinNoise.create(random, params.firstOctave(), params.amplitudes());
            this.second = PerlinNoise.create(random, params.firstOctave(), params.amplitudes());
        } else {
            this.first = createLegacy(random, params);
            this.second = createLegacy(random, params);
        }

        int minNonZero = Integer.MAX_VALUE;
        int maxNonZero = Integer.MIN_VALUE;
        for (int i = 0; i < params.amplitudes().length; i++) {
            if (params.amplitudes()[i] != 0.0) {
                minNonZero = Math.min(minNonZero, i);
                maxNonZero = Math.max(maxNonZero, i);
            }
        }

        this.valueFactor = TARGET_DEVIATION / expectedDeviation(maxNonZero - minNonZero);
        this.maxValue = (this.first.maxValue() + this.second.maxValue()) * this.valueFactor;
    }

    private static PerlinNoise createLegacy(RandomSource random, NoiseParameters params) {
        return new PerlinNoise(random, params.firstOctave(), params.amplitudes(), false);
    }

    public double getValue(double x, double y, double z) {
        double x2 = x * INPUT_FACTOR;
        double y2 = y * INPUT_FACTOR;
        double z2 = z * INPUT_FACTOR;
        return (this.first.getValue(x, y, z) + this.second.getValue(x2, y2, z2)) * this.valueFactor;
    }

    public double maxValue() {
        return this.maxValue;
    }

    private static double expectedDeviation(int octaves) {
        return 0.1 * (1.0 + 1.0 / (octaves + 1));
    }

    public NoiseParameters parameters() {
        return this.parameters;
    }
}
