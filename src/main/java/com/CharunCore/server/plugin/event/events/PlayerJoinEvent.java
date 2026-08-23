package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerJoinEvent extends Event {

    private final NetworkHandler player;
    private String joinMessage;

    public PlayerJoinEvent(NetworkHandler player, String joinMessage) {{
        this.player = player;
        this.joinMessage = joinMessage;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public String getJoinMessage() {{ return joinMessage; }}
    public void setJoinMessage(String joinMessage) {{ this.joinMessage = joinMessage; }}
}
