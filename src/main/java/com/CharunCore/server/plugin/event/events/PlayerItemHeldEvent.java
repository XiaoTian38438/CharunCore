package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerItemHeldEvent extends Event {

    private final NetworkHandler player;
    private final int previousSlot;
    private final int newSlot;

    public PlayerItemHeldEvent(NetworkHandler player, int previousSlot, int newSlot) {{
        this.player = player;
        this.previousSlot = previousSlot;
        this.newSlot = newSlot;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public int getPreviousSlot() {{ return previousSlot; }}
    public int getNewSlot() {{ return newSlot; }}
}
