package com.CharunCore.server.worldgen.noisechunk.carver;

import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.world.gen.Mth;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.noisechunk.aquifer.Aquifer;

public class CanyonWorldCarver extends WorldCarver {

    public static final CanyonWorldCarver INSTANCE = new CanyonWorldCarver();

    private static final float VERTICAL_RADIUS_DEFAULT_FACTOR = 1.0f;
    private static final float VERTICAL_RADIUS_CENTER_FACTOR = 0.0f;
    private static final int WIDTH_SMOOTHNESS = 3;

    protected CanyonWorldCarver() {
        super(0.01f, -56);
    }

    @Override
    public boolean carve(Chunk chunk, CarvingMask mask, RandomSource random,
                          Aquifer aquifer, int originChunkX, int originChunkZ,
                          int targetChunkX, int targetChunkZ,
                          int minY, int height) {
        int range = 4;
        int rangeBlocks = (range * 2 - 1) * 16;
        int originMinX = originChunkX * 16;
        int originMinZ = originChunkZ * 16;
        int targetMinX = targetChunkX * 16;
        int targetMinZ = targetChunkZ * 16;

        double x = originMinX + random.nextInt(16);
        int y = 10 + random.nextInt(58);
        double z = originMinZ + random.nextInt(16);
        float yaw = random.nextFloat() * (float) (Math.PI * 2);
        float vertRotation = (random.nextFloat() - 0.5f) * 0.25f;
        double yScale = 3.0;
        float thickness = random.nextFloat() * 4.0f + random.nextFloat() * 2.0f;
        int genDepth = (int) ((float) rangeBlocks * (0.75f + random.nextFloat() * 0.25f));

        doCarve(chunk, mask, aquifer, minY, height, targetMinX, targetMinZ,
                random.nextLong(), x, y, z, thickness, yaw, vertRotation,
                0, genDepth, yScale);
        return true;
    }

    private void doCarve(Chunk chunk, CarvingMask mask, Aquifer aquifer,
                          int minY, int height, int targetMinX, int targetMinZ,
                          long seed, double x, double y, double z,
                          float thickness, float yaw, float vertRot,
                          int step, int maxSteps, double yScale) {
        RandomSource rng = new LegacyRandomSource(seed);
        float[] widthFactors = initWidthFactors(height, rng);
        float dpitch = 0.0f;
        float dyaw = 0.0f;

        int midX = targetMinX + 8;
        int midZ = targetMinZ + 8;

        for (int i = step; i < maxSteps; i++) {
            double radius = 1.5 + Math.sin(Math.PI * i / maxSteps) * thickness;
            double vRadius = radius * yScale;
            radius *= 0.75f + rng.nextFloat() * 0.25f;
            vRadius = updateVerticalRadius(rng, vRadius, maxSteps, i);

            float cp = (float) Math.cos(vertRot);
            float sp = (float) Math.sin(vertRot);
            x += Math.cos(yaw) * cp;
            y += sp;
            z += Math.sin(yaw) * cp;

            vertRot *= 0.7f;
            vertRot += dpitch * 0.05f;
            yaw += dyaw * 0.05f;
            dpitch *= 0.8f;
            dyaw *= 0.5f;
            dpitch += (rng.nextFloat() - rng.nextFloat()) * rng.nextFloat() * 2.0f;
            dyaw += (rng.nextFloat() - rng.nextFloat()) * rng.nextFloat() * 4.0f;

            if (rng.nextInt(4) == 0) continue;
            if (!canReach(midX, midZ, x, z, i, maxSteps, thickness)) return;

            final int stepRef = i;
            CarveSkipChecker skipChecker = (dx, dy, dz, yy) -> shouldSkip(widthFactors, dx, dy, dz, yy, minY);

            carveEllipsoid(chunk, mask, aquifer, minY, height, targetMinX, targetMinZ,
                    x, y, z, radius, vRadius, skipChecker);
        }
    }

    private static float[] initWidthFactors(int height, RandomSource random) {
        float[] factors = new float[height];
        float f = 1.0f;
        for (int i = 0; i < height; i++) {
            if (i == 0 || random.nextInt(WIDTH_SMOOTHNESS) == 0) {
                f = 1.0f + random.nextFloat() * random.nextFloat();
            }
            factors[i] = f * f;
        }
        return factors;
    }

    private static double updateVerticalRadius(RandomSource random, double radius, float maxSteps, int step) {
        float centerness = 1.0f - Mth.abs(0.5f - (float) step / maxSteps) * 2.0f;
        float factor = VERTICAL_RADIUS_DEFAULT_FACTOR + VERTICAL_RADIUS_CENTER_FACTOR * centerness;
        return (double) factor * radius * (double) Mth.randomBetween(random, 0.75f, 1.0f);
    }

    private static boolean shouldSkip(float[] widthFactors, double dx, double dy, double dz, int y, int minY) {
        int idx = y - minY;
        if (idx < 1) return true;
        if (idx - 1 >= widthFactors.length) return true;
        return (dx * dx + dz * dz) * (double) widthFactors[idx - 1] + dy * dy / 6.0 >= 1.0;
    }
}
