# P5 维度与传送门 审计报告（主世界 / 下界 / 末地）

审计对象：自研 MC 1.21.11（Protocol 774）核心，包名 `com.yanrong.server`
对照源：mapping/remapped_server_1.21.11.jar.src（DimensionType 反编译失败、PortalForcer.java）、json/1.21.11/protocol.json（packet_respawn / packet_position / packet_update_time / packet_game_state_change / SpawnInfo）、wiki.vg 协议
审计日期：2026-08-11
铁律：仅读取源码 + 写报告，未修改任何 .java。

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|--------|------|------|
| 三维定义 DimensionType | 部分实现 | 含 minY/height/seaLevel/hasSkylight/hasCeiling/coordinateScale/id/registryId/key；**缺失** ambientLight/ultrawarm/fixedTime/infiniburn/piglinSafe/bedWorks/respawnAnchorWorks/effects 等 |
| 维度管理与切换（WorldManager） | 已实现 | 每维度独立区块存储（dimChunks）、独立生成器、get/setBlock 维度重载 |
| 下界传送门（扫描/点燃/传送/建返回门） | 已实现 | scanPortalBlock + tryIgniteNetherPortal + teleportToDimension(build_return_portal) |
| 末地传送门（激活/传送/返回/末地岛） | 已实现 | tryActivateEndPortal + end_portal 传送 + teleportViaEndGateway |
| 维度切换发包序列（0x50/0x46/0x26/0x6F） | 已实现（已知坑已修） | 发送 Respawn + Position + GameEvent13 + SetTime，包格式经 protocol.json 核对正确 |

整体：维度切换与传送门链路**可用**，已知的“漏发包导致加载界面卡死”坑已在当前代码中修复（见 `NetworkHandler.java:4725-4732` 注释）。主要偏差集中在维度元数据字段缺失与 SetTime 硬编码。

---

## 2. 关键文件与行号证据

### 2.1 维度定义
- `world/DimensionType.java:3-6` — 三维枚举：OVERWORLD(-64,384,63,sky,noCeil,scale1,id0,reg0)、THE_NETHER(0,256,31,noSky,ceil,scale8,id1,reg3)、THE_END(0,256,0,sky,noCeil,scale1,id2,reg2)。
- `world/DimensionType.java:8-22` — 字段集合（无 ambientLight/ultrawarm/fixedTime/infiniburn/piglinSafe/bedWorks/respawnAnchorWorks/effects/logicalHeight/monsterSpawnLight）。
- `world/DimensionType.java:38-59` — `byId/byKey/getFolderName/getRegionDir`（DIM-1 / DIM1）。

### 2.2 维度管理（WorldManager）
- `world/WorldManager.java:31` — `dimChunks`：维度 → 区块表。
- `world/WorldManager.java:124-131` — 初始化：`overworldGenerator/netherGenerator/endGenerator` 三个生成器。
- `world/WorldManager.java:413-434` — `getChunk(DimensionType,...)`：每维度加载/生成/缓存。
- `world/WorldManager.java:458-467` — `setBlock(DimensionType,...)` + 放置 spawner 时注册 `SpawnerSystem`。
- `world/WorldManager.java:469-472` — `getBlockState(DimensionType,...)`。
- `world/WorldManager.java:449-452` — `isChunkCached(DimensionType,key)`（被 `EntityManager` 刷怪调用）。

### 2.3 维度切换与发包（NetworkHandler）
- `network/NetworkHandler.java:4632-4736` — `teleportToDimension(...)`：
  - `:4643-4658` 坐标缩放（下界↔主世界 1/8 与 8）。
  - `:4664-4668` 进入末地时建黑曜石平台 `EndDragonFight.createObsidianPlatform()`。
  - `:4671-4683` 发送 **0x50 respawn（SpawnInfo）**。
  - `:4685-4693` 设置 `currentDim/坐标/portalTimer=-300`。
  - `:4695-4709` 建/复用返回门并落点门内。
  - `:4711-4723` 发送 **0x46 position（Player Position）**。
  - `:4730` 发送 **0x26 game_state_change(event=13)**（已修复的已知坑）。
  - `:4732` 发送 **0x6F update_time**（硬编码 0 / 6000）。
- `network/NetworkHandler.java:3520-3537` — `scanPortalBlock()`：扫描玩家 AABB 内 nether_portal/end_portal/end_gateway。
- `network/NetworkHandler.java:3552-3589` — `tickSurvival` 内传送门触发（nether/end 累计 80 tick≈4s；gateway 瞬时）。
- `network/NetworkHandler.java:4837-4855` — `findNearbyPortal()`：半径 32 立方、dy ±16 搜已有 nether_portal。
- `network/NetworkHandler.java:4857-4880` — `buildNetherPortalFrame()`：4×5 黑曜石框 + 点火（axis=x）。
- `network/NetworkHandler.java:4882-4908` — `findSafeArrivalY()`：找安全落点，必要时建平台。
- `network/NetworkHandler.java:5568-5632` — `tryIgniteNetherPortal()`：校验黑曜石框（宽 2~21、高 3~21）并点火。
- `network/NetworkHandler.java:5838-5868` — `tryActivateEndPortal()`：区域累计 12 个带眼末地门框即激活。
- `network/NetworkHandler.java:4743-4784` — `teleportViaEndGateway()`：主岛↔外岛网关传送。

### 2.4 发包格式核对（json/1.21.11/protocol.json）
- 包 ID：`0x50=respawn`、`0x46=position`、`0x26=game_state_change`、`0x6F=update_time`（`protocol.json:10052,10062,10093`）。
- `packet_respawn` = SpawnInfo + copyMetadata(u8)（`protocol.json:8419-8431`）。
- `SpawnInfo` 字段顺序（`:5960-6017`）：`dimension(varint) → name(string) → hashedSeed(i64) → gamemode(i8 mapper) → previousGamemode(u8) → isDebug(bool) → isFlat(bool) → death(option GlobalPos) → portalCooldown(varint) → seaLevel(varint)`。与 `NetworkHandler.java:4671-4683` 顺序完全一致 ✅。
- `packet_position` 末字段 `flags` 类型为 `PositionUpdateRelatives`（底层 **u32**，`protocol.json:8150-8165`），与 `writeInt(0)` 的 4 字节一致 ✅。
- `packet_update_time` = `age(i64) → time(i64) → tickDayTime(bool)`（`protocol.json:9131-9146`），与 `writeLong(0); writeLong(6000); writeBoolean(true)` 一致 ✅。
- `game_state_change` = `event(u8) + value(f32)`，与 `writeByte(13); writeFloat(0.0f)` 一致 ✅。

---

## 3. 对照原版的关键差异 / 偏差（逐条）

1. **DimensionType 元数据字段缺失**（对照原版 `DimensionType` codec）
   - 原版维度定义含：`fixedTime`(末地=18000 固定夜晚)、`ultrawarm`(下界=true)、`natural`、`coordinateScale`、`bedWorks`/`respawnAnchorWorks`、`minY`/`height`/`logicalHeight`、`infiniburn`(下界=lava)、`effects`(天空/天气)、`ambientLight`(下界=0.1)、`piglinSafe`(下界=true) 等。
   - 本项目仅存 minY/height/seaLevel/hasSkylight/hasCeiling/coordinateScale/id/registryId/key（`:8-22`）。
   - 影响：客户端实际看到的维度元数据来自**登录阶段下发的注册表**（`dumped_registries/reg_14.bin`），若该注册表未正确含 `ambient_light`/`ultrawarm`/`piglinSafe`/`fixed_time` 等，则下界“不温暖（无防火特性视觉）、末地时间不锁夜、猪灵不中立”等客户端/行为异常。**需单独核对注册表是否完整**（本项目内部 `DimensionType` 不强制这些字段）。
   - 关键后果：`fixedTime` 缺失 → 末地时间无法锁定为 18000（见 Bug P5-1）。

2. **SetTime 硬编码**（对照 `ServerLevel` 发送 `level.getGameTime()/getDayTime()`）
   - 维度切换后 `:4732` 固定 `writeLong(0); writeLong(6000)`（世界龄=0、时刻=6000/正午），**未使用 `Main.dayTime`/`Main.worldAge`**。原版 respawn 发送真实世界时间。

3. **坐标缩放用 int 截断**（对照原版 `PortalForcer` 保留门内偏移）
   - `:4651-4652` `targetX=(int)(this.x*scale)` 丢失小数部分；原版在目标维度找到已有门后按门内相对偏移定位，不会丢失精度（对玩法影响轻微）。

4. **返回门查找半径/方式简化**（对照 `PortalForcer.findClosestPortalPosition` 用 POI 半径 128/16）
   - 本项目 `findNearbyPortal` 仅 `dy±16`、半径 32 立方体暴力扫描（`:4837-4855`）；原版基于 POI（下界半径 16、主世界半径 128）。可能在主世界远处已有门下仍误建新门。

5. **末地返回主世界坐标固定**（对照原版返回玩家重生点/出口传送门）
   - `:4655-4657` 末地→主世界硬编码 (8,8)；原版返回玩家 `RespawnData`/出口传送门位置（通常为出生点附近）。

6. **末地门激活判定宽松**（对照原版严格 3×3 框架 + 12 眼校验）
   - `tryActivateEndPortal`（`:5838-5868`）在 cx±4/cz±4 大区域内累计 `eye=true` 的末地门框数，`bestCount>=12` 即激活；未校验这些框是否构成完整 3×3 开口 + 正确朝向，存在误激活可能（轻微）。

7. **返回门落点依赖 portalTimer 冷却**（对照原版 300 tick 冷却）
   - `build_return_portal` 把玩家放在门内（`:4695-4709`），靠 `portalTimer=-300` 防立即回传；逻辑方向与原版一致，但边界较脆弱（若玩家退出门前计数到 80 会回传）。

---

## 4. Bug 清单（只记录不修）

| 编号 | 严重度 | 文件:行号 | 现象 | 建议修复方向 |
|------|--------|-----------|------|--------------|
| P5-1 | 中等 | `NetworkHandler.java:4732`；`DimensionType.java:3-6`（无 fixedTime） | 维度切换后 `update_time` 硬编码 age=0/time=6000；末地时间不锁定为 18000（原版末地恒为夜晚）；玩家每次跨维度后客户端时间被重置为正午 | 用 `Main.dayTime`/`Main.worldAge` 填 age/time；末地按 `fixedTime=18000` 发送；或在 `DimensionType` 增 `fixedTime` 字段并在分支处理 |
| P5-2 | 中等 | `DimensionType.java:8-22`（缺字段）；注册表下发需核对 | 内部维度元数据缺 `ambientLight/ultrawarm/fixedTime/infiniburn/piglinSafe/bedWorks/effects` 等；若登录下发注册表不含这些，客户端渲染/行为（下界温暖、末地锁夜、猪灵中立）可能异常 | 核对 `dumped_registries/reg_14.bin` 中 `dimension_type` 是否含上述字段并值正确；必要时补全内部枚举字段供 worldgen/逻辑使用 |
| P5-3 | 轻微 | `NetworkHandler.java:4651-4652` | 坐标缩放 `(int)` 截断丢失小数，门内偏移不保留 | 保留 double 偏移或按目标门中心对齐（轻微手感问题） |
| P5-4 | 轻微 | `NetworkHandler.java:4655-4657` | 末地→主世界固定回 (8,8)，非玩家重生点 | 改为返回玩家重生点/出口门位置（如需更贴近原版） |
| P5-5 | 轻微 | `NetworkHandler.java:4837-4855` | `findNearbyPortal` 半径 32、dy±16 暴力扫描，非 POI；主世界远处已有门时可能误建新门 | 扩大搜索/对齐原版 POI 半径（主世界 128、下界 16）或按区块记录门位置 |
| P5-6 | 轻微 | `NetworkHandler.java:5838-5868` | 末地门激活仅统计“区域内有 12 个带眼框”，未校验完整 3×3 结构 | 严格校验 3×3 开口（12 框围一圈 + 中心 3×3 空） |
| P5-7 | 轻微 | `NetworkHandler.java:4695-4709` | 返回门落点置于门内，依赖 portalTimer 冷却防回传，边界脆弱 | 落点略偏门侧并确保冷却起始值正确；可显式设 `inPortal` 标记 |

> 注：已知坑“维度切换漏发 GameEvent13/SetTime 导致加载界面卡死”**已在当前代码修复**（`NetworkHandler.java:4725-4732` 注释 + 实际发送 0x26/0x6F），本次未发现该缺陷复现。包格式（SpawnInfo 顺序、PositionUpdateRelatives、update_time、game_state_change）经 protocol.json 逐项核对均正确。

---

## 5. 结论与优先级建议

- **最高优先级（P5-1 / P5-2）**：`update_time` 硬编码 + 末地 `fixedTime` 缺失导致时间/锁夜异常；并务必核对下发注册表是否含完整维度元数据（ambient_light/ultrawarm/piglinSafe 等），否则客户端表现偏差。
- **低优先级（P5-3~P5-7）**：坐标截断、返回点、门查找半径、末地门激活宽松、落点冷却——均为手感/边界细节，不影响主链路可用。

总体：维度切换与传送门**主链路正确且发包齐全**（已知坑已修，包格式经核对无误）。核心风险在“维度元数据字段缺失导致的时间/环境表现”和“SetTime 硬编码”。建议优先处理 P5-1 与 P5-2 的注册表核对。
