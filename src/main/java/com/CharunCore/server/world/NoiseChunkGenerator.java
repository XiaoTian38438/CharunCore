package com.CharunCore.server.world;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;

public class NoiseChunkGenerator {
    public NoiseChunkGenerator(long seed) {}

    public Chunk generate(int chunkX, int chunkZ) {
        Chunk chunk = new Chunk(chunkX, chunkZ);

        // 一次性查好 stateId，避免重复查表
        int bedrock    = BlockStateHelper.getDefault("bedrock");
        int stone      = BlockStateHelper.getDefault("stone");
        int dirt       = BlockStateHelper.getDefault("dirt");
        int grassBlock = BlockStateHelper.getDefault("grass_block");


        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunk.setBlock(x, -64, z, bedrock);
                for (int y = -63; y <= 57; y++) chunk.setBlock(x, y, z, stone);
                for (int y =  58; y <= 59; y++) chunk.setBlock(x, y, z, dirt);
                chunk.setBlock(x, 60, z, grassBlock);
            }
        }
        return chunk;
    }
}