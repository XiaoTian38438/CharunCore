package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerShearEntityEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final com.CharunCore.server.world.entity.Entity entity;

    public PlayerShearEntityEvent(com.CharunCore.server.network.NetworkHandler player, com.CharunCore.server.world.entity.Entity entity) { this.player=player; this.entity=entity; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public com.CharunCore.server.world.entity.Entity getEntity() { return entity; }
}
