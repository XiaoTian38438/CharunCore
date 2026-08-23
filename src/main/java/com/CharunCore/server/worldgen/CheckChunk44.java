package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.density.DensityFunction;

public final class CheckChunk44 {
    public static void main(String[] args) {
        BlockStateHelper.init();
        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(1234567L);
        for (int i = 0; i < 3; i++) gen.generate(0, 0);
        Chunk c = gen.generate(4, 4);
        int stoneId = BlockStateHelper.getDefault("stone");
        int deepId = BlockStateHelper.getDefault("deepslate");
        int waterId = BlockStateHelper.getDefault("water");
        int dirtId = BlockStateHelper.getDefault("dirt");
        int grassId = BlockStateHelper.getDefault("grass_block");
        int bedrockId = BlockStateHelper.getDefault("bedrock");
        int sandId = BlockStateHelper.getDefault("sand");
        System.out.printf("sandId=%d grassId=%d stoneId=%d waterId=%d%n", sandId, grassId, stoneId, waterId);

        // Direct density at column (8,8) cell corners for chunk (4,4)
        int wx = 4*16 + 8;
        int wz = 4*16 + 8;
        System.out.printf("=== Direct finalDensity at column (%d,%d) world (%d,%d) ===%n", 8, 8, wx, wz);
        for (int y = 0; y <= 96; y += 8) {
            DensityFunction.FunctionContext ctx = new DensityFunction.SinglePointContext(wx, y, wz);
            double d = gen.router.finalDensity().compute(ctx);
            System.out.printf("  y=%3d finalDens=%12.6f%n", y, d);
        }

        // print columns
        for (int cx = 2; cx < 16; cx += 6) {
            for (int cz = 2; cz < 16; cz += 6) {
                System.out.printf("=== Column (%d,%d) world (%d,%d) ===%n", cx, cz, 4*16+cx, 4*16+cz);
                for (int y = -64; y <= 100; y++) {
                    int s = c.getBlock(cx, y, cz);
                    if (s != 0) {
                        String name = BlockStateHelper.getName(s);
                        System.out.printf("  y=%3d %s%n", y, name);
                    }
                }
                System.out.println();
            }
        }
        // also print full stats
        int stone = 0, deep = 0, water = 0, lava = 0, air = 0, dirt = 0, grass = 0, sand = 0, bedrock = 0, other = 0;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = -64; y < 320; y++) {
                    int s = c.getBlock(x, y, z);
                    if (s == 0) air++;
                    else if (s == stoneId) stone++;
                    else if (s == deepId) deep++;
                    else if (s == waterId) water++;
                    else if (s == dirtId) dirt++;
                    else if (s == grassId) grass++;
                    else if (s == sandId) sand++;
                    else if (s == bedrockId) bedrock++;
                    else other++;
                }
            }
        }
        int total = 16 * 16 * 384;
        System.out.printf("Stats: air=%d stone=%d deep=%d water=%d dirt=%d grass=%d sand=%d bedrock=%d other=%d total=%d%n",
            air, stone, deep, water, dirt, grass, sand, bedrock, other, total);
    }
}
