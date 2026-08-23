package com.CharunCore.server.worldgen.noisechunk.carver;

import java.util.Set;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.Mth;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.noisechunk.aquifer.Aquifer;

public abstract class WorldCarver {

    protected static final int AIR_ID = 0;
    protected static final int WATER_ID = BlockStateHelper.getDefault("water");
    protected static final int LAVA_ID = BlockStateHelper.getDefault("lava");
    protected static final int CAVE_AIR_ID = BlockStateHelper.getDefault("cave_air");
    protected static final int GRASS_BLOCK_ID = BlockStateHelper.getDefault("grass_block");
    protected static final int MYCELIUM_ID = BlockStateHelper.getDefault("mycelium");
    protected static final int DIRT_ID = BlockStateHelper.getDefault("dirt");
    protected static final int STONE_ID = BlockStateHelper.getDefault("stone");
    protected static final int SAND_ID = BlockStateHelper.getDefault("sand");
    protected static final int RED_SAND_ID = BlockStateHelper.getDefault("red_sand");
    protected static final int GRAVEL_ID = BlockStateHelper.getDefault("gravel");
    protected static final int SANDSTONE_ID = BlockStateHelper.getDefault("sandstone");
    protected static final int RED_SANDSTONE_ID = BlockStateHelper.getDefault("red_sandstone");
    protected static final int COARSE_DIRT_ID = BlockStateHelper.getDefault("coarse_dirt");
    protected static final int PODZOL_ID = BlockStateHelper.getDefault("podzol");
    protected static final int PACKED_ICE_ID = BlockStateHelper.getDefault("packed_ice");
    protected static final int SNOW_BLOCK_ID = BlockStateHelper.getDefault("snow_block");

    protected static final Set<Integer> REPLACEABLE = new java.util.HashSet<>(java.util.Arrays.asList(
        BlockStateHelper.getDefault("stone"),
        BlockStateHelper.getDefault("granite"),
        BlockStateHelper.getDefault("diorite"),
        BlockStateHelper.getDefault("andesite"),
        BlockStateHelper.getDefault("tuff"),
        BlockStateHelper.getDefault("deepslate"),
        BlockStateHelper.getDefault("dirt"),
        BlockStateHelper.getDefault("grass_block"),
        BlockStateHelper.getDefault("podzol"),
        BlockStateHelper.getDefault("coarse_dirt"),
        BlockStateHelper.getDefault("mycelium"),
        BlockStateHelper.getDefault("rooted_dirt"),
        BlockStateHelper.getDefault("moss_block"),
        BlockStateHelper.getDefault("pale_moss_block"),
        BlockStateHelper.getDefault("mud"),
        BlockStateHelper.getDefault("muddy_mangrove_roots"),
        BlockStateHelper.getDefault("sand"),
        BlockStateHelper.getDefault("red_sand"),
        BlockStateHelper.getDefault("suspicious_sand"),
        BlockStateHelper.getDefault("terracotta"),
        BlockStateHelper.getDefault("white_terracotta"),
        BlockStateHelper.getDefault("orange_terracotta"),
        BlockStateHelper.getDefault("magenta_terracotta"),
        BlockStateHelper.getDefault("light_blue_terracotta"),
        BlockStateHelper.getDefault("yellow_terracotta"),
        BlockStateHelper.getDefault("lime_terracotta"),
        BlockStateHelper.getDefault("pink_terracotta"),
        BlockStateHelper.getDefault("gray_terracotta"),
        BlockStateHelper.getDefault("light_gray_terracotta"),
        BlockStateHelper.getDefault("cyan_terracotta"),
        BlockStateHelper.getDefault("purple_terracotta"),
        BlockStateHelper.getDefault("blue_terracotta"),
        BlockStateHelper.getDefault("brown_terracotta"),
        BlockStateHelper.getDefault("green_terracotta"),
        BlockStateHelper.getDefault("red_terracotta"),
        BlockStateHelper.getDefault("black_terracotta"),
        BlockStateHelper.getDefault("iron_ore"),
        BlockStateHelper.getDefault("deepslate_iron_ore"),
        BlockStateHelper.getDefault("copper_ore"),
        BlockStateHelper.getDefault("deepslate_copper_ore"),
        BlockStateHelper.getDefault("snow"),
        BlockStateHelper.getDefault("snow_block"),
        BlockStateHelper.getDefault("powder_snow"),
        BlockStateHelper.getDefault("water"),
        BlockStateHelper.getDefault("gravel"),
        BlockStateHelper.getDefault("suspicious_gravel"),
        BlockStateHelper.getDefault("sandstone"),
        BlockStateHelper.getDefault("red_sandstone"),
        BlockStateHelper.getDefault("calcite"),
        BlockStateHelper.getDefault("packed_ice"),
        BlockStateHelper.getDefault("raw_iron_block"),
        BlockStateHelper.getDefault("raw_copper_block")
    ));

    protected final float probability;
    protected final int lavaLevel;

    protected WorldCarver(float probability, int lavaLevel) {
        this.probability = probability;
        this.lavaLevel = lavaLevel;
    }

    public boolean isStartChunk(RandomSource randomSource) {
        return randomSource.nextFloat() <= this.probability;
    }

    public abstract boolean carve(Chunk chunk, CarvingMask mask, RandomSource random,
                                   Aquifer aquifer, int originChunkX, int originChunkZ,
                                   int targetChunkX, int targetChunkZ,
                                   int minY, int height);

    protected boolean carveEllipsoid(Chunk chunk, CarvingMask mask, Aquifer aquifer,
                                       int minY, int height, int targetMinX, int targetMinZ,
                                       double cx, double cy, double cz,
                                       double hRadius, double vRadius,
                                       CarveSkipChecker skipChecker) {
        double midX = targetMinX + 8;
        double midZ = targetMinZ + 8;
        double earlyExit = 16.0 + hRadius * 2.0;
        if (Math.abs(cx - midX) > earlyExit || Math.abs(cz - midZ) > earlyExit) {
            return false;
        }

        int minX = Math.max(Mth.floor(cx - hRadius) - targetMinX - 1, 0);
        int maxX = Math.min(Mth.floor(cx + hRadius) - targetMinX, 15);
        int minZ = Math.max(Mth.floor(cz - hRadius) - targetMinZ - 1, 0);
        int maxZ = Math.min(Mth.floor(cz + hRadius) - targetMinZ, 15);
        int minYb = Math.max(Mth.floor(cy - vRadius) - 1, minY + 1);
        int maxYb = Math.min(Mth.floor(cy + vRadius) + 1, minY + height - 1);

        boolean carved = false;
        boolean[] grassAbove = {false};
        for (int lx = minX; lx <= maxX; lx++) {
            int bx = targetMinX + lx;
            double dx = (bx + 0.5 - cx) / hRadius;
            for (int lz = minZ; lz <= maxZ; lz++) {
                int bz = targetMinZ + lz;
                double dz = (bz + 0.5 - cz) / hRadius;
                double dxz = dx * dx + dz * dz;
                if (dxz >= 1.0) continue;
                grassAbove[0] = false;
                for (int y = maxYb; y > minYb; y--) {
                    double dy = (y - 0.5 - cy) / vRadius;
                    if (skipChecker.shouldSkip(dx, dy, dz, y)) continue;
                    if (mask.get(lx, y, lz)) continue;
                    mask.set(lx, y, lz);
                    carved |= carveBlock(chunk, aquifer, lx, y, lz, bx, bz, minY, grassAbove);
                }
            }
        }
        return carved;
    }

    protected boolean carveBlock(Chunk chunk, Aquifer aquifer,
                                  int lx, int y, int lz, int bx, int bz, int minY,
                                  boolean[] grassAbove) {
        int existing = chunk.getBlock(lx, y, lz);

        if (existing == GRASS_BLOCK_ID || existing == MYCELIUM_ID) {
            grassAbove[0] = true;
        }

        if (!REPLACEABLE.contains(existing)) {
            return false;
        }

        int blockState;
        if (y <= lavaLevel) {
            blockState = LAVA_ID;
        } else {
            Integer result = aquifer.computeSubstance(
                    new DensityFunction.SinglePointContext(bx, y, bz), 0.0);
            if (result == null) {
                return false;
            }
            blockState = result;
        }

        chunk.setBlock(lx, y, lz, blockState);

        if (grassAbove[0]) {
            int below = chunk.getBlock(lx, y - 1, lz);
            if (below == DIRT_ID) {
                chunk.setBlock(lx, y - 1, lz, GRASS_BLOCK_ID);
            }
        }

        return true;
    }

    protected static boolean canReach(int chunkMiddleX, int chunkMiddleZ,
                                       double x, double z, int step, int maxSteps, float thickness) {
        double dx = x - chunkMiddleX;
        double dz = z - chunkMiddleZ;
        double remaining = maxSteps - step;
        double range = thickness + 2.0f + 16.0f;
        return dx * dx + dz * dz - remaining * remaining <= range * range;
    }

    @FunctionalInterface
    public interface CarveSkipChecker {
        boolean shouldSkip(double dx, double dy, double dz, int y);
    }
}
