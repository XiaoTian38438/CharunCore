---
feature: terrain-generation-fix
status: designed
updated: 2026-07-25
---

# Terrain Generation Fix — 梯田化/斑块化/单调特征

## Report

## [S1] Problem

用户报告三个地形生成缺陷：
1. **梯田状地形**：地形出现小规模突变起伏，缺乏海绵状自然过渡
2. **地表草与沙子交替成不规则圆形分布**：表面方块类型分布不自然
3. **岩浆池/树木/植物过于单一**：特征放置与原版差异巨大

经过对 vanilla 1.21.11 源码的逐行对比分析，确认核心密度函数链（NoiseRouterData, DensityFunctions, CubicSpline）已正确移植。问题出在三个下游系统。

## [S2] Design

### 问题 A：梯田状地形（根本原因：特征放置网格化）

`DensityRouterChunkGenerator.placeFeatures()` 在固定网格上放置特征：
- 树木：8×8 网格（lx=4,12; lz=4,12），1/chance 概率
- 草/花：4×4 网格（lx+=4, lz+=4），每个格子固定放置

原版使用 `RandomPatchFeature` + `VegetationPatchFeature`：
- 在 **23×23 评估区域**内随机尝试 128 次
- 使用 `column` 检查方块有效性
- 使用 `SpreadType` 控制水平扩展
- 密度由 biome 的 `vegetation` 噪声控制

**修复方案**：重写 `placeFeatures()` 中的特征放置逻辑：
1. 树木使用 `XoroshiroRandomSource` + biome-specific 配置（高度范围、叶子类型）
2. 草/花使用 128 次随机尝试 + 12×12 评估区域（匹配原版 `RandomPatch` 语义）
3. 树木放置使用 16×16 区域内的 biome 特定密度

### 问题 B：表面斑块化（根本原因：表面规则 biome 条件正确但特征掩盖了效果）

`OverworldSurfaceRules` 的 biome 条件与原版匹配。不规则圆形分布是由 `surfaceNoise` 的噪声阈值自然产生的，这在原版中也存在。但用户看到的是不自然的模式，因为：

1. `SimplePatchFeature.placeGrassPatch()` 只在 `grass_block` 上放置 → 非草地生物群系（沙漠、海洋等）完全没有植被
2. `placeFeatures()` 中的草地放置过于规则（4×4 网格）

**修复方案**：
1. 草地放置改为 128 次随机散布（匹配原版 RandomPatch 语义）
2. 添加 biome-specific 花种多样性（匹配原版 `FlowersFeature` 的 18 种花）
3. 在生物群系边界使用噪声混合（已有 surfaceNoise 支持）

### 问题 C：特征单调（根本原因：特征类型和配置不足）

当前已实现的特征类型 vs 原版 overworld 生物群系需要的特征：

| 特征类型 | 当前状态 | 原版需要 |
|---------|---------|---------|
| 树木 | 4 种（橡木/白桦/云杉/深色橡木） | 12+ 种（含丛林木、金合欢、红树林） |
| 草 | 1 种（short_grass） | 多种高度 + 空气间隙 |
| 花 | 5 种 | 18+ 种，按 biome 分配 |
| 仙人掌 | 有 | 正确 |
| 藤蔓 | 有 | 需要方向属性 |
| 巨型蘑菇 | 有 | 需要更大的变种 |
| 灌木 | 无 | 丛林灌木、浆果丛 |
| 地衣 | 有（glow_lichen） | 需要多面生长 |

**修复方案（分优先级）**：

**P0 — 修复特征放置系统（解决梯田+单调问题）**：
1. 重写 `placeFeatures()` —— 去除固定网格，改用随机散布
2. 添加 `RandomPatchFeature` 逻辑（128 次尝试，12×12 区域）
3. 调整树木密度（匹配原版 `TreeFeature` 配置）

**P1 — 增强特征多样性**：
1. 添加金合欢树、红树林树、丛林灌木
2. 增加花种类（匹配原版 `FlowersFeature` 的 18 种）
3. 调整 biome→特征 映射表

**P2 — 修复表面装饰系统**：
1. `SurfaceDecorator` 中添加更多 biome-specific 装饰
2. 确保沼泽、恶地等特殊 biome 的装饰正确

## [S3] Out of Scope

- 不修改核心密度函数链（NoiseRouterData, DensityFunction, CubicSpline）
- 不修改 NoiseChunk 插值系统
- 不修改 Aquifer 系统
- 不修改 Carver 系统
- 不修改 SurfaceRules DSL 框架
- 不引入结构生成（Village, Mineshaft 等）
- 不引入世界边界/混合系统

## Tasks

- [ ] T1: 重写 RandomPatch 特征放置逻辑 — 替换固定网格为 128 次随机散布，12×12 评估区域，支持 spreadType 水平扩展 — acceptance: 草地/花分布均匀无网格感 (covers: S2-A, S2-C)
- [ ] T2: 重写树木放置逻辑 — 使用 XoroshiroRandomSource + biome-specific 密度配置，添加丛林木、金合欢、红树林 — acceptance: 树木密度与原版匹配，不同 biome 有不同树种 (covers: S2-A, S2-C)
- [ ] T3: 增强花种多样性 — 添加原版 18 种花，实现 biome-specific 花种映射 — acceptance: 花园森林有多种花，平原有蒲公英+虞美人 (covers: S2-B, S2-C)
- [ ] T4: 修复灌木和浆果丛放置 — 添加丛林灌木、甜浆果丛、藤蔓方向属性 — acceptance: 丛林有灌木，针叶林有浆果 (covers: S2-C)
- [ ] T5: 调整 biome→特征密度参数 — 匹配原版 `BiomeGenerationSettings` 中的装饰步骤密度 — acceptance: 各 biome 的特征密度与原版一致 (covers: S2-A, S2-B)
- [ ] T6: 验证和修复 — 编译运行，生成测试区块，对比原版截图验证 — acceptance: 梯田感消除，斑块分布自然，特征丰富 (covers: S2-A, S2-B, S2-C)
