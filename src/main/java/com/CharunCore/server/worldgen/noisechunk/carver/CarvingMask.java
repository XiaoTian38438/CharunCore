package com.CharunCore.server.worldgen.noisechunk.carver;

import java.util.BitSet;

public final class CarvingMask {
    private final BitSet mask;
    private final int minY;
    private final int height;

    public CarvingMask(int height, int minY) {
        this.minY = minY;
        this.height = height;
        this.mask = new BitSet(16 * 16 * height);
    }

    public void set(int x, int y, int z) {
        this.mask.set(index(x, y, z));
    }

    public boolean get(int x, int y, int z) {
        return this.mask.get(index(x, y, z));
    }

    private int index(int x, int y, int z) {
        return (x & 0xF) | ((z & 0xF) << 4) | ((y - minY) << 8);
    }
}
