package com.CharunCore.server.plugin.api;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.ItemEntity;
import com.CharunCore.server.world.entity.MobEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体生成/查询门面(对标 Paper World.spawnEntity):
 * spawn 按原版实体名创建(zombie/creeper/skeleton/pig/cow/item/...), 自动广播生成包。
 */
public final class Entities {

    private Entities() {}

    /** 按名称在指定维度生成实体, 返回插件门面。未知类型返回 null。 */
    public static Entity spawn(String typeName, DimensionType dim, double x, double y, double z) {
        MobEntity mob = new MobEntity(EntityManager.allocateId(), typeName.toLowerCase(), x, y, z);
        mob.dim = dim;
        EntityManager.addEntity(mob);
        return new Mob(mob);
    }

    /** 在主世界生成实体。 */
    public static Entity spawn(String typeName, double x, double y, double z) {
        return spawn(typeName, DimensionType.OVERWORLD, x, y, z);
    }

    /** 按名称生成掉落物实体。 */
    public static Entity dropItem(String itemName, int count, DimensionType dim, double x, double y, double z) {
        int id = BlockManager.getItemIdByName(itemName);
        if (id <= 0) return null;
        ItemEntity it = new ItemEntity(EntityManager.allocateId(), x, y, z, id, count);
        it.dim = dim;
        EntityManager.addEntity(it);
        return new Entity(it);
    }

    /** 按 eid 查找实体。 */
    public static Entity get(int eid) {
        return wrap(EntityManager.getEntity(eid));
    }

    /** 指定维度全部实体(dim 为 null 时返回所有维度)。 */
    public static List<Entity> getAll(DimensionType dim) {
        List<Entity> out = new ArrayList<>();
        for (com.CharunCore.server.world.entity.Entity e : EntityManager.getAllEntities()) {
            if (dim == null || e.dim == dim) out.add(wrap(e));
        }
        return out;
    }

    /** 范围内实体查询。 */
    public static List<Entity> getInRange(DimensionType dim, double x, double y, double z, double radius) {
        List<Entity> out = new ArrayList<>();
        double r2 = radius * radius;
        for (com.CharunCore.server.world.entity.Entity e : EntityManager.getAllEntities()) {
            if (e.dim != dim) continue;
            double dx = e.x - x, dy = e.y - y, dz = e.z - z;
            if (dx * dx + dy * dy + dz * dz <= r2) out.add(wrap(e));
        }
        return out;
    }

    static Entity wrap(com.CharunCore.server.world.entity.Entity e) {
        if (e == null) return null;
        return e instanceof MobEntity ? new Mob(e) : new Entity(e);
    }
}
