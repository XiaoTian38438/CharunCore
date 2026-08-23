/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BiomeTags
/*    */ {
/* 13 */   public static final TagKey<Biome> IS_DEEP_OCEAN = create("is_deep_ocean");
/* 14 */   public static final TagKey<Biome> IS_OCEAN = create("is_ocean");
/* 15 */   public static final TagKey<Biome> IS_BEACH = create("is_beach");
/* 16 */   public static final TagKey<Biome> IS_RIVER = create("is_river");
/* 17 */   public static final TagKey<Biome> IS_MOUNTAIN = create("is_mountain");
/* 18 */   public static final TagKey<Biome> IS_BADLANDS = create("is_badlands");
/* 19 */   public static final TagKey<Biome> IS_HILL = create("is_hill");
/* 20 */   public static final TagKey<Biome> IS_TAIGA = create("is_taiga");
/* 21 */   public static final TagKey<Biome> IS_JUNGLE = create("is_jungle");
/* 22 */   public static final TagKey<Biome> IS_FOREST = create("is_forest");
/* 23 */   public static final TagKey<Biome> IS_SAVANNA = create("is_savanna");
/* 24 */   public static final TagKey<Biome> IS_OVERWORLD = create("is_overworld");
/* 25 */   public static final TagKey<Biome> IS_NETHER = create("is_nether");
/* 26 */   public static final TagKey<Biome> IS_END = create("is_end");
/*    */ 
/*    */   
/* 29 */   public static final TagKey<Biome> STRONGHOLD_BIASED_TO = create("stronghold_biased_to");
/* 30 */   public static final TagKey<Biome> HAS_BURIED_TREASURE = create("has_structure/buried_treasure");
/* 31 */   public static final TagKey<Biome> HAS_DESERT_PYRAMID = create("has_structure/desert_pyramid");
/* 32 */   public static final TagKey<Biome> HAS_IGLOO = create("has_structure/igloo");
/* 33 */   public static final TagKey<Biome> HAS_JUNGLE_TEMPLE = create("has_structure/jungle_temple");
/* 34 */   public static final TagKey<Biome> HAS_MINESHAFT = create("has_structure/mineshaft");
/* 35 */   public static final TagKey<Biome> HAS_MINESHAFT_MESA = create("has_structure/mineshaft_mesa");
/* 36 */   public static final TagKey<Biome> HAS_OCEAN_MONUMENT = create("has_structure/ocean_monument");
/* 37 */   public static final TagKey<Biome> HAS_OCEAN_RUIN_COLD = create("has_structure/ocean_ruin_cold");
/* 38 */   public static final TagKey<Biome> HAS_OCEAN_RUIN_WARM = create("has_structure/ocean_ruin_warm");
/* 39 */   public static final TagKey<Biome> HAS_PILLAGER_OUTPOST = create("has_structure/pillager_outpost");
/* 40 */   public static final TagKey<Biome> HAS_RUINED_PORTAL_DESERT = create("has_structure/ruined_portal_desert");
/* 41 */   public static final TagKey<Biome> HAS_RUINED_PORTAL_JUNGLE = create("has_structure/ruined_portal_jungle");
/* 42 */   public static final TagKey<Biome> HAS_RUINED_PORTAL_OCEAN = create("has_structure/ruined_portal_ocean");
/* 43 */   public static final TagKey<Biome> HAS_RUINED_PORTAL_SWAMP = create("has_structure/ruined_portal_swamp");
/* 44 */   public static final TagKey<Biome> HAS_RUINED_PORTAL_MOUNTAIN = create("has_structure/ruined_portal_mountain");
/* 45 */   public static final TagKey<Biome> HAS_RUINED_PORTAL_STANDARD = create("has_structure/ruined_portal_standard");
/* 46 */   public static final TagKey<Biome> HAS_SHIPWRECK_BEACHED = create("has_structure/shipwreck_beached");
/* 47 */   public static final TagKey<Biome> HAS_SHIPWRECK = create("has_structure/shipwreck");
/* 48 */   public static final TagKey<Biome> HAS_STRONGHOLD = create("has_structure/stronghold");
/* 49 */   public static final TagKey<Biome> HAS_TRIAL_CHAMBERS = create("has_structure/trial_chambers");
/* 50 */   public static final TagKey<Biome> HAS_SWAMP_HUT = create("has_structure/swamp_hut");
/* 51 */   public static final TagKey<Biome> HAS_VILLAGE_DESERT = create("has_structure/village_desert");
/* 52 */   public static final TagKey<Biome> HAS_VILLAGE_PLAINS = create("has_structure/village_plains");
/* 53 */   public static final TagKey<Biome> HAS_VILLAGE_SAVANNA = create("has_structure/village_savanna");
/* 54 */   public static final TagKey<Biome> HAS_VILLAGE_SNOWY = create("has_structure/village_snowy");
/* 55 */   public static final TagKey<Biome> HAS_VILLAGE_TAIGA = create("has_structure/village_taiga");
/* 56 */   public static final TagKey<Biome> HAS_TRAIL_RUINS = create("has_structure/trail_ruins");
/* 57 */   public static final TagKey<Biome> HAS_WOODLAND_MANSION = create("has_structure/woodland_mansion");
/* 58 */   public static final TagKey<Biome> HAS_NETHER_FORTRESS = create("has_structure/nether_fortress");
/* 59 */   public static final TagKey<Biome> HAS_NETHER_FOSSIL = create("has_structure/nether_fossil");
/* 60 */   public static final TagKey<Biome> HAS_BASTION_REMNANT = create("has_structure/bastion_remnant");
/* 61 */   public static final TagKey<Biome> HAS_ANCIENT_CITY = create("has_structure/ancient_city");
/* 62 */   public static final TagKey<Biome> HAS_RUINED_PORTAL_NETHER = create("has_structure/ruined_portal_nether");
/* 63 */   public static final TagKey<Biome> HAS_END_CITY = create("has_structure/end_city");
/* 64 */   public static final TagKey<Biome> REQUIRED_OCEAN_MONUMENT_SURROUNDING = create("required_ocean_monument_surrounding");
/* 65 */   public static final TagKey<Biome> MINESHAFT_BLOCKING = create("mineshaft_blocking");
/*    */ 
/*    */   
/* 68 */   public static final TagKey<Biome> WATER_ON_MAP_OUTLINES = create("water_on_map_outlines");
/* 69 */   public static final TagKey<Biome> PRODUCES_CORALS_FROM_BONEMEAL = create("produces_corals_from_bonemeal");
/*    */ 
/*    */ 
/*    */   
/* 73 */   public static final TagKey<Biome> WITHOUT_ZOMBIE_SIEGES = create("without_zombie_sieges");
/* 74 */   public static final TagKey<Biome> WITHOUT_WANDERING_TRADER_SPAWNS = create("without_wandering_trader_spawns");
/* 75 */   public static final TagKey<Biome> SPAWNS_COLD_VARIANT_FROGS = create("spawns_cold_variant_frogs");
/* 76 */   public static final TagKey<Biome> SPAWNS_WARM_VARIANT_FROGS = create("spawns_warm_variant_frogs");
/* 77 */   public static final TagKey<Biome> SPAWNS_COLD_VARIANT_FARM_ANIMALS = create("spawns_cold_variant_farm_animals");
/* 78 */   public static final TagKey<Biome> SPAWNS_WARM_VARIANT_FARM_ANIMALS = create("spawns_warm_variant_farm_animals");
/* 79 */   public static final TagKey<Biome> SPAWNS_GOLD_RABBITS = create("spawns_gold_rabbits");
/* 80 */   public static final TagKey<Biome> SPAWNS_WHITE_RABBITS = create("spawns_white_rabbits");
/* 81 */   public static final TagKey<Biome> REDUCED_WATER_AMBIENT_SPAWNS = create("reduce_water_ambient_spawns");
/* 82 */   public static final TagKey<Biome> ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT = create("allows_tropical_fish_spawns_at_any_height");
/* 83 */   public static final TagKey<Biome> POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS = create("polar_bears_spawn_on_alternate_blocks");
/* 84 */   public static final TagKey<Biome> MORE_FREQUENT_DROWNED_SPAWNS = create("more_frequent_drowned_spawns");
/* 85 */   public static final TagKey<Biome> ALLOWS_SURFACE_SLIME_SPAWNS = create("allows_surface_slime_spawns");
/* 86 */   public static final TagKey<Biome> SPAWNS_SNOW_FOXES = create("spawns_snow_foxes");
/* 87 */   public static final TagKey<Biome> SPAWNS_CORAL_VARIANT_ZOMBIE_NAUTILUS = create("spawns_coral_variant_zombie_nautilus");
/*    */   
/*    */   private static TagKey<Biome> create(String paramString) {
/* 90 */     return TagKey.create(Registries.BIOME, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\BiomeTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */