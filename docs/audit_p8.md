# P8 生电 / 技术向系统审计（红石 / 活塞 / 漏斗 / 观察者 / 粘液块 / 蜂蜜块 / 音符盒 / 矿车 / 铁轨）

> 审计对象：自研 MC 1.21.11 (Protocol 774) 核心，`com.yanrong.server`
> 核心文件：`../src/main/java/com/CharunCore/server/world/RedstoneEngine.java` (827 行)、`../src/main/java/com/CharunCore/server/world/ContainerStore.java`（漏斗传输）、`../src/main/java/com/CharunCore/server/utils/BlockManager.java`（仅方块定义/硬度数据，无红石行为）
> 对照源：`mapping/remapped_server_1.21.11.jar.src/net/minecraft/`（反编译原版）
> 审计方式：仅读取源码 + 反编译对照，**未修改任何 `.java` 文件**，未启动/编译服务器。
> 审计时间依据：本报告所有 `文件:行号` 均来自本次实际读取的代码（项目演进快，旧审计文档已过时，已重新 grep 核实）。

---

## 1. 实现状态概述

| 子项 | 状态 | 说明 |
|------|------|------|
| 红石线 `redstone_wire` | 部分实现 | 15 格衰减、`north/south/east/west` 形状、上下爬升连接大体正确；但**方向性供电/转角连接不完全**，且不把 repeater/comparator/observer/按钮/压力板当作电源 |
| 红石火把 `redstone_torch`/`redstone_wall_torch` | 部分实现 | 支撑块通电→熄灭（NOT 反转）已实现；但无 1 红石刻锁存/防振荡处理 |
| 红石方块 `redstone_block` | 已实现 | 恒定 15 信号，逻辑正确 |
| 中继器 `repeater` | 部分实现 | 有 `powered` 状态与延迟；但 **`DELAY`(1–4) 与 `LOCKED` 未实现**，且输出下游"死"（见 Bug #1） |
| 比较器 `comparator` | 部分实现 | compare/subtract 模式逻辑在；但**不读取容器模拟信号**，且输出下游"死"（见 Bug #1/#6） |
| 活塞 `piston`/`sticky_piston` | 部分实现 | 推动算法从 `PistonStructureResolver` 移植；但**无 `PistonMovingBlock` 动画实体、无计划刻时序、不携带实体、方块实体不可推、蜂蜜回拉错误** |
| 活塞臂 `piston_head`（移动中方块） | 缺失 | 仅放置静态 `piston_head` 方块，无 `MovingPistonBlock`/`PistonMovingBlockEntity`（无平滑移动、无实体携带） |
| 漏斗 `hopper` | 部分实现 | 基础"从上方抽 / 向 facing 推"物品已实现；但**无红石锁定(ENABLED)、无物品实体吸取、比较器读不到容器填充** |
| 观察者 `observer` | 部分实现 | 轮询检测前方方块变化并发出脉冲；但用**墙钟 100ms 而非 1 红石刻(2gt)**，且输出下游"死" |
| 粘液块 `slime_block` | 已实现 | 作为 sticky，粘连+拉取逻辑正确 |
| 蜂蜜块 `honey_block` | 部分实现 | 作为 sticky 可推；但 retract 时**错误地被拉回**（原版为 `PUSH_ONLY`，不可被拉） |
| 音符盒 `note_block` | 部分实现 | `powered` 状态已设置；但**完全不发声**（无 sound） |
| 矿车 `minecart` | 缺失 | 项目中无任何 Minecart 实体类（`grep Minecart` 在 `src/` 下零结果），无移动逻辑 |
| （动力/探测/激活）铁轨 | 缺失 | 仅 `powered_rail`/`activator_rail` 设置 `powered` 状态；无矿车→无加速/探测/激活效果 |
| 压力板 / 按钮 | 部分实现 | 压力板仅检测玩家（无 mob/物品实体）；且输出下游"死"（见 Bug #1） |

---

## 2. 关键文件与行号证据（自研核心）

- **`RedstoneEngine.java`**
  - 信号传播入口：`onBlockChanged(int,int,int)` 28–227（BFS，depth 初始 16）
  - 红石线功率：`calcWirePower` 233–253；`receivesAnyPower` 258–275
  - 红石线形状：`calcWireConnection` 301–326；`isRedstoneConnectable` 329–344
  - 电源枚举（正确但未被传播使用）：`providesPower` 637–653
  - 火把反转：`torchSupportPowered` 656–669
  - 比较器：`updateComparatorState` 695–714
  - 中继器：`updateRepeater` 717–728；`applyRepeater` 730–739
  - 观察者：`registerTracked` 752–756；`tick()` 中扫描 759–792；`applyObserverOff` 741–749
  - 活塞：`extendPiston` 589–600；`retractPiston` 602–618；`resolvePiston` 448–478；`pistonAddBlockLine` 480–532；`pistonApplyMove` 556–575；`pistonCanStick` 377–387；`pistonIsSticky` 372–375；`pistonPushReaction` 414–419（仅 NORMAL/DESTROY）；`pistonIsPushable` 421–436（435 行 `!pistonIsBlockEntity`）；`PISTON_BLOCKENTITY` 354–370
  - TNT：`tntScheduler` 113–137（2 秒延迟，3×3×3 简化爆炸）
  - 音符盒：`note_block` 分支 164–170（仅设 `powered`，无 sound）
  - 铁轨：`powered_rail`/`activator_rail` 分支 139–145（仅设 `powered`）
  - 主循环驱动：`Main.java:105`（ContainerStore.tick）、`Main.java:107`（RedstoneEngine.tick）
- **`ContainerStore.java`**
  - 漏斗传输：`tickHopper` 266–292；`moveOne` 247–264；`refAt` 226–236；`ContainerRef` 182–224
  - 漏斗 `transferCd = 8`：35、267–291
- **`BlockManager.java`**：仅 `blockHardness`/`harvestTools` 数据（124–274），**不含任何红石行为逻辑**。
- **实体目录** `world/entity/`：仅有 Arrow/EndCrystal/EndDragon/EnderPearl/Entity/ExperienceOrb/EyeOfEnder/ItemEntity/LivingEntity/MobEntity/Pathfinder——**无 Minecart、无 PistonMoving 实体**。

---

## 3. 对照原版的关键差异 / 偏差（逐条，附 文件:行号）

### 3.1 【核心架构缺陷】传播函数未使用 `providesPower()`，漏掉 repeater/comparator/observer/button/plate 作为电源
- 自研：`calcWirePower`（233–253）与 `receivesAnyPower`（258–275）各自硬编码了 `lever / redstone_wire / redstone_block / redstone_torch`，**未包含** `repeater`、`comparator`、`observer`、`*_button`、`*_pressure_plate`、`weighted_pressure_plate`。
- 自研已有正确的 `providesPower`（637–653）枚举了上述全部器件，但**仅被 `torchSupportPowered`（668）调用**，从未进入信号传播。
- 原版：`RedStoneWireBlock.getSignal`（359–372）、`DiodeBlock` 等通过统一的信号系统给相邻器件供电。
- 后果：repeater/comparator/observer/按钮/压力板的输出**无法点亮相邻红石线、无法驱动活塞/门/灯/漏斗**（见 Bug #1）。这是整个生电系统最大的阻断性缺陷。

### 3.2 红石线方向性供电与转角连接为近似实现
- 自研 `receivesAnyPower`（258–275）和方向输出判断为**全方向**（任意 6 邻居）。原版 `RedStoneWireBlock.getSignal`（359–372）只在"连接方向或上方"输出，且 `getIncomingWireSignal`（`RedstoneWireEvaluator.java:27–45`）含转角上下爬升逻辑。
- 自研 `calcWireConnection`（301–326）有 up/down 爬升近似，但未实现**两线经公共实体方块转角相连（corner connection）**的功率传递，复杂红石线网络（环、并行线互不串扰）行为可能偏差。

### 3.3 中继器：`DELAY` 与 `LOCKED` 未实现，延迟用墙钟
- 自研 `updateRepeater`（717–728）固定 `System.currentTimeMillis() + 100`(≈1 红石刻) 后翻转，**忽略 `DELAY`(1–4 红石刻)**；无锁定逻辑。
- 原版 `RepeaterBlock.getDelay = DELAY*2`（49–51，即 2/4/6/8 gt），且 `isLocked`（72–74，由 `getAlternateSignal` 检测侧向 repeater/torch）实现锁定。

### 3.4 比较器：不读取容器模拟输出信号
- 自研 `updateComparatorState`（695–714）back 输入来自 `inputLevelAt`（仅红石电平），**未读取背后容器的填充等级**（`getAnalogOutputSignal`）。
- 原版 `ComparatorBlock.getInputSignal`（104–129）当正面方块有 `hasAnalogOutputSignal` 时取容器模拟信号——这是**物品分类机（item sorter）**的核心机理。原版 compare/subtract 模式（`calculateOutputSignal` 70–86）逻辑与自研一致，但缺容器读取。
- 另：自研未对比较器输出做 2gt 计划刻延迟（`ComparatorBlock` 169、198–200 用 `scheduleTick(...,2)`）。

### 3.5 活塞：缺少 MovingPiston 实体、计划刻时序、实体携带
- 自研 `extendPiston`/`retractPiston`（589–618）在 `onBlockChanged` 中**同步瞬时**完成推动（`pistonApplyMove` 556–575），无 `MovingPistonBlock`/`PistonMovingBlockEntity`，无 2gt(伸)/1gt(缩) 计划刻。
- 原版 `PistonBaseBlock.moveBlocks`（287+）→ `PistonStructureResolver` + `PistonMovingBlockEntity`：分 tick 平滑移动并**携带骑乘实体**。
- 自研 `pistonApplyMove` 不移动任何实体（玩家/生物/掉落物站在被推方块上不会被带动）；被推掉落物也不处理。

### 3.6 活塞：方块实体（箱子/熔炉/漏斗等）全部不可推
- 自研 `pistonIsPushable`（421–436）末尾 `return !pistonIsBlockEntity(n);`（435），`PISTON_BLOCKENTITY`（354–370）把 chest/furnace/hopper/dispenser/dropper/brewing_stand 等**全部判为不可推**。
- 原版 `PistonBaseBlock.isPushable`（284 行 `return !paramBlockState.hasBlockEntity();`）——**带方块实体的方块默认可推**（仅 obsidian/end_portal 等经前面分支排除）。
- 后果：活塞推不动箱子/熔炉/漏斗等；即便强行推，自研只复制 `stateId` 不迁移 `ContainerStore` 数据，会导致箱子物品遗留在旧坐标（数据错位）。

### 3.7 活塞：蜂蜜块 retract 被错误拉回（应为 PUSH_ONLY）
- 自研 `pistonPushReaction`（414–419）只返回 `RXN_NORMAL`/`RXN_DESTROY`，**常量 `RXN_PUSH_ONLY` 从未被返回**；`pistonCanStick`/`pistonIsSticky` 把 honey 与 slime 同等对待为 sticky。因此粘性活塞 retract 时蜂蜜结构被拉回。
- 原版：honey 的 `PushReaction = PUSH_ONLY`，`PistonBaseBlock.isPushable`（278–279）`case PUSH_ONLY: return paramDirection1 == paramDirection2;`——即**仅在"沿活塞方向推"时可动，回拉（opposite 方向）不可动**。故原版粘性活塞 retract **不会拉回蜂蜜块**（这是蜂蜜块被生电广泛使用的根本特性）。

### 3.8 观察者：轮询 + 墙钟时间，非精确 1 红石刻脉冲
- 自研 `tick()` 每主循环扫描 `trackedObservers`（773–792），比较"前方方块 stateId 是否变化"，变化则置 `powered=true` 并排程 `observer_off`（`System.currentTimeMillis()+100`）。
- 原版 `ObserverBlock`：通过 `updateShape`（64–70）在**前方方块发生 block update 时**触发，`tick`（53–61）发 2gt 脉冲（`scheduleTick(...,2)`）后熄灭，`getSignal`（98–103）仅向 `facing` 方向输出 15。
- 偏差：① 自研基于"stateId 变化"轮询，可能漏掉一 tick 内来回翻转、或对"无状态变化但触发更新"的变更不敏感；② 脉冲长度用墙钟 100ms 而非精确 1 红石刻，服务器卡顿时失准；③ 输出方向未建模（且因 Bug #1 输出本身下游也读不到）。

### 3.9 音符盒：完全不发声
- 自研 `note_block` 分支（164–170）仅在收到信号时设置 `powered` 状态，**无任何 `playSound` / 音符粒子 / instrument 计算**。
- 原版 `NoteBlock.neighborChanged`（81–90）上升沿调用 `playNote`→`triggerEvent`（135–159）按 instrument+note 播放对应音高声音；instrument 由上/下方方块决定（`setInstrument` 54–63）。

### 3.10 漏斗：缺红石锁定（ENABLED）、缺物品实体吸取
- 自研 `tickHopper`（266–292）无条件传输，无 `enabled`/被红石供能即禁用的判断。
- 原版 `HopperBlockEntity.tryMoveItems`（113–136）首行 `&& ((Boolean)state.getValue(HopperBlock.ENABLED))`（118）——**收到红石信号时漏斗停用**。
- 原版另有 `suckInItems` 吸取上方物品实体（掉落物），自研 `moveOne` 仅容器→容器，无实体吸取。
- 自研漏斗 8 tick 冷却（`transferCd=8`，267/274/291）与原版 `MOVE_ITEM_SPEED=8`（HopperBlockEntity 33、cooldown 129）一致——速率正确。
- 注：比较器读不到漏斗填充（因 Bug #1 + 3.4），物品分类机的"比较器读容器"环节断裂。

### 3.11 矿车 / 铁轨：整体缺失
- `grep -rn "Minecart" src/main/java` 零结果；`world/entity/` 无 Minecart 类，**无矿车实体、无移动、无碰撞**。
- `powered_rail`/`activator_rail` 仅在 `onBlockChanged`（139–145）被设置 `powered` 状态，但无矿车可加速/激活；`detector_rail` 仅在 `isRedstoneConnectable`（336）列为可连，无探测逻辑。
- 原版：`BaseRailBlock`/`PoweredRailBlock`/`Minecart` 实体在轨上按速度/动力轨加速、探测轨触发比较器等。

### 3.12 TNT 爆炸为简化模型
- 自研（105–138）：2 秒后移除自身 + 周围 3×3×3（仅排除 bedrock/obsidian）瞬时清除，无爆心随机、无浮力、无方块掉落、无对实体伤害。
- 原版：`TntBlock`/`Explosion` 有半径~4、按爆炸抗性决定方块是否被毁、掉落物、实体伤害与击退。

---

## 4. Bug 清单（仅记录，不修复）

> 严重程度：严重（阻断多数生电玩法）/ 中等（部分 contraption 失败或数据错误）/ 轻微（边缘/近似）

### Bug #1 —【严重】repeater/comparator/observer/按钮/压力板 输出无法驱动任何下游
- 文件:行号：`RedstoneEngine.java:233–253`（calcWirePower）、`258–275`（receivesAnyPower）；正确但未使用的 `providesPower` 在 `637–653`
- 现象：中继器、比较器、观察者、按钮、压力板被点亮/激活后，相邻红石线不亮、活塞/门/灯/漏斗不响应，整条"进阶红石"链路中断。
- 建议修复方向：让 `calcWirePower` 与 `receivesAnyPower`（及所有邻居求值）统一走 `providesPower()`；对红石线取 `power` 值，对方向性器件按 `facing` 只向输出方向供强电。

### Bug #2 —【严重】漏斗无红石锁定（ENABLED）
- 文件:行号：`ContainerStore.java:266–292`（tickHopper 无 enabled 判断）
- 现象：被红石供能的漏斗仍持续传输，物品分类机等依赖"比较器→红石→锁漏斗"的电路失效/溢出。
- 建议修复方向：在 `tickHopper` 开头读取漏斗 `enabled` 属性（红石供能则 `enabled=false`），禁用时直接 `return`。

### Bug #3 —【中等】活塞无 PistonMovingBlock 实体 / 瞬时推动 / 不携带实体
- 文件:行号：`RedstoneEngine.java:556–575`（pistonApplyMove）、`589–618`（extend/retract 同步执行）
- 现象：活塞瞬间到位，无 2gt/1gt 动画时序；站在被推方块上的玩家/生物/掉落物不被带动；依赖活塞时序的 contraption（活塞门、0 刻、QC）行为偏离原版。
- 建议修复方向：引入 `PistonMovingBlock`/移动计划刻，分 tick 移动并携带实体；用游戏 tick 而非同步执行。

### Bug #4 —【中等】活塞回拉时错误拉回蜂蜜块（应为 PUSH_ONLY）
- 文件:行号：`RedstoneEngine.java:414–419`(pistonPushReaction 无 PUSH_ONLY)、`377–387`(pistonCanStick)、对照 `PistonBaseBlock.java:278–279`
- 现象：粘性活塞 retract 会拉回蜂蜜结构，原版不会（蜂蜜不可被拉）。 flying machine / 蜂蜜相关 contraption 行为错误。
- 建议修复方向：`pistonPushReaction` 对 honey_block 返回 `RXN_PUSH_ONLY`，并在 `pistonIsPushable` 按 `pushDir==pistonDir` 判定（对齐原版 278–279）。

### Bug #5 —【中等】活塞把所有方块实体判为不可推（箱子/熔炉/漏斗等）
- 文件:行号：`RedstoneEngine.java:435`(`!pistonIsBlockEntity`)、`354–370`(PISTON_BLOCKENTITY)；对照 `PistonBaseBlock.java:284`(`!hasBlockEntity`)
- 现象：活塞推不动 chest/furnace/hopper/dispenser/dropper 等；即便强推也只复制 stateId、不迁移 `ContainerStore` 数据，导致物品遗留在旧坐标。
- 建议修复方向：除 bedrock/obsidian/end_portal 等真正不可推者外，允许带方块实体的方块被推；推动时一并迁移对应 `ContainerStore` 数据。

### Bug #6 —【中等】比较器不读取容器模拟信号
- 文件:行号：`RedstoneEngine.java:695–714`（updateComparatorState 仅 `inputLevelAt`）；对照 `ComparatorBlock.java:104–129`
- 现象：比较器背后放箱子/漏斗时不会输出填充等级信号，物品分类机/存储检测电路不可用。
- 建议修复方向：back 输入在正面方块 `hasAnalogOutputSignal` 时取 `getAnalogOutputSignal`（实现各容器的填充等级计算）。

### Bug #7 —【中等】中继器忽略 DELAY 与 LOCKED
- 文件:行号：`RedstoneEngine.java:717–728`（固定 100ms，无 DELAY/LOCKED）；对照 `RepeaterBlock.java:49–51,72–74`
- 现象：1–4 红石刻延迟档位全部无效；侧边 repeater/torch 锁定失效。
- 建议修复方向：按 `DELAY` 属性计算延迟（DELAY*2 gt，用游戏 tick 调度）；实现 `isLocked`（读取侧向 alternate signal）。

### Bug #8 —【中等】观察者用墙钟 100ms 而非 1 红石刻(2gt)，且为轮询
- 文件:行号：`RedstoneEngine.java:759–826`（tick 扫描 + `now+100`）；对照 `ObserverBlock.java:53–61`
- 现象：脉冲长度受服务器 TPS 影响，可能漏/重触发；与原版基于 block update 的精确 2gt 脉冲不一致。
- 建议修复方向：用游戏 tick 计划刻实现精确 2gt 脉冲；改为基于"前方方块发生 block update"触发而非每 tick 比对 stateId。

### Bug #9 —【中等】音符盒不发声
- 文件:行号：`RedstoneEngine.java:164–170`（仅设 powered）；对照 `NoteBlock.java:81–97,135–159`
- 现象：红石触发音符盒无任何声音/粒子，音乐/技术向音符盒电路不可用。
- 建议修复方向：上升沿调用发声（按 instrument+note 计算音高），并据上/下方方块决定 instrument。

### Bug #10 —【中等】时序使用墙钟 `System.currentTimeMillis()` 而非游戏 tick
- 文件:行号：`RedstoneEngine.java:725`(repeater)、`790`(observer_off)；对照原版 `scheduleTick(...,2)`
- 现象：repeater/observer 延迟随服务器性能漂移，低 TPS 时红石比世界"跑得快"，时序 contraption 不可复现。
- 建议修复方向：所有延迟改为基于游戏 tick 计数（在 `tick()` 中按 `tickCount` 到期判断）。

### Bug #11 —【中等】矿车 / 铁轨整体缺失
- 文件:行号：`world/entity/` 无 Minecart；`RedstoneEngine.java:139–145` 仅设 `powered`
- 现象：动力/探测/激活轨无车可作用；矿车运输、探测轨触发、激活轨效果全部不存在。
- 建议修复方向：实现 Minecart 实体与轨道移动（BaseRailBlock/PoweredRailBlock 逻辑）、探测轨→比较器信号、激活轨→实体效果。

### Bug #12 —【轻微】红石线方向性/转角连接为近似
- 文件:行号：`RedstoneEngine.java:258–275,301–326`；对照 `RedStoneWireBlock.java:359–372`、`RedstoneWireEvaluator.java:27–45`
- 现象：线对全方向供"弱电"近似，复杂并行走线/转角连接可能与原版有细微差异。
- 建议修复方向：按连接方向输出、实现 corner connection 功率传递。

### Bug #13 —【轻微】压力板仅检测玩家，不检测 mob/物品实体
- 文件:行号：`RedstoneEngine.java:817–825`（entityOnPlate 仅遍历 `NetworkHandler.players`）；且因 Bug #1 输出下游读不到
- 现象：生物踩压力板无效；且该输出因 Bug #1 本就失效。
- 建议修复方向：纳入 mob/物品实体检测；先修 Bug #1。

### Bug #14 —【轻微】观察者/压力板 tracked 集合不持久化
- 文件:行号：`RedstoneEngine.java:624–626`（`trackedObservers`/`trackedPlates` 仅内存）
- 现象：服务器重启后观察者/压力板需等下次变化才重新生效；`observerLastSeen` 重置可能重启瞬间误触发一次。
- 建议修复方向：随区块加载注册 tracked；或在关服时持久化。

### Bug #15 —【轻微】TNT 爆炸为简化模型
- 文件:行号：`RedstoneEngine.java:105–138`
- 现象：3×3×3 瞬时清除、无抗性/掉落/实体伤害，与原版爆炸差异大。
- 建议修复方向：按爆炸抗性判定、生成掉落物、对实体造成伤害与击退（爆炸半径按原版）。

---

## 5. 结论与优先级建议

生电/技术向系统的**主干被两个严重 Bug 阻断**：

1. **第一优先级（严重，必须修）**
   - **Bug #1**：传播函数未走 `providesPower()`，导致 repeater/comparator/observer/按钮/压力板全部"输出死"。这是生电系统能否工作的前提，单点修复收益最大。
   - **Bug #2**：漏斗红石锁定（ENABLED）。物品分类机/红石控制漏斗依赖它。

2. **第二优先级（中等，决定"像不像原版"）**
   - Bug #3/#4/#5（活塞时序+蜂蜜拉取+方块实体可推）：活塞是生电中枢，当前实现能推简单方块但时序与多类方块错误，建议引入 PistonMovingBlock 计划刻并修正 honey/方块实体规则。
   - Bug #6/#7/#8/#9：比较器读容器、中继器 DELAY/LOCK、观察者精确脉冲、音符盒发声——决定存储系统、计时电路、音乐/技术装置可用。
   - Bug #11：矿车/铁轨若纳入 P8 范围则需整体实现（实体 + 轨道逻辑）。

3. **第三优先级（轻微，边缘近似）**
   - Bug #12–#15：红石线方向性、压力板实体检测、tracked 持久化、TNT 爆炸模型。

**对生存玩法的影响判断**：
- 对**普通生存**（拉杆点灯、红石火把、红石方块、简单红石线、单活塞门、漏斗单向传输）：基本可用（受 Bug #2 影响的部分红石控制漏斗除外）。
- 对**生电/技术向**（中继器延时电路、比较器存储、观察者脉冲、物品分类机、活塞时序 contraption、蜂蜜飞行机、矿车运输、音符盒音乐）：**当前基本不可用**，需先解决 Bug #1/#2，再依次处理 #3–#11。

> 备注：本次审计未修改任何源码，所有结论基于实际读取的 `RedstoneEngine.java`、`ContainerStore.java`、`BlockManager.java` 及 `mapping/...` 反编译原版。行号对应审计时文件内容，后续代码演进需重新核实。
