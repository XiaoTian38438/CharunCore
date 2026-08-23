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
import java.util.Random;

public class LootTableLoader {

    private static final String LOOT_DIR = "json/minecraft/loot_table/";
    private static final Map<String, JsonObject> cache = new HashMap<>();

    public static JsonObject get(String id) {
        if (id == null) return null;
        id = id.startsWith("minecraft:") ? id.substring(10) : id;
        if (cache.containsKey(id)) return cache.get(id);
        JsonObject loaded = load(id);
        cache.put(id, loaded);
        return loaded;
    }

    private static JsonObject load(String id) {
        String path = LOOT_DIR + id + ".json";
        File file = new File(path);
        if (!file.exists()) return null;
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static List<LootEntry> generateLoot(String lootTableId, long seed) {
        List<LootEntry> result = new ArrayList<>();
        JsonObject table = get(lootTableId);
        if (table == null) return result;

        Random rng = new Random(seed);
        JsonArray pools = table.getAsJsonArray("pools");
        if (pools == null) return result;

        for (JsonElement poolElem : pools) {
            JsonObject pool = poolElem.getAsJsonObject();
            int rolls = 1;
            if (pool.has("rolls")) {
                JsonElement rollsEl = pool.get("rolls");
                if (rollsEl.isJsonObject()) {
                    JsonObject rollsObj = rollsEl.getAsJsonObject();
                    if (rollsObj.has("min")) {
                        int min = rollsObj.get("min").getAsInt();
                        int max = rollsObj.has("max") ? rollsObj.get("max").getAsInt() : min;
                        rolls = min + rng.nextInt(max - min + 1);
                    } else if (rollsObj.has("value")) {
                        rolls = rollsObj.get("value").getAsInt();
                    }
                } else if (rollsEl.isJsonPrimitive()) {
                    rolls = rollsEl.getAsInt();
                }
            }

            JsonArray entries = pool.getAsJsonArray("entries");
            if (entries == null) continue;

            int totalWeight = 0;
            for (JsonElement e : entries) {
                JsonObject entry = e.getAsJsonObject();
                totalWeight += entry.has("weight") ? entry.get("weight").getAsInt() : 1;
            }

            for (int r = 0; r < rolls; r++) {
                if (totalWeight <= 0) continue;
                int roll = rng.nextInt(totalWeight);
                int acc = 0;
                for (JsonElement e : entries) {
                    JsonObject entry = e.getAsJsonObject();
                    int weight = entry.has("weight") ? entry.get("weight").getAsInt() : 1;
                    acc += weight;
                    if (roll < acc) {
                        String type = entry.has("type") ? entry.get("type").getAsString() : "";
                        if (type.endsWith("item") && entry.has("name")) {
                            String itemName = entry.get("name").getAsString();
                            itemName = itemName.startsWith("minecraft:") ? itemName.substring(10) : itemName;
                            int count = 1;
                            if (entry.has("functions")) {
                                count = applyFunctions(entry.getAsJsonArray("functions"), rng);
                            }
                            result.add(new LootEntry(itemName, count));
                        }
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static int applyFunctions(JsonArray functions, Random rng) {
        int count = 1;
        for (JsonElement fe : functions) {
            JsonObject func = fe.getAsJsonObject();
            String function = func.has("function") ? func.get("function").getAsString() : "";
            if (function.endsWith("set_count")) {
                // count 在原版 loot table 中可能是数字（"count": 1）或对象（{"min":1,"max":3}）。
                // 【修复】曾直接 getAsJsonObject → 数字型抛 JsonPrimitive→JsonObject 异常，
                // 中断所有包含该 function 的结构（buried_treasure 等）导致区块生成失败。
                JsonElement countEl = func.has("count") ? func.get("count") : null;
                if (countEl != null && countEl.isJsonObject()) {
                    JsonObject countObj = countEl.getAsJsonObject();
                    if (countObj.has("min")) {
                        int min = countObj.get("min").getAsInt();
                        int max = countObj.has("max") ? countObj.get("max").getAsInt() : min;
                        count = min + rng.nextInt(max - min + 1);
                    } else if (countObj.has("value")) {
                        count = countObj.get("value").getAsInt();
                    }
                } else if (countEl != null && countEl.isJsonPrimitive()) {
                    count = countEl.getAsInt();
                }
            }
        }
        return Math.max(count, 1);
    }

    public static class LootEntry {
        public final String itemName;
        public final int count;

        public LootEntry(String itemName, int count) {
            this.itemName = itemName;
            this.count = count;
        }
    }
}
