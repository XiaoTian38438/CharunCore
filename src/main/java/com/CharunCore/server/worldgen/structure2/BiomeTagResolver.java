package com.CharunCore.server.worldgen.structure2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BiomeTagResolver {

    private static final String TAG_DIR = "json/minecraft/tags/worldgen/biome/has_structure/";
    private static final String BIOME_TAG_DIR = "json/minecraft/tags/worldgen/biome/";
    private static final Map<String, Set<Integer>> cache = new HashMap<>();

    private static final String[] BIOME_NAMES = {
        "badlands", "bamboo_jungle", "basalt_deltas", "beach", "birch_forest",
        "cherry_grove", "cold_ocean", "crimson_forest", "dark_forest", "deep_cold_ocean",
        "deep_dark", "deep_frozen_ocean", "deep_lukewarm_ocean", "deep_ocean", "desert",
        "dripstone_caves", "end_barrens", "end_highlands", "end_midlands", "eroded_badlands",
        "flower_forest", "forest", "frozen_ocean", "frozen_peaks", "frozen_river",
        "grove", "ice_spikes", "jagged_peaks", "jungle", "lukewarm_ocean",
        "lush_caves", "mangrove_swamp", "meadow", "mushroom_fields", "nether_wastes",
        "ocean", "old_growth_birch_forest", "old_growth_pine_taiga", "old_growth_spruce_taiga", "pale_garden",
        "plains", "river", "savanna", "savanna_plateau", "small_end_islands",
        "snowy_beach", "snowy_plains", "snowy_slopes", "snowy_taiga", "soul_sand_valley",
        "sparse_jungle", "stony_peaks", "stony_shore", "sunflower_plains", "swamp",
        "taiga", "the_end", "the_void", "warm_ocean", "warped_forest",
        "windswept_forest", "windswept_gravelly_hills", "windswept_hills", "windswept_savanna", "wooded_badlands"
    };

    private static final Map<String, Integer> nameToId = new HashMap<>();
    static {
        for (int i = 0; i < BIOME_NAMES.length; i++) {
            nameToId.put(BIOME_NAMES[i], i);
        }
    }

    public static Integer getBiomeId(String name) {
        if (name == null) return null;
        name = name.startsWith("minecraft:") ? name.substring(10) : name;
        return nameToId.get(name);
    }

    public static Set<Integer> getBiomesForStructure(String tagName) {
        if (tagName == null) return Set.of();
        tagName = tagName.startsWith("minecraft:has_structure/") ? tagName.substring(22) : tagName;
        if (cache.containsKey(tagName)) return cache.get(tagName);
        Set<Integer> result = resolveTag(tagName, TAG_DIR, 0);
        cache.put(tagName, result);
        return result;
    }

    /**
     * 递归解析 biome tag：values 可能是直接 biome 名，也可能是嵌套 tag 引用
     * （如 nether_fortress -> #minecraft:is_nether）。【修复】原实现只认直接名 → 要塞 biome 空集。
     */
    private static Set<Integer> resolveTag(String tagName, String dir, int depth) {
        Set<Integer> result = new HashSet<>();
        if (depth > 5) return result;
        String path = dir + tagName + ".json";
        File file = new File(path);
        if (!file.exists()) {
            // 嵌套引用通常在普通 biome tag 目录（非 has_structure 子目录）
            if (!dir.equals(BIOME_TAG_DIR)) return resolveTag(tagName, BIOME_TAG_DIR, depth + 1);
            return result;
        }
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if (!json.has("values")) return result;
            JsonArray values = json.getAsJsonArray("values");
            for (JsonElement elem : values) {
                String v = elem.getAsString();
                if (v.startsWith("#")) {
                    String ref = v.startsWith("#minecraft:") ? v.substring(11) : v.substring(1);
                    result.addAll(resolveTag(ref, dir.equals(TAG_DIR) ? BIOME_TAG_DIR : BIOME_TAG_DIR, depth + 1));
                } else {
                    Integer id = getBiomeId(v);
                    if (id != null) result.add(id);
                }
            }
        } catch (Exception e) {
            System.err.println("[structure2] Failed to load biome tag " + tagName + ": " + e.getMessage());
        }
        return result;
    }
}
