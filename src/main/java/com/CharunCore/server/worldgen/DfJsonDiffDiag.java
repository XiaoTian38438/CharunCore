package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.gen.NormalNoise;
import com.CharunCore.server.world.gen.NoiseParameters;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/** 用官方 density_function JSON 作为参照实现，对比项目编译版各分量的求值差异。 */
public final class DfJsonDiffDiag {

    static Map<String, JsonObject> fileCache = new HashMap<>();
    static Map<String, Evaluator> nodeCache = new HashMap<>();

    interface Evaluator {
        double compute(int x, int y, int z);
    }

    static boolean SPLINE_TRACE = false;

    public static void main(String[] args) {
        BlockStateHelper.init();
        DensityFunction.NoiseHolder.setWorldSeed(1234567L, 0L);
        var gen = new DensityRouterChunkGenerator(1234567L, DimensionType.OVERWORLD);
        var dfMap = gen.getDensityMap();

        String[] names = {
            "minecraft:overworld/continents", "minecraft:overworld/erosion",
            "minecraft:overworld/ridges", "minecraft:overworld/ridges_folded",
            "minecraft:overworld/offset", "minecraft:overworld/factor",
            "minecraft:overworld/depth", "minecraft:overworld/jaggedness",
            "minecraft:overworld/sloped_cheese"
        };
        // DEBUG: 逐层拆解 factor.json
        try {
            // SPLINE_TRACE = true;
            JsonObject fj = loadFile("minecraft:overworld/factor");
            JsonObject flat = fj.getAsJsonObject("argument");          // cache_2d
            JsonObject add10 = flat.getAsJsonObject("argument");       // add(10, mul(...))
            JsonObject mulN = add10.getAsJsonObject("argument2");      // mul(blend_alpha, add(-10, spline))
            JsonObject addN10 = mulN.getAsJsonObject("argument2");     // add(-10, spline)
            Evaluator eSpline = evalSpline(addN10.getAsJsonObject("argument2"));
            System.out.println("[dbg] spline part @block(1,68,0) = " + eSpline.compute(1, 68, 0));
            System.out.println("[dbg] add(-10,spline) = " + eval(addN10).compute(1, 68, 0));
            System.out.println("[dbg] mul(alpha,...)  = " + eval(mulN).compute(1, 68, 0));
            System.out.println("[dbg] full add10      = " + eval(add10).compute(1, 68, 0));
            SPLINE_TRACE = false;
        } catch (Throwable t) {
            SPLINE_TRACE = false;
            System.out.println("[dbg] factor decompose error: " + t);
        }

        // 用 JSON final_density 沿列扫描表面高度，并与真值对比
        try {
            var nr = com.google.gson.JsonParser.parseReader(new java.io.InputStreamReader(
                new java.io.FileInputStream("json/minecraft/worldgen/noise_settings/overworld.json"),
                StandardCharsets.UTF_8)).getAsJsonObject();
            Evaluator fd2 = eval(nr.getAsJsonObject("noise_router").get("final_density"));
            int[][] cols = {{1, 0}, {-120, -120}, {-8 * 16 + 3, -8 * 16 + 3}, {64, 64}};
            for (int[] c : cols) {
                int top = Integer.MIN_VALUE;
                for (int y = 320; y >= -64; y--) {
                    if (fd2.compute(c[0], y, c[1]) > 0) { top = y; break; }
                }
                System.out.printf("json-finalDensity top at (%d,%d) = %d%n", c[0], c[1], top);
            }
        } catch (Throwable t) { System.out.println("fd scan error " + t); }

        // 二分定位：shift 原子值 vs 项目 ShiftA/B；再比 continents 全链
        try {
            var gen2 = gen;
            var shX = gen2.getDensityMap().get("minecraft:shift_x");
            var shZ = gen2.getDensityMap().get("minecraft:shift_z");
            var contOurs = gen2.getDensityMap().get("minecraft:overworld/continents");
            int[][] ps = {{1, 68, 0}, {64, 70, 64}};
            for (int[] p : ps) {
                double jx = shiftNoise(false, p[0], p[2]);
                double ox = shX.compute(new DensityFunction.SinglePointContext(p[0], p[1], p[2]));
                double jz = shiftNoise(true, p[0], p[2]);
                double oz = shZ.compute(new DensityFunction.SinglePointContext(p[0], p[1], p[2]));
                System.out.printf("shift@(%d,%d): jsonX=%.5f oursX=%.5f jsonZ=%.5f oursZ=%.5f%n",
                    p[0], p[2], jx, ox, jz, oz);
                // 手动 continents 链
                var contParams = NoiseParameters.get("minecraft:continentalness");
                var cn = new NormalNoise(DensityFunction.NoiseHolder.sharedFactory().fromHashOf("minecraft:continentalness"), contParams);
                double manual = cn.getValue(p[0] * 0.25 + jx, 0.0, p[2] * 0.25 + jz);
                System.out.printf("continents@( %d,%d ): manual=%.5f json=%.5f ours=%.5f%n",
                    p[0], p[2], manual,
                    get("minecraft:overworld/continents").compute(p[0], p[1], p[2]),
                    contOurs.compute(new DensityFunction.SinglePointContext(p[0], p[1], p[2])));
            }
        } catch (Throwable t) { System.out.println("bisect error " + t); }

        try {
            var b3j = eval(loadFile("minecraft:overworld/base_3d_noise"));
            var b3o = gen.getDensityMap().get("minecraft:overworld/base_3d_noise");
            int[][] ps2 = {{1, 68, 0}, {1, 40, 0}, {64, 70, 64}, {-120, 80, -120}};
            for (int[] p : ps2) {
                double jv = b3j.compute(p[0], p[1], p[2]);
                double ov = b3o.compute(new DensityFunction.SinglePointContext(p[0], p[1], p[2]));
                System.out.printf("base3d@(%d,%d,%d): json=%.6f ours=%.6f%n", p[0], p[1], p[2], jv, ov);
            }
        } catch (Throwable t) { System.out.println("b3 error " + t); }

        try {
            var nrRoot = com.google.gson.JsonParser.parseReader(new java.io.InputStreamReader(
                new java.io.FileInputStream("json/minecraft/worldgen/noise_settings/overworld.json"),
                StandardCharsets.UTF_8)).getAsJsonObject();
            Evaluator fdJ = eval(nrRoot.getAsJsonObject("noise_router").get("final_density"));
            DensityFunction fdO = gen.getRouter().finalDensity();
            for (int[] c : new int[][]{{1, 0}, {-120, -120}, {64, 64}}) {
                StringBuilder lj = new StringBuilder(), lo = new StringBuilder();
                int topJ = Integer.MIN_VALUE, topO = Integer.MIN_VALUE;
                for (int y = 200; y >= -60; y -= 4) {
                    double vj = fdJ.compute(c[0], y, c[1]);
                    double vo = fdO.compute(new DensityFunction.SinglePointContext(c[0], y, c[1]));
                    if (vj > 0 && topJ == Integer.MIN_VALUE) topJ = y;
                    if (vo > 0 && topO == Integer.MIN_VALUE) topO = y;
                    lj.append(String.format("%7.3f", vj)); lo.append(String.format("%7.3f", vo));
                }
                System.out.printf("finalDensity col(%d,%d): jsonTop=%d oursTop=%d%n", c[0], c[1], topJ, topO);
                System.out.println("  json: " + lj);
                System.out.println("  ours: " + lo);
            }
        } catch (Throwable t) { System.out.println("fdcol error " + t); }

        int[][] pts = {{1, 68, 0}, {0, 80, -128}, {-120, 80, -120}, {64, 70, 64}, {-200, 75, -200}};
        for (int[] p : pts) {
            System.out.printf("--- block(%d,%d,%d)%n", p[0], p[1], p[2]);
            for (String name : names) {
                try {
                    Evaluator ev = get(name);
                    double ref = ev.compute(p[0], p[1], p[2]);
                    DensityFunction ours = dfMap.get(name);
                    double got = ours != null ? ours.compute(new DensityFunction.SinglePointContext(p[0], p[1], p[2])) : Double.NaN;
                    String flag = Math.abs(ref - got) < 5e-3 ? "" : "   <<< DIFF";
                    System.out.printf("  %-34s json=%10.5f ours=%10.5f%s%n", shortName(name), ref, got, flag);
                } catch (Throwable t) {
                    System.out.printf("  %-34s ERROR %s%n", shortName(name), t);
                }
            }
        }
    }

    private static String shortName(String s) { return s.replace("minecraft:overworld/", ""); }

    static Evaluator get(String key) {
        Evaluator e = nodeCache.get(key);
        if (e == null) { e = eval(loadFile(key)); nodeCache.put(key, e); }
        return e;
    }

    static JsonObject loadFile(String key) {
        JsonObject cached = fileCache.get(key);
        if (cached != null) return cached;
        String path = "json/minecraft/worldgen/density_function/" + strip(key) + ".json";
        try (FileInputStream fis = new FileInputStream(path);
             InputStreamReader r = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            JsonObject jo = JsonParser.parseReader(r).getAsJsonObject();
            fileCache.put(key, jo);
            return jo;
        } catch (Exception ex) {
            throw new RuntimeException("missing df json " + path, ex);
        }
    }

    static String strip(String s) { return s.startsWith("minecraft:") ? s.substring(10) : s; }

    static double num(JsonElement e) { return e.getAsDouble(); }

    static Evaluator constEval(double v) { return (_x, _y, _z) -> v; }

    /** 字符串引用：shift 系 / y / 其它 density_function 文件。 */
    static Evaluator named(String key) {
        return switch (strip(key)) {
            case "zero" -> constEval(0.0);
            case "y" -> (x, y, z) -> y;
            case "shift_x" -> (x, y, z) -> shiftNoise(false, x, z);
            case "shift_z" -> (x, y, z) -> shiftNoise(true, x, z);
            case "shift_y" -> constEval(0.0);
            case "blend_alpha" -> constEval(1.0);
            default -> get(key);
        };
    }

    static Evaluator eval(JsonElement el) {
        if (el.isJsonPrimitive() && el.getAsJsonPrimitive().isNumber()) { double v = el.getAsDouble(); return (_x, _y, _z) -> v; }
        if (el.isJsonPrimitive()) return named(el.getAsString());
        JsonObject o = el.getAsJsonObject();
        String type = strip(o.get("type").getAsString());
        try {
        switch (type) {
            case "constant": { double v = o.get("argument").getAsDouble(); return (_x, _y, _z) -> v; }
            case "flat_cache": case "cache_2d": case "cache_once":
            case "interpolated": case "cache_all_in_cell": case "blend_density": {
                return eval(o.get("argument"));
            }
            case "blend_alpha": return (_x, _y, _z) -> 1.0;
            case "blend_offset": return (_x, _y, _z) -> 0.0;
            case "y_clamped_gradient": {
                int fromY = o.get("from_y").getAsInt(), toY = o.get("to_y").getAsInt();
                double fv = o.get("from_value").getAsDouble(), tv = o.get("to_value").getAsDouble();
                return (x, y, z) -> y <= fromY ? fv : y >= toY ? tv : fv + (tv - fv) * (y - fromY) / (double) (toY - fromY);
            }
            case "add": case "mul": case "min": case "max": {
                Evaluator a1 = eval(o.get("argument1")), a2 = eval(o.get("argument2"));
                return switch (type) {
                    case "add" -> (x, y, z) -> a1.compute(x, y, z) + a2.compute(x, y, z);
                    case "mul" -> (x, y, z) -> a1.compute(x, y, z) * a2.compute(x, y, z);
                    case "min" -> (x, y, z) -> Math.min(a1.compute(x, y, z), a2.compute(x, y, z));
                    default -> (x, y, z) -> Math.max(a1.compute(x, y, z), a2.compute(x, y, z));
                };
            }
            case "abs": case "square": case "cube": case "half_negative":
            case "quarter_negative": case "invert": case "squeeze": {
                Evaluator in = eval(o.get("argument"));
                return switch (type) {
                    case "abs" -> (x, y, z) -> Math.abs(in.compute(x, y, z));
                    case "square" -> (x, y, z) -> { double d = in.compute(x, y, z); return d * d; };
                    case "cube" -> (x, y, z) -> { double d = in.compute(x, y, z); return d * d * d; };
                    case "half_negative" -> (x, y, z) -> { double d = in.compute(x, y, z); return d > 0 ? d : d * 0.5; };
                    case "quarter_negative" -> (x, y, z) -> { double d = in.compute(x, y, z); return d > 0 ? d : d * 0.25; };
                    case "invert" -> (x, y, z) -> -in.compute(x, y, z);
                    default -> (x, y, z) -> { double c = Math.clamp(in.compute(x, y, z), -1, 1); return c / 2.0 - c * c * c / 24.0; };
                };
            }
            case "clamp": {
                Evaluator in = eval(o.get("input"));
                double lo = o.has("min_inclusive") ? o.get("min_inclusive").getAsDouble() : o.get("min").getAsDouble();
                double hi = o.has("max_inclusive") ? o.get("max_inclusive").getAsDouble() : o.get("max").getAsDouble();
                return (x, y, z) -> Math.clamp(in.compute(x, y, z), lo, hi);
            }
            case "range_choice": {
                Evaluator in = eval(o.get("input"));
                double lo = o.get("min_inclusive").getAsDouble(), hi = o.get("max_exclusive").getAsDouble();
                Evaluator onRange = eval(o.get("when_in_range")), out = eval(o.get("when_out_of_range"));
                return (x, y, z) -> { double d = in.compute(x, y, z); return d >= lo && d < hi ? onRange.compute(x, y, z) : out.compute(x, y, z); };
            }
            case "noise": {
                String noiseKey = strip(o.get("noise").getAsString());
                double xz = o.has("xz_scale") ? o.get("xz_scale").getAsDouble() : 1.0;
                double ys = o.has("y_scale") ? o.get("y_scale").getAsDouble() : 1.0;
                NormalNoise n = new NormalNoise(
                    DensityFunction.NoiseHolder.sharedFactory().fromHashOf(noiseKey),
                    NoiseParameters.get(noiseKey.startsWith("minecraft:") ? noiseKey : "minecraft:" + noiseKey));
                return (x, y, z) -> n.getValue(x * xz, y * ys, z * xz);
            }
            case "shifted_noise": {
                String noiseKey = strip(o.get("noise").getAsString());
                double xz = o.has("xz_scale") ? o.get("xz_scale").getAsDouble() : 1.0;
                double ys = o.has("y_scale") ? o.get("y_scale").getAsDouble() : 1.0;
                Evaluator sx = evalShift(o.get("shift_x")), sy = evalShift(o.get("shift_y")), sz = evalShift(o.get("shift_z"));
                String fullKey = noiseKey.startsWith("minecraft:") ? noiseKey : "minecraft:" + noiseKey;
                NormalNoise n = new NormalNoise(
                    DensityFunction.NoiseHolder.sharedFactory().fromHashOf(fullKey),
                    NoiseParameters.get(fullKey));
                return (x, y, z) -> n.getValue(x * xz + sx.compute(x, y, z), y * ys + sy.compute(x, y, z), z * xz + sz.compute(x, y, z));
            }
            case "shift_a": case "shift_b": {
                boolean b = type.equals("shift_b");
                String noiseKey = strip(o.has("noise") ? o.get("noise").getAsString() : "offset");
                NormalNoise n = new NormalNoise(
                    DensityFunction.NoiseHolder.sharedFactory().fromHashOf(noiseKey),
                    NoiseParameters.get(noiseKey));
                return b ? (x, y, z) -> n.getValue(z * 0.25, x * 0.25, 0.0) * 4.0
                         : (x, y, z) -> n.getValue(x * 0.25, 0.0, z * 0.25) * 4.0;
            }
            case "spline": {
                return evalSpline(o.get("spline"));
            }
            case "weird_scaled_sampler": {
                String noiseKey = strip(o.get("noise").getAsString());
                String fullKey = noiseKey.startsWith("minecraft:") ? noiseKey : "minecraft:" + noiseKey;
                var rndW = DensityFunction.NoiseHolder.sharedFactory().fromHashOf(fullKey);
                var nn = new NormalNoise(rndW, NoiseParameters.get(fullKey));
                Evaluator in = eval(o.get("input"));
                // RarityValueMapper TYPE1: 0.75/1.0/1.5/2.0 分桶; TYPE2: 0.5/0.75/1.0/1.5? 按原版:
                String mapper;
                JsonElement rm = o.get("rarity_value_mapper");
                if (rm.isJsonObject()) mapper = strip(rm.getAsJsonObject().get("type").getAsString());
                else mapper = rm.getAsString().replace("minecraft:", "");
                boolean type1 = mapper.endsWith("type_1");
                return (x, y, z) -> {
                    double d = in.compute(x, y, z);
                    double m;
                    if (type1) {
                        float f = (float) d;
                        if (f < -0.5f) m = 0.75;
                        else if (f < 0.0f) m = 1.0;
                        else if (f < 0.5f) m = 1.5;
                        else m = 2.0;
                    } else {
                        float f = (float) d;
                        if (f < -0.75f) m = 0.5;
                        else if (f < -0.5f) m = 0.75;
                        else if (f < 0.5f) m = 1.0;
                        else m = 1.5;
                    }
                    double dv = m;
                    return dv * Math.abs(nn.getValue(x / dv, y / dv, z / dv));
                };
            }
            case "old_blended_noise": {
                // 原版 wrapNew：legacy 维度用 LegacyRandomSource(seed)，否则 fromHashOf("minecraft:terrain")
                com.CharunCore.server.world.gen.RandomSource rnd =
                    DensityFunction.NoiseHolder.useLegacyRandomSource()
                        ? new com.CharunCore.server.world.gen.LegacyRandomSource(DensityFunction.NoiseHolder.worldSeed())
                        : DensityFunction.NoiseHolder.sharedFactory().fromHashOf("minecraft:terrain");
                var bn = new com.CharunCore.server.world.gen.BlendedNoise(rnd,
                    o.get("xz_scale").getAsDouble(), o.get("y_scale").getAsDouble(),
                    o.get("xz_factor").getAsDouble(), o.get("y_factor").getAsDouble(),
                    o.get("smear_scale_multiplier").getAsDouble());
                return (x, y, z) -> bn.compute(x, y, z);
            }
            default:
                throw new RuntimeException("unsupported df type " + type);
        }
        } catch (RuntimeException ex) {
            throw new RuntimeException("at node type=" + type + " keys=" + o.keySet() + ": " + ex.getMessage(), ex);
        }
    }

    static Evaluator evalShift(JsonElement el) {
        if (el.isJsonPrimitive() && el.getAsJsonPrimitive().isNumber()) { double v = el.getAsDouble(); return (_x, _y, _z) -> v; }
        if (el.isJsonPrimitive()) return named(el.getAsString());
        JsonObject o = el.getAsJsonObject();
        String key = o.get("type").getAsString();
        if (key.endsWith("shift_a")) return (x, y, z) -> shiftNoise(false, x, z);
        if (key.endsWith("shift_b")) return (x, y, z) -> shiftNoise(true, x, z);
        throw new RuntimeException("bad shift " + key);
    }

    static NormalNoise OFFSET_NOISE;
    static double shiftNoise(boolean swap, int x, int z) {
        if (OFFSET_NOISE == null) {
            OFFSET_NOISE = new NormalNoise(
                DensityFunction.NoiseHolder.sharedFactory().fromHashOf("minecraft:offset"),
                NoiseParameters.get("minecraft:offset"));
        }
        return swap ? OFFSET_NOISE.getValue(z * 0.25, x * 0.25, 0.0) * 4.0
                    : OFFSET_NOISE.getValue(x * 0.25, 0.0, z * 0.25) * 4.0;
    }

    /** spline 节点：{coordinate:{type:..}|name, points:[{location,value[,derivative]}]} */
    static Evaluator evalSpline(JsonElement el) {
        JsonObject spo = el.getAsJsonObject();
        JsonObject sp = spo.has("type") && strip(spo.get("type").getAsString()).equals("spline")
            ? spo.getAsJsonObject("spline") : spo;
        if (sp.has("coordinate")) {
            JsonElement coordEl = sp.get("coordinate");
            Evaluator coord;
            coord = coordEl.isJsonObject() ? eval(coordEl) : named(coordEl.getAsString());
            Evaluator c = coord;
            record P(double loc, Evaluator val, double der) {}
            var points = new java.util.ArrayList<P>();
            for (JsonElement pe : sp.getAsJsonArray("points")) {
                JsonObject po = pe.getAsJsonObject();
                double loc = po.get("location").getAsDouble();
                JsonElement valEl = po.get("value");
                Evaluator val = (valEl.isJsonPrimitive() && valEl.getAsJsonPrimitive().isNumber())
                    ? constEval(valEl.getAsDouble())
                    : evalSpline(valEl);
                double der = po.has("derivative") ? po.get("derivative").getAsDouble() : 0.0;
                points.add(new P(loc, val, der));
            }
            points.sort(java.util.Comparator.comparingDouble(P::loc));
            final Evaluator cc = c;
            return (x, y, z) -> {
                double cv = cc.compute(x, y, z);
                int n = points.size();
                if (SPLINE_TRACE) System.out.println("[spline] cv=" + cv + " locs=" + points.stream().map(p2 -> String.valueOf(p2.loc())).toList());
                if (Double.isNaN(cv)) { System.out.println("[spline] NaN coordinate!"); return 0.0; }
                if (n == 0) throw new RuntimeException("empty spline points");
                if (cv <= points.get(0).loc) return points.get(0).val().compute(x, y, z);
                if (cv >= points.get(n - 1).loc) {
                    double rv = points.get(n - 1).val().compute(x, y, z);
                    if (SPLINE_TRACE) System.out.println("[spline] >=last(" + points.get(n - 1).loc + ") -> " + rv);
                    return rv;
                }
                int i = 0;
                while (i < n - 1 && points.get(i + 1).loc < cv) i++;
                P a = points.get(i), b = points.get(i + 1);
                double va = a.val().compute(x, y, z), vb = b.val().compute(x, y, z);
                double t = (cv - a.loc()) / (b.loc() - a.loc());
                double h = b.loc() - a.loc();
                // 三次 Hermite（原版 CubicSpline 半径语义）
                double m0 = a.der() * h, m1 = b.der() * h;
                double t2 = t * t, t3 = t2 * t;
                double res = (2 * t3 - 3 * t2 + 1) * va + (t3 - 2 * t2 + t) * m0
                    + (-2 * t3 + 3 * t2) * vb + (t3 - t2) * m1;
                if (SPLINE_TRACE) System.out.println("[spline] interp[" + a.loc() + "," + b.loc() + "] va=" + va + " vb=" + vb + " -> " + res);
                return res;
            };
        }
        throw new RuntimeException("bad spline node");
    }
}
