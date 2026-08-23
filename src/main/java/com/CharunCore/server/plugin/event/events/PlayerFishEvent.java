package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerFishEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final String caughtItem;

    public PlayerFishEvent(com.CharunCore.server.network.NetworkHandler player, String caughtItem) { this.player=player; this.caughtItem=caughtItem; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public String getCaughtItem() { return caughtItem; }
}
