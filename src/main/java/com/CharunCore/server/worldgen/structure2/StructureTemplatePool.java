package com.CharunCore.server.worldgen.structure2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.CharunCore.server.world.gen.RandomSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureTemplatePool {

    public static final StructureTemplatePool EMPTY = new StructureTemplatePool(
        Collections.emptyList(), "empty", StructurePoolElement.EMPTY_SINGLETON);

    private final List<StructurePoolElement> templates;
    private final String fallbackPoolId;
    private final StructurePoolElement fallbackElement;

    public StructureTemplatePool(List<StructurePoolElement> templates, String fallbackPoolId,
                                   StructurePoolElement fallbackElement) {
        this.templates = templates;
        this.fallbackPoolId = fallbackPoolId;
        this.fallbackElement = fallbackElement;
    }

    public int size() { return templates.size(); }

    public List<StructurePoolElement> getTemplates() { return templates; }

    public String getFallbackPoolId() { return fallbackPoolId; }

    public StructurePoolElement getRandomTemplate(RandomSource random) {
        if (templates.isEmpty()) return null;
        return templates.get(random.nextInt(templates.size()));
    }

    public List<StructurePoolElement> getShuffledTemplates(RandomSource random) {
        List<StructurePoolElement> shuffled = new ArrayList<>(templates);
        Collections.shuffle(shuffled, new java.util.Random(random.nextLong()));
        return shuffled;
    }

    private static final String POOL_DIR = "json/minecraft/worldgen/template_pool/";
    private static final Map<String, StructureTemplatePool> cache = new HashMap<>();
    private static StructureTemplateManager templateManager;

    public static void setTemplateManager(StructureTemplateManager mgr) {
        templateManager = mgr;
    }

    public static StructureTemplatePool get(String poolId) {
        if (poolId == null) return EMPTY;
        poolId = stripMinecraft(poolId);
        if (cache.containsKey(poolId)) return cache.get(poolId);
        StructureTemplatePool loaded = load(poolId);
        cache.put(poolId, loaded);
        return loaded;
    }

    private static String stripMinecraft(String s) {
        return s.startsWith("minecraft:") ? s.substring(10) : s;
    }

    private static StructureTemplatePool load(String poolId) {
        String path = POOL_DIR + poolId + ".json";
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("[structure2] Pool not found: " + path);
            return EMPTY;
        }
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray elements = json.getAsJsonArray("elements");
            String fallback = json.has("fallback") ? json.get("fallback").getAsString() : "minecraft:empty";
            fallback = stripMinecraft(fallback);

            List<StructurePoolElement> templates = new ArrayList<>();
            for (JsonElement entryElem : elements) {
                JsonObject entry = entryElem.getAsJsonObject();
                int weight = entry.get("weight").getAsInt();
                JsonObject elem = entry.getAsJsonObject("element");
                String type = elem.get("element_type").getAsString();
                type = stripMinecraft(type);

                StructurePoolElement element;
                if (type.equals("empty_pool_element")) {
                    element = StructurePoolElement.EMPTY_SINGLETON;
                } else if (type.equals("single_pool_element") || type.equals("legacy_single_pool_element")) {
                    String location = elem.get("location").getAsString();
                    location = stripMinecraft(location);
                    String projectionStr = elem.has("projection") ? elem.get("projection").getAsString() : "rigid";
                    Projection projection = Projection.fromString(projectionStr);
                    element = new SinglePoolElement(location, projection);
                } else {
                    element = StructurePoolElement.EMPTY_SINGLETON;
                }

                for (int w = 0; w < weight; w++) {
                    templates.add(element);
                }
            }
            return new StructureTemplatePool(templates, fallback, StructurePoolElement.EMPTY_SINGLETON);
        } catch (Exception e) {
            System.err.println("[structure2] Failed to load pool " + poolId + ": " + e.getMessage());
            return EMPTY;
        }
    }
}
