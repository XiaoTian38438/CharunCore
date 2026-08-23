package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerToggleSprintEvent extends Event {

    private final NetworkHandler player;
    private final boolean sprinting;

    public PlayerToggleSprintEvent(NetworkHandler player, boolean sprinting) {{
        this.player = player;
        this.sprinting = sprinting;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public boolean isSprinting() {{ return sprinting; }}
}
