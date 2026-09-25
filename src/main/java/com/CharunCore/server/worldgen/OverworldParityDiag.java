package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.RegistryHelper;
import com.CharunCore.server.world.AnvilManager;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.chunk.Chunk;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 主世界同种子对比：① 群系（区块调色板）② 顶面高度与表层方块。 */
public final class OverworldParityDiag {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;
        File truth = new File("C:/Users/tian_/Desktop/CharunCore/tmp-truth/world/region");
        boolean baseOnly = args.length > 0 && args[0].equals("base");
        System.out.println("mode=" + (baseOnly ? "generateBaseOnly(no carvers/features)" : "full generate"));
        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(seed, DimensionType.OVERWORLD);

        int bioMatch = 0, bioDiff = 0, loaded = 0;
        int colTotal = 0, colTopMatch = 0, colBlockMatch = 0;
        Map<String, Integer> diffSamples = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        for (int cx = -12; cx < 12 && loaded < 400; cx += 2) {
            for (int cz = -12; cz < 12 && loaded < 400; cz += 2) {
                NbtMap nbt = AnvilManager.loadChunkNbt(truth, cx, cz);
                if (nbt == null || !nbt.containsKey("sections")) continue;
                String status = nbt.getString("Status");
                if (!status.equals("minecraft:full") && !status.equals("full")) continue;
                loaded++;

                // ── 群系：取 Y=4 区段调色板首个
                String vBiome = null;
                for (NbtMap sec : nbt.getList("sections", NbtType.COMPOUND)) {
                    if (sec.getByte("Y") != 4 || !sec.containsKey("biomes")) continue;
                    List<String> pal = sec.getCompound("biomes").getList("palette", NbtType.STRING);
                    if (!pal.isEmpty()) vBiome = strip(pal.get(0));
                }
                // ── 逐列顶面（最高非空气）与其下方表层
                int[][] vTop = new int[2][256];
                String[] vSurfName = new String[256];
                java.util.Arrays.fill(vTop[0], -1);
                java.util.Arrays.fill(vTop[1], -1);
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
                        if (names[v].equals("air")) continue;
                        int y = sy + (idx >> 8);
                        int ci = ((idx >> 4) & 15) * 16 + (idx & 15);
                        if (y >= vTop[0][ci]) {
                            vTop[1][ci] = vTop[0][ci];
                            vTop[0][ci] = y;
                            vSurfName[ci] = names[v];
                        } else if (y >= vTop[1][ci]) {
                            vTop[1][ci] = y;
                        }
                    }
                }

                Chunk ours = baseOnly ? gen.generateBaseOnly(cx, cz) : gen.generate(cx, cz);
                int minY = ours.getMinY(), maxY = minY + ours.getSectionCount() * 16 - 1;
                for (int lx = 0; lx < 16; lx++) {
                    for (int lz = 0; lz < 16; lz++) {
                        int oTop = -1, oSecond = -1;
                        for (int y = maxY; y >= minY; y--) {
                            int b = ours.getBlock(lx, y, lz);
                            if (b == 0) continue;
                            if (oTop < 0) oTop = y;
                            else if (oSecond < 0) { oSecond = y; break; }
                        }
                        int ci = lz * 16 + lx;
                        colTotal++;
                        if (oTop == vTop[0][ci]) colTopMatch++;
                        else {
                            String key = "dY=" + (oTop - vTop[0][ci]);
                            diffSamples.merge(key, 1, Integer::sum);
                            if (sb.length() < 900 && vTop[0][ci] >= 0)
                                sb.append(String.format("sample (%d,%d): vanilla=%d ours=%d%n",
                                    cx * 16 + lx, cz * 16 + lz, vTop[0][ci], oTop));
                        }
                        boolean blockOk = oTop == vTop[0][ci]
                            && sameBlock(ours.getBlock(lx, oTop, lz), vSurfName[ci]);
                        if (blockOk) colBlockMatch++;
                    }
                }

                // 群系对比
                if (vBiome != null) {
                    int oursBio = gen.getColumnBiome(cx * 16 + 8, cz * 16 + 8);
                    String oursName = strip(RegistryHelper.biomeIdToName(oursBio));
                    boolean ok = oursName.equals(vBiome);
                    if (ok) bioMatch++;
                    else {
                        bioDiff++;
                        if (bioDiff <= 20) sb.append(String.format(
                            "biome DIFF chunk(%d,%d) vanilla=%s ours=%s%n", cx, cz, vBiome, oursName));
                    }
                }
            }
        }
        System.out.print(sb);
        System.out.printf("loaded=%d biomes: match=%d diff=%d (%.1f%%)%n",
            loaded, bioMatch, bioDiff, 100.0 * bioMatch / Math.max(1, bioMatch + bioDiff));
        System.out.printf("columns: total=%d topY-match=%.2f%% surfBlock-match=%.2f%% diffs=%s%n",
            colTotal, 100.0 * colTopMatch / Math.max(1, colTotal),
            100.0 * colBlockMatch / Math.max(1, colTotal), diffSamples);
    }

    private static boolean sameBlock(int state, String vanillaName) {
        if (vanillaName == null) return state == 0;
        String n = BlockStateHelper.getName(state);
        if (n == null) return false;
        return strip(n).equals(vanillaName);
    }

    private static String strip(String s) {
        return s.startsWith("minecraft:") ? s.substring(10) : s;
    }
}
