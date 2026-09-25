package com.CharunCore.server.worldgen.feature.vanilla;

import com.CharunCore.server.world.gen.RandomSource;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * IntProvider JSON 解析（minecraft:uniform / constant / biased_to_bottom / trapezoid /
 * clamped / weighted_list / 纯 int）。纯解析，无世界依赖。
 */
public final class IntProviders {

    public interface IntProvider {
        int sample(RandomSource rng);
    }

    private IntProviders() {}

    public static final IntProvider ZERO = (rng) -> 0;

    /** 支持纯整数或带 "type" 的对象；未知类型记入 unsupported 并返回常数 fallback。 */
    public static IntProvider parse(JsonElement el, Set<String> unsupported) {
        if (el == null || el.isJsonNull()) return ZERO;
        if (el.isJsonPrimitive()) {
            try { return of(el.getAsInt()); } catch (Exception e) { return ZERO; }
        }
        if (!el.isJsonObject()) return ZERO;
        JsonObject o = el.getAsJsonObject();
        String type = o.has("type") ? o.get("type").getAsString() : "minecraft:constant";
        type = strip(type);
        switch (type) {
            case "constant": return of(o.get("value").getAsInt());
            case "uniform": {
                int min = o.get("min_inclusive").getAsInt();
                int max = o.get("max_inclusive").getAsInt();
                return uniform(min, max);
            }
            case "biased_to_bottom": {
                int min = o.get("min_inclusive").getAsInt();
                int max = o.get("max_inclusive").getAsInt();
                return biasedToBottom(min, max);
            }
            case "very_biased_to_bottom": {
                int min = o.get("min_inclusive").getAsInt();
                int max = o.get("max_inclusive").getAsInt();
                return veryBiasedToBottom(min, max);
            }
            case "trapezoid": {
                int min = o.get("min_inclusive").getAsInt();
                int max = o.get("max_inclusive").getAsInt();
                int plateau = o.has("plateau") ? o.get("plateau").getAsInt() : 0;
                return trapezoid(min, max, plateau);
            }
            case "clamped": {
                IntProvider src = parse(o.get("source"), unsupported);
                int min = o.has("min") ? o.get("min").getAsInt() : o.get("min_inclusive").getAsInt();
                int max = o.has("max") ? o.get("max").getAsInt() : o.get("max_inclusive").getAsInt();
                return (rng) -> Math.max(min, Math.min(max, src.sample(rng)));
            }
            case "clamped_normal": {
                // RandomSource.nextGaussian 不可用，退化为区间均匀
                unsupported.add("int_provider.clamped_normal");
                int min = o.get("min_inclusive").getAsInt();
                int max = o.get("max_inclusive").getAsInt();
                return uniform(min, max);
            }
            case "weighted_list": {
                JsonArray dist = o.getAsJsonArray("distribution");
                List<IntProvider> items = new ArrayList<>();
                List<Integer> weights = new ArrayList<>();
                int total = 0;
                for (JsonElement e2 : dist) {
                    JsonObject d = e2.getAsJsonObject();
                    items.add(parse(d.get("data"), unsupported));
                    int w = d.get("weight").getAsInt();
                    weights.add(w);
                    total += w;
                }
                if (items.isEmpty() || total <= 0) return ZERO;
                int t = total;
                return (rng) -> {
                    int r = rng.nextInt(t);
                    for (int i = 0; i < items.size(); i++) {
                        r -= weights.get(i);
                        if (r < 0) return items.get(i).sample(rng);
                    }
                    return items.get(items.size() - 1).sample(rng);
                };
            }
            default:
                unsupported.add("int_provider." + type);
                return ZERO;
        }
    }

    public static String strip(String s) {
        return s != null && s.startsWith("minecraft:") ? s.substring(10) : s;
    }

    public static IntProvider of(final int v) { return (rng) -> v; }

    public static IntProvider uniform(final int min, final int max) {
        if (max <= min) return of(Math.min(min, max));
        return (rng) -> min + rng.nextInt(max - min + 1);
    }

    public static IntProvider biasedToBottom(final int min, final int max) {
        // 原版 BiasedToBottomHeight.sample（inner=0）
        if (max <= min) return of(Math.min(min, max));
        return (rng) -> {
            int n3 = rng.nextInt(max - min + 1);
            return rng.nextInt(n3 + 1) + min;
        };
    }

    public static IntProvider veryBiasedToBottom(final int min, final int max) {
        // 原版 VeryBiasedToBottomHeight.sample（inner=0 路径）
        if (max <= min) return of(Math.min(min, max));
        return (rng) -> {
            int n3 = between(rng, min, max);
            int n4 = between(rng, min, Math.max(n3 - 1, min));
            return between(rng, min, Math.max(n4 - 1, min));
        };
    }

    public static IntProvider trapezoid(final int min, final int max, final int plateau) {
        if (max <= min) return of(Math.min(min, max));
        return (rng) -> {
            int span = max - min;
            if (plateau >= span) return between(rng, min, max);
            int halfA = (span - plateau) / 2;
            int halfB = span - halfA;
            return min + between(rng, 0, halfB) + between(rng, 0, halfA);
        };
    }

    static int between(RandomSource rng, int min, int max) {
        if (max <= min) return min;
        return min + rng.nextInt(max - min + 1);
    }

    /** 从对象读取可选 IntProvider 字段，缺省返回 def。 */
    public static IntProvider opt(JsonObject o, String key, int def, Set<String> unsupported) {
        if (!o.has(key)) return of(def);
        return parse(o.get(key), unsupported);
    }
}
