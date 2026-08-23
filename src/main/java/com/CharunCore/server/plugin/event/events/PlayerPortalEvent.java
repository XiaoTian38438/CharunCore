package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerPortalEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final String fromDim;
    private final String toDim;

    public PlayerPortalEvent(com.CharunCore.server.network.NetworkHandler player, String fromDim, String toDim) { this.player=player; this.fromDim=fromDim; this.toDim=toDim; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public String getFromDim() { return fromDim; }
    public String getToDim() { return toDim; }
}
