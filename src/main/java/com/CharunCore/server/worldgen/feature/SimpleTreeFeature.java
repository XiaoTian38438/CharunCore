package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

public class SimpleTreeFeature {

    private static final int OAK_LOG = BlockStateHelper.getDefault("oak_log");
    private static final int OAK_LEAVES = BlockStateHelper.getDefault("oak_leaves");
    private static final int BIRCH_LOG = BlockStateHelper.getDefault("birch_log");
    private static final int BIRCH_LEAVES = BlockStateHelper.getDefault("birch_leaves");
    private static final int SPRUCE_LOG = BlockStateHelper.getDefault("spruce_log");
    private static final int SPRUCE_LEAVES = BlockStateHelper.getDefault("spruce_leaves");
    private static final int DARK_OAK_LOG = BlockStateHelper.getDefault("dark_oak_log");
    private static final int DARK_OAK_LEAVES = BlockStateHelper.getDefault("dark_oak_leaves");
    private static final int JUNGLE_LOG = BlockStateHelper.getDefault("jungle_log");
    private static final int JUNGLE_LEAVES = BlockStateHelper.getDefault("jungle_leaves");
    private static final int ACACIA_LOG = BlockStateHelper.getDefault("acacia_log");
    private static final int ACACIA_LEAVES = BlockStateHelper.getDefault("acacia_leaves");
    private static final int CHERRY_LOG = BlockStateHelper.getDefault("cherry_log");
    private static final int CHERRY_LEAVES = BlockStateHelper.getDefault("cherry_leaves");
    private static final int DIRT = BlockStateHelper.getDefault("dirt");
    private static final int VINE = BlockStateHelper.getDefault("vine");
    private static final int GRASS_BLOCK = BlockStateHelper.getDefault("grass_block");
    private static final int MOSS_CARPET = BlockStateHelper.getDefault("moss_carpet");

    public enum TreeType {
        OAK, BIRCH, SPRUCE, DARK_OAK, JUNGLE, ACACIA, CHERRY, SWAMP_OAK
    }

    public static boolean placeTreeChunk(Chunk chunk, int x, int y, int z, TreeType type, RandomSource random, int chunkX, int chunkZ) {
        int lx = x - chunkX * 16;
        int lz = z - chunkZ * 16;
        if (lx < 0 || lx >= 16 || lz < 0 || lz >= 16) return false;

        WorldGenLevel singleChunkLevel = new SingleChunkLevel(chunk, chunkX, chunkZ);
        return placeTree(singleChunkLevel, x, y, z, type, random);
    }

    /** 树干行进受阻判定(B3): 树叶与实心地形都算阻挡。调用处区分 —— 实心地形或过矮撞叶则
     *  放弃种树; 已长够 4 格后撞邻树叶冠则提前停高(树冠照常生成), 消除"原木穿出邻树冠"的突出。 */
    private static boolean trunkBlocked(WorldGenLevel level, int x, int y, int z) {
        return level.getBlock(x, y, z) != 0;
    }

    /** 树冠格放置判定: 空气或已有树叶(相邻树冠重叠时覆盖, 避免互斥导致单侧光杆)。 */
    private static boolean canPlaceLeaf(WorldGenLevel level, int x, int y, int z) {
        int s = level.getBlock(x, y, z);
        if (s == 0) return true;
        String n = BlockStateHelper.getName(s);
        return n != null && n.contains("leaves");
    }

    public static boolean placeTree(WorldGenLevel level, int x, int y, int z, TreeType type, RandomSource random) {
        if (type == TreeType.SPRUCE) return placeSpruce(level, x, y, z, random);
        if (type == TreeType.DARK_OAK) return placeDarkOak(level, x, y, z, random);
        if (type == TreeType.JUNGLE) return placeJungle(level, x, y, z, random);
        if (type == TreeType.ACACIA) return placeAcacia(level, x, y, z, random);
        if (type == TreeType.CHERRY) return placeCherry(level, x, y, z, random);
        if (type == TreeType.SWAMP_OAK) return placeSwampOak(level, x, y, z, random);
        int height = 4 + random.nextInt(3);
        if (type == TreeType.OAK && random.nextInt(10) == 0) height += random.nextInt(4) + 2;
        int logId = type == TreeType.BIRCH ? BIRCH_LOG : OAK_LOG;
        int leafId = type == TreeType.BIRCH ? BIRCH_LEAVES : OAK_LEAVES;
        return placeTreeTrunkAndBlob(level, x, y, z, height, logId, leafId, random, type == TreeType.BIRCH);
    }

    private static boolean placeTreeTrunkAndBlob(WorldGenLevel level, int x, int y, int z, int height, int logId, int leafId, RandomSource random, boolean isBirch) {
        if (level.getBlock(x, y, z) != 0) return false;

        // B4: 记录实际树干格数 —— break 停高后 topY 必须跟随实际高度,
        // 曾用完整 height -> 叶环悬在断干上方, 树干顶端裸露成"光杆"。
        int trunkTop = height;
        for (int dy = 0; dy < height; dy++) {
            int by = y + dy;
            if (dy > 0 && trunkBlocked(level, x, by, z)) {
                // B3: 已长够 4 格后撞上邻树叶冠 -> 提前停高(树冠照常), 不再穿出造成"突出原木"
                int bs = level.getBlock(x, by, z);
                String bn = bs == 0 ? null : BlockStateHelper.getName(bs);
                if (dy >= 4 && bn != null && bn.contains("leaves")) { trunkTop = dy; break; }
                return false;
            }
            level.setBlock(x, by, z, logId);
        }

        int topY = y + trunkTop;
        int leafStart = topY - 2;

        for (int ly = leafStart; ly <= topY + 1; ly++) {
            int layer = ly - leafStart;
            int radius = layer <= 1 ? 2 : (layer == 2 ? 1 : 0);
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int bx = x + dx;
                    int bz = z + dz;
                    int by = ly;
                    if (!canPlaceLeaf(level, bx, by, bz)) continue;
                    int dist = Math.abs(dx) + Math.abs(dz);
                    if (layer == 0) {
                        if (dist > 3) continue;
                    } else if (layer == 1) {
                        if (dist > 3) continue;
                    } else if (layer == 2) {
                        if (dist > 2) continue;
                    } else {
                        if (dist > 0) continue;
                    }
                    if (dist == radius && radius > 0 && random.nextInt(3) == 0) continue;
                    level.setBlock(bx, by, bz, leafId);
                }
            }
        }

        if (!isBirch && trunkTop >= 5 && random.nextInt(3) == 0) {
            int branchY = y + trunkTop / 2 + random.nextInt(2);
            int[][] dirs = {{1,0,1},{-1,0,-1},{0,1,1},{0,-1,-1},{1,1,0},{-1,-1,0}};
            int[] dir = dirs[random.nextInt(dirs.length)];
            int blen = 1 + random.nextInt(2);
            for (int b = 1; b <= blen; b++) {
                int bx = x + dir[0] * b;
                int bz = z + dir[1] * b;
                int by = branchY + dir[2] * b;
                if (level.getBlock(bx, by, bz) == 0) {
                    level.setBlock(bx, by, bz, logId);
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            if (level.getBlock(bx + dx, by + 1, bz + dz) == 0 && Math.abs(dx) + Math.abs(dz) <= 1) {
                                level.setBlock(bx + dx, by + 1, bz + dz, leafId);
                            }
                        }
                    }
                }
            }
        }

        int dirtY = y - 1;
        int existing = level.getBlock(x, dirtY, z);
        if (existing == GRASS_BLOCK || existing == 0) {
            level.setBlock(x, dirtY, z, DIRT);
        }
        return true;
    }

    private static boolean placeSpruce(WorldGenLevel level, int x, int y, int z, RandomSource random) {
        // 原版云杉两种形态: pine(高细塔形, 老生长针叶林/云杉林 50%) 与普通 spruce(矮胖)
        boolean pine = random.nextInt(2) == 0;
        int height = pine ? 8 + random.nextInt(5) : 6 + random.nextInt(4);
        if (level.getBlock(x, y, z) != 0) return false;

        // B4: 记录实际树干格数, break 停高后叶环贴着真实顶端(曾用完整 height -> 光杆)。
        int trunkTop = height;
        for (int dy = 0; dy < height; dy++) {
            int by = y + dy;
            if (dy > 0 && trunkBlocked(level, x, by, z)) {
                // B3: 已长够 4 格后撞上邻树叶冠 -> 提前停高(树冠照常), 不再穿出造成"突出原木"
                int bs = level.getBlock(x, by, z);
                String bn = bs == 0 ? null : BlockStateHelper.getName(bs);
                if (dy >= 4 && bn != null && bn.contains("leaves")) { trunkTop = dy; break; }
                return false;
            }
            level.setBlock(x, by, z, SPRUCE_LOG);
        }

        if (pine) {
            // pine: 自顶向下每 2 层一个叶环, 半径 0(尖) -> 1 -> 1 -> 2 -> 2 ...
            int topY = y + trunkTop - 1;
            for (int ly = topY; ly >= y + 2; ly--) {
                int fromTop = topY - ly;
                if (fromTop > 0 && fromTop % 2 != 0) continue; // 叶环间隔一层
                int radius = fromTop == 0 ? 0 : Math.min(2, 1 + fromTop / 4);
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        int d = Math.abs(dx) + Math.abs(dz);
                        if (d > radius) continue;
                        int bx = x + dx;
                        int bz = z + dz;
                        if (!canPlaceLeaf(level, bx, ly, bz)) continue;
                        level.setBlock(bx, ly, bz, SPRUCE_LEAVES);
                    }
                }
            }
        } else {
            int leafBase = y + trunkTop - 2;
            for (int layer = 0; layer < 4; layer++) {
                int ly = leafBase + layer;
                int radius = 2 - (layer / 2);
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        if (dx == 0 && dz == 0 && layer < 3) continue;
                        int bx = x + dx;
                        int bz = z + dz;
                        if (!canPlaceLeaf(level, bx, ly, bz)) continue;
                        int d = Math.abs(dx) + Math.abs(dz);
                        if (d > radius) continue;
                        level.setBlock(bx, ly, bz, SPRUCE_LEAVES);
                    }
                }
            }
        }

        int dirtY = y - 1;
        int existing = level.getBlock(x, dirtY, z);
        if (existing == BlockStateHelper.getDefault("grass_block") || existing == 0) {
            level.setBlock(x, dirtY, z, DIRT);
        }
        return true;
    }

    private static boolean placeDarkOak(WorldGenLevel level, int x, int y, int z, RandomSource random) {
        int height = 6 + random.nextInt(3);
        if (level.getBlock(x, y, z) != 0) return false;

        for (int dy = 0; dy < height; dy++) {
            int by = y + dy;
            for (int dx = 0; dx <= 1; dx++) {
                for (int dz = 0; dz <= 1; dz++) {
                    int bx = x + dx;
                    int bz = z + dz;
                    if (level.getBlock(bx, by, bz) != 0 && dy > 0) continue;
                    level.setBlock(bx, by, bz, DARK_OAK_LOG);
                }
            }
        }

        int topY = y + height;
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int bx = x + dx;
                    int bz = z + dz;
                    int by = topY + dy;
                    if (level.getBlock(bx, by, bz) != 0) continue;
                    int d = Math.abs(dx) + Math.abs(dz) + (dy == -1 ? 2 : dy == 1 ? 1 : 0);
                    if (d > 4) continue;
                    level.setBlock(bx, by, bz, DARK_OAK_LEAVES);
                }
            }
        }

        for (int dx = 0; dx <= 1; dx++) {
            for (int dz = 0; dz <= 1; dz++) {
                int bx = x + dx;
                int bz = z + dz;
                int existing = level.getBlock(bx, y - 1, bz);
                if (existing == BlockStateHelper.getDefault("grass_block") || existing == 0) {
                    level.setBlock(bx, y - 1, bz, DIRT);
                }
            }
        }
        return true;
    }

    public static TreeType biomeTreeType(int biome) {
        return switch (biome) {
            case B_BIRCH_FOREST, B_OLD_GROWTH_BIRCH_FOREST -> TreeType.BIRCH;
            case B_TAIGA, B_OLD_GROWTH_SPRUCE_TAIGA, B_OLD_GROWTH_PINE_TAIGA,
                 B_SNOWY_TAIGA -> TreeType.SPRUCE;
            case B_DARK_FOREST -> TreeType.DARK_OAK;
            case B_FOREST, B_FLOWER_FOREST, B_PLAINS, B_MEADOW,
                 B_SUNFLOWER_PLAINS, B_RIVER -> TreeType.OAK;
            case B_JUNGLE, B_BAMBOO_JUNGLE, B_SPARSE_JUNGLE -> TreeType.JUNGLE;
            case B_SAVANNA, B_SAVANNA_PLATEAU, B_WINDSWEPT_SAVANNA -> TreeType.ACACIA;
            case B_SWAMP -> TreeType.SWAMP_OAK;
            case B_WINDSWEPT_FOREST -> TreeType.OAK;
            default -> null;
        };
    }

    private static boolean placeJungle(WorldGenLevel level, int x, int y, int z, RandomSource random) {
        int height = 6 + random.nextInt(5);
        if (level.getBlock(x, y, z) != 0) return false;

        int trunkTop = height;
        for (int dy = 0; dy < height; dy++) {
            int by = y + dy;
            if (dy > 0 && trunkBlocked(level, x, by, z)) {
                // B3: 已长够 4 格后撞上邻树叶冠 -> 提前停高(树冠照常), 不再穿出造成"突出原木"
                int bs = level.getBlock(x, by, z);
                String bn = bs == 0 ? null : BlockStateHelper.getName(bs);
                if (dy >= 4 && bn != null && bn.contains("leaves")) { trunkTop = dy; break; }
                return false;
            }
            level.setBlock(x, by, z, JUNGLE_LOG);
        }

        int topY = y + trunkTop;
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                for (int dy = -2; dy <= 1; dy++) {
                    int bx = x + dx;
                    int bz = z + dz;
                    int by = topY + dy;
                    double dist = Math.sqrt(dx * dx + dz * dz + dy * dy * 0.5);
                    if (dist > 3.0) continue;
                    if (!canPlaceLeaf(level, bx, by, bz)) continue;
                    if (bx == x && bz == z && dy < 0) continue;
                    level.setBlock(bx, by, bz, JUNGLE_LEAVES);
                }
            }
        }

        for (int dy = 1; dy < trunkTop; dy++) {
            int by = y + dy;
            if (random.nextInt(3) == 0) {
                int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
                for (int[] d : dirs) {
                    int nx = x + d[0];
                    int nz = z + d[1];
                    if (level.getBlock(nx, by, nz) == 0) {
                        level.setBlock(nx, by, nz, VINE);
                        for (int v = 1; v <= 3; v++) {
                            int vy = by - v;
                            if (vy < -64) break;
                            if (level.getBlock(nx, vy, nz) != 0) break;
                            level.setBlock(nx, vy, nz, VINE);
                        }
                        break;
                    }
                }
            }
        }

        int dirtY = y - 1;
        int existing = level.getBlock(x, dirtY, z);
        if (existing == BlockStateHelper.getDefault("grass_block") || existing == 0) {
            level.setBlock(x, dirtY, z, DIRT);
        }
        return true;
    }

    private static final int B_BIRCH_FOREST = 4;
    private static final int B_OLD_GROWTH_BIRCH_FOREST = 36;
    private static final int B_TAIGA = 55;
    private static final int B_OLD_GROWTH_SPRUCE_TAIGA = 38;
    private static final int B_OLD_GROWTH_PINE_TAIGA = 37;
    private static final int B_SNOWY_TAIGA = 48;
    private static final int B_DARK_FOREST = 8;
    private static final int B_FOREST = 21;
    private static final int B_FLOWER_FOREST = 20;
    private static final int B_PLAINS = 40;
    private static final int B_MEADOW = 32;
    private static final int B_SUNFLOWER_PLAINS = 53;
    private static final int B_RIVER = 41;
    private static final int B_JUNGLE = 28;
    private static final int B_BAMBOO_JUNGLE = 1;
    private static final int B_SPARSE_JUNGLE = 50;
    private static final int B_SAVANNA = 42;
    private static final int B_SAVANNA_PLATEAU = 43;
    private static final int B_WINDSWEPT_FOREST = 60;
    private static final int B_WINDSWEPT_SAVANNA = 63;
    private static final int B_SWAMP = 54;

    private static boolean placeAcacia(WorldGenLevel level, int x, int y, int z, RandomSource random) {
        int height = 5 + random.nextInt(4);
        if (level.getBlock(x, y, z) != 0) return false;

        int trunkTop = height;
        for (int dy = 0; dy < height; dy++) {
            int by = y + dy;
            if (dy > 0 && trunkBlocked(level, x, by, z)) {
                // B3: 已长够 4 格后撞上邻树叶冠 -> 提前停高(树冠照常), 不再穿出造成"突出原木"
                int bs = level.getBlock(x, by, z);
                String bn = bs == 0 ? null : BlockStateHelper.getName(bs);
                if (dy >= 4 && bn != null && bn.contains("leaves")) { trunkTop = dy; break; }
                return false;
            }
            level.setBlock(x, by, z, ACACIA_LOG);
        }

        int topY = y + trunkTop;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                for (int dy = -1; dy <= 0; dy++) {
                    int bx = x + dx;
                    int bz = z + dz;
                    int by = topY + dy;
                    if (!canPlaceLeaf(level, bx, by, bz)) continue;
                    int d = Math.abs(dx) + Math.abs(dz);
                    if (d > 3) continue;
                    level.setBlock(bx, by, bz, ACACIA_LEAVES);
                }
            }
        }

        int dirtY = y - 1;
        int existing = level.getBlock(x, dirtY, z);
        if (existing == GRASS_BLOCK || existing == 0) {
            level.setBlock(x, dirtY, z, DIRT);
        }
        return true;
    }

    private static boolean placeCherry(WorldGenLevel level, int x, int y, int z, RandomSource random) {
        int height = 4 + random.nextInt(3);
        if (level.getBlock(x, y, z) != 0) return false;

        int trunkTop = height;
        for (int dy = 0; dy < height; dy++) {
            int by = y + dy;
            if (dy > 0 && trunkBlocked(level, x, by, z)) {
                // B3: 已长够 4 格后撞上邻树叶冠 -> 提前停高(树冠照常), 不再穿出造成"突出原木"
                int bs = level.getBlock(x, by, z);
                String bn = bs == 0 ? null : BlockStateHelper.getName(bs);
                if (dy >= 4 && bn != null && bn.contains("leaves")) { trunkTop = dy; break; }
                return false;
            }
            level.setBlock(x, by, z, CHERRY_LOG);
        }

        int topY = y + trunkTop;
        int leafStart = topY - 1;
        for (int ly = leafStart; ly <= topY + 2; ly++) {
            int layer = ly - leafStart;
            int radius = layer <= 1 ? 2 : (layer == 2 ? 1 : 0);
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int bx = x + dx;
                    int bz = z + dz;
                    if (!canPlaceLeaf(level, bx, ly, bz)) continue;
                    int dist = Math.abs(dx) + Math.abs(dz);
                    if (layer == 0 && dist > 3) continue;
                    if (layer == 1 && dist > 3) continue;
                    if (layer == 2 && dist > 2) continue;
                    if (layer == 3 && dist > 0) continue;
                    if (dist == radius && radius > 0 && random.nextInt(4) == 0) continue;
                    level.setBlock(bx, ly, bz, CHERRY_LEAVES);
                }
            }
        }

        int dirtY = y - 1;
        int existing = level.getBlock(x, dirtY, z);
        if (existing == GRASS_BLOCK || existing == 0) {
            level.setBlock(x, dirtY, z, DIRT);
        }
        return true;
    }

    private static boolean placeSwampOak(WorldGenLevel level, int x, int y, int z, RandomSource random) {
        int height = 3 + random.nextInt(3);
        if (level.getBlock(x, y, z) != 0) return false;

        int trunkTop = height;
        for (int dy = 0; dy < height; dy++) {
            int by = y + dy;
            if (dy > 0 && trunkBlocked(level, x, by, z)) {
                // B3: 已长够 4 格后撞上邻树叶冠 -> 提前停高(树冠照常), 不再穿出造成"突出原木"
                int bs = level.getBlock(x, by, z);
                String bn = bs == 0 ? null : BlockStateHelper.getName(bs);
                if (dy >= 4 && bn != null && bn.contains("leaves")) { trunkTop = dy; break; }
                return false;
            }
            level.setBlock(x, by, z, OAK_LOG);
        }

        int topY = y + trunkTop;
        for (int ly = topY - 1; ly <= topY + 1; ly++) {
            int layer = ly - (topY - 1);
            int radius = layer == 0 ? 2 : (layer == 1 ? 2 : 1);
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int bx = x + dx;
                    int bz = z + dz;
                    if (!canPlaceLeaf(level, bx, ly, bz)) continue;
                    int dist = Math.abs(dx) + Math.abs(dz);
                    if (layer < 2 && dist > 3) continue;
                    if (layer == 2 && dist > 1) continue;
                    if (dist == radius && radius > 0 && random.nextInt(3) == 0) continue;
                    level.setBlock(bx, ly, bz, OAK_LEAVES);
                }
            }
        }

        for (int dy = 0; dy <= trunkTop; dy++) {
            int by = y + dy;
            int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
            for (int[] d : dirs) {
                int nx = x + d[0];
                int nz = z + d[1];
                if (level.getBlock(nx, by, nz) == 0) {
                    int vineLen = 1 + random.nextInt(3);
                    for (int v = 0; v < vineLen; v++) {
                        int vy = by - v;
                        if (vy < -64) break;
                        if (level.getBlock(nx, vy, nz) != 0) break;
                        level.setBlock(nx, vy, nz, VINE);
                    }
                }
            }
        }

        int dirtY = y - 1;
        int existing = level.getBlock(x, dirtY, z);
        if (existing == GRASS_BLOCK || existing == 0) {
            level.setBlock(x, dirtY, z, DIRT);
        }
        if (random.nextInt(3) == 0) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (Math.abs(dx) + Math.abs(dz) > 2) continue;
                    int bx = x + dx;
                    int bz = z + dz;
                    if (level.getBlock(bx, y - 1, bz) == GRASS_BLOCK && level.getBlock(bx, y, bz) == 0) {
                        level.setBlock(bx, y, bz, MOSS_CARPET);
                    }
                }
            }
        }
        return true;
    }
}
