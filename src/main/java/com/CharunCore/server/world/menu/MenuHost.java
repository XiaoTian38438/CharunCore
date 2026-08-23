package com.CharunCore.server.world.menu;

/**
 * 菜单逻辑类向 NetworkHandler 请求“发包/玩家状态”的窄接口。
 * NetworkHandler 实现本接口（由主Agent负责接线），菜单逻辑不直接触碰网络包。
 * 这样容器/UI 子系统逻辑可独立于 NetworkHandler 演进。
 */
public interface MenuHost {

    /** 发送 container_data 单字段 (index, value)。 */
    void sendProperty(int windowId, int index, int value);

    /** 发送 container_set_content（slots 为 id/count 并行数组，count 为有效槽对数）。 */
    void sendContent(int windowId, int[] slots, int count);

    /** 玩家当前经验等级。 */
    int xpLevel();

    /** 玩家是否创造/OP（免经验、免材料）。 */
    boolean creative();

    /** 扣除经验等级（附魔台消费）。 */
    void takeXp(int levels);
}
