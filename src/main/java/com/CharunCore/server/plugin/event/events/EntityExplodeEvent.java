package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityExplodeEvent extends Event {
    private final com.CharunCore.server.world.entity.Entity entity;

    public EntityExplodeEvent(com.CharunCore.server.world.entity.Entity entity) { this.entity=entity; }

    public com.CharunCore.server.world.entity.Entity getEntity() { return entity; }
}
