package com.CharunCore.server.world.gen;

public class TerrainProvider {

    public static final BoundedFloatFunction<Float> AMPLIFIED_OFFSET = BoundedFloatFunction.createUnlimited(v -> v < 0 ? v : v * 2.0f);
    public static final BoundedFloatFunction<Float> AMPLIFIED_FACTOR = BoundedFloatFunction.createUnlimited(v -> 1.25f - 6.25f / (v + 5.0f));
    public static final BoundedFloatFunction<Float> AMPLIFIED_JAGGEDNESS = BoundedFloatFunction.createUnlimited(v -> v * 2.0f);
    private static final BoundedFloatFunction<Float> NO_TRANSFORM = BoundedFloatFunction.IDENTITY;

    private static final int IDX_CONTINENTALNESS = 0;
    private static final int IDX_EROSION = 1;
    private static final int IDX_RIDGES = 2;
    private static final int IDX_RIDGES_FOLDED = 3;

    public static CubicSpline overworldOffset(BoundedFloatFunction<float[]> continentalnessCoord,
                                              BoundedFloatFunction<float[]> erosionCoord,
                                              BoundedFloatFunction<float[]> ridgesFoldedCoord,
                                              boolean amplified) {
        BoundedFloatFunction<Float> transform = amplified ? AMPLIFIED_OFFSET : NO_TRANSFORM;

        CubicSpline e1 = buildErosionOffsetSpline(erosionCoord, ridgesFoldedCoord, -0.15f, 0.0f, 0.0f, 0.1f, 0.0f, -0.03f, false, false, transform);
        CubicSpline e2 = buildErosionOffsetSpline(erosionCoord, ridgesFoldedCoord, -0.1f, 0.03f, 0.1f, 0.1f, 0.01f, -0.03f, false, false, transform);
        CubicSpline e3 = buildErosionOffsetSpline(erosionCoord, ridgesFoldedCoord, -0.1f, 0.03f, 0.1f, 0.7f, 0.01f, -0.03f, true, true, transform);
        CubicSpline e4 = buildErosionOffsetSpline(erosionCoord, ridgesFoldedCoord, -0.05f, 0.03f, 0.1f, 1.0f, 0.01f, 0.01f, true, true, transform);

        return CubicSpline.builder(continentalnessCoord, transform)
            .addPoint(-1.1f, 0.044f)
            .addPoint(-1.02f, -0.2222f)
            .addPoint(-0.51f, -0.2222f)
            .addPoint(-0.44f, -0.12f)
            .addPoint(-0.18f, -0.12f)
            .addPoint(-0.16f, e1)
            .addPoint(-0.15f, e1)
            .addPoint(-0.1f, e2)
            .addPoint(0.25f, e3)
            .addPoint(1.0f, e4)
            .build();
    }

    public static CubicSpline overworldFactor(BoundedFloatFunction<float[]> continentalnessCoord,
                                              BoundedFloatFunction<float[]> erosionCoord,
                                              BoundedFloatFunction<float[]> ridgesCoord,
                                              BoundedFloatFunction<float[]> ridgesFoldedCoord,
                                              boolean amplified) {
        BoundedFloatFunction<Float> transform = amplified ? AMPLIFIED_FACTOR : NO_TRANSFORM;

        return CubicSpline.builder(continentalnessCoord, NO_TRANSFORM)
            .addPoint(-0.19f, 3.95f)
            .addPoint(-0.15f, getErosionFactor(erosionCoord, ridgesCoord, ridgesFoldedCoord, 6.25f, true, NO_TRANSFORM))
            .addPoint(-0.1f, getErosionFactor(erosionCoord, ridgesCoord, ridgesFoldedCoord, 5.47f, true, transform))
            .addPoint(0.03f, getErosionFactor(erosionCoord, ridgesCoord, ridgesFoldedCoord, 5.08f, true, transform))
            .addPoint(0.06f, getErosionFactor(erosionCoord, ridgesCoord, ridgesFoldedCoord, 4.69f, false, transform))
            .build();
    }

    public static CubicSpline overworldJaggedness(BoundedFloatFunction<float[]> continentalnessCoord,
                                                  BoundedFloatFunction<float[]> erosionCoord,
                                                  BoundedFloatFunction<float[]> ridgesCoord,
                                                  BoundedFloatFunction<float[]> ridgesFoldedCoord,
                                                  boolean amplified) {
        BoundedFloatFunction<Float> transform = amplified ? AMPLIFIED_JAGGEDNESS : NO_TRANSFORM;

        return CubicSpline.builder(continentalnessCoord, transform)
            .addPoint(-0.11f, 0.0f)
            .addPoint(0.03f, buildErosionJaggednessSpline(erosionCoord, ridgesCoord, ridgesFoldedCoord, 1.0f, 0.5f, 0.0f, 0.0f, transform))
            .addPoint(0.65f, buildErosionJaggednessSpline(erosionCoord, ridgesCoord, ridgesFoldedCoord, 1.0f, 1.0f, 1.0f, 0.0f, transform))
            .build();
    }

    private static CubicSpline buildErosionJaggednessSpline(
            BoundedFloatFunction<float[]> erosionCoord, BoundedFloatFunction<float[]> ridgesCoord,
            BoundedFloatFunction<float[]> ridgesFoldedCoord,
            float f1, float f2, float f3, float f4, BoundedFloatFunction<Float> transform) {
        CubicSpline rj1 = buildRidgeJaggednessSpline(ridgesCoord, ridgesFoldedCoord, f1, f3, transform);
        CubicSpline rj2 = buildRidgeJaggednessSpline(ridgesCoord, ridgesFoldedCoord, f2, f4, transform);

        return CubicSpline.builder(erosionCoord, transform)
            .addPoint(-1.0f, rj1)
            .addPoint(-0.78f, rj2)
            .addPoint(-0.5775f, rj2)
            .addPoint(-0.375f, 0.0f)
            .build();
    }

    private static CubicSpline buildRidgeJaggednessSpline(
            BoundedFloatFunction<float[]> ridgesCoord, BoundedFloatFunction<float[]> ridgesFoldedCoord,
            float weirdnessFactor, float minWeirdness, BoundedFloatFunction<Float> transform) {
        float pv1 = peaksAndValleys(0.4f);
        float pv2 = peaksAndValleys(0.56666666f);
        float f3 = (pv1 + pv2) / 2.0f;

        CubicSpline.Builder builder = CubicSpline.builder(ridgesFoldedCoord, transform);
        builder.addPoint(pv1, 0.0f);
        if (minWeirdness > 0.0f) {
            builder.addPoint(f3, buildWeirdnessJaggednessSpline(ridgesCoord, minWeirdness, transform));
        } else {
            builder.addPoint(f3, 0.0f);
        }
        if (weirdnessFactor > 0.0f) {
            builder.addPoint(1.0f, buildWeirdnessJaggednessSpline(ridgesCoord, weirdnessFactor, transform));
        } else {
            builder.addPoint(1.0f, 0.0f);
        }
        return builder.build();
    }

    private static CubicSpline buildWeirdnessJaggednessSpline(
            BoundedFloatFunction<float[]> ridgesCoord, float factor, BoundedFloatFunction<Float> transform) {
        float f1 = 0.63f * factor;
        float f2 = 0.3f * factor;
        return CubicSpline.builder(ridgesCoord, transform)
            .addPoint(-0.01f, f1)
            .addPoint(0.01f, f2)
            .build();
    }

    private static CubicSpline getErosionFactor(
            BoundedFloatFunction<float[]> erosionCoord, BoundedFloatFunction<float[]> ridgesCoord,
            BoundedFloatFunction<float[]> ridgesFoldedCoord,
            float baseFactor, boolean hasRidgeContribution, BoundedFloatFunction<Float> transform) {
        CubicSpline erosionInner = CubicSpline.builder(ridgesCoord, transform)
            .addPoint(-0.2f, 6.3f)
            .addPoint(0.2f, baseFactor)
            .build();

        CubicSpline.Builder builder = CubicSpline.builder(erosionCoord, transform)
            .addPoint(-0.6f, erosionInner)
            .addPoint(-0.5f, CubicSpline.builder(ridgesCoord, transform).addPoint(-0.05f, 6.3f).addPoint(0.05f, 2.67f).build())
            .addPoint(-0.35f, erosionInner)
            .addPoint(-0.25f, erosionInner)
            .addPoint(-0.1f, CubicSpline.builder(ridgesCoord, transform).addPoint(-0.05f, 2.67f).addPoint(0.05f, 6.3f).build())
            .addPoint(0.03f, erosionInner);

        if (hasRidgeContribution) {
            CubicSpline ridgeErosionInner = CubicSpline.builder(ridgesCoord, transform)
                .addPoint(0.0f, baseFactor)
                .addPoint(0.1f, 0.625f)
                .build();

            CubicSpline ridgeSpline = CubicSpline.builder(ridgesFoldedCoord, transform)
                .addPoint(-0.9f, baseFactor)
                .addPoint(-0.69f, ridgeErosionInner)
                .build();

            builder
                .addPoint(0.35f, baseFactor)
                .addPoint(0.45f, ridgeSpline)
                .addPoint(0.55f, ridgeSpline)
                .addPoint(0.62f, baseFactor);
        } else {
            CubicSpline ridgeContrib1 = CubicSpline.builder(ridgesFoldedCoord, transform)
                .addPoint(-0.7f, erosionInner)
                .addPoint(-0.15f, 1.37f)
                .build();

            CubicSpline ridgeContrib2 = CubicSpline.builder(ridgesFoldedCoord, transform)
                .addPoint(0.45f, erosionInner)
                .addPoint(0.7f, 1.56f)
                .build();

            builder
                .addPoint(0.05f, ridgeContrib2)
                .addPoint(0.4f, ridgeContrib2)
                .addPoint(0.45f, ridgeContrib1)
                .addPoint(0.55f, ridgeContrib1)
                .addPoint(0.58f, baseFactor);
        }
        return builder.build();
    }

    public static CubicSpline buildErosionOffsetSpline(
            BoundedFloatFunction<float[]> erosionCoord, BoundedFloatFunction<float[]> ridgesFoldedCoord,
            float e1, float e2, float e3, float e4, float e5, float e6,
            boolean b1, boolean b2, BoundedFloatFunction<Float> transform) {
        CubicSpline r1 = buildMountainRidgeSplineWithPoints(ridgesFoldedCoord, Mth.lerp(e4, 0.6f, 1.5f), b2, transform);
        CubicSpline r2 = buildMountainRidgeSplineWithPoints(ridgesFoldedCoord, Mth.lerp(e4, 0.6f, 1.0f), b2, transform);
        CubicSpline r3 = buildMountainRidgeSplineWithPoints(ridgesFoldedCoord, e4, b2, transform);
        CubicSpline r4 = ridgeSpline(ridgesFoldedCoord, e1 - 0.15f, 0.5f * e4,
            Mth.lerp(0.5f, 0.5f, 0.5f) * e4, 0.5f * e4, 0.6f * e4, 0.5f, transform);
        CubicSpline r5 = ridgeSpline(ridgesFoldedCoord, e1, e5 * e4, e2 * e4, 0.5f * e4, 0.6f * e4, 0.5f, transform);
        CubicSpline r6 = ridgeSpline(ridgesFoldedCoord, e1, e5, e5, e2, e3, 0.5f, transform);

        CubicSpline inner = CubicSpline.builder(ridgesFoldedCoord, transform)
            .addPoint(-1.0f, e1)
            .addPoint(-0.4f, r6)
            .addPoint(0.0f, e3 + 0.07f)
            .build();

        CubicSpline r8 = ridgeSpline(ridgesFoldedCoord, -0.02f, e6, e6, e2, e3, 0.0f, transform);

        CubicSpline.Builder builder = CubicSpline.builder(erosionCoord, transform)
            .addPoint(-0.85f, r1)
            .addPoint(-0.7f, r2)
            .addPoint(-0.4f, r3)
            .addPoint(-0.35f, r4)
            .addPoint(-0.1f, r5)
            .addPoint(0.2f, r6);

        if (b1) {
            builder
                .addPoint(0.4f, r6)
                .addPoint(0.45f, inner)
                .addPoint(0.55f, inner)
                .addPoint(0.58f, r6);
        }
        builder.addPoint(0.7f, r8);
        return builder.build();
    }

    private static CubicSpline buildMountainRidgeSplineWithPoints(
            BoundedFloatFunction<float[]> ridgesFoldedCoord, float mountainHeight,
            boolean surfaceDepthOverride, BoundedFloatFunction<Float> transform) {
        CubicSpline.Builder builder = CubicSpline.builder(ridgesFoldedCoord, transform);

        float f3 = mountainContinentalness(-1.0f, mountainHeight, -0.7f);
        float f5 = mountainContinentalness(1.0f, mountainHeight, -0.7f);
        float f6 = calculateMountainRidgeZeroContinentalnessPoint(mountainHeight);

        if (-0.65f < f6 && f6 < 1.0f) {
            float f8 = mountainContinentalness(-0.65f, mountainHeight, -0.7f);
            float f10 = mountainContinentalness(-0.75f, mountainHeight, -0.7f);
            float f11 = calculateSlope(f3, f10, -1.0f, -0.75f);
            float f12 = mountainContinentalness(f6, mountainHeight, -0.7f);
            float f13 = calculateSlope(f12, f5, f6, 1.0f);

            builder.addPoint(-1.0f, f3, f11);
            builder.addPoint(-0.75f, f10);
            builder.addPoint(-0.65f, f8);
            builder.addPoint(f6 - 0.01f, f12);
            builder.addPoint(f6, f12, f13);
            builder.addPoint(1.0f, f5, f13);
        } else {
            float slope = calculateSlope(f3, f5, -1.0f, 1.0f);
            if (surfaceDepthOverride) {
                builder.addPoint(-1.0f, Math.max(0.2f, f3));
                builder.addPoint(0.0f, Mth.lerp(0.5f, f3, f5), slope);
            } else {
                builder.addPoint(-1.0f, f3, slope);
            }
            builder.addPoint(1.0f, f5, slope);
        }
        return builder.build();
    }

    private static CubicSpline ridgeSpline(
            BoundedFloatFunction<float[]> coord, float v1, float v2, float v3, float v4, float v5,
            float v6, BoundedFloatFunction<Float> transform) {
        float slope1 = Math.max(0.5f * (v2 - v1), v6);
        float slope2 = 5.0f * (v3 - v2);
        return CubicSpline.builder(coord, transform)
            .addPoint(-1.0f, v1, slope1)
            .addPoint(-0.4f, v2, Math.min(slope1, slope2))
            .addPoint(0.0f, v3, slope2)
            .addPoint(0.4f, v4, 2.0f * (v4 - v3))
            .addPoint(1.0f, v5, 0.7f * (v5 - v4))
            .build();
    }

    private static float mountainContinentalness(float continentalness, float mountainHeight, float oceanThreshold) {
        float f3 = 1.0f - (1.0f - mountainHeight) * 0.5f;
        float f4 = 0.5f * (1.0f - mountainHeight);
        float f5 = (continentalness + 1.17f) * 0.46082947f;
        float f6 = f5 * f3 - f4;
        if (continentalness < oceanThreshold) return Math.max(f6, -0.2222f);
        return Math.max(f6, 0.0f);
    }

    private static float calculateMountainRidgeZeroContinentalnessPoint(float mountainHeight) {
        float f3 = 1.0f - (1.0f - mountainHeight) * 0.5f;
        float f4 = 0.5f * (1.0f - mountainHeight);
        return f4 / (0.46082947f * f3) - 1.17f;
    }

    public static float peaksAndValleys(float v) {
        return -(Math.abs(Math.abs(v) - 0.6666667f) - 0.33333334f) * 3.0f;
    }

    private static float calculateSlope(float v1, float v2, float x1, float x2) {
        return (v2 - v1) / (x2 - x1);
    }

    public static BoundedFloatFunction<float[]> coordinate(int index) {
        return new BoundedFloatFunction<float[]>() {
            @Override public float apply(float[] ctx) { return ctx[index]; }
            @Override public float minValue() { return -1.0f; }
            @Override public float maxValue() { return 1.0f; }
        };
    }
}
