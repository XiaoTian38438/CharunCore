package com.CharunCore.server.utils;

import com.CharunCore.server.network.protocol.PacketBuffer;
import com.CharunCore.server.world.WorldManager;
import com.CharunCore.server.world.chunk.Chunk;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;

import java.io.File;
import java.nio.file.Files;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RegistryHelper {

    /**
     * 发送所有 registry data 包。
     * 1. 发送 dumped_registries/ 中的预生成 .bin 文件（旧逻辑）
     * 2. 动态生成并发送 minecraft:block registry（关键！之前缺失这个导致客户端崩溃）
     * 3. 动态生成并发送 minecraft:item registry
     */
    public static void sendAllDumpedRegistries(BiConsumer<Integer, Consumer<PacketBuffer>> packetSender) {
        // ── 1. 发送预生成的 .bin registry 文件 ──
        File folder = new File("dumped_registries");
        File[] files = folder.listFiles((dir, name) -> name.startsWith("reg_") && name.endsWith(".bin"));
        if (files != null) {
            for (File f : files) {
                try {
                    byte[] data = Files.readAllBytes(f.toPath());
                    packetSender.accept(0x07, pb -> pb.writeRawBytes(data));
                } catch (Exception ignored) {}
            }
        }

        // ── 2. 动态生成 minecraft:block registry ──
        // 这是最关键的修复！客户端需要 block registry 来解码 PalettedContainer 中的 state ID
        sendBlockRegistry(packetSender);

        // ── 3. 动态生成 minecraft:item registry ──
        sendItemRegistry(packetSender);

        // ── 4. 动态生成 minecraft:menu registry（用于容器交互）──
        sendMenuRegistry(packetSender);
    }

    /**
     * 动态生成并发送 minecraft:block registry (0x07 Registry Data)。
     *
     * 格式与现有 .bin 文件一致：
     *   [VarInt: registry_name_length] [UTF-8: registry_name]
     *   [VarInt: entry_count]
     *   for each entry:
     *     [VarInt: entry_name_length] [UTF-8: entry_name]
     *     [0x00] (空 NBT Compound — TAG_END)
     *
     * 客户端收到后，会按 entry 的顺序分配 block state ID：
     *   第 0 个 entry (air) → stateId 0
     *   第 1 个 entry (stone) → stateId 1
     *   ...
     *   第 34 个 entry (bedrock) → bedrock 的 properties 开始
     *
     * 注意：这必须与 BlockStateHelper 加载的 blocks.json 中的
     *       block 顺序和 stateId 分配完全一致！
     */
    private static void sendBlockRegistry(BiConsumer<Integer, Consumer<PacketBuffer>> packetSender) {
        packetSender.accept(0x07, pb -> {
            // Registry name: "minecraft:block"
            pb.writeString("minecraft:block");

            // 收集所有 block 名称，按 BlockStateHelper 中的 ID 顺序排列
            // BlockStateHelper.byName 是 HashMap，无法保证顺序，
            // 所以我们直接重新读取 blocks.json 中的顺序
            java.util.List<String> blockNames = new java.util.ArrayList<>();
            try {
                com.google.gson.JsonArray arr = com.google.gson.JsonParser.parseReader(
                        new java.io.FileReader("block/blocks.json")).getAsJsonArray();
                for (com.google.gson.JsonElement el : arr) {
                    String name = el.getAsJsonObject().get("name").getAsString();
                    blockNames.add(name);
                }
            } catch (Exception e) {
                System.err.println("[错误] 无法读取 blocks.json 生成 block registry: " + e.getMessage());
                return;
            }

            // Entry count
            pb.writeVarInt(blockNames.size());

            // Each entry: name + empty NBT compound (0x00 = TAG_END)
            for (String name : blockNames) {
                pb.writeString("minecraft:" + name);
                pb.writeByte(0x00); // 空 NBT Compound — 只有 TAG_END
            }
        });
    }

    /**
     * 动态生成并发送 minecraft:item registry (0x07 Registry Data)。
     * 格式与 block registry 相同。
     */
    private static void sendItemRegistry(BiConsumer<Integer, Consumer<PacketBuffer>> packetSender) {
        packetSender.accept(0x07, pb -> {
            pb.writeString("minecraft:item");

            java.util.List<String> itemNames = new java.util.ArrayList<>();
            try {
                com.google.gson.JsonArray arr = com.google.gson.JsonParser.parseReader(
                        new java.io.FileReader("block/items.json")).getAsJsonArray();
                for (com.google.gson.JsonElement el : arr) {
                    String name = el.getAsJsonObject().get("name").getAsString();
                    itemNames.add(name);
                }
            } catch (Exception e) {
                System.err.println("[错误] 无法读取 items.json 生成 item registry: " + e.getMessage());
                // 如果 items.json 不可用，至少发送 air
                pb.writeVarInt(1);
                pb.writeString("minecraft:air");
                pb.writeByte(0x00);
                return;
            }

            pb.writeVarInt(itemNames.size());
            for (String name : itemNames) {
                pb.writeString("minecraft:" + name);
                pb.writeByte(0x00);
            }
        });
    }

    /**
     * 将内存中的 Chunk 转换为 Anvil NBT 格式（用于保存到磁盘）。
     */
    public static NbtMap createAnvilNbtFromChunk(Chunk chunk) {
        java.util.List<NbtMap> sections = new java.util.ArrayList<>();

        for (int i = 0; i < 24; i++) {
            Chunk.Section sec = chunk.getSections()[i];
            int y = i + (chunk.getMinY() >> 4); // overworld: -4..19, nether/end: 0..7

            // 收集调色盘
            java.util.LinkedHashMap<Integer, String> idToName = new java.util.LinkedHashMap<>();
            for (int stateId : sec.getBlocks()) {
                if (!idToName.containsKey(stateId)) {
                    String name = BlockStateHelper.getName(stateId);
                    idToName.put(stateId, "minecraft:" + name);
                }
            }

            // 构建 palette NBT list
            java.util.List<NbtMap> paletteList = new java.util.ArrayList<>();
            java.util.List<Integer> paletteIds = new java.util.ArrayList<>(idToName.keySet());
            for (int stateId : paletteIds) {
                String blockName = idToName.get(stateId);
                NbtMapBuilder entryBuilder = NbtMap.builder().putString("Name", blockName);
                BlockStateHelper.BlockData bd = BlockStateHelper.getByState(stateId);
                if (bd != null && bd.props != null && !bd.props.isEmpty()) {
                    java.util.Map<String, String> props = bd.parseState(stateId);
                    if (props != null && !props.isEmpty()) {
                        NbtMapBuilder propsBuilder = NbtMap.builder();
                        for (java.util.Map.Entry<String, String> e : props.entrySet()) {
                            propsBuilder.putString(e.getKey(), e.getValue());
                        }
                        entryBuilder.putCompound("Properties", propsBuilder.build());
                    }
                }
                paletteList.add(entryBuilder.build());
            }

            NbtMapBuilder statesBuilder = NbtMap.builder()
                    .putList("palette", org.cloudburstmc.nbt.NbtType.COMPOUND, paletteList);

            // 多于1个方块才需要 data 数组
            if (paletteList.size() > 1) {
                java.util.Map<Integer, Integer> stateToIdx = new java.util.HashMap<>();
                for (int j = 0; j < paletteIds.size(); j++) stateToIdx.put(paletteIds.get(j), j);

                int bpe = computeBpe(paletteIds.size());
                int[] blockStates = sec.getBlocks();
                int[] paletteIndices = new int[4096];
                for (int j = 0; j < 4096; j++) {
                    Integer idx = stateToIdx.get(blockStates[j]);
                    if (idx == null) {
                        System.err.println("[Anvil 保存警告] section Y=" + y + " 位置 " + j + " 的 stateId=" + blockStates[j] + " 不在 palette 中，将使用 0 作为索引");
                        paletteIndices[j] = 0;
                    } else {
                        paletteIndices[j] = idx;
                    }
                }
                long[] data = BitStorage.pack(bpe, paletteIndices);
                statesBuilder.putLongArray("data", data);
            }

            boolean hasBlocks = false;
            for (int b : sec.getBlocks()) {
                if (b != 0) { hasBlocks = true; break; }
            }
            if (!hasBlocks) continue;

            byte[] emptyLight = new byte[2048];
            sections.add(NbtMap.builder()
                    .putByte("Y", (byte) y)
                    .putCompound("block_states", statesBuilder.build())
                    .putCompound("biomes", buildBiomeNbt(sec))
                    .putByteArray("BlockLight", emptyLight)
                    .putByteArray("SkyLight", emptyLight)
                    .build());
        }

        chunk.getHeightmapWorldSurface();
        chunk.getHeightmapMotionBlocking();
        long[] wsHeightmap = packHeightmap(chunk.getHeightmapWorldSurface());
        long[] mbHeightmap = packHeightmap(chunk.getHeightmapMotionBlocking());

        java.util.List<NbtMap> beList = new java.util.ArrayList<>();
        for (java.util.Map.Entry<Long, org.cloudburstmc.nbt.NbtMap> e : chunk.getBlockEntityMap().entrySet()) {
            long beKey = e.getKey();
            int rx = (int)(beKey & 15);
            int rz = (int)((beKey >> 4) & 15);
            int ry = (int)((beKey >> 16) & 0xFFFF) - 64;
            NbtMapBuilder beb = NbtMap.builder();
            beb.putInt("x", rx);
            beb.putInt("y", ry);
            beb.putInt("z", rz);
            org.cloudburstmc.nbt.NbtMap src = e.getValue();
            for (String bk : src.keySet()) {
                if (bk.equals("x") || bk.equals("y") || bk.equals("z")) continue;
                beb.put(bk, src.get(bk));
            }
            beList.add(beb.build());
        }

        return NbtMap.builder()
                .putInt("DataVersion", 4671)
                .putInt("xPos", chunk.getX())
                .putInt("zPos", chunk.getZ())
                .putInt("yPos", chunk.getMinY() >> 4)
                .putString("Status", "minecraft:full")
                .putLong("LastUpdate", 0L)
                .putLong("InhabitedTime", 0L)
                .putList("PostProcessing", org.cloudburstmc.nbt.NbtType.BYTE_ARRAY, java.util.Collections.emptyList())
                .putList("block_entities", org.cloudburstmc.nbt.NbtType.COMPOUND, beList)
                .putList("block_ticks", org.cloudburstmc.nbt.NbtType.COMPOUND, java.util.Collections.emptyList())
                .putList("fluid_ticks", org.cloudburstmc.nbt.NbtType.COMPOUND, java.util.Collections.emptyList())
                .putCompound("Heightmaps", NbtMap.builder()
                        .putLongArray("WORLD_SURFACE", wsHeightmap)
                        .putLongArray("MOTION_BLOCKING", mbHeightmap)
                        .putLongArray("MOTION_BLOCKING_NO_LEAVES", mbHeightmap)
                        .putLongArray("OCEAN_FLOOR", wsHeightmap)
                        .build())
                .putCompound("structures", NbtMap.builder()
                        .putCompound("starts", NbtMap.builder().build())
                        .putCompound("References", NbtMap.builder().build())
                        .build())
                .putList("sections", org.cloudburstmc.nbt.NbtType.COMPOUND, sections)
                .putInt("CharunCore_gen_version", WorldManager.CHUNK_GEN_VERSION)
                .build();
    }

    private static NbtMap buildBiomeNbt(Chunk.Section sec) {
        int[] biomes = sec.getBiomes();
        java.util.LinkedHashMap<Integer, String> biomePalette = new java.util.LinkedHashMap<>();
        for (int b : biomes) {
            if (!biomePalette.containsKey(b)) {
                biomePalette.put(b, biomeIdToName(b));
            }
        }
        java.util.List<String> paletteNames = new java.util.ArrayList<>(biomePalette.values());
        if (paletteNames.size() == 1) {
            return NbtMap.builder()
                    .putList("palette", org.cloudburstmc.nbt.NbtType.STRING, paletteNames)
                    .build();
        }
        java.util.Map<Integer, Integer> biomeToIdx = new java.util.HashMap<>();
        int idx = 0;
        for (int b : biomePalette.keySet()) {
            biomeToIdx.put(b, idx++);
        }
        int bpe = computeBiomeBpe(paletteNames.size());
        int[] indices = new int[64];
        for (int j = 0; j < 64; j++) {
            indices[j] = biomeToIdx.getOrDefault(biomes[j], 0);
        }
        long[] data = BitStorage.pack(bpe, indices);
        return NbtMap.builder()
                .putList("palette", org.cloudburstmc.nbt.NbtType.STRING, paletteNames)
                .putLongArray("data", data)
                .build();
    }

    private static int computeBpe(int paletteSize) {
        if (paletteSize <= 1) return 0;
        int bits = ceillog2(paletteSize);
        return switch (bits) {
            case 0 -> 0;
            case 1, 2, 3, 4 -> 4;
            case 5 -> 5;
            case 6 -> 6;
            case 7 -> 7;
            case 8 -> 8;
            default -> bits;
        };
    }

    private static int computeBiomeBpe(int paletteSize) {
        if (paletteSize <= 1) return 0;
        int bits = ceillog2(paletteSize);
        return switch (bits) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            default -> bits;
        };
    }

    private static int ceillog2(int n) {
        if (n <= 1) return 0;
        return 32 - Integer.numberOfLeadingZeros(n - 1);
    }

    public static int computeBpeForRead(int paletteSize) {
        return computeBpe(paletteSize);
    }

    private static String biomeIdToName(int id) {
        String[] names = {
            "badlands", "bamboo_jungle", "basalt_deltas", "beach", "birch_forest",
            "cherry_grove", "cold_ocean", "crimson_forest", "dark_forest", "deep_cold_ocean",
            "deep_dark", "deep_frozen_ocean", "deep_lukewarm_ocean", "deep_ocean", "desert",
            "dripstone_caves", "end_barrens", "end_highlands", "end_midlands", "eroded_badlands",
            "flower_forest", "forest", "frozen_ocean", "frozen_peaks", "frozen_river",
            "grove", "ice_spikes", "jagged_peaks", "jungle", "lukewarm_ocean",
            "lush_caves", "mangrove_swamp", "meadow", "mushroom_fields", "nether_wastes",
            "ocean", "old_growth_birch_forest", "old_growth_pine_taiga", "old_growth_spruce_taiga", "pale_garden",
            "plains", "river", "savanna", "savanna_plateau", "small_end_islands",
            "snowy_beach", "snowy_plains", "snowy_slopes", "snowy_taiga", "soul_sand_valley",
            "sparse_jungle", "stony_peaks", "stony_shore", "sunflower_plains", "swamp",
            "taiga", "the_end", "the_void", "warm_ocean", "warped_forest",
            "windswept_forest", "windswept_gravelly_hills", "windswept_hills", "windswept_savanna", "wooded_badlands"
        };
        if (id >= 0 && id < names.length) return "minecraft:" + names[id];
        return "minecraft:plains";
    }

    /** #52 反向映射: biome 名 -> 本服内部 biome id (与 biomeIdToName 数组索引一致)。 */
    public static int biomeNameToId(String name) {
        if (name == null) return 0;
        if (name.startsWith("minecraft:")) name = name.substring(10);
        String[] names = {
            "badlands", "bamboo_jungle", "basalt_deltas", "beach", "birch_forest",
            "cherry_grove", "cold_ocean", "crimson_forest", "dark_forest", "deep_cold_ocean",
            "deep_dark", "deep_frozen_ocean", "deep_lukewarm_ocean", "deep_ocean", "desert",
            "dripstone_caves", "end_barrens", "end_highlands", "end_midlands", "eroded_badlands",
            "flower_forest", "forest", "frozen_ocean", "frozen_peaks", "frozen_river",
            "grove", "ice_spikes", "jagged_peaks", "jungle", "lukewarm_ocean",
            "lush_caves", "mangrove_swamp", "meadow", "mushroom_fields", "nether_wastes",
            "ocean", "old_growth_birch_forest", "old_growth_pine_taiga", "old_growth_spruce_taiga", "pale_garden",
            "plains", "river", "savanna", "savanna_plateau", "small_end_islands",
            "snowy_beach", "snowy_plains", "snowy_slopes", "snowy_taiga", "soul_sand_valley",
            "sparse_jungle", "stony_peaks", "stony_shore", "sunflower_plains", "swamp",
            "taiga", "the_end", "the_void", "warm_ocean", "warped_forest",
            "windswept_forest", "windswept_gravelly_hills", "windswept_hills", "windswept_savanna", "wooded_badlands"
        };
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(name)) return i;
        }
        return 0; // 未知回退平原
    }

    private static long[] packHeightmap(int[] heightmap) {
        int bpe = 9;
        int[] adjusted = new int[256];
        for (int i = 0; i < 256; i++) {
            adjusted[i] = heightmap[i] + 64;
        }
        return BitStorage.pack(bpe, adjusted);
    }

    /**
     * 原版 1.21.11 minecraft:menu 注册表顺序（来自 net.minecraft.world.inventory.MenuType 的静态注册顺序）。
     * 这是客户端内建 STATIC 注册表的权威顺序，open_screen(0x39) 的 menuType VarInt 必须与之匹配。
     * 注意：原版包含 crafter_3x3（索引 7），本项目之前遗漏了它，导致后续所有索引整体偏移 1。
     */
    private static final java.util.List<String> MENU_ORDER = java.util.List.of(
        "generic_9x1", "generic_9x2", "generic_9x3", "generic_9x4", "generic_9x5", "generic_9x6",
        "generic_3x3", "crafter_3x3", "anvil", "beacon", "blast_furnace", "brewing_stand",
        "crafting", "enchantment", "furnace", "grindstone", "hopper", "lectern", "loom",
        "merchant", "shulker_box", "smithing", "smoker", "cartography_table", "stonecutter"
    );

    /** 返回容器/菜单名对应的原版 menuType 索引（open_screen 用）。未知名称返回 0。 */
    public static int menuType(String name) {
        int idx = MENU_ORDER.indexOf(name);
        return idx < 0 ? 0 : idx;
    }

    /** block_entity_type 注册表 id（原版 1.21.11 BlockEntityType.register 顺序）。
     *  用于 0x2C 区块包 / 0x09 block_entity_data 的 typeId 字段。
     *  告示牌/头颅/床等按材质派生名称需先规范化到注册表条目名(sign/skull/bed)。 */
    public static int blockEntityTypeId(String name) {
        if (name == null) return 1;
        if (name.startsWith("minecraft:")) name = name.substring(10);
        // 规范化: 材质前缀的告示牌/墙告示牌 -> sign
        if (name.endsWith("_sign") || name.endsWith("_wall_sign")) name = "sign";
        if (name.endsWith("_bed")) name = "bed";
        if (name.endsWith("_banner")) name = "banner";
        if (name.endsWith("_skull") || name.endsWith("_wall_head") || name.endsWith("_head")) name = "skull";
        if (name.endsWith("_shulker_box")) name = "shulker_box";
        return switch (name) {
            case "furnace" -> 0;
            case "chest" -> 1;
            case "trapped_chest" -> 2;
            case "ender_chest" -> 3;
            case "jukebox" -> 4;
            case "dispenser" -> 5;
            case "dropper" -> 6;
            case "sign" -> 7;
            case "hanging_sign" -> 8;
            case "mob_spawner", "spawner" -> 9;
            case "creaking_heart" -> 10;   // #29 1.21.11 新增, 曾漏 -> 后续索引全部偏移 1
            case "piston" -> 11;
            case "brewing_stand" -> 12;
            case "enchanting_table" -> 13;
            case "end_portal" -> 14;
            case "beacon" -> 15;
            case "skull" -> 16;
            case "daylight_detector" -> 17;
            case "hopper" -> 18;
            case "comparator" -> 19;
            case "banner" -> 20;
            case "structure_block" -> 21;
            case "end_gateway" -> 22;
            case "command_block" -> 23;
            case "shulker_box" -> 24;
            case "bed" -> 25;
            case "conduit" -> 26;
            case "barrel" -> 27;
            case "smoker" -> 28;
            case "blast_furnace" -> 29;
            case "lectern" -> 30;
            case "bell" -> 31;
            case "jigsaw" -> 32;
            case "campfire", "soul_campfire" -> 33;
            case "beehive" -> 34;
            case "sculk_sensor" -> 35;
            case "calibrated_sculk_sensor" -> 36;
            case "sculk_catalyst" -> 37;
            case "sculk_shrieker" -> 38;
            case "chiseled_bookshelf" -> 39;
            case "shelf" -> 40;                    // 1.21.11 新增
            case "brushable_block" -> 41;          // suspicious_sand/gravel
            case "decorated_pot" -> 42;
            case "crafter" -> 43;
            case "trial_spawner" -> 44;
            case "vault" -> 45;
            case "test_block" -> 46;
            case "test_instance_block" -> 47;
            case "copper_golem_statue" -> 48;
            default -> 1;
        };
    }

    /**
     * 动态生成并发送 minecraft:menu registry (0x07 Registry Data)。
     * 顺序必须与原版内建注册表一致（见 MENU_ORDER），否则 open_screen 的 menuType 索引错位。
     */
    private static void sendMenuRegistry(BiConsumer<Integer, Consumer<PacketBuffer>> packetSender) {
        packetSender.accept(0x07, pb -> {
            pb.writeString("minecraft:menu");
            pb.writeVarInt(MENU_ORDER.size());
            for (String typeName : MENU_ORDER) {
                pb.writeString("minecraft:" + typeName);
                pb.writeByte(0x00);
            }
        });
    }

    /** @deprecated 使用 BlockStateHelper 代替 */
    public static int getBlockIdByName(String name) {
        return switch (name) {
            case "minecraft:air" -> 0;
            case "minecraft:stone" -> 1;
            case "minecraft:dirt" -> 10;
            case "minecraft:grass_block" -> 33;
            case "minecraft:bedrock" -> 34;
            default -> 1;
        };
    }
}
