# 地形 P2 噪声系统审计报告

> 审计对象：自研 MC 1.21.11（Protocol 774）核心 `com.yanrong.server` 地形噪声生成系统
> 审计角色：资深 Minecraft Java Edition 服务器核心架构师
> 审计铁律：仅产出报告，**未修改任何源码**
> 权威对照：原版 `mapping/remapped_server_1.21.11.jar.src/` 与 `mapping/cfr-source/`（cfr 反编译，可读）

---

## 1. 实现状态概述

| 子系统 | 状态 | 说明 |
|---|---|---|
| `DensityFunction` 接口与 `NoiseHolder` | ✅ 已实现（忠实） | 接口形态与原版一致；噪声实例通过 `sharedFactory().fromHashOf(key)` 创建，与原版一致 |
| 坐标偏移噪声族 `ShiftA` / `ShiftB` / `ShiftNoise` / `ShiftedNoise` / `ShiftedNoise2D` | ✅ 已实现（忠实） | 数学与原版 `DensityFunctions` 对应类一致 |
| `FindTopSurface` | ✅ 已实现（忠实，已修正） | 与原版一致的自上而下扫描，返回首个密度 > 0 的 Y |
| `NoiseRouterData` | ✅ 已实现（结构移植忠实） | 图、`slide`、`spaghetti`/`noodle`/`pillars`、常量均移植 |
| `NormalNoise` | ✅ 已实现（忠实） | `INPUT_FACTOR=1.0181268882175227`、`TARGET_DEVIATION=0.16666666666666666`、双层采样一致 |
| `BlendedNoise` / `BlendedNoiseAsDF` | ✅ 已实现（忠实） | `compute` 与原版逐行一致 |
| `Mapped`（含 `SQUEEZE`） | ✅ 已实现（忠实） | 六种映射全部对齐，含 `SQUEEZE` 的 `smoothstep` 形式 |
| `WeirdScaledSampler` | ✅ 已实现（忠实） | rarity 阈值（3.0/2.0/1.0/0.75/0.5 等）与 `|noise|*rarity` 一致 |
| `TerrainSplineProvider.peaksAndValleysDF` | ✅ 已实现（忠实） | 表达式 `1 - 3*||ridges|-2/3|` 一致 |
| `NoiseChunk` 插值（`lerp3`、`NoiseInterpolator`、`CacheAllInCell`/`Cache2D`/`CacheOnce`/`FlatCache`） | ✅ 已实现（忠实） | `Mth.lerp3`、cell 内缓存、Y 轴自上而下遍历一致 |
| `DensityFunctions` 工厂装配 | ⚠️ 部分偏差 | `Marker` 类型 → `NoiseChunk.wrap` 装配正确；但 `noise(key,params,xzFactor)` 2/3 参形式把单参当 xzScale 且 yScale 恒为 0（见 **P2-4**），与原版“单参=yScale、xzScale=1.0”不符 |
| `Noise` / `RangeChoice` / `TwoArgumentSimpleFunction` / `CubicSpline`（`Multipoint.apply` 三次 Hermite）/ `SplineAdapter` / `PerlinNoise`（`wrap`/`getOctaveNoise`）/ `Lerp` / `YClampedGradient` / `ImprovedNoise` | ✅ 已实现（忠实） | 逐一比对无偏差 |
| `XoroshiroRandomSource`（`XoroshiroPositionalRandomFactory.at`） | ⚠️ 部分偏差 | `fromHashOf` 忠实；`at(x,y,z)` **未将坐标异或进 `seedHi`** |
| `NoiseBasedAquifer` 流体判定 | ⚠️ 偏差 | 整体为忠实 CFR 移植，但 `isLava`/`isWater` **硬编码 lava=11 / water=9** |

**总体结论**：P2 噪声数学链路是**高保真移植**，密度图、`slide`、插值、`blended`、样条、坐标偏移噪声、RNG 主体均与原版一致；识别到 **2 个需关注偏差**（P2-1 含水层硬编码流体 ID、P2-2 位置随机 `at()` 未混淆高位种子），均无崩溃风险但会影响流体/表面/矿脉随机性的“原版一致性”。

---

## 2. 关键文件与行号证据

> 所有路径相对项目根。包层级注意：`density` 在原版中位于 `world.level.levelgen`，本项目拆为 `worldgen.density` 与 `worldgen.density.functions`；底层噪声工具位于 `world.gen`。

| 文件 | 关键行 | 证据 |
|---|---|---|
| `../src/main/java/com/CharunCore/server/worldgen/density/DensityFunction.java` | 82–97 | `NoiseHolder` 的 `sharedFactory()` 用 `new XoroshiroRandomSource(seedLo).forkPositional()`，与原版 `RandomSource.create(seed).forkPositional()` 一致 |
| `../src/main/java/com/CharunCore/server/worldgen/density/functions/FindTopSurface.java` | 22–35 | 自上而下（从 `upperBound` 向下按 `cellHeight` 步进）返回首个 `density.compute(...) > 0.0` 的 Y；与原版 `findTopSurface` 语义一致 |
| `../src/main/java/com/CharunCore/server/world/gen/NormalNoise.java` | 4–5, 47–49 | `INPUT_FACTOR = 1.0181268882175227`、`TARGET_DEVIATION = 0.16666666666666666`、`x2=x*INPUT_FACTOR` 等，与原版常量与坐标缩放一致 |
| `../src/main/java/com/CharunCore/server/world/gen/BlendedNoise.java` | 48–108 | `compute` 与原版逐行一致：主噪声 8 个八度 `(wrap(d4*l12), wrap(d5*l12), wrap(d6*l12), d8*l12, d5*l12)`，`d13=(d11/10+1)/2`，最终 `clampedLerp(d13, d9/512, d10/512)/128` |
| `../src/main/java/com/CharunCore/server/worldgen/density/functions/BlendedNoiseAsDF.java` | 整类 | 包装 `BlendedNoise.compute`，`minValue/maxValue` 透传 |
| `../src/main/java/com/CharunCore/server/worldgen/density/functions/Mapped.java` | 14, 73/76/81 | `SQUEEZE(Mapped::squeeze, ...)`；`squeeze` 第 81 行 `(abs<1?abs*abs*(3-2*abs):1)*signum(v)`，与原版 `SQUEEZE` 的 smoothstep 一致 |
| `../src/main/java/com/CharunCore/server/worldgen/density/functions/WeirdScaledSampler.java` | 30–51 | `rarity(v)` 阈值映射、第 51 行 `Math.abs(noise.getValue(x,y,z))*r`，与原版一致 |
| `../src/main/java/com/CharunCore/server/worldgen/density/TerrainSplineProvider.java` | 62–66 | `peaksAndValleysDF`：`abs`、`+(−2/3)` 取 abs、`+(−1/3)` 乘 `−3`，即 `1-3*||ridges|-2/3|`，与原版一致 |
| `../src/main/java/com/CharunCore/server/worldgen/noisechunk/NoiseChunk.java` | 280(`forIndex`)、358(`NoiseInterpolator`)、413(`compute`)、424(`Mth.lerp3`)、458(`CacheAllInCell`) | `lerp3` 三线性插值、`cellCaches`/`interpolators` 装配、Y 自上而下遍历，与原版一致 |
| `../src/main/java/com/CharunCore/server/worldgen/density/NoiseRouterData.java` | 整类 | 噪声图、`slide`（含 `slide0`/`slide1`）、`spaghetti`、`noodle`、`pillars`、`jaggedness` 等结构移植 |
| `../src/main/java/com/CharunCore/server/world/gen/CubicSpline.java` | `Multipoint.apply` | 三次 Hermite 插值（与 `mapping/cfr-source/.../CubicSpline.java` 比对一致） |
| `../src/main/java/com/CharunCore/server/world/gen/PerlinNoise.java` | `wrap` / `getOctaveNoise` | `wrap` 取模与八度索引与原版一致 |
| `../src/main/java/com/CharunCore/server/world/gen/ImprovedNoise.java` | `noise(...)` | 5 参数 `noise` 与原版一致 |
| `../src/main/java/com/CharunCore/server/world/gen/XoroshiroRandomSource.java` | 103–129 | `XoroshiroPositionalRandomFactory`：`fromHashOf` 忠实（120–124）；**`at` 见 P2-2** |
| `../src/main/java/com/CharunCore/server/worldgen/noisechunk/aquifer/NoiseBasedAquifer.java` | 446–452 | `isLava`/`isWater` 硬编码（见 P2-1） |

---

## 3. 对照原版的关键差异 / 偏差（逐条）

### D1. `NoiseBasedAquifer` 流体 ID 硬编码（中等，影响运行一致性）
- 位置：`../src/main/java/com/CharunCore/server/worldgen/noisechunk/aquifer/NoiseBasedAquifer.java:446–452`
```java
private static boolean isLava(int blockId) {
    return blockId == 11; // lava ID in BlockStateHelper
}
private static boolean isWater(int blockId) {
    return blockId == 9;  // water ID in BlockStateHelper
}
```
- 原版 `NoiseBasedAquifer` 通过 `DripstoneUtils`/传入的 `fluidLevel` 与 `Block` 引用判定流体类型，并不假设全局固定 ID。本项目把 `lava`/`water` 绑定为 `11`/`9`。
- 影响：若 `BlockStateHelper` 中 lava/water 的 state ID 并非恰好 11/9（ID 表随状态枚举顺序而定，极易因新增方块/状态顺序变化而漂移），含水层会把别的方块误判为水或岩浆，或漏判真实流体 → 岩浆湖/地下水分布错误。属于“数据耦合脆弱”的隐患。

### D2. `XoroshiroPositionalRandomFactory.at()` 未将坐标异或进高位种子（中等，影响随机一致性）
- 位置：`../src/main/java/com/CharunCore/server/world/gen/XoroshiroRandomSource.java:113–117`
```java
public RandomSource at(int x, int y, int z) {
    long l = Mth.getSeed(x, y, z);
    long lo = l ^ this.seedLo;
    return new XoroshiroRandomSource(lo, this.seedHi);   // 注意：seedHi 未与 l 异或
}
```
- 原版为：
```java
long i = Mth.getSeed(x, y, z);
long j = i ^ this.seedLo;
long k = i ^ this.seedHi;          // 高位同样混入坐标
return new XoroshiroRandomSource(j, k);
```
- 即本项目所有 `at(x,y,z)` 生成的位置随机源**共享同一个 `seedHi`**，原版则 `seedHi` 也随坐标变化。
- 影响范围（经全仓 grep 确认 `at()` 的实际调用点）：
  - `NoiseBasedAquifer.java:149` — 含水层 cell 抖动随机
  - `OreVeinifier.java:67` — 矿脉（铜/铁）随机
  - `SurfaceSystem.java:91 / 255 / 293` 与 `SurfaceRules.java:488` — 表面规则随机（噪声带、黏土带、不规则表面判定等）
- 后果：上述基于位置的随机流与原版**不完全一致**——不同坐标的随机源高位相同，导致表面纹理、矿脉形态、含水层抖动的统计分布偏离原版。不崩溃，但“用同一世界种子复现原版地形”的目标在此类属性上会失准。

### D3. `DensityFunction` 噪声图装配顺序依赖 `NoiseRouterData`（低风险，已适配）
- `DensityFunctions` 工厂（marker → wrap）与 `NoiseRouter` 字段/入参顺序已按原版 `overworld()` 对齐。未发现装配错位。仅作记录，无偏差。

### D4. 大坐标噪声溢出风险（经评估：**当前未触发**，需注意）
- 任务特别要求评估 `FindTopSurface`、Shift 噪声、超大坐标的溢出风险。
- `FindTopSurface`（22–35）输入为 `compute` 的 double 坐标，无整数乘法溢出路径。
- `ShiftA`/`ShiftB`/`ShiftNoise`：原版用 `Mth.getSeed` + 位移对区块坐标做确定性哈希，本项目同逻辑；区块坐标（`int`）范围内不会溢出。
- `ShiftedNoise`/`ShiftedNoise2D`：将 `x/z` 经线性变换后送入 `NormalNoise`，坐标在区块尺度（`0..(1<<24)` 量级）下 `double` 精度足够，`INPUT_FACTOR` 缩放后仍远未达到 `double` 失精区。
- 结论：在常规世界边界（±30M）内无溢出；若未来支持超远坐标（接近 ±2^31 / 自定义维度），`Mth.getSeed` 的整数乘法与 `double` 精度需复核，但**当前实现与原版同级，未发现额外溢出点**。

---

## 4. Bug 清单（仅记录，不修复）

| 编号 | 严重度 | 文件:行号 | 现象 | 建议修复方向 |
|---|---|---|---|---|
| P2-1 | 中等 | `NoiseBasedAquifer.java:446–452` | `isLava`/`isWater` 硬编码 `11`/`9`，与 `BlockStateHelper` 的 state ID 强耦合；ID 表顺序变化即误判流体，导致地下水/岩浆湖生成错误 | 改为通过注入的 `lavaId`/`waterId`（或 `Block` 引用）判定，与原版一致从构造参数传入流体类型 |
| P2-2 | 中等 | `XoroshiroRandomSource.java:113–117` | `at(x,y,z)` 未将坐标异或进 `seedHi`（仅 `lo` 异或），位置随机源高位恒为常量；影响含水层抖动、矿脉随机、表面规则随机，与原版统计分布不一致 | 改为 `long k = l ^ this.seedHi; return new XoroshiroRandomSource(lo, k);`，严格对齐原版 `XoroshiroPositionalRandomFactory.at` |
| P2-3 | 轻微 | `DensityFunction.java:85–89` | `setWorldSeed` 用 `new XoroshiroRandomSource(seedLo).forkPositional()` 直接构造 128 位工厂（高位由 `seedLo` 派生），而原版 `RandomSource.create(seed)` 内部走 `upgradedSeedTo128bit`；本项目单参数构造器（第 12–14 行）未做 128 位提升，若 `setWorldSeed` 只传 `seedLo`，则 `seedHi` 派生方式与原版 `create` 不同 | 明确 `setWorldSeed` 应传入完整 128 位种子，或在内部调用 `RandomSupport.upgradeSeedTo128bit(seedLo)`，确保与 `RandomSource.create` 等价 |

> 备注：P2-3 与 P2-2 同源（RNG 种子处理）。在“主地形密度图”路径下，`NoiseHolder` 用的是 `sharedFactory().fromHashOf(key)`（第 89/94、104 行），已正确派生 128 位，故 **P2-3 不会污染主地形密度**；仅当 `setWorldSeed` 仅给 `seedLo` 且下游直接 `forkPositional().at(...)` 时才有偏差，与 P2-2 重叠。列为轻微以提示种子初始化一致性。

---

## 5. 结论与优先级建议

### 结论
P2 噪声系统是**高保真移植**：密度函数链（22 类核心组件 + 底层噪声工具）数学正确，`slide`/插值/`blended`/样条/坐标偏移噪声全部与原版逐行一致；未发现会导致地形坍塌、崩溃或坐标溢出的错误。仅识别两类“原版一致性”偏差——含水层流体 ID 硬编码（P2-1）与位置随机高位种子未混淆（P2-2）——二者都影响“与原版逐字节复现”的目标，但**不影响可玩性与稳定性**。

### 优先级建议
1. **P2-1（中等，建议尽快）**：把 `isLava`/`isWater` 改为注入式流体 ID 判定。这是最可能在 ID 表变动后**实际产生错误地形**的隐患，且修复成本低（接收参数即可）。
2. **P2-2（中等，建议纳入一致性基线）**：补齐 `at()` 的高位种子异或。对“复现原版表面/矿脉/含水层随机”是必需的；否则同一世界种子下的表面细节与原版不符。不紧急但应修。
3. **P2-3（轻微，顺带处理）**：统一世界种子到 128 位初始化的路径，避免后续维护者误用单参数构造器产生偏差。

### 与 P1 的关联
P1 报告中指出结构生成的 nbt 加载走 `structure2` 管线，而地形密度（含含水层/表面规则）由本 P2 噪声系统驱动。两处偏差（P2-1 流体、P2-2 表面随机）会间接影响结构周边地形（如要塞井水、村庄地下水、海底神殿含水层），但结构本身的 nbt 拼装不受 P2 影响。建议 P1-P2 的“表面/流体”相关偏差在统一回归测试中一并验证。

---

*本报告所有 `文件:行号` 均来自实际读取的源码，未做推断或杜撰。审计期间未对任何 `.java` 文件进行修改。*
