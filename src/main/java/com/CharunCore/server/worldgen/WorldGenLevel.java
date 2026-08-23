package com.CharunCore.server.worldgen;

import com.CharunCore.server.worldgen.structure2.Mirror;
import com.CharunCore.server.worldgen.structure2.Rotation;
import com.CharunCore.server.worldgen.structure2.StructureTemplate;
import com.CharunCore.server.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldGenLevel {

    public static final int WRITE_RADIUS = 1;

    private final int centerCX;
    private final int centerCZ;
    private final Map<Long, Chunk> window;
    private final long seed;
    private final int seaLevel;
    private final int minY;
    private final int height;

    /** 结构 data markers（structure_block 方块, 含 metadata）——生成完成后由
     *  StructureMarkerProcessor 统一处理（Chest→战利品 / Sentry→潜影贝 / Elytra→鞘翅）。 */
    private final List<StructureTemplate.DataMarker>
        dataMarkers = new ArrayList<>();

    public WorldGenLevel(int centerCX, int centerCZ, Map<Long, Chunk> window,
                         long seed, int seaLevel, int minY, int height) {
        this.centerCX = centerCX;
        this.centerCZ = centerCZ;
        this.window = window;
        this.seed = seed;
        this.seaLevel = seaLevel;
        this.minY = minY;
        this.height = height;
    }

    public void addDataMarker(int x, int y, int z, String metadata,
                              Rotation rotation,
                              Mirror mirror) {
        dataMarkers.add(new StructureTemplate.DataMarker(
            x, y, z, metadata, rotation, mirror));
    }

    public List<StructureTemplate.DataMarker>
        getDataMarkers() {
        return dataMarkers;
    }

    private static long chunkKey(int cx, int cz) {
        return ((long) cx << 32) | (cz & 0xFFFFFFFFL);
    }

    public Chunk getChunkByCoord(int cx, int cz) {
        Chunk c = window.get(chunkKey(cx, cz));
        if (c == null) {
            throw new IllegalStateException("Requested chunk (" + cx + "," + cz
                + ") unavailable during world generation");
        }
        return c;
    }

    public Chunk getChunk(int absBlockX, int absBlockZ) {
        return getChunkByCoord(absBlockX >> 4, absBlockZ >> 4);
    }

    public boolean ensureCanWrite(int absBlockX, int absBlockZ) {
        int cx = absBlockX >> 4;
        int cz = absBlockZ >> 4;
        return Math.abs(cx - centerCX) <= WRITE_RADIUS
            && Math.abs(cz - centerCZ) <= WRITE_RADIUS;
    }

    public boolean setBlock(int absX, int y, int absZ, int stateId) {
        if (!ensureCanWrite(absX, absZ)) return false;
        Chunk c = getChunk(absX, absZ);
        int rx = absX & 15;
        int rz = absZ & 15;
        c.setBlock(rx, y, rz, stateId);
        return true;
    }

    public void setBlockEntity(int absX, int y, int absZ, org.cloudburstmc.nbt.NbtMap nbt) {
        if (!ensureCanWrite(absX, absZ)) return;
        Chunk c = getChunk(absX, absZ);
        int rx = absX & 15;
        int rz = absZ & 15;
        c.setBlockEntity(rx, y, rz, nbt);
    }

    public int getBlock(int absX, int y, int absZ) {
        int cx = absX >> 4;
        int cz = absZ >> 4;
        Chunk c = window.get(chunkKey(cx, cz));
        if (c == null) return 0;
        int rx = absX & 15;
        int rz = absZ & 15;
        return c.getBlock(rx, y, rz);
    }

    public int getHeight(int absBlockX, int absBlockZ) {
        Chunk c = getChunk(absBlockX, absBlockZ);
        int rx = absBlockX & 15;
        int rz = absBlockZ & 15;
        int[] hm = c.getHeightmapWorldSurface();
        return hm[rz * 16 + rx];
    }

    public long getSeed() { return seed; }
    public int getMinY() { return minY; }
    public int getHeight() { return height; }
    public int getSeaLevel() { return seaLevel; }
    public int getCenterCX() { return centerCX; }
    public int getCenterCZ() { return centerCZ; }

    public Map<Long, Chunk> getWindow() { return window; }

    public static long key(int cx, int cz) { return chunkKey(cx, cz); }
}
