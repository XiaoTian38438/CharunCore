package com.CharunCore.server.world.entity;

import com.CharunCore.server.network.NetworkHandler;

/**
 * 女巫投掷的药水(射弹)。命中玩家后造成少量伤害并附加缓慢/中毒, 落地点短暂滞留后消失。
 * P4-3: 作为女巫的远程攻击手段。
 */
public class WitchPotionEntity extends Entity {
    private static final double GRAVITY = 0.05;
    public NetworkHandler shooter;
    private final double damageAmount = 3.0;
    private int life = 0;
    private boolean landed = false;
    private int landedTicks = 0;

    public WitchPotionEntity(int id, double x, double y, double z,
                             double vx, double vy, double vz, NetworkHandler shooter) {
        super(id, 0, x, y, z);
        // 复用 arrow 的客户端实体模型(type id 6), 避免回退到默认实体(显示为僵尸)造成视觉异常
        this.typeName = "arrow";
        this.noPhysics = true;
        this.width = 0.4;
        this.height = 0.4;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.shooter = shooter;
        if (shooter != null) this.dim = shooter.currentDim;
    }

    @Override
    public void tick() {
        life++;
        if (life > 400) {
            remove();
            return;
        }
        if (landed) {
            if (++landedTicks > 40) remove();
            return;
        }
        double speed = Math.sqrt(vx * vx + vy * vy + vz * vz);
        int steps = Math.max(1, Math.min(6, (int) Math.ceil(speed / 0.35)));
        double sx = vx / steps, sy = vy / steps, sz = vz / steps;
        for (int i = 0; i < steps; i++) {
            double nx = x + sx, ny = y + sy, nz = z + sz;
            NetworkHandler hit = findPlayerHit(nx, ny, nz);
            if (hit != null) {
                onHit(hit);
                return;
            }
            if (isSolidAt(nx, ny, nz)) {
                x = nx; y = ny; z = nz;
                landed = true;
                return;
            }
            x = nx; y = ny; z = nz;
        }
        vy -= GRAVITY;
        vx *= 0.99;
        vz *= 0.99;
        if (y < -80) remove();
    }

    private NetworkHandler findPlayerHit(double nx, double ny, double nz) {
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.currentDim != this.dim || p.isDead) continue;
            double hw = 0.6;
            if (nx < p.x - hw || nx > p.x + hw) continue;
            if (nz < p.z - hw || nz > p.z + hw) continue;
            if (ny < p.y - 0.3 || ny > p.y + 1.8 + 0.3) continue;
            return p;
        }
        return null;
    }

    private void onHit(NetworkHandler target) {
        target.damagePlayer((float) damageAmount, "mob", x, z);
        target.addEffect("slowness", 0, 100); // 减速 ~5 秒
        target.addEffect("poison", 0, 80);    // 中毒 ~4 秒
        if (shooter != null) shooter.sendSoundAt("minecraft:entity.witch.throw", x, y, z, 1.0f, 1.0f);
        remove();
    }
}
