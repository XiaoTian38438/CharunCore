/*     */ package net.minecraft.core.registries;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.CriterionTrigger;
/*     */ import net.minecraft.advancements.criterion.EntitySubPredicate;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.particles.ParticleType;
/*     */ import net.minecraft.gametest.framework.GameTestHelper;
/*     */ import net.minecraft.gametest.framework.GameTestInstance;
/*     */ import net.minecraft.gametest.framework.TestEnvironmentDefinition;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.network.chat.numbers.NumberFormatType;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ import net.minecraft.server.dialog.body.DialogBody;
/*     */ import net.minecraft.server.dialog.input.InputControl;
/*     */ import net.minecraft.server.jsonrpc.IncomingRpcMethod;
/*     */ import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.server.permissions.Permission;
/*     */ import net.minecraft.server.permissions.PermissionCheck;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.util.debug.DebugSubscription;
/*     */ import net.minecraft.util.valueproviders.FloatProviderType;
/*     */ import net.minecraft.util.valueproviders.IntProviderType;
/*     */ import net.minecraft.world.attribute.AttributeType;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowVariant;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
/*     */ import net.minecraft.world.entity.animal.pig.PigVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.entity.variant.SpawnCondition;
/*     */ import net.minecraft.world.inventory.MenuType;
/*     */ import net.minecraft.world.item.CreativeModeTab;
/*     */ import net.minecraft.world.item.Instrument;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.JukeboxSong;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.consume_effects.ConsumeEffect;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.item.slot.SlotSource;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPattern;
/*     */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.PositionSourceType;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.SurfaceRules;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.carver.WorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
/*     */ import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ public class Registries
/*     */ {
/* 142 */   public static final Identifier ROOT_REGISTRY_NAME = Identifier.withDefaultNamespace("root");
/*     */ 
/*     */   
/* 145 */   public static final ResourceKey<Registry<Activity>> ACTIVITY = createRegistryKey("activity");
/* 146 */   public static final ResourceKey<Registry<Attribute>> ATTRIBUTE = createRegistryKey("attribute");
/* 147 */   public static final ResourceKey<Registry<MapCodec<? extends BiomeSource>>> BIOME_SOURCE = createRegistryKey("worldgen/biome_source");
/* 148 */   public static final ResourceKey<Registry<BlockEntityType<?>>> BLOCK_ENTITY_TYPE = createRegistryKey("block_entity_type");
/* 149 */   public static final ResourceKey<Registry<BlockPredicateType<?>>> BLOCK_PREDICATE_TYPE = createRegistryKey("block_predicate_type");
/* 150 */   public static final ResourceKey<Registry<BlockStateProviderType<?>>> BLOCK_STATE_PROVIDER_TYPE = createRegistryKey("worldgen/block_state_provider_type");
/* 151 */   public static final ResourceKey<Registry<MapCodec<? extends Block>>> BLOCK_TYPE = createRegistryKey("block_type");
/* 152 */   public static final ResourceKey<Registry<Block>> BLOCK = createRegistryKey("block");
/* 153 */   public static final ResourceKey<Registry<WorldCarver<?>>> CARVER = createRegistryKey("worldgen/carver");
/* 154 */   public static final ResourceKey<Registry<MapCodec<? extends ChunkGenerator>>> CHUNK_GENERATOR = createRegistryKey("worldgen/chunk_generator");
/* 155 */   public static final ResourceKey<Registry<ChunkStatus>> CHUNK_STATUS = createRegistryKey("chunk_status");
/* 156 */   public static final ResourceKey<Registry<ArgumentTypeInfo<?, ?>>> COMMAND_ARGUMENT_TYPE = createRegistryKey("command_argument_type");
/* 157 */   public static final ResourceKey<Registry<ConsumeEffect.Type<?>>> CONSUME_EFFECT_TYPE = createRegistryKey("consume_effect_type");
/* 158 */   public static final ResourceKey<Registry<CreativeModeTab>> CREATIVE_MODE_TAB = createRegistryKey("creative_mode_tab");
/* 159 */   public static final ResourceKey<Registry<Identifier>> CUSTOM_STAT = createRegistryKey("custom_stat");
/* 160 */   public static final ResourceKey<Registry<DataComponentPredicate.Type<?>>> DATA_COMPONENT_PREDICATE_TYPE = createRegistryKey("data_component_predicate_type");
/* 161 */   public static final ResourceKey<Registry<DataComponentType<?>>> DATA_COMPONENT_TYPE = createRegistryKey("data_component_type");
/* 162 */   public static final ResourceKey<Registry<GameRule<?>>> GAME_RULE = createRegistryKey("game_rule");
/* 163 */   public static final ResourceKey<Registry<DebugSubscription<?>>> DEBUG_SUBSCRIPTION = createRegistryKey("debug_subscription");
/* 164 */   public static final ResourceKey<Registry<DecoratedPotPattern>> DECORATED_POT_PATTERN = createRegistryKey("decorated_pot_pattern");
/* 165 */   public static final ResourceKey<Registry<MapCodec<? extends DensityFunction>>> DENSITY_FUNCTION_TYPE = createRegistryKey("worldgen/density_function_type");
/* 166 */   public static final ResourceKey<Registry<MapCodec<? extends DialogBody>>> DIALOG_BODY_TYPE = createRegistryKey("dialog_body_type");
/* 167 */   public static final ResourceKey<Registry<MapCodec<? extends Dialog>>> DIALOG_TYPE = createRegistryKey("dialog_type");
/* 168 */   public static final ResourceKey<Registry<DataComponentType<?>>> ENCHANTMENT_EFFECT_COMPONENT_TYPE = createRegistryKey("enchantment_effect_component_type");
/* 169 */   public static final ResourceKey<Registry<MapCodec<? extends EnchantmentEntityEffect>>> ENCHANTMENT_ENTITY_EFFECT_TYPE = createRegistryKey("enchantment_entity_effect_type");
/* 170 */   public static final ResourceKey<Registry<MapCodec<? extends LevelBasedValue>>> ENCHANTMENT_LEVEL_BASED_VALUE_TYPE = createRegistryKey("enchantment_level_based_value_type");
/* 171 */   public static final ResourceKey<Registry<MapCodec<? extends EnchantmentLocationBasedEffect>>> ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE = createRegistryKey("enchantment_location_based_effect_type");
/* 172 */   public static final ResourceKey<Registry<MapCodec<? extends EnchantmentProvider>>> ENCHANTMENT_PROVIDER_TYPE = createRegistryKey("enchantment_provider_type");
/* 173 */   public static final ResourceKey<Registry<MapCodec<? extends EnchantmentValueEffect>>> ENCHANTMENT_VALUE_EFFECT_TYPE = createRegistryKey("enchantment_value_effect_type");
/* 174 */   public static final ResourceKey<Registry<MapCodec<? extends EntitySubPredicate>>> ENTITY_SUB_PREDICATE_TYPE = createRegistryKey("entity_sub_predicate_type");
/* 175 */   public static final ResourceKey<Registry<EntityType<?>>> ENTITY_TYPE = createRegistryKey("entity_type");
/* 176 */   public static final ResourceKey<Registry<EnvironmentAttribute<?>>> ENVIRONMENT_ATTRIBUTE = createRegistryKey("environment_attribute");
/* 177 */   public static final ResourceKey<Registry<AttributeType<?>>> ATTRIBUTE_TYPE = createRegistryKey("attribute_type");
/* 178 */   public static final ResourceKey<Registry<FeatureSizeType<?>>> FEATURE_SIZE_TYPE = createRegistryKey("worldgen/feature_size_type");
/* 179 */   public static final ResourceKey<Registry<Feature<?>>> FEATURE = createRegistryKey("worldgen/feature");
/* 180 */   public static final ResourceKey<Registry<FloatProviderType<?>>> FLOAT_PROVIDER_TYPE = createRegistryKey("float_provider_type");
/* 181 */   public static final ResourceKey<Registry<Fluid>> FLUID = createRegistryKey("fluid");
/* 182 */   public static final ResourceKey<Registry<FoliagePlacerType<?>>> FOLIAGE_PLACER_TYPE = createRegistryKey("worldgen/foliage_placer_type");
/* 183 */   public static final ResourceKey<Registry<GameEvent>> GAME_EVENT = createRegistryKey("game_event");
/* 184 */   public static final ResourceKey<Registry<HeightProviderType<?>>> HEIGHT_PROVIDER_TYPE = createRegistryKey("height_provider_type");
/* 185 */   public static final ResourceKey<Registry<MapCodec<? extends InputControl>>> INPUT_CONTROL_TYPE = createRegistryKey("input_control_type");
/* 186 */   public static final ResourceKey<Registry<IntProviderType<?>>> INT_PROVIDER_TYPE = createRegistryKey("int_provider_type");
/* 187 */   public static final ResourceKey<Registry<Item>> ITEM = createRegistryKey("item");
/* 188 */   public static final ResourceKey<Registry<MapCodec<? extends SlotSource>>> SLOT_SOURCE_TYPE = createRegistryKey("slot_source_type");
/* 189 */   public static final ResourceKey<Registry<LootItemConditionType>> LOOT_CONDITION_TYPE = createRegistryKey("loot_condition_type");
/* 190 */   public static final ResourceKey<Registry<LootItemFunctionType<?>>> LOOT_FUNCTION_TYPE = createRegistryKey("loot_function_type");
/* 191 */   public static final ResourceKey<Registry<LootNbtProviderType>> LOOT_NBT_PROVIDER_TYPE = createRegistryKey("loot_nbt_provider_type");
/* 192 */   public static final ResourceKey<Registry<LootNumberProviderType>> LOOT_NUMBER_PROVIDER_TYPE = createRegistryKey("loot_number_provider_type");
/* 193 */   public static final ResourceKey<Registry<LootPoolEntryType>> LOOT_POOL_ENTRY_TYPE = createRegistryKey("loot_pool_entry_type");
/* 194 */   public static final ResourceKey<Registry<LootScoreProviderType>> LOOT_SCORE_PROVIDER_TYPE = createRegistryKey("loot_score_provider_type");
/* 195 */   public static final ResourceKey<Registry<MapDecorationType>> MAP_DECORATION_TYPE = createRegistryKey("map_decoration_type");
/* 196 */   public static final ResourceKey<Registry<MapCodec<? extends SurfaceRules.ConditionSource>>> MATERIAL_CONDITION = createRegistryKey("worldgen/material_condition");
/* 197 */   public static final ResourceKey<Registry<MapCodec<? extends SurfaceRules.RuleSource>>> MATERIAL_RULE = createRegistryKey("worldgen/material_rule");
/* 198 */   public static final ResourceKey<Registry<MemoryModuleType<?>>> MEMORY_MODULE_TYPE = createRegistryKey("memory_module_type");
/* 199 */   public static final ResourceKey<Registry<MenuType<?>>> MENU = createRegistryKey("menu");
/* 200 */   public static final ResourceKey<Registry<MobEffect>> MOB_EFFECT = createRegistryKey("mob_effect");
/* 201 */   public static final ResourceKey<Registry<NumberFormatType<?>>> NUMBER_FORMAT_TYPE = createRegistryKey("number_format_type");
/* 202 */   public static final ResourceKey<Registry<ParticleType<?>>> PARTICLE_TYPE = createRegistryKey("particle_type");
/* 203 */   public static final ResourceKey<Registry<PlacementModifierType<?>>> PLACEMENT_MODIFIER_TYPE = createRegistryKey("worldgen/placement_modifier_type");
/* 204 */   public static final ResourceKey<Registry<PoiType>> POINT_OF_INTEREST_TYPE = createRegistryKey("point_of_interest_type");
/* 205 */   public static final ResourceKey<Registry<MapCodec<? extends PoolAliasBinding>>> POOL_ALIAS_BINDING = createRegistryKey("worldgen/pool_alias_binding");
/* 206 */   public static final ResourceKey<Registry<PositionSourceType<?>>> POSITION_SOURCE_TYPE = createRegistryKey("position_source_type");
/* 207 */   public static final ResourceKey<Registry<PosRuleTestType<?>>> POS_RULE_TEST = createRegistryKey("pos_rule_test");
/* 208 */   public static final ResourceKey<Registry<Potion>> POTION = createRegistryKey("potion");
/* 209 */   public static final ResourceKey<Registry<RecipeBookCategory>> RECIPE_BOOK_CATEGORY = createRegistryKey("recipe_book_category");
/* 210 */   public static final ResourceKey<Registry<RecipeDisplay.Type<?>>> RECIPE_DISPLAY = createRegistryKey("recipe_display");
/* 211 */   public static final ResourceKey<Registry<RecipeSerializer<?>>> RECIPE_SERIALIZER = createRegistryKey("recipe_serializer");
/* 212 */   public static final ResourceKey<Registry<RecipeType<?>>> RECIPE_TYPE = createRegistryKey("recipe_type");
/* 213 */   public static final ResourceKey<Registry<RootPlacerType<?>>> ROOT_PLACER_TYPE = createRegistryKey("worldgen/root_placer_type");
/* 214 */   public static final ResourceKey<Registry<RuleBlockEntityModifierType<?>>> RULE_BLOCK_ENTITY_MODIFIER = createRegistryKey("rule_block_entity_modifier");
/* 215 */   public static final ResourceKey<Registry<RuleTestType<?>>> RULE_TEST = createRegistryKey("rule_test");
/* 216 */   public static final ResourceKey<Registry<SensorType<?>>> SENSOR_TYPE = createRegistryKey("sensor_type");
/* 217 */   public static final ResourceKey<Registry<SlotDisplay.Type<?>>> SLOT_DISPLAY = createRegistryKey("slot_display");
/* 218 */   public static final ResourceKey<Registry<SoundEvent>> SOUND_EVENT = createRegistryKey("sound_event");
/* 219 */   public static final ResourceKey<Registry<MapCodec<? extends SpawnCondition>>> SPAWN_CONDITION_TYPE = createRegistryKey("spawn_condition_type");
/* 220 */   public static final ResourceKey<Registry<StatType<?>>> STAT_TYPE = createRegistryKey("stat_type");
/* 221 */   public static final ResourceKey<Registry<StructurePieceType>> STRUCTURE_PIECE = createRegistryKey("worldgen/structure_piece");
/* 222 */   public static final ResourceKey<Registry<StructurePlacementType<?>>> STRUCTURE_PLACEMENT = createRegistryKey("worldgen/structure_placement");
/* 223 */   public static final ResourceKey<Registry<StructurePoolElementType<?>>> STRUCTURE_POOL_ELEMENT = createRegistryKey("worldgen/structure_pool_element");
/* 224 */   public static final ResourceKey<Registry<StructureProcessorType<?>>> STRUCTURE_PROCESSOR = createRegistryKey("worldgen/structure_processor");
/* 225 */   public static final ResourceKey<Registry<StructureType<?>>> STRUCTURE_TYPE = createRegistryKey("worldgen/structure_type");
/* 226 */   public static final ResourceKey<Registry<MapCodec<? extends Action>>> DIALOG_ACTION_TYPE = createRegistryKey("dialog_action_type");
/* 227 */   public static final ResourceKey<Registry<MapCodec<? extends TestEnvironmentDefinition>>> TEST_ENVIRONMENT_DEFINITION_TYPE = createRegistryKey("test_environment_definition_type");
/* 228 */   public static final ResourceKey<Registry<Consumer<GameTestHelper>>> TEST_FUNCTION = createRegistryKey("test_function");
/* 229 */   public static final ResourceKey<Registry<MapCodec<? extends GameTestInstance>>> TEST_INSTANCE_TYPE = createRegistryKey("test_instance_type");
/* 230 */   public static final ResourceKey<Registry<TicketType>> TICKET_TYPE = createRegistryKey("ticket_type");
/* 231 */   public static final ResourceKey<Registry<TreeDecoratorType<?>>> TREE_DECORATOR_TYPE = createRegistryKey("worldgen/tree_decorator_type");
/* 232 */   public static final ResourceKey<Registry<TrunkPlacerType<?>>> TRUNK_PLACER_TYPE = createRegistryKey("worldgen/trunk_placer_type");
/* 233 */   public static final ResourceKey<Registry<VillagerProfession>> VILLAGER_PROFESSION = createRegistryKey("villager_profession");
/* 234 */   public static final ResourceKey<Registry<VillagerType>> VILLAGER_TYPE = createRegistryKey("villager_type");
/* 235 */   public static final ResourceKey<Registry<IncomingRpcMethod<?, ?>>> INCOMING_RPC_METHOD = createRegistryKey("incoming_rpc_methods");
/* 236 */   public static final ResourceKey<Registry<OutgoingRpcMethod<?, ?>>> OUTGOING_RPC_METHOD = createRegistryKey("outgoing_rpc_methods");
/* 237 */   public static final ResourceKey<Registry<MapCodec<? extends Permission>>> PERMISSION_TYPE = createRegistryKey("permission_type");
/* 238 */   public static final ResourceKey<Registry<MapCodec<? extends PermissionCheck>>> PERMISSION_CHECK_TYPE = createRegistryKey("permission_check_type");
/*     */ 
/*     */   
/* 241 */   public static final ResourceKey<Registry<BannerPattern>> BANNER_PATTERN = createRegistryKey("banner_pattern");
/* 242 */   public static final ResourceKey<Registry<Biome>> BIOME = createRegistryKey("worldgen/biome");
/* 243 */   public static final ResourceKey<Registry<CatVariant>> CAT_VARIANT = createRegistryKey("cat_variant");
/* 244 */   public static final ResourceKey<Registry<ChatType>> CHAT_TYPE = createRegistryKey("chat_type");
/* 245 */   public static final ResourceKey<Registry<ChickenVariant>> CHICKEN_VARIANT = createRegistryKey("chicken_variant");
/* 246 */   public static final ResourceKey<Registry<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT = createRegistryKey("zombie_nautilus_variant");
/* 247 */   public static final ResourceKey<Registry<ConfiguredWorldCarver<?>>> CONFIGURED_CARVER = createRegistryKey("worldgen/configured_carver");
/* 248 */   public static final ResourceKey<Registry<ConfiguredFeature<?, ?>>> CONFIGURED_FEATURE = createRegistryKey("worldgen/configured_feature");
/* 249 */   public static final ResourceKey<Registry<CowVariant>> COW_VARIANT = createRegistryKey("cow_variant");
/* 250 */   public static final ResourceKey<Registry<DamageType>> DAMAGE_TYPE = createRegistryKey("damage_type");
/* 251 */   public static final ResourceKey<Registry<DensityFunction>> DENSITY_FUNCTION = createRegistryKey("worldgen/density_function");
/* 252 */   public static final ResourceKey<Registry<Dialog>> DIALOG = createRegistryKey("dialog");
/* 253 */   public static final ResourceKey<Registry<DimensionType>> DIMENSION_TYPE = createRegistryKey("dimension_type");
/* 254 */   public static final ResourceKey<Registry<EnchantmentProvider>> ENCHANTMENT_PROVIDER = createRegistryKey("enchantment_provider");
/* 255 */   public static final ResourceKey<Registry<Enchantment>> ENCHANTMENT = createRegistryKey("enchantment");
/* 256 */   public static final ResourceKey<Registry<FlatLevelGeneratorPreset>> FLAT_LEVEL_GENERATOR_PRESET = createRegistryKey("worldgen/flat_level_generator_preset");
/* 257 */   public static final ResourceKey<Registry<FrogVariant>> FROG_VARIANT = createRegistryKey("frog_variant");
/* 258 */   public static final ResourceKey<Registry<Instrument>> INSTRUMENT = createRegistryKey("instrument");
/* 259 */   public static final ResourceKey<Registry<JukeboxSong>> JUKEBOX_SONG = createRegistryKey("jukebox_song");
/* 260 */   public static final ResourceKey<Registry<MultiNoiseBiomeSourceParameterList>> MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST = createRegistryKey("worldgen/multi_noise_biome_source_parameter_list");
/* 261 */   public static final ResourceKey<Registry<NoiseGeneratorSettings>> NOISE_SETTINGS = createRegistryKey("worldgen/noise_settings");
/* 262 */   public static final ResourceKey<Registry<NormalNoise.NoiseParameters>> NOISE = createRegistryKey("worldgen/noise");
/* 263 */   public static final ResourceKey<Registry<PaintingVariant>> PAINTING_VARIANT = createRegistryKey("painting_variant");
/* 264 */   public static final ResourceKey<Registry<PigVariant>> PIG_VARIANT = createRegistryKey("pig_variant");
/* 265 */   public static final ResourceKey<Registry<PlacedFeature>> PLACED_FEATURE = createRegistryKey("worldgen/placed_feature");
/* 266 */   public static final ResourceKey<Registry<StructureProcessorList>> PROCESSOR_LIST = createRegistryKey("worldgen/processor_list");
/* 267 */   public static final ResourceKey<Registry<StructureSet>> STRUCTURE_SET = createRegistryKey("worldgen/structure_set");
/* 268 */   public static final ResourceKey<Registry<Structure>> STRUCTURE = createRegistryKey("worldgen/structure");
/* 269 */   public static final ResourceKey<Registry<StructureTemplatePool>> TEMPLATE_POOL = createRegistryKey("worldgen/template_pool");
/* 270 */   public static final ResourceKey<Registry<TestEnvironmentDefinition>> TEST_ENVIRONMENT = createRegistryKey("test_environment");
/* 271 */   public static final ResourceKey<Registry<GameTestInstance>> TEST_INSTANCE = createRegistryKey("test_instance");
/* 272 */   public static final ResourceKey<Registry<Timeline>> TIMELINE = createRegistryKey("timeline");
/* 273 */   public static final ResourceKey<Registry<TrialSpawnerConfig>> TRIAL_SPAWNER_CONFIG = createRegistryKey("trial_spawner");
/* 274 */   public static final ResourceKey<Registry<CriterionTrigger<?>>> TRIGGER_TYPE = createRegistryKey("trigger_type");
/* 275 */   public static final ResourceKey<Registry<TrimMaterial>> TRIM_MATERIAL = createRegistryKey("trim_material");
/* 276 */   public static final ResourceKey<Registry<TrimPattern>> TRIM_PATTERN = createRegistryKey("trim_pattern");
/* 277 */   public static final ResourceKey<Registry<WolfVariant>> WOLF_VARIANT = createRegistryKey("wolf_variant");
/* 278 */   public static final ResourceKey<Registry<WolfSoundVariant>> WOLF_SOUND_VARIANT = createRegistryKey("wolf_sound_variant");
/* 279 */   public static final ResourceKey<Registry<WorldPreset>> WORLD_PRESET = createRegistryKey("worldgen/world_preset");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 285 */   public static final ResourceKey<Registry<Level>> DIMENSION = createRegistryKey("dimension");
/*     */ 
/*     */ 
/*     */   
/* 289 */   public static final ResourceKey<Registry<LevelStem>> LEVEL_STEM = createRegistryKey("dimension");
/*     */ 
/*     */   
/* 292 */   public static final ResourceKey<Registry<LootTable>> LOOT_TABLE = createRegistryKey("loot_table");
/* 293 */   public static final ResourceKey<Registry<LootItemFunction>> ITEM_MODIFIER = createRegistryKey("item_modifier");
/* 294 */   public static final ResourceKey<Registry<LootItemCondition>> PREDICATE = createRegistryKey("predicate");
/*     */ 
/*     */   
/* 297 */   public static final ResourceKey<Registry<Advancement>> ADVANCEMENT = createRegistryKey("advancement");
/* 298 */   public static final ResourceKey<Registry<Recipe<?>>> RECIPE = createRegistryKey("recipe");
/*     */   
/*     */   public static ResourceKey<Level> levelStemToLevel(ResourceKey<LevelStem> paramResourceKey) {
/* 301 */     return ResourceKey.create(DIMENSION, paramResourceKey.identifier());
/*     */   }
/*     */   
/*     */   public static ResourceKey<LevelStem> levelToLevelStem(ResourceKey<Level> paramResourceKey) {
/* 305 */     return ResourceKey.create(LEVEL_STEM, paramResourceKey.identifier());
/*     */   }
/*     */   
/*     */   private static <T> ResourceKey<Registry<T>> createRegistryKey(String paramString) {
/* 309 */     return ResourceKey.createRegistryKey(Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   public static String elementsDirPath(ResourceKey<? extends Registry<?>> paramResourceKey) {
/* 314 */     return paramResourceKey.identifier().getPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String tagsDirPath(ResourceKey<? extends Registry<?>> paramResourceKey) {
/* 319 */     return "tags/" + paramResourceKey.identifier().getPath();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\registries\Registries.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */