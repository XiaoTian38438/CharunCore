package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.world.gen.SimplexNoise;
import com.CharunCore.server.worldgen.density.DensityFunction;

public final class EndIslandDensityFunction implements DensityFunction {
    private static final float ISLAND_THRESHOLD = -0.9f;
    private final SimplexNoise islandNoise;

    public EndIslandDensityFunction(long seed) {
        LegacyRandomSource legacyRandomSource = new LegacyRandomSource(seed);
        legacyRandomSource.consumeCount(17292);
        this.islandNoise = new SimplexNoise(legacyRandomSource);
    }

    @Override
    public double compute(FunctionContext ctx) {
        return (getHeightValue(islandNoise, ctx.blockX() / 8, ctx.blockZ() / 8) - 8.0) / 128.0;
    }

    @Override
    public void fillArray(double[] array, ContextProvider provider) {
        for (int i = 0; i < array.length; i++) array[i] = compute(provider.forIndex(i));
    }

    @Override
    public EndIslandDensityFunction mapAll(Visitor visitor) { return this; }
    @Override
    public double minValue() { return -0.84375; }
    @Override
    public double maxValue() { return 0.5625; }

    private static float getHeightValue(SimplexNoise n, int nx, int nz) {
        int n3 = nx / 2;
        int n4 = nz / 2;
        int n5 = nx % 2;
        int n6 = nz % 2;
        float f = 100.0f - (float) Math.sqrt(nx * nx + nz * nz) * 8.0f;
        f = Math.max(-100.0f, Math.min(80.0f, f));
        for (int i = -12; i <= 12; i++) {
            for (int j = -12; j <= 12; j++) {
                long l = n3 + i;
                long l2 = n4 + j;
                if (l * l + l2 * l2 <= 4096L) continue;
                if (!(n.getValue(l, l2) < (double) ISLAND_THRESHOLD)) continue;
                float f2 = (Math.abs(l) * 3439.0f + Math.abs(l2) * 147.0f) % 13.0f + 9.0f;
                float f3 = n5 - i * 2;
                float f4 = n6 - j * 2;
                float f5 = 100.0f - (float) Math.sqrt(f3 * f3 + f4 * f4) * f2;
                f5 = Math.max(-100.0f, Math.min(80.0f, f5));
                f = Math.max(f, f5);
            }
        }
        return f;
    }
}
