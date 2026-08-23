# 生存系统审计 P1：连接 / 进入 / 区块

> 审计对象：自研 MC 1.21.11（Protocol 774）核心 `com.yanrong.server`
> 对照源：`mapping/remapped_server_1.21.11.jar.src/` 反编译源码 + `mapping/deobf-work/remapped_server_1.21.11.jar` javap 真实方法签名
> 审计方式：仅读取源码 + javap 反汇编对照，**未修改任何 .java**
> 结论基准日：2026-08-11

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|--------|------|------|
| 登录状态机 handshake→login→config→play | 已实现 / 正确 | `ConnectionState` 枚举 + `channelRead0` 分发 |
| play 包（Login 0x30 等） | 已实现 / 正确 | 字段顺序经 javap 核对与原版一致 |
| 区块发送（ChunkEncoder + BitStorage） | 已实现 / 正确 | 此前标注的"崩溃坑"（光照 VarInt 前缀、heightmap key、位打包）**当前代码已修复并验证正确** |
| 区块卸载 | 已实现 / 正确 | `handleMove` 按距离卸载 |
| 传送/重生 Game Event 13 | 已实现 / 正确 | 登录、重生、维度切换三处均补发 |
| 玩家位置同步（0x46） | 已实现 / 正确 | 含 deltaMovement 与 INT 位掩码，符合原版 |

**总体判定：P1 关键路径已基本可用，此前已知的"崩溃坑"均已在当前代码中修复。**

---

## 2. 关键文件与行号证据

- `network/ConnectionState.java` — 状态枚举（HANDSHAKE/STATUS/LOGIN/CONFIG/PLAY）
- `network/NetworkHandler.java`（6806 行）：
  - 状态机分发：`channelRead0` 205、`handleHandshake` 224、`handleLogin` 231、`handleConfig` 243、`handleStatus` 250
  - `sendLoginPlay` 6492、`sendInitialChunks` 6602、`sendConfigPackets` 6664
  - 区块流送/卸载：`handleMove` 5085、`broadcastBlockChange` 5159
  - 传送/重生：`teleportPlayer` 2178；Game Event 13 在 **366（重生）/ 4730（维度切换）/ 6550（登录）**
  - 玩家位置：`sendLoginPlay` 6561
- `network/ChunkEncoder.java` — `writeChunkPacket` 8
- `world/chunk/Chunk.java` — `write` 185（section 数据）
- `utils/BitStorage.java` — `pack` 16
- `network/PlayerChunkTracker.java` — **死代码**（见 §4）

---

## 3. 对照原版的关键差异 / 偏差（逐条，含 文件:行号）

### 3.1 ChunkEncoder 区块编码 —— 与原版一致（已知坑已修复）✅

1. **heightmap key 使用 Heightmap.Types 的 id（而非序数）**
   `ChunkEncoder.java:18,22` 写 `writeVarInt(1)`（WORLD_SURFACE）与 `writeVarInt(4)`（MOTION_BLOCKING）。
   经 `javap -c` 反汇编 `Heightmap$Types` 静态块确认：id 与枚举声明序一致（WORLD_SURFACE_WG=0、WORLD_SURFACE=1、OCEAN_FLOOR_WG=2、OCEAN_FLOOR=3、MOTION_BLOCKING=4、MOTION_BLOCKING_NO_LEAVES=5），故 1/4 正确。
   原版 `ClientboundLevelChunkPacketData.HEIGHTMAPS_STREAM_CODEC` = `ByteBufCodecs.map(EnumMap, Types.STREAM_CODEC, LONG_ARRAY)`，即 `VarInt(count) + 每 entry(VarInt id + LONG_ARRAY)`；项目编码（`:12-24`）与之完全一致。

2. **每个 2048 字节光照数组前有 VarInt(2048) 长度前缀**
   `ChunkEncoder.java:44` `pb.writeVarInt(2048); pb.writeRawBytes(skyLight);`
   对应原版 `ClientboundLightUpdatePacketData` 中 `DATA_LAYER_STREAM_CODEC = ByteBufCodecs.byteArray(2048)`（写 `writeVarInt(2048)+bytes`）。**此前崩溃坑已修复。**

3. **位打包与原版 SimpleBitStorage 一致**
   `BitStorage.pack(15, 4096)`（`BitStorage.java:16`）：`valuesPerLong = 64/15 = 4`，`longCount = ceil(4096/4) = 1024`，LSB-first。
   原版 `SimpleBitStorage.java:131,138`：`valuesPerLong = (char)(64/bits) = 4`，`j = (size + valuesPerLong - 1)/valuesPerLong = 1024`，且 `get/set` 均为 LSB-first。二者一致。

4. **区块 section 数据编码与原版 PalettedContainer 一致**
   `Chunk.write`（`:191-234`）：写 `blockCount(short)`，block states 用 `bits=0`（单值 → `writeByte(0)+writeVarInt(value)`）或 `bits=15`（直接调色板 → 无数据数组长度前缀，直接写 1024 个 long）。
   原版 `PalettedContainer$Data.write`（`javap -c` 确认）：`writeByte(bits)` → `palette.write`（SingleValuePalette 写 VarInt 单值；GlobalPalette.write 为空）→ `writeFixedSizeLongArray(storage.getRaw())`（**无长度前缀**，`javap -c` 确认只写 `writeLong`）。
   故项目对 bits=0/15 的编码与原版逐字节一致。**此前"数据数组长度前缀"误区已正确规避。**

5. **光照掩码顺序与格式**
   `ChunkEncoder.java:30-35` 4 个 `writeBitSet`（对应 `BitSet` → `VarInt(count)+longs`），顺序 `skyYMask / blockYMask / emptySkyYMask / emptyBlockYMask`，与原版 `ClientboundLightUpdatePacketData.write`（`:54-58`）一致。 skyYMask=全置位、blockYMask=空、emptySkyYMask=空、emptyBlockYMask=全置位 → 客户端收到全部天空光（全 0xFF/0x00）+ 无方块光，属"过亮但可玩、不崩溃"的合理近似。

### 3.2 Login 包（0x30）—— 与原版一致 ✅

经 `javap -c ClientboundLoginPacket.write` 确认字段顺序：
`writeInt(entityId) → writeBoolean(hardcore) → writeCollection(worldNames) → writeVarInt(maxPlayers) → writeVarInt(viewDistance) → writeVarInt(simulationDistance) → writeBoolean(reducedDebugInfo) → writeBoolean(enableRespawnScreen) → writeBoolean(doLimitedCrafting) → CommonPlayerSpawnInfo.write → writeBoolean(enforcesSecureChat)`。

项目 `sendLoginPlay`（`:6498-6524`）逐字段匹配，含 `CommonPlayerSpawnInfo`（dimensionType id + dimension name + hashedSeed(long) + gamemode(byte) + previousGamemode(byte) + isDebug + isFlat + deathLocation(optional) + portalCooldown(varint) + seaLevel(varint)）。**结构无误，登录不会因字段错位崩溃。**

### 3.3 Player Position 包（0x46）—— 与原版一致 ✅

`PositionMoveRotation` 含 `position(Vec3)` + `deltaMovement(Vec3)` + `yRot/xRot(float)`；`Set<Relative>` 经 `javap -c Relative` 确认用 `ByteBufCodecs.INT`（4 字节位掩码）。
项目 `teleportPlayer`（`:2181`）/`sendLoginPlay`（`:6561`）写 `VarInt(id) + 3 double + 3 double(delta=0) + 2 float + writeInt(0)`，`writeInt(0)` 的 4 字节恰与原版 INT 位掩码一致。

### 3.4 偏差（非崩溃，轻微）

- **biome 单值化**：`Chunk.java:228-232` 每 section 仅取 `(0,0,0)` 处一个 biome，`bits=0` 单值写出。原版可对 4×4×4=64 个 biome 用调色板。→ 区块内 biome 变化丢失（草/叶色、温度、群系相关行为在子区块粒度可能不准）。线格式有效但不精确。
- **heightmap 仅发 2 个**：`ChunkEncoder.java:12` 仅 WORLD_SURFACE(1) 与 MOTION_BLOCKING(4)。原版 `sendToClient` 的 heightmap 主要也是这两个，可接受。
- **handleMove 每次跨块发送 ChunkBatchStart/Finished（见 §4 中等项）**。

---

## 4. Bug 清单（严重程度 + 文件:行号 + 现象 + 修复方向；只记录不修）

### 4.1 [中等] 每次跨区块边界都发送 ChunkBatchStart（0x0C）
- **文件:行号**：`NetworkHandler.java:5090`（发送 0x0C）、`:5118`（发送 0x0B ChunkBatchFinished），位于 `handleMove`
- **现象**：原版仅在"强制加载"（登录 / 重生 / 维度切换）时成对发送 `ChunkBatchStart`/`ChunkBatchFinished` 来驱动"加载地形"覆盖层与批处理计数；正常移动时只流式发 `LevelChunkWithLight`，不发送这对包。项目在每次跨块都发送，可能使客户端批处理计数/加载覆盖层状态异常（例如加载进度条抖动、或覆盖层在不应出现时出现）。
- **建议修复方向**：把 `0x0C`/`0x0B` 的发送从 `handleMove` 移除，仅保留在 `sendInitialChunks`（已正确发送于 `:6607`/`:6653`）。`handleMove` 只发 `SetChunkCacheCenter`(0x5C) + 增量区块，不发批次开始/结束。

### 4.2 [轻微] biome 单值化丢失区块内群系变化
- **文件:行号**：`Chunk.java:228-232`
- **现象**：草色/树叶色/温度依赖等按子区块粒度不准。
- **建议修复方向**：按原版对 64 个 biome 用 palette（`bits>0` 时写 palette 长度 + 调色板 + 数据数组）编码；或至少按列/按 4×4×4 采样多 biome。

### 4.3 [信息/清理] PlayerChunkTracker.java 为死代码
- **文件:行号**：`network/PlayerChunkTracker.java`（整文件，含 `viewDistance=8` 与 `ChunkSender` 接口）—— 经全仓 grep 无任何 `new PlayerChunkTracker` / `.update(` 调用点。
- **现象**：真实的区块追踪由 `NetworkHandler.loadedChunks`（`NetworkHandler.java:83`）实例字段 + `handleMove`（`:5085-5128`）内联逻辑实现。该独立类与实际逻辑重复且从未被使用，易造成维护混淆。
- **建议修复方向**：删除 `PlayerChunkTracker.java`，或在 `handleMove` 中复用它（需把 `sendChunk/unloadChunk` 接到 `WorldManager`/`ChunkEncoder` 与 `0x25` 卸载包）。

---

## 5. 结论与优先级建议

- **P1 关键路径当前实现基本正确**。此前审计/团队标注的三处"已知崩溃坑"——（a）每个 2048 字节光照数组前缺 VarInt(2048) 前缀、（b）heightmap key 误用序数、（c）区块 block state 位打包顺序/长度错误——**经本次对当前代码的逐字节核对，均已在代码中正确实现**，不再构成崩溃风险。
- **首要修复项**：§4.1 的 `ChunkBatchStart` 在移动时重复发送（中等），建议优先处理以防客户端加载覆盖层/批计数异常。
- **次要点**：§4.2 biome 单值化（轻微，影响观感与群系相关行为）、§4.3 死代码清理。
- 区块卸载（§3 中 `handleMove` 卸载 `VIEW_DISTANCE+2` 外区块并仅广播给已加载该区块的玩家）、`broadcastBlockChange`(0x08) 格式、`Game Event 13` 三处补发，均经核对正确。

**优先级：P1-4.1（中等）> P1-4.2（轻微）> P1-4.3（清理）。**

---

## 6. 补充发现（general-purpose-12 增补，2026-08-11 复核）

> 以下为对当前代码二次复核后**新增**的条目（文档既有 §3/§4 结论经逐项核对维持有效）。
> 复核手段：对照 `mapping/cfr-source/net/minecraft/network/protocol/...` 反编译源码 + `javap` 真实签名 + 解析 `dumped_registries/reg_*.bin` 真实下发内容。

### 6.1 [P1] 下界 dimension_type 注册索引错配 —— 下界进入/传送使用错误维度类型
- **Bug ID**：P1-B5
- **File:Line**：
  - `../src/main/java/com/CharunCore/server/world/DimensionType.java:5`（`THE_NETHER(..., registryId=1, ...)` → registryId 硬编码为 **1**）
  - `dumped_registries/reg_14.bin`（config 阶段经 `RegistryHelper.sendAllDumpedRegistries` 作为 `minecraft:dimension_type` 下发，解析得顺序：`overworld(0) / overworld_caves(1) / the_end(2) / the_nether(3)`）
  - 引用点：`NetworkHandler.java:6512`（Login 0x30 的 `CommonPlayerSpawnInfo` 用 `currentDim.registryId`）、`:4712`（Respawn 0x50 用 `respawnDim.registryId`）
- **原版行为**：`CommonPlayerSpawnInfo` 的 `dimensionType` 经 `DimensionType.STREAM_CODEC`（= `ByteBufCodecs.holderRegistry(Registries.DIMENSION_TYPE)`，VarInt 注册索引）编码。config 阶段下发的 `minecraft:dimension_type` 注册表会在客户端**整体替换**为其条目顺序（见 `ClientboundRegistryDataPacket` → `WritableRegistry.bind`）。原版/本服下发顺序为 `overworld=0, overworld_caves=1, the_end=2, the_nether=3`，故 `the_nether` 真实索引 = **3**。
- **当前实现**：`DimensionType.THE_NETHER.registryId` 硬编码为 **1**，与 `overworld_caves`(1) 冲突。
- **证据片段**：
  ```
  // DimensionType.java:4-6
  OVERWORLD(-64, 384, 63, true,  false, 1.0, 0, 0, "minecraft:overworld"),
  THE_NETHER(0,   256, 31, false, true, 8.0, 1, 3, "minecraft:the_nether"),
  THE_END(0,   256, 0,  true, false, 1.0, 2, 2, "minecraft:the_end");

  // reg_14.bin 解析（minecraft:dimension_type, count=4）
  [0]=minecraft:overworld  [1]=minecraft:overworld_caves
  [2]=minecraft:the_end    [3]=minecraft:the_nether
  ```
- **影响**：玩家进入/传送到下界时，SpawnInfo 的 `dimensionType=1` 被客户端解析为 **`overworld_caves`**（minY/height/seaLevel/是否有天空/雾等维度属性全部按 overworld_caves 解释）→ 下界世界表现错误（可能显示天空、错误高度范围、出生 Y 错位、群系/光照异常）。**不影响主世界首次进入**（`registryId=0=overworld` 正确）。属"能进但维度错误"的重大破坏（P1）。
- **建议修复方向**：将 `THE_NETHER.registryId` 改为 **3**，与 `reg_14.bin`/原版 `dimension_type` 注册顺序一致（同时复核 `OVERWORLD=0`、`THE_END=2` 已正确）；更稳妥做法是让 `registryId` 直接由 `dumped_registries/reg_14.bin` 的条目顺序驱动，避免硬编码漂移。

### 6.2 [P1] 服务端实际视距 12 与 Login 声明视距 8 不一致 —— 客户端/服务端区块集合不同步
- **Bug ID**：P1-B6
- **File:Line**：
  - `NetworkHandler.java:85`（`private final int VIEW_DISTANCE = 12;`）
  - `NetworkHandler.java:6506`（`writeVarInt(8) // viewDistance`）、`:6507`（`writeVarInt(8) // simulationDistance`）
  - 全仓 `grep 0x5D / SetChunkCacheRadius / 0x6D / SetSimulationDistance`：**无任何发送点**（客户端只能以 Login 声明的 8 为准）
- **原版行为**：Login(0x30) 的 `viewDistance`/`simulationDistance` 告知客户端 `ClientChunkCache` 半径与实体模拟半径；服务端应按该半径推送/卸载区块。若两端半径不一致，客户端会按 8 卸载、服务端按 12 保留，造成 8~12 环带区块"服务端认为已加载、客户端已卸载"。
- **当前实现**：服务端 `handleMove` / `sendInitialChunks` 均按 `VIEW_DISTANCE=12` 推送并仅在 >12+2 才卸载；但 Login 仅声明 8，且从未补发 `SetChunkCacheRadius(0x5D)` / `SetSimulationDistance(0x6D)`。
- **证据片段**：`:6506-6507` 与 `:85` 直接矛盾；`handleMove` `:5093`/`:5121` 用 `VIEW_DISTANCE`；无 `0x5D`/`0x6D` 发送。
- **影响**：半径 8~12 区域地形在客户端缺失/随移动出现空洞与闪烁（移动时 `handleMove` 因 `loadedChunks` 已含这些 key 不会重发，但客户端已按 8 卸载 → 持续空洞）；`simulationDistance` 不一致影响实体/怪物模拟范围。属会破坏"收到并正确解码区块"体验的重大问题（P1）。
- **建议修复方向**：让 Login 声明的 `viewDistance`/`simulationDistance` 等于实际 `VIEW_DISTANCE`（12），或将 `VIEW_DISTANCE` 改为 8；或在进入后补发 `SetChunkCacheRadius(0x5D)`/`SetSimulationDistance(0x6D)` 使两端一致。

### 6.3 [P2] ChunkBatchFinished(0x0B) 早于批次内其余区块出队 —— 协议顺序错误
- **Bug ID**：P1-B7
- **File:Line**：
  - `NetworkHandler.java:6607`（`sendInitialChunks` 内 `sendPacket(ctx, 0x0C, ...)` ChunkBatchStart）
  - `:6640-6643`（各 0x2C 区块经 `getIoExecutor().execute(...)` 再 `ctx.executor().execute(sendPacket 0x2C)` 入队）
  - `:6651-6656`（`ctx.executor().execute(() -> sendPacket(ctx, 0x0B, ...))` ChunkBatchFinished **立即入队**）
  - 移动路径 `:5090`/`:5118` 同理
- **原版行为**：普通批次中 `ChunkBatchFinished` 应在批次内**全部** `LevelChunkWithLight` 发送完成后才发出（客户端用其计数关闭加载遮罩/推进进度）。
- **当前实现**：事件循环为 FIFO，`0x0B` 任务在 `:6651` 同步入队，而 `0x2C` 任务需等 IO 线程完成之后才入队 → `0x0B` 先于其余 `0x2C` 出队。**（注：中心区块 `:6617` 为同步 `sendPacket`，与 `0x0B` 的相对顺序正确，故初始加载遮罩通常仍能关闭。）**
- **影响**：加载进度/批次计数与实际到达不符，属协议顺序错误。通常不阻断进入（中心块已同步、GameEvent13 驱动遮罩），但为 §4.1 的延伸问题，建议一并修。
- **建议修复方向**：在所有 `0x2C` 真正发送完成后再发送 `0x0B`（用 `CompletableFuture` 聚合或"已发送计数归零"回调），或将 `0x0B` 的发送挂到最后一个 `0x2C` 的回调里。

### 6.4 [P3] biome 注册表 65 项 vs 服务端映射 64 项 —— 第 65 个 biome 不可达（轻微）
- **Bug ID**：P1-B8
- **File:Line**：`dumped_registries/reg_1.bin`（`minecraft:worldgen/biome`, count=**65**）vs `RegistryHelper.java:324-342` `biomeIdToName` 数组（**64** 项）
- **原版行为**：biome 注册表下发 65 项，客户端按索引建表；`worldgen/biome` 注册键名正确（`minecraft:worldgen/biome`，已核对）。
- **当前实现**：服务端世界生成仅用前 64 个 biome 名称（`biomeIdToName`），第 65 个 biome 在客户端存在但服务端从不引用。
- **影响**：极轻微。服务端分配的 biome id `0..63` 均在客户端 `0..64` 有效范围内，**无越界**；仅第 65 个 biome（服务端映射缺失）不可达。区块 biome 为单值编码（§4.2）已限制精度，此条为边缘群系表一致性问题。
- **建议修复方向**：让 `biomeIdToName` 与 `reg_1.bin` 的顺序/数量完全一致（补齐第 65 项），或统一由同一数据源生成 biome 表，避免将来新增 biome 时错位。

---

## 7. 复核 PASS 增补清单（确认正确的项）

以下项在二次复核中对照反编译源码/`javap`/`.bin` 解析确认**正确**，补充进 PASS 清单：

- **P1 config 状态包号全部正确**：对照 `mapping/cfr-source/.../configuration/ConfigurationProtocols.java` 的 `CLIENTBOUND_TEMPLATE` 注册顺序，本服使用的 ID 与权威一致：
  - `0x07` = Registry Data（`RegistryHelper` 用 0x07，✓）
  - `0x03` = Finish Configuration（✓）
  - `0x0D` = Update Tags（`sendConfigPackets` 发 `tags.bin`，✓）
  - `0x0E` = Select Known Packs（`sendConfigPackets` 发 `minecraft:core/1.21.11`，✓，且位于 registry/tags 之前，顺序正确）
- **P1 0x25 Forget Level Chunk 格式正确**：原版 `ClientboundForgetLevelChunkPacket` 经 `writeChunkPos` = 两个 `Int`；本服 `:5122` `writeInt(cx); writeInt(cz)` 正确（非 VarInt）。
- **P1 0x46 Player Position relative 掩码为 INT（4 字节）**：`Relative.SET_STREAM_CODEC = ByteBufCodecs.INT.map(...)`（见 `Relative.java:114`），本服 `writeInt(0)` 正确；1.21.11 该包**无**尾部 `dismountVehicle` 布尔（`ClientboundPlayerPositionPacket` 仅 `VAR_INT id + PositionMoveRotation + Relative.SET`），本服未写该布尔，正确。
- **P1 区块 heightmap long 数组长度自洽**：`ChunkEncoder.packHeightmap` 计算 `longCount=(256*9+63)/64=36`，与原版 `roundUp(256*9,64)/64=36` 一致；`LONG_ARRAY` 以 VarInt(36) 自描述长度，客户端按长度读，无错位。
- **P1 入站分帧正确**：`MinecraftFrameDecoder` 使用 VarInt 长度前缀分帧（`decode` 读 VarInt length 再 `readRetainedSlice`），与 MC 协议一致；Main `:144` 管线 `MinecraftFrameDecoder → NetworkHandler` 正确，绑定 `:25565`。
- **P1 登录流程状态机正确**：handshake→login(0x00 读用户名+离线 UUID→0x02 Login Success)→login(0x03 Login Acknowledged)→config→play 转移路径与包号符合 774。

**新增优先级（在原有之上）**：P1-B5（下界维度类型错配）与 P1-B6（视距声明不一致）为本次复核发现的**重大（P1）**互操作缺陷，建议优先于 §4.1 处理；P1-B7（批次结束顺序）为中，P1-B8（biome 表计数）为轻。

**综合判定**：P1 关键路径的"握手/状态/登录/配置/Login/区块解码/出生点（主世界）"在包号与字段布局层面经核对**基本互操作正确**；但新增的 **P1-B5（下界维度类型）、P1-B6（视距错配）** 会在"进入下界"与"半径 8~12 区块完整性"上破坏体验，应在放行前修复。
