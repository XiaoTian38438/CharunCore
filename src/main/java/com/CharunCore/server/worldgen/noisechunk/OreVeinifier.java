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
        // Bug20: 原版 nether.json 明确 ore_veins_enabled=false —— 下界没有矿脉系统。
        // 曾自加"下界金矿脉/远古残骸脉" -> 下界岩里出现原版没有的大片矿脉结构。
        if (isNether) {
            return ctx -> null;
        }
        return ctx -> {
            double toggle = veinToggle.compute(ctx);
            int y = ctx.blockY();
            int ore, rawOreBlock, filler, minY, maxY;

            if (toggle > 0.0) {
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
