package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

/** 死代码（审计 P1-6）。仅被已废弃的 StructureManager.generateStructures() 引用，外部无调用。活跃管线改用 structure2.NonJigsawPlacer.placeScatterTemplate。 */
@Deprecated(since = "audit P1-6", forRemoval = true)
public class RuinedPortalGenerator {

    private static final String[] TEMPLATES = {
        "ruined_portal/ruined_portal_1.nbt",
        "ruined_portal/ruined_portal_2.nbt",
        "ruined_portal/ruined_portal_3.nbt",
        "ruined_portal/ruined_portal_4.nbt",
        "ruined_portal/ruined_portal_5.nbt",
        "ruined_portal/ruined_portal_6.nbt",
        "ruined_portal/ruined_portal_7.nbt",
        "ruined_portal/ruined_portal_giant_1.nbt",
        "ruined_portal/ruined_portal_giant_2.nbt"
    };

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, RandomSource random) {
        int baseY = findSurfaceY(chunk, localX, localZ);
        if (baseY < -60 || baseY > 300) return;

        int templateIdx = random.nextInt(TEMPLATES.length);
        int rotation = random.nextInt(4);

        StructureTemplateLoader.placeTemplate(chunk, TEMPLATES[templateIdx], localX, localZ, baseY - 1, rotation);
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
