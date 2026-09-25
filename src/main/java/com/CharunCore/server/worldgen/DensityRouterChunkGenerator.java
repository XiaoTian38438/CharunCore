package com.CharunCore.server.worldgen;

import java.util.Arrays;
import java.util.Set;

import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.worldgen.biome.BiomeManager;
import com.CharunCore.server.worldgen.feature.*;
import com.CharunCore.server.worldgen.structure.StructureManager;
import com.CharunCore.server.worldgen.structure2.*;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.world.gen.WorldgenRandom;
import com.CharunCore.server.world.gen.XoroshiroRandomSource;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.noisechunk.NoiseChunk;
import com.CharunCore.server.worldgen.noisechunk.NoiseSettings;
import com.CharunCore.server.worldgen.noisechunk.QuartPos;
import com.CharunCore.server.worldgen.noisechunk.aquifer.Aquifer;
import com.CharunCore.server.worldgen.noisechunk.carver.CarvingMask;
import com.CharunCore.server.worldgen.noisechunk.carver.CaveWorldCarver;
import com.CharunCore.server.worldgen.noisechunk.carver.CanyonWorldCarver;
import com.CharunCore.server.worldgen.biome.MultiNoiseBiomeSource;
import com.CharunCore.server.worldgen.biome.NetherBiomeSource;
import com.CharunCore.server.worldgen.biome.Climate;
import com.CharunCore.server.worldgen.feature.SimpleTreeFeature.TreeType;
import com.CharunCore.server.world.gen.NormalNoise;
import com.CharunCore.server.world.gen.NoiseParameters;
import com.CharunCore.server.worldgen.surfacerule.NetherSurfaceRules;
import com.CharunCore.server.worldgen.surfacerule.OverworldSurfaceRules;
import com.CharunCore.server.worldgen.surfacerule.SurfaceRules;
import com.CharunCore.server.worldgen.surfacerule.SurfaceSystem;


/**
 * 基于密度链的原版地形生成器 Stage 0G+。
 * 使用 NoiseChunk 插值层 + Aquifer（含水层）进行 4×4×8 单元格插值。
 * - 缺少 SurfaceRules（SurfaceSystem）：硬编码草地/沙/雪
 */
public final class DensityRouterChunkGenerator {
    final int MIN_Y;
    final int MAX_Y;
    final int SEA_LEVEL;
    final int LAVA_LEVEL;
    final DimensionType dimensionType;

    private final long seedLo, seedHi;
    final NoiseRouter router;
    private final java.util.Map<String, DensityFunction> densityMap;
    private final MultiNoiseBiomeSource biomeSource;
    private final NetherBiomeSource netherBiomeSource;
    /** 下界表面规则树（每生成器构建一次，携带本世界种子的噪声实例）。 */
    private SurfaceRules.RuleSource netherSurfaceRule;
    private final OreGenerator oreGen;
    private final AquaticGenerator aquaticGen;
    private final SurfaceDecorator surfaceDecor;
    private final SpringGenerator springGen;
    private final GlowLichenGenerator glowLichenGen;
    private final MonsterRoomGenerator monsterRoomGen;
    private final AmethystGeodeGenerator geodeGen;
    private final DripstoneGenerator dripstoneGen;
    private final BiomeDecorator biomeDecor;

    private final int stoneId, deepslateId, waterId, lavaId, bedrockId, grassId, dirtId, sandId, snowId;
    private final int gravelId, sandstoneId, packedIceId, coarseDirtId, podzolId, mudId, clayId;
    private final int netherrackId, basaltId, blackstoneId, endStoneId;

    private long caveA, caveB, caveExtraA, caveExtraB, canyonA, canyonB;
    private boolean carverSeedsComputed = false;

    private void computeCarverSeeds() {
        if (carverSeedsComputed) return;
        LegacyRandomSource rng;
        rng = new LegacyRandomSource(seedLo);
        rng.setSeed(seedLo);
        caveA = rng.nextLong(); caveB = rng.nextLong();
        rng = new LegacyRandomSource(seedLo + 1);
        rng.setSeed(seedLo + 1);
        caveExtraA = rng.nextLong(); caveExtraB = rng.nextLong();
        rng = new LegacyRandomSource(seedLo + 2);
        rng.setSeed(seedLo + 2);
        canyonA = rng.nextLong(); canyonB = rng.nextLong();
        carverSeedsComputed = true;
    }

    private final java.util.Map<Long, Chunk> chunkCache = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Set<Long> featuresDone = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private final java.util.Map<Long, java.util.concurrent.locks.ReentrantLock> featureLocks = new java.util.concurrent.ConcurrentHashMap<>();

    public DensityRouterChunkGenerator(long seed) {
        this(seed, DimensionType.OVERWORLD);
    }

    public DensityRouterChunkGenerator(long seed, DimensionType dimType) {
        this.seedLo = seed;
        this.seedHi = 0L;
        this.dimensionType = dimType;
        this.MIN_Y = dimType.minY;
        this.MAX_Y = dimType.minY + dimType.height - 1;
        this.SEA_LEVEL = dimType.seaLevel;
        this.LAVA_LEVEL = dimType == DimensionType.THE_NETHER ? 31 : -54;

        NoiseHolder.setWorldSeed(seedLo, seedHi);
        NoiseHolder.setUseLegacyRandomSource(dimType == DimensionType.THE_NETHER || dimType == DimensionType.THE_END);
        this.densityMap = NoiseRouterData.bootstrap();

        if (dimType == DimensionType.THE_NETHER) {
            this.router = NoiseRouterData.nether(this.densityMap);
        } else if (dimType == DimensionType.THE_END) {
            this.router = NoiseRouterData.end(this.densityMap);
        } else {
            this.router = NoiseRouterData.overworld(this.densityMap, false, false);
        }

        this.stoneId = BlockStateHelper.getDefault("stone");
        this.deepslateId = BlockStateHelper.getDefault("deepslate");
        this.waterId = BlockStateHelper.getDefault("water");
        this.lavaId = BlockStateHelper.getDefault("lava");
        this.bedrockId = BlockStateHelper.getDefault("bedrock");
        this.grassId = BlockStateHelper.getDefault("grass_block");
        this.dirtId = BlockStateHelper.getDefault("dirt");
        this.sandId = BlockStateHelper.getDefault("sand");
        this.snowId = BlockStateHelper.getDefault("snow_block");
        this.gravelId = BlockStateHelper.getDefault("gravel");
        this.sandstoneId = BlockStateHelper.getDefault("sandstone");
        this.packedIceId = BlockStateHelper.getDefault("packed_ice");
        this.coarseDirtId = BlockStateHelper.getDefault("coarse_dirt");
        this.podzolId = BlockStateHelper.getDefault("podzol");
        this.mudId = BlockStateHelper.getDefault("mud");
        this.clayId = BlockStateHelper.getDefault("clay");
        this.netherrackId = BlockStateHelper.getDefault("netherrack");
        this.basaltId = BlockStateHelper.getDefault("basalt");
        this.blackstoneId = BlockStateHelper.getDefault("blackstone");
        this.endStoneId = BlockStateHelper.getDefault("end_stone");

        this.biomeSource = new MultiNoiseBiomeSource();
        this.netherBiomeSource = new NetherBiomeSource();
        // 要塞环带的群系解析（原版 ChunkGeneratorStructureState 用 climate sampler 搜索偏好群系）
        Climate.Sampler ringSampler = biomeSource.createSampler(this.router);
        ConcentricRingsStructurePlacement.setBiomeResolver((qx, qy, qz) ->
            biomeSource.getBiome(qx, qy, qz, ringSampler));
        // Bug44: 要塞环带位置计算(128 环点 × 225x225 群系搜索)首次调用 ~20s,
        // 曾发生在第一个玩家进服的第一个区块生成里 = 进服巨卡。挪到生成器构造时后台预热。
        if (dimensionType == DimensionType.OVERWORLD && seedLo != 0L) {
            final long fSeed = seedLo;
            com.CharunCore.server.world.WorldManager.getIoExecutor().execute(() -> {
                try {
                    isStrongholdRingChunk(0, 0); // 触发环带位置缓存计算
                } catch (Throwable t) {
                    System.err.println("[要塞] 环带预热失败: " + t);
                }
            });
        }
        this.oreGen = new OreGenerator();
        this.aquaticGen = new AquaticGenerator();
        this.surfaceDecor = new SurfaceDecorator();
        this.springGen = new SpringGenerator();
        this.glowLichenGen = new GlowLichenGenerator();
        this.monsterRoomGen = new MonsterRoomGenerator();
        this.geodeGen = new AmethystGeodeGenerator();
        this.dripstoneGen = new DripstoneGenerator();
        this.biomeDecor = new BiomeDecorator();
    }

    public int getDefaultBlockId() {
        return switch (dimensionType) {
            case THE_NETHER -> netherrackId;
            case THE_END -> endStoneId;
            default -> stoneId;
        };
    }

    /** 下界表面系统：原版 nether 表面规则 + nether preset 群系管理器 + netherrack 为目标方块。 */
    private SurfaceSystem buildNetherSurfaceSystem(Chunk chunk, int chunkX, int chunkZ,
                                                   int height, int[] topSolidY) {
        var surfFactory = DensityFunction.NoiseHolder.sharedFactory();
        NormalNoise surfDepthNoise = new NormalNoise(surfFactory.fromHashOf("minecraft:surface"), NoiseParameters.get("minecraft:surface"));
        NormalNoise surfSecondaryNoise = new NormalNoise(surfFactory.fromHashOf("minecraft:surface_secondary"), NoiseParameters.get("minecraft:surface_secondary"));
        NormalNoise clayBandsNoise = new NormalNoise(surfFactory.fromHashOf("minecraft:clay_bands_offset"), NoiseParameters.get("minecraft:clay_bands_offset"));
        Climate.Sampler sampler = netherBiomeSource.createSampler(router);
        BiomeManager netherBiomeMgr = new BiomeManager(
            (qx, qy, qz) -> netherBiomeSource.getBiome(qx, qy, qz, sampler),
            BiomeManager.obfuscateSeed(seedLo));
        if (netherSurfaceRule == null) netherSurfaceRule = NetherSurfaceRules.nether();
        return new SurfaceSystem(netherSurfaceRule,
            SEA_LEVEL, MIN_Y, height,
            surfDepthNoise, surfSecondaryNoise, clayBandsNoise,
            chunk, chunkX, chunkZ, netherBiomeMgr,
            (x, z) -> SEA_LEVEL,
            (x, z) -> {
                int solid = topSolidY[(z & 15) * 16 + (x & 15)];
                return solid > MIN_Y ? solid : SEA_LEVEL;
            },
            netherrackId);
    }

    // profiling accumulators (enable for diagnostics)
    private static boolean profiling = false;
    private static boolean blockDiagnostics = false;
    public static void setBlockDiagnostics(boolean on) { blockDiagnostics = on; }
    public NoiseRouter getRouter() { return router; }
    public java.util.Map<String, DensityFunction> getDensityMap() { return densityMap; }

    public static void setProfiling(boolean on) { profiling = on; }
    private static long profNoiseChunk = 0, profLoop = 0, profBiome = 0;
    private static long profSurface = 0, profDeepBed = 0, profCarve = 0, profOre = 0, profFeatures = 0;
    private static int profCount = 0;

    public static void printProfile() {
        if (profCount == 0) return;
        long total = profNoiseChunk + profLoop + profBiome + profSurface + profDeepBed + profCarve + profOre + profFeatures;
        System.out.printf("[Profile] %,d chunks: NoiseChunk=%dμs(%.0f%%) Loop=%dμs(%.0f%%) Biome=%dμs(%.0f%%) Surface=%dμs(%.0f%%) DeepRock=%dμs(%.0f%%) Carve=%dμs(%.0f%%) Ore=%dμs(%.0f%%) Features=%dμs(%.0f%%) Total=%dμs%n",
            profCount,
            profNoiseChunk / profCount / 1000, profNoiseChunk * 100.0 / total,
            profLoop / profCount / 1000, profLoop * 100.0 / total,
            profBiome / profCount / 1000, profBiome * 100.0 / total,
            profSurface / profCount / 1000, profSurface * 100.0 / total,
            profDeepBed / profCount / 1000, profDeepBed * 100.0 / total,
            profCarve / profCount / 1000, profCarve * 100.0 / total,
            profOre / profCount / 1000, profOre * 100.0 / total,
            profFeatures / profCount / 1000, profFeatures * 100.0 / total,
            total / profCount / 1000);
    }

    public boolean isFeaturesDone(long key) {
        return featuresDone.contains(key);
    }

    /** 磁盘加载的区块视为已完成 feature 放置 —— 否则缓存淘汰后重跑 generate() 会
     *  重放 feature(末地水晶一柱双颗/树重复等, Bug57)。 */
    public void markFeaturesDone(long key) {
        featuresDone.add(key);
    }

    /** 当区块从 WorldManager 卸载时，同步清理生成器内部的缓存数据，防止内存泄漏。 */
    public void purgeChunkCache(java.util.Collection<Long> keys) {
        for (Long key : keys) {
            chunkCache.remove(key);
            featuresDone.remove(key);
            featureLocks.remove(key);
        }
    }

    public Chunk generateBaseOnly(int chunkX, int chunkZ) {
        long key = WorldGenLevel.key(chunkX, chunkZ);
        return chunkCache.computeIfAbsent(key, k -> generateBase(chunkX, chunkZ));
    }

    public Chunk generate(int chunkX, int chunkZ) {
        long key = WorldGenLevel.key(chunkX, chunkZ);
        if (featuresDone.contains(key)) {
            return chunkCache.get(key);
        }

        java.util.concurrent.locks.ReentrantLock lock = featureLocks.computeIfAbsent(key, k -> new java.util.concurrent.locks.ReentrantLock());
        lock.lock();
        try {
            if (featuresDone.contains(key)) {
                return chunkCache.get(key);
            }

            Chunk chunk = chunkCache.get(key);
            if (chunk == null) {
                chunk = generateBase(chunkX, chunkZ);
                chunkCache.put(key, chunk);
            }

            java.util.Map<Long, Chunk> window = new java.util.HashMap<>();
            window.put(key, chunk);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) continue;
                    int nx = chunkX + dx;
                    int nz = chunkZ + dz;
                    long nkey = WorldGenLevel.key(nx, nz);
                    Chunk nc = chunkCache.get(nkey);
                    if (nc == null) {
                        nc = generateBase(nx, nz);
                        chunkCache.put(nkey, nc);
                    }
                    window.put(nkey, nc);
                }
            }

            NoiseSettings settings = (dimensionType == DimensionType.THE_NETHER)
                ? NoiseSettings.NETHER
                : (dimensionType == DimensionType.THE_END)
                ? NoiseSettings.END : NoiseSettings.OVERWORLD;
            WorldGenLevel level = new WorldGenLevel(chunkX, chunkZ, window,
                seedLo, SEA_LEVEL, MIN_Y, settings.height());

            int[] topSolidY = computeTopSolidY(chunk);
            int[] colBiome = computeColBiome(chunk, chunkX, chunkZ, topSolidY);

            // 【结构完整性】jigsaw start 在 features 阶段前提前注册：任何 chunk 生成时即注册
            // 周围潜在结构，邻居 generate 时 getStartsIntersecting 立即命中 → 消除"先完成
            // 的邻居永久缺角"时序问题（曾导致村庄/古城缺角）。
            registerStructuresForChunk(chunk, chunkX, chunkZ, topSolidY);
            // Bug51: 预注册 3x3 邻居的 start(他们可能在本区块之后才轮到 generate)。
            // Beardifier 与跨区块结构放置都需要邻居 start 已就位。
            for (int wdx = -1; wdx <= 1; wdx++) {
                for (int wdz = -1; wdz <= 1; wdz++) {
                    if (wdx == 0 && wdz == 0) continue;
                    int nwx = chunkX + wdx, nwz = chunkZ + wdz;
                    Chunk nc = window.get(WorldGenLevel.key(nwx, nwz));
                    if (nc == null) continue;
                    registerStructuresForChunk(nc, nwx, nwz, computeTopSolidY(nc));
                }
            }
            // Bug43: 纯种子 start(下界要塞)远窗口提前注册 —— 要塞分件延伸 ~10 区块,
            // 3x3 预注册覆盖不到, 先于锚点区块生成的区块永久缺失分件(烈焰人刷怪笼房间)。
            registerFarSeedStarts(chunkX, chunkZ);

            if (dimensionType == DimensionType.OVERWORLD) {
                FeatureDecoration.carveOverworld(level, chunkX, chunkZ, seedLo);
            }

            // Bug51: Beardifier —— terrain_adaptation != none 的结构对地形做增密修正,
            // 消除"村庄房屋悬空/嵌地/同一高度"(原版在噪声阶段完成, 此处为等效方块后处理)。
            applyBeardifier(level, chunk, chunkX, chunkZ);

            // Bug46: 散矿在雕刻之后放置(原版顺序: ore 属于 feature 阶段)。
            // 曾在 generateBase 雕刻前放置 -> 洞穴/峡谷把矿周围挖空, 矿残留悬空。
            RandomSource oreRng = new XoroshiroRandomSource(seedLo + chunkX * 341873128712L + chunkZ * 132897987541L);
            if (dimensionType == DimensionType.OVERWORLD) {
                oreGen.generate(chunk, chunkX, chunkZ, oreRng.fork());
            } else if (dimensionType == DimensionType.THE_NETHER) {
                oreGen.generateNether(chunk, chunkX, chunkZ, oreRng.fork());
            }
            // THE_END：末地无散矿特征

            placeFeatures(level, chunk, chunkX, chunkZ, topSolidY, colBiome);

            placeStructures2(level, chunk, chunkX, chunkZ, topSolidY);

            // Bug27: 要塞改走 RegisterStructuresForChunk 注册的 StrongholdPieces 分件系统,
            // 原版 moveBelowSeaLevel 由 ProceduralStructureStart 的分件引擎处理。
            // 曾直接调用 StrongholdPortalRoomGenerator(仅生成传送门房间) -> 要塞只剩一个房间。

            // Bug46 修复: 光照在初始地形生成阶段就计算了(isLightComputed=true),
            // 但火把/荧石/熔岩等光源是之后才由 placeFeatures/placeStructures2 放入的,
            // 光照数据永远不含它们 -> "自然光源完全不亮"。清除光照标记使其在
            // 区块包发送前(ChunkEncoder.writeLightData -> chunk.ensureLight)重算。
            chunk.clearLight();

            featuresDone.add(key);
            return chunk;
        } catch (Throwable t) {
            // 临时诊断：用户报告 JsonPrimitive→JsonObject 转换异常（移动生成异常）。
            // 生成完整栈便于定位。若重启后仍出现 JsonPrimitive，按栈顶文件修复。
            System.err.print("[区块生成异常] generate(" + chunkX + "," + chunkZ + "): ");
            t.printStackTrace(System.err);
            // 【修复】base 生成后已放入 chunkCache；若 features 阶段失败, 残缺 chunk 留在
            // 缓存会让后续重试基于残缺 base, 且 saveChunkToDisk 可能在异常前保存残缺区块
            // → 重启后加载残缺区块 = "区块错乱"。异常时移除缓存, 下次从零生成。
            chunkCache.remove(key);
            throw t;
        } finally {
            lock.unlock();
            featureLocks.remove(key);
        }
    }

    /** Bug51: 收集 3x3 邻域内 terrain_adaptation != none 的结构片段并应用 Beardifier。 */
    private void applyBeardifier(WorldGenLevel level, Chunk chunk, int chunkX, int chunkZ) {
        com.CharunCore.server.worldgen.structure2.StructureManager2 mgr =
            com.CharunCore.server.worldgen.structure2.StructureManager2.getInstance();
        java.util.List<com.CharunCore.server.worldgen.structure2.Beardifier.Rigid> rigids = new java.util.ArrayList<>();
        java.util.List<com.CharunCore.server.worldgen.structure2.Beardifier.Junction> junctions = new java.util.ArrayList<>();
        int cMinX = chunkX << 4, cMinZ = chunkZ << 4;
        // Bug52 二轮: start 查询窗口 ±1 → ±8。村庄 piece 包围盒横跨 ~7 区块,
        // 锚点在 3x3 之外的 piece 完全不吃 Beardifier(实测"重测还是悬空"的根因之一)。
        // start 表全局缓存, 查询是纯 Map 读, 扩窗代价可忽略。
        for (int wdx = -8; wdx <= 8; wdx++) {
            for (int wdz = -8; wdz <= 8; wdz++) {
                for (var start : mgr.getStartsForChunk(dimensionType, chunkX + wdx, chunkZ + wdz)) {
                    if (!(start instanceof com.CharunCore.server.worldgen.structure2.StructureStart ss)
                            || !ss.isValid()) continue;
                    com.CharunCore.server.worldgen.structure2.StructureRegistry.ConfiguredStructure cs =
                        com.CharunCore.server.worldgen.structure2.StructureRegistry.get(ss.getStructureId());
                    if (cs == null || "none".equals(cs.terrainAdaptation)) continue;
                    for (com.CharunCore.server.worldgen.structure2.PoolElementStructurePiece p : ss.getPieces()) {
                        com.CharunCore.server.worldgen.structure2.BoundingBox bb = p.getBoundingBox();
                        if (bb == null) continue;
                        if (bb.maxX < cMinX - 12 || bb.minX > cMinX + 27
                                || bb.maxZ < cMinZ - 12 || bb.minZ > cMinZ + 27) continue;
                        rigids.add(new com.CharunCore.server.worldgen.structure2.Beardifier.Rigid(
                            bb, cs.terrainAdaptation, p.getGroundLevelDelta()));
                        for (var j : p.getJunctions()) {
                            junctions.add(new com.CharunCore.server.worldgen.structure2.Beardifier.Junction(
                                j.sourceX(), j.sourceGroundY(), j.sourceZ()));
                        }
                    }
                }
            }
        }
        if (rigids.isEmpty() && junctions.isEmpty()) return;
        com.CharunCore.server.worldgen.structure2.Beardifier.applyToChunk(
            level, chunk, chunkX, chunkZ, rigids, junctions);
    }

    private int[] computeTopSolidY(Chunk chunk) {        int[] topSolidY = new int[256];
        java.util.Arrays.fill(topSolidY, MIN_Y - 1);
        int waterId = this.waterId;
        int lavaId = this.lavaId;
        for (int lz = 0; lz < 16; lz++) {
            for (int lx = 0; lx < 16; lx++) {
                for (int y = MAX_Y; y >= MIN_Y; y--) {
                    int b = chunk.getBlock(lx, y, lz);
                    if (b == 0 || b == waterId || b == lavaId) continue;
                    // Skip non-ground blocks (leaves, logs, plants from neighbor features)
                    // so topSolidY points to actual terrain surface, not floating decorations.
                    String name = BlockStateHelper.getName(b);
                    if (name != null) {
                        String n = name.startsWith("minecraft:") ? name.substring(10) : name;
                        if (n.contains("leaves") || n.contains("_log") || n.contains("_wood")
                            || n.equals("vine") || n.contains("sapling")
                            || n.contains("_grass") && !n.contains("block")
                            || n.contains("flower") || n.contains("fern")) continue;
                    }
                    topSolidY[lz * 16 + lx] = y;
                    break;
                }
            }
        }
        return topSolidY;
    }

    private int[] computeColBiome(Chunk chunk, int chunkX, int chunkZ, int[] topSolidY) {
        // 下界：原版 nether MultiNoise preset（temperature/vegetation 五点最近邻）；
        // 末地：原版 TheEndBiomeSource（主岛 64 区块半径内 + 岛屿高度函数分档）。
        if (dimensionType == DimensionType.THE_NETHER) {
            Climate.Sampler sampler = netherBiomeSource.createSampler(router);
            int[] b = new int[256];
            for (int lx = 0; lx < 16; lx++)
                for (int lz = 0; lz < 16; lz++) {
                    // Bug20: 地表规则用的列群系按该列地表高度采样(曾固定 quart y=8,
                    // 地表群系与实际方块带错位)。
                    int surfY = Math.max(MIN_Y, Math.min(MAX_Y - 1, topSolidY[lz * 16 + lx]));
                    b[lz * 16 + lx] = netherBiomeSource.getBiome(
                        ((chunkX << 4) + lx) >> 2, surfY >> 2, ((chunkZ << 4) + lz) >> 2, sampler);
                }
            return b;
        }
        if (dimensionType == DimensionType.THE_END) {
            int[] b = new int[256];
            java.util.Arrays.fill(b, endBiomeForChunk(chunkX, chunkZ));
            return b;
        }
        int[] colBiome = new int[256];
        int chunkMinX = chunkX * 16;
        int chunkMinZ = chunkZ * 16;
        Climate.Sampler climateSampler = biomeSource.createSampler(router);
        BiomeManager biomeManager =
            new BiomeManager(
                (qx, qy, qz) -> biomeSource.getBiome(qx, qy, qz, climateSampler),
                BiomeManager.obfuscateSeed(seedLo));
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int colIdx = lz * 16 + lx;
                int y = Math.max(topSolidY[colIdx], SEA_LEVEL);
                colBiome[colIdx] = biomeManager.getBiome(chunkMinX + lx, y, chunkMinZ + lz);
            }
        }
        return colBiome;
    }

    public int[] computeColBiomeForTest(Chunk chunk) {
        return computeColBiome(chunk, chunk.getX(), chunk.getZ(), computeTopSolidY(chunk));
    }

    /** 按世界坐标查询该列的覆盖层生物群系 id (供 /locate 做结构合法性校验)。 */
    public int getColumnBiome(int blockX, int blockZ) {
        if (dimensionType == DimensionType.THE_NETHER) {
            return netherBiomeSource.getBiomeAt(router, blockX, SEA_LEVEL, blockZ);
        }
        if (dimensionType == DimensionType.THE_END) {
            return endBiomeForChunk(blockX >> 4, blockZ >> 4);
        }
        Climate.Sampler climateSampler = biomeSource.createSampler(router);
        BiomeManager biomeManager =
            new BiomeManager(
                (qx, qy, qz) -> biomeSource.getBiome(qx, qy, qz, climateSampler),
                BiomeManager.obfuscateSeed(seedLo));
        return biomeManager.getBiome(blockX, SEA_LEVEL, blockZ);
    }

    /**
     * 原版 TheEndBiomeSource.getNoiseBiome 逐值移植：
     * chunk 坐标 <=64 区块半径 -> the_end；其余采样 router.erosion()（即 cache2d(endIslands(0))，
     * 与决定岛屿实体位置的函数同源）按 列中心 (chunkX*2+1)*8 分档：
     *   >0.25 highlands, >=-0.0625 midlands, <-0.21875 small_islands, else barrens。
     */
    private transient java.util.Set<Long> strongholdRingCache;

    /** 原始气候采样（无 BiomeManager 四角模糊），用于特征 biome 过滤与诊断。 */
    public int getRawBiomeAt(int blockX, int blockY, int blockZ) {
        if (dimensionType == DimensionType.THE_NETHER) {
            return netherBiomeSource.getBiomeAt(router, blockX, blockY, blockZ);
        }
        if (dimensionType == DimensionType.THE_END) {
            return endBiomeForChunk(blockX >> 4, blockZ >> 4);
        }
        Climate.Sampler sampler = biomeSource.createSampler(router);
        return biomeSource.getBiome(blockX >> 2, blockY >> 2, blockZ >> 2, sampler);
    }

    /** 本世界种子下的要塞环带区块（原版 concentric_rings 定位）。 */
    boolean isStrongholdRingChunk(int chunkX, int chunkZ) {
        if (strongholdRingCache == null) {
            StructureSet set = StructureSet.get("strongholds");
            if (set == null || set.getRings() == null) return false;
            strongholdRingCache = new java.util.HashSet<>();
            for (int[] cp : set.getRings().ringPositions(seedLo)) {
                strongholdRingCache.add(((long) cp[0] << 32) | (cp[1] & 0xFFFFFFFFL));
            }
        }
        long key = ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
        return strongholdRingCache.contains(key);
    }

    private int endBiomeForChunk(int chunkX, int chunkZ) {
        if ((long) chunkX * chunkX + (long) chunkZ * chunkZ <= 4096L) return 56;
        double d = router.erosion().compute(new DensityFunction.SinglePointContext(
            (chunkX * 2 + 1) * 8, 64, (chunkZ * 2 + 1) * 8));
        if (d > 0.25) return 17;      // end_highlands
        if (d >= -0.0625) return 18;  // end_midlands
        if (d < -0.21875) return 44;  // small_end_islands
        return 16;                    // end_barrens
    }

    private Chunk generateBaseNoCarvers(int chunkX, int chunkZ) {
        NoiseSettings settings = (dimensionType == DimensionType.THE_NETHER)
            ? NoiseSettings.NETHER
            : (dimensionType == DimensionType.THE_END)
            ? NoiseSettings.END : NoiseSettings.OVERWORLD;
        Chunk chunk = new Chunk(chunkX, chunkZ, MIN_Y, dimensionType.height >> 4);
        int cellCountXZ = 16 / settings.getCellWidth();
        int cellWidth = settings.getCellWidth();
        int cellHeight = settings.getCellHeight();
        int chunkMinX = chunkX * 16;
        int chunkMinZ = chunkZ * 16;

        NoiseChunk noiseChunk = new NoiseChunk(cellCountXZ, router,
                chunkMinX, chunkMinZ, settings,
                dimensionType == DimensionType.THE_NETHER,
                waterId, lavaId,
                SEA_LEVEL, LAVA_LEVEL,
                dimensionType == DimensionType.OVERWORLD, false, false);

        int[] topSolidY = new int[256];
        java.util.Arrays.fill(topSolidY, MIN_Y - 1);

        for (int cellY = noiseChunk.cellCountY - 1; cellY >= 0; cellY--) {
            int cellBlockY = (cellY + noiseChunk.cellNoiseMinY) * cellHeight;
            noiseChunk.updateForY(cellBlockY + cellHeight - 1, 1.0);
            for (int cellX = 0; cellX < cellCountXZ; cellX++) {
                int cellBlockX = chunkMinX + cellX * cellWidth;
                noiseChunk.updateForX(cellBlockX + cellWidth - 1, 1.0);
                for (int cellZ = 0; cellZ < cellCountXZ; cellZ++) {
                    int cellBlockZ = chunkMinZ + cellZ * cellWidth;
                    noiseChunk.updateForZ(cellBlockZ + cellWidth - 1, 1.0);
                    for (int yOff = cellHeight - 1; yOff >= 0; yOff--) {
                        int blockY = cellBlockY + yOff;
                        double yDelta = (double) yOff / cellHeight;
                        noiseChunk.updateForY(blockY, yDelta);
                        for (int xOff = 0; xOff < cellWidth; xOff++) {
                            int blockX = cellBlockX + xOff;
                            double xDelta = (double) xOff / cellWidth;
                            noiseChunk.updateForX(blockX, xDelta);
                            for (int zOff = 0; zOff < cellWidth; zOff++) {
                                int blockZ = cellBlockZ + zOff;
                                double zDelta = (double) zOff / cellWidth;
                                noiseChunk.updateForZ(blockZ, zDelta);
                                int lx = blockX & 15;
                                int lz = blockZ & 15;
                                int colIdx = lz * 16 + lx;
                                Integer state = noiseChunk.getInterpolatedState();
                                if (state == null) {
                                    chunk.setBlock(lx, blockY, lz, getDefaultBlockId());
                                    if (topSolidY[colIdx] < blockY) {
                                        topSolidY[colIdx] = blockY;
                                    }
                                } else if (state != 0) {
                                    chunk.setBlock(lx, blockY, lz, state);
                                    if (topSolidY[colIdx] < blockY) {
                                        topSolidY[colIdx] = blockY;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            noiseChunk.swapSlices();
        }

        if (dimensionType == DimensionType.OVERWORLD) {
            Climate.Sampler climateSampler = biomeSource.createSampler(router);
            BiomeManager biomeManager =
                new BiomeManager(
                    (qx, qy, qz) -> biomeSource.getBiome(qx, qy, qz, climateSampler),
                    BiomeManager.obfuscateSeed(seedLo));
            int qxBase = QuartPos.fromBlock(chunkMinX);
            int qzBase = QuartPos.fromBlock(chunkMinZ);
            for (int sec = 0; sec < 24; sec++) {
                int qyBase = (MIN_Y + sec * 16) >> 2;
                for (int qy = 0; qy < 4; qy++) {
                    for (int qz = 0; qz < 4; qz++) {
                        for (int qx = 0; qx < 4; qx++) {
                            int biome = biomeSource.getBiome(qxBase + qx, qyBase + qy, qzBase + qz, climateSampler);
                            int blockY = (qyBase + qy) << 2;
                            chunk.setBiome(qx * 4, blockY, qz * 4, biome);
                        }
                    }
                }
            }
            var surfFactory = DensityFunction.NoiseHolder.sharedFactory();
            NormalNoise surfDepthNoise = new NormalNoise(surfFactory.fromHashOf("minecraft:surface"), NoiseParameters.get("minecraft:surface"));
            NormalNoise surfSecondaryNoise = new NormalNoise(surfFactory.fromHashOf("minecraft:surface_secondary"), NoiseParameters.get("minecraft:surface_secondary"));
            NormalNoise clayBandsNoise = new NormalNoise(surfFactory.fromHashOf("minecraft:clay_bands_offset"), NoiseParameters.get("minecraft:clay_bands_offset"));
            SurfaceSystem surfSys = new SurfaceSystem(
                OverworldSurfaceRules.overworld(),
                SEA_LEVEL, MIN_Y, settings.height(),
                surfDepthNoise, surfSecondaryNoise, clayBandsNoise,
                chunk, chunkX, chunkZ, biomeManager,
                (x, z) -> noiseChunk.preliminarySurfaceLevel(x, z),
                (x, z) -> {
                    int solid = topSolidY[(z & 15) * 16 + (x & 15)];
                    return solid > MIN_Y ? solid : SEA_LEVEL;
                });
            surfSys.buildSurface(chunk);
        } else {
            fillFixedBiomes(chunk, chunkX, chunkZ, settings.height());
            if (dimensionType == DimensionType.THE_NETHER) {
                buildNetherSurfaceSystem(chunk, chunkX, chunkZ, settings.height(), topSolidY)
                    .buildSurface(chunk);
            }
        }

        return chunk;
    }

    /**
     * 下界/末地群系填充：
     * - 下界：原版 nether preset MultiNoise，逐 quart 列采样（噪声为 2D，纵向不变）。
     * - 末地：原版 TheEndBiomeSource 阈值（见 endBiomeForChunk），原版粒度即按 16×16 列。
     * biome id 取自 dumped_registries/reg_1.bin：
     *   2 basalt_deltas, 7 crimson_forest, 16 end_barrens, 17 end_highlands,
     *   18 end_midlands, 34 nether_wastes, 44 small_end_islands, 49 soul_sand_valley,
     *   56 the_end, 59 warped_forest
     */
    private void fillFixedBiomes(Chunk chunk, int chunkX, int chunkZ, int height) {
        if (dimensionType == DimensionType.THE_NETHER) {
            // Bug20: 原版下界群系是 3D 的(nether preset temperature 带 y 偏移 -7, 群系随高度变化:
            // 玄武岩三角洲成片悬空/诡异森林贴顶)。曾固定在 quart y=8 采样 -> 整列同群系,
            // 下界观感与原版差异明显。改为逐 quart 全高度采样。
            Climate.Sampler sampler = netherBiomeSource.createSampler(router);
            int qxBase = (chunkX << 4) >> 2, qzBase = (chunkZ << 4) >> 2;
            int sections = height >> 4;
            for (int sec = 0; sec < sections; sec++) {
                for (int qy = 0; qy < 4; qy++) {
                    int blockY = MIN_Y + sec * 16 + qy * 4;
                    int qyQ = blockY >> 2;
                    for (int qz = 0; qz < 4; qz++)
                        for (int qx = 0; qx < 4; qx++)
                            chunk.setBiome(qx * 4, blockY, qz * 4,
                                netherBiomeSource.getBiome(qxBase + qx, qyQ, qzBase + qz, sampler));
                }
            }
            return;
        }
        if (dimensionType != DimensionType.THE_END) return;

        int biome = endBiomeForChunk(chunkX, chunkZ);
        int sections = height >> 4;
        for (int sec = 0; sec < sections; sec++) {
            for (int qy = 0; qy < 4; qy++) {
                int blockY = MIN_Y + sec * 16 + qy * 4;
                for (int qz = 0; qz < 4; qz++) {
                    for (int qx = 0; qx < 4; qx++) {
                        chunk.setBiome(qx * 4, blockY, qz * 4, biome);
                    }
                }
            }
        }
    }

    private Chunk generateBase(int chunkX, int chunkZ) {
        NoiseSettings settings = (dimensionType == DimensionType.THE_NETHER)
            ? NoiseSettings.NETHER
            : (dimensionType == DimensionType.THE_END)
            ? NoiseSettings.END : NoiseSettings.OVERWORLD;
        Chunk chunk = new Chunk(chunkX, chunkZ, MIN_Y, dimensionType.height >> 4);
        // Bug57/58: 生成期必须立刻设置维度 —— 曾等 generate() 返回后才由 WorldManager 赋值,
        // 生成期创建的末地水晶等实体 dim 全为默认 OVERWORLD -> 水晶刷进主世界。
        chunk.dim = dimensionType;
        int cellCountXZ = 16 / settings.getCellWidth();
        int cellWidth = settings.getCellWidth();
        int cellHeight = settings.getCellHeight();

        int chunkMinX = chunkX * 16;
        int chunkMinZ = chunkZ * 16;

        long t0 = profiling ? System.nanoTime() : 0;

        NoiseChunk noiseChunk = new NoiseChunk(cellCountXZ, router,
                chunkMinX, chunkMinZ, settings,
                dimensionType == DimensionType.THE_NETHER,
                waterId, lavaId,
                SEA_LEVEL, LAVA_LEVEL,
                dimensionType == DimensionType.OVERWORLD, false, false);

        int minBlockX = chunkMinX;
        int minBlockZ = chunkMinZ;

        int[] topSolidY = new int[256];
        int[] topBlockY = new int[256];
        double[] topTemp = new double[256];
        double[] topVeg = new double[256];
        int[] colBiome = new int[256];
        Arrays.fill(topSolidY, MIN_Y - 1);
        Arrays.fill(topBlockY, MIN_Y - 1);

        noiseChunk.initializeForFirstCellX();

        long t1 = profiling ? System.nanoTime() : 0;

        for (int cellX = 0; cellX < cellCountXZ; cellX++) {
            noiseChunk.advanceCellX(cellX);

            for (int cellZ = 0; cellZ < cellCountXZ; cellZ++) {
                for (int cellY = noiseChunk.cellCountY - 1; cellY >= 0; cellY--) {
                    noiseChunk.selectCellYZ(cellY, cellZ);

                    int cellBlockY = (cellY + noiseChunk.cellNoiseMinY) * cellHeight;

                    for (int yOff = cellHeight - 1; yOff >= 0; yOff--) {
                        int blockY = cellBlockY + yOff;
                        double yDelta = (double) yOff / cellHeight;
                        noiseChunk.updateForY(blockY, yDelta);

                        for (int xOff = 0; xOff < cellWidth; xOff++) {
                            int blockX = minBlockX + cellX * cellWidth + xOff;
                            double xDelta = (double) xOff / cellWidth;
                            noiseChunk.updateForX(blockX, xDelta);

                            for (int zOff = 0; zOff < cellWidth; zOff++) {
                                int blockZ = minBlockZ + cellZ * cellWidth + zOff;
                                double zDelta = (double) zOff / cellWidth;
                                noiseChunk.updateForZ(blockZ, zDelta);

                                int lx = blockX & 15;
                                int lz = blockZ & 15;
                                int colIdx = lz * 16 + lx;

                                Integer state = noiseChunk.getInterpolatedState();
                                if (state == null) {
                                    // null = solid block → fill with default block (stone)
                                    chunk.setBlock(lx, blockY, lz, getDefaultBlockId());
                                    if (topSolidY[colIdx] < blockY) {
                                        topSolidY[colIdx] = blockY;
                                        topTemp[colIdx] = router.temperature().compute(noiseChunk);
                                        topVeg[colIdx] = router.vegetation().compute(noiseChunk);
                                    }
                                    if (topBlockY[colIdx] < blockY) {
                                        topBlockY[colIdx] = blockY;
                                    }
                                } else if (state != 0) {
                                    chunk.setBlock(lx, blockY, lz, state);
                                    if (topBlockY[colIdx] < blockY) {
                                        topBlockY[colIdx] = blockY;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            noiseChunk.swapSlices();
        }

        noiseChunk.stopInterpolation();

        long t2 = profiling ? System.nanoTime() : 0;

        Climate.Sampler climateSampler = biomeSource.createSampler(router);
        BiomeManager biomeManager =
            new BiomeManager(
                (qx, qy, qz) -> biomeSource.getBiome(qx, qy, qz, climateSampler),
                BiomeManager.obfuscateSeed(seedLo));

        int qxBase = QuartPos.fromBlock(chunkMinX);
        int qzBase = QuartPos.fromBlock(chunkMinZ);
        if (dimensionType == DimensionType.OVERWORLD) {
            for (int sec = 0; sec < 24; sec++) {
                int qyBase = (MIN_Y + sec * 16) >> 2;
                for (int qy = 0; qy < 4; qy++) {
                    for (int qz = 0; qz < 4; qz++) {
                        for (int qx = 0; qx < 4; qx++) {
                            int biome = biomeSource.getBiome(qxBase + qx, qyBase + qy, qzBase + qz, climateSampler);
                            int blockY = (qyBase + qy) << 2;
                            chunk.setBiome(qx * 4, blockY, qz * 4, biome);
                        }
                    }
                }
            }
        } else {
            fillFixedBiomes(chunk, chunkX, chunkZ, settings.height());
        }

        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int colIdx = lz * 16 + lx;
                int y = Math.max(topSolidY[colIdx], SEA_LEVEL);
                colBiome[colIdx] = biomeManager.getBiome(chunkMinX + lx, y, chunkMinZ + lz);
            }
        }

        long t3 = profiling ? System.nanoTime() : 0;

        // SurfaceRules DSL 表面系统
        var surfFactory = DensityFunction.NoiseHolder.sharedFactory();
        NormalNoise surfDepthNoise = new NormalNoise(surfFactory.fromHashOf("minecraft:surface"), NoiseParameters.get("minecraft:surface"));
        NormalNoise surfSecondaryNoise = new NormalNoise(surfFactory.fromHashOf("minecraft:surface_secondary"), NoiseParameters.get("minecraft:surface_secondary"));
        NormalNoise clayBandsNoise = new NormalNoise(surfFactory.fromHashOf("minecraft:clay_bands_offset"), NoiseParameters.get("minecraft:clay_bands_offset"));
        SurfaceSystem surfSys = new SurfaceSystem(
            OverworldSurfaceRules.overworld(),
            SEA_LEVEL, MIN_Y, settings.height(),
            surfDepthNoise, surfSecondaryNoise, clayBandsNoise,
            chunk, chunkX, chunkZ, biomeManager,
            (x, z) -> noiseChunk.preliminarySurfaceLevel(x, z),
            (x, z) -> {
                int solid = topSolidY[(z & 15) * 16 + (x & 15)];
                return solid > MIN_Y ? solid : SEA_LEVEL;
            });
        if (dimensionType == DimensionType.OVERWORLD) {
        surfSys.buildSurface(chunk);
        } else if (dimensionType == DimensionType.THE_NETHER) {
            // 原版 noise_settings/nether.json surface_rule 全量移植（基岩顶底梯度/玄武岩三角洲/
            // 灵魂沙峡谷/绯红诡异森林 nylium/下界荒地砾石与灵魂沙带/y<32 熔岩坑）。
            buildNetherSurfaceSystem(chunk, chunkX, chunkZ, settings.height(), topSolidY)
                .buildSurface(chunk);
        }

        long t4 = profiling ? System.nanoTime() : 0;

        long t4b = profiling ? System.nanoTime() : 0;

        // 洞穴/峡谷雕刻 — 原版遍历 17×17 邻居区块，用 LegacyRandomSource + setLargeFeatureSeed
        // [原版化迁移] 雕刻移至 generate() 特征阶段（FeatureDecoration.carveOverworld）
        // Bug46: 散矿同样移至 generate() 雕刻之后(原版 ore 是 feature 阶段放置,
        // 曾在雕刻前放置 -> 挖洞后煤/金/钻石等矿残留悬空在洞穴里)。

        if (profiling) {
            profNoiseChunk += t1 - t0;
            profLoop += t2 - t1;
            profBiome += t3 - t2;
            profSurface += t4 - t3;
            profDeepBed += t4b - t4;
            profCarve += (profiling ? System.nanoTime() : 0) - t4b;
            profOre += 0;
        }

        return chunk;
    }



    // biome ID constants matching json/1.21.11/biomes.json
    private static final int B_BADLANDS = 0;
    private static final int B_BAMBOO_JUNGLE = 1;
    private static final int B_BEACH = 3;
    private static final int B_BIRCH_FOREST = 4;
    private static final int B_COLD_OCEAN = 6;
    private static final int B_DARK_FOREST = 8;
    private static final int B_DEEP_COLD_OCEAN = 9;
    private static final int B_DEEP_FROZEN_OCEAN = 11;
    private static final int B_DEEP_LUKEWARM_OCEAN = 12;
    private static final int B_DESERT = 14;
    private static final int B_ERODED_BADLANDS = 19;
    private static final int B_FLOWER_FOREST = 20;
    private static final int B_FOREST = 21;
    private static final int B_FROZEN_OCEAN = 22;
    private static final int B_FROZEN_PEAKS = 23;
    private static final int B_FROZEN_RIVER = 24;
    private static final int B_GROVE = 25;
    private static final int B_ICE_SPIKES = 26;
    private static final int B_JAGGED_PEAKS = 27;
    private static final int B_JUNGLE = 28;
    private static final int B_LUKEWARM_OCEAN = 29;
    private static final int B_MANGROVE_SWAMP = 31;
    private static final int B_MEADOW = 32;
    private static final int B_MUSHROOM_FIELDS = 33;
    private static final int B_OLD_GROWTH_BIRCH_FOREST = 36;
    private static final int B_OLD_GROWTH_PINE_TAIGA = 37;
    private static final int B_OLD_GROWTH_SPRUCE_TAIGA = 38;
    private static final int B_PALE_GARDEN = 39;
    private static final int B_PLAINS = 40;
    private static final int B_RIVER = 41;
    private static final int B_SAVANNA = 42;
    private static final int B_SAVANNA_PLATEAU = 43;
    private static final int B_SNOWY_BEACH = 45;
    private static final int B_SNOWY_PLAINS = 46;
    private static final int B_SNOWY_SLOPES = 47;
    private static final int B_SNOWY_TAIGA = 48;
    private static final int B_SPARSE_JUNGLE = 50;
    private static final int B_STONY_PEAKS = 51;
    private static final int B_STONY_SHORE = 52;
    private static final int B_SUNFLOWER_PLAINS = 53;
    private static final int B_SWAMP = 54;
    private static final int B_TAIGA = 55;
    private static final int B_WARM_OCEAN = 58;
    private static final int B_WINDSWEPT_FOREST = 60;
    private static final int B_WINDSWEPT_GRAVELLY_HILLS = 61;
    private static final int B_WINDSWEPT_HILLS = 62;
    private static final int B_WINDSWEPT_SAVANNA = 63;
    private static final int B_WOODED_BADLANDS = 64;

    private static java.util.Map<String, StructureSet> cachedStructureSets;

    // 维度守卫按结构 id 判断（json 文件名），不能按 type：
    // fortress.json type=minecraft:fortress、bastion_remnant.json type=minecraft:jigsaw，
    // 用 type 字面量匹配 nether_fortress/bastion_remnant 会永不命中 → 下界要塞/堡垒不生成。
    private boolean isStructureAllowed(String id) {
        return switch (dimensionType) {
            case THE_END -> id.equals("end_city");
            case THE_NETHER -> id.equals("nether_fossil") || id.equals("ruined_portal")
                || id.equals("fortress") || id.equals("bastion_remnant");
            default -> !id.equals("end_city") && !id.equals("nether_fossil")
                && !id.equals("fortress") && !id.equals("bastion_remnant");
        };
    }

    /** 中心 4x4 列中位数表面高度（抗极端值，避免浮空/埋地）。 */
    private int medianSurface(int chunkX, int chunkZ, int[] topSolidY) {
        int[] samples = new int[16];
        int si = 0;
        for (int dx = 6; dx <= 9; dx++) {
            for (int dz = 6; dz <= 9; dz++) {
                int ci = dz * 16 + dx;
                samples[si++] = (ci < topSolidY.length) ? topSolidY[ci] + 1 : 64;
            }
        }
        java.util.Arrays.sort(samples);
        return samples[si / 2];
    }

    /** 提前注册本区块潜在 jigsaw 结构 start（在 placeFeatures 前），供所有 chunk 放置时命中。 */
    private void registerStructuresForChunk(Chunk chunk, int chunkX, int chunkZ, int[] topSolidY) {
        StructureManager2 mgr =
            StructureManager2.getInstance();
        if (cachedStructureSets == null) {
            cachedStructureSets = StructureSet.loadAll();
        }
        int medianSurfaceY = medianSurface(chunkX, chunkZ, topSolidY);
        final int finalMedian = medianSurfaceY;
        final int chunkMinBX = chunkX << 4, chunkMaxBX = chunkMinBX + 15;
        final int chunkMinBZ = chunkZ << 4, chunkMaxBZ = chunkMinBZ + 15;
        java.util.function.IntBinaryOperator terrainHeightAt = (lx, lz) -> {
            if (lx >= chunkMinBX && lx <= chunkMaxBX && lz >= chunkMinBZ && lz <= chunkMaxBZ) {
                int ci = (lz & 15) * 16 + (lx & 15);
                return (ci >= 0 && ci < topSolidY.length) ? topSolidY[ci] + 1 : finalMedian;
            }
            return finalMedian;
        };
        int[] allBiomes = computeColBiome(chunk, chunkX, chunkZ, topSolidY);
        int chunkBiome = allBiomes[8 + 8 * 16];

        for (StructureSet set : cachedStructureSets.values()) {
            if (set == null || set.getPlacementLike() == null) continue;
            if (set.getRings() != null) continue; // 要塞走专用环带管线（isStrongholdRingChunk）
            if (!set.getPlacementLike().isStructureChunk(seedLo, chunkX, chunkZ)) continue;

            RandomSource picker =
                StructurePlacementMath
                    .withLargeFeatureSeed(seedLo, chunkX, chunkZ);

            int totalWeight = 0;
            for (StructureSelectionEntry e : set.getStructures()) {
                StructureRegistry.ConfiguredStructure cs =
                    StructureRegistry.get(e.structureId());
                if (cs == null) continue;
                if (!isStructureAllowed(cs.id)) continue;
                Set<Integer> biomes = BiomeTagResolver
                    .getBiomesForStructure(cs.biomesTag);
                if (!biomes.contains(chunkBiome)) continue;
                totalWeight += e.weight();
            }
            if (totalWeight <= 0) continue;

            int roll = picker.nextInt(totalWeight);
            int acc = 0;
            StructureSelectionEntry chosen = null;
            for (StructureSelectionEntry e : set.getStructures()) {
                StructureRegistry.ConfiguredStructure cs =
                    StructureRegistry.get(e.structureId());
                if (cs == null) continue;
                if (!isStructureAllowed(cs.id)) continue;
                Set<Integer> biomes = BiomeTagResolver
                    .getBiomesForStructure(cs.biomesTag);
                if (!biomes.contains(chunkBiome)) continue;
                acc += e.weight();
                if (roll < acc) { chosen = e; break; }
            }
            if (chosen == null) continue;

            StructureRegistry.ConfiguredStructure cs =
                StructureRegistry.get(chosen.structureId());
            if (cs == null) continue;

            int centerX = (chunkX << 4) + 8;
            int centerZ = (chunkZ << 4) + 8;

            // 过程化结构（原版件系统移植）：中央注册 start，跨区块按包围盒重复构建。
            if (cs.id.equals("mineshaft")) {
                // Bug27: 分件系统矿井(房间/走廊/十字/楼梯 + 轨道/蛛网/刷怪笼),
                // 曾用每区块独立 14 段迷你走廊 -> 矿井碎片化互不连通。
                long sSeed = picker.nextLong();
                int centerBiome = chunk.getBiome(8, 8);
                boolean mesa = centerBiome == B_BADLANDS || centerBiome == B_ERODED_BADLANDS
                    || centerBiome == B_WOODED_BADLANDS;
                mgr.addStart(dimensionType, chunkX, chunkZ,
                    ProceduralStructureStart.mineshaft(sSeed, centerX - 4, centerZ - 4,
                        mesa, cs.id, chunkX, chunkZ));
                continue;
            }
            if (cs.id.equals("fortress")) {
                long sSeed = picker.nextLong();
                mgr.addStart(dimensionType, chunkX, chunkZ,
                    ProceduralStructureStart.fortress(sSeed, centerX - 8, centerZ - 8,
                        cs.id, chunkX, chunkZ));
                continue;
            }
            if (cs.id.equals("end_city")) {
                long sSeed = picker.nextLong();
                mgr.addStart(dimensionType, chunkX, chunkZ,
                    ProceduralStructureStart.endCity(sSeed, centerX, medianSurfaceY + 1,
                        centerZ, cs.id, chunkX, chunkZ));
                continue;
            }
            if (cs.id.equals("stronghold")) {
                // Bug27: 主世界要塞(原版 StrongholdPieces 分件系统 + moveBelowSeaLevel)
                long sSeed = picker.nextLong();
                int startY = medianSurfaceY - 25; // 大致地下埋深(原版从 y25 探深调整)
                mgr.addStart(dimensionType, chunkX, chunkZ,
                    ProceduralStructureStart.stronghold(sSeed,
                        centerX, startY, centerZ, cs.id, chunkX, chunkZ));
                continue;
            }
            if (!cs.isJigsaw() || cs.startPool == null) continue; // 非 jigsaw 由 placeStructures2 直接放置

            int surfaceY = medianSurfaceY;
            int heightY;
            if (cs.projectStartToHeightmap) {
                heightY = surfaceY + cs.startHeightMin;
            } else if ("uniform".equals(cs.startHeightType)) {
                int range = cs.startHeightMax - cs.startHeightMin + 1;
                heightY = cs.startHeightMin + (range > 0 ? picker.nextInt(range) : 0);
            } else {
                heightY = cs.startHeightMin;
            }
            long structSeed = picker.nextLong();
            java.util.List<PoolElementStructurePiece> pieces =
                JigsawPlacement.addPieces(
                    mgr.getTemplateManager(), cs.startPool, cs.size, cs.maxDistance,
                    centerX, heightY, centerZ, heightY, structSeed, terrainHeightAt,
                    com.CharunCore.server.worldgen.structure2.PoolAliases.resolve(cs.poolAliases, picker));
            if (pieces == null || pieces.isEmpty()) continue;
            mgr.addStart(dimensionType, chunkX, chunkZ,
                new StructureStart(
                    chosen.structureId(), chunkX, chunkZ, pieces));
        }
    }

    /** Bug43: 纯种子 start(下界要塞)远窗口提前注册。
     *  要塞 start 只依赖种子/网格/权重, 不依赖锚点区块地形(无需 medianSurface/biome 查询 ——
     *  要塞 biome 标签覆盖全部下界群系), 因此可在任意区块生成期确定性重算并提前注册。
     *  与 registerStructuresForChunk 的选择流程逐行同序(随机数消费一致), 两者对同一锚点
     *  必然产出相同 start, addStart 按 (id,chunkX,chunkZ) 去重幂等。 */
    private void registerFarSeedStarts(int chunkX, int chunkZ) {
        if (dimensionType != DimensionType.THE_NETHER) return;
        StructureManager2 mgr = StructureManager2.getInstance();
        if (cachedStructureSets == null) cachedStructureSets = StructureSet.loadAll();
        final int window = 12; // 要塞分件最大延伸(区块)
        for (int dx = -window; dx <= window; dx++) {
            for (int dz = -window; dz <= window; dz++) {
                int ax = chunkX + dx, az = chunkZ + dz;
                for (StructureSet set : cachedStructureSets.values()) {
                    if (set == null || set.getPlacementLike() == null) continue;
                    if (set.getRings() != null) continue;
                    if (!set.getPlacementLike().isStructureChunk(seedLo, ax, az)) continue;
                    boolean hasFortress = false;
                    for (StructureSelectionEntry e : set.getStructures()) {
                        if ("fortress".equals(e.structureId())) { hasFortress = true; break; }
                    }
                    if (!hasFortress) continue;
                    // 与 registerStructuresForChunk 相同的随机流: picker -> 权重选择 -> nextLong
                    RandomSource picker = StructurePlacementMath.withLargeFeatureSeed(seedLo, ax, az);
                    int totalWeight = 0;
                    for (StructureSelectionEntry e : set.getStructures()) {
                        StructureRegistry.ConfiguredStructure cs = StructureRegistry.get(e.structureId());
                        if (cs == null) continue;
                        if (!isStructureAllowed(cs.id)) continue;
                        totalWeight += e.weight();
                    }
                    if (totalWeight <= 0) continue;
                    int roll = picker.nextInt(totalWeight);
                    int acc = 0;
                    StructureSelectionEntry chosen = null;
                    for (StructureSelectionEntry e : set.getStructures()) {
                        StructureRegistry.ConfiguredStructure cs = StructureRegistry.get(e.structureId());
                        if (cs == null) continue;
                        if (!isStructureAllowed(cs.id)) continue;
                        acc += e.weight();
                        if (roll < acc) { chosen = e; break; }
                    }
                    if (chosen == null || !"fortress".equals(chosen.structureId())) continue;
                    long sSeed = picker.nextLong();
                    int centerX = (ax << 4) + 8;
                    int centerZ = (az << 4) + 8;
                    mgr.addStart(dimensionType, ax, az,
                        ProceduralStructureStart.fortress(sSeed, centerX - 8, centerZ - 8,
                            "fortress", ax, az));
                }
            }
        }
    }

    private void placeStructures2(WorldGenLevel level, Chunk chunk, int chunkX, int chunkZ,
                                    int[] topSolidY) {
        StructureManager2 mgr =
            StructureManager2.getInstance();

        if (cachedStructureSets == null) {
            cachedStructureSets = StructureSet.loadAll();
        }

        int medianSurfaceY = medianSurface(chunkX, chunkZ, topSolidY);
        int[] allBiomes = computeColBiome(chunk, chunkX, chunkZ, topSolidY);
        int chunkBiome = allBiomes[8 + 8 * 16];

        for (StructureSet set : cachedStructureSets.values()) {
            if (set == null || set.getPlacementLike() == null) continue;
            if (set.getRings() != null) continue; // 要塞走专用环带管线（isStrongholdRingChunk）
            if (!set.getPlacementLike().isStructureChunk(seedLo, chunkX, chunkZ)) continue;

            RandomSource picker =
                StructurePlacementMath
                    .withLargeFeatureSeed(seedLo, chunkX, chunkZ);

            int totalWeight = 0;
            for (StructureSelectionEntry e : set.getStructures()) {
                StructureRegistry.ConfiguredStructure cs =
                    StructureRegistry.get(e.structureId());
                if (cs == null) continue;
                if (!isStructureAllowed(cs.id)) continue;
                Set<Integer> biomes = BiomeTagResolver
                    .getBiomesForStructure(cs.biomesTag);
                if (!biomes.contains(chunkBiome)) continue;
                totalWeight += e.weight();
            }
            if (totalWeight <= 0) continue;

            int roll = picker.nextInt(totalWeight);
            int acc = 0;
            StructureSelectionEntry chosen = null;
            for (StructureSelectionEntry e : set.getStructures()) {
                StructureRegistry.ConfiguredStructure cs =
                    StructureRegistry.get(e.structureId());
                if (cs == null) continue;
                if (!isStructureAllowed(cs.id)) continue;
                Set<Integer> biomes = BiomeTagResolver
                    .getBiomesForStructure(cs.biomesTag);
                if (!biomes.contains(chunkBiome)) continue;
                acc += e.weight();
                if (roll < acc) { chosen = e; break; }
            }
            if (chosen == null) continue;

            StructureRegistry.ConfiguredStructure cs =
                StructureRegistry.get(chosen.structureId());
            if (cs == null) continue;

            int centerX = (chunkX << 4) + 8;
            int centerZ = (chunkZ << 4) + 8;
            int surfaceY = medianSurfaceY;

            if (cs.isJigsaw() && cs.startPool != null) {
                // jigsaw start 已在 registerStructuresForChunk 提前注册（addStart 幂等去重）
                continue;
            } else {
                NonJigsawPlacer.place(
                    level, cs.id, cs.type, chunkX, chunkZ, centerX, centerZ, surfaceY, picker);
            }
        }

        mgr.placeStructuresForChunk(level, dimensionType, chunkX, chunkZ);

        // 【原版结构语义】处理 structure_block 数据标记:
        // Chest→下方箱子战利品 / Sentry→潜影贝 / Elytra→鞘翅展示框
        StructureMarkerProcessor.process(level, dimensionType);
    }

    private void placeFeatures(WorldGenLevel level, Chunk chunk, int chunkX, int chunkZ,
                                int[] topSolidY, int[] colBiome) {
        // 末地: 黑曜石柱(主岛) + 紫颂树(外岛), 无主世界植被/矿石
        if (dimensionType == DimensionType.THE_END) {
            com.CharunCore.server.worldgen.feature.EndSpikeFeature.generate(
                level, chunkX, chunkZ, seedLo);
            ChorusTreeFeature.generate(
                level, chunkX, chunkZ,
                new XoroshiroRandomSource(seedLo + chunkX * 341873128712L + chunkZ * 132897987541L + 98765L));
            return;
        }

        // 下界: 原版式特征（萤石簇/岩浆块/蘑菇/火焰/巨型菌/玄武岩柱）
        if (dimensionType == DimensionType.THE_NETHER) {
            NetherFeatures.generate(level, chunk, chunkX, chunkZ, colBiome,
                new XoroshiroRandomSource(seedLo + chunkX * 341873128712L + chunkZ * 132897987541L + 424242L));
            return;
        }

        // 主世界: 采用快速稳定的旧版装饰管线（~50ms/区块，避免 JSON 管线从网络线程
        // 同步生成导致客户端超时/区块永不完成 = "无法放置/挖掘"）。
        // JSON 版 applyBiomeDecoration 管线保留在 feature/vanilla，待性能与
        // RNG 顺序核对后再重新接线。
        if (dimensionType != DimensionType.OVERWORLD) return;

        WorldgenRandom worldgenRandom = new WorldgenRandom(new XoroshiroRandomSource(0L));
        long decorationSeed = worldgenRandom.setDecorationSeed(seedLo, chunkX << 4, chunkZ << 4);

        aquaticGen.generate(level, chunk, chunkX, chunkZ, featureRandom(decorationSeed, 0, 0), colBiome, topSolidY);
        springGen.generate(chunk, chunkX, chunkZ, featureRandom(decorationSeed, 1, 0));

        placeTrees(level, chunk, chunkX, chunkZ, topSolidY, colBiome, featureRandom(decorationSeed, 2, 0));
        placeGroundCover(chunk, chunkX, chunkZ, topSolidY, colBiome, featureRandom(decorationSeed, 3, 0));

        surfaceDecor.decorate(chunk, chunkX, chunkZ, topSolidY, colBiome, featureRandom(decorationSeed, 4, 0));
        biomeDecor.decorate(chunk, chunkX, chunkZ, topSolidY, colBiome, featureRandom(decorationSeed, 5, 0));

        glowLichenGen.generate(chunk, chunkX, chunkZ, featureRandom(decorationSeed, 6, 0));
        monsterRoomGen.generate(chunk, chunkX, chunkZ, featureRandom(decorationSeed, 7, 0));
        dripstoneGen.generate(chunk, chunkX, chunkZ, featureRandom(decorationSeed, 8, 0));
        geodeGen.generate(chunk, chunkX, chunkZ, featureRandom(decorationSeed, 9, 0));
    }

    private static RandomSource featureRandom(long decorationSeed, int index, int step) {
        WorldgenRandom r = new WorldgenRandom(new XoroshiroRandomSource(0L));
        r.setFeatureSeed(decorationSeed, index, step);
        return r;
    }

    private void placeTrees(WorldGenLevel level, Chunk chunk, int chunkX, int chunkZ,
                            int[] topSolidY, int[] colBiome, RandomSource rng) {
        // 已放置树的记录: 同 chunk 内新树与已放树树干水平距离 < MIN_TREE_DIST 则跳过,
        // 避免树冠重叠挤一起(原版树生成有隐式间距)。树冠半径 ~2 -> 间距 3 足够。
        final int MIN_TREE_DIST = 3;
        java.util.List<int[]> placed = new java.util.ArrayList<>();
        for (int attempt = 0; attempt < 64; attempt++) {
            // 巨冠树(黑橡木/丛林: 2x2 树干 + 半径 3 树冠)需要 3 格内缩, 其余 2 格,
            // 否则树冠被区块边界裁掉
            int lx = 2 + rng.nextInt(12);
            int lz = 2 + rng.nextInt(12);
            int probeBiome = colBiome[lz * 16 + lx];
            TreeType probeType = SimpleTreeFeature.biomeTreeType(probeBiome);
            if (probeType == TreeType.DARK_OAK || probeType == TreeType.JUNGLE) {
                lx = 3 + rng.nextInt(10);
                lz = 3 + rng.nextInt(10);
            }
            int colIdx = lz * 16 + lx;
            int tsy = topSolidY[colIdx];
            if (tsy < SEA_LEVEL || tsy >= MAX_Y - 12) continue;

            int biome = colBiome[colIdx];
            TreeType treeType = SimpleTreeFeature.biomeTreeType(biome);
            if (treeType == null) continue;

            int threshold = treeThreshold(biome);
            if (threshold <= 0 || rng.nextInt(threshold) != 0) continue;

            // 间距检查: 与本 chunk 已放树树干距离 < MIN_TREE_DIST 则跳过
            boolean tooClose = false;
            for (int[] p : placed) {
                int dx = p[0] - lx, dz = p[1] - lz;
                if (dx * dx + dz * dz < MIN_TREE_DIST * MIN_TREE_DIST) { tooClose = true; break; }
            }
            if (tooClose) continue;

            int groundBlock = chunk.getBlock(lx, tsy, lz);
            String groundName = BlockStateHelper.getName(groundBlock);
            if (groundName.equals("water") || groundName.equals("lava") || groundName.equals("air")) continue;
            // Defense-in-depth: reject non-ground surfaces (leaves/logs from neighbor features)
            if (groundName != null) {
                String gn = groundName.startsWith("minecraft:") ? groundName.substring(10) : groundName;
                if (gn.contains("leaves") || gn.contains("_log") || gn.contains("_wood")
                    || gn.equals("vine") || gn.contains("sapling")) continue;
            }
            if (chunk.getBlock(lx, tsy + 1, lz) != 0) continue;

            SimpleTreeFeature.placeTree(level, chunkX * 16 + lx, tsy + 1, chunkZ * 16 + lz, treeType, rng);
            placed.add(new int[]{lx, lz});
        }
    }

    private int treeThreshold(int biome) {
        return switch (biome) {
            case B_DARK_FOREST -> 3;
            case B_FOREST, B_FLOWER_FOREST -> 5;
            case B_BIRCH_FOREST, B_OLD_GROWTH_BIRCH_FOREST -> 5;
            case B_TAIGA, B_OLD_GROWTH_SPRUCE_TAIGA, B_OLD_GROWTH_PINE_TAIGA -> 5;
            case B_JUNGLE, B_BAMBOO_JUNGLE, B_SPARSE_JUNGLE -> 6;
            case B_SWAMP -> 8;
            case B_MEADOW -> 8;
            case B_SAVANNA, B_SAVANNA_PLATEAU -> 10;
            case B_WINDSWEPT_FOREST -> 7;
            case B_WINDSWEPT_SAVANNA -> 12;
            case B_PLAINS, B_SUNFLOWER_PLAINS -> 16;
            default -> 0;
        };
    }

    private void placeGroundCover(Chunk chunk, int chunkX, int chunkZ,
                                  int[] topSolidY, int[] colBiome, RandomSource rng) {
        for (int attempt = 0; attempt < 128; attempt++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            int tsy = topSolidY[colIdx];
            if (tsy < SEA_LEVEL || tsy >= MAX_Y - 1) continue;
            if (tsy + 1 > MAX_Y) continue;

            int biome = colBiome[colIdx];
            int surfaceBlock = chunk.getBlock(lx, tsy, lz);
            int aboveBlock = chunk.getBlock(lx, tsy + 1, lz);

            if (aboveBlock != 0) continue;

            boolean canGrass = surfaceBlock == grassId || surfaceBlock == dirtId
                || surfaceBlock == podzolId || surfaceBlock == coarseDirtId;
            if (!canGrass) continue;

            boolean isFern = biome == B_TAIGA || biome == B_OLD_GROWTH_SPRUCE_TAIGA
                || biome == B_OLD_GROWTH_PINE_TAIGA || biome == B_SNOWY_TAIGA
                || biome == B_GROVE || biome == B_OLD_GROWTH_BIRCH_FOREST;

            int plantY = tsy + 1;

            int r = rng.nextInt(10);
            if (r < 3) {
                int plant = isFern ? FERN_ID : SHORT_GRASS_ID;
                chunk.setBlock(lx, plantY, lz, plant);
                if (!isFern && rng.nextInt(8) == 0 && plantY + 1 <= MAX_Y) {
                    chunk.setBlock(lx, plantY, lz, TALL_GRASS_ID);
                    chunk.setBlock(lx, plantY + 1, lz, TALL_GRASS_ID);
                }
                if (isFern && rng.nextInt(6) == 0 && plantY + 1 <= MAX_Y) {
                    chunk.setBlock(lx, plantY, lz, LARGE_FERN_ID);
                    chunk.setBlock(lx, plantY + 1, lz, LARGE_FERN_ID);
                }
            } else if (r < 5) {
                int flower = flowerForBiome(biome, rng);
                chunk.setBlock(lx, plantY, lz, flower);
            } else if (r < 6) {
                if (biome == B_DESERT && surfaceBlock == SAND_ID) {
                    chunk.setBlock(lx, plantY, lz, DEAD_BUSH_ID);
                } else if (biome == B_SWAMP) {
                    chunk.setBlock(lx, plantY, lz, rng.nextBoolean() ? RED_MUSHROOM_ID : BROWN_MUSHROOM_ID);
                } else if (biome == B_DARK_FOREST) {
                    chunk.setBlock(lx, plantY, lz, rng.nextBoolean() ? RED_MUSHROOM_ID : BROWN_MUSHROOM_ID);
                } else if (isTaigaBiome(biome) && surfaceBlock == grassId) {
                    if (rng.nextInt(20) == 0) {
                        chunk.setBlock(lx, plantY, lz, SWEET_BERRY_BUSH_ID);
                    }
                }
            }
        }

        if (rng.nextInt(20) == 0) {
            for (int i = 0; i < 3; i++) {
                int lx = rng.nextInt(16);
                int lz = rng.nextInt(16);
                int colIdx = lz * 16 + lx;
                int tsy = topSolidY[colIdx];
                if (tsy < SEA_LEVEL || tsy >= MAX_Y - 1) continue;
                int ground = chunk.getBlock(lx, tsy, lz);
                if ((ground == grassId || ground == SAND_ID) && chunk.getBlock(lx, tsy + 1, lz) == 0) {
                    boolean nearWater = false;
                    if (lx > 0 && chunk.getBlock(lx - 1, tsy, lz) == WATER_ID) nearWater = true;
                    if (lx < 15 && chunk.getBlock(lx + 1, tsy, lz) == WATER_ID) nearWater = true;
                    if (lz > 0 && chunk.getBlock(lx, tsy, lz - 1) == WATER_ID) nearWater = true;
                    if (lz < 15 && chunk.getBlock(lx, tsy, lz + 1) == WATER_ID) nearWater = true;
                    if (nearWater) {
                        int caneH = 1 + rng.nextInt(3);
                        for (int h = 0; h < caneH && tsy + 1 + h <= MAX_Y; h++) {
                            if (chunk.getBlock(lx, tsy + 1 + h, lz) == 0) {
                                chunk.setBlock(lx, tsy + 1 + h, lz, SUGAR_CANE_ID);
                            }
                        }
                    }
                }
            }
        }
    }

    private static int flowerForBiome(int biome, RandomSource rng) {
        return switch (biome) {
            case B_FLOWER_FOREST -> {
                int r = rng.nextInt(10);
                yield switch (r) {
                    case 0 -> DANDELION_ID;
                    case 1, 2 -> POPPY_ID;
                    case 3 -> ALLIUM_ID;
                    case 4 -> CORNFLOWER_ID;
                    case 5 -> OXEYE_DAISY_ID;
                    case 6 -> AZURE_BLUET_ID;
                    case 7 -> RED_TULIP_ID;
                    case 8 -> ORANGE_TULIP_ID;
                    default -> WHITE_TULIP_ID;
                };
            }
            case B_PLAINS, B_SUNFLOWER_PLAINS -> {
                int r = rng.nextInt(6);
                yield r < 2 ? DANDELION_ID : r < 4 ? POPPY_ID : r == 4 ? OXEYE_DAISY_ID : CORNFLOWER_ID;
            }
            case B_MEADOW -> {
                int r = rng.nextInt(5);
                yield r < 2 ? DANDELION_ID : r == 2 ? POPPY_ID : r == 3 ? CORNFLOWER_ID : OXEYE_DAISY_ID;
            }
            case B_SWAMP -> BLUE_ORCHID_ID;
            case B_TAIGA, B_OLD_GROWTH_SPRUCE_TAIGA, B_SNOWY_TAIGA -> LILY_OF_THE_VALLEY_ID;
            case B_FOREST, B_DARK_FOREST -> {
                int r = rng.nextInt(4);
                yield r == 0 ? DANDELION_ID : r == 1 ? POPPY_ID : r == 2 ? OXEYE_DAISY_ID : CORNFLOWER_ID;
            }
            case B_JUNGLE, B_SPARSE_JUNGLE, B_BAMBOO_JUNGLE -> {
                int r = rng.nextInt(3);
                yield r == 0 ? DANDELION_ID : r == 1 ? POPPY_ID : ALLIUM_ID;
            }
            case B_SAVANNA, B_SAVANNA_PLATEAU, B_WINDSWEPT_SAVANNA -> {
                int r = rng.nextInt(4);
                yield r == 0 ? DANDELION_ID : r == 1 ? POPPY_ID : r == 2 ? ALLIUM_ID : CORNFLOWER_ID;
            }
            default -> DANDELION_ID;
        };
    }

    private static boolean isTaigaBiome(int biome) {
        return biome == B_TAIGA || biome == B_OLD_GROWTH_SPRUCE_TAIGA || biome == B_OLD_GROWTH_PINE_TAIGA || biome == B_SNOWY_TAIGA;
    }

    private static final int FERN_ID = BlockStateHelper.getDefault("fern");
    private static final int SHORT_GRASS_ID = BlockStateHelper.getDefault("short_grass");
    private static final int TALL_GRASS_ID = BlockStateHelper.getDefault("tall_grass");
    private static final int LARGE_FERN_ID = BlockStateHelper.getDefault("large_fern");
    private static final int DEAD_BUSH_ID = BlockStateHelper.getDefault("dead_bush");
    private static final int RED_MUSHROOM_ID = BlockStateHelper.getDefault("red_mushroom");
    private static final int BROWN_MUSHROOM_ID = BlockStateHelper.getDefault("brown_mushroom");
    private static final int SWEET_BERRY_BUSH_ID = BlockStateHelper.getDefault("sweet_berry_bush");
    private static final int SUGAR_CANE_ID = BlockStateHelper.getDefault("sugar_cane");
    private static final int SAND_ID = BlockStateHelper.getDefault("sand");
    private static final int WATER_ID = BlockStateHelper.getDefault("water");
    private static final int DANDELION_ID = BlockStateHelper.getDefault("dandelion");
    private static final int POPPY_ID = BlockStateHelper.getDefault("poppy");
    private static final int ALLIUM_ID = BlockStateHelper.getDefault("allium");
    private static final int CORNFLOWER_ID = BlockStateHelper.getDefault("cornflower");
    private static final int OXEYE_DAISY_ID = BlockStateHelper.getDefault("oxeye_daisy");
    private static final int AZURE_BLUET_ID = BlockStateHelper.getDefault("azure_bluet");
    private static final int RED_TULIP_ID = BlockStateHelper.getDefault("red_tulip");
    private static final int ORANGE_TULIP_ID = BlockStateHelper.getDefault("orange_tulip");
    private static final int WHITE_TULIP_ID = BlockStateHelper.getDefault("white_tulip");
    private static final int BLUE_ORCHID_ID = BlockStateHelper.getDefault("blue_orchid");
    private static final int LILY_OF_THE_VALLEY_ID = BlockStateHelper.getDefault("lily_of_the_valley");
}
