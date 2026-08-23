# 项目长期笔记 - mc-server-core1.21.11 (YanRong 自研 MC 1.21.11 核心)

## 编译与运行
- 编译（全量重编，最可靠）：项目根目录执行 `javac @javac_args.txt`。该文件含 `-d target/classes`（自动建目录）并显式列出所有源码文件（含 ChunkEncoder.java），无增量跳过问题。
- 启动：`cd` 项目根后 `java -cp "$(cat runcp.txt)" com.yanrong.server.Main`（Windows 用分号 classpath）；读取 server.properties/world/players 等相对路径，默认监听 25565。
- IntelliJ 教训：勿用普通 Make/Run 增量编译——曾静默跳过 ChunkEncoder.java，使修复前的旧 8 段 class 上线，主世界玩家收到 8 段包却按 24 段解析而 readLong 越界崩溃。改用 Build → Rebuild Project 全量重编。
- 区块包(0x2C level_chunk_with_light)验证：**必须用真实 MC 1.21.11 客户端解码器做 oracle**——`ClientboundLevelChunkWithLightPacket.STREAM_CODEC.decode(new RegistryFriendlyByteBuf(new FriendlyByteBuf(wrapped), RegistryAccess.EMPTY))` 后断言 `readableBytes()==0`，见 `tools_test/VerifyWithVanilla.java`（覆盖主世界/下界/末地多坐标）。这是唯一可信回归测试。
  - **光照数组陷阱（曾导致主世界/下界双双崩溃的回归）**：每个 2048 字节光照数组前必须有 `VarInt(2048)` 长度前缀。`ByteBufCodecs.byteArray(2048)` 的 encode=`writeByteArray`(VarInt+2048字节)，decode=`readByteArray(buf,2048)`(VarInt.read()+bytes)。漏写前缀（用 `writeRawBytes` 直接写 2048）→ 客户端把主世界 0xFF 天空光当 VarInt 读→「VarInt too big」崩溃；把下界/末地 0x00 当下标 0→吞掉全部光照(desync 32752 字节，客户端不报错但光照全丢)。`ChunkEncoder.writeChunkPacket` 已修。
  - **高度图 key 陷阱**：heightmap map codec 的 key 写 `Heightmap.Types` 的 `id`（WORLD_SURFACE=1, MOTION_BLOCKING=4），不是 enum 序数(0/2)。
  - 自编自解只验「外层 0 剩余」是假阳（手动解码会复用同样的错误假设，曾误判 PASS）。

## 传送/重生「加载地形中」界面卡住
- 1.21 起客户端关闭加载界面必须满足：收到 **Game Event 13 (LEVEL_CHUNKS_LOAD_START)** 且玩家处于已加载/编译区块（`LevelLoadStatusManager`：WAITING_FOR_SERVER→WAITING_FOR_PLAYER_CHUNK→LEVEL_READY）。`ChunkBatchFinished`(0x0B) 只更新批量统计、**不**关界面。
- 登录 `sendLoginPlay` 已在末尾发 `0x26`(game_event) `writeByte(13)+writeFloat(0.0f)` → 登录正常。
- 传送 `teleportToDimension` 与死亡重生 handler 原本**漏发** Game Event 13 → 界面卡死。修复：0x46 传位后、`sendInitialChunks` 前各补 `0x26`(event=13,param=0.0f) + `0x6F`(SetTime)。原版在 `PlayerList.sendLevelInfo`(PlayerList.java:583) 对死亡重生与维度切换都发 `LEVEL_CHUNKS_LOAD_START`。
- 1.21.11 包号：game_event=0x26（xKumorio 表 1.21.9–1.21.11）。**未核实的包号切勿乱加**（曾误加 0x39/0x38 已删，会破坏客户端解码流）。

## 包格式审计权威参考（2026-08-09 加）
- **标准 1.21.11 客户端包号表**：`mapping/protocol_ref/protocol_1.21.11.json`（minecraft-data，curl 下载）。线序在 `play.toClient` 内的 `"0xNN":"packet_name"` 反向映射。本服 play 阶段包号即此表；`GameProtocols.java` 反编译的 addPacket 顺序**不等同**真实线序。
- **两个基础原语已验证正确**（勿再疑）：`writeSlot`=VarInt(count),VarInt(id),VarInt(0),VarInt(0)（=ItemStack.OPTIONAL，DataComponentPatch 空=两个 VarInt(0)）；`writeAnonymousNbt`=byte(10)+entries+TAG_End（匿名根，组件字段用它对）。
- **已知已修的崩溃包**：攻击受击闪原误发 `0x29`(hurt_animation) 应为 `0x22`(entity_status,int+byte,status=2)。`0x19` DamageEvent 的 source id 须 `writeVarInt(id+1)`（writeOptionalEntityId）。
- 对照源：`mapping/cfr-source/net/minecraft/network/protocol/game/Clientbound*Packet.java` 的 `write` 方法（字段顺序/线类型权威）。

## 审计/改 bug 铁律（2026-08-10 加）
- 项目演进极快，带日期的旧审计文档（如 `docs/survival_audit.md`）数天内即过时。**动手前必须 grep 当前代码核实状态，绝不信任旧结论**（实例：旧版称"窒息缺失"实为已实现；"溺水已修复"实为每 tick 过快）。
- 玩法中枢是 `NetworkHandler.java`（6120 行）。审计前先 `grep -nE "方法签名"` 画方法地图，再分域用 Explore 子代理核验，勿通读全文件。

## 结构生成：jigsaw/NBT 模板旋转（2026-08-11 加）
- **核心坑：`BlockTransform.transformOrientation` 曾是空实现**（直接 `return stateId` 不旋转）→ 所有 jigsaw 结构朝向错乱：坐标被旋转但 `orientation` 属性没旋转，attach 点反向，村庄只剩 1 片段、所有结构旋转错位。已修复：`rotateDir`（按 Rotation 旋转水平 front）+ `mirrorDir`（按 Mirror 翻转水平 top），与 vanilla `Orientation.rotate/mirror` 语义一致。
- jigsaw 用 1.21 的 `orientation` 属性（如 `north_up` = front_up），不是旧版 `facing`。`JigsawBlockInfo.frontFacing()` 从 orientation 第一个词读。旋转后 front 必须随之旋转，否则 `canAttach` 虽匹配但 attach 点算反、子片段塞进父内部被碰撞误拒。
- 村庄 jigsaw（`JigsawPlacement.addPieces`）关键：碰撞检测**必须跳过父 piece 自身**（decorator 如 iron_golem/cats/villagers 的 jigsaw 在父建筑内部，`front=up target=bottom`，其 1×1×3 占位本就落在父内；vanilla 对「attach 点在父内部的候选」只用父 shape 检测而接受，若拿全局 pieces（含父）比对则误拒，村庄缺铁傀儡/猫/村民生成点）。
- 验证手段：`StructVerify.java`（临时诊断，已删）驱动真实生成器放要塞/末地城/村庄到 WorldGenLevel 窗口并 dump 方块名。要塞应含 `end_portal_frame:12`+`end_portal:9`+`lava`；村庄应 ~30-110 片段、含 `dirt_path/oak_planks/oak_log/farmland/lectern` 等且无 `[WARN bad/air]`（方块编码正确）。

## 末地外岛 & SimplexNoise（2026-08-12 加）
- **EndIslandDensityFunction 必须用 2D simplex**：原误用 3D `getValue(l,0.0,l2)`（y=0 切片），项目 3D 值域只有 [-0.868, 0.868] → 永远 < -0.9 阈值 → 外岛候选零命中 → 末地只有主岛。`SimplexNoise` 已加原版 2D `getValue(double,double)`（归一化 **70.0**、maxValue **0.5**、3 角、z=0、梯度 %12，值域约 [-1,1]）。`EndIslandDensityFunction` 构造用 `LegacyRandomSource(seed).consumeCount(17292)` + `SimplexNoise(rng)`（原版种子派生）。
- 2D/3D simplex 值域与分布不同；凡用噪声阈值（-0.9 之类）判断，先验证噪声值域。回归工具 `tools_test/EndIslandChunkTest.java`（主岛 end_stone 12000+、外岛区 1344+ 方块外有 110~10491、空隙 ~900 方块为空）。
- end router 的 `endIslands(0L)` 硬编码 0 与原版一致（不是 bug）。

## RedstoneEngine 维度架构（2026-08-12 加）
- RedstoneEngine 全 static，现用 `ThreadLocal<DimensionType> CTX_DIM` + 私有 `getState/putState(x,y,z)`（内部走 `WorldManager.getBlockState/setBlock(CTX_DIM.get(),...)`）+ `withDim(dim, Runnable)`（try/finally 恢复）。**55 处**方块读写、**18 处**广播全部维度感知。
- 入口重载：`onBlockChanged/updateNeighborsAt/registerTracked(dim,...)`（无维度版委托 OVERWORLD）；`tick()` 按 3 维度 withDim 遍历；`trackedObservers/trackedPlates/observerLastSeen` 是 **EnumMap<DimensionType,...>** 分层；`ScheduledUpdate` 带 dim 字段。
- **TNT 陷阱**：引爆在独立 tntScheduler 线程，必须捕获 `ctxDim()` 到 lambda 再 withDim，否则下界 TNT 炸主世界。
- `NetworkHandler.broadcastBlockChange/broadcastRedstoneParticles` 有维度重载并过滤 `player.currentDim != dim`（修复下界红石变化错发主世界玩家）。新子系统若广播方块变化，务必传对维度。
- 已知待优化：村庄结构生成对每个放置方块调 broadcastBlockChange → 单秒数千条 0x08 发给玩家（带宽浪费），后续可在结构放置阶段批量/抑制广播。

## 流体 0x08 风暴 & 结构/合成/附魔（2026-08-13 加）
- **0x08 block_update 风暴根因**：`FluidEngine.processFluid` 对**水源(level=0)也执行水平蔓延**（`level < maxLevel` 恒真）→ 海洋/河流边缘水源持续溢出 → 每 tick 广播（实测 90K 条/40s，state 86-91=water）。诊断方法：broadcastBlockChange 临时打"线程名+坐标"，见线程即定位。
- **最终流体方案（2026-08-13 22:30 定稿）**：①向下流优先且 `return`（垂直优先，防水流下山坡每层横向扩散的级联）；②水源水平蔓延恢复（放桶水能扩散）；③**玩家模拟距离 128 格**（`anyPlayerNearby`，远处流体不处理，原版 ticking 区块语义）；④**`scheduleChunkFluids` 只调度"稳定水源"**（level=0 且下方有实体支撑）——自然水体保持生成状态不排洪（原版地形水域不流动），流动水靠玩家交互 `neighborChanged` 触发；⑤玩家倒水走 `FluidEngine.spreadSource()` 显式扩散。效果：0x08 从 90K → 2-6 条，玩家放水/挖水正常流动。
- **结构缺角不能靠 generate 递归补做**（无限递归栈溢出）。正解：`registerStructuresForChunk` 在 placeFeatures 前提前注册 jigsaw start，`placeStructures2` 只放置。
- **下界/末地结构生成的必要条件**：`computeColBiome` 必须对下界/末地返回固定 biome（下界 34 nether_wastes、末地 56/44/16/18/17 按距离），否则结构 biome 检查永远失败 → 要塞/堡垒静默不生成。
- **`isStructureAllowed` 按结构 id 判断**（fortress.json type=minecraft:fortress、bastion type=jigsaw，按 type 匹配必失败）；`BiomeTagResolver` 需递归展开嵌套 tag（nether_fortress → #minecraft:is_nether）。
- **1.21.11 serverbound 包号**：0x2f=name_item（0x2c=pong）。铁砧改名用 0x2f。
- **战斗/实体 status（2026-08-13 22:40）**：0x22 entity_status：2=hurt(红闪)、3=death、4=attack(生物攻击动画)。受击链路=broadcastEntityHurt（0x22+0x61 health+音效）；死亡=broadcastEntityDeath（0x22 status=3+death 音效）；攻击动画=broadcastEntityAttack（status=4）。玩家攻击冷却剑 625ms、受击无敌 10 tick（连点会被无敌挡→无伤害无动画，原版同机制）。PVP/打怪统一用 `getAttackDamage(weaponName)`+二次蓄力公式+3.5 格距离限制。末影人受击 33% 瞬移 8-32 格；苦力怕 2.5 格内 30 tick fuse。
- **附魔台 data slot 顺序**（EnchantmentMenu）：0-2=cost、3=seed、4-6=enchantClue(附魔注册表id)、7-9=levelClue。
- **minecraft:custom_name 组件值是 NBT 复合标签**（`writeAnonymousNbt({"text":...})`），不是字符串。
- **配方书填充（place_recipe 0x26）必须查背包扣料**（3×3 分支曾只写快照=白嫖合成）；tag 匹配用 `findCraftingSrc` 按全集；shapeless 消耗走贪心全扣；milk_bucket/bowl 消耗需返还（refundByproduct）。


## 审计要点（2026-08-14）
- **红石坐标 key 用原版 BlockPos 编码**：`(x&0x3FFFFFF)<<38 | (z&0x3FFFFFF)<<12 | (y&0xFFF)`——旧 `x<<32|z32|y16` 位冲突（z 高 16 位被 y 覆盖，z<0 全失效）。
- **箭/珍珠必须检测玩家**（玩家不在 EntityManager 实体 Map 里，findEntityHit 需同时遍历 NetworkHandler.players）。
- **物品堆叠上限**：items.json stackSize 字段（末影珍珠/箭 16、桶/药水 1），BlockManager.getStackSize 查表。
- **怪物受击无敌帧有意 10 tick**（非原版 20）：用户嫌怪难打，保持玩家 DPS。
- **末地紫颂树**：ChorusTreeFeature（外岛 |x|>100 才生成）。
- **javac_args.txt 自动生成**：`find src/main/java -name *.java`，新类必须重新生成否则漏编译。
- 成就系统：25 个（story/nether/end/adventure/husbandry），ENCHANT_ITEM/CONSUME_ITEM trigger 已接线。
