# 审计 P11：种植系统（作物 / 灌溉 / 开垦）

> 审计范围：随机刻引擎（作物生长速率、成熟阶段、光照/hydration 条件）、耕地（FarmBlock）开垦与灌溉（水源 4 格内）、踩踏退化、作物（小麦/胡萝卜/马铃薯/甜菜）、甘蔗、仙人掌、树苗生长。
> 审计铁律：仅读取 + 本报告，**未修改任何源码**。所有行号来自本次实际读取。
> 关键源文件：`../src/main/java/com/CharunCore/server/world/RandomTickEngine.java`、`../src/main/java/com/CharunCore/server/utils/BlockManager.java`
> 原版对照：`mapping/remapped_server_1.21.11.jar.src/net/minecraft/world/level/block/{CropBlock,FarmBlock,SugarCaneBlock,CactusBlock}.java`

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|---|---|---|
| 随机刻引擎（玩家周边区块） | 已实现 | `RandomTickEngine.tick` |
| 作物 age 增长（小麦/胡萝卜/马铃薯/甜菜/地狱疣/可可/瓜藤） | 已实现（过快 + 无环境约束） | `tickCrop` |
| 作物最大 age 表 | 已实现且值正确 | `CROP_MAX_AGE`（小麦7/甜菜3/可可2/瓜藤7…） |
| 甘蔗 / 仙人掌 / 竹子向上生长 | 已实现（过快 + 无邻接约束） | `tickUpwardPlant` |
| 树叶自然凋零 | 已实现（近似） | `tickLeavesDecay` |
| 耕地（FarmBlock）moisture hydration | **缺失** | 见 §3 G1 |
| 耕地踩踏退化（turnToDirt） | **缺失** | 见 §3 G2 |
| 作物对光照要求（夜间不生长） | **缺失** | 见 §3 G3 |
| 作物必须种在耕地 + 失效弹落 | **缺失** | 见 §3 G4 |
| 甘蔗需邻水 / 仙人掌需邻空格 | **缺失** | 见 §3 G5 |
| 瓜类成熟后生成果实（西瓜/南瓜方块） | **缺失** | 见 §3 G6 |
| 树苗长成树 | **缺失** | 见 §3 G7 |
| 藤蔓生长 | **缺失** | 见 §3 G8 |

---

## 2. 关键文件与行号证据

### 2.1 随机刻引擎（RandomTickEngine.java）
- `RandomTickEngine.java:25-51` — `tick()`：遍历在线玩家，对玩家周边 **±2 区块（5×5）**、每区块 **3 次**随机刻（`RND.nextInt(16)` 选 x/z，`py + RND.nextInt(33) - 16` 选 y）。
- `RandomTickEngine.java:18-21` — `CROP_MAX_AGE`：wheat/carrots/potatoes=7，beetroots=3，nether_wart=3，cocoa=2，melon_stem/pumpkin_stem=7。
- `RandomTickEngine.java:53-70` — `randomTick` 分发：作物 → `tickCrop`；cactus/sugar_cane/bamboo → `tickUpwardPlant`；`*_leaves` → `tickLeavesDecay`。
- `RandomTickEngine.java:72-83` — `tickCrop`：读 `age`，若 `<maxAge` 且 `RND.nextInt(4)==0`（**1/4 概率**）则 `age+1`，**无任何光照/hydration/方块下方检查**。
- `RandomTickEngine.java:85-99` — `tickUpwardPlant`：上方为空气且当前植株高度 `<3` 且 `RND.nextInt(3)==0`（**1/3 概率**）则在 `y+1` 生成新块；高度向下连数至非同类停止。
- `RandomTickEngine.java:102-114` — `tickLeavesDecay`：`RND.nextInt(8)==0` 时检查 9×9×9 范围内是否有 `*_log`，无则置空气。

### 2.2 耕地/工具（BlockManager.java）
- `BlockManager.java:249-274` — `getRequiredTool`：含 `farmland`? 否；耕地未被归类，沿用默认 `none`。
- 全局 grep `farmland|moisture|hydrated|FarmBlock|trample|sapling`：**仅 `ExplosionEngine.java:153` 把 farmland 爆炸退化为 dirt，以及 `BlockStateHelper`/`NetworkHandler` 把 sapling 当非实心/树叶掉落物处理**。**没有任何 FarmBlock 的 moisture 随机刻或踩踏逻辑**。

---

## 3. 对照原版的关键差异 / 偏差（逐条）

**G1. 耕地（FarmBlock）无 moisture / 灌溉机制 —— 缺失（严重）**
- 原版 `FarmBlock.randomTick`（`FarmBlock.java:89-100`）：若 `isNearWater`（9×4×9 范围、半径 **4 格**内见水，`FarmBlock.java:125-132`）或下雨 → moisture 设为 7；否则每随机刻 moisture−1；moisture=0 且无作物维持则 `turnToDirt`。`MAX_MOISTURE=7`（`FarmBlock.java:42`）。
- 本核心**完全没有 FarmBlock 的 moisture 状态与随机刻**（grep 确认无 `moisture`/`hydrated` 逻辑）。耕地永远是默认状态（即"干"），且不会被自动退化。

**G2. 耕地踩踏退化（fallOn → turnToDirt）缺失 —— 缺失**
- 原版 `FarmBlock.fallOn`（`FarmBlock.java:103-113`）：实体以足够速度跌落且碰撞体积 >0.512 时，按概率把耕地变回 dirt。
- 本核心无对应实现；踩在耕地上不会退化。**影响红石农场/路径设计语义，但不致命**。

**G3. 作物生长无视光照（夜间也长）—— 偏差（严重）**
- 原版 `CropBlock.randomTick`：**`if (getRawBrightness(pos,0) >= 9)` 才生长**（`CropBlock.java:80`）；`hasSufficientLight` 阈值为 8（`CropBlock.java:155-156`）。
- 本核心 `tickCrop`（`RandomTickEngine.java:72-83`）**无任何光照检查**，作物在夜晚、洞穴、封闭室内照常生长。

**G4. 作物生长无视 hydration，且不必种在耕地 —— 偏差（严重）**
- 原版 `CropBlock.getGrowthSpeed`（`CropBlock.java:101-114`）：耕地下方 f=1.0；**湿润耕地（moisture>0）f=3.0**；相邻耕地/作物再加权。生长概率 = `random.nextInt((int)(25.0F / f) + 1) == 0`（`CropBlock.java:85`）。即：**湿润耕地作物比干旱快约 3 倍**，且作物必须立于耕地（否则 `canSurvive` 失败弹落）。
- 本核心固定 `1/4` 概率（`RandomTickEngine.java:78`），**不区分干/湿、不要求耕地**。结果：① 作物生长速度与是否灌溉无关；② 作物种在石头/沙子等任意方块上都能长（原版会弹落）；③ 由于固定 1/4 远快于原版干旱（1/26）且快于原版湿润（≈1/9），**整体成熟过快**。

**G5. 甘蔗/仙人掌无视邻接约束 —— 偏差**
- 原版甘蔗 `canSurvive`（`SugarCaneBlock.java:79-97`）：下方为甘蔗（可），或下方为泥土/沙且**基底水平邻居之一含水**（水邻接才存活）。
- 原版仙人掌 `canSurvive`（`CactusBlock.java:108-119`）：**4 个水平邻居均不可实心、不可为岩浆**；下方为仙人掌/沙；上方不可为液体。
- 本核心 `tickUpwardPlant`（`RandomTickEngine.java:85-99`）**只检查上方空气 + 高度<3 + 1/3 概率**，无基底/邻水/邻空格检查。后果：① 甘蔗无需邻水也能长；② 仙人掌贴墙（邻实心方块）也能长；③ 二者均比原版快（原版用 `AGE 0..15` 累加、仅在 age==15 时才真正生长一节，本核心每次 1/3 直接长）。

**G6. 瓜类（西瓜/南瓜）成熟不生成果实 —— 缺失**
- 原版 `StemBlock` 成熟后，在相邻空地生成 `melon`/`pumpkin` 方块（受空间/光照约束）。
- 本核心 `CROP_MAX_AGE` 把 `melon_stem`/`pumpkin_stem` 当普通 age 作物只增 `age` 到 7（`RandomTickEngine.java:20,72-83`），**从不生成果实方块**。玩家种下的瓜藤只会"长满 age"但永不出瓜。

**G7. 树苗（sapling）不长成树 —— 缺失**
- 原版 `SaplingBlock.randomTick`：满足光照/空间/（部分需骨粉）后长成对应树。
- 本核心 `randomTick` 分发（`RandomTickEngine.java:58-69`）**不含 sapling**（只处理 CROP_MAX_AGE、cactus/sugar_cane/bamboo、leaves）。树苗随机刻什么都不做，永远是小树苗。

**G8. 藤蔓（vine）不生长 —— 缺失**
- 原版藤蔓有独立随机刻蔓延逻辑。本核心未处理 `vine`。属功能缺失（轻）。

**G9. 随机刻范围（±2 区块）小于原版 —— 偏差**
- 原版随机刻区域为玩家周围 **128 格半径（≈±8 区块，17×17）**；本核心 `RandomTickEngine.java:37` 仅 **±2 区块（5×5）**。距离玩家超过 ~2 区块的已加载耕地/作物不会随机刻，表现为"远处农田不长"。

**G10. 树叶凋零为近似 —— 轻微偏差**
- 原版用 `updateDistance` 的 6 步 BFS（约 5 格曼哈顿距离）判定；本核心 `tickLeavesDecay`（`RandomTickEngine.java:102-114`）用 **9×9×9 立方体**暴力扫描是否有 `*_log`。范围略大于原版、且仅按"是否含 log"而非"距离"，但功能近似可用；凋零时直接置空气，不掉落树苗/木棍（原版有概率掉落）。

---

## 4. Bug 清单（只记录不修）

| # | 严重度 | 文件:行号 | 现象 | 建议修复方向 |
|---|---|---|---|---|
| B1 | **严重** | `RandomTickEngine.java:72-83`（`tickCrop` 无光照/hydration/耕地检查）；`RandomTickEngine.java:78`（固定 1/4） | 作物无视光照、无视是否灌溉、可种任意方块，且成熟过快 | 加入光照阈值（`getRawBrightness>=9`）、要求下方为耕地（否则弹落）、按耕地 moisture 调整生长概率（湿润 f=3 → 概率≈1/9，干旱≈1/26） |
| B2 | 严重 | 全项目无 FarmBlock moisture/trample 逻辑（grep 确认） | 耕地无灌溉状态、不干涸、不被踩踏退化 | 为耕地引入 `moisture`(0..7) 属性；`randomTick` 中近水/下雨→7，否则递减，0 且无作物维持→变 dirt；`fallOn` 中按跌落速度概率退化 |
| B3 | 中等 | `RandomTickEngine.java:85-99`（`tickUpwardPlant` 无邻接检查 + 1/3） | 甘蔗不需邻水、仙人掌贴墙也长、二者过快 | 甘蔗：基底水平邻居含水才允许生长；仙人掌：4 邻居非实心且非岩浆；改用 `AGE 0..15` 累加、age==15 才长一节（对齐原版速率） |
| B4 | 中等 | `RandomTickEngine.java:20,72-83`（`melon_stem`/`pumpkin_stem` 仅增 age） | 西瓜/南瓜藤成熟后从不生成果实 | 在 stem 满 age 时检测相邻 4 格空气，按原版规则生成 `melon`/`pumpkin` 方块 |
| B5 | 中等 | `RandomTickEngine.java:58-69`（分发不含 sapling） | 树苗永远不长成树 | 增加 `sapling` 随机刻：满足光照/上方空间后生成对应树（可先用简单竖直树干+树冠） |
| B6 | 轻微 | `RandomTickEngine.java:37`（±2 区块） | 玩家远处 (>2 区块) 农田不生长 | 将随机刻区域扩大至原版量级（±8 区块 / 128 格半径），或至少扩大范围 |
| B7 | 轻微 | `RandomTickEngine.java:102-114`（立方体扫描 + 无掉落） | 树叶凋零判定范围/语义与原版 BFS 有出入，且不掉树苗/木棍 | 改为距离判定并加掉落概率（可选） |
| B8 | 轻微 | 全局（无 vine 处理） | 藤蔓不蔓延 | 如需支持，增加 `vine` 随机刻蔓延 |

---

## 5. 结论与优先级建议

**整体评价**：随机刻引擎骨架可用，`CROP_MAX_AGE` 数值正确，作物/甘蔗/仙人掌/树叶都能"动"。但与原版存在系统性偏差——**作物生长完全脱离光照与耕地灌溉约束且速率过快（B1）**，**耕地本身没有 moisture/退化机制（B2）**，这两点是 P11 与原版生存体验差异最大的根因。此外瓜类不结果（B4）、树苗不长树（B5）属于明显功能缺失。

**优先级建议**：
1. **P0/P1**：B1 + B2 —— 实现作物光照要求、耕地 moisture 灌溉与退化。这是"种植系统正确性"的核心，否则农业玩法与原版天差地别（作物夜长、旱地同速、耕地不干）。
2. **P1**：B3 —— 修正甘蔗/仙人掌邻接约束与生长速率。
3. **P1/P2**：B4、B5 —— 西瓜/南瓜结果、树苗长树，否则相关作物"半成品"。
4. **P3**：B6/B7/B8 —— 随机刻范围、树叶调零细节、藤蔓。

> 注：本报告对照基于 `mapping` 原版反编译（`CropBlock.java:80,85,101-114`、`FarmBlock.java:89-132`、`SugarCaneBlock.java:51-97`、`CactusBlock.java:56-119`）与 `RandomTickEngine.java` 实际代码逐项比对。未运行/未编译/未改动任何源码。
