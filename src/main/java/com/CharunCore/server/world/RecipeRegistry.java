package com.CharunCore.server.world;

import com.CharunCore.server.utils.BlockManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 原版 1.21.11 配方注册表。
 * 加载 json/1.21.11/recipes.json (886 个原版配方) + items.json (id→name),
 * 建立 registry id ↔ 项目 itemId (BlockManager) 映射。
 *
 * 简化: 仅支持 shaped/shapeless（transmute 归 shapeless）, 不支持 tag 配料
 * （仅具体 itemId）, 边界对齐匹配 + 镜像匹配 + 贪心无序匹配。
 */
public final class RecipeRegistry {

    public static final class Recipe {
        public final String key;       // recipes.json 的整数 key (String 形式)
        public final int[] grid = new int[9]; // 项目 itemId, 0 = 空气/空
        public final int resultItemId; // 项目 itemId
        public final int resultCount;
        public final boolean shapeless;
        public final int category;     // 配方书分类: 0=建筑 1=红石 2=装备 3=杂项

        Recipe(String key, int[] grid, int resultItemId, int resultCount, boolean shapeless) {
            this.key = key;
            System.arraycopy(grid, 0, this.grid, 0, 9);
            this.resultItemId = resultItemId;
            this.resultCount = resultCount;
            this.shapeless = shapeless;
            this.category = categoryFor(resultItemId);
        }

        public boolean fits2x2() {
            return grid[2] == 0 && grid[5] == 0 && grid[6] == 0 && grid[7] == 0 && grid[8] == 0;
        }

        private static int categoryFor(int itemId) {
            String name = BlockManager.itemIdToName(itemId);
            if (name == null) return 3;
            if (name.endsWith("_planks") || name.endsWith("_slab") || name.endsWith("_stairs")
                || name.endsWith("_wall") || name.endsWith("_fence") || name.equals("stick")
                || name.equals("crafting_table") || name.equals("chest") || name.equals("torch")
                || name.equals("ladder") || name.endsWith("_door") || name.equals("trapdoor")
                || name.endsWith("_trapdoor") || name.equals("fence_gate") || name.endsWith("_fence_gate")
                || name.equals("sign") || name.endsWith("_sign") || name.equals("bookshelf")
                || name.endsWith("_bed") || name.equals("glass_pane") || name.equals("brick")
                || name.equals("stone_bricks") || name.equals("sandstone") || name.equals("dirt_path")
                || name.equals("lantern") || name.equals("chain") || name.equals("campfire")
                || name.equals("soul_campfire") || name.equals("iron_bars") || name.equals("barrel")
                || name.equals("composter") || name.equals("loom") || name.equals("bell")) {
                return 0;
            }
            if (name.equals("redstone") || name.equals("redstone_torch") || name.equals("redstone_block")
                || name.equals("repeater") || name.equals("comparator") || name.equals("lever")
                || name.equals("observer") || name.equals("piston") || name.equals("sticky_piston")
                || name.equals("dispenser") || name.equals("dropper") || name.equals("hopper")
                || name.equals("note_block") || name.equals("jukebox") || name.equals("daylight_detector")
                || name.equals("tripwire_hook") || name.equals("trapped_chest") || name.equals("target")
                || name.equals("lightning_rod") || name.equals("sculk_sensor") || name.equals("redstone_lamp")
                || name.equals("activator_rail") || name.equals("detector_rail") || name.equals("powered_rail")
                || name.equals("rail")) {
                return 1;
            }
            if (name.endsWith("_sword") || name.endsWith("_pickaxe") || name.endsWith("_axe")
                || name.endsWith("_shovel") || name.endsWith("_hoe") || name.endsWith("_helmet")
                || name.endsWith("_chestplate") || name.endsWith("_leggings") || name.endsWith("_boots")
                || name.equals("bow") || name.equals("arrow") || name.equals("shield")
                || name.equals("book") || name.equals("bucket") || name.equals("glass_bottle")
                || name.equals("bread") || name.equals("cake") || name.equals("cookie")
                || name.equals("golden_apple") || name.equals("pumpkin_pie") || name.equals("rabbit_stew")) {
                return 2;
            }
            return 3;
        }
    }

    // recipes.json registry id (整数) → 项目 itemId
    private static final Map<Integer, Integer> REG_TO_ITEM = new HashMap<>();
    // 项目 itemId → 全部产生该 item 的配方 (包括 shapeless/shaped)
    private static final Map<Integer, List<Recipe>> BY_RESULT = new HashMap<>();
    // 按 recipes.json 加载顺序 (原版注册顺序) 的全体配方 —— match 按此顺序返回, 与原版一致
    private static final List<Recipe> ALL_ORDERED = new ArrayList<>();
    private static boolean loaded = false;

    private RecipeRegistry() {}

    public static synchronized void loadAll() {
        if (loaded) return;
        loadItemMap();
        loadRecipes();
        loaded = true;
    }

    /** 扫 items.json 建 registry id → 项目 itemId 映射 */
    private static void loadItemMap() {
        File f = new File("json/1.21.11/items.json");
        if (!f.exists()) {
            System.err.println("[合成] items.json 不存在, 配方系统不可用");
            return;
        }
        try (FileInputStream fis = new FileInputStream(f);
             InputStreamReader r = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            JsonArray arr = JsonParser.parseReader(r).getAsJsonArray();
            int n = 0;
            for (JsonElement e : arr) {
                JsonObject obj = e.getAsJsonObject();
                int regId = obj.get("id").getAsInt();
                String name = obj.get("name").getAsString();
                if (name.startsWith("minecraft:")) name = name.substring(10);
                int itemId = BlockManager.getItemIdByName(name);
                if (itemId > 0) {
                    REG_TO_ITEM.put(regId, itemId);
                    n++;
                }
            }
            System.out.println("[合成] items 映射: " + n + " 个 (registry→项目 itemId)");
        } catch (Exception e) {
            System.err.println("[合成] items.json 加载失败: " + e.getMessage());
        }
    }

    /** 加载 recipes.json 每个 variant (inShape + result) 转 Recipe */
    private static void loadRecipes() {
        File f = new File("json/1.21.11/recipes.json");
        if (!f.exists()) return;
        try (FileInputStream fis = new FileInputStream(f);
             InputStreamReader r = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
            int loaded = 0, skipped = 0;
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                String key = entry.getKey();
                JsonArray variants = entry.getValue().getAsJsonArray();
                for (JsonElement ve : variants) {
                    Recipe rec = parseVariant(key, ve.getAsJsonObject());
                    if (rec != null) {
                        BY_RESULT.computeIfAbsent(rec.resultItemId, k -> new ArrayList<>()).add(rec);
                        ALL_ORDERED.add(rec);
                        loaded++;
                    } else skipped++;
                }
            }
            System.out.println("[合成] recipes 加载: " + loaded + " 个 (" + skipped + " 个跳过/缺原料)");
        } catch (Exception e) {
            System.err.println("[合成] recipes.json 加载失败: " + e.getMessage());
        }
    }

    private static Recipe parseVariant(String key, JsonObject v) {
        // 跳过非 crafting (smelting/stonecutting/special ...)
        if (v.has("inShape")) {
            return parseShapedOrShapeless(key, v, false);
        }
        if (v.has("ingredients")) {
            return parseShapedOrShapeless(key, v, true);
        }
        return null; // 其他类型（熔炉/切石机等）跳过
    }

    private static Recipe parseShapedOrShapeless(String key, JsonObject v, boolean shapeless) {
        int[] grid = new int[9];
        if (shapeless) {
            JsonArray arr = v.getAsJsonArray("ingredients");
            int idx = 0;
            for (JsonElement e : arr) {
                if (idx >= 9) break;
                int itemId = resolveIngredient(e);
                if (itemId > 0) grid[idx++] = itemId;
            }
        } else {
            JsonArray rows = v.getAsJsonArray("inShape");
            int rIdx = 0;
            for (JsonElement rowEl : rows) {
                if (rIdx >= 3) break;
                JsonArray row = rowEl.getAsJsonArray();
                int cIdx = 0;
                for (JsonElement cell : row) {
                    if (cIdx >= 3) break;
                    int itemId = cell.isJsonNull() ? 0 : resolveIngredient(cell);
                    grid[rIdx * 3 + cIdx] = itemId;
                    cIdx++;
                }
                rIdx++;
            }
        }
        if (!hasAnyItem(grid)) return null;
        JsonObject res = v.getAsJsonObject("result");
        int resultRegId = res.get("id").getAsInt();
        int resultItemId = REG_TO_ITEM.getOrDefault(resultRegId, 0);
        if (resultItemId <= 0) return null;
        int count = res.has("count") ? res.get("count").getAsInt() : 1;
        return new Recipe(key, grid, resultItemId, count, shapeless);
    }

    /** ingredient 元素: 数字 id 或 {"id":N} 或 {"item":"minecraft:..."} 或 {"tag":"..."}(不支持, 跳过) */
    private static int resolveIngredient(JsonElement e) {
        if (e.isJsonNull()) return 0;
        if (e.isJsonPrimitive()) {
            int regId = e.getAsInt();
            return REG_TO_ITEM.getOrDefault(regId, 0);
        }
        if (e.isJsonObject()) {
            JsonObject o = e.getAsJsonObject();
            if (o.has("id")) return REG_TO_ITEM.getOrDefault(o.get("id").getAsInt(), 0);
            if (o.has("item")) {
                String name = o.get("item").getAsString();
                if (name.startsWith("minecraft:")) name = name.substring(10);
                return BlockManager.getItemIdByName(name);
            }
            // tag 不支持 (grid 用具体 itemId)
        }
        return 0;
    }

    private static boolean hasAnyItem(int[] grid) {
        for (int g : grid) if (g > 0) return true;
        return false;
    }

    /** 匹配玩家或工作台的 3×3 grid (itemId 数组) → 返回配方; null = 无 */
    public static Recipe match(int[] grid) {
        if (!loaded) loadAll();
        if (grid == null) return null;
        // 第一遍: shaped (正向 + 水平镜像, 与原版一致)
        for (int[] g : transforms(grid)) {
            int minR = 3, maxR = -1, minC = 3, maxC = -1;
            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    if (g[r * 3 + c] != 0) { minR = Math.min(minR, r); maxR = Math.max(maxR, r); minC = Math.min(minC, c); maxC = Math.max(maxC, c); }
            if (maxR < 0) continue;
            int gh = maxR - minR + 1, gw = maxC - minC + 1;
            if (gh > 3 || gw > 3) continue;
            for (Recipe r : ALL_ORDERED) {
                if (r.shapeless) continue;
                int[] rg = r.grid;
                int rMinR = 3, rMaxR = -1, rMinC = 3, rMaxC = -1;
                for (int rr = 0; rr < 3; rr++)
                    for (int cc = 0; cc < 3; cc++)
                        if (rg[rr * 3 + cc] != 0) { rMinR = Math.min(rMinR, rr); rMaxR = Math.max(rMaxR, rr); rMinC = Math.min(rMinC, cc); rMaxC = Math.max(rMaxC, cc); }
                if (rMaxR < 0 || (rMaxR - rMinR + 1) != gh || (rMaxC - rMinC + 1) != gw) continue;
                boolean ok = true;
                for (int rr = 0; rr < gh && ok; rr++)
                    for (int cc = 0; cc < gw && ok; cc++) {
                        int a = rg[(rMinR + rr) * 3 + (rMinC + cc)];
                        int b = g[(minR + rr) * 3 + (minC + cc)];
                        if (a != b) ok = false;
                    }
                if (ok) return r;
            }
        }
        // 第二遍: shapeless (频率匹配, 顺序无关; 变换无意义但保留一致性)
        int neededSize = countNonZero(grid);
        if (neededSize > 0) {
            for (Recipe r : ALL_ORDERED) {
                if (!r.shapeless) continue;
                if (countNonZero(r.grid) != neededSize) continue;
                if (frequencyMatch(grid, r.grid)) return r;
            }
        }
        return null;
    }

    /** 原版语义: 仅恒等 + 水平镜像 (shaped 配方不旋转, 横放木板不能合木棍) */
    private static int[][] transforms(int[] g) {
        return new int[][]{g, mirrorH(g)};
    }


    private static int countNonZero(int[] g) { int n = 0; for (int v : g) if (v != 0) n++; return n; }
    private static int[] nonZeroItems(int[] g) {
        int[] items = new int[countNonZero(g)];
        int j = 0;
        for (int v : g) if (v != 0) items[j++] = v;
        return items;
    }
    private static boolean frequencyMatch(int[] a, int[] b) {
        int[] sortedA = nonZeroItems(a), sortedB = nonZeroItems(b);
        Arrays.sort(sortedA); Arrays.sort(sortedB);
        return Arrays.equals(sortedA, sortedB);
    }
    private static int neededSize(int[] g) { return countNonZero(g); }

    private static int[] mirrorH(int[] g) {
        int[] r = new int[9];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                r[i * 3 + (2 - j)] = g[i * 3 + j];
        return r;
    }

    public static java.util.List<Recipe> all() {
        if (!loaded) loadAll();
        java.util.List<Recipe> all = new ArrayList<>();
        for (java.util.List<Recipe> l : BY_RESULT.values()) all.addAll(l);
        return all;
    }

    /**
     * 计算每格应消耗 1 个的索引 (长度 9, 1=扣, 0=不扣).
     * 与 match 一致支持恒等/水平镜像: 找到与玩家 grid 对齐的配方变换后映射消耗.
     */
    public static int[] consumeMap(Recipe recipe, int[] grid) {
        if (recipe == null || grid == null) return null;
        int[] map = new int[9];
        if (recipe.shapeless) {
            // 无序: 任意包含配方原料的格子各扣 1 个 (原料数 = 配方非空格数, 由 match 保证)
            int need = countNonZero(recipe.grid);
            for (int i = 0; i < 9 && need > 0; i++) {
                if (grid[i] != 0) { map[i] = 1; need--; }
            }
            return map;
        }
        // shaped: 找使 T(recipe.grid) 与 grid 包围盒对齐匹配的变换
        for (int[] tg : transforms(recipe.grid)) {
            int gMinR = 3, gMaxR = -1, gMinC = 3, gMaxC = -1;
            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    if (grid[r * 3 + c] != 0) { gMinR = Math.min(gMinR, r); gMaxR = Math.max(gMaxR, r); gMinC = Math.min(gMinC, c); gMaxC = Math.max(gMaxC, c); }
            if (gMaxR < 0) return map;
            int gh = gMaxR - gMinR + 1, gw = gMaxC - gMinC + 1;
            int tMinR = 3, tMaxR = -1, tMinC = 3, tMaxC = -1;
            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    if (tg[r * 3 + c] != 0) { tMinR = Math.min(tMinR, r); tMaxR = Math.max(tMaxR, r); tMinC = Math.min(tMinC, c); tMaxC = Math.max(tMaxC, c); }
            if (tMaxR < 0 || gh != (tMaxR - tMinR + 1) || gw != (tMaxC - tMinC + 1)) continue;
            boolean ok = true;
            for (int r = 0; r < gh && ok; r++)
                for (int c = 0; c < gw && ok; c++) {
                    int a = tg[(tMinR + r) * 3 + (tMinC + c)];
                    int b = grid[(gMinR + r) * 3 + (gMinC + c)];
                    if (a != b) ok = false;
                }
            if (!ok) continue;
            for (int r = 0; r < gh; r++)
                for (int c = 0; c < gw; c++)
                    if (tg[(tMinR + r) * 3 + (tMinC + c)] != 0) {
                        map[(gMinR + r) * 3 + (gMinC + c)] = 1;
                    }
            return map;
        }
        return map;
    }
}