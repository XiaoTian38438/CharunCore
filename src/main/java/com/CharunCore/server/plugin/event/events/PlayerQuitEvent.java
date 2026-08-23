package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerQuitEvent extends Event {

    private final NetworkHandler player;
    private String quitMessage;

    public PlayerQuitEvent(NetworkHandler player, String quitMessage) {{
        this.player = player;
        this.quitMessage = quitMessage;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public String getQuitMessage() {{ return quitMessage; }}
    public void setQuitMessage(String quitMessage) {{ this.quitMessage = quitMessage; }}
}
