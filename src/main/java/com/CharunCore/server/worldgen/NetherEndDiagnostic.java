package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.DimensionType;

import java.util.HashMap;
import java.util.Map;

/**
 * Bug20/Bug21 验证：
 *  END  — 外岛是否存在、高度是否起伏、群系分档（TheEndBiomeSource 原版阈值）是否与实体岛屿对应。
 *  NETHER — 熔岩海(y<32)、顶底基岩、表面方块按群系分布（灵魂沙/玄武岩/nylium）、萤石/岩浆块/巨型菌。
 */
public final class NetherEndDiagnostic {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;

        System.out.println("=== END terrain (seed=" + seed + ") ===");
        DensityRouterChunkGenerator endGen = new DensityRouterChunkGenerator(seed, DimensionType.THE_END);
        int endStone = BlockStateHelper.getDefault("end_stone");
        int[][] endCoords = {
            {0, 0}, {70, 0}, {80, 80}, {90, 30}, {110, 60}, {130, 40},
            {150, 150}, {170, 90}, {200, 120}, {230, 200}, {260, 260}, {300, 180}
        };
        Map<Integer, Integer> biomeLandChunks = new HashMap<>();
        for (int[] c : endCoords) {
            int cx = c[0], cz = c[1];
            Chunk chunk = endGen.generateBaseOnly(cx, cz);
            int minY = chunk.getMinY();
            int maxY = minY + chunk.getSectionCount() * 16 - 1;
            int cols = 0, minTop = Integer.MAX_VALUE, maxTop = Integer.MIN_VALUE;
            long sumTop = 0;
            for (int lx = 0; lx < 16; lx++) {
                for (int lz = 0; lz < 16; lz++) {
                    int topY = -1;
                    for (int y = maxY; y >= minY; y--) {
                        if (chunk.getBlock(lx, y, lz) == endStone) { topY = y; break; }
                    }
                    if (topY >= 0) { cols++; sumTop += topY; minTop = Math.min(minTop, topY); maxTop = Math.max(maxTop, topY); }
                }
            }
            int biome = endGen.getColumnBiome(cx * 16 + 8, cz * 16 + 8);
            String bn = biome == 56 ? "the_end" : biome == 17 ? "highlands" : biome == 18 ? "midlands"
                : biome == 44 ? "small_islands" : "barrens";
            if (cols > 0) biomeLandChunks.merge(biome, 1, Integer::sum);
            double dist = Math.sqrt((double) cx * cx + (double) cz * cz);
            System.out.printf("chunk(%4d,%4d) d=%5.0f biome=%-13s landCols=%3d topY=[%d..%d avg=%.0f]%n",
                cx, cz, dist, bn, cols,
                cols > 0 ? minTop : -1, cols > 0 ? maxTop : -1,
                cols > 0 ? (double) sumTop / cols : -1);
        }

        System.out.println();
        System.out.println("=== NETHER terrain (seed=" + seed + ") ===");
        DensityRouterChunkGenerator netGen = new DensityRouterChunkGenerator(seed, DimensionType.THE_NETHER);
        String[] track = {"netherrack", "bedrock", "lava", "soul_sand", "soul_soil", "basalt",
            "blackstone", "gravel", "crimson_nylium", "warped_nylium", "nether_wart_block",
            "warped_wart_block", "glowstone", "magma_block", "crimson_stem", "warped_stem",
            "shroomlight", "weeping_vines", "crimson_roots", "warped_roots"};
        Map<String, Long> hist = new HashMap<>();
        for (String s : track) hist.put(s, 0L);

        int nFloorMin = 128, nFloorMax = 0, cCeilMin = 128, cCeilMax = 0;
        int lavaSeaCols = 0, solidCols = 0;
        Map<Integer, Integer> biomeCols = new HashMap<>();
        for (int cx = -6; cx < 6; cx++) {
            for (int cz = -6; cz < 6; cz++) {
                Chunk chunk = netGen.generate(cx, cz);
                int[] cb = netGen.computeColBiomeForTest(chunk);
                biomeCols.merge(cb[136], 1, Integer::sum);
                for (int lx = 0; lx < 16; lx++) {
                    for (int lz = 0; lz < 16; lz++) {
                        int floor = -1, ceil = -1;
                        boolean sawAirBelow32 = false;
                        for (int y = 0; y <= 127; y++) {
                            int b = chunk.getBlock(lx, y, lz);
                            String name = b == 0 ? null : shortName(BlockStateHelper.getName(b));
                            if (name != null && hist.containsKey(name)) hist.merge(name, 1L, Long::sum);
                            if (b == 0 && y < 32) sawAirBelow32 = true;
                            if (b != 0) { if (floor < 0) floor = y; ceil = y; }
                        }
                        if (floor >= 0) {
                            nFloorMin = Math.min(nFloorMin, floor); nFloorMax = Math.max(nFloorMax, floor);
                            cCeilMin = Math.min(cCeilMin, ceil); cCeilMax = Math.max(cCeilMax, ceil);
                            solidCols++;
                        }
                        if (!sawAirBelow32) lavaSeaCols++;
                    }
                }
            }
        }
        System.out.println("floor Y range: " + nFloorMin + ".." + nFloorMax
            + "   ceiling Y range: " + cCeilMin + ".." + cCeilMax);
        System.out.println("columns fully filled below y=32 (lava sea): " + lavaSeaCols + "/" + (solidCols));
        System.out.println("biome distribution (center col): " + biomeCols);
        System.out.println("block histogram:");
        for (Map.Entry<String, Long> e : hist.entrySet()) {
            if (e.getValue() > 0) System.out.printf("  %-18s %,12d%n", e.getKey(), e.getValue());
        }
    }

    private static String shortName(String name) {
        return name.startsWith("minecraft:") ? name.substring(10) : name;
    }
}
