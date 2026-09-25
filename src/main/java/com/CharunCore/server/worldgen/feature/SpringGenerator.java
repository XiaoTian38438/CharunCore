package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public final class SpringGenerator {

    private static final int AIR = 0;

    private final int stone, deepslate, granite, diorite, andesite, tuff, dirt, gravel, grassBlock;
    private final int water, lava;
    private final int cobblestone, blackstone;

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
        this.blackstone = BlockStateHelper.getDefault("blackstone");
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
            tryPlaceSpring(chunk, lx, y, lz, water, rng);
        }

        // Lava springs: 原版语义 —— 只在岩浆线(y<10)以下的洞穴内出现,
        // 8 次尝试(原版 spring_lava 密度量级)。曾自创 Y-10..30 甚至地表高度,
        // 在高处洞穴放孤立岩浆源 = "死板岩浆池"的主要来源。
        for (int i = 0; i < 8; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int y = -54 + rng.nextInt(64); // Y -54..9
            if (y >= 10) continue;
            tryPlaceSpring(chunk, lx, y, lz, lava, rng);
        }
    }

    private void tryPlaceSpring(Chunk chunk, int lx, int y, int lz, int fluid, RandomSource rng) {
        if (y < -64 || y > 319) return;
        int block = chunk.getBlock(lx, y, lz);
        if (!isReplaceable(block)) return;
        if (!hasAirAbove(chunk, lx, y, lz)) return;

        // Place spring
        chunk.setBlock(lx, y, lz, fluid);

        // 周边岩石随机替换(原版 spring 的 rock 语义): 每格 60% 概率,
        // 形成不规则边缘而非规则 3x3 圈。水泉 stone→圆石化, 岩浆泉 stone→黑石点染。
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    int nx = lx + dx;
                    int nz = lz + dz;
                    int ny = y + dy;
                    if (nx < 0 || nx > 15 || nz < 0 || nz > 15) continue;
                    int nb = chunk.getBlock(nx, ny, nz);
                    if (nb != stone && nb != deepslate) continue;
                    // 距离衰减: 相邻格 60%, 外圈 25%
                    boolean near = Math.abs(dx) <= 1 && Math.abs(dz) <= 1;
                    if (!rng.nextBoolean() && (near || rng.nextInt(4) != 0)) {
                        continue;
                    }
                    chunk.setBlock(nx, ny, nz, fluid == water ? cobblestone : blackstone);
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
