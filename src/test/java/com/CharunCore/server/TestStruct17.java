package com.CharunCore.server;

import com.CharunCore.server.worldgen.structure2.BiomeTagResolver;
import com.CharunCore.server.worldgen.structure2.RandomSpreadStructurePlacement;
import com.CharunCore.server.worldgen.structure2.StructureSelectionEntry;
import com.CharunCore.server.worldgen.structure2.StructureSet;

import java.util.Map;
import java.util.Set;

public class TestStruct17 {
    public static void main(String[] args) {
        Map<String, StructureSet> sets = StructureSet.loadAll();
        System.out.println("structure sets loaded: " + sets.size());
        for (String name : new String[]{"nether_complexes", "end_cities", "trial_chambers"}) {
            StructureSet s = sets.get(name);
            if (s == null) { System.out.println("MISSING SET " + name); continue; }
            RandomSpreadStructurePlacement p = s.getPlacement();
            System.out.println("set=" + name + " spacing=" + p.getSpacing()
                + " sep=" + p.getSeparation() + " salt=" + p.getSalt()
                + " structures=" + s.getStructures().size());
            for (StructureSelectionEntry e : s.getStructures()) {
                var cs = com.CharunCore.server.worldgen.structure2.StructureRegistry.get(e.structureId());
                Set<Integer> biomes = cs == null ? Set.of() : BiomeTagResolver.getBiomesForStructure(cs.biomesTag);
                System.out.println("  -> " + e.structureId() + " type=" + (cs == null ? "?" : cs.type)
                    + " jigsaw=" + (cs != null && cs.isJigsaw()) + " biomesTag=" + (cs == null ? "?" : cs.biomesTag)
                    + " resolvedBiomeIds=" + biomes);
            }
            // 找前几个会触发的区块
            long seed = 1234567L;
            int found = 0;
            for (int cx = -400; found < 3 && cx <= 400; cx += 1) {
                for (int cz = -400; found < 3 && cz <= 400; cz += 1) {
                    if (p.isStructureChunk(seed, cx, cz)) {
                        int bx = cx << 4, bz = cz << 4;
                        long distSq = (long) bx * bx + (long) bz * bz;
                        System.out.println("   trigger chunk (" + cx + "," + cz + ") blockDist^2=" + distSq);
                        found++;
                    }
                }
            }
        }
    }
}
