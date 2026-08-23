package com.CharunCore.server.worldgen;

import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.utils.BlockStateHelper;

/**
 * Stage 0H 路径验证：用 DensityRouterChunkGenerator 生成 chunk 0,0，
 * 输出基本统计 (石头/水/空气各多少块)，证明密度链能正确驱动 chunk 生成。
 */
public final class ChunkGenSmokeTest {
    public static void main(String[] args) {
        BlockStateHelper.init();
        DensityRouterChunkGenerator.setProfiling(true);
        long t0 = System.currentTimeMillis();
        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(1234567L);
        long t1 = System.currentTimeMillis();
        System.out.println("[ChunkGen] 构造生成器耗时 " + (t1 - t0) + "ms");

        // warmup
        for (int i = 1; i <= 3; i++) gen.generate(i, i);

        Chunk c = gen.generate(0, 0);
        long t2 = System.currentTimeMillis();
        System.out.println("[ChunkGen] 生成 chunk(0,0) 耗时 " + (t2 - t1) + "ms");

        // generate more for profile
        for (int x = 1; x <= 10; x++) gen.generate(x, x);

        int stone = 0, deep = 0, water = 0, lava = 0, air = 0, dirt = 0, grass = 0, sand = 0, bedrock = 0, other = 0;
        int oreBlocks = 0, fillerBlocks = 0;
        int featureBlocks = 0;
        int stoneId = BlockStateHelper.getDefault("stone");
        int deepId = BlockStateHelper.getDefault("deepslate");
        int waterId = BlockStateHelper.getDefault("water");
        int lavaId = BlockStateHelper.getDefault("lava");
        int dirtId = BlockStateHelper.getDefault("dirt");
        int grassId = BlockStateHelper.getDefault("grass_block");
        int sandId = BlockStateHelper.getDefault("sand");
        int bedrockId = BlockStateHelper.getDefault("bedrock");
        int copperOreId = BlockStateHelper.getDefault("copper_ore");
        int deepslateIronOreId = BlockStateHelper.getDefault("deepslate_iron_ore");
        int graniteId = BlockStateHelper.getDefault("granite");
        int tuffId = BlockStateHelper.getDefault("tuff");
        int shortGrassId = BlockStateHelper.getDefault("short_grass");
        int fernId = BlockStateHelper.getDefault("fern");
        int dandelionId = BlockStateHelper.getDefault("dandelion");
        int poppyId = BlockStateHelper.getDefault("poppy");
        int oakLogId = BlockStateHelper.getDefault("oak_log");
        int oakLeavesId = BlockStateHelper.getDefault("oak_leaves");
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = -64; y < 320; y++) {
                    int s = c.getBlock(x, y, z);
                    if (s == 0) air++;
                    else if (s == stoneId) stone++;
                    else if (s == deepId) deep++;
                    else if (s == waterId) water++;
                    else if (s == lavaId) lava++;
                    else if (s == dirtId) dirt++;
                    else if (s == grassId) grass++;
                    else if (s == sandId) sand++;
                    else if (s == bedrockId) bedrock++;
                    else if (s == copperOreId || s == deepslateIronOreId) oreBlocks++;
                    else if (s == graniteId || s == tuffId) fillerBlocks++;
                    else if (s == shortGrassId || s == fernId || s == dandelionId || s == poppyId
                             || s == oakLogId || s == oakLeavesId) featureBlocks++;
                    else other++;
                }
            }
        }
        int total = 16 * 16 * 384;
        System.out.println("[ChunkGen] Chunk (0,0) 方块统计 (NoiseChunk 插值, lavaLevel=" + gen.LAVA_LEVEL + "):");
        System.out.printf("  空气    : %d (%.1f%%)%n", air, air * 100.0 / total);
        System.out.printf("  石头    : %d (%.1f%%)%n", stone, stone * 100.0 / total);
        System.out.printf("  深板岩  : %d (%.1f%%)%n", deep, deep * 100.0 / total);
        System.out.printf("  泥土    : %d (%.1f%%)%n", dirt, dirt * 100.0 / total);
        System.out.printf("  草地    : %d (%.1f%%)%n", grass, grass * 100.0 / total);
        System.out.printf("  沙子    : %d (%.1f%%)%n", sand, sand * 100.0 / total);
        System.out.printf("  水      : %d (%.1f%%)%n", water, water * 100.0 / total);
        if (lava > 0) System.out.printf("  熔岩    : %d (%.1f%%)%n", lava, lava * 100.0 / total);
        System.out.printf("  基岩    : %d (%.1f%%)%n", bedrock, bedrock * 100.0 / total);
        if (oreBlocks > 0) System.out.printf("  矿石    : %d (%.1f%%)%n", oreBlocks, oreBlocks * 100.0 / total);
        if (fillerBlocks > 0) System.out.printf("  填充岩  : %d (%.1f%%)%n", fillerBlocks, fillerBlocks * 100.0 / total);
        if (featureBlocks > 0) {
            int sg = 0, fl = 0, dn = 0, pp = 0, ol = 0, lv = 0;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = -64; y < 320; y++) {
                        int s = c.getBlock(x, y, z);
                        if (s == shortGrassId) sg++;
                        else if (s == fernId) fl++;
                        else if (s == dandelionId) dn++;
                        else if (s == poppyId) pp++;
                        else if (s == oakLogId) ol++;
                        else if (s == oakLeavesId) lv++;
                    }
                }
            }
            System.out.printf("  特征方块: %d (短草=%d, 蕨=%d, 蒲公英=%d, 虞美人=%d, 橡木原木=%d, 橡树树叶=%d)%n",
                featureBlocks, sg, fl, dn, pp, ol, lv);
        }
        if (other > 0) System.out.printf("  其他    : %d (%.1f%%)%n", other, other * 100.0 / total);
        System.out.printf("  合计    : %d (应为 %d)%n", air + stone + deep + water + lava + dirt + grass + sand + bedrock + oreBlocks + fillerBlocks + featureBlocks + other, total);

        // 抽取一列方块打印简易地形截面
        System.out.println("[ChunkGen] x=8, z=8 列从下到上的方块 (每隔 8):");
        for (int y = -64; y < 100; y += 4) {
            int s = c.getBlock(8, y, 8);
            String name = s == 0 ? "air" : BlockStateHelper.getName(s);
            String bar = ".".repeat(Math.max(0, (int)((Math.log(1 + (s == 0 ? 0 : 1)) * 5))));
            System.out.printf("  y=%4d %s%n", y, name);
        }

        // ---- 地表方块 16x16 网格可视化（看矩形混杂） ----
        System.out.println("[ChunkGen] 地表方块网格 (每个 (x,z) 顶部非空气方块名首字母):");
        int stoneId2 = stoneId, waterId2 = waterId, sandId2 = sandId, grassId2 = grassId;
        StringBuilder header = new StringBuilder("   z\\x");
        for (int x = 0; x < 16; x++) header.append(String.format("%3d", x));
        System.out.println(header);
        for (int z = 0; z < 16; z++) {
            StringBuilder row = new StringBuilder(String.format("%3d: ", z));
            for (int x = 0; x < 16; x++) {
                int top = 0;
                for (int y = 319; y >= -64; y--) {
                    int s = c.getBlock(x, y, z);
                    if (s != 0 && s != waterId2) { top = s; break; }
                }
                String tag;
                if (top == 0) tag = " . ";
                else if (top == grassId2) tag = "G";
                else if (top == sandId2) tag = "S";
                else if (top == stoneId2) tag = "#";
                else tag = "?";
                row.append(String.format("%3s", tag));
            }
            System.out.println(row);
        }
        // 同时打印每列 biome id
        System.out.println("[ChunkGen] 每列 colBiome 网格:");
        int[] colBiome = gen.computeColBiomeForTest(c);
        StringBuilder bhdr = new StringBuilder("   z\\x");
        for (int x = 0; x < 16; x++) bhdr.append(String.format("%3d", x));
        System.out.println(bhdr);
        for (int z = 0; z < 16; z++) {
            StringBuilder row = new StringBuilder(String.format("%3d: ", z));
            for (int x = 0; x < 16; x++) {
                int b = colBiome[z * 16 + x];
                row.append(String.format("%3d", b));
            }
            System.out.println(row);
        }

        DensityRouterChunkGenerator.printProfile();
        System.out.println("[ChunkGen] DONE");
    }
}
