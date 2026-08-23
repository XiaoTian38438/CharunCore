/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.attribute;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class GaussianSampler {
    private static final int GAUSSIAN_SAMPLE_RADIUS = 2;
    private static final int GAUSSIAN_SAMPLE_BREADTH = 6;
    private static final double[] GAUSSIAN_SAMPLE_KERNEL = new double[]{0.0, 1.0, 4.0, 6.0, 4.0, 1.0, 0.0};

    public static <V> void sample(Vec3 vec3, Sampler<V> sampler, Accumulator<V> accumulator) {
        vec3 = vec3.subtract(0.5, 0.5, 0.5);
        int n = Mth.floor(vec3.x());
        int n2 = Mth.floor(vec3.y());
        int n3 = Mth.floor(vec3.z());
        double d = vec3.x() - (double)n;
        double d2 = vec3.y() - (double)n2;
        double d3 = vec3.z() - (double)n3;
        for (int i = 0; i < 6; ++i) {
            double d4 = Mth.lerp(d3, GAUSSIAN_SAMPLE_KERNEL[i + 1], GAUSSIAN_SAMPLE_KERNEL[i]);
            int n4 = n3 - 2 + i;
            for (int j = 0; j < 6; ++j) {
                double d5 = Mth.lerp(d, GAUSSIAN_SAMPLE_KERNEL[j + 1], GAUSSIAN_SAMPLE_KERNEL[j]);
                int n5 = n - 2 + j;
                for (int k = 0; k < 6; ++k) {
                    double d6 = Mth.lerp(d2, GAUSSIAN_SAMPLE_KERNEL[k + 1], GAUSSIAN_SAMPLE_KERNEL[k]);
                    int n6 = n2 - 2 + k;
                    double d7 = d5 * d6 * d4;
                    V v = sampler.get(n5, n6, n4);
                    accumulator.accumulate(d7, v);
                }
            }
        }
    }

    @FunctionalInterface
    public static interface Sampler<V> {
        public V get(int var1, int var2, int var3);
    }

    @FunctionalInterface
    public static interface Accumulator<V> {
        public void accumulate(double var1, V var3);
    }
}

