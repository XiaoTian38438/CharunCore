package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public class PillagerOutpostGenerator {

    private static final int DARK_OAK_PLANKS = BlockStateHelper.getDefault("dark_oak_planks");
    private static final int DARK_OAK_LOG = BlockStateHelper.getDefault("dark_oak_log");
    private static final int DARK_OAK_STAIRS = BlockStateHelper.getDefault("dark_oak_stairs");
    private static final int DARK_OAK_SLAB = BlockStateHelper.getDefault("dark_oak_slab");
    private static final int COBBLESTONE = BlockStateHelper.getDefault("cobblestone");
    private static final int OAK_FENCE = BlockStateHelper.getDefault("oak_fence");
    private static final int OAK_LOG = BlockStateHelper.getDefault("oak_log");
    private static final int LADDER = BlockStateHelper.getDefault("ladder");
    private static final int CHEST = BlockStateHelper.getDefault("chest");
    private static final int DARK_OAK_FENCE = BlockStateHelper.getDefault("dark_oak_fence");
    private static final int IRON_BARS = BlockStateHelper.getDefault("iron_bars");
    private static final int CAULDRON = BlockStateHelper.getDefault("cauldron");

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, RandomSource random) {
        int baseY = findSurfaceY(chunk, localX, localZ);
        if (baseY < 1 || baseY > 280) return;
        buildTower(chunk, localX, localZ, baseY);
        buildPlatforms(chunk, localX, localZ, baseY, random);
    }

    private static int findSurfaceY(Chunk chunk, int lx, int lz) {
        for (int y = 319; y >= -64; y--) {
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                if (chunk.getBlock(lx, y, lz) != 0) return y + 1;
            }
        }
        return 64;
    }

    private static void buildTower(Chunk chunk, int cx, int cz, int baseY) {
        for (int y = 0; y < 16; y++) {
            int by = baseY + y;
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    int lx = cx + x;
                    int lz = cz + z;
                    if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                    if (y == 0) {
                        chunk.setBlock(lx, by, lz, COBBLESTONE);
                    } else if (y == 15) {
                        if (Math.abs(x) <= 2 && Math.abs(z) <= 2) {
                            chunk.setBlock(lx, by, lz, DARK_OAK_SLAB);
                        }
                    } else if (Math.abs(x) == 2 || Math.abs(z) == 2) {
                        chunk.setBlock(lx, by, lz, DARK_OAK_LOG);
                    } else {
                        chunk.setBlock(lx, by, lz, 0);
                    }
                }
            }
        }

        int topY = baseY + 15;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) == 2 || Math.abs(z) == 2) {
                    int lx = cx + x;
                    int lz = cz + z;
                    if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                        chunk.setBlock(lx, topY + 1, lz, DARK_OAK_FENCE);
                    }
                }
            }
        }

        if (cx >= 0 && cx <= 15 && cz >= 0 && cz <= 15) {
            chunk.setBlock(cx, baseY + 14, cz, CHEST);
        }

        for (int y = 1; y < 15; y++) {
            int lx = cx + 2;
            if (lx >= 0 && lx <= 15 && cz >= 0 && cz <= 15) {
                chunk.setBlock(lx, baseY + y, cz, LADDER);
            }
        }
    }

    private static void buildPlatforms(Chunk chunk, int cx, int cz, int baseY, RandomSource random) {
        int[][] offsets = {{10, 0}, {-10, 0}, {0, 10}, {0, -10}};
        for (int[] offset : offsets) {
            int px = cx + offset[0];
            int pz = cz + offset[1];
            if (px < -4 || px > 19 || pz < -4 || pz > 19) continue;

            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    int lx = px + x;
                    int lz = pz + z;
                    if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                    chunk.setBlock(lx, baseY + 5, lz, DARK_OAK_PLANKS);
                }
            }

            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if (Math.abs(x) == 2 || Math.abs(z) == 2) {
                        int lx = px + x;
                        int lz = pz + z;
                        if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                            chunk.setBlock(lx, baseY + 6, lz, DARK_OAK_FENCE);
                        }
                    }
                }
            }

            int sx = px;
            int sz = pz;
            if (sx >= 0 && sx <= 15 && sz >= 0 && sz <= 15) {
                for (int y = 0; y < 5; y++) {
                    chunk.setBlock(sx, baseY + y, sz, OAK_LOG);
                }
            }
        }
    }
}
