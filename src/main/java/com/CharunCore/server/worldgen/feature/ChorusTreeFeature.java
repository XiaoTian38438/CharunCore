package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

/**
 * 末地紫颂树 (简化版 ChorusPlantFeature): 在末地外岛 (|x|>100 或 |z|>100) 的末地石
 * 地表上生成 2-5 节主干 + 随机分支 + 顶部紫颂花。
 * 方块: chorus_plant (带 up/down/north/south/east/west 属性) + chorus_flower。
 */
public final class ChorusTreeFeature {

    private ChorusTreeFeature() {}

    public static void generate(WorldGenLevel level, int chunkX, int chunkZ, RandomSource rnd) {
        int plant = BlockStateHelper.getDefault("chorus_plant");
        int flower = BlockStateHelper.getDefault("chorus_flower");
        if (plant <= 0 || flower <= 0) return;

        for (int attempt = 0; attempt < 4; attempt++) {
            int bx = chunkX * 16 + rnd.nextInt(16);
            int bz = chunkZ * 16 + rnd.nextInt(16);
            // 原版: 紫颂树只在末地外岛生成
            if (Math.abs(bx) < 100 && Math.abs(bz) < 100) continue;

            int top = findTopEndStone(level, bx, bz);
            if (top < 0) continue;

            int y = top + 1;
            // 主干高度 2-5
            int height = 2 + rnd.nextInt(4);
            int[][] placed = new int[16][3];
            int pc = 0;
            for (int i = 0; i < height; i++) {
                int above = level.getBlock(bx, y + 1, bz);
                if (above != 0) break;
                int cur = level.getBlock(bx, y, bz);
                if (cur != 0) break;
                setPlant(level, bx, y, bz, plant, placed, pc++);
                // 分支: 每节 40% 向一侧长 1-2 格
                if (rnd.nextInt(10) < 4 && pc < 14) {
                    int dir = rnd.nextInt(4);
                    int dx = dir == 0 ? 1 : dir == 1 ? -1 : 0;
                    int dz = dir == 2 ? 1 : -1;
                    int len = 1 + rnd.nextInt(2);
                    int cx = bx, cz = bz, cy = y;
                    for (int b = 0; b < len && pc < 14; b++) {
                        cx += dx; cz += dz;
                        if (level.getBlock(cx, cy, cz) != 0) break;
                        setPlant(level, cx, cy, cz, plant, placed, pc++);
                    }
                }
                y++;
                if (y > 250) break;
            }
            if (pc > 0 && level.getBlock(bx, y, bz) == 0) {
                setFlower(level, bx, y, bz, flower);
            }
        }
    }

    private static void setPlant(WorldGenLevel level, int x, int y, int z, int plant, int[][] placed, int idx) {
        int st = plant;
        int up = level.getBlock(x, y + 1, z);
        int down = level.getBlock(x, y - 1, z);
        st = BlockStateHelper.withProp(st, "up", up != 0 ? "true" : "false");
        st = BlockStateHelper.withProp(st, "down", down != 0 ? "true" : "false");
        int n = level.getBlock(x, y, z - 1), s = level.getBlock(x, y, z + 1);
        int e = level.getBlock(x + 1, y, z), w = level.getBlock(x - 1, y, z);
        st = BlockStateHelper.withProp(st, "north", isChorus(n, "chorus_plant", "chorus_flower") ? "true" : "false");
        st = BlockStateHelper.withProp(st, "south", isChorus(s, "chorus_plant", "chorus_flower") ? "true" : "false");
        st = BlockStateHelper.withProp(st, "east", isChorus(e, "chorus_plant", "chorus_flower") ? "true" : "false");
        st = BlockStateHelper.withProp(st, "west", isChorus(w, "chorus_plant", "chorus_flower") ? "true" : "false");
        level.setBlock(x, y, z, st);
        if (idx < placed.length) { placed[idx][0] = x; placed[idx][1] = y; placed[idx][2] = z; }
    }

    private static void setFlower(WorldGenLevel level, int x, int y, int z, int flower) {
        level.setBlock(x, y, z, flower);
    }

    private static boolean isChorus(int state, String... names) {
        if (state <= 0) return false;
        String n = BlockStateHelper.getName(state);
        for (String s : names) if (s.equals(n)) return true;
        return false;
    }

    private static int findTopEndStone(WorldGenLevel level, int bx, int bz) {
        for (int y = 80; y <= 100; y++) {
            int st = level.getBlock(bx, y, bz);
            if (st != 0 && BlockStateHelper.getName(st).equals("end_stone")) {
                if (level.getBlock(bx, y + 1, bz) == 0) return y;
            }
        }
        return -1;
    }
}
