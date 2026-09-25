package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.AnvilManager;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.chunk.Chunk;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 洞穴系统同种子逐区块对比：地下空洞体积 + 深度分布 + 水/熔岩填充。 */
public final class CaveParityDiag {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;
        File truth = new File("C:/Users/tian_/Desktop/CharunCore/tmp-truth/world/region");
        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(seed, DimensionType.OVERWORLD);

        long vCave = 0, oCave = 0, vWater = 0, oWater = 0, vLava = 0, oLava = 0;
        long[] vDepth = new long[5], oDepth = new long[5];
        int chunks = 0, matchCaveChunks = 0;
        StringBuilder sb = new StringBuilder();

        for (int cx = -8; cx < 8; cx++) {
            for (int cz = -8; cz < 8; cz++) {
                NbtMap nbt = AnvilManager.loadChunkNbt(truth, cx, cz);
                if (nbt == null || !nbt.containsKey("sections")) continue;
                String status = nbt.getString("Status");
                if (!status.equals("minecraft:full") && !status.equals("full")) continue;
                chunks++;

                long[] vStat = caveStatsFromNbt(nbt);
                Chunk ours = gen.generate(cx, cz);
                long[] oStat = caveStatsFromChunk(ours);
                vCave += vStat[0]; oCave += oStat[0];
                vWater += vStat[1]; oWater += oStat[1];
                vLava += vStat[2]; oLava += oStat[2];
                for (int d = 0; d < 5; d++) { vDepth[d] += vStat[3 + d]; oDepth[d] += oStat[3 + d]; }
                if (Math.abs(vStat[0] - oStat[0]) < 200) matchCaveChunks++;
                if (chunks <= 6) sb.append(String.format(
                    "chunk(%-3d,%-3d) caveV=%6d/%6d water=%5d/%5d lava=%3d/%3d%n",
                    cx, cz, vStat[0], oStat[0], vStat[1], oStat[1], vStat[2], oStat[2]));
            }
        }
        System.out.print(sb);
        System.out.printf("chunks=%d caveVolume: vanilla=%d ours=%d (%.0f%%)%n", chunks, vCave, oCave,
            100.0 * oCave / Math.max(1, vCave));
        System.out.printf("waterBelow: vanilla=%d ours=%d | lava: vanilla=%d ours=%d%n", vWater, oWater, vLava, oLava);
        System.out.println("depth bands (y): vanilla vs ours");
        String[] bands = {"<-54", "-54..-1", "0..31", "32..63", "64+"};
        for (int d = 0; d < 5; d++) {
            System.out.printf("  %-8s %9d %9d%n", bands[d], vDepth[d], oDepth[d]);
        }
        System.out.println("chunks with |caveDiff|<200: " + matchCaveChunks + "/" + chunks);
    }

    private static final int CAVE_AIR = BlockStateHelper.getDefault("cave_air");
    private static final int AIR = BlockStateHelper.getDefault("air");

    /** 返回 [空洞体积, 水体积, 熔岩体积, 5 档深度分布] */
    static long[] caveStatsFromNbt(NbtMap nbt) {
        long[] out = new long[8];
        int[] topSolid = new int[256];
        java.util.Arrays.fill(topSolid, -64);
        for (NbtMap sec : nbt.getList("sections", NbtType.COMPOUND)) {
            NbtMap bs = sec.getCompound("block_states");
            List<NbtMap> palette = bs.getList("palette", NbtType.COMPOUND);
            long[] data = bs.getLongArray("data");
            if (data.length == 0 || palette.size() <= 1) continue;
            int sy = sec.getByte("Y") * 16;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            int vpl = 64 / bits;
            String[] names = new String[palette.size()];
            for (int p = 0; p < palette.size(); p++) names[p] = strip(palette.get(p).getString("Name"));
            for (int idx = 0; idx < 4096; idx++) {
                long l = data[idx / vpl];
                int v = (int) ((l >>> ((idx % vpl) * bits)) & ((1L << bits) - 1));
                if (v >= names.length) continue;
                String n = names[v];
                int y = sy + (idx >> 8);
                int ci = ((idx >> 4) & 15) * 16 + (idx & 15);
                if (!n.equals("air") && !n.equals("cave_air")) {
                    if (y > topSolid[ci]) topSolid[ci] = y;
                } else {
                    out[0]++;
                    int band = y < -54 ? 0 : y < 0 ? 1 : y < 32 ? 2 : y < 64 ? 3 : 4;
                    out[3 + band]++;
                }
                if (n.equals("water")) out[1]++;
                if (n.equals("lava")) out[2]++;
            }
        }
        // 减去海洋水体：把无固体支撑的整列空气（水面以上）与水下空气分开
        // 简化：只统计低于该列最高固体 2 格以下的空气为"洞穴"
        for (int ci = 0; ci < 256; ci++) {
            int top = topSolid[ci];
            if (top <= -64) continue;
            // 重新扫描剔除非洞穴空气（把空气行数据恢复到 out）——此处用二次扫描
        }
        return recount(nbt, out, topSolid);
    }

    private static long[] recount(NbtMap nbt, long[] out, int[] topSolid) {
        // 直接按列二次统计：低于 topSolid-1 的空气才算洞穴空气
        long[] r = new long[8];
        int[][] solid = new int[256][];
        for (int ci = 0; ci < 256; ci++) solid[ci] = null;
        for (NbtMap sec : nbt.getList("sections", NbtType.COMPOUND)) {
            NbtMap bs = sec.getCompound("block_states");
            List<NbtMap> palette = bs.getList("palette", NbtType.COMPOUND);
            long[] data = bs.getLongArray("data");
            if (data.length == 0 || palette.size() <= 1) continue;
            int sy = sec.getByte("Y") * 16;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            int vpl = 64 / bits;
            String[] names = new String[palette.size()];
            for (int p = 0; p < palette.size(); p++) names[p] = strip(palette.get(p).getString("Name"));
            for (int idx = 0; idx < 4096; idx++) {
                long l = data[idx / vpl];
                int v = (int) ((l >>> ((idx % vpl) * bits)) & ((1L << bits) - 1));
                if (v >= names.length) continue;
                String n = names[v];
                int y = sy + (idx >> 8);
                int ci = ((idx >> 4) & 15) * 16 + (idx & 15);
                int top = topSolid[ci];
                if (top <= -64) continue;
                if ((n.equals("air") || n.equals("cave_air")) && y <= top - 1) {
                    int band = y < -54 ? 0 : y < 0 ? 1 : y < 32 ? 2 : y < 64 ? 3 : 4;
                    r[0]++; r[3 + band]++;
                }
                if (n.equals("water") && y < 63) r[1]++;
                if (n.equals("lava")) r[2]++;
            }
        }
        return r;
    }

    /** 项目侧统计：列顶面之上是地表，顶面以下的空气为洞穴；水位以下的水为水体。 */
    static long[] caveStatsFromChunk(Chunk chunk) {
        long[] r = new long[8];
        int minY = chunk.getMinY();
        int maxY = minY + chunk.getSectionCount() * 16 - 1;
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int top = minY - 1;
                for (int y = maxY; y >= minY; y--) {
                    if (chunk.getBlock(lx, y, lz) != 0) { top = y; break; }
                }
                if (top < minY) continue;
                boolean sawWater = false;
                for (int y = top - 1; y >= minY; y--) {
                    int b = chunk.getBlock(lx, y, lz);
                    if (b == 0) {
                        int band = y < -54 ? 0 : y < 0 ? 1 : y < 32 ? 2 : y < 64 ? 3 : 4;
                        r[0]++; r[3 + band]++;
                    } else {
                        String n = shortName(BlockStateHelper.getName(b));
                        if (n.equals("water")) { r[1]++; sawWater = true; }
                        if (n.equals("lava")) r[2]++;
                    }
                }
                // 水位列：把海洋水体算作 water 而非洞穴（已在循环内按 water 计数，air 不会在海底以下出现）
            }
        }
        return r;
    }

    private static String strip(String s) { return s.startsWith("minecraft:") ? s.substring(10) : s; }
    private static String shortName(String n) { return n == null ? "" : (n.startsWith("minecraft:") ? n.substring(10) : n); }
}