package com.CharunCore.server.worldgen.feature.vanilla;

import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 特征管线胶水层：placed_feature 入口、上下文、configured_feature 引用执行。
 * 与同包 PlacementModifiers / FeatureTypes / StateProviders / BlockPredicates 配套。
 */
public final class FeaturePipeline {

    public static final Set<String> UNSUPPORTED = ConcurrentHashMap.newKeySet();

    private FeaturePipeline() {}

    public static void unsupported(String what) {
        UNSUPPORTED.add(what);
    }

    public static java.util.Set<String> unsupportedTypes() {
        return UNSUPPORTED;
    }

    /** 放置执行上下文（public final 字段，便于同包/跨类直接访问）。 */
    public static final class Ctx {
        public final WorldGenLevel level;
        public final RandomSource rng;
        public final int chunkOriginX, chunkOriginZ;
        public final java.util.function.IntBinaryOperator biomeAt;

        public Ctx(WorldGenLevel level, RandomSource rng, int chunkOriginX, int chunkOriginZ,
                   java.util.function.IntBinaryOperator biomeAt) {
            this.level = level;
            this.rng = rng;
            this.chunkOriginX = chunkOriginX;
            this.chunkOriginZ = chunkOriginZ;
            this.biomeAt = biomeAt;
        }

        public int biomeIdAt(int blockX, int blockZ) { return biomeAt.applyAsInt(blockX, blockZ); }

        public int minY() { return level.getMinY(); }
        public int maxY() { return level.getMinY() + level.getHeight() - 1; }
        public int genHeight() { return level.getHeight(); }
        public final java.util.Map<Long, Integer> columnMemo = new java.util.HashMap<>();
    }

    private static final Map<String, JsonObject> PLACED_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, JsonObject> CONFIGURED_CACHE = new ConcurrentHashMap<>();

    /**
     * 执行一个 placed feature（懒加载 json/minecraft/worldgen/placed_feature/<name>.json）。
     */
    public static boolean place(String placedName, WorldGenLevel level,
                                int chunkOriginX, int chunkOriginZ,
                                RandomSource rng,
                                java.util.function.IntBinaryOperator biomeAt) {
        Ctx ctx = new Ctx(level, rng, chunkOriginX, chunkOriginZ, biomeAt);
        return placePlaced(placedName.startsWith("minecraft:") ? placedName.substring(10) : placedName, ctx);
    }

    static boolean placePlaced(String name, Ctx ctx) {
        JsonObject root = placedJson(name);
        if (root == null) {
            unsupported("missing_placed." + name);
            return false;
        }
        JsonElement featureEl = root.get("feature");
        JsonArray placement = root.has("placement") ? root.getAsJsonArray("placement") : new com.google.gson.JsonArray();
        List<PlacementModifiers.Op> ops = PlacementModifiers.compile(placement, UNSUPPORTED);
        List<int[]> pts = new ArrayList<>();
        pts.add(new int[]{ctx.chunkOriginX, ctx.level.getMinY(), ctx.chunkOriginZ});
        for (PlacementModifiers.Op op : ops) {
            pts = op.apply(pts, ctx);
            if (pts.isEmpty()) return false;
        }
        boolean any = false;
        for (int[] p : pts) {
            if (p[1] < ctx.level.getMinY() || p[1] >= ctx.level.getMinY() + ctx.level.getHeight()) continue;
            if (executeConfigured(featureEl, ctx, p[0], p[1], p[2])) any = true;
        }
        return any;
    }

    /** 执行 configured 引用（内联对象或字符串名）。返回是否放置了方块。 */
    @SuppressWarnings("unused")
    static boolean executeConfigured(JsonElement featureEl, Ctx ctx, int x, int y, int z) {
        if (featureEl == null || featureEl.isJsonNull()) return false;
        if (featureEl.isJsonPrimitive()) {
            String name = strip(featureEl.getAsString());
            JsonObject cfgFile = configuredJson(name);
            if (cfgFile == null) {
                unsupported("missing_configured." + name);
                return false;
            }
            return execConfiguredObj(cfgFile, ctx, x, y, z);
        }
        return execConfiguredObj(featureEl.getAsJsonObject(), ctx, x, y, z);
    }

    static boolean execConfiguredObj(JsonObject obj, Ctx ctx, int x, int y, int z) {
        String type = strip(obj.has("type") ? obj.get("type").getAsString() : "");
        JsonObject cfg = obj.has("config") ? obj.getAsJsonObject("config") : new JsonObject();
        return FeatureTypes.place(type, cfg, ctx, x, y, z);
    }

    /** FeatureTypes 回调：执行 placed 引用（random_patch 等内部也可能引用 placed）。 */
    public static boolean executePlacedRef(JsonElement el, Ctx ctx, int x, int y, int z) {
        if (el == null || el.isJsonNull()) return false;
        // 内联 configured 对象（random_patch 的 feature 字段）
        if (el.isJsonObject() && el.getAsJsonObject().has("type")) {
            return execConfiguredObj(el.getAsJsonObject(), ctx, x, y, z);
        }
        if (el.isJsonObject() && el.getAsJsonObject().has("feature")) {
            return executeConfigured(el.getAsJsonObject().get("feature"), ctx, x, y, z);
        }
        if (el.isJsonPrimitive()) {
            String name = strip(el.getAsString());
            JsonObject cfgFile = configuredJson(name);
            if (cfgFile != null) return execConfiguredObj(cfgFile, ctx, x, y, z);
            // 可能是 placed_feature 引用（如 fancy_oak_checked）
            if (placedJson(name) != null) return placePlaced(name, ctx);
            unsupported("missing_configured." + name);
            return false;
        }
        return false;
    }

    /** 树分发（FeatureTypes 调用）；cfg 为 tree 的 config 对象。 */
    public static boolean dispatchTree(String type, JsonObject cfg, Ctx ctx, int x, int y, int z) {
        return TreeFeatureVanilla.place(cfg, ctx.level, x, y, z, ctx.rng);
    }

    static JsonObject placedJson(String name) {
        return PLACED_CACHE.computeIfAbsent(name, n -> read("json/minecraft/worldgen/placed_feature/" + n + ".json"));
    }

    static JsonObject configuredJson(String name) {
        return CONFIGURED_CACHE.computeIfAbsent(name, n -> read("json/minecraft/worldgen/configured_feature/" + n + ".json"));
    }

    private static JsonObject read(String path) {
        try (FileInputStream fis = new FileInputStream(path);
             InputStreamReader r = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(r).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    static String strip(String s) {
        return s.startsWith("minecraft:") ? s.substring(10) : s;
    }
}
