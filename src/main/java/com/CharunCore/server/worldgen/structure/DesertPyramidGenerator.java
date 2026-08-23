package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public class DesertPyramidGenerator {

    private static final int SANDSTONE = BlockStateHelper.getDefault("sandstone");
    private static final int CUT_SANDSTONE = BlockStateHelper.getDefault("cut_sandstone");
    private static final int CHISELED_SANDSTONE = BlockStateHelper.getDefault("chiseled_sandstone");
    private static final int SMOOTH_SANDSTONE = BlockStateHelper.getDefault("smooth_sandstone");
    private static final int BLUE_TERRACOTTA = BlockStateHelper.getDefault("blue_terracotta");
    private static final int ORANGE_TERRACOTTA = BlockStateHelper.getDefault("orange_terracotta");
    private static final int TNT = BlockStateHelper.getDefault("tnt");
    private static final int STONE_PRESSURE_PLATE = BlockStateHelper.getDefault("stone_pressure_plate");
    private static final int CHEST = BlockStateHelper.getDefault("chest");

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, RandomSource random) {
        int baseY = findSurfaceY(chunk, localX, localZ);
        if (baseY < -60 || baseY > 300) return;

        buildPyramid(chunk, localX, localZ, baseY, random);
        buildTrapRoom(chunk, localX, localZ, baseY);
        placeLootChests(chunk, localX, localZ, baseY);
        placeTerracotta(chunk, localX, localZ, baseY);
    }

    private static int findSurfaceY(Chunk chunk, int lx, int lz) {
        for (int y = 319; y >= -64; y--) {
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                if (chunk.getBlock(lx, y, lz) != 0) return y + 1;
            }
        }
        return 64;
    }

    private static void buildPyramid(Chunk chunk, int cx, int cz, int baseY, RandomSource random) {
        for (int layer = 0; layer < 9; layer++) {
            int y = baseY + layer;
            int minCoord = layer;
            int maxCoord = 20 - layer;
            for (int x = minCoord; x <= maxCoord; x++) {
                for (int z = minCoord; z <= maxCoord; z++) {
                    int lx = cx + x - 10;
                    int lz = cz + z - 10;
                    if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                    boolean isEdge = x == minCoord || x == maxCoord || z == minCoord || z == maxCoord;
                    if (isEdge) {
                        int block = (layer % 2 == 0) ? SANDSTONE : CUT_SANDSTONE;
                        chunk.setBlock(lx, y, lz, block);
                    } else if (layer == 0) {
                        chunk.setBlock(lx, y, lz, SANDSTONE);
                    }
                }
            }
        }

        for (int x = 0; x < 21; x++) {
            for (int z = 0; z < 21; z++) {
                int lx = cx + x - 10;
                int lz = cz + z - 10;
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                for (int dy = -5; dy < 0; dy++) {
                    int by = baseY + dy;
                    if (x == 0 || x == 20 || z == 0 || z == 20) {
                        chunk.setBlock(lx, by, lz, SANDSTONE);
                    }
                }
            }
        }
    }

    private static void buildTrapRoom(Chunk chunk, int cx, int cz, int baseY) {
        int roomY = baseY - 11;
        for (int x = 8; x <= 12; x++) {
            for (int z = 8; z <= 12; z++) {
                int lx = cx + x - 10;
                int lz = cz + z - 10;
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                chunk.setBlock(lx, roomY, lz, CHISELED_SANDSTONE);
                chunk.setBlock(lx, roomY + 1, lz, 0);
                chunk.setBlock(lx, roomY + 2, lz, 0);
            }
        }

        int plateX = cx;
        int plateZ = cz;
        if (plateX >= 0 && plateX <= 15 && plateZ >= 0 && plateZ <= 15) {
            chunk.setBlock(plateX, roomY + 1, plateZ, STONE_PRESSURE_PLATE);
        }

        for (int x = 9; x <= 11; x++) {
            for (int z = 9; z <= 11; z++) {
                int lx = cx + x - 10;
                int lz = cz + z - 10;
                if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                    chunk.setBlock(lx, roomY - 2, lz, TNT);
                }
            }
        }

        for (int x = 9; x <= 11; x++) {
            for (int z = 9; z <= 11; z++) {
                int lx = cx + x - 10;
                int lz = cz + z - 10;
                if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                    chunk.setBlock(lx, roomY - 1, lz, SANDSTONE);
                }
            }
        }
    }

    private static void placeLootChests(Chunk chunk, int cx, int cz, int baseY) {
        int chestY = baseY - 11;
        int[][] chestPos = {{12, 10}, {8, 10}, {10, 12}, {10, 8}};
        for (int[] pos : chestPos) {
            int lx = cx + pos[0] - 10;
            int lz = cz + pos[1] - 10;
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                chunk.setBlock(lx, chestY, lz, CHEST);
            }
        }
    }

    private static void placeTerracotta(Chunk chunk, int cx, int cz, int baseY) {
        int topY = baseY + 9;
        int centerX = cx;
        int centerZ = cz;
        if (centerX >= 0 && centerX <= 15 && centerZ >= 0 && centerZ <= 15) {
            chunk.setBlock(centerX, topY, centerZ, BLUE_TERRACOTTA);
        }

        int[][] orangePos = {
            {10, 8}, {10, 12}, {8, 10}, {12, 10},
            {9, 9}, {9, 11}, {11, 9}, {11, 11}
        };
        for (int[] pos : orangePos) {
            int lx = cx + pos[0] - 10;
            int lz = cz + pos[1] - 10;
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                chunk.setBlock(lx, topY, lz, ORANGE_TERRACOTTA);
            }
        }
    }
}
