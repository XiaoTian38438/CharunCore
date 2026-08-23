package com.CharunCore.server.worldgen.biome;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

public final class OverworldBiomeBuilder {

    private static final float VALLEY_SIZE = 0.05f;
    private static final float LOW_START = 0.26666668f;
    private static final float HIGH_START = 0.4f;
    private static final float HIGH_END = 0.93333334f;
    private static final float PEAK_SIZE = 0.1f;
    private static final float PEAK_START = 0.56666666f;
    private static final float PEAK_END = 0.7666667f;
    public static final float NEAR_INLAND_START = -0.11f;
    public static final float MID_INLAND_START = 0.03f;
    public static final float FAR_INLAND_START = 0.3f;
    public static final float EROSION_INDEX_1_START = -0.78f;
    public static final float EROSION_INDEX_2_START = -0.375f;

    private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0f, 1.0f);
    private final Climate.Parameter[] temperatures = {
        Climate.Parameter.span(-1.0f, -0.45f),
        Climate.Parameter.span(-0.45f, -0.15f),
        Climate.Parameter.span(-0.15f, 0.2f),
        Climate.Parameter.span(0.2f, 0.55f),
        Climate.Parameter.span(0.55f, 1.0f)
    };
    private final Climate.Parameter[] humidities = {
        Climate.Parameter.span(-1.0f, -0.35f),
        Climate.Parameter.span(-0.35f, -0.1f),
        Climate.Parameter.span(-0.1f, 0.1f),
        Climate.Parameter.span(0.1f, 0.3f),
        Climate.Parameter.span(0.3f, 1.0f)
    };
    private final Climate.Parameter[] erosions = {
        Climate.Parameter.span(-1.0f, -0.78f),
        Climate.Parameter.span(-0.78f, -0.375f),
        Climate.Parameter.span(-0.375f, -0.2225f),
        Climate.Parameter.span(-0.2225f, 0.05f),
        Climate.Parameter.span(0.05f, 0.45f),
        Climate.Parameter.span(0.45f, 0.55f),
        Climate.Parameter.span(0.55f, 1.0f)
    };
    private final Climate.Parameter FROZEN_RANGE = temperatures[0];
    private final Climate.Parameter UNFROZEN_RANGE = Climate.Parameter.span(temperatures[1], temperatures[4]);
    private final Climate.Parameter mushroomFieldsContinentalness = Climate.Parameter.span(-1.2f, -1.05f);
    private final Climate.Parameter deepOceanContinentalness = Climate.Parameter.span(-1.05f, -0.455f);
    private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.455f, -0.19f);
    private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19f, -0.11f);
    private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11f, 0.55f);
    private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.11f, 0.03f);
    private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03f, 0.3f);
    private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3f, 1.0f);

    private static final int B_DEEP_FROZEN_OCEAN = 11;
    private static final int B_DEEP_COLD_OCEAN = 9;
    private static final int B_DEEP_OCEAN = 13;
    private static final int B_DEEP_LUKEWARM_OCEAN = 12;
    private static final int B_WARM_OCEAN = 58;
    private static final int B_FROZEN_OCEAN = 22;
    private static final int B_COLD_OCEAN = 6;
    private static final int B_OCEAN = 35;
    private static final int B_LUKEWARM_OCEAN = 29;
    private static final int B_SNOWY_PLAINS = 46;
    private static final int B_SNOWY_TAIGA = 48;
    private static final int B_TAIGA = 55;
    private static final int B_PLAINS = 40;
    private static final int B_FOREST = 21;
    private static final int B_OLD_GROWTH_SPRUCE_TAIGA = 38;
    private static final int B_FLOWER_FOREST = 20;
    private static final int B_BIRCH_FOREST = 4;
    private static final int B_DARK_FOREST = 8;
    private static final int B_SAVANNA = 42;
    private static final int B_JUNGLE = 28;
    private static final int B_DESERT = 14;
    private static final int B_ICE_SPIKES = 26;
    private static final int B_OLD_GROWTH_PINE_TAIGA = 37;
    private static final int B_SUNFLOWER_PLAINS = 53;
    private static final int B_OLD_GROWTH_BIRCH_FOREST = 36;
    private static final int B_SPARSE_JUNGLE = 50;
    private static final int B_BAMBOO_JUNGLE = 1;
    private static final int B_MEADOW = 32;
    private static final int B_PALE_GARDEN = 39;
    private static final int B_SAVANNA_PLATEAU = 43;
    private static final int B_BADLANDS = 0;
    private static final int B_WOODED_BADLANDS = 64;
    private static final int B_CHERRY_GROVE = 5;
    private static final int B_ERODED_BADLANDS = 19;
    private static final int B_WINDSWEPT_GRAVELLY_HILLS = 61;
    private static final int B_WINDSWEPT_HILLS = 62;
    private static final int B_WINDSWEPT_FOREST = 60;
    private static final int B_MUSHROOM_FIELDS = 33;
    private static final int B_STONY_SHORE = 52;
    private static final int B_SWAMP = 54;
    private static final int B_MANGROVE_SWAMP = 31;
    private static final int B_STONY_PEAKS = 51;
    private static final int B_JAGGED_PEAKS = 27;
    private static final int B_FROZEN_PEAKS = 23;
    private static final int B_SNOWY_SLOPES = 47;
    private static final int B_GROVE = 25;
    private static final int B_RIVER = 41;
    private static final int B_FROZEN_RIVER = 24;
    private static final int B_SNOWY_BEACH = 45;
    private static final int B_BEACH = 3;
    private static final int B_DRIPSTONE_CAVES = 15;
    private static final int B_LUSH_CAVES = 30;
    private static final int B_DEEP_DARK = 10;
    private static final int B_WINDSWEPT_SAVANNA = 63;

    private final int[][] OCEANS = {
        {B_DEEP_FROZEN_OCEAN, B_DEEP_COLD_OCEAN, B_DEEP_OCEAN, B_DEEP_LUKEWARM_OCEAN, B_WARM_OCEAN},
        {B_FROZEN_OCEAN, B_COLD_OCEAN, B_OCEAN, B_LUKEWARM_OCEAN, B_WARM_OCEAN}
    };
    private final int[][] MIDDLE_BIOMES = {
        {B_SNOWY_PLAINS, B_SNOWY_PLAINS, B_SNOWY_PLAINS, B_SNOWY_TAIGA, B_TAIGA},
        {B_PLAINS, B_PLAINS, B_FOREST, B_TAIGA, B_OLD_GROWTH_SPRUCE_TAIGA},
        {B_FLOWER_FOREST, B_PLAINS, B_FOREST, B_BIRCH_FOREST, B_DARK_FOREST},
        {B_SAVANNA, B_SAVANNA, B_FOREST, B_JUNGLE, B_JUNGLE},
        {B_DESERT, B_DESERT, B_DESERT, B_DESERT, B_DESERT}
    };
    private final int[][] MIDDLE_BIOMES_VARIANT = {
        {B_ICE_SPIKES, -1, B_SNOWY_TAIGA, -1, -1},
        {-1, -1, -1, -1, B_OLD_GROWTH_PINE_TAIGA},
        {B_SUNFLOWER_PLAINS, -1, -1, B_OLD_GROWTH_BIRCH_FOREST, -1},
        {-1, -1, B_PLAINS, B_SPARSE_JUNGLE, B_BAMBOO_JUNGLE},
        {-1, -1, -1, -1, -1}
    };
    private final int[][] PLATEAU_BIOMES = {
        {B_SNOWY_PLAINS, B_SNOWY_PLAINS, B_SNOWY_PLAINS, B_SNOWY_TAIGA, B_SNOWY_TAIGA},
        {B_MEADOW, B_MEADOW, B_FOREST, B_TAIGA, B_OLD_GROWTH_SPRUCE_TAIGA},
        {B_MEADOW, B_MEADOW, B_MEADOW, B_MEADOW, B_PALE_GARDEN},
        {B_SAVANNA_PLATEAU, B_SAVANNA_PLATEAU, B_FOREST, B_FOREST, B_JUNGLE},
        {B_BADLANDS, B_BADLANDS, B_BADLANDS, B_WOODED_BADLANDS, B_WOODED_BADLANDS}
    };
    private final int[][] PLATEAU_BIOMES_VARIANT = {
        {B_ICE_SPIKES, -1, -1, -1, -1},
        {B_CHERRY_GROVE, -1, B_MEADOW, B_MEADOW, B_OLD_GROWTH_PINE_TAIGA},
        {B_CHERRY_GROVE, B_CHERRY_GROVE, B_FOREST, B_BIRCH_FOREST, -1},
        {-1, -1, -1, -1, -1},
        {B_ERODED_BADLANDS, B_ERODED_BADLANDS, -1, -1, -1}
    };
    private final int[][] SHATTERED_BIOMES = {
        {B_WINDSWEPT_GRAVELLY_HILLS, B_WINDSWEPT_GRAVELLY_HILLS, B_WINDSWEPT_HILLS, B_WINDSWEPT_FOREST, B_WINDSWEPT_FOREST},
        {B_WINDSWEPT_GRAVELLY_HILLS, B_WINDSWEPT_GRAVELLY_HILLS, B_WINDSWEPT_HILLS, B_WINDSWEPT_FOREST, B_WINDSWEPT_FOREST},
        {B_WINDSWEPT_HILLS, B_WINDSWEPT_HILLS, B_WINDSWEPT_HILLS, B_WINDSWEPT_FOREST, B_WINDSWEPT_FOREST},
        {-1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1}
    };

    public static List<SimpleEntry<Climate.ParameterPoint, Integer>> buildParameters() {
        List<SimpleEntry<Climate.ParameterPoint, Integer>> list = new ArrayList<>();
        new OverworldBiomeBuilder().addBiomes(list);
        return list;
    }

    private void addBiomes(List<SimpleEntry<Climate.ParameterPoint, Integer>> list) {
        addOffCoastBiomes(list);
        addInlandBiomes(list);
        addUndergroundBiomes(list);
    }

    private void addOffCoastBiomes(List<SimpleEntry<Climate.ParameterPoint, Integer>> list) {
        addSurface(list, FULL_RANGE, FULL_RANGE, mushroomFieldsContinentalness, FULL_RANGE, FULL_RANGE, 0.0f, B_MUSHROOM_FIELDS);
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temp = temperatures[i];
            addSurface(list, temp, FULL_RANGE, deepOceanContinentalness, FULL_RANGE, FULL_RANGE, 0.0f, OCEANS[0][i]);
            addSurface(list, temp, FULL_RANGE, oceanContinentalness, FULL_RANGE, FULL_RANGE, 0.0f, OCEANS[1][i]);
        }
    }

    private void addInlandBiomes(List<SimpleEntry<Climate.ParameterPoint, Integer>> list) {
        addMidSlice(list, Climate.Parameter.span(-1.0f, -HIGH_END));
        addHighSlice(list, Climate.Parameter.span(-HIGH_END, -PEAK_END));
        addPeaks(list, Climate.Parameter.span(-PEAK_END, -PEAK_START));
        addHighSlice(list, Climate.Parameter.span(-PEAK_START, -HIGH_START));
        addMidSlice(list, Climate.Parameter.span(-HIGH_START, -LOW_START));
        addLowSlice(list, Climate.Parameter.span(-LOW_START, -VALLEY_SIZE));
        addValleys(list, Climate.Parameter.span(-VALLEY_SIZE, VALLEY_SIZE));
        addLowSlice(list, Climate.Parameter.span(VALLEY_SIZE, LOW_START));
        addMidSlice(list, Climate.Parameter.span(LOW_START, HIGH_START));
        addHighSlice(list, Climate.Parameter.span(HIGH_START, PEAK_START));
        addPeaks(list, Climate.Parameter.span(PEAK_START, PEAK_END));
        addHighSlice(list, Climate.Parameter.span(PEAK_END, HIGH_END));
        addMidSlice(list, Climate.Parameter.span(HIGH_END, 1.0f));
    }

    private void addPeaks(List<SimpleEntry<Climate.ParameterPoint, Integer>> list, Climate.Parameter weirdness) {
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temp = temperatures[i];
            for (int j = 0; j < humidities.length; j++) {
                Climate.Parameter hum = humidities[j];
                int middle = pickMiddleBiome(i, j, weirdness);
                int middleOrBadlands = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
                int middleOrBadlandsOrSlope = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
                int plateau = pickPlateauBiome(i, j, weirdness);
                int shattered = pickShatteredBiome(i, j, weirdness);
                int windsweptSavanna = maybePickWindsweptSavannaBiome(i, j, weirdness, shattered);
                int peak = pickPeakBiome(i, j, weirdness);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0f, peak);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), erosions[1], weirdness, 0.0f, middleOrBadlandsOrSlope);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[1], weirdness, 0.0f, peak);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0f, middle);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[2], weirdness, 0.0f, plateau);
                addSurface(list, temp, hum, midInlandContinentalness, erosions[3], weirdness, 0.0f, middleOrBadlands);
                addSurface(list, temp, hum, farInlandContinentalness, erosions[3], weirdness, 0.0f, plateau);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0f, middle);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), erosions[5], weirdness, 0.0f, windsweptSavanna);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0f, shattered);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, middle);
            }
        }
    }

    private void addHighSlice(List<SimpleEntry<Climate.ParameterPoint, Integer>> list, Climate.Parameter weirdness) {
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temp = temperatures[i];
            for (int j = 0; j < humidities.length; j++) {
                Climate.Parameter hum = humidities[j];
                int middle = pickMiddleBiome(i, j, weirdness);
                int middleOrBadlands = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
                int middleOrBadlandsOrSlope = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
                int plateau = pickPlateauBiome(i, j, weirdness);
                int shattered = pickShatteredBiome(i, j, weirdness);
                int windsweptSavanna = maybePickWindsweptSavannaBiome(i, j, weirdness, middle);
                int slope = pickSlopeBiome(i, j, weirdness);
                int peak = pickPeakBiome(i, j, weirdness);
                addSurface(list, temp, hum, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0f, middle);
                addSurface(list, temp, hum, nearInlandContinentalness, erosions[0], weirdness, 0.0f, slope);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0f, peak);
                addSurface(list, temp, hum, nearInlandContinentalness, erosions[1], weirdness, 0.0f, middleOrBadlandsOrSlope);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[1], weirdness, 0.0f, slope);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0f, middle);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[2], weirdness, 0.0f, plateau);
                addSurface(list, temp, hum, midInlandContinentalness, erosions[3], weirdness, 0.0f, middleOrBadlands);
                addSurface(list, temp, hum, farInlandContinentalness, erosions[3], weirdness, 0.0f, plateau);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0f, middle);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), erosions[5], weirdness, 0.0f, windsweptSavanna);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0f, shattered);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, middle);
            }
        }
    }

    private void addMidSlice(List<SimpleEntry<Climate.ParameterPoint, Integer>> list, Climate.Parameter weirdness) {
        addSurface(list, FULL_RANGE, FULL_RANGE, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[2]), weirdness, 0.0f, B_STONY_SHORE);
        addSurface(list, Climate.Parameter.span(temperatures[1], temperatures[2]), FULL_RANGE,
            Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, B_SWAMP);
        addSurface(list, Climate.Parameter.span(temperatures[3], temperatures[4]), FULL_RANGE,
            Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, B_MANGROVE_SWAMP);
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temp = temperatures[i];
            for (int j = 0; j < humidities.length; j++) {
                Climate.Parameter hum = humidities[j];
                int middle = pickMiddleBiome(i, j, weirdness);
                int middleOrBadlands = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
                int middleOrBadlandsOrSlope = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
                int shattered = pickShatteredBiome(i, j, weirdness);
                int plateau = pickPlateauBiome(i, j, weirdness);
                int beach = pickBeachBiome(i, j);
                int windsweptSavanna = maybePickWindsweptSavannaBiome(i, j, weirdness, middle);
                int shatteredCoast = pickShatteredCoastBiome(i, j, weirdness);
                int slope = pickSlopeBiome(i, j, weirdness);
                addSurface(list, temp, hum, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0f, slope);
                addSurface(list, temp, hum, Climate.Parameter.span(nearInlandContinentalness, midInlandContinentalness), erosions[1], weirdness, 0.0f, middleOrBadlandsOrSlope);
                addSurface(list, temp, hum, farInlandContinentalness, erosions[1], weirdness, 0.0f, i == 0 ? slope : plateau);
                addSurface(list, temp, hum, nearInlandContinentalness, erosions[2], weirdness, 0.0f, middle);
                addSurface(list, temp, hum, midInlandContinentalness, erosions[2], weirdness, 0.0f, middleOrBadlands);
                addSurface(list, temp, hum, farInlandContinentalness, erosions[2], weirdness, 0.0f, plateau);
                addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), erosions[3], weirdness, 0.0f, middle);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[3], weirdness, 0.0f, middleOrBadlands);
                if (weirdness.max() < 0L) {
                    addSurface(list, temp, hum, coastContinentalness, erosions[4], weirdness, 0.0f, beach);
                    addSurface(list, temp, hum, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0f, middle);
                } else {
                    addSurface(list, temp, hum, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0f, middle);
                }
                addSurface(list, temp, hum, coastContinentalness, erosions[5], weirdness, 0.0f, shatteredCoast);
                addSurface(list, temp, hum, nearInlandContinentalness, erosions[5], weirdness, 0.0f, windsweptSavanna);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0f, shattered);
                if (weirdness.max() < 0L) {
                    addSurface(list, temp, hum, coastContinentalness, erosions[6], weirdness, 0.0f, beach);
                } else {
                    addSurface(list, temp, hum, coastContinentalness, erosions[6], weirdness, 0.0f, middle);
                }
                if (i != 0) continue;
                addSurface(list, temp, hum, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, middle);
            }
        }
    }

    private void addLowSlice(List<SimpleEntry<Climate.ParameterPoint, Integer>> list, Climate.Parameter weirdness) {
        addSurface(list, FULL_RANGE, FULL_RANGE, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[2]), weirdness, 0.0f, B_STONY_SHORE);
        addSurface(list, Climate.Parameter.span(temperatures[1], temperatures[2]), FULL_RANGE,
            Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, B_SWAMP);
        addSurface(list, Climate.Parameter.span(temperatures[3], temperatures[4]), FULL_RANGE,
            Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, B_MANGROVE_SWAMP);
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temp = temperatures[i];
            for (int j = 0; j < humidities.length; j++) {
                Climate.Parameter hum = humidities[j];
                int middle = pickMiddleBiome(i, j, weirdness);
                int middleOrBadlands = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
                int middleOrBadlandsOrSlope = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
                int beach = pickBeachBiome(i, j);
                int windsweptSavanna = maybePickWindsweptSavannaBiome(i, j, weirdness, middle);
                int shatteredCoast = pickShatteredCoastBiome(i, j, weirdness);
                addSurface(list, temp, hum, nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0f, middleOrBadlands);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0f, middleOrBadlandsOrSlope);
                addSurface(list, temp, hum, nearInlandContinentalness, Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0f, middle);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0f, middleOrBadlands);
                addSurface(list, temp, hum, coastContinentalness, Climate.Parameter.span(erosions[3], erosions[4]), weirdness, 0.0f, beach);
                addSurface(list, temp, hum, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0f, middle);
                addSurface(list, temp, hum, coastContinentalness, erosions[5], weirdness, 0.0f, shatteredCoast);
                addSurface(list, temp, hum, nearInlandContinentalness, erosions[5], weirdness, 0.0f, windsweptSavanna);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0f, middle);
                addSurface(list, temp, hum, coastContinentalness, erosions[6], weirdness, 0.0f, beach);
                if (i != 0) continue;
                addSurface(list, temp, hum, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, middle);
            }
        }
    }

    private void addValleys(List<SimpleEntry<Climate.ParameterPoint, Integer>> list, Climate.Parameter weirdness) {
        boolean negative = weirdness.max() < 0L;
        addSurface(list, FROZEN_RANGE, FULL_RANGE, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0f, negative ? B_STONY_SHORE : B_FROZEN_RIVER);
        addSurface(list, UNFROZEN_RANGE, FULL_RANGE, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0f, negative ? B_STONY_SHORE : B_RIVER);
        addSurface(list, FROZEN_RANGE, FULL_RANGE, nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0f, B_FROZEN_RIVER);
        addSurface(list, UNFROZEN_RANGE, FULL_RANGE, nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0f, B_RIVER);
        addSurface(list, FROZEN_RANGE, FULL_RANGE, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0.0f, B_FROZEN_RIVER);
        addSurface(list, UNFROZEN_RANGE, FULL_RANGE, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0.0f, B_RIVER);
        addSurface(list, FROZEN_RANGE, FULL_RANGE, coastContinentalness, erosions[6], weirdness, 0.0f, B_FROZEN_RIVER);
        addSurface(list, UNFROZEN_RANGE, FULL_RANGE, coastContinentalness, erosions[6], weirdness, 0.0f, B_RIVER);
        addSurface(list, Climate.Parameter.span(temperatures[1], temperatures[2]), FULL_RANGE,
            Climate.Parameter.span(inlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, B_SWAMP);
        addSurface(list, Climate.Parameter.span(temperatures[3], temperatures[4]), FULL_RANGE,
            Climate.Parameter.span(inlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, B_MANGROVE_SWAMP);
        addSurface(list, FROZEN_RANGE, FULL_RANGE, Climate.Parameter.span(inlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, B_FROZEN_RIVER);
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temp = temperatures[i];
            for (int j = 0; j < humidities.length; j++) {
                Climate.Parameter hum = humidities[j];
                int middleOrBadlands = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
                addSurface(list, temp, hum, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0f, middleOrBadlands);
            }
        }
    }

    private void addUndergroundBiomes(List<SimpleEntry<Climate.ParameterPoint, Integer>> list) {
        addUnderground(list, FULL_RANGE, FULL_RANGE, Climate.Parameter.span(0.8f, 1.0f), FULL_RANGE, FULL_RANGE, 0.0f, B_DRIPSTONE_CAVES);
        addUnderground(list, FULL_RANGE, Climate.Parameter.span(0.7f, 1.0f), FULL_RANGE, FULL_RANGE, FULL_RANGE, 0.0f, B_LUSH_CAVES);
        addBottom(list, FULL_RANGE, FULL_RANGE, FULL_RANGE, Climate.Parameter.span(erosions[0], erosions[1]), FULL_RANGE, 0.0f, B_DEEP_DARK);
    }

    private int pickMiddleBiome(int tempIdx, int humIdx, Climate.Parameter weirdness) {
        if (weirdness.max() < 0L) return MIDDLE_BIOMES[tempIdx][humIdx];
        int variant = MIDDLE_BIOMES_VARIANT[tempIdx][humIdx];
        return variant == -1 ? MIDDLE_BIOMES[tempIdx][humIdx] : variant;
    }

    private int pickMiddleBiomeOrBadlandsIfHot(int tempIdx, int humIdx, Climate.Parameter weirdness) {
        return tempIdx == 4 ? pickBadlandsBiome(humIdx, weirdness) : pickMiddleBiome(tempIdx, humIdx, weirdness);
    }

    private int pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(int tempIdx, int humIdx, Climate.Parameter weirdness) {
        return tempIdx == 0 ? pickSlopeBiome(tempIdx, humIdx, weirdness) : pickMiddleBiomeOrBadlandsIfHot(tempIdx, humIdx, weirdness);
    }

    private int maybePickWindsweptSavannaBiome(int tempIdx, int humIdx, Climate.Parameter weirdness, int fallback) {
        if (tempIdx > 1 && humIdx < 4 && weirdness.max() >= 0L) return B_WINDSWEPT_SAVANNA;
        return fallback;
    }

    private int pickShatteredCoastBiome(int tempIdx, int humIdx, Climate.Parameter weirdness) {
        int fallback = weirdness.max() >= 0L ? pickMiddleBiome(tempIdx, humIdx, weirdness) : pickBeachBiome(tempIdx, humIdx);
        return maybePickWindsweptSavannaBiome(tempIdx, humIdx, weirdness, fallback);
    }

    private int pickBeachBiome(int tempIdx, int humIdx) {
        if (tempIdx == 0) return B_SNOWY_BEACH;
        if (tempIdx == 4) return B_DESERT;
        return B_BEACH;
    }

    private int pickBadlandsBiome(int humIdx, Climate.Parameter weirdness) {
        if (humIdx < 2) return weirdness.max() < 0L ? B_BADLANDS : B_ERODED_BADLANDS;
        if (humIdx < 3) return B_BADLANDS;
        return B_WOODED_BADLANDS;
    }

    private int pickPlateauBiome(int tempIdx, int humIdx, Climate.Parameter weirdness) {
        if (weirdness.max() >= 0L) {
            int variant = PLATEAU_BIOMES_VARIANT[tempIdx][humIdx];
            if (variant != -1) return variant;
        }
        return PLATEAU_BIOMES[tempIdx][humIdx];
    }

    private int pickPeakBiome(int tempIdx, int humIdx, Climate.Parameter weirdness) {
        if (tempIdx <= 2) return weirdness.max() < 0L ? B_JAGGED_PEAKS : B_FROZEN_PEAKS;
        if (tempIdx == 3) return B_STONY_PEAKS;
        return pickBadlandsBiome(humIdx, weirdness);
    }

    private int pickSlopeBiome(int tempIdx, int humIdx, Climate.Parameter weirdness) {
        if (tempIdx >= 3) return pickPlateauBiome(tempIdx, humIdx, weirdness);
        if (humIdx <= 1) return B_SNOWY_SLOPES;
        return B_GROVE;
    }

    private int pickShatteredBiome(int tempIdx, int humIdx, Climate.Parameter weirdness) {
        int s = SHATTERED_BIOMES[tempIdx][humIdx];
        return s == -1 ? pickMiddleBiome(tempIdx, humIdx, weirdness) : s;
    }

    private void addSurface(List<SimpleEntry<Climate.ParameterPoint, Integer>> list,
                             Climate.Parameter temp, Climate.Parameter hum,
                             Climate.Parameter cont, Climate.Parameter erosion,
                             Climate.Parameter weirdness, float offset, int biome) {
        list.add(new SimpleEntry<>(Climate.parameters(temp, hum, cont, erosion, Climate.Parameter.point(0.0f), weirdness, offset), biome));
        list.add(new SimpleEntry<>(Climate.parameters(temp, hum, cont, erosion, Climate.Parameter.point(1.0f), weirdness, offset), biome));
    }

    private void addUnderground(List<SimpleEntry<Climate.ParameterPoint, Integer>> list,
                                 Climate.Parameter temp, Climate.Parameter hum,
                                 Climate.Parameter cont, Climate.Parameter erosion,
                                 Climate.Parameter weirdness, float offset, int biome) {
        list.add(new SimpleEntry<>(Climate.parameters(temp, hum, cont, erosion, Climate.Parameter.span(0.2f, 0.9f), weirdness, offset), biome));
    }

    private void addBottom(List<SimpleEntry<Climate.ParameterPoint, Integer>> list,
                            Climate.Parameter temp, Climate.Parameter hum,
                            Climate.Parameter cont, Climate.Parameter erosion,
                            Climate.Parameter weirdness, float offset, int biome) {
        list.add(new SimpleEntry<>(Climate.parameters(temp, hum, cont, erosion, Climate.Parameter.point(1.1f), weirdness, offset), biome));
    }
}
