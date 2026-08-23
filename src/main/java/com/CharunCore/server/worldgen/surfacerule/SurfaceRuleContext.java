package com.CharunCore.server.worldgen.surfacerule;

import java.util.HashMap;
import java.util.Map;

import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.Mth;
import com.CharunCore.server.world.gen.PositionalRandomFactory;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.noisechunk.ChunkPos;
import com.CharunCore.server.worldgen.surfacerule.SurfaceRules.Condition;

public class SurfaceRuleContext {
    public final SurfaceSystem system;
    public final Chunk chunk;
    public int lastUpdateXZ;
    public int lastUpdateY;
    public int blockX;
    public int blockZ;
    public int surfaceDepth;
    public int blockY;
    public int waterHeight;
    public int stoneDepthAbove;
    public int stoneDepthBelow;

    private int columnBiomeY = Integer.MIN_VALUE;
    private int cachedColumnBiome = Integer.MIN_VALUE;

    public final Condition temperature;
    public final Condition steep;
    public final Condition hole;
    public final Condition abovePreliminarySurface;

    private int cachedX = Integer.MIN_VALUE;
    private int cachedZ = Integer.MIN_VALUE;
    private double cachedSurfaceSecondary;
    private boolean surfaceSecondaryComputed;
    private final Map<String, PositionalRandomFactory> randomFactoryCache = new HashMap<>();

    private int lastMinSurfaceLevelUpdate = -1;
    private long lastPreliminarySurfaceCellOrigin = Long.MIN_VALUE;
    private final int[] preliminarySurfaceCache = new int[4];
    private int minSurfaceLevel;

    public PositionalRandomFactory getRandomFactory(String name) {
        return randomFactoryCache.computeIfAbsent(name,
            n -> DensityFunction.NoiseHolder.sharedFactory().fromHashOf(n).forkPositional());
    }

    private static int blockCoordToSurfaceCell(int n) {
        return n >> 4;
    }

    private static int surfaceCellToBlockCoord(int n) {
        return n << 4;
    }

    public SurfaceRuleContext(SurfaceSystem system, Chunk chunk) {
        this.system = system;
        this.chunk = chunk;
        this.temperature = new TemperatureHelperCondition(this);
        this.steep = new SteepMaterialCondition(this);
        this.hole = new HoleCondition(this);
        this.abovePreliminarySurface = new AbovePreliminarySurfaceCondition(this);
    }

    public void updateXZ(int x, int z) {
        this.lastUpdateXZ++;
        this.lastUpdateY++;
        this.blockX = x;
        this.blockZ = z;
        this.surfaceDepth = this.system.getSurfaceDepth(x, z);
        this.surfaceSecondaryComputed = false;
        this.cachedColumnBiome = Integer.MIN_VALUE;
    }

    public void setSurfaceY(int surfaceY) {
        this.columnBiomeY = surfaceY;
        this.cachedColumnBiome = Integer.MIN_VALUE;
    }

    public void updateY(int stoneAbove, int stoneBelow, int waterHeight, int y) {
        this.lastUpdateY++;
        this.blockY = y;
        this.waterHeight = waterHeight;
        this.stoneDepthAbove = stoneAbove;
        this.stoneDepthBelow = stoneBelow;
    }

    public double getSurfaceSecondary() {
        if (!surfaceSecondaryComputed) {
            cachedSurfaceSecondary = system.getSurfaceSecondary(blockX, blockZ);
            surfaceSecondaryComputed = true;
        }
        return cachedSurfaceSecondary;
    }

    public int getMinSurfaceLevel() {
        if (lastMinSurfaceLevelUpdate != lastUpdateXZ) {
            lastMinSurfaceLevelUpdate = lastUpdateXZ;
            int cellX = blockCoordToSurfaceCell(blockX);
            int cellZ = blockCoordToSurfaceCell(blockZ);
            long cellKey = ChunkPos.asLong(cellX, cellZ);
            if (lastPreliminarySurfaceCellOrigin != cellKey) {
                lastPreliminarySurfaceCellOrigin = cellKey;
                preliminarySurfaceCache[0] = system.getPreliminarySurfaceLevel(surfaceCellToBlockCoord(cellX), surfaceCellToBlockCoord(cellZ));
                preliminarySurfaceCache[1] = system.getPreliminarySurfaceLevel(surfaceCellToBlockCoord(cellX + 1), surfaceCellToBlockCoord(cellZ));
                preliminarySurfaceCache[2] = system.getPreliminarySurfaceLevel(surfaceCellToBlockCoord(cellX), surfaceCellToBlockCoord(cellZ + 1));
                preliminarySurfaceCache[3] = system.getPreliminarySurfaceLevel(surfaceCellToBlockCoord(cellX + 1), surfaceCellToBlockCoord(cellZ + 1));
            }
            int lerp = Mth.floor(Mth.lerp2(
                (double)(blockX & 0xF) / 16.0,
                (double)(blockZ & 0xF) / 16.0,
                preliminarySurfaceCache[0], preliminarySurfaceCache[1],
                preliminarySurfaceCache[2], preliminarySurfaceCache[3]));
            minSurfaceLevel = lerp + surfaceDepth - 8;
        }
        return minSurfaceLevel;
    }

    public int getBiome() {
        if (cachedColumnBiome == Integer.MIN_VALUE) {
            int y = columnBiomeY != Integer.MIN_VALUE ? columnBiomeY : blockY;
            cachedColumnBiome = system.getBiome(blockX, y, blockZ);
        }
        return cachedColumnBiome;
    }

    public boolean isColdEnoughToSnow() {
        return system.isColdEnoughToSnowBiome(getBiome());
    }

    public int getSeaLevel() {
        return system.getSeaLevel();
    }

    static class TemperatureHelperCondition extends SurfaceRules.LazyYCondition {
        TemperatureHelperCondition(SurfaceRuleContext context) {
            super(context);
        }

        @Override
        protected boolean compute() {
            return context.isColdEnoughToSnow();
        }
    }

    static class SteepMaterialCondition extends SurfaceRules.LazyXZCondition {
        SteepMaterialCondition(SurfaceRuleContext context) {
            super(context);
        }

        @Override
        protected boolean compute() {
            int lx = context.blockX & 15;
            int lz = context.blockZ & 15;
            int chunkMinX = context.blockX - lx;
            int chunkMinZ = context.blockZ - lz;
            int lzM1 = Math.max(lz - 1, 0);
            int lzP1 = Math.min(lz + 1, 15);
            int hzM1 = context.system.getSurfaceHeight(chunkMinX + lx, chunkMinZ + lzM1);
            int hzP1 = context.system.getSurfaceHeight(chunkMinX + lx, chunkMinZ + lzP1);
            if (hzP1 >= hzM1 + 4) return true;
            int lxM1 = Math.max(lx - 1, 0);
            int lxP1 = Math.min(lx + 1, 15);
            int hxM1 = context.system.getSurfaceHeight(chunkMinX + lxM1, chunkMinZ + lz);
            int hxP1 = context.system.getSurfaceHeight(chunkMinX + lxP1, chunkMinZ + lz);
            return hxM1 >= hxP1 + 4;
        }
    }

    static class HoleCondition extends SurfaceRules.LazyXZCondition {
        HoleCondition(SurfaceRuleContext context) {
            super(context);
        }

        @Override
        protected boolean compute() {
            return context.surfaceDepth <= 0;
        }
    }

    static class AbovePreliminarySurfaceCondition implements SurfaceRules.Condition {
        private final SurfaceRuleContext context;

        AbovePreliminarySurfaceCondition(SurfaceRuleContext context) {
            this.context = context;
        }

        @Override
        public boolean test() {
            return context.blockY >= context.getMinSurfaceLevel();
        }
    }
}
