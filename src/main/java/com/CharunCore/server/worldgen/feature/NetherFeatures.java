package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

/**
 * 下界地表特征（原版下半部分 configured features 的近似移植）：
 * 全群系：萤石簇（GlowstoneFeature 逐行）/ 岩浆块带（近熔岩海）/ 蘑菇斑 / 火焰斑（灵魂沙上为灵魂火）
 * 绯红森林：巨型绯红菌 + 绯红菌/绯红根草皮
 * 诡异森林：巨型诡异菌 + 诡异菌/诡异根草皮
 * 玄武岩三角洲：玄武岩柱
 */
public final class NetherFeatures {

    private NetherFeatures() {}

    private static final int GLOWSTONE = BlockStateHelper.getDefault("glowstone");
    private static final int NETHERRACK = BlockStateHelper.getDefault("netherrack");
    private static final int BASALT = BlockStateHelper.getDefault("basalt");
    private static final int BLACKSTONE = BlockStateHelper.getDefault("blackstone");
    private static final int SOUL_SAND = BlockStateHelper.getDefault("soul_sand");
    private static final int SOUL_SOIL = BlockStateHelper.getDefault("soul_soil");
    private static final int FIRE = BlockStateHelper.getDefault("fire");
    private static final int SOUL_FIRE = BlockStateHelper.getDefault("soul_fire");
    private static final int BROWN_MUSHROOM = BlockStateHelper.getDefault("brown_mushroom");
    private static final int RED_MUSHROOM = BlockStateHelper.getDefault("red_mushroom");
    private static final int MAGMA = BlockStateHelper.getDefault("magma_block");
    private static final int LAVA = BlockStateHelper.getDefault("lava");
    private static final int CRIMSON_STEM = BlockStateHelper.withProp(BlockStateHelper.getDefault("crimson_stem"), "axis", "y");
    private static final int WARPED_STEM = BlockStateHelper.withProp(BlockStateHelper.getDefault("warped_stem"), "axis", "y");
    private static final int NETHER_WART = BlockStateHelper.getDefault("nether_wart_block");
    private static final int WARPED_WART = BlockStateHelper.getDefault("warped_wart_block");
    private static final int SHROOMLIGHT = BlockStateHelper.getDefault("shroomlight");
    private static final int CRIMSON_NYLIUM = BlockStateHelper.getDefault("crimson_nylium");
    private static final int WARPED_NYLIUM = BlockStateHelper.getDefault("warped_nylium");
    private static final int CRIMSON_FUNGUS = BlockStateHelper.getDefault("crimson_fungus");
    private static final int WARPED_FUNGUS = BlockStateHelper.getDefault("warped_fungus");
    private static final int CRIMSON_ROOTS = BlockStateHelper.getDefault("crimson_roots");
    private static final int WARPED_ROOTS = BlockStateHelper.getDefault("warped_roots");
    private static final int WEEPING_VINES = BlockStateHelper.getDefault("weeping_vines");

    private static final int CRIMSON_FOREST = 7;
    private static final int WARPED_FOREST = 59;
    private static final int BASALT_DELTAS = 2;
    private static final int SOUL_SAND_VALLEY = 49;

    public static void generate(WorldGenLevel level, Chunk chunk, int chunkX, int chunkZ,
                                int[] colBiome, RandomSource rng) {
        if (GLOWSTONE <= 0 || NETHERRACK <= 0) return;
        int baseX = chunkX << 4, baseZ = chunkZ << 4;

        // 1) 萤石簇：原版 GlowstoneFeature —— 垂在下界岩/玄武岩/黑石天花板下。
        for (int i = 0; i < 12; i++) {
            int bx = baseX + rng.nextInt(16), bz = baseZ + rng.nextInt(16);
            int by = 4 + rng.nextInt(113); // 4..116
            placeGlowstoneBlob(level, bx, by, bz, rng);
        }

        // 2) 岩浆块带：y 24..40，把贴着空气/熔岩的下界岩/玄武岩/黑石换成岩浆块。
        for (int i = 0; i < 4; i++) {
            int bx = baseX + rng.nextInt(16), bz = baseZ + rng.nextInt(16);
            int by = 24 + rng.nextInt(17);
            placeMagmaPatch(level, bx, by, bz, rng);
        }

        // 3) 蘑菇斑
        for (int i = 0; i < 3; i++) {
            int bx = baseX + rng.nextInt(16), bz = baseZ + rng.nextInt(16);
            placeMushroomPatch(level, bx, bz, rng);
        }

        // 4) 火焰斑（灵魂沙/灵魂土上为灵魂火）
        if (rng.nextInt(2) == 0) {
            int bx = baseX + rng.nextInt(16), bz = baseZ + rng.nextInt(16);
            placeFirePatch(level, bx, bz, rng);
        }

        // 5) 森林群系：巨型菌 + 小型菌/根草皮
        int cxBiome = colBiome != null ? colBiome[8 * 16 + 8] : -1;
        boolean crimson = cxBiome == CRIMSON_FOREST, warped = cxBiome == WARPED_FOREST;
        if (crimson || warped) {
            for (int i = 0; i < 7; i++) {
                int lx = rng.nextInt(16), lz = rng.nextInt(16);
                if (colBiome == null || colBiome[lz * 16 + lx] != cxBiome) continue;
                int bx = baseX + lx, bz = baseZ + lz;
                placeHugeFungus(level, bx, bz, crimson, rng);
            }
            for (int i = 0; i < 16; i++) {
                int lx = rng.nextInt(16), lz = rng.nextInt(16);
                if (colBiome != null && colBiome[lz * 16 + lx] != cxBiome) continue;
                int bx = baseX + lx, bz = baseZ + lz;
                placeSmallVegetation(level, bx, bz, crimson, rng);
            }
        }

        // 6) 玄武岩三角洲：玄武岩柱
        if (cxBiome == BASALT_DELTAS) {
            for (int i = 0; i < 8; i++) {
                int lx = rng.nextInt(16), lz = rng.nextInt(16);
                if (colBiome != null && colBiome[lz * 16 + lx] != cxBiome) continue;
                placeBasaltPillar(level, baseX + lx, baseZ + lz, rng);
            }
        }
    }

    /** 原版 GlowstoneFeature.place 逐行移植。 */
    private static void placeGlowstoneBlob(WorldGenLevel level, int ox, int oy, int oz, RandomSource rng) {
        if (level.getBlock(ox, oy, oz) != 0) return;
        int above = level.getBlock(ox, oy + 1, oz);
        if (above != NETHERRACK && above != BASALT && above != BLACKSTONE) return;
        if (!level.setBlock(ox, oy, oz, GLOWSTONE)) return;
        for (int i = 0; i < 1500; i++) {
            int px = ox + rng.nextInt(8) - rng.nextInt(8);
            int py = oy - rng.nextInt(12);
            int pz = oz + rng.nextInt(8) - rng.nextInt(8);
            if (level.getBlock(px, py, pz) != 0) continue;
            if (py <= level.getMinY() || py >= level.getMinY() + level.getHeight() - 1) continue;
            int n = 0;
            for (int[] d : DIRS) {
                if (level.getBlock(px + d[0], py + d[1], pz + d[2]) == GLOWSTONE) n++;
                if (n > 1) break;
            }
            if (n != 1) continue;
            level.setBlock(px, py, pz, GLOWSTONE);
        }
    }

    private static final int[][] DIRS = {{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};

    private static void placeMagmaPatch(WorldGenLevel level, int ox, int oy, int oz, RandomSource rng) {
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                for (int dy = -2; dy <= 2; dy++) {
                    double dist = dx * dx + dz * dz + dy * dy * 2.0;
                    if (dist > 21.0 - rng.nextInt(6)) continue;
                    int x = ox + dx, y = oy + dy, z = oz + dz;
                    int b = level.getBlock(x, y, z);
                    if (b != NETHERRACK && b != BASALT && b != BLACKSTONE) continue;
                    boolean exposed = false;
                    for (int[] d : DIRS) {
                        int nb = level.getBlock(x + d[0], y + d[1], z + d[2]);
                        if (nb == 0 || nb == LAVA) { exposed = true; break; }
                    }
                    if (exposed) level.setBlock(x, y, z, MAGMA);
                }
            }
        }
    }

    /** 蘑菇斑：落在空位且下方为可腐朽地面（下界岩/菌岩系/灵魂沙土/玄武岩/黑石）。 */
    private static void placeMushroomPatch(WorldGenLevel level, int bx, int bz, RandomSource rng) {
        int length = 3 + rng.nextInt(6);
        for (int i = 0; i < length; i++) {
            int x = bx + rng.nextInt(9) - 4, z = bz + rng.nextInt(9) - 4;
            for (int y = 4 + rng.nextInt(113); y > 6; y--) {
                int below = level.getBlock(x, y - 1, z);
                if (!canGrowVeg(below)) continue;
                if (level.getBlock(x, y, z) != 0) continue;
                level.setBlock(x, y, z, rng.nextBoolean() ? BROWN_MUSHROOM : RED_MUSHROOM);
                break;
            }
        }
    }

    private static void placeFirePatch(WorldGenLevel level, int bx, int bz, RandomSource rng) {
        int length = 2 + rng.nextInt(4);
        for (int i = 0; i < length; i++) {
            int x = bx + rng.nextInt(9) - 4, z = bz + rng.nextInt(9) - 4;
            for (int y = 4 + rng.nextInt(113); y > 6; y--) {
                int below = level.getBlock(x, y - 1, z);
                if (below != NETHERRACK && below != BASALT && below != BLACKSTONE
                    && below != SOUL_SAND && below != SOUL_SOIL) continue;
                if (level.getBlock(x, y, z) != 0) continue;
                boolean soul = below == SOUL_SAND || below == SOUL_SOIL;
                int st = soul ? SOUL_FIRE : FIRE;
                if (st > 0) level.setBlock(x, y, z, st);
                break;
            }
        }
    }

    private static boolean canGrowVeg(int below) {
        return below == NETHERRACK || below == CRIMSON_NYLIUM || below == WARPED_NYLIUM
            || below == SOUL_SOIL || below == BASALT || below == BLACKSTONE || below == MAGMA;
    }

    /** 简化版 HugeFungusFeature：菌柄 1 柱 4..11 高 + 两层菌盖 + 内部菌光体 + 垂藤。 */
    private static void placeHugeFungus(WorldGenLevel level, int x, int z, boolean crimson, RandomSource rng) {
        int ground = findFloor(level, x, z);
        if (ground < 0) return;
        int below = level.getBlock(x, ground, z);
        if (below != (crimson ? CRIMSON_NYLIUM : WARPED_NYLIUM)) return;
        if (level.getBlock(x, ground + 1, z) != 0) return;

        int stem = crimson ? CRIMSON_STEM : WARPED_STEM;
        int wart = crimson ? NETHER_WART : WARPED_WART;
        int height = 4 + rng.nextInt(8);      // 4..11
        int capY = ground + height;

        // 菌柄
        for (int y = 1; y <= height + 1; y++) {
            level.setBlock(x, ground + y, z, stem);
        }

        // 中层菌盖 5x5 减去部分角（带少量菌光体）
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                boolean corner = Math.abs(dx) == 2 && Math.abs(dz) == 2;
                if (corner && rng.nextInt(3) > 0) continue;
                int id = (rng.nextInt(12) == 0 && !corner) ? SHROOMLIGHT : wart;
                level.setBlock(x + dx, capY, z + dz, id);
            }
        }
        // 下层菌盖 3x3（去角）
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (Math.abs(dx) == 1 && Math.abs(dz) == 1) continue;
                level.setBlock(x + dx, capY - 1, z + dz, wart);
            }
        }
        // 顶部小帽 3x3（高 2 层，越顶越稀）
        for (int dy = 1; dy <= 2; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dy == 2 && rng.nextInt(3) == 0) continue;
                    if (Math.abs(dx) == 1 && Math.abs(dz) == 1 && dy == 1) continue;
                    level.setBlock(x + dx, capY + dy, z + dz, wart);
                }
            }
        }
        // 垂藤（绯红菌才有 weeping_vines 下垂；诡异菌为 twisting_vines 上攀，简化跳过）
        if (crimson && WEEPING_VINES > 0) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (Math.abs(dx) != 2 && Math.abs(dz) != 2) continue;
                    if (rng.nextInt(3) != 0) continue;
                    int len = 1 + rng.nextInt(4);
                    for (int yy = capY - 1; yy > capY - 1 - len; yy--) {
                        if (level.getBlock(x + dx, yy, z + dz) == 0)
                            level.setBlock(x + dx, yy, z + dz, WEEPING_VINES);
                        else break;
                    }
                }
            }
        }
    }

    private static void placeSmallVegetation(WorldGenLevel level, int x, int z, boolean crimson, RandomSource rng) {
        int ground = findFloor(level, x, z);
        if (ground < 0) return;
        int below = level.getBlock(x, ground, z);
        if (below != (crimson ? CRIMSON_NYLIUM : WARPED_NYLIUM)) return;
        if (level.getBlock(x, ground + 1, z) != 0) return;
        int plant;
        int r = rng.nextInt(10);
        if (r < 3) plant = crimson ? CRIMSON_FUNGUS : WARPED_FUNGUS;
        else plant = crimson ? CRIMSON_ROOTS : WARPED_ROOTS;
        if (plant > 0) level.setBlock(x, ground + 1, z, plant);
    }

    /** 自上而下找该列这段高度内的地板面（首个下方非空且非流体的位置）。 */
    private static int findFloor(WorldGenLevel level, int x, int z) {
        int top = Math.min(level.getMinY() + level.getHeight() - 2, 117);
        for (int y = top; y >= level.getMinY() + 3; y--) {
            int b = level.getBlock(x, y, z);
            if (b == 0 || b == LAVA) continue;
            if (level.getBlock(x, y + 1, z) == 0) return y;
        }
        return -1;
    }

    /** 玄武岩三角洲的玄武岩柱：地板面抬升 5..18 高、半径 1..2、顶部随机磨耗。 */
    private static void placeBasaltPillar(WorldGenLevel level, int x, int z, RandomSource rng) {
        int ground = findFloor(level, x, z);
        if (ground < 0) return;
        int below = level.getBlock(x, ground, z);
        if (!canGrowVeg(below) && below != NETHERRACK) return;
        int h = 5 + rng.nextInt(14);
        int r = 1 + rng.nextInt(2);
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                if (Math.abs(dx) == r && Math.abs(dz) == r) continue;
                int trim = rng.nextInt(3);
                for (int dy = 1; dy <= h - trim; dy++) {
                    int b = level.getBlock(x + dx, ground + dy, z + dz);
                    if (b == 0) level.setBlock(x + dx, ground + dy, z + dz, BASALT); else break;
                }
            }
        }
    }
}
