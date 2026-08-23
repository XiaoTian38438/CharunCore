/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class LootItemConditions {
/*  9 */   public static final LootItemConditionType INVERTED = register("inverted", (MapCodec)InvertedLootItemCondition.CODEC);
/* 10 */   public static final LootItemConditionType ANY_OF = register("any_of", (MapCodec)AnyOfCondition.CODEC);
/* 11 */   public static final LootItemConditionType ALL_OF = register("all_of", (MapCodec)AllOfCondition.CODEC);
/* 12 */   public static final LootItemConditionType RANDOM_CHANCE = register("random_chance", (MapCodec)LootItemRandomChanceCondition.CODEC);
/* 13 */   public static final LootItemConditionType RANDOM_CHANCE_WITH_ENCHANTED_BONUS = register("random_chance_with_enchanted_bonus", (MapCodec)LootItemRandomChanceWithEnchantedBonusCondition.CODEC);
/* 14 */   public static final LootItemConditionType ENTITY_PROPERTIES = register("entity_properties", (MapCodec)LootItemEntityPropertyCondition.CODEC);
/* 15 */   public static final LootItemConditionType KILLED_BY_PLAYER = register("killed_by_player", (MapCodec)LootItemKilledByPlayerCondition.CODEC);
/* 16 */   public static final LootItemConditionType ENTITY_SCORES = register("entity_scores", (MapCodec)EntityHasScoreCondition.CODEC);
/* 17 */   public static final LootItemConditionType BLOCK_STATE_PROPERTY = register("block_state_property", (MapCodec)LootItemBlockStatePropertyCondition.CODEC);
/* 18 */   public static final LootItemConditionType MATCH_TOOL = register("match_tool", (MapCodec)MatchTool.CODEC);
/* 19 */   public static final LootItemConditionType TABLE_BONUS = register("table_bonus", (MapCodec)BonusLevelTableCondition.CODEC);
/* 20 */   public static final LootItemConditionType SURVIVES_EXPLOSION = register("survives_explosion", (MapCodec)ExplosionCondition.CODEC);
/* 21 */   public static final LootItemConditionType DAMAGE_SOURCE_PROPERTIES = register("damage_source_properties", (MapCodec)DamageSourceCondition.CODEC);
/* 22 */   public static final LootItemConditionType LOCATION_CHECK = register("location_check", (MapCodec)LocationCheck.CODEC);
/* 23 */   public static final LootItemConditionType WEATHER_CHECK = register("weather_check", (MapCodec)WeatherCheck.CODEC);
/* 24 */   public static final LootItemConditionType REFERENCE = register("reference", (MapCodec)ConditionReference.CODEC);
/* 25 */   public static final LootItemConditionType TIME_CHECK = register("time_check", (MapCodec)TimeCheck.CODEC);
/* 26 */   public static final LootItemConditionType VALUE_CHECK = register("value_check", (MapCodec)ValueCheckCondition.CODEC);
/* 27 */   public static final LootItemConditionType ENCHANTMENT_ACTIVE_CHECK = register("enchantment_active_check", (MapCodec)EnchantmentActiveCheck.CODEC);
/*    */   
/*    */   private static LootItemConditionType register(String paramString, MapCodec<? extends LootItemCondition> paramMapCodec) {
/* 30 */     return (LootItemConditionType)Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, Identifier.withDefaultNamespace(paramString), new LootItemConditionType(paramMapCodec));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemConditions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */