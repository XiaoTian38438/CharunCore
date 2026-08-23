package com.CharunCore.server.worldgen.surfacerule;

import java.util.List;
import java.util.function.IntPredicate;

import com.CharunCore.server.world.gen.NormalNoise;
import com.CharunCore.server.world.gen.Mth;
import com.CharunCore.server.world.gen.PositionalRandomFactory;

public class SurfaceRules {

    public static final int FLOOR = 0;
    public static final int CEILING = 1;

    public static final ConditionSource ON_FLOOR = stoneDepthCheck(0, false, FLOOR);
    public static final ConditionSource UNDER_FLOOR = stoneDepthCheck(0, true, FLOOR);
    public static final ConditionSource DEEP_UNDER_FLOOR = stoneDepthCheck(0, true, 6, FLOOR);
    public static final ConditionSource VERY_DEEP_UNDER_FLOOR = stoneDepthCheck(0, true, 30, FLOOR);
    public static final ConditionSource ON_CEILING = stoneDepthCheck(0, false, CEILING);
    public static final ConditionSource UNDER_CEILING = stoneDepthCheck(0, true, CEILING);

    public static ConditionSource stoneDepthCheck(int offset, boolean addSurfaceDepth, int surfaceType) {
        return new StoneDepthCheck(offset, addSurfaceDepth, 0, surfaceType);
    }

    public static ConditionSource stoneDepthCheck(int offset, boolean addSurfaceDepth, int secondaryDepthRange, int surfaceType) {
        return new StoneDepthCheck(offset, addSurfaceDepth, secondaryDepthRange, surfaceType);
    }

    public static ConditionSource not(ConditionSource target) {
        return new NotConditionSource(target);
    }

    public static ConditionSource yBlockCheck(int anchorY, int surfaceDepthMultiplier) {
        return new YConditionSource(anchorY, surfaceDepthMultiplier, false);
    }

    public static ConditionSource yStartCheck(int anchorY, int surfaceDepthMultiplier) {
        return new YConditionSource(anchorY, surfaceDepthMultiplier, true);
    }

    public static ConditionSource waterBlockCheck(int offset, int surfaceDepthMultiplier) {
        return new WaterConditionSource(offset, surfaceDepthMultiplier, false);
    }

    public static ConditionSource waterStartCheck(int offset, int surfaceDepthMultiplier) {
        return new WaterConditionSource(offset, surfaceDepthMultiplier, true);
    }

    public static ConditionSource isBiome(IntPredicate biomeTest) {
        return new BiomeConditionSource(biomeTest);
    }

    public static ConditionSource noiseCondition(NormalNoise noise, double min, double max) {
        return new NoiseThresholdConditionSource(noise, min, max);
    }

    public static ConditionSource steep() {
        return Steep.INSTANCE;
    }

    public static ConditionSource hole() {
        return Hole.INSTANCE;
    }

    public static ConditionSource abovePreliminarySurface() {
        return AbovePreliminarySurface.INSTANCE;
    }

    public static ConditionSource temperature() {
        return Temperature.INSTANCE;
    }

    public static ConditionSource verticalGradient(String randomName, int trueAtAndBelow, int falseAtAndAbove) {
        return new VerticalGradientConditionSource(randomName, trueAtAndBelow, falseAtAndAbove);
    }

    public static RuleSource ifTrue(ConditionSource condition, RuleSource thenRun) {
        return new TestRuleSource(condition, thenRun);
    }

    public static RuleSource sequence(RuleSource... rules) {
        if (rules.length == 0) {
            throw new IllegalArgumentException("Need at least 1 rule for a sequence");
        }
        return new SequenceRuleSource(List.of(rules));
    }

    public static RuleSource state(int blockState) {
        return new BlockRuleSource(blockState);
    }

    public static RuleSource bandlands() {
        return Bandlands.INSTANCE;
    }

    public static interface Condition {
        boolean test();
    }

    public static interface ConditionSource {
        Condition apply(SurfaceRuleContext context);
    }

    public static interface SurfaceRule {
        int tryApply(int x, int y, int z);
    }

    public static interface RuleSource {
        SurfaceRule apply(SurfaceRuleContext context);
    }

    static abstract class LazyCondition implements Condition {
        protected final SurfaceRuleContext context;

        protected LazyCondition(SurfaceRuleContext context) {
            this.context = context;
        }

        @Override
        public boolean test() {
            return compute();
        }

        protected abstract boolean compute();
    }

    static abstract class LazyYCondition extends LazyCondition {
        protected int lastUpdateY = Integer.MIN_VALUE;
        protected boolean cachedResult;

        protected LazyYCondition(SurfaceRuleContext context) {
            super(context);
        }

        @Override
        public boolean test() {
            if (this.lastUpdateY != context.lastUpdateY) {
                this.lastUpdateY = context.lastUpdateY;
                this.cachedResult = compute();
            }
            return this.cachedResult;
        }
    }

    static abstract class LazyXZCondition extends LazyCondition {
        protected int lastUpdateXZ = Integer.MIN_VALUE;
        protected boolean cachedResult;

        protected LazyXZCondition(SurfaceRuleContext context) {
            super(context);
        }

        @Override
        public boolean test() {
            if (this.lastUpdateXZ != context.lastUpdateXZ) {
                this.lastUpdateXZ = context.lastUpdateXZ;
                this.cachedResult = compute();
            }
            return this.cachedResult;
        }
    }

    static final class StoneDepthCheck implements ConditionSource {
        final int offset;
        final boolean addSurfaceDepth;
        final int secondaryDepthRange;
        final int surfaceType;

        StoneDepthCheck(int offset, boolean addSurfaceDepth, int secondaryDepthRange, int surfaceType) {
            this.offset = offset;
            this.addSurfaceDepth = addSurfaceDepth;
            this.secondaryDepthRange = secondaryDepthRange;
            this.surfaceType = surfaceType;
        }

        @Override
        public Condition apply(SurfaceRuleContext context) {
            return new StoneDepthCondition(context);
        }

        class StoneDepthCondition extends LazyYCondition {
            StoneDepthCondition(SurfaceRuleContext context) {
                super(context);
            }

            @Override
            protected boolean compute() {
                boolean isCeiling = surfaceType == 1;
                int depth = isCeiling ? context.stoneDepthBelow : context.stoneDepthAbove;
                int surfaceD = addSurfaceDepth ? context.surfaceDepth : 0;
                int secondary = secondaryDepthRange == 0 ? 0
                    : (int) map(context.getSurfaceSecondary(), -1.0, 1.0, 0.0, secondaryDepthRange);
                return depth <= 1 + offset + surfaceD + secondary;
            }
        }
    }

    static final class NotConditionSource implements ConditionSource {
        final ConditionSource target;

        NotConditionSource(ConditionSource target) {
            this.target = target;
        }

        @Override
        public Condition apply(SurfaceRuleContext context) {
            return new NotCondition(target.apply(context));
        }

        record NotCondition(Condition inner) implements Condition {
            @Override
            public boolean test() {
                return !inner.test();
            }
        }
    }

    static final class YConditionSource implements ConditionSource {
        final int anchorY;
        final int surfaceDepthMultiplier;
        final boolean addStoneDepth;

        YConditionSource(int anchorY, int surfaceDepthMultiplier, boolean addStoneDepth) {
            this.anchorY = anchorY;
            this.surfaceDepthMultiplier = surfaceDepthMultiplier;
            this.addStoneDepth = addStoneDepth;
        }

        @Override
        public Condition apply(SurfaceRuleContext context) {
            return new YCondition(context);
        }

        class YCondition extends LazyYCondition {
            YCondition(SurfaceRuleContext context) {
                super(context);
            }

            @Override
            protected boolean compute() {
                int y = context.blockY;
                if (addStoneDepth) y += context.stoneDepthAbove;
                return y >= anchorY + context.surfaceDepth * surfaceDepthMultiplier;
            }
        }
    }

    static final class WaterConditionSource implements ConditionSource {
        final int offset;
        final int surfaceDepthMultiplier;
        final boolean addStoneDepth;

        WaterConditionSource(int offset, int surfaceDepthMultiplier, boolean addStoneDepth) {
            this.offset = offset;
            this.surfaceDepthMultiplier = surfaceDepthMultiplier;
            this.addStoneDepth = addStoneDepth;
        }

        @Override
        public Condition apply(SurfaceRuleContext context) {
            return new WaterCondition(context);
        }

        class WaterCondition extends LazyYCondition {
            WaterCondition(SurfaceRuleContext context) {
                super(context);
            }

            @Override
            protected boolean compute() {
                int y = context.blockY;
                if (addStoneDepth) y += context.stoneDepthAbove;
                int wh = context.waterHeight;
                return wh == Integer.MIN_VALUE || y >= wh + offset + context.surfaceDepth * surfaceDepthMultiplier;
            }
        }
    }

    static final class BiomeConditionSource implements ConditionSource {
        final IntPredicate biomeTest;

        BiomeConditionSource(IntPredicate biomeTest) {
            this.biomeTest = biomeTest;
        }

        @Override
        public Condition apply(SurfaceRuleContext context) {
            return new BiomeCondition(context);
        }

        class BiomeCondition extends LazyYCondition {
            BiomeCondition(SurfaceRuleContext context) {
                super(context);
            }

            @Override
            protected boolean compute() {
                return biomeTest.test(context.getBiome());
            }
        }
    }

    static final class NoiseThresholdConditionSource implements ConditionSource {
        final NormalNoise noise;
        final double min;
        final double max;

        NoiseThresholdConditionSource(NormalNoise noise, double min, double max) {
            this.noise = noise;
            this.min = min;
            this.max = max;
        }

        @Override
        public Condition apply(SurfaceRuleContext context) {
            return new NoiseThresholdCondition(context);
        }

        class NoiseThresholdCondition extends LazyXZCondition {
            NoiseThresholdCondition(SurfaceRuleContext context) {
                super(context);
            }

            @Override
            protected boolean compute() {
                double value = noise.getValue(context.blockX, 0, context.blockZ);
                return value >= min && value <= max;
            }
        }
    }

    static enum Steep implements ConditionSource {
        INSTANCE;

        @Override
        public Condition apply(SurfaceRuleContext context) {
            return context.steep;
        }
    }

    static enum Hole implements ConditionSource {
        INSTANCE;

        @Override
        public Condition apply(SurfaceRuleContext context) {
            return context.hole;
        }
    }

    static enum AbovePreliminarySurface implements ConditionSource {
        INSTANCE;

        @Override
        public Condition apply(SurfaceRuleContext context) {
            return context.abovePreliminarySurface;
        }
    }

    static enum Temperature implements ConditionSource {
        INSTANCE;

        @Override
        public Condition apply(SurfaceRuleContext context) {
            return context.temperature;
        }
    }

    static final class TestRuleSource implements RuleSource {
        final ConditionSource condition;
        final RuleSource thenRun;

        TestRuleSource(ConditionSource condition, RuleSource thenRun) {
            this.condition = condition;
            this.thenRun = thenRun;
        }

        @Override
        public SurfaceRule apply(SurfaceRuleContext context) {
            Condition cond = condition.apply(context);
            SurfaceRule follow = thenRun.apply(context);
            return new TestRule(cond, follow);
        }

        record TestRule(Condition condition, SurfaceRule follow) implements SurfaceRule {
            @Override
            public int tryApply(int x, int y, int z) {
                if (condition.test()) {
                    return follow.tryApply(x, y, z);
                }
                return Integer.MIN_VALUE;
            }
        }
    }

    static final class SequenceRuleSource implements RuleSource {
        final List<RuleSource> sequence;

        SequenceRuleSource(List<RuleSource> sequence) {
            this.sequence = sequence;
        }

        @Override
        public SurfaceRule apply(SurfaceRuleContext context) {
            if (sequence.size() == 1) {
                return sequence.get(0).apply(context);
            }
            SurfaceRule[] rules = new SurfaceRule[sequence.size()];
            for (int i = 0; i < sequence.size(); i++) {
                rules[i] = sequence.get(i).apply(context);
            }
            return new SequenceRule(rules);
        }

        record SequenceRule(SurfaceRule[] rules) implements SurfaceRule {
            @Override
            public int tryApply(int x, int y, int z) {
                for (SurfaceRule rule : rules) {
                    int result = rule.tryApply(x, y, z);
                    if (result != Integer.MIN_VALUE) {
                        return result;
                    }
                }
                return Integer.MIN_VALUE;
            }
        }
    }

    static final class BlockRuleSource implements RuleSource {
        final int resultState;

        BlockRuleSource(int resultState) {
            this.resultState = resultState;
        }

        @Override
        public SurfaceRule apply(SurfaceRuleContext context) {
            return new StateRule(resultState);
        }

    record StateRule(int state) implements SurfaceRule {
        @Override
        public int tryApply(int x, int y, int z) {
            return state;
        }
    }
    }

    static enum Bandlands implements RuleSource {
        INSTANCE;

        @Override
        public SurfaceRule apply(SurfaceRuleContext context) {
            return (x, y, z) -> context.system.getBand(x, y, z);
        }
    }

    static final class VerticalGradientConditionSource implements ConditionSource {
        final String randomName;
        final int trueAtAndBelow;
        final int falseAtAndAbove;

        VerticalGradientConditionSource(String randomName, int trueAtAndBelow, int falseAtAndAbove) {
            this.randomName = randomName;
            this.trueAtAndBelow = trueAtAndBelow;
            this.falseAtAndAbove = falseAtAndAbove;
        }

        @Override
        public Condition apply(SurfaceRuleContext context) {
            PositionalRandomFactory factory = context.getRandomFactory(randomName);
            return new VerticalGradientCondition(context, factory);
        }

        class VerticalGradientCondition extends LazyYCondition {
            private final PositionalRandomFactory factory;

            VerticalGradientCondition(SurfaceRuleContext context, PositionalRandomFactory factory) {
                super(context);
                this.factory = factory;
            }

            @Override
            protected boolean compute() {
                int y = context.blockY;
                if (y <= trueAtAndBelow) return true;
                if (y >= falseAtAndAbove) return false;
                double d = Mth.map((double) y, (double) trueAtAndBelow, (double) falseAtAndAbove, 1.0, 0.0);
                return factory.at(context.blockX, y, context.blockZ).nextFloat() < d;
            }
        }
    }

    public static double map(double value, double fromMin, double fromMax, double toMin, double toMax) {
        return toMin + (value - fromMin) * (toMax - toMin) / (fromMax - fromMin);
    }
}
