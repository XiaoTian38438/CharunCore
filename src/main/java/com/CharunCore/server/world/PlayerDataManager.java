package com.CharunCore.server.world;

import com.CharunCore.server.utils.BlockManager;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class PlayerDataManager {

    private static final int DATA_VERSION = 4671;
    private static final Map<Integer, String> ENCHANT_ID_TO_NAME = new HashMap<>();
    private static final Map<String, Integer> ITEM_NAME_TO_ID = new HashMap<>();

    static {
        try (java.io.Reader r = new java.io.InputStreamReader(
                new java.io.FileInputStream("data/enchantment.json"),
                java.nio.charset.StandardCharsets.UTF_8)) {
            com.google.gson.JsonObject root = com.google.gson.JsonParser.parseReader(r).getAsJsonObject();
            for (com.google.gson.JsonElement e : root.getAsJsonObject("minecraft:enchantment").getAsJsonArray("value")) {
                com.google.gson.JsonObject o = e.getAsJsonObject();
                ENCHANT_ID_TO_NAME.put(o.get("id").getAsInt(), o.get("name").getAsString());
            }
        } catch (Exception ignored) {}
    }

    private PlayerDataManager() {}

    /** 单物品解析出的组件（与设计/读取共用）。lore 多行以 \n 分隔; glint: -1 无/0 强制关/1 强制开。 */
    public record ItemComps(int damage, java.util.Map<Integer, Integer> enchants, String potion, String customName,
                            int trimMaterial, int trimPattern,
                            String lore, boolean unbreakable, int glint) {
        public ItemComps(int damage, java.util.Map<Integer, Integer> enchants, String potion, String customName) {
            this(damage, enchants, potion, customName, -1, -1, null, false, 0);
        }

        public ItemComps(int damage, java.util.Map<Integer, Integer> enchants, String potion, String customName,
                         int trimMaterial, int trimPattern) {
            this(damage, enchants, potion, customName, trimMaterial, trimPattern, null, false, 0);
        }
    }

    // Bug44: trim 注册表 id ↔ 原版资源路径 (顺序 = dumped_registries reg_3/reg_4)
    private static final String[] TRIM_MATERIAL_NAMES = {
        "amethyst", "copper", "diamond", "emerald", "gold", "iron", "lapis",
        "netherite", "quartz", "redstone", "resin"
    };
    private static final String[] TRIM_PATTERN_NAMES = {
        "bolt", "coast", "dune", "eye", "flow", "host", "raiser", "rib",
        "sentry", "shaper", "silence", "snout", "spire", "tide", "vex",
        "ward", "wayfinder", "wild"
    };

    /** 构建某物品的 components NBT（附魔/药水/自定义名/耐久/纹饰）。 */
    public static NbtMap buildItemComponents(String itemName, int damage,
            java.util.Map<Integer, Integer> enchants, String potion, String customName) {
        return buildItemComponents(itemName, damage, enchants, potion, customName, -1, -1);
    }

    public static NbtMap buildItemComponents(String itemName, int damage,
            java.util.Map<Integer, Integer> enchants, String potion, String customName,
            int trimMaterial, int trimPattern) {
        return buildItemComponents(itemName, damage, enchants, potion, customName,
                trimMaterial, trimPattern, null, false, 0);
    }

    /** B1: 完整组件构建(含插件扩展 lore/unbreakable/glint)。 */
    public static NbtMap buildItemComponents(String itemName, int damage,
            java.util.Map<Integer, Integer> enchants, String potion, String customName,
            int trimMaterial, int trimPattern,
            String lore, boolean unbreakable, int glint) {
        NbtMapBuilder components = NbtMap.builder();
        if (damage > 0) components.putInt("minecraft:damage", damage);
        if (enchants != null && !enchants.isEmpty()) {
            NbtMapBuilder levels = NbtMap.builder();
            for (java.util.Map.Entry<Integer, Integer> e : enchants.entrySet()) {
                String en = ENCHANT_ID_TO_NAME.get(e.getKey());
                if (en != null && e.getValue() > 0) levels.putInt(en, e.getValue());
            }
            NbtMap enchantments = NbtMap.builder()
                    .putCompound("levels", levels.build())
                    .putBoolean("show_in_tooltip", true)
                    .build();
            boolean book = "enchanted_book".equals(itemName);
            components.putCompound(book ? "minecraft:stored_enchantments" : "minecraft:enchantments", enchantments);
        }
        if (potion != null && !potion.isEmpty()) {
            String[] parts = potion.split("\\|");
            if (parts.length >= 3) {
                NbtMapBuilder effect = NbtMap.builder()
                        .putString("id", "minecraft:" + parts[0])
                        .putInt("amplifier", Math.max(0, Integer.parseInt(parts[1]) - 1))
                        .putInt("duration", Integer.parseInt(parts[2]))
                        .putBoolean("ambient", false)
                        .putBoolean("show_particles", true);
                NbtMap contents = NbtMap.builder()
                        .putList("custom_effects", NbtType.COMPOUND, List.of(effect.build()))
                        .build();
                components.putCompound("minecraft:potion_contents", contents);
            } else {
                // 原版 PotionContents NBT 形式: {potion:"minecraft:x"}, 仅有类型无自定义效果
                String p = parts[0].startsWith("minecraft:") ? parts[0] : "minecraft:" + parts[0];
                components.putCompound("minecraft:potion_contents", NbtMap.builder()
                        .putString("potion", p).build());
            }
        }
        if (customName != null && !customName.isEmpty()) {
            components.putString("minecraft:custom_name",
                    "{\"text\":" + new com.google.gson.Gson().toJson(customName) + "}");
        }
        if (trimMaterial >= 0 && trimPattern >= 0
                && trimMaterial < TRIM_MATERIAL_NAMES.length
                && trimPattern < TRIM_MATERIAL_NAMES.length) {
            // Bug44: 原版 trim 组件 NBT = {material:"minecraft:x", pattern:"minecraft:y"}
            components.putCompound("minecraft:trim", NbtMap.builder()
                    .putString("material", "minecraft:" + TRIM_MATERIAL_NAMES[trimMaterial])
                    .putString("pattern", "minecraft:" + TRIM_PATTERN_NAMES[trimPattern])
                    .build());
        }
        if (lore != null && !lore.isEmpty()) {
            // 原版 minecraft:lore 组件: List<Text> 每行一条
            // 注: BE 存储格式为内部归一化(客户端不经此路径, 显示走 774 wire lore=11)
            List<NbtMap> lines = new java.util.ArrayList<>();
            for (String line : lore.split("\n", -1)) {
                lines.add(NbtMap.builder().putString("text", line).build());
            }
            components.putList("minecraft:lore", NbtType.COMPOUND, lines);
        }
        if (unbreakable) {
            components.putCompound("minecraft:unbreakable", NbtMap.builder().build());
        }
        if (glint != 0) {
            components.putBoolean("minecraft:enchantment_glint_override", glint > 0);
        }
        return components.build();
    }

    /** 从物品 NBT（含 components 复合标签）解析出组件。 */
    public static ItemComps parseItemComponents(NbtMap itemNbt) {
        int damage = 0;
        java.util.Map<Integer, Integer> enchants = new java.util.HashMap<>();
        String potion = null;
        String customName = null;
        int trimMaterial = -1, trimPattern = -1;
        String lore = null;
        boolean unbreakable = false;
        int glint = 0;
        NbtMap components = itemNbt.getCompound("components");
        if (components != null) {
            if (components.containsKey("minecraft:damage")) damage = components.getInt("minecraft:damage", 0);
            NbtMap ench = null;
            if (components.containsKey("minecraft:enchantments")) {
                ench = components.getCompound("minecraft:enchantments");
            } else if (components.containsKey("minecraft:stored_enchantments")) {
                ench = components.getCompound("minecraft:stored_enchantments");
            }
            if (ench != null) {
                NbtMap levels = ench.getCompound("levels");
                if (levels != null) for (String key : levels.keySet()) {
                    Integer eid = enchantIdByName(key);
                    if (eid != null) enchants.put(eid, levels.getInt(key, 0));
                }
            }
            Object potionV = components.get("minecraft:potion_contents");
            if (potionV instanceof String ps) {
                // 原版裸字符串形式: 仅有药水类型无自定义效果
                potion = ps.startsWith("minecraft:") ? ps.substring(10) : ps;
            } else if (potionV instanceof NbtMap potionC) {
                String typeOnly = potionC.getString("potion", null);
                List<NbtMap> effects = potionC.getList("custom_effects", NbtType.COMPOUND);
                if (effects != null && !effects.isEmpty()) {
                    NbtMap eff = effects.get(0);
                    String eidName = eff.getString("id", "speed");
                    eidName = eidName.startsWith("minecraft:") ? eidName.substring(10) : eidName;
                    potion = eidName + "|" + (eff.getInt("amplifier", 0) + 1) + "|" + eff.getInt("duration", 3600);
                } else if (typeOnly != null && !typeOnly.isEmpty()) {
                    potion = typeOnly.startsWith("minecraft:") ? typeOnly.substring(10) : typeOnly;
                }
            }
            String cn = components.getString("minecraft:custom_name", null);
            if (cn != null && !cn.isEmpty()) {
                try {
                    com.google.gson.JsonObject o = com.google.gson.JsonParser.parseString(cn).getAsJsonObject();
                    if (o.has("text")) customName = o.get("text").getAsString();
                } catch (Exception ignored) {}
            }
            NbtMap trim = components.getCompound("minecraft:trim");
            if (trim != null && trim.containsKey("material")) {
                String m = trim.getString("material", "");
                String pt = trim.getString("pattern", "");
                for (int i = 0; i < TRIM_MATERIAL_NAMES.length; i++) {
                    if (("minecraft:" + TRIM_MATERIAL_NAMES[i]).equals(m)) { trimMaterial = i; break; }
                }
                for (int i = 0; i < TRIM_PATTERN_NAMES.length; i++) {
                    if (("minecraft:" + TRIM_PATTERN_NAMES[i]).equals(pt)) { trimPattern = i; break; }
                }
            }
            List<NbtMap> loreLines = components.getList("minecraft:lore", NbtType.COMPOUND);
            if (loreLines != null && !loreLines.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < loreLines.size(); i++) {
                    if (i > 0) sb.append('\n');
                    NbtMap ln = loreLines.get(i);
                    String txt = ln != null ? ln.getString("text", "") : "";
                    if (txt.startsWith("{")) {
                        try {
                            com.google.gson.JsonObject o = com.google.gson.JsonParser.parseString(txt).getAsJsonObject();
                            if (o.has("text")) txt = o.get("text").getAsString();
                        } catch (Exception ignored) {}
                    }
                    sb.append(txt);
                }
                lore = sb.toString();
            }
            unbreakable = components.containsKey("minecraft:unbreakable");
            if (components.containsKey("minecraft:enchantment_glint_override")) {
                Boolean g = components.getBoolean("minecraft:enchantment_glint_override", false);
                glint = g ? 1 : -1;
            }
        }
        return new ItemComps(damage, enchants, potion, customName, trimMaterial, trimPattern,
                lore, unbreakable, glint);
    }

    public static void init() {
        new File("world/playerdata").mkdirs();
        new File("world/advancements").mkdirs();
        new File("world/stats").mkdirs();
        ITEM_NAME_TO_ID.clear();
        try (java.io.Reader r = new java.io.InputStreamReader(
                new java.io.FileInputStream("json/1.21.11/items.json"),
                java.nio.charset.StandardCharsets.UTF_8)) {
            com.google.gson.JsonArray arr = com.google.gson.JsonParser.parseReader(r).getAsJsonArray();
            for (com.google.gson.JsonElement e : arr) {
                com.google.gson.JsonObject o = e.getAsJsonObject();
                ITEM_NAME_TO_ID.put(o.get("name").getAsString(), o.get("id").getAsInt());
            }
        } catch (Exception ignored) {}
    }

    public static PlayerData load(UUID uuid) {
        PlayerData result = null;
        File dat = new File("world/playerdata", uuid + ".dat");
        if (dat.exists()) {
            try (NBTInputStream in = new NBTInputStream(
                    new DataInputStream(new GZIPInputStream(new FileInputStream(dat))))) {
                Object tag = in.readTag();
                if (tag instanceof NbtMap nbt) {
                    result = fromNbt(nbt, uuid);
                }
            } catch (Exception e) {
                System.err.println("[玩家数据] 读取 " + uuid + ".dat 失败: " + e.getMessage());
            }
        }
        if (result == null) {
            File legacy = new File("players", uuid + ".json");
            if (legacy.exists()) {
                try (java.io.Reader r = new java.io.FileReader(legacy)) {
                    PlayerData d = new com.google.gson.Gson().fromJson(r, PlayerData.class);
                    if (d != null) {
                        save(uuid, d);
                        System.out.println("[玩家数据] 已迁移旧版 JSON → world/playerdata/" + uuid + ".dat");
                        result = d;
                    }
                } catch (Exception e) {
                    System.err.println("[玩家数据] 读取旧版 JSON 失败: " + e.getMessage());
                }
            }
        }
        if (result != null) loadAdvancements(uuid, result);
        return result;
    }

    private static void loadAdvancements(UUID uuid, PlayerData d) {
        File f = new File("world/advancements", uuid + ".json");
        if (!f.exists()) return;
        try (java.io.Reader r = new java.io.InputStreamReader(new java.io.FileInputStream(f),
                java.nio.charset.StandardCharsets.UTF_8)) {
            com.google.gson.JsonObject root = com.google.gson.JsonParser.parseReader(r).getAsJsonObject();
            for (String key : root.keySet()) d.unlockedAdvancements.add(key);
        } catch (Exception ignored) {}
    }

    public static void save(UUID uuid, PlayerData d) {
        if (d == null || uuid == null) return;
        File dir = new File("world/playerdata");
        dir.mkdirs();
        File dat = new File(dir, uuid + ".dat");
        File tmp = new File(dir, uuid + ".dat.tmp");
        try (NBTOutputStream out = new NBTOutputStream(
                new DataOutputStream(new GZIPOutputStream(new FileOutputStream(tmp))))) {
            out.writeTag(toNbt(uuid, d));
        } catch (Exception e) {
            System.err.println("[玩家数据] 写入 " + uuid + ".dat 失败: " + e.getMessage());
            tmp.delete();
            return;
        }
        if (dat.exists()) dat.delete();
        if (!tmp.renameTo(dat)) {
            System.err.println("[玩家数据] 重命名 " + uuid + ".dat 失败");
        }
        saveAdvancements(uuid, d);
    }

    private static NbtMap toNbt(UUID uuid, PlayerData d) {
        NbtMapBuilder b = NbtMap.builder();
        b.putList("Pos", NbtType.DOUBLE, List.of(d.x, d.y, d.z));
        b.putList("Motion", NbtType.DOUBLE, List.of(0.0, -0.0784, 0.0));
        b.putList("Rotation", NbtType.FLOAT, List.of(d.yaw, d.pitch));
        b.putFloat("Health", d.health);
        b.putShort("Air", (short) d.airTicks);
        b.putShort("Fire", (short) -1);
        b.putBoolean("OnGround", true);
        b.putBoolean("Invulnerable", false);
        b.putInt("playerGameType", d.gameMode);
        b.putInt("foodLevel", d.food);
        b.putFloat("foodSaturationLevel", d.saturation);
        b.putFloat("foodExhaustionLevel", d.exhaustion);
        b.putInt("XpLevel", d.xpLevel);
        b.putInt("XpTotal", d.xpTotal);
        b.putFloat("XpP", d.xpProgress);
        b.putLong("XpSeed", new java.util.Random().nextLong());
        b.putInt("SelectedItemSlot", d.heldSlot);
        b.putString("Dimension", d.dimension == null ? "minecraft:overworld" : d.dimension);
        b.putInt("score", 0);
        b.putList("UUID", NbtType.INT, uuidToInts(uuid));
        if (d.username != null) b.putString("CharunCore_username", d.username);
        if (d.respawnY != Integer.MIN_VALUE) {
            b.putInt("SpawnX", d.respawnX);
            b.putInt("SpawnY", d.respawnY);
            b.putInt("SpawnZ", d.respawnZ);
            b.putString("SpawnDimension", d.respawnDimension == null ? "minecraft:overworld" : d.respawnDimension);
            b.putFloat("SpawnAngle", 0.0f);
        }

        List<NbtMap> inventory = new ArrayList<>();
        for (int slot = 0; slot < d.inventoryIds.length; slot++) {
            int itemId = d.inventoryIds[slot];
            if (itemId <= 0 || d.inventoryCounts[slot] <= 0) continue;
            String name = BlockManager.itemIdToName(itemId);
            if (name == null || name.isEmpty()) continue;
            NbtMapBuilder item = NbtMap.builder();
            int vs = vanillaSlot(slot);
            if (vs < 0) continue;
            item.putInt("Slot", vs);
            item.putString("id", name.startsWith("minecraft:") ? name : "minecraft:" + name);
            item.putInt("count", d.inventoryCounts[slot]);
            NbtMap builtComponents = buildItemComponents(name,
                d.inventoryDamage != null && slot < d.inventoryDamage.length ? d.inventoryDamage[slot] : 0,
                d.inventoryEnchants != null && slot < d.inventoryEnchants.length ? d.inventoryEnchants[slot] : null,
                d.inventoryPotion != null && slot < d.inventoryPotion.length ? d.inventoryPotion[slot] : null,
                d.inventoryCustomName != null && slot < d.inventoryCustomName.length ? d.inventoryCustomName[slot] : null,
                d.inventoryTrimMaterial != null && slot < d.inventoryTrimMaterial.length ? d.inventoryTrimMaterial[slot] : -1,
                d.inventoryTrimPattern != null && slot < d.inventoryTrimPattern.length ? d.inventoryTrimPattern[slot] : -1,
                d.inventoryLore != null && slot < d.inventoryLore.length ? d.inventoryLore[slot] : null,
                d.inventoryUnbreakable != null && slot < d.inventoryUnbreakable.length && d.inventoryUnbreakable[slot],
                d.inventoryGlint != null && slot < d.inventoryGlint.length ? d.inventoryGlint[slot] : 0);
            if (!builtComponents.isEmpty()) item.putCompound("components", builtComponents);
            inventory.add(item.build());
        }
        b.putList("Inventory", NbtType.COMPOUND, inventory);
        // Bug4/33 修复: 末影箱内容随玩家数据落盘 (原硬编码空列表 -> 末影箱物品永远丢失)。
        b.putList("EnderItems", NbtType.COMPOUND, buildEnderItems(uuid));
        // B1: 插件持久数据 (PersistentDataContainer 底存) 随 NBT 落盘。
        if (d.pluginData != null && !d.pluginData.isEmpty()) {
            NbtMapBuilder pd = NbtMap.builder();
            for (java.util.Map.Entry<String, String> e : d.pluginData.entrySet()) {
                pd.putString(e.getKey(), e.getValue());
            }
            b.putCompound("CharunCorePluginData", pd.build());
        }
        b.putInt("DataVersion", DATA_VERSION);
        return b.build();
    }

    /** 把内存中的末影箱内容序列化为 EnderItems 列表（带组件, 与背包一致）。 */
    private static List<NbtMap> buildEnderItems(UUID uuid) {
        List<NbtMap> ender = new ArrayList<>();
        try {
            ContainerStore.ChestData ed = ContainerStore.peekEnderChest(uuid);
            if (ed == null) return ender;
            for (int s = 0; s < 27; s++) {
                int id = ed.slots[2 * s], cnt = ed.slots[2 * s + 1];
                if (id <= 0 || cnt <= 0) continue;
                String name = BlockManager.itemIdToName(id);
                if (name == null) continue;
                NbtMapBuilder item = NbtMap.builder();
                item.putInt("Slot", s);
                item.putString("id", name.startsWith("minecraft:") ? name : "minecraft:" + name);
                item.putInt("count", cnt);
                NbtMap comps = buildItemComponents(name, ed.meta.slotDamage[s], ed.meta.slotEnchants[s],
                        ed.meta.slotPotion[s], ed.meta.slotCustomName[s], -1, -1,
                        ed.meta.slotLore[s], ed.meta.slotUnbreakable[s], ed.meta.slotGlint[s]);
                if (!comps.isEmpty()) item.putCompound("components", comps);
                ender.add(item.build());
            }
        } catch (Exception ignored) {}
        return ender;
    }

    private static PlayerData fromNbt(NbtMap nbt, java.util.UUID uuid) {
        try {
            PlayerData d = new PlayerData();
            List<Double> pos = nbt.getList("Pos", NbtType.DOUBLE);
            if (pos != null && pos.size() >= 3) {
                d.x = pos.get(0); d.y = pos.get(1); d.z = pos.get(2);
            }
            List<Float> rot = nbt.getList("Rotation", NbtType.FLOAT);
            if (rot != null && rot.size() >= 2) {
                d.yaw = rot.get(0); d.pitch = rot.get(1);
            }
            d.health = nbt.getFloat("Health", 20.0f);
            d.airTicks = nbt.getInt("Air", 300);
            d.gameMode = nbt.getInt("playerGameType", 0);
            d.food = nbt.getInt("foodLevel", 20);
            d.saturation = nbt.getFloat("foodSaturationLevel", 5.0f);
            d.exhaustion = nbt.getFloat("foodExhaustionLevel", 0.0f);
            d.xpLevel = nbt.getInt("XpLevel", 0);
            d.xpTotal = nbt.getInt("XpTotal", 0);
            d.xpProgress = nbt.getFloat("XpP", 0.0f);
            d.heldSlot = nbt.getInt("SelectedItemSlot", 0);
            if (d.heldSlot < 0 || d.heldSlot > 8) d.heldSlot = 0;
            String dim = nbt.getString("Dimension", "minecraft:overworld");
            d.dimension = dim == null || dim.isEmpty() ? "minecraft:overworld" : dim;
            if (nbt.containsKey("SpawnX")) {
                d.respawnX = nbt.getInt("SpawnX", 0);
                d.respawnY = nbt.getInt("SpawnY", 0);
                d.respawnZ = nbt.getInt("SpawnZ", 0);
                d.respawnDimension = nbt.getString("SpawnDimension", "minecraft:overworld");
            }
            d.spawnInitialized = true;
            // B1: 插件持久数据还原
            NbtMap pluginData = nbt.getCompound("CharunCorePluginData");
            if (pluginData != null && !pluginData.isEmpty()) {
                for (String key : pluginData.keySet()) {
                    d.pluginData.put(key, pluginData.getString(key, ""));
                }
            }

            List<NbtMap> inventory = nbt.getList("Inventory", NbtType.COMPOUND);
            if (inventory != null) {
                for (NbtMap item : inventory) {
                    int slot = reverseVanillaSlot(nbt_slot(item));
                    if (slot < 0 || slot >= 46) continue;
                    String id = item.getString("id", "");
                    String name = id.startsWith("minecraft:") ? id.substring(10) : id;
                    Integer itemId = ITEM_NAME_TO_ID.get(name);
                    if (itemId == null) continue;
                    int count = item.getInt("count", item.containsKey("Count") ? (int) item.getByte("Count", (byte) 0) : 1);
                    if (count <= 0) count = 1;
                    d.inventoryIds[slot] = itemId;
                    d.inventoryCounts[slot] = count;
                    ItemComps c = parseItemComponents(item);
                    d.inventoryDamage[slot] = c.damage();
                    if (!c.enchants().isEmpty()) d.inventoryEnchants[slot] = new java.util.HashMap<>(c.enchants());
                    d.inventoryPotion[slot] = c.potion();
                    d.inventoryCustomName[slot] = c.customName();
                    d.inventoryTrimMaterial[slot] = c.trimMaterial();
                    d.inventoryTrimPattern[slot] = c.trimPattern();
                    d.inventoryLore[slot] = c.lore();
                    d.inventoryUnbreakable[slot] = c.unbreakable();
                    d.inventoryGlint[slot] = c.glint();
                }
            }
            // Bug4/33 修复: 末影箱内容从 EnderItems 还原到内存 (原未读取 -> 末影箱永远空)。
            List<NbtMap> enderItems = nbt.getList("EnderItems", NbtType.COMPOUND);
            if (enderItems != null) {
                ContainerStore.ChestData ed = ContainerStore.enderChest(uuid);
                for (NbtMap item : enderItems) {
                    int s = item.getInt("Slot", item.getInt("slot", 0));
                    if (s < 0 || s >= 27) continue;
                    String id = item.getString("id", "");
                    String name = id.startsWith("minecraft:") ? id.substring(10) : id;
                    Integer itemId = ITEM_NAME_TO_ID.get(name);
                    if (itemId == null) continue;
                    int count = item.getInt("count", item.containsKey("Count") ? (int) item.getByte("Count", (byte) 0) : 1);
                    if (count <= 0) count = 1;
                    ItemComps c = parseItemComponents(item);
                    ed.setChestSlot(s, itemId, count, c.damage(), c.enchants(), c.potion(), c.customName());
                }
            }
            return d;
        } catch (Exception e) {
            System.err.println("[玩家数据] NBT 解析异常: " + e.getMessage());
            return null;
        }
    }

    private static int nbt_slot(NbtMap item) {
        return item.getInt("Slot", item.getInt("slot", 0));
    }

    private static void saveAdvancements(UUID uuid, PlayerData d) {
        if (d.unlockedAdvancements == null || d.unlockedAdvancements.isEmpty()) return;
        try {
            com.google.gson.JsonObject root = new com.google.gson.JsonObject();
            for (String key : d.unlockedAdvancements) {
                com.google.gson.JsonObject adv = new com.google.gson.JsonObject();
                adv.add("criteria", new com.google.gson.JsonObject());
                root.add(key, adv);
            }
            java.io.File f = new java.io.File("world/advancements", uuid + ".json");
            try (java.io.Writer w = new java.io.OutputStreamWriter(new java.io.FileOutputStream(f),
                    java.nio.charset.StandardCharsets.UTF_8)) {
                new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(root, w);
            }
        } catch (Exception ignored) {}
    }

    private static Integer enchantIdByName(String name) {
        String n = name.startsWith("minecraft:") ? name : "minecraft:" + name;
        for (Map.Entry<Integer, String> e : ENCHANT_ID_TO_NAME.entrySet()) {
            if (e.getValue().equals(n)) return e.getKey();
        }
        return null;
    }

    /** 项目槽位(0-4工作台,5-8盔甲,9-35主背包,36-44快捷栏,45副手) → 原版 .dat 槽位(0-8快捷栏,9-35主背包,100-103盔甲,-106副手)。
     *  曾把 36-39 误当盔甲、40 误当副手 -> 快捷栏前 5 格被存进盔甲槽、41-44 存进无效槽,
     *  重进后快捷栏物品丢失或落到盔甲槽("重进后背包不被保存")。 */
    private static int vanillaSlot(int slot) {
        if (slot == 45) return -106;                         // 副手
        if (slot >= 5 && slot <= 8) return 100 + (slot - 5); // 盔甲
        if (slot >= 36 && slot <= 44) return slot - 36;      // 快捷栏 → 0-8
        if (slot >= 9 && slot <= 35) return slot;            // 主背包
        return -1;                                           // 工作台区不持久化
    }

    private static int reverseVanillaSlot(int vanilla) {
        if (vanilla >= 100 && vanilla <= 103) return 5 + (vanilla - 100); // 盔甲
        if (vanilla == -106) return 45;                                    // 副手
        if (vanilla >= 0 && vanilla <= 8) return 36 + vanilla;            // 快捷栏
        if (vanilla >= 9 && vanilla <= 35) return vanilla;                // 主背包
        return -1;
    }

    private static List<Integer> uuidToInts(UUID uuid) {
        long most = uuid.getMostSignificantBits(), least = uuid.getLeastSignificantBits();
        return List.of((int) (most >>> 32), (int) most, (int) (least >>> 32), (int) least);
    }
}
