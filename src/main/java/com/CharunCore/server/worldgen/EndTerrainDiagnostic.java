package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.DimensionType;

/**
 * Bug21 验证：末地地形（主岛 + 外岛）是否与原版一致。
 * 生成多个末地区块（含中心 0,0 与外岛坐标），统计 end_stone 数量与表面高度分布，
 * 判断是否存在浮空岛屿（外岛），以及中心主岛是否实心。
 */
public final class EndTerrainDiagnostic {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;
        int endStoneId = BlockStateHelper.getDefault("end_stone");

        // 坐标：中心主岛(0,0)，外岛区(距离 ~1100~3000 之间)
        int[][] coords = {
            {0, 0},       // 主岛中心
            {1, 0},       // 主岛边缘
            {64, 64},     // ~905 距离 小岛区
            {80, 80},     // ~1131 距离 外岛起点
            {120, 120},   // ~1697 距离 高地
            {160, 160},   // ~2263 距离 高地
            {200, 200},   // ~2828 距离 远方
        };

        System.out.println("=== End terrain diagnose (seed=" + seed + ") ===");
        System.out.printf("%8s %8s %10s %10s %12s %12s%n", "cx", "cz", "endStone", "maxTopY", "minTopY", "islands?");

        for (int[] c : coords) {
            int cx = c[0], cz = c[1];
            DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(seed, DimensionType.THE_END);
            Chunk chunk = gen.generateBaseOnly(cx, cz);

            int endStone = 0;
            int maxTopY = Integer.MIN_VALUE;
            int minTopY = Integer.MAX_VALUE;
            int surfaceCols = 0;
            int varyingCols = 0;

            int minY = chunk.getMinY();
            int topYmax = minY + chunk.getSectionCount() * 16 - 1;

            for (int lx = 0; lx < 16; lx++) {
                for (int lz = 0; lz < 16; lz++) {
                    int topY = -1;
                    for (int y = topYmax; y >= minY; y--) {
                        int blk = chunk.getBlock(lx, y, lz);
                        if (blk == endStoneId && blk != 0) {
                            topY = y;
                            break;
                        }
                    }
                    if (topY >= 0) {
                        surfaceCols++;
                        endStone += countEndStoneColumn(chunk, lx, topY, lz, endStoneId);
                        maxTopY = Math.max(maxTopY, topY);
                        minTopY = Math.min(minTopY, topY);
                        varyingCols++;
                    }
                }
            }

            boolean hasIslands = surfaceCols > 0;
            System.out.printf("%8d %8d %10d %10d %12d %12s (surfCols=%d, range=%d)%n",
                cx, cz, endStone,
                maxTopY == Integer.MIN_VALUE ? -1 : maxTopY,
                minTopY == Integer.MAX_VALUE ? -1 : minTopY,
                hasIslands ? "YES" : "none",
                surfaceCols,
                (maxTopY == Integer.MIN_VALUE ? 0 : maxTopY) - (minTopY == Integer.MAX_VALUE ? 0 : minTopY));
        }
    }

    private static int countEndStoneColumn(Chunk chunk, int lx, int topY, int lz, int endStoneId) {
        int cnt = 0;
        for (int y = topY; y >= topY - 20 && y >= 0; y--) {
            if (chunk.getBlock(lx, y, lz) == endStoneId) cnt++;
        }
        return cnt;
    }
}
