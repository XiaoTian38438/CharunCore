package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerChatEvent extends Event {

    private final NetworkHandler player;
    private String message;

    public PlayerChatEvent(NetworkHandler player, String message) {{
        this.player = player;
        this.message = message;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public String getMessage() {{ return message; }}
    public void setMessage(String message) {{ this.message = message; }}
}
