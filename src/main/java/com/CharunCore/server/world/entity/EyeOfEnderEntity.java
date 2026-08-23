package com.CharunCore.server.world.entity;

import com.CharunCore.server.utils.BlockManager;

public class EyeOfEnderEntity extends Entity {

    private final double targetX;
    private final double targetZ;
    private final double startY;
    private final boolean survives;
    private final int maxLife;
    private int life = 0;

    public EyeOfEnderEntity(int id, double x, double y, double z,
                            double targetX, double targetZ, java.util.Random rng) {
        super(id, 0, x, y, z);
        this.typeName = "eye_of_ender";
        this.noPhysics = true;
        this.width = 0.25;
        this.height = 0.25;
        this.targetX = targetX;
        this.targetZ = targetZ;
        this.startY = y;
        double dx = targetX - x;
        double dz = targetZ - z;
        double horiz = Math.sqrt(dx * dx + dz * dz);
        this.maxLife = (int) Math.max(30, Math.min(110, 25 + horiz * 0.6));
        this.survives = rng.nextInt(5) != 0;
    }

    @Override
    public void tick() {
        life++;

        double dx = targetX - x;
        double dz = targetZ - z;
        double horiz = Math.sqrt(dx * dx + dz * dz);

        double speed = 0.85;
        if (horiz > 0.4) {
            vx = dx / horiz * speed;
            vz = dz / horiz * speed;
        } else {
            vx = 0;
            vz = 0;
        }

        int descentStart = maxLife - 12;
        if (life < 10) {
            vy = 0.45;
        } else if (life < descentStart) {
            double desired = startY + 9.0;
            vy = Math.max(-0.2, Math.min(0.2, (desired - y) * 0.1));
        } else {
            vy = -0.35;
        }

        x += vx;
        y += vy;
        z += vz;

        this.yaw = (float) Math.toDegrees(Math.atan2(-vx, vz));

        if (life >= maxLife || (horiz < 1.0 && life > 15)) {
            finish();
        }
    }

    private void finish() {
        if (survives) {
            int itemId = BlockManager.getItemIdByName("ender_eye");
            if (itemId <= 0) itemId = BlockManager.getItemIdByName("eye_of_ender");
            if (itemId > 0) {
                ItemEntity drop = new ItemEntity(EntityManager.allocateId(), x, y, z, itemId, 1);
                drop.dim = this.dim;
                EntityManager.addEntity(drop);
            }
        }
        remove();
    }
}
