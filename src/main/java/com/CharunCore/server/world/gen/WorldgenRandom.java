package com.CharunCore.server.world.gen;

public final class WorldgenRandom implements RandomSource {
    private final RandomSource randomSource;

    public WorldgenRandom(RandomSource randomSource) {
        this.randomSource = randomSource;
    }

    public long setDecorationSeed(long levelSeed, int blockX, int blockZ) {
        randomSource.setSeed(levelSeed);
        long a = randomSource.nextLong() | 1L;
        long b = randomSource.nextLong() | 1L;
        long seed = (long) blockX * a + (long) blockZ * b ^ levelSeed;
        randomSource.setSeed(seed);
        return seed;
    }

    public void setFeatureSeed(long decorationSeed, int featureIndex, int step) {
        randomSource.setSeed(decorationSeed + featureIndex + 10000L * step);
    }

    public void setLargeFeatureSeed(long levelSeed, int chunkX, int chunkZ) {
        randomSource.setSeed(levelSeed);
        long a = randomSource.nextLong();
        long b = randomSource.nextLong();
        long seed = (long) chunkX * a ^ (long) chunkZ * b ^ levelSeed;
        randomSource.setSeed(seed);
    }

    public void setLargeFeatureWithSalt(long levelSeed, int chunkX, int chunkZ, int salt) {
        randomSource.setSeed((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L + levelSeed + salt);
    }

    @Override
    public RandomSource fork() {
        return randomSource.fork();
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return randomSource.forkPositional();
    }

    @Override
    public void setSeed(long seed) {
        randomSource.setSeed(seed);
    }

    @Override
    public int next(int bits) {
        return randomSource.next(bits);
    }

    @Override
    public int nextInt() {
        return randomSource.nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return randomSource.nextInt(bound);
    }

    @Override
    public long nextLong() {
        return randomSource.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return randomSource.nextBoolean();
    }

    @Override
    public float nextFloat() {
        return randomSource.nextFloat();
    }

    @Override
    public double nextDouble() {
        return randomSource.nextDouble();
    }

    @Override
    public double nextGaussian() {
        return randomSource.nextGaussian();
    }

    @Override
    public void consumeCount(int count) {
        randomSource.consumeCount(count);
    }
}
