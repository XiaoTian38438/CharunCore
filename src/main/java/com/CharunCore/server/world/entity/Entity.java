package com.CharunCore.server.world.entity;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.WorldManager;

public class Entity {
    public int id;
    public int type;
    public double x, y, z;
    /** 上一次同步给客户端的位置/朝向, 供 rel_entity_move 增量编码。 */
    public transient double prevX, prevY, prevZ;
    /** 本 tick 相对上一次同步的位置增量(syncTracking 每 tick 计算一次)。 */
    public transient double dxMove, dyMove, dzMove;
    /** 本 tick 是否需要以 full teleport(0x23) 同步(位移过大或朝向变化)。 */
    public transient boolean needsTeleport = false;
    /** 本 tick yaw 是否变化(是否发送 head_rotation 0x51)。 */
    public transient boolean needHeadRot = false;
    /** 是否已至少同步过一次位置(用于静止实体跳过重复发包)。 */
    public transient boolean syncedOnce = false;
    public float yaw, pitch;
    public float prevYaw, prevPitch;
    public boolean onGround;
    public double vx, vy, vz;
    public float health = 20.0f;
    public int airTicks = 300;
    public int fireTicks = 0;
    private int fireDamageTickCounter = 0;  // 着火伤害计时：每 20 tick 造 1 点火伤
    public float fallDistance = 0;
    public int hurtTime = 0;
    public int deathTime = 0;
    public DimensionType dim = DimensionType.OVERWORLD;
    public double width = 0.6;
    public double height = 1.8;
    public boolean noPhysics = false;
    public String typeName = null;
    /** TNT 实体剩余引信刻数 (-1 = 非点燃 TNT)。 */
    public int tntFuse = -1;

    public Entity(int id, int type, double x, double y, double z) {
        this.id = id;
        this.type = type;
        this.x = x; this.y = y; this.z = z;
    }

    protected boolean isSolidAt(double wx, double wy, double wz) {
        int state = WorldManager.getBlockState(dim, (int) Math.floor(wx), (int) Math.floor(wy), (int) Math.floor(wz));
        if (state == 0) return false;
        String n = BlockStateHelper.getName(state);
        return !("water".equals(n) || "lava".equals(n) || "air".equals(n)
            || "cave_air".equals(n) || "void_air".equals(n)
            || n.endsWith("_sign") || n.equals("short_grass") || n.equals("tall_grass")
            || n.equals("fern") || n.equals("large_fern") || n.equals("dead_bush")
            || n.endsWith("_torch") || n.equals("torch") || n.equals("vine")
            || n.endsWith("_sapling") || n.equals("dandelion") || n.equals("poppy")
            || n.equals("blue_orchid") || n.equals("allium") || n.equals("cornflower")
            || n.equals("snow") || n.equals("nether_portal") || n.equals("end_portal")
            || n.equals("lily_pad")); // 荷叶: 薄片(1/16)非实心, 玩家可穿过游出, 防卡死溺水
    }

    protected boolean collidesHorizontally(double nx, double nz) {
        double half = width / 2.0;
        for (double oy = 0.05; oy < height; oy += Math.max(0.5, height / 2.0)) {
            for (double ox = -half; ox <= half; ox += half * 2) {
                for (double oz = -half; oz <= half; oz += half * 2) {
                    if (isSolidAt(nx + ox, y + oy, nz + oz)) return true;
                }
            }
        }
        return false;
    }

    public boolean isInFluid(String fluidName) {
        int state = WorldManager.getBlockState(dim, (int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
        return fluidName.equals(BlockStateHelper.getName(state));
    }

    public void damage(float amount, String source) {}

    public void tick() {
        if (fireTicks > 0) {
            fireTicks--;
            // 原版：着火实体每 20 tick（1 秒）受 1 点火伤（fireImmune 的实体在子类中跳过）
            fireDamageTickCounter++;
            if (fireDamageTickCounter >= 20) {
                fireDamageTickCounter = 0;
                damage(1.0f, "onFire");
            }
        } else {
            fireDamageTickCounter = 0;
        }
        if (hurtTime > 0) hurtTime--;
        if (deathTime > 0) {
            deathTime++;
            if (deathTime > 20) {
                remove();
            }
            return;
        }

        if (noPhysics) {
            x += vx; y += vy; z += vz;
            return;
        }

        // 结构生物在区块生成线程提前入表: 所在区块尚未注册进 WorldManager 时
        // getBlockState 返回空气, 实体会凭空下坠(落到地面时已掉进地形里)。
        // 区块未加载则冻结本 tick(不积重力), 加载后自然落地。
        if (WorldManager.getChunkCached(dim, ((int) Math.floor(x)) >> 4, ((int) Math.floor(z)) >> 4) == null) {
            return;
        }

        boolean inWater = isInFluid("water");

        vy -= inWater ? 0.02 : 0.08;
        if (inWater) {
            vy *= 0.8;
            vx *= 0.8;
            vz *= 0.8;
        }
        if (vy < -3.0) vy = -3.0;

        double nx = x + vx;
        double nz = z + vz;
        if (vx != 0 && !collidesHorizontally(nx, z)) {
            x = nx;
        } else if (vx != 0) {
            if (canStepUp(nx, z)) { x = nx; y += 1.0; }
            vx = 0;
        }
        if (vz != 0 && !collidesHorizontally(x, nz)) {
            z = nz;
        } else if (vz != 0) {
            if (canStepUp(x, nz)) { z = nz; y += 1.0; }
            vz = 0;
        }

        double ny = y + vy;
        if (vy < 0) {
            double floorY = Math.floor(ny);
            if (isSolidAt(x, ny, z) || isSolidAt(x, floorY, z)) {
                double landY = Math.floor(ny) + 1.0;
                if (y >= landY - 0.001) {
                    y = landY;
                    if (fallDistance > 3 && this instanceof LivingEntity le) {
                        le.damage(fallDistance - 3, "fall");
                    }
                    fallDistance = 0;
                    vy = 0;
                    onGround = true;
                } else {
                    y = ny;
                    onGround = false;
                }
            } else {
                y = ny;
                onGround = false;
                fallDistance += (float) -vy;
            }
        } else if (vy > 0) {
            if (isSolidAt(x, y + height + vy, z)) {
                vy = 0;
            } else {
                y = ny;
            }
            onGround = false;
        } else {
            onGround = isSolidAt(x, y - 0.05, z);
            if (!onGround) {
                y = ny;
            }
        }

        if (onGround) {
            vx *= 0.6;
            vz *= 0.6;
        } else {
            vx *= 0.91;
            vz *= 0.91;
        }
        if (Math.abs(vx) < 0.003) vx = 0;
        if (Math.abs(vz) < 0.003) vz = 0;

        if (y < dim.minY - 16) {
            remove();
        }
    }

    private boolean canStepUp(double nx, double nz) {
        if (!onGround) return false;
        if (isSolidAt(nx, y + 1.0, nz)) return false;
        if (isSolidAt(nx, y + 1.0 + height, nz)) return false;
        return true;
    }

    public void remove() {
        EntityManager.removeEntity(id);
    }

    public int getChunkX() { return (int) Math.floor(x) >> 4; }
    public int getChunkZ() { return (int) Math.floor(z) >> 4; }
}
