package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerExpChangeEvent extends Event {

    private final NetworkHandler player;
    private int amount;

    public PlayerExpChangeEvent(NetworkHandler player, int amount) {{
        this.player = player;
        this.amount = amount;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public int getAmount() {{ return amount; }}
    public void setAmount(int amount) {{ this.amount = amount; }}
}
