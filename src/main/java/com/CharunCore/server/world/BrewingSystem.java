package com.CharunCore.server.world;

import com.CharunCore.server.utils.BlockManager;

import java.util.HashMap;
import java.util.Map;

public class BrewingSystem {

    public static boolean isBottle(int itemId) {
        if (itemId <= 0) return false;
        String n = BlockManager.itemIdToName(itemId);
        // #12 修复: 本服药水物品统一为 "potion"(效果存 inventoryPotion 字符串)。
        // 玩家放入的水瓶/粗制药水/成品药水 id 都是 potion -> 必须视为瓶子,
        // 曾只认 water_bottle/awkward_potion 等 -> 水瓶放进去 isBottle=false -> 无法酿造。
        return n != null && (n.equals("potion") || n.equals("water_bottle") || n.equals("awkward_potion")
                || n.equals("splash_potion") || n.equals("lingering_potion") || n.equals("glass_bottle"));
    }

    public static boolean isFuel(String itemName) {
        return "blaze_powder".equals(itemName);
    }

    /**
     * 基础酿造配方: 瓶子 + 材料 → 结果瓶子名。
     * 注意: 本服物品槽不序列化 NBT, 故药水类型在客户端无法区分 (均为 potion 物品);
     * 此处按名称计算, 若结果名无法解析为物品 id 则回退为原瓶子。
     */
    private static final Map<String, String> BASE = new HashMap<>();
    static {
        // #12 修复: 玩家放入的水瓶在物品栏是 "potion"(效果为 null), 等价原版 water_bottle。
        BASE.put("potion+nether_wart", "awkward_potion");
        BASE.put("water_bottle+nether_wart", "awkward_potion");
        BASE.put("awkward_potion+golden_carrot", "potion");        // 夜视
        BASE.put("awkward_potion+spider_eye", "potion");           // 中毒
        BASE.put("awkward_potion+pufferfish", "potion");          // 水下呼吸
        BASE.put("awkward_potion+sugar", "potion");               // 迅捷
        BASE.put("awkward_potion+blaze_powder", "potion");        // 力量
        BASE.put("awkward_potion+magma_cream", "potion");         // 抗火(原版两种材料均可)
        BASE.put("awkward_potion+ghast_tear", "potion");          // 再生
        BASE.put("awkward_potion+fermented_spider_eye", "potion");// 虚弱
        BASE.put("awkward_potion+phantom_membrane", "potion");    // 缓降
        BASE.put("awkward_potion+rabbit_foot", "potion");         // 跳跃
        BASE.put("awkward_potion+glistering_melon_slice", "potion"); // 治疗
        BASE.put("potion+fermented_spider_eye", "potion");        // 反转(治疗→伤害/夜视→隐身/其余→虚弱)
        BASE.put("potion+redstone", "potion");                    // 延长
        BASE.put("potion+glowstone_dust", "potion");              // 增强
        BASE.put("potion+gunpowder", "splash_potion");            // 喷溅
        BASE.put("splash_potion+dragon_breath", "lingering_potion");
    }

    /** #12 水瓶等价: 物品栏 "potion" 且无效果 = 原版 water_bottle。 */
    private static String normalizeBottle(String bottleName, String currentEffect) {
        if ("potion".equals(bottleName) && (currentEffect == null || currentEffect.isEmpty())) {
            return "water_bottle";
        }
        return bottleName;
    }

    public static String getBrewResult(String bottleName, String ingredientName) {
        if (bottleName == null || ingredientName == null) return null;
        String key = bottleName + "+" + ingredientName;
        return BASE.get(key);
    }

    /**
     * 判断瓶子 + 材料是否可酿造 (对齐 PotionBrewing.hasMix)。
     * 修饰型酿造 (红石延长/荧石增强/发酵蛛眼反转) 结果瓶名不变, 仅效果字符串变化,
     * 故除比较瓶名外, 还需检查 resolveBrewEffect 是否产生新的效果字符串。
     */
    public static boolean canBrew(String bottleName, String ingredientName, String currentEffect) {
        if (bottleName == null || ingredientName == null) return false;
        String nb = normalizeBottle(bottleName, currentEffect);   // #12 水瓶(potion,无效果)->water_bottle
        String res = getBrewResult(nb, ingredientName);
        if (res != null && !res.equals(nb)) return true;          // 瓶名变化 (基础转化/喷溅/滞留)
        String eff = resolveBrewEffect(nb, ingredientName, currentEffect);
        return eff != null && !eff.equals(currentEffect);         // 瓶名不变但效果变化 (修饰酿造)
    }

    /**
     * 由 "瓶子 + 材料" 计算该瓶子最终携带的药水效果字符串 "效果名|等级|持续刻"。
     * 仅在瓶子当前无效果(水/粗制药水)时作为基础效果; 已有基础效果时由 applyModifier 处理修饰。
     * 返回 null 表示该步骤不赋予效果(如 水+地狱疣→粗制药水)。
     */
    private static String baseEffect(String bottleName, String ingredientName) {
        if (!"awkward_potion".equals(bottleName)) return null;
        // 持续时间对齐原版默认值: poison/regeneration 0:45(900), slow_falling 1:30(1800)
        return switch (ingredientName) {
            case "golden_carrot" -> "night_vision|0|3600";
            case "spider_eye" -> "poison|0|900";
            case "pufferfish" -> "water_breathing|0|3600";
            case "sugar" -> "speed|0|3600";
            case "blaze_powder", "magma_cream" -> "fire_resistance|0|3600";
            case "ghast_tear" -> "regeneration|0|900";
            case "fermented_spider_eye" -> "weakness|0|1800";
            case "phantom_membrane" -> "slow_falling|0|1800";
            case "rabbit_foot" -> "jump_boost|0|3600";
            case "glistering_melon_slice" -> "instant_health|0|1";
            default -> null;
        };
    }

    /**
     * 对已有基础效果字符串施加修饰材料(红石延长/荧石增强/发酵蛛眼反转)。
     * 红石: 持续刻 ×2 (上限 9600)。荧石: 等级 +1。发酵蛛眼: 夜视→隐身, 其余→虚弱。
     * 火药/龙息: 仅改变物品名(喷溅/滞留), 效果字符串不变。
     */
    private static String applyModifier(String eff, String ingredientName) {
        if (eff == null) return null;
        String[] p = eff.split("\\|");
        String name = p[0];
        int amp = Integer.parseInt(p[1]);
        int dur = Integer.parseInt(p[2]);
        return switch (ingredientName) {
            case "redstone" -> name + "|" + amp + "|" + Math.min(9600, dur * 2);
            case "glowstone_dust" -> name + "|" + Math.min(255, amp + 1) + "|" + dur;
            case "fermented_spider_eye" -> {
                // 原版: 夜视→隐身, 治疗→伤害, 其余→虚弱
                if ("night_vision".equals(name)) yield "invisibility|0|3600";
                if ("instant_health".equals(name)) yield "instant_damage|0|1";
                yield "weakness|0|1800";
            }
            default -> eff;
        };
    }

    /** 计算酿造完成后瓶子应携带的效果字符串(供 ContainerStore 写入 potionType)。 */
    public static String resolveBrewEffect(String bottleName, String ingredientName, String currentEffect) {
        if (bottleName == null || ingredientName == null) return null;
        if (currentEffect != null) return applyModifier(currentEffect, ingredientName);
        return baseEffect(bottleName, ingredientName);
    }
}
