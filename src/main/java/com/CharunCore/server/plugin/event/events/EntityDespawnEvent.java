package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityDespawnEvent extends Event {
    private final com.CharunCore.server.world.entity.Entity entity;

    public EntityDespawnEvent(com.CharunCore.server.world.entity.Entity entity) { this.entity=entity; }

    public com.CharunCore.server.world.entity.Entity getEntity() { return entity; }
}
