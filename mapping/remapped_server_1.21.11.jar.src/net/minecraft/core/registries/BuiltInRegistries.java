/*     */ package net.minecraft.core.registries;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.advancements.CriterionTrigger;
/*     */ import net.minecraft.advancements.criterion.EntitySubPredicate;
/*     */ import net.minecraft.advancements.criterion.EntitySubPredicates;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfos;
/*     */ import net.minecraft.core.DefaultedMappedRegistry;
/*     */ import net.minecraft.core.DefaultedRegistry;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.RegistrationInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.particles.ParticleType;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.gametest.framework.BuiltinTestFunctions;
/*     */ import net.minecraft.gametest.framework.GameTestHelper;
/*     */ import net.minecraft.gametest.framework.GameTestInstance;
/*     */ import net.minecraft.gametest.framework.TestEnvironmentDefinition;
/*     */ import net.minecraft.network.chat.numbers.NumberFormatType;
/*     */ import net.minecraft.network.chat.numbers.NumberFormatTypes;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.server.dialog.DialogTypes;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ import net.minecraft.server.dialog.action.ActionTypes;
/*     */ import net.minecraft.server.dialog.body.DialogBody;
/*     */ import net.minecraft.server.dialog.body.DialogBodyTypes;
/*     */ import net.minecraft.server.dialog.input.InputControl;
/*     */ import net.minecraft.server.dialog.input.InputControlTypes;
/*     */ import net.minecraft.server.jsonrpc.IncomingRpcMethod;
/*     */ import net.minecraft.server.jsonrpc.IncomingRpcMethods;
/*     */ import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
/*     */ import net.minecraft.server.jsonrpc.OutgoingRpcMethods;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.server.permissions.Permission;
/*     */ import net.minecraft.server.permissions.PermissionCheck;
/*     */ import net.minecraft.server.permissions.PermissionCheckTypes;
/*     */ import net.minecraft.server.permissions.PermissionTypes;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.debug.DebugSubscription;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.valueproviders.FloatProviderType;
/*     */ import net.minecraft.util.valueproviders.IntProviderType;
/*     */ import net.minecraft.world.attribute.AttributeType;
/*     */ import net.minecraft.world.attribute.AttributeTypes;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.entity.variant.SpawnCondition;
/*     */ import net.minecraft.world.entity.variant.SpawnConditions;
/*     */ import net.minecraft.world.inventory.MenuType;
/*     */ import net.minecraft.world.item.CreativeModeTab;
/*     */ import net.minecraft.world.item.CreativeModeTabs;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.item.consume_effects.ConsumeEffect;
/*     */ import net.minecraft.world.item.crafting.RecipeBookCategories;
/*     */ import net.minecraft.world.item.crafting.RecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplays;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplays;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProviderTypes;
/*     */ import net.minecraft.world.item.slot.SlotSource;
/*     */ import net.minecraft.world.item.slot.SlotSources;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.BiomeSources;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.BlockTypes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPattern;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerators;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.PositionSourceType;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.DensityFunctions;
/*     */ import net.minecraft.world.level.levelgen.SurfaceRules;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
/*     */ import net.minecraft.world.level.levelgen.carver.WorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBindings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProviders;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
/*     */ import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProviders;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BuiltInRegistries
/*     */ {
/* 167 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 168 */   private static final Map<Identifier, Supplier<?>> LOADERS = Maps.newLinkedHashMap();
/*     */   
/* 170 */   private static final WritableRegistry<WritableRegistry<?>> WRITABLE_REGISTRY = (WritableRegistry<WritableRegistry<?>>)new MappedRegistry(ResourceKey.createRegistryKey(Registries.ROOT_REGISTRY_NAME), Lifecycle.stable());
/*     */ 
/*     */ 
/*     */   
/* 174 */   public static final DefaultedRegistry<GameEvent> GAME_EVENT = registerDefaulted(Registries.GAME_EVENT, "step", GameEvent::bootstrap);
/* 175 */   public static final Registry<SoundEvent> SOUND_EVENT = registerSimple(Registries.SOUND_EVENT, paramRegistry -> SoundEvents.ITEM_PICKUP);
/* 176 */   public static final DefaultedRegistry<Fluid> FLUID = registerDefaultedWithIntrusiveHolders(Registries.FLUID, "empty", paramRegistry -> Fluids.EMPTY);
/* 177 */   public static final Registry<MobEffect> MOB_EFFECT = registerSimple(Registries.MOB_EFFECT, MobEffects::bootstrap);
/* 178 */   public static final DefaultedRegistry<Block> BLOCK = registerDefaultedWithIntrusiveHolders(Registries.BLOCK, "air", paramRegistry -> Blocks.AIR);
/* 179 */   public static final Registry<DebugSubscription<?>> DEBUG_SUBSCRIPTION = registerSimple(Registries.DEBUG_SUBSCRIPTION, DebugSubscriptions::bootstrap);
/* 180 */   public static final DefaultedRegistry<EntityType<?>> ENTITY_TYPE = registerDefaultedWithIntrusiveHolders(Registries.ENTITY_TYPE, "pig", paramRegistry -> EntityType.PIG);
/* 181 */   public static final DefaultedRegistry<Item> ITEM = registerDefaultedWithIntrusiveHolders(Registries.ITEM, "air", paramRegistry -> Items.AIR);
/* 182 */   public static final Registry<Potion> POTION = registerSimple(Registries.POTION, Potions::bootstrap);
/* 183 */   public static final Registry<ParticleType<?>> PARTICLE_TYPE = registerSimple(Registries.PARTICLE_TYPE, paramRegistry -> ParticleTypes.BLOCK);
/* 184 */   public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPE = registerSimpleWithIntrusiveHolders(Registries.BLOCK_ENTITY_TYPE, paramRegistry -> BlockEntityType.FURNACE);
/* 185 */   public static final Registry<Identifier> CUSTOM_STAT = registerSimple(Registries.CUSTOM_STAT, paramRegistry -> Stats.JUMP);
/* 186 */   public static final DefaultedRegistry<ChunkStatus> CHUNK_STATUS = registerDefaulted(Registries.CHUNK_STATUS, "empty", paramRegistry -> ChunkStatus.EMPTY);
/* 187 */   public static final Registry<RuleTestType<?>> RULE_TEST = registerSimple(Registries.RULE_TEST, paramRegistry -> RuleTestType.ALWAYS_TRUE_TEST);
/* 188 */   public static final Registry<RuleBlockEntityModifierType<?>> RULE_BLOCK_ENTITY_MODIFIER = registerSimple(Registries.RULE_BLOCK_ENTITY_MODIFIER, paramRegistry -> RuleBlockEntityModifierType.PASSTHROUGH);
/* 189 */   public static final Registry<PosRuleTestType<?>> POS_RULE_TEST = registerSimple(Registries.POS_RULE_TEST, paramRegistry -> PosRuleTestType.ALWAYS_TRUE_TEST);
/* 190 */   public static final Registry<MenuType<?>> MENU = registerSimple(Registries.MENU, paramRegistry -> MenuType.ANVIL);
/* 191 */   public static final Registry<RecipeType<?>> RECIPE_TYPE = registerSimple(Registries.RECIPE_TYPE, paramRegistry -> RecipeType.CRAFTING);
/* 192 */   public static final Registry<RecipeSerializer<?>> RECIPE_SERIALIZER = registerSimple(Registries.RECIPE_SERIALIZER, paramRegistry -> RecipeSerializer.SHAPELESS_RECIPE);
/* 193 */   public static final Registry<Attribute> ATTRIBUTE = registerSimple(Registries.ATTRIBUTE, Attributes::bootstrap);
/* 194 */   public static final Registry<PositionSourceType<?>> POSITION_SOURCE_TYPE = registerSimple(Registries.POSITION_SOURCE_TYPE, paramRegistry -> PositionSourceType.BLOCK);
/* 195 */   public static final Registry<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPE = registerSimple(Registries.COMMAND_ARGUMENT_TYPE, ArgumentTypeInfos::bootstrap);
/* 196 */   public static final Registry<StatType<?>> STAT_TYPE = registerSimple(Registries.STAT_TYPE, paramRegistry -> Stats.ITEM_USED);
/* 197 */   public static final DefaultedRegistry<VillagerType> VILLAGER_TYPE = registerDefaulted(Registries.VILLAGER_TYPE, "plains", VillagerType::bootstrap);
/* 198 */   public static final DefaultedRegistry<VillagerProfession> VILLAGER_PROFESSION = registerDefaulted(Registries.VILLAGER_PROFESSION, "none", VillagerProfession::bootstrap);
/* 199 */   public static final Registry<PoiType> POINT_OF_INTEREST_TYPE = registerSimple(Registries.POINT_OF_INTEREST_TYPE, PoiTypes::bootstrap);
/* 200 */   public static final DefaultedRegistry<MemoryModuleType<?>> MEMORY_MODULE_TYPE = registerDefaulted(Registries.MEMORY_MODULE_TYPE, "dummy", paramRegistry -> MemoryModuleType.DUMMY);
/* 201 */   public static final DefaultedRegistry<SensorType<?>> SENSOR_TYPE = registerDefaulted(Registries.SENSOR_TYPE, "dummy", paramRegistry -> SensorType.DUMMY);
/* 202 */   public static final Registry<Activity> ACTIVITY = registerSimple(Registries.ACTIVITY, paramRegistry -> Activity.IDLE);
/* 203 */   public static final Registry<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = registerSimple(Registries.LOOT_POOL_ENTRY_TYPE, paramRegistry -> LootPoolEntries.EMPTY);
/* 204 */   public static final Registry<LootItemFunctionType<?>> LOOT_FUNCTION_TYPE = registerSimple(Registries.LOOT_FUNCTION_TYPE, paramRegistry -> LootItemFunctions.SET_COUNT);
/* 205 */   public static final Registry<LootItemConditionType> LOOT_CONDITION_TYPE = registerSimple(Registries.LOOT_CONDITION_TYPE, paramRegistry -> LootItemConditions.INVERTED);
/* 206 */   public static final Registry<LootNumberProviderType> LOOT_NUMBER_PROVIDER_TYPE = registerSimple(Registries.LOOT_NUMBER_PROVIDER_TYPE, paramRegistry -> NumberProviders.CONSTANT);
/* 207 */   public static final Registry<LootNbtProviderType> LOOT_NBT_PROVIDER_TYPE = registerSimple(Registries.LOOT_NBT_PROVIDER_TYPE, paramRegistry -> NbtProviders.CONTEXT);
/* 208 */   public static final Registry<LootScoreProviderType> LOOT_SCORE_PROVIDER_TYPE = registerSimple(Registries.LOOT_SCORE_PROVIDER_TYPE, paramRegistry -> ScoreboardNameProviders.CONTEXT);
/* 209 */   public static final Registry<FloatProviderType<?>> FLOAT_PROVIDER_TYPE = registerSimple(Registries.FLOAT_PROVIDER_TYPE, paramRegistry -> FloatProviderType.CONSTANT);
/* 210 */   public static final Registry<IntProviderType<?>> INT_PROVIDER_TYPE = registerSimple(Registries.INT_PROVIDER_TYPE, paramRegistry -> IntProviderType.CONSTANT);
/* 211 */   public static final Registry<HeightProviderType<?>> HEIGHT_PROVIDER_TYPE = registerSimple(Registries.HEIGHT_PROVIDER_TYPE, paramRegistry -> HeightProviderType.CONSTANT);
/* 212 */   public static final Registry<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = registerSimple(Registries.BLOCK_PREDICATE_TYPE, paramRegistry -> BlockPredicateType.NOT);
/* 213 */   public static final Registry<WorldCarver<?>> CARVER = registerSimple(Registries.CARVER, paramRegistry -> WorldCarver.CAVE);
/* 214 */   public static final Registry<Feature<?>> FEATURE = registerSimple(Registries.FEATURE, paramRegistry -> Feature.ORE);
/* 215 */   public static final Registry<StructurePlacementType<?>> STRUCTURE_PLACEMENT = registerSimple(Registries.STRUCTURE_PLACEMENT, paramRegistry -> StructurePlacementType.RANDOM_SPREAD);
/* 216 */   public static final Registry<StructurePieceType> STRUCTURE_PIECE = registerSimple(Registries.STRUCTURE_PIECE, paramRegistry -> StructurePieceType.MINE_SHAFT_ROOM);
/* 217 */   public static final Registry<StructureType<?>> STRUCTURE_TYPE = registerSimple(Registries.STRUCTURE_TYPE, paramRegistry -> StructureType.JIGSAW);
/* 218 */   public static final Registry<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = registerSimple(Registries.PLACEMENT_MODIFIER_TYPE, paramRegistry -> PlacementModifierType.COUNT);
/* 219 */   public static final Registry<BlockStateProviderType<?>> BLOCKSTATE_PROVIDER_TYPE = registerSimple(Registries.BLOCK_STATE_PROVIDER_TYPE, paramRegistry -> BlockStateProviderType.SIMPLE_STATE_PROVIDER);
/* 220 */   public static final Registry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = registerSimple(Registries.FOLIAGE_PLACER_TYPE, paramRegistry -> FoliagePlacerType.BLOB_FOLIAGE_PLACER);
/* 221 */   public static final Registry<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = registerSimple(Registries.TRUNK_PLACER_TYPE, paramRegistry -> TrunkPlacerType.STRAIGHT_TRUNK_PLACER);
/* 222 */   public static final Registry<RootPlacerType<?>> ROOT_PLACER_TYPE = registerSimple(Registries.ROOT_PLACER_TYPE, paramRegistry -> RootPlacerType.MANGROVE_ROOT_PLACER);
/* 223 */   public static final Registry<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = registerSimple(Registries.TREE_DECORATOR_TYPE, paramRegistry -> TreeDecoratorType.LEAVE_VINE);
/* 224 */   public static final Registry<FeatureSizeType<?>> FEATURE_SIZE_TYPE = registerSimple(Registries.FEATURE_SIZE_TYPE, paramRegistry -> FeatureSizeType.TWO_LAYERS_FEATURE_SIZE);
/* 225 */   public static final Registry<MapCodec<? extends BiomeSource>> BIOME_SOURCE = registerSimple(Registries.BIOME_SOURCE, BiomeSources::bootstrap);
/* 226 */   public static final Registry<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATOR = registerSimple(Registries.CHUNK_GENERATOR, ChunkGenerators::bootstrap);
/* 227 */   public static final Registry<MapCodec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITION = registerSimple(Registries.MATERIAL_CONDITION, SurfaceRules.ConditionSource::bootstrap);
/* 228 */   public static final Registry<MapCodec<? extends SurfaceRules.RuleSource>> MATERIAL_RULE = registerSimple(Registries.MATERIAL_RULE, SurfaceRules.RuleSource::bootstrap);
/* 229 */   public static final Registry<MapCodec<? extends DensityFunction>> DENSITY_FUNCTION_TYPE = registerSimple(Registries.DENSITY_FUNCTION_TYPE, DensityFunctions::bootstrap);
/* 230 */   public static final Registry<MapCodec<? extends Block>> BLOCK_TYPE = registerSimple(Registries.BLOCK_TYPE, BlockTypes::bootstrap);
/* 231 */   public static final Registry<StructureProcessorType<?>> STRUCTURE_PROCESSOR = registerSimple(Registries.STRUCTURE_PROCESSOR, paramRegistry -> StructureProcessorType.BLOCK_IGNORE);
/* 232 */   public static final Registry<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = registerSimple(Registries.STRUCTURE_POOL_ELEMENT, paramRegistry -> StructurePoolElementType.EMPTY);
/* 233 */   public static final Registry<MapCodec<? extends PoolAliasBinding>> POOL_ALIAS_BINDING_TYPE = registerSimple(Registries.POOL_ALIAS_BINDING, PoolAliasBindings::bootstrap);
/* 234 */   public static final Registry<DecoratedPotPattern> DECORATED_POT_PATTERN = registerSimple(Registries.DECORATED_POT_PATTERN, DecoratedPotPatterns::bootstrap);
/* 235 */   public static final Registry<CreativeModeTab> CREATIVE_MODE_TAB = registerSimple(Registries.CREATIVE_MODE_TAB, CreativeModeTabs::bootstrap);
/* 236 */   public static final Registry<CriterionTrigger<?>> TRIGGER_TYPES = registerSimple(Registries.TRIGGER_TYPE, CriteriaTriggers::bootstrap);
/* 237 */   public static final Registry<NumberFormatType<?>> NUMBER_FORMAT_TYPE = registerSimple(Registries.NUMBER_FORMAT_TYPE, NumberFormatTypes::bootstrap);
/* 238 */   public static final Registry<DataComponentType<?>> DATA_COMPONENT_TYPE = registerSimple(Registries.DATA_COMPONENT_TYPE, DataComponents::bootstrap);
/* 239 */   public static final Registry<GameRule<?>> GAME_RULE = registerSimple(Registries.GAME_RULE, GameRules::bootstrap);
/* 240 */   public static final Registry<MapCodec<? extends EntitySubPredicate>> ENTITY_SUB_PREDICATE_TYPE = registerSimple(Registries.ENTITY_SUB_PREDICATE_TYPE, EntitySubPredicates::bootstrap);
/* 241 */   public static final Registry<DataComponentPredicate.Type<?>> DATA_COMPONENT_PREDICATE_TYPE = registerSimple(Registries.DATA_COMPONENT_PREDICATE_TYPE, DataComponentPredicates::bootstrap);
/* 242 */   public static final Registry<MapDecorationType> MAP_DECORATION_TYPE = registerSimple(Registries.MAP_DECORATION_TYPE, MapDecorationTypes::bootstrap);
/* 243 */   public static final Registry<DataComponentType<?>> ENCHANTMENT_EFFECT_COMPONENT_TYPE = registerSimple(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, EnchantmentEffectComponents::bootstrap);
/* 244 */   public static final Registry<MapCodec<? extends LevelBasedValue>> ENCHANTMENT_LEVEL_BASED_VALUE_TYPE = registerSimple(Registries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE, LevelBasedValue::bootstrap);
/* 245 */   public static final Registry<MapCodec<? extends EnchantmentEntityEffect>> ENCHANTMENT_ENTITY_EFFECT_TYPE = registerSimple(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, EnchantmentEntityEffect::bootstrap);
/* 246 */   public static final Registry<MapCodec<? extends EnchantmentLocationBasedEffect>> ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE = registerSimple(Registries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE, EnchantmentLocationBasedEffect::bootstrap);
/* 247 */   public static final Registry<MapCodec<? extends EnchantmentValueEffect>> ENCHANTMENT_VALUE_EFFECT_TYPE = registerSimple(Registries.ENCHANTMENT_VALUE_EFFECT_TYPE, EnchantmentValueEffect::bootstrap);
/* 248 */   public static final Registry<MapCodec<? extends EnchantmentProvider>> ENCHANTMENT_PROVIDER_TYPE = registerSimple(Registries.ENCHANTMENT_PROVIDER_TYPE, EnchantmentProviderTypes::bootstrap);
/* 249 */   public static final Registry<ConsumeEffect.Type<?>> CONSUME_EFFECT_TYPE = registerSimple(Registries.CONSUME_EFFECT_TYPE, paramRegistry -> ConsumeEffect.Type.APPLY_EFFECTS);
/* 250 */   public static final Registry<RecipeDisplay.Type<?>> RECIPE_DISPLAY = registerSimple(Registries.RECIPE_DISPLAY, RecipeDisplays::bootstrap);
/* 251 */   public static final Registry<SlotDisplay.Type<?>> SLOT_DISPLAY = registerSimple(Registries.SLOT_DISPLAY, SlotDisplays::bootstrap);
/* 252 */   public static final Registry<RecipeBookCategory> RECIPE_BOOK_CATEGORY = registerSimple(Registries.RECIPE_BOOK_CATEGORY, RecipeBookCategories::bootstrap);
/* 253 */   public static final Registry<TicketType> TICKET_TYPE = registerSimple(Registries.TICKET_TYPE, paramRegistry -> TicketType.UNKNOWN);
/* 254 */   public static final Registry<IncomingRpcMethod<?, ?>> INCOMING_RPC_METHOD = registerSimple(Registries.INCOMING_RPC_METHOD, IncomingRpcMethods::bootstrap);
/* 255 */   public static final Registry<OutgoingRpcMethod<?, ?>> OUTGOING_RPC_METHOD = registerSimple(Registries.OUTGOING_RPC_METHOD, paramRegistry -> OutgoingRpcMethods.SERVER_STARTED);
/* 256 */   public static final Registry<MapCodec<? extends TestEnvironmentDefinition>> TEST_ENVIRONMENT_DEFINITION_TYPE = registerSimple(Registries.TEST_ENVIRONMENT_DEFINITION_TYPE, TestEnvironmentDefinition::bootstrap);
/* 257 */   public static final Registry<MapCodec<? extends GameTestInstance>> TEST_INSTANCE_TYPE = registerSimple(Registries.TEST_INSTANCE_TYPE, GameTestInstance::bootstrap);
/* 258 */   public static final Registry<MapCodec<? extends SpawnCondition>> SPAWN_CONDITION_TYPE = registerSimple(Registries.SPAWN_CONDITION_TYPE, SpawnConditions::bootstrap);
/* 259 */   public static final Registry<MapCodec<? extends Dialog>> DIALOG_TYPE = registerSimple(Registries.DIALOG_TYPE, DialogTypes::bootstrap);
/* 260 */   public static final Registry<MapCodec<? extends Action>> DIALOG_ACTION_TYPE = registerSimple(Registries.DIALOG_ACTION_TYPE, ActionTypes::bootstrap);
/* 261 */   public static final Registry<MapCodec<? extends InputControl>> INPUT_CONTROL_TYPE = registerSimple(Registries.INPUT_CONTROL_TYPE, InputControlTypes::bootstrap);
/* 262 */   public static final Registry<MapCodec<? extends DialogBody>> DIALOG_BODY_TYPE = registerSimple(Registries.DIALOG_BODY_TYPE, DialogBodyTypes::bootstrap);
/* 263 */   public static final Registry<MapCodec<? extends Permission>> PERMISSION_TYPE = registerSimple(Registries.PERMISSION_TYPE, PermissionTypes::bootstrap);
/* 264 */   public static final Registry<MapCodec<? extends PermissionCheck>> PERMISSION_CHECK_TYPE = registerSimple(Registries.PERMISSION_CHECK_TYPE, PermissionCheckTypes::bootstrap);
/* 265 */   public static final Registry<EnvironmentAttribute<?>> ENVIRONMENT_ATTRIBUTE = registerSimple(Registries.ENVIRONMENT_ATTRIBUTE, EnvironmentAttributes::bootstrap);
/* 266 */   public static final Registry<AttributeType<?>> ATTRIBUTE_TYPE = registerSimple(Registries.ATTRIBUTE_TYPE, AttributeTypes::bootstrap);
/* 267 */   public static final Registry<MapCodec<? extends SlotSource>> SLOT_SOURCE_TYPE = registerSimple(Registries.SLOT_SOURCE_TYPE, SlotSources::bootstrap);
/*     */ 
/*     */   
/* 270 */   public static final Registry<Consumer<GameTestHelper>> TEST_FUNCTION = registerSimple(Registries.TEST_FUNCTION, BuiltinTestFunctions::bootstrap);
/*     */   
/* 272 */   public static final Registry<? extends Registry<?>> REGISTRY = (Registry)WRITABLE_REGISTRY;
/*     */   
/*     */   private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> paramResourceKey, RegistryBootstrap<T> paramRegistryBootstrap) {
/* 275 */     return (Registry<T>)internalRegister(paramResourceKey, new MappedRegistry(paramResourceKey, Lifecycle.stable(), false), paramRegistryBootstrap);
/*     */   }
/*     */   
/*     */   private static <T> Registry<T> registerSimpleWithIntrusiveHolders(ResourceKey<? extends Registry<T>> paramResourceKey, RegistryBootstrap<T> paramRegistryBootstrap) {
/* 279 */     return (Registry<T>)internalRegister(paramResourceKey, new MappedRegistry(paramResourceKey, Lifecycle.stable(), true), paramRegistryBootstrap);
/*     */   }
/*     */   
/*     */   private static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> paramResourceKey, String paramString, RegistryBootstrap<T> paramRegistryBootstrap) {
/* 283 */     return (DefaultedRegistry<T>)internalRegister(paramResourceKey, new DefaultedMappedRegistry(paramString, paramResourceKey, Lifecycle.stable(), false), paramRegistryBootstrap);
/*     */   }
/*     */   
/*     */   private static <T> DefaultedRegistry<T> registerDefaultedWithIntrusiveHolders(ResourceKey<? extends Registry<T>> paramResourceKey, String paramString, RegistryBootstrap<T> paramRegistryBootstrap) {
/* 287 */     return (DefaultedRegistry<T>)internalRegister(paramResourceKey, new DefaultedMappedRegistry(paramString, paramResourceKey, Lifecycle.stable(), true), paramRegistryBootstrap);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T, R extends WritableRegistry<T>> R internalRegister(ResourceKey<? extends Registry<T>> paramResourceKey, R paramR, RegistryBootstrap<T> paramRegistryBootstrap) {
/* 292 */     Bootstrap.checkBootstrapCalled(() -> "registry " + String.valueOf(paramResourceKey.identifier()));
/* 293 */     Identifier identifier = paramResourceKey.identifier();
/* 294 */     LOADERS.put(identifier, () -> paramRegistryBootstrap.run((Registry)paramWritableRegistry));
/*     */     
/* 296 */     WRITABLE_REGISTRY.register(paramResourceKey, paramR, RegistrationInfo.BUILT_IN);
/* 297 */     return paramR;
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/* 301 */     createContents();
/* 302 */     freeze();
/* 303 */     validate(REGISTRY);
/*     */   }
/*     */   
/*     */   private static void createContents() {
/* 307 */     LOADERS.forEach((paramIdentifier, paramSupplier) -> {
/*     */           if (paramSupplier.get() == null) {
/*     */             LOGGER.error("Unable to bootstrap registry '{}'", paramIdentifier);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static void freeze() {
/* 316 */     REGISTRY.freeze();
/* 317 */     for (Registry<?> registry : REGISTRY) {
/* 318 */       bindBootstrappedTagsToEmpty(registry);
/* 319 */       registry.freeze();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T extends Registry<?>> void validate(Registry<T> paramRegistry) {
/* 324 */     paramRegistry.forEach(paramRegistry2 -> {
/*     */           if (paramRegistry2.keySet().isEmpty()) {
/*     */             Util.logAndPauseIfInIde("Registry '" + String.valueOf(paramRegistry1.getKey(paramRegistry2)) + "' was empty after loading");
/*     */           }
/*     */           if (paramRegistry2 instanceof DefaultedRegistry) {
/*     */             Identifier identifier = ((DefaultedRegistry)paramRegistry2).getDefaultKey();
/*     */             Objects.requireNonNull(paramRegistry2.getValue(identifier), "Missing default of DefaultedMappedRegistry: " + String.valueOf(identifier));
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> HolderGetter<T> acquireBootstrapRegistrationLookup(Registry<T> paramRegistry) {
/* 344 */     return ((WritableRegistry)paramRegistry).createRegistrationLookup();
/*     */   }
/*     */   
/*     */   private static void bindBootstrappedTagsToEmpty(Registry<?> paramRegistry) {
/* 348 */     ((MappedRegistry)paramRegistry).bindAllTagsToEmpty();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface RegistryBootstrap<T> {
/*     */     Object run(Registry<T> param1Registry);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\registries\BuiltInRegistries.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */