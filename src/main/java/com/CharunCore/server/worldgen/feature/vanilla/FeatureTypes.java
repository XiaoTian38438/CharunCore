package com.CharunCore.server.worldgen.feature.vanilla;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.RegistryHelper;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;
import com.CharunCore.server.worldgen.feature.vanilla.FeaturePipeline.Ctx;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * configured_feature 的 "feature" 字段分发与执行。
 * 各实现对照 mapping/cfr-source/net/minecraft/world/level/levelgen/feature/*.java，
 * 做了窗口级简化（越界写由 WorldGenLevel.setBlock 静默丢弃）。
 */
public final class FeatureTypes {

    private static final Set<Integer> FEATURES_CANNOT_REPLACE = new HashSet<>();
    private static final Set<String> DIRT_NAMES = Set.of(
        "dirt", "grass_block", "podzol", "coarse_dirt", "mycelium", "moss_block",
        "mud", "muddy_mangrove_roots");
    private static final Set<String> STONE_NAMES = Set.of(
        "stone", "granite", "diorite", "andesite", "deepslate", "tuff");
    private static volatile BitSet coldBiomes;

    static {
        for (String n : new String[]{"bedrock", "chest", "spawner",
            "end_portal_frame", "end_portal"}) {
            FEATURES_CANNOT_REPLACE.add(BlockStateHelper.getDefault(n));
        }
    }

    private FeatureTypes() {}

    /** 执行一个 configured feature。返回是否放置了方块。 */
    public static boolean place(String typeRaw, JsonObject cfg, Ctx ctx, int x, int y, int z) {
        String type = StateProviders.shortName(typeRaw == null ? "" : typeRaw);
        WorldGenLevel l = ctx.level;
        RandomSource rng = ctx.rng;
        try {
            return switch (type) {
                case "simple_block" -> simpleBlock(cfg, ctx, x, y, z);
                case "random_patch", "flower", "no_bonemeal_flower" -> patch(cfg, ctx, x, y, z);
                case "block_column" -> blockColumn(cfg, ctx, x, y, z);
                case "disk" -> disk(cfg, ctx, x, y, z);
                case "lake" -> lake(cfg, ctx, x, y, z);
                case "spring_feature" -> spring(cfg, ctx, x, y, z);
                case "freeze_top_layer" -> freezeTopLayer(ctx, x, z);
                case "forest_rock" -> forestRock(cfg, ctx, x, y, z);
                case "multiface_growth", "glow_lichen" -> multifaceGrowth(cfg, ctx, x, y, z);
                case "monster_room" -> monsterRoom(ctx, x, y, z);
                case "bamboo" -> bamboo(cfg, ctx, x, y, z);
                case "ore" -> ore(cfg, ctx, x, y, z, false);
                case "scattered_ore" -> ore(cfg, ctx, x, y, z, true);
                case "random_selector" -> randomSelector(cfg, ctx, x, y, z);
                case "simple_random_selector" -> simpleRandomSelector(cfg, ctx, x, y, z);
                case "random_boolean_selector" -> randomBooleanSelector(cfg, ctx, x, y, z);
                case "tree", "azalea_tree", "fallen_tree" ->
                    FeaturePipeline.dispatchTree(type, cfg, ctx, x, y, z);
                default -> {
                    FeaturePipeline.unsupported("feature." + type);
                    yield false;
                }
            };
        } catch (Throwable t) {
            FeaturePipeline.unsupported("feature_error." + type);
            return false;
        }
    }

    // ==================== 状态提供器包装 ====================

    /** 兼容 rotated_block_provider 与 rule_based（disk 内联形态），其余委托既有解析器。 */
    public static StateProviders.StateProvider wrapProvider(JsonElement el, String what) {
        if (el == null || el.isJsonNull()) {
            return (rng, xx, yy, zz) -> 0;
        }
        if (!el.isJsonObject()) return (rng, xx, yy, zz) -> 0;
        JsonObject o = el.getAsJsonObject();
        if (o.has("rules") && o.has("fallback")) return ruleBased(o, what);
        String t = o.has("type") ? StateProviders.shortName(o.get("type").getAsString()) : "";
        if ("rotated_block_provider".equals(t)) {
            int base = StateProviders.stateFromJson(o.getAsJsonObject("state"));
            if (base == 0) return (rng, xx, yy, zz) -> 0;
            final int b = base;
            return (rng, xx, yy, zz) ->
                BlockStateHelper.withProp(b, "axis", rng.nextBoolean() ? "x" : "z");
        }
        if ("noise_provider".equals(t) || "noise_threshold_provider".equals(t)
            || "dual_noise_provider".equals(t)) {
            FeaturePipeline.unsupported("state_provider." + t);
            int fb = o.has("state") ? StateProviders.stateFromJson(o.getAsJsonObject("state"))
                : (o.has("default_state")
                    ? StateProviders.stateFromJson(o.getAsJsonObject("default_state")) : 0);
            final int f = fb;
            return (rng, xx, yy, zz) -> f;
        }
        return StateProviders.parseStateProvider(el, what);
    }

    private static StateProviders.StateProvider ruleBased(JsonObject o, String what) {
        record Rule(int prio, BlockPredicates.BlockPredicate cond, int state) {}
        List<Rule> rules = new ArrayList<>();
        for (JsonElement e : o.getAsJsonArray("rules")) {
            JsonObject r = e.getAsJsonObject();
            int prio = r.has("priority") ? r.get("priority").getAsInt() : 0;
            BlockPredicates.BlockPredicate cond =
                BlockPredicates.parse(r.has("if_true") ? r.get("if_true") : r.get("input_predicate"),
                    FeaturePipeline.UNSUPPORTED);
            int st = r.has("state")
                ? StateProviders.stateFromJson(r.getAsJsonObject("state"))
                : (r.has("output_state")
                    ? StateProviders.stateFromJson(r.getAsJsonObject("output_state")) : 0);
            rules.add(new Rule(prio, cond, st));
        }
        rules.sort((a, b) -> Integer.compare(b.prio(), a.prio()));
        StateProviders.StateProvider fallback = wrapProvider(o.get("fallback"), what + ".fallback");
        List<Rule> rs = rules;
        return (rng, xx, yy, zz) -> {
            for (Rule r : rs) if (r.cond().test(null, xx, yy, zz)) return r.state();
            return fallback.get(rng, xx, yy, zz);
        };
    }

    private static StateProviders.StateProvider field(JsonObject cfg, String key, String defBlock) {
        if (cfg != null && cfg.has(key)) return wrapProvider(cfg.get(key), key);
        final int def = defBlock == null ? 0 : BlockStateHelper.getDefault(defBlock);
        return (rng, x, y, z) -> def;
    }

    // ==================== simple_block ====================

    static boolean simpleBlock(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        WorldGenLevel l = ctx.level;
        StateProviders.StateProvider sp =
            cfg != null && cfg.has("to_place")
                ? wrapProvider(cfg.get("to_place"), "simple_block.to_place")
                : (rng2, xx, yy, zz) -> 0;
        int st = sp.get(ctx.rng, x, y, z);
        if (st == 0) return false;
        String nm = BlockPredicates.name(st);
        if (!BlockPredicates.canEnter(l.getBlock(x, y, z))) return false;
        // 双高植物：上下两格 half 属性分开落
        if (nm.equals("tall_grass") || nm.equals("large_fern") || nm.equals("sunflower")
            || nm.equals("lilac") || nm.equals("rose_bush") || nm.equals("peony")) {
            if (!BlockPredicates.canEnter(l.getBlock(x, y + 1, z))) return false;
            int lower = BlockStateHelper.withProp(st, "half", "lower");
            int upper = BlockStateHelper.withProp(st, "half", "upper");
            l.setBlock(x, y, z, lower != 0 ? lower : st);
            l.setBlock(x, y + 1, z, upper != 0 ? upper : st);
            return true;
        }
        // 可存活近似：植物类需要下方固体顶面
        if ((BlockPredicates.plantLike(nm) || nm.endsWith("_sapling"))
            && !nm.equals("glow_lichen") && !nm.contains("vine")
            && !BlockPredicates.hasSolidTop(l, x, y - 1, z)) {
            return false;
        }
        l.setBlock(x, y, z, st);
        return true;
    }

    // ==================== random_patch / flower ====================

    static boolean patch(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        if (cfg == null || !cfg.has("feature")) return false;
        int tries = cfg.has("tries") ? cfg.get("tries").getAsInt() : 32;
        int xs = cfg.has("xz_spread") ? cfg.get("xz_spread").getAsInt()
            : (cfg.has("x_spread") ? cfg.get("x_spread").getAsInt() : 7);
        int zs = cfg.has("xz_spread") ? cfg.get("xz_spread").getAsInt()
            : (cfg.has("z_spread") ? cfg.get("z_spread").getAsInt() : 7);
        int ys = cfg.has("y_spread") ? cfg.get("y_spread").getAsInt() : 3;
        List<BlockPredicates.BlockPredicate> whitelist = predicateList(cfg.get("whitelist"));
        List<BlockPredicates.BlockPredicate> blacklist = predicateList(cfg.get("blacklist"));
        boolean any = false;
        for (int t = 0; t < tries; t++) {
            int px = x + ctx.rng.nextInt(xs + 1) - ctx.rng.nextInt(xs + 1);
            int py = y + ctx.rng.nextInt(ys + 1) - ctx.rng.nextInt(ys + 1);
            int pz = z + ctx.rng.nextInt(zs + 1) - ctx.rng.nextInt(zs + 1);
            boolean ok = true;
            for (BlockPredicates.BlockPredicate p : whitelist) {
                if (!p.test(ctx.level, px, py, pz)) { ok = false; break; }
            }
            for (BlockPredicates.BlockPredicate p : blacklist) {
                if (ok && p.test(ctx.level, px, py, pz)) { ok = false; break; }
            }
            if (!ok) continue;
            if (FeaturePipeline.executePlacedRef(cfg.get("feature"), ctx, px, py, pz)) any = true;
        }
        return any;
    }

    private static List<BlockPredicates.BlockPredicate> predicateList(JsonElement el) {
        List<BlockPredicates.BlockPredicate> out = new ArrayList<>();
        if (el != null && el.isJsonArray()) {
            for (JsonElement e : el.getAsJsonArray()) {
                out.add(BlockPredicates.parse(e, FeaturePipeline.UNSUPPORTED));
            }
        }
        return out;
    }

    // ==================== block_column ====================

    static boolean blockColumn(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        if (cfg == null || !cfg.has("layers")) return false;
        boolean up = !"down".equals(
            cfg.has("direction") ? cfg.get("direction").getAsString() : "up");
        int dy = up ? 1 : -1;
        BlockPredicates.BlockPredicate allowed = cfg.has("allowed_placement")
            ? BlockPredicates.parse(cfg.get("allowed_placement"), FeaturePipeline.UNSUPPORTED)
            : (l, xx, yy, zz) -> true;
        boolean prioritizeTip = cfg.has("prioritize_tip") && cfg.get("prioritize_tip").getAsBoolean();
        JsonArray layers = cfg.getAsJsonArray("layers");
        int n = layers.size();
        int[] heights = new int[n];
        StateProviders.StateProvider[] providers = new StateProviders.StateProvider[n];
        int total = 0;
        for (int i = 0; i < n; i++) {
            JsonObject lo = layers.get(i).getAsJsonObject();
            heights[i] = Math.max(0, IntProviders.parse(lo.get("height"),
                FeaturePipeline.UNSUPPORTED).sample(ctx.rng));
            providers[i] = wrapProvider(lo.get("provider"), "block_column.layer");
            total += heights[i];
        }
        if (total == 0) return false;
        int cx = x, cy = y + dy, cz = z;
        for (int i = 0; i < total; i++) {
            if (!allowed.test(ctx.level, cx, cy, cz)) {
                truncate(heights, total, i, prioritizeTip);
                break;
            }
            cy += dy;
        }
        WorldGenLevel l = ctx.level;
        int wy = y;
        boolean placed = false;
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < heights[i]; k++) {
                if (providers[i] != null) {
                    int st = providers[i].get(ctx.rng, x, wy, z);
                    if (st != 0) {
                        l.setBlock(x, wy, z, st);
                        placed = true;
                    }
                }
                wy += dy;
            }
        }
        return placed;
    }

    /** 原版 BlockColumnFeature.truncate。 */
    private static void truncate(int[] arr, int total, int idx, boolean prioritizeTip) {
        int remaining = total - idx;
        int step = prioritizeTip ? 1 : -1;
        int start = prioritizeTip ? 0 : arr.length - 1;
        int end = prioritizeTip ? arr.length : -1;
        for (int i = start; i != end && remaining > 0; i += step) {
            int cut = Math.min(arr[i], remaining);
            arr[i] -= cut;
            remaining -= cut;
        }
    }

    // ==================== disk ====================

    static boolean disk(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        if (cfg == null) return false;
        int r = Math.max(0, IntProviders.parse(
            cfg.has("radius") ? cfg.get("radius") : cfg.get("radius_uniform"),
            FeaturePipeline.UNSUPPORTED).sample(ctx.rng));
        int hh = cfg.has("half_height") ? cfg.get("half_height").getAsInt() : 1;
        BlockPredicates.BlockPredicate target = cfg.has("target")
            ? BlockPredicates.parse(cfg.get("target"), FeaturePipeline.UNSUPPORTED)
            : (l, xx, yy, zz) -> true;
        StateProviders.StateProvider prov = cfg.has("state_provider")
            ? wrapProvider(cfg.get("state_provider"), "disk.state_provider")
            : (rng, xx, yy, zz) -> 0;
        WorldGenLevel l = ctx.level;
        int top = y + hh;
        int bottom = y - hh - 1;
        boolean any = false;
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                if (dx * dx + dz * dz > r * r) continue;
                boolean run = false;
                for (int i = top; i > bottom; i--) {
                    if (target.test(l, x + dx, i, z + dz)) {
                        int st = prov.get(ctx.rng, x + dx, i, z + dz);
                        if (st != 0) l.setBlock(x + dx, i, z + dz, st);
                        any = true;
                        run = true;
                    } else {
                        run = false;
                    }
                }
            }
        }
        return any;
    }

    // ==================== lake ====================

    static boolean lake(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        if (cfg == null) return false;
        WorldGenLevel l = ctx.level;
        if (y <= l.getMinY() + 4) return false;
        StateProviders.StateProvider fluidP = field(cfg, "fluid", "water");
        StateProviders.StateProvider barrierP = field(cfg, "barrier", "stone");
        int bx = x, bz = z;
        int by = y - 4;
        int fluid = fluidP.get(ctx.rng, bx, by, bz);
        if (fluid == 0) return false;
        boolean[][][] filled = new boolean[16][8][16];
        int blobs = ctx.rng.nextInt(4) + 4;
        for (int i = 0; i < blobs; i++) {
            double dx = ctx.rng.nextDouble() * 6.0 + 3.0;
            double dy = ctx.rng.nextDouble() * 4.0 + 2.0;
            double dz = ctx.rng.nextDouble() * 6.0 + 3.0;
            double cxx = ctx.rng.nextDouble() * (16.0 - dx - 2.0) + 1.0 + dx / 2.0;
            double cyy = ctx.rng.nextDouble() * (8.0 - dy - 4.0) + 2.0 + dy / 2.0;
            double czz = ctx.rng.nextDouble() * (16.0 - dz - 2.0) + 1.0 + dz / 2.0;
            for (int j = 1; j < 15; j++) {
                for (int k = 1; k < 15; k++) {
                    for (int m = 1; m < 7; m++) {
                        double a = (j - cxx) / (dx / 2.0);
                        double b = (m - cyy) / (dy / 2.0);
                        double c = (k - czz) / (dz / 2.0);
                        if (a * a + b * b + c * c < 1.0) filled[j][m][k] = true;
                    }
                }
            }
        }
        int[][] dirs6 = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {0, 1, 0}, {0, -1, 0}};
        for (int j = 0; j < 16; j++) {
            for (int k = 0; k < 16; k++) {
                for (int m = 0; m < 8; m++) {
                    if (filled[j][m][k]) continue;
                    boolean border = false;
                    for (int[] d : dirs6) {
                        int nj = j + d[0], nm = m + d[1], nk = k + d[2];
                        if (nj < 0 || nj > 15 || nm < 0 || nm > 7 || nk < 0 || nk > 15) continue;
                        if (filled[nj][nm][nk]) { border = true; break; }
                    }
                    if (!border) continue;
                    int cur = l.getBlock(bx + j, by + m, bz + k);
                    if (m >= 4 && BlockPredicates.liquid(cur)) return false;
                    if (m >= 4 || BlockStateHelper.isSolidOpaque(cur) || cur == fluid) continue;
                    return false;
                }
            }
        }
        for (int j = 0; j < 16; j++) {
            for (int k = 0; k < 16; k++) {
                for (int m = 0; m < 8; m++) {
                    if (!filled[j][m][k]) continue;
                    int ax = bx + j, ay = by + m, az = bz + k;
                    int cur = l.getBlock(ax, ay, az);
                    if (FEATURES_CANNOT_REPLACE.contains(cur)) continue;
                    l.setBlock(ax, ay, az, m >= 4 ? 0 : fluid);
                }
            }
        }
        int barrier = barrierP.get(ctx.rng, bx, by, bz);
        if (barrier != 0) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    for (int m = 0; m < 8; m++) {
                        if (filled[j][m][k]) continue;
                        boolean border = false;
                        for (int[] d : dirs6) {
                            int nj = j + d[0], nm = m + d[1], nk = k + d[2];
                            if (nj < 0 || nj > 15 || nm < 0 || nm > 7 || nk < 0 || nk > 15) continue;
                            if (filled[nj][nm][nk]) { border = true; break; }
                        }
                        if (!border) continue;
                        if (m >= 4 && ctx.rng.nextInt(2) == 0) continue;
                        int ax = bx + j, ay = by + m, az = bz + k;
                        int cur = l.getBlock(ax, ay, az);
                        if (!BlockStateHelper.isSolidOpaque(cur)) continue;
                        l.setBlock(ax, ay, az, barrier);
                    }
                }
            }
        }
        return true;
    }

    // ==================== spring_feature ====================

    static boolean spring(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        if (cfg == null || !cfg.has("valid_blocks")) return false;
        WorldGenLevel l = ctx.level;
        Set<Integer> valid = new HashSet<>();
        for (JsonElement e : cfg.getAsJsonArray("valid_blocks")) {
            valid.add(BlockStateHelper.getDefault(StateProviders.shortName(e.getAsString())));
        }
        int rockCount = cfg.has("rock_count") ? cfg.get("rock_count").getAsInt() : 4;
        int holeCount = cfg.has("hole_count") ? cfg.get("hole_count").getAsInt() : 1;
        boolean requiresBelow = !cfg.has("requires_block_below")
            || cfg.get("requires_block_below").getAsBoolean();
        int fluid = StateProviders.stateFromJson(cfg.getAsJsonObject("state"));
        if (fluid == 0) return false;
        if (!valid.contains(l.getBlock(x, y + 1, z))) return false;
        if (requiresBelow && !valid.contains(l.getBlock(x, y - 1, z))) return false;
        int here = l.getBlock(x, y, z);
        boolean hereEmpty = here == 0;
        if (!hereEmpty && !valid.contains(here)) return false;
        int rocks = 0;
        int holes = 0;
        int[] sides = {x - 1, x + 1};
        int[] sidesZ = {z - 1, z + 1};
        for (int sx : sides) {
            if (valid.contains(l.getBlock(sx, y, z))) rocks++;
            if (l.getBlock(sx, y, z) == 0) holes++;
        }
        for (int sz : sidesZ) {
            if (valid.contains(l.getBlock(x, y, sz))) rocks++;
            if (l.getBlock(x, y, sz) == 0) holes++;
        }
        int below = l.getBlock(x, y - 1, z);
        if (valid.contains(below)) rocks++;
        if (below == 0) holes++;
        if (rocks != rockCount || holes != holeCount) return false;
        l.setBlock(x, y, z, fluid);
        return true;
    }

    // ==================== freeze_top_layer ====================

    static boolean freezeTopLayer(Ctx ctx, int x, int z) {
        WorldGenLevel l = ctx.level;
        BitSet cold = coldBiomes();
        int ice = BlockStateHelper.getDefault("ice");
        int snowLayer = BlockStateHelper.getDefault("snow");
        int grassBlock = BlockStateHelper.getDefault("grass_block");
        int bx = (x >> 4) << 4;
        int bz = (z >> 4) << 4;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                int wx = bx + i, wz = bz + j;
                int h = PlacementModifiers.colHeight(ctx,
                    PlacementModifiers.HM_MOTION_BLOCKING, wx, wz);
                if (h == Integer.MIN_VALUE || h <= l.getMinY() + 1) continue;
                int bid = ctx.biomeAt.applyAsInt(wx, wz);
                if (bid < 0 || !cold.get(bid)) continue;
                int topCell = h - 1;
                int underCell = h - 2;
                if (BlockPredicates.isWaterName(BlockPredicates.name(l.getBlock(wx, underCell, wz)))) {
                    l.setBlock(wx, underCell, wz, ice);
                } else if (l.getBlock(wx, topCell, wz) == 0) {
                    l.setBlock(wx, topCell, wz, snowLayer);
                    int ground = l.getBlock(wx, underCell, wz);
                    if (ground == grassBlock) {
                        int snowy = BlockStateHelper.withProp(ground, "snowy", "true");
                        l.setBlock(wx, underCell, wz, snowy != 0 ? snowy : ground);
                    }
                }
            }
        }
        return true;
    }

    private static BitSet coldBiomes() {
        BitSet b = coldBiomes;
        if (b == null) {
            b = new BitSet();
            for (String n : new String[]{"snowy_plains", "ice_spikes", "snowy_beach",
                "snowy_taiga", "frozen_ocean", "frozen_river", "frozen_peaks",
                "jagged_peaks", "grove", "snowy_slopes", "deep_frozen_ocean"}) {
                int id = RegistryHelper.biomeNameToId(n);
                if (id >= 0) b.set(id);
            }
            coldBiomes = b;
        }
        return b;
    }

    // ==================== forest_rock（block_blob） ====================

    static boolean forestRock(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        if (cfg == null) return false;
        WorldGenLevel l = ctx.level;
        int state = StateProviders.stateFromJson(cfg.getAsJsonObject("state"));
        if (state == 0) return false;
        int minY = l.getMinY();
        while (y > minY + 3) {
            int below = l.getBlock(x, y - 1, z);
            String bn = BlockPredicates.name(below);
            boolean empty = below == 0;
            if (!empty && (DIRT_NAMES.contains(bn) || STONE_NAMES.contains(bn))) break;
            y--;
        }
        if (y <= minY + 3) return false;
        for (int i = 0; i < 3; i++) {
            int rx = ctx.rng.nextInt(2);
            int ry = ctx.rng.nextInt(2);
            int rz = ctx.rng.nextInt(2);
            float rad = (rx + ry + rz) * 0.333f + 0.5f;
            for (int dx = -rx; dx <= rx; dx++) {
                for (int dy = -ry; dy <= ry; dy++) {
                    for (int dz = -rz; dz <= rz; dz++) {
                        if (dx * dx + dy * dy + dz * dz > rad * rad) continue;
                        l.setBlock(x + dx, y + dy, z + dz, state);
                    }
                }
            }
            x += -1 + ctx.rng.nextInt(2);
            y += -ctx.rng.nextInt(2);
            z += -1 + ctx.rng.nextInt(2);
        }
        return true;
    }

    // ==================== multiface_growth（glow_lichen） ====================

    static boolean multifaceGrowth(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        if (cfg == null) return false;
        WorldGenLevel l = ctx.level;
        int block = BlockStateHelper.getDefault(
            StateProviders.shortName(cfg.has("block") ? cfg.get("block").getAsString()
                : "glow_lichen"));
        if (block == 0) return false;
        Set<String> placeableOn = new HashSet<>();
        if (cfg.has("can_be_placed_on")) {
            for (JsonElement e : cfg.getAsJsonArray("can_be_placed_on")) {
                placeableOn.add(StateProviders.shortName(e.getAsString()));
            }
        }
        boolean floor = !cfg.has("can_place_on_floor") || cfg.get("can_place_on_floor").getAsBoolean();
        boolean ceiling = !cfg.has("can_place_on_ceiling") || cfg.get("can_place_on_ceiling").getAsBoolean();
        boolean wall = !cfg.has("can_place_on_wall") || cfg.get("can_place_on_wall").getAsBoolean();
        float spreadChance = cfg.has("chance_of_spreading")
            ? cfg.get("chance_of_spreading").getAsFloat() : 0.5f;
        int searchRange = cfg.has("search_range") ? cfg.get("search_range").getAsInt() : 20;
        List<int[]> dirs = new ArrayList<>();
        if (floor) dirs.add(new int[]{0, -1, 0});
        if (ceiling) dirs.add(new int[]{0, 1, 0});
        if (wall) {
            dirs.add(new int[]{0, 0, -1});
            dirs.add(new int[]{0, 0, 1});
            dirs.add(new int[]{-1, 0, 0});
            dirs.add(new int[]{1, 0, 0});
        }
        shuffle(ctx.rng, dirs);
        int search = Math.min(searchRange, 24);
        if (tryLichen(ctx, l, x, y, z, block, placeableOn, dirs)) return true;
        for (int[] d : dirs) {
            for (int k = 1; k <= search; k++) {
                int cx2 = x + d[0] * k, cy2 = y + d[1] * k, cz2 = z + d[2] * k;
                int st = l.getBlock(cx2, cy2, cz2);
                boolean enterable = st == 0 || BlockPredicates.isWaterName(BlockPredicates.name(st));
                if (!enterable) break;
                if (tryLichen(ctx, l, cx2, cy2, cz2, block, placeableOn, dirs)) {
                    if (ctx.rng.nextFloat() < spreadChance) {
                        shuffle(ctx.rng, dirs);
                        tryLichen(ctx, l, cx2 + d[0], cy2 + d[1], cz2 + d[2],
                            block, placeableOn, dirs);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean tryLichen(Ctx ctx, WorldGenLevel l, int x, int y, int z,
                                     int block, Set<String> placeableOn, List<int[]> dirs) {
        int cur = l.getBlock(x, y, z);
        String cn = BlockPredicates.name(cur);
        boolean inWater = BlockPredicates.isWaterName(cn);
        if (!(cur == 0 || inWater)) return false;
        boolean anyFace = false;
        int st = block;
        for (int[] d : dirs) {
            int nb = l.getBlock(x + d[0], y + d[1], z + d[2]);
            if (!placeableOn.contains(BlockPredicates.name(nb))) continue;
            String face = d[1] == 1 ? "up" : d[1] == -1 ? "down"
                : d[2] == 1 ? "south" : d[2] == -1 ? "north"
                : d[0] == 1 ? "east" : "west";
            int with = BlockStateHelper.withProp(st, face, "true");
            if (with != 0) st = with;
            anyFace = true;
        }
        if (!anyFace) return false;
        if (inWater) {
            int wl = BlockStateHelper.withProp(st, "waterlogged", "true");
            if (wl != 0) st = wl;
        }
        l.setBlock(x, y, z, st);
        return true;
    }

    static void shuffle(RandomSource rng, List<int[]> list) {
        for (int i = list.size() - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int[] t = list.get(i);
            list.set(i, list.get(j));
            list.set(j, t);
        }
    }

    // ==================== monster_room ====================

    static boolean monsterRoom(Ctx ctx, int x, int y, int z) {
        WorldGenLevel l = ctx.level;
        RandomSource rng = ctx.rng;
        int halfX = rng.nextInt(2) + 2;
        int halfZ = rng.nextInt(2) + 2;
        int cobble = BlockStateHelper.getDefault("cobblestone");
        int mossy = BlockStateHelper.getDefault("mossy_cobblestone");
        int chest = BlockStateHelper.getDefault("chest");
        int spawner = BlockStateHelper.getDefault("spawner");
        int openCount = 0;
        for (int dx = -halfX - 1; dx <= halfX + 1; dx++) {
            for (int dy = -1; dy <= 4; dy++) {
                for (int dz = -halfZ - 1; dz <= halfZ + 1; dz++) {
                    boolean solid = BlockStateHelper.isSolidOpaque(
                        l.getBlock(x + dx, y + dy, z + dz));
                    if (dy == -1 && !solid) return false;
                    if (dy == 4 && !solid) return false;
                    boolean rim = (dx == -halfX - 1 || dx == halfX + 1
                        || dz == -halfZ - 1 || dz == halfZ + 1) && dy == 0;
                    if (rim && l.getBlock(x + dx, y + dy, z + dz) == 0
                        && l.getBlock(x + dx, y + dy + 1, z + dz) == 0) {
                        openCount++;
                    }
                }
            }
        }
        if (openCount < 1 || openCount > 5) return false;
        for (int dx = -halfX - 1; dx <= halfX + 1; dx++) {
            for (int dy = 3; dy >= -1; dy--) {
                for (int dz = -halfZ - 1; dz <= halfZ + 1; dz++) {
                    int ax = x + dx, ay = y + dy, az = z + dz;
                    int cur = l.getBlock(ax, ay, az);
                    boolean shell = dx == -halfX - 1 || dx == halfX + 1
                        || dz == -halfZ - 1 || dz == halfZ + 1
                        || dy == -1 || dy == 4;
                    if (shell) {
                        if (ay >= l.getMinY()
                            && !BlockStateHelper.isSolidOpaque(l.getBlock(ax, ay - 1, az))) {
                            l.setBlock(ax, ay, az, 0);
                            continue;
                        }
                        if (!BlockStateHelper.isSolidOpaque(cur) || cur == chest) continue;
                        int pick = (dy == -1 && rng.nextInt(4) != 0) ? mossy : cobble;
                        safeSet(l, ax, ay, az, pick);
                    } else {
                        if (cur == chest || cur == spawner) continue;
                        safeSet(l, ax, ay, az, 0);
                    }
                }
            }
        }
        chestLoop:
        for (int attempt = 0; attempt < 2; attempt++) {
            for (int tries = 0; tries < 3; tries++) {
                int cx2 = x + rng.nextInt(halfX * 2 + 1) - halfX;
                int cz2 = z + rng.nextInt(halfZ * 2 + 1) - halfZ;
                int cur = l.getBlock(cx2, y, cz2);
                if (cur != 0) continue;
                int solidNeighbors = 0;
                if (BlockStateHelper.isSolidOpaque(l.getBlock(cx2 + 1, y, cz2))) solidNeighbors++;
                if (BlockStateHelper.isSolidOpaque(l.getBlock(cx2 - 1, y, cz2))) solidNeighbors++;
                if (BlockStateHelper.isSolidOpaque(l.getBlock(cx2, y, cz2 + 1))) solidNeighbors++;
                if (BlockStateHelper.isSolidOpaque(l.getBlock(cx2, y, cz2 - 1))) solidNeighbors++;
                if (solidNeighbors != 1) continue;
                String facing = switch (rng.nextInt(4)) {
                    case 0 -> "north";
                    case 1 -> "south";
                    case 2 -> "west";
                    default -> "east";
                };
                int withFacing = BlockStateHelper.withProp(chest, "facing", facing);
                safeSet(l, cx2, y, cz2, withFacing != 0 ? withFacing : chest);
                break chestLoop;
            }
        }
        if (spawner != 0) safeSet(l, x, y, z, spawner);
        String[] mobs = {"minecraft:skeleton", "minecraft:zombie",
            "minecraft:zombie", "minecraft:spider"};
        String mob = mobs[rng.nextInt(mobs.length)];
        org.cloudburstmc.nbt.NbtMap spawnData = org.cloudburstmc.nbt.NbtMap.builder()
            .putCompound("entity", org.cloudburstmc.nbt.NbtMap.builder()
                .putString("id", mob).build())
            .build();
        org.cloudburstmc.nbt.NbtMap be = org.cloudburstmc.nbt.NbtMap.builder()
            .putString("id", "minecraft:mob_spawner")
            .putInt("x", x).putInt("y", y).putInt("z", z)
            .putInt("Delay", 20)
            .putInt("MinSpawnDelay", 200)
            .putInt("MaxSpawnDelay", 800)
            .putInt("SpawnCount", 4)
            .putInt("MaxNearbyEntities", 6)
            .putInt("RequiredPlayerRange", 16)
            .putInt("SpawnRange", 4)
            .putCompound("SpawnData", spawnData)
            .putList("SpawnPotentials", org.cloudburstmc.nbt.NbtType.COMPOUND,
                java.util.List.of(org.cloudburstmc.nbt.NbtMap.builder()
                    .putInt("weight", 1)
                    .putCompound("data", spawnData)
                    .build()))
            .build();
        l.setBlockEntity(x, y, z, be);
        return true;
    }

    private static void safeSet(WorldGenLevel l, int x, int y, int z, int state) {
        int cur = l.getBlock(x, y, z);
        if (FEATURES_CANNOT_REPLACE.contains(cur)) return;
        if (state != 0 && !(cur == 0 || BlockPredicates.canEnter(cur)
            || BlockPredicates.liquid(cur) && state != 0 && false)) {
            // 仅替换空气/可替换方块；液体保留（近似原版 replaceable 规则）
            if (!(cur == 0 || BlockPredicates.canEnter(cur))) return;
        }
        l.setBlock(x, y, z, state);
    }

    // ==================== bamboo ====================

    static boolean bamboo(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        WorldGenLevel l = ctx.level;
        RandomSource rng = ctx.rng;
        float podzolChance = cfg != null && cfg.has("probability")
            ? cfg.get("probability").getAsFloat() : 0.0f;
        if (l.getBlock(x, y, z) != 0) return false;
        int below = l.getBlock(x, y - 1, z);
        String bn = BlockPredicates.name(below);
        if (!DIRT_NAMES.contains(bn) && !bn.equals("bamboo")) return false;
        int stalkLen = rng.nextInt(12) + 5;
        if (rng.nextFloat() < podzolChance) {
            int podzol = BlockStateHelper.getDefault("podzol");
            int radius = rng.nextInt(4) + 1;
            for (int dx = x - radius; dx <= x + radius; dx++) {
                for (int dz = z - radius; dz <= z + radius; dz++) {
                    if ((dx - x) * (dx - x) + (dz - z) * (dz - z) > radius * radius) continue;
                    int g = PlacementModifiers.colHeight(ctx,
                        PlacementModifiers.HM_WORLD_SURFACE, dx, dz);
                    int gy = g - 2;
                    String gn = BlockPredicates.name(l.getBlock(dx, gy, dz));
                    if (gn != null && DIRT_NAMES.contains(gn) && podzol != 0) {
                        l.setBlock(dx, gy, dz, podzol);
                    }
                }
            }
        }
        int bamboo = BlockStateHelper.getDefault("bamboo");
        if (bamboo == 0) return false;
        int trunkAge1 = BlockStateHelper.withProp(
            BlockStateHelper.withProp(
                BlockStateHelper.withProp(bamboo, "age", "1"),
                "leaves", "none"), "stage", "0");
        int cy = y;
        for (int i = 0; i < stalkLen && l.getBlock(x, cy, z) == 0; i++) {
            l.setBlock(x, cy, z, trunkAge1);
            cy++;
        }
        if (cy - y >= 3) {
            int finalLarge = BlockStateHelper.withProp(
                BlockStateHelper.withProp(trunkAge1, "leaves", "large"), "stage", "1");
            int topLarge = BlockStateHelper.withProp(trunkAge1, "leaves", "large");
            int topSmall = BlockStateHelper.withProp(trunkAge1, "leaves", "small");
            l.setBlock(x, cy, z, finalLarge);
            l.setBlock(x, cy - 1, z, topLarge);
            l.setBlock(x, cy - 2, z, topSmall);
        }
        return true;
    }

    // ==================== ore / scattered_ore ====================

    static boolean ore(JsonObject cfg, Ctx ctx, int x, int y, int z, boolean scattered) {
        if (cfg == null || !cfg.has("targets")) return false;
        int size = cfg.has("size") ? cfg.get("size").getAsInt() : 1;
        float discard = cfg.has("discard_chance_on_air_exposure")
            ? cfg.get("discard_chance_on_air_exposure").getAsFloat() : 0.0f;
        record Target(BlockPredicates.BlockPredicate pred, int state) {}
        List<Target> targets = new ArrayList<>();
        for (JsonElement e : cfg.getAsJsonArray("targets")) {
            JsonObject t = e.getAsJsonObject();
            targets.add(new Target(
                BlockPredicates.parse(t.get("target"), FeaturePipeline.UNSUPPORTED),
                StateProviders.stateFromJson(t.getAsJsonObject("state"))));
        }
        if (targets.isEmpty() || size == 0) return false;
        WorldGenLevel l = ctx.level;
        RandomSource rng = ctx.rng;
        float angle = rng.nextFloat() * (float) Math.PI;
        float radiusXZ = size / 8.0f;
        double ex1 = x + Math.sin(angle) * radiusXZ;
        double ex2 = x - Math.sin(angle) * radiusXZ;
        double ez1 = z + Math.cos(angle) * radiusXZ;
        double ez2 = z - Math.cos(angle) * radiusXZ;
        double ey1 = y + rng.nextInt(3) - 2;
        double ey2 = y + rng.nextInt(3) - 2;
        int minX = l.getMinY(), maxY = l.getMinY() + l.getHeight() - 1;
        int placedCount = 0;
        double wx = 0, wy = 0, wz = 0;
        for (int i = 0; i < size; i++) {
            float f = (float) i / size;
            double cx = lerp(f, ex1, ex2);
            double cy = lerp(f, ey1, ey2);
            double cz = lerp(f, ez1, ez2);
            double rr = rng.nextDouble() * size / 16.0;
            double rad = ((Math.sin(Math.PI * f) + 1.0) * rr + 1.0) / 2.0;
            if (scattered) {
                wx += rng.nextInt(3) - 1;
                wy += rng.nextInt(3) - 1;
                wz += rng.nextInt(3) - 1;
                cx += wx; cy += wy; cz += wz;
            }
            int x0 = (int) Math.floor(cx - rad), x1 = (int) Math.ceil(cx + rad);
            int y0 = Math.max((int) Math.floor(cy - rad), minX);
            int y1 = Math.min((int) Math.ceil(cy + rad), maxY);
            int z0 = (int) Math.floor(cz - rad), z1 = (int) Math.ceil(cz + rad);
            for (int bx = x0; bx <= x1; bx++) {
                for (int by = y0; by <= y1; by++) {
                    for (int bz = z0; bz <= z1; bz++) {
                        if (((bx + 0.5 - cx) / rad) * ((bx + 0.5 - cx) / rad)
                            + ((by + 0.5 - cy) / rad) * ((by + 0.5 - cy) / rad)
                            + ((bz + 0.5 - cz) / rad) * ((bz + 0.5 - cz) / rad) >= 1.0) {
                            continue;
                        }
                        int cur = l.getBlock(bx, by, bz);
                        for (Target t : targets) {
                            if (!t.pred().test(l, bx, by, bz)) continue;
                            boolean skipAirCheck = discard <= 0.0f
                                || (discard < 1.0f && rng.nextFloat() >= discard);
                            if (!skipAirCheck && adjacentToAir(l, bx, by, bz)) break;
                            l.setBlock(bx, by, bz, t.state());
                            placedCount++;
                            break;
                        }
                    }
                }
            }
        }
        return placedCount > 0;
    }

    private static boolean adjacentToAir(WorldGenLevel l, int x, int y, int z) {
        return l.getBlock(x + 1, y, z) == 0 || l.getBlock(x - 1, y, z) == 0
            || l.getBlock(x, y + 1, z) == 0 || l.getBlock(x, y - 1, z) == 0
            || l.getBlock(x, y, z + 1) == 0 || l.getBlock(x, y, z - 1) == 0;
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    // ==================== 选择器 ====================

    static boolean randomSelector(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        if (cfg == null) return false;
        if (cfg.has("features")) {
            for (JsonElement e : cfg.getAsJsonArray("features")) {
                JsonObject f = e.getAsJsonObject();
                if (ctx.rng.nextFloat() < f.get("chance").getAsFloat()) {
                    return FeaturePipeline.executePlacedRef(f.get("feature"), ctx, x, y, z);
                }
            }
        }
        if (cfg.has("default")) {
            return FeaturePipeline.executePlacedRef(cfg.get("default"), ctx, x, y, z);
        }
        return false;
    }

    static boolean simpleRandomSelector(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        if (cfg == null || !cfg.has("features")) return false;
        JsonArray arr = cfg.getAsJsonArray("features");
        if (arr.size() == 0) return false;
        JsonElement pick = arr.get(ctx.rng.nextInt(arr.size()));
        return FeaturePipeline.executePlacedRef(pick, ctx, x, y, z);
    }

    static boolean randomBooleanSelector(JsonObject cfg, Ctx ctx, int x, int y, int z) {
        if (cfg == null) return false;
        JsonElement pick = ctx.rng.nextBoolean() ? cfg.get("feature_true") : cfg.get("feature_false");
        return FeaturePipeline.executePlacedRef(pick, ctx, x, y, z);
    }
}
