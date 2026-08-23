# 管理员命令手册

> 控制台直接输入（可省略 `/`），游戏内聊天框输入 `/命令`。
> 标注 [OP] 的命令需要 OP 权限（`ops.json`，`/op` 添加）。目标支持玩家名与选择器（如 `@a` `@p` `@e[type=...]`）。

## 服务器管理

| 命令 | 权限 | 说明 |
|---|---|---|
| `/stop` `/end` `/shutdown` | - | 关闭服务器（优雅保存） |
| `/list` | - | 在线玩家列表 |
| `/kick <玩家> [原因]` | OP | 踢出玩家 |
| `/op <玩家>` / `/deop <玩家>` | OP | 授予/移除管理员（权限等级 4） |
| `/tps` | - | TPS 与运行时长 |
| `/rules` `/motd` | - | 查看规则/MOTD |
| `/plugins` `/pl` | - | 已加载插件列表 |
| `/reloadplugins` `/rp` | - | 热重载全部插件 |
| `/dump` | OP | 线程/内存/网络诊断 |

## 世界与时间

| 命令 | 权限 | 说明 |
|---|---|---|
| `/time set <day\|night\|noon\|midnight\|ticks>` | OP | 设置时间 |
| `/time add <ticks>` / `/time query ...` | OP | 推进/查询时间 |
| `/weather clear\|rain\|thunder [秒]` | OP | 设置天气（触发 WeatherChangeEvent） |
| `/difficulty <peaceful\|easy\|normal\|hard>` | OP | 难度 |
| `/gamerule <规则> [值]` | OP | 游戏规则（keepInventory 等） |
| `/seed` | - | 世界种子 |
| `/world <玩家> <overworld\|nether\|end>` | OP | 跨维度传送 |
| `/setblock <x y z> <方块[状态]> [replace\|destroy\|keep] [维度]` | OP | 设置方块 |
| `/fill <x1 y1 z1 x2 y2 z2> <方块> [模式] [维度]` | OP | 填充区域 |
| `/clone <x1 y1 z1 x2 y2 z2> <x y z> [维度]` | OP | 复制区域 |

## 传送

| 命令 | 权限 | 说明 |
|---|---|---|
| `/tp <x y z [目标]>` / `/tp <目标>` / `/tp <源> <目的>` | OP | 坐标/玩家传送，支持 `~` 相对坐标 |
| `/tphere <玩家>` | OP | 把玩家传送到自己 |
| `/tpa <玩家>` | - | 请求传送到对方（60 秒有效） |
| `/tpahere <玩家>` | - | 请求对方传送到自己 |
| `/tpaccept` / `/tpdeny` | - | 接受/拒绝传送请求 |
| `/spawnpoint <玩家> [x y z]` | OP | 设置重生点 |
| `/home <玩家>` | OP | 回到床/重生点 |
| `/back <玩家>` | OP | 回到上次死亡位置 |
| `/top <玩家>` / `/bottom <玩家>`` | OP | 传到最高/最低实心面 |
| `/sethome` `/delhome` | - | 家管理 |

## 玩家管理

| 命令 | 权限 | 说明 |
|---|---|---|
| `/gamemode <survival\|creative\|adventure\|spector> [目标]` | OP | 游戏模式（触发 PlayerGameModeChangeEvent） |
| `/give <目标> <物品> [数量]` | OP | 发放物品 |
| `/clear <目标> [物品]` | OP | 清空背包 |
| `/enchant <玩家> <附魔> [等级]` | OP | 附魔手持物品 |
| `/effect give\|clear <玩家> <效果> [秒] [放大器]` | OP | 状态效果 |
| `/xp <数量> <玩家>` | OP | 经验 |
| `/heal <玩家>` / `/feed <玩家>` | OP | 治疗/喂饱 |
| `/kill <玩家>` / `/suicide <玩家>` | OP | 击杀 |
| `/fly <玩家> [on\|off]` / `/speed <玩家> <值>` | OP | 飞行/速度 |
| `/god <玩家>` | OP | 无敌 |
| `/repair <玩家>` / `/rename` / `/hat` | OP | 修装备/改名/戴帽子 |
| `/invsee <玩家>` / `/enderchest` / `/workbench` | OP | 远程查看容器 |
| `/summon <实体> <x y z\|玩家>` | OP | 生成实体 |
| `/msg <玩家> <消息>` / `/say` / `/me` | - | 私聊/广播 |
| `/ping <玩家>` / `/afk <玩家>` | - | 延迟/挂机 |

## 权限体系

- 内置权限等级：OP（等级 4，`ops.json`）与非 OP 两级。
- 管理命令集在 `NetworkHandler.ADMIN_COMMANDS` 中枚举，未列入的为全员可用。
- 插件命令支持自定义权限节点：`registerCommand(...)` 时 `CommandSender.hasPermission(node)` 校验（玩家发送者当前映射为 isOp）。

## 日志与审计

- 管理操作（op/deop/kick/gamemode 等）输出到控制台与 `logs/latest.log`。
- 玩家数据: `world/playerdata/<uuid>.dat`（原版 NBT 格式），进度: `world/advancements/<uuid>.json`。
