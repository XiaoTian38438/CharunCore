package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

public final class AquaticGenerator {

    private final int air, water;
    private final int stone, deepslate, dirt, grassBlock;
    private final int sand, sandstone, gravel, clay;
    private final int seagrass, tallSeagrass;
    private final int kelp, kelpPlant;
    private final int tubeCoral, brainCoral, bubbleCoral, fireCoral, hornCoral;
    private final int tubeCoralFan, brainCoralFan, bubbleCoralFan, fireCoralFan, hornCoralFan;
    private final int coralBlock;

    private static final int B_COLD_OCEAN = 6;
    private static final int B_DEEP_COLD_OCEAN = 9;
    private static final int B_DEEP_FROZEN_OCEAN = 11;
    private static final int B_DEEP_LUKEWARM_OCEAN = 12;
    private static final int B_DEEP_OCEAN = 13;
    private static final int B_FROZEN_OCEAN = 22;
    private static final int B_LUKEWARM_OCEAN = 29;
    private static final int B_OCEAN = 35;
    private static final int B_WARM_OCEAN = 58;
    private static final int B_RIVER = 41;
    private static final int B_FROZEN_RIVER = 24;
    private static final int B_SWAMP = 54;
    private static final int B_MANGROVE_SWAMP = 31;
    private static final int B_BEACH = 3;
    private static final int B_SNOWY_BEACH = 45;
    private static final int B_STONY_SHORE = 52;
    private static final int SEA_LEVEL = 63;

    private int[] topSolidY;

    public AquaticGenerator() {
        this.air = 0;
        this.water = BlockStateHelper.getDefault("water");
        this.stone = BlockStateHelper.getDefault("stone");
        this.deepslate = BlockStateHelper.getDefault("deepslate");
        this.dirt = BlockStateHelper.getDefault("dirt");
        this.grassBlock = BlockStateHelper.getDefault("grass_block");
        this.sand = BlockStateHelper.getDefault("sand");
        this.sandstone = BlockStateHelper.getDefault("sandstone");
        this.gravel = BlockStateHelper.getDefault("gravel");
        this.clay = BlockStateHelper.getDefault("clay");
        this.seagrass = BlockStateHelper.getDefault("seagrass");
        this.tallSeagrass = BlockStateHelper.getDefault("tall_seagrass");
        this.kelp = BlockStateHelper.getDefault("kelp");
        this.kelpPlant = BlockStateHelper.getDefault("kelp_plant");
        this.tubeCoral = BlockStateHelper.getDefault("tube_coral");
        this.brainCoral = BlockStateHelper.getDefault("brain_coral");
        this.bubbleCoral = BlockStateHelper.getDefault("bubble_coral");
        this.fireCoral = BlockStateHelper.getDefault("fire_coral");
        this.hornCoral = BlockStateHelper.getDefault("horn_coral");
        this.tubeCoralFan = BlockStateHelper.getDefault("tube_coral_fan");
        this.brainCoralFan = BlockStateHelper.getDefault("brain_coral_fan");
        this.bubbleCoralFan = BlockStateHelper.getDefault("bubble_coral_fan");
        this.fireCoralFan = BlockStateHelper.getDefault("fire_coral_fan");
        this.hornCoralFan = BlockStateHelper.getDefault("horn_coral_fan");
        this.coralBlock = BlockStateHelper.getDefault("tube_coral_block");
    }

    public void generate(WorldGenLevel level, Chunk chunk, int chunkX, int chunkZ,
                         RandomSource rng, int[] colBiome, int[] topSolidY) {
        this.topSolidY = topSolidY;
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        boolean hasWarmOcean = false;
        boolean hasColdOcean = false;
        boolean hasOcean = false;
        boolean hasRiver = false;
        boolean hasSwamp = false;
        boolean hasBeach = false;
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int colIdx = lz * 16 + lx;
                int biome = colBiome[colIdx];
                hasWarmOcean |= isWarmOcean(biome);
                hasColdOcean |= isColdOcean(biome);
                hasOcean |= isOcean(biome);
                hasRiver |= biome == B_RIVER || biome == B_FROZEN_RIVER;
                hasSwamp |= biome == B_SWAMP || biome == B_MANGROVE_SWAMP;
                hasBeach |= biome == B_BEACH || biome == B_SNOWY_BEACH || biome == B_STONY_SHORE;
            }
        }

        boolean canPlaceDisks = hasWarmOcean || hasColdOcean || hasOcean || hasRiver || hasSwamp || hasBeach;
        if (canPlaceDisks) {
            placeDisks(level, baseX, baseZ, rng);
        }

        // Vanilla placements (AquaticPlacements):
        //   WARM/LUKEWARM/DEEP_LUKEWARM -> seagrass_warm(80, tall) + kelp_warm(80) + coral
        //   OCEAN(35)/DEEP_OCEAN(13)    -> seagrass_normal(48) / seagrass_deep(48)
        //   COLD/FROZEN oceans          -> seagrass_cold(32) + kelp_cold(120)
        if (hasWarmOcean) {
            placeSeagrass(chunk, baseX, baseZ, rng, 80, true);
            placeKelp(chunk, baseX, baseZ, rng, 80);
            placeCoral(chunk, baseX, baseZ, rng);
        }
        if (hasOcean) {
            placeSeagrass(chunk, baseX, baseZ, rng, 48, false);
        }
        if (hasColdOcean) {
            placeSeagrass(chunk, baseX, baseZ, rng, 32, false);
            placeKelp(chunk, baseX, baseZ, rng, 120);
        }
        if (hasRiver) {
            placeSeagrass(chunk, baseX, baseZ, rng, 48, false);
        }
        if (hasSwamp) {
            placeSeagrass(chunk, baseX, baseZ, rng, 64, false);
        }
    }

    private boolean isColdOcean(int biome) {
        return biome == B_COLD_OCEAN || biome == B_DEEP_COLD_OCEAN
            || biome == B_FROZEN_OCEAN || biome == B_DEEP_FROZEN_OCEAN;
    }

    private boolean isWarmOcean(int biome) {
        return biome == B_WARM_OCEAN || biome == B_LUKEWARM_OCEAN
            || biome == B_DEEP_LUKEWARM_OCEAN;
    }

    private boolean isOcean(int biome) {
        return isWarmOcean(biome) || isColdOcean(biome)
            || biome == B_OCEAN || biome == B_DEEP_OCEAN;
    }

    private void placeDisks(WorldGenLevel level, int baseX, int baseZ, RandomSource rng) {
        for (int i = 0; i < 3; i++) {
            int cx = baseX + rng.nextInt(16);
            int cz = baseZ + rng.nextInt(16);
            int cy = findWaterFloor(level, cx, cz);
            if (cy < -64) continue;
            if (level.getBlock(cx, cy + 1, cz) != water) continue;
            if (!isOpenWaterColumn(level, cx, cy, cz)) continue;
            int radius = 2 + rng.nextInt(5);
            placeDisk(level, cx, cy, cz, radius, 2, sand, sandstone,
                (b) -> b == dirt || b == grassBlock || b == sand || b == gravel);
        }

        for (int i = 0; i < 1; i++) {
            int cx = baseX + rng.nextInt(16);
            int cz = baseZ + rng.nextInt(16);
            int cy = findWaterFloor(level, cx, cz);
            if (cy < -64) continue;
            if (level.getBlock(cx, cy + 1, cz) != water) continue;
            if (!isOpenWaterColumn(level, cx, cy, cz)) continue;
            int radius = 2 + rng.nextInt(4);
            placeDisk(level, cx, cy, cz, radius, 2, gravel, gravel,
                (b) -> b == dirt || b == grassBlock || b == sand || b == gravel);
        }

        for (int i = 0; i < 1; i++) {
            int cx = baseX + rng.nextInt(16);
            int cz = baseZ + rng.nextInt(16);
            int cy = findWaterFloor(level, cx, cz);
            if (cy < -64) continue;
            if (level.getBlock(cx, cy + 1, cz) != water) continue;
            if (!isOpenWaterColumn(level, cx, cy, cz)) continue;
            int radius = 2 + rng.nextInt(2);
            placeDisk(level, cx, cy, cz, radius, 1, clay, clay,
                (b) -> b == dirt || b == clay || b == sand || b == gravel);
        }
    }

    // Water surface is at SEA_LEVEL-1: blocks y <= 62 are water, y == 63 (SEA_LEVEL) is air.
    // The open-water column must be checked up to the water top (y < SEA_LEVEL), not inclusive
    // of the air block at sea level, otherwise every ocean column is rejected.
    private boolean isOpenWaterColumn(WorldGenLevel level, int cx, int cy, int cz) {
        for (int y = cy + 1; y < SEA_LEVEL; y++) {
            if (level.getBlock(cx, y, cz) != water) return false;
        }
        return true;
    }

    private boolean isOpenWaterColumnChunk(Chunk chunk, int lx, int cy, int lz) {
        for (int y = cy + 1; y < SEA_LEVEL; y++) {
            if (chunk.getBlock(lx, y, lz) != water) return false;
        }
        return true;
    }

    private int findWaterFloor(WorldGenLevel level, int bx, int bz) {
        for (int y = 319; y >= -64; y--) {
            int block = level.getBlock(bx, y, bz);
            if (block != air && block != water) return y;
        }
        return -999;
    }

    private int findWaterFloor(Chunk chunk, int bx, int bz) {
        int lx = bx & 15;
        int lz = bz & 15;
        for (int y = 319; y >= -64; y--) {
            int block = chunk.getBlock(lx, y, lz);
            if (block != air && block != water) return y;
        }
        return -999;
    }

    private void placeDisk(WorldGenLevel level, int cx, int cy, int cz, int radius, int halfHeight,
                           int topBlock, int belowBlock,
                           java.util.function.IntPredicate targetTest) {
        int topY = cy + halfHeight;
        int bottomY = cy - halfHeight;
        int radiusSq = radius * radius;
        for (int dx = -radius; dx <= radius; dx++) {
            int bx = cx + dx;
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radiusSq) continue;
                int bz = cz + dz;
                boolean inRun = false;
                for (int y = topY; y >= bottomY; y--) {
                    int cur = level.getBlock(bx, y, bz);
                    if (targetTest.test(cur)) {
                        int place = topBlock;
                        if (level.getBlock(bx, y - 1, bz) == air) {
                            place = belowBlock;
                        }
                        level.setBlock(bx, y, bz, place);
                        inRun = true;
                    } else if (inRun) {
                        break;
                    }
                }
            }
        }
    }

    private void placeSeagrass(Chunk chunk, int baseX, int baseZ, RandomSource rng,
                                int count, boolean tall) {
        for (int i = 0; i < count; i++) {
            int bx = baseX + rng.nextInt(16);
            int bz = baseZ + rng.nextInt(16);
            int lx = bx & 15;
            int lz = bz & 15;
            int floorY = findWaterFloor(chunk, bx, bz);
            if (floorY < -64) continue;
            int y = floorY + 1;
            if (chunk.getBlock(lx, y, lz) != water) continue;
            if (!isOpenWaterColumnChunk(chunk, lx, floorY, lz)) continue;
            if (chunk.getBlock(lx, y + 1, lz) == water && tall) {
                chunk.setBlock(lx, y, lz, tallSeagrass);
            } else {
                chunk.setBlock(lx, y, lz, seagrass);
            }
        }
    }

    private void placeKelp(Chunk chunk, int baseX, int baseZ, RandomSource rng, int count) {
        for (int i = 0; i < count; i++) {
            int bx = baseX + rng.nextInt(16);
            int bz = baseZ + rng.nextInt(16);
            int lx = bx & 15;
            int lz = bz & 15;
            int floorY = findWaterFloor(chunk, bx, bz);
            if (floorY < -64) continue;
            int startY = floorY + 1;
            if (chunk.getBlock(lx, startY, lz) != water) continue;
            if (!isOpenWaterColumnChunk(chunk, lx, floorY, lz)) continue;
            int height = 1 + rng.nextInt(10);
            int topY = startY + height;
            if (topY > 319) topY = 319;
            for (int y = startY; y < topY; y++) {
                if (chunk.getBlock(lx, y, lz) != water) break;
                if (y == topY - 1) {
                    chunk.setBlock(lx, y, lz, kelp);
                } else {
                    chunk.setBlock(lx, y, lz, kelpPlant);
                }
            }
        }
    }

    private void placeCoral(Chunk chunk, int baseX, int baseZ, RandomSource rng) {
        int[] corals = {tubeCoral, brainCoral, bubbleCoral, fireCoral, hornCoral};
        int[] fans = {tubeCoralFan, brainCoralFan, bubbleCoralFan, fireCoralFan, hornCoralFan};

        for (int i = 0; i < 20; i++) {
            int bx = baseX + rng.nextInt(16);
            int bz = baseZ + rng.nextInt(16);
            int lx = bx & 15;
            int lz = bz & 15;
            int floorY = findWaterFloor(chunk, bx, bz);
            if (floorY < -64) continue;

            // Coral structure: coral block base + coral plant on top
            int y = floorY + 1;
            if (chunk.getBlock(lx, y, lz) != water) continue;
            if (!isOpenWaterColumnChunk(chunk, lx, floorY, lz)) continue;

            // Place a coral block on the floor
            if (chunk.getBlock(lx, floorY, lz) == stone || chunk.getBlock(lx, floorY, lz) == sand
                || chunk.getBlock(lx, floorY, lz) == gravel) {
                int c = rng.nextInt(3);
                if (c == 0) {
                    // Coral block
                    int blockIdx = rng.nextInt(5);
                    int cb = switch (blockIdx) {
                        case 0 -> BlockStateHelper.getDefault("tube_coral_block");
                        case 1 -> BlockStateHelper.getDefault("brain_coral_block");
                        case 2 -> BlockStateHelper.getDefault("bubble_coral_block");
                        case 3 -> BlockStateHelper.getDefault("fire_coral_block");
                        default -> BlockStateHelper.getDefault("horn_coral_block");
                    };
                    chunk.setBlock(lx, y, lz, cb);
                    if (rng.nextInt(3) == 0 && y + 1 <= 319 && chunk.getBlock(lx, y + 1, lz) == water) {
                        chunk.setBlock(lx, y + 1, lz, corals[rng.nextInt(5)]);
                    }
                } else if (c == 1) {
                    // Coral fan on floor
                    chunk.setBlock(lx, y, lz, fans[rng.nextInt(5)]);
                } else {
                    // Coral plant directly on floor
                    chunk.setBlock(lx, y, lz, corals[rng.nextInt(5)]);
                }
            }
        }
    }
}
