package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public final class DripstoneGenerator {

    private static final int AIR = 0;

    private final int dripstoneBlock, pointedDripstone;
    private final int stone, deepslate, tuff, granite, diorite, andesite, water;

    public DripstoneGenerator() {
        this.dripstoneBlock = BlockStateHelper.getDefault("dripstone_block");
        this.pointedDripstone = BlockStateHelper.getDefault("pointed_dripstone");
        this.stone = BlockStateHelper.getDefault("stone");
        this.deepslate = BlockStateHelper.getDefault("deepslate");
        this.tuff = BlockStateHelper.getDefault("tuff");
        this.granite = BlockStateHelper.getDefault("granite");
        this.diorite = BlockStateHelper.getDefault("diorite");
        this.andesite = BlockStateHelper.getDefault("andesite");
        this.water = BlockStateHelper.getDefault("water");
    }

    public void generate(Chunk chunk, int chunkX, int chunkZ, RandomSource rng) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        // 3 cluster attempts per chunk, only underground
        for (int cluster = 0; cluster < 3; cluster++) {
            int lx = 2 + rng.nextInt(12);
            int lz = 2 + rng.nextInt(12);
            int y = -30 + rng.nextInt(50); // Y -30 to 19

            int bx = baseX + lx;
            int bz = baseZ + lz;

            // Scan for ceiling and floor in this column
            int ceilingY = -999;
            int floorY = -999;

            // Scan upward for ceiling (first solid block above y)
            for (int sy = y; sy <= 319; sy++) {
                int block = chunk.getBlock(lx, sy, lz);
                if (isSolidStone(block)) {
                    ceilingY = sy;
                    break;
                }
            }
            if (ceilingY == -999 || ceilingY == y) continue;

            // Scan downward for floor (first solid below y)
            for (int sy = y; sy >= -64; sy--) {
                int block = chunk.getBlock(lx, sy, lz);
                if (isSolidStone(block)) {
                    floorY = sy;
                    break;
                }
            }
            if (floorY == -999 || floorY == y) continue;

            int caveHeight = ceilingY - floorY;

            // Need at least 3 blocks of space
            if (caveHeight < 3) continue;

            // Determine stalactite length (from ceiling down)
            int stalactiteLen = 0;
            int stalagmiteLen = 0;

            if (rng.nextBoolean() && caveHeight >= 3) {
                stalactiteLen = 1 + rng.nextInt(Math.min(caveHeight - 2, 6));
            }
            if (rng.nextBoolean() && caveHeight >= 3) {
                stalagmiteLen = 1 + rng.nextInt(Math.min(caveHeight - stalactiteLen - 1, 5));
            }

            // Replace ceiling with dripstone block
            int dripsAbove = 0;
            if (rng.nextDouble() < 0.4) {
                dripsAbove = 1 + rng.nextInt(2);
            }
            for (int i = 0; i < dripsAbove; i++) {
                int sy = ceilingY + i + 1;
                if (sy > 319) break;
                if (chunk.getBlock(lx, sy, lz) != stone) break;
                chunk.setBlock(lx, sy, lz, dripstoneBlock);
            }

            // Replace floor with dripstone block
            int dripsBelow = 0;
            if (rng.nextDouble() < 0.4) {
                dripsBelow = 1 + rng.nextInt(2);
            }
            for (int i = 0; i < dripsBelow; i++) {
                int sy = floorY - i - 1;
                if (sy < -64) break;
                if (chunk.getBlock(lx, sy, lz) != stone) break;
                chunk.setBlock(lx, sy, lz, dripstoneBlock);
            }

            // Grow stalactite (from ceiling down)
            for (int i = 0; i < stalactiteLen; i++) {
                int sy = ceilingY - 1 - i;
                if (sy <= floorY) break;
                if (i == stalactiteLen - 1) {
                    chunk.setBlock(lx, sy, lz, pointedDripstone);
                } else {
                    chunk.setBlock(lx, sy, lz, dripstoneBlock);
                }
            }

            // Grow stalagmite (from floor up)
            for (int i = 0; i < stalagmiteLen; i++) {
                int sy = floorY + 1 + i;
                if (sy >= ceilingY) break;
                if (i == stalagmiteLen - 1) {
                    chunk.setBlock(lx, sy, lz, pointedDripstone);
                } else {
                    chunk.setBlock(lx, sy, lz, dripstoneBlock);
                }
            }

            // Merge case: stalactite and stalagmite meet
            int stcTip = ceilingY - stalactiteLen;
            int stgTip = floorY + stalagmiteLen;
            if (stalactiteLen > 0 && stalagmiteLen > 0 && stcTip <= stgTip) {
                // Merge: point meets point
                chunk.setBlock(lx, stcTip, lz, pointedDripstone);
            }

            // Small water pool on floor (10% chance)
            if (rng.nextDouble() < 0.1 && stalagmiteLen == 0) {
                int poolY = floorY + 1;
                if (chunk.getBlock(lx, poolY, lz) == AIR) {
                    chunk.setBlock(lx, poolY, lz, water);
                }
            }
        }
    }

    private boolean isSolidStone(int block) {
        return block == stone || block == deepslate || block == tuff
            || block == granite || block == diorite || block == andesite
            || block == dripstoneBlock;
    }
}
