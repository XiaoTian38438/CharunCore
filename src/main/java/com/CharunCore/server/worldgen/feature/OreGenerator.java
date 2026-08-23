package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public final class OreGenerator {

    private final int air, stone, deepslate;
    private final int coal, deepslateCoal;
    private final int iron, deepslateIron;
    private final int gold, deepslateGold;
    private final int redstone, deepslateRedstone;
    private final int diamond, deepslateDiamond;
    private final int lapis, deepslateLapis;
    private final int copper, deepslateCopper;
    private final int emerald, deepslateEmerald;
    private final int infestedStone, infestedDeepslate;
    private final int granite, diorite, andesite, tuff;
    private final int gravel, dirtBlock;
    // 下界矿石
    private final int netherGold, quartz, ancientDebris;
    private final int netherrack, basalt, blackstone;

    public OreGenerator() {
        this.air = 0;
        this.stone = BlockStateHelper.getDefault("stone");
        this.deepslate = BlockStateHelper.getDefault("deepslate");
        this.coal = BlockStateHelper.getDefault("coal_ore");
        this.deepslateCoal = BlockStateHelper.getDefault("deepslate_coal_ore");
        this.iron = BlockStateHelper.getDefault("iron_ore");
        this.deepslateIron = BlockStateHelper.getDefault("deepslate_iron_ore");
        this.gold = BlockStateHelper.getDefault("gold_ore");
        this.deepslateGold = BlockStateHelper.getDefault("deepslate_gold_ore");
        this.redstone = BlockStateHelper.getDefault("redstone_ore");
        this.deepslateRedstone = BlockStateHelper.getDefault("deepslate_redstone_ore");
        this.diamond = BlockStateHelper.getDefault("diamond_ore");
        this.deepslateDiamond = BlockStateHelper.getDefault("deepslate_diamond_ore");
        this.lapis = BlockStateHelper.getDefault("lapis_ore");
        this.deepslateLapis = BlockStateHelper.getDefault("deepslate_lapis_ore");
        this.copper = BlockStateHelper.getDefault("copper_ore");
        this.deepslateCopper = BlockStateHelper.getDefault("deepslate_copper_ore");
        this.emerald = BlockStateHelper.getDefault("emerald_ore");
        this.deepslateEmerald = BlockStateHelper.getDefault("deepslate_emerald_ore");
        this.infestedStone = BlockStateHelper.getDefault("infested_stone");
        this.infestedDeepslate = BlockStateHelper.getDefault("infested_deepslate");
        this.granite = BlockStateHelper.getDefault("granite");
        this.diorite = BlockStateHelper.getDefault("diorite");
        this.andesite = BlockStateHelper.getDefault("andesite");
        this.tuff = BlockStateHelper.getDefault("tuff");
        this.gravel = BlockStateHelper.getDefault("gravel");
        this.dirtBlock = BlockStateHelper.getDefault("dirt");
        this.netherGold = BlockStateHelper.getDefault("nether_gold_ore");
        this.quartz = BlockStateHelper.getDefault("nether_quartz_ore");
        this.ancientDebris = BlockStateHelper.getDefault("ancient_debris");
        this.netherrack = BlockStateHelper.getDefault("netherrack");
        this.basalt = BlockStateHelper.getDefault("basalt");
        this.blackstone = BlockStateHelper.getDefault("blackstone");
    }

    public void generate(Chunk chunk, int chunkX, int chunkZ, RandomSource rng) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        placeOre(chunk, baseX, baseZ, rng, coal, deepslateCoal, 17, 0.0f, 0, 192, true, 20);
        placeOre(chunk, baseX, baseZ, rng, coal, deepslateCoal, 17, 0.0f, 136, 320, false, 30);
        placeOre(chunk, baseX, baseZ, rng, coal, deepslateCoal, 17, 0.5f, 0, 192, true, 20); // buried

        placeOre(chunk, baseX, baseZ, rng, iron, deepslateIron, 9, 0.0f, 80, 384, true, 90);
        placeOre(chunk, baseX, baseZ, rng, iron, deepslateIron, 9, 0.0f, -24, 56, true, 10);
        placeOre(chunk, baseX, baseZ, rng, iron, deepslateIron, 4, 0.0f, -64, 72, false, 10); // small

        placeOre(chunk, baseX, baseZ, rng, gold, deepslateGold, 9, 0.0f, -64, 32, true, 4);
        placeOre(chunk, baseX, baseZ, rng, gold, deepslateGold, 9, 0.0f, -64, -48, false, 1); // lower
        placeOre(chunk, baseX, baseZ, rng, gold, deepslateGold, 9, 0.5f, -64, 32, true, 4); // buried

        placeOre(chunk, baseX, baseZ, rng, redstone, deepslateRedstone, 8, 0.0f, -64, 15, false, 4);
        placeOre(chunk, baseX, baseZ, rng, redstone, deepslateRedstone, 8, 0.0f, -32, 32, true, 8); // lower

        placeOre(chunk, baseX, baseZ, rng, diamond, deepslateDiamond, 4, 0.5f, -64, 16, true, 7); // small
        placeOre(chunk, baseX, baseZ, rng, diamond, deepslateDiamond, 8, 0.5f, -64, -4, false, 2); // medium
        placeOre(chunk, baseX, baseZ, rng, diamond, deepslateDiamond, 12, 0.7f, -64, 16, true, 1); // large (rare)

        placeOre(chunk, baseX, baseZ, rng, lapis, deepslateLapis, 7, 0.0f, -32, 32, true, 2);
        placeOre(chunk, baseX, baseZ, rng, lapis, deepslateLapis, 7, 0.0f, -64, 64, false, 4); // buried

        placeOre(chunk, baseX, baseZ, rng, copper, deepslateCopper, 10, 0.0f, -16, 112, true, 16);
        placeOre(chunk, baseX, baseZ, rng, copper, deepslateCopper, 20, 0.0f, -16, 112, true, 16); // large

        placeOre(chunk, baseX, baseZ, rng, emerald, deepslateEmerald, 3, 0.0f, -16, 480, true, 100);

        placeOre(chunk, baseX, baseZ, rng, infestedStone, infestedDeepslate, 9, 0.0f, -64, 63, false, 14);

        placeStoneType(chunk, baseX, baseZ, rng, dirtBlock, 33, 0.0f, 0, 160, 7);
        placeStoneType(chunk, baseX, baseZ, rng, gravel, 33, 0.0f, -64, 320, 14);
        placeStoneType(chunk, baseX, baseZ, rng, granite, 64, 0.0f, 64, 128, 1);
        placeStoneType(chunk, baseX, baseZ, rng, granite, 64, 0.0f, 0, 60, 2);
        placeStoneType(chunk, baseX, baseZ, rng, diorite, 64, 0.0f, 64, 128, 1);
        placeStoneType(chunk, baseX, baseZ, rng, diorite, 64, 0.0f, 0, 60, 2);
        placeStoneType(chunk, baseX, baseZ, rng, andesite, 64, 0.0f, 64, 128, 1);
        placeStoneType(chunk, baseX, baseZ, rng, andesite, 64, 0.0f, 0, 60, 2);
        placeStoneType(chunk, baseX, baseZ, rng, tuff, 64, 0.0f, -64, 0, 2);
        // Clay is not placed as a generic stone-type blob in vanilla.
        // Underwater clay disks are handled by AquaticGenerator.
    }

    /**
     * 下界散矿生成（忠实原版 1.21 下界矿石分布）。
     * 仅替换 下界岩/玄武岩/黑石，不暴露在空气中的远古残骸由 discardChance 控制。
     */
    public void generateNether(Chunk chunk, int chunkX, int chunkZ, RandomSource rng) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        // 下界金矿石：散布于 Y=10..117，常见
        placeNetherOre(chunk, baseX, baseZ, rng, netherGold, 9, 0.0f, 10, 117, false, 12);
        // 下界石英矿石：散布于 Y=10..117，常见（红石生电必需：比较器/观察者/ daylight）
        placeNetherOre(chunk, baseX, baseZ, rng, quartz, 9, 0.0f, 10, 117, false, 12);

        // 远古残骸 簇1：Y=8..24 三角分布，每簇 0-3 块，1 簇/区块
        placeNetherOre(chunk, baseX, baseZ, rng, ancientDebris, 4, 0.5f, 8, 24, true, 1);
        // 远古残骸 簇2：Y=8..119 均匀分布，每簇 0-2 块，1 簇/区块
        placeNetherOre(chunk, baseX, baseZ, rng, ancientDebris, 3, 0.5f, 8, 119, false, 1);
    }

    private void placeNetherOre(Chunk chunk, int baseX, int baseZ, RandomSource rng,
                                int oreId, int size, float discardChance,
                                int minY, int maxY, boolean trapezoid, int count) {
        for (int i = 0; i < count; i++) {
            int originX = baseX + rng.nextInt(16);
            int originZ = baseZ + rng.nextInt(16);
            int originY = randHeight(rng, minY, maxY, trapezoid);
            placeBlob(chunk, rng, originX, originY, originZ, size, discardChance,
                (blockId) -> {
                    if (blockId == netherrack || blockId == basalt || blockId == blackstone) return oreId;
                    return -1;
                });
        }
    }

    private void placeOre(Chunk chunk, int baseX, int baseZ, RandomSource rng,
                          int oreId, int deepslateOreId,
                          int size, float discardChance,
                          int minY, int maxY, boolean trapezoid, int count) {
        for (int i = 0; i < count; i++) {
            int originX = baseX + rng.nextInt(16);
            int originZ = baseZ + rng.nextInt(16);
            int originY = randHeight(rng, minY, maxY, trapezoid);
            placeBlob(chunk, rng, originX, originY, originZ, size, discardChance,
                (blockId) -> {
                    if (blockId == stone) return oreId;
                    if (blockId == deepslate) return deepslateOreId;
                    return -1;
                });
        }
    }

    private void placeStoneType(Chunk chunk, int baseX, int baseZ, RandomSource rng,
                                int blockId, int size, float discardChance,
                                int minY, int maxY, int count) {
        for (int i = 0; i < count; i++) {
            int originX = baseX + rng.nextInt(16);
            int originZ = baseZ + rng.nextInt(16);
            int originY = randHeight(rng, minY, maxY, false);
            placeBlob(chunk, rng, originX, originY, originZ, size, discardChance,
                (blockIdIn) -> {
                    if (blockIdIn == stone || blockIdIn == deepslate
                        || blockIdIn == granite || blockIdIn == diorite
                        || blockIdIn == andesite || blockIdIn == tuff) {
                        return blockId;
                    }
                    return -1;
                });
        }
    }

    private int randHeight(RandomSource rng, int minY, int maxY, boolean trapezoid) {
        if (trapezoid) {
            int half = (maxY - minY) / 2;
            return minY + rng.nextInt(half + 1) + rng.nextInt(half + 1);
        }
        if (minY >= maxY) return minY;
        return minY + rng.nextInt(maxY - minY + 1);
    }

    private void placeBlob(Chunk chunk, RandomSource rng,
                           int originX, int originY, int originZ,
                           int size, float discardChance,
                           java.util.function.IntUnaryOperator blockSelector) {
        if (size == 0) return;

        float angle = rng.nextFloat() * (float) Math.PI;
        float radiusXZ = size / 8.0f;
        int padding = (size / 16 * 2 + 2) / 2;

        float endAX = originX + (float) Math.sin(angle) * radiusXZ;
        float endBX = originX - (float) Math.sin(angle) * radiusXZ;
        float endAZ = originZ + (float) Math.cos(angle) * radiusXZ;
        float endBZ = originZ - (float) Math.cos(angle) * radiusXZ;
        float endAY = originY + rng.nextInt(3) - 2;
        float endBY = originY + rng.nextInt(3) - 2;

        int minBlobX = Math.max(originX - size / 2 - padding, (originX >> 4) << 4);
        int minBlobY = Math.max(originY - size / 2 - padding, -64);
        int minBlobZ = Math.max(originZ - size / 2 - padding, (originZ >> 4) << 4);
        int maxBlobX = Math.min(originX + size / 2 + padding, ((originX >> 4) << 4) + 15);
        int maxBlobY = Math.min(originY + size / 2 + padding, 320);
        int maxBlobZ = Math.min(originZ + size / 2 + padding, ((originZ >> 4) << 4) + 15);

        float[] sphereX = new float[size];
        float[] sphereY = new float[size];
        float[] sphereZ = new float[size];
        float[] sphereR = new float[size];

        for (int i = 0; i < size; i++) {
            float t = (float) i / size;
            sphereX[i] = lerp(t, endAX, endBX);
            sphereY[i] = lerp(t, endAY, endBY);
            sphereZ[i] = lerp(t, endAZ, endBZ);
            float randRadius = (float) (rng.nextDouble() * size / 16.0);
            sphereR[i] = ((float) Math.sin(Math.PI * t) + 1.0f) * randRadius / 2.0f + 0.5f;
        }

        for (int i = 0; i < size; i++) {
            if (sphereR[i] < 0) continue;
            float rSq = sphereR[i] * sphereR[i];
            for (int j = i + 1; j < size; j++) {
                if (sphereR[j] < 0) continue;
                float dx = sphereX[i] - sphereX[j];
                float dy = sphereY[i] - sphereY[j];
                float dz = sphereZ[i] - sphereZ[j];
                float distSq = dx * dx + dy * dy + dz * dz;
                float rB = sphereR[j];
                if (distSq + rB > rSq) continue;
                if (rSq < sphereR[j] * sphereR[j]) {
                    sphereR[i] = -1;
                } else {
                    sphereR[j] = -1;
                }
                break;
            }
        }

        for (int i = 0; i < size; i++) {
            if (sphereR[i] < 0) continue;
            float r = sphereR[i];
            float rSq = r * r;
            float cx = sphereX[i];
            float cy = sphereY[i];
            float cz = sphereZ[i];

            int siMinX = Math.max((int) Math.floor(cx - r), minBlobX);
            int siMaxX = Math.min((int) Math.ceil(cx + r), maxBlobX);
            int siMinY = Math.max((int) Math.floor(cy - r), minBlobY);
            int siMaxY = Math.min((int) Math.ceil(cy + r), maxBlobY);
            int siMinZ = Math.max((int) Math.floor(cz - r), minBlobZ);
            int siMaxZ = Math.min((int) Math.ceil(cz + r), maxBlobZ);

            for (int bx = siMinX; bx <= siMaxX; bx++) {
                float dx = bx + 0.5f - cx;
                for (int by = siMinY; by <= siMaxY; by++) {
                    float dy = by + 0.5f - cy;
                    float dySq = dy * dy;
                    for (int bz = siMinZ; bz <= siMaxZ; bz++) {
                        float dz = bz + 0.5f - cz;
                        if (dx * dx + dySq + dz * dz >= rSq) continue;

                        int lx = bx & 15;
                        int lz = bz & 15;
                        int currentBlock = chunk.getBlock(lx, by, lz);
                        int newBlock = blockSelector.applyAsInt(currentBlock);
                        if (newBlock < 0) continue;

                        if (discardChance > 0.0f && !shouldSkipAirCheck(discardChance, rng)) {
                            if (isAdjacentToAir(chunk, lx, by, lz)) continue;
                        }
                        chunk.setBlock(lx, by, lz, newBlock);
                    }
                }
            }
        }
    }

    private boolean isAdjacentToAir(Chunk chunk, int lx, int y, int lz) {
        return chunk.getBlock(lx, y + 1, lz) == air
            || chunk.getBlock(lx, y - 1, lz) == air
            || chunk.getBlock((lx + 1) & 15, y, lz) == air
            || chunk.getBlock((lx - 1) & 15, y, lz) == air
            || chunk.getBlock(lx, y, (lz + 1) & 15) == air
            || chunk.getBlock(lx, y, (lz - 1) & 15) == air;
    }

    private boolean shouldSkipAirCheck(float discardChance, RandomSource rng) {
        if (discardChance <= 0.0f) return true;
        if (discardChance >= 1.0f) return false;
        return rng.nextFloat() >= discardChance;
    }

    private static float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }
}
