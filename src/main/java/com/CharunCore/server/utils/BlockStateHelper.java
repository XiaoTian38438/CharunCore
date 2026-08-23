package com.CharunCore.server.utils;

import com.google.gson.*;
import java.io.FileReader;
import java.util.*;

/**
 * 从 blocks.json 动态加载方块属性，提供 stateId ↔ 属性 互转。
 *
 * bool 顺序：PrismarineJS 1.21.11 中 bool = ["true","false"]（true=index 0）
 * 验证：grass_block snowy=false → defaultState=9 = minStateId(8)+1 ✓
 */
public class BlockStateHelper {

    public static class PropDef {
        public final String name;
        public final List<String> values;
        PropDef(String n, List<String> v) { name = n; values = v; }
    }

    public static class BlockData {
        public final String name;
        public final int minStateId, maxStateId, defaultStateId;
        public final int blockId;
        public final List<PropDef> props;
        final int[] strides;

        BlockData(String n, int min, int max, int def, int bid, List<PropDef> p) {
            name = n; minStateId = min; maxStateId = max; defaultStateId = def; blockId = bid; props = p;
            strides = new int[p.size()];
            int s = 1;
            for (int i = p.size() - 1; i >= 0; i--) { strides[i] = s; s *= p.get(i).values.size(); }
        }

        public int computeState(Map<String, String> pm) {
            int off = 0;
            for (int i = 0; i < props.size(); i++) {
                PropDef p = props.get(i);
                String v = pm.getOrDefault(p.name, p.values.get(p.values.size() - 1));
                int idx = p.values.indexOf(v); if (idx < 0) idx = 0;
                off += strides[i] * idx;
            }
            return minStateId + off;
        }

        public Map<String, String> parseState(int stateId) {
            int off = stateId - minStateId;
            Map<String, String> m = new LinkedHashMap<>();
            for (int i = 0; i < props.size(); i++) {
                PropDef p = props.get(i);
                int idx = strides[i] == 0 ? 0 : (off / strides[i]) % p.values.size();
                m.put(p.name, p.values.get(idx));
            }
            return m;
        }
    }

    private static final Map<String, BlockData> byName = new HashMap<>();
    private static final TreeMap<Integer, BlockData> byMin = new TreeMap<>();

    public static void init() {
        try {
            JsonArray arr = JsonParser.parseReader(new FileReader("block/blocks.json")).getAsJsonArray();
            for (JsonElement el : arr) {
                JsonObject o = el.getAsJsonObject();
                String name = o.get("name").getAsString();
                int min = o.get("minStateId").getAsInt(), max = o.get("maxStateId").getAsInt();
                int def = o.get("defaultState").getAsInt();
                List<PropDef> props = new ArrayList<>();
                if (o.has("states")) {
                    for (JsonElement se : o.getAsJsonArray("states")) {
                        JsonObject sp = se.getAsJsonObject();
                        String pn = sp.get("name").getAsString(), pt = sp.get("type").getAsString();
                        List<String> vals;
                        if ("bool".equals(pt)) {
                            vals = List.of("true", "false"); // true=0, false=1 (verified from data)
                        } else if (sp.has("values")) {
                            vals = new ArrayList<>();
                            for (JsonElement v : sp.getAsJsonArray("values")) vals.add(v.getAsString());
                        } else {
                            int n = sp.get("num_values").getAsInt(); vals = new ArrayList<>();
                            for (int i = 0; i < n; i++) vals.add(String.valueOf(i));
                        }
                        props.add(new PropDef(pn, vals));
                    }
                }
                int bid = o.has("id") ? o.get("id").getAsInt() : 0;
                BlockData bd = new BlockData(name, min, max, def, bid, props);
                byName.put(name, bd); byMin.put(min, bd);
            }
            System.out.println("[数据] BlockStateHelper 已加载 " + byName.size() + " 个方块");
        } catch (Exception e) { System.err.println("[错误] BlockStateHelper: " + e.getMessage()); }
    }

    public static BlockData getByState(int stateId) {
        Map.Entry<Integer, BlockData> e = byMin.floorEntry(stateId);
        if (e == null) return null;
        BlockData bd = e.getValue();
        return stateId <= bd.maxStateId ? bd : null;
    }

    public static int withProp(int stateId, String prop, String value) {
        BlockData bd = getByState(stateId); if (bd == null) return stateId;
        Map<String, String> m = bd.parseState(stateId); m.put(prop, value);
        return bd.computeState(m);
    }

    public static int toggleBool(int stateId, String prop) {
        String cur = getProp(stateId, prop);
        return withProp(stateId, prop, "true".equals(cur) ? "false" : "true");
    }

    public static String getProp(int stateId, String prop) {
        BlockData bd = getByState(stateId); if (bd == null) return null;
        return bd.parseState(stateId).get(prop);
    }

    public static String getName(int stateId) {
        BlockData bd = getByState(stateId); return bd != null ? bd.name : "air";
    }

    /** 方块注册 id（用于 block_event 包第 4 字段 ByteBufCodecs.registry(Registries.BLOCK)）。
     *  与服务端下发注册表编号一致（block/blocks.json 的 id 字段）。 */
    public static int getBlockNumericId(int stateId) {
        BlockData bd = getByState(stateId);
        return bd != null ? bd.blockId : 0;
    }

    /** 近似实心判定（用于窒息/防卡墙/寻路）：仅完整方块视为实心。
     *  空气/液体/非完整方块(台阶/楼梯/活板门/栅栏/玻璃/树叶/地毯等)视为可穿过。 */
    public static boolean isSolidOpaque(int stateId) {
        if (stateId == 0) return false;
        String n = getName(stateId);
        if (n == null) return false;
        if (n.startsWith("minecraft:")) n = n.substring(10);
        if (n.contains("water") || n.contains("lava") || n.contains("air")) return false;
        if (n.contains("grass") && !n.contains("block")) return false;
        if (n.contains("flower") || n.contains("sapling") || n.contains("torch")
                || n.contains("sign") || n.contains("banner") || n.contains("rail")
                || n.contains("carpet") || n.contains("snow") || n.contains("vine")
                || n.contains("fern") || n.contains("mushroom") || n.contains("button")
                || n.contains("pressure_plate") || n.contains("_door") || n.contains("ladder")) return false;
        // 非完整方块: 这些不会让玩家窒息(原版行为), 也都可穿过/可放置。
        if (n.contains("slab") || n.contains("stairs") || n.contains("trapdoor")
                || n.contains("fence") || n.contains("wall") && n.contains("wall_")
                || n.contains("pane") || n.contains("glass") || n.contains("leaves")
                || n.contains("hedge") || n.contains("iron_bars") || n.contains("chain")
                || n.contains("coral") || n.contains("kelp") || n.contains("bamboo")
                || n.contains("cake") || n.contains("lantern") || n.contains("end_rod")
                || n.contains("conduit") || n.contains("scaffolding") || n.contains("hopper")
                || n.contains("grindstone") || n.contains("bell") || n.contains("amethyst")
                || n.contains("soul") && n.contains("torch") || n.contains("light")
                || n.endsWith("_carpet") || n.contains("azalea") || n.contains("moss")
                || n.contains("composter") || n.contains("sweet_berry") || n.contains("nether_sprout")) return false;
        return true;
    }

    public static int getDefault(String name) {
        BlockData bd = byName.get(name); return bd != null ? bd.defaultStateId : 0;
    }

    /** 原版 Material.isReplaceable: 草/花/雪层/蘑菇/蕨/苔藓/根土等可被方块替换。
     *  火/火把/红石粉/压力板等非完整方块不可替换(原版行为), 防止"火焰堆叠/隔空放"。 */
    public static boolean isReplaceable(String n) {
        if (n == null) return false;
        if (n.startsWith("minecraft:")) n = n.substring(10);
        if (n.endsWith("_grass") && !n.equals("grass_block")) return true; // 草丛
        if (n.contains("tall_grass") || n.contains("fern") || n.contains("bush")) return true;
        if (n.endsWith("flower") || n.contains("_tulip") || n.contains("dandelion")
            || n.contains("poppy") || n.contains("blue_orchid") || n.contains("allium")
            || n.contains("azure_bluet") || n.contains("oxeye_daisy") || n.contains("cornflower")
            || n.contains("lily_of_the_valley") || n.contains("wither_rose")
            || n.contains("suspicious") ) return true;
        if (n.equals("snow") || n.contains("_sapling") || n.equals("vine")
            || n.equals("moss_carpet") || n.equals("nether_sprouts")
            || n.equals("weeping_vines") || n.equals("twisting_vines")
            || n.equals("dead_bush") || n.equals("mushroom") ) return true;
        return false;
    }

    public static int getState(String name, Map<String, String> props) {
        BlockData bd = byName.get(name); if (bd == null) return 0;
        return bd.computeState(props);
    }

    /**
     * 玩家 yaw → 水平朝向字符串
     * Minecraft yaw: 0=south, 90=west, 180=north, 270=east
     */
    public static String horizontalFacing(float yaw) {
        yaw = ((yaw % 360) + 360) % 360;
        if (yaw >= 315 || yaw < 45) return "south";
        if (yaw < 135) return "west";
        if (yaw < 225) return "north";
        return "east";
    }

    /** 朝向 → 偏移量 [dx, dz] */
    public static int[] facingOffset(String facing) {
        return switch (facing) {
            case "north" -> new int[]{0, -1};
            case "south" -> new int[]{0, +1};
            case "east"  -> new int[]{+1, 0};
            case "west"  -> new int[]{-1, 0};
            default      -> new int[]{0, 0};
        };
    }

    /** 判断方块是否可交互（门/拉杆/按钮/床/活板门） */
    public static boolean isInteractable(int stateId) {
        String n = getName(stateId);
        return n.endsWith("_door") || n.endsWith("_trapdoor") || n.equals("lever")
            || n.endsWith("_button") || n.endsWith("_bed")
            || n.equals("bell") || n.equals("crafting_table")
            || n.equals("chest") || n.endsWith("_chest") || n.equals("ender_chest") || n.endsWith("_gate")
            || n.equals("furnace") || n.equals("blast_furnace") || n.equals("smoker")
            || n.equals("brewing_stand") || n.equals("anvil")
            || n.equals("enchanting_table") || n.equals("grindstone")
            || n.equals("stonecutter") || n.equals("loom")
            || n.equals("cartography_table") || n.equals("smithing_table")
            || n.equals("hopper") || n.equals("dispenser") || n.equals("dropper")
            || n.equals("shulker_box") || n.endsWith("_shulker_box")
            || n.equals("barrel") || n.equals("chiseled_bookshelf")
            || n.equals("lectern") || n.equals("beacon")
            // 右键调音/调档/切模式(原版可交互):
            || n.equals("note_block") || n.equals("repeater") || n.equals("comparator")
            || n.equals("daylight_detector") || n.equals("jukebox")
            // #20 营火: 右键放食物烤/取出熟食(原版可交互, 曾缺失导致右键走放置逻辑无反应)。
            || n.equals("campfire") || n.equals("soul_campfire")
            || n.endsWith("_sign") || n.endsWith("_wall_sign")
            || n.equals("sign") || n.equals("wall_sign")
            // #29 命令方块: 右键打开编辑器 UI(仅 OP/创造可编辑, 原版 CommandBlockEditMenu)。
            || n.equals("command_block") || n.equals("chain_command_block") || n.equals("repeating_command_block");
    }
}
