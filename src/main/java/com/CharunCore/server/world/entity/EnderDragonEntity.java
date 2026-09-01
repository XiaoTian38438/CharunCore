package com.CharunCore.server.world.entity;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.world.DimensionType;

public class EnderDragonEntity extends LivingEntity {

    public static final double CENTER_X = 0.5;
    public static final double CENTER_Z = 0.5;

    // B11: 原版阶段近似 (HoldingPattern / StrafePlayer / LandingApproach / Landing /
    //      SittingFlaming / Takeoff / Death)
    private static final int PHASE_CIRCLING = 0;
    private static final int PHASE_CHARGING = 1;
    private static final int PHASE_PERCHING = 2;
    private static final int PHASE_TAKEOFF = 3;
    private static final int PHASE_LANDING = 4;
    private static final int PHASE_STRAFE = 5;
    private static final int PHASE_DEATH = 6;

    private int phase = PHASE_CIRCLING;
    private int phaseTimer = 0;
    private double angle = 0.0;
    private double orbitRadius = 42.0;
    private double cruiseY;
    private double targetX, targetY, targetZ;
    private int attackCooldown = 0;
    private int breathCooldown = 0;
    private double strafeAngle = 0.0;
    private final java.util.Random rng = new java.util.Random();

    public EnderDragonEntity(int id, double podiumY) {
        super(id, 0, CENTER_X, podiumY + 40.0, CENTER_Z + 42.0);
        this.typeName = "ender_dragon";
        this.noPhysics = true;
        this.width = 16.0;
        this.height = 8.0;
        this.maxHealth = 200.0f;
        this.health = 200.0f;
        this.cruiseY = podiumY + 40.0;
        this.dim = DimensionType.THE_END;
    }

    public int getPhase() {
        return phase;
    }

    public boolean isPerching() {
        return phase == PHASE_PERCHING;
    }

    @Override
    public void damage(float amount, String source) {
        if (deathTime > 0) return;
        // B4: 水晶存活时，仅末地水晶爆炸("crystal")可造成伤害，其余来源近似免疫
        if (EndDragonFight.hasLivingCrystals() && !"crystal".equals(source)) {
            return;
        }
        if (invulnTicks > 0) return;
        this.health -= amount;
        this.hurtTime = 10;
        this.invulnTicks = 8;
        this.lastDamageSource = source;
        EntityManager.broadcastEntityHurt(this);
        if (this.health <= 0) {
            this.health = 0;
            this.deathTime = 1;
            onDeath();
        }
    }

    @Override
    protected void onDeath() {
        phase = PHASE_DEATH;
        EndDragonFight.onDragonDeath(this);
    }

    @Override
    public void tick() {
        if (deathTime > 0) {
            deathTime++;
            tickDeath();
            x += vx;
            y += vy;
            z += vz;
            if (deathTime > 200) remove();
            return;
        }
        if (invulnTicks > 0) invulnTicks--;
        if (hurtTime > 0) hurtTime--;
        if (attackCooldown > 0) attackCooldown--;
        if (breathCooldown > 0) breathCooldown--;

        // B4: 水晶存活时强化回血（约 10 HP/s）
        if (EndDragonFight.hasLivingCrystals() && health < maxHealth) {
            health = Math.min(maxHealth, health + 0.5f);
        }

        phaseTimer--;
        if (phaseTimer <= 0) {
            chooseNextPhase();
        }

        switch (phase) {
            case PHASE_CHARGING -> tickCharging();
            case PHASE_STRAFE -> tickStrafe();
            case PHASE_LANDING -> tickLanding();
            case PHASE_TAKEOFF -> tickTakeoff();
            case PHASE_PERCHING -> tickPerching();
            case PHASE_DEATH -> tickDeath();
            default -> tickCircling();
        }

        x += vx;
        y += vy;
        z += vz;

        damageNearbyPlayers();
    }

    private void chooseNextPhase() {
        // B11: 栖息结束后先起飞，再进入常规阶段
        if (phase == PHASE_PERCHING) {
            phase = PHASE_TAKEOFF;
            phaseTimer = 70;
            return;
        }
        NetworkHandler target = nearestPlayer(200.0);
        int roll = rng.nextInt(10);
        if (target != null && roll < 3) {
            phase = PHASE_CHARGING;
            phaseTimer = 90;
            targetX = target.x;
            targetY = target.y + 1.5;
            targetZ = target.z;
        } else if (target != null && roll < 5) {
            phase = PHASE_STRAFE;
            phaseTimer = 130;
            strafeAngle = rng.nextDouble() * Math.PI * 2.0;
            targetX = target.x;
            targetY = target.y + 2.5;
            targetZ = target.z;
        } else if (target != null && roll < 8) {
            phase = PHASE_LANDING;
            phaseTimer = 150;
            targetX = CENTER_X;
            targetY = EndDragonFight.getPodiumY() + 3.0;
            targetZ = CENTER_Z;
        } else {
            phase = PHASE_CIRCLING;
            phaseTimer = 200 + rng.nextInt(200);
            orbitRadius = 30.0 + rng.nextInt(20);
            cruiseY = EndDragonFight.getPodiumY() + 25.0 + rng.nextInt(20);
        }
    }

    private void tickCircling() {
        angle += 0.025;
        double tx = CENTER_X + Math.cos(angle) * orbitRadius;
        double tz = CENTER_Z + Math.sin(angle) * orbitRadius;
        moveToward(tx, cruiseY, tz, 0.85);
        faceMovementDragon();
    }

    private void tickCharging() {
        NetworkHandler target = nearestPlayer(200.0);
        if (target != null && phaseTimer % 20 == 0) {
            targetX = target.x;
            targetY = target.y + 1.0;
            targetZ = target.z;
        }
        moveToward(targetX, targetY, targetZ, 1.1);
        faceMovementDragon();
    }

    private void tickStrafe() {
        NetworkHandler target = nearestPlayer(200.0);
        if (target != null && phaseTimer % 15 == 0) {
            strafeAngle += Math.PI / 3.0;
            double off = 7.0;
            targetX = target.x + Math.cos(strafeAngle) * off;
            targetY = target.y + 2.5;
            targetZ = target.z + Math.sin(strafeAngle) * off;
        }
        moveToward(targetX, targetY, targetZ, 1.0);
        faceMovementDragon();
    }

    private void tickTakeoff() {
        moveToward(CENTER_X, cruiseY + 14.0, CENTER_Z, 0.8);
        faceMovementDragon();
    }

    private void tickLanding() {
        moveToward(targetX, targetY, targetZ, 0.7);
        double dx = targetX - x;
        double dz = targetZ - z;
        if (dx * dx + dz * dz < 9.0) {
            phase = PHASE_PERCHING;
            phaseTimer = 200;
        }
        faceMovementDragon();
    }

    private void tickPerching() {
        moveToward(targetX, targetY, targetZ, 0.7);
        double dx = targetX - x;
        double dz = targetZ - z;
        if (dx * dx + dz * dz < 9.0) {
            vx *= 0.3;
            vz *= 0.3;
            if (breathCooldown <= 0) {
                breathAttack();
                breathCooldown = 60;
            }
        }
        faceMovementDragon();
    }

    private void tickDeath() {
        vy += 0.03;
        vx *= 0.92;
        vz *= 0.92;
        faceMovementDragon();
    }

    private void breathAttack() {
        for (NetworkHandler player : NetworkHandler.players.values()) {
            if (player.currentDim != this.dim || player.isDead) continue;
            if (player.gameMode == 1 || player.gameMode == 3) continue;
            double dx = player.x - x;
            double dy = player.y - y;
            double dz = player.z - z;
            if (dx * dx + dy * dy + dz * dz < 100.0) {
                player.damagePlayer(6.0f, "dragon_breath");
            }
            player.sendSoundAt("minecraft:entity.ender_dragon.growl", x, y, z, 5.0f, 1.0f);
        }
    }

    private void moveToward(double tx, double ty, double tz, double speed) {
        double dx = tx - x;
        double dy = ty - y;
        double dz = tz - z;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist < 0.001) {
            vx = vy = vz = 0;
            return;
        }
        double ax = dx / dist * speed;
        double ay = dy / dist * speed;
        double az = dz / dist * speed;
        // Bug25: 加快 lerp(0.15->0.3) 让龙更快达到目标速度, 原值下龙启动极慢("速度非常慢")。
        vx += (ax - vx) * 0.3;
        vy += (ay - vy) * 0.3;
        vz += (az - vz) * 0.3;
    }

    private void faceMovement() {
        double speed = Math.sqrt(vx * vx + vy * vy + vz * vz);
        // Bug25: 阈值从 0.002 降到 0.0 -- 曾在低速时跳过 yaw 更新, 龙始终朝一个方向(看起来"倒着飞")。
        if (speed < 1e-6) return;
        double horiz = Math.sqrt(vx * vx + vz * vz);
        if (horiz > 1e-6) {
            this.yaw = (float) Math.toDegrees(Math.atan2(-vx, vz));
            this.yaw = ((this.yaw % 360) + 360) % 360;
        }
        this.pitch = (float) -Math.toDegrees(Math.atan2(vy, Math.max(horiz, 1e-6)));
    }

    /** Bug25: 龙模型朝向与普通生物相反(客户端 EnderDragonRenderer 机身轴向翻转),
     *  按 atan2(-vx,vz) 朝向的龙在客户端看是"倒着飞", 需 +180°。 */
    private void faceMovementDragon() {
        faceMovement();
        this.yaw = (this.yaw + 180.0f) % 360.0f;
    }

    private void damageNearbyPlayers() {
        if (attackCooldown > 0) return;
        for (NetworkHandler player : NetworkHandler.players.values()) {
            if (player.currentDim != this.dim || player.isDead) continue;
            if (player.gameMode == 1 || player.gameMode == 3) continue;
            double dx = player.x - x;
            double dy = player.y - y;
            double dz = player.z - z;
            if (dx * dx + dy * dy + dz * dz > 49.0) continue;
            player.damagePlayer(10.0f, "dragon");
            double horiz = Math.sqrt(dx * dx + dz * dz);
            if (horiz < 0.001) horiz = 0.001;
            player.knockback(dx / horiz * 1.4, dz / horiz * 1.4, 0.8);
            attackCooldown = 25;
            break;
        }
    }

    private NetworkHandler nearestPlayer(double maxDist) {
        NetworkHandler best = null;
        double bestSq = maxDist * maxDist;
        for (NetworkHandler player : NetworkHandler.players.values()) {
            if (player.currentDim != this.dim || player.isDead) continue;
            if (player.gameMode == 1 || player.gameMode == 3) continue;
            double dx = player.x - x;
            double dy = player.y - y;
            double dz = player.z - z;
            double sq = dx * dx + dy * dy + dz * dz;
            if (sq < bestSq) {
                bestSq = sq;
                best = player;
            }
        }
        return best;
    }
}
