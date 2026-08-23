package com.CharunCore.server.world.menu;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.ContainerStore;
import com.CharunCore.server.world.EnchantSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * 附魔台逻辑 (#44)。原版 EnchantmentMenu: data 槽 0-2=cost、3=seed、4-6=enchantClue、7-9=levelClue。
 * enchant() 消耗经验等级 + 青金石，把选项全部附魔写入物品并放回输入槽(槽0)。
 */
public final class EnchantmentMenu {

    public static final int DATA_COST_0 = 0;
    public static final int DATA_COST_1 = 1;
    public static final int DATA_COST_2 = 2;
    public static final int DATA_SEED = 3;
    public static final int DATA_ENCHANT_CLUE_0 = 4;
    public static final int DATA_LEVEL_CLUE_0 = 7;

    private EnchantmentMenu() {}

    public static void recompute(MenuHost host, int windowId,
                                 ContainerStore.EnchantingData ed, long seed, int xpLevel, int bookshelf) {
        if (ed == null) return;
        ed.seed = seed;
        int inId = ed.slots[0], inCount = ed.slots[1];
        if (inId <= 0 || inCount <= 0) {
            for (int i = 0; i < 3; i++) { ed.optionEnchant[i] = 0; ed.optionLevel[i] = 0; ed.optionCost[i] = 0; }
            host.sendProperty(windowId, DATA_SEED, (int) (seed & 0x7fffffff));
            for (int i = 0; i < 3; i++) {
                host.sendProperty(windowId, DATA_COST_0 + i, 0);
                host.sendProperty(windowId, DATA_ENCHANT_CLUE_0 + i, -1);
                host.sendProperty(windowId, DATA_LEVEL_CLUE_0 + i, -1);
            }
            return;
        }
        String itemName = BlockManager.itemIdToName(inId);
        EnchantSystem.Option[] opts = EnchantSystem.compute(itemName, bookshelf, xpLevel, seed);
        for (int i = 0; i < 3; i++) {
            ed.optionEnchant[i] = opts[i].enchantId;
            ed.optionLevel[i] = opts[i].level;
            ed.optionCost[i] = opts[i].cost;
            ed.optionEnchList[i] = new java.util.ArrayList<>(opts[i].enchantments);
        }
        for (int i = 0; i < 3; i++) host.sendProperty(windowId, DATA_COST_0 + i, ed.optionCost[i]);
        host.sendProperty(windowId, DATA_SEED, (int) (seed & 0x7fffffff));
        for (int i = 0; i < 3; i++) {
            host.sendProperty(windowId, DATA_ENCHANT_CLUE_0 + i, ed.optionEnchant[i]);
            host.sendProperty(windowId, DATA_LEVEL_CLUE_0 + i, ed.optionLevel[i]);
        }
    }

    /** 玩家点选第 button 个选项 (0-2)。消耗青金石(槽1) + 经验等级，结果写回输入槽。 */
    public static void enchant(MenuHost host, int windowId,
                               ContainerStore.EnchantingData ed, int button, int xpLevel, int bookshelf) {
        if (ed == null || button < 0 || button > 2) return;
        int cost = ed.optionCost[button];
        if (cost <= 0) return;
        if (!host.creative() && xpLevel < cost) return;
        int inId = ed.slots[0], inCount = ed.slots[1];
        if (inId <= 0) return;
        int lapId = ed.slots[2], lapCount = ed.slots[3];
        if (lapCount <= 0) return; // 需要青金石

        // 消耗青金石
        ed.slots[3]--;
        if (ed.slots[3] <= 0) { ed.slots[2] = 0; ed.slots[3] = 0; }
        // 消耗经验
        if (!host.creative()) host.takeXp(cost);

        // 应用选项全部附魔
        Map<Integer, Integer> ench = new HashMap<>();
        for (EnchantSystem.EnchantInstance e : ed.optionEnchList[button]) {
            if (e != null) ench.put(e.enchantId, e.level);
        }
        String inName = BlockManager.itemIdToName(inId);
        int outId = "book".equals(inName) ? BlockManager.getItemIdByName("enchanted_book") : inId;
        ed.slots[0] = outId;
        ed.slots[1] = 1;
        ed.resultEnch = ench;
        ed.version++;

        // 一次附魔后重掷（seed 由主Agent 在下次 recompute 传入新值）
        recompute(host, windowId, ed, ed.seed, xpLevel, bookshelf);
        host.sendContent(windowId, ed.slots, 3);
    }
}
