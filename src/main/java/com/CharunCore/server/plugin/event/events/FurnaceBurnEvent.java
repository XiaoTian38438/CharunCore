package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class FurnaceBurnEvent extends Event {
    private final int x;
    private final int y;
    private final int z;
    private final String fuel;
    private final int burnTime;

    public FurnaceBurnEvent(int x, int y, int z, String fuel, int burnTime) { this.x=x; this.y=y; this.z=z; this.fuel=fuel; this.burnTime=burnTime; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public String getFuel() { return fuel; }
    public int getBurnTime() { return burnTime; }
}
