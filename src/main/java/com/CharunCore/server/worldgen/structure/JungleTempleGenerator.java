package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public class JungleTempleGenerator {

    private static final int COBBLESTONE = BlockStateHelper.getDefault("cobblestone");
    private static final int MOSSY_COBBLESTONE = BlockStateHelper.getDefault("mossy_cobblestone");
    private static final int CHISELED_STONE_BRICKS = BlockStateHelper.getDefault("chiseled_stone_bricks");
    private static final int STONE_BRICK_STAIRS = BlockStateHelper.getDefault("stone_brick_stairs");
    private static final int TRIPWIRE_HOOK = BlockStateHelper.getDefault("tripwire_hook");
    private static final int TRIPWIRE = BlockStateHelper.getDefault("tripwire");
    private static final int REDSTONE_WIRE = BlockStateHelper.getDefault("redstone_wire");
    private static final int STICKY_PISTON = BlockStateHelper.getDefault("sticky_piston");
    private static final int REPEATER = BlockStateHelper.getDefault("redstone_repeater");
    private static final int DISPENSER = BlockStateHelper.getDefault("dispenser");
    private static final int CHEST = BlockStateHelper.getDefault("chest");
    private static final int VINE = BlockStateHelper.getDefault("vine");
    private static final int LEVER = BlockStateHelper.getDefault("lever");

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, RandomSource random) {
        int baseY = findSurfaceY(chunk, localX, localZ);
        if (baseY < -60 || baseY > 300) return;
        buildTemple(chunk, localX, localZ, baseY, random);
    }

    private static int findSurfaceY(Chunk chunk, int lx, int lz) {
        for (int y = 319; y >= -64; y--) {
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                if (chunk.getBlock(lx, y, lz) != 0) return y + 1;
            }
        }
        return 64;
    }

    private static void buildTemple(Chunk chunk, int cx, int cz, int baseY, RandomSource random) {
        for (int x = 0; x < 12; x++) {
            for (int z = 0; z < 15; z++) {
                int lx = cx + x - 5;
                int lz = cz + z - 7;
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
                for (int y = -4; y <= 9; y++) {
                    int by = baseY + y;
                    boolean wall = (x == 0 || x == 11 || z == 0 || z == 14) && y >= -3 && y <= 6;
                    boolean roof = y == 7 || y == 8 || y == 9;
                    boolean floor = y == -4;
                    if (wall || roof || floor) {
                        int block = random.nextInt(10) < 6 ? MOSSY_COBBLESTONE : COBBLESTONE;
                        chunk.setBlock(lx, by, lz, block);
                    } else {
                        chunk.setBlock(lx, by, lz, 0);
                    }
                }
            }
        }

        placeStairs(chunk, cx, cz, baseY);
        placeTrap1(chunk, cx, cz, baseY);
        placeTrap2(chunk, cx, cz, baseY);
        placePuzzle(chunk, cx, cz, baseY);
        placeLoot(chunk, cx, cz, baseY);
        placeVines(chunk, cx, cz, baseY);
    }

    private static void placeStairs(Chunk chunk, int cx, int cz, int baseY) {
        for (int z = 0; z < 5; z++) {
            int lx = cx;
            int lz = cz + z - 2;
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                chunk.setBlock(lx, baseY - 3, lz, STONE_BRICK_STAIRS);
            }
        }
    }

    private static void placeTrap1(Chunk chunk, int cx, int cz, int baseY) {
        int hookX1 = cx + 1 - 5;
        int hookX2 = cx + 4 - 5;
        int hookZ = cz + 8 - 7;
        if (hookX1 >= 0 && hookX1 <= 15 && hookZ >= 0 && hookZ <= 15) {
            chunk.setBlock(hookX1, baseY - 3, hookZ, TRIPWIRE_HOOK);
        }
        if (hookX2 >= 0 && hookX2 <= 15 && hookZ >= 0 && hookZ <= 15) {
            chunk.setBlock(hookX2, baseY - 3, hookZ, TRIPWIRE_HOOK);
        }
        for (int x = 2; x <= 3; x++) {
            int lx = cx + x - 5;
            if (lx >= 0 && lx <= 15 && hookZ >= 0 && hookZ <= 15) {
                chunk.setBlock(lx, baseY - 3, hookZ, TRIPWIRE);
            }
        }

        int wireX = cx + 5 - 5;
        int wireZ = cz + 7 - 7;
        if (wireX >= 0 && wireX <= 15 && wireZ >= 0 && wireZ <= 15) {
            chunk.setBlock(wireX, baseY - 3, wireZ, REDSTONE_WIRE);
        }
        for (int z = 6; z >= 1; z--) {
            int lz = cz + z - 7;
            if (wireX >= 0 && wireX <= 15 && lz >= 0 && lz <= 15) {
                chunk.setBlock(wireX, baseY - 3, lz, REDSTONE_WIRE);
            }
        }

        int dispX = cx + 3 - 5;
        int dispZ = cz + 1 - 7;
        if (dispX >= 0 && dispX <= 15 && dispZ >= 0 && dispZ <= 15) {
            chunk.setBlock(dispX, baseY - 2, dispZ, DISPENSER);
        }
    }

    private static void placeTrap2(Chunk chunk, int cx, int cz, int baseY) {
        int hookZ1 = cz + 1 - 7;
        int hookZ2 = cz + 5 - 7;
        int hookX = cx + 7 - 5;
        if (hookX >= 0 && hookX <= 15 && hookZ1 >= 0 && hookZ1 <= 15) {
            chunk.setBlock(hookX, baseY - 3, hookZ1, TRIPWIRE_HOOK);
        }
        if (hookX >= 0 && hookX <= 15 && hookZ2 >= 0 && hookZ2 <= 15) {
            chunk.setBlock(hookX, baseY - 3, hookZ2, TRIPWIRE_HOOK);
        }
        for (int z = 2; z <= 4; z++) {
            int lz = cz + z - 7;
            if (hookX >= 0 && hookX <= 15 && lz >= 0 && lz <= 15) {
                chunk.setBlock(hookX, baseY - 3, lz, TRIPWIRE);
            }
        }

        int wireX = cx + 8 - 5;
        int wireZ = cz + 6 - 7;
        if (wireX >= 0 && wireX <= 15 && wireZ >= 0 && wireZ <= 15) {
            chunk.setBlock(wireX, baseY - 3, wireZ, REDSTONE_WIRE);
        }
        int wireX2 = cx + 9 - 5;
        if (wireX2 >= 0 && wireX2 <= 15 && wireZ >= 0 && wireZ <= 15) {
            chunk.setBlock(wireX2, baseY - 3, wireZ, REDSTONE_WIRE);
        }
        int wireZ2 = cz + 5 - 7;
        if (wireX2 >= 0 && wireX2 <= 15 && wireZ2 >= 0 && wireZ2 <= 15) {
            chunk.setBlock(wireX2, baseY - 3, wireZ2, REDSTONE_WIRE);
        }

        int dispX = cx + 9 - 5;
        int dispZ = cz + 3 - 7;
        if (dispX >= 0 && dispX <= 15 && dispZ >= 0 && dispZ <= 15) {
            chunk.setBlock(dispX, baseY - 2, dispZ, DISPENSER);
        }
    }

    private static void placePuzzle(Chunk chunk, int cx, int cz, int baseY) {
        for (int i = 0; i < 3; i++) {
            int lx = cx + (8 + i) - 5;
            int lz = cz + 11 - 7;
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                chunk.setBlock(lx, baseY - 2, lz, CHISELED_STONE_BRICKS);
            }
        }

        for (int i = 0; i < 3; i++) {
            int lx = cx + (8 + i) - 5;
            int lz = cz + 12 - 7;
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                chunk.setBlock(lx, baseY - 2, lz, LEVER);
            }
        }

        for (int x = 8; x <= 10; x++) {
            for (int z = 9; z <= 10; z++) {
                int lx = cx + x - 5;
                int lz = cz + z - 7;
                if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                    chunk.setBlock(lx, baseY - 2, lz, REDSTONE_WIRE);
                }
            }
        }

        int pistonX = cx + 10 - 5;
        int pistonZ = cz + 10 - 7;
        if (pistonX >= 0 && pistonX <= 15 && pistonZ >= 0 && pistonZ <= 15) {
            chunk.setBlock(pistonX, baseY - 2, pistonZ, STICKY_PISTON);
        }

        int repX = cx + 10 - 5;
        int repZ = cz + 10 - 7;
        if (repX >= 0 && repX <= 15 && repZ >= 0 && repZ <= 15) {
            chunk.setBlock(repX, baseY - 2, repZ, REPEATER);
        }
    }

    private static void placeLoot(Chunk chunk, int cx, int cz, int baseY) {
        int chest1X = cx + 8 - 5;
        int chest1Z = cz + 3 - 7;
        if (chest1X >= 0 && chest1X <= 15 && chest1Z >= 0 && chest1Z <= 15) {
            chunk.setBlock(chest1X, baseY - 3, chest1Z, CHEST);
        }

        int chest2X = cx + 9 - 5;
        int chest2Z = cz + 10 - 7;
        if (chest2X >= 0 && chest2X <= 15 && chest2Z >= 0 && chest2Z <= 15) {
            chunk.setBlock(chest2X, baseY - 3, chest2Z, CHEST);
        }
    }

    private static void placeVines(Chunk chunk, int cx, int cz, int baseY) {
        int vineX1 = cx + 3 - 5;
        int vineZ1 = cz + 2 - 7;
        if (vineX1 >= 0 && vineX1 <= 15 && vineZ1 >= 0 && vineZ1 <= 15) {
            chunk.setBlock(vineX1, baseY - 2, vineZ1, VINE);
        }

        int vineX2 = cx + 8 - 5;
        int vineZ2 = cz + 3 - 7;
        if (vineX2 >= 0 && vineX2 <= 15 && vineZ2 >= 0 && vineZ2 <= 15) {
            chunk.setBlock(vineX2, baseY - 1, vineZ2, VINE);
            chunk.setBlock(vineX2, baseY - 2, vineZ2, VINE);
        }
    }
}
