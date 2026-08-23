package com.CharunCore.server.world.menu;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.ContainerStore;

/**
 * 唱片机逻辑 (#33)。放入 record 物品时设置正在播放的唱片并触发播放音效包（由 NetworkHandler 广播）。
 * 物品在容器内容槽(槽0)中，客户端据此显示唱片并播放。
 */
public final class JukeboxMenu {

    public static final int SLOT_RECORD = 0; // slots[0/1]

    private JukeboxMenu() {}

    public static boolean isRecord(String name) {
        return name != null && (name.startsWith("music_disc_") || name.equals("music_disc"));
    }

    /** 唱片放入/取出后调用：更新播放状态并通知 NetworkHandler 广播音效/方块状态。 */
    public static void onSlotChanged(MenuHost host, int windowId, ContainerStore.JukeboxData jd) {
        if (jd == null) return;
        int id = jd.recordId;
        boolean playing = id > 0 && isRecord(BlockManager.itemIdToName(id));
        jd.playing = playing;
        jd.version++;
        // 实际播放音效包 + 方块 lit 状态由 NetworkHandler 依 jd.playing/recordId 广播（见回报）
    }

    public static void setRecord(ContainerStore.JukeboxData jd, int recordId) {
        if (jd == null) return;
        jd.recordId = recordId;
        jd.playing = recordId > 0 && isRecord(BlockManager.itemIdToName(recordId));
    }
}
