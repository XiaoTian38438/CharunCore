package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class VehicleEnterEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final com.CharunCore.server.world.entity.Entity vehicle;

    public VehicleEnterEvent(com.CharunCore.server.network.NetworkHandler player, com.CharunCore.server.world.entity.Entity vehicle) { this.player=player; this.vehicle=vehicle; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public com.CharunCore.server.world.entity.Entity getVehicle() { return vehicle; }
}
