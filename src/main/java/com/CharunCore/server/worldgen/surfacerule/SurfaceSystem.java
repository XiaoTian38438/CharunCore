package com.CharunCore.server.worldgen.surfacerule;

import java.util.function.BiFunction;

import com.CharunCore.server.world.gen.PositionalRandomFactory;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.Mth;
import com.CharunCore.server.world.gen.NormalNoise;
import com.CharunCore.server.world.gen.NoiseParameters;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.biome.BiomeManager;

public class SurfaceSystem {
    public static final int NULL = Integer.MIN_VALUE;

    static final int STONE = BlockStateHelper.getDefault("stone");
    static final int WATER = BlockStateHelper.getDefault("water");
    static final int LAVA = BlockStateHelper.getDefault("lava");
    static final int DEEPSLATE = BlockStateHelper.getDefault("deepslate");
    static final int BEDROCK = BlockStateHelper.getDefault("bedrock");
    static final int SNOW_BLOCK = BlockStateHelper.getDefault("snow_block");
    static final int PACKED_ICE = BlockStateHelper.getDefault("packed_ice");
    static final int AIR = BlockStateHelper.getDefault("air");
    static final int CAVE_AIR = BlockStateHelper.getDefault("cave_air");

    private static final int B_ERODED_BADLANDS = 19;
    private static final int B_FROZEN_OCEAN = 22;
    private static final int B_DEEP_FROZEN_OCEAN = 11;

    private final SurfaceRules.RuleSource ruleSource;
    private final int seaLevel;
    private final int minY;
    private final int height;

    private final NormalNoise surfaceDepthNoise;
    private final NormalNoise surfaceSecondaryNoise;
    private final NormalNoise clayBandsOffsetNoise;
    private final NormalNoise badlandsSurfaceNoise;
    private final NormalNoise badlandsPillarNoise;
    private final NormalNoise badlandsPillarRoofNoise;
    private final NormalNoise icebergSurfaceNoise;
    private final NormalNoise icebergPillarNoise;
    private final NormalNoise icebergPillarRoofNoise;

    private final SurfaceRuleContext context;
    private final int[] clayBands;
    private final Chunk chunk;
    private final int chunkX;
    private final int chunkZ;
    private final BiomeManager biomeManager;
    private final BiFunction<Integer, Integer, Integer> preliminarySurfaceFunc;
    private final BiFunction<Integer, Integer, Integer> heightmapFunc;
    /** 表面规则只对该方块生效（主世界=stone，下界=netherrack）。 */
    private final int targetBlock;

    public SurfaceSystem(SurfaceRules.RuleSource ruleSource,
                          int seaLevel, int minY, int height,
                          NormalNoise surfaceDepthNoise,
                          NormalNoise surfaceSecondaryNoise,
                          NormalNoise clayBandsOffsetNoise,
                          Chunk chunk, int chunkX, int chunkZ,
                          BiomeManager biomeManager,
                          BiFunction<Integer, Integer, Integer> preliminarySurfaceFunc,
                          BiFunction<Integer, Integer, Integer> heightmapFunc) {
        this(ruleSource, seaLevel, minY, height, surfaceDepthNoise, surfaceSecondaryNoise,
            clayBandsOffsetNoise, chunk, chunkX, chunkZ, biomeManager,
            preliminarySurfaceFunc, heightmapFunc, STONE);
    }

    public SurfaceSystem(SurfaceRules.RuleSource ruleSource,
                          int seaLevel, int minY, int height,
                          NormalNoise surfaceDepthNoise,
                          NormalNoise surfaceSecondaryNoise,
                          NormalNoise clayBandsOffsetNoise,
                          Chunk chunk, int chunkX, int chunkZ,
                          BiomeManager biomeManager,
                          BiFunction<Integer, Integer, Integer> preliminarySurfaceFunc,
                          BiFunction<Integer, Integer, Integer> heightmapFunc,
                          int targetBlock) {
        this.ruleSource = ruleSource;
        this.seaLevel = seaLevel;
        this.minY = minY;
        this.height = height;
        this.targetBlock = targetBlock;
        this.surfaceDepthNoise = surfaceDepthNoise;
        this.surfaceSecondaryNoise = surfaceSecondaryNoise;
        this.clayBandsOffsetNoise = clayBandsOffsetNoise;

        var factory = DensityFunction.NoiseHolder.sharedFactory();
        this.badlandsSurfaceNoise = new NormalNoise(factory.fromHashOf("minecraft:badlands_surface"), NoiseParameters.get("minecraft:badlands_surface"));
        this.badlandsPillarNoise = new NormalNoise(factory.fromHashOf("minecraft:badlands_pillar"), NoiseParameters.get("minecraft:badlands_pillar"));
        this.badlandsPillarRoofNoise = new NormalNoise(factory.fromHashOf("minecraft:badlands_pillar_roof"), NoiseParameters.get("minecraft:badlands_pillar_roof"));
        this.icebergSurfaceNoise = new NormalNoise(factory.fromHashOf("minecraft:iceberg_surface"), NoiseParameters.get("minecraft:iceberg_surface"));
        this.icebergPillarNoise = new NormalNoise(factory.fromHashOf("minecraft:iceberg_pillar"), NoiseParameters.get("minecraft:iceberg_pillar"));
        this.icebergPillarRoofNoise = new NormalNoise(factory.fromHashOf("minecraft:iceberg_pillar_roof"), NoiseParameters.get("minecraft:iceberg_pillar_roof"));
        this.chunk = chunk;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.biomeManager = biomeManager;
        this.preliminarySurfaceFunc = preliminarySurfaceFunc;
        this.heightmapFunc = heightmapFunc;
        this.clayBands = generateClayBands();
        this.context = new SurfaceRuleContext(this, chunk);
    }

    public int getSurfaceDepth(int x, int z) {
        double noise = surfaceDepthNoise != null ? surfaceDepthNoise.getValue(x, 0, z) : 0.0;
        double random = DensityFunction.NoiseHolder.sharedFactory()
            .fromHashOf("minecraft:surface").forkPositional().at(x, 0, z).nextDouble() * 0.25;
        return (int) (noise * 2.75 + 3.0 + random);
    }

    public double getSurfaceSecondary(int x, int z) {
        return surfaceSecondaryNoise != null ? surfaceSecondaryNoise.getValue(x, 0, z) : 0.0;
    }

    public int getPreliminarySurfaceLevel(int x, int z) {
        if (preliminarySurfaceFunc != null) {
            return preliminarySurfaceFunc.apply(x, z);
        }
        return seaLevel;
    }

    public int getSurfaceHeight(int x, int z) {
        return heightmapFunc.apply(x, z);
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public int getBiome(int x, int y, int z) {
        return biomeManager.getBiome(x, y, z);
    }

    public boolean isColdEnoughToSnow(int x, int y, int z) {
        int biome = getBiome(x, y, z);
        return isColdEnoughToSnowBiome(biome);
    }

    public boolean isColdEnoughToSnowBiome(int biome) {
        return biome == 46 || biome == 48 || biome == 26 || biome == 22
            || biome == 23 || biome == 25 || biome == 47 || biome == 24 || biome == 45;
    }

    public int getBand(int x, int y, int z) {
        double noise = clayBandsOffsetNoise != null ? clayBandsOffsetNoise.getValue(x, 0, z) : 0.0;
        int offset = (int) Math.round(noise * 4.0);
        return clayBands[(y + offset + clayBands.length) % clayBands.length];
    }

    public void buildSurface(Chunk chunk) {
        SurfaceRules.SurfaceRule surfaceRule = ruleSource.apply(context);

        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int blockX = chunkX * 16 + lx;
                int blockZ = chunkZ * 16 + lz;

                context.updateXZ(blockX, blockZ);

                // Vanilla scans from WORLD_SURFACE_WG + 1 (the topmost non-air block INCLUDING
                // fluid), not from the solid floor. This is required so that the first fluid block
                // the downward scan hits is the TOP of the water column (≈ sea level), making
                // waterHeight ≈ sea level. With waterHeight set just above the floor, the
                // waterStartCheck(-6,-1) condition in ruleSource9 Part D misfires (true), which makes
                // UNDER_FLOOR -> ruleSource7 -> DIRT preempt ruleSource3 (GRAVEL). Starting the scan
                // at the top of the water column fixes ocean floors to sand/gravel.
                int topY = minY - 1;
                int maxY = minY + height - 1;
                for (int y = maxY; y >= minY; y--) {
                    int b = chunk.getBlock(lx, y, lz);
                    if (b != AIR && b != CAVE_AIR) { topY = y; break; }
                }
                if (topY < minY) continue;
                int surfaceY = topY;
                context.setSurfaceY(surfaceY);

                int biome = getBiome(blockX, surfaceY, blockZ);
                if (biome == B_ERODED_BADLANDS) {
                    erodedBadlandsExtension(chunk, lx, lz, blockX, blockZ, surfaceY);
                }

                int scanStart = surfaceY + 1;

                int stoneDepthAbove = 0;
                int waterHeight = Integer.MIN_VALUE;
                int stoneDepthBelowStart = Integer.MAX_VALUE;

                for (int y = scanStart; y >= minY; y--) {
                    int block = chunk.getBlock(lx, y, lz);

                    if (block == AIR || block == CAVE_AIR) {
                        stoneDepthAbove = 0;
                        waterHeight = Integer.MIN_VALUE;
                        continue;
                    }
                    if (block == WATER || block == LAVA) {
                        if (waterHeight == Integer.MIN_VALUE) {
                            waterHeight = y + 1;
                        }
                        continue;
                    }

                    if (stoneDepthBelowStart >= y) {
                        stoneDepthBelowStart = minY - 1;
                        for (int by = y - 1; by >= minY - 1; by--) {
                            int b = chunk.getBlock(lx, by, lz);
                            if (b == AIR || b == CAVE_AIR || b == WATER || b == LAVA) {
                                stoneDepthBelowStart = by + 1;
                                break;
                            }
                        }
                    }
                    stoneDepthAbove++;
                    int stoneDepthBelow = y - stoneDepthBelowStart + 1;

                    context.updateY(stoneDepthAbove, stoneDepthBelow, waterHeight, y);
                    if (block == targetBlock) {
                        int result = surfaceRule.tryApply(blockX, y, blockZ);
                        if (result != NULL) {
                            chunk.setBlock(lx, y, lz, result);
                        }
                    }
                }

                if (biome == B_FROZEN_OCEAN || biome == B_DEEP_FROZEN_OCEAN) {
                    frozenOceanExtension(chunk, lx, lz, blockX, blockZ, surfaceY, biome);
                }
            }
        }
    }

    private void erodedBadlandsExtension(Chunk chunk, int lx, int lz, int blockX, int blockZ, int surfaceY) {
        double surfaceVal = Math.min(Math.abs(badlandsSurfaceNoise.getValue(blockX, 0, blockZ) * 8.25),
            badlandsPillarNoise.getValue(blockX * 0.2, 0, blockZ * 0.2) * 15.0);
        if (surfaceVal <= 0.0) return;
        double roofVal = Math.abs(badlandsPillarRoofNoise.getValue(blockX * 0.75, 0, blockZ * 0.75) * 1.5);
        double heightVal = 64.0 + Math.min(surfaceVal * surfaceVal * 2.5, Math.ceil(roofVal * 50.0) + 24.0);
        int pillarHeight = Mth.floor(heightVal);
        if (surfaceY > pillarHeight) return;

        int firstStoneY = pillarHeight;
        while (firstStoneY >= minY && chunk.getBlock(lx, firstStoneY, lz) != STONE) {
            if (chunk.getBlock(lx, firstStoneY, lz) == WATER) return;
            firstStoneY--;
        }

        for (int y = pillarHeight; y >= minY && chunk.getBlock(lx, y, lz) == AIR; y--) {
            chunk.setBlock(lx, y, lz, STONE);
        }
    }

    private void frozenOceanExtension(Chunk chunk, int lx, int lz, int blockX, int blockZ, int surfaceY, int biome) {
        double surfaceVal = Math.min(Math.abs(icebergSurfaceNoise.getValue(blockX, 0, blockZ) * 8.25),
            icebergPillarNoise.getValue(blockX * 1.28, 0, blockZ * 1.28) * 15.0);
        if (surfaceVal <= 1.8) return;
        double roofVal = Math.abs(icebergPillarRoofNoise.getValue(blockX * 1.17, 0, blockZ * 1.17) * 1.5);
        double icebergHeight = Math.min(surfaceVal * surfaceVal * 1.2, Math.ceil(roofVal * 40.0) + 14.0);
        if (biome == B_DEEP_FROZEN_OCEAN) {
            icebergHeight -= 2.0;
        }
        double lowerBound;
        if (icebergHeight > 2.0) {
            lowerBound = (double) seaLevel - icebergHeight - 7.0;
            icebergHeight += (double) seaLevel;
        } else {
            icebergHeight = 0.0;
            lowerBound = 0.0;
        }
        double icebergTop = icebergHeight;
        RandomSource rng = DensityFunction.NoiseHolder.sharedFactory()
            .fromHashOf("minecraft:surface").forkPositional().at(blockX, 0, blockZ);
        int snowCapCount = 2 + rng.nextInt(4);
        int snowCapMaxY = seaLevel + 18 + rng.nextInt(10);
        int snowPlaced = 0;
        int startY = Math.max(surfaceY, (int) icebergTop + 1);
        for (int y = startY; y >= minY; y--) {
            int block = chunk.getBlock(lx, y, lz);
            boolean shouldPlace = false;
            if (block == 0 && y < (int) icebergTop && rng.nextDouble() > 0.01) {
                shouldPlace = true;
            } else if (block == WATER && y > (int) lowerBound && y < seaLevel && lowerBound != 0.0 && rng.nextDouble() > 0.15) {
                shouldPlace = true;
            }
            if (!shouldPlace) continue;
            if (snowPlaced <= snowCapCount && y > snowCapMaxY) {
                chunk.setBlock(lx, y, lz, SNOW_BLOCK);
                snowPlaced++;
            } else {
                chunk.setBlock(lx, y, lz, PACKED_ICE);
            }
        }
    }

    private int[] generateClayBands() {
        int[] bands = new int[192];
        int terracotta = BlockStateHelper.getDefault("terracotta");
        int orangeTerracotta = BlockStateHelper.getDefault("orange_terracotta");
        int yellowTerracotta = BlockStateHelper.getDefault("yellow_terracotta");
        int brownTerracotta = BlockStateHelper.getDefault("brown_terracotta");
        int redTerracotta = BlockStateHelper.getDefault("red_terracotta");
        int whiteTerracotta = BlockStateHelper.getDefault("white_terracotta");
        int lightGrayTerracotta = BlockStateHelper.getDefault("light_gray_terracotta");

        java.util.Arrays.fill(bands, terracotta);

        PositionalRandomFactory factory =
            DensityFunction.NoiseHolder.sharedFactory()
                .fromHashOf("clay_bands").forkPositional();
        RandomSource rng = factory.at(0, 0, 0);

        for (int n = 0; n < bands.length; ++n) {
            n += rng.nextInt(5) + 1;
            if (n >= bands.length) continue;
            bands[n] = orangeTerracotta;
        }

        makeBands(rng, bands, 1, yellowTerracotta);
        makeBands(rng, bands, 2, brownTerracotta);
        makeBands(rng, bands, 1, redTerracotta);

        int whiteCount = rng.nextInt(15 - 9 + 1) + 9;
        int pos = 0;
        for (int i = 0; pos < whiteCount && i < bands.length; ++pos, i += rng.nextInt(16) + 4) {
            bands[i] = whiteTerracotta;
            if (i - 1 > 0 && rng.nextBoolean()) {
                bands[i - 1] = lightGrayTerracotta;
            }
            if (i + 1 >= bands.length || !rng.nextBoolean()) continue;
            bands[i + 1] = lightGrayTerracotta;
        }

        return bands;
    }

    private static void makeBands(RandomSource rng, int[] bands, int n, int blockState) {
        int count = rng.nextInt(15 - 6 + 1) + 6;
        for (int i = 0; i < count; ++i) {
            int bandLen = n + rng.nextInt(3);
            int start = rng.nextInt(bands.length);
            for (int j = 0; start + j < bands.length && j < bandLen; ++j) {
                bands[start + j] = blockState;
            }
        }
    }
}
