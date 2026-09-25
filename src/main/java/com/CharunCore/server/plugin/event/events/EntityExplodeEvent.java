package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityExplodeEvent extends Event {
    private final double x;
    private final double y;
    private final double z;
    private float power;

    public EntityExplodeEvent(double x, double y, double z, float power) {
        this.x = x; this.y = y; this.z = z; this.power = power;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getPower() { return power; }
    public void setPower(float power) { this.power = power; }
}
