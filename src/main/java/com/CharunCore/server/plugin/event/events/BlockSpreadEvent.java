package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class BlockSpreadEvent extends Event {
    private final int x;
    private final int y;
    private final int z;
    private final int newStateId;

    public BlockSpreadEvent(int x, int y, int z, int newStateId) { this.x=x; this.y=y; this.z=z; this.newStateId=newStateId; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public int getNewStateId() { return newStateId; }
}
