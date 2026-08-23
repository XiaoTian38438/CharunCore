# CharunCore MC Server 插件 API 指南

> 版本: 1.21.11 (Protocol 774) · 包 `com.CharunCore.server.plugin`（对标 Paper/Bukkit 核心能力）

## 快速开始

一个插件 = 一个 JAR：`plugin.yml`(必需) + `Plugin` 子类(必需) + `config.yml`(可选)。

**plugin.yml**

```yaml
name: MyPlugin
version: 1.0.0
main: com.example.MyPlugin
api-version: '1.0'
author: YourName
depend: [OtherPlugin]        # 硬依赖
softdepend: [SoftPlugin]     # 软依赖(仅排序)
permissions:
  myplugin.fly:
    description: 允许飞行
    default: op              # everyone | op | nobody
commands:
  fly:
    description: 切换飞行
    aliases: [f]
```

**最小插件**

```java
package com.example;

import com.CharunCore.server.plugin.Plugin;
import com.CharunCore.server.plugin.Server;
import com.CharunCore.server.plugin.api.BossBar;
import com.CharunCore.server.plugin.api.PermissionManager;
import com.CharunCore.server.plugin.api.Player;
import com.CharunCore.server.plugin.api.World;
import com.CharunCore.server.plugin.event.EventHandler;
import com.CharunCore.server.plugin.event.EventListener;
import com.CharunCore.server.plugin.event.EventPriority;
import com.CharunCore.server.plugin.event.events.BlockBreakEvent;
import com.CharunCore.server.plugin.event.events.EntityDamageByEntityEvent;
import com.CharunCore.server.plugin.event.events.PlayerJoinEvent;

public class MyPlugin extends Plugin implements EventListener {

    private BossBar bossBar;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerEvents(this);

        // 权限注册 (也可在 plugin.yml 声明)
        PermissionManager.register("myplugin.fly", PermissionManager.Default.OP);

        // 命令 + Tab 补全
        registerCommand("fly", "切换飞行", (sender, label, args) -> {
            if (!sender.isPlayer()) return true;
            if (!sender.hasPermission("myplugin.fly")) {
                sender.sendMessage("§c无权限");
                return true;
            }
            Player p = (Player) sender;
            p.getHandle().allowFlight = !p.getHandle().allowFlight;
            p.sendMessage(p.getHandle().allowFlight ? "§a飞行开启" : "§c飞行关闭");
            return true;
        }, "f");

        // 调度器
        getScheduler().runTaskTimer(ServerScheduler.ref(this), () -> {
            int online = Server.get().getOnlineCount();
            // 每 5 秒更新 BossBar
        }, 100L, 100L);

        // BossBar
        bossBar = Server.get().createBossBar("§b在线: 0", BossBar.Color.BLUE, BossBar.Style.NOTCHED_10, 1.0f);
    }

    @Override
    public void onDisable() {
        if (bossBar != null) bossBar.removeAll();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player p = Player.wrap(e.getPlayer());
        p.sendTitle("§6欢迎", "§7" + p.getName(), 10, 60, 10);
        p.sendActionBar("§a你好!");
        p.playSound("minecraft:entity.player.levelup", 1.0f, 1.0f);
        p.spawnParticle("happy_villager", p.getX(), p.getY() + 1, p.getZ(), 10, 0.5, 0.5, 0.5, 0.0);
        if (bossBar != null) bossBar.addPlayer(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        World world = World.overworld();
        world.dropItem(e.getX(), e.getY(), e.getZ(), "diamond", 1); // 每次破坏掉钻石(示例)
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player p && p.getItemInMainHand().name().equals("diamond_sword")) {
            e.setAmount(e.getAmount() * 2);
        }
    }
}
```

编译打包：

```
javac -cp "target/classes" MyPlugin.java
jar cf plugins/MyPlugin.jar com/ plugin.yml config.yml
```

`/reloadplugins`(控制台) 热重载；`/plugins` 查看列表。

## 核心 API 一览

| 类 | 说明 |
|---|---|
| `Server.get()` | 单例门面: 插件管理/事件/调度/在线玩家/广播/时间天气/BossBar 工厂/dispatchCommand |
| `Plugin` | 基类: onEnable/onDisable/getLogger/getConfig/registerEvents/registerCommand/getScheduler |
| `plugin.api.Player` | 玩家门面: 消息/标题/动作栏/音效/粒子/传送/血量/模式/背包槽/给物品/踢出/权限 |
| `plugin.api.ItemStack` | (itemId, count) 记录 + name/maxStackSize/of() |
| `plugin.api.World` | 方块读写(带属性)/掉落物/范围实体查询, overworld() 或 World.of(dim) |
| `plugin.api.BossBar` | 标题/颜色/样式/进度, addPlayer/removePlayer 实时同步 |
| `plugin.api.PermissionManager` | register(node, everyone/op/nobody) + setPermission 附件 |
| `EventManager` | 事件总线(优先级/取消/注销) |
| `ServerScheduler` | 同步/异步/延迟/周期任务, 插件卸载自动失效 |

## Player API (api.plugin.com.CharunCore.server.Player)

- 消息: `sendMessage(msg[, color])`
- 标题: `sendTitle(title, subtitle, fadeIn, stay, fadeOut)`; 动作栏: `sendActionBar(msg)`
- 表现: `playSound(soundName, volume, pitch)` · `spawnParticle(particleName, x,y,z, count, offX,offY,offZ, speed)`
- 位置: `getX/Y/Z/getYaw/getPitch` · `getWorld()` · `teleport(x,y,z | Player)` · `setVelocity`
- 状态: `getHealth/setHealth` · `getGameMode/setGameMode` · `getFoodLevel/setFoodLevel` · `getExpLevel` · `isOnline` · `getOpLevel` · `kick(reason)`
- 物品: `getItemInMainHand/setItemInMainHand` · `getInventorySlot/setInventorySlot(0-45)` · `giveItem(name, count)`
- 权限: `hasPermission(node)`; `getHandle()` 取底层 NetworkHandler(高级用法)

## 事件总表 (com.CharunCore.server.plugin.event.events)

| 事件 | 触发点 | 可取消/可变 |
|---|---|---|
| PlayerJoinEvent / PlayerQuitEvent | 进出服 | 消息可改 |
| PlayerChatEvent | 聊天 | 可取消 |
| PlayerCommandPreprocessEvent | 内置命令前 | 可取消 |
| PlayerMoveEvent | 移动 | 可取消(回弹)/目标可改 |
| PlayerTeleportEvent | 任何传送 | 可取消/目标可改 |
| PlayerInteractEvent | 点击方块 | 可取消 |
| BlockBreakEvent / BlockPlaceEvent | 破坏/放置 | 可取消, dropItems |
| PlayerDeathEvent / PlayerRespawnEvent | 死亡/重生 | 消息/keepInventory/复活点 |
| PlayerGameModeChangeEvent | 模式切换 | 可取消 |
| PlayerToggleSneakEvent / PlayerToggleSprintEvent | 潜行/疾跑 | — |
| PlayerDropItemEvent | 丢物品 | 可取消 |
| PlayerItemHeldEvent | 切换快捷栏 | 可取消 |
| PlayerKickEvent | 被踢出 | 可取消/原因可改 |
| PlayerExpChangeEvent | 经验变化 | 数量可改 |
| EntityDamageEvent / EntityDamageByEntityEvent | 实体受伤 | 可取消/数值可改 |
| EntityDeathEvent* | 实体死亡 | 掉落经验可改 |
| InventoryClickEvent / InventoryCloseEvent | 容器点击/关闭 | 点击可取消 |
| ServerListPingEvent | 服务器列表 | MOTD/人数上限 |
| WeatherChangeEvent | 天气切换 | 可取消 |

*EntityTargetEvent/EntityDeathEvent 部分接线中。扩展新事件: 继承 `Event` -> 在逻辑处 `EventManager.INSTANCE.fire(...)`。

## 调度器

```java
ServerScheduler s = getScheduler();
var ref = ServerScheduler.ref(this);
long id = s.runTaskTimer(ref, task, delayTicks, periodTicks);
s.runTaskAsync(ref, ioTask);      // 异步池, 勿直接改世界
s.cancel(id);
```

## 权限

- 插件注册: `PermissionManager.register("myplugin.cmd", Default.OP)`；或 plugin.yml `permissions:` 块(default: everyone/op/nobody)。
- 运行时附件: `PermissionManager.setPermission(player, node, true/false)`，登出自动清理。
- 判定顺序: Lv5 全通过 > 附件显式值 > 注册默认值 > 未注册节点要求 OP。
- 命令权限: `PluginCommand.execute` 内用 `sender.hasPermission(node)` 自行校验。

## 命令与补全

- `registerCommand(name, description, executor, aliases...)`；`CommandExecutor.onCommand(sender,label,args)` + `onTabComplete(sender,label,args)` 返回候选。
- 路由: 玩家 `/cmd`、控制台 `cmd` 均优先派发插件命令; 注册后 Brigadier 树自动刷新(Tab 可见)。
- `Server.get().dispatchCommand(cmd)` 以控制台身份执行任意命令。

## BossBar

```java
BossBar bar = Server.get().createBossBar("标题", BossBar.Color.RED, BossBar.Style.NOTCHED_6, 0.5f);
bar.addPlayer(handle); bar.setProgress(0.8f); bar.setTitle("新标题"); bar.removeAll();
```

## World API

```java
World w = World.overworld();
w.getBlockName(x,y,z); w.setBlock(x,y,z,"oak_log","axis","y");
w.dropItem(x,y,z,"stone",1);
w.getEntitiesInRange(x,y,z,16).forEach(e -> ...);
```

## 配置

`saveDefaultConfig()` 从 jar 释放 config.yml; `getConfig()` 返回嵌套 Map/List/标量; `reloadConfig()/saveConfig()`。YAML 子集: 嵌套 map/列表/引号标量/注释/类型自动识别。

## 生命周期

启动扫描 `plugins/*.jar` -> 依赖拓扑排序 -> 逐个 onEnable；`/reloadplugins` 全量禁用/重载(JAR 可替换)；卸载自动清理任务/命令/监听/权限附件。数据目录 `plugins/<插件名>/`。

## 与 Paper 的差异(现状)

- 物品模型是轻量 `(itemId, count)`，无 ItemMeta/lore 编辑（附魔等走底层句柄）。
- 无自定义 Inventory/Scoreboard/PDC/资源包接口(规划中)。
- 事件约 25 个已接线, Paper 有 200+; 总线语义一致, 新事件接入成本低。
- 文本为 String(§ 颜色码), 无 Adventure Component。
