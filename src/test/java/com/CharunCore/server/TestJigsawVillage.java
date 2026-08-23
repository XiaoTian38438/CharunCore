package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.worldgen.structure2.*;
import com.CharunCore.server.worldgen.structure2.*;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

import java.util.List;

public class TestJigsawVillage {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BlockStateHelper.init();
        BlockManager.init();

        StructureTemplateManager mgr = StructureTemplateManager.getInstance();
        StructureTemplatePool pool = StructureTemplatePool.get("village/plains/town_centers");
        System.out.println("Pool size: " + pool.size());
        System.out.println("Fallback: " + pool.getFallbackPoolId());

        long seed = 12345L;
        int startX = 0, startY = 0, startZ = 0;
        int heightmapY = 70;

        System.out.println("Running JigsawPlacement.addPieces...");
        List<PoolElementStructurePiece> pieces = JigsawPlacement.addPieces(
            mgr, "village/plains/town_centers", 6,
            startX, startY, startZ, heightmapY, seed);

        if (pieces == null) {
            System.out.println("FAIL: addPieces returned null");
            return;
        }
        System.out.println("Pieces generated: " + pieces.size());

        for (int i = 0; i < Math.min(pieces.size(), 10); i++) {
            PoolElementStructurePiece p = pieces.get(i);
            BoundingBox bb = p.getBoundingBox();
            String elemName = "";
            if (p.getElement() instanceof SinglePoolElement spe) {
                elemName = spe.getTemplateId();
            }
            System.out.println("  piece " + i + ": " + elemName
                + " pos=(" + p.getPositionX() + "," + p.getPositionY() + "," + p.getPositionZ() + ")"
                + " rot=" + p.getRotation()
                + " bb=[" + bb.minX + "," + bb.minY + "," + bb.minZ + " - "
                + bb.maxX + "," + bb.maxY + "," + bb.maxZ + "]");
        }

        int uniqueTemplates = 0;
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (PoolElementStructurePiece p : pieces) {
            if (p.getElement() instanceof SinglePoolElement spe) {
                if (seen.add(spe.getTemplateId())) uniqueTemplates++;
            }
        }
        System.out.println("Unique templates: " + uniqueTemplates);

        if (pieces.size() > 1) {
            System.out.println("PASS: jigsaw engine produced " + pieces.size() + " pieces");
        } else {
            System.out.println("FAIL: only " + pieces.size() + " pieces (expected >1)");
        }
    }
}
