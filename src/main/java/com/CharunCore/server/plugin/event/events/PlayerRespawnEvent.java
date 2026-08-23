package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerRespawnEvent extends Event {

    private final NetworkHandler player;
    private double x, y, z;

    public PlayerRespawnEvent(NetworkHandler player, double x, double y, double z) {{
        this.player = player;
        this.x = x; this.y = y; this.z = z;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public double getX() {{ return x; }}
    public double getY() {{ return y; }}
    public double getZ() {{ return z; }}
    public void setRespawnPosition(double x, double y, double z) {{ this.x = x; this.y = y; this.z = z; }}
}
