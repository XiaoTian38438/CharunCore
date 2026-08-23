package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityShootBowEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final float force;

    public EntityShootBowEvent(com.CharunCore.server.network.NetworkHandler player, float force) { this.player=player; this.force=force; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public float getForce() { return force; }
}
