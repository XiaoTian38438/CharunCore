package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class LightningStrikeEvent extends Event {
    private final float x;
    private final float y;
    private final float z;

    public LightningStrikeEvent(float x, float y, float z) { this.x=x; this.y=y; this.z=z; }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
}
