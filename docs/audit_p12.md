# P12 成就系统（Advancements）审计报告

审计对象：`com.yanrong.server` 自研核心（MC 1.21.11 / Protocol 774，Java 21 + Netty）
对照源：
- 原版 `net.minecraft.advancements.*`（Advancement / AdvancementProgress / CriterionTrigger / AdvancementHolder）
- 协议包规范 `json/1.21.11/protocol.json`（Clientbound `advancements` = 0x80）、`json/1.21.11/effects.json`（toast 音效/帧类型）

> 说明：本审计所有结论基于实际读到的源码；未改动任何 `.java` 源码。

---

## 1. 实现状态概述

**状态：未实现 / 缺失（0% 实现）。**

经全量搜索 `src/` 目录（关键字 `Advancement|advancement|criteria|progress|toast`、`0x80`、`advancements(`），结论如下：

- 全代码库**不存在**任何 Advancement 数据结构（无 `Advancement`、`AdvancementProgress`、`Criterion`、`CriterionTrigger` 的等价实现）。
- 服务端**从未构造或发送** `ClientboundUpdateAdvancementsPacket`（协议 id `0x80`，`protocol.json:10110` `"0x80": "advancements"`）。搜索 `0x80` 在 `src/` 中仅命中与成就无关的位掩码（`NetworkHandler.java:5064`、`5314`），无任何成就包发送。
- 唯一与 “advancement” 相关的字符串是 gamerule 名 `announceAdvancements`：
  - `NetworkHandler.java:2052`（仅用于 `/gamerule` 命令的 Tab 补全列表）
  - `WorldInitializer.java:116`（仅作为默认 gamerule 键值 `"announceAdvancements" -> "true"` 写入）
  - 该 gamerule 仅是字符常量，**没有任何读取/生效逻辑**，与成就授予/弹窗毫无关联。
- 不存在 `criteria`、`progress`（成就进度）、`toast`（成就弹窗）的任意实现或协议下发。

因此：玩家在该核心中**不会获得任何成就、不会看到任何 toast 弹窗、进度界面为空**。这是功能性缺失，而非可局部修补的 bug。

---

## 2. 关键文件与行号证据

| 检索目标 | 结果 | 证据 |
|---|---|---|
| `Advancement` / `advancement` 类或引用 | 无实现 | `src/` 全量 grep：仅 `NetworkHandler.java:2052`、`WorldInitializer.java:116`（gamerule 字符串） |
| `criteria` / `progress`（成就进度） | 无 | `src/` 全量 grep：无匹配 |
| `toast`（成就弹窗） | 无 | `src/` 全量 grep：无匹配 |
| `0x80` 成就包发送 | 无 | `src/` 中 `0x80` 仅作位掩码（`5064`、`5314`），无 `sendPacket(ctx, 0x80, ...)` |
| 客户端成就包接收处理 | 无（核心为服务端，无需收） | — |

> 注：`EndDragonFight.java`、`WorldInitializer.java` 在宽匹配 `(Advancement|advancement|criteria|progress)` 中被命中，但经核实命中词为 `progress`（普通变量名，如进度条/装载进度）与 `announceAdvancements` 字符串，均**非**成就系统。

---

## 3. 对照原版的关键差异 / 偏差

原版 1.21.11 成就系统由以下部分组成（作为后续实现的对标基线）：

### 3.1 数据结构（原版）
- `Advancement`：含 `parentId`（可选）、`DisplayInfo`（title/description/icon/frameType/flags 的 NBT）、`Criteria`（命名条件集合）、`Requirements`（条件组合逻辑）。
- `AdvancementProgress`：每个 criterion 的 `CriterionProgress`（获得时间），以及整体 `done`/`reachable`。
- 服务端在玩家登录、条件达成时通过 `ClientboundUpdateAdvancementsPacket` 下发“新增/更新/移除”的 advancement 与对应 progress。

### 3.2 协议包结构（权威：`json/1.21.11/protocol.json:9408` `packet_advancements`，id `0x80`）
```
bool   reset                              // 是否清空客户端已有进度
array  advancementMapping:
    string key                            // 如 "minecraft:story/mine_stone"
    container value:
        option<string> parentId
        option<container> displayData:
            anonymousNbt title
            anonymousNbt description
            Slot icon
            varint frameType             // 0=task,1=goal,2=challenge
            bitfield flags               // backgroundTexture / showToast / hidden
        ... criteria / requirements
array  progressMapping:
    string key
    container:
        array criteria: (string name, optional<int64> time)
array  removed: string keys
```
当某 advancement 的 progress 在包中为 `done` 且 `displayData.flags.showToast=true` 时，客户端弹出 **toast** 通知（`effects.json` 提供对应音效/帧外观）。

### 3.3 进度触发（原版 CriterionTrigger 机制）
- 条件通过 `CriterionTrigger` 在游戏事件发生时检测：如 `minecraft:inventory_changed`（物品变化）、`minecraft:player_killed_entity` / `minecraft:entity_killed_player`（击杀）、`minecraft:brewed_potion`（酿造药水）、`minecraft:recipe_unlocked` / `minecraft:recipe_crafted`（合成）、`minecraft:consume_item`（进食）、`minecraft:location` / `minecraft:player_touch_color`（到达/进入维度）、`minecraft:tick` 等。
- 原版触发点贯穿 `ServerPlayer`、各 `LivingEntity`、合成/酿造/方块交互逻辑。本核心的 `NetworkHandler.java` 与 `world/*` 中**没有任何一处**向成就系统回传事件。

### 3.4 关键缺失对照
| 原版能力 | 本核心状态 |
|---|---|
| 成就数据加载（数据包/硬编码树） | 缺失 |
| 进度追踪（AdvancementProgress / criteria） | 缺失 |
| 事件触发（击杀/采矿/酿造/合成/进食/到达维度） | 缺失，游戏事件未向成就层暴露 |
| `0x80` 下发新增/更新/移除 | 缺失 |
| toast 弹窗（showToast + frameType + 音效） | 缺失 |
| 成就面板（Advancements 界面数据） | 缺失（客户端无数据可显示） |

---

## 4. Bug 清单

本子系统**无“代码 bug”可列**（因为没有相关代码）。审计结论为**功能性缺失（缺失项）**，按缺失项登记，不评级“严重/中等/轻微”——其影响等价于“整块系统未实现”。

> 若项目曾参考过旧审计文档称“成就已实现/部分实现”，则旧文档已过时：当前 `src/` 中确无任何成就实现痕迹（已全量 grep 核实）。

---

## 5. 结论与后续实现参考（优先级建议）

**结论**：P12 成就系统完全未实现。它不是“有 bug 需要修”，而是需要从零建设。建议优先级：**中低（P2）** —— 对纯生存玩法（血量/饥饿/战斗/死亡/经验，即 P3）无阻塞，但影响完整体验、成就党玩法与原版一致性。

### 5.1 建议的最小实现路线
1. **数据层**：用 1.21.11 官方数据包的 `advancements/*.json` 作为权威源，解析为内存 `Advancement` 树（parent/display/criteria/requirements）。建议直接读取/内嵌官方数据包而非手写。
2. **进度层**：实现 `AdvancementProgress`（每 criterion 获得时间、整体 done），支持按 `Requirements` 逻辑判定完成。
3. **触发层**：在现有游戏事件点挂接 `CriterionTrigger` 回调——
   - 击杀：`NetworkHandler.attackMob`（`1194`）击杀分支 / `MobEntity` AI 击杀；
   - 采矿：`handleBlockInteraction` 破坏方块分支（约 `6111`）；
   - 酿造：`world/BrewingSystem.java`；
   - 合成：`world/CraftingSystem.java` / `handleCraftingResultClick`（`3386`）；
   - 进食：`NetworkHandler` 进食分支（`1027`）；
   - 进入维度：`teleportToDimension`（`4632`）/ 复活点设置。
4. **协议层**：实现 `sendPacket(ctx, 0x80, ...)` 按 `protocol.json:9408` 结构编码 `reset / advancementMapping / progressMapping / removed`，在玩家登录与每次 progress 变更时下发；对 `showToast` 的 advancement 触发客户端 toast。
5. **持久化**：将已授予成就 key 集合存入 `PlayerData`（当前 `PlayerData.java` 无该字段，需新增）。

### 5.2 原版 1.21.11 关键成就类别（实现时的树结构参考）
（具体每个 advancement 的 key / criteria / parent 以官方数据包为准，这里只列大类与代表项，避免凭记忆编造精确路径）

- **minecraft:story（下界前主线）**：root（采集木头）、mine_stone、upgrade_tools、smelt_iron、obtain_armor、lava_bucket、iron_tools、deflect_arrow、enter_the_nether 等。
- **minecraft:nether（下界）**：root（进入下界）、return_to_sender、fast_travel、subspace_bubble、obtain_ancient_debris、netherite_armor、brew_potion、loot_bastion、distract_piglin 等。
- **minecraft:end（末地）**：root（进入末地）、kill_dragon、free_the_end、the_next_generation、remote_gateway 等。
- **minecraft:adventure（冒险）**：root、voluntary_exile（袭击）、hero_of_the_village、trade、sleep_in_bed、totem_of_undying、two_birds_one_arrow、whos_the_pilot、sniper_duel、very_very_frightening、monsters_hunted 等。
- **minecraft:husbandry（农牧）**：root、plant_seed、breed_an_animal、tame_an_animal、fishy_business、balanced_diet、bred_all_animals、complete_catalogue、enchanted_golden_apple、a_complete_catalogue? 等。
- **minecraft:recipe（合成）**：大量 `recipe/<配方名>` 子项（与 `CraftingSystem` 联动）。

> 实现时强烈建议：直接内嵌官方 1.21.11 数据包的 `advancements/` 目录（JSON），复用其 key 与 criteria 定义，避免手工重建导致与原版不一致；触发层用“事件名 → 官方 criteria trigger 类型”映射即可。

---

**交付物**：本报告 `docs/audit_p12.md` 确认 P12 为未实现/缺失，并给出协议包结构（`0x80`）、触发机制与 1.21.11 成就大类作为后续实现基线。未修改任何源码。
