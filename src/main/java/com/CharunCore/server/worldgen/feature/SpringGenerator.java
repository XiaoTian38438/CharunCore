package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public final class SpringGenerator {

    private static final int AIR = 0;

    private final int stone, deepslate, granite, diorite, andesite, tuff, dirt, gravel, grassBlock;
    private final int water, lava;
    private final int cobblestone;

    public SpringGenerator() {
        this.stone = BlockStateHelper.getDefault("stone");
        this.deepslate = BlockStateHelper.getDefault("deepslate");
        this.granite = BlockStateHelper.getDefault("granite");
        this.diorite = BlockStateHelper.getDefault("diorite");
        this.andesite = BlockStateHelper.getDefault("andesite");
        this.tuff = BlockStateHelper.getDefault("tuff");
        this.dirt = BlockStateHelper.getDefault("dirt");
        this.gravel = BlockStateHelper.getDefault("gravel");
        this.grassBlock = BlockStateHelper.getDefault("grass_block");
        this.water = BlockStateHelper.getDefault("water");
        this.lava = BlockStateHelper.getDefault("lava");
        this.cobblestone = BlockStateHelper.getDefault("cobblestone");
    }

    public void generate(Chunk chunk, int chunkX, int chunkZ, RandomSource rng) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        // Water springs: 25 attempts, below Y=60, need air/water above
        for (int i = 0; i < 25; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int y = 10 + rng.nextInt(50); // Y 10-59
            if (y > 60) continue;
            int bx = baseX + lx;
            int bz = baseZ + lz;
            tryPlaceSpring(chunk, lx, y, lz, water);
        }

        // Lava springs: 20 attempts, below Y=0, need air above
        for (int i = 0; i < 20; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int y = rng.nextInt(30) - 10; // Y -10 to 19, mainly underground
            if (y > 0) continue;
            int bx = baseX + lx;
            int bz = baseZ + lz;
            tryPlaceSpring(chunk, lx, y, lz, lava);
        }

        // Lava springs above Y=0 (rare, in surface caves)
        for (int i = 0; i < 5; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int y = 1 + rng.nextInt(30); // Y 1-30
            int bx = baseX + lx;
            int bz = baseZ + lz;
            tryPlaceSpring(chunk, lx, y, lz, lava);
        }
    }

    private void tryPlaceSpring(Chunk chunk, int lx, int y, int lz, int fluid) {
        if (y < -64 || y > 319) return;
        int block = chunk.getBlock(lx, y, lz);
        if (!isReplaceable(block)) return;
        if (!hasAirAbove(chunk, lx, y, lz)) return;

        // Place spring
        chunk.setBlock(lx, y, lz, fluid);

        // Surrounding stone → cobblestone/stone (for water springs, stone → cobblestone)
        if (fluid == water) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        int nx = lx + dx;
                        int nz = lz + dz;
                        int ny = y + dy;
                        if (nx < 0 || nx > 15 || nz < 0 || nz > 15) continue;
                        int nb = chunk.getBlock(nx, ny, nz);
                        if (nb == stone || nb == deepslate) {
                            chunk.setBlock(nx, ny, nz, cobblestone);
                        }
                    }
                }
            }
        }
    }

    private boolean isReplaceable(int block) {
        return block == stone || block == deepslate || block == granite
            || block == diorite || block == andesite || block == tuff
            || block == dirt || block == gravel || block == grassBlock;
    }

    private boolean hasAirAbove(Chunk chunk, int lx, int y, int lz) {
        if (y + 1 > 319) return false;
        int above = chunk.getBlock(lx, y + 1, lz);
        return above == AIR;
    }
}
