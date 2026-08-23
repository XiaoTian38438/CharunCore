package com.CharunCore.server.worldgen.structure2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureSet {

    private final List<StructureSelectionEntry> structures;
    private final RandomSpreadStructurePlacement placement;

    public StructureSet(List<StructureSelectionEntry> structures, RandomSpreadStructurePlacement placement) {
        this.structures = structures;
        this.placement = placement;
    }

    public List<StructureSelectionEntry> getStructures() { return structures; }
    public RandomSpreadStructurePlacement getPlacement() { return placement; }

    private static final String STRUCTURE_SET_DIR = "json/minecraft/worldgen/structure_set/";
    private static final Map<String, StructureSet> cache = new HashMap<>();

    public static StructureSet get(String id) {
        if (id == null) return null;
        id = id.startsWith("minecraft:") ? id.substring(10) : id;
        if (cache.containsKey(id)) return cache.get(id);
        StructureSet loaded = load(id);
        if (loaded != null) cache.put(id, loaded); // 不缓存 null，避免 map 含 null 值导致迭代 NPE
        return loaded;
    }

    public static Map<String, StructureSet> loadAll() {
        File dir = new File(STRUCTURE_SET_DIR);
        if (!dir.exists() || !dir.isDirectory()) return cache;
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null) return cache;
        for (File f : files) {
            String id = f.getName().replace(".json", "");
            get(id);
        }
        return cache;
    }

    private static StructureSet load(String id) {
        String path = STRUCTURE_SET_DIR + id + ".json";
        File file = new File(path);
        if (!file.exists()) return null;
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject placement = json.getAsJsonObject("placement");
            String type = placement.get("type").getAsString();
            type = type.startsWith("minecraft:") ? type.substring(10) : type;

            if (!type.equals("random_spread")) return null;

            int salt = placement.get("salt").getAsInt();
            int separation = placement.get("separation").getAsInt();
            int spacing = placement.get("spacing").getAsInt();
            RandomSpreadStructurePlacement pl = new RandomSpreadStructurePlacement(spacing, separation, salt);

            JsonArray structures = json.getAsJsonArray("structures");
            List<StructureSelectionEntry> entries = new ArrayList<>();
            for (JsonElement elem : structures) {
                JsonObject entry = elem.getAsJsonObject();
                String structId = entry.get("structure").getAsString();
                structId = structId.startsWith("minecraft:") ? structId.substring(10) : structId;
                int weight = entry.get("weight").getAsInt();
                entries.add(new StructureSelectionEntry(structId, weight));
            }
            return new StructureSet(entries, pl);
        } catch (Exception e) {
            System.err.println("[structure2] Failed to load structure_set " + id + ": " + e.getMessage());
            return null;
        }
    }
}
