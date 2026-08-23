package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerAnimationEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;

    public PlayerAnimationEvent(com.CharunCore.server.network.NetworkHandler player) { this.player=player; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
}
