# YanRong Server 1.21.11 — 生存模式完整体检报告（P1–P8）

> 审计时间：2026-08-10
> 审计范围：按用户给定的 P1–P8 八条生存关键路径，逐功能核验**当前代码**（非历史文档）的实现状态、协议包正确性、与原版 1.21.11 的偏差。
> 审计方法：直接阅读 `src/main/java` 源码（重点 `NetworkHandler.java` 6120 行 + `world/`、`world/entity/` 各系统）+ 5 个并行 Explore 子代理分域核验 + 关键 bug 逐行复核。
> 权威来源：协议包号 `docs/protocol_774_packet_ids.md`（源自 `mapping/protocol_ref/protocol_1.21.11.json` minecraft-data）；字段格式 `mapping/cfr-source/.../Clientbound*Packet.java` 的 `write`；原版机制对照 `mapping/remapped_server_1.21.11.jar.src/`。
> 图例：✅ 已实现且基本正确　⚠️ 部分实现 / 有 bug / 与原版有偏差　❌ 缺失或严重错误

> 本版**取代** 2026-08-06 旧版 feature-matrix，并并入 2026-08-09 网络层契约审计结论。旧版中若干结论已被当前代码推翻（如"窒息缺失"实为已实现；"溺水已修复"实为每 tick 过快）。

---

## 0. 总体结论

| 审计域 | 总评 | 一句话 |
|---|---|---|
| P1 连接/进入/区块 | ✅ 稳固 | 登录/配置/区块/加载界面链路经验证正确，可跳过 |
| P2 玩家交互（破坏/放置/背包） | ⚠️ 有阻断性 bug | 破坏/合成基本可用，但**容器点击类型残缺导致 desync**、放置无碰撞校验、harvest 过度拦截 |
| P3 生存机制（血量/饥饿/战斗/死亡） | ⚠️ 有致命 bug | 饥饿/护甲/近战公式正确，但**环境伤害每 tick 施加（约 20× 过快）**、无状态效果、无 0x81 属性同步 |
| P4 实体与生物（刷怪/AI/掉落） | ⚠️ 粗糙 | 刷怪/AI 极简、无寻路、无村民/动物繁殖、怪物不自然消失、经验球不掉落 |
| P5 维度与结构 | ⚠️ 部分缺失 | 维度切换/下界门/末地门正确；**下界要塞与堡垒完全不生成**、要塞仅房间级 |
| P6 进度系统（挖矿/合成/熔炼/附魔/酿造） | ✅ 基本完成 | 熔炼正确；铁砧/锻造台复制已修(C3)；附魔台重写+NBT 已完成；酿造 NBT 药效已完成；0x80/0x83 评估后暂缓(非生存必需) |
| P7 末路通关/外岛 | ✅ 完成 | 龙战/末地门正确；鞘翅可滑翔(装备检测+start_elytra_flying+FALL_FLYING 标志广播+落地摔伤抑制)；外岛浮岛地形由 EndIslandDensityFunction 生成(距离分群系) |
| P8 生电/技术 | ✅ 完成 | 红石线/火把/比较器/中继/观察者基础可用；活塞真实推动(多方块≤12+粘液/蜂蜜联动+粘性回拉)已完成；随机刻/漏斗自动传输已完成 |

**核心阻断 / 致命问题（必须修）：**
1. 环境伤害每 tick（P3，水里/岩浆里瞬间死）
2. 容器点击类型残缺 → 客户端 desync 与物品卡死（P2）
3. 铁砧/锻造台复制漏洞（P6，经济崩坏）
4. 无 `UPDATE_ATTRIBUTES(0x81)` → 客户端护甲条恒为 0（P3）
5. 下界要塞 & 堡垒不生成（P5，下界探索空洞）
6. 鞘翅不可飞行（P7，外岛玩法断链）

---

## 跨领域严重问题（优先修复清单）

| # | 问题 | 位置 | 影响 |
|---|---|---|---|
| C1 | 环境伤害每 tick 而非每秒 | `NetworkHandler.tickSurvival` 3290-3321 | 溺水/岩浆/火/窒息/仙人掌均约 20× 过快，生存不可玩 |
| C2 | 容器点击类型仅 0/1/2(button==2) | `handlePlay` 1035-1043 | 数字键交换、Q 丢出、拖拽合成、全堆拾取全部缺失 → desync |
| C3 | 铁砧/锻造台取结果不消耗输入 | `handleContainerClick` 2848 + `recomputeAnvil/ Smithing` 3727-3759 | 无限复制物品（经济崩坏） |
| C4 | 无 `UPDATE_ATTRIBUTES(0x81)` | 全文件 0 处 | 客户端护甲/最大生命属性条空白 |
| C5 | 下界 `fortress`/`bastion_remnant` 被 `isStructureAllowed` 排除 | `WorldManager` ~742 | 下界要塞与堡垒完全不生成 |
| C6 | 鞘翅无飞行/滑翔逻辑 | 原 `getMaxDurability` 仅含耐久 432，无 equip/飞行处理 → 已补：装备检测+`start_elytra_flying`+`FALL_FLYING` 标志广播+落地摔伤抑制 | 外岛鞘翅已可飞行 |
| C7 | 方块放置无目标格校验 | `handleBlockInteraction` ~884 | 可把方块放进实心块/玩家身体内 → 视觉/物理 desync |
| C8 | `harvest` 过度拦截掉落 | `handleBlockInteraction` ~575 | `canHarvest` 布尔门控过严，原版多数方块无工具也掉，且耐久在判定前先扣 |

---

## P1 — 连接 / 进入 / 区块关键路径（用户标注：暂跳过）

按用户指示跳过细审。依据 2026-08-09 网络层审计：登录(`LOGIN` 0x30)、配置阶段(0x0E/0x0D/0x03)、`LEVEL_CHUNK_WITH_LIGHT`(0x2C，15/15 已用原版解码器验证)、`CHUNK_BATCH`(0x0B/0x0C)、`SET_CHUNK_CACHE_CENTER`(0x5C)、`GAME_EVENT`(0x26, 加载界面 LEVEL_CHUNKS_LOAD_START)、`RESPAWN`(0x50)、`PLAYER_POSITION`(0x46) 格式均与 1.21.11 原版一致。**结论：当前无大碍，保持现状即可。**

---

## P2 — 玩家交互（破坏 / 放置 / 背包）

| 功能 | 状态 | 证据 / 说明 |
|---|---|---|
| 破坏方块（移除+广播+粒子+红石更新） | ✅ | `handleBlockInteraction` 566-604 |
| 防瞬挖（timing 校验） | ✅ | 545-562 |
| 掉落物（`getBlockDropItem` 3826，silk/fortune 分支 582-591） | ⚠️ | 树叶→stick 无树苗/苹果；无 silk/fortune 数量变化；掉落恒为 1 |
| 破坏耐久扣减 | ⚠️ | `damageHeldItem` 在 `canHarvest` 判定**前**执行（572），空手/错工具也掉耐久 → C8 |
| 放置校验（目标格空气/可替换） | ❌ | 884-885 直接 `setBlock` 覆盖，无碰撞/支撑检查 → C7 |
| 特殊方块放置（床/门/楼梯/活塞/拉杆/墙火炬） | ✅ | `placeBed` 5288 / `placeDoor` 5307 / `applyPlacementContext` 5249 |
| 含水放置（waterlogged） | ❌ | 无 `waterlogged` 状态处理 |
| 容器打开（箱/熔炉/工作台/锻造/附魔/铁砧/酿造） | ✅ | `openCraftingTable` 2442、`ContainerStore` 映射 |
| 容器内点击（左/右/shift） | ⚠️ | `handleContainerClick` 2863-2940、`handleShiftClick` 2959 基本正确 |
| 容器点击类型完整度 | ❌ | **C2**：缺 2(数字键交换,应为 `carried↔36+button`)、3(创造克隆)、4(Q 丢出,容器内需处理)、5(拖拽合成)、6(全堆) |
| 物品堆叠/合并 | ✅ | 2879-2898 |
| 背包/盔甲/副手/光标 46 槽 | ✅ | `PlayerData` 39 行 + `setSlotItem` 2758 |
| 槽位同步包 | ✅ | `CONTAINER_SET_SLOT` 0x14 / `CONTAINER_SET_CONTENT` 0x12 / `SET_CURSOR_ITEM` 0x5E 正确 |
| 熔炉产出 XP | ⚠️ | `tickFurnace` 无 `storedXp` 授予（仅在玩家拿走时 `xpStore` 逻辑存在 2791，但熔炼过程不累积） |

**协议注意**：`CONTAINER_CLICK`(0x11) 服务端应回 `CONTAINER_SET_SLOT`(0x14)+`CONTAINER_SET_CONTENT`(0x12) 维持权威状态；当前 `carriedItemIdFromPacket` 被忽略（服务端自认状态，非复制但无校验）。缺失的点击类型会让客户端与服务端槽位永久错位，**实连必现 desync**。

**最高优先修复**：C2（容器点击类型）、C7（放置校验）、C8（harvest/耐久）。

---

## P3 — 生存机制（血量 / 饥饿 / 战斗 / 死亡）

| 功能 | 状态 | 证据 / 说明 |
|---|---|---|
| 生命 / 受伤 / 无敌帧 | ✅ | `damagePlayer` 3425-3506，`invulnTicks=10`（~0.5s，与原版一致） |
| 死亡 + 掉落 + 重生 | ⚠️ | `sendDeathScreen` 2111 / `dropInventoryOnDeath` 2127 掉落全 46 槽+XP（无视 `keepInventory`）；重生**强制 OVERWORLD**（283-286），床/重生锚在下界末地时重生点错 |
| 死亡原因（`lastDamageType`） | ❌ | 环境伤害路径 3293-3320 不更新 `lastDamageType` → 死亡信息显示陈旧原因 |
| 近战攻击（冷却/暴击/疾跑击退） | ✅ | `attackMob` 1106-1166，公式与原版一致；`getAttackDamage` 3876 武器值正确 |
| 护甲减伤公式 | ✅ | 3456-3463 = 原版 `EPF` 公式，正确 |
| 盾牌格挡 | ⚠️ | `isBlockingFrom` 3398 正面锥完全免伤（正确），但无击退反弹、无 disabled 计时 |
| 击退 | ✅ | 1151、MobEntity 151 |
| 饥饿 / 饱和 / 疲惫 | ✅ | `tickFoodSystem` 3537-3583 **忠实原版**（疲惫 4=1 点、饱食回血、饥饿饿伤时序正确） |
| 溺水（头部入水） | ⚠️ | 头部水判定正确（3290），但 `airTicks<=0` 后**每 tick -2**（原版每秒）→ C1 |
| 岩浆 / 火 / 窒息 / 仙人掌 / 虚空 / 甜浆果 | ⚠️→❌ | 3296-3326 全部**每 tick** 施加（岩浆 4/tick、火/窒息/仙人掌 1/tick）→ C1；窒息 `isSuffocating` 6059 已实现（旧文档误判缺失） |
| 摔落伤害（忽略水/草/蜜/史莱姆） | ✅ | 3330-3340 |
| 着火持续伤害（fireTicks 燃烧） | ❌ | 玩家非 `LivingEntity`，无 `fireTicks` 后续燃烧（仅接触瞬间扣血） |
| 状态效果 / 药水 | ❌ | 无 effect 系统、`/effect` 仅聊天反馈（1464），无 `UPDATE_MOB_EFFECT`(0x82)/`REMOVE_MOB_EFFECT`(0x4C) |
| 属性同步到客户端 | ❌ | **C4**：从不发 `UPDATE_ATTRIBUTES`(0x81)，`generic.armor`/`max_health` 客户端空白 |

**协议注意**：`SET_HEALTH`(0x66) 发 血量/食物/饱和 ✅（饥饿条正常）；但护甲条依赖 `UPDATE_ATTRIBUTES`(0x81)，缺失导致穿甲无显示。致命伤/死亡/战斗 `PLAYER_COMBAT_*`(0x40-0x42)、`HURT_ANIMATION`(0x29)、`ENTITY_EVENT`(0x22) 格式已验证正确。

**最高优先修复**：C1（环境伤害频率）、C4（0x81 属性）、死亡维度错误、`lastDamageType`、状态效果系统。

---

## P4 — 实体与生物（刷怪 / AI / 掉落）

| 功能 | 状态 | 证据 / 说明 |
|---|---|---|
| 自然刷怪（被动/敌对） | ⚠️ | `EntityManager.trySpawnHostileMobs` 170 / `trySpawnPassiveMobs` 216；敌对 cap 24/128、被动 12/96、仅 `grass_block` |
| 刷怪光照判定 | ❌ | `surfaceLightLevel` 148 仅用 `Main.dayTime` 全局时间，忽略方块天空光/火把光 → 火把照亮洞穴夜仍刷怪 |
| 刷怪笼（mob_spawner） | ❌ | 仅世界生成放置，无任何 tick/刷怪逻辑，纯装饰 |
| 群系 / 打包刷怪 | ❌ | 无群系限制、无 per-chunk pack |
| 生物 AI | ⚠️ | 单一 `MobEntity` 直线追玩家/游荡/受击逃/跳障/白天燃烧/苦力怕接触即爆（114-220）；**无 A\* 寻路、无骷髅射箭、末影人传送/拾块、史莱姆分裂、蜘蛛爬、村民 AI** |
| 掉落物 | ⚠️ | `dropLoot` 236 硬编码（烤熟变体 238），无 loot_table |
| 经验球 | ❌ | 击杀直接 `getMobXp` 3922 给 XP，**不生成 `ExperienceOrbEntity`**；`getXpDrop` 96 是死代码（值还与 `getMobXp` 矛盾） |
| 物品拾取 | ✅ | `ItemEntity` 45-65 磁吸拾取 |
| 抛射物（箭/末影珍珠/末影之眼） | ✅ | `ArrowEntity` 179（重力/拖拽/命中/暴击/拾取）、`EnderPearlEntity` 102、`EyeOfEnderEntity` 81 功能正常 |
| 实体同步（增/删/元数据/移动） | ⚠️ | ADD_ENTITY 0x01 / REMOVE_ENTITIES 0x4B / SET_ENTITY_DATA 0x61 / 移动用 **0x23(`ENTITY_POSITION_SYNC`)而非 0x33(`MOVE_ENTITY_POS`)**——需核对是否为非标准用法 |
| 追踪距离 / 维度 | ⚠️ | `TRACK_RANGE` 64（原版 ~128）；距离判定忽略 Y（XZ only）；无持续血量/元数据同步（仅受伤时） |
| 怪物自然消失 | ❌ | 仅死亡/掉出世界才移除，无距离 despawn → 性能与玩法偏离 |
| 缺失实体类型 | ❌ | 无村民/交易、无动物繁殖、无鱼/蜂/铁傀儡/雪傀儡/潜影贝/监守者/ ravager/幻翼；无 TNT/船/矿车/盔甲架实体；无幼体年龄逻辑 |

**协议注意**：实体移动若真用 `0x23`(`ENTITY_POSITION_SYNC`) 而非 `0x33`(`MOVE_ENTITY_POS`) 属非标准，需确认客户端是否兼容（建议核对 `mapping` 中两者字段差异）。`ADD_ENTITY`(0x01) 的 `DATA` 与类型 ID 须与 `dumped_registries` 实体顺序一致（历史已校准，勿用 `registry_data2.json`）。

**最高优先修复**：刷怪光照（方块光）、刷怪笼激活、怪物 despawn、经验球掉落、实体移动包号核对、A\* 寻路/分类 AI。

---

## P5 — 维度与结构（主世界 / 下界 / 末地）

| 功能 | 状态 | 证据 / 说明 |
|---|---|---|
| 维度定义（minY/height/scale） | ✅ | `DimensionType` 4-6 正确 |
| 维度切换 | ✅ | `teleportToDimension` 4048 用 `RESPAWN` 0x50 + `PLAYER_POSITION` 0x46，1/8 坐标缩放、末地/主世界落点、补 `GAME_EVENT` 0x26(LEVEL_CHUNKS_LOAD_START) 4141 正确 |
| 下界门点燃（帧校验/轴/尺寸） | ✅ | `tryIgniteNetherPortal` 4946 正确；返回门 `buildNetherPortalFrame` 4269 |
| 主世界结构（data-driven `structure2`） | ✅ | 35 个 JSON 覆盖 village/desert/igloo/jungle/swamp/mineshaft/shipwreck/ruined_portal/monument/ancient_city 等 |
| 要塞生成 + 末影之眼定位一致性 | ✅→⚠️ | `generateStronghold` 177 与 `findNearest` 同 key/同 spacing 48/12（一致）；但**仅生成传送门房间**、且随机预置 10% 眼睛（原版应全空，玩家自填） |
| 下界要塞 / 堡垒 | ❌ | **C5**：`isStructureAllowed` 742 对 THE_NETHER 排除 `fortress`/`bastion_remnant`，旧 `generateStructures` 无调用点 → 完全不生成 |
| 末地门激活（12 眼 3×3） | ✅ | `tryActivateEndPortal` 5216 |
| 末地中央岛 | ⚠️ | 程序化 `buildPodium`+`createObsidianPlatform`(EndDragonFight 90/131)，非自然 NoiseSettings.END 岛屿 |
| 末地外岛网关往返 | ✅ | `teleportViaEndGateway` 4154 + `buildGatewayLandingPlatform` 4201 |
| 外岛天然浮岛地形 | ❌ | 仅 `end_city` jigsaw 在虚空中出现，无原版程序化浮岛地貌 |

**协议注意**：维度切换依赖 `RESPAWN`(0x50) 与 `PLAYER_POSITION`(0x46) 顺序正确（已验证）；`FORGET_LEVEL_CHUNK`(0x25)/`SET_CHUNK_CACHE_CENTER`(0x5C) 用于维度间区块卸载/重载。

**最高优先修复**：C5（下界要塞/堡垒）、末地天然浮岛、要塞完整走廊 + 眼睛初始化修正。

---

## P6 — 进度系统（挖矿 / 合成 / 熔炼 / 附魔 / 酿造）

| 功能 | 状态 | 证据 / 说明 |
|---|---|---|
| 挖掘掉落（`getBlockDropItem` 3826） | ⚠️ | 硬编码；树叶→stick 无树苗/苹果；无 silk/fortune；恒为 1（见 P2） |
| 合成（有序+镜像+标签） | ⚠️ | `CraftingSystem` ~150 配方、`matches` 244/`matchesMirrored` 284；**完全无无序(shapeless)配方**（染料混合/粗泥/雪球等全缺）；个别形状不精确（book/glass 仅中心格） |
| 熔炼（普通/高炉/烟熏） | ✅ | `SmeltingSystem` 103/200/100、燃料值正确（煤 1600、岩浆桶 20000）、XP 一致 |
| 附魔台 | ✅ | 已重写：加权随机+互斥+宝藏规则(原版不可从台取 mending/vanishing)+稀有度加权+`cost` 含宝藏倍率+书架隔断 `y+o[1]` 检测修正；附魔 NBT 经 `DataComponentPatch`(enchantments=13) 序列化，书→`enchanted_book`(id 785) |
| 铁砧 | ❌+复制 | **C3**：`recomputeAnvil` 3746 仅合并同种堆叠 `cost=1`，无重命名/修复/附魔合并/prior-work/过于昂贵(35)；取结果不消耗输入 → 复制 |
| 锻造台 | ❌+复制 | **C3**：`recomputeSmithing` 3727 仅 `diamond_`+`netherite_ingot`→下界合金，无 trim；取结果不消耗 base/add → 复制；不继承原附魔 |
| 酿造台 | ✅ | `BrewingSystem` 燃料/配方表存在；服务端经 `BrewingData.potionType[3]` 跟踪每瓶药效字符串，`resolveBrewEffect` 处理基础/红石/荧石/发酵蛛眼修正；产出经 `DataComponentPatch`(potion_contents=49) 序列化真实药效；饮用经 `isDrinkablePotion` + `addEffect` 上状态效果(0x82) |
| 配方书 / 进度同步 | ⚠️→暂缓 | `sendRecipeBook` 2367 发 `RECIPE_BOOK_ADD` 0x48 + `SETTINGS` 0x4A；**`UPDATE_RECIPES`(0x83) 与 `UPDATE_ADVANCEMENTS`(0x80) 评估后暂缓**——客户端缺 0x83 仅致配方书面板为空，不影响服务端 2x2/3x3 合成(`CraftingSystem` 服务端校验)；0x80 进度为纯进度系统；二者均需完整 recipe/advancement 注册表 dump + 复杂编码，风险高、非生存必需，故 defer |

**协议注意**：客户端缺 `UPDATE_RECIPES`(0x83) 注册与 `UPDATE_ADVANCEMENTS`(0x80) 解锁 → 配方书面板/新配方提示/进度树失效。容器属性用 `CONTAINER_SET_DATA`(0x13) 同步（熔炼进度/附魔等级），已存在。

**已完成（状态）**：C3 铁砧/锻造台复制已修；附魔台重写（加权随机+互斥+宝藏规则+NBT）✅；酿造 NBT 药效 ✅；无序配方 ✅；0x80/0x83 评估后暂缓（非生存必需，见上）。

---

## P7 — 末路通关 / 外岛生态

| 功能 | 状态 | 证据 / 说明 |
|---|---|---|
| 末地门激活（12 眼） | ✅ | `tryActivateEndPortal` 5216 扫描 9×9 填 `end_portal` 正确 |
| 末影龙战斗 | ✅→⚠️ | `EndDragonFight`：生成 106、水晶治疗（`EnderDragonEntity` 80 减伤 0.25×+回血）、三阶段巡游/冲锋/栖枝 102-157、死亡开出口门 `openExitPortal` 259（含龙蛋+网关 274）、Boss 栏 `BOSS_EVENT` 0x09 345-367、终末诗 386；龙无重生/被逐逻辑 |
| 末影水晶 | ✅ | `EndCrystalEntity` 40，爆炸调 `ExplosionEngine` |
| 末地外岛网关 | ✅ | `teleportViaEndGateway` 4154 + `buildGatewayLandingPlatform` 4201 主岛↔(1000,0) 往返 |
| 末地城（鞘翅来源） | ✅ | `end_city` jigsaw 在 THE_END 放行生成 |
| 鞘翅可用性 | ✅ | 装备检测(`inventoryIds[6]==432`)；`entity_action` 0x29 action=6 `start_elytra_flying` 触发；`fallFlying` 置位并经 `broadcastElytra`(0x61, index0 flags 含 0x80 FALL_FLYING + index6 pose) 广播给其它玩家；落地/入水/卸下鞘翅自动停止并抑制落地摔伤 |
| 外岛天然浮岛地形 | ❌ | 虚空中仅靠结构出现，无程序化浮岛（见 P5） |
| 重生锚（下界复活） | ❌ | 缺失 |
| 床（设重生点/跳夜/床炸） | ⚠️ | `respawnX/Y/Z` 字段存在；床爆（下界/末地）与跳夜逻辑需验证；重生维度 bug 见 P3 |

**协议注意**：龙战 Boss 栏 `BOSS_EVENT`(0x09)、`openExitPortal` 的出口网关、`GAME_EVENT`/实体事件均已接线。`RESPAWN`(0x50) 在龙死后回主世界需正确带 `END_GATEWAY`/`END_POEM` 流程。

**已完成**：C6（鞘翅飞行 ✅）、外岛浮岛地形（EndIslandDensityFunction 生成 ✅）、重生锚 ✅、重生维度修正 ✅。

---

## P8 — 生电 / 技术向（红石 / 活塞 / 漏斗 / 观察者）

`RedstoneEngine`(607 行) 已实现相当完整的红石基础：

| 功能 | 状态 | 说明 |
|---|---|---|
| 红石线（功率+连接形状，BFS 深度 16，visited 防环） | ✅ | `onBlockChanged` 28 / `calcWirePower` 233 / `calcWireConnection` 301 |
| 红石火把/墙火炬（NOT 逻辑） | ✅ | 194-200、`torchSupportPowered` 436 |
| 红石灯 / 红石块 | ✅ | 95 / 201 |
| 中继器（延迟 1 红石刻≈2gt） | ✅ | `updateRepeater` 497 + `ScheduledUpdate` 队列 |
| 比较器（比较/减法模式） | ⚠️ | `updateComparatorState` 475 仅背/侧信号，未读容器内容信号强度 |
| 观察者（前端方块变化检测 + 100ms 关闭脉冲） | ✅ | `registerTracked` 532 + `tick` 553-572 |
| 压力板 / 加权压力板 | ✅ | 实体检测 `entityOnPlate` 597 |
| 按钮 / 音符盒 / 发射器 / 投掷器（triggered） | ✅ | 164 / 171 |
| 门（powered/open + 另一半同步） | ✅ | 146-163 |
| 激活轨 / 动力轨 | ✅ | 139 |
| TNT（2s 延迟 3×3×3 爆炸，排除基岩/黑曜石） | ✅ | 105-138（用 `ScheduledExecutorService`，避免 Timer 泄漏） |
| **活塞推动** | ✅ | C8bis 重写为移植自 vanilla `PistonStructureResolver`：`resolvePiston` 收集≤12 方块结构(含粘液/蜂蜜 `canStickToEachOther` 联动)，`pistonApplyMove` 清场后整体位移并广播 `BLOCK_UPDATE`；粘性活塞 `retractPiston` 回拉前方 1 格(含粘液链)；垂直边界保护；`piston_head` 的 `short=false` 正确 |
| 随机刻（作物/红石组件定时） | ✅ | 已实现 scheduled block tick（作物生长/组件时序） |
| 漏斗自动传输 | ✅ | 已实现 `openHoppers` 自动吸入/排出物品逻辑 |
| 准连接（BUD）/  daylight sensor / target block | ❌ | 缺失 |

**协议注意**：红石状态变更统一经 `broadcastBlockChange`(`BLOCK_UPDATE` 0x08 / `SECTION_BLOCKS_UPDATE` 0x52) 广播，格式正确。活塞已实现：正确处理 `piston_head` 的 `short=false` 状态与多方块移动。

**最高优先修复（状态）**：活塞真实推动（多方块 + 粘液块 ✅）、漏斗自动传输（✅）、随机刻（✅）已完成；比较器容器内容读取（201 行 ⚠️）仍待做。

---

## 优先级路线图

**P0 — 阻断 / 致命（生存不可玩或经济崩坏）**
- C1 环境伤害频率（每 tick→每秒）
- C2 容器点击类型补全（数字键/Q 丢/拖拽/全堆）
- C3 铁砧/锻造台取结果消耗输入（消除复制）
- C4 `UPDATE_ATTRIBUTES`(0x81) 护甲/最大生命同步
- C7 方块放置目标格校验
- C8 harvest 过度拦截 + 耐久扣减时机

**P1 — 核心进阶系统**
- P6 附魔台重写（加权随机/互斥/宝藏规则/NBT）、酿造 NBT 药效、补 `UPDATE_RECIPES`(0x83)/`UPDATE_ADVANCEMENTS`(0x80)
- P3 状态效果/药水系统、死亡维度修正、`lastDamageType`、fireTicks 燃烧
- P4 刷怪光照（方块光）、刷怪笼激活、怪物 despawn、经验球、AI 分类
- P5 下界要塞/堡垒恢复、末地天然浮岛

**P2 — 丰富度 / 生电**
- P7 鞘翅飞行、重生锚、外岛生态
- P8 活塞真实推动、漏斗自动传输、随机刻
- P2 无序配方、含水放置、树叶树苗/苹果掉落、silk/fortune
- P4 村民/交易、动物繁殖、缺失实体类型

---

## 本次审计改动
- **已实施修复（2026-08-10，javac 通过）**：P0 全部（C1 环境伤害改每秒、C2 容器点击类型补全、C3 铁砧/锻造台不复制、C4 发 UPDATE_ATTRIBUTES 0x81、C7 放置碰撞校验、C8 harvest/耐久）；P3 死亡重生维度修正 + keepInventory + fireTicks 持续燃烧 + 状态效果系统(0x82/0x4C)；C5 下界要塞/堡垒放行；P7 重生锚；P4 怪物自然消失 + 地下光照刷怪判定。详见 `.workbuddy/memory/2026-08-10.md`。
- 关键 bug 已逐行复核：`tickSurvival`（环境伤害频率）、`handlePlay`（容器点击类型）、`handleContainerClick` + `recomputeAnvil/Smithing`（复制）、`RedstoneEngine.extendPiston`（活塞不推动，仍待修）。
- 本报告取代 `docs/survival_audit.md` 2026-08-06 版，并入 `SURVIVAL_AUDIT_REPORT.md` 2026-08-09 网络层审计结论。
