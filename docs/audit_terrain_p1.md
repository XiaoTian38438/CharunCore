# 地形 P1 审计：结构系统

> 审计范围：结构是否使用原版 nbt 模板、结构生成完整性、朝向/旋转正确性。
> 对照基准：`mapping/remapped_server_1.21.11.jar.src/`（原版反编译源码）。
> 审计方式：只读取源码与 nbt 数据，未修改任何 `.java`，未编译/未启动服务器。
> 审计日期：2026-08-11（项目演进快，结论基于本次实际读到的代码）。

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|--------|------|------|
| nbt 模板加载（活跃管线） | **已实现** | `structure2/StructureTemplate` 从 `json/minecraft/structure/`（gzip）+ `mapping/.../structure/`（回退）读取原版 nbt，并应用 `BlockTransform` 做旋转/镜像变换。 |
| jigsaw 结构（村庄/堡垒/古城等） | **已实现（简化）** | `structure2/JigsawPlacement` + `structure2/StructureTemplate`，可生成连通结构。 |
| 非 jigsaw 模板（igloo/ocean_ruin/ruined_portal/shipwreck/end_city/woodland_mansion 等） | **已实现** | `structure2/NonJigsawPlacer` 驱动。 |
| 程序化结构（desert_pyramid/jungle_temple/swamp_hut/mineshaft/stronghold 等） | **部分实现（近似）** | `structure2/NonJigsawPlacer` 手工搭建近似版，非 1:1 原版。 |
| 朝向变换 `BlockTransform` | **已实现（有偏差）** | `transformOrientation` 已实现（曾为空壳），但逻辑与原版 `FrontAndTop` 不一致。 |
| 旧版 `StructureManager.generateStructures` 管线 | **死亡代码** | 实际控制流只调用 `generateStronghold`，`generateStructures()` 无任何调用方。 |

**结论：活跃管线是 `structure2`（经 `DensityRouterChunkGenerator` 调用），旧版 `structure/` 包内的 `StructureManager.generateStructures` 与 4 个旧的模板生成器（IglooTemplateGenerator/OceanRuinGenerator/RuinedPortalGenerator/ShipwreckGenerator）及其 `StructureTemplateLoader` 均为死代码。**

---

## 2. 关键文件与行号证据

### 活跃管线调用点
- `../src/main/java/com/CharunCore/server/worldgen/DensityRouterChunkGenerator.java:823` — `JigsawPlacement.addPieces(...)`（jigsaw 结构）
- `../src/main/java/com/CharunCore/server/worldgen/DensityRouterChunkGenerator.java:834` — `NonJigsawPlacer.place(...)`（非 jigsaw 结构）
- `../src/main/java/com/CharunCore/server/worldgen/DensityRouterChunkGenerator.java:258` — `StructureManager.generateStronghold(...)`（仅要塞走旧分支）

### nbt 加载（活跃）
- `../src/main/java/com/CharunCore/server/worldgen/structure2/StructureTemplate.java:18` — `TEMPLATE_DIR = "json/minecraft/structure/"`
- `../src/main/java/com/CharunCore/server/worldgen/structure2/StructureTemplate.java:142-158` — `load()`：先试 `json/...`，失败回退 `mapping/...`；用 `GZIPInputStream` 解压（文件确为 gzip，magic `1f 8b`）。
- `../src/main/java/com/CharunCore/server/worldgen/structure2/StructureTemplate.java:75-108` — `placeInWorld()`：跳过 `jigsaw` 块（`line 79`），对其它块调用 `BlockTransform.transform`（line 84），并写方块实体（line 88-106）。
- `../src/main/java/com/CharunCore/server/worldgen/structure2/StructureTemplate.java:110-121` — `isBlockEntityBlock()`：硬编码方块实体名单。

### 朝向变换
- `../src/main/java/com/CharunCore/server/worldgen/structure2/BlockTransform.java:11-58` — `transform()` 主逻辑。
- `../src/main/java/com/CharunCore/server/worldgen/structure2/BlockTransform.java:67-75` — `transformOrientation()`（front 旋转 / top 镜像）。
- `../src/main/java/com/CharunCore/server/worldgen/structure2/BlockTransform.java:77-118` — `rotateDir()` / `mirrorDir()`。

### jigsaw
- `../src/main/java/com/CharunCore/server/worldgen/structure2/JigsawPlacement.java:16-63` — `addPieces()` 入口。
- `../src/main/java/com/CharunCore/server/worldgen/structure2/JigsawPlacement.java:100-257` — `tryPlacingChildren()` 片段拼接与碰撞剔除。
- `../src/main/java/com/CharunCore/server/worldgen/structure2/JigsawBlockInfo.java:39-45` — `canAttach()`（front 相对 + top + target/name 匹配）。

### 非 jigsaw / 程序化结构
- `../src/main/java/com/CharunCore/server/worldgen/structure2/NonJigsawPlacer.java:30-54` — `place()` 按 type 分发。
- `../src/main/java/com/CharunCore/server/worldgen/structure2/NonJigsawPlacer.java:79-96` — `placeIgloo()`（正确用 nbt 模板）。
- `../src/main/java/com/CharunCore/server/worldgen/structure2/NonJigsawPlacer.java:180-214` — `placeStronghold()`（手工近似，见下）。
- `../src/main/java/com/CharunCore/server/worldgen/structure/StrongholdPortalRoomGenerator.java:30-43` — **活跃**的要塞生成（传送门房间）。

### 死亡代码（仅记录，不影响运行）
- `../src/main/java/com/CharunCore/server/worldgen/structure/StructureTemplateLoader.java:100-111` — 旧加载器，裸 `NBTInputStream` 读 gzip 文件（无 `GZIPInputStream`）。
- `../src/main/java/com/CharunCore/server/worldgen/structure/ShipwreckGenerator.java:9-29` — 引用 `shipwreck/shipwreck_beached_1..20.nbt`（不存在）。

---

## 3. 对照原版的关键差异/偏差

### 3.1 朝向 `orientation` 变换逻辑与原版不一致（活跃管线）
原版 `FrontAndTop.rotation(Rotation)` = `fromFrontAndTop(front.rotate(rot), top.rotate(rot))`，即 **front 与 top 同时旋转**；`mirror(Mirror)` 同理 **front 与 top 同时镜像**（见 `mapping/cfr-source/net/minecraft/core/FrontAndTop.java` 及 `CrafterBlock.java:229/234`）。

项目 `BlockTransform.transformOrientation`（`BlockTransform.java:67-75`）只旋转 `front`、只镜像 `top`，二者未同时处理：
- 纯旋转（mirror=NONE）时 `top` 不变（原版会一并旋转）；
- 纯镜像（rotation=NONE）时 `front` 不变（原版会一并镜像）。

影响范围：原版 `orientation` 属性（`FrontAndTop`）只被 `jigsaw`（放置时被跳过，`StructureTemplate.java:79`）与 `crafter`（`CrafterBlock.java:66` 注册 `ORIENTATION`）使用。因此该偏差**仅当模板中含 `crafter` 且结构以非 NONE 的旋转/镜像放置时**才显现（crafter 朝向/顶面会错误）。

### 3.2 jigsaw `ROLLED` joint 未作用于子片段旋转选择（活跃管线）
`JigsawPlacement.tryPlacingChildren`（`JigsawPlacement.java:155-254`）遍历 4 个旋转并依赖 `canAttach`（`JigsawBlockInfo.java:39-45`）匹配。原版对 `joint == ROLLED` 的父子连接会把子片段旋转额外翻转 180°；项目未实现该翻转，仅通过穷举旋转 + `canAttach`（非 ROLLED 时强约束 top 相等）找到首个可连接姿态。对大多数拼图池只是“选了等价但不同的旋转”，结构仍能连通，但与原版姿态可能有细微差异。

### 3.3 碰撞剔除策略（活跃管线）
`JigsawPlacement.java:203-225` 仅当子包围盒与已存在片段**真正 3D 重叠**（排除共面/相切）时才拒绝。原版 jigsaw 放置本身不做基于包围盒的重叠拒绝（片段允许重叠，靠拼图设计保证不冲突）。当前策略比原版更“保守地拒绝”，但注释说明是为了避免把正常共享墙的相邻片段误杀（此前曾因过度拒绝导致村庄只剩 1 块）。属合理偏差，但与原版不完全一致。

### 3.4 程序化结构为近似而非 1:1（活跃管线）
`NonJigsawPlacer` 中的 `placeDesertPyramid`（`NonJigsawPlacer.java:216-277`）、`placeJungleTemple`（`:279-351`）、`placeSwampHut`（`:353-412`）、`placeMineshaft`（`:119-178`）、`placeStronghold`（`:180-214`，且为死代码）、以及活跃的 `StrongholdPortalRoomGenerator`（传送门房间）均为**手工近似**：
- 沙漠神殿/丛林神殿/沼泽小屋外形接近，但缺少原版内部机关（红石陷阱连线、隐藏密室等细节）的逐块还原；
- 矿井为笔直/微弯隧道 + 随机铁轨/蛛网/箱子（原版为分叉房间网络）；
- **要塞仅生成单个 13×13 传送门房间 + 竖井（`StrongholdPortalRoomGenerator.java:58-158`），不含原版的 12 环走廊、图书馆、喷泉房、蠹虫刷怪笼等**。原版要塞在结构系统中属 `concentric_rings` 放置类型，项目未实现完整要塞（`StructureManager.java:171-176` 注释已说明此限制）。
- 活跃要塞传送门眼的随机概率为 10%（`StrongholdPortalRoomGenerator.java:99`），原版传送门框架默认无眼（由玩家放置），属轻微偏差。

### 3.5 方块实体名单硬编码（活跃管线）
`StructureTemplate.java:110-121` `isBlockEntityBlock()` 为手写名单。原版通过方块注册表判断是否为方块实体。当前名单可能遗漏部分 BE 类型（如 `smoker`/`blast_furnace`/`sculk_catalyst`/`trial_spawner`/`vault`/`chiseled_bookshelf` 等），导致这些方块在模板中以普通方块放置、丢失方块实体数据。

---

## 4. Bug 清单（只记录不修）

| # | 严重度 | 文件:行号 | 现象 | 建议修复方向 |
|---|--------|-----------|------|--------------|
| P1-1 | 中等 | `structure2/BlockTransform.java:67-75` | `transformOrientation` 只旋转 front / 只镜像 top，原版 `FrontAndTop` 为 front 与 top 同时旋转/镜像；含 `crafter` 且带旋转的模板中 crafter 朝向错误。 | 改为 `front = front.rotate(rot)` + `top = top.rotate(rot)`（镜像同理），两方向同时变换；参考 `FrontAndTop.fromFrontAndTop`。 |
| P1-2 | 中等 | `structure/StrongholdPortalRoomGenerator.java:30-158`（活跃）| 要塞仅生成单个传送门房间 + 竖井，无完整要塞迷宫（环廊/图书馆/喷泉/蠹虫笼）。 | 实现完整 `concentric_rings` 要塞（或至少在结构集中登记要塞拼图池并用 jigsaw/手拼生成）；否则需明确文档标注“仅传送门房间”。 |
| P1-3 | 中等 | `structure2/NonJigsawPlacer.java:119-178`（矿井）、`:216-277`/`:279-351`/`:353-412`（三神庙） | 程序化结构为近似，缺内部细节，与原版 1:1 偏差。 | 若要求逐块一致，应改用原版 nbt 模板或逐块移植原版 `Piece` 逻辑；否则纳入“已知近似”文档。 |
| P1-4 | 轻微 | `structure2/StructureTemplate.java:110-121` | `isBlockEntityBlock()` 硬编码名单，可能遗漏部分 BE 类型。 | 用方块注册表/属性判断是否为方块实体，或补齐名单（含 trial_spawner、vault、chiseled_bookshelf 等 1.21 新增）。 |
| P1-5 | 轻微 | `structure2/JigsawPlacement.java:155-254` | `ROLLED` joint 未翻转子片段旋转，与原版姿态可能有细微差异。 | 在子旋转选择中：若 `parent.rollable()`，对计算出的旋转再叠加 `CLOCKWISE_180`（对齐原版 `JigsawPlacement`）。 |
| P1-6 | 低（死代码） | `structure/StructureTemplateLoader.java:100-111` + `ShipwreckGenerator.java:9-29` 等 | 旧加载器裸读 gzip nbt（无 GZIP 解包）会解析失败；ShipwreckGenerator 引用不存在的 `shipwreck_beached_1..20.nbt`。均为死代码，不影响运行，但易误导。 | 删除旧 `structure/StructureTemplateLoader` 及 4 个旧模板生成器（IglooTemplateGenerator/OceanRuinGenerator/RuinedPortalGenerator/ShipwreckGenerator）与 `StructureManager.generateStructures()`，避免与活跃管线混淆。 |
| P1-7 | 低 | 结构注册（需确认） | 需确认 `StructureRegistry`/`StructureSet` 是否把所有结构类型（ocean_monument/woodland_mansion/end_city/trial_chambers/trail_ruins/ancient_city 等）正确路由到活跃 `structure2` 管线。 | 核对 `StructureRegistry` 注册表，确保每个原版结构类型都有对应 type 且被 `NonJigsawPlacer`/`JigsawPlacement` 覆盖。 |

---

## 5. 结论与优先级建议

**整体判断**：活跃结构管线（`structure2`）正确加载并使用原版 nbt 模板（gzip 压缩、带有 `mapping` 回退），并且对模板块应用了旋转/镜像变换；jigsaw 拼接可生成连通结构，nbt 模板路径（如 shipwreck 的 `rightsideup_*`）在活跃管线中是正确的。**此前的 jigsaw 旋转错位（`BlockTransform.transformOrientation` 空壳）问题已修复**，但修复后的实现与原版 `FrontAndTop` 语义仍有偏差。

**优先级建议**：
1. **P1-2（要塞完整性）**：若游戏目标包含“通关末地”，要塞当前只能到达传送门房间、无探索内容，属最高玩法影响项，建议优先评估。
2. **P1-1（transformOrientation）**：影响含 crafter 的模板在旋转/镜像时的正确性，建议修正以与原版 `FrontAndTop` 对齐。
3. **P1-3 / P1-4**：程序化结构近似与方块实体名单，属“外观/细节”偏差，可排期中。
4. **P1-5**：jigsaw ROLLED 姿态差异，影响极小，可最后处理。
5. **P1-6 / P1-7**：死代码清理 + 结构注册核对，建议顺手做以消除误导与潜在遗漏。

> 注：旧版 `StructureManager.generateStructures()` 与本报告列出的旧生成器均未被调用，其问题（如 ShipwreckGenerator 错误路径）不会在实际地形中触发，列为“死代码”低优先级。
