package com.CharunCore.server.world.gen;

public class XoroshiroRandomSource implements RandomSource {
    private Xoroshiro128PlusPlus rng;
    private final MarsagliaPolarGaussian gaussianSource;

    public XoroshiroRandomSource(long seed) {
        this.gaussianSource = new MarsagliaPolarGaussian(this);
        this.rng = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
    }

    public XoroshiroRandomSource(long seedLo, long seedHi) {
        this.gaussianSource = new MarsagliaPolarGaussian(this);
        this.rng = new Xoroshiro128PlusPlus(seedLo, seedHi);
    }

    public XoroshiroRandomSource(RandomSupport.Seed128bit seed) {
        this(seed.seedLo(), seed.seedHi());
    }

    private XoroshiroRandomSource(Xoroshiro128PlusPlus rng) {
        this.gaussianSource = new MarsagliaPolarGaussian(this);
        this.rng = rng;
    }

    @Override
    public RandomSource fork() {
        return new XoroshiroRandomSource(this.rng.nextLong(), this.rng.nextLong());
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return new XoroshiroPositionalRandomFactory(this.rng.nextLong(), this.rng.nextLong());
    }

    @Override
    public void setSeed(long seed) {
        this.rng = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
        this.gaussianSource.reset();
    }

    @Override
    public int next(int bits) {
        return (int)(this.rng.nextLong() >>> (64 - bits));
    }

    @Override
    public int nextInt() {
        return (int)this.rng.nextLong();
    }

    @Override
    public int nextInt(int bound) {
        if (bound <= 0) throw new IllegalArgumentException("Bound must be positive");
        long r = Integer.toUnsignedLong(nextInt());
        long product = r * bound;
        long low = product & 0xFFFFFFFFL;
        if (low < bound) {
            int m = Integer.remainderUnsigned(-bound, bound);
            while (low < m) {
                r = Integer.toUnsignedLong(nextInt());
                product = r * bound;
                low = product & 0xFFFFFFFFL;
            }
        }
        return (int)(product >>> 32);
    }

    @Override
    public long nextLong() {
        return this.rng.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return (this.rng.nextLong() & 1L) != 0L;
    }

    @Override
    public float nextFloat() {
        return (float)(nextBits(24)) * 5.9604645E-8f;
    }

    @Override
    public double nextDouble() {
        return (double)(nextBits(53)) * 1.1102230246251565E-16;
    }

    @Override
    public double nextGaussian() {
        return this.gaussianSource.nextGaussian();
    }

    @Override
    public void consumeCount(int count) {
        for (int i = 0; i < count; i++) this.rng.nextLong();
    }

    private long nextBits(int bits) {
        return this.rng.nextLong() >>> (64 - bits);
    }

    public static class XoroshiroPositionalRandomFactory implements PositionalRandomFactory {
        private final long seedLo;
        private final long seedHi;

        public XoroshiroPositionalRandomFactory(long seedLo, long seedHi) {
            this.seedLo = seedLo;
            this.seedHi = seedHi;
        }

        @Override
        public RandomSource at(int x, int y, int z) {
            long l = Mth.getSeed(x, y, z);
            long lo = l ^ this.seedLo;
            return new XoroshiroRandomSource(lo, this.seedHi);
        }

        @Override
        public RandomSource fromHashOf(String name) {
            RandomSupport.Seed128bit hashed = RandomSupport.seedFromHashOf(name);
            RandomSupport.Seed128bit xored = hashed.xor(this.seedLo, this.seedHi);
            return new XoroshiroRandomSource(xored);
        }

        @Override
        public RandomSource fromSeed(long seed) {
            return new XoroshiroRandomSource(seed ^ this.seedLo, seed ^ this.seedHi);
        }
    }
}
