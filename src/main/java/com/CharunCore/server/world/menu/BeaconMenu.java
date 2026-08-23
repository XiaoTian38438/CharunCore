package com.CharunCore.server.world.menu;

import com.CharunCore.server.world.ContainerStore;

/**
 * 信标逻辑 (#15)。levels 由 NetworkHandler 自金字塔方块扫描填充。
 * 原版 BeaconMenu：levels>=1 可选主效果；levels>=4 可选副效果；支付物存在且有权限时确认生效。
 * 客户端 data 槽：0=levels、1=primary、2=secondary。
 */
public final class BeaconMenu {

    public static final int DATA_LEVELS = 0;
    public static final int DATA_PRIMARY = 1;
    public static final int DATA_SECONDARY = 2;

    private BeaconMenu() {}

    public static void syncData(MenuHost host, int windowId, ContainerStore.BeaconData bd) {
        if (bd == null) return;
        host.sendProperty(windowId, DATA_LEVELS, Math.max(0, Math.min(4, bd.levels)));
        host.sendProperty(windowId, DATA_PRIMARY, bd.primary);
        host.sendProperty(windowId, DATA_SECONDARY, bd.secondary);
    }

    /** 选主效果（点击主效果格）。levels>=1 才允许。 */
    public static void selectPrimary(MenuHost host, int windowId, ContainerStore.BeaconData bd, int effectId) {
        if (bd == null) return;
        if (bd.levels < 1) return;
        if (effectId != -1 && !isValid(effectId)) return;
        bd.primary = effectId;
        if (bd.levels < 4) bd.secondary = 0; // 不足 4 级无副效果
        bd.updating = true;
        bd.version++;
        syncData(host, windowId, bd);
    }

    /** 选副效果（点击副效果格）。levels>=4 且已选主效果才允许；0 = 仅主效果(1级)。 */
    public static void selectSecondary(MenuHost host, int windowId, ContainerStore.BeaconData bd, int effectId) {
        if (bd == null) return;
        if (bd.levels < 4 || bd.primary == -1) return;
        if (effectId != 0 && !isValid(effectId)) return;
        bd.secondary = effectId;
        bd.updating = true;
        bd.version++;
        syncData(host, windowId, bd);
    }

    private static boolean isValid(int id) {
        for (int e : MenuUtil.beaconEffects()) if (e == id) return true;
        return false;
    }
}
