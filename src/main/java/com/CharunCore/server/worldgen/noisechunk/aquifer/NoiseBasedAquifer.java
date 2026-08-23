package com.CharunCore.server.worldgen.noisechunk.aquifer;

import java.util.Arrays;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.Mth;
import com.CharunCore.server.world.gen.PositionalRandomFactory;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.noisechunk.NoiseChunk;

/**
 * Port of net.minecraft.world.level.levelgen.Aquifer.NoiseBasedAquifer (CFR 0.152).
 * Handles fluid barrier noise, fluid surface level, pressure calcs.
 */
public class NoiseBasedAquifer implements Aquifer {
    private static final int X_RANGE = 10;
    private static final int Y_RANGE = 9;
    private static final int Z_RANGE = 10;
    private static final int X_SEPARATION = 6;
    private static final int Y_SEPARATION = 3;
    private static final int Z_SEPARATION = 6;
    private static final int X_SPACING = 16;
    private static final int Y_SPACING = 12;
    private static final int Z_SPACING = 16;
    private static final int X_SPACING_SHIFT = 4;
    private static final int Z_SPACING_SHIFT = 4;
    private static final int MAX_REASONABLE_DISTANCE_TO_AQUIFER_CENTER = 11;
    private static final double FLOWING_UPDATE_SIMULARITY = similarity(Mth.square(10), Mth.square(12));
    private static final int SAMPLE_OFFSET_X = -5;
    private static final int SAMPLE_OFFSET_Y = 1;
    private static final int SAMPLE_OFFSET_Z = -5;
    private static final int MIN_CELL_SAMPLE_X = 0;
    private static final int MIN_CELL_SAMPLE_Y = -1;
    private static final int MIN_CELL_SAMPLE_Z = 0;
    private static final int MAX_CELL_SAMPLE_X = 1;
    private static final int MAX_CELL_SAMPLE_Y = 1;
    private static final int MAX_CELL_SAMPLE_Z = 1;
    private static final int WAY_BELOW_MIN_Y = -1024;
    private static final int LAVA_ID = BlockStateHelper.getDefault("lava");
    private static final int WATER_ID = BlockStateHelper.getDefault("water");
    private static final int[][] SURFACE_SAMPLING_OFFSETS_IN_CHUNKS = new int[][]{
        {0, 0}, {-2, -1}, {-1, -1}, {0, -1}, {1, -1},
        {-3, 0}, {-2, 0}, {-1, 0}, {1, 0},
        {-2, 1}, {-1, 1}, {0, 1}, {1, 1}
    };

    private final NoiseChunk noiseChunk;
    private final DensityFunction barrierNoise;
    private final DensityFunction fluidLevelFloodednessNoise;
    private final DensityFunction fluidLevelSpreadNoise;
    private final DensityFunction lavaNoise;
    private final PositionalRandomFactory positionalRandomFactory;
    private final FluidStatus[] aquiferCache;
    private final long[] aquiferLocationCache;
    private final FluidPicker globalFluidPicker;
    private final DensityFunction erosion;
    private final DensityFunction depth;
    private boolean shouldScheduleFluidUpdate;
    private final int skipSamplingAboveY;
    private final int minGridX;
    private final int minGridY;
    private final int minGridZ;
    private final int gridSizeX;
    private final int gridSizeZ;

    public NoiseBasedAquifer(NoiseChunk noiseChunk, int chunkMinX, int chunkMinZ, int chunkMaxX, int chunkMaxZ,
                      NoiseChunk wrappedRouter_, PositionalRandomFactory positionalRandomFactory,
                      int minY, int height, FluidPicker fluidPicker) {
        this.noiseChunk = noiseChunk;
        this.barrierNoise = wrappedRouter_.wrappedRouter().barrierNoise();
        this.fluidLevelFloodednessNoise = wrappedRouter_.wrappedRouter().fluidLevelFloodednessNoise();
        this.fluidLevelSpreadNoise = wrappedRouter_.wrappedRouter().fluidLevelSpreadNoise();
        this.lavaNoise = wrappedRouter_.wrappedRouter().lavaNoise();
        this.erosion = wrappedRouter_.wrappedRouter().erosion();
        this.depth = wrappedRouter_.wrappedRouter().depth();
        this.positionalRandomFactory = positionalRandomFactory;
        this.minGridX = gridX(chunkMinX + SAMPLE_OFFSET_X) + MIN_CELL_SAMPLE_X;
        this.globalFluidPicker = fluidPicker;
        int n3 = gridX(chunkMaxX + SAMPLE_OFFSET_X) + MAX_CELL_SAMPLE_X;
        this.gridSizeX = n3 - this.minGridX + 1;
        this.minGridY = gridY(minY + SAMPLE_OFFSET_Y) + MIN_CELL_SAMPLE_Y;
        int n4 = gridY(minY + height + SAMPLE_OFFSET_Y) + MAX_CELL_SAMPLE_Y;
        int n5 = n4 - this.minGridY + 1;
        this.minGridZ = gridZ(chunkMinZ + SAMPLE_OFFSET_Z) + MIN_CELL_SAMPLE_Z;
        int n6 = gridZ(chunkMaxZ + SAMPLE_OFFSET_Z) + MAX_CELL_SAMPLE_Z;
        this.gridSizeZ = n6 - this.minGridZ + 1;
        int n7 = this.gridSizeX * n5 * this.gridSizeZ;
        this.aquiferCache = new FluidStatus[n7];
        this.aquiferLocationCache = new long[n7];
        Arrays.fill(this.aquiferLocationCache, Long.MAX_VALUE);
        int n8 = this.adjustSurfaceLevel(noiseChunk.maxPreliminarySurfaceLevel(
            fromGridX(this.minGridX, 0),
            fromGridZ(this.minGridZ, 0),
            fromGridX(n3, X_RANGE - 1),
            fromGridZ(n6, Z_RANGE - 1)));
        int n9 = gridY(n8 + 12) - MIN_CELL_SAMPLE_Y;
        this.skipSamplingAboveY = fromGridY(n9, 11) - 1;
    }

    private int getIndex(int n, int n2, int n3) {
        int n4 = n - this.minGridX;
        int n5 = n2 - this.minGridY;
        int n6 = n3 - this.minGridZ;
        return (n5 * this.gridSizeZ + n6) * this.gridSizeX + n4;
    }

    @Override
    public java.lang.Integer computeSubstance(DensityFunction.FunctionContext functionContext, double d) {
        if (d > 0.0) {
            this.shouldScheduleFluidUpdate = false;
            return null;
        }
        int n = functionContext.blockX();
        int n2 = functionContext.blockY();
        int n3 = functionContext.blockZ();
        FluidStatus fluidStatus = this.globalFluidPicker.computeFluid(n, n2, n3);
        if (n2 > this.skipSamplingAboveY) {
            this.shouldScheduleFluidUpdate = false;
            return fluidStatus.at(n2);
        }
        Integer fluidAt = fluidStatus.at(n2);
        if (fluidAt != null && fluidAt != 0 && isLava(fluidAt)) {
            this.shouldScheduleFluidUpdate = false;
            return fluidAt;
        }
        int n4 = gridX(n + SAMPLE_OFFSET_X);
        int n5 = gridY(n2 + SAMPLE_OFFSET_Y);
        int n6 = gridZ(n3 + SAMPLE_OFFSET_Z);
        int n7 = Integer.MAX_VALUE;
        int n8 = Integer.MAX_VALUE;
        int n9 = Integer.MAX_VALUE;
        int n10 = Integer.MAX_VALUE;
        int n11 = 0;
        int n12 = 0;
        int n13 = 0;
        int n14 = 0;
        for (int i = MIN_CELL_SAMPLE_X; i <= MAX_CELL_SAMPLE_X; ++i) {
            for (int j = MIN_CELL_SAMPLE_Y; j <= MAX_CELL_SAMPLE_Y; ++j) {
                for (int k = MIN_CELL_SAMPLE_Z; k <= MAX_CELL_SAMPLE_Z; ++k) {
                    long l;
                    int n15 = n4 + i;
                    int n16 = n5 + j;
                    int n17 = n6 + k;
                    int n18 = this.getIndex(n15, n16, n17);
                    long l2 = this.aquiferLocationCache[n18];
                    if (l2 != Long.MAX_VALUE) {
                        l = l2;
                    } else {
                        RandomSource randomSource = this.positionalRandomFactory.at(n15, n16, n17);
                        l = packPos(fromGridX(n15, randomSource.nextInt(X_RANGE)),
                                   fromGridY(n16, randomSource.nextInt(Y_RANGE)),
                                   fromGridZ(n17, randomSource.nextInt(Z_RANGE)));
                        this.aquiferLocationCache[n18] = l;
                    }
                    int n19 = unpackX(l) - n;
                    int n20 = unpackY(l) - n2;
                    int n21 = unpackZ(l) - n3;
                    int n22 = n19 * n19 + n20 * n20 + n21 * n21;
                    if (n7 >= n22) {
                        n14 = n13;
                        n13 = n12;
                        n12 = n11;
                        n11 = n18;
                        n10 = n9;
                        n9 = n8;
                        n8 = n7;
                        n7 = n22;
                        continue;
                    }
                    if (n8 >= n22) {
                        n14 = n13;
                        n13 = n12;
                        n12 = n18;
                        n10 = n9;
                        n9 = n8;
                        n8 = n22;
                        continue;
                    }
                    if (n9 >= n22) {
                        n14 = n13;
                        n13 = n18;
                        n10 = n9;
                        n9 = n22;
                        continue;
                    }
                    if (n10 < n22) continue;
                    n14 = n18;
                    n10 = n22;
                }
            }
        }
        FluidStatus fluidStatus2 = this.getAquiferStatus(n11);
        double d4 = similarity(n7, n8);
        int blockState2 = fluidStatus2.at(n2);
        int blockState = blockState2 != 0 ? blockState2 : 0;
        if (d4 <= 0.0) {
            FluidStatus fluidStatus3;
            this.shouldScheduleFluidUpdate = d4 >= FLOWING_UPDATE_SIMULARITY ? !fluidStatus2.equals(fluidStatus3 = this.getAquiferStatus(n12)) : false;
            return blockState;
        }
        if (blockState2 != 0 && isWater(blockState2) && this.globalFluidPicker.computeFluid(n, n2 - 1, n3).at(n2 - 1) != null && isLava(this.globalFluidPicker.computeFluid(n, n2 - 1, n3).at(n2 - 1))) {
            this.shouldScheduleFluidUpdate = true;
            return blockState;
        }
        MutableDouble mutableDouble = new MutableDouble();
        FluidStatus fluidStatus4 = this.getAquiferStatus(n12);
        double d5 = d4 * this.calculatePressure(functionContext, mutableDouble, fluidStatus2, fluidStatus4);
        if (d + d5 > 0.0) {
            this.shouldScheduleFluidUpdate = false;
            return null;
        }
        FluidStatus fluidStatus5 = this.getAquiferStatus(n13);
        double d6 = similarity(n7, n9);
        if (d6 > 0.0) {
            double d3 = d4 * d6 * this.calculatePressure(functionContext, mutableDouble, fluidStatus2, fluidStatus5);
            if (d + d3 > 0.0) {
                this.shouldScheduleFluidUpdate = false;
                return null;
            }
        }
        double d7 = similarity(n8, n9);
        if (d7 > 0.0) {
            double d2 = d4 * d7 * this.calculatePressure(functionContext, mutableDouble, fluidStatus4, fluidStatus5);
            if (d + d2 > 0.0) {
                this.shouldScheduleFluidUpdate = false;
                return null;
            }
        }
        boolean bl2 = !fluidStatus2.equals(fluidStatus4);
        boolean bl3 = d7 >= FLOWING_UPDATE_SIMULARITY && !fluidStatus4.equals(fluidStatus5);
        boolean bl = d6 >= FLOWING_UPDATE_SIMULARITY && !fluidStatus2.equals(fluidStatus5);
        this.shouldScheduleFluidUpdate = bl2 || bl3 || bl
            ? true
            : d6 >= FLOWING_UPDATE_SIMULARITY
              && similarity(n7, n10) >= FLOWING_UPDATE_SIMULARITY
              && !fluidStatus2.equals(this.getAquiferStatus(n14));
        return blockState;
    }

    @Override
    public boolean shouldScheduleFluidUpdate() {
        return this.shouldScheduleFluidUpdate;
    }

    private static double similarity(int n, int n2) {
        double d = 25.0;
        return 1.0 - (double)(n2 - n) / 25.0;
    }

    private double calculatePressure(DensityFunction.FunctionContext functionContext, MutableDouble mutableDouble, FluidStatus fluidStatus, FluidStatus fluidStatus2) {
        double d;
        double d2;
        int n = functionContext.blockY();
        int blockState = fluidStatus.at(n);
        int blockState2 = fluidStatus2.at(n);
        if ((isLava(blockState) && isWater(blockState2)) || (isWater(blockState) && isLava(blockState2))) {
            return 2.0;
        }
        int n2 = Math.abs(fluidStatus.fluidLevel() - fluidStatus2.fluidLevel());
        if (n2 == 0) {
            return 0.0;
        }
        double d3 = 0.5 * (double)(fluidStatus.fluidLevel() + fluidStatus2.fluidLevel());
        double d4 = (double)n + 0.5 - d3;
        double d5 = (double)n2 / 2.0;
        double d6 = 0.0;
        double d7 = 2.5;
        double d8 = 1.5;
        double d9 = 3.0;
        double d10 = 10.0;
        double d11 = 3.0;
        double d12 = d5 - Math.abs(d4);
        double d13 = d4 > 0.0
            ? ((d2 = d6 + d12) > 0.0 ? d2 / d8 : d2 / d7)
            : ((d2 = d9 + d12) > 0.0 ? d2 / d11 : d2 / d10);
        if (d13 < -2.0 || d13 > 2.0) {
            d = 0.0;
        } else {
            double d14 = mutableDouble.value;
            if (Double.isNaN(d14)) {
                double d15 = this.barrierNoise.compute(functionContext);
                mutableDouble.value = d15;
                d = d15;
            } else {
                d = d14;
            }
        }
        return 2.0 * (d + d13);
    }

    private static int gridX(int n) {
        return n >> X_SPACING_SHIFT;
    }

    private static int fromGridX(int n, int n2) {
        return (n << X_SPACING_SHIFT) + n2;
    }

    private static int gridY(int n) {
        return Math.floorDiv(n, Y_SPACING);
    }

    private static int fromGridY(int n, int n2) {
        return n * Y_SPACING + n2;
    }

    private static int gridZ(int n) {
        return n >> Z_SPACING_SHIFT;
    }

    private static int fromGridZ(int n, int n2) {
        return (n << Z_SPACING_SHIFT) + n2;
    }

    private FluidStatus getAquiferStatus(int n) {
        FluidStatus fluidStatus2 = this.aquiferCache[n];
        if (fluidStatus2 != null) {
            return fluidStatus2;
        }
        long l = this.aquiferLocationCache[n];
        int cx = unpackX(l);
        int cy = unpackY(l);
        int cz = unpackZ(l);
        this.aquiferCache[n] = fluidStatus2 = this.computeFluid(cx, cy, cz);
        return fluidStatus2;
    }

    private FluidStatus computeFluid(int n, int n2, int n3) {
        FluidStatus fluidStatus = this.globalFluidPicker.computeFluid(n, n2, n3);
        int n4 = Integer.MAX_VALUE;
        int n5 = n2 + 12;
        int n6 = n2 - 12;
        boolean bl = false;
        for (int[] nArray : SURFACE_SAMPLING_OFFSETS_IN_CHUNKS) {
            int n7 = n + sectionToBlockCoord(nArray[0]);
            int n8 = n3 + sectionToBlockCoord(nArray[1]);
            int n9 = this.noiseChunk.preliminarySurfaceLevel(n7, n8);
            int n10 = this.adjustSurfaceLevel(n9);
            boolean bl3 = nArray[0] == 0 && nArray[1] == 0;
            if (bl3 && n6 > n10) {
                return fluidStatus;
            }
            boolean bl2 = n5 > n10;
            FluidStatus fluidStatus2 = this.globalFluidPicker.computeFluid(n7, n10, n8);
            Integer at = fluidStatus2.at(n10);
            if ((bl2 || bl3) && (at != null && at != 0)) {
                if (bl3) {
                    bl = true;
                }
                if (bl2) {
                    return fluidStatus2;
                }
            }
            n4 = Math.min(n4, n9);
        }
        int n11 = this.computeSurfaceLevel(n, n2, n3, fluidStatus, n4, bl);
        return new FluidStatus(n11, this.computeFluidType(n, n2, n3, fluidStatus, n11));
    }

    private int adjustSurfaceLevel(int n) {
        return n + 8;
    }

    private int computeSurfaceLevel(int n, int n2, int n3, FluidStatus fluidStatus, int n4, boolean bl) {
        int n5;
        double d;
        double d2;
        DensityFunction.SinglePointContext singlePointContext = new DensityFunction.SinglePointContext(n, n2, n3);
        if (isDeepDarkRegion(this.erosion, this.depth, singlePointContext)) {
            d2 = -1.0;
            d = -1.0;
        } else {
            n5 = n4 + 8 - n2;
            int n6 = 64;
            double d3 = bl ? Mth.clampedMap((double)n5, 0.0, 64.0, 1.0, 0.0) : 0.0;
            double d4 = Mth.clamp(this.fluidLevelFloodednessNoise.compute(singlePointContext), -1.0, 1.0);
            double d5 = Mth.map(d3, 1.0, 0.0, -0.3, 0.8);
            double d6 = Mth.map(d3, 1.0, 0.0, -0.8, 0.4);
            d2 = d4 - d6;
            d = d4 - d5;
        }
        n5 = d > 0.0
            ? fluidStatus.fluidLevel()
            : (d2 > 0.0
                ? this.computeRandomizedFluidSurfaceLevel(n, n2, n3, n4)
                : WAY_BELOW_MIN_Y);
        return n5;
    }

    private int computeRandomizedFluidSurfaceLevel(int n, int n2, int n3, int n4) {
        int n5 = 16;
        int n6 = 40;
        int n7 = Math.floorDiv(n, 16);
        int n8 = Math.floorDiv(n2, 40);
        int n9 = Math.floorDiv(n3, 16);
        int n10 = n8 * 40 + 20;
        int n11 = 10;
        double d = this.fluidLevelSpreadNoise.compute(new DensityFunction.SinglePointContext(n7, n8, n9)) * 10.0;
        int n12 = Mth.quantize(d, 3);
        int n13 = n10 + n12;
        return Math.min(n4, n13);
    }

    private int computeFluidType(int n, int n2, int n3, FluidStatus fluidStatus, int n4) {
        int blockState = fluidStatus.fluidType();
        if (n4 <= -10 && n4 != WAY_BELOW_MIN_Y && !isLava(fluidStatus.fluidType())) {
            int n5;
            int n6;
            int n7 = 64;
            int n8 = 40;
            int n9 = Math.floorDiv(n, 64);
            double d = this.lavaNoise.compute(new DensityFunction.SinglePointContext(n9, n6 = Math.floorDiv(n2, 40), n5 = Math.floorDiv(n3, 64)));
            if (Math.abs(d) > 0.3) {
                blockState = LAVA_ID;
            }
        }
        return blockState;
    }

    // ====== utility methods ======

    private static int sectionToBlockCoord(int n) {
        return n * 16;
    }

    private static long packPos(int x, int y, int z) {
        long l = 0L;
        l |= ((long)x & 0x3FFFFFFL) << 38;
        l |= ((long)z & 0x3FFFFFFL) << 12;
        l |= (long)y & 0xFFFL;
        return l;
    }

    private static int unpackX(long packed) {
        return (int)(packed >> 38);
    }

    private static int unpackY(long packed) {
        return (int)(packed << 52 >> 52);
    }

    private static int unpackZ(long packed) {
        return (int)(packed << 26 >> 38);
    }

    private static boolean isLava(int blockId) {
        // LAVA_ID/WATER_ID come from BlockStateHelper.getDefault, so fluid identification
        // no longer drifts if the block-state table order changes.
        return blockId == LAVA_ID;
    }

    private static boolean isWater(int blockId) {
        return blockId == WATER_ID;
    }

    private static boolean isDeepDarkRegion(DensityFunction erosion, DensityFunction depth, DensityFunction.FunctionContext ctx) {
        return erosion.compute(ctx) < -0.22499999403953552d
            && depth.compute(ctx) > 0.8999999761581421d;
    }

    private static class MutableDouble {
        double value = Double.NaN;
    }
}
