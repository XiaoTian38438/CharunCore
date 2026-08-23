package com.CharunCore.server.plugin.api;

import com.CharunCore.server.utils.BlockManager;

/**
 * 轻量 ItemStack: (itemId, count)。itemId 为项目内部物品注册表 id,
 * 名称经 BlockManager.itemIdToName / getItemIdByName 互转。
 */
public record ItemStack(int itemId, int count) {

    public static ItemStack of(String name, int count) {
        int id = BlockManager.getItemIdByName(name);
        return id <= 0 ? null : new ItemStack(id, count);
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
}
