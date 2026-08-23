package com.CharunCore.server.worldgen.noisechunk;

/**
 * 原版 net.minecraft.world.level.ChunkPos 的简化版 — 只保留 NoiseChunk 用到的 asLong/getX/getZ/INVALID。
 */
public final class ChunkPos {
    public static final int INVALID_CHUNK_POS = Integer.MIN_VALUE;

    private final int x;
    private final int z;

    public ChunkPos(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int x() { return x; }
    public int z() { return z; }

    public int getMinBlockX() { return x << 4; }
    public int getMinBlockZ() { return z << 4; }

    public static long asLong(int x, int z) {
        return (long) x & 4294967295L | ((long) z & 4294967295L) << 32;
    }

    public static int getX(long l) {
        return (int) (l & 4294967295L);
    }

    public static int getZ(long l) {
        return (int) (l >>> 32 & 4294967295L);
    }

    public static int blockToSectionCoord(int block) {
        return block >> 4;
    }
}
