package com.CharunCore.server.world.entity;

import com.CharunCore.server.utils.BlockManager;

import java.util.*;

/**
 * 村民职业与交易表（原版 1.21.11 忠实还原）。
 *
 * 物品以「注册表名字符串」描述，运行时由 {@link BlockManager#getItemIdByName}
 * 解析为 int id（与本项目 flat int-id 物品模型一致）。若某物品在本地注册表缺失(id<=0)，该条交易会被跳过。
 *
 * 结构：profession -> level(1..5) -> 该等级可交易的报价列表。
 * 除 nitwit / unemployed 外，13 个职业均按原版提供 novice→master 的报价。
 */
public final class VillagerTrades {

    /** 单条报价定义（物品名 + 数量，不携带 NBT）。 */
    public static final class Def {
        public final String aItem;     // 主成本物品名
        public final int aCount;
        public final String bItem;     // 第二成本物品名（可为 null）
        public final int bCount;
        public final String resultItem;// 产出物品名
        public final int resultCount;
        public final int maxUses;      // 补货前最大交易次数
        public final int xp;           // 成交后村民获得的经验
        public final float priceMult;  // 价格乘数（需求波动用，原版默认 0.05）

        public Def(String aItem, int aCount, String resultItem, int resultCount,
                   int maxUses, int xp, float priceMult) {
            this(aItem, aCount, null, 0, resultItem, resultCount, maxUses, xp, priceMult);
        }

        public Def(String aItem, int aCount, String bItem, int bCount,
                   String resultItem, int resultCount, int maxUses, int xp, float priceMult) {
            this.aItem = aItem;
            this.aCount = aCount;
            this.bItem = bItem;
            this.bCount = bCount;
            this.resultItem = resultItem;
            this.resultCount = resultCount;
            this.maxUses = maxUses;
            this.xp = xp;
            this.priceMult = priceMult;
        }
    }

    /** 所有职业名（含无交易的特殊职业）。 */
    public static final List<String> PROFESSIONS = Arrays.asList(
        "farmer", "fisherman", "shepherd", "fletcher",
        "librarian", "cartographer", "cleric",
        "armorer", "weaponsmith", "toolsmith", "butcher",
        "leatherworker", "mason");

    private static final Map<String, List<List<Def>>> TABLE = build();

    private VillagerTrades() {}

    /** 返回某职业某等级（1..5）的报价定义（未定义则空列表）。 */
    public static List<Def> offersFor(String profession, int level) {
        List<List<Def>> byLevel = TABLE.get(profession);
        if (byLevel == null) return Collections.emptyList();
        if (level < 1 || level > byLevel.size()) return Collections.emptyList();
        return byLevel.get(level - 1);
    }

    /** 该职业是否存在（可用于随机分配）。 */
    public static boolean isTradeProfession(String profession) {
        return TABLE.containsKey(profession);
    }

    // ── 构建表 ──────────────────────────────────────────────────────────────
    private static Map<String, List<List<Def>>> build() {
        Map<String, List<List<Def>>> t = new LinkedHashMap<>();

        // 农民 farmer
        t.put("farmer", List.of(
            List.of(
                new Def("wheat", 20, "emerald", 1, 16, 2, 0.05f),
                new Def("potato", 26, "emerald", 1, 16, 2, 0.05f),
                new Def("carrot", 22, "emerald", 1, 16, 2, 0.05f),
                new Def("beetroot", 15, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "pumpkin", 1, 12, 2, 0.05f),
                new Def("emerald", 1, "melon_slice", 4, 12, 2, 0.05f),
                new Def("emerald", 1, "apple", 1, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "cookie", 6, 12, 2, 0.05f),
                new Def("emerald", 3, "suspicious_stew", 1, 12, 10, 0.05f),
                new Def("emerald", 2, "cake", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 1, "golden_carrot", 3, 12, 2, 0.05f),
                new Def("emerald", 3, "glistering_melon_slice", 3, 12, 10, 0.05f),
                new Def("emerald", 2, "pumpkin_pie", 4, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 3, "rabbit_stew", 1, 12, 10, 0.05f),
                new Def("emerald", 4, "bread", 6, 12, 10, 0.05f),
                new Def("emerald", 3, "baked_potato", 6, 12, 10, 0.05f))
        ));

        // 渔夫 fisherman
        t.put("fisherman", List.of(
            List.of(
                new Def("string", 20, "emerald", 1, 16, 2, 0.05f),
                new Def("coal", 10, "emerald", 1, 16, 2, 0.05f),
                new Def("cod", 15, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "bread", 1, 12, 2, 0.05f),
                new Def("emerald", 2, "cooked_salmon", 3, 12, 2, 0.05f),
                new Def("emerald", 1, "cooked_cod", 3, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 3, "campfire", 1, 12, 10, 0.05f),
                new Def("emerald", 3, "lantern", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 1, "baked_potato", 4, 12, 2, 0.05f),
                new Def("emerald", 3, "fishing_rod", 1, 12, 10, 0.05f)),
            List.of(
                new Def("emerald", 8, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 2, "cake", 1, 12, 5, 0.05f))
        ));

        // 牧羊人 shepherd
        t.put("shepherd", List.of(
            List.of(
                new Def("wool", 18, "emerald", 1, 16, 2, 0.05f),
                new Def("rabbit_hide", 9, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "scaffolding", 4, 12, 2, 0.05f),
                new Def("emerald", 2, "shears", 1, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "painting", 1, 12, 2, 0.05f),
                new Def("emerald", 2, "bed", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 1, "suspicious_stew", 1, 12, 10, 0.05f),
                new Def("emerald", 2, "light_blue_bed", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 3, "painting", 3, 12, 10, 0.05f),
                new Def("emerald", 2, "lime_banner", 1, 12, 10, 0.05f))
        ));

        // 制箭师 fletcher
        t.put("fletcher", List.of(
            List.of(
                new Def("string", 20, "emerald", 1, 16, 2, 0.05f),
                new Def("flint", 26, "emerald", 1, 16, 2, 0.05f),
                new Def("feather", 24, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "arrow", 8, 12, 2, 0.05f),
                new Def("emerald", 2, "glass_pane", 4, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 2, "crossbow", 1, 12, 5, 0.05f),
                new Def("emerald", 1, "tripwire_hook", 4, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 7, "bow", 1, 3, 10, 0.05f),
                new Def("emerald", 2, "arrow", 16, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 8, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 8, "crossbow", 1, 3, 10, 0.05f))
        ));

        // 图书管理员 librarian
        t.put("librarian", List.of(
            List.of(
                new Def("paper", 24, "emerald", 1, 16, 2, 0.05f),
                new Def("book", 4, "emerald", 1, 16, 2, 0.05f),
                new Def("ink_sac", 5, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 5, "book", 1, 12, 2, 0.05f),
                new Def("emerald", 5, "lantern", 1, 12, 2, 0.05f),
                new Def("emerald", 4, "glass_bottle", 4, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 8, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 4, "clock", 1, 12, 5, 0.05f),
                new Def("emerald", 5, "compass", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 10, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 7, "name_tag", 1, 12, 10, 0.05f),
                new Def("emerald", 4, "bookshelf", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 12, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 9, "glass_bottle", 4, 12, 10, 0.05f),
                new Def("emerald", 8, "end_crystal", 1, 12, 10, 0.05f))
        ));

        // 制图师 cartographer
        t.put("cartographer", List.of(
            List.of(
                new Def("paper", 24, "emerald", 1, 16, 2, 0.05f),
                new Def("glass_pane", 11, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 7, "map", 1, 12, 2, 0.05f),
                new Def("emerald", 1, "empty_map", 1, 12, 2, 0.05f)),
            List.of(
                new Def("compass", 1, "emerald", 1, 12, 2, 0.05f),
                new Def("emerald", 8, "item_frame", 1, 12, 2, 0.05f),
                new Def("emerald", 4, "globe_banner_pattern", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 8, "compass", 1, 12, 10, 0.05f),
                new Def("emerald", 13, "banner_pattern", 1, 12, 10, 0.05f)),
            List.of(
                new Def("emerald", 14, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 8, "globe_banner_pattern", 1, 12, 10, 0.05f),
                new Def("emerald", 11, "item_frame", 1, 12, 10, 0.05f))
        ));

        // 牧师 cleric
        t.put("cleric", List.of(
            List.of(
                new Def("rotten_flesh", 32, "emerald", 1, 16, 2, 0.05f),
                new Def("gold_ingot", 3, "emerald", 1, 16, 2, 0.05f),
                new Def("scute", 5, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "redstone", 2, 12, 2, 0.05f),
                new Def("emerald", 1, "lapis_lazuli", 1, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "glowstone", 4, 12, 2, 0.05f),
                new Def("emerald", 3, "ender_pearl", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 1, "redstone", 2, 12, 2, 0.05f),
                new Def("emerald", 4, "rabbit_foot", 1, 12, 10, 0.05f)),
            List.of(
                new Def("emerald", 5, "bottle_o_enchanting", 1, 12, 10, 0.05f),
                new Def("emerald", 4, "ender_pearl", 1, 12, 10, 0.05f),
                new Def("emerald", 3, "quartz", 4, 12, 10, 0.05f))
        ));

        // 盔甲匠 armorer
        t.put("armorer", List.of(
            List.of(
                new Def("coal", 20, "emerald", 1, 16, 2, 0.05f),
                new Def("iron_ingot", 4, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "iron_helmet", 1, 12, 2, 0.05f),
                new Def("emerald", 3, "chainmail_chestplate", 1, 12, 5, 0.05f),
                new Def("emerald", 1, "iron_boots", 1, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 3, "chainmail_leggings", 1, 12, 5, 0.05f),
                new Def("emerald", 1, "iron_chestplate", 1, 12, 2, 0.05f),
                new Def("emerald", 1, "bell", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 4, "chainmail_helmet", 1, 12, 10, 0.05f),
                new Def("emerald", 6, "diamond_chestplate", 1, 3, 10, 0.05f),
                new Def("emerald", 5, "shield", 1, 12, 10, 0.05f)),
            List.of(
                new Def("emerald", 8, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 13, "diamond_helmet", 1, 3, 10, 0.05f),
                new Def("emerald", 12, "diamond_boots", 1, 3, 10, 0.05f))
        ));

        // 武器匠 weaponsmith
        t.put("weaponsmith", List.of(
            List.of(
                new Def("coal", 20, "emerald", 1, 16, 2, 0.05f),
                new Def("iron_ingot", 4, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "iron_sword", 1, 12, 2, 0.05f),
                new Def("emerald", 3, "bell", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 2, "iron_axe", 1, 12, 2, 0.05f),
                new Def("emerald", 1, "iron_sword", 1, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 7, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 8, "diamond_sword", 1, 3, 10, 0.05f),
                new Def("emerald", 6, "diamond_axe", 1, 3, 10, 0.05f)),
            List.of(
                new Def("emerald", 8, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 13, "diamond_sword", 1, 3, 10, 0.05f),
                new Def("emerald", 17, "diamond_axe", 1, 3, 10, 0.05f))
        ));

        // 工具匠 toolsmith
        t.put("toolsmith", List.of(
            List.of(
                new Def("coal", 20, "emerald", 1, 16, 2, 0.05f),
                new Def("iron_ingot", 4, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "iron_shovel", 1, 12, 2, 0.05f),
                new Def("emerald", 1, "iron_pickaxe", 1, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 2, "iron_axe", 1, 12, 2, 0.05f),
                new Def("emerald", 1, "flint_and_steel", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 7, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 8, "diamond_pickaxe", 1, 3, 10, 0.05f),
                new Def("emerald", 6, "diamond_shovel", 1, 3, 10, 0.05f)),
            List.of(
                new Def("emerald", 8, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 13, "diamond_pickaxe", 1, 3, 10, 0.05f),
                new Def("emerald", 17, "diamond_axe", 1, 3, 10, 0.05f))
        ));

        // 屠夫 butcher
        t.put("butcher", List.of(
            List.of(
                new Def("raw_porkchop", 14, "emerald", 1, 16, 2, 0.05f),
                new Def("raw_chicken", 14, "emerald", 1, 16, 2, 0.05f),
                new Def("rabbit", 4, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "rabbit_stew", 1, 12, 2, 0.05f),
                new Def("emerald", 1, "cooked_porkchop", 5, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "cooked_chicken", 8, 12, 2, 0.05f),
                new Def("emerald", 1, "dried_kelp", 10, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "cooked_rabbit", 6, 12, 2, 0.05f),
                new Def("emerald", 1, "cooked_mutton", 7, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 3, "sweet_berries", 10, 12, 10, 0.05f),
                new Def("emerald", 2, "honey_bottle", 4, 12, 10, 0.05f),
                new Def("emerald", 3, "cooked_porkchop", 8, 12, 10, 0.05f))
        ));

        // 皮匠 leatherworker
        t.put("leatherworker", List.of(
            List.of(
                new Def("leather", 6, "emerald", 1, 16, 2, 0.05f),
                new Def("rabbit_hide", 9, "emerald", 1, 16, 2, 0.05f),
                new Def("flint", 26, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "leather_leggings", 1, 12, 2, 0.05f),
                new Def("emerald", 1, "leather_boots", 1, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 2, "leather_tunic", 1, 12, 2, 0.05f),
                new Def("emerald", 2, "leather_horse_armor", 1, 12, 5, 0.05f)),
            List.of(
                new Def("emerald", 2, "leather_helmet", 1, 12, 2, 0.05f),
                new Def("emerald", 4, "saddle", 1, 12, 10, 0.05f)),
            List.of(
                new Def("emerald", 8, "enchanted_book", 1, 12, 10, 0.05f),
                new Def("emerald", 5, "leather_horse_armor", 1, 12, 10, 0.05f))
        ));

        // 石匠 mason
        t.put("mason", List.of(
            List.of(
                new Def("clay_ball", 10, "emerald", 1, 16, 2, 0.05f),
                new Def("stone", 20, "emerald", 1, 16, 2, 0.05f),
                new Def("andesite", 16, "emerald", 1, 16, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "dripstone_block", 4, 12, 2, 0.05f),
                new Def("emerald", 1, "polished_andesite", 4, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "terracotta", 1, 12, 2, 0.05f),
                new Def("emerald", 1, "polished_diorite", 4, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "quartz_block", 1, 12, 5, 0.05f),
                new Def("emerald", 1, "polished_granite", 4, 12, 2, 0.05f)),
            List.of(
                new Def("emerald", 1, "cracked_stone_bricks", 4, 12, 10, 0.05f),
                new Def("emerald", 2, "glazed_terracotta", 1, 12, 10, 0.05f),
                new Def("emerald", 1, "quartz_pillar", 1, 12, 10, 0.05f))
        ));

        return t;
    }
}
