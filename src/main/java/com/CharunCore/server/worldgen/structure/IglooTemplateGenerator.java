package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

/** 死代码（审计 P1-6）。仅被已废弃的 StructureManager.generateStructures() 引用，外部无调用。活跃管线改用 structure2.NonJigsawPlacer.placeIgloo。 */
@Deprecated(since = "audit P1-6", forRemoval = true)
public class IglooTemplateGenerator {

    private static final String[] TEMPLATES = {
        "igloo/top.nbt",
        "igloo/middle.nbt",
        "igloo/bottom.nbt"
    };

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, RandomSource random) {
        int baseY = findSurfaceY(chunk, localX, localZ);
        if (baseY < -60 || baseY > 300) return;

        int rotation = random.nextInt(4);

        StructureTemplateLoader.placeTemplate(chunk, TEMPLATES[0], localX, localZ, baseY, rotation);

        if (random.nextInt(2) == 0) {
            int basementY = baseY - 4;
            StructureTemplateLoader.placeTemplate(chunk, TEMPLATES[2], localX, localZ, basementY, rotation);
            int shaftY = basementY + 3;
            int shaftPieces = 2 + random.nextInt(5);
            for (int i = 0; i < shaftPieces; i++) {
                StructureTemplateLoader.placeTemplate(chunk, TEMPLATES[1], localX, localZ, shaftY + i * 3, rotation);
            }
        }
    }

    private static int findSurfaceY(Chunk chunk, int lx, int lz) {
        for (int y = 319; y >= -64; y--) {
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                if (chunk.getBlock(lx, y, lz) != 0) return y + 1;
            }
        }
        return 64;
    }
}
