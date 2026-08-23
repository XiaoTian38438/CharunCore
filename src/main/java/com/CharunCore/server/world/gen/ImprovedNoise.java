package com.CharunCore.server.world.gen;

public final class ImprovedNoise {
    private static final float SHIFT_UP_EPSILON = 1.0E-7F;
    private final byte[] p;
    public final double xo, yo, zo;

    public ImprovedNoise(RandomSource random) {
        this.xo = random.nextDouble() * 256.0;
        this.yo = random.nextDouble() * 256.0;
        this.zo = random.nextDouble() * 256.0;
        this.p = new byte[256];
        for (int b = 0; b < 256; b++) this.p[b] = (byte)b;
        for (int b = 0; b < 256; b++) {
            int i = random.nextInt(256 - b);
            byte b1 = this.p[b];
            this.p[b] = this.p[b + i];
            this.p[b + i] = b1;
        }
    }

    public double noise(double x, double y, double z) {
        return noise(x, y, z, 0.0, 0.0);
    }

    public double noise(double x, double y, double z, double yStretch, double yLimit) {
        double d1 = x + this.xo;
        double d2 = y + this.yo;
        double d3 = z + this.zo;
        int i = Mth.floor(d1);
        int j = Mth.floor(d2);
        int k = Mth.floor(d3);
        double d4 = d1 - i;
        double d5 = d2 - j;
        double d6 = d3 - k;
        double d7;
        if (yStretch != 0.0) {
            double d;
            if (yLimit >= 0.0 && yLimit < d5) {
                d = yLimit;
            } else {
                d = d5;
            }
            d7 = Mth.floor(d / yStretch + 1.0000000116860974E-7) * yStretch;
        } else {
            d7 = 0.0;
        }
        return sampleAndLerp(i, j, k, d4, d5 - d7, d6, d5);
    }

    private static double gradDot(int hash, double x, double y, double z) {
        return SimplexNoise.dot(SimplexNoise.GRADIENT[hash & 0xF], x, y, z);
    }

    private int p(int v) {
        return this.p[v & 0xFF] & 0xFF;
    }

    private double sampleAndLerp(int ix, int iy, int iz, double x, double y, double z, double yUnstretched) {
        int i = p(ix);
        int j = p(ix + 1);
        int k = p(i + iy);
        int m = p(i + iy + 1);
        int n = p(j + iy);
        int i1 = p(j + iy + 1);

        double d1 = gradDot(p(k + iz), x, y, z);
        double d2 = gradDot(p(n + iz), x - 1.0, y, z);
        double d3 = gradDot(p(m + iz), x, y - 1.0, z);
        double d4 = gradDot(p(i1 + iz), x - 1.0, y - 1.0, z);
        double d5 = gradDot(p(k + iz + 1), x, y, z - 1.0);
        double d6 = gradDot(p(n + iz + 1), x - 1.0, y, z - 1.0);
        double d7 = gradDot(p(m + iz + 1), x, y - 1.0, z - 1.0);
        double d8 = gradDot(p(i1 + iz + 1), x - 1.0, y - 1.0, z - 1.0);

        double d9 = Mth.smoothstep(x);
        double d10 = Mth.smoothstep(yUnstretched);
        double d11 = Mth.smoothstep(z);

        return Mth.lerp3(d9, d10, d11, d1, d2, d3, d4, d5, d6, d7, d8);
    }
}
