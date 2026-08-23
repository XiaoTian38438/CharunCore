package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.world.gen.XoroshiroRandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class StructureManager {

    private static final int SEA_LEVEL = 63;

    /**
     * @deprecated 仅保留以兼容历史引用；要塞实际放置已改为同心环（B3），
     * 见 {@link #getStrongholds(long)} / {@link #isStrongholdChunk(int, int, long)}。
     */
    @Deprecated
    public static final int STRONGHOLD_SPACING = 48;
    @Deprecated
    public static final int STRONGHOLD_SEPARATION = 12;

    // 同心环要塞放置（vanilla ConcentricRingsStructurePlacement 近似）：
    // 共 128 个、按环半径递增、环内均匀角分布 + 每环半径扰动。
    private static final int STRONGHOLD_TOTAL = 128;
    private static final double STRONGHOLD_FIRST_RING_RADIUS = 1536.0; // 首环半径（方块）
    private static final double STRONGHOLD_RING_SPACING = 1536.0;      // 每环半径递增
    private static final double STRONGHOLD_RING_JITTER = 256.0;        // 每环半径扰动
    private static final long STRONGHOLD_SALT = 0x9E3779B97F4A7C15L;

    // seed -> 要塞区块坐标列表（{chunkX, chunkZ}），缓存以避免每区块重算 128 点。
    private static final Map<Long, int[][]> STRONGHOLD_CACHE = new ConcurrentHashMap<>();

    /**
     * 死代码（审计 P1-6）。无任何外部调用方（活跃管线经 structure2 与 generateStronghold）。
     * 仅引用同包已废弃的 4 个旧模板生成器与 StructureTemplateLoader；删除需一并清理这些引用，
     * 否则会破坏编译，故保留并标注。
     */
    @Deprecated(since = "audit P1-6", forRemoval = true)
    public static void generateStructures(Chunk chunk, int chunkX, int chunkZ, long seed,
                                           int[] topSolidY, int[] colBiome) {
        RandomSource rng = new XoroshiroRandomSource(seed + chunkX * 98765L + chunkZ * 12345L);

        if (shouldPlace(chunkX, chunkZ, seed, 32, 8)) {
            int lx = 3 + rng.nextInt(10);
            int lz = 3 + rng.nextInt(10);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y > SEA_LEVEL && y < 300) {
                int biome = colBiome[colIdx];
                if (isDesertBiome(biome)) {
                    DesertPyramidGenerator.generate(chunk, chunkX, chunkZ, lx, lz, rng);
                }
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 16, 4)) {
            int lx = 4 + rng.nextInt(8);
            int lz = 4 + rng.nextInt(8);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y >= SEA_LEVEL && y < 300) {
                int biome = colBiome[colIdx];
                if (isTaigaOrSnowyBiome(biome) || biome == 40 || biome == 21) {
                    IglooGenerator.generate(chunk, chunkX, chunkZ, lx, lz, rng);
                }
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 24, 6)) {
            int lx = 4 + rng.nextInt(8);
            int lz = 4 + rng.nextInt(8);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y >= SEA_LEVEL && y < 300) {
                int biome = colBiome[colIdx];
                if (biome == 28 || biome == 1 || biome == 50) {
                    JungleTempleGenerator.generate(chunk, chunkX, chunkZ, lx, lz, rng);
                }
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 20, 5)) {
            int lx = 5 + rng.nextInt(6);
            int lz = 5 + rng.nextInt(6);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y >= SEA_LEVEL && y < 300) {
                int biome = colBiome[colIdx];
                if (biome == 54 || biome == 31) {
                    SwampHutGenerator.generate(chunk, chunkX, chunkZ, lx, lz, rng);
                }
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 40, 10)) {
            int lx = 2 + rng.nextInt(12);
            int lz = 2 + rng.nextInt(12);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y > 5 && y < 50) {
                MineshaftGenerator.generate(chunk, chunkX, chunkZ, lx, lz, y - 2, rng);
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 50, 12)) {
            int lx = 2 + rng.nextInt(12);
            int lz = 2 + rng.nextInt(12);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y > 5 && y < SEA_LEVEL) {
                DungeonGenerator.generate(chunk, lx, lz, y - 2, rng);
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 27, 7)) {
            int lx = 4 + rng.nextInt(8);
            int lz = 4 + rng.nextInt(8);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y >= SEA_LEVEL - 5 && y < 300) {
                PillagerOutpostGenerator.generate(chunk, chunkX, chunkZ, lx, lz, rng);
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 14, 3)) {
            int lx = 4 + rng.nextInt(8);
            int lz = 4 + rng.nextInt(8);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y > 10 && y < 40) {
                NetherFortressGenerator.generate(chunk, chunkX, chunkZ, lx, lz, y, rng);
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 80, 20)) {
            int lx = 3 + rng.nextInt(10);
            int lz = 3 + rng.nextInt(10);
            NetherFossilGenerator.generate(chunk, lx, lz, 10 + rng.nextInt(30), rng);
        }

        if (isStrongholdChunk(chunkX, chunkZ, seed)) {
            int surface = topSolidY[8 * 16 + 8];
            if (surface > SEA_LEVEL - 2 && surface < 220) {
                int floorY = Math.max(-30, Math.min(surface - 40, 25));
                StrongholdPortalRoomGenerator.generate(chunk, floorY, surface, rng);
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 20, 5)) {
            int lx = 3 + rng.nextInt(10);
            int lz = 3 + rng.nextInt(10);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y >= SEA_LEVEL - 2 && y < 300) {
                ShipwreckGenerator.generate(chunk, chunkX, chunkZ, lx, lz, rng);
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 20, 8)) {
            int lx = 3 + rng.nextInt(10);
            int lz = 3 + rng.nextInt(10);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y <= SEA_LEVEL && y > -60) {
                int biome = colBiome[colIdx];
                boolean warm = biome == 29 || biome == 12 || biome == 6 || biome == 58;
                OceanRuinGenerator.generate(chunk, chunkX, chunkZ, lx, lz, rng, warm);
            }
        }

        if (shouldPlace(chunkX, chunkZ, seed, 25, 8)) {
            int lx = 3 + rng.nextInt(10);
            int lz = 3 + rng.nextInt(10);
            int colIdx = lz * 16 + lx;
            int y = topSolidY[colIdx];
            if (y >= SEA_LEVEL - 5 && y < 300) {
                RuinedPortalGenerator.generate(chunk, chunkX, chunkZ, lx, lz, rng);
            }
        }

        for (int i = 0; i < 2; i++) {
            if (shouldPlace(chunkX + i * 100, chunkZ + i * 77, seed + i, 30, 8)) {
                int lx = 3 + rng.nextInt(10);
                int lz = 3 + rng.nextInt(10);
                int colIdx = lz * 16 + lx;
                int y = topSolidY[colIdx];
                if (y >= SEA_LEVEL && y < 300) {
                    int biome = colBiome[colIdx];
                    if (isDesertBiome(biome) || biome == 14 || biome == 0 || biome == 19) {
                        DesertPyramidGenerator.generate(chunk, chunkX, chunkZ, lx, lz, rng);
                    }
                }
            }
        }
    }

    /**
     * Stronghold-only entry point. The legacy generateStructures() is superseded by the
     * data-driven structure2 pipeline for every structure EXCEPT the stronghold, whose
     * vanilla placement type (concentric_rings) is not supported by StructureSet.load().
     * Without this hook the End portal room would never generate anywhere in the world.
     */
    public static void generateStronghold(Chunk chunk, int chunkX, int chunkZ, long seed, int[] topSolidY) {
        if (!isStrongholdChunk(chunkX, chunkZ, seed)) return;
        int surface = topSolidY[8 * 16 + 8];
        RandomSource rng = new XoroshiroRandomSource(
                seed ^ ((long) chunkX * 341873128712L) ^ ((long) chunkZ * 132897987541L));
        int floorY = Math.max(-30, Math.min(surface - 40, 25));
        StrongholdPortalRoomGenerator.generate(chunk, floorY, surface, rng);
        System.out.println("[结构] 要塞传送门房间生成于 chunk(" + chunkX + ", " + chunkZ + ") floorY=" + floorY);
    }

    private static boolean shouldPlace(int chunkX, int chunkZ, long seed, int spacing, int separation) {
        int cellX = Math.floorDiv(chunkX, spacing);
        int cellZ = Math.floorDiv(chunkZ, spacing);
        long key = seed ^ ((long) cellX * 341873128712L) ^ ((long) cellZ * 132897987541L);
        RandomSource cellRng = new XoroshiroRandomSource(key);
        int nextSpacing = spacing - separation;
        int offsetX = cellRng.nextInt(nextSpacing);
        int offsetZ = cellRng.nextInt(nextSpacing);
        int structX = cellX * spacing + offsetX;
        int structZ = cellZ * spacing + offsetZ;
        return chunkX == structX && chunkZ == structZ;
    }

    private static boolean isDesertBiome(int biome) {
        return biome == 14 || biome == 0 || biome == 19 || biome == 64;
    }

    private static boolean isTaigaOrSnowyBiome(int biome) {
        return biome == 55 || biome == 37 || biome == 38 || biome == 48 || biome == 46 || biome == 26 || biome == 47;
    }

    public static int[] findNearest(String structureName, int playerChunkX, int playerChunkZ, long seed) {
        // B3：要塞用同心环放置，直接在所有预生成位置中取最近的一个。
        if ("stronghold".equals(structureName) || "strongholds".equals(structureName)) {
            int[][] sh = getStrongholds(seed);
            long bestDist = Long.MAX_VALUE;
            int[] best = null;
            for (int[] s : sh) {
                long ddx = (long) s[0] - playerChunkX;
                long ddz = (long) s[1] - playerChunkZ;
                long dist = ddx * ddx + ddz * ddz;
                if (dist < bestDist) {
                    bestDist = dist;
                    best = s;
                }
            }
            return best;
        }
        int[] cfg = placementFor(structureName);
        if (cfg == null) return null;
        int spacing = cfg[0];
        int separation = cfg[1];
        int nextSpacing = spacing - separation;
        if (nextSpacing <= 0) return null;

        int baseCellX = Math.floorDiv(playerChunkX, spacing);
        int baseCellZ = Math.floorDiv(playerChunkZ, spacing);
        long bestDist = Long.MAX_VALUE;
        int[] bestResult = null;

        for (int dx = -8; dx <= 8; dx++) {
            for (int dz = -8; dz <= 8; dz++) {
                int cellX = baseCellX + dx;
                int cellZ = baseCellZ + dz;
                long key = seed ^ ((long) cellX * 341873128712L) ^ ((long) cellZ * 132897987541L);
                RandomSource cellRng = new XoroshiroRandomSource(key);
                int structX = cellX * spacing + cellRng.nextInt(nextSpacing);
                int structZ = cellZ * spacing + cellRng.nextInt(nextSpacing);
                long ddx = structX - playerChunkX;
                long ddz = structZ - playerChunkZ;
                long dist = ddx * ddx + ddz * ddz;
                if (dist < bestDist) {
                    bestDist = dist;
                    bestResult = new int[]{structX, structZ};
                }
            }
        }
        return bestResult;
    }

    /**
     * 判定某区块是否为要塞所在区块（B3 同心环放置）。
     * 与 {@link #getStrongholds(long)} 使用同一 seed 派生算法，保证生成与定位一致。
     */
    public static boolean isStrongholdChunk(int chunkX, int chunkZ, long seed) {
        for (int[] s : getStrongholds(seed)) {
            if (s[0] == chunkX && s[1] == chunkZ) return true;
        }
        return false;
    }

    /**
     * 返回（并缓存）该 seed 下全部 128 个要塞的区块坐标（{chunkX, chunkZ}），
     * 按同心环分布：首环半径 {@link #STRONGHOLD_FIRST_RING_RADIUS}，每环递增
     * {@link #STRONGHOLD_RING_SPACING}，环内数量随环号递增（3, 6, 9, …），
     * 角向均匀 + 每环半径扰动，整体围绕出生点 (0,0)。
     */
    private static int[][] getStrongholds(long seed) {
        int[][] cached = STRONGHOLD_CACHE.get(seed);
        if (cached == null) {
            synchronized (STRONGHOLD_CACHE) {
                cached = STRONGHOLD_CACHE.get(seed);
                if (cached == null) {
                    cached = computeStrongholds(seed);
                    STRONGHOLD_CACHE.put(seed, cached);
                }
            }
        }
        return cached;
    }

    private static int[][] computeStrongholds(long seed) {
        RandomSource rng = new XoroshiroRandomSource(seed ^ STRONGHOLD_SALT);
        List<int[]> list = new ArrayList<>();
        Set<Long> used = new HashSet<>();
        int placed = 0;
        int ring = 0;
        double radius = STRONGHOLD_FIRST_RING_RADIUS;
        while (placed < STRONGHOLD_TOTAL && ring < 64) {
            int inRing = Math.min(STRONGHOLD_TOTAL - placed, 3 * (ring + 1));
            double r = radius + (rng.nextDouble() - 0.5) * STRONGHOLD_RING_JITTER;
            double startAngle = rng.nextDouble() * Math.PI * 2.0;
            for (int i = 0; i < inRing; i++) {
                double angle = startAngle + (i / (double) inRing) * Math.PI * 2.0;
                int bx = (int) Math.round(Math.cos(angle) * r);
                int bz = (int) Math.round(Math.sin(angle) * r);
                int cgx = Math.floorDiv(bx, 16);
                int cgz = Math.floorDiv(bz, 16);
                long key = ((long) cgx << 32) ^ (cgz & 0xFFFFFFFFL);
                if (used.add(key)) {
                    list.add(new int[]{cgx, cgz});
                    placed++;
                }
                // 区块冲突极罕见（环间半径相差 1536 格），跳过即可，不影响总数级。
            }
            radius += STRONGHOLD_RING_SPACING;
            ring++;
        }
        return list.toArray(new int[0][]);
    }

    private static int[] placementFor(String name) {
        return switch (name) {
            case "desert_pyramid" -> new int[]{32, 8};
            case "igloo" -> new int[]{16, 4};
            case "jungle_temple" -> new int[]{24, 6};
            case "swamp_hut" -> new int[]{20, 5};
            case "mineshaft" -> new int[]{40, 10};
            case "dungeon" -> new int[]{50, 12};
            case "pillager_outpost" -> new int[]{27, 7};
            case "nether_fortress" -> new int[]{14, 3};
            case "nether_fossil" -> new int[]{80, 20};
            case "stronghold" -> new int[]{STRONGHOLD_SPACING, STRONGHOLD_SEPARATION};
            case "shipwreck" -> new int[]{20, 5};
            case "ocean_ruin" -> new int[]{20, 8};
            case "ruined_portal" -> new int[]{25, 8};
            default -> null;
        };
    }
}
