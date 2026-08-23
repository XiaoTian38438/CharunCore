package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class BlockRedstoneEvent extends Event {
    private final int x;
    private final int y;
    private final int z;
    private final int oldPower;
    private final int newPower;

    public BlockRedstoneEvent(int x, int y, int z, int oldPower, int newPower) { this.x=x; this.y=y; this.z=z; this.oldPower=oldPower; this.newPower=newPower; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public int getOldPower() { return oldPower; }
    public int getNewPower() { return newPower; }
}
