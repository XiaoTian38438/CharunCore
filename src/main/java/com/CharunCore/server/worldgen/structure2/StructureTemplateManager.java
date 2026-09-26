package com.CharunCore.server.worldgen.structure2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StructureTemplateManager {

    private static final StructureTemplateManager INSTANCE = new StructureTemplateManager();
    /** ConcurrentHashMap: 区块生成在 ioExecutor 多线程跑, /tp 大范围加载时多个
     *  线程同时首次解析模板 -> HashMap.computeIfAbsent 抛 ConcurrentModificationException。 */
    private final Map<String, StructureTemplate> cache = new ConcurrentHashMap<>();

    public static StructureTemplateManager getInstance() {
        return INSTANCE;
    }

    public StructureTemplate getOrCreate(String id) {
        if (id == null) return null;
        id = id.startsWith("minecraft:") ? id.substring(10) : id;
        StructureTemplate t = cache.get(id);
        if (t != null) return t;
        return cache.computeIfAbsent(id, StructureTemplate::load);
    }

    public void clearCache() {
        cache.clear();
    }
}
