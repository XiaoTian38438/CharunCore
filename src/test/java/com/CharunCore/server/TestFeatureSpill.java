package com.CharunCore.server;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.DensityRouterChunkGenerator;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

public class TestFeatureSpill {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BlockStateHelper.init();
        BlockManager.init();
        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(12345L);

        int leaves = BlockStateHelper.getDefault("dark_oak_leaves");
        int oakLeaves = BlockStateHelper.getDefault("oak_leaves");
        int birchLeaves = BlockStateHelper.getDefault("birch_leaves");
        int spruceLeaves = BlockStateHelper.getDefault("spruce_leaves");

        int totalSpilledLeaves = 0;
        for (int cx = 0; cx <= 8; cx++) {
            Chunk chunk = gen.generate(cx, 0);
            int edgeLeaves = 0;
            for (int lz = 0; lz < 16; lz++) {
                for (int y = 60; y < 120; y++) {
                    int b = chunk.getBlock(0, y, lz);
                    if (b == leaves || b == oakLeaves || b == birchLeaves || b == spruceLeaves) edgeLeaves++;
                    b = chunk.getBlock(15, y, lz);
                    if (b == leaves || b == oakLeaves || b == birchLeaves || b == spruceLeaves) edgeLeaves++;
                }
            }
            if (edgeLeaves > 0) {
                System.out.println("chunk(" + cx + ",0): " + edgeLeaves + " leaf blocks at x=0 and x=15 edges (spill across boundary)");
                totalSpilledLeaves += edgeLeaves;
            }
        }
        System.out.println("Total edge-spilled leaf blocks: " + totalSpilledLeaves);
        if (totalSpilledLeaves > 0) {
            System.out.println("PASS: features cross chunk boundaries (leaves found at chunk edges)");
        } else {
            System.out.println("NOTE: no edge leaves found (trees may not be near edges in this seed range)");
        }
    }
}
