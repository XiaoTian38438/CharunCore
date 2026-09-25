package com.CharunCore.server.plugin.api;

import com.CharunCore.server.utils.BlockManager;

/**
 * 插件侧物品: itemId + count + 可选 {@link ItemMeta}。
 * itemId 为项目内部物品注册表 id, 名称经 BlockManager 互转。
 * 兼容旧 record 用法: 构造器 (int,int) 与访问器 itemId()/count() 语义不变。
 */
public final class ItemStack {

    private final int itemId;
    private final int count;
    private ItemMeta meta;

    public ItemStack(int itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }

    public ItemStack(int itemId, int count, ItemMeta meta) {
        this.itemId = itemId;
        this.count = count;
        this.meta = meta;
    }

    public static ItemStack of(String name, int count) {
        int id = BlockManager.getItemIdByName(name);
        return id <= 0 ? null : new ItemStack(id, count);
    }

    public static ItemStack of(String name, int count, ItemMeta meta) {
        int id = BlockManager.getItemIdByName(name);
        return id <= 0 ? null : new ItemStack(id, count, meta);
    }

    public int itemId() { return itemId; }
    public int count() { return count; }

    /** 返回元数据(懒创建, 可变)。空物品返回的 meta 不生效。 */
    public ItemMeta meta() {
        if (meta == null) meta = new ItemMeta();
        return meta;
    }

    public boolean hasItemMeta() { return meta != null && !meta.isEmpty(); }

    /** 返回携带指定元数据的新 ItemStack。 */
    public ItemStack withMeta(ItemMeta meta) {
        return new ItemStack(itemId, count, meta);
    }

    public boolean isEmpty() {
        return itemId <= 0 || count <= 0;
    }

    public String name() {
        return itemId <= 0 ? "air" : BlockManager.itemIdToName(itemId);
    }

    public int maxStackSize() {
        return BlockManager.getStackSize(itemId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemStack other)) return false;
        return itemId == other.itemId && count == other.count;
    }

    @Override
    public int hashCode() {
        return 31 * itemId + count;
    }

    @Override
    public String toString() {
        return "ItemStack{" + name() + " x" + count + (hasItemMeta() ? ", meta" : "") + "}";
    }
}
