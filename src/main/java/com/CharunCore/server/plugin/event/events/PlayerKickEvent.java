package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerKickEvent extends Event {

    private final NetworkHandler player;
    private String reason;

    public PlayerKickEvent(NetworkHandler player, String reason) {{
        this.player = player;
        this.reason = reason;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public String getReason() {{ return reason; }}
    public void setReason(String reason) {{ this.reason = reason; }}
}
