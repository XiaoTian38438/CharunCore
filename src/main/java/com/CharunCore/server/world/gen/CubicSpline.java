package com.CharunCore.server.world.gen;

public interface CubicSpline extends BoundedFloatFunction<float[]> {

    static CubicSpline constant(float value) {
        return new Constant(value);
    }

    static Builder builder(BoundedFloatFunction<float[]> coordinate) {
        return new Builder(coordinate, BoundedFloatFunction.IDENTITY);
    }

    static Builder builder(BoundedFloatFunction<float[]> coordinate, BoundedFloatFunction<Float> transform) {
        return new Builder(coordinate, transform);
    }

    @Override
    float apply(float[] context);

    final class Constant implements CubicSpline {
        private final float value;
        Constant(float value) { this.value = value; }
        @Override public float apply(float[] context) { return this.value; }
        @Override public float minValue() { return this.value; }
        @Override public float maxValue() { return this.value; }
    }

    final class Multipoint implements CubicSpline {
        private final BoundedFloatFunction<float[]> coordinate;
        private final float[] locations;
        private final CubicSpline[] values;
        private final float[] derivatives;
        private final float minValue;
        private final float maxValue;

        public Multipoint(BoundedFloatFunction<float[]> coordinate, float[] locations,
                          CubicSpline[] values, float[] derivatives) {
            this.coordinate = coordinate;
            this.locations = locations;
            this.values = values;
            this.derivatives = derivatives;
            float min = Float.POSITIVE_INFINITY;
            float max = Float.NEGATIVE_INFINITY;

            float coordMin = coordinate.minValue();
            float coordMax = coordinate.maxValue();
            if (coordMin < locations[0]) {
                float v0 = (values[0].minValue() + values[0].maxValue()) * 0.5f;
                float lo = linearExtend(coordMin, locations, v0, derivatives, 0);
                float hi = linearExtend(coordMax, locations, v0, derivatives, 0);
                min = Math.min(min, Math.min(lo, hi));
                max = Math.max(max, Math.max(lo, hi));
            }
            int last = locations.length - 1;
            if (coordMax > locations[last]) {
                float vN = (values[last].minValue() + values[last].maxValue()) * 0.5f;
                float lo = linearExtend(coordMin, locations, vN, derivatives, last);
                float hi = linearExtend(coordMax, locations, vN, derivatives, last);
                min = Math.min(min, Math.min(lo, hi));
                max = Math.max(max, Math.max(lo, hi));
            }
            for (CubicSpline v : values) {
                min = Math.min(min, v.minValue());
                max = Math.max(max, v.maxValue());
            }
            for (int i = 0; i < last; i++) {
                float locDiff = locations[i + 1] - locations[i];
                float d0Scaled = derivatives[i] * locDiff;
                float d1Scaled = derivatives[i + 1] * locDiff;
                float valMin = Math.min(values[i].minValue(), values[i + 1].minValue());
                float valMax = Math.max(values[i].maxValue(), values[i + 1].maxValue());
                float b0 = d0Scaled - (values[i + 1].maxValue() - values[i].minValue());
                float b1 = d0Scaled - (values[i + 1].minValue() - values[i].maxValue());
                float b2 = -d1Scaled + values[i + 1].maxValue() - values[i].minValue();
                float b3 = -d1Scaled + values[i + 1].minValue() - values[i].maxValue();
                float bMin = Math.min(b0, b2);
                float bMax = Math.max(b1, b3);
                min = Math.min(min, valMin + 0.25f * bMin);
                max = Math.max(max, valMax + 0.25f * bMax);
            }
            this.minValue = min;
            this.maxValue = max;
        }

        @Override
        public float apply(float[] context) {
            float coord = this.coordinate.apply(context);
            int i = findIntervalStart(this.locations, coord);
            int last = this.locations.length - 1;
            if (i < 0) {
                return linearExtend(coord, this.locations, this.values[0].apply(context), this.derivatives, 0);
            }
            if (i >= last) {
                return linearExtend(coord, this.locations, this.values[last].apply(context), this.derivatives, last);
            }
            float loc0 = this.locations[i];
            float loc1 = this.locations[i + 1];
            float t = (coord - loc0) / (loc1 - loc0);
            float val0 = this.values[i].apply(context);
            float val1 = this.values[i + 1].apply(context);
            float d0 = this.derivatives[i];
            float d1 = this.derivatives[i + 1];
            float a0 = d0 * (loc1 - loc0) - (val1 - val0);
            float a1 = -d1 * (loc1 - loc0) + (val1 - val0);
            return Mth.lerp(t, val0, val1) + t * (1.0f - t) * Mth.lerp(t, a0, a1);
        }

        private static float linearExtend(float coord, float[] locations, float value, float[] derivatives, int idx) {
            float d = derivatives[idx];
            if (d == 0.0f) return value;
            return value + d * (coord - locations[idx]);
        }

        private static int findIntervalStart(float[] locations, float coord) {
            int low = 0;
            int high = locations.length;
            while (low < high) {
                int mid = (low + high) >>> 1;
                if (coord < locations[mid]) high = mid;
                else low = mid + 1;
            }
            return low - 1;
        }

        @Override public float minValue() { return this.minValue; }
        @Override public float maxValue() { return this.maxValue; }
    }

    class Builder {
        private final BoundedFloatFunction<float[]> coordinate;
        private final BoundedFloatFunction<Float> transform;
        private final java.util.List<Float> locationList = new java.util.ArrayList<>();
        private final java.util.List<CubicSpline> valueList = new java.util.ArrayList<>();
        private final java.util.List<Float> derivativeList = new java.util.ArrayList<>();

        Builder(BoundedFloatFunction<float[]> coordinate, BoundedFloatFunction<Float> transform) {
            this.coordinate = coordinate;
            this.transform = transform;
        }

        public Builder addPoint(float location, float value) {
            return addPoint(location, value, 0.0f);
        }

        public Builder addPoint(float location, CubicSpline value) {
            return addPoint(location, value, 0.0f);
        }

        public Builder addPoint(float location, float value, float derivative) {
            this.locationList.add(location);
            this.valueList.add(CubicSpline.constant(this.transform.apply(value)));
            this.derivativeList.add(this.transform.apply(derivative));
            return this;
        }

        public Builder addPoint(float location, CubicSpline value, float derivative) {
            this.locationList.add(location);
            this.valueList.add(value);
            this.derivativeList.add(this.transform.apply(derivative));
            return this;
        }

        public CubicSpline build() {
            if (this.locationList.isEmpty())
                throw new IllegalArgumentException("Cannot create a multipoint spline with no points");
            float[] locs = new float[this.locationList.size()];
            CubicSpline[] vals = new CubicSpline[this.valueList.size()];
            float[] derivs = new float[this.derivativeList.size()];
            for (int i = 0; i < locs.length; i++) {
                locs[i] = this.locationList.get(i);
                vals[i] = this.valueList.get(i);
                derivs[i] = this.derivativeList.get(i);
            }
            return new Multipoint(this.coordinate, locs, vals, derivs);
        }
    }
}
