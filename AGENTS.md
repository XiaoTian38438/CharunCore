# AGENTS.md — yanrong mc-server-core 1.21.11 AI 工作指引

## 项目概览
- 自研高性能 Minecraft Java Edition 服务器核心 (1.21.11 / Protocol 774)
- Java 21 + Netty；不依赖 Paper/Spigot/Mojang NMS
- 包名：`com.yanrong.server`
- 主类：`com.CharunCore.server.Main`

## 2026-08-19 玩家验收 bug 批次 5：红石/实体/世界/容器深项修复（已编译通过）
- **红石**: 红石粉放置后重读实际状态再 broadcastBlockChange(原用放置前状态广播->新放的不连线/红石灯不亮,重进才好); 红石线网络多轮收敛重算(BFS单次通过不收敛->拆电源后长线残留微弱信号且落盘); 动力铁轨激活后沿同种铁轨链两侧各传播8格共17格(原仅单格激活); 活塞伸头/缩回加 block_event(0x07 action=0/1)+音效(原无动画无音效); 新增 broadcastBlockEvent/broadcastSoundAt(dim) 工具
- **实体**: TNT爆炸摧毁范围内掉落物(原仅击退); 爆炸 y 击背包限制≤0.4 防玩家被推入方块假死卡住; 环境伤害 blockY/eyeY 用 Math.floor(原(int)对负数向零取整->洞底玩家窒息);
- **世界**: 末影人在末地无视光照生成(原light>7跳过->末地恒亮天空光15不生末影人); 火随机刻蔓延(点燃相邻可燃方块木/叶/羊毛等)+雨天露天熄灭+无支撑熄灭+age上限熄灭(原火永不蔓延永不自然灭); 水桶放水/岩浆接触火熄灭相邻火(WorldManager.setBlock 检测); 玩家进水熄灭身上火(fireTicks清零)
- **容器/交互**: 火焰弹(fire_charge)右键放火+消耗; 信标右键打开 UI(beacon menu); 唱片机右键放/取唱片(RecordItem BE + 音效); 刷怪笼右键用刷怪蛋改 SpawnData/SpawnPotentials(原刷怪蛋在刷怪笼上直接生成生物); ChunkEncoder 用 RegistryHelper.blockEntityTypeId 规范化材质前缀(原 oak_sign->chest 导致告示牌/床渲染错误透明); canPlaceInto 改用 isReplaceable(原!isSolidOpaque过宽->火焰/火把可堆叠隔空放); /killitem 清除掉落物命令
- **死亡F3+F4**: 重生补发 player_info_update(0x44 UPDATE_GAMEMODE) 给自己, 客户端 LocalPlayer.getPermissionLevel 据此刷新->创造=2->F3+F4可用(原仅发Respawn包不够)

## 2026-08-18 玩家验收 bug 批次 4：崩溃踢出 + 关键功能修复（已编译通过）
- **踢出崩溃(3个)**: 难度切换误发 0x03(award_stats) -> 改 0x0A(change_difficulty: byte+boolean locked); 告示牌编辑广播误用 0x09(boss_event) -> 改 0x06(block_entity_data: pos+typeId+NBT); 铁砧重命名 custom_name 组件多写 writeBoolean(true) -> 去掉(原版 CUSTOM_NAME 的 StreamCodec 直接写 Component NBT 无 boolean 前缀)
- **铁砧修复**: recomputeAnvil 同种可损伤物品合并不再 outCount=leftCount+rightCount(输出两件堆叠), 改为输出 1 件+耐久度相加(maxDmg-leftDmg + maxDmg-rightDmg + 12% 奖励, 上限 maxDmg); 新增 AnvilData.leftDamage/rightDamage/outDamage + setSlotItem 同步耐久转移 + takeProcessorResult 把 outDamage 转光标
- **营火右键误丢出**: 营火分支原来手持食物但4槽满/非食物时会走到"取出"逻辑 dropItemInFront 误丢光标物品 -> 改为仅空手(heldItemId<=0)才取出, 手持非食物直接 return
- **箱子开合动画**: 新增 sendChestBlockEvent 广播 0x07 block_event(action=1 param=open?1:0), 打开/关闭箱子时播放箱盖动画(原无任何 block_event 发送)
- **死亡后F3+F4无权限**: 重生流程补发 player_info_update(0x44 action=UPDATE_GAMEMODE=0x04) 给自己, 客户端 LocalPlayer.getPermissionLevel() 据此刷新 gamemode -> 创造=2 -> F3+F4 可用(原仅发 Respawn 包不够)
- **/killitem 命令**: 新增 killitem/killitems/clearitems/removeitems [半径] 清除当前维度所有 ItemEntity(解决水中掉落物抽搐堆积), 加入 ALL_COMMANDS tab 补全

## 2026-08-18 玩家验收 bug 批次 3：已知缺失补全（已全部编译通过）
- **树密度过高**: DensityRouterChunkGenerator.placeTrees 新增 placed 列表, 新树与已放树树干水平距离 < 3 格则跳过, 消除树冠重叠挤一起
- **营火烤食**: ContainerStore 新增 CampfireData(4 食物槽+独立 cookTime)+ tickCampfire(原版 600 tick/灵魂营火 300 tick)+ load/persistCampfire(BE 持久化); NetworkHandler use_item_on 营火分支: 手持食物右键放生食(扣物品), 右键取出已熟食弹出到掉落物; SmeltingSystem.isFood 改 public 复用
- **猪灵以物易物 barter**: MobEntity 新增 barterTimer 字段, piglin tick 检测 4 格内金锭 ItemEntity 并拾取, 120 tick(6 秒)后吐出随机战利品(rollBarterLoot: 末影珍珠/线/皮革/下界石英/黑曜石/哭泣黑曜石/火焰弹/灵魂沙/下界砖/光灵箭/荧石粉/铁粒/金粒/黑石/沙砾/水肺药水/抗火药水/下界合金锄)
- **告示牌/刷怪笼/信标 BE NBT 持久化**: createInitialBlockEntity 在放置 sign/spawner/beacon/campfire 时创建初始 BE NBT(告示牌 front_text/back_text 各 4 行空文本+color+has_glowing_text, 刷怪笼 Delay/SpawnCount/Range 等, 信标 Levels/Primary/Secondary); 告示牌右键打开编辑器(0x3A open_sign_editor)+ isInteractable 加 sign; serverbound 0x3B sign_update 回写 4 行文本到 BE + broadcastBlockEntityData(0x09)广播刷新; RegistryHelper.blockEntityTypeId 规范化(oak_sign->sign, *_bed->bed, *_skull->skull 等)
- **统计系统**: 新建 StatisticsManager(按 UUID 存 minecraft:<category>:<key>->count, 持久化 world/stats/<uuid>.json 原版格式); 埋点: 挖矿(mined)/放置(used)/合成(crafted)/击杀生物(killed)/拾取掉落物(picked_up); 进服 preload + savePlayerData 调 saveAll 落盘
- **已知缺失(本次未做)**: 无(本批 5 项已知缺失全部补全)

## 2026-08-18 玩家验收 bug 批次 2（已全部编译通过）
- **崩溃**: 酿造台 setSlotItem AIOOBE(potionType[3] 仅 3 格却访问燃料/原料槽 3/4 -> 限 s<3); 铁砧 container_set_content 超长(writeStackWithComponents 缺 enchantments.showInTooltip + potion_contents 末尾多写 + custom_name 误用 component id 4 应为 6); 活塞 StackOverflow(onBlockChangedInner putState 回调 setBlock->onBlockChanged 无限递归 -> ThreadLocal IN_UPDATE 重入保护)
- **耐久/组件**: writeStackWithComponents 新增 minecraft:damage 组件(id 3)并扩展签名传 damage; damageHeldItem 每次扣耐久即 sendSlotUpdate(原仅耗尽发包->耐久条不动); carriedDamage 随光标转移
- **容器 UI**: 切石机 menu type 修复(MENU_ORDER 缺 stonecutter -> 返回 0=漏斗, 补 stonecutter); 磨石去附魔(recomputeGrindstone 单/双输入产出干净物品, takeProcessorResult 清空光标元数据)
- **红石**: 红石粉 power/shape 变化不再发 level_event 2001 破坏粒子; 中继器右键调档(delay 1->2->3->4->1); isInteractable 加入 repeater/comparator/note_block/daylight_detector/jukebox; 红石粉上下坡连接已有
- **世界**: 进服/重生时间闪修复(0x6F 原发固定 dayTime=6000 -> 改用 Main.worldAge/dayTime); 功能方块朝向(applyPlacementContext 补熔炉/高炉/烟熏炉/酿造台/木桶/制图台/锻造台/织布机/砂轮设 facing)
- **已验证已有(无需改)**: 饱食度/装备切换/Q扔/下蹲放方块/破坏非完整方块/重生 abilities/附魔台/铁砧合并/村民交易入包/熔炉进度/矿车放置/压力板/阳光传感器
- **已知缺失(本次未做)**: 营火烤食(批次3已补); 统计系统(批次3已补); 猪灵以物易物 barter(批次3已补); 告示牌/刷怪笼/信标 BE NBT 持久化(批次3已补); 树密度过高(批次3已补)

## 2026-08-17 玩家验收 bug 批次 + 插件加强（已全部编译+冒烟验证）
- **崩溃**: 聊天 0x21 ChatType.Bound holder=varint(id+1)(774 变更); 村民 0x32 ItemCost=[id][count][谓词0]+boolean前缀Optional+过滤空结果; 挖掘硬度从 blocks.json 全量载入(花/火把可秒破)
- **数据安全**: AnvilManager setLength 截断根因(复用旧扇区把文件砍短->区块物理丢失错乱)+loadChunkNbt 并发锁; ChunkEncoder 单值 palette 仅限整节均匀(16³沙块根因)
- **特效**: 玩家死亡 entity_status3+音效广播; mob受伤+damage_event(0x19方向); 苦力怕 SWELL_DIR(index16)膨胀+6格中止; TNT 实体化(primeTnt: FUSE元数据index8+80刻引信+连锁引爆+打火石点燃+水吸收射线防爆); 末影人改中立/蜘蛛昼间中立; 远程生物<5格面向玩家倒退
- **容器/合成**: 数字键换位36+button; 窗外丢弃左右键修正; 2x2 shift 全部合成; 拖拽/右键放置后刷新结果预览; 双击收集(6); 漏斗朝下; 熔炉/漏斗/酿造台 BE 回读+熔炉进度落盘; 箱子内存权威
- **OP 分级 Lv1-5**: OpList(uuid+name 双键,空表=开发模式Lv5)+Permissions 命令等级表+BanList(banned-players.json 登录拒绝)+/op [等级]/ops/ban/unban/banlist/mute/unmute/tempkick/playerinfo/adminmenu; keepInventory gamerule 原有
- **结构 NBT**: 客户端 jar 提取 1202 个原版模板 -> json/minecraft/structure/ (structure2 jigsaw 加载器 TEMPLATE_DIR 就位, village/bastion/end_city/igloo/mansion/shipwreck/trial_chambers/ancient_city 全套)
- **插件加强(对标Paper)**: plugin.api 包(Player门面/ItemStack/World/BossBar 0x09/PermissionManager everyone-op-nobody+附件); NetworkHandler 新增 sendTitle(0x70/0x6E/0x71)/sendActionBar(0x55)/sendParticle(0x2E,particles.json)/setGameModeInternal; 新事件10个(Teleport/ItemHeld/ToggleSprint/InventoryClick/Close/Kick/ExpChange/EntityDamageByEntity等,共26); TabCompleter 接入 0x0E; plugin.yml permissions 块解析; Server.dispatchCommand; 文档 docs/PLUGIN_API.md 全量重写
- **已知待办**: 红石需真机验证(方向/负坐标/火把已修未实测); ItemMeta/lore/自定义Inventory/Scoreboard/PDC 未做; 比较器二值; 末影箱持久化; 容器components落盘; EntityTarget/EntityDeath事件部分接线

## 2026-08-16 大版本更新（已全部编译+冒烟验证）
- **网络**: `ServerConfig`(server.properties: 端口/视距/MOTD/压缩阈值/在线模式)；zlib 双向压缩(login 0x03 set_compression, `Compression.java`, 阈值可配)；混合登录 offline+UUID fix(`MojangProfileService`: 正版名→正版UUID+签名皮肤, 8s超时回退离线)；完整在线模式(RSA+AES-CFB8 `MinecraftCipher` + sessionserver hasJoined)；keep-alive 30s 超时踢人。冒烟测试: `scripts/LoginSmokeTest.java`(自实现协议, 全链路 PASS)
- **光照**: `world/light/LightEngine.java` 真实天空光/方块光 BFS(初始 ~70ms/chunk + 增量增暗/增亮), 区块包+Update Light(0x2F) 真实数据, 刷怪光照接入(`EntityManager.skyDarkenNow`)。诊断: `LightDiagnostic`
- **玩家数据**: `world/playerdata/<uuid>.dat` 原版 gzip NBT(含 components 物品组件), `PlayerDataManager` 自动迁移旧 players/*.json; 进度 → world/advancements/<uuid>.json
- **插件**: `plugin/` 包 — 事件总线(EventManager/@EventHandler/优先级/取消, 16个核心事件已接线), JAR 加载(plugin.yml+依赖拓扑+URLClassLoader 热重载 /reloadplugins), 命令(registerCommand+Brigadier树刷新), 调度器(ServerScheduler), YAML 子集解析器(Yaml.java)。文档: docs/PLUGIN_API.md
- **审计修复**: 配方去旋转匹配(原版仅恒等+水平镜像, recipes.json 标签已展开无需tag支持); FluidEngine 下落流体 level=8(修复水源复制机)/岩浆30/10tick/下界岩浆7格/维度扫描键; RedstoneEngine posKey符号扩展(负坐标组件复活)/中继器比较器从背面读输入/观察者背面输出/火把弱充能全邻/BFS深度256/setBlock补红石通知; ContainerStore 熔炉/漏斗/酿造台BE回读+熔炉进度持久化/漏斗朝下传输/箱子内存权威防覆盖; NetworkHandler 数字键换位36+button/窗外丢弃左右键修正; AnvilManager gzip区块读取+255扇区溢出防护; TpaSystem(/tpa /tpahere /tpaccept /tpdeny)
- **已知待办(优先级降序)**: NetworkHandler 9000行拆分(建议借事件系统逐步抽离); 末影箱持久化; 比较器模拟信号(当前二值)+容器满度按堆叠; 活塞推方块实体(当前可推); 容器物品components落盘(箱子附魔物品重启掉附魔); 准连接性(BUD)/火把烧毁/TNT游戏刻引信; 拖拽分配在结果槽扣减bug; 双击收集(6)未处理; 跨线程Chunk.setBlock同步化


## 构建与运行
- Maven，Java 21 编译
- 编译：`mvn -o compile -DskipTests`
- 运行：通过 Main 类启动服务器，Minecraft 客户端连接服务器验证地形
- 注意：Windows PowerShell 不支持 `&&`，命令链接用 `;` 或 `if ($?)`
- PowerShell 中包含 `$` 的字符串若用于 javap 参数必须用单引号
- mvn 输出对中文乱码 → 用 `chcp 65001 | Out-Null; [Console]::OutputEncoding = [System.Text.Encoding]::UTF8` 修复

## 代码风格
- 不添加 emoji
- 不添加注释（除非用户明确要求）
- Java 21 records 可用
- 适配项目现有 `BlockStateHelper.getDefault(String name)` 取方块 ID

## 原版源码参考
- 反编译位置：`mapping\remapped_server_1.21.11.jar.src\net\minecraft\world\...`
- 完整 jar（用于 javap 反编译）：`mapping\deobf-work\remapped_server_1.21.11.jar`
- 关键路径：
  - `world\level\levelgen\NoiseBasedChunkGenerator.java` 主入口
  - `world\level\levelgen\NoiseRouterData.java` 完整密度链（727 行已可读）
  - `world\level\levelgen\aquifer\Aquifer$NoiseBasedAquifer.java` 含水层
- 反编译状态：JD-Core 对约 17 个核心类报 `INTERNAL ERROR`；用 `javap -p -classpath "...\remapped_server_1.21.11.jar" '类名'` 取真实签名

## 官方数据
- `json\1.21.11\*.json` — 方块/群系/物品等数据
- `dumped_registries\` — 注册表 dump
- 项目已实现：Anvil MCA 读写、NBT 解析、协议包序列化

## 已有移植路线图（分阶段实施）

### 完成阶段
- Stage 0A-0D：DensityFunction 子系统（22 类）
  - 位置：`src/main/java/com/CharunCore/server/worldgen/density/`
  - 关键文件：`DensityFunction.java` 接口；`DensityFunctions.java` 工厂；`functions/` 包含 22 个实现类（Constant/YClampedGradient/Clamp/Mapped/TwoArgumentSimpleFunction/Lerp/Marker/Noise/ShiftedNoise/ShiftedNoise2D/ShiftA/ShiftB/ShiftNoise/MappedNoise/SplineAdapter/RangeChoice/WeirdScaledSampler/BlendAlpha/BlendOffset/BlendDensity/FindTopSurface/EndIslandDensityFunction）
- Stage 0E：`NoiseRouter.java` 数据类（15 字段路由）
  - 位置：`src/main/java/com/CharunCore/server/worldgen/density/NoiseRouter.java`
- **Bugfix (2026-07-11):** `FindTopSurface.java` 原是直接返回 `upperBound.compute(ctx)`（raw clamped height，可能返回~237）
  → 改为实现正确扫描逻辑：`floor(upperBound/cellHeight)*cellHeight` 向下扫描 `density > 0` 找真实地表 Y
  → 影响：`preliminarySurfaceLevel` 在 aquifer 中用于确定地表位置和流体压力；修复后 aquifer 中心 fluid level 始终为 63（海平面），无异常压力 → 消除 y=91-92 浮空方块
- Stage 0F：`NoiseRouterData.overworld()` 277 行 + `TerrainSplineProvider`（包装项目现有 `CubicSpline`）已移植
  - 位置：`src/main/java/com/CharunCore/server/worldgen/density/NoiseRouterData.java`、`TerrainSplineProvider.java`
- Stage 0G：**NoiseChunk 坐标插补 — 已完成**
  - 位置：`src/main/java/com/CharunCore/server/worldgen/noisechunk/NoiseChunk.java` (614 行)
  - 用 CFR 重反编译 `mapping/cfr-source/.../NoiseChunk.java`（785 行，无 INTERNAL ERROR）
  - 移植了全部内部类：NoiseInterpolator / CacheAllInCell / FlatCache / Cache2D / CacheOnce + 辅助类 QuartPos / ChunkPos / ColumnPos / NoiseSettings / Blender
  - 已重构：`blockStateRule` 返回 null 表示固体（vanilla 协议），生成器填入默认方块 STONE
  - `MaterialRuleList` 链式判定：Aquifer → OreVeinifier → null（固体）
  - 关键修复：`Marker.mapAll()` 必须调 `visitor.apply(new Marker(...))`；`BlendAlpha` 返回 1.0
- Stage 0H：**集成密度路由发生器 — 已完成（合并入 Stage 0G）**
  - `DensityRouterChunkGenerator` 通过 NoiseChunk 调用插补后的 `finalDensity`
  - 性能：~100ms/chunk（含 Aquifer + OreVeinifier 开销）

- Stage 1A：**NoiseBasedAquifer — 已完成**
  - 位置：`src/main/java/com/CharunCore/server/worldgen/noisechunk/aquifer/NoiseBasedAquifer.java` (457 行)
  - 完整含水层：流体屏障/压力/表面采样/随机化
  - `Aquifer.java` 接口：FluidStatus record、FluidPicker @FunctionalInterface、createDisabled()

- Stage 1B：**OreVeinifier — 已完成**
  - 位置：`src/main/java/com/CharunCore/server/worldgen/noisechunk/OreVeinifier.java` (80 行)
  - 铜矿脉(y 0-50)：铜矿石/粗铜/花岗岩；铁矿脉(y -60 to -8)：深层铁矿石/粗铁/凝灰岩
  - 完整噪声阈值、边角圆化、富集度判定
  - 集成进 NoiseChunk MaterialRuleList（Aquifer 之后执行）
  - 测试：42 chunks 中 42 个含矿脉，~~0 个为空

- Stage 1C：**SurfaceSystem 简化版 — 已完成**
  - `DensityRouterChunkGenerator.applySurfaceSystem()` 三阶段后处理：
    - Phase 1: 地表方块替换（8种表面：grass/sand/gravel/snow/packed_ice/podzol/coarse_dirt/clay）
    - Phase 2: 深板岩梯度（stone at y&lt;0 → deepslate，与原版 y=0 边界一致）
    - Phase 3: 基岩地板（stone/deepslate at minY → bedrock）
  - 次表层替换：沙下砂岩、草地下泥土（变深度 1-4）、砾石下石头
  - 水下表面检测：海底用 sand/gravel/clay（非 grass）

- Stage 3A：**Carvers — 已完成**
  - 位置：`src/main/java/com/CharunCore/server/worldgen/noisechunk/carver/`
  - `CaveWorldCarver.java`（~136 行）：随机游走隧道 + 房间，具分支/变向/变厚行为
  - `CanyonWorldCarver.java`（~132 行）：峡谷雕刻，具垂直平滑/随机宽度/水下检测
  - `CarvingMask.java`：BitSet 雕刻掩码，防止重复雕刻
  - `WorldCarver.java`：抽象基类，含 carveEllipsoid/carveBlock 通用方法
  - 集成进 `DensityRouterChunkGenerator.generate()`，在 SurfaceSystem 之后执行
  - 参考 CFR 反编译：`mapping/cfr-source/.../carver/CaveWorldCarver.java` 等

- Stage 3B Part 1：**Biome Resolution — 已完成**
  - 位置：`src/main/java/com/CharunCore/server/worldgen/biome/`
  - `Climate.java`：TargetPoint/Parameter/ParameterPoint/Sampler 记录 + ParameterList<T>（含 brute-force fitness 搜索）
  - `OverworldBiomeBuilder.java`：完整移植原版 5×5 温度×湿度查找表 + 大陆度/侵蚀度/怪异度分类 + addSurface/Underground/Bottom 分层（~440 行）
  - `MultiNoiseBiomeSource.java`：桥接 NoiseRouter 的 6 个气候 DensityFunction 到 Climate.Sampler → ParameterList 查询 → biome ID
  - 集成进 `DensityRouterChunkGenerator`：采样气候 → 逐列写入 Chunk.setBiome()
  - SurfaceSystem 已升级为 biome-aware：沙漠→sand、雪原→snow、恶地→coarse_dirt、平原→grass 等
  - 不含 R-Tree（brute-force 仅 ~400 条目，够快）

### 已完成阶段
- Stage 2：**SurfaceRules DSL + SurfaceSystem — 已完成**
  - 位置：`src/main/java/com/CharunCore/server/worldgen/surfacerule/`
  - `SurfaceRules.java`：完整 DSL（ConditionSource/RuleSource/Condition/SurfaceRule 接口 + StoneDepthCheck/NotCondition/YCondition/WaterCondition/BiomeCondition/NoiseThreshold/Steep/Hole/AbovePreliminarySurface/Temperature 条件；TestRule/SequenceRule/StateRule/Bandlands 规则；LazyYCondition/LazyXZCondition 延迟求值）
  - `SurfaceRuleContext.java`：Context 类，含 updateXZ/updateY、stoneDepthAbove/stoneDepthBelow/waterHeight/surfaceDepth 跟踪、温度/陡坡/空洞/预表面 lazy 条件
  - `SurfaceSystem.java`：buildSurface() 按原版语义（n7 自增 for 每个固体方块，非替换方块也计数）
  - `OverworldSurfaceRules.java`：完整覆盖 65 个 biome 的规则树（恶地陶土带、沼泽黏土、沙漠砂岩、雪原雪块、冰原冰、河流沙、海洋砾石/沙、通用草地/泥土等）
  - 集成进 DensityRouterChunkGenerator，保持深板岩梯度/基岩地板后处理
  - 测试验证：chunk(0,0) 产出 Stone 12.9% / Deepslate 15.0% / Dirt 2.0% / Grass 0.8% / Bedrock 0.3%，地形剖面正确

- Stage 3B Part 2：**Features + Placement — 已完成**
  - 位置：`src/main/java/com/CharunCore/server/worldgen/feature/`
  - `SimpleTreeFeature.java`：橡树/白桦/云杉/深色橡树 4 种树，带树干+树冠+泥土填充逻辑
  - `SimplePatchFeature.java`：grass/fern + flower 斑块（蒲公英/虞美人/兰花/绒球葱/矢车菊），按 biome 选择花种
  - 集成进 `DensityRouterChunkGenerator.placeFeatures()`，在 Carvers 之后、return 之前调用
  - 随机种子：`seedLo + chunkX*L + chunkZ*L + 98765L`
  - 测试验证：chunk(0,0) 产出 53 个特征方块（30 短草 + 12 蒲公英 + 11 虞美人），无树（1/12 概率未触发）

### 关键架构笔记
- 原版 `getInterpolatedState()` 返回 null → 生成器用 `settings.defaultBlock()`（通常为 STONE）
  → MaterialRuleList 链式判定：Aquifer → OreVeinifier，首个非 null 胜出
  → SurfaceRules DSL 替换默认方块（stone）为地表方块（grass/sand/snow 等）
  → 深板岩梯度（y<0）和基岩地板（minY）是独立后处理，在 SurfaceSystem 之后执行
  → Carvers（洞穴/峡谷）在 SurfaceSystem 之后应用，用 RandomSource + Aquifer + CarvingMask 雕刻洞穴
  → Biome Resolution 用 Climate.Sampler 采样 6 个路由器 → TargetPoint → brute-force ParameterList → biome ID
- `NoiseChunk` 内部类全部非静态（有隐式 outer this 引用），和原版一致
- `OreVeinifier.create()` 返回值遵循 `BlockStateFiller` 协议（Integer or null）

## 重要点
- NoiseRouterData 已完整可读（727 行），是移植密度链的"黄金"
- 项目已有简化 TerrainProvider（多个 `CubicSpline` 工厂）— 必须复用，重写会浪费
- 项目 noise 参数在 `com.CharunCore.server.world.gen.NoiseParameters` 已硬编码（35 个 noise 字段）
- `BlockStateHelper.getDefault(String name)` 是项目方块 ID 查询入口
- 现有 `com.CharunCore.server.world.gen.CubicSpline` 使用 `float[]` 上下文（4 元素: continentalness/erosion/ridges/ridges_folded）
  → `SplineAdapter` 已桥接：接受 4 个 `DensityFunction` 作坐标源，返回 spline.apply() 结果
