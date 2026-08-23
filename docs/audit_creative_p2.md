# 审计 P2：创造/管理员 模式切换

> 范围：`PlayerData.gameMode` 存储与切换、F3+F4 客户端模式切换包（0x04）、`sendAbilitiesUpdate`、模式对玩法的影响（破坏/放置/无敌/飞行/耐久/饥饿）。
> 对照源：`mapping/remapped_server_1.21.11.jar.src/net/minecraft/world/level/GameType.java`、`net/minecraft/server/commands/GameModeCommand.java`、`net/minecraft/server/network/ServerGamePacketListenerImpl.java`（能力/伤害）、`json/1.21.11/protocol.json`。
> 审计铁律：所有行号来自实际阅读，未修改任何源码。

---

## 1. 实现状态概述

**状态：部分实现（核心切换可用，模式对玩法的约束不完整）。**

- 模式存储：`PlayerData.gameMode`（0=生存,1=创造,2=冒险,3=旁观）已实现（`PlayerData.java:25`）。
- F3+F4 切换：服务端处理 `change_game_mode` 0x04 包，更新 `gameMode`、`allowFlight`，下发 `game_state_change` 0x26（reason 3）、`player_info` 0x44（UPDATE_GAME_MODE）、`abilities` 0x3e（`:423-444`）。
- 能力同步：`sendAbilitiesUpdate`（`:2192-2203`）正确置位瞬时破坏(0x08)/可飞行(0x04)/无敌(0x01)。
- 模式对玩法影响**已实现**：创造/旁观免伤、创造免耐久、创造/旁观免饥饿、创造瞬破、生存落摔伤害、PvP 目标免疫、旁观不可攻击生物。
- **偏差/缺陷**：冒险/旁观模式可放置方块；创造模式放置船/刷怪蛋/桶会由服务端额外 `giveItem` 复制物品；创造左键攻击生物仍造成伤害；`/gamemode` 指令路径不同步飞行能力；旁观缺少隐形/穿墙等原版行为。

---

## 2. 关键文件与行号证据

| 模块 | 文件:行号 | 说明 |
|---|---|---|
| 模式字段 | `world/PlayerData.java:25` | `gameMode`（0/1/2/3） |
| F3+F4 切换处理 | `network/NetworkHandler.java:423`（`id==0x04` change_game_mode） | `allowFlight=(mode==1||mode==3)`（:427），`game_state_change` 0x26 reason3（:429），`player_info` 0x44（:431-439），`this.gameMode=mode`（:440），`sendAbilitiesUpdate()`（:441） |
| 能力同步 | `NetworkHandler.java:2192-2203` | 标志位：godMode→0x01、allowFlight→0x04、creative→0x08（instantBreak） |
| 破坏逻辑 | `NetworkHandler.java:578`（`id==0x28` player_action） | `status==2 \|\| (status==0 && gameMode==1)` → 破坏（创造瞬破，:590）；`status==0 && gameMode==0` → 起挖（:655）；冒险(2)/旁观(3) 不匹配任何破坏分支（正确不可破） |
| 放置逻辑 | `NetworkHandler.java:697`（`id==0x3F` use_item_on）起 | **整段无 `gameMode` 守卫**，冒险/旁观可放方块/船/蛋/桶 |
| 创造免伤 | `NetworkHandler.java:3808`（`damagePlayer`） | `if (gameMode==1 || gameMode==3) return;` |
| 创造免耐久 | `NetworkHandler.java:5667`（`damageHeldItem`） | `if (gameMode==1 ...) return;` |
| 创造/旁观免饥饿 | `NetworkHandler.java:3915`（`addExhaustion`）、`:3920`（`tickFoodSystem`） | `if (gameMode==1 || gameMode==3) return;` |
| 生存落摔 | `NetworkHandler.java:3703` | 仅 `gameMode==0` 计落摔伤害 |
| PvP 目标免疫 | `NetworkHandler.java:463` | `if (target.gameMode==1 || target.gameMode==3 ...) break;` |
| 旁观不可攻击生物 | `NetworkHandler.java:1200`（`attackMob`） | `if (this.gameMode==3) return;` |
| 创造攻击生物（偏差） | `NetworkHandler.java:1215` | `if (this.gameMode==1) damage=Math.max(damage, baseDamage);` —— 创造仍造成伤害且被加强 |

---

## 3. 对照原版的关键差异/偏差（逐条，附 文件:行号）

1. **冒险/旁观模式可放置方块（严重偏差）**
   原版：`GameType.ADVENTURE` 不能放置/破坏方块（除非手持物带 `CanPlaceOn`/`CanDestroy`）；`GameType.SPECTATOR` 完全不能与世界交互。本核 `use_item_on`（`:697-960`）从入口到各个分支（普通方块 `:947`、床 `:916`、门 `:931`、船 `:841`、刷怪蛋 `:863`、桶 `:753/772/807`）**没有任何 `gameMode` 判断**，冒险(2)与旁观(3)均能放置。对照原版 `ServerPlayer.gameMode` 在 `useItemOn`/place 路径的 `mayInteract`/`GameType` 检查。

2. **创造放置特殊物品时服务端额外发放物品（复制）**
   普通方块放置在 `gameMode!=0` 时不递减（正确，创造无限）。但船/刷怪蛋/桶走 `if (gameMode==0){递减} else { giveItem(...) }`（`NetworkHandler.java:759/778/793/813/849/871`）。创造模式客户端本就保持物品无限，服务端再 `giveItem` 等于**每次放置免费获得一个物品**（桶/船/刷怪蛋复制）。原版创造放置仅消耗客户端本地计数（不变），服务端不额外给予。

3. **创造左键攻击生物仍造成伤害**
   原版 `GameType.CREATIVE` 左键实体不造成伤害（玩家“穿过”实体）。本核 `attackMob` 仅拦截 `gameMode==3`（`:1200`），且 `:1215` 把创造伤害提升为至少 `baseDamage`，导致创造模式可击杀生物。对照原版 `Player attacking mobs in creative deals no damage`。

4. **`/gamemode` 指令路径不同步飞行能力（与 P1-3 同源）**
   F3+F4 路径正确设置 `allowFlight` 并 `sendAbilitiesUpdate`（`:427`、`:441`）；但 `handleCommand` 的 `/gamemode`（`:1331-1351`）与控制台 `/gamemode`（`ConsoleCommandHandler.java:123-145`）均**只发 `game_state_change`/`player_info`，不设置 `allowFlight`、不调用 `sendAbilitiesUpdate`**。后果：用指令切到创造后客户端未收到能力包，无法飞行（需再 F3+F4 或 `/fly`）。

5. **`change_game_mode` 服务端不校验权限**
   原版 F3+F4 仅在该玩家被允许时由客户端发起，且服务端对 `/gamemode` 有 `requires` 校验。本核 0x04 处理无条件接受任意模式（`:423-426` 仅 `mode<0||mode>3` 校验）。任意玩家可切到任意模式（安全/平衡问题）。

6. **旁观模式缺少原版行为**
   已做：免伤（`:3808`）、不可攻击生物（`:1200`）、可飞行（`:427`）。**未做**：对其他玩家隐形（仍可见）、穿墙/无碰撞（无碰撞逻辑变更）、禁止打开容器/交互（受 bug#1 影响仍可 place）。对照原版 `GameType.SPECTATOR` 的 `canHitBlocks=false`、无碰撞、不可交互。

7. **模式未持久化到存档（需进一步核实）**
   `PlayerData.gameMode` 已定义（`:25`），但本次审计未在 `PlayerData` 的 save/load 路径核实其读写（搜索范围限于指令/模式相关逻辑）。若登出未保存、重进未恢复，则模式切换在断线后丢失。建议后续核实 `WorldManager`/存档读写是否包含 `gameMode`。

---

## 4. Bug 清单（只记录不修）

| # | 严重程度 | 文件:行号 | 现象 | 建议修复方向 |
|---|---|---|---|---|
| P2-1 | 严重 | `NetworkHandler.java:697-960`（尤 :841/:863/:918/:935/:947） | 冒险/旁观模式可放置方块、船、刷怪蛋、液体桶；原版冒险不可放（除非 CanPlaceOn）、旁观完全不可交互 | 在 `use_item_on` 入口按 `gameMode` 拦截：spectator 直接 return；adventure 仅放行手持物带 `CanPlaceOn` 的目标面（需解析组件，当前组件未存，可先一律拦截） |
| P2-2 | 中等 | `NetworkHandler.java:759/778/793/813/849/871` | 创造模式放置船/刷怪蛋/桶时服务端 `giveItem` 额外赠送，造成物品复制 | 创造模式不调用 `giveItem`，仅正常放置（客户端保持无限）；或统一为 `gameMode==0` 递减、否则不增不减 |
| P2-3 | 中等 | `NetworkHandler.java:1200/1215` | 创造模式左键攻击生物仍造成伤害且被加强 | `attackMob` 中 `if (gameMode==1 \|\| gameMode==3) return;`（创造无敌穿透），移除 `:1215` 的伤害加强 |
| P2-4 | 中等 | `NetworkHandler.java:1331-1351`、`ConsoleCommandHandler.java:123-145` | `/gamemode` 指令路径不设置 `allowFlight`、不 `sendAbilitiesUpdate`，切到创造无法飞行 | 两条路径均 `allowFlight=(mode==1||mode==3)` + `sendAbilitiesUpdate()`，与 F3+F4 路径一致 |
| P2-5 | 中等（安全） | `NetworkHandler.java:423-426` | 任意玩家可 F3+F4 切到任意模式，无权限校验 | 对 `change_game_mode` 增加权限/白名单校验（与 P1-2 的权限系统联动） |
| P2-6 | 中等 | 多处（无隐形/无碰撞/可交互） | 旁观模式缺少隐形、穿墙、禁止交互等原版行为 | 旁观：对其他玩家不广播实体外观/位置或标记不可见；移动无碰撞；`use_item_on`/`player_action` 入口 return |
| P2-7 | 轻微 | `PlayerData.java:25` | `gameMode` 是否在存档读写未核实，可能断线丢失 | 核实并在 `PlayerData` 序列化中读写 `gameMode` |

---

## 5. 结论与优先级建议

- **最高优先级（玩法正确性/平衡）**：P2-1（冒险/旁观可放置）——直接破坏原版模式契约，旁观模式形同虚设；P2-2（创造复制物品）——经济/物品复制漏洞。
- **次高优先级**：P2-3（创造攻击生物）、P2-4（指令切模式不同步飞行，影响管理员日常）、P2-5（切模式无权限）。
- **长期**：P2-6（旁观完整行为）、P2-7（模式持久化）。

> 正面：F3+F4 切换、`abilities` 同步、创造免伤/免耐久/免饥饿/瞬破、生存落摔、PvP 目标免疫、旁观不可攻击生物均已正确实现，基础切换链路可用。
