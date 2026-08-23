package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerDeathEvent extends Event {

    private final NetworkHandler player;
    private String deathMessage;
    private boolean keepInventory;

    public PlayerDeathEvent(NetworkHandler player, String deathMessage, boolean keepInventory) {{
        this.player = player;
        this.deathMessage = deathMessage;
        this.keepInventory = keepInventory;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public String getDeathMessage() {{ return deathMessage; }}
    public void setDeathMessage(String deathMessage) {{ this.deathMessage = deathMessage; }}
    public boolean isKeepInventory() {{ return keepInventory; }}
    public void setKeepInventory(boolean keepInventory) {{ this.keepInventory = keepInventory; }}
}
