package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.RandomSource;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Bug15: 结构 JSON 的 pool_aliases(1.21 原版 PoolAliasBinding)。
 *  每个 structure start 生成时决议一次: direct 直映射 / random 加权随机 / random_group
 *  按组权重选一组并应用组内全部映射。试炼密室的刷怪笼内容池全靠它展开,
 *  曾未实现 -> spawner/contents/* 池找不到 -> 整条走廊分支不展开(规模过小)。 */
public final class PoolAliases {

    private PoolAliases() {}

    public interface Alias {
        void apply(Map<String, String> out, RandomSource rng);
    }

    public static List<Alias> parse(JsonArray arr) {
        List<Alias> out = new ArrayList<>();
        if (arr == null) return out;
        for (JsonElement el : arr) {
            JsonObject o = el.getAsJsonObject();
            String type = o.has("type") ? o.get("type").getAsString() : "minecraft:direct";
            type = strip(type);
            switch (type) {
                case "direct" -> out.add(direct(strip(o.get("alias").getAsString()),
                        strip(o.get("target").getAsString())));
                case "random" -> {
                    String alias = strip(o.get("alias").getAsString());
                    List<String> ids = new ArrayList<>();
                    List<Integer> weights = new ArrayList<>();
                    for (JsonElement t : o.getAsJsonArray("targets")) {
                        JsonObject to = t.getAsJsonObject();
                        ids.add(strip(to.get("data").getAsString()));
                        weights.add(to.has("weight") ? to.get("weight").getAsInt() : 1);
                    }
                    out.add(random(alias, ids, weights));
                }
                case "random_group" -> {
                    List<List<Alias>> groups = new ArrayList<>();
                    List<Integer> weights = new ArrayList<>();
                    for (JsonElement g : o.getAsJsonArray("groups")) {
                        JsonObject go = g.getAsJsonObject();
                        List<Alias> group = new ArrayList<>();
                        for (JsonElement a : go.getAsJsonArray("data")) {
                            group.addAll(parseSingle(a.getAsJsonObject()));
                        }
                        groups.add(group);
                        weights.add(go.has("weight") ? go.get("weight").getAsInt() : 1);
                    }
                    out.add(randomGroup(groups, weights));
                }
                default -> { }
            }
        }
        return out;
    }

    /** 解析单个 alias 绑定(direct/random)。 */
    private static List<Alias> parseSingle(JsonObject o) {
        List<Alias> out = new ArrayList<>();
        String type = o.has("type") ? o.get("type").getAsString() : "minecraft:direct";
        type = strip(type);
        switch (type) {
            case "direct" -> out.add(direct(strip(o.get("alias").getAsString()),
                    strip(o.get("target").getAsString())));
            case "random" -> {
                String alias = strip(o.get("alias").getAsString());
                List<String> ids = new ArrayList<>();
                List<Integer> weights = new ArrayList<>();
                for (JsonElement t : o.getAsJsonArray("targets")) {
                    JsonObject to = t.getAsJsonObject();
                    ids.add(strip(to.get("data").getAsString()));
                    weights.add(to.has("weight") ? to.get("weight").getAsInt() : 1);
                }
                out.add(random(alias, ids, weights));
            }
            default -> { }
        }
        return out;
    }

    public static Alias direct(String alias, String target) {
        return (out, rng) -> out.put(alias, target);
    }

    public static Alias random(String alias, List<String> ids, List<Integer> weights) {
        return (out, rng) -> {
            String pick = weightedPick(ids, weights, rng);
            if (pick != null) out.put(alias, pick);
        };
    }

    public static Alias randomGroup(List<List<Alias>> groups, List<Integer> weights) {
        return (out, rng) -> {
            int total = 0;
            for (int w : weights) total += Math.max(1, w);
            int roll = total > 0 ? rng.nextInt(total) : 0;
            for (int i = 0; i < groups.size(); i++) {
                roll -= Math.max(1, weights.get(i));
                if (roll < 0) {
                    for (Alias a : groups.get(i)) a.apply(out, rng);
                    return;
                }
            }
        };
    }

    private static String weightedPick(List<String> ids, List<Integer> weights, RandomSource rng) {
        int total = 0;
        for (int w : weights) total += Math.max(1, w);
        if (ids.isEmpty() || total <= 0) return null;
        int roll = rng.nextInt(total);
        for (int i = 0; i < ids.size(); i++) {
            roll -= Math.max(1, weights.get(i));
            if (roll < 0) return ids.get(i);
        }
        return ids.get(ids.size() - 1);
    }

    /** 每个 start 调用一次, 返回 alias->实际池 映射。 */
    public static java.util.Map<String, String> resolve(List<Alias> aliases, RandomSource rng) {
        Map<String, String> out = new java.util.HashMap<>();
        if (aliases != null) for (Alias a : aliases) a.apply(out, rng);
        return out;
    }

    private static String strip(String s) {
        return s != null && s.startsWith("minecraft:") ? s.substring(10) : s;
    }
}
