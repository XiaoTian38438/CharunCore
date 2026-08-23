/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ 
/*    */ public interface EnchantmentTags {
/*  8 */   public static final TagKey<Enchantment> TOOLTIP_ORDER = create("tooltip_order");
/*    */   
/* 10 */   public static final TagKey<Enchantment> ARMOR_EXCLUSIVE = create("exclusive_set/armor");
/* 11 */   public static final TagKey<Enchantment> BOOTS_EXCLUSIVE = create("exclusive_set/boots");
/* 12 */   public static final TagKey<Enchantment> BOW_EXCLUSIVE = create("exclusive_set/bow");
/* 13 */   public static final TagKey<Enchantment> CROSSBOW_EXCLUSIVE = create("exclusive_set/crossbow");
/* 14 */   public static final TagKey<Enchantment> DAMAGE_EXCLUSIVE = create("exclusive_set/damage");
/* 15 */   public static final TagKey<Enchantment> MINING_EXCLUSIVE = create("exclusive_set/mining");
/* 16 */   public static final TagKey<Enchantment> RIPTIDE_EXCLUSIVE = create("exclusive_set/riptide");
/*    */ 
/*    */   
/* 19 */   public static final TagKey<Enchantment> TRADEABLE = create("tradeable");
/* 20 */   public static final TagKey<Enchantment> DOUBLE_TRADE_PRICE = create("double_trade_price");
/* 21 */   public static final TagKey<Enchantment> IN_ENCHANTING_TABLE = create("in_enchanting_table");
/* 22 */   public static final TagKey<Enchantment> ON_MOB_SPAWN_EQUIPMENT = create("on_mob_spawn_equipment");
/* 23 */   public static final TagKey<Enchantment> ON_TRADED_EQUIPMENT = create("on_traded_equipment");
/* 24 */   public static final TagKey<Enchantment> ON_RANDOM_LOOT = create("on_random_loot");
/* 25 */   public static final TagKey<Enchantment> CURSE = create("curse");
/* 26 */   public static final TagKey<Enchantment> SMELTS_LOOT = create("smelts_loot");
/*    */   
/* 28 */   public static final TagKey<Enchantment> PREVENTS_BEE_SPAWNS_WHEN_MINING = create("prevents_bee_spawns_when_mining");
/* 29 */   public static final TagKey<Enchantment> PREVENTS_DECORATED_POT_SHATTERING = create("prevents_decorated_pot_shattering");
/* 30 */   public static final TagKey<Enchantment> PREVENTS_ICE_MELTING = create("prevents_ice_melting");
/* 31 */   public static final TagKey<Enchantment> PREVENTS_INFESTED_SPAWNS = create("prevents_infested_spawns");
/*    */ 
/*    */   
/* 34 */   public static final TagKey<Enchantment> TREASURE = create("treasure");
/* 35 */   public static final TagKey<Enchantment> NON_TREASURE = create("non_treasure");
/*    */   
/* 37 */   public static final TagKey<Enchantment> TRADES_DESERT_COMMON = create("trades/desert_common");
/* 38 */   public static final TagKey<Enchantment> TRADES_JUNGLE_COMMON = create("trades/jungle_common");
/* 39 */   public static final TagKey<Enchantment> TRADES_PLAINS_COMMON = create("trades/plains_common");
/* 40 */   public static final TagKey<Enchantment> TRADES_SAVANNA_COMMON = create("trades/savanna_common");
/* 41 */   public static final TagKey<Enchantment> TRADES_SNOW_COMMON = create("trades/snow_common");
/* 42 */   public static final TagKey<Enchantment> TRADES_SWAMP_COMMON = create("trades/swamp_common");
/* 43 */   public static final TagKey<Enchantment> TRADES_TAIGA_COMMON = create("trades/taiga_common");
/*    */   
/* 45 */   public static final TagKey<Enchantment> TRADES_DESERT_SPECIAL = create("trades/desert_special");
/* 46 */   public static final TagKey<Enchantment> TRADES_JUNGLE_SPECIAL = create("trades/jungle_special");
/* 47 */   public static final TagKey<Enchantment> TRADES_PLAINS_SPECIAL = create("trades/plains_special");
/* 48 */   public static final TagKey<Enchantment> TRADES_SAVANNA_SPECIAL = create("trades/savanna_special");
/* 49 */   public static final TagKey<Enchantment> TRADES_SNOW_SPECIAL = create("trades/snow_special");
/* 50 */   public static final TagKey<Enchantment> TRADES_SWAMP_SPECIAL = create("trades/swamp_special");
/* 51 */   public static final TagKey<Enchantment> TRADES_TAIGA_SPECIAL = create("trades/taiga_special");
/*    */   
/*    */   private static TagKey<Enchantment> create(String paramString) {
/* 54 */     return TagKey.create(Registries.ENCHANTMENT, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\EnchantmentTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */