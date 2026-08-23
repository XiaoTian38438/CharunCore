package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class ItemSpawnEvent extends Event {
    private final com.CharunCore.server.world.entity.ItemEntity item;

    public ItemSpawnEvent(com.CharunCore.server.world.entity.ItemEntity item) { this.item=item; }

    public com.CharunCore.server.world.entity.ItemEntity getItem() { return item; }
}
