package com.CharunCore.server.world.menu;

import com.CharunCore.server.world.ContainerStore;

/**
 * 命令方块逻辑 (#29)。打开时 NetworkHandler 自方块实体读取 command/mode/conditional/auto/trackOutput
 * 并发送命令方块更新包（含权限），玩家有权限时按钮可用；确认时发回设置包由 NetworkHandler 写回方块实体。
 * 本类负责数据建模与权限判断，发包/写回由 NetworkHandler 负责（见回报）。
 */
public final class CommandBlockEditMenu {

    private CommandBlockEditMenu() {}

    /** 权限判断：仅有权限玩家可编辑/确认。 */
    public static boolean canEdit(ContainerStore.CommandBlockData cb) {
        return cb != null && cb.hasPermission;
    }

    /** 是否脉冲/连锁/重复模式（原版 0=脉冲,1=连锁,2=重复）。 */
    public static boolean isValidMode(int mode) {
        return mode >= 0 && mode <= 2;
    }

    /** 确认前校验。 */
    public static boolean validate(ContainerStore.CommandBlockData cb) {
        if (!canEdit(cb)) return false;
        if (cb.command == null) return false;
        return isValidMode(cb.mode);
    }
}
