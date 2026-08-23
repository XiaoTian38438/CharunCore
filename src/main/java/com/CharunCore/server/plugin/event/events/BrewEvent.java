package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class BrewEvent extends Event {
    private final int x;
    private final int y;
    private final int z;

    public BrewEvent(int x, int y, int z) { this.x=x; this.y=y; this.z=z; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
}
