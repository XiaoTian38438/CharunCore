# CharunCore 示例插件

三个完整可编译的示例插件, 演示插件 API 的主要能力。

| 示例 | 演示内容 |
|---|---|
| `EssentialsGUI` | 自定义容器菜单(Inventory)、物品元数据(ItemMeta)、持久数据(PDC)、命令注册 |
| `InfoBoard` | 计分板侧边栏(Scoreboard/Objective/Team)、队伍前缀、ServerTickEvent、调度器 |
| `Guard` | 事件取消(爆炸/火烧/区域保护)、权限、Tab 补全、物品发放 |

## 编译打包 (以 EssentialsGUI 为例)

```powershell
# 前提: 主项目已 mvn compile (需要 target/classes)
javac -encoding UTF-8 -cp "target/classes" -d out examples\EssentialsGUI\EssentialsPlugin.java
# 连同 plugin.yml 打包
Compress-Archive -Path out\*, examples\EssentialsGUI\plugin.yml -DestinationPath EssentialsGUI.zip
Move-Item EssentialsGUI.zip plugins\EssentialsGUI.jar -Force
# 或者用 jar 命令
jar cf plugins\EssentialsGUI.jar -C out . examples\EssentialsGUI\plugin.yml
```

放到 `plugins/` 目录后重启服务器或 `/reloadplugins` 即可加载。

## 使用

- **EssentialsGUI**: `/menu` 打开传送菜单(主世界/下界/末地按钮), `/sethome` `/home` 个人主页(跨重启保留)
- **InfoBoard**: 进服自动显示侧边栏(在线/时间/天气), OP 头上带 `[管理]` 前缀
- **Guard**: 默认全服防爆+防火蔓延, 出生点 32 格保护区(OP 持金斧豁免), `/guard radius <n>` 调整

## API 文档

见 `docs/PLUGIN_API.md` — 全量接口说明(门面/Inventory/ItemMeta/Scoreboard/PDC/实体/底层包/事件表/调度器/权限/命令)。
