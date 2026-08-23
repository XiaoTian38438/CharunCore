package com.CharunCore.server.world;

import com.CharunCore.server.network.NetworkHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家统计系统 (原版 minecraft:custom / mined / crafted / used / killed / picked_up / dropped / broken)。
 * 持久化到 world/stats/<uuid>.json, 与原版存档格式一致: { "stat.<category>.<key>": <int> }。
 * 埋点位置: NetworkHandler 挖矿/放置/合成/击杀/拾取处调用对应 add 方法。
 */
public final class StatisticsManager {

    private static final Map<UUID, Map<String, Long>> STORE = new ConcurrentHashMap<>();
    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Long>>() {}.getType();

    private StatisticsManager() {}

    private static Map<String, Long> forPlayer(UUID uuid) {
        return STORE.computeIfAbsent(uuid, k -> {
            Map<String, Long> m = new ConcurrentHashMap<>();
            load(uuid, m);
            return m;
        });
    }

    /** 增加一项统计 (线程安全)。category: mined/crafted/used/killed/picked_up/dropped/broken/custom。 */
    public static void add(NetworkHandler player, String category, String key, long amount) {
        if (player == null || player.data == null || key == null) return;
        add(player.uuid, category, key, amount);
    }

    public static void add(UUID uuid, String category, String key, long amount) {
        if (uuid == null || category == null || key == null) return;
        String stat = "minecraft:" + category + ":" + key;
        forPlayer(uuid).merge(stat, amount, Long::sum);
    }

    public static long get(UUID uuid, String category, String key) {
        if (uuid == null) return 0;
        String stat = "minecraft:" + category + ":" + key;
        return forPlayer(uuid).getOrDefault(stat, 0L);
    }

    public static Map<String, Long> snapshot(UUID uuid) {
        Map<String, Long> src = forPlayer(uuid);
        Map<String, Long> dst = new java.util.TreeMap<>();
        dst.putAll(src);
        return dst;
    }

    /** 玩家进服时预加载统计 (避免首次埋点竞态)。 */
    public static void preload(UUID uuid) { forPlayer(uuid); }

    /** 周期落盘 (由 WorldManager 自动保存调用)。 */
    public static void saveAll() {
        for (Map.Entry<UUID, Map<String, Long>> e : STORE.entrySet()) {
            save(e.getKey(), e.getValue());
        }
    }

    private static void save(UUID uuid, Map<String, Long> stats) {
        try {
            Path dir = Paths.get("world", "stats");
            Files.createDirectories(dir);
            Path file = dir.resolve(uuid + ".json");
            JsonObject root = new JsonObject();
            for (Map.Entry<String, Long> e : stats.entrySet()) {
                root.addProperty(e.getKey(), e.getValue());
            }
            try (FileWriter w = new FileWriter(file.toFile())) {
                GSON.toJson(root, w);
            }
        } catch (Exception ignored) {}
    }

    private static void load(UUID uuid, Map<String, Long> out) {
        try {
            Path file = Paths.get("world", "stats", uuid + ".json");
            if (!Files.exists(file)) return;
            try (FileReader r = new FileReader(file.toFile())) {
                JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
                for (Map.Entry<String, com.google.gson.JsonElement> e : root.entrySet()) {
                    out.put(e.getKey(), e.getValue().getAsLong());
                }
            }
        } catch (Exception ignored) {}
    }
}
