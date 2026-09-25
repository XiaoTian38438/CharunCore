package com.CharunCore.server.plugin.api;

import java.util.HashMap;
import java.util.Map;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.PlayerData;

/** PlayerData 背包槽位与插件 ItemStack(含 ItemMeta) 互转工具。仅内部使用。 */
final class ItemStackBuilder {

    private ItemStackBuilder() {}

    /** 从玩家数据槽位读出带元数据的插件 ItemStack。 */
    static ItemStack fromPlayerSlot(PlayerData d, int slot) {
        int id = d.inventoryIds[slot];
        int count = d.inventoryCounts[slot];
        if (id <= 0 || count <= 0) return new ItemStack(0, 0);
        ItemMeta meta = null;
        boolean hasEnch = d.inventoryEnchants[slot] != null && !d.inventoryEnchants[slot].isEmpty();
        boolean hasName = d.inventoryCustomName[slot] != null && !d.inventoryCustomName[slot].isEmpty();
        boolean hasLore = d.inventoryLore[slot] != null && !d.inventoryLore[slot].isEmpty();
        boolean hasExt = hasLore || d.inventoryUnbreakable[slot] || d.inventoryGlint[slot] != 0;
        if (hasEnch || hasName || hasExt || d.inventoryDamage[slot] > 0) {
            meta = new ItemMeta();
            if (hasName) meta.setDisplayName(d.inventoryCustomName[slot]);
            if (d.inventoryDamage[slot] > 0) meta.setDamage(d.inventoryDamage[slot]);
            if (hasLore) {
                for (String line : d.inventoryLore[slot].split("\n", -1)) meta.addLoreLine(line);
            }
            meta.setUnbreakable(d.inventoryUnbreakable[slot]);
            meta.setGlintOverride(d.inventoryGlint[slot] == 0 ? null : d.inventoryGlint[slot] > 0);
            if (hasEnch) {
                for (Map.Entry<Integer, Integer> e : d.inventoryEnchants[slot].entrySet()) {
                    String name = BlockManager.getEnchantName(e.getKey());
                    if (name != null) meta.addEnchant(name, e.getValue());
                }
            }
        }
        return meta == null ? new ItemStack(id, count) : new ItemStack(id, count, meta);
    }

    /** 把插件 ItemStack 写入玩家数据槽位(覆盖式, 含元数据)。 */
    static void writeToPlayerSlot(PlayerData d, int slot, ItemStack item) {
        if (item == null || item.isEmpty()) {
            d.inventoryIds[slot] = 0;
            d.inventoryCounts[slot] = 0;
            d.inventoryDamage[slot] = 0;
            d.inventoryEnchants[slot].clear();
            d.inventoryPotion[slot] = null;
            d.inventoryCustomName[slot] = null;
            d.inventoryTrimMaterial[slot] = -1;
            d.inventoryTrimPattern[slot] = -1;
            d.inventoryLore[slot] = null;
            d.inventoryUnbreakable[slot] = false;
            d.inventoryGlint[slot] = 0;
            return;
        }
        d.inventoryIds[slot] = item.itemId();
        d.inventoryCounts[slot] = Math.max(1, item.count());
        d.inventoryPotion[slot] = null;
        d.inventoryTrimMaterial[slot] = -1;
        d.inventoryTrimPattern[slot] = -1;
        if (item.hasItemMeta()) {
            ItemMeta m = item.meta();
            d.inventoryCustomName[slot] = m.hasDisplayName() ? m.getDisplayName() : null;
            d.inventoryDamage[slot] = Math.max(0, m.getDamage());
            d.inventoryLore[slot] = m.hasLore() ? String.join("\n", m.getLore()) : null;
            d.inventoryUnbreakable[slot] = m.isUnbreakable();
            Boolean glint = m.getGlintOverride();
            d.inventoryGlint[slot] = glint == null ? 0 : (glint ? 1 : -1);
            d.inventoryEnchants[slot] = new HashMap<>();
            for (Map.Entry<String, Integer> e : m.getEnchants().entrySet()) {
                int enchId = BlockManager.getEnchantId(e.getKey());
                if (enchId > 0) d.inventoryEnchants[slot].put(enchId, e.getValue());
            }
        } else {
            d.inventoryCustomName[slot] = null;
            d.inventoryDamage[slot] = 0;
            d.inventoryLore[slot] = null;
            d.inventoryUnbreakable[slot] = false;
            d.inventoryGlint[slot] = 0;
            d.inventoryEnchants[slot] = new HashMap<>();
        }
    }
}
