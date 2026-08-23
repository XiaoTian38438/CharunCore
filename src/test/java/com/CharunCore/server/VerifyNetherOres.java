package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.DensityRouterChunkGenerator;
import com.CharunCore.server.world.DimensionType;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

public class VerifyNetherOres {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BlockStateHelper.init();
        BlockManager.init();

        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(12345L, DimensionType.THE_NETHER);
        int ad = 0, ngo = 0, nqo = 0, copperWrong = 0;
        int chunks = 0;
        for (int cx = -4; cx <= 4; cx++) {
            for (int cz = -4; cz <= 4; cz++) {
                Chunk c = gen.generate(cx, cz);
                chunks++;
                int yMin = c.getMinY();
                int yMax = yMin + c.getSectionCount() * 16;
                for (int y = yMin; y < yMax; y++) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            int id = c.getBlock(x, y, z);
                            if (id == 0) continue;
                            String name = BlockStateHelper.getName(id);
                            if ("ancient_debris".equals(name)) ad++;
                            else if ("nether_gold_ore".equals(name)) ngo++;
                            else if ("nether_quartz_ore".equals(name)) nqo++;
                            else if ("copper_ore".equals(name) || "granite".equals(name)
                                  || "deepslate_iron_ore".equals(name) || "tuff".equals(name)) copperWrong++;
                        }
                    }
                }
            }
        }
        System.out.println("Nether ore scan over " + chunks + " chunks (seed=12345):");
        System.out.println("  ancient_debris     = " + ad);
        System.out.println("  nether_gold_ore    = " + ngo);
        System.out.println("  nether_quartz_ore  = " + nqo);
        System.out.println("  WRONG overworld ore in nether (copper/granite/iron/tuff) = " + copperWrong);
    }
}
