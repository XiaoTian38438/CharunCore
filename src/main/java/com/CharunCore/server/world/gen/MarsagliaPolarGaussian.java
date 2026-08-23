package com.CharunCore.server.world.gen;

public final class MarsagliaPolarGaussian {
    private final RandomSource source;
    private boolean hasNextGaussian;
    private double nextGaussian;

    public MarsagliaPolarGaussian(RandomSource source) {
        this.source = source;
    }

    public void reset() {
        this.hasNextGaussian = false;
    }

    public double nextGaussian() {
        if (this.hasNextGaussian) {
            this.hasNextGaussian = false;
            return this.nextGaussian;
        }
        double v1, v2, s;
        do {
            v1 = 2.0 * this.source.nextDouble() - 1.0;
            v2 = 2.0 * this.source.nextDouble() - 1.0;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1.0 || s == 0.0);
        double multiplier = Math.sqrt(-2.0 * Math.log(s) / s);
        this.nextGaussian = v2 * multiplier;
        this.hasNextGaussian = true;
        return v1 * multiplier;
    }
}
