package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerItemConsumeEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final String itemName;

    public PlayerItemConsumeEvent(com.CharunCore.server.network.NetworkHandler player, String itemName) { this.player=player; this.itemName=itemName; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public String getItemName() { return itemName; }
}
