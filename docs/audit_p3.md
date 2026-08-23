# P3 生存机制审计报告（血量 / 饥饿 / 战斗 / 死亡 / 状态效果 / 经验）

审计对象：`com.yanrong.server` 自研核心（MC 1.21.11 / Protocol 774，Java 21 + Netty）
核心玩法代码：`../src/main/java/com/CharunCore/server/network/NetworkHandler.java`（6806 行）
持久化：`../src/main/java/com/CharunCore/server/world/PlayerData.java`
对照源：
- 原版反编译 `mapping/remapped_server_1.21.11.jar.src/.../world/food/FoodData.java`、`world/damagesource/CombatRules.java`、`data/tags/DamageTypeTagsProvider.java`
- 协议包规范 `json/1.21.11/protocol.json`、`json/1.21.11/attributes.json`、`json/1.21.11/foods.json`

> 说明：本审计所有结论均基于实际读到的源码行号；未改动任何 `.java` 源码。原版 `LivingEntity.java` 反编译为空文件（mapping 未产出），故部分战斗细节以 `CombatRules`/`DamageTypeTagsProvider` 与协议 JSON 为准。

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|---|---|---|
| 血量 / 伤害计算 | **已实现（部分偏差）** | 护甲点数表、护甲减伤、保护附魔 EPF 公式均与原版一致；属性包（attribute）ID 映射严重错误 |
| 饥饿 / 食物 | **已实现（部分偏差）** | 饥饿衰减/饱和消耗与 `FoodData.tick` 高度一致；进食饱和度被放大、部分食物 hunger 值不对 |
| 战斗（近战） | **已实现（明显缺口）** | 基础伤害/冷却/暴击/击退有；**玩家武器附魔（锋利/击退/火焰）完全未读取** |
| 死亡 / 掉落 / 复活 | **已实现（部分偏差）** | 掉落、复活点、床/重生锚有；复活无条件清零经验（与 keepInventory 冲突） |
| 状态效果 | **已实现（部分偏差）** | 协议 ID 映射正确、regen/poison/wither 节奏正确；瞬时效果与吸收/生命提升缺失 |
| 经验 | **已实现** | `xpForLevel`、加经验、升级进度计算正确 |

整体：**核心框架可用**，但存在 1 个严重（SEVERE）属性同步错误、若干中等偏差。以下逐条给出证据。

---

## 2. 关键文件与行号证据

- `NetworkHandler.java:3741` `armorPointsOf(...)` —— 护甲点数表
- `NetworkHandler.java:3758` `armorToughnessOf(...)` —— 韧性表
- `NetworkHandler.java:3766` `bypassesArmor(...)` —— 无视护甲判定
- `NetworkHandler.java:3774` `isBlockingFrom(...)` / `3785` `getShieldSlot()` / `3792` `isShield()` —— 盾牌
- `NetworkHandler.java:3803/3807` `damagePlayer(...)` —— 伤害入口（护甲+保护附魔）
- `NetworkHandler.java:3890` `deathMessageFor(...)` —— 死亡消息
- `NetworkHandler.java:3907` `knockback(...)` / `3914` `addExhaustion(...)` / `3919` `tickFoodSystem()`
- `NetworkHandler.java:4168` `sendHealthUpdate()` / `4177` `syncOnFire()` / `4201` `effectProtocolId()`
- `NetworkHandler.java:4218` `addEffect()` / `4244` `removeEffect()` / `4256` `hasEffect()` / `4262` `tickEffects()`
- `NetworkHandler.java:4296` `syncAttributesIfNeeded()` / `4310` `sendAttributesUpdate()` —— **属性包**
- `NetworkHandler.java:4325` `sendExperienceUpdate()` / `4333` `addExperience()` / `4345` `xpForLevel()`
- `NetworkHandler.java:2206` `sendDeathScreen()` / `2222` `dropInventoryOnDeath()`
- `NetworkHandler.java:3539` `tickSurvival()` —— 环境伤害/饥饿/死亡总调度
- `NetworkHandler.java:281` 复活逻辑（client_command respawn）/ `327` 发送 Respawn(0x50)
- `NetworkHandler.java:1194` `attackMob(...)` / `4465` `getAttackDamage()` / `4495` `getAttackCooldownMs()`
- `NetworkHandler.java:1027` 进食逻辑 / `4584` `getSaturationModifier()` / `4599` `getFoodValue()`
- `PlayerData.java:17-31` —— health/food/airTicks/xp/saturation/exhaustion/gameMode/dimension/respawn* 全部字段存在

---

## 3. 对照原版的关键差异 / 偏差（逐条，附 文件:行号）

### 3.1 护甲点数表（正确）`NetworkHandler.java:3741-3756`
逐槽核对原版 `ArmorMaterials` 防御点数：皮/金/锁/铁/钻/下界合金各槽点数全部一致（如 iron_chestplate=6、diamond_chestplate=8、netherite_boots=3）。**与原版一致，无偏差。**

### 3.2 护甲减伤公式（正确，逐字对应原版）
`NetworkHandler.java:3838-3841`：
```
reduce = min(20, max(armor/5, armor - damage/(2 + toughness/4)))
damage *= (1 - reduce/25)
```
对照 `CombatRules.getDamageAfterAbsorb`（`mapping/.../damagesource/CombatRules.java:16-19`）：
```
f1 = 2 + toughness/4;  f2 = clamp(armor - damage/f1, armor*0.2, 20);  f3 = f2/25
```
`armor/5 == armor*0.2`，分母 `2 + toughness/4 == f1`，完全一致。**无偏差。**

### 3.3 保护附魔 EPF 公式（正确，逐字对应原版）
`NetworkHandler.java:3862-3864`：`damage -= damage * min(epf,20)/25`。
对照 `CombatRules.getDamageAfterMagicAbsorb`（`:31-34`）：`return dmg * (1 - clamp(epf,0,20)/25)`。完全一致。**无偏差。**（注：原版对“锋利武器降效” `modifyArmorEffectiveness` 未实现，属极边缘特性，可忽略。）

### 3.4 状态效果协议 ID（正确）
`NetworkHandler.java:4201-4215` 的 effect→协议 ID 映射（speed=1 … darkness=33）与 1.21.11 协议完全一致。**无偏差。**

### 3.5 饥饿系统（高度一致，难度相关偏差）`NetworkHandler.java:3919-3965`
逻辑结构与 `FoodData.tick`（`mapping/.../food/FoodData.java:34-75`）对应：
-  exhaustion>4 → 扣 1 饱和或扣 1 饥饿 ✓
- 饥饿满+有饱和+受伤 → 每 10 tick 回血 `min(sat,6)/6` ✓
- 饥饿≥18+受伤 → 每 80 tick 回血 1 ✓
- 饥饿=0 → 每 80 tick 饥饿伤害 1 ✓

**差异（见 Bug #4/#5）**：未实现难度阈值与 `naturalRegeneration` gamerule，且和平模式仍扣饥饿。

### 3.6 进食（偏差）`NetworkHandler.java:1027-1048`
```
data.food = min(20, food + foodValue)
data.saturation = min(food, saturation + foodValue * getSaturationModifier * 2.0)
```
原版 `FoodData.eat(nutrition, saturation)` 中 `saturation` 在食物定义里已是 `nutrition*saturationModifier`（如 cooked_beef = 8×0.8=6.4），**直接加该值，不乘 2**。
- 此处额外 `*2.0` 使饱食度普遍被放大（见 Bug #2）。
- `getFoodValue` 部分值与原版 nutrition 不符（见 Bug #8）。

### 3.7 经验（正确）`NetworkHandler.java:4333-4349`
`xpForLevel` 三段公式（7+2L / 37+5(L-15) / 112+9(L-30)）与原版一致；`addExperience` 溢出进位、进度 `xpTotal/needed` 计算正确。死亡掉落 `min(level*7,100)`（`2222-2258`）与原版一致。**无偏差。**

### 3.8 死亡 / 掉落 / 复活（基本正确 + 偏差）
- 死亡掉落全部 46 槽、经验球拆解（`:2222`）与原版一致。
- 床/重生锚复活点：`:5966-5971`（床）、`:5998-6033`（重生锚消耗电荷）已处理。
- 复活包 0x50 字段顺序（`327-340`）正确。**偏差见 Bug #3（复活清零经验）。**

### 3.9 战斗近战（明显缺口）`NetworkHandler.java:1194-1253`
- 基础伤害 `getAttackDamage`、冷却 `getAttackCooldownMs`、暴击（progress>0.9 且在下落且非疾跑）、击退（按是否疾跑 0.4/0.8）均已实现。
- **未读取玩家武器附魔**：全程只用了 `getAttackDamage(weaponName)`（基础材料伤害），没有锋利(sharpness)加伤、没有击退(knockback)附魔加击退、没有火焰(fire_aspect)点燃目标（见 Bug #6）。
- 攻击冷却判定用 `this.y < this.lastY`（在下落）近似原版 `fallDistance>0 && !onGround`，可接受。

### 3.10 属性包（严重错误）`NetworkHandler.java:4310-4323`
见 Bug #1。

---

## 4. Bug 清单（严重程度 / 文件:行号 / 现象 / 建议修复方向）

### [严重] Bug #1 — 属性包（Update Attributes 0x81）ID 映射使用 1.12 旧顺序，1.21.11 完全错位
- **文件:行号**：`NetworkHandler.java:4310-4323`（调用来自 `:4296` `syncAttributesIfNeeded`）
- **现象**：发送的属性 ID 数组为 `{0,1,2,3,4,5,6,7,8,27,28}`，值 `{20,32,0,0.1,0.4,1,4,0,0,armor,toughness}`。但 **1.21.11 协议属性注册表顺序不同**（权威依据 `json/1.21.11/protocol.json:9626-9657`）：
  - `0=generic.armor`、`1=generic.armor_toughness`、`2=generic.attack_damage`、`3=generic.attack_knockback`、`4=generic.attack_speed`、`19=generic.max_health`、`20=generic.movement_speed`……
  - 即 `armor=0`、`armor_toughness=1`、`max_health=19`。
- **后果**：
  1. 客户端把 id0=20.0 当成 **armor** → 护甲 HUD 永远显示满甲（20），与实际穿戴无关（`syncAttributesIfNeeded` 注释“否则客户端护甲条恒为 0”说明作者已发现症状，但把真实护甲值错发到了 id27 `tempt_range`，客户端忽略）。
  2. 客户端把 id1=32.0 当成 **armor_toughness** → 韧性 HUD 显示 32（超过上限 20）。
  3. `max_health`（id19）从未发送 → 一旦有生命提升类效果，客户端血量上限不一致。
  4. `attack_damage`(id2) 被写成 0、`attack_speed`(id4) 被写成 0.4（原版 4.0）、`movement_speed`(id20) 未发送。
- **建议修复方向**：按 1.21.11 注册表顺序重排。至少必须：`0=armor(实际值)`、`1=armor_toughness(实际值)`、`19=max_health(20.0)`、`20=movement_speed(0.7? 由服务端控制)`、`2=attack_damage`、`3=attack_knockback`、`4=attack_speed`、`16=knockback_resistance`、`17=luck` 等，并核对各属性 base 值（参考 `json/1.21.11/attributes.json` 的 default）。删除末尾的 `27/28` 错误项。

### [中等] Bug #2 — 进食饱食度被放大约 2 倍且 per-food 比率表不一致
- **文件:行号**：`NetworkHandler.java:1030-1031`、`4584-4597`
- **现象**：`data.saturation += foodValue * getSaturationModifier(n) * 2.0`。原版直接加 `foodValue*saturationModifier`（食物内已含该乘积，无 ×2）。且 `getSaturationModifier` 表部分值偏离原版（如 `carrot` 给 0.3，原版比率 0.6；`bread` 给 0.6 原版也是 0.6）。叠加 ×2 后：cooked_beef 加约 9.6（原版 6.4）、golden_apple 加约 19.2 被 clamp 到 food（原版 9.6）。结果饱食度长期顶满，饥饿几乎不会下降，生存难度被严重削弱。
- **建议修复方向**：去掉 `*2.0`；用权威 `json/1.21.11/foods.json` 的 `saturationRatio/32` 作为每点 hunger 的饱食度增量，或直接按食物查表；保证 `clamp(saturation, 0, foodLevel)`。

### [中等] Bug #3 — 复活时无条件清零经验，与 keepInventory gamerule 冲突
- **文件:行号**：`NetworkHandler.java:288-290`
- **现象**：`respawn` 永远执行 `xpLevel=xpTotal=xpProgress=0`，且 `:348` 重发经验包。但当 `keepInventory=true` 时，死亡阶段 `dropInventoryOnDeath`（`:2227`）直接 return **不扣经验**，于是玩家死亡时保留了经验，复活又被清零 → **开了保留物品/经验也会丢经验**。原版 keepInventory 同时保留等级与经验。
- **建议修复方向**：复活清零前判断 `keepInventory` gamerule；为 true 时不重置 XP（保留死亡时 `data` 中的值）。

### [中等] Bug #4 — 饥饿伤害不区分难度，且困难模式无法饿死
- **文件:行号**：`NetworkHandler.java:3953-3964`
- **现象**：`if (food<=0) { starveTimer>=80 && (health>1.0 || gameMode==0) → health-=1 }`。原版 `FoodData.tick`（`:64-69`）按难度：`EASY` 仅 `health>10` 才扣；`NORMAL` 仅 `health>1` 扣；`HARD` 无下限（可饿到 0 致死）。当前实现：生存模式下 `health>1` 恒成立 → 困难模式最低卡在 1 血，**不会饿死**；且简单模式也会在 >10 血时被饿。
- **建议修复方向**：引入 `difficulty` 判断（服务器应已记录难度，参考 `:1580` 的 `parseDiff/setDifficulty`），按原版三档阈值决定 starve 伤害与是否致死。

### [中等] Bug #5 — 饥饿系统忽略难度与 naturalRegeneration gamerule
- **文件:行号**：`NetworkHandler.java:3919-3965`（对照 `FoodData.tick:43,48,57`）
- **现象**：(a) 饥饿衰减（exhaustion>4 扣饥饿）在和平模式也执行，原版仅在 `difficulty != PEACEFUL` 时扣饥饿（`:43`）；(b) 自然回血分支未检查 `naturalRegeneration` gamerule（原版 `:48,57` 以 `bool = gamerule` 为前置），关闭该 gamerule 时仍会回血。
- **建议修复方向**：在饥饿衰减与回血分支前读取对应 gamerule / 难度。

### [中等] Bug #6 — 玩家近战攻击未应用武器附魔（锋利/击退/火焰）
- **文件:行号**：`NetworkHandler.java:1205-1247`
- **现象**：`baseDamage = getAttackDamage(weaponName)` 仅取材料基础伤害；最终 `damage = base*(0.2+progress²*0.8)`，暴击×1.5。全程**未读取** `data.inventoryEnchants[36+heldItemSlot]` 中的 `sharpness`（锋利加伤）、`knockback`（击退）、`fire_aspect`（点燃目标）。结果：附魔剑造成的伤害、击退距离、点燃效果均与原版不符，玩家攻击被系统性低估。
- **建议修复方向**：在 `attackMob` 中读取当前手持槽附魔：锋利按 `1 + 0.5*level`（每级 +1 伤害，原版公式）加算；击退按附魔等级放大 `kb` 并提升 `vy`；火焰按等级对目标 `setOnFire`/调用 `addEffect("fire",...)`（对可燃实体）。

### [轻微] Bug #7 — turtle（海龟）头盔韧性应为 2，代码为 1
- **文件:行号**：`NetworkHandler.java:3761`
- **现象**：`if (n.startsWith("turtle_")) return 1;`。原版 `ArmorMaterials.TURTLE` 的 toughness=2。差 1 点韧性，影响高伤害下的减伤。
- **建议修复方向**：改为 `return 2`。

### [轻微] Bug #8 — `getFoodValue` 部分食物 hunger 值偏离原版
- **文件:行号**：`NetworkHandler.java:4599-4640`（对照 `json/1.21.11/foods.json` `foodPoints`）
- **现象**：cooked_beef / cooked_porkchop 返回 6，原版 nutrition=8（foods.json foodPoints=8.0）；cooked_rabbit 返回 6，原版 5；cooked_salmon 返回 5，原版 6。多数食物正确，但上述几项偏差 1~2 点。
- **建议修复方向**：以 `foods.json` 的 `foodPoints` 为准修正这几项。

### [轻微] Bug #9 — 瞬时效果（instant_health / instant_damage）缩放与基数偏离原版
- **文件:行号**：`NetworkHandler.java:4223-4233`
- **现象**：`instant_health` = `4*(amp+1)`、`instant_damage` = `3*(amp+1)`。原版为指数：`4 * 2^amp`（治疗）、`6 * 2^amp`（对亡灵外伤害，且亡灵受到的是治疗）。amp≥2 时相差明显；且对亡灵（僵尸/骷髅等）未做“伤害↔治疗”反转。此外 `instant_damage` 不走 `damagePlayer`，不受护甲/保护影响（原版 magic 伤害由护甲部分处理，差异较小）。
- **建议修复方向**：改为 `4 * (1<<amp)` / `6 * (1<<amp)`，并判断目标是否为亡灵以反转治疗/伤害。

### [轻微] Bug #10 — 环境伤害节奏（applyEnvSecond 每 20 tick 一次）可能慢于原版
- **文件:行号**：`NetworkHandler.java:3609-3675`
- **现象**：岩浆/火焰/燃烧等所有环境伤害都 `applyEnvSecond`（每 20 tick=1 秒）才施加一次。原版中 `lavaHurt` 每 tick 调用但受无敌帧限制（约每 0.5s 一次 4 点），`onFire` 约每 0.5s 一次 1 点。当前实现岩浆 4/秒、燃烧 1/秒大致为原版一半节奏（注意：原版 `LivingEntity.java` 反编译为空，无法逐字核对，标记为“待核实”）。
- **建议修复方向**：环境伤害改用每 ~10 tick 施加（或保留每 20 tick 但调整单次数值），并用无敌帧控制频率以贴近原版。

### [轻微] Bug #11 — deathMessageFor 缺少若干伤害类型文案
- **文件:行号**：`NetworkHandler.java:3890-3905`
- **现象**：环境伤害写入的 `lastDamageType` 含 `inFire`/`onFire`/`witherRose`/`sweetBerry`，但 switch 无对应 case，全部落到默认“…死了”。死亡提示文案缺失。
- **建议修复方向**：补 `inFire`/`onFire`→“被烧死”、`witherRose`→“被凋零玫瑰杀死了”等 case。

### [轻微] Bug #12 — 盾牌仅能格挡 source="player"/"mob"，且 offhand 兜底不当
- **文件:行号**：`NetworkHandler.java:3813`、`3774-3795`
- **现象**：(a) 盾牌格挡条件硬编码 `source=="player"||"mob"`，原版盾牌同样可格挡弹射物（箭/烟花）与部分爆炸，此处未覆盖；(b) `getShieldSlot()` 当主手/副手都不是盾时仍返回 45（副手），导致 `damagePlayer` 里 `damageHeldItem(45,...)` 可能误扣副手物品耐久。
- **建议修复方向**：按攻击来源类型扩展可格挡集合；`getShieldSlot()` 无盾时返回 -1 并跳过扣耐久。

### [轻微] Bug #13 — 生命提升(health_boost)/吸收(absorption) 效果未实现
- **文件:行号**：`NetworkHandler.java:4218-4242`、`:4168`
- **现象**：`addEffect` 仅处理 instant_health/instant_damage，其余仅发包、不修改 `health` 上限或吸收心。原版 health_boost 提高 maxHealth、absorption 增加吸收心（额外血量），且 `sendHealthUpdate` 的 `health` 字段上限应随 maxHealth 变化（当前硬编码 20、无 maxHealth 属性下发，见 Bug #1）。
- **建议修复方向**：实现 health_boost 的 maxHealth 调整（并随属性包下发 id19）、absorption 的额外血量与独立 HUD 同步。

---

## 5. 结论与优先级建议

**总体结论**：生存系统骨架完整、可运行，且护甲减伤、保护 EPF、效果协议 ID、经验曲线、死亡掉落这几处**与原版逐字一致**，说明核心数学是正确的。但属性同步（Bug #1）是必须立即修复的**严重**缺陷——它让客户端护甲/韧性 HUD 恒为错误值并且永不下发 max_health，直接破坏玩家对防御状态的感知，也暴露出“属性 ID 仍沿用 1.12 旧排序”这一系统性误判。

**修复优先级建议**：
1. P0（严重）：Bug #1 属性包 ID 重排到 1.21.11 注册表顺序。
2. P1（中等）：Bug #6 近战附魔（锋利/击退/火焰）、Bug #2 进食饱食度、Bug #3 复活经验与 keepInventory、Bug #4/#5 饥饿难度与 gamerule。
3. P2（轻微）：Bug #7~#13 数值与文案修正、生命提升/吸收效果、环境伤害节奏核实。

其余（护甲点数表、护甲/保护公式、效果 ID、经验计算、死亡掉落、复活点逻辑）已正确，无需改动，仅建议在回归测试中保持对照。
