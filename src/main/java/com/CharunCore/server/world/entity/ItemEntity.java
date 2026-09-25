package com.CharunCore.server.world.entity;

import com.CharunCore.server.network.NetworkHandler;

public class ItemEntity extends Entity {
    public final int itemId;
    public int count;
    // Bug4/33: 掉落物携带的物品组件(附魔/药水/自定义名/耐久/纹饰),
    // Q 键丢弃/死亡掉落/铁砧退回等场景不再丢失 NBT。
    public int itemDamage = 0;
    public java.util.Map<Integer, Integer> itemEnchants = null;
    public String itemPotion = null;
    public String itemCustomName = null;
    public int trimMaterial = -1;
    public int trimPattern = -1;
    /** B1: 掉落物扩展组件(lore 多行 \n / 不可破坏 / 发光 -1无0关1开)。 */
    public String itemLore = null;
    public boolean itemUnbreakable = false;
    public int itemGlint = 0;
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
            var despawnEvent = com.CharunCore.server.plugin.event.EventManager.INSTANCE.fire(
                    new com.CharunCore.server.plugin.event.events.ItemDespawnEvent(this));
            if (despawnEvent.isCancelled()) {
                age = 3000; // 取消消失: 重置存活计时
                return;
            }
            remove();
            return;
        }

        // Bug18 修复: 水中掉落物浮到水面附近漂浮。原实现弹簧系数过大, 离散系统主特征值 >1
        // (发散), 与 Entity.tick 的下沉物理叠加形成正反馈: 物品逐格棘轮上抬直至飘出水面。
        // 改为按浸没深度比例的浮力 + 强阻尼(特征值 0.93/0.43, 单调收敛):
        // 平衡点约浸没 60%(水面下 0.4 格), 水平速度阻尼让物品随水流平稳漂移。
        if (isInFluid("water")) {
            // Bug18 二轮: 三段式浮力 —— 近水面(浸没 0.3~0.75)强阻尼静浮(速度收敛到 0,
            // 静止时 syncTracking 不再发包 -> 消除"近玩家抖动/远离后正常"的分叉);
            // 深层上浮、露出过多回落。
            // Bug18 四轮: 补上原版水流推力(FlowingFluid.getFlow 近似) —— 服务端从未推动
            // 水中物品, 客户端本地顺水漂走后被 60 tick 重锚定拉回 = "来回抽搐"。
            applyWaterFlowPush();
            double waterTop = Math.floor(y) + 1.0;
            double depth = Math.max(0.0, Math.min(1.0, waterTop - y));
            if (depth > 0.75) vy = Math.min(vy + 0.05, 0.08);
            else if (depth > 0.3) vy *= 0.3;
            else vy -= 0.03;
            if (vy > 0.08) vy = 0.08;
            if (vy < -0.1) vy = -0.1;
            vx *= 0.85;
            vz *= 0.85;
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
            // Bug47: pickupItemCount 先并入背包已有堆再放空槽; 背包满则部分拾取后留余量。
            for (NetworkHandler player : NetworkHandler.players.values()) {
                if (player.isDead || player.currentDim != this.dim) continue; // P4-7: 跨维度误拾取过滤
                // Bug18: 原版语义 —— 物品 AABB 外扩 (1, 0.5, 1) 与玩家 AABB 相交即可拾取
                double ix = Math.abs(player.x - x), iz = Math.abs(player.z - z);
                boolean overlapX = ix < 1.0 + 0.3 + 0.125;
                boolean overlapZ = iz < 1.0 + 0.3 + 0.125;
                boolean overlapY = (y + 0.125) > (player.y - 0.5)
                        && (y - 0.125) < (player.y + 1.8 + 0.5);
                if (overlapX && overlapZ && overlapY) {
                    var pickEvent = com.CharunCore.server.plugin.event.EventManager.INSTANCE.fire(
                            new com.CharunCore.server.plugin.event.events.EntityPickupItemEvent(player, this));
                    if (pickEvent.isCancelled()) {
                        pickupDelay = 20;
                        return;
                    }
                    NetworkHandler.ItemMeta dropMeta = NetworkHandler.ItemMeta.of(
                            itemEnchants, itemPotion, itemCustomName, itemDamage, trimMaterial, trimPattern,
                            itemLore, itemUnbreakable, itemGlint);
                    int got = player.pickupItemCount(itemId, count, dropMeta);
                    if (got >= count) {
                        NetworkHandler.broadcastCollect(dim, id, player.eid, count);
                        remove();
                    } else if (got > 0) {
                        NetworkHandler.broadcastCollect(dim, id, player.eid, got);
                        count -= got;
                        pickupDelay = 10;
                    } else {
                        pickupDelay = 20;
                    }
                    return;
                }
            }
        }

        // Bug18: 原版式世界内掉落物合并 —— 同 id、同维度、距离 <0.6 格、均无组件差异时并堆
        if ((age & 7) == 0 && itemEnchants == null && itemPotion == null
                && itemCustomName == null && itemDamage == 0 && trimMaterial < 0 && count > 0) {
            for (Entity other : EntityManager.getEntities().values()) {
                if (!(other instanceof ItemEntity o) || o == this || o.dim != dim) continue;
                if (o.itemId != itemId || o.count <= 0 || o.pickupDelay > 0) continue;
                if (o.itemEnchants != null || o.itemPotion != null
                        || o.itemCustomName != null || o.itemDamage != 0 || o.trimMaterial >= 0) continue;
                int max = com.CharunCore.server.utils.BlockManager.getStackSize(
                    com.CharunCore.server.utils.BlockManager.itemIdToName(itemId));
                if (max <= 0) max = 64;
                if (count + o.count > max) continue;
                double mdx = o.x - x, mdy = o.y - y, mdz = o.z - z;
                if (mdx * mdx + mdy * mdy + mdz * mdz > 0.36) continue;
                count += o.count;
                EntityManager.removeEntity(o.id);
                break;
            }
        }
    }

    /** 原版 FlowingFluid.getFlow 近似: 朝更低水位(或溢流边缘)方向推物品, 阻尼后稳态漂速 ~0.05 格/tick。 */
    private void applyWaterFlowPush() {
        int bx = (int) Math.floor(x), by = (int) Math.floor(y), bz = (int) Math.floor(z);
        int own = fluidLevelAt(bx, by, bz);
        if (own < 0 || own == 8) return; // 不在水里(算错)或下落水柱(竖直方向交给浮力)
        double fx = 0, fz = 0;
        boolean any = false;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : dirs) {
            int nl = fluidLevelAt(bx + d[0], by, bz + d[1]);
            double w = 0;
            if (nl < 0) {
                // 非水邻居: 空气且其下方也是空气 -> 水会溢流过去, 视作更低
                int nb = com.CharunCore.server.world.WorldManager.getBlockState(dim, bx + d[0], by, bz + d[1]);
                int below = com.CharunCore.server.world.WorldManager.getBlockState(dim, bx + d[0], by - 1, bz + d[1]);
                String nbName = com.CharunCore.server.utils.BlockStateHelper.getName(nb);
                String belowName = com.CharunCore.server.utils.BlockStateHelper.getName(below);
                boolean airLike = nb == 0 || "air".equals(nbName) || "cave_air".equals(nbName);
                boolean belowAirLike = below == 0 || "air".equals(belowName) || "cave_air".equals(belowName);
                if (airLike && belowAirLike) w = 1.0;
            } else if (nl > own) {
                w = Math.min(3.0, nl - own);
            }
            if (w > 0) { fx += d[0] * w; fz += d[1] * w; any = true; }
        }
        if (any) {
            double len = Math.hypot(fx, fz);
            vx += fx / len * 0.008;
            vz += fz / len * 0.008;
        }
    }

    /** 水位: 0=水源,1-7=流动,8=下落; 非水返回 -1。 */
    private int fluidLevelAt(int x, int y, int z) {
        int st = com.CharunCore.server.world.WorldManager.getBlockState(dim, x, y, z);
        String n = com.CharunCore.server.utils.BlockStateHelper.getName(st);
        if (!"water".equals(n)) return -1;
        String p = com.CharunCore.server.utils.BlockStateHelper.getProp(st, "level");
        return p == null ? 0 : Integer.parseInt(p);
    }
}
