package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class PlayerBucketEmptyEvent extends Event {
    private final com.CharunCore.server.network.NetworkHandler player;
    private final int x;
    private final int y;
    private final int z;
    private final String bucketName;

    public PlayerBucketEmptyEvent(com.CharunCore.server.network.NetworkHandler player, int x, int y, int z, String bucketName) { this.player=player; this.x=x; this.y=y; this.z=z; this.bucketName=bucketName; }

    public com.CharunCore.server.network.NetworkHandler getPlayer() { return player; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public String getBucketName() { return bucketName; }
}
