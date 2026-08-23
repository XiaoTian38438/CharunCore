package com.CharunCore.server.world;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.MobEntity;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 刷怪笼（mob_spawner）激活：玩家靠近时按间隔在其周围生成对应生物。
 * 注册由 WorldManager.setBlock 在放置 spawner 方块时调用；tick 周期性清理已移除的笼子。
 *
 * P4-9: 读取刷怪笼 NBT（SpawnData / SpawnPotentials / Min|MaxSpawnDelay / SpawnCount 等）
 * 决定种群与生成参数，而非固定随机池。
 */
public final class SpawnerSystem {

    private static final Random RND = new Random();
    private static final Map<String, SpawnerData> SPAWNERS = new ConcurrentHashMap<>();

    private static final String[] DEFAULT_TYPES = {"zombie", "zombie", "skeleton", "spider"};

    private SpawnerSystem() {}

    private static final class SpawnerData {
        String[] types;
        int delay;
        int minDelay;
        int maxDelay;
        int spawnCount;

        SpawnerData(String[] types, int minDelay, int maxDelay, int spawnCount) {
            this.types = types;
            this.minDelay = minDelay;
            this.maxDelay = Math.max(minDelay, maxDelay);
            this.spawnCount = Math.max(1, spawnCount);
            this.delay = minDelay + RND.nextInt(Math.max(1, this.maxDelay - minDelay + 1));
        }
    }

    private static String key(DimensionType dim, int x, int y, int z) {
        return dim.name() + ":" + x + "," + y + "," + z;
    }

    public static void register(DimensionType dim, int x, int y, int z) {
        String k = key(dim, x, y, z);
        // 原版: 必须每次重新读取方块实体 NBT。空笼子无 SpawnData -> types=null(不刷);
        // 右键刷怪蛋写入 SpawnData 后再次 register 需覆盖旧的随机 types, 否则类型不刷新(#14)。
        SPAWNERS.put(k, readOrCreate(dim, x, y, z));
    }

    /** 读取刷怪笼方块 NBT 配置；无 NBT 或解析失败则用默认种群。 */
    private static SpawnerData readOrCreate(DimensionType dim, int x, int y, int z) {
        String[] types = null;
        int minDelay = 20 + RND.nextInt(40);
        int maxDelay = 200 + RND.nextInt(400);
        int spawnCount = 4;
        try {
            var chunk = WorldManager.getChunk(dim, x >> 4, z >> 4);
            if (chunk != null) {
                NbtMap be = chunk.getBlockEntity(x & 15, y, z & 15);
                if (be != null) {
                    if (be.containsKey("MinSpawnDelay")) minDelay = be.getInt("MinSpawnDelay", minDelay);
                    if (be.containsKey("MaxSpawnDelay")) maxDelay = be.getInt("MaxSpawnDelay", maxDelay);
                    if (be.containsKey("SpawnCount")) spawnCount = be.getInt("SpawnCount", spawnCount);
                    if (be.containsKey("SpawnPotentials")) {
                        List<NbtMap> pots = be.getList("SpawnPotentials", NbtType.COMPOUND);
                        List<String> tl = new ArrayList<>();
                        for (NbtMap m : pots) {
                            NbtMap data = m.getCompound("data");
                            String id = entityIdFromNbt(data.getString("id", ""));
                            int w = m.getInt("weight", 1);
                            for (int i = 0; i < Math.max(1, w); i++) tl.add(id);
                        }
                        if (!tl.isEmpty()) types = tl.toArray(new String[0]);
                    } else if (be.containsKey("SpawnData")) {
                        NbtMap sd = be.getCompound("SpawnData");
                        String id = entityIdFromNbt(sd.getString("id", ""));
                        if (!id.isEmpty()) types = new String[]{id};
                    }
                }
            }
        } catch (Exception e) {
            // 解析异常则回退默认配置
        }
        // 原版: 空刷怪笼(无 SpawnData 也无 SpawnPotentials) 绝不刷怪,
        // 必须玩家右键刷怪蛋设置 SpawnData 后才刷。故 types 保持 null, tick 中跳过。
        // 不再回退随机类型(否则空笼子也会刷蜘蛛, 违反 #14)。
        return new SpawnerData(types, minDelay, maxDelay, spawnCount);
    }

    private static String entityIdFromNbt(String id) {
        if (id == null) return "";
        return id.startsWith("minecraft:") ? id.substring(10) : id;
    }

    /**
     * 区块加载/生成完成后，扫描其方块实体图，为所有刷怪笼（含世界生成期放置的，
     * 如地牢/矿坑/下界要塞/要塞银鱼笼）注册刷怪数据。
     * 关键修复：原本 SpawnerSystem.register 仅由运行时 WorldManager.setBlock 调用，
     * 世界生成经 WorldGenLevel.setBlock 放置的笼子永不注册 → 从不刷怪。现于
     * WorldManager.getChunk 加载/生成后统一扫描注册，重启后从磁盘 NBT 重新注册，持久有效。
     */
    public static void registerChunk(DimensionType dim, Chunk chunk, int chunkX, int chunkZ) {
        if (chunk == null) return;
        for (Map.Entry<Long, NbtMap> e : chunk.getBlockEntityMap().entrySet()) {
            long k = e.getKey();
            int rx = (int) (k & 15);
            int rz = (int) ((k >> 4) & 15);
            int y = (int) ((k >> 16) - 64);
            int state = chunk.getBlock(rx, y, rz);
            if (!"spawner".equals(BlockStateHelper.getName(state))) continue;
            register(dim, chunkX * 16 + rx, y, chunkZ * 16 + rz);
        }
    }

    /** 外部(如 NBT 加载器)可用此方法显式覆盖刷怪笼配置。 */
    public static void configure(DimensionType dim, int x, int y, int z,
                                  String[] types, int minDelay, int maxDelay, int spawnCount) {
        SPAWNERS.put(key(dim, x, y, z),
            new SpawnerData(types != null && types.length > 0 ? types : DEFAULT_TYPES,
                minDelay, maxDelay, spawnCount));
    }

    public static void tick() {
        for (Map.Entry<String, SpawnerData> e : SPAWNERS.entrySet()) {
            SpawnerData d = e.getValue();
            d.delay--;
            if (d.delay > 0) continue;
            d.delay = d.minDelay + RND.nextInt(Math.max(1, d.maxDelay - d.minDelay + 1));

            String[] parts = e.getKey().split(":");
            DimensionType dim = DimensionType.valueOf(parts[0]);
            String[] xyz = parts[1].split(",");
            int sx = Integer.parseInt(xyz[0]);
            int sy = Integer.parseInt(xyz[1]);
            int sz = Integer.parseInt(xyz[2]);

            // 笼子方块已被移除则清理
            if (!"spawner".equals(BlockStateHelper.getName(WorldManager.getBlockState(dim, sx, sy, sz)))) {
                SPAWNERS.remove(e.getKey());
                continue;
            }

            // 是否有玩家在 16 格内
            NetworkHandler near = null;
            for (NetworkHandler p : NetworkHandler.players.values()) {
                if (p.ctx == null || !p.ctx.channel().isActive() || p.isDead) continue;
                if (p.currentDim != dim) continue;
                double dx = p.x - sx, dy = p.y - sy, dz = p.z - sz;
                if (dx * dx + dy * dy + dz * dz <= 16 * 16) { near = p; break; }
            }
            if (near == null) continue;

            // 原版: 仅当已设置 SpawnData(刷怪类型) 才刷怪; 空笼子(types=null)绝不刷。
            if (d.types == null || d.types.length == 0) continue;

            int spawned = 0;
            int attempts = 0;
            while (spawned < d.spawnCount && attempts < 12) {
                attempts++;
                int tx = sx + RND.nextInt(9) - 4;
                int ty = sy + RND.nextInt(3) - 1;
                int tz = sz + RND.nextInt(9) - 4;
                int here = WorldManager.getBlockStateCached(dim, tx, ty, tz);
                int below = WorldManager.getBlockStateCached(dim, tx, ty - 1, tz);
                if (here != 0) continue;                       // 目标须为空
                if (below == 0) continue;                      // 下方须为实心
                String type = d.types[RND.nextInt(d.types.length)];
                MobEntity mob = new MobEntity(EntityManager.allocateId(), type, tx + 0.5, ty, tz + 0.5);
                mob.dim = dim;
                EntityManager.addEntity(mob);
                spawned++;
            }
        }
    }
}
