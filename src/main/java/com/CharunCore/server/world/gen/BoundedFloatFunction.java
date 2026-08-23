package com.CharunCore.server.world.gen;

public interface BoundedFloatFunction<C> {
    BoundedFloatFunction<Float> IDENTITY = new BoundedFloatFunction<Float>() {
        public float apply(Float f) { return f; }
        public float minValue() { return Float.NEGATIVE_INFINITY; }
        public float maxValue() { return Float.POSITIVE_INFINITY; }
    };

    static BoundedFloatFunction<Float> createUnlimited(java.util.function.UnaryOperator<Float> fn) {
        return new BoundedFloatFunction<Float>() {
            public float apply(Float f) { return fn.apply(f); }
            public float minValue() { return Float.NEGATIVE_INFINITY; }
            public float maxValue() { return Float.POSITIVE_INFINITY; }
        };
    }

    float apply(C value);
    float minValue();
    float maxValue();
}
