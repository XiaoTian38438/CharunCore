package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

public class SimplePatchFeature {

    private static final int SHORT_GRASS = BlockStateHelper.getDefault("short_grass");
    private static final int FERN = BlockStateHelper.getDefault("fern");
    private static final int DANDELION = BlockStateHelper.getDefault("dandelion");
    private static final int POPPY = BlockStateHelper.getDefault("poppy");
    private static final int CORNFLOWER = BlockStateHelper.getDefault("cornflower");
    private static final int BLUE_ORCHID = BlockStateHelper.getDefault("blue_orchid");
    private static final int ALLIUM = BlockStateHelper.getDefault("allium");
    private static final int OXEYE_DAISY = BlockStateHelper.getDefault("oxeye_daisy");
    private static final int AZURE_BLUET = BlockStateHelper.getDefault("azure_bluet");
    private static final int RED_TULIP = BlockStateHelper.getDefault("red_tulip");
    private static final int ORANGE_TULIP = BlockStateHelper.getDefault("orange_tulip");
    private static final int WHITE_TULIP = BlockStateHelper.getDefault("white_tulip");
    private static final int PINK_TULIP = BlockStateHelper.getDefault("pink_tulip");
    private static final int LILY_OF_THE_VALLEY = BlockStateHelper.getDefault("lily_of_the_valley");
    private static final int TALL_GRASS = BlockStateHelper.getDefault("tall_grass");
    private static final int LARGE_FERN = BlockStateHelper.getDefault("large_fern");
    private static final int DEAD_BUSH = BlockStateHelper.getDefault("dead_bush");
    private static final int RED_MUSHROOM = BlockStateHelper.getDefault("red_mushroom");
    private static final int BROWN_MUSHROOM = BlockStateHelper.getDefault("brown_mushroom");
    private static final int SWEET_BERRY_BUSH = BlockStateHelper.getDefault("sweet_berry_bush");
    private static final int SUGAR_CANE = BlockStateHelper.getDefault("sugar_cane");
    private static final int GRASS_BLOCK = BlockStateHelper.getDefault("grass_block");
    private static final int SAND = BlockStateHelper.getDefault("sand");
    private static final int WATER = BlockStateHelper.getDefault("water");

    public static void placeGrassPatch(WorldGenLevel level, int blockX, int blockY, int blockZ,
                                        int radius, int count, RandomSource random, boolean fern) {
        for (int i = 0; i < count; i++) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            int x = blockX + dx;
            int z = blockZ + dz;

            for (int yOff = -1; yOff <= 1; yOff++) {
                int y = blockY + yOff;
                if (y < 0 || y > 318) continue;
                if (level.getBlock(x, y - 1, z) != GRASS_BLOCK) continue;
                if (level.getBlock(x, y, z) != 0) continue;
                int plant = fern ? FERN : SHORT_GRASS;
                level.setBlock(x, y, z, plant);
                if (!fern && random.nextInt(8) == 0 && y + 1 <= 319 && level.getBlock(x, y + 1, z) == 0) {
                    level.setBlock(x, y, z, TALL_GRASS);
                    level.setBlock(x, y + 1, z, TALL_GRASS);
                }
                if (fern && random.nextInt(6) == 0 && y + 1 <= 319 && level.getBlock(x, y + 1, z) == 0) {
                    level.setBlock(x, y, z, LARGE_FERN);
                    level.setBlock(x, y + 1, z, LARGE_FERN);
                }
                break;
            }
        }
    }

    public static void placeBiomeDecoration(WorldGenLevel level, int blockX, int blockY, int blockZ,
                                              int radius, int count, RandomSource random, int biome) {
        for (int i = 0; i < count; i++) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            int x = blockX + dx;
            int z = blockZ + dz;

            for (int yOff = -1; yOff <= 1; yOff++) {
                int y = blockY + yOff;
                if (y < 0) continue;
                int groundBlock = level.getBlock(x, y - 1, z);
                if (level.getBlock(x, y, z) != 0) continue;

                if (isDesertBiome(biome) && groundBlock == SAND) {
                    if (random.nextInt(3) == 0) {
                        level.setBlock(x, y, z, DEAD_BUSH);
                    }
                    break;
                }
                if (isSwampBiome(biome)) {
                    if (random.nextInt(4) == 0) {
                        level.setBlock(x, y, z, random.nextBoolean() ? RED_MUSHROOM : BROWN_MUSHROOM);
                    }
                    break;
                }
                if (isDarkForestBiome(biome)) {
                    if (random.nextInt(3) == 0) {
                        level.setBlock(x, y, z, random.nextBoolean() ? RED_MUSHROOM : BROWN_MUSHROOM);
                    }
                    break;
                }
                if (isTaigaBiome(biome) && groundBlock == GRASS_BLOCK) {
                    if (random.nextInt(20) == 0) {
                        level.setBlock(x, y, z, SWEET_BERRY_BUSH);
                    }
                    break;
                }
                break;
            }
        }
    }

    public static void placeSugarCane(WorldGenLevel level, int blockX, int blockY, int blockZ,
                                       int count, RandomSource random) {
        for (int i = 0; i < count; i++) {
            int dx = random.nextInt(16) - 8;
            int dz = random.nextInt(16) - 8;
            int x = blockX + dx;
            int z = blockZ + dz;
            for (int yOff = -1; yOff <= 1; yOff++) {
                int y = blockY + yOff;
                if (y < 0 || y > 318) continue;
                if (level.getBlock(x, y - 1, z) == GRASS_BLOCK || level.getBlock(x, y - 1, z) == SAND) {
                    if (level.getBlock(x, y, z) != 0) continue;
                    boolean nearWater = false;
                    int[][] checks = {{1,0},{-1,0},{0,1},{0,-1}};
                    for (int[] d : checks) {
                        if (level.getBlock(x + d[0], y - 1, z + d[1]) == WATER) {
                            nearWater = true;
                            break;
                        }
                    }
                    if (nearWater) {
                        int caneHeight = 1 + random.nextInt(3);
                        for (int h = 0; h < caneHeight; h++) {
                            if (level.getBlock(x, y + h, z) == 0) {
                                level.setBlock(x, y + h, z, SUGAR_CANE);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public static void placeFlowerPatch(WorldGenLevel level, int blockX, int blockY, int blockZ,
                                         int radius, int count, RandomSource random, int biome) {
        for (int i = 0; i < count; i++) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            int x = blockX + dx;
            int z = blockZ + dz;

            for (int yOff = -1; yOff <= 1; yOff++) {
                int y = blockY + yOff;
                if (y < 0) continue;
                if (level.getBlock(x, y - 1, z) != GRASS_BLOCK) continue;
                if (level.getBlock(x, y, z) != 0) continue;
                level.setBlock(x, y, z, flowerForBiome(biome, random));
                break;
            }
        }
    }

    private static int flowerForBiome(int biome, RandomSource random) {
        return switch (biome) {
            case B_FLOWER_FOREST -> {
                int r = random.nextInt(10);
                yield switch (r) {
                    case 0 -> DANDELION;
                    case 1, 2 -> POPPY;
                    case 3 -> ALLIUM;
                    case 4 -> CORNFLOWER;
                    case 5 -> OXEYE_DAISY;
                    case 6 -> AZURE_BLUET;
                    case 7 -> RED_TULIP;
                    case 8 -> ORANGE_TULIP;
                    default -> WHITE_TULIP;
                };
            }
            case B_PLAINS, B_SUNFLOWER_PLAINS -> {
                int r = random.nextInt(6);
                yield r < 2 ? DANDELION : r < 4 ? POPPY : r == 4 ? OXEYE_DAISY : CORNFLOWER;
            }
            case B_MEADOW -> {
                int r = random.nextInt(5);
                yield r < 2 ? DANDELION : r == 2 ? POPPY : r == 3 ? CORNFLOWER : OXEYE_DAISY;
            }
            case B_SWAMP -> BLUE_ORCHID;
            case B_TAIGA, B_OLD_GROWTH_SPRUCE_TAIGA, B_SNOWY_TAIGA -> LILY_OF_THE_VALLEY;
            case B_FOREST, B_DARK_FOREST -> {
                int r = random.nextInt(4);
                yield r == 0 ? DANDELION : r == 1 ? POPPY : r == 2 ? OXEYE_DAISY : CORNFLOWER;
            }
            case B_JUNGLE, B_SPARSE_JUNGLE, B_BAMBOO_JUNGLE -> {
                int r = random.nextInt(3);
                yield r == 0 ? DANDELION : r == 1 ? POPPY : ALLIUM;
            }
            case B_SAVANNA, B_SAVANNA_PLATEAU, B_WINDSWEPT_SAVANNA -> {
                int r = random.nextInt(4);
                yield r == 0 ? DANDELION : r == 1 ? POPPY : r == 2 ? ALLIUM : CORNFLOWER;
            }
            default -> DANDELION;
        };
    }

    private static boolean isDesertBiome(int biome) {
        return biome == B_DESERT;
    }

    private static boolean isSwampBiome(int biome) {
        return biome == B_SWAMP;
    }

    private static boolean isDarkForestBiome(int biome) {
        return biome == B_DARK_FOREST;
    }

    private static boolean isTaigaBiome(int biome) {
        return biome == B_TAIGA || biome == B_OLD_GROWTH_SPRUCE_TAIGA || biome == B_OLD_GROWTH_PINE_TAIGA || biome == B_SNOWY_TAIGA;
    }

    private static final int B_DESERT = 14;
    private static final int B_FLOWER_FOREST = 20;
    private static final int B_PLAINS = 40;
    private static final int B_SUNFLOWER_PLAINS = 53;
    private static final int B_MEADOW = 32;
    private static final int B_SWAMP = 54;
    private static final int B_TAIGA = 55;
    private static final int B_OLD_GROWTH_SPRUCE_TAIGA = 38;
    private static final int B_OLD_GROWTH_PINE_TAIGA = 37;
    private static final int B_SNOWY_TAIGA = 48;
    private static final int B_FOREST = 21;
    private static final int B_DARK_FOREST = 8;
    private static final int B_JUNGLE = 28;
    private static final int B_BAMBOO_JUNGLE = 1;
    private static final int B_SPARSE_JUNGLE = 50;
    private static final int B_SAVANNA = 42;
    private static final int B_SAVANNA_PLATEAU = 43;
    private static final int B_WINDSWEPT_SAVANNA = 63;

    private static void setBlockInChunk(Chunk chunk, int worldX, int y, int worldZ, int blockId, int chunkX, int chunkZ) {
        int lx = worldX - chunkX * 16;
        int lz = worldZ - chunkZ * 16;
        if (lx < 0 || lx >= 16 || lz < 0 || lz >= 16) return;
        if (y < -64 || y > 319) return;
        chunk.setBlock(lx, y, lz, blockId);
    }

    private static int getBlockInChunk(Chunk chunk, int worldX, int y, int worldZ, int chunkX, int chunkZ) {
        int lx = worldX - chunkX * 16;
        int lz = worldZ - chunkZ * 16;
        if (lx < 0 || lx >= 16 || lz < 0 || lz >= 16) return -1;
        if (y < -64 || y > 319) return -1;
        return chunk.getBlock(lx, y, lz);
    }

    public static void placeGrassPatchChunk(Chunk chunk, int blockX, int blockY, int blockZ,
                                            int radius, int count, RandomSource random, boolean fern, int chunkX, int chunkZ) {
        for (int i = 0; i < count; i++) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            int x = blockX + dx;
            int z = blockZ + dz;

            for (int yOff = -1; yOff <= 1; yOff++) {
                int y = blockY + yOff;
                if (y < 0 || y > 318) continue;
                int ground = getBlockInChunk(chunk, x, y - 1, z, chunkX, chunkZ);
                if (ground != GRASS_BLOCK) continue;
                if (getBlockInChunk(chunk, x, y, z, chunkX, chunkZ) != 0) continue;
                int plant = fern ? FERN : SHORT_GRASS;
                setBlockInChunk(chunk, x, y, z, plant, chunkX, chunkZ);
                if (!fern && random.nextInt(8) == 0 && y + 1 <= 319 && getBlockInChunk(chunk, x, y + 1, z, chunkX, chunkZ) == 0) {
                    setBlockInChunk(chunk, x, y, z, TALL_GRASS, chunkX, chunkZ);
                    setBlockInChunk(chunk, x, y + 1, z, TALL_GRASS, chunkX, chunkZ);
                }
                if (fern && random.nextInt(6) == 0 && y + 1 <= 319 && getBlockInChunk(chunk, x, y + 1, z, chunkX, chunkZ) == 0) {
                    setBlockInChunk(chunk, x, y, z, LARGE_FERN, chunkX, chunkZ);
                    setBlockInChunk(chunk, x, y + 1, z, LARGE_FERN, chunkX, chunkZ);
                }
                break;
            }
        }
    }

    public static void placeFlowerPatchChunk(Chunk chunk, int blockX, int blockY, int blockZ,
                                             int radius, int count, RandomSource random, int biome, int chunkX, int chunkZ) {
        for (int i = 0; i < count; i++) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            int x = blockX + dx;
            int z = blockZ + dz;
            for (int yOff = -1; yOff <= 1; yOff++) {
                int y = blockY + yOff;
                if (y < 0 || y > 318) continue;
                if (getBlockInChunk(chunk, x, y - 1, z, chunkX, chunkZ) != GRASS_BLOCK) continue;
                if (getBlockInChunk(chunk, x, y, z, chunkX, chunkZ) != 0) continue;
                int flower = flowerForBiome(biome, random);
                setBlockInChunk(chunk, x, y, z, flower, chunkX, chunkZ);
                break;
            }
        }
    }

    public static void placeBiomeDecorationChunk(Chunk chunk, int blockX, int blockY, int blockZ,
                                                 int radius, int count, RandomSource random, int biome, int chunkX, int chunkZ) {
        for (int i = 0; i < count; i++) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            int x = blockX + dx;
            int z = blockZ + dz;
            for (int yOff = -1; yOff <= 1; yOff++) {
                int y = blockY + yOff;
                if (y < 0) continue;
                int groundBlock = getBlockInChunk(chunk, x, y - 1, z, chunkX, chunkZ);
                if (getBlockInChunk(chunk, x, y, z, chunkX, chunkZ) != 0) continue;

                if (isDesertBiome(biome) && groundBlock == SAND) {
                    if (random.nextInt(3) == 0) {
                        setBlockInChunk(chunk, x, y, z, DEAD_BUSH, chunkX, chunkZ);
                    }
                    break;
                }
                if (isSwampBiome(biome)) {
                    if (random.nextInt(4) == 0) {
                        setBlockInChunk(chunk, x, y, z, random.nextBoolean() ? RED_MUSHROOM : BROWN_MUSHROOM, chunkX, chunkZ);
                    }
                    break;
                }
                if (isDarkForestBiome(biome)) {
                    if (random.nextInt(3) == 0) {
                        setBlockInChunk(chunk, x, y, z, random.nextBoolean() ? RED_MUSHROOM : BROWN_MUSHROOM, chunkX, chunkZ);
                    }
                    break;
                }
                if (isTaigaBiome(biome) && groundBlock == GRASS_BLOCK) {
                    if (random.nextInt(20) == 0) {
                        setBlockInChunk(chunk, x, y, z, SWEET_BERRY_BUSH, chunkX, chunkZ);
                    }
                    break;
                }
                break;
            }
        }
    }

    public static void placeSugarCaneChunk(Chunk chunk, int blockX, int blockY, int blockZ,
                                           int count, RandomSource random, int chunkX, int chunkZ) {
        for (int i = 0; i < count; i++) {
            int dx = random.nextInt(16) - 8;
            int dz = random.nextInt(16) - 8;
            int x = blockX + dx;
            int z = blockZ + dz;
            for (int yOff = -1; yOff <= 1; yOff++) {
                int y = blockY + yOff;
                if (y < 0 || y > 318) continue;
                int ground = getBlockInChunk(chunk, x, y - 1, z, chunkX, chunkZ);
                if (ground != GRASS_BLOCK && ground != SAND) continue;
                if (getBlockInChunk(chunk, x, y, z, chunkX, chunkZ) != 0) continue;
                int caneHeight = 1 + random.nextInt(3);
                for (int h = 0; h < caneHeight; h++) {
                    if (getBlockInChunk(chunk, x, y + h, z, chunkX, chunkZ) == 0) {
                        setBlockInChunk(chunk, x, y + h, z, SUGAR_CANE, chunkX, chunkZ);
                    }
                }
                break;
            }
        }
    }
}
