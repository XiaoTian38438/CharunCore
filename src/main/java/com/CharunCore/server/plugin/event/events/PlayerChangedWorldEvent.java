package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerChangedWorldEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final String world;

    public PlayerChangedWorldEvent(com.CharunCore.server.network.NetworkHandler player, String world) { this.player=player; this.world=world; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public String getWorld() { return world; }
}
