package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.DensityRouterChunkGenerator;
import com.CharunCore.server.worldgen.density.DensityFunction;

public class DiagnosticMain {
    static final int BIOME_PLAINS = 40;
    static final String[] BIOME_NAMES = {
        "BADLANDS", "BAMBOO_JUNGLE", "-2", "BEACH",
        "BIRCH_FOREST", "-5", "COLD_OCEAN", "-7",
        "DARK_FOREST", "DEEP_COLD_OCEAN", "DEEP_DARK", "DEEP_FROZEN_OCEAN",
        "DEEP_LUKEWARM_OCEAN", "DEEP_OCEAN", "DESERT", "DRIPSTONE_CAVES",
        "-16", "-17", "-18", "ERODED_BADLANDS",
        "FLOWER_FOREST", "FOREST", "FROZEN_OCEAN", "FROZEN_PEAKS",
        "FROZEN_RIVER", "GROVE", "ICE_SPIKES", "JAGGED_PEAKS",
        "JUNGLE", "LUKEWARM_OCEAN", "LUSH_CAVES", "MANGROVE_SWAMP",
        "MEADOW", "MUSHROOM_FIELDS", "-34", "OCEAN",
        "OLD_GROWTH_BIRCH_FOREST", "OLD_GROWTH_PINE_TAIGA", "OLD_GROWTH_SPRUCE_TAIGA", "PALE_GARDEN",
        "PLAINS", "RIVER", "SAVANNA", "SAVANNA_PLATEAU",
        "-44", "SNOWY_BEACH", "SNOWY_PLAINS", "SNOWY_SLOPES",
        "SNOWY_TAIGA", "-49", "SPARSE_JUNGLE", "STONY_PEAKS",
        "STONY_SHORE", "SUNFLOWER_PLAINS", "SWAMP", "TAIGA",
        "-56", "-57", "WARM_OCEAN", "-59",
        "WINDSWEPT_FOREST", "WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_HILLS", "WINDSWEPT_SAVANNA",
        "WOODED_BADLANDS"
    };

    static String biomeName(int id) {
        if (id >= 0 && id < BIOME_NAMES.length) return BIOME_NAMES[id];
        return "UNKNOWN_" + id;
    }

    public static void main(String[] args) throws Exception {
        BlockStateHelper.init();
        System.out.println("[诊断] 初始化完成");
        System.out.flush();

        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(12345L);

        // 测试多个坐标分布
        int[][] testChunks = {
            {0, 0},     // 世界原点
            {5, 0},    // X正方向
            {-5, 0},   // X负方向
            {0, 5},    // Z正方向
            {0, -5},   // Z负方向
            {10, 10}, {-10, -10}, {15, -15},
            {30, 0}, {-30, 0}, {0, 30}, {0, -30},
            {100, 100}, {-100, 100}, {100, -100}, {-100, -100}
        };

        var router = gen.getRouter();
        var dm = gen.getDensityMap();
        System.out.println("\n=== NOISE VALUE SCAN (without chunk generation) ===");
        // Scan for high-continentalness+low-erosion areas (mountain signatures)
        var contFunc = dm.get("minecraft:overworld/continents");
        var eroFunc = dm.get("minecraft:overworld/erosion");
        var ridgesFunc = dm.get("minecraft:overworld/ridges");
        var ridgesFoldedFunc = dm.get("minecraft:overworld/ridges_folded");
        var offsetSpline = dm.get("minecraft:overworld/offset");
        var factorSpline = dm.get("minecraft:overworld/factor");
        int foundMountains = 0;
        for (int bx = -1500; bx <= 1500 && foundMountains < 10; bx += 16) {
            for (int bz = -1500; bz <= 1500 && foundMountains < 10; bz += 16) {
                var ctx = new DensityFunction.SinglePointContext(bx, 64, bz);
                double c = contFunc.compute(ctx);
                double e = eroFunc.compute(ctx);
                if (c > 0.6 && e < -0.3) {
                    double r = ridgesFunc.compute(ctx);
                    double off = offsetSpline.compute(ctx);
                    double fac = factorSpline.compute(ctx);
                    double surfaceY = 128 * (1.5 + off) - 64;
                    System.out.printf("MOUNTAIN@(%d,%d): C=%.3f E=%.3f R=%.3f OFF=%.3f FAC=%.3f est_h=%.0f%n",
                        bx, bz, c, e, r, off, fac, surfaceY);
                    foundMountains++;
                }
            }
        }
        if (foundMountains == 0) System.out.println("No mountain regions found in a 3000x3000 block scan!");
        // Also check max ranges
        double cMin = 999, cMax = -999, eMin = 999, eMax = -999;
        for (int bx = -1500; bx <= 1500; bx += 32) {
            for (int bz = -1500; bz <= 1500; bz += 32) {
                var ctx = new DensityFunction.SinglePointContext(bx, 64, bz);
                double c = contFunc.compute(ctx);
                double e = eroFunc.compute(ctx);
                if (c < cMin) cMin = c;
                if (c > cMax) cMax = c;
                if (e < eMin) eMin = e;
                if (e > eMax) eMax = e;
            }
        }
        System.out.printf("Range: C=[%.3f, %.3f]  E=[%.3f, %.3f]%n", cMin, cMax, eMin, eMax);
        // Check for high OFFSET candidates
        double maxOff = -999;
        int maxOffBx = 0, maxOffBz = 0;
        for (int bx = -1500; bx <= 1500; bx += 32) {
            for (int bz = -1500; bz <= 1500; bz += 32) {
                var ctx = new DensityFunction.SinglePointContext(bx, 64, bz);
                double off = offsetSpline.compute(ctx);
                if (off > maxOff) { maxOff = off; maxOffBx = bx; maxOffBz = bz; }
            }
        }
        System.out.printf("MaxOFFSET=%.3f at (%d,%d), est_height=%.0f%n", maxOff, maxOffBx, maxOffBz, 128*(1.5+maxOff)-64);
        // Generate chunks across a wide grid to see terrain height distribution
        System.out.println("\n=== TERRAIN HEIGHT DISTRIBUTION ===");
        int[] gridChunks = {-64, -32, 0, 32, 64};
        int maxHeight = -999;
        String maxLoc = "";
        for (int gx : gridChunks) {
            for (int gz : gridChunks) {
                Chunk ch = gen.generate(gx, gz);
                int[] hm = ch.getHeightmapWorldSurface();
                int mn=999, mx=-999, su=0, co=0;
                for (int v : hm) { if (v > -64) { co++; su += v; mn = Math.min(mn, v); mx = Math.max(mx, v); } }
                if (mx > maxHeight) { maxHeight = mx; maxLoc = "("+gx+","+gz+")"; }
                System.out.printf("  CH(%d,%d) h=[%d,%.0f,%d]%n", gx, gz, mn, (double)su/co, mx);
            }
        }
        System.out.println("Max height in grid: " + maxHeight + " at " + maxLoc);
        // Also generate max OFFSET chunk for comparison
        int cxOff = maxOffBx / 16; if (maxOffBx < 0 && maxOffBx % 16 != 0) cxOff--;
        int czOff = maxOffBz / 16; if (maxOffBz < 0 && maxOffBz % 16 != 0) czOff--;
        System.out.println("Max OFFSET at chunk (" + cxOff + "," + czOff + "):");
        Chunk mountainChunk = gen.generate(cxOff, czOff);
        int[] hm2 = mountainChunk.getHeightmapWorldSurface();
        int mMin=999, mMax=-999, mSum=0, mCount=0;
        for (int v : hm2) { if (v > -64) { mCount++; mSum += v; mMin = Math.min(mMin, v); mMax = Math.max(mMax, v); } }
        System.out.printf("  h=[%d,%.0f,%d]%n", mMin, (double)mSum/mCount, mMax);

        System.out.println("\n[诊断] 所有块生成完成");
        System.out.flush();
        System.exit(0);
    }
}
