package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerLevelChangeEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final int oldLevel;
    private final int newLevel;

    public PlayerLevelChangeEvent(com.CharunCore.server.network.NetworkHandler player, int oldLevel, int newLevel) { this.player=player; this.oldLevel=oldLevel; this.newLevel=newLevel; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public int getOldLevel() { return oldLevel; }
    public int getNewLevel() { return newLevel; }
}
