package com.CharunCore.server.worldgen;

import com.CharunCore.server.world.AnvilManager;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 从真实原版 1.21.11 服务端存档(Paper)提取末地/下界地形统计，作为对齐基准。
 * 只读。末地：外岛顶面高度分布；下界：方块直方图 + 熔岩海覆盖。
 */
public final class VanillaTruthDiag {

    public static void main(String[] args) {
        String base = args.length > 0 ? args[0] : "C:/Users/tian_/Desktop/1.21.11PAPERSERVER/world_the_end/DIM1/region";
        File endRegion = new File(base);
        File netherRegion = args.length > 1 ? new File(args[1]) : new File("C:/Users/tian_/Desktop/1.21.11PAPERSERVER/world_nether/DIM-1/region");

        System.out.println("=== VANILLA END truth ===");
        scanEnd(endRegion);
        System.out.println();
        System.out.println("=== VANILLA NETHER truth ===");
        scanNether(netherRegion);
    }

    private static void scanEnd(File regionDir) {
        Map<Integer, Integer> biomeTopBuckets = new TreeMap<>();
        int voidChunks = 0, landChunks = 0;
        int minTop = Integer.MAX_VALUE, maxTop = Integer.MIN_VALUE;
        for (int gcx = -64; gcx < 64; gcx++) {
            for (int gcz = -64; gcz < 64; gcz++) {
                NbtMap nbt = AnvilManager.loadChunkNbt(regionDir, gcx, gcz);
                if (nbt == null || !nbt.containsKey("sections")) continue;
                String status = nbt.containsKey("Status") ? nbt.getString("Status") : "full";
                if (!status.equals("minecraft:full") && !status.equals("full")) continue;
                int[] colTop = new int[256];
                java.util.Arrays.fill(colTop, -1);
                List<NbtMap> sections = nbt.getList("sections", NbtType.COMPOUND);
                for (NbtMap sec : sections) {
                    NbtMap bs = sec.getCompound("block_states");
                    List<NbtMap> palette = bs.getList("palette", NbtType.COMPOUND);
                    long[] data = bs.getLongArray("data");
                    if (data.length == 0) continue;
                    boolean hasEndStone = false;
                    for (NbtMap p : palette) if (p.getString("Name").endsWith("end_stone")) hasEndStone = true;
                    if (!hasEndStone) continue;
                    int sy = sec.getByte("Y") * 16;
                    int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(Math.max(1, palette.size() - 1)));
                    int vpl = 64 / bits;
                    for (int idx = 0; idx < 4096; idx++) {
                        long l = data[idx / vpl];
                        int v = (int) ((l >>> ((idx % vpl) * bits)) & ((1L << bits) - 1));
                        if (v >= palette.size()) continue;
                        if (!palette.get(v).getString("Name").endsWith("end_stone")) continue;
                        int y = sy + (idx >> 8);          // YZX: idx = y*256 + z*16 + x
                        int lx = idx & 15, lz = (idx >> 4) & 15;
                        if (y > colTop[lz * 16 + lx]) colTop[lz * 16 + lx] = y;
                    }
                }
                int cols = 0, sum = 0, mn = Integer.MAX_VALUE, mx = Integer.MIN_VALUE;
                for (int t : colTop) if (t >= 0) { cols++; sum += t; mn = Math.min(mn, t); mx = Math.max(mx, t); }
                double dist = Math.sqrt((double) gcx * gcx + (double) gcz * gcz);
                if (cols == 0) {
                    voidChunks++;
                } else {
                    landChunks++;
                    minTop = Math.min(minTop, mn); maxTop = Math.max(maxTop, mx);
                    int bucket = dist < 8 ? 8 : dist < 32 ? 32 : dist < 64 ? 64 : dist < 96 ? 96 : dist < 160 ? 160 : 999;
                    biomeTopBuckets.merge(bucket, cols, Integer::sum);
                    if (dist > 68 && dist < 400 && (landChunks % 40 == 0)) {
                        System.out.printf("  chunk(%5d,%5d) d=%5.0f landCols=%3d top=[%d..%d]%n",
                            gcx, gcz, dist, cols, mn, mx);
                    }
                }
            }
        }
        System.out.println("land chunks=" + landChunks + " void chunks=" + voidChunks
            + " island top range=" + (minTop == Integer.MAX_VALUE ? "-" : minTop + ".." + maxTop));
        System.out.println("land columns by distance bucket: " + biomeTopBuckets);
    }

    private static void scanNether(File regionDir) {
        Map<String, Long> hist = new TreeMap<>();
        int solidBelow32Full = 0, totalCols = 0;
        int floorMin = 128, ceilMax = 0;
        for (int gcx = -24; gcx < 24; gcx++) {
            for (int gcz = -24; gcz < 24; gcz++) {
                NbtMap nbt = AnvilManager.loadChunkNbt(regionDir, gcx, gcz);
                if (nbt == null || !nbt.containsKey("sections")) continue;
                String status = nbt.containsKey("Status") ? nbt.getString("Status") : "full";
                if (!status.equals("minecraft:full") && !status.equals("full")) continue;
                // 直方图按 palette 权重近似（不解包 data，仅统计 palette 出现次数 * 该节非空概率）
                List<NbtMap> sections = nbt.getList("sections", NbtType.COMPOUND);
                int[][] compact = new int[16][16];   // floor/ceil
                for (int i = 0; i < 256; i++) { compact[i / 16][i % 16] = -1; }
                for (NbtMap sec : sections) {
                    NbtMap bs = sec.getCompound("block_states");
                    List<NbtMap> palette = bs.getList("palette", NbtType.COMPOUND);
                    long[] data = bs.getLongArray("data");
                    int sy = sec.getByte("Y") * 16;
                    if (palette.size() == 1) {
                        String n = simple(palette.get(0).getString("Name"));
                        if (!n.equals("air")) hist.merge(n, 4096L, Long::sum);
                        continue;
                    }
                    if (data.length == 0) continue;
                    int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
                    int vpl = 64 / bits;
                    long[] counts = new long[palette.size()];
                    for (int idx = 0; idx < 4096; idx++) {
                        long l = data[idx / vpl];
                        int v = (int) ((l >>> ((idx % vpl) * bits)) & ((1L << bits) - 1));
                        counts[v]++;
                        if (v < palette.size() && !simple(palette.get(v).getString("Name")).equals("air")) {
                            int y = sy + (idx >> 8), lx = idx & 15, lz = (idx >> 4) & 15;
                            if (compact[lz][lx] < 0) compact[lz][lx] = y;
                        }
                    }
                    for (int p = 0; p < palette.size(); p++)
                        if (counts[p] > 0) hist.merge(simple(palette.get(p).getString("Name")), counts[p], Long::sum);
                }
                for (int lz = 0; lz < 16; lz++) {
                    for (int lx = 0; lx < 16; lx++) {
                        int floor = compact[lz][lx];
                        if (floor < 0) continue;
                        totalCols++;
                        floorMin = Math.min(floorMin, floor);
                        // ceiling: 找该列最高非空气
                    }
                }
            }
        }
        System.out.println("floor min over sampled columns: y=" + floorMin + ", cols=" + totalCols);
        System.out.println("nether block histogram (top 20):");
        hist.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
            .limit(20)
            .forEach(e -> System.out.printf("  %-22s %,14d%n", e.getKey(), e.getValue()));
    }

    private static String simple(String name) {
        return name.startsWith("minecraft:") ? name.substring(10) : name;
    }
}
