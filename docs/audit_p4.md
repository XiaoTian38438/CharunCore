# P4 实体与生物 审计报告（刷怪 / AI / 掉落）

审计对象：自研 MC 1.21.11（Protocol 774）核心，包名 `com.yanrong.server`
对照源：mapping/remapped_server_1.21.11.jar.src（NaturalSpawner.java、PortalForcer.java、DimensionType 反编译失败）、json/1.21.11/protocol.json、wiki.vg 协议
审计日期：2026-08-11
铁律：仅读取源码 + 写报告，未修改任何 .java。

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|--------|------|------|
| 刷怪笼（Spawner Block） | 已实现 | `SpawnerSystem.java` 提供 mob_spawner 方块激活刷怪 |
| 自然刷怪（hostile/passive） | 部分实现 | `EntityManager.tick()` 内 `trySpawnHostileMobs`/`trySpawnPassiveMobs`，简化版，非原版 NaturalSpawner |
| 实体 AI（移动/寻路/攻击/寻食） | 部分实现 | `MobEntity.tick()` 自写近战追逐 + A* 寻路 + 游荡 + 逃跑；**无 GoalSelector**，无远程/特殊行为 |
| 击杀掉落物 | 已实现（近似） | `MobEntity.dropLoot()` 硬编码掉落表 |
| 经验 | 部分实现 | `attackMob` 直接 `addExperience` 给玩家；`spawnExperienceOrbs` 拆分算法与原版一致但仅用于玩家死亡 |

整体：核心玩法链路（刷怪→追击→击杀→掉物/经验）**可用**，但与原版在刷怪算法、AI 行为、LootTable、经验发放方式上有大量偏差。

---

## 2. 关键文件与行号证据

### 2.1 刷怪
- `world/SpawnerSystem.java:43-90` — 刷怪笼 tick：16 格内有玩家才刷，每 10~30 秒刷最多 4 只。
- `world/entity/EntityManager.java:116-133` — 主 tick 入口，`spawnRng.nextInt(8)==0` 触发敌对刷怪、`nextInt(200)==0` 触发被动刷怪。
- `world/entity/EntityManager.java:214-258` — `trySpawnHostileMobs()`：每玩家每触发刷 1 只，距离 24~54 格，按维度选硬编池。
- `world/entity/EntityManager.java:260-299` — `trySpawnPassiveMobs()`：仅主世界，草方块上刷牛/羊/猪/鸡，群 2~4 只。
- `world/entity/EntityManager.java:199-212` — `findSpawnY()`：从世界顶向下扫描，返回“列顶首个实心块上方 2 格空气”的 Y。
- `world/entity/EntityManager.java:174-197` — 光照近似：`surfaceLightLevel()`（仅 0/10/15）+ `isLitNearby()`（5×5×4 盒内有无发光方块）。
- `world/entity/EntityManager.java:136-158` — `despawnMobs()`：>128 即时消失，32~128 随机消失（与原版一致）。

### 2.2 AI
- `world/entity/MobEntity.java:128-241` — `tick()`：近战追逐（`getAttackDamage` 伤害）、A* 寻路（`Pathfinder.step`）、游荡、受击逃跑、白天燃烧、苦力怕自爆。
- `world/entity/MobEntity.java:54-115` — `isHostile/isPassive/isNeutral/burnsInDaylight/getAttackDamage/getXpDrop`。
- `world/entity/Pathfinder.java:27-76` — 轻量 A*（1500 节点上限，超出回退直线 homing）。
- `world/entity/LivingEntity.java:14-44` — 基类 `damage()`（含火焰/熔岩环境伤害）、`onDeath()` 钩子。
- **无** `GoalSelector` 对应实现（项目自写行为树替代）。

### 2.3 掉落与经验
- `world/entity/MobEntity.java:263-270` — `onDeath()`：调 `dropLoot()`，并尝试 `killer.addExperience(getXpDrop())`。
- `world/entity/MobEntity.java:272-316` — `dropLoot()`：硬编码 switch 掉落表（含火烧熟肉判断）。
- `network/NetworkHandler.java:1194-1254` — `attackMob()`：伤害计算 + 击杀时 `addExperience(getMobXp(living))`（行 1249-1252）。
- `network/NetworkHandler.java:4511-4536` — `getMobXp()`：另一套 XP 表。
- `network/NetworkHandler.java:2262-2285` — `spawnExperienceOrbs()`：面额拆分 `[1,3,7,17,37,73,149,307,617,1237,2477]`（与原版 `ExperienceOrb` 完全一致）。
- `world/entity/ItemEntity.java:24-66` — 掉落物 tick + 拾取（无维度校验！）。
- `world/entity/ExperienceOrbEntity.java:22-88` — 经验球 tick + 吸引拾取（有维度校验）。

---

## 3. 对照原版的关键差异 / 偏差（逐条）

1. **自然刷怪算法完全不同**（对照 `NaturalSpawner.java`）
   - 原版按 chunk + `MobCategory`（MONSTER/CREATURE/AMBIENT/WATER/...）的全局/局部上限（`canSpawnForCategoryGlobal/Local`）刷怪，且以生物群系加权表 `MobSpawnSettings` + 结构（如下界要塞 `FORTRESS_ENEMIES`）决定种群；本项目为单维度扁平计数（`mobCount>24` / `>12`）与硬编码池（`EntityManager.java:246-251,275`）。
   - 原版刷怪距离：`MIN_SPAWN_DISTANCE=24`、`SPAWN_DISTANCE_BLOCK=128`，且要求距玩家 >24（576）且 <128²；本项目距离固定 24~54 格（`EntityManager.java:230-231,280-281`），且单点只刷 1 只（`:255`/`:297` 的 `break`）。
   - 原版有 `spawnPotential` 能量预算（同一点刷多了会被抑制）、`checkSpawnRules`/`SpawnPlacements` 合法性校验（如僵尸需固体地面且光≤某值、鱿鱼需水中、蝙蝠需黑暗）；本项目用 `findSpawnY` + 近似光照，无群系/结构/合法性校验。

2. **刷怪 Y 高度逻辑（findSpawnY）偏差**（对照 `NaturalSpawner.getTopNonCollidingPos` 对 `hasCeiling` 的处理）
   - 本项目 `findSpawnY`（`EntityManager.java:199-212`）从世界顶 Y 向下扫描，返回**列最高**实心块上方。后果：
     - 主世界：怪物只刷在“列最高点”（山顶），**绝不刷在洞穴/玩家脚边地面**；原版大量敌对怪在地下黑暗处刷。
     - 下界（`hasCeiling=true`）：**若 worldgen 生成了基岩天花板，则刷怪点会落在天花板上而非下界岩地面**（见 Bug 清单 P4-1）。

3. **AI 非 GoalSelector**（对照 `Mob.java` / `GoalSelector.java`）
   - 本项目 `MobEntity.tick()` 单一手写行为；原版每个 mob 由 `GoalSelector` 组合多个 Goal（如 `ZombieAttackGoal`、`RangedBowAttackGoal`、`MeleeAttackGoal`、`FleeSunGoal` 等）。
   - **骷髅、女巫等应为远程攻击**，本项目中统一近战（`MobEntity.java:159-167` 仅近战伤害），无射箭/喷药水。
   - 无门破坏、无村民 AI、无繁殖、无骑乘组合（如蜘蛛骑士）、无僵尸感染村民、无按难度（简单/普通/困难）的攻击/刷新差异。

4. **掉落表硬编码，非 LootTable**（对照原版 `LootTable`）
   - 本项目 `dropLoot()`（`MobEntity.java:272-316`）硬编码。缺失：抢夺（looting）附魔加成（原版按等级 +1~3 掉落区间）、部分生物完整掉落（如女巫原版掉红石/甘油/糖/蜘蛛眼/玻璃瓶等，此处仅 `redstone`；史莱姆/岩浆怪数量区间与原版不同）、稀有掉落（如僵尸极低概率铁/胡萝卜/土豆）。
   - 优点：火烧熟肉映射（`:274,291-295`）与原版一致。

5. **经验发放方式偏差**（对照原版 `Mob` 死亡生成 `ExperienceOrb`）
   - 原版：击杀 mob 在死亡处**生成可拾取经验球**，由玩家走近拾取（可多人抢、可被经验修补消耗）。
   - 本项目：玩家击杀经 `attackMob` 直接 `addExperience(getMobXp)`（`NetworkHandler.java:1251`）即时入账，**不生成经验球**；`spawnExperienceOrbs` 拆分算法虽正确，但仅用于玩家死亡掉 XP（`NetworkHandler.java:2257`）。
   - 由此 `MobEntity.getEntity#onDeath` 里的 killer-XP 分支实际是死代码（见 Bug P4-2）。

6. **水生/特殊生物未接入自然刷怪**
   - `squid` 在 `isPassive()`（`:66`）但自然刷怪池不含；`findSpawnY` 遇水返回 -1，故无水中刷怪。原版鱿鱼/各类鱼按水体刷。

---

## 4. Bug 清单（只记录不修）

| 编号 | 严重度 | 文件:行号 | 现象 | 建议修复方向 |
|------|--------|-----------|------|--------------|
| P4-1 | 中等 | `EntityManager.java:199-212`（`findSpawnY` 被 `214-258` 调用） | 刷怪 Y 为世界列顶：下界若有天花板则怪刷在天花板而非地面；主世界只在山顶刷，无洞穴刷怪 | 改为“在玩家附近、从地表向下寻找合法固体地表（含地下无光处）”，并对 `hasCeiling` 维度从地面而非顶部扫描；参考原版 `getTopNonCollidingPos` 的 `hasCeiling` 分支 |
| P4-2 | 中等 | `LivingEntity.java:8`（仅声明）、`MobEntity.java:266`（仅读取，从未赋值）；`MobEntity.java:263-270` | `lastAttacker` 永远为 null，`onDeath()` 的 `killer.addExperience(getXpDrop())` 是死代码；且存在两套 XP 表（`getMobXp` vs `getXpDrop`）不一致 | 在 `attackMob`/`damage("player")` 时把攻击者写入 `lastAttacker` 以启用 onDeath 经验；或删除死代码，统一用 `getMobXp`。注意避免双重发放 |
| P4-3 | 中等 | `MobEntity.java:159-167`、`MobEntity.java:128-241` | 骷髅/女巫等远程生物被强行近战；无门破坏、无村民/繁殖/骑乘等原版行为 | 为 ranged 生物增加射弹攻击分支（ArrowEntity 已存在）；按需补充 Goal 化行为 |
| P4-4 | 中等 | `NetworkHandler.java:1251`（直接 addExperience）、`NetworkHandler.java:2262-2285`（仅死亡用） | 击杀直接给经验，不生成可拾取经验球；与“多人抢 XP”“经验修补”等原版机制不符 | 击杀时调用 `spawnExperienceOrbs(dim, x, y, z, getMobXp)` 而非直接 addExperience（保留玩家死亡路径） |
| P4-5 | 中等 | `EntityManager.java:116-133,214-258,260-299` | 刷怪节奏过简：单玩家单点单只、距离仅 24~54、无群系/结构/难度/局部上限，整体刷怪密度与原版差异大 | 引入基于 chunk 的 MobCategory 上限与生物群系权重表；放宽刷怪距离到 128；按难度调攻击与刷怪量 |
| P4-6 | 轻微 | `MobEntity.java:272-316` | 掉落为硬编码表，无 looting 加成、无稀有掉落、部分生物掉落不全（女巫等） | 接入 LootTable 或至少补齐 looting 倍数与稀有掉落概率 |
| P4-7 | 轻微 | `ItemEntity.java:46`（遍历 `players` 未校验 `currentDim`） | 掉落物拾取未按维度过滤，另一维度玩家若在相近 x/y/z 可能误拾取；而 `ExperienceOrbEntity.java:34` 已校验维度，两者行为不一致 | 在 `ItemEntity.tick` 与 `giveItem` 前加 `player.currentDim != this.dim` 判断 |
| P4-8 | 轻微 | `EntityManager.java:264`（`trySpawnPassiveMobs` 仅 OVERWORLD）、`:275` | 被动生物无水生（鱿鱼等）；也无下界/末地被动群系（如原版下界某些生物） | 增加水体刷怪检测与对应种群（可选，按玩法需求） |
| P4-9 | 轻微 | `SpawnerSystem.java:21` `DEFAULT_TYPES`、`:48` 间隔 | 刷怪笼种类固定随机池、间隔 10~30s，未读取刷怪笼 NBT 中配置的实体/生成参数（原版 mob_spawner 可配置实体/权重/最大邻近玩家数等） | 读取 spawner 方块 NBT 决定种群与参数（如已存 NBT 则对接） |

---

## 5. 结论与优先级建议

- **最高优先级（P4-1 / P4-2）**：刷怪 Y 算法与经验 death-path 死代码，直接影响“怪刷在哪、给多少经验”的正确性，且可能引发下界刷在天花板的明显 bug。
- **高优先级（P4-3 / P4-4 / P4-5）**：AI 远程行为缺失、经验发放方式、整体刷怪密度与原版不符，影响生存手感与平衡。
- **低优先级（P4-6~P4-9）**：掉落表精细度、维度拾取过滤、水生刷怪、刷怪笼参数，属可后续打磨项。

总体：链路可玩，但“自然刷怪”与原版差距最大（算法级），“AI”为可接受简化（建议至少补远程），“掉落/经验”基本正确但发放机制偏离原版。建议优先修 P4-1 与 P4-2。
