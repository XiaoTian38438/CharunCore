package com.CharunCore.server.advancement;

/**
 * 本成就系统支持的触发类型 (对应原版 CriterionTrigger 的简化子集)。
 * 游戏事件发生时由 NetworkHandler 调用 AdvancementManager 的对应 onXxx 方法，
 * 再由 AdvancementManager 根据 trigger 类型匹配相应的 AdvancementDef 并判定。
 */
public enum AdvancementTrigger {
    /** 破坏方块 (原版 minecraft:mined_block)。 */
    BLOCK_BREAK,
    /** 放置方块 (原版 minecraft:placed_block)。 */
    BLOCK_PLACE,
    /** 击杀生物 (原版 minecraft:player_killed_entity)。 */
    ENTITY_KILL,
    /** 进入维度 (原版 minecraft:location / entered_custom_dimension)。 */
    ENTER_DIMENSION,
    /** 合成物品 (原版 minecraft:recipe_crafted)。 */
    CRAFT_ITEM,
    /** 附魔物品 (原版 minecraft:enchanted_item)。 */
    ENCHANT_ITEM,
    /** 食用物品 (原版 minecraft:consume_item)。 */
    CONSUME_ITEM
}
