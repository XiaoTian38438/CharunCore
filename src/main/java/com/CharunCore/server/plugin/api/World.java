package com.CharunCore.server.plugin.api;

import java.util.ArrayList;
import java.util.List;

import com.CharunCore.server.Main;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.ExplosionEngine;
import com.CharunCore.server.world.WorldManager;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.ItemEntity;

/** 世界操作门面(对标 Paper World): 方块读写/掉落物/实体查询生成/雷击/爆炸/时间/天气。 */
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

    public static World nether() {
        return new World(DimensionType.THE_NETHER);
    }

    public static World theEnd() {
        return new World(DimensionType.THE_END);
    }

    public DimensionType getDimension() {
        return dim;
    }

    // ── 方块 ────────────────────────────────────────────────────

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

    /** 按方块状态 id 直接设置(高频批量用, 单点仍会广播)。 */
    public void setBlockState(int x, int y, int z, int stateId) {
        WorldManager.setBlock(dim, x, y, z, stateId);
        NetworkHandler.broadcastBlockChange(dim, x, y, z, stateId);
    }

    /** 该列最高非空气方块 y(无则 minY-1)。 */
    public int getHighestBlockYAt(int x, int z) {
        for (int y = dim.minY + dim.height - 1; y >= dim.minY; y--) {
            int st = WorldManager.getBlockState(dim, x, y, z);
            if (!"air".equals(BlockStateHelper.getName(st)) && st != 0) return y;
        }
        return dim.minY - 1;
    }

    // ── 物品/实体 ───────────────────────────────────────────────

    public void dropItem(int x, int y, int z, String itemName, int count) {
        int id = BlockManager.getItemIdByName(itemName);
        if (id <= 0) return;
        ItemEntity item = new ItemEntity(EntityManager.allocateId(), x + 0.5, y + 0.5, z + 0.5, id, count);
        item.dim = dim;
        EntityManager.addEntity(item);
    }

    public void dropItem(double x, double y, double z, ItemStack item) {
        if (item == null || item.isEmpty()) return;
        ItemEntity it = new ItemEntity(EntityManager.allocateId(), x, y, z, item.itemId(), item.count());
        it.dim = dim;
        EntityManager.addEntity(it);
    }

    /** 生成实体(原版类型名), 返回插件门面; 未知类型返回 null。 */
    public Entity spawnEntity(String typeName, double x, double y, double z) {
        return Entities.spawn(typeName, dim, x, y, z);
    }

    public List<Entity> getEntitiesInRange(int x, int y, int z, double radius) {
        return Entities.getInRange(dim, x, y, z, radius);
    }

    public List<Entity> getEntities() {
        return Entities.getAll(dim);
    }

    public List<NetworkHandler> getPlayers() {
        List<NetworkHandler> out = new ArrayList<>();
        for (NetworkHandler h : NetworkHandler.players.values()) {
            if (h.currentDim == dim && h.ctx != null && h.ctx.channel().isActive()) out.add(h);
        }
        return out;
    }

    // ── 特效 ────────────────────────────────────────────────────

    public void playSound(String sound, double x, double y, double z, float volume, float pitch) {
        NetworkHandler.broadcastSoundAt(dim, x, y, z, sound, volume, pitch);
    }

    /** 召雷(生成闪电 + 3 格内伤害点燃)。 */
    public void strikeLightning(double x, double y, double z) {
        NetworkHandler.strikeLightning(dim, x, y, z);
    }

    /** 爆炸(power=4 约 TNT 当量; destroyBlocks=false 仅击退+伤害)。 */
    public void createExplosion(double x, double y, double z, float power, boolean destroyBlocks) {
        ExplosionEngine.explode(dim, x, y, z, power, destroyBlocks);
    }

    // ── 时间/天气(全服共享, 广播刷新) ────────────────────────────

    /** 设置主世界昼夜时间(0=清晨 6000=正午 13000=入夜)并广播。 */
    public void setTime(long dayTime) {
        Main.dayTime = dayTime;
        NetworkHandler.broadcastTime();
    }

    public long getTime() {
        return Main.dayTime;
    }

    public boolean isRaining() {
        return Main.isRaining;
    }

    // ── 区块 ────────────────────────────────────────────────────

    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        return WorldManager.isChunkCached(dim, ((long) chunkX << 32) ^ (chunkZ & 0xFFFFFFFFL))
                || WorldManager.getChunk(dim, chunkX, chunkZ) != null;
    }

    /** 同步预生成区块(主线程阻塞)。 */
    public void loadChunk(int chunkX, int chunkZ) {
        WorldManager.getChunk(dim, chunkX, chunkZ);
    }
}
