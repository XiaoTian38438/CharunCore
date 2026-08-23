package com.CharunCore.server.world.entity;

/**
 * 钓鱼浮标实体。由玩家手持钓竿右键抛出；经过随机延迟后进入 "ready" 状态，
 * 再次右键收回时若已 ready 则结算战利品。客户端会据此在玩家与浮标间绘制钓线。
 */
public class FishingBobberEntity extends Entity {
    /** 抛出该浮标的玩家 entity id(用于客户端绘制钓线 + 归属)。 */
    public int ownerEid;
    /** 距离可钓出的剩余 tick(>0 表示等待上钩)。 */
    public int hookTicks;
    /** 是否已上钩(可收回获得战利品)。 */
    public boolean ready = false;

    public FishingBobberEntity(int id, double x, double y, double z) {
        super(id, 0, x, y, z);
        this.noPhysics = true; // 漂浮不受重力/碰撞影响
    }

    @Override
    public void tick() {
        if (hookTicks > 0) {
            hookTicks--;
            if (hookTicks == 0) ready = true;
        }
    }
}
