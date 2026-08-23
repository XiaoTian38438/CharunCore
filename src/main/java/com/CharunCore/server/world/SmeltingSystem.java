package com.CharunCore.server.world;

import java.util.HashMap;
import java.util.Map;

public class SmeltingSystem {
    private static final Map<String, SmeltResult> smeltingRecipes = new HashMap<>();
    private static final Map<String, SmeltResult> blastingRecipes = new HashMap<>();
    private static final Map<String, SmeltResult> smokingRecipes = new HashMap<>();

    public static class SmeltResult {
        public final String resultItem;
        public final float xp;

        public SmeltResult(String resultItem, float xp) {
            this.resultItem = resultItem;
            this.xp = xp;
        }
    }

    static {
        smeltingRecipes.put("iron_ore", new SmeltResult("iron_ingot", 0.7f));
        smeltingRecipes.put("deepslate_iron_ore", new SmeltResult("iron_ingot", 0.7f));
        smeltingRecipes.put("raw_iron", new SmeltResult("iron_ingot", 0.7f));
        smeltingRecipes.put("raw_iron_block", new SmeltResult("iron_block", 6.3f));
        smeltingRecipes.put("gold_ore", new SmeltResult("gold_ingot", 1.0f));
        smeltingRecipes.put("deepslate_gold_ore", new SmeltResult("gold_ingot", 1.0f));
        smeltingRecipes.put("raw_gold", new SmeltResult("gold_ingot", 1.0f));
        smeltingRecipes.put("raw_gold_block", new SmeltResult("gold_block", 9.0f));
        smeltingRecipes.put("copper_ore", new SmeltResult("copper_ingot", 0.7f));
        smeltingRecipes.put("deepslate_copper_ore", new SmeltResult("copper_ingot", 0.7f));
        smeltingRecipes.put("raw_copper", new SmeltResult("copper_ingot", 0.7f));
        smeltingRecipes.put("raw_copper_block", new SmeltResult("copper_block", 6.3f));
        smeltingRecipes.put("sand", new SmeltResult("glass", 0.1f));
        smeltingRecipes.put("red_sand", new SmeltResult("glass", 0.1f));
        smeltingRecipes.put("cobblestone", new SmeltResult("stone", 0.1f));
        smeltingRecipes.put("stone", new SmeltResult("smooth_stone", 0.1f));
        smeltingRecipes.put("clay_ball", new SmeltResult("brick", 0.3f));
        smeltingRecipes.put("clay", new SmeltResult("terracotta", 0.35f));
        smeltingRecipes.put("netherrack", new SmeltResult("nether_brick", 0.1f));
        smeltingRecipes.put("ancient_debris", new SmeltResult("netherite_scrap", 2.0f));
        smeltingRecipes.put("cactus", new SmeltResult("green_dye", 1.0f));
        smeltingRecipes.put("sea_pickle", new SmeltResult("lime_dye", 0.1f));
        // 花朵熔炼 XP 原版为 0.1 (非 1.0)
        smeltingRecipes.put("poppy", new SmeltResult("red_dye", 0.1f));
        smeltingRecipes.put("dandelion", new SmeltResult("yellow_dye", 0.1f));
        smeltingRecipes.put("blue_orchid", new SmeltResult("light_blue_dye", 0.1f));
        smeltingRecipes.put("allium", new SmeltResult("magenta_dye", 0.1f));
        smeltingRecipes.put("azure_bluet", new SmeltResult("light_gray_dye", 0.1f));
        smeltingRecipes.put("cornflower", new SmeltResult("blue_dye", 0.1f));
        smeltingRecipes.put("lily_of_the_valley", new SmeltResult("white_dye", 0.1f));
        smeltingRecipes.put("oxeye_daisy", new SmeltResult("light_gray_dye", 0.1f));
        smeltingRecipes.put("kelp", new SmeltResult("dried_kelp", 0.1f));
        smeltingRecipes.put("wet_sponge", new SmeltResult("sponge", 0.15f));
        smeltingRecipes.put("beef", new SmeltResult("cooked_beef", 0.35f));
        smeltingRecipes.put("porkchop", new SmeltResult("cooked_porkchop", 0.35f));
        smeltingRecipes.put("chicken", new SmeltResult("cooked_chicken", 0.35f));
        smeltingRecipes.put("mutton", new SmeltResult("cooked_mutton", 0.35f));
        smeltingRecipes.put("rabbit", new SmeltResult("cooked_rabbit", 0.35f));
        smeltingRecipes.put("cod", new SmeltResult("cooked_cod", 0.35f));
        smeltingRecipes.put("salmon", new SmeltResult("cooked_salmon", 0.35f));
        smeltingRecipes.put("potato", new SmeltResult("baked_potato", 0.35f));
        smeltingRecipes.put("chorus_fruit", new SmeltResult("popped_chorus_fruit", 0.1f));

        for (Map.Entry<String, SmeltResult> entry : smeltingRecipes.entrySet()) {
            String input = entry.getKey();
            if (isOre(input) || isRawMetal(input) || input.equals("ancient_debris")) {
                blastingRecipes.put(input, entry.getValue());
            }
            if (isFood(input)) {
                smokingRecipes.put(input, entry.getValue());
            }
        }
    }

    private static boolean isOre(String name) {
        return name.endsWith("_ore") || name.equals("ancient_debris");
    }

    private static boolean isRawMetal(String name) {
        return name.startsWith("raw_");
    }

    public static boolean isFood(String name) {
        // 原版烟熏炉只熔炼食物: 不含 kelp (海带在原版普通熔炉熔炼)
        return switch (name) {
            case "beef", "porkchop", "chicken", "mutton", "rabbit",
                 "cod", "salmon", "potato" -> true;
            default -> false;
        };
    }

    public static SmeltResult getSmeltingResult(String inputItem) {
        return smeltingRecipes.get(inputItem);
    }

    public static SmeltResult getBlastingResult(String inputItem) {
        return blastingRecipes.get(inputItem);
    }

    public static SmeltResult getSmokingResult(String inputItem) {
        return smokingRecipes.get(inputItem);
    }

    public static SmeltResult getResult(String inputItem, String furnaceType) {
        return switch (furnaceType) {
            case "blast_furnace" -> getBlastingResult(inputItem);
            case "smoker" -> getSmokingResult(inputItem);
            default -> getSmeltingResult(inputItem);
        };
    }

    /** 全部熔炼配方条目 (input -> result+xp), 供配方书(recipe_book_add)与客户端左侧列表使用。 */
    public static java.util.List<Entry> allSmeltingEntries() {
        java.util.List<Entry> out = new java.util.ArrayList<>();
        for (Map.Entry<String, SmeltResult> e : smeltingRecipes.entrySet()) {
            out.add(new Entry(e.getKey(), e.getValue().resultItem, e.getValue().xp));
        }
        return out;
    }

    /** 全部高炉配方条目 (供高炉配方书左侧列表)。 */
    public static java.util.List<Entry> allBlastingEntries() {
        java.util.List<Entry> out = new java.util.ArrayList<>();
        for (Map.Entry<String, SmeltResult> e : blastingRecipes.entrySet()) {
            out.add(new Entry(e.getKey(), e.getValue().resultItem, e.getValue().xp));
        }
        return out;
    }

    /** 全部烟熏炉配方条目 (供烟熏炉配方书左侧列表)。 */
    public static java.util.List<Entry> allSmokingEntries() {
        java.util.List<Entry> out = new java.util.ArrayList<>();
        for (Map.Entry<String, SmeltResult> e : smokingRecipes.entrySet()) {
            out.add(new Entry(e.getKey(), e.getValue().resultItem, e.getValue().xp));
        }
        return out;
    }

    /** 配方书条目: 输入物品名 + 产物物品名 + 经验。 */
    public static class Entry {
        public final String input;
        public final String result;
        public final float xp;
        public Entry(String input, String result, float xp) {
            this.input = input; this.result = result; this.xp = xp;
        }
    }

    public static int getCookTime(String furnaceType) {
        return switch (furnaceType) {
            case "blast_furnace", "smoker" -> 100;
            default -> 200;
        };
    }

    public static boolean isFuel(String itemName) {
        return getFuelBurnTime(itemName) > 0;
    }

    public static int getFuelBurnTime(String itemName) {
        return switch (itemName) {
            case "coal" -> 1600;
            case "charcoal" -> 1600;
            case "coal_block" -> 16000;
            case "stick" -> 100;
            case "oak_planks", "spruce_planks", "birch_planks", "jungle_planks",
                 "acacia_planks", "dark_oak_planks", "mangrove_planks", "cherry_planks",
                 "pale_oak_planks", "bamboo_planks", "crimson_planks", "warped_planks" -> 300;
            case "oak_log", "spruce_log", "birch_log", "jungle_log",
                 "acacia_log", "dark_oak_log", "mangrove_log", "cherry_log", "pale_oak_log",
                 "oak_wood", "spruce_wood", "birch_wood", "jungle_wood",
                 "acacia_wood", "dark_oak_wood", "mangrove_wood", "cherry_wood", "pale_oak_wood",
                 "crimson_stem", "warped_stem", "crimson_hyphae", "warped_hyphae",
                 "stripped_oak_log", "stripped_spruce_log", "stripped_birch_log", "stripped_jungle_log",
                 "stripped_acacia_log", "stripped_dark_oak_log", "stripped_mangrove_log", "stripped_cherry_log",
                 "stripped_pale_oak_log", "bamboo_block" -> 300;
            case "oak_slab", "spruce_slab", "birch_slab", "jungle_slab",
                 "acacia_slab", "dark_oak_slab", "mangrove_slab", "cherry_slab", "pale_oak_slab" -> 150;
            case "oak_fence", "spruce_fence", "birch_fence", "jungle_fence",
                 "acacia_fence", "dark_oak_fence", "mangrove_fence", "cherry_fence" -> 300;
            case "oak_fence_gate", "spruce_fence_gate", "birch_fence_gate", "jungle_fence_gate",
                 "acacia_fence_gate", "dark_oak_fence_gate" -> 300;
            case "oak_stairs", "spruce_stairs", "birch_stairs", "jungle_stairs",
                 "acacia_stairs", "dark_oak_stairs" -> 300;
            case "oak_trapdoor", "spruce_trapdoor", "birch_trapdoor", "jungle_trapdoor",
                 "acacia_trapdoor", "dark_oak_trapdoor" -> 300;
            case "oak_door", "spruce_door", "birch_door", "jungle_door",
                 "acacia_door", "dark_oak_door" -> 200;
            case "oak_pressure_plate", "spruce_pressure_plate", "birch_pressure_plate",
                 "jungle_pressure_plate", "acacia_pressure_plate", "dark_oak_pressure_plate" -> 300;
            case "oak_button", "spruce_button", "birch_button", "jungle_button",
                 "acacia_button", "dark_oak_button" -> 100;
            case "crafting_table" -> 300;
            case "chest" -> 300;
            case "bookshelf" -> 300;
            case "ladder" -> 300;
            case "bamboo" -> 50;
            case "dead_bush" -> 100;
            case "grass", "short_grass", "fern" -> 50;
            case "carpet", "white_carpet", "red_carpet" -> 67;
            case "lava_bucket" -> 20000;
            case "blaze_rod" -> 2400;
            case "dried_kelp_block" -> 4000;
            case "banner" -> 300;
            case "bow" -> 300;
            case "crossbow" -> 300;
            case "fishing_rod" -> 300;
            case "wooden_sword", "wooden_pickaxe", "wooden_axe", "wooden_shovel", "wooden_hoe" -> 200;
            case "wooden_helmet", "wooden_chestplate", "wooden_leggings", "wooden_boots" -> 200;
            default -> 0;
        };
    }
}
