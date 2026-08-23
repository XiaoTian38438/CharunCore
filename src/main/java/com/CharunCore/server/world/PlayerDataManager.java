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
                    result = fromNbt(nbt);
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
            NbtMapBuilder components = NbtMap.builder();
            if (d.inventoryDamage != null && slot < d.inventoryDamage.length && d.inventoryDamage[slot] > 0) {
                components.putInt("minecraft:damage", d.inventoryDamage[slot]);
            }
            if (d.inventoryEnchants != null && slot < d.inventoryEnchants.length
                    && d.inventoryEnchants[slot] != null && !d.inventoryEnchants[slot].isEmpty()) {
                NbtMapBuilder levels = NbtMap.builder();
                for (Map.Entry<Integer, Integer> e : d.inventoryEnchants[slot].entrySet()) {
                    String en = ENCHANT_ID_TO_NAME.get(e.getKey());
                    if (en != null && e.getValue() > 0) levels.putInt(en, e.getValue());
                }
                NbtMap enchantments = NbtMap.builder()
                        .putCompound("levels", levels.build())
                        .putBoolean("show_in_tooltip", true)
                        .build();
                // 附魔书用 stored_enchantments, 普通物品用 enchantments (曾一律写 enchantments
                // -> 附魔书重进后丢失附魔信息, 只显示"附魔书"三个灰字)。
                boolean book = "enchanted_book".equals(name);
                components.putCompound(book ? "minecraft:stored_enchantments" : "minecraft:enchantments", enchantments);
            }
            if (d.inventoryPotion != null && slot < d.inventoryPotion.length
                    && d.inventoryPotion[slot] != null && !d.inventoryPotion[slot].isEmpty()) {
                String[] parts = d.inventoryPotion[slot].split("\\|");
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
                }
            }
            if (d.inventoryCustomName != null && slot < d.inventoryCustomName.length
                    && d.inventoryCustomName[slot] != null && !d.inventoryCustomName[slot].isEmpty()) {
                components.putString("minecraft:custom_name",
                        "{\"text\":" + new com.google.gson.Gson().toJson(d.inventoryCustomName[slot]) + "}");
            }
            NbtMap builtComponents = components.build();
            if (!builtComponents.isEmpty()) item.putCompound("components", builtComponents);
            inventory.add(item.build());
        }
        b.putList("Inventory", NbtType.COMPOUND, inventory);
        b.putList("EnderItems", NbtType.COMPOUND, List.of());
        b.putInt("DataVersion", DATA_VERSION);
        return b.build();
    }

    private static PlayerData fromNbt(NbtMap nbt) {
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
                    NbtMap components = item.getCompound("components");
                    if (components == null) continue;
                    if (components.containsKey("minecraft:damage")) {
                        d.inventoryDamage[slot] = components.getInt("minecraft:damage", 0);
                    }
                    NbtMap ench = components.getCompound("minecraft:enchantments");
                    if (ench == null) ench = components.getCompound("minecraft:stored_enchantments");
                    if (ench != null) {
                        NbtMap levels = ench.getCompound("levels");
                        if (levels != null) {
                            for (String key : levels.keySet()) {
                                Integer eid = enchantIdByName(key);
                                if (eid != null) d.inventoryEnchants[slot].put(eid, levels.getInt(key, 0));
                            }
                        }
                    }
                    NbtMap potion = components.getCompound("minecraft:potion_contents");
                    if (potion != null) {
                        List<NbtMap> effects = potion.getList("custom_effects", NbtType.COMPOUND);
                        if (effects != null && !effects.isEmpty()) {
                            NbtMap eff = effects.get(0);
                            String eidName = eff.getString("id", "speed");
                            eidName = eidName.startsWith("minecraft:") ? eidName.substring(10) : eidName;
                            d.inventoryPotion[slot] = eidName + "|" + (eff.getInt("amplifier", 0) + 1)
                                    + "|" + eff.getInt("duration", 3600);
                        }
                    }
                    String customName = components.getString("minecraft:custom_name", null);
                    if (customName != null && !customName.isEmpty()) {
                        try {
                            com.google.gson.JsonObject o = com.google.gson.JsonParser.parseString(customName).getAsJsonObject();
                            if (o.has("text")) d.inventoryCustomName[slot] = o.get("text").getAsString();
                        } catch (Exception ignored) {}
                    }
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
