# server.properties 配置说明

首次启动自动生成于服务器根目录，改后需重启生效。

| 键 | 默认 | 说明 |
|---|---|---|
| `server-port` | `25565` | 监听端口 |
| `online-mode` | `false` | `true` = 完整正版认证（RSA/AES-CFB8 加密 + sessionserver hasJoined 校验，仅正版可进）；`false` = 离线模式 |
| `uuid-fix` | `true` | 离线模式下登录时异步查询 Mojang API：正版名 → 正版 UUID + 签名皮肤属性（皮肤/披风正常显示），盗版名 → 离线 UUID。正版+盗版都能进 |
| `network-compression-threshold` | `256` | zlib 压缩阈值（字节），`-1` 关闭压缩。双向（含 serverbound） |
| `view-distance` | `12` | 视距（区块），2-32 |
| `simulation-distance` | `8` | 模拟距离，2-32 |
| `max-players` | `20` | 最大在线人数（超出拒绝登录） |
| `motd` | `§bYanRong ...` | 服务器列表描述（支持 § 颜色码） |
| `level-seed` | 空 | 预留（当前种子来自 level.dat，无 level.dat 时 1234567） |
| `difficulty` | `1` | 0和平 1简单 2普通 3困难 |
| `gamemode` | `survival` | 新玩家默认游戏模式 |
| `allow-nether` | `true` | 预留 |
| `spawn-protection` | `16` | 预留 |
| `white-list` | `false` | 预留 |

## 在线模式三种组合

| online-mode | uuid-fix | 效果 |
|---|---|---|
| `false` | `true`（默认） | 混合模式：盗端+正端都能进；正版玩家有正确 UUID/皮肤 |
| `false` | `false` | 纯离线：全部离线 UUID |
| `true` | - | 纯正版：加密握手 + Mojang 认证，盗版无法进入 |

注意：切换 uuid-fix 会改变正版玩家的 UUID，对应 playerdata 文件也会切换（旧 JSON 数据按 UUID 存放，不跟随迁移）。

## 新特性开关位置

- **压缩**：登录阶段发送 set_compression(0x03) 后，双向所有包 zlib 封帧（`Compression.java`）。
- **光照**：真实天空光/方块光引擎（`world/light/LightEngine.java`），区块包与 Update Light(0x2F) 均为真实数据；放/挖方块即时增量更新。
- **玩家数据**：`world/playerdata/<uuid>.dat`（gzip NBT，原版兼容，含 components 物品组件）；首次登录自动从旧 `players/*.json` 迁移。
- **插件**：`plugins/` 目录放入 JAR 自动加载；`/reloadplugins` 热重载。详见 `docs/PLUGIN_API.md`。
