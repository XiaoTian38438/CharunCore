package com.CharunCore.server.plugin.api;

import com.CharunCore.server.network.NetworkHandler;

/**
 * 插件 Inventory 点击上下文: 在 {@link Inventory.ClickHandler#onClick} 中使用。
 * rawSlot: 客户端槽位号(0..size-1 为菜单格, size 起为玩家背包)。
 * 默认不取消不接管 —— 未调用 setHandled 时服务端按普通容器逻辑处理(可正常拿起/放下/交换)。
 * 调用 setCancelled(true): 服务端忽略本次点击并回发全窗口刷新。
 * 调用 setHandled(true): 服务端忽略原生逻辑(插件在回调里自己改槽位后调 inventory.refresh())。
 */
public final class InventoryClickContext {

    public enum ClickType { PICKUP, PICKUP_HALF, SWAP, CLONE, THROW, QUICK_MOVE, QUICK_CRAFT, DOUBLE_CLICK }

    private final NetworkHandler handle;
    private final Inventory inventory;
    private final int rawSlot;
    private final int button;
    private final ClickType type;
    private boolean cancelled;
    private boolean handled;

    public InventoryClickContext(NetworkHandler handle, Inventory inventory, int rawSlot,
                                 int button, ClickType type) {
        this.handle = handle;
        this.inventory = inventory;
        this.rawSlot = rawSlot;
        this.button = button;
        this.type = type;
    }

    public Player getPlayer() { return Player.wrap(handle); }
    public Inventory getInventory() { return inventory; }
    /** 客户端原始槽位号; <0 为窗口外。 */
    public int getRawSlot() { return rawSlot; }
    /** 菜单内槽位号(仅当点击落在菜单区)。否则 -1。 */
    public int getSlot() { return rawSlot >= 0 && rawSlot < inventory.getSize() ? rawSlot : -1; }
    /** 点击是否落在菜单区(否则在玩家背包区)。 */
    public boolean inMenuArea() { return getSlot() >= 0; }
    public int getButton() { return button; }
    public ClickType getType() { return type; }

    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    /** 标记已接管(插件自行处理槽位), 服务端不再执行原生容器逻辑。 */
    public void setHandled(boolean handled) { this.handled = handled; }
    public boolean isHandled() { return handled; }
}
