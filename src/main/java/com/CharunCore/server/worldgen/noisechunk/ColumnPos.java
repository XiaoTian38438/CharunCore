package com.CharunCore.server.worldgen.noisechunk;

/**
 * 原版 net.minecraft.server.level.ColumnPos 的简化版 — 只保留 NoiseChunk 用到的 asLong/getX/getZ。
 */
public final class ColumnPos {
    private final int x;
    private final int z;

    public ColumnPos(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int x() { return x; }
    public int z() { return z; }

    public static long asLong(int x, int z) {
        return (long) x & 4294967295L | ((long) z & 4294967295L) << 32;
    }

    public static int getX(long l) {
        return (int) (l & 4294967295L);
    }

    public static int getZ(long l) {
        return (int) (l >>> 32 & 4294967295L);
    }
}
