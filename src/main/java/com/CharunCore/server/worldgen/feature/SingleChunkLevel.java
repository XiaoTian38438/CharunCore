package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.WorldGenLevel;
import java.util.HashMap;
import java.util.Map;

public class SingleChunkLevel extends WorldGenLevel {
    private final Chunk chunk;

    public SingleChunkLevel(Chunk chunk, int chunkX, int chunkZ) {
        super(chunkX, chunkZ, new HashMap<>(), 0L, 63, -64, 384);
        this.chunk = chunk;
        Map<Long, Chunk> win = getWindow();
        win.put(key(chunkX, chunkZ), chunk);
    }

    @Override
    public boolean setBlock(int absX, int y, int absZ, int stateId) {
        int cx = absX >> 4;
        int cz = absZ >> 4;
        if (cx != getCenterCX() || cz != getCenterCZ()) return false;
        int rx = absX & 15;
        int rz = absZ & 15;
        if (y < -64 || y > 319) return false;
        chunk.setBlock(rx, y, rz, stateId);
        return true;
    }

    @Override
    public int getBlock(int absX, int y, int absZ) {
        int cx = absX >> 4;
        int cz = absZ >> 4;
        if (cx != getCenterCX() || cz != getCenterCZ()) return 0;
        int rx = absX & 15;
        int rz = absZ & 15;
        if (y < -64 || y > 319) return 0;
        return chunk.getBlock(rx, y, rz);
    }

    @Override
    public boolean ensureCanWrite(int absBlockX, int absBlockZ) {
        int cx = absBlockX >> 4;
        int cz = absBlockZ >> 4;
        return cx == getCenterCX() && cz == getCenterCZ();
    }
}
