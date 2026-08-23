package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class FoodLevelChangeEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final int foodLevel;

    public FoodLevelChangeEvent(com.CharunCore.server.network.NetworkHandler player, int foodLevel) { this.player=player; this.foodLevel=foodLevel; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public int getFoodLevel() { return foodLevel; }
}
