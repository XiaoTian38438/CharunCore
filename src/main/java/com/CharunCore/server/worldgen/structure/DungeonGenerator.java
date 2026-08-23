package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public class DungeonGenerator {

    private static final int COBBLESTONE = BlockStateHelper.getDefault("cobblestone");
    private static final int MOSSY_COBBLESTONE = BlockStateHelper.getDefault("mossy_cobblestone");
    private static final int SPAWNER = BlockStateHelper.getDefault("spawner");
    private static final int CHEST = BlockStateHelper.getDefault("chest");

    public static void generate(Chunk chunk, int localX, int localZ, int y, RandomSource random) {
        int roomW = 5 + random.nextInt(3) * 2;
        int roomH = 3 + random.nextInt(2);
        int roomD = 5 + random.nextInt(3) * 2;

        for (int x = -roomW / 2; x <= roomW / 2; x++) {
            for (int z = -roomD / 2; z <= roomD / 2; z++) {
                int lx = localX + x;
                int lz = localZ + z;
                if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;

                for (int dy = 0; dy <= roomH; dy++) {
                    int by = y + dy;
                    boolean floor = dy == 0;
                    boolean ceiling = dy == roomH;
                    boolean wall = Math.abs(x) == roomW / 2 || Math.abs(z) == roomD / 2;

                    if (floor || ceiling || wall) {
                        int block = (random.nextInt(3) == 0) ? MOSSY_COBBLESTONE : COBBLESTONE;
                        chunk.setBlock(lx, by, lz, block);
                    } else {
                        chunk.setBlock(lx, by, lz, 0);
                    }
                }
            }
        }

        if (localX >= 0 && localX <= 15 && localZ >= 0 && localZ <= 15) {
            chunk.setBlock(localX, y + 1, localZ, SPAWNER);
        }

        int numChests = 1 + random.nextInt(2);
        for (int i = 0; i < numChests; i++) {
            int cx = localX + random.nextInt(roomW - 1) - roomW / 2 + 1;
            int cz = localZ + random.nextInt(roomD - 1) - roomD / 2 + 1;
            if (cx >= 0 && cx <= 15 && cz >= 0 && cz <= 15) {
                chunk.setBlock(cx, y + 1, cz, CHEST);
            }
        }
    }
}
