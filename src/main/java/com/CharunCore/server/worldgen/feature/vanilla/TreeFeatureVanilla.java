package com.CharunCore.server.worldgen.feature.vanilla;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 原版 TreeFeature 的移植（对照 mapping/cfr-source .../feature/TreeFeature.java 与
 * trunkplacer/foliageplacer/treedecorator 包）。支持 oak/birch/fancy_oak/spruce/pine/
 * mega_pine/mega_spruce/jungle/dark_oak/acacia/cherry/azalea 的配置字段。
 */
public final class TreeFeatureVanilla {

    private TreeFeatureVanilla() {}

    private static final java.util.Set<String> UNSUPPORTED_FIELDS =
        java.util.concurrent.ConcurrentHashMap.newKeySet();

    public static java.util.Set<String> unsupportedFields() { return UNSUPPORTED_FIELDS; }

    private static void unsup(String f) { UNSUPPORTED_FIELDS.add(f); }

    private static String strip(String s) { return FeaturePipeline.strip(s); }

    // ── state providers ─────────────────────────────────────────────

    private record SP(int[] states, double[] weights, String intProp, int propMin, int propMax) {
        int sample(RandomSource rng) {
            if (states.length == 1) {
                if (intProp != null) return withInt(states[0], rng.nextInt(propMax - propMin + 1) + propMin);
                return states[0];
            }
            double total = 0;
            for (double w : weights) total += w;
            double r = rng.nextDouble() * total;
            for (int i = 0; i < states.length; i++) {
                r -= weights[i];
                if (r <= 0) {
                    if (intProp != null) return withInt(states[i], rng.nextInt(propMax - propMin + 1) + propMin);
                    return states[i];
                }
            }
            return states[states.length - 1];
        }
    }

    private static int withInt(int state, int v) {
        // 常见整数属性猜测：candles/age/pickles 由调用方传入属性名时才生效；此处默认 age
        return BlockStateHelper.withProp(state, "age", String.valueOf(v));
    }

    private static SP parseSP(JsonElement el) {
        JsonObject o = el.getAsJsonObject();
        String t = FeaturePipeline.strip(o.get("type").getAsString());
        switch (t) {
            case "simple_state_provider": {
                return new SP(new int[]{stateOf(o.getAsJsonObject("state"))}, new double[]{1}, null, 0, 0);
            }
            case "weighted_state_provider": {
                JsonArray entries = o.getAsJsonArray("entries");
                int n = entries.size();
                int[] st = new int[n];
                double[] w = new double[n];
                for (int i = 0; i < n; i++) {
                    JsonObject e = entries.get(i).getAsJsonObject();
                    st[i] = stateOf(e.getAsJsonObject("data"));
                    w[i] = e.get("weight").getAsDouble();
                }
                return new SP(st, w, null, 0, 0);
            }
            case "randomized_int_state_provider": {
                SP base = parseSP(o.get("source"));
                JsonObject ip = o.getAsJsonObject("property");
                String name = strip(ip.get("name").getAsString());
                int lo, hi;
                if (ip.has("values") && ip.get("values").isJsonArray()) {
                    lo = ip.getAsJsonArray("values").get(0).getAsInt();
                    hi = ip.getAsJsonArray("values").get(1).getAsInt();
                } else {
                    JsonObject vs = ip.getAsJsonObject("values");
                    lo = vs.get("min_inclusive").getAsInt();
                    hi = vs.get("max_inclusive").getAsInt();
                }
                return new SP(base.states(), base.weights(), name.equals("age") ? null : name, lo, hi);
            }
            default: {
                unsup("state_provider." + t);
                return new SP(new int[]{0}, new double[]{1}, null, 0, 0);
            }
        }
    }

    private static int stateOf(JsonObject stateObj) {
        String name = FeaturePipeline.strip(stateObj.get("Name").getAsString());
        int st = BlockStateHelper.getDefault(name);
        if (stateObj.has("Properties")) {
            for (var e : stateObj.getAsJsonObject("Properties").entrySet()) {
                st = BlockStateHelper.withProp(st, e.getKey(), e.getValue().getAsString());
            }
        }
        return st;
    }

    // ── 放置主流程 ──────────────────────────────────────────────────

    public static boolean place(JsonObject cfg, WorldGenLevel level, int x, int y, int z, RandomSource rng) {
        SP trunkProvider = parseSP(cfg.get("trunk_provider"));
        SP foliageProvider = parseSP(cfg.get("foliage_provider"));
        SP dirtProvider = cfg.has("dirt_provider")
            ? parseSP(cfg.get("dirt_provider")) : null;
        JsonObject trunkPlacer = cfg.getAsJsonObject("trunk_placer");
        String tp = FeaturePipeline.strip(trunkPlacer.get("type").getAsString());
        int baseH = trunkPlacer.has("base_height") ? trunkPlacer.get("base_height").getAsInt() : 4;
        int randA = trunkPlacer.has("height_rand_a") ? trunkPlacer.get("height_rand_a").getAsInt() : 0;
        int randB = trunkPlacer.has("height_rand_b") ? trunkPlacer.get("height_rand_b").getAsInt() : 0;
        JsonObject foliagePlacer = cfg.getAsJsonObject("foliage_placer");
        String fp = FeaturePipeline.strip(foliagePlacer.get("type").getAsString());
        int radius = foliagePlacer.has("radius") ? foliagePlacer.get("radius").getAsInt() : 2;
        int offset = foliagePlacer.has("offset") ? foliagePlacer.get("offset").getAsInt() : 0;
        boolean ignoreVines = !cfg.has("ignore_vines") || cfg.get("ignore_vines").getAsBoolean();

        if (!canPlaceOn(level, x, y - 1, z)) return false;

        int height = baseH + rng.nextInt(randA + 1) + rng.nextInt(randB + 1);
        if (height < 1) height = 1;

        // dirt 层替换
        if (dirtProvider != null && y > level.getMinY()) {
            for (int dx = -1; dx <= 1; dx++)
                for (int dz = -1; dz <= 1; dz++)
                    trySet(level, x + dx, y - 1, z + dz, dirtProvider.sample(rng), true);
        }

        boolean mega = tp.contains("mega") || tp.equals("giant_trunk_placer");
        int topY = y + height - 1;

        switch (tp) {
            case "straight_trunk_placer" -> {
                placeColumn(level, trunkProvider, rng, x, y, z, height, 0, 0, mega);
                blobFoliage(level, foliageProvider, rng, x, topY, z, radius, offset, height, mega ? 1 : 0);
            }
            case "forking_trunk_placer", "dark_oak_trunk_placer" -> {
                int splitH = Math.max(1, height / 2 + rng.nextInt(2));
                placeColumn(level, trunkProvider, rng, x, y, z, splitH, 0, 0, mega);
                if (mega || tp.equals("dark_oak_trunk_placer")) {
                    placeColumn(level, trunkProvider, rng, x + 1, y, z, splitH, 0, 0, true);
                    placeColumn(level, trunkProvider, rng, x, y, z + 1, splitH, 0, 0, true);
                    placeColumn(level, trunkProvider, rng, x + 1, y, z + 1, splitH, 0, 0, true);
                }
                int branchLen = 1 + rng.nextInt(2);
                for (int s = 0; s < 2; s++) {
                    int dx = (s == 0 || rng.nextBoolean()) ? branchLen : -branchLen;
                    int dz = dx == branchLen ? (rng.nextBoolean() ? branchLen : -branchLen) : branchLen;
                    int bx = x + dx, bz = z + dz;
                    int bh = height - splitH + rng.nextInt(2);
                    placeColumn(level, trunkProvider, rng, bx, y + splitH - 1, bz, bh, 0, 0, mega);
                    blobFoliage(level, foliageProvider, rng, bx, y + splitH + bh - 1, bz,
                        radius, offset, bh, mega ? 1 : 0);
                }
            }
            case "giant_trunk_placer" -> {
                for (int dy = 0; dy < height; dy++) {
                    trySetLog2x2(level, trunkProvider, rng, x, y + dy, z);
                }
                blobFoliage(level, foliageProvider, rng, x + 1, topY, z + 1, radius, offset, height, 1);
            }
            case "mega_jungle_trunk_placer" -> {
                for (int dy = 0; dy < height; dy++) trySetLog2x2(level, trunkProvider, rng, x, y + dy, z);
                for (int b = 0; b < 3 + rng.nextInt(3); b++) {
                    int by = y + height - 8 - b * 3 + rng.nextInt(3);
                    int dx = 1 + rng.nextInt(3), dz = 1 + rng.nextInt(3);
                    int sx = rng.nextBoolean() ? -1 : 1, sz = rng.nextBoolean() ? -1 : 1;
                    for (int k = 0; k <= dx; k++) trySet(level, x + sx * k, by, z, trunkProvider.sample(rng), false);
                    for (int k = 0; k <= dz; k++) trySet(level, x, by, z + sz * k, trunkProvider.sample(rng), false);
                    blobFoliage(level, foliageProvider, rng, x + sx * dx, by, z + sz * dz, 2, 0, 2, 0);
                }
                blobFoliage(level, foliageProvider, rng, x + 1, topY, z + 1, radius, offset, height, 1);
            }
            case "cherry_trunk_placer" -> {
                int lean = rng.nextInt(5) - 2;
                for (int dy = 0; dy < height; dy++) {
                    int off = dy > height / 2 ? Integer.signum(lean) : 0;
                    trySet(level, x + off, y + dy, z, trunkProvider.sample(rng), false);
                    if (dy == height / 2 && lean != 0)
                        trySet(level, x + off, y + dy, z + Integer.signum(lean), trunkProvider.sample(rng), false);
                }
                cherryFoliage(level, foliageProvider, rng, x + Integer.signum(lean), topY + 1, z, radius);
            }
            case "upward_branching_trunk_placer" -> {
                placeColumn(level, trunkProvider, rng, x, y, z, height, 0, 0, false);
                for (int b = 0; b < 2 + rng.nextInt(3); b++) {
                    int by = y + 1 + rng.nextInt(height - 1);
                    int dx = rng.nextInt(3) - 1, dz = rng.nextInt(3) - 1;
                    trySet(level, x + dx, by, z + dz, trunkProvider.sample(rng), false);
                }
                blobFoliage(level, foliageProvider, rng, x, topY, z, radius, offset, height, 0);
            }
            default -> {
                unsup("trunk." + tp);
                placeColumn(level, trunkProvider, rng, x, y, z, height, 0, 0, false);
                blobFoliage(level, foliageProvider, rng, x, topY, z, radius, offset, height, 0);
            }
        }

        // 特定树叶形态
        switch (fp) {
            case "spruce_foliage_placer", "pine_foliage_placer",
                 "mega_pine_foliage_placer", "mega_spruce_foliage_placer" -> {
                boolean pine = fp.startsWith("pine");
                int trunkHeight = foliagePlacer.has("trunk_height") ? foliagePlacer.get("trunk_height").getAsInt() : height / 2;
                spruceFoliage(level, foliageProvider, rng, x, y, z, height, radius, trunkHeight, pine);
                return true;
            }
            case "acacia_foliage_placer" -> {
                acaciaFoliage(level, foliageProvider, rng, x, topY, z, radius, offset);
                return true;
            }
            default -> { /* 其余已在各分支放置 */ }
        }

        // 树干顶部保证一层叶帽（blob 分支未覆盖的 placer）
        ensureTopCap(level, foliageProvider, rng, x, topY, z, radius);

        // decorators（leave_vine/trunk_vine/cocoa 仅丛林相关，跳过 vine 细节；beehive 概率）
        if (cfg.has("decorators")) {
            for (JsonElement d : cfg.getAsJsonArray("decorators")) {
                JsonObject dj = d.getAsJsonObject();
                String dt = FeaturePipeline.strip(dj.get("type").getAsString());
                if (dt.equals("beehive_decorator") && rng.nextFloat() < dj.get("probability").getAsFloat()) {
                    int hx = x + rng.nextInt(3) - 1, hz = z + rng.nextInt(3) - 1;
                    int hy = y + height - rng.nextInt(2);
                    trySet(level, hx, hy, hz, BlockStateHelper.getDefault("bee_nest"), false);
                } else if (!dt.equals("leave_vine_decorator") && !dt.equals("trunk_vine_decorator")
                    && !dt.equals("cocoa_decorator") && !dt.equals("attached_to_leaves_decorator")
                    && !dt.equals("alter_ground_decorator")) {
                    unsup("decorator." + dt);
                }
            }
        }
        return true;
    }

    private static void placeColumn(WorldGenLevel l, SP sp, RandomSource rng,
                                    int x, int yBase, int z, int h, int dx, int dz, boolean mega) {
        for (int i = 0; i < h; i++) {
            if (mega) trySetLog2x2(l, sp, rng, x, yBase + i, z);
            else trySet(l, x + dx, yBase + i, z + dz, sp.sample(rng), false);
        }
    }

    private static void trySetLog2x2(WorldGenLevel l, SP sp, RandomSource rng, int x, int y, int z) {
        for (int dx = 0; dx <= 1; dx++)
            for (int dz = 0; dz <= 1; dz++) trySet(l, x + dx, y, z + dz, sp.sample(rng), false);
    }

    /** 原版 BlobFoliagePlacer：球状叶层。 */
    private static void blobFoliage(WorldGenLevel l, SP sp, RandomSource rng,
                                    int cx, int cy, int cz, int radius, int offset, int trunkH, int megaPad) {
        int r = radius + megaPad;
        for (int dy = -r; dy <= r + offset; dy++) {
            int ly = cy + dy;
            int rr = r - dy + (dy >= 0 ? 0 : 1);
            if (rr < 0) continue;
            for (int dx = -rr; dx <= rr; dx++)
                for (int dz = -rr; dz <= rr; dz++) {
                    if (dx * dx + dy * dy * 2 + dz * dz > rr * rr + rr) continue;
                    if (dx == 0 && dz == 0 && dy <= 0) continue; // 让出树干
                    tryLeaf(l, cx + dx, ly, cz + dz, sp.sample(rng), rng);
                }
        }
    }

    private static void ensureTopCap(WorldGenLevel l, SP sp, RandomSource rng,
                                     int x, int topY, int z, int radius) {
        tryLeaf(l, x, topY + 1, z, sp.sample(rng), rng);
        for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
            tryLeaf(l, x + d[0], topY + 1, z + d[1], sp.sample(rng), rng);
        }
    }

    /** Spruce/PineFoliagePlacer：锥形分层。 */
    private static void spruceFoliage(WorldGenLevel l, SP sp, RandomSource rng,
                                      int x, int yBase, int z, int height, int radius, int trunkH, boolean pine) {
        int topY = yBase + height;
        // 顶帽
        tryLeaf(l, x, topY + 1, z, sp.sample(rng), rng);
        tryLeaf(l, x, topY, z, sp.sample(rng), rng);
        int start = Math.max(yBase + 2, topY - trunkH - radius * 2);
        int layer = 0;
        for (int ly = topY - 1; ly >= start; ly--) {
            int rr = pine ? ((layer % 2 == 0) ? (layer / 2 % 2 == 0 ? 1 : 2) : 1 + (layer % 4) / 2)
                          : (layer == 0 ? radius : 1 + (layer % 2));
            if (pine) rr = 1 + (layer / 2) % 3;
            rr = Math.min(rr, radius + 1);
            for (int dx = -rr; dx <= rr; dx++)
                for (int dz = -rr; dz <= rr; dz++) {
                    if (dx == 0 && dz == 0 && ly <= topY - 1 && Math.abs(dx) + Math.abs(dz) == 0) {
                        trySet(l, x, ly, z, sp.sample(rng), false); // 树干继续
                        continue;
                    }
                    if (Math.abs(dx) == rr && Math.abs(dz) == rr && rr > 1 && rng.nextBoolean()) continue;
                    tryLeaf(l, x + dx, ly, z + dz, sp.sample(rng), rng);
                }
            layer++;
        }
    }

    /** AcaciaFoliagePlacer：顶层圆盘 + 下层小盘。 */
    private static void acaciaFoliage(WorldGenLevel l, SP sp, RandomSource rng,
                                      int x, int topY, int z, int radius, int offset) {
        diskAt(l, sp, rng, x, topY, z, radius);
        diskAt(l, sp, rng, x - 1, topY - 1, z, radius - 1);
    }

    private static void diskAt(WorldGenLevel l, SP sp, RandomSource rng, int x, int y, int z, int r) {
        for (int dx = -r; dx <= r; dx++)
            for (int dz = -r; dz <= r; dz++) {
                if (Math.abs(dx) == r && Math.abs(dz) == r && r > 1) continue;
                tryLeaf(l, x + dx, y, z + dz, sp.sample(rng), rng);
            }
    }

    private static void cherryFoliage(WorldGenLevel l, SP sp, RandomSource rng,
                                      int x, int y, int z, int radius) {
        for (int dy = -1; dy <= 1; dy++) {
            int rr = radius - Math.abs(dy);
            for (int dx = -rr; dx <= rr; dx++)
                for (int dz = -rr; dz <= rr; dz++) {
                    int dd = dx * dx + dz * dz + dy * dy * 2;
                    if (dd > rr * rr + 1) continue;
                    if (rng.nextInt(6) == 0 && Math.abs(dx) == rr) continue;
                    tryLeaf(l, x + dx, y + dy, z + dz, sp.sample(rng), rng);
                }
        }
        tryLeaf(l, x, y + 2, z, sp.sample(rng), rng);
    }

    private static void tryLeaf(WorldGenLevel l, int x, int y, int z, int leafState, RandomSource rng) {
        if (l.getBlock(x, y, z) == 0) l.setBlock(x, y, z, leafState);
    }

    private static void trySet(WorldGenLevel l, int x, int y, int z, int state, boolean force) {
        int cur = l.getBlock(x, y, z);
        if (force || cur == 0 || replaceable(cur)) l.setBlock(x, y, z, state);
    }

    private static boolean replaceable(int state) {
        String n = BlockStateHelper.getName(state);
        if (n == null) return true;
        return n.endsWith("_grass") || n.endsWith("fern") || n.endsWith("flower")
            || n.contains("sapling") || n.contains("carpet") || n.equals("snow");
    }

    private static boolean canPlaceOn(WorldGenLevel l, int x, int y, int z) {
        int b = l.getBlock(x, y, z);
        if (b == 0) return false;
        String n = BlockStateHelper.getName(b);
        if (n == null) return false;
        return n.equals("grass_block") || n.equals("dirt") || n.equals("coarse_dirt")
            || n.equals("podzol") || n.equals("moss_block") || n.equals("mud")
            || n.equals("mycelium") || n.equals("farmland") || n.equals("rooted_dirt")
            || n.equals("end_stone") || n.endsWith("nylium") || n.equals("sand")
            || n.equals("red_sand") || n.equals("snow_block");
    }
}
