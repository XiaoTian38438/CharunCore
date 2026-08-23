package com.CharunCore.server.network;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class MojangProfileService {

    public record ProfileProperty(String name, String value, String signature) {}

    public record ResolvedProfile(java.util.UUID uuid, String name, List<ProfileProperty> properties) {}

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final Map<String, CacheEntry> CACHE = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MS = 10 * 60 * 1000L;

    private record CacheEntry(long fetchedAt, Optional<ResolvedProfile> profile) {}

    private MojangProfileService() {}

    public static CompletableFuture<Optional<ResolvedProfile>> lookup(String name) {
        if (name == null || name.isEmpty() || name.length() > 16) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        CacheEntry cached = CACHE.get(name.toLowerCase());
        if (cached != null && System.currentTimeMillis() - cached.fetchedAt() < CACHE_TTL_MS) {
            return CompletableFuture.completedFuture(cached.profile());
        }
        return httpGet("https://api.mojang.com/users/profiles/minecraft/" + name)
                .orTimeout(6, java.util.concurrent.TimeUnit.SECONDS)
                .thenCompose(opt -> {
                    if (opt.isEmpty() || opt.get().isBlank()) {
                        return CompletableFuture.completedFuture(Optional.<ResolvedProfile>empty());
                    }
                    try {
                        com.google.gson.JsonObject obj = com.google.gson.JsonParser.parseString(opt.get()).getAsJsonObject();
                        String id = obj.get("id").getAsString();
                        String resolvedName = obj.has("name") ? obj.get("name").getAsString() : name;
                        java.util.UUID uuid = parseUndashed(id);
                        return fetchTextures(uuid).thenApply(props ->
                                Optional.of(new ResolvedProfile(uuid, resolvedName, props)));
                    } catch (Exception e) {
                        return CompletableFuture.completedFuture(Optional.<ResolvedProfile>empty());
                    }
                })
                .thenApply(profile -> {
                    CACHE.put(name.toLowerCase(), new CacheEntry(System.currentTimeMillis(), profile));
                    return profile;
                })
                .exceptionally(t -> Optional.empty());
    }

    public static CompletableFuture<List<ProfileProperty>> fetchTextures(java.util.UUID uuid) {
        String dashed = uuid.toString().replace("-", "");
        return httpGet("https://sessionserver.mojang.com/session/minecraft/profile/" + dashed + "?unsigned=false")
                .thenApply(opt -> {
                    List<ProfileProperty> empty = List.of();
                    if (opt.isEmpty() || opt.get().isBlank()) return empty;
                    try {
                        com.google.gson.JsonObject obj = com.google.gson.JsonParser.parseString(opt.get()).getAsJsonObject();
                        if (!obj.has("properties")) return empty;
                        com.google.gson.JsonArray arr = obj.getAsJsonArray("properties");
                        List<ProfileProperty> props = new java.util.ArrayList<>();
                        for (com.google.gson.JsonElement el : arr) {
                            com.google.gson.JsonObject p = el.getAsJsonObject();
                            String pname = p.get("name").getAsString();
                            String pvalue = p.get("value").getAsString();
                            String psig = p.has("signature") ? p.get("signature").getAsString() : null;
                            props.add(new ProfileProperty(pname, pvalue, psig));
                        }
                        return props;
                    } catch (Exception e) {
                        return empty;
                    }
                })
                .exceptionally(t -> List.of());
    }

    public static CompletableFuture<Optional<ResolvedProfile>> hasJoined(String username, String serverHash) {
        String url = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username="
                + urlEncode(username) + "&serverId=" + urlEncode(serverHash);
        return httpGet(url)
                .orTimeout(6, java.util.concurrent.TimeUnit.SECONDS)
                .thenApply(opt -> {
            if (opt.isEmpty() || opt.get().isBlank()) return Optional.<ResolvedProfile>empty();
            try {
                com.google.gson.JsonObject obj = com.google.gson.JsonParser.parseString(opt.get()).getAsJsonObject();
                java.util.UUID uuid = parseUndashed(obj.get("id").getAsString());
                String name = obj.get("name").getAsString();
                List<ProfileProperty> props = new java.util.ArrayList<>();
                if (obj.has("properties")) {
                    for (com.google.gson.JsonElement el : obj.getAsJsonArray("properties")) {
                        com.google.gson.JsonObject p = el.getAsJsonObject();
                        props.add(new ProfileProperty(
                                p.get("name").getAsString(),
                                p.get("value").getAsString(),
                                p.has("signature") ? p.get("signature").getAsString() : null));
                    }
                }
                return Optional.of(new ResolvedProfile(uuid, name, props));
            } catch (Exception e) {
                return Optional.<ResolvedProfile>empty();
            }
        });
    }

    private static CompletableFuture<Optional<String>> httpGet(String url) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(8))
                .header("User-Agent", "CharunCoreServer/1.21.11")
                .GET()
                .build();
        return HTTP.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(resp -> resp.statusCode() == 200 ? Optional.of(resp.body()) : Optional.<String>empty());
    }

    private static java.util.UUID parseUndashed(String id) {
        return java.util.UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16)
                + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
    }

    private static String urlEncode(String s) {
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }
}
