package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class ProjectileHitEvent extends Event {
    private final com.CharunCore.server.world.entity.Entity projectile;
    private final int x;
    private final int y;
    private final int z;

    public ProjectileHitEvent(com.CharunCore.server.world.entity.Entity projectile, int x, int y, int z) { this.projectile=projectile; this.x=x; this.y=y; this.z=z; }

    public com.CharunCore.server.world.entity.Entity getProjectile() { return projectile; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
}
