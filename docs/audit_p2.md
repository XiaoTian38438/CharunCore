# 审计 P2：玩家交互 / 背包系统

> 审计范围：客户端→服务器交互入口、物品交互（放置/使用/丢弃/交换）、背包与容器模型（46 槽玩家背包、箱子/漏斗/熔炉/工作台窗口）、合成系统（2×2 与 3×3、配方匹配与下发）。
> 审计铁律：仅读取源码 + 反编译对照，**未修改任何 .java**。所有行号均来自本次实际读取（`../src/main/java/com/CharunCore/server/network/NetworkHandler.java` 6806 行、`world/PlayerData.java` 41 行、`world/CraftingSystem.java` 339 行）。
> 对照源：`mapping/cfr-source/net/minecraft/world/inventory/InventoryMenu.java`（窗口 0 槽位布局权威）、`mapping/remapped_server_1.21.11.jar.src/` 原版实现、任务给定的线序包号（`mapping/protocol_ref/protocol_1.21.11.json` 为 type/schema 结构，包号以任务给定 + 代码内注释为准）。
> 结论基准日：2026-08-11

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|---|---|---|
| 右键放置方块（use_item_on / 0x3F） | 已实现 / 高保真 | 特殊物品（桶/桶装液体/打火石/末影之眼/船/刷怪蛋）、墙面火把、含水放置、床/门双高、放置校验齐全 |
| 左键/右键 攻击·使用（use_item / 0x40） | 已实现 / 高保真 | 进食/饮用药水/弓/盾/末影珍珠/丢物品（PlayerAction status 3/4） |
| 容器点击语义（左/右/半堆/Shift/数字键/克隆/丢/Q/拖拽） | 已实现 / 高保真 | `handleContainerClick` 等 8 个分支，光标（carried）服务端权威 |
| 创造模式物品槽（set_creative_slot / 0x37） | 已实现 / 部分 | 写入 0–45，但跳过组件、不处理盔甲/副手特殊语义 |
| 46 槽玩家背包模型（PlayerData） | 已实现 / 高保真 | 布局与原版窗口 0 一致（见 §3.1） |
| 窗口 0 槽位同步（0x12 ContainerSetContent / 0x14 ContainerSetSlot） | 已实现 / 高保真 | 1:1 直发 `PlayerData[i]` 于协议槽 `i`，布局匹配原版 |
| 箱子/末影箱 打开·关闭·点击·持久化 | 已实现 / 部分 | 单箱 27 格可用；**木桶无法打开（P9-B1 再现）**；多人同容器不同步（见 P2-B4） |
| 熔炉/漏斗 窗口 | 已实现 | 独立数据结构 + 自动 tick |
| 工作台（3×3）窗口 | 已实现 / 部分 | 打开、网格同步、合成结果、Shift 批量可用；仅硬编码配方表 |
| 2×2 玩家随身合成 | 已实现 / 部分 | 网格映射正确；仅硬编码配方表 |
| 合成系统（CraftingSystem） | 部分实现 | 手工配方表（约 110 条）、形状+镜像匹配；**无 JSON 配方注册表、多格无序配方不支持** |
| 木桶打开 | **缺失（功能性 Bug）** | `NetworkHandler.java:6244` 分支无 `"barrel"`，全文件 0 处 |
| 陷阱箱红石信号 | **缺失** | 见 P9-D2（交叉引用） |
| 双箱合并 54 格 | **缺失** | 见 P9-D3（交叉引用） |

**总体判定：玩家交互与背包域“部分实现、协议高保真”。** 窗口 0 的槽位布局、点击语义、光标权威、放置/使用逻辑与原版高度一致，可正常游玩；主要缺口集中在**配方覆盖范围、木桶、多人同容器同步、若干协议细节**。

---

## 2. 关键文件与行号证据

### 2.1 交互入口（NetworkHandler.java）
- `handlePlay` 分发：`267`（按 `id` 路由）
- 右键放置：`697` `id == 0x3F`（use_item_on）：握手 ack `0x04`（`707`）、特殊物品 `716-816`、方块交互 `876`、正常放置 `882-960`
- 使用物品：`963` `id == 0x40`（use_item）：进食/药水/弓/盾/末影珍珠 `973-1049`
- 创造槽：`1053` `id == 0x37`（set_creative_slot）
- 容器按钮：`1084` `id == 0x10`；容器点击：`1091` `id == 0x11`（serverbound_container_click）
- 关闭窗口：`1135` `id == 0x12`（close_window，回退 carried 物品 + `sendInventoryUpdate`）
- 配方书填充：`1170` `id == 0x26`（place_recipe，仅工作台窗口）
- 攻击/交互：`1194` `attackMob`（0x19 Interact 回落）

### 2.2 点击语义（NetworkHandler.java）
- `handleContainerClick` `3025`（左键 button0 / 右键 button1）
- `handleShiftClick` `3278`；`handleSwapClick` `3184`；`handleCloneClick` `3206`；`handleThrowClick` `3216`；`handleDragClick` `3230`
- `handleCraftingResultClick` `3386`；`craftAllFromResult`（Shift 结果）`3416`；`consumeCraftingGrid` `3447`
- 玩家 2×2 网格：`getPlayerCraftingGrid3x3` `3466`、`handlePlayerCraftingResultClick` `3475`、`consumePlayerCraftingGrid` `3503`

### 2.3 背包/容器模型与同步
- `PlayerData.java:7-15`：`inventoryIds/Counts/Damage[46]` + `inventoryEnchants[]` + `inventoryPotion[]`
- `sendInventoryUpdate` `2334`（0x12，窗口 0，46 槽直发）；`sendCarriedItem` `2346`（0x5e）；`writePlayerSlot` `2363`（带组件写出）
- `sendSlotUpdate` `2376` / `sendSlotUpdateRaw` `2448`（0x14）
- 槽位映射：`chestToPlayerSlot` `2585`、`craftingToPlayerSlot` `2580`、`furnaceToPlayerSlot` `2590`、`hopperToPlayerSlot` `2595`
- `getSlotItem` `2811` / `setSlotItem` `2880`（按 windowId 分发，窗口 0 直写 `data.inventoryIds[slot]`）
- 盔甲模型：`ARMOR_SLOT_FIRST=5` `3738`，伤害读取 `3829-3851`、护甲栏发送键 `4297-4303`
- 副手交换：`PlayerAction` status 6 用槽 **45** `683-686`（正确）

### 2.4 容器打开（NetworkHandler.java）
- 箱子/末影箱分支：`6244`（`name.endsWith("_chest") || name.equals("ender_chest")`，**无 barrel**）；打开 0x39+0x12 `6320-6338`（menu `generic_9x3`，63 槽）
- 工作台：`6340`（menu `crafting`，46 槽）；`sendCraftingTableContent` `2536`
- 熔炉：`6354`；漏斗：`6383`；`persistChest` `2762`；`dropContainerContents` `2657`
- `openCraftingTable` `2564`

### 2.5 合成系统（CraftingSystem.java）
- 配方表：`add(...)` `230`，形状识别 `isSingleCell` `235`；`matchRecipe` `241`（精确→镜像→单格无序三段）
- `matches` `255`、`matchesMirrored` `295`、`matchesShapeless` `305`
- 标签系统 `TAGS` `9`、`itemMatches` `222`；`representativeItem` `215`（配方书图标）

---

## 3. 对照原版的关键差异 / 偏差（逐条，含 文件:行号）

### 3.1 窗口 0（玩家背包）槽位布局 —— 与原版一致 ✅
- 原版 `InventoryMenu` 构造（cfr `InventoryMenu.java:54-62`）：`addResultSlot`(0) → `addCraftingGridSlots`(1–4) → 盔甲 4 槽(5–8，`39-i`，即靴/腿/胸/头) → `addStandardInventorySlots`(主背包 9–35 + 快捷栏 36–44) → 副手 `addSlot(inventory, 40, ...)` 即**容器槽 45**（`:62`）。
- 项目 `sendInventoryUpdate`（`2334-2343`）按协议槽 `i` 直发 `data.inventoryIds[i]`；其用途分布为：0=结果、1–4=2×2 网格、5–8=盔甲、9–35=主背包、36–44=快捷栏、45=副手。与上面逐槽对应，**布局与原版一致**。盔甲经通用 `setSlotItem(0,slot)` 写入（拖放即可装备），伤害计算 `3829` 读取 `ARMOR_SLOT_FIRST..LAST=5..8`，闭环正确。

### 3.2 use_item_on（0x3F）/ use_item（0x40）—— 与原版高度一致 ✅
- 放置前先发 `0x04` ack（`707`/`966`，携带真实 sequence），符合 1.21.11“服务器必须确认 block-change”的要求。
- 特殊物品（桶/液体/打火石/末影之眼/船/刷怪蛋）、墙面火把转 `wall_torch`（`889-895`）、含水放置（`906-909`）、床/门双高（`916-945`）、放置校验 `canPlaceInto`+`intersectsPlayer`（`912-913`）齐备，字段顺序与原版 `ServerboundUseItemOn`（`BlockHitResult` 尾 14 字节 skip，`702`）一致。

### 3.3 容器点击语义 —— 与原版一致 ✅
- 左键取放/合并/交换、右键 1 个/半堆、Shift 双向搬运、数字键交换（含副手 40）、创造克隆、Q 丢、拖拽（左/右 begin/add/end）均在 `3025-3276` 实现；`carried`（光标）由服务端权威维护并通过 `0x5e` 下发，避免客户端预测漂移。

### 3.4 偏差（需修复）

**D1. 木桶（barrel）无法右键打开 —— 严重功能缺失（P9-B1 再现）**
- `NetworkHandler.java:6244` 分支条件为 `name.endsWith("_chest") || name.equals("ender_chest")`。全文件 `grep -ci barrel` = **0**，即桶右键无任何处理（连 `handleBlockInteraction` 也未对 barrel 开界面）。
- 原版 `BarrelBlock` 右键打开 `generic_9x3` 界面（与箱子同菜单）。结果：生存下木桶成为不可用存储（仅漏斗可间接写入，`ContainerStore.refAt` 已含 barrel）。
- 影响：木桶功能性报废。修复方向：在 `6244` 增加 `|| name.equals("barrel")`，并复用单箱 27 格 + `generic_9x3` 下发逻辑（`6320-6338`）。

**D2. 多人同容器并发：其他观察者不收槽位更新（P2 新发现）**
- 所有槽更新经 `sendSlotUpdate`→`sendSlotUpdateRaw`（`2448`）→`sendPacket(ctx, 0x14, ...)`，其中 `ctx` 为**当前玩家**上下文。两个玩家打开同一箱子（各自 windowId，但共享 `ContainerStore.chest(pos).slots`）时，玩家 A 的点击只把 `0x14` 发给 A，玩家 B 的界面不会刷新，直到 B 重新开关。
- 原版对共享容器向所有观察者广播 `ContainerSetSlot`/`ContainerSetContent`。
- 影响：多人共用箱子/漏斗时视图错乱、可重复取放导致“刷物品”观感。修复方向：维护 `windowId → Set<player>`，槽变更时向所有持有该窗口的玩家广播 `0x14`/`0x12`。

**D3. 容器点击误发 AcknowledgeBlockChange（0x04, sequence=0）**
- `NetworkHandler.java:1107` 在每次 `0x11` 容器点击后 `sendPacket(ctx, 0x04, pb -> pb.writeVarInt(0))`。容器点击不应触发 block-change 确认；该包仅 UseItemOn/UseItem 需要（`:707`/`:966` 带真实 sequence）。恒发 seq=0 可能扰乱客户端 block-change 确认计数器。
- 影响：低（客户端对不匹配 ack 通常容忍），但属非原版协议行为。修复方向：删除 `1107`。

**D4. 容器点击 changed-slots 解析未完整消耗 ItemStack 组件**
- `NetworkHandler.java:1098-1105`：对每个 changed-slot 读 `Short`+`VarInt id`，若 `id>0` 再 `readVarInt()`（count）+ `readByte()`。但 774 的 `ItemStack` 在 count 之后还有可变长 `DataComponentPatch`（非单字节）。`readByte()` 仅吞掉 1 字节组件，后续槽条目与末尾 carried 物品被错读。
- 影响：有限——服务端光标权威、changed-slots 内容被忽略，仅 `1104` 的 carried 判定可能取错（仅用于 `>0` 守卫）。但仍属协议解析缺陷。修复方向：对每个 changed-slot 的 ItemStack 完整读取组件补丁（added/removed 组件数 + 数据）。

**D5. 合成系统为硬编码配方表，非数据驱动（P2 新发现）**
- `CraftingSystem.java` 用约 110 条 `add(...)` 手工登记；`matchesShapeless`（`305`）仅对 `isSingleCell`（单格）配方生效（`235`）。原版从 `data/minecraft/recipes/*.json` 加载完整注册表，且支持**多格无序配方**（染色、书与笔等大量配方为无序）。
- 影响：大量原版配方不可合成，生存进阶受限。修复方向：解析原版 recipes JSON（含 `type`/`pattern`/`key`/`ingredients`/`result`），实现通用 shaped + shapeless 匹配；`matchRecipe` 增加多格无序分支。

**D6. 容器点击 SWAP（副手）目标槽错误（button==40 → 槽 40，应为 45）**
- `handleSwapClick`（`3185-3194`）：`button==40` 时 `getSlotItem(0, 40)` 交换。原版 SWAP 的 `button=40` 是“副手”的**玩家背包序号**，但在本项目窗口 0 容器内副手为 `PlayerData[45]`（`InventoryMenu.java:62`；且 `PlayerAction` status 6 副手交换在 `683-686` 正确用 45）。
- 影响：低——常规 F 键副手走 `PlayerAction` status 6（正确），仅“在容器内用副手格交换”小众路径错位。修复方向：交换目标用 45。

**D7. place_recipe（0x26）仅支持工作台窗口，不支持 2×2 随身合成**
- `NetworkHandler.java:1176`：`if (!openCraftingGrids.containsKey(windowId)) return;` 仅工作台。原版配方书点击也可填玩家 2×2 网格。影响：随身 2×2 配方无法一键填充（小众）。修复方向：对 windowId==0 单独填 `getPlayerCraftingGrid3x3` 对应槽 1–4。

**D8. 交叉引用：陷阱箱无红石、双箱不合并、容器丢 NBT（见 audit_p9 D2/D3/D5）**
- 陷阱箱打开不输出红石信号（P9-D2）、相邻双箱不合并 54 格（P9-D3）、容器物品无 per-item NBT（P9-D5）——均属本交互域相关缺陷，已在 P9 详述，此处不重复计级，但共同构成“容器交互不完全原版”。

**D9. P6-B1 旧线索在现行代码位置失效（验证说明）**
- 任务线索“P6-B1 酿造修饰失效 `NetworkHandler.java:428-429`”。现行 `428-429` 实为 `change_game_mode`（0x04 serverbound）处理分支（`425` 起），与酿造无关——属旧文档行号漂移。酿造修饰属 P6 域，本审计不重复定级，但提示：引用旧审计时须按现行行号复核，不可直接沿用。

---

## 4. Bug 清单（只记录不修；严重度 P0–P3）

| # | 严重度 | 文件:行号 | 现象 | 原版对照 | 影响 | 建议修复方向 |
|---|---|---|---|---|---|---|
| P2-B1 | **P2** | `NetworkHandler.java:6244` | 木桶右键无法打开（全文件 0 处 barrel 处理） | `BarrelBlock` 右键开 `generic_9x3` | 木桶不可用存储 | 分支增加 `\|\| name.equals("barrel")`，复用单箱 27 格打开逻辑 |
| P2-B2 | **P2** | `NetworkHandler.java:2448`（sendSlotUpdateRaw）+ `2880` | 多人开同一箱子，仅操作方收到槽更新，另一观察者不同步 | 原版向所有观察者广播 | 多人共用容器视图错乱 | 维护 windowId→玩家集，广播 `0x14`/`0x12` 给所有持有者 |
| P2-B3 | **P2** | `CraftingSystem.java:230,235,305` | 仅硬编码 ~110 配方；多格无序配方不支持；无 JSON 注册表 | 原版 recipes JSON + 通用 shapeless | 大量配方不可合成 | 加载 recipes JSON，补多格 shapeless 匹配 |
| P2-B4 | **P2** | `NetworkHandler.java:1135-1166`（close_window 仅 `sendInventoryUpdate` 给本人）+ P9-D3 | 双箱不合并、陷阱箱无红石（交叉引用 P9） | `DoubleBlockCombiner` / `TrappedChestBlock.getSignal` | 容器交互非原版 | 见 P9 修复建议 |
| P2-B5 | **P3** | `NetworkHandler.java:1107` | 容器点击误发 `0x04` AcknowledgeBlockChange(seq=0) | 仅 UseItem 需 ack | 协议非原版，潜在确认计数器扰动 | 删除该行 |
| P2-B6 | **P3** | `NetworkHandler.java:1098-1105` | changed-slots 的 ItemStack 组件未完整消耗（仅 `readByte`） | 774 ItemStack 含 DataComponentPatch | 解析漂移（实际影响有限，光标权威） | 完整读组件补丁 |
| P2-B7 | **P3** | `NetworkHandler.java:3185-3194` | 容器内 SWAP 副手目标槽 40 而非 45 | 窗口 0 副手容器槽=45 | 小众路径错位（F 键走 status6 正确） | 交换目标改 45 |
| P2-B8 | **P3** | `NetworkHandler.java:1170-1176` | place_recipe 忽略窗口 0（2×2 随身合成） | 配方书可填 2×2 | 随身配方无法一键填充 | 对 windowId==0 填槽 1–4 |
| P2-B9 | **P3** | `NetworkHandler.java:1054-1079` | set_creative_slot 跳过组件、盔甲/副手特殊语义不处理 | 创造槽含组件/特殊槽 | 创造模式物品无 NBT、特殊槽不识别 | 按 774 解析组件；交叉引用 audit_creative_p2 |

> 注：P0/P1 级（崩溃/关键路径不可用）在本域**未发现**。窗口 0 布局、点击语义、放置/使用、光标权威均与原版一致，当前可正常游玩。

---

## 5. 与原版一致性结论

**整体完成度：部分实现 / 协议高保真。**

- **高保真部分**：玩家背包窗口 0 的 46 槽布局（结果/2×2 网格/盔甲/主背包/快捷栏/副手）逐槽对应原版 `InventoryMenu`；`use_item_on`/`use_item` 的字段顺序与 ack 机制正确；容器点击的 8 类语义（含拖拽、Shift 批量、光标权威）与原版一致；放置/使用/丢物品/创造槽的协议行为正确。
- **偏差部分**：
  1. **配方覆盖范围**（P2-B3）：硬编码表而非数据驱动，多格无序缺失 → 生存进阶受限。
  2. **木桶交互**（P2-B1）：完全缺失，复用单箱逻辑即可修复。
  3. **多人同容器同步**（P2-B2）：仅广播给操作方，破坏共享容器语义。
  4. **协议细节**（P2-B5/B6/B7/B8）：容器点击误发 ack、changed-slots 组件未耗尽、副手 SWAP 槽错位、2×2 配方书填充缺失——均非崩溃级，但属非原版。
  5. **交叉引用缺陷**（P9-D2/D3/D5）：陷阱箱红石、双箱合并、容器 NBT 丢失。
- **协议正确性**：play 阶段包号与任务给定线序一致；客户端→服务器字段解析在交互/背包域经核对正确。须持续注意 774 的 `ItemStack` 携带 `DataComponentPatch`，凡读取客户端 ItemStack 处（容器点击、创造槽）均应完整消耗组件，否则存在解析漂移风险（P2-B6/B9）。

**最高优先级修复建议**：P2-B1（木桶）、P2-B2（多人同容器同步）、P2-B3（配方注册表）。三者直接决定生存玩法的完整度与多人可靠性。
