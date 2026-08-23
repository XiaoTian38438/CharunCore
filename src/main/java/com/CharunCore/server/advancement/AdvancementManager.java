package com.CharunCore.server.advancement;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.network.protocol.PacketBuffer;
import com.CharunCore.server.utils.BlockManager;
import org.cloudburstmc.nbt.NbtMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * 成就系统中央协调器 (P12 best-effort 从零实现)。
 *
 * <p>职责：
 * <ol>
 *   <li><b>数据层</b>：内置少量关键 advancement 定义 (story / nether / adventure / husbandry 各一叶子成就 + 对应根节点)，
 *       形成客户端可显示的树。</li>
 *   <li><b>进度层</b>：维护每个玩家 {@link PlayerAdvancementState}，按 requirements 判定完成。</li>
 *   <li><b>触发层</b>：游戏事件 (破坏/放置/击杀/进入维度/合成) 由 NetworkHandler 调用本类的 onXxx 方法，
 *       命中后授予 criterion，首次整体完成时通过协议包 0x80 (advancements) 下发 toast。</li>
 *   <li><b>协议层</b>：按原版 1.21.11 {@code ClientboundUpdateAdvancementsPacket} 线格式编码 0x80 包
 *       (结构源自 json/1.21.11/protocol.json 的 packet_advancements，并经
 *       mapping/cfr-source 的 ClientboundUpdateAdvancementsPacket / Advancement / DisplayInfo / AdvancementProgress 核对)。</li>
 *   <li><b>持久化</b>：已授予成就 id 集合写入 PlayerData.unlockedAdvancements，随玩家数据落盘 (GSON)。</li>
 * </ol>
 *
 * <p>线程：本系统仅在 Netty IO 线程 (每连接单线程) 的事件回调中访问，STATES 用 ConcurrentHashMap 仅作防御。</p>
 */
public final class AdvancementManager {

    /** 成就包协议 id (ClientboundUpdateAdvancementsPacket = 0x80, protocol.json:10110)。 */
    private static final int PACKET_ADVANCEMENTS = 0x80;

    // ── 注册表 (内置关键成就) ───────────────────────────────────────────────
    private static final List<AdvancementDef> REGISTRY = buildRegistry();
    private static final Map<String, AdvancementDef> BY_ID = new java.util.HashMap<>();
    static {
        for (AdvancementDef d : REGISTRY) BY_ID.put(d.id, d);
    }

    // ── 每玩家进度 ─────────────────────────────────────────────────────────
    private static final Map<UUID, PlayerAdvancementState> STATES = new ConcurrentHashMap<>();

    private AdvancementManager() {}

    // =========================================================================
    // 生命周期
    // =========================================================================

    /**
     * 玩家登录完成、连接进入 PLAY 后调用：加载已持久化的已完成成就，并下发全量同步
     * (reset=true，含全部 advancement 映射 + 当前进度)。客户端据此构建成就树并显示已完成项。
     */
    public static void onLogin(NetworkHandler nh) {
        if (nh == null || nh.uuid == null || nh.ctx == null) return;
        PlayerAdvancementState state = new PlayerAdvancementState();
        if (nh.data != null && nh.data.unlockedAdvancements != null) {
            for (String id : nh.data.unlockedAdvancements) state.markDone(id);
        }
        STATES.put(nh.uuid, state);
        sendFullSync(nh);
    }

    /** 玩家断开时清理内存状态 (落盘由 NetworkHandler.savePlayerData 负责)。 */
    public static void onLogout(UUID uuid) {
        if (uuid != null) STATES.remove(uuid);
    }

    // =========================================================================
    // 事件入口 (由 NetworkHandler 在对应游戏事件处调用)
    // =========================================================================

    /** 破坏方块。blockName 为破坏前方块名 (来自 BlockStateHelper.getName)。 */
    public static void onBlockBreak(NetworkHandler nh, String blockName) {
        if (blockName == null) return;
        fire(nh, AdvancementTrigger.BLOCK_BREAK, def -> blockMatches(def, blockName));
    }

    /** 放置方块。blockName 为放置后方块名 (来自 BlockStateHelper.getName)。 */
    public static void onBlockPlace(NetworkHandler nh, String blockName) {
        if (blockName == null) return;
        fire(nh, AdvancementTrigger.BLOCK_PLACE, def -> blockMatches(def, blockName));
    }

    /** 击杀生物。mobType 为生物名 (如 "zombie"), 匹配 def.matchMobs (null=任意)。 */
    public static void onMobKill(NetworkHandler nh, String mobType) {
        fire(nh, AdvancementTrigger.ENTITY_KILL, def -> def.matchMobs == null
            || (mobType != null && def.matchMobs.contains(mobType)));
    }

    /** 合成物品。itemName 为合成产物名 (来自 itemIdToName)。 */
    public static void onCraftItem(NetworkHandler nh, String itemName) {
        if (itemName == null) return;
        fire(nh, AdvancementTrigger.CRAFT_ITEM, def -> itemMatches(def, itemName));
    }

    /** 附魔物品 (附魔台/铁砧)。itemName 为被附魔物品名。 */
    public static void onEnchantItem(NetworkHandler nh, String itemName) {
        if (itemName == null) return;
        fire(nh, AdvancementTrigger.ENCHANT_ITEM, def -> true);
    }

    /** 食用食物 (原版 minecraft:consume_item)。 */
    public static void onConsumeItem(NetworkHandler nh, String itemName) {
        if (itemName == null) return;
        fire(nh, AdvancementTrigger.CONSUME_ITEM, def -> itemMatches(def, itemName));
    }

    private static boolean itemMatches(AdvancementDef def, String itemName) {
        if (def.matchBlocks != null && def.matchBlocks.contains(itemName)) return true;
        if (def.matchSuffixes != null) {
            for (String sfx : def.matchSuffixes) if (itemName.endsWith(sfx)) return true;
        }
        return false;
    }

    /** 进入维度。dimKey 为目标维度 key (如 "minecraft:the_nether")。 */
    public static void onEnterDimension(NetworkHandler nh, String dimKey) {
        if (dimKey == null) return;
        fire(nh, AdvancementTrigger.ENTER_DIMENSION, def -> dimKey.equals(def.matchDimension));
    }

    // =========================================================================
    // 内部：匹配与授予
    // =========================================================================

    private static boolean blockMatches(AdvancementDef def, String blockName) {
        if (def.matchBlocks != null && def.matchBlocks.contains(blockName)) return true;
        if (def.matchSuffixes != null) {
            for (String sfx : def.matchSuffixes) if (blockName.endsWith(sfx)) return true;
        }
        return false;
    }

    private static void fire(NetworkHandler nh, AdvancementTrigger trigger, Predicate<AdvancementDef> match) {
        if (nh == null || nh.ctx == null) return;
        PlayerAdvancementState state = STATES.get(nh.uuid);
        if (state == null) return;
        for (AdvancementDef def : REGISTRY) {
            if (def.isRoot) continue;
            if (def.trigger != trigger) continue;
            if (!match.test(def)) continue;
            if (state.isDone(def.id)) continue;
            if (state.grant(def.id, def.criterionName)) {
                persist(nh, def.id);
                sendProgress(nh, def);
            }
        }
    }

    private static void persist(NetworkHandler nh, String advId) {
        if (nh.data != null) {
            if (nh.data.unlockedAdvancements == null) nh.data.unlockedAdvancements = new HashSet<>();
            nh.data.unlockedAdvancements.add(advId);
        }
    }

    // =========================================================================
    // 协议编码 (0x80 ClientboundUpdateAdvancementsPacket)
    // =========================================================================

    /** 登录全量同步：reset=true，发送全部映射与进度。 */
    private static void sendFullSync(NetworkHandler nh) {
        PlayerAdvancementState state = STATES.get(nh.uuid);
        nh.sendPacket(nh.ctx, PACKET_ADVANCEMENTS, pb -> {
            pb.writeBoolean(true); // reset
            // advancementMapping (全部)
            pb.writeVarInt(REGISTRY.size());
            for (AdvancementDef d : REGISTRY) encodeAdvancement(pb, d);
            // removed identifiers
            pb.writeVarInt(0);
            // progressMapping (全部)
            pb.writeVarInt(REGISTRY.size());
            for (AdvancementDef d : REGISTRY) encodeProgress(pb, d, state);
            // showAdvancements
            pb.writeBoolean(true);
        });
    }

    /** 增量同步：reset=false，仅发送单条成就的进度 (客户端已持有映射，据此弹 toast)。 */
    private static void sendProgress(NetworkHandler nh, AdvancementDef def) {
        PlayerAdvancementState state = STATES.get(nh.uuid);
        nh.sendPacket(nh.ctx, PACKET_ADVANCEMENTS, pb -> {
            pb.writeBoolean(false); // reset
            pb.writeVarInt(0);      // advancementMapping (空)
            pb.writeVarInt(0);      // removed identifiers (空)
            pb.writeVarInt(1);      // progressMapping: 仅本成就
            encodeProgress(pb, def, state);
            pb.writeBoolean(true);  // showAdvancements
        });
    }

    /** 编码单个 advancement 的映射条目 (key + value 容器，对应 protocol.json advancementMapping)。 */
    private static void encodeAdvancement(PacketBuffer pb, AdvancementDef def) {
        pb.writeString(def.id);
        // parentId: Optional<Identifier>
        if (def.parentId == null) pb.writeBoolean(false);
        else { pb.writeBoolean(true); pb.writeString(def.parentId); }
        // displayData: Optional<DisplayInfo>
        pb.writeBoolean(true);
        pb.writeAnonymousNbt(component(def.title));
        pb.writeAnonymousNbt(component(def.description));
        pb.writeSlot(def.iconItemId, 1);
        pb.writeVarInt(def.frame.id);
        int flags = 0;
        if (def.background != null) flags |= 1;
        if (def.showToast)        flags |= 2;
        if (def.hidden)            flags |= 4;
        pb.writeInt(flags);
        if (def.background != null) pb.writeString(def.background);
        pb.writeFloat(def.x);
        pb.writeFloat(def.y);
        // requirements
        pb.writeVarInt(def.requirements.size());
        for (List<String> group : def.requirements) {
            pb.writeVarInt(group.size());
            for (String c : group) pb.writeString(c);
        }
        // sendsTelemetryEvent
        pb.writeBoolean(false);
    }

    /** 编码单个 advancement 的进度条目 (对应 protocol.json progressMapping 的 value)。 */
    private static void encodeProgress(PacketBuffer pb, AdvancementDef def, PlayerAdvancementState state) {
        pb.writeString(def.id);
        pb.writeVarInt(def.criteria.size());
        for (String c : def.criteria) {
            pb.writeString(c);
            boolean done = state != null && state.isCriterionDone(def.id, c);
            if (done) {
                pb.writeBoolean(true);
                pb.writeLong(System.currentTimeMillis());
            } else {
                pb.writeBoolean(false);
            }
        }
    }

    private static NbtMap component(String text) {
        return NbtMap.builder().putString("text", text).build();
    }

    // =========================================================================
    // 注册表构建 (内置关键成就)
    // =========================================================================

    private static List<AdvancementDef> buildRegistry() {
        List<AdvancementDef> list = new ArrayList<>();

        // ===== story 分支 =====
        list.add(root("minecraft:story/root", "冒险起点", "采集木头，开始你的旅程", "crafting_table", AdvancementFrame.TASK, 0.0f, 0.0f));
        list.add(leaf("minecraft:story/mine_stone", "minecraft:story/root", "石器时代", "挖掘一块石头", "stone", AdvancementFrame.TASK,
                0.0f, 1.0f, AdvancementTrigger.BLOCK_BREAK, Set.of("stone")));
        list.add(leaf("minecraft:story/upgrade_tools", "minecraft:story/mine_stone", "更好的升级", "合成一把石镐", "stone_pickaxe", AdvancementFrame.TASK,
                0.0f, 2.0f, AdvancementTrigger.CRAFT_ITEM, Set.of("stone_pickaxe")));
        list.add(leaf("minecraft:story/smelt_iron", "minecraft:story/upgrade_tools", "进取", "熔炼一块铁锭", "iron_ingot", AdvancementFrame.TASK,
                1.0f, 2.0f, AdvancementTrigger.CRAFT_ITEM, Set.of("iron_ingot")));
        list.add(leaf("minecraft:story/iron_tools", "minecraft:story/smelt_iron", "我不想再当农夫", "合成一把铁镐", "iron_pickaxe", AdvancementFrame.TASK,
                1.0f, 3.0f, AdvancementTrigger.CRAFT_ITEM, Set.of("iron_pickaxe")));
        list.add(leaf("minecraft:story/obtain_armor", "minecraft:story/iron_tools", "整装待发", "合成一件铁胸甲", "iron_chestplate", AdvancementFrame.TASK,
                2.0f, 3.0f, AdvancementTrigger.CRAFT_ITEM, Set.of("iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots")));
        list.add(leaf("minecraft:story/form_obsidian", "minecraft:story/iron_tools", "冰桶挑战", "挖掘一块黑曜石", "obsidian", AdvancementFrame.TASK,
                2.0f, 2.0f, AdvancementTrigger.BLOCK_BREAK, Set.of("obsidian")));
        list.add(leaf("minecraft:story/enter_the_nether", "minecraft:story/form_obsidian", "下界", "建造并激活下界传送门", "obsidian", AdvancementFrame.TASK,
                3.0f, 2.0f, AdvancementTrigger.ENTER_DIMENSION, null, "minecraft:the_nether"));
        list.add(leaf("minecraft:story/enter_the_end", "minecraft:story/enter_the_nether", "末地", "进入末地", "end_stone", AdvancementFrame.TASK,
                4.0f, 2.0f, AdvancementTrigger.ENTER_DIMENSION, null, "minecraft:the_end"));

        // ===== nether 分支 =====
        list.add(root("minecraft:nether/root", "下界", "进入下界", "netherrack", AdvancementFrame.TASK, 5.0f, 0.0f));
        list.add(leaf("minecraft:nether/obtain_blaze_rod", "minecraft:nether/root", "勇闯地狱", "击杀一只烈焰人", "blaze_rod", AdvancementFrame.TASK,
                5.0f, 1.0f, AdvancementTrigger.ENTITY_KILL, null, null, Set.of("blaze")));
        list.add(leaf("minecraft:nether/get_wither_skull", "minecraft:nether/obtain_blaze_rod", "恐怖大头", "获得凋灵骷髅头颅", "wither_skeleton_skull", AdvancementFrame.TASK,
                6.0f, 1.0f, AdvancementTrigger.ENTITY_KILL, null, null, Set.of("wither_skeleton")));
        list.add(leaf("minecraft:nether/fast_travel", "minecraft:nether/root", "曲速旅行", "利用下界在主世界快速移动", "map", AdvancementFrame.TASK,
                5.0f, -1.0f, AdvancementTrigger.ENTER_DIMENSION, null, "minecraft:the_nether"));
        list.add(leaf("minecraft:nether/brew_potion", "minecraft:nether/root", "本地酿造厂", "酿造一瓶药水", "potion", AdvancementFrame.TASK,
                4.0f, 1.0f, AdvancementTrigger.CRAFT_ITEM, Set.of("potion")));
        list.add(leaf("minecraft:nether/all_potions", "minecraft:nether/brew_potion", "狂乱的鸡尾酒", "同时拥有所有药水效果", "milk_bucket", AdvancementFrame.CHALLENGE,
                4.0f, 2.0f, AdvancementTrigger.CONSUME_ITEM, Set.of("potion")));

        // ===== end 分支 =====
        list.add(root("minecraft:end/root", "末地", "或者这是开始？", "end_stone", AdvancementFrame.TASK, 7.0f, 0.0f));
        list.add(leaf("minecraft:end/kill_dragon", "minecraft:end/root", "解放末地", "击杀末影龙", "dragon_head", AdvancementFrame.TASK,
                7.0f, 1.0f, AdvancementTrigger.ENTITY_KILL, null, null, Set.of("ender_dragon")));
        list.add(leaf("minecraft:end/elytra", "minecraft:end/kill_dragon", "天空是我们的极限", "找到鞘翅", "elytra", AdvancementFrame.GOAL,
                7.0f, 2.0f, AdvancementTrigger.CRAFT_ITEM, Set.of("elytra")));
        list.add(leaf("minecraft:end/dragon_breath", "minecraft:end/kill_dragon", "你需要来点薄荷", "收集龙息", "dragon_breath", AdvancementFrame.GOAL,
                8.0f, 2.0f, AdvancementTrigger.CRAFT_ITEM, Set.of("dragon_breath")));

        // ===== adventure 分支 =====
        list.add(root("minecraft:adventure/root", "冒险", "冒险吧！", "compass", AdvancementFrame.TASK, 9.0f, 0.0f));
        list.add(leaf("minecraft:adventure/kill_a_mob", "minecraft:adventure/root", "怪物猎人", "击杀一只怪物", "iron_sword", AdvancementFrame.TASK,
                9.0f, 1.0f, AdvancementTrigger.ENTITY_KILL, null, null, null));
        list.add(leaf("minecraft:adventure/ol_betsy", "minecraft:adventure/kill_a_mob", "老伙计贝琪", "合成一把弩", "crossbow", AdvancementFrame.TASK,
                9.0f, 2.0f, AdvancementTrigger.CRAFT_ITEM, Set.of("crossbow")));
        list.add(leaf("minecraft:adventure/sleep_in_bed", "minecraft:adventure/root", "甜蜜的梦", "在床上睡觉", "red_bed", AdvancementFrame.TASK,
                10.0f, 1.0f, AdvancementTrigger.BLOCK_PLACE, Set.of("red_bed", "white_bed", "blue_bed")));
        list.add(leaf("minecraft:adventure/totem_of_undying", "minecraft:adventure/kill_a_mob", "超越死亡", "使用不死图腾", "totem_of_undying", AdvancementFrame.GOAL,
                8.0f, 2.0f, AdvancementTrigger.CRAFT_ITEM, Set.of("totem_of_undying")));

        // ===== husbandry 分支 =====
        list.add(root("minecraft:husbandry/root", "农牧", "世界充满生命", "wheat", AdvancementFrame.TASK, 11.0f, 0.0f));
        list.add(leaf("minecraft:husbandry/plant_seed", "minecraft:husbandry/root", "种下种子", "种植农作物或树苗", "wheat_seeds", AdvancementFrame.TASK,
                11.0f, 1.0f, AdvancementTrigger.BLOCK_PLACE,
                Set.of("wheat", "carrot", "potato", "beetroot", "bamboo", "cocoa", "torchflower", "pitcher_crop"),
                Set.of("_sapling")));
        list.add(leaf("minecraft:husbandry/fishy_business", "minecraft:husbandry/root", "美味的鱼儿", "捕到一条鱼", "fishing_rod", AdvancementFrame.TASK,
                12.0f, 1.0f, AdvancementTrigger.CRAFT_ITEM, Set.of("fishing_rod")));
        list.add(leaf("minecraft:husbandry/balanced_diet", "minecraft:husbandry/plant_seed", "均衡膳食", "品尝所有能吃的东西", "bread", AdvancementFrame.CHALLENGE,
                11.0f, 2.0f, AdvancementTrigger.CONSUME_ITEM, Set.of("bread", "apple", "cooked_beef", "cooked_porkchop", "golden_apple", "cake", "cookie")));
        list.add(leaf("minecraft:husbandry/rooted", "minecraft:husbandry/root", "根深蒂固", "获取下界疣", "nether_wart", AdvancementFrame.TASK,
                12.0f, 2.0f, AdvancementTrigger.BLOCK_BREAK, Set.of("nether_wart")));

        return list;
    }

    private static AdvancementDef root(String id, String title, String desc, String icon, AdvancementFrame frame, float x, float y) {
        return new AdvancementDef.Builder()
                .id(id).title(title).description(desc)
                .icon(id(icon)).frame(frame).showToast(false).pos(x, y)
                .root().build();
    }

    private static AdvancementDef leaf(String id, String parent, String title, String desc, String icon, AdvancementFrame frame,
                                       float x, float y, AdvancementTrigger trigger, Set<String> matchBlocks) {
        return new AdvancementDef.Builder()
                .id(id).parent(parent).title(title).description(desc)
                .icon(id(icon)).frame(frame).showToast(true).pos(x, y)
                .trigger(trigger).criterion("0")
                .matchBlocks(matchBlocks).build();
    }

    private static AdvancementDef leaf(String id, String parent, String title, String desc, String icon, AdvancementFrame frame,
                                       float x, float y, AdvancementTrigger trigger, Set<String> matchBlocks, String matchDimension) {
        return new AdvancementDef.Builder()
                .id(id).parent(parent).title(title).description(desc)
                .icon(id(icon)).frame(frame).showToast(true).pos(x, y)
                .trigger(trigger).criterion("0")
                .matchBlocks(matchBlocks).matchDimension(matchDimension).build();
    }

    private static AdvancementDef leaf(String id, String parent, String title, String desc, String icon, AdvancementFrame frame,
                                       float x, float y, AdvancementTrigger trigger, Set<String> matchBlocks, Set<String> matchSuffixes) {
        return new AdvancementDef.Builder()
                .id(id).parent(parent).title(title).description(desc)
                .icon(id(icon)).frame(frame).showToast(true).pos(x, y)
                .trigger(trigger).criterion("0")
                .matchBlocks(matchBlocks).matchSuffixes(matchSuffixes).build();
    }

    private static AdvancementDef leaf(String id, String parent, String title, String desc, String icon, AdvancementFrame frame,
                                       float x, float y, AdvancementTrigger trigger, Set<String> matchBlocks, Set<String> matchSuffixes, Set<String> matchMobs) {
        return new AdvancementDef.Builder()
                .id(id).parent(parent).title(title).description(desc)
                .icon(id(icon)).frame(frame).showToast(true).pos(x, y)
                .trigger(trigger).criterion("0")
                .matchBlocks(matchBlocks).matchSuffixes(matchSuffixes).matchMobs(matchMobs).build();
    }

    /** 解析物品名到协议 registry id；失败时回退到 1 (stone? 仍非 0，避免空图标)。 */
    private static int id(String name) {
        int v = BlockManager.getItemIdByName(name);
        return v > 0 ? v : 1;
    }
}
