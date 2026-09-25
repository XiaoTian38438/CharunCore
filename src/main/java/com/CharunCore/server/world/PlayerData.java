// --- PlayerData.java 增改部分 ---
package com.CharunCore.server.world;

public class PlayerData {
    public double x = 8.5, y = 61.0, z = 8.5; // standing on grass_block at y=60
    public float yaw = 0, pitch = 0;
    public int[] inventoryIds = new int[46];
    public int[] inventoryCounts = new int[46];
    /** 每个槽位已消耗的耐久 (0 = 全新)。仅对有 max_damage 的工具/武器/护甲有意义。 */
    public int[] inventoryDamage = new int[46];
    /** 每个槽位携带的附魔: 附魔 id -> 等级。用于精准采集 / 时运 / 保护等。 */
    @SuppressWarnings("unchecked")
    public java.util.Map<Integer, Integer>[] inventoryEnchants = new java.util.Map[46];
    /** 每个槽位携带的药水类型: "效果名|等级|持续刻" 或 null。酿造产出时写入, 饮用时结算。 */
    public String[] inventoryPotion = new String[46];
    /** 每个槽位物品自定义名称(铁砧改名), null 或空串表示无。 */
    public String[] inventoryCustomName = new String[46];
    /** #19 每个槽位盔甲纹饰: trim material/pattern 注册 id (-1=无)。 */
    public int[] inventoryTrimMaterial = new int[46];
    public int[] inventoryTrimPattern = new int[46];
    public int heldSlot = 0; // 【增】当前选中的快捷栏 (0-8)
    public float health = 20.0f;
    public int   food   = 20;
    public int   airTicks = 300;
    public int   xpLevel = 0;
    public int   xpTotal = 0;
    public float xpProgress = 0.0f;
    public float saturation = 5.0f;
    public float exhaustion = 0.0f;
    public int   gameMode = 0;                      // 生存服务器默认生存模式
                                                 // 持久化：GSON 序列化本 public 字段；NetworkHandler.loadPlayerData 读取 data.gameMode、
                                                 // savePlayerData 写回 data.gameMode，因此 gameMode 已随玩家数据落盘 (P2-7 已核实)。
    public String dimension = "minecraft:overworld"; // 退出时所在维度
    /** 玩家用户名(迁移/审计用), 不参与游戏逻辑。 */
    public String username;
    /** 已授予 (整体完成) 的成就 key 集合，随玩家数据落盘 (GSON)，用于登录时恢复进度与去重。 */
    public java.util.Set<String> unlockedAdvancements = new java.util.HashSet<>();
    /** 是否已确定过出生点。false 表示首次进服, 需要搜索安全陆地出生点。 */
    public boolean spawnInitialized = false;
    /** 玩家个人复活点 (床/重生锚)。y = Integer.MIN_VALUE 表示未设置, 使用世界出生点。 */
    public int respawnX = 0, respawnY = Integer.MIN_VALUE, respawnZ = 0;
    public String respawnDimension = "minecraft:overworld";
    /** 插件持久数据 (PersistentDataContainer 底存, String→String, 随 GSON 落盘)。 */
    public java.util.Map<String, String> pluginData = new java.util.HashMap<>();
    /** 插件 ItemMeta 扩展组件: lore(\n 分隔) / 不可破坏 / 发光覆盖 (-1 无, 0 false, 1 true)。 */
    public String[] inventoryLore = new String[46];
    public boolean[] inventoryUnbreakable = new boolean[46];
    public int[] inventoryGlint = new int[46];

    public PlayerData() {
        for (int i = 0; i < 46; i++) {
            inventoryEnchants[i] = new java.util.HashMap<>();
            inventoryTrimMaterial[i] = -1;
            inventoryTrimPattern[i] = -1;
        }
    }

    /** 读取某槽位上某附魔的等级 (0 = 无)。 */
    public int getSlotEnchant(int slot, int enchantId) {
        java.util.Map<Integer, Integer> m = inventoryEnchants[slot];
        return m == null ? 0 : m.getOrDefault(enchantId, 0);
    }
}