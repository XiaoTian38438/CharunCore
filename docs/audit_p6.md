# 审计 P6：生存进度系统（挖矿 / 合成 / 熔炼 / 附魔 / 酿造）

> 审计对象：自研 MC 1.21.11 (Protocol 774) 核心，包名 `com.yanrong.server`
> 对照源：
> - 反编译源码 `mapping/cfr-source/net/minecraft/...`（注意 `remapped_server_1.21.11.jar.src` 大量文件为 `// INTERNAL ERROR`，改以 cfr-source 为准）
> - 协议 JSON `json/1.21.11/`、proto 字段 `protocol.json` 的 `rarity` 枚举
> 审计方式：仅读取源码 + 反编译对照，**未修改任何 .java、未启动服务器、未编译**。
> 关键文件行号均取自实际读取内容。

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|---|---|---|
| 挖矿掉落 | 部分实现 | 硬编码单物品掉落表（非 loot table），时运/精准采集部分支持；无工具分级校验、经验与精准采集未解耦 |
| 合成 | 基本已实现 | 自研 3×3 配方匹配 + 镜像 + 单格无序；容器点击/结果点击/合成全部/消耗网格均有；玩家 2×2 仅支持左键点击结果 |
| 熔炼 | 基本已实现 | 熔炉/烟熏炉/烘干机配方、燃料、进度、产出、经验累积基本正确；经验发放阈值与末影/客户端 orb 有偏差 |
| 附魔 | 部分实现（偏差严重） | 书架计数、青金石、经验扣减、附魔台 UI 流程齐全，但**核心随机/等级/消耗算法为自研近似，与原版完全不同** |
| 酿造 | 部分实现（存在功能缺失） | 基础药水/喷溅/滞留转换可用，但**红石延长、荧石增强、发酵蛛眼反转等“修饰型”酿造完全失效** |

---

## 2. 关键文件与行号证据

### 2.1 挖矿掉落
- `NetworkHandler.java:577-694` — digging 包（0x28）处理；`status==2||(status==0&&gameMode==1)` 进入破坏逻辑。
- `NetworkHandler.java:613-650` — 掉落主逻辑：`canHarvest` 判定 → `damageHeldItem` → 精准采集/树叶/普通分支 → 生成 `ItemEntity` → `getOreXp` 加经验。
- `NetworkHandler.java:4363-4422` — `getOreXp`（经验）与 `getBlockDropItem`（掉落物名，硬编码 switch）。
- `NetworkHandler.java:4425-4462` — `isLeaves` / `leafToSapling` / `rollLeavesDrop`（树苗/苹果概率）。
- `BlockManager.java:88-100` — `canHarvest`（仅校验工具**类别**）。
- `BlockManager.java:120-122, 23-34` — `isFortuneable` 与 `fortuneableBlocks` 集合（矿石/glowstone/sea_lantern/clay/melon）。
- `BlockManager.java:170-173` — `getBreakSecondsBestCase`（反作弊耗时）。

### 2.2 合成
- `CraftingSystem.java:230-253` — `add` / `matchRecipe`（精确 → 镜像 → 单格无序）。
- `CraftingSystem.java:255-324` — `matches` / `matchesMirrored` / `matchesShapeless`（带 bounding-box 归一）。
- `NetworkHandler.java:3367-3474` — `updateCraftingResult` / `handlePlayerCraftingResultClick` / `craftAllFromResult`（合成全部）/ `consumeCraftingGrid`（容器台消耗）。
- `NetworkHandler.java:3503-3509` — `consumePlayerCraftingGrid`（玩家 2×2 消耗 slots 1-4）。
- `NetworkHandler.java:3123-3138` — 玩家 2×2 结果刷新与点击路由。

### 2.3 熔炼
- `SmeltingSystem.java:21-73` — 配方表、blast/smoker 子集生成。
- `SmeltingSystem.java:103-116` — `getResult`（按炉型选配方）、`getCookTime`（200/100）。
- `SmeltingSystem.java:118-174` — `getFuelBurnTime`（燃料燃烧刻）。
- `ContainerStore.java:304-385` — `tickFurnace`（燃料消耗、熔炼进度、产出、经验累积 `xpStore`、lit 状态）。
- `NetworkHandler.java:3967-3988` — `syncOpenFurnaces`（属性同步）。
- `NetworkHandler.java:2657-2793` — `getFurnaceData` / 取物时经验结算（2932-2936 附近 furnace 分支）。

### 2.4 附魔
- `EnchantSystem.java:12-67` — MAX/CATEGORY/WEIGHT/GROUP/TREASURE 常量。
- `EnchantSystem.java:87-134` — `compute`（自研等级/消耗算法）。
- `NetworkHandler.java:4010-4029` — `computeBookshelfCount`（书架计数 + 隔断判定）。
- `NetworkHandler.java:4032-4059` — `handleContainerButtonClick`（附魔选项选择/扣费/产出）。
- `NetworkHandler.java:4068-4086` — `giveEnchantedItem`（书→附魔书转换）。
- `NetworkHandler.java:4088-4113` — `recomputeEnchanting`（每帧重算 3 选项）。

### 2.5 酿造
- `BrewingSystem.java:8-49` — `isBottle` / `isFuel` / `BASE` 配方（瓶子名映射）。
- `BrewingSystem.java:56-100` — `baseEffect` / `applyModifier` / `resolveBrewEffect`（效果字符串）。
- `ContainerStore.java:403-455` — `tickBrewing`（燃料、进度、产出、`potionType` 写入）。
- `NetworkHandler.java:4151-4166` — `syncOpenBrewings`（属性同步）。

### 2.6 原版对照关键行
- `cfr-source/.../world/inventory/EnchantmentMenu.java:117-130` — `getEnchantmentCost` 调用与种子机制；`:181-192` `getEnchantmentList`（种子+槽位）。
- `cfr-source/.../world/item/enchantment/EnchantmentHelper.java:450-466` — `getEnchantmentCost` 原版公式；`:483-504` `selectEnchantment`（power budget → minCost/maxCost → 多选）。
- `cfr-source/.../world/level/block/EnchantingTableBlock.java:41,52` — `BOOKSHELF_OFFSETS` 与 `isValidBookShelf`（需检查**两个**空气格）。
- `cfr-source/.../world/level/block/entity/AbstractFurnaceBlockEntity.java:73-75,174-211` — `BURN_TIME_STANDARD=200`、`BURN_COOL_SPEED=2`、cook/lit 时序。
- `cfr-source/.../world/level/block/entity/BrewingStandBlockEntity.java` — `BREWING_TIME=400`、blaze 粉燃料机制。
- `json/1.21.11/protocol.json:353-360` — `rarity` 枚举 `0=common,1=uncommon,2=rare,3=very_rare`。

---

## 3. 对照原版的关键差异 / 偏差（逐条）

### 3.1 挖矿
1. **掉落表为硬编码单物品，非 loot table**（`NetworkHandler.java:4376-4422`）。原版由 `LootTable` + `applyBonus(fortune)` + `binomial_with_bonus_count` 驱动，可多物品、条件掉落、NBT。自研仅返回单个物品名，导致：
   - `bookshelf` → 1 `book`（原版 3 个 `book`）。
   - 多数非矿方块靠 `default -> blockName` 掉自身（合理），但无法表达“受时运/精准影响”的多样掉落。
2. **精准采集仍发放经验**（`NetworkHandler.java:627-649`）：silk 分支 `dropId=blockName`，随后 `if (xp>0) addExperience(xp)` **无条件执行**。原版：矿石精准采集只掉矿石方块、**不**给经验。此处带丝必给经验，偏差。
3. **工具分级（tier）未校验**（`BlockManager.java:88-100`）：`canHarvest` 仅比较工具**类别**（pickaxe/axe…），不校验材质等级。原版：钻石矿需至少石镐，木镐破坏**无掉落**。此处木镐即可从钻石矿获得掉落，偏差（中）。
4. **时运分布为均匀分布，非二项**（`NetworkHandler.java:638`）：`dropCount = 1 + nextInt(fortune+1)` → 均值 `fortune/2+1`，且**最小值恒为 1**。原版矿石时运为 `binomial(n=fortune+1, p=0.5)`（可加 0，均值 (fortune+1)/2）。自研平均多出约 0.5/个，且不会“零加成”，整体偏高（轻-中）。
5. **时运仅作用于 `fortuneableBlocks`**（`BlockManager.java:23-34`）：glowstone/clay/melon 虽在集合内，但 `getBlockDropItem` 均返回**单个**物品，时运只把数量加到 `1+fortune`（glowstone 原版 1-4 且时运增益、melon 3-7、clay 基础 4）。自研对这几类基数偏低（轻）。
6. **容器方块双重掉落**（`NetworkHandler.java:2662` 先 `dropContainerContents` 溢出内容物，`:634` `getBlockDropItem("chest")` 走 `default` 再掉一个 `chest`）：原版**有物品的箱子被破坏时不掉箱子方块**（仅溢物品）。自研额外掉一个箱子；同理 `ender_chest` 原版不掉任何物，自研却掉 `ender_chest`（中）。

### 3.2 合成
7. **无序配方仅支持单格**（`CraftingSystem.java:248-251`）：`matchesShapeless` 只在 `recipe.shapeless`（即单输入格，如 9↔1 压缩）时启用。原版多格无序配方（如某些染料/台阶）不会被识别（轻，当前配方集内无此类，故暂无实际影响）。
8. **玩家 2×2 仅左键点击结果可合成，不支持 shift 合成全部**（`NetworkHandler.java:3503` `consumePlayerCraftingGrid` 仅被左键 `handlePlayerCraftingResultClick` 调用；`windowId==0` 的 shift 走 `handleShiftClick` 不触发合成，`NetworkHandler.java` shift 分支针对容器台 `windowId!=0`）。容器台（3×3）支持 shift 合成全部（`craftAllFromResult`，`:3416`）（轻）。
9. **匹配用 bounding-box 归一**（`CraftingSystem.java:255-292`）：玩家 2×2 网格映射到 9 格左上 2×2，bounding-box 后 3×3 配方（如 chest）自然无法在 2×2 命中，行为与原版一致（正确，非偏差）。

### 3.3 熔炼
10. **熔炼逻辑与原版高度一致**（`ContainerStore.java:304-385`）：`BURN_COOL_SPEED=2` 的冷却递减（`:376-377`）、`canBurn` 接受判定、`lava_bucket` 回收空桶（`:345-347`）、cook/total 时序均匹配 `AbstractFurnaceBlockEntity`（`cfr-source:174-211`）。炉型选配方（blast/smoker/普通）与 `getCookTime`（200/100）正确。
11. **经验发放阈值偏差**（`NetworkHandler.java` furnace 取物分支）：`if (slot==2 && count < contents[5] && fd.xpStore >= 1.0f)` 仅当累积 ≥1 才发。原版按配方 `xp`（如 iron 0.7）以 orb 概率结算，单块即可给约 0.7 经验。自研 <1 经验被“囤积”到下一次，iron(0.7)/sand(0.1) 等需多次熔炼才发一次（轻）。
12. **经验以直接加玩家代替经验球**（`NetworkHandler.java`）：无实体 `ExperienceOrb`；且经漏斗抽取产出时不触发取物逻辑→经验不发放（设计性偏差，无漏斗搬运时影响小，轻）。

### 3.4 附魔（偏差最严重）
13. **等级算法非原版**（`EnchantSystem.java:101,128`）：自研 `tierBase = {1+bs/4, 1+bs/2, 1+bs}`，`lvl = max(1, min(max, tierBase[i] + nextInt(2)))`。原版等级由 **power budget**（派生自 `getEnchantmentCost`）经各附魔 `minCost/maxCost(level)` 筛选决定（`EnchantmentHelper.java:483-504`），三个槽位的差异来自 cost 修正（`n3/3`、`n3*2/3+1`、`max(n3,bs*2)`），**绝非** `1+bs/4 / 1+bs/2 / 1+bs` 的线性映射。结果：附魔等级随书架近似线性增长，且无法表达“低级槽高花费”等原版关系（严重）。
14. **消耗（经验等级）算法非原版**（`EnchantSystem.java:131` `cost = lvl + 1` vs `EnchantmentHelper.java:450-466`）：原版 cost 为独立随机值（受 `bs/2 + rand(bs+1) + rand(8)` 影响），与结果等级解耦。自研 cost 恒 = 等级+1，对高级附魔**严重少收费**（如“等级5 锋利”原版底部槽约 30+ 级，自研仅 6 级）（严重）。
15. **随机种子每帧重置**（`NetworkHandler.java:4100` `seed = new Random().nextLong()`）：原版用**玩家稳定** `enchantmentSeed`，仅在一次附魔后重掷（`EnchantmentMenu.java:116,171`）。自研每次 `recomputeEnchanting`（每次放入/取出物品/青金石）都重掷，导致三个选项随交互乱跳，与原版“物品与书架确定后选项固定”的行为不符（中）。
16. **每次点击仅 1 个附魔**（`EnchantSystem.java:104-132` 单循环；`NetworkHandler.java:4056` 单 `giveEnchantedItem`）：原版 `selectEnchantment` 可一次施加 1-3 个（书最多 2 个，逐级减半 power 叠加，`EnchantmentHelper.java:494-501`）。自研每次只给一个，强烈削弱附魔台产出（中-重）。
17. **青金石消耗恒为 1，未按槽位 n+1**（`NetworkHandler.java:4039,4049`）：原版消耗 `n+1` 颗青金石（顶部 1 / 中部 2 / 底部 3，`EnchantmentMenu.java:145-162`）。自研三者均只扣 1（中）。
18. **书架有效性只检查 1 个空气隔断**（`NetworkHandler.java:4023-4026`）：仅检查 `mx=x+signum(dx), y, mz=z+signum(dz)` 的空气。原版 `isValidBookShelf`（`:52`）要求**两个**空气格：`(offset/2, y, offset/2)` 与 `(offset/2, y+1, offset/2)`。自研漏判 y+1 空气，对“顶层隔断被挡”的书架仍会计数，略微更宽松（中-轻）。
19. **稀有度权重缺失 UNCOMMON 档**（`EnchantSystem.java:44-54`）：自研只用了 10（common）/ 2（thorns,frost_walker）/ 1（mending），把原版 `UNCOMMON=5` 的那些附魔（looting/knockback/fire_aspect/sweeping_edge/depth_strider 等）都并入 10（`protocol.json:353-360` 证明原版有 4 档权重）。导致这些附魔相对原版**过权**（中-轻）。
20. **入物品在槽内被消耗而非原地附魔**（`NetworkHandler.java:4052-4053`）：自研 `slots[1]--` 取走输入并 `giveEnchantedItem` 新生成附魔物放入背包；原版在同槽原地附魔并保留其余堆叠。单件等价，但堆叠（如 16 本书）交互体验不同（轻）。
21. **附魔覆盖子集**（`EnchantSystem.java:12-42`）：仅收录主武器/工具/护甲/弓类；三叉戟（loyalty/riptide/impaling 等）、弩（multishot/piercing/quick_charge）、swift_sneak、soul_speed 等不可用附魔台（覆盖缺口，轻；若服务端不提供这些物品则无影响）。

### 3.5 酿造
22. **修饰型酿造完全失效**（`NetworkHandler.java:428-429` `if (res != null && !res.equals(bn))` + `BrewingSystem.java:38-40`）：`potion+redstone→potion`、`potion+glowstone_dust→potion`、`potion+fermented_spider_eye→potion`、以及 `awkward_potion+nether_wart→awkward_potion` 的**结果瓶名等于输入瓶名**，导致 `!res.equals(bn)` 恒为 false，`canBrew` 永不触发。即：**红石延长 / 荧石增强 / 发酵蛛眼反转全部无法酿造**（尽管 `resolveBrewEffect`/`applyModifier` 已正确实现效果字符串，但因酿造不启动而不会生效）。基础转化（water→awkward、`awkward+材料→potion`、`potion+gunpowder→splash`、`splash+dragon_breath→lingering`）因瓶名变化而正常（严重）。
23. **药水用“不同物品名”建模**（`BrewingSystem.java:8-13, 注释 19-23`）：服务端不序列化药水 NBT，故 water_bottle/awkward_potion/potion/splash_potion/lingering_potion 为独立物品，效果另存 `potionType[]`。这是设计性限制：客户端无法区分同物品名的药水效果（已自注），但正是该建模使 `canBrew` 的“瓶名是否变化”判定无法覆盖“瓶名不变但效果变”的修饰酿造（根因见 22）。
24. **燃料/进度常量与原版一致**（`ContainerStore.java:411,432` `fuelTime=8000`≈20 次×400，`brewTotal=400`；blaze 粉每次 1 份=20 酿）：与 `BrewingStandBlockEntity` 一致。基础酿造时序正确（非偏差）。

---

## 4. Bug 清单（只记录，不修复）

| # | 严重度 | 文件:行号 | 现象 | 建议修复方向 |
|---|---|---|---|---|
| B1 | 严重 | `NetworkHandler.java:428-429` + `BrewingSystem.java:38-40` | 红石/荧石/发酵蛛眼修饰酿造永不启动（瓶名未变导致 `canBrew=false`） | `canBrew` 应同时判断“瓶名变化 **或** 效果字符串变化”（对已有 `potionType` 的瓶子，调用 `resolveBrewEffect` 看结果是否非空且变化），而非仅比较物品名 |
| B2 | 严重 | `EnchantSystem.java:101,128,131`；`NetworkHandler.java:4100,4056,4039,4049` | 附魔等级/消耗/种子/多选/青金石全为自研近似，与原版差异巨大 | 参考 `EnchantmentHelper.getEnchantmentCost`（`:450-466`）与 `selectEnchantment`（`:483-504`）实现：power budget → `minCost/maxCost(level)` 选级、稳定玩家种子、`n+1` 青金石、一次可叠 1-3 附魔 |
| B3 | 中等 | `NetworkHandler.java:627-649` | 精准采集矿石仍发放经验（原版不掉经验） | 若 `silk>0` 分支命中，跳过 `getOreXp` 加经验 |
| B4 | 中等 | `BlockManager.java:88-100` | 仅校验工具类别，不校验材质等级（木镐能挖钻石矿并掉矿） | `canHarvest` 附加 tier 比对（如 `harvestLevel` 映射 wood/stone/iron/diamond/netherite） |
| B5 | 中等 | `NetworkHandler.java:2662` + `:634`（`default→blockName`） | 有物品的箱子/末影箱破坏时额外掉落箱子方块（原版箱子不掉、末影箱不掉任何物） | 对 chest/ender_chest/trapped_chest，掉落物分支走 `default` 前特判：仅溢内容物、不掉落方块 |
| B6 | 中等 | `NetworkHandler.java:415,4039,4049` | 附魔青金石消耗恒为 1，未按槽位 n+1 | 扣青金石数量改为 `buttonId+1`，并校验 `lapisCount >= buttonId+1` |
| B7 | 中等 | `NetworkHandler.java:4100` | 附魔选项每帧重掷，随交互乱跳 | 使用玩家稳定种子（如 `data` 上的 `enchantmentSeed` 字段），仅在一次附魔后重掷 |
| B8 | 中等 | `EnchantSystem.java:104-132` + `NetworkHandler.java:4056` | 每次点击仅 1 个附魔 | 复用 `selectEnchantment` 式多选（按 power 递减叠加，过滤互斥） |
| B9 | 中等 | `NetworkHandler.java:4023-4026` | 书架计数漏判 y+1 空气隔断 | 按 `EnchantingTableBlock.isValidBookShelf` 同时检查 `(offset/2,y,offset/2)` 与 `(offset/2,y+1,offset/2)` 两格空气 |
| B10 | 轻-中 | `NetworkHandler.java:638` | 时运为均匀分布且最小值恒 1，高于原版期望 | 改为二项 `binomial(fortune+1, 0.5)`，允许 0 加成 |
| B11 | 轻 | `EnchantSystem.java:44-54` | 稀有度权重缺 UNCOMMON(5) 档，部分附魔过权 | 按 `protocol.json` 的 `rarity` 枚举补全各附魔权重（10/5/2/1） |
| B12 | 轻 | `NetworkHandler.java` furnace 取物分支（`fd.xpStore>=1.0f`） | <1 经验被囤积，单块熔炼经验延迟发放 | 直接按配方 `xp` 概率结算（如 `floor(f)+rand<frac?+1`） |
| B13 | 轻 | `CraftingSystem.java:248-251` | 多格无序配方不被识别 | 支持多格 `shapeless` 匹配（当前配方集无此类，暂无实际影响） |
| B14 | 轻 | 玩家 2×2 shift 合成（`NetworkHandler.java` shift 分支） | 玩家背包 2×2 网格不支持 shift 合成全部 | `windowId==0 && slot==0` 的 shift 走 `handlePlayerCraftingResultClick`/`craftAllFromResult` 逻辑 |
| B15 | 轻 | `NetworkHandler.java:4376-4422`（`bookshelf`→`book`） | 书架掉 1 本书（原版 3） | 书架掉落数量改为 3 |

---

## 5. 结论与优先级建议

**总体结论**：熔炼子系统实现最贴近原版（B12 为轻微偏差）；合成可用但有两个轻量体验缺口（B13/B14）；**挖矿、附魔、酿造存在需要重点修复的偏差**。

**优先级建议**（按“对生存玩法正确性影响”排序）：

1. **P0（严重，建议优先）**：
   - B1 酿造修饰失效 —— 直接导致红石/荧石/反转药水无法制作，生存药水链断裂。修复点集中在 `canBrew` 判定（十几行），性价比高。
   - B2 附魔算法 —— 这是 P6 中偏差最大、最影响“原版手感与平衡”的子系统。建议按 `EnchantmentHelper` 重写 `EnchantSystem.compute` 与 `recomputeEnchanting` 的种子/多选/青金石逻辑；可分步：先修种子稳定(B7)+青金石(B6)+多选(B8)，再替换等级/消耗公式(B2)。

2. **P1（中等）**：B3 精准采集经验、B4 工具分级、B5 容器双重掉落、B9 书架隔断 —— 这几处影响公平/经济（免费箱子、木镐挖钻石），改动量小、风险低，建议一并修。

3. **P2（轻量）**：B10 时运分布、B11 稀有度权重、B12 熔炼经验、B13/B14/B15 合成与掉落细节 —— 可在后续打磨批次处理，不影响核心可玩性。

**说明**：本报告所有结论基于实际读取的源码与 `cfr-source` 反编译对照；因 `remapped_server_1.21.11.jar.src` 大量文件为 `// INTERNAL ERROR`，原版算法以 `mapping/cfr-source` 下可读文件为准。`EnchantmentMenu`/`EnchantmentHelper` 等关键对照均已成功读取并逐行核对。
