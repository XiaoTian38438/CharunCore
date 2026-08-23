package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public class NetherFossilGenerator {

    private static final int BONE_BLOCK = BlockStateHelper.getDefault("bone_block");
    private static final int SOUL_SAND = BlockStateHelper.getDefault("soul_sand");
    private static final int SOUL_SOIL = BlockStateHelper.getDefault("soul_soil");

    public static void generate(Chunk chunk, int localX, int localZ, int y, RandomSource random) {
        int shape = random.nextInt(3);
        int size = 5 + random.nextInt(11);

        switch (shape) {
            case 0:
                generateCurved(chunk, localX, localZ, y, size, random);
                break;
            case 1:
                generateStraight(chunk, localX, localZ, y, size, random);
                break;
            case 2:
                generateTShaped(chunk, localX, localZ, y, size, random);
                break;
        }
    }

    private static void generateCurved(Chunk chunk, int startX, int startY, int startZ, int size, RandomSource random) {
        int x = startX;
        int y = startY;
        int z = startZ;
        int dx = random.nextBoolean() ? 1 : -1;
        int dz = random.nextBoolean() ? 1 : -1;

        for (int i = 0; i < size; i++) {
            placeBone(chunk, x, y, z, random);
            if (i > 0 && i % (2 + random.nextInt(3)) == 0) {
                if (random.nextBoolean()) {
                    dx = -dx;
                } else {
                    dz = -dz;
                }
            }
            x += dx;
            z += dz;
            y += random.nextInt(3) - 1;
        }
    }

    private static void generateStraight(Chunk chunk, int startX, int startY, int startZ, int size, RandomSource random) {
        int x = startX;
        int y = startY;
        int z = startZ;
        int axis = random.nextInt(2);
        int dir = random.nextBoolean() ? 1 : -1;

        for (int i = 0; i < size; i++) {
            placeBone(chunk, x, y, z, random);
            if (axis == 0) {
                x += dir;
            } else {
                z += dir;
            }
            if (random.nextInt(5) == 0) {
                y += random.nextInt(3) - 1;
            }
        }
    }

    private static void generateTShaped(Chunk chunk, int startX, int startY, int startZ, int size, RandomSource random) {
        int half = size / 2;
        int x = startX;
        int y = startY;
        int z = startZ;
        int axis = random.nextInt(2);
        int dir = random.nextBoolean() ? 1 : -1;

        for (int i = 0; i < half; i++) {
            placeBone(chunk, x, y, z, random);
            if (axis == 0) {
                x += dir;
            } else {
                z += dir;
            }
            if (random.nextInt(5) == 0) {
                y += random.nextInt(3) - 1;
            }
        }

        int branchLen = size - half;
        int branchDir = random.nextBoolean() ? 1 : -1;
        for (int i = 0; i < branchLen; i++) {
            placeBone(chunk, x, y, z, random);
            if (axis == 0) {
                z += branchDir;
            } else {
                x += branchDir;
            }
            if (random.nextInt(4) == 0) {
                y += random.nextInt(3) - 1;
            }
        }

        int crossLen = half / 2 + random.nextInt(2);
        int crossDir = random.nextBoolean() ? 1 : -1;
        int midX = startX;
        int midZ = startZ;
        for (int i = 0; i < half; i++) {
            midX = axis == 0 ? startX + dir * i : startX;
            midZ = axis == 0 ? startZ : startZ + dir * i;
        }
        for (int i = 0; i < crossLen; i++) {
            int bx = midX;
            int bz = midZ;
            if (axis == 0) {
                bz += crossDir * i;
            } else {
                bx += crossDir * i;
            }
            placeBone(chunk, bx, y, bz, random);
        }
    }

    private static void placeBone(Chunk chunk, int x, int y, int z, RandomSource random) {
        if (x < 0 || x > 15 || z < 0 || z > 15) return;
        if (y < -64 || y > 319) return;

        int roll = random.nextInt(10);
        int block;
        if (roll == 0) {
            block = SOUL_SAND;
        } else if (roll == 1) {
            block = SOUL_SOIL;
        } else {
            block = BONE_BLOCK;
        }
        chunk.setBlock(x, y, z, block);

        if (random.nextInt(4) == 0 && y - 1 >= -64) {
            int belowBlock;
            int belowRoll = random.nextInt(10);
            if (belowRoll == 0) {
                belowBlock = SOUL_SAND;
            } else if (belowRoll == 1) {
                belowBlock = SOUL_SOIL;
            } else {
                belowBlock = BONE_BLOCK;
            }
            chunk.setBlock(x, y - 1, z, belowBlock);
        }
    }
}
