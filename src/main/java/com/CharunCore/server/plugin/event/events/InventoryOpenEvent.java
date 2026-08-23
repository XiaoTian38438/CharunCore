package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class InventoryOpenEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final int windowId;
    private final String containerType;

    public InventoryOpenEvent(com.CharunCore.server.network.NetworkHandler player, int windowId, String containerType) { this.player=player; this.windowId=windowId; this.containerType=containerType; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public int getWindowId() { return windowId; }
    public String getContainerType() { return containerType; }
}
