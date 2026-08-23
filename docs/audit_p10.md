# 审计 P10：流体系统（水 / 岩浆 / 原石 / 黑曜石）

> 审计范围：流体流动（源方块、流动等级、蔓延）、水+岩浆接触生成原石（cobblestone）/ 黑曜石（obsidian）反应。
> 审计铁律：仅读取 + 本报告，**未修改任何源码**。所有行号来自本次实际读取。
> 关键源文件：`../src/main/java/com/CharunCore/server/world/FluidEngine.java`
> 原版对照：`mapping/remapped_server_1.21.11.jar.src/net/minecraft/world/level/material/{FlowingFluid,LiquidBlock}.java`、`.../world/level/block/{LiquidBlock,WaterFluid,LavaFluid}.java`（注：`FlowingFluid.java` 当前反编译为 `// INTERNAL ERROR`，故以 `LiquidBlock`/`WaterFluid`/`LavaFluid` 及已知原版语义 + 本核心代码行为对照为准）

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|---|---|---|
| 流体定时 tick（单线程调度） | 已实现 | `FluidEngine.scheduleFluidTick` / `tick` |
| 区块加载时扫描已有水/岩浆并纳入引擎 | 已实现 | `scheduleChunkFluids`（按区块去重） |
| 向下流动 + 水平蔓延 + 流动等级递减 | 已实现 | `processFluid` |
| 水+岩浆 → 原石/黑曜石 反应 | 已实现（规则正确） | 见 §3 R1 |
| 水桶/岩浆桶放置触发流体 | 已实现 | `NetworkHandler` 调用 `scheduleFluidTick`（行 751/805） |
| 邻居方块变化后流体重新蔓延 | **缺失（严重）** | 见 §3 R2 / §4 B1 |
| `activeFluids` 集合回收 | **缺失（内存泄漏）** | 见 §4 B2 |
| 无限水（infinite water）生成 | **缺失** | 见 §3 R3 |
| 流体随方块更新触发 neighbor 重算 | 部分 | 仅 spread 内自我调度 |

---

## 2. 关键文件与行号证据

### 2.1 调度与数据结构（FluidEngine.java）
- `FluidEngine.java:10-16` — `ScheduledExecutorService` 单线程；`activeFluids`（去重集合）、`pendingFluids`（本轮待处理）、`scannedChunks`（区块去重）、`MAX_ACTIVE_FLUIDS=600000`。
- `FluidEngine.java:18-23` — `scheduleFluidTick`：`activeFluids.add(key)` 去重；**加入后不再移除**。
- `FluidEngine.java:30-48` — `scheduleChunkFluids`：扫描区块内 `water`/`lava`，逐格调度（仅扫描一次）。
- `FluidEngine.java:50-61` — `tick()`：复制 `pendingFluids` 处理，**处理成功后不将当前位置从 `activeFluids` 移除**。

### 2.2 流动与反应（FluidEngine.java:63-133）
- `FluidEngine.java:74` — 6 方向邻居（含上下）。
- `FluidEngine.java:75-86` — **水方块的 tick**：邻居是岩浆时，按"岩浆是否源方块"决定产物——`lavaSource ? obsidian : cobblestone`（行 79-80）。
- `FluidEngine.java:87-98` — **岩浆方块的 tick**：邻居是水时，按"本岩浆是否源方块"决定产物（行 91-92）。
- `FluidEngine.java:100-103` — 读取 `level`；`isSource = (level==0)`；`maxLevel = lava?3 : 7`。
- `FluidEngine.java:105-118` — **向下流动**：`below==0`（空气）时，在下方放置流体——若是源则保持 `level=0`，否则也写为 `level=0`（行 112-113）。新块加入调度。
- `FluidEngine.java:120-132` — **水平蔓延**：`level < maxLevel` 时向 4 个水平邻居的**空气**格放置 `level = isSource?1 : level+1` 的新流体并调度。

### 2.3 调度触发点（全局 grep `scheduleFluidTick`/`scheduleChunkFluids`）
- `NetworkHandler.java:751` — 放置水桶 → `scheduleFluidTick`。
- `NetworkHandler.java:805` — 放置岩浆桶 → `scheduleFluidTick`。
- `NetworkHandler.java:5109 / 6614 / 6637` — 区块加载/生成 → `scheduleChunkFluids`。
- `FluidEngine.java:116,129` — 自身蔓延时调度新块。
- **`WorldManager.setBlock`（`WorldManager.java:357,458`）不调度任何邻居流体**（grep `world/WorldManager.java` 中 `setBlock` 定义未见 `scheduleFluid` 调用）。

---

## 3. 对照原版的关键差异 / 偏差（逐条）

**R1. 水+岩浆反应规则正确（与原版一致）**
- 原版规则：岩浆为**源方块**遇水（源或流动）→ 黑曜石；岩浆为**流动**遇水 → 原石（`LavaFluid`/`LiquidBlock` 反应逻辑）。
- 本核心两侧（水 tick 与岩浆 tick）均按"**对方（或自身，岩浆侧）是否为源**"判定：`lavaSource ? obsidian : cobblestone`（`FluidEngine.java:80,92`），并将产物写在**岩浆**所在格（水侧行 81、岩浆侧行 93），符合原版"岩浆被消耗转化"的语义。✅ 此部分正确。

**R2. 邻居方块变化后流体不重新蔓延 —— 严重偏差**
- 原版：当方块被放置/破坏导致相邻流体可流动时，流体方块会被重新调度（`LiquidBlock`/`Block` 的 `neighborChanged`/`onPlace` 触发 fluid tick）。
- 本核心流体**只在三种情况被调度**：① 桶放置（行 751/805）；② 区块加载扫描（行 5109 等）；③ 自身 spread 时（行 116/129）。**`WorldManager.setBlock` 不触发任何流体重调度**（§2.3）。
- 后果：玩家挖开紧贴水/岩浆的方块后，水/岩浆**不会流入新产生的空腔**，表现为"流体是静止的、不流动"，与原版动态流体完全不符。这是流体系统最关键的偏差。

**R3. 无无限水（infinite water source）机制 —— 偏差**
- 原版：两个及以上水**源**方块相邻（如 2×2 凹槽中放两桶水）会生成第三个水源，形成可无限取水的泉眼。
- 本核心 `processFluid` 没有任何"多源合并生成新源"的逻辑，只做向下/水平蔓延。因此无法制作无限水，取水会耗尽。属**功能缺失**（原版生存必备机制）。

**R4. 向下流动一律生成 level=0 源方块 —— 与原版"向下即成源"一致，但叠加 R2 导致后果放大**
- 原版水流向下进入空气同样生成**源**方块（这就是水柱到底成泉的原理），本核心行 112-113 行为一致 ✅。
- 但注意：本核心水向下流动时 `downState` 在 `isSource` 分支保持 `currentState`（`level=0`），在 `!isSource` 分支显式 `withProp(level,"0")`——**流动水向下也变成源**（行 113）。原版流动水向下同样成源，故一致。该行为本身正确，但其正确性被 R2（无法重新蔓延）掩盖：一旦流体被"卡住"不动，坑底也不会再被填充。

**R5. 蔓延判定只看"空气（state==0）"，未考虑可替换流体/下落/可被流体冲走方块 —— 偏差**
- `FluidEngine.java:108` 向下：`below == 0`；行 126 水平：`== 0`。原版 `canDisplace` 允许流体置换空气与若干可替换方块（如 `REPLACEABLE`/某些植物），并会避让同种/更高优先级流体。本核心只认纯空气，遇到非空气格直接跳过（例如水遇到另一格水不再推进，符合；但遇到如 `short_grass` 等可替换植物不会冲走）。属轻度偏差。

**R6. 流动等级语义简化（0..7 / 0..3） —— 设计偏差**
- 原版水 blockstate `level` 0=源，1..7=流动（仅奇数位有效 + `falling` 标志，内部用 0..15 编码）；岩浆同理 0..3（下界岩浆流动更远，原版按维度区分）。
- 本核心用连续 `0..7`（水）/ `0..3`（岩浆）线性递减（行 103,122），**下界岩浆流动距离未按维度区分**（原版下界岩浆可流 4 远、且不会生成原石/黑曜石于下界流体反应）。属简化偏差，不影响主世界正确性，但下界岩浆行为不对。

**R7. 流体 tick 为单线程 ScheduledExecutor + 每 tick 全量扫描 pendingFluids —— 性能风险**
- `tick()`（行 50-61）每游戏 tick 复制并遍历整个 `pendingFluids`。配合 R2（无法重新蔓延）通常不会无限增长，但大面积水体（如海洋）在区块加载时一次性调度数百万格（受 `MAX_ACTIVE_FLUIDS` 限制，行 21），可能造成启动/加载时卡顿。属性能注意项，非逻辑 Bug。

---

## 4. Bug 清单（只记录不修）

| # | 严重度 | 文件:行号 | 现象 | 建议修复方向 |
|---|---|---|---|---|
| B1 | **严重** | `FluidEngine.java:18-23,50-61`（调度的唯一入口）；`WorldManager.java:357,458`（setBlock 不重调度）；`NetworkHandler.java:610-612`（破坏方块不调度邻居流体） | 玩家挖开/放置方块后，相邻水/岩浆不再流动，流体表现为静止 | 在 `WorldManager.setBlock` / 破坏方块路径中，对受影响格的 6 邻居若是水/岩浆则调用 `FluidEngine.scheduleFluidTick`；或仿原版在 `neighborChanged` 中重调度 |
| B2 | 中等 | `FluidEngine.java:20`（activeFluids.add 后无 remove） | `activeFluids` 集合只增不减，每个曾被 tick 的流体格永久驻留，长期运行内存持续增长（泄漏） | 在 `tick()` 处理完成后从 `activeFluids` 移除该 key（与 `pendingFluids` 解耦：用 pending 驱动，active 仅用于去重可改为短时去重或处理后移除） |
| B3 | 中等 | `FluidEngine.java:63-133`（无多源合并逻辑） | 无法生成无限水，取水耗尽；失去原版生存基础机制 | 在水 tick 时检测相邻是否有两个及以上水**源**（如 2×2 水环绕的空格），则在该空格生成水**源** |
| B4 | 轻微 | `FluidEngine.java:108,126`（仅 `==0`） | 水/岩浆不会冲走可替换方块（矮草等），与原版 `canDisplace` 行为略有出入 | 参考原版 `canDisplace`：对 `REPLACEABLE`/可流体替换方块也允许流入/冲走 |
| B5 | 轻微 | `FluidEngine.java:103`（岩浆 maxLevel 固定 3，未区分维度） | 下界岩浆流动距离/流体反应与原版下界不一致 | 按维度设置岩浆流动等级与反应规则（下界岩浆不生成黑曜石/原石） |
| B6 | 轻微/性能 | `FluidEngine.java:50-61`（每 tick 全量遍历 pendingFluids） | 大范围水体加载时 pending 巨大，可能引发卡顿 | 限制每 tick 处理预算（如分批），或对已稳定的流体块避免重复入队 |

---

## 5. 结论与优先级建议

**整体评价**：流体反应的**产物判定（原石/黑曜石）是正确的**（R1），单线程调度骨架可用。但系统存在**一个严重逻辑缺陷（B1：邻居变化后流体不重新蔓延）**，使得整个流体系统在实际游玩中"不会流动"，这是 P10 最需要修复的问题。其次 `activeFluids` 内存泄漏（B2）在长期运行服务器上会累积。

**优先级建议**：
1. **P0（立即）**：B1 —— 在 `WorldManager.setBlock` 与方块破坏路径中加入邻居流体重调度，否则水/岩浆完全不流动。
2. **P1**：B2 —— 修复 `activeFluids` 永不回收，防止长期内存泄漏。
3. **P1/P2**：B3 —— 实现无限水，恢复生存基础玩法。
4. **P3**：B4/B5/B6 —— 可替换方块/维度差异/性能，按需完善。

> 注：本报告对照时 `mapping/.../material/FlowingFluid.java` 反编译为 `// INTERNAL ERROR`，已结合 `LiquidBlock`/`WaterFluid`/`LavaFluid` 及原版已知语义进行比对；反应规则以本核心 `FluidEngine.java:80,92` 实际代码为准进行正确性判定。未运行/未编译/未改动任何源码。
