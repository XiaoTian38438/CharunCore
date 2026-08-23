package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class ItemDespawnEvent extends Event {
    private final com.CharunCore.server.world.entity.ItemEntity item;

    public ItemDespawnEvent(com.CharunCore.server.world.entity.ItemEntity item) { this.item=item; }

    public com.CharunCore.server.world.entity.ItemEntity getItem() { return item; }
}
