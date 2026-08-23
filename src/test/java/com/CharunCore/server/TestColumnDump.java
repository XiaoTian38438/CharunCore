package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.DensityRouterChunkGenerator;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

public class TestColumnDump {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BlockStateHelper.init();
        BlockManager.init();
        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(12345L);
        Chunk chunk = gen.generate(0, 0);
        int lx = 8, lz = 8;
        System.out.println("=== chunk(0,0) column (lx=8,lz=8) ===");
        for (int y = 70; y >= 20; y--) {
            int b = chunk.getBlock(lx, y, lz);
            int biome2d = chunk.getBiome(lx, lz);
            int biome3d = chunk.getBiome(lx, y, lz);
            System.out.println("  y=" + y + " block=" + BlockStateHelper.getName(b) + " biome2d=" + biome2d + " biome3d=" + biome3d);
        }
    }
}
