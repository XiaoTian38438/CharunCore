package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerBedEnterEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final int x;
    private final int y;
    private final int z;

    public PlayerBedEnterEvent(com.CharunCore.server.network.NetworkHandler player, int x, int y, int z) { this.player=player; this.x=x; this.y=y; this.z=z; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
}
