package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public class NetherFortressGenerator {

    private static final int NETHER_BRICKS = BlockStateHelper.getDefault("nether_bricks");
    private static final int NETHER_BRICK_FENCE = BlockStateHelper.getDefault("nether_brick_fence");
    private static final int NETHER_BRICK_STAIRS = BlockStateHelper.getDefault("nether_brick_stairs");
    private static final int NETHER_BRICK_SLAB = BlockStateHelper.getDefault("nether_brick_slab");
    private static final int CHISELED_NETHER_BRICKS = BlockStateHelper.getDefault("chiseled_nether_bricks");
    private static final int SPAWNER = BlockStateHelper.getDefault("spawner");
    private static final int CHEST = BlockStateHelper.getDefault("chest");
    private static final int LAVA = BlockStateHelper.getDefault("lava");

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, int y, RandomSource random) {
        generateBridge(chunk, localX, localZ, y, random);

        if (random.nextInt(2) == 0) {
            int corridorX = localX + (random.nextBoolean() ? 8 : -8);
            int corridorZ = localZ + (random.nextBoolean() ? 3 : -3);
            if (corridorX >= 0 && corridorX <= 15 && corridorZ >= 0 && corridorZ <= 15) {
                generateCorridor(chunk, corridorX, corridorZ, y, random);
            }
        }

        if (random.nextInt(3) == 0) {
            int throneX = localX + (random.nextBoolean() ? 10 : -10);
            int throneZ = localZ;
            if (throneX >= 0 && throneX <= 15 && throneZ >= 0 && throneZ <= 15) {
                generateMonsterThrone(chunk, throneX, throneZ, y, random);
            }
        }
    }

    private static void generateBridge(Chunk chunk, int cx, int cz, int y, RandomSource random) {
        for (int i = 0; i < 19; i++) {
            int lx = cx + i;
            if (lx < 0 || lx > 15) continue;

            for (int z = 0; z < 5; z++) {
                int lz = cz + z - 2;
                if (lz < 0 || lz > 15) continue;
                chunk.setBlock(lx, y, lz, NETHER_BRICKS);
                chunk.setBlock(lx, y + 1, lz, 0);
                chunk.setBlock(lx, y + 2, lz, 0);
                chunk.setBlock(lx, y + 3, lz, NETHER_BRICKS);
            }

            if (i < 4 || i > 14) {
                for (int z = 0; z < 5; z++) {
                    int lz = cz + z - 2;
                    if (lz < 0 || lz > 15) continue;
                    for (int dy = 1; dy <= 2; dy++) {
                        chunk.setBlock(lx, y + dy, lz, NETHER_BRICKS);
                    }
                }
            }

            int fenceLx = cx + i;
            if (fenceLx >= 0 && fenceLx <= 15) {
                int fenceZ1 = cz - 2;
                int fenceZ2 = cz + 2;
                if (fenceZ1 >= 0 && fenceZ1 <= 15) {
                    chunk.setBlock(fenceLx, y + 4, fenceZ1, NETHER_BRICK_FENCE);
                }
                if (fenceZ2 >= 0 && fenceZ2 <= 15) {
                    chunk.setBlock(fenceLx, y + 4, fenceZ2, NETHER_BRICK_FENCE);
                }
            }

            if (i == 1 || i == 4 || i == 14 || i == 17) {
                int pillarLx = cx + i;
                if (pillarLx >= 0 && pillarLx <= 15) {
                    int pz1 = cz - 2;
                    int pz2 = cz + 2;
                    if (pz1 >= 0 && pz1 <= 15) {
                        chunk.setBlock(pillarLx, y + 1, pz1, NETHER_BRICK_FENCE);
                        chunk.setBlock(pillarLx, y + 2, pz1, NETHER_BRICK_FENCE);
                        chunk.setBlock(pillarLx, y + 3, pz1, NETHER_BRICK_FENCE);
                    }
                    if (pz2 >= 0 && pz2 <= 15) {
                        chunk.setBlock(pillarLx, y + 1, pz2, NETHER_BRICK_FENCE);
                        chunk.setBlock(pillarLx, y + 2, pz2, NETHER_BRICK_FENCE);
                        chunk.setBlock(pillarLx, y + 3, pz2, NETHER_BRICK_FENCE);
                    }
                }
            }
        }
    }

    private static void generateCorridor(Chunk chunk, int cx, int cz, int y, RandomSource random) {
        for (int i = 0; i < 12; i++) {
            int lx = cx + i;
            if (lx < 0 || lx > 15) continue;
            for (int z = 0; z < 3; z++) {
                int lz = cz + z - 1;
                if (lz < 0 || lz > 15) continue;
                chunk.setBlock(lx, y, lz, NETHER_BRICKS);
                chunk.setBlock(lx, y + 1, lz, 0);
                chunk.setBlock(lx, y + 2, lz, 0);
                chunk.setBlock(lx, y + 3, lz, NETHER_BRICKS);
            }
        }

        if (random.nextInt(3) == 0) {
            int chestX = cx + 6;
            int chestZ = cz;
            if (chestX >= 0 && chestX <= 15 && chestZ >= 0 && chestZ <= 15) {
                chunk.setBlock(chestX, y + 1, chestZ, CHEST);
            }
        }
    }

    private static void generateMonsterThrone(Chunk chunk, int cx, int cz, int y, RandomSource random) {
        for (int i = 0; i < 9; i++) {
            int lx = cx + i - 4;
            if (lx < 0 || lx > 15) continue;
            for (int z = 0; z < 7; z++) {
                int lz = cz + z - 3;
                if (lz < 0 || lz > 15) continue;
                chunk.setBlock(lx, y, lz, NETHER_BRICKS);
                chunk.setBlock(lx, y + 1, lz, 0);
                chunk.setBlock(lx, y + 2, lz, 0);
                chunk.setBlock(lx, y + 3, lz, NETHER_BRICKS);
            }
        }

        int stepY = y;
        for (int i = 0; i < 5; i++) {
            int lx = cx;
            int lz = cz - 3 + i;
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                for (int x = -1; x <= 1; x++) {
                    int sx = cx + x;
                    if (sx >= 0 && sx <= 15) {
                        chunk.setBlock(sx, stepY + i, lz, NETHER_BRICKS);
                    }
                }
            }
        }

        int spawnerX = cx;
        int spawnerZ = cz + 2;
        if (spawnerX >= 0 && spawnerX <= 15 && spawnerZ >= 0 && spawnerZ <= 15) {
            chunk.setBlock(spawnerX, y + 5, spawnerZ, SPAWNER);
        }

        int fenceX1 = cx - 1;
        int fenceX2 = cx + 1;
        int fenceZ = cz + 2;
        if (fenceX1 >= 0 && fenceX1 <= 15 && fenceZ >= 0 && fenceZ <= 15) {
            chunk.setBlock(fenceX1, y + 6, fenceZ, NETHER_BRICK_FENCE);
            chunk.setBlock(fenceX1, y + 7, fenceZ, NETHER_BRICK_FENCE);
            chunk.setBlock(fenceX1, y + 8, fenceZ, NETHER_BRICK_FENCE);
        }
        if (fenceX2 >= 0 && fenceX2 <= 15 && fenceZ >= 0 && fenceZ <= 15) {
            chunk.setBlock(fenceX2, y + 6, fenceZ, NETHER_BRICK_FENCE);
            chunk.setBlock(fenceX2, y + 7, fenceZ, NETHER_BRICK_FENCE);
            chunk.setBlock(fenceX2, y + 8, fenceZ, NETHER_BRICK_FENCE);
        }
    }
}
