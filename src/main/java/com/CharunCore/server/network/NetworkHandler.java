package com.CharunCore.server.network;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.CharunCore.server.Main;
import com.CharunCore.server.ServerConfig;
import com.CharunCore.server.advancement.AdvancementManager;
import com.CharunCore.server.command.BanList;
import com.CharunCore.server.command.EntitySelector;
import com.CharunCore.server.command.OpList;
import com.CharunCore.server.command.Permissions;
import com.CharunCore.server.command.TpaSystem;
import com.CharunCore.server.network.protocol.PacketBuffer;
import com.CharunCore.server.plugin.Server;
import com.CharunCore.server.plugin.command.CommandSender;
import com.CharunCore.server.plugin.event.EventManager;
import com.CharunCore.server.plugin.event.events.BlockBreakEvent;
import com.CharunCore.server.plugin.event.events.BlockPlaceEvent;
import com.CharunCore.server.plugin.event.events.EntityDamageByEntityEvent;
import com.CharunCore.server.plugin.event.events.InventoryClickEvent;
import com.CharunCore.server.plugin.event.events.InventoryCloseEvent;
import com.CharunCore.server.plugin.event.events.PlayerChatEvent;
import com.CharunCore.server.plugin.event.events.PlayerCommandPreprocessEvent;
import com.CharunCore.server.plugin.event.events.PlayerDeathEvent;
import com.CharunCore.server.plugin.event.events.PlayerDropItemEvent;
import com.CharunCore.server.plugin.event.events.PlayerExpChangeEvent;
import com.CharunCore.server.plugin.event.events.PlayerGameModeChangeEvent;
import com.CharunCore.server.plugin.event.events.PlayerInteractEvent;
import com.CharunCore.server.plugin.event.events.PlayerItemHeldEvent;
import com.CharunCore.server.plugin.event.events.PlayerJoinEvent;
import com.CharunCore.server.plugin.event.events.PlayerMoveEvent;
import com.CharunCore.server.plugin.event.events.PlayerQuitEvent;
import com.CharunCore.server.plugin.event.events.PlayerRespawnEvent;
import com.CharunCore.server.plugin.event.events.PlayerTeleportEvent;
import com.CharunCore.server.plugin.event.events.PlayerToggleSneakEvent;
import com.CharunCore.server.plugin.event.events.PlayerToggleSprintEvent;
import com.CharunCore.server.plugin.event.events.ServerListPingEvent;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.utils.RegistryHelper;
import com.CharunCore.server.world.ContainerStore;
import com.CharunCore.server.world.CraftingSystem;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.EnchantSystem;
import com.CharunCore.server.world.ExplosionEngine;
import com.CharunCore.server.world.FluidEngine;
import com.CharunCore.server.world.PlayerData;
import com.CharunCore.server.world.PlayerDataManager;
import com.CharunCore.server.world.RecipeRegistry;
import com.CharunCore.server.world.RedstoneEngine;
import com.CharunCore.server.world.SmeltingSystem;
import com.CharunCore.server.world.StatisticsManager;
import com.CharunCore.server.world.WorldManager;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.entity.ArrowEntity;
import com.CharunCore.server.world.entity.EndCrystalEntity;
import com.CharunCore.server.world.entity.EndDragonFight;
import com.CharunCore.server.world.entity.EnderDragonEntity;
import com.CharunCore.server.world.entity.EnderPearlEntity;
import com.CharunCore.server.world.entity.Entity;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.EyeOfEnderEntity;
import com.CharunCore.server.world.entity.FishingBobberEntity;
import com.CharunCore.server.world.entity.ItemEntity;
import com.CharunCore.server.world.entity.LivingEntity;
import com.CharunCore.server.world.entity.MinecartEntity;
import com.CharunCore.server.world.entity.MobEntity;
import com.CharunCore.server.world.entity.PotionEntity;
import com.CharunCore.server.world.entity.TridentEntity;
import com.CharunCore.server.worldgen.DensityRouterChunkGenerator;
import com.CharunCore.server.worldgen.structure2.BiomeTagResolver;
import com.CharunCore.server.worldgen.structure2.LootTableLoader;
import com.CharunCore.server.worldgen.structure2.RandomSpreadStructurePlacement;
import com.CharunCore.server.worldgen.structure2.StructureRegistry;
import com.CharunCore.server.worldgen.structure2.StructureSelectionEntry;
import com.CharunCore.server.worldgen.structure2.StructureSet;
import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NetworkHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public static final Map<UUID, NetworkHandler> players = new ConcurrentHashMap<>();

    private static final EventManager EVENTS =
            EventManager.INSTANCE;

    private final class PlayerSender implements CommandSender {
        @Override public String getName() { return username; }
        @Override public void sendMessage(String message) { sendFeedback(message, "white"); }
        @Override public boolean hasPermission(String permission) { return isOp(); }
        @Override public boolean isPlayer() { return true; }
    }

    /** 按用户名(忽略大小写)查找在线玩家, 供 EntitySelector 等选择器解析层调用。 */
    public static NetworkHandler player(String name) {
        if (name == null) return null;
        for (NetworkHandler h : players.values()) if (h.username.equalsIgnoreCase(name)) return h;
        return null;
    }

    private static final java.util.concurrent.ExecutorService chunkGenExecutor =
        java.util.concurrent.Executors.newFixedThreadPool(8, r -> {
            Thread t = new Thread(r, "ChunkGen-Worker");
            t.setDaemon(true);
            return t;
        });

    private static int windowIdCounter = 1;

    // windowId 0 是玩家自身背包, 容器窗口必须避开
    private static synchronized int nextWindowId() {
        windowIdCounter = windowIdCounter % 100 + 1;
        return windowIdCounter;
    }

    private final java.util.Map<Integer, String[]> openCraftingGrids = new java.util.concurrent.ConcurrentHashMap<>();
    /** Bug4/33: 工作台网格每槽组件(放入附魔/改名物品再取出/关窗退回时不丢 NBT)。 */
    private final java.util.Map<Integer, ItemMeta[]> openGridMetas = new java.util.concurrent.ConcurrentHashMap<>();
    // 与 openCraftingGrids 平行的 9 格数量表, 使合成网格支持堆叠
    private final java.util.Map<Integer, int[]> openCraftingCounts = new java.util.concurrent.ConcurrentHashMap<>();

    // Bug 9: recipe book — 把每个 CraftingSystem 配方索引成一个顺序 displayId，
    // 客户端点击配方书时回传该 id（place_recipe），服务端据此反查并填充网格。
    private static final java.util.List<RecipeRegistry.Recipe> RECIPE_BOOK_ENTRIES = new java.util.ArrayList<>();
    private static final java.util.Map<Integer, RecipeRegistry.Recipe> RECIPE_BOOK_BY_ID = new java.util.HashMap<>();
    /** 配方书展示去重: 每个产物物品只保留一个展示条目(recipes.json 的同产物变体配方
     *  —— 如 12 种木板合成木棍 —— 原版配方书里只显示一个木棍条目, 曾全部作为独立
     *  displayId 推送 -> 配方书里"木棍×12"式重复)。BOOK_DISPLAY_TO_RECIPE.get(d) = RECIPE_BOOK_ENTRIES 索引。 */
    private static final java.util.List<Integer> BOOK_DISPLAY_TO_RECIPE = new java.util.ArrayList<>();
    private static final java.util.Set<Integer> BOOK_REPRESENTATIVE_INDICES = java.util.concurrent.ConcurrentHashMap.newKeySet();
    static {
        // 惰性触发 RecipeRegistry.loadAll() (避免类加载顺序: BlockManager 必须先加载)
        try {
            RecipeRegistry.loadAll();
        } catch (Throwable t) {
            System.err.println("[配方书] RecipeRegistry 加载失败: " + t);
        }
        int id = 0;
        java.util.Set<String> seenResults = new java.util.HashSet<>();
        for (RecipeRegistry.Recipe r : RecipeRegistry.all()) {
            RECIPE_BOOK_ENTRIES.add(r);
            String rn = r.resultItemId > 0 ? BlockManager.itemIdToName(r.resultItemId) : null;
            if (rn != null && seenResults.add(rn)) {
                BOOK_DISPLAY_TO_RECIPE.add(id);
                BOOK_REPRESENTATIVE_INDICES.add(id);
                RECIPE_BOOK_BY_ID.put(BOOK_DISPLAY_TO_RECIPE.size() - 1, r);
            }
            id++;
        }
        System.out.println("[配方书] 已索引 " + id + " 个合成配方, 去重后 "
            + BOOK_DISPLAY_TO_RECIPE.size() + " 个配方书展示条目");
    }

    /** BUG2: 每个玩家已解锁的配方(配方书 displayId 集合)。拿到对应物品才解锁, 模拟原版。 */
    private final java.util.Set<Integer> unlockedRecipes = java.util.concurrent.ConcurrentHashMap.newKeySet();

    // 食物 nutrition 查表(来自 json/1.21.11/foods.json 的 foodPoints), 进食/饥饿修正以原版为准。
    private static final java.util.Map<String, Integer> FOOD_POINTS = new java.util.HashMap<>();
    static {
        try (java.io.FileReader fr = new java.io.FileReader("json/1.21.11/foods.json")) {
            com.google.gson.JsonArray arr = com.google.gson.JsonParser.parseReader(fr).getAsJsonArray();
            for (com.google.gson.JsonElement e : arr) {
                com.google.gson.JsonObject o = e.getAsJsonObject();
                FOOD_POINTS.put(o.get("name").getAsString(), (int) o.get("foodPoints").getAsDouble());
            }
        } catch (Exception ignored) { /* 回退到 getFoodValue 的手工 switch */ }
    }

    private int[] craftingCounts(int windowId) {
        return openCraftingCounts.computeIfAbsent(windowId, k -> new int[9]);
    }
    private final java.util.Map<Integer, ContainerStore.Pos> openChests = new java.util.concurrent.ConcurrentHashMap<>();
    /** Bug51: 大箱子窗口 → 另一半箱子位置(存在 = 该窗口是 generic_9x6 双箱界面)。 */
    private final java.util.Map<Integer, ContainerStore.Pos> openChestPartners = new java.util.concurrent.ConcurrentHashMap<>();
    /** 共享容器观察者: Pos → 正在查看该容器的玩家集(用于多人同箱实时同步)。 */
    private static final java.util.Map<ContainerStore.Pos, java.util.Set<NetworkHandler>> chestObservers = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<Integer, ContainerStore.Pos> openHoppers = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<Integer, java.util.UUID> openEnderChests = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<Integer, ContainerStore.Pos> openFurnaces = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<Integer, ContainerStore.Pos> openDispensers = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<Integer, ContainerStore.Pos> openStonecutters = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<Integer, ContainerStore.Pos> openGrindstones = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<Integer, ContainerStore.Pos> openSmithing = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<Integer, ContainerStore.Pos> openEnchanting = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<Integer, ContainerStore.Pos> openAnvil = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<Integer, ContainerStore.Pos> openBrewing = new java.util.concurrent.ConcurrentHashMap<>();
    /** #15 信标: windowId → 信标坐标 (BeaconMenu 打开后由 set_beacon_effect 选效果)。 */
    private final java.util.Map<Integer, ContainerStore.Pos> openBeacons = new java.util.concurrent.ConcurrentHashMap<>();
    /** 当前信标窗口 (set_beacon_effect 无 windowId 字段, 靠此定位)。 */
    private int beaconWindowId = -1;
    /** #29 命令方块: windowId → 坐标; 当前窗口 (update_command_block 无 windowId 字段, 靠 pos 定位)。 */
    private final java.util.Map<Integer, ContainerStore.Pos> openCommandBlocks = new java.util.concurrent.ConcurrentHashMap<>();
    private int commandBlockWindowId = -1;
    /** 村民交易：windowId → 会话（村民 eid / 选中交易 / 容器槽位）。 */
    private final java.util.Map<Integer, MerchantSession> openMerchants = new java.util.concurrent.ConcurrentHashMap<>();
    /** 当前玩家打开的村民交易窗口（select_trade 无 windowId 字段，靠此定位）。 */
    public int merchantWindowId = -1;
    /** 插件自定义容器: windowId → Inventory (plugin.api)。点击/关闭/槽位读写全链路路由。 */
    final java.util.Map<Integer, com.CharunCore.server.plugin.api.Inventory> openPluginMenus =
            new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<String, Long> digStarts = new java.util.concurrent.ConcurrentHashMap<>();

    // ── 插件自定义容器 (plugin.api.Inventory) ────────────────────────────────

    /** 打开插件菜单: 分配 windowId + 0x39 open_screen + 0x12 container_set_content。 */
    public void openPluginInventory(com.CharunCore.server.plugin.api.Inventory inv) {
        if (ctx == null || !ctx.channel().isActive()) return;
        int windowId = nextWindowId();
        openPluginMenus.put(windowId, inv);
        inv.registerOpenWindow(windowId, this);
        sendPluginMenuOpen(windowId, inv);
        sendPluginMenuContent(windowId, inv);
    }

    private void sendPluginMenuOpen(int windowId, com.CharunCore.server.plugin.api.Inventory inv) {
        final int size = inv.getSize();
        String menuType = switch (size) {
            case 9 -> "generic_9x1";
            case 18 -> "generic_9x2";
            case 36 -> "generic_9x4";
            case 45 -> "generic_9x5";
            case 54 -> "generic_9x6";
            default -> "generic_9x3";
        };
        final String title = inv.getTitle();
        sendPacket(ctx, 0x39, pb -> {
            pb.writeVarInt(windowId);
            pb.writeVarInt(com.CharunCore.server.utils.RegistryHelper.menuType(menuType));
            pb.writeAnonymousNbt(org.cloudburstmc.nbt.NbtMap.builder().putString("text", title).build());
        });
    }

    /** 重发菜单内容(0x12)。插件 setItem 后由 Inventory.refresh 调用。 */
    public void refreshPluginMenu(com.CharunCore.server.plugin.api.Inventory inv) {
        for (java.util.Map.Entry<Integer, com.CharunCore.server.plugin.api.Inventory> e
                : openPluginMenus.entrySet()) {
            if (e.getValue() == inv) {
                sendPluginMenuContent(e.getKey(), inv);
                return;
            }
        }
    }

    /** 重发菜单标题(重新 open_screen)。 */
    public void refreshPluginWindowTitle(com.CharunCore.server.plugin.api.Inventory inv) {
        for (java.util.Map.Entry<Integer, com.CharunCore.server.plugin.api.Inventory> e
                : openPluginMenus.entrySet()) {
            if (e.getValue() == inv) {
                sendPluginMenuOpen(e.getKey(), inv);
                return;
            }
        }
    }

    /** 服务端强制关闭插件菜单(0x13 close_container + 状态清理)。 */
    public void closePluginMenu(com.CharunCore.server.plugin.api.Inventory inv) {
        for (java.util.Map.Entry<Integer, com.CharunCore.server.plugin.api.Inventory> e
                : openPluginMenus.entrySet()) {
            if (e.getValue() == inv) {
                int windowId = e.getKey();
                openPluginMenus.remove(windowId);
                if (ctx != null && ctx.channel().isActive()) {
                    sendPacket(ctx, 0x13, pb -> pb.writeByte(0));
                }
                firePluginMenuClose(windowId, inv);
                return;
            }
        }
    }

    private void firePluginMenuClose(int windowId, com.CharunCore.server.plugin.api.Inventory inv) {
        inv.unregisterOpenWindow(windowId, this);
        // 光标物品退回背包(关窗防丢)
        if (carriedItemCount > 0) {
            returnSlotToPlayer(carriedItemId, carriedItemCount, carriedSnapshot());
            carriedItemId = 0;
            carriedItemCount = 0;
            sendCarriedItem();
        }
        inv.fireClose(this);
    }

    /** 0x12 container_set_content 全量下发插件菜单 + 玩家背包区。 */
    private void sendPluginMenuContent(int windowId, com.CharunCore.server.plugin.api.Inventory inv) {
        if (ctx == null || !ctx.channel().isActive()) return;
        final int size = inv.getSize();
        final int total = size + 36;
        final int stateId = ++containerStateCounter;
        sendPacket(ctx, 0x12, pb -> {
            pb.writeVarInt(windowId);
            pb.writeVarInt(stateId);
            pb.writeVarInt(total);
            for (int s = 0; s < size; s++) {
                int id = inv.slotId(s);
                int count = inv.slotCount(s);
                if (id <= 0 || count <= 0) {
                    pb.writeVarInt(0);
                } else {
                    writePluginItemMeta(pb, id, count, inv.slotMeta(s));
                }
            }
            for (int ps = 9; ps < 45; ps++) {
                writePlayerSlotInline(pb, ps);
            }
        });
    }

    private int containerStateCounter = 1;

    /** 插件菜单窗口槽 → 玩家数据槽(菜单区返回 -1)。 */
    private int pluginMenuPlayerSlot(com.CharunCore.server.plugin.api.Inventory inv, int slot) {
        int size = inv.getSize();
        if (slot < size) return -1;
        int rel = slot - size;
        return rel < 27 ? rel + 9 : rel - 27;
    }

    /** 全服广播任意 clientbound 包(插件底层通道 Packets/Entity 使用)。 */
    public static void broadcastAll(int packetId, java.util.function.Consumer<com.CharunCore.server.network.protocol.PacketBuffer> writer) {
        for (NetworkHandler h : players.values()) {
            if (h.ctx == null || !h.ctx.channel().isActive()) continue;
            h.sendPacket(h.ctx, packetId, writer);
        }
    }

    /** 玩家背包第一个空槽(快捷栏+主背包+装备区), 无则 -1。 */
    public int firstEmptyInventorySlot() {
        if (data == null) return -1;
        for (int i = 0; i < 46; i++) {
            if (data.inventoryIds[i] <= 0 || data.inventoryCounts[i] <= 0) return i;
        }
        return -1;
    }

    /** plugin.api.ItemMeta → 内部 ItemMeta(附魔名转 id)。 */
    private ItemMeta toInternalMeta(com.CharunCore.server.plugin.api.ItemMeta pm) {
        if (pm == null || pm.isEmpty()) return ItemMeta.EMPTY;
        java.util.Map<Integer, Integer> ench = new java.util.HashMap<>();
        for (java.util.Map.Entry<String, Integer> e : pm.getEnchants().entrySet()) {
            int id = com.CharunCore.server.utils.BlockManager.getEnchantId(e.getKey());
            if (id > 0) ench.put(id, e.getValue());
        }
        return ItemMeta.of(ench, null,
                pm.hasDisplayName() ? pm.getDisplayName() : null,
                pm.getDamage(), -1, -1);
    }

    /** 内部 ItemMeta → plugin.api.ItemMeta(附魔 id 转名)。 */
    private com.CharunCore.server.plugin.api.ItemMeta fromInternalMeta(ItemMeta m) {
        if (m == null || m.isEmpty()) return null;
        com.CharunCore.server.plugin.api.ItemMeta out = new com.CharunCore.server.plugin.api.ItemMeta();
        if (m.customName() != null && !m.customName().isEmpty()) out.setDisplayName(m.customName());
        if (m.damage() > 0) out.setDamage(m.damage());
        for (java.util.Map.Entry<Integer, Integer> e : m.enchants().entrySet()) {
            String name = com.CharunCore.server.utils.BlockManager.getEnchantName(e.getKey());
            if (name != null) out.addEnchant(name, e.getValue());
        }
        return out;
    }

    /** 玩家背包区(槽 9-44)单槽内联写入。 */
    private void writePlayerSlotInline(com.CharunCore.server.network.protocol.PacketBuffer pb, int ps) {
        int id = data.inventoryIds[ps];
        int count = data.inventoryCounts[ps];
        if (id <= 0 || count <= 0) {
            pb.writeVarInt(0);
        } else if (hasExtSlotMeta(ps)) {
            writePluginItemMeta(pb, id, count, extSlotMeta(ps));
        } else {
            ItemMeta m = playerSlotMeta(ps);
            if (m.isEmpty()) {
                pb.writeSlot(id, count);
            } else {
                writeStackWithMeta(pb, id, count, m);
            }
        }
    }

    /** 玩家槽位是否带扩展组件(lore/unbreakable/glint)。 */
    private boolean hasExtSlotMeta(int ps) {
        return data != null && ps >= 0 && ps < 46
                && ((data.inventoryLore[ps] != null && !data.inventoryLore[ps].isEmpty())
                    || data.inventoryUnbreakable[ps] || data.inventoryGlint[ps] != 0);
    }

    /** data 三数组 + 内部组件 → 插件 ItemMeta(发送与序列化统一入口)。 */
    private com.CharunCore.server.plugin.api.ItemMeta extSlotMeta(int ps) {
        com.CharunCore.server.plugin.api.ItemMeta pm = new com.CharunCore.server.plugin.api.ItemMeta();
        if (data.inventoryCustomName[ps] != null) pm.setDisplayName(data.inventoryCustomName[ps]);
        if (data.inventoryDamage[ps] > 0) pm.setDamage(data.inventoryDamage[ps]);
        if (data.inventoryLore[ps] != null) {
            for (String line : data.inventoryLore[ps].split("\n", -1)) pm.addLoreLine(line);
        }
        pm.setUnbreakable(data.inventoryUnbreakable[ps]);
        pm.setGlintOverride(data.inventoryGlint[ps] == 0 ? null : data.inventoryGlint[ps] > 0);
        if (data.inventoryEnchants[ps] != null) {
            for (java.util.Map.Entry<Integer, Integer> e : data.inventoryEnchants[ps].entrySet()) {
                String name = com.CharunCore.server.utils.BlockManager.getEnchantName(e.getKey());
                if (name != null) pm.addEnchant(name, e.getValue());
            }
        }
        return pm;
    }

    /**
     * 插件 ItemStack 组件完整写入 (plugin.api.ItemMeta → 774 组件)。
     * 组件表(protocol.json SlotComponentType): 3=damage 4=unbreakable 6=custom_name
     * 11=lore 13=enchantments 21=enchantment_glint_override。
     */
    private void writePluginItemMeta(com.CharunCore.server.network.protocol.PacketBuffer pb,
                                     int id, int count,
                                     com.CharunCore.server.plugin.api.ItemMeta meta) {
        if (count <= 0 || id <= 0) { pb.writeVarInt(0); return; }
        boolean hasName = meta != null && meta.hasDisplayName();
        boolean hasLore = meta != null && meta.hasLore();
        boolean hasEnch = meta != null && meta.hasEnchants();
        boolean hasDamage = meta != null && meta.getDamage() > 0;
        boolean hasUnbreak = meta != null && meta.isUnbreakable();
        boolean hasGlint = meta != null && meta.getGlintOverride() != null;
        int nSet = (hasName ? 1 : 0) + (hasLore ? 1 : 0) + (hasEnch ? 1 : 0)
                + (hasDamage ? 1 : 0) + (hasUnbreak ? 1 : 0) + (hasGlint ? 1 : 0);
        pb.writeVarInt(count);
        pb.writeVarInt(id);
        pb.writeVarInt(nSet);
        pb.writeVarInt(0);
        if (hasDamage) {
            pb.writeVarInt(3);
            pb.writeVarInt(meta.getDamage());
        }
        if (hasUnbreak) {
            pb.writeVarInt(4);
            pb.writeAnonymousNbt(org.cloudburstmc.nbt.NbtMap.builder().build());
        }
        if (hasName) {
            pb.writeVarInt(6);
            pb.writeAnonymousNbt(org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", meta.getDisplayName()).build());
        }
        if (hasLore) {
            java.util.List<String> lore = meta.getLore();
            pb.writeVarInt(11);
            pb.writeVarInt(lore.size());
            for (String line : lore) {
                pb.writeAnonymousNbt(org.cloudburstmc.nbt.NbtMap.builder()
                        .putString("text", line).build());
            }
        }
        if (hasEnch) {
            pb.writeVarInt(13);
            pb.writeVarInt(meta.getEnchants().size());
            for (java.util.Map.Entry<String, Integer> e : meta.getEnchants().entrySet()) {
                int enchId = com.CharunCore.server.utils.BlockManager.getEnchantId(e.getKey());
                pb.writeVarInt(Math.max(0, enchId));
                pb.writeVarInt(e.getValue());
            }
        }
        if (hasGlint) {
            pb.writeVarInt(21);
            pb.writeBoolean(meta.getGlintOverride());
        }
    }

    /** 村民交易会话状态。容器槽布局：0,1=结果 2,3=输入1 4,5=输入2。 */
    private static final class MerchantSession {
        final int villagerEid;
        int selectedTrade = 0;
        final int[] slots = new int[6]; // [resultId,resultCount, in1Id,in1Count, in2Id,in2Count]
        int version = 0;
        MerchantSession(int villagerEid) { this.villagerEid = villagerEid; }
    }

    private final java.util.Map<Integer, Integer> containerSyncVersion = new java.util.concurrent.ConcurrentHashMap<>();
    public DimensionType currentDim = DimensionType.OVERWORLD;

    public UUID uuid;
    public String username;
    public double x, y, z;
    public float yaw, pitch;
    public int eid;

    private volatile int compressionThreshold = -1;
    public java.util.List<MojangProfileService.ProfileProperty> profileProperties = java.util.List.of();
    private java.security.KeyPair loginKeyPair;
    private byte[] loginChallenge;
    private volatile long lastKeepaliveResponse = System.currentTimeMillis();

    private static final Gson GSON = new Gson();
    public PlayerData data;
    private int lastChunkX = Integer.MAX_VALUE, lastChunkZ = Integer.MAX_VALUE;
    private final java.util.Set<Long> loadedChunks =
            new ConcurrentHashMap<Long, Boolean>().keySet(true);
    /** Bug44: 待发送区块队列(每 tick 限量泵送), queuedChunkKeys 防重复入队。 */
    final java.util.concurrent.ConcurrentLinkedDeque<long[]> pendingChunkSends =
            new java.util.concurrent.ConcurrentLinkedDeque<>();
    private final java.util.Set<Long> queuedChunkKeys = ConcurrentHashMap.newKeySet();
    private final int VIEW_DISTANCE = ServerConfig.viewDistance;
    public float health = 20.0f;

    public int gameMode = 1; // 默认创造模式
    public boolean onGround = true;
    public double lastY = 75.0;
    public double lastX = 0.0, lastZ = 0.0;
    public boolean wasOnGround = true;
    public boolean sprinting = false;
    private int foodTickTimer = 0;
    private int starveTimer = 0;
    public boolean isDead = false;
    private float fallDistance = 0.0f;
    // invulnTicks：简单无敌帧，防连续击杀
    private int invulnTicks = 0;
    /** 环境伤害(溺水/岩浆/火/窒息/仙人掌等)按每秒施加, 累计到 20 tick 触发一次, 原版每秒而非每 tick。 */
    private int envDamageTimer = 0;
    /** 着火剩余刻数: >0 时持续每秒灼伤, 离开火/岩浆后仍继续(原版 fireTicks)。插件可读写。 */
    public int fireTicks = 0;
    /** 当前生效的状态效果: key=效果名(去掉 minecraft: 前缀), value={放大器, 剩余刻数} */
    private final java.util.Map<String,int[]> activeEffects = new java.util.HashMap<>();
    private int effectTickCounter = 0;
    /** #11 信标效果周期重施(每 4 秒), 保证玩家在范围内持续获得 buff。 */
    private int beaconTickTimer = 0;
    /** 上次发送给客户端的护甲键值, 用于变化时重发 UPDATE_ATTRIBUTES。 */
    private int lastSentArmorKey = -1;
    /** 无敌帧窗口内已承受的伤害量, 用于原版"更强伤害可穿透"语义。 */
    private float lastDamageAmount = 0.0f;
    /** 本次死亡是否已执行掉落, 防止多条死亡路径重复掉落。 */
    private boolean deathDropsDone = false;
    /** 死亡事件决定的 keepInventory 结果(默认跟随 gamerule)。 */
    private boolean deathKeepInventory = false;

    // Atomic counter prevents duplicate entity IDs during concurrent logins
    private static final java.util.concurrent.atomic.AtomicInteger nextEid = new java.util.concurrent.atomic.AtomicInteger(2000);
    private long lastAttackTime = 0L;
    public long lastDamageTime = 0L;
    public String lastDamageType = null;
    private int portalTimer = 0;
    /** 弓开始拉弦的时间戳 (0 = 未拉弓)。 */
    private long bowChargeStart = 0L;
    /** 弩开始蓄力的时间戳 (0 = 未蓄力)。 */
    private long crossbowChargeStart = 0L;
    /** 是否正在使用物品 (拉弓/吃东西), 用于同步实体元数据。 */
    private boolean usingItem = false;
    /** 是否举盾格挡。 */
    private boolean isBlocking = false;
    public boolean godMode = false;
    public boolean isAfk = false;
    public double lastDeathX = 0, lastDeathY = 0, lastDeathZ = 0;
    public long lastPingTime = System.currentTimeMillis();
    public boolean allowFlight = false;
    /** 客户端飞行状态(abilities 0x02)。插件经 Player.setFlying 控制。 */
    public boolean flying = false;
    public float flySpeed = 0.05f;
    public float walkSpeed = 0.1f;

    /** 吸收心当前点数(absorption 效果); best-effort 下经 max_absorption 属性近似同步。 */
    private float absorption = 0.0f;
    /** 当前最大生命值(受 health_boost 效果影响), 随属性包 id19 下发。 */
    private float maxHealth = 20.0f;
    /** 玩家稳定附魔种子: 物品与书架确定后选项固定, 仅一次附魔后重掷(原版 enchantmentSeed)。 */
    private long enchantSeed = new java.util.Random().nextLong();

    // ── NEW FIELDS ────────────────────────────────────────────────────────────

    /** Which hotbar slot is currently selected (0-8). Updated by SetCarriedItem. */
    public short heldItemSlot = 0;

    /** Sneak state; used when broadcasting entity metadata to others. 插件可读。 */
    public boolean isSneaking = false;

    /** Elytra fall-flying state; set by client start_elytra_flying action. */
    private boolean fallFlying = false;

    private int carriedItemId = 0;
    private int carriedItemCount = 0;
    /** 当前抛出的钓鱼浮标 entity id(-1 表示未钓鱼)。 */
    public int fishingBobberEid = -1;
    /** 当前骑乘的实体(矿车等)；null 表示未骑乘。 */
    public Entity riddenEntity = null;
    /** 光标物品携带的附魔/药水元数据(随拿起/放下在槽位间转移)。 */
    private java.util.Map<Integer, Integer> carriedEnchants = new java.util.HashMap<>();
    private String carriedPotionType = null;
    /** 光标物品自定义名称(铁砧改名后随结果转移到光标)。 */
    private String carriedCustomName = null;
    /** 光标物品已损失耐久(随拿起/放下在槽位间转移)。 */
    private int carriedDamage = 0;
    /** #19 光标物品盔甲纹饰(trim 组件): material/pattern 注册 id (-1=无)。 */
    private int carriedTrimMaterial = -1;
    private int carriedTrimPattern = -1;

    /** Bug4/33: 槽位/光标/掉落物通用的物品组件快照, 随物品移动而不再依赖各处手工拷贝。 */
    public record ItemMeta(java.util.Map<Integer, Integer> enchants, String potion, String customName,
                           int damage, int trimMaterial, int trimPattern) {
        public static final ItemMeta EMPTY =
                new ItemMeta(java.util.Map.of(), null, null, 0, -1, -1);
        public boolean isEmpty() {
            return damage == 0 && trimMaterial < 0 && trimPattern < 0
                    && (potion == null || potion.isEmpty())
                    && (customName == null || customName.isEmpty())
                    && (enchants == null || enchants.isEmpty());
        }
        public static ItemMeta of(java.util.Map<Integer, Integer> enchants, String potion,
                                  String customName, int damage, int trimM, int trimP) {
            ItemMeta m = new ItemMeta(enchants == null ? java.util.Map.of() : enchants,
                    potion, customName, damage, trimM, trimP);
            return m.isEmpty() ? EMPTY : m;
        }
    }

    private ItemMeta carriedSnapshot() {
        return ItemMeta.of(carriedEnchants, carriedPotionType, carriedCustomName,
                carriedDamage, carriedTrimMaterial, carriedTrimPattern);
    }

    private void loadCarriedFrom(ItemMeta m) {
        if (m == null) m = ItemMeta.EMPTY;
        carriedEnchants = m.enchants().isEmpty() ? new java.util.HashMap<>() : new java.util.HashMap<>(m.enchants());
        carriedPotionType = m.potion();
        carriedCustomName = m.customName();
        carriedDamage = m.damage();
        carriedTrimMaterial = m.trimMaterial();
        carriedTrimPattern = m.trimPattern();
    }

    private void clearCarriedMeta() {
        carriedEnchants = new java.util.HashMap<>();
        carriedPotionType = null;
        carriedCustomName = null;
        carriedDamage = 0;
        carriedTrimMaterial = -1;
        carriedTrimPattern = -1;
    }

    // ─────────────────────────────────────────────────────────────────────────

    public ChannelHandlerContext ctx;
    private ConnectionState state = ConnectionState.HANDSHAKE;
    // =========================================================================
    // CHANNEL LIFECYCLE
    // =========================================================================

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        super.channelActive(ctx);
    }

    /** 客户端异常断线(强退/断网/宿主软件中止连接)时 Netty 会把 IOException 抛到
     *  pipeline 尾部打整段 WARN 堆栈。这里安静地关闭通道走正常 channelInactive 清理流程。 */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof java.io.IOException) {
            ctx.close();
            return;
        }
        System.err.println("[网络] " + (username == null ? "未知连接" : username)
            + " 连接异常: " + cause);
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (this.uuid != null) {
            // 1. 保存最终位置
            if (data != null) { data.x = x; data.y = y; data.z = z; }
            savePlayerData();
            // 成就系统：清理内存态 (P12)
            AdvancementManager.onLogout(this.uuid);

            // 2. 先从 players Map 中移除自己（用原子操作防止并发修改）
            NetworkHandler removed = players.remove(this.uuid);
            if (removed == null) {
                // Already removed by another thread — skip
                super.channelInactive(ctx);
                return;
            }
            System.out.println("[网络] 玩家 " + username + " 退出，在线: " + players.size());

            EntityManager.onPlayerDisconnect(this);
            EndDragonFight.onPlayerDisconnect(this);
            TpaSystem.onQuit(this);

            // 清理钓鱼浮标(若有), 避免实体泄漏
            if (fishingBobberEid >= 0) {
                EntityManager.removeEntity(fishingBobberEid);
                fishingBobberEid = -1;
            }

            // 清理骑乘状态(若有), 避免矿车残留乘客引用
            if (this.riddenEntity instanceof MinecartEntity rc) {
                rc.passengerEid = -1;
                rc.throttle = 0;
                rc.speed = 0.0;
                this.riddenEntity = null;
            }

            // 清理共享容器观察者, 避免泄漏
            for (ContainerStore.Pos cp : this.openChests.values()) {
                java.util.Set<NetworkHandler> obs = chestObservers.get(cp);
                if (obs != null) { obs.remove(this); if (obs.isEmpty()) chestObservers.remove(cp); }
            }

            // 3. 广播离开消息
            var quitEvent = EVENTS.fire(new PlayerQuitEvent(
                    this, username + " 退出了游戏"));
            if (!quitEvent.getQuitMessage().isEmpty()) {
                broadcastSystemMessage(quitEvent.getQuitMessage(), "yellow");
            }

            // 4. 复制快照并通知其他客户端
            java.util.List<NetworkHandler> others = new java.util.ArrayList<>(players.values());
            for (NetworkHandler h : others) {
                if (h.ctx == null || h == this) continue;
                // Remove from tab list (0x43 = Player Info Remove)
                h.sendPacket(h.ctx, 0x43, pb -> { pb.writeVarInt(1); pb.writeUUID(this.uuid); });
                // Despawn entity (0x4B = Remove Entities)
                h.sendPacket(h.ctx, 0x4b, pb -> { pb.writeVarInt(1); pb.writeVarInt(this.eid); });
            }
        }
        super.channelInactive(ctx);
    }

    // =========================================================================
    // PACKET DISPATCH
    // =========================================================================

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        if (!msg.isReadable()) return;
        PacketBuffer in = new PacketBuffer(msg);
        int id = in.readVarInt();
        //System.out.println("[C->S] 0x" + Integer.toHexString(id) + " | " + state);

        switch (state) {
            case HANDSHAKE -> handleHandshake(ctx, in, id);
            case LOGIN     -> handleLogin(ctx, in, id);
            case CONFIG    -> handleConfig(ctx, in, id);
            case PLAY      -> handlePlay(ctx, in, id);
            case STATUS    -> handleStatus(ctx, in, id);
        }
    }

    // =========================================================================
    // HANDSHAKE / LOGIN / CONFIG / STATUS
    // =========================================================================

    private void handleHandshake(ChannelHandlerContext ctx, PacketBuffer in, int id) {
        if (id == 0x00) {
            in.readVarInt(); in.readString(); in.readUnsignedShort();
            state = (in.readVarInt() == 2) ? ConnectionState.LOGIN : ConnectionState.STATUS;
        }
    }

    private void handleLogin(ChannelHandlerContext ctx, PacketBuffer in, int id) {
        if (id == 0x00) {
            this.username = in.readString();
            if (username == null || !username.matches("[a-zA-Z0-9_]{1,16}")) {
                sendLoginDisconnect(ctx, "Invalid username");
                return;
            }
            if (players.size() >= ServerConfig.maxPlayers) {
                sendLoginDisconnect(ctx, "The server is full!");
                return;
            }
            if (ServerConfig.onlineMode) {
                loginKeyPair = CryptHelper.generateKeyPair();
                loginChallenge = new byte[16];
                new java.security.SecureRandom().nextBytes(loginChallenge);
                byte[] encodedKey = CryptHelper.encodePublicKey(loginKeyPair.getPublic());
                sendPacket(ctx, 0x01, pb -> {
                    pb.writeString("");
                    pb.writeByteArray(encodedKey);
                    pb.writeByteArray(loginChallenge);
                    pb.writeBoolean(true);
                });
            } else if (ServerConfig.uuidFix) {
                MojangProfileService.lookup(username).thenAccept(opt -> ctx.executor().execute(() -> {
                    if (state != ConnectionState.LOGIN) return;
                    if (opt.isPresent()) {
                        MojangProfileService.ResolvedProfile profile = opt.get();
                        this.uuid = profile.uuid();
                        this.username = profile.name();
                        this.profileProperties = profile.properties();
                    } else {
                        this.uuid = offlineUuid(username);
                    }
                    finishLoginHandshake(ctx);
                }));
            } else {
                this.uuid = offlineUuid(username);
                finishLoginHandshake(ctx);
            }
        } else if (id == 0x01) {
            handleEncryptionResponse(ctx, in);
        } else if (id == 0x03) {
            state = ConnectionState.CONFIG;
            sendConfigPackets(ctx);
        }
    }

    private void handleEncryptionResponse(ChannelHandlerContext ctx, PacketBuffer in) {
        if (loginKeyPair == null) { ctx.close(); return; }
        byte[] encSecret = readByteArray(in);
        byte[] encChallenge = readByteArray(in);
        try {
            javax.crypto.Cipher rsa = javax.crypto.Cipher.getInstance("RSA/ECB/PKCS1");
            rsa.init(javax.crypto.Cipher.DECRYPT_MODE, loginKeyPair.getPrivate());
            byte[] secret = rsa.doFinal(encSecret);
            byte[] challenge = rsa.doFinal(encChallenge);
            if (!java.util.Arrays.equals(challenge, loginChallenge)) {
                sendLoginDisconnect(ctx, "Encryption handshake failed (challenge mismatch)");
                return;
            }
            ctx.pipeline().addFirst(new MinecraftCipher(secret));
            String serverHash = CryptHelper.serverIdHash(
                    CryptHelper.encodePublicKey(loginKeyPair.getPublic()), secret);
            String loginName = this.username;
            MojangProfileService.hasJoined(loginName, serverHash).thenAccept(opt -> ctx.executor().execute(() -> {
                if (state != ConnectionState.LOGIN) return;
                if (opt.isEmpty()) {
                    sendLoginDisconnect(ctx, "Failed to verify username (session authentication failed)");
                    return;
                }
                MojangProfileService.ResolvedProfile profile = opt.get();
                this.uuid = profile.uuid();
                this.username = profile.name();
                this.profileProperties = profile.properties();
                finishLoginHandshake(ctx);
            }));
        } catch (Exception e) {
            System.err.println("[登录] 加密握手异常: " + e.getMessage());
            sendLoginDisconnect(ctx, "Encryption handshake failed");
        }
    }

    private void finishLoginHandshake(ChannelHandlerContext ctx) {
        String banReason = BanList.checkBanned(uuid, username);
        if (banReason != null) {
            System.out.println("[登录] 拒绝封禁玩家 " + username + ": " + banReason);
            sendLoginDisconnect(ctx, "你已被封禁: " + banReason);
            return;
        }
        NetworkHandler existing = players.get(uuid);
        if (existing != null && existing != this
                && existing.ctx != null && existing.ctx.channel().isActive()) {
            System.out.println("[登录] " + username + " 重复登录, 踢出旧连接");
            existing.ctx.close();
        }
        int threshold = ServerConfig.compressionThreshold;
        if (threshold >= 0) {
            sendPacket(ctx, 0x03, pb -> pb.writeVarInt(threshold));
            compressionThreshold = threshold;
            MinecraftFrameDecoder decoder = ctx.pipeline().get(MinecraftFrameDecoder.class);
            if (decoder != null) decoder.enableCompression(threshold);
        }
        java.util.List<MojangProfileService.ProfileProperty> props = profileProperties;
        sendPacket(ctx, 0x02, pb -> {
            pb.writeUUID(uuid);
            pb.writeString(username);
            writeProfileProperties(pb, props);
        });
        System.out.println("[登录] " + username + " UUID=" + uuid
                + (props.isEmpty() ? " (离线档案)" : " (正版档案, 含皮肤属性)"));
    }

    private void sendLoginDisconnect(ChannelHandlerContext ctx, String message) {
        String json = "{\"text\":" + GSON.toJson(message) + "}";
        sendPacket(ctx, 0x00, pb -> pb.writeString(json));
        ctx.executor().schedule(() -> ctx.close(), 250, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public static UUID offlineUuid(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name)
                .getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    private static byte[] readByteArray(PacketBuffer in) {
        int len = in.readVarInt();
        byte[] data = new byte[len];
        in.getBuffer().readBytes(data);
        return data;
    }

    private static void writeProfileProperties(PacketBuffer pb, java.util.List<MojangProfileService.ProfileProperty> props) {
        pb.writeVarInt(props.size());
        for (MojangProfileService.ProfileProperty p : props) {
            pb.writeString(p.name());
            pb.writeString(p.value());
            if (p.signature() != null) {
                pb.writeBoolean(true);
                pb.writeString(p.signature());
            } else {
                pb.writeBoolean(false);
            }
        }
    }

    private void handleConfig(ChannelHandlerContext ctx, PacketBuffer in, int id) {
        if (id == 0x03) {
            state = ConnectionState.PLAY;
            sendLoginPlay(ctx);
        }
    }

    private void handleStatus(ChannelHandlerContext ctx, PacketBuffer in, int id) {
        if (id == 0x00) {
            var pingEvent = EVENTS.fire(new ServerListPingEvent(
                    ServerConfig.motd, ServerConfig.maxPlayers,
                    players.size(), ctx.channel().remoteAddress() == null ? "" : ctx.channel().remoteAddress().toString()));
            String json = "{\"version\":{\"name\":\"1.21.11\",\"protocol\":774},"
                    + "\"players\":{\"max\":" + pingEvent.getMaxPlayers()
                    + ",\"online\":" + pingEvent.getOnlinePlayers() + ",\"sample\":[]},"
                    + "\"description\":{\"text\":" + GSON.toJson(pingEvent.getMotd()) + "},"
                    + "\"enforcesSecureChat\":false}";
            sendPacket(ctx, 0x00, pb -> pb.writeString(json));
        } else if (id == 0x01) {
            long t = in.readLong();
            sendPacket(ctx, 0x01, pb -> pb.writeLong(t));
        }
    }

    // =========================================================================
    // PLAY PACKET HANDLER
    // =========================================================================

    private void handlePlay(ChannelHandlerContext ctx, PacketBuffer in, int id) {
        // 死亡状态过滤：只允许重生请求、keep-alive、 teleport confirm
        if (this.isDead) {
            if (id == 0x0B) { /* client_command: respawn handled below */ }
            else if (id == 0x00 || id == 0x1B) { /* keep-alive / teleport confirm */ }
            else { return; }
        }

        if (id == 0x00) {
            // Teleport confirm — ignore
        }

        else if (id == 0x0B) { // Client Command (客户端请求操作)
            int action = in.readVarInt();
            if (action == 0 && this.isDead) { // 0 = Perform Respawn
                this.isDead = false;
                this.health = 20.0f;
                this.data.food = 20;
                this.data.saturation = 5.0f;
                this.data.exhaustion = 0.0f;
                this.data.airTicks = 300;
                // keepInventory=true: 复活保留经验(与死亡掉落守卫一致)
                if (!"true".equals(WorldManager.getGameRule("keepInventory"))) {
                    this.data.xpLevel = 0;
                    this.data.xpTotal = 0;
                    this.data.xpProgress = 0.0f;
                }
                this.fallDistance = 0.0f;
                this.deathDropsDone = false;
                this.invulnTicks = 0;
                this.fireTicks = 0;
                this.lastDamageAmount = 0.0f;
                this.bowChargeStart = 0L;

                // 0. 计算复活维度: 已设置复活点(主世界床/下界重生锚)则使用其维度; 否则用主世界出生点
                DimensionType respawnDim;
                if (data.respawnY != Integer.MIN_VALUE) {
                    respawnDim = DimensionType.byKey(data.respawnDimension);
                } else {
                    respawnDim = DimensionType.OVERWORLD;
                }
                // 若复活维度与死亡时所在维度不同, 需切换服务端维度实体归属
                if (this.currentDim != respawnDim) {
                    EntityManager.onPlayerChangeDimension(this);
                    this.currentDim = respawnDim;
                }
                // 重生锚消耗 1 点电荷; 耗尽则复活点失效
                if (respawnDim == DimensionType.THE_NETHER
                        && data.respawnY != Integer.MIN_VALUE) {
                    int as = WorldManager.getBlockState(respawnDim, data.respawnX, data.respawnY, data.respawnZ);
                    if ("respawn_anchor".equals(BlockStateHelper.getName(as))) {
                        String ac = BlockStateHelper.getProp(as, "charges");
                        int acv = ac == null ? 0 : Integer.parseInt(ac);
                        if (acv > 0) {
                            int ns = BlockStateHelper.withProp(as, "charges", String.valueOf(acv - 1));
                            WorldManager.setBlock(respawnDim, data.respawnX, data.respawnY, data.respawnZ, ns);
                            broadcastBlockChange(respawnDim, data.respawnX, data.respawnY, data.respawnZ, ns);
                        }
                        if (acv - 1 <= 0) data.respawnY = Integer.MIN_VALUE;
                    }
                }
                this.portalTimer = -300;

                // 1. 发送 Respawn (0x50) — CommonPlayerSpawnInfo + dataKept
                sendPacket(ctx, 0x50, pb -> {
                    pb.writeVarInt(respawnDim.registryId);
                    pb.writeString(respawnDim.key);
                    pb.writeLong(WorldManager.getSeed()); // hashedSeed
                    pb.writeByte((byte) this.gameMode); // gamemode
                    pb.writeByte((byte) -1);        // previousGamemode
                    pb.writeBoolean(false);          // isDebug
                    pb.writeBoolean(false);          // isFlat
                    pb.writeBoolean(false);          // death location present
                    pb.writeVarInt(0);               // portalCooldown
                    pb.writeVarInt(respawnDim.seaLevel); // seaLevel
                    pb.writeByte((byte) 0x00);       // dataKept = 0 (死亡重生不保留属性/元数据)
                });

                // 2. 满血复活 (update_health/set_health = 0x66; 0x67=set_held_slot —— 曾误用 0x67)
                sendPacket(ctx, 0x66, pb -> {
                    pb.writeFloat(20.0f); pb.writeVarInt(20); pb.writeFloat(5.0f);
                });

                // 2b. 重置经验
                sendExperienceUpdate();

                // 3. 传送到复活点并重新加载地形
                double rx, ry, rz;
                if (data.respawnY != Integer.MIN_VALUE) {
                    rx = data.respawnX + 0.5; rz = data.respawnZ + 0.5;
                    ry = findSafeArrivalY(respawnDim, data.respawnX, data.respawnZ);
                } else {
                    double[] sp = WorldManager.resolveWorldSpawn();
                    rx = sp[0]; rz = sp[2];
                    ry = findSafeArrivalY(respawnDim, (int) Math.floor(rx), (int) Math.floor(rz));
                }
                var respawnEvent = EVENTS.fire(new PlayerRespawnEvent(this, rx, ry, rz));
                rx = respawnEvent.getX(); ry = respawnEvent.getY(); rz = respawnEvent.getZ();
                teleportPlayer(this, rx, ry, rz);

                // 3b. 关键修复：死亡重生后必须发送 Game Event 13 (LEVEL_CHUNKS_LOAD_START)，
                // 否则客户端 ClientLevel 的 LevelLoadStatusManager 不会进入"等待玩家区块编译"状态，
                // 加载地形界面永远不会关闭。登录流程在 sendLoginPlay 末尾已发送同样的 0x26 包。
                // 包格式：writeByte(event=13) + writeFloat(param=0.0f)。
                sendPacket(ctx, 0x26, pb -> { pb.writeByte(13); pb.writeFloat(0.0f); });
                // SetTime：保证重生维度的世界时间正确（用真实 dayTime，非固定 6000，避免天空闪）
                sendPacket(ctx, 0x6F, pb -> { pb.writeLong(Main.worldAge); pb.writeLong(Main.dayTime); pb.writeBoolean(true); });
                // 【修复】重生后必须重新发送 abilities，否则客户端丢失权限等级，
                // 导致 F3+F4 游戏模式切换器显示"没有权限"（命令仍可走服务端校验）。
                sendAbilitiesUpdate();
                // 重生后向自己重发 player_info_update(UPDATE_GAMEMODE=0x04)。
                // 客户端 LocalPlayer.getPermissionLevel() 依赖此包刷新 gamemode -> 创造模式=2 -> F3+F4 可用;
                // 仅发 Respawn 包不够, 客户端 tab 列表缓存的 gamemode 未刷新会导致 hasPermissions 判定滞后。
                // #1 修复: 原版 PlayerList.broadcastAllPlayerInfo 在重生时重发完整 ADD_PLAYER 条目
                // (0x01|0x04|0x08 = ADD_PLAYER|UPDATE_GAME_MODE|UPDATE_LISTED, 含 username/properties/gamemode),
                // 仅单发 UPDATE_GAME_MODE(0x04) 客户端 tab 列表重建时权限等级判定仍可能滞后。
                final int fgm = this.gameMode;
                for (NetworkHandler h : players.values()) {
                    if (h.ctx == null || !h.ctx.channel().isActive()) continue;
                    h.sendPacket(h.ctx, 0x44, pb -> {
                        pb.writeByte(0x01 | 0x04 | 0x08);
                        pb.writeVarInt(1); pb.writeUUID(this.uuid); pb.writeString(this.username);
                        writeProfileProperties(pb, this.profileProperties);
                        pb.writeVarInt(fgm);
                        pb.writeBoolean(true);
                    });
                }
                // #1 强化: 补发 game_state(0x26 reason=3 change_game_mode) 强制客户端刷新
                // LocalPlayer 的 gamemode 缓存(F3+F4 权限判定直接读它; 重生后仅 ADD_PLAYER 可能不够)。
                sendPacket(ctx, 0x26, pb -> { pb.writeByte(3); pb.writeFloat(fgm); });

                // #1 修复: 1.21.5+ 客户端 LocalPlayer 的权限集(PermissionSet)由 declare_commands(0x10)
                // 命令树中的 restricted 节点派生; 重生时客户端会重建 LocalPlayer/权限态, 必须重发命令树,
                // 否则 F3+F4 切换器权限丢失("没有权限打开游戏模式切换器", 重进游戏才恢复)。
                sendCommandsPacket(ctx);

                // #1 修复(关键): 客户端权限等级的唯一来源是 entity_event 24+等级
                // (LocalPlayer.handleEntityEvent: eventId 24..28 -> setPermissionLevel(0..4))。
                // 登录时已发过一次(见 sendLoginPlay 的 writeByte(28)); 重生后客户端重建
                // LocalPlayer, 权限态清零且不会从 player_info_update/命令树恢复 ->
                // F3+F4 报"没有权限打开游戏模式切换器", 重进游戏才好。此处按真实 OP 等级重发。
                final int fOpLevel = Math.max(0, Math.min(4, opLevel()));
                sendPacket(ctx, 0x22, pb -> {
                    pb.writeInt(this.eid);
                    pb.writeByte((byte) (24 + fOpLevel));
                });

                // 4. 【修复】重生后需要重新加载周围的chunk数据，否则客户端会无限加载
                ctx.executor().execute(() -> {
                    resetChunkSendQueue();
                    sendInitialChunks(ctx);
                });
            }
        }

        // ── Keep-alive ───────────────────────────────────────────────────────
        else if (id == 0x1B) {
            lastKeepaliveResponse = System.currentTimeMillis();
        }

        // ── Movement ─────────────────────────────────────────────────────────
        else if (id == 0x1D) { // move_player_pos
            double rx = in.getBuffer().readDouble();
            double ry = in.getBuffer().readDouble();
            double rz = in.getBuffer().readDouble();
            this.onGround = (in.getBuffer().readByte() & 1) != 0;
            // Bug34: 拒绝非有限/越界坐标 —— 曾 NaN 直接进 x/y/z 并被存进 playerdata,
            // 客户端被传送到 NaN 后物理坏死(F5 看不见自己/无法移动/相机错位), 重进仍坏。
            if (validPlayerPos(rx, ry, rz) && this.riddenEntity == null) { x = rx; y = ry; z = rz; handleMove(); }
        }
        else if (id == 0x1E) { // move_player_pos_rot
            double rx = in.getBuffer().readDouble();
            double ry = in.getBuffer().readDouble();
            double rz = in.getBuffer().readDouble();
            float ryaw = in.getBuffer().readFloat();
            float rpitch = in.getBuffer().readFloat();
            this.onGround = (in.getBuffer().readByte() & 1) != 0;
            if (validPlayerPos(rx, ry, rz) && this.riddenEntity == null) {
                x = rx; y = ry; z = rz;
                if (Float.isFinite(ryaw)) yaw = ryaw;
                if (Float.isFinite(rpitch)) pitch = rpitch;
                handleMove();
            }
        }
        else if (id == 0x1F) { // move_player_rot
            float ryaw = in.getBuffer().readFloat();
            float rpitch = in.getBuffer().readFloat();
            this.onGround = (in.getBuffer().readByte() & 1) != 0;
            if (Float.isFinite(ryaw) && Float.isFinite(rpitch)) { yaw = ryaw; pitch = rpitch; broadcastMove(); }
        }
        else if (id == 0x20) { // move_player_status_only (只报着地状态)
            this.onGround = (in.getBuffer().readByte() & 1) != 0;
        }
        else if (id == 0x2a) { // #36 player_input: 1 字节位掩码(bit0=forward bit1=backward bit2=left bit3=right bit4=jump bit5=shift/sneak bit6=sprint)
            int flags = in.getBuffer().readUnsignedByte();
            boolean forward = (flags & 1) != 0;
            boolean backward = (flags & 2) != 0;
            boolean shift = (flags & 0x20) != 0;
            // #2 修复: 1.21 下蹲(sneak)状态经 player_input 的 shift 位上报(不再有 entity_action 的
            // START_SNEAKING)。曾未同步 isSneaking -> 潜行右键箱子/熔炉时服务端仍走交互分支,
            // "下蹲无法在可右键互动方块上放物品"。此处同步并在变化时广播潜行元数据。
            if (shift != this.isSneaking) {
                setSneaking(shift);
            }
            // 矿车骑乘: W 前进 / S 后退 (原版 Boat/Minecart 由 player_input 驱动;
            // 曾只靠 vehicle_move 的 yaw 点积, 玩家在车内不动视角时 throttle 恒 0 -> 无法开动)。
            if (this.riddenEntity instanceof MinecartEntity mc2) {
                if (forward) mc2.throttle = 1;
                else if (backward) mc2.throttle = -1;
                else mc2.throttle = 0;
            }
            // Shift 下车(0x2a shift 位, 兼容部分客户端不发 0x29)
            if (shift && this.riddenEntity != null) {
                dismount();
            }
        }
        else if (id == 0x21) { // vehicle_move (骑乘时客户端上报载具位置/朝向)
            double vcx = in.getBuffer().readDouble();
            double vcy = in.getBuffer().readDouble();
            double vcz = in.getBuffer().readDouble();
            float vyaw = in.getBuffer().readFloat();
            float vpitch = in.getBuffer().readFloat();
            if (this.riddenEntity instanceof MinecartEntity cart) {
                // Bug24 三轮: 1.21.2+ 矿车为 NewMinecartBehavior = 服务端权威, 客户端矿车
                // 位置/插值完全由专用 move_minecart(0x35) 驱动, 不消费 vehicle_move 上报位置。
                // 曾做"客户端权威" -> 服务端格子物理与客户端物理互相拉扯(坐车抖动/开不动/
                // 推车没反应)。此处只采纳朝向作为转向输入(玩家按 W 时朝最近的轨口开),
                // 坐标一律忽略, 位置由服务端 MinecartEntity.tick 每刻 0x35 广播。
                if (Double.isFinite(vcx) && Double.isFinite(vcy) && Double.isFinite(vcz)
                        && Float.isFinite(vyaw) && Float.isFinite(vpitch)) {
                    cart.lastInputYaw = vyaw;
                }
            }
        }

        // ── CHAT / COMMANDS ──────────────────────────────────────────────────
        else if (id == 0x06) { // Chat Command (客户端发送 / 指令)
                String cmd = in.readString();
                handleCommand(ctx, cmd);
        }
        else if (id == 0x08) {
            String rawMsg = in.readString();
            Long mutedUntil = MUTED_UNTIL.get(this.uuid);
            if (mutedUntil != null) {
                if (System.currentTimeMillis() < mutedUntil) {
                    sendFeedback("你已被禁言, 剩余 " + ((mutedUntil - System.currentTimeMillis()) / 60000 + 1) + " 分钟", "red");
                    return;
                }
                MUTED_UNTIL.remove(this.uuid);
            }
            var chatEvent = EVENTS.fire(new PlayerChatEvent(this, rawMsg));
            if (chatEvent.isCancelled()) return;
            // 使用 profileless_chat (0x21) 发送真正的玩家消息
            broadcastPlayerMessage(chatEvent.getMessage());
        }

        // ── CHANGE GAME MODE (F3+F4) ────────────────────────────────────────
        else if (id == 0x04) { // change_game_mode (serverbound, F3+F4 客户端发起)
            // 权限校验(P2-5): 仅 OP 可切换; 未配置 OP 时保持开放(见 isOp)
            if (!isOp()) {
                sendFeedback("权限不足: 需要 OP 才能切换游戏模式", "red");
                return;
            }
            int mode = in.readVarInt();
            if (mode < 0 || mode > 3) return;
            var gmEvent = EVENTS.fire(new PlayerGameModeChangeEvent(this, this.gameMode, mode));
            if (gmEvent.isCancelled()) return;
            mode = gmEvent.getNewGameMode();
            if (mode < 0 || mode > 3) return;
            final int finalMode = mode;
            int oldMode = this.gameMode;
            allowFlight = (mode == 1 || mode == 3);
            // Change Game State reason 3 = change game mode
            sendPacket(ctx, 0x26, pb -> { pb.writeByte(3); pb.writeFloat(finalMode); });
            // Update tab-list game mode (PlayerInfoUpdate UPDATE_GAME_MODE)
            for (NetworkHandler h : players.values()) {
                if (h.ctx == null) continue;
                h.sendPacket(h.ctx, 0x44, pb -> {
                    pb.writeByte(0x04);
                    pb.writeVarInt(1);
                    pb.writeUUID(this.uuid);
                    pb.writeVarInt(finalMode);
                });
            }
            this.gameMode = mode;
            syncEntityVisibilityToOthers(oldMode);
            sendAbilitiesUpdate();
            String[] names = {"survival", "creative", "adventure", "spectator"};
            broadcastSystemMessage("[服务器] " + username + " 的游戏模式已改为 " + names[finalMode], "gray");
        }

        // ── INTERACTION ──────────────────────────────────────────────────────
        else if (id == 0x19) { // Interact
            int targetEid = in.readVarInt();
            int type = in.readVarInt();
            if (type == 2) { in.getBuffer().skipBytes(12); }
            if (type == 0 || type == 2) { in.readVarInt(); }
            in.getBuffer().readBoolean();

            if (type == 1) { // 发生攻击
                // 已死亡玩家不能攻击
                if (this.isDead) return;

                // 攻击矿车 → 破坏并掉落物品(TNT 矿车则引爆)
                Entity atkT = EntityManager.resolveInteractTarget(targetEid);
                if (atkT instanceof MinecartEntity mcart) {
                    mcart.breakCart();
                    return;
                }

                boolean hitPlayer = false;
                for (NetworkHandler target : players.values()) {
                    if (target.eid == targetEid) {
                        hitPlayer = true;
                        // 创造/旁观模式免疫伤害，已死亡也不能被攻击
                        if (target.gameMode == 1 || target.gameMode == 3 || target.isDead) break;
                        // 原版攻击范围约 3 格 —— 防隔墙/隔远打人
                        double adx = target.x - this.x;
                        double ady = target.y - this.y;
                        double adz = target.z - this.z;
                        if (adx * adx + ady * ady + adz * adz > 3.5 * 3.5) break;

                        // 【现代 PvP 冷却】与打怪攻击同源: 武器伤害 + 二次蓄力公式 + 附魔 + 击退
                        long now = System.currentTimeMillis();
                        long elapsed = now - this.lastAttackTime;
                        int weaponId = data.inventoryIds[36 + heldItemSlot];
                        String weaponName = weaponId > 0
                            ? BlockManager.itemIdToName(weaponId) : null;
                        float cooldownMs = getAttackCooldownMs(weaponName);
                        float progress = Math.min(1.0f, elapsed / cooldownMs);

                        // 暴击判定：未着地、正在下落、未疾跑(原版)
                        boolean isCrit = !this.onGround && this.y < this.lastY && !this.sprinting;
                        float baseDamage = getAttackDamage(weaponName);
                        float damage = baseDamage * (0.2f + progress * progress * 0.8f); // 二次蓄力
                        int heldSlot = 36 + heldItemSlot;
                        int sharpness = data.getSlotEnchant(heldSlot,
                            BlockManager.getEnchantId("sharpness"));
                        if (sharpness > 0) damage += (float) sharpness * 0.5f + 0.5f;
                        // 重锤: 密度(下落加伤)/破甲/风爆
                        MaceBonus mb = computeMaceBonus(heldSlot, weaponName, this.fallDistance, totalArmorPoints(target));
                        damage += mb.dmg;
                        if (isCrit) damage *= 1.5f;
                        this.lastAttackTime = now;

                        // 受伤动画 + 伤害事件 (视觉)
                        float hitYaw = (float) Math.toDegrees(Math.atan2(
                            this.x - target.x, target.z - this.z));
                        for (NetworkHandler p : players.values()) {
                            if (p.ctx == null) continue;
                            p.sendPacket(p.ctx, 0x22, pb -> {
                                pb.writeInt(target.eid);
                                pb.writeByte(2); // Entity Status 2 = hurt (triggers hit flash)
                            });
                        }
                        for (NetworkHandler p : players.values()) {
                            if (p.ctx == null) continue;
                            p.sendPacket(p.ctx, 0x19, pb -> {
                                pb.writeVarInt(target.eid);
                                pb.writeVarInt(0); // sourceTypeId (generic)
                                pb.writeVarInt(this.eid + 1); // sourceCauseId (optional-entity-id encoding: id+1)
                                pb.writeVarInt(this.eid + 1); // sourceDirectId
                                pb.writeBoolean(false); // no source position
                            });
                        }
                        if (isCrit) {
                            broadcastAnimation(target.eid, 4);
                        }

                        // 盾牌格挡 + 护甲减伤 统一结算 (damagePlayer 内已含护甲/格挡/血量同步/死亡)
                        boolean wasDead = target.isDead;
                        target.damagePlayer(damage, "player", this.x, this.z);
                        if (target.isDead && !wasDead) {
                            broadcastSystemMessage("§c☠ §7" + target.username + " 被 " + this.username + " 击败了", "white");
                        }

                        // PVP 击退(与打怪一致): 疾跑 0.8, 否则 0.4; 击退附魔加成
                        int knockbackLvl = data.getSlotEnchant(36 + heldItemSlot,
                            BlockManager.getEnchantId("knockback"));
                        double pdx = target.x - this.x;
                        double pdz = target.z - this.z;
                        double ph = Math.sqrt(pdx * pdx + pdz * pdz);
                        if (ph > 0.001 && !target.isDead) {
                            double kb = this.sprinting ? 0.8 : 0.4;
                            if (knockbackLvl > 0) kb += 0.5 * knockbackLvl;
                            kb += mb.kb;
                            target.knockback(pdx / ph * kb, pdz / ph * kb, 0.25 + mb.kbUp);
                        }
                        break;
                    }
                }
                // 目标不是在线玩家 → 按生物处理（僵尸/骷髅/末影龙部件…）
                if (!hitPlayer) attackMob(targetEid);
            }
            else if (type == 0 || type == 2) { // 与实体交互(右键使用物品于实体)
                Entity t = EntityManager.resolveInteractTarget(targetEid);
                // 右键矿车 → 骑乘(若当前未骑乘)
                if (t instanceof MinecartEntity cart) {
                    if (this.riddenEntity == null) tryMount(cart);
                    return;
                }
                if (t instanceof MobEntity mob) {
                    if (mob.entityName.equals("villager")) {
                        openMerchant(mob);
                        return;
                    }
                    if (mob.deathTime == 0 && mob.isBreedable() && !mob.baby
                            && mob.loveTimer <= 0 && mob.breedCooldown <= 0) {
                        int slot = 36 + heldItemSlot;
                        int itemId = data.inventoryIds[slot];
                        if (itemId > 0) {
                            String food = BlockManager.itemIdToName(itemId);
                            if (MobEntity.isBreedFood(mob.entityName, food)) {
                                data.inventoryCounts[slot]--;
                                if (data.inventoryCounts[slot] <= 0) {
                                    data.inventoryCounts[slot] = 0;
                                    data.inventoryIds[slot] = 0;
                                }
                                sendSlotUpdate(0, slot);
                                mob.loveTimer = 600; // 进入发情期 30 秒
                            }
                        }
                    }
                }
                return;
            }
        }

        // ── ARM SWING ────────────────────────────────────────────────────────
        else if (id == 0x3C) { // arm_animation (serverbound)
            int hand = in.readVarInt(); // 0 = main hand, 1 = off hand
            int animId = (hand == 0) ? 0 : 3; // 0=swing main, 3=swing off
            EVENTS.fire(new com.CharunCore.server.plugin.event.events.PlayerAnimationEvent(this));
            broadcastAnimation(this.eid, animId);
            this.isBlocking = false;
        }

        // ── TAB COMPLETE ─────────────────────────────────────────────────────
        else if (id == 0x0E) { // tab_complete (serverbound)
            int transactionId = in.readVarInt();
            String text = in.readString();

            java.util.List<String> matches = getTabCompletions(text);
            int start = text.lastIndexOf(' ') + 1;
            int length = text.length() - start;

            sendPacket(ctx, 0x0F, pb -> {
                pb.writeVarInt(transactionId);
                pb.writeVarInt(start);
                pb.writeVarInt(length);
                pb.writeVarInt(matches.size());
                for (String match : matches) {
                    pb.writeString(match);
                    pb.writeBoolean(false); // no tooltip
                }
            });
        }


        // ── SNEAK / SPRINT ───────────────────────────────────────────────────
        // entity_action (0x29) — 1.21.11 的 action 枚举与旧版不同(0=leave_bed, 1=start_sprinting,
        // 2=stop_sprinting, 3=start_horse_jump, 4=stop_horse_jump, 5=open_vehicle_inventory, 6=start_elytra_flying)。
        // 注意: 1.21 下蹲(sneak)状态改由 player_input(0x2a) 的 shift 位上报, 不再走本包。
        else if (id == 0x29) {
            in.readVarInt();       // entity id (always self, can ignore)
            int action = in.readVarInt();
            in.readVarInt();       // mount jump height (unused)
            if      (action == 0) {                     // START_SNEAKING
                if (this.riddenEntity != null) { dismount(); return; }
                EVENTS.fire(new PlayerToggleSneakEvent(this, true));
                setSneaking(true);
            }
            else if (action == 1) {                     // STOP_SNEAKING
                EVENTS.fire(new PlayerToggleSneakEvent(this, false));
                setSneaking(false);
                if (fallFlying) stopElytra();           // 滑翔中按潜行会停止飞行
            }
            else if (action == 3) { EVENTS.fire(new PlayerToggleSprintEvent(this, true)); sprinting = true; }
            else if (action == 4) { EVENTS.fire(new PlayerToggleSprintEvent(this, false)); sprinting = false; }
            else if (action == 6) {                     // START_ELYTRA_FLYING
                if (!fallFlying && hasElytraEquipped() && !onGround) {
                    var flightEvent = EVENTS.fire(new com.CharunCore.server.plugin.event.events.PlayerToggleFlightEvent(this, true));
                    if (!flightEvent.isCancelled()) {
                        fallFlying = true;
                        broadcastElytra();
                    }
                }
            }
        }

        // ── SET CARRIED ITEM (hotbar scroll) ─────────────────────────────────
        // ❓VERIFY: scroll hotbar, watch logs for correct ID
        else if (id == 0x34) { // held_item_slot (serverbound)
            short newSlot = in.getBuffer().readShort();
            if (newSlot >= 0 && newSlot <= 8 && newSlot != heldItemSlot) {
                var heldEvent = EVENTS.fire(new PlayerItemHeldEvent(
                        this, heldItemSlot, newSlot));
                if (!heldEvent.isCancelled()) {
                    heldItemSlot = newSlot;
                }
            }
            broadcastEquipment(); // ← 新增：切换快捷栏后立即广播装备给其他玩家
            //System.out.println("[游戏] " + username + " 切换到快捷栏槽 " + heldItemSlot);
        }

        // ── PLAYER ACTION (digging) ──────────────────────────────────────────
        else if (id == 0x28) {
            int status = in.readVarInt();
            int[] pos  = in.readPosition();
            in.readByte();               // face
            int sequence = in.readVarInt();

            // #56 诊断: 下界/末地无法破坏方块 —— 临时追踪服务端视角
            if (this.currentDim != DimensionType.OVERWORLD) {
                int dbgState = WorldManager.getBlockState(this.currentDim, pos[0], pos[1], pos[2]);
                System.out.println("[挖掘诊断] dim=" + this.currentDim.key + " status=" + status
                    + " pos=" + pos[0] + "," + pos[1] + "," + pos[2]
                    + " state=" + dbgState + "/" + BlockStateHelper.getName(dbgState)
                    + " gameMode=" + gameMode);
            }

            // 1. 必须回复 Acknowledge Block Change（否则客户端预测永远不被清除）
            //    ❓ 0x04 = block_changed_ack，通过日志验证
            final int seq = sequence;
            sendPacket(ctx, 0x04, pb -> pb.writeVarInt(seq));

            // 2. status=0 创造/瞬间挖掘；status=2 生存挖掘完成
            if (status == 2 || (status == 0 && gameMode == 1)) {
                // ── 反作弊: 生存模式校验挖掘耗时, 拒绝瞬破 ──
                if (status == 2 && gameMode == 0) {
                    String bName = BlockStateHelper.getName(
                        WorldManager.getBlockState(this.currentDim, pos[0], pos[1], pos[2]));
                    float reqSec = BlockManager.getBreakSecondsBestCase(bName);
                    if (reqSec > 0.0f) {
                        String dkey = pos[0] + "," + pos[1] + "," + pos[2];
                    Long start = digStarts.remove(dkey);
                    if (start == null) {
                        clearDigProgress(); // Bug59: 被拒也要清其他玩家看到的裂纹
                        return; // 无开始记录且非瞬破方块 → 拒绝
                    }
                    long elapsed = System.currentTimeMillis() - start;
                    if (elapsed < (long) (reqSec * 1000.0f * 0.5f)) {
                        clearDigProgress(); // Bug59: 被拒也要清其他玩家看到的裂纹
                        return; // 挖掘过快 → 判定作弊, 不破坏
                    }
                    }
                }
                breakBlockAt(pos[0], pos[1], pos[2]);
                // Bug59: 挖掘完成/被拒 -> 其他玩家的裂纹消失
                clearDigProgress();
            }
            else if (status == 0 && gameMode == 0) {
                // #2 修复: 原版客户端挖「瞬破方块」(硬度 0, 如火把/红石粉/中继器/花/草) 时
                // 只发 START_DIGGING(status=0), 不会再发 FINISH_DIGGING(status=2)。
                // 服务端必须在 status=0 时检查目标硬度: 若为瞬破方块则立即执行破坏,
                // 否则才登记挖掘开始计时等 status=2 完成。
                int pre0 = WorldManager.getBlockState(this.currentDim, pos[0], pos[1], pos[2]);
                String bName0 = BlockStateHelper.getName(pre0);
                float reqSec0 = BlockManager.getBreakSecondsBestCase(bName0);
                if (reqSec0 <= 0.0f && pre0 != 0) {
                    breakBlockAt(pos[0], pos[1], pos[2]);
                } else {
                    digStarts.put(pos[0] + "," + pos[1] + "," + pos[2], System.currentTimeMillis());
                    broadcastBlockBreakProgress(this.currentDim, this.eid, pos[0], pos[1], pos[2], 0);
                    // Bug59: 记录进行中的生存挖掘, tickSurvival 按 elapsed/总时长 广播裂纹阶段
                    digProgressX = pos[0]; digProgressY = pos[1]; digProgressZ = pos[2];
                    digProgressStart = System.currentTimeMillis();
                    // Bug59: 用真实工具耗时而非 best-case, 否则裂纹阶段对不上、中途消失
                    digProgressDurMs = Math.max(50.0f,
                        BlockManager.getBreakSeconds(bName0, data.inventoryIds[36 + heldItemSlot]) * 1000.0f);
                    digProgressStage = 0;
                }
            }
            // status=3 DROP_ALL_ITEMS (Ctrl+Q, 丢整组), status=4 DROP_ITEM (Q, 丢一个)
            else if (status == 3 || status == 4) {
                int slot = 36 + heldItemSlot;
                if (data.inventoryCounts[slot] > 0) {
                    int itemId = data.inventoryIds[slot];
                    ItemMeta dm = playerSlotMeta(slot);
                    int count = (status == 3 || !dm.isEmpty()) ? data.inventoryCounts[slot] : 1;
                    data.inventoryCounts[slot] -= count;
                    if (data.inventoryCounts[slot] <= 0) {
                        data.inventoryCounts[slot] = 0;
                        data.inventoryIds[slot] = 0;
                        writePlayerSlotMeta(slot, null);
                    }
                    sendInventoryUpdate();
                    // Bug4/33: Q 键丢弃携带组件
                    dropItemInFront(itemId, count, dm);
                }
            }
            else if (status == 5) { // RELEASE_USE_ITEM (松开弓/盾/弩)
                releaseBow();
                releaseCrossbow();
                this.isBlocking = false;
                // Bug52: 提前松手取消进食/饮用
                eatingFinishAt = 0L;
                eatingSlot = -1;
                eatingMode = 0;
                setUsingItem(false);
            }
            else if (status == 6) { // SWAP_ITEM_WITH_OFFHAND
                int main = 36 + heldItemSlot;
                int tmpId = data.inventoryIds[main];
                int tmpCt = data.inventoryCounts[main];
                ItemMeta tmpMeta = playerSlotMeta(main);
                ItemMeta offMeta = playerSlotMeta(45);
                data.inventoryIds[main] = data.inventoryIds[45];
                data.inventoryCounts[main] = data.inventoryCounts[45];
                writePlayerSlotMeta(main, offMeta);
                data.inventoryIds[45] = tmpId;
                data.inventoryCounts[45] = tmpCt;
                writePlayerSlotMeta(45, tmpMeta);
                sendInventoryUpdate();
            }
            // status=1 = 取消挖掘 → 清理挖掘计时
            if (status == 1) {
                digStarts.remove(pos[0] + "," + pos[1] + "," + pos[2]);
                // Bug59: 取消挖掘 → 通知其他玩家移除裂纹(stage -1)
                if (digProgressDurMs > 0.0f) {
                    clearDigProgress();
                }
            }
            // status=1 = 取消挖掘 → 什么都不做，客户端已收到 ack 会自动复原
        }

        // ── BLOCK PLACEMENT ──────────────────────────────────────────────────
        else if (id == 0x3F) { // use_item_on
            int hand     = in.readVarInt();
            int[] pos    = in.readPosition();
            int face     = in.readVarInt();
            // BlockHitResult 尾部: cursor xyz(3×float=12) + insideBlock(bool) + worldBorderHit(bool) = 14 字节
            in.getBuffer().skipBytes(14);
            int sequence = in.readVarInt();

            // 【修复】acknowledge_player_digging (0x04) 必须最先发送，清除客户端预测
            final int sq = sequence;
            sendPacket(ctx, 0x04, pb -> pb.writeVarInt(sq));

            // 模式守卫 (P2-1): 旁观完全不可与世界交互; 冒险不可放置/交互
            // (组件系统未存 CanPlaceOn, 先一律拦截, 与原版"冒险仅可放带 CanPlaceOn 物品"最接近的可行实现)
            if (this.gameMode == 3 || this.gameMode == 2) return;

            int targetStateId = WorldManager.getBlockState(this.currentDim,pos[0], pos[1], pos[2]);
            int heldItemId    = data.inventoryIds[36 + heldItemSlot];
            String heldName   = BlockManager.itemIdToName(heldItemId); // 需要在 BlockManager 新增
            var interactEvent = EVENTS.fire(new PlayerInteractEvent(
                    this, PlayerInteractEvent.Action.RIGHT_CLICK,
                    pos[0], pos[1], pos[2], targetStateId, heldItemId));
            if (interactEvent.isCancelled()) return;
            // ── 特殊物品优先 ──────────────────────────────

            // 打火石：在目标面放置火
            if ("flint_and_steel".equals(heldName)) {
                String clickedName = BlockStateHelper.getName(targetStateId);
                int[] fp = faceOffset(pos, face);
                if ("obsidian".equals(clickedName)) {
                    if (tryIgniteNetherPortal(fp[0], fp[1], fp[2])) return;
                }
                // 打火石点燃 TNT: 激活为 TNT 实体(80 刻引信)
                if ("tnt".equals(clickedName)) {
                    RedstoneEngine.primeTnt(this.currentDim, pos[0], pos[1], pos[2], 80);
                    if (gameMode == 0) {
                        int fSlot = 36 + heldItemSlot;
                        data.inventoryDamage[fSlot] += 1;
                        if (data.inventoryDamage[fSlot] >= 64) {
                            data.inventoryIds[fSlot] = 0;
                            data.inventoryCounts[fSlot] = 0;
                        }
                        sendSlotUpdate(0, fSlot);
                    }
                    return;
                }
                int existingBlock = WorldManager.getBlockState(this.currentDim,fp[0], fp[1], fp[2]);
                // #45/#27 修复: 火必须有支撑面(点击的方块面), 不能隔空放。
                // 曾只判 existingBlock==0 -> 点向非实心方块/空中的面也放火(隔空放 -> 立刻碎成掉落物)。
                if (existingBlock == 0 && face >= 0 && face <= 5
                        && targetStateId != 0 && BlockStateHelper.isSolidOpaque(targetStateId)) {
                    var igniteEvent = EVENTS.fire(new com.CharunCore.server.plugin.event.events.BlockIgniteEvent(fp[0], fp[1], fp[2]));
                    if (igniteEvent.isCancelled()) return;
                    int fireState = com.CharunCore.server.world.FluidEngine.fireStateAt(this.currentDim, fp[0], fp[1], fp[2], 0);
                    WorldManager.setBlock(this.currentDim,fp[0], fp[1], fp[2], fireState);
                    broadcastBlockChange(this.currentDim, fp[0], fp[1], fp[2], fireState);
                    FluidEngine.scheduleFireTick(this.currentDim, fp[0], fp[1], fp[2], fireState);
                }
                return;
            }

            // 火焰弹(fire_charge): 右键放置火(原版行为, 不点燃 TNT, 消耗 1 个)。
            if ("fire_charge".equals(heldName)) {
                int[] fp = faceOffset(pos, face);
                int existingBlock = WorldManager.getBlockState(this.currentDim,fp[0], fp[1], fp[2]);
                // #45/#27 同上: 火焰弹也不能隔空放火(需紧贴被点击的实心方块面)。
                if (existingBlock == 0 && face >= 0 && face <= 5
                        && targetStateId != 0 && BlockStateHelper.isSolidOpaque(targetStateId)) {
                    var igniteEvent2 = EVENTS.fire(new com.CharunCore.server.plugin.event.events.BlockIgniteEvent(fp[0], fp[1], fp[2]));
                    if (igniteEvent2.isCancelled()) return;
                    int fireState = com.CharunCore.server.world.FluidEngine.fireStateAt(this.currentDim, fp[0], fp[1], fp[2], 0);
                    WorldManager.setBlock(this.currentDim,fp[0], fp[1], fp[2], fireState);
                    broadcastBlockChange(this.currentDim, fp[0], fp[1], fp[2], fireState);
                    FluidEngine.scheduleFireTick(this.currentDim, fp[0], fp[1], fp[2], fireState);
                    if (gameMode == 0) {
                        int slot = 36 + heldItemSlot;
                        data.inventoryCounts[slot]--;
                        if (data.inventoryCounts[slot] <= 0) {
                            data.inventoryIds[slot] = 0;
                            data.inventoryCounts[slot] = 0;
                        }
                        sendSlotUpdate(0, slot);
                    }
                }
                return;
            }

            if ("ender_eye".equals(heldName) || "eye_of_ender".equals(heldName)) {
                String clickedName = BlockStateHelper.getName(targetStateId);
                if ("end_portal_frame".equals(clickedName)) {
                    String hasEye = BlockStateHelper.getProp(targetStateId, "eye");
                    if (!"true".equals(hasEye)) {
                        int newState = BlockStateHelper.withProp(targetStateId, "eye", "true");
                        WorldManager.setBlock(this.currentDim,pos[0], pos[1], pos[2], newState);
                        broadcastBlockChange(this.currentDim, pos[0], pos[1], pos[2], newState);
                        tryActivateEndPortal(pos[0], pos[1], pos[2]);
                        // 原版: 放入末影之眼消耗物品(生存模式)
                        if (gameMode == 0) {
                            int eSlot = 36 + heldItemSlot;
                            data.inventoryCounts[eSlot]--;
                            if (data.inventoryCounts[eSlot] <= 0) {
                                data.inventoryIds[eSlot] = 0;
                                data.inventoryCounts[eSlot] = 0;
                            }
                            sendSlotUpdate(0, eSlot);
                        }
                    }
                    return;
                }
            }

            // 水桶：点击任何位置放置水源
            if ("water_bucket".equals(heldName)) {
                int[] fp = faceOffset(pos, face);
                int waterState = BlockStateHelper.getDefault("water");
                WorldManager.setBlock(this.currentDim,fp[0], fp[1], fp[2], waterState);
                broadcastBlockChange(this.currentDim, fp[0], fp[1], fp[2], waterState);
                FluidEngine.scheduleFluidTick(fp[0], fp[1], fp[2], waterState);
                // 玩家倒水: 显式向 4 方向扩散成 level=1 流动水 (自然水体不排洪,
                // 若只放 1 格水源会因 processFluid 不蔓延而"水不流动")。
                FluidEngine.spreadSource(this.currentDim, fp[0], fp[1], fp[2], waterState);
                int bucketId0 = BlockManager.getItemIdByName("bucket");
                if (gameMode == 0) {
                    int slot0 = 36 + heldItemSlot;
                    data.inventoryIds[slot0] = bucketId0;
                    data.inventoryCounts[slot0] = 1;
                    sendSlotUpdate(0, slot0);
                }
                // 创造/旁观: 客户端保持物品无限, 服务端不再 giveItem(避免复制) —— 修复 P2-2
                return;
            }

            // 空桶：点击水源收水，点击岩浆源收岩浆
            if ("bucket".equals(heldName)) {
                String clickedName = BlockStateHelper.getName(targetStateId);
                if ("water".equals(clickedName)
                        && "0".equals(BlockStateHelper.getProp(targetStateId, "level"))) {
                    WorldManager.setBlock(this.currentDim,pos[0], pos[1], pos[2], 0);
                    broadcastBlockChange(this.currentDim, pos[0], pos[1], pos[2], 0);
                    int wbId = BlockManager.getItemIdByName("water_bucket");
                    if (gameMode == 0) {
                        int slotW = 36 + heldItemSlot;
                        data.inventoryIds[slotW] = wbId;
                        data.inventoryCounts[slotW] = 1;
                        sendSlotUpdate(0, slotW);
                    }
                    // 创造/旁观不重复发放(修复 P2-2)
                    return;
                }
                if ("lava".equals(clickedName)
                        && "0".equals(BlockStateHelper.getProp(targetStateId, "level"))) {
                    WorldManager.setBlock(this.currentDim,pos[0], pos[1], pos[2], 0);
                    broadcastBlockChange(this.currentDim, pos[0], pos[1], pos[2], 0);
                    int lbId = BlockManager.getItemIdByName("lava_bucket");
                    if (gameMode == 0) {
                        int slotL = 36 + heldItemSlot;
                        data.inventoryIds[slotL] = lbId;
                        data.inventoryCounts[slotL] = 1;
                        sendSlotUpdate(0, slotL);
                    }
                    // 创造/旁观不重复发放(修复 P2-2)
                    return;
                }
            }

            // lava bucket
            if ("lava_bucket".equals(heldName)) {
                int[] fp = faceOffset(pos, face);
                int lavaState = BlockStateHelper.getDefault("lava");
                WorldManager.setBlock(this.currentDim,fp[0], fp[1], fp[2], lavaState);
                broadcastBlockChange(this.currentDim, fp[0], fp[1], fp[2], lavaState);
                FluidEngine.scheduleFluidTick(fp[0], fp[1], fp[2], lavaState);
                int bucketIdL = BlockManager.getItemIdByName("bucket");
                if (gameMode == 0) {
                    int slotL = 36 + heldItemSlot;
                    data.inventoryIds[slotL] = bucketIdL;
                    data.inventoryCounts[slotL] = 1;
                    sendSlotUpdate(0, slotL);
                }
                // 创造/旁观不重复发放(修复 P2-2)
                return;
            }

            // ── 船：在水面或地面上生成船实体 ─────────────────
            if (heldName != null && heldName.endsWith("_boat")) {
                int[] fp = faceOffset(pos, face);
                double bx = fp[0] + 0.5;
                double bz = fp[2] + 0.5;
                double by = fp[1] + 1.0;
                int wState = WorldManager.getBlockState(this.currentDim, fp[0], fp[1], fp[2]);
                String wName = BlockStateHelper.getName(wState);
                if ("water".equals(wName)) {
                    int wy = fp[1];
                    while (wy < 319) {
                        int above = WorldManager.getBlockState(this.currentDim, fp[0], wy + 1, fp[2]);
                        if (!"water".equals(BlockStateHelper.getName(above))) break;
                        wy++;
                    }
                    by = wy + 0.05;
                }
                Entity boat =
                    new Entity(
                        EntityManager.allocateId(), 0, bx, by, bz);
                boat.typeName = heldName;
                boat.dim = this.currentDim;
                EntityManager.addEntity(boat);
                if (gameMode == 0) {
                    int slot = 36 + heldItemSlot;
                    data.inventoryCounts[slot]--;
                    if (data.inventoryCounts[slot] <= 0) {
                        data.inventoryIds[slot] = 0;
                        data.inventoryCounts[slot] = 0;
                    }
                    sendSlotUpdate(0, slot);
                }
                return;
            }

            // ── 矿车：在玩家点击的轨道上方生成矿车实体 ──────
            if (heldName != null && isMinecartItem(heldName)) {
                int[] rail = findRailForMinecart(pos, face);
                if (rail != null) {
                    MinecartEntity cart =
                        new MinecartEntity(
                            EntityManager.allocateId(),
                            heldName, rail[0] + 0.5, rail[1] + 0.0625, rail[2] + 0.5);
                    cart.dim = this.currentDim;
                    // 朝玩家视线初始方向，便于上车即行进
                    initCartDir(cart, this.yaw);
                    EntityManager.addEntity(cart);
                    if (gameMode == 0) {
                        int slot = 36 + heldItemSlot;
                        data.inventoryCounts[slot]--;
                        if (data.inventoryCounts[slot] <= 0) {
                            data.inventoryIds[slot] = 0;
                            data.inventoryCounts[slot] = 0;
                        }
                        sendSlotUpdate(0, slot);
                    }
                }
                return; // 矿车不是方块，不落入下方方块放置逻辑
            }

            // ── 刷怪蛋：生成对应生物实体 / 右键刷怪笼改变其刷怪类型 ─────────
            if (heldName != null && heldName.endsWith("_spawn_egg")) {
                String mobName = heldName.substring(0, heldName.length() - "_spawn_egg".length());
                // 右键刷怪笼: 改变其 SpawnData(刷怪类型), 原版行为。
                String clickedName = BlockStateHelper.getName(targetStateId);
                if ("spawner".equals(clickedName) || "mob_spawner".equals(clickedName)) {
                    Chunk spChunk = WorldManager.getChunk(this.currentDim, pos[0] >> 4, pos[2] >> 4);
                    if (spChunk != null) {
                        org.cloudburstmc.nbt.NbtMap be = spChunk.getBlockEntity(pos[0] & 15, pos[1], pos[2] & 15);
                        org.cloudburstmc.nbt.NbtMapBuilder sb = org.cloudburstmc.nbt.NbtMap.builder();
                        if (be != null) for (String k : be.keySet()) sb.put(k, be.get(k));
                        else {
                            sb.putString("id", "minecraft:mob_spawner");
                            sb.putInt("x", pos[0]); sb.putInt("y", pos[1]); sb.putInt("z", pos[2]);
                        }
                        // Bug10 修复: 1.19.3+ 刷怪笼 BE 格式为 SpawnData:{entity:{id:"minecraft:x"}},
                        // 客户端旋转预览模型读的是 SpawnData.entity; 曾直接写 {id:...} -> 无预览。
                        org.cloudburstmc.nbt.NbtMap spawnData = org.cloudburstmc.nbt.NbtMap.builder()
                            .putCompound("entity", org.cloudburstmc.nbt.NbtMap.builder()
                                .putString("id", "minecraft:" + mobName).build())
                            .build();
                        sb.putCompound("SpawnData", spawnData);
                        sb.putList("SpawnPotentials", org.cloudburstmc.nbt.NbtType.COMPOUND,
                            java.util.List.of(org.cloudburstmc.nbt.NbtMap.builder()
                                .putInt("weight", 1)
                                .putCompound("data", org.cloudburstmc.nbt.NbtMap.builder()
                                    .putCompound("entity", org.cloudburstmc.nbt.NbtMap.builder()
                                        .putString("id", "minecraft:" + mobName).build())
                                    .build())
                                .build()));
                        spChunk.setBlockEntity(pos[0] & 15, pos[1], pos[2] & 15, sb.build());
                        // #14 修复: 广播 block_entity_data(0x09) 让客户端立即刷新刷怪笼渲染,
                        // 否则放蛋后客户端看不到笼内旋转的生物模型(仅服务端数据变了)。
                        broadcastBlockEntityData(pos[0], pos[1], pos[2], targetStateId, sb.build());
                        // 重新注册刷怪笼使新类型立即生效
                        com.CharunCore.server.world.SpawnerSystem.register(this.currentDim, pos[0], pos[1], pos[2]);
                    }
                    if (gameMode == 0) {
                        int slot = 36 + heldItemSlot;
                        data.inventoryCounts[slot]--;
                        if (data.inventoryCounts[slot] <= 0) {
                            data.inventoryIds[slot] = 0;
                            data.inventoryCounts[slot] = 0;
                        }
                        sendSlotUpdate(0, slot);
                    }
                    return;
                }
                int[] fp = faceOffset(pos, face);
                MobEntity mob =
                    new MobEntity(
                        EntityManager.allocateId(),
                        mobName, fp[0] + 0.5, fp[1] + 1.0, fp[2] + 0.5);
                mob.dim = this.currentDim;
                EntityManager.addEntity(mob);
                if (gameMode == 0) {
                    int slot = 36 + heldItemSlot;
                    data.inventoryCounts[slot]--;
                    if (data.inventoryCounts[slot] <= 0) {
                        data.inventoryIds[slot] = 0;
                        data.inventoryCounts[slot] = 0;
                    }
                    sendSlotUpdate(0, slot);
                }
                return;
            }

            // ── 末地水晶物品：放置 EndCrystalEntity（用于重生末影龙）─────
            if (heldName != null && heldName.equals("end_crystal")) {
                int[] fp = faceOffset(pos, face);
                EndCrystalEntity crystal =
                    new EndCrystalEntity(
                        EntityManager.allocateId(),
                        fp[0] + 0.5, fp[1] + 0.5, fp[2] + 0.5);
                crystal.dim = this.currentDim;
                crystal.yaw = 0.0f;
                EntityManager.addEntity(crystal);
                EndDragonFight.onEndCrystalPlaced(
                    fp[0], fp[1], fp[2]);
                if (gameMode == 0) {
                    int slot = 36 + heldItemSlot;
                    data.inventoryCounts[slot]--;
                    if (data.inventoryCounts[slot] <= 0) {
                        data.inventoryIds[slot] = 0;
                        data.inventoryCounts[slot] = 0;
                    }
                    sendSlotUpdate(0, slot);
                }
                return;
            }

            // ── 龙蛋：右键瞬移（原版行为，teleport up to 5 blocks）─────
            String targetName = BlockStateHelper.getName(targetStateId);
            if (targetName != null && targetName.equals("dragon_egg")) {
                int eggX = pos[0], eggY = pos[1], eggZ = pos[2];
                WorldManager.setBlock(this.currentDim, eggX, eggY, eggZ, 0);
                broadcastBlockChange(this.currentDim, eggX, eggY, eggZ, 0);
                // 瞬移到附近随机位置 (5 格内, 需要 2 格空间)
                java.util.Random rng = new java.util.Random();
                int eggState = BlockStateHelper.getDefault("dragon_egg");
                boolean placed = false;
                for (int attempt = 0; attempt < 8 && !placed; attempt++) {
                    int nx = eggX + rng.nextInt(11) - 5;
                    int nz = eggZ + rng.nextInt(11) - 5;
                    int ny = eggY;
                    if (rng.nextBoolean()) ny += rng.nextInt(3) - 1;
                    // 向下找地面
                    while (ny > this.currentDim.minY + 1) {
                        int below = WorldManager.getBlockState(this.currentDim, nx, ny - 1, nz);
                        String bn = BlockStateHelper.getName(below);
                        boolean solidBelow = below != 0 && bn != null && !bn.equals("air")
                            && !bn.equals("cave_air") && !bn.equals("water") && !bn.equals("lava");
                        if (solidBelow) break;
                        ny--;
                    }
                    int at = WorldManager.getBlockState(this.currentDim, nx, ny, nz);
                    String an = BlockStateHelper.getName(at);
                    if (at == 0 || "air".equals(an) || "cave_air".equals(an)) {
                        WorldManager.setBlock(this.currentDim, nx, ny, nz, eggState);
                        broadcastBlockChange(this.currentDim, nx, ny, nz, eggState);
                        placed = true;
                    }
                }
                if (!placed) {
                    // 回退: 原位放回
                    WorldManager.setBlock(this.currentDim, eggX, eggY, eggZ, eggState);
                    broadcastBlockChange(this.currentDim, eggX, eggY, eggZ, eggState);
                }
                return;
            }

            // ── 营火: 右键放食物(4 槽)/取出已烹饪食物 ───────────────────
            String campName = BlockStateHelper.getName(targetStateId);
            if ("campfire".equals(campName) || "soul_campfire".equals(campName)) {
                ContainerStore.Pos cp = new ContainerStore.Pos(this.currentDim, pos[0], pos[1], pos[2]);
                ContainerStore.CampfireData cd = ContainerStore.campfire(cp);
                int[] c = cd.slots;
                if (heldItemId > 0) {
                    String heldFoodName = BlockManager.itemIdToName(heldItemId);
                    if (SmeltingSystem.isFood(heldFoodName)
                            && SmeltingSystem.getResult(heldFoodName, "furnace") != null) {
                        // 找空槽放生食
                        for (int i = 0; i < 4; i++) {
                            if (c[i * 2] <= 0) {
                                c[i * 2] = heldItemId;
                                c[i * 2 + 1] = 1;
                                cd.cookTime[i] = 0;
                                cd.version++;
                                if (gameMode == 0) {
                                    int slot = 36 + heldItemSlot;
                                    data.inventoryCounts[slot]--;
                                    if (data.inventoryCounts[slot] <= 0) {
                                        data.inventoryIds[slot] = 0;
                                        data.inventoryCounts[slot] = 0;
                                    }
                                    sendSlotUpdate(0, slot);
                                }
                                // #15: 营火上放置食物需广播 block_entity_data(Items),
                                // 曾只 broadcastBlockChange -> 客户端不渲染架上的食物(重进才显示)。
                                persistCampfireAndBroadcast(cp, cd);
                                return;
                            }
                        }
                        return; // 4 槽满, 不取出
                    }
                    // 手持非食物: 走取出逻辑(下文), 让玩家用空手/工具取出熟食
                }
                // 取出: 仅空手或手持非食物时取第一个食物槽, 弹出为掉落物
                if (heldItemId <= 0) {
                    for (int i = 0; i < 4; i++) {
                        if (c[i * 2] > 0) {
                            int outId = c[i * 2], outCnt = c[i * 2 + 1];
                            c[i * 2] = 0; c[i * 2 + 1] = 0;
                            cd.cookTime[i] = 0;
                            cd.version++;
                            dropItemInFront(outId, outCnt);
                            persistCampfireAndBroadcast(cp, cd);
                            return;
                        }
                    }
                }
                return;
            }

            // ── 方块交互（不潜行时优先）────────────────────
            if (!isSneaking && BlockStateHelper.isInteractable(targetStateId)) {
                handleBlockInteraction(pos[0], pos[1], pos[2], targetStateId, heldItemId);
                return;
            }

            // ── 正常放置逻辑 ─────────────────────────────
            if (heldItemId <= 0) return;
            int placeStateId = BlockManager.getDefaultStateForItem(heldItemId);
            if (placeStateId == 0) return;

            String blockName  = BlockStateHelper.getName(placeStateId);

// 【修复】：处理原版特殊的墙面变体(火把/告示牌/头颅/旗帜在侧面点击时切换)
            if (face >= 2 && face <= 5) {
                if (blockName.equals("redstone_torch")) {
                    placeStateId = BlockStateHelper.getDefault("redstone_wall_torch");
                    blockName = "redstone_wall_torch";
                } else if (blockName.equals("torch")) {
                    placeStateId = BlockStateHelper.getDefault("wall_torch");
                    blockName = "wall_torch";
                } else if (blockName.endsWith("_hanging_sign")) {
                    int ns = BlockStateHelper.getDefault(blockName.replace("_hanging_sign", "_wall_hanging_sign"));
                    if (ns != 0) { placeStateId = ns; blockName = blockName.replace("_hanging_sign", "_wall_hanging_sign"); }
                } else if (blockName.endsWith("_sign")) {
                    // Bug32: 立牌点墙面 -> 墙牌变体(原版规则), 否则墙上永远立着悬空告示牌
                    int ns = BlockStateHelper.getDefault(blockName.substring(0, blockName.length() - 5) + "_wall_sign");
                    if (ns != 0) { placeStateId = ns; blockName = blockName.substring(0, blockName.length() - 5) + "_wall_sign"; }
                } else if (blockName.endsWith("_skull")) {
                    int ns = BlockStateHelper.getDefault(blockName.replace("_skull", "_wall_skull"));
                    if (ns != 0) { placeStateId = ns; blockName = blockName.replace("_skull", "_wall_skull"); }
                } else if (blockName.endsWith("_head")) {
                    int ns = BlockStateHelper.getDefault(blockName.replace("_head", "_wall_head"));
                    if (ns != 0) { placeStateId = ns; blockName = blockName.replace("_head", "_wall_head"); }
                } else if (blockName.endsWith("_banner")) {
                    int ns = BlockStateHelper.getDefault(blockName.replace("_banner", "_wall_banner"));
                    if (ns != 0) { placeStateId = ns; blockName = blockName.replace("_banner", "_wall_banner"); }
                }
            }

            String facing     = BlockStateHelper.horizontalFacing(this.yaw);
            placeStateId      = applyPlacementContext(blockName, placeStateId, facing, face);

            // 放置坐标 = 目标面偏移
            int[] pp = faceOffset(pos, face);

            // #35 铁轨: 放置后用目标坐标自动连接相邻铁轨(原版 RailState.updateDir),
            // 无相邻时回退玩家朝向。applyPlacementContext 内无法拿目标坐标, 在此重算。
            if (blockName.equals("rail") || blockName.equals("powered_rail")
                    || blockName.equals("activator_rail") || blockName.equals("detector_rail")) {
                String curShape = BlockStateHelper.getProp(placeStateId, "shape");
                if (curShape != null) {
                    // Bug23: 动力/激活/探测铁轨不能弯折, 仅普通铁轨可生成弯角形状
                    String auto = autoRailShape(pp[0], pp[1], pp[2], blockName.equals("rail"));
                    if (auto != null) {
                        placeStateId = BlockStateHelper.withProp(placeStateId, "shape", auto);
                    }
                }
            }

            // 含水放置：目标格为水且该方块支持 waterlogged 时，放置为含水状态
            int tgtState = WorldManager.getBlockState(this.currentDim, pp[0], pp[1], pp[2]);
            String tgtName = BlockStateHelper.getName(tgtState);
            if ("water".equals(tgtName)
                    && BlockStateHelper.getProp(placeStateId, "waterlogged") != null) {
                placeStateId = BlockStateHelper.withProp(placeStateId, "waterlogged", "true");
            }

            // C7: 放置校验 — 目标格必须是空气/液体/可替换, 且不能落在玩家身体内
            if (!canPlaceInto(WorldManager.getBlockState(this.currentDim, pp[0], pp[1], pp[2]))) {
                // Bug54: 拒绝时回发当前真实方块, 消除客户端预测的幽灵方块
                broadcastBlockChange(this.currentDim, pp[0], pp[1], pp[2],
                    WorldManager.getBlockState(this.currentDim, pp[0], pp[1], pp[2]));
                return;
            }
            // Bug47 修复: 只有"有碰撞箱"的方块才禁止放在脚下(原版行为);
            // 火把/红石粉/花等无碰撞方块原版本就可以放在玩家所站格子。
            // Bug54: 碰撞检测从"仅自己"扩展到所有实体(其他玩家/生物), 与原版一致。
            boolean collides = BlockManager.hasCollision(blockName);
            if (collides && intersectsAnyEntity(pp[0], pp[1], pp[2])) {
                broadcastBlockChange(this.currentDim, pp[0], pp[1], pp[2],
                    WorldManager.getBlockState(this.currentDim, pp[0], pp[1], pp[2]));
                return;
            }
            // Bug37: 非完整方块无支撑时按原版直接拒绝放置(客户端不预测, 观感=不予响应)。
            // 曾用"放置后立刻破碎"会白扣手中物品并掉出一个掉落物。
            if (!hasPlacementSupport(pp[0], pp[1], pp[2], blockName, targetStateId)) return;

            // ── 多方块结构（床）───────────────────────────
            if (blockName.endsWith("_bed")) {
                placeBed(pp[0], pp[1], pp[2], blockName, facing);
                if (gameMode == 0) {
                    int slot = 36 + heldItemSlot;
                    data.inventoryCounts[slot]--;
                    if (data.inventoryCounts[slot] <= 0) {
                        data.inventoryIds[slot] = 0;
                        data.inventoryCounts[slot] = 0;
                    }
                    sendSlotUpdate(0, slot);
                }
                return;
            }

            // ── 门（双高）────────────────────────────────
            if (blockName.endsWith("_door")) {
                if (!canPlaceInto(WorldManager.getBlockState(this.currentDim, pp[0], pp[1] + 1, pp[2]))
                        || intersectsAnyEntity(pp[0], pp[1] + 1, pp[2])) return;
                placeDoor(pp[0], pp[1], pp[2], blockName, facing);
                if (gameMode == 0) {
                    int slot = 36 + heldItemSlot;
                    data.inventoryCounts[slot]--;
                    if (data.inventoryCounts[slot] <= 0) {
                        data.inventoryIds[slot] = 0;
                        data.inventoryCounts[slot] = 0;
                    }
                    sendSlotUpdate(0, slot);
                }
                return;
            }

            var placeEvent = EVENTS.fire(new BlockPlaceEvent(
                    this, pp[0], pp[1], pp[2], placeStateId));
            if (placeEvent.isCancelled()) return;

            WorldManager.setBlock(this.currentDim,pp[0], pp[1], pp[2], placeStateId);
            broadcastBlockChange(this.currentDim, pp[0], pp[1], pp[2], placeStateId);
            RedstoneEngine.onBlockChanged(this.currentDim, pp[0], pp[1], pp[2]);
            // 放置带方块实体的方块时创建初始 BE NBT(告示牌/刷怪笼/信标等), 否则重启/重载后数据丢失。
            createInitialBlockEntity(pp[0], pp[1], pp[2], blockName);
            // Bug51: 箱子相邻合并时同步 type 属性(本箱+邻箱), 否则客户端渲染不出大箱子
            if ("chest".equals(blockName) || "trapped_chest".equals(blockName)) {
                updateChestType(pp[0], pp[1], pp[2]);
            }
            // 成就系统：放置方块事件 (P12)
            AdvancementManager.onBlockPlace(this, blockName);
            StatisticsManager.add(this, "used", blockName, 1);
            // 关键修复: setBlock 后红石引擎可能已更新该方块状态(红石粉自动连线/充能、红石灯点亮等),
            // 必须重读实际状态再广播。原用放置前的 placeStateId 广播 -> 客户端显示未连接/未激活的旧状态,
            // 直到重进游戏才正确(用户报"新放红石粉不连线/红石灯不亮, 重进就好"的根因)。
            int actualState = WorldManager.getBlockState(this.currentDim, pp[0], pp[1], pp[2]);
            broadcastBlockChange(this.currentDim, pp[0], pp[1], pp[2], actualState);

            if (gameMode == 0) {
                int slot = 36 + heldItemSlot;
                data.inventoryCounts[slot]--;
                if (data.inventoryCounts[slot] <= 0) {
                    data.inventoryIds[slot] = 0;
                    data.inventoryCounts[slot] = 0;
                }
                sendSlotUpdate(0, slot);
            }
        }

        // ── USE ITEM (eating/drinking) ──────────────────────────────────────
        else if (id == 0x40) { // use_item (774: SERVERBOUND_USE_ITEM)
            int hand = in.readVarInt();
            int sequence = in.readVarInt();
            sendPacket(ctx, 0x04, pb -> pb.writeVarInt(sequence));

            int slot = 36 + heldItemSlot;
            if (data.inventoryCounts[slot] > 0) {
                int itemId = data.inventoryIds[slot];
                String itemName = BlockManager.itemIdToName(itemId);

                if ("ender_eye".equals(itemName) || "eye_of_ender".equals(itemName)) {
                    throwEyeOfEnder();
                    if (gameMode == 0) {
                        data.inventoryCounts[slot]--;
                        if (data.inventoryCounts[slot] <= 0) data.inventoryIds[slot] = 0;
                        sendSlotUpdate(0, slot);
                    }
                    return;
                }

                if (isShield(itemId)) {
                    this.isBlocking = true;
                    setUsingItem(true);
                    return;
                }

                if ("bow".equals(itemName)) {
                    if (gameMode == 1 || findArrowSlot() >= 0) {
                        this.bowChargeStart = System.currentTimeMillis();
                        setUsingItem(true);
                    }
                    return;
                }

                if ("crossbow".equals(itemName)) {
                    // 弩蓄力(满蓄力后由 status==5 触发 releaseCrossbow 发射;
                    // quick_charge 仅影响客户端蓄力速度, 服务端满蓄力发射)
                    this.crossbowChargeStart = System.currentTimeMillis();
                    setUsingItem(true);
                    return;
                }

                if ("trident".equals(itemName)) {
                    throwTrident();
                    return;
                }

                if ("ender_pearl".equals(itemName)) {
                    throwEnderPearl();
                    if (gameMode == 0) {
                        data.inventoryCounts[slot]--;
                        if (data.inventoryCounts[slot] <= 0) data.inventoryIds[slot] = 0;
                        sendSlotUpdate(0, slot);
                    }
                    return;
                }

                // ── Bug52: 喷溅/滞留药水 = 投掷(不可饮用) ──
                if ("splash_potion".equals(itemName) || "lingering_potion".equals(itemName)) {
                    boolean ling = "lingering_potion".equals(itemName);
                    throwPotion(ling);
                    if (gameMode == 0) {
                        data.inventoryCounts[slot]--;
                        if (data.inventoryCounts[slot] <= 0) {
                            data.inventoryIds[slot] = 0;
                            data.inventoryPotion[slot] = null;
                        }
                        sendSlotUpdate(0, slot);
                    }
                    return;
                }

                // ── 饮用药水: 延迟 1.6s 结算(原版饮用时长), 立即改背包会打断客户端动画 ──
                if (isDrinkablePotion(itemName)) {
                    this.eatingFinishAt = System.currentTimeMillis() + 1600L;
                    this.eatingSlot = slot;
                    this.eatingMode = 1;
                    setUsingItem(true);
                    return;
                }
                if (false) { // legacy instant-drink (disabled)
                    String ptOld = null;
                    String pt = data.inventoryPotion[slot];
                    if (pt != null) {
                        for (String e : pt.split(",")) {
                            String[] kv = e.split("\\|");
                            if (kv.length >= 3) addEffect(kv[0], Integer.parseInt(kv[1]), Integer.parseInt(kv[2]));
                        }
                    }
                    data.inventoryCounts[slot]--;
                    if (data.inventoryCounts[slot] <= 0) {
                        data.inventoryIds[slot] = 0;
                        data.inventoryEnchants[slot] = new java.util.HashMap<>();
                        data.inventoryPotion[slot] = null;
                    }
                    giveItem(BlockManager.getItemIdByName("glass_bottle"), 1);
                    sendInventoryUpdate();
                    return;
                }

                // ── 牛奶: 清除全部状态效果 (原版行为) ──
                if ("milk_bucket".equals(itemName)) {
                    clearEffects();
                    data.inventoryCounts[slot]--;
                    if (data.inventoryCounts[slot] <= 0) data.inventoryIds[slot] = 0;
                    giveItem(BlockManager.getItemIdByName("bucket"), 1);
                    sendSlotUpdate(0, slot);
                    sendInventoryUpdate();
                    return;
                }

                // ── 钓竿: 抛竿 / 收竿(结算战利品) ──
                if ("fishing_rod".equals(itemName)) {
                    toggleFishing();
                    return;
                }

                int foodValue = getFoodValue(itemName);
                if (foodValue > 0 && (data.food < 20 || isAlwaysEdible(itemName))) {
                    // Bug52: 延迟 1.6s 结算进食, 客户端才能完整播放进食动画
                    this.eatingFinishAt = System.currentTimeMillis() + 1600L;
                    this.eatingSlot = slot;
                    this.eatingMode = 2;
                    setUsingItem(true);
                    return;
                }
                if (foodValue > 0 && false) { // legacy instant-eat (disabled)
                    data.food = Math.min(20, data.food + foodValue);
                    data.saturation = Math.min(data.food,
                        data.saturation + foodValue * getSaturationModifier(itemName));
                    if (itemName.equals("golden_apple") || itemName.equals("enchanted_golden_apple")) {
                        health = Math.min(20.0f, health + 4.0f);
                        // 原版: 金苹果给予再生(5s)+吸收(2:00), 附魔金苹果更强(再生30s/吸收4:00/抗性5:00/抗火5:00)
                        if (itemName.equals("enchanted_golden_apple")) {
                            addEffect("regeneration", 1, 600);
                            addEffect("absorption", 3, 4800);
                            addEffect("resistance", 0, 6000);
                            addEffect("fire_resistance", 0, 6000);
                        } else {
                            addEffect("regeneration", 1, 100);
                            addEffect("absorption", 0, 2400);
                        }
                    }
                    if (itemName.equals("rotten_flesh") || itemName.equals("spider_eye")
                        || itemName.equals("poisonous_potato")) {
                        addExhaustion(2.0f);
                    }
                    if (itemName.equals("chorus_fruit")) {
                        chorusFruitTeleport();
                    }
                    sendHealthUpdate();
                    data.inventoryCounts[slot]--;
                    if (data.inventoryCounts[slot] <= 0) {
                        data.inventoryIds[slot] = 0;
                    }
                    if (itemName.endsWith("_bucket") && !itemName.equals("milk_bucket")) {
                        giveItem(BlockManager.getItemIdByName("bucket"), 1);
                    }
                    AdvancementManager.onConsumeItem(this, itemName);
                    sendInventoryUpdate();
                }
            }
        }

        // ── CREATIVE INVENTORY ───────────────────────────────────────────────
        else if (id == 0x37) { // set_creative_slot — 仅创造/旁观模式处理(原版客户端只在创造/旁观发送)
            if (this.gameMode != 1 && this.gameMode != 3) return;
            short slot  = in.getBuffer().readShort();
            int   count = in.readVarInt();
            // Bug4/33 根因修复: 曾在此处把组件数据 skipBytes 丢弃、只存 id+count,
            // parseCreativeSlot 写好了却从未被调用 -> 创造模式拿取的附魔书/药水/改名物品
            // 服务端从头就没有组件, 关闭背包权威同步后客户端显示"变白"。
            if (count > 0) {
                parseCreativeSlot(slot, count, in);
            } else {
                if (slot >= 0 && slot < 46) {
                    data.inventoryIds[slot]  = 0;
                    data.inventoryCounts[slot] = 0;
                    writePlayerSlotMeta(slot, null);
                }
            }
            broadcastEquipment();
        }

        // ── CONTAINER BUTTON CLICK (附魔台选项 / 熔炉配方书等) ───────────────
        else if (id == 0x10) { // serverbound_container_button_click
            int windowId = in.readVarInt();
            int buttonId = in.readVarInt();
            handleContainerButtonClick(windowId, buttonId);
        }

        else if (id == 0x32) { // serverbound_select_trade（村民交易选中某条报价）
            int sel = in.readVarInt();
            if (merchantWindowId >= 0) {
                MerchantSession ms = openMerchants.get(merchantWindowId);
                if (ms != null) ms.selectedTrade = sel;
            }
        }

        else if (id == 0x33) { // #15 serverbound_set_beacon_effect: 两个 Optional<MobEffect>(registry id, varint id+1, 0=无)
            // Bug11: 原版 Optional<Holder<MobEffect>> = bool 前缀 + varint 0 基注册表 id
            // (曾按 "varint id+1, 无前缀" 解析 -> 读到 bool 当 id, 效果全错/全无)
            boolean hasP = in.getBuffer().readByte() != 0;
            int primary = hasP ? in.readVarInt() : -1;
            boolean hasS = in.getBuffer().readByte() != 0;
            int secondary = hasS ? in.readVarInt() : -1;
            handleSetBeaconEffect(primary, secondary);
        }

        else if (id == 0x35) { // #29 serverbound_update_command_block: pos + command + mode + flags
            int[] cbPos = in.readPosition();
            String cbCmd = in.readString();
            int cbMode = in.readVarInt();
            byte cbFlags = in.getBuffer().readByte();
            handleSetCommandBlock(cbPos, cbCmd, cbMode, cbFlags);
        }

        else if (id == 0x3B) { // serverbound_sign_update: 客户端编辑告示牌完成回传
            int[] spos = in.readPosition();
            boolean isFront = in.getBuffer().readBoolean();
            java.util.List<String> lines = new java.util.ArrayList<>(4);
            for (int i = 0; i < 4; i++) lines.add(in.readString());
            updateSignText(spos[0], spos[1], spos[2], isFront, lines);
        }

        else if (id == 0x11) { // serverbound_container_click (1.21.11 用 HashedStack 编码物品)
            int windowId = in.readVarInt();
            int stateId = in.readVarInt();
            int slotNum = in.readShort();
            int buttonNum = in.readByte();
            int clickTypeId = in.readVarInt();
            int changedSlotsCount = in.readVarInt();
            for (int i = 0; i < changedSlotsCount; i++) {
                in.readShort();                 // slot key (Int2ObjectMap)
                skipHashedStack(in);           // HashedStack: Optional + item + count + HashedPatchMap
            }
            int carriedItemIdFromPacket = skipHashedStack(in); // carried item 也是 HashedStack
            // 注意: 容器点击不发送 AcknowledgeBlockChange(0x04), 该包仅 UseItem 需要

            // slotNum == -999 表示点击窗口外(丢弃/拖拽开始或结束)
            if (slotNum == -999) {
                if (clickTypeId == 5) {
                    // Bug36: 拖拽的开始(0/4/8)与结束(2/6/10)都在 slot=-999, 曾被直接丢弃
                    // -> 服务端从未执行分发, 客户端预测被关窗后的全量同步抹掉("退出背包归零")。
                    handleDragClick(windowId, slotNum, buttonNum);
                    return;
                }
                if (carriedItemCount > 0 && (clickTypeId == 0 || clickTypeId == 4)) {
                    // 原版: 左键(0)=丢整组, 右键(1)=丢一个
                    ItemMeta cm = carriedSnapshot();
                    int drop = (buttonNum == 0 || !cm.isEmpty()) ? carriedItemCount : 1;
                    drop = Math.min(drop, carriedItemCount);
                    dropItemInFront(carriedItemId, drop, cm);
                    carriedItemCount -= drop;
                    if (carriedItemCount <= 0) {
                        carriedItemId = 0;
                        carriedItemCount = 0;
                        clearCarriedMeta();
                    }
                    sendCarriedItem();
                }
                return;
            }

            var invClickEvent = EVENTS.fire(new InventoryClickEvent(
                    this, windowId, slotNum, clickTypeId, buttonNum));
            if (invClickEvent.isCancelled()) return;

            switch (clickTypeId) {
                case 0 -> handleContainerClick(windowId, slotNum, buttonNum);
                case 1 -> handleShiftClick(windowId, slotNum);
                case 2 -> handleSwapClick(windowId, slotNum, buttonNum);
                case 3 -> handleCloneClick(windowId, slotNum);
                case 4 -> handleThrowClick(windowId, slotNum, buttonNum);
                case 5 -> handleDragClick(windowId, slotNum, buttonNum);
                case 6 -> handleDoubleClickCollect(windowId);
                default -> { /* 未知点击类型忽略 */ }
            }
        }

        // ── CLOSE WINDOW ─────────────────────────────────────────────────────
        else if (id == 0x12) { // close_window (serverbound)
            int closedWindowId = in.getBuffer().readByte();
            EVENTS.fire(new InventoryCloseEvent(this, closedWindowId));
            // Bug43 修复: 关闭玩家背包时, 2x2 合成格(窗口0槽1-4)里的物品必须退回背包。
            // 这些槽不参与 .dat 持久化(原版语义: 关闭即返还), 曾直接遗留在数组里,
            // 重进游戏后物品"消失"(实际是存进永不读回的槽位)。
            if (closedWindowId == 0) {
                for (int s = 1; s <= 4; s++) {
                    int gid = data.inventoryIds[s], gcnt = data.inventoryCounts[s];
                    if (gid > 0 && gcnt > 0) {
                        ItemMeta gm = playerSlotMeta(s);
                        int got = pickupItemCount(gid, gcnt, gm.enchants(), gm.potion(), gm.customName(),
                                gm.damage(), gm.trimMaterial(), gm.trimPattern());
                        if (got < gcnt) dropItemInFront(gid, gcnt - got, gm);
                        data.inventoryIds[s] = 0;
                        data.inventoryCounts[s] = 0;
                        writePlayerSlotMeta(s, null);
                    }
                }
                sendInventoryUpdate();
            }
            String[] leftoverGrid = openCraftingGrids.remove(closedWindowId);
            int[] leftoverCounts = openCraftingCounts.remove(closedWindowId);
            ItemMeta[] leftoverMetas = openGridMetas.remove(closedWindowId);
            if (leftoverGrid != null) {
                for (int gi = 0; gi < 9; gi++) {
                    if (leftoverGrid[gi] != null) {
                        int lid = BlockManager.getItemIdByName(leftoverGrid[gi]);
                        int lc = (leftoverCounts != null && leftoverCounts[gi] > 0) ? leftoverCounts[gi] : 1;
                        if (lid > 0) {
                            // Bug4/33: 网格原料的组件(放入附魔/改名物品)退回时不丢
                            ItemMeta gm = leftoverMetas != null && gi < leftoverMetas.length
                                    && leftoverMetas[gi] != null ? leftoverMetas[gi] : ItemMeta.EMPTY;
                            int got = pickupItemCount(lid, lc, gm.enchants(), gm.potion(), gm.customName(),
                                    gm.damage(), gm.trimMaterial(), gm.trimPattern());
                            if (got < lc) dropItemInFront(lid, lc - got, gm);
                        }
                        leftoverGrid[gi] = null;
                    }
                }
            }
            com.CharunCore.server.plugin.api.Inventory closedPlugin = openPluginMenus.remove(closedWindowId);
            if (closedPlugin != null) firePluginMenuClose(closedWindowId, closedPlugin);
            ContainerStore.Pos closedChest = openChests.remove(closedWindowId);
            if (closedChest != null) {
                ContainerStore.decrementViewers(closedChest); // P9-B2 陷阱箱查看者计数
                persistChest(closedChest);
                // 关闭箱盖动画: block_event action=1 param=0 (open=false)
                sendChestBlockEvent(closedChest, false);
                java.util.Set<NetworkHandler> obs = chestObservers.get(closedChest);
                if (obs != null) { obs.remove(this); if (obs.isEmpty()) chestObservers.remove(closedChest); }
            }
            // Bug51: 大箱子另一半同步关闭
            ContainerStore.Pos closedPartner = openChestPartners.remove(closedWindowId);
            if (closedPartner != null) {
                ContainerStore.decrementViewers(closedPartner);
                persistChest(closedPartner);
                sendChestBlockEvent(closedPartner, false);
                java.util.Set<NetworkHandler> pobs = chestObservers.get(closedPartner);
                if (pobs != null) { pobs.remove(this); if (pobs.isEmpty()) chestObservers.remove(closedPartner); }
            }
            openEnderChests.remove(closedWindowId);
            openFurnaces.remove(closedWindowId);
            openHoppers.remove(closedWindowId);
            ContainerStore.Pos closedDispenser = openDispensers.remove(closedWindowId);
            if (closedDispenser != null) ContainerStore.persistDispenser(closedDispenser);
            // #50: 锻造台关闭退回三输入槽(模板/底材/附加), 结果槽不退(原版 SmithingMenu.removed)。
            ContainerStore.Pos closedSmithing = openSmithing.remove(closedWindowId);
            if (closedSmithing != null) {
                ContainerStore.SmithingData sd = ContainerStore.peekSmithing(closedSmithing);
                if (sd != null) {
                    for (int i = 0; i < 3; i++) {
                        returnSlotToPlayer(sd.slots[i * 2], sd.slots[i * 2 + 1], contMeta(sd.meta, i));
                        sd.slots[i * 2] = 0;
                        sd.slots[i * 2 + 1] = 0;
                    }
                    sd.version++;
                    ContainerStore.removeSmithing(closedSmithing);
                }
            }
            // #50: 附魔台关闭退回物品+青金石(原版 EnchantmentMenu.removed)。
            ContainerStore.Pos closedEnch = openEnchanting.remove(closedWindowId);
            if (closedEnch != null) {
                ContainerStore.EnchantingData ed = ContainerStore.peekEnchanting(closedEnch);
                if (ed != null) {
                    returnSlotToPlayer(ed.slots[0], ed.slots[1], contMeta(ed.meta, 0));
                    returnSlotToPlayer(ed.slots[2], ed.slots[3], contMeta(ed.meta, 1));
                    ed.slots[0] = 0; ed.slots[1] = 0; ed.slots[2] = 0; ed.slots[3] = 0;
                    ed.version++;
                    ContainerStore.removeEnchanting(closedEnch);
                }
            }
            // #7: 关闭铁砧菜单时, 把仍留在铁砧里的输入物品退回玩家背包(原版 AnvilMenu.removed)。
            // #4 修复: 只退回两个输入槽(0/1); 派生输出槽(2)不是玩家真正放入的物品, 曾一并退回
            // -> 两根铁剑放进去后再按 esc, 背包里多出第三根(输出)铁剑。
            ContainerStore.Pos closedAnvil = openAnvil.remove(closedWindowId);
            if (closedAnvil != null) {
                ContainerStore.AnvilData ad = ContainerStore.anvil(closedAnvil);
                for (int i = 0; i < 2; i++) {
                    int itId = ad.slots[i * 2], cnt = ad.slots[i * 2 + 1];
                    if (itId > 0 && cnt > 0) {
                        // 注意: 此时窗口已从 openAnvil 移除, 直接读 AnvilData 字段
                        ItemMeta am = i == 0
                                ? ItemMeta.of(ad.leftEnchants, ad.leftPotion, ad.leftName, ad.leftDamage, -1, -1)
                                : ItemMeta.of(ad.rightEnchants, ad.rightPotion, ad.rightName, ad.rightDamage, -1, -1);
                        int got = pickupItemCount(itId, cnt, am.enchants(), am.potion(), am.customName(),
                                am.damage(), am.trimMaterial(), am.trimPattern());
                        if (got < cnt) dropItemInFront(itId, cnt - got, am);
                        ad.slots[i * 2] = 0;
                        ad.slots[i * 2 + 1] = 0;
                    }
                }
                ad.slots[4] = 0; ad.slots[5] = 0;
                ad.leftEnchants.clear(); ad.rightEnchants.clear();
                ad.leftDamage = 0; ad.rightDamage = 0; ad.outDamage = 0;
                ad.leftPotion = null; ad.rightPotion = null; ad.outPotion = null;
                ad.leftName = null; ad.rightName = null; ad.rename = "";
                ad.version++;
                ContainerStore.removeAnvil(closedAnvil);
            }
            openBrewing.remove(closedWindowId);
            // #15 信标: 关闭时若支付物槽还有物品, 退回背包(原版 BeaconMenu.removed drop)。
            ContainerStore.Pos closedBeacon = openBeacons.remove(closedWindowId);
            if (closedBeacon != null) {
                ContainerStore.BeaconData bd = ContainerStore.beacon(closedBeacon);
                if (bd.paymentSlot[0] > 0 && bd.paymentSlot[1] > 0) {
                    giveItem(bd.paymentSlot[0], bd.paymentSlot[1]);
                    bd.paymentSlot[0] = 0; bd.paymentSlot[1] = 0;
                    bd.payment = false;
                }
                bd.version++;
            }
            if (beaconWindowId == closedWindowId) beaconWindowId = -1;
            // #29 命令方块
            openCommandBlocks.remove(closedWindowId);
            if (commandBlockWindowId == closedWindowId) commandBlockWindowId = -1;
            // #50: 切石机/砂轮关闭退回输入槽(原版 StonecutterMenu/GrindstoneMenu.removed)。
            ContainerStore.Pos closedStonecutter = openStonecutters.remove(closedWindowId);
            if (closedStonecutter != null) {
                ContainerStore.StonecutterData sd = ContainerStore.peekStonecutter(closedStonecutter);
                if (sd != null) {
                    returnSlotToPlayer(sd.slots[0], sd.slots[1], contMeta(sd.meta, 0));
                    sd.slots[0] = 0; sd.slots[1] = 0;
                    sd.version++;
                    ContainerStore.removeStonecutter(closedStonecutter);
                }
            }
            ContainerStore.Pos closedGrindstone = openGrindstones.remove(closedWindowId);
            if (closedGrindstone != null) {
                ContainerStore.GrindstoneData gd = ContainerStore.peekGrindstone(closedGrindstone);
                if (gd != null) {
                    returnSlotToPlayer(gd.slots[0], gd.slots[1], contMeta(gd.meta, 0));
                    returnSlotToPlayer(gd.slots[2], gd.slots[3], contMeta(gd.meta, 1));
                    gd.slots[0] = 0; gd.slots[1] = 0; gd.slots[2] = 0; gd.slots[3] = 0;
                    gd.version++;
                    ContainerStore.removeGrindstone(closedGrindstone);
                }
            }
            openMerchants.remove(closedWindowId);
            if (merchantWindowId == closedWindowId) merchantWindowId = -1;
            containerSyncVersion.remove(closedWindowId);
            if (carriedItemCount > 0) {
                // Bug4/33: 光标物品(可能带附魔/改名/耐久)退回背包时携带组件
                ItemMeta cm = carriedSnapshot();
                int got = pickupItemCount(carriedItemId, carriedItemCount, cm.enchants(), cm.potion(),
                        cm.customName(), cm.damage(), cm.trimMaterial(), cm.trimPattern());
                if (got < carriedItemCount) dropItemInFront(carriedItemId, carriedItemCount - got, cm);
                carriedItemId = 0;
                carriedItemCount = 0;
                clearCarriedMeta();
            }
            // 关闭任意容器后, 主动把主背包(windowId 0)整体重发给客户端,
            // 避免熔炉/箱子交互后客户端背包视图停留在被污染的旧快照(物品看似消失/回退)。
            sendInventoryUpdate();
        }

        // ── PLACE RECIPE (配方书点击填充) ─────────────────────────────────────
        else if (id == 0x26) { // SERVERBOUND_PLACE_RECIPE — 客户端点击配方书触发
            int windowId = in.readVarInt();   // ContainerID = varint
            int recipeId = in.readVarInt();   // 服务端自分配的 displayId
            in.readByte();                    // makeAll (useMaxItems) — 暂忽略
            RecipeRegistry.Recipe r = RECIPE_BOOK_BY_ID.get(recipeId);
            if (r == null) return;
            if (windowId == 0) {
                // 玩家随身 2×2 网格(槽 1-4 ↔ grid[0,1,3,4]); 仅支持能放入 2×2 的配方
                boolean fits2x2 = (r.grid[2] == 0 && r.grid[5] == 0
                    && r.grid[6] == 0 && r.grid[7] == 0 && r.grid[8] == 0);
                if (!fits2x2) return;
                // 先把 2×2 现有物品放回背包
                for (int s : new int[]{1, 2, 3, 4}) {
                    if (data.inventoryIds[s] > 0 && data.inventoryCounts[s] > 0) {
                        // Bug4/33: 带组件物品退回不丢 NBT
                        ItemMeta gm = playerSlotMeta(s);
                        int got = pickupItemCount(data.inventoryIds[s], data.inventoryCounts[s],
                                gm.enchants(), gm.potion(), gm.customName(), gm.damage(),
                                gm.trimMaterial(), gm.trimPattern());
                        if (got < data.inventoryCounts[s])
                            dropItemInFront(data.inventoryIds[s], data.inventoryCounts[s] - got, gm);
                        data.inventoryIds[s] = 0;
                        data.inventoryCounts[s] = 0;
                        writePlayerSlotMeta(s, null);
                    }
                }
                int[] g2s = {1, 2, 0, 3, 4}; // grid[0..4] → 玩家槽(下标2未用)
                int[] gidx = {0, 1, 3, 4};
                for (int k = 0; k < 4; k++) {
                    int gi = gidx[k];
                    int needId = r.grid[gi]; // 新配方引擎: grid 是 itemId
                    if (needId <= 0) continue;
                    int src = findCraftingSrcById(needId);
                    int slot = g2s[gi];
                    if (src >= 0) {
                        data.inventoryIds[slot] = needId;
                        data.inventoryCounts[slot] = 1;
                        data.inventoryCounts[src]--;
                        if (data.inventoryCounts[src] <= 0) {
                            data.inventoryIds[src] = 0;
                            data.inventoryCounts[src] = 0;
                        }
                    } else {
                        data.inventoryIds[slot] = 0;
                        data.inventoryCounts[slot] = 0;
                    }
                }
                sendInventoryUpdate();
                updateCraftingResult(0);
                return;
            }
            if (!openCraftingGrids.containsKey(windowId)) return; // 仅工作台窗口(windowId>0)支持
            String[] grid = openCraftingGrids.get(windowId);
            int[] counts = openCraftingCounts.get(windowId);
            // 先把当前网格已有物品放回背包（原实现直接清空 → 已放的原料静默丢失）
            ItemMeta[] rbMetas = openGridMetas.get(windowId);
            for (int gi = 0; gi < 9; gi++) {
                if (grid[gi] != null && counts[gi] > 0) {
                    int oldId = BlockManager.getItemIdByName(grid[gi]);
                    if (oldId > 0) {
                        ItemMeta gm = rbMetas != null && gi < rbMetas.length && rbMetas[gi] != null
                                ? rbMetas[gi] : ItemMeta.EMPTY;
                        int got = pickupItemCount(oldId, counts[gi], gm.enchants(), gm.potion(),
                                gm.customName(), gm.damage(), gm.trimMaterial(), gm.trimPattern());
                        if (got < counts[gi]) dropItemInFront(oldId, counts[gi] - got, gm);
                    }
                }
                grid[gi] = null; counts[gi] = 0;
                if (rbMetas != null && gi < rbMetas.length) rbMetas[gi] = null;
            }
            // 逐格从背包找料并扣减（原实现只写快照不扣背包 → 配方书点选=白嫖合成）
            for (int gi = 0; gi < 9; gi++) {
                int needId = r.grid[gi]; // 新配方引擎: grid 是 itemId
                if (needId <= 0) continue;
                String itemName = BlockManager.itemIdToName(needId);
                if (itemName == null) continue;
                int src = findCraftingSrcById(needId);
                if (src >= 0) {
                    grid[gi] = itemName;
                    counts[gi] = 1;
                    data.inventoryCounts[src]--;
                    if (data.inventoryCounts[src] <= 0) {
                        data.inventoryIds[src] = 0;
                        data.inventoryCounts[src] = 0;
                    }
                } else {
                    grid[gi] = null; counts[gi] = 0;
                }
            }
            sendCraftingTableContent(windowId);
            updateCraftingResult(windowId);
            // 【修复】曾漏掉 sendInventoryUpdate → 扣背包后未通知客户端，UI 仍显示原 8 块
            // 但服务端 grid 已放 4 块，玩家拿结果后再关窗会归还重复造成复制。
            sendInventoryUpdate();
        }

        // ── NAME ITEM (铁砧重命名: 客户端在铁砧输入名字触发) ──────────────────
        // 1.21.11 serverbound 包号：0x2c=pong、0x2f=name_item（protocol_ref 权威）。
        // 曾用 0x2c → 把 pong 的 i32 载荷当窗口/名字读 → 改名无效甚至误解析。
        else if (id == 0x2f) { // SERVERBOUND_NAME_ITEM — name(string)（1.21.11 无 container_id 字段）
            String newName = in.readString();
            if (newName == null) newName = "";
            if (newName.length() > 50) newName = newName.substring(0, 50);
            for (java.util.Map.Entry<Integer, ContainerStore.Pos> e : openAnvil.entrySet()) {
                ContainerStore.AnvilData ad =
                    ContainerStore.anvil(e.getValue());
                if (ad != null) {
                    ad.rename = newName;
                    ad.version++;
                    recomputeAnvil(e.getKey());
                }
            }
        }
    }

    /**
     * 攻击非玩家实体（生物 / 末影龙子部件）。
     * 由 0x19 SERVERBOUND_INTERACT 在目标不是玩家时回落调用。
     */
    private void attackMob(int targetEntityId) {
        Entity target =
            EntityManager.resolveInteractTarget(targetEntityId);
        if (target instanceof LivingEntity living) {
            if (living.deathTime > 0) return;
            if (living.health <= 0) return;
            // 原版攻击范围约 3 格(实体中心距 3.5 内, 含垂直差) —— 防隔墙/隔远打怪
            double adx = living.x - this.x;
            double ady = living.y - this.y;
            double adz = living.z - this.z;
            if (adx * adx + ady * ady + adz * adz > 3.5 * 3.5) return;
            // 创造/旁观左键实体不造成伤害(原版 Creative/Spectator 穿透实体) —— 修复 P2-3
            if (this.gameMode == 3 || this.gameMode == 1) return;

            int weaponId = data.inventoryIds[36 + heldItemSlot];
            String weaponName = weaponId > 0
                ? BlockManager.itemIdToName(weaponId) : null;
            float baseDamage = getAttackDamage(weaponName);
            float cooldownMs = getAttackCooldownMs(weaponName);

            // 近战附魔读取(锋利/击退/火焰) — slot 36..44 为主手槽
            int heldSlot = 36 + heldItemSlot;
            int sharpness = data.getSlotEnchant(heldSlot,
                BlockManager.getEnchantId("sharpness"));
            int knockbackLvl = data.getSlotEnchant(heldSlot,
                BlockManager.getEnchantId("knockback"));
            int fireAspect = data.getSlotEnchant(heldSlot,
                BlockManager.getEnchantId("fire_aspect"));

            long now = System.currentTimeMillis();
            long elapsed = now - this.lastAttackTime;
            float progress = Math.min(1.0f, elapsed / cooldownMs);
            boolean isCrit = progress > 0.9f && !this.onGround
                             && this.y < this.lastY && !this.sprinting;
            float damage = baseDamage * (0.2f + progress * progress * 0.8f);
            if (sharpness > 0) damage += (float) sharpness * 0.5f + 0.5f; // 锋利: +0.5*lvl+0.5
            if (isCrit) damage *= 1.5f;
            // 重锤: 密度(下落加伤)/破甲/风爆
            MaceBonus mb = computeMaceBonus(heldSlot, weaponName, this.fallDistance, 0);
            damage += mb.dmg;
            // 三叉戟近战: 对水生额外伤害
            int impalingLvl = data.getSlotEnchant(heldSlot,
                BlockManager.getEnchantId("impaling"));
            if ("trident".equals(weaponName) && impalingLvl > 0 && isAquaticMob(living.typeName)) {
                damage += impalingLvl * 2.5f;
            }
            this.lastAttackTime = now;

            // P4-2: 记录玩家为击杀者, 由 MobEntity.onDeath 统一生成可拾取经验球
            living.lastAttacker = this;

            var dmgByEntity = EVENTS.fire(new EntityDamageByEntityEvent(
                    living, this, damage, "player"));
            if (dmgByEntity.isCancelled()) return;
            damage = dmgByEntity.getAmount();

            living.damage(damage, "player");

            // 火焰附加: 点燃目标 fire_aspect*lvl 秒(80 刻/级)
            if (fireAspect > 0) {
                living.fireTicks = Math.max(living.fireTicks, 80 * fireAspect);
            }

            double kdx = living.x - this.x;
            double kdz = living.z - this.z;
            double kh = Math.sqrt(kdx * kdx + kdz * kdz);
            if (kh > 0.001 && !(living instanceof EnderDragonEntity)) {
                double kb = this.sprinting ? 0.8 : 0.4;
                if (knockbackLvl > 0) kb += 0.5 * knockbackLvl;
                kb += mb.kb;
                living.vx += kdx / kh * kb;
                living.vz += kdz / kh * kb;
                living.vy = Math.max(living.vy, 0.25 + mb.kbUp);
            }

            if (isCrit) {
                broadcastAnimation(living.id, 4);
                sendSoundAt("minecraft:entity.player.attack.crit", living.x, living.y, living.z, 1.0f, 1.0f);
            } else {
                sendSoundAt(progress > 0.9f
                        ? "minecraft:entity.player.attack.strong"
                        : "minecraft:entity.player.attack.weak",
                    living.x, living.y, living.z, 1.0f, 1.0f);
            }
            broadcastAnimation(this.eid, 0);

            if (this.gameMode == 0) {
                addExhaustion(0.1f);
                if (weaponName != null) {
                    damageHeldItem(36 + heldItemSlot,
                        weaponName.endsWith("_sword") || weaponName.endsWith("_axe") ? 1 : 2);
                }
            }

            if (living.health <= 0) {
                // P4-4: 不再直接 addExperience; 经验球由 MobEntity.onDeath 经 EntityManager.spawnExperienceOrbs 生成。
                // 成就系统：击杀生物事件 (P12)
                AdvancementManager.onMobKill(this, living.typeName);
                StatisticsManager.add(this, "killed", living.typeName, 1);
            }
        }
    }

    /** P4-6: 读取当前主手武器的抢夺(looting)附魔等级。 */
    public int getLootingLevel() {
        int slot = 36 + heldItemSlot;
        return data.getSlotEnchant(slot, BlockManager.getEnchantId("looting"));
    }

    // =========================================================================
    // COMMAND HANDLER
    // =========================================================================

    /**
     * Handles a command string (the '/' has already been stripped).
     * Sends feedback via system message to the executing player only.
     */
    // =========================================================================
    // 权限 / OP 持久化 (Creative P1-2)
    // =========================================================================
    /** 是否为 OP。未配置任何 OP(ops.json 为空/不存在)时返回 true 以保持与原"全员可执指令"行为一致, 避免首玩家被锁; 一旦配置则仅 OP 可执行管理指令。 */
    /** 玩家 OP 等级 (0-5)。ops.json 为空时为 5(开发模式); UUID 优先匹配防改名漂移。 */
    public int opLevel() {
        return OpList.level(this.uuid, this.username);
    }

    private boolean isOp() {
        return opLevel() >= 1;
    }
    private void addOp(String name) { OpList.addOp(name, 4); }
    private void removeOp(String name) { OpList.removeOp(name); }

    /** 禁言表: uuid → 解禁时间戳(ms)。 */
    private static final java.util.Map<java.util.UUID, Long> MUTED_UNTIL = new java.util.concurrent.ConcurrentHashMap<>();

    private static final java.util.Set<String> ADMIN_COMMANDS = java.util.Set.of(
        "op","deop","gamemode","gm","kick","difficulty","gamerule","time","weather","give","world",
        "spawnpoint","setblock","fill","clone","clear","kill","heal","feed","summon","effect",
        "enchant","xp","experience","tp","tphere","invsee");

    // =========================================================================
    // @ 目标选择器 (Creative P1-1) —— 委托给通用选择器解析层, 不重复实现。
    // 玩家型指令统一走 resolvePlayers(@a/@p/@r/@s + 用户名匹配); @e 由解析层
    // 留给实体型指令, 玩家指令中不返回实体。
    // =========================================================================
    private java.util.List<NetworkHandler> resolveSelector(String sel) {
        return EntitySelector.resolvePlayers(sel, this);
    }

    private void handleCommand(ChannelHandlerContext ctx,String cmd) {
        if (Server.get().getPluginManager()
                .dispatchCommand(new com.CharunCore.server.plugin.api.Player(this), cmd)) {
            return;
        }
        var cmdEvent = EVENTS.fire(new PlayerCommandPreprocessEvent(this, cmd));
        if (cmdEvent.isCancelled()) return;
        cmd = cmdEvent.getCommand();
        System.out.println("[命令] " + username + ": /" + cmd);
        String[] parts = cmd.split("\\s+");
        if (parts.length == 0) return;
        String cmd0 = parts[0].toLowerCase();

        // OP 分级权限校验: /tp 单目标(传送到玩家) = Lv1, 其余 tp = Lv2
        int required = Permissions.requiredLevel(cmd0);
        if (cmd0.equals("tp") && parts.length == 2) required = 1;
        if (required > opLevel()) {
            sendFeedback("权限不足: 需要 OP Lv" + required + " (你当前 Lv" + opLevel() + ")", "red");
            return;
        }

        switch (cmd0) {

            // /time set <ticks|day|night|noon|midnight>
            // /time add <ticks>
            // /time query daytime|gametime|day
            case "time" -> {
                if (parts.length < 2) { sendFeedback("用法: /time set|add|query <值>", "red"); return; }
                switch (parts[1].toLowerCase()) {
                    case "set" -> {
                        if (parts.length < 3) { sendFeedback("用法: /time set <值>", "red"); return; }
                        long val = parseTimeValue(parts[2]);
                        if (val < 0) { sendFeedback("未知时间值: " + parts[2], "red"); return; }
                        Main.dayTime = val % 24000;
                        broadcastTime();
                        broadcastSystemMessage("[服务器] 时间已设置为 " + Main.dayTime, "gray");
                    }
                    case "add" -> {
                        if (parts.length < 3) { sendFeedback("用法: /time add <ticks>", "red"); return; }
                        try {
                            Main.dayTime = (Main.dayTime + Long.parseLong(parts[2])) % 24000;
                            broadcastTime();
                            sendFeedback("时间已增加 " + parts[2] + " ticks", "gray");
                        } catch (NumberFormatException e) { sendFeedback("无效数字: " + parts[2], "red"); }
                    }
                    case "query" -> {
                        if (parts.length < 3) { sendFeedback("用法: /time query daytime|gametime|day", "red"); return; }
                        switch (parts[2].toLowerCase()) {
                            case "daytime"  -> sendFeedback("当前游戏时间: " + Main.dayTime, "gray");
                            case "gametime" -> sendFeedback("总世界龄: " + Main.worldAge, "gray");
                            case "day"      -> sendFeedback("当前天数: " + (Main.worldAge / 24000), "gray");
                            default         -> sendFeedback("未知 query 类型: " + parts[2], "red");
                        }
                    }
                    default -> sendFeedback("用法: /time set|add|query <值>", "red");
                }
            }

            // /tp <x> <y> <z>
            // /tp <player>
            case "tp" -> {
                try {
                    boolean coordForm = false;
                    double tx = 0, ty = 0, tz = 0;
                    if (parts.length >= 4) {
                        tx = parseRelCoord(parts[1], x); ty = parseRelCoord(parts[2], y); tz = parseRelCoord(parts[3], z);
                        coordForm = true;
                    }
                    if (coordForm) {
                        if (parts.length >= 5) {
                            for (NetworkHandler t : resolveSelector(parts[4])) {
                                teleportPlayer(t, tx, ty, tz);
                                if (t.ctx != null) t.sendFeedback("已传送到 " + tx + " " + ty + " " + tz, "gray");
                            }
                            sendFeedback("已传送目标到坐标", "gray");
                        } else {
                            teleportPlayer(this, tx, ty, tz);
                            sendFeedback("已传送到 " + tx + " " + ty + " " + tz, "gray");
                        }
                    } else if (parts.length == 2) {
                        java.util.List<NetworkHandler> ts = resolveSelector(parts[1]);
                        if (ts.isEmpty()) { sendFeedback("目标不存在: " + parts[1], "red"); return; }
                        for (NetworkHandler t : ts) teleportPlayer(this, t.x, t.y, t.z);
                        sendFeedback("已传送到 " + parts[1], "gray");
                    } else if (parts.length == 3) {
                        // /tp <源> <目的>
                        java.util.List<NetworkHandler> dst = resolveSelector(parts[2]);
                        if (dst.isEmpty()) { sendFeedback("目标不存在: " + parts[2], "red"); return; }
                        NetworkHandler d = dst.get(0);
                        for (NetworkHandler s : resolveSelector(parts[1])) teleportPlayer(s, d.x, d.y, d.z);
                        sendFeedback("已传送 " + parts[1] + " 到 " + parts[2], "gray");
                    } else {
                        sendFeedback("用法: /tp <x> <y> <z> [目标] | /tp <目标> | /tp <源> <目的>", "red");
                    }
                } catch (NumberFormatException e) { sendFeedback("无效坐标", "red"); }
            }

            // /tpa <玩家> | /tpahere <玩家> | /tpaccept | /tpdeny
            case "tpa", "tpahere" -> {
                if (parts.length < 2) { sendFeedback("用法: /" + cmd0 + " <玩家>", "red"); return; }
                NetworkHandler target = resolveSelector(parts[1]).stream().findFirst().orElse(null);
                if (target == null) { sendFeedback("玩家不在线: " + parts[1], "red"); return; }
                TpaSystem.request(this, target, cmd0.equals("tpahere"));
            }
            case "tpaccept" -> {
                if (!TpaSystem.accept(this)) {
                    sendFeedback("没有待处理的传送请求", "red");
                }
            }
            case "tpdeny" -> {
                if (!TpaSystem.deny(this)) {
                    sendFeedback("没有待处理的传送请求", "red");
                }
            }

            // /gamemode <survival|creative|adventure|spectator|0|1|2|3>
            case "gamemode", "gm" -> {
                if (parts.length < 2) { sendFeedback("用法: /gamemode <模式> [目标]", "red"); return; }
                int mode = parseGameMode(parts[1]);
                if (mode < 0) { sendFeedback("未知游戏模式: " + parts[1], "red"); return; }
                final int finalMode = mode;
                // 目标: 默认自己; 提供第三段则按 @ 选择器/用户名解析(原版 /gamemode <模式> [目标])
                java.util.List<NetworkHandler> targets;
                if (parts.length >= 3) {
                    targets = resolveSelector(parts[2]);
                    if (targets.isEmpty()) { sendFeedback("目标不存在: " + parts[2], "red"); return; }
                } else {
                    targets = java.util.List.of(this);
                }
                String[] names = {"survival", "creative", "adventure", "spectator"};
                for (NetworkHandler t : targets) {
                    if (t.ctx != null) {
                        // Change Game State reason 3 = change game mode
                        t.sendPacket(t.ctx, 0x26, pb -> { pb.writeByte(3); pb.writeFloat(finalMode); });
                        // 同步飞行能力(创造/旁观允许飞行) —— 修复 P2-4: 指令切模式后也能飞行
                        t.allowFlight = (finalMode == 1 || finalMode == 3);
                        t.sendAbilitiesUpdate();
                    }
                    // 更新全体 tab-list 中的游戏模式
                    for (NetworkHandler h : players.values()) {
                        if (h.ctx == null) continue;
                        h.sendPacket(h.ctx, 0x44, pb -> {
                            pb.writeByte(0x04); // UPDATE_GAME_MODE action
                            pb.writeVarInt(1);
                            pb.writeUUID(t.uuid);
                            pb.writeVarInt(finalMode);
                        });
                    }
                    int oldMode = t.gameMode;
                    t.gameMode = mode;
                    t.syncEntityVisibilityToOthers(oldMode);
                    broadcastSystemMessage("[服务器] " + t.username + " 的游戏模式已改为 " + names[finalMode], "gray");
                }
            }

            case "locate" -> {
                if (parts.length < 2) {
                    sendFeedback("用法: /locate <结构名>", "red");
                    sendFeedback("可用结构集: " + String.join(", ",
                        StructureSet.loadAll().keySet()), "gray");
                    return;
                }
                String structureName = parts[1].toLowerCase();
                int playerChunkX = ((int) x) >> 4;
                int playerChunkZ = ((int) z) >> 4;
                long seed = WorldManager.getSeed();
                // 先走数据驱动结构集(random_spread 类，与其生成器一致)
                int[] result = locateStructure(structureName, playerChunkX, playerChunkZ, seed);
                // stronghold 用 concentric_rings 放置(结构集无法表示)。
                // #9: 曾回退到旧 StructureManager.findNearest 的手写环近似数学, 与生成器实际使用的
                // ConcentricRingsStructurePlacement(ringPositions)不一致 -> 定位到从未生成的位置。
                // 现直接读生成器同源 ringPositions 取最近环点。
                if (result == null && (structureName.equals("stronghold") || structureName.equals("strongholds"))) {
                    result = locateStrongholdRing(playerChunkX, playerChunkZ, seed);
                }
                if (result == null) {
                    sendFeedback("未找到结构: " + structureName, "red");
                } else {
                    int structChunkX = result[0];
                    int structChunkZ = result[1];
                    int blockX = structChunkX * 16 + 8;
                    int blockZ = structChunkZ * 16 + 8;
                    sendFeedback("找到 " + structureName + " 在 [" + blockX + ", ~, " + blockZ
                        + "] (相对 " + (structChunkX - playerChunkX) + ", " + (structChunkZ - playerChunkZ) + " 区块)", "green");
                }
            }

            // /give <物品名> [数量]
            case "give" -> {
                if (parts.length < 2) { sendFeedback("用法: /give [目标] <物品名> [数量]", "red"); return; }
                // 语法: /give [目标] <物品> [数量]; 若第二段是 @ 选择器或在线玩家名则视为目标
                java.util.List<NetworkHandler> targets;
                String itemName;
                int amount = 1;
                boolean hasTarget = parts.length >= 3
                    && (parts[1].startsWith("@") || players.values().stream().anyMatch(p -> p.username.equalsIgnoreCase(parts[1])));
                if (hasTarget) {
                    targets = resolveSelector(parts[1]);
                    if (targets.isEmpty()) { sendFeedback("目标不存在: " + parts[1], "red"); return; }
                    itemName = parts[2].startsWith("minecraft:") ? parts[2].substring(10) : parts[2];
                    if (parts.length >= 4) { try { amount = Integer.parseInt(parts[3]); } catch (NumberFormatException e) { } }
                } else {
                    targets = java.util.List.of(this);
                    itemName = parts[1].startsWith("minecraft:") ? parts[1].substring(10) : parts[1];
                    if (parts.length >= 3) { try { amount = Integer.parseInt(parts[2]); } catch (NumberFormatException e) { } }
                }
                int itemId = BlockManager.getItemIdByName(itemName);
                if (itemId <= 0) { sendFeedback("未知物品: " + itemName, "red"); return; }
                // 数量上限校验(堆遇上界, 通用上限 64) —— 修复 P1-6
                if (amount <= 0) amount = 1;
                if (amount > 64) { sendFeedback("数量超出堆叠上限(64), 已截断为 64", "yellow"); amount = 64; }
                for (NetworkHandler t : targets) t.giveItem(itemId, amount);
                sendFeedback("已给予 " + targets.size() + " 名目标 " + amount + "x " + itemName, "gray");
            }

            // /clear [物品名] [数量]
            case "clear" -> {
                if (parts.length < 2) {
                    for (int i = 0; i < 46; i++) {
                        data.inventoryIds[i] = 0;
                        data.inventoryCounts[i] = 0;
                    }
                    sendInventoryUpdate();
                    sendFeedback("已清空物品栏", "gray");
                } else {
                    String itemName = parts[1].startsWith("minecraft:") ? parts[1].substring(10) : parts[1];
                    int itemId = BlockManager.getItemIdByName(itemName);
                    if (itemId <= 0) { sendFeedback("未知物品: " + itemName, "red"); return; }
                    int cleared = 0;
                    for (int i = 0; i < 46; i++) {
                        if (data.inventoryIds[i] == itemId) {
                            cleared += data.inventoryCounts[i];
                            data.inventoryIds[i] = 0;
                            data.inventoryCounts[i] = 0;
                        }
                    }
                    sendInventoryUpdate();
                    sendFeedback("已清除 " + cleared + "x " + itemName, "gray");
                }
            }

            // /kill [目标]
            case "kill" -> {
                if (parts.length >= 2 && parts[1].startsWith("@e[type=")) {
                    // #27: kill @e[type=item] 清除所有掉落物; type 可指定任意实体类型,
                    // 前导 ! = 不选该类型(其余全部清除)。原版实体选择器语义。
                    String arg = parts[1].trim();
                    String inner = arg.substring(arg.indexOf('[') + 1, arg.lastIndexOf(']'));
                    String typeExpr = "";
                    for (String kv : inner.split(",")) {
                        String[] p = kv.split("=", 2);
                        if (p.length == 2 && p[0].trim().equals("type")) typeExpr = p[1].trim();
                    }
                    if (typeExpr.isEmpty()) { sendFeedback("用法: /kill @e[type=实体类型] (支持 ! 前缀排除)", "red"); return; }
                    boolean negate = typeExpr.startsWith("!");
                    String typeName = negate ? typeExpr.substring(1) : typeExpr;
                    java.util.List<com.CharunCore.server.world.entity.Entity> toKill = new java.util.ArrayList<>();
                    for (com.CharunCore.server.world.entity.Entity e : com.CharunCore.server.world.entity.EntityManager.getAllEntities()) {
                        if (e.dim != this.currentDim) continue;
                        String et = e.typeName == null ? "" : e.typeName;
                        boolean match = et.equals(typeName);
                        if (negate ? !match : match) toKill.add(e);
                    }
                    for (com.CharunCore.server.world.entity.Entity e : toKill) {
                        if (e instanceof com.CharunCore.server.world.entity.MobEntity mob) {
                            mob.health = 0;
                            com.CharunCore.server.world.entity.EntityManager.removeEntity(e.id);
                        } else {
                            com.CharunCore.server.world.entity.EntityManager.removeEntity(e.id);
                        }
                    }
                    sendFeedback("已清除 " + toKill.size() + " 个 " + (negate ? "非" + typeName : typeName) + " 实体", "gray");
                    return;
                }
                if (parts.length >= 2) {
                    java.util.List<NetworkHandler> ts = resolveSelector(parts[1]);
                    if (ts.isEmpty()) { sendFeedback("目标不存在: " + parts[1], "red"); return; }
                    for (NetworkHandler target : ts) {
                        target.health = 0;
                        target.isDead = true;
                        target.sendHealthUpdate();
                        target.sendDeathScreen(target.username + " 被击杀");
                    }
                    broadcastSystemMessage("[服务器] " + ts.size() + " 名玩家被击杀", "gray");
                } else {
                    this.health = 0; this.isDead = true; this.sendHealthUpdate();
                    this.sendDeathScreen(username + " 被击杀");
                    broadcastSystemMessage("[服务器] " + username + " 被击杀", "gray");
                }
            }

            // /killitem [半径] - 清除所有掉落物(可选半径, 默认全图)。解决水中掉落物抽搐/堆积问题。
            case "killitem", "killitems", "clearitems", "removeitems" -> {
                int radius = -1; // -1 = 全图
                if (parts.length >= 2) {
                    try { radius = Integer.parseInt(parts[1]); } catch (NumberFormatException ignored) {}
                }
                int removed = 0;
                java.util.List<com.CharunCore.server.world.entity.Entity> toRemove = new java.util.ArrayList<>();
                for (com.CharunCore.server.world.entity.Entity e : com.CharunCore.server.world.entity.EntityManager.getAllEntities()) {
                    if (!(e instanceof com.CharunCore.server.world.entity.ItemEntity)) continue;
                    if (e.dim != this.currentDim) continue;
                    if (radius > 0) {
                        double dx = e.x - x, dz = e.z - z;
                        if (dx * dx + dz * dz > (double) radius * radius) continue;
                    }
                    toRemove.add(e);
                }
                for (com.CharunCore.server.world.entity.Entity e : toRemove) {
                    com.CharunCore.server.world.entity.EntityManager.removeEntity(e.id);
                    removed++;
                }
                sendFeedback("已清除 " + removed + " 个掉落物", "gray");
            }

            // /heal [目标]
            case "heal" -> {
                java.util.List<NetworkHandler> ts = parts.length >= 2 ? resolveSelector(parts[1]) : java.util.List.of(this);
                if (ts.isEmpty()) { sendFeedback("目标不存在: " + parts[1], "red"); return; }
                for (NetworkHandler target : ts) {
                    target.health = 20.0f;
                    target.data.food = 20;
                    target.sendHealthUpdate();
                }
                sendFeedback("已治疗 " + ts.size() + " 名目标", "green");
            }

            // /feed [目标]
            case "feed" -> {
                java.util.List<NetworkHandler> ts = parts.length >= 2 ? resolveSelector(parts[1]) : java.util.List.of(this);
                if (ts.isEmpty()) { sendFeedback("目标不存在: " + parts[1], "red"); return; }
                for (NetworkHandler target : ts) {
                    target.data.food = 20;
                    target.sendHealthUpdate();
                }
                sendFeedback("已喂饱 " + ts.size() + " 名目标", "green");
            }

            // /weather <clear|rain|thunder> [时长]
            case "weather" -> {
                if (parts.length < 2) { sendFeedback("用法: /weather clear|rain|thunder [时长]", "red"); return; }
                switch (parts[1].toLowerCase()) {
                    case "clear" -> {
                        Main.isRaining = false;
                        Main.isThundering = false;
                        Main.rainTarget = 0.0;
                        for (NetworkHandler p : players.values()) {
                            p.sendPacket(p.ctx, 0x26, pb -> { pb.writeByte(1); pb.writeFloat(0); });
                        }
                        sendFeedback("天气正在转晴", "gray");
                    }
                    case "rain" -> {
                        Main.isRaining = true;
                        Main.isThundering = false;
                        Main.rainTarget = 1.0;
                        for (NetworkHandler p : players.values()) {
                            p.sendPacket(p.ctx, 0x26, pb -> { pb.writeByte(2); pb.writeFloat(1); });
                        }
                        sendFeedback("天气正在转雨", "gray");
                    }
                    case "thunder" -> {
                        Main.isRaining = true;
                        Main.isThundering = true;
                        Main.rainTarget = 1.0;
                        for (NetworkHandler p : players.values()) {
                            p.sendPacket(p.ctx, 0x26, pb -> { pb.writeByte(7); pb.writeFloat(1); });
                            p.sendPacket(p.ctx, 0x26, pb -> { pb.writeByte(2); pb.writeFloat(1); });
                        }
                        sendFeedback("天气正在转雷暴", "gray");
                    }
                    default -> sendFeedback("未知天气: " + parts[1], "red");
                }
            }

            // /summon <实体类型> [x y z]
            case "summon" -> {
                if (parts.length < 2) { sendFeedback("用法: /summon <实体类型> [x y z]", "red"); return; }
                String entityType = parts[1].startsWith("minecraft:") ? parts[1].substring(10) : parts[1];
                double sx = x, sy = y, sz = z;
                if (parts.length >= 5) {
                    try {
                        sx = parts[2].equals("~") ? x : Double.parseDouble(parts[2]);
                        sy = parts[3].equals("~") ? y : Double.parseDouble(parts[3]);
                        sz = parts[4].equals("~") ? z : Double.parseDouble(parts[4]);
                    } catch (NumberFormatException e) { sendFeedback("无效坐标", "red"); return; }
                }
                if (entityType.equals("lightning_bolt")) {
                    sendFeedback("闪电召唤功能待实现", "gray");
                } else {
                    MobEntity mob =
                        new MobEntity(
                            EntityManager.allocateId(), entityType, sx, sy, sz);
                    EntityManager.addEntity(mob);
                    sendFeedback("已召唤 " + entityType, "gray");
                }
            }

            // /xp <数量> [玩家]  或  /experience add <数量>
            case "xp", "experience" -> {
                int amtIdx0 = 1;
                if (parts[0].equalsIgnoreCase("experience") && parts.length >= 2 && parts[1].equalsIgnoreCase("add")) {
                    amtIdx0 = 2;
                }
                final int amtIdx = amtIdx0;
                if (parts.length <= amtIdx) { sendFeedback("用法: /xp <数量> [玩家]", "red"); return; }
                try {
                    int amount = Integer.parseInt(parts[amtIdx]);
                    if (parts.length > amtIdx + 1) {
                        java.util.List<NetworkHandler> ts = resolveSelector(parts[amtIdx + 1]);
                        if (ts.isEmpty()) { sendFeedback("目标不存在: " + parts[amtIdx + 1], "red"); return; }
                        for (NetworkHandler t : ts) t.addExperience(amount);
                        sendFeedback("已给予 " + ts.size() + " 名目标 " + amount + " 经验值", "gray");
                    } else {
                        this.addExperience(amount);
                        sendFeedback("已给予 " + amount + " 经验值", "gray");
                    }
                } catch (NumberFormatException e) { sendFeedback("无效数字", "red"); }
            }

            // /enchant <附魔类型> [等级]
            case "enchant" -> {
                if (parts.length < 2) { sendFeedback("用法: /enchant <附魔类型> [等级]", "red"); return; }
                String enchantName = parts[1].startsWith("minecraft:") ? parts[1].substring(10) : parts[1];
                int level = 1;
                if (parts.length >= 3) { try { level = Integer.parseInt(parts[2]); } catch (NumberFormatException e) { } }
                int slot = 36 + heldItemSlot;
                if (data.inventoryIds[slot] == 0) { sendFeedback("你手中没有物品", "red"); return; }
                sendFeedback("附魔功能待实现: " + enchantName + " " + level, "gray");
            }

            // /effect <give|clear> [玩家] <效果> [秒] [放大器]
            case "effect" -> {
                if (parts.length < 2) { sendFeedback("用法: /effect give|clear [玩家] <效果> [秒] [放大器]", "red"); return; }
                if (parts[1].equalsIgnoreCase("clear")) {
                    clearEffects();
                    sendFeedback("已清除所有药水效果", "gray");
                } else if (parts[1].equalsIgnoreCase("give")) {
                    if (parts.length < 3) { sendFeedback("用法: /effect give <效果> [秒] [放大器]", "red"); return; }
                    String effectName = parts[2];
                    int duration = parts.length >= 4 ? Integer.parseInt(parts[3]) * 20 : 600;
                    int amplifier = parts.length >= 5 ? Integer.parseInt(parts[4]) : 0;
                    addEffect(effectName, amplifier, duration);
                    sendFeedback("已给予效果 " + effectName + " " + (duration/20) + "秒 等级" + amplifier, "gray");
                }
            }

            // /seed
            case "seed" -> {
                sendFeedback("种子: " + WorldManager.getSeed(), "gray");
            }

            // /difficulty <peaceful|easy|normal|hard|0|1|2|3>
            case "difficulty" -> {
                if (parts.length < 2) { sendFeedback("当前难度: " + difficultyName(Main.difficulty), "gray"); return; }
                int diff = switch (parts[1].toLowerCase()) {
                    case "peaceful", "0" -> 0;
                    case "easy", "1" -> 1;
                    case "normal", "2" -> 2;
                    case "hard", "3" -> 3;
                    default -> -1;
                };
                if (diff < 0) { sendFeedback("未知难度: " + parts[1], "red"); return; }
                Main.difficulty = diff;
                for (NetworkHandler p : players.values()) {
                    // 0x0A = change_difficulty (原版 ClientboundChangeDifficultyPacket: byte difficulty + boolean locked)
                    // 曾误用 0x03(award_stats) -> 客户端按 award_stats 解码报 "found N bytes extra" 踢出。
                    p.sendPacket(p.ctx, 0x0A, pb -> {
                        pb.writeByte(diff);
                        pb.writeBoolean(false);
                    });
                }
                sendFeedback("难度已设为 " + difficultyName(diff), "gray");
            }

            // /setblock <x> <y> <z> <方块名> [destroy|keep|replace]
            case "setblock" -> {
                if (parts.length < 5) { sendFeedback("用法: /setblock <x> <y> <z> <方块名> [destroy|keep|replace]", "red"); return; }
                try {
                    int bx = parseCoord(parts[1], (int)x);
                    int by = parseCoord(parts[2], (int)y);
                    int bz = parseCoord(parts[3], (int)z);
                    String blockName = parts[4].startsWith("minecraft:") ? parts[4].substring(10) : parts[4];
                    int blockId = resolveBlockStateId(parts[4]);
                    if (blockId == 0 && !blockName.equals("air")) { sendFeedback("未知方块: " + blockName, "red"); return; }
                    String mode = parts.length >= 6 ? parts[5].toLowerCase() : "replace";
                    if (mode.equals("keep") && WorldManager.getBlockState(this.currentDim,bx, by, bz) != 0) {
                        sendFeedback("方块已存在(keep模式)", "red"); return;
                    }
                    // destroy 模式: 先掉落原方块(尽力而为, 无组件)
                    if (mode.equals("destroy")) {
                        int old = WorldManager.getBlockState(this.currentDim,bx, by, bz);
                        if (old != 0) spawnBlockDrop(bx, by, bz, old);
                    }
                    WorldManager.setBlock(this.currentDim,bx, by, bz, blockId);
                    broadcastBlockChange(this.currentDim, bx, by, bz, blockId);
                    sendFeedback("已设置方块 " + blockName + " 在 " + bx + " " + by + " " + bz, "gray");
                } catch (NumberFormatException e) { sendFeedback("无效坐标", "red"); }
            }

            // /fill <x1> <y1> <z1> <x2> <y2> <z2> <方块名> [replace <旧方块>]
            case "fill" -> {
                if (parts.length < 8) { sendFeedback("用法: /fill <x1> <y1> <z1> <x2> <y2> <z2> <方块名>", "red"); return; }
                try {
                    int x1 = parseCoord(parts[1], (int)x), y1 = parseCoord(parts[2], (int)y), z1 = parseCoord(parts[3], (int)z);
                    int x2 = parseCoord(parts[4], (int)x), y2 = parseCoord(parts[5], (int)y), z2 = parseCoord(parts[6], (int)z);
                    String blockName = parts[7].startsWith("minecraft:") ? parts[7].substring(10) : parts[7];
                    int blockId = resolveBlockStateId(parts[7]);
                    if (blockId == 0 && !blockName.equals("air")) { sendFeedback("未知方块: " + blockName, "red"); return; }
                    // /fill ... replace <filterBlock> : 仅替换匹配的方块
                    Integer filterId = null;
                    if (parts.length >= 10 && parts[8].equalsIgnoreCase("replace")) {
                        filterId = resolveBlockStateId(parts[9]);
                    }
                    int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
                    int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
                    int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);
                    int count = 0;
                    for (int bx = minX; bx <= maxX; bx++) {
                        for (int by = minY; by <= maxY; by++) {
                            for (int bz = minZ; bz <= maxZ; bz++) {
                                if (filterId != null && WorldManager.getBlockState(this.currentDim,bx, by, bz) != filterId) continue;
                                WorldManager.setBlock(this.currentDim,bx, by, bz, blockId);
                                broadcastBlockChange(this.currentDim, bx, by, bz, blockId);
                                count++;
                            }
                        }
                    }
                    sendFeedback("已填充 " + count + " 个方块", "gray");
                } catch (NumberFormatException e) { sendFeedback("无效坐标", "red"); }
            }

            // /clone <x1> <y1> <z1> <x2> <y2> <z2> <x> <y> <z>
            case "clone" -> {
                if (parts.length < 10) { sendFeedback("用法: /clone <x1> <y1> <z1> <x2> <y2> <z2> <x> <y> <z>", "red"); return; }
                try {
                    int x1 = parseCoord(parts[1], (int)x), y1 = parseCoord(parts[2], (int)y), z1 = parseCoord(parts[3], (int)z);
                    int x2 = parseCoord(parts[4], (int)x), y2 = parseCoord(parts[5], (int)y), z2 = parseCoord(parts[6], (int)z);
                    int dx = parseCoord(parts[7], (int)x), dy = parseCoord(parts[8], (int)y), dz = parseCoord(parts[9], (int)z);
                    int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
                    int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
                    int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);
                    int offX = dx - minX, offY = dy - minY, offZ = dz - minZ;
                    for (int bx = minX; bx <= maxX; bx++) {
                        for (int by = minY; by <= maxY; by++) {
                            for (int bz = minZ; bz <= maxZ; bz++) {
                                int state = WorldManager.getBlockState(this.currentDim,bx, by, bz);
                                int nx = bx + offX, ny = by + offY, nz = bz + offZ;
                                WorldManager.setBlock(this.currentDim,nx, ny, nz, state);
                                broadcastBlockChange(this.currentDim, nx, ny, nz, state);
                            }
                        }
                    }
                    sendFeedback("已克隆区域", "gray");
                } catch (NumberFormatException e) { sendFeedback("无效坐标", "red"); }
            }

            // /spawnpoint [玩家] [x y z]
            case "spawnpoint" -> {
                double spx = x, spy = y, spz = z;
                if (parts.length >= 4) {
                    try {
                        spx = parseCoord(parts[1], (int)x);
                        spy = parseCoord(parts[2], (int)y);
                        spz = parseCoord(parts[3], (int)z);
                    } catch (NumberFormatException e) { sendFeedback("无效坐标", "red"); return; }
                }
                WorldManager.setSpawnPoint((int)spx, (int)spy, (int)spz);
                sendFeedback("出生点已设为 " + (int)spx + " " + (int)spy + " " + (int)spz, "gray");
            }

            // /home
            case "home" -> {
                double[] home = WorldManager.getSpawnPoint();
                teleportPlayer(this, home[0] + 0.5, home[1], home[2] + 0.5);
                sendFeedback("已传送到出生点", "gray");
            }

            // /back
            case "back" -> {
                if (lastDeathX != 0 || lastDeathY != 0 || lastDeathZ != 0) {
                    teleportPlayer(this, lastDeathX, lastDeathY, lastDeathZ);
                    sendFeedback("已传送到上次死亡位置", "gray");
                } else {
                    sendFeedback("没有可返回的死亡位置", "red");
                }
            }

            // /gamerule <规则> <值>
            case "gamerule" -> {
                if (parts.length < 2) { sendFeedback("用法: /gamerule <规则> [值]", "red"); return; }
                if (parts.length < 3) {
                    String val = WorldManager.getGameRule(parts[1]);
                    sendFeedback(parts[1] + " = " + (val != null ? val : "(未设置)"), "gray");
                    return;
                }
                WorldManager.setGameRule(parts[1], parts[2]);
                sendFeedback("游戏规则 " + parts[1] + " 已设为 " + parts[2], "gray");
            }

            // /kick <玩家> [原因]
            case "kick" -> {
                if (parts.length < 2) { sendFeedback("用法: /kick <玩家> [原因]", "red"); return; }
                java.util.List<NetworkHandler> ts = resolveSelector(parts[1]);
                if (ts.isEmpty()) { sendFeedback("玩家不在线: " + parts[1], "red"); return; }
                String reason = parts.length >= 3 ? String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length)) : "已被踢出";
                for (NetworkHandler target : ts) {
                    if (target.ctx != null) target.ctx.channel().close();
                }
                broadcastSystemMessage("[服务器] " + ts.size() + " 名玩家被踢出: " + reason, "gray");
            }

            // /op <玩家>
            case "op" -> {
                if (opLevel() < 5) { sendFeedback("只有 Lv5(总负责人) 可以修改 OP", "red"); return; }
                if (parts.length < 2) { sendFeedback("用法: /op <玩家> [等级1-5, 默认4]", "red"); return; }
                int level = 4;
                if (parts.length >= 3) {
                    try { level = Integer.parseInt(parts[2]); } catch (NumberFormatException e) {
                        sendFeedback("无效等级: " + parts[2], "red"); return;
                    }
                    if (level < 1 || level > 5) { sendFeedback("等级范围 1-5", "red"); return; }
                }
                NetworkHandler target = player(parts[1]);
                if (target != null) {
                    OpList.addOp(target.uuid, target.username, level);
                    target.sendFeedback("§6你已被 " + username + " 授予 OP Lv" + level, "yellow");
                } else {
                    OpList.addOp(parts[1], level);
                }
                broadcastSystemMessage("[服务器] " + parts[1] + " 已被授予 OP Lv" + level, "gray");
            }

            // /ops — 查看 OP 列表
            case "ops" -> {
                if (OpList.names().isEmpty()) {
                    sendFeedback("ops.json 为空 (开发模式: 全员 Lv5)", "gray");
                    return;
                }
                StringBuilder sb = new StringBuilder("OP 列表:");
                for (String n : OpList.names()) {
                    sb.append("\n §e").append(n).append(" §7- Lv").append(OpList.level(n));
                }
                sendFeedback(sb.toString(), "gray");
            }

            // /ban <玩家> [原因] | /unban <玩家> — Lv4
            case "ban" -> {
                if (parts.length < 2) { sendFeedback("用法: /ban <玩家> [原因]", "red"); return; }
                String reason = parts.length >= 3 ? String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length)) : "违反服务器规则";
                NetworkHandler target = player(parts[1]);
                if (target != null) {
                    BanList.ban(target.uuid, target.username, reason, -1);
                    target.sendFeedback("§c你已被封禁: " + reason, "red");
                    if (target.ctx != null) target.ctx.close();
                } else {
                    BanList.ban(null, parts[1], reason, -1);
                }
                broadcastSystemMessage("[服务器] " + parts[1] + " 已被封禁: " + reason, "red");
            }
            case "unban", "pardon" -> {
                if (parts.length < 2) { sendFeedback("用法: /unban <玩家>", "red"); return; }
                if (BanList.unban(parts[1])) {
                    sendFeedback("已解封 " + parts[1], "green");
                } else {
                    sendFeedback("未找到封禁记录: " + parts[1], "red");
                }
            }

            // /mute <玩家> <分钟> /unmute — Lv3
            case "mute" -> {
                if (parts.length < 3) { sendFeedback("用法: /mute <玩家> <分钟>", "red"); return; }
                NetworkHandler target = player(parts[1]);
                if (target == null) { sendFeedback("玩家不在线", "red"); return; }
                long minutes;
                try { minutes = Long.parseLong(parts[2]); } catch (NumberFormatException e) {
                    sendFeedback("无效分钟数", "red"); return;
                }
                MUTED_UNTIL.put(target.uuid, System.currentTimeMillis() + minutes * 60000L);
                target.sendFeedback("§c你已被禁言 " + minutes + " 分钟", "red");
                sendFeedback("已禁言 " + target.username + " " + minutes + " 分钟", "green");
            }
            case "unmute" -> {
                if (parts.length < 2) { sendFeedback("用法: /unmute <玩家>", "red"); return; }
                NetworkHandler target = player(parts[1]);
                if (target == null) { sendFeedback("玩家不在线", "red"); return; }
                MUTED_UNTIL.remove(target.uuid);
                target.sendFeedback("§a你已被解除禁言", "green");
                sendFeedback("已解除 " + target.username + " 的禁言", "green");
            }

            // /tempkick <玩家> — Lv1 临时踢出(可重进)
            case "tempkick" -> {
                if (parts.length < 2) { sendFeedback("用法: /tempkick <玩家>", "red"); return; }
                NetworkHandler target = player(parts[1]);
                if (target == null) { sendFeedback("玩家不在线", "red"); return; }
                if (target.ctx != null) target.ctx.close();
                broadcastSystemMessage("[服务器] " + target.username + " 被临时踢出", "gray");
            }

            // /playerinfo <玩家> — Lv3 查看玩家数据
            case "playerinfo" -> {
                if (parts.length < 2) { sendFeedback("用法: /playerinfo <玩家>", "red"); return; }
                NetworkHandler target = player(parts[1]);
                if (target == null) { sendFeedback("玩家不在线", "red"); return; }
                String[] modes = {"生存", "创造", "冒险", "旁观"};
                sendFeedback("=== " + target.username + " ===\n"
                        + "坐标: " + String.format("%.1f, %.1f, %.1f", target.x, target.y, target.z)
                        + " (" + target.currentDim.key + ")\n"
                        + "生命: " + target.health + "/20  食物: " + target.data.food + "/20\n"
                        + "经验: Lv" + target.data.xpLevel + " (" + target.data.xpTotal + ")\n"
                        + "模式: " + modes[target.gameMode] + "  OP: Lv" + target.opLevel() + "\n"
                        + "延迟: " + (System.currentTimeMillis() - target.lastPingTime) + "ms", "gray");
            }

            // /adminmenu — Lv4 专属管理员菜单
            case "adminmenu", "admin" -> {
                sendFeedback("§6===== 管理员菜单 =====\n"
                        + "§e/ban <玩家> [原因] §7- 封禁\n"
                        + "§e/unban <玩家> §7- 解封\n"
                        + "§e/kick <玩家> §7- 踢出\n"
                        + "§e/tp <源> <目的> §7- 队伍传送\n"
                        + "§e/mute <玩家> <分钟> §7- 禁言\n"
                        + "§e/gamemode <模式> [目标] §7- 切换模式\n"
                        + "§e/playerinfo <玩家> §7- 查看数据\n"
                        + "§e/time|weather|difficulty §7- 世界控制", "yellow");
            }

            // /deop <玩家>
            case "deop" -> {
                if (opLevel() < 5) { sendFeedback("只有 Lv5(总负责人) 可以修改 OP", "red"); return; }
                if (parts.length < 2) { sendFeedback("用法: /deop <玩家>", "red"); return; }
                removeOp(parts[1]);
                NetworkHandler target = player(parts[1]);
                if (target != null) target.sendFeedback("§c你的 OP 已被移除", "red");
                broadcastSystemMessage("[服务器] " + parts[1] + " 已被移除OP权限", "gray");
            }

            // /list
            case "list" -> {
                StringBuilder sb = new StringBuilder("在线玩家 (" + players.size() + "): ");
                int i = 0;
                for (NetworkHandler p : players.values()) {
                    if (i++ > 0) sb.append(", ");
                    sb.append(p.username);
                }
                sendFeedback(sb.toString(), "gray");
            }

            // /msg <玩家> <消息>  (also /tell /w)
            case "msg", "tell", "w" -> {
                if (parts.length < 3) { sendFeedback("用法: /msg <玩家> <消息>", "red"); return; }
                java.util.List<NetworkHandler> ts = resolveSelector(parts[1]);
                if (ts.isEmpty()) { sendFeedback("玩家不在线: " + parts[1], "red"); return; }
                String msg = String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length));
                for (NetworkHandler target : ts) target.sendFeedback("[私聊] " + username + ": " + msg, "gray");
                sendFeedback("[私聊] -> " + ts.size() + " 名目标: " + msg, "gray");
            }

            // /say <消息>
            case "say" -> {
                if (parts.length < 2) { sendFeedback("用法: /say <消息>", "red"); return; }
                String msg = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
                broadcastSystemMessage("[" + username + "] " + msg, "gray");
            }

            // /me <动作>
            case "me" -> {
                if (parts.length < 2) { sendFeedback("用法: /me <动作>", "red"); return; }
                String msg = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
                broadcastSystemMessage("* " + username + " " + msg, "gray");
            }

            // /help [页码]
            case "help" -> {
                int page = parts.length >= 2 ? Integer.parseInt(parts[1]) : 1;
                String[] cmds = {
                    "/give <物品> [数量]", "/clear [物品]", "/tp <x y z|玩家>", "/gamemode <模式>",
                    "/time set|add|query <值>", "/weather clear|rain|thunder", "/kill [玩家]",
                    "/heal [玩家]", "/feed [玩家]", "/summon <实体> [x y z]",
                    "/xp <数量>", "/enchant <类型> [等级]", "/effect give|clear",
                    "/seed", "/difficulty <难度>", "/setblock <x y z> <方块>",
                    "/fill <x1 y1 z1 x2 y2 z2> <方块>", "/clone <x1 y1 z1 x2 y2 z2> <x y z>",
                    "/spawnpoint [x y z]", "/home", "/back", "/gamerule <规则> [值]",
                    "/kick <玩家>", "/op <玩家>", "/deop <玩家>", "/list",
                    "/msg <玩家> <消息>", "/say <消息>", "/me <动作>",
                    "/world <overworld|nether|end>", "/fly [on|off]", "/speed <值>",
                    "/god [on|off]", "/repair", "/rename <名称>",
                    "/top", "/bottom", "/sethome [名称]", "/delhome <名称>",
                    "/tphere <玩家>", "/tpa <玩家>", "/tpaccept", "/tpdeny",
                    "/invsee <玩家>", "/enderchest", "/workbench",
                    "/hat", "/ping", "/rules", "/motd", "/afk", "/suicide"
                };
                int perPage = 10;
                int totalPages = (cmds.length + perPage - 1) / perPage;
                if (page < 1) page = 1;
                if (page > totalPages) page = totalPages;
                sendFeedback("--- 帮助 第 " + page + "/" + totalPages + " 页 ---", "gray");
                int start = (page - 1) * perPage;
                int end = Math.min(start + perPage, cmds.length);
                for (int i = start; i < end; i++) sendFeedback("  " + cmds[i], "white");
            }

            // /world <overworld|nether|end>
            case "world" -> {
                if (parts.length < 2) { sendFeedback("用法: /world overworld|nether|end", "red"); return; }
                switch (parts[1].toLowerCase()) {
                    case "overworld" -> teleportToDimension(ctx, DimensionType.OVERWORLD);
                    case "nether" -> teleportToDimension(ctx, DimensionType.THE_NETHER);
                    case "end" -> teleportToDimension(ctx, DimensionType.THE_END);
                    default -> sendFeedback("未知维度: " + parts[1], "red");
                }
            }

            // /fly [on|off]
            case "fly" -> {
                boolean fly = parts.length < 2 ? !allowFlight : parts[1].equalsIgnoreCase("on");
                allowFlight = fly;
                sendAbilitiesUpdate();
                sendFeedback("飞行已" + (fly ? "启用" : "禁用"), "gray");
            }

            // /speed <值>
            case "speed" -> {
                if (parts.length < 2) { sendFeedback("当前速度: " + flySpeed, "gray"); return; }
                try {
                    float s = Float.parseFloat(parts[1]);
                    flySpeed = s;
                    walkSpeed = s;
                    sendAbilitiesUpdate();
                    sendFeedback("速度已设为 " + s, "gray");
                } catch (NumberFormatException e) { sendFeedback("无效数字", "red"); }
            }

            // /god [on|off]
            case "god" -> {
                godMode = parts.length < 2 ? !godMode : parts[1].equalsIgnoreCase("on");
                sendFeedback("无敌模式已" + (godMode ? "启用" : "禁用"), "gray");
            }

            // /top - 传送到地表
            case "top" -> {
                int tx = (int) x, tz = (int) z;
                for (int ty = 319; ty >= -64; ty--) {
                    if (WorldManager.getBlockState(this.currentDim,tx, ty, tz) != 0) {
                        teleportPlayer(this, x, ty + 1, z);
                        sendFeedback("已传送到地表", "gray");
                        return;
                    }
                }
                sendFeedback("未找到固体方块", "red");
            }

            // /bottom - 传送到最低
            case "bottom" -> {
                teleportPlayer(this, x, -64, z);
                sendFeedback("已传送到底部", "gray");
            }

            // /suicide
            case "suicide" -> {
                health = 0;
                isDead = true;
                sendHealthUpdate();
                sendDeathScreen(username + " 自杀了");
                broadcastSystemMessage("[服务器] " + username + " 自杀了", "gray");
            }

            // /ping
            case "ping" -> {
                sendFeedback("Pong! 延迟: " + (System.currentTimeMillis() - lastPingTime) + "ms", "gray");
            }

            // /motd
            case "motd" -> {
                sendFeedback("=== " + Main.SERVER_NAME + " ===", "gold");
                sendFeedback("欢迎来到服务器!", "yellow");
                sendFeedback("输入 /help 查看可用命令", "gray");
            }

            // /rules
            case "rules" -> {
                sendFeedback("=== 服务器规则 ===", "gold");
                sendFeedback("1. 禁止恶意破坏", "gray");
                sendFeedback("2. 禁止作弊/外挂", "gray");
                sendFeedback("3. 尊重其他玩家", "gray");
                sendFeedback("4. 禁止刷屏/广告", "gray");
            }

            // /afk
            case "afk" -> {
                isAfk = !isAfk;
                broadcastSystemMessage("[服务器] " + username + (isAfk ? " 现在挂机" : " 回来了"), "gray");
            }

            // /repair
            case "repair" -> {
                int slot = 36 + heldItemSlot;
                if (data.inventoryIds[slot] == 0) { sendFeedback("手中没有物品", "red"); return; }
                sendFeedback("已修复手中物品(待实现)", "gray");
            }

            // /hat - 把手中物品戴在头上
            case "hat" -> {
                int slot = 36 + heldItemSlot;
                if (data.inventoryIds[slot] == 0) { sendFeedback("手中没有物品", "red"); return; }
                int headSlot = 5;
                int tmpId = data.inventoryIds[headSlot];
                int tmpCnt = data.inventoryCounts[headSlot];
                ItemMeta tmpMeta = playerSlotMeta(headSlot);
                ItemMeta srcMeta = playerSlotMeta(slot);
                data.inventoryIds[headSlot] = data.inventoryIds[slot];
                data.inventoryCounts[headSlot] = data.inventoryCounts[slot];
                writePlayerSlotMeta(headSlot, srcMeta);
                data.inventoryIds[slot] = tmpId;
                data.inventoryCounts[slot] = tmpCnt;
                writePlayerSlotMeta(slot, tmpMeta);
                sendInventoryUpdate();
                sendFeedback("已戴上方块", "gray");
            }

            // /enderchest - 打开末影箱
            case "enderchest", "ec" -> {
                sendFeedback("末影箱功能待实现", "gray");
            }

            // /workbench - 打开工作台
            case "workbench", "craft" -> {
                openCraftingTable();
            }

            // /invsee <目标>
            case "invsee" -> {
                if (parts.length < 2) { sendFeedback("用法: /invsee <玩家>", "red"); return; }
                java.util.List<NetworkHandler> ts = resolveSelector(parts[1]);
                if (ts.isEmpty()) { sendFeedback("玩家不在线: " + parts[1], "red"); return; }
                for (NetworkHandler target : ts) {
                    sendFeedback(target.username + " 的物品栏:", "gray");
                    for (int i = 9; i < 45; i++) {
                        if (target.data.inventoryIds[i] > 0) {
                            String name = BlockManager.itemIdToName(target.data.inventoryIds[i]);
                            sendFeedback("  [" + i + "] " + name + " x" + target.data.inventoryCounts[i], "gray");
                        }
                    }
                }
            }

            default -> sendFeedback("未知命令: /" + parts[0] + " (输入 /help 查看可用命令)", "red");
        }
    }

    private static int parseCoord(String s, int relative) throws NumberFormatException {
        if (s.equals("~")) return relative;
        if (s.startsWith("~")) return relative + Integer.parseInt(s.substring(1));
        return Integer.parseInt(s);
    }

    /** 双精度相对坐标解析, 支持 ~ 与 ~n (用于 /tp)。 */
    private static double parseRelCoord(String s, double relative) throws NumberFormatException {
        if (s.equals("~")) return relative;
        if (s.startsWith("~")) return relative + Double.parseDouble(s.substring(1));
        return Double.parseDouble(s);
    }

    /** 解析可能带 [属性=值,...] 的方块名, 返回 blockStateId (Creative P1-7)。无属性或不识别时回退默认态。 */
    private static int resolveBlockStateId(String raw) {
        String name = raw.startsWith("minecraft:") ? raw.substring(10) : raw;
        java.util.Map<String, String> props = null;
        int bracket = name.indexOf('[');
        if (bracket >= 0 && name.endsWith("]")) {
            String base = name.substring(0, bracket);
            String inner = name.substring(bracket + 1, name.length() - 1);
            props = new java.util.HashMap<>();
            for (String kv : inner.split(",")) {
                int eq = kv.indexOf('=');
                if (eq > 0) props.put(kv.substring(0, eq).trim(), kv.substring(eq + 1).trim());
            }
            name = base;
        }
        if (props != null && !props.isEmpty()) {
            int id = BlockStateHelper.getState(name, props);
            if (id != 0) return id;
        }
        return BlockStateHelper.getDefault(name);
    }

    /** 在指定位置掉落一个方块对应的物品(用于 setblock destroy 模式, 尽力而为)。 */
    private void spawnBlockDrop(int bx, int by, int bz, int stateId) {
        String full = BlockStateHelper.getName(stateId);
        if (full == null) return;
        String name = full.contains("[") ? full.substring(0, full.indexOf('[')) : full;
        int itemId = BlockManager.getItemIdByName(name);
        if (itemId > 0) {
            ItemEntity e =
                new ItemEntity(
                    EntityManager.allocateId(),
                    bx + 0.5, by + 0.5, bz + 0.5, itemId, 1);
            EntityManager.addEntity(e);
        }
    }

    private static final java.util.List<String> ALL_COMMANDS = java.util.List.of(
        "give", "clear", "tp", "gamemode", "gm", "time", "weather", "kill", "killitem", "heal", "feed",
        "summon", "xp", "enchant", "effect", "seed", "difficulty", "setblock", "fill",
        "clone", "spawnpoint", "home", "back", "gamerule", "kick", "op", "deop",
        "list", "msg", "tell", "w", "say", "me", "help", "world", "fly", "speed",
        "god", "top", "bottom", "suicide", "ping", "motd", "rules", "afk", "repair",
        "hat", "enderchest", "ec", "workbench", "craft", "invsee", "experience"
    );

    private java.util.List<String> getTabCompletions(String text) {
        java.util.List<String> result = new java.util.ArrayList<>();
        if (!text.startsWith("/")) return result;
        String[] parts = text.substring(1).split(" ", -1);
        String partial = parts[parts.length - 1];
        boolean isLastPart = text.endsWith(" ") || parts.length == 1;

        if (parts.length == 1 || (parts.length == 2 && !text.endsWith(" "))) {
            String prefix = parts[0];
            for (String cmd : ALL_COMMANDS) {
                if (cmd.startsWith(prefix)) {
                    result.add("/" + cmd);
                }
            }
            for (String pluginCmd : Server.get().getPluginManager()
                    .getCommands().keySet()) {
                if (pluginCmd.startsWith(prefix)) {
                    result.add("/" + pluginCmd);
                }
            }
            return result;
        }

        String cmd = parts[0].toLowerCase();

        // 插件命令 Tab 补全
        var pluginCommand = Server.get().getPluginManager().getCommand(cmd);
        if (pluginCommand != null) {
            try {
                String[] args = new String[parts.length - 1];
                System.arraycopy(parts, 1, args, 0, args.length);
                for (String suggestion : pluginCommand.getExecutor()
                        .onTabComplete(new com.CharunCore.server.plugin.api.Player(this), cmd, args)) {
                    if (suggestion != null && suggestion.startsWith(partial)) {
                        result.add(suggestion);
                    }
                }
            } catch (Exception ignored) {}
            return result;
        }

        int argIdx = text.endsWith(" ") ? parts.length - 1 : parts.length - 2;

        switch (cmd) {
            case "gamemode", "gm" -> {
                if (argIdx == 1) {
                    for (String s : java.util.List.of("survival", "creative", "adventure", "spectator", "0", "1", "2", "3")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "time" -> {
                if (argIdx == 1) {
                    for (String s : java.util.List.of("set", "add", "query", "day", "night", "noon", "midnight", "sunrise", "sunset")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                } else if (argIdx == 2 && parts.length >= 2 && parts[1].equals("set")) {
                    for (String s : java.util.List.of("day", "night", "noon", "midnight", "0", "6000", "12000", "18000")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "weather" -> {
                if (argIdx == 1) {
                    for (String s : java.util.List.of("clear", "rain", "thunder")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "difficulty" -> {
                if (argIdx == 1) {
                    for (String s : java.util.List.of("peaceful", "easy", "normal", "hard", "0", "1", "2", "3")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "world" -> {
                if (argIdx == 1) {
                    for (String s : java.util.List.of("overworld", "nether", "end")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "effect" -> {
                if (argIdx == 1) {
                    for (String s : java.util.List.of("give", "clear")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "summon" -> {
                if (argIdx == 1) {
                    for (String s : java.util.List.of("zombie", "skeleton", "creeper", "spider", "enderman",
                        "witch", "slime", "blaze", "ghast", "magma_cube", "phantom",
                        "pillager", "vindicator", "ravager", "lightning_bolt",
                        "cow", "pig", "sheep", "chicken", "horse", "villager")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "fly", "god" -> {
                if (argIdx == 1) {
                    for (String s : java.util.List.of("on", "off")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "gamerule" -> {
                if (argIdx == 1) {
                    for (String s : java.util.List.of("doFireTick", "doMobSpawning", "doDaylightCycle",
                        "doWeatherCycle", "keepInventory", "mobGriefing", "naturalRegeneration",
                        "commandBlockOutput", "reducedDebugInfo", "sendCommandFeedback",
                        "showDeathMessages", "spawnRadius", "doInsomnia", "doImmediateRespawn",
                        "announceAdvancements")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                } else if (argIdx == 2) {
                    for (String s : java.util.List.of("true", "false")) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "give" -> {
                if (argIdx == 1) {
                    java.util.Set<String> items = BlockManager.getAllItemNames();
                    for (String s : items) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "setblock", "fill" -> {
                if (argIdx >= 1 && argIdx <= 3) {
                    if (partial.isEmpty()) result.add("~");
                } else {
                    java.util.Set<String> blocks = BlockManager.getAllItemNames();
                    for (String s : blocks) {
                        if (s.startsWith(partial)) result.add(s);
                    }
                }
            }
            case "tp" -> {
                if (argIdx == 1) {
                    // 坐标形式: ~ 或数字
                    if (partial.isEmpty() || partial.startsWith("~")) result.add("~");
                    // 目标选择器 / 玩家名
                    if (partial.startsWith("@")) {
                        for (String s : java.util.List.of("@a", "@p", "@r", "@s", "@e")) {
                            if (s.startsWith(partial)) result.add(s);
                        }
                    }
                    for (NetworkHandler p : players.values()) {
                        if (p.username.toLowerCase().startsWith(partial.toLowerCase())) {
                            result.add(p.username);
                        }
                    }
                } else if (argIdx == 2 || argIdx == 3) {
                    if (partial.isEmpty() || partial.startsWith("~")) result.add("~");
                }
            }
            case "kill", "heal", "feed", "kick", "op", "deop", "invsee",
                 "msg", "tell", "w" -> {
                if (argIdx == 1) {
                    if (partial.startsWith("@")) {
                        for (String s : java.util.List.of("@a", "@p", "@r", "@s", "@e")) {
                            if (s.startsWith(partial)) result.add(s);
                        }
                    }
                    for (NetworkHandler p : players.values()) {
                        if (p.username.toLowerCase().startsWith(partial.toLowerCase())) {
                            result.add(p.username);
                        }
                    }
                }
            }
            default -> {
                if (argIdx == 0) {
                    for (String cmd2 : ALL_COMMANDS) {
                        if (cmd2.startsWith(partial)) result.add("/" + cmd2);
                    }
                }
            }
        }
        return result;
    }

    public static String difficultyName(int diff) {
        return switch (diff) { case 0 -> "peaceful"; case 1 -> "easy"; case 2 -> "normal"; default -> "hard"; };
    }
//    private void handleCommand(String cmd) {
//        String[] args = cmd.split(" ");
//        if (args[0].equals("time") && args.length >= 3 && args[1].equals("set")) {
//            try {
//                long time = Long.parseLong(args[2]);
//                com.CharunCore.server.Main.dayTime = time; // 确保 Main 中 dayTime 是 public static
//                //broadcastSystemMessage("时间已设置为 " + time, "gray");
//            } catch (Exception e) {}
//        } else if (args[0].equals("gamemode") && args.length >= 2) {
//            try {
//                int mode = Integer.parseInt(args[1]);
//                final int finalMode = mode;
//                sendPacket(ctx, 0x26, pb -> { // 0x26 Game Event (3 = Change Gamemode)
//                    pb.writeByte(3);
//                    pb.writeFloat((float) mode);
//                });
////                for (NetworkHandler h : players.values()) {
////                    if (h.ctx == null) continue;
////                    h.sendPacket(h.ctx, 0x44, pb -> {
////                        pb.writeByte(0x04); // UPDATE_GAME_MODE action
////                        pb.writeVarInt(1);
////                        pb.writeUUID(this.uuid);
////                        pb.writeVarInt(finalMode);
////                    });
////                }
//                //broadcastSystemMessage("游戏模式已更改", "gray");
//            } catch (Exception e) {}
//        } else if (args[0].equals("tp") && args.length >= 4) {
//            try {
//                double tx = Double.parseDouble(args[1]);
//                double ty = Double.parseDouble(args[2]);
//                double tz = Double.parseDouble(args[3]);
//                sendPacket(ctx, 0x46, pb -> {
//                    pb.writeVarInt(1);
//                    pb.writeDouble(tx); pb.writeDouble(ty); pb.writeDouble(tz);
//                    pb.writeDouble(0); pb.writeDouble(0); pb.writeDouble(0);
//                    pb.writeFloat(0); pb.writeFloat(0);
//                    pb.writeInt(0);
//                });
//            } catch (Exception e) {}
//        }
//    }

    /** Parse "day"|"night"|"noon"|"midnight" or a raw tick number. Returns -1 on error. */
    public static long parseTimeValue(String s) {
        return switch (s.toLowerCase()) {
            case "day"      -> 1000L;
            case "noon"     -> 6000L;
            case "night"    -> 13000L;
            case "midnight" -> 18000L;
            default -> {
                try { yield Long.parseLong(s); }
                catch (NumberFormatException e) { yield -1L; }
            }
        };
    }

    /** Parse "survival"|"creative"|"adventure"|"spectator" or 0-3. Returns -1 on error. */
    public static int parseGameMode(String s) {
        return switch (s.toLowerCase()) {
            case "0", "survival"   -> 0;
            case "1", "creative"   -> 1;
            case "2", "adventure"  -> 2;
            case "3", "spectator"  -> 3;
            default -> -1;
        };
    }

    /** 把自己传送到绝对坐标 (同维度)。 */
    public void teleportTo(double tx, double ty, double tz) {
        teleportPlayer(this, tx, ty, tz);
        this.fallDistance = 0.0f;
    }

    /** Teleport a player to absolute coordinates. */
    public static void teleportPlayer(NetworkHandler target, double tx, double ty, double tz) {
        var teleportEvent = EVENTS.fire(new PlayerTeleportEvent(
                target, target.x, target.y, target.z, tx, ty, tz));
        if (teleportEvent.isCancelled()) return;
        final double fx = teleportEvent.getToX();
        final double fy = teleportEvent.getToY();
        final double fz = teleportEvent.getToZ();
        target.x = fx; target.y = fy; target.z = fz;
        int teleportId = ++target.teleportIdCounter;
        target.sendPacket(target.ctx, 0x46, pb -> {
            pb.writeVarInt(teleportId);
            pb.writeDouble(fx); pb.writeDouble(fy); pb.writeDouble(fz);
            pb.writeDouble(0); pb.writeDouble(0); pb.writeDouble(0);
            pb.writeFloat(target.yaw); pb.writeFloat(target.pitch);
            pb.writeInt(0);
        });
    }

    private int teleportIdCounter = 0;

    // ============================================================
    // 矿车骑乘 / 生成辅助
    // ============================================================
    private static boolean isMinecartItem(String name) {
        return "minecart".equals(name) || "chest_minecart".equals(name)
            || "furnace_minecart".equals(name) || "tnt_minecart".equals(name)
            || "hopper_minecart".equals(name) || "command_block_minecart".equals(name);
    }

    /** 玩家右键轨道时，寻找用于生成矿车的轨道方块坐标；无则返回 null。 */
    private int[] findRailForMinecart(int[] pos, int face) {
        int[][] cands = { pos, faceOffset(pos, face) };
        for (int[] c : cands) {
            if (c == null) continue;
            int st = WorldManager.getBlockState(this.currentDim, c[0], c[1], c[2]);
            String n = BlockStateHelper.getName(st);
            if ("rail".equals(n) || "powered_rail".equals(n) || "activator_rail".equals(n) || "detector_rail".equals(n)) {
                return c;
            }
        }
        return null;
    }

    /** 按玩家朝向初始化矿车行进方向(选与朝向点积最大的轨道端口)。 */
    private void initCartDir(MinecartEntity cart, float yaw) {
        int rx = (int) Math.floor(cart.x), ry = (int) Math.floor(cart.y), rz = (int) Math.floor(cart.z);
        int st = WorldManager.getBlockState(cart.dim, rx, ry, rz);
        String shape = BlockStateHelper.getProp(st, "shape");
        int[][] ports = portsForShape(shape);
        double fdx = -Math.sin(Math.toRadians(yaw));
        double fdz = Math.cos(Math.toRadians(yaw));
        double best = Double.NEGATIVE_INFINITY;
        int[] bestP = ports[0];
        for (int[] p : ports) {
            double dot = fdx * p[0] + fdz * p[1];
            if (dot > best) { best = dot; bestP = p; }
        }
        cart.dirX = bestP[0]; cart.dirZ = bestP[1];
        cart.yaw = (float) Math.toDegrees(Math.atan2(-cart.dirX, cart.dirZ));
    }

    private static int[][] portsForShape(String shape) {
        switch (shape) {
            case "north_south":            return new int[][]{{0, -1}, {0, 1}};
            case "east_west":              return new int[][]{{1, 0}, {-1, 0}};
            case "ascending_north":        return new int[][]{{0, -1}, {0, 1}};
            case "ascending_south":        return new int[][]{{0, -1}, {0, 1}};
            case "ascending_east":         return new int[][]{{1, 0}, {-1, 0}};
            case "ascending_west":         return new int[][]{{1, 0}, {-1, 0}};
            case "south_east":             return new int[][]{{0, 1}, {1, 0}};
            case "south_west":             return new int[][]{{0, 1}, {-1, 0}};
            case "north_west":             return new int[][]{{0, -1}, {-1, 0}};
            case "north_east":             return new int[][]{{0, -1}, {1, 0}};
            default:                       return new int[][]{{0, -1}, {0, 1}};
        }
    }

    /** 玩家骑上矿车：发送 set_passengers(0x69) 给所有玩家(含自己)。 */
    private void tryMount(MinecartEntity cart) {
        this.riddenEntity = cart;
        cart.passengerEid = this.eid;
        cart.speed = 0.0;
        cart.throttle = 0;
        initCartDir(cart, this.yaw);
        broadcastSetPassengers(cart.id, new int[]{ this.eid });
    }

    /** 玩家下车：发送空乘客列表并将玩家传送到矿车上方。 */
    private void dismount() {
        if (!(this.riddenEntity instanceof MinecartEntity cart)) return;
        this.riddenEntity = null;
        cart.passengerEid = -1;
        cart.throttle = 0;
        cart.speed = 0.0;
        broadcastSetPassengers(cart.id, new int[0]);
        teleportPlayer(this, cart.x, cart.y + 1.0, cart.z);
    }

    /** 发送 set_passengers(0x69)：vehicleId + 乘客 eid 列表。 */
    private void broadcastSetPassengers(int vehicleId, int[] passengers) {
        for (NetworkHandler h : players.values()) {
            if (h.ctx == null) continue;
            h.sendPacket(h.ctx, 0x69, pb -> {
                pb.writeVarInt(vehicleId);
                pb.writeVarInt(passengers.length);
                for (int p : passengers) pb.writeVarInt(p);
            });
        }
    }

    public void sendAbilitiesUpdate() {
        int flags = 0;
        if (godMode) flags |= 0x01;
        if (allowFlight) flags |= 0x04;
        if (gameMode == 1) flags |= 0x08;
        if (flying && allowFlight) flags |= 0x02;
        final int f = flags;
        sendPacket(ctx, 0x3e, pb -> {
            pb.writeByte((byte) f);
            pb.writeFloat(flySpeed);
            pb.writeFloat(walkSpeed);
        });
    }

    /** Send a feedback message to this player only. */
    public void sendDeathScreen(String deathMessage) {
        this.isDead = true;
        this.lastDeathX = this.x;
        this.lastDeathY = this.y;
        this.lastDeathZ = this.z;
        var deathEvent = EVENTS.fire(new PlayerDeathEvent(
                this, deathMessage, "true".equals(WorldManager.getGameRule("keepInventory"))));
        deathMessage = deathEvent.getDeathMessage();
        deathKeepInventory = deathEvent.isKeepInventory();
        dropInventoryOnDeath();

        org.cloudburstmc.nbt.NbtMap comp = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", deathMessage)
                .build();
        final int myEid = this.eid;
        sendPacket(ctx, 0x42, pb -> { pb.writeVarInt(myEid); pb.writeAnonymousNbt(comp); });
        broadcastSystemMessage(deathMessage, "white");

        // 其他人可见的死亡特效: 实体状态 3(死亡倒地动画) + 死亡音效
        for (NetworkHandler h : players.values()) {
            if (h == this || h.ctx == null || !h.ctx.channel().isActive()) continue;
            if (h.currentDim != this.currentDim) continue;
            h.sendPacket(h.ctx, 0x22, pb -> {
                pb.writeInt(myEid);
                pb.writeByte(3);
            });
            h.sendSoundAt("minecraft:entity.player.death", this.x, this.y + 1.0, this.z, 1.0f, 1.0f);
        }
        // Bug60: 死亡动画后移除玩家实体模型(原版尸体不滞留到重生)
        despawnPlayerEntityForTrackers();
        com.CharunCore.server.world.entity.EntityManager.removeTrackingEverywhere(this.eid);
    }

    /** 死亡掉落: 全部物品散落地面, 经验掉落 min(level*7, 100)。 */
    private void dropInventoryOnDeath() {
        if (deathDropsDone) return;
        deathDropsDone = true;
        if (gameMode == 1 || gameMode == 3) return;
        // keepInventory=true: 玩家保留物品与经验(原版行为); 死亡事件可覆盖
        if (deathKeepInventory) return;

        java.util.Random rng = new java.util.Random();
        for (int i = 0; i < 46; i++) {
            int itemId = data.inventoryIds[i];
            int count = data.inventoryCounts[i];
            if (itemId <= 0 || count <= 0) continue;
            ItemMeta m = playerSlotMeta(i);
            data.inventoryIds[i] = 0;
            data.inventoryCounts[i] = 0;
            writePlayerSlotMeta(i, null);

            ItemEntity drop =
                new ItemEntity(
                    EntityManager.allocateId(),
                    this.x, this.y + 0.8, this.z, itemId, count);
            drop.dim = this.currentDim;
            drop.vx = (rng.nextDouble() - 0.5) * 0.3;
            drop.vy = 0.2;
            drop.vz = (rng.nextDouble() - 0.5) * 0.3;
            drop.pickupDelay = 40;
            if (!m.isEmpty()) {
                drop.itemDamage = m.damage();
                drop.itemEnchants = m.enchants().isEmpty() ? null : new java.util.HashMap<>(m.enchants());
                drop.itemPotion = m.potion();
                drop.itemCustomName = m.customName();
                drop.trimMaterial = m.trimMaterial();
                drop.trimPattern = m.trimPattern();
            }
            EntityManager.addEntity(drop);
        }
        sendInventoryUpdate();

        // 原版: 死亡掉落总经验的 7% (int), 无硬上限
        int xpDrop = (int) ((long) data.xpTotal * 7 / 100);
        data.xpLevel = 0;
        data.xpTotal = 0;
        data.xpProgress = 0.0f;
        sendExperienceUpdate();
        if (xpDrop > 0) {
            EntityManager.spawnExperienceOrbs(this.x, this.y + 0.5, this.z, xpDrop, this.currentDim);
        }
    }

    public void sendExplosionEffect(double cx, double cy, double cz, float power) {
        double dx = this.x - cx, dy = this.y - cy, dz = this.z - cz;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        final double kx, ky, kz;
        double radius = power * 2.0;
        if (dist > 0.0001 && dist < radius) {
            double f = (1.0 - dist / radius) * 0.7;
            kx = dx / dist * f; ky = dy / dist * f; kz = dz / dist * f;
        } else { kx = 0; ky = 0; kz = 0; }
        final boolean hasKnockback = (kx != 0 || ky != 0 || kz != 0);
        sendPacket(ctx, 0x24, pb -> {
            pb.writeDouble(cx); pb.writeDouble(cy); pb.writeDouble(cz);
            pb.writeFloat(power);
            pb.writeInt(0);
            pb.writeBoolean(hasKnockback);
            if (hasKnockback) { pb.writeDouble(kx); pb.writeDouble(ky); pb.writeDouble(kz); }
            pb.writeVarInt(22);
            pb.writeVarInt(0);
            pb.writeString("minecraft:entity.generic.explode");
            pb.writeBoolean(false);
            pb.writeVarInt(0);
        });
    }

    public void sendSoundAt(String soundName, double sx, double sy, double sz, float volume, float pitch) {
        sendPacket(ctx, 0x73, pb -> {
            pb.writeVarInt(0);
            pb.writeString(soundName);
            pb.writeBoolean(false);
            pb.writeVarInt(0);
            pb.writeInt((int) (sx * 8.0));
            pb.writeInt((int) (sy * 8.0));
            pb.writeInt((int) (sz * 8.0));
            pb.writeFloat(volume);
            pb.writeFloat(pitch);
            pb.writeLong(java.util.concurrent.ThreadLocalRandom.current().nextLong());
        });
    }

    /** 停止指定名称的声音 (0x75 stop_sound)。flags=2 表示仅按声音名停止(不含 source)。
     *  用于唱片机取出唱片时停掉正在播放的曲目 (Bug22)。 */
    public void sendStopSound(String soundName) {
        sendPacket(ctx, 0x75, pb -> {
            pb.writeByte((byte) 2);   // flags: 0x02 = has sound name
            pb.writeString(soundName);
        });
    }

    /** 标题 (0x70 标题文本 + 0x6E 副标题 + 0x71 停留时间)。 */
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (title != null && !title.isEmpty()) {
            org.cloudburstmc.nbt.NbtMap comp = org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", title).build();
            sendPacket(ctx, 0x70, pb -> pb.writeAnonymousNbt(comp));
        }
        if (subtitle != null && !subtitle.isEmpty()) {
            org.cloudburstmc.nbt.NbtMap sub = org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", subtitle).build();
            sendPacket(ctx, 0x6E, pb -> pb.writeAnonymousNbt(sub));
        }
        sendPacket(ctx, 0x71, pb -> {
            pb.writeVarInt(Math.max(0, fadeIn));
            pb.writeVarInt(Math.max(0, stay));
            pb.writeVarInt(Math.max(0, fadeOut));
        });
    }

    /** 动作栏 (0x55 set_action_bar_text)。 */
    public void sendActionBar(String message) {
        org.cloudburstmc.nbt.NbtMap comp = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", message).build();
        sendPacket(ctx, 0x55, pb -> pb.writeAnonymousNbt(comp));
    }

    private static final java.util.Map<String, Integer> PARTICLE_IDS = new java.util.HashMap<>();
    static {
        try (java.io.Reader r = new java.io.InputStreamReader(
                new java.io.FileInputStream("json/1.21.11/particles.json"),
                java.nio.charset.StandardCharsets.UTF_8)) {
            com.google.gson.JsonArray arr = com.google.gson.JsonParser.parseReader(r).getAsJsonArray();
            for (com.google.gson.JsonElement e : arr) {
                com.google.gson.JsonObject o = e.getAsJsonObject();
                PARTICLE_IDS.put(o.get("name").getAsString(), o.get("id").getAsInt());
            }
        } catch (Exception ignored) {}
    }

    /** 粒子 (0x2E level_particles)。无附加数据的粒子类型适用。 */
    public void sendParticle(String particle, double x, double y, double z, int count,
                             double offsetX, double offsetY, double offsetZ, double speed) {
        Integer id = PARTICLE_IDS.get(particle);
        if (id == null) return;
        sendPacket(ctx, 0x2E, pb -> {
            pb.writeBoolean(false);   // long distance
            pb.writeDouble(x); pb.writeDouble(y); pb.writeDouble(z);
            pb.writeFloat((float) offsetX);
            pb.writeFloat((float) offsetY);
            pb.writeFloat((float) offsetZ);
            pb.writeFloat((float) speed);
            pb.writeInt(Math.max(0, count));
            pb.writeVarInt(id);
        });
    }

    /** 插件用: 服务端侧直接切换该玩家游戏模式并同步。 */
    public void setGameModeInternal(int mode) {
        if (mode < 0 || mode > 3 || mode == this.gameMode) return;
        int oldMode = this.gameMode;
        this.gameMode = mode;
        allowFlight = (mode == 1 || mode == 3);
        sendPacket(ctx, 0x26, pb -> { pb.writeByte(3); pb.writeFloat(mode); });
        for (NetworkHandler h : players.values()) {
            if (h.ctx == null) continue;
            h.sendPacket(h.ctx, 0x44, pb -> {
                pb.writeByte(0x04);
                pb.writeVarInt(1);
                pb.writeUUID(this.uuid);
                pb.writeVarInt(mode);
            });
        }
        syncEntityVisibilityToOthers(oldMode);
        sendAbilitiesUpdate();
    }

    public void sendFeedback(String text, String color) {
        org.cloudburstmc.nbt.NbtMap comp = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", text)
                .putString("color", color)
                .build();
        sendPacket(ctx, 0x77, pb -> { pb.writeAnonymousNbt(comp); pb.writeBoolean(false); });
    }

    public void sendInventoryUpdate() {
        sendPacket(ctx, 0x12, pb -> {
            pb.writeVarInt(0);
            pb.writeVarInt(0);
            pb.writeVarInt(46);
            for (int i = 0; i < 46; i++) {
                writePlayerSlot(pb, i);
            }
            writeCarriedSlot(pb);
        });
        // BUG2: 背包变化后, 检查是否有新配方达到解锁条件(拿到对应物品)
        checkRecipeUnlocks();
    }

    private void sendCarriedItem() {
        sendPacket(ctx, 0x5e, pb -> {
            writeCarriedSlot(pb);
        });
    }

    // ── 带组件(附魔/药水)的物品栈写出 ──
    /** 药水类型标记 -> minecraft:potion 注册表 holder id (1.21.11 Potions 注册顺序)。 */
    private static final java.util.Map<String, Integer> POTION_TYPE_IDS = new java.util.HashMap<>();
    private static final java.util.Map<Integer, String> POTION_ID_TO_NAME = new java.util.HashMap<>();
    static {
        String[] potionNames = {"water","mundane","thick","awkward",
            "night_vision","long_night_vision","invisibility","long_invisibility",
            "leaping","long_leaping","strong_leaping","fire_resistance","long_fire_resistance",
            "swiftness","long_swiftness","strong_swiftness",
            "slowness","long_slowness","strong_slowness",
            "turtle_master","long_turtle_master","strong_turtle_master",
            "water_breathing","long_water_breathing","healing","strong_healing",
            "harming","strong_harming","poison","long_poison","strong_poison",
            "regeneration","long_regeneration","strong_regeneration",
            "strength","long_strength","strong_strength","weakness","long_weakness",
            "luck","slow_falling","long_slow_falling","wind_charged","weaving","oozing","infested"};
        for (int i = 0; i < potionNames.length; i++) {
            POTION_TYPE_IDS.put(potionNames[i], i);
            POTION_ID_TO_NAME.put(i, potionNames[i]);
        }
    }

    /** 药水效果名 -> 客户端 mob_effect 注册表 id (与 effectProtocolId 一致)。 */
    private static final java.util.Map<String, Integer> EFFECT_PROTOCOL_IDS = new java.util.HashMap<>();
    static {
        // Bug52: mob_effect registry ids are 0-based (speed=0, effects.json); was 1-based -> all effects off by one
        int[] ids = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32};
        String[] names = {"speed","slowness","haste","mining_fatigue","strength","instant_health","instant_damage",
            "jump_boost","nausea","regeneration","resistance","fire_resistance","water_breathing","invisibility",
            "blindness","night_vision","hunger","weakness","poison","wither","health_boost","absorption",
            "saturation","glow","levitation","luck","bad_luck","slow_falling","conduit_power","dolphins_grace",
            "bad_omen","hero_of_the_village","darkness"};
        for (int i = 0; i < ids.length; i++) EFFECT_PROTOCOL_IDS.put(names[i], ids[i]);
    }

    private int[] parsePotion(String p) {
        if (p == null) return new int[]{0, 0, 0};
        String[] a = p.split("\\|");
        if (a.length < 3) return new int[]{0, 0, 0};
        int eid = effectProtocolId(a[0]);
        if (eid < 0) return new int[]{0, 0, 0};
        try { return new int[]{eid, Integer.parseInt(a[1]), Integer.parseInt(a[2])}; }
        catch (NumberFormatException e) { return new int[]{0, 0, 0}; }
    }

    /** 解析药水字符串: 返回 [特效id, 等级, 持续刻, 药水类型id(-1=无)]。
     *  支持两种格式: "效果|等级|持续刻"(自定义效果) 与 "water|awkward|..."(基础药水类型标记)。 */
    private int[] parsePotionEx(String p) {
        if (p == null) return new int[]{0, 0, 0, -1};
        Integer typeId = POTION_TYPE_IDS.get(p.trim());
        if (typeId != null) return new int[]{0, 0, 0, typeId};
        int[] e = parsePotion(p);
        return new int[]{e[0], e[1], e[2], -1};
    }

    private void writePlayerSlot(PacketBuffer pb, int slot) {
        int id = data.inventoryIds[slot], count = data.inventoryCounts[slot];
        boolean book = "enchanted_book".equals(BlockManager.itemIdToName(id));
        int[] pot = parsePotionEx(data.inventoryPotion[slot]);
        pb.writeStackWithComponents(id, count, data.inventoryEnchants[slot], book, pot[0], pot[1], pot[2],
            data.inventoryCustomName[slot], data.inventoryDamage[slot],
            data.inventoryTrimMaterial[slot], data.inventoryTrimPattern[slot], pot[3]);
    }

    private void writeCarriedSlot(PacketBuffer pb) {
        boolean book = "enchanted_book".equals(BlockManager.itemIdToName(carriedItemId));
        int[] pot = parsePotionEx(carriedPotionType);
        pb.writeStackWithComponents(carriedItemId, carriedItemCount, carriedEnchants, book, pot[0], pot[1], pot[2],
            carriedCustomName, carriedDamage, carriedTrimMaterial, carriedTrimPattern, pot[3]);
    }

    /** 带组件写出箱子/末影箱槽位 (Bug4/33: 附魔/药水/自定义名/耐久随物品同步到客户端)。 */
    private void writeChestSlot(PacketBuffer pb, ContainerStore.ChestData cd, int s) {
        int id = cd.slots[2 * s], count = cd.slots[2 * s + 1];
        if (id <= 0 || count <= 0) { pb.writeSlot(0, 0); return; }
        writeStackWithMeta(pb, id, count, contMeta(cd.meta, s));
    }

    /** Bug4/33: 按物品组件写一个完整槽位(count+id+components)。 */
    private void writeStackWithMeta(PacketBuffer pb, int id, int count, ItemMeta m) {
        boolean book = "enchanted_book".equals(BlockManager.itemIdToName(id));
        ItemMeta mm = m == null ? ItemMeta.EMPTY : m;
        int[] pot = parsePotionEx(mm.potion());
        pb.writeStackWithComponents(id, count,
                mm.enchants().isEmpty() ? null : mm.enchants(), book,
                pot[0], pot[1], pot[2],
                mm.customName(), mm.damage(),
                mm.trimMaterial(), mm.trimPattern(), pot[3]);
    }

    /**
     * 解析客户端 set_creative_slot(0x37) 的物品组件 (DELIMITED patch: 组件带 dataLen)。
     * 将附魔(stored_enchantments/enchantments)/改名/耐久/药水类型/纹饰写入对应槽位,
     * 修复"创造模式拿附魔书 -> 铁砧无法附魔/附魔书只显示灰字"。
     */
    private void parseCreativeSlot(int slot, int count, PacketBuffer in) {
        if (slot < 0 || slot >= 46) return;
        if (count <= 0) {
            data.inventoryIds[slot] = 0;
            data.inventoryCounts[slot] = 0;
            data.inventoryEnchants[slot] = new java.util.HashMap<>();
            data.inventoryPotion[slot] = null;
            data.inventoryCustomName[slot] = null;
            data.inventoryDamage[slot] = 0;
            data.inventoryTrimMaterial[slot] = -1;
            data.inventoryTrimPattern[slot] = -1;
            return;
        }
        int itemId = in.readVarInt();
        int addedComponents = in.readVarInt();
        int removedComponents = in.readVarInt();
        int damage = 0;
        String customName = null;
        java.util.Map<Integer, Integer> enchants = new java.util.HashMap<>();
        int trimMaterial = -1, trimPattern = -1;
        int potionTypeId = -1;
        java.util.List<int[]> effects = new java.util.ArrayList<>();
        for (int c = 0; c < addedComponents; c++) {
            int type = in.readVarInt();
            int dataLen = in.readVarInt();
            int start = in.getBuffer().readerIndex();
            Pair parsed = parseClientComponent(type, dataLen, in);
            if (parsed == null) { in.getBuffer().readerIndex(start + dataLen); continue; }
            damage = parsed.damage; customName = parsed.customName;
            if (parsed.enchants != null) enchants = parsed.enchants;
            trimMaterial = parsed.trimMaterial; trimPattern = parsed.trimPattern;
            potionTypeId = parsed.potionTypeId;
            effects = parsed.effects;
            in.getBuffer().readerIndex(start + dataLen);
        }
        for (int c = 0; c < removedComponents; c++) in.readVarInt();
        data.inventoryIds[slot] = itemId;
        data.inventoryCounts[slot] = count;
        data.inventoryEnchants[slot] = enchants;
        data.inventoryDamage[slot] = damage;
        data.inventoryCustomName[slot] = customName;
        data.inventoryTrimMaterial[slot] = trimMaterial;
        data.inventoryTrimPattern[slot] = trimPattern;
        if (potionTypeId >= 0) {
            data.inventoryPotion[slot] = POTION_ID_TO_NAME.getOrDefault(potionTypeId, "water");
        } else if (!effects.isEmpty()) {
            int[] eff = effects.get(0); // [effectId, amplifier, duration]
            String en = effectNameById(eff[0]);
            data.inventoryPotion[slot] = en != null ? (en + "|" + (eff[1] + 1) + "|" + eff[2]) : null;
        } else {
            data.inventoryPotion[slot] = null;
        }
    }

    private static final class Pair {
        int damage = 0;
        String customName = null;
        java.util.Map<Integer, Integer> enchants = null;
        int trimMaterial = -1, trimPattern = -1;
        int potionTypeId = -1;
        java.util.List<int[]> effects = new java.util.ArrayList<>();
    }

    /** 客户端组件值解析(在 dataLen 限定的 payload 内)。仅提取本项目支持的组件, 其余忽略。 */
    private Pair parseClientComponent(int type, int dataLen, PacketBuffer in) {
        Pair p = new Pair();
        switch (type) {
            case 3: { // minecraft:damage
                p.damage = in.readVarInt();
                return p;
            }
            case 6: { // minecraft:custom_name (Text Component as anonymous NBT)
                p.customName = readClientNbtText(in);
                return p;
            }
            case 13: case 41: { // enchantments / stored_enchantments
                int size = in.readVarInt();
                java.util.Map<Integer, Integer> m = new java.util.HashMap<>();
                for (int i = 0; i < size; i++) {
                    int eid = in.readVarInt();
                    int lvl = in.readVarInt();
                    if (lvl > 0) m.put(eid, lvl);
                }
                p.enchants = m;
                return p;
            }
            case 49: { // minecraft:potion_contents
                boolean hasPotion = in.getBuffer().readBoolean();
                if (hasPotion) {
                    p.potionTypeId = in.readVarInt();
                }
                boolean hasColor = in.getBuffer().readBoolean();
                if (hasColor) in.readInt();
                int effCount = in.readVarInt();
                for (int i = 0; i < effCount; i++) {
                    int eid = in.readVarInt();
                    int amp = in.readVarInt();
                    int dur = in.readVarInt();
                    in.getBuffer().readBoolean(); in.getBuffer().readBoolean(); in.getBuffer().readBoolean();
                    boolean hasHidden = in.getBuffer().readBoolean();
                    if (!hasHidden) p.effects.add(new int[]{eid, amp, dur});
                }
                boolean hasName = in.getBuffer().readBoolean();
                if (hasName) in.readString();
                return p;
            }
            case 54: { // minecraft:trim — 原版 ByteBufCodecs.holder(REFERENCE) 编码为 VarInt(registryId+1)
                int m = in.readVarInt();
                int pt = in.readVarInt();
                p.trimMaterial = m > 0 ? m - 1 : -1;
                p.trimPattern = pt > 0 ? pt - 1 : -1;
                return p;
            }
            default:
                return null;
        }
    }

    /** 读取客户端 Text Component 匿名 NBT({text:"..."} 或字符串), 返回纯文本。 */
    private String readClientNbtText(PacketBuffer in) {
        try {
            int typeByte = in.getBuffer().readByte() & 0xFF;
            if (typeByte == 0) return null;
            if (typeByte == 8) { // TAG_STRING (纯文本组件)
                int len = in.getBuffer().readShort();
                byte[] bytes = new byte[len];
                in.getBuffer().readBytes(bytes);
                return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
            }
            if (typeByte == 10) { // TAG_COMPOUND
                int start = in.getBuffer().readerIndex();
                org.cloudburstmc.nbt.NbtMap tag = readAnonymousNbtFrom(in);
                if (tag != null) {
                    if (tag.containsKey("text")) {
                        Object t = tag.get("text");
                        if (t instanceof String s) return s;
                    }
                    if (tag.containsKey("extra")) return null;
                    // 纯文本组件也可能是 {text:...} 或 { "": ...}
                }
                // 若读取失败, 跳过剩余 dataLen 逻辑交给调用方 (readerIndex 未推进)
                return null;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private org.cloudburstmc.nbt.NbtMap readAnonymousNbtFrom(PacketBuffer in) {
        try {
            int len = 8 * 1024 * 1024;
            byte[] buf = new byte[len];
            int read = in.getBuffer().readableBytes();
            if (read <= 0) return null;
            byte[] raw = new byte[Math.min(read, len)];
            in.getBuffer().readBytes(raw);
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(raw);
            try (org.cloudburstmc.nbt.NBTInputStream nbtIn = new org.cloudburstmc.nbt.NBTInputStream(
                    new java.io.DataInputStream(bais))) {
                Object tag = nbtIn.readTag();
                if (tag instanceof org.cloudburstmc.nbt.NbtMap m) return m;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /** 药水效果注册表 id -> 效果名(用于 client 带入的 custom_effects 反显工具提示)。 */
    private static String effectNameById(int id) {
        if (id < 0) return null;
        for (java.util.Map.Entry<String, Integer> e : EFFECT_PROTOCOL_IDS.entrySet()) {
            if (e.getValue() == id) return e.getKey();
        }
        return null;
    }

    public void sendSlotUpdate(int windowId, int slot) {
        int itemId;
        int itemCount;
        if (windowId == 0) {
            if (slot < 0 || slot >= 46) return;
            itemId = data.inventoryIds[slot];
            itemCount = data.inventoryCounts[slot];
        } else if (openCraftingGrids.containsKey(windowId)) {
            if (slot == 0) {
                String[] grid = openCraftingGrids.get(windowId);
                RecipeRegistry.Recipe result =
                    CraftingSystem.matchRecipe(grid);
                if (result != null) {
                    int ri = result.resultItemId;
                    sendSlotUpdateRaw(windowId, slot, ri, result.resultCount);
                } else {
                    sendSlotUpdateRaw(windowId, slot, 0, 0);
                }
                return;
            } else if (slot >= 1 && slot <= 9) {
                String[] grid = openCraftingGrids.get(windowId);
                String itemName = grid[slot - 1];
                int iid = itemName != null ? BlockManager.getItemIdByName(itemName) : 0;
                int gc = craftingCounts(windowId)[slot - 1];
                if (itemName != null && gc <= 0) gc = 1;
                sendSlotUpdateRaw(windowId, slot, iid, itemName != null ? gc : 0);
                return;
            } else {
                int playerSlot = craftingToPlayerSlot(slot);
                if (playerSlot < 0 || playerSlot >= 46) return;
                itemId = data.inventoryIds[playerSlot];
                itemCount = data.inventoryCounts[playerSlot];
            }
        } else if (openChests.containsKey(windowId) || openEnderChests.containsKey(windowId)) {
            // Bug51: 双箱窗口按 0-26 本箱 / 27-53 邻箱路由
            ContainerStore.ChestData cdata = chestDataForSlot(windowId, slot);
            if (cdata != null) {
                int ls = isPartnerChestSlot(windowId, slot) ? slot - 27 : slot;
                itemId = cdata.slots[ls * 2];
                itemCount = cdata.slots[ls * 2 + 1];
                // 共享箱子槽位实时同步给其他观察者
                ContainerStore.Pos cpos = isPartnerChestSlot(windowId, slot)
                    ? openChestPartners.get(windowId) : openChests.get(windowId);
                if (cpos != null) broadcastChestSlot(cpos, ls, itemId, itemCount);
            } else {
                int playerSlot = chestToPlayerSlot(windowId, slot);
                if (playerSlot < 0 || playerSlot >= 46) return;
                itemId = data.inventoryIds[playerSlot];
                itemCount = data.inventoryCounts[playerSlot];
            }
        } else if (openFurnaces.containsKey(windowId)) {
            int[] furnaceContents = getFurnaceContents(windowId);
            if (slot >= 0 && slot < 3) {
                itemId = furnaceContents[slot * 2];
                itemCount = furnaceContents[slot * 2 + 1];
            } else {
                int playerSlot = furnaceToPlayerSlot(slot);
                if (playerSlot < 0 || playerSlot >= 46) return;
                itemId = data.inventoryIds[playerSlot];
                itemCount = data.inventoryCounts[playerSlot];
            }
        } else if (openHoppers.containsKey(windowId)) {
            int[] hopperContents = getHopperContents(windowId);
            if (slot >= 0 && slot < 5) {
                itemId = hopperContents[slot * 2];
                itemCount = hopperContents[slot * 2 + 1];
            } else {
                int playerSlot = hopperToPlayerSlot(slot);
                if (playerSlot < 0 || playerSlot >= 46) return;
                itemId = data.inventoryIds[playerSlot];
                itemCount = data.inventoryCounts[playerSlot];
            }
        } else if (openDispensers.containsKey(windowId)) {
            int[] dc = getDispenserContents(windowId);
            if (slot >= 0 && slot < 9) {
                itemId = dc[slot * 2];
                itemCount = dc[slot * 2 + 1];
            } else {
                int playerSlot = dispenserToPlayerSlot(slot);
                if (playerSlot < 0 || playerSlot >= 46) return;
                itemId = data.inventoryIds[playerSlot];
                itemCount = data.inventoryCounts[playerSlot];
            }
        } else if (openPluginMenus.containsKey(windowId)) {
            var pluginInv = openPluginMenus.get(windowId);
            if (slot >= 0 && slot < pluginInv.getSize()) {
                itemId = pluginInv.slotId(slot);
                itemCount = pluginInv.slotCount(slot);
            } else {
                int playerSlot = pluginMenuPlayerSlot(pluginInv, slot);
                if (playerSlot < 0 || playerSlot >= 46) return;
                itemId = data.inventoryIds[playerSlot];
                itemCount = data.inventoryCounts[playerSlot];
            }
        } else {
            return;
        }
        sendSlotUpdateRaw(windowId, slot, itemId, itemCount);
    }

    private void sendSlotUpdateRaw(int windowId, int slot, int itemId, int count) {
        // Bug4/33: 容器槽位也带组件下发(附魔/药水/改名物品在箱/炉等界面不再显示为白板)。
        // 预览槽(合成结果等)无存储组件, readSlotMeta 返回 EMPTY 走原路径。
        final boolean empty = itemId <= 0 || count <= 0;
        var pluginInv0 = openPluginMenus.get(windowId);
        if (pluginInv0 != null && slot >= 0 && slot < pluginInv0.getSize()) {
            final com.CharunCore.server.plugin.api.ItemMeta pluginMeta =
                    empty ? null : pluginInv0.slotMeta(slot);
            final int fId = itemId, fCount = count;
            sendPacket(ctx, 0x14, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(0);
                pb.writeShort(slot);
                writePluginItemMeta(pb, fId, fCount, pluginMeta);
            });
            return;
        }
        final ItemMeta m = empty ? ItemMeta.EMPTY : readSlotMeta(windowId, slot);
        final boolean hasMeta = !m.isEmpty();
        sendPacket(ctx, 0x14, pb -> {
            pb.writeVarInt(windowId);
            pb.writeVarInt(0);
            pb.writeShort(slot);
            if (hasMeta) {
                writeStackWithMeta(pb, itemId, count, m);
            } else if (windowId == 0 && slot >= 1 && slot < 46 && !empty) {
                // Bug35: 窗口0槽0是2x2合成结果槽(非玩家背包格), 曾走 writePlayerSlot
                // 读恒空的 data.inventoryIds[0] -> 背包合成栏永远无输出预览。
                // 插件扩展组件(lore/unbreakable/glint)槽走完整组件路径。
                if (hasExtSlotMeta(slot)) {
                    writePluginItemMeta(pb, itemId, count, extSlotMeta(slot));
                } else {
                    writePlayerSlot(pb, slot);
                }
            } else {
                pb.writeSlot(itemId, count);
            }
        });
    }

    /** 向同一 Pos 容器的其他观察者广播共享槽位(0-26)更新(多人同箱实时同步)。 */
    private void broadcastChestSlot(ContainerStore.Pos pos, int slot, int itemId, int itemCount) {
        java.util.Set<NetworkHandler> obs = chestObservers.get(pos);
        if (obs == null) return;
        for (NetworkHandler h : obs) {
            if (h == this || h.ctx == null) continue;
            int hw = -1;
            for (java.util.Map.Entry<Integer, ContainerStore.Pos> e : h.openChests.entrySet()) {
                if (e.getValue().equals(pos)) { hw = e.getKey(); break; }
            }
            if (hw < 0) continue;
            final int fhw = hw;
            h.sendPacket(h.ctx, 0x14, pb -> {
                pb.writeVarInt(fhw);
                pb.writeVarInt(0);
                pb.writeShort(slot);
                writeChestSlot(pb, ContainerStore.chest(pos), slot);
            });
        }
    }

    /** #28 漏斗/箱子实时动效: 任意容器内容变化时, 通知正在查看该容器的所有玩家重发内容。
     *  由 ContainerStore.tickHopper 等在物品自动传输后调用, 否则打开箱子/漏斗页面的玩家
     *  看不到物品"一个一个"被漏斗吸走/推入(原版会实时刷新)。 */
    public static void broadcastContainerUpdate(ContainerStore.Pos pos) {
        for (NetworkHandler h : players.values()) {
            if (h.ctx == null || !h.ctx.channel().isActive()) continue;
            int hw = -1;
            boolean isHopper = false;
            for (java.util.Map.Entry<Integer, ContainerStore.Pos> e : h.openChests.entrySet()) {
                if (e.getValue().equals(pos)) { hw = e.getKey(); break; }
            }
            if (hw < 0) {
                for (java.util.Map.Entry<Integer, ContainerStore.Pos> e : h.openHoppers.entrySet()) {
                    if (e.getValue().equals(pos)) { hw = e.getKey(); isHopper = true; break; }
                }
            }
            if (hw < 0) continue;
            final int fhw = hw;
            if (isHopper) {
                ContainerStore.HopperData hd = h.getHopperData(fhw);
                int[] contents = h.getHopperContents(fhw);
                h.sendPacket(h.ctx, 0x12, pb -> {
                    pb.writeVarInt(fhw); pb.writeVarInt(0); pb.writeVarInt(5 + 36);
                    for (int i = 0; i < 5; i++) {
                        if (hd != null && contents[i * 2] > 0) h.writeStackWithMeta(pb, contents[i * 2], contents[i * 2 + 1], contMeta(hd.meta, i));
                        else pb.writeSlot(contents[i * 2], contents[i * 2 + 1]);
                    }
                    for (int ps = 9; ps <= 44; ps++) h.writePlayerSlot(pb, ps);
                    h.writeCarriedSlot(pb);
                });
            } else {
                ContainerStore.ChestData cd = ContainerStore.chest(pos);
                int[] contents = h.getChestContents(fhw);
                h.sendPacket(h.ctx, 0x12, pb -> {
                    pb.writeVarInt(fhw); pb.writeVarInt(0); pb.writeVarInt(27 + 36);
                    for (int i = 0; i < 27; i++) h.writeChestSlot(pb, cd, i);
                    for (int ps = 9; ps <= 44; ps++) h.writePlayerSlot(pb, ps);
                    h.writeCarriedSlot(pb);
                });
            }
        }
    }

    /**
     * 工作台窗口共 46 格: 0 结果 / 1-9 网格 / 10-36 主背包 / 37-45 快捷栏。
     * 玩家区必须一并下发, 否则客户端界面里背包是空的, 无法把物品拖进网格。
     */
    // ── Bug 9: recipe book (配方书) ───────────────────────────────────────────
    // 1.21.11 配方系统由 "显示" 驱动: recipe_book_add(0x48) 携带 RecipeDisplay,
    // 客户端据此填充左侧配方书。displayId 为服务端自分配序号, 客户端点击时回传。

    // SlotDisplay 类型 (SLOT_DISPLAY 注册表 id): 0=empty 1=any_fuel 2=item 3=item_stack 4=tag ...
    // 写一个 ingredient 单元: 接受 int itemId (项目) 或 String cell 名.
    private void writeIngredientSlotDisplay(PacketBuffer pb, int itemId) {
        if (itemId <= 0) { pb.writeVarInt(0); return; }
        pb.writeVarInt(2);
        pb.writeVarInt(itemId);
    }
    private void writeIngredientSlotDisplay(PacketBuffer pb, String cell) {
        if (cell == null) { pb.writeVarInt(0); return; }
        String itemName = CraftingSystem.representativeItem(cell);
        if (itemName == null) { pb.writeVarInt(0); return; }
        int itemId = BlockManager.getItemIdByName(itemName);
        if (itemId <= 0) { pb.writeVarInt(0); return; }
        pb.writeVarInt(2);      // type = item
        pb.writeVarInt(itemId); // 物品注册表 id
    }

    // 写一个 result 单元: item_stack(3) = 完整 Slot 格式（与 writeSlot 一致）。
    private void writeResultSlotDisplay(PacketBuffer pb, String itemName, int count) {
        int itemId = BlockManager.getItemIdByName(itemName);
        if (itemId <= 0 || count <= 0) { pb.writeVarInt(0); return; } // empty
        pb.writeVarInt(3);      // type = item_stack
        pb.writeSlot(itemId, count); // 完整 Slot: count + id + components
    }
    private void writeResultSlotDisplay(PacketBuffer pb, int itemId, int count) {
        if (itemId <= 0 || count <= 0) { pb.writeVarInt(0); return; } // empty
        pb.writeVarInt(3);      // type = item_stack
        pb.writeSlot(itemId, count); // 完整 Slot: count + id + components
    }

    private void sendRecipeBook(ChannelHandlerContext ctx) {
        // 0x48 recipe_book_add — 登录时只重置配方书并推送站台配方(熔炉/高炉/烟熏炉/切石机);
        // 合成配方由 checkRecipeUnlocks 渐进解锁(replace=false 增量) —— 曾在登录全量推送全部
        // 合成配方 + 渐进解锁同 id 重发, 双轨状态导致条目重复/错乱。
        // displayId 分配: 0..C-1 = 去重后的合成配方(C = BOOK_DISPLAY_TO_RECIPE.size()),
        // C.. = 站台配方, 与 checkRecipeUnlocks 的增量 id 永不冲突。
        var smeltAll = com.CharunCore.server.world.SmeltingSystem.allSmeltingEntries().stream()
                .filter(se -> BlockManager.getItemIdByName(se.input) > 0 && BlockManager.getItemIdByName(se.result) > 0).toList();
        var blastAll = com.CharunCore.server.world.SmeltingSystem.allBlastingEntries().stream()
                .filter(se -> BlockManager.getItemIdByName(se.input) > 0 && BlockManager.getItemIdByName(se.result) > 0).toList();
        var smokeAll = com.CharunCore.server.world.SmeltingSystem.allSmokingEntries().stream()
                .filter(se -> BlockManager.getItemIdByName(se.input) > 0 && BlockManager.getItemIdByName(se.result) > 0).toList();
        var stonecutterEntries = com.CharunCore.server.world.menu.MenuUtil.stonecutterEntries().stream()
                .filter(se -> BlockManager.getItemIdByName(se.input) > 0 && BlockManager.getItemIdByName(se.result) > 0).toList();
        int craftDisplayCount = BOOK_DISPLAY_TO_RECIPE.size();
        // total 只算本包实际写入的站台条目! 合成配方不在登录包写(由 checkRecipeUnlocks 增量发),
        // craftDisplayCount 仅用于给站台条目的 displayId 让出 0..C-1 的 id 空间。
        // 曾把 craftDisplayCount 误算进 total -> 声明了数百个从未写入的幽灵条目,
        // 客户端读完站台条目后越界 -> "Failed to decode recipe_book_add" 断线。
        int total = smeltAll.size() + blastAll.size() + smokeAll.size() + stonecutterEntries.size();
        sendPacket(ctx, 0x48, pb -> {
            pb.writeVarInt(total);
            int idx = craftDisplayCount;

            // #8/#13 熔炉/高炉/烟熏炉/切石机左侧配方。
            // 原版 FurnaceRecipeDisplay: ingredient + fuel(any_fuel) + result + craftingStation + duration + experience。
            // category 必须按站台归类(4-6 熔炉 / 7-8 高炉 / 9 烟熏炉 / 10 切石机), 否则客户端左侧面板过滤后为空。
            // Bug5 修复: 无效条目必须在写入任何字节之前 continue —— 曾先写 id/type 再 continue,
            // 一条无效原料即令整包字节错位, 客户端解析失败 -> 左侧面板全空。
            for (var se : smeltAll) {
                int inId = BlockManager.getItemIdByName(se.input);
                int outId = BlockManager.getItemIdByName(se.result);
                if (inId <= 0 || outId <= 0) continue;
                pb.writeVarInt(idx++);
                pb.writeVarInt(2); // FurnaceRecipeDisplay
                pb.writeVarInt(2); pb.writeVarInt(inId);       // ingredient (item)
                pb.writeVarInt(1);                              // fuel (any_fuel)
                pb.writeVarInt(3); pb.writeSlot(outId, 1);      // result (item_stack)
                pb.writeVarInt(2); pb.writeVarInt(BlockManager.getItemIdByName("furnace")); // station
                pb.writeVarInt(200);                            // duration (原版烧制 200 tick)
                pb.writeFloat(se.xp);                           // experience
                pb.writeVarInt(0);                              // group (null)
                pb.writeVarInt(isFoodItem(se.input) ? 4 : (isBlockItem(se.result) ? 5 : 6)); // furnace_food/blocks/misc
                pb.writeBoolean(false);                         // craftingRequirements
                pb.writeByte(0);                                // flags
            }
            for (var se : blastAll) {
                int inId = BlockManager.getItemIdByName(se.input);
                int outId = BlockManager.getItemIdByName(se.result);
                if (inId <= 0 || outId <= 0) continue;
                pb.writeVarInt(idx++);
                pb.writeVarInt(2); // FurnaceRecipeDisplay
                pb.writeVarInt(2); pb.writeVarInt(inId);
                pb.writeVarInt(1);
                pb.writeVarInt(3); pb.writeSlot(outId, 1);
                pb.writeVarInt(2); pb.writeVarInt(BlockManager.getItemIdByName("blast_furnace")); // station
                pb.writeVarInt(100);                            // duration (高炉减半)
                pb.writeFloat(se.xp);
                pb.writeVarInt(0);
                pb.writeVarInt(isBlockItem(se.result) ? 7 : 8); // blast_furnace_blocks/misc
                pb.writeBoolean(false);
                pb.writeByte(0);
            }
            for (var se : smokeAll) {
                int inId = BlockManager.getItemIdByName(se.input);
                int outId = BlockManager.getItemIdByName(se.result);
                if (inId <= 0 || outId <= 0) continue;
                pb.writeVarInt(idx++);
                pb.writeVarInt(2); // FurnaceRecipeDisplay
                pb.writeVarInt(2); pb.writeVarInt(inId);
                pb.writeVarInt(1);
                pb.writeVarInt(3); pb.writeSlot(outId, 1);
                pb.writeVarInt(2); pb.writeVarInt(BlockManager.getItemIdByName("smoker")); // station
                pb.writeVarInt(100);                            // duration (烟熏炉减半)
                pb.writeFloat(se.xp);
                pb.writeVarInt(0);
                pb.writeVarInt(9);                              // smoker_food
                pb.writeBoolean(false);
                pb.writeByte(0);
            }

            // #13 切石机左侧样式列表: StonecutterRecipeDisplay(3) = input + result + station。
            // category 必须为 10(stonecutter), 曾写 0(建筑) -> 客户端切石机面板过滤后为空, 无法选样式。
            for (var se2 : stonecutterEntries) {
                int inId2 = BlockManager.getItemIdByName(se2.input);
                int outId2 = BlockManager.getItemIdByName(se2.result);
                if (inId2 <= 0 || outId2 <= 0) continue;
                pb.writeVarInt(idx++);
                pb.writeVarInt(3); // StonecutterRecipeDisplay
                pb.writeVarInt(2); pb.writeVarInt(inId2);       // input (item)
                pb.writeVarInt(3); pb.writeSlot(outId2, 1);     // result (item_stack)
                pb.writeVarInt(2); pb.writeVarInt(BlockManager.getItemIdByName("stonecutter")); // station
                pb.writeVarInt(0);                              // group (null)
                pb.writeVarInt(10);                             // category = stonecutter
                pb.writeBoolean(false);                         // craftingRequirements
                pb.writeByte(0);                                // flags
            }
            pb.writeBoolean(true); // replace
        });

        // 0x4A recipe_book_settings — 开启合成配方书面板
        sendRecipeBookSettings(ctx);
    }

    /** 配方分类辅助: 输入是否为食物(用于熔炉 category furnace_food)。 */
    private static boolean isFoodItem(String name) {
        if (name == null) return false;
        return name.equals("beef") || name.equals("porkchop") || name.equals("chicken") || name.equals("cod")
            || name.equals("salmon") || name.equals("mutton") || name.equals("rabbit") || name.equals("potato")
            || name.equals("chorus_fruit") || name.equals("kelp");
    }

    /** 配方分类辅助: 产物是否为方块(用于高炉 category blast_furnace_blocks)。 */
    private static boolean isBlockItem(String name) {
        if (name == null) return false;
        return name.endsWith("_ingot") || name.endsWith("_block") || name.equals("glass")
            || name.equals("terracotta") || name.endsWith("_terracotta")
            || name.equals("smooth_stone") || name.endsWith("_bricks");
    }

    /** BUG2: 仅开启配方书面板(不推送任何配方); 配方由 checkRecipeUnlocks 渐进解锁。 */
    private void sendRecipeBookSettings(ChannelHandlerContext ctx) {
        sendPacket(ctx, 0x4A, pb -> {
            pb.writeBoolean(true); pb.writeBoolean(false);  // crafting: open / filtering
            pb.writeBoolean(false); pb.writeBoolean(false); // furnace
            pb.writeBoolean(false); pb.writeBoolean(false); // blast
            pb.writeBoolean(false); pb.writeBoolean(false); // smoker
        });
    }

    /** #13/#35: declare_recipes(0x83) — 原版 RecipePropertySet(7 个槽位过滤集) + 切石机配方列表。
     *  1.21.2+ 客户端切石机界面的样式列表数据源就是本包的 stoneCutterRecipes 段,
     *  从未发送导致切石机中间样式区永远为空(只能靠旧按钮逻辑切出台阶)。 */
    private void sendDeclareRecipes(ChannelHandlerContext ctx) {
        var smeltAll = com.CharunCore.server.world.SmeltingSystem.allSmeltingEntries();
        var blastAll = com.CharunCore.server.world.SmeltingSystem.allBlastingEntries();
        var smokeAll = com.CharunCore.server.world.SmeltingSystem.allSmokingEntries();
        var stonecutterEntries = com.CharunCore.server.world.menu.MenuUtil.stonecutterEntries();
        sendPacket(ctx, 0x83, pb -> {
            // recipes: map<ResourceKey<RecipePropertySet>, RecipePropertySet>
            // 值编码 = VarInt 数量 + VarInt 物品注册表 id 列表(Item.STREAM_CODEC list)
            java.util.LinkedHashMap<String, java.util.List<Integer>> sets = new java.util.LinkedHashMap<>();
            java.util.List<Integer> smithBase = new java.util.ArrayList<>();
            for (String s : new String[]{"diamond_pickaxe","diamond_axe","diamond_shovel","diamond_hoe","diamond_sword",
                    "diamond_helmet","diamond_chestplate","diamond_leggings","diamond_boots",
                    "leather_helmet","leather_chestplate","leather_leggings","leather_boots",
                    "chainmail_helmet","chainmail_chestplate","chainmail_leggings","chainmail_boots",
                    "iron_helmet","iron_chestplate","iron_leggings","iron_boots",
                    "golden_helmet","golden_chestplate","golden_leggings","golden_boots",
                    "turtle_helmet","netherite_helmet","netherite_chestplate","netherite_leggings","netherite_boots"}) {
                int iid = BlockManager.getItemIdByName(s);
                if (iid > 0) smithBase.add(iid);
            }
            java.util.List<Integer> smithAdd = new java.util.ArrayList<>();
            for (String s : new String[]{"netherite_ingot","amethyst_shard","copper_ingot","diamond","emerald",
                    "gold_ingot","iron_ingot","lapis_lazuli","nether_quartz","redstone","resin_brick"}) {
                int iid = BlockManager.getItemIdByName(s);
                if (iid > 0) smithAdd.add(iid);
            }
            java.util.List<Integer> smithTpl = new java.util.ArrayList<>();
            for (String s : new String[]{"netherite_upgrade_smithing_template",
                    "sentry_armor_trim_smithing_template","vex_armor_trim_smithing_template","wild_armor_trim_smithing_template",
                    "coast_armor_trim_smithing_template","dune_armor_trim_smithing_template","wayfinder_armor_trim_smithing_template",
                    "shaper_armor_trim_smithing_template","raiser_armor_trim_smithing_template","host_armor_trim_smithing_template",
                    "ward_armor_trim_smithing_template","silence_armor_trim_smithing_template","tide_armor_trim_smithing_template",
                    "snout_armor_trim_smithing_template","rib_armor_trim_smithing_template","eye_armor_trim_smithing_template",
                    "spire_armor_trim_smithing_template","bolt_armor_trim_smithing_template","flow_armor_trim_smithing_template"}) {
                int iid = BlockManager.getItemIdByName(s);
                if (iid > 0) smithTpl.add(iid);
            }
            sets.put("minecraft:smithing_base", smithBase);
            sets.put("minecraft:smithing_template", smithTpl);
            sets.put("minecraft:smithing_addition", smithAdd);
            sets.put("minecraft:furnace_input", itemIdsOf(smeltAll.stream().map(se -> se.input).toList()));
            sets.put("minecraft:blast_furnace_input", itemIdsOf(blastAll.stream().map(se -> se.input).toList()));
            sets.put("minecraft:smoker_input", itemIdsOf(smokeAll.stream().map(se -> se.input).toList()));
            sets.put("minecraft:campfire_input", itemIdsOf(smokeAll.stream().map(se -> se.input).toList()));
            pb.writeVarInt(sets.size());
            for (var e : sets.entrySet()) {
                pb.writeString(e.getKey());
                java.util.List<Integer> ids = e.getValue().stream().distinct().collect(java.util.stream.Collectors.toList());
                pb.writeVarInt(ids.size());
                for (int iid : ids) pb.writeVarInt(iid);
            }
            // stoneCutterRecipes: array of { input: IDSet(VarInt count+1 形式), result: SlotDisplay }
            var filtered = stonecutterEntries.stream()
                    .filter(se -> BlockManager.getItemIdByName(se.input) > 0 && BlockManager.getItemIdByName(se.result) > 0).toList();
            pb.writeVarInt(filtered.size());
            for (var se : filtered) {
                int inId = BlockManager.getItemIdByName(se.input);
                int outId = BlockManager.getItemIdByName(se.result);
                pb.writeVarInt(2);          // IDSet: count+1 = 2 -> 单元素
                pb.writeVarInt(inId);
                pb.writeVarInt(3);          // SlotDisplay item_stack
                pb.writeSlot(outId, 1);
            }
        });
    }

    private static java.util.List<Integer> itemIdsOf(java.util.List<String> names) {
        java.util.List<Integer> out = new java.util.ArrayList<>();
        for (String n : names) {
            int iid = BlockManager.getItemIdByName(n);
            if (iid > 0) out.add(iid);
        }
        return out;
    }

    /**
     * BUG2: 渐进式解锁 — 仅当背包里出现某配方的任一原料或产物时才解锁该配方,
     * 模拟原版"拿到对应物品才解锁配方书"的行为。增量推送新解锁的配方(replace=false)。
     */
    private void checkRecipeUnlocks() {
        if (ctx == null) return;
        // 收集背包内所有物品名(含主背包与快捷栏 9~44)
        java.util.Set<String> have = new java.util.HashSet<>();
        for (int s = 9; s <= 44; s++) {
            int id = data.inventoryIds[s];
            if (id <= 0 || data.inventoryCounts[s] <= 0) continue;
            String n = BlockManager.itemIdToName(id);
            if (n != null) have.add(n);
        }
        java.util.List<Integer> newly = new java.util.ArrayList<>();
        for (int d = 0; d < BOOK_DISPLAY_TO_RECIPE.size(); d++) {
            if (unlockedRecipes.contains(d)) continue;
            RecipeRegistry.Recipe r = RECIPE_BOOK_ENTRIES.get(BOOK_DISPLAY_TO_RECIPE.get(d));
            boolean ok = false;
            // 产物或任一原料出现在背包即解锁
            String resultName = BlockManager.itemIdToName(r.resultItemId);
            if (resultName != null && have.contains(resultName)) ok = true;
            if (!ok && r.grid != null) {
                for (int cell : r.grid) {
                    if (cell <= 0) continue;
                    String rep = BlockManager.itemIdToName(cell);
                    if (rep != null && have.contains(rep)) { ok = true; break; }
                }
            }
            if (ok) { unlockedRecipes.add(d); newly.add(d); }
        }
        if (newly.isEmpty()) return;
        // 增量推送(只发新解锁的, replace=false 以免覆盖已解锁列表)
        final java.util.List<Integer> toSend = newly;
        sendPacket(ctx, 0x48, pb -> {
            pb.writeVarInt(toSend.size());
            for (int d : toSend) {
                RecipeRegistry.Recipe r = RECIPE_BOOK_ENTRIES.get(BOOK_DISPLAY_TO_RECIPE.get(d));
                pb.writeVarInt(d); // displayId (与登录站台配方的 id 空间不冲突)
                if (r.shapeless) {
                    pb.writeVarInt(0); // shapeless
                    int ingCount = 0;
                    for (int g = 0; g < 9; g++) if (r.grid[g] != 0) ingCount++;
                    pb.writeVarInt(ingCount);
                    for (int g = 0; g < 9; g++)
                        if (r.grid[g] != 0) writeIngredientSlotDisplay(pb, r.grid[g]);
                } else {
                    pb.writeVarInt(1); // shaped
                    int minR = 3, maxR = -1, minC = 3, maxC = -1;
                    for (int rr = 0; rr < 3; rr++) for (int cc = 0; cc < 3; cc++) {
                        if (r.grid[rr * 3 + cc] != 0) {
                            minR = Math.min(minR, rr); maxR = Math.max(maxR, rr);
                            minC = Math.min(minC, cc); maxC = Math.max(maxC, cc);
                        }
                    }
                    if (maxR < 0) {
                        pb.writeVarInt(1); pb.writeVarInt(1);
                        pb.writeVarInt(1); writeIngredientSlotDisplay(pb, null);
                    } else {
                        int w = maxC - minC + 1, h = maxR - minR + 1;
                        pb.writeVarInt(w); pb.writeVarInt(h);
                        pb.writeVarInt(w * h);
                        for (int rr = minR; rr <= maxR; rr++)
                            for (int cc = minC; cc <= maxC; cc++)
                                writeIngredientSlotDisplay(pb, r.grid[rr * 3 + cc]);
                    }
                }
                writeResultSlotDisplay(pb, r.resultItemId, r.resultCount);
                writeResultSlotDisplay(pb, "crafting_table", 1);
                // group = ByteBufCodecs.OPTIONAL_VAR_INT(移位式: 0=无分组, present=值+1)。
                // 曾写 varint(-1) -> 客户端解出 OptionalInt.of(-2) -> 所有解锁配方共享同一分组,
                // 全部折叠进一个格子轮换显示。
                pb.writeVarInt(0);
                pb.writeVarInt(r.category);
                pb.writeBoolean(false);
                pb.writeByte(0);
            }
            pb.writeBoolean(false); // replace = false (增量)
        });
    }

    private void sendCraftingTableContent(int windowId) {
        final String[] grid = openCraftingGrids.get(windowId);
        final int[] counts = craftingCounts(windowId);
        RecipeRegistry.Recipe r =
            grid != null ? CraftingSystem.matchRecipe(grid) : null;
        final int resultId = r != null
            ? r.resultItemId : 0;
        final int resultCount = r != null ? r.resultCount : 0;
        sendPacket(ctx, 0x12, pb -> {
            pb.writeVarInt(windowId);
            pb.writeVarInt(0);
            pb.writeVarInt(46);
            pb.writeSlot(resultId, resultCount);
            for (int i = 0; i < 9; i++) {
                String n = grid != null ? grid[i] : null;
                int iid = n != null ? BlockManager.getItemIdByName(n) : 0;
                int c = counts[i] > 0 ? counts[i] : (n != null ? 1 : 0);
                pb.writeSlot(iid, c);
            }
            for (int s = 10; s <= 45; s++) {
                int ps = s - 1;
                writePlayerSlot(pb, ps);
            }
            writeCarriedSlot(pb);
        });
    }

    /** 为当前玩家打开一个 3x3 工作台界面（玩家指令与控制台指令共用）。 */
    public void openCraftingTable() {
        int windowId = nextWindowId();
        openCraftingGrids.put(windowId, new String[9]);
        openCraftingCounts.put(windowId, new int[9]);
        org.cloudburstmc.nbt.NbtMap title = org.cloudburstmc.nbt.NbtMap.builder()
            .putString("text", "Crafting Table").build();
        sendPacket(this.ctx, 0x39, pb -> {
            pb.writeVarInt(windowId);
            pb.writeVarInt(RegistryHelper.menuType("crafting"));
            pb.writeAnonymousNbt(title);
        });
        sendCraftingTableContent(windowId);
    }

    // 容器窗口的玩家区在协议上是连续的 27 格主背包 + 9 格快捷栏,
    // 对应内部 inventory 的 9-35 与 36-44, 因此两段共用同一个偏移量。
    private int craftingToPlayerSlot(int containerSlot) {
        if (containerSlot >= 10 && containerSlot <= 45) return containerSlot - 1;
        return -1;
    }

    /** Bug51: 该窗口 slot 是否属于大箱子另一半(27..53)。 */
    private boolean isPartnerChestSlot(int windowId, int slot) {
        return openChestPartners.containsKey(windowId) && slot >= 27 && slot < 54;
    }

    /** Bug51: 取窗口 slot 对应的箱子存储(双箱按 0-26 本箱 / 27-53 邻箱路由)。null = 非箱子槽。 */
    private ContainerStore.ChestData chestDataForSlot(int windowId, int slot) {
        if (openEnderChests.containsKey(windowId)) {
            return (slot >= 0 && slot < 27) ? ContainerStore.enderChest(openEnderChests.get(windowId)) : null;
        }
        ContainerStore.Pos p = openChests.get(windowId);
        if (p == null) return null;
        if (slot >= 0 && slot < 27) return ContainerStore.chest(p);
        if (isPartnerChestSlot(windowId, slot)) return ContainerStore.chest(openChestPartners.get(windowId));
        return null;
    }

    /** 槽位所属箱子(或大箱子另一半)的坐标; 末影箱/玩家背包区返回 null。 */
    private ContainerStore.Pos chestDataPosForSlot(int windowId, int slot) {
        if (openEnderChests.containsKey(windowId)) return null;
        if (slot >= 0 && slot < 27) return openChests.get(windowId);
        if (isPartnerChestSlot(windowId, slot)) return openChestPartners.get(windowId);
        return null;
    }

    private int chestToPlayerSlot(int windowId, int containerSlot) {
        if (openChestPartners.containsKey(windowId)) {
            if (containerSlot >= 54 && containerSlot <= 89) return containerSlot - 45;
            return -1;
        }
        if (containerSlot >= 27 && containerSlot <= 62) return containerSlot - 18;
        return -1;
    }

    private int furnaceToPlayerSlot(int containerSlot) {
        if (containerSlot >= 3 && containerSlot <= 38) return containerSlot + 6;
        return -1;
    }

    private int hopperToPlayerSlot(int containerSlot) {
        if (containerSlot >= 5 && containerSlot <= 40) return containerSlot + 4;
        return -1;
    }

    private int dispenserToPlayerSlot(int containerSlot) {
        if (containerSlot >= 9 && containerSlot <= 44) return containerSlot;
        return -1;
    }

    private int[] getChestContents(int windowId) {
        ContainerStore.Pos p = openChests.get(windowId);
        if (p != null) return ContainerStore.chest(p).slots;
        java.util.UUID eu = openEnderChests.get(windowId);
        if (eu != null) return ContainerStore.enderChest(eu).slots;
        return new int[54];
    }

    private int[] getFurnaceContents(int windowId) {
        ContainerStore.Pos p = openFurnaces.get(windowId);
        if (p == null) return new int[6];
        return ContainerStore.furnace(p, furnaceTypeAt(p)).slots;
    }

    private int[] getHopperContents(int windowId) {
        ContainerStore.Pos p = openHoppers.get(windowId);
        if (p == null) return new int[10];
        return ContainerStore.hopper(p).slots;
    }

    private int[] getDispenserContents(int windowId) {
        ContainerStore.Pos p = openDispensers.get(windowId);
        if (p == null) return new int[18];
        return ContainerStore.dispenser(p).slots;
    }

    private ContainerStore.DispenserData getDispenserData(int windowId) {
        ContainerStore.Pos p = openDispensers.get(windowId);
        return p == null ? null : ContainerStore.dispenser(p);
    }

    private ContainerStore.HopperData getHopperData(int windowId) {
        ContainerStore.Pos p = openHoppers.get(windowId);
        return p == null ? null : ContainerStore.hopper(p);
    }

    private int[] getSmithingContents(int windowId) {
        ContainerStore.Pos p = openSmithing.get(windowId);
        if (p == null) return new int[6];
        return ContainerStore.smithing(p).slots;
    }
    private ContainerStore.SmithingData getSmithingData(int windowId) {
        ContainerStore.Pos p = openSmithing.get(windowId);
        return p == null ? null : ContainerStore.smithing(p);
    }
    private int[] getEnchantingContents(int windowId) {
        ContainerStore.Pos p = openEnchanting.get(windowId);
        if (p == null) return new int[6];
        return ContainerStore.enchanting(p).slots;
    }
    private ContainerStore.EnchantingData getEnchantingData(int windowId) {
        ContainerStore.Pos p = openEnchanting.get(windowId);
        return p == null ? null : ContainerStore.enchanting(p);
    }
    private int[] getAnvilContents(int windowId) {
        ContainerStore.Pos p = openAnvil.get(windowId);
        if (p == null) return new int[6];
        return ContainerStore.anvil(p).slots;
    }
    private ContainerStore.AnvilData getAnvilData(int windowId) {
        ContainerStore.Pos p = openAnvil.get(windowId);
        return p == null ? null : ContainerStore.anvil(p);
    }
    private int[] getBrewingContents(int windowId) {
        ContainerStore.Pos p = openBrewing.get(windowId);
        if (p == null) return new int[10];
        return ContainerStore.brewing(p).slots;
    }

    /** #15 信标: 计算金字塔层数 0-4 (原版 BeaconBlockEntity.getLevels: 4x4 基座 + 逐层收窄, 层高 1)。 */
    private int computeBeaconLevels(DimensionType dim, int x, int y, int z) {
        java.util.Set<String> pyramid = java.util.Set.of(
            "iron_block", "gold_block", "diamond_block", "emerald_block", "netherite_block");
        int levels = 0;
        // Bug11 修复: 原版金字塔层尺寸为 3x3 / 5x5 / 7x7 / 9x9 (半宽 = 层数 l)。
        // 曾用 4/6/8/10 的偶数环 -> 只有把信标"包进" oversized 一层才判定通过。
        for (int l = 1; l <= 4; l++) {
            boolean ok = true;
            for (int dx = -l; dx <= l && ok; dx++) {
                for (int dz = -l; dz <= l && ok; dz++) {
                    int by = y - l;
                    if (by < -64) { ok = false; break; }
                    int st = WorldManager.getBlockStateCached(dim, x + dx, by, z + dz);
                    if (!pyramid.contains(BlockStateHelper.getName(st))) { ok = false; break; }
                }
            }
            if (ok) levels = l; else break;
        }
        return levels;
    }
    private ContainerStore.BrewingData getBrewingData(int windowId) {
        ContainerStore.Pos p = openBrewing.get(windowId);
        return p == null ? null : ContainerStore.brewing(p);
    }

    // ── 切石机 / 砂轮 数据访问器 ──────────────────────────────────────────
    private int[] getStonecutterContents(int windowId) {
        ContainerStore.Pos p = openStonecutters.get(windowId);
        if (p == null) return new int[4];
        return ContainerStore.stonecutter(p).slots;
    }
    private ContainerStore.StonecutterData getStonecutterData(int windowId) {
        ContainerStore.Pos p = openStonecutters.get(windowId);
        return p == null ? null : ContainerStore.stonecutter(p);
    }
    private int stonecutterToPlayerSlot(int slot) {
        int ps = 9 + (slot - 2);
        return ps > 44 ? -1 : ps;
    }
    private int[] getGrindstoneContents(int windowId) {
        ContainerStore.Pos p = openGrindstones.get(windowId);
        if (p == null) return new int[6];
        return ContainerStore.grindstone(p).slots;
    }
    private ContainerStore.GrindstoneData getGrindstoneData(int windowId) {
        ContainerStore.Pos p = openGrindstones.get(windowId);
        return p == null ? null : ContainerStore.grindstone(p);
    }
    private int grindstoneToPlayerSlot(int slot) {
        int ps = 9 + (slot - 3);
        return ps > 44 ? -1 : ps;
    }

    // 切石机: 单输入 → 全部可行切制产物(左侧列表), 选中项进结果槽 (原版 StonecutterMenu)。
    // #18 修复: 曾只取首个产物 -> 无法选择样式, 只能切台阶。现计算全部候选 + 支持 0x10 button 选择。
    private void recomputeStonecutter(int windowId) {
        ContainerStore.StonecutterData sd = getStonecutterData(windowId);
        if (sd == null) return;
        int inId = sd.slots[0], inCount = sd.slots[1];
        sd.slots[2] = 0; sd.slots[3] = 0;
        sd.candidates = new int[0];
        if (inId > 0 && inCount > 0) {
            String inName = BlockManager.itemIdToName(inId);
            int[] cands = stonecutCandidates(inName);
            sd.candidates = cands;
            if (sd.selectedIndex < 0) sd.selectedIndex = cands.length > 0 ? 0 : -1;
            if (sd.selectedIndex >= 0 && sd.selectedIndex < cands.length) {
                sd.slots[2] = cands[sd.selectedIndex];
                sd.slots[3] = 1;
            }
        } else {
            sd.selectedIndex = -1;
        }
        sd.version++;
        sendProcessorContent(windowId, sd.slots, 2);
    }

    /** 切石机: 返回输入物品的全部可行切制产物 id (候选列表, 供左侧样式列表)。 */
    private int[] stonecutCandidates(String in) {
        if (in == null) return new int[0];
        String[] cands;
        switch (in) {
            case "stone": cands = new String[]{"stone_slab","stone_stairs","stone_bricks","stone_brick_slab","stone_brick_stairs","stone_brick_wall","chiseled_stone_bricks"}; break;
            case "granite": cands = new String[]{"granite_slab","granite_stairs","granite_wall","polished_granite","polished_granite_slab","polished_granite_stairs","polished_granite_wall"}; break;
            case "diorite": cands = new String[]{"diorite_slab","diorite_stairs","diorite_wall","polished_diorite","polished_diorite_slab","polished_diorite_stairs","polished_diorite_wall"}; break;
            case "andesite": cands = new String[]{"andesite_slab","andesite_stairs","andesite_wall","polished_andesite","polished_andesite_slab","polished_andesite_stairs","polished_andesite_wall"}; break;
            case "sandstone": cands = new String[]{"sandstone_slab","sandstone_stairs","sandstone_wall","chiseled_sandstone","cut_sandstone","cut_sandstone_slab"}; break;
            case "red_sandstone": cands = new String[]{"red_sandstone_slab","red_sandstone_stairs","red_sandstone_wall","chiseled_red_sandstone","cut_red_sandstone","cut_red_sandstone_slab"}; break;
            case "quartz_block": cands = new String[]{"quartz_slab","quartz_stairs","quartz_pillar","chiseled_quartz_block","smooth_quartz"}; break;
            case "cobblestone": cands = new String[]{"cobblestone_slab","cobblestone_stairs","cobblestone_wall"}; break;
            case "stone_bricks": cands = new String[]{"stone_brick_slab","stone_brick_stairs","stone_brick_wall","chiseled_stone_bricks"}; break;
            case "bricks": cands = new String[]{"brick_slab","brick_stairs","brick_wall"}; break;
            case "nether_bricks": cands = new String[]{"nether_brick_slab","nether_brick_stairs","nether_brick_wall","chiseled_nether_bricks"}; break;
            case "red_nether_bricks": cands = new String[]{"red_nether_brick_slab","red_nether_brick_stairs","red_nether_brick_wall"}; break;
            case "blackstone": cands = new String[]{"blackstone_slab","blackstone_stairs","blackstone_wall","polished_blackstone","polished_blackstone_slab","polished_blackstone_stairs","polished_blackstone_wall","chiseled_polished_blackstone"}; break;
            case "polished_blackstone": cands = new String[]{"polished_blackstone_slab","polished_blackstone_stairs","polished_blackstone_wall"}; break;
            case "polished_blackstone_bricks": cands = new String[]{"polished_blackstone_brick_slab","polished_blackstone_brick_stairs","polished_blackstone_brick_wall","chiseled_polished_blackstone"}; break;
            case "prismarine": cands = new String[]{"prismarine_slab","prismarine_stairs","prismarine_bricks","prismarine_brick_slab","prismarine_brick_stairs","prismarine_wall"}; break;
            case "purpur_block": cands = new String[]{"purpur_slab","purpur_stairs","purpur_pillar"}; break;
            case "end_stone": cands = new String[]{"end_stone_bricks"}; break;
            case "deepslate": cands = new String[]{"deepslate_slab","deepslate_stairs","deepslate_wall","deepslate_bricks","deepslate_brick_slab","deepslate_brick_stairs","deepslate_brick_wall","chiseled_deepslate","cracked_deepslate_bricks","cracked_deepslate_tiles"}; break;
            case "cobbled_deepslate": cands = new String[]{"cobbled_deepslate_slab","cobbled_deepslate_stairs","cobbled_deepslate_wall"}; break;
            case "basalt": cands = new String[]{"polished_basalt"}; break;
            case "copper_block": cands = new String[]{"cut_copper_slab","cut_copper_stairs","cut_copper","exposed_cut_copper","weathered_cut_copper","oxidized_cut_copper"}; break;
            case "mossy_stone_bricks": cands = new String[]{"mossy_stone_brick_slab","mossy_stone_brick_stairs","mossy_stone_brick_wall"}; break;
            case "mossy_cobblestone": cands = new String[]{"mossy_cobblestone_slab","mossy_cobblestone_stairs","mossy_cobblestone_wall"}; break;
            case "tuff": cands = new String[]{"tuff_slab","tuff_stairs","tuff_bricks","tuff_brick_slab","tuff_brick_stairs","tuff_brick_wall","chiseled_tuff"}; break;
            case "calcite": cands = new String[]{"calcite_slab","calcite_stairs"}; break;
            default: cands = new String[]{in + "_slab", in + "_stairs", in + "_bricks", in + "_wall", in + "_pillar"};
        }
        java.util.ArrayList<Integer> out = new java.util.ArrayList<>();
        for (String c : cands) {
            int id = BlockManager.getItemIdByName(c);
            if (id > 0) out.add(id);
        }
        int[] r = new int[out.size()];
        for (int i = 0; i < r.length; i++) r[i] = out.get(i);
        return r;
    }

    // 砂轮: 同物品合并 (架构限制下不做除魔/修复耐久, 仅合并数量)
    private void recomputeGrindstone(int windowId) {
        ContainerStore.GrindstoneData gd = getGrindstoneData(windowId);
        if (gd == null) return;
        int aId = gd.slots[0], aCount = gd.slots[1];
        int bId = gd.slots[2], bCount = gd.slots[3];
        gd.slots[4] = 0; gd.slots[5] = 0;
        // 磨石核心功能: 去附魔 + (双输入同物时) 合并修复耐久。
        // 单输入: 直接产出同物品(去附魔/去改名)。
        // 双输入同物品: 合并数量并补耐久(此处简化: 数量相加)。
        if (aId > 0 && bId > 0 && aId == bId) {
            gd.slots[4] = aId;
            gd.slots[5] = Math.min(64, aCount + bCount);
        } else if (aId > 0 && bId <= 0) {
            gd.slots[4] = aId;
            gd.slots[5] = aCount;
        } else if (bId > 0 && aId <= 0) {
            gd.slots[4] = bId;
            gd.slots[5] = bCount;
        }
        // 产出的附魔/改名始终为空(磨石移除所有附魔与自定义名)。
        gd.version++;
        sendProcessorContent(windowId, gd.slots, 3);
    }

    // 切石产物解析: 给定输入物品名, 返回首个存在的切制产物 id (运行时查注册表, 不存在则跳过)
    private int stonecutResult(String in) {
        if (in == null) return 0;
        String[] cands;
        switch (in) {
            case "stone": cands = new String[]{"stone_slab","stone_stairs","stone_bricks","stone_brick_slab","stone_brick_stairs","stone_brick_wall","chiseled_stone_bricks"}; break;
            case "granite": cands = new String[]{"granite_slab","granite_stairs","granite_wall","polished_granite","polished_granite_slab","polished_granite_stairs","polished_granite_wall"}; break;
            case "diorite": cands = new String[]{"diorite_slab","diorite_stairs","diorite_wall","polished_diorite","polished_diorite_slab","polished_diorite_stairs","polished_diorite_wall"}; break;
            case "andesite": cands = new String[]{"andesite_slab","andesite_stairs","andesite_wall","polished_andesite","polished_andesite_slab","polished_andesite_stairs","polished_andesite_wall"}; break;
            case "sandstone": cands = new String[]{"sandstone_slab","sandstone_stairs","sandstone_wall","chiseled_sandstone","cut_sandstone","cut_sandstone_slab"}; break;
            case "red_sandstone": cands = new String[]{"red_sandstone_slab","red_sandstone_stairs","red_sandstone_wall","chiseled_red_sandstone","cut_red_sandstone","cut_red_sandstone_slab"}; break;
            case "quartz_block": cands = new String[]{"quartz_slab","quartz_stairs","quartz_pillar","chiseled_quartz_block","smooth_quartz"}; break;
            case "cobblestone": cands = new String[]{"cobblestone_slab","cobblestone_stairs","cobblestone_wall"}; break;
            case "stone_bricks": cands = new String[]{"stone_brick_slab","stone_brick_stairs","stone_brick_wall","chiseled_stone_bricks"}; break;
            case "bricks": cands = new String[]{"brick_slab","brick_stairs","brick_wall"}; break;
            case "nether_bricks": cands = new String[]{"nether_brick_slab","nether_brick_stairs","nether_brick_wall","chiseled_nether_bricks"}; break;
            case "red_nether_bricks": cands = new String[]{"red_nether_brick_slab","red_nether_brick_stairs","red_nether_brick_wall"}; break;
            case "blackstone": cands = new String[]{"blackstone_slab","blackstone_stairs","blackstone_wall","polished_blackstone","polished_blackstone_slab","polished_blackstone_stairs","polished_blackstone_wall","chiseled_polished_blackstone"}; break;
            case "polished_blackstone": cands = new String[]{"polished_blackstone_slab","polished_blackstone_stairs","polished_blackstone_wall"}; break;
            case "polished_blackstone_bricks": cands = new String[]{"polished_blackstone_brick_slab","polished_blackstone_brick_stairs","polished_blackstone_brick_wall","chiseled_polished_blackstone"}; break;
            case "prismarine": cands = new String[]{"prismarine_slab","prismarine_stairs","prismarine_bricks","prismarine_brick_slab","prismarine_brick_stairs","prismarine_wall"}; break;
            case "purpur_block": cands = new String[]{"purpur_slab","purpur_stairs","purpur_pillar"}; break;
            case "end_stone": cands = new String[]{"end_stone_bricks"}; break;
            case "deepslate": cands = new String[]{"deepslate_slab","deepslate_stairs","deepslate_wall","deepslate_bricks","deepslate_brick_slab","deepslate_brick_stairs","deepslate_brick_wall","chiseled_deepslate","cracked_deepslate_bricks","cracked_deepslate_tiles"}; break;
            case "cobbled_deepslate": cands = new String[]{"cobbled_deepslate_slab","cobbled_deepslate_stairs","cobbled_deepslate_wall"}; break;
            case "basalt": cands = new String[]{"polished_basalt"}; break;
            case "copper_block": cands = new String[]{"cut_copper_slab","cut_copper_stairs","cut_copper","exposed_cut_copper","weathered_cut_copper","oxidized_cut_copper"}; break;
            case "mossy_stone_bricks": cands = new String[]{"mossy_stone_brick_slab","mossy_stone_brick_stairs","mossy_stone_brick_wall"}; break;
            case "mossy_cobblestone": cands = new String[]{"mossy_cobblestone_slab","mossy_cobblestone_stairs","mossy_cobblestone_wall"}; break;
            case "tuff": cands = new String[]{"tuff_slab","tuff_stairs","tuff_bricks","tuff_brick_slab","tuff_brick_stairs","tuff_brick_wall","chiseled_tuff"}; break;
            case "calcite": cands = new String[]{"calcite_slab","calcite_stairs"}; break;
            default: cands = new String[]{in + "_slab", in + "_stairs", in + "_bricks", in + "_wall", in + "_pillar"};
        }
        for (String c : cands) {
            int id = BlockManager.getItemIdByName(c);
            if (id > 0) return id;
        }
        return 0;
    }

    private void dropContainerContents(int x, int y, int z) {
        ContainerStore.Pos p =
            new ContainerStore.Pos(this.currentDim, x, y, z);
        ContainerStore.ChestData cd =
            ContainerStore.removeChest(p);
        if (cd != null) {
            for (int i = 0; i < 27; i++) {
                spawnDrop(x, y, z, cd.slots[i * 2], cd.slots[i * 2 + 1]);
            }
        }
        ContainerStore.FurnaceData fd =
            ContainerStore.removeFurnace(p);
        if (fd != null) {
            for (int i = 0; i < 3; i++) {
                spawnDrop(x, y, z, fd.slots[i * 2], fd.slots[i * 2 + 1]);
            }
            if (fd.xpStore >= 1.0f) addExperience((int) fd.xpStore);
        }
        ContainerStore.HopperData hd =
            ContainerStore.removeHopper(p);
        if (hd != null) {
            for (int i = 0; i < 5; i++) {
                spawnDrop(x, y, z, hd.slots[i * 2], hd.slots[i * 2 + 1]);
            }
        }
        openChests.entrySet().removeIf(e -> e.getValue().equals(p));
        chestObservers.remove(p); // 破坏容器时清空观察者
        openFurnaces.entrySet().removeIf(e -> e.getValue().equals(p));
        openHoppers.entrySet().removeIf(e -> e.getValue().equals(p));
    }

    private void spawnDrop(int x, int y, int z, int itemId, int count) {
        if (itemId <= 0 || count <= 0) return;
        ItemEntity ie =
            new ItemEntity(
                EntityManager.allocateId(),
                x + 0.5 + (Math.random() - 0.5) * 0.4,
                y + 0.5,
                z + 0.5 + (Math.random() - 0.5) * 0.4,
                itemId, count);
        EntityManager.addEntity(ie);
    }

    private boolean consumeHeldItem(int itemId) {
        int slot = 36 + heldItemSlot;
        if (data.inventoryIds[slot] != itemId || data.inventoryCounts[slot] <= 0) return false;
        data.inventoryCounts[slot]--;
        if (data.inventoryCounts[slot] <= 0) { data.inventoryIds[slot] = 0; data.inventoryCounts[slot] = 0; }
        if (gameMode == 0) sendSlotUpdate(0, slot);
        else sendInventoryUpdate();
        return true;
    }

    private void replaceHeldWith(int oldId, int newId) {
        int slot = 36 + heldItemSlot;
        if (data.inventoryIds[slot] != oldId) return;
        if (data.inventoryCounts[slot] <= 1) {
            data.inventoryIds[slot] = newId;
            data.inventoryCounts[slot] = 1;
            sendSlotUpdate(0, slot);
        } else {
            data.inventoryCounts[slot]--;
            sendSlotUpdate(0, slot);
            giveItem(newId, 1);
        }
        sendInventoryUpdate();
    }

    /** 把手中的玻璃瓶替换成水瓶(potion + "water" 药水类型标记)。 */
    private void giveWaterBottleHeld(int bottleId) {
        int potionId = BlockManager.getItemIdByName("potion");
        if (potionId <= 0) return;
        int slot = 36 + heldItemSlot;
        if (data.inventoryIds[slot] == bottleId && data.inventoryCounts[slot] > 0) {
            data.inventoryCounts[slot]--;
            if (data.inventoryCounts[slot] <= 0) { data.inventoryIds[slot] = 0; data.inventoryCounts[slot] = 0; }
            sendSlotUpdate(0, slot);
        }
        int target = -1;
        for (int i : PICKUP_SLOT_ORDER) {
            if (data.inventoryIds[i] == potionId && "water".equals(data.inventoryPotion[i])
                    && data.inventoryCounts[i] < BlockManager.getStackSize(potionId)) { target = i; break; }
            if (data.inventoryIds[i] == 0) { target = i; break; }
        }
        if (target < 0) return;
        data.inventoryIds[target] = potionId;
        data.inventoryCounts[target] = 1;
        data.inventoryPotion[target] = "water";
        sendSlotUpdate(0, target);
        sendInventoryUpdate();
    }

    private static final java.util.Map<String, Integer> COMPOST_CHANCE = new java.util.HashMap<>();
    static {
        for (String n : new String[]{
            "sapling", "oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling",
            "acacia_sapling", "dark_oak_sapling", "mangrove_propagule", "oak_leaves", "apple",
            "short_grass", "tall_grass", "fern", "vine", "lily_pad", "sea_pickle",
            "hanging_roots", "moss_carpet", "pink_petals", "nether_sprouts", "weeping_vines",
            "twisting_vines", "glow_lichen", "small_dripleaf", "big_dripleaf"
        }) COMPOST_CHANCE.put(n, 30);
        for (String n : new String[]{
            "dried_kelp", "kelp", "leaf_litter", "mangrove_roots", "moss_block",
            "sugar_cane", "sweet_berries", "watermelon_slice", "glow_berries", "pumpkin", "carved_pumpkin",
            "sea_grass", "cactus", "sponge", "wet_sponge", "cobweb", "large_fern", "dead_bush"
        }) COMPOST_CHANCE.put(n, 50);
        for (String n : new String[]{
            "beetroot", "beetroot_seeds", "dried_kelp_block", "melon",
            "brown_mushroom", "red_mushroom", "mushroom_block", "mushroom_stem", "nether_wart",
            "wheat", "potato", "carrot", "baked_potato", "bread", "cookie", "hay_block",
            "brown_mushroom_block", "red_mushroom_block", "warped_fungus", "crimson_fungus",
            "warped_wart_block", "nether_wart_block"
        }) COMPOST_CHANCE.put(n, 65);
        for (String n : new String[]{
            "cake", "pumpkin_pie", "cooked_beetroot", "cooked_carrot", "torchflower_seeds",
            "pitcher_pod", "dandelion", "poppy",
            "blue_orchid", "allium", "azure_bluet", "red_tulip", "orange_tulip", "white_tulip",
            "pink_tulip", "oxeye_daisy", "cornflower", "lily_of_the_valley", "wither_rose",
            "sunflower", "lilac", "rose_bush", "peony", "torchflower", "pitcher_plant"
        }) COMPOST_CHANCE.put(n, 85);
    }

    private static boolean isCompostable(String itemName) {
        return COMPOST_CHANCE.containsKey(itemName);
    }

    private static int composterChance(String itemName) {
        return COMPOST_CHANCE.getOrDefault(itemName, 0);
    }

    private void persistChest(ContainerStore.Pos p) {
        ContainerStore.ChestData d =
            ContainerStore.peekChest(p);
        if (d == null) return;
        Chunk chunk = WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
        if (chunk == null) return;
        java.util.List<org.cloudburstmc.nbt.NbtMap> items = new java.util.ArrayList<>();
        for (int i = 0; i < 27; i++) {
            int id = d.slots[i * 2];
            int cnt = d.slots[i * 2 + 1];
            if (id <= 0 || cnt <= 0) continue;
            String iname = BlockManager.itemIdToName(id);
            if (iname == null) continue;
            org.cloudburstmc.nbt.NbtMapBuilder it = org.cloudburstmc.nbt.NbtMap.builder()
                .putByte("Slot", (byte) i)
                .putString("id", iname.startsWith("minecraft:") ? iname : "minecraft:" + iname)
                .putByte("Count", (byte) Math.min(127, cnt));
            // Bug4/33: 箱子物品组件(附魔/药水/自定义名/耐久)一并落盘, 否则重进丢失 NBT
            org.cloudburstmc.nbt.NbtMap comps = com.CharunCore.server.world.PlayerDataManager.buildItemComponents(
                iname, d.meta.slotDamage[i], d.meta.slotEnchants[i], d.meta.slotPotion[i], d.meta.slotCustomName[i]);
            if (!comps.isEmpty()) it.putCompound("components", comps);
            items.add(it.build());
        }
        org.cloudburstmc.nbt.NbtMap existing = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
        org.cloudburstmc.nbt.NbtMapBuilder b = org.cloudburstmc.nbt.NbtMap.builder();
        if (existing != null) {
            for (String k : existing.keySet()) {
                if (k.equals("Items") || k.equals("LootTable") || k.equals("LootTableSeed")) continue;
                b.put(k, existing.get(k));
            }
        } else {
            // 按实际方块名写对应的 block entity id(原版区分 chest/trapped_chest/barrel)
            String bn = BlockStateHelper.getName(
                WorldManager.getBlockState(p.dim(), p.x(), p.y(), p.z()));
            String beId = switch (bn) {
                case "trapped_chest" -> "minecraft:trapped_chest";
                case "barrel" -> "minecraft:barrel";
                default -> "minecraft:chest";
            };
            b.putString("id", beId);
            b.putInt("x", p.x());
            b.putInt("y", p.y());
            b.putInt("z", p.z());
        }
        b.putList("Items", org.cloudburstmc.nbt.NbtType.COMPOUND, items);
        chunk.setBlockEntity(p.x() & 15, p.y(), p.z() & 15, b.build());
    }

    private ContainerStore.FurnaceData getFurnaceData(int windowId) {
        ContainerStore.Pos p = openFurnaces.get(windowId);
        if (p == null) return null;
        return ContainerStore.furnace(p, furnaceTypeAt(p));
    }

    private static String furnaceTypeAt(ContainerStore.Pos p) {
        int st = WorldManager.getBlockState(p.dim(), p.x(), p.y(), p.z());
        String n = BlockStateHelper.getName(st);
        if ("blast_furnace".equals(n) || "smoker".equals(n)) return n;
        return "furnace";
    }

    private int[] getSlotItem(int windowId, int slot) {
        var pluginInv = openPluginMenus.get(windowId);
        if (pluginInv != null) {
            if (slot >= 0 && slot < pluginInv.getSize()) {
                return new int[]{pluginInv.slotId(slot), pluginInv.slotCount(slot)};
            }
            int ps = pluginMenuPlayerSlot(pluginInv, slot);
            if (ps < 0 || ps >= 46) return new int[]{0, 0};
            return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
        }
        if (windowId == 0) {
            if (slot < 0 || slot >= 46) return new int[]{0, 0};
            return new int[]{data.inventoryIds[slot], data.inventoryCounts[slot]};
        } else if (openCraftingGrids.containsKey(windowId)) {
            if (slot >= 1 && slot <= 9) {
                String[] grid = openCraftingGrids.get(windowId);
                String name = grid[slot - 1];
                if (name == null) return new int[]{0, 0};
                int gc = craftingCounts(windowId)[slot - 1];
                if (gc <= 0) gc = 1;
                return new int[]{BlockManager.getItemIdByName(name), gc};
            } else if (slot >= 10) {
                int ps = craftingToPlayerSlot(slot);
                if (ps < 0) return new int[]{0, 0};
                return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
            }
            return new int[]{0, 0};
        } else if (openChests.containsKey(windowId) || openEnderChests.containsKey(windowId)) {
            // Bug51: 双箱窗口按 0-26 本箱 / 27-53 邻箱路由
            ContainerStore.ChestData cdata = chestDataForSlot(windowId, slot);
            if (cdata != null) {
                int ls = isPartnerChestSlot(windowId, slot) ? slot - 27 : slot;
                return new int[]{cdata.slots[ls * 2], cdata.slots[ls * 2 + 1]};
            } else {
                int ps = chestToPlayerSlot(windowId, slot);
                if (ps < 0) return new int[]{0, 0};
                return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
            }
        } else if (openFurnaces.containsKey(windowId)) {
            int[] contents = getFurnaceContents(windowId);
            if (slot >= 0 && slot < 3) {
                return new int[]{contents[slot * 2], contents[slot * 2 + 1]};
            } else {
                int ps = furnaceToPlayerSlot(slot);
                if (ps < 0) return new int[]{0, 0};
                return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
            }
        } else if (openHoppers.containsKey(windowId)) {
            int[] contents = getHopperContents(windowId);
            if (slot >= 0 && slot < 5) {
                return new int[]{contents[slot * 2], contents[slot * 2 + 1]};
            } else {
                int ps = hopperToPlayerSlot(slot);
                if (ps < 0) return new int[]{0, 0};
                return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
            }
        } else if (openDispensers.containsKey(windowId)) {
            int[] contents = getDispenserContents(windowId);
            if (slot >= 0 && slot < 9) {
                return new int[]{contents[slot * 2], contents[slot * 2 + 1]};
            } else {
                int ps = dispenserToPlayerSlot(slot);
                if (ps < 0) return new int[]{0, 0};
                return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
            }
        } else if (openStonecutters.containsKey(windowId)) {
            int[] contents = getStonecutterContents(windowId);
            if (slot >= 0 && slot < 2) {
                return new int[]{contents[slot * 2], contents[slot * 2 + 1]};
            } else {
                int ps = stonecutterToPlayerSlot(slot);
                if (ps < 0) return new int[]{0, 0};
                return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
            }
        } else if (openBeacons.containsKey(windowId)) {
            // #15 信标: 容器槽 0 = 支付物槽; 其余为玩家背包(slot 1 起)。
            if (slot == 0) {
                ContainerStore.BeaconData bd = ContainerStore.beacon(openBeacons.get(windowId));
                int[] c = bd.paymentSlot;
                return new int[]{c[0], c[1]};
            } else {
                int ps = 8 + slot; // 槽1 → 玩家槽9
                if (ps > 44) return new int[]{0, 0};
                return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
            }
        } else if (openGrindstones.containsKey(windowId)) {
            int[] contents = getGrindstoneContents(windowId);
            if (slot >= 0 && slot < 3) {
                return new int[]{contents[slot * 2], contents[slot * 2 + 1]};
            } else {
                int ps = grindstoneToPlayerSlot(slot);
                if (ps < 0) return new int[]{0, 0};
                return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
            }
        } else if (openSmithing.containsKey(windowId)) {
            int[] c = getSmithingContents(windowId);
            // #19 原版 SmithingMenu: 4 容器槽(0-3) + 36 玩家槽(从 4 起)。
            if (slot >= 0 && slot < 4) return new int[]{c[slot * 2], c[slot * 2 + 1]};
            int ps = 9 + (slot - 4); if (ps > 44) return new int[]{0, 0};
            return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
        } else if (openEnchanting.containsKey(windowId)) {
            int[] c = getEnchantingContents(windowId);
            // #44 修复: 附魔台仅 2 容器槽(物品+青金石), 背包从 slot 2 起。
            // 曾用 slot<3 + ps=9+(slot-3) -> 拿起物品错位一格(拿到左边物品), 且背包首格被吞。
            if (slot >= 0 && slot < 2) return new int[]{c[slot * 2], c[slot * 2 + 1]};
            int ps = 9 + (slot - 2); if (ps > 44) return new int[]{0, 0};
            return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
        } else if (openAnvil.containsKey(windowId)) {
            int[] c = getAnvilContents(windowId);
            if (slot >= 0 && slot < 3) return new int[]{c[slot * 2], c[slot * 2 + 1]};
            int ps = 9 + (slot - 3); if (ps > 44) return new int[]{0, 0};
            return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
        } else if (openBrewing.containsKey(windowId)) {
            int[] c = getBrewingContents(windowId);
            if (slot >= 0 && slot < 5) return new int[]{c[slot * 2], c[slot * 2 + 1]};
            int ps = 9 + (slot - 5); if (ps > 44) return new int[]{0, 0};
            return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
        } else if (openMerchants.containsKey(windowId)) {
            int[] c = getMerchantContents(windowId);
            if (slot >= 0 && slot < 3) return new int[]{c[slot * 2], c[slot * 2 + 1]};
            int ps = merchantToPlayerSlot(slot);
            if (ps < 0) return new int[]{0, 0};
            return new int[]{data.inventoryIds[ps], data.inventoryCounts[ps]};
        }
        return new int[]{0, 0};
    }

    // ── Bug4/33: 槽位组件统一读写 ────────────────────────────────────────────
    // 旧实现把"拿起=组件进光标/放入=组件出光标"散落在 setSlotItem 各分支,
    // 熔炉/漏斗/发射器/切石机/砂轮/锻造台/附魔台/信标等槽位完全没有组件存储,
    // shift-click/数字键/丢出/死亡掉落/掉落物拾取也不搬组件 -> NBT 到处丢失。
    // 现在: readSlotMeta/writeSlotMeta 统一寻址所有窗口槽位, 物品移动一律
    // "读源组件 → 写目标(setSlotItemFull) → 清源", 光标字段只作为中转。

    private static ItemMeta contMeta(ContainerStore.SlotMeta sm, int s) {
        return ItemMeta.of(sm.slotEnchants[s], sm.slotPotion[s], sm.slotCustomName[s], sm.slotDamage[s], -1, -1);
    }

    private ItemMeta playerSlotMeta(int ps) {
        if (ps < 0 || ps >= 46) return ItemMeta.EMPTY;
        return ItemMeta.of(data.inventoryEnchants[ps], data.inventoryPotion[ps],
                data.inventoryCustomName[ps], data.inventoryDamage[ps],
                data.inventoryTrimMaterial[ps], data.inventoryTrimPattern[ps]);
    }

    private void writePlayerSlotMeta(int ps, ItemMeta m) {
        if (ps < 0 || ps >= 46) return;
        boolean empty = m == null || m.isEmpty();
        if (empty) {
            data.inventoryEnchants[ps] = new java.util.HashMap<>();
            data.inventoryPotion[ps] = null;
            data.inventoryCustomName[ps] = null;
            data.inventoryDamage[ps] = 0;
            data.inventoryTrimMaterial[ps] = -1;
            data.inventoryTrimPattern[ps] = -1;
        } else {
            data.inventoryEnchants[ps] = new java.util.HashMap<>(m.enchants());
            data.inventoryPotion[ps] = m.potion();
            data.inventoryCustomName[ps] = m.customName();
            data.inventoryDamage[ps] = m.damage();
            data.inventoryTrimMaterial[ps] = m.trimMaterial();
            data.inventoryTrimPattern[ps] = m.trimPattern();
        }
    }

    /** 读任意窗口槽位的物品组件(无则 EMPTY)。 */
    private ItemMeta readSlotMeta(int windowId, int slot) {
        var pluginInv = openPluginMenus.get(windowId);
        if (pluginInv != null) {
            if (slot >= 0 && slot < pluginInv.getSize()) {
                com.CharunCore.server.plugin.api.ItemMeta pm = pluginInv.slotMeta(slot);
                if (pm == null || pm.isEmpty()) return ItemMeta.EMPTY;
                return toInternalMeta(pm);
            }
            return playerSlotMeta(pluginMenuPlayerSlot(pluginInv, slot));
        }
        if (windowId == 0) return playerSlotMeta(slot);
        if (openCraftingGrids.containsKey(windowId)) {
            if (slot >= 1 && slot <= 9) {
                ItemMeta[] ms = openGridMetas.get(windowId);
                if (ms == null || slot - 1 >= ms.length || ms[slot - 1] == null) return ItemMeta.EMPTY;
                return ms[slot - 1];
            }
            return playerSlotMeta(craftingToPlayerSlot(slot));
        }
        if (openChests.containsKey(windowId)) {
            ContainerStore.ChestData cd = chestDataForSlot(windowId, slot);
            if (cd != null) {
                int ls = isPartnerChestSlot(windowId, slot) ? slot - 27 : slot;
                return contMeta(cd.meta, ls);
            }
            return playerSlotMeta(chestToPlayerSlot(windowId, slot));
        }
        if (openEnderChests.containsKey(windowId)) {
            ContainerStore.ChestData cd = ContainerStore.enderChest(this.uuid);
            if (slot >= 0 && slot < 27) return contMeta(cd.meta, slot);
            return playerSlotMeta(chestToPlayerSlot(windowId, slot));
        }
        if (openFurnaces.containsKey(windowId)) {
            ContainerStore.FurnaceData fd = getFurnaceData(windowId);
            if (fd != null && slot >= 0 && slot < 3) return contMeta(fd.meta, slot);
            return playerSlotMeta(furnaceToPlayerSlot(slot));
        }
        if (openHoppers.containsKey(windowId)) {
            ContainerStore.HopperData hd = getHopperData(windowId);
            if (hd != null && slot >= 0 && slot < 5) return contMeta(hd.meta, slot);
            return playerSlotMeta(hopperToPlayerSlot(slot));
        }
        if (openDispensers.containsKey(windowId)) {
            ContainerStore.DispenserData dd = getDispenserData(windowId);
            if (dd != null && slot >= 0 && slot < 9) return contMeta(dd.meta, slot);
            return playerSlotMeta(dispenserToPlayerSlot(slot));
        }
        if (openStonecutters.containsKey(windowId)) {
            ContainerStore.StonecutterData sd = getStonecutterData(windowId);
            if (sd != null && slot >= 0 && slot < 2) return contMeta(sd.meta, slot);
            return playerSlotMeta(stonecutterToPlayerSlot(slot));
        }
        if (openGrindstones.containsKey(windowId)) {
            ContainerStore.GrindstoneData gd = getGrindstoneData(windowId);
            if (gd != null && slot >= 0 && slot < 3) return contMeta(gd.meta, slot);
            return playerSlotMeta(grindstoneToPlayerSlot(slot));
        }
        if (openBeacons.containsKey(windowId)) {
            ContainerStore.BeaconData bd = ContainerStore.beacon(openBeacons.get(windowId));
            if (slot == 0) return contMeta(bd.meta, 0);
            return playerSlotMeta(8 + slot);
        }
        if (openSmithing.containsKey(windowId)) {
            ContainerStore.SmithingData sd = getSmithingData(windowId);
            if (sd != null && slot >= 0 && slot < 4) return contMeta(sd.meta, slot);
            return playerSlotMeta(9 + (slot - 4));
        }
        if (openEnchanting.containsKey(windowId)) {
            ContainerStore.EnchantingData ed = getEnchantingData(windowId);
            if (ed != null && slot >= 0 && slot < 2) return contMeta(ed.meta, slot);
            return playerSlotMeta(9 + (slot - 2));
        }
        if (openAnvil.containsKey(windowId)) {
            ContainerStore.AnvilData ad = getAnvilData(windowId);
            if (ad != null && slot >= 0 && slot < 3) {
                if (slot == 0) return ItemMeta.of(ad.leftEnchants, ad.leftPotion, ad.leftName, ad.leftDamage, -1, -1);
                if (slot == 1) return ItemMeta.of(ad.rightEnchants, ad.rightPotion, ad.rightName, ad.rightDamage, -1, -1);
                return ItemMeta.of(ad.outEnchants, ad.outPotion, ad.outName, ad.outDamage, -1, -1);
            }
            return playerSlotMeta(9 + (slot - 3));
        }
        if (openBrewing.containsKey(windowId)) {
            ContainerStore.BrewingData bd = getBrewingData(windowId);
            if (bd != null && slot >= 0 && slot < 5) {
                if (slot < 3) {
                    return ItemMeta.of(bd.meta.slotEnchants[slot], bd.potionType[slot],
                            bd.meta.slotCustomName[slot], bd.meta.slotDamage[slot], -1, -1);
                }
                return contMeta(bd.meta, slot);
            }
            return playerSlotMeta(9 + (slot - 5));
        }
        if (openMerchants.containsKey(windowId)) {
            if (slot < 3) return ItemMeta.EMPTY;
            return playerSlotMeta(merchantToPlayerSlot(slot));
        }
        return ItemMeta.EMPTY;
    }

    /** 写任意窗口槽位的物品组件; m 为 null/EMPTY 时清空。 */
    private void writeSlotMeta(int windowId, int slot, ItemMeta m) {
        var pluginInv = openPluginMenus.get(windowId);
        if (pluginInv != null) {
            if (slot >= 0 && slot < pluginInv.getSize()) {
                boolean empty = m == null || m.isEmpty();
                pluginInv.writeSlotRaw(slot,
                        pluginInv.slotId(slot), pluginInv.slotCount(slot),
                        empty ? null : fromInternalMeta(m));
            } else {
                writePlayerSlotMeta(pluginMenuPlayerSlot(pluginInv, slot), m);
            }
            return;
        }
        boolean empty = m == null || m.isEmpty();
        if (windowId == 0) { writePlayerSlotMeta(slot, m); return; }
        if (openCraftingGrids.containsKey(windowId)) {
            if (slot >= 1 && slot <= 9) {
                ItemMeta[] ms = openGridMetas.computeIfAbsent(windowId, k -> new ItemMeta[9]);
                ms[slot - 1] = empty ? null : m;
                return;
            }
            writePlayerSlotMeta(craftingToPlayerSlot(slot), m);
            return;
        }
        if (openChests.containsKey(windowId) || openEnderChests.containsKey(windowId)) {
            ContainerStore.ChestData cd = chestDataForSlot(windowId, slot);
            if (cd != null) {
                int ls = isPartnerChestSlot(windowId, slot) ? slot - 27 : slot;
                cd.meta.slotDamage[ls] = empty ? 0 : m.damage();
                cd.meta.slotEnchants[ls] = (empty || m.enchants().isEmpty())
                        ? new java.util.HashMap<>() : new java.util.HashMap<>(m.enchants());
                cd.meta.slotPotion[ls] = empty ? null : m.potion();
                cd.meta.slotCustomName[ls] = empty ? null : m.customName();
                return;
            }
        }
        if (openFurnaces.containsKey(windowId)) {
            ContainerStore.FurnaceData fd = getFurnaceData(windowId);
            if (fd != null && slot >= 0 && slot < 3) { writeContMeta(fd.meta, slot, m); return; }
            writePlayerSlotMeta(furnaceToPlayerSlot(slot), m);
            return;
        }
        if (openHoppers.containsKey(windowId)) {
            ContainerStore.HopperData hd = getHopperData(windowId);
            if (hd != null && slot >= 0 && slot < 5) { writeContMeta(hd.meta, slot, m); return; }
            writePlayerSlotMeta(hopperToPlayerSlot(slot), m);
            return;
        }
        if (openDispensers.containsKey(windowId)) {
            ContainerStore.DispenserData dd = getDispenserData(windowId);
            if (dd != null && slot >= 0 && slot < 9) { writeContMeta(dd.meta, slot, m); return; }
            writePlayerSlotMeta(dispenserToPlayerSlot(slot), m);
            return;
        }
        if (openStonecutters.containsKey(windowId)) {
            ContainerStore.StonecutterData sd = getStonecutterData(windowId);
            if (sd != null && slot >= 0 && slot < 2) { writeContMeta(sd.meta, slot, m); return; }
            writePlayerSlotMeta(stonecutterToPlayerSlot(slot), m);
            return;
        }
        if (openGrindstones.containsKey(windowId)) {
            ContainerStore.GrindstoneData gd = getGrindstoneData(windowId);
            if (gd != null && slot >= 0 && slot < 3) { writeContMeta(gd.meta, slot, m); return; }
            writePlayerSlotMeta(grindstoneToPlayerSlot(slot), m);
            return;
        }
        if (openBeacons.containsKey(windowId)) {
            ContainerStore.BeaconData bd = ContainerStore.beacon(openBeacons.get(windowId));
            if (slot == 0) { writeContMeta(bd.meta, 0, m); return; }
            writePlayerSlotMeta(8 + slot, m);
            return;
        }
        if (openSmithing.containsKey(windowId)) {
            ContainerStore.SmithingData sd = getSmithingData(windowId);
            if (sd != null && slot >= 0 && slot < 4) { writeContMeta(sd.meta, slot, m); return; }
            writePlayerSlotMeta(9 + (slot - 4), m);
            return;
        }
        if (openEnchanting.containsKey(windowId)) {
            ContainerStore.EnchantingData ed = getEnchantingData(windowId);
            if (ed != null && slot >= 0 && slot < 2) { writeContMeta(ed.meta, slot, m); return; }
            writePlayerSlotMeta(9 + (slot - 2), m);
            return;
        }
        if (openAnvil.containsKey(windowId)) {
            ContainerStore.AnvilData ad = getAnvilData(windowId);
            if (ad == null) { writePlayerSlotMeta(9 + (slot - 3), m); return; }
            if (slot == 0) {
                ad.leftEnchants = empty ? new java.util.HashMap<>() : new java.util.HashMap<>(m.enchants());
                ad.leftDamage = empty ? 0 : m.damage();
                ad.leftPotion = empty ? null : m.potion();
                ad.leftName = empty ? null : m.customName();
            } else if (slot == 1) {
                ad.rightEnchants = empty ? new java.util.HashMap<>() : new java.util.HashMap<>(m.enchants());
                ad.rightDamage = empty ? 0 : m.damage();
                ad.rightPotion = empty ? null : m.potion();
                ad.rightName = empty ? null : m.customName();
            } else if (slot == 2) {
                ad.outEnchants = empty ? new java.util.HashMap<>() : new java.util.HashMap<>(m.enchants());
                ad.outDamage = empty ? 0 : m.damage();
                ad.outPotion = empty ? null : m.potion();
                ad.outName = empty ? null : m.customName();
            } else {
                writePlayerSlotMeta(9 + (slot - 3), m);
            }
            return;
        }
        if (openBrewing.containsKey(windowId)) {
            ContainerStore.BrewingData bd = getBrewingData(windowId);
            if (bd == null) { writePlayerSlotMeta(9 + (slot - 5), m); return; }
            if (slot >= 0 && slot < 5) {
                if (slot < 3) {
                    bd.potionType[slot] = empty ? null : m.potion();
                    bd.meta.slotDamage[slot] = empty ? 0 : m.damage();
                    bd.meta.slotEnchants[slot] = (empty || m.enchants().isEmpty())
                            ? new java.util.HashMap<>() : new java.util.HashMap<>(m.enchants());
                    bd.meta.slotCustomName[slot] = empty ? null : m.customName();
                    bd.meta.slotPotion[slot] = null;
                } else {
                    writeContMeta(bd.meta, slot, m);
                }
                return;
            }
            writePlayerSlotMeta(9 + (slot - 5), m);
            return;
        }
        if (openMerchants.containsKey(windowId)) {
            if (slot < 3) return;
            writePlayerSlotMeta(merchantToPlayerSlot(slot), m);
        }
    }

    private static void writeContMeta(ContainerStore.SlotMeta sm, int s, ItemMeta m) {
        boolean empty = m == null || m.isEmpty();
        sm.slotDamage[s] = empty ? 0 : m.damage();
        sm.slotEnchants[s] = (empty || m.enchants().isEmpty())
                ? new java.util.HashMap<>() : new java.util.HashMap<>(m.enchants());
        sm.slotPotion[s] = empty ? null : m.potion();
        sm.slotCustomName[s] = empty ? null : m.customName();
    }

    /** 写槽位并携带组件(物品移动的规范入口)。元数据经 pendingWriteMeta 在 setSlotItem
     *  内部生效, 保证依赖元数据的重算(铁砧/锻造台等)读到的是新值。 */
    private ItemMeta pendingWriteMeta = null;

    private void setSlotItemFull(int windowId, int slot, int itemId, int count, ItemMeta meta) {
        boolean empty = itemId <= 0 || count <= 0 || meta == null || meta.isEmpty();
        pendingWriteMeta = empty ? null : meta;
        try {
            setSlotItem(windowId, slot, itemId, count);
        } finally {
            pendingWriteMeta = null;
        }
    }

    /** 把源槽整个搬到目标槽(id/count+组件), 并清空源槽。 */
    private void moveSlotFull(int windowId, int src, int dst) {
        int[] item = getSlotItem(windowId, src);
        ItemMeta m = readSlotMeta(windowId, src);
        setSlotItemFull(windowId, dst, item[0], item[1], m);
        setSlotItem(windowId, src, 0, 0);
    }

    /** 写槽位(id/count)。组件语义: 清槽时同时清组件; 放入普通物品时覆盖为无组件。
     *  需要保留/写入组件的场景必须走 setSlotItemFull。 */
    private void setSlotItem(int windowId, int slot, int itemId, int count) {
        boolean emptied = (itemId <= 0 || count <= 0);
        var pluginInv = openPluginMenus.get(windowId);
        if (pluginInv != null) {
            if (slot >= 0 && slot < pluginInv.getSize()) {
                boolean empty = pendingWriteMeta == null || pendingWriteMeta.isEmpty();
                pluginInv.writeSlotRaw(slot, Math.max(itemId, 0), Math.max(count, 0),
                        empty ? null : fromInternalMeta(pendingWriteMeta));
                sendPluginMenuContent(windowId, pluginInv);
            } else {
                int ps = pluginMenuPlayerSlot(pluginInv, slot);
                if (ps >= 0 && ps < 46) {
                    writePlayerSlotMeta(ps, pendingWriteMeta);
                    data.inventoryIds[ps] = itemId;
                    data.inventoryCounts[ps] = count;
                    sendSlotUpdateRaw(0, ps, itemId, count);
                }
            }
            return;
        }
        if (windowId == 0) {
            if (slot < 0 || slot >= 46) return;
            writePlayerSlotMeta(slot, pendingWriteMeta);
            data.inventoryIds[slot] = itemId;
            data.inventoryCounts[slot] = count;
            return;
        }
        if (openCraftingGrids.containsKey(windowId)) {
            if (slot >= 1 && slot <= 9) {
                String[] grid = openCraftingGrids.get(windowId);
                boolean has = itemId > 0 && count > 0;
                grid[slot - 1] = has ? BlockManager.itemIdToName(itemId) : null;
                craftingCounts(windowId)[slot - 1] = has ? count : 0;
                ItemMeta[] ms = openGridMetas.computeIfAbsent(windowId, k -> new ItemMeta[9]);
                ms[slot - 1] = (pendingWriteMeta == null || pendingWriteMeta.isEmpty()) ? null : pendingWriteMeta;
            } else if (slot >= 10) {
                int ps = craftingToPlayerSlot(slot);
                if (ps < 0) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId;
                data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
            return;
        }
        if (openChests.containsKey(windowId) || openEnderChests.containsKey(windowId)) {
            // Bug51: 双箱窗口按 0-26 本箱 / 27-53 邻箱路由
            ContainerStore.ChestData cd = chestDataForSlot(windowId, slot);
            if (cd != null) {
                int ls = isPartnerChestSlot(windowId, slot) ? slot - 27 : slot;
                cd.setChestSlot(ls, Math.max(itemId, 0), Math.max(count, 0),
                    pendingWriteMeta != null && !pendingWriteMeta.isEmpty() ? pendingWriteMeta.damage() : 0,
                    pendingWriteMeta != null && !pendingWriteMeta.isEmpty() && !pendingWriteMeta.enchants().isEmpty()
                        ? pendingWriteMeta.enchants() : null,
                    pendingWriteMeta != null && !pendingWriteMeta.isEmpty() ? pendingWriteMeta.potion() : null,
                    pendingWriteMeta != null && !pendingWriteMeta.isEmpty() ? pendingWriteMeta.customName() : null);
                cd.version++;
                // #14: 容器内容变化 -> 邻接比较器(满度信号)重算。普通点击与 shift 均经此处写入。
                ContainerStore.Pos notifyPos = chestDataPosForSlot(windowId, slot);
                if (notifyPos != null) {
                    RedstoneEngine.onBlockChanged(notifyPos.dim(), notifyPos.x(), notifyPos.y(), notifyPos.z());
                }
            } else {
                int ps = chestToPlayerSlot(windowId, slot);
                if (ps < 0) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId;
                data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
            return;
        }
        if (openFurnaces.containsKey(windowId)) {
            ContainerStore.FurnaceData fd = getFurnaceData(windowId);
            if (fd == null) return;
            int[] contents = fd.slots;
            if (slot >= 0 && slot < 3) {
                if (slot == 2 && count < contents[5] && fd.xpStore >= 1.0f) {
                    int xp = (int) fd.xpStore;
                    fd.xpStore -= xp;
                    if (xp > 0) addExperience(xp);
                }
                contents[slot * 2] = itemId;
                contents[slot * 2 + 1] = count;
                writeContMeta(fd.meta, slot, pendingWriteMeta);
                fd.version++;
            } else {
                int ps = furnaceToPlayerSlot(slot);
                if (ps < 0) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId;
                data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
            return;
        }
        if (openHoppers.containsKey(windowId)) {
            ContainerStore.HopperData hd = getHopperData(windowId);
            int[] contents = getHopperContents(windowId);
            if (slot >= 0 && slot < 5) {
                contents[slot * 2] = itemId;
                contents[slot * 2 + 1] = count;
                if (hd != null) { writeContMeta(hd.meta, slot, pendingWriteMeta); hd.version++; }
            } else {
                int ps = hopperToPlayerSlot(slot);
                if (ps < 0) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId;
                data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
            return;
        }
        if (openDispensers.containsKey(windowId)) {
            int[] contents = getDispenserContents(windowId);
            if (slot >= 0 && slot < 9) {
                contents[slot * 2] = itemId;
                contents[slot * 2 + 1] = count;
                ContainerStore.DispenserData dd = getDispenserData(windowId);
                if (dd != null) { writeContMeta(dd.meta, slot, pendingWriteMeta); dd.version++; }
                ContainerStore.persistDispenser(openDispensers.get(windowId));
            } else {
                int ps = dispenserToPlayerSlot(slot);
                if (ps < 0) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId;
                data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
            return;
        }
        if (openStonecutters.containsKey(windowId)) {
            int[] contents = getStonecutterContents(windowId);
            if (slot >= 0 && slot < 2) {
                contents[slot * 2] = itemId;
                contents[slot * 2 + 1] = count;
                ContainerStore.StonecutterData sd = getStonecutterData(windowId);
                if (sd != null) { writeContMeta(sd.meta, slot, pendingWriteMeta); }
                if (slot == 0) recomputeStonecutter(windowId);
                else if (sd != null) sd.version++;
            } else {
                int ps = stonecutterToPlayerSlot(slot);
                if (ps < 0) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId;
                data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
            return;
        }
        if (openGrindstones.containsKey(windowId)) {
            int[] contents = getGrindstoneContents(windowId);
            if (slot >= 0 && slot < 3) {
                contents[slot * 2] = itemId;
                contents[slot * 2 + 1] = count;
                ContainerStore.GrindstoneData gd = getGrindstoneData(windowId);
                if (gd != null) { writeContMeta(gd.meta, slot, pendingWriteMeta); }
                if (slot < 2) recomputeGrindstone(windowId);
                else if (gd != null) gd.version++;
            } else {
                int ps = grindstoneToPlayerSlot(slot);
                if (ps < 0) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId;
                data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
            return;
        }
        if (openBeacons.containsKey(windowId)) {
            // #15 信标支付物槽: 仅接受原版 BEACON_PAYMENT_ITEMS tag(铁锭/金锭/绿宝石/钻石/下界合金锭)。
            ContainerStore.BeaconData bd = ContainerStore.beacon(openBeacons.get(windowId));
            if (slot == 0) {
                String inName = BlockManager.itemIdToName(itemId);
                if (itemId > 0 && !isBeaconPayment(inName)) return; // 非法支付物拒收
                bd.paymentSlot[0] = itemId;
                bd.paymentSlot[1] = count;
                bd.payment = itemId > 0;
                writeContMeta(bd.meta, 0, pendingWriteMeta);
                bd.version++;
                sendSlotUpdate(windowId, 0);
            } else {
                int ps = 8 + slot;
                if (ps > 44) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId;
                data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
            return;
        }
        if (openSmithing.containsKey(windowId)) {
            ContainerStore.SmithingData sd = getSmithingData(windowId);
            if (sd == null) return;
            int[] c = sd.slots;
            // #19 4 容器槽(0-3)
            if (slot >= 0 && slot < 4) { c[slot * 2] = itemId; c[slot * 2 + 1] = count; writeContMeta(sd.meta, slot, pendingWriteMeta); sd.version++; recomputeSmithing(windowId); }
            else { int ps = 9 + (slot - 4); if (ps > 44) return; writePlayerSlotMeta(ps, pendingWriteMeta); data.inventoryIds[ps] = itemId; data.inventoryCounts[ps] = count; sendSlotUpdateRaw(0, ps, itemId, count); }
            return;
        }
        if (openEnchanting.containsKey(windowId)) {
            ContainerStore.EnchantingData ed = getEnchantingData(windowId);
            if (ed == null) return;
            int[] c = ed.slots;
            // 附魔台仅 2 容器槽(物品+青金石)，背包从 slot 2 开始。
            if (slot >= 0 && slot < 2) { c[slot * 2] = itemId; c[slot * 2 + 1] = count; writeContMeta(ed.meta, slot, pendingWriteMeta); ed.version++; recomputeEnchanting(windowId); }
            else { int ps = 9 + (slot - 2); if (ps > 44) return; writePlayerSlotMeta(ps, pendingWriteMeta); data.inventoryIds[ps] = itemId; data.inventoryCounts[ps] = count; sendSlotUpdateRaw(0, ps, itemId, count); }
            return;
        }
        if (openAnvil.containsKey(windowId)) {
            ContainerStore.AnvilData ad = getAnvilData(windowId);
            if (ad == null) return;
            int[] c = ad.slots;
            if (slot >= 0 && slot < 3) {
                c[slot * 2] = itemId; c[slot * 2 + 1] = count; ad.version++;
                writeSlotMeta(windowId, slot, pendingWriteMeta);
                recomputeAnvil(windowId);
            } else {
                int ps = 9 + (slot - 3);
                if (ps > 44) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId; data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
            return;
        }
        if (openBrewing.containsKey(windowId)) {
            ContainerStore.BrewingData bd = getBrewingData(windowId);
            if (bd == null) return;
            int[] c = bd.slots;
            if (slot >= 0 && slot < 5) {
                c[slot * 2] = itemId; c[slot * 2 + 1] = count; bd.version++;
                writeSlotMeta(windowId, slot, pendingWriteMeta);
            } else {
                int ps = 9 + (slot - 5);
                if (ps > 44) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId; data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
            return;
        }
        if (openMerchants.containsKey(windowId)) {
            MerchantSession ms = openMerchants.get(windowId);
            if (ms == null) return;
            if (slot >= 0 && slot < 3) {
                ms.slots[slot * 2] = itemId;
                ms.slots[slot * 2 + 1] = count;
                ms.version++;
            } else {
                int ps = merchantToPlayerSlot(slot);
                if (ps < 0) return;
                writePlayerSlotMeta(ps, pendingWriteMeta);
                data.inventoryIds[ps] = itemId; data.inventoryCounts[ps] = count;
                sendSlotUpdateRaw(0, ps, itemId, count);
            }
        }
    }

    private int getMaxStackSize(int itemId) {
        if (itemId <= 0) return 0;
        // 原版堆叠上限 (items.json stackSize): 末影珍珠/箭 16, 桶/药水 1, 其余 64
        return BlockManager.getStackSize(itemId);
    }

    private void handleContainerClick(int windowId, int slot, int button) {
        if (slot < 0) return;

        var pluginInv0 = openPluginMenus.get(windowId);
        if (pluginInv0 != null) {
            com.CharunCore.server.plugin.api.InventoryClickContext pc =
                    new com.CharunCore.server.plugin.api.InventoryClickContext(
                            this, pluginInv0, slot, button,
                            button == 1 ? com.CharunCore.server.plugin.api.InventoryClickContext.ClickType.PICKUP_HALF
                                    : com.CharunCore.server.plugin.api.InventoryClickContext.ClickType.PICKUP);
            pluginInv0.dispatchClick(pc);
            if (pc.isCancelled()) {
                sendPluginMenuContent(windowId, pluginInv0);
                sendCarriedItem();
                return;
            }
            if (pc.isHandled()) {
                sendPluginMenuContent(windowId, pluginInv0);
                return;
            }
        }

        if (openCraftingGrids.containsKey(windowId) && slot == 0) {
            handleCraftingResultClick(windowId, button);
            return;
        }

        if (windowId == 0 && slot == 0) {
            handlePlayerCraftingResultClick();
            return;
        }

        // 处理器结果槽: 取走结果必须消耗输入, 否则无限复制
        if (openAnvil.containsKey(windowId) && slot == 2) {
            takeProcessorResult(windowId, button);
            return;
        }
        if (openSmithing.containsKey(windowId) && slot == 3) { // #19 原版结果槽=3
            takeProcessorResult(windowId, button);
            return;
        }
        if (openStonecutters.containsKey(windowId) && slot == 1) {
            takeProcessorResult(windowId, button);
            return;
        }
        if (openGrindstones.containsKey(windowId) && slot == 2) {
            takeProcessorResult(windowId, button);
            return;
        }
        if (openMerchants.containsKey(windowId) && slot == 0) {
            executeTrade(windowId, button);
            return;
        }


        int[] slotItem = getSlotItem(windowId, slot);

        if (button == 0) {
            if (carriedItemCount <= 0) {
                if (slotItem[1] > 0) {
                    carriedItemId = slotItem[0];
                    carriedItemCount = slotItem[1];
                    loadCarriedFrom(readSlotMeta(windowId, slot));
                    setSlotItem(windowId, slot, 0, 0);
                    sendSlotUpdate(windowId, slot);
                    sendCarriedItem();
                }
            } else {
                if (slotItem[1] <= 0) {
                    ItemMeta cm = carriedSnapshot();
                    setSlotItemFull(windowId, slot, carriedItemId, carriedItemCount, cm);
                    clearCarriedMeta();
                    carriedItemId = 0;
                    carriedItemCount = 0;
                    sendSlotUpdate(windowId, slot);
                    sendCarriedItem();
                } else if (slotItem[0] == carriedItemId) {
                    // Bug4/33: 任一方携带组件即不可并堆(原版带组件物品堆叠上限为 1)
                    boolean mergeable = readSlotMeta(windowId, slot).isEmpty()
                            && carriedSnapshot().isEmpty();
                    int max = getMaxStackSize(carriedItemId);
                    int canPlace = Math.min(max - slotItem[1], carriedItemCount);
                    if (mergeable && canPlace > 0) {
                        setSlotItemFull(windowId, slot, carriedItemId, slotItem[1] + canPlace, ItemMeta.EMPTY);
                        carriedItemCount -= canPlace;
                        if (carriedItemCount <= 0) {
                            carriedItemId = 0;
                            carriedItemCount = 0;
                        }
                        sendSlotUpdate(windowId, slot);
                        sendCarriedItem();
                    }
                } else {
                    ItemMeta targetMeta = readSlotMeta(windowId, slot);
                    int curId = carriedItemId, curCnt = carriedItemCount;
                    setSlotItemFull(windowId, slot, curId, curCnt, carriedSnapshot());
                    loadCarriedFrom(targetMeta);
                    carriedItemId = slotItem[0];
                    carriedItemCount = slotItem[1];
                    sendSlotUpdate(windowId, slot);
                    sendCarriedItem();
                }
            }
        } else if (button == 1) {
            if (carriedItemCount <= 0) {
                if (slotItem[1] > 0) {
                    int half = (slotItem[1] + 1) / 2;
                    carriedItemId = slotItem[0];
                    carriedItemCount = half;
                    int remaining = slotItem[1] - half;
                    ItemMeta srcMeta = readSlotMeta(windowId, slot);
                    if (remaining <= 0) {
                        loadCarriedFrom(srcMeta); // 整组拿走: 组件随光标
                        setSlotItem(windowId, slot, 0, 0);
                    } else {
                        loadCarriedFrom(null);   // 只可能发生在普通可堆叠物品上
                        setSlotItem(windowId, slot, slotItem[0], remaining);
                    }
                    sendSlotUpdate(windowId, slot);
                    sendCarriedItem();
                }
            } else {
                if (slotItem[1] <= 0) {
                    ItemMeta cm = carriedSnapshot();
                    setSlotItemFull(windowId, slot, carriedItemId, 1,
                            carriedItemCount == 1 ? cm : ItemMeta.EMPTY);
                    if (carriedItemCount == 1) { clearCarriedMeta(); carriedItemId = 0; }
                    carriedItemCount--;
                    if (carriedItemCount <= 0) {
                        carriedItemId = 0;
                        carriedItemCount = 0;
                    }
                    sendSlotUpdate(windowId, slot);
                    sendCarriedItem();
                } else if (slotItem[0] == carriedItemId
                        && readSlotMeta(windowId, slot).isEmpty()
                        && carriedSnapshot().isEmpty()) {
                    int max = getMaxStackSize(carriedItemId);
                    if (slotItem[1] < max) {
                        setSlotItemFull(windowId, slot, carriedItemId, slotItem[1] + 1, ItemMeta.EMPTY);
                        carriedItemCount--;
                        if (carriedItemCount <= 0) {
                            carriedItemId = 0;
                            carriedItemCount = 0;
                        }
                        sendSlotUpdate(windowId, slot);
                        sendCarriedItem();
                    }
                }
            }
        }
        if ((openCraftingGrids.containsKey(windowId) && slot >= 1 && slot <= 9)
                || (windowId == 0 && slot >= 1 && slot <= 4)) {
            updateCraftingResult(windowId);
        }
        // Bug39/40/41 诊断: 玩家 2x2 合成格的点击/放置/回显全链路日志(定位"归零"根因用)
        if (windowId == 0 && slot >= 1 && slot <= 4) {
            System.out.println("[2x2诊断] click slot=" + slot + " button=" + button
                + " -> id=" + data.inventoryIds[slot] + " cnt=" + data.inventoryCounts[slot]
                + " carried=" + carriedItemId + "x" + carriedItemCount);
        }
    }

    /** 取走处理器结果槽(铁砧/锻造台/切石机/砂轮): 把输出放入光标并消耗输入, 防止复制。 */
    private void takeProcessorResult(int windowId, int button) {
        boolean isAnvil = openAnvil.containsKey(windowId);
        boolean isSmithing = openSmithing.containsKey(windowId);
        boolean isStonecutter = openStonecutters.containsKey(windowId);
        boolean isGrindstone = openGrindstones.containsKey(windowId);
        int resultSlot;
        if (isAnvil || isGrindstone) resultSlot = 2;
        else if (isSmithing) resultSlot = 3; // #19 锻造台结果槽=3
        else if (isStonecutter) resultSlot = 1;
        else return;
        int[] out = getSlotItem(windowId, resultSlot);
        if (out[1] <= 0) return;
        // Bug4/33: 必须在清结果槽之前读出组件(新语义下清槽会同时清组件)
        ItemMeta outMeta = readSlotMeta(windowId, resultSlot);
        // Bug36: 经验校验/扣费必须在 setSlotItem 之前完成 —— setSlotItem 会触发
        // recomputeAnvil 重算并覆盖 ad.cost; 且原实现从不校验经验不足, 生存模式可白拿。
        int anvilCost = 0;
        if (isAnvil) {
            ContainerStore.AnvilData adPre = getAnvilData(windowId);
            if (adPre != null) anvilCost = adPre.cost;
            if (anvilCost > 0 && gameMode == 0 && data.xpLevel < anvilCost) {
                sendFeedback("经验等级不足, 无法从铁砧取出(需要 " + anvilCost + " 级)", "red");
                return;
            }
        }
        if (carriedItemCount > 0 && carriedItemId != out[0]) return; // 光标物品不匹配, 不拿
        if (carriedItemCount > 0 && !outMeta.isEmpty()) return; // 带组件结果不能并入已有堆
        int max = getMaxStackSize(out[0]);
        int remaining = out[1];
        boolean mergedIntoCursor = false;
        if (carriedItemCount > 0) {
            int can = Math.min(max - carriedItemCount, remaining);
            if (can <= 0) return;
            carriedItemCount += can;
            remaining -= can;
            mergedIntoCursor = true;
        } else {
            carriedItemId = out[0];
            carriedItemCount = remaining;
            loadCarriedFrom(outMeta);
            remaining = 0;
        }
        setSlotItem(windowId, resultSlot, remaining > 0 ? out[0] : 0, remaining);
        sendSlotUpdate(windowId, resultSlot);

        if (isAnvil) {
            ContainerStore.AnvilData ad = getAnvilData(windowId);
            if (ad != null) {
                if (anvilCost > 0 && !mergedIntoCursor && gameMode == 0) spendXpLevels(anvilCost);
                // 合并后的附魔/改名/耐久/药水已在上面经 outMeta 转移到光标
                ad.leftEnchants.clear();
                ad.rightEnchants.clear();
                ad.leftDamage = 0;
                ad.rightDamage = 0;
                ad.outDamage = 0;
                ad.outPotion = null;
                ad.leftPotion = null;
                ad.rightPotion = null;
                ad.outName = null;
                ad.outId = 0;
                ad.outCount = 0;
                ad.leftName = null;
                ad.rightName = null;
                ad.rename = "";
                ad.slots[0] = 0; ad.slots[1] = 0; ad.slots[2] = 0; ad.slots[3] = 0; ad.version++;
                recomputeAnvil(windowId);
            }
        } else if (isSmithing) {
            ContainerStore.SmithingData sd = getSmithingData(windowId);
            if (sd != null) {
                // #19 纹饰: 若为 trim 合成, 记录 trim 组件到光标物品(原版 DataComponents.TRIM)。
                // 注意 id 0 是合法值(amethyst/bolt), 哨兵为 -1。
                if (!mergedIntoCursor && sd.outTrimMaterial >= 0 && sd.outTrimPattern >= 0) {
                    carriedTrimMaterial = sd.outTrimMaterial;
                    carriedTrimPattern = sd.outTrimPattern;
                }
                // Bug45 修复: 原版每次锻造只消耗模板/基础/附加各 1 个, 剩余留在槽内;
                // 曾整槽清空 -> 放一组只出一件还吞掉整组。
                consumeOne(sd, 0);
                consumeOne(sd, 1);
                consumeOne(sd, 2);
                sd.version++;
                recomputeSmithing(windowId);
            }
        } else if (isStonecutter) {
            ContainerStore.StonecutterData sd = getStonecutterData(windowId);
            if (sd != null) {
                // 切石机产物必为干净物品: 显式清空光标残留组件
                if (!mergedIntoCursor) clearCarriedMeta();
                if (sd.slots[1] > 0) sd.slots[1]--;
                if (sd.slots[1] <= 0) { sd.slots[0] = 0; sd.slots[1] = 0; writeContMeta(sd.meta, 0, null); }
                writeContMeta(sd.meta, 1, null);
                sd.version++;
                recomputeStonecutter(windowId);
            }
        } else if (isGrindstone) {
            ContainerStore.GrindstoneData gd = getGrindstoneData(windowId);
            if (gd != null) {
                // 磨石结果必为去附魔/去改名的干净物品: 显式清空光标元数据,
                // 防止上一操作残留的附魔被错误带到结果上。
                if (!mergedIntoCursor) clearCarriedMeta();
                if (gd.slots[1] > 0) gd.slots[1]--;
                if (gd.slots[1] <= 0) { gd.slots[0] = 0; gd.slots[1] = 0; writeContMeta(gd.meta, 0, null); }
                if (gd.slots[3] > 0) gd.slots[3]--;
                if (gd.slots[3] <= 0) { gd.slots[2] = 0; gd.slots[3] = 0; writeContMeta(gd.meta, 1, null); }
                writeContMeta(gd.meta, 2, null);
                gd.version++;
                recomputeGrindstone(windowId);
            }
        }
        sendCarriedItem();
    }

    /** Bug45: 锻造台输入槽消耗 1 个, 耗尽才清槽(组件随槽清空)。 */
    private static void consumeOne(ContainerStore.SmithingData sd, int slot) {
        int cnt = sd.slots[slot * 2 + 1];
        if (cnt <= 0) return;
        cnt--;
        sd.slots[slot * 2 + 1] = cnt;
        if (cnt <= 0) {
            sd.slots[slot * 2] = 0;
            writeContMeta(sd.meta, slot, null);
        }
    }

    private void placeOneItem(int windowId, int slot) {
        int[] slotItem = getSlotItem(windowId, slot);
        boolean placed = false;
        if (slotItem[1] <= 0) {
            boolean lastOne = carriedItemCount == 1;
            setSlotItemFull(windowId, slot, carriedItemId, 1, lastOne ? carriedSnapshot() : ItemMeta.EMPTY);
            if (lastOne) clearCarriedMeta();
            carriedItemCount--;
            if (carriedItemCount <= 0) { carriedItemId = 0; carriedItemCount = 0; }
            sendSlotUpdate(windowId, slot);
            sendCarriedItem();
            placed = true;
        } else if (slotItem[0] == carriedItemId && slotItem[1] < getMaxStackSize(carriedItemId)
                && readSlotMeta(windowId, slot).isEmpty() && carriedSnapshot().isEmpty()) {
            setSlotItemFull(windowId, slot, carriedItemId, slotItem[1] + 1, ItemMeta.EMPTY);
            carriedItemCount--;
            if (carriedItemCount <= 0) { carriedItemId = 0; carriedItemCount = 0; }
            sendSlotUpdate(windowId, slot);
            sendCarriedItem();
            placed = true;
        }
        if (placed && (windowId == 0 || openCraftingGrids.containsKey(windowId))
                && slot != 0) {
            updateCraftingResult(windowId);
        }
    }

    /** 数字键交换等: 把点击槽与玩家快捷栏槽 button(0-8) 互换; button==40 表示副手。 */
    private void handleSwapClick(int windowId, int slot, int button) {
        if (button == 40) {
            // 副手键(F): 与副手(槽 45)交换; 无悬停槽时与当前选中快捷栏交换
            int other = (slot < 0) ? 36 + heldItemSlot : slot;
            if ((windowId == 0 && (other < 0 || other >= 46)) || (windowId != 0 && slot < 0)) return;
            int[] a = getSlotItem(windowId, other);
            ItemMeta ma = readSlotMeta(windowId, other);
            int[] b = getSlotItem(0, 45);
            ItemMeta mb = readSlotMeta(0, 45);
            setSlotItemFull(windowId, other, b[0], b[1], mb);
            setSlotItemFull(0, 45, a[0], a[1], ma);
            sendSlotUpdate(windowId, other);
            sendSlotUpdate(0, 45);
            return;
        }
        if (button < 0 || button > 8 || slot < 0) return;
        int[] a = getSlotItem(windowId, slot);
        ItemMeta ma = readSlotMeta(windowId, slot);
        // 数字键 1-9 对应快捷栏槽 36-44 (0-8 是合成格, 不是快捷栏)
        int[] b = getSlotItem(0, 36 + button);
        ItemMeta mb = readSlotMeta(0, 36 + button);
        setSlotItemFull(windowId, slot, b[0], b[1], mb);
        setSlotItemFull(0, 36 + button, a[0], a[1], ma);
        sendSlotUpdate(windowId, slot);
        sendSlotUpdate(0, 36 + button);
    }

    /** 创造模式克隆: 把点击槽物品完整复制到光标(含组件, 不消耗原物品)。 */
    private void handleCloneClick(int windowId, int slot) {
        if (gameMode != 1 || slot < 0) return;
        int[] item = getSlotItem(windowId, slot);
        if (item[1] <= 0) return;
        carriedItemId = item[0];
        // Bug36: 创造中键按原版复制满组(原复制当前数量 -> 1 个物品中键只得 1 个)
        carriedItemCount = getMaxStackSize(item[0]);
        loadCarriedFrom(readSlotMeta(windowId, slot));
        sendCarriedItem();
    }

    /** Q 键丢出: 从点击槽丢 1(button==0) 或全堆(button==1)。带组件物品整组丢(原版堆叠为 1)。 */
    private void handleThrowClick(int windowId, int slot, int button) {
        if (slot < 0) return;
        int[] item = getSlotItem(windowId, slot);
        if (item[1] <= 0) return;
        ItemMeta m = readSlotMeta(windowId, slot);
        int drop = (button == 1 || !m.isEmpty()) ? item[1] : Math.min(1, item[1]);
        dropItemInFront(item[0], drop, m);
        int remain = item[1] - drop;
        setSlotItem(windowId, slot, remain > 0 ? item[0] : 0, remain);
        sendSlotUpdate(windowId, slot);
    }

    /** 双击收集: 把窗口内同类物品收到光标(不越堆叠上限)。 */
    private void handleDoubleClickCollect(int windowId) {
        if (carriedItemCount <= 0 || carriedItemId <= 0) return;
        int max = getMaxStackSize(carriedItemId);
        int limit = windowId == 0 ? 46 : 64;
        for (int i = 0; i < limit && carriedItemCount < max; i++) {
            // #7 修复: 处理器结果槽不收(双击收集会绕过 takeProcessorResult 无限刷物品)。
            if (openAnvil.containsKey(windowId) && i == 2) continue;
            if (openSmithing.containsKey(windowId) && i == 3) continue;
            if (openStonecutters.containsKey(windowId) && i == 1) continue;
            if (openGrindstones.containsKey(windowId) && i == 2) continue;
            if (i == 0 && (windowId == 0 || openCraftingGrids.containsKey(windowId)
                    || openMerchants.containsKey(windowId))) continue; // 结果槽不收
            int[] si;
            try {
                si = getSlotItem(windowId, i);
            } catch (Exception e) {
                continue;
            }
            if (si[1] > 0 && si[0] == carriedItemId && readSlotMeta(windowId, i).isEmpty()) {
                int can = Math.min(max - carriedItemCount, si[1]);
                carriedItemCount += can;
                int remain = si[1] - can;
                setSlotItem(windowId, i, remain > 0 ? si[0] : 0, remain);
                sendSlotUpdate(windowId, i);
            }
        }
        sendCarriedItem();
    }

    /** 拖拽合成(QUICK_CRAFT): 收集经过的槽, 结束时分配光标物品。 */
    private final java.util.Set<Integer> dragSlots = new java.util.HashSet<>();
    private void handleDragClick(int windowId, int slot, int button) {
        // button: 0=左开始 1=左添加 2=左结束 4=右开始 5=右添加 6=右结束
        //         8=中开始 9=中添加 10=中结束 (创造模式单件分发)
        if (button == 0 || button == 4 || button == 8) {
            dragSlots.clear();
        } else if (button == 1 || button == 5 || button == 9) {
            if (slot >= 0) dragSlots.add(slot);
        } else if (button == 2 || button == 6 || button == 10) {
            if (carriedItemCount <= 0) { dragSlots.clear(); return; }
            int total = dragSlots.size();
            if (total == 0) { dragSlots.clear(); return; }
            // Bug4/33: 光标物品的组件只随第一格放置(带组件物品堆叠为 1, 实际只会放一格)
            ItemMeta dragMeta = carriedSnapshot();
            boolean metaLeft = !dragMeta.isEmpty();
            if (button == 2) {
                // Bug36: 原版左拖均分后余数从第一格起逐格 +1(原整除丢弃余数)
                int per = carriedItemCount / total;
                int extra = carriedItemCount % total;
                int left = carriedItemCount;
                for (int s : dragSlots) {
                    if (left <= 0) break;
                    int give = Math.min(per + (extra > 0 ? 1 : 0), left);
                    if (extra > 0) extra--;
                    if (give <= 0) continue;
                    int[] si = getSlotItem(windowId, s);
                    if (si[1] <= 0) {
                        setSlotItemFull(windowId, s, carriedItemId, give, metaLeft ? dragMeta : ItemMeta.EMPTY);
                        metaLeft = false;
                        left -= give;
                        sendSlotUpdate(windowId, s);
                    } else if (si[0] == carriedItemId && readSlotMeta(windowId, s).isEmpty() && !metaLeft) {
                        int max = getMaxStackSize(carriedItemId);
                        int can = Math.min(max - si[1], give);
                        if (can > 0) { setSlotItemFull(windowId, s, carriedItemId, si[1] + can, ItemMeta.EMPTY); left -= can; sendSlotUpdate(windowId, s); }
                    }
                }
                carriedItemCount = left;
            } else {
                for (int s : dragSlots) {
                    int[] si = getSlotItem(windowId, s);
                    if (carriedItemCount <= 0) break;
                    if (si[1] <= 0) {
                        setSlotItemFull(windowId, s, carriedItemId, 1, metaLeft ? dragMeta : ItemMeta.EMPTY);
                        metaLeft = false;
                        carriedItemCount--;
                        sendSlotUpdate(windowId, s);
                    } else if (si[0] == carriedItemId && si[1] < getMaxStackSize(carriedItemId)
                            && readSlotMeta(windowId, s).isEmpty() && !metaLeft) {
                        setSlotItemFull(windowId, s, carriedItemId, si[1] + 1, ItemMeta.EMPTY);
                        carriedItemCount--;
                        sendSlotUpdate(windowId, s);
                    }
                }
            }
            if (carriedItemCount <= 0) { carriedItemId = 0; carriedItemCount = 0; clearCarriedMeta(); }
            sendCarriedItem();
            dragSlots.clear();
            // 拖拽放入合成格后刷新结果预览
            if (windowId == 0 || openCraftingGrids.containsKey(windowId)) {
                updateCraftingResult(windowId);
            }
        }
    }

    private void handleShiftClick(int windowId, int slot) {
        var pluginShift = openPluginMenus.get(windowId);
        if (pluginShift != null) {
            int size = pluginShift.getSize();
            if (slot >= 0 && slot < size) {
                // 菜单 → 玩家背包(入包失败部分掉落脚前, 不丢物品)
                int itemId = pluginShift.slotId(slot);
                int count = pluginShift.slotCount(slot);
                if (itemId > 0 && count > 0) {
                    com.CharunCore.server.plugin.api.ItemMeta pm = pluginShift.slotMeta(slot);
                    returnSlotToPlayer(itemId, count, toInternalMeta(pm));
                    pluginShift.clearSlotRaw(slot);
                }
            } else {
                // 玩家背包 → 菜单第一个空位
                int ps = pluginMenuPlayerSlot(pluginShift, slot);
                if (ps >= 0 && ps < 46 && data.inventoryIds[ps] > 0 && data.inventoryCounts[ps] > 0) {
                    int itemId = data.inventoryIds[ps];
                    int count = data.inventoryCounts[ps];
                    int target = pluginShift.firstEmpty();
                    if (target >= 0) {
                        ItemMeta sm = playerSlotMeta(ps);
                        pluginShift.writeSlotRaw(target, itemId, count, fromInternalMeta(sm));
                        data.inventoryIds[ps] = 0;
                        data.inventoryCounts[ps] = 0;
                        writePlayerSlotMeta(ps, null);
                        sendSlotUpdateRaw(0, ps, 0, 0);
                    }
                }
            }
            sendPluginMenuContent(windowId, pluginShift);
            return;
        }
        if (openCraftingGrids.containsKey(windowId) && slot == 0) {
            craftAllFromResult(windowId);
            return;
        }
        if (windowId == 0 && slot == 0) {
            craftAllFromPlayerGrid();
            return;
        }
        // #7 修复: 处理器结果槽 shift 点击必须走 takeProcessorResult(消耗输入), 否则直接移走
        // 结果槽物品而不扣输入 -> 无限刷物品(铁砧/锻造台/切石机/砂轮/磨石)。
        if (openAnvil.containsKey(windowId) && slot == 2) { takeProcessorResult(windowId, 0); return; }
        if (openSmithing.containsKey(windowId) && slot == 3) { takeProcessorResult(windowId, 0); return; }
        if (openStonecutters.containsKey(windowId) && slot == 1) { takeProcessorResult(windowId, 0); return; }
        if (openGrindstones.containsKey(windowId) && slot == 2) { takeProcessorResult(windowId, 0); return; }
        if (openMerchants.containsKey(windowId) && slot == 0) {
            // 快捷栏(shift)点击结果槽 = 重复交易直至背包满或次数用尽
            if (carriedItemCount <= 0) {
                int guard = 0;
                while (guard++ < 64 && executeTrade(windowId, 0)) { /* 循环至无法成交 */ }
            }
            return;
        }
        int[] item = getSlotItem(windowId, slot);
        if (item[1] <= 0) return;
        ItemMeta srcMeta = readSlotMeta(windowId, slot);

        if (windowId == 0) {
            // 原版: 快捷栏 <-> 主背包 双向
            int rangeStart = slot >= 36 ? 9 : 36;
            int rangeEnd = slot >= 36 ? 35 : 44;
            shiftMoveSlots(windowId, slot, item, srcMeta, rangeStart, rangeEnd, null);
        } else {
            // 容器区 <-> 玩家区 双向搬运; 目标区间按窗口类型区分。
            int containerCount;           // 容器槽总数(0..containerCount-1)
            int[] forbidden = null;       // 结果槽不可作为目标
            if (openChests.containsKey(windowId) || openEnderChests.containsKey(windowId)) {
                containerCount = 27;
            } else if (openCraftingGrids.containsKey(windowId)) {
                // 工作台布局: 0=结果, 1-9=网格, 10-45=玩家区
                if (slot >= 1 && slot <= 9) {
                    shiftMoveSlots(windowId, slot, item, srcMeta, 10, 45, new int[]{0});
                } else {
                    shiftMoveSlots(windowId, slot, item, srcMeta, 1, 9, new int[]{0});
                }
                return;
            } else if (openFurnaces.containsKey(windowId)) {
                containerCount = 3;
            } else if (openHoppers.containsKey(windowId)) {
                containerCount = 5;
            } else if (openDispensers.containsKey(windowId)) {
                containerCount = 9;
            } else if (openStonecutters.containsKey(windowId)) {
                containerCount = 2; forbidden = new int[]{1};
            } else if (openGrindstones.containsKey(windowId)) {
                containerCount = 3; forbidden = new int[]{2};
            } else if (openSmithing.containsKey(windowId)) {
                containerCount = 4; forbidden = new int[]{3};
            } else if (openEnchanting.containsKey(windowId)) {
                containerCount = 2;
            } else if (openBeacons.containsKey(windowId)) {
                containerCount = 1;
            } else if (openAnvil.containsKey(windowId)) {
                containerCount = 3; forbidden = new int[]{2};
            } else if (openBrewing.containsKey(windowId)) {
                containerCount = 5;
            } else if (openMerchants.containsKey(windowId)) {
                containerCount = 3; forbidden = new int[]{0};
            } else {
                return;
            }
            if (slot < containerCount) {
                // 熔炉: 从容器区进背包; 从背包区进熔炉需按物品选输入/燃料槽(下方处理)
                shiftMoveSlots(windowId, slot, item, srcMeta, containerCount, containerCount + 35, forbidden);
            } else {
                if (openFurnaces.containsKey(windowId)) {
                    String fn = BlockManager.itemIdToName(item[0]);
                    boolean isFuel = fn != null && SmeltingSystem.getFuelBurnTime(fn) > 0;
                    int target = isFuel ? 1 : 0;
                    shiftMoveSlots(windowId, slot, item, srcMeta, target, target, forbidden);
                } else {
                    shiftMoveSlots(windowId, slot, item, srcMeta, 0, containerCount - 1, forbidden);
                }
            }
        }
    }

    /** Bug4/33: shift 移动核心。优先并入同 id 未满堆(仅双方均无组件), 否则放空槽; 组件随物品走。 */
    private void shiftMoveSlots(int windowId, int src, int[] item, ItemMeta srcMeta,
                                int start, int end, int[] forbiddenTargets) {
        boolean hasMeta = !srcMeta.isEmpty();
        if (!hasMeta) {
            for (int i = start; i <= end; i++) {
                if (i == src || isForbiddenTarget(forbiddenTargets, i)) continue;
                int[] t = getSlotItem(windowId, i);
                if (t[0] == item[0] && t[1] > 0 && t[1] < getMaxStackSize(item[0])
                        && readSlotMeta(windowId, i).isEmpty()) {
                    int can = Math.min(getMaxStackSize(item[0]) - t[1], item[1]);
                    setSlotItemFull(windowId, i, item[0], t[1] + can, ItemMeta.EMPTY);
                    int remain = item[1] - can;
                    setSlotItem(windowId, src, remain > 0 ? item[0] : 0, remain);
                    sendSlotUpdate(windowId, src);
                    sendSlotUpdate(windowId, i);
                    return;
                }
            }
        }
        for (int i = start; i <= end; i++) {
            if (i == src || isForbiddenTarget(forbiddenTargets, i)) continue;
            int[] t = getSlotItem(windowId, i);
            if (t[1] <= 0) {
                setSlotItemFull(windowId, i, item[0], item[1], srcMeta);
                setSlotItem(windowId, src, 0, 0);
                sendSlotUpdate(windowId, src);
                sendSlotUpdate(windowId, i);
                return;
            }
        }
    }

    private static boolean isForbiddenTarget(int[] arr, int v) {
        if (arr == null) return false;
        for (int x : arr) if (x == v) return true;
        return false;
    }

    private void updateCraftingResult(int windowId) {
        String[] grid;
        if (windowId == 0) {
            grid = getPlayerCraftingGrid3x3();
        } else if (openCraftingGrids.containsKey(windowId)) {
            grid = openCraftingGrids.get(windowId);
        } else {
            return;
        }
        RecipeRegistry.Recipe result =
            CraftingSystem.matchRecipe(grid);
        if (result != null) {
            int itemId = result.resultItemId;
            sendSlotUpdateRaw(windowId, 0, itemId, result.resultCount);
        } else {
            sendSlotUpdateRaw(windowId, 0, 0, 0);
        }
    }

    private void handleCraftingResultClick(int windowId, int button) {
        if (!openCraftingGrids.containsKey(windowId)) return;
        String[] grid = openCraftingGrids.get(windowId);
        RecipeRegistry.Recipe result =
            CraftingSystem.matchRecipe(grid);
        if (result == null) return;

        int resultItemId = result.resultItemId;
        if (resultItemId <= 0) return;
        AdvancementManager.onCraftItem(this,
            BlockManager.itemIdToName(resultItemId));
        StatisticsManager.add(this, "crafted", BlockManager.itemIdToName(resultItemId), result.resultCount);

        if (carriedItemCount <= 0) {
            carriedItemId = resultItemId;
            carriedItemCount = result.resultCount;
            consumeCraftingGrid(windowId, grid);
            sendCarriedItem();
            for (int i = 1; i <= 9; i++) sendSlotUpdate(windowId, i);
            updateCraftingResult(windowId);
        } else if (carriedItemId == resultItemId) {
            int max = getMaxStackSize(resultItemId);
            if (carriedItemCount + result.resultCount <= max) {
                carriedItemCount += result.resultCount;
                consumeCraftingGrid(windowId, grid);
                sendCarriedItem();
                for (int i = 1; i <= 9; i++) sendSlotUpdate(windowId, i);
                updateCraftingResult(windowId);
            }
        }
    }

    /** Shift 点击结果槽: 反复合成直到原料耗尽或背包放不下 */
    private void craftAllFromResult(int windowId) {
        String[] grid = openCraftingGrids.get(windowId);
        if (grid == null) return;
        boolean any = false;
        for (int guard = 0; guard < 512; guard++) {
            RecipeRegistry.Recipe r =
                CraftingSystem.matchRecipe(grid);
            if (r == null) break;
            int rid = r.resultItemId;
            if (rid <= 0 || !hasInventorySpace(rid, r.resultCount)) break;
            giveItem(rid, r.resultCount);
            consumeCraftingGrid(windowId, grid);
            any = true;
        }
        if (!any) return;
        for (int i = 1; i <= 9; i++) sendSlotUpdate(windowId, i);
        for (int s = 10; s <= 45; s++) sendSlotUpdate(windowId, s);
        updateCraftingResult(windowId);
    }

    private boolean hasInventorySpace(int itemId, int count) {
        int max = getMaxStackSize(itemId);
        if (max <= 0) max = 64;
        for (int i : PICKUP_SLOT_ORDER) {
            if (data.inventoryIds[i] == 0 || data.inventoryCounts[i] == 0) return true;
            if (data.inventoryIds[i] == itemId && data.inventoryCounts[i] + count <= max) return true;
        }
        return false;
    }

    /** 在背包(槽9-44)找配方单元匹配的物品：cell 是 tag 时按 tag 全集匹配（防只认代表物品而失败）。 */
    private int findCraftingSrc(String cell) {
        java.util.Set<String> tagItems = CraftingSystem.getTagItems(cell);
        for (int s = 9; s <= 44; s++) {
            if (data.inventoryIds[s] <= 0 || data.inventoryCounts[s] <= 0) continue;
            String name = BlockManager.itemIdToName(data.inventoryIds[s]);
            if (name == null) continue;
            if (tagItems != null) { if (tagItems.contains(name)) return s; }
            else if (cell.equals(name)) return s;
        }
        return -1;
    }

    /** 新配方引擎: 按 itemId 精确找背包槽 */
    private int findCraftingSrcById(int itemId) {
        if (itemId <= 0) return -1;
        for (int s = 9; s <= 44; s++) {
            if (data.inventoryIds[s] == itemId && data.inventoryCounts[s] > 0) return s;
        }
        return -1;
    }

    /** 合成消耗 milk_bucket/bowl 时返还空桶/碗（原版行为，曾丢失副产品） */
    private void refundByproduct(String consumedName, int consume) {
        if (consumedName == null || consume <= 0) return;
        if (consumedName.equals("milk_bucket")) {
            int bid = BlockManager.getItemIdByName("bucket");
            if (bid > 0) giveItem(bid, consume);
        } else if (consumedName.equals("bowl")) {
            int bid = BlockManager.getItemIdByName("bowl");
            if (bid > 0) giveItem(bid, consume);
        }
    }

    /** 合成一次只消耗配方所需的一格 1 个, 多余摆放的格子不扣(原版行为, 防误扣背包) */
    private void consumeCraftingGrid(int windowId, String[] grid) {
        RecipeRegistry.Recipe recipe =
            CraftingSystem.matchRecipe(grid);
        int[] map = CraftingSystem.consumeMap(recipe, grid);
        int[] counts = craftingCounts(windowId);
        for (int i = 0; i < 9; i++) {
            int consume = (map != null) ? map[i] : (grid[i] != null ? 1 : 0);
            if (consume <= 0) continue;
            refundByproduct(grid[i], consume); // 桶/碗返还
            int have = (counts[i] <= 0 ? 1 : counts[i]);
            int left = have - consume;
            if (left <= 0) {
                grid[i] = null;
                counts[i] = 0;
            } else {
                counts[i] = left;
            }
        }
    }

    private String getPlayerCraftingItemName(int slot) {
        int id = data.inventoryIds[slot];
        return id > 0 ? BlockManager.itemIdToName(id) : null;
    }

    private String[] getPlayerCraftingGrid3x3() {
        String[] grid = new String[9];
        grid[0] = getPlayerCraftingItemName(1);
        grid[1] = getPlayerCraftingItemName(2);
        grid[3] = getPlayerCraftingItemName(3);
        grid[4] = getPlayerCraftingItemName(4);
        return grid;
    }

    /** 玩家背包 2×2 合成格: shift 点击结果槽 → 反复合成直到原料耗尽或背包放不下。 */
    private void craftAllFromPlayerGrid() {
        boolean any = false;
        int craftedId = 0;
        for (int guard = 0; guard < 512; guard++) {
            String[] grid = getPlayerCraftingGrid3x3();
            RecipeRegistry.Recipe r =
                CraftingSystem.matchRecipe(grid);
            if (r == null) break;
            if (r.resultItemId <= 0 || !hasInventorySpace(r.resultItemId, r.resultCount)) break;
            giveItem(r.resultItemId, r.resultCount);
            consumePlayerCraftingGrid();
            craftedId = r.resultItemId;
            any = true;
        }
        if (any) {
            AdvancementManager.onCraftItem(this,
                BlockManager.itemIdToName(craftedId));
            for (int i = 1; i <= 4; i++) sendSlotUpdate(0, i);
            updateCraftingResult(0);
            sendCarriedItem();
        }
    }

    private void handlePlayerCraftingResultClick() {
        String[] grid = getPlayerCraftingGrid3x3();
        RecipeRegistry.Recipe result =
            CraftingSystem.matchRecipe(grid);
        if (result == null) return;
        int resultItemId = result.resultItemId;
        if (resultItemId <= 0) return;
        AdvancementManager.onCraftItem(this,
            BlockManager.itemIdToName(resultItemId));
        StatisticsManager.add(this, "crafted", BlockManager.itemIdToName(resultItemId), result.resultCount);
        if (carriedItemCount <= 0) {
            carriedItemId = resultItemId;
            carriedItemCount = result.resultCount;
            consumePlayerCraftingGrid();
            sendCarriedItem();
            sendSlotUpdate(0, 1); sendSlotUpdate(0, 2);
            sendSlotUpdate(0, 3); sendSlotUpdate(0, 4);
            updateCraftingResult(0);
        } else if (carriedItemId == resultItemId) {
            int max = getMaxStackSize(resultItemId);
            if (carriedItemCount + result.resultCount <= max) {
                carriedItemCount += result.resultCount;
                consumePlayerCraftingGrid();
                sendCarriedItem();
                sendSlotUpdate(0, 1); sendSlotUpdate(0, 2);
                sendSlotUpdate(0, 3); sendSlotUpdate(0, 4);
                updateCraftingResult(0);
            }
        }
    }

    private void consumePlayerCraftingGrid() {
        String[] grid = getPlayerCraftingGrid3x3();
        RecipeRegistry.Recipe recipe =
            CraftingSystem.matchRecipe(grid);
        int[] map = CraftingSystem.consumeMap(recipe, grid);
        // 2×2 网格对应 inventory 槽 1,2,3,4（即 grid 的 0,1,3,4）
        int[] gridToInv = new int[]{1, 2, -1, 3, 4, -1, -1, -1, -1};
        for (int i = 0; i < 9; i++) {
            int consume = (map != null) ? map[i] : (grid[i] != null ? 1 : 0);
            if (consume <= 0) continue;
            refundByproduct(grid[i], consume); // 桶/碗返还
            int inv = gridToInv[i];
            if (inv < 0) continue;
            int have = data.inventoryCounts[inv];
            int left = have - consume;
            if (left <= 0) {
                data.inventoryIds[inv] = 0;
                data.inventoryCounts[inv] = 0;
            } else {
                data.inventoryCounts[inv] = left;
            }
        }
    }

    /**
     * 扫描玩家碰撞箱(AABB)覆盖范围内的传送门方块。
     * 玩家碰撞箱: x/z 各 ±0.3, y 从脚到头(~1.8)。
     * 站在传送门内时中心列是空气, 传送门方块在侧列, 故必须扫整圈。
     */
    private String scanPortalBlock() {
        int minX = (int) Math.floor(x - 0.3), maxX = (int) Math.floor(x + 0.3);
        int minZ = (int) Math.floor(z - 0.3), maxZ = (int) Math.floor(z + 0.3);
        int minY = (int) Math.floor(y) - 1, maxY = (int) Math.floor(y + 1.8); // 下探 1 格以覆盖"站在门上"的返回门
        for (int bx = minX; bx <= maxX; bx++) {
            for (int bz = minZ; bz <= maxZ; bz++) {
                for (int by = minY; by <= maxY; by++) {
                    String n = BlockStateHelper.getName(
                        WorldManager.getBlockState(this.currentDim, bx, by, bz));
                    if (n != null && (n.equals("nether_portal") || n.equals("end_portal")
                            || n.equals("end_gateway"))) {
                        return n;
                    }
                }
            }
        }
        return null;
    }

    public void tickSurvival() {
        if (isDead) return;

        // Bug44: 每 tick 泵送限量区块(生成在 IO 线程逐个做, 压缩/光照负载摊平),
        // 替代曾一次性倾泻全部视距区块造成的进服初期帧率剧烈波动。
        pumpChunkSends();

        if (invulnTicks > 0) {
            invulnTicks--;
            if (invulnTicks == 0) lastDamageAmount = 0.0f;
        }

        // 用 Math.floor 而非 (int): (int) 对负数向零取整, 负坐标/洞底玩家脚部 Y 会取到错误方块,
        // 导致"挖空区域走进去还受窒息"(eyeBlockY 落到上方实心方块)。
        int blockX = (int) Math.floor(x), blockY = (int) Math.floor(y), blockZ = (int) Math.floor(z);
        int feetBlock = WorldManager.getBlockState(this.currentDim,blockX, blockY, blockZ);
        String feetName = BlockStateHelper.getName(feetBlock);

        // 传送门对所有游戏模式生效(旁观除外), 不能因为创造模式提前 return
        if (gameMode != 3) {
            // 玩家站在传送门里时, 中心列(脚/头所在 XZ)是空气, 传送门方块在侧列(x±1/z±1)。
            // 因此必须扫描玩家碰撞箱(AABB)覆盖的所有方块, 而非只看脚/头单一方块。
            String portalName = scanPortalBlock();
            boolean inEndGateway = "end_gateway".equals(portalName);
            boolean inNetherPortal = "nether_portal".equals(portalName);
            boolean inEndPortal = "end_portal".equals(portalName);
            if (inEndGateway) {
                if (portalTimer >= 0) {
                    portalTimer = -100; // gateway 是瞬时传送, 用负计时器作冷却
                    teleportViaEndGateway(ctx);
                    return;
                }
                portalTimer++;
            } else if (inNetherPortal || inEndPortal) {
                portalTimer++;
                if (portalTimer >= 80) {
                    portalTimer = 0;
                    if (inNetherPortal) {
                        DimensionType dest =
                            this.currentDim == DimensionType.THE_NETHER
                                ? DimensionType.OVERWORLD
                                : DimensionType.THE_NETHER;
                        teleportToDimension(ctx, dest, true);
                    } else {
                        DimensionType dest =
                            this.currentDim == DimensionType.THE_END
                                ? DimensionType.OVERWORLD
                                : DimensionType.THE_END;
                        teleportToDimension(ctx, dest, false);
                    }
                    return;
                }
            } else {
                if (portalTimer < 0) portalTimer++;
                else portalTimer = 0;
            }
        }

        // Bug5/8 修复: 容器进度同步/状态效果必须在游戏模式门控之前执行。
        // 曾放在 if (gameMode != 0) return 之后 -> 创造/冒险模式下熔炉火焰与烧炼箭头、
        // 酿造台进度、信标 buff、药水效果全部冻结(每刻同步从未发出)。
        // Bug59: 生存挖掘裂纹阶段广播
        tickDigProgress();
        // Bug52: 延迟进食/饮用结算
        tickEating();
        syncOpenFurnaces();
        syncOpenBrewings();
        syncAttributesIfNeeded();
        tickEffects();
        // #11: 信标 buff 持续(每 ~4 秒重施一次, 原版范围内持续生效)。
        beaconTickTimer++;
        if (beaconTickTimer >= 80) {
            beaconTickTimer = 0;
            for (var beh : ContainerStore.beaconEntries()) {
                ContainerStore.Pos bp = beh.getKey();
                ContainerStore.BeaconData bd = beh.getValue();
                if (bp == null || bd == null || bd.levels < 1 || bd.primary < 0) continue;
                if (bp.dim() != this.currentDim) continue;
                int dx = (int) (this.x - bp.x()), dz = (int) (this.z - bp.z());
                int range = 10 + bd.levels * 10;
                if (dx * dx + dz * dz <= range * range) {
                    applyBeaconEffectToPlayers(bp);
                }
            }
        }

        if (gameMode != 0) return;

        double dxMove = x - lastX;
        double dzMove = z - lastZ;
        double dyMove = y - lastY;
        double horiz = Math.sqrt(dxMove * dxMove + dzMove * dzMove);
        // 游泳消耗(原版 0.01/tick): 玩家在水中水平移动时额外消耗
        boolean swimmingNow = "water".equals(BlockStateHelper.getName(
            WorldManager.getBlockState(this.currentDim, (int) x, (int) y, (int) z)));
        if (onGround && horiz > 0.0001) {
            addExhaustion((float) (horiz * (sprinting ? 0.1 : 0.01)));
        } else if (swimmingNow && horiz > 0.0001) {
            addExhaustion(0.01f);
        }
        if (!onGround && dyMove > 0.05 && wasOnGround) {
            addExhaustion(sprinting ? 0.2f : 0.05f);
        }
        wasOnGround = onGround;
        lastX = x;
        lastZ = z;

        // 溺水判定必须与客户端一致: 用"眼睛高度"所在方块, 而非脚部上方 1 格。
        // 【修复】原用 blockY+1(脚部上方 1 格): 玩家踩水(眼睛刚出水面)时客户端氧气条满,
        // 服务端却按水中持续扣血 → "满气泡被淹死"。(窒息判定 L4174 已用 y+1.62, 此处对齐)
        int eyeY = (int) Math.floor(y + 1.62);
        String headName = BlockStateHelper.getName(
            WorldManager.getBlockState(this.currentDim, blockX, eyeY, blockZ));
        // 环境伤害约每 10 tick(0.5s)施加一次, 贴近原版节奏(岩浆/火约每 0.5s 一次), 由无敌帧限频。
        envDamageTimer++;
        boolean applyEnvSecond = (envDamageTimer >= 10);
        if (applyEnvSecond) envDamageTimer = 0;

        boolean fireImmune = hasEffect("fire_resistance");
        if ("water".equals(headName)) {
            if (hasEffect("water_breathing") || hasEffect("conduit_power")) {
                data.airTicks = 300;
            } else if (data.airTicks > 0) data.airTicks--;
            if (data.airTicks <= 0 && applyEnvSecond) {
                health -= 2.0f;
                this.lastDamageType = "drown";
                sendHealthUpdate();
            }
        } else if (!fireImmune && ("lava".equals(headName) || "lava".equals(feetName))) {
            if (applyEnvSecond) {
                health -= 4.0f;
                this.lastDamageType = "lava";
                sendHealthUpdate();
            }
        } else {
            data.airTicks = 300;
        }

        // 窒息判定: 只检查"眼睛所在方块"(y+1.62), 与原版一致。
        // 不能用 (int)y+1 —— 当脚部 Y 为小数时, 该格会落入胸口/脚部区域,
        // 把脚下的实心地面误判为头顶方块, 导致站在坑里也被窒息。
        int eyeBlockY = (int) Math.floor(y + 1.62);
        int eyeState = WorldManager.getBlockState(this.currentDim, blockX, eyeBlockY, blockZ);
        if (applyEnvSecond && eyeState != 0 && isSolidOpaque(eyeState)
                && !"water".equals(headName) && !"lava".equals(headName)) {
            health -= 1.0f;
            this.lastDamageType = "suffocate";
            sendHealthUpdate();
        }

        int blockBelow = WorldManager.getBlockState(this.currentDim,blockX, blockY - 1, blockZ);
        String belowName = BlockStateHelper.getName(blockBelow);

        // 岩浆/火: 即时灼伤 + 点燃(之后持续燃烧); 抗火时完全免疫
        boolean standingInFire = !fireImmune && ("fire".equals(feetName) || "fire".equals(belowName));
        boolean standingInLava = !fireImmune && ("lava".equals(feetName) || "lava".equals(belowName));
        // 水熄灭玩家身上的火: 站在水中或头部在水中立即清零 fireTicks(原版行为)。
        if (fireTicks > 0 && ("water".equals(feetName) || "water".equals(headName))) {
            fireTicks = 0;
            syncOnFire();
        }
        if (applyEnvSecond && (standingInFire || standingInLava)) {
            health -= 1.0f;
            this.lastDamageType = "inFire";
            sendHealthUpdate();
            int newFire = standingInLava ? 300 : 160;
            if (fireTicks < newFire) {
                boolean wasOff = fireTicks <= 0;
                fireTicks = newFire;
                if (wasOff) syncOnFire();
            }
        }

        // 岩浆块: 即时灼伤但不点燃(抗火免疫)
        if (applyEnvSecond && !fireImmune && "magma_block".equals(belowName)) {
            health -= 1.0f;
            this.lastDamageType = "inFire";
            sendHealthUpdate();
        }

        // 持续燃烧: 离开火/岩浆后仍按每秒灼伤(原版 fireTicks)
        if (fireTicks > 0 && !standingInFire && !standingInLava) {
            if (applyEnvSecond) {
                health -= 1.0f;
                this.lastDamageType = "onFire";
                sendHealthUpdate();
            }
            fireTicks--;
            if (fireTicks == 0) syncOnFire();
        }

        if (applyEnvSecond && ("cactus".equals(feetName) || "cactus".equals(belowName) ||
            "sweet_berry_bush".equals(feetName) || "campfire".equals(belowName) ||
            "soul_campfire".equals(belowName) || "wither_rose".equals(feetName))) {
            health -= 1.0f;
            if ("cactus".equals(feetName) || "cactus".equals(belowName)) this.lastDamageType = "cactus";
            else if ("wither_rose".equals(feetName)) this.lastDamageType = "witherRose";
            else this.lastDamageType = "sweetBerry";
            sendHealthUpdate();
        }

        if (y < -64) {
            health = 0;
            sendHealthUpdate();
        }

        tickFoodSystem();

        // 鞘翅：落地 / 入水 / 卸下鞘翅 → 停止滑翔；并据此抑制落地摔伤
        boolean elytraWasFlying = fallFlying;
        if (fallFlying) {
            String elytraFeet = BlockStateHelper.getName(
                WorldManager.getBlockState(this.currentDim, blockX, blockY, blockZ));
            boolean inWater = "water".equals(elytraFeet);
            if (onGround || inWater || !hasElytraEquipped()) stopElytra();
        }

        if (fallDistance > 3.0f && onGround && gameMode == 0 && !elytraWasFlying) {
            float damage = fallDistance - 3.0f;
            int blockBelowFall = WorldManager.getBlockState(this.currentDim,blockX, blockY - 1, blockZ);
            String belowFallName = BlockStateHelper.getName(blockBelowFall);
            if (!"water".equals(belowFallName) && !"hay_block".equals(belowFallName)
                && !"honey_block".equals(belowFallName) && !"slime_block".equals(belowFallName)) {
                // Bug42: 摔落伤害走 damagePlayer —— 曾直接 health-=damage 绕过全部减伤,
                // 摔落缓冲/保护附魔完全无效("附魔的工具和普通工具没差别"的组成部分)。
                damagePlayer(damage, "fall");
            }
            fallDistance = 0.0f;
        }

        if (!onGround && y < lastY) {
            fallDistance += (float)(lastY - y);
        }
        if (onGround) {
            fallDistance = 0.0f;
        }

        // soul_speed: 灵魂沙/灵魂土加速(架构限制: 客户端权威位移, 这里对实体速度做最佳近似)
        applySoulSpeed();

        if (health <= 0 && !isDead) {
            isDead = true;
            health = 0;
            sendHealthUpdate();
            sendDeathScreen(deathMessageFor(this.lastDamageType));
        }

        lastY = y;
    }

    /** 护甲槽位: 5=头盔 6=胸甲 7=护腿 8=靴子 */
    private static final int ARMOR_SLOT_FIRST = 5;
    private static final int ARMOR_SLOT_LAST = 8;

    private static int armorPointsOf(String n) {
        return switch (n) {
            case "leather_helmet", "leather_boots", "golden_boots", "chainmail_boots" -> 1;
            case "leather_leggings" -> 2;
            case "leather_chestplate", "golden_leggings" -> 3;
            case "chainmail_helmet", "iron_helmet", "golden_helmet",
                 "iron_boots", "turtle_helmet" -> 2;
            case "diamond_helmet", "netherite_helmet",
                 "diamond_boots", "netherite_boots" -> 3;
            case "chainmail_leggings" -> 4;
            case "chainmail_chestplate", "golden_chestplate", "iron_leggings" -> 5;
            case "iron_chestplate", "diamond_leggings", "netherite_leggings" -> 6;
            case "diamond_chestplate", "netherite_chestplate" -> 8;
            default -> 0;
        };
    }

    private static int armorToughnessOf(String n) {
        if (n.startsWith("diamond_")) return 2;
        if (n.startsWith("netherite_")) return 3;
        if (n.startsWith("turtle_")) return 2;
        return 0;
    }

    /** 伤害是否无视护甲。 */
    private static boolean bypassesArmor(String source) {
        if (source == null) return false;
        return switch (source) {
            case "void", "starve", "magic", "wither", "ender_pearl", "suffocate", "drown" -> true;
            default -> false;
        };
    }

    private boolean isBlockingFrom(double srcX, double srcZ) {
        if (!isBlocking) return false;
        double dx = srcX - this.x, dz = srcZ - this.z;
        double len = Math.hypot(dx, dz);
        if (len < 1e-6) return true;
        dx /= len; dz /= len;
        double yawRad = Math.toRadians(this.yaw);
        double fx = -Math.sin(yawRad), fz = Math.cos(yawRad);
        return (fx * dx + fz * dz) > -0.5;
    }

    private int getShieldSlot() {
        int main = 36 + heldItemSlot;
        if (isShield(data.inventoryIds[main])) return main;
        if (isShield(data.inventoryIds[45])) return 45;
        return -1;
    }

    private static boolean isShield(int id) {
        String n = BlockManager.itemIdToName(id);
        return n != null && n.contains("shield");
    }

    private static boolean isDrinkablePotion(String itemName) {
        return itemName != null && (itemName.equals("potion") || itemName.equals("water_bottle")
                || itemName.equals("awkward_potion") || itemName.equals("splash_potion")
                || itemName.equals("lingering_potion"));
    }

    public void damagePlayer(float damage, String source) {
        damagePlayer(damage, source, Double.NaN, Double.NaN);
    }

    /** Bug42: 火矢点燃玩家(火抗药水免疫点燃, 与原版 setRemainingFire 一致)。 */
    public void ignite(int ticks) {
        if (gameMode == 1 || gameMode == 3 || hasEffect("fire_resistance")) return;
        if (ticks > fireTicks) {
            boolean wasOff = fireTicks <= 0;
            fireTicks = ticks;
            if (wasOff) syncOnFire();
        }
    }

    public void damagePlayer(float damage, String source, double srcX, double srcZ) {
        if (gameMode == 1 || gameMode == 3) return;
        if (isDead) return;
        if (godMode) return;

        // 盾牌格挡 (近战/弹射物/部分爆炸, 且攻击者在正面锥内); 无盾返回 -1 跳过扣耐久
        if (this.isBlocking && !Double.isNaN(srcX) && isBlockingFrom(srcX, srcZ)
                && ("player".equals(source) || "mob".equals(source)
                    || "projectile".equals(source) || "explosion".equals(source))) {
            int ss = getShieldSlot();
            if (ss >= 0) damageHeldItem(ss, 1);
            return;
        }

        if (damage <= 0) return;

        // 无敌帧: 同一次伤害窗口内只有更强的伤害才能穿透 (原版语义)
        if (invulnTicks > 0) {
            if (damage <= lastDamageAmount) return;
            damage -= lastDamageAmount;
        }

        // 吸收心: 先扣吸收, 剩余才伤血
        if (absorption > 0.0f) {
            float absorbed = Math.min(absorption, damage);
            absorption -= absorbed;
            damage -= absorbed;
            sendHealthUpdate();
            if (damage <= 0.0f) {
                this.lastDamageTime = System.currentTimeMillis();
                this.lastDamageAmount = absorbed;
                this.invulnTicks = 10;
                return;
            }
        }

        float armor = 0.0f, toughness = 0.0f;
        if (!bypassesArmor(source)) {
            for (int s = ARMOR_SLOT_FIRST; s <= ARMOR_SLOT_LAST; s++) {
                int aid = data.inventoryIds[s];
                if (aid <= 0 || data.inventoryCounts[s] <= 0) continue;
                String an = BlockManager.itemIdToName(aid);
                if (an == null) continue;
                if (an.startsWith("minecraft:")) an = an.substring(10);
                armor += armorPointsOf(an);
                toughness += armorToughnessOf(an);
            }
            if (armor > 0) {
                float reduce = Math.min(20.0f,
                    Math.max(armor / 5.0f, armor - damage / (2.0f + toughness / 4.0f)));
                damage *= (1.0f - reduce / 25.0f);
                for (int s = ARMOR_SLOT_FIRST; s <= ARMOR_SLOT_LAST; s++) {
                    if (data.inventoryIds[s] > 0) damageHeldItem(s, 1);
                }
            }
        }

        // 附魔保护 (protection / fire_protection / blast_protection / projectile_protection / feather_falling)
        if (!bypassesArmor(source) && !"void".equals(source)) {
            int prot = 0, fireProt = 0, blastProt = 0, projProt = 0, featherFall = 0;
            for (int s = ARMOR_SLOT_FIRST; s <= ARMOR_SLOT_LAST; s++) {
                if (data.inventoryIds[s] <= 0) continue;
                prot     += data.getSlotEnchant(s, BlockManager.getEnchantId("protection"));
                fireProt += data.getSlotEnchant(s, BlockManager.getEnchantId("fire_protection"));
                blastProt+= data.getSlotEnchant(s, BlockManager.getEnchantId("blast_protection"));
                projProt += data.getSlotEnchant(s, BlockManager.getEnchantId("projectile_protection"));
                // Bug42: 摔落缓冲按原版 EPF=3×等级 只对摔落伤害生效
                featherFall += data.getSlotEnchant(s, BlockManager.getEnchantId("feather_falling"));
            }
            int epf = prot;
            if ("lava".equals(source) || "fire".equals(source) || "onFire".equals(source)) epf += fireProt;
            if ("explosion".equals(source)) epf += blastProt;
            if ("projectile".equals(source)) epf += projProt;
            if ("fall".equals(source)) epf += featherFall * 3;
            if (epf > 0) {
                damage -= damage * Math.min(epf, 20) / 25.0f;
            }
        }

        this.health -= damage;
        this.lastDamageTime = System.currentTimeMillis();
        this.lastDamageType = source;
        this.lastDamageAmount = damage;
        this.invulnTicks = 10;
        sendHealthUpdate();

        for (NetworkHandler p : players.values()) {
            if (p.ctx == null || p.currentDim != this.currentDim) continue;
            p.sendPacket(p.ctx, 0x22, pb -> {
                pb.writeInt(this.eid);
                pb.writeByte(2);
            });
        }
        sendSoundAt("minecraft:entity.player.hurt", this.x, this.y, this.z, 1.0f, 1.0f);

        if (this.health <= 0 && !isDead) {
            isDead = true;
            health = 0;
            sendDeathScreen(deathMessageFor(source));
        }
    }

    public String deathMessageFor(String source) {
        String cause = source == null ? "unknown" : source;
        return switch (cause) {
            case "fall" -> username + " 摔死了";
            case "lava" -> username + " 试图在岩浆里游泳";
            case "fire", "onFire", "inFire" -> username + " 被烧死了";
            case "drown" -> username + " 淹死了";
            case "void" -> username + " 掉出了世界";
            case "explosion" -> username + " 被炸死了";
            case "starve" -> username + " 饿死了";
            case "cactus" -> username + " 被仙人掌扎死了";
            case "suffocate" -> username + " 在墙里窒息而死";
            case "witherRose" -> username + " 被凋零玫瑰杀死了";
            case "wither" -> username + " 被凋零杀死了";
            case "sweetBerry" -> username + " 被甜浆果刺死了";
            case "magic" -> username + " 被魔法杀死了";
            case "mob" -> username + " 被怪物杀死了";
            default -> username + " 死了";
        };
    }

    public void knockback(double kx, double kz, double ky) {
        // entity_velocity/set_entity_motion = 0x63 (服务端 jar GameProtocols 注册序确认;
        // 0x64=entity_equipment —— 曾误用 0x64 导致击退速度被客户端当装备数据静默吞掉)。
        // velocity 必须用 lpVec3 编码, 不能用 3×Short(旧格式), 否则客户端 lpVec3 解码器越界崩溃。
        sendPacket(ctx, 0x63, pb -> {
            pb.writeVarInt(this.eid);
            pb.writeLpVec3(kx, ky, kz);
        });
    }

    public void addExhaustion(float amount) {
        if (gameMode == 1 || gameMode == 3) return;
        data.exhaustion = Math.min(40.0f, data.exhaustion + amount);
    }

    private void tickFoodSystem() {
        if (gameMode == 1 || gameMode == 3) return;

        while (data.exhaustion >= 4.0f) {
            data.exhaustion -= 4.0f;
            if (data.saturation > 0.0f) {
                data.saturation = Math.max(0.0f, data.saturation - 1.0f);
            } else {
                var foodEvent = EVENTS.fire(new com.CharunCore.server.plugin.event.events.FoodLevelChangeEvent(
                        this, Math.max(0, data.food - 1)));
                if (foodEvent.isCancelled()) {
                    data.exhaustion = 0;
                    return;
                }
                data.food = Math.max(0, data.food - 1);
                sendHealthUpdate();
            }
        }

        // 自然恢复需 naturalRegeneration gamerule 为 true(原版默认 true)
        boolean naturalRegen = "true".equals(
            WorldManager.getGameRule("naturalRegeneration"));
        if (naturalRegen) {
            if (data.food >= 20 && data.saturation > 0.0f && health < 20.0f) {
                foodTickTimer++;
                if (foodTickTimer >= 10) {
                    float heal = Math.min(data.saturation, 6.0f) / 6.0f;
                    var regainEvent = EVENTS.fire(new com.CharunCore.server.plugin.event.events.EntityRegainHealthEvent(
                            this, Math.min(20.0f - health, heal)));
                    if (regainEvent.isCancelled()) {
                        foodTickTimer = 0;
                    } else {
                        health = Math.min(20.0f, health + heal);
                        addExhaustion(heal);
                        foodTickTimer = 0;
                        sendHealthUpdate();
                    }
                }
            } else if (data.food >= 18 && health < 20.0f) {
                foodTickTimer++;
                if (foodTickTimer >= 80) {
                    health = Math.min(20.0f, health + 1.0f);
                    addExhaustion(6.0f);
                    foodTickTimer = 0;
                    sendHealthUpdate();
                }
            } else {
                foodTickTimer = 0;
            }
        } else {
            foodTickTimer = 0;
        }

        // 饥饿伤害按难度分级(原版): 和平不扣血; 简单>=10; 普通>=1; 困难可降至 0(饿死)
        if (data.food <= 0) {
            starveTimer++;
            if (starveTimer >= 80) {
                starveTimer = 0;
                int diff = Main.difficulty;
                if (diff >= 1) { // 非和平
                    float floor = switch (diff) {
                        case 1 -> 10.0f; // easy
                        case 2 -> 1.0f;  // normal
                        default -> 0.0f; // hard
                    };
                    if (health > floor) {
                        health -= 1.0f;
                        sendHealthUpdate();
                    }
                }
            }
        } else {
            starveTimer = 0;
        }
    }

    private void syncOpenFurnaces() {
        for (var entry : openFurnaces.entrySet()) {
            int windowId = entry.getKey();
            ContainerStore.Pos pos = entry.getValue();
            ContainerStore.FurnaceData f =
                ContainerStore.peekFurnace(pos);
            if (f == null) continue;

            sendContainerProperty(windowId, 0, f.burnTime);
            sendContainerProperty(windowId, 1, f.burnTotal);
            sendContainerProperty(windowId, 2, f.cookTime);
            sendContainerProperty(windowId, 3, f.cookTotal);

            int known = containerSyncVersion.getOrDefault(windowId, -1);
            if (known != f.version) {
                containerSyncVersion.put(windowId, f.version);
                sendSlotUpdate(windowId, 0);
                sendSlotUpdate(windowId, 1);
                sendSlotUpdate(windowId, 2);
            }
        }
    }

    private void sendContainerProperty(int windowId, int property, int value) {
        sendPacket(ctx, 0x13, pb -> {
            pb.writeVarInt(windowId);
            pb.writeShort(property);
            pb.writeShort(value);
        });
    }

    private void sendProcessorContent(int windowId, int[] slots, int containerSlotCount) {
        sendPacket(ctx, 0x12, pb -> {
            pb.writeVarInt(windowId);
            pb.writeVarInt(0);
            pb.writeVarInt(containerSlotCount + 36);
            for (int i = 0; i < containerSlotCount; i++) {
                int id = slots[i * 2], cnt = slots[i * 2 + 1];
                // Bug8: 处理器容器槽(酿造台瓶中药水等)也带组件下发。
                // 曾一律 writeSlot(无 components) -> 立在酿造台里的药水在 UI 里永远显示
                // "不可合成的药水"(potion_contents 丢失)。
                if (id > 0 && cnt > 0) {
                    ItemMeta m = readSlotMeta(windowId, i);
                    if (m != null && !m.isEmpty()) writeStackWithMeta(pb, id, cnt, m);
                    else pb.writeSlot(id, cnt);
                } else {
                    pb.writeSlot(id, cnt);
                }
            }
            for (int ps = 9; ps <= 44; ps++) writePlayerSlot(pb, ps);
            writeCarriedSlot(pb);
        });
    }

    // ── 村民交易（merchant 容器 + trade_list 包）────────────────────────────
    private void openMerchant(MobEntity villager) {
        if (villager.villagerTrades == null) villager.initVillager();
        int windowId = nextWindowId();
        MerchantSession s = new MerchantSession(villager.id);
        openMerchants.put(windowId, s);
        merchantWindowId = windowId;
        org.cloudburstmc.nbt.NbtMap title = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", villager.profession == null ? "Villager" : villager.profession).build();
        sendPacket(ctx, 0x39, pb -> {
            pb.writeVarInt(windowId);
            pb.writeVarInt(RegistryHelper.menuType("merchant"));
            pb.writeAnonymousNbt(title);
        });
        sendProcessorContent(windowId, s.slots, 3); // 结果 + 输入1 + 输入2 + 36 玩家
        sendTradeList(windowId, villager);
    }

    /** 发送 trade_list(0x32)：列出已解锁的全部报价（含禁用状态）。
     *  774 线格式: ItemCost = [物品id varint][数量 varint][组件谓词列表 varint(0)];
     *  结果 = 完整 ItemStack(不可为空); costB = boolean 前缀的 Optional<ItemCost>。 */
    private void sendTradeList(int windowId, MobEntity villager) {
        java.util.List<MobEntity.VillagerTrade> trades = villager.unlockedTrades();
        java.util.List<MobEntity.VillagerTrade> valid = new java.util.ArrayList<>();
        for (MobEntity.VillagerTrade tr : trades) {
            if (tr.costAId > 0 && tr.resultId > 0 && tr.resultCount > 0) valid.add(tr);
        }
        sendPacket(ctx, 0x32, pb -> {
            pb.writeVarInt(windowId);
            pb.writeVarInt(valid.size());
            for (MobEntity.VillagerTrade tr : valid) {
                // ItemCost baseCostA: 物品id + 数量 + 空组件谓词
                pb.writeVarInt(tr.costAId);
                pb.writeVarInt(tr.costACount);
                pb.writeVarInt(0);
                // result: 完整 ItemStack (已过滤空结果)
                pb.writeSlot(tr.resultId, tr.resultCount);
                // Optional<ItemCost> costB: boolean 前缀
                if (tr.costBId > 0) {
                    pb.writeBoolean(true);
                    pb.writeVarInt(tr.costBId);
                    pb.writeVarInt(tr.costBCount);
                    pb.writeVarInt(0);
                } else {
                    pb.writeBoolean(false);
                }
                pb.writeBoolean(!tr.available()); // tradeDisabled
                pb.writeInt(tr.uses);
                pb.writeInt(tr.maxUses);
                pb.writeInt(tr.xp);
                pb.writeInt(tr.specialPrice);
                pb.writeFloat(tr.priceMult);
                pb.writeInt(tr.demand);
            }
            pb.writeVarInt(villager.villagerLevel);
            pb.writeVarInt(villager.villagerXp);
            pb.writeBoolean(true); // isRegularVillager
            pb.writeBoolean(true); // canRestock
        });
    }

    /** 执行当前选中交易：从输入槽/背包扣料、给产出、加经验、升级、补货状态回写。返回是否成交。 */
    private boolean executeTrade(int windowId, int button) {
        MerchantSession s = openMerchants.get(windowId);
        if (s == null) return false;
        MobEntity villager =
            (MobEntity) EntityManager
                .getEntities().get(s.villagerEid);
        if (villager == null) return false;
        java.util.List<MobEntity.VillagerTrade> trades = villager.unlockedTrades();
        if (s.selectedTrade < 0 || s.selectedTrade >= trades.size()) return false;
        MobEntity.VillagerTrade tr = trades.get(s.selectedTrade);
        if (!tr.available()) return false;

        // 1. 优先从容器输入槽扣料（客户端自动填入的），不足再从背包补
        int needA = tr.costACount;
        int gotA = takeFromMerchantOrInventory(s, tr.costAId, needA, 2); // in1 在 slots[2,3]
        if (gotA < needA) return false;
        int needB = tr.costBId > 0 ? tr.costBCount : 0;
        int gotB = 0;
        if (needB > 0) {
            gotB = takeFromMerchantOrInventory(s, tr.costBId, needB, 4); // in2 在 slots[4,5]
            if (gotB < needB) {
                refundToInventory(tr.costAId, gotA); // 回退刚才扣的 A
                return false;
            }
        }

        // 2. 给产出
        giveItem(tr.resultId, tr.resultCount);
        // 3. 更新村民状态
        tr.uses++;
        tr.demand++;
        villager.addVillagerXp(tr.xp);
        // 4. 回写界面
        sendProcessorContent(windowId, s.slots, 3);
        sendTradeList(windowId, villager);
        return true;
    }

    /** 先从商人容器槽(idx 2或4)扣，再从玩家背包扣；返回实际扣除数。 */
    private int takeFromMerchantOrInventory(MerchantSession s, int itemId, int need, int slotBase) {
        int got = 0;
        int inSlot = s.slots[slotBase];
        if (inSlot == itemId && s.slots[slotBase + 1] > 0) {
            int take = Math.min(s.slots[slotBase + 1], need);
            s.slots[slotBase + 1] -= take;
            if (s.slots[slotBase + 1] <= 0) { s.slots[slotBase] = 0; s.slots[slotBase + 1] = 0; }
            got += take;
        }
        while (got < need) {
            int removed = removeFromInventory(itemId, 1);
            if (removed <= 0) break;
            got += removed;
        }
        return got;
    }

    private void refundToInventory(int itemId, int count) {
        if (count > 0) giveItem(itemId, count);
    }

    /** 从玩家背包(9..44)移除最多 count 个 itemId，返回移除数。 */
    private int removeFromInventory(int itemId, int count) {
        int removed = 0;
        for (int i : PICKUP_SLOT_ORDER) {
            if (removed >= count) break;
            if (data.inventoryIds[i] == itemId && data.inventoryCounts[i] > 0) {
                int take = Math.min(data.inventoryCounts[i], count - removed);
                data.inventoryCounts[i] -= take;
                if (data.inventoryCounts[i] <= 0) { data.inventoryIds[i] = 0; data.inventoryCounts[i] = 0; }
                removed += take;
            }
        }
        if (removed > 0) sendInventoryUpdate();
        return removed;
    }

    private int merchantToPlayerSlot(int slot) {
        if (slot < 3) return -1; // 0,1,2 为商人容器槽
        int ps = 9 + (slot - 3);
        return ps > 44 ? -1 : ps;
    }

    /** #15 信标支付物白名单（原版 ItemTags.BEACON_PAYMENT_ITEMS）。 */
    private static boolean isBeaconPayment(String name) {
        return name != null && (name.equals("iron_ingot") || name.equals("gold_ingot")
            || name.equals("emerald") || name.equals("diamond") || name.equals("netherite_ingot"));
    }

    /** #29 发送命令方块数据: 通过 block_entity_data(0x09) 下发 Command/Mode/Conditional/Auto/LastOutput,
     *  客户端据此渲染编辑器 UI(原版客户端打开 command block 菜单即请求此数据)。 */
    private void sendCommandBlockData(ContainerStore.Pos pos, boolean canEdit) {
        Chunk cChunk = WorldManager.getChunk(pos.dim(), pos.x() >> 4, pos.z() >> 4);
        if (cChunk == null) return;
        ContainerStore.CommandBlockData cbd = ContainerStore.commandBlock(pos);
        org.cloudburstmc.nbt.NbtMapBuilder cb = org.cloudburstmc.nbt.NbtMap.builder();
        cb.putString("id", "minecraft:command_block");
        cb.putInt("x", pos.x()); cb.putInt("y", pos.y()); cb.putInt("z", pos.z());
        cb.putString("Command", cbd.command);
        // 原版 CommandBlockEntity.Mode 序数: 0=SEQUENCE(脉冲), 1=AUTO(连锁), 2=REDSTONE(重复)。
        // NBT 无独立 mode 键: auto=1 表示连锁(auto), powered=1 表示红石(重复), 均 0 为脉冲。
        cb.putByte("auto", (byte) (cbd.mode == 1 ? 1 : 0));
        cb.putByte("powered", (byte) (cbd.mode == 2 ? 1 : 0));
        cb.putByte("conditionMet", (byte) (cbd.conditional ? 1 : 0));
        cb.putByte("TrackOutput", (byte) (cbd.trackOutput ? 1 : 0));
        cb.putString("LastOutput", cbd.lastOutput == null ? "" : cbd.lastOutput);
        // 权限: 客户端根据 CanExecute 判断按钮是否可点(原版 uses "customName" 无关)。
        cb.putByte("CanExecute", (byte) (canEdit ? 1 : 0));
        org.cloudburstmc.nbt.NbtMap nbt = cb.build();
        cChunk.setBlockEntity(pos.x() & 15, pos.y(), pos.z() & 15, nbt);
        // 1.21.11: block_entity_data = 0x06 (曾误用 0x09=boss_bar -> 客户端按 boss_event 解码
        // 报 "Index 23 out of bounds for length 6" 直接断开)。
        sendPacket(ctx, 0x06, pb -> {
            pb.writePosition(pos.x(), pos.y(), pos.z());
            pb.writeVarInt(com.CharunCore.server.utils.RegistryHelper.blockEntityTypeId("command_block"));
            pb.writeAnonymousNbt(nbt);
        });
    }

    /** #29 处理 serverbound update_command_block(0x35): pos + command + mode + flags。
     *  协议 Mode 枚举(原版 CommandBlockEntity.Mode 序数): 0=SEQUENCE(连锁), 1=AUTO(循环), 2=REDSTONE(脉冲)。
     *  内部 cbd.mode 约定: 1=连锁, 2=循环, 0=脉冲(与 RedstoneEngine 重载回读一致)。 */
    private void handleSetCommandBlock(int[] pos, String command, int mode, byte flags) {
        ContainerStore.Pos cp = new ContainerStore.Pos(this.currentDim, pos[0], pos[1], pos[2]);
        ContainerStore.CommandBlockData cbd = ContainerStore.commandBlock(cp);
        if (!cbd.hasPermission && gameMode != 1 && opLevel() < 2) return; // 无权限拒收
        cbd.command = command == null ? "" : command;
        int internalMode = switch (mode) {
            case 0 -> 1;  // SEQUENCE -> 连锁
            case 1 -> 2;  // AUTO -> 循环
            default -> 0; // REDSTONE -> 脉冲
        };
        cbd.mode = internalMode;
        cbd.trackOutput = (flags & 1) != 0;
        cbd.conditional = (flags & 2) != 0;
        cbd.auto = (flags & 4) != 0;
        cbd.version++;
        // Bug19 修复: 原版脉冲/循环/连锁是三个不同方块(command_block/chain/repeating),
        // 客户端 UI 切模式后经 update_command_block 上报 mode, 服务端必须把方块状态
        // 换成对应方块(保留朝向等属性)。曾按旧序理解(1=连锁,2=循环) -> 点循环出连锁、
        // 点连锁出脉冲, 观感即"无法调成循环或连锁"。
        String wantBlock = switch (internalMode) {
            case 1 -> "chain_command_block";
            case 2 -> "repeating_command_block";
            default -> "command_block";
        };
        int curState = WorldManager.getBlockState(this.currentDim, pos[0], pos[1], pos[2]);
        String curBlock = BlockStateHelper.getName(curState);
        if ((curBlock.equals("command_block") || curBlock.equals("chain_command_block")
                || curBlock.equals("repeating_command_block")) && !wantBlock.equals(curBlock)) {
            int newState = BlockStateHelper.getDefault(wantBlock);
            for (String prop : new String[]{"facing", "conditional", "powered"}) {
                String v = BlockStateHelper.getProp(curState, prop);
                if (v != null && BlockStateHelper.getProp(newState, prop) != null) {
                    newState = BlockStateHelper.withProp(newState, prop, v);
                }
            }
            String condWant = cbd.conditional ? "true" : "false";
            if (BlockStateHelper.getProp(newState, "conditional") != null) {
                newState = BlockStateHelper.withProp(newState, "conditional", condWant);
            }
            WorldManager.setBlock(this.currentDim, pos[0], pos[1], pos[2], newState);
            broadcastBlockChange(this.currentDim, pos[0], pos[1], pos[2], newState);
            RedstoneEngine.onBlockChanged(this.currentDim, pos[0], pos[1], pos[2]);
        }
        // 写回 BE 持久化
        Chunk cChunk = WorldManager.getChunk(cp.dim(), pos[0] >> 4, pos[2] >> 4);
        if (cChunk != null) {
            org.cloudburstmc.nbt.NbtMap cbe = cChunk.getBlockEntity(pos[0] & 15, pos[1], pos[2] & 15);
            org.cloudburstmc.nbt.NbtMapBuilder cbb = org.cloudburstmc.nbt.NbtMap.builder();
            if (cbe != null) for (String k : cbe.keySet()) cbb.put(k, cbe.get(k));
            cbb.putString("Command", cbd.command);
            cbb.putByte("auto", (byte) internalMode);
            cbb.putByte("conditionMet", (byte) (cbd.conditional ? 1 : 0));
            cbb.putByte("TrackOutput", (byte) (cbd.trackOutput ? 1 : 0));
            cChunk.setBlockEntity(pos[0] & 15, pos[1], pos[2] & 15, cbb.build());
        }
        sendFeedback("命令方块已更新: /" + cbd.command, "green");
    }

    /** #15 处理 set_beacon_effect(0x33)：校验金字塔层数 + 支付物后写入效果并广播。 */
    private void handleSetBeaconEffect(int primary, int secondary) {
        if (beaconWindowId < 0) return;
        ContainerStore.Pos pos = openBeacons.get(beaconWindowId);
        if (pos == null) return;
        ContainerStore.BeaconData bd = ContainerStore.beacon(pos);
        // Bug11 修复: 选效果时实时重算金字塔层数(曾只在打开 UI 时算一次,
        // 后补的金字塔/层数变化导致 levels 恒为 0 -> 点击确认永远被拒)。
        bd.levels = computeBeaconLevels(pos.dim(), pos.x(), pos.y(), pos.z());
        // #11 二轮诊断: 确认服务端是否收到选择、条件为何被拒
        System.out.println("[信标诊断] primary=" + primary + " secondary=" + secondary
            + " levels=" + bd.levels + " payment=" + bd.payment
            + " payItem=" + bd.paymentSlot[0] + "x" + bd.paymentSlot[1]);
        if (!bd.payment || bd.paymentSlot[0] <= 0) return;
        if (bd.levels < 1) return;
        if (secondary > -1 && bd.levels < 4) secondary = -1;
        // 主效果范围: 0 基注册表 id, -1 = 无效果
        if (primary < -1 || primary > 32) primary = -1;
        if (secondary < -1 || secondary > 32) secondary = -1;
        if (primary == secondary && primary != -1) secondary = -1;

        bd.primary = primary;
        bd.secondary = secondary;
        // 消耗支付物(取 1 个)
        bd.paymentSlot[1]--;
        if (bd.paymentSlot[1] <= 0) { bd.paymentSlot[0] = 0; bd.paymentSlot[1] = 0; bd.payment = false; }
        bd.version++;
        // 写回信标 BE(Primary/Secondary/Levels) 以便持久化
        Chunk bChunk = WorldManager.getChunk(pos.dim(), pos.x() >> 4, pos.z() >> 4);
        if (bChunk != null) {
            org.cloudburstmc.nbt.NbtMap bbe = bChunk.getBlockEntity(pos.x() & 15, pos.y(), pos.z() & 15);
            org.cloudburstmc.nbt.NbtMapBuilder bb = org.cloudburstmc.nbt.NbtMap.builder();
            if (bbe != null) for (String k : bbe.keySet()) bb.put(k, bbe.get(k));
            bb.putInt("Primary", primary);
            bb.putInt("Secondary", secondary);
            bb.putInt("Levels", bd.levels);
            bChunk.setBlockEntity(pos.x() & 15, pos.y(), pos.z() & 15, bb.build());
        }
        sendContainerProperty(beaconWindowId, 0, bd.levels);
        sendContainerProperty(beaconWindowId, 1, primary);
        sendContainerProperty(beaconWindowId, 2, secondary);
        sendSlotUpdate(beaconWindowId, 0);
        // 施放效果到信标周围 30 格内玩家（简化：直接提示，效果施加受架构限制）。
        applyBeaconEffectToPlayers(pos);
    }

    /** #15 信标效果施加（架构近似）：主效果 I 级、副效果 II 级，持续 4 秒，范围 30 格。
     *   #11 强化: 同时写入服务端 activeEffects, 使 buff 实际生效(不止客户端图标)。 */
    private void applyBeaconEffectToPlayers(ContainerStore.Pos pos) {
        ContainerStore.BeaconData bd = ContainerStore.beacon(pos);
        int levels = bd.levels;
        if (levels < 1) return;
        int range = 10 + levels * 10; // 原版: 10 + 层数*10
        for (NetworkHandler p : players.values()) {
            if (p.ctx == null || !p.ctx.channel().isActive() || p.currentDim != pos.dim()) continue;
            int dx = (int) (p.x - pos.x()), dz = (int) (p.z - pos.z());
            if (dx * dx + dz * dz > range * range) continue;
            applyEffectToPlayer(p, bd.primary, 1, 4 * 20);
            if (bd.secondary > 0) applyEffectToPlayer(p, bd.secondary, 2, 4 * 20);
        }
    }

    /** Bug11: 信标 buff 周期性重施加(原版 BeaconBlockEntity 每 80 tick 给范围内玩家上效果,
     *  曾只在点击确认瞬间施加一次 4 秒 -> 玩家几乎察觉不到 buff = "无法受到该效果的 buff")。 */
    public static void beaconEffectTick() {
        for (var e : ContainerStore.beaconEntries()) {
            ContainerStore.BeaconData bd = e.getValue();
            if (bd == null || bd.primary < 0 || bd.levels < 1) continue;
            if ((int) (com.CharunCore.server.Main.worldAge % 80) != 0) continue;
            ContainerStore.Pos pos = e.getKey();
            int range = 10 + bd.levels * 10;
            int duration = (8 + bd.levels * 2) * 20;
            for (NetworkHandler p : players.values()) {
                if (p.ctx == null || !p.ctx.channel().isActive() || p.isDead
                        || p.currentDim != pos.dim()) continue;
                double dx = p.x - pos.x(), dz = p.z - pos.z();
                double dy = p.y - pos.y();
                if (dx * dx + dy * dy + dz * dz > (double) range * range) continue;
                int primaryAmp = (bd.secondary > 0 && bd.secondary == bd.primary && bd.levels >= 4) ? 1 : 0;
                p.applyEffectToPlayer(p, bd.primary, primaryAmp, duration);
                if (bd.secondary > 0 && bd.secondary != bd.primary) {
                    p.applyEffectToPlayer(p, bd.secondary, 1, duration);
                }
            }
        }
    }

    /** 给玩家施加状态效果（实体状态效果系统的基础实现，供信标/药水共用）。
     *  #11 修复: 同时写入 activeEffects, 让 buff 真正在服务端/客户端生效。 */
    private static void applyEffectToPlayer(NetworkHandler p, int effectId, int amplifier, int durationTicks) {
        if (effectId < 0 || p.data == null) return;
        // 原版 ClientboundUpdateMobEffectPacket(0x82 entity_effect):
        // entityId VarInt + effect(registry id, VarInt id+1) + amplifier VarInt + duration VarInt + flags Byte。
        // flags: 1=ambient 2=visible 4=show_icon -> 7 表示可见+图标。
        if (p.ctx == null || !p.ctx.channel().isActive()) return;
        p.sendPacket(p.ctx, 0x82, pb -> {
            pb.writeVarInt(p.eid);
            pb.writeVarInt(effectId); // 0 基 mob_effect 注册表 id
            pb.writeVarInt(amplifier);
            pb.writeVarInt(durationTicks);
            pb.writeByte(7);
        });
        // 同步给其他玩家
        for (NetworkHandler o : players.values()) {
            if (o == p || o.ctx == null || !o.ctx.channel().isActive()) continue;
            if (o.currentDim != p.currentDim) continue;
            o.sendPacket(o.ctx, 0x82, pb -> {
                pb.writeVarInt(p.eid);
                pb.writeVarInt(effectId);
                pb.writeVarInt(amplifier);
                pb.writeVarInt(durationTicks);
                pb.writeByte(7);
            });
        }
        // 服务端 buff 实际生效
        String en = effectNameById(effectId);
        if (en != null) p.addEffect(en, amplifier, durationTicks);
    }

    private int[] getMerchantContents(int windowId) {
        MerchantSession s = openMerchants.get(windowId);
        return s == null ? new int[6] : s.slots;
    }

    /** Bug11: 区块加载时从 BE 注册信标 —— beaconEffectTick 遍历 ContainerStore 的信标表,
     *  曾只在打开 UI 时注册 -> 重启后信标静默失效("效果不能保存")。 */
    public static void registerBeaconFromBE(ContainerStore.Pos pos, org.cloudburstmc.nbt.NbtMap be) {
        ContainerStore.BeaconData bd = ContainerStore.beacon(pos);
        if (be.containsKey("Primary")) bd.primary = be.getInt("Primary", -1);
        if (be.containsKey("Secondary")) bd.secondary = be.getInt("Secondary", -1);
        if (be.containsKey("Levels")) bd.levels = be.getInt("Levels", 0);
        bd.version++;
    }

    // ── 附魔台书架计数 ──────────────────────────────────────────────────────
    private int computeBookshelfCount(int x, int y, int z) {
        // Bug41: 原版 EnchantingTableBlock.BOOKSHELF_OFFSETS = [-2..2]^2 环圈(|dx|==2 或 |dz|==2) × dy∈{0,1}
        // 共 32 个候选位, 中间隔断检查在书架自身高度 dy 处(曾漏同层斜角 -> 满环书架只数到 4 个)。
        int count = 0;
        for (int dy = 0; dy <= 1; dy++) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (Math.abs(dx) != 2 && Math.abs(dz) != 2) continue;
                    int s = WorldManager.getBlockState(this.currentDim, x + dx, y + dy, z + dz);
                    String sn = BlockStateHelper.getName(s);
                    if (!"bookshelf".equals(sn) && !"chiseled_bookshelf".equals(sn)) continue;
                    // 中间隔断须为可穿透方块(原版 #enchantment_power_transmitter: air/水/花草等)
                    int m = WorldManager.getBlockState(this.currentDim, x + dx / 2, y + dy, z + dz / 2);
                    String mn = BlockStateHelper.getName(m);
                    boolean transmitter = "air".equals(mn) || "water".equals(mn)
                        || (mn != null && BlockStateHelper.isReplaceable(mn));
                    if (transmitter) count++;
                }
            }
        }
        return Math.min(15, count);
    }

    // ── 0x10 容器按钮点击 (附魔台选项选择) ─────────────────────────────────
    private void handleContainerButtonClick(int windowId, int buttonId) {
        // #18 切石机: 0x10 button click = 选择左侧样式列表中的配方 (原版 StonecutterMenu.clickMenuButton)。
        if (openStonecutters.containsKey(windowId)) {
            ContainerStore.StonecutterData sd = getStonecutterData(windowId);
            if (sd == null || buttonId < 0) return;
            if (buttonId < sd.candidates.length) {
                sd.selectedIndex = buttonId;
                sd.slots[2] = sd.candidates[buttonId];
                sd.slots[3] = 1;
                sd.version++;
                sendProcessorContent(windowId, sd.slots, 2);
            }
            return;
        }
        if (!openEnchanting.containsKey(windowId)) return;
        if (buttonId < 0 || buttonId > 2) return;
        ContainerStore.EnchantingData ed = getEnchantingData(windowId);
        if (ed == null) return;
        if (ed.optionEnchList[buttonId] == null || ed.optionEnchList[buttonId].isEmpty()) return;
        boolean creative = gameMode == 1;
        int cost = ed.optionCost[buttonId];
        // Bug26 修复: 原版 hasInfiniteMaterials(创造)无视经验等级与青金石数量限制,
        // 曾一律按生存校验 -> 创造模式点击选项无任何反应。
        if (!creative && data.xpLevel < cost) return;
        int lapisCount = ed.slots[3];
        int lapisNeed = buttonId + 1; // 原版: 顶部1/中部2/底部3
        if (!creative && lapisCount < lapisNeed) return; // 青金石不足
        int inId = ed.slots[0], inCount = ed.slots[1];
        if (inId <= 0 || inCount <= 0) return;
        if (!creative && !hasEmptySlot()) return; // 背包需有空位

        // 扣经验等级 / 扣青金石 / 扣输入物品
        if (!creative) {
            data.xpLevel -= cost;
            sendExperienceUpdate();
            ed.slots[3] -= lapisNeed;
            if (ed.slots[3] <= 0) { ed.slots[2] = 0; ed.slots[3] = 0; }
            ed.slots[1]--;
            if (ed.slots[1] <= 0) { ed.slots[0] = 0; ed.slots[1] = 0; writeContMeta(ed.meta, 0, null); }
        } else {
            // 创造: 输入物品不消耗, 附魔结果直接进背包
        }
        ed.version++;

        giveEnchantedItem(inId, 1, ed.optionEnchList[buttonId]);
        AdvancementManager.onEnchantItem(this,
            BlockManager.itemIdToName(inId));
        this.enchantSeed = new java.util.Random().nextLong(); // 一次附魔后重掷(原版)
        recomputeEnchanting(windowId);
        // #44 修复: 附魔台仅 2 容器槽(物品+青金石), 曾误用 3 -> 客户端背包槽位错位一格,
        // 附魔后界面刷新错乱/无法继续。打开时用 2, 点击后也必须用 2。
        sendProcessorContent(windowId, ed.slots, 2);
    }

    private boolean hasEmptySlot() {
        for (int i : PICKUP_SLOT_ORDER) {
            if (data.inventoryIds[i] == 0 || data.inventoryCounts[i] == 0) return true;
        }
        return false;
    }

    private int giveEnchantedItem(int itemId, int count,
                                  java.util.List<EnchantSystem.EnchantInstance> enchants) {
        int slot = -1;
        for (int i : PICKUP_SLOT_ORDER) {
            if (data.inventoryIds[i] == 0 || data.inventoryCounts[i] == 0) { slot = i; break; }
        }
        if (slot < 0) return -1;
        // 书作为输入 → 产出附魔书(物品 id 改为 enchanted_book)
        String inName = BlockManager.itemIdToName(itemId);
        int outId = itemId;
        if ("book".equals(inName)) {
            int eb = BlockManager.getItemIdByName("enchanted_book");
            if (eb > 0) outId = eb;
        }
        data.inventoryIds[slot] = outId;
        data.inventoryCounts[slot] = count;
        // 应用全部附魔（原版一个选项可含 1-3 个；曾只写第一个）
        if (enchants != null) {
            for (EnchantSystem.EnchantInstance e : enchants) {
                if (e == null) continue;
                data.inventoryEnchants[slot].put(e.enchantId, e.level);
            }
        }
        sendSlotUpdate(0, slot);
        return slot;
    }

    // ── 附魔台: 根据输入物品 + 书架数 + 玩家等级重新计算 3 个选项 ──────────
    private void recomputeEnchanting(int windowId) {
        ContainerStore.EnchantingData ed = getEnchantingData(windowId);
        if (ed == null) return;
        // #44 修复: 每次重算时重新扫描周围书架(放书架后即使 UI 已打开, 选项等级也应上升;
        // 曾只在打开时算一次 -> 摆好书架再放物品/重开仍用旧书架数)。
        ContainerStore.Pos epos = openEnchanting.get(windowId);
        if (epos != null) {
            ed.bookshelfCount = computeBookshelfCount(epos.x(), epos.y(), epos.z());
        }
        int inId = ed.slots[0], inCount = ed.slots[1];
        if (inId <= 0 || inCount <= 0) {
            for (int i = 0; i < 3; i++) { ed.optionEnchant[i] = 0; ed.optionLevel[i] = 0; ed.optionCost[i] = 0; }
            sendContainerProperty(windowId, 3, new java.util.Random().nextInt()); // 3: seed
            for (int i = 0; i < 3; i++) {
                sendContainerProperty(windowId, i, 0);       // 0-2: cost
                sendContainerProperty(windowId, 4 + i, -1);  // 4-6: enchantClue(-1=无)
                sendContainerProperty(windowId, 7 + i, -1);  // 7-9: levelClue(-1=无)
            }
            return;
        }
        String itemName = BlockManager.itemIdToName(inId);
        long seed = this.enchantSeed; // 稳定种子: 选项随交互不乱跳
        EnchantSystem.Option[] opts =
            EnchantSystem.compute(itemName, ed.bookshelfCount, data.xpLevel, seed);
        for (int i = 0; i < 3; i++) {
            ed.optionEnchant[i] = opts[i].enchantId;
            ed.optionLevel[i] = opts[i].level;
            ed.optionCost[i] = opts[i].cost;
            ed.optionEnchList[i] = new java.util.ArrayList<>(opts[i].enchantments);
        }
        // 原版 EnchantmentMenu data slot 顺序：0-2=cost、3=seed、4-6=enchantClue(附魔注册表id)、7-9=levelClue。
        // 曾把 seed 发到 0、cost 发到 1-3、等级发到 4-6 → 成本/附魔名/等级全错位。
        for (int i = 0; i < 3; i++) {
            sendContainerProperty(windowId, i, ed.optionCost[i]);       // 0-2: 所需经验等级
        }
        sendContainerProperty(windowId, 3, (int) (seed & 0x7fffffff)); // 3: 附魔种子
        for (int i = 0; i < 3; i++) {
            sendContainerProperty(windowId, 4 + i, ed.optionEnchant[i]); // 4-6: 附魔注册表 id
            sendContainerProperty(windowId, 7 + i, ed.optionLevel[i]);   // 7-9: 附魔等级
        }
    }

    // ── 锻造台: 钻石装备 + 下界合金锭 → 下界合金装备 ──────────────────────
    // ── 锻造台 (原版 SmithingMenu): 槽 0=模板 1=基础装备 2=附加材料 3=结果 ──
    private void recomputeSmithing(int windowId) {
        ContainerStore.SmithingData sd = getSmithingData(windowId);
        if (sd == null) return;
        int templateId = sd.slots[0], baseId = sd.slots[2], addId = sd.slots[4];
        sd.slots[6] = 0; sd.slots[7] = 0;
        sd.outTrimMaterial = -1; sd.outTrimPattern = -1;
        // Bug14: 结果槽组件继承基础装备组件(下界合金升级/纹饰都要保留附魔/改名/耐久)
        writeContMeta(sd.meta, 3, contMeta(sd.meta, 1));
        if (baseId > 0 && addId > 0) {
            String baseName = BlockManager.itemIdToName(baseId);
            String addName = BlockManager.itemIdToName(addId);
            String templateName = templateId > 0 ? BlockManager.itemIdToName(templateId) : null;
            // #19 模板校验: 下界合金升级需 netherite_upgrade_smithing_template(原版)。
            boolean netheriteUpgrade = templateId <= 0
                || "netherite_upgrade_smithing_template".equals(templateName)
                || "netherite_upgrade".equals(templateName);
            if (netheriteUpgrade && baseName != null && baseName.startsWith("diamond_") && "netherite_ingot".equals(addName)) {
                String resultName = "netherite_" + baseName.substring("diamond_".length());
                int rid = BlockManager.getItemIdByName(resultName);
                if (rid > 0) { sd.slots[6] = rid; sd.slots[7] = 1; }
            } else if (templateName != null && templateName.endsWith("_smithing_template")
                    && !"netherite_upgrade_smithing_template".equals(templateName)
                    && isArmorForTrim(baseName) && isTrimMaterial(addName)) {
                // 盔甲纹饰: 模板(非升级) + 盔甲 + 材料 -> 输出盔甲(带 trim 组件)。
                // 原版 SmithingTrimRecipe: 附加材料经 TrimMaterials.getFromIngredient 匹配。
                // trimMaterialId/trimPatternId 返回 0 基注册表 id(amethyst/bolt=0), 仅在均有效时出结果。
                int tm = trimMaterialId(addName);
                int tp = trimPatternId(templateName);
                if (tm >= 0 && tp >= 0) {
                    sd.slots[6] = baseId; sd.slots[7] = 1;
                    sd.outTrimMaterial = tm;
                    sd.outTrimPattern = tp;
                }
            }
        }
        sd.version++;
        sendSmithingContent(windowId, sd);
    }

    /** 锻造台窗口: 结果槽带完整组件(继承基础装备 + 纹饰 trim)下发。 */
    private void sendSmithingContent(int windowId, ContainerStore.SmithingData sd) {
        int[] c = sd.slots;
        int tpl = c[0], tpc = c[1], base = c[2], bc = c[3], add = c[4], ac = c[5], res = c[6], rc = c[7];
        boolean tplBook = "enchanted_book".equals(BlockManager.itemIdToName(tpl));
        boolean addBook = "enchanted_book".equals(BlockManager.itemIdToName(add));
        ItemMeta tplMeta = contMeta(sd.meta, 0);
        ItemMeta baseMeta = contMeta(sd.meta, 1);
        ItemMeta addMeta = contMeta(sd.meta, 2);
        ItemMeta resMeta = contMeta(sd.meta, 3);
        boolean resBook = "enchanted_book".equals(BlockManager.itemIdToName(res));
        boolean hasTrim = sd.outTrimMaterial >= 0 && sd.outTrimPattern >= 0;
        sendPacket(ctx, 0x12, pb -> {
            pb.writeVarInt(windowId);
            pb.writeVarInt(0);
            pb.writeVarInt(4 + 36);
            writeStackWithMeta(pb, tpl, tpc, tplMeta);
            writeStackWithMeta(pb, base, bc, baseMeta);
            writeStackWithMeta(pb, add, ac, addMeta);
            // Bug4/33: 模板/附加材料也按存储组件下发(放入带附魔物品不再显示白板)。
            // 结果槽 = 继承的基础组件 + 可选 trim。
            if (hasTrim) {
                int[] rp = parsePotionEx(resMeta.potion());
                pb.writeStackWithComponents(res, rc,
                        resMeta.enchants().isEmpty() ? null : resMeta.enchants(), resBook,
                        rp[0], rp[1], rp[2], resMeta.customName(), resMeta.damage(),
                        sd.outTrimMaterial, sd.outTrimPattern, rp[3]);
            } else {
                writeStackWithMeta(pb, res, rc, resMeta);
            }
            for (int ps = 9; ps <= 44; ps++) writePlayerSlot(pb, ps);
            writeCarriedSlot(pb);
        });
    }

    /** #19 可纹饰的盔甲(原版: 头盔/胸甲/护腿/靴子)。 */
    private static boolean isArmorForTrim(String name) {
        if (name == null) return false;
        return (name.endsWith("_helmet") || name.endsWith("_chestplate")
            || name.endsWith("_leggings") || name.endsWith("_boots"))
            && !name.startsWith("leather_");
    }

    /** #19 纹饰材料(原版 TrimMaterials, 简化映射): emerald/diamond/iron/gold/quartz/redstone/lapis/copper/amethyst/netherite/resin。 */
    private static boolean isTrimMaterial(String name) {
        return switch (name) {
            case "emerald","diamond","iron_ingot","gold_ingot","quartz","redstone","lapis_lazuli",
                 "copper_ingot","amethyst_shard","netherite_ingot","resin_brick" -> true;
            default -> false;
        };
    }

    // 原版 1.21.11 trim_material 注册表 id（来自 dumped_registries/reg_4.bin 的真实客户端注册顺序）。
    // 客户端按此 bin 顺序分配 id: amethyst=0,copper=1,diamond=2,emerald=3,gold=4,iron=5,lapis=6,netherite=7,quartz=8,redstone=9,resin=10
    private static int trimMaterialId(String name) {
        return switch (name) {
            case "amethyst_shard" -> 0;
            case "copper_ingot" -> 1;
            case "diamond" -> 2;
            case "emerald" -> 3;
            case "gold_ingot" -> 4;
            case "iron_ingot" -> 5;
            case "lapis_lazuli" -> 6;
            case "netherite_ingot" -> 7;
            case "quartz" -> 8;
            case "redstone" -> 9;
            case "resin_brick" -> 10;
            default -> -1;
        };
    }

    // 原版 1.21.11 trim_pattern 注册表 id（来自 dumped_registries/reg_3.bin 的真实客户端注册顺序）。
    // bolt=0,coast=1,dune=2,eye=3,flow=4,host=5,raiser=6,rib=7,sentry=8,shaper=9,silence=10,snout=11,
    // spire=12,tide=13,vex=14,ward=15,wayfinder=16,wild=17
    private static int trimPatternId(String templateName) {
        return switch (templateName) {
            case "bolt_armor_trim_smithing_template" -> 0;
            case "coast_armor_trim_smithing_template" -> 1;
            case "dune_armor_trim_smithing_template" -> 2;
            case "eye_armor_trim_smithing_template" -> 3;
            case "flow_armor_trim_smithing_template" -> 4;
            case "host_armor_trim_smithing_template" -> 5;
            case "raiser_armor_trim_smithing_template" -> 6;
            case "rib_armor_trim_smithing_template" -> 7;
            case "sentry_armor_trim_smithing_template" -> 8;
            case "shaper_armor_trim_smithing_template" -> 9;
            case "silence_armor_trim_smithing_template" -> 10;
            case "snout_armor_trim_smithing_template" -> 11;   // 猪鼻纹饰
            case "spire_armor_trim_smithing_template" -> 12;
            case "tide_armor_trim_smithing_template" -> 13;
            case "vex_armor_trim_smithing_template" -> 14;
            case "ward_armor_trim_smithing_template" -> 15;
            case "wayfinder_armor_trim_smithing_template" -> 16;
            case "wild_armor_trim_smithing_template" -> 17;
            default -> -1;
        };
    }

    // ── 铁砧: 重命名 + 附魔书合并 + 同种物品修复/合并(原版行为) ──
    /** Bug3/6/12/31: 按原版 AnvilMenu.createResult 重写。
     *  原版逻辑(mapping/remapped_server_1.21.11.jar.src/net/minecraft/world/inventory/AnvilMenu.java):
     *  - 同种可损伤物品合并: 剩余耐久 = r1 + r2 + max*12%, 新损伤 = max - n (clamp>=0, 仅在变好时生效, +2 级)
     *  - 附魔书/物品附魔转移: 同级则 +1, 否则取高; clamp 到 max_level; 消耗 = anvil_cost(书减半,min 1) * 结果等级
     *  - 互斥组附魔冲突时该条不施加(且全部冲突+无修复 -> 无结果)
     *  - 改名: 与当前显示名不同才计 1 级; 清空输入框 = 去掉自定义名
     *  - 可堆叠非工具同物合并: 原版不允许(直接无结果) */
    private void recomputeAnvil(int windowId) {
        ContainerStore.AnvilData ad = getAnvilData(windowId);
        if (ad == null) return;
        int leftId = ad.slots[0], leftCount = ad.slots[1];
        int rightId = ad.slots[2], rightCount = ad.slots[3];
        ad.slots[4] = 0; ad.slots[5] = 0; ad.cost = 0;
        ad.outId = 0; ad.outCount = 0;
        java.util.Map<Integer, Integer> freshOut = new java.util.HashMap<>();
        ad.outEnchants = freshOut;
        ad.outName = null;
        ad.outDamage = 0;

        if (leftId <= 0) { ad.version++; sendAnvilContent(windowId); return; }

        String leftName = BlockManager.itemIdToName(leftId);
        String rightName = rightId > 0 ? BlockManager.itemIdToName(rightId) : null;
        boolean rightIsBook = rightId > 0 && "enchanted_book".equals(rightName);

        int outId = leftId;
        int outCount = leftCount;
        int outDamage = ad.leftDamage;
        String outPotion = ad.leftPotion;
        java.util.Map<Integer, Integer> outEnch = new java.util.HashMap<>(ad.leftEnchants);
        int cost = 0;
        boolean changedAny = false;

        if (rightId > 0) {
            int maxDmg = getMaxDurability(leftName);
            boolean sameDamageable = !rightIsBook && rightId == leftId && maxDmg > 0;
            if (!rightIsBook && !sameDamageable) {
                // 原版: 非书、非同种可损伤物品 -> 无结果(材料修复暂未实现)
                ad.version++;
                sendAnvilContent(windowId);
                return;
            }
            if (sameDamageable) {
                int j = maxDmg - ad.leftDamage;
                int k = maxDmg - ad.rightDamage;
                int n = j + k + maxDmg * 12 / 100;
                int newDmg = Math.max(maxDmg - n, 0);
                if (newDmg < ad.leftDamage) {
                    outDamage = newDmg;
                    changedAny = true;
                    cost += 2;
                }
            }
            // 附魔转移/合并(附魔书或带附魔的同种物品)
            boolean anyApplied = false;
            boolean anyConflict = false;
            for (java.util.Map.Entry<Integer, Integer> e : ad.rightEnchants.entrySet()) {
                int enchId = e.getKey();
                int curLvl = outEnch.getOrDefault(enchId, 0);
                int srcLvl = e.getValue();
                // 原版: 目标已有同级 -> 提升一级; 否则取高
                int newLvl = (curLvl == srcLvl) ? srcLvl + 1 : Math.max(srcLvl, curLvl);
                boolean canApply = true;
                for (Integer have : outEnch.keySet()) {
                    if (!have.equals(enchId) && !EnchantSystem.areCompatibleById(have, enchId)) {
                        canApply = false;
                        cost++;
                    }
                }
                if (!canApply) { anyConflict = true; continue; }
                anyApplied = true;
                int cap = EnchantSystem.maxLevelById(enchId);
                if (newLvl > cap) newLvl = cap;
                outEnch.put(enchId, newLvl);
                int unit = EnchantSystem.anvilCostById(enchId);
                if (rightIsBook) unit = Math.max(1, unit / 2);
                cost += unit * newLvl;
            }
            if (anyConflict && !anyApplied) {
                // 全部冲突且无其它变更 -> 无结果(原版 bool3&&!bool2)
                ad.version++;
                sendAnvilContent(windowId);
                return;
            }
            changedAny |= anyApplied;
        }

        // 改名: 输入框为空且物品有自定义名 -> 去名(+1); 非空且与现名不同 -> 改名(+1)
        String currentName = ad.leftName == null ? "" : ad.leftName;
        if (ad.rename == null || ad.rename.isBlank()) {
            if (ad.leftName != null) {
                ad.outName = null;
                cost += 1;
                changedAny = true;
            } else {
                ad.outName = null;
            }
        } else if (!ad.rename.equals(currentName)) {
            ad.outName = ad.rename;
            cost += 1;
            changedAny = true;
        }

        if (changedAny && cost > 0 && cost < 40) {
            // 原版: 消耗 >= 40 级时生存模式无结果(onlyRenaming 时钳到 39 的分支略去)
            ad.outId = outId;
            ad.outCount = Math.min(outCount, 64);
            ad.outEnchants = outEnch;
            ad.outDamage = outDamage;
            ad.outPotion = outPotion;
            ad.slots[4] = outId; ad.slots[5] = ad.outCount;
            ad.cost = cost;
        }
        ad.version++;
        sendAnvilContent(windowId);
    }

    /** 铁砧窗口内容: 3 个铁砧槽带完整组件(耐久/附魔/改名/药水)下发,
     *  修复"铁砧里工具显示全耐久 / 输出端不显示改名"(曾用 writeSlot 丢组件)。 */
    private void sendAnvilContent(int windowId) {
        ContainerStore.AnvilData ad = getAnvilData(windowId);
        if (ad == null) return;
        int[] c = ad.slots;
        int li = c[0], lc = c[1], ri = c[2], rc = c[3], oi = c[4], oc = c[5];
        boolean lBook = "enchanted_book".equals(BlockManager.itemIdToName(li));
        boolean rBook = "enchanted_book".equals(BlockManager.itemIdToName(ri));
        boolean oBook = "enchanted_book".equals(BlockManager.itemIdToName(oi));
        int[] lp = parsePotionEx(ad.leftPotion);
        int[] rp = parsePotionEx(ad.rightPotion);
        int[] op = parsePotionEx(ad.outPotion);
        sendPacket(ctx, 0x12, pb -> {
            pb.writeVarInt(windowId);
            pb.writeVarInt(0);
            pb.writeVarInt(3 + 36);
            pb.writeStackWithComponents(li, lc, ad.leftEnchants, lBook, lp[0], lp[1], lp[2], ad.leftName, ad.leftDamage, -1, -1, lp[3]);
            pb.writeStackWithComponents(ri, rc, ad.rightEnchants, rBook, rp[0], rp[1], rp[2], ad.rightName, ad.rightDamage, -1, -1, rp[3]);
            pb.writeStackWithComponents(oi, oc, ad.outEnchants, oBook, op[0], op[1], op[2], ad.outName, ad.outDamage, -1, -1, op[3]);
            for (int ps = 9; ps <= 44; ps++) writePlayerSlot(pb, ps);
            writeCarriedSlot(pb);
        });
    }

    // ── 酿造台进度同步 ─────────────────────────────────────────────────────
    private void syncOpenBrewings() {
        for (var entry : openBrewing.entrySet()) {
            int windowId = entry.getKey();
            ContainerStore.Pos pos = entry.getValue();
            ContainerStore.BrewingData b =
                ContainerStore.peekBrewing(pos);
            if (b == null) continue;
            sendContainerProperty(windowId, 0, b.brewTime);
            // 原版属性1 = fuel(0-20 刻度), 客户端气泡按它渲染
            sendContainerProperty(windowId, 1, b.fuelTotal > 0
                ? (int) Math.min(20, (long) b.fuelTime * 20 / b.fuelTotal) : 0);
            int known = containerSyncVersion.getOrDefault(windowId, -1);
            if (known != b.version) {
                containerSyncVersion.put(windowId, b.version);
                sendProcessorContent(windowId, b.slots, 5);
                // #8: 酿造台物理方块上的 3 个瓶子/气泡随内容变化刷新(block_entity_data)。
                ContainerStore.persistBrewingAndGet(pos, b);
                int st = WorldManager.getBlockState(pos.dim(), pos.x(), pos.y(), pos.z());
                Chunk bChunk = WorldManager.getChunk(pos.dim(), pos.x() >> 4, pos.z() >> 4);
                if (bChunk != null) {
                    org.cloudburstmc.nbt.NbtMap be = bChunk.getBlockEntity(pos.x() & 15, pos.y(), pos.z() & 15);
                    if (be != null) {
                        for (NetworkHandler p : players.values()) {
                            if (p.ctx == null || !p.ctx.channel().isActive()) continue;
                            if (p.currentDim != pos.dim()) continue;
                            p.broadcastBlockEntityData(pos.x(), pos.y(), pos.z(), st, be);
                        }
                    }
                }
            }
        }
    }

    public void sendHealthUpdate() {
        // update_health/set_health = 0x66 (服务端 jar GameProtocols 注册序确认)。
        // 0x63=set_entity_motion / 0x64=set_equipment / 0x65=set_experience / 0x66=set_health /
        // 0x67=set_held_slot / 0x69=set_passengers —— 本区域曾连环错位(8-30 与 9-24 的
        // "set_experience 3 bytes extra" 断线即装备包误放 0x65 所致), 已全部按注册序校准。
        sendPacket(ctx, 0x66, pb -> {
            pb.writeFloat(health);
            pb.writeVarInt(data.food);
            pb.writeFloat(data.saturation);
        });
    }

    /** 同步自身 onFire 元数据标志(索引0 bit0), 同时保留潜行(bit1)与姿态(索引6)。 */
    public void syncOnFire() {
        byte flags = 0;
        if (isSneaking) flags |= 0x02;
        if (fireTicks > 0) flags |= 0x01;
        byte pose = (byte)(isSneaking ? 5 : 0); // 5=crouching, 0=standing
        final byte fFlags = flags, fPose = pose;
        this.sendPacket(this.ctx, 0x61, pb -> { // entity_metadata/set_entity_data=0x61 (0x62=attach_entity/set_entity_link 栓绳包,固定 8 字节 2×i32; 曾误用 0x62 -> eid 大于 127 时载荷超出 8 字节, 客户端报 "N bytes extra" 断线)
            pb.writeVarInt(this.eid);
            pb.writeByte(0); pb.writeVarInt(0); pb.writeByte(fFlags);
            pb.writeByte(6); pb.writeVarInt(20); pb.writeVarInt(fPose);
            pb.writeByte(0xFF);
        });
        for (NetworkHandler h : players.values()) {
            if (h == this || h.ctx == null) continue;
            h.sendPacket(h.ctx, 0x61, pb -> { // entity_metadata/set_entity_data=0x61 (0x62=attach_entity/set_entity_link 栓绳包,固定 8 字节 2×i32; 曾误用 0x62 -> eid 大于 127 时载荷超出 8 字节, 客户端报 "N bytes extra" 断线)
                pb.writeVarInt(this.eid);
                pb.writeByte(0); pb.writeVarInt(0); pb.writeByte(fFlags);
                pb.writeByte(6); pb.writeVarInt(20); pb.writeVarInt(fPose);
                pb.writeByte(0xFF);
            });
        }
    }

    // ── 状态效果系统 ──────────────────────────────────────────────────────────
    private static int effectProtocolId(String name) {
        String n = name.startsWith("minecraft:") ? name.substring(10) : name;
        return switch (n) {
            case "speed" -> 0; case "slowness" -> 1; case "haste" -> 2; case "mining_fatigue" -> 3;
            case "strength" -> 4; case "instant_health" -> 5; case "instant_damage" -> 6; case "jump_boost" -> 7;
            case "nausea" -> 8; case "regeneration" -> 9; case "resistance" -> 10; case "fire_resistance" -> 11;
            case "water_breathing" -> 12; case "invisibility" -> 13; case "blindness" -> 14; case "night_vision" -> 15;
            case "hunger" -> 16; case "weakness" -> 17; case "poison" -> 18; case "wither" -> 19;
            case "health_boost" -> 20; case "absorption" -> 21; case "saturation" -> 22; case "glow" -> 23;
            case "levitation" -> 24; case "luck" -> 25; case "bad_luck" -> 26; case "slow_falling" -> 27;
            case "conduit_power" -> 28; case "dolphins_grace" -> 29; case "bad_omen" -> 30;
            case "hero_of_the_village" -> 31; case "darkness" -> 32;
            default -> -1;
        };
    }

    /** 给予/刷新一个状态效果。durationTicks 为剩余刻数(20=1秒)。 */
    public void addEffect(String name, int amplifier, int durationTicks) {
        String n = name.startsWith("minecraft:") ? name.substring(10) : name;
        int id = effectProtocolId(n);
        if (id < 0) return;
        // health_boost / absorption: 调整上限/吸收心(立即同步)
        if (n.equals("health_boost")) {
            maxHealth = 20.0f + 4.0f * (amplifier + 1);
            health = Math.min(maxHealth, health + 4.0f * (amplifier + 1));
        }
        if (n.equals("absorption")) absorption = 4.0f * (amplifier + 1);
        // 瞬时效果: 立即结算一次(指数缩放 4*2^amp / 6*2^amp, 亡灵反转)
        if (n.equals("instant_health")) {
            if (isUndeadTarget()) { health -= 6.0f * (1 << amplifier); this.lastDamageType = "magic"; }
            else { health = Math.min(maxHealth, health + 4.0f * (1 << amplifier)); }
            sendHealthUpdate();
            return;
        }
        if (n.equals("instant_damage")) {
            if (isUndeadTarget()) { health = Math.min(maxHealth, health + 4.0f * (1 << amplifier)); }
            else { health -= 6.0f * (1 << amplifier); this.lastDamageType = "magic"; }
            sendHealthUpdate();
            if (health <= 0 && !isDead) { isDead = true; health = 0; sendDeathScreen(deathMessageFor("magic")); }
            return;
        }
        activeEffects.put(n, new int[]{amplifier, Math.max(1, durationTicks)});
        if (n.equals("health_boost") || n.equals("absorption")) {
            recomputeAndSendAttributes();
            sendHealthUpdate();
        }
        sendPacket(ctx, 0x82, pb -> {
            pb.writeVarInt(this.eid);
            pb.writeVarInt(id);
            pb.writeByte((byte) amplifier);
            pb.writeVarInt(Math.max(1, durationTicks));
            pb.writeByte((byte) 0x03); // showParticles + showIcon
        });
    }

    public void removeEffect(String name) {
        String n = name.startsWith("minecraft:") ? name.substring(10) : name;
        int[] cur = activeEffects.remove(n);
        if (cur == null) return;
        if (n.equals("health_boost")) { maxHealth = 20.0f; if (health > maxHealth) health = maxHealth; recomputeAndSendAttributes(); }
        if (n.equals("absorption")) { absorption = 0.0f; sendHealthUpdate(); }
        int id = effectProtocolId(n);
        if (id >= 0) sendPacket(ctx, 0x4C, pb -> { pb.writeVarInt(this.eid); pb.writeVarInt(id); });
    }

    public void clearEffects() {
        for (String n : new java.util.ArrayList<>(activeEffects.keySet())) removeEffect(n);
    }

    public boolean hasEffect(String name) {
        String n = name.startsWith("minecraft:") ? name.substring(10) : name;
        return activeEffects.containsKey(n);
    }

    /** 每 tick 调用: 递减时长, 结算持续治疗/伤害效果, 到期移除并通知客户端。 */
    private void tickEffects() {
        if (activeEffects.isEmpty()) return;
        effectTickCounter++;
        java.util.Iterator<java.util.Map.Entry<String,int[]>> it = activeEffects.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry<String,int[]> e = it.next();
            int[] v = e.getValue();
            v[1]--;
            String n = e.getKey();
            if (n.equals("regeneration")) {
                int interval = Math.max(1, 50 >> v[0]);
                if (effectTickCounter % interval == 0 && health < 20.0f) {
                    health = Math.min(20.0f, health + 1.0f); sendHealthUpdate();
                }
            } else if (n.equals("poison")) {
                int interval = Math.max(1, 25 >> v[0]);
                if (effectTickCounter % interval == 0 && health > 1.0f) {
                    health -= 1.0f; sendHealthUpdate();
                }
            } else if (n.equals("wither")) {
                if (effectTickCounter % 2 == 0 && health > 0.0f) {
                    health -= 1.0f; sendHealthUpdate();
                }
            }
            if (v[1] <= 0) {
                it.remove();
                int id = effectProtocolId(n);
                if (id >= 0) sendPacket(ctx, 0x4C, pb -> { pb.writeVarInt(this.eid); pb.writeVarInt(id); });
            }
        }
    }

    /** 计算当前护甲值与韧性值(分别求和 4 个护甲槽)。 */
    /** 护甲/韧性变化时才重发 UPDATE_ATTRIBUTES(0x81), 否则客户端护甲条恒为 0。 */
    private void syncAttributesIfNeeded() {
        int armor = 0, toughness = 0;
        for (int s = 5; s <= 8; s++) {
            String an = BlockManager.itemIdToName(data.inventoryIds[s]);
            if (an != null) { armor += armorPointsOf(an); toughness += armorToughnessOf(an); }
        }
        int key = (armor << 8) | toughness;
        if (key != lastSentArmorKey) {
            lastSentArmorKey = key;
            sendAttributesUpdate(armor, toughness);
        }
    }

    /** 立即重算护甲/韧性/max_health/吸收并下发 UPDATE_ATTRIBUTES(0x81)。 */
    private void recomputeAndSendAttributes() {
        int armor = 0, toughness = 0;
        for (int s = 5; s <= 8; s++) {
            String an = BlockManager.itemIdToName(data.inventoryIds[s]);
            if (an != null) { armor += armorPointsOf(an); toughness += armorToughnessOf(an); }
        }
        lastSentArmorKey = (armor << 8) | toughness;
        sendAttributesUpdate(armor, toughness);
    }

    /** 自身是否亡灵(玩家永远不是; 瞬时效果亡灵反转在此恒为 false, best-effort)。 */
    private boolean isUndeadTarget() {
        return false;
    }

    /** 发送玩家属性(1.21.11 attribute 注册表 id 顺序)。 */
    private void sendAttributesUpdate(int armor, int toughness) {
        // 1.21.11 注册表顺序: 0=armor,1=armor_toughness,2=attack_damage,3=attack_knockback,
        // 4=attack_speed,16=knockback_resistance,17=luck,18=max_absorption,19=max_health,20=movement_speed
        int[] ids = {0, 1, 2, 3, 4, 16, 17, 18, 19, 20};
        double[] vals = {
            (double) armor,                       // 0  armor
            (double) toughness,                    // 1  armor_toughness
            attackDamageValue(),                   // 2  attack_damage (手持武器基础伤害)
            0.0,                                   // 3  attack_knockback
            4.0,                                   // 4  attack_speed (原版基础 4.0)
            0.0,                                   // 16 knockback_resistance
            0.0,                                   // 17 luck
            (double) absorption,                   // 18 max_absorption (吸收心近似同步)
            (double) maxHealth,                    // 19 max_health (受 health_boost 影响)
            0.7                                    // 20 movement_speed
        };
        sendPacket(ctx, 0x81, pb -> {
            pb.writeVarInt(this.eid);
            pb.writeVarInt(ids.length);
            for (int i = 0; i < ids.length; i++) {
                pb.writeVarInt(ids[i]);
                pb.writeDouble(vals[i]);
                pb.writeVarInt(0); // 无 modifier
            }
        });
    }

    /** 当前手持武器的 attack_damage 基础值(用于属性包 id2)。 */
    private float attackDamageValue() {
        int wid = data.inventoryIds[36 + heldItemSlot];
        String wn = wid > 0 ? BlockManager.itemIdToName(wid) : null;
        return getAttackDamage(wn);
    }

    public void sendExperienceUpdate() {
        // set_experience = 0x65 (0x66=update_health —— 曾误用 0x66 导致经验包被当血量包解码,
        // 捡经验时客户端越界断线; 0x64=entity_equipment 也不是经验包)。
        sendPacket(ctx, 0x65, pb -> {
            pb.writeFloat(data.xpProgress);
            pb.writeVarInt(data.xpLevel);
            pb.writeVarInt(data.xpTotal);
        });
    }

    public void addExperience(int amount) {
        var expEvent = EVENTS.fire(new PlayerExpChangeEvent(this, amount));
        amount = expEvent.getAmount();
        data.xpTotal += amount;
        int needed = xpForLevel(data.xpLevel);
        while (data.xpTotal >= needed) {
            data.xpTotal -= needed;
            data.xpLevel++;
            needed = xpForLevel(data.xpLevel);
        }
        data.xpProgress = (float) data.xpTotal / (float) needed;
        sendExperienceUpdate();
    }

    private static int xpForLevel(int level) {
        if (level >= 30) return 112 + (level - 30) * 9;
        if (level >= 15) return 37 + (level - 15) * 5;
        return 7 + level * 2;
    }

    /** 铁砧/附魔消耗经验等级(经济正确性)。 */
    private void spendXpLevels(int levels) {
        if (levels <= 0) return;
        int newLevel = Math.max(0, data.xpLevel - levels);
        int total = 0;
        for (int i = 0; i < newLevel; i++) total += xpForLevel(i);
        data.xpLevel = newLevel;
        data.xpTotal = total;
        data.xpProgress = 0.0f;
        sendExperienceUpdate();
    }

    /** 时运加成数量: 二项分布 binomial(fortune+1, 0.5), 允许 0 加成(贴近原版 applyBonus)。 */
    private static int fortuneBonus(int fortune) {
        int trials = fortune + 1;
        int extra = 0;
        java.util.concurrent.ThreadLocalRandom r = java.util.concurrent.ThreadLocalRandom.current();
        for (int i = 0; i < trials; i++) if (r.nextDouble() < 0.5) extra++;
        return extra;
    }

    private static int getOreXp(String blockName) {
        return switch (blockName) {
            case "coal_ore", "deepslate_coal_ore" -> 1 + new java.util.Random().nextInt(2);
            case "diamond_ore", "deepslate_diamond_ore" -> 3 + new java.util.Random().nextInt(5);
            case "emerald_ore", "deepslate_emerald_ore" -> 3 + new java.util.Random().nextInt(7);
            case "lapis_ore", "deepslate_lapis_ore" -> 2 + new java.util.Random().nextInt(5);
            case "nether_quartz_ore" -> 2 + new java.util.Random().nextInt(4);
            case "redstone_ore", "deepslate_redstone_ore" -> 1 + new java.util.Random().nextInt(5);
            case "nether_gold_ore" -> 1;
            default -> 0;
        };
    }

    private static String getBlockDropItem(String blockName) {
        return switch (blockName) {
            case "stone" -> "cobblestone";
            case "grass_block" -> "dirt";
            case "coal_ore" -> "coal";
            case "deepslate_coal_ore" -> "coal";
            case "diamond_ore" -> "diamond";
            case "deepslate_diamond_ore" -> "diamond";
            case "emerald_ore" -> "emerald";
            case "deepslate_emerald_ore" -> "emerald";
            case "redstone_ore" -> "redstone";
            case "deepslate_redstone_ore" -> "redstone";
            case "lapis_ore" -> "lapis_lazuli";
            case "deepslate_lapis_ore" -> "lapis_lazuli";
            case "nether_quartz_ore" -> "quartz";
            case "nether_gold_ore" -> "gold_nugget";
            case "iron_ore" -> "raw_iron";
            case "deepslate_iron_ore" -> "raw_iron";
            case "gold_ore" -> "raw_gold";
            case "deepslate_gold_ore" -> "raw_gold";
            case "copper_ore" -> "raw_copper";
            case "deepslate_copper_ore" -> "raw_copper";
            case "ancient_debris" -> "netherite_scrap";
            case "snow_block" -> "snowball";
            case "clay" -> "clay_ball";
            case "stone_bricks" -> "stone_bricks";
            case "wheat" -> "wheat";
            case "beetroots" -> "beetroot";
            case "carrots" -> "carrot";
            case "potatoes" -> "potato";
            case "melon" -> "melon_slice";
            case "bookshelf" -> "book";
            case "ice" -> "air";
            case "blue_ice" -> "blue_ice";
            case "packed_ice" -> "packed_ice";
            case "glass", "glass_pane", "white_stained_glass" -> "air";
            case "glowstone" -> "glowstone_dust";
            case "sea_lantern" -> "prismarine_crystals";
            case "leaves", "oak_leaves", "birch_leaves", "spruce_leaves",
                 "jungle_leaves", "acacia_leaves", "dark_oak_leaves",
                 "mangrove_leaves", "cherry_leaves", "pale_oak_leaves" -> "stick";
            case "grass", "short_grass", "tall_grass", "fern" -> "air";
            case "vine" -> "air";
            case "water", "lava", "fire", "nether_portal", "end_portal" -> "air";
            case "bedrock" -> "air";
            default -> blockName;
        };
    }

    private static boolean isLeaves(String blockName) {
        return blockName.endsWith("_leaves");
    }

    private static String leafToSapling(String blockName) {
        return switch (blockName) {
            case "oak_leaves" -> "oak_sapling";
            case "birch_leaves" -> "birch_sapling";
            case "spruce_leaves" -> "spruce_sapling";
            case "jungle_leaves" -> "jungle_sapling";
            case "acacia_leaves" -> "acacia_sapling";
            case "dark_oak_leaves" -> "dark_oak_sapling";
            case "mangrove_leaves" -> "mangrove_propagule";
            case "cherry_leaves" -> "cherry_sapling";
            case "pale_oak_leaves" -> "pale_oak_sapling";
            default -> null;
        };
    }

    /** 树叶掉落：精准采集(silk)已在调用方处理为掉自身；此处处理树苗/苹果概率。 */
    private static int[] rollLeavesDrop(String blockName, int fortune) {
        java.util.concurrent.ThreadLocalRandom r = java.util.concurrent.ThreadLocalRandom.current();
        int saplingChance = 5 * (fortune + 1); // 树苗基础 5%，每级时运 +5%
        String sapling = leafToSapling(blockName);
        if (sapling != null && r.nextInt(100) < saplingChance) {
            int id = BlockManager.getItemIdByName(sapling);
            if (id > 0) return new int[] { id, 1 };
        }
        // 苹果：橡木 0.5%×(fortune+1)，深色橡木 2%×(fortune+1)
        if (blockName.equals("oak_leaves") && r.nextInt(200) < (fortune + 1)) {
            int id = BlockManager.getItemIdByName("apple");
            if (id > 0) return new int[] { id, 1 };
        } else if (blockName.equals("dark_oak_leaves") && r.nextInt(100) < 2 * (fortune + 1)) {
            int id = BlockManager.getItemIdByName("apple");
            if (id > 0) return new int[] { id, 1 };
        }
        return new int[] { 0, 0 };
    }

    /** 原版主手基础攻击伤害 (含空手 1.0)。 */
    private static float getAttackDamage(String item) {
        if (item == null) return 1.0f;
        String n = item.startsWith("minecraft:") ? item.substring(10) : item;
        return switch (n) {
            case "wooden_sword", "golden_sword" -> 4.0f;
            case "stone_sword" -> 5.0f;
            case "iron_sword" -> 6.0f;
            case "diamond_sword" -> 7.0f;
            case "netherite_sword" -> 8.0f;
            case "wooden_axe", "golden_axe" -> 7.0f;
            case "stone_axe" -> 9.0f;
            case "iron_axe", "diamond_axe" -> 9.0f;
            case "netherite_axe" -> 10.0f;
            case "wooden_pickaxe", "golden_pickaxe" -> 2.0f;
            case "stone_pickaxe" -> 3.0f;
            case "iron_pickaxe" -> 4.0f;
            case "diamond_pickaxe" -> 5.0f;
            case "netherite_pickaxe" -> 6.0f;
            case "wooden_shovel", "golden_shovel" -> 2.5f;
            case "stone_shovel" -> 3.5f;
            case "iron_shovel" -> 4.5f;
            case "diamond_shovel" -> 5.5f;
            case "netherite_shovel" -> 6.5f;
            case "trident" -> 9.0f;
            case "mace" -> 7.0f;
            default -> 1.0f;
        };
    }

    /** 攻击冷却毫秒 = 1000 / attackSpeed。 */
    private static float getAttackCooldownMs(String item) {
        if (item == null) return 250.0f; // 4.0 attack speed
        String n = item.startsWith("minecraft:") ? item.substring(10) : item;
        if (n.endsWith("_sword")) return 625.0f;   // 1.6
        if (n.endsWith("_axe")) {
            if (n.startsWith("wooden") || n.startsWith("stone")) return 1250.0f; // 0.8
            return 1000.0f;                        // 1.0
        }
        if (n.endsWith("_pickaxe")) return 833.0f; // 1.2
        if (n.endsWith("_shovel")) return 1000.0f; // 1.0
        if (n.endsWith("_hoe")) return 250.0f;
        if (n.equals("trident")) return 909.0f;    // 1.1
        if (n.equals("mace")) return 1666.0f;      // 0.6
        return 250.0f;
    }

    private static int getMobXp(Entity entity) {
        // P4-2: 统一经验来源 —— 一律以 MobEntity.getXpDrop() 为准, 避免与原两套 XP 表不一致
        if (entity instanceof MobEntity mob) {
            return mob.getXpDrop();
        }
        return 0;
    }

    /**
     * 原版 Inventory 的槽位遍历顺序是 0..35，其中 0-8 是快捷栏。
     * 协议槽位里快捷栏是 36-44、主背包是 9-35，所以拾取/给予必须先填 36-44 再填 9-35。
     */
    private static final int[] PICKUP_SLOT_ORDER = buildPickupOrder();

    private static int[] buildPickupOrder() {
        int[] order = new int[36];
        int k = 0;
        for (int i = 36; i <= 44; i++) order[k++] = i; // 快捷栏
        for (int i = 9; i <= 35; i++) order[k++] = i;  // 主背包
        return order;
    }

    public void giveItem(int itemId, int count) {
        int max = getMaxStackSize(itemId);
        if (max <= 0) max = 64;
        String iname = BlockManager.itemIdToName(itemId);
        if (iname != null) StatisticsManager.add(this, "picked_up", iname, count);
        int given = 0;
        for (int i : PICKUP_SLOT_ORDER) {
            if (count <= 0) break;
            if (data.inventoryIds[i] == itemId && data.inventoryCounts[i] > 0
                    && data.inventoryCounts[i] < max) {
                int add = Math.min(max - data.inventoryCounts[i], count);
                data.inventoryCounts[i] += add;
                count -= add;
                given += add;
            }
        }
        for (int i : PICKUP_SLOT_ORDER) {
            if (count <= 0) break;
            if (data.inventoryIds[i] == 0 || data.inventoryCounts[i] == 0) {
                int add = Math.min(max, count);
                data.inventoryIds[i] = itemId;
                data.inventoryCounts[i] = add;
                count -= add;
                given += add;
            }
        }
        sendInventoryUpdate();
        sendHealthUpdate();
        sendExperienceUpdate();
        if (given > 0 && ctx != null) {
            sendSoundAt("minecraft:entity.item.pickup", this.x, this.y + 1.0, this.z, 0.3f, 1.0f);
        }
    }

    /** 给物品(含附魔/药水/改名/耐久等组件数据, 供铁砧关闭退回等场景保留 NBT)。 */
    private int giveItemWithData(int itemId, int count, java.util.Map<Integer, Integer> enchants,
                                 String potion, String name, int damage) {
        int max = Math.max(1, BlockManager.getStackSize(itemId));
        ItemMeta in = ItemMeta.of(enchants, potion, name, damage, -1, -1);
        int left = count;
        if (in.isEmpty()) {
            // Bug47: 无组件物品先并堆, 且目标槽必须同样无组件
            for (int i : PICKUP_SLOT_ORDER) {
                if (left <= 0) break;
                if (data.inventoryIds[i] == itemId && data.inventoryCounts[i] > 0
                        && data.inventoryCounts[i] < max && playerSlotMeta(i).isEmpty()) {
                    int add = Math.min(max - data.inventoryCounts[i], left);
                    data.inventoryCounts[i] += add;
                    left -= add;
                    sendSlotUpdate(0, i);
                }
            }
        }
        int slot = -1;
        for (int i : PICKUP_SLOT_ORDER) {
            if (left <= 0) break;
            if (data.inventoryIds[i] == 0 || data.inventoryCounts[i] <= 0) { slot = i; break; }
        }
        if (left <= 0) return count;
        if (slot < 0) {
            ItemEntity it = new ItemEntity(EntityManager.allocateId(),
                this.x, this.y + 0.5, this.z, itemId, left);
            it.dim = this.currentDim;
            it.vx = 0; it.vy = 0.15; it.vz = 0;
            it.pickupDelay = 20;
            if (!in.isEmpty()) {
                it.itemEnchants = in.enchants().isEmpty() ? null : new java.util.HashMap<>(in.enchants());
                it.itemPotion = in.potion();
                it.itemCustomName = in.customName();
                it.itemDamage = in.damage();
            }
            EntityManager.addEntity(it);
            return count - left;
        }
        data.inventoryIds[slot] = itemId;
        data.inventoryCounts[slot] = Math.min(max, left);
        writePlayerSlotMeta(slot, in);
        sendSlotUpdate(0, slot);
        return slot;
    }

    /** 抛竿(无浮标时)或收竿(已有浮标时, 若已上钩则结算战利品)。 */
    private void toggleFishing() {
        if (fishingBobberEid >= 0) {
            Entity bob =
                EntityManager.getEntities().get(fishingBobberEid);
            if (bob instanceof FishingBobberEntity fbe) {
                if (fbe.ready) grantFishingLoot();
                EntityManager.removeEntity(fbe.id);
                for (NetworkHandler p : players.values()) {
                    if (p.ctx == null) continue;
                    p.sendPacket(p.ctx, 0x4B, pb -> {
                        pb.writeVarInt(1);
                        pb.writeVarInt(fbe.id);
                    });
                }
            }
            fishingBobberEid = -1;
            return;
        }
        // 抛竿: 在玩家前方 3 格、视线高度处生成浮标
        double dx = -Math.sin(Math.toRadians(yaw));
        double dz = Math.cos(Math.toRadians(yaw));
        double bx = x + dx * 3.0;
        double bz = z + dz * 3.0;
        double by = y + 1.0;
        FishingBobberEntity bob =
            new FishingBobberEntity(
                EntityManager.allocateId(), bx, by, bz);
        bob.ownerEid = this.eid;
        bob.dim = this.currentDim;
        bob.hookTicks = 100 + (int) (Math.random() * 500); // 5~35 秒后上钩
        EntityManager.addEntity(bob);
        fishingBobberEid = bob.id;
        sendSoundAt("minecraft:entity.fishing_bobber.throw", x, y + 1.0, z, 0.5f, 1.0f);
    }

    /** 结算钓鱼战利品(简化原版 loot table: 鱼 65% / 垃圾 30% / 宝藏 5%)。 */
    private void grantFishingLoot() {
        double r = Math.random();
        String item;
        if (r < 0.05) {
            String[] treasure = {"bow", "enchanted_book", "name_tag", "nautilus_shell", "saddle"};
            item = treasure[(int) (Math.random() * treasure.length)];
        } else if (r < 0.35) {
            String[] junk = {"stick", "bone", "string", "ink_sac", "leather", "bowl",
                             "rotten_flesh", "paper", "glass_bottle"};
            item = junk[(int) (Math.random() * junk.length)];
        } else {
            String[] fish = {"cod", "salmon", "tropical_fish", "pufferfish"};
            item = fish[(int) (Math.random() * fish.length)];
        }
        int id = BlockManager.getItemIdByName(item);
        if (id > 0) {
            giveItem(id, 1);
            sendSoundAt("minecraft:entity.fishing_bobber.splash", x, y + 1.0, z, 0.5f, 1.0f);
        }
    }

    private boolean isAlwaysEdible(String itemName) {
        return itemName.equals("golden_apple") || itemName.equals("enchanted_golden_apple")
            || itemName.equals("chorus_fruit") || itemName.equals("honey_bottle")
            || itemName.equals("milk_bucket");
    }

    private float getSaturationModifier(String itemName) {
        return switch (itemName) {
            case "golden_apple", "enchanted_golden_apple", "golden_carrot" -> 2.4f;
            case "cooked_beef", "cooked_porkchop", "cooked_mutton", "cooked_salmon" -> 1.6f;
            case "cooked_chicken", "cooked_rabbit", "cooked_cod", "baked_potato",
                 "bread", "beetroot_soup", "mushroom_stew", "rabbit_stew", "pumpkin_pie" -> 1.2f;
            case "apple", "carrot", "melon_slice", "cookie", "beetroot",
                 "sweet_berries", "glow_berries", "dried_kelp", "chorus_fruit" -> 0.6f;
            case "beef", "porkchop", "mutton", "chicken", "rabbit",
                 "cod", "salmon", "tropical_fish", "potato" -> 0.6f;
            case "rotten_flesh", "spider_eye" -> 0.2f;
            default -> 0.6f;
        };
    }

    private int getFoodValue(String itemName) {
        Integer p = FOOD_POINTS.get(itemName);
        if (p != null) return p;
        return switch (itemName) {
            case "apple" -> 4;
            case "golden_apple" -> 8;
            case "enchanted_golden_apple" -> 8;
            case "bread" -> 5;
            case "cooked_beef", "cooked_porkchop", "cooked_mutton", "cooked_chicken", "cooked_rabbit" -> 6;
            case "beef", "porkchop", "mutton", "chicken", "rabbit" -> 3;
            case "cooked_cod", "cooked_salmon" -> 5;
            case "cod", "salmon", "tropical_fish" -> 2;
            case "carrot" -> 3;
            case "golden_carrot" -> 6;
            case "potato" -> 1;
            case "baked_potato" -> 5;
            case "beetroot" -> 1;
            case "beetroot_soup" -> 6;
            case "mushroom_stew" -> 6;
            case "rabbit_stew" -> 10;
            case "cookie" -> 2;
            case "melon_slice" -> 2;
            case "dried_kelp" -> 1;
            case "sweet_berries" -> 2;
            case "glow_berries" -> 2;
            case "cooked_honey" -> 6;
            case "pumpkin_pie" -> 8;
            case "chorus_fruit" -> 4;
            case "spider_eye" -> 2;
            case "rotten_flesh" -> 4;
            case "milk_bucket" -> 0;
            default -> 0;
        };
    }

    /** 紫颂果: 生存 100% 随机传送到附近安全位置(原版外岛核心机动)。 */
    private void chorusFruitTeleport() {
        if (gameMode == 1 || gameMode == 3) return; // 创造/旁观不传送
        java.util.concurrent.ThreadLocalRandom r = java.util.concurrent.ThreadLocalRandom.current();
        double nx = x + (r.nextDouble() * 16.0 - 8.0);
        double nz = z + (r.nextDouble() * 16.0 - 8.0);
        int bx = (int) Math.floor(nx), bz = (int) Math.floor(nz);
        int foundY = Integer.MIN_VALUE;
        for (int dy = -8; dy <= 8; dy++) {
            int ty = (int) Math.floor(y) + dy;
            if (ty < -63 || ty > 319) continue;
            String here = BlockStateHelper.getName(
                WorldManager.getBlockState(currentDim, bx, ty, bz));
            String above = BlockStateHelper.getName(
                WorldManager.getBlockState(currentDim, bx, ty + 1, bz));
            String below = BlockStateHelper.getName(
                WorldManager.getBlockState(currentDim, bx, ty - 1, bz));
            if ("air".equals(here) && "air".equals(above) && !"air".equals(below)) {
                foundY = ty;
                break;
            }
        }
        if (foundY == Integer.MIN_VALUE) foundY = (int) findSafeArrivalY(currentDim, bx, bz);
        teleportPlayer(this, nx, (double) foundY, nz);
    }

    public void teleportToDimension(io.netty.channel.ChannelHandlerContext ctx,
                                      DimensionType targetDim) {
        teleportToDimension(ctx, targetDim, false);
    }

    public void teleportToDimension(io.netty.channel.ChannelHandlerContext ctx,
                                      DimensionType targetDim,
                                      boolean buildReturnPortal) {
        DimensionType fromDim = this.currentDim;
        if (fromDim == targetDim) return;

        double scale = 1.0;
        if (targetDim == DimensionType.THE_NETHER
                && fromDim == DimensionType.OVERWORLD) {
            scale = 1.0 / 8.0;
        } else if (fromDim == DimensionType.THE_NETHER
                && targetDim == DimensionType.OVERWORLD) {
            scale = 8.0;
        }
        int targetX = (int) Math.round(this.x * scale);
        int targetZ = (int) Math.round(this.z * scale);
        if (targetDim == DimensionType.THE_END) {
            targetX = 100; targetZ = 0;
        } else if (fromDim == DimensionType.THE_END) {
            // Bug58: 从末地返回主世界 —— 原版回到玩家重生点(床/重生锚), 无则回到世界出生点。
            // 曾硬编码 (0,0) -> 出现在出生点之外的随机位置(那里可能恰好有之前建造的平台)。
            if (data.respawnY != Integer.MIN_VALUE) {
                targetX = data.respawnX; targetZ = data.respawnZ;
            } else {
                double[] sp = WorldManager.resolveWorldSpawn();
                targetX = (int) sp[0]; targetZ = (int) sp[2];
            }
        }

        String dimName = targetDim.key;

        EntityManager.onPlayerChangeDimension(this);

        int endPlatformY = -1;
        if (targetDim == DimensionType.THE_END) {
            EndDragonFight.onPlayerEnterEnd(this);
            endPlatformY = EndDragonFight.createObsidianPlatform();
        }

        // Respawn (0x50) = CommonPlayerSpawnInfo + dataKept byte
        sendPacket(ctx, 0x50, pb -> {
            pb.writeVarInt(targetDim.registryId);   // dimension type registry index
            pb.writeString(dimName);                // dimension name
            pb.writeLong(WorldManager.getSeed());   // hashed seed
            pb.writeByte((byte) this.gameMode);     // game mode
            pb.writeByte((byte) -1);                // previous game mode
            pb.writeBoolean(false);                 // is debug
            pb.writeBoolean(false);                 // is flat
            pb.writeBoolean(false);                 // has death location
            pb.writeVarInt(300);                    // portal cooldown
            pb.writeVarInt(targetDim.seaLevel);     // sea level
            pb.writeByte((byte) 0x03);              // dataKept = KEEP_ALL_DATA (attributes|metadata)
        });

        this.currentDim = targetDim;
        // Bug1: 维度切换后客户端重建 LocalPlayer, 权限等级(F3+F4/命令方块编辑)丢失。
        // 与登录时一样补发 entity_event 24+opLevel, 并在 1 秒后兜底重发一次。
        final int dimOpLevel = Math.max(0, Math.min(4, opLevel()));
        sendPacket(ctx, 0x22, pb -> {
            pb.writeInt(eid);
            pb.writeByte((byte) (24 + dimOpLevel));
        });
        // Bug1 强化: 与重生路径保持一致 —— abilities / player_info_update(ADD|GAMEMODE|LISTED) /
        // game_state(3) / declare_commands 都要重发。1.21.5+ 客户端权限集由命令树派生,
        // LocalPlayer.gamemode 由 player_info/game_state 刷新; 只发 entity_event 不够。
        sendAbilitiesUpdate();
        final int dimGm = this.gameMode;
        for (NetworkHandler h : players.values()) {
            if (h.ctx == null || !h.ctx.channel().isActive()) continue;
            h.sendPacket(h.ctx, 0x44, pb -> {
                pb.writeByte(0x01 | 0x04 | 0x08);
                pb.writeVarInt(1); pb.writeUUID(this.uuid); pb.writeString(this.username);
                writeProfileProperties(pb, this.profileProperties);
                pb.writeVarInt(dimGm);
                pb.writeBoolean(true);
            });
        }
        sendPacket(ctx, 0x26, pb -> { pb.writeByte(3); pb.writeFloat(dimGm); });
        sendCommandsPacket(ctx);
        final ChannelHandlerContext dimCtx = ctx;
        ctx.executor().schedule(() -> {
            if (dimCtx.channel().isActive() && !isDead && this.currentDim == targetDim) {
                int lvl = Math.max(0, Math.min(4, opLevel()));
                sendPacket(dimCtx, 0x22, pb -> {
                    pb.writeInt(this.eid);
                    pb.writeByte((byte) (24 + lvl));
                });
            }
        }, 1, java.util.concurrent.TimeUnit.SECONDS);
        // 成就系统：进入维度事件 (P12)
        AdvancementManager.onEnterDimension(this, dimName);
        EVENTS.fire(new com.CharunCore.server.plugin.event.events.PlayerChangedWorldEvent(this, dimName));
        this.x = targetX + 0.5;
        this.z = targetZ + 0.5;
        this.y = endPlatformY > 0
                ? endPlatformY
                : findSafeArrivalY(targetDim, targetX, targetZ);
        this.fallDistance = 0.0f;
        this.isDead = false;
        this.portalTimer = -300;

        if (buildReturnPortal) {
            int[] existing = findNearbyPortal(targetDim, targetX, (int) this.y, targetZ, 64);
            int px = targetX, pz = targetZ, py = (int) this.y;
            if (existing != null) {
                px = existing[0]; pz = existing[2]; py = existing[1];
            } else {
                buildNetherPortalFrame(targetDim, targetX, (int) this.y, targetZ);
            }
            // 把玩家放在传送门"上方"，避免落在门内的传送门方块里被立即再次判定传送
            // 玩家落在传送门方块上(门内), 才能正常踩门触发回主世界; 落地冷却由 portalTimer=-300 提供
            this.x = px + 0.5;
            this.z = pz + 0.5;
            this.y = py + 1.0;
            this.z = pz + 0.5;
        }

        int tpId = ++this.teleportIdCounter;
        sendPacket(ctx, 0x46, pb -> {
            pb.writeVarInt(tpId);
            pb.writeDouble(this.x);
            pb.writeDouble(this.y);
            pb.writeDouble(this.z);
            pb.writeDouble(0);
            pb.writeDouble(0);
            pb.writeDouble(0);
            pb.writeFloat(this.yaw);
            pb.writeFloat(this.pitch);
            pb.writeInt(0);
        });

        // 关键修复：维度切换/重生后必须发送 Game Event 13 (LEVEL_CHUNKS_LOAD_START)，
        // 否则客户端 ClientLevel 的 LevelLoadStatusManager 不会进入“等待玩家区块编译”状态，
        // 加载地形界面永远不会关闭（只能重进服务器才能看到地形）。
        // 登录流程在 sendLoginPlay 末尾已发送同样的 0x26 包，所以登录正常、传送卡住。
        // 包格式：writeByte(event=13) + writeFloat(param=0.0f)（参见 ClientboundGameEventPacket）。
        sendPacket(ctx, 0x26, pb -> { pb.writeByte(13); pb.writeFloat(0.0f); });
        // SetTime：使用真实世界时间(原版 sendLevelInfo); 末地 fixedTime=18000 锁定夜晚
        long age = Main.worldAge;
        long time = (targetDim == DimensionType.THE_END)
            ? 18000L : Main.dayTime;
        sendPacket(ctx, 0x6F, pb -> { pb.writeLong(age); pb.writeLong(time); pb.writeBoolean(true); });

        loadedChunks.clear();
        resetChunkSendQueue();
        ctx.executor().execute(() -> sendInitialChunks(ctx));
    }

    /**
     * End gateway travel: main island -> outer end island (and back).
     * Vanilla places the arrival platform at the target and links the two gateways;
     * here we use one fixed outer-island landing spot with a return gateway next to it.
     */
    private void teleportViaEndGateway(io.netty.channel.ChannelHandlerContext ctx) {
        DimensionType end = DimensionType.THE_END;
        if (this.currentDim != end) return;

        boolean onMainIsland = Math.abs(this.x) < 256 && Math.abs(this.z) < 256;
        int tx, tz;
        if (onMainIsland) {
            tx = 1000; tz = 0;
        } else {
            tx = 0; tz = 0;
        }

        int landY;
        if (onMainIsland) {
            landY = buildGatewayLandingPlatform(end, tx, tz);
        } else {
            landY = EndDragonFight.getPodiumY() + 1;
            // 主岛落点不能落在传送门方块里, 稍微偏出中心
            tx = 4; tz = 0;
        }

        this.x = tx + 0.5;
        this.y = landY;
        this.z = tz + 0.5;
        this.fallDistance = 0.0f;

        int tpId = ++this.teleportIdCounter;
        sendPacket(ctx, 0x46, pb -> {
            pb.writeVarInt(tpId);
            pb.writeDouble(this.x);
            pb.writeDouble(this.y);
            pb.writeDouble(this.z);
            pb.writeDouble(0); pb.writeDouble(0); pb.writeDouble(0);
            pb.writeFloat(this.yaw);
            pb.writeFloat(this.pitch);
            pb.writeInt(0);
        });
        sendSoundAt("minecraft:block.end_gateway.spawn", this.x, this.y, this.z, 1.0f, 1.0f);

        resetChunkSendQueue();
        ctx.executor().execute(() -> sendInitialChunks(ctx));
    }

    /**
     * Ensure the outer-island arrival area is safe: a 5x5 end_stone pad plus a return gateway.
     * Returns the Y the player should stand on.
     */
    private int buildGatewayLandingPlatform(DimensionType dim, int cx, int cz) {
        int endStone = BlockStateHelper.getDefault("end_stone");
        int bedrock = BlockStateHelper.getDefault("bedrock");
        int gateway = BlockStateHelper.getDefault("end_gateway");

        int surface = -1;
        for (int y = dim.minY + dim.height - 2; y > dim.minY; y--) {
            if (WorldManager.getBlockState(dim, cx, y, cz) != 0) { surface = y; break; }
        }
        int padY = surface >= 0 ? surface : 74;

        if (surface < 0) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    WorldManager.setBlock(dim, cx + dx, padY, cz + dz, endStone);
                }
            }
        }
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                for (int dy = 1; dy <= 3; dy++) {
                    if (WorldManager.getBlockState(dim, cx + dx, padY + dy, cz + dz) != 0) {
                        WorldManager.setBlock(dim, cx + dx, padY + dy, cz + dz, 0);
                    }
                }
            }
        }

        // 返程网关: 悬在落点旁边的基岩座上
        int gx = cx + 4, gy = padY + 3, gz = cz;
        if (gateway > 0
                && !"end_gateway".equals(BlockStateHelper.getName(
                        WorldManager.getBlockState(dim, gx, gy, gz)))) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dz == 0 && dy == 0) continue;
                        WorldManager.setBlock(dim, gx + dx, gy + dy, gz + dz, bedrock);
                    }
                }
            }
            WorldManager.setBlock(dim, gx, gy, gz, gateway);
        }
        return padY + 1;
    }

    /** Search for an existing nether_portal block within a cube radius. Returns {x,y,z} or null. */
    private int[] findNearbyPortal(DimensionType dim, int cx, int cy, int cz, int radius) {
        int portalId = BlockStateHelper.getDefault("nether_portal");
        int best = Integer.MAX_VALUE;
        int[] found = null;
        for (int dx = -radius; dx <= radius; dx += 1) {
            for (int dz = -radius; dz <= radius; dz += 1) {
                for (int dy = -32; dy <= 32; dy++) {
                    int bx = cx + dx, by = cy + dy, bz = cz + dz;
                    if (by <= dim.minY || by >= dim.minY + dim.height - 1) continue;
                    int st = WorldManager.getBlockState(dim, bx, by, bz);
                    if (st == 0) continue;
                    if (!"nether_portal".equals(BlockStateHelper.getName(st))) continue;
                    int d = dx * dx + dy * dy + dz * dz;
                    if (d < best) { best = d; found = new int[]{bx, by, bz}; }
                }
            }
        }
        return found;
    }

    /** Build a 4x5 obsidian frame with a lit portal interior, oriented on the X axis. */
    private void buildNetherPortalFrame(DimensionType dim, int bx, int by, int bz) {
        int obsidian = BlockStateHelper.getDefault("obsidian");
        int portal = BlockStateHelper.withProp(
                BlockStateHelper.getDefault("nether_portal"), "axis", "x");

        for (int ox = -1; ox <= 2; ox++) {
            for (int oz = -1; oz <= 1; oz++) {
                WorldManager.setBlock(dim, bx + ox, by - 1, bz + oz, obsidian);
            }
        }
        for (int ox = -1; ox <= 2; ox++) {
            for (int oy = 0; oy <= 4; oy++) {
                boolean edge = (ox == -1 || ox == 2 || oy == 4);
                int state = edge ? obsidian : portal;
                if (!edge) {
                    WorldManager.setBlock(dim, bx + ox, by + oy, bz - 1, 0);
                    WorldManager.setBlock(dim, bx + ox, by + oy, bz + 1, 0);
                }
                WorldManager.setBlock(dim, bx + ox, by + oy, bz, state);
                broadcastBlockChange(dim, bx + ox, by + oy, bz, state);
            }
        }
    }

    /** Find a safe Y to arrive at: 2 air blocks above a solid floor, building a platform if needed. */
    private double findSafeArrivalY(DimensionType dim, int bx, int bz) {
        int top = dim == DimensionType.THE_NETHER ? 120 : dim.minY + dim.height - 2;
        int bottom = dim.minY + 1;
        for (int yy = top; yy > bottom; yy--) {
            int floor = WorldManager.getBlockState(dim, bx, yy - 1, bz);
            if (floor == 0) continue;
            String fn = BlockStateHelper.getName(floor);
            if (fn != null && (fn.contains("water") || fn.contains("lava"))) continue;
            if (WorldManager.getBlockState(dim, bx, yy, bz) == 0
                    && WorldManager.getBlockState(dim, bx, yy + 1, bz) == 0) {
                return yy;
            }
        }
        int platformY = dim == DimensionType.THE_END ? 64
                : (dim == DimensionType.THE_NETHER ? 70 : 70);
        int plat = BlockStateHelper.getDefault(
                dim == DimensionType.THE_END ? "obsidian" : "stone");
        for (int ox = -2; ox <= 2; ox++) {
            for (int oz = -2; oz <= 2; oz++) {
                WorldManager.setBlock(dim, bx + ox, platformY - 1, bz + oz, plat);
                WorldManager.setBlock(dim, bx + ox, platformY, bz + oz, 0);
                WorldManager.setBlock(dim, bx + ox, platformY + 1, bz + oz, 0);
            }
        }
        return platformY;
    }

    /** Broadcast a player chat message (profileless_chat 0x21) to ALL online players. */
    private void broadcastPlayerMessage(String rawMsg) {
        org.cloudburstmc.nbt.NbtMap msgComp = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", rawMsg)
                .build();
        org.cloudburstmc.nbt.NbtMap contents = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("type", "minecraft:player")
                .putString("id", this.uuid.toString())
                .putString("name", this.username)
                .build();
        org.cloudburstmc.nbt.NbtMap hoverEvent = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("action", "show_entity")
                .putCompound("contents", contents)
                .build();
        org.cloudburstmc.nbt.NbtMap nameComp = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", this.username)
                .putString("insertion", this.username)
                .putCompound("hoverEvent", hoverEvent)
                .build();
        for (NetworkHandler p : players.values()) {
            if (p.ctx == null) continue;
            p.sendPacket(p.ctx, 0x21, pb -> {
                pb.writeAnonymousNbt(msgComp);   // message
                pb.writeVarInt(1);                // ChatType.Bound holder: varint(registryId+1), chat=0 → 1
                pb.writeAnonymousNbt(nameComp);   // name
                pb.writeBoolean(false);            // no target
            });
        }
    }

    private void broadcastMetadata() {
        for (NetworkHandler h : players.values()) {
            if (h == this || h.ctx == null) continue;
            h.sendPacket(h.ctx, 0x61, pb -> { // entity_metadata/set_entity_data=0x61 (0x62=attach_entity/set_entity_link 栓绳包,固定 8 字节 2×i32; 曾误用 0x62 -> eid 大于 127 时载荷超出 8 字节, 客户端报 "N bytes extra" 断线)
                pb.writeVarInt(this.eid);

                // Index 0: Entity Flags (type 0 = byte)
                pb.writeByte(0);
                pb.writeVarInt(0); // type = byte
                pb.writeByte(this.isSneaking ? (byte) 0x02 : (byte) 0x00);

                // Index 6: Pose (type 20 = pose)
                pb.writeByte(6);
                pb.writeVarInt(20); // type = pose
                pb.writeVarInt(this.isSneaking ? 5 : 0); // 5=SNEAKING, 0=STANDING

                pb.writeByte(0xFF); // end marker
            });
        }
    }

    // =========================================================================
    // CHAT BROADCAST
    // =========================================================================

    /**
     * Broadcasts a system chat message (0x77) to ALL online players.
     *
     * FIX: previously sent the server-status JSON string via writeString().
     * The System Chat Message "content" field is anonymousNbt (compound payload,
     * no type byte or name prefix). writeAnonymousNbt() writes the correct format.
     */
    public static void broadcastSystemMessage(String text, String color) {
        org.cloudburstmc.nbt.NbtMap comp = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", text)
                .putString("color", color)
                .build();
        for (NetworkHandler p : players.values()) {
            if (p.ctx == null) continue;
            p.sendPacket(p.ctx, 0x77, pb -> {
                pb.writeAnonymousNbt(comp);  // ← anonymousNbt, NOT writeString/writeNbt
                pb.writeBoolean(false);       // overlay=false → appears in chat bar
            });
        }
    }

    // =========================================================================
    // TIME BROADCAST HELPER
    // =========================================================================

    public static void broadcastTime() {
        for (NetworkHandler p : players.values()) {
            if (p.ctx == null) continue;
            p.sendPacket(p.ctx, 0x6F, pb -> {
                pb.writeLong(Main.worldAge);
                pb.writeLong(Main.dayTime);
                pb.writeBoolean(true);
            });
        }
    }

    // =========================================================================
    // ANIMATION & METADATA BROADCAST
    // =========================================================================

    private void broadcastAnimation(int entityId, int animationId) {
        for (NetworkHandler other : players.values()) {
            if (other == this || other.ctx == null) continue;
            other.sendPacket(other.ctx, 0x02, pb -> {
                pb.writeVarInt(entityId);
                pb.writeByte(animationId);
            });
        }
    }

    /**
     * Update the local sneak state and broadcast entity metadata to others.
     *
     * Two metadata entries are sent:
     *   Index 0 (Entity Flags, type=byte): bit 0x02 = sneaking
     *   Index 6 (Pose, type=VarInt): 0=STANDING, 5=CROUCHING
     *
     * Set Entity Data clientbound ID: 0x61 (0x62=attach_entity/set_entity_link, 曾误用导致断线)。
     * Pose type VarInt ID: 20 (1.21.11 序列化器注册序: BYTE=0..BOOLEAN=8..POSE=20, 服务端 jar 反汇编确认)。
     */
    private void setSneaking(boolean sneak) {
        this.isSneaking = sneak;
        byte flags = sneak ? (byte) 0x02 : (byte) 0x00; // bit 1 = sneaking
        int  pose  = sneak ? 5 : 0;                      // 5=CROUCHING, 0=STANDING

        for (NetworkHandler other : players.values()) {
            if (other == this || other.ctx == null) continue;
            other.sendPacket(other.ctx, 0x61, pb -> { // entity_metadata/set_entity_data=0x61 (0x62=attach_entity/set_entity_link 栓绳包,固定 8 字节 2×i32; 曾误用 0x62 -> eid 大于 127 时载荷超出 8 字节, 客户端报 "N bytes extra" 断线)  // ❓ verify 0x57
                pb.writeVarInt(this.eid);

                // Entry 1: flags byte (index 0, type 0 = byte)
                pb.writeByte(0);   // metadata index
                pb.writeVarInt(0); // type = byte
                pb.writeByte(flags);

                // Entry 2: pose (index 6, type 20 = Pose as VarInt)
                pb.writeByte(6);   // metadata index
                pb.writeVarInt(20); // type = Pose (confirmed: type 20 in protocol 774)
                pb.writeVarInt(pose);

                pb.writeByte(0xFF); // end of metadata
            });
        }
    }

    /** 玩家是否装备鞘翅（胸甲槽 inventoryIds[6] == elytra 注册 id）。 */
    private boolean hasElytraEquipped() {
        return data != null && data.inventoryIds[6] == BlockManager.getItemIdByName("elytra");
    }

    /** 停止鞘翅滑翔并广播元数据给其他玩家。 */
    private void stopElytra() {
        if (!fallFlying) return;
        fallFlying = false;
        broadcastElytra();
    }

    /** 广播鞘翅/潜行实体元数据（index 0 flags + index 6 pose）给其他玩家。 */
    private void broadcastElytra() {
        final byte flags = (byte) ((isSneaking ? 0x02 : 0) | (fallFlying ? 0x80 : 0)); // FALL_FLYING = bit 7
        int pose = isSneaking ? 5 : 0;
        for (NetworkHandler other : players.values()) {
            if (other == this || other.ctx == null) continue;
            other.sendPacket(other.ctx, 0x61, pb -> { // entity_metadata/set_entity_data=0x61 (0x62=attach_entity/set_entity_link 栓绳包,固定 8 字节 2×i32; 曾误用 0x62 -> eid 大于 127 时载荷超出 8 字节, 客户端报 "N bytes extra" 断线)
                pb.writeVarInt(this.eid);
                pb.writeByte(0);      // metadata index 0
                pb.writeVarInt(0);    // type = byte
                pb.writeByte(flags);
                pb.writeByte(6);      // metadata index 6 (pose)
                pb.writeVarInt(20);   // type = Pose
                pb.writeVarInt(pose);
                pb.writeByte(0xFF);   // end of metadata
            });
        }
    }

    // =========================================================================
    // MOVEMENT BROADCAST
    // =========================================================================

    private void handleMove() {
        if (Math.abs(x - lastX) > 1e-9 || Math.abs(y - lastY) > 1e-9 || Math.abs(z - lastZ) > 1e-9) {
            var moveEvent = EVENTS.fire(new PlayerMoveEvent(
                    this, lastX, lastY, lastZ, x, y, z));
            if (moveEvent.isCancelled()) {
                x = lastX; y = lastY; z = lastZ;
                sendPacket(ctx, 0x46, pb -> {
                    pb.writeVarInt(1);
                    pb.writeDouble(lastX); pb.writeDouble(lastY); pb.writeDouble(lastZ);
                    pb.writeDouble(0); pb.writeDouble(0); pb.writeDouble(0);
                    pb.writeFloat(yaw); pb.writeFloat(pitch);
                    pb.writeInt(0);
                });
                return;
            }
            if (moveEvent.getToX() != x || moveEvent.getToY() != y || moveEvent.getToZ() != z) {
                x = moveEvent.getToX(); y = moveEvent.getToY(); z = moveEvent.getToZ();
            }
        }
        int curX = ((int) x) >> 4, curZ = ((int) z) >> 4;
        if (curX != lastChunkX || curZ != lastChunkZ) {
            // update_view_position: 通知客户端新的区块中心(移动时必须保留)
            sendPacket(ctx, 0x5C, pb -> { pb.writeVarInt(curX); pb.writeVarInt(curZ); });
            // Bug44: 区块发送改为每 tick 预算泵送(见 tickSurvival 的 pumpChunkSends),
            // 曾一次性把视距内全部区块丢进 IO 线程 -> 每包同步 zlib 压缩 + 完整光照 BFS
            // 造成进服/跨区块后客户端帧率 40-200 剧烈波动。
            int radius = VIEW_DISTANCE;
            java.util.List<long[]> chunkOrder = new java.util.ArrayList<>();
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int nx = curX + dx, nz = curZ + dz;
                    long key = ((long) nx << 32) | (nz & 0xFFFFFFFFL);
                    if (!loadedChunks.contains(key) && !queuedChunkKeys.contains(key)) {
                        chunkOrder.add(new long[]{dx * dx + dz * dz, key, nx, nz});
                    }
                }
            }
            chunkOrder.sort((a, b) -> Long.compare(a[0], b[0]));
            for (long[] entry : chunkOrder) {
                queuedChunkKeys.add(entry[1]);
                pendingChunkSends.add(new long[]{entry[1], entry[2], entry[3]});
            }
            // 注意: 移动时不再发送 chunk_batch_start/finished(0x0C/0x0B),
            // 否则客户端会反复进入"加载地形中"界面而无法退出(该配对仅用于登录/重生初始加载)
            loadedChunks.removeIf(key -> {
                int cx = (int)(key >> 32), cz = (int)(long)(key & 0xFFFFFFFFL);
                if (Math.abs(cx - curX) > VIEW_DISTANCE + 2 || Math.abs(cz - curZ) > VIEW_DISTANCE + 2) {
                    sendPacket(ctx, 0x25, pb -> { pb.writeInt(cx); pb.writeInt(cz); }); // Unload Chunk: X first, then Z
                    return true;
                }
                return false;
            });
            lastChunkX = curX; lastChunkZ = curZ;
        }
        broadcastMove();
    }

    /** Bug44: 每 tick 最多提交 CHUNK_SEND_BUDGET 个区块任务(生成+光照在 IO 线程逐个做,
     *  压缩/发送经 event loop), 摊平进服/跨区块的突发负载。
     *  注意: 生成必须回到 IO 线程 —— getChunk 在世界 tick 线程只读缓存(BUG7 防同步生成),
     *  未加载的区块直接返回 null, 曾在 tick 线程生成导致整个发送队列全部 NPE 失败。 */
    private static final int CHUNK_SEND_BUDGET = 6;

    private void pumpChunkSends() {
        if (pendingChunkSends.isEmpty() || ctx == null || !ctx.channel().isActive()) return;
        // Bug: 远距离 /tp 后提交速度(6/tick=120/s)远超远处区块生成速度(~10/s),
        // ioExecutor 积压曾达 5197 任务 -> 世界长时间空转。积压超阈值时暂停提交,
        // 让已入队区块先消化(区块按距离排序入队, 近处先出)。
        if (WorldManager.ioBacklog() > 64) return;
        int budget = CHUNK_SEND_BUDGET;
        long[] entry;
        while (budget-- > 0 && (entry = pendingChunkSends.poll()) != null) {
            final long key = entry[0];
            final int nx = (int) entry[1], nz = (int) entry[2];
            queuedChunkKeys.remove(key);
            if (loadedChunks.contains(key)) continue;
            // 玩家已走远(卸载圈外) -> 丢弃过期排队项
            if (lastChunkX != Integer.MAX_VALUE
                    && (Math.abs(nx - lastChunkX) > VIEW_DISTANCE + 2 || Math.abs(nz - lastChunkZ) > VIEW_DISTANCE + 2)) {
                continue;
            }
            final DimensionType dim = this.currentDim;
            WorldManager.getIoExecutor().execute(() -> {
                try {
                    Chunk c = WorldManager.getChunk(dim, nx, nz);
                    if (c == null) return;
                    FluidEngine.scheduleChunkFluids(dim, c);
                    loadedChunks.add(key);
                    ctx.executor().execute(() -> {
                        if (ctx.channel().isActive()) {
                            sendPacket(ctx, 0x2C, pb -> ChunkEncoder.writeChunkPacket(pb, c));
                        }
                    });
                } catch (Exception e) {
                    System.err.println("[区块] 生成异常 (" + nx + "," + nz + "): " + e.getMessage());
                }
            });
        }
    }

    /** 维度切换/重生/登录时清空区块发送队列并复位扫描中心,
     *  防止旧维度的排队条目被当作新维度的坐标生成(浪费)或被距离检查误杀(缺区块)。 */
    private void resetChunkSendQueue() {
        pendingChunkSends.clear();
        queuedChunkKeys.clear();
        lastChunkX = Integer.MAX_VALUE;
        lastChunkZ = Integer.MAX_VALUE;
    }

    /** Broadcast position + head rotation to all other online players. */
    private void broadcastMove() {
        // 旁观者不向其他玩家广播移动(隐形, P2-6)
        if (this.gameMode == 3) return;
        for (NetworkHandler h : players.values()) {
            if (h == this || h.ctx == null) continue;

            // Entity Position Sync (0x23): absolute position + look angles (f32)
            h.sendPacket(h.ctx, 0x23, pb -> {
                pb.writeVarInt(this.eid);
                pb.writeDouble(this.x); pb.writeDouble(this.y); pb.writeDouble(this.z);
                pb.writeDouble(0.0); pb.writeDouble(0.0); pb.writeDouble(0.0); // velocity
                pb.writeFloat(this.yaw); pb.writeFloat(this.pitch);
                pb.writeBoolean(true);
            });

            // Set Head Rotation (0x51): head yaw as Angle (u8), must be sent
            // separately — entity_position_sync alone does not update body direction.
            h.sendPacket(h.ctx, 0x51, pb -> {
                pb.writeVarInt(this.eid);
                pb.writeAngle(this.yaw);
            });
        }
    }

    // =========================================================================
    // SPECTATOR 可见性同步 (P2-6)
    // =========================================================================
    /** 模式切换后同步自身对其他玩家的可见性: 进入旁观→销毁实体(隐形); 离开旁观→重新生成。 */
    private void syncEntityVisibilityToOthers(int oldMode) {
        boolean wasSpectator = oldMode == 3;
        boolean isSpectator = this.gameMode == 3;
        if (wasSpectator == isSpectator) return; // 可见性未变, 无需处理
        for (NetworkHandler h : players.values()) {
            if (h == this || h.ctx == null) continue;
            if (isSpectator) {
                h.sendPacket(h.ctx, 0x4B, pb -> { pb.writeVarInt(1); pb.writeVarInt(this.eid); }); // entity_destroy
            } else {
                h.spawnPlayerInstance(this);
            }
        }
    }

    // =========================================================================
    // BLOCK CHANGE & PARTICLES
    // =========================================================================

    public static void broadcastBlockChange(int x, int y, int z, int blockStateId) {
        broadcastBlockChange(DimensionType.OVERWORLD, x, y, z, blockStateId);
    }

    /** 维度感知广播：只发给位于同一维度的玩家（防下界/末地变化错发主世界）。
     *  Bug48: 单块更新统一经玩家 event loop 队列发送, 与 0x2C 区块包严格保序 ——
     *  曾 0x08 从 tick 线程直写、0x2C 从 IO 线程入队, 乱序时新区块包覆盖客户端已收到的
     *  方块改动, 表现为大量透明/缺方块区块。 */
    public static void broadcastBlockChange(DimensionType dim, int x, int y, int z, int blockStateId) {
        int chunkX = x >> 4, chunkZ = z >> 4;
        long chunkKey = ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
        for (NetworkHandler player : players.values()) {
            if (player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.currentDim != dim) continue;
            if (player.loadedChunks.contains(chunkKey)) {
                player.ctx.executor().execute(() -> {
                    if (player.ctx.channel().isActive()) {
                        player.sendPacket(player.ctx, 0x08, pb -> {
                            pb.writePosition(x, y, z);
                            pb.writeVarInt(blockStateId);
                        });
                    }
                });
            }
        }
    }

    /** 光照更新广播 (0x2F ClientboundLightUpdatePacket): 发给已加载该区块的同维度玩家(经 event loop 保序)。 */
    public static void broadcastLightUpdate(Chunk chunk) {
        DimensionType dim = chunk.dim;
        long chunkKey = ((long) chunk.getX() << 32) | (chunk.getZ() & 0xFFFFFFFFL);
        for (NetworkHandler player : players.values()) {
            if (player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.currentDim != dim) continue;
            if (player.loadedChunks.contains(chunkKey)) {
                player.ctx.executor().execute(() -> {
                    if (player.ctx.channel().isActive()) {
                        player.sendPacket(player.ctx, 0x2F, pb -> ChunkEncoder.writeLightUpdate(pb, chunk));
                    }
                });
            }
        }
    }

    /** 广播 block_action(0x07): pos + byte action + byte param + blockType。用于活塞/箱子/音符盒动画。 */
    public static void broadcastBlockEvent(DimensionType dim, int x, int y, int z, byte action, byte param) {
        int blockId = BlockStateHelper.getBlockNumericId(WorldManager.getBlockState(dim, x, y, z));
        for (NetworkHandler player : players.values()) {
            if (player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.currentDim != dim) continue;
            int dx = (int) player.x - x, dz = (int) player.z - z;
            if (dx * dx + dz * dz > 16384) continue;
            player.sendPacket(player.ctx, 0x07, pb -> {
                pb.writePosition(x, y, z);
                pb.writeByte(action);
                pb.writeByte(param);
                pb.writeVarInt(blockId);
            });
        }
    }

    /** 维度感知广播音效(0x19 level_sound_event): 发给同维度 128 格内玩家。 */
    public static void broadcastSoundAt(DimensionType dim, double x, double y, double z,
                                        String soundName, float volume, float pitch) {
        for (NetworkHandler player : players.values()) {
            if (player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.currentDim != dim) continue;
            int dx = (int) (player.x - x), dz = (int) (player.z - z);
            if (dx * dx + dz * dz > 16384) continue;
            player.sendSoundAt(soundName, x, y, z, volume, pitch);
        }
    }

    /** Bug18: 拾取动画包 collect(0x7A, 1.21.2+ 线序: collectedId + collectorId + count)。
     *  掉落物被拾取时物品飞向玩家的客户端动画 —— 此前缺失, 拾取只有音效没有动画。 */
    public static void broadcastCollect(DimensionType dim, int collectedEid, int collectorEid, int count) {
        for (NetworkHandler player : players.values()) {
            if (player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.currentDim != dim) continue;
            player.sendPacket(player.ctx, 0x7A, pb -> {
                pb.writeVarInt(collectedEid);
                pb.writeVarInt(collectorEid);
                pb.writeVarInt(Math.max(1, count));
            });
        }
    }

    /** 维度感知停止声音 (0x75 stop_sound): 发给同维度 128 格内玩家 (Bug22 唱片机停播)。 */
    public static void broadcastStopSound(DimensionType dim, double x, double y, double z,
                                          String soundName) {
        for (NetworkHandler player : players.values()) {
            if (player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.currentDim != dim) continue;
            int dx = (int) (player.x - x), dz = (int) (player.z - z);
            if (dx * dx + dz * dz > 16384) continue;
            player.sendStopSound(soundName);
        }
    }

    /** 插件命令注册/注销后, 向所有在线玩家重发 Brigadier 命令树以刷新补全。 */
    public static void refreshCommandsForAll() {
        for (NetworkHandler p : players.values()) {
            if (p.ctx != null && p.ctx.channel().isActive()) {
                try {
                    p.sendCommandsPacket(p.ctx);
                } catch (Exception e) {
                    System.err.println("[插件] 重发命令树失败: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Broadcast block-break particle effects to other players.
     *
     * NOTE: In Protocol 774 (1.21.11), 0x26 is "game_state_change" (u8 reason + f32 value).
     * The old "world_event" packet (used for particles in older protocols) no longer exists
     * at this ID. A correct replacement needs to be identified (e.g., packet_particles or
     * packet_level_event if available). Until then, this method is a no-op to avoid
     * disconnecting clients with malformed packets.
     *
     * TODO: Re-implement using the correct 1.21.11 particle packet once identified.
     */
    public static void broadcastBlockBreakParticles(DimensionType dim, int x, int y, int z, int blockStateId) {
        for (NetworkHandler player : players.values()) {
            if (player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.currentDim != dim) continue; // Bug56: 跨维度粒子泄漏
            int dx = (int) player.x - x;
            int dz = (int) player.z - z;
            if (dx * dx + dz * dz > 16384) continue;
            player.sendPacket(player.ctx, 0x2d, pb -> {
                pb.writeInt(2001);
                pb.writePosition(x, y, z);
                pb.writeInt(blockStateId);
                pb.writeByte(0);
            });
        }
    }

    public static void broadcastBlockBreakProgress(DimensionType dim, int breakerId, int x, int y, int z, int stage) {
        for (NetworkHandler player : players.values()) {
            if (player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.currentDim != dim) continue; // Bug56/59: 裂纹按维度过滤
            int dx = (int) player.x - x;
            int dz = (int) player.z - z;
            if (dx * dx + dz * dz > 16384) continue;
            player.sendPacket(player.ctx, 0x05, pb -> {
                pb.writeVarInt(breakerId);
                pb.writePosition(x, y, z);
                pb.writeByte((byte) stage);
            });
        }
    }

    /** Bug59: crack stages to other players only (own client renders locally), dimension-filtered. */
    private void broadcastBlockBreakProgressExceptSelf(int breakerId, int x, int y, int z, int stage) {
        for (NetworkHandler player : players.values()) {
            if (player == this || player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.currentDim != this.currentDim) continue;
            int dx = (int) player.x - x;
            int dz = (int) player.z - z;
            if (dx * dx + dz * dz > 16384) continue;
            player.sendPacket(player.ctx, 0x05, pb -> {
                pb.writeVarInt(breakerId);
                pb.writePosition(x, y, z);
                pb.writeByte((byte) stage);
            });
        }
    }

    public static void broadcastRedstoneParticles(int x, int y, int z) {
        broadcastRedstoneParticles(DimensionType.OVERWORLD, x, y, z);
    }

    /** 维度感知广播：只发给位于同一维度的玩家 */
    public static void broadcastRedstoneParticles(DimensionType dim, int x, int y, int z) {
        for (NetworkHandler player : players.values()) {
            if (player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.currentDim != dim) continue;
            int dx = (int) player.x - x;
            int dz = (int) player.z - z;
            if (dx * dx + dz * dz > 4096) continue;
            player.sendPacket(player.ctx, 0x2d, pb -> {
                pb.writeInt(2001);
                pb.writePosition(x, y, z);
                pb.writeInt(BlockStateHelper.getDefault("redstone_wire"));
                pb.writeByte(0);
            });
        }
    }
    // =========================================================================

    /**
     * Spawn Entity (0x01) for a player entity.
     *
     * FIX (from previous session): removed stray writeByte(0) before angles,
     * added missing velocity shorts (3×i16 = 0).
     */
    private void spawnPlayerInstance(NetworkHandler target) {
        // 旁观者对其他玩家不可见(P2-6)
        if (target.gameMode == 3) return;
        this.sendPacket(this.ctx, 0x01, pb -> {
            pb.writeVarInt(target.eid);
            pb.writeUUID(target.uuid);
            pb.writeVarInt(155);             // Entity type: player
            pb.writeDouble(target.x);
            pb.writeDouble(target.y);
            pb.writeDouble(target.z);
            pb.writeLpVec3(0.0, 0.0, 0.0);   // Velocity (LpVec3): zero -> single 0 byte
            pb.writeAngle(target.pitch);     // Pitch
            pb.writeAngle(target.yaw);       // Yaw (body)
            pb.writeAngle(target.yaw);       // Head Yaw
            pb.writeVarInt(0);               // Data
        });

        // Set Head Rotation immediately after spawn
        this.sendPacket(this.ctx, 0x51, pb -> {
            pb.writeVarInt(target.eid);
            pb.writeAngle(target.yaw);
        });

        // If the spawning player is already sneaking or on fire, tell this client about it
        if (target.isSneaking || target.fireTicks > 0) {
            byte tf = (byte) ((target.isSneaking ? 0x02 : 0) | (target.fireTicks > 0 ? 0x01 : 0));
            byte tp = (byte)(target.isSneaking ? 5 : 0);
            this.sendPacket(this.ctx, 0x61, pb -> { // entity_metadata/set_entity_data=0x61 (0x62=attach_entity/set_entity_link 栓绳包,固定 8 字节 2×i32; 曾误用 0x62 -> eid 大于 127 时载荷超出 8 字节, 客户端报 "N bytes extra" 断线)
                pb.writeVarInt(target.eid);
                pb.writeByte(0); pb.writeVarInt(0); pb.writeByte(tf);
                pb.writeByte(6); pb.writeVarInt(20); pb.writeVarInt(tp);
                pb.writeByte(0xFF);
            });
        }

        sendEquipmentTo(this, target);
        sendEquipmentTo(target, this);
    }

    /** 广播自身装备给所有其他玩家 */
    private void broadcastEquipment() {
        for (NetworkHandler h : players.values()) {
            if (h == this || h.ctx == null) continue;
            sendEquipmentTo(h, this);
        }
    }

    /**
     * 向 receiver 发送 source 的装备信息。
     * Set Equipment 格式：每条目 = slotByte(含续bit) + Slot data
     *   slot 高位 1 = 还有后续条目；0 = 最后一条
     *   slot 编号：0=主手, 1=副手, 2=靴, 3=护腿, 4=胸甲, 5=头盔
     *
     * entity_equipment = 0x65 (曾误用 0x64=entity_velocity -> 客户端把装备数据当速度解析)
     */
    private static void sendEquipmentTo(NetworkHandler receiver, NetworkHandler source) {
        if (receiver.ctx == null || source.data == null) return;

        int mhSlot  = 36 + source.heldItemSlot;
        int[] eqIds = {
                source.data.inventoryIds[mhSlot], // 0 主手
                source.data.inventoryIds[45],     // 1 副手
                source.data.inventoryIds[8],      // 2 靴子  (inv slot 8)
                source.data.inventoryIds[7],      // 3 护腿  (inv slot 7)
                source.data.inventoryIds[6],      // 4 胸甲  (inv slot 6)
                source.data.inventoryIds[5],      // 5 头盔  (inv slot 5)
        };
        int[] eqCnt = {
                source.data.inventoryCounts[mhSlot],
                source.data.inventoryCounts[45],
                source.data.inventoryCounts[8],
                source.data.inventoryCounts[7],
                source.data.inventoryCounts[6],
                source.data.inventoryCounts[5],
        };

        // 至少主手要发（空也发，让别人看到拿东西）
        int[] eqSlots = {mhSlot, 45, 8, 7, 6, 5};
        receiver.sendPacket(receiver.ctx, 0x64, pb -> { // entity_equipment=0x64 (0x65=set_experience, 曾误用 0x65 -> 骷髅持弓/盔甲广播被当经验包解码, 报 "3 bytes extra" 断线)
            pb.writeVarInt(source.eid);
            for (int i = 0; i < 6; i++) {
                boolean last = (i == 5);
                pb.writeByte(last ? i : (i | 0x80)); // 高bit=1表示还有后续
                // Bug44: 装备按完整组件下发(纹饰/附魔在他人视角与自身身上可见)
                source.writeStackWithMeta(pb, eqIds[i], eqCnt[i], source.playerSlotMeta(eqSlots[i]));
            }
        });
    }

    // =========================================================================
    // COMMANDS PACKET (brigadier tree)
    // =========================================================================

    /**
     * Sends the clientbound Commands packet so the client shows tab-completion
     * for /time, /tp, /gamemode.
     *
     * Node layout (0-indexed):
     *  0  ROOT
     *  1  "time"        literal
     *  2  "tp"          literal
     *  3  "gamemode"    literal
     *  4  "set"         literal (child of time)
     *  5  "add"         literal (child of time)
     *  6  "query"       literal (child of time)
     *  7  "x"           argument float (child of tp)
     *  8  "survival"    literal, executable (child of gamemode)
     *  9  "creative"    literal, executable
     * 10  "adventure"   literal, executable
     * 11  "spectator"   literal, executable
     * 12  "time_value"  argument integer, executable (child of set/add)
     * 13  "daytime"     literal, executable (child of query)
     * 14  "gametime"    literal, executable
     * 15  "day"         literal, executable
     * 16  "y"           argument float (child of x)
     * 17  "z"           argument float, executable (child of y)
     *
     * Packet ID 0x11 — ❓verify via logs (it is sent during PLAY phase at login).
     *
     * Brigadier flag byte:
     *   bits 0-1: 00=root, 01=literal, 10=argument
     *   bit  2:   executable
     *   bit  3:   has redirect
     *   bit  4:   has suggestions type
     */
    private void sendCommandsPacket(ChannelHandlerContext ctx) {
        java.util.List<Object[]> nodes = new java.util.ArrayList<>();
        java.util.List<Integer> rootChildren = new java.util.ArrayList<>();

        for (String cmd : java.util.List.of(
            "give", "clear", "kill", "heal", "feed", "summon", "xp", "enchant",
            "seed", "setblock", "fill", "clone", "spawnpoint", "home", "back",
            "gamerule", "kick", "op", "deop", "list", "msg", "tell", "w", "say",
            "me", "help", "top", "bottom", "suicide", "ping", "motd", "rules",
            "afk", "repair", "hat", "enderchest", "ec", "workbench", "craft",
            "invsee", "experience"
        )) {
            rootChildren.add(nodes.size() + 1);
            nodes.add(new Object[]{0x05, new int[]{}, cmd, -1, null});
        }

        for (String pluginCmd : Server.get().getPluginManager().getCommands().keySet()) {
            rootChildren.add(nodes.size() + 1);
            nodes.add(new Object[]{0x05, new int[]{}, pluginCmd, -1, null});
        }

        addLiteralWithChildren(nodes, rootChildren, "tp", false, childNodes -> {
            int xNode = childNodes.size() + 1;
            childNodes.add(new Object[]{0x06, null, "x", 1, null});
            int yNode = childNodes.size() + 1;
            childNodes.add(new Object[]{0x06, new int[]{}, "y", 1, null});
            childNodes.get(xNode - 1)[1] = new int[]{yNode};
            int zNode = childNodes.size() + 1;
            childNodes.add(new Object[]{0x06, new int[]{}, "z", 1, null});
            childNodes.get(yNode - 1)[1] = new int[]{zNode};
            return new int[]{xNode};
        });

        addLiteralWithChildren(nodes, rootChildren, "gamemode", false, true, childNodes -> {
            int[] gm = new int[8];
            for (int i = 0; i < 8; i++) {
                gm[i] = childNodes.size() + 1;
                childNodes.add(new Object[]{0x05, new int[]{}, java.util.List.of("survival","creative","adventure","spectator","0","1","2","3").get(i), -1, null});
            }
            return gm;
        });
        addLiteralWithChildren(nodes, rootChildren, "gm", false, true, childNodes -> {
            int[] gm = new int[8];
            for (int i = 0; i < 8; i++) {
                gm[i] = childNodes.size() + 1;
                childNodes.add(new Object[]{0x05, new int[]{}, java.util.List.of("survival","creative","adventure","spectator","0","1","2","3").get(i), -1, null});
            }
            return gm;
        });

        addLiteralWithChildren(nodes, rootChildren, "time", false, childNodes -> {
            int setIdx = childNodes.size() + 1;
            childNodes.add(new Object[]{0x01, null, "set", -1, null});
            int[] setKids = addTimeSet(childNodes);
            childNodes.get(setIdx - 1)[1] = setKids;

            int addIdx = childNodes.size() + 1;
            childNodes.add(new Object[]{0x01, null, "add", -1, null});
            int addArg = childNodes.size() + 1;
            childNodes.add(new Object[]{0x06, new int[]{}, "value", 3, null});
            childNodes.get(addIdx - 1)[1] = new int[]{addArg};

            int queryIdx = childNodes.size() + 1;
            childNodes.add(new Object[]{0x01, null, "query", -1, null});
            int[] queryKids = addTimeQuery(childNodes);
            childNodes.get(queryIdx - 1)[1] = queryKids;

            return new int[]{setIdx, addIdx, queryIdx};
        });

        addLiteralWithChildren(nodes, rootChildren, "weather", false, childNodes -> {
            int[] kids = new int[3];
            for (int i = 0; i < 3; i++) kids[i] = childNodes.size() + 1 + i;
            for (String w : java.util.List.of("clear","rain","thunder")) {
                childNodes.add(new Object[]{0x05, new int[]{}, w, -1, null});
            }
            return kids;
        });

        addLiteralWithChildren(nodes, rootChildren, "difficulty", false, childNodes -> {
            int[] kids = new int[8];
            for (int i = 0; i < 8; i++) kids[i] = childNodes.size() + 1 + i;
            for (String d : java.util.List.of("peaceful","easy","normal","hard","0","1","2","3")) {
                childNodes.add(new Object[]{0x05, new int[]{}, d, -1, null});
            }
            return kids;
        });

        addLiteralWithChildren(nodes, rootChildren, "world", false, childNodes -> {
            int[] kids = new int[3];
            for (int i = 0; i < 3; i++) kids[i] = childNodes.size() + 1 + i;
            for (String w : java.util.List.of("overworld","nether","end")) {
                childNodes.add(new Object[]{0x05, new int[]{}, w, -1, null});
            }
            return kids;
        });

        addLiteralWithChildren(nodes, rootChildren, "fly", false, childNodes -> {
            int[] kids = new int[2];
            for (int i = 0; i < 2; i++) kids[i] = childNodes.size() + 1 + i;
            for (String f : java.util.List.of("on","off")) {
                childNodes.add(new Object[]{0x05, new int[]{}, f, -1, null});
            }
            return kids;
        });

        addLiteralWithChildren(nodes, rootChildren, "god", false, childNodes -> {
            int[] kids = new int[2];
            for (int i = 0; i < 2; i++) kids[i] = childNodes.size() + 1 + i;
            for (String g : java.util.List.of("on","off")) {
                childNodes.add(new Object[]{0x05, new int[]{}, g, -1, null});
            }
            return kids;
        });

        addLiteralWithChildren(nodes, rootChildren, "effect", false, childNodes -> {
            int[] kids = new int[2];
            for (int i = 0; i < 2; i++) kids[i] = childNodes.size() + 1 + i;
            for (String e : java.util.List.of("give","clear")) {
                childNodes.add(new Object[]{0x05, new int[]{}, e, -1, null});
            }
            return kids;
        });

        addLiteralWithChildren(nodes, rootChildren, "speed", false, childNodes -> {
            int argIdx = childNodes.size() + 1;
            childNodes.add(new Object[]{0x06, new int[]{}, "value", 3, null});
            return new int[]{argIdx};
        });

        addLiteralWithChildren(nodes, rootChildren, "locate", false, childNodes -> {
            int argIdx = childNodes.size() + 1;
            childNodes.add(new Object[]{0x06, new int[]{}, "structure", 5, null});
            return new int[]{argIdx};
        });

        int totalNodes = 1 + nodes.size();

        sendPacket(ctx, 0x10, pb -> {
            pb.writeVarInt(totalNodes);

            pb.writeByte(0x00);
            pb.writeVarInt(rootChildren.size());
            for (int c : rootChildren) pb.writeVarInt(c);

            for (Object[] node : nodes) {
                int flags = ((Number) node[0]).intValue();
                int[] children = (int[]) node[1];
                String name = (String) node[2];
                int parserId = ((Number) node[3]).intValue();
                pb.writeByte(flags);
                pb.writeVarInt(children.length);
                for (int c : children) pb.writeVarInt(c);
                pb.writeString(name);
                if ((flags & 0x03) == 0x02) {
                    pb.writeVarInt(parserId);
                    pb.writeByte(0x00);
                }
            }

            pb.writeVarInt(0);
        });
    }

    @FunctionalInterface
    private interface ChildBuilder {
        int[] build(java.util.List<Object[]> childList);
    }

    private void addLiteralWithChildren(java.util.List<Object[]> nodes,
                                        java.util.List<Integer> rootChildren,
                                        String name, boolean executable,
                                        ChildBuilder builder) {
        addLiteralWithChildren(nodes, rootChildren, name, executable, false, builder);
    }

    private void addLiteralWithChildren(java.util.List<Object[]> nodes,
                                        java.util.List<Integer> rootChildren,
                                        String name, boolean executable, boolean restricted,
                                        ChildBuilder builder) {
        int parentIdx = nodes.size() + 1;
        rootChildren.add(parentIdx);
        // 命令节点 flags: 0x01=literal 0x02=argument 0x04=executable 0x20=restricted。
        // #1 修复: 客户端把带 0x20(FLAG_RESTRICTED) 的命令节点映射为"受限命令"权限
        // (client/commands/restricted), F3+F4 游戏模式切换器据此判定玩家是否有权限;
        // 曾无任何 restricted 节点 -> 客户端 PermissionSet 不含 RESTRICTED_COMMAND -> F3+F4 永远"无权限"。
        int flags = 0x01 | (executable ? 0x04 : 0) | (restricted ? 0x20 : 0);
        nodes.add(new Object[]{(byte) flags, new int[]{}, name, -1, null});

        int[] childIndices = builder.build(nodes);

        nodes.get(parentIdx - 1)[1] = childIndices;
    }

    private int[] addTimeSet(java.util.List<Object[]> nodes) {
        java.util.List<Integer> kids = new java.util.ArrayList<>();
        for (String v : java.util.List.of("day","night","noon","midnight","sunrise","sunset")) {
            kids.add(nodes.size() + 1);
            nodes.add(new Object[]{0x05, new int[]{}, v, -1, null});
        }
        int argIdx = nodes.size() + 1;
        kids.add(argIdx);
        nodes.add(new Object[]{0x06, new int[]{}, "value", 3, null});
        return kids.stream().mapToInt(Integer::intValue).toArray();
    }

    private int[] addTimeQuery(java.util.List<Object[]> nodes) {
        int[] kids = new int[3];
        for (int i = 0; i < 3; i++) kids[i] = nodes.size() + 1 + i;
        for (String q : java.util.List.of("daytime","gametime","day")) {
            nodes.add(new Object[]{0x05, new int[]{}, q, -1, null});
        }
        return kids;
    }

    // ── 辅助：面偏移 ──────────────────────────────────
    private int[] faceOffset(int[] pos, int face) {
        return switch (face) {
            case 0 -> new int[]{pos[0], pos[1]-1, pos[2]};
            case 1 -> new int[]{pos[0], pos[1]+1, pos[2]};
            case 2 -> new int[]{pos[0], pos[1], pos[2]-1};
            case 3 -> new int[]{pos[0], pos[1], pos[2]+1};
            case 4 -> new int[]{pos[0]-1, pos[1], pos[2]};
            case 5 -> new int[]{pos[0]+1, pos[1], pos[2]};
            default-> new int[]{pos[0], pos[1]+1, pos[2]};
        };
    }

    private boolean tryIgniteNetherPortal(int fireX, int fireY, int fireZ) {
        int obsidian = BlockStateHelper.getDefault("obsidian");
        int portal = BlockStateHelper.getDefault("nether_portal");

        for (int axis = 0; axis < 2; axis++) {
            int stepX = (axis == 0) ? 1 : 0;
            int stepZ = (axis == 0) ? 0 : 1;

            int minW = 0, maxW = 0;
            while (WorldManager.getBlockState(this.currentDim,fireX + (minW - 1) * stepX, fireY, fireZ + (minW - 1) * stepZ) == 0) minW--;
            while (WorldManager.getBlockState(this.currentDim,fireX + (maxW + 1) * stepX, fireY, fireZ + (maxW + 1) * stepZ) == 0) maxW++;

            if (WorldManager.getBlockState(this.currentDim,fireX + (minW - 1) * stepX, fireY, fireZ + (minW - 1) * stepZ) != obsidian) continue;
            if (WorldManager.getBlockState(this.currentDim,fireX + (maxW + 1) * stepX, fireY, fireZ + (maxW + 1) * stepZ) != obsidian) continue;

            int minY = 0, maxY = 0;
            while (WorldManager.getBlockState(this.currentDim,fireX, fireY + minY - 1, fireZ) == 0) minY--;
            while (WorldManager.getBlockState(this.currentDim,fireX, fireY + maxY + 1, fireZ) == 0) maxY++;

            if (WorldManager.getBlockState(this.currentDim,fireX, fireY + minY - 1, fireZ) != obsidian) continue;
            if (WorldManager.getBlockState(this.currentDim,fireX, fireY + maxY + 1, fireZ) != obsidian) continue;

            int width = maxW - minW + 1;
            int height = maxY - minY + 1;
            if (width < 2 || width > 21 || height < 3 || height > 21) continue;

            boolean valid = true;
            for (int w = minW; w <= maxW && valid; w++) {
                for (int h = minY; h <= maxY && valid; h++) {
                    int px = fireX + w * stepX;
                    int pz = fireZ + w * stepZ;
                    if (WorldManager.getBlockState(this.currentDim,px, fireY + h, pz) != 0) valid = false;
                }
            }
            for (int w = minW - 1; w <= maxW + 1 && valid; w++) {
                int px = fireX + w * stepX;
                int pz = fireZ + w * stepZ;
                if (WorldManager.getBlockState(this.currentDim,px, fireY + minY - 1, pz) != obsidian) valid = false;
                if (WorldManager.getBlockState(this.currentDim,px, fireY + maxY + 1, pz) != obsidian) valid = false;
            }
            for (int h = minY; h <= maxY && valid; h++) {
                int lx = fireX + (minW - 1) * stepX;
                int lz = fireZ + (minW - 1) * stepZ;
                int rx = fireX + (maxW + 1) * stepX;
                int rz = fireZ + (maxW + 1) * stepZ;
                if (WorldManager.getBlockState(this.currentDim,lx, fireY + h, lz) != obsidian) valid = false;
                if (WorldManager.getBlockState(this.currentDim,rx, fireY + h, rz) != obsidian) valid = false;
            }
            if (!valid) continue;

            for (int w = minW; w <= maxW; w++) {
                for (int h = minY; h <= maxY; h++) {
                    int px = fireX + w * stepX;
                    int pz = fireZ + w * stepZ;
                    int portalState = BlockStateHelper.withProp(portal, "axis", axis == 0 ? "x" : "z");
                    WorldManager.setBlock(this.currentDim,px, fireY + h, pz, portalState);
                    broadcastBlockChange(this.currentDim, px, fireY + h, pz, portalState);
                }
            }
            return true;
        }
        return false;
    }

    /** Throws an eye of ender toward the nearest stronghold (overworld only). */
    /** 物品最大耐久; 0 = 该物品无耐久。 */
    public static int getMaxDurability(String item) {
        if (item == null) return 0;
        String n = item.startsWith("minecraft:") ? item.substring(10) : item;
        if (n.startsWith("wooden_")) return 59;
        if (n.startsWith("stone_")) return 131;
        if (n.startsWith("iron_")) return 250;
        if (n.startsWith("golden_")) return 32;
        if (n.startsWith("diamond_")) return 1561;
        if (n.startsWith("netherite_")) return 2031;
        return switch (n) {
            case "bow" -> 384;
            case "crossbow" -> 465;
            case "trident" -> 250;
            case "shield" -> 336;
            case "fishing_rod" -> 64;
            case "flint_and_steel" -> 64;
            case "shears" -> 238;
            case "elytra" -> 432;
            case "leather_helmet", "leather_boots" -> 55;
            case "leather_chestplate" -> 80;
            case "leather_leggings" -> 75;
            case "chainmail_helmet", "chainmail_boots" -> 165;
            case "chainmail_chestplate" -> 240;
            case "chainmail_leggings" -> 225;
            case "turtle_helmet" -> 275;
            default -> 0;
        };
    }

    /** 扣除槽位物品耐久; 归零则销毁并播放断裂音效。 */
    public void damageHeldItem(int slot, int amount) {
        if (gameMode == 1 || slot < 0 || slot >= 46) return;
        int itemId = data.inventoryIds[slot];
        if (itemId <= 0) return;
        String name = BlockManager.itemIdToName(itemId);
        int max = getMaxDurability(name);
        if (max <= 0) return;

        // Bug42: 耐久附魔(unbreaking)按原版 1/(等级+1) 概率免耗
        int unb = data.getSlotEnchant(slot, BlockManager.getEnchantId("unbreaking"));
        if (unb > 0) {
            int effective = 0;
            for (int i = 0; i < amount; i++) {
                if (java.util.concurrent.ThreadLocalRandom.current().nextInt(unb + 1) == 0) effective++;
            }
            if (effective == 0) {
                sendSlotUpdate(0, slot); // 耐久条保持
                return;
            }
            amount = effective;
        }

        data.inventoryDamage[slot] += amount;
        if (data.inventoryDamage[slot] >= max) {
            data.inventoryDamage[slot] = 0;
            data.inventoryCounts[slot]--;
            if (data.inventoryCounts[slot] <= 0) {
                data.inventoryCounts[slot] = 0;
                data.inventoryIds[slot] = 0;
            }
            sendSlotUpdate(0, slot);
            sendSoundAt("minecraft:entity.item.break", this.x, this.y, this.z, 0.8f, 0.9f);
        } else {
            // 每次耐久变化都同步给客户端, 否则耐久条不动 -> 玩家误以为耐久不消耗。
            sendSlotUpdate(0, slot);
        }
    }

    /** 找到背包里第一个箭矢槽位, 没有返回 -1。 */
    private int findArrowSlot() {
        int arrowId = BlockManager.getItemIdByName("arrow");
        if (arrowId <= 0) return -1;
        for (int i = 9; i < 46; i++) {
            if (data.inventoryIds[i] == arrowId && data.inventoryCounts[i] > 0) return i;
        }
        return -1;
    }

    private void setUsingItem(boolean using) {
        this.usingItem = using;
        final byte flags = (byte) (using ? 0x01 : 0x00);
        for (NetworkHandler p : players.values()) {
            if (p.ctx == null || p.currentDim != this.currentDim) continue;
            p.sendPacket(p.ctx, 0x61, pb -> { // entity_metadata/set_entity_data=0x61 (0x62=attach_entity/set_entity_link 栓绳包,固定 8 字节 2×i32; 曾误用 0x62 -> eid 大于 127 时载荷超出 8 字节, 客户端报 "N bytes extra" 断线)
                pb.writeVarInt(this.eid);
                pb.writeByte(8);      // index 8 = living entity flags
                pb.writeVarInt(0);    // type: byte
                pb.writeByte(flags);
                pb.writeByte(0xFF);
            });
        }
    }

    /** 松开弓: 按拉弓时长计算力度并发射箭矢。 */
    private void releaseBow() {
        if (bowChargeStart <= 0) return;
        long heldMs = System.currentTimeMillis() - bowChargeStart;
        bowChargeStart = 0;
        setUsingItem(false);

        int slot = 36 + heldItemSlot;
        int itemId = data.inventoryIds[slot];
        String itemName = itemId > 0
            ? BlockManager.itemIdToName(itemId) : null;
        if (!"bow".equals(itemName)) return;

        int arrowSlot = findArrowSlot();
        if (gameMode != 1 && arrowSlot < 0) return;

        int ticks = (int) (heldMs / 50);
        float power = ticks / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power < 0.1f) return;
        if (power > 1.0f) power = 1.0f;

        double yawRad = Math.toRadians(this.yaw);
        double pitchRad = Math.toRadians(this.pitch);
        double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
        double dy = -Math.sin(pitchRad);
        double dz = Math.cos(yawRad) * Math.cos(pitchRad);
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len < 1.0E-6) return;

        double speed = power * 3.0;
        java.util.Random rng = new java.util.Random();
        double inacc = (1.0 - power) * 0.0075;
        double vx = dx / len * speed + rng.nextGaussian() * inacc;
        double vy = dy / len * speed + rng.nextGaussian() * inacc;
        double vz = dz / len * speed + rng.nextGaussian() * inacc;

        // Bug42: 弓附魔 —— 力量(power)加伤/冲击(punch)击退/火矢(flame)点燃/无限(infinity)不耗箭
        int bowSlot = slot;
        int powerLvl = data.getSlotEnchant(bowSlot, BlockManager.getEnchantId("power"));
        int punchLvl = data.getSlotEnchant(bowSlot, BlockManager.getEnchantId("punch"));
        int flameLvl = data.getSlotEnchant(bowSlot, BlockManager.getEnchantId("flame"));
        int infinityLvl = data.getSlotEnchant(bowSlot, BlockManager.getEnchantId("infinity"));

        ArrowEntity arrow =
            new ArrowEntity(
                EntityManager.allocateId(),
                this.x, this.y + 1.5, this.z, vx, vy, vz, this);
        arrow.dim = this.currentDim;
        arrow.isCritical = power >= 1.0f;
        arrow.pickupable = gameMode != 1;
        // 力量: 额外伤害 0.25×(等级+1)×每格速度, 交给箭矢结算时使用
        arrow.bonusDamage = powerLvl > 0 ? 0.25 * (powerLvl + 1) : 0.0;
        arrow.knockbackStrength = punchLvl;
        if (flameLvl > 0) arrow.fireTicks = 20 * 8;
        EntityManager.addEntity(arrow);

        boolean infinity = infinityLvl > 0 && gameMode == 0;
        if (gameMode == 0 && !infinity) {
            data.inventoryCounts[arrowSlot]--;
            if (data.inventoryCounts[arrowSlot] <= 0) data.inventoryIds[arrowSlot] = 0;
            sendSlotUpdate(0, arrowSlot);
        }
        if (gameMode == 0 || gameMode == 2) {
            damageHeldItem(slot, 1);
        }

        float pitchSnd = 1.0f / (rng.nextFloat() * 0.4f + 1.2f) + power * 0.5f;
        for (NetworkHandler p : players.values()) {
            if (p.currentDim != this.currentDim) continue;
            p.sendSoundAt("minecraft:entity.arrow.shoot", this.x, this.y, this.z, 1.0f, pitchSnd);
        }
    }

    /** 投掷三叉戟: 生成 TridentEntity; 激流(riptide)在水中/雨中由客户端处理玩家位移。 */
    private void throwTrident() {
        int slot = 36 + heldItemSlot;
        int itemId = data.inventoryIds[slot];
        String itemName = itemId > 0
            ? BlockManager.itemIdToName(itemId) : null;
        if (!"trident".equals(itemName)) return;

        int loyalty = data.getSlotEnchant(slot, BlockManager.getEnchantId("loyalty"));
        int riptide = data.getSlotEnchant(slot, BlockManager.getEnchantId("riptide"));
        int channelingLvl = data.getSlotEnchant(slot, BlockManager.getEnchantId("channeling"));
        int impaling = data.getSlotEnchant(slot, BlockManager.getEnchantId("impaling"));

        // 激流: 在水中或雨中右键 → 将玩家向前发射(客户端自行处理位移, 这里播放音效)
        if (riptide > 0 && (isStandingInWater() || Main.isRaining)) {
            long now = System.currentTimeMillis();
            if (now - this.lastAttackTime < 500) return;
            this.lastAttackTime = now;
            for (NetworkHandler p : players.values()) {
                if (p.currentDim != this.currentDim) continue;
                p.sendSoundAt("minecraft:item.trident.riptide_1", this.x, this.y, this.z, 1.0f, 1.0f);
            }
            return;
        }

        long now = System.currentTimeMillis();
        if (now - this.lastAttackTime < 500) return; // 投掷冷却
        this.lastAttackTime = now;

        double yawRad = Math.toRadians(this.yaw);
        double pitchRad = Math.toRadians(this.pitch);
        double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
        double dy = -Math.sin(pitchRad);
        double dz = Math.cos(yawRad) * Math.cos(pitchRad);
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len < 1.0E-6) return;
        double speed = 2.5;

        TridentEntity trident =
            new TridentEntity(
                EntityManager.allocateId(),
                this.x, this.y + 1.5, this.z,
                dx / len * speed, dy / len * speed, dz / len * speed, this);
        trident.dim = this.currentDim;
        trident.loyalty = loyalty;
        trident.channeling = channelingLvl > 0;
        trident.impaling = impaling;
        trident.pickupable = gameMode != 1;
        EntityManager.addEntity(trident);

        // 投掷即从手中移除(回归/落地后成为可拾取物品)
        if (gameMode == 0 || gameMode == 2) {
            data.inventoryCounts[slot]--;
            if (data.inventoryCounts[slot] <= 0) {
                data.inventoryIds[slot] = 0;
                data.inventoryCounts[slot] = 0;
            }
            sendSlotUpdate(0, slot);
            damageHeldItem(slot, 1);
        }

        for (NetworkHandler p : players.values()) {
            if (p.currentDim != this.currentDim) continue;
            p.sendSoundAt("minecraft:item.trident.throw", this.x, this.y, this.z, 1.0f, 1.0f);
        }
    }

    /** 松开弩: 满蓄力发射箭矢(支持 multishot 三发 / piercing 穿透)。 */
    private void releaseCrossbow() {
        if (crossbowChargeStart <= 0) return;
        long heldMs = System.currentTimeMillis() - crossbowChargeStart;
        crossbowChargeStart = 0;
        setUsingItem(false);

        int slot = 36 + heldItemSlot;
        int itemId = data.inventoryIds[slot];
        String itemName = itemId > 0
            ? BlockManager.itemIdToName(itemId) : null;
        if (!"crossbow".equals(itemName)) return;

        int arrowSlot = findArrowSlot();
        if (gameMode != 1 && arrowSlot < 0) return;

        int multishot = data.getSlotEnchant(slot, BlockManager.getEnchantId("multishot"));
        int piercing = data.getSlotEnchant(slot, BlockManager.getEnchantId("piercing"));
        // quick_charge 仅影响客户端蓄力速度, 服务端满蓄力发射

        double yawRad = Math.toRadians(this.yaw);
        double pitchRad = Math.toRadians(this.pitch);
        double baseSpeed = 3.15; // 弩满蓄力箭速(略高于弓)

        java.util.Random rng = new java.util.Random();
        int[] spread = (multishot > 0) ? new int[]{-10, 0, 10} : new int[]{0};
        for (int off : spread) {
            double yawR = yawRad + Math.toRadians(off);
            double dx = -Math.sin(yawR) * Math.cos(pitchRad);
            double dy = -Math.sin(pitchRad);
            double dz = Math.cos(yawR) * Math.cos(pitchRad);
            double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (len < 1.0E-6) continue;
            double vx = dx / len * baseSpeed + rng.nextGaussian() * 0.0075;
            double vy = dy / len * baseSpeed + rng.nextGaussian() * 0.0075;
            double vz = dz / len * baseSpeed + rng.nextGaussian() * 0.0075;

            ArrowEntity arrow =
                new ArrowEntity(
                    EntityManager.allocateId(),
                    this.x, this.y + 1.5, this.z, vx, vy, vz, this);
            arrow.dim = this.currentDim;
            arrow.baseDamage = 6.0; // 弩箭基础伤害
            arrow.isCritical = false;
            arrow.pickupable = gameMode != 1;
            arrow.piercing = piercing;
            EntityManager.addEntity(arrow);
        }

        if (gameMode == 0 || gameMode == 2) {
            data.inventoryCounts[arrowSlot]--;
            if (data.inventoryCounts[arrowSlot] <= 0) data.inventoryIds[arrowSlot] = 0;
            sendSlotUpdate(0, arrowSlot);
            damageHeldItem(slot, 1);
        }

        float pitchSnd = 1.0f / (rng.nextFloat() * 0.4f + 1.2f) + 0.5f;
        for (NetworkHandler p : players.values()) {
            if (p.currentDim != this.currentDim) continue;
            p.sendSoundAt("minecraft:item.crossbow.shoot", this.x, this.y, this.z, 1.0f, pitchSnd);
        }
    }

    /** 召雷: 向维度内玩家下发 lightning_bolt(add_entity type 77) 并对落点周围生物/玩家造成伤害与点燃。 */
    public static void strikeLightning(DimensionType dim, double x, double y, double z) {
        var lightningEvent = EVENTS.fire(new com.CharunCore.server.plugin.event.events.LightningStrikeEvent(
                (float) x, (float) y, (float) z));
        if (lightningEvent.isCancelled()) return;
        int lid = EntityManager.allocateId();
        for (NetworkHandler p : players.values()) {
            if (p.currentDim != dim) continue;
            p.sendPacket(p.ctx, 0x01, pb -> {
                pb.writeVarInt(lid);
                pb.writeUUID(java.util.UUID.randomUUID());
                pb.writeVarInt(77); // lightning_bolt
                pb.writeDouble(x);
                pb.writeDouble(y);
                pb.writeDouble(z);
                pb.writeLpVec3(0, 0, 0);
                pb.writeByte(0);
                pb.writeByte(0);
                pb.writeByte(0);
                pb.writeVarInt(0);
            });
            p.sendSoundAt("minecraft:entity.lightning_bolt.thunder", x, y, z, 1.0f, 1.0f);
        }
        for (Entity e : EntityManager.getAllEntities()) {
            if (!(e instanceof LivingEntity living)) continue;
            if (living.dim != dim) continue;
            double dx = living.x - x, dy = living.y - y, dz = living.z - z;
            if (dx * dx + dy * dy + dz * dz <= 9.0) {
                living.damage(5.0f, "lightning");
                if (living instanceof MobEntity mob) {
                    mob.fireTicks = Math.max(mob.fireTicks, 80);
                }
            }
        }
        for (NetworkHandler p : players.values()) {
            if (p.currentDim != dim || p.isDead) continue;
            double dx = p.x - x, dy = p.y - y, dz = p.z - z;
            if (dx * dx + dy * dy + dz * dz <= 9.0) p.damagePlayer(5.0f, "lightning", x, z);
        }
    }

    /** 玩家脚部是否处于水中(用于激流判定)。 */
    private boolean isStandingInWater() {
        int bx = (int) Math.floor(this.x);
        int by = (int) Math.floor(this.y);
        int bz = (int) Math.floor(this.z);
        for (int dy = 0; dy <= 1; dy++) {
            int st = WorldManager.getBlockState(this.currentDim, bx, by + dy, bz);
            if ("water".equals(BlockStateHelper.getName(st))) return true;
        }
        return false;
    }

    /** 重锤附魔加成: density 下落加伤 / wind_burst 击退 / breach 破甲(返回额外伤害与击退分量)。 */
    private static final class MaceBonus { float dmg; float kb; float kbUp; }

    private MaceBonus computeMaceBonus(int heldSlot, String weaponName, float fallDist, int breachArmor) {
        MaceBonus b = new MaceBonus();
        if (!"mace".equals(weaponName)) return b;
        int density = data.getSlotEnchant(heldSlot, BlockManager.getEnchantId("density"));
        int windBurst = data.getSlotEnchant(heldSlot, BlockManager.getEnchantId("wind_burst"));
        int breach = data.getSlotEnchant(heldSlot, BlockManager.getEnchantId("breach"));
        if (density > 0 && fallDist > 0) b.dmg += (float) (fallDist * density * 0.5);
        if (breach > 0) b.dmg += breach * (breachArmor > 0 ? breachArmor * 0.15f : 0.5f);
        if (windBurst > 0) { b.kb += fallDist * windBurst * 0.3f; b.kbUp += 0.2f * windBurst; }
        return b;
    }

    private static boolean isAquaticMob(String typeName) {
        if (typeName == null) return false;
        return switch (typeName) {
            case "axolotl", "dolphin", "guardian", "elder_guardian", "squid", "glow_squid",
                 "cod", "salmon", "tropical_fish", "pufferfish", "drowned" -> true;
            default -> false;
        };
    }

    private static int totalArmorPoints(NetworkHandler p) {
        int sum = 0;
        for (int s = 5; s <= 8; s++) {
            int id = p.data.inventoryIds[s];
            if (id > 0) sum += armorPointsOf(BlockManager.itemIdToName(id));
        }
        return sum;
    }

    /** soul_speed: 站在灵魂沙/灵魂土且穿灵魂疾行靴时, 调高 walkSpeed 并下发能力包(客户端据此提速)。 */
    private void applySoulSpeed() {
        float target = 0.1f;
        if (onGround) {
            int bx = (int) Math.floor(this.x);
            int by = (int) Math.floor(this.y) - 1;
            int bz = (int) Math.floor(this.z);
            int st = WorldManager.getBlockState(this.currentDim, bx, by, bz);
            String ground = BlockStateHelper.getName(st);
            if ("soul_sand".equals(ground) || "soul_soil".equals(ground)) {
                // Bug42: 靴子槽位是 8(5=头盔 6=胸甲 7=护腿 8=靴子), 曾读 5(头盔) -> 灵魂疾行无效
                int bootId = data.inventoryIds[8];
                if (bootId > 0) {
                    int ss = data.getSlotEnchant(8, BlockManager.getEnchantId("soul_speed"));
                    if (ss > 0) target = 0.1f + 0.06f * ss;
                }
            }
        }
        if (Math.abs(target - this.walkSpeed) > 1.0E-4f) {
            this.walkSpeed = target;
            sendAbilitiesUpdate();
        }
    }

    /** 投掷末影珍珠: 命中处传送玩家并造成 5 点摔落伤害。 */
    private void throwEnderPearl() {
        double yawRad = Math.toRadians(this.yaw);
        double pitchRad = Math.toRadians(this.pitch);
        double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
        double dy = -Math.sin(pitchRad);
        double dz = Math.cos(yawRad) * Math.cos(pitchRad);
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len < 1.0E-6) return;
        double speed = 1.5;

        EnderPearlEntity pearl =
            new EnderPearlEntity(
                EntityManager.allocateId(),
                this.x, this.y + 1.5, this.z,
                dx / len * speed, dy / len * speed, dz / len * speed, this);
        pearl.dim = this.currentDim;
        EntityManager.addEntity(pearl);
        sendSoundAt("minecraft:entity.ender_pearl.throw", this.x, this.y, this.z, 1.0f, 1.0f);
    }

    /** 广播箱子/木桶/潜影盒开合动画: block_event(0x07) action=1, param=open?1:0。
     *  客户端据此播放箱盖/木桶盖开合, 否则打开容器无动画。 */
    private void sendChestBlockEvent(ContainerStore.Pos pos, boolean open) {
        byte param = (byte) (open ? 1 : 0);
        int blockId = BlockStateHelper.getBlockNumericId(WorldManager.getBlockState(pos.dim(), pos.x(), pos.y(), pos.z()));
        for (NetworkHandler p : players.values()) {
            if (p.ctx == null || !p.ctx.channel().isActive()) continue;
            if (p.currentDim != pos.dim()) continue;
            int dx = (int) p.x - pos.x(), dz = (int) p.z - pos.z();
            if (dx * dx + dz * dz > 16384) continue;
            p.sendPacket(p.ctx, 0x07, pb -> {
                pb.writePosition(pos.x(), pos.y(), pos.z());
                pb.writeByte(1);     // action = 1 (chest open/close)
                pb.writeByte(param); // param = 1 open / 0 close
                pb.writeVarInt(blockId);
            });
        }
    }

    /** 把物品丢在玩家视线前方 (Q 键)。 */
    /** #2 破坏方块核心逻辑 (status=2 挖掘完成 与 status=0 瞬破方块共用)。
     *  容器掉落/方块替换/掉落物/成就/统计/耐久/附魔(精准采集/时运)/经验/红石通知。 */
    private void breakBlockAt(int x, int y, int z) {
        int oldState = WorldManager.getBlockState(this.currentDim, x, y, z);
        var breakEvent = EVENTS.fire(new BlockBreakEvent(this, x, y, z, oldState));
        if (breakEvent.isCancelled()) return;
        String preName = BlockStateHelper.getName(oldState);
        // 容器方块: 有物品的箱子/末影箱不再额外掉落方块本身(内容物已由 dropContainerContents 溢出)
        boolean dropBlock = true;
        if (preName.equals("ender_chest")) {
            dropBlock = false;
        } else if (preName.equals("chest") || preName.equals("trapped_chest")) {
            ContainerStore.Pos cp = new ContainerStore.Pos(this.currentDim, x, y, z);
            dropBlock = ContainerStore.peekChest(cp) == null; // 仅空箱掉箱子
        }
        dropContainerContents(x, y, z);
        WorldManager.setBlock(this.currentDim, x, y, z, 0);
        broadcastBlockChange(this.currentDim, x, y, z, 0); // Bug56: 曾固定发主世界 -> 下界/末地挖不掉
        // Bug51: 破坏大箱子的一半后, 邻箱 type 回退 single
        if ("chest".equals(preName) || "trapped_chest".equals(preName)) {
            updateChestType(x + 1, y, z);
            updateChestType(x - 1, y, z);
            updateChestType(x, y, z + 1);
            updateChestType(x, y, z - 1);
        }
        // 成就系统：破坏方块事件 (P12)
        AdvancementManager.onBlockBreak(this, preName);
        StatisticsManager.add(this, "mined", preName, 1);
        if (oldState != 0) {
            broadcastBlockBreakParticles(this.currentDim, x, y, z, oldState);
            if (gameMode == 0) {
                addExhaustion(0.005f);
                String blockName = BlockStateHelper.getName(oldState);
                int heldSlotId = data.inventoryIds[36 + heldItemSlot];
                if (BlockManager.canHarvest(blockName, heldSlotId)) {
                    damageHeldItem(36 + heldItemSlot, 1);
                    int silk = data.getSlotEnchant(36 + heldItemSlot,
                        BlockManager.getEnchantId("silk_touch"));
                    int fortune = data.getSlotEnchant(36 + heldItemSlot,
                        BlockManager.getEnchantId("fortune"));
                    int dropId;
                    int dropCount = 1;
                    if (silk > 0) {
                        dropId = BlockManager.getItemIdByName(blockName);
                    } else if (isLeaves(blockName)) {
                        int[] d = rollLeavesDrop(blockName, fortune);
                        dropId = d[0];
                        dropCount = d[1];
                    } else {
                        String dropItem = getBlockDropItem(blockName);
                        if (blockName.equals("bookshelf")) dropCount = 3; // 原版书架掉 3 本书
                        dropId = BlockManager.getItemIdByName(dropItem);
                        if (fortune > 0 && dropId > 0
                                && BlockManager.isFortuneable(blockName)) {
                            dropCount = fortuneBonus(fortune); // 二项分布(可加0), 更贴近原版
                        }
                    }
                    if (dropBlock && dropId > 0 && breakEvent.isDropItems()) {
                        ItemEntity itemEntity = new ItemEntity(
                            EntityManager.allocateId(),
                            x + 0.5, y + 0.5, z + 0.5, dropId, dropCount);
                        EntityManager.addEntity(itemEntity);
                    }
                    int xp = getOreXp(blockName);
                    if (xp > 0 && silk == 0) addExperience(xp); // 精准采集不给经验(原版)
                }
            }
        }
        RedstoneEngine.onBlockChanged(this.currentDim, x, y, z);
    }

    private void dropItemInFront(int itemId, int count) {        if (itemId <= 0 || count <= 0) return;
        dropItemInFront(itemId, count, ItemMeta.EMPTY);
    }

    /** Bug4/33: 带组件丢出(附魔/药水/自定义名/耐久随掉落物保留)。 */
    public void dropItemInFront(int itemId, int count, ItemMeta meta) {
        if (itemId <= 0 || count <= 0) return;
        var dropEvent = EVENTS.fire(new PlayerDropItemEvent(this, itemId, count));
        if (dropEvent.isCancelled()) return;
        double yawRad = Math.toRadians(this.yaw);
        double pitchRad = Math.toRadians(this.pitch);
        ItemEntity drop =
            new ItemEntity(
                EntityManager.allocateId(),
                this.x, this.y + 1.3, this.z, itemId, count);
        drop.dim = this.currentDim;
        drop.vx = -Math.sin(yawRad) * Math.cos(pitchRad) * 0.3;
        drop.vy = -Math.sin(pitchRad) * 0.3 + 0.1;
        drop.vz = Math.cos(yawRad) * Math.cos(pitchRad) * 0.3;
        drop.pickupDelay = 40;
        if (meta != null && !meta.isEmpty()) {
            drop.itemDamage = meta.damage();
            drop.itemEnchants = meta.enchants().isEmpty() ? null : new java.util.HashMap<>(meta.enchants());
            drop.itemPotion = meta.potion();
            drop.itemCustomName = meta.customName();
            drop.trimMaterial = meta.trimMaterial();
            drop.trimPattern = meta.trimPattern();
        }
        EntityManager.addEntity(drop);
    }

    /** Bug4/33: 拾取带组件的掉落物。返回实际拾取数量(0=背包满一个都拿不动)。
     *  Bug47: 先并入背包中同物品且组件一致的堆(无附魔/药水/改名/耐久/纹饰才可并堆),
     *  余量再放空槽; 组件不一致的物品永不并堆。 */
    public int pickupItemCount(int itemId, int count, java.util.Map<Integer, Integer> enchants,
                               String potion, String customName, int damage,
                               int trimMaterial, int trimPattern) {
        ItemMeta m = ItemMeta.of(enchants, potion, customName, damage, trimMaterial, trimPattern);
        int max = Math.max(1, getMaxStackSize(itemId));
        int picked = 0;
        if (m.isEmpty()) {
            for (int i : PICKUP_SLOT_ORDER) {
                if (count - picked <= 0) break;
                if (data.inventoryIds[i] == itemId && data.inventoryCounts[i] > 0
                        && data.inventoryCounts[i] < max && playerSlotMeta(i).isEmpty()) {
                    int add = Math.min(max - data.inventoryCounts[i], count - picked);
                    data.inventoryCounts[i] += add;
                    picked += add;
                    sendSlotUpdate(0, i);
                }
            }
        }
        for (int i : PICKUP_SLOT_ORDER) {
            if (count - picked <= 0) break;
            if (data.inventoryIds[i] == 0 || data.inventoryCounts[i] <= 0) {
                int add = Math.min(max, count - picked);
                data.inventoryIds[i] = itemId;
                data.inventoryCounts[i] = add;
                writePlayerSlotMeta(i, m);
                picked += add;
                sendSlotUpdate(0, i);
            }
        }
        if (picked > 0) {
            String iname = BlockManager.itemIdToName(itemId);
            if (iname != null) StatisticsManager.add(this, "picked_up", iname, picked);
            sendSoundAt("minecraft:entity.item.pickup", this.x, this.y + 1.0, this.z, 0.3f, 1.0f);
        }
        return picked;
    }

    /** 拾取带组件的掉落物。背包满返回 false(物品留在地上, 原版行为)。 */
    public boolean pickupItemWithData(int itemId, int count, java.util.Map<Integer, Integer> enchants,
                                      String potion, String customName, int damage,
                                      int trimMaterial, int trimPattern) {
        return pickupItemCount(itemId, count, enchants, potion, customName, damage,
                trimMaterial, trimPattern) >= count;
    }

    /** #50: 关容器退物(原版 clearContainer): 退回背包, 放不下掉在脚前。 */
    private void returnSlotToPlayer(int itemId, int count, ItemMeta m) {
        if (itemId <= 0 || count <= 0) return;
        int got = pickupItemCount(itemId, count, m.enchants(), m.potion(), m.customName(),
                m.damage(), m.trimMaterial(), m.trimPattern());
        if (got < count) dropItemInFront(itemId, count - got, m);
    }

    private void throwEyeOfEnder() {
        int chunkX = (int) Math.floor(this.x) >> 4;
        int chunkZ = (int) Math.floor(this.z) >> 4;
        int[] found = null;
        if (this.currentDim == DimensionType.OVERWORLD) {
            // #9: 与生成器同源的同心环定位(旧 findNearest 环近似数学与实际生成不一致)
            found = locateStrongholdRing(chunkX, chunkZ, WorldManager.getSeed());
        }

        double tx, tz;
        if (found != null) {
            tx = found[0] * 16 + 8.5;
            tz = found[1] * 16 + 8.5;
        } else {
            double yawRad = Math.toRadians(this.yaw);
            tx = this.x - Math.sin(yawRad) * 24.0;
            tz = this.z + Math.cos(yawRad) * 24.0;
        }

        EyeOfEnderEntity eye =
                new EyeOfEnderEntity(
                        EntityManager.allocateId(),
                        this.x, this.y + 1.4, this.z, tx, tz, new java.util.Random());
        eye.dim = this.currentDim;
        EntityManager.addEntity(eye);
        sendSoundAt("minecraft:entity.ender_eye.launch", this.x, this.y, this.z, 1.0f, 1.0f);
    }

    private void tryActivateEndPortal(int x, int y, int z) {
        int frameId = BlockStateHelper.getDefault("end_portal_frame");
        int portalId = BlockStateHelper.getDefault("end_portal");
        int bestCx = -1, bestCz = -1, bestCount = -1;
        for (int cx = x - 4; cx <= x + 4; cx++) {
            for (int cz = z - 4; cz <= z + 4; cz++) {
                int count = 0;
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        int state = WorldManager.getBlockState(this.currentDim,cx + dx, y, cz + dz);
                        if (BlockStateHelper.getName(state).equals("end_portal_frame")) {
                            if ("true".equals(BlockStateHelper.getProp(state, "eye"))) {
                                count++;
                            } else {
                                count = -100;
                            }
                        }
                    }
                }
                if (count > bestCount) { bestCount = count; bestCx = cx; bestCz = cz; }
            }
        }
        if (bestCount >= 12) {
            // 严格校验: 中心 3×3 开口必须为空(无 end_portal_frame), 否则构成不完整结构不激活
            boolean centerClear = true;
            for (int dx = -1; dx <= 1 && centerClear; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if ("end_portal_frame".equals(BlockStateHelper.getName(
                            WorldManager.getBlockState(this.currentDim, bestCx + dx, y, bestCz + dz)))) {
                        centerClear = false;
                        break;
                    }
                }
            }
            if (!centerClear) return;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    WorldManager.setBlock(this.currentDim,bestCx + dx, y, bestCz + dz, portalId);
                    broadcastBlockChange(this.currentDim, bestCx + dx, y, bestCz + dz, portalId);
                }
            }
        }
    }

    // ── 问题 4：根据放置上下文调整 stateId ────────────
    private int applyPlacementContext(String name, int stateId, String facing, int face) {
        if (name.equals("lever") || name.endsWith("_button")) {
            if (face == 0) { // 点在方块底部
                stateId = BlockStateHelper.withProp(stateId, "face", "ceiling");
                stateId = BlockStateHelper.withProp(stateId, "facing", facing);
            } else if (face == 1) { // 点在方块顶部
                stateId = BlockStateHelper.withProp(stateId, "face", "floor");
                stateId = BlockStateHelper.withProp(stateId, "facing", facing);
            } else { // 点在方块侧面
                stateId = BlockStateHelper.withProp(stateId, "face", "wall");
                // 【修复】：墙面元件的朝向必须等于被点击的面，而不是玩家视线！
                String wallFacing = face == 2 ? "north" : face == 3 ? "south" : face == 4 ? "west" : "east";
                stateId = BlockStateHelper.withProp(stateId, "facing", wallFacing);
            }
        }
        if (name.endsWith("wall_torch")) {
            String wallFacing = face == 2 ? "north" : face == 3 ? "south" : face == 4 ? "west" : "east";
            stateId = BlockStateHelper.withProp(stateId, "facing", wallFacing);
        }
        if (name.endsWith("_stairs")) {
            stateId = BlockStateHelper.withProp(stateId, "half", face == 0 ? "top" : "bottom");
            stateId = BlockStateHelper.withProp(stateId, "facing", facing);
        }
        // #35 铁轨: 朝向在放置处(use_item_on)用目标坐标自动连接; 此处仅确保默认值。
        if (name.equals("rail") || name.equals("powered_rail") || name.equals("activator_rail")
                || name.equals("detector_rail")) {
            String shape = BlockStateHelper.getProp(stateId, "shape");
            if (shape != null && (shape.equals("north_south") || shape.equals("east_west"))) {
                String f2 = BlockStateHelper.horizontalFacing(this.yaw);
                stateId = BlockStateHelper.withProp(stateId, "shape",
                    (f2.equals("north") || f2.equals("south")) ? "north_south" : "east_west");
            }
        }
        // 【新增】活塞/发射器/投掷器/漏斗朝向
        if (name.equals("piston") || name.equals("sticky_piston")
                || name.equals("dispenser") || name.equals("dropper") || name.equals("hopper")) {
            String pf;
            if (face == 0) pf = "down";
            else if (face == 1) pf = "up";
            else pf = facing;
            stateId = BlockStateHelper.withProp(stateId, "facing", pf);
        }
        // #34 熔炉/高炉/烟熏炉: 开口朝玩家(原版 FACING = 玩家视线反向)。
        if (name.equals("furnace") || name.equals("blast_furnace") || name.equals("smoker")) {
            String pOpp = BlockStateHelper.horizontalFacing(this.yaw);
            pOpp = switch (pOpp) { // 反向
                case "north" -> "south"; case "south" -> "north";
                case "east" -> "west"; case "west" -> "east";
                default -> pOpp;
            };
            stateId = BlockStateHelper.withProp(stateId, "facing", pOpp);
        }
        // Logger 之类水平朝向方块: 面向玩家视线方向(原版多数方块 FACING = 玩家视线)。
        if (name.endsWith("_log") || name.equals("hay_block") || name.equals("bone_block")
                || name.equals("pumpkin") || name.equals("carved_pumpkin") || name.equals("melon")
                || name.equals("jack_o_lantern") || name.equals("chain")
                || name.equals("quartz_pillar") || name.equals("purpur_pillar")) {
            String pf2;
            if (face == 0) pf2 = "y"; else if (face == 1) pf2 = "y";
            else pf2 = facing;
            stateId = BlockStateHelper.withProp(stateId, "axis", pf2.equals("north") || pf2.equals("south") ? "z"
                : pf2.equals("east") || pf2.equals("west") ? "x" : "y");
        } else if (name.equals("brewing_stand") || name.equals("barrel")
                || name.equals("crafting_table") || name.equals("fletching_table")
                || name.equals("cartography_table") || name.equals("smithing_table")
                || name.equals("loom") || name.equals("grindstone")) {
            // 水平朝向方块: 按 face 决定水平 facing (点击侧面用对应朝向, 顶/底用玩家朝向)。
            String pf;
            if (face == 2) pf = "north";
            else if (face == 3) pf = "south";
            else if (face == 4) pf = "west";
            else if (face == 5) pf = "east";
            else pf = facing;
            if (BlockStateHelper.getProp(stateId, "facing") != null) {
                stateId = BlockStateHelper.withProp(stateId, "facing", pf);
            }
        } else if (name.equals("comparator") || name.equals("repeater")) {
            // 这些器件仅水平朝向, 直接用玩家水平朝向
            stateId = BlockStateHelper.withProp(stateId, "facing", facing);
        } else if (name.equals("observer")) {
            // 原版: 观察者支持垂直放置 (facing=up/down)。
            String pf;
            if (face == 0) pf = "up";        // 点击下表面 → 观察者朝上
            else if (face == 1) pf = "down"; // 点击上表面 → 观察者朝下
            else pf = facing;
            stateId = BlockStateHelper.withProp(stateId, "facing", pf);
        }
        // Bug32: 墙面挂件(墙牌/墙旗/墙头)朝向 = 被点击面
        if (name.endsWith("_wall_sign") || name.endsWith("_wall_hanging_sign")
                || name.endsWith("_wall_banner") || name.endsWith("_wall_head")
                || name.endsWith("_wall_skull") || name.equals("ladder")) {
            stateId = BlockStateHelper.withProp(stateId, "facing",
                face == 2 ? "north" : face == 3 ? "south" : face == 4 ? "west" : face == 5 ? "east" : facing);
        }
        // Bug32: 立式告示牌/旗帜/头颅 rotation(0-15) 按玩家视线取 16 分度
        if (BlockStateHelper.getProp(stateId, "rotation") != null
                && !name.contains("wall")) {
            int rot = (int) Math.floor((this.yaw * 16.0 / 360.0) + 0.5) & 15;
            stateId = BlockStateHelper.withProp(stateId, "rotation", String.valueOf(rot));
        }
        // Bug32: 活板门 facing+half(点击底面=贴天花板 top)
        if (name.endsWith("_trapdoor")) {
            String tf = face == 2 ? "north" : face == 3 ? "south" : face == 4 ? "west" : face == 5 ? "east" : facing;
            stateId = BlockStateHelper.withProp(stateId, "facing", tf);
            String half = face == 0 ? "top" : "bottom";
            if (BlockStateHelper.getProp(stateId, "half") != null) {
                stateId = BlockStateHelper.withProp(stateId, "half", half);
            }
        }
        // Bug32: 箱子类/讲台/营火 朝向 = 玩家视线反向(开口朝玩家, 原版 horizontalDirection.getOpposite)
        if (name.equals("chest") || name.equals("trapped_chest") || name.equals("ender_chest")
                || name.equals("lectern") || name.equals("campfire") || name.equals("soul_campfire")) {
            String pOpp = switch (facing) {
                case "north" -> "south"; case "south" -> "north";
                case "east" -> "west"; case "west" -> "east";
                default -> facing;
            };
            if (BlockStateHelper.getProp(stateId, "facing") != null) {
                stateId = BlockStateHelper.withProp(stateId, "facing", pOpp);
            }
        }
        // Bug32: 铁砧朝向 = 玩家水平朝向
        if (name.endsWith("_anvil") && BlockStateHelper.getProp(stateId, "facing") != null) {
            stateId = BlockStateHelper.withProp(stateId, "facing", facing);
        }
        // Bug32: 末地烛/避雷针 全向 facing(曾错误地归入 axis 组)
        if (name.equals("end_rod") || name.equals("lightning_rod") || name.equals("rod")) {
            String rf = face == 1 ? "up" : face == 0 ? "down"
                : face == 2 ? "north" : face == 3 ? "south" : face == 4 ? "west" : "east";
            stateId = BlockStateHelper.withProp(stateId, "facing", rf);
        }
        // Bug32: 灯笼 hanging 属性(点方块底面=挂式)
        if (name.equals("lantern") || name.equals("soul_lantern")) {
            if (BlockStateHelper.getProp(stateId, "hanging") != null) {
                stateId = BlockStateHelper.withProp(stateId, "hanging", face == 0 ? "true" : "false");
            }
        }
        return stateId;
    }

    /** #35 铁轨自动连接: 检查目标位置 4 方向相邻铁轨, 返回连接 shape。
     *  原版 RailState.updateDir 自动连线: 直线/弯道。返回 null 表示无相邻铁轨(用玩家朝向)。
     */
    /** Bug23: 铁轨放置自适应朝向。检测四方向同层/上一层/下一层的相邻铁轨:
     *  相邻轨在本侧上一层 -> 该方向爬升(ascending_X); 弯角形状仅普通铁轨(canCurve)允许。 */
    private String autoRailShape(int x, int y, int z, boolean canCurve) {
        // 返回值: 0=无, 1=同层, +2=上一层(爬升), -3=下一层(下降)
        int n = railSideOffset(x, y, z - 1);
        int s = railSideOffset(x, y, z + 1);
        int w = railSideOffset(x - 1, y, z);
        int e = railSideOffset(x + 1, y, z);
        boolean north = n != 0, south = s != 0, west = w != 0, east = e != 0;
        if (!north && !south && !west && !east) return null;

        // 爬升: 本侧相邻轨在上一层 -> ascending_该方向; 或本侧在下一层且对侧有轨 -> 对侧爬升
        if (n == 2) return "ascending_north";
        if (s == 2) return "ascending_south";
        if (e == 2) return "ascending_east";
        if (w == 2) return "ascending_west";
        if (s == -3 && n != 0) return "ascending_north";
        if (n == -3 && s != 0) return "ascending_south";
        if (w == -3 && e != 0) return "ascending_east";
        if (e == -3 && w != 0) return "ascending_west";

        boolean ns = north || south;
        boolean ew = east || west;
        if (ns && ew) {
            if (canCurve) {
                String a = n != 0 ? "north" : "south";
                String b = e != 0 ? "east" : "west";
                return a + "_" + b;
            }
            return "north_south";
        }
        if (ns) return "north_south";
        if (ew) return "east_west";
        return null;
    }

    /** 检测 (x,y,z) 一格的相邻铁轨: 0=无 1=同层 2=上一层 -3=下一层 */
    private int railSideOffset(int x, int y, int z) {
        if (isRailAt(x, y, z)) return 1;
        if (isRailAt(x, y + 1, z)) return 2;
        if (isRailAt(x, y - 1, z)) return -3;
        return 0;
    }

    private boolean isRailAt(int x, int y, int z) {
        String n = BlockStateHelper.getName(WorldManager.getBlockState(this.currentDim, x, y, z));
        return n != null && (n.equals("rail") || n.equals("powered_rail")
            || n.equals("activator_rail") || n.equals("detector_rail"));
    }

    /** 放置带方块实体的方块时创建初始 BE NBT, 保证重启/区块重载后数据不丢。 */
    private void createInitialBlockEntity(int x, int y, int z, String blockName) {
        org.cloudburstmc.nbt.NbtMapBuilder b = null;
        if (blockName.endsWith("_sign") || blockName.endsWith("_wall_sign")
                || blockName.equals("sign") || blockName.equals("wall_sign")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:" + blockName);
            // 原版 SignBlockEntity: front_text/back_text 各含 messages(4 行 JSON 文本)/color/has_glowing_text。
            // #10 修复: 空行必须是空字符串("")而非 {"text":""}, 否则未写字的一面显示字面 {"text":""}。
            for (String side : new String[]{"front_text", "back_text"}) {
                org.cloudburstmc.nbt.NbtMapBuilder tb = org.cloudburstmc.nbt.NbtMap.builder();
                tb.putList("messages", org.cloudburstmc.nbt.NbtType.STRING,
                    java.util.List.of("", "", "", ""));
                tb.putString("color", "black");
                tb.putBoolean("has_glowing_text", false);
                b.put(side, tb.build());
            }
        } else if (blockName.equals("spawner") || blockName.equals("mob_spawner")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:mob_spawner");
            b.putShort("Delay", (short) 20);
            b.putShort("MinSpawnDelay", (short) 200);
            b.putShort("MaxSpawnDelay", (short) 800);
            b.putShort("SpawnCount", (short) 4);
            b.putShort("MaxNearbyEntities", (short) 6);
            b.putShort("RequiredPlayerRange", (short) 16);
            b.putShort("SpawnRange", (short) 4);
        } else if (blockName.equals("beacon")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:beacon");
            b.putInt("Levels", 0);
            b.putInt("Primary", -1);
            b.putInt("Secondary", -1);
        } else if (blockName.equals("campfire") || blockName.equals("soul_campfire")) {
            // 营火 BE(食物槽)由 ContainerStore 管理, 这里仅建空壳保证 id/坐标存在
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:" + blockName);
        } else if (blockName.equals("chest") || blockName.equals("trapped_chest")
                || blockName.equals("barrel") || blockName.endsWith("_shulker_box")) {
            // #13 修复: 箱子/木桶/潜影盒等容器方块放置时须创建 BE, 否则重进游戏后
            // 客户端无 BE 数据 -> 渲染成透明(曾只覆盖 sign/spawner/beacon/campfire)。
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:" + blockName);
            b.putByte("facing", (byte) 2);
            b.putInt("x", x); b.putInt("y", y); b.putInt("z", z);
            org.cloudburstmc.nbt.NbtList items = new org.cloudburstmc.nbt.NbtList(org.cloudburstmc.nbt.NbtType.COMPOUND);
            b.put("Items", items);
        } else if (blockName.equals("furnace") || blockName.equals("blast_furnace") || blockName.equals("smoker")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:" + blockName);
            b.putShort("lit_time", (short) 0);
            b.putShort("lit_total_time", (short) 0);
            b.putShort("cooking_time", (short) 0);
            b.putShort("cooking_total_time", (short) 200);
        } else if (blockName.equals("hopper")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:hopper");
            b.putByte("facing", (byte) 0);
            b.putInt("TransferCooldown", 0);
        } else if (blockName.equals("brewing_stand")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:brewing_stand");
            b.putByte("HasBottle0", (byte) 0);
            b.putByte("HasBottle1", (byte) 0);
            b.putByte("HasBottle2", (byte) 0);
            b.putInt("BrewTime", 0);
            b.putShort("Fuel", (short) 0);
        } else if (blockName.equals("dispenser") || blockName.equals("dropper")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:" + blockName);
            b.putByte("facing", (byte) 3);
            org.cloudburstmc.nbt.NbtList ditem = new org.cloudburstmc.nbt.NbtList(org.cloudburstmc.nbt.NbtType.COMPOUND);
            b.put("Items", ditem);
        } else if (blockName.equals("jukebox")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:jukebox");
        } else if (blockName.equals("enchanting_table")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:enchanting_table");
        } else if (blockName.equals("grindstone") || blockName.equals("stonecutter")
                || blockName.equals("smithing_table") || blockName.equals("cartography_table")
                || blockName.equals("loom")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:" + blockName);
        } else if (blockName.equals("ender_chest")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:ender_chest");
            b.putByte("facing", (byte) 2);
        } else if (blockName.endsWith("_bed")) {
            // Bug9: 床/旗帜/头颅等 BE 渲染方块必须建壳 BE, 否则客户端 BlockEntityRenderer
            // 无数据可渲染 -> 整块透明。
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:bed");
        } else if (blockName.endsWith("_banner") || blockName.endsWith("_wall_banner")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:banner");
        } else if (blockName.endsWith("_skull") || blockName.endsWith("_head")
                || blockName.endsWith("_wall_head")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:skull");
        } else if (blockName.endsWith("_hanging_sign") || blockName.endsWith("_wall_hanging_sign")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:hanging_sign");
            for (String side : new String[]{"front_text", "back_text"}) {
                org.cloudburstmc.nbt.NbtMapBuilder tb = org.cloudburstmc.nbt.NbtMap.builder();
                tb.putList("messages", org.cloudburstmc.nbt.NbtType.STRING,
                    java.util.List.of("", "", "", ""));
                tb.putString("color", "black");
                tb.putBoolean("has_glowing_text", false);
                b.put(side, tb.build());
            }
        } else if (blockName.equals("lectern")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:lectern");
        } else if (blockName.equals("bell")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:bell");
        } else if (blockName.equals("comparator")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:comparator");
        } else if (blockName.equals("daylight_detector")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:daylight_detector");
        } else if (blockName.equals("conduit")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:conduit");
        } else if (blockName.equals("decorated_pot")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:decorated_pot");
        } else if (blockName.equals("chiseled_bookshelf")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:chiseled_bookshelf");
        } else if (blockName.equals("crafter")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:crafter");
        } else if (blockName.equals("trial_spawner")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:trial_spawner");
        } else if (blockName.equals("vault")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:vault");
        } else if (blockName.equals("beehive") || blockName.equals("bee_nest")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:beehive");
        } else if (blockName.equals("suspicious_sand") || blockName.equals("suspicious_gravel")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:brushable_block");
        } else if (blockName.equals("sculk_sensor") || blockName.equals("calibrated_sculk_sensor")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:" + blockName);
        } else if (blockName.equals("sculk_shrieker") || blockName.equals("sculk_catalyst")
                || blockName.equals("creaking_heart")) {
            b = org.cloudburstmc.nbt.NbtMap.builder();
            b.putString("id", "minecraft:" + blockName);
        }
        if (b == null) return;
        if (!b.containsKey("x")) b.putInt("x", x);
        if (!b.containsKey("y")) b.putInt("y", y);
        if (!b.containsKey("z")) b.putInt("z", z);
        Chunk chunk = WorldManager.getChunk(this.currentDim, x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlockEntity(x & 15, y, z & 15, b.build());
        }
    }

    /** 把告示牌编辑后的 4 行文本写回区块 BE, 并广播给附近玩家(原版用 block_entity_data 包刷新)。 */
    private void updateSignText(int x, int y, int z, boolean isFront, java.util.List<String> lines) {
        Chunk chunk = WorldManager.getChunk(this.currentDim, x >> 4, z >> 4);
        if (chunk == null) return;
        org.cloudburstmc.nbt.NbtMap be = chunk.getBlockEntity(x & 15, y, z & 15);
        org.cloudburstmc.nbt.NbtMapBuilder b = org.cloudburstmc.nbt.NbtMap.builder();
        if (be != null) {
            for (String k : be.keySet()) b.put(k, be.get(k));
        } else {
            int st = WorldManager.getBlockState(this.currentDim, x, y, z);
            String sn = BlockStateHelper.getName(st);
            b.putString("id", "minecraft:" + (sn == null ? "oak_sign" : sn));
            b.putInt("x", x); b.putInt("y", y); b.putInt("z", z);
        }
        // 原版: 每行文本需包装为 {"text": "..."} 的 JSON Component, 客户端回传已是该格式。
        // #10 修复: 空行必须原样保存客户端回传的空字符串(""), 原版空行 = Component.empty()
        // 的 JSON 序列化即空串; 曾把空行改写为 {"text":""} -> 客户端把该字面量当文本渲染,
        // 告示牌上显示出 {"text":""} 字样而非空行。
        java.util.List<String> safe = new java.util.ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            safe.add(i < lines.size() ? lines.get(i) : "");
        }
        String side = isFront ? "front_text" : "back_text";
        org.cloudburstmc.nbt.NbtMapBuilder tb = org.cloudburstmc.nbt.NbtMap.builder();
        tb.putList("messages", org.cloudburstmc.nbt.NbtType.STRING, safe);
        tb.putString("color", "black");
        tb.putBoolean("has_glowing_text", false);
        b.put(side, tb.build());
        // Bug7 修复: 另一侧缺失时补默认空文本。旧存档/旧版本创建的告示牌 BE 只有
        // 单侧(或 legacy Text1-4 字段), 1.20+ 渲染器只认 front_text/back_text ->
        // 重进后"只有最后编辑的一面有字"。
        String otherSide = isFront ? "back_text" : "front_text";
        if (!b.containsKey(otherSide)) {
            b.put(otherSide, org.cloudburstmc.nbt.NbtMap.builder()
                .putList("messages", org.cloudburstmc.nbt.NbtType.STRING, java.util.List.of("", "", "", ""))
                .putString("color", "black")
                .putBoolean("has_glowing_text", false)
                .build());
        }
        org.cloudburstmc.nbt.NbtMap updated = b.build();
        chunk.setBlockEntity(x & 15, y, z & 15, updated);
        // 广播 block_update(0x09 block_entity_data) 让附近客户端刷新告示牌渲染
        int state = WorldManager.getBlockState(this.currentDim, x, y, z);
        broadcastBlockEntityData(x, y, z, state, updated);
    }

    /** 向附近玩家广播方块实体数据(0x09 block_entity_data: pos + typeId + data NBT)。 */
    private void broadcastBlockEntityData(int x, int y, int z, int state, org.cloudburstmc.nbt.NbtMap be) {
        String id = be.getString("id", "minecraft:chest");
        if (id.startsWith("minecraft:")) id = id.substring(10);
        int typeId = blockEntityTypeIdFor(id);
        org.cloudburstmc.nbt.NbtMapBuilder payload = org.cloudburstmc.nbt.NbtMap.builder();
        for (String key : be.keySet()) {
            if (key.equals("id") || key.equals("x") || key.equals("y") || key.equals("z")) continue;
            Object val = be.get(key);
            if (val instanceof String || val instanceof Integer || val instanceof Long
                || val instanceof Byte || val instanceof Short || val instanceof Float
                || val instanceof Double || val instanceof org.cloudburstmc.nbt.NbtMap
                || val instanceof org.cloudburstmc.nbt.NbtList
                || val instanceof int[] || val instanceof long[] || val instanceof byte[]) {
                payload.put(key, val);
            }
        }
        final int fType = typeId;
        final org.cloudburstmc.nbt.NbtMap fPayload = payload.build();
        for (NetworkHandler p : players.values()) {
            if (p.ctx == null || !p.ctx.channel().isActive()) continue;
            if (p.currentDim != this.currentDim) continue;
            int dx = (int) p.x - x, dz = (int) p.z - z;
            if (dx * dx + dz * dz > 16384) continue;
            p.sendPacket(p.ctx, 0x06, pb -> {
                pb.writePosition(x, y, z);
                pb.writeVarInt(fType);
                pb.writeAnonymousNbt(fPayload);
            });
        }
    }

    private int blockEntityTypeIdFor(String name) {
        return com.CharunCore.server.utils.RegistryHelper.blockEntityTypeId(name);
    }

    // ── 问题 3：床放置（foot + head）────────────────────
    private void placeBed(int fx, int fy, int fz, String bedName, String facing) {
        int[] off = BlockStateHelper.facingOffset(facing);
        int hx = fx + off[0], hz = fz + off[1];

        java.util.Map<String,String> footProps = new java.util.LinkedHashMap<>();
        footProps.put("facing", facing); footProps.put("occupied", "false"); footProps.put("part", "foot");
        int footState = BlockStateHelper.getState(bedName, footProps);

        java.util.Map<String,String> headProps = new java.util.LinkedHashMap<>();
        headProps.put("facing", facing); headProps.put("occupied", "false"); headProps.put("part", "head");
        int headState = BlockStateHelper.getState(bedName, headProps);

        WorldManager.setBlock(this.currentDim,fx, fy, fz, footState);
        WorldManager.setBlock(this.currentDim,hx, fy, hz, headState);
        broadcastBlockChange(this.currentDim, fx, fy, fz, footState);
        broadcastBlockChange(this.currentDim, hx, fy, hz, headState);
    }

    // ── 门放置（下半 + 上半）────────────────────────────
    private void placeDoor(int x, int y, int z, String doorName, String facing) {
        java.util.Map<String,String> lower = new java.util.LinkedHashMap<>();
        lower.put("facing", facing); lower.put("half", "lower");
        lower.put("hinge", "left");  lower.put("open", "false"); lower.put("powered", "false");

        java.util.Map<String,String> upper = new java.util.LinkedHashMap<>(lower);
        upper.put("half", "upper");

        int lowerState = BlockStateHelper.getState(doorName, lower);
        int upperState = BlockStateHelper.getState(doorName, upper);

        WorldManager.setBlock(this.currentDim,x, y,   z, lowerState);
        WorldManager.setBlock(this.currentDim,x, y+1, z, upperState);
        broadcastBlockChange(this.currentDim, x, y,   z, lowerState);
        broadcastBlockChange(this.currentDim, x, y+1, z, upperState);
    }

    // ── 问题 5：方块交互处理 ────────────────────────────
    /**
     * 床交互：主世界白天只设置复活点；夜晚/雷雨时睡觉跳到清晨。
     * 下界/末地睡床会爆炸（原版行为）。
     */
    private void handleBedUse(int x, int y, int z) {
        if (currentDim != DimensionType.OVERWORLD) {
            WorldManager.setBlock(currentDim, x, y, z, 0);
            broadcastBlockChange(currentDim, x, y, z, 0);
            ExplosionEngine.explode(
                    currentDim, x + 0.5, y + 0.5, z + 0.5, 5.0f, true);
            return;
        }

        double dx = this.x - (x + 0.5), dy = this.y - y, dz = this.z - (z + 0.5);
        if (dx * dx + dy * dy + dz * dz > 100.0) {
            sendFeedback("§c床太远了", "white");
            return;
        }

        boolean firstTime = data.respawnY == Integer.MIN_VALUE
                || data.respawnX != x || data.respawnZ != z;
        data.respawnX = x;
        data.respawnY = y;
        data.respawnZ = z;
        data.respawnDimension = "minecraft:overworld";
        if (firstTime) sendFeedback("§7已设置重生点", "white");

        long t = Main.dayTime % 24000;
        boolean night = t >= 12542 && t <= 23459;
        if (!night) {
            sendFeedback("§c你只能在夜间睡觉", "white");
            return;
        }

        if (!isBedSafe(x, y, z)) {
            sendFeedback("§c你无法在此休息, 附近有怪物", "white");
            return;
        }

        var bedEvent = EVENTS.fire(new com.CharunCore.server.plugin.event.events.PlayerBedEnterEvent(this, x, y, z));
        if (bedEvent.isCancelled()) return;

        Main.dayTime = 0;
        for (NetworkHandler p : players.values()) {
            p.sendPacket(p.ctx, 0x6F, pb -> {
                pb.writeLong(0);
                pb.writeLong(0);
                pb.writeBoolean(true);
            });
        }
        broadcastSystemMessage(username + " 睡了一觉, 天亮了", "yellow");
    }

    /** 重生锚: 下界内右键设置复活点; 手持萤石充能(每块+1, 上限4); 主世界/末地右键爆炸。 */
    private void handleRespawnAnchorUse(int x, int y, int z) {
        int stateId = WorldManager.getBlockState(this.currentDim, x, y, z);
        String charges = BlockStateHelper.getProp(stateId, "charges");
        int c = charges == null ? 0 : Integer.parseInt(charges);

        if (this.currentDim != DimensionType.THE_NETHER) {
            // 在主世界/末地使用重生锚会爆炸(原版行为)
            WorldManager.setBlock(this.currentDim, x, y, z, 0);
            broadcastBlockChange(this.currentDim, x, y, z, 0);
            ExplosionEngine.explode(
                    this.currentDim, x + 0.5, y + 0.5, z + 0.5, 5.0f, true);
            return;
        }

        int held = data.inventoryIds[36 + heldItemSlot];
        String heldName = BlockManager.itemIdToName(held);
        if ("glowstone".equals(heldName)) {
            if (c < 4) {
                int ns = BlockStateHelper.withProp(stateId, "charges", String.valueOf(c + 1));
                WorldManager.setBlock(this.currentDim, x, y, z, ns);
                broadcastBlockChange(this.currentDim, x, y, z, ns);
                if (gameMode == 0) {
                    data.inventoryCounts[36 + heldItemSlot]--;
                    if (data.inventoryCounts[36 + heldItemSlot] <= 0) {
                        data.inventoryIds[36 + heldItemSlot] = 0;
                        data.inventoryCounts[36 + heldItemSlot] = 0;
                    }
                    sendInventoryUpdate();
                }
            }
            return;
        }

        if (c <= 0) { sendFeedback("§c重生锚没有能量", "white"); return; }
        data.respawnX = x; data.respawnY = y; data.respawnZ = z;
        data.respawnDimension = "minecraft:the_nether";
        sendFeedback("§7已设置重生点(重生锚)", "white");
    }

    /** 床附近 8 格内是否有敌对生物。 */
    private boolean isBedSafe(int x, int y, int z) {
        for (Entity e :
                EntityManager.getAllEntities()) {
            if (!(e instanceof MobEntity m)) continue;
            if (m.dim != currentDim || !m.isHostile()) continue;
            double dx = m.x - x, dy = m.y - y, dz = m.z - z;
            if (dx * dx + dy * dy + dz * dz <= 64.0) return false;
        }
        return true;
    }

    // ---- /locate 结构定位 (路由到 StructureSet 真实放置数学) ----

    private static StructureSet resolveStructureSet(String name) {
        name = name.toLowerCase();
        StructureSet direct =
            StructureSet.get(name);
        if (direct != null) return direct;
        if (!name.endsWith("s")) {
            StructureSet pl =
                StructureSet.get(name + "s");
            if (pl != null) return pl;
        }
        java.util.Map<String, StructureSet> all =
            StructureSet.loadAll();
        for (java.util.Map.Entry<String, StructureSet> en : all.entrySet()) {
            StructureSet ss = en.getValue();
            if (ss == null) continue; // 跳过无法加载(如 concentric_rings)的结构集，避免 NPE
            if (en.getKey().contains(name)) return ss;
            for (StructureSelectionEntry e : ss.getStructures()) {
                if (e.structureId().contains(name)) return ss;
            }
        }
        return null;
    }

    /** #9: 要塞定位 —— 与生成器同源(DensityRouterChunkGenerator.isStrongholdRingChunk 使用
     *  同一 StructureSet.get("strongholds").getRings().ringPositions(seed)), 取距玩家最近的环点。 */
    private int[] locateStrongholdRing(int playerChunkX, int playerChunkZ, long seed) {
        StructureSet set = StructureSet.get("strongholds");
        if (set == null || set.getRings() == null) return null;
        long bestDist = Long.MAX_VALUE;
        int[] best = null;
        for (int[] cp : set.getRings().ringPositions(seed)) {
            long dx = (long) cp[0] - playerChunkX;
            long dz = (long) cp[1] - playerChunkZ;
            long dist = dx * dx + dz * dz;
            if (dist < bestDist) {
                bestDist = dist;
                best = cp;
            }
        }
        return best;
    }

    public int[] locateStructure(String name, int playerChunkX, int playerChunkZ, long seed) {
        StructureSet set = resolveStructureSet(name);
        if (set == null || set.getPlacement() == null) return null;
        RandomSpreadStructurePlacement pl = set.getPlacement();
        int maxRing = 128;
        for (int r = 0; r <= maxRing; r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != r) continue;
                    int cx = playerChunkX + dx;
                    int cz = playerChunkZ + dz;
                    if (!pl.isStructureChunk(seed, cx, cz)) continue;
                    if (biomeValidForSet(set, cx, cz)) return new int[]{cx, cz};
                }
            }
        }
        return null;
    }

    private boolean biomeValidForSet(StructureSet set, int cx, int cz) {
        DensityRouterChunkGenerator gen =
            WorldManager.getGenerator(this.currentDim);
        if (gen == null) return true;
        int centerX = (cx << 4) + 8;
        int centerZ = (cz << 4) + 8;
        int biome = gen.getColumnBiome(centerX, centerZ);
        for (StructureSelectionEntry e : set.getStructures()) {
            StructureRegistry.ConfiguredStructure cs =
                StructureRegistry.get(e.structureId());
            if (cs == null) continue;
            java.util.Set<Integer> biomes =
                BiomeTagResolver.getBiomesForStructure(cs.biomesTag);
            if (biomes != null && biomes.contains(biome)) return true;
        }
        return false;
    }

    /** 营火内容变化后持久化 BE 并广播 block_entity_data(#15 架上食物实时显示)。 */
    private void persistCampfireAndBroadcast(ContainerStore.Pos cp, ContainerStore.CampfireData cd) {
        org.cloudburstmc.nbt.NbtMap be = ContainerStore.persistCampfireAndGet(cp, cd);
        if (be == null) return;
        int st = WorldManager.getBlockState(this.currentDim, cp.x(), cp.y(), cp.z());
        for (NetworkHandler p : players.values()) {
            if (p.ctx == null || !p.ctx.channel().isActive()) continue;
            if (p.currentDim != cp.dim()) continue;
            p.broadcastBlockEntityData(cp.x(), cp.y(), cp.z(), st, be);
        }
    }

    /** 玩家是否站在告示牌正面(原版 SignBlockEntity.isFacingFrontText)。
     *  墙牌: 朝向 yaw = facing.toYRot(); 立牌: 朝向 yaw = rotation*22.5°(0=北,4=东,8=南,12=西)。 */
    private boolean signFacingFront(int x, int y, int z, int stateId) {
        try {
            String signName = BlockStateHelper.getName(stateId);
            boolean wall = signName != null && (signName.endsWith("_wall_sign") || signName.equals("wall_sign"));
            float yaw;
            if (wall) {
                String facing = BlockStateHelper.getProp(stateId, "facing");
                yaw = switch (facing) {
                    case "north" -> 180.0f; case "south" -> 0.0f; case "west" -> 90.0f; case "east" -> -90.0f;
                    default -> 0.0f;
                };
            } else {
                String rot = BlockStateHelper.getProp(stateId, "rotation");
                int r = 0;
                if (rot != null) { try { r = Integer.parseInt(rot.trim()); } catch (NumberFormatException ignored) {} }
                yaw = r * 22.5f;
            }
            double d1 = this.x - (x + 0.5);
            double d2 = this.z - (z + 0.5);
            float playerAngle = (float) (Math.atan2(d2, d1) * 57.29577951308232) - 90.0f;
            float diff = Math.abs(180.0f - Math.abs(Math.abs(yaw - playerAngle) % 360.0f - 180.0f));
            return diff <= 90.0f;
        } catch (Exception e) {
            return true;
        }
    }

    private void handleBlockInteraction(int x, int y, int z, int stateId, int heldItemId) {
        String name = BlockStateHelper.getName(stateId);
        int newState = stateId;
        System.out.println("[DBG interact] " + name + " @ " + x + "," + y + "," + z);

        if (name.endsWith("_door") || name.endsWith("_trapdoor")) {
            // 门：同步切换上下两半的 open 属性
            newState = BlockStateHelper.toggleBool(stateId, "open");
            String half = BlockStateHelper.getProp(stateId, "half");
            int otherY = "lower".equals(half) ? y + 1 : y - 1;
            int otherState = WorldManager.getBlockState(this.currentDim,x, otherY, z);
            int otherNew   = BlockStateHelper.toggleBool(otherState, "open");
            WorldManager.setBlock(this.currentDim,x, otherY, z, otherNew);
            broadcastBlockChange(this.currentDim, x, otherY, z, otherNew);

        } else if (name.equals("lever")) {
            newState = BlockStateHelper.toggleBool(stateId, "powered");
            // 拉杆触发红石更新
            WorldManager.setBlock(this.currentDim,x, y, z, newState);
            broadcastBlockChange(this.currentDim, x, y, z, newState);
            RedstoneEngine.onBlockChanged(this.currentDim, x, y, z);
            return;

        } else if (name.endsWith("_button")) {
            newState = BlockStateHelper.withProp(stateId, "powered", "true");
            WorldManager.setBlock(this.currentDim,x, y, z, newState);
            broadcastBlockChange(this.currentDim, x, y, z, newState);
            RedstoneEngine.onBlockChanged(this.currentDim, x, y, z);
            // 2秒后复位
            final int bx = x, by = y, bz = z, offState = BlockStateHelper.withProp(stateId, "powered", "false");
            ctx.executor().schedule(() -> {
                WorldManager.setBlock(this.currentDim,bx, by, bz, offState);
                broadcastBlockChange(this.currentDim, bx, by, bz, offState);
                RedstoneEngine.onBlockChanged(this.currentDim, bx, by, bz);
            }, 2, java.util.concurrent.TimeUnit.SECONDS);
            return;

        } else if (name.endsWith("_gate") || name.endsWith("_fence_gate")) {
            newState = BlockStateHelper.toggleBool(stateId, "open");

        } else if (name.equals("comparator")) {
            // 比较器右键: 切换 比较(compare) / 减法(subtract) 模式
            String cur = BlockStateHelper.getProp(stateId, "mode");
            newState = BlockStateHelper.withProp(stateId, "mode",
                "compare".equals(cur) ? "subtract" : "compare");
            WorldManager.setBlock(this.currentDim,x, y, z, newState);
            broadcastBlockChange(this.currentDim, x, y, z, newState);
            RedstoneEngine.onBlockChanged(this.currentDim, x, y, z);
            return;

        } else if (name.equals("note_block")) {
            // 原版: 右键音符盒调音 (note 0-24 循环), 并发出当前音色声音
            String cur = BlockStateHelper.getProp(stateId, "note");
            int note = cur != null ? Integer.parseInt(cur) : 0;
            note = (note + 1) % 25;
            newState = BlockStateHelper.withProp(stateId, "note", String.valueOf(note));
            WorldManager.setBlock(this.currentDim, x, y, z, newState);
            broadcastBlockChange(this.currentDim, x, y, z, newState);
            RedstoneEngine.playNoteManual(this.currentDim, x, y, z, newState);
            return;

        } else if (name.equals("repeater")) {
            // 原版: 右键中继器在 1/2/4 档延迟间循环 (delay 属性 1->2->3->4->1, 对应 2/4/6/8 tick)。
            // 曾未处理右键 -> 放置逻辑覆盖 -> 无法调档。
            String cur = BlockStateHelper.getProp(stateId, "delay");
            int d = cur != null ? Integer.parseInt(cur) : 1;
            d = (d >= 4) ? 1 : d + 1;
            newState = BlockStateHelper.withProp(stateId, "delay", String.valueOf(d));
            WorldManager.setBlock(this.currentDim, x, y, z, newState);
            broadcastBlockChange(this.currentDim, x, y, z, newState);
            return;

        } else if (name.equals("daylight_detector")) {
            // 原版: 右键切换 inverted(昼/夜感应模式)，并立即按昼夜重算 power
            boolean inv = "true".equals(BlockStateHelper.getProp(stateId, "inverted"));
            newState = BlockStateHelper.withProp(stateId, "inverted", inv ? "false" : "true");
            WorldManager.setBlock(this.currentDim, x, y, z, newState);
            broadcastBlockChange(this.currentDim, x, y, z, newState);
            RedstoneEngine.onBlockChanged(this.currentDim, x, y, z);
            return;

        } else if (name.equals("composter")) {
            int level = Integer.parseInt(BlockStateHelper.getProp(stateId, "level"));
            String heldName = BlockManager.itemIdToName(heldItemId);
            boolean consumed = gameMode == 0 ? consumeHeldItem(heldItemId) : (heldName != null);
            if (level < 8 && heldName != null && isCompostable(heldName) && consumed) {
                int chance = composterChance(heldName);
                if (java.util.concurrent.ThreadLocalRandom.current().nextInt(100) < chance) {
                    int nl = level + 1;
                    newState = BlockStateHelper.withProp(stateId, "level", String.valueOf(nl));
                    WorldManager.setBlock(this.currentDim,x, y, z, newState);
                    broadcastBlockChange(this.currentDim, x, y, z, newState);
                    if (nl == 8) {
                        spawnDrop(x, y + 1, z,
                            BlockManager.getItemIdByName("bone_meal"), 1);
                    }
                    return;
                }
            }
            return;

        } else if (name.equals("cauldron") || name.equals("water_cauldron") || name.equals("lava_cauldron")) {
            String heldName = BlockManager.itemIdToName(heldItemId);
            if ("water_bucket".equals(heldName)) {
                int ws = BlockStateHelper.withProp(
                    BlockStateHelper.getDefault("water_cauldron"), "level", "3");
                WorldManager.setBlock(this.currentDim,x, y, z, ws);
                broadcastBlockChange(this.currentDim, x, y, z, ws);
                if (gameMode == 0) replaceHeldWith(heldItemId,
                    BlockManager.getItemIdByName("bucket"));
                else giveItem(BlockManager.getItemIdByName("bucket"), 1);
                return;
            }
            if ("lava_bucket".equals(heldName)) {
                int ls = BlockStateHelper.getDefault("lava_cauldron");
                WorldManager.setBlock(this.currentDim,x, y, z, ls);
                broadcastBlockChange(this.currentDim, x, y, z, ls);
                if (gameMode == 0) replaceHeldWith(heldItemId,
                    BlockManager.getItemIdByName("bucket"));
                else giveItem(BlockManager.getItemIdByName("bucket"), 1);
                return;
            }
            if ("glass_bottle".equals(heldName) && !"lava_cauldron".equals(name)) {
                int lvl = "water_cauldron".equals(name)
                    ? Integer.parseInt(BlockStateHelper.getProp(stateId, "level")) : 0;
                if (lvl > 0) {
                    int ns = lvl == 1 ? BlockStateHelper.getDefault("cauldron")
                        : BlockStateHelper.withProp(
                            BlockStateHelper.getDefault("water_cauldron"),
                            "level", String.valueOf(lvl - 1));
                    WorldManager.setBlock(this.currentDim,x, y, z, ns);
                    broadcastBlockChange(this.currentDim, x, y, z, ns);
                    if (gameMode == 0) replaceHeldWith(heldItemId,
                            BlockManager.getItemIdByName("water_bottle"));
                    else giveItem(BlockManager.getItemIdByName("water_bottle"), 1);
                }
                return;
            }
            if ("bucket".equals(heldName) && "water_cauldron".equals(name)) {
                if ("3".equals(BlockStateHelper.getProp(stateId, "level"))) {
                    int ns = BlockStateHelper.getDefault("cauldron");
                    WorldManager.setBlock(this.currentDim,x, y, z, ns);
                    broadcastBlockChange(this.currentDim, x, y, z, ns);
                    if (gameMode == 0) replaceHeldWith(heldItemId,
                        BlockManager.getItemIdByName("water_bucket"));
                    else giveItem(BlockManager.getItemIdByName("water_bucket"), 1);
                }
                return;
            }
            return;
        }

        else if (name.endsWith("_bed")) {
            handleBedUse(x, y, z);
            return;
        } else if (name.equals("respawn_anchor")) {
            handleRespawnAnchorUse(x, y, z);
            return;
        } else if (name.equals("nether_portal")) {
            return;

        } else if (name.equals("end_portal")) {
            return;

        } else if (name.equals("chest") || name.endsWith("_chest") || name.equals("ender_chest")
                || name.equals("barrel") || name.endsWith("_shulker_box")) {
            boolean isEnder = name.equals("ender_chest");
            int windowId = nextWindowId();
            int[] slotIds = new int[27];
            int[] slotCounts = new int[27];
            String lootTableId = null;
            boolean hasLoot = false;

            Chunk chunk = WorldManager.getChunk(this.currentDim,x >> 4, z >> 4);
            if (chunk != null) {
                org.cloudburstmc.nbt.NbtMap be = chunk.getBlockEntity(x & 15, y, z & 15);
                if (be != null) {
                    lootTableId = be.containsKey("LootTable")
                        ? be.getString("LootTable") : null;
                    if (lootTableId != null) {
                        lootTableId = lootTableId.startsWith("minecraft:") ? lootTableId.substring(10) : lootTableId;
                        long lootSeed = be.containsKey("LootTableSeed") ? be.getLong("LootTableSeed") : (long) x * 341873128712L ^ (long) z * 132897987541L ^ y;
                        java.util.List<LootTableLoader.LootEntry> loot =
                            LootTableLoader.generateLoot(lootTableId, lootSeed);
                        int slot = 0;
                        for (LootTableLoader.LootEntry entry : loot) {
                            if (slot >= 27) break;
                            int itemId = BlockManager.getItemIdByName(entry.itemName);
                            if (itemId > 0) {
                                slotIds[slot] = itemId;
                                slotCounts[slot] = entry.count;
                                hasLoot = true;
                            }
                            slot++;
                        }
                        org.cloudburstmc.nbt.NbtMapBuilder beBuilder = org.cloudburstmc.nbt.NbtMap.builder();
                        for (String key : be.keySet()) {
                            if (key.equals("LootTable") || key.equals("LootTableSeed")) continue;
                            beBuilder.put(key, be.get(key));
                        }
                        chunk.setBlockEntity(x & 15, y, z & 15, beBuilder.build());
                    } else {
                        org.cloudburstmc.nbt.NbtList items = be.containsKey("Items")
                            ? (org.cloudburstmc.nbt.NbtList) be.get("Items") : null;
                        if (items != null) {
                            for (int i = 0; i < items.size() && i < 27; i++) {
                                org.cloudburstmc.nbt.NbtMap item = (org.cloudburstmc.nbt.NbtMap) items.get(i);
                                String itemName = item.getString("id");
                                itemName = itemName.startsWith("minecraft:") ? itemName.substring(10) : itemName;
                                slotIds[i] = BlockManager.getItemIdByName(itemName);
                                slotCounts[i] = item.containsKey("Count") ? item.getByte("Count") : 1;
                            }
                        }
                    }
                }
            }

            ContainerStore.Pos chestPos =
                new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.ChestData chestData = isEnder
                ? ContainerStore.enderChest(this.uuid)
                : ContainerStore.chest(chestPos);
            // chest() 已在首次访问时从 BE 载入; 此后内存为权威 (防漏斗先存入的物品被覆盖)
            for (int i = 0; i < 27; i++) {
                slotIds[i] = chestData.slots[i * 2];
                slotCounts[i] = chestData.slots[i * 2 + 1];
            }
            // Bug51: 大箱子 —— 相邻同类箱作为另一半, 打开 generic_9x6 (0-26 本箱 / 27-53 邻箱)
            ContainerStore.Pos partnerPos = null;
            if (!isEnder) {
                partnerPos = ContainerStore.findChestPartner(chestPos);
            }
            ContainerStore.ChestData partnerData = partnerPos != null ? ContainerStore.chest(partnerPos) : null;
            final int[] pSlotIds = new int[27];
            final int[] pSlotCounts = new int[27];
            if (partnerData != null) {
                for (int i = 0; i < 27; i++) {
                    pSlotIds[i] = partnerData.slots[i * 2];
                    pSlotCounts[i] = partnerData.slots[i * 2 + 1];
                }
            }
            if (isEnder) openEnderChests.put(windowId, this.uuid);
            else {
                openChests.put(windowId, chestPos);
                if (partnerPos != null) openChestPartners.put(windowId, partnerPos);
                chestObservers.computeIfAbsent(chestPos, k -> java.util.concurrent.ConcurrentHashMap.newKeySet()).add(this);
                ContainerStore.incrementViewers(chestPos); // P9-B2 陷阱箱查看者计数
                persistChest(chestPos); // 打开即持久化(奖励箱生成后立即落盘, 防未关闭即卸载丢物品 P9-B7)
                // 打开箱盖动画: block_event action=1 param=1 (open=true)
                sendChestBlockEvent(chestPos, true);
                // Bug51: 大箱子另一半同样计数/开盖动画
                if (partnerPos != null) {
                    chestObservers.computeIfAbsent(partnerPos, k -> java.util.concurrent.ConcurrentHashMap.newKeySet()).add(this);
                    ContainerStore.incrementViewers(partnerPos);
                    sendChestBlockEvent(partnerPos, true);
                }
            }
            final int[] fSlotIds = slotIds;
            final int[] fSlotCounts = slotCounts;
            final boolean doubleChest = partnerData != null;
            final int stateCount = doubleChest ? 90 : 63;
            org.cloudburstmc.nbt.NbtMap title = org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", name.endsWith("_shulker_box") ? "Shulker Box"
                        : name.equals("barrel") ? "Barrel"
                        : (isEnder ? "Ender Chest" : "Chest"))
                    .build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType(doubleChest ? "generic_9x6" : "generic_9x3"));
                pb.writeAnonymousNbt(title);
            });
            // 单箱窗口共 63 格: 0-26 箱子 / 27-53 主背包 / 54-62 快捷栏
            // Bug51: 大箱子窗口共 90 格: 0-26 本箱 / 27-53 邻箱 / 54-80 主背包 / 81-89 快捷栏
            sendPacket(ctx, 0x12, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(0);
                pb.writeVarInt(stateCount);
                for (int i = 0; i < 27; i++) {
                    writeChestSlot(pb, chestData, i);
                }
                if (doubleChest) {
                    for (int i = 0; i < 27; i++) {
                        writeChestSlot(pb, partnerData, i);
                    }
                    for (int s = 54; s <= 89; s++) {
                        int ps = s - 45;
                        writePlayerSlot(pb, ps);
                    }
                } else {
                    for (int s = 27; s <= 62; s++) {
                        int ps = s - 18;
                        writePlayerSlot(pb, ps);
                    }
                }
                writeCarriedSlot(pb);
            });
            return;
        } else if (name.equals("dispenser") || name.equals("dropper")) {
            int windowId = nextWindowId();
            ContainerStore.Pos dpos =
                new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.DispenserData dd =
                ContainerStore.dispenser(dpos);
            openDispensers.put(windowId, dpos);
            containerSyncVersion.put(windowId, dd.version);
            final int[] fDSlots = dd.slots;
            org.cloudburstmc.nbt.NbtMap dtitle = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", name.equals("dropper") ? "Dropper" : "Dispenser").build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType("generic_3x3"));
                pb.writeAnonymousNbt(dtitle);
            });
            sendPacket(ctx, 0x12, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(0);
                pb.writeVarInt(45); // 9 发射器/投掷器 + 36 玩家
                for (int i = 0; i < 9; i++) pb.writeSlot(fDSlots[i * 2], fDSlots[i * 2 + 1]);
                for (int ps = 9; ps <= 44; ps++) writePlayerSlot(pb, ps);
                writeCarriedSlot(pb);
            });
            return;
        } else if (name.equals("stonecutter")) {
            int windowId = nextWindowId();
            ContainerStore.Pos sp =
                new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.StonecutterData sd =
                ContainerStore.stonecutter(sp);
            openStonecutters.put(windowId, sp);
            containerSyncVersion.put(windowId, sd.version);
            final int[] fS = sd.slots;
            org.cloudburstmc.nbt.NbtMap stitle = org.cloudburstmc.nbt.NbtMap.builder().putString("text", "Stonecutter").build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType("stonecutter"));
                pb.writeAnonymousNbt(stitle);
            });
            sendProcessorContent(windowId, fS, 2);
            return;
        } else if (name.equals("grindstone")) {
            int windowId = nextWindowId();
            ContainerStore.Pos gp =
                new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.GrindstoneData gd =
                ContainerStore.grindstone(gp);
            openGrindstones.put(windowId, gp);
            containerSyncVersion.put(windowId, gd.version);
            final int[] fG = gd.slots;
            org.cloudburstmc.nbt.NbtMap gtitle = org.cloudburstmc.nbt.NbtMap.builder().putString("text", "Grindstone").build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType("grindstone"));
                pb.writeAnonymousNbt(gtitle);
            });
            sendProcessorContent(windowId, fG, 3);
            return;
        } else if (name.equals("crafting_table")) {
            int windowId = nextWindowId();
            openCraftingGrids.put(windowId, new String[9]);
            openCraftingCounts.put(windowId, new int[9]);
            org.cloudburstmc.nbt.NbtMap title = org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", "Crafting Table")
                    .build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType("crafting"));
                pb.writeAnonymousNbt(title);
            });
            sendCraftingTableContent(windowId);
            return;
        } else if (name.equals("furnace") || name.equals("blast_furnace") || name.equals("smoker")) {
            int windowId = nextWindowId();
            ContainerStore.Pos furnacePos =
                new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.FurnaceData furnaceData =
                ContainerStore.furnace(furnacePos, name);
            openFurnaces.put(windowId, furnacePos);
            containerSyncVersion.put(windowId, furnaceData.version);
            final int[] fFurnaceSlots = furnaceData.slots;
            String displayTitle = name.equals("blast_furnace") ? "Blast Furnace"
                : name.equals("smoker") ? "Smoker" : "Furnace";
            org.cloudburstmc.nbt.NbtMap title = org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", displayTitle)
                    .build();
            int menuType = RegistryHelper.menuType(name);
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(menuType);
                pb.writeAnonymousNbt(title);
            });
            sendPacket(ctx, 0x12, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(0);
                pb.writeVarInt(39);
                for (int i = 0; i < 3; i++) pb.writeSlot(fFurnaceSlots[i * 2], fFurnaceSlots[i * 2 + 1]);
                for (int i = 0; i < 36; i++) pb.writeSlot(data.inventoryIds[9 + i], data.inventoryCounts[9 + i]);
                writeCarriedSlot(pb);
            });
            return;
        } else if (name.equals("hopper")) {
            int windowId = nextWindowId();
            ContainerStore.Pos hopperPos =
                new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.HopperData hopperData =
                ContainerStore.hopper(hopperPos);
            openHoppers.put(windowId, hopperPos);
            final int[] fHopperSlots = hopperData.slots;
            org.cloudburstmc.nbt.NbtMap title = org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", "Hopper")
                    .build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType("hopper"));
                pb.writeAnonymousNbt(title);
            });
            sendPacket(ctx, 0x12, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(0);
                pb.writeVarInt(41); // 5 漏斗 + 36 玩家
                for (int i = 0; i < 5; i++) {
                    pb.writeSlot(fHopperSlots[i * 2], fHopperSlots[i * 2 + 1]);
                }
                for (int ps = 9; ps <= 44; ps++) {
                    writePlayerSlot(pb, ps);
                }
                writeCarriedSlot(pb);
            });
            return;
        } else if (name.equals("smithing_table")) {
            int windowId = nextWindowId();
            ContainerStore.Pos sp =
                new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.SmithingData sd =
                ContainerStore.smithing(sp);
            openSmithing.put(windowId, sp);
            containerSyncVersion.put(windowId, sd.version);
            org.cloudburstmc.nbt.NbtMap stitle = org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", "Smithing Table").build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType("smithing"));
                pb.writeAnonymousNbt(stitle);
            });
            // #19 原版 SmithingMenu 4 容器槽: 0=模板 1=基础装备 2=附加材料(下界合金锭) 3=结果。
            sendProcessorContent(windowId, sd.slots, 4);
            recomputeSmithing(windowId);
            return;
        } else if (name.equals("enchanting_table")) {
            int windowId = nextWindowId();
            ContainerStore.Pos ep =
                new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.EnchantingData ed =
                ContainerStore.enchanting(ep);
            openEnchanting.put(windowId, ep);
            containerSyncVersion.put(windowId, ed.version);
            ed.bookshelfCount = computeBookshelfCount(x, y, z);
            org.cloudburstmc.nbt.NbtMap etitle = org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", "Enchanting Table").build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType("enchantment"));
                pb.writeAnonymousNbt(etitle);
            });
            sendProcessorContent(windowId, ed.slots, 2);
            recomputeEnchanting(windowId);
            return;
        } else if (name.equals("anvil")) {
            int windowId = nextWindowId();
            ContainerStore.Pos ap =
                new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.AnvilData ad =
                ContainerStore.anvil(ap);
            ad.rename = ""; // Bug31: 每次打开铁砧清掉上一次会话残留的改名文本(原版菜单为一次性实例)
            openAnvil.put(windowId, ap);
            containerSyncVersion.put(windowId, ad.version);
            org.cloudburstmc.nbt.NbtMap atitle = org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", "Anvil").build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType("anvil"));
                pb.writeAnonymousNbt(atitle);
            });
            recomputeAnvil(windowId);
            return;
        } else if (name.equals("brewing_stand")) {
            int windowId = nextWindowId();
            ContainerStore.Pos bp =
                new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.BrewingData bd =
                ContainerStore.brewing(bp);
            openBrewing.put(windowId, bp);
            containerSyncVersion.put(windowId, bd.version);
            org.cloudburstmc.nbt.NbtMap btitle = org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("text", "Brewing Stand").build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType("brewing_stand"));
                pb.writeAnonymousNbt(btitle);
            });
            sendProcessorContent(windowId, bd.slots, 5);
            return;
        } else if (name.endsWith("_sign") || name.endsWith("_wall_sign")
                || name.equals("sign") || name.equals("wall_sign")) {
            // 右键告示牌打开编辑器(0x3A open_sign_editor: pos + isFrontText)。
            // #7 修复: 曾恒发 isFrontText=true -> 背面编辑也打开正面, 两面试图显示同一份文本。
            // 按玩家位置 vs 告示牌朝向判断(对齐原版 SignBlockEntity.isFacingFrontText)。
            boolean isFront = signFacingFront(x, y, z, stateId);
            final boolean fFront = isFront;
            sendPacket(ctx, 0x3A, pb -> {
                pb.writePosition(x, y, z);
                pb.writeBoolean(fFront);
            });
            return;
        } else if (name.equals("beacon")) {
            // 信标 UI: 打开 menu(原版 BeaconMenu, 玩家可放置支付物并选效果)。
            int windowId = nextWindowId();
            ContainerStore.Pos bp = new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.BeaconData bd = ContainerStore.beacon(bp);
            openBeacons.put(windowId, bp);
            beaconWindowId = windowId;
            org.cloudburstmc.nbt.NbtMap btitle = org.cloudburstmc.nbt.NbtMap.builder().putString("text", "Beacon").build();
            sendPacket(ctx, 0x39, pb -> {
                pb.writeVarInt(windowId);
                pb.writeVarInt(RegistryHelper.menuType("beacon"));
                pb.writeAnonymousNbt(btitle);
            });
            // 信标容器内容(空, 客户端按 beacon 菜单渲染支付槽)
            sendPacket(ctx, 0x12, pb -> {
                pb.writeVarInt(windowId); pb.writeVarInt(0); pb.writeVarInt(37);
                pb.writeSlot(0, 0);
                for (int ps = 9; ps <= 44; ps++) writePlayerSlot(pb, ps);
                writeCarriedSlot(pb);
            });
            // Bug11: 从区块 BE 回读已保存的效果(曾只在选择时写 BE, 打开时不读 -> 重启后效果丢失)
            Chunk bch = WorldManager.getChunk(this.currentDim, x >> 4, z >> 4);
            org.cloudburstmc.nbt.NbtMap bbe = bch != null
                ? bch.getBlockEntity(x & 15, y, z & 15) : null;
            if (bbe != null) {
                if (bbe.containsKey("Primary")) bd.primary = bbe.getInt("Primary", 0);
                if (bbe.containsKey("Secondary")) bd.secondary = bbe.getInt("Secondary", 0);
            }
            // 发送信标数据: 0=levels(金字塔层) 1=primary 2=secondary (原版 BeaconMenu DATA_COUNT=3)。
            // 层级需实时计算: 原版按下方基岩/铁块/金块/绿宝石块/钻石块金字塔判定 1-4 层。
            int levels = computeBeaconLevels(this.currentDim, x, y, z);
            bd.levels = levels;
            bd.version++;
            sendContainerProperty(windowId, 0, levels);
            sendContainerProperty(windowId, 1, bd.primary);
            sendContainerProperty(windowId, 2, bd.secondary);
            return;
        } else if (name.equals("command_block") || name.equals("chain_command_block")
                || name.equals("repeating_command_block")) {
            // #29 命令方块: 右键打开编辑器 UI。仅 OP(权限等级>=2) 或创造可编辑(原版 CommandBlockEditScreen)。
            // 修复: 原版命令方块 UI 不通过 open_screen(0x39) 打开 —— 客户端收到 block_entity_data(0x09)
            // 且 typeId=command_block 时自动弹出 CommandBlockEditScreen。曾误发 0x39 且 menuType 无
            // command_block 注册项(返回 0=generic_9x1) -> 弹错箱子/漏斗 UI 并因包流错乱断开。
            boolean canEdit = gameMode == 1 || opLevel() >= 2;
            ContainerStore.Pos cp = new ContainerStore.Pos(this.currentDim, x, y, z);
            ContainerStore.CommandBlockData cbd = ContainerStore.commandBlock(cp);
            cbd.hasPermission = canEdit;
            // 发送 block_entity_data(0x09): 客户端据此识别命令方块并自动打开编辑界面。
            sendCommandBlockData(cp, canEdit);
            return;
        } else if (name.equals("jukebox")) {
            // 唱片机: 手持唱片放入(播放), 空手右键取出(停止)。
            String heldItemName = heldItemId > 0 ? BlockManager.itemIdToName(heldItemId) : null;
            if (heldItemName != null && heldItemName.startsWith("music_disc_")) {
                // 放入唱片: 记录到 BE, 播放音效(0x19 level_sound_event, 唱片 id)。
                Chunk jChunk = WorldManager.getChunk(this.currentDim, x >> 4, z >> 4);
                if (jChunk != null) {
                    org.cloudburstmc.nbt.NbtMap jbe = jChunk.getBlockEntity(x & 15, y, z & 15);
                    org.cloudburstmc.nbt.NbtMapBuilder jb = org.cloudburstmc.nbt.NbtMap.builder();
                    if (jbe != null) for (String k : jbe.keySet()) jb.put(k, jbe.get(k));
                    else {
                        jb.putString("id", "minecraft:jukebox");
                        jb.putInt("x", x); jb.putInt("y", y); jb.putInt("z", z);
                    }
                    org.cloudburstmc.nbt.NbtMap recordItem = org.cloudburstmc.nbt.NbtMap.builder()
                        .putString("id", "minecraft:" + heldItemName)
                        .putByte("Count", (byte) 1).build();
                    jb.putCompound("RecordItem", recordItem);
                    jChunk.setBlockEntity(x & 15, y, z & 15, jb.build());
                    // #33 修复: 原版 JukeboxBlock 用 has_record 方块状态属性渲染唱片机顶部唱片,
                    // 曾只写 BE RecordItem -> 客户端不显示唱片也不播放。设 has_record=true 并广播。
                    int jState = WorldManager.getBlockState(this.currentDim, x, y, z);
                    if (BlockStateHelper.getProp(jState, "has_record") != null) {
                        int ns = BlockStateHelper.withProp(jState, "has_record", "true");
                        WorldManager.setBlock(this.currentDim, x, y, z, ns);
                        broadcastBlockChange(this.currentDim, x, y, z, ns);
                    }
                    // 广播音效: 唱片对应 sound event(1.21 唱片机曲目 sound_event = music_disc.<歌曲>,
                    // 如 music_disc_13 -> music_disc.13; 曾用 item.music_disc_13.play -> 客户端不播放)。
                    String songEvent = "minecraft:music_disc." + heldItemName.substring("music_disc_".length());
                    NetworkHandler.broadcastSoundAt(this.currentDim, x + 0.5, y + 0.5, z + 0.5,
                        songEvent, 4.0f, 1.0f);
                    if (gameMode == 0) {
                        int slot = 36 + heldItemSlot;
                        data.inventoryCounts[slot]--;
                        if (data.inventoryCounts[slot] <= 0) {
                            data.inventoryIds[slot] = 0;
                            data.inventoryCounts[slot] = 0;
                        }
                        sendSlotUpdate(0, slot);
                    }
                }
            } else {
                // 取出唱片: 弹出为掉落物
                Chunk jChunk = WorldManager.getChunk(this.currentDim, x >> 4, z >> 4);
                if (jChunk != null) {
                    org.cloudburstmc.nbt.NbtMap jbe = jChunk.getBlockEntity(x & 15, y, z & 15);
                    if (jbe != null && jbe.containsKey("RecordItem")) {
                        org.cloudburstmc.nbt.NbtMap rec = jbe.getCompound("RecordItem");
                        String recId = rec.getString("id", "");
                        if (recId.startsWith("minecraft:")) recId = recId.substring(10);
                        String songEvent = recId.startsWith("music_disc_")
                            ? "minecraft:music_disc." + recId.substring("music_disc_".length()) : null;
                        int recItemId = BlockManager.getItemIdByName(recId);
                        if (recItemId > 0) dropItemInFront(recItemId, 1);
                        org.cloudburstmc.nbt.NbtMapBuilder jb = org.cloudburstmc.nbt.NbtMap.builder();
                        for (String k : jbe.keySet()) if (!k.equals("RecordItem")) jb.put(k, jbe.get(k));
                        jChunk.setBlockEntity(x & 15, y, z & 15, jb.build());
                        // #33 取出: 复位 has_record=false 移除顶部唱片渲染
                        int jState2 = WorldManager.getBlockState(this.currentDim, x, y, z);
                        if (BlockStateHelper.getProp(jState2, "has_record") != null) {
                            int ns2 = BlockStateHelper.withProp(jState2, "has_record", "false");
                            WorldManager.setBlock(this.currentDim, x, y, z, ns2);
                            broadcastBlockChange(this.currentDim, x, y, z, ns2);
                        }
                        // Bug22 修复: 取出唱片时停掉正在播放的曲目 (原只复位 has_record 不发送 stop_sound
                        // -> 客户端唱片机循环音效持续播放不停止)。
                        if (songEvent != null) {
                            NetworkHandler.broadcastStopSound(this.currentDim, x + 0.5, y + 0.5, z + 0.5, songEvent);
                        }
                        NetworkHandler.broadcastSoundAt(this.currentDim, x + 0.5, y + 0.5, z + 0.5,
                            "minecraft:block.note_block.hat", 1.0f, 1.0f);
                    }
                }
            }
            return;
        }

        WorldManager.setBlock(this.currentDim,x, y, z, newState);
        broadcastBlockChange(this.currentDim, x, y, z, newState);
    }

    // =========================================================================
    // LOGIN SEQUENCE
    // =========================================================================

    private void sendLoginPlay(ChannelHandlerContext ctx) {
        loadPlayerData();
        players.put(this.uuid, this);
        this.eid = nextEid.incrementAndGet();

        // 0x30 Login (Play) — matches SpawnInfo structure from protocol 774
        sendPacket(ctx, 0x30, pb -> {
            pb.writeInt(this.eid);               // entityId
            pb.writeBoolean(false);              // isHardcore
            pb.writeVarInt(3);                   // worldNames count
            pb.writeString("minecraft:overworld");
            pb.writeString("minecraft:the_nether");
            pb.writeString("minecraft:the_end");
            pb.writeVarInt(ServerConfig.maxPlayers); // maxPlayers
            pb.writeVarInt(ServerConfig.viewDistance); // viewDistance
            pb.writeVarInt(ServerConfig.simulationDistance); // simulationDistance
            pb.writeBoolean(false);              // reducedDebugInfo
            pb.writeBoolean(true);               // enableRespawnScreen
            pb.writeBoolean(false);              // doLimitedCrafting
            // SpawnInfo (inline):
            pb.writeVarInt(this.currentDim.registryId); // dimension type registry index
            pb.writeString(this.currentDim.key);        // dimension name
            pb.writeLong(WorldManager.getSeed());       // hashedSeed
            pb.writeByte((byte) this.gameMode);  // gamemode
            pb.writeByte(-1);                    // previousGamemode (-1 = none)
            pb.writeBoolean(false);              // isDebug
            pb.writeBoolean(false);               // isFlat
            pb.writeBoolean(false);              // death location present
            // (no death coords since present=false)
            pb.writeVarInt(0);                   // portalCooldown
            pb.writeVarInt(this.currentDim.seaLevel); // seaLevel
            pb.writeBoolean(false);              // enforcesSecureChat
        });

        // Tab-list info for all players
        for (NetworkHandler other : players.values()) {
            other.sendPacket(other.ctx, 0x44, pb -> {
                pb.writeByte(0x01 | 0x04 | 0x08);
                pb.writeVarInt(1); pb.writeUUID(this.uuid); pb.writeString(this.username);
                writeProfileProperties(pb, this.profileProperties);
                pb.writeVarInt(1); pb.writeBoolean(true);
            });
            if (other != this) {
                this.sendPacket(this.ctx, 0x44, pb -> {
                    pb.writeByte(0x01 | 0x04 | 0x08);
                    pb.writeVarInt(1); pb.writeUUID(other.uuid); pb.writeString(other.username);
                    writeProfileProperties(pb, other.profileProperties);
                    pb.writeVarInt(1); pb.writeBoolean(true);
                });
            }
        }

        sendPacket(ctx, 0x18, pb -> { pb.writeString("minecraft:brand"); pb.writeString("CharunCore"); });

        // Recipe book: #13/#35 — 登录时全量推送配方显示(0x48, 含切石机/熔炉显示条目) +
        // declare_recipes(0x83, 槽位过滤集 + 切石机界面样式列表数据源), 之后再开启配方书面板。
        sendRecipeBook(ctx);
        sendDeclareRecipes(ctx);

        // Time, abilities, center chunk
        // 用真实 dayTime(非固定 6000), 否则进服先收到正午再被 tick 广播真实时间 -> 天空闪一下。
        sendPacket(ctx, 0x6F, pb -> { pb.writeLong(Main.worldAge); pb.writeLong(Main.dayTime); pb.writeBoolean(true); });
        // entity_event 24+等级 = 客户端权限等级(F3+F4/命令方块编辑等以此判定)。
        // 注意: ops.json 非空时按名单严格匹配(名字或UUID); 名单里没有的玩家=0 权限,
        // 不再像旧硬编码 28 一样人人 Lv4。控制台打印便于排查"F3+F4 无权限"。
        int loginOpLevel = Math.max(0, Math.min(4, opLevel()));
        System.out.println("[权限] " + username + " opLevel=" + loginOpLevel
            + " (uuid=" + this.uuid + ")");
        sendPacket(ctx, 0x22, pb -> {
            pb.writeInt(eid);
            pb.writeByte((byte) (24 + loginOpLevel));
        });
        // 兜底: 进服后 1 秒重发一次, 覆盖客户端 LocalPlayer 重建导致的权限态丢失
        final ChannelHandlerContext loginCtx = ctx;
        ctx.executor().schedule(() -> {
            if (loginCtx.channel().isActive() && !isDead) {
                int lvl = Math.max(0, Math.min(4, opLevel()));
                sendPacket(loginCtx, 0x22, pb -> {
                    pb.writeInt(this.eid);
                    pb.writeByte((byte) (24 + lvl));
                });
            }
        }, 1, java.util.concurrent.TimeUnit.SECONDS);
        sendPacket(ctx, 0x26, pb -> { pb.writeByte(13); pb.writeFloat(0.0f); });
        sendPacket(ctx, 0x5C, pb -> { pb.writeVarInt((int) this.x >> 4); pb.writeVarInt((int) this.z >> 4); });

        // Inventory
        sendPacket(ctx, 0x12, pb -> {
            pb.writeVarInt(0); pb.writeVarInt(1); pb.writeVarInt(46);
            for (int i = 0; i < 46; i++) writePlayerSlot(pb, i);
            writeCarriedSlot(pb);
        });

        // BUG2: 背包已同步, 按现持有物品解锁首批配方(例如已有木板则解锁木棍配方)
        checkRecipeUnlocks();

        // Synchronize player position
        sendPacket(ctx, 0x46, pb -> {
            pb.writeVarInt(1);
            pb.writeDouble(this.x); pb.writeDouble(this.y); pb.writeDouble(this.z);
            pb.writeDouble(0); pb.writeDouble(0); pb.writeDouble(0);
            pb.writeFloat(this.yaw); pb.writeFloat(this.pitch);
            pb.writeInt(0);
        });

        // Spawn existing players on this client, spawn this player on theirs
        for (NetworkHandler other : players.values()) {
            if (other == this) continue;
            this.spawnPlayerInstance(other);
            other.spawnPlayerInstance(this);
        }

        // Brigadier command tree (enables tab-completion)
        sendCommandsPacket(ctx);

        // Chunks
        sendInitialChunks(ctx);

        // 初次发送玩家属性(护甲条等), 之后仅装备变化时由 syncAttributesIfNeeded 重发
        lastSentArmorKey = -1;
        syncAttributesIfNeeded();

        // 成就系统：登录后下发全量成就树与进度 (P12)
        AdvancementManager.onLogin(this);

        // Keep-alive scheduler: 每秒探测, 60 秒无应答判超时断开。
        // #7: 大量区块下发时客户端网络线程被压缩/解码堵住, 应答会延迟到 30s+,
        // 曾被误判超时踢出("生成大量区块时总是莫名断开") -> 区块队列积压期间豁免超时。
        ctx.executor().scheduleAtFixedRate(() -> {
            if (ctx.channel().isActive()) {
                long now = System.currentTimeMillis();
                if (now - lastKeepaliveResponse > 60000) {
                    boolean chunkBurst = !pendingChunkSends.isEmpty();
                    if (chunkBurst) {
                        lastKeepaliveResponse = now - 45000; // 积压期间续期, 不累计到下次
                        return;
                    }
                    System.out.println("[网络] " + username + " keep-alive 超时, 断开连接");
                    ctx.close();
                    return;
                }
                sendPacket(ctx, 0x2B, pb -> pb.writeLong(now));
            }
        }, 5, 1, java.util.concurrent.TimeUnit.SECONDS);

        // #11 周期性自动存档: 原版每 ~30s 自动保存玩家数据, 防止退出/崩溃时物品栏丢失
        // (曾仅在 channelInactive 保存一次 -> 服务端强杀/断电时创造模式刚拿的物品不落盘)。
        ctx.executor().scheduleAtFixedRate(() -> {
            if (ctx.channel().isActive() && data != null) {
                savePlayerData();
            }
        }, 30, 30, java.util.concurrent.TimeUnit.SECONDS);

        // Join message (AFTER login is fully sent)
        var joinEvent = EVENTS.fire(new PlayerJoinEvent(
                this, username + " 加入了游戏"));
        if (!joinEvent.getJoinMessage().isEmpty()) {
            broadcastSystemMessage(joinEvent.getJoinMessage(), "yellow");
        }
    }

    // =========================================================================
    // CHUNK LOADING
    // =========================================================================

    private void sendInitialChunks(ChannelHandlerContext ctx) {
        int centerX = (int) x >> 4, centerZ = (int) z >> 4;
        System.out.println("[区块] 初始加载区块中心 (" + centerX + "," + centerZ + ") 坐标 (" + (int)x + "," + (int)z + ") 维度=" + this.currentDim);
        // update_view_position (0x5C): 告知客户端初始区块中心
        sendPacket(ctx, 0x5C, pb -> { pb.writeVarInt(centerX); pb.writeVarInt(centerZ); });
        // ChunkBatchStart (0x0C): 进入"加载地形中"界面（必须配对 ChunkBatchFinished 才能退出）
        sendPacket(ctx, 0x0C, pb -> {});

        DimensionType sendDim = this.currentDim;

        // ---- 同步立即发送中心区块（客户端"加载地形中"的解除关键）----
        try {
            Chunk centerChunk = WorldManager.getChunk(sendDim, centerX, centerZ);
            FluidEngine.scheduleChunkFluids(sendDim, centerChunk);
            long cKey = ((long)centerX << 32) | ((long)centerZ & 0xFFFFFFFFL);
            loadedChunks.add(cKey);
            sendPacket(ctx, 0x2C, pb -> ChunkEncoder.writeChunkPacket(pb, centerChunk));
            ctx.flush();
            System.out.println("[区块] 中心区块 (" + centerX + "," + centerZ + ") 已同步发送, sections=" + centerChunk.getSectionCount());
        } catch (Exception e) {
            System.err.println("[区块] 中心区块发送异常: " + e.getMessage());
            e.printStackTrace();
        }

        // ---- 其余区块入队, 由 tickSurvival 的 pumpChunkSends 每 tick 限量发送 ----
        // Bug44: 曾一次性把全部视距区块丢进 IO 线程同步生成+压缩 -> 进服初期帧率剧烈波动。
        int radius = VIEW_DISTANCE;
        java.util.List<long[]> order = new java.util.ArrayList<>();
        for (int d = 0; d <= radius * 2; d++) {
            int dx = (d <= radius) ? -d : d - radius - 1;
            for (int dz = -radius; dz <= radius; dz++) {
                if (Math.abs(dx) > radius) continue;
                if (dx == 0 && dz == 0) continue; // 中心块已发
                int nx = centerX + dx, nz = centerZ + dz;
                long key = ((long) nx << 32) | (nz & 0xFFFFFFFFL);
                if (!loadedChunks.contains(key) && !queuedChunkKeys.contains(key)) {
                    order.add(new long[]{(long) dx * dx + dz * dz, key, nx, nz});
                }
            }
        }
        order.sort((a, b) -> Long.compare(a[0], b[0]));
        for (long[] e : order) {
            queuedChunkKeys.add(e[1]);
            pendingChunkSends.add(new long[]{e[1], e[2], e[3]});
        }
        // ChunkBatchFinished: 中心块已同步发出, 客户端可立即退出"加载地形中"界面,
        // 其余区块随后每 tick 流式补发(与原版渐进加载一致)。
        final int fc = order.size();
        final DimensionType batchDim = sendDim;
        ctx.executor().execute(() -> {
            if (this.currentDim == batchDim && ctx.channel().isActive()) {
                sendPacket(ctx, 0x0B, pb -> pb.writeVarInt(fc + 1));
                System.out.println("[区块] 初始加载排队完成, 共 " + (fc + 1) + " 个区块(含中心块), 维度=" + batchDim);
            }
        });
    }

    // =========================================================================
    // CONFIG PACKETS
    // =========================================================================

    private void sendConfigPackets(ChannelHandlerContext ctx) {
        sendPacket(ctx, 0x0E, pb -> {
            pb.writeVarInt(1);
            pb.writeString("minecraft"); pb.writeString("core"); pb.writeString("1.21.11");
        });
        RegistryHelper.sendAllDumpedRegistries((pid, action) -> sendPacket(ctx, pid, action));
        File tagsFile = new File("dumped_registries/tags.bin");
        if (tagsFile.exists()) {
            try {
                byte[] tagsData = Files.readAllBytes(tagsFile.toPath());
                sendPacket(ctx, 0x0D, pb -> pb.writeRawBytes(tagsData));
            } catch (Exception e) { e.printStackTrace(); }
        }
        sendPacket(ctx, 0x03, pb -> {});
    }

    // =========================================================================
    // PLAYER DATA
    // =========================================================================

    private void loadPlayerData() {
        this.data = PlayerDataManager.load(uuid);
        if (this.data == null) this.data = new PlayerData();
        StatisticsManager.preload(this.uuid);
        this.x = data.x; this.y = data.y; this.z = data.z;
        this.gameMode = data.gameMode;
        this.currentDim = switch (data.dimension == null ? "" : data.dimension) {
            case "minecraft:the_nether" -> DimensionType.THE_NETHER;
            case "minecraft:the_end" -> DimensionType.THE_END;
            default -> DimensionType.OVERWORLD;
        };

        if (!data.spawnInitialized) {
            double[] sp = WorldManager.resolveWorldSpawn();
            this.currentDim = DimensionType.OVERWORLD;
            this.gameMode = ServerConfig.defaultGameModeId();
            data.gameMode = this.gameMode;
            this.x = sp[0]; this.y = sp[1]; this.z = sp[2];
            data.x = this.x; data.y = this.y; data.z = this.z;
            data.spawnInitialized = true;
            System.out.println("[登录] " + username + " 首次进服, 出生点 ("
                    + (int) this.x + ", " + (int) this.y + ", " + (int) this.z + ")");
        } else {
            ensureNotTrapped();
        }
    }

    /** 若玩家保存的位置被方块埋住或悬在虚空, 就地拉到该 XZ 的安全高度。 */
    private void ensureNotTrapped() {
        int bx = (int) Math.floor(this.x), by = (int) Math.floor(this.y), bz = (int) Math.floor(this.z);
        if (by < currentDim.minY || by > currentDim.minY + currentDim.height - 3) {
            this.y = findSafeArrivalY(currentDim, bx, bz);
            this.data.y = this.y;
            return;
        }
        if (isSuffocating(currentDim, bx, by, bz)) {
            double safe = findSafeArrivalY(currentDim, bx, bz);
            System.out.println("[登录] " + username + " 位置被埋 (" + bx + "," + by + "," + bz
                    + "), 已上移到 y=" + (int) safe);
            this.y = safe;
            this.data.y = safe;
        }
    }

    private boolean isSuffocating(DimensionType dim, int bx, int by, int bz) {
        // 仅当脚部与眼睛两格都是实心方块时才算被埋(登录防卡墙用)。
        // 眼睛格用 by+1 (站立时眼睛约在脚部之上 1~2 格), 不把单独脚部实心地面误判为窒息。
        return isSolidOpaque(WorldManager.getBlockState(dim, bx, by, bz))
                && isSolidOpaque(WorldManager.getBlockState(dim, bx, by + 1, bz));
    }

    private boolean isSolidOpaque(int stateId) {
        return BlockStateHelper.isSolidOpaque(stateId);
    }

    /** 放置目标格是否可放入: 空气/液体/非实心(草/花/火把等可替换)才允许。 */
/** 放置目标格是否可放入: 空气/液体/可替换(草、花、雪层等)才允许。
     *  原 !isSolidOpaque 过宽: 火/火把/红石粉等我非完整方块也可放入 -> "火焰堆叠/隔空放非完整方块"。 */
    private boolean canPlaceInto(int stateId) {
        // 原版: 只能放入空气/水/岩浆/可替换方块(草、花、雪层、菌丝等)。
        // 原 !isSolidOpaque 过宽: 火/火把/红石粉等非完整方块也算可放入 -> "火焰堆叠/隔空放非完整方块"。
        if (stateId == 0) return true;
        String n = BlockStateHelper.getName(stateId);
        if (n == null || n.equals("air") || n.equals("cave_air") || n.equals("void_air")) return true;
        if (n.equals("water") || n.equals("lava")) return true;
        return BlockStateHelper.isReplaceable(n);
    }

    /** Bug51: 箱子/陷阱箱相邻合并时同步原版 type 属性(single/left/right)。
     *  客户端靠该属性渲染大箱子模型, 曾不设置 → 两个箱子永远各自独立, 无法成大箱子。
     *  规则(原版 ChestBlock): 邻箱在 facing 顺时针侧 → 本箱 left/邻箱 right; 逆时针侧反之。
     *  任一箱上方有实体方块则不合并。 */
    private void updateChestType(int x, int y, int z) {
        int st = WorldManager.getBlockState(this.currentDim, x, y, z);
        String name = BlockStateHelper.getName(st);
        if (!"chest".equals(name) && !"trapped_chest".equals(name)) return;
        String facing = BlockStateHelper.getProp(st, "facing");
        if (facing == null || BlockStateHelper.getProp(st, "type") == null) return;
        String cw = switch (facing) {
            case "north" -> "east"; case "east" -> "south";
            case "south" -> "west"; default -> "north";
        };
        int cwX = x + ("east".equals(cw) ? 1 : "west".equals(cw) ? -1 : 0);
        int cwZ = z + ("south".equals(cw) ? 1 : "north".equals(cw) ? -1 : 0);
        int ccwX = x - ("east".equals(cw) ? 1 : "west".equals(cw) ? -1 : 0);
        int ccwZ = z - ("south".equals(cw) ? 1 : "north".equals(cw) ? -1 : 0);
        String nbCw = BlockStateHelper.getName(WorldManager.getBlockState(this.currentDim, cwX, y, cwZ));
        String nbCcw = BlockStateHelper.getName(WorldManager.getBlockState(this.currentDim, ccwX, y, ccwZ));
        int aboveState = WorldManager.getBlockState(this.currentDim, x, y + 1, z);
        boolean blocked = BlockStateHelper.isSolidOpaque(aboveState);
        String myType = "single";
        String otherType = "single";
        int[] otherPos = null;
        if (!blocked && name.equals(nbCw)) {
            myType = "left"; otherType = "right"; otherPos = new int[]{cwX, y, cwZ};
        } else if (!blocked && name.equals(nbCcw)) {
            myType = "right"; otherType = "left"; otherPos = new int[]{ccwX, y, ccwZ};
        }
        String curType = BlockStateHelper.getProp(st, "type");
        if (!myType.equals(curType)) {
            int ns = BlockStateHelper.withProp(st, "type", myType);
            WorldManager.setBlock(this.currentDim, x, y, z, ns);
            broadcastBlockChange(this.currentDim, x, y, z, ns);
        }
        if (otherPos != null) {
            int os = WorldManager.getBlockState(this.currentDim, otherPos[0], otherPos[1], otherPos[2]);
            if (!otherType.equals(BlockStateHelper.getProp(os, "type"))) {
                int ns = BlockStateHelper.withProp(os, "type", otherType);
                WorldManager.setBlock(this.currentDim, otherPos[0], otherPos[1], otherPos[2], ns);
                broadcastBlockChange(this.currentDim, otherPos[0], otherPos[1], otherPos[2], ns);
            }
        }
    }

    /** Bug60: 玩家死亡时向所有追踪者移除玩家实体模型(原版死亡动画后实体消失)。 */
    // Bug52: delayed eating/drinking state
    private long eatingFinishAt = 0L;
    private int eatingSlot = -1;
    private int eatingMode = 0; // 1=drink 2=eat

    private void tickEating() {
        if (eatingFinishAt == 0L) return;
        if (System.currentTimeMillis() < eatingFinishAt) return;
        int slot = eatingSlot;
        eatingFinishAt = 0L;
        eatingSlot = -1;
        int mode = eatingMode;
        eatingMode = 0;
        setUsingItem(false);
        if (slot < 0 || slot >= 46 || data.inventoryCounts[slot] <= 0) return;
        int itemId = data.inventoryIds[slot];
        String itemName = BlockManager.itemIdToName(itemId);
        var consumeEvent = EVENTS.fire(new com.CharunCore.server.plugin.event.events.PlayerItemConsumeEvent(this, itemName));
        if (consumeEvent.isCancelled()) return;
        sendSoundAt("minecraft:entity.player.burp", x, y, z, 0.5f, 1.0f);
        if (mode == 1) {
            String pt = data.inventoryPotion[slot];
            if (pt != null) {
                for (String e : pt.split(",")) {
                    String[] kv = e.split("\\|");
                    if (kv.length >= 3) {
                        try { addEffect(kv[0], Integer.parseInt(kv[1]), Integer.parseInt(kv[2])); }
                        catch (NumberFormatException ignored) {}
                    }
                }
            }
            data.inventoryCounts[slot]--;
            if (data.inventoryCounts[slot] <= 0) {
                data.inventoryIds[slot] = 0;
                data.inventoryEnchants[slot] = new java.util.HashMap<>();
                data.inventoryPotion[slot] = null;
            }
            giveItem(BlockManager.getItemIdByName("glass_bottle"), 1);
            sendInventoryUpdate();
            return;
        }
        int foodValue = getFoodValue(itemName);
        if (foodValue <= 0) return;
        data.food = Math.min(20, data.food + foodValue);
        data.saturation = Math.min(data.food,
            data.saturation + foodValue * getSaturationModifier(itemName));
        if (itemName.equals("golden_apple") || itemName.equals("enchanted_golden_apple")) {
            health = Math.min(20.0f, health + 4.0f);
            if (itemName.equals("enchanted_golden_apple")) {
                addEffect("regeneration", 1, 600);
                addEffect("absorption", 3, 4800);
                addEffect("resistance", 0, 6000);
                addEffect("fire_resistance", 0, 6000);
            } else {
                addEffect("regeneration", 1, 100);
                addEffect("absorption", 0, 2400);
            }
        }
        if (itemName.equals("rotten_flesh") || itemName.equals("spider_eye")
            || itemName.equals("poisonous_potato")) {
            addExhaustion(2.0f);
        }
        if (itemName.equals("chorus_fruit")) {
            chorusFruitTeleport();
        }
        sendHealthUpdate();
        data.inventoryCounts[slot]--;
        if (data.inventoryCounts[slot] <= 0) {
            data.inventoryIds[slot] = 0;
        }
        if (itemName.endsWith("_bucket") && !itemName.equals("milk_bucket")) {
            giveItem(BlockManager.getItemIdByName("bucket"), 1);
        }
        AdvancementManager.onConsumeItem(this, itemName);
        sendInventoryUpdate();
    }

    /** Bug52: throw splash/lingering potion. */
    private void throwPotion(boolean lingering) {
        double yawRad = Math.toRadians(this.yaw);
        double pitchRad = Math.toRadians(this.pitch);
        double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
        double dy = -Math.sin(pitchRad);
        double dz = Math.cos(yawRad) * Math.cos(pitchRad);
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len < 1.0E-6) return;
        double speed = 0.5;
        int slot = 36 + heldItemSlot;
        PotionEntity pot = new PotionEntity(EntityManager.allocateId(),
            lingering ? "lingering_potion" : "splash_potion",
            this.x, this.y + 1.5, this.z,
            dx / len * speed, dy / len * speed + 0.1, dz / len * speed,
            this, lingering, data.inventoryPotion[slot]);
        EntityManager.addEntity(pot);
        sendSoundAt("minecraft:entity.potion.throw", this.x, this.y + 1.5, this.z, 0.5f, 0.5f);
    }

    // Bug59: ongoing survival dig crack sync
    private int digProgressX, digProgressY, digProgressZ;
    private long digProgressStart;
    private float digProgressDurMs;
    private int digProgressStage = -1;

    private void clearDigProgress() {
        if (digProgressDurMs > 0.0f) {
            broadcastBlockBreakProgressExceptSelf(this.eid,
                digProgressX, digProgressY, digProgressZ, (byte) -1);
        }
        digProgressDurMs = 0.0f;
        digProgressStage = -1;
    }

    private void tickDigProgress() {
        if (digProgressDurMs <= 0.0f) return;
        long elapsed = System.currentTimeMillis() - digProgressStart;
        if (elapsed > digProgressDurMs + 500) {
            clearDigProgress();
            return;
        }
        int stage = (int) (elapsed / digProgressDurMs * 10.0f);
        if (stage < 0) stage = 0;
        if (stage > 9) stage = 9;
        if (stage != digProgressStage) {
            digProgressStage = stage;
            broadcastBlockBreakProgressExceptSelf(this.eid,
                digProgressX, digProgressY, digProgressZ, (byte) stage);
        }
    }

    private void despawnPlayerEntityForTrackers() {
        for (NetworkHandler p : players.values()) {
            if (p == this || p.ctx == null || p.currentDim != this.currentDim) continue;
            p.sendPacket(p.ctx, 0x4B, pb -> {
                pb.writeVarInt(1);
                pb.writeVarInt(this.eid);
            });
        }
    }

    /** 目标格是否落在玩家碰撞箱(AABB)内, 防止把方块放进自己身体。 */
    private boolean intersectsPlayer(int bx, int by, int bz) {        double pminX = x - 0.3, pmaxX = x + 0.3;
        double pminZ = z - 0.3, pmaxZ = z + 0.3;
        double pminY = y, pmaxY = y + 1.8;
        return (bx + 1 > pminX && bx < pmaxX)
            && (bz + 1 > pminZ && bz < pmaxZ)
            && (by + 1 > pminY && by < pmaxY);
    }

    /** Bug54: 目标格是否与同维度任意实体碰撞箱相交(原版 isUnobstructed 语义, 包含放置者自己:
     *  原版不能把有碰撞的方块放进自己的碰撞箱)。物品/经验球/箭等不阻挡放置。 */
    private boolean intersectsAnyEntity(int bx, int by, int bz) {
        double minX = bx, maxX = bx + 1, minY = by, maxY = by + 1, minZ = bz, maxZ = bz + 1;
        for (NetworkHandler p : players.values()) {
            if (p == null || p.ctx == null || !p.ctx.channel().isActive()) continue;
            if (p.currentDim != this.currentDim) continue;
            if (p.x + 0.3 > minX && p.x - 0.3 < maxX
                    && p.z + 0.3 > minZ && p.z - 0.3 < maxZ
                    && p.y + 1.8 > minY && p.y < maxY) return true;
        }
        for (com.CharunCore.server.world.entity.Entity e : com.CharunCore.server.world.entity.EntityManager.getEntities().values()) {
            if (e == null || e.dim != this.currentDim) continue;
            if (e instanceof com.CharunCore.server.world.entity.ItemEntity) continue;
            if (e instanceof com.CharunCore.server.world.entity.ArrowEntity) continue;
            if (e instanceof com.CharunCore.server.world.entity.ExperienceOrbEntity) continue;
            double half = e.width / 2.0;
            if (e.x + half > minX && e.x - half < maxX
                    && e.z + half > minZ && e.z - half < maxZ
                    && e.y + e.height > minY && e.y < maxY) return true;
        }
        return false;
    }

    /** 该非完整方块是否需要实心支撑才能放置(原版 canSurvive)。统一走 SupportEngine。 */
    private static boolean blockNeedsSupport(String name) {
        return com.CharunCore.server.world.SupportEngine.needsSupport(name);
    }

    /** 某方块是否可为非完整方块提供支撑(原版 canSurvive 的近似)。统一走 SupportEngine。 */
    private static boolean isSupportBlock(int state) {
        return com.CharunCore.server.world.SupportEngine.isSupportBlock(state);
    }

    /** 校验非完整方块的支撑面。clicks = 被点击的方块(state), 用于墙面支撑。统一走 SupportEngine。 */
    private boolean hasPlacementSupport(int ppX, int ppY, int ppZ, String blockName, int clickedState) {
        if (!blockNeedsSupport(blockName)) return true;
        return com.CharunCore.server.world.SupportEngine.hasSupport(this.currentDim, ppX, ppY, ppZ, blockName);
    }

    /** Bug34: 移动包坐标合法性 —— 非有限值/越界/单包瞬移>512 格一律拒收。 */
    private static boolean validPlayerPos(double rx, double ry, double rz) {
        return Double.isFinite(rx) && Double.isFinite(ry) && Double.isFinite(rz)
            && Math.abs(rx) <= 3.2e7 && Math.abs(rz) <= 3.2e7
            && ry > -2048.0 && ry < 2048.0;
    }

    private void savePlayerData() {
        if (this.data == null || this.uuid == null) return;
        // Bug34: 绝不把非有限坐标写进 playerdata(否则重启后玩家出生在 NaN, 物理坏死)。
        if (Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z)) {
            data.x = x; data.y = y; data.z = z;
        }
        data.gameMode = this.gameMode;
        data.dimension = this.currentDim.key;
        data.username = this.username;
        PlayerDataManager.save(uuid, data);
        StatisticsManager.saveAll();
    }

    // =========================================================================
    // PACKET SENDER
    // =========================================================================

    /**
     * Sends a packet to the client.
     *
     * Format: [VarInt(bodyLen) | VarInt(packetId) | payload]
     *
     * Uses a single contiguous ByteBuf to avoid the wrappedBuffer refCount bug
     * that caused IllegalReferenceCountException: refCnt: 0.
     */
    private static final boolean PACKET_LOG = Boolean.parseBoolean(System.getProperty("charun.debugPkt", "0"));
    private int pktSeq = 0;

    public void sendPacket(ChannelHandlerContext ctx, int id, java.util.function.Consumer<PacketBuffer> action) {
        // 1. Write body (packetId + payload) into a temporary buffer
        ByteBuf bodyBuf = Unpooled.buffer();
        PacketBuffer bodyPb = new PacketBuffer(bodyBuf);
        bodyPb.writeVarInt(id);
        action.accept(bodyPb);
        int bodyLen = bodyBuf.readableBytes();

        if (PACKET_LOG && ctx != null) {
            System.out.println("[pkt] seq=" + (pktSeq++) + " id=0x" + Integer.toHexString(id)
                + " bodyLen=" + bodyLen + " player=" + username);
        }

        // 2. Write header (length VarInt) + body into the final buffer,
        //    applying zlib framing when compression has been negotiated.
        ByteBuf out = Unpooled.buffer();
        PacketBuffer outPb = new PacketBuffer(out);
        int threshold = this.compressionThreshold;
        if (threshold >= 0) {
            if (bodyLen >= threshold) {
                byte[] compressed = Compression.compress(bodyBuf);
                outPb.writeVarInt(PacketBuffer.varIntSize(bodyLen) + compressed.length);
                outPb.writeVarInt(bodyLen);
                out.writeBytes(compressed);
            } else {
                outPb.writeVarInt(bodyLen + 1);
                outPb.writeVarInt(0);
                out.writeBytes(bodyBuf);
            }
        } else {
            outPb.writeVarInt(bodyLen);
            out.writeBytes(bodyBuf);
        }
        bodyBuf.release(); // temp buffer no longer needed

        // 3. Single writeAndFlush — Netty will auto-release 'out' after writing
        ctx.writeAndFlush(out);
    }

    /** 完整消费一个 DataComponentPatch(774 ItemStack 协议): added 组件数 + 每组件(type+dataLen+data) + removed 组件数 + 每组件 type。 */
    private static void skipComponentPatch(PacketBuffer in) {
        int added = in.readVarInt();
        for (int c = 0; c < added; c++) {
            in.readVarInt();                 // component type
            int len = in.readVarInt();       // data length
            in.getBuffer().skipBytes(len);   // data
        }
        int removed = in.readVarInt();
        for (int c = 0; c < removed; c++) {
            in.readVarInt();                 // component type
        }
    }

    /**
     * 跳过一个 1.21.11 HashedStack（用于 serverbound_container_click 等包）。
     * 线格式：Optional byte(0=空/1=有) → 若有则 [item(varint) + count(varint) + HashedPatchMap]。
     * HashedPatchMap = addedMap(size + {compType(varint), hash(int)}*) + removedSet(size + {compType(varint)}*)。
     */
    private static int skipHashedStack(PacketBuffer in) {
        int present = in.readByte() & 0xFF; // Optional: 0=empty, 1=present
        if (present == 0) return 0;          // 空 HashedStack
        int itemId = in.readVarInt();         // holderRegistry(ITEM) → 实际就是 item registry id
        int count = in.readVarInt();
        // skip HashedPatchMap
        int addedCount = in.readVarInt();
        for (int i = 0; i < addedCount; i++) {
            in.readVarInt();                  // component type (registry id)
            in.readInt();                     // hash (4 bytes)
        }
        int removedCount = in.readVarInt();
        for (int i = 0; i < removedCount; i++) {
            in.readVarInt();                  // component type (registry id)
        }
        return itemId;
    }
}
