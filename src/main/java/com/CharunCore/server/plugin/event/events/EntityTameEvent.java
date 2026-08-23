package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityTameEvent extends Event {
    private final com.CharunCore.server.world.entity.Entity entity;
    private final com.CharunCore.server.network.NetworkHandler owner;

    public EntityTameEvent(com.CharunCore.server.world.entity.Entity entity, com.CharunCore.server.network.NetworkHandler owner) { this.entity=entity; this.owner=owner; }

    public com.CharunCore.server.world.entity.Entity getEntity() { return entity; }
    public com.CharunCore.server.network.NetworkHandler getOwner() { return owner; }
}
