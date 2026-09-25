package com.CharunCore.server.worldgen.feature.vanilla;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.NormalNoise;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;
import com.CharunCore.server.worldgen.feature.vanilla.FeaturePipeline.Ctx;
import com.CharunCore.server.worldgen.structure2.BiomeTagResolver;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * placed_feature 的 placement 数组编译为修饰符链。
 * 语义对照 mapping/cfr-source/net/minecraft/world/level/levelgen/placement/*.java。
 * 链从种子点（区块原点）出发，顺序执行：生成器扩展候选、过滤器剔除候选。
 */
public final class PlacementModifiers {

    /** 高度查询类型序号，与原版 Heightmap.Types 子集对应。 */
    public static final int HM_WORLD_SURFACE = 0;       // 含 WORLD_SURFACE_WG
    public static final int HM_OCEAN_FLOOR = 1;         // 含 OCEAN_FLOOR_WG
    public static final int HM_MOTION_BLOCKING = 2;
    public static final int HM_MOTION_BLOCKING_NO_LEAVES = 3;

    private static volatile NormalNoise infoNoise;

    private PlacementModifiers() {}

    public static String strip(String s) {
        return s != null && s.startsWith("minecraft:") ? s.substring(10) : s;
    }

    // ==================== 编译 ====================

    public interface Op {
        List<int[]> apply(List<int[]> pts, Ctx ctx);
    }

    public static List<Op> compile(JsonArray placement, Set<String> unsupported) {
        List<Op> ops = new ArrayList<>();
        if (placement == null) return ops;
        for (JsonElement el : placement) {
            if (!el.isJsonObject()) continue;
            JsonObject o = el.getAsJsonObject();
            String type = o.has("type") ? strip(o.get("type").getAsString()) : "";
            switch (type) {
                case "count":
                    ops.add(new Count(IntProviders.parse(o.get("count"), unsupported)));
                    break;
                case "rarity_filter":
                    ops.add(new Rarity(o.get("chance").getAsInt()));
                    break;
                case "in_square":
                    ops.add(new InSquare());
                    break;
                case "height_range":
                    ops.add(new HeightRange(parseHeightProvider(o.get("height"), unsupported)));
                    break;
                case "heightmap":
                    ops.add(new Heightmap(parseHeightmapType(
                        o.has("heightmap") ? o.get("heightmap").getAsString() : "WORLD_SURFACE")));
                    break;
                case "biome":
                    ops.add(new Biome(o));
                    break;
                case "noise_based_count":
                    ops.add(new NoiseBasedCount(
                        o.get("noise_to_count_ratio").getAsInt(),
                        o.get("noise_factor").getAsDouble(),
                        o.has("noise_offset") ? o.get("noise_offset").getAsDouble() : 0.0));
                    break;
                case "noise_threshold_count":
                    ops.add(new NoiseThresholdCount(
                        o.get("noise_level").getAsDouble(),
                        o.get("below_noise").getAsInt(),
                        o.get("above_noise").getAsInt()));
                    break;
                case "count_on_every_layer":
                    ops.add(new CountOnEveryLayer(
                        IntProviders.opt(o, "count", 1, unsupported)));
                    break;
                case "block_predicate_filter":
                    ops.add(new PredicateFilter(
                        BlockPredicates.parse(o.get("predicate"), unsupported)));
                    break;
                case "environment_scan":
                    ops.add(new EnvScan(
                        "up".equalsIgnoreCase(o.get("direction_of_search").getAsString()),
                        BlockPredicates.parse(o.get("target_condition"), unsupported),
                        o.has("allowed_search_condition")
                            ? BlockPredicates.parse(o.get("allowed_search_condition"), unsupported)
                            : (l, x, y, z) -> true,
                        o.get("max_steps").getAsInt()));
                    break;
                case "surface_relative_threshold_filter":
                    ops.add(new SurfaceRelativeThreshold(
                        parseHeightmapType(o.get("heightmap").getAsString()),
                        o.has("min_inclusive") ? o.get("min_inclusive").getAsInt() : Integer.MIN_VALUE,
                        o.has("max_inclusive") ? o.get("max_inclusive").getAsInt() : Integer.MAX_VALUE));
                    break;
                case "surface_water_depth_filter":
                    ops.add(new SurfaceWaterDepth(o.get("max_water_depth").getAsInt()));
                    break;
                case "random_offset":
                    ops.add(new RandomOffset(
                        IntProviders.opt(o, "xz_spread", 0, unsupported),
                        IntProviders.opt(o, "y_spread", 0, unsupported)));
                    break;
                case "fixed_placement": {
                    List<int[]> fixed = new ArrayList<>();
                    for (JsonElement p : o.getAsJsonArray("positions")) {
                        JsonArray a = p.getAsJsonArray();
                        fixed.add(new int[]{a.get(0).getAsInt(), a.get(1).getAsInt(), a.get(2).getAsInt()});
                    }
                    ops.add(new Fixed(fixed));
                    break;
                }
                default:
                    unsupported.add("placement." + type);
            }
        }
        return ops;
    }

    public static int parseHeightmapType(String s) {
        s = strip(s == null ? "" : s.replace("WG", "_WG"));
        return switch (s) {
            case "OCEAN_FLOOR", "OCEAN_FLOOR_WG" -> HM_OCEAN_FLOOR;
            case "MOTION_BLOCKING" -> HM_MOTION_BLOCKING;
            case "MOTION_BLOCKING_NO_LEAVES" -> HM_MOTION_BLOCKING_NO_LEAVES;
            default -> HM_WORLD_SURFACE; // WORLD_SURFACE / WORLD_SURFACE_WG
        };
    }

    // ==================== 高度提供器（height_range） ====================

    public interface HeightProvider {
        int sample(Ctx ctx, RandomSource rng);
    }

    /** VerticalAnchor：{"absolute":N}|{"above_bottom":N}|{"below_top":N}|纯数字。 */
    public static int[] parseAnchor(JsonElement el) {
        // 返回 [mode, value]: mode 0=absolute 1=above_bottom 2=below_top
        if (el == null) return new int[]{0, 0};
        if (el.isJsonPrimitive()) return new int[]{0, el.getAsInt()};
        JsonObject o = el.getAsJsonObject();
        if (o.has("absolute")) return new int[]{0, o.get("absolute").getAsInt()};
        if (o.has("above_bottom")) return new int[]{1, o.get("above_bottom").getAsInt()};
        if (o.has("below_top")) return new int[]{2, o.get("below_top").getAsInt()};
        return new int[]{0, 0};
    }

    public static int resolveAnchor(int[] anchor, Ctx ctx) {
        return switch (anchor[0]) {
            case 1 -> ctx.minY() + anchor[1];
            case 2 -> ctx.minY() + ctx.genHeight() - anchor[1];
            default -> anchor[1];
        };
    }

    public static HeightProvider parseHeightProvider(JsonElement el, Set<String> unsupported) {
        if (el == null || !el.isJsonObject()) return (c, r) -> c.minY();
        JsonObject o = el.getAsJsonObject();
        String type = o.has("type") ? strip(o.get("type").getAsString()) : "uniform";
        switch (type) {
            case "uniform": {
                int[] lo = parseAnchor(o.get("min_inclusive"));
                int[] hi = parseAnchor(o.get("max_inclusive"));
                return (c, r) -> IntProviders.between(r,
                    resolveAnchor(lo, c), Math.max(resolveAnchor(hi, c), resolveAnchor(lo, c)));
            }
            case "constant":
                int[] v = parseAnchor(o.has("value") ? o.get("value") : o.get("absolute"));
                return (c, r) -> resolveAnchor(v, c);
            case "trapezoid": {
                int[] lo = parseAnchor(o.get("min_inclusive"));
                int[] hi = parseAnchor(o.get("max_inclusive"));
                int plateau = o.has("plateau") ? o.get("plateau").getAsInt() : 0;
                return (c, r) -> trapezoidSample(r,
                    resolveAnchor(lo, c), resolveAnchor(hi, c), plateau);
            }
            case "very_biased_to_bottom":
            case "biased_to_bottom": {
                int[] lo = parseAnchor(o.get("min_inclusive"));
                int[] hi = parseAnchor(o.get("max_inclusive"));
                boolean very = type.equals("very_biased_to_bottom");
                return (c, r) -> biasedSample(r,
                    resolveAnchor(lo, c), resolveAnchor(hi, c), very);
            }
            case "weighted_list":
                return parseWeightedHeight(o, unsupported);
            default:
                unsupported.add("height_provider." + type);
                return (c, r) -> c.minY();
        }
    }

    private static HeightProvider parseWeightedHeight(JsonObject o, Set<String> unsupported) {
        record Entry(HeightProvider p, int w) {}
        List<Entry> items = new ArrayList<>();
        int total = 0;
        for (JsonElement e : o.getAsJsonArray("distribution")) {
            JsonObject d = e.getAsJsonObject();
            items.add(new Entry(parseHeightProvider(d.get("data"), unsupported),
                d.has("weight") ? d.get("weight").getAsInt() : 1));
            total += d.has("weight") ? d.get("weight").getAsInt() : 1;
        }
        if (items.isEmpty() || total <= 0) return (c, r) -> c.minY();
        int t = total;
        return (c, r) -> {
            int roll = r.nextInt(t);
            for (Entry e : items) {
                roll -= e.w();
                if (roll < 0) return e.p().sample(c, r);
            }
            return items.get(items.size() - 1).p().sample(c, r);
        };
    }

    static int trapezoidSample(RandomSource rng, int min, int max, int plateau) {
        if (max <= min) return min;
        int span = max - min;
        if (plateau >= span) return IntProviders.between(rng, min, max);
        int halfA = (span - plateau) / 2;
        int halfB = span - halfA;
        return min + IntProviders.between(rng, 0, halfB) + IntProviders.between(rng, 0, halfA);
    }

    /** 原版 VeryBiasedToBottomHeight/BiasedToBottomHeight（inner=0）。 */
    static int biasedSample(RandomSource rng, int min, int max, boolean very) {
        if (max <= min) return min;
        if (!very) {
            int n3 = rng.nextInt(max - min + 1);
            return rng.nextInt(n3 + 1) + min;
        }
        int n3 = IntProviders.between(rng, min, max);
        int n4 = IntProviders.between(rng, min, Math.max(n3 - 1, min));
        return IntProviders.between(rng, min, Math.max(n4 - 1 + 0, min));
    }

    // ==================== 高度图列查询 ====================

    /** 返回该列首个可通过方块的 Y（最高阻挡方块 +1）；窗口外返回 Integer.MIN_VALUE。 */
    public static int colHeight(Ctx ctx, int type, int x, int z) {
        long key = ((long) type << 58)
            | ((long) (x & 0x1FFFFFFF) << 29)
            | (z & 0x1FFFFFFFL);
        Integer memo = ctx.columnMemo.get(key);
        if (memo != null) return memo;
        WorldGenLevel level = ctx.level;
        Chunk ch = level.getWindow().get(WorldGenLevel.key(x >> 4, z >> 4));
        if (ch == null) return Integer.MIN_VALUE;
        int lx = x & 15, lz = z & 15;
        int found = level.getMinY();
        for (int y = ctx.maxY(); y >= ctx.minY(); y--) {
            int st = ch.getBlock(lx, y, lz);
            if (st == 0) continue;
            String n = BlockPredicates.name(st);
            boolean match = switch (type) {
                case HM_WORLD_SURFACE -> true;
                case HM_OCEAN_FLOOR -> blocksMotion(n);
                case HM_MOTION_BLOCKING -> blocksMotion(n) || BlockPredicates.liquid(st);
                case HM_MOTION_BLOCKING_NO_LEAVES ->
                    (blocksMotion(n) && !n.contains("leaves")) || BlockPredicates.liquid(st);
                default -> true;
            };
            if (match) {
                found = y + 1;
                break;
            }
        }
        ctx.columnMemo.put(key, found);
        return found;
    }

    /** 近似 blocksMotion：完整固体 + 玻璃/树叶类；排除空气/流体/可替换植物。 */
    static boolean isOpaqueName(String n) {
        return !(n.contains("air") || n.contains("water") || n.contains("lava")
            || n.contains("carpet") || n.contains("sapling") || n.contains("flower")
            || n.contains("_grass") || n.endsWith("fern") || n.contains("torch")
            || n.contains("rail") || n.contains("sign") || n.contains("button")
            || n.contains("pressure_plate") || n.contains("slab") || n.contains("stairs")
            || n.contains("fence") || n.contains("pane") || n.contains("leaves"));
    }

    static boolean blocksMotion(String n) {
        if (n == null) return false;
        if (n.contains("air") || n.contains("water") || n.contains("lava")) return false;
        if (BlockStateHelper.isReplaceable(n)) return false;
        if (isOpaqueName(n)) return true;
        return n.contains("leaves") || n.contains("glass") || n.endsWith("_pane")
            || n.equals("hedge") || n.equals("bamboo") || n.equals("short_dry_grass") || n.equals("tall_dry_grass");
    }

    // ==================== 噪声（BIOME_INFO_NOISE 近似） ====================

    static NormalNoise infoNoise() {
        NormalNoise n = infoNoise;
        if (n == null) {
            synchronized (PlacementModifiers.class) {
                if (infoNoise == null) {
                    RandomSource r = com.CharunCore.server.worldgen.density.DensityFunction
                        .NoiseHolder.sharedFactory().fromHashOf("minecraft:biome_info_noise");
                    infoNoise = n = new NormalNoise(r,
                        new NormalNoise.NoiseParameters(-7, new double[]{1.0}));
                }
            }
        }
        return n;
    }

    // ==================== biome 过滤集合 ====================

    static BitSet parseBiomeArg(JsonElement el, Set<String> unsupported) {
        BitSet out = new BitSet();
        if (el == null) return out;
        if (el.isJsonArray()) {
            for (JsonElement e : el.getAsJsonArray()) mergeBiomeToken(out, e.getAsString());
        } else if (el.isJsonPrimitive()) {
            mergeBiomeToken(out, el.getAsString());
        }
        return out;
    }

    private static void mergeBiomeToken(BitSet out, String token) {
        if (token == null || token.isEmpty()) return;
        Integer id = BiomeTagResolver.getBiomeId(token);
        if (id != null) {
            out.set(id);
            return;
        }
        String tag = token.startsWith("#") ? token.substring(1) : token;
        for (Integer b : BiomeTagResolver.getBiomesForStructure(strip(tag))) out.set(b);
    }

    // ==================== Op 实现 ====================

    static final class Count implements Op {
        private final IntProviders.IntProvider count;
        Count(IntProviders.IntProvider c) { count = c; }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            for (int[] p : pts) {
                int n = Math.max(0, count.sample(ctx.rng));
                for (int i = 0; i < n; i++) out.add(p.clone());
            }
            return out;
        }
    }

    static final class Rarity implements Op {
        private final int chance;
        Rarity(int c) { chance = Math.max(1, c); }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            for (int[] p : pts) {
                if (ctx.rng.nextFloat() < 1.0f / chance) out.add(p);
            }
            return out;
        }
    }

    static final class InSquare implements Op {
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            for (int[] p : pts) {
                p[0] += ctx.rng.nextInt(16);
                p[2] += ctx.rng.nextInt(16);
            }
            return pts;
        }
    }

    static final class HeightRange implements Op {
        private final HeightProvider hp;
        HeightRange(HeightProvider h) { hp = h; }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            for (int[] p : pts) p[1] = hp.sample(ctx, ctx.rng);
            return pts;
        }
    }

    static final class Heightmap implements Op {
        private final int type;
        Heightmap(int t) { type = t; }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            for (int[] p : pts) {
                int h = colHeight(ctx, type, p[0], p[2]);
                if (h > ctx.minY()) out.add(new int[]{p[0], h, p[2]});
            }
            return out;
        }
    }

    static final class Biome implements Op {
        private final BitSet allow; // null = 无参数（原版注册表接线语义），放行由调用方群系驱动
        Biome(JsonObject o) {
            boolean hasArg = o != null && (o.has("biome_is") || o.has("tag") || o.has("tags"));
            this.allow = hasArg ? new BitSet() : null;
            if (hasArg) {
                BitSet b = parseBiomeArg(o.get("biome_is"), FeaturePipeline.UNSUPPORTED);
                b.or(parseBiomeArg(o.get("tags"), FeaturePipeline.UNSUPPORTED));
                if (o.has("tag")) b.or(parseBiomeArg(o.get("tag"), FeaturePipeline.UNSUPPORTED));
                this.allow.or(b);
            }
        }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            if (allow == null || allow.isEmpty()) return pts;
            List<int[]> out = new ArrayList<>();
            for (int[] p : pts) {
                int bid = ctx.biomeAt.applyAsInt(p[0], p[2]);
                if (bid >= 0 && allow.get(bid)) out.add(p);
            }
            return out;
        }
    }

    static final class NoiseBasedCount implements Op {
        private final int ratio;
        private final double factor;
        private final double offset;
        NoiseBasedCount(int r, double f, double o) { ratio = r; factor = f; offset = o; }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            for (int[] p : pts) {
                double d = infoNoise().getValue(p[0] / factor, 0.0, p[2] / factor);
                int n = Math.max(0, (int) Math.ceil((d + offset) * ratio));
                for (int i = 0; i < n; i++) out.add(p.clone());
            }
            return out;
        }
    }

    static final class NoiseThresholdCount implements Op {
        private final double level;
        private final int below;
        private final int above;
        NoiseThresholdCount(double l, int b, int a) { level = l; below = b; above = a; }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            for (int[] p : pts) {
                double d = infoNoise().getValue(p[0] / 200.0, 0.0, p[2] / 200.0);
                int n = d < level ? below : above;
                for (int i = 0; i < n; i++) out.add(p.clone());
            }
            return out;
        }
    }

    static final class CountOnEveryLayer implements Op {
        private final IntProviders.IntProvider count;
        CountOnEveryLayer(IntProviders.IntProvider c) { count = c; }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            for (int[] p : pts) {
                int layer = 0;
                boolean again;
                do {
                    again = false;
                    int n = Math.max(0, count.sample(ctx.rng));
                    for (int i = 0; i < n; i++) {
                        int x = ctx.rng.nextInt(16) + p[0];
                        int z = ctx.rng.nextInt(16) + p[2];
                        int top = colHeight(ctx, HM_MOTION_BLOCKING, x, z);
                        int y = findOnGround(ctx, x, top, z, layer);
                        if (y != Integer.MAX_VALUE) {
                            out.add(new int[]{x, y, z});
                            again = true;
                        }
                    }
                    layer++;
                } while (again);
            }
            return out;
        }

        /** 自顶向下找第 layer 个「上空下实」过渡；找不到返回 Integer.MAX_VALUE。 */
        private int findOnGround(Ctx ctx, int x, int top, int z, int wantLayer) {
            if (top <= ctx.minY() + 1) return Integer.MAX_VALUE;
            int seen = 0;
            boolean curEmpty = isEmpty(ctx.level.getBlock(x, Math.min(top, ctx.maxY()), z));
            for (int y = Math.min(top, ctx.maxY()); y > ctx.minY(); y--) {
                int bs = ctx.level.getBlock(x, y - 1, z);
                boolean belowEmpty = isEmpty(bs);
                if (!belowEmpty && curEmpty && !"bedrock".equals(BlockPredicates.name(bs))) {
                    if (seen == wantLayer) return y;
                    seen++;
                }
                curEmpty = belowEmpty;
            }
            return Integer.MAX_VALUE;
        }

        private static boolean isEmpty(int st) {
            return st == 0 || BlockPredicates.canEnter(st);
        }
    }

    static final class PredicateFilter implements Op {
        private final BlockPredicates.BlockPredicate pred;
        PredicateFilter(BlockPredicates.BlockPredicate p) { pred = p; }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            for (int[] p : pts) {
                if (pred.test(ctx.level, p[0], p[1], p[2])) out.add(p);
            }
            return out;
        }
    }

    static final class EnvScan implements Op {
        private final boolean up;
        private final BlockPredicates.BlockPredicate target;
        private final BlockPredicates.BlockPredicate allowed;
        private final int maxSteps;
        EnvScan(boolean up, BlockPredicates.BlockPredicate t,
                BlockPredicates.BlockPredicate a, int m) {
            this.up = up; target = t; allowed = a; maxSteps = m;
        }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            int dy = up ? 1 : -1;
            for (int[] p : pts) {
                int x = p[0], y = p[1], z = p[2];
                if (!allowed.test(ctx.level, x, y, z)) continue;
                int i = 0;
                for (; i < maxSteps; i++) {
                    if (target.test(ctx.level, x, y, z)) break;
                    y += dy;
                    if (y < ctx.minY() || y > ctx.maxY()) { y = Integer.MIN_VALUE; break; }
                    if (!allowed.test(ctx.level, x, y, z)) break;
                }
                if (y == Integer.MIN_VALUE) continue;
                if (target.test(ctx.level, x, y, z)) out.add(new int[]{x, y, z});
            }
            return out;
        }
    }

    static final class SurfaceRelativeThreshold implements Op {
        private final int type;
        private final int min;
        private final int max;
        SurfaceRelativeThreshold(int t, int mn, int mx) { type = t; min = mn; max = mx; }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            for (int[] p : pts) {
                long h = colHeight(ctx, type, p[0], p[2]);
                if (h == Integer.MIN_VALUE) continue;
                if (h + min <= p[1] && p[1] <= h + max) out.add(p);
            }
            return out;
        }
    }

    static final class SurfaceWaterDepth implements Op {
        private final int maxDepth;
        SurfaceWaterDepth(int m) { maxDepth = m; }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            for (int[] p : pts) {
                int ws = colHeight(ctx, HM_WORLD_SURFACE, p[0], p[2]);
                int of = colHeight(ctx, HM_OCEAN_FLOOR, p[0], p[2]);
                if (ws == Integer.MIN_VALUE || of == Integer.MIN_VALUE) continue;
                if (ws - of <= maxDepth) out.add(p);
            }
            return out;
        }
    }

    static final class RandomOffset implements Op {
        private final IntProviders.IntProvider xz;
        private final IntProviders.IntProvider ys;
        RandomOffset(IntProviders.IntProvider xs, IntProviders.IntProvider ysp) {
            xz = xs; ys = ysp;
        }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            for (int[] p : pts) {
                p[0] += xz.sample(ctx.rng) - xz.sample(ctx.rng);
                p[1] += ys.sample(ctx.rng) - ys.sample(ctx.rng);
                p[2] += xz.sample(ctx.rng) - xz.sample(ctx.rng);
            }
            return pts;
        }
    }

    static final class Fixed implements Op {
        private final List<int[]> fixed;
        Fixed(List<int[]> f) { fixed = f; }
        @Override public List<int[]> apply(List<int[]> pts, Ctx ctx) {
            List<int[]> out = new ArrayList<>();
            for (int[] p : fixed) out.add(p.clone());
            return out;
        }
    }
}
