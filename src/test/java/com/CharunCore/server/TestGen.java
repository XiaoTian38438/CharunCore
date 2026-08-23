package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.worldgen.DensityRouterChunkGenerator;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

public class TestGen {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BlockStateHelper.init();
        BlockManager.init();

        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(12345L);
        
        long t0 = System.nanoTime();
        for (int cz = -1; cz <= 1; cz++) {
            for (int cx = -1; cx <= 1; cx++) {
                gen.generate(cx, cz);
            }
        }
        long t1 = System.nanoTime();
        System.out.println("9 chunks generated in " + ((t1 - t0) / 1_000_000) + "ms");
    }
}
