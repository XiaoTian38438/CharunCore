package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.DensityRouterChunkGenerator;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestSurfaceDist {
    static int AIR, WATER, LAVA;
    static final Set<Integer> OCEAN_BIOMES = new HashSet<>();

    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BlockStateHelper.init();
        BlockManager.init();
        AIR = BlockStateHelper.getDefault("air");
        WATER = BlockStateHelper.getDefault("water");
        LAVA = BlockStateHelper.getDefault("lava");
        int[] oceans = {11, 9, 13, 12, 58, 22, 6, 35, 29, 45, 3, 52, 24, 41};
        for (int o : oceans) OCEAN_BIOMES.add(o);

        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(12345L);

        int landChunks = 0;
        for (int cz = 0; cz <= 8 && landChunks < 3; cz++) {
            for (int cx = 0; cx <= 8 && landChunks < 3; cx++) {
                Chunk chunk = gen.generate(cx, cz);
                int landCols = 0;
                for (int lz = 0; lz < 16; lz++) {
                    for (int lx = 0; lx < 16; lx++) {
                        int topY = -1;
                        for (int y = 320; y >= 63; y--) {
                            int b = chunk.getBlock(lx, y, lz);
                            if (b != AIR && b != WATER && b != LAVA) { topY = y; break; }
                        }
                        if (topY >= 63) landCols++;
                    }
                }
                if (landCols < 64) continue;
                landChunks++;
                System.out.println("=== chunk(" + cx + "," + cz + ") landCols=" + landCols + " ===");
                Map<String, Integer> surfaceCounts = new HashMap<>();
                Map<Integer, Integer> biomeCounts = new HashMap<>();
                for (int lz = 0; lz < 16; lz++) {
                    for (int lx = 0; lx < 16; lx++) {
                        biomeCounts.merge(chunk.getBiome(lx, lz), 1, Integer::sum);
                        int topY = -1;
                        for (int y = 320; y >= -64; y--) {
                            int b = chunk.getBlock(lx, y, lz);
                            if (b != AIR && b != WATER && b != LAVA) { topY = y; break; }
                        }
                        if (topY < 0) { surfaceCounts.merge("empty", 1, Integer::sum); continue; }
                        surfaceCounts.merge(BlockStateHelper.getName(chunk.getBlock(lx, topY, lz)), 1, Integer::sum);
                    }
                }
                System.out.println("  biomes: " + biomeCounts);
                surfaceCounts.entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .forEach(e -> System.out.println(String.format("  %-20s %d", e.getKey(), e.getValue())));
            }
        }
        if (landChunks == 0) System.out.println("NO land chunks found in 0..6 grid");
    }
}
