package com.CharunCore.server.worldgen.density.functions;

import com.CharunCore.server.worldgen.density.DensityFunction;

/** 原版 DensityFunctions$Marker — Interpolated/FlatCache/Cache2D/CacheOnce/CacheAllInCell 5 种缓存语义标记 */
public final class Marker implements DensityFunction {
    public enum Type {
        INTERPOLATED, FLAT_CACHE, CACHE2D, CACHE_ONCE, CACHE_ALL_IN_CELL;
        public final String name = name().toLowerCase(java.util.Locale.ROOT);
    }

    private final Type type;
    private final DensityFunction wrapped;

    public Marker(Type type, DensityFunction wrapped) {
        this.type = type;
        this.wrapped = wrapped;
    }

    public Type type() { return type; }
    public DensityFunction wrapped() { return wrapped; }

    @Override public double compute(FunctionContext ctx) { return wrapped.compute(ctx); }

    @Override public void fillArray(double[] array, ContextProvider provider) { wrapped.fillArray(array, provider); }

    @Override public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new Marker(type, wrapped.mapAll(visitor)));
    }

    @Override public double minValue() { return wrapped.minValue(); }
    @Override public double maxValue() { return wrapped.maxValue(); }
}
