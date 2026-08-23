package com.CharunCore.server.world.gen;

public class PerlinNoise {
    private static final int ROUND_OFF = 33554432;
    private final ImprovedNoise[] noiseLevels;
    private final int firstOctave;
    private final double[] amplitudes;
    private final double lowestFreqValueFactor;
    private final double lowestFreqInputFactor;
    private final double maxValue;

    public static PerlinNoise create(RandomSource random, int firstOctave, double[] amplitudes) {
        return new PerlinNoise(random, firstOctave, amplitudes, true);
    }

    public PerlinNoise(PositionalRandomFactory factory, int firstOctave, double[] amplitudes) {
        this.firstOctave = firstOctave;
        this.amplitudes = amplitudes;
        int count = amplitudes.length;
        this.noiseLevels = new ImprovedNoise[count];

        for (int i = 0; i < count; i++) {
            if (amplitudes[i] != 0.0) {
                int octave = firstOctave + i;
                this.noiseLevels[i] = new ImprovedNoise(factory.fromHashOf("octave_" + octave));
            }
        }

        int negativeOctaves = -firstOctave;
        this.lowestFreqInputFactor = Math.pow(2.0, -negativeOctaves);
        this.lowestFreqValueFactor = Math.pow(2.0, (count - 1)) / (Math.pow(2.0, count) - 1.0);
        this.maxValue = edgeValue(2.0);
    }

    public PerlinNoise(RandomSource random, int firstOctave, double[] amplitudes, boolean isNew) {
        this.firstOctave = firstOctave;
        this.amplitudes = amplitudes;
        int count = amplitudes.length;
        this.noiseLevels = new ImprovedNoise[count];
        if (isNew) {
            PositionalRandomFactory factory = random.forkPositional();
            for (int i = 0; i < count; i++) {
                if (amplitudes[i] != 0.0) {
                    int octave = firstOctave + i;
                    this.noiseLevels[i] = new ImprovedNoise(factory.fromHashOf("octave_" + octave));
                }
            }
        } else {
            int j = -firstOctave;
            ImprovedNoise first = new ImprovedNoise(random);
            if (j >= 0 && j < count) {
                if (amplitudes[j] != 0.0) {
                    this.noiseLevels[j] = first;
                }
            }
            for (int k = j - 1; k >= 0; k--) {
                if (k < count) {
                    if (amplitudes[k] != 0.0) {
                        this.noiseLevels[k] = new ImprovedNoise(random);
                    } else {
                        random.consumeCount(262);
                    }
                } else {
                    random.consumeCount(262);
                }
            }
        }
        int negativeOctaves = -firstOctave;
        this.lowestFreqInputFactor = Math.pow(2.0, -negativeOctaves);
        this.lowestFreqValueFactor = Math.pow(2.0, (amplitudes.length - 1)) / (Math.pow(2.0, amplitudes.length) - 1.0);
        this.maxValue = edgeValue(2.0);
    }

    public double getValue(double x, double y, double z) {
        return getValue(x, y, z, 0.0, 0.0, false);
    }

    public double getValue(double x, double y, double z, double yStretch, double yLimit, boolean useOriginY) {
        double result = 0.0;
        double inputFactor = this.lowestFreqInputFactor;
        double valueFactor = this.lowestFreqValueFactor;
        for (int b = 0; b < this.noiseLevels.length; b++) {
            ImprovedNoise noise = this.noiseLevels[b];
            if (noise != null) {
                double v = noise.noise(
                    wrap(x * inputFactor),
                    useOriginY ? -noise.yo : wrap(y * inputFactor),
                    wrap(z * inputFactor),
                    yStretch * inputFactor,
                    yLimit * inputFactor
                );
                result += this.amplitudes[b] * v * valueFactor;
            }
            inputFactor *= 2.0;
            valueFactor /= 2.0;
        }
        return result;
    }

    public double maxValue() {
        return this.maxValue;
    }

    public double maxBrokenValue(double yMultiplier) {
        return edgeValue(yMultiplier + 2.0);
    }

    private double edgeValue(double value) {
        double result = 0.0;
        double vf = this.lowestFreqValueFactor;
        for (int b = 0; b < this.noiseLevels.length; b++) {
            ImprovedNoise noise = this.noiseLevels[b];
            if (noise != null) {
                result += this.amplitudes[b] * value * vf;
            }
            vf /= 2.0;
        }
        return result;
    }

    public ImprovedNoise getOctaveNoise(int index) {
        return this.noiseLevels[this.noiseLevels.length - 1 - index];
    }

    public static double wrap(double v) {
        return v - Mth.lfloor(v / 33554432.0 + 0.5) * 33554432.0;
    }
}
