package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerMoveEvent extends Event {

    private final NetworkHandler player;
    private final double fromX, fromY, fromZ;
    private double toX, toY, toZ;

    public PlayerMoveEvent(NetworkHandler player, double fromX, double fromY, double fromZ,
                           double toX, double toY, double toZ) {{
        this.player = player;
        this.fromX = fromX; this.fromY = fromY; this.fromZ = fromZ;
        this.toX = toX; this.toY = toY; this.toZ = toZ;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public double getFromX() {{ return fromX; }}
    public double getFromY() {{ return fromY; }}
    public double getFromZ() {{ return fromZ; }}
    public double getToX() {{ return toX; }}
    public double getToY() {{ return toY; }}
    public double getToZ() {{ return toZ; }}
    public void setTo(double x, double y, double z) {{ this.toX = x; this.toY = y; this.toZ = z; }}
}
