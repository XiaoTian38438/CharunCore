package com.CharunCore.server.world.menu;

import com.CharunCore.server.world.ContainerStore;

/**
 * 酿造台逻辑 (#12)。烧炼推进在 ContainerStore.tickBrewing（已修正多份材料消耗）。
 * 客户端 data 槽：0=brewTime(进度)、1=fuel(>0 有燃料)、2=fuelTime 剩余(可选)。
 */
public final class BrewingStandMenu {

    public static final int DATA_BREW_TIME = 0;
    public static final int DATA_FUEL = 1;
    public static final int DATA_FUEL_TIME = 2;

    private BrewingStandMenu() {}

    public static void syncData(MenuHost host, int windowId, ContainerStore.BrewingData b) {
        if (b == null) return;
        host.sendProperty(windowId, DATA_BREW_TIME, b.brewTime);
        host.sendProperty(windowId, DATA_FUEL, b.fuelTime > 0 ? 1 : 0);
        host.sendProperty(windowId, DATA_FUEL_TIME, b.fuelTime);
    }
}
