package com.CharunCore.server.world.entity;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;

import java.util.HashSet;
import java.util.Set;

/**
 * 抛射箭矢。重力 0.05 / 空气阻力 0.99 / 水中阻力 0.6,
 * 命中生物按动量造成伤害, 命中方块钉住并可被玩家拾回。
 */
public class ArrowEntity extends Entity {

    private static final double GRAVITY = 0.05;
    private static final double DRAG_AIR = 0.99;
    private static final double DRAG_WATER = 0.6;

    public NetworkHandler shooter;
    public double baseDamage = 2.0;
    public boolean isCritical = false;
    public boolean pickupable = true;
    /** 穿透等级(弩 piercing 附魔): >0 时命中生物不消失, 继续飞行。 */
    public int piercing = 0;
    /** 已被穿透命中的实体 id, 避免重复命中同一目标。 */
    public Set<Integer> piercedIds = new HashSet<>();

    private int life = 0;
    private int stuckTicks = 0;
    private boolean inGround = false;

    public ArrowEntity(int id, double x, double y, double z,
                       double vx, double vy, double vz, NetworkHandler shooter) {
        super(id, 0, x, y, z);
        this.typeName = "arrow";
        this.noPhysics = true;
        this.width = 0.5;
        this.height = 0.5;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.shooter = shooter;
        if (shooter != null) this.dim = shooter.currentDim;
        updateRotation();
    }

    @Override
    public void tick() {
        life++;
        if (life > 1200) {
            remove();
            return;
        }

        if (inGround) {
            stuckTicks++;
            tryPickup();
            if (stuckTicks > 1200) remove();
            return;
        }

        double speed = Math.sqrt(vx * vx + vy * vy + vz * vz);
        int steps = Math.max(1, Math.min(8, (int) Math.ceil(speed / 0.35)));
        double sx = vx / steps, sy = vy / steps, sz = vz / steps;

        for (int i = 0; i < steps; i++) {
            double nx = x + sx, ny = y + sy, nz = z + sz;

            Object hit = findEntityHit(nx, ny, nz);
            if (hit != null) {
                onHitEntity(hit);
                return;
            }

            if (isSolidAt(nx, ny, nz)) {
                x = nx - sx * 0.3;
                y = ny - sy * 0.3;
                z = nz - sz * 0.3;
                vx = vy = vz = 0;
                inGround = true;
                onGround = true;
                broadcastStuckSound();
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

    /** 命中检测: 遍历生物实体 + 在线玩家 (修复: 玩家不在实体 Map 中, 原实现箭永远打不中玩家)。 */
    private Object findEntityHit(double nx, double ny, double nz) {
        Object best = null;
        double bestSq = Double.MAX_VALUE;
        for (Entity e : EntityManager.getAllEntities()) {
            if (!(e instanceof LivingEntity living)) continue;
            if (living.dim != this.dim) continue;
            if (living.deathTime > 0 || living.health <= 0) continue;
            if (piercedIds.contains(living.id)) continue;
            if (life < 5 && shooter != null
                && Math.abs(living.x - shooter.x) < 1.5
                && Math.abs(living.z - shooter.z) < 1.5) continue;

            double hw = living.width / 2.0 + 0.3;
            if (nx < living.x - hw || nx > living.x + hw) continue;
            if (nz < living.z - hw || nz > living.z + hw) continue;
            if (ny < living.y - 0.3 || ny > living.y + living.height + 0.3) continue;

            double dx = nx - living.x, dy = ny - living.y, dz = nz - living.z;
            double sq = dx * dx + dy * dy + dz * dz;
            if (sq < bestSq) { bestSq = sq; best = living; }
        }
        // 玩家: 碰撞箱 0.6 宽 / 1.8 高(从脚底起)
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null || p.isDead || p.gameMode == 3) continue;
            if (p.currentDim != this.dim) continue;
            if (piercedIds.contains(p.eid)) continue;
            if (life < 5 && p == shooter) continue; // 射手免疫(刚射出)
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
        if (isCritical) dmg += 1 + (int) (Math.random() * (dmg / 2 + 1));
        if (dmg < 1) dmg = 1;

        if (target instanceof NetworkHandler player) {
            // 命中玩家: 走护甲/盾牌/吸收心统一结算
            player.damagePlayer(dmg, "arrow", x, z);
            double horiz = Math.sqrt(vx * vx + vz * vz);
            if (horiz > 0.001 && !player.isDead) {
                player.knockback(vx / horiz * 0.6, vz / horiz * 0.6, 0.1);
            }
            if (shooter != null) {
                shooter.sendSoundAt("minecraft:entity.arrow.hit_player", x, y, z, 1.0f, 1.2f);
            }
            if (piercing > 0) { piercing--; piercedIds.add(player.eid); return; }
            remove();
            return;
        }

        LivingEntity target2 = (LivingEntity) target;
        // P4-2: 记录射手的击杀归属, 由 MobEntity.onDeath 统一生成经验球(避免双重发放)
        if (shooter != null && target2 instanceof MobEntity) {
            ((MobEntity) target2).lastAttacker = shooter;
        }

        target2.damage(dmg, "arrow");

        double horiz = Math.sqrt(vx * vx + vz * vz);
        if (horiz > 0.001 && !(target2 instanceof EnderDragonEntity)) {
            target2.vx += vx / horiz * 0.6;
            target2.vz += vz / horiz * 0.6;
        }

        if (shooter != null) {
            shooter.sendSoundAt("minecraft:entity.arrow.hit_player", x, y, z, 1.0f, 1.2f);
        }
        if (piercing > 0) { piercing--; piercedIds.add(target2.id); return; }
        remove();
    }

    private void tryPickup() {
        if (!pickupable) return;
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.currentDim != this.dim || p.isDead) continue;
            double dx = p.x - x, dy = (p.y + 0.9) - y, dz = p.z - z;
            if (dx * dx + dy * dy + dz * dz > 2.25) continue;
            if (p.gameMode == 0 || p.gameMode == 2) {
                int itemId = BlockManager.getItemIdByName("arrow");
                if (itemId > 0) p.giveItem(itemId, 1);
            }
            p.sendSoundAt("minecraft:entity.item.pickup", x, y, z, 0.2f, 1.6f);
            remove();
            return;
        }
    }

    private void broadcastStuckSound() {
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.currentDim != this.dim) continue;
            double dx = p.x - x, dz = p.z - z;
            if (dx * dx + dz * dz < 1024.0) {
                p.sendSoundAt("minecraft:entity.arrow.hit", x, y, z, 1.0f, 1.2f);
            }
        }
    }

    private void updateRotation() {
        double horiz = Math.sqrt(vx * vx + vz * vz);
        if (horiz > 0.0001 || Math.abs(vy) > 0.0001) {
            this.yaw = (float) Math.toDegrees(Math.atan2(vx, vz));
            this.pitch = (float) Math.toDegrees(Math.atan2(vy, horiz));
        }
    }
}
