package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

/**
 * 废弃矿井 (原版 SmallMineshaftPieces)。
 * 原版结构由 1 条主通道(可带侧支/交叉)组成:
 *  - 地板: 橡木原木横梁 / 橡木木板 / 圆石
 *  - 天花板: 橡木木板 + 圆石支撑柱
 *  - 通道每 3 格在两侧有木栅栏支柱, 净空超过 4 格时中间增设原木支撑柱
 *  - 铁轨 1/3 概率, 含动力铁轨/探测铁轨点缀
 *  - 蛛网团、箱子、刷怪笼
 * 注: 本实现按区块内生成(原版是跨区块的多段 piece), 尽力接近原版外观。
 */
public class MineshaftGenerator {

    private static final int OAK_PLANKS = BlockStateHelper.getDefault("oak_planks");
    private static final int OAK_FENCE = BlockStateHelper.getDefault("oak_fence");
    private static final int COBBLESTONE = BlockStateHelper.getDefault("cobblestone");
    private static final int RAIL = BlockStateHelper.getDefault("rail");
    private static final int POWERED_RAIL = BlockStateHelper.getDefault("powered_rail");
    private static final int DETECTOR_RAIL = BlockStateHelper.getDefault("detector_rail");
    private static final int CHEST = BlockStateHelper.getDefault("chest");
    private static final int SPAWNER = BlockStateHelper.getDefault("spawner");
    private static final int OAK_LOG = BlockStateHelper.getDefault("oak_log");
    private static final int TORCH = BlockStateHelper.getDefault("torch");
    private static final int COBWEB = BlockStateHelper.getDefault("cobweb");
    private static final int OAK_STAIRS = BlockStateHelper.getDefault("oak_stairs");
    private static final int OAK_SLAB = BlockStateHelper.getDefault("oak_slab");

    /** 矿井生成总数(较大值控制相同 chunk 内的密度)。 */
    private static final int MAX_PIECES = 6;

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, int y, RandomSource random) {
        // 主通道沿 X
        int[][] mainBranch = placeCorridor(chunk, localX, localZ, y, random, true, 16, localX + 8);
        // 沿 Z 支通道
        if (Math.abs(mainBranch[1][0] - mainBranch[0][0]) > 3) {
            if (mainBranch[0][0] > 2) placeCorridor(chunk, 2, mainBranch[0][1], y, random, false, 12, 8);
        }
        // 交叉/侧支
        if (random.nextInt(3) == 0) {
            int crossX = mainBranch[2][0];
            int crossZ = mainBranch[2][1];
            if (crossX > 2 && crossX < 13) {
                placeCrossing(chunk, crossX, crossZ, y, random);
            }
        }
        if (random.nextInt(5) == 0) {
            int offZ = mainBranch[2][1] + (random.nextBoolean() ? 2 : -2);
            if (offZ >= 0 && offZ <= 15 && mainBranch[2][0] > 2 && mainBranch[2][0] < 13) {
                placeCorridor(chunk, mainBranch[2][0], offZ, y, random, false, 8, mainBranch[2][0]);
            }
        }
        // 刷怪笼房间(罕见)
        if (random.nextInt(40) == 0) {
            int spawnerX = mainBranch[2][0];
            int spawnerZ = mainBranch[2][1];
            placeSpawnerRoom(chunk, spawnerX, spawnerZ, y, random);
        }
    }

    /** 返回值: [start(x,z), end(x,z), mid(x,z)] */
    private static int[][] placeCorridor(Chunk chunk, int cx, int cz, int y, RandomSource random,
                                         boolean alongX, int length, int midX) {
        boolean hasRails = random.nextInt(3) == 0;
        int startX = cx, endX = cx;
        int startZ = cz, endZ = cz;
        int supportCount = 0;
        for (int i = 0; i < length; i++) {
            int wx = alongX ? cx + i : cx;
            int wz = alongX ? cz : cz + i;
            if (wx < 0 || wx > 15 || wz < 0 || wz > 15) { endX = wx; endZ = wz; break; }
            endX = wx; endZ = wz;

            for (int dx = -1; dx <= 1; dx++) {
                int lx = wx + (alongX ? dx : 0);
                int lz = wz + (alongX ? 0 : dx);
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                // 地板: 交替 圆石 与 橡木板, 边缘用原木垫脚
                if (dx == 0) {
                    chunk.setBlock(lx, y, lz, random.nextInt(4) == 0 ? COBBLESTONE : OAK_PLANKS);
                } else {
                    chunk.setBlock(lx, y, lz, random.nextInt(5) == 0 ? OAK_LOG : OAK_PLANKS);
                }
                // 内腔
                chunk.setBlock(lx, y + 1, lz, 0);
                chunk.setBlock(lx, y + 2, lz, 0);
                // 天花板
                chunk.setBlock(lx, y + 3, lz, OAK_PLANKS);
                if (dx == 0 && random.nextInt(6) == 0) chunk.setBlock(lx, y + 4, lz, OAK_PLANKS);
            }
            // 支撑柱: 每 3 格放木栅栏/原木柱(原版)
            supportCount++;
            if (supportCount % 3 == 0) {
                int px = wx - (alongX ? 1 : 0);
                int pz = wz - (alongX ? 0 : 1);
                if (px >= 0 && px <= 15 && pz >= 0 && pz <= 15) {
                    chunk.setBlock(px, y + 1, pz, OAK_FENCE);
                    chunk.setBlock(px, y + 2, pz, OAK_FENCE);
                }
            }
            // 铁轨
            if (hasRails) {
                int railState = RAIL;
                if (random.nextInt(12) == 0) railState = POWERED_RAIL;
                else if (random.nextInt(8) == 0) railState = DETECTOR_RAIL;
                chunk.setBlock(wx, y + 1, wz, railState);
            }
            // 蛛网
            if (random.nextInt(9) == 0) {
                int off = (alongX ? 1 : 1) * (random.nextBoolean() ? 1 : -1);
                int cix = wx + (alongX ? 0 : off);
                int ciz = wz + (alongX ? off : 0);
                if (cix >= 0 && cix <= 15 && ciz >= 0 && ciz <= 15) {
                    if (random.nextInt(3) == 0) chunk.setBlock(cix, y + 2, ciz, COBWEB);
                    else chunk.setBlock(cix, y + 1, ciz, COBWEB);
                }
            }
            // 火把(1/12)
            if (random.nextInt(12) == 0) {
                int tx = wx - (alongX ? 1 : 0);
                int tz = wz - (alongX ? 0 : 1);
                if (tx >= 0 && tx <= 15 && tz >= 0 && tz <= 15) {
                    chunk.setBlock(tx, y + 2, tz, TORCH);
                }
            }
            // 箱子(2%)
            if (random.nextInt(50) == 0) {
                int chX = wx + (alongX ? 1 : 0);
                int chZ = wz + (alongX ? 0 : 1);
                if (chX >= 0 && chX <= 15 && chZ >= 0 && chZ <= 15) {
                    if (chunk.getBlock(chX, y + 1, chZ) == 0) chunk.setBlock(chX, y + 1, chZ, CHEST);
                }
            }
        }
        return new int[][]{ {startX, startZ}, {endX, endZ}, { midX, (startZ + endZ) / 2 } };
    }

    private static void placeCrossing(Chunk chunk, int cx, int cz, int y, RandomSource random) {
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                int lx = cx + x, lz = cz + z;
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                chunk.setBlock(lx, y, lz, OAK_PLANKS);
                chunk.setBlock(lx, y + 3, lz, OAK_PLANKS);
                chunk.setBlock(lx, y + 1, lz, 0);
                chunk.setBlock(lx, y + 2, lz, 0);
                if (random.nextInt(30) == 0) chunk.setBlock(lx, y + 4, lz, OAK_PLANKS);
            }
        }
        // 中央十字垫高/四角柱
        int[][] pillars = {{-3, -3}, {-3, 3}, {3, -3}, {3, 3}};
        for (int[] p : pillars) {
            int lx = cx + p[0], lz = cz + p[1];
            if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
            chunk.setBlock(lx, y + 1, lz, OAK_LOG);
            chunk.setBlock(lx, y + 2, lz, OAK_LOG);
        }
        // 十字中心与原版一样是空(交叉口)
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int lx = cx + i, lz = cz + j;
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                chunk.setBlock(lx, y + 1, lz, 0);
                chunk.setBlock(lx, y + 2, lz, 0);
            }
        }
        if (random.nextInt(2) == 0) {
            int chestX = cx + (random.nextBoolean() ? 2 : -2);
            int chestZ = cz + (random.nextBoolean() ? 2 : -2);
            if (chestX >= 0 && chestX <= 15 && chestZ >= 0 && chestZ <= 15) {
                chunk.setBlock(chestX, y + 1, chestZ, CHEST);
            }
        }
    }

    private static void placeSpawnerRoom(Chunk chunk, int cx, int cz, int y, RandomSource random) {
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                int lx = cx + x, lz = cz + z;
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                if (Math.abs(x) == 4 || Math.abs(z) == 4) {
                    chunk.setBlock(lx, y, lz, COBBLESTONE);
                    chunk.setBlock(lx, y + 3, lz, COBBLESTONE);
                } else {
                    chunk.setBlock(lx, y, lz, OAK_PLANKS);
                    chunk.setBlock(lx, y + 3, lz, OAK_PLANKS);
                    chunk.setBlock(lx, y + 1, lz, 0);
                    chunk.setBlock(lx, y + 2, lz, 0);
                }
            }
        }
        chunk.setBlock(cx, y + 1, cz, SPAWNER);
        for (int i = 0; i < 6; i++) {
            int wix = cx + random.nextInt(7) - 3;
            int wiz = cz + random.nextInt(7) - 3;
            if (wix >= 0 && wix <= 15 && wiz >= 0 && wiz <= 15) chunk.setBlock(wix, y + 1, wiz, COBWEB);
        }
    }
}