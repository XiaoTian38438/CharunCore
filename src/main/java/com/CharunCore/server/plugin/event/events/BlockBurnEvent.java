package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class BlockBurnEvent extends Event {
    private final int x;
    private final int y;
    private final int z;
    private final int blockStateId;

    public BlockBurnEvent(int x, int y, int z, int blockStateId) { this.x=x; this.y=y; this.z=z; this.blockStateId=blockStateId; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public int getBlockStateId() { return blockStateId; }
}
