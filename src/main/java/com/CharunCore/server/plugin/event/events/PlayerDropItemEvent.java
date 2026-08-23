package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerDropItemEvent extends Event {

    private final NetworkHandler player;
    private final int itemId, count;

    public PlayerDropItemEvent(NetworkHandler player, int itemId, int count) {{
        this.player = player;
        this.itemId = itemId;
        this.count = count;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public int getItemId() {{ return itemId; }}
    public int getCount() {{ return count; }}
}
