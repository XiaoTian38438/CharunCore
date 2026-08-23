package com.CharunCore.server.world.gen;

public class LegacyRandomSource implements RandomSource {
    private static final long MULTIPLIER = 25214903917L;
    private static final long INCREMENT = 11L;
    private static final long MODULUS_MASK = 0xFFFFFFFFFFFFL;
    private long seed;
    private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);

    public LegacyRandomSource(long seed) {
        setSeed(seed);
    }

    @Override
    public RandomSource fork() {
        return new LegacyRandomSource(nextLong());
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return new LegacyPositionalRandomFactory(nextLong());
    }

    @Override
    public void setSeed(long seed) {
        this.seed = (seed ^ 0x5DEECE66DL) & MODULUS_MASK;
        this.gaussianSource.reset();
    }

    @Override
    public int next(int bits) {
        long nextSeed = (this.seed * MULTIPLIER + INCREMENT) & MODULUS_MASK;
        this.seed = nextSeed;
        return (int)(nextSeed >>> (48 - bits));
    }

    @Override
    public double nextGaussian() {
        return this.gaussianSource.nextGaussian();
    }

    public static class LegacyPositionalRandomFactory implements PositionalRandomFactory {
        private final long seed;

        public LegacyPositionalRandomFactory(long seed) {
            this.seed = seed;
        }

        @Override
        public RandomSource at(int x, int y, int z) {
            long l = Mth.getSeed(x, y, z);
            return new LegacyRandomSource(l ^ this.seed);
        }

        @Override
        public RandomSource fromHashOf(String name) {
            int hash = name.hashCode();
            return new LegacyRandomSource(hash ^ this.seed);
        }

        @Override
        public RandomSource fromSeed(long seed) {
            return new LegacyRandomSource(seed);
        }
    }
}
