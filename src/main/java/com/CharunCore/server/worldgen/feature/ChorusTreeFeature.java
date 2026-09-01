package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

/**
 * 原版 ChorusPlantFeature + ChorusFlowerBlock.generatePlant/growTreeRecursive 逐行移植。
 * 灌木式递归生长：主干每层 1..4 格（根层 +1），每层可向四方分支（深度 <4），
 * 无分支时顶端放置 age=5 成熟紫颂花；植物方块连接态(up/down/nsew)按邻居实时计算。
 * 仅在外岛生成（主岛半径 ~1000 格内跳过，与原版群系分布一致）。
 */
public final class ChorusTreeFeature {

    private static final int MAX_DIST = 8;

    private ChorusTreeFeature() {}

    public static void generate(WorldGenLevel level, int chunkX, int chunkZ, RandomSource rnd) {
        int plant = BlockStateHelper.getDefault("chorus_plant");
        int flower = BlockStateHelper.getDefault("chorus_flower");
        if (plant <= 0 || flower <= 0) return;

        for (int attempt = 0; attempt < 4; attempt++) {
            int bx = chunkX * 16 + rnd.nextInt(16);
            int bz = chunkZ * 16 + rnd.nextInt(16);
            long dx = bx, dz = bz;
            if (dx * dx + dz * dz <= 900L * 900L) continue;

            int top = findTopEndStone(level, bx, bz);
            if (top < 0) continue;
            int y = top + 1;
            if (level.getBlock(bx, y, bz) != 0) continue;
            if (!isEndStone(level, bx, y - 1, bz)) continue;

            generatePlant(level, bx, y, bz, plant, flower, rnd);
        }
    }

    /** 原版 ChorusFlowerBlock.generatePlant(level, pos, random, 8)。 */
    private static void generatePlant(WorldGenLevel level, int x, int y, int z,
                                      int plant, int flower, RandomSource rnd) {
        setPlantWithConnections(level, x, y, z, plant, flower);
        growTreeRecursive(level, x, y, z, x, z, plant, flower, rnd, 0);
    }

    private static void growTreeRecursive(WorldGenLevel level, int px, int py, int pz,
                                          int ox, int oz, int plant, int flower,
                                          RandomSource rnd, int depth) {
        int h = rnd.nextInt(4) + 1;
        if (depth == 0) h++;

        for (int i = 1; i <= h; i++) {
            int ax = px, ay = py + i, az = pz;
            if (!allNeighborsEmpty(level, ax, ay, az, null)) return;
            setPlantWithConnections(level, ax, ay, az, plant, flower);
            setPlantWithConnections(level, ax, ay - 1, az, plant, flower);
        }

        boolean branched = false;
        if (depth < 4) {
            int n = rnd.nextInt(4);
            if (depth == 0) n++;
            for (int i = 0; i < n; i++) {
                int[] dir = HORIZONTAL[rnd.nextInt(4)];
                int bxx = px + dir[0];
                int byy = py + h;
                int bzz = pz + dir[2];
                if (Math.abs(bxx - ox) >= MAX_DIST || Math.abs(bzz - oz) >= MAX_DIST) continue;
                if (level.getBlock(bxx, byy, bzz) != 0) continue;
                if (level.getBlock(bxx, byy - 1, bzz) != 0) continue;
                int[] back = {-dir[0], 0, -dir[2]};
                if (!allNeighborsEmpty(level, bxx, byy, bzz, back)) continue;
                branched = true;
                setPlantWithConnections(level, bxx, byy, bzz, plant, flower);
                setPlantWithConnections(level, bxx + back[0], byy + back[1], bzz + back[2], plant, flower);
                growTreeRecursive(level, bxx, byy, bzz, ox, oz, plant, flower, rnd, depth + 1);
            }
        }
        if (!branched) {
            level.setBlock(px, py + h, pz, BlockStateHelper.withProp(flower, "age", "5"));
        }
    }

    private static final int[][] HORIZONTAL = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};

    /** 原版 ChorusFlowerBlock.allNeighborsEmpty：水平四邻（排除指定方向）必须全空。 */
    private static boolean allNeighborsEmpty(WorldGenLevel level, int x, int y, int z, int[] exclude) {
        for (int[] d : HORIZONTAL) {
            if (exclude != null && d[0] == exclude[0] && d[2] == exclude[2]) continue;
            if (level.getBlock(x + d[0], y, z + d[2]) != 0) return false;
        }
        return true;
    }

    /** 原版 ChorusPlantBlock.getStateWithConnections。 */
    private static void setPlantWithConnections(WorldGenLevel level, int x, int y, int z,
                                                int plant, int flower) {
        if (level.getBlock(x, y, z) != 0 && !isChorusLike(level.getBlock(x, y, z))) return;
        int st = plant;
        st = BlockStateHelper.withProp(st, "down",
            isChorusLike(level.getBlock(x, y - 1, z)) || isEndStone(level, x, y - 1, z) ? "true" : "false");
        st = BlockStateHelper.withProp(st, "up", isChorusLike(level.getBlock(x, y + 1, z)) ? "true" : "false");
        st = BlockStateHelper.withProp(st, "north", isChorusLike(level.getBlock(x, y, z - 1)) ? "true" : "false");
        st = BlockStateHelper.withProp(st, "south", isChorusLike(level.getBlock(x, y, z + 1)) ? "true" : "false");
        st = BlockStateHelper.withProp(st, "west", isChorusLike(level.getBlock(x - 1, y, z)) ? "true" : "false");
        st = BlockStateHelper.withProp(st, "east", isChorusLike(level.getBlock(x + 1, y, z)) ? "true" : "false");
        level.setBlock(x, y, z, st);
    }

    private static boolean isChorusLike(int state) {
        if (state <= 0) return false;
        String n = BlockStateHelper.getName(state);
        return "chorus_plant".equals(n) || "chorus_flower".equals(n);
    }

    private static boolean isEndStone(WorldGenLevel level, int x, int y, int z) {
        int st = level.getBlock(x, y, z);
        return st > 0 && "end_stone".equals(BlockStateHelper.getName(st));
    }

    private static int findTopEndStone(WorldGenLevel level, int bx, int bz) {
        int top = Math.min(level.getHeight() - 2, 250);
        for (int y = top; y >= 1; y--) {
            if (level.getBlock(bx, y, bz) != 0 && isEndStone(level, bx, y, bz)) {
                if (level.getBlock(bx, y + 1, bz) == 0) return y;
            }
        }
        return -1;
    }
}
