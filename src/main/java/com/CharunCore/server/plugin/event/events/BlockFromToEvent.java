package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class BlockFromToEvent extends Event {
    private final int x;
    private final int y;
    private final int z;
    private final int toX;
    private final int toY;
    private final int toZ;

    public BlockFromToEvent(int x, int y, int z, int toX, int toY, int toZ) { this.x=x; this.y=y; this.z=z; this.toX=toX; this.toY=toY; this.toZ=toZ; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public int getToX() { return toX; }
    public int getToY() { return toY; }
    public int getToZ() { return toZ; }
}
