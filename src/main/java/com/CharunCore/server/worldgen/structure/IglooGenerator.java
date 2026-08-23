package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public class IglooGenerator {

    private static final int SNOW_BLOCK = BlockStateHelper.getDefault("snow_block");
    private static final int SPRUCE_PLANKS = BlockStateHelper.getDefault("spruce_planks");
    private static final int SPRUCE_LOG = BlockStateHelper.getDefault("spruce_log");
    private static final int GLASS_PANE = BlockStateHelper.getDefault("glass_pane");
    private static final int CRAFTING_TABLE = BlockStateHelper.getDefault("crafting_table");
    private static final int WHITE_BED = BlockStateHelper.getDefault("white_bed");
    private static final int FURNACE = BlockStateHelper.getDefault("furnace");
    private static final int CHEST = BlockStateHelper.getDefault("chest");
    private static final int BREWING_STAND = BlockStateHelper.getDefault("brewing_stand");
    private static final int STONE_BRICKS = BlockStateHelper.getDefault("stone_bricks");
    private static final int SMOOTH_STONE = BlockStateHelper.getDefault("smooth_stone");
    private static final int LEVER = BlockStateHelper.getDefault("lever");
    private static final int CAULDRON = BlockStateHelper.getDefault("cauldron");
    private static final int OAK_PLANKS = BlockStateHelper.getDefault("oak_planks");
    private static final int OAK_FENCE = BlockStateHelper.getDefault("oak_fence");

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, RandomSource random) {
        int baseY = findSurfaceY(chunk, localX, localZ);
        if (baseY < -60 || baseY > 300) return;

        buildDome(chunk, localX, localZ, baseY);

        if (random.nextInt(2) == 0) {
            buildBasement(chunk, localX, localZ, baseY - 4, random);
        }
    }

    private static int findSurfaceY(Chunk chunk, int lx, int lz) {
        for (int y = 319; y >= -64; y--) {
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                if (chunk.getBlock(lx, y, lz) != 0) return y + 1;
            }
        }
        return 64;
    }

    private static void buildDome(Chunk chunk, int cx, int cz, int baseY) {
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                int lx = cx + x;
                int lz = cz + z;
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                chunk.setBlock(lx, baseY, lz, SPRUCE_PLANKS);
                for (int dy = 1; dy <= 3; dy++) {
                    int by = baseY + dy;
                    boolean wall = Math.abs(x) == 3 || Math.abs(z) == 3;
                    if (wall) {
                        chunk.setBlock(lx, by, lz, SNOW_BLOCK);
                    } else {
                        chunk.setBlock(lx, by, lz, 0);
                    }
                }
                chunk.setBlock(lx, baseY + 4, lz, SNOW_BLOCK);
            }
        }

        int windowX = cx + 3;
        int windowZ = cz;
        if (windowX >= 0 && windowX <= 15 && windowZ >= 0 && windowZ <= 15) {
            chunk.setBlock(windowX, baseY + 2, windowZ, GLASS_PANE);
        }

        int doorX = cx;
        int doorZ = cz - 3;
        if (doorX >= 0 && doorX <= 15 && doorZ >= 0 && doorZ <= 15) {
            chunk.setBlock(doorX, baseY + 1, doorZ, 0);
            chunk.setBlock(doorX, baseY + 2, doorZ, 0);
        }

        int craftX = cx - 2;
        int craftZ = cz + 2;
        if (craftX >= 0 && craftX <= 15 && craftZ >= 0 && craftZ <= 15) {
            chunk.setBlock(craftX, baseY + 1, craftZ, CRAFTING_TABLE);
        }

        int furnaceX = cx + 2;
        int furnaceZ = cz + 2;
        if (furnaceX >= 0 && furnaceX <= 15 && furnaceZ >= 0 && furnaceZ <= 15) {
            chunk.setBlock(furnaceX, baseY + 1, furnaceZ, FURNACE);
        }

        int bedX = cx + 1;
        int bedZ = cz + 2;
        if (bedX >= 0 && bedX <= 15 && bedZ >= 0 && bedZ <= 15) {
            chunk.setBlock(bedX, baseY + 1, bedZ, WHITE_BED);
        }

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                int lx = cx + x;
                int lz = cz + z;
                if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                    chunk.setBlock(lx, baseY + 1, lz, OAK_PLANKS);
                }
            }
        }
    }

    private static void buildBasement(Chunk chunk, int cx, int cz, int baseY, RandomSource random) {
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                int lx = cx + x;
                int lz = cz + z;
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                for (int y = 0; y <= 3; y++) {
                    int by = baseY + y;
                    boolean wall = Math.abs(x) == 4 || Math.abs(z) == 4;
                    if (y == 0 || y == 3 || wall) {
                        chunk.setBlock(lx, by, lz, STONE_BRICKS);
                    } else {
                        chunk.setBlock(lx, by, lz, 0);
                    }
                }
            }
        }

        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                int lx = cx + x;
                int lz = cz + z;
                if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                    chunk.setBlock(lx, baseY, lz, SMOOTH_STONE);
                }
            }
        }

        int brewX = cx - 3;
        int brewZ = cz;
        if (brewX >= 0 && brewX <= 15 && brewZ >= 0 && brewZ <= 15) {
            chunk.setBlock(brewX, baseY + 1, brewZ, BREWING_STAND);
        }

        int chestX = cx + 3;
        int chestZ = cz;
        if (chestX >= 0 && chestX <= 15 && chestZ >= 0 && chestZ <= 15) {
            chunk.setBlock(chestX, baseY + 1, chestZ, CHEST);
        }

        int leverX = cx;
        int leverZ = cz + 3;
        if (leverX >= 0 && leverX <= 15 && leverZ >= 0 && leverZ <= 15) {
            chunk.setBlock(leverX, baseY + 2, leverZ, LEVER);
        }

        int cauldronX = cx + 1;
        int cauldronZ = cz - 2;
        if (cauldronX >= 0 && cauldronX <= 15 && cauldronZ >= 0 && cauldronZ <= 15) {
            chunk.setBlock(cauldronX, baseY + 1, cauldronZ, CAULDRON);
        }

        int fenceX = cx - 2;
        int fenceZ = cz;
        if (fenceX >= 0 && fenceX <= 15 && fenceZ >= 0 && fenceZ <= 15) {
            chunk.setBlock(fenceX, baseY + 1, fenceZ, OAK_FENCE);
            chunk.setBlock(fenceX, baseY + 2, fenceZ, OAK_FENCE);
            chunk.setBlock(fenceX, baseY + 3, fenceZ, OAK_FENCE);
        }
    }
}
