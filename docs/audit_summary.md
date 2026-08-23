# YanRong Server 1.21.11 生存/创造/地形 大审计总览索引

> 审计对象：`com.yanrong.server` 自研 MC 1.21.11（Protocol 774，Java 21 + Netty）核心
> 索引生成日期：**2026-08-11**
> 覆盖域数：**17 域**（生存 12 域 + 创造 3 域 + 地形 2 域）

---

## 1. 开篇说明

### 1.1 审计范围
本次大审计对自研核心的 **17 个域** 完成纯代码级审计，每份报告独立落盘于 `docs/audit_*.md`：

| 域组 | 域 |
|---|---|
| 生存（12） | P1 连接/进入/区块、P2 玩家交互/背包、P3 生存机制、P4 实体与生物、P5 维度与传送门、P6 进度系统、P7 末路与外岛、P8 生电/技术向、P9 存储、P10 流体、P11 种植、P12 成就 |
| 创造（3） | 创造 P1 指令、创造 P2 模式切换、创造 P3 中键选中 |
| 地形（2） | 地形 P1 结构、地形 P2 噪声 |

### 1.2 审计铁律
- **只审计不修**：全部 17 份报告均仅读取源码 + 反编译对照，**未修改任何 `.java` 文件**，未编译、未启动服务器（少量“需实测”项已在报告中标注）。
- **对照原版反编译 + 协议文档**：原版对照源为 `mapping/remapped_server_1.21.11.jar.src/`（含 cfr 可读源）、`mapping/cfr-source/`。
- **协议权威参考**：
  - `mapping/protocol_ref/protocol_1.21.11.json`（type/schema 结构）
  - `json/1.21.11/protocol.json`（包号/字段顺序/枚举权威）
  - wiki.vg 协议文档

### 1.3 严重度说明（统一口径）
各源报告使用的严重度词不尽一致，本索引按统一口径映射到 **P0 / P1 / P2 / P3**：
- **P0（崩溃级）**：会导致服务端崩溃 / 真实 JVM 异常。本审计**未确认任何真实崩溃级缺陷**（已知“崩溃坑”在 P1 域已核实修复），故本总览 **P0 计数 = 0**。
- **P1（功能性报废 / 关键路径不可用）**：对应源报告中的“严重”项 —— 特征为某功能完全不可用（如木桶不可开、流体不流动、进阶红石中断、附魔/酿造算法整体偏离）。
- **P2（明显影响玩法但可绕行）**：对应源报告中的“中等”项。
- **P3（协议非原版 / 小众路径 / 低影响）**：对应源报告中的“轻微 / 轻 / 低 / 性能”项。

> 注：部分源报告在其“优先级建议”小节内将个别“严重”缺陷标为“P0（立即）”，意指**修复优先级最高**；按本统一严重度口径（无真实崩溃 → 不升 P0），这些缺陷在总览中统一归入 **P1**。本索引保留源报告的编号与行号，仅在“严重度”列做统一映射。

---

## 2. 域覆盖总表（17 行）

> 计数口径：各域按**自身文档**的 Bug 表计数（含交叉引用项）。跨域交叉引用在主 Bug 清单（§3）中只计一次，另见 §3 首部说明。

| 域编号 + 名称 | 报告文件 | 实现总评（摘自各文档 §1） | P0 | P1 | P2 | P3 |
|---|---|---|---|---|---|---|
| P1 连接/进入/区块 | audit_p1.md | 高保真（关键路径可用，已知崩溃坑已修复） | 0 | 0 | 1 | 2 |
| P2 玩家交互/背包 | audit_p2.md | 部分实现 / 协议高保真（配方/木桶/多人同步缺口） | 0 | 0 | 4 | 5 |
| P3 生存机制 | audit_p3.md | 部分实现（核心数学正确，属性同步严重错误） | 0 | 1 | 5 | 7 |
| P4 实体与生物 | audit_p4.md | 部分实现（刷怪→击杀链路可用，刷怪/AI 偏差大） | 0 | 0 | 5 | 4 |
| P5 维度与传送门 | audit_p5.md | 基本可用（发包齐全，元数据/时间硬编码偏差） | 0 | 0 | 2 | 5 |
| P6 进度系统 | audit_p6.md | 部分实现（熔炼贴近原版，附魔/酿造严重偏差） | 0 | 2 | 8 | 5 |
| P7 末路与外岛 | audit_p7.md | 部分实现（主流程可通，鞘翅/末地门/要塞关键缺失） | 0 | 3 | 6 | 4 |
| P8 生电/技术向 | audit_p8.md | 部分实现（主干被 2 个严重 Bug 阻断） | 0 | 2 | 9 | 4 |
| P9 存储 | audit_p9.md | 部分实现（核心可用，木桶/陷阱箱/双箱/NBT 缺失） | 0 | 2 | 3 | 2 |
| P10 流体 | audit_p10.md | 部分实现（反应产物正确，蔓延缺失 + 泄漏） | 0 | 1 | 2 | 3 |
| P11 种植 | audit_p11.md | 部分实现（引擎可用，光照/灌溉/耕地机制缺失） | 0 | 2 | 3 | 3 |
| P12 成就 | audit_p12.md | 缺失（0% 实现，整系统无代码） | 0 | 0 | 1 | 0 |
| 创造 P1 指令 | audit_creative_p1.md | 部分实现（指令齐全，缺 @选择器 / 权限系统） | 0 | 2 | 6 | 2 |
| 创造 P2 模式切换 | audit_creative_p2.md | 部分实现（核心切换可用，模式约束不完整） | 0 | 1 | 5 | 1 |
| 创造 P3 中键选中 | audit_creative_p3.md | 已实现（协议解析正确，组件/守卫缺失） | 0 | 0 | 2 | 1 |
| 地形 P1 结构 | audit_terrain_p1.md | 高保真（nbt 模板可用，要塞仅近似） | 0 | 0 | 3 | 4 |
| 地形 P2 噪声 | audit_terrain_p2.md | 高保真（噪声数学逐行一致，2 处一致性偏差） | 0 | 0 | 2 | 1 |
| **合计** | — | — | **0** | **16** | **67** | **53**（域计数）/ **133**（主清单去重） |

> 域计数合计 136 个缺陷条目；主 Bug 清单（§3）去重交叉引用后为 **133** 条（P1=16 / P2=64 / P3=53）。

---

## 3. 主 Bug 清单（按严重度降序：P0 → P1 → P2 → P3）

### 交叉引用说明（已在主清单合并，不重复计级）
- **木桶无法打开**：以 **P9-B1** 为主计，`audit_p2.md` 的 P2-B1 合并至此（见 P9-B1 行）。
- **陷阱箱红石 / 双箱合并 / 容器 NBT 丢失**：以 **P9-B2 / P9-B3 / P9-B4** 为主计，`audit_p2.md` 的 P2-B4（标注“交叉引用 P9”）合并至此。
- **/gamemode 切模式不同步飞行**：以 **创造 P1-3** 为主计，`audit_creative_p2.md` 的 P2-4 合并至此。

---

### 3.1 P1 级（功能性报废 / 关键路径不可用，共 16 条）

| 编号 | 严重度 | 所属域 | 文件:行号 | 现象（一句话） | 建议修复方向（一句话） |
|---|---|---|---|---|---|
| P3-#1 | P1 | P3 生存机制 | `NetworkHandler.java:4310-4323` | 属性包 Update Attributes 按 1.12 旧顺序，护甲/韧性 HUD 恒错、max_health 永不下发 | 按 1.21.11 注册表重排属性 ID 与 base 值，删除末尾 27/28 |
| P6-B1 | P1 | P6 进度系统 | `NetworkHandler.java:428-429` + `BrewingSystem.java:38-40` | 红石/荧石/发酵蛛眼修饰酿造因瓶名未变导致 canBrew 恒 false，全部失效 | canBrew 同时判断“效果字符串变化”而非仅比较物品名 |
| P6-B2 | P1 | P6 进度系统 | `EnchantSystem.java:101,128,131`；`NetworkHandler.java:4100,4056,4039,4049` | 附魔等级/消耗/种子/多选/青金石全为自研近似，与原版差异巨大 | 按 `EnchantmentHelper` 实现 power budget→minCost/maxCost 选级、稳定种子、n+1 青金石、可叠 1–3 附魔 |
| P7-B1 | P1 | P7 末路与外岛 | `json/minecraft/loot_table/chests/end_city_treasure.json`；`NetworkHandler.java:6256-6264` | 末地船宝箱战利品表不含 elytra，鞘翅不可获得、滑翔玩法无法体验 | 在 end_city_treasure.json 增加 elytra 条目（weight 1） |
| P7-B2 | P1 | P7 末路与外岛 | `StrongholdPortalRoomGenerator.java:89-94`；`NetworkHandler.java:5838-5868` | 末地门生成时即预激活，玩家无需填充眼睛即可进末地 | 中央 3×3 生成时留空，仅 12 框架全 eye=true 时由 tryActivateEndPortal 激活 |
| P7-B3 | P1 | P7 末路与外岛 | `StructureManager.java:10-11,187-198` | 要塞为均匀网格而非原版 128 同心环，数量/分布/距离偏离 | 实现 concentric_rings 放置（或至少 128 个、按环半径递增）并同步 findNearest |
| P8-#1 | P1 | P8 生电/技术向 | `RedstoneEngine.java:233-253,258-275` | repeater/comparator/observer/按钮/压力板输出无法驱动下游，进阶红石链路中断 | 传播函数统一走 `providesPower()`，按 facing 向输出方向供强电 |
| P8-#2 | P1 | P8 生电/技术向 | `ContainerStore.java:266-292` | 漏斗无红石锁定（ENABLED），被供能仍持续传输 | tickHopper 读取 enabled 属性，供能时直接 return |
| P9-B1 | P1 | P9 存储 | `NetworkHandler.java:6244` | 木桶右键无法打开（全文件无 barrel 处理），木桶等于不可用存储【合并 P2-B1】 | 打开分支增加 `barrel` 判断，复用单箱 27 格打开/持久化路径 |
| P9-B2 | P1 | P9 存储 | `ContainerStore.java:230` | 陷阱箱打开无红石信号，与普通箱无异【合并 P2-B4 部分】 | 维护每陷阱箱查看者计数并 updateNeighborsAt，信号=clamp(viewers,0,15) |
| P10-B1 | P1 | P10 流体 | `FluidEngine.java:18-23,50-61`；`WorldManager.java:357,458`；`NetworkHandler.java:610-612` | 方块变化后流体不重新蔓延，水/岩浆表现为静止 | WorldManager.setBlock 与破坏路径对邻居流体调用 scheduleFluidTick |
| P11-B1 | P1 | P11 种植 | `RandomTickEngine.java:72-83,78` | 作物无视光照/灌溉、可种任意方块且成熟过快 | 加光照阈值(getRawBrightness≥9)、要求耕地、按 moisture 调生长概率 |
| P11-B2 | P1 | P11 种植 | 全项目无 FarmBlock moisture/trample 逻辑 | 耕地无灌溉状态、不干涸、不被踩踏退化 | 引入 moisture(0..7)，randomTick 近水/雨→7 否则递减，0 且无作物→变 dirt |
| 创造P1-1 | P1 | 创造 P1 指令 | `ConsoleCommandHandler.java:31/57`；`NetworkHandler.java:1264/1319` | 完全缺失 @a/@p/@r/@e/@s 目标选择器，指令只能精确匹配玩家名 | 实现 EntitySelector 解析层，指令目标统一经选择器解析 |
| 创造P1-2 | P1 | 创造 P1 指令 | `ConsoleCommandHandler.java:595-602`；`NetworkHandler.java:1724-1734` | op/deop 仅广播消息，无权限授予/持久化，任意玩家可执行管理指令 | 引入权限等级与 ops.json，handleCommand 入口做 requires 校验 |
| 创造P2-1 | P1 | 创造 P2 模式切换 | `NetworkHandler.java:697-960` | 冒险/旁观模式可放置方块/船/蛋/桶，破坏原版模式契约 | use_item_on 入口按 gameMode 拦截 spectator（return）/adventure（仅 CanPlaceOn） |

---

### 3.2 P2 级（明显影响玩法但可绕行，共 64 条）

| 编号 | 严重度 | 所属域 | 文件:行号 | 现象（一句话） | 建议修复方向（一句话） |
|---|---|---|---|---|---|
| P1-4.1 | P2 | P1 连接/进入/区块 | `NetworkHandler.java:5090,5118` | 每次跨区块边界发送 ChunkBatchStart(0x0C)/Finished(0x0B) | 从 handleMove 移除，仅保留在 sendInitialChunks |
| P2-B2 | P2 | P2 玩家交互/背包 | `NetworkHandler.java:2448,2880` | 多人开同一箱子仅操作方收槽更新，观察者不同步 | 维护 windowId→玩家集，广播 0x14/0x12 给所有持有者 |
| P2-B3 | P2 | P2 玩家交互/背包 | `CraftingSystem.java:230,235,305` | 仅硬编码 ~110 配方、多格无序不支持、无 JSON 注册表 | 加载 recipes JSON，补多格 shapeless 匹配 |
| P3-#2 | P2 | P3 生存机制 | `NetworkHandler.java:1030-1031,4584-4597` | 进食饱食度 ×2 且 per-food 比率表不一致 | 去掉 ×2，以 foods.json 查表并 clamp(saturation,0,food) |
| P3-#3 | P2 | P3 生存机制 | `NetworkHandler.java:288-290` | 复活无条件清零经验，与 keepInventory 冲突 | 复活前判断 keepInventory，为 true 不重置 XP |
| P3-#4 | P2 | P3 生存机制 | `NetworkHandler.java:3953-3964` | 饥饿伤害不区分难度，困难模式无法饿死 | 引入 difficulty 判断按三档阈值决定 starve 伤害 |
| P3-#5 | P2 | P3 生存机制 | `NetworkHandler.java:3919-3965` | 饥饿忽略难度与 naturalRegeneration gamerule | 饥饿衰减/回血分支前读取对应 gamerule/难度 |
| P3-#6 | P2 | P3 生存机制 | `NetworkHandler.java:1205-1247` | 近战未应用武器附魔（锋利/击退/火焰） | attackMob 读取手持槽附魔加算伤害/击退/点燃 |
| P4-1 | P2 | P4 实体与生物 | `EntityManager.java:199-212` | 刷怪 Y 为世界列顶，下界天花板刷怪/主世界只山顶 | 改玩家附近合法地表扫描，hasCeiling 维度从地面扫描 |
| P4-2 | P2 | P4 实体与生物 | `LivingEntity.java:8`；`MobEntity.java:266,263-270` | lastAttacker 恒 null，onDeath 经验分支为死代码，两套 XP 表不一致 | attackMob/damage("player") 写入 lastAttacker 或统一用 getMobXp |
| P4-3 | P2 | P4 实体与生物 | `MobEntity.java:159-167,128-241` | 骷髅/女巫等远程生物被强行近战，无门破坏/村民等 | 增加射弹攻击分支与 Goal 化行为 |
| P4-4 | P2 | P4 实体与生物 | `NetworkHandler.java:1251`；`2262-2285` | 击杀直接给经验不生成可拾取经验球 | 击杀调用 spawnExperienceOrbs 而非直接 addExperience |
| P4-5 | P2 | P4 实体与生物 | `EntityManager.java:116-133,214-258,260-299` | 刷怪节奏过简，单点单只、无群系/结构/难度 | 引入 chunk MobCategory 上限与群系权重表，放宽到 128 |
| P5-1 | P2 | P5 维度与传送门 | `NetworkHandler.java:4732`；`DimensionType.java:3-6` | update_time 硬编码 age=0/time=6000，末地不锁 18000 | 用 Main.dayTime/worldAge，末地按 fixedTime=18000 |
| P5-2 | P2 | P5 维度与传送门 | `DimensionType.java:8-22` | 维度元数据缺 ambientLight/ultrawarm/fixedTime 等 | 核对 reg_14.bin 注册表并补全内部字段供逻辑使用 |
| P6-B3 | P2 | P6 进度系统 | `NetworkHandler.java:627-649` | 精准采集矿石仍发放经验（原版不掉） | silk>0 分支跳过 getOreXp 加经验 |
| P6-B4 | P2 | P6 进度系统 | `BlockManager.java:88-100` | 仅校验工具类别不校验材质等级（木镐挖钻石矿） | canHarvest 加 harvestLevel/tier 比对 |
| P6-B5 | P2 | P6 进度系统 | `NetworkHandler.java:2662`+`:634` | 有物品的箱子/末影箱破坏时额外掉箱子方块 | 特判 chest/ender_chest 仅溢内容物、不掉落方块 |
| P6-B6 | P2 | P6 进度系统 | `NetworkHandler.java:415,4039,4049` | 附魔青金石消耗恒 1，未按槽位 n+1 | 扣青金石改为 buttonId+1 并校验数量 |
| P6-B7 | P2 | P6 进度系统 | `NetworkHandler.java:4100` | 附魔选项每帧重掷随交互乱跳 | 用玩家稳定 enchantmentSeed，仅一次附魔后重掷 |
| P6-B8 | P2 | P6 进度系统 | `EnchantSystem.java:104-132`；`NetworkHandler.java:4056` | 每次点击仅 1 个附魔 | 复用 selectEnchantment 式多选（按 power 递减叠加） |
| P6-B9 | P2 | P6 进度系统 | `NetworkHandler.java:4023-4026` | 书架计数漏判 y+1 空气隔断 | 同时检查 (offset/2,y,offset/2) 与 (offset/2,y+1,offset/2) |
| P6-B10 | P2 | P6 进度系统 | `NetworkHandler.java:638` | 时运均匀分布且最小值恒 1，高于原版期望 | 改为 binomial(fortune+1,0.5)，允许 0 加成 |
| P7-B4 | P2 | P7 末路与外岛 | `EnderDragonEntity.java:47-49` | 水晶存活时龙仍可被非水晶伤害（仅 ×0.25） | 水晶存活时非爆炸伤害近似免疫并强化回血 |
| P7-B5 | P2 | P7 末路与外岛 | `EndDragonFight.java:31,80-114` | 龙击败后永不重生，无法重复挑战/刷经验 | 实现四水晶重生逻辑并持久化 dragonKilled |
| P7-B6 | P2 | P7 末路与外岛 | `NetworkHandler.java:3520-3537` | 返回传送门与祭坛齐平，站立其上被扫描漏检（需实测） | scanPortalBlock 的 minY 下探 floor(y)-1 或 AABB 相交判定 |
| P7-B7 | P2 | P7 末路与外岛 | `EndDragonFight.java:274-289`；`NetworkHandler.java:4743-4784` | end gateway 仅单点硬编码、未写 exit 位置 | 引入随机出口坐标，按原版散布 gateway 岛 |
| P7-B8 | P2 | P7 末路与外岛 | `NetworkHandler.java:1027-1044` | 紫颂果无随机传送，仅为普通食物 | 进食 chorus_fruit 调用随机传送（生存 100%） |
| P7-B9 | P2 | P7 末路与外岛 | `DensityRouterChunkGenerator.java:112-113` | 末地外岛地形是否生成未核实（可能悬浮虚空） | 核实 NoiseRouterData.end 是否实现外岛噪声 |
| P8-#3 | P2 | P8 生电/技术向 | `RedstoneEngine.java:556-575,589-618` | 活塞无 MovingBlock 实体/瞬时推动/不携带实体 | 引入 PistonMovingBlock 计划刻分 tick 移动并携带实体 |
| P8-#4 | P2 | P8 生电/技术向 | `RedstoneEngine.java:414-419,377-387` | 粘性活塞 retract 错误拉回蜂蜜（应为 PUSH_ONLY） | pistonPushReaction 对 honey 返回 PUSH_ONLY |
| P8-#5 | P2 | P8 生电/技术向 | `RedstoneEngine.java:435,354-370` | 所有方块实体判为不可推（箱/炉/漏斗） | 除 bedrock/obsidian 等外允许带 BE 方块被推并迁移 ContainerStore |
| P8-#6 | P2 | P8 生电/技术向 | `RedstoneEngine.java:695-714` | 比较器不读取容器模拟信号 | back 输入 hasAnalogOutputSignal 时取 getAnalogOutputSignal |
| P8-#7 | P2 | P8 生电/技术向 | `RedstoneEngine.java:717-728` | 中继器忽略 DELAY 与 LOCKED | 按 DELAY*2gt 调度，实现 isLocked |
| P8-#8 | P2 | P8 生电/技术向 | `RedstoneEngine.java:759-826` | 观察者用墙钟 100ms 而非 1 红石刻(2gt)且为轮询 | 用游戏 tick 计划刻实现精确 2gt 脉冲、基于 block update 触发 |
| P8-#9 | P2 | P8 生电/技术向 | `RedstoneEngine.java:164-170` | 音符盒完全不发声 | 上升沿调用 playNote 按 instrument+note 发声 |
| P8-#10 | P2 | P8 生电/技术向 | `RedstoneEngine.java:725,790` | 时序使用墙钟 System.currentTimeMillis 而非游戏 tick | 改为基于 tickCount 到期判断 |
| P8-#11 | P2 | P8 生电/技术向 | `world/entity/` 无 Minecart | 矿车/铁轨整体缺失 | 实现 Minecart 实体与轨道移动逻辑 |
| P9-B3 | P2 | P9 存储 | `ContainerStore.java:81-83`；`NetworkHandler.java:6322` | 双箱不合并，两箱独立 27 格、互不共享 | 检测水平相邻同类箱合并 54 格共享 ChestData |
| P9-B4 | P2 | P9 存储 | `NetworkHandler.java:3013-3017`；`ContainerStore.java:14-18` | 容器物品无 per-item NBT，附魔/命名丢失 | 槽增加 NBT 组件存储，取放时保留 |
| P9-B5 | P2 | P9 存储 | `NetworkHandler.java:1150`；`persistChest` | 自动系统写入容器的内容未落盘可能丢失 | 世界保存时统一 flush 内存容器到 block entity |
| P10-B2 | P2 | P10 流体 | `FluidEngine.java:20` | activeFluids 只增不减，长期运行内存泄漏 | tick() 处理后从 activeFluids 移除该 key |
| P10-B3 | P2 | P10 流体 | `FluidEngine.java:63-133` | 无无限水机制，取水耗尽 | 水 tick 检测相邻两水源则生成水源 |
| P11-B3 | P2 | P11 种植 | `RandomTickEngine.java:85-99` | 甘蔗不需邻水/仙人掌贴墙也长且过快 | 加基底邻水/邻居非实心约束，用 AGE 0..15 累加 |
| P11-B4 | P2 | P11 种植 | `RandomTickEngine.java:20,72-83` | 瓜类（西瓜/南瓜）成熟不生成果实 | stem 满 age 检测相邻 4 格生成 melon/pumpkin |
| P11-B5 | P2 | P11 种植 | `RandomTickEngine.java:58-69` | 树苗永远不长成树 | 增加 sapling 随机刻生成对应树 |
| P12（缺失项） | P2 | P12 成就 | 全 `src/` 无 Advancement 实现 | 成就系统完全未实现（0%），无 toast/进度/0x80 下发 | 从零建设：数据层+进度层+触发层+0x80 协议下发 |
| 创造P1-3 | P2 | 创造 P1 指令 | `NetworkHandler.java:1331-1351`；`ConsoleCommandHandler.java:123-145` | /gamemode 指令路径不同步飞行能力【合并 创造P2-4】 | 两条路径均设 allowFlight 并 sendAbilitiesUpdate |
| 创造P1-4 | P2 | 创造 P1 指令 | `ConsoleCommandHandler.java:123-145` | 控制台 /gamemode <玩家> <模式> 参数顺序与原版相反 | 对齐为 /gamemode <模式> [玩家] |
| 创造P1-5 | P2 | 创造 P1 指令 | `NetworkHandler.java:1308-1328`；`ConsoleCommandHandler.java:99-120` | /tp 不支持 ~ 相对坐标/实体目标/facing | 复用 parseCoord 支持 ~，目标支持选择器/实体 |
| 创造P1-6 | P2 | 创造 P1 指令 | `NetworkHandler.java:1383-1392`；`ConsoleCommandHandler.java:174-185` | /give 无数量上界/NBT/目标选择器 | 校验 amount<=maxStackSize，目标走选择器，可选 components |
| 创造P1-7 | P2 | 创造 P1 指令 | `NetworkHandler.java:1599-1642`；`ConsoleCommandHandler.java:388-437` | /setblock、/fill 不解析方块状态，fill 仅 replace | 解析方块 [] 状态，fill 加 replace<filter>/destroy/keep |
| 创造P1-8 | P2 | 创造 P1 指令 | `NetworkHandler.java:1962-2098` | getTabCompletions 不补全 @/坐标且与命令树不一致 | 补全选择器/坐标，命令树与补全共用元数据 |
| 创造P2-2 | P2 | 创造 P2 模式切换 | `NetworkHandler.java:759/778/793/813/849/871` | 创造放置船/蛋/桶服务端 giveItem 造成物品复制 | 创造模式不调用 giveItem，仅正常放置 |
| 创造P2-3 | P2 | 创造 P2 模式切换 | `NetworkHandler.java:1200/1215` | 创造左键攻击生物仍造成伤害且被加强 | attackMob 中 gameMode==1||3 return，移除伤害加强 |
| 创造P2-5 | P2 | 创造 P2 模式切换 | `NetworkHandler.java:423-426` | 任意玩家 F3+F4 切任意模式无权限校验 | change_game_mode 增加权限/白名单校验 |
| 创造P2-6 | P2 | 创造 P2 模式切换 | 多处 | 旁观模式缺隐形/穿墙/禁止交互等原版行为 | 旁观不广播外观、移动无碰撞、交互入口 return |
| 创造P3-1 | P2 | 创造 P3 中键选中 | `NetworkHandler.java:1060-1069` | 中键选中带组件物品服务端丢失组件 | 解析并保留 DataComponentPatch |
| 创造P3-2 | P2 | 创造 P3 中键选中 | `NetworkHandler.java:1053` | 生存模式接受 set_creative_slot 可复制任意物品（安全） | 入口加 `gameMode!=1 && gameMode!=3` return |
| 地形P1-1 | P2 | 地形 P1 结构 | `BlockTransform.java:67-75` | transformOrientation 只旋转 front/只镜像 top，原版 front+top 同时 | 改为 front/top 同时 rotate/mirror |
| 地形P1-2 | P2 | 地形 P1 结构 | `StrongholdPortalRoomGenerator.java:30-158` | 要塞仅生成单个传送门房间，无完整迷宫 | 实现完整 concentric_rings 要塞或文档标注“仅房间” |
| 地形P1-3 | P2 | 地形 P1 结构 | `NonJigsawPlacer.java:119-178,216-277,279-351,353-412` | 程序化结构为近似，缺内部机关细节 | 改用原版 nbt 模板或逐块移植，或纳入“已知近似”文档 |
| 地形P2-1 | P2 | 地形 P2 噪声 | `NoiseBasedAquifer.java:446-452` | isLava/isWater 硬编码 11/9，与 state ID 强耦合 | 改为注入 lavaId/waterId 判定 |
| 地形P2-2 | P2 | 地形 P2 噪声 | `XoroshiroRandomSource.java:113-117` | at() 未将坐标异或进 seedHi | 改为 `k = l ^ seedHi` 返回 XoroshiroRandomSource(lo,k) |

---

### 3.3 P3 级（协议非原版 / 小众路径 / 低影响，共 53 条）

| 编号 | 严重度 | 所属域 | 文件:行号 | 现象（一句话） | 建议修复方向（一句话） |
|---|---|---|---|---|---|
| P1-4.2 | P3 | P1 连接/进入/区块 | `Chunk.java:228-232` | biome 单值化丢失区块内群系变化 | 按原版对 64 个 biome 用 palette 编码 |
| P1-4.3 | P3 | P1 连接/进入/区块 | `network/PlayerChunkTracker.java` | 整文件死代码（区块追踪由 handleMove 内联实现） | 删除该类或接入 handleMove |
| P2-B5 | P3 | P2 玩家交互/背包 | `NetworkHandler.java:1107` | 容器点击误发 AcknowledgeBlockChange(0x04, seq=0) | 删除该行 |
| P2-B6 | P3 | P2 玩家交互/背包 | `NetworkHandler.java:1098-1105` | changed-slots 的 ItemStack 组件未完整消耗（仅 readByte） | 对每个 changed-slot 完整读取 DataComponentPatch |
| P2-B7 | P3 | P2 玩家交互/背包 | `NetworkHandler.java:3185-3194` | 容器内 SWAP 副手目标槽 40 而非 45 | 交换目标改 45 |
| P2-B8 | P3 | P2 玩家交互/背包 | `NetworkHandler.java:1170-1176` | place_recipe 忽略窗口 0（2×2 随身合成） | 对 windowId==0 填槽 1–4 |
| P2-B9 | P3 | P2 玩家交互/背包 | `NetworkHandler.java:1054-1079` | set_creative_slot 跳过组件、盔甲/副手特殊语义不处理 | 按 774 解析组件与特殊槽 |
| P3-#7 | P3 | P3 生存机制 | `NetworkHandler.java:3761` | turtle 头盔韧性应为 2 代码为 1 | 改 return 2 |
| P3-#8 | P3 | P3 生存机制 | `NetworkHandler.java:4599-4640` | getFoodValue 部分食物 hunger 值偏离原版 | 以 foods.json foodPoints 修正 |
| P3-#9 | P3 | P3 生存机制 | `NetworkHandler.java:4223-4233` | 瞬时效果缩放与基数偏离原版 | 改 `4*(1<<amp)`/`6*(1<<amp)` 并判断亡灵反转 |
| P3-#10 | P3 | P3 生存机制 | `NetworkHandler.java:3609-3675` | 环境伤害节奏可能慢于原版 | 改每 ~10 tick 施加并用无敌帧控制 |
| P3-#11 | P3 | P3 生存机制 | `NetworkHandler.java:3890-3905` | deathMessageFor 缺若干伤害类型文案 | 补 inFire/onFire/witherRose 等 case |
| P3-#12 | P3 | P3 生存机制 | `NetworkHandler.java:3813,3774-3795` | 盾牌仅格挡 player/mob 且 offhand 兜底不当 | 扩展可格挡集合，无盾返回 -1 跳过扣耐久 |
| P3-#13 | P3 | P3 生存机制 | `NetworkHandler.java:4218-4242,4168` | health_boost/absorption 效果未实现 | 实现 maxHealth 调整与吸收心 HUD 同步 |
| P4-6 | P3 | P4 实体与生物 | `MobEntity.java:272-316` | 掉落硬编码表，无 looting/稀有掉落 | 接入 LootTable 或补 looting 倍数与稀有掉落 |
| P4-7 | P3 | P4 实体与生物 | `ItemEntity.java:46` | 掉落物拾取未按维度过滤 | 加 `player.currentDim != this.dim` 判断 |
| P4-8 | P3 | P4 实体与生物 | `EntityManager.java:264,275` | 无水生（鱿鱼）刷怪 | 增加水体刷怪检测与对应种群 |
| P4-9 | P3 | P4 实体与生物 | `SpawnerSystem.java:21,48` | 刷怪笼种类固定未读 NBT 配置 | 读 spawner NBT 决定种群与参数 |
| P5-3 | P3 | P5 维度与传送门 | `NetworkHandler.java:4651-4652` | 坐标缩放 (int) 截断丢失小数 | 保留 double 偏移或按目标门中心对齐 |
| P5-4 | P3 | P5 维度与传送门 | `NetworkHandler.java:4655-4657` | 末地→主世界固定 (8,8) | 改返回玩家重生点/出口门位置 |
| P5-5 | P3 | P5 维度与传送门 | `NetworkHandler.java:4837-4855` | findNearbyPortal 半径 32 暴力扫描非 POI | 扩大/对齐原版 POI 半径（主世界 128、下界 16） |
| P5-6 | P3 | P5 维度与传送门 | `NetworkHandler.java:5838-5868` | 末地门激活仅统计 12 眼未校验 3×3 结构 | 严格校验 3×3 开口 |
| P5-7 | P3 | P5 维度与传送门 | `NetworkHandler.java:4695-4709` | 返回门落点依赖 portalTimer 冷却、边界脆弱 | 落点略偏门侧并显式设 inPortal 标记 |
| P6-B11 | P3 | P6 进度系统 | `EnchantSystem.java:44-54` | 稀有度权重缺 UNCOMMON(5) 档，部分附魔过权 | 按 protocol.json 补全 10/5/2/1 权重 |
| P6-B12 | P3 | P6 进度系统 | `NetworkHandler.java` furnace 取物分支 | <1 经验被囤积延迟发放 | 按配方 xp 概率结算（floor+rand<frac） |
| P6-B13 | P3 | P6 进度系统 | `CraftingSystem.java:248-251` | 多格无序配方不被识别 | 支持多格 shapeless 匹配 |
| P6-B14 | P3 | P6 进度系统 | 玩家 2×2 shift 分支 | 玩家背包 2×2 不支持 shift 合成全部 | windowId==0 && slot==0 走 craftAllFromResult |
| P6-B15 | P3 | P6 进度系统 | `NetworkHandler.java:4376-4422` | 书架掉 1 本书（原版 3） | 书架掉落数量改为 3 |
| P7-B10 | P3 | P7 末路与外岛 | `NonJigsawPlacer.java:460-473` | 末地城二层楼重叠/船固定偏移，门洞不对齐 | 改用 jigsaw 拼接或调整偏移接驳塔身 |
| P7-B11 | P3 | P7 末路与外岛 | `EnderDragonEntity.java:10-123` | 龙阶段仅 3 种，缺起飞/降落/扫射/死亡 | 增加 Landing/Takeoff/Strafe/Death 阶段 |
| P7-B12 | P3 | P7 末路与外岛 | `EndDragonFight.java:386-413` | 末地诗仅聊天提示，无 credits/advancement | 触发 credits 画面与 the_end advancement |
| P7-B13 | P3 | P7 末路与外岛 | `StrongholdPortalRoomGenerator.java:97-102` | 框架眼睛随机与预激活叠加纯装饰 | 随 B2 修复后眼睛默认 false 由玩家填 |
| P8-#12 | P3 | P8 生电/技术向 | `RedstoneEngine.java:258-275,301-326` | 红石线方向性/转角连接为近似 | 按连接方向输出、实现 corner connection |
| P8-#13 | P3 | P8 生电/技术向 | `RedstoneEngine.java:817-825` | 压力板仅检测玩家，不检测 mob/物品 | 纳入 mob/物品实体检测 |
| P8-#14 | P3 | P8 生电/技术向 | `RedstoneEngine.java:624-626` | trackedObservers/trackedPlates 不持久化 | 随区块加载注册或关服持久化 |
| P8-#15 | P3 | P8 生电/技术向 | `RedstoneEngine.java:105-138` | TNT 爆炸为简化模型（3×3×3 瞬时清除） | 按抗性判定、生成掉落物、对实体造成伤害 |
| P9-B6 | P3 | P9 存储 | `NetworkHandler.java:2789` | block entity id 硬编码为 chest（陷阱箱/木桶不符） | 按方块名写对应 block entity id |
| P9-B7 | P3 | P9 存储 | `NetworkHandler.java:6256-6279` | 奖励箱未关闭即卸载可能丢（同 B5） | 生成后立即 persistChest 或卸载前 flush |
| P10-B4 | P3 | P10 流体 | `FluidEngine.java:108,126` | 水/岩浆不冲走可替换方块 | 参考 canDisplace 对 REPLACEABLE 允许流入 |
| P10-B5 | P3 | P10 流体 | `FluidEngine.java:103` | 下界岩浆流动距离/反应未按维度区分 | 按维度设置岩浆等级与反应规则 |
| P10-B6 | P3 | P10 流体 | `FluidEngine.java:50-61` | 每 tick 全量遍历 pendingFluids，性能风险 | 限制每 tick 处理预算或避免重复入队 |
| P11-B6 | P3 | P11 种植 | `RandomTickEngine.java:37` | 随机刻范围 ±2 区块，远处农田不长 | 扩大至原版量级（±8 区块/128 格半径） |
| P11-B7 | P3 | P11 种植 | `RandomTickEngine.java:102-114` | 树叶凋零范围/语义与原版 BFS 有出入且不掉树苗 | 改距离判定并加掉落概率 |
| P11-B8 | P3 | P11 种植 | 全局无 vine 处理 | 藤蔓不蔓延 | 增加 vine 随机刻蔓延 |
| 创造P1-9 | P3 | 创造 P1 指令 | `NetworkHandler.java:1331-1351` | 玩家版 /gamemode 无目标参数 | 增加可选目标参数（受权限约束） |
| 创造P1-10 | P3 | 创造 P1 指令 | `NetworkHandler.java:5356` vs `:1264` | 指令执行与 Brigadier 树无共享注册易漂移 | 抽离统一指令注册表，执行与补全共用 |
| 创造P2-7 | P3 | 创造 P2 模式切换 | `PlayerData.java:25` | gameMode 是否存档读写未核实，可能丢失 | 核实并在 PlayerData 序列化中读写 gameMode |
| 创造P3-3 | P3 | 创造 P3 中键选中 | `NetworkHandler.java:1053-1081` | 仅存 id+count 未跟踪组件 | 建立统一 ItemStack 表示(id+count+components) |
| 地形P1-4 | P3 | 地形 P1 结构 | `StructureTemplate.java:110-121` | isBlockEntityBlock 硬编码名单可能遗漏 BE | 用方块注册表或补齐 1.21 新增 BE |
| 地形P1-5 | P3 | 地形 P1 结构 | `JigsawPlacement.java:155-254` | ROLLED joint 未翻转子片段旋转 | 若 rollable 对旋转叠加 CLOCKWISE_180 |
| 地形P1-6 | P3 | 地形 P1 结构 | `StructureTemplateLoader.java:100-111` + `ShipwreckGenerator.java:9-29` | 旧加载器/生成器为死代码 | 删除旧 structure/ 生成器避免误导 |
| 地形P1-7 | P3 | 地形 P1 结构 | 结构注册（需确认） | 需核对 StructureRegistry 覆盖所有原版结构类型 | 核对注册表确保各类型路由到活跃管线 |
| 地形P2-3 | P3 | 地形 P2 噪声 | `DensityFunction.java:85-89` | setWorldSeed 单参数构造未做 128 位提升 | 调用 upgradeSeedTo128bit 确保与 RandomSource.create 等价 |

---

## 4. 跨域主题归纳（系统性重复问题）

### ① 协议 ItemStack / DataComponentPatch 组件未完整消耗（协议层共病）
- **出现位置**：P2-B6（容器点击 changed-slots，仅 readByte）、P2-B9（set_creative_slot 跳过组件/盔甲副手）、P9-B4（容器物品无 per-item NBT）、创造P3-1（中键选中丢组件）、创造P3-3（仅存 id+count）。
- **根因**：774 协议下 `ItemStack` 携带可变长 `DataComponentPatch`，而多处读取客户端物品时只解析 `id+count`（或仅吞 1 字节），且服务端存储层（`PlayerData.inventoryIds`、`ContainerStore.ChestData`）只存 `int id + int count`。
- **影响**：带附魔/药水/命名/方块状态的物品在容器、创造槽、死亡掉落回发时“掉组件”；容器 NBT 丢失使附魔装备/命名牌入库即降级。
- **系统性修复方向**：建立统一 `ItemStack`（id+count+components）表示，凡读取/回发客户端物品处（容器点击、创造槽、中键、掉落物）均完整消耗/保留组件补丁。

### ② 存储容器类交互缺陷（集中在 P9 / P2）
- **出现位置**：P9-B1（木桶不可开）、P9-B2（陷阱箱无红石）、P9-B3（双箱不合并）、P9-B4（容器 NBT 丢失）、P9-B5（自动系统写入不落盘）、P9-B6（BE id 硬编码）、P9-B7（奖励箱可能丢）、P2-B2（多人同容器不同步）、P2-B3（配方硬编码）、P2-B5/B6/B7/B8/B9（容器点击协议细节）。
- **根因**：容器打开分支以 `name.endsWith("_chest")` 硬编码，未含 barrel/trapped_chest 特殊语义；`ContainerStore` 以精确 Pos 为 key 不合并相邻箱；`persistChest` 仅在窗口关闭调用，漏斗/熔炉等自动 tick 改动不回写；并发展示未广播给其他观察者。
- **影响**：木桶/陷阱箱/双箱三大存储语义全部偏离原版，多人共享容器视图错乱，容器物品元数据丢失，自动系统写入有丢失风险。

### ③ 硬编码而非数据驱动（配方 / 流体 / 刷怪 / 结构）
- **出现位置**：P2-B3（硬编码 ~110 配方、无 JSON 注册表）、P6-B2/B13（附魔/合成非数据驱动）、P10-B3（无无限水、流体蔓延硬编码）、P4-5（刷怪硬编码池/扁平计数非 MobCategory）、P11-B1/B2（作物/耕地硬编码无光照灌溉）、地形P1-3（程序化结构手工近似非原版 nbt）、P6-B5（容器双重掉落硬编码）。
- **根因**：大量玩法逻辑以固定表/常数实现，未加载原版 `data/minecraft/recipes`、`tags`、群系权重表、`loot_table`、噪声配置等数据驱动源。
- **影响**：生存进阶受限（配方缺失）、世界生成/流体/农业与原版系统性偏差。

### ④ 多人同步缺失（容器 / 维度 / 实体）
- **出现位置**：P2-B2（多人同容器其他观察者不收槽更新）、P5（维度切换仅单点、末地门激活宽松）、P4-7（掉落物未按维度过滤）、P8-#1/#2（红石器件输出下游死，影响多人 contraption）、P7-B6（返回门漏检）。
- **根因**：状态变更多只向“当前玩家上下文”广播，未维护“窗口/维度/区块 → 玩家集”做全观察者/全相关客户端广播；维度/实体校验缺多人视角。
- **影响**：多人服务器下共享容器视图错乱、维度边界行为不稳、跨维度物品误拾取。

### ⑤ 时序用墙钟而非游戏 tick（生电域共病）
- **出现位置**：P8-#7（中继器固定 100ms）、P8-#8（观察者墙钟 100ms）、P8-#10（红石延迟均用 System.currentTimeMillis）。
- **根因**：红石器件延迟用 `System.currentTimeMillis()+100` 而非游戏 tick 计划刻。
- **影响**：低 TPS 时红石比世界“跑得快”，时序 contraption 不可复现；与原版基于 `scheduleTick` 的精确红石刻不一致。

### ⑥ 属性/ID/状态注册表沿用旧版或硬编码（原版一致性隐患）
- **出现位置**：P3-#1（属性包按 1.12 旧顺序）、地形P2-1（流体 ID 硬编码 11/9）、地形P2-2（seedHi 未异或）、P5-2（维度元数据字段缺失）、P2-B9/P9-B6（block entity id 硬编码）。
- **根因**：客户端/服务端的注册表顺序、方块 ID、维度元数据未能随 1.21.11 演进动态对齐，部分写死常量。
- **影响**：客户端显示错误（护甲 HUD）、地形/流体随 ID 表漂移而错乱、维度表现偏差。

---

## 5. 修复优先级路线图

### 5.1 按严重度顺序
- **P0（立即）**：本审计 **未发现真实崩溃级缺陷**，P0 计数 = 0。各源报告标注“P0（立即）”的均为“最高修复优先级”之意，已统一归入 P1。
- **P1（高，功能性报废，立即处理）**：共 16 条。集中在——
  - 生存正确性：`P3-#1` 属性包 ID 重排、`P11-B1/B2` 作物光照灌溉+耕地 moisture、`P10-B1` 流体重新蔓延；
  - 末路可玩性：`P7-B1` 鞘翅来源、`P7-B2` 末地门预激活、`P7-B3` 要塞 128 环；
  - 生电可用：`P8-#1` 传播走 providesPower、`P8-#2` 漏斗红石锁定；
  - 存储/`P9-B1/B2` 木桶+陷阱箱；
  - 创造/管理：`创造P1-1` @选择器、`创造P1-2` 权限系统、`创造P2-1` 模式契约；
  - 进度：`P6-B1` 修饰酿造、`P6-B2` 附魔算法。
- **P2（中，明显影响玩法）**：共 64 条。建议按域分批：先修“改动量小、收益大”的（如 `P9`、`P2-B2/B3`、`P3-#2~#6`、`P6-B3~B9`、`P11-B3~B5`、`P10-B2/B3`、`P7-B4~B9`），再修需中等重构的（如 `P4` 刷怪/AI、`P8-#3~#11` 活塞/比较器/矿车、`地形P1` 结构、`P12` 成就从零建设）。
- **P3（低，协议/观感/小众）**：共 53 条。可在后续打磨批次处理（数值修正、协议细节、死代码清理、性能、外观）。

### 5.2 “对照原版小改” vs “架构级重构”分类

**只需对照原版小改（加一行分支/改几行，风险低、性价比高）：**
- `P9-B1` 木桶打开（加 `barrel` 分支）
- `P3-#1` 属性包（重排 ID 数组）
- `P6-B1` 修饰酿造（canBrew 加效果判定）
- `P3-#3` 复活经验、`P3-#7` turtle 韧性、`P3-#8` 食物值、`P6-B6` 青金石 n+1、`P6-B7` 稳定种子、`P6-B15` 书架 3 本
- `P8-#2` 漏斗 enabled、`P8-#9` 音符盒发声、`P8-#4` honey PUSH_ONLY
- `P10-B2` activeFluids 回收、`P10-B3` 无限水
- `P11-B1` 作物光照/耕地校验、`P11-B2` 耕地 moisture
- `地形P2-1/2/3` 流体 ID 注入、`at()` seedHi 异或、128 位种子
- `P2-B5/B7` 删 0x04、SWAP 改 45、`P2-B8` 2×2 配方书、`P9-B6` BE id、`P7-B13` 眼睛
- `创造P2-2` 创造不 giveItem、`创造P2-3` 创造不伤生物、`创造P3-2` 模式守卫

**需架构级重构（影响面大、需设计）：**
- `P2-B3` + `P6-B2/B13` 合成/附魔 **数据驱动注册表**（JSON recipes、EnchantmentHelper 重写）
- `P4` 全套 **自然刷怪算法 + 生物 AI（GoalSelector/远程/繁殖）+ 经验球发放**
- `P8-#1/#3/#5/#6/#7/#8/#10/#11` 红石 **统一信号系统 + PistonMovingBlock + 矿车实体**（生电域主干）
- `P9-B3/B4/B5` 双箱合并 + **容器 NBT 存储 + 自动系统落盘 flush 钩子**
- `P10-B1` 流体 **neighborChanged 重调度框架**
- `P11` 种植系统 **光照/灌溉/耕地/作物类型完整模型**
- `P7` 末路 **末地门激活机制 + 要塞 concentric_rings + 外岛噪声 + gateway 散布 + 龙重生**
- `P5-2/P7-B9` 维度元数据/外岛 **数据驱动生成**
- `P12` 成就系统 **从零建设（数据层+进度层+触发层+0x80）**
- `创造P1-1/P1-2` **EntitySelector + 权限系统**
- `地形P1-1/P1-2/P1-3` 结构 **FrontAndTop 对齐 + 完整要塞 + 程序化结构 nbt 化**

---

> **汇报（收尾汇总）**
> - **总 Bug 数**：133 条（主清单去重后），严重度拆分 **P0 = 0 / P1 = 16 / P2 = 64 / P3 = 53**。若按各域文档自身计数（含交叉引用）则为 136 条（P0=0/P1=16/P2=67/P3=53）。
> - **最关键的 3 个跨域主题**：① 协议 ItemStack/DataComponentPatch 组件未完整消耗（容器/创造槽/中键/掉落全链路“掉组件”）；② 存储容器类交互缺陷集中（木桶不可开、陷阱箱无红石、双箱不合并、容器 NBT 丢失、多人不同步）；③ 硬编码而非数据驱动（配方/附魔/流体/刷怪/结构均偏离原版注册表与表驱动源）。
