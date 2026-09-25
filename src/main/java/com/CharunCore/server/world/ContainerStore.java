package com.CharunCore.server.world;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.entity.Entity;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.ItemEntity;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.CharunCore.server.Main;

public final class ContainerStore {

    public record Pos(DimensionType dim, int x, int y, int z) {}

    public static final class ChestData {
        public final int[] slots = new int[54];
        public volatile boolean loaded = false;
        public volatile int version = 0;
        // Bug4/33 修复: 每格物品的 NBT 组件（附魔/药水/自定义名/耐久），原仅存 id+count ->
        // 放入箱子的附魔书/药水重进后丢失全部 NBT。
        public final SlotMeta meta = new SlotMeta(27);
        public void clearChestSlot(int s) {
            slots[2 * s] = 0; slots[2 * s + 1] = 0;
            meta.clear(s);
        }
        public void setChestSlot(int s, int id, int cnt, int dmg,
                java.util.Map<Integer, Integer> ench, String pot, String name) {
            slots[2 * s] = id; slots[2 * s + 1] = cnt;
            meta.slotDamage[s] = dmg;
            meta.slotEnchants[s] = ench == null ? new java.util.HashMap<>() : new java.util.HashMap<>(ench);
            meta.slotPotion[s] = pot; meta.slotCustomName[s] = name;
        }
    }

    /** Bug4/33: 通用每槽组件并行数组(附魔/药水/自定义名/耐久)。熔炉/漏斗/发射器/
     *  酿造台/切石机/砂轮/锻造台/附魔台等容器共用, 与 ChestData 的离散字段语义一致。 */
    public static final class SlotMeta {
        public final int n;
        public final int[] slotDamage;
        public final java.util.Map<Integer, Integer>[] slotEnchants;
        public final String[] slotPotion;
        public final String[] slotCustomName;
        public SlotMeta(int n) {
            this.n = n;
            this.slotDamage = new int[n];
            this.slotEnchants = new java.util.HashMap[n];
            this.slotPotion = new String[n];
            this.slotCustomName = new String[n];
            for (int i = 0; i < n; i++) slotEnchants[i] = new java.util.HashMap<>();
        }
        public void clear(int s) {
            slotDamage[s] = 0; slotEnchants[s].clear(); slotPotion[s] = null; slotCustomName[s] = null;
        }
        public boolean has(int s) {
            return slotDamage[s] > 0 || !slotEnchants[s].isEmpty()
                    || slotPotion[s] != null || slotCustomName[s] != null;
        }
    }

    public static final class FurnaceData {
        public final int[] slots = new int[6];
        public final SlotMeta meta = new SlotMeta(3);
        public volatile int cookTime = 0;
        public volatile int cookTotal = 200;
        public volatile int burnTime = 0;     // 剩余燃烧时间 (litTime)
        public volatile int burnTotal = 0;    // 当前燃料总燃烧时间 (litDuration)
        public volatile boolean lit = false;
        public volatile String type = "furnace";
        public volatile float xpStore = 0.0f;
        public volatile int version = 0;
        public volatile boolean loaded = false;
    }

    public static final class HopperData {
        public final int[] slots = new int[10];
        public final SlotMeta meta = new SlotMeta(5);
        public volatile int version = 0;
        public volatile int transferCd = 0; // 每 8 tick 传输 1 个物品
        public volatile boolean loaded = false;
    }

    /** 锻造台: 原版 4 槽 → 0=template(下界合金升级模板), 1=base(钻石装备), 2=addition(下界合金锭), 3=result。 */
    public static final class SmithingData {
        public final int[] slots = new int[8]; // 4 槽 (id,count 并行)
        public final SlotMeta meta = new SlotMeta(4);
        public volatile int outTrimMaterial = -1;  // #19 纹饰: 输出 trim 材料 id (-1 无; 0=紫水晶是合法 id!)
        public volatile int outTrimPattern = -1;   // #19 纹饰: 输出 trim 图案 id (-1 无)
        public volatile int version = 0;
    }

    public static final class EnchantingData {
        public final int[] slots = new int[6]; // 0=item, 1=lapis, 2=result(preview)
        public final SlotMeta meta = new SlotMeta(2);
        public volatile int[] optionEnchant = new int[3]; // 各选项主附魔 id (0=无)
        public volatile int[] optionLevel = new int[3];
        public volatile int[] optionCost = new int[3];    // 各选项所需 xp 等级
        // 各选项完整附魔列表（原版一个选项可含 1-3 个附魔；应用时必须全部写入）
        public volatile java.util.List<EnchantSystem.EnchantInstance>[] optionEnchList =
            new java.util.ArrayList[]{new java.util.ArrayList<>(), new java.util.ArrayList<>(), new java.util.ArrayList<>()};
        public volatile int bookshelfCount = 0;
        public volatile long seed = 0;       // 附魔台稳定种子 (一次附魔后重掷)
        public volatile java.util.Map<Integer, Integer> resultEnch = new java.util.HashMap<>();
        public volatile int version = 0;
    }

    /** 信标: levels 由 NetworkHandler 自金字塔方块扫描填充; 选效果/校验在此完成。 */
    public static final class BeaconData {
        public volatile int levels = 0;            // 金字塔层数 0..4
        public volatile int primary = 0;           // 主效果协议 id (0=无)
        public volatile int secondary = 0;         // 副效果协议 id (0=无)
        public volatile boolean payment = false;   // 是否有支付物 (杏矿/金块等)
        public volatile boolean updating = false;  // 当前是否处于“确认中”
        public final int[] paymentSlot = new int[2]; // #15 支付物槽 (id, count) — 原版 SimpleContainer(1)
        public final SlotMeta meta = new SlotMeta(1);
        public volatile int version = 0;
    }

    /** 唱片机: 放入的唱片 id 与是否正在播放。 */
    public static final class JukeboxData {
        public volatile int recordId = 0;    // 当前唱片物品 id (0=空)
        public volatile boolean playing = false;
        public volatile int version = 0;
    }

    /** 命令方块: 打开/确认的数据与其权限判断。 */
    public static final class CommandBlockData {
        public volatile String command = "";
        public volatile int mode = 0;          // 0=连锁? 原版: 0=SEQUENCE(脉冲)? 这里 0=脉冲,1=连锁,2=重复
        public volatile boolean conditional = false;
        public volatile boolean auto = false;
        public volatile boolean trackOutput = true;
        public volatile String lastOutput = "";
        public volatile boolean hasPermission = false; // 玩家是否有权限(创造/OP)
        public volatile int version = 0;
    }

    public static final class AnvilData {
        public final int[] slots = new int[6]; // 0=left, 1=right, 2=output
        public volatile int cost = 0;          // 所需 xp 等级
        public volatile String rename = "";
        public volatile int version = 0;
        /** 计算结果物品携带的附魔(合并后)与自定义名, 取走结果时转移到光标物品。 */
        public volatile java.util.Map<Integer, Integer> outEnchants = new java.util.HashMap<>();
        public volatile String outName = null;
        public volatile int outId = 0;
        public volatile int outCount = 0;
        /** 左/右输入槽物品携带的附魔(放置时从光标转移过来)。 */
        public volatile java.util.Map<Integer, Integer> leftEnchants = new java.util.HashMap<>();
        public volatile java.util.Map<Integer, Integer> rightEnchants = new java.util.HashMap<>();
        /** 左/右输入槽物品已损伤耐久(铁砧修复合并用)。 */
        public volatile int leftDamage = 0;
        public volatile int rightDamage = 0;
        /** 输出物品已损伤耐久(取走结果时转移到光标)。 */
        public volatile int outDamage = 0;
        /** 输入/输出携带的药水类型字符串(与 inventoryPotion 同格式), 用于窗口内正确显示。 */
        public volatile String leftPotion = null;
        public volatile String rightPotion = null;
        public volatile String outPotion = null;
        /** 输入槽物品自定义名(窗口内显示), 输出名沿用 outName。 */
        public volatile String leftName = null;
        public volatile String rightName = null;
    }

    public static final class BrewingData {
        public final int[] slots = new int[10]; // 0-2=瓶子, 3=燃料(烈焰粉), 4=材料
        public final SlotMeta meta = new SlotMeta(5); // Bug4/33: 各槽附魔/自定义名/耐久
        public final String[] potionType = new String[3]; // 每个瓶子槽的药水效果字符串
        public volatile int brewTime = 0;
        public volatile int brewTotal = 400;
        public volatile int fuelTime = 0;
        public volatile int fuelTotal = 0;
        public volatile int version = 0;
        public volatile boolean loaded = false;
    }

    /** 营火/灵魂营火: 4 个食物槽, 每槽独立烹饪(原版 600 tick = 30 秒)。 */
    public static final class CampfireData {
        public final int[] slots = new int[8]; // 4 槽 (id,count 并行): 0/1,2/3,4/5,6/7
        public final int[] cookTime = new int[4]; // 每槽已烹饪 tick
        public volatile int version = 0;
        public volatile boolean loaded = false;
    }
    private static final Map<Pos, CampfireData> CAMPFIRES = new ConcurrentHashMap<>();
    public static CampfireData campfire(Pos p) {
        CampfireData d = CAMPFIRES.computeIfAbsent(p, k -> new CampfireData());
        if (!d.loaded) { loadCampfireFromBE(p, d); d.loaded = true; }
        return d;
    }
    public static CampfireData peekCampfire(Pos p) { return CAMPFIRES.get(p); }
    public static CampfireData removeCampfire(Pos p) { return CAMPFIRES.remove(p); }
    public static java.util.Collection<CampfireData> allCampfires() { return CAMPFIRES.values(); }

    public static final class DispenserData {
        public final int[] slots = new int[18]; // 9 槽 (id,count 并行)
        public final SlotMeta meta = new SlotMeta(9);
        public volatile int version = 0;
        public volatile boolean loaded = false;
    }

    private static final Map<Pos, DispenserData> DISPENSERS = new ConcurrentHashMap<>();

    // ── P5-#2: 切石机 / 砂轮 (进阶制造) ──
    public static final class StonecutterData {
        public final int[] slots = new int[4]; // 0=input, 1=result
        public final SlotMeta meta = new SlotMeta(2);
        public volatile int selectedIndex = -1;   // 当前选中配方索引 (-1=未选)
        public volatile int[] candidates = new int[0]; // #18 全部可行切制产物 (左侧样式列表)
        public volatile int version = 0;
    }
    public static final class GrindstoneData {
        public final int[] slots = new int[6]; // 0=inA, 1=inB, 2=result
        public final SlotMeta meta = new SlotMeta(3);
        public volatile int version = 0;
    }
    private static final Map<Pos, StonecutterData> STONECUTTERS = new ConcurrentHashMap<>();
    private static final Map<Pos, GrindstoneData> GRINDSTONES = new ConcurrentHashMap<>();
    public static StonecutterData stonecutter(Pos p) {
        return STONECUTTERS.computeIfAbsent(p, k -> new StonecutterData());
    }
    public static GrindstoneData grindstone(Pos p) {
        return GRINDSTONES.computeIfAbsent(p, k -> new GrindstoneData());
    }
    public static StonecutterData peekStonecutter(Pos p) { return STONECUTTERS.get(p); }
    public static GrindstoneData peekGrindstone(Pos p) { return GRINDSTONES.get(p); }

    public static DispenserData dispenser(Pos p) {
        DispenserData d = DISPENSERS.computeIfAbsent(p, k -> new DispenserData());
        if (!d.loaded) { loadDispenserFromBE(p, d); d.loaded = true; }
        return d;
    }

    public static DispenserData peekDispenser(Pos p) {
        return DISPENSERS.get(p);
    }

    public static DispenserData removeDispenser(Pos p) {
        return DISPENSERS.remove(p);
    }

    /** 首次访问时从区块 block entity 的 Items 载入 (结构生成/旧存档的发射器内容物)。 */
    private static void loadDispenserFromBE(Pos p, DispenserData d) {
        loadPairedFromBE(p, d.slots, 9, d.meta);
    }

    /** 通用: 从区块 BE 的 Items 载入 (id,count) 并行数组容器 (漏斗/酿造台)。 */
    private static void loadPairedFromBE(Pos p, int[] slots, int slotCount) {
        loadPairedFromBE(p, slots, slotCount, null);
    }

    /** Bug4/33: 载入时解析每槽 components(附魔/药水/自定义名/耐久), 与 writeContainerBlockEntity 对称。 */
    private static void loadPairedFromBE(Pos p, int[] slots, int slotCount, SlotMeta meta) {
        try {
            Chunk chunk =
                WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return;
            org.cloudburstmc.nbt.NbtMap be = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            if (be == null || !be.containsKey("Items")) return;
            org.cloudburstmc.nbt.NbtList items = (org.cloudburstmc.nbt.NbtList) be.get("Items");
            for (int i = 0; i < items.size(); i++) {
                org.cloudburstmc.nbt.NbtMap item = (org.cloudburstmc.nbt.NbtMap) items.get(i);
                String iname = item.getString("id");
                if (iname == null) continue;
                iname = iname.startsWith("minecraft:") ? iname.substring(10) : iname;
                int id = BlockManager.getItemIdByName(iname);
                if (id <= 0) continue;
                int cnt = item.containsKey("Count") ? item.getByte("Count", (byte) 1) : 1;
                if (cnt <= 0) cnt = 1;
                int slot = item.containsKey("Slot") ? item.getByte("Slot", (byte) i) : i;
                if (slot >= 0 && slot < slotCount) {
                    slots[slot * 2] = id;
                    slots[slot * 2 + 1] = cnt;
                    if (meta != null && item.containsKey("components")) {
                        PlayerDataManager.ItemComps c = PlayerDataManager.parseItemComponents(item);
                        meta.slotDamage[slot] = c.damage();
                        meta.slotEnchants[slot] = c.enchants() == null ? new java.util.HashMap<>() : new java.util.HashMap<>(c.enchants());
                        meta.slotPotion[slot] = c.potion();
                        meta.slotCustomName[slot] = c.customName();
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    /** 酿造台载入: 复用 loadPairedFromBE 读瓶子/燃料/材料槽, 同时把瓶子 tag.potion 还原到 potionType。 */
    private static void loadBrewingFromBE(Pos p, BrewingData d) {
        loadPairedFromBE(p, d.slots, 5, d.meta);
        try {
            Chunk chunk = WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return;
            org.cloudburstmc.nbt.NbtMap be = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            if (be == null || !be.containsKey("Items")) return;
            org.cloudburstmc.nbt.NbtList items = (org.cloudburstmc.nbt.NbtList) be.get("Items");
            for (int i = 0; i < items.size(); i++) {
                org.cloudburstmc.nbt.NbtMap item = (org.cloudburstmc.nbt.NbtMap) items.get(i);
                int slot = item.containsKey("Slot") ? item.getByte("Slot", (byte) i) : i;
                if (slot < 0 || slot > 2) continue;             // 仅 3 个瓶子槽带效果
                String potion = null;
                if (item.containsKey("tag")) {
                    org.cloudburstmc.nbt.NbtMap tag = (org.cloudburstmc.nbt.NbtMap) item.get("tag");
                    if (tag.containsKey("potion")) {
                        potion = tag.getString("potion");
                        if (potion != null && potion.startsWith("minecraft:")) potion = potion.substring(10);
                    }
                }
                if (potion == null) potion = d.meta.slotPotion[slot]; // Bug8: components 形式回读
                if (potion != null) d.potionType[slot] = potion;
            }
        } catch (Exception ignored) {}
    }

    /** 熔炉: 内容物 + 烧炼/燃烧进度一起从 BE 载入。 */
    private static void loadFurnaceFromBE(Pos p, FurnaceData d) {
        loadPairedFromBE(p, d.slots, 3, d.meta);
        try {
            Chunk chunk =
                WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return;
            org.cloudburstmc.nbt.NbtMap be = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            if (be == null) return;
            d.cookTime = be.getInt("CookTime", 0);
            d.cookTotal = Math.max(20, be.getInt("CookTimeTotal", 200));
            d.burnTime = be.getShort("BurnTime", (short) 0);
            d.burnTotal = Math.max(0, be.getShort("BurnTimeTotal", (short) 0));
            d.lit = d.burnTime > 0;
            d.xpStore = be.getFloat("xpStore", 0.0f);
        } catch (Exception ignored) {}
    }

    /** 把发射器/投掷器内容物写回区块 block entity (UI 关闭 / 发射后持久化)。 */
    public static void persistDispenser(Pos p) {
        DispenserData d = DISPENSERS.get(p);
        if (d == null) return;
        try {
            Chunk chunk =
                WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return;
            java.util.List<org.cloudburstmc.nbt.NbtMap> items = new java.util.ArrayList<>();
            for (int i = 0; i < 9; i++) {
                int id = d.slots[i * 2], cnt = d.slots[i * 2 + 1];
                if (id <= 0 || cnt <= 0) continue;
                String iname = BlockManager.itemIdToName(id);
                if (iname == null) continue;
                org.cloudburstmc.nbt.NbtMapBuilder ib = org.cloudburstmc.nbt.NbtMap.builder()
                    .putByte("Slot", (byte) i)
                    .putString("id", iname.startsWith("minecraft:") ? iname : "minecraft:" + iname)
                    .putByte("Count", (byte) Math.min(64, cnt));
                if (d.meta.has(i)) {
                    ib.putCompound("components", PlayerDataManager.buildItemComponents(iname,
                        d.meta.slotDamage[i], d.meta.slotEnchants[i], d.meta.slotPotion[i], d.meta.slotCustomName[i]));
                }
                items.add(ib.build());
            }
            org.cloudburstmc.nbt.NbtMap existing = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            org.cloudburstmc.nbt.NbtMapBuilder b = org.cloudburstmc.nbt.NbtMap.builder();
            if (existing != null) {
                for (String k : existing.keySet()) {
                    if (k.equals("Items")) continue;
                    b.put(k, existing.get(k));
                }
            } else {
                int st = WorldManager.getBlockState(p.dim(), p.x(), p.y(), p.z());
                String n = BlockStateHelper.getName(st);
                b.putString("id", "minecraft:" + (n == null ? "dispenser" : n));
                b.putInt("x", p.x()); b.putInt("y", p.y()); b.putInt("z", p.z());
            }
            b.putList("Items", org.cloudburstmc.nbt.NbtType.COMPOUND, items);
            chunk.setBlockEntity(p.x() & 15, p.y(), p.z() & 15, b.build());
        } catch (Exception ignored) {}
    }

    private static void flushDispensers() {
        for (Map.Entry<Pos, DispenserData> e : DISPENSERS.entrySet()) {
            Pos p = e.getKey();
            DispenserData d = e.getValue();
            Integer last = LAST_FLUSH_VERSION.get(p);
            if (last != null && last == d.version) continue;
            LAST_FLUSH_VERSION.put(p, d.version);
            persistDispenser(p);
        }
    }

    private static final Map<Pos, ChestData> CHESTS = new ConcurrentHashMap<>();
    private static final Map<Pos, FurnaceData> FURNACES = new ConcurrentHashMap<>();
    private static final Map<Pos, HopperData> HOPPERS = new ConcurrentHashMap<>();
    private static final Map<Pos, SmithingData> SMITHINGS = new ConcurrentHashMap<>();
    private static final Map<Pos, EnchantingData> ENCHANTINGS = new ConcurrentHashMap<>();
    private static final Map<Pos, AnvilData> ANVILS = new ConcurrentHashMap<>();
    private static final Map<Pos, BrewingData> BREWINGS = new ConcurrentHashMap<>();
    private static final Map<Pos, BeaconData> BEACONS = new ConcurrentHashMap<>();
    private static final Map<Pos, JukeboxData> JUKEBOXES = new ConcurrentHashMap<>();
    private static final Map<Pos, CommandBlockData> COMMANDBLOCKS = new ConcurrentHashMap<>();
    private static final Map<UUID, ChestData> ENDER_CHESTS = new ConcurrentHashMap<>();

    private ContainerStore() {}

    public static ChestData chest(Pos p) {
        ChestData d = CHESTS.get(p);
        if (d != null) return d;
        // Bug51: 移除"相邻箱共享同一 27 格 ChestData"的旧近似 —— 两口相邻箱子会互相
        // 覆盖内容(物品复制/丢失)。原版语义: 每箱独立 27 格存储, 大箱子由 UI 层
        // 打开 generic_9x6 把两箱各 27 格拼成 54 格。
        ChestData created = CHESTS.computeIfAbsent(p, k -> new ChestData());
        if (!created.loaded) {
            loadChestFromBE(p, created);
            created.loaded = true;
        }
        return created;
    }

    /** Bug51: 检测水平相邻同类箱(大箱子另一半)。返回 null = 无相邻同类箱。 */
    public static Pos findChestPartner(Pos p) {
        int st = WorldManager.getBlockState(p.dim(), p.x(), p.y(), p.z());
        String n = BlockStateHelper.getName(st);
        if (n == null) return null;
        if (!("chest".equals(n) || "trapped_chest".equals(n))) return null;
        int[][] dirs = {{0,0,-1},{0,0,1},{1,0,0},{-1,0,0}};
        for (int[] d : dirs) {
            Pos np = new Pos(p.dim(), p.x() + d[0], p.y() + d[1], p.z() + d[2]);
            int ns = WorldManager.getBlockState(np.dim(), np.x(), np.y(), np.z());
            String nn = BlockStateHelper.getName(ns);
            if (n.equals(nn) && ("chest".equals(nn) || "trapped_chest".equals(nn))) return np;
        }
        return null;
    }

    /** 箱子/木桶/潜影盒: 从区块 BE 的 Items 载入 (id,count) + 组件(附魔/药水/自定义名/耐久)。 */
    private static void loadChestFromBE(Pos p, ChestData d) {
        try {
            Chunk chunk = WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return;
            org.cloudburstmc.nbt.NbtMap be = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            if (be == null || !be.containsKey("Items")) return;
            org.cloudburstmc.nbt.NbtList items = (org.cloudburstmc.nbt.NbtList) be.get("Items");
            for (int i = 0; i < items.size(); i++) {
                org.cloudburstmc.nbt.NbtMap item = (org.cloudburstmc.nbt.NbtMap) items.get(i);
                String iname = item.getString("id");
                if (iname == null) continue;
                iname = iname.startsWith("minecraft:") ? iname.substring(10) : iname;
                int id = BlockManager.getItemIdByName(iname);
                if (id <= 0) continue;
                int cnt = item.containsKey("Count") ? item.getByte("Count", (byte) 1) : 1;
                if (cnt <= 0) cnt = 1;
                int slot = item.containsKey("Slot") ? item.getByte("Slot", (byte) i) : i;
                if (slot < 0 || slot >= 27) continue;
                PlayerDataManager.ItemComps c = PlayerDataManager.parseItemComponents(item);
                d.setChestSlot(slot, id, cnt, c.damage(), c.enchants(), c.potion(), c.customName());
            }
        } catch (Exception ignored) {}
    }

    // ── P9-B2: 陷阱箱查看者计数 → 红石信号 (clamp(viewers,0,15)) ─────────────
    private static final Map<Pos, Integer> CHEST_VIEWERS = new ConcurrentHashMap<>();

    public static void incrementViewers(Pos p) {
        int st = WorldManager.getBlockState(p.dim(), p.x(), p.y(), p.z());
        if (!"trapped_chest".equals(BlockStateHelper.getName(st))) return;
        CHEST_VIEWERS.merge(p, 1, Integer::sum);
        RedstoneEngine.updateNeighborsAt(p.dim(), p.x(), p.y(), p.z());
    }

    public static void decrementViewers(Pos p) {
        int st = WorldManager.getBlockState(p.dim(), p.x(), p.y(), p.z());
        if (!"trapped_chest".equals(BlockStateHelper.getName(st))) return;
        int v = CHEST_VIEWERS.getOrDefault(p, 0) - 1;
        if (v <= 0) CHEST_VIEWERS.remove(p);
        else CHEST_VIEWERS.put(p, v);
        RedstoneEngine.updateNeighborsAt(p.dim(), p.x(), p.y(), p.z());
    }

    public static int trappedChestSignal(Pos p) {
        int v = CHEST_VIEWERS.getOrDefault(p, 0);
        return Math.max(0, Math.min(15, v));
    }

    // ── P8-#5: 活塞推动容器时迁移 ContainerStore 数据 ─────────────────────────
    public static void moveContainerData(Pos from, Pos to) {
        ChestData cd = CHESTS.remove(from);
        if (cd != null) CHESTS.put(to, cd);
        FurnaceData fd = FURNACES.remove(from);
        if (fd != null) FURNACES.put(to, fd);
        HopperData hd = HOPPERS.remove(from);
        if (hd != null) HOPPERS.put(to, hd);
        BrewingData bd = BREWINGS.remove(from);
        if (bd != null) BREWINGS.put(to, bd);
        DispenserData dd = DISPENSERS.remove(from);
        if (dd != null) DISPENSERS.put(to, dd);
        BeaconData bc = BEACONS.remove(from);
        if (bc != null) BEACONS.put(to, bc);
        JukeboxData jb = JUKEBOXES.remove(from);
        if (jb != null) JUKEBOXES.put(to, jb);
        CommandBlockData cb = COMMANDBLOCKS.remove(from);
        if (cb != null) COMMANDBLOCKS.put(to, cb);
    }

    // ── P9-B5: 世界保存时统一把内存容器内容落盘到区块 block entity ───────────
    private static final Map<Pos, Integer> LAST_FLUSH_VERSION = new ConcurrentHashMap<>();

    public static void flushDirtyContainers() {
        flushAll(CHESTS);
        flushAll(FURNACES);
        flushAll(HOPPERS);
        flushAll(BREWINGS);
        flushDispensers();
        flushCampfires();
    }

    /** 营火烹饪: 每槽独立计时, 满足原版 600 tick(30秒)出熟食; 灵魂营火速度翻倍(原版 2x)。 */
    private static void tickCampfire(Pos pos, CampfireData f) {
        boolean changed = false;
        int st = WorldManager.getBlockState(pos.dim(), pos.x(), pos.y(), pos.z());
        String bname = BlockStateHelper.getName(st);
        boolean soul = "soul_campfire".equals(bname);
        for (int i = 0; i < 4; i++) {
            int id = f.slots[i * 2], cnt = f.slots[i * 2 + 1];
            if (id <= 0 || cnt <= 0) { f.cookTime[i] = 0; continue; }
            String iname = BlockManager.itemIdToName(id);
            SmeltingSystem.SmeltResult r = SmeltingSystem.getResult(iname, "furnace");
            if (r == null || !SmeltingSystem.isFood(iname)) { f.cookTime[i] = 0; continue; }
            int total = soul ? 300 : 600;
            f.cookTime[i]++;
            if (f.cookTime[i] >= total) {
                f.cookTime[i] = 0;
                int outId = BlockManager.getItemIdByName(r.resultItem);
                if (outId > 0) {
                    // #20 修复: 原版营火食物烤好后自动弹出为掉落物(不是留在槽里等玩家取)。
                    // 曾把熟食写回槽位 -> 玩家需重进/右键才看到熟食, 且槽位不自动清空。
                    f.slots[i * 2] = 0; f.slots[i * 2 + 1] = 0;
                    changed = true;
                    ItemEntity item = new ItemEntity(
                        EntityManager.allocateId(),
                        pos.x() + 0.5 + (i % 2 == 0 ? -0.3 : 0.3),
                        pos.y() + 0.6,
                        pos.z() + 0.5 + (i / 2 == 0 ? -0.3 : 0.3),
                        outId, 1);
                    item.dim = pos.dim();
                    item.vx = 0; item.vy = 0.1; item.vz = 0;
                    item.pickupDelay = 20;
                    EntityManager.addEntity(item);
                }
            } else {
                changed = true;
            }
        }
        if (changed) f.version++;
    }

    private static void flushCampfires() {
        for (Map.Entry<Pos, CampfireData> e : CAMPFIRES.entrySet()) {
            Pos p = e.getKey();
            CampfireData d = e.getValue();
            Integer last = LAST_FLUSH_VERSION.get(p);
            if (last != null && last == d.version) continue;
            LAST_FLUSH_VERSION.put(p, d.version);
            persistCampfire(p, d);
        }
    }

    /** 营火: 持久化 Items 到区块 BE 并返回 NBT(供 0x06 block_entity_data 广播渲染架上食物)。 */
    public static org.cloudburstmc.nbt.NbtMap persistCampfireAndGet(Pos p, CampfireData d) {
        persistCampfire(p, d);
        try {
            Chunk chunk = WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return null;
            return chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
        } catch (Exception ignored) { return null; }
    }

    private static void persistCampfire(Pos p, CampfireData d) {
        try {
            Chunk chunk = WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return;
            java.util.List<org.cloudburstmc.nbt.NbtMap> items = new java.util.ArrayList<>();
            for (int i = 0; i < 4; i++) {
                int id = d.slots[i * 2], cnt = d.slots[i * 2 + 1];
                if (id <= 0 || cnt <= 0) continue;
                String iname = BlockManager.itemIdToName(id);
                if (iname == null) continue;
                items.add(org.cloudburstmc.nbt.NbtMap.builder()
                    .putByte("Slot", (byte) i)
                    .putString("id", iname.startsWith("minecraft:") ? iname : "minecraft:" + iname)
                    .putByte("Count", (byte) Math.min(64, cnt))
                    .putInt("CookTime", d.cookTime[i])
                    .build());
            }
            org.cloudburstmc.nbt.NbtMap existing = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            org.cloudburstmc.nbt.NbtMapBuilder b = org.cloudburstmc.nbt.NbtMap.builder();
            if (existing != null) {
                for (String k : existing.keySet()) {
                    if (k.equals("Items")) continue;
                    b.put(k, existing.get(k));
                }
            } else {
                int st = WorldManager.getBlockState(p.dim(), p.x(), p.y(), p.z());
                String n = BlockStateHelper.getName(st);
                b.putString("id", "minecraft:" + (n == null ? "campfire" : n));
                b.putInt("x", p.x()); b.putInt("y", p.y()); b.putInt("z", p.z());
            }
            b.putList("Items", org.cloudburstmc.nbt.NbtType.COMPOUND, items);
            chunk.setBlockEntity(p.x() & 15, p.y(), p.z() & 15, b.build());
        } catch (Exception ignored) {}
    }

    private static void loadCampfireFromBE(Pos p, CampfireData d) {
        try {
            Chunk chunk = WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return;
            org.cloudburstmc.nbt.NbtMap be = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            if (be == null) return;
            java.util.List<org.cloudburstmc.nbt.NbtMap> items =
                be.getList("Items", org.cloudburstmc.nbt.NbtType.COMPOUND);
            if (items == null) return;
            for (org.cloudburstmc.nbt.NbtMap item : items) {
                int slot = item.getByte("Slot", (byte) -1);
                if (slot < 0 || slot >= 4) continue;
                String idStr = item.getString("id", "");
                if (idStr.startsWith("minecraft:")) idStr = idStr.substring(10);
                int iid = BlockManager.getItemIdByName(idStr);
                if (iid <= 0) continue;
                d.slots[slot * 2] = iid;
                d.slots[slot * 2 + 1] = item.getByte("Count", (byte) 1);
                d.cookTime[slot] = item.getInt("CookTime", 0);
            }
        } catch (Exception ignored) {}
    }

    private static void flushAll(Map<Pos, ?> map) {
        for (Map.Entry<Pos, ?> e : map.entrySet()) {
            Pos p = e.getKey();
            Object d = e.getValue();
            int[] slots;
            int ver;
            SlotMeta meta;
            int count;
            if (d instanceof ChestData cd) {
                slots = cd.slots; ver = cd.version; meta = cd.meta; count = 27;
            } else if (d instanceof FurnaceData fd) { slots = fd.slots; ver = fd.version; meta = fd.meta; count = 3; }
            else if (d instanceof HopperData hd) { slots = hd.slots; ver = hd.version; meta = hd.meta; count = 5; }
            else if (d instanceof BrewingData bd) {
                slots = bd.slots; ver = bd.version; count = 5;
                meta = new SlotMeta(5);
                for (int i = 0; i < 5; i++) {
                    meta.slotDamage[i] = bd.meta.slotDamage[i];
                    meta.slotEnchants[i] = bd.meta.slotEnchants[i];
                    meta.slotCustomName[i] = bd.meta.slotCustomName[i];
                    meta.slotPotion[i] = (i < 3 && bd.potionType[i] != null) ? bd.potionType[i] : bd.meta.slotPotion[i];
                }
            }
            else continue;
            Integer last = LAST_FLUSH_VERSION.get(p);
            if (last != null && last == ver) continue;
            LAST_FLUSH_VERSION.put(p, ver);
            writeContainerBlockEntity(p, slots, count, meta);
            // Bug8: 酿造台瓶子槽的药水类型(potionType)也要落盘(原仅 UI 关闭时持久化)
            if (d instanceof BrewingData bd) writeBrewingState(p, bd);
            if (d instanceof FurnaceData fd) writeFurnaceState(p, fd);
        }
    }

    /** 酿造台进度/燃料持久化到区块 BE (周期 flush 用; Items 由 writeContainerBlockEntity 写)。 */
    private static void writeBrewingState(Pos p, BrewingData d) {
        try {
            Chunk chunk = WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return;
            org.cloudburstmc.nbt.NbtMap be = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            if (be == null) return;
            org.cloudburstmc.nbt.NbtMapBuilder b = org.cloudburstmc.nbt.NbtMap.builder();
            for (String k : be.keySet()) b.put(k, be.get(k));
            b.putInt("BrewTime", d.brewTime);
            b.putShort("Fuel", (short) d.fuelTime);
            b.putShort("FuelTotal", (short) d.fuelTotal);
            chunk.setBlockEntity(p.x() & 15, p.y(), p.z() & 15, b.build());
        } catch (Exception ignored) {}
    }

    /** 熔炉烧炼/燃烧进度持久化到区块 BE。 */
    private static void writeFurnaceState(Pos p, FurnaceData d) {
        try {
            Chunk chunk =
                    WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return;
            org.cloudburstmc.nbt.NbtMap be = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            if (be == null) return;
            org.cloudburstmc.nbt.NbtMapBuilder b = org.cloudburstmc.nbt.NbtMap.builder();
            for (String k : be.keySet()) b.put(k, be.get(k));
            b.putInt("CookTime", d.cookTime);
            b.putInt("CookTimeTotal", d.cookTotal);
            b.putShort("BurnTime", (short) d.burnTime);
            b.putShort("BurnTimeTotal", (short) d.burnTotal);
            b.putFloat("xpStore", d.xpStore);
            chunk.setBlockEntity(p.x() & 15, p.y(), p.z() & 15, b.build());
        } catch (Exception ignored) {}
    }

    /** 酿造台: 把 Items/BrewTime/Fuel 写回区块 BE, 并供 0x06 block_entity_data 广播刷新实体瓶/气泡动效。 */
    public static org.cloudburstmc.nbt.NbtMap persistBrewingAndGet(Pos p, BrewingData d) {
        try {
            Chunk chunk = WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return null;
            java.util.List<org.cloudburstmc.nbt.NbtMap> items = new java.util.ArrayList<>();
            for (int i = 0; i < 5; i++) {
                int id = d.slots[i * 2], cnt = d.slots[i * 2 + 1];
                if (id <= 0 || cnt <= 0) continue;
                String iname = BlockManager.itemIdToName(id);
                if (iname == null) continue;
                org.cloudburstmc.nbt.NbtMapBuilder ib = org.cloudburstmc.nbt.NbtMap.builder()
                        .putByte("Slot", (byte) i)
                        .putString("id", iname.startsWith("minecraft:") ? iname : "minecraft:" + iname)
                        .putByte("Count", (byte) Math.min(127, cnt));
                // #8 修复: 瓶子效果走标准 components(potion_contents)。
                // 曾把内部效果串("night_vision|0|3600")直接写进 tag.potion —— 那不是合法
                // 原版药水 id, 客户端悬浮瓶渲染成"不可合成的药水"。
                if (i < 3) {
                    String eff = d.potionType[i];
                    if (eff != null && !eff.isEmpty()) {
                        ib.putCompound("components", PlayerDataManager.buildItemComponents(iname, 0, null, eff, null));
                    }
                }
                items.add(ib.build());
            }
            org.cloudburstmc.nbt.NbtMap existing = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            org.cloudburstmc.nbt.NbtMapBuilder b = org.cloudburstmc.nbt.NbtMap.builder();
            if (existing != null) {
                for (String k : existing.keySet()) {
                    if (k.equals("Items") || k.equals("BrewTime") || k.equals("Fuel") || k.equals("FuelTotal")) continue;
                    b.put(k, existing.get(k));
                }
            } else {
                b.putString("id", "minecraft:brewing_stand");
                b.putInt("x", p.x()); b.putInt("y", p.y()); b.putInt("z", p.z());
            }
            b.putList("Items", org.cloudburstmc.nbt.NbtType.COMPOUND, items);
            b.putInt("BrewTime", d.brewTime);
            b.putShort("Fuel", (short) d.fuelTime);
            b.putShort("FuelTotal", (short) d.fuelTotal);
            org.cloudburstmc.nbt.NbtMap out = b.build();
            chunk.setBlockEntity(p.x() & 15, p.y(), p.z() & 15, out);
            return out;
        } catch (Exception ignored) { return null; }
    }

    private static void writeContainerBlockEntity(Pos p, int[] slots, int count) {
        writeContainerBlockEntity(p, slots, count, null);
    }

    /** Bug4/33: 落盘时把每槽组件(附魔/药水/自定义名/耐久)一并写入 Items NBT,
     *  否则箱子附魔物品重启掉附魔、酿造台药水重启变白瓶。 */
    private static void writeContainerBlockEntity(Pos p, int[] slots, int count, SlotMeta meta) {
        try {
            Chunk chunk =
                    WorldManager.getChunk(p.dim(), p.x() >> 4, p.z() >> 4);
            if (chunk == null) return;
            java.util.List<org.cloudburstmc.nbt.NbtMap> items = new java.util.ArrayList<>();
            for (int i = 0; i < count; i++) {
                int id = slots[2 * i], cnt = slots[2 * i + 1];
                if (id <= 0 || cnt <= 0) continue;
                String iname = BlockManager.itemIdToName(id);
                if (iname == null) continue;
                org.cloudburstmc.nbt.NbtMapBuilder ib = org.cloudburstmc.nbt.NbtMap.builder()
                        .putByte("Slot", (byte) i)
                        .putString("id", iname.startsWith("minecraft:") ? iname : "minecraft:" + iname)
                        .putByte("Count", (byte) Math.min(127, cnt));
                if (meta != null && meta.has(i)) {
                    String pot = meta.slotPotion[i];
                    ib.putCompound("components", PlayerDataManager.buildItemComponents(iname,
                            meta.slotDamage[i], meta.slotEnchants[i], pot, meta.slotCustomName[i]));
                }
                items.add(ib.build());
            }
            org.cloudburstmc.nbt.NbtMap existing = chunk.getBlockEntity(p.x() & 15, p.y(), p.z() & 15);
            org.cloudburstmc.nbt.NbtMapBuilder b = org.cloudburstmc.nbt.NbtMap.builder();
            if (existing != null) {
                for (String k : existing.keySet()) {
                    if (k.equals("Items")) continue;
                    b.put(k, existing.get(k));
                }
            } else {
                int st = WorldManager.getBlockState(p.dim(), p.x(), p.y(), p.z());
                String n = BlockStateHelper.getName(st);
                b.putString("id", "minecraft:" + (n == null ? "chest" : n));
                b.putInt("x", p.x()); b.putInt("y", p.y()); b.putInt("z", p.z());
            }
            b.putList("Items", org.cloudburstmc.nbt.NbtType.COMPOUND, items);
            chunk.setBlockEntity(p.x() & 15, p.y(), p.z() & 15, b.build());
        } catch (Exception ignored) {
        }
    }

    public static ChestData peekChest(Pos p) {
        return CHESTS.get(p);
    }

    /** 末影箱按玩家维度存储 (原版语义: 同一玩家所有末影箱共享一个 27 格背包)。 */
    public static ChestData enderChest(UUID player) {
        return ENDER_CHESTS.computeIfAbsent(player, k -> new ChestData());
    }

    public static ChestData peekEnderChest(UUID player) {
        return ENDER_CHESTS.get(player);
    }

    public static FurnaceData furnace(Pos p, String type) {
        FurnaceData d = FURNACES.computeIfAbsent(p, k -> new FurnaceData());
        if (!d.loaded) { loadFurnaceFromBE(p, d); d.loaded = true; }
        if (type != null) d.type = type;
        return d;
    }

    public static FurnaceData peekFurnace(Pos p) {
        return FURNACES.get(p);
    }

    public static ChestData removeChest(Pos p) {
        return CHESTS.remove(p);
    }

    public static FurnaceData removeFurnace(Pos p) {
        return FURNACES.remove(p);
    }

    public static HopperData hopper(Pos p) {
        return HOPPERS.computeIfAbsent(p, k -> new HopperData());
    }

    public static HopperData peekHopper(Pos p) {
        return HOPPERS.get(p);
    }

    public static HopperData removeHopper(Pos p) {
        return HOPPERS.remove(p);
    }

    public static SmithingData smithing(Pos p) {
        return SMITHINGS.computeIfAbsent(p, k -> new SmithingData());
    }

    public static SmithingData peekSmithing(Pos p) {
        return SMITHINGS.get(p);
    }

    public static EnchantingData enchanting(Pos p) {
        return ENCHANTINGS.computeIfAbsent(p, k -> new EnchantingData());
    }

    public static EnchantingData peekEnchanting(Pos p) {
        return ENCHANTINGS.get(p);
    }

    public static AnvilData anvil(Pos p) {
        return ANVILS.computeIfAbsent(p, k -> new AnvilData());
    }

    public static AnvilData peekAnvil(Pos p) {
        return ANVILS.get(p);
    }

    public static BrewingData brewing(Pos p) {
        BrewingData d = BREWINGS.computeIfAbsent(p, k -> new BrewingData());
        if (!d.loaded) { loadBrewingFromBE(p, d); d.loaded = true; }
        return d;
    }

    public static BrewingData peekBrewing(Pos p) {
        return BREWINGS.get(p);
    }

    public static SmithingData removeSmithing(Pos p) { return SMITHINGS.remove(p); }
    public static EnchantingData removeEnchanting(Pos p) { return ENCHANTINGS.remove(p); }
    public static StonecutterData removeStonecutter(Pos p) { return STONECUTTERS.remove(p); }
    public static GrindstoneData removeGrindstone(Pos p) { return GRINDSTONES.remove(p); }
    public static AnvilData removeAnvil(Pos p) { return ANVILS.remove(p); }
    public static BrewingData removeBrewing(Pos p) { return BREWINGS.remove(p); }
    public static BeaconData beacon(Pos p) { return BEACONS.computeIfAbsent(p, k -> new BeaconData()); }
    public static BeaconData peekBeacon(Pos p) { return BEACONS.get(p); }
    public static BeaconData removeBeacon(Pos p) { return BEACONS.remove(p); }
    public static java.util.Set<Map.Entry<Pos, BeaconData>> beaconEntries() { return BEACONS.entrySet(); }
    public static JukeboxData jukebox(Pos p) { return JUKEBOXES.computeIfAbsent(p, k -> new JukeboxData()); }
    public static JukeboxData peekJukebox(Pos p) { return JUKEBOXES.get(p); }
    public static JukeboxData removeJukebox(Pos p) { return JUKEBOXES.remove(p); }
    public static CommandBlockData commandBlock(Pos p) { return COMMANDBLOCKS.computeIfAbsent(p, k -> new CommandBlockData()); }
    public static CommandBlockData peekCommandBlock(Pos p) { return COMMANDBLOCKS.get(p); }
    public static CommandBlockData removeCommandBlock(Pos p) { return COMMANDBLOCKS.remove(p); }

    public static Map<Pos, FurnaceData> furnaces() {
        return FURNACES;
    }

    public static void tick() {
        for (Map.Entry<Pos, FurnaceData> e : FURNACES.entrySet()) {
            tickFurnace(e.getKey(), e.getValue());
        }
        for (Map.Entry<Pos, BrewingData> e : BREWINGS.entrySet()) {
            tickBrewing(e.getKey(), e.getValue());
        }
        for (Map.Entry<Pos, HopperData> e : HOPPERS.entrySet()) {
            tickHopper(e.getKey(), e.getValue());
        }
        for (Map.Entry<Pos, CampfireData> e : CAMPFIRES.entrySet()) {
            tickCampfire(e.getKey(), e.getValue());
        }
        // P9-B5: 周期性把脏容器落盘到区块 block entity（随后由 WorldManager 周期保存写入磁盘）
        if (Main.worldAge % 100 == 0) flushDirtyContainers();
    }

    // ── 漏斗自动传输 ──────────────────────────────────────
    private static final class ContainerRef {
        final int[] slots;
        final SlotMeta meta; // 可为 null(不跟踪组件)
        final int slotCount;
        final int type; // 0=chest, 1=hopper, 2=furnace, 3=brewing
        final Pos pos;

        ContainerRef(int[] slots, int slotCount, int type, Pos pos) {
            this(slots, null, slotCount, type, pos);
        }

        ContainerRef(int[] slots, SlotMeta meta, int slotCount, int type, Pos pos) {
            this.slots = slots;
            this.meta = meta;
            this.slotCount = slotCount;
            this.type = type;
            this.pos = pos;
        }

        int idAt(int i) { return slots[2 * i]; }
        int countAt(int i) { return slots[2 * i + 1]; }
        void setId(int i, int v) { slots[2 * i] = v; }
        void setCount(int i, int v) { slots[2 * i + 1] = v; }
        void dec(int i) {
            int c = countAt(i) - 1;
            if (c <= 0) { setId(i, 0); setCount(i, 0); }
            else setCount(i, c);
        }

        boolean extractable(int i) {
            if (type == 2) return i == 2;           // 熔炉只取输出槽
            if (type == 3) return i >= 0 && i <= 2; // 酿造台只取瓶子槽
            return true;
        }

        boolean insertable(int i, int itemId) {
            if (type == 2) {
                if (i == 0) return SmeltingSystem.getResult(BlockManager.itemIdToName(itemId), "furnace") != null;
                if (i == 1) return SmeltingSystem.getFuelBurnTime(BlockManager.itemIdToName(itemId)) > 0;
                return false;
            }
            if (type == 3) {
                if (i >= 0 && i <= 2) return BrewingSystem.isBottle(itemId);
                if (i == 3) return true;
                return false;
            }
            return true;
        }
    }

    private static ContainerRef refAt(Pos p) {
        int st = WorldManager.getBlockState(p.dim(), p.x(), p.y(), p.z());
        String name = BlockStateHelper.getName(st);
        return switch (name) {
            case "chest", "trapped_chest", "barrel" -> new ContainerRef(chest(p).slots, chest(p).meta, 27, 0, p);
            default -> {
                if (name != null && name.endsWith("_shulker_box"))
                    yield new ContainerRef(chest(p).slots, chest(p).meta, 27, 0, p);
                yield switch (name) {
                    case "hopper" -> new ContainerRef(hopper(p).slots, hopper(p).meta, 5, 1, p);
                    case "furnace", "blast_furnace", "smoker" -> new ContainerRef(furnace(p, name).slots, furnace(p, name).meta, 3, 2, p);
                    case "brewing_stand" -> new ContainerRef(brewing(p).slots, brewing(p).meta, 5, 3, p);
                    case "dispenser", "dropper" -> new ContainerRef(dispenser(p).slots, dispenser(p).meta, 9, 4, p);
                    default -> null;
                };
            }
        };
    }

    private static void bumpVersion(ContainerRef ref) {
        switch (ref.type) {
            case 0 -> { ChestData d = CHESTS.get(ref.pos); if (d != null) d.version++; }
            case 1 -> { HopperData d = HOPPERS.get(ref.pos); if (d != null) d.version++; }
            case 2 -> { FurnaceData d = FURNACES.get(ref.pos); if (d != null) d.version++; }
            case 3 -> { BrewingData d = BREWINGS.get(ref.pos); if (d != null) d.version++; }
            case 4 -> { DispenserData d = DISPENSERS.get(ref.pos); if (d != null) d.version++; }
        }
    }

    private static boolean moveOne(ContainerRef src, ContainerRef dst) {
        for (int i = 0; i < src.slotCount; i++) {
            if (!src.extractable(i)) continue;
            int id = src.idAt(i), c = src.countAt(i);
            if (id <= 0 || c <= 0) continue;
            boolean hasMeta = src.meta != null && src.meta.has(i);
            // Bug4/33: 带组件的物品(附魔/药水/改名)不与普通堆合并, 只整组移入空槽并携带组件
            for (int j = 0; j < dst.slotCount; j++) {
                if (!dst.insertable(j, id)) continue;
                int dj = dst.idAt(j), dc = dst.countAt(j);
                if (!hasMeta && dj == id && dc > 0 && dc < 64) {
                    dst.setCount(j, dc + 1); src.dec(i); return true;
                }
                if (dj == 0 && dc == 0) {
                    dst.setId(j, id); dst.setCount(j, 1); 
                    if (src.meta != null && dst.meta != null) {
                        dst.meta.slotDamage[j] = src.meta.slotDamage[i];
                        dst.meta.slotEnchants[j] = new java.util.HashMap<>(src.meta.slotEnchants[i]);
                        dst.meta.slotPotion[j] = src.meta.slotPotion[i];
                        dst.meta.slotCustomName[j] = src.meta.slotCustomName[i];
                    }
                    if (src.meta != null) src.meta.clear(i);
                    src.dec(i); return true;
                }
            }
        }
        return false;
    }

    private static void tickHopper(Pos pos, HopperData h) {
        if (h.transferCd > 0) { h.transferCd--; return; }
        // P8-#2: 红石供能时漏斗禁用 (enabled=false)，不传输
        int st = WorldManager.getBlockState(pos.dim(), pos.x(), pos.y(), pos.z());
        if ("false".equals(BlockStateHelper.getProp(st, "enabled"))) { h.transferCd = 8; return; }

        // 0. 原版: 漏斗吸起上方 1 格范围内的掉落物 (item entity)
        for (Entity e :
                EntityManager.getAllEntities()) {
            if (!(e instanceof ItemEntity item)) continue;
            if (item.dim != pos.dim() || item.pickupDelay > 0) continue;
            double dx = item.x - (pos.x() + 0.5);
            double dy = item.y - (pos.y() + 1.0);
            double dz = item.z - (pos.z() + 0.5);
            if (Math.abs(dx) < 0.6 && dy >= -0.1 && dy <= 1.4 && Math.abs(dz) < 0.6) {
                // 掉落物进入漏斗: 找空槽或同种堆叠槽
                int slot = -1;
                for (int i = 0; i < 5; i++) {
                    if (h.slots[2*i] == item.itemId && h.slots[2*i+1] < 64) { slot = i; break; }
                    if (h.slots[2*i] == 0) { slot = i; break; }
                }
                if (slot >= 0) {
                    int add = Math.min(item.count, 64 - h.slots[2*slot+1]);
                    if (h.slots[2*slot] == 0) {
                        h.slots[2*slot] = item.itemId;
                        // Bug4/33: 掉落物携带的组件随物品进入漏斗槽
                        h.meta.slotDamage[slot] = item.itemDamage;
                        h.meta.slotEnchants[slot] = item.itemEnchants == null
                            ? new java.util.HashMap<>() : new java.util.HashMap<>(item.itemEnchants);
                        h.meta.slotPotion[slot] = item.itemPotion;
                        h.meta.slotCustomName[slot] = item.itemCustomName;
                    }
                    if (add >= item.count) {
                        EntityManager.removeEntity(item.id);
                    } else {
                        item.count -= add;
                    }
                    h.slots[2*slot+1] += add;
                    h.version++;
                    h.transferCd = 8;
                    NetworkHandler.broadcastContainerUpdate(pos); // #28 实时动效
                    notifyRedstone(pos); // #14 满度信号重算
                    return;
                }
            }
        }

        // 1. 从正上方容器抽取
        ContainerRef above = refAt(new Pos(pos.dim(), pos.x(), pos.y() + 1, pos.z()));
        if (above != null && moveOne(above, new ContainerRef(h.slots, 5, 1, pos))) {
            bumpVersion(above);
            h.version++;
            h.transferCd = 8;
            NetworkHandler.broadcastContainerUpdate(pos); // #28 实时动效
            if (above.pos != null) { NetworkHandler.broadcastContainerUpdate(above.pos); notifyRedstone(above.pos); } // 源容器同步
            notifyRedstone(pos); // #14 满度信号重算
            return;
        }

        // 2. 向 facing 方向容器推送
        st = WorldManager.getBlockState(pos.dim(), pos.x(), pos.y(), pos.z());
        String facing = BlockStateHelper.getProp(st, "facing");
        int[] d = facingDelta(facing);
        if (d != null) {
            ContainerRef dst = refAt(new Pos(pos.dim(), pos.x() + d[0], pos.y() + d[1], pos.z() + d[2]));
            if (dst != null && moveOne(new ContainerRef(h.slots, 5, 1, pos), dst)) {
                bumpVersion(dst);
                h.version++;
                h.transferCd = 8;
                NetworkHandler.broadcastContainerUpdate(pos); // #28 实时动效
                if (dst.pos != null) { NetworkHandler.broadcastContainerUpdate(dst.pos); notifyRedstone(dst.pos); } // 目标容器同步
                notifyRedstone(pos); // #14 满度信号重算
                return;
            }
        }
        h.transferCd = 8;
    }

    /** #14: 容器内容变化后通知红石引擎重算邻接比较器的满度信号。 */
    private static void notifyRedstone(Pos p) {
        RedstoneEngine.onBlockChanged(p.dim(), p.x(), p.y(), p.z());
    }

    private static int[] facingDelta(String facing) {
        if (facing == null) return null; // 漏斗方块已卸载/被移除时 getProp 返回 null(曾 switch(null) 直接 NPE)
        return switch (facing) {
            case "north" -> new int[] { 0, 0, -1 };
            case "south" -> new int[] { 0, 0, 1 };
            case "east" -> new int[] { 1, 0, 0 };
            case "west" -> new int[] { -1, 0, 0 };
            case "down" -> new int[] { 0, -1, 0 };
            default -> null;
        };
    }

    private static void tickFurnace(Pos pos, FurnaceData f) {
        boolean wasLit = f.lit;
        boolean changed = false;

        int inputId = f.slots[0];
        int inputCount = f.slots[1];
        int fuelId = f.slots[2];
        int fuelCount = f.slots[3];
        int outputId = f.slots[4];
        int outputCount = f.slots[5];

        SmeltingSystem.SmeltResult result = null;
        int resultItemId = 0;
        if (inputCount > 0 && inputId > 0) {
            String inputName = BlockManager.itemIdToName(inputId);
            result = SmeltingSystem.getResult(inputName, f.type);
            if (result != null) {
                resultItemId = BlockManager.getItemIdByName(result.resultItem);
                if (resultItemId <= 0) result = null;
            }
        }

        boolean canAccept = result != null
                && (outputCount <= 0 || (outputId == resultItemId && outputCount < 64));

        if (f.burnTime > 0) {
            f.burnTime--;
            if (f.burnTime <= 0) {
                f.lit = false;
            }
        }

        if (f.burnTime <= 0 && canAccept && fuelCount > 0 && fuelId > 0) {
            String fuelName = BlockManager.itemIdToName(fuelId);
            int burn = SmeltingSystem.getFuelBurnTime(fuelName);
            if (burn > 0) {
                f.burnTime = burn;
                f.burnTotal = burn;
                f.lit = true;
                f.slots[3]--;
                if (f.slots[3] <= 0) {
                    if ("lava_bucket".equals(fuelName)) {
                        f.slots[2] = BlockManager.getItemIdByName("bucket");
                        f.slots[3] = 1;
                    } else {
                        f.slots[2] = 0;
                        f.slots[3] = 0;
                    }
                }
                changed = true;
            }
        }

        if (f.lit && canAccept) {
            f.cookTotal = SmeltingSystem.getCookTime(f.type);
            f.cookTime++;
            if (f.cookTime >= f.cookTotal) {
                f.cookTime = 0;
                if (outputCount <= 0) {
                    f.slots[4] = resultItemId;
                    f.slots[5] = 1;
                } else {
                    f.slots[5]++;
                }
                f.slots[1]--;
                if (f.slots[1] <= 0) {
                    f.slots[0] = 0;
                    f.slots[1] = 0;
                }
                f.xpStore += result.xp;
                changed = true;
            }
        } else if (f.cookTime > 0) {
            f.cookTime = Math.max(0, f.cookTime - 2);
        }

        if (changed) f.version++;

        if (wasLit != f.lit) {
            updateLitState(pos, f.lit);
        }
    }

    private static void updateLitState(Pos pos, boolean lit) {
        try {
            int state = WorldManager.getBlockState(pos.dim(), pos.x(), pos.y(), pos.z());
            String name = BlockStateHelper.getName(state);
            if (!"furnace".equals(name) && !"blast_furnace".equals(name) && !"smoker".equals(name)) return;
            String cur = BlockStateHelper.getProp(state, "lit");
            String want = lit ? "true" : "false";
            if (want.equals(cur)) return;
            int newState = BlockStateHelper.withProp(state, "lit", want);
            if (newState == state) return;
            WorldManager.setBlock(pos.dim(), pos.x(), pos.y(), pos.z(), newState);
            NetworkHandler.broadcastBlockChange(pos.dim(), pos.x(), pos.y(), pos.z(), newState);
        } catch (Exception ignored) {
        }
    }

    private static void tickBrewing(Pos pos, BrewingData b) {
        // #12 修复: 原版 BrewingStandMenu 槽位 = 0-2 瓶子 / 3 燃料(烈焰粉) / 4 材料。
        // 曾把槽3(燃料)当材料、槽4(材料)当燃料 -> 烈焰粉放燃料位被当材料、材料放材料位
        // 被当燃料 -> 永远无法酿造。现按原版: 槽3=slots[6/7]材料, 槽4=slots[8/9]燃料。
        // Bug8 二修: 原版 1.21.11 BrewingStandMenu 布局是 0-2 瓶子 / 3=材料(IngredientSlot) /
        // 4=燃料(FuelSlot) —— 曾按 3=燃料/4=材料 理解, 烈焰粉进了材料格、材料进了燃料格,
        // 酿造永远无法启动(实测"烈焰粉不消耗/无动效/东西不消失")。
        // ── 材料 (槽 3 → slots[6/7]) ──
        int ingId = b.slots[6], ingCount = b.slots[7];
        String ingName = BlockManager.itemIdToName(ingId);

        // ── 燃料 (槽 4 → slots[8/9]) ──
        int fuelId = b.slots[8], fuelCount = b.slots[9];
        String fuelName = BlockManager.itemIdToName(fuelId);
        if (b.fuelTime <= 0) {
            if (fuelCount > 0 && BrewingSystem.isFuel(fuelName)) {
                b.slots[9] = fuelCount - 1;
                if (b.slots[9] <= 0) { b.slots[8] = 0; b.slots[9] = 0; }
                b.fuelTime = 8000; b.fuelTotal = 8000;
                b.version++;
            }
        } else {
            b.fuelTime--;
        }

        if (b.brewTime <= 0) {
            if (ingCount > 0 && b.fuelTime > 0) {
                boolean canBrew = false;
                for (int s = 0; s <= 2; s++) {
                    int bid = b.slots[s * 2], bct = b.slots[s * 2 + 1];
                    if (bct > 0 && BrewingSystem.isBottle(bid)) {
                        String bn = BlockManager.itemIdToName(bid);
                        if (BrewingSystem.canBrew(bn, ingName, b.potionType[s])) { canBrew = true; break; }
                    }
                }
                // 原版 brewTime 从 400 倒数到 0, 客户端箭头按该值渲染(曾正数递增 -> 箭头反向)
                if (canBrew) { b.brewTime = 400; b.brewTotal = 400; b.version++; }
            }
        } else {
            b.brewTime--;
            if (b.brewTime <= 0) {
                b.brewTime = 0;
                // 原版: 每次酿造消耗 1 个材料(在 3 个瓶子都处理完时)。
                if (ingCount > 0) {
                    b.slots[7] = ingCount - 1;
                    if (b.slots[7] <= 0) { b.slots[6] = 0; b.slots[7] = 0; }
                }
                for (int s = 0; s <= 2; s++) {
                    int bid = b.slots[s * 2], bct = b.slots[s * 2 + 1];
                    if (bct > 0 && BrewingSystem.isBottle(bid)) {
                        String bn = BlockManager.itemIdToName(bid);
                        if (BrewingSystem.canBrew(bn, ingName, b.potionType[s])) {
                            String res = BrewingSystem.getBrewResult(bn, ingName);
                            // #12 修复: items.json 无 water_bottle/awkward_potion, 瓶子统一为 potion/
                            // splash_potion/lingering_potion。结果名不是 potion 系列时映射回 potion,
                            // 药水类型由 potionType 字符串承载(曾 getItemIdByName(awkward_potion)=0 -> 不转化)。
                            if (res != null && !res.equals("glass_bottle")) {
                                int rid = BlockManager.getItemIdByName(res);
                                if (rid <= 0) {
                                    String baseItem = res.contains("splash") ? "splash_potion"
                                        : res.contains("lingering") ? "lingering_potion" : "potion";
                                    rid = BlockManager.getItemIdByName(baseItem);
                                }
                                if (rid > 0) b.slots[s * 2] = rid;
                            }
                            String eff = BrewingSystem.resolveBrewEffect(bn, ingName, b.potionType[s]);
                            // 无效果的基础药水(水/粗制)用类型标记, 否则客户端显示"不可合成的药水"。
                            if (res != null) {
                                if ("awkward_potion".equals(res)) eff = "awkward";
                                else if ("water_bottle".equals(res)) eff = "water";
                                else if (eff == null && "mundane".equals(res)) eff = "mundane";
                            }
                            b.potionType[s] = eff;
                        }
                    }
                }
                b.version++;
            }
        }
    }
}
