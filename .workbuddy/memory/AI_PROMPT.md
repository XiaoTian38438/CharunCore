# mc-server-core1.21.11 — AI 接手提示词

## 项目概况

- **项目**: 自定义 Minecraft Java Edition 服务端核心
- **版本**: 1.21.11 (Protocol 即将确认)
- **技术栈**: Java 21 + Netty (纯手写, 不使用 Paper/Spigot/NMS)
- **世界高度**: -64 ~ 319, 共 384 格, 即 **24 个 Section** (每个 Section 16 格高)
- **基线规则** (红字):
  1. **除用户明确要求, 绝不可修改** `Chunk.java`/`ChunkEncoder.java`/`BitStorage.java`/`Netty 底层` 等核心文件。
  2. **绝不要重构/优化未提及的代码。**
  3. **严格遵循 `data/protocol.json` 中的协议定义。**

---

## 协议核心定义 (来自 `data/protocol.json`)

### 1. packet_map_chunk (服务端 -> 客户端)

ID: `0x2C` (map_chunk)

```json
"packet_map_chunk": [
  "container", [
    { "name": "x", "type": "i32" },
    { "name": "z", "type": "i32" },
    { "name": "heightmaps", "type": ["array", { "countType": "varint", "type": ["container", [
      { "name": "type", "type": ["mapper", { "type": "varint", "mappings": {
        "0": "world_surface_wg", "1": "world_surface",
        "2": "ocean_floor_wg", "3": "ocean_floor",
        "4": "motion_blocking", "5": "motion_blocking_no_leaves"
      }}]},
      { "name": "data", "type": ["array", { "countType": "varint", "type": "i64" }]}
    ]}]},
    { "name": "chunkData", "type": "ByteArray" },
    { "name": "blockEntities", "type": ["array", { "countType": "varint", "type": "chunkBlockEntity" }]}
    { "name": "skyLightMask", "type": ["array", { "countType": "varint", "type": "i64" }]}
    { "name": "blockLightMask", "type": ["array", { "countType": "varint", "type": "i64" }]}
    { "name": "emptySkyLightMask", "type": ["array", { "countType": "varint", "type": "i64" }]}
    { "name": "emptyBlockLightMask", "type": ["array", { "countType": "varint", "type": "i64" }]}
    { "name": "skyLight", "type": ["array", { "countType": "varint", "type": ["array", { "countType": "varint", "type": "u8" }] }]}
    { "name": "blockLight", "type": ["array", { "countType": "varint", "type": ["array", { "countType": "varint", "type": "u8" }] }]}
  ]
]
```

**关键总结**: `heightmaps` 和 `light masks` 都是 `varint count` + `count * i64` 的数组格式。light data 是嵌套数组。

### 2. PalettedContainer 格式

每个 Section:
1. `non-air count` (short)
2. Block States (Paletted Container)
3. Biomes (Paletted Container)

**Block States 格式**:
- bits (byte) - 0 表示单值模式
- 如果 bits == 0: varint stateId
- 否则: paletteSize (varint) + paletteSize * varint stateId + dataLen (varint) + dataLen * i64 packed data

---

## 核心代码状态

### `Chunk.java`
- 坐标顺序: `(ry << 8) | (rz << 4) | rx` (Mojang yzx 顺序)
- `write()` 方法: bits 硬编码为 15 (直接模式)
- 问题: 直接模式下地形显示为“混乱的条纹方块”

### `BitStorage.java`
- `pack(bits, data)` 方法已实现
- `valuesPerLong = 64 / bits`
- 打包逻辑: `res[longIndex] = (res[longIndex] & ~(mask << bitOffset)) | ((data[i] & mask) << bitOffset)`

### `ChunkEncoder.java`
- heightmaps: 数组格式, 2 个 heightmap (motion_blocking=4, world_surface=1)
- light masks: `varint(1) + long(mask)` 格式
- sky light: 24 个数组, block light: 0 个数组

---

## 历史修复记录 (失败案例)

1. **heightmaps NBT 格式错误**: 最初使用 `writeNbt()` 发送 NBT Compound, 客户端报错。已改为 varint + i64 数组格式。
2. **light mask BitSet 格式错误**: 最初使用 `writeBitSet()`, 已改为 varint + long 格式。
3. **light array 数量错误**: 最初发送 26 个数组, 已改为 24 个。
4. **Extra bytes 错误**: 曾添加 `writeVarInt(0)` 导致客户端报 "found 2 bytes extra", 已移除。
5. **"Missing Palette entry for index 8"**: 使用调色盘模式时, 索引超出 palette 范围。错误可能出在 `BitStorage.pack()` 或 `Chunk.write()` 的索引导入逻辑。
6. **"混乱的条纹方块"**: 使用直接模式 (bits=15) 时地形渲染错误。可能原因: stateId 与实际方块映射错误, 或数据打包/解包时 index 顺序不匹配。

---

## 绝对不可做的事项 ❌

1. **绝不要修改以下文件** (除非用户明确要求): `Chunk.java` (但用户后来允许修改了), `ChunkEncoder.java`, `BitStorage.java`, Netty 底层
2. **绝不要重构/优化未提及的代码**
3. **绝不要在协议格式上猜测或添加额外字节**, 必须严格遵循 `data/protocol.json`
4. **绝不要假设客户端会忽略错误**, 1.21.11 客户端对协议格式非常严格
5. **发送光照数据时**, 确保 sky light 数量与 skyLightMask 中置位的 bit 数量一致

---

## 调试建议

1. **使用 Minecraft 客户端配合 `System.out.println` 或断点** 检查 `chunkData` 字节数组的内容
2. **对比参考**: 使用像 Minestom 这样的开源服务端, 抓包比较 `map_chunk` 包的二进制数据
3. **分步验证**: 先确保单个 Section (全空气) 能正确渲染, 再逐步添加有方块 Section
4. **检查 stateId**: 确保使用的是**状态 ID** 而非方块 ID。bedrock 的默认状态 ID 可能不是 0

---

## 待完成任务

1. **修复 chunk 数据包发送**: 解决客户端崩溃 `readerIndex exceeds writerIndex` 或 `Missing Palette entry` 错误
2. **修复地形渲染**: 确保超平坦地形正确渲染为 bedrock/dirt/grass, 而非混乱条纹
3. **验证所有已实现功能**: 装备同步、流体、红石、死亡重生、PvP、聊天、箱子、活塞、TNT 在修复 chunk 包后是否仍能正常工作

---

## 代码快速参考

### Chunk.java write() 方法关键逻辑 (当前):

```java
import com.CharunCore.server.utils.BitStorage;for(int i = 0;
i< 24;i++){
Section section = sections[i];
    data.

writeShort(section.getNonAirCount());

// Block States - 动态调色盘 (当前 bits 硬编码为 15)
int[] blocks = section.getBlocks();
// ... 收集 unique state IDs ...

    if(unique.

size() ==1){
        // 单值模式 (0-bit)
        data.

writeByte(0);

writeVarInt(data, unique.iterator().

next());
        }else{
        // bits 硬编码为 15 (直接模式)
        data.

writeByte(15);

long[] packed = BitStorage.pack(15, blocks);

writeVarInt(data, packed.length);
        for(
long l :packed)data.

writeLong(l);
    }

            // Biomes - 单值模式 (Plains = 39)
            data.

writeByte(0);

writeVarInt(data, 39);
}
```

### ChunkEncoder.java 关键逻辑 (当前):
```java
// heightmaps: 2 个
pb.writeVarInt(2);
// motion_blocking
pb.writeVarInt(4);
pb.writeVarInt(37);
for (long l : heightmapData) pb.writeLong(l);
// world_surface
pb.writeVarInt(1);
pb.writeVarInt(37);
for (long l : heightmapData) pb.writeLong(l);

chunk.write(pb);

pb.writeVarInt(0); // blockEntities

// light masks
long fullMask = (1L << 24) - 1L;
pb.writeVarInt(1); pb.writeLong(fullMask);     // skyLightMask
pb.writeVarInt(1); pb.writeLong(0L);            // blockLightMask
pb.writeVarInt(1); pb.writeLong(0L);            // emptySkyLightMask
pb.writeVarInt(1); pb.writeLong(0L);            // emptyBlockLightMask

// sky light: 24 个数组
pb.writeVarInt(24);
byte[] lightData = new byte[2048];
Arrays.fill(lightData, (byte) 0xFF);
for (int i = 0; i < 24; i++) pb.writeByteArray(lightData);

// block light: 0 个数组
pb.writeVarInt(0);
```

---

## 总结

这是一个高度复杂的底层 Minecraft 服务端实现项目。核心问题始终围绕着 **Chunk 数据包 (`0x2C map_chunk`) 的格式正确性**。历史上经历了多次协议格式调整、BitStorage 打包逻辑修正、光照数据修正, 但客户端仍存在渲染问题。接手者需要具备:

1. 深入理解 Minecraft 1.21 区块数据格式 (PalettedContainer)
2. 能进行二进制数据对比分析 (抓包/二进制 diff)
3. 高度严谨的协议字段序列化能力
4. 耐心 — 这是一个迭代调试的过程, 每次修改后都需要验证客户端行为

**祝好运, 并请务必遵守"不可修改未授权文件"的红线规则!**
