package com.CharunCore.server.world.menu;

import com.CharunCore.server.world.ContainerStore;

/**
 * 熔炉/烟熏炉/高炉 (#8)。烧炼推进逻辑在 ContainerStore.tickFurnace（已维护 burnTime/lit、cookTime 等）。
 * 客户端据此画火焰(0,1)与进度箭头(2,3)。本类把菜单侧状态映射成 container_data 字段发往客户端。
 * 左侧配方列表来自 recipe book（客户端原生，无需服务端额外发包）。
 */
public final class AbstractFurnaceMenu {

    public static final int DATA_LIT_TIME = 0;          // 剩余燃烧时间
    public static final int DATA_LIT_DURATION = 1;      // 当前燃料总燃烧时间
    public static final int DATA_COOKING_PROGRESS = 2;  // 已烧炼刻数
    public static final int DATA_COOKING_TOTAL_TIME = 3;// 配方总烧炼刻数

    private AbstractFurnaceMenu() {}

    /** 由 NetworkHandler 每 tick 调用，把熔炉状态同步到客户端（索引即 client 的 data slot）。 */
    public static void syncData(MenuHost host, int windowId, ContainerStore.FurnaceData f) {
        if (f == null) return;
        host.sendProperty(windowId, DATA_LIT_TIME, f.burnTime);
        host.sendProperty(windowId, DATA_LIT_DURATION, f.burnTotal);
        host.sendProperty(windowId, DATA_COOKING_PROGRESS, f.cookTime);
        host.sendProperty(windowId, DATA_COOKING_TOTAL_TIME, f.cookTotal);
    }
}
