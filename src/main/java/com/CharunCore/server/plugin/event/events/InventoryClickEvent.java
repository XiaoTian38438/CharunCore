package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class InventoryClickEvent extends Event {

    private final NetworkHandler player;
    private final int windowId;
    private final int slot;
    private final int clickType;
    private final int button;

    public InventoryClickEvent(NetworkHandler player, int windowId, int slot, int clickType, int button) {{
        this.player = player;
        this.windowId = windowId;
        this.slot = slot;
        this.clickType = clickType;
        this.button = button;
    }}

    public NetworkHandler getPlayer() {{ return player; }}
    public int getWindowId() {{ return windowId; }}
    public int getSlot() {{ return slot; }}
    public int getClickType() {{ return clickType; }}
    public int getButton() {{ return button; }}
}
