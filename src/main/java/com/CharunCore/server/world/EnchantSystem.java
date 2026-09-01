package com.CharunCore.server.world;

import com.CharunCore.server.utils.BlockManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class EnchantSystem {

    /** 单个附魔实例, 对齐原版 EnchantmentInstance (附魔 id + 等级)。 */
    public static final class EnchantInstance {
        public final int enchantId;
        public final int level;
        public EnchantInstance(int enchantId, int level) {
            this.enchantId = enchantId;
            this.level = level;
        }
    }

    /** 附魔台一个槽位的结果: 经验等级消耗 + 本槽施加的全部附魔 (1-3, 对齐 selectEnchantment)。 */
    public static final class Option {
        public int cost;
        public final List<EnchantInstance> enchantments = new ArrayList<>();
        /** 主附魔字段 (取列表中第一个), 兼容旧调用方直接读 enchantId/level。 */
        public int enchantId = -1;
        public int level = 0;
    }

    /** 附魔定义 (对齐原版 min_cost/max_cost 与 weight/rarity)。 */
    private static final class Def {
        final int minBase, minPer, maxBase, maxPer, maxLevel, weight;
        final String category;
        final String group;
        Def(int minBase, int minPer, int maxBase, int maxPer, int maxLevel, int weight, String category, String group) {
            this.minBase = minBase; this.minPer = minPer;
            this.maxBase = maxBase; this.maxPer = maxPer;
            this.maxLevel = maxLevel; this.weight = weight;
            this.category = category; this.group = group;
        }
    }

    private static final Map<String, Def> DEFS = new HashMap<>();
    private static final Set<String> TREASURE = new java.util.HashSet<>();
    static {
        // 数据取自 json/minecraft/enchantment/*.json (1.21.11 原版 min_cost/max_cost/weight)。
        // 互斥组: damage / protection / mining / feet (对齐 Enchantment.exclusiveSet)。
        DEFS.put("sharpness",            new Def(1, 11, 21, 11, 5, 10, "weapon", "damage"));
        DEFS.put("smite",                new Def(5, 8, 25, 8, 5, 5, "weapon", "damage"));
        DEFS.put("bane_of_arthropods",   new Def(5, 8, 25, 8, 5, 5, "weapon", "damage"));
        DEFS.put("looting",              new Def(15, 9, 65, 9, 3, 2, "weapon", null));
        DEFS.put("knockback",            new Def(5, 20, 55, 20, 2, 5, "weapon", null));
        DEFS.put("fire_aspect",          new Def(10, 20, 60, 20, 2, 2, "weapon", null));
        DEFS.put("sweeping_edge",        new Def(5, 9, 20, 9, 3, 2, "weapon", null));

        DEFS.put("efficiency",           new Def(1, 10, 51, 10, 5, 10, "tool", null));
        DEFS.put("fortune",              new Def(15, 9, 65, 9, 3, 2, "tool", "mining"));
        DEFS.put("silk_touch",           new Def(15, 0, 65, 0, 1, 1, "tool", "mining"));

        DEFS.put("unbreaking",           new Def(5, 8, 55, 8, 3, 5, "any", null));
        DEFS.put("mending",              new Def(25, 25, 75, 25, 1, 2, "any", null)); // 宝藏

        DEFS.put("protection",           new Def(1, 11, 12, 11, 4, 10, "armor", "protection"));
        DEFS.put("fire_protection",      new Def(10, 8, 18, 8, 4, 5, "armor", "protection"));
        DEFS.put("blast_protection",     new Def(5, 8, 13, 8, 4, 2, "armor", "protection"));
        DEFS.put("projectile_protection",new Def(3, 6, 9, 6, 4, 5, "armor", "protection"));
        DEFS.put("thorns",               new Def(10, 20, 60, 20, 3, 1, "armor", null));
        DEFS.put("respiration",          new Def(10, 10, 40, 10, 3, 2, "armor", null));
        DEFS.put("aqua_affinity",        new Def(1, 0, 41, 0, 1, 2, "armor", null));
        DEFS.put("depth_strider",        new Def(10, 10, 25, 10, 3, 2, "armor", "feet"));
        DEFS.put("frost_walker",         new Def(10, 10, 25, 10, 2, 2, "armor", "feet")); // 宝藏
        DEFS.put("feather_falling",      new Def(5, 6, 11, 6, 4, 5, "armor", null));

        DEFS.put("power",                new Def(1, 10, 16, 10, 5, 10, "bow", null));
        DEFS.put("punch",                new Def(12, 20, 37, 20, 2, 2, "bow", null));
        DEFS.put("flame",                new Def(20, 0, 50, 0, 1, 2, "bow", null));
        DEFS.put("infinity",             new Def(20, 0, 50, 0, 1, 1, "bow", null));

        DEFS.put("curse_of_vanishing",   new Def(25, 0, 50, 0, 1, 1, "any", null)); // 宝藏
        DEFS.put("curse_of_binding",     new Def(25, 0, 50, 0, 1, 1, "armor", null)); // 宝藏

        // ── P-补全: 三叉戟/弩/重锤 相关附魔 (1.21.11 原版 min_cost/max_cost) ──
        DEFS.put("loyalty",              new Def(12, 7, 50, 0, 3, 5, "trident", null));
        DEFS.put("impaling",             new Def(1, 8, 21, 8, 5, 2, "trident", null));
        DEFS.put("riptide",              new Def(10, 7, 40, 7, 3, 2, "trident", "riptide"));
        DEFS.put("channeling",           new Def(25, 0, 50, 0, 1, 1, "trident", "riptide"));
        DEFS.put("multishot",            new Def(20, 0, 50, 0, 1, 2, "crossbow", "crossbow"));
        DEFS.put("piercing",             new Def(1, 10, 51, 10, 4, 3, "crossbow", "crossbow"));
        DEFS.put("quick_charge",         new Def(12, 20, 37, 20, 3, 2, "crossbow", null));
        DEFS.put("breach",               new Def(15, 8, 55, 8, 4, 5, "mace", "damage"));
        DEFS.put("density",              new Def(5, 8, 25, 8, 5, 5, "mace", "damage"));
        DEFS.put("wind_burst",           new Def(15, 9, 45, 9, 3, 2, "mace", null)); // 宝藏
        DEFS.put("soul_speed",           new Def(10, 10, 25, 10, 3, 1, "armor", "feet")); // 宝藏

        TREASURE.add("mending");
        TREASURE.add("frost_walker");
        TREASURE.add("curse_of_vanishing");
        TREASURE.add("curse_of_binding");
        TREASURE.add("wind_burst");
        TREASURE.add("soul_speed");
    }

    /** 附魔名称 → 最大等级（原版 Enchantment.getMaxLevel）。未知返回 1。供铁砧合并等使用。 */
    public static int getEnchantMaxLevel(String name) {
        if (name == null) return 1;
        if (name.startsWith("minecraft:")) name = name.substring(10);
        Def d = DEFS.get(name);
        return d != null ? d.maxLevel : 1;
    }

    // ── Bug6/12: 铁砧按注册表 id 操作附魔所需的定义查询 ─────────────────────
    private static final Map<Integer, String> ID_TO_NAME = new HashMap<>();
    private static final Map<Integer, Integer> ID_TO_ANVIL_COST = new HashMap<>();
    private static final Map<String, Integer> NAME_TO_ID = new HashMap<>();

    static {
        try (java.io.Reader r = new java.io.InputStreamReader(
                new java.io.FileInputStream("data/enchantment.json"),
                java.nio.charset.StandardCharsets.UTF_8)) {
            com.google.gson.JsonObject root = com.google.gson.JsonParser.parseReader(r).getAsJsonObject();
            for (com.google.gson.JsonElement e : root.getAsJsonObject("minecraft:enchantment").getAsJsonArray("value")) {
                com.google.gson.JsonObject o = e.getAsJsonObject();
                int id = o.get("id").getAsInt();
                String name = o.get("name").getAsString();
                ID_TO_NAME.put(id, name.startsWith("minecraft:") ? name.substring(10) : name);
                NAME_TO_ID.put(name.startsWith("minecraft:") ? name.substring(10) : name, id);
                ID_TO_ANVIL_COST.put(id, o.getAsJsonObject("element").get("anvil_cost").getAsInt());
            }
        } catch (Exception ignored) {}
    }

    /** 附魔注册表 id → 最大等级; 未知返回 1。 */
    public static int maxLevelById(int enchantId) {
        String n = ID_TO_NAME.get(enchantId);
        return getEnchantMaxLevel(n);
    }

    /** 附魔注册表 id → 原版 anvil_cost (铁砧消耗权重); 未知返回 1。 */
    public static int anvilCostById(int enchantId) {
        return ID_TO_ANVIL_COST.getOrDefault(enchantId, 1);
    }

    /** 原版 Enchantment.areCompatible: 同一互斥组(exclusive_set)的两个附魔不兼容。 */
    public static boolean areCompatibleById(int a, int b) {
        Def da = a == b ? null : DEFS.get(ID_TO_NAME.get(a));
        Def db = DEFS.get(ID_TO_NAME.get(b));
        String ga = da == null ? null : da.group;
        String gb = db == null ? null : db.group;
        return ga == null || gb == null || !ga.equals(gb);
    }

    private static String itemCategory(String itemName) {
        if (itemName == null) return "none";
        if (itemName.endsWith("_sword")) return "weapon";
        if (itemName.endsWith("_pickaxe") || itemName.endsWith("_axe")
                || itemName.endsWith("_shovel") || itemName.endsWith("_hoe")) return "tool";
        if (itemName.endsWith("_helmet") || itemName.endsWith("_chestplate")
                || itemName.endsWith("_leggings") || itemName.endsWith("_boots")) return "armor";
        if (itemName.equals("bow")) return "bow";
        if (itemName.equals("trident")) return "trident";
        if (itemName.equals("crossbow")) return "crossbow";
        if (itemName.equals("mace")) return "mace";
        if (itemName.equals("book")) return "book";
        return "none";
    }

    /** 物品附魔能力值 (对齐原版 DataComponents.ENCHANTABLE 的 value)。 */
    private static int enchantability(String name) {
        if (name == null) return 1;
        if (name.equals("book") || name.equals("bow") || name.equals("crossbow")
                || name.equals("fishing_rod") || name.equals("trident") || name.equals("shield")) return 1;
        if (name.startsWith("wooden_") || name.startsWith("leather_") || name.startsWith("netherite_")) return 15;
        if (name.startsWith("golden_")) return 22;
        if (name.startsWith("stone_")) return 5;
        if (name.startsWith("iron_")) return 14;
        if (name.startsWith("chainmail_")) return 12;
        if (name.startsWith("turtle_")) return 9;
        if (name.startsWith("diamond_")) return 10;
        return 1;
    }

    private static int minCost(String e, int lvl) {
        Def d = DEFS.get(e);
        return d.minBase + d.minPer * (lvl - 1);
    }

    private static int maxCost(String e, int lvl) {
        Def d = DEFS.get(e);
        return d.maxBase + d.maxPer * (lvl - 1);
    }

    /** 对齐 EnchantmentHelper.getEnchantmentCost: 槽位 n 的经验等级消耗 (也是 selectEnchantment 的 power 预算)。 */
    public static int getEnchantmentCost(Random r, int slot, int bookshelf) {
        if (bookshelf > 15) bookshelf = 15;
        int n3 = r.nextInt(8) + 1 + (bookshelf >> 1) + r.nextInt(bookshelf + 1);
        if (slot == 0) return Math.max(n3 / 3, 1);
        if (slot == 1) return n3 * 2 / 3 + 1;
        return Math.max(n3, bookshelf * 2);
    }

    private static final class Cand {
        final String name; final int id; final int level; final int weight; final String group;
        Cand(String name, int id, int level, int weight, String group) {
            this.name = name; this.id = id; this.level = level; this.weight = weight; this.group = group;
        }
    }

    /** 对齐 EnchantmentHelper.getAvailableEnchantmentResults: 在 power 预算内可获得的 (附魔, 最高等级) 候选。 */
    private static List<Cand> getAvailableEnchantmentResults(int power, String cat, boolean isBook) {
        List<Cand> list = new ArrayList<>();
        for (Map.Entry<String, Def> e : DEFS.entrySet()) {
            String name = e.getKey();
            Def d = e.getValue();
            if (TREASURE.contains(name)) continue;                       // 宝藏附魔不在附魔台出现
            if (!isBook && !"any".equals(d.category) && !d.category.equals(cat)) continue; // 书不限定类别
            int id = BlockManager.getEnchantId(name);
            if (id < 0) continue;
            for (int lvl = d.maxLevel; lvl >= 1; lvl--) {
                if (power >= minCost(name, lvl) && power <= maxCost(name, lvl)) {
                    list.add(new Cand(name, id, lvl, d.weight, d.group));
                    break;
                }
            }
        }
        return list;
    }

    private static Cand weightedPick(Random r, List<Cand> cands) {
        int total = 0;
        for (Cand c : cands) total += c.weight;
        int roll = r.nextInt(total);
        for (Cand c : cands) {
            roll -= c.weight;
            if (roll < 0) return c;
        }
        return cands.get(cands.size() - 1);
    }

    private static void filterCompatible(List<Cand> cands, List<Cand> chosen) {
        Set<String> groups = new java.util.HashSet<>();
        for (Cand c : chosen) if (c.group != null) groups.add(c.group);
        cands.removeIf(c -> c.group != null && groups.contains(c.group));
    }

    /** 对齐 EnchantmentHelper.selectEnchantment: power 预算 → 加权选 1 个, 再按递减预算叠加 (最多 3, 书最多 2)。 */
    private static List<EnchantInstance> selectEnchantment(Random r, String itemName, int cost, boolean isBook) {
        int ench = enchantability(itemName);
        int power = cost + 1 + r.nextInt(ench / 4 + 1) + r.nextInt(ench / 4 + 1);
        float f = (r.nextFloat() + r.nextFloat() - 1.0f) * 0.15f;
        power = Math.round(power + power * f);
        if (power < 1) power = 1;

        List<EnchantInstance> result = new ArrayList<>();
        List<Cand> candidates = getAvailableEnchantmentResults(power, itemCategory(itemName), isBook);
        if (candidates.isEmpty()) return result;

        List<Cand> chosen = new ArrayList<>();
        Cand first = weightedPick(r, candidates);
        chosen.add(first);
        while (r.nextInt(50) <= power) {
            filterCompatible(candidates, chosen);
            if (candidates.isEmpty()) break;
            Cand next = weightedPick(r, candidates);
            chosen.add(next);
            power /= 2;
        }
        if (isBook && chosen.size() > 1) {
            chosen.remove(r.nextInt(chosen.size())); // 书最多 2 个附魔
        }
        for (Cand c : chosen) result.add(new EnchantInstance(c.id, c.level));
        return result;
    }

    /**
     * 计算附魔台 3 个槽位选项, 对齐原版 EnchantmentMenu 行为。
     * 调用方须传入玩家稳定种子 (data.enchantmentSeed), 仅在一次附魔后重掷, 以复现原版"物品与书架确定则选项固定"。
     *
     * @param itemName   输入物品名 (book / *_sword / *_pickaxe / 盔甲 / bow ...)
     * @param bookshelf  有效书架数 (0-15)
     * @param playerLevel 玩家经验等级 (仅用于展示/门槛, 原版算法本身不用于计算)
     * @param seed       稳定附魔种子
     */
    public static Option[] compute(String itemName, int bookshelf, int playerLevel, long seed) {
        Option[] out = { new Option(), new Option(), new Option() };
        String cat = itemCategory(itemName);
        if ("none".equals(cat)) return out;
        boolean isBook = "book".equals(cat);

        int[] cost = new int[3];
        Random r = new Random(seed);
        for (int i = 0; i < 3; i++) {
            cost[i] = getEnchantmentCost(r, i, bookshelf);
            if (cost[i] < i + 1) cost[i] = 0; // 槽位 n 至少需 n+1 级 (对齐 EnchantmentMenu:117-123)
        }
        for (int i = 0; i < 3; i++) {
            out[i].cost = cost[i];
            if (cost[i] <= 0) continue;
            Random r2 = new Random(seed + i); // 对齐 getEnchantmentList: random.setSeed(seed + n)
            List<EnchantInstance> list = selectEnchantment(r2, itemName, cost[i], isBook);
            out[i].enchantments.addAll(list);
            if (!list.isEmpty()) {
                out[i].enchantId = list.get(0).enchantId;
                out[i].level = list.get(0).level;
            }
        }
        return out;
    }
}
