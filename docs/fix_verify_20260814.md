# MC 1.21.11 核心遗留修复验证报告（2026-08-14）

## 一、区块包 BlockEntity 数组（0x2C）

**问题**：`ChunkEncoder` 硬写 `writeVarInt(0)`，0x2C 包 block_entities 永远为空 → 箱子盖动画/告示牌/刷怪笼/末地水晶柱无法渲染。

**修复**：
- 从 Chunk 的 `getBlockEntities()` 发真实 BE 数组（VarInt 计数 + packedXZ + y + typeId + tag）
- 新增 `blockEntityTypeId()` 注册表映射（furnace=0, chest=1, trapped_chest=2, sign=7, barrel=27, lectern=30, jigsaw=32 … 按原版注册顺序）
- tag 用 `writeAnonymousNbt`（type+entries+TAG_End），**不能**用 writeNbt（尾部多写 0x00，BE 连续数组每项错位 1 字节）

**验证**：VerifyWithVanilla 15/15 PASS；VerifyBE（Bootstrap 完整注册表）leftover=0，解码器正确读出 `BE @5,70,3 CustomName=TestChest`，type 映射正确。

## 二、结构 data markers（末地船鞘翅/潜影贝）

**验证**：VerifyMarkerDirect 驱动真实 ship.nbt 模板 → 鞘翅展示框 @(14,65,15) facing=SOUTH、潜影贝 ×3、战利品箱子 ×2 全部生成。markers 收集=6（Sentry×3 + Chest×2 + Elytra×1）。

## 三、红石遗留（9 项）

| 项 | 修复 |
|---|---|
| 活塞缩回 | 普通活塞缩回**不**拉方块（仅粘性活塞拉） |
| 活塞顶障碍 | 前方不可推动障碍时**不伸头**（extendPiston 返回 boolean，失败不置 extended=true） |
| 红石火把弱充能 | 只强充能上方方块；侧面/下方仅红石线/中继器/比较器/观察者可收信号 |
| 漏斗吸掉落物 | 新增：吸上方 1 格范围 ItemEntity 入槽（0.6 格判定 + 堆叠合并 + 冷却） |
| 音符盒调音 | 右键 note=(note+1)%25 并发声（playNoteManual，维度感知） |
| 比较器延迟 | 1 tick（2 游戏刻）延迟，复用 scheduledUpdates 机制（"comparator" kind） |
| 观察者垂直放置 | facing 支持 up/down（点击下表面→朝上，上表面→朝下） |
| 红石灯 | 已实现（确认） |
| 红石线交叉 | 已实现（确认） |

## 四、末影龙遗留（6 项）

- **四水晶重生**：`use_item_on` 加 end_crystal 分支 → 生成 EndCrystalEntity + `onEndCrystalPlaced`（四柱集齐重生龙）
- **龙蛋瞬移**：右键 dragon_egg 瞬移到附近 5 格内安全位置（原版行为）
- **黑曜石平台**：5×5 → **33×33**（x: 84-116, z: -16-16，原版规格）
- **水晶重建修复**（关键 bug）：
  - 重建只在 `pendingCrystalRestore`（服务器重启恢复）时执行——玩家战斗中破坏的水晶原版不会重生，无条件重建会让龙永远被保护
  - `buildSpikes` 重建前 `crystalIds.clear()`——否则幽灵水晶累积，`hasLivingCrystals()` 恒 true，龙不可击杀

## 五、掉落表

- **ghast**：+music_disc_tears（原版 3 pool）
- **女巫**：按原版 1-3 次 rolls × 6 种材料（glowstone_dust/sugar/spider_eye/glass_bottle/gunpowder/stick）+ redstone 0-3

## 六、流体

- 内部级联调度加 **5 tick 延迟**（FLOW_DELAY_TICKS=5，原版随机 tick 语义）；玩家倒水 spreadSource 保持即时

## 七、下界群系多样性

- `computeColBiome` 按 chunk hash 分配 5 种下界群系：nether_wastes=34 / soul_sand_valley=49 / crimson_forest=7 / warped_forest=59 / basalt_deltas=2
- BiomeTagResolver 的 BIOME_NAMES 已验证**恰好等于注册表 id 顺序**（65 项 0 mismatch）
- nether_fossil（仅灵魂沙谷）现在能生成；fortress/bastion 在 is_nether tag（5 群系）内正常

## 最终回归

- **LateGameBot 27/27 全 PASS**：合成 8 项 → 熔炉烧炼 → 黑曜石传送门 → 下界往返 → 要塞定位 → 末影龙（10 水晶 + BossBar + 破坏水晶 10/10 + 部件攻击命中 + 击杀 + 终末诗 + 12000 经验 0→68 级）
- **SurvivalBot 13/13 PASS**；服务器 0 异常

## 测试要点（LateGameBot）

1. 末影龙在祭坛 (0,65,0)，玩家从黑曜石平台 (100,50,0) 距 100 > TRACK_RANGE=64 看不到 → 需 tp 主岛
2. bot 判定龙死用**终末诗 title 包**（0x70/0x6E/0x71）——removedEntities 会被 tp 进出 64 格误触发
3. 攻击龙：每次攻击前 tp 到 0x23 实时位置（近战 3.5 格 + 冷却衰减伤害 ~1.55/次，200 血临界需高命中）
4. 玩家数据残留（末地位置/背包）影响复跑 → 测试前回主世界 + clear
