package com.CharunCore.server.worldgen.structure2;

import java.util.HashMap;
import java.util.Map;

public class StructureTemplateManager {

    private static final StructureTemplateManager INSTANCE = new StructureTemplateManager();
    private final Map<String, StructureTemplate> cache = new HashMap<>();

    public static StructureTemplateManager getInstance() {
        return INSTANCE;
    }

    public StructureTemplate getOrCreate(String id) {
        if (id == null) return null;
        id = id.startsWith("minecraft:") ? id.substring(10) : id;
        return cache.computeIfAbsent(id, StructureTemplate::load);
    }

    public void clearCache() {
        cache.clear();
    }
}
