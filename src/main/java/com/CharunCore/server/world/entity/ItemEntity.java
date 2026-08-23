package com.CharunCore.server.world.entity;

import com.CharunCore.server.network.NetworkHandler;

public class ItemEntity extends Entity {
    public final int itemId;
    public int count;
    public int pickupDelay = 10;
    public int age = 0;
    /** 旋转动画角度(原版: 每 tick 旋转 ~10°) */
    public float rotation = 0.0f;
    /** 悬浮 bob 相位 */
    public float bobTimer = 0.0f;
    private static final java.util.Random rng = new java.util.Random();

    public ItemEntity(int id, double x, double y, double z, int itemId, int count) {
        super(id, 72, x, y, z);
        this.itemId = itemId;
        this.count = count;
        this.vx = (rng.nextDouble() - 0.5) * 0.3;
        this.vy = 0.2 + rng.nextDouble() * 0.2;
        this.vz = (rng.nextDouble() - 0.5) * 0.3;
    }

    @Override
    public void tick() {
        // 旋转动画: 始终随时间旋转(原版掉落物持续自旋)
        rotation += 10.0f;
        if (rotation >= 360.0f) rotation -= 360.0f;

        boolean wasOnGround = onGround;
        double prevVy = vy;
        super.tick();

        age++;
        if (pickupDelay > 0) pickupDelay--;

        if (age > 6000) {
            remove();
            return;
        }

        // #27/#18 修复: 掉落物入水浮起(原版物品在水中有浮力), 曾在水格内恒定加速上浮
        // -> 浮出水面又落回水中反复上下抖动。改为: 未到水面持续上浮; 接近/浮出水面时
        // 大幅阻尼, 让物品停在水面附近轻轻浮动(原版不沉底也不抽搐)。
        if (isInFluid("water")) {
            double surfTop = Math.floor(y) + 1.0;
            if (y + 0.5 >= surfTop - 0.05) {
                vy *= 0.6;
                vy += 0.006;
                if (vy > 0.012) vy = 0.012;
            } else {
                vy += 0.05;
                if (vy > 0.12) vy = 0.12;
            }
        }

        // 悬浮 bob: 落地后做轻微上下浮动(原版 item bob)
        bobTimer += 0.2f;

        // 落地即停: 原版掉落物触地瞬间被摩擦完全吸收, 不会反复弹跳
        // (曾 vy=-prevVy*0.45 -> 落在轻质压力板上反复弹跳 -> "物品在压力板上上下抽搐")。
        if (!wasOnGround && onGround) {
            vy = 0.0;
            vx *= 0.6;
            vz *= 0.6;
        }

        if (pickupDelay <= 0) {
            // 原版拾取: 玩家(中心)与物品(中心)距离 < 1.5 格内直接拾取。
            // 注意: 原版物品没有向玩家的"磁力拉近"——物品不会穿墙飞向玩家,
            //       只有玩家走进拾取半径才捡起 (移除原 3 格磁力导致的隔墙吸物)。
            for (NetworkHandler player : NetworkHandler.players.values()) {
                if (player.isDead || player.currentDim != this.dim) continue; // P4-7: 跨维度误拾取过滤
                double dx = player.x - x;
                double dy = player.y - y;
                double dz = player.z - z;
                double distSq = dx * dx + dy * dy + dz * dz;
                if (distSq < 1.5 * 1.5) {
                    player.giveItem(itemId, count);
                    remove();
                    return;
                }
            }
        }
    }
}
