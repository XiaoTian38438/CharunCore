package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

/**
 * 死代码（审计 P1-6）。旧版沉船生成器：引用不存在的 shipwreck/shipwreck_beached_1..20.nbt，
 * 活跃管线改用 structure2.NonJigsawPlacer.placeScatterTemplate（读取 mapping 下真实模板）。
 * 仅被同包已废弃的 StructureManager.generateStructures() 引用，外部无调用。保留标注避免误删破坏编译。
 */
@Deprecated(since = "audit P1-6", forRemoval = true)
public class ShipwreckGenerator {

    private static final String[] TEMPLATES = {
        "shipwreck/shipwreck_beached_1.nbt",
        "shipwreck/shipwreck_beached_2.nbt",
        "shipwreck/shipwreck_beached_3.nbt",
        "shipwreck/shipwreck_beached_4.nbt",
        "shipwreck/shipwreck_beached_5.nbt",
        "shipwreck/shipwreck_beached_6.nbt",
        "shipwreck/shipwreck_beached_7.nbt",
        "shipwreck/shipwreck_beached_8.nbt",
        "shipwreck/shipwreck_beached_9.nbt",
        "shipwreck/shipwreck_beached_10.nbt",
        "shipwreck/shipwreck_beached_11.nbt",
        "shipwreck/shipwreck_beached_12.nbt",
        "shipwreck/shipwreck_beached_13.nbt",
        "shipwreck/shipwreck_beached_14.nbt",
        "shipwreck/shipwreck_beached_15.nbt",
        "shipwreck/shipwreck_beached_16.nbt",
        "shipwreck/shipwreck_beached_17.nbt",
        "shipwreck/shipwreck_beached_18.nbt",
        "shipwreck/shipwreck_beached_19.nbt",
        "shipwreck/shipwreck_beached_20.nbt"
    };

    public static void generate(Chunk chunk, int chunkX, int chunkZ, int localX, int localZ, RandomSource random) {
        int baseY = findSurfaceY(chunk, localX, localZ);
        if (baseY < -60 || baseY > 300) return;

        int templateIdx = random.nextInt(TEMPLATES.length);
        int rotation = random.nextInt(4);

        StructureTemplateLoader.placeTemplate(chunk, TEMPLATES[templateIdx], localX, localZ, baseY - 2, rotation);
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
