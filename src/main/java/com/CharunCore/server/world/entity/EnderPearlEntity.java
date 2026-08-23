package com.CharunCore.server.world.entity;

import com.CharunCore.server.network.NetworkHandler;

/**
 * 末影珍珠。命中方块或生物时把投掷者传送到落点, 并造成 5 点无视护甲的传送伤害。
 */
public class EnderPearlEntity extends Entity {

    private static final double GRAVITY = 0.03;
    private static final double DRAG = 0.99;

    private final NetworkHandler thrower;
    private int life = 0;

    public EnderPearlEntity(int id, double x, double y, double z,
                            double vx, double vy, double vz, NetworkHandler thrower) {
        super(id, 0, x, y, z);
        this.typeName = "ender_pearl";
        this.noPhysics = true;
        this.width = 0.25;
        this.height = 0.25;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.thrower = thrower;
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

            Object hit = findEntityHit(nx, ny, nz);
            if (hit != null) {
                if (hit instanceof LivingEntity living) {
                    living.damage(0.0f, "ender_pearl");
                }
                teleportThrower(x, y, z);
                return;
            }
            if (isSolidAt(nx, ny, nz)) {
                teleportThrower(x, y + 0.2, z);
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

    /** 命中检测: 生物 + 在线玩家 (修复: 玩家不在实体 Map 中, 原实现珍珠穿过玩家)。 */
    private Object findEntityHit(double nx, double ny, double nz) {
        for (Entity e : EntityManager.getAllEntities()) {
            if (!(e instanceof LivingEntity living)) continue;
            if (living.dim != this.dim) continue;
            if (living.deathTime > 0 || living.health <= 0) continue;
            if (life < 4) continue;
            double hw = living.width / 2.0 + 0.25;
            if (nx < living.x - hw || nx > living.x + hw) continue;
            if (nz < living.z - hw || nz > living.z + hw) continue;
            if (ny < living.y - 0.25 || ny > living.y + living.height + 0.25) continue;
            return living;
        }
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null || p.isDead || p.gameMode == 3 || p == thrower) continue;
            if (p.currentDim != this.dim) continue;
            if (life < 4) continue;
            if (nx < p.x - 0.55 || nx > p.x + 0.55) continue;
            if (nz < p.z - 0.55 || nz > p.z + 0.55) continue;
            if (ny < p.y - 0.25 || ny > p.y + 2.05) continue;
            return p;
        }
        return null;
    }

    private void teleportThrower(double tx, double ty, double tz) {
        if (thrower != null && !thrower.isDead && thrower.currentDim == this.dim) {
            int landY = findLandingY((int) Math.floor(tx), (int) Math.floor(ty), (int) Math.floor(tz));
            thrower.teleportTo(tx, landY, tz);
            if (thrower.gameMode == 0) {
                thrower.damagePlayer(5.0f, "ender_pearl");
            }
            thrower.sendSoundAt("minecraft:entity.enderman.teleport", tx, landY, tz, 1.0f, 1.0f);
        }
        remove();
    }

    private int findLandingY(int bx, int by, int bz) {
        for (int y = Math.min(by + 3, 318); y > by - 6; y--) {
            if (!isSolidAt(bx + 0.5, y, bz + 0.5) && !isSolidAt(bx + 0.5, y + 1, bz + 0.5)) {
                if (isSolidAt(bx + 0.5, y - 1, bz + 0.5)) return y;
            }
        }
        return by + 1;
    }
}
