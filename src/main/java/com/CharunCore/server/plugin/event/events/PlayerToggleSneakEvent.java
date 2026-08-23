package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerToggleSneakEvent extends Event {

    private final NetworkHandler player;
    private final boolean sneaking;

    public PlayerToggleSneakEvent(NetworkHandler player, boolean sneaking) {{
        this.player = player;
        this.sneaking = sneaking;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public boolean isSneaking() {{ return sneaking; }}
}
