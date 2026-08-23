package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.worldgen.structure2.*;
import com.CharunCore.server.worldgen.structure2.*;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

import java.util.List;
import java.util.Map;

public class TestStructurePlacement {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BlockStateHelper.init();
        BlockManager.init();

        long seed = 12345L;
        System.out.println("Loading all structure sets...");
        Map<String, StructureSet> sets = StructureSet.loadAll();
        System.out.println("Loaded " + sets.size() + " structure sets");
        for (StructureSet set : sets.values()) {
            if (set == null || set.getPlacement() == null) continue;
            for (StructureSelectionEntry e : set.getStructures()) {
                StructureRegistry.ConfiguredStructure cs = StructureRegistry.get(e.structureId());
                if (cs == null) continue;
                System.out.println("  set entry: " + e.structureId()
                    + " weight=" + e.weight()
                    + " type=" + cs.type
                    + " startPool=" + cs.startPool
                    + " size=" + cs.size
                    + " spacing=" + set.getPlacement().getSpacing()
                    + " sep=" + set.getPlacement().getSeparation()
                    + " salt=" + set.getPlacement().getSalt());
            }
        }

        System.out.println("\nSearching for structure chunks in -10..10 range...");
        int found = 0;
        for (int cx = -10; cx <= 10; cx++) {
            for (int cz = -10; cz <= 10; cz++) {
                for (StructureSet set : sets.values()) {
                    if (set == null || set.getPlacement() == null) continue;
                    if (set.getPlacement().isStructureChunk(seed, cx, cz)) {
                        for (StructureSelectionEntry e : set.getStructures()) {
                            StructureRegistry.ConfiguredStructure cs = StructureRegistry.get(e.structureId());
                            if (cs == null || !cs.isJigsaw()) continue;
                            System.out.println("  FOUND: chunk(" + cx + "," + cz + ") -> " + e.structureId()
                                + " pool=" + cs.startPool);
                            found++;
                        }
                    }
                }
            }
        }
        System.out.println("\nTotal jigsaw structure chunks found: " + found);

        if (found > 0) {
            StructureManager2 mgr = StructureManager2.getInstance();
            System.out.println("\nGenerating structure at first found chunk...");
            int genCx = 0, genCz = 0;
            String genId = null;
            String genPool = null;
            int genSize = 6;
            outer:
            for (int cx = -10; cx <= 10; cx++) {
                for (int cz = -10; cz <= 10; cz++) {
                    for (StructureSet set : sets.values()) {
                        if (set == null || set.getPlacement() == null) continue;
                        if (set.getPlacement().isStructureChunk(seed, cx, cz)) {
                            for (StructureSelectionEntry e : set.getStructures()) {
                                StructureRegistry.ConfiguredStructure cs = StructureRegistry.get(e.structureId());
                                if (cs == null || !cs.isJigsaw() || cs.startPool == null) continue;
                                genCx = cx; genCz = cz; genId = e.structureId();
                                genPool = cs.startPool; genSize = cs.size;
                                break outer;
                            }
                        }
                    }
                }
            }

            if (genId != null) {
                int centerX = (genCx << 4) + 8;
                int centerZ = (genCz << 4) + 8;
                int heightY = 70;
                long structSeed = StructurePlacementMath.withLargeFeatureSeed(seed, genCx, genCz).nextLong();
                System.out.println("Generating: " + genId + " at chunk(" + genCx + "," + genCz + ")"
                    + " center=(" + centerX + "," + heightY + "," + centerZ + ")"
                    + " pool=" + genPool + " size=" + genSize);

                List<PoolElementStructurePiece> pieces = JigsawPlacement.addPieces(
                    mgr.getTemplateManager(), genPool, genSize,
                    centerX, heightY, centerZ, heightY, structSeed);

                if (pieces != null) {
                    System.out.println("Pieces: " + pieces.size());
                    BoundingBox allBB = null;
                    for (PoolElementStructurePiece p : pieces) {
                        BoundingBox bb = p.getBoundingBox();
                        if (allBB == null) {
                            allBB = bb;
                        } else {
                            allBB = new BoundingBox(
                                Math.min(allBB.minX, bb.minX), Math.min(allBB.minY, bb.minY), Math.min(allBB.minZ, bb.minZ),
                                Math.max(allBB.maxX, bb.maxX), Math.max(allBB.maxY, bb.maxY), Math.max(allBB.maxZ, bb.maxZ));
                        }
                    }
                    if (allBB != null) {
                        System.out.println("Total bounds: [" + allBB.minX + "," + allBB.minY + "," + allBB.minZ
                            + " - " + allBB.maxX + "," + allBB.maxY + "," + allBB.maxZ + "]");
                        System.out.println("Size: " + (allBB.maxX - allBB.minX + 1) + "x"
                            + (allBB.maxY - allBB.minY + 1) + "x"
                            + (allBB.maxZ - allBB.minZ + 1));
                    }
                    System.out.println("PASS");
                } else {
                    System.out.println("FAIL: no pieces generated");
                }
            }
        }
    }
}
