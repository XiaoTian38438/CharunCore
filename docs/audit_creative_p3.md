# 审计 P3：创造/管理员 中键选中（Middle-Click Pick）

> 范围：创造模式玩家中键点击方块时，对应方块物品是否加入背包（而非破坏）；服务端 `set_creative_slot` 0x37 处理逻辑。
> 对照源：`json/1.21.11/protocol.json`（`packet_set_creative_slot` / `UntrustedSlot`）、原版 `CreativeModeInventoryScreen` 中键逻辑（客户端决定物品并由 `SetCreativeSlot` 通知服务端）。
> 审计铁律：所有行号来自实际阅读，未修改任何源码。协议字段顺序已对照 `protocol.json` 实地核实。

---

## 1. 实现状态概述

**状态：已实现，且服务端协议解析正确；仅有组件丢失与缺模式校验两类偏差。**

- 服务端在 `id==0x37`（`set_creative_slot`）处接收客户端中键选中的物品并更新背包（`NetworkHandler.java:1053-1081`）。
- 经 `protocol.json` 核对：`packet_set_creative_slot` = `slot: i16` + `item: UntrustedSlot`；`UntrustedSlot` = `itemCount: varint`（0 → 空）其后 `itemId: varint` + `addedComponentCount` + `removedComponentCount` + 组件数组。**代码解析顺序与协议完全一致**（`readShort` 取 slot、`readVarInt` 取 itemCount、再 itemId、added/removed 组件计数并跳过组件数据），未发现此前怀疑的“字段错位/过度读取”问题。

> 注：曾怀疑 `slot` 应为 VarInt、或读顺序错位导致中键解析全错；经 `protocol.json:11231`（`packet_set_creative_slot`，slot=i16）与 `:2158`（`UntrustedSlot`，首字段为 itemCount 而非 present 布尔）核实，**代码实现正确**，该担心不成立。

---

## 2. 关键文件与行号证据

| 模块 | 文件:行号 | 说明 |
|---|---|---|
| 中键/set_creative_slot 处理 | `network/NetworkHandler.java:1053`（`id==0x37` set_creative_slot） | `short slot=readShort()`（:1054，i16 正确）；`int count=readVarInt()`（:1055，=itemCount）；`if(count>0)` 内 `itemId=readVarInt()`（:1057）、`addedComponents=readVarInt()`（:1058）、`removedComponents=readVarInt()`（:1059）；跳过 added 组件（type VarInt+len VarInt+data，:1061-1065）；跳过 removed 类型（:1067-1069）；写入 `data.inventoryIds[slot]=itemId`、`inventoryCounts[slot]=count`（:1070-1073）；空则清零（:1075-1078）；`broadcastEquipment()`（:1080） |
| 协议字段定义 | `json/1.21.11/protocol.json:11231`（`packet_set_creative_slot`，slot=i16，item=UntrustedSlot） | 确认 slot 为 i16 |
| UntrustedSlot 定义 | `json/1.21.11/protocol.json:2158`（`itemCount: varint` 起，0→void，否则 itemId+added/removed 组件计数+components） | 确认代码读取顺序正确 |
| 背包数据结构 | `world/PlayerData.java:7-8`（`inventoryIds[46]`/`inventoryCounts[46]`） | 中键结果写入的存储空间，索引 0..45 与 `set_creative_slot` 的 slot 一致 |

客户端行为（原版，非本核代码）：中键点击时由客户端计算目标方块对应 `ItemStack`，写入玩家库存对应槽位，并发送 `SetCreativeSlot` 通知服务端。本核服务端正确接收并落库。

---

## 3. 对照原版的关键差异/偏差（逐条，附 文件:行号）

1. **组件（DataComponentPatch）被丢弃，服务端只保留 itemId+count**
   `NetworkHandler.java:1060-1069` 仅“跳过” added/removed 组件数据，未解析保存。中键选中的带组件物品（附魔、命名、自定义数据、方块状态等）在**服务端侧丢失组件信息**。原版 `ServerGamePacketListenerImpl.handleSetCreativeModeSlot` 会保留完整 `ItemStack`（含组件）。
   - 影响：服务端后续若向该玩家回发库存（死亡掉落、某些槽位更新、或任何 `sendInventoryUpdate` 覆盖该槽），会用“无组件”的 `inventoryIds[slot]` 覆盖客户端本地带组件的物品，造成物品“掉组件”。当前对自己无 `sendInventoryUpdate` 回发（仅 `broadcastEquipment` 广播给其他人），短期不影响自己，但服务端权威状态已不全。

2. **未校验游戏模式**
   `NetworkHandler.java:1053` 对任何 `gameMode` 都接受 `set_creative_slot`。原版仅 creative/spectator 客户端会发送该包；**修改版客户端可在生存模式用此包把任意物品写入自己背包**（配合 `inventoryIds` 直接写库），实现物品复制。缺少 `if (gameMode!=1 && gameMode!=3) return;` 守卫。

3. **服务端不做“从方块推导物品”的二次校验（说明性）**
   原版也由客户端决定中键物品、服务端信任；本核同样信任客户端，无服务端推导。此点与原版一致，不算偏差，仅作说明。

4. **`broadcastEquipment` 仅广播给其他玩家**
   `:1080` 调用 `broadcastEquipment()`（装备/手持广播），未向自己回发完整库存。对自身无影响（客户端本地已更新），服务端 `inventoryIds[slot]` 已更新，状态自洽。无 bug，仅说明。

---

## 4. Bug 清单（只记录不修）

| # | 严重程度 | 文件:行号 | 现象 | 建议修复方向 |
|---|---|---|---|---|
| P3-1 | 中等 | `NetworkHandler.java:1060-1069` | 中键选中的带组件物品（附魔/命名/自定义数据）在服务端丢失组件；后续若服务端回发该槽库存会“掉组件” | 解析并保留 `DataComponentPatch`（至少缓存 added 组件），或在回发库存时优先使用客户端权威物品；短期可保证不对该槽做无组件覆盖 |
| P3-2 | 中等（安全） | `NetworkHandler.java:1053` | 生存模式下服务端接受 `set_creative_slot`，修改版客户端可借此复制任意物品 | 入口增加 `if (gameMode!=1 && gameMode!=3) return;`（并校验 slot ∈ [0,46) 已具备） |
| P3-3 | 轻微 | `NetworkHandler.java:1053-1081` | 仅存储 itemId+count，未跟踪组件；未来若支持带 NBT 的物品（如 `/give` 加组件，见 P1-6）与此路径不互通 | 建立统一 ItemStack 表示（id+count+components），`set_creative_slot` 与 `/give`/掉落物共用 |

---

## 5. 结论与优先级建议

- **核心结论**：中键选中功能**已实现且协议解析正确**（已对照 `protocol.json` 的 `packet_set_creative_slot` 与 `UntrustedSlot` 逐字段核实，未发现解析错位）。创造模式玩家中键点击方块，对应物品会正确写入 `PlayerData.inventoryIds[slot]` 并广播给其他人。
- **优先修复**：P3-2（生存模式缺模式守卫，物品复制漏洞）属安全类，建议在切模式守卫（P2-5）一并处理；P3-1（组件丢失）在与组件系统（P1-6 `/give` components、掉落物组件）统一时一并解决。
- **无需改动**：解析顺序、slot 边界检查、空物品清零逻辑均已正确。

> 对比 P2-1：中键“选中”走的是 `set_creative_slot`（0x37），与“放置”（`use_item_on` 0x3F）是不同路径；即便修复了冒险/旁观可放置的 bug，中键本身不受其影响（中键在创造/旁观下由客户端决定，服务端照单全收）。
