package com.CharunCore.server.world.gen;

public final class SimplexNoise {
    public static final int[][] GRADIENT = {
        {1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0},
        {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1},
        {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1},
        {1, 1, 0}, {0, -1, 1}, {-1, 1, 0}, {0, -1, -1}
    };

    private static final double F2 = 0.5D * (Math.sqrt(3.0D) - 1.0D);
    private static final double G2 = (3.0D - Math.sqrt(3.0D)) / 6.0D;

    private final int[] p = new int[512];
    public final double xo, yo, zo;

    public SimplexNoise(long seed) {
        java.util.Random r = new java.util.Random(seed);
        this.xo = r.nextDouble() * 256.0D;
        this.yo = r.nextDouble() * 256.0D;
        this.zo = r.nextDouble() * 256.0D;
        for (int b = 0; b < 256; b++) this.p[b] = b;
        for (int b = 0; b < 256; b++) {
            int i = r.nextInt(256 - b);
            int j = this.p[b];
            this.p[b] = this.p[i + b];
            this.p[i + b] = j;
        }
    }

    public SimplexNoise(RandomSource randomSource) {
        this.xo = randomSource.nextDouble() * 256.0D;
        this.yo = randomSource.nextDouble() * 256.0D;
        this.zo = randomSource.nextDouble() * 256.0D;
        for (int b = 0; b < 256; b++) this.p[b] = b;
        for (int b = 0; b < 256; b++) {
            int i = randomSource.nextInt(256 - b);
            int j = this.p[b];
            this.p[b] = this.p[i + b];
            this.p[i + b] = j;
        }
    }

    private int p(int v) {
        return this.p[v & 0xFF];
    }

    public static double dot(int[] g, double x, double y, double z) {
        return g[0] * x + g[1] * y + g[2] * z;
    }

    private double getCornerNoise3D(int gradientIndex, double x, double y, double z, double maxValue) {
        double t = maxValue - x * x - y * y - z * z;
        if (t < 0.0D) return 0.0D;
        t *= t;
        return t * t * dot(GRADIENT[gradientIndex & 0xF], x, y, z);
    }

    /** 2D simplex — 原版 getValue(double,double)：归一化 70.0、maxValue 0.5、3 角、z 固定 0（值域约 [-1,1]）。 */
    public double getValue(double d, double d2) {
        double d6 = (d + d2) * F2;
        int n4 = (int) Math.floor(d + d6);
        int n3 = (int) Math.floor(d2 + d6);
        double d5 = (double) (n4 + n3) * G2;
        double d8 = d - ((double) n4 - d5);
        double d4 = d2 - ((double) n3 - d5);
        int n2, n;
        if (d8 > d4) { n2 = 1; n = 0; }
        else { n2 = 0; n = 1; }
        double d9 = d8 - n2 + G2;
        double d10 = d4 - n + G2;
        double d11 = d8 - 1.0 + 2.0 * G2;
        double d12 = d4 - 1.0 + 2.0 * G2;
        int n5 = n4 & 0xFF;
        int n6 = n3 & 0xFF;
        int n7 = p(n5 + p(n6)) % 12;
        int n8 = p(n5 + n2 + p(n6 + n)) % 12;
        int n9 = p(n5 + 1 + p(n6 + 1)) % 12;
        double d13 = getCornerNoise3D(n7, d8, d4, 0.0, 0.5);
        double d14 = getCornerNoise3D(n8, d9, d10, 0.0, 0.5);
        double d15 = getCornerNoise3D(n9, d11, d12, 0.0, 0.5);
        return 70.0 * (d13 + d14 + d15);
    }

    public double getValue(double x, double y, double z) {
        double d = (x + y + z) * 0.3333333333333333D;
        int i = (int) Math.floor(x + d);
        int j = (int) Math.floor(y + d);
        int k = (int) Math.floor(z + d);
        double d4 = (i + j + k) * 0.16666666666666666D;
        double d5 = i - d4;
        double d6 = j - d4;
        double d7 = k - d4;
        double d8 = x - d5;
        double d9 = y - d6;
        double d10 = z - d7;

        int b1, b2, b3, b4, b5, b6;
        if (d8 >= d9) {
            if (d9 >= d10)      { b1=1; b2=0; b3=0; b4=1; b5=1; b6=0; }
            else if (d8 >= d10) { b1=1; b2=0; b3=0; b4=1; b5=0; b6=1; }
            else                { b1=0; b2=0; b3=1; b4=1; b5=0; b6=1; }
        } else if (d9 < d10)   { b1=0; b2=0; b3=1; b4=0; b5=1; b6=1; }
        else if (d8 < d10)     { b1=0; b2=1; b3=0; b4=0; b5=1; b6=1; }
        else                   { b1=0; b2=1; b3=0; b4=1; b5=1; b6=0; }

        double d11 = d8 - b1 + 0.16666666666666666D;
        double d12 = d9 - b2 + 0.16666666666666666D;
        double d13 = d10 - b3 + 0.16666666666666666D;
        double d14 = d8 - b4 + 0.3333333333333333D;
        double d15 = d9 - b5 + 0.3333333333333333D;
        double d16 = d10 - b6 + 0.3333333333333333D;
        double d17 = d8 - 1.0D + 0.5D;
        double d18 = d9 - 1.0D + 0.5D;
        double d19 = d10 - 1.0D + 0.5D;

        int m = i & 0xFF, n = j & 0xFF, i1 = k & 0xFF;
        int i2 = p(m + p(n + p(i1))) % 12;
        int i3 = p(m + b1 + p(n + b2 + p(i1 + b3))) % 12;
        int i4 = p(m + b4 + p(n + b5 + p(i1 + b6))) % 12;
        int i5 = p(m + 1 + p(n + 1 + p(i1 + 1))) % 12;

        double d20 = getCornerNoise3D(i2, d8, d9, d10, 0.6D);
        double d21 = getCornerNoise3D(i3, d11, d12, d13, 0.6D);
        double d22 = getCornerNoise3D(i4, d14, d15, d16, 0.6D);
        double d23 = getCornerNoise3D(i5, d17, d18, d19, 0.6D);

        return 32.0D * (d20 + d21 + d22 + d23);
    }
}
