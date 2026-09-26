package com.CharunCore.server.world.chunk;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.light.LightEngine;
import com.CharunCore.server.utils.BitStorage;
import io.netty.buffer.ByteBuf;
import com.CharunCore.server.network.protocol.PacketBuffer;

public class Chunk {
    private final int x, z;
    private final int minY;
    private final int sectionCount;
    private final Section[] sections = new Section[24];
    private final java.util.Map<Long, org.cloudburstmc.nbt.NbtMap> blockEntities = new java.util.concurrent.ConcurrentHashMap<>();
    /** B3: 区块入内存时间戳 —— unloadDistantChunks 的保护期依据(90 秒内新载区块不卸载)。 */
    public volatile long loadedAtMs = System.currentTimeMillis();

    public Chunk(int x, int z) {
        this(x, z, -64, 24);
    }

    public Chunk(int x, int z, int minY, int sectionCount) {
        this.x = x;
        this.z = z;
        this.minY = minY;
        this.sectionCount = Math.min(sectionCount, 24);
        for (int i = 0; i < 24; i++) {
            sections[i] = new Section();
        }
    }

    public int getMinY() { return minY; }
    public int getSectionCount() { return sectionCount; }

    public void setBlockEntity(int rx, int y, int rz, org.cloudburstmc.nbt.NbtMap nbt) {
        long key = blockEntityKey(rx & 15, y, rz & 15);
        blockEntities.put(key, nbt);
        com.CharunCore.server.world.WorldManager.markChunkDirty(this);
    }

    /** 区块加载(从磁盘回读 BE)用: 不标脏 —— 刚加载的区块本就与磁盘一致。 */
    public void putBlockEntityRaw(int rx, int y, int rz, org.cloudburstmc.nbt.NbtMap nbt) {
        blockEntities.put(blockEntityKey(rx & 15, y, rz & 15), nbt);
    }

    public org.cloudburstmc.nbt.NbtMap getBlockEntity(int rx, int y, int rz) {
        return blockEntities.get(blockEntityKey(rx & 15, y, rz & 15));
    }

    public java.util.Collection<org.cloudburstmc.nbt.NbtMap> getBlockEntities() {
        return blockEntities.values();
    }

    public java.util.Map<Long, org.cloudburstmc.nbt.NbtMap> getBlockEntityMap() {
        return blockEntities;
    }

    private static long blockEntityKey(int rx, int y, int rz) {
        return ((long)(y + 64) << 16) | ((rz & 15) << 4) | (rx & 15);
    }

    private final int[] heightmapWorldSurface = new int[256];
    private final int[] heightmapMotionBlocking = new int[256];
    private boolean heightmapDirty = true;
    private final int[] biomePerColumn = new int[256];
    private static final int BIOME_PLAINS = 40;

    public void setBlock(int rx, int y, int rz, int stateId) {
        int idx = (y - minY) >> 4;
        if (idx >= 0 && idx < 24) sections[idx].setInternalBlock(rx & 15, y & 15, rz & 15, stateId);
        heightmapDirty = true;
    }

    public int getBlock(int rx, int y, int rz) {
        int idx = (y - minY) >> 4;
        if (idx < 0 || idx >= 24) return 0;
        return sections[idx].getBlock(rx & 15, y & 15, rz & 15);
    }

    public void fillLayer(int y, short id) {
        for (int rx = 0; rx < 16; rx++) {
            for (int rz = 0; rz < 16; rz++) {
                setBlock(rx, y, rz, id);
            }
        }
    }

    public int[] getHeightmapWorldSurface() {
        computeHeightmaps();
        return heightmapWorldSurface;
    }

    public int[] getHeightmapMotionBlocking() {
        computeHeightmaps();
        return heightmapMotionBlocking;
    }

    private void computeHeightmaps() {
        if (!heightmapDirty) return;
        heightmapDirty = false;

        for (int xz = 0; xz < 256; xz++) {
            int rx = xz & 15;
            int rz = xz >> 4;
            int ws = -999;
            int mb = -999;
            int top = minY + sectionCount * 16 - 1;
            for (int y = top; y >= minY; y--) {
                int state = getBlock(rx, y, rz);
                if (state != 0) {
                    if (mb == -999) mb = y + 1;
                    if (ws == -999) {
                        String name = BlockStateHelper.getName(state);
                        if (!name.equals("water") && !name.equals("lava") && !name.equals("bubble_column")) {
                            ws = y + 1;
                        }
                    }
                    if (ws != -999 && mb != -999) break;
                }
            }
            heightmapWorldSurface[xz] = ws == -999 ? minY : ws;
            heightmapMotionBlocking[xz] = mb == -999 ? minY : mb;
        }
    }

    public int getX() { return x; }
    public int getZ() { return z; }
    public Section[] getSections() { return sections; }

    public DimensionType dim = DimensionType.OVERWORLD;
    private final byte[][] skyLightArrays = new byte[24][];
    private final byte[][] blockLightArrays = new byte[24][];
    private volatile boolean lightComputed = false;

    public byte[][] skyLightSections() { return skyLightArrays; }
    public byte[][] blockLightSections() { return blockLightArrays; }
    public boolean isLightComputed() { return lightComputed; }
    public void markLightComputed() { lightComputed = true; }
    public void clearLight() {
        java.util.Arrays.fill(skyLightArrays, null);
        java.util.Arrays.fill(blockLightArrays, null);
        lightComputed = false;
    }
    public void ensureLight() {
        LightEngine.ensureChunkLight(this);
    }

    public void setBiome(int rx, int rz, int biomeId) {
        biomePerColumn[(rz & 15) * 16 + (rx & 15)] = biomeId;
    }

    public void setBiome(int rx, int y, int rz, int biomeId) {
        int secIdx = (y - minY) >> 4;
        if (secIdx >= 0 && secIdx < 24) {
            int qy = (y & 15) >> 2;
            int qx = (rx & 15) >> 2;
            int qz = (rz & 15) >> 2;
            sections[secIdx].setBiome(qx, qy, qz, biomeId);
        }
    }

    public int getBiome(int rx, int rz) {
        int id = biomePerColumn[(rz & 15) * 16 + (rx & 15)];
        return id > 0 ? id : BIOME_PLAINS;
    }

    public int getBiome(int rx, int y, int rz) {
        int secIdx = (y - minY) >> 4;
        if (secIdx < 0 || secIdx >= 24) return BIOME_PLAINS;
        int qx = (rx & 15) >> 2;
        int qy = (y & 15) >> 2;
        int qz = (rz & 15) >> 2;
        int id = sections[secIdx].getBiome(qx, qy, qz);
        return id > 0 ? id : BIOME_PLAINS;
    }

    public static class Section {
        private final int[] blocks = new int[4096];
        private final int[] biomes = new int[64];
        private int nonAirCount = 0;

        protected void setInternalBlock(int rx, int ry, int rz, int stateId) {
            int localIdx = (ry << 8) | (rz << 4) | rx;
            int oldId = blocks[localIdx];
            if (oldId == stateId) return;

            if (oldId == 0 && stateId != 0) nonAirCount++;
            else if (oldId != 0 && stateId == 0) nonAirCount--;

            blocks[localIdx] = stateId;
        }

        public int getBlock(int rx, int ry, int rz) {
            return blocks[(rx & 15) | ((rz & 15) << 4) | ((ry & 15) << 8)];
        }

        public void setBiome(int qx, int qy, int qz, int biomeId) {
            biomes[(qy << 4) | (qz << 2) | qx] = biomeId;
        }

        public int getBiome(int qx, int qy, int qz) {
            return biomes[(qy << 4) | (qz << 2) | qx];
        }

        public int[] getBiomes() {
            return biomes;
        }

        public int getNonAirCount() { return nonAirCount; }
        public int[] getBlocks() { return blocks; }
    }

    public void write(PacketBuffer pb) {
        io.netty.buffer.ByteBuf data = io.netty.buffer.Unpooled.buffer();
        try {
            for (int i = 0; i < sectionCount; i++) {
                Section sec = sections[i];

                // 1) blockCount
                data.writeShort(sec.getNonAirCount());

                // 2) Block States
                if (sec.getNonAirCount() == 0) {
                    data.writeByte(0); // Bits = 0
                    writeVarIntToBuf(data, 0); // Air

                } else {
                    int[] rawBlocks = sec.getBlocks();
                    // 快速路径：先扫描唯一值数量，避免分配 LinkedHashSet（热路径优化）
                    int uniqueCount = countUnique(rawBlocks);

                    // 单值 palette 仅允许"整节 4096 格同为该方块"时使用。
                    // 旧实现 uniqueCount==1 (一种非空方块+空气) 也发单值 → 客户端把
                    // 含薄沙层/零散方块的整节渲染成 16³ 实心方块 (16x16x16 沙块根因)。
                    if (uniqueCount == 1 && sec.getNonAirCount() == 4096) {
                        data.writeByte(0); // Bits = 0
                        writeVarIntToBuf(data, rawBlocks[0]); // 单值直接取第一个
                    } else {
                        // 复杂地形：Direct Palette (15-bit)
                        data.writeByte(15);
                        // rawBlocks 就是 sec 内部的 int[4096]，无需复制（原代码 new int[4096]+循环拷贝是冗余的）
                        long[] packed = BitStorage.pack(15, rawBlocks);
                        for (long l : packed) data.writeLong(l);
                    }
                }

                // 3) Biomes — 64 个 biome 用 palette 编码
                writeBiomePalette(data, sec.getBiomes());
            }

            pb.writeVarInt(data.readableBytes());
            byte[] b = new byte[data.readableBytes()];
            data.readBytes(b);
            pb.writeRawBytes(b);
        } finally {
            data.release();
        }
    }

    /** 快速统计 int[4096] 中唯一值的数量（避免分配 HashSet，热路径优化） */
    private static int countUnique(int[] arr) {
        // 用一个小的 int[] 做线性探测表，覆盖绝大多数 section（通常 < 20 种方块）
        final int TABLE_SIZE = 64; // 必须是 2 的幂
        final int MASK = TABLE_SIZE - 1;
        int[] table = new int[TABLE_SIZE]; // 初始全 0
        int unique = 0;
        boolean overflow = false;

        for (int v : arr) {
            if (v == 0) continue; // air 跳过，0 是默认空值
            int idx = (v * 31) & MASK;
            // 线性探测
            int probe = 0;
            while (probe < TABLE_SIZE) {
                int slot = (idx + probe) & MASK;
                if (table[slot] == 0) {
                    table[slot] = v;
                    unique++;
                    if (unique > 16) { overflow = true; break; } // 超过阈值直接走 direct palette
                    break;
                } else if (table[slot] == v) {
                    break; // 已存在
                }
                probe++;
            }
            if (overflow || unique > 16) return 4096; // 标记为"多值"
        }
        return unique;
    }

    /**
     * 编码一个 section 的 64 个 biome (4×4×4)，对照原版 PalettedContainer(Strategy.createForBiomes)：
     * - uniqueCount==1 → Bits=0 单值；
     * - uniqueCount∈[2,8] → Bits=ceillog2(uniqueCount)∈{1,2,3} 列表调色板 (写 VarInt 数量 + 调色板 + 索引数据)；
     * - uniqueCount>8 → 全局调色板，Bits=7 (biome 注册表位宽)，直接写原始 id，无调色板列表。
     * 原版 biome 数据数组无长度前缀 (writeFixedSizeLongArray)，与本服区块 block state 一致。
     */
    private void writeBiomePalette(ByteBuf data, int[] biomes) {
        int[] vals = new int[64];
        for (int i = 0; i < 64; i++) {
            int v = biomes[i];
            vals[i] = v <= 0 ? BIOME_PLAINS : v; // 未设置(0)按原单值逻辑视为 plains
        }
        java.util.LinkedHashSet<Integer> unique = new java.util.LinkedHashSet<>();
        for (int v : vals) unique.add(v);
        int uniqueCount = unique.size();

        if (uniqueCount == 1) {
            data.writeByte(0); // Bits = 0 单值
            writeVarIntToBuf(data, vals[0]);
            return;
        }
        if (uniqueCount <= 8) {
            int bits = ceillog2(uniqueCount); // ∈ {1,2,3}
            data.writeByte(bits);
            writeVarIntToBuf(data, uniqueCount);
            java.util.Map<Integer, Integer> idxOf = new java.util.HashMap<>();
            int k = 0;
            for (int u : unique) {
                idxOf.put(u, k);
                writeVarIntToBuf(data, u);
                k++;
            }
            int[] indices = new int[64];
            for (int i = 0; i < 64; i++) indices[i] = idxOf.get(vals[i]);
            for (long l : BitStorage.pack(bits, indices)) data.writeLong(l);
        } else {
            // 全局调色板：biome 注册表位宽 = ceillog2(注册表大小) = 7，直接写原始 id
            data.writeByte(7);
            for (long l : BitStorage.pack(7, vals)) data.writeLong(l);
        }
    }

    private static int ceillog2(int n) {
        if (n <= 1) return 0;
        return 32 - Integer.numberOfLeadingZeros(n - 1);
    }

    private void writeVarIntToBuf(ByteBuf buf, int value) {
        while ((value & -128) != 0) { buf.writeByte(value & 127 | 128); value >>>= 7; }
        buf.writeByte(value);
    }
}