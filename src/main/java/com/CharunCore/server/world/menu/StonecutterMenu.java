package com.CharunCore.server.world.menu;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.ContainerStore;

/**
 * 切石机逻辑 (#18)。原版 StonecutterMenu：输入改变时列出所有可行配方(group)，选中某项改输出。
 * 客户端 data 槽 0 = selectedRecipeIndex。配方组由客户端按 registry 自行列出，服务端只需响应选中。
 */
public final class StonecutterMenu {

    public static final int DATA_SELECTED = 0; // container_data index 0

    private StonecutterMenu() {}

    /** 输入变化或打开时调用：根据输入重算候选，并据 selectedIndex 设定结果槽。 */
    public static void recompute(MenuHost host, int windowId, ContainerStore.StonecutterData sd) {
        if (sd == null) return;
        int inId = sd.slots[0];
        int[] cands = inId > 0 ? MenuUtil.stonecutterResults(BlockManager.itemIdToName(inId)) : new int[0];
        sd.slots[2] = 0; sd.slots[3] = 0;
        if (cands.length == 0) {
            sd.selectedIndex = -1;
        } else if (sd.selectedIndex < 0 || sd.selectedIndex >= cands.length) {
            sd.selectedIndex = -1; // 由客户端默认选中首项
        } else {
            sd.slots[2] = cands[sd.selectedIndex];
            sd.slots[3] = 1;
        }
        sd.version++;
        host.sendContent(windowId, sd.slots, 2);
        host.sendProperty(windowId, DATA_SELECTED, sd.selectedIndex);
    }

    /** 玩家点击左侧第 index 个配方。 */
    public static void selectRecipe(MenuHost host, int windowId, ContainerStore.StonecutterData sd, int index) {
        if (sd == null) return;
        int inId = sd.slots[0];
        int[] cands = inId > 0 ? MenuUtil.stonecutterResults(BlockManager.itemIdToName(inId)) : new int[0];
        if (index < 0 || index >= cands.length) return;
        sd.selectedIndex = index;
        sd.slots[2] = cands[index];
        sd.slots[3] = 1;
        sd.version++;
        host.sendContent(windowId, sd.slots, 2);
        host.sendProperty(windowId, DATA_SELECTED, index);
    }

    /** 返回当前输入所有可行产物 id（group）。 */
    public static int[] getGroup(ContainerStore.StonecutterData sd) {
        if (sd == null || sd.slots[0] <= 0) return new int[0];
        return MenuUtil.stonecutterResults(BlockManager.itemIdToName(sd.slots[0]));
    }
}
