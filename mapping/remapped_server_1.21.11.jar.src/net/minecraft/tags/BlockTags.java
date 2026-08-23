/*     */ package net.minecraft.tags;
/*     */ 
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BlockTags
/*     */ {
/*  14 */   public static final TagKey<Block> WOOL = create("wool");
/*  15 */   public static final TagKey<Block> PLANKS = create("planks");
/*  16 */   public static final TagKey<Block> STONE_BRICKS = create("stone_bricks");
/*  17 */   public static final TagKey<Block> WOODEN_BUTTONS = create("wooden_buttons");
/*  18 */   public static final TagKey<Block> STONE_BUTTONS = create("stone_buttons");
/*  19 */   public static final TagKey<Block> BUTTONS = create("buttons");
/*  20 */   public static final TagKey<Block> WOOL_CARPETS = create("wool_carpets");
/*  21 */   public static final TagKey<Block> WOODEN_DOORS = create("wooden_doors");
/*  22 */   public static final TagKey<Block> WOODEN_STAIRS = create("wooden_stairs");
/*  23 */   public static final TagKey<Block> WOODEN_SLABS = create("wooden_slabs");
/*  24 */   public static final TagKey<Block> WOODEN_FENCES = create("wooden_fences");
/*  25 */   public static final TagKey<Block> FENCE_GATES = create("fence_gates");
/*  26 */   public static final TagKey<Block> WOODEN_PRESSURE_PLATES = create("wooden_pressure_plates");
/*  27 */   public static final TagKey<Block> WOODEN_SHELVES = create("wooden_shelves");
/*  28 */   public static final TagKey<Block> DOORS = create("doors");
/*  29 */   public static final TagKey<Block> SAPLINGS = create("saplings");
/*  30 */   public static final TagKey<Block> BAMBOO_BLOCKS = create("bamboo_blocks");
/*  31 */   public static final TagKey<Block> OAK_LOGS = create("oak_logs");
/*  32 */   public static final TagKey<Block> DARK_OAK_LOGS = create("dark_oak_logs");
/*  33 */   public static final TagKey<Block> PALE_OAK_LOGS = create("pale_oak_logs");
/*  34 */   public static final TagKey<Block> BIRCH_LOGS = create("birch_logs");
/*  35 */   public static final TagKey<Block> ACACIA_LOGS = create("acacia_logs");
/*  36 */   public static final TagKey<Block> SPRUCE_LOGS = create("spruce_logs");
/*  37 */   public static final TagKey<Block> MANGROVE_LOGS = create("mangrove_logs");
/*  38 */   public static final TagKey<Block> JUNGLE_LOGS = create("jungle_logs");
/*  39 */   public static final TagKey<Block> CHERRY_LOGS = create("cherry_logs");
/*  40 */   public static final TagKey<Block> CRIMSON_STEMS = create("crimson_stems");
/*  41 */   public static final TagKey<Block> WARPED_STEMS = create("warped_stems");
/*  42 */   public static final TagKey<Block> WART_BLOCKS = create("wart_blocks");
/*  43 */   public static final TagKey<Block> LOGS_THAT_BURN = create("logs_that_burn");
/*  44 */   public static final TagKey<Block> LOGS = create("logs");
/*  45 */   public static final TagKey<Block> SAND = create("sand");
/*  46 */   public static final TagKey<Block> SMELTS_TO_GLASS = create("smelts_to_glass");
/*  47 */   public static final TagKey<Block> SLABS = create("slabs");
/*  48 */   public static final TagKey<Block> WALLS = create("walls");
/*  49 */   public static final TagKey<Block> STAIRS = create("stairs");
/*  50 */   public static final TagKey<Block> ANVIL = create("anvil");
/*  51 */   public static final TagKey<Block> RAILS = create("rails");
/*  52 */   public static final TagKey<Block> LEAVES = create("leaves");
/*  53 */   public static final TagKey<Block> WOODEN_TRAPDOORS = create("wooden_trapdoors");
/*  54 */   public static final TagKey<Block> TRAPDOORS = create("trapdoors");
/*  55 */   public static final TagKey<Block> SMALL_FLOWERS = create("small_flowers");
/*  56 */   public static final TagKey<Block> FLOWERS = create("flowers");
/*  57 */   public static final TagKey<Block> BEDS = create("beds");
/*  58 */   public static final TagKey<Block> FENCES = create("fences");
/*  59 */   public static final TagKey<Block> SOUL_FIRE_BASE_BLOCKS = create("soul_fire_base_blocks");
/*  60 */   public static final TagKey<Block> CANDLES = create("candles");
/*  61 */   public static final TagKey<Block> DAMPENS_VIBRATIONS = create("dampens_vibrations");
/*  62 */   public static final TagKey<Block> GOLD_ORES = create("gold_ores");
/*  63 */   public static final TagKey<Block> IRON_ORES = create("iron_ores");
/*  64 */   public static final TagKey<Block> DIAMOND_ORES = create("diamond_ores");
/*  65 */   public static final TagKey<Block> REDSTONE_ORES = create("redstone_ores");
/*  66 */   public static final TagKey<Block> LAPIS_ORES = create("lapis_ores");
/*  67 */   public static final TagKey<Block> COAL_ORES = create("coal_ores");
/*  68 */   public static final TagKey<Block> EMERALD_ORES = create("emerald_ores");
/*  69 */   public static final TagKey<Block> COPPER_ORES = create("copper_ores");
/*  70 */   public static final TagKey<Block> DIRT = create("dirt");
/*  71 */   public static final TagKey<Block> TERRACOTTA = create("terracotta");
/*  72 */   public static final TagKey<Block> COMPLETES_FIND_TREE_TUTORIAL = create("completes_find_tree_tutorial");
/*  73 */   public static final TagKey<Block> SHULKER_BOXES = create("shulker_boxes");
/*  74 */   public static final TagKey<Block> COPPER_CHESTS = create("copper_chests");
/*  75 */   public static final TagKey<Block> LIGHTNING_RODS = create("lightning_rods");
/*  76 */   public static final TagKey<Block> COPPER = create("copper");
/*  77 */   public static final TagKey<Block> CHAINS = create("chains");
/*  78 */   public static final TagKey<Block> COPPER_GOLEM_STATUES = create("copper_golem_statues");
/*  79 */   public static final TagKey<Block> LANTERNS = create("lanterns");
/*  80 */   public static final TagKey<Block> BARS = create("bars");
/*     */ 
/*     */   
/*  83 */   public static final TagKey<Block> CEILING_HANGING_SIGNS = create("ceiling_hanging_signs");
/*  84 */   public static final TagKey<Block> STANDING_SIGNS = create("standing_signs");
/*  85 */   public static final TagKey<Block> BEE_ATTRACTIVE = create("bee_attractive");
/*     */ 
/*     */   
/*  88 */   public static final TagKey<Block> MOB_INTERACTABLE_DOORS = create("mob_interactable_doors");
/*  89 */   public static final TagKey<Block> PRESSURE_PLATES = create("pressure_plates");
/*  90 */   public static final TagKey<Block> STONE_PRESSURE_PLATES = create("stone_pressure_plates");
/*  91 */   public static final TagKey<Block> OVERWORLD_NATURAL_LOGS = create("overworld_natural_logs");
/*  92 */   public static final TagKey<Block> BANNERS = create("banners");
/*     */   
/*  94 */   public static final TagKey<Block> PIGLIN_REPELLENTS = create("piglin_repellents");
/*  95 */   public static final TagKey<Block> BADLANDS_TERRACOTTA = create("badlands_terracotta");
/*  96 */   public static final TagKey<Block> CONCRETE_POWDER = create("concrete_powder");
/*     */   
/*  98 */   public static final TagKey<Block> FLOWER_POTS = create("flower_pots");
/*  99 */   public static final TagKey<Block> ENDERMAN_HOLDABLE = create("enderman_holdable");
/* 100 */   public static final TagKey<Block> ICE = create("ice");
/* 101 */   public static final TagKey<Block> VALID_SPAWN = create("valid_spawn");
/* 102 */   public static final TagKey<Block> IMPERMEABLE = create("impermeable");
/* 103 */   public static final TagKey<Block> UNDERWATER_BONEMEALS = create("underwater_bonemeals");
/* 104 */   public static final TagKey<Block> CORAL_BLOCKS = create("coral_blocks");
/* 105 */   public static final TagKey<Block> WALL_CORALS = create("wall_corals");
/* 106 */   public static final TagKey<Block> CORAL_PLANTS = create("coral_plants");
/* 107 */   public static final TagKey<Block> CORALS = create("corals");
/* 108 */   public static final TagKey<Block> BAMBOO_PLANTABLE_ON = create("bamboo_plantable_on");
/* 109 */   public static final TagKey<Block> WALL_SIGNS = create("wall_signs");
/* 110 */   public static final TagKey<Block> SIGNS = create("signs");
/* 111 */   public static final TagKey<Block> WALL_HANGING_SIGNS = create("wall_hanging_signs");
/* 112 */   public static final TagKey<Block> ALL_HANGING_SIGNS = create("all_hanging_signs");
/* 113 */   public static final TagKey<Block> ALL_SIGNS = create("all_signs");
/* 114 */   public static final TagKey<Block> DRAGON_IMMUNE = create("dragon_immune");
/* 115 */   public static final TagKey<Block> DRAGON_TRANSPARENT = create("dragon_transparent");
/* 116 */   public static final TagKey<Block> WITHER_IMMUNE = create("wither_immune");
/* 117 */   public static final TagKey<Block> WITHER_SUMMON_BASE_BLOCKS = create("wither_summon_base_blocks");
/* 118 */   public static final TagKey<Block> BEEHIVES = create("beehives");
/* 119 */   public static final TagKey<Block> CROPS = create("crops");
/* 120 */   public static final TagKey<Block> BEE_GROWABLES = create("bee_growables");
/* 121 */   public static final TagKey<Block> PORTALS = create("portals");
/* 122 */   public static final TagKey<Block> FIRE = create("fire");
/* 123 */   public static final TagKey<Block> NYLIUM = create("nylium");
/* 124 */   public static final TagKey<Block> BEACON_BASE_BLOCKS = create("beacon_base_blocks");
/* 125 */   public static final TagKey<Block> SOUL_SPEED_BLOCKS = create("soul_speed_blocks");
/* 126 */   public static final TagKey<Block> WALL_POST_OVERRIDE = create("wall_post_override");
/* 127 */   public static final TagKey<Block> CLIMBABLE = create("climbable");
/* 128 */   public static final TagKey<Block> FALL_DAMAGE_RESETTING = create("fall_damage_resetting");
/* 129 */   public static final TagKey<Block> HOGLIN_REPELLENTS = create("hoglin_repellents");
/* 130 */   public static final TagKey<Block> STRIDER_WARM_BLOCKS = create("strider_warm_blocks");
/* 131 */   public static final TagKey<Block> CAMPFIRES = create("campfires");
/* 132 */   public static final TagKey<Block> GUARDED_BY_PIGLINS = create("guarded_by_piglins");
/* 133 */   public static final TagKey<Block> PREVENT_MOB_SPAWNING_INSIDE = create("prevent_mob_spawning_inside");
/* 134 */   public static final TagKey<Block> UNSTABLE_BOTTOM_CENTER = create("unstable_bottom_center");
/* 135 */   public static final TagKey<Block> MUSHROOM_GROW_BLOCK = create("mushroom_grow_block");
/* 136 */   public static final TagKey<Block> EDIBLE_FOR_SHEEP = create("edible_for_sheep");
/* 137 */   public static final TagKey<Block> CAN_GLIDE_THROUGH = create("can_glide_through");
/*     */   
/* 139 */   public static final TagKey<Block> INFINIBURN_OVERWORLD = create("infiniburn_overworld");
/* 140 */   public static final TagKey<Block> INFINIBURN_NETHER = create("infiniburn_nether");
/* 141 */   public static final TagKey<Block> INFINIBURN_END = create("infiniburn_end");
/*     */   
/* 143 */   public static final TagKey<Block> BASE_STONE_OVERWORLD = create("base_stone_overworld");
/* 144 */   public static final TagKey<Block> STONE_ORE_REPLACEABLES = create("stone_ore_replaceables");
/* 145 */   public static final TagKey<Block> DEEPSLATE_ORE_REPLACEABLES = create("deepslate_ore_replaceables");
/* 146 */   public static final TagKey<Block> BASE_STONE_NETHER = create("base_stone_nether");
/* 147 */   public static final TagKey<Block> OVERWORLD_CARVER_REPLACEABLES = create("overworld_carver_replaceables");
/* 148 */   public static final TagKey<Block> NETHER_CARVER_REPLACEABLES = create("nether_carver_replaceables");
/*     */   
/* 150 */   public static final TagKey<Block> CANDLE_CAKES = create("candle_cakes");
/*     */   
/* 152 */   public static final TagKey<Block> CAULDRONS = create("cauldrons");
/* 153 */   public static final TagKey<Block> CRYSTAL_SOUND_BLOCKS = create("crystal_sound_blocks");
/* 154 */   public static final TagKey<Block> INSIDE_STEP_SOUND_BLOCKS = create("inside_step_sound_blocks");
/* 155 */   public static final TagKey<Block> COMBINATION_STEP_SOUND_BLOCKS = create("combination_step_sound_blocks");
/* 156 */   public static final TagKey<Block> CAMEL_SAND_STEP_SOUND_BLOCKS = create("camel_sand_step_sound_blocks");
/* 157 */   public static final TagKey<Block> HAPPY_GHAST_AVOIDS = create("happy_ghast_avoids");
/* 158 */   public static final TagKey<Block> OCCLUDES_VIBRATION_SIGNALS = create("occludes_vibration_signals");
/*     */   
/* 160 */   public static final TagKey<Block> DRIPSTONE_REPLACEABLE = create("dripstone_replaceable_blocks");
/* 161 */   public static final TagKey<Block> CAVE_VINES = create("cave_vines");
/* 162 */   public static final TagKey<Block> MOSS_REPLACEABLE = create("moss_replaceable");
/* 163 */   public static final TagKey<Block> LUSH_GROUND_REPLACEABLE = create("lush_ground_replaceable");
/* 164 */   public static final TagKey<Block> AZALEA_ROOT_REPLACEABLE = create("azalea_root_replaceable");
/* 165 */   public static final TagKey<Block> SMALL_DRIPLEAF_PLACEABLE = create("small_dripleaf_placeable");
/* 166 */   public static final TagKey<Block> BIG_DRIPLEAF_PLACEABLE = create("big_dripleaf_placeable");
/*     */   
/* 168 */   public static final TagKey<Block> SNOW = create("snow");
/*     */   
/* 170 */   public static final TagKey<Block> MINEABLE_WITH_AXE = create("mineable/axe");
/* 171 */   public static final TagKey<Block> MINEABLE_WITH_HOE = create("mineable/hoe");
/* 172 */   public static final TagKey<Block> MINEABLE_WITH_PICKAXE = create("mineable/pickaxe");
/* 173 */   public static final TagKey<Block> MINEABLE_WITH_SHOVEL = create("mineable/shovel");
/* 174 */   public static final TagKey<Block> SWORD_EFFICIENT = create("sword_efficient");
/* 175 */   public static final TagKey<Block> SWORD_INSTANTLY_MINES = create("sword_instantly_mines");
/*     */   
/* 177 */   public static final TagKey<Block> NEEDS_DIAMOND_TOOL = create("needs_diamond_tool");
/* 178 */   public static final TagKey<Block> NEEDS_IRON_TOOL = create("needs_iron_tool");
/* 179 */   public static final TagKey<Block> NEEDS_STONE_TOOL = create("needs_stone_tool");
/*     */   
/* 181 */   public static final TagKey<Block> INCORRECT_FOR_NETHERITE_TOOL = create("incorrect_for_netherite_tool");
/* 182 */   public static final TagKey<Block> INCORRECT_FOR_DIAMOND_TOOL = create("incorrect_for_diamond_tool");
/* 183 */   public static final TagKey<Block> INCORRECT_FOR_IRON_TOOL = create("incorrect_for_iron_tool");
/* 184 */   public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = create("incorrect_for_copper_tool");
/* 185 */   public static final TagKey<Block> INCORRECT_FOR_STONE_TOOL = create("incorrect_for_stone_tool");
/* 186 */   public static final TagKey<Block> INCORRECT_FOR_GOLD_TOOL = create("incorrect_for_gold_tool");
/* 187 */   public static final TagKey<Block> INCORRECT_FOR_WOODEN_TOOL = create("incorrect_for_wooden_tool");
/*     */   
/* 189 */   public static final TagKey<Block> FEATURES_CANNOT_REPLACE = create("features_cannot_replace");
/* 190 */   public static final TagKey<Block> LAVA_POOL_STONE_CANNOT_REPLACE = create("lava_pool_stone_cannot_replace");
/* 191 */   public static final TagKey<Block> GEODE_INVALID_BLOCKS = create("geode_invalid_blocks");
/* 192 */   public static final TagKey<Block> FROG_PREFER_JUMP_TO = create("frog_prefer_jump_to");
/* 193 */   public static final TagKey<Block> SCULK_REPLACEABLE = create("sculk_replaceable");
/* 194 */   public static final TagKey<Block> SCULK_REPLACEABLE_WORLD_GEN = create("sculk_replaceable_world_gen");
/* 195 */   public static final TagKey<Block> ANCIENT_CITY_REPLACEABLE = create("ancient_city_replaceable");
/* 196 */   public static final TagKey<Block> VIBRATION_RESONATORS = create("vibration_resonators");
/*     */   
/* 198 */   public static final TagKey<Block> ANIMALS_SPAWNABLE_ON = create("animals_spawnable_on");
/* 199 */   public static final TagKey<Block> ARMADILLO_SPAWNABLE_ON = create("armadillo_spawnable_on");
/* 200 */   public static final TagKey<Block> AXOLOTLS_SPAWNABLE_ON = create("axolotls_spawnable_on");
/* 201 */   public static final TagKey<Block> GOATS_SPAWNABLE_ON = create("goats_spawnable_on");
/* 202 */   public static final TagKey<Block> MOOSHROOMS_SPAWNABLE_ON = create("mooshrooms_spawnable_on");
/* 203 */   public static final TagKey<Block> PARROTS_SPAWNABLE_ON = create("parrots_spawnable_on");
/* 204 */   public static final TagKey<Block> POLAR_BEARS_SPAWNABLE_ON_ALTERNATE = create("polar_bears_spawnable_on_alternate");
/* 205 */   public static final TagKey<Block> RABBITS_SPAWNABLE_ON = create("rabbits_spawnable_on");
/* 206 */   public static final TagKey<Block> FOXES_SPAWNABLE_ON = create("foxes_spawnable_on");
/* 207 */   public static final TagKey<Block> WOLVES_SPAWNABLE_ON = create("wolves_spawnable_on");
/* 208 */   public static final TagKey<Block> FROGS_SPAWNABLE_ON = create("frogs_spawnable_on");
/* 209 */   public static final TagKey<Block> BATS_SPAWNABLE_ON = create("bats_spawnable_on");
/* 210 */   public static final TagKey<Block> CAMELS_SPAWNABLE_ON = create("camels_spawnable_on");
/*     */   
/* 212 */   public static final TagKey<Block> AZALEA_GROWS_ON = create("azalea_grows_on");
/* 213 */   public static final TagKey<Block> CONVERTABLE_TO_MUD = create("convertable_to_mud");
/* 214 */   public static final TagKey<Block> MANGROVE_LOGS_CAN_GROW_THROUGH = create("mangrove_logs_can_grow_through");
/* 215 */   public static final TagKey<Block> MANGROVE_ROOTS_CAN_GROW_THROUGH = create("mangrove_roots_can_grow_through");
/* 216 */   public static final TagKey<Block> DRY_VEGETATION_MAY_PLACE_ON = create("dry_vegetation_may_place_on");
/* 217 */   public static final TagKey<Block> SNAPS_GOAT_HORN = create("snaps_goat_horn");
/* 218 */   public static final TagKey<Block> REPLACEABLE_BY_TREES = create("replaceable_by_trees");
/* 219 */   public static final TagKey<Block> REPLACEABLE_BY_MUSHROOMS = create("replaceable_by_mushrooms");
/*     */   
/* 221 */   public static final TagKey<Block> SNOW_LAYER_CANNOT_SURVIVE_ON = create("snow_layer_cannot_survive_on");
/* 222 */   public static final TagKey<Block> SNOW_LAYER_CAN_SURVIVE_ON = create("snow_layer_can_survive_on");
/* 223 */   public static final TagKey<Block> INVALID_SPAWN_INSIDE = create("invalid_spawn_inside");
/*     */   
/* 225 */   public static final TagKey<Block> SNIFFER_DIGGABLE_BLOCK = create("sniffer_diggable_block");
/* 226 */   public static final TagKey<Block> SNIFFER_EGG_HATCH_BOOST = create("sniffer_egg_hatch_boost");
/* 227 */   public static final TagKey<Block> TRAIL_RUINS_REPLACEABLE = create("trail_ruins_replaceable");
/*     */   
/* 229 */   public static final TagKey<Block> REPLACEABLE = create("replaceable");
/* 230 */   public static final TagKey<Block> ENCHANTMENT_POWER_PROVIDER = create("enchantment_power_provider");
/* 231 */   public static final TagKey<Block> ENCHANTMENT_POWER_TRANSMITTER = create("enchantment_power_transmitter");
/*     */   
/* 233 */   public static final TagKey<Block> MAINTAINS_FARMLAND = create("maintains_farmland");
/* 234 */   public static final TagKey<Block> BLOCKS_WIND_CHARGE_EXPLOSIONS = create("blocks_wind_charge_explosions");
/*     */   
/* 236 */   public static final TagKey<Block> DOES_NOT_BLOCK_HOPPERS = create("does_not_block_hoppers");
/* 237 */   public static final TagKey<Block> TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS = create("triggers_ambient_desert_sand_block_sounds");
/* 238 */   public static final TagKey<Block> TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS = create("triggers_ambient_desert_dry_vegetation_block_sounds");
/*     */   
/* 240 */   public static final TagKey<Block> TRIGGERS_AMBIENT_DRIED_GHAST_BLOCK_SOUNDS = create("triggers_ambient_dried_ghast_block_sounds");
/*     */   
/* 242 */   public static final TagKey<Block> AIR = create("air");
/*     */   
/*     */   private static TagKey<Block> create(String paramString) {
/* 245 */     return TagKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\BlockTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */