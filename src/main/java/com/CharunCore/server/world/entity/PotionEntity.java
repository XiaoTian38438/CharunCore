package com.CharunCore.server.world.entity;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;

/** Bug52: 喷溅/滞留药水投掷物。落地或命中生物时碎裂,
 *  按携带的效果串对半径内玩家施加效果; 滞留型半径更大但时长减半(近似原版云)。 */
public class PotionEntity extends Entity {

    private static final double GRAVITY = 0.05;
    private static final double DRAG = 0.99;

    private final NetworkHandler thrower;
    private final boolean lingering;
    private final String effects; // "effect|amp|dur" 逗号分隔, 与 inventoryPotion 格式一致
    private int life = 0;

    public PotionEntity(int id, String typeName, double x, double y, double z,
                        double vx, double vy, double vz,
                        NetworkHandler thrower, boolean lingering, String effects) {
        super(id, 0, x, y, z);
        this.typeName = typeName;
        this.noPhysics = true;
        this.width = 0.25;
        this.height = 0.25;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.thrower = thrower;
        this.lingering = lingering;
        this.effects = effects;
        if (thrower != null) this.dim = thrower.currentDim;
    }

    @Override
    public void tick() {
        life++;
        if (life > 600) {
            remove();
            return;
        }

        double speed = Math.sqrt(vx * vx + vy * vy + vz * vz);
        int steps = Math.max(1, Math.min(8, (int) Math.ceil(speed / 0.3)));
        double sx = vx / steps, sy = vy / steps, sz = vz / steps;

        for (int i = 0; i < steps; i++) {
            double nx = x + sx, ny = y + sy, nz = z + sz;

            if (findEntityHit(nx, ny, nz) || isSolidAt(nx, ny, nz)) {
                smash();
                return;
            }
            x = nx; y = ny; z = nz;
        }

        vx *= DRAG;
        vy *= DRAG;
        vz *= DRAG;
        vy -= GRAVITY;

        this.yaw = (float) Math.toDegrees(Math.atan2(vx, vz));

        if (y < -80) remove();
    }

    private boolean findEntityHit(double nx, double ny, double nz) {
        for (Entity e : EntityManager.getAllEntities()) {
            if (!(e instanceof LivingEntity living)) continue;
            if (living.dim != this.dim) continue;
            if (living.deathTime > 0 || living.health <= 0) continue;
            if (life < 4) continue;
            double hw = living.width / 2.0 + 0.25;
            if (nx < living.x - hw || nx > living.x + hw) continue;
            if (nz < living.z - hw || nz > living.z + hw) continue;
            if (ny < living.y - 0.25 || ny > living.y + living.height + 0.25) continue;
            return true;
        }
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null || p.isDead || p.gameMode == 3) continue;
            if (p.currentDim != this.dim) continue;
            if (life < 4) continue;
            if (nx < p.x - 0.55 || nx > p.x + 0.55) continue;
            if (nz < p.z - 0.55 || nz > p.z + 0.55) continue;
            if (ny < p.y - 0.25 || ny > p.y + 2.05) continue;
            return true;
        }
        return false;
    }

    private void smash() {
        // 命中音效(碎裂)给附近玩家
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.currentDim != this.dim || p.ctx == null) continue;
            double dx = p.x - x, dz = p.z - z;
            if (dx * dx + dz * dz < 1024.0) {
                p.sendSoundAt("minecraft:entity.potion.splash", x, y, z, 1.0f, 1.0f);
            }
        }
        double radius = lingering ? 5.0 : 4.0;
        if (effects != null && !effects.isEmpty()) {
            for (NetworkHandler p : NetworkHandler.players.values()) {
                if (p.ctx == null || p.isDead || p.gameMode == 3) continue;
                if (p.currentDim != this.dim) continue;
                double dx = p.x - x, dy = (p.y + 0.9) - y, dz = p.z - z;
                if (dx * dx + dy * dy + dz * dz > radius * radius) continue;
                // 滞留型效果时长减半(近似原版 AreaEffectCloud 的持续衰减)
                int scale = lingering ? 2 : 1;
                for (String e : effects.split(",")) {
                    String[] kv = e.split("\\|");
                    if (kv.length >= 3) {
                        try {
                            p.addEffect(kv[0], Integer.parseInt(kv[1]),
                                    Math.max(20, Integer.parseInt(kv[2]) / scale));
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
        }
        remove();
    }
}
