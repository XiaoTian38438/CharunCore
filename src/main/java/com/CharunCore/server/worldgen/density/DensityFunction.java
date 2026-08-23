package com.CharunCore.server.worldgen.density;

import com.CharunCore.server.world.gen.*;
import com.CharunCore.server.worldgen.density.functions.Clamp;
import com.CharunCore.server.worldgen.density.functions.Mapped;
import com.CharunCore.server.worldgen.density.functions.*;

/**
 * 原版 net.minecraft.world.level.levelgen.DensityFunction 的移植版。
 * 1.21.11 密度函数链的顶层抽象 — 所有地形高度/洞穴/矿脉判定都基于此。
 * 接口签名严格对照原版 javap 输出，去除 Codec / Holder 引用（无 Netty <-> Mojang infra 接入）。
 */
public interface DensityFunction {

    double compute(FunctionContext ctx);

    void fillArray(double[] array, ContextProvider provider);

    DensityFunction mapAll(Visitor visitor);

    double minValue();

    double maxValue();

    // ============= default 变换方法（对应原版 interface default methods） =============

    default DensityFunction clamp(double min, double max) {
        return new Clamp(this, min, max);
    }

    default DensityFunction abs() {
        return Mapped.map(this, Mapped.Type.ABS);
    }

    default DensityFunction square() {
        return Mapped.map(this, Mapped.Type.SQUARE);
    }

    default DensityFunction cube() {
        return Mapped.map(this, Mapped.Type.CUBE);
    }

    default DensityFunction halfNegative() {
        return Mapped.map(this, Mapped.Type.HALF_NEGATIVE);
    }

    default DensityFunction quarterNegative() {
        return Mapped.map(this, Mapped.Type.QUARTER_NEGATIVE);
    }

    default DensityFunction invert() {
        return Mapped.map(this, Mapped.Type.INVERT);
    }

    default DensityFunction squeeze() {
        return Mapped.map(this, Mapped.Type.SQUEEZE);
    }

    // ============== 接口内嵌类型 ==============

    /** 上下文 — 提供方块级整数坐标 */
    interface FunctionContext {
        int blockX();
        int blockY();
        int blockZ();
    }

    /** 单点上下文 record — 计算单点密度时使用 */
    record SinglePointContext(int blockX, int blockY, int blockZ) implements FunctionContext {}

    /** 批量填充提供器 */
    interface ContextProvider {
        FunctionContext forIndex(int index);
        void fillAllDirectly(double[] array, DensityFunction function);
    }

    /** Visitor — 用于 mapAll 遍历密度树 */
    interface Visitor {
        DensityFunction apply(DensityFunction function);
    }

    /** 噪声持有者包装 — 用于 DensityFunctions.Noise */
    final class NoiseHolder {
        private static volatile long WORLD_SEED_LO = 0L;
        private static volatile long WORLD_SEED_HI = 0L;
        private static volatile PositionalRandomFactory SHARED_FACTORY = null;

        public static void setWorldSeed(long seedLo, long seedHi) {
            WORLD_SEED_LO = seedLo;
            WORLD_SEED_HI = seedHi;
            // Vanilla: RandomSource.create(seed) / new XoroshiroRandomSource(seed) internally
            // promotes the 64-bit world seed to a 128-bit seed via upgradeSeedTo128bit.
            // Make the promotion explicit here so the shared positional factory is identical
            // to vanilla regardless of how the single-arg constructor evolves.
            RandomSupport.Seed128bit upgraded =
                RandomSupport.upgradeSeedTo128bit(seedLo);
            SHARED_FACTORY = new XoroshiroRandomSource(upgraded).forkPositional();
        }

        public static PositionalRandomFactory sharedFactory() {
            if (SHARED_FACTORY == null) {
                SHARED_FACTORY = new XoroshiroRandomSource(WORLD_SEED_LO).forkPositional();
            }
            return SHARED_FACTORY;
        }

        private final String noiseKey;
        private final NormalNoise.NoiseParameters noiseData;
        private final NormalNoise noise;

        public NoiseHolder(String key, NormalNoise.NoiseParameters data) {
            this(key, data, instantiateNoise(key, data));
        }
        public NoiseHolder(String key, NormalNoise.NoiseParameters data,
                           NormalNoise noise) {
            this.noiseKey = key;
            this.noiseData = data;
            this.noise = noise;
        }

        private static NormalNoise instantiateNoise(
                String key, NormalNoise.NoiseParameters data) {
            if (data == null || key == null) return null;
            try {
                RandomSource r = sharedFactory().fromHashOf(key);
                return new NormalNoise(r, data);
            } catch (Throwable t) {
                return null;
            }
        }
        public double getValue(double x, double y, double z) {
            if (noise == null) return 0.0;
            return noise.getValue(x, y, z);
        }
        public double maxValue() {
            return noise != null ? noise.maxValue() : (noiseData != null ? 1.0 : 0.0);
        }
        public String noiseKey() { return noiseKey; }
        public NormalNoise.NoiseParameters noiseData() { return noiseData; }
        public NormalNoise noise() { return noise; }
    }
}
