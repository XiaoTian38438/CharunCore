package com.CharunCore.server.worldgen.structure2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class StructureRegistry {

    public static class ConfiguredStructure {
        public final String id;
        public final String type;
        public final String startPool;
        public final int size;
        public final String step;
        public final String biomesTag;
        public final String terrainAdaptation;
        public final String startHeightType;
        public final int startHeightMin;
        public final int startHeightMax;
        public final boolean projectStartToHeightmap;

        public ConfiguredStructure(String id, String type, String startPool, int size,
                                    String step, String biomesTag, String terrainAdaptation,
                                    String startHeightType, int startHeightMin, int startHeightMax,
                                    boolean projectStartToHeightmap) {
            this.id = id;
            this.type = type;
            this.startPool = startPool;
            this.size = size;
            this.step = step;
            this.biomesTag = biomesTag;
            this.terrainAdaptation = terrainAdaptation;
            this.startHeightType = startHeightType;
            this.startHeightMin = startHeightMin;
            this.startHeightMax = startHeightMax;
            this.projectStartToHeightmap = projectStartToHeightmap;
        }

        public boolean isJigsaw() {
            return "jigsaw".equals(type);
        }
    }

    private static final String STRUCTURE_DIR = "json/minecraft/worldgen/structure/";
    private static final Map<String, ConfiguredStructure> registry = new HashMap<>();

    public static ConfiguredStructure get(String id) {
        if (id == null) return null;
        id = id.startsWith("minecraft:") ? id.substring(10) : id;
        if (registry.containsKey(id)) return registry.get(id);
        ConfiguredStructure loaded = load(id);
        registry.put(id, loaded);
        return loaded;
    }

    public static Map<String, ConfiguredStructure> loadAll() {
        File dir = new File(STRUCTURE_DIR);
        if (!dir.exists() || !dir.isDirectory()) return registry;
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null) return registry;
        for (File f : files) {
            String id = f.getName().replace(".json", "");
            get(id);
        }
        return registry;
    }

    private static int parseHeightAnchor(JsonObject anchor, int minY) {
        if (anchor == null) return 0;
        if (anchor.has("absolute")) return anchor.get("absolute").getAsInt();
        if (anchor.has("above_bottom")) return minY + anchor.get("above_bottom").getAsInt();
        if (anchor.has("below_top")) return 320 - 1 - anchor.get("below_top").getAsInt();
        return 0;
    }

    private static ConfiguredStructure load(String id) {
        String path = STRUCTURE_DIR + id + ".json";
        File file = new File(path);
        if (!file.exists()) return null;
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            String type = json.has("type") ? json.get("type").getAsString() : "";
            type = type.startsWith("minecraft:") ? type.substring(10) : type;
            String startPool = json.has("start_pool") ? json.get("start_pool").getAsString() : null;
            if (startPool != null) startPool = startPool.startsWith("minecraft:") ? startPool.substring(10) : startPool;
            int size = json.has("size") ? json.get("size").getAsInt() : 6;
            String step = json.has("step") ? json.get("step").getAsString() : "surface_structures";
            String biomes = json.has("biomes") ? json.get("biomes").getAsString() : "";
            if (biomes.startsWith("#minecraft:has_structure/")) {
                biomes = biomes.substring("#minecraft:has_structure/".length());
            }
            String terrainAdaptation = json.has("terrain_adaptation") ? json.get("terrain_adaptation").getAsString() : "none";

            String startHeightType = "absolute";
            int startHeightMin = 0;
            int startHeightMax = 0;
            if (json.has("start_height")) {
                JsonObject sh = json.getAsJsonObject("start_height");
                String sht = sh.has("type") ? sh.get("type").getAsString() : "absolute";
                sht = sht.startsWith("minecraft:") ? sht.substring(10) : sht;
                startHeightType = sht;
                int minY = -64;
                if (sht.equals("uniform")) {
                    startHeightMin = parseHeightAnchor(sh.getAsJsonObject("min_inclusive"), minY);
                    startHeightMax = parseHeightAnchor(sh.getAsJsonObject("max_inclusive"), minY);
                } else if (sht.equals("absolute")) {
                    startHeightMin = parseHeightAnchor(sh, minY);
                    startHeightMax = startHeightMin;
                } else {
                    startHeightMin = parseHeightAnchor(sh, minY);
                    startHeightMax = startHeightMin;
                }
            }

            boolean projectStartToHeightmap = json.has("project_start_to_heightmap");

            return new ConfiguredStructure(id, type, startPool, size, step, biomes,
                terrainAdaptation, startHeightType, startHeightMin, startHeightMax,
                projectStartToHeightmap);
        } catch (Exception e) {
            System.err.println("[structure2] Failed to load structure " + id + ": " + e.getMessage());
            return null;
        }
    }
}
