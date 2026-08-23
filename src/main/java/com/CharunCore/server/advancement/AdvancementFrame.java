package com.CharunCore.server.advancement;

/**
 * 成就框类型 (AdvancementType)。对应协议包 0x80 (advancements) 中 displayData.frameType:
 *   0 = task (普通), 1 = goal (目标), 2 = challenge (挑战)。
 * 客户端据此决定 toast 弹出时的边框外观与音效。
 */
public enum AdvancementFrame {
    TASK(0),
    GOAL(1),
    CHALLENGE(2);

    public final int id;

    AdvancementFrame(int id) {
        this.id = id;
    }
}
