package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityPickupItemEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final com.CharunCore.server.world.entity.ItemEntity item;

    public EntityPickupItemEvent(com.CharunCore.server.network.NetworkHandler player, com.CharunCore.server.world.entity.ItemEntity item) { this.player=player; this.item=item; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public com.CharunCore.server.world.entity.ItemEntity getItem() { return item; }
}
