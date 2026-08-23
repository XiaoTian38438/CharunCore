package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class ChunkLoadEvent extends Event {
    private final int chunkX;
    private final int chunkZ;
    private final String world;

    public ChunkLoadEvent(int chunkX, int chunkZ, String world) { this.chunkX=chunkX; this.chunkZ=chunkZ; this.world=world; }

    public int getChunkX() { return chunkX; }
    public int getChunkZ() { return chunkZ; }
    public String getWorld() { return world; }
}
