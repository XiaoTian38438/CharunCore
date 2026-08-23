package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityRegainHealthEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final float amount;

    public EntityRegainHealthEvent(com.CharunCore.server.network.NetworkHandler player, float amount) { this.player=player; this.amount=amount; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public float getAmount() { return amount; }
}
