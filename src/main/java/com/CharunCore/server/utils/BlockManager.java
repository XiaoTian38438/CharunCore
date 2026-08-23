package com.CharunCore.server.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BlockManager {

    private static final Map<Integer, Integer> blockIdToState   = new HashMap<>();
    private static final Map<String, Integer>  nameToState      = new HashMap<>();
    private static final Map<Integer, String>  itemIdToName     = new HashMap<>();
    private static final Map<String, Integer>  ITEM_STACK       = new HashMap<>();
    private static final Map<String, java.util.Set<Integer>> blockHarvestTools = new HashMap<>();
    private static final Map<String, Integer> enchantNameToId  = new HashMap<>();
    private static final java.util.Set<String> fortuneableBlocks = new java.util.HashSet<>();

    /**
     * 工具挖掘等级 (tier)：对照原版 Tiers — wood=0, copper/stone=1, iron=2, diamond=3, netherite=4。
     * gold 与原版一致等同 wood(0)。用于 canHarvest 的等级比对（修复木镐挖钻石矿）。
     * 无等级的"工具"（剪刀/剑）不在此表，由调用方按类别单独处理。
     */
    private static final Map<String, Integer> TOOL_TIER = new HashMap<>();
    static {
        String[] tiers = {"wooden", "copper", "stone", "iron", "golden", "diamond", "netherite"};
        int[] level = {0, 1, 1, 2, 0, 3, 4};
        for (int i = 0; i < tiers.length; i++) {
            String p = tiers[i];
            TOOL_TIER.put(p + "_pickaxe", level[i]);
            TOOL_TIER.put(p + "_axe", level[i]);
            TOOL_TIER.put(p + "_shovel", level[i]);
            TOOL_TIER.put(p + "_hoe", level[i]);
        }
    }
    static {
        for (String n : new String[]{
            "coal_ore", "deepslate_coal_ore",
            "diamond_ore", "deepslate_diamond_ore",
            "emerald_ore", "deepslate_emerald_ore",
            "lapis_ore", "deepslate_lapis_ore",
            "redstone_ore", "deepslate_redstone_ore",
            "nether_quartz_ore", "nether_gold_ore",
            "iron_ore", "deepslate_iron_ore",
            "gold_ore", "deepslate_gold_ore",
            "copper_ore", "deepslate_copper_ore",
            "ancient_debris",
            "glowstone", "sea_lantern", "clay", "melon"
        }) fortuneableBlocks.add(n);
    }

    public static void init() {
        loadBlocks();
        loadItems();
        loadEnchantments();
        loadHardness();
    }

    /** 从 json/1.21.11/blocks.json 载入全部方块原版硬度 (花/火把/拉杆等 hardness=0 → 可徒手秒破)。 */
    private static void loadHardness() {
        try (FileReader r = new FileReader(Paths.get("json", "1.21.11", "blocks.json").toString())) {
            JsonArray array = JsonParser.parseReader(r).getAsJsonArray();
            int n = 0;
            for (JsonElement el : array) {
                JsonObject obj = el.getAsJsonObject();
                if (!obj.has("hardness")) continue;
                float h = obj.get("hardness").getAsFloat();
                blockHardness.put(obj.get("name").getAsString(), h);
                n++;
            }
            System.out.println("[数据] 已从 blocks.json 载入 " + n + " 个方块硬度");
        } catch (Exception e) {
            System.err.println("[数据] blocks.json 硬度加载失败(沿用内置表): " + e.getMessage());
        }
    }

    private static void loadBlocks() {
        try {
            String path = Paths.get("block", "blocks.json").toString();
            JsonArray array = JsonParser.parseReader(new FileReader(path)).getAsJsonArray();
            for (JsonElement el : array) {
                JsonObject obj   = el.getAsJsonObject();
                int    id           = obj.get("id").getAsInt();
                int    defaultState = obj.get("defaultState").getAsInt();
                String name         = obj.get("name").getAsString();
                blockIdToState.put(id,   defaultState);
                nameToState.put(name,    defaultState);
                if (obj.has("harvestTools")) {
                    JsonObject ht = obj.getAsJsonObject("harvestTools");
                    java.util.Set<Integer> s = new java.util.HashSet<>();
                    for (java.util.Map.Entry<String, JsonElement> e : ht.entrySet()) {
                        if (e.getValue().getAsBoolean()) s.add(Integer.parseInt(e.getKey()));
                    }
                    blockHarvestTools.put(name, s);
                }
            }
            System.out.println("[数据] 已加载 " + blockIdToState.size() + " 个方块定义");
        } catch (Exception e) {
            System.err.println("[错误] 无法加载 blocks.json: " + e.getMessage());
        }
    }

    private static void loadEnchantments() {
        try {
            String path = Paths.get("data", "enchantment.json").toString();
            JsonObject root = JsonParser.parseReader(new FileReader(path)).getAsJsonObject();
            JsonObject enc = root.getAsJsonObject("minecraft:enchantment");
            JsonArray value = enc.getAsJsonArray("value");
            for (JsonElement el : value) {
                JsonObject o = el.getAsJsonObject();
                int id = o.get("id").getAsInt();
                String name = o.get("name").getAsString();
                if (name.startsWith("minecraft:")) name = name.substring(10);
                enchantNameToId.put(name, id);
            }
            System.out.println("[数据] 已加载 " + enchantNameToId.size() + " 个附魔定义");
        } catch (Exception e) {
            System.err.println("[警告] 无法加载 enchantment.json: " + e.getMessage());
        }
    }

    public static boolean canHarvest(String blockName, int heldItemId) {
        java.util.Set<Integer> tools = blockHarvestTools.get(blockName);
        if (tools == null || tools.isEmpty()) return true; // 无工具要求 → 空手也可挖(掉落)
        if (heldItemId <= 0) return false;
        String heldCat = toolCategory(heldItemId);
        if (heldCat == null) return false; // 手持非工具, 不能挖需要工具的方块
        int heldTier = toolTier(heldItemId);
        // 在 harvestTools 集合中, 与本手持类别相同的工具里取最低所需等级;
        // 若集合中存在同类别的"无等级"工具(剪刀/剑), 则该类无需等级比对。
        int requiredTier = Integer.MAX_VALUE;
        boolean tierlessMatch = false;
        for (int t : tools) {
            String tc = toolCategory(t);
            if (tc == null || !tc.equals(heldCat)) continue;
            int tier = toolTier(t);
            if (tier < 0) tierlessMatch = true;          // 剪刀/剑之类无等级工具
            else requiredTier = Math.min(requiredTier, tier);
        }
        if (tierlessMatch) return true;                   // 手持同类无等级工具即可
        if (requiredTier == Integer.MAX_VALUE) return false; // 该类别无允许工具
        return heldTier >= requiredTier;                  // 等级比对(木镐 < 铁级 → 失败)
    }

    /** 由物品 id 推断工具类别(pickaxe/axe/shovel/hoe/shears), 非工具返回 null。 */
    public static String toolCategory(int itemId) {
        String n = itemIdToName(itemId);
        if (n == null) return null;
        if (n.contains("pickaxe")) return "pickaxe";
        if (n.contains("axe")) return "axe";
        if (n.contains("shovel")) return "shovel";
        if (n.contains("hoe")) return "hoe";
        if (n.contains("shears")) return "shears";
        return null;
    }

    /**
     * 手持物品的挖掘等级 (0=wood, 1=copper/stone, 2=iron, 3=diamond, 4=netherite)。
     * 非分级工具(剪刀/剑)或无工具返回 -1。
     */
    public static int toolTier(int itemId) {
        String n = itemIdToName(itemId);
        if (n == null) return -1;
        Integer tier = TOOL_TIER.get(n);
        return tier == null ? -1 : tier;
    }

    public static int getEnchantId(String name) {
        if (name == null) return -1;
        if (name.startsWith("minecraft:")) name = name.substring(10);
        return enchantNameToId.getOrDefault(name, -1);
    }

    public static boolean isFortuneable(String blockName) {
        return fortuneableBlocks.contains(blockName);
    }

    // ── 挖掘速度 / 反作弊 ──────────────────────────────────────────────────
    private static final Map<String, Float> blockHardness = new HashMap<>();
    static {
        // 常见方块硬度 (原版值, 单位: 秒·速度因子)。未知方块默认 0.5。
        blockHardness.put("air", 0.0f);
        blockHardness.put("dirt", 0.5f);
        blockHardness.put("grass_block", 0.6f);
        blockHardness.put("sand", 0.5f);
        blockHardness.put("gravel", 0.6f);
        blockHardness.put("coarse_dirt", 0.5f);
        blockHardness.put("podzol", 0.5f);
        blockHardness.put("mycelium", 0.6f);
        blockHardness.put("snow", 0.1f);
        blockHardness.put("snow_block", 0.2f);
        blockHardness.put("clay", 0.6f);
        blockHardness.put("soul_sand", 0.5f);
        blockHardness.put("soul_soil", 0.5f);
        blockHardness.put("netherrack", 0.4f);
        blockHardness.put("magma_block", 0.5f);
        blockHardness.put("glowstone", 0.3f);
        blockHardness.put("cobweb", 4.0f);
        blockHardness.put("oak_log", 2.0f);
        blockHardness.put("spruce_log", 2.0f);
        blockHardness.put("birch_log", 2.0f);
        blockHardness.put("jungle_log", 2.0f);
        blockHardness.put("acacia_log", 2.0f);
        blockHardness.put("dark_oak_log", 2.0f);
        blockHardness.put("mangrove_log", 2.0f);
        blockHardness.put("cherry_log", 2.0f);
        blockHardness.put("pale_oak_log", 2.0f);
        blockHardness.put("crimson_stem", 2.0f);
        blockHardness.put("warped_stem", 2.0f);
        blockHardness.put("oak_planks", 2.0f);
        blockHardness.put("oak_wood", 2.0f);
        blockHardness.put("stone", 2.0f);
        blockHardness.put("cobblestone", 2.0f);
        blockHardness.put("andesite", 1.5f);
        blockHardness.put("diorite", 1.5f);
        blockHardness.put("granite", 1.5f);
        blockHardness.put("deepslate", 3.0f);
        blockHardness.put("cobbled_deepslate", 3.5f);
        blockHardness.put("sandstone", 0.8f);
        blockHardness.put("obsidian", 50.0f);
        blockHardness.put("crying_obsidian", 50.0f);
        blockHardness.put("enchanting_table", 5.0f);
        blockHardness.put("anvil", 5.0f);
        blockHardness.put("iron_block", 5.0f);
        blockHardness.put("gold_block", 3.0f);
        blockHardness.put("diamond_block", 5.0f);
        blockHardness.put("netherite_block", 50.0f);
        blockHardness.put("bedrock", 3600000.0f);
        blockHardness.put("coal_ore", 3.0f);
        blockHardness.put("deepslate_coal_ore", 4.5f);
        blockHardness.put("iron_ore", 3.0f);
        blockHardness.put("deepslate_iron_ore", 4.5f);
        blockHardness.put("copper_ore", 3.0f);
        blockHardness.put("deepslate_copper_ore", 4.5f);
        blockHardness.put("gold_ore", 3.0f);
        blockHardness.put("deepslate_gold_ore", 4.5f);
        blockHardness.put("redstone_ore", 3.0f);
        blockHardness.put("deepslate_redstone_ore", 4.5f);
        blockHardness.put("emerald_ore", 3.0f);
        blockHardness.put("deepslate_emerald_ore", 4.5f);
        blockHardness.put("lapis_ore", 3.0f);
        blockHardness.put("deepslate_lapis_ore", 4.5f);
        blockHardness.put("diamond_ore", 3.0f);
        blockHardness.put("deepslate_diamond_ore", 4.5f);
        blockHardness.put("nether_quartz_ore", 3.0f);
        blockHardness.put("nether_gold_ore", 3.0f);
        blockHardness.put("ancient_debris", 30.0f);
        blockHardness.put("end_stone", 3.0f);
        blockHardness.put("iron_bars", 5.0f);
        blockHardness.put("glass", 0.3f);
        blockHardness.put("bookshelf", 1.5f);
        blockHardness.put("crafting_table", 2.5f);
        blockHardness.put("furnace", 3.5f);
        blockHardness.put("smoker", 3.5f);
        blockHardness.put("blast_furnace", 3.5f);
        blockHardness.put("chest", 2.5f);
        blockHardness.put("ender_chest", 22.5f);
        blockHardness.put("mob_spawner", 5.0f);
        blockHardness.put("ice", 0.5f);
        blockHardness.put("packed_ice", 0.5f);
        blockHardness.put("blue_ice", 0.5f);
        blockHardness.put("terracotta", 1.25f);
        blockHardness.put("nether_brick", 2.0f);
        blockHardness.put("bricks", 2.0f);
        blockHardness.put("stone_bricks", 1.5f);
        blockHardness.put("end_stone_bricks", 3.0f);
        blockHardness.put("prismarine", 1.5f);
    }

    public static float getBlockHardness(String blockName) {
        return blockHardness.getOrDefault(blockName, 0.5f);
    }

    /** 最佳工具速度 (netherite 9.0) — 用于反作弊下界估算, 合法玩家永远 >= 此值。 */
    public static float bestToolSpeed() {
        return 9.0f;
    }

    /**
     * 最佳情况下挖穿该方块所需秒数 (用最佳工具速度, 假设用对工具)。
     * 用于服务端反作弊: 合法玩家实际耗时 >= 此值, 改包瞬破会被拒绝。
     */
    public static float getBreakSecondsBestCase(String blockName) {
        float h = getBlockHardness(blockName);
        if (h <= 0.0f) return 0.0f;
        return (h * 1.5f) / bestToolSpeed();
    }

    /** 判断手持物品是否为某种工具 (pickaxe/axe/shovel), 用于"对工具"判定。 */
    public static String getToolType(int heldItemId) {
        String name = itemIdToName(heldItemId);
        if (name == null) return "none";
        if (name.endsWith("_pickaxe")) return "pickaxe";
        if (name.endsWith("_axe")) return "axe";
        if (name.endsWith("_shovel")) return "shovel";
        if (name.endsWith("_hoe")) return "hoe";
        if (name.endsWith("_sword")) return "sword";
        return "hand";
    }

    /** 方块需要的工具类型 (与 harvest 工具映射一致)。 */
    public static String getRequiredTool(String blockName) {
        if (blockName.endsWith("_ore") || blockName.endsWith("_log") || blockName.endsWith("_wood")
                || blockName.equals("stone") || blockName.equals("cobblestone") || blockName.equals("andesite")
                || blockName.equals("diorite") || blockName.equals("granite") || blockName.equals("deepslate")
                || blockName.equals("cobbled_deepslate") || blockName.equals("obsidian")
                || blockName.equals("crying_obsidian") || blockName.equals("enchanting_table")
                || blockName.equals("anvil") || blockName.equals("iron_block") || blockName.equals("gold_block")
                || blockName.equals("diamond_block") || blockName.equals("netherite_block")
                || blockName.equals("iron_bars") || blockName.equals("mob_spawner")
                || blockName.equals("sandstone") || blockName.equals("nether_brick") || blockName.equals("bricks")
                || blockName.equals("stone_bricks") || blockName.equals("end_stone_bricks")
                || blockName.equals("prismarine") || blockName.equals("end_stone"))
            return "pickaxe";
        if (blockName.endsWith("_planks") || blockName.equals("bookshelf") || blockName.equals("crafting_table")
                || blockName.equals("chest") || blockName.equals("furnace") || blockName.equals("smoker")
                || blockName.equals("blast_furnace"))
            return "axe";
        if (blockName.equals("dirt") || blockName.equals("grass_block") || blockName.equals("sand")
                || blockName.equals("gravel") || blockName.equals("clay") || blockName.equals("soul_sand")
                || blockName.equals("soul_soil") || blockName.equals("snow_block") || blockName.equals("snow")
                || blockName.equals("coarse_dirt") || blockName.equals("podzol") || blockName.equals("mycelium")
                || blockName.equals("netherrack") || blockName.equals("magma_block") || blockName.equals("glowstone")
                || blockName.equals("ice") || blockName.equals("packed_ice") || blockName.equals("blue_ice")
                || blockName.equals("terracotta"))
            return "shovel";
        return "none";
    }

    public static boolean usesCorrectTool(String blockName, int heldItemId) {
        String req = getRequiredTool(blockName);
        if ("none".equals(req)) return true;
        return req.equals(getToolType(heldItemId));
    }

    private static void loadItems() {
        try {
            String path = Paths.get("block", "items.json").toString();
            JsonArray array = JsonParser.parseReader(new FileReader(path)).getAsJsonArray();
            for (JsonElement el : array) {
                JsonObject obj = el.getAsJsonObject();
                int    id   = obj.get("id").getAsInt();
                String name = obj.get("name").getAsString();
                itemIdToName.put(id, name);
            }
            System.out.println("[数据] 已加载 " + itemIdToName.size() + " 个物品定义");
        } catch (Exception e) {
            System.err.println("[警告] 无法加载 items.json: " + e.getMessage());
        }
        // 物品堆叠上限: 从 json/1.21.11/items.json 读 stackSize (block/items.json 无此字段)
        try {
            java.io.File f = new java.io.File("json/1.21.11/items.json");
            if (f.exists()) {
                JsonArray arr = JsonParser.parseReader(new java.io.FileReader(f)).getAsJsonArray();
                int n = 0;
                for (JsonElement el : arr) {
                    JsonObject obj = el.getAsJsonObject();
                    String name = obj.get("name").getAsString();
                    if (name.startsWith("minecraft:")) name = name.substring(10);
                    int stack = obj.has("stackSize") ? obj.get("stackSize").getAsInt() : 64;
                    ITEM_STACK.put(name, stack);
                    n++;
                }
                System.out.println("[数据] 物品堆叠上限已加载 " + n + " 项");
            }
        } catch (Exception e) {
            System.err.println("[警告] 无法加载物品堆叠上限: " + e.getMessage());
        }
    }

    /** 物品堆叠上限 (原版: 大多数 64, 末影珍珠/箭/蛋等 16, 桶/药水/末影之眼等 1)。 */
    public static int getStackSize(String itemName) {
        if (itemName == null) return 64;
        String n = itemName.startsWith("minecraft:") ? itemName.substring(10) : itemName;
        Integer v = ITEM_STACK.get(n);
        return v != null ? v : 64;
    }

    public static int getStackSize(int itemId) {
        String name = itemIdToName.get(itemId);
        return getStackSize(name);
    }

    public static int getDefaultState(int blockId) {
        return blockIdToState.getOrDefault(blockId, 1);
    }

    public static int getDefaultStateForItem(int itemId) {
        if (itemIdToName.isEmpty()) {
            return blockIdToState.getOrDefault(itemId, 0);
        }
        String name = itemIdToName.get(itemId);
        if (name == null) return 0;
        if (name.equals("redstone")) {
            name = "redstone_wire";
        } else if (name.equals("string")) {
            name = "tripwire";
        } else if (name.equals("wheat_seeds")) {
            name = "wheat";
        } else if (name.equals("melon_seeds")) {
            name = "melon_stem";
        }
        Integer state = nameToState.get(name);
        if (state == null) return 0;
        return state;
    }

    public static String itemIdToName(int itemId) {
        return itemIdToName.getOrDefault(itemId, "unknown");
    }

    public static int getItemIdByName(String name) {
        for (Map.Entry<Integer, String> e : itemIdToName.entrySet()) {
            if (e.getValue().equals(name)) return e.getKey();
        }
        return 0;
    }

    public static java.util.Set<String> getAllItemNames() {
        return new java.util.HashSet<>(itemIdToName.values());
    }
}
