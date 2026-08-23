package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class InventoryCloseEvent extends Event {

    private final NetworkHandler player;
    private final int windowId;

    public InventoryCloseEvent(NetworkHandler player, int windowId) {{
        this.player = player;
        this.windowId = windowId;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public int getWindowId() {{ return windowId; }}
}
