package com.CharunCore.server.plugin.api;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.WorldManager;
import com.CharunCore.server.world.entity.Entity;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.ItemEntity;

/** 世界操作门面: 方块读写/掉落物/实体查询, 按维度隔离。 */
public final class World {

    private final DimensionType dim;

    private World(DimensionType dim) {
        this.dim = dim;
    }

    public static World of(DimensionType dim) {
        return new World(dim);
    }

    public static World overworld() {
        return new World(DimensionType.OVERWORLD);
    }

    public DimensionType getDimension() {
        return dim;
    }

    public String getBlockName(int x, int y, int z) {
        return BlockStateHelper.getName(
                WorldManager.getBlockState(dim, x, y, z));
    }

    public int getBlockState(int x, int y, int z) {
        return WorldManager.getBlockState(dim, x, y, z);
    }

    public void setBlock(int x, int y, int z, String blockName, String... props) {
        int state = BlockStateHelper.getDefault(blockName);
        for (int i = 0; i + 1 < props.length; i += 2) {
            state = BlockStateHelper.withProp(state, props[i], props[i + 1]);
        }
        WorldManager.setBlock(dim, x, y, z, state);
        NetworkHandler.broadcastBlockChange(dim, x, y, z, state);
    }

    public void dropItem(int x, int y, int z, String itemName, int count) {
        int id = BlockManager.getItemIdByName(itemName);
        if (id <= 0) return;
        ItemEntity item = new ItemEntity(EntityManager.allocateId(), x + 0.5, y + 0.5, z + 0.5, id, count);
        item.dim = dim;
        EntityManager.addEntity(item);
    }

    public java.util.List<Entity> getEntitiesInRange(int x, int y, int z, double radius) {
        java.util.List<Entity> result = new java.util.ArrayList<>();
        double r2 = radius * radius;
        for (Entity e : EntityManager.getAllEntities()) {
            if (e.dim != dim) continue;
            double dx = e.x - x, dy = e.y - y, dz = e.z - z;
            if (dx * dx + dy * dy + dz * dz <= r2) result.add(e);
        }
        return result;
    }
}
