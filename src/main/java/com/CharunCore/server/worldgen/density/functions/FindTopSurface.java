package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.world.gen.Mth;
import com.CharunCore.server.worldgen.density.DensityFunction;

/**
 * 原版 DensityFunctions.findTopSurface — 扫描密度场找地表 Y
 */
public final class FindTopSurface implements DensityFunction {
    private final DensityFunction density;
    private final DensityFunction upperBound;
    private final int lowerBound;
    private final int cellHeight;

    public FindTopSurface(DensityFunction density, DensityFunction upperBound, int lowerBound, int cellHeight) {
        this.density = density;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.cellHeight = cellHeight;
    }

    @Override public double compute(FunctionContext ctx) {
        int n2 = Mth.floor(upperBound.compute(ctx) / cellHeight) * cellHeight;
        if (n2 <= lowerBound) {
            return lowerBound;
        }
        int n3 = n2;
        while (n3 >= lowerBound) {
            if (density.compute(new SinglePointContext(ctx.blockX(), n3, ctx.blockZ())) > 0.0) {
                return n3;
            }
            n3 -= cellHeight;
        }
        return lowerBound;
    }

    @Override public void fillArray(double[] array, ContextProvider provider) {
        provider.fillAllDirectly(array, this);
    }

    @Override public FindTopSurface mapAll(Visitor visitor) {
        return new FindTopSurface(density.mapAll(visitor), upperBound.mapAll(visitor), lowerBound, cellHeight);
    }

    @Override public double minValue() { return lowerBound; }
    @Override public double maxValue() { return Math.max(lowerBound, upperBound.maxValue()); }
}
