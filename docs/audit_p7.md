# 审计 P7：末路与外岛（生存系统正确性）

> 审计对象：自研 MC 1.21.11（Protocol 774）核心 `com.yanrong.server`
> 审计范围：要塞 / 末地门 / 末影龙战斗 / 末地外岛与 gateway / 末地城与末地船 / 鞘翅 / 紫颂果
> 对照源：
> - 原版反编译 `mapping/remapped_server_1.21.11.jar.src/net/minecraft/...`（EndDragonFight、EndPodiumFeature、EndGatewayFeature、EndCityPieces、Elytra、ChorusFruit 等）
> - 协议包规范 `json/1.21.11/protocol.json`
> 铁律：未修改任何源码；所有结论均来自实际读取的代码与对照源。

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|---|---|---|
| 要塞生成（Stronghold） | 部分 | 仅生成"传送门房间"，非原版多房间要塞；放置算法为均匀网格，非 128 环 |
| 末地门激活（眼睛填充） | 缺失（绕过） | 传送门在生成时即预激活，眼睛填充逻辑形同虚设 |
| 末影龙战斗（EndDragonFight） | 部分 | 生命值/经验/光柱/水晶扣血正确；阶段简化、无重生、无死亡动画 |
| 出口传送门 / 龙蛋 | 已实现 | 击败后开启返回传送门 + 龙蛋 + 1 个 gateway（位置硬编码） |
| 末地外岛地形 | 存疑 | 取决于 THE_END 噪声路由是否生成外岛（未核实到具体实现） |
| end gateway 传送 | 部分 | 仅 1 个固定返回 gateway + 1 个外岛返回网关；非原版 20 个散布 gateway 岛 |
| 末地城 / 末地船 | 已实现（近似） | 使用**真实原版 NBT 模板**拼接，结构对齐不完美 |
| 鞘翅（elytra）滑翔 | 逻辑已实现 | 装备检测 + 滑翔元数据广播已写；但**鞘翅物品无来源（战利品表缺 elytra）** |
| 紫颂果（chorus fruit） | 部分 | 作为食物已实现；**缺失标志性的随机传送效果** |

结论：末路核心流程"可走通"（进末地→打龙→开返回门→到外岛→进末地城），但存在若干与原版行为不符、且影响生存玩法正确性的偏差，其中最严重的是**鞘翅不可获得**与**末地门绕过眼睛激活**。

---

## 2. 关键文件与行号证据

### 末影龙战斗
- `../src/main/java/com/CharunCore/server/world/entity/EndDragonFight.java`
  - `:30-34` 静态状态 `islandBuilt / dragonKilled / portalOpen / podiumY / dragon`
  - `:67-84` `onPlayerEnterEnd`：首次进末地建主岛 + 未击杀则生成龙
  - `:90-104` `createObsidianPlatform`：落脚黑曜石平台 (100,49,0)
  - `:106-114` `spawnDragon`：生命值来自 `EnderDragonEntity(200)`
  - `:120-129` `computePodiumY`：从 y=200 向下找首个非空气块
  - `:131-168` `buildPodium`：祭坛（bedrock 内圈 + 4 根带火把立柱）
  - `:170-221` `buildSpikes`：10 根尖刺（半径 42、2 个铁栏笼、顶部末地水晶）
  - `:227-233` `onCrystalDestroyed`：水晶被毁 → 龙受 10 伤害
  - `:235-257` `onDragonDeath`：关 BossBar、开返回门、全员 +12000 XP、播末地诗
  - `:259-289` `openExitPortal` / `buildGateway`：填传送门、放龙蛋、建 gateway @ (96,75,0)
  - `:306-343` `tick`：BossBar 进度（health/maxHealth）
  - `:386-413` `sendEndPoem`：标题 + 4 行聊天（非完整 credits）

- `../src/main/java/com/CharunCore/server/world/entity/EnderDragonEntity.java`
  - `:30-33` `maxHealth = 200.0f`（与原版一致）
  - `:44-61` `damage`：水晶存活时非水晶伤害 `amount *= 0.25f`
  - `:80-82` 水晶存活且血量不满时每 tick 回 0.05
  - `:102-123` `chooseNextPhase`：仅 3 阶段（绕圈/冲撞/栖息）
  - `:159-171` `breathAttack`：栖息龙息 6 伤害
  - `:198-214` `damageNearbyPlayers`：接触 10 伤害 + 击退

- `../src/main/java/com/CharunCore/server/world/entity/EndCrystalEntity.java`
  - `:18-32` 被毁时爆炸 `ExplosionEngine.explode(..., 6.0f, false)` + 回调 `onCrystalDestroyed`

### 要塞 / 末地门
- `../src/main/java/com/CharunCore/server/worldgen/structure/StrongholdPortalRoomGenerator.java`
  - `:75-95` `placeFloorAndPortal`：框架 + 中央 3x3 **在生成时即设为 end_portal（悬浮传送门）**（`:89-94`）
  - `:97-102` `placeFrame`：眼睛随机 `random.nextFloat() > 0.9f`
- `../src/main/java/com/CharunCore/server/worldgen/structure/StructureManager.java`
  - `:10-11` `STRONGHOLD_SPACING = 48`, `STRONGHOLD_SEPARATION = 12`
  - `:115, :177-185` 要塞生成入口（网格判定 `shouldPlace`）
  - `:187-198` `shouldPlace`：均匀网格（非同心环）
  - `:208-239` `findNearest`：眼之末影定位（与网格一致）
- `../src/main/java/com/CharunCore/server/network/NetworkHandler.java`
  - `:731-743` 点击末地传送门框架放眼睛
  - `:5838-5868` `tryActivateEndPortal`：数眼睛 ≥12 填中央 3x3（与预激活重复）
  - `:5810-5836` `throwEyeOfEnder`：抛眼定位要塞

### 维度传送 / gateway
- `../src/main/java/com/CharunCore/server/network/NetworkHandler.java`
  - `:3520-3537` `scanPortalBlock`：仅扫描 `floor(y) .. floor(y+1.8)`（脚→头）
  - `:3559-3584` 站门计时传送（gateway 瞬时 / 其余 80 tick）
  - `:4637-4709` `teleportToDimension`（进末地调 `onPlayerEnterEnd` + `createObsidianPlatform`）
  - `:4743-4784` `teleportViaEndGateway`：主岛→(1000,0)，外岛→(0,0)
  - `:4790-4830` `buildGatewayLandingPlatform`：外岛落点平台 + 返回 gateway
  - `:5051-5079` `hasElytraEquipped`(槽6==432) / `stopElytra` / `broadcastElytra`
  - `:1027-1044` 进食逻辑（chorus_fruit 仅回饥饿/饱和度）

### 末地城 / 末地船 / 战利品
- `../src/main/java/com/CharunCore/server/worldgen/structure2/NonJigsawPlacer.java`
  - `:49` `case "end_city" -> placeEndCity(...)`
  - `:414-476` `placeEndCity`：base_floor→tower_base→[tower_floor×0-2]→tower_top，随机附加二层楼或 ship
- `../src/main/java/com/CharunCore/server/worldgen/structure2/StructureTemplate.java`
  - `:88-106` 保留方块实体 NBT（含 LootTable）
  - `:110-121` `isBlockEntityBlock` 含 chest / end_gateway 等
- `../src/main/java/com/CharunCore/server/network/NetworkHandler.java`
  - `:6244-6314` 开箱读取 `LootTable` 标签并 `generateLoot` 填充
- `json/minecraft/loot_table/chests/end_city_treasure.json`
  - 条目（`:20-364`）：diamond / 各类 ingot / emerald / saddle / 马铠 / 钻石铁套 / spire_armor_trim … **无 elytra**
- `json/minecraft/structure/end_city/*.nbt`：base_floor / tower_base / tower_floor / tower_piece / tower_top / second_floor_1 / second_roof / ship 等模板均存在

### 紫颂果
- `../src/main/java/com/CharunCore/server/network/NetworkHandler.java`
  - `:4578-4582` `isAlwaysEdible` 含 chorus_fruit
  - `:4593, :4624` 食物值 4、饱和度 0.3
  - `:1027-1044` 进食：仅 `data.food/saturation`，**无传送分支**

### 对照：原版参考源（已读取）
- `mapping/.../net/minecraft/world/level/levelgen/feature/EndPodiumFeature.java`：祭坛半径 4、内圈 bedrock、激活时填 END_PORTAL + 4 根墙火炬（对照 `buildPodium`）
- `mapping/.../net/minecraft/world/level/levelgen/feature/EndGatewayFeature.java`：gateway 以 (x,y,z) 为中心、y±2 的 x==z==center 列放 bedrock、中心放 END_GATEWAY 并写入 exit 位置（对照 `buildGateway` 未写 exit）
- `mapping/.../net/minecraft/world/level/levelgen/structure/structures/EndCityPieces.java`：末地城用 jigsaw 拼接（对照 `placeEndCity` 手工叠加）
- 协议 `json/1.21.11/protocol.json`：确认 `0x09 boss_bar`、`0x08 block_change`、`0x77 system_chat`、`0x70 set_title_text`、`0x6E set_title_subtitle`、`0x71 set_title_time` 均正确（EndDragonFight 的包 ID 全部正确）

---

## 3. 对照原版的关键差异 / 偏差（逐条，附 文件:行号）

### D1. 末地门在生成时即被预激活，绕过"眼睛填充"原版机制
- 原版：`EndPodiumFeature`/`EndPortalBlock` 仅在 12 个框架全部 `eye=true` 时激活；眼睛需玩家逐一填充。
- 自研：`StrongholdPortalRoomGenerator.java:89-94` 在房间生成时就把中央 3x3 (`floorY+1`) 直接设为 `end_portal`（注释"悬浮传送门"），且 `:97-102` 框架眼睛是随机装饰。
- 后果：`tryActivateEndPortal`（`NetworkHandler.java:5838-5868`）的"数眼睛≥12"逻辑被预激活覆盖——玩家无需任何眼睛即可使用末地门，与原版核心机制不符。
- 同时 `placeFloorAndPortal:89-94` 把中央 3x3 设为传送门，但中央 `floorY` 层是空气、其下 `floorY-1` 是岩浆（`buildShell:62-63` 中央留空），玩家落入该 3x3 会站在悬浮传送门上/掉向岩浆，布局也偏离原版（原版传送门嵌在框架环内、下方为祭坛石砖而非岩浆）。

### D2. 要塞放置算法为均匀网格，非原版 128 同心环
- 原版：`StrongholdGenerators` 用 `concentric_rings`（8 环、共 128 个、半径递增、每环数量递增）。
- 自研：`StructureManager.java:10-11, :187-198` 用 `spacing=48, separation=12` 的均匀网格 `shouldPlace`，全图每 48 格一个；`findNearest(:208-239)` 与之一致。
- 后果：要塞数量、分布、距出生点距离均与原版不同（远超/少于 128，且均匀）。系统内部自洽（眼之末影能正确指向），但整体地形与原版世界不符。代码注释（`:171-176`）已自陈"vanilla placement type (concentric_rings) is not supported"。

### D3. 要塞结构大幅简化
- 原版要塞是多房间走廊迷宫（library、 prison、portal room 等）。
- 自研：`StrongholdPortalRoomGenerator.java` 仅生成一个 13×13 房间 + 直通地表的竖井（`:138-158`），无走廊/图书馆等。功能上能进末地，但探索体验缺失。

### D4. 末影龙在水晶存活时仍可被非水晶伤害（应为近乎无敌）
- 原版：末地水晶存在期间，龙对非爆炸来源基本免疫（只有水晶爆炸能伤它），且水晶持续回满血。
- 自研：`EnderDragonEntity.java:47-49` 仅 `amount *= 0.25f`（非 0），`:80-82` 每 tick 回 0.05。
- 后果：玩家可在水晶未清时"磨"死龙，难度显著低于原版。

### D5. 末影龙无重生机制
- 原版：在祭坛四边放 4 个末地水晶可重生龙（重复挑战/刷经验/再得龙蛋）。
- 自研：`EndDragonFight.java:31, :80-114` 用静态 `dragonKilled` 标志，击败后永不重生；无"四水晶重生"逻辑。服务器重启会重置该静态量（行为不一致）。

### D6. 末影龙战斗阶段简化
- 原版：`EnderDragonPhase` 含 HoldingPattern / StrafePlayer / LandingApproach / Landing / Sitting* / Takeoff / Death 等。
- 自研：`EnderDragonEntity.java:10-123` 仅 3 阶段（绕圈/冲撞/栖息），缺起飞/降落/扫射/死亡动画；龙蛋在 `openExitPortal` 击败瞬间生成，而非原版"龙死坠落、蛋在祭坛顶生成"的时序。

### D7. 返回传送门（出口传送门）触发可能失败（需验证）
- 原版：出口传送门（祭坛顶水平 end_portal）玩家走入即传送；`EndPortalBlock` 通过 `entityInside` 检测 AABB 与方块相交。
- 自研：`scanPortalBlock`（`NetworkHandler.java:3520-3537`）只扫描 `floor(y)..floor(y+1.8)`，即玩家脚底到头部所在方块，**不扫描脚正下方块** `floor(y)-1`。
- 后果：激活后的出口传送门与祭坛齐平（`openExitPortal:263-269` 在 `podiumY` 层填 portal）。若玩家站在传送门上（脚方块为 air，传送门在其脚下），扫描不到 → 踩门不传送。风险取决于 `end_portal` 是否被当作站立实心方块（若是，则玩家站其上、传送门在脚下方块，必然漏检）。建议实测；若复现，应把 `minY` 下探到 `floor(y)-1` 或改用"玩家 AABB 与传送门方块相交"判定。

### D8. 末地外岛 / end gateway 与原版不符
- 原版：外岛由 `EndDimension` 噪声生成散布岛屿；击败龙后生成 1 个 return gateway，且**全图散布 20 个 end gateway 岛**，每个都能把玩家送回主岛原点附近（exit 随机）。
- 自研：
  - 外岛地形依赖 `DensityRouterChunkGenerator.java:112-113` 的 `NoiseRouterData.end(...)`，未核实外岛是否真生成（若噪声未实现外岛，末地城会悬浮虚空）。
  - `buildGateway`（`EndDragonFight.java:274-289`）只在 (96,75,0) 建 1 个 gateway，且**未写入 exit 位置**（`EndGatewayFeature.java` 原版会 `setExitPosition`）；`teleportViaEndGateway`（`NetworkHandler.java:4743-4784`）硬编码 (1000,0)。外岛落点再建 1 个返回 gateway（`:4790-4830`）。
- 后果：双向可达（主岛↔外岛）但仅单点，无原版 20 散布 gateway；gateway 落点固定，不符原版随机 exit。

### D9. 末地城结构拼接不完美（jigsaw 缺失）
- 原版：`EndCityPieces` 用 jigsaw 块拼接，门洞/楼层严格对齐。
- 自研：`NonJigsawPlacer.java:414-476` 手工叠加模板：主塔（tower_*）竖直堆叠正确；但 `second_floor_1 + second_roof`（`:460-464`）放在与主塔**同一 centerX/centerZ**（直接重叠），末地船（`:467-473`）固定偏移 ±12 格。
- 后果：二层楼可能与主塔穿模、门洞不对齐；船位置固定非原版随机朝向拼接。属外观/结构偏差，不阻断通关。

### D10. 紫颂果缺失随机传送
- 原版：`ChorusFruitItem` 进食后（生存 100% / 创造 80% 几率）传送到附近随机位置，且本身回 4 饥饿 2.4 饱和度。
- 自研：`NetworkHandler.java:1027-1044` 进食仅 `data.food += 4`、`saturation += 4*0.3*2`，**无传送分支**。
- 后果：紫颂果只是普通食物，失去外岛核心机动机制。

### D11. 鞘翅物品无来源（战利品表缺 elytra）
- 原版：末地船宝箱（`chests/end_city_treasure`）含 elytra（weight 1）。
- 自研：滑翔逻辑已实现（`hasElytraEquipped` 槽6==432、`broadcastElytra` 元数据、`fallFlying` 处理），但 `json/minecraft/loot_table/chests/end_city_treasure.json` 全部条目（`:20-364`）**无 elytra**；全仓 grep `elytra` 仅出现在 `NetworkHandler.java`（装备/元数据）与 `SmeltingSystem.java`，无生成/掉落来源。
- 后果：玩家**无法获得鞘翅**，滑翔功能成摆设；且即便模板 ship 的 chest 带 `LootTable` 标签（由 `StructureTemplate.java:88-106` 正确保留、开箱时 `NetworkHandler.java:6256-6264` 生成），表内也无 elytra，故终不可得。

### D12. 末地诗/结局简化
- 原版：击败龙后播放完整 credits 画面 + 授予 `the_end` advancement。
- 自研：`sendEndPoem`（`EndDragonFight.java:386-413`）仅发标题 + 4 行聊天；无 credits、无 advancement 授予（成就系统见 P12）。轻微。

---

## 4. Bug 清单（只记录不修）

| # | 严重度 | 文件:行号 | 现象 | 建议修复方向 |
|---|---|---|---|---|
| B1 | **严重** | `json/minecraft/loot_table/chests/end_city_treasure.json`（全表）；`NetworkHandler.java:6256-6264` | 末地船宝箱战利品表不含 elytra，鞘翅不可获得，滑翔玩法无法体验 | 在 `end_city_treasure.json` 增加 elytra 条目（weight 1，必要时带损坏函数），或单独在 ship 模板 chest 注入 elytra |
| B2 | **严重** | `StrongholdPortalRoomGenerator.java:89-94`；`NetworkHandler.java:5838-5868` | 末地门在要塞生成时即预激活，无需填充眼睛即可进末地，违背原版核心机制 | 中央 3x3 生成时留空（不放 end_portal），仅在 12 框架全 `eye=true` 时由 `tryActivateEndPortal` 激活；并移除悬浮传送门下的岩浆坑 |
| B3 | **严重** | `StructureManager.java:10-11, :187-198`；`:115, :177-185` | 要塞为均匀网格非原版 128 同心环，数量/分布/距离偏离 | 实现 `concentric_rings` 放置（或至少 128 个、按环半径递增），并同步 `findNearest` 算法 |
| B4 | **中等** | `EnderDragonEntity.java:47-49` | 末地水晶存活时龙仍可被非水晶伤害（仅 ×0.25），难度过低 | 水晶存活时非爆炸伤害应近似免疫（如 ≤0 或仅爆炸来源生效），并强化水晶回血 |
| B5 | **中等** | `EndDragonFight.java:31, :80-114` | 末影龙击败后永不重生，无法重复挑战/刷经验，且静态量重启即重置导致不一致 | 实现四水晶重生逻辑（清水晶→放四边→重生）；持久化 `dragonKilled` 到世界数据 |
| B6 | **中等** | `NetworkHandler.java:3520-3537`（配合 `EndDragonFight.java:263-269`） | 返回传送门与祭坛齐平，玩家站立其上时被扫描漏检，可能踩门不传送（需实测确认） | `scanPortalBlock` 的 `minY` 下探到 `floor(y)-1`，或改用"玩家 AABB 与传送门方块相交"判定 |
| B7 | **中等** | `EndDragonFight.java:274-289`；`NetworkHandler.java:4743-4784` | end gateway 仅单点、硬编码 (96,75,0) 与 (1000,0)，未写 exit 位置，不符原版 20 散布 gateway | 引入 gateway 出口坐标（随机外岛点）；按原版在击败龙后于祭坛附近生成 return gateway + 外岛散布 gateway 岛 |
| B8 | **中等** | `NetworkHandler.java:1027-1044`（进食分支）；`ChorusFruit` 原版对照 | 紫颂果无随机传送，仅为普通食物 | 进食 chorus_fruit 时调用随机传送（生存 100% 触发，创造可设 80%），并复用现有维度内传送逻辑 |
| B9 | **中等** | `DensityRouterChunkGenerator.java:112-113`（THE_END 噪声） | 末地外岛地形是否生成未核实，可能末地城悬浮虚空 | 核实 `NoiseRouterData.end` 是否实现外岛噪声（`EndIsland`/`end_islands`），否则末地城永远落在虚空 |
| B10 | **轻微** | `NonJigsawPlacer.java:460-473` | 末地城二层楼与主塔重叠、末地船固定偏移 ±12，门洞不对齐/可能穿模 | 改用 jigsaw 风格拼接（或调整二层楼偏移使其接驳塔身门洞、船偏移按模板门洞对齐） |
| B11 | **轻微** | `EnderDragonEntity.java:10-123` | 龙阶段仅 3 种，缺起飞/降落/扫射/死亡动画 | 增加 Landing/Takeoff/Strafe/Death 阶段，逼近原版行为（非阻断性） |
| B12 | **轻微** | `EndDragonFight.java:386-413` | 末地诗仅聊天提示，无 credits 画面/advancements | 触发 credits 画面（若客户端支持）与 `the_end` advancement 授予 |
| B13 | **轻微** | `StrongholdPortalRoomGenerator.java:97-102` | 框架眼睛随机（含 0 眼），与 B2 预激活叠加后眼睛纯装饰 | 随 B2 修复后，眼睛应默认 false，由玩家填充；若保留随机，需保证与激活判定一致 |

> 说明：B6、B9 标注"需实测/核实"——审计期间仅做静态代码核对，未启动服务器，无法动态复现，但逻辑风险高，建议优先实测确认。

---

## 5. 结论与优先级建议

### 已实现（正确/可走通）
- 末影龙基础战斗：生命值 200、接触/龙息伤害、水晶扣血与回血、击败 +12000 XP、BossBar（包 ID 与字段经 `protocol.json` 核对全部正确：0x09/0x08/0x77/0x70/0x6E/0x71）。
- 末地光柱：10 根尖刺、半径 42、2 个铁栏笼、顶部末地水晶，与原版结构近似。
- 眼之末影抛射 + 要塞定位（与要塞放置算法自洽）。
- 进/出末地维度传送、落脚黑曜石平台、出口传送门填充、龙蛋生成、gateway 双向传送（主岛↔外岛）。
- 末地城/末地船使用**真实原版 NBT 模板**，外观可辨识；开箱按 `LootTable` 生成战利品的机制完整。
- 鞘翅滑翔的装备检测/元数据广播逻辑已就位。

### 必须修复（阻塞生存玩法正确性）
1. **B1 鞘翅不可获得** —— 直接导致末路终局玩法（鞘翅飞行）无法体验，优先级最高。
2. **B2 末地门预激活** —— 跳过眼睛填充这一原版标志性机制，影响整个要塞→末地流程的正确性。
3. **B3 要塞 128 环** —— 架构性偏差；若要"像原版"，需替换放置算法（否则至少注明非原版）。
4. **B9 外岛地形核实** —— 若外岛不生成，末地城/鞘翅来源（B1）都无从谈起，应最先确认。

### 建议修复（提升还原度）
5. B5 龙重生机制（可重复挑战）
6. B4 龙无敌窗口（水晶存活时）
7. B6 返回门触发（实测后修）
8. B7 gateway 散布
9. B8 紫颂果传送

### 可后置（外观/体验）
10. B10/B11/B12/B13 结构拼接、龙阶段、credits、眼睛随机。

### 总体判断
末路主流程"可通关"，但**生存正确性在三个关键点偏离原版且影响实质玩法**：鞘翅获取（B1）、末地门激活（B2）、要塞分布（B3）。紫颂果传送（B8）与龙重生（B5）属"功能缺失"。建议按上述优先级推进修复，并优先实测 B6（返回门）与 B9（外岛生成）两项高风险项。
