package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public final class GlowLichenGenerator {

    private static final int AIR = 0;

    private final int glowLichen;
    private final int stone, deepslate, tuff, granite, diorite, andesite, calcite, dripstoneBlock, smoothBasalt;

    public GlowLichenGenerator() {
        this.glowLichen = BlockStateHelper.getDefault("glow_lichen");
        this.stone = BlockStateHelper.getDefault("stone");
        this.deepslate = BlockStateHelper.getDefault("deepslate");
        this.tuff = BlockStateHelper.getDefault("tuff");
        this.granite = BlockStateHelper.getDefault("granite");
        this.diorite = BlockStateHelper.getDefault("diorite");
        this.andesite = BlockStateHelper.getDefault("andesite");
        this.calcite = BlockStateHelper.getDefault("calcite");
        this.dripstoneBlock = BlockStateHelper.getDefault("dripstone_block");
        this.smoothBasalt = BlockStateHelper.getDefault("smooth_basalt");
    }

    public void generate(Chunk chunk, int chunkX, int chunkZ, RandomSource rng) {
        // 30 attempts per chunk, in caves below sea level
        for (int i = 0; i < 30; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int y = rng.nextInt(60); // Y 0-59 (underground caves)

            if (chunk.getBlock(lx, y, lz) != AIR) continue;

            // Try to place on any adjacent solid face
            int[] dx = {0, 0, 1, -1, 0, 0};
            int[] dy = {1, -1, 0, 0, 0, 0};
            int[] dz = {0, 0, 0, 0, 1, -1};

            int[] order = {0, 1, 2, 3, 4, 5};
            // Fisher-Yates shuffle
            for (int j = 5; j > 0; j--) {
                int k = rng.nextInt(j + 1);
                int tmp = order[j];
                order[j] = order[k];
                order[k] = tmp;
            }

            for (int dirIdx : order) {
                int nx = lx + dx[dirIdx];
                int ny = y + dy[dirIdx];
                int nz = lz + dz[dirIdx];
                if (nx < 0 || nx > 15 || nz < 0 || nz > 15) continue;
                if (ny < -64 || ny > 319) continue;

                int neighbor = chunk.getBlock(nx, ny, nz);
                if (isSolidStone(neighbor)) {
                    chunk.setBlock(lx, y, lz, glowLichen);
                    break;
                }
            }
        }
    }

    private boolean isSolidStone(int block) {
        return block == stone || block == deepslate || block == tuff
            || block == granite || block == diorite || block == andesite
            || block == calcite || block == dripstoneBlock || block == smoothBasalt;
    }
}
