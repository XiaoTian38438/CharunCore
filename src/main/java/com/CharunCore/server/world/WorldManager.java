package com.CharunCore.server.world;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BitStorage;
import com.CharunCore.server.world.light.LightEngine;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.RegistryHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.DensityRouterChunkGenerator;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class WorldManager {
    /**
     * 区块生成器版本戳。写入每个保存到磁盘的区块 NBT。
     * 当加载到的区块版本低于此值时, 视为旧版生成器的产物(地形/特征可能损坏),
     * 直接丢弃并从当前生成器重新生成, 而不是把损坏地形发给客户端。
     * 每次地形/特征逻辑有重大修改时 +1, 旧世界会自动渐进式自愈, 无需手动删 world/。
     */
    public static final int CHUNK_GEN_VERSION = 2;
    private static final int MAX_CACHED_CHUNKS = 4096;
    private static final Map<Long, Chunk> chunkCache = new ConcurrentHashMap<>();
    private static final Map<DimensionType, Map<Long, Chunk>> dimChunks = new ConcurrentHashMap<>();
    private static long WORLD_SEED = 1234567L;
    private static DensityRouterChunkGenerator overworldGenerator;
    private static DensityRouterChunkGenerator netherGenerator;
    private static DensityRouterChunkGenerator endGenerator;
    private static final ScheduledExecutorService ioExecutor = Executors.newScheduledThreadPool(2);
    private static final java.util.concurrent.ConcurrentMap<Long, java.util.concurrent.locks.ReentrantLock> chunkLocks =
        new java.util.concurrent.ConcurrentHashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { ioExecutor.shutdown(); ioExecutor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println("[WorldManager] 优雅关闭: 所有待保存区块已刷盘");
        }, "WorldManager-ShutdownHook"));
    }

    public static ScheduledExecutorService getIoExecutor() { return ioExecutor; }

    public static long getSeed() { return WORLD_SEED; }

    private static double[] spawnPoint = {0.5, 75.0, 0.5};
    private static final java.util.Map<String, String> gameRules = new java.util.concurrent.ConcurrentHashMap<>();

    public static double[] getSpawnPoint() { return spawnPoint; }
    public static void setSpawnPoint(int x, int y, int z) {
        spawnPoint[0] = x + 0.5; spawnPoint[1] = y; spawnPoint[2] = z + 0.5;
        spawnResolved = true;
    }

    private static volatile boolean spawnResolved = false;

    private static final java.util.Set<String> SPAWN_PASSABLE = java.util.Set.of(
            "short_grass", "tall_grass", "fern", "large_fern", "dead_bush", "vine",
            "dandelion", "poppy", "blue_orchid", "allium", "azure_bluet", "oxeye_daisy",
            "cornflower", "lily_of_the_valley", "torchflower", "red_tulip", "orange_tulip",
            "white_tulip", "pink_tulip", "sunflower", "lilac", "rose_bush", "peony",
            "brown_mushroom", "red_mushroom", "sweet_berry_bush", "snow", "sugar_cane",
            "bamboo", "seagrass", "tall_seagrass", "kelp", "kelp_plant", "cactus",
            "wither_rose", "pink_petals", "moss_carpet", "azalea", "flowering_azalea");

    /**
     * 原版风格世界出生点搜索：从 (0,0) 向外按环形扩散，寻找海平面以上、
     * 顶面为固体（非水/岩浆/树叶/原木）且头顶两格为空气的陆地列。
     * 结果缓存，全服只算一次。
     */
    public static synchronized double[] resolveWorldSpawn() {
        if (spawnResolved) return spawnPoint;
        long t0 = System.currentTimeMillis();
        int[] found = null;
        outer:
        for (int r = 0; r <= 384; r += 16) {
            for (int dx = -r; dx <= r; dx += 16) {
                for (int dz = -r; dz <= r; dz += 16) {
                    if (r > 0 && Math.abs(dx) != r && Math.abs(dz) != r) continue;
                    int[] c = probeLandColumn(dx, dz);
                    if (c != null) { found = c; break outer; }
                }
            }
        }
        if (found == null) {
            found = new int[]{0, 80, 0};
            int stone = BlockStateHelper.getDefault("stone");
            for (int ox = -3; ox <= 3; ox++)
                for (int oz = -3; oz <= 3; oz++) {
                    setBlock(DimensionType.OVERWORLD, ox, 79, oz, stone);
                    setBlock(DimensionType.OVERWORLD, ox, 80, oz, 0);
                    setBlock(DimensionType.OVERWORLD, ox, 81, oz, 0);
                }
            System.out.println("[世界] 未找到陆地出生点, 已在 (0,80,0) 构建应急平台");
        }
        spawnPoint[0] = found[0] + 0.5;
        spawnPoint[1] = found[1];
        spawnPoint[2] = found[2] + 0.5;
        spawnResolved = true;
        System.out.println("[世界] 出生点已确定: (" + found[0] + ", " + found[1] + ", " + found[2]
                + ") 耗时 " + (System.currentTimeMillis() - t0) + "ms");
        return spawnPoint;
    }

    /** 探测一列地形；返回 {x, 站立Y, z}，若非合格陆地则返回 null。 */
    private static int[] probeLandColumn(int bx, int bz) {
        DimensionType dim = DimensionType.OVERWORLD;
        int top = dim.minY + dim.height - 2;
        for (int y = top; y > 40; y--) {
            int st = getBlockState(dim, bx, y, bz);
            if (st == 0) continue;
            String n = BlockStateHelper.getName(st);
            if (n == null) return null;
            if (n.startsWith("minecraft:")) n = n.substring(10);
            if (SPAWN_PASSABLE.contains(n)) continue;
            if (n.contains("water") || n.contains("lava") || n.contains("ice")) return null;
            if (n.contains("leaves") || n.contains("_log") || n.equals("bamboo")) return null;
            if (y + 1 < 63) return null;
            if (getBlockState(dim, bx, y + 1, bz) != 0) return null;
            if (getBlockState(dim, bx, y + 2, bz) != 0) return null;
            return new int[]{bx, y + 1, bz};
        }
        return null;
    }
    public static String getGameRule(String name) { return gameRules.get(name); }
    public static void setGameRule(String name, String value) { gameRules.put(name, value); }

    static {
        WorldInitializer.initWorld("world");
        WORLD_SEED = readSeedFromLevelDat("world/level.dat");
        overworldGenerator = new DensityRouterChunkGenerator(WORLD_SEED, DimensionType.OVERWORLD);
        netherGenerator = new DensityRouterChunkGenerator(WORLD_SEED, DimensionType.THE_NETHER);
        endGenerator = new DensityRouterChunkGenerator(WORLD_SEED, DimensionType.THE_END);
        System.out.println("[世界] 种子: " + WORLD_SEED);
    }

    public static DensityRouterChunkGenerator getGenerator(DimensionType dim) {
        return switch (dim) {
            case THE_NETHER -> netherGenerator;
            case THE_END -> endGenerator;
            default -> overworldGenerator;
        };
    }

    public static DensityRouterChunkGenerator getOverworldGenerator() {
        return overworldGenerator;
    }

    private static long readSeedFromLevelDat(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) return 1234567L;
            try (FileInputStream fis = new FileInputStream(f)) {
                byte[] first3 = new byte[3];
                fis.read(first3);
                InputStream input;
                if (first3[0] == 0x0A && first3[1] == 0x00 && first3[2] == 0x00) {
                    input = fis;
                } else {
                    byte[] rest = new byte[(int)(f.length() - 3)];
                    fis.read(rest);
                    byte[] all = new byte[(int)f.length()];
                    System.arraycopy(first3, 0, all, 0, 3);
                    System.arraycopy(rest, 0, all, 3, rest.length);
                    input = new ByteArrayInputStream(all);
                }
                java.util.zip.GZIPInputStream gzis = new java.util.zip.GZIPInputStream(input);
                org.cloudburstmc.nbt.NBTInputStream nbtIn = new org.cloudburstmc.nbt.NBTInputStream(new DataInputStream(gzis));
                NbtMap root = (NbtMap) nbtIn.readTag();
                nbtIn.close();
                NbtMap data = root.getCompound("Data");
                if (data.containsKey("RandomSeed")) {
                    return data.getLong("RandomSeed");
                }
                if (data.containsKey("WorldGenSettings")) {
                    NbtMap wgs = data.getCompound("WorldGenSettings");
                    if (wgs.containsKey("seed")) {
                        return wgs.getLong("seed");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[世界] 读取种子失败: " + e.getMessage());
        }
        return 1234567L;
    }

    /**
     * 根据调色盘大小选择正确的 BPE（Bits Per Entry）。
     * 1.21+ 协议规则：
     *   - 间接模式 BPE 只能取 1/2/3/4（对应 ≤2/≤4/≤8/≤16 个条目）
     *   - 超过则使用 Direct (BPE=15)
     */
    public static int selectPaletteBpe(int paletteSize) {
        if (paletteSize <= 2) return 1;
        if (paletteSize <= 4) return 2;
        if (paletteSize <= 8) return 3;
        if (paletteSize <= 16) return 4;
        return 15; // Direct
    }

    public static Chunk convertNbtToChunk(int x, int z, NbtMap nbt) {
        return convertNbtToChunk(x, z, nbt, DimensionType.OVERWORLD);
    }

    public static Chunk convertNbtToChunk(int x, int z, NbtMap nbt,
                                          DimensionType dim) {
        Chunk chunk = new Chunk(x, z, dim.minY, dim.height >> 4);

        // 旧版生成器的区块(无版本戳或版本过低) → 返回 null, 由调用方重新生成,
        // 避免把损坏/过时的地形发给客户端(表现为"远处方块乱套")。
        // 放在最前, 连"全空"的旧区块也一并重新生成。
        if (nbt.getInt("CharunCore_gen_version", -1) < CHUNK_GEN_VERSION) return null;

        if (!nbt.containsKey("sections")) return chunk;

        List<NbtMap> sections = nbt.getList("sections", NbtType.COMPOUND);

        for (NbtMap sectionNbt : sections) {
            int sectionY = sectionNbt.getByte("Y");
            int internalIdx = sectionY - (dim.minY >> 4);

            if (internalIdx < 0 || internalIdx >= 24) continue;

            if (sectionNbt.containsKey("block_states")) {
                NbtMap states = sectionNbt.getCompound("block_states");
                List<NbtMap> palette = states.getList("palette", NbtType.COMPOUND);

                if (palette.size() == 1) {
                    String name = palette.get(0).getString("Name").replace("minecraft:", "");
                    int stateId = BlockStateHelper.getDefault(name);
                    if (stateId != 0) {
                        for (int rx = 0; rx < 16; rx++)
                            for (int ry = 0; ry < 16; ry++)
                                for (int rz = 0; rz < 16; rz++)
                                    chunk.setBlock(rx, (sectionY << 4) + ry, rz, stateId);
                    }
                } else if (states.containsKey("data")) {
                    long[] data = states.getLongArray("data");
                    if (data == null || data.length == 0) {
                        String name = palette.get(0).getString("Name").replace("minecraft:", "");
                        int defaultState = BlockStateHelper.getDefault(name);
                        for (int rx = 0; rx < 16; rx++)
                            for (int ry = 0; ry < 16; ry++)
                                for (int rz = 0; rz < 16; rz++)
                                    chunk.setBlock(rx, (sectionY << 4) + ry, rz, defaultState);
                    } else {
                        int[] paletteStates = new int[palette.size()];
                        for (int i = 0; i < palette.size(); i++) {
                            String name = palette.get(i).getString("Name").replace("minecraft:", "");
                            if (palette.get(i).containsKey("Properties")) {
                                java.util.Map<String, String> props = new java.util.HashMap<>();
                                NbtMap propsNbt = palette.get(i).getCompound("Properties");
                                for (String key : propsNbt.keySet()) {
                                    props.put(key, String.valueOf(propsNbt.get(key)));
                                }
                                paletteStates[i] = BlockStateHelper.getState(name, props);
                                if (paletteStates[i] <= 0) {
                                    paletteStates[i] = BlockStateHelper.getDefault(name);
                                }
                            } else {
                                paletteStates[i] = BlockStateHelper.getDefault(name);
                            }
                        }

                        int bpe = RegistryHelper.computeBpeForRead(palette.size());
                        if (bpe == 0) bpe = 4;
                        if (bpe < 1) bpe = 1;
                        int[] indices = BitStorage.unpack(bpe, data, 4096);

                        if (indices.length > 0) {
                            for (int i = 0; i < 4096; i++) {
                                int idx = indices[i];
                                if (idx >= 0 && idx < paletteStates.length) {
                                    // i = ry * 256 + rz * 16 + rx
                                    int rx = i & 15, ry = (i >> 8) & 15, rz = (i >> 4) & 15;
                                    chunk.setBlock(rx, (sectionY << 4) + ry, rz, paletteStates[idx]);
                                }
                            }
                        }
                    }
                }
                // #52 修复: 读取 section 的 biomes(重载区块曾丢生物群系 -> 草方块颜色全变平原)。
                if (sectionNbt.containsKey("biomes")) {
                    org.cloudburstmc.nbt.NbtMap biomeNbt = sectionNbt.getCompound("biomes");
                    java.util.List<String> biomePalette = biomeNbt.containsKey("palette")
                        ? biomeNbt.getList("palette", org.cloudburstmc.nbt.NbtType.STRING) : null;
                    if (biomePalette != null && !biomePalette.isEmpty()) {
                        int[] biomeIds = new int[64];
                        for (int p = 0; p < biomePalette.size(); p++) {
                            biomeIds[p] = RegistryHelper.biomeNameToId(biomePalette.get(p));
                        }
                        if (biomeNbt.containsKey("data")) {
                            long[] biomeData = biomeNbt.getLongArray("data");
                            int biomeBpe = RegistryHelper.computeBpeForRead(biomePalette.size());
                            if (biomeBpe == 0) biomeBpe = 2;
                            if (biomeBpe < 1) biomeBpe = 1;
                            int[] bioIdx = BitStorage.unpack(biomeBpe, biomeData, 64);
                            for (int j = 0; j < 64 && j < bioIdx.length; j++) {
                                int bid = bioIdx[j];
                                if (bid >= 0 && bid < biomePalette.size() && bid < biomeIds.length) {
                                    int qx = j & 3, qz = (j >> 2) & 3, qy = (j >> 4) & 3;
                                    chunk.setBiome(qx * 4, (sectionY << 4) + qy * 4, qz * 4, biomeIds[bid]);
                                }
                            }
                        } else {
                            // 单值 palette: 全 section 同一 biome
                            for (int qy = 0; qy < 4; qy++)
                                for (int qz = 0; qz < 4; qz++)
                                    for (int qx = 0; qx < 4; qx++)
                                        chunk.setBiome(qx * 4, (sectionY << 4) + qy * 4, qz * 4, biomeIds[0]);
                        }
                    }
                }
            }
        }

        if (nbt.containsKey("block_entities")) {
            for (org.cloudburstmc.nbt.NbtMap be : nbt.getList("block_entities", NbtType.COMPOUND)) {
                int rx = be.getInt("x", 0) & 15;
                int ry = be.getInt("y", 0);
                int rz = be.getInt("z", 0) & 15;
                chunk.putBlockEntityRaw(rx, ry, rz, be);
                // Bug37 修复: 熔炉/酿造台/漏斗等带自动逻辑的容器必须在区块加载时
                // 注册进 ContainerStore, 否则没人打开过 UI 就永远不会 tick
                // ("物品放进去好久了才开始烧, 打开过一次就正常")。
                String beId = be.getString("id", "");
                if (beId.startsWith("minecraft:")) beId = beId.substring(10);
                ContainerStore.Pos cpos = new ContainerStore.Pos(dim,
                        (x << 4) + rx, ry, (z << 4) + rz);
                switch (beId) {
                    case "furnace", "blast_furnace", "smoker" -> ContainerStore.furnace(cpos, beId);
                    case "brewing_stand" -> ContainerStore.brewing(cpos);
                    case "hopper" -> ContainerStore.hopper(cpos);
                    default -> { }
                }
            }
        }
        return chunk;
    }

    public static boolean isChunkCached(long key) {
        Map<Long, Chunk> store = dimChunks.get(DimensionType.OVERWORLD);
        return store != null && store.containsKey(key);
    }

    public static boolean isFeaturesDone(long key) {
        return overworldGenerator.isFeaturesDone(key);
    }

    public static void generateFeatures(int x, int z) {
        overworldGenerator.generate(x, z);
    }

    public static Chunk getChunk(int x, int z) {
        // 【红石/流体修复】无维度重载统一委托到主世界维度存储，避免与维度感知版
        // (dimChunks.get(OVERWORLD)) 形成两张割裂的 chunk 表，否则 RedstoneEngine /
        // FluidEngine 读到的永远是空表(air)，导致红石"整个失效"。
        return getChunk(DimensionType.OVERWORLD, x, z);
    }

    public static void preGenerateChunk(int x, int z) {
        preGenerateChunk(DimensionType.OVERWORLD, x, z);
    }

    public static void preGenerateChunk(DimensionType dim, int x, int z) {
        Map<Long, Chunk> store = storeFor(dim);
        long key = ((long) x << 32) | (z & 0xFFFFFFFFL);
        if (store.containsKey(key)) return;
        java.util.concurrent.locks.ReentrantLock lock = chunkLocks.computeIfAbsent(key,
            k -> new java.util.concurrent.locks.ReentrantLock());
        lock.lock();
        try {
            if (store.containsKey(key)) return;
            org.cloudburstmc.nbt.NbtMap mcaNbt = AnvilManager.loadChunkNbt(new java.io.File(dim.getRegionDir()), x, z);
            if (mcaNbt != null) {
                Chunk loaded = convertNbtToChunk(x, z, mcaNbt, dim);
                if (loaded != null) { loaded.dim = dim; store.put(key, loaded); 
                    RedstoneEngine.onChunkLoaded(dim, x, z); // #34 加载后重评估红石
                    return; 
                }
            }
            Chunk newChunk = getGenerator(dim).generateBaseOnly(x, z);
            newChunk.dim = dim;
            store.put(key, newChunk);
            ioExecutor.execute(() -> saveChunkToDisk(newChunk, dim));
        } finally {
            lock.unlock();
            chunkLocks.remove(key);
        }
    }

    private static final java.util.Set<Long> dirtyChunks = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static final java.util.Map<DimensionType, java.util.Set<Long>> dirtyDimChunks =
        new java.util.concurrent.ConcurrentHashMap<>();

    /** Bug7/9: BE 写入(告示牌文字/命令方块/容器落盘)也要标脏区块, 否则只在 30s 自动存档前
     *  恰好被 setBlock 标脏过的区块能落盘, 纯 BE 修改重启后全部丢失。 */
    public static void markChunkDirty(Chunk chunk) {
        for (var dimEntry : dimChunks.entrySet()) {
            for (var ce : dimEntry.getValue().entrySet()) {
                if (ce.getValue() == chunk) {
                    dirtyDimChunks.computeIfAbsent(dimEntry.getKey(),
                        d -> java.util.concurrent.ConcurrentHashMap.newKeySet()).add(ce.getKey());
                    return;
                }
            }
        }
    }

    public static void setBlock(int x, int y, int z, int stateId) {
        int chunkX = x >> 4, chunkZ = z >> 4;
        DimensionType dim = DimensionType.OVERWORLD;
        Chunk chunk = getChunk(dim, chunkX, chunkZ);
        int oldState = chunk.getBlock(x & 15, y, z & 15);
        chunk.setBlock(x & 15, y, z & 15, stateId);
        long key = ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
        dirtyDimChunks.computeIfAbsent(dim,
            d -> java.util.concurrent.ConcurrentHashMap.newKeySet()).add(key);
        // B1: 方块变化后, 让相邻水/岩浆重新蔓延 + 红石通知 (按维度调度)
        FluidEngine.neighborChanged(dim, x, y, z);
        // #49: 放置方块覆盖流体时, 清除该格流体并让周围流动流体重新评估。
        String oldName2 = BlockStateHelper.getName(oldState);
        if ("water".equals(oldName2) || "lava".equals(oldName2)) {
            FluidEngine.onBlockPlacedIntoFluid(dim, x, y, z);
        }
        RedstoneEngine.onBlockChanged(dim, x, y, z);
        LightEngine.onBlockChanged(dim, x, y, z, oldState, stateId);
    }

    static {
        ioExecutor.scheduleAtFixedRate(() -> {
            // ---- 保存脏区块到磁盘 ----
            if (!dirtyChunks.isEmpty()) {
                java.util.Set<Long> toSave = new java.util.HashSet<>(dirtyChunks);
                dirtyChunks.clear();
                for (Long key : toSave) {
                    Chunk chunk = chunkCache.get(key);
                    if (chunk != null) {
                        try { saveChunkToDisk(chunk); } catch (Exception e) {
                            System.err.println("[Anvil] 保存失败: " + e.getMessage());
                        }
                    }
                }
            }
            if (!dirtyDimChunks.isEmpty()) {
                for (var dimEntry : dirtyDimChunks.entrySet()) {
                    DimensionType d = dimEntry.getKey();
                    java.util.Set<Long> set = dimEntry.getValue();
                    java.util.Set<Long> toSave = new java.util.HashSet<>(set);
                    set.clear();
                    Map<Long, Chunk> store = storeFor(d);
                    for (Long key : toSave) {
                        Chunk chunk = store.get(key);
                        if (chunk != null) {
                            try { saveChunkToDisk(chunk, d); } catch (Exception e) {
                                System.err.println("[Anvil] 维度保存失败: " + e.getMessage());
                            }
                        }
                    }
                }
            }

            // ---- 卸载远离所有玩家的区块（防止 OOM）----
            unloadDistantChunks();
        }, 30, 30, java.util.concurrent.TimeUnit.SECONDS);
    }

    /**
     * 区块卸载：把不在任何玩家视野范围内的区块从内存中移除。
     * 先保存脏数据，再从 WorldManager.dimChunks 和生成器缓存中同时清除。
     * 每个维度的区块独立判断；无在线玩家时保留最近使用的 256 个区块。
     */
    private static void unloadDistantChunks() {
        // 收集所有在线玩家的区块坐标（按维度分组）
        java.util.Map<DimensionType, java.util.Set<long[]>> playerCenters = new java.util.HashMap<>();
        for (var handler : NetworkHandler.players.values()) {
            if (handler.ctx == null || !handler.ctx.channel().isActive()) continue;
            int pcx = ((int) handler.x) >> 4;
            int pcz = ((int) handler.z) >> 4;
            playerCenters.computeIfAbsent(handler.currentDim, d -> new java.util.HashSet<>())
                       .add(new long[]{pcx, pcz});
        }

        // 视野距离 + 缓冲区（多留几格避免玩家来回走时反复加载/卸载）
        final int UNLOAD_RADIUS = 16; // VIEW_DISTANCE(12) + 4 buffer

        for (var dimEntry : dimChunks.entrySet()) {
            DimensionType dim = dimEntry.getKey();
            Map<Long, Chunk> store = dimEntry.getValue();
            if (store.isEmpty()) continue;

            java.util.Set<long[]> centers = playerCenters.get(dim);
            boolean hasPlayers = (centers != null && !centers.isEmpty());

            java.util.List<Long> toUnload = new java.util.ArrayList<>();
            for (Long key : store.keySet()) {
                int cx = (int)(key >> 32);
                int cz = (int)(long)(key & 0xFFFFFFFFL);

                boolean inRange = false;
                if (hasPlayers) {
                    for (long[] c : centers) {
                        if (Math.abs(cx - (int)c[0]) <= UNLOAD_RADIUS &&
                            Math.abs(cz - (int)c[1]) <= UNLOAD_RADIUS) {
                            inRange = true;
                            break;
                        }
                    }
                }
                if (!inRange) toUnload.add(key);
            }

            if (toUnload.isEmpty()) continue;

            // 先保存即将卸载的脏区块
            java.util.Set<Long> dirtyInDim = dirtyDimChunks.get(dim);
            if (dirtyInDim != null) {
                for (Long key : toUnload) {
                    if (dirtyInDim.remove(key)) {
                        Chunk c = store.get(key);
                        if (c != null) {
                            try { saveChunkToDisk(c, dim); } catch (Exception ignored) {}
                        }
                    }
                }
            }

            // 从主存储中移除
            for (Long key : toUnload) {
                store.remove(key);
            }

            // 同步清理生成器缓存（防止内存泄漏）
            DensityRouterChunkGenerator gen = getGenerator(dim);
            gen.purgeChunkCache(toUnload);

            // 清理流体引擎的扫描记录
            FluidEngine.purgeScannedChunks(toUnload);

            if (toUnload.size() > 0) {
                System.out.println("[卸载] " + dim + ": 卸载 " + toUnload.size() + " 个区块, 剩余 " + store.size());
            }
        }
    }

    /**
     * 返回世界坐标 (x,y,z) 处的 block state ID。
     * 用于互动系统读取当前方块状态。
     */
    public static int getBlockState(int x, int y, int z) {
        // BUG7: 世界 tick 线程内走缓存只读, 绝不生成新区块
        if (isTickThread()) return getBlockStateCached(DimensionType.OVERWORLD, x, y, z);
        Chunk chunk = getChunk(x >> 4, z >> 4);
        return chunk.getBlock(x & 15, y, z & 15);
    }

    // ===== Dimension-aware overloads (nether / end) =====
    private static Map<Long, Chunk> storeFor(DimensionType dim) {
        return dimChunks.computeIfAbsent(dim, d -> new ConcurrentHashMap<>());
    }

    /**
     * 【BUG7 彻底修复】线程局部标记：世界 tick 线程(WorldTickScheduler)在每 tick 内做任何区块读写
     * 时都不允许触发"同步生成"。一旦标记，getChunk/getBlockState/setBlock 一律退化为缓存只读
     * (未加载区块返回 null/air / 跳过写入)，从而让世界 tick 无论何时都不会因生成新区块而卡死，
     * TPS 与服务器时间不再冻结。区块的加载/生成只由 ioExecutor(handleMove/sendInitialChunks)
     * 与显式的重生/末地/传送逻辑负责（它们运行在其它线程，不会被此开关影响）。
     */
    private static final ThreadLocal<Boolean> TICK_THREAD = ThreadLocal.withInitial(() -> Boolean.FALSE);
    public static void markTickThread() { TICK_THREAD.set(Boolean.TRUE); }
    public static void unmarkTickThread() { TICK_THREAD.set(Boolean.FALSE); }
    private static boolean isTickThread() { return TICK_THREAD.get() == Boolean.TRUE; }

    public static Chunk getChunk(DimensionType dim, int x, int z) {
        if (isTickThread()) return getChunkCached(dim, x, z); // 世界 tick 内绝不生成新区块
        Map<Long, Chunk> store = storeFor(dim);
        long key = ((long) x << 32) | (z & 0xFFFFFFFFL);
        // 快速路径：已在缓存中
        Chunk cached = store.get(key);
        if (cached != null) {
            if (!isTickThread() && !getGenerator(dim).isFeaturesDone(key)) getGenerator(dim).generate(x, z);
            return cached;
        }
        // 慢速路径：按 chunk key 加锁，防止多线程同时生成同一区块（TOCTOU 竞态）
        java.util.concurrent.locks.ReentrantLock lock = chunkLocks.computeIfAbsent(key,
            k -> new java.util.concurrent.locks.ReentrantLock());
        lock.lock();
        try {
            // 双重检查：等待锁期间可能已被其他线程生成
            cached = store.get(key);
            if (cached != null) {
                if (!getGenerator(dim).isFeaturesDone(key)) getGenerator(dim).generate(x, z);
                return cached;
            }
            org.cloudburstmc.nbt.NbtMap mcaNbt =
                AnvilManager.loadChunkNbt(new java.io.File(dim.getRegionDir()), x, z);
            if (mcaNbt != null) {
                Chunk loaded = convertNbtToChunk(x, z, mcaNbt, dim);
                if (loaded != null) {
                    loaded.dim = dim;
                    store.put(key, loaded);
                    SpawnerSystem.registerChunk(dim, loaded, x, z);
                    // #34: 区块加载后重评估红石(灯/线/开关), 修复持久化状态与信号源不一致
                    RedstoneEngine.onChunkLoaded(dim, x, z);
                    return loaded;
                } else {
                    System.out.println("[Chunk] 磁盘区块 (" + x + "," + z + "," + dim.key + ") NBT 反序列化返回 null（版本过期或数据损坏），将重新生成 → 可能与相邻旧区块接缝不一致");
                }
            }
            Chunk newChunk = getGenerator(dim).generate(x, z);
            newChunk.dim = dim;
            store.put(key, newChunk);
            SpawnerSystem.registerChunk(dim, newChunk, x, z);
            ioExecutor.execute(() -> saveChunkToDisk(newChunk, dim));
            return newChunk;
        } finally {
            lock.unlock();
            chunkLocks.remove(key);
        }
    }

    public static void generateFeatures(DimensionType dim, int x, int z) {
        getGenerator(dim).generate(x, z);
    }

    public static boolean isChunkCached(DimensionType dim, long key) {
        Map<Long, Chunk> store = dimChunks.get(dim);
        return store != null && store.containsKey(key);
    }

    public static boolean isFeaturesDone(DimensionType dim, long key) {
        return getGenerator(dim).isFeaturesDone(key);
    }

    /**
     * 【BUG7 修复】缓存只读访问：若区块尚未在内存中，直接返回 null，绝不触发同步生成。
     * 仅用于游戏主循环(tick)路径上的读取（刷怪、生物群系查询、实体/流体/随机刻等），
     * 这样主线程在任何情况下都不会因"生成新区块"而卡死，从而避免 TPS≈0、服务器时间停滞。
     * 区块的生成/发送已由登录初始加载(handleMove)与 ioExecutor 异步预生成负责。
     */
    public static Chunk getChunkCached(DimensionType dim, int x, int z) {
        Map<Long, Chunk> store = dimChunks.get(dim);
        if (store == null) return null;
        long key = ((long) x << 32) | (z & 0xFFFFFFFFL);
        return store.get(key);
    }

    /** 【BUG7 修复】缓存只读方块读取：未加载区块返回 0(air)，不触发同步生成。 */
    public static int getBlockStateCached(DimensionType dim, int x, int y, int z) {
        Chunk chunk = getChunkCached(dim, x >> 4, z >> 4);
        if (chunk == null) return 0;
        return chunk.getBlock(x & 15, y, z & 15);
    }

    public static void setBlock(DimensionType dim, int x, int y, int z, int stateId) {
        int chunkX = x >> 4, chunkZ = z >> 4;
        // BUG7: 世界 tick 线程内绝不对"尚未加载的区块"做写入(否则会触发同步生成并卡死 TPS)。
        // 区块可由 ioExecutor 异步预加载, 写入本就该落到已加载的区块上。
        if (isTickThread() && getChunkCached(dim, chunkX, chunkZ) == null) return;
        Chunk chunk = getChunk(dim, chunkX, chunkZ);
        int oldState = chunk.getBlock(x & 15, y, z & 15);
        chunk.setBlock(x & 15, y, z & 15, stateId);
        long key = ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
        dirtyDimChunks.computeIfAbsent(dim, d -> java.util.concurrent.ConcurrentHashMap.newKeySet()).add(key);
        if ("spawner".equals(BlockStateHelper.getName(stateId))) {
            SpawnerSystem.register(dim, x, y, z);
        }
        // 水/岩浆接触火 -> 熄灭相邻火方块(原版行为, 水桶泼火/水流灭火)。
        String placedName = BlockStateHelper.getName(stateId);
        if ("water".equals(placedName) || "lava".equals(placedName)) {
            int[][] nbs = {{x+1,y,z},{x-1,y,z},{x,y+1,z},{x,y-1,z},{x,y,z+1},{x,y,z-1}};
            for (int[] nb : nbs) {
                int fs = getBlockState(dim, nb[0], nb[1], nb[2]);
                if ("fire".equals(BlockStateHelper.getName(fs))) {
                    chunk = getChunk(dim, nb[0] >> 4, nb[2] >> 4);
                    if (chunk != null) {
                        chunk.setBlock(nb[0] & 15, nb[1], nb[2] & 15, 0);
                        NetworkHandler.broadcastBlockChange(dim, nb[0], nb[1], nb[2], 0);
                    }
                }
            }
        }
        // B1: 方块变化后, 让相邻水/岩浆重新蔓延 + 红石通知 (按维度调度)
        FluidEngine.neighborChanged(dim, x, y, z);
        // #49: 放置方块覆盖流体时, 清除该格流体并让周围流动流体重新评估(无滋养则干涸)。
        String oldName = BlockStateHelper.getName(oldState);
        if ("water".equals(oldName) || "lava".equals(oldName)) {
            FluidEngine.onBlockPlacedIntoFluid(dim, x, y, z);
        }
        RedstoneEngine.onBlockChanged(dim, x, y, z);
        LightEngine.onBlockChanged(dim, x, y, z, oldState, stateId);
        // Bug37: 支撑方块被移除后, 六邻居中失去支撑的非完整方块破碎为掉落物(原版 neighborChanged)。
        if (stateId == 0) {
            SupportEngine.checkNeighbors(dim, x, y, z);
        }
    }

    public static int getBlockState(DimensionType dim, int x, int y, int z) {
        // BUG7: 世界 tick 线程内走缓存只读, 绝不生成新区块
        if (isTickThread()) return getBlockStateCached(dim, x, y, z);
        Chunk chunk = getChunk(dim, x >> 4, z >> 4);
        return chunk.getBlock(x & 15, y, z & 15);
    }

    public static void saveChunkToDisk(Chunk chunk) {
        saveChunkToDisk(chunk, DimensionType.OVERWORLD);
    }

    public static void saveChunkToDisk(Chunk chunk,
                                       DimensionType dim) {
        NbtMap nbt = RegistryHelper.createAnvilNbtFromChunk(chunk);
        org.cloudburstmc.nbt.NbtMapBuilder vb = org.cloudburstmc.nbt.NbtMap.builder();
        vb.putAll(nbt);
        vb.putInt("CharunCore_gen_version", CHUNK_GEN_VERSION);
        AnvilManager.saveChunkNbt(new File(dim.getRegionDir()), chunk.getX(), chunk.getZ(), vb.build());
    }
}