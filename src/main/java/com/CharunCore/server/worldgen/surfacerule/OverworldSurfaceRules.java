package com.CharunCore.server.worldgen.surfacerule;

import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.*;

import java.util.function.IntPredicate;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.world.gen.NormalNoise;
import com.CharunCore.server.world.gen.NoiseParameters;

public class OverworldSurfaceRules {

    private static final int AIR = sid("air");
    private static final int BEDROCK = sid("bedrock");
    private static final int WHITE_TERRACOTTA = sid("white_terracotta");
    private static final int ORANGE_TERRACOTTA = sid("orange_terracotta");
    private static final int TERRACOTTA = sid("terracotta");
    private static final int RED_SAND = sid("red_sand");
    private static final int RED_SANDSTONE = sid("red_sandstone");
    private static final int STONE = sid("stone");
    private static final int DEEPSLATE = sid("deepslate");
    private static final int DIRT = sid("dirt");
    private static final int PODZOL = sid("podzol");
    private static final int COARSE_DIRT = sid("coarse_dirt");
    private static final int MYCELIUM = sid("mycelium");
    private static final int GRASS_BLOCK = sid("grass_block");
    private static final int CALCITE = sid("calcite");
    private static final int GRAVEL = sid("gravel");
    private static final int SAND = sid("sand");
    private static final int SANDSTONE = sid("sandstone");
    private static final int PACKED_ICE = sid("packed_ice");
    private static final int SNOW_BLOCK = sid("snow_block");
    private static final int MUD = sid("mud");
    private static final int POWDER_SNOW = sid("powder_snow");
    private static final int ICE = sid("ice");
    private static final int WATER = sid("water");

    private static int sid(String name) {
        return BlockStateHelper.getDefault(name);
    }

    private static NormalNoise makeNoise(String name) {
        return new NormalNoise(
            DensityFunction.NoiseHolder.sharedFactory().fromHashOf(name),
            NoiseParameters.get(name));
    }

    public static RuleSource overworld() {
        NormalNoise surfaceNoise = makeNoise("minecraft:surface");
        NormalNoise calciteNoise = makeNoise("minecraft:calcite");
        NormalNoise gravelNoise = makeNoise("minecraft:gravel");
        NormalNoise packedIceNoise = makeNoise("minecraft:packed_ice");
        NormalNoise iceNoise = makeNoise("minecraft:ice");
        NormalNoise powderSnowNoise = makeNoise("minecraft:powder_snow");
        NormalNoise swampNoise = makeNoise("minecraft:surface_swamp");

        ConditionSource condY97 = yBlockCheck(97, 2);
        ConditionSource condY256 = yBlockCheck(256, 0);
        ConditionSource condY63Start = yStartCheck(63, -1);
        ConditionSource condY74Start = yStartCheck(74, 1);
        ConditionSource condY60 = yBlockCheck(60, 0);
        ConditionSource condY62 = yBlockCheck(62, 0);
        ConditionSource condY63 = yBlockCheck(63, 0);
        ConditionSource waterCheckM1 = waterBlockCheck(-1, 0);
        ConditionSource waterCheck0 = waterBlockCheck(0, 0);
        ConditionSource waterStartM6 = waterStartCheck(-6, -1);
        ConditionSource holeCond = hole();
        ConditionSource steepCond = steep();
        ConditionSource frozenOceanBiome = isBiome(b -> b == 22 || b == 11);
        ConditionSource warmOceanBeachBiome = isBiome(b -> b == 58 || b == 3 || b == 45);
        ConditionSource desertBiome = isBiome(b -> b == 14);

        RuleSource ruleSource1 = sequence(ifTrue(waterCheck0, state(GRASS_BLOCK)), state(DIRT));
        RuleSource ruleSource2 = sequence(ifTrue(ON_CEILING, state(SANDSTONE)), state(SAND));
        RuleSource ruleSource3 = sequence(ifTrue(ON_CEILING, state(STONE)), state(GRAVEL));

        RuleSource ruleSource4 = sequence(
            biomeIf(b -> b == 51, sequence(
                ifTrue(noiseCondition(calciteNoise, -0.0125, 0.0125), state(CALCITE)), state(STONE))),
            biomeIf(b -> b == 52, sequence(
                ifTrue(noiseCondition(gravelNoise, -0.05, 0.05), ruleSource3), state(STONE))),
            biomeIf(b -> b == 62, ifTrue(surfaceNoiseAbove(surfaceNoise, 1.0), state(STONE))),
            ifTrue(warmOceanBeachBiome, ruleSource2),
            ifTrue(desertBiome, ruleSource2),
            biomeIf(b -> b == 15, state(STONE))
        );

        RuleSource ruleSource5 = ifTrue(noiseCondition(powderSnowNoise, 0.45, 0.58),
            ifTrue(waterCheck0, state(POWDER_SNOW)));
        RuleSource ruleSource6 = ifTrue(noiseCondition(powderSnowNoise, 0.35, 0.6),
            ifTrue(waterCheck0, state(POWDER_SNOW)));

        RuleSource ruleSource7 = sequence(
            biomeIf(b -> b == 23, sequence(
                ifTrue(steepCond, state(PACKED_ICE)),
                ifTrue(noiseCondition(packedIceNoise, -0.5, 0.2), state(PACKED_ICE)),
                ifTrue(noiseCondition(iceNoise, -0.0625, 0.025), state(ICE)),
                ifTrue(waterCheck0, state(SNOW_BLOCK)))),
            biomeIf(b -> b == 47, sequence(
                ifTrue(steepCond, state(STONE)),
                ruleSource5,
                ifTrue(waterCheck0, state(SNOW_BLOCK)))),
            biomeIf(b -> b == 27, state(STONE)),
            biomeIf(b -> b == 25, sequence(ruleSource5, state(DIRT))),
            ruleSource4,
            biomeIf(b -> b == 63, ifTrue(surfaceNoiseAbove(surfaceNoise, 1.75), state(STONE))),
            biomeIf(b -> b == 61, sequence(
                ifTrue(surfaceNoiseAbove(surfaceNoise, 2.0), ruleSource3),
                ifTrue(surfaceNoiseAbove(surfaceNoise, 1.0), state(STONE)),
                ifTrue(surfaceNoiseAbove(surfaceNoise, -1.0), state(DIRT)),
                ruleSource3)),
            biomeIf(b -> b == 31, state(MUD)),
            state(DIRT)
        );

        RuleSource ruleSource8 = sequence(
            biomeIf(b -> b == 23, sequence(
                ifTrue(steepCond, state(PACKED_ICE)),
                ifTrue(noiseCondition(packedIceNoise, 0.0, 0.2), state(PACKED_ICE)),
                ifTrue(noiseCondition(iceNoise, 0.0, 0.025), state(ICE)),
                ifTrue(waterCheck0, state(SNOW_BLOCK)))),
            biomeIf(b -> b == 47, sequence(
                ifTrue(steepCond, state(STONE)),
                ruleSource6,
                ifTrue(waterCheck0, state(SNOW_BLOCK)))),
            biomeIf(b -> b == 27, sequence(
                ifTrue(steepCond, state(STONE)),
                ifTrue(waterCheck0, state(SNOW_BLOCK)))),
            biomeIf(b -> b == 25, sequence(ruleSource6, ifTrue(waterCheck0, state(SNOW_BLOCK)))),
            ruleSource4,
            biomeIf(b -> b == 63, sequence(
                ifTrue(surfaceNoiseAbove(surfaceNoise, 1.75), state(STONE)),
                ifTrue(surfaceNoiseAbove(surfaceNoise, -0.5), state(COARSE_DIRT)))),
            biomeIf(b -> b == 61, sequence(
                ifTrue(surfaceNoiseAbove(surfaceNoise, 2.0), ruleSource3),
                ifTrue(surfaceNoiseAbove(surfaceNoise, 1.0), state(STONE)),
                ifTrue(surfaceNoiseAbove(surfaceNoise, -1.0), ruleSource1),
                ruleSource3)),
            biomeIf(b -> b == 37 || b == 38, sequence(
                ifTrue(surfaceNoiseAbove(surfaceNoise, 1.75), state(COARSE_DIRT)),
                ifTrue(surfaceNoiseAbove(surfaceNoise, -0.95), state(PODZOL)))),
            biomeIf(b -> b == 26, ifTrue(waterCheck0, state(SNOW_BLOCK))),
            biomeIf(b -> b == 31, state(MUD)),
            biomeIf(b -> b == 33, state(MYCELIUM)),
            ruleSource1
        );

        ConditionSource condNoiseLow = noiseCondition(surfaceNoise, -0.909, -0.5454);
        ConditionSource condNoiseMid = noiseCondition(surfaceNoise, -0.1818, 0.1818);
        ConditionSource condNoiseHigh = noiseCondition(surfaceNoise, 0.5454, 0.909);

        IntPredicate badlandsBiome = b -> b == 0 || b == 19 || b == 64;
        IntPredicate woodedBadlandsBiome = b -> b == 64;
        IntPredicate swampBiome = b -> b == 54;
        IntPredicate mangroveBiome = b -> b == 31;
        IntPredicate frozenPeaksJaggedPeaks = b -> b == 23 || b == 27;
        IntPredicate warmLukewarmOcean = b -> b == 58 || b == 29 || b == 12;
        IntPredicate snowyLandBiome = b -> b == 46 || b == 48;

        RuleSource ruleSource9 = sequence(
            ifTrue(ON_FLOOR, sequence(
                ifTrue(isBiome(woodedBadlandsBiome),
                    ifTrue(condY97, sequence(
                        ifTrue(condNoiseLow, state(COARSE_DIRT)),
                        ifTrue(condNoiseMid, state(COARSE_DIRT)),
                        ifTrue(condNoiseHigh, state(COARSE_DIRT)),
                        ruleSource1))),
                ifTrue(isBiome(swampBiome),
                    ifTrue(condY62,
                        ifTrue(not(condY63),
                            ifTrue(noiseCondition(swampNoise, 0.0, Double.MAX_VALUE), state(WATER))))),
                ifTrue(isBiome(mangroveBiome),
                    ifTrue(condY60,
                        ifTrue(not(condY63),
                            ifTrue(noiseCondition(swampNoise, 0.0, Double.MAX_VALUE), state(WATER))))))),
            ifTrue(isBiome(badlandsBiome), sequence(
                ifTrue(ON_FLOOR, sequence(
                    ifTrue(condY256, state(ORANGE_TERRACOTTA)),
                    ifTrue(condY74Start, sequence(
                        ifTrue(condNoiseLow, state(TERRACOTTA)),
                        ifTrue(condNoiseMid, state(TERRACOTTA)),
                        ifTrue(condNoiseHigh, state(TERRACOTTA)),
                        bandlands())),
                    ifTrue(waterCheckM1, sequence(ifTrue(ON_CEILING, state(RED_SANDSTONE)), state(RED_SAND))),
                    ifTrue(not(holeCond), state(ORANGE_TERRACOTTA)),
                    ifTrue(waterStartM6, state(WHITE_TERRACOTTA)),
                    ruleSource3)),
                ifTrue(condY63Start, sequence(
                    ifTrue(condY63, ifTrue(not(condY74Start), state(ORANGE_TERRACOTTA))),
                    bandlands())),
                ifTrue(UNDER_FLOOR, ifTrue(waterStartM6, state(WHITE_TERRACOTTA))))),
            ifTrue(ON_FLOOR, ifTrue(waterCheckM1, sequence(
                ifTrue(frozenOceanBiome, ifTrue(holeCond, sequence(
                    ifTrue(waterCheck0, state(AIR)),
                    ifTrue(temperature(), state(ICE)),
                    state(WATER)))),
                ifTrue(isBiome(snowyLandBiome), state(SNOW_BLOCK)),
                ruleSource8))),
            ifTrue(waterStartM6, sequence(
                ifTrue(ON_FLOOR, ifTrue(frozenOceanBiome, ifTrue(holeCond, state(WATER)))),
                ifTrue(UNDER_FLOOR, ruleSource7),
                ifTrue(warmOceanBeachBiome, ifTrue(DEEP_UNDER_FLOOR, state(SANDSTONE))),
                ifTrue(desertBiome, ifTrue(VERY_DEEP_UNDER_FLOOR, state(SANDSTONE))))),
            ifTrue(ON_FLOOR, sequence(
                ifTrue(isBiome(frozenPeaksJaggedPeaks), state(STONE)),
                ifTrue(isBiome(warmLukewarmOcean), ruleSource2),
                ruleSource3))
        );

        return sequence(
            ifTrue(verticalGradient("bedrock_floor", -64, -59), state(BEDROCK)),
            ifTrue(abovePreliminarySurface(), ruleSource9),
            ifTrue(verticalGradient("deepslate", 0, 8), state(DEEPSLATE))
        );
    }

    private static ConditionSource surfaceNoiseAbove(NormalNoise surfaceNoise, double threshold) {
        return noiseCondition(surfaceNoise, threshold / 8.25, Double.MAX_VALUE);
    }

    private static RuleSource biomeIf(IntPredicate biomeTest, RuleSource thenRun) {
        return ifTrue(isBiome(biomeTest), thenRun);
    }
}
