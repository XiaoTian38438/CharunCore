package com.CharunCore.server.worldgen.surfacerule;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.NormalNoise;
import com.CharunCore.server.world.gen.NoiseParameters;
import com.CharunCore.server.worldgen.biome.NetherBiomeSource;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.surfacerule.SurfaceRules.ConditionSource;
import com.CharunCore.server.worldgen.surfacerule.SurfaceRules.RuleSource;

import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.UNDER_CEILING;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.UNDER_FLOOR;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.ON_FLOOR;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.hole;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.ifTrue;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.isBiome;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.noiseCondition;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.not;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.sequence;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.state;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.verticalGradient;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.yBlockCheck;
import static com.CharunCore.server.worldgen.surfacerule.SurfaceRules.yStartCheck;

/**
 * 原版 noise_settings/nether.json surface_rule 逐条移植。
 * 序列（首个匹配者生效）：
 *  1. bedrock_floor 梯度 (0..5) -> 基岩
 *  2. NOT bedrock_roof 梯度 (123..128) -> 基岩
 *  3. y >= 123 -> 下界岩（天花板本体保持下界岩）
 *  4. basalt_deltas: 天花板下玄武岩; 地板下 patch噪声30..35->砾石 / selector->玄武岩 else 黑石
 *  5. soul_sand_valley: 天花板下 selector->灵魂沙 else 灵魂土; 地板下 同 4 但 selector->灵魂沙 else 灵魂土
 *  6. ON_FLOOR: y<32 & hole -> 熔岩; 森林群系 nylium（netherrack噪声<0.54 且 y>=31;
 *     nether_wart 噪声>=1.17 处长 wart 方块）
 *  7. nether_wastes: 地板下 soul_sand_layer 30..35 -> 灵魂沙 else 下界岩;
 *     地表 y 31..35 gravel_layer -> 砾石
 */
public final class NetherSurfaceRules {

    public static final int TOP_Y = 128; // nether minY(height) = 0(128) -> below_top:5 = 123

    private NetherSurfaceRules() {}

    private static NormalNoise noise(String name) {
        var factory = DensityFunction.NoiseHolder.sharedFactory();
        return new NormalNoise(factory.fromHashOf(name), NoiseParameters.get(name));
    }

    public static RuleSource nether() {
        int bedrock = BlockStateHelper.getDefault("bedrock");
        int netherrack = BlockStateHelper.getDefault("netherrack");
        int basaltY = BlockStateHelper.withProp(BlockStateHelper.getDefault("basalt"), "axis", "y");
        int blackstone = BlockStateHelper.getDefault("blackstone");
        int gravel = BlockStateHelper.getDefault("gravel");
        int soulSand = BlockStateHelper.getDefault("soul_sand");
        int soulSoil = BlockStateHelper.getDefault("soul_soil");
        int warpedNylium = BlockStateHelper.getDefault("warped_nylium");
        int crimsonNylium = BlockStateHelper.getDefault("crimson_nylium");
        int warpedWart = BlockStateHelper.getDefault("warped_wart_block");
        int netherWart = BlockStateHelper.getDefault("nether_wart_block");
        int lava = BlockStateHelper.getDefault("lava");

        NormalNoise patchNoise = noise("minecraft:patch");
        NormalNoise selectorNoise = noise("minecraft:nether_state_selector");
        NormalNoise soulSandLayerNoise = noise("minecraft:soul_sand_layer");
        NormalNoise gravelLayerNoise = noise("minecraft:gravel_layer");
        NormalNoise netherrackNoise = noise("minecraft:netherrack");
        NormalNoise netherWartNoise = noise("minecraft:nether_wart");

        ConditionSource basaltDeltas = isBiome(b -> b == NetherBiomeSource.B_BASALT_DELTAS);
        ConditionSource soulSandValley = isBiome(b -> b == NetherBiomeSource.B_SOUL_SAND_VALLEY);
        ConditionSource warpedForest = isBiome(b -> b == NetherBiomeSource.B_WARPED_FOREST);
        ConditionSource crimsonForest = isBiome(b -> b == NetherBiomeSource.B_CRIMSON_FOREST);
        ConditionSource netherWastes = isBiome(b -> b == NetherBiomeSource.B_NETHER_WASTES);

        // 砾石带: patch >= -0.012 且 30 <= y < 35
        ConditionSource gravelBand = noiseCondition(patchNoise, -0.012, Double.MAX_VALUE);
        ConditionSource y30up = yStartCheck(30, 0);
        ConditionSource not35up = not(yStartCheck(35, 0));
        RuleSource gravelPatch = ifTrue(gravelBand,
            ifTrue(y30up, ifTrue(not35up, state(gravel))));

        // selector >= 0 -> 首选方块 else 次选方块
        return sequence(
            ifTrue(verticalGradient("minecraft:bedrock_floor", 0, 5), state(bedrock)),
            ifTrue(not(verticalGradient("minecraft:bedrock_roof", TOP_Y - 5, TOP_Y)), state(bedrock)),
            ifTrue(yBlockCheck(TOP_Y - 5, 0), state(netherrack)),

            ifTrue(basaltDeltas, sequence(
                ifTrue(UNDER_CEILING, state(basaltY)),
                ifTrue(UNDER_FLOOR, sequence(
                    gravelPatch,
                    ifTrue(noiseCondition(selectorNoise, 0.0, Double.MAX_VALUE), state(basaltY)),
                    state(blackstone))))),

            ifTrue(soulSandValley, sequence(
                ifTrue(UNDER_CEILING, sequence(
                    ifTrue(noiseCondition(selectorNoise, 0.0, Double.MAX_VALUE), state(soulSand)),
                    state(soulSoil))),
                ifTrue(UNDER_FLOOR, sequence(
                    gravelPatch,
                    ifTrue(noiseCondition(selectorNoise, 0.0, Double.MAX_VALUE), state(soulSand)),
                    state(soulSoil))))),

            ifTrue(ON_FLOOR, sequence(
                ifTrue(not(yBlockCheck(32, 0)), ifTrue(hole(), state(lava))),
                ifTrue(warpedForest, ifTrue(not(noiseCondition(netherrackNoise, 0.54, Double.MAX_VALUE)),
                    ifTrue(yBlockCheck(31, 0), sequence(
                        ifTrue(noiseCondition(netherWartNoise, 1.17, Double.MAX_VALUE), state(warpedWart)),
                        state(warpedNylium))))),
                ifTrue(crimsonForest, ifTrue(not(noiseCondition(netherrackNoise, 0.54, Double.MAX_VALUE)),
                    ifTrue(yBlockCheck(31, 0), sequence(
                        ifTrue(noiseCondition(netherWartNoise, 1.17, Double.MAX_VALUE), state(netherWart)),
                        state(crimsonNylium))))))),

            ifTrue(netherWastes, sequence(
                ifTrue(UNDER_FLOOR, ifTrue(noiseCondition(soulSandLayerNoise, -0.012, Double.MAX_VALUE),
                    sequence(
                        ifTrue(not(hole()), ifTrue(yStartCheck(30, 0), ifTrue(not(yStartCheck(35, 0)), state(soulSand)))),
                        state(netherrack)))),
                ifTrue(ON_FLOOR, ifTrue(yBlockCheck(31, 0), ifTrue(not(yStartCheck(35, 0)),
                    ifTrue(noiseCondition(gravelLayerNoise, -0.012, Double.MAX_VALUE),
                        sequence(
                            ifTrue(yBlockCheck(32, 0), state(gravel)),
                            ifTrue(not(hole()), state(gravel))))))))));
    }
}
