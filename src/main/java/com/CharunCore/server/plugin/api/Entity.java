package com.CharunCore.server.plugin.api;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.entity.EntityManager;

/** 实体门面(对标 Paper Entity): 包装底层 com.CharunCore.server.world.entity.Entity。 */
public class Entity {

    protected final com.CharunCore.server.world.entity.Entity h;

    Entity(com.CharunCore.server.world.entity.Entity handle) {
        this.h = handle;
    }

    public com.CharunCore.server.world.entity.Entity getHandle() { return h; }

    /** 实体唯一 id (eid)。 */
    public int getEntityId() { return h.id; }

    /** 类型名 (zombie/creeper/item/tnt...)。 */
    public String getType() { return h.typeName != null ? h.typeName : "unknown"; }

    public double getX() { return h.x; }
    public double getY() { return h.y; }
    public double getZ() { return h.z; }
    public float getYaw() { return h.yaw; }
    public float getPitch() { return h.pitch; }

    public DimensionType getWorld() { return h.dim; }

    /** 是否仍存活于世界。 */
    public boolean isValid() { return EntityManager.getEntity(h.id) == h; }

    /** 从世界中移除该实体。 */
    public void remove() { EntityManager.removeEntity(h.id); }

    /** 传送(同维度, 广播 0x7B entity_teleport)。 */
    public void teleport(double x, double y, double z) {
        h.x = x; h.y = y; h.z = z;
        h.needsTeleport = true;
        NetworkHandler.broadcastAll(0x7B, pb -> {
            pb.writeVarInt(h.id);
            pb.writeDouble(x); pb.writeDouble(y); pb.writeDouble(z);
            byte yawB = (byte) (h.yaw / 360f * 256f);
            byte pitchB = (byte) (h.pitch / 360f * 256f);
            pb.writeByte(yawB); pb.writeByte(pitchB);
            pb.writeBoolean(h.onGround);
        });
    }

    /** 设置速度(广播 0x63 set_entity_motion)。 */
    public void setVelocity(double vx, double vy, double vz) {
        h.vx = vx; h.vy = vy; h.vz = vz;
        NetworkHandler.broadcastAll(0x63, pb -> {
            pb.writeVarInt(h.id);
            pb.writeLpVec3(vx, vy, vz);
        });
    }

    public int getFireTicks() { return h.fireTicks; }
    public void setFireTicks(int ticks) {
        h.fireTicks = Math.max(0, ticks);
        EntityManager.broadcastEntityFire(h, ticks > 0);
    }

    public boolean isOnGround() { return h.onGround; }

    /** 刷新元数据广播(直接改底层字段后调用)。 */
    public void refreshMetadata() {
        EntityManager.broadcastMetadata(h);
    }

    public static Entity wrap(com.CharunCore.server.world.entity.Entity e) {
        return Entities.wrap(e);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Entity e && e.h == this.h;
    }

    @Override
    public int hashCode() { return h.id; }
}
