package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class PlayerInteractEvent extends Event {

    public enum Action { LEFT_CLICK, RIGHT_CLICK, RIGHT_CLICK_AIR }
    private final NetworkHandler player;
    private final Action action;
    private final int x, y, z, blockStateId, heldItemId;

    public PlayerInteractEvent(NetworkHandler player, Action action, int x, int y, int z,
                               int blockStateId, int heldItemId) {{
        this.player = player;
        this.action = action;
        this.x = x; this.y = y; this.z = z;
        this.blockStateId = blockStateId;
        this.heldItemId = heldItemId;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public Action getAction() {{ return action; }}
    public int getX() {{ return x; }}
    public int getY() {{ return y; }}
    public int getZ() {{ return z; }}
    public int getBlockStateId() {{ return blockStateId; }}
    public int getHeldItemId() {{ return heldItemId; }}
}
