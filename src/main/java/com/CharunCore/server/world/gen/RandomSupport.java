package com.CharunCore.server.world.gen;

public final class RandomSupport {
    public static final long GOLDEN_RATIO_64 = 0x9E3779B97F4A7C15L;
    public static final long SILVER_RATIO_64 = 0xBB1A8571D9F3C2A7L;

    private RandomSupport() {}

    public static long mixStafford13(long seed) {
        seed ^= seed >>> 30;
        seed *= -4658895280553007687L;
        seed ^= seed >>> 27;
        seed *= -7723592293110705685L;
        seed ^= seed >>> 31;
        return seed;
    }

    public static Seed128bit upgradeSeedTo128bit(long seed) {
        return upgradeSeedTo128bitUnmixed(seed).mixed();
    }

    public static Seed128bit upgradeSeedTo128bitUnmixed(long seed) {
        long lo = seed ^ 7640891576956012809L;
        long hi = lo + -7046029254386353131L;
        return new Seed128bit(lo, hi);
    }

    public static Seed128bit seedFromHashOf(String name) {
        try {
            var md5 = java.security.MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(name.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            long lo = longFromBytes(bytes, 0);
            long hi = longFromBytes(bytes, 8);
            return new Seed128bit(lo, hi);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static long longFromBytes(byte[] b, int off) {
        return ((long)(b[off] & 0xFF) << 56)
             | ((long)(b[off+1] & 0xFF) << 48)
             | ((long)(b[off+2] & 0xFF) << 40)
             | ((long)(b[off+3] & 0xFF) << 32)
             | ((long)(b[off+4] & 0xFF) << 24)
             | ((long)(b[off+5] & 0xFF) << 16)
             | ((long)(b[off+6] & 0xFF) << 8)
             | ((long)(b[off+7] & 0xFF));
    }

    public record Seed128bit(long seedLo, long seedHi) {
        public Seed128bit xor(long lo, long hi) {
            return new Seed128bit(this.seedLo ^ lo, this.seedHi ^ hi);
        }

        public Seed128bit xor(Seed128bit other) {
            return xor(other.seedLo, other.seedHi);
        }

        public Seed128bit mixed() {
            return new Seed128bit(
                RandomSupport.mixStafford13(this.seedLo),
                RandomSupport.mixStafford13(this.seedHi));
        }
    }
}
