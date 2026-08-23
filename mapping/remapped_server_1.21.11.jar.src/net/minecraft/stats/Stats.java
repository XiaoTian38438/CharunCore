/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class Stats {
/*  13 */   public static final StatType<Block> BLOCK_MINED = makeRegistryStatType("mined", (Registry<Block>)BuiltInRegistries.BLOCK);
/*  14 */   public static final StatType<Item> ITEM_CRAFTED = makeRegistryStatType("crafted", (Registry<Item>)BuiltInRegistries.ITEM);
/*  15 */   public static final StatType<Item> ITEM_USED = makeRegistryStatType("used", (Registry<Item>)BuiltInRegistries.ITEM);
/*  16 */   public static final StatType<Item> ITEM_BROKEN = makeRegistryStatType("broken", (Registry<Item>)BuiltInRegistries.ITEM);
/*  17 */   public static final StatType<Item> ITEM_PICKED_UP = makeRegistryStatType("picked_up", (Registry<Item>)BuiltInRegistries.ITEM);
/*  18 */   public static final StatType<Item> ITEM_DROPPED = makeRegistryStatType("dropped", (Registry<Item>)BuiltInRegistries.ITEM);
/*  19 */   public static final StatType<EntityType<?>> ENTITY_KILLED = makeRegistryStatType("killed", (Registry<EntityType<?>>)BuiltInRegistries.ENTITY_TYPE);
/*  20 */   public static final StatType<EntityType<?>> ENTITY_KILLED_BY = makeRegistryStatType("killed_by", (Registry<EntityType<?>>)BuiltInRegistries.ENTITY_TYPE);
/*  21 */   public static final StatType<Identifier> CUSTOM = makeRegistryStatType("custom", BuiltInRegistries.CUSTOM_STAT);
/*     */ 
/*     */   
/*  24 */   public static final Identifier LEAVE_GAME = makeCustomStat("leave_game", StatFormatter.DEFAULT);
/*     */   
/*  26 */   public static final Identifier PLAY_TIME = makeCustomStat("play_time", StatFormatter.TIME);
/*  27 */   public static final Identifier TOTAL_WORLD_TIME = makeCustomStat("total_world_time", StatFormatter.TIME);
/*  28 */   public static final Identifier TIME_SINCE_DEATH = makeCustomStat("time_since_death", StatFormatter.TIME);
/*  29 */   public static final Identifier TIME_SINCE_REST = makeCustomStat("time_since_rest", StatFormatter.TIME);
/*  30 */   public static final Identifier CROUCH_TIME = makeCustomStat("sneak_time", StatFormatter.TIME);
/*  31 */   public static final Identifier WALK_ONE_CM = makeCustomStat("walk_one_cm", StatFormatter.DISTANCE);
/*  32 */   public static final Identifier CROUCH_ONE_CM = makeCustomStat("crouch_one_cm", StatFormatter.DISTANCE);
/*  33 */   public static final Identifier SPRINT_ONE_CM = makeCustomStat("sprint_one_cm", StatFormatter.DISTANCE);
/*  34 */   public static final Identifier WALK_ON_WATER_ONE_CM = makeCustomStat("walk_on_water_one_cm", StatFormatter.DISTANCE);
/*  35 */   public static final Identifier FALL_ONE_CM = makeCustomStat("fall_one_cm", StatFormatter.DISTANCE);
/*  36 */   public static final Identifier CLIMB_ONE_CM = makeCustomStat("climb_one_cm", StatFormatter.DISTANCE);
/*  37 */   public static final Identifier FLY_ONE_CM = makeCustomStat("fly_one_cm", StatFormatter.DISTANCE);
/*  38 */   public static final Identifier WALK_UNDER_WATER_ONE_CM = makeCustomStat("walk_under_water_one_cm", StatFormatter.DISTANCE);
/*  39 */   public static final Identifier MINECART_ONE_CM = makeCustomStat("minecart_one_cm", StatFormatter.DISTANCE);
/*  40 */   public static final Identifier BOAT_ONE_CM = makeCustomStat("boat_one_cm", StatFormatter.DISTANCE);
/*  41 */   public static final Identifier PIG_ONE_CM = makeCustomStat("pig_one_cm", StatFormatter.DISTANCE);
/*  42 */   public static final Identifier HAPPY_GHAST_ONE_CM = makeCustomStat("happy_ghast_one_cm", StatFormatter.DISTANCE);
/*  43 */   public static final Identifier HORSE_ONE_CM = makeCustomStat("horse_one_cm", StatFormatter.DISTANCE);
/*  44 */   public static final Identifier AVIATE_ONE_CM = makeCustomStat("aviate_one_cm", StatFormatter.DISTANCE);
/*  45 */   public static final Identifier SWIM_ONE_CM = makeCustomStat("swim_one_cm", StatFormatter.DISTANCE);
/*  46 */   public static final Identifier STRIDER_ONE_CM = makeCustomStat("strider_one_cm", StatFormatter.DISTANCE);
/*  47 */   public static final Identifier NAUTILUS_ONE_CM = makeCustomStat("nautilus_one_cm", StatFormatter.DISTANCE);
/*     */   
/*  49 */   public static final Identifier JUMP = makeCustomStat("jump", StatFormatter.DEFAULT);
/*  50 */   public static final Identifier DROP = makeCustomStat("drop", StatFormatter.DEFAULT);
/*     */   
/*  52 */   public static final Identifier DAMAGE_DEALT = makeCustomStat("damage_dealt", StatFormatter.DIVIDE_BY_TEN);
/*  53 */   public static final Identifier DAMAGE_DEALT_ABSORBED = makeCustomStat("damage_dealt_absorbed", StatFormatter.DIVIDE_BY_TEN);
/*  54 */   public static final Identifier DAMAGE_DEALT_RESISTED = makeCustomStat("damage_dealt_resisted", StatFormatter.DIVIDE_BY_TEN);
/*     */   
/*  56 */   public static final Identifier DAMAGE_TAKEN = makeCustomStat("damage_taken", StatFormatter.DIVIDE_BY_TEN);
/*  57 */   public static final Identifier DAMAGE_BLOCKED_BY_SHIELD = makeCustomStat("damage_blocked_by_shield", StatFormatter.DIVIDE_BY_TEN);
/*  58 */   public static final Identifier DAMAGE_ABSORBED = makeCustomStat("damage_absorbed", StatFormatter.DIVIDE_BY_TEN);
/*  59 */   public static final Identifier DAMAGE_RESISTED = makeCustomStat("damage_resisted", StatFormatter.DIVIDE_BY_TEN);
/*     */   
/*  61 */   public static final Identifier DEATHS = makeCustomStat("deaths", StatFormatter.DEFAULT);
/*  62 */   public static final Identifier MOB_KILLS = makeCustomStat("mob_kills", StatFormatter.DEFAULT);
/*  63 */   public static final Identifier ANIMALS_BRED = makeCustomStat("animals_bred", StatFormatter.DEFAULT);
/*  64 */   public static final Identifier PLAYER_KILLS = makeCustomStat("player_kills", StatFormatter.DEFAULT);
/*  65 */   public static final Identifier FISH_CAUGHT = makeCustomStat("fish_caught", StatFormatter.DEFAULT);
/*     */   
/*  67 */   public static final Identifier TALKED_TO_VILLAGER = makeCustomStat("talked_to_villager", StatFormatter.DEFAULT);
/*  68 */   public static final Identifier TRADED_WITH_VILLAGER = makeCustomStat("traded_with_villager", StatFormatter.DEFAULT);
/*     */   
/*  70 */   public static final Identifier EAT_CAKE_SLICE = makeCustomStat("eat_cake_slice", StatFormatter.DEFAULT);
/*  71 */   public static final Identifier FILL_CAULDRON = makeCustomStat("fill_cauldron", StatFormatter.DEFAULT);
/*  72 */   public static final Identifier USE_CAULDRON = makeCustomStat("use_cauldron", StatFormatter.DEFAULT);
/*  73 */   public static final Identifier CLEAN_ARMOR = makeCustomStat("clean_armor", StatFormatter.DEFAULT);
/*  74 */   public static final Identifier CLEAN_BANNER = makeCustomStat("clean_banner", StatFormatter.DEFAULT);
/*  75 */   public static final Identifier CLEAN_SHULKER_BOX = makeCustomStat("clean_shulker_box", StatFormatter.DEFAULT);
/*  76 */   public static final Identifier INTERACT_WITH_BREWINGSTAND = makeCustomStat("interact_with_brewingstand", StatFormatter.DEFAULT);
/*  77 */   public static final Identifier INTERACT_WITH_BEACON = makeCustomStat("interact_with_beacon", StatFormatter.DEFAULT);
/*  78 */   public static final Identifier INSPECT_DROPPER = makeCustomStat("inspect_dropper", StatFormatter.DEFAULT);
/*  79 */   public static final Identifier INSPECT_HOPPER = makeCustomStat("inspect_hopper", StatFormatter.DEFAULT);
/*  80 */   public static final Identifier INSPECT_DISPENSER = makeCustomStat("inspect_dispenser", StatFormatter.DEFAULT);
/*  81 */   public static final Identifier PLAY_NOTEBLOCK = makeCustomStat("play_noteblock", StatFormatter.DEFAULT);
/*  82 */   public static final Identifier TUNE_NOTEBLOCK = makeCustomStat("tune_noteblock", StatFormatter.DEFAULT);
/*  83 */   public static final Identifier POT_FLOWER = makeCustomStat("pot_flower", StatFormatter.DEFAULT);
/*  84 */   public static final Identifier TRIGGER_TRAPPED_CHEST = makeCustomStat("trigger_trapped_chest", StatFormatter.DEFAULT);
/*  85 */   public static final Identifier OPEN_ENDERCHEST = makeCustomStat("open_enderchest", StatFormatter.DEFAULT);
/*  86 */   public static final Identifier ENCHANT_ITEM = makeCustomStat("enchant_item", StatFormatter.DEFAULT);
/*  87 */   public static final Identifier PLAY_RECORD = makeCustomStat("play_record", StatFormatter.DEFAULT);
/*  88 */   public static final Identifier INTERACT_WITH_FURNACE = makeCustomStat("interact_with_furnace", StatFormatter.DEFAULT);
/*  89 */   public static final Identifier INTERACT_WITH_CRAFTING_TABLE = makeCustomStat("interact_with_crafting_table", StatFormatter.DEFAULT);
/*  90 */   public static final Identifier OPEN_CHEST = makeCustomStat("open_chest", StatFormatter.DEFAULT);
/*  91 */   public static final Identifier SLEEP_IN_BED = makeCustomStat("sleep_in_bed", StatFormatter.DEFAULT);
/*  92 */   public static final Identifier OPEN_SHULKER_BOX = makeCustomStat("open_shulker_box", StatFormatter.DEFAULT);
/*  93 */   public static final Identifier OPEN_BARREL = makeCustomStat("open_barrel", StatFormatter.DEFAULT);
/*  94 */   public static final Identifier INTERACT_WITH_BLAST_FURNACE = makeCustomStat("interact_with_blast_furnace", StatFormatter.DEFAULT);
/*  95 */   public static final Identifier INTERACT_WITH_SMOKER = makeCustomStat("interact_with_smoker", StatFormatter.DEFAULT);
/*  96 */   public static final Identifier INTERACT_WITH_LECTERN = makeCustomStat("interact_with_lectern", StatFormatter.DEFAULT);
/*  97 */   public static final Identifier INTERACT_WITH_CAMPFIRE = makeCustomStat("interact_with_campfire", StatFormatter.DEFAULT);
/*  98 */   public static final Identifier INTERACT_WITH_CARTOGRAPHY_TABLE = makeCustomStat("interact_with_cartography_table", StatFormatter.DEFAULT);
/*  99 */   public static final Identifier INTERACT_WITH_LOOM = makeCustomStat("interact_with_loom", StatFormatter.DEFAULT);
/* 100 */   public static final Identifier INTERACT_WITH_STONECUTTER = makeCustomStat("interact_with_stonecutter", StatFormatter.DEFAULT);
/* 101 */   public static final Identifier BELL_RING = makeCustomStat("bell_ring", StatFormatter.DEFAULT);
/* 102 */   public static final Identifier RAID_TRIGGER = makeCustomStat("raid_trigger", StatFormatter.DEFAULT);
/* 103 */   public static final Identifier RAID_WIN = makeCustomStat("raid_win", StatFormatter.DEFAULT);
/* 104 */   public static final Identifier INTERACT_WITH_ANVIL = makeCustomStat("interact_with_anvil", StatFormatter.DEFAULT);
/* 105 */   public static final Identifier INTERACT_WITH_GRINDSTONE = makeCustomStat("interact_with_grindstone", StatFormatter.DEFAULT);
/* 106 */   public static final Identifier TARGET_HIT = makeCustomStat("target_hit", StatFormatter.DEFAULT);
/* 107 */   public static final Identifier INTERACT_WITH_SMITHING_TABLE = makeCustomStat("interact_with_smithing_table", StatFormatter.DEFAULT);
/*     */   
/*     */   private static Identifier makeCustomStat(String paramString, StatFormatter paramStatFormatter) {
/* 110 */     Identifier identifier = Identifier.withDefaultNamespace(paramString);
/* 111 */     Registry.register(BuiltInRegistries.CUSTOM_STAT, paramString, identifier);
/* 112 */     CUSTOM.get(identifier, paramStatFormatter);
/* 113 */     return identifier;
/*     */   }
/*     */   
/*     */   private static <T> StatType<T> makeRegistryStatType(String paramString, Registry<T> paramRegistry) {
/* 117 */     MutableComponent mutableComponent = Component.translatable("stat_type.minecraft." + paramString);
/* 118 */     return (StatType<T>)Registry.register(BuiltInRegistries.STAT_TYPE, paramString, new StatType<>(paramRegistry, (Component)mutableComponent));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\stats\Stats.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */