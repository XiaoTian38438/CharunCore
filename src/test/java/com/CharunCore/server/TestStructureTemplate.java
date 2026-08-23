package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.worldgen.structure2.StructureTemplate;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

public class TestStructureTemplate {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BlockStateHelper.init();
        BlockManager.init();

        StructureTemplate t = StructureTemplate.load("igloo/top");
        if (t == null) { System.out.println("FAIL: igloo/top.nbt not loaded"); return; }
        System.out.println("igloo/top size: " + t.sizeX + "x" + t.sizeY + "x" + t.sizeZ);
        System.out.println("igloo/top blocks: " + t.blocks().size());
        System.out.println("igloo/top jigsaws: " + t.getJigsaws().size());
        for (var j : t.getJigsaws()) {
            System.out.println("  jigsaw at " + j.x() + "," + j.y() + "," + j.z()
                + " pool=" + j.pool() + " name=" + j.name() + " target=" + j.target());
        }

        StructureTemplate v = StructureTemplate.load("village/plains/houses/plains_small_house_1");
        if (v == null) { System.out.println("FAIL: village house not loaded"); return; }
        System.out.println("village plains_small_house_1 size: " + v.sizeX + "x" + v.sizeY + "x" + v.sizeZ);
        System.out.println("village plains_small_house_1 blocks: " + v.blocks().size());
        System.out.println("village plains_small_house_1 jigsaws: " + v.getJigsaws().size());

        int grass = 0, planks = 0, logs = 0, doors = 0;
        for (var b : v.blocks()) {
            String name = BlockStateHelper.getName(b.blockStateId());
            if (name == null) continue;
            if (name.equals("grass_block")) grass++;
            else if (name.contains("planks")) planks++;
            else if (name.contains("log")) logs++;
            else if (name.contains("door")) doors++;
        }
        System.out.println("  grass:" + grass + " planks:" + planks + " logs:" + logs + " doors:" + doors);
        System.out.println("PASS");
    }
}
