package com.CharunCore.server.worldgen.feature.vanilla;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.worldgen.WorldGenLevel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * block predicate JSON 解析与执行。
 * 同时兼容两种方言：BlockPredicate（"type": matching_blocks / all_of / ...）
 * 与 RulePredicate（"predicate_type": block_match / tag_match，用于 ore target 等）。
 */
public final class BlockPredicates {

    public interface BlockPredicate {
        boolean test(WorldGenLevel level, int x, int y, int z);
    }

    private static final Map<String, Set<String>> BLOCK_TAG_CACHE = new ConcurrentHashMap<>();
    static final String TAG_DIR = "json/minecraft/tags/block/";

    private BlockPredicates() {}

    public static String strip(String s) {
        return s != null && s.startsWith("minecraft:") ? s.substring(10) : s;
    }

    /** 未知/不支持的谓词一律放行（安全跳过），仅记录。 */
    public static BlockPredicate parse(JsonElement el, Set<String> unsupported) {
        if (el == null || el.isJsonNull()) return (l, x, y, z) -> true;
        if (!el.isJsonObject()) return (l, x, y, z) -> true;
        JsonObject o = el.getAsJsonObject();
        String type = o.has("type")
            ? strip(o.get("type").getAsString())
            : (o.has("predicate_type") ? strip(o.get("predicate_type").getAsString()) : "true");
        switch (type) {
            case "true":
            case "always_true":
                return (l, x, y, z) -> true;
            case "false":
                return (l, x, y, z) -> false;
            case "all_of": {
                JsonArray arr = o.getAsJsonArray("predicates");
                BlockPredicate[] ps = arr.size() == 0 ? new BlockPredicate[0]
                    : StreamSupport.stream(arr.spliterator(), false)
                        .map(e -> parse(e, unsupported)).toArray(BlockPredicate[]::new);
                return (l, x, y, z) -> {
                    for (BlockPredicate p : ps) if (!p.test(l, x, y, z)) return false;
                    return true;
                };
            }
            case "any_of": {
                JsonArray arr = o.getAsJsonArray("predicates");
                BlockPredicate[] ps = arr.size() == 0 ? new BlockPredicate[0]
                    : StreamSupport.stream(arr.spliterator(), false)
                        .map(e -> parse(e, unsupported)).toArray(BlockPredicate[]::new);
                return (l, x, y, z) -> {
                    for (BlockPredicate p : ps) if (p.test(l, x, y, z)) return true;
                    return false;
                };
            }
            case "not": {
                BlockPredicate inner = parse(o.get("predicate"), unsupported);
                return (l, x, y, z) -> !inner.test(l, x, y, z);
            }
            case "matching_blocks":
            case "block_match": {
                int[] off = offset(o);
                Set<Integer> ids = new HashSet<>();
                JsonElement b = o.has("blocks") ? o.get("blocks") : o.get("block");
                if (b != null && b.isJsonArray()) {
                    for (JsonElement e : b.getAsJsonArray()) addId(ids, e.getAsString());
                } else if (b != null) {
                    addId(ids, b.getAsString());
                }
                int[] idArr = ids.stream().mapToInt(Integer::intValue).toArray();
                return (l, x, y, z) -> {
                    int cur = l.getBlock(x + off[0], y + off[1], z + off[2]);
                    for (int id : idArr) if (cur == id) return true;
                    return false;
                };
            }
            case "matching_block_tag":
            case "tag_match": {
                int[] off = offset(o);
                String tag = o.get("tag").getAsString();
                Set<String> names = blockTag(strip(tag));
                return (l, x, y, z) -> {
                    int cur = l.getBlock(x + off[0], y + off[1], z + off[2]);
                    String n = name(cur);
                    return n != null && names.contains(n);
                };
            }
            case "matching_fluids": {
                int[] off = offset(o);
                Set<String> fluids = new HashSet<>();
                JsonElement f = o.get("fluids");
                if (f != null && f.isJsonArray()) {
                    for (JsonElement e : f.getAsJsonArray()) fluids.add(strip(e.getAsString()));
                } else if (f != null) {
                    fluids.add(strip(f.getAsString()));
                }
                return (l, x, y, z) -> {
                    String n = name(l.getBlock(x + off[0], y + off[1], z + off[2]));
                    if (n == null) return false;
                    for (String fl : fluids) {
                        if ("water".equals(fl) && isWaterName(n)) return true;
                        if ("lava".equals(fl) && isLavaName(n)) return true;
                        if (n.equals(fl)) return true;
                    }
                    return false;
                };
            }
            case "would_survive": {
                // 近似：目标格可进入（air/replaceable）且下方为固体顶面
                JsonObject st = o.has("state") ? o.getAsJsonObject("state") : null;
                int stateId = st != null ? StateProviders.stateFromJson(st) : 0;
                boolean plantLike = plantLike(name(stateId));
                return (l, x, y, z) -> canEnter(l.getBlock(x, y, z))
                    && hasSolidTop(l, x, y - 1, z)
                    && (!plantLike || stateId != 0);
            }
            case "replaceable":
            case "replaceable_matching": {
                int[] off = offset(o);
                return (l, x, y, z) ->
                    canEnter(l.getBlock(x + off[0], y + off[1], z + off[2]));
            }
            case "solid": {
                int[] off = offset(o);
                return (l, x, y, z) ->
                    BlockStateHelper.isSolidOpaque(l.getBlock(x + off[0], y + off[1], z + off[2]));
            }
            case "has_sturdy_face": {
                String dir = o.has("direction") ? o.get("direction").getAsString() : "up";
                int[] d = dirOffset(dir);
                return (l, x, y, z) ->
                    BlockStateHelper.isSolidOpaque(
                        l.getBlock(x + d[0], y + d[1], z + d[2]));
            }
            case "inside_world_bounds": {
                int[] off = offset(o);
                return (l, x, y, z) -> {
                    int yy = y + off[1];
                    return yy >= l.getMinY() && yy < l.getMinY() + l.getHeight();
                };
            }
            default:
                // heightmap 等 JSON 中出现的其余类型安全跳过
                unsupported.add("predicate." + type);
                return (l, x, y, z) -> true;
        }
    }

    private static void addId(Set<Integer> ids, String name) {
        int id = BlockStateHelper.getDefault(strip(name));
        if (id != 0 || "air".equals(strip(name))) {
            if ("air".equals(strip(name))) { ids.add(0); ids.add(BlockStateHelper.getDefault("cave_air")); }
            else ids.add(id);
        }
    }

    private static int[] offset(JsonObject o) {
        if (!o.has("offset")) return new int[]{0, 0, 0};
        JsonArray a = o.getAsJsonArray("offset");
        return new int[]{a.get(0).getAsInt(), a.get(1).getAsInt(), a.get(2).getAsInt()};
    }

    private static int[] dirOffset(String dir) {
        return switch (dir) {
            case "down" -> new int[]{0, -1, 0};
            case "up" -> new int[]{0, 1, 0};
            case "north" -> new int[]{0, 0, -1};
            case "south" -> new int[]{0, 0, 1};
            case "west" -> new int[]{-1, 0, 0};
            case "east" -> new int[]{1, 0, 0};
            default -> new int[]{0, 0, 0};
        };
    }

    public static String name(int stateId) {
        String n = BlockStateHelper.getName(stateId);
        return n == null ? null : strip(n);
    }

    public static boolean isWaterName(String n) {
        return n != null && (n.equals("water") || n.equals("flowing_water")
            || n.equals("bubble_column") || n.equals("kelp") || n.equals("seagrass")
            || n.equals("tall_seagrass"));
    }

    public static boolean isLavaName(String n) { return n != null && n.contains("lava"); }

    /** 目标格是否可被 feature 进入（空气或原版可替换方块）。 */
    public static boolean canEnter(int stateId) {
        if (stateId == 0) return true;
        String n = name(stateId);
        if (n == null) return false;
        if (n.contains("air")) return true;
        return BlockStateHelper.isReplaceable(n);
    }

    /** 近似 canSurvive 的支撑判定：下方为固体完整方块。 */
    public static boolean hasSolidTop(WorldGenLevel l, int x, int y, int z) {
        return BlockStateHelper.isSolidOpaque(l.getBlock(x, y, z));
    }

    public static boolean plantLike(String n) {
        if (n == null) return false;
        return BlockStateHelper.isReplaceable(n)
            || n.endsWith("_sapling") || n.endsWith("_propagule")
            || n.endsWith("_fungus") || n.endsWith("_coral")
            || n.contains("dripleaf") || n.equals("dead_bush")
            || n.equals("bamboo") || n.equals("sugar_cane")
            || n.equals("glow_lichen") || n.equals("vine")
            || n.endsWith("_sign");
    }

    public static boolean liquid(int stateId) {
        String n = name(stateId);
        return n != null && (isWaterName(n) || isLavaName(n));
    }

    /** 方块 tag 递归解析（json/minecraft/tags/block/<name>.json）。 */
    public static Set<String> blockTag(String tagNameNoPrefix) {
        return BLOCK_TAG_CACHE.computeIfAbsent(tagNameNoPrefix, t -> resolveTag(t, 0));
    }

    private static Set<String> resolveTag(String tag, int depth) {
        Set<String> out = new HashSet<>();
        if (depth > 5) return out;
        File f = new File(TAG_DIR + tag + ".json");
        if (!f.exists()) {
            // 兼容子目录 tag（如 wool 是单文件；shulker_boxes 等在根目录）
            return out;
        }
        try (FileInputStream fis = new FileInputStream(f);
             InputStreamReader r = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            JsonObject json = com.google.gson.JsonParser.parseReader(r).getAsJsonObject();
            if (!json.has("values")) return out;
            for (JsonElement e : json.getAsJsonArray("values")) {
                String v = e.getAsString();
                if (v.startsWith("#")) {
                    out.addAll(resolveTag(strip(v.substring(1)), depth + 1));
                } else {
                    out.add(strip(v));
                }
            }
        } catch (Exception ignored) {}
        return out;
    }
}
