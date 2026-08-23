package com.CharunCore.server.world.gen;

public interface RandomSource {
    RandomSource fork();
    PositionalRandomFactory forkPositional();
    void setSeed(long seed);
    int next(int bits);
    default int nextInt() { return next(32); }
    default int nextInt(int bound) {
        if (bound <= 0) throw new IllegalArgumentException("Bound must be positive");
        if ((bound & (bound - 1)) == 0) return (int)((long)bound * next(31) >> 31);
        int bits, val;
        do {
            bits = next(31);
            val = bits % bound;
        } while (bits - val + (bound - 1) < 0);
        return val;
    }
    default long nextLong() {
        return ((long)next(32) << 32) + next(32);
    }
    default boolean nextBoolean() { return next(1) != 0; }
    default float nextFloat() { return next(24) * 5.9604645E-8f; }
    default double nextDouble() {
        return (((long)next(26) << 27) + next(27)) * 1.1102230246251565E-16;
    }
    default double nextGaussian() { throw new UnsupportedOperationException(); }
    default void consumeCount(int count) { for (int i = 0; i < count; i++) next(1); }
}
