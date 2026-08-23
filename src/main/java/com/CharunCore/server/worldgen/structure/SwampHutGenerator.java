package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public class SwampHutGenerator {

    private static final int SPRUCE_PLANKS = BlockStateHelper.getDefault("spruce_planks");
    private static final int SPRUCE_LOG = BlockStateHelper.getDefault("spruce_log");
    private static final int OAK_LOG = BlockStateHelper.getDefault("oak_log");
    private static final int OAK_FENCE = BlockStateHelper.getDefault("oak_fence");
    private static final int CAULDRON = BlockStateHelper.getDefault("cauldron");
    private static final int CRAFTING_TABLE = BlockStateHelper.getDefault("crafting_table");
    private static final int FLOWER_POT = BlockStateHelper.getDefault("flower_pot");
    private static final int RED_MUSHROOM = BlockStateHelper.getDefault("red_mushroom");

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, RandomSource random) {
        int baseY = findSurfaceY(chunk, localX, localZ);
        if (baseY < -60 || baseY > 300) return;
        buildHut(chunk, localX, localZ, baseY);
    }

    private static int findSurfaceY(Chunk chunk, int lx, int lz) {
        for (int y = 319; y >= -64; y--) {
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                if (chunk.getBlock(lx, y, lz) != 0) return y + 1;
            }
        }
        return 64;
    }

    private static void buildHut(Chunk chunk, int cx, int cz, int baseY) {
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                int lx = cx + x;
                int lz = cz + z;
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                chunk.setBlock(lx, baseY, lz, SPRUCE_PLANKS);
                for (int dy = 1; dy <= 4; dy++) {
                    int by = baseY + dy;
                    boolean wall = Math.abs(x) == 3 || Math.abs(z) == 3;
                    boolean edge = Math.abs(x) == 2 && Math.abs(z) == 2;
                    if (wall) {
                        chunk.setBlock(lx, by, lz, SPRUCE_PLANKS);
                    } else if (dy == 4 && !edge) {
                        chunk.setBlock(lx, by, lz, SPRUCE_PLANKS);
                    } else {
                        chunk.setBlock(lx, by, lz, 0);
                    }
                }
            }
        }

        int doorX = cx;
        int doorZ = cz - 3;
        if (doorX >= 0 && doorX <= 15 && doorZ >= 0 && doorZ <= 15) {
            chunk.setBlock(doorX, baseY + 1, doorZ, 0);
            chunk.setBlock(doorX, baseY + 2, doorZ, 0);
        }

        int cauldronX = cx + 1;
        int cauldronZ = cz + 1;
        if (cauldronX >= 0 && cauldronX <= 15 && cauldronZ >= 0 && cauldronZ <= 15) {
            chunk.setBlock(cauldronX, baseY + 1, cauldronZ, CAULDRON);
        }

        int craftX = cx - 1;
        int craftZ = cz + 1;
        if (craftX >= 0 && craftX <= 15 && craftZ >= 0 && craftZ <= 15) {
            chunk.setBlock(craftX, baseY + 1, craftZ, CRAFTING_TABLE);
        }

        int potX = cx + 2;
        int potZ = cz;
        if (potX >= 0 && potX <= 15 && potZ >= 0 && potZ <= 15) {
            chunk.setBlock(potX, baseY + 1, potZ, FLOWER_POT);
        }

        int[][] stiltPos = {{-3, -3}, {-3, 3}, {3, -3}, {3, 3}};
        for (int[] pos : stiltPos) {
            int lx = cx + pos[0];
            int lz = cz + pos[1];
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                for (int dy = 0; dy < baseY; dy++) {
                    if (chunk.getBlock(lx, dy, lz) == 0) {
                        chunk.setBlock(lx, dy, lz, OAK_LOG);
                    }
                }
            }
        }
    }
}
