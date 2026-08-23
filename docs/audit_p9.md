# 审计 P9：存储系统（箱子 / 陷阱箱 / 木桶）

> 审计范围：箱子、奖励箱、陷阱箱、木桶的容器数据结构、打开/关闭、内容持久化、破坏掉落，以及与原版 `ChestBlockEntity` / `BarrelBlockEntity` / `TrappedChestBlock` 的对照。
> 审计铁律：仅读取 + 本报告，**未修改任何源码**。所有行号均来自本次实际读取。
> 关键源文件：`../src/main/java/com/CharunCore/server/world/ContainerStore.java`、`../src/main/java/com/CharunCore/server/utils/BlockManager.java`、`../src/main/java/com/CharunCore/server/network/NetworkHandler.java`
> 原版对照：`mapping/remapped_server_1.21.11.jar.src/net/minecraft/world/level/block/entity/{ChestBlockEntity,BarrelBlockEntity,TrappedChestBlockEntity}.java`、`.../block/{ChestBlock,TrappedChestBlock,BarrelBlock}.java`

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|---|---|---|
| 单箱/陷阱箱/木桶 27 格内存存储 | 已实现 | `ContainerStore.ChestData`（27 物品槽，id+count 打包到 `int[54]`） |
| 熔炉 / 漏斗 / 锻造 / 附魔 / 铁砧 / 酿造台 | 已实现 | 各自独立数据结构 + 自动 tick |
| 打开/关闭窗口、内容下发、取放物品 | 已实现 | `NetworkHandler` 容器交互网络 |
| 关闭时持久化到区块 block entity | 已实现（有缺陷，见 §4） | `persistChest` |
| 破坏掉落 | 已实现 | `dropContainerContents` |
| 末影箱（按玩家共享 27 格） | 已实现 | `enderChest(UUID)` |
| 陷阱箱打开时输出红石信号 | **缺失** | 见 §3 / §4 |
| 木桶右键打开界面 | **缺失（功能性 Bug）** | 见 §3 / §4 |
| 双箱合并为 54 格 | **缺失** | 见 §3 / §4 |
| 奖励箱 LootTable 生成 | 已实现 | 打开时按种子生成，但仅一次性（见 §4） |

---

## 2. 关键文件与行号证据

### 2.1 容器数据结构（ContainerStore.java）
- `ContainerStore.java:14-18` — `ChestData`：`int[] slots = new int[54]`，id 存于 `2*i`、count 存于 `2*i+1`，即 **27 个物品槽**。
- `ContainerStore.java:70-77` — 各类容器用独立 `ConcurrentHashMap` 按 `Pos` 存储；末影箱按 `UUID`。
- `ContainerStore.java:226-236` — `refAt(Pos)`：把方块名映射到容器引用；`"chest","trapped_chest","barrel"` 统一映射到 `slotCount=27, type=0`。**陷阱箱与木桶在存储层被当作普通箱子处理**。
- `ContainerStore.java:266-292` — `tickHopper`：从正上方抽取、向 `facing` 方向推送，冷却 8 tick；`refAt` 见 2.1。
- `ContainerStore.java:304-385` — `tickFurnace`；`ContainerStore.java:403-455` — `tickBrewing`。

### 2.2 打开 / 关闭 / 持久化（NetworkHandler.java）
- `NetworkHandler.java:62,64` — `openChests` / `openEnderChests` 记录窗口 id → 位置 / 玩家 uuid。
- `NetworkHandler.java:6244` — 右键打开分支条件：`name.endsWith("_chest") || name.equals("ender_chest")`。**注意这里不含 `"barrel"`**。
- `NetworkHandler.java:6296-6338` — 打开流程：从区块 block entity 读取 `Items` / `LootTable`；`loaded` 标记防止重复载入；发送 `0x39`（OpenScreen）+ `0x12`（ContainerSetContent），菜单类型 `generic_9x3`（行 6322），下发 27 格容器 + 36 格玩家背包 + 9 格快捷栏。
- `NetworkHandler.java:2585-2588` — `chestToPlayerSlot`：容器槽 `27..62` → 玩家内部槽 `9..44`。
- `NetworkHandler.java:1149-1151` — 窗口关闭时 `persistChest(closedChest)`。
- `NetworkHandler.java:2762-2796` — `persistChest`：把内存槽写回区块 block entity 的 `Items` 列表；`Count` 截断到 127（`Math.min(127, cnt)`，行 2778）。**block entity id 硬编码为 `"minecraft:chest"`（行 2789），不区分陷阱箱/木桶**。
- `NetworkHandler.java:2657-2685` — `dropContainerContents`：破坏时 `removeChest/removeFurnace/removeHopper` 取出内存并逐个 `spawnDrop`。
- `NetworkHandler.java:2564-2576` — `openCraftingTable`：3×3 工作台窗口（与箱子无关，列此备查）。

### 2.3 容器方块注册（BlockManager.java）
- `BlockManager.java:202` — `"chest": 2.5f` 硬度定义；`BlockManager.java:262-263` — `chest`/`furnace`/`smoker`/`blast_furnace` 归类为 `axe` 工具。木桶（barrel）、陷阱箱（trapped_chest）未在特殊列表，沿用默认。
- `BlockManager.java:308-316` — `getDefaultStateForItem`：`wheat_seeds → wheat`，用于播种（P11 相关）。

---

## 3. 对照原版的关键差异 / 偏差（逐条）

**D1. 木桶（barrel）无法右键打开 —— 严重偏差**
- 原版 `BarrelBlock` 右键打开 `generic_9x3` 容器界面（与箱子相同菜单）。
- 本核心打开分支条件为 `name.endsWith("_chest") || name.equals("ender_chest")`（`NetworkHandler.java:6244`），`"barrel"` 不匹配，且全局 grep 显示 `NetworkHandler.java` 中 **完全没有 "barrel" 字样**。
- 结果：玩家放置木桶后无法右键查看/存取物品（仅漏斗可间接写入，见 `refAt` 在 `ContainerStore.java:230` 已包含 barrel）。**生存模式下木桶等于不可用存储**。

**D2. 陷阱箱（trapped_chest）打开时不输出红石信号 —— 偏差**
- 原版 `TrappedChestBlock.getSignal` 返回 `Mth.clamp(ChestBlockEntity.getOpenCount(...), 0, 15)`（`TrappedChestBlock.java:47-48`），打开人数越多信号越强（1..15），关闭时 `signalOpenCount` 向自身及下方方块更新邻居红石（`TrappedChestBlockEntity.java:18-26`）。
- 本核心 `refAt`（`ContainerStore.java:230`）把 `trapped_chest` 当普通箱子（type 0），打开逻辑走同一 `_chest` 分支（`NetworkHandler.java:6244`），**没有任何打开计数 / 红石输出逻辑**。陷阱箱行为与普通箱子完全一致，失去其核心特性。

**D3. 双箱（double chest）不合并为 54 格 —— 偏差**
- 原版相邻两箱经 `DoubleBlockCombiner` 合并为 54 格（`generic_9x6`）共享库存。
- 本核心 `ContainerStore.chest(p)` 以**精确 Pos 为 key**（`ContainerStore.java:81-83`），相邻两箱各持独立 27 格；打开永远发送 `generic_9x3`（行 6322）。**双箱退化为两个互不共享的独立 27 格箱子**，破坏"大容量合并"语义。

**D4. persistChest 的 block entity id 恒为 chest —— 轻微偏差**
- `NetworkHandler.java:2789` 硬编码 `b.putString("id", "minecraft:chest")`。原版会写 `minecraft:trapped_chest` / `minecraft:barrel`。
- 实际影响小：重载时读取逻辑只看 `Items` 列表（行 6281-6291），不依赖 id；但 block entity 类型与方块不一致，第三方工具/调试会误判。

**D5. 容器物品无 per-item NBT（附魔/自定义数据丢失）—— 设计偏差**
- `ChestData.slots` 仅存 `int id + int count`（无 NBT 组件）。玩家背包有 `inventoryEnchants`/`inventoryPotion`，但**进入容器时强制清空 carried 元数据**（`NetworkHandler.java:3013-3017`，`setSlotItem` 注释明确"容器不保存 per-item 元数据"）。
- 结果：带附魔/药水/自定义名称的物品存入箱子后丢失附魔与自定义数据（仅保留 id/count）。原版容器完整保留物品 NBT。对生存玩法影响显著（附魔装备、命名牌等入库即"降级"）。

**D6. 奖励箱 LootTable 仅生成一次且无"已生成"标记持久化 —— 偏差**
- `NetworkHandler.java:6256-6279`：打开时若 block entity 含 `LootTable`，按种子生成物品并移除 `LootTable`/`LootTableSeed`（行 6276-6279）。机制本身正确（防重复生成）。
- 偏差点：生成后仅从内存 `chestData.slots` 持有，`loaded=true`；但若该区块被卸载且未触发关闭持久化，重载时 block entity 的 `LootTable` 已被抹去、而内存槽未写回 → 物品丢失（与 D7 同源）。

**D7. 持久化时机依赖"窗口关闭"，自动系统写入的内容可能不落盘 —— 偏差/风险**
- `persistChest` 仅在 `NetworkHandler.java:1150`（窗口关闭）被调用。漏斗/熔炉/酿造等自动 tick 直接改 `ContainerStore` 内存（`ContainerStore.java:256-264` 的 `moveOne` / `bumpVersion`），**不会回写区块 block entity**。
- 若容器内容被自动系统改动后、在玩家打开-关闭之前区块被保存/卸载，则保存的是旧（空或过期）block entity，内存中的物品在重启后丢失。

**D8. Count 截断到 127 —— 轻微偏差**
- `NetworkHandler.java:2778`：`Math.min(127, cnt)`。原版最大堆叠 64（含末影箱），127 不会超限但属于非原版取值（正常不会触发，仅防御性）。

---

## 4. Bug 清单（只记录不修）

| # | 严重度 | 文件:行号 | 现象 | 建议修复方向 |
|---|---|---|---|---|
| B1 | **严重** | `NetworkHandler.java:6244`（打开分支不含 `barrel`）；grep 确认全文件无 barrel 处理 | 木桶右键无法打开，玩家无法存取木桶内物品（漏斗可写但不可读） | 在右键打开分支增加 `"barrel".equals(name)`（或 `name.equals("barrel")`），复用与 chest 相同的 27 格打开/持久化路径；`dropContainerContents` 已能掉落（经 `removeChest`），无需改掉落 |
| B2 | 严重 | `ContainerStore.java:230`（trapped_chest 等同 chest）；缺失打开计数 | 陷阱箱打开无红石信号，与普通箱无异 | 维护每陷阱箱的查看者计数（`ContainerStore` 增加 `viewers` 计数），打开/关闭时调用红石更新（向自身及下方 `updateNeighborsAt`），信号强度 = `clamp(viewers,0,15)` |
| B3 | 中等 | `ContainerStore.java:81-83` + `NetworkHandler.java:6322`（始终 27 格 / generic_9x3） | 双箱不合并，两箱独立 27 格、互不共享 | 打开箱子时检测水平相邻（北/东）同类箱，合并为 54 格（generic_9x6），用一对 Pos 共享同一 `ChestData` |
| B4 | 中等 | `NetworkHandler.java:3013-3017`、`ContainerStore.java:14-18`（仅 id+count） | 带附魔/药水/自定义数据的物品存入容器后元数据丢失 | 容器槽增加 NBT 组件存储（或复用玩家背包的 `enchants/potion` 结构），取放时保留 |
| B5 | 中等 | `NetworkHandler.java:1150`（仅关闭时持久化）；`persistChest` 不被自动 tick 调用 | 漏斗/熔炉等自动写入容器的物品，若未触发打开-关闭即保存，重启后可能丢失 | 在 `ContainerStore.tick` 各 `bumpVersion` 后，对 `version` 变化的容器在合适时机（如区块保存钩子）回写 block entity，或世界保存时统一 flush 内存容器到 block entity |
| B6 | 轻微 | `NetworkHandler.java:2789`（id 硬编码为 chest） | 陷阱箱/木桶的 block entity id 写作 chest，类型与方块不符 | 按当前方块名写对应 block entity id（chest/trapped_chest/barrel） |
| B7 | 轻微 | `NetworkHandler.java:6256-6279`（LootTable 删除但在 D5/D7 路径下可能丢） | 奖励箱若未关闭即卸载，物品可能丢失（同 B5） | 同 B5：在生成物品后立即 `persistChest` 一次，或在区块卸载前 flush |

---

## 5. 结论与优先级建议

**整体评价**：单箱/末影箱/熔炉/漏斗/酿造台等核心存储功能可用，内存模型清晰（id+count 打包、按 Pos 索引）。但与原版存在若干**功能性缺失**，其中**木桶不可用（B1）**与**陷阱箱无红石（B2）**是生存玩法层面的硬伤，应优先修复。

**优先级建议**：
1. **P0（立即）**：B1 木桶打开 —— 一行分支条件即可恢复可用的木桶存储，影响面大。
2. **P0/P1**：B2 陷阱箱红石 —— 若服务器有红石机关玩法，必须实现查看者计数 + 信号输出。
3. **P1**：B3 双箱合并 —— 影响存储容量预期，建议实现。
4. **P2**：B4 物品元数据 —— 影响附魔装备/命名物品，建议实现 NBT 级存储。
5. **P2**：B5 自动系统写入持久化 —— 数据丢失风险，建议统一 flush 钩子。
6. **P3**：B6/B7 轻微一致性问题。

> 注：本报告所有结论均基于 `grep`/`Read` 当前源码与 `mapping` 原版反编译对照，未运行服务器、未编译、未改动任何 `.java` 文件。
