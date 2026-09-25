package com.CharunCore.server.worldgen.feature.vanilla;

import com.CharunCore.server.utils.RegistryHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 原版 1.21.11 主世界 55 群系的 placed feature 有序注册表转录。
 * 来源: mapping/cfr-source net/minecraft/data/worldgen/biome/OverworldBiomes.java + BiomeData.java
 *       + net/minecraft/data/worldgen/BiomeDefaultFeatures.java + placement/*.java 的 ResourceKey 字符串。
 * 步骤顺序即 GenerationStep.Decoration 枚举序(0=raw_generation .. 10=top_layer_modification);
 * 同一步骤内顺序为原版 addFeature 调用序, 装饰 RNG 对齐依赖它, 不可重排。
 * 洞穴雕刻器(carver)不属于 placed feature, 不在转录范围。
 */
public final class BiomeFeatureLists {

    public static final int STEP_COUNT = 11;

    private static final Map<Integer, String[][]> BY_BIOME_ID = new HashMap<Integer, String[][]>();

    private BiomeFeatureLists() {
    }

    /** 按 Decoration step 返回该群系此步骤的 placed feature 名列表(无 minecraft: 前缀)。未登记群系/越界返回空列表。 */
    public static List<String> featuresForStep(int biomeId, int step) {
        if (step < 0 || step >= STEP_COUNT) {
            return Collections.emptyList();
        }
        String[][] steps = BY_BIOME_ID.get(biomeId);
        if (steps == null || steps[step].length == 0) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList(steps[step]));
    }

    private static String[][] part(int step, String... names) {
        String[][] p = new String[STEP_COUNT][];
        p[step] = names;
        return p;
    }

    private static String[][] merge(String[][]... parts) {
        int[] lens = new int[STEP_COUNT];
        for (String[][] p : parts) {
            for (int s = 0; s < STEP_COUNT; s++) {
                if (p[s] != null) {
                    lens[s] += p[s].length;
                }
            }
        }
        String[][] out = new String[STEP_COUNT][];
        for (int s = 0; s < STEP_COUNT; s++) {
            String[] a = new String[lens[s]];
            int k = 0;
            for (String[][] p : parts) {
                if (p[s] != null) {
                    for (String n : p[s]) {
                        a[k++] = n;
                    }
                }
            }
            out[s] = a;
        }
        return out;
    }

    private static void register(String biomeName, String[][] steps) {
        BY_BIOME_ID.put(RegistryHelper.biomeNameToId(biomeName), steps);
    }

    private static final String[][] GEODE = part(2, "amethyst_geode");
    private static final String[][] MONSTER_ROOMS = part(3, "monster_room", "monster_room_deep");
    private static final String[][] VARIETY_ORES = part(6, "ore_dirt", "ore_gravel",
        "ore_granite_upper", "ore_granite_lower", "ore_diorite_upper", "ore_diorite_lower",
        "ore_andesite_upper", "ore_andesite_lower", "ore_tuff");
    private static final String[][] GLOW_LICHEN = part(9, "glow_lichen");
    private static final String[][] SPRINGS = part(8, "spring_water", "spring_lava");
    private static final String[][] FREEZE_TOP = part(10, "freeze_top_layer");

    private static final String[][] LAKES = part(1, "lake_lava_underground", "lake_lava_surface");
    private static final String[][] FOSSILS = part(3, "fossil_upper", "fossil_lower");
    private static final String[][] FROZEN_SPRING = part(8, "spring_lava_frozen");
    private static final String[][] EMERALD = part(6, "ore_emerald");
    private static final String[][] INFESTED = part(7, "ore_infested");

    private static final String[][] DEFAULT_ORES = part(6,
        "ore_coal_upper", "ore_coal_lower", "ore_iron_upper", "ore_iron_middle", "ore_iron_small",
        "ore_gold", "ore_gold_lower", "ore_redstone", "ore_redstone_lower",
        "ore_diamond", "ore_diamond_medium", "ore_diamond_large", "ore_diamond_buried",
        "ore_lapis", "ore_lapis_buried", "ore_copper", "underwater_magma");
    private static final String[][] DEFAULT_ORES_LARGE_COPPER = part(6,
        "ore_coal_upper", "ore_coal_lower", "ore_iron_upper", "ore_iron_middle", "ore_iron_small",
        "ore_gold", "ore_gold_lower", "ore_redstone", "ore_redstone_lower",
        "ore_diamond", "ore_diamond_medium", "ore_diamond_large", "ore_diamond_buried",
        "ore_lapis", "ore_lapis_buried", "ore_copper_large", "underwater_magma");
    private static final String[][] SOFT_DISKS = part(6, "disk_sand", "disk_clay", "disk_gravel");

    private static final String[][] PLAIN_GRASS = part(9, "patch_tall_grass_2");
    private static final String[][] PLAIN_VEG = part(9, "trees_plains", "flower_plains", "patch_grass_plain");
    private static final String[][] BUSHES = part(9, "patch_bush");
    private static final String[][] DEFAULT_FLOWERS = part(9, "flower_default");
    private static final String[][] DEFAULT_GRASS = part(9, "patch_grass_badlands");
    private static final String[][] FOREST_GRASS = part(9, "patch_grass_forest");
    private static final String[][] MUSHROOMS = part(9, "brown_mushroom_normal", "red_mushroom_normal");
    private static final String[][] EXTRA_WITH_WATER = part(9, "patch_pumpkin", "patch_sugar_cane", "patch_firefly_bush_near_water");
    private static final String[][] EXTRA_PUMPKIN_ONLY = part(9, "patch_pumpkin");
    private static final String[][] WATER_TREES = part(9, "trees_water");

    private static final String[][] GLOBAL_OVERWORLD =
        merge(LAKES, GEODE, MONSTER_ROOMS, VARIETY_ORES, GLOW_LICHEN, SPRINGS, FREEZE_TOP);

    static {
        register("the_void", merge(part(10, "void_start_platform")));

        register("plains", merge(GLOBAL_OVERWORLD, PLAIN_GRASS, BUSHES,
            DEFAULT_ORES, SOFT_DISKS, PLAIN_VEG, MUSHROOMS, EXTRA_WITH_WATER));
        register("sunflower_plains", merge(GLOBAL_OVERWORLD, PLAIN_GRASS, part(9, "patch_sunflower"),
            DEFAULT_ORES, SOFT_DISKS, PLAIN_VEG, MUSHROOMS, EXTRA_WITH_WATER));

        register("snowy_plains", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_snowy"), DEFAULT_FLOWERS, DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER));
        register("ice_spikes", merge(part(4, "ice_spike", "ice_patch"), GLOBAL_OVERWORLD,
            DEFAULT_ORES, SOFT_DISKS, part(9, "trees_snowy"), DEFAULT_FLOWERS, DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER));

        register("desert", merge(FOSSILS, GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            DEFAULT_FLOWERS, DEFAULT_GRASS, part(9, "patch_dry_grass_desert", "patch_dead_bush_2"), MUSHROOMS,
            part(9, "patch_sugar_cane_desert", "patch_pumpkin", "patch_cactus_desert"), part(4, "desert_well")));

        register("swamp", merge(FOSSILS, GLOBAL_OVERWORLD, DEFAULT_ORES, part(6, "disk_clay"),
            part(9, "trees_swamp", "flower_swamp", "patch_grass_normal", "patch_dead_bush", "patch_waterlily",
                "brown_mushroom_swamp", "red_mushroom_swamp"),
            MUSHROOMS,
            part(9, "patch_sugar_cane_swamp", "patch_pumpkin", "patch_firefly_bush_swamp", "patch_firefly_bush_near_water_swamp"),
            part(9, "seagrass_swamp")));

        register("mangrove_swamp", merge(FOSSILS, GLOBAL_OVERWORLD, DEFAULT_ORES, part(6, "disk_grass", "disk_clay"),
            part(9, "trees_mangrove", "patch_grass_normal", "patch_dead_bush", "patch_waterlily"),
            part(9, "seagrass_swamp", "patch_firefly_bush_near_water")));

        register("forest", merge(GLOBAL_OVERWORLD, part(9, "forest_flowers"), DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_birch_and_oak_leaf_litter"), BUSHES, DEFAULT_FLOWERS, FOREST_GRASS, MUSHROOMS, EXTRA_WITH_WATER));
        register("flower_forest", merge(GLOBAL_OVERWORLD, part(9, "flower_forest_flowers"), DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_flower_forest", "flower_flower_forest"), DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER));
        register("birch_forest", merge(GLOBAL_OVERWORLD, part(9, "forest_flowers"), DEFAULT_ORES, SOFT_DISKS,
            part(9, "wildflowers_birch_forest", "trees_birch"), BUSHES, DEFAULT_FLOWERS, FOREST_GRASS, MUSHROOMS, EXTRA_WITH_WATER));
        register("old_growth_birch_forest", merge(GLOBAL_OVERWORLD, part(9, "forest_flowers"), DEFAULT_ORES, SOFT_DISKS,
            part(9, "wildflowers_birch_forest", "birch_tall"), BUSHES, DEFAULT_FLOWERS, FOREST_GRASS, MUSHROOMS, EXTRA_WITH_WATER));
        register("dark_forest", merge(GLOBAL_OVERWORLD, part(9, "dark_forest_vegetation", "forest_flowers"),
            DEFAULT_ORES, SOFT_DISKS, DEFAULT_FLOWERS, FOREST_GRASS, MUSHROOMS, part(9, "patch_leaf_litter"), EXTRA_WITH_WATER));
        register("pale_garden", merge(GLOBAL_OVERWORLD,
            part(9, "pale_garden_vegetation", "pale_moss_patch", "pale_garden_flowers"),
            DEFAULT_ORES, SOFT_DISKS, part(9, "flower_pale_garden"), FOREST_GRASS, EXTRA_WITH_WATER));

        String[][] oldGrowthTaigaHead = merge(GLOBAL_OVERWORLD, part(2, "forest_rock"),
            part(9, "patch_large_fern"), DEFAULT_ORES, SOFT_DISKS);
        String[][] oldGrowthTaigaTail =
            part(9, "patch_grass_taiga", "patch_dead_bush", "brown_mushroom_old_growth", "red_mushroom_old_growth");
        register("old_growth_pine_taiga", merge(oldGrowthTaigaHead, part(9, "trees_old_growth_pine_taiga"),
            DEFAULT_FLOWERS, oldGrowthTaigaTail, MUSHROOMS, EXTRA_WITH_WATER, part(9, "patch_berry_common")));
        register("old_growth_spruce_taiga", merge(oldGrowthTaigaHead, part(9, "trees_old_growth_spruce_taiga"),
            DEFAULT_FLOWERS, oldGrowthTaigaTail, MUSHROOMS, EXTRA_WITH_WATER, part(9, "patch_berry_common")));

        String[][] taigaBody = merge(GLOBAL_OVERWORLD, part(9, "patch_large_fern"), DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_taiga"), DEFAULT_FLOWERS,
            part(9, "patch_grass_taiga_2", "brown_mushroom_taiga", "red_mushroom_taiga"), EXTRA_WITH_WATER);
        register("taiga", merge(taigaBody, part(9, "patch_berry_common")));
        register("snowy_taiga", merge(taigaBody, part(9, "patch_berry_rare")));

        String[][] savanna = merge(GLOBAL_OVERWORLD, part(9, "patch_tall_grass"), DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_savanna", "flower_warm", "patch_grass_savanna"), MUSHROOMS, EXTRA_WITH_WATER);
        register("savanna", savanna);
        register("savanna_plateau", savanna);
        register("windswept_savanna", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_windswept_savanna"), DEFAULT_FLOWERS, part(9, "patch_grass_normal"), MUSHROOMS, EXTRA_WITH_WATER));

        register("jungle", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            part(9, "bamboo_light"), part(9, "trees_jungle"), part(9, "flower_warm"), part(9, "patch_grass_jungle"),
            MUSHROOMS, EXTRA_WITH_WATER, part(9, "vines"), part(9, "patch_melon")));
        register("sparse_jungle", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_sparse_jungle"), part(9, "flower_warm"), part(9, "patch_grass_jungle"),
            MUSHROOMS, EXTRA_WITH_WATER, part(9, "vines"), part(9, "patch_melon_sparse")));
        register("bamboo_jungle", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            part(9, "bamboo", "bamboo_vegetation"), part(9, "flower_warm"), part(9, "patch_grass_jungle"),
            MUSHROOMS, EXTRA_WITH_WATER, part(9, "vines")));

        register("windswept_hills", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_windswept_hills"), BUSHES, DEFAULT_FLOWERS, DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER,
            EMERALD, INFESTED));
        register("windswept_gravelly_hills", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_windswept_hills"), BUSHES, DEFAULT_FLOWERS, DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER,
            EMERALD, INFESTED));
        register("windswept_forest", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_windswept_forest"), BUSHES, DEFAULT_FLOWERS, DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER,
            EMERALD, INFESTED));

        String[][] badlandsCore = merge(GLOBAL_OVERWORLD, DEFAULT_ORES, part(6, "ore_gold_extra"), SOFT_DISKS,
            part(9, "patch_grass_badlands", "patch_dry_grass_badlands", "patch_dead_bush_badlands"),
            MUSHROOMS,
            part(9, "patch_sugar_cane_badlands", "patch_pumpkin", "patch_cactus_decorated", "patch_firefly_bush_near_water"));
        register("badlands", badlandsCore);
        register("eroded_badlands", badlandsCore);
        register("wooded_badlands", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, part(6, "ore_gold_extra"), SOFT_DISKS,
            part(9, "trees_badlands"),
            part(9, "patch_grass_badlands", "patch_dry_grass_badlands", "patch_dead_bush_badlands"),
            MUSHROOMS,
            part(9, "patch_sugar_cane_badlands", "patch_pumpkin", "patch_cactus_decorated", "patch_firefly_bush_near_water")));

        register("meadow", merge(GLOBAL_OVERWORLD, PLAIN_GRASS, DEFAULT_ORES, SOFT_DISKS,
            part(9, "patch_grass_meadow", "flower_meadow", "trees_meadow", "wildflowers_meadow"), EMERALD, INFESTED));
        register("cherry_grove", merge(GLOBAL_OVERWORLD, PLAIN_GRASS, DEFAULT_ORES, SOFT_DISKS,
            part(9, "patch_grass_plain", "flower_cherry", "trees_cherry"), EMERALD, INFESTED));

        register("grove", merge(GLOBAL_OVERWORLD, FROZEN_SPRING, DEFAULT_ORES, SOFT_DISKS,
            part(9, "trees_grove"), EXTRA_PUMPKIN_ONLY, EMERALD, INFESTED));
        register("snowy_slopes", merge(GLOBAL_OVERWORLD, FROZEN_SPRING, DEFAULT_ORES, SOFT_DISKS,
            EXTRA_PUMPKIN_ONLY, EMERALD, INFESTED));
        String[][] peaks = merge(GLOBAL_OVERWORLD, FROZEN_SPRING, DEFAULT_ORES, SOFT_DISKS, EMERALD, INFESTED);
        register("frozen_peaks", peaks);
        register("jagged_peaks", peaks);
        register("stony_peaks", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS, EMERALD, INFESTED));

        register("frozen_river", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            WATER_TREES, BUSHES, DEFAULT_FLOWERS, DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER));
        register("river", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            WATER_TREES, BUSHES, DEFAULT_FLOWERS, DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER, part(9, "seagrass_river")));

        String[][] beach = merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            DEFAULT_FLOWERS, DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER);
        register("beach", beach);
        register("snowy_beach", beach);
        register("stony_shore", beach);

        String[][] oceanBase = merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            WATER_TREES, DEFAULT_FLOWERS, DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER);
        register("warm_ocean", merge(oceanBase, part(9, "warm_ocean_vegetation", "seagrass_warm", "sea_pickle")));
        register("lukewarm_ocean", merge(oceanBase, part(9, "seagrass_warm", "kelp_warm")));
        register("deep_lukewarm_ocean", merge(oceanBase, part(9, "seagrass_deep_warm", "kelp_warm")));
        register("ocean", merge(oceanBase, part(9, "seagrass_normal", "kelp_cold")));
        register("deep_ocean", merge(oceanBase, part(9, "seagrass_deep", "kelp_cold")));
        register("cold_ocean", merge(oceanBase, part(9, "seagrass_cold", "kelp_cold")));
        register("deep_cold_ocean", merge(oceanBase, part(9, "seagrass_deep_cold", "kelp_cold")));

        String[][] frozenOcean = merge(part(2, "iceberg_packed", "iceberg_blue"), GLOBAL_OVERWORLD, part(4, "blue_ice"),
            DEFAULT_ORES, SOFT_DISKS, WATER_TREES, DEFAULT_FLOWERS, DEFAULT_GRASS, MUSHROOMS, EXTRA_WITH_WATER);
        register("frozen_ocean", frozenOcean);
        register("deep_frozen_ocean", frozenOcean);

        register("mushroom_fields", merge(GLOBAL_OVERWORLD, DEFAULT_ORES, SOFT_DISKS,
            part(9, "mushroom_island_vegetation", "brown_mushroom_taiga", "red_mushroom_taiga"),
            part(9, "patch_sugar_cane", "patch_firefly_bush_near_water")));

        register("dripstone_caves", merge(GLOBAL_OVERWORLD, PLAIN_GRASS, DEFAULT_ORES_LARGE_COPPER, SOFT_DISKS,
            PLAIN_VEG, MUSHROOMS, EXTRA_PUMPKIN_ONLY,
            part(2, "large_dripstone"), part(7, "dripstone_cluster", "pointed_dripstone")));

        register("lush_caves", merge(GLOBAL_OVERWORLD, PLAIN_GRASS, DEFAULT_ORES, part(6, "ore_clay"), SOFT_DISKS,
            part(9, "lush_caves_ceiling_vegetation", "cave_vines", "lush_caves_clay", "lush_caves_vegetation",
                "rooted_azalea_tree", "spore_blossom", "classic_vines_cave_feature")));

        register("deep_dark", merge(GEODE, MONSTER_ROOMS, VARIETY_ORES, GLOW_LICHEN, FREEZE_TOP,
            PLAIN_GRASS, DEFAULT_ORES, SOFT_DISKS, PLAIN_VEG, MUSHROOMS, EXTRA_PUMPKIN_ONLY,
            part(7, "sculk_vein", "sculk_patch_deep_dark")));
    }
}
