package com.CharunCore.server.world.gen;

public final class Mth {
    private Mth() {}

    public static long getSeed(int x, int y, int z) {
        long l = (long)(x * 3129871) ^ (long)(z * 116129781L) ^ (long)y;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }

    public static double clampedLerp(double t, double a, double b) {
        if (t < 0.0) return a;
        if (t > 1.0) return b;
        return a + t * (b - a);
    }

    public static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    public static float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    public static double smoothstep(double t) {
        return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
    }

    public static double lerp3(double t1, double t2, double t3,
            double v000, double v100, double v010, double v110,
            double v001, double v101, double v011, double v111) {
        return lerp(t3,
            lerp(t2, lerp(t1, v000, v100), lerp(t1, v010, v110)),
            lerp(t2, lerp(t1, v001, v101), lerp(t1, v011, v111)));
    }

    public static double lerp2(double t1, double t2,
            double v00, double v10, double v01, double v11) {
        return lerp(t2, lerp(t1, v00, v10), lerp(t1, v01, v11));
    }

    public static int floor(double d) {
        int i = (int)d;
        return d < i ? i - 1 : i;
    }

    public static long lfloor(double d) {
        long l = (long)d;
        return d < l ? l - 1L : l;
    }

    public static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    public static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    public static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    public static int floorDiv(int a, int b) {
        return Math.floorDiv(a, b);
    }

    public static int floorMod(int a, int b) {
        return Math.floorMod(a, b);
    }

    public static double clampedMap(double value, double min, double max, double start, double end) {
        if (value <= min) return start;
        if (value >= max) return end;
        return start + (value - min) / (max - min) * (end - start);
    }

    public static double map(double value, double min, double max, double start, double end) {
        return start + (value - min) / (max - min) * (end - start);
    }

    public static int quantize(double value, int factor) {
        return (int)Math.round(value / factor) * factor;
    }

    public static int square(int n) {
        return n * n;
    }

    public static float abs(float f) {
        return Math.abs(f);
    }

    public static int abs(int n) {
        return Math.abs(n);
    }

    public static float randomBetween(RandomSource randomSource, float min, float max) {
        return min + randomSource.nextFloat() * (max - min);
    }
}
