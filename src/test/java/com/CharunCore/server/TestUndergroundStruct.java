package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.worldgen.structure2.StructureRegistry;
import com.CharunCore.server.worldgen.structure2.StructureSelectionEntry;
import com.CharunCore.server.worldgen.structure2.StructureSet;
import com.CharunCore.server.worldgen.structure2.*;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

import java.util.Map;

public class TestUndergroundStruct {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BlockStateHelper.init();
        BlockManager.init();

        long seed = 12345L;
        Map<String, StructureSet> sets = StructureSet.loadAll();
        StructureRegistry.loadAll();

        System.out.println("=== Structure start_height configs ===");
        for (String id : StructureRegistry.loadAll().keySet()) {
            StructureRegistry.ConfiguredStructure cs = StructureRegistry.get(id);
            if (cs == null || !cs.isJigsaw()) continue;
            System.out.println(id + ": type=" + cs.startHeightType
                + " min=" + cs.startHeightMin + " max=" + cs.startHeightMax
                + " heightmap=" + cs.projectStartToHeightmap
                + " step=" + cs.step);
        }

        System.out.println("\n=== Searching for trial_chambers and ancient_city chunks ===");
        for (int cx = -20; cx <= 20; cx++) {
            for (int cz = -20; cz <= 20; cz++) {
                for (StructureSet set : sets.values()) {
                    if (set == null || set.getPlacement() == null) continue;
                    if (!set.getPlacement().isStructureChunk(seed, cx, cz)) continue;
                    for (StructureSelectionEntry e : set.getStructures()) {
                        StructureRegistry.ConfiguredStructure cs = StructureRegistry.get(e.structureId());
                        if (cs == null || !cs.isJigsaw()) continue;
                        if (!cs.id.equals("trial_chambers") && !cs.id.equals("ancient_city")
                            && !cs.id.equals("trail_ruins") && !cs.id.equals("bastion_remnant")) continue;

                        int surfaceY = 70;
                        int heightY;
                        if (cs.projectStartToHeightmap) {
                            heightY = surfaceY + cs.startHeightMin;
                        } else if ("uniform".equals(cs.startHeightType)) {
                            int range = cs.startHeightMax - cs.startHeightMin + 1;
                            heightY = cs.startHeightMin + (range > 0 ? new java.util.Random(seed ^ (cx*341873128712L + cz*132897987541L)).nextInt(range) : 0);
                        } else {
                            heightY = cs.startHeightMin;
                        }
                        System.out.println("  chunk(" + cx + "," + cz + ") -> " + cs.id
                            + " startY=" + heightY
                            + " (surface=" + surfaceY + ")"
                            + (heightY < 0 ? " [UNDERGROUND]" : " [SURFACE]"));
                    }
                }
            }
        }
        System.out.println("DONE");
    }
}
