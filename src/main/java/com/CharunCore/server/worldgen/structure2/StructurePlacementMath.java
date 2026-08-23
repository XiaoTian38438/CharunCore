package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.world.gen.RandomSource;

public final class StructurePlacementMath {

    private StructurePlacementMath() {}

    public static RandomSource withLargeFeatureSeed(long levelSeed, int chunkX, int chunkZ) {
        LegacyRandomSource rng = new LegacyRandomSource(levelSeed);
        long l2 = rng.nextLong();
        long l3 = rng.nextLong();
        long l4 = (long) chunkX * l2 ^ (long) chunkZ * l3 ^ levelSeed;
        rng.setSeed(l4);
        return rng;
    }

    public static RandomSource withLargeFeatureWithSalt(long levelSeed, int gridX, int gridZ, int salt) {
        long l2 = (long) gridX * 341873128712L + (long) gridZ * 132897987541L + levelSeed + (long) salt;
        LegacyRandomSource rng = new LegacyRandomSource(0L);
        rng.setSeed(l2);
        return rng;
    }

    public static int floorDiv(int a, int b) {
        int q = a / b;
        if (a < 0 && b * q != a) q--;
        return q;
    }
}
