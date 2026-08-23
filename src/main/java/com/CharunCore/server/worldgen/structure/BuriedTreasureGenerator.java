package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;

public class BuriedTreasureGenerator {

    private static final int CHEST = BlockStateHelper.getDefault("chest");

    public static void generate(Chunk chunk, int localX, int localZ, int y) {
        if (localX >= 0 && localX <= 15 && localZ >= 0 && localZ <= 15 && y >= -64 && y <= 319) {
            chunk.setBlock(localX, y, localZ, CHEST);
        }
    }
}
