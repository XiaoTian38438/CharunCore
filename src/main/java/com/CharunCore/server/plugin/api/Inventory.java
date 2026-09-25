package com.CharunCore.server.plugin.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;

/**
 * 插件自定义容器界面(对标 Paper Inventory): 9/18/27/36/45/54 格, 自定义标题,
 * 支持点击回调(装饰格取消+接管, 物品格放行原生交互)、关闭回调、实时刷新。
 * 创建: Server.get().createInventory(size, title) → player.openInventory(inv)。
 */
public final class Inventory {

    public interface ClickHandler { void onClick(InventoryClickContext ctx); }
    public interface CloseHandler { void onClose(Player player); }

    private final int size;
    private volatile String title;
    private final int[] itemIds;
    private final int[] itemCounts;
    private final ItemMeta[] metas;
    private ClickHandler clickHandler;
    private CloseHandler closeHandler;
    private final Map<Integer, NetworkHandler> openWindows = new ConcurrentHashMap<>();
    public final CopyOnWriteArraySet<NetworkHandler> viewers = new CopyOnWriteArraySet<>();

    public Inventory(int size, String title) {
        if (size <= 0 || size % 9 != 0 || size > 54) {
            throw new IllegalArgumentException("Inventory 大小必须为 9/18/27/36/45/54: " + size);
        }
        this.size = size;
        this.title = title == null ? "Inventory" : title;
        this.itemIds = new int[size];
        this.itemCounts = new int[size];
        this.metas = new ItemMeta[size];
    }

    public int getSize() { return size; }
    public String getTitle() { return title; }
    public void setTitle(String t) {
        this.title = t == null ? "Inventory" : t;
        for (NetworkHandler h : openWindows.values()) h.refreshPluginWindowTitle(this);
    }

    // ── 槽位读写 ────────────────────────────────────────────────

    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= size || itemIds[slot] <= 0 || itemCounts[slot] <= 0) return null;
        return new ItemStack(itemIds[slot], itemCounts[slot], metas[slot]);
    }

    /** 设置槽位; item 为 null 清空。自动刷新所有查看者。 */
    public void setItem(int slot, ItemStack item) {
        if (slot < 0 || slot >= size) return;
        if (item == null || item.isEmpty()) {
            itemIds[slot] = 0;
            itemCounts[slot] = 0;
            metas[slot] = null;
        } else {
            itemIds[slot] = item.itemId();
            itemCounts[slot] = item.count();
            metas[slot] = item.hasItemMeta() ? item.meta() : null;
        }
        refresh();
    }

    /** 填充一整行装饰物(每 9 格一行)。 */
    public void fillRow(int row, ItemStack item) {
        if (row < 0 || row >= size / 9) return;
        for (int i = row * 9; i < row * 9 + 9; i++) setItemQuiet(i, item);
        refresh();
    }

    /** 全部填充(常用于背景玻璃板)。 */
    public void fill(ItemStack item) {
        for (int i = 0; i < size; i++) setItemQuiet(i, item);
        refresh();
    }

    /** 静默设置(不触发刷新, 用于批量填充)。 */
    private void setItemQuiet(int slot, ItemStack item) {
        if (slot < 0 || slot >= size) return;
        if (item == null || item.isEmpty()) {
            itemIds[slot] = 0; itemCounts[slot] = 0; metas[slot] = null;
        } else {
            itemIds[slot] = item.itemId();
            itemCounts[slot] = item.count();
            metas[slot] = item.hasItemMeta() ? item.meta() : null;
        }
    }

    /** 追加物品到第一个空位(不合并堆叠), 成功返回槽位, 满则 -1。 */
    public int addItem(ItemStack item) {
        if (item == null || item.isEmpty()) return -1;
        for (int i = 0; i < size; i++) {
            if (itemIds[i] <= 0) {
                setItemQuiet(i, item);
                refresh();
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        java.util.Arrays.fill(itemIds, 0);
        java.util.Arrays.fill(itemCounts, 0);
        java.util.Arrays.fill(metas, null);
        refresh();
    }

    public int firstEmpty() {
        for (int i = 0; i < size; i++) if (itemIds[i] <= 0) return i;
        return -1;
    }

    // ── 回调 ────────────────────────────────────────────────────

    /** 注册点击回调(每次点击菜单区域时触发; 点击玩家背包区域仅触发回调, 不接管)。 */
    public Inventory onClick(ClickHandler handler) { this.clickHandler = handler; return this; }

    /** 注册关闭回调(玩家关闭窗口或窗口被服务端关闭时触发)。 */
    public Inventory onClose(CloseHandler handler) { this.closeHandler = handler; return this; }

    void fireClick(InventoryClickContext ctx) {
        if (clickHandler != null) {
            try { clickHandler.onClick(ctx); }
            catch (Throwable t) { System.err.println("[插件菜单] 点击回调异常: " + t); }
        }
    }

    /** 底层接线: 窗口关闭时回调(先清回调注册再触发)。 */
    public void fireClose(NetworkHandler who) {
        if (closeHandler != null) {
            try { closeHandler.onClose(Player.wrap(who)); }
            catch (Throwable t) { System.err.println("[插件菜单] 关闭回调异常: " + t); }
        }
    }

    // ── 查看者 ──────────────────────────────────────────────────

    /** 当前打开该菜单的玩家数。 */
    public int getViewersCount() { return openWindows.size(); }

    public List<Player> getViewers() {
        List<Player> out = new ArrayList<>();
        openWindows.values().forEach(h -> out.add(Player.wrap(h)));
        return out;
    }

    Map<Integer, NetworkHandler> openWindows() { return openWindows; }

    /** 底层接线: 查询某玩家当前打开的本菜单 windowId(-1=未打开)。 */
    public int windowIdOf(NetworkHandler viewer) {
        for (Map.Entry<Integer, NetworkHandler> e : openWindows.entrySet()) {
            if (e.getValue() == viewer) return e.getKey();
        }
        return -1;
    }

    /** 全量刷新所有查看者的窗口内容(0x12 container_set_content)。 */
    public void refresh() {
        for (NetworkHandler h : openWindows.values()) h.refreshPluginMenu(this);
    }

    /** 强制关闭所有查看者的窗口。 */
    public void closeAll() {
        for (NetworkHandler h : openWindows.values()) h.closePluginMenu(this);
    }

    /** 快捷构造装饰物(灰色玻璃板等)。 */
    public static ItemStack decor(String blockName, String displayName) {
        ItemStack it = ItemStack.of(blockName, 1);
        if (it != null && displayName != null) {
            it = it.withMeta(new ItemMeta().setDisplayName(displayName));
        }
        return it;
    }

    /** 底层接线: 槽位原始数据。 */
    public int slotId(int slot) { return slot >= 0 && slot < size ? itemIds[slot] : 0; }
    public int slotCount(int slot) { return slot >= 0 && slot < size ? itemCounts[slot] : 0; }
    public ItemMeta slotMeta(int slot) { return slot >= 0 && slot < size ? metas[slot] : null; }
    /** 底层接线: 注册打开窗口(windowId → viewer)。 */
    public void registerOpenWindow(int windowId, NetworkHandler viewer) {
        openWindows.put(windowId, viewer);
        viewers.add(viewer);
    }
    /** 底层接线: 移除打开窗口。 */
    public void unregisterOpenWindow(int windowId, NetworkHandler viewer) {
        openWindows.remove(windowId);
        viewers.remove(viewer);
    }
    /** 底层接线: 直接触发点击回调。 */
    public void dispatchClick(InventoryClickContext ctx) { fireClick(ctx); }
    /** 底层接线: 直接写入槽位(不触发刷新)。 */
    public void writeSlotRaw(int slot, int id, int count, ItemMeta meta) {
        if (slot < 0 || slot >= size) return;
        itemIds[slot] = Math.max(0, id);
        itemCounts[slot] = Math.max(0, count);
        metas[slot] = (meta == null || meta.isEmpty()) ? null : meta;
    }
    /** 底层接线: 清槽。 */
    public void clearSlotRaw(int slot) {
        if (slot < 0 || slot >= size) return;
        itemIds[slot] = 0;
        itemCounts[slot] = 0;
        metas[slot] = null;
    }

    /** 供底层序列化: 附魔名→id 转换由 BlockManager.getEnchantId 完成。 */
    static int enchantId(String name) { return BlockManager.getEnchantId(name); }
}
