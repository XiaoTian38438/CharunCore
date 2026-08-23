package com.CharunCore.server.world.entity;

import com.CharunCore.server.network.NetworkHandler;

/**
 * 经验球。带简单重力与地面吸附, 玩家靠近时被吸引并拾取。
 */
public class ExperienceOrbEntity extends Entity {

    public final int value;
    private int age = 0;
    private int pickupDelay = 10;

    public ExperienceOrbEntity(int id, double x, double y, double z, int value) {
        super(id, 0, x, y, z);
        this.typeName = "experience_orb";
        this.width = 0.5;
        this.height = 0.5;
        this.value = Math.max(1, value);
    }

    @Override
    public void tick() {
        age++;
        if (age > 6000) {
            remove();
            return;
        }
        if (pickupDelay > 0) pickupDelay--;

        NetworkHandler nearest = null;
        double bestSq = 64.0;
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.currentDim != this.dim || p.isDead) continue;
            double dx = p.x - x, dy = (p.y + 0.8) - y, dz = p.z - z;
            double sq = dx * dx + dy * dy + dz * dz;
            if (sq < bestSq) { bestSq = sq; nearest = p; }
        }

        if (nearest != null) {
            double dx = nearest.x - x;
            double dy = (nearest.y + 0.8) - y;
            double dz = nearest.z - z;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist > 0.001) {
                double pull = (1.0 - dist / 8.0) * 0.12;
                vx += dx / dist * pull;
                vy += dy / dist * pull;
                vz += dz / dist * pull;
            }
            if (pickupDelay <= 0 && dist < 1.2) {
                nearest.addExperience(value);
                nearest.sendSoundAt("minecraft:entity.experience_orb.pickup", x, y, z, 0.2f, 1.4f);
                remove();
                return;
            }
        } else {
            vy -= 0.03;
        }

        vx *= 0.98;
        vy *= 0.98;
        vz *= 0.98;

        if (isSolidAt(x, y - 0.1, z)) {
            if (vy < 0) vy = 0;
            vx *= 0.7;
            vz *= 0.7;
            onGround = true;
        } else {
            onGround = false;
            vy -= 0.03;
        }

        if (!isSolidAt(x + vx, y, z + vz)) {
            x += vx;
            z += vz;
        } else {
            vx = 0; vz = 0;
        }
        if (!isSolidAt(x, y + vy, z)) {
            y += vy;
        } else if (vy < 0) {
            vy = 0;
        }

        if (y < -80) remove();
    }
}
