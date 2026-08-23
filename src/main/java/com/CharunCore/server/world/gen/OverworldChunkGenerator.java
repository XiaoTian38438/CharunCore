package com.CharunCore.server.world.gen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;

public class OverworldChunkGenerator {
    private static final int MIN_Y = -64;
    private static final int MAX_Y = 320;
    private static final int HEIGHT = MAX_Y - MIN_Y;
    private static final int SEA_LEVEL = 63;
    private static final double GLOBAL_OFFSET = -0.50375;
    private static final double CHEESE_NOISE_TARGET = -0.703125;
    private static final double SURFACE_DENSITY_THRESHOLD = 1.5625;

    private final long seed;
    private final XoroshiroRandomSource worldRandom;
    private final PositionalRandomFactory positionalFactory;

    private final BlendedNoise base3dNoise;
    private final NormalNoise continentalnessNoise, erosionNoise, temperatureNoise, vegetationNoise, ridgeNoise, jaggedNoise;
    private final NormalNoise shiftNoise;
    private final NormalNoise aquiferBarrier, aquiferFloodedness, aquiferSpread, aquiferLava;
    private final NormalNoise caveEntrance, caveCheese, caveLayer;
    private final NormalNoise spaghetti2d, spaghetti2dElevation, spaghetti2dModulator, spaghetti2dThickness;
    private final NormalNoise spaghetti3d1, spaghetti3d2, spaghetti3dRarity, spaghetti3dThickness;
    private final NormalNoise spaghettiRoughness, spaghettiRoughnessModulator;
    private final NormalNoise noodle, noodleThickness, noodleRidgeA, noodleRidgeB;
    private final NormalNoise pillar, pillarRareness, pillarThickness;

    private final CubicSpline offsetSpline;
    private final CubicSpline factorSpline;
    private final CubicSpline jaggednessSpline;

    private final BoundedFloatFunction<float[]> continentalnessCoord;
    private final BoundedFloatFunction<float[]> erosionCoord;
    private final BoundedFloatFunction<float[]> ridgesCoord;
    private final BoundedFloatFunction<float[]> ridgesFoldedCoord;

    private final int stoneId, deepslateId, waterId, lavaId, bedrockId;

    public OverworldChunkGenerator(long seed) {
        this.seed = seed;
        this.worldRandom = new XoroshiroRandomSource(seed);
        this.positionalFactory = this.worldRandom.forkPositional();

        this.base3dNoise = BlendedNoise.createUnseeded(0.25, 0.125, 80.0, 160.0, 8.0);

        this.continentalnessNoise = createNoise("minecraft:continentalness");
        this.erosionNoise = createNoise("minecraft:erosion");
        this.ridgeNoise = createNoise("minecraft:ridge");
        this.jaggedNoise = createNoise("minecraft:jagged");
        this.temperatureNoise = createNoise("minecraft:temperature");
        this.vegetationNoise = createNoise("minecraft:vegetation");
        this.shiftNoise = createNoise("minecraft:offset");

        this.aquiferBarrier = createNoise("minecraft:aquifer_barrier");
        this.aquiferFloodedness = createNoise("minecraft:aquifer_fluid_level_floodedness");
        this.aquiferSpread = createNoise("minecraft:aquifer_fluid_level_spread");
        this.aquiferLava = createNoise("minecraft:aquifer_lava");

        this.caveEntrance = createNoise("minecraft:cave_entrance");
        this.caveCheese = createNoise("minecraft:cave_cheese");
        this.caveLayer = createNoise("minecraft:cave_layer");
        this.spaghetti2d = createNoise("minecraft:spaghetti_2d");
        this.spaghetti2dElevation = createNoise("minecraft:spaghetti_2d_elevation");
        this.spaghetti2dModulator = createNoise("minecraft:spaghetti_2d_modulator");
        this.spaghetti2dThickness = createNoise("minecraft:spaghetti_2d_thickness");
        this.spaghetti3d1 = createNoise("minecraft:spaghetti_3d_1");
        this.spaghetti3d2 = createNoise("minecraft:spaghetti_3d_2");
        this.spaghetti3dRarity = createNoise("minecraft:spaghetti_3d_rarity");
        this.spaghetti3dThickness = createNoise("minecraft:spaghetti_3d_thickness");
        this.spaghettiRoughness = createNoise("minecraft:spaghetti_roughness");
        this.spaghettiRoughnessModulator = createNoise("minecraft:spaghetti_roughness_modulator");
        this.noodle = createNoise("minecraft:noodle");
        this.noodleThickness = createNoise("minecraft:noodle_thickness");
        this.noodleRidgeA = createNoise("minecraft:noodle_ridge_a");
        this.noodleRidgeB = createNoise("minecraft:noodle_ridge_b");
        this.pillar = createNoise("minecraft:pillar");
        this.pillarRareness = createNoise("minecraft:pillar_rareness");
        this.pillarThickness = createNoise("minecraft:pillar_thickness");

        this.continentalnessCoord = TerrainProvider.coordinate(0);
        this.erosionCoord = TerrainProvider.coordinate(1);
        this.ridgesCoord = TerrainProvider.coordinate(2);
        this.ridgesFoldedCoord = TerrainProvider.coordinate(3);

        this.offsetSpline = TerrainProvider.overworldOffset(continentalnessCoord, erosionCoord, ridgesFoldedCoord, false);
        this.factorSpline = TerrainProvider.overworldFactor(continentalnessCoord, erosionCoord, ridgesCoord, ridgesFoldedCoord, false);
        this.jaggednessSpline = TerrainProvider.overworldJaggedness(continentalnessCoord, erosionCoord, ridgesCoord, ridgesFoldedCoord, false);

        this.stoneId = BlockStateHelper.getDefault("stone");
        this.deepslateId = BlockStateHelper.getDefault("deepslate");
        this.waterId = BlockStateHelper.getDefault("water");
        this.lavaId = BlockStateHelper.getDefault("lava");
        this.bedrockId = BlockStateHelper.getDefault("bedrock");
    }

    private NormalNoise createNoise(String name) {
        NormalNoise.NoiseParameters params = NoiseParameters.get(name);
        if (params == null) throw new IllegalArgumentException("Unknown noise: " + name);
        RandomSource noiseRandom = this.positionalFactory.fromHashOf(name);
        return new NormalNoise(noiseRandom, params);
    }

    private double shiftA(int x, int z) {
        return this.shiftNoise.getValue(x * 0.25, 0, z * 0.25) * 4.0;
    }

    private double shiftB(int x, int z) {
        return this.shiftNoise.getValue(z * 0.25, x * 0.25, 0) * 4.0;
    }

    private double shiftedNoise2d(int x, int z, NormalNoise noise) {
        double sx = shiftA(x, z);
        double sz = shiftB(x, z);
        return noise.getValue(x * 0.25 + sx, 0, z * 0.25 + sz);
    }

    public Chunk generate(int chunkX, int chunkZ) {
        Chunk chunk = new Chunk(chunkX, chunkZ);

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int bx = chunkX * 16 + localX;
                int bz = chunkZ * 16 + localZ;

                for (int y = MIN_Y; y < MAX_Y; y++) {
                    double density = computeFinalDensity(bx, y, bz);
                    if (density > 0.0) {
                        int stateId = (y < 0) ? deepslateId : stoneId;
                        chunk.setBlock(localX, y, localZ, stateId);
                    } else if (y < SEA_LEVEL) {
                        chunk.setBlock(localX, y, localZ, waterId);
                    }
                }

                chunk.setBlock(localX, MIN_Y, localZ, bedrockId);
            }
        }

        applySurfaceRules(chunk, chunkX, chunkZ);
        return chunk;
    }

    private double computeFinalDensity(int x, int y, int z) {
        double continentalness = shiftedNoise2d(x, z, this.continentalnessNoise);
        double erosion = shiftedNoise2d(x, z, this.erosionNoise);
        double ridges = shiftedNoise2d(x, z, this.ridgeNoise);
        double ridgesFolded = TerrainProvider.peaksAndValleys((float) ridges);

        float[] climateContext = new float[4];
        climateContext[0] = (float) continentalness;
        climateContext[1] = (float) erosion;
        climateContext[2] = (float) ridges;
        climateContext[3] = (float) ridgesFolded;

        float offsetSplineVal = this.offsetSpline.apply(climateContext);
        double offset = GLOBAL_OFFSET + offsetSplineVal;

        double depth = yClampedGradient(y, -64, 320, 1.5, -1.5) + offset;

        float factorVal = this.factorSpline.apply(climateContext);
        float jaggednessVal = this.jaggednessSpline.apply(climateContext);

        double jaggedNoiseVal = this.jaggedNoise.getValue(x * 1500.0, 0, z * 1500.0);
        double jagContrib = jaggednessVal * halfNegative(jaggedNoiseVal);

        double depthWithJag = depth + jagContrib;

        double densityTimesFactor = depthWithJag * factorVal;
        double slopedCheese = 4.0 * quarterNegative(densityTimesFactor) + this.base3dNoise.compute(x, y, z);

        double density;
        if (slopedCheese < SURFACE_DENSITY_THRESHOLD) {
            density = Math.min(slopedCheese, 5.0 * computeEntrances(x, y, z));
        } else {
            double cheeseCave = computeCheeseCave(x, y, z, slopedCheese);
            double entrances = computeEntrances(x, y, z);
            double spaghetti2d = computeSpaghetti2D(x, y, z);
            double roughnessFunc = computeSpaghettiRoughnessFunction(x, y, z);

            double underground = Math.min(cheeseCave, Math.min(entrances, spaghetti2d + roughnessFunc));

            double pillars = computePillars(x, y, z);
            if (pillars >= 0.03) {
                underground = Math.max(underground, pillars);
            } else {
                underground = Math.max(underground, -1000000.0);
            }

            density = underground;
        }

        density = slideOverworld(y, density);
        density = density * 0.64;

        double noodleCave = computeNoodleCave(x, y, z);
        density = Math.min(density, noodleCave);

        return density;
    }

    private double computeCheeseCave(int x, int y, int z, double slopedCheese) {
        double caveLayerNoise = this.caveLayer.getValue(x * 8.0, y * 1.0, z * 8.0);
        double layerContrib = 4.0 * caveLayerNoise * caveLayerNoise;

        double cheeseNoise = this.caveCheese.getValue(x * 0.6666666666666666, y * 0.6666666666666666, z * 0.6666666666666666);
        double cheese = clamp(-1.0, 1.0, 0.27 + cheeseNoise)
            + clamp(0.0, 0.5, 1.5 + (-0.64) * slopedCheese);

        return layerContrib + cheese;
    }

    private double computeSpaghetti2D(int x, int y, int z) {
        double modulator = this.spaghetti2dModulator.getValue(x * 2.0, 0.0, z * 2.0);
        double rarity = type2Rarity(modulator);
        double spaghettiNoise = Math.abs(this.spaghetti2d.getValue(x * 2.0 / rarity, 0.0, z * 2.0 / rarity)) * rarity;

        double elevationRaw = this.spaghetti2dElevation.getValue(x * 1.0, 0.0, z * 1.0);
        double elevation = mapFromUnitTo(elevationRaw, (double) Math.floorDiv(-64, 8), 8.0);
        double yGradient = yClampedGradient(y, -64, 320, 8.0, -40.0);
        double dist = Math.abs(elevation + yGradient);
        double thicknessModRaw = this.spaghetti2dThickness.getValue(x * 2.0, y * 1.0, z * 2.0);
        double thicknessMod = mapFromUnitTo(thicknessModRaw, -0.6, -1.3);

        double distCubed = (dist + thicknessMod);
        distCubed = distCubed * distCubed * distCubed;

        double spaghettiContrib = spaghettiNoise + 0.083 * thicknessMod;

        return clamp(-1.0, 1.0, Math.max(spaghettiContrib, distCubed));
    }

    private double computeSpaghettiRoughnessFunction(int x, int y, int z) {
        double roughness = this.spaghettiRoughness.getValue(x * 1.0, y * 1.0, z * 1.0);
        double roughModRaw = this.spaghettiRoughnessModulator.getValue(x * 1.0, y * 1.0, z * 1.0);
        double roughMod = mapFromUnitTo(roughModRaw, 0.0, -0.1);
        return roughMod * (Math.abs(roughness) - 0.4);
    }

    private double computeEntrances(int x, int y, int z) {
        double rarity3d = this.spaghetti3dRarity.getValue(x * 2.0, y * 2.0, z * 2.0);
        double rarityVal = type1Rarity(rarity3d);

        double s1 = Math.abs(this.spaghetti3d1.getValue(x * 2.0 / rarityVal, y * 2.0 / rarityVal, z * 2.0 / rarityVal)) * rarityVal;
        double s2 = Math.abs(this.spaghetti3d2.getValue(x * 2.0 / rarityVal, y * 2.0 / rarityVal, z * 2.0 / rarityVal)) * rarityVal;

        double thicknessRaw = this.spaghetti3dThickness.getValue(x * 1.0, y * 1.0, z * 1.0);
        double mappedThickness = mapFromUnitTo(thicknessRaw, -0.065, -0.088);

        double combined3d = clamp(-1.0, 1.0, Math.max(s1, s2) + mappedThickness);

        double roughness = computeSpaghettiRoughnessFunction(x, y, z);

        double entranceNoise = this.caveEntrance.getValue(x * 0.75, y * 0.5, z * 0.75);
        double entranceContrib = entranceNoise + 0.37 + yClampedGradient(y, -10, 30, 0.3, 0.0);

        return Math.min(entranceContrib, roughness + combined3d);
    }

    private double computeNoodleCave(int x, int y, int z) {
        double n1 = this.noodle.getValue(x * 1.0, y * 1.0, z * 1.0);
        if (n1 >= -1000000.0 && n1 < 0.0) return 64.0;

        double thicknessRaw = this.noodleThickness.getValue(x * 1.0, y * 1.0, z * 1.0);
        double thickness = mapFromUnitTo(thicknessRaw, -0.05, -0.1);
        double ridgeA = this.noodleRidgeA.getValue(x * 2.6666666666666665, y * 2.6666666666666665, z * 2.6666666666666665);
        double ridgeB = this.noodleRidgeB.getValue(x * 2.6666666666666665, y * 2.6666666666666665, z * 2.6666666666666665);
        double ridgeContrib = 1.5 * Math.max(Math.abs(ridgeA), Math.abs(ridgeB));

        return thickness + ridgeContrib;
    }

    private double computePillars(int x, int y, int z) {
        double p = this.pillar.getValue(x * 25.0, y * 0.3, z * 25.0);
        double rarenessRaw = this.pillarRareness.getValue(x * 25.0, y * 1.0, z * 25.0);
        double rareMapped = mapFromUnitTo(rarenessRaw, 0.0, -2.0);
        double thicknessRaw = this.pillarThickness.getValue(x * 25.0, y * 1.0, z * 25.0);
        double thickMapped = mapFromUnitTo(thicknessRaw, 0.0, 1.1);

        double combined = p * 2.0 + rareMapped;
        double cubed = thickMapped * thickMapped * thickMapped;

        return combined * cubed;
    }

    private double slideOverworld(int y, double density) {
        density = slide(density, y, -64, 384, 80, 64, -0.078125, 0, 24, 0.1171875);
        return density;
    }

    private static double slide(double density, int y, int minY, int height,
                                int topFrom, int topTo, double topValue,
                                int bottomFrom, int bottomTo, double bottomValue) {
        double topLerp = yClampedGradient(y, minY + height - topFrom, minY + height - topTo, 1.0, 0.0);
        density = Mth.clampedLerp(topLerp, topValue, density);

        double bottomLerp = yClampedGradient(y, minY + bottomFrom, minY + bottomTo, 0.0, 1.0);
        density = Mth.clampedLerp(bottomLerp, bottomValue, density);

        return density;
    }

    private static double yClampedGradient(int y, int fromY, int toY, double fromValue, double toValue) {
        if (y <= fromY) return fromValue;
        if (y >= toY) return toValue;
        return fromValue + (double)(y - fromY) / (double)(toY - fromY) * (toValue - fromValue);
    }

    private void applySurfaceRules(Chunk chunk, int chunkX, int chunkZ) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int bx = chunkX * 16 + x;
                int bz = chunkZ * 16 + z;

                double continentalness = shiftedNoise2d(bx, bz, this.continentalnessNoise);
                double erosion = shiftedNoise2d(bx, bz, this.erosionNoise);
                double temperature = shiftedNoise2d(bx, bz, this.temperatureNoise);
                double vegetation = shiftedNoise2d(bx, bz, this.vegetationNoise);

                for (int y = MAX_Y - 1; y >= MIN_Y; y--) {
                    int current = chunk.getBlock(x, y, z);
                    if (current == 0) continue;

                    boolean isStone = (current == stoneId || current == deepslateId);
                    if (!isStone) continue;

                    int above = (y + 1 < MAX_Y) ? chunk.getBlock(x, y + 1, z) : 0;
                    if (above != 0 && above != waterId) continue;

                    int surfaceBlock = getSurfaceBlock(continentalness, erosion, temperature, vegetation);
                    int underBlock = getUnderBlock(continentalness, erosion, temperature, vegetation);

                    chunk.setBlock(x, y, z, surfaceBlock);
                    for (int d = 1; d <= 3; d++) {
                        if (y - d >= MIN_Y) {
                            int below = chunk.getBlock(x, y - d, z);
                            if (below == stoneId || below == deepslateId) {
                                chunk.setBlock(x, y - d, z, underBlock);
                            } else break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private int getSurfaceBlock(double c, double e, double t, double v) {
        if (c < -0.19) return BlockStateHelper.getDefault("gravel");
        if (c < -0.11) {
            if (t < -0.45) return BlockStateHelper.getDefault("snow_block");
            return BlockStateHelper.getDefault("sand");
        }
        if (t < -0.45) return BlockStateHelper.getDefault("snow_block");
        if (t > 0.55) {
            if (e < -0.2225) return BlockStateHelper.getDefault("red_sand");
            return BlockStateHelper.getDefault("sand");
        }
        if (e > 0.55) {
            if (t < 0.2) return BlockStateHelper.getDefault("podzol");
            return BlockStateHelper.getDefault("mycelium");
        }
        if (e > 0.45) return BlockStateHelper.getDefault("coarse_dirt");
        return BlockStateHelper.getDefault("grass_block");
    }

    private int getUnderBlock(double c, double e, double t, double v) {
        if (c < -0.19) return BlockStateHelper.getDefault("gravel");
        if (c < -0.11) return BlockStateHelper.getDefault("sand");
        if (t < -0.45) return BlockStateHelper.getDefault("snow_block");
        if (t > 0.55) {
            if (e < -0.2225) return BlockStateHelper.getDefault("terracotta");
            return BlockStateHelper.getDefault("sand");
        }
        if (e > 0.55) return BlockStateHelper.getDefault("coarse_dirt");
        return BlockStateHelper.getDefault("dirt");
    }

    private static double halfNegative(double v) {
        return v < 0 ? -Math.sqrt(Math.abs(v)) : Math.sqrt(v);
    }

    private static double quarterNegative(double v) {
        return v < 0 ? -Math.pow(Math.abs(v), 0.25) : Math.pow(v, 0.25);
    }

    private static double clamp(double min, double max, double v) {
        return Math.max(min, Math.min(max, v));
    }

    private static double type1Rarity(double v) {
        if (v < -0.5D) return 0.75D;
        if (v < 0.0D) return 1.0D;
        if (v < 0.5D) return 1.5D;
        return 2.0D;
    }

    private static double type2Rarity(double v) {
        if (v < -0.75D) return 0.5D;
        if (v < -0.5D) return 0.75D;
        if (v < 0.5D) return 1.0D;
        if (v < 0.75D) return 2.0D;
        return 3.0D;
    }

    private static double mapFromUnitTo(double v, double min, double max) {
        return Mth.clampedLerp(v, min, max);
    }
}
