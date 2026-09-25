package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.AnvilManager;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.DimensionType;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.File;
import java.util.List;

/**
 * 同种子(seed=1234567)末地逐区块对比：vanilla 真值 vs 本核心。
 * 输出每列 top-Y 差异统计与首个失配点的密度成分拆解。
 */
public final class EndParityDiag {
    public static void main(String[] args) throws Exception {
        BlockStateHelper.init();
        long seed = 1234567L;
        File truthRegion = new File("C:/Users/tian_/Desktop/CharunCore/tmp-truth/world/DIM1/region");
        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(seed, DimensionType.THE_END);
        int endStone = BlockStateHelper.getDefault("end_stone");

        int matchCols = 0, diffCols = 0, voidMatch = 0;
        int printed = 0;
        StringBuilder sample = new StringBuilder();
        for (int gcx = -16; gcx < 16 && printed < 6; gcx++) {
            for (int gcz = -16; gcz < 16; gcz++) {
                NbtMap nbt = AnvilManager.loadChunkNbt(truthRegion, gcx, gcz);
                if (nbt == null || !nbt.containsKey("sections")) continue;
                String status = nbt.getString("Status");
                if (!status.equals("minecraft:full") && !status.equals("full")) continue;

                int[] vTop = new int[256];
                java.util.Arrays.fill(vTop, -1);
                for (NbtMap sec : nbt.getList("sections", NbtType.COMPOUND)) {
                    NbtMap bs = sec.getCompound("block_states");
                    List<NbtMap> palette = bs.getList("palette", NbtType.COMPOUND);
                    long[] data = bs.getLongArray("data");
                    if (data.length == 0 || palette.size() == 1) continue;
                    boolean hasES = false;
                    for (NbtMap p : palette) if (p.getString("Name").endsWith("end_stone")) hasES = true;
                    if (!hasES) continue;
                    int sy = sec.getByte("Y") * 16;
                    int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
                    int vpl = 64 / bits;
                    for (int idx = 0; idx < 4096; idx++) {
                        long l = data[idx / vpl];
                        int v = (int) ((l >>> ((idx % vpl) * bits)) & ((1L << bits) - 1));
                        if (v >= palette.size()) continue;
                        if (!palette.get(v).getString("Name").endsWith("end_stone")) continue;
                        int y = sy + (idx >> 8);
                        int ci = (idx >> 4) & 15;
                        if (y > vTop[ci]) vTop[ci] = y;
                    }
                }

                Chunk ours = gen.generateBaseOnly(gcx, gcz);
                int minY = ours.getMinY(), maxY = minY + ours.getSectionCount() * 16 - 1;
                for (int lx = 0; lx < 16; lx++) {
                    for (int lz = 0; lz < 16; lz++) {
                        int oTop = -1;
                        for (int y = maxY; y >= minY; y--) {
                            if (ours.getBlock(lx, y, lz) == endStone) { oTop = y; break; }
                        }
                        int vT = vTop[lz * 16 + lx];
                        if (vT == oTop) { matchCols++; if (vT < 0) voidMatch++; }
                        else {
                            diffCols++;
                            if (printed < 6) {
                                printed++;
                                int bx = gcx * 16 + lx, bz = gcz * 16 + lz;
                                double isl = gen.getRouter().erosion().compute(
                                    new com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext(bx, 57, bz));
                                sample.append(String.format(
                                    "MISMATCH chunk(%d,%d) col(%d,%d) block(%d,%d): vanilla=%d ours=%d islandsFn=%.4f%n",
                                    gcx, gcz, lx, lz, bx, bz, vT, oTop, isl));
                            }
                        }
                    }
                }
            }
        }
        System.out.println(sample);
        System.out.printf("columns: match=%d (void-match=%d) diff=%d (%.2f%%)%n",
            matchCols, voidMatch, diffCols, 100.0 * diffCols / Math.max(1, matchCols + diffCols));
    }
}
