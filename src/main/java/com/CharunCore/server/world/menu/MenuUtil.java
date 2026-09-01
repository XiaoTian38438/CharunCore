package com.CharunCore.server.world.menu;

import com.CharunCore.server.utils.BlockManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 菜单逻辑共用的小工具。 */
public final class MenuUtil {

    private MenuUtil() {}

    /** 常见可损伤物品的耐久上限（无 registry 时的最小实现）。 */
    private static final Map<String, Integer> MAX_DMG = new HashMap<>();
    static {
        String[] tools = {"wooden","stone","iron","golden","diamond","netherite"};
        for (String t : tools) {
            MAX_DMG.put(t + "_sword", 1); // 仅占位；真实上限见下方特例
        }
        MAX_DMG.put("wooden_sword", 59); MAX_DMG.put("stone_sword", 131);
        MAX_DMG.put("iron_sword", 250); MAX_DMG.put("golden_sword", 32);
        MAX_DMG.put("diamond_sword", 1561); MAX_DMG.put("netherite_sword", 2031);
        String[] t2 = {"pickaxe","axe","shovel","hoe"};
        int[] wp = {59,131,250,32,1561,2031}, wpp = {32,40,48,59,78,200}; // 工具/锄
        for (int i = 0; i < tools.length; i++) {
            MAX_DMG.put(tools[i] + "_pickaxe", wp[i]);
            MAX_DMG.put(tools[i] + "_axe", wp[i] + 1);
            MAX_DMG.put(tools[i] + "_shovel", wp[i] - 27);
            MAX_DMG.put(tools[i] + "_hoe", wpp[i]);
        }
        String[] armor = {"helmet","chestplate","leggings","boots"};
        int[][] ap = {{55,165,77,33,407,550},{80,240,112,48,529,721},{75,225,105,45,495,675},{65,195,91,39,429,585}};
        for (int a = 0; a < armor.length; a++)
            for (int i = 0; i < tools.length; i++)
                MAX_DMG.put(tools[i] + "_" + armor[a], ap[a][i]);
        MAX_DMG.put("shield", 336); MAX_DMG.put("bow", 384);
        MAX_DMG.put("crossbow", 465); MAX_DMG.put("trident", 250);
        MAX_DMG.put("fishing_rod", 64); MAX_DMG.put("flint_and_steel", 64);
        MAX_DMG.put("carrot_on_a_stick", 25); MAX_DMG.put("warped_fungus_on_a_stick", 100);
        MAX_DMG.put("shears", 238); MAX_DMG.put("elytra", 432); MAX_DMG.put("mace", 250);
    }

    public static int getMaxDurability(String itemName) {
        if (itemName == null) return 0;
        return MAX_DMG.getOrDefault(itemName, 0);
    }

    /** 切石机：返回该输入的所有可行产物 id（group 列表）。 */
    public static int[] stonecutterResults(String in) {
        if (in == null) return new int[0];
        String[] cands;
        switch (in) {
            case "stone": cands = new String[]{"stone_slab","stone_stairs","stone_bricks","stone_brick_slab","stone_brick_stairs","stone_brick_wall","chiseled_stone_bricks"}; break;
            case "granite": cands = new String[]{"granite_slab","granite_stairs","granite_wall","polished_granite","polished_granite_slab","polished_granite_stairs","polished_granite_wall"}; break;
            case "diorite": cands = new String[]{"diorite_slab","diorite_stairs","diorite_wall","polished_diorite","polished_diorite_slab","polished_diorite_stairs","polished_diorite_wall"}; break;
            case "andesite": cands = new String[]{"andesite_slab","andesite_stairs","andesite_wall","polished_andesite","polished_andesite_slab","polished_andesite_stairs","polished_andesite_wall"}; break;
            case "sandstone": cands = new String[]{"sandstone_slab","sandstone_stairs","sandstone_wall","chiseled_sandstone","cut_sandstone","cut_sandstone_slab"}; break;
            case "red_sandstone": cands = new String[]{"red_sandstone_slab","red_sandstone_stairs","red_sandstone_wall","chiseled_red_sandstone","cut_red_sandstone","cut_red_sandstone_slab"}; break;
            case "quartz_block": cands = new String[]{"quartz_slab","quartz_stairs","quartz_pillar","chiseled_quartz_block","smooth_quartz"}; break;
            case "cobblestone": cands = new String[]{"cobblestone_slab","cobblestone_stairs","cobblestone_wall"}; break;
            case "stone_bricks": cands = new String[]{"stone_brick_slab","stone_brick_stairs","stone_brick_wall","chiseled_stone_bricks"}; break;
            case "bricks": cands = new String[]{"brick_slab","brick_stairs","brick_wall"}; break;
            case "nether_bricks": cands = new String[]{"nether_brick_slab","nether_brick_stairs","nether_brick_wall","chiseled_nether_bricks"}; break;
            case "red_nether_bricks": cands = new String[]{"red_nether_brick_slab","red_nether_brick_stairs","red_nether_brick_wall"}; break;
            case "blackstone": cands = new String[]{"blackstone_slab","blackstone_stairs","blackstone_wall","polished_blackstone","polished_blackstone_slab","polished_blackstone_stairs","polished_blackstone_wall","chiseled_polished_blackstone"}; break;
            case "polished_blackstone": cands = new String[]{"polished_blackstone_slab","polished_blackstone_stairs","polished_blackstone_wall"}; break;
            case "polished_blackstone_bricks": cands = new String[]{"polished_blackstone_brick_slab","polished_blackstone_brick_stairs","polished_blackstone_brick_wall","chiseled_polished_blackstone"}; break;
            case "prismarine": cands = new String[]{"prismarine_slab","prismarine_stairs","prismarine_bricks","prismarine_brick_slab","prismarine_brick_stairs","prismarine_wall"}; break;
            case "purpur_block": cands = new String[]{"purpur_slab","purpur_stairs","purpur_pillar"}; break;
            case "end_stone": cands = new String[]{"end_stone_bricks"}; break;
            case "deepslate": cands = new String[]{"deepslate_slab","deepslate_stairs","deepslate_wall","deepslate_bricks","deepslate_brick_slab","deepslate_brick_stairs","deepslate_brick_wall","chiseled_deepslate","cracked_deepslate_bricks","cracked_deepslate_tiles"}; break;
            case "cobbled_deepslate": cands = new String[]{"cobbled_deepslate_slab","cobbled_deepslate_stairs","cobbled_deepslate_wall"}; break;
            case "basalt": cands = new String[]{"polished_basalt"}; break;
            case "copper_block": cands = new String[]{"cut_copper_slab","cut_copper_stairs","cut_copper","exposed_cut_copper","weathered_cut_copper","oxidized_cut_copper"}; break;
            case "mossy_stone_bricks": cands = new String[]{"mossy_stone_brick_slab","mossy_stone_brick_stairs","mossy_stone_brick_wall"}; break;
            case "mossy_cobblestone": cands = new String[]{"mossy_cobblestone_slab","mossy_cobblestone_stairs","mossy_cobblestone_wall"}; break;
            case "tuff": cands = new String[]{"tuff_slab","tuff_stairs","tuff_bricks","tuff_brick_slab","tuff_brick_stairs","tuff_brick_wall","chiseled_tuff"}; break;
            case "calcite": cands = new String[]{"calcite_slab","calcite_stairs"}; break;
            default: cands = new String[]{in + "_slab", in + "_stairs", in + "_bricks", in + "_wall", in + "_pillar"};
        }
        java.util.ArrayList<Integer> out = new java.util.ArrayList<>();
        for (String c : cands) {
            int id = BlockManager.getItemIdByName(c);
            if (id > 0) out.add(id);
        }
        int[] r = new int[out.size()];
        for (int i = 0; i < r.length; i++) r[i] = out.get(i);
        return r;
    }

    /** 切石机全部配方条目(input -> 每个可行产物), 供配方书(recipe_book_add type=3)推送左侧样式列表。 */
    public static java.util.List<StonecutterEntry> stonecutterEntries() {
        java.util.List<StonecutterEntry> out = new java.util.ArrayList<>();
        String[] inputs = {
            "stone","granite","diorite","andesite","sandstone","red_sandstone","quartz_block",
            "cobblestone","stone_bricks","bricks","nether_bricks","red_nether_bricks","blackstone",
            "polished_blackstone","polished_blackstone_bricks","prismarine","purpur_block","end_stone",
            "deepslate","cobbled_deepslate","basalt","copper_block","mossy_stone_bricks",
            "mossy_cobblestone","tuff","calcite"
        };
        for (String in : inputs) {
            int inId = BlockManager.getItemIdByName(in);
            if (inId <= 0) continue;
            for (int rid : stonecutterResults(in)) {
                out.add(new StonecutterEntry(in, BlockManager.itemIdToName(rid)));
            }
        }
        return out;
    }

    /** 切石机配方书条目: 输入物品名 + 产物物品名。 */
    public static class StonecutterEntry {
        public final String input;
        public final String result;
        public StonecutterEntry(String input, String result) {
            this.input = input; this.result = result;
        }
    }

    /** 信标可选主/副效果协议 id（对齐 MobEffect 协议 id）。 */
    public static int[] beaconEffects() {
        return new int[]{0,2,4,7,9,10,11,12,15}; // speed,haste,strength,jump_boost,regeneration,resistance,fire_resistance,water_breathing,night_vision (0 基注册表 id)
    }

    /** 锻造台升级模板物品名。 */
    public static boolean isSmithingTemplate(String name) {
        return name != null && (name.equals("netherite_upgrade") || name.endsWith("upgrade_smithing_template"));
    }
}
