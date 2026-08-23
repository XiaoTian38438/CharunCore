package com.CharunCore.server.world.gen;

public final class Xoroshiro128PlusPlus {
    private long seedLo;
    private long seedHi;
    private static final long DEFAULT_LO = -7046029254386353131L;
    private static final long DEFAULT_HI = 7640891576956012809L;

    public Xoroshiro128PlusPlus(long seedLo, long seedHi) {
        this.seedLo = seedLo;
        this.seedHi = seedHi;
        if ((seedLo | seedHi) == 0) {
            this.seedLo = DEFAULT_LO;
            this.seedHi = DEFAULT_HI;
        }
    }

    public Xoroshiro128PlusPlus(RandomSupport.Seed128bit seed) {
        this(seed.seedLo(), seed.seedHi());
    }

    public long nextLong() {
        long lo = this.seedLo;
        long hi = this.seedHi;
        long result = Long.rotateLeft(lo + hi, 17) + lo;
        hi ^= lo;
        this.seedLo = Long.rotateLeft(lo, 49) ^ hi ^ (hi << 21);
        this.seedHi = Long.rotateLeft(hi, 28);
        return result;
    }
}
