package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class BlockPlaceEvent extends Event {

    private final NetworkHandler player;
    private final int x, y, z, blockStateId;

    public BlockPlaceEvent(NetworkHandler player, int x, int y, int z, int blockStateId) {{
        this.player = player;
        this.x = x; this.y = y; this.z = z;
        this.blockStateId = blockStateId;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public int getX() {{ return x; }}
    public int getY() {{ return y; }}
    public int getZ() {{ return z; }}
    public int getBlockStateId() {{ return blockStateId; }}
}
