# 审计 P1：创造/管理员 指令系统

> 范围：控制台指令（`ConsoleCommandHandler.java`）+ 玩家指令（`NetworkHandler.handleCommand`）+ Tab 补全（`getTabCompletions`）+ 客户端 Brigadier 命令树（`sendCommandsPacket`）+ @ 目标选择器。
> 对照源：`json/1.21.11/protocol.json`、`mapping/remapped_server_1.21.11.jar.src/net/minecraft/commands/`、`net/minecraft/server/commands/`、`net/minecraft/commands/CommandSourceStack.java`。
> 审计铁律：本报告所有行号均来自实际阅读，未修改任何源码。

---

## 1. 实现状态概述

**状态：部分实现。**

- 已实现一组覆盖常用玩法的自定义指令（约 50+ 条），玩家版与控制台版各一套 `switch` 分发。
- 已实现**两套** Tab 补全机制：服务端 `getTabCompletions`（响应客户端 `tab_complete` 0x0E）+ 登录时下发客户端 Brigadier 命令树（`sendCommandsPacket`，`declare_commands` 0x10），用于客户端原生补全/语法高亮。
- **缺失**：原版 `@a/@p/@r/@e/@s` 目标选择器完全未实现；无 `CommandSourceStack`；无权限等级/OP 持久化；指令执行为纯自定义 `switch`，与原版 Brigadier 执行树架构不兼容。
- 多数常用指令（gamemode/give/teleport/tp/difficulty/time/setblock/fill/clone/gamerule/kill/weather/spawnpoint/op/deop 等）均有对应实现，但参数语法与原版存在差异。

---

## 2. 关键文件与行号证据

| 模块 | 文件:行号 | 说明 |
|---|---|---|
| 控制台指令分发 | `console/ConsoleCommandHandler.java:51`（`execute`），`switch` 起 `:57` | 全部控制台指令的 `switch` 实现（约 855 行） |
| 玩家指令分发 | `network/NetworkHandler.java:1264`（`handleCommand`） | 玩家 `/指令` 的 `switch` 实现 |
| 玩家指令入口 | `NetworkHandler.java:412`（`id==0x06` Chat Command） | 读取命令字符串后调用 `handleCommand` |
| Tab 补全（服务端） | `NetworkHandler.java:527`（`id==0x0E` tab_complete）、`:1962`（`getTabCompletions`）、`:1953`（ALL_COMMANDS 列表） | 服务端补全逻辑 |
| 客户端命令树 | `NetworkHandler.java:5356`（`sendCommandsPacket`）、`:5489`（`sendPacket(ctx, 0x10, ...)`） | 下发 `declare_commands`（客户端包 ID 经 protocol.json 核对为 0x10，注释里的 0x11 已过时但代码正确） |
| 解析助手 | `NetworkHandler.java:1947`（`parseCoord`）、`:2100`（`difficultyName`）、`:2147`（`parseTimeValue`）、`:2161`（`parseGameMode`）、`:2178`（`teleportPlayer`） | 坐标/难度/时间/模式/传送解析 |
| 目标解析 | `ConsoleCommandHandler.java:31`（`player(name)`）、`NetworkHandler.java:1319`/`:1715`/`:1750`（在线玩家精确名匹配） | 仅按精确用户名匹配在线玩家 |
| OP 指令 | `ConsoleCommandHandler.java:595`（op）、`:599`（deop）、`NetworkHandler.java:1724`（op）、`:1731`（deop） | 仅 `broadcastSystemMessage`，无实际权限授予 |

**已实现的指令清单（控制台版 `ConsoleCommandHandler.java`）：**
`stop/end/shutdown`、`time`、`tp`、`gamemode`、`locate`、`give`、`clear`、`kill`、`heal`、`feed`、`weather`、`summon`、`xp`/`experience`、`enchant`、`effect`、`seed`、`difficulty`、`setblock`、`fill`、`clone`、`spawnpoint`、`home`、`sethome`、`delhome`、`rename`、`tphere`、`tpa`、`tpaccept`、`tpdeny`、`back`、`gamerule`、`kick`、`op`、`deop`、`list`、`msg`/`tell`/`w`、`say`/`broadcast`、`me`、`help`、`world`、`fly`、`speed`、`god`、`top`、`bottom`、`suicide`、`ping`、`motd`、`rules`、`afk`、`repair`、`hat`、`enderchest`/`ec`、`workbench`/`craft`、`invsee`。

**玩家版（`NetworkHandler.handleCommand`）覆盖（子集）：** `time`、`tp`、`gamemode`、`locate`、`give`、`clear`、`kill`、`heal`、`feed`、`weather`、`summon`、`xp`/`experience`、`enchant`、`effect`、`seed`、`difficulty`、`setblock`、`fill`、`clone`、`spawnpoint`、`home`、`back`、`gamerule`、`kick`、`op`、`deop`、`list`、`msg`/`tell`/`w`、`say`、`me`、`help`、`world`、`fly`、`speed`、`god`、`top`、`bottom`、`suicide`、`ping`、`motd`、`rules`、`afk`、`repair`、`hat`、`invsee`、`enderchest`、`workbench`。

---

## 3. 对照原版的关键差异/偏差（逐条，附 文件:行号）

1. **无 @ 目标选择器（最关键的架构性缺失）**
   `ConsoleCommandHandler.java` 与目标解析全部为精确用户名匹配（`ConsoleCommandHandler.java:31`、`NetworkHandler.java:1319/1715/1750`）。全仓 grep `@a/@p/@s/@e/@r`、`EntitySelector`、`CommandSourceStack` 均无结果（仅在 `worldgen/noisechunk/NoiseChunk.java` 的 Javadoc `@param` 中出现 `@`，与指令无关）。原版 `Commands` 注册树 + `CommandSourceStack` + `EntitySelector` 提供 `@a/@p/@r/@e/@s` 选择，本核完全缺失。

2. **`/gamemode` 参数顺序与原版相反（仅控制台版）**
   原版：`/gamemode <mode> [targets]`。本核控制台版：`/gamemode <玩家> <模式>`（`ConsoleCommandHandler.java:123-145`）。玩家版 `/gamemode <模式>` 只作用于自己（`NetworkHandler.java:1331-1351`），与原版“未指定目标时作用于执行者（需权限）”一致，但缺少“可指定目标”能力。

3. **无权限系统 / OP 仅为广播**
   原版 OP 写入 `ops.json` 并驱动 4 级权限（`Commands` 的 `requires`）。本核 `op`/`deop` 只发一条系统消息（`ConsoleCommandHandler.java:595-602`、`NetworkHandler.java:1724-1734`），任何玩家默认可执行全部指令，无权限等级、无持久化。

4. **`/tp` 不支持实体目标、朝向、相对坐标受限**
   玩家版 `/tp`：4 段走 `x y z`（`Double.parseDouble`，`NetworkHandler.java:1311-1313`，**不支持 `~`**），2 段走 `<玩家>`（`:1317-1324`）；控制台版 `/tp` 也不支持 `~`（`ConsoleCommandHandler.java:99-120`）。仅 `/summon` 支持 `~`（`NetworkHandler.java:1508-1510`）。原版 `/tp` 支持实体/玩家选择器、`facing`、相对坐标。

5. **`/give` 不支持目标选择器 / NBT / 数量上限**
   `NetworkHandler.java:1383-1392`、`ConsoleCommandHandler.java:174-185`：`amount` 未校验上界（可能 > 64/堆叠上限），无 components/NBT，目标只能是自己（玩家版）或精确名（控制台版）。原版 `/give <targets> <item> [count] [components]`。

6. **`/setblock` / `/fill` 不支持方块状态/NBT，模式语义不全**
   玩家版 `/setblock` 仅识别 `keep`/`replace` 文本且 `destroy` 未真正实现破坏（`NetworkHandler.java:1599-1616`）；`/fill` 仅 `replace` 一种（`NetworkHandler.java:1619-1642`）。原版支持 `replace <filter>`、`destroy`、`keep` 以及方块 `[]` 状态/组件。控制台版支持 `destroy|keep|replace` 关键字但同样不解析方块状态（`ConsoleCommandHandler.java:388-437`）。

7. **客户端命令树与实际执行脱节**
   `sendCommandsPacket`（`NetworkHandler.java:5356-5513`）向客户端声明 `time/tp/gamemode/weather/difficulty/world/fly/god/effect/speed/locate` 等节点（仅用于补全），但实际执行走 `handleCommand` 的 `switch`，二者无共享注册表。新增指令需手动同步两处，否则客户端补全与实际可用不一致（如 `getTabCompletions` 不识别 `gm` 别名，而命令树声明了 `gm`：`NetworkHandler.java:5392` vs `:1983`）。

---

## 4. Bug 清单（只记录不修）

| # | 严重程度 | 文件:行号 | 现象 | 建议修复方向 |
|---|---|---|---|---|
| P1-1 | 严重 | `ConsoleCommandHandler.java:31/57`、`NetworkHandler.java:1264/1319` | 完全缺失 `@a/@p/@r/@e/@s` 目标选择器，所有指令只能精确匹配在线玩家名；无法做群体/条件选择 | 实现 `EntitySelector` 解析层，指令目标统一经选择器解析（参考原版 `CommandSourceStack.getOnlinePlayers()` + 选择器） |
| P1-2 | 严重（安全） | `ConsoleCommandHandler.java:595-602`、`NetworkHandler.java:1724-1734` | `op`/`deop` 仅广播消息，无任何权限授予/持久化；任意玩家可执行任意指令 | 引入权限等级与 `ops.json`（或等价持久化），在 `handleCommand` 入口做 `requires` 校验 |
| P1-3 | 中等 | `NetworkHandler.java:1331-1351`、`ConsoleCommandHandler.java:123-145` | `/gamemode` 指令路径**不设置 `allowFlight`，也不调用 `sendAbilitiesUpdate`**；与 F3+F4 路径（`:427`、`:441`）不一致 → 用指令切到创造模式后无法飞行，需再按 F3+F4 或 `/fly` | 在 `/gamemode` 两条路径均设置 `allowFlight = (mode==1||mode==3)` 并 `sendAbilitiesUpdate()`（见 P2-4） |
| P1-4 | 中等 | `ConsoleCommandHandler.java:123-145` | 控制台 `/gamemode <玩家> <模式>` 参数顺序与原版 `<模式> [目标]` 相反，老玩家易用错 | 对齐为 `/gamemode <模式> [玩家]` |
| P1-5 | 中等 | `NetworkHandler.java:1308-1328`、`ConsoleCommandHandler.java:99-120` | `/tp` 不支持 `~` 相对坐标（除 `/summon`）、不支持实体目标与 `facing` | 复用 `parseCoord` 支持 `~`；目标支持选择器/实体 |
| P1-6 | 中等 | `NetworkHandler.java:1383-1392`、`ConsoleCommandHandler.java:174-185` | `/give` 无数量上界校验、无 components、无目标选择器 | 校验 `amount<=maxStackSize`，目标走选择器，可选 components |
| P1-7 | 中等 | `NetworkHandler.java:1599-1642`、`ConsoleCommandHandler.java:388-437` | `/setblock`/`/fill` 不解析方块状态/组件，`fill` 仅 `replace`，`destroy` 未实现真正破坏 | 解析方块 `[]` 状态；`fill` 增加 `replace <filter>`/`destroy`/`keep` |
| P1-8 | 中等 | `NetworkHandler.java:1962-2098` | `getTabCompletions` 不补全 `@` 选择器、不补全坐标数字、对 `/give` 数量、`/gamerule` 值等覆盖有限；且与命令树不完全一致（缺 `gm` 别名） | 补全目标选择器、坐标、并为命令树与补全共用同一份指令元数据 |
| P1-9 | 轻微 | `NetworkHandler.java:1331-1351` | 玩家版 `/gamemode` 无目标参数（原版玩家可指定目标，需权限） | 增加可选目标参数（受权限约束） |
| P1-10 | 轻微（架构） | `NetworkHandler.java:5356` vs `:1264` | 指令执行（自定义 switch）与客户端 Brigadier 树无共享注册，长期维护易漂移 | 抽离统一指令注册表，执行与补全共用 |

---

## 5. 结论与优先级建议

- **最高优先级（阻塞“管理员系统”可用性）**：P1-1（@ 选择器）、P1-2（OP/权限系统）。没有选择器与权限，本核的“管理员/创造指令系统”与原版体验差距最大，且存在任意玩家可执行管理指令的安全风险。
- **次高优先级**：P1-3（指令切模式不同步飞行）、P1-5/P1-6/P1-7（主要指令参数语法与原版偏差，影响脚本/数据驱动玩法与肌肉记忆）。
- **维护性**：P1-8/P1-10（补全与执行统一）建议作为长期重构，避免两套指令定义漂移。

> 说明：客户端 `declare_commands`（0x10）已正确下发，普通玩家在客户端能看到 `/time`、`/tp`、`/gamemode` 等补全与语法提示；但执行正确性、权限、选择器仍是主要短板。
