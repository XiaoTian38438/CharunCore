package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public final class SurfaceDecorator {

    private static final int AIR = 0;

    private final int cactus, sugarCane, deadBush;
    private final int pumpkinId, melonId;
    private final int ice, packedIce, snowBlock;
    private final int stone, cobblestone, mossyCobblestone;
    private final int sand, redSand, grassBlock, dirt, coarseDirt, podzol;
    private final int water;
    private final int bamboo;
    private final int brownMushroom, redMushroom;
    private final int bambooShoot;
    private final int air;

    public SurfaceDecorator() {
        this.cactus = BlockStateHelper.getDefault("cactus");
        this.sugarCane = BlockStateHelper.getDefault("sugar_cane");
        this.deadBush = BlockStateHelper.getDefault("dead_bush");
        this.pumpkinId = BlockStateHelper.getDefault("pumpkin");
        this.melonId = BlockStateHelper.getDefault("melon");
        this.ice = BlockStateHelper.getDefault("ice");
        this.packedIce = BlockStateHelper.getDefault("packed_ice");
        this.snowBlock = BlockStateHelper.getDefault("snow_block");
        this.stone = BlockStateHelper.getDefault("stone");
        this.cobblestone = BlockStateHelper.getDefault("cobblestone");
        this.mossyCobblestone = BlockStateHelper.getDefault("mossy_cobblestone");
        this.sand = BlockStateHelper.getDefault("sand");
        this.redSand = BlockStateHelper.getDefault("red_sand");
        this.grassBlock = BlockStateHelper.getDefault("grass_block");
        this.dirt = BlockStateHelper.getDefault("dirt");
        this.coarseDirt = BlockStateHelper.getDefault("coarse_dirt");
        this.podzol = BlockStateHelper.getDefault("podzol");
        this.water = BlockStateHelper.getDefault("water");
        this.bamboo = BlockStateHelper.getDefault("bamboo");
        this.bambooShoot = BlockStateHelper.getDefault("bamboo_sapling");
        this.brownMushroom = BlockStateHelper.getDefault("brown_mushroom");
        this.redMushroom = BlockStateHelper.getDefault("red_mushroom");
        this.air = AIR;
    }

    public void decorate(Chunk chunk, int chunkX, int chunkZ,
                          int[] topSolidY, int[] colBiome, RandomSource rng) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        // Per-chunk cactus/sugar cane/dead bush count attempts
        boolean[] desertBadlands = new boolean[256];
        boolean[] hasWater = new boolean[256];
        boolean[] isForest = new boolean[256];
        boolean[] isJungle = new boolean[256];
        boolean[] isPlains = new boolean[256];
        boolean[] isSwamp = new boolean[256];
        boolean[] isFrozenOcean = new boolean[256];
        boolean[] isTaiga = new boolean[256];
        boolean[] isBambooJungle = new boolean[256];
        boolean[] isIceSpikes = new boolean[256];
        boolean[] isMushroom = new boolean[256];

        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int colIdx = lz * 16 + lx;
                int biome = colBiome[colIdx];
                desertBadlands[colIdx] = biome == 0 || biome == 19 || biome == 14 || biome == 64;
                isForest[colIdx] = biome == 21 || biome == 20 || biome == 36 || biome == 38 || biome == 37 || biome == 8;
                isJungle[colIdx] = biome == 28 || biome == 50;
                isBambooJungle[colIdx] = biome == 1;
                isPlains[colIdx] = biome == 40 || biome == 53;
                isSwamp[colIdx] = biome == 54;
                isFrozenOcean[colIdx] = biome == 22 || biome == 11;
                isTaiga[colIdx] = biome == 55 || biome == 48 || biome == 38 || biome == 37;
                isIceSpikes[colIdx] = biome == 26;
                isMushroom[colIdx] = biome == 33;
                int tsy = topSolidY[colIdx];
                if (tsy < -60) continue;
                int bx = baseX + lx;
                int bz = baseZ + lz;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        int wx = bx + dy;
                        int wz = bz + dz;
                        int wlx = wx & 15;
                        int wlz = wz & 15;
                        int wy = tsy;
                        if (chunk.getBlock(wlx, wy, wlz) == water) {
                            hasWater[colIdx] = true;
                            break;
                        }
                    }
                    if (hasWater[colIdx]) break;
                }
            }
        }

        // 1) Cactus: 10 attempts in desert/badlands
        for (int i = 0; i < 10; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            if (!desertBadlands[colIdx]) continue;
            int tsy = topSolidY[colIdx];
            if (tsy < 63) continue;
            int bx = baseX + lx;
            int bz = baseZ + lz;
            placeCactus(chunk, bx, tsy + 1, bz, rng);
        }

        // 2) Dead bush: 2 attempts in desert/badlands
        for (int i = 0; i < 2; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            if (!desertBadlands[colIdx]) continue;
            int tsy = topSolidY[colIdx];
            if (tsy < 63) continue;
            int bx = baseX + lx;
            int bz = baseZ + lz;
            placeDeadBush(chunk, bx, tsy + 1, bz, chunk.getBlock(lx, tsy, lz));
        }

        // 3) Sugar cane: 10 attempts near water (desert, plains, swamp, beach)
        for (int i = 0; i < 10; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            int biome = colBiome[colIdx];
            boolean canSugarCane = desertBadlands[colIdx] || isPlains[colIdx]
                || isSwamp[colIdx] || biome == 3 || biome == 45
                || biome == 29 || biome == 6 || biome == 22
                || biome == 9 || biome == 11 || biome == 12 || biome == 58;
            if (!canSugarCane) continue;
            int tsy = topSolidY[colIdx];
            if (tsy < 63) continue;
            if (!hasWater[colIdx]) continue;
            int bx = baseX + lx;
            int bz = baseZ + lz;
            placeSugarCane(chunk, bx, tsy + 1, bz, rng);
        }

        // 4) Pumpkin: 1/32 chance in plains/forest/taiga/meadow/etc
        if (rng.nextInt(32) == 0) {
            for (int i = 0; i < 64; i++) {
                int lx = rng.nextInt(16);
                int lz = rng.nextInt(16);
                int colIdx = lz * 16 + lx;
                boolean canPumpkin = isPlains[colIdx] || isForest[colIdx]
                    || isTaiga[colIdx] || biomeCheck(colBiome[colIdx], 32, 42, 43, 24, 63);
                if (!canPumpkin) continue;
                int tsy = topSolidY[colIdx];
                if (tsy < 63) continue;
                int bx = baseX + lx;
                int bz = baseZ + lz;
                if (chunk.getBlock(lx, tsy, lz) == grassBlock) {
                    int py = tsy + 1;
                    if (chunk.getBlock(lx, py, lz) == air) {
                        chunk.setBlock(lx, py, lz, pumpkinId);
                        break;
                    }
                }
            }
        }

        // 5) Melon: 1/32 chance in jungle
        if (rng.nextInt(32) == 0) {
            for (int i = 0; i < 64; i++) {
                int lx = rng.nextInt(16);
                int lz = rng.nextInt(16);
                int colIdx = lz * 16 + lx;
                if (!isJungle[colIdx]) continue;
                int tsy = topSolidY[colIdx];
                if (tsy < 63) continue;
                int bx = baseX + lx;
                int bz = baseZ + lz;
                if (chunk.getBlock(lx, tsy, lz) == grassBlock) {
                    int py = tsy + 1;
                    if (chunk.getBlock(lx, py, lz) == air) {
                        chunk.setBlock(lx, py, lz, melonId);
                        break;
                    }
                }
            }
        }

        // 6) Forest rocks: 2 attempts in forest
        for (int i = 0; i < 2; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            if (!isForest[colIdx]) continue;
            int tsy = topSolidY[colIdx];
            if (tsy < 63) continue;
            int bx = baseX + lx;
            int bz = baseZ + lz;
            placeForestRock(chunk, bx, tsy + 1, bz, rng);
        }

        // 7) Ice patches in frozen oceans and ice spikes
        for (int i = 0; i < 3; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            if (!isFrozenOcean[colIdx] && !isIceSpikes[colIdx]) continue;
            int tsy = topSolidY[colIdx];
            if (tsy < 63) continue;
            int bx = baseX + lx;
            int bz = baseZ + lz;
            placeIcePatch(chunk, bx, tsy + 1, bz, rng, isIceSpikes[colIdx]);
        }

        // 8) Blue ice in frozen oceans: 1 per chunk
        if (isAnyFrozenOcean(colBiome)) {
            for (int i = 0; i < 1; i++) {
                int lx = rng.nextInt(16);
                int lz = rng.nextInt(16);
                int colIdx = lz * 16 + lx;
                if (!isFrozenOcean[colIdx]) continue;
                int tsy = topSolidY[colIdx];
                if (tsy < 63) continue;
                int bx = baseX + lx;
                int bz = baseZ + lz;
                // Place blue_ice block on ocean floor
                if (chunk.getBlock(lx, tsy, lz) == ice || chunk.getBlock(lx, tsy, lz) == packedIce) {
                    int blueIceId = BlockStateHelper.getDefault("blue_ice");
                    chunk.setBlock(lx, tsy, lz, blueIceId);
                }
            }
        }

        // 9) Bamboo: 10 attempts in bamboo jungle
        for (int i = 0; i < 10; i++) {
            int lx = rng.nextInt(16);
            int lz = rng.nextInt(16);
            int colIdx = lz * 16 + lx;
            if (!isBambooJungle[colIdx]) continue;
            int tsy = topSolidY[colIdx];
            if (tsy < 63) continue;
            int bx = baseX + lx;
            int bz = baseZ + lz;
            placeBamboo(chunk, bx, tsy + 1, bz, rng);
        }

        // 10) Swamp vegetation: mushrooms, dead bushes
        if (isAnySwamp(colBiome)) {
            for (int i = 0; i < 8; i++) {
                int lx = rng.nextInt(16);
                int lz = rng.nextInt(16);
                int colIdx = lz * 16 + lx;
                if (!isSwamp[colIdx]) continue;
                int tsy = topSolidY[colIdx];
                if (tsy < 60) continue;
                int bx = baseX + lx;
                int bz = baseZ + lz;
                if (rng.nextInt(2) == 0) {
                    placeDeadBush(chunk, bx, tsy + 1, bz, chunk.getBlock(lx, tsy, lz));
                } else {
                    int mushroom = rng.nextInt(2) == 0 ? brownMushroom : redMushroom;
                    int my = tsy + 1;
                    if (chunk.getBlock(lx, my, lz) == air && chunk.getBlock(lx, my - 1, lz) != water) {
                        chunk.setBlock(lx, my, lz, mushroom);
                    }
                }
            }
        }

        // 11) Mushroom fields: huge mushrooms + mycelium surface
        if (isAnyMushroom(colBiome)) {
            for (int i = 0; i < 5; i++) {
                int lx = rng.nextInt(16);
                int lz = rng.nextInt(16);
                int colIdx = lz * 16 + lx;
                if (!isMushroom[colIdx]) continue;
                int tsy = topSolidY[colIdx];
                if (tsy < 63) continue;
                int bx = baseX + lx;
                int bz = baseZ + lz;
                int mushroom = rng.nextInt(2) == 0 ? brownMushroom : redMushroom;
                int my = tsy + 1;
                if (chunk.getBlock(lx, my, lz) == air) {
                    chunk.setBlock(lx, my, lz, mushroom);
                }
            }
            // Replace grass block with mycelium on surface
            for (int lx = 0; lx < 16; lx++) {
                for (int lz = 0; lz < 16; lz++) {
                    int colIdx = lz * 16 + lx;
                    if (!isMushroom[colIdx]) continue;
                    int tsy = topSolidY[colIdx];
                    if (tsy < 63) continue;
                    if (chunk.getBlock(lx, tsy, lz) == grassBlock) {
                        int myceliumId = BlockStateHelper.getDefault("mycelium");
                        chunk.setBlock(lx, tsy, lz, myceliumId);
                    }
                }
            }
        }
    }

    private void placeCactus(Chunk chunk, int bx, int by, int bz, RandomSource rng) {
        int lx = bx & 15;
        int lz = bz & 15;
        int height = 1 + rng.nextInt(3); // 1-3 blocks tall
        for (int y = 0; y < height; y++) {
            int py = by + y;
            if (py > 319) break;
            if (chunk.getBlock(lx, py, lz) != air) break;
            // Check 4 sides are air (cactus can't touch blocks)
            boolean blocked = false;
            for (int d = -1; d <= 1; d += 2) {
                if ((chunk.getBlock((bx + d) & 15, py, lz) & 0xFFFF) != 0) blocked = true;
                if ((chunk.getBlock(lx, py, (bz + d) & 15) & 0xFFFF) != 0) blocked = true;
            }
            if (blocked) break;
            chunk.setBlock(lx, py, lz, cactus);
        }
    }

    private void placeDeadBush(Chunk chunk, int bx, int by, int bz, int baseBlock) {
        int lx = bx & 15;
        int lz = bz & 15;
        if (by > 319) return;
        if (chunk.getBlock(lx, by, lz) != air) return;
        boolean sandLike = baseBlock == sand || baseBlock == redSand
            || baseBlock == BlockStateHelper.getDefault("terracotta")
            || baseBlock == BlockStateHelper.getDefault("white_terracotta")
            || baseBlock == BlockStateHelper.getDefault("orange_terracotta")
            || baseBlock == BlockStateHelper.getDefault("red_sandstone")
            || baseBlock == BlockStateHelper.getDefault("sandstone");
        if (!sandLike) return;
        chunk.setBlock(lx, by, lz, deadBush);
    }

    private void placeSugarCane(Chunk chunk, int bx, int by, int bz, RandomSource rng) {
        int lx = bx & 15;
        int lz = bz & 15;
        int height = 1 + rng.nextInt(3); // 1-3 blocks tall
        for (int y = 0; y < height; y++) {
            int py = by + y;
            if (py > 319) break;
            if (chunk.getBlock(lx, py, lz) != air) break;
            chunk.setBlock(lx, py, lz, sugarCane);
        }
    }

    private void placeForestRock(Chunk chunk, int bx, int by, int bz, RandomSource rng) {
        int lx = bx & 15;
        int lz = bz & 15;
        if (chunk.getBlock(lx, by, lz) != air) return;
        // Mossy cobblestone boulder (small)
        int rockType = rng.nextInt(3) == 0 ? mossyCobblestone : cobblestone;
        chunk.setBlock(lx, by, lz, rockType);
        // Occasionally place extra blocks around it
        if (rng.nextInt(2) == 0) {
            int dx = rng.nextInt(3) - 1;
            int dz = rng.nextInt(3) - 1;
            if (dx == 0 && dz == 0) return;
            int nlx = (bx + dx) & 15;
            int nlz = (bz + dz) & 15;
            if (chunk.getBlock(nlx, by, nlz) == air
                && chunk.getBlock(nlx, by - 1, nlz) != 0) {
                chunk.setBlock(nlx, by, nlz, rockType);
            }
        }
    }

    private void placeIcePatch(Chunk chunk, int bx, int by, int bz, RandomSource rng, boolean iceSpikes) {
        int lx = bx & 15;
        int lz = bz & 15;
        if (chunk.getBlock(lx, by, lz) != air) return;
        if (iceSpikes) {
            // In ice spikes biome, place packed ice spikes
            int spikeHeight = 1 + rng.nextInt(4);
            for (int y = 0; y < spikeHeight; y++) {
                int py = by + y;
                if (py > 319) break;
                if (chunk.getBlock(lx, py, lz) != air) break;
                chunk.setBlock(lx, py, lz, packedIce);
            }
        } else {
            // Small ice/snow patches on frozen ocean surface
            if (rng.nextInt(2) == 0) {
                chunk.setBlock(lx, by, lz, packedIce);
                if (rng.nextInt(3) == 0) {
                    chunk.setBlock(lx, by + 1, lz, snowBlock);
                }
            } else {
                chunk.setBlock(lx, by, lz, snowBlock);
            }
        }
    }

    private void placeBamboo(Chunk chunk, int bx, int by, int bz, RandomSource rng) {
        int lx = bx & 15;
        int lz = bz & 15;
        int height = 3 + rng.nextInt(4); // 3-6 blocks tall
        // Bamboo has shoot at base
        chunk.setBlock(lx, by, lz, bambooShoot);
        for (int y = 1; y < height; y++) {
            int py = by + y;
            if (py > 319) break;
            if (chunk.getBlock(lx, py, lz) != air) break;
            chunk.setBlock(lx, py, lz, bamboo);
        }
    }

    private boolean biomeCheck(int biome, int... ids) {
        for (int id : ids) {
            if (biome == id) return true;
        }
        return false;
    }

    private boolean isAnyFrozenOcean(int[] colBiome) {
        for (int i = 0; i < 256; i++) {
            int b = colBiome[i];
            if (b == 22 || b == 11) return true;
        }
        return false;
    }

    private boolean isAnySwamp(int[] colBiome) {
        for (int i = 0; i < 256; i++) {
            if (colBiome[i] == 54) return true;
        }
        return false;
    }

    private boolean isAnyMushroom(int[] colBiome) {
        for (int i = 0; i < 256; i++) {
            if (colBiome[i] == 33) return true;
        }
        return false;
    }
}
