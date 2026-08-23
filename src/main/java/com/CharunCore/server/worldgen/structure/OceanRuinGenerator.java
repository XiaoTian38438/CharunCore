package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

/** 死代码（审计 P1-6）。仅被已废弃的 StructureManager.generateStructures() 引用，外部无调用。活跃管线改用 structure2.NonJigsawPlacer.placeScatterTemplate。 */
@Deprecated(since = "audit P1-6", forRemoval = true)
public class OceanRuinGenerator {

    private static final String[] COLD_TEMPLATES = {
        "underwater_ruin/cold/ruins_1.nbt",
        "underwater_ruin/cold/ruins_2.nbt",
        "underwater_ruin/cold/ruins_3.nbt",
        "underwater_ruin/cold/ruins_4.nbt",
        "underwater_ruin/cold/ruins_5.nbt",
        "underwater_ruin/cold/ruins_6.nbt",
        "underwater_ruin/cold/ruins_7.nbt",
        "underwater_ruin/cold/ruins_8.nbt"
    };

    private static final String[] WARM_TEMPLATES = {
        "underwater_ruin/warm/ruins_1.nbt",
        "underwater_ruin/warm/ruins_2.nbt",
        "underwater_ruin/warm/ruins_3.nbt",
        "underwater_ruin/warm/ruins_4.nbt",
        "underwater_ruin/warm/ruins_5.nbt",
        "underwater_ruin/warm/ruins_6.nbt",
        "underwater_ruin/warm/ruins_7.nbt",
        "underwater_ruin/warm/ruins_8.nbt"
    };

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, RandomSource random, boolean warm) {
        int baseY = findWaterY(chunk, localX, localZ);
        if (baseY < -64 || baseY > 300) return;

        String[] templates = warm ? WARM_TEMPLATES : COLD_TEMPLATES;
        int templateIdx = random.nextInt(templates.length);
        int rotation = random.nextInt(4);

        StructureTemplateLoader.placeTemplate(chunk, templates[templateIdx], localX, localZ, baseY - 3, rotation);
    }

    private static int findWaterY(Chunk chunk, int lx, int lz) {
        for (int y = 319; y >= -64; y--) {
            if (lx >= 0 && lx <= 15 && lz >= 0 && lz <= 15) {
                int block = chunk.getBlock(lx, y, lz);
                if (block != 0) return y + 1;
            }
        }
        return 64;
    }
}
