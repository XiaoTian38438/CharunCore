package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerToggleFlightEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final boolean flying;

    public PlayerToggleFlightEvent(com.CharunCore.server.network.NetworkHandler player, boolean flying) { this.player=player; this.flying=flying; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public boolean isFlying() { return flying; }
}
