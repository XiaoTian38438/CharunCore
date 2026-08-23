package com.CharunCore.server.world.menu;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.ContainerStore;

import java.util.HashMap;
import java.util.Map;

/**
 * 铁砧逻辑 (#7 重命名 / #9 / #17 合并附魔)。
 * 原版 AnvilMenu.createResult(): 输出 = 左物; 改名 +1 经验; 附魔书合并取高等级; 同物修复合并耐久。
 * 取走结果(光标物品)与关闭退回输入由 NetworkHandler 负责（见回报）。
 */
public final class AnvilMenu {

    public static final int INPUT_SLOT = 0;       // 左 (slots[0/1])
    public static final int ADDITION_SLOT = 1;    // 右 (slots[2/3])
    public static final int RESULT_SLOT = 2;      // 输出 (slots[4/5])
    public static final int MAX_NAME_LENGTH = 50;
    public static final int DATA_COST = 0;        // container_data index 0 = 经验花费

    private AnvilMenu() {}

    /** 客户端发来改名文本（容器包 extra data）。 */
    public static void setName(ContainerStore.AnvilData ad, String name) {
        if (name != null && name.length() > MAX_NAME_LENGTH) name = name.substring(0, MAX_NAME_LENGTH);
        ad.rename = name;
    }

    public static void recompute(MenuHost host, int windowId, ContainerStore.AnvilData ad) {
        if (ad == null) return;
        int leftId = ad.slots[0], leftCount = ad.slots[1];
        int rightId = ad.slots[2], rightCount = ad.slots[3];
        ad.slots[4] = 0; ad.slots[5] = 0; ad.cost = 0;
        ad.outId = 0; ad.outCount = 0; ad.outEnchants = new HashMap<>();
        ad.outName = null; ad.outDamage = 0; ad.outPotion = null;

        if (leftId <= 0) {
            ad.version++;
            host.sendContent(windowId, ad.slots, 3);
            host.sendProperty(windowId, DATA_COST, 0);
            return;
        }

        String leftName = BlockManager.itemIdToName(leftId);
        String rightName = rightId > 0 ? BlockManager.itemIdToName(rightId) : null;

        int outId = leftId;
        int outCount = leftCount;
        int outDamage = ad.leftDamage;
        String outPotion = ad.leftPotion;
        Map<Integer, Integer> outEnch = new HashMap<>(ad.leftEnchants);
        int cost = 1; // 原版基础成本

        // 1) 重命名
        if (ad.rename != null && !ad.rename.isEmpty()) {
            ad.outName = ad.rename;
            cost += 1;
        }

        // 2) 附魔书 / 附魔物品合并（冲突取高等级）
        boolean rightIsBook = "enchanted_book".equals(rightName);
        if (rightId > 0 && (rightIsBook || !ad.rightEnchants.isEmpty())) {
            for (Map.Entry<Integer, Integer> e : ad.rightEnchants.entrySet()) {
                int id = e.getKey(), lvl = e.getValue();
                int cur = outEnch.getOrDefault(id, 0);
                outEnch.put(id, cur == 0 ? lvl : Math.max(cur, lvl));
            }
            cost += rightIsBook ? (1 + outEnch.size()) : 2;
        }

        // 3) 同种可损伤物品修复合并（数量恒为 1）
        if (rightId > 0 && rightId == leftId && !rightIsBook) {
            int maxDmg = MenuUtil.getMaxDurability(leftName);
            if (maxDmg > 0) {
                int repaired = (maxDmg - ad.leftDamage) + (maxDmg - ad.rightDamage) + (int) (maxDmg * 0.12);
                repaired = Math.min(maxDmg, repaired);
                ad.outDamage = maxDmg - repaired;
                outCount = 1;
                cost += 2;
            } else {
                outCount = Math.min(64, leftCount + rightCount);
                cost += 2;
            }
        }

        boolean changed = ad.outName != null
                || !outEnch.equals(ad.leftEnchants)
                || (rightId > 0 && rightId == leftId && !rightIsBook)
                || outDamage != ad.leftDamage;

        if (changed) {
            ad.outId = outId;
            ad.outCount = outCount;
            ad.outEnchants = outEnch;
            ad.outDamage = outDamage;
            ad.outPotion = outPotion;
            ad.slots[4] = outId;
            ad.slots[5] = outCount;
            ad.cost = Math.max(1, Math.min(40, cost));
        }
        ad.version++;
        host.sendContent(windowId, ad.slots, 3);
        host.sendProperty(windowId, DATA_COST, ad.cost);
    }
}
