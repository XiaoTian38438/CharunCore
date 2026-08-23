package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityCombustEvent extends Event {
    private final com.CharunCore.server.world.entity.Entity entity;
    private final int duration;

    public EntityCombustEvent(com.CharunCore.server.world.entity.Entity entity, int duration) { this.entity=entity; this.duration=duration; }

    public com.CharunCore.server.world.entity.Entity getEntity() { return entity; }
    public int getDuration() { return duration; }
}
