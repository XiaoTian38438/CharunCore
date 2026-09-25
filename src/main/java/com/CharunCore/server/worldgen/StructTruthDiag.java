package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.AnvilManager;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** 从原版区块 NBT structures.starts 提取结构位置真值，并与本核心的放置判定对比。 */
public final class StructTruthDiag {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;
        File owTruth = new File("C:/Users/tian_/Desktop/CharunCore/tmp-truth/world/region");

        Map<String, List<int[]>> vanilla = new TreeMap<>();
        int scanned = 0;
        for (int cx = -16; cx < 16; cx++) {
            for (int cz = -16; cz < 16; cz++) {
                NbtMap nbt = AnvilManager.loadChunkNbt(owTruth, cx, cz);
                if (nbt == null || !nbt.containsKey("structures")) continue;
                scanned++;
                NbtMap st = nbt.getCompound("structures");
                if (!st.containsKey("starts")) continue;
                NbtMap starts = st.getCompound("starts");
                for (String name : starts.keySet()) {
                    if (name.equals("minecraft:invalid")) continue;
                    NbtMap s = starts.getCompound(name);
                    if (!s.containsKey("id") || !s.containsKey("ChunkX")) continue;
                    String id = strip(s.getString("id"));
                    vanilla.computeIfAbsent(id, k -> new ArrayList<>())
                        .add(new int[]{s.getInt("ChunkX"), s.getInt("ChunkZ"), cx, cz});
                }
            }
        }
        System.out.println("scanned=" + scanned);
        System.out.println("vanilla starts:");
        for (Map.Entry<String, List<int[]>> e : vanilla.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (int[] b : e.getValue()) sb.append(String.format("(startC %d,%d | hitC %d,%d) ", b[0], b[1], b[2], b[3]));
            System.out.println("  " + e.getKey() + " x" + e.getValue().size() + ": " + sb);
        }
        // 与我们的候选对比：结构 id -> structure_set 名映射（同名近似）
        System.out.println();
        System.out.println("position match check (set candidates vs vanilla start chunks):");
        Map<String, String> setOfId = Map.ofEntries(
            Map.entry("village", "villages"), Map.entry("pillager_outpost", "pillager_outposts"),
            Map.entry("desert_pyramid", "desert_pyramids"), Map.entry("jungle_pyramid", "jungle_temples"),
            Map.entry("swamp_hut", "swamp_huts"), Map.entry("igloo", "igloos"),
            Map.entry("ocean_monument", "ocean_monuments"), Map.entry("woodland_mansion", "woodland_mansions"),
            Map.entry("shipwreck", "shipwrecks"), Map.entry("ocean_ruin", "ocean_ruins"),
            Map.entry("ruined_portal", "ruined_portals"), Map.entry("buried_treasure", "buried_treasures"),
            Map.entry("mineshaft", "mineshafts"), Map.entry("ancient_city", "ancient_cities"),
            Map.entry("trail_ruins", "trail_ruins"), Map.entry("trial_chambers", "trial_chambers"));
        for (Map.Entry<String, List<int[]>> e : vanilla.entrySet()) {
            String setId = setOfId.get(e.getKey());
            if (setId == null) continue;
            var set = com.CharunCore.server.worldgen.structure2.StructureSet.get(setId);
            int ok = 0, bad = 0;
            for (int[] b : e.getValue()) {
                boolean hit = set != null && set.getPlacement() != null
                    && set.getPlacement().isStructureChunk(seed, b[0], b[1]);
                if (hit) ok++; else { bad++; System.out.printf("  MISS %s at (%d,%d)%n", e.getKey(), b[0], b[1]); }
            }
            System.out.printf("  %s: match=%d miss=%d%n", setId, ok, bad);
        }

        // 我们的放置候选：对每个 random_spread 集合扫描同区域
        System.out.println();
        System.out.println("ours candidates in same region:");
        var sets = com.CharunCore.server.worldgen.structure2.StructureSet.loadAll();
        for (var entry : sets.entrySet()) {
            String setId = entry.getKey();
            var set = entry.getValue();
            if (set == null || set.getRings() != null || set.getPlacement() == null) continue;
            List<int[]> hits = new ArrayList<>();
            for (int cx = -16; cx < 16; cx++)
                for (int cz = -16; cz < 16; cz++)
                    if (set.getPlacement().isStructureChunk(seed, cx, cz))
                        hits.add(new int[]{cx, cz});
            if (!hits.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int[] h : hits) sb.append(String.format("(c%d,%d) ", h[0], h[1]));
                System.out.println("  " + setId + " x" + hits.size() + ": " + sb);
            }
        }
    }

    private static String strip(String s) {
        return s.startsWith("minecraft:") ? s.substring(10) : s;
    }
}
