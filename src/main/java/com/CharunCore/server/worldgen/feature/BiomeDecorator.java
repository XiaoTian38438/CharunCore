package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public final class BiomeDecorator {

    private static final int AIR = 0;

    private final int vine;
    private final int lilyPad;
    private final int seaPickle;
    private final int sweetBerryBush;
    private final int packedIce, ice, snowBlock, blueIce;
    private final int water, grassBlock, stone, sand, gravel, dirt;
    private final int oakLog, oakLeaves;
    private final int deadBush;
    private final int brownMushroom, redMushroom;
    private final int brownMushroomBlock, redMushroomBlock, mushroomStem;
    private final int lava;

    // Biome IDs
    private static final int B_JUNGLE = 28;
    private static final int B_BAMBOO_JUNGLE = 1;
    private static final int B_SPARSE_JUNGLE = 50;
    private static final int B_SWAMP = 54;
    private static final int B_MANGROVE_SWAMP = 31;
    private static final int B_WARM_OCEAN = 58;
    private static final int B_LUKEWARM_OCEAN = 29;
    private static final int B_DEEP_LUKEWARM_OCEAN = 12;
    private static final int B_TAIGA = 55;
    private static final int B_OLD_GROWTH_SPRUCE_TAIGA = 38;
    private static final int B_OLD_GROWTH_PINE_TAIGA = 37;
    private static final int B_SNOWY_TAIGA = 48;
    private static final int B_ICE_SPIKES = 26;
    private static final int B_FROZEN_OCEAN = 22;
    private static final int B_DEEP_FROZEN_OCEAN = 11;
    private static final int B_RIVER = 41;
    private static final int B_FROZEN_RIVER = 24;
    private static final int B_DESERT = 14;
    private static final int B_BADLANDS = 0;
    private static final int B_ERODED_BADLANDS = 19;
    private static final int B_WOODED_BADLANDS = 64;
    private static final int B_MUSHROOM_FIELDS = 33;
    private static final int B_SNOWY_PLAINS = 46;
    private static final int B_SNOWY_SLOPES = 47;
    private static final int B_FROZEN_PEAKS = 23;
    private static final int B_JAGGED_PEAKS = 27;
    private static final int B_GROVE = 25;
    private static final int B_SNOWY_BEACH = 45;
    private static final int B_DARK_FOREST = 8;

    public BiomeDecorator() {
        this.vine = BlockStateHelper.getDefault("vine");
        this.lilyPad = BlockStateHelper.getDefault("lily_pad");
        this.seaPickle = BlockStateHelper.getDefault("sea_pickle");
        this.sweetBerryBush = BlockStateHelper.getDefault("sweet_berry_bush");
        this.packedIce = BlockStateHelper.getDefault("packed_ice");
        this.ice = BlockStateHelper.getDefault("ice");
        this.snowBlock = BlockStateHelper.getDefault("snow_block");
        this.blueIce = BlockStateHelper.getDefault("blue_ice");
        this.water = BlockStateHelper.getDefault("water");
        this.grassBlock = BlockStateHelper.getDefault("grass_block");
        this.stone = BlockStateHelper.getDefault("stone");
        this.sand = BlockStateHelper.getDefault("sand");
        this.gravel = BlockStateHelper.getDefault("gravel");
        this.dirt = BlockStateHelper.getDefault("dirt");
        this.oakLog = BlockStateHelper.getDefault("oak_log");
        this.oakLeaves = BlockStateHelper.getDefault("oak_leaves");
        this.deadBush = BlockStateHelper.getDefault("dead_bush");
        this.brownMushroom = BlockStateHelper.getDefault("brown_mushroom");
        this.redMushroom = BlockStateHelper.getDefault("red_mushroom");
        this.brownMushroomBlock = BlockStateHelper.getDefault("brown_mushroom_block");
        this.redMushroomBlock = BlockStateHelper.getDefault("red_mushroom_block");
        this.mushroomStem = BlockStateHelper.getDefault("mushroom_stem");
        this.lava = BlockStateHelper.getDefault("lava");
    }

    public void decorate(Chunk chunk, int chunkX, int chunkZ,
                          int[] topSolidY, int[] colBiome, RandomSource rng) {
        boolean hasJungle = false;
        boolean hasSwamp = false;
        boolean hasWarmOcean = false;
        boolean hasTaiga = false;
        boolean hasIceSpikes = false;
        boolean hasFrozen = false;
        int[] waterSurfaceY = new int[256];

        // Classify columns
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int colIdx = lz * 16 + lx;
                int biome = colBiome[colIdx];
                hasJungle |= biome == B_JUNGLE || biome == B_BAMBOO_JUNGLE || biome == B_SPARSE_JUNGLE;
                hasSwamp |= biome == B_SWAMP || biome == B_MANGROVE_SWAMP;
                hasWarmOcean |= biome == B_WARM_OCEAN || biome == B_LUKEWARM_OCEAN || biome == B_DEEP_LUKEWARM_OCEAN;
                hasTaiga |= biome == B_TAIGA || biome == B_OLD_GROWTH_SPRUCE_TAIGA || biome == B_OLD_GROWTH_PINE_TAIGA || biome == B_SNOWY_TAIGA;
                hasIceSpikes |= biome == B_ICE_SPIKES;
                hasFrozen |= isFrozenBiome(biome);

                // Find water surface for lily pads
                // B4: 记录"顶部水格"(其上为空气) —— 曾记录水底固体上方第一格,
                // 深水列荷叶被放到海底(水底荷叶)。自上而下扫到真实水面。
                int tsy = topSolidY[colIdx];
                waterSurfaceY[colIdx] = -999;
                if (tsy < 63) {
                    for (int wy = 63; wy > tsy; wy--) {
                        if (chunk.getBlock(lx, wy, lz) == water
                                && chunk.getBlock(lx, wy + 1, lz) == AIR) {
                            waterSurfaceY[colIdx] = wy;
                            break;
                        }
                    }
                }
            }
        }

        // 0) Freeze top layer (ice + snow in frozen biomes)
        if (hasFrozen) {
            freezeTopLayer(chunk, colBiome);
        }

        // 1) Vines in jungle
        if (hasJungle) {
            placeVines(chunk, chunkX, chunkZ, topSolidY, rng, false);
        }

        // 2) Vines in swamp
        if (hasSwamp) {
            placeVines(chunk, chunkX, chunkZ, topSolidY, rng, true);
        }

        // 3) Lily pads —— 原版仅沼泽/红树林沼泽水面(河流/海洋不放)
        if (hasSwamp) {
            placeLilyPads(chunk, chunkX, chunkZ, waterSurfaceY, rng);
        }

        // 4) Sea pickles in warm ocean
        if (hasWarmOcean) {
            placeSeaPickles(chunk, chunkX, chunkZ, colBiome, topSolidY, rng);
        }

        // 5) Sweet berry bushes in taiga
        if (hasTaiga) {
            placeSweetBerries(chunk, chunkX, chunkZ, topSolidY, colBiome, rng);
        }

        // 6) Ice spikes
        if (hasIceSpikes) {
            placeIceSpikes(chunk, chunkX, chunkZ, topSolidY, colBiome, rng);
        }

        // 7) Icebergs in frozen oceans
        boolean hasFrozenOcean = false;
        for (int i = 0; i < 256; i++) {
            int b = colBiome[i];
            if (b == B_FROZEN_OCEAN || b == B_DEEP_FROZEN_OCEAN) {
                hasFrozenOcean = true;
                break;
            }
        }
        if (hasFrozenOcean && rng.nextInt(4) == 0) {
            placeIceberg(chunk, chunkX, chunkZ, topSolidY, colBiome, rng);
        }

        // 8) Lava lakes (very rare, 1/100)
        if (rng.nextInt(100) == 0) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            int tsy = topSolidY[colIdx];
            if (tsy > 60 && tsy < 319) {
                placeLavaLake(chunk, lx, tsy + 1, lz, rng);
            }
        }

        // 9) Huge mushrooms in mushroom fields and swamp
        if (hasAnyMushroomOrSwamp(colBiome)) {
            for (int i = 0; i < 2; i++) {
                int lx = rng.nextInt(16);
                int lz = rng.nextInt(16);
                int colIdx = lz * 16 + lx;
                int biome = colBiome[colIdx];
                if (biome != B_MUSHROOM_FIELDS && biome != B_SWAMP && biome != B_DARK_FOREST) continue;
                int tsy = topSolidY[colIdx];
                if (tsy < 63) continue;
                int bx = chunkX * 16 + lx;
                int bz = chunkZ * 16 + lz;
                placeHugeMushroom(chunk, lx, tsy + 1, lz, rng);
            }
        }
    }

    private boolean isFrozenBiome(int biome) {
        return biome == B_ICE_SPIKES || biome == B_SNOWY_PLAINS || biome == B_SNOWY_TAIGA
            || biome == B_SNOWY_SLOPES || biome == B_FROZEN_PEAKS || biome == B_JAGGED_PEAKS
            || biome == B_FROZEN_OCEAN || biome == B_DEEP_FROZEN_OCEAN
            || biome == B_FROZEN_RIVER || biome == B_GROVE || biome == B_SNOWY_BEACH;
    }

    private void freezeTopLayer(Chunk chunk, int[] colBiome) {
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int colIdx = lz * 16 + lx;
                int biome = colBiome[colIdx];
                if (!isFrozenBiome(biome)) continue;

                // Scan from top down for water
                for (int y = 319; y >= -64; y--) {
                    int block = chunk.getBlock(lx, y, lz);
                    if (block == water) {
                        // Check if the water has air above (surface water)
                        if (y + 1 <= 319 && chunk.getBlock(lx, y + 1, lz) == 0) {
                            // Replace water with ice
                            chunk.setBlock(lx, y, lz, ice);
                            // Place snow on top if air
                            if (y + 1 <= 319 && chunk.getBlock(lx, y + 1, lz) == 0) {
                                chunk.setBlock(lx, y + 1, lz, snowBlock);
                            }
                            // In very cold biomes, also freeze one layer below
                            if (biome == B_FROZEN_OCEAN || biome == B_DEEP_FROZEN_OCEAN
                                || biome == B_ICE_SPIKES || biome == B_FROZEN_PEAKS) {
                                if (y - 1 >= -64 && chunk.getBlock(lx, y - 1, lz) == water) {
                                    chunk.setBlock(lx, y - 1, lz, packedIce);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    // === VINES ===
    private void placeVines(Chunk chunk, int chunkX, int chunkZ,
                             int[] topSolidY, RandomSource rng, boolean swampMode) {
        int attempts = swampMode ? 30 : 50;
        for (int i = 0; i < attempts; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            int tsy = topSolidY[colIdx];
            if (tsy < 63) continue;

            int height = swampMode ? 3 + rng.nextInt(5) : 4 + rng.nextInt(8);
            int y = tsy + rng.nextInt(Math.max(1, height));

            // Place vine on the side of a solid block
            // Check horizontal neighbors for solid faces
            int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
            for (int[] d : dirs) {
                int nx = lx + d[0];
                int nz = lz + d[1];
                if (nx < 0 || nx > 15 || nz < 0 || nz > 15) continue;
                if (isLogOrLeaves(chunk.getBlock(nx, y, nz)) || isFullSolid(chunk.getBlock(nx, y, nz))) {
                    if (chunk.getBlock(lx, y, lz) == AIR) {
                        chunk.setBlock(lx, y, lz, vine);
                        // Grow vine downward
                        for (int v = 1; v <= 3; v++) {
                            int vy = y - v;
                            if (vy < -64) break;
                            if (chunk.getBlock(lx, vy, lz) != AIR) break;
                            chunk.setBlock(lx, vy, lz, vine);
                        }
                    }
                    break;
                }
            }
        }
    }

    // === LILY PADS ===
    private void placeLilyPads(Chunk chunk, int chunkX, int chunkZ,
                                int[] waterSurfaceY, RandomSource rng) {
        for (int i = 0; i < 10; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            int wy = waterSurfaceY[colIdx];
            if (wy < -64) continue;
            // 荷叶放在顶部水格之上(空气格), 且必须还是空气(冰面/已有方块不放)
            if (chunk.getBlock(lx, wy + 1, lz) == AIR) {
                chunk.setBlock(lx, wy + 1, lz, lilyPad);
            }
        }
    }

    // === SEA PICKLES ===
    private void placeSeaPickles(Chunk chunk, int chunkX, int chunkZ,
                                  int[] colBiome, int[] topSolidY, RandomSource rng) {
        for (int i = 0; i < 20; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            int biome = colBiome[colIdx];
            // Only in warm ocean biomes
            if (biome != B_WARM_OCEAN && biome != B_LUKEWARM_OCEAN && biome != B_DEEP_LUKEWARM_OCEAN) continue;

            int tsy = topSolidY[colIdx];
            if (tsy < -64) continue;
            int y = tsy + 1;

            // Check underwater, on solid floor
            if (chunk.getBlock(lx, y, lz) != water) continue;
            int floor = chunk.getBlock(lx, tsy, lz);
            if (floor == sand || floor == gravel || floor == stone) {
                int count = 1 + rng.nextInt(3); // 1-4 pickles
                for (int p = 0; p < count && y + p <= 319; p++) {
                    if (chunk.getBlock(lx, y + p, lz) != water) break;
                    chunk.setBlock(lx, y + p, lz, seaPickle);
                }
            }
        }
    }

    // === SWEET BERRIES ===
    private void placeSweetBerries(Chunk chunk, int chunkX, int chunkZ,
                                    int[] topSolidY, int[] colBiome, RandomSource rng) {
        for (int i = 0; i < 10; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            int tsy = topSolidY[colIdx];
            if (tsy < 63) continue;
            int y = tsy + 1;
            if (chunk.getBlock(lx, y, lz) != AIR) continue;
            int surface = chunk.getBlock(lx, tsy, lz);
            if (surface == grassBlock || surface == dirt) {
                chunk.setBlock(lx, y, lz, sweetBerryBush);
            }
        }
    }

    // === ICE SPIKES ===
    private void placeIceSpikes(Chunk chunk, int chunkX, int chunkZ,
                                 int[] topSolidY,
                                 int[] colBiome, RandomSource rng) {
        for (int i = 0; i < 3; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            if (colBiome[colIdx] != B_ICE_SPIKES) continue;
            int tsy = topSolidY[colIdx];
            if (tsy < 63) continue;

            // Large spike
            int spikeHeight = 6 + rng.nextInt(14);
            int radius = 1 + rng.nextInt(3);
            placeIceSpike(chunk, lx, tsy, lz, spikeHeight, radius, rng);
        }

        // Small ice spikes
        for (int i = 0; i < 8; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            if (colBiome[colIdx] != B_ICE_SPIKES) continue;
            int tsy = topSolidY[colIdx];
            if (tsy < 63) continue;

            int spikeHeight = 1 + rng.nextInt(4);
            placeSmallIceSpike(chunk, lx, tsy, lz, spikeHeight);
        }
    }

    private void placeIceSpike(Chunk chunk, int lx, int baseY, int lz, int height, int radius, RandomSource rng) {
        int topY = baseY + height;
        for (int y = baseY + 1; y <= topY; y++) {
            int layerRadius = radius;
            // taper at top
            if (y > topY - 3) layerRadius = Math.max(1, radius - (topY - y) - 1);
            if (y == topY) layerRadius = 1;

            for (int dx = -layerRadius; dx <= layerRadius; dx++) {
                for (int dz = -layerRadius; dz <= layerRadius; dz++) {
                    int bx = lx + dx;
                    int bz = lz + dz;
                    if (bx < 0 || bx > 15 || bz < 0 || bz > 15) continue;
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist > layerRadius + 0.5) continue;
                    // Edge blocks have chance to be air (roughness)
                    if (dist > layerRadius - 0.5 && rng.nextDouble() < 0.3) continue;
                    int cur = chunk.getBlock(bx, y, bz);
                    if (cur == AIR || cur == water || cur == ice || cur == snowBlock) {
                        chunk.setBlock(bx, y, bz, packedIce);
                    }
                }
            }
        }
    }

    private void placeSmallIceSpike(Chunk chunk, int lx, int baseY, int lz, int height) {
        for (int y = 0; y < height; y++) {
            int by = baseY + 1 + y;
            if (chunk.getBlock(lx, by, lz) != AIR) break;
            chunk.setBlock(lx, by, lz, packedIce);
        }
    }

    // === ICEBERGS ===
    private void placeIceberg(Chunk chunk, int chunkX, int chunkZ,
                               int[] topSolidY, int[] colBiome, RandomSource rng) {
        for (int attempt = 0; attempt < 3; attempt++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            if (colBiome[colIdx] != B_FROZEN_OCEAN && colBiome[colIdx] != B_DEEP_FROZEN_OCEAN) continue;
            int tsy = topSolidY[colIdx];
            if (tsy < 50) continue;

            int iceRadius = 2 + rng.nextInt(4);
            int iceHeight = 2 + rng.nextInt(3);
            int bx = chunkX * 16 + lx;
            int bz = chunkZ * 16 + lz;

            for (int dx = -iceRadius; dx <= iceRadius; dx++) {
                for (int dz = -iceRadius; dz <= iceRadius; dz++) {
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist > iceRadius + 0.5) continue;
                    int h = (int)(iceHeight * (1.0 - dist / (iceRadius + 1)));
                    h = Math.max(1, h + rng.nextInt(2) - 1);
                    for (int dy = 0; dy < h; dy++) {
                        int nx = (bx + dx) & 15;
                        int nz = (bz + dz) & 15;
                        int ny = tsy + 1 + dy;
                        if (ny > 319 || ny < -64) continue;
                        int cur = chunk.getBlock(nx, ny, nz);
                        if (cur == water || cur == ice || cur == 0) {
                            if (dy == 0 && dx == 0 && dz == 0 && rng.nextInt(4) == 0) {
                                chunk.setBlock(nx, ny, nz, blueIce);
                            } else {
                                chunk.setBlock(nx, ny, nz, packedIce);
                            }
                        }
                    }
                }
            }
            break;
        }
    }

    // === LAVA LAKES ===
    private void placeLavaLake(Chunk chunk, int lx, int y, int lz, RandomSource rng) {
        int radius = 1 + rng.nextInt(2);
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int bx = lx + dx;
                int bz = lz + dz;
                if (bx < 0 || bx > 15 || bz < 0 || bz > 15) continue;
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > radius + 0.5) continue;
                if (dist > radius - 0.3) {
                    int cur = chunk.getBlock(bx, y, bz);
                    if (cur == 0) chunk.setBlock(bx, y, bz, stone);
                } else {
                    if (chunk.getBlock(bx, y - 1, bz) != 0) {
                        chunk.setBlock(bx, y, bz, lava);
                        // Air above lava
                        if (chunk.getBlock(bx, y + 1, bz) == 0) {
                            // leave air
                        }
                    }
                }
            }
        }
    }

    // === HUGE MUSHROOMS ===
    private void placeHugeMushroom(Chunk chunk, int lx, int y, int lz, RandomSource rng) {
        if (y > 319) return;
        if (chunk.getBlock(lx, y, lz) != 0) return;

        boolean redType = rng.nextBoolean();
        int capBlock = redType ? redMushroomBlock : brownMushroomBlock;
        int height = 3 + rng.nextInt(3); // 3-5

        // Stem
        for (int dy = 0; dy < height; dy++) {
            int by = y + dy;
            if (by > 319) break;
            if (chunk.getBlock(lx, by, lz) != 0 && dy > 0) break;
            chunk.setBlock(lx, by, lz, mushroomStem);
        }

        // Cap
        int capY = y + height;
        int capRadius = 2;
        for (int dx = -capRadius; dx <= capRadius; dx++) {
            for (int dz = -capRadius; dz <= capRadius; dz++) {
                for (int dy = 0; dy <= 1; dy++) {
                    int bx = lx + dx;
                    int bz = lz + dz;
                    if (bx < 0 || bx > 15 || bz < 0 || bz > 15) continue;
                    int by = capY + dy;
                    if (by > 319) continue;
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist > capRadius + 0.5) continue;
                    if (dist > capRadius - 0.3 && dy == 0) continue; // cap underside is empty
                    if (chunk.getBlock(bx, by, bz) != 0) continue;
                    chunk.setBlock(bx, by, bz, capBlock);
                }
            }
        }
    }

    // === UTILITY ===

    private boolean hasAnyRiver(int[] colBiome) {
        for (int i = 0; i < 256; i++) {
            int b = colBiome[i];
            if (b == B_RIVER || b == B_FROZEN_RIVER) return true;
        }
        return false;
    }

    private boolean isLogOrLeaves(int block) {
        return block == oakLog || block == oakLeaves
            || block == BlockStateHelper.getDefault("spruce_log")
            || block == BlockStateHelper.getDefault("spruce_leaves")
            || block == BlockStateHelper.getDefault("birch_log")
            || block == BlockStateHelper.getDefault("birch_leaves")
            || block == BlockStateHelper.getDefault("dark_oak_log")
            || block == BlockStateHelper.getDefault("dark_oak_leaves")
            || block == BlockStateHelper.getDefault("jungle_log")
            || block == BlockStateHelper.getDefault("jungle_leaves");
    }

    private boolean isFullSolid(int block) {
        return block == stone || block == dirt || block == grassBlock
            || block == sand || block == gravel
            || block == BlockStateHelper.getDefault("cobblestone")
            || block == BlockStateHelper.getDefault("mossy_cobblestone")
            || block == BlockStateHelper.getDefault("deepslate");
    }

    private boolean hasAnyMushroomOrSwamp(int[] colBiome) {
        for (int i = 0; i < 256; i++) {
            int b = colBiome[i];
            if (b == B_MUSHROOM_FIELDS || b == B_SWAMP || b == B_MANGROVE_SWAMP || b == B_DARK_FOREST) return true;
        }
        return false;
    }
}
