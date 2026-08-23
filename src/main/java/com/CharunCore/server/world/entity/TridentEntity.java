package com.CharunCore.server.world.entity;

import com.CharunCore.server.Main;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;

/**
 * 三叉戟投射物。对齐原版 Trident:
 * - 命中生物按动量造成伤害(基础 8, 水生目标受 impaling 加成)
 * - loyalty>0: 落地/命中后飞回投掷者并归还物品
 * - channeling: 雷暴中命中生物时召雷(strikeLightning)
 * - riptide 在水/雨中由客户端处理玩家位移, 服务端在 throwTrident 中仅播放音效不生成实体
 */
public class TridentEntity extends Entity {

    private static final double GRAVITY = 0.05;
    private static final double DRAG_AIR = 0.99;
    private static final double DRAG_WATER = 0.6;

    public NetworkHandler thrower;
    public double baseDamage = 8.0;
    public int loyalty = 0;            // 0-3: 返回等级
    public boolean channeling = false;
    public int impaling = 0;           // 0-5: 对水生额外伤害
    public boolean pickupable = true;

    private int life = 0;
    private int stuckTicks = 0;
    private boolean inGround = false;
    private boolean returning = false;
    private int returnDelay = 0;

    public TridentEntity(int id, double x, double y, double z,
                         double vx, double vy, double vz, NetworkHandler thrower) {
        super(id, 0, x, y, z);
        this.typeName = "trident";
        this.noPhysics = true;
        this.width = 0.5;
        this.height = 0.5;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.thrower = thrower;
        if (thrower != null) this.dim = thrower.currentDim;
        updateRotation();
    }

    @Override
    public void tick() {
        life++;
        if (life > 1200) { dropOrDespawn(); return; }

        if (returning) { flyBack(); return; }

        if (inGround) {
            stuckTicks++;
            tryPickup();
            if (stuckTicks > 100) {
                if (loyalty > 0) { returning = true; returnDelay = 8; }
                else dropOrDespawn();
            }
            return;
        }

        double speed = Math.sqrt(vx * vx + vy * vy + vz * vz);
        int steps = Math.max(1, Math.min(8, (int) Math.ceil(speed / 0.35)));
        double sx = vx / steps, sy = vy / steps, sz = vz / steps;

        for (int i = 0; i < steps; i++) {
            double nx = x + sx, ny = y + sy, nz = z + sz;

            Object hit = findEntityHit(nx, ny, nz);
            if (hit != null) { onHitEntity(hit); return; }

            if (isSolidAt(nx, ny, nz)) {
                x = nx - sx * 0.3;
                y = ny - sy * 0.3;
                z = nz - sz * 0.3;
                vx = vy = vz = 0;
                inGround = true;
                onGround = true;
                if (loyalty > 0) { returning = true; returnDelay = 8; }
                return;
            }
            x = nx; y = ny; z = nz;
        }

        double drag = isInFluid("water") ? DRAG_WATER : DRAG_AIR;
        vx *= drag;
        vy *= drag;
        vz *= drag;
        vy -= GRAVITY;
        updateRotation();

        if (y < -80) remove();
    }

    private Object findEntityHit(double nx, double ny, double nz) {
        Object best = null;
        double bestSq = Double.MAX_VALUE;
        for (Entity e : EntityManager.getAllEntities()) {
            if (!(e instanceof LivingEntity living)) continue;
            if (living.dim != this.dim) continue;
            if (living.deathTime > 0 || living.health <= 0) continue;
            if (life < 5 && thrower != null
                && Math.abs(living.x - thrower.x) < 1.5
                && Math.abs(living.z - thrower.z) < 1.5) continue;
            double hw = living.width / 2.0 + 0.3;
            if (nx < living.x - hw || nx > living.x + hw) continue;
            if (nz < living.z - hw || nz > living.z + hw) continue;
            if (ny < living.y - 0.3 || ny > living.y + living.height + 0.3) continue;
            double dx = nx - living.x, dy = ny - living.y, dz = nz - living.z;
            double sq = dx * dx + dy * dy + dz * dz;
            if (sq < bestSq) { bestSq = sq; best = living; }
        }
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null || p.isDead || p.gameMode == 3) continue;
            if (p.currentDim != this.dim) continue;
            if (life < 5 && p == thrower) continue;
            double hw = 0.6;
            if (nx < p.x - hw || nx > p.x + hw) continue;
            if (nz < p.z - hw || nz > p.z + hw) continue;
            if (ny < p.y - 0.3 || ny > p.y + 1.8 + 0.3) continue;
            double dx = nx - p.x, dy = ny - p.y, dz = nz - p.z;
            double sq = dx * dx + dy * dy + dz * dz;
            if (sq < bestSq) { bestSq = sq; best = p; }
        }
        return best;
    }

    private void onHitEntity(Object target) {
        double speed = Math.sqrt(vx * vx + vy * vy + vz * vz);
        int dmg = (int) Math.ceil(Math.min(2.147483647E9, speed * baseDamage));
        if (dmg < 1) dmg = 1;

        LivingEntity livingTarget = (target instanceof LivingEntity) ? (LivingEntity) target : null;
        if (impaling > 0 && livingTarget != null && isAquatic(livingTarget.typeName)) {
            dmg += (int) (impaling * 2.5);
        }

        if (target instanceof NetworkHandler player) {
            player.damagePlayer(dmg, "trident", x, z);
            double horiz = Math.sqrt(vx * vx + vz * vz);
            if (horiz > 0.001 && !player.isDead) {
                player.knockback(vx / horiz * 0.6, vz / horiz * 0.6, 0.1);
            }
            if (thrower != null) {
                thrower.sendSoundAt("minecraft:entity.trident.hit", x, y, z, 1.0f, 1.2f);
            }
        } else if (livingTarget != null) {
            if (thrower != null && livingTarget instanceof MobEntity) {
                ((MobEntity) livingTarget).lastAttacker = thrower;
            }
            livingTarget.damage(dmg, "trident");
            double horiz = Math.sqrt(vx * vx + vz * vz);
            if (horiz > 0.001 && !(livingTarget instanceof EnderDragonEntity)) {
                livingTarget.vx += vx / horiz * 0.6;
                livingTarget.vz += vz / horiz * 0.6;
            }
            if (thrower != null) {
                thrower.sendSoundAt("minecraft:entity.trident.hit", x, y, z, 1.0f, 1.2f);
            }
        }

        // 引雷: 雷暴中且命中生物 → 召雷
        if (channeling && Main.isThundering && livingTarget != null && thrower != null) {
            thrower.strikeLightning(this.dim, x, y, z);
        }

        if (loyalty > 0) { returning = true; returnDelay = 8; }
    }

    private void flyBack() {
        if (returnDelay > 0) { returnDelay--; return; }
        if (thrower == null || thrower.isDead || thrower.currentDim != this.dim) {
            dropOrDespawn();
            return;
        }
        double dx = thrower.x - x, dy = (thrower.y + 1.0) - y, dz = thrower.z - z;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist < 1.5) {
            int tid = BlockManager.getItemIdByName("trident");
            if (tid > 0) thrower.giveItem(tid, 1);
            remove();
            return;
        }
        double sp = 1.0 + loyalty * 0.1;
        vx = dx / dist * sp;
        vy = dy / dist * sp;
        vz = dz / dist * sp;
        x += vx;
        y += vy;
        z += vz;
        if (isSolidAt(x, y, z)) dropOrDespawn();
    }

    private void dropOrDespawn() {
        if (loyalty > 0 && thrower != null && !thrower.isDead && thrower.currentDim == this.dim) {
            returning = true;
            returnDelay = 4;
            return;
        }
        int tid = BlockManager.getItemIdByName("trident");
        if (tid > 0) {
            ItemEntity it = new ItemEntity(
                EntityManager.allocateId(), x, y, z, tid, 1);
            it.dim = this.dim;
            EntityManager.addEntity(it);
        }
        remove();
    }

    private void tryPickup() {
        if (!pickupable) return;
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.currentDim != this.dim || p.isDead) continue;
            double dx = p.x - x, dy = (p.y + 0.9) - y, dz = p.z - z;
            if (dx * dx + dy * dy + dz * dz > 2.25) continue;
            if (p.gameMode == 0 || p.gameMode == 2) {
                int tid = BlockManager.getItemIdByName("trident");
                if (tid > 0) p.giveItem(tid, 1);
            }
            p.sendSoundAt("minecraft:item.trident.return", x, y, z, 0.2f, 1.6f);
            remove();
            return;
        }
    }

    private static boolean isAquatic(String typeName) {
        if (typeName == null) return false;
        return switch (typeName) {
            case "axolotl", "dolphin", "guardian", "elder_guardian", "squid", "glow_squid",
                 "cod", "salmon", "tropical_fish", "pufferfish", "drowned" -> true;
            default -> false;
        };
    }

    private void updateRotation() {
        double horiz = Math.sqrt(vx * vx + vz * vz);
        if (horiz > 0.0001 || Math.abs(vy) > 0.0001) {
            this.yaw = (float) Math.toDegrees(Math.atan2(vx, vz));
            this.pitch = (float) Math.toDegrees(Math.atan2(vy, horiz));
        }
    }
}
