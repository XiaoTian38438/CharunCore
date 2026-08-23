package com.CharunCore.server.world.menu;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.ContainerStore;

/**
 * 锻造台逻辑 (#19)。原版槽位：0=template(升级模板)、1=base(钻石装备)、2=addition(下界合金锭)、3=result。
 * 下界合金升级：template=netherite_upgrade + base=diamond_* + addition=netherite_ingot → netherite_*。
 * 结果写回槽3，盔甲架预览由客户端依结果物品自动渲染。
 */
public final class SmithingMenu {

    public static final int SLOT_TEMPLATE = 0; // slots[0/1]
    public static final int SLOT_BASE = 1;     // slots[2/3]
    public static final int SLOT_ADDITION = 2; // slots[4/5]
    public static final int SLOT_RESULT = 3;   // slots[6/7]

    private SmithingMenu() {}

    public static void recompute(MenuHost host, int windowId, ContainerStore.SmithingData sd) {
        if (sd == null) return;
        int tplId = sd.slots[0], baseId = sd.slots[2], addId = sd.slots[4];
        sd.slots[6] = 0; sd.slots[7] = 0;

        if (baseId > 0 && addId > 0) {
            String tplName = tplId > 0 ? BlockManager.itemIdToName(tplId) : null;
            String baseName = BlockManager.itemIdToName(baseId);
            String addName = BlockManager.itemIdToName(addId);
            if (MenuUtil.isSmithingTemplate(tplName) && baseName != null && baseName.startsWith("diamond_")
                    && "netherite_ingot".equals(addName)) {
                String resultName = "netherite_" + baseName.substring("diamond_".length());
                int rid = BlockManager.getItemIdByName(resultName);
                if (rid > 0) { sd.slots[6] = rid; sd.slots[7] = 1; }
            }
        }
        sd.version++;
        host.sendContent(windowId, sd.slots, 4);
    }
}
