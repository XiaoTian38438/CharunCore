/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ 
/*     */ public class BuiltInLootTables
/*     */ {
/*  15 */   private static final Set<ResourceKey<LootTable>> LOCATIONS = new HashSet<>();
/*  16 */   private static final Set<ResourceKey<LootTable>> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);
/*     */ 
/*     */   
/*  19 */   public static final ResourceKey<LootTable> SPAWN_BONUS_CHEST = register("chests/spawn_bonus_chest");
/*  20 */   public static final ResourceKey<LootTable> END_CITY_TREASURE = register("chests/end_city_treasure");
/*  21 */   public static final ResourceKey<LootTable> SIMPLE_DUNGEON = register("chests/simple_dungeon");
/*  22 */   public static final ResourceKey<LootTable> VILLAGE_WEAPONSMITH = register("chests/village/village_weaponsmith");
/*  23 */   public static final ResourceKey<LootTable> VILLAGE_TOOLSMITH = register("chests/village/village_toolsmith");
/*  24 */   public static final ResourceKey<LootTable> VILLAGE_ARMORER = register("chests/village/village_armorer");
/*  25 */   public static final ResourceKey<LootTable> VILLAGE_CARTOGRAPHER = register("chests/village/village_cartographer");
/*  26 */   public static final ResourceKey<LootTable> VILLAGE_MASON = register("chests/village/village_mason");
/*  27 */   public static final ResourceKey<LootTable> VILLAGE_SHEPHERD = register("chests/village/village_shepherd");
/*  28 */   public static final ResourceKey<LootTable> VILLAGE_BUTCHER = register("chests/village/village_butcher");
/*  29 */   public static final ResourceKey<LootTable> VILLAGE_FLETCHER = register("chests/village/village_fletcher");
/*  30 */   public static final ResourceKey<LootTable> VILLAGE_FISHER = register("chests/village/village_fisher");
/*  31 */   public static final ResourceKey<LootTable> VILLAGE_TANNERY = register("chests/village/village_tannery");
/*  32 */   public static final ResourceKey<LootTable> VILLAGE_TEMPLE = register("chests/village/village_temple");
/*  33 */   public static final ResourceKey<LootTable> VILLAGE_DESERT_HOUSE = register("chests/village/village_desert_house");
/*  34 */   public static final ResourceKey<LootTable> VILLAGE_PLAINS_HOUSE = register("chests/village/village_plains_house");
/*  35 */   public static final ResourceKey<LootTable> VILLAGE_TAIGA_HOUSE = register("chests/village/village_taiga_house");
/*  36 */   public static final ResourceKey<LootTable> VILLAGE_SNOWY_HOUSE = register("chests/village/village_snowy_house");
/*  37 */   public static final ResourceKey<LootTable> VILLAGE_SAVANNA_HOUSE = register("chests/village/village_savanna_house");
/*  38 */   public static final ResourceKey<LootTable> ABANDONED_MINESHAFT = register("chests/abandoned_mineshaft");
/*  39 */   public static final ResourceKey<LootTable> NETHER_BRIDGE = register("chests/nether_bridge");
/*  40 */   public static final ResourceKey<LootTable> STRONGHOLD_LIBRARY = register("chests/stronghold_library");
/*  41 */   public static final ResourceKey<LootTable> STRONGHOLD_CROSSING = register("chests/stronghold_crossing");
/*  42 */   public static final ResourceKey<LootTable> STRONGHOLD_CORRIDOR = register("chests/stronghold_corridor");
/*  43 */   public static final ResourceKey<LootTable> DESERT_PYRAMID = register("chests/desert_pyramid");
/*  44 */   public static final ResourceKey<LootTable> JUNGLE_TEMPLE = register("chests/jungle_temple");
/*  45 */   public static final ResourceKey<LootTable> JUNGLE_TEMPLE_DISPENSER = register("chests/jungle_temple_dispenser");
/*  46 */   public static final ResourceKey<LootTable> IGLOO_CHEST = register("chests/igloo_chest");
/*  47 */   public static final ResourceKey<LootTable> WOODLAND_MANSION = register("chests/woodland_mansion");
/*  48 */   public static final ResourceKey<LootTable> UNDERWATER_RUIN_SMALL = register("chests/underwater_ruin_small");
/*  49 */   public static final ResourceKey<LootTable> UNDERWATER_RUIN_BIG = register("chests/underwater_ruin_big");
/*  50 */   public static final ResourceKey<LootTable> BURIED_TREASURE = register("chests/buried_treasure");
/*  51 */   public static final ResourceKey<LootTable> SHIPWRECK_MAP = register("chests/shipwreck_map");
/*  52 */   public static final ResourceKey<LootTable> SHIPWRECK_SUPPLY = register("chests/shipwreck_supply");
/*  53 */   public static final ResourceKey<LootTable> SHIPWRECK_TREASURE = register("chests/shipwreck_treasure");
/*  54 */   public static final ResourceKey<LootTable> PILLAGER_OUTPOST = register("chests/pillager_outpost");
/*  55 */   public static final ResourceKey<LootTable> BASTION_TREASURE = register("chests/bastion_treasure");
/*  56 */   public static final ResourceKey<LootTable> BASTION_OTHER = register("chests/bastion_other");
/*  57 */   public static final ResourceKey<LootTable> BASTION_BRIDGE = register("chests/bastion_bridge");
/*  58 */   public static final ResourceKey<LootTable> BASTION_HOGLIN_STABLE = register("chests/bastion_hoglin_stable");
/*  59 */   public static final ResourceKey<LootTable> ANCIENT_CITY = register("chests/ancient_city");
/*  60 */   public static final ResourceKey<LootTable> ANCIENT_CITY_ICE_BOX = register("chests/ancient_city_ice_box");
/*  61 */   public static final ResourceKey<LootTable> RUINED_PORTAL = register("chests/ruined_portal");
/*  62 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_REWARD = register("chests/trial_chambers/reward");
/*  63 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_REWARD_COMMON = register("chests/trial_chambers/reward_common");
/*  64 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_REWARD_RARE = register("chests/trial_chambers/reward_rare");
/*  65 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_REWARD_UNIQUE = register("chests/trial_chambers/reward_unique");
/*  66 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_REWARD_OMINOUS = register("chests/trial_chambers/reward_ominous");
/*  67 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON = register("chests/trial_chambers/reward_ominous_common");
/*  68 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_REWARD_OMINOUS_RARE = register("chests/trial_chambers/reward_ominous_rare");
/*  69 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE = register("chests/trial_chambers/reward_ominous_unique");
/*  70 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_SUPPLY = register("chests/trial_chambers/supply");
/*  71 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_CORRIDOR = register("chests/trial_chambers/corridor");
/*  72 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_INTERSECTION = register("chests/trial_chambers/intersection");
/*  73 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_INTERSECTION_BARREL = register("chests/trial_chambers/intersection_barrel");
/*  74 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_ENTRANCE = register("chests/trial_chambers/entrance");
/*  75 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_CORRIDOR_DISPENSER = register("dispensers/trial_chambers/corridor");
/*  76 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_CHAMBER_DISPENSER = register("dispensers/trial_chambers/chamber");
/*  77 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_WATER_DISPENSER = register("dispensers/trial_chambers/water");
/*  78 */   public static final ResourceKey<LootTable> TRIAL_CHAMBERS_CORRIDOR_POT = register("pots/trial_chambers/corridor");
/*     */ 
/*     */   
/*  81 */   public static final ResourceKey<LootTable> EQUIPMENT_TRIAL_CHAMBER = register("equipment/trial_chamber");
/*  82 */   public static final ResourceKey<LootTable> EQUIPMENT_TRIAL_CHAMBER_RANGED = register("equipment/trial_chamber_ranged");
/*  83 */   public static final ResourceKey<LootTable> EQUIPMENT_TRIAL_CHAMBER_MELEE = register("equipment/trial_chamber_melee");
/*     */ 
/*     */   
/*  86 */   public static final Map<DyeColor, ResourceKey<LootTable>> SHEEP_BY_DYE = makeDyeKeyMap("entities/sheep");
/*     */ 
/*     */   
/*  89 */   public static final ResourceKey<LootTable> FISHING = register("gameplay/fishing");
/*  90 */   public static final ResourceKey<LootTable> FISHING_JUNK = register("gameplay/fishing/junk");
/*  91 */   public static final ResourceKey<LootTable> FISHING_TREASURE = register("gameplay/fishing/treasure");
/*  92 */   public static final ResourceKey<LootTable> FISHING_FISH = register("gameplay/fishing/fish");
/*     */ 
/*     */   
/*  95 */   public static final ResourceKey<LootTable> CAT_MORNING_GIFT = register("gameplay/cat_morning_gift");
/*  96 */   public static final ResourceKey<LootTable> ARMORER_GIFT = register("gameplay/hero_of_the_village/armorer_gift");
/*  97 */   public static final ResourceKey<LootTable> BUTCHER_GIFT = register("gameplay/hero_of_the_village/butcher_gift");
/*  98 */   public static final ResourceKey<LootTable> CARTOGRAPHER_GIFT = register("gameplay/hero_of_the_village/cartographer_gift");
/*  99 */   public static final ResourceKey<LootTable> CLERIC_GIFT = register("gameplay/hero_of_the_village/cleric_gift");
/* 100 */   public static final ResourceKey<LootTable> FARMER_GIFT = register("gameplay/hero_of_the_village/farmer_gift");
/* 101 */   public static final ResourceKey<LootTable> FISHERMAN_GIFT = register("gameplay/hero_of_the_village/fisherman_gift");
/* 102 */   public static final ResourceKey<LootTable> FLETCHER_GIFT = register("gameplay/hero_of_the_village/fletcher_gift");
/* 103 */   public static final ResourceKey<LootTable> LEATHERWORKER_GIFT = register("gameplay/hero_of_the_village/leatherworker_gift");
/* 104 */   public static final ResourceKey<LootTable> LIBRARIAN_GIFT = register("gameplay/hero_of_the_village/librarian_gift");
/* 105 */   public static final ResourceKey<LootTable> MASON_GIFT = register("gameplay/hero_of_the_village/mason_gift");
/* 106 */   public static final ResourceKey<LootTable> SHEPHERD_GIFT = register("gameplay/hero_of_the_village/shepherd_gift");
/* 107 */   public static final ResourceKey<LootTable> TOOLSMITH_GIFT = register("gameplay/hero_of_the_village/toolsmith_gift");
/* 108 */   public static final ResourceKey<LootTable> WEAPONSMITH_GIFT = register("gameplay/hero_of_the_village/weaponsmith_gift");
/* 109 */   public static final ResourceKey<LootTable> UNEMPLOYED_GIFT = register("gameplay/hero_of_the_village/unemployed_gift");
/* 110 */   public static final ResourceKey<LootTable> BABY_VILLAGER_GIFT = register("gameplay/hero_of_the_village/baby_gift");
/*     */ 
/*     */   
/* 113 */   public static final ResourceKey<LootTable> SNIFFER_DIGGING = register("gameplay/sniffer_digging");
/* 114 */   public static final ResourceKey<LootTable> PANDA_SNEEZE = register("gameplay/panda_sneeze");
/* 115 */   public static final ResourceKey<LootTable> CHICKEN_LAY = register("gameplay/chicken_lay");
/* 116 */   public static final ResourceKey<LootTable> ARMADILLO_SHED = register("gameplay/armadillo_shed");
/* 117 */   public static final ResourceKey<LootTable> TURTLE_GROW = register("gameplay/turtle_grow");
/*     */ 
/*     */   
/* 120 */   public static final ResourceKey<LootTable> HARVEST_CAVE_VINE = register("harvest/cave_vine");
/* 121 */   public static final ResourceKey<LootTable> HARVEST_SWEET_BERRY_BUSH = register("harvest/sweet_berry_bush");
/* 122 */   public static final ResourceKey<LootTable> HARVEST_BEEHIVE = register("harvest/beehive");
/* 123 */   public static final ResourceKey<LootTable> CARVE_PUMPKIN = register("carve/pumpkin");
/*     */ 
/*     */   
/* 126 */   public static final ResourceKey<LootTable> PIGLIN_BARTERING = register("gameplay/piglin_bartering");
/*     */ 
/*     */   
/* 129 */   public static final ResourceKey<LootTable> SPAWNER_TRIAL_CHAMBER_KEY = register("spawners/trial_chamber/key");
/* 130 */   public static final ResourceKey<LootTable> SPAWNER_TRIAL_CHAMBER_CONSUMABLES = register("spawners/trial_chamber/consumables");
/* 131 */   public static final ResourceKey<LootTable> SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY = register("spawners/ominous/trial_chamber/key");
/* 132 */   public static final ResourceKey<LootTable> SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES = register("spawners/ominous/trial_chamber/consumables");
/* 133 */   public static final ResourceKey<LootTable> SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS = register("spawners/trial_chamber/items_to_drop_when_ominous");
/*     */ 
/*     */   
/* 136 */   public static final ResourceKey<LootTable> ARMADILLO_BRUSH = register("brush/armadillo");
/*     */ 
/*     */   
/* 139 */   public static final ResourceKey<LootTable> BOGGED_SHEAR = register("shearing/bogged");
/* 140 */   public static final ResourceKey<LootTable> SHEAR_MOOSHROOM = register("shearing/mooshroom");
/* 141 */   public static final ResourceKey<LootTable> SHEAR_RED_MOOSHROOM = register("shearing/mooshroom/red");
/* 142 */   public static final ResourceKey<LootTable> SHEAR_BROWN_MOOSHROOM = register("shearing/mooshroom/brown");
/* 143 */   public static final ResourceKey<LootTable> SHEAR_SNOW_GOLEM = register("shearing/snow_golem");
/* 144 */   public static final ResourceKey<LootTable> SHEAR_SHEEP = register("shearing/sheep");
/* 145 */   public static final Map<DyeColor, ResourceKey<LootTable>> SHEAR_SHEEP_BY_DYE = makeDyeKeyMap("shearing/sheep");
/*     */ 
/*     */   
/* 148 */   public static final ResourceKey<LootTable> CHARGED_CREEPER = register("charged_creeper/root");
/* 149 */   public static final ResourceKey<LootTable> CHARGED_CREEPER_PIGLIN = register("charged_creeper/piglin");
/* 150 */   public static final ResourceKey<LootTable> CHARGED_CREEPER_CREEPER = register("charged_creeper/creeper");
/* 151 */   public static final ResourceKey<LootTable> CHARGED_CREEPER_SKELETON = register("charged_creeper/skeleton");
/* 152 */   public static final ResourceKey<LootTable> CHARGED_CREEPER_WITHER_SKELETON = register("charged_creeper/wither_skeleton");
/* 153 */   public static final ResourceKey<LootTable> CHARGED_CREEPER_ZOMBIE = register("charged_creeper/zombie");
/*     */ 
/*     */   
/* 156 */   public static final ResourceKey<LootTable> DESERT_WELL_ARCHAEOLOGY = register("archaeology/desert_well");
/* 157 */   public static final ResourceKey<LootTable> DESERT_PYRAMID_ARCHAEOLOGY = register("archaeology/desert_pyramid");
/* 158 */   public static final ResourceKey<LootTable> TRAIL_RUINS_ARCHAEOLOGY_COMMON = register("archaeology/trail_ruins_common");
/* 159 */   public static final ResourceKey<LootTable> TRAIL_RUINS_ARCHAEOLOGY_RARE = register("archaeology/trail_ruins_rare");
/* 160 */   public static final ResourceKey<LootTable> OCEAN_RUIN_WARM_ARCHAEOLOGY = register("archaeology/ocean_ruin_warm");
/* 161 */   public static final ResourceKey<LootTable> OCEAN_RUIN_COLD_ARCHAEOLOGY = register("archaeology/ocean_ruin_cold");
/*     */   
/*     */   private static Map<DyeColor, ResourceKey<LootTable>> makeDyeKeyMap(String paramString) {
/* 164 */     return Util.makeEnumMap(DyeColor.class, paramDyeColor -> register(paramString + "/" + paramString));
/*     */   }
/*     */   
/*     */   private static ResourceKey<LootTable> register(String paramString) {
/* 168 */     return register(ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace(paramString)));
/*     */   }
/*     */   
/*     */   private static ResourceKey<LootTable> register(ResourceKey<LootTable> paramResourceKey) {
/* 172 */     if (LOCATIONS.add(paramResourceKey)) {
/* 173 */       return paramResourceKey;
/*     */     }
/*     */     
/* 176 */     throw new IllegalArgumentException(String.valueOf(paramResourceKey.identifier()) + " is already a registered built-in loot table");
/*     */   }
/*     */   
/*     */   public static Set<ResourceKey<LootTable>> all() {
/* 180 */     return IMMUTABLE_LOCATIONS;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\BuiltInLootTables.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */