package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerCommandPreprocessEvent extends Event {

    private final NetworkHandler player;
    private String command;

    public PlayerCommandPreprocessEvent(NetworkHandler player, String command) {{
        this.player = player;
        this.command = command;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public String getCommand() {{ return command; }}
    public void setCommand(String command) {{ this.command = command; }}
}
