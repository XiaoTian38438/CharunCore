package com.CharunCore.server.worldgen.noisechunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.CharunCore.server.world.gen.Mth;
import com.CharunCore.server.world.gen.PositionalRandomFactory;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.DensityFunctions;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.functions.Marker;
import com.CharunCore.server.worldgen.noisechunk.aquifer.Aquifer;
import com.CharunCore.server.worldgen.noisechunk.aquifer.NoiseBasedAquifer;

/**
 * 原版 net.minecraft.world.level.levelgen.NoiseChunk 移植 (Stage 0G+)。
 * 负责噪声密度函数的坐标插值、缓存与单元格计算。
 * 含 Aquifer 支持（流体质判定）。
 */
public class NoiseChunk implements DensityFunction.ContextProvider, DensityFunction.FunctionContext {

    public final int cellCountXZ;
    public final int cellCountY;
    public final int cellNoiseMinY;
    private final int firstCellX;
    private final int firstCellZ;
    final int firstNoiseX;
    final int firstNoiseZ;
    final List<NoiseInterpolator> interpolators;
    final List<CacheAllInCell> cellCaches;
    private final Map<DensityFunction, DensityFunction> wrapped = new HashMap<>();
    final int noiseSizeXZ;
    final int cellWidth;
    final int cellHeight;
    boolean interpolating;
    boolean fillingCell;
    private int cellStartBlockX;
    int cellStartBlockY;
    private int cellStartBlockZ;
    int inCellX;
    int inCellY;
    int inCellZ;
    long interpolationCounter;
    long arrayInterpolationCounter;
    int arrayIndex;

    public final NoiseRouter wrappedRouter;
    public final BlockStateFiller blockStateRule;
    public Aquifer aquifer;

    private final DensityFunction.ContextProvider sliceFillingContextProvider = new DensityFunction.ContextProvider() {
        @Override
        public DensityFunction.FunctionContext forIndex(int n) {
            NoiseChunk.this.cellStartBlockY = (n + NoiseChunk.this.cellNoiseMinY) * NoiseChunk.this.cellHeight;
            ++NoiseChunk.this.interpolationCounter;
            NoiseChunk.this.inCellY = 0;
            NoiseChunk.this.arrayIndex = n;
            return NoiseChunk.this;
        }

        @Override
        public void fillAllDirectly(double[] dArray, DensityFunction densityFunction) {
            for (int i = 0; i < NoiseChunk.this.cellCountY + 1; ++i) {
                NoiseChunk.this.cellStartBlockY = (i + NoiseChunk.this.cellNoiseMinY) * NoiseChunk.this.cellHeight;
                ++NoiseChunk.this.interpolationCounter;
                NoiseChunk.this.inCellY = 0;
                NoiseChunk.this.arrayIndex = i;
                dArray[i] = densityFunction.compute(NoiseChunk.this);
            }
        }
    };

    /**
     * 创建 NoiseChunk（含 Aquifer 支持）。
     * @param cellCountXZ  X/Z 方向每区块单元格数 (16/cellWidth)
     * @param router       原始 NoiseRouter
     * @param chunkStartX  区块起始方块 X
     * @param chunkStartZ  区块起始方块 Z
     * @param settings     NoiseSettings (cellWidth, cellHeight, minY, height)
     * @param waterId      水方块 ID
     * @param lavaId       熔岩方块 ID
     * @param seaLevel     海平面 Y
     * @param lavaLevel    熔岩层 Y
     * @param aquiferEnabled  是否启用完整 NoiseBasedAquifer
     * @param largeBiomes  是否大群系（传给 NoiseRouterData）
     * @param amplified    是否放大模式（传给 NoiseRouterData）
     */
    public NoiseChunk(int cellCountXZ, NoiseRouter router, int chunkStartX, int chunkStartZ,
                      NoiseSettings settings, boolean isNether, int waterId, int lavaId,
                      int seaLevel, int lavaLevel,
                      boolean aquiferEnabled, boolean largeBiomes, boolean amplified) {
        this.cellWidth = settings.getCellWidth();
        this.cellHeight = settings.getCellHeight();
        this.cellCountXZ = cellCountXZ;
        this.cellCountY = Mth.floorDiv(settings.height(), this.cellHeight);
        this.cellNoiseMinY = Mth.floorDiv(settings.minY(), this.cellHeight);
        this.firstCellX = Math.floorDiv(chunkStartX, this.cellWidth);
        this.firstCellZ = Math.floorDiv(chunkStartZ, this.cellWidth);
        this.interpolators = new ArrayList<>();
        this.cellCaches = new ArrayList<>();
        this.firstNoiseX = QuartPos.fromBlock(chunkStartX);
        this.firstNoiseZ = QuartPos.fromBlock(chunkStartZ);
        this.noiseSizeXZ = QuartPos.fromBlock(cellCountXZ * this.cellWidth);

        NoiseRouter mappedRouter = router.mapAll(this::wrap);
        this.wrappedRouter = mappedRouter;

        int minY = settings.minY();
        DensityFunction baseDensity = this.wrappedRouter.finalDensity();

        // Vanilla: cacheAllInCell(add(finalDensity, BeardifierMarker)).mapAll(wrap)
        // Beardifier is a no-op (no structures yet) so use constant(0)
        DensityFunction density = DensityFunctions.add(baseDensity, DensityFunctions.constant(0));
        density = DensityFunctions.cacheAllInCell(density);
        DensityFunction finalDensity = density.mapAll(this::wrap);

        // 创建 Aquifer
        Aquifer finalAquifer;
        if (aquiferEnabled) {
            Aquifer.FluidStatus lavaStatus = new Aquifer.FluidStatus(lavaLevel, lavaId);
            Aquifer.FluidStatus waterStatus = new Aquifer.FluidStatus(seaLevel, waterId);
            Aquifer.FluidPicker globalFluidPicker = (x, y, z) -> {
                if (y < Math.min(lavaLevel, seaLevel)) return lavaStatus;
                return waterStatus;
            };
            int chunkMaxX = chunkStartX + cellCountXZ * this.cellWidth - 1;
            int chunkMaxZ = chunkStartZ + cellCountXZ * this.cellWidth - 1;
            PositionalRandomFactory aquiferRandomFactory = DensityFunction.NoiseHolder.sharedFactory()
                    .fromHashOf("aquifer").forkPositional();
            finalAquifer = new NoiseBasedAquifer(this,
                    chunkStartX, chunkStartZ, chunkMaxX, chunkMaxZ,
                    this, aquiferRandomFactory,
                    minY, settings.height(), globalFluidPicker);
        } else {
            // 原版 NoiseBasedChunkGenerator.createFluidPicker 三段式：
            //   y < min(-54, seaLevel) -> 熔岩(-54)
            //   y < seaLevel          -> 维度默认流体（下界=熔岩，主世界=水，末地=空气）
            //   否则                  -> 空气
            // 下界 seaLevel=32 且默认流体=熔岩 → y<32 全部灌熔岩海；末地 seaLevel=0 → 无流体。
            int defaultFluid = isNether ? lavaId : waterId;
            int dimMinY = settings.minY();
            finalAquifer = Aquifer.createDisabled((x, y, z) -> {
                if (y < Math.min(-54, seaLevel)) return new Aquifer.FluidStatus(-54, lavaId);
                if (y < seaLevel) return new Aquifer.FluidStatus(seaLevel, defaultFluid);
                return new Aquifer.FluidStatus(dimMinY * 2, 0);
            });
        }
        this.aquifer = finalAquifer;

        // 构建主物质判定规则 — MaterialRuleList (Aquifer → OreVeinifier)
        NoiseChunk.BlockStateFiller aquiferFiller = (DensityFunction.FunctionContext ctx) -> {
            double d = finalDensity.compute(ctx);
            return finalAquifer.computeSubstance(ctx, d);
        };
        PositionalRandomFactory oreRandomFactory = DensityFunction.NoiseHolder.sharedFactory()
                .fromHashOf("ore").forkPositional();
        NoiseChunk.BlockStateFiller veinFiller = OreVeinifier.create(
                this.wrappedRouter.veinToggle(), this.wrappedRouter.veinRidged(),
                this.wrappedRouter.veinGap(), oreRandomFactory, isNether);
        // Bug46: 矿脉只允许替换固体(density>0)。曾放任 aquifer 压力分支返回 null 落穿到
        // 矿脉判定 -> 应为空气/流体的格位被矿脉/凝灰岩回填, 洞穴顶悬空矿脉。
        NoiseChunk.BlockStateFiller guardedVeinFiller = (DensityFunction.FunctionContext ctx) -> {
            if (finalDensity.compute(ctx) <= 0.0) return 0;
            return veinFiller.calculate(ctx);
        };
        NoiseChunk.BlockStateFiller[] materialRules = { aquiferFiller, guardedVeinFiller };
        this.blockStateRule = ctx -> {
            for (NoiseChunk.BlockStateFiller rule : materialRules) {
                Integer result = rule.calculate(ctx);
                if (result != null) return result;
            }
            return null;
        };
    }

    /** 获取经过 wrap 后的路由器（含 NoiseInterpolator 等插值层） */
    public NoiseRouter wrappedRouter() {
        return wrappedRouter;
    }

    /**
     * 返回当前插值位置的方块 ID。
     * 必须在 interpolation loop 内部调用（updateForY/X/Z 之后）。
     * 返回 0 = 空气，非零 = 方块 ID（包括水/熔岩）。
     * 返回 null = 需要生成器填入默认方块（如 STONE）。
     * 原版对应: net.minecraft.world.level.levelgen.NoiseChunk#getInterpolatedState()
     */
    public Integer getInterpolatedState() {
        return this.blockStateRule.calculate(this);
    }

    // ==================== FunctionContext 实现 ====================

    @Override
    public int blockX() {
        return this.cellStartBlockX + this.inCellX;
    }

    @Override
    public int blockY() {
        return this.cellStartBlockY + this.inCellY;
    }

    @Override
    public int blockZ() {
        return this.cellStartBlockZ + this.inCellZ;
    }

    // ==================== 插值循环 API ====================

    private void fillSlice(boolean firstSlice, int cellX) {
        this.cellStartBlockX = cellX * this.cellWidth;
        this.inCellX = 0;
        for (int i = 0; i < this.cellCountXZ + 1; ++i) {
            int n2 = this.firstCellZ + i;
            this.cellStartBlockZ = n2 * this.cellWidth;
            this.inCellZ = 0;
            ++this.arrayInterpolationCounter;
            for (NoiseInterpolator interpolator : this.interpolators) {
                double[] slice = (firstSlice ? interpolator.slice0 : interpolator.slice1)[i];
                interpolator.fillArray(slice, this.sliceFillingContextProvider);
            }
        }
        ++this.arrayInterpolationCounter;
    }

    public void initializeForFirstCellX() {
        if (this.interpolating) {
            throw new IllegalStateException("Starting interpolation twice");
        }
        this.interpolating = true;
        this.interpolationCounter = 0L;
        this.fillSlice(true, this.firstCellX);
    }

    public void advanceCellX(int n) {
        this.fillSlice(false, this.firstCellX + n + 1);
        this.cellStartBlockX = (this.firstCellX + n) * this.cellWidth;
        this.inCellX = 0;
    }

    public void selectCellYZ(int cellY, int cellZ) {
        for (NoiseInterpolator interpolator : this.interpolators) {
            interpolator.selectCellYZ(cellY, cellZ);
        }
        this.fillingCell = true;
        this.cellStartBlockY = (cellY + this.cellNoiseMinY) * this.cellHeight;
        this.cellStartBlockZ = (this.firstCellZ + cellZ) * this.cellWidth;
        ++this.arrayInterpolationCounter;
        for (CacheAllInCell cache : this.cellCaches) {
            cache.noiseFiller.fillArray(cache.values, this);
        }
        ++this.arrayInterpolationCounter;
        this.fillingCell = false;
    }

    public void updateForY(int n, double d) {
        this.inCellY = n - this.cellStartBlockY;
        for (NoiseInterpolator interpolator : this.interpolators) {
            interpolator.updateForY(d);
        }
    }

    public void updateForX(int n, double d) {
        this.inCellX = n - this.cellStartBlockX;
        for (NoiseInterpolator interpolator : this.interpolators) {
            interpolator.updateForX(d);
        }
    }

    public void updateForZ(int n, double d) {
        this.inCellZ = n - this.cellStartBlockZ;
        ++this.interpolationCounter;
        for (NoiseInterpolator interpolator : this.interpolators) {
            interpolator.updateForZ(d);
        }
    }

    public void stopInterpolation() {
        if (!this.interpolating) {
            throw new IllegalStateException("Stopping interpolation when not started");
        }
        this.interpolating = false;
    }

    public void swapSlices() {
        this.interpolators.forEach(NoiseInterpolator::swapSlices);
    }

    @Override
    public DensityFunction.FunctionContext forIndex(int n) {
        int n2 = Math.floorMod(n, this.cellWidth);
        int n3 = Math.floorDiv(n, this.cellWidth);
        int n4 = Math.floorMod(n3, this.cellWidth);
        int n5 = this.cellHeight - 1 - Math.floorDiv(n3, this.cellWidth);
        this.inCellX = n4;
        this.inCellY = n5;
        this.inCellZ = n2;
        this.arrayIndex = n;
        return this;
    }

    @Override
    public void fillAllDirectly(double[] dArray, DensityFunction densityFunction) {
        this.arrayIndex = 0;
        for (int i = this.cellHeight - 1; i >= 0; --i) {
            this.inCellY = i;
            for (int j = 0; j < this.cellWidth; ++j) {
                this.inCellX = j;
                for (int k = 0; k < this.cellWidth; ++k) {
                    this.inCellZ = k;
                    dArray[this.arrayIndex++] = densityFunction.compute(this);
                }
            }
        }
    }

    public int maxPreliminarySurfaceLevel(int n, int n2, int n3, int n4) {
        int n5 = Integer.MIN_VALUE;
        for (int i = n2; i <= n4; i += 4) {
            for (int j = n; j <= n3; j += 4) {
                int n6 = this.preliminarySurfaceLevel(j, i);
                if (n6 <= n5) continue;
                n5 = n6;
            }
        }
        return n5;
    }

    public int preliminarySurfaceLevel(int n, int n2) {
        int n3 = QuartPos.toBlock(QuartPos.fromBlock(n));
        int n4 = QuartPos.toBlock(QuartPos.fromBlock(n2));
        long key = ColumnPos.asLong(n3, n4);
        return preliminarySurfaceLevelCache.computeIfAbsent(key, k -> {
            DensityFunction.SinglePointContext ctx = new DensityFunction.SinglePointContext(
                ColumnPos.getX(k), 0, ColumnPos.getZ(k));
            return Mth.floor(this.wrappedRouter.preliminarySurfaceLevel().compute(ctx));
        });
    }

    private final HashMap<Long, Integer> preliminarySurfaceLevelCache = new HashMap<>();

    // ==================== wrap — 替换 Marker 节点 ====================

    protected DensityFunction wrap(DensityFunction densityFunction) {
        return this.wrapped.computeIfAbsent(densityFunction, this::wrapNew);
    }

    private DensityFunction wrapNew(DensityFunction densityFunction) {
        if (densityFunction instanceof Marker marker) {
            return switch (marker.type()) {
                case INTERPOLATED -> new NoiseInterpolator(marker.wrapped());
                case FLAT_CACHE -> new FlatCache(marker.wrapped(), true);
                case CACHE2D -> new Cache2D(marker.wrapped());
                case CACHE_ONCE -> new CacheOnce(marker.wrapped());
                case CACHE_ALL_IN_CELL -> new CacheAllInCell(marker.wrapped());
            };
        }
        return densityFunction;
    }

    // ==================== 内部类 ====================

    @FunctionalInterface
    public interface BlockStateFiller {
        Integer calculate(DensityFunction.FunctionContext ctx);
    }

    class NoiseInterpolator implements DensityFunction {
        double[][] slice0;
        double[][] slice1;
        private final DensityFunction noiseFiller;
        private double noise000, noise001, noise100, noise101;
        private double noise010, noise011, noise110, noise111;
        private double valueXZ00, valueXZ10, valueXZ01, valueXZ11;
        private double valueZ0, valueZ1;
        private double value;

        NoiseInterpolator(DensityFunction densityFunction) {
            this.noiseFiller = densityFunction;
            this.slice0 = allocateSlice(NoiseChunk.this.cellCountY, NoiseChunk.this.cellCountXZ);
            this.slice1 = allocateSlice(NoiseChunk.this.cellCountY, NoiseChunk.this.cellCountXZ);
            NoiseChunk.this.interpolators.add(this);
        }

        private double[][] allocateSlice(int cellCountY, int cellCountXZ) {
            int n3 = cellCountXZ + 1;
            int n4 = cellCountY + 1;
            double[][] dArray = new double[n3][n4];
            for (int i = 0; i < n3; ++i) {
                dArray[i] = new double[n4];
            }
            return dArray;
        }

        void selectCellYZ(int n, int n2) {
            this.noise000 = this.slice0[n2][n];
            this.noise001 = this.slice0[n2 + 1][n];
            this.noise100 = this.slice1[n2][n];
            this.noise101 = this.slice1[n2 + 1][n];
            this.noise010 = this.slice0[n2][n + 1];
            this.noise011 = this.slice0[n2 + 1][n + 1];
            this.noise110 = this.slice1[n2][n + 1];
            this.noise111 = this.slice1[n2 + 1][n + 1];
        }

        void updateForY(double d) {
            this.valueXZ00 = Mth.lerp(d, this.noise000, this.noise010);
            this.valueXZ10 = Mth.lerp(d, this.noise100, this.noise110);
            this.valueXZ01 = Mth.lerp(d, this.noise001, this.noise011);
            this.valueXZ11 = Mth.lerp(d, this.noise101, this.noise111);
        }

        void updateForX(double d) {
            this.valueZ0 = Mth.lerp(d, this.valueXZ00, this.valueXZ10);
            this.valueZ1 = Mth.lerp(d, this.valueXZ01, this.valueXZ11);
        }

        void updateForZ(double d) {
            this.value = Mth.lerp(d, this.valueZ0, this.valueZ1);
        }

        @Override
        public double compute(DensityFunction.FunctionContext functionContext) {
            if (functionContext != NoiseChunk.this) {
                return this.noiseFiller.compute(functionContext);
            }
            if (!NoiseChunk.this.interpolating) {
                throw new IllegalStateException("Trying to sample interpolator outside interpolation loop");
            }
            if (NoiseChunk.this.fillingCell) {
                double deltaX = (double) NoiseChunk.this.inCellX / (double) NoiseChunk.this.cellWidth;
                double deltaY = (double) NoiseChunk.this.inCellY / (double) NoiseChunk.this.cellHeight;
                double deltaZ = (double) NoiseChunk.this.inCellZ / (double) NoiseChunk.this.cellWidth;
                return Mth.lerp3(deltaX, deltaY, deltaZ,
                        this.noise000, this.noise100, this.noise010, this.noise110,
                        this.noise001, this.noise101, this.noise011, this.noise111);
            }
            return this.value;
        }

        @Override
        public void fillArray(double[] dArray, DensityFunction.ContextProvider contextProvider) {
            if (NoiseChunk.this.fillingCell) {
                contextProvider.fillAllDirectly(dArray, this);
                return;
            }
            this.wrapped().fillArray(dArray, contextProvider);
        }

        DensityFunction wrapped() {
            return this.noiseFiller;
        }

        private void swapSlices() {
            double[][] tmp = this.slice0;
            this.slice0 = this.slice1;
            this.slice1 = tmp;
        }

        @Override
        public double minValue() { return noiseFiller.minValue(); }
        @Override
        public double maxValue() { return noiseFiller.maxValue(); }
        @Override
        public DensityFunction mapAll(Visitor visitor) { return this; }
    }

    class CacheAllInCell implements DensityFunction {
        final DensityFunction noiseFiller;
        final double[] values;

        CacheAllInCell(DensityFunction densityFunction) {
            this.noiseFiller = densityFunction;
            this.values = new double[NoiseChunk.this.cellWidth * NoiseChunk.this.cellWidth * NoiseChunk.this.cellHeight];
            NoiseChunk.this.cellCaches.add(this);
        }

        @Override
        public double compute(DensityFunction.FunctionContext functionContext) {
            if (functionContext != NoiseChunk.this) {
                return this.noiseFiller.compute(functionContext);
            }
            if (!NoiseChunk.this.interpolating) {
                throw new IllegalStateException("Trying to sample cache outside interpolation loop");
            }
            int n = NoiseChunk.this.inCellX;
            int n2 = NoiseChunk.this.inCellY;
            int n3 = NoiseChunk.this.inCellZ;
            if (n >= 0 && n2 >= 0 && n3 >= 0 && n < NoiseChunk.this.cellWidth
                    && n2 < NoiseChunk.this.cellHeight && n3 < NoiseChunk.this.cellWidth) {
                int idx = ((NoiseChunk.this.cellHeight - 1 - n2) * NoiseChunk.this.cellWidth + n)
                        * NoiseChunk.this.cellWidth + n3;
                return this.values[idx];
            }
            return this.noiseFiller.compute(functionContext);
        }

        @Override
        public void fillArray(double[] dArray, DensityFunction.ContextProvider contextProvider) {
            contextProvider.fillAllDirectly(dArray, this);
        }

        @Override
        public double minValue() { return noiseFiller.minValue(); }
        @Override
        public double maxValue() { return noiseFiller.maxValue(); }
        @Override
        public DensityFunction mapAll(Visitor visitor) { return this; }
    }

    class FlatCache implements DensityFunction {
        private final DensityFunction noiseFiller;
        final double[] values;
        final int sizeXZ;

        FlatCache(DensityFunction densityFunction, boolean fill) {
            this.noiseFiller = densityFunction;
            this.sizeXZ = NoiseChunk.this.noiseSizeXZ + 1;
            this.values = new double[this.sizeXZ * this.sizeXZ];
            if (fill) {
                for (int i = 0; i <= NoiseChunk.this.noiseSizeXZ; ++i) {
                    int n = NoiseChunk.this.firstNoiseX + i;
                    int n2 = QuartPos.toBlock(n);
                    for (int j = 0; j <= NoiseChunk.this.noiseSizeXZ; ++j) {
                        int n3 = NoiseChunk.this.firstNoiseZ + j;
                        int n4 = QuartPos.toBlock(n3);
                        this.values[i + j * this.sizeXZ] = densityFunction.compute(
                                new DensityFunction.SinglePointContext(n2, 0, n4));
                    }
                }
            }
        }

        @Override
        public double compute(DensityFunction.FunctionContext functionContext) {
            int n = QuartPos.fromBlock(functionContext.blockX());
            int n2 = QuartPos.fromBlock(functionContext.blockZ());
            int n3 = n - NoiseChunk.this.firstNoiseX;
            int n4 = n2 - NoiseChunk.this.firstNoiseZ;
            if (n3 >= 0 && n4 >= 0 && n3 < this.sizeXZ && n4 < this.sizeXZ) {
                return this.values[n3 + n4 * this.sizeXZ];
            }
            return this.noiseFiller.compute(functionContext);
        }

        @Override
        public void fillArray(double[] dArray, DensityFunction.ContextProvider contextProvider) {
            contextProvider.fillAllDirectly(dArray, this);
        }

        @Override
        public double minValue() { return noiseFiller.minValue(); }
        @Override
        public double maxValue() { return noiseFiller.maxValue(); }
        @Override
        public DensityFunction mapAll(Visitor visitor) { return this; }
    }

    static class Cache2D implements DensityFunction {
        private final DensityFunction function;
        private long lastPos2D = ChunkPos.INVALID_CHUNK_POS;
        private double lastValue;

        Cache2D(DensityFunction densityFunction) {
            this.function = densityFunction;
        }

        @Override
        public double compute(DensityFunction.FunctionContext functionContext) {
            int n = functionContext.blockX();
            int n2 = functionContext.blockZ();
            long l = ChunkPos.asLong(n, n2);
            if (this.lastPos2D == l) {
                return this.lastValue;
            }
            this.lastPos2D = l;
            this.lastValue = this.function.compute(functionContext);
            return this.lastValue;
        }

        @Override
        public void fillArray(double[] dArray, DensityFunction.ContextProvider contextProvider) {
            this.function.fillArray(dArray, contextProvider);
        }

        @Override
        public double minValue() { return function.minValue(); }
        @Override
        public double maxValue() { return function.maxValue(); }
        @Override
        public DensityFunction mapAll(Visitor visitor) { return this; }
    }

    class CacheOnce implements DensityFunction {
        private final DensityFunction function;
        private long lastCounter;
        private long lastArrayCounter;
        private double lastValue;
        private double[] lastArray;

        CacheOnce(DensityFunction densityFunction) {
            this.function = densityFunction;
        }

        @Override
        public double compute(DensityFunction.FunctionContext functionContext) {
            if (functionContext != NoiseChunk.this) {
                return this.function.compute(functionContext);
            }
            if (this.lastArray != null && this.lastArrayCounter == NoiseChunk.this.arrayInterpolationCounter) {
                return this.lastArray[NoiseChunk.this.arrayIndex];
            }
            if (this.lastCounter == NoiseChunk.this.interpolationCounter) {
                return this.lastValue;
            }
            this.lastCounter = NoiseChunk.this.interpolationCounter;
            this.lastValue = this.function.compute(functionContext);
            return this.lastValue;
        }

        @Override
        public void fillArray(double[] dArray, DensityFunction.ContextProvider contextProvider) {
            if (this.lastArray != null && this.lastArrayCounter == NoiseChunk.this.arrayInterpolationCounter) {
                System.arraycopy(this.lastArray, 0, dArray, 0, dArray.length);
                return;
            }
            this.wrapped().fillArray(dArray, contextProvider);
            if (this.lastArray != null && this.lastArray.length == dArray.length) {
                System.arraycopy(dArray, 0, this.lastArray, 0, dArray.length);
            } else {
                this.lastArray = dArray.clone();
            }
            this.lastArrayCounter = NoiseChunk.this.arrayInterpolationCounter;
        }

        DensityFunction wrapped() {
            return this.function;
        }

        @Override
        public double minValue() { return function.minValue(); }
        @Override
        public double maxValue() { return function.maxValue(); }
        @Override
        public DensityFunction mapAll(Visitor visitor) { return this; }
    }
}
