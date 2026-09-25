package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.AnvilManager;
import com.CharunCore.server.world.DimensionType;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.File;
import java.util.List;

public final class BiomeParityDiag {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;
        File truth = new File("C:/Users/tian_/Desktop/CharunCore/tmp-truth/world/DIM-1/region");
        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(seed, DimensionType.THE_NETHER);

        int loaded = 0, match = 0, diff = 0, printed = 0;
        for (int cx = -10; cx < 10; cx += 2) {
            for (int cz = -10; cz < 10; cz += 2) {
                NbtMap nbt = AnvilManager.loadChunkNbt(truth, cx, cz);
                if (nbt == null) continue;
                loaded++;
                String vName = null;
                List<NbtMap> sections = nbt.getList("sections", NbtType.COMPOUND);
                for (NbtMap sec : sections) {
                    if (!sec.containsKey("biomes")) continue;
                    NbtMap bio = sec.getCompound("biomes");
                    List<String> pal = bio.getList("palette", NbtType.STRING);
                    if (pal.isEmpty()) continue;
                    if (vName == null) vName = pal.get(0);
                }
                if (vName == null) continue;
                int ours = gen.getColumnBiome(cx * 16 + 8, cz * 16 + 8);
                String oursName = biomeName(ours);
                boolean ok = vName.endsWith(oursName);
                if (ok) match++; else diff++;
                if (printed++ < 25)
                    System.out.printf("chunk(%3d,%3d) vanilla=%-22s ours=%-18s %s%n",
                        cx, cz, vName, oursName, ok ? "OK" : "DIFF");
            }
        }
        System.out.printf("%nloaded=%d match=%d diff=%d%n", loaded, match, diff);
    }

    private static String biomeName(int id) {
        return switch (id) {
            case 34 -> "nether_wastes"; case 49 -> "soul_sand_valley";
            case 7 -> "crimson_forest"; case 59 -> "warped_forest";
            case 2 -> "basalt_deltas"; default -> "?" + id;
        };
    }
}
