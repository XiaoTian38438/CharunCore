package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerGameModeChangeEvent extends Event {

    private final NetworkHandler player;
    private final int oldGameMode;
    private int newGameMode;

    public PlayerGameModeChangeEvent(NetworkHandler player, int oldGameMode, int newGameMode) {{
        this.player = player;
        this.oldGameMode = oldGameMode;
        this.newGameMode = newGameMode;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public int getOldGameMode() {{ return oldGameMode; }}
    public int getNewGameMode() {{ return newGameMode; }}
    public void setNewGameMode(int newGameMode) {{ this.newGameMode = newGameMode; }}
}
