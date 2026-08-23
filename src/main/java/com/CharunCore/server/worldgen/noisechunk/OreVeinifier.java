package com.CharunCore.server.worldgen.noisechunk;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.Mth;
import com.CharunCore.server.world.gen.PositionalRandomFactory;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.density.DensityFunction;

public final class OreVeinifier {

    private static final float VEININESS_THRESHOLD = 0.4f;
    private static final int EDGE_ROUNDOFF_BEGIN = 20;
    private static final double MAX_EDGE_ROUNDOFF = 0.2;
    private static final float VEIN_SOLIDNESS = 0.7f;
    private static final float MIN_RICHNESS = 0.1f;
    private static final float MAX_RICHNESS = 0.3f;
    private static final float MAX_RICHNESS_THRESHOLD = 0.6f;
    private static final float CHANCE_OF_RAW_ORE_BLOCK = 0.02f;
    private static final float SKIP_ORE_IF_GAP_NOISE_IS_BELOW = -0.3f;

    private static class BlockIds {
        static final int copperOre = BlockStateHelper.getDefault("copper_ore");
        static final int rawCopper = BlockStateHelper.getDefault("raw_copper_block");
        static final int granite = BlockStateHelper.getDefault("granite");
        static final int deepslateIronOre = BlockStateHelper.getDefault("deepslate_iron_ore");
        static final int rawIron = BlockStateHelper.getDefault("raw_iron_block");
        static final int tuff = BlockStateHelper.getDefault("tuff");
        // 下界变体：铁脉 → 下界金矿石 + 远古残骸，填充下界岩
        static final int netherGoldOre = BlockStateHelper.getDefault("nether_gold_ore");
        static final int ancientDebris = BlockStateHelper.getDefault("ancient_debris");
        static final int netherrack = BlockStateHelper.getDefault("netherrack");
    }

    public static NoiseChunk.BlockStateFiller create(DensityFunction veinToggle, DensityFunction veinRidged,
                                                      DensityFunction veinGap,
                                                      PositionalRandomFactory randomFactory,
                                                      boolean isNether) {
        return ctx -> {
            double toggle = veinToggle.compute(ctx);
            int y = ctx.blockY();
            int ore, rawOreBlock, filler, minY, maxY;

            if (isNether) {
                // 下界只有「铁脉(toggle<=0)」承载矿石：下界金矿石 + 远古残骸(2% 稀有块) + 下界岩填充。
                // 铜脉(toggle>0)在下界不存在（下界无铜矿石），直接禁用，避免错误生成 copper_ore/granite。
                // Y 范围 0..30：下界低层矿脉带，与散矿(ancient_debris Y8-119)互补。
                if (toggle > 0.0) return null;
                ore = BlockIds.netherGoldOre;
                rawOreBlock = BlockIds.ancientDebris;
                filler = BlockIds.netherrack;
                minY = 0;
                maxY = 30;
            } else if (toggle > 0.0) {
                ore = BlockIds.copperOre;
                rawOreBlock = BlockIds.rawCopper;
                filler = BlockIds.granite;
                minY = 0;
                maxY = 50;
            } else {
                ore = BlockIds.deepslateIronOre;
                rawOreBlock = BlockIds.rawIron;
                filler = BlockIds.tuff;
                minY = -60;
                maxY = -8;
            }

            double absToggle = Math.abs(toggle);
            int distToMax = maxY - y;
            int distToMin = y - minY;

            if (distToMin < 0 || distToMax < 0) {
                return null;
            }

            int minDist = Math.min(distToMax, distToMin);
            double edgeRoundoff = Mth.clampedMap(minDist, 0, EDGE_ROUNDOFF_BEGIN, -MAX_EDGE_ROUNDOFF, 0.0);

            if (absToggle + edgeRoundoff < VEININESS_THRESHOLD) {
                return null;
            }

            RandomSource random = randomFactory.at(ctx.blockX(), y, ctx.blockZ());
            if (random.nextFloat() > VEIN_SOLIDNESS) {
                return null;
            }

            if (veinRidged.compute(ctx) >= 0.0) {
                return null;
            }

            double richness = Mth.clampedMap(absToggle, VEININESS_THRESHOLD, MAX_RICHNESS_THRESHOLD,
                    MIN_RICHNESS, MAX_RICHNESS);
            if (random.nextFloat() < richness && veinGap.compute(ctx) > SKIP_ORE_IF_GAP_NOISE_IS_BELOW) {
                return random.nextFloat() < CHANCE_OF_RAW_ORE_BLOCK ? rawOreBlock : ore;
            }

            return filler;
        };
    }
}
