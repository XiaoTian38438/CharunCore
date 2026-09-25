package com.CharunCore.server.network;

import com.CharunCore.server.network.protocol.PacketBuffer;
import com.CharunCore.server.world.chunk.Chunk;

public class ChunkEncoder {

    public static void writeChunkPacket(PacketBuffer pb, Chunk chunk) {
        pb.writeInt(chunk.getX());
        pb.writeInt(chunk.getZ());

        pb.writeVarInt(2);

        int heightmapOffset = -chunk.getMinY(); // y - minY (overworld: +64, nether/end: +0)
        long[] wsData = packHeightmap(chunk.getHeightmapWorldSurface(), heightmapOffset);
        long[] mbData = packHeightmap(chunk.getHeightmapMotionBlocking(), heightmapOffset);

        pb.writeVarInt(1); // Heightmap.Types.WORLD_SURFACE id
        pb.writeVarInt(wsData.length);
        for (long l : wsData) pb.writeLong(l);

        pb.writeVarInt(4); // Heightmap.Types.MOTION_BLOCKING id
        pb.writeVarInt(mbData.length);
        for (long l : mbData) pb.writeLong(l);

        chunk.write(pb);

        // blockEntities 数组: VarInt 计数 + 每项 [packedXZ(byte) | y(short) | typeId(varint) | tag(NBT)]
        // 原版 ClientboundLevelChunkPacketData.BlockEntityInfo 线序 (1.21.11):
        //   packedXZ = (x & 15) | ((z & 15) << 4);  y 为 short;  typeId 为
        //   block_entity_type 注册表 id;  tag 为不含 id/x/y/z 的更新数据。
        // Bug9: 位置一律取自 blockEntityMap 的 key —— 曾从 BE NBT 的 x/y/z 读取,
        // 而 createInitialBlockEntity 多数分支不写坐标 -> 全部发到 (0,0,0),
        // 客户端把 BE 实例化在错误位置, 原位置的告示牌/床等 BE 方块重进后透明。
        java.util.Map<Long, org.cloudburstmc.nbt.NbtMap> beMap = chunk.getBlockEntityMap();
        // Bug9 诊断开关: -Dcharun.debugBe=1 时打印区块包携带的 BE(名称/坐标/typeId)
        if (BE_DEBUG && !beMap.isEmpty()) {
            StringBuilder sb = new StringBuilder("[BE诊断] chunk(" + chunk.getX() + "," + chunk.getZ()
                + ") be=" + beMap.size() + ":");
            int n = 0;
            for (var entry : beMap.entrySet()) {
                if (n++ >= 4) { sb.append(" ..."); break; }
                long k = entry.getKey();
                int rx = (int)(k & 15), rz = (int)((k >> 4) & 15), yy = (int)((k >> 16) & 0xFFFF) - 64;
                String id = entry.getValue().getString("id", "?");
                sb.append(" ").append(id.replace("minecraft:", "")).append("@")
                  .append(rx).append(",").append(yy).append(",").append(rz);
            }
            System.out.println(sb);
        }
        pb.writeVarInt(beMap.size());
        for (java.util.Map.Entry<Long, org.cloudburstmc.nbt.NbtMap> beEntry : beMap.entrySet()) {
            long beKey = beEntry.getKey();
            int rx = (int) (beKey & 15);
            int rz = (int) ((beKey >> 4) & 15);
            int y = (int) ((beKey >> 16) & 0xFFFF) - 64;
            org.cloudburstmc.nbt.NbtMap be = beEntry.getValue();
            String id = be.getString("id", "minecraft:chest");
            if (id.startsWith("minecraft:")) id = id.substring(10);
            // 用 RegistryHelper.blockEntityTypeId 统一规范化(oak_sign->sign, *_bed->bed 等),
            // 旧私有 blockEntityTypeId 不规范化材质前缀 -> 告示牌/床等返回 chest -> 客户端渲染错误/透明。
            int typeId = com.CharunCore.server.utils.RegistryHelper.blockEntityTypeId(id);
            pb.writeByte((byte) (rx | (rz << 4)));
            pb.writeShort(y);
            pb.writeVarInt(typeId);
            // tag: 去掉 id/x/y/z, 保留数据 (Items/CustomName/LootTable 等)。
            // 用 writeAnonymousNbt(byte(10)+entries+TAG_End), 与原版 NbtIo.writeAnyTag 一致;
            // writeNbt 尾部多写一个 0x00 会在 BE 连续数组里每项错位 1 字节。
            org.cloudburstmc.nbt.NbtMapBuilder builder = org.cloudburstmc.nbt.NbtMap.builder();
            for (String key : be.keySet()) {
                if (key.equals("id") || key.equals("x") || key.equals("y") || key.equals("z")) continue;
                Object val = be.get(key);
                if (val instanceof String || val instanceof Integer || val instanceof Long
                    || val instanceof Byte || val instanceof Short || val instanceof Float
                    || val instanceof Double || val instanceof org.cloudburstmc.nbt.NbtMap
                    || val instanceof org.cloudburstmc.nbt.NbtList
                    || val instanceof int[] || val instanceof long[] || val instanceof byte[]) {
                    builder.put(key, val);
                }
            }
            pb.writeAnonymousNbt(builder.build());
        }

        writeLightData(pb, chunk);
    }

        private static final boolean BE_DEBUG = Boolean.parseBoolean(System.getProperty("charun.debugBe", "0"));

    private static final byte[] SKY_FULL_SECTION = new byte[2048];
    static {
        java.util.Arrays.fill(SKY_FULL_SECTION, (byte) 0xFF);
    }

    static void writeLightData(PacketBuffer pb, Chunk chunk) {
        chunk.ensureLight();
        byte[][] sky = chunk.skyLightSections();
        byte[][] block = chunk.blockLightSections();
        int sections = chunk.getSectionCount();
        // 原版语义 (ClientboundLightUpdatePacketData): 掩码位 b 对应
        // light section = minLightSection + b，其中 minLightSection = minSectionY - 1。
        // 即 bit0 = 世界底下方 padding section, bit1..N = 数据 section, bit N+1 = 世界顶上方 padding。
        // Bug40: 曾把数据 section 锚在 bit0 -> 客户端把所有光照整体下移 16 格渲染,
        // 火把/萤石/岩浆光斑全部落在地下, 表现为"所有光源只亮一格"。
        int lightSections = sections + 2;

        long skyMask = 0, blockMask = 0, emptySky = 0, emptyBlock = 0;
        java.util.List<byte[]> skyArrays = new java.util.ArrayList<>(lightSections);
        java.util.List<byte[]> blockArrays = new java.util.ArrayList<>(lightSections);
        for (int b = 0; b < lightSections; b++) {
            if (b == 0) {
                emptySky |= 1L;
                emptyBlock |= 1L;
                continue;
            }
            if (b > sections) {
                skyMask |= 1L << b;
                skyArrays.add(SKY_FULL_SECTION);
                emptyBlock |= 1L << b;
                continue;
            }
            int i = b - 1;
            byte[] s = sky[i];
            if (s != null && isNonZero(s)) { skyMask |= 1L << b; skyArrays.add(s); }
            else emptySky |= 1L << b;
            byte[] bl = block[i];
            if (bl != null && isNonZero(bl)) { blockMask |= 1L << b; blockArrays.add(bl); }
            else emptyBlock |= 1L << b;
        }

        pb.writeBitSet(bitSetToLongs(skyMask));
        pb.writeBitSet(bitSetToLongs(blockMask));
        pb.writeBitSet(bitSetToLongs(emptySky));
        pb.writeBitSet(bitSetToLongs(emptyBlock));

        pb.writeVarInt(skyArrays.size());
        for (byte[] arr : skyArrays) {
            pb.writeVarInt(2048);
            pb.writeRawBytes(arr);
        }
        pb.writeVarInt(blockArrays.size());
        for (byte[] arr : blockArrays) {
            pb.writeVarInt(2048);
            pb.writeRawBytes(arr);
        }
    }

    static void writeLightUpdate(PacketBuffer pb, Chunk chunk) {
        pb.writeVarInt(chunk.getX());
        pb.writeVarInt(chunk.getZ());
        writeLightData(pb, chunk);
    }

    private static boolean isNonZero(byte[] arr) {
        for (byte b : arr) if (b != 0) return true;
        return false;
    }

    private static long[] bitSetToLongs(long mask) {
        if (mask == 0) return new long[0];
        return new long[]{mask};
    }

    /** block_entity_type 注册表 id（按原版 1.21.11 注册顺序, BlockEntityType.register 顺序）。
     *  用于 0x2C 区块包的 BE typeId 字段。 */
    private static int blockEntityTypeId(String name) {
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
            case "mob_spawner" -> 9;
            case "creaking_heart" -> 10;
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
            case "campfire" -> 33;
            case "beehive" -> 34;
            case "sculk_sensor" -> 35;
            case "calibrated_sculk_sensor" -> 36;
            case "sculk_catalyst" -> 37;
            case "sculk_shrieker" -> 38;
            case "chiseled_bookshelf" -> 39;
            case "shelf" -> 40;
            case "brushable_block" -> 41;
            case "decorated_pot" -> 42;
            case "crafter" -> 43;
            case "trial_spawner" -> 44;
            case "vault" -> 45;
            case "test_block" -> 46;
            case "test_instance_block" -> 47;
            case "copper_golem_statue" -> 48;
            default -> 1; // 未知类型回退 chest
        };
    }

    private static long[] packHeightmap(int[] heightmap, int minY) {        int bitsPerEntry = 9;
        int longCount = (256 * bitsPerEntry + 63) / 64; // 256 项 × 9 bit 正好 36 个 long
        long[] result = new long[longCount];

        for (int i = 0; i < 256; i++) {
            int value = heightmap[i] - minY; // encode as y - minY (protocol format)
            if (value < 0) value = 0;
            if (value > 383) value = 383;
            int bitOffset = i * bitsPerEntry;
            int longIndex = bitOffset / 64;
            int bitIndex = bitOffset % 64;
            result[longIndex] |= ((long) value & ((1L << bitsPerEntry) - 1)) << bitIndex;
            if (bitIndex + bitsPerEntry > 64 && longIndex + 1 < longCount) {
                result[longIndex + 1] |= ((long) value & ((1L << bitsPerEntry) - 1)) >> (64 - bitIndex);
            }
        }

        return result;
    }
}
