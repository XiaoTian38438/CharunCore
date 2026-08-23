package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.worldgen.structure2.*;
import com.CharunCore.server.worldgen.structure2.*;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

import java.util.List;

public class TestJigsawDebug2 {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BlockStateHelper.init();
        BlockManager.init();

        StructureTemplateManager mgr = StructureTemplateManager.getInstance();

        StructureTemplate root = mgr.getOrCreate("village/plains/town_centers/plains_meeting_point_2");
        System.out.println("Root template size: " + root.sizeX + "x" + root.sizeY + "x" + root.sizeZ);
        List<JigsawBlockInfo> jigsaws = root.getJigsaws();
        System.out.println("Root jigsaw blocks: " + jigsaws.size());
        for (JigsawBlockInfo j : jigsaws) {
            System.out.println("  jigsaw at " + j.x() + "," + j.y() + "," + j.z()
                + " pool=" + j.pool()
                + " name=" + j.name()
                + " target=" + j.target()
                + " front=" + j.frontFacing()
                + " top=" + j.topFacing()
                + " rollable=" + j.rollable());
        }

        if (!jigsaws.isEmpty()) {
            JigsawBlockInfo first = jigsaws.get(0);
            String poolId = first.pool();
            System.out.println("\nLoading pool: " + poolId);
            StructureTemplatePool childPool = StructureTemplatePool.get(poolId);
            System.out.println("Pool size: " + childPool.size());

            if (childPool.size() > 0) {
                StructurePoolElement elem = childPool.getTemplates().get(0);
                if (elem instanceof SinglePoolElement spe) {
                    System.out.println("First child template: " + spe.getTemplateId());
                    StructureTemplate childT = mgr.getOrCreate(spe.getTemplateId());
                    if (childT != null) {
                        System.out.println("Child template size: " + childT.sizeX + "x" + childT.sizeY + "x" + childT.sizeZ);
                        List<JigsawBlockInfo> childJigsaws = childT.getJigsaws();
                        System.out.println("Child jigsaw blocks: " + childJigsaws.size());
                        for (JigsawBlockInfo cj : childJigsaws) {
                            System.out.println("  child jigsaw at " + cj.x() + "," + cj.y() + "," + cj.z()
                                + " pool=" + cj.pool()
                                + " name=" + cj.name()
                                + " target=" + cj.target()
                                + " front=" + cj.frontFacing()
                                + " top=" + cj.topFacing()
                                + " rollable=" + cj.rollable());

                            System.out.println("  canAttach(parent=" + first.name() + " target=" + first.target()
                                + " front=" + first.frontFacing()
                                + ", child=" + cj.name() + " front=" + cj.frontFacing()
                                + "): " + JigsawBlockInfo.canAttach(first, cj));
                        }
                    }
                }
            }
        }
    }
}
