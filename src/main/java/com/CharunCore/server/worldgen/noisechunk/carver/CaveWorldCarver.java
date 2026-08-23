package com.CharunCore.server.worldgen.noisechunk.carver;

import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.noisechunk.aquifer.Aquifer;

public class CaveWorldCarver extends WorldCarver {

    // Vanilla configured_carver/cave.json: probability 0.15, y range [above_bottom 8, absolute 180]
    public static final CaveWorldCarver INSTANCE = new CaveWorldCarver();
    // Vanilla configured_carver/cave_extra_underground.json: probability 0.07, y range [above_bottom 8, absolute 47]
    public static final CaveWorldCarver CAVE_EXTRA = new CaveWorldCarver(0.07f, 47);

    protected CaveWorldCarver() {
        super(0.15f, -56);
        this.maxY = 180;
    }

    protected CaveWorldCarver(float probability, int maxY) {
        super(probability, -56);
        this.maxY = maxY;
    }

    private int maxY;

    @Override
    public boolean carve(Chunk chunk, CarvingMask mask, RandomSource random,
                          Aquifer aquifer, int originChunkX, int originChunkZ,
                          int targetChunkX, int targetChunkZ,
                          int minY, int height) {
        int range = 4;
        int rangeBlocks = (range * 2 - 1) * 16;
        int caveCount = random.nextInt(random.nextInt(random.nextInt(15) + 1) + 1);
        int originMinX = originChunkX * 16;
        int originMinZ = originChunkZ * 16;
        int targetMinX = targetChunkX * 16;
        int targetMinZ = targetChunkZ * 16;

        for (int i = 0; i < caveCount; i++) {
            double x = originMinX + random.nextInt(16);
            double y = sampleY(random, minY, height);
            double z = originMinZ + random.nextInt(16);
            double hRadiusMul = random.nextFloat() * 0.7 + 0.7;
            double vRadiusMul = random.nextFloat() * 0.5 + 0.8;
            double floorLevel = random.nextFloat() * 0.6 - 1.0;

            CarveSkipChecker skipChecker = (dx, dy, dz, yy) -> shouldSkip(dx, dy, dz, floorLevel);

            int tunnels = 1;
            if (random.nextInt(4) == 0) {
                double yScale = random.nextFloat() * 0.8 + 0.1;
                double roomThickness = 1.0 + random.nextFloat() * 6.0;
                createRoom(chunk, mask, aquifer, minY, height, targetMinX, targetMinZ,
                        x, y, z, (float) roomThickness, yScale, skipChecker);
                tunnels += random.nextInt(4);
            }

            for (int j = 0; j < tunnels; j++) {
                float yaw = random.nextFloat() * (float) (Math.PI * 2);
                float pitch = (random.nextFloat() - 0.5f) / 4.0f;
                float thickness = getThickness(random);
                int tunnelLen = rangeBlocks - random.nextInt(rangeBlocks / 4);
                createTunnel(chunk, mask, aquifer, minY, height, targetMinX, targetMinZ,
                        random.nextLong(), x, y, z, hRadiusMul, vRadiusMul,
                        thickness, yaw, pitch, 0, tunnelLen, 1.0, skipChecker);
            }
        }
        return true;
    }

    private double sampleY(RandomSource random, int minY, int height) {
        int yMin = minY + 8;
        return yMin + random.nextInt(maxY - yMin + 1);
    }

    private float getThickness(RandomSource random) {
        float f = random.nextFloat() * 2.0f + random.nextFloat();
        if (random.nextInt(10) == 0) {
            f *= random.nextFloat() * random.nextFloat() * 3.0f + 1.0f;
        }
        return f;
    }

    private void createRoom(Chunk chunk, CarvingMask mask, Aquifer aquifer,
                             int minY, int height, int targetMinX, int targetMinZ,
                             double x, double y, double z, float thickness, double yScale,
                             CarveSkipChecker skipChecker) {
        double radius = 1.5 + Math.sin(1.5707963705062866) * thickness;
        double vRadius = radius * yScale;
        carveEllipsoid(chunk, mask, aquifer, minY, height, targetMinX, targetMinZ,
                x + 1.0, y, z, radius, vRadius, skipChecker);
    }

    private void createTunnel(Chunk chunk, CarvingMask mask, Aquifer aquifer,
                               int minY, int height, int targetMinX, int targetMinZ,
                               long seed, double x, double y, double z,
                               double hMul, double vMul, float thickness,
                               float yaw, float pitch, int step, int maxSteps,
                               double yScale, CarveSkipChecker skipChecker) {
        RandomSource rng = new LegacyRandomSource(seed);
        int branchStep = rng.nextInt(maxSteps / 2) + maxSteps / 4;
        boolean slowPitch = rng.nextInt(6) == 0;
        float dpitch = 0.0f;
        float dyaw = 0.0f;

        int midX = targetMinX + 8;
        int midZ = targetMinZ + 8;

        for (int i = step; i < maxSteps; i++) {
            double radius = 1.5 + Math.sin(Math.PI * i / maxSteps) * thickness;
            double vRadius = radius * yScale;

            float cp = (float) Math.cos(pitch);
            x += Math.cos(yaw) * cp;
            y += Math.sin(pitch);
            z += Math.sin(yaw) * cp;

            pitch *= slowPitch ? 0.92f : 0.7f;
            pitch += dpitch * 0.1f;
            yaw += dyaw * 0.1f;
            dpitch *= 0.9f;
            dyaw *= 0.75f;
            dpitch += (rng.nextFloat() - rng.nextFloat()) * rng.nextFloat() * 2.0f;
            dyaw += (rng.nextFloat() - rng.nextFloat()) * rng.nextFloat() * 4.0f;

            if (i == branchStep && thickness > 1.0f) {
                createTunnel(chunk, mask, aquifer, minY, height, targetMinX, targetMinZ,
                        rng.nextLong(), x, y, z, hMul, vMul,
                        rng.nextFloat() * 0.5f + 0.5f, yaw - 1.5707964f, pitch / 3.0f,
                        i, maxSteps, 1.0, skipChecker);
                createTunnel(chunk, mask, aquifer, minY, height, targetMinX, targetMinZ,
                        rng.nextLong(), x, y, z, hMul, vMul,
                        rng.nextFloat() * 0.5f + 0.5f, yaw + 1.5707964f, pitch / 3.0f,
                        i, maxSteps, 1.0, skipChecker);
                return;
            }

            if (rng.nextInt(4) == 0) continue;
            if (!canReach(midX, midZ, x, z, i, maxSteps, thickness)) return;

            carveEllipsoid(chunk, mask, aquifer, minY, height, targetMinX, targetMinZ,
                    x, y, z, radius * hMul, vRadius * vMul, skipChecker);
        }
    }

    private static boolean shouldSkip(double dx, double dy, double dz, double floorLevel) {
        if (dy <= floorLevel) {
            return true;
        }
        return dx * dx + dy * dy + dz * dz >= 1.0;
    }
}
