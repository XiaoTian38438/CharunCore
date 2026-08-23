package com.CharunCore.server.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** 封禁名单 (banned-players.json): 支持永久与限时封禁, 按 UUID/用户名双匹配。 */
public final class BanList {

    public record BanEntry(String name, UUID uuid, String reason, long expiresAtMs) {}

    private static final Path FILE = Path.of("banned-players.json");
    private static final List<BanEntry> ENTRIES = new java.util.concurrent.CopyOnWriteArrayList<>();
    private static volatile boolean loaded = false;

    private BanList() {}

    private static void ensureLoaded() {
        if (loaded) return;
        synchronized (BanList.class) {
            if (loaded) return;
            ENTRIES.clear();
            if (Files.exists(FILE)) {
                try {
                    String json = Files.readString(FILE);
                    com.google.gson.JsonArray arr = com.google.gson.JsonParser.parseString(json).getAsJsonArray();
                    for (com.google.gson.JsonElement el : arr) {
                        com.google.gson.JsonObject o = el.getAsJsonObject();
                        UUID uuid = null;
                        try {
                            if (o.has("uuid") && !o.get("uuid").isJsonNull()) {
                                uuid = UUID.fromString(o.get("uuid").getAsString());
                            }
                        } catch (IllegalArgumentException ignored) {}
                        ENTRIES.add(new BanEntry(
                                o.get("name").getAsString(),
                                uuid,
                                o.has("reason") ? o.get("reason").getAsString() : "Banned",
                                o.has("expiresAt") ? o.get("expiresAt").getAsLong() : -1L));
                    }
                } catch (Exception e) {
                    System.err.println("[BanList] 读取 banned-players.json 失败: " + e.getMessage());
                }
            }
            loaded = true;
        }
    }

    public static void ban(UUID uuid, String name, String reason, long durationMs) {
        ensureLoaded();
        unbanInternal(uuid, name);
        ENTRIES.add(new BanEntry(name, uuid, reason == null || reason.isEmpty() ? "Banned" : reason,
                durationMs <= 0 ? -1L : System.currentTimeMillis() + durationMs));
        save();
    }

    public static boolean unban(String nameOrUuid) {
        ensureLoaded();
        UUID uuid = null;
        try { uuid = UUID.fromString(nameOrUuid); } catch (IllegalArgumentException ignored) {}
        boolean removed = unbanInternal(uuid, nameOrUuid);
        if (removed) save();
        return removed;
    }

    private static boolean unbanInternal(UUID uuid, String nameOrUuid) {
        boolean removed = false;
        for (BanEntry e : new ArrayList<>(ENTRIES)) {
            boolean match = (uuid != null && uuid.equals(e.uuid()))
                    || e.name().equalsIgnoreCase(nameOrUuid);
            if (match) {
                ENTRIES.remove(e);
                removed = true;
            }
        }
        return removed;
    }

    /** 永久封禁或未到期的限时封禁 → 返回封禁原因; 否则 null。过期间自动清理。 */
    public static String checkBanned(UUID uuid, String name) {
        ensureLoaded();
        long now = System.currentTimeMillis();
        boolean expired = false;
        for (BanEntry e : new ArrayList<>(ENTRIES)) {
            boolean match = (uuid != null && uuid.equals(e.uuid())) || e.name().equalsIgnoreCase(name);
            if (!match) continue;
            if (e.expiresAtMs() > 0 && now >= e.expiresAtMs()) {
                ENTRIES.remove(e);
                expired = true;
                continue;
            }
            return e.reason();
        }
        if (expired) save();
        return null;
    }

    public static List<BanEntry> entries() {
        ensureLoaded();
        List<BanEntry> active = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (BanEntry e : ENTRIES) {
            if (e.expiresAtMs() > 0 && now >= e.expiresAtMs()) continue;
            active.add(e);
        }
        return active;
    }

    private static void save() {
        com.google.gson.JsonArray arr = new com.google.gson.JsonArray();
        for (BanEntry e : ENTRIES) {
            com.google.gson.JsonObject o = new com.google.gson.JsonObject();
            o.addProperty("name", e.name());
            if (e.uuid() != null) o.addProperty("uuid", e.uuid().toString());
            o.addProperty("reason", e.reason());
            o.addProperty("expiresAt", e.expiresAtMs());
            arr.add(o);
        }
        try {
            Files.writeString(FILE, new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(arr));
        } catch (IOException ex) {
            System.err.println("[BanList] 写入 banned-players.json 失败: " + ex.getMessage());
        }
    }
}
