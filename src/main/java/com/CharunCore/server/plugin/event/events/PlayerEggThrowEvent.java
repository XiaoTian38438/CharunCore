package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerEggThrowEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;

    public PlayerEggThrowEvent(com.CharunCore.server.network.NetworkHandler player) { this.player=player; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
}
