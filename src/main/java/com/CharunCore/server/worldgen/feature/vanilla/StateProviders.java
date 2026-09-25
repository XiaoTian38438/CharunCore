package com.CharunCore.server.worldgen.feature.vanilla;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.RandomSource;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/** JSON 驱动的 BlockStateProvider / IntProvider 解析（对应原版 worldgen/stateprovider）。 */
public final class StateProviders {

    private static final SortedSet<String> UNSUPPORTED =
        Collections.synchronizedSortedSet(new TreeSet<>());

    private StateProviders() {
    }

    static void recordUnsupported(String field) {
        UNSUPPORTED.add(field);
    }

    public static Set<String> unsupportedFields() {
        return Collections.unmodifiableSortedSet(UNSUPPORTED);
    }

    public interface IntProvider {
        int sample(RandomSource rng);
    }

    public interface StateProvider {
        int get(RandomSource rng, int x, int y, int z);
    }

    /** 支持: 常量 int / uniform(min_inclusive,max_inclusive, type 可省略) / weighted_list。 */
    public static IntProvider parseIntProvider(JsonElement el, String what) {
        if (el == null || el.isJsonNull()) {
            recordUnsupported(what);
            return rng -> 0;
        }
        if (el.isJsonPrimitive() && el.getAsJsonPrimitive().isNumber()) {
            final int c = el.getAsInt();
            return rng -> c;
        }
        if (!el.isJsonObject()) {
            recordUnsupported(what);
            return rng -> 0;
        }
        JsonObject o = el.getAsJsonObject();
        String type = o.has("type") ? o.get("type").getAsString() : "minecraft:uniform";
        switch (shortName(type)) {
            case "uniform": {
                int min = o.has("min_inclusive") ? o.get("min_inclusive").getAsInt()
                    : (o.has("min") ? o.get("min").getAsInt() : 0);
                int max = o.has("max_inclusive") ? o.get("max_inclusive").getAsInt()
                    : (o.has("max") ? o.get("max").getAsInt() : min);
                if (max < min) max = min;
                final int lo = min, hi = max;
                return rng -> lo + rng.nextInt(hi - lo + 1);
            }
            case "weighted_list": {
                List<IntProvider> items = new ArrayList<>();
                List<Integer> weights = new ArrayList<>();
                int total = 0;
                if (o.has("distribution")) {
                    for (JsonElement e : o.getAsJsonArray("distribution")) {
                        JsonObject d = e.getAsJsonObject();
                        IntProvider p = parseIntProvider(d.get("data"), what + ".data");
                        int w = d.has("weight") ? d.get("weight").getAsInt() : 1;
                        if (w < 1) w = 1;
                        items.add(p);
                        weights.add(w);
                        total += w;
                    }
                }
                if (items.isEmpty()) {
                    recordUnsupported(what);
                    return rng -> 0;
                }
                final int sum = total;
                IntProvider[] arr = items.toArray(new IntProvider[0]);
                int[] ws = new int[weights.size()];
                for (int i = 0; i < ws.length; i++) ws[i] = weights.get(i);
                return rng -> {
                    int roll = rng.nextInt(sum);
                    for (int i = 0; i < arr.length; i++) {
                        roll -= ws[i];
                        if (roll < 0) return arr[i].sample(rng);
                    }
                    return arr[arr.length - 1].sample(rng);
                };
            }
            case "constant": {
                int c = o.has("value") ? o.get("value").getAsInt() : 0;
                final int v = c;
                return rng -> v;
            }
            default:
                recordUnsupported(what + ":" + shortName(type));
                return rng -> 0;
        }
    }

    /** 支持 simple_state_provider / weighted_state_provider / randomized_int_state_provider。 */
    public static StateProvider parseStateProvider(JsonElement el, String what) {
        if (el == null || !el.isJsonObject()) {
            recordUnsupported(what);
            return (rng, x, y, z) -> 0;
        }
        JsonObject o = el.getAsJsonObject();
        String type = o.has("type") ? o.get("type").getAsString() : "minecraft:simple_state_provider";
        switch (shortName(type)) {
            case "simple_state_provider": {
                final int state = stateFromJson(o.getAsJsonObject("state"));
                return (rng, x, y, z) -> state;
            }
            case "weighted_state_provider": {
                List<Integer> states = new ArrayList<>();
                List<Integer> weights = new ArrayList<>();
                int total = 0;
                if (o.has("entries")) {
                    for (JsonElement e : o.getAsJsonArray("entries")) {
                        JsonObject en = e.getAsJsonObject();
                        int st = stateFromJson(en.getAsJsonObject("data"));
                        int w = en.has("weight") ? en.get("weight").getAsInt() : 1;
                        if (w < 1) w = 1;
                        states.add(st);
                        weights.add(w);
                        total += w;
                    }
                }
                if (states.isEmpty()) {
                    recordUnsupported(what);
                    return (rng, x, y, z) -> 0;
                }
                final int sum = total;
                int[] sts = new int[states.size()];
                int[] ws = new int[weights.size()];
                for (int i = 0; i < sts.length; i++) {
                    sts[i] = states.get(i);
                    ws[i] = weights.get(i);
                }
                return (rng, x, y, z) -> {
                    int roll = rng.nextInt(sum);
                    for (int i = 0; i < sts.length; i++) {
                        roll -= ws[i];
                        if (roll < 0) return sts[i];
                    }
                    return sts[sts.length - 1];
                };
            }
            case "randomized_int_state_provider": {
                StateProvider source = parseStateProvider(o.get("source"), what + ".source");
                String prop = o.has("property") ? o.get("property").getAsString() : null;
                IntProvider values = parseIntProvider(o.get("values"), what + ".values");
                return (rng, x, y, z) -> {
                    int st = source.get(rng, x, y, z);
                    if (st == 0 || prop == null) return st;
                    return BlockStateHelper.withProp(st, prop, String.valueOf(values.sample(rng)));
                };
            }
            default:
                recordUnsupported(what + ":" + shortName(type));
                return (rng, x, y, z) -> 0;
        }
    }

    /** {Name, Properties} -> stateId；名字去 minecraft: 前缀后查 blocks.json。 */
    public static int stateFromJson(JsonObject state) {
        if (state == null || !state.has("Name")) return 0;
        String name = state.get("Name").getAsString();
        String bare = shortName(name);
        int st;
        if (state.has("Properties") && state.get("Properties").isJsonObject()) {
            java.util.Map<String, String> props = new java.util.LinkedHashMap<>();
            JsonObject p = state.getAsJsonObject("Properties");
            for (java.util.Map.Entry<String, JsonElement> e : p.entrySet()) {
                props.put(e.getKey(), e.getValue().getAsString());
            }
            st = BlockStateHelper.getState(bare, props);
            if (st == 0) st = BlockStateHelper.getDefault(bare);
        } else {
            st = BlockStateHelper.getDefault(bare);
        }
        return st;
    }

    public static String shortName(String id) {
        int i = id.indexOf(':');
        return i >= 0 ? id.substring(i + 1) : id;
    }
}
