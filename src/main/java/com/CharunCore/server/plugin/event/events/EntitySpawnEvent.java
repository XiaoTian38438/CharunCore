package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntitySpawnEvent extends Event {
    private final com.CharunCore.server.world.entity.Entity entity;

    public EntitySpawnEvent(com.CharunCore.server.world.entity.Entity entity) { this.entity=entity; }

    public com.CharunCore.server.world.entity.Entity getEntity() { return entity; }
}
