package com.CharunCore.server.world.entity;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.ExplosionEngine;
import com.CharunCore.server.world.WorldManager;
import com.CharunCore.server.network.NetworkHandler;

/**
 * 矿车实体（含 chest / furnace / tnt / hopper 变体）。
 *
 * 物理模型（对齐原版 OldMinecartBehavior）：
 *  - 静止/行驶时的 y = 所在轨道方块 Y + 0.0625（原版 0.0625 偏移）。
 *  - 沿轨道端口移动：直轨保持方向；弯道在方块中心翻转入射方向；坡道按 ascending_* 抬升 1 格。
 *  - 动力轨 / 激活轨通电 → 加速；动力轨未通电 → 刹车停住。
 *  - 动力矿车有燃料时自动前进；TNT 矿车踩在通电激活轨 → 引爆。
 *  - 骑乘：玩家右键上车（set_passengers 0x69），每 tick 跟随矿车位置；sneak 下车。
 */
public class MinecartEntity extends Entity {
    public String variant;            // minecart / chest_minecart / furnace_minecart / tnt_minecart / hopper_minecart
    public int passengerEid = -1;     // 当前骑乘者 entity id（-1 = 空）
    public int dirX = 0, dirZ = 1;    // 当前行进方向（单位向量）
    public double speed = 0.0;        // 当前速度（沿 dir；可负=反向）
    public int throttle = 0;          // 骑乘油门：+1 正向 / -1 反向 / 0 滑行
    public double lastInputYaw = 0.0; // 最近一次 vehicle_move 上报的玩家朝向
    /** Bug24: 骑乘时客户端权威 —— 收到 vehicle_move 后置 true, 服务端跳过自行物理模拟。 */
    public boolean clientDriven = false;
    public double fuel = 0.0;         // furnace_minecart 燃料剩余 tick
    public int[] invIds;              // chest/hopper 存储（暂未接 hopper 自动传输）
    public int[] invCounts;
    public boolean dead = false;
    /** Bug24: 推力冷却(刻) —— 玩家持续贴着矿车时每 5 刻才施加一次推力, 防推力与回中力逐刻对抗抖动。 */
    public int pushCooldown = 0;
    /** Bug24: 客户端权威骑乘的宽限期(刻), 每次 vehicle_move 刷新为 10。 */
    public int clientDrivenGrace = 0;

    public MinecartEntity(int id, String variant, double x, double y, double z) {
        super(id, 0, x, y, z);
        this.variant = variant;
        this.typeName = variant;
        this.width = 0.98;
        this.height = 0.7;
        this.noPhysics = true;        // 自行处理轨道/重力物理
        if ("chest_minecart".equals(variant) || "hopper_minecart".equals(variant)) {
            invIds = new int[27];
            invCounts = new int[27];
        }
    }

    private static boolean isRail(String n) {
        return "rail".equals(n) || "powered_rail".equals(n)
            || "activator_rail".equals(n) || "detector_rail".equals(n);
    }

    /** 轨的两个端口方向（单位向量）。弯道在中心翻转入射方向。 */
    private static int[][] portsOf(String shape) {
        switch (shape) {
            case "north_south":            return new int[][]{{0, -1}, {0, 1}};
            case "east_west":              return new int[][]{{1, 0}, {-1, 0}};
            case "ascending_north":        return new int[][]{{0, -1}, {0, 1}};
            case "ascending_south":        return new int[][]{{0, -1}, {0, 1}};
            case "ascending_east":         return new int[][]{{1, 0}, {-1, 0}};
            case "ascending_west":         return new int[][]{{1, 0}, {-1, 0}};
            case "south_east":             return new int[][]{{0, 1}, {1, 0}};
            case "south_west":             return new int[][]{{0, 1}, {-1, 0}};
            case "north_west":             return new int[][]{{0, -1}, {-1, 0}};
            case "north_east":             return new int[][]{{0, -1}, {1, 0}};
            default:                       return new int[][]{{0, -1}, {0, 1}};
        }
    }

    /** 上坡方向（仅 ascending_*）。 */
    private static int[] upDirOf(String shape) {
        switch (shape) {
            case "ascending_north": return new int[]{0, -1};
            case "ascending_south": return new int[]{0, 1};
            case "ascending_east":  return new int[]{1, 0};
            case "ascending_west":  return new int[]{-1, 0};
            default:                return null;
        }
    }

    @Override
    public void tick() {
        if (dead) return;
        if (fireTicks > 0) fireTicks--;
        if (pushCooldown > 0) pushCooldown--;

        // TNT 矿车：踩在通电激活轨 -> 引爆
        if ("tnt_minecart".equals(variant)) {
            int cy = (int) Math.floor(y);
            int st = WorldManager.getBlockState(dim, (int) Math.floor(x), cy, (int) Math.floor(z));
            if ("activator_rail".equals(BlockStateHelper.getName(st))
                    && "true".equals(BlockStateHelper.getProp(st, "powered"))) {
                explode();
                return;
            }
        }

        // Bug24 二轮: 骑乘时 W/S(throttle!=0) -> 服务端主导物理(油门驱动),
        // 无输入且客户端在流式同步 -> 采纳客户端位置(空滑不拉扯)。
        if (clientDriven && passengerEid >= 0 && throttle == 0) {
            if (--clientDrivenGrace > 0) {
                updateRider();
                return;
            }
        }
        clientDriven = false; // 有油门输入/无乘客/超时 -> 服务端物理

        int rx = (int) Math.floor(x), rz = (int) Math.floor(z), ry = (int) Math.floor(y);
        int state = WorldManager.getBlockState(dim, rx, ry, rz);
        String rname = BlockStateHelper.getName(state);
        if (!isRail(rname)) {
            tickOffRail(rx, ry, rz);
            return;
        }
        tickOnRail(rx, ry, rz, state, rname);
    }

    private void tickOffRail(int rx, int ry, int rz) {
        applyPushOffRail();
        vy -= 0.08;
        if (vy < -3.0) vy = -3.0;
        y += vy;
        int fy = (int) Math.floor(y);
        int fst = WorldManager.getBlockState(dim, rx, fy, rz);
        String fn = BlockStateHelper.getName(fst);
        if (isRail(fn)) {
            y = fy + 0.0625; vy = 0;
        } else if (isSolid(rx, fy, rz)) {
            y = fy + 1.0; vy = 0; speed = 0;
        } else if (y < dim.minY - 16) {
            remove();
            return;
        }
        speed *= 0.9;
        if (Math.abs(speed) < 0.003) speed = 0;
        updateRider();
    }

    private void tickOnRail(int rx, int ry, int rz, int state, String rname) {
        String shape = BlockStateHelper.getProp(state, "shape");
        boolean railPowered = "true".equals(BlockStateHelper.getProp(state, "powered"));
        boolean isPowered = "powered_rail".equals(rname);
        boolean isActivator = "activator_rail".equals(rname);
        int[][] ports = portsOf(shape);

        // 实体/玩家碰撞推车(原版: 生物/玩家可推车)。在轨则沿轨轴施加, 排除乘客。
        applyPushFromCollisions(ports);

        // 静止且无动力/无输入 -> 仅设定默认方向，不移动
        if (speed == 0 && throttle == 0 && fuel <= 0 && !(isPowered && railPowered) && !(isActivator && railPowered)) {
            dirX = ports[0][0]; dirZ = ports[0][1];
        }

        // 骑乘控制（玩家朝向投影到轨轴 -> 油门）。#24 修复: 曾 throttle==0 时强制巡航 0.3,
        // 玩家不按键矿车也自己走/反直觉; 改为无输入则摩擦滑行, 有输入才按玩家朝向选轨口。
        if (passengerEid >= 0) {
            if (throttle != 0) {
                // 按玩家朝向选离朝向最近的轨口(原版 Minecart 转弯由玩家朝向引导)
                double fdx = -Math.sin(Math.toRadians(lastInputYaw));
                double fdz = Math.cos(Math.toRadians(lastInputYaw));
                int[][] ports3 = portsOf(shape);
                int best0 = ports3[0][0], best1 = ports3[0][1];
                double bestDot = -2.0;
                for (int[] p : ports3) {
                    double dot = p[0] * fdx + p[1] * fdz;
                    if (dot > bestDot) { bestDot = dot; best0 = p[0]; best1 = p[1]; }
                }
                if (throttle > 0) { dirX = best0; dirZ = best1; }
                else { dirX = -best0; dirZ = -best1; } // S 倒退 = 与朝向相反
                double maxS = 0.3;
                speed = (throttle > 0 ? 1 : -1) * Math.min(Math.abs(speed) + 0.05, maxS);
            } else {
                // 无转向输入: 摩擦滑行(原版松手后减速), 不强制恒速
                speed *= 0.97;
                if (Math.abs(speed) < 0.004) speed = 0;
            }
        }

        // 动力矿车：有燃料自动前进
        if ("furnace_minecart".equals(variant) && fuel > 0) {
            if (speed == 0) { dirX = ports[0][0]; dirZ = ports[0][1]; }
            double sgn = speed >= 0 ? 1 : -1;
            if (speed == 0) sgn = 1;
            speed = sgn * Math.min(Math.abs(speed) + 0.02, 0.3);
            fuel--;
        }

        // 动力轨 / 激活轨通电加速
        if ((isPowered || isActivator) && railPowered) {
            double sgn = speed >= 0 ? 1 : -1;
            if (speed == 0) { dirX = ports[0][0]; dirZ = ports[0][1]; sgn = 1; }
            speed = sgn * Math.min(Math.abs(speed) + 0.06, 0.5);
        }
        // 动力轨未通电且速度很低且无骑乘 -> 刹车停住
        if (isPowered && !railPowered && Math.abs(speed) < 0.12 && throttle == 0 && passengerEid < 0) {
            speed = 0;
        }
        // 普通轨 / 无输入摩擦
        if (!isPowered && !isActivator && passengerEid < 0 && fuel <= 0) {
            speed *= 0.97;
            if (Math.abs(speed) < 0.004) speed = 0;
        }
        if (speed > 0.6) speed = 0.6;
        if (speed < -0.6) speed = -0.6;

        // 移动：先对准当前方块中心，再到下一中心
        double cx = rx + 0.5, cz = rz + 0.5;
        double offX = x - cx, offZ = z - cz;
        double off = Math.abs(offX) + Math.abs(offZ);
        double step = Math.abs(speed);
        if (off > 0.02 && step > 0) {
            if (Math.abs(offX) >= Math.abs(offZ) && dirX != 0) {
                double s = Math.signum(-offX) * Math.min(step, Math.abs(offX));
                x += s;
            } else if (dirZ != 0) {
                double s = Math.signum(-offZ) * Math.min(step, Math.abs(offZ));
                z += s;
            }
            y = ry + 0.0625;
        } else {
            // 在中心：选出口方向并朝下一中心移动
            x = cx; z = cz; y = ry + 0.0625;
            int entryX = -dirX, entryZ = -dirZ;
            boolean entryOk = false;
            for (int[] p : ports) {
                if (p[0] == entryX && p[1] == entryZ) { entryOk = true; break; }
            }
            if (entryOk) {
                for (int[] p : ports) {
                    if (!(p[0] == entryX && p[1] == entryZ)) { dirX = p[0]; dirZ = p[1]; break; }
                }
            } else {
                dirX = ports[0][0]; dirZ = ports[0][1];
            }
            // 上坡高度
            int nry = ry;
            int[] up = upDirOf(shape);
            if (up != null && up[0] == dirX && up[1] == dirZ) nry = ry + 1;
            int nrx = rx + dirX, nrz = rz + dirZ;
            int nst = WorldManager.getBlockState(dim, nrx, nry, nrz);
            String nname = BlockStateHelper.getName(nst);
            if (isRail(nname)) {
                String nshape = BlockStateHelper.getProp(nst, "shape");
                int[][] nports = portsOf(nshape);
                boolean canEnter = false;
                for (int[] p : nports) {
                    if (p[0] == -dirX && p[1] == -dirZ) { canEnter = true; break; }
                }
                double tx = nrx + 0.5, tz = nrz + 0.5, ty = nry + 0.0625;
                double ddx = tx - x, ddz = tz - z;
                double dist = Math.hypot(ddx, ddz);
                if (canEnter && dist > 1e-6) {
                    if (step >= dist) { x = tx; z = tz; y = ty; }
                    else {
                        x += ddx / dist * step;
                        z += ddz / dist * step;
                        // Bug24: 坡道按行进比例渐变抬升(曾直接 y=ty 每格瞬移 1 格 -> 视觉高频抖动)
                        y += (ty - y) * (step / dist);
                    }
                } else {
                    speed = 0;
                }
            } else {
                speed = 0;
            }
        }
        // 朝向：使模型对准行进方向（与项目 (-sin,cos) 习惯一致）
        this.yaw = (float) Math.toDegrees(Math.atan2(-dirX, dirZ));

        updateRider();
    }

    private void updateRider() {
        if (passengerEid < 0) return;
        for (NetworkHandler h : NetworkHandler.players.values()) {
            if (h.eid == passengerEid) {
                h.x = this.x;
                h.z = this.z;
                h.y = this.y + 0.7;
                h.onGround = true;
                break;
            }
        }
    }

    private boolean isSolid(int x, int y, int z) {
        int st = WorldManager.getBlockState(dim, x, y, z);
        if (st == 0) return false;
        String n = BlockStateHelper.getName(st);
        return !("water".equals(n) || "lava".equals(n) || "air".equals(n)
            || "rail".equals(n) || "powered_rail".equals(n)
            || "activator_rail".equals(n) || "detector_rail".equals(n));
    }

    // ── 碰撞推车(原版: 生物/玩家可推矿车) ──────────────────────────────────
    // 在轨: 沿最近且顺向的轨轴施加速度; 落地: 直接给 vx/vz。均排除乘客。

    private void applyPushFromCollisions(int[][] ports) {
        for (Entity e : EntityManager.getEntities().values()) {
            if (e == this || e.dim != dim || e instanceof MinecartEntity) continue;
            tryPushOnRail(e.x, e.y, e.z, e.width, e.height, ports, Double.NaN);
        }
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.eid == passengerEid || p.currentDim != dim) continue;
            tryPushOnRail(p.x, p.y, p.z, 0.6, 1.8, ports, p.yaw);
        }
    }

    private void tryPushOnRail(double ex, double ey, double ez, double ew, double eh, int[][] ports, double pusherYaw) {
        double dx = x - ex, dz = z - ez;
        if (Math.abs(y - ey) > (height / 2 + eh / 2) + 0.5) return;
        double dist = Math.hypot(dx, dz);
        double minDist = (width / 2 + ew / 2) + 0.05;
        // Bug24 修复: 只在真正碰撞重叠(dist < minDist)时推车。曾放宽到 minDist+0.25,
        // 玩家/生物只是站在矿车旁边(未接触)也每刻被判定为"推" -> 空车原地高频抖动。
        if (dist >= minDist || dist < 1e-4) return;
        // Bug24: 推车方向 = 推动者视线方向在轨轴上的投影(原版玩家推车语义)。
        // 曾用"推离推动者"的径向方向 -> 侧向贴脸时径向垂直轨轴不推(无法开动),
        // 正对时又与回中力逐刻对抗(高频抖动)。
        double pdx, pdz;
        if (!Double.isNaN(pusherYaw)) {
            pdx = -Math.sin(Math.toRadians(pusherYaw));
            pdz = Math.cos(Math.toRadians(pusherYaw));
        } else {
            pdx = dx / dist;
            pdz = dz / dist;
        }
        if (pushCooldown > 0) return;
        double best = -2; int bdx = dirX, bdz = dirZ;
        for (int[] p : ports) {
            double dot = pdx * p[0] + pdz * p[1];
            if (dot > best) { best = dot; bdx = p[0]; bdz = p[1]; }
        }
        if (best <= 0.3) return; // 视线与轨轴几乎垂直, 不推
        pushCooldown = 5;
        dirX = bdx; dirZ = bdz;
        speed = Math.min(0.35, Math.max(Math.abs(speed), 0.16));
    }

    private void applyPushOffRail() {
        for (Entity e : EntityManager.getEntities().values()) {
            if (e == this || e.dim != dim || e instanceof MinecartEntity) continue;
            tryPushOffRail(e.x, e.y, e.z, e.width, e.height);
        }
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.eid == passengerEid || p.currentDim != dim) continue;
            tryPushOffRail(p.x, p.y, p.z, 0.6, 1.8);
        }
    }

    private void tryPushOffRail(double ex, double ey, double ez, double ew, double eh) {
        double dx = x - ex, dz = z - ez;
        if (Math.abs(y - ey) > (height / 2 + eh / 2) + 0.5) return;
        double dist = Math.hypot(dx, dz);
        double minDist = (width / 2 + ew / 2) + 0.05;
        if (dist >= minDist || dist < 1e-4) return; // Bug24: 同上, 仅真实重叠才推
        double pdx = dx / dist, pdz = dz / dist;
        vx += pdx * 0.06; vz += pdz * 0.06;
        double sp = Math.hypot(vx, vz);
        if (sp > 0.6) { vx = vx / sp * 0.6; vz = vz / sp * 0.6; }
    }

    /** 破坏矿车：掉落对应物品（TNT 矿车则先引爆）。 */
    public void breakCart() {
        if (dead) return;
        if ("tnt_minecart".equals(variant)) { explode(); return; }
        dropItem();
        remove();
    }

    /** 掉落矿车物品。 */
    public void dropItem() {
        int itemId = BlockManager.getItemIdByName(variant);
        if (itemId > 0) {
            ItemEntity drop = new ItemEntity(EntityManager.allocateId(), x, y + 0.3, z, itemId, 1);
            drop.dim = dim;
            EntityManager.addEntity(drop);
        }
    }

    /** TNT 矿车引爆。 */
    public void explode() {
        if (dead) return;
        dead = true;
        ExplosionEngine.explode(dim, x, y + 0.5, z, 4.0f, false);
        dropItem();
        remove();
    }
}
