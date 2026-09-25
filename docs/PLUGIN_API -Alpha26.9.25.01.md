# CharunCore 插件 API 全量指南（最后修改时间：2026/9/25）

> 使用核心版本: CharunCore Alpha26.9.25.01
> 游戏版本: Minecraft 1.21.11 (Protocol 774) · 包 `com.CharunCore.server.plugin`
> 目标: 对标 Paper/Bukkit —— 插件可控制服务器的一切, 包括底层协议包

完整可编译示例见 `examples/` 目录 (EssentialsGUI / InfoBoard / Guard)。

---

## 1. 快速开始

一个插件 = 一个 JAR：`plugin.yml`(必需) + `Plugin` 子类(必需) + `config.yml`(可选)。

**plugin.yml**

```yaml
name: MyPlugin
version: 1.0.0
main: com.example.MyPlugin
api-version: '1.0'
author: YourName
depend: [OtherPlugin]        # 硬依赖(缺失则跳过加载)
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

public class MyPlugin extends Plugin {
    @Override public void onEnable() {
        getLogger().info("MyPlugin 已启用!");
    }
    @Override public void onDisable() {}
}
```

编译打包：

```
javac -cp "target/classes" -d . MyPlugin.java
jar cf plugins/MyPlugin.jar com/ plugin.yml config.yml
```

`/reloadplugins`(控制台) 热重载；`/plugins` 查看列表。生命周期: 启动扫描 `plugins/*.jar` → 依赖拓扑排序 → 逐个 onEnable；卸载自动清理 调度任务/命令/监听器/权限附件。

---

## 2. 核心 API 总览

| 类 | 说明 |
|---|---|
| `Server.get()` | 服务器门面: 世界/玩家/实体/命令/调度/配置/封禁/工厂 |
| `Plugin` | 基类: onEnable/onDisable/getLogger/getConfig/registerEvents/registerCommand |
| `plugin.api.Player` | 玩家门面: 消息/标题/音效/传送/状态/背包/自定义容器/PDC/飞行/伤害 |
| `plugin.api.World` | 世界门面: 方块/掉落物/生成实体/雷击/爆炸/时间/天气/范围查询 |
| `plugin.api.ItemStack` | 物品(itemId, count + ItemMeta) |
| `plugin.api.ItemMeta` | 元数据: 显示名/lore/附魔/耐久/不可破坏/发光 |
| `plugin.api.Inventory` | 插件自定义容器界面(9~54 格, 点击/关闭回调, 实时刷新) |
| `plugin.api.InventoryClickContext` | 菜单点击上下文(cancel/handled 语义) |
| `plugin.api.Scoreboard/Objective/Team` | 计分板: 侧边栏/Tab/名字下分数 + 队伍前缀后缀颜色 |
| `plugin.api.PersistentDataContainer` | 持久键值(随玩家数据落盘) |
| `plugin.api.Entity/Mob/Entities` | 实体门面: 生成/查询/传送/速度/点火/血量 |
| `plugin.api.Packets` | 底层协议包直发(单发/维度广播/全服广播) |
| `plugin.api.BossBar` | Boss 血条 |
| `plugin.api.PermissionManager` | 权限注册/运行时附件 |
| `EventManager` | 事件总线(优先级/取消/注销) |
| `ServerScheduler` | 同步/异步/延迟/周期任务 |

---

## 3. Server 门面 (`Server.get()`)

```java
Server s = Server.get();

// 玩家
s.getOnlinePlayers();            // List<NetworkHandler>
s.getPlayer("Love_computers");   // 按名(忽略大小写)
s.getPlayer(uuid);
s.getOnlineCount(); s.getMaxPlayers(); s.setMaxPlayers(50);
s.broadcast("§a全服公告");        s.broadcast(msg, "yellow");
s.kickAll? — 遍历 p.kick("原因")

// 世界
s.getWorlds();                   // [overworld, nether, the_end] (plugin.api.World)
s.getWorld("the_nether");
s.getSeed();
s.getGameRule("keepInventory"); s.setGameRule("keepInventory", true);

// 实体
s.spawnEntity("zombie", x, y, z);          // 主世界
s.spawnEntity("ghast", s.getWorld("the_nether"), x, y, z);

// 命令
s.dispatchCommand("say hello");  // 以控制台身份执行
s.getConsoleSender();

// UI 工厂
s.createInventory(27, "§8商店");  // 插件自定义容器
s.createBossBar("标题", BossBar.Color.RED, BossBar.Style.NOTCHED_10, 1.0f);
s.createScoreboard();

// 配置
s.getMotd(); s.setMotd("§b新 MOTD");
s.getViewDistance(); s.setViewDistance(12);
s.getPort();

// 封禁
s.ban("某人", "理由");            // 永久 + 立即踢出
s.banTemp("某人", "理由", 3600_000);
s.unban("某人"); s.isBanned("某人");
s.getBanEntries();

// 时间/天气
s.getDayTime(); s.setDayTime(1000);   // 自动广播 0x6F
s.isRaining(); s.setRaining(true);    // 自动广播 game_state

// 生命周期
s.getVersion(); s.getName();
s.reloadPlugins();
s.shutdown();
```

## 4. Player API (`plugin.api.Player`)

命令执行器中 `sender` 就是 `Player` 实例(控制台为 ConsoleSender)。

```java
Player p = (Player) sender;

// 基础
p.getName(); p.getUniqueId(); p.getAddress();
p.isOnline(); p.isOp(); p.getOpLevel();
p.hasPermission("node");
p.kick("原因");
p.sendMessage("§a文本"); p.sendMessage(msg, "yellow");

// 位置/移动
p.getX/Y/Z/getYaw/getPitch(); p.isOnGround();
p.getWorld();                    // DimensionType
p.teleport(x, y, z);
p.teleport(otherPlayer);
p.teleport(dim, x, y, z);        // 跨维度
p.setVelocity(vx, vy, vz);

// 飞行/潜行
p.getAllowFlight(); p.setAllowFlight(true);
p.isFlying(); p.setFlying(true);     // 需先 setAllowFlight
p.isSneaking(); p.isSprinting();

// 状态
p.getHealth(); p.setHealth(10f);
p.heal(); p.feed();
p.damage(3f); p.damage(3f, "explosion");   // 走完整伤害管线(事件/死亡/护甲)
p.getGameMode(); p.setGameMode(1);
p.getFoodLevel(); p.setFoodLevel(20);
p.getSaturation(); p.setSaturation(5f);
p.getExpLevel(); p.setExpLevel(30); p.giveExp(100);
p.getFireTicks(); p.setFireTicks(40);

// 物品/背包 (槽 0-8 快捷栏, 9-35 主背包, 36-39 装备, 40 副手)
p.getItemInMainHand(); p.setItemInMainHand(item);
p.getInventorySlot(9); p.setInventorySlot(9, item);
p.getHeldItemSlot(); p.setHeldItemSlot(3);
p.giveItem("diamond", 64);
p.giveItem(itemWithMeta);        // 背包满自动掉落
p.clearInventory();

// 自定义容器 / 持久数据
p.openInventory(menu);           // 见第 7 节
p.closeInventory();
p.getPersistentDataContainer().setInt("myplugin.kills", 5);

// 表现
p.sendTitle("§6标题", "§7副标题", 10, 60, 10);
p.sendActionBar("§e动作栏");
p.playSound("minecraft:entity.player.levelup", 1f, 1f);
p.playSound(sound, x, y, z, 1f, 1f);
p.spawnParticle("happy_villager", x, y, z, 10, 0.5, 0.5, 0.5, 0);
p.sendBlockChange(x, y, z, "diamond_block");   // 强制客户端显示

// 底层
p.performCommand("spawn");
p.getHandle();                   // NetworkHandler (高级用法)
```

## 5. World API (`plugin.api.World`)

```java
World w = World.overworld();           // 或 World.nether() / World.theEnd() / World.of(dim)

// 方块
w.getBlockName(x, y, z);
w.getBlockState(x, y, z);
w.setBlock(x, y, z, "oak_stairs", "facing", "north", "half", "bottom");
w.setBlockState(x, y, z, stateId);
w.getHighestBlockYAt(x, z);

// 物品/实体
w.dropItem(x, y, z, "stone", 1);
w.dropItem(x, y, z, itemStack);
w.spawnEntity("creeper", x, y, z);
w.getEntities(); w.getEntitiesInRange(x, y, z, 16);
w.getPlayers();

// 特效
w.playSound("minecraft:entity.lightning_bolt.thunder", x, y, z, 1f, 1f);
w.strikeLightning(x, y, z);            // 闪电 + 3 格伤害点燃
w.createExplosion(x, y, z, 4f, true);  // TNT 当量; false=仅伤害击退

// 时间/天气(全服共享)
w.getTime(); w.setTime(6000);
w.isRaining();

// 区块
w.isChunkLoaded(cx, cz); w.loadChunk(cx, cz);
```

## 6. ItemStack + ItemMeta

```java
// 构造
ItemStack plain = ItemStack.of("diamond_sword", 1);
ItemStack named = ItemStack.of("diamond_sword", 1,
        new ItemMeta()
            .setDisplayName("§b霜之哀伤")
            .addLoreLine("§7传说之剑")
            .addLoreLine("§8右键释放寒冰"))
        .meta().addEnchant("sharpness", 5)
               .addEnchant("unbreaking", 3)
               .setDamage(0)
               .setUnbreakable(true)
               .setGlintOverride(true);

// ItemStack 常用
plain.itemId(); plain.count(); plain.name(); plain.isEmpty(); plain.maxStackSize();
plain.hasItemMeta(); plain.meta();          // meta() 懒创建(可变)
plain.withMeta(m);                          // 返回带 meta 的新对象

// ItemMeta 全能力
meta.setDisplayName("§6名");     // 落盘持久
meta.setLore(List.of("行1","行2")); addLoreLine(...);   // 会话内有效
meta.addEnchant("protection", 4); removeEnchant(...); getEnchantLevel(...);  // 落盘持久
meta.setDamage(100);              // 已损失耐久(持久)
meta.setUnbreakable(true);        // 不可破坏
meta.setGlintOverride(true/false);
meta.isEmpty(); meta.clone();
```

附魔名与原版一致: sharpness/smoothness→efficiency/protection/feather_falling/unbreaking/fortune/silk_touch/looting/knockback/fire_aspect/mending/...

## 7. 插件自定义容器 (Inventory)

```java
Inventory menu = Server.get().createInventory(27, "§8传送菜单");

// 填充装饰
menu.fill(Inventory.decor("gray_stained_glass_pane", " "));

// 放功能物品
menu.setItem(13, ItemStack.of("ender_pearl", 1)
        .withMeta(new ItemMeta().setDisplayName("§5回主世界")
                                .addLoreLine("§7点击传送")));

// 点击回调
menu.onClick(ctx -> {
    Player p = ctx.getPlayer();
    int slot = ctx.getRawSlot();
    if (!ctx.inMenuArea()) return;         // 点在玩家背包区, 不接管
    ItemStack clicked = ctx.getInventory().getItem(slot);
    if (clicked == null) return;
    if (slot == 13) {
        p.teleport(World.overworld(), 0, 100, 0);
        p.closeInventory();
    }
    ctx.setCancelled(true);                // 取消原生交互(装饰格不可拿走)
    // ctx.setHandled(true);               // 或完全接管: 自己改槽位后 menu.refresh()
});

// 关闭回调
menu.onClose(pl -> pl.sendMessage("菜单已关闭"));

// 打开
p.openInventory(menu);

// 动态更新
menu.setItem(11, someItem);       // 自动刷新所有查看者
menu.refresh(); menu.closeAll();
menu.getViewers(); menu.getViewersCount();
menu.fillRow(0, decor);           // 整行填充
menu.firstEmpty(); menu.addItem(item); menu.clear();
menu.setTitle("§9新标题");         // 重发 open_screen
```

**点击语义**: 默认不取消不接管 —— 未调用 `setHandled` 时服务端按普通容器逻辑处理
(可正常拿起/放下/交换)。装饰格场景统一 `setCancelled(true)`。
点击类型: ctx.getType() ∈ PICKUP / PICKUP_HALF / SWAP / CLONE / THROW / QUICK_MOVE / QUICK_CRAFT / DOUBLE_CLICK。

## 8. 计分板 (Scoreboard / Objective / Team)

```java
Scoreboard sb = Server.get().createScoreboard();

// 侧边栏
Objective side = sb.createObjective("server_info", "§b§l服务器信息");
side.display(player, Objective.SLOT_SIDEBAR);   // SLOT_LIST=0 / SLOT_SIDEBAR=1 / SLOT_BELOW_NAME=2

side.setScore("§e在线: §f" + n, n);
side.setScore("§e金币: §f" + coins, coins);
side.setScore("分隔线", 0);
side.removeScore("旧条目");
side.setDisplayName("§a新标题");               // 广播更新
side.getAllScores();

// Tab 列表 / 名字下方
side.display(player, Objective.SLOT_LIST);
side.display(player, Objective.SLOT_BELOW_NAME);
Objective.clearDisplay(player, Objective.SLOT_SIDEBAR);

// 队伍(头上/Tab 前缀后缀)
Team red = sb.createTeam("red", "红队");
red.setColorCode('c');                     // 颜色码字符
red.setPrefix("§c[红] ");
red.setSuffix(" §7◆");
red.setFriendlyFire(false);
red.addEntry("Love_computers");            // 立即生效
red.removeEntry("..."); red.getEntries();

// 计分板对玩家可见性
sb.addViewer(player); sb.removeViewer(handle); sb.getViewerCount();
sb.getObjective("server_info"); sb.removeObjective("server_info");
sb.getTeam("red"); sb.removeTeam("red");
```

## 9. 实体 API (Entity / Mob / Entities)

```java
// 生成
Entity e = Server.get().spawnEntity("zombie", x, y, z);
Entity e2 = World.nether().spawnEntity("ghast", x, y, z);
Entity drop = Entities.dropItem("diamond", 1, dim, x, y, z);
Entities.get(eid); Entities.getAll(dim); Entities.getInRange(dim, x, y, z, 16);

// Entity
e.getEntityId(); e.getType();
e.getX/Y/Z/getYaw/getPitch(); e.getWorld();
e.teleport(x, y, z); e.setVelocity(vx, vy, vz);
e.getFireTicks(); e.setFireTicks(40);
e.isOnGround(); e.isValid(); e.remove();
e.refreshMetadata();

// Mob (生物)
if (e instanceof Mob m) {
    m.getHealth(); m.setHealth(5f);
    m.maxHealth(); m.setMaxHealth(40f);
    m.damage(3f); m.damage(3f, "plugin");
    m.mobHandle();           // MobEntity (AI 底层)
}
```

## 10. 持久数据 (PersistentDataContainer)

```java
PersistentDataContainer pdc = p.getPersistentDataContainer();
pdc.setString("myplugin.home.1", "100,64,100");
pdc.setInt("myplugin.kills", 42);
pdc.setLong / setDouble / setBoolean / setByte;
pdc.getInt("myplugin.kills", 0);
pdc.has("myplugin.kills"); pdc.remove("..."); pdc.getKeys(); pdc.clear();

// 数据随玩家落盘 (world/playerdata/<uuid>.dat 的 pluginData 字段), 跨重启保留。
// 键名建议带插件前缀防冲突。
```

## 11. 底层协议包直发 (Packets)

插件可以控制服务器的一切 —— 包括直接发送任意 clientbound 协议包 (774)。

```java
// 单发 / 维度广播 / 全服广播
Packets.send(player, 0x6F, pb -> {
    pb.writeLong(Main.worldAge); pb.writeLong(Main.dayTime); pb.writeBoolean(true);
});
Packets.broadcast(dim, packetId, pb -> { ... });
Packets.broadcastAll(packetId, pb -> { ... });

// 快捷底层操作
Packets.sendBlockChange(player, x, y, z, stateId);   // 0x09
Packets.sendMultiBlockChange(player, cx, cy, cz, coords, states);  // 0x0B
Packets.sendEntityMetadata(player, eid, index, serializerId, valueWriter); // 0x61
Packets.broadcastSound(dim, x, y, z, sound, vol, pitch);
```

包 id 参考 `json/1.21.11/protocol.json` (权威 wire 表 = 服务端 jar GameProtocols 注册序)。
注意: 发送格式错误的包会导致客户端断线, 仅发送已知格式的包。

## 12. 事件系统

注册:

```java
public class MyListener implements EventListener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) { ... }
}
// onEnable 中:
registerEvents(new MyListener());
// 或 registerEvents(this); (Plugin 自身实现 EventListener)
```

事件总表 (com.CharunCore.server.plugin.event.events):

| 事件 | 触发点 | 可取消/可变 |
|---|---|---|
| PlayerJoinEvent / PlayerQuitEvent | 进出服 | 消息可改 |
| PlayerChatEvent | 聊天 | 可取消 |
| PlayerCommandPreprocessEvent | 内置命令前 | 可取消 |
| PlayerMoveEvent | 移动 | 可取消(回弹)/目标可改 |
| PlayerTeleportEvent | 任何传送 | 可取消/目标可改 |
| PlayerInteractEvent | 点击方块 | 可取消 |
| BlockBreakEvent / BlockPlaceEvent | 破坏/放置 | 可取消, dropItems |
| BlockBurnEvent | 火焰蔓延烧毁方块 | 可取消 |
| PlayerDeathEvent / PlayerRespawnEvent | 死亡/重生 | 消息/keepInventory/复活点 |
| PlayerGameModeChangeEvent | 模式切换 | 可取消 |
| PlayerToggleSneakEvent / PlayerToggleSprintEvent | 潜行/疾跑 | — |
| PlayerToggleFlightEvent | 切换飞行 | — |
| PlayerDropItemEvent | 丢物品 | 可取消 |
| PlayerItemHeldEvent | 切换快捷栏 | 可取消 |
| PlayerKickEvent | 被踢出 | 可取消/原因可改 |
| PlayerExpChangeEvent | 经验变化 | 数量可改 |
| PlayerLevelChangeEvent | 等级变化 | — |
| PlayerAnimationEvent | 挥手/挖动手臂 | — |
| PlayerChangedWorldEvent | 跨维度 | — |
| PlayerPortalEvent | 传送门 | — |
| PlayerBedEnterEvent / PlayerBedLeaveEvent | 睡觉/起床 | — |
| PlayerBucketFillEvent / PlayerBucketEmptyEvent | 桶 | — |
| PlayerItemConsumeEvent | 吃喝 | — |
| PlayerFishEvent | 钓鱼 | — |
| PlayerShearEntityEvent / PlayerEggThrowEvent | 剪羊毛/扔蛋 | — |
| FoodLevelChangeEvent | 饥饿值下降 | 可取消 |
| EntityDamageEvent / EntityDamageByEntityEvent | 实体受伤 | 可取消/数值可改 |
| EntityDeathEvent | 实体死亡 | 掉落经验可改 |
| EntitySpawnEvent | 实体生成 | 可取消 |
| EntityDespawnEvent | 实体移除 | — |
| EntityExplodeEvent | 爆炸 | 可取消/威力可改 |
| EntityRegainHealthEvent | 回血 | — |
| EntityCombustEvent | 燃烧 | — |
| EntityTargetEvent | 生物索敌 | — |
| EntityShootBowEvent | 射箭 | — |
| EntityTameEvent | 驯服 | — |
| EntityPickupItemEvent | 拾取物品 | 可取消 |
| ProjectileHitEvent | 投射物命中 | — |
| ItemSpawnEvent / ItemDespawnEvent | 掉落物生成/消失 | — |
| InventoryClickEvent / InventoryOpenEvent / InventoryCloseEvent | 容器交互 | 点击可取消 |
| BrewEvent / FurnaceSmeltEvent / FurnaceBurnEvent | 酿造/熔炼 | — |
| BlockGrowEvent / BlockSpreadEvent / BlockFromToEvent / BlockPhysicsEvent / BlockRedstoneEvent / BlockIgniteEvent | 方块变化 | 部分可取消 |
| LeavesDecayEvent | 树叶枯萎 | — |
| ChunkLoadEvent / ChunkUnloadEvent | 区块加载 | — |
| TimeChangeEvent | 时间变化 | — |
| WeatherChangeEvent / ThunderChangeEvent | 天气切换 | 可取消 |
| LightningStrikeEvent | 雷击 | — |
| VehicleEnterEvent / VehicleExitEvent | 载具 | — |
| ServerTickEvent | 每 tick (50ms) | — |
| ServerListPingEvent | 服务器列表 | MOTD/人数上限 |

扩展新事件: 继承 `Event` → 在逻辑处 `EventManager.INSTANCE.fire(...)`。

## 13. 调度器

```java
ServerScheduler s = getScheduler();
var ref = ServerScheduler.ref(this);
long id = s.runTaskTimer(ref, task, delayTicks, periodTicks);  // 主线程周期
s.runTaskLater(ref, task, delayTicks);                          // 主线程延迟
s.runTask(ref, task);                                           // 主线程下一 tick
s.runTaskAsync(ref, ioTask);                                    // 异步池, 勿直接改世界
s.cancel(id);                                                   // 按 id 取消
// 插件卸载自动取消其全部任务
```

## 14. 权限

- 注册: `PermissionManager.register("myplugin.cmd", Default.OP)`；或 plugin.yml `permissions:` 块。
- 运行时附件: `PermissionManager.setPermission(player, node, true/false)`，登出自动清理。
- 判定顺序: Lv5 全通过 > 附件显式值 > 注册默认值 > 未注册节点要求 OP。
- 命令权限: PluginCommand 构造可传 permission(自动校验), 或 executor 内 `sender.hasPermission(node)`。

## 15. 命令与补全

```java
registerCommand("spawn", "回出生点", new CommandExecutor() {
    @Override public boolean onCommand(CommandSender s, String label, String[] args) {
        if (!s.isPlayer()) return true;
        ((Player) s).teleport(0, 100, 0);
        return true;
    }
    @Override public java.util.List<String> onTabComplete(CommandSender s, String label, String[] args) {
        return List.of("help");
    }
}, "sp");
```

路由: 玩家 `/cmd`、控制台 `cmd` 均优先派发插件命令; 注册后 Brigadier 树自动刷新(Tab 可见)。
命令执行时传入的 sender 即 `plugin.api.Player`。

## 16. 配置

`saveDefaultConfig()` 从 jar 释放 config.yml; `getConfig()` 返回嵌套 Map/List/标量; `reloadConfig()/saveConfig()`。
YAML 子集: 嵌套 map/列表/引号标量/注释/类型自动识别。

---

## 17. 与 Paper 的差异

>目前和 Paper 有差异，如下：

- 文本为 String(§ 颜色码), 无 Adventure Component。
- ItemMeta 的 lore/unbreakable/glint 会话内有效(不随背包落盘); displayName/enchants/damage 持久。
- 自定义 Inventory 不支持强制放置光标物品的高阶操作(QUICK_CRAFT 拖拽走原生逻辑)。
- 事件约 60 类已接线; 未接线事件类会静默无触发。
- PDC 值以 String 序列化, 类型为便利封装。
