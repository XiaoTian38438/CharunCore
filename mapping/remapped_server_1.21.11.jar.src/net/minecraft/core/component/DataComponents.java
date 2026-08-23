/*     */ package net.minecraft.core.component;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowVariant;
/*     */ import net.minecraft.world.entity.animal.cow.MushroomCow;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.entity.animal.equine.Variant;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.fish.Salmon;
/*     */ import net.minecraft.world.entity.animal.fish.TropicalFish;
/*     */ import net.minecraft.world.entity.animal.fox.Fox;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ import net.minecraft.world.entity.animal.pig.PigVariant;
/*     */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.item.AdventureModePredicate;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.EitherHolder;
/*     */ import net.minecraft.world.item.JukeboxPlayable;
/*     */ import net.minecraft.world.item.Rarity;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.item.component.Bees;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.item.component.BlocksAttacks;
/*     */ import net.minecraft.world.item.component.BundleContents;
/*     */ import net.minecraft.world.item.component.ChargedProjectiles;
/*     */ import net.minecraft.world.item.component.Consumable;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.item.component.CustomModelData;
/*     */ import net.minecraft.world.item.component.DamageResistant;
/*     */ import net.minecraft.world.item.component.DyedItemColor;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.item.component.Fireworks;
/*     */ import net.minecraft.world.item.component.InstrumentComponent;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.item.component.ItemLore;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.item.component.LodestoneTracker;
/*     */ import net.minecraft.world.item.component.MapItemColor;
/*     */ import net.minecraft.world.item.component.OminousBottleAmplifier;
/*     */ import net.minecraft.world.item.component.PiercingWeapon;
/*     */ import net.minecraft.world.item.component.ProvidesTrimMaterial;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*     */ import net.minecraft.world.item.component.SwingAnimation;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.component.UseCooldown;
/*     */ import net.minecraft.world.item.component.UseEffects;
/*     */ import net.minecraft.world.item.component.UseRemainder;
/*     */ import net.minecraft.world.item.component.Weapon;
/*     */ import net.minecraft.world.item.component.WritableBookContent;
/*     */ import net.minecraft.world.item.component.WrittenBookContent;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.item.enchantment.Repairable;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*     */ import net.minecraft.world.level.block.entity.PotDecorations;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ 
/*     */ public class DataComponents {
/*     */   public static final DataComponentType<CustomData> CUSTOM_DATA;
/*     */   public static final DataComponentType<Integer> MAX_STACK_SIZE;
/*     */   public static final DataComponentType<Integer> MAX_DAMAGE;
/*     */   public static final DataComponentType<Integer> DAMAGE;
/*     */   public static final DataComponentType<Unit> UNBREAKABLE;
/*     */   public static final DataComponentType<UseEffects> USE_EFFECTS;
/*     */   public static final DataComponentType<Component> CUSTOM_NAME;
/*     */   public static final DataComponentType<Float> MINIMUM_ATTACK_CHARGE;
/*     */   public static final DataComponentType<EitherHolder<DamageType>> DAMAGE_TYPE;
/*     */   public static final DataComponentType<Component> ITEM_NAME;
/*     */   public static final DataComponentType<Identifier> ITEM_MODEL;
/*     */   public static final DataComponentType<ItemLore> LORE;
/*     */   public static final DataComponentType<Rarity> RARITY;
/*     */   public static final DataComponentType<ItemEnchantments> ENCHANTMENTS;
/*     */   public static final DataComponentType<AdventureModePredicate> CAN_PLACE_ON;
/*     */   public static final DataComponentType<AdventureModePredicate> CAN_BREAK;
/*     */   public static final DataComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS;
/*     */   public static final DataComponentType<CustomModelData> CUSTOM_MODEL_DATA;
/*     */   public static final DataComponentType<TooltipDisplay> TOOLTIP_DISPLAY;
/*     */   public static final DataComponentType<Integer> REPAIR_COST;
/*     */   public static final DataComponentType<Unit> CREATIVE_SLOT_LOCK;
/*     */   public static final DataComponentType<Boolean> ENCHANTMENT_GLINT_OVERRIDE;
/*     */   public static final DataComponentType<Unit> INTANGIBLE_PROJECTILE;
/*     */   public static final DataComponentType<FoodProperties> FOOD;
/*     */   public static final DataComponentType<Consumable> CONSUMABLE;
/*     */   public static final DataComponentType<UseRemainder> USE_REMAINDER;
/* 106 */   static final EncoderCache ENCODER_CACHE = new EncoderCache(512); public static final DataComponentType<UseCooldown> USE_COOLDOWN; public static final DataComponentType<DamageResistant> DAMAGE_RESISTANT; public static final DataComponentType<Tool> TOOL; public static final DataComponentType<Weapon> WEAPON; public static final DataComponentType<AttackRange> ATTACK_RANGE; public static final DataComponentType<Enchantable> ENCHANTABLE; public static final DataComponentType<Equippable> EQUIPPABLE; public static final DataComponentType<Repairable> REPAIRABLE; public static final DataComponentType<Unit> GLIDER; public static final DataComponentType<Identifier> TOOLTIP_STYLE; public static final DataComponentType<DeathProtection> DEATH_PROTECTION; public static final DataComponentType<BlocksAttacks> BLOCKS_ATTACKS; public static final DataComponentType<PiercingWeapon> PIERCING_WEAPON; public static final DataComponentType<KineticWeapon> KINETIC_WEAPON; public static final DataComponentType<SwingAnimation> SWING_ANIMATION; public static final DataComponentType<ItemEnchantments> STORED_ENCHANTMENTS; public static final DataComponentType<DyedItemColor> DYED_COLOR; public static final DataComponentType<MapItemColor> MAP_COLOR; public static final DataComponentType<MapId> MAP_ID; public static final DataComponentType<MapDecorations> MAP_DECORATIONS; public static final DataComponentType<MapPostProcessing> MAP_POST_PROCESSING; public static final DataComponentType<ChargedProjectiles> CHARGED_PROJECTILES; public static final DataComponentType<BundleContents> BUNDLE_CONTENTS; public static final DataComponentType<PotionContents> POTION_CONTENTS; public static final DataComponentType<Float> POTION_DURATION_SCALE;
/*     */   public static final DataComponentType<SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS;
/*     */   
/*     */   static {
/* 110 */     CUSTOM_DATA = register("custom_data", paramBuilder -> paramBuilder.persistent(CustomData.CODEC));
/*     */     
/* 112 */     MAX_STACK_SIZE = register("max_stack_size", paramBuilder -> paramBuilder.persistent(ExtraCodecs.intRange(1, 99)).networkSynchronized(ByteBufCodecs.VAR_INT));
/*     */ 
/*     */     
/* 115 */     MAX_DAMAGE = register("max_damage", paramBuilder -> paramBuilder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
/*     */ 
/*     */     
/* 118 */     DAMAGE = register("damage", paramBuilder -> paramBuilder.persistent(ExtraCodecs.NON_NEGATIVE_INT).ignoreSwapAnimation().networkSynchronized(ByteBufCodecs.VAR_INT));
/*     */ 
/*     */     
/* 121 */     UNBREAKABLE = register("unbreakable", paramBuilder -> paramBuilder.persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC));
/*     */ 
/*     */     
/* 124 */     USE_EFFECTS = register("use_effects", paramBuilder -> paramBuilder.persistent(UseEffects.CODEC).networkSynchronized(UseEffects.STREAM_CODEC));
/*     */ 
/*     */     
/* 127 */     CUSTOM_NAME = register("custom_name", paramBuilder -> paramBuilder.persistent(ComponentSerialization.CODEC).networkSynchronized(ComponentSerialization.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 131 */     MINIMUM_ATTACK_CHARGE = register("minimum_attack_charge", paramBuilder -> paramBuilder.persistent(ExtraCodecs.floatRange(0.0F, 1.0F)).networkSynchronized(ByteBufCodecs.FLOAT));
/*     */ 
/*     */     
/* 134 */     DAMAGE_TYPE = register("damage_type", paramBuilder -> paramBuilder.persistent(EitherHolder.codec(Registries.DAMAGE_TYPE, DamageType.CODEC)).networkSynchronized(EitherHolder.streamCodec(Registries.DAMAGE_TYPE, DamageType.STREAM_CODEC)));
/*     */ 
/*     */ 
/*     */     
/* 138 */     ITEM_NAME = register("item_name", paramBuilder -> paramBuilder.persistent(ComponentSerialization.CODEC).networkSynchronized(ComponentSerialization.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 142 */     ITEM_MODEL = register("item_model", paramBuilder -> paramBuilder.persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 146 */     LORE = register("lore", paramBuilder -> paramBuilder.persistent(ItemLore.CODEC).networkSynchronized(ItemLore.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 150 */     RARITY = register("rarity", paramBuilder -> paramBuilder.persistent(Rarity.CODEC).networkSynchronized(Rarity.STREAM_CODEC));
/*     */ 
/*     */     
/* 153 */     ENCHANTMENTS = register("enchantments", paramBuilder -> paramBuilder.persistent(ItemEnchantments.CODEC).networkSynchronized(ItemEnchantments.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 157 */     CAN_PLACE_ON = register("can_place_on", paramBuilder -> paramBuilder.persistent(AdventureModePredicate.CODEC).networkSynchronized(AdventureModePredicate.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 161 */     CAN_BREAK = register("can_break", paramBuilder -> paramBuilder.persistent(AdventureModePredicate.CODEC).networkSynchronized(AdventureModePredicate.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 165 */     ATTRIBUTE_MODIFIERS = register("attribute_modifiers", paramBuilder -> paramBuilder.persistent(ItemAttributeModifiers.CODEC).networkSynchronized(ItemAttributeModifiers.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 169 */     CUSTOM_MODEL_DATA = register("custom_model_data", paramBuilder -> paramBuilder.persistent(CustomModelData.CODEC).networkSynchronized(CustomModelData.STREAM_CODEC));
/*     */ 
/*     */     
/* 172 */     TOOLTIP_DISPLAY = register("tooltip_display", paramBuilder -> paramBuilder.persistent(TooltipDisplay.CODEC).networkSynchronized(TooltipDisplay.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 176 */     REPAIR_COST = register("repair_cost", paramBuilder -> paramBuilder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
/*     */ 
/*     */     
/* 179 */     CREATIVE_SLOT_LOCK = register("creative_slot_lock", paramBuilder -> paramBuilder.networkSynchronized(Unit.STREAM_CODEC));
/*     */ 
/*     */     
/* 182 */     ENCHANTMENT_GLINT_OVERRIDE = register("enchantment_glint_override", paramBuilder -> paramBuilder.persistent((Codec)Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
/*     */ 
/*     */     
/* 185 */     INTANGIBLE_PROJECTILE = register("intangible_projectile", paramBuilder -> paramBuilder.persistent(Unit.CODEC));
/*     */     
/* 187 */     FOOD = register("food", paramBuilder -> paramBuilder.persistent(FoodProperties.DIRECT_CODEC).networkSynchronized(FoodProperties.DIRECT_STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 191 */     CONSUMABLE = register("consumable", paramBuilder -> paramBuilder.persistent(Consumable.CODEC).networkSynchronized(Consumable.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 195 */     USE_REMAINDER = register("use_remainder", paramBuilder -> paramBuilder.persistent(UseRemainder.CODEC).networkSynchronized(UseRemainder.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 199 */     USE_COOLDOWN = register("use_cooldown", paramBuilder -> paramBuilder.persistent(UseCooldown.CODEC).networkSynchronized(UseCooldown.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 203 */     DAMAGE_RESISTANT = register("damage_resistant", paramBuilder -> paramBuilder.persistent(DamageResistant.CODEC).networkSynchronized(DamageResistant.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 207 */     TOOL = register("tool", paramBuilder -> paramBuilder.persistent(Tool.CODEC).networkSynchronized(Tool.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 211 */     WEAPON = register("weapon", paramBuilder -> paramBuilder.persistent(Weapon.CODEC).networkSynchronized(Weapon.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 215 */     ATTACK_RANGE = register("attack_range", paramBuilder -> paramBuilder.persistent(AttackRange.CODEC).networkSynchronized(AttackRange.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 219 */     ENCHANTABLE = register("enchantable", paramBuilder -> paramBuilder.persistent(Enchantable.CODEC).networkSynchronized(Enchantable.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 223 */     EQUIPPABLE = register("equippable", paramBuilder -> paramBuilder.persistent(Equippable.CODEC).networkSynchronized(Equippable.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 227 */     REPAIRABLE = register("repairable", paramBuilder -> paramBuilder.persistent(Repairable.CODEC).networkSynchronized(Repairable.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 231 */     GLIDER = register("glider", paramBuilder -> paramBuilder.persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC));
/*     */ 
/*     */     
/* 234 */     TOOLTIP_STYLE = register("tooltip_style", paramBuilder -> paramBuilder.persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 238 */     DEATH_PROTECTION = register("death_protection", paramBuilder -> paramBuilder.persistent(DeathProtection.CODEC).networkSynchronized(DeathProtection.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 242 */     BLOCKS_ATTACKS = register("blocks_attacks", paramBuilder -> paramBuilder.persistent(BlocksAttacks.CODEC).networkSynchronized(BlocksAttacks.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 246 */     PIERCING_WEAPON = register("piercing_weapon", paramBuilder -> paramBuilder.persistent(PiercingWeapon.CODEC).networkSynchronized(PiercingWeapon.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 250 */     KINETIC_WEAPON = register("kinetic_weapon", paramBuilder -> paramBuilder.persistent(KineticWeapon.CODEC).networkSynchronized(KineticWeapon.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 254 */     SWING_ANIMATION = register("swing_animation", paramBuilder -> paramBuilder.persistent(SwingAnimation.CODEC).networkSynchronized(SwingAnimation.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     STORED_ENCHANTMENTS = register("stored_enchantments", paramBuilder -> paramBuilder.persistent(ItemEnchantments.CODEC).networkSynchronized(ItemEnchantments.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 264 */     DYED_COLOR = register("dyed_color", paramBuilder -> paramBuilder.persistent(DyedItemColor.CODEC).networkSynchronized(DyedItemColor.STREAM_CODEC));
/*     */ 
/*     */     
/* 267 */     MAP_COLOR = register("map_color", paramBuilder -> paramBuilder.persistent(MapItemColor.CODEC).networkSynchronized(MapItemColor.STREAM_CODEC));
/*     */ 
/*     */     
/* 270 */     MAP_ID = register("map_id", paramBuilder -> paramBuilder.persistent(MapId.CODEC).networkSynchronized(MapId.STREAM_CODEC));
/*     */ 
/*     */     
/* 273 */     MAP_DECORATIONS = register("map_decorations", paramBuilder -> paramBuilder.persistent(MapDecorations.CODEC).cacheEncoding());
/*     */ 
/*     */     
/* 276 */     MAP_POST_PROCESSING = register("map_post_processing", paramBuilder -> paramBuilder.networkSynchronized(MapPostProcessing.STREAM_CODEC));
/*     */     
/* 278 */     CHARGED_PROJECTILES = register("charged_projectiles", paramBuilder -> paramBuilder.persistent(ChargedProjectiles.CODEC).networkSynchronized(ChargedProjectiles.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 282 */     BUNDLE_CONTENTS = register("bundle_contents", paramBuilder -> paramBuilder.persistent(BundleContents.CODEC).networkSynchronized(BundleContents.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 286 */     POTION_CONTENTS = register("potion_contents", paramBuilder -> paramBuilder.persistent(PotionContents.CODEC).networkSynchronized(PotionContents.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 290 */     POTION_DURATION_SCALE = register("potion_duration_scale", paramBuilder -> paramBuilder.persistent(ExtraCodecs.NON_NEGATIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 294 */     SUSPICIOUS_STEW_EFFECTS = register("suspicious_stew_effects", paramBuilder -> paramBuilder.persistent(SuspiciousStewEffects.CODEC).networkSynchronized(SuspiciousStewEffects.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 298 */     WRITABLE_BOOK_CONTENT = register("writable_book_content", paramBuilder -> paramBuilder.persistent(WritableBookContent.CODEC).networkSynchronized(WritableBookContent.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 302 */     WRITTEN_BOOK_CONTENT = register("written_book_content", paramBuilder -> paramBuilder.persistent(WrittenBookContent.CODEC).networkSynchronized(WrittenBookContent.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 306 */     TRIM = register("trim", paramBuilder -> paramBuilder.persistent(ArmorTrim.CODEC).networkSynchronized(ArmorTrim.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 310 */     DEBUG_STICK_STATE = register("debug_stick_state", paramBuilder -> paramBuilder.persistent(DebugStickState.CODEC).cacheEncoding());
/*     */ 
/*     */     
/* 313 */     ENTITY_DATA = register("entity_data", paramBuilder -> paramBuilder.persistent(TypedEntityData.codec(EntityType.CODEC)).networkSynchronized(TypedEntityData.streamCodec(EntityType.STREAM_CODEC)));
/*     */ 
/*     */     
/* 316 */     BUCKET_ENTITY_DATA = register("bucket_entity_data", paramBuilder -> paramBuilder.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC));
/*     */ 
/*     */     
/* 319 */     BLOCK_ENTITY_DATA = register("block_entity_data", paramBuilder -> paramBuilder.persistent(TypedEntityData.codec(BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec())).networkSynchronized(TypedEntityData.streamCodec(ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE))));
/*     */ 
/*     */     
/* 322 */     INSTRUMENT = register("instrument", paramBuilder -> paramBuilder.persistent(InstrumentComponent.CODEC).networkSynchronized(InstrumentComponent.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 326 */     PROVIDES_TRIM_MATERIAL = register("provides_trim_material", paramBuilder -> paramBuilder.persistent(ProvidesTrimMaterial.CODEC).networkSynchronized(ProvidesTrimMaterial.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 330 */     OMINOUS_BOTTLE_AMPLIFIER = register("ominous_bottle_amplifier", paramBuilder -> paramBuilder.persistent(OminousBottleAmplifier.CODEC).networkSynchronized(OminousBottleAmplifier.STREAM_CODEC));
/*     */ 
/*     */     
/* 333 */     JUKEBOX_PLAYABLE = register("jukebox_playable", paramBuilder -> paramBuilder.persistent(JukeboxPlayable.CODEC).networkSynchronized(JukeboxPlayable.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 337 */     PROVIDES_BANNER_PATTERNS = register("provides_banner_patterns", paramBuilder -> paramBuilder.persistent(TagKey.hashedCodec(Registries.BANNER_PATTERN)).networkSynchronized(TagKey.streamCodec(Registries.BANNER_PATTERN)).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 341 */     RECIPES = register("recipes", paramBuilder -> paramBuilder.persistent(Recipe.KEY_CODEC.listOf()).cacheEncoding());
/*     */ 
/*     */     
/* 344 */     LODESTONE_TRACKER = register("lodestone_tracker", paramBuilder -> paramBuilder.persistent(LodestoneTracker.CODEC).networkSynchronized(LodestoneTracker.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 348 */     FIREWORK_EXPLOSION = register("firework_explosion", paramBuilder -> paramBuilder.persistent(FireworkExplosion.CODEC).networkSynchronized(FireworkExplosion.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 352 */     FIREWORKS = register("fireworks", paramBuilder -> paramBuilder.persistent(Fireworks.CODEC).networkSynchronized(Fireworks.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 356 */     PROFILE = register("profile", paramBuilder -> paramBuilder.persistent(ResolvableProfile.CODEC).networkSynchronized(ResolvableProfile.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 360 */     NOTE_BLOCK_SOUND = register("note_block_sound", paramBuilder -> paramBuilder.persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC));
/*     */ 
/*     */     
/* 363 */     BANNER_PATTERNS = register("banner_patterns", paramBuilder -> paramBuilder.persistent(BannerPatternLayers.CODEC).networkSynchronized(BannerPatternLayers.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 367 */     BASE_COLOR = register("base_color", paramBuilder -> paramBuilder.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/*     */ 
/*     */     
/* 370 */     POT_DECORATIONS = register("pot_decorations", paramBuilder -> paramBuilder.persistent(PotDecorations.CODEC).networkSynchronized(PotDecorations.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 374 */     CONTAINER = register("container", paramBuilder -> paramBuilder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 378 */     BLOCK_STATE = register("block_state", paramBuilder -> paramBuilder.persistent(BlockItemStateProperties.CODEC).networkSynchronized(BlockItemStateProperties.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 382 */     BEES = register("bees", paramBuilder -> paramBuilder.persistent(Bees.CODEC).networkSynchronized(Bees.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */     
/* 386 */     LOCK = register("lock", paramBuilder -> paramBuilder.persistent(LockCode.CODEC));
/*     */     
/* 388 */     CONTAINER_LOOT = register("container_loot", paramBuilder -> paramBuilder.persistent(SeededContainerLoot.CODEC));
/*     */     
/* 390 */     BREAK_SOUND = register("break_sound", paramBuilder -> paramBuilder.persistent(SoundEvent.CODEC).networkSynchronized(SoundEvent.STREAM_CODEC).cacheEncoding());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 395 */     VILLAGER_VARIANT = register("villager/variant", paramBuilder -> paramBuilder.persistent(VillagerType.CODEC).networkSynchronized(VillagerType.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 399 */     WOLF_VARIANT = register("wolf/variant", paramBuilder -> paramBuilder.persistent(WolfVariant.CODEC).networkSynchronized(WolfVariant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 403 */     WOLF_SOUND_VARIANT = register("wolf/sound_variant", paramBuilder -> paramBuilder.persistent(WolfSoundVariant.CODEC).networkSynchronized(WolfSoundVariant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 407 */     WOLF_COLLAR = register("wolf/collar", paramBuilder -> paramBuilder.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 411 */     FOX_VARIANT = register("fox/variant", paramBuilder -> paramBuilder.persistent((Codec)Fox.Variant.CODEC).networkSynchronized(Fox.Variant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 415 */     SALMON_SIZE = register("salmon/size", paramBuilder -> paramBuilder.persistent((Codec)Salmon.Variant.CODEC).networkSynchronized(Salmon.Variant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 419 */     PARROT_VARIANT = register("parrot/variant", paramBuilder -> paramBuilder.persistent(Parrot.Variant.CODEC).networkSynchronized(Parrot.Variant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 423 */     TROPICAL_FISH_PATTERN = register("tropical_fish/pattern", paramBuilder -> paramBuilder.persistent(TropicalFish.Pattern.CODEC).networkSynchronized(TropicalFish.Pattern.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 427 */     TROPICAL_FISH_BASE_COLOR = register("tropical_fish/base_color", paramBuilder -> paramBuilder.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 431 */     TROPICAL_FISH_PATTERN_COLOR = register("tropical_fish/pattern_color", paramBuilder -> paramBuilder.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 435 */     MOOSHROOM_VARIANT = register("mooshroom/variant", paramBuilder -> paramBuilder.persistent(MushroomCow.Variant.CODEC).networkSynchronized(MushroomCow.Variant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 439 */     RABBIT_VARIANT = register("rabbit/variant", paramBuilder -> paramBuilder.persistent(Rabbit.Variant.CODEC).networkSynchronized(Rabbit.Variant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 443 */     PIG_VARIANT = register("pig/variant", paramBuilder -> paramBuilder.persistent(PigVariant.CODEC).networkSynchronized(PigVariant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 447 */     COW_VARIANT = register("cow/variant", paramBuilder -> paramBuilder.persistent(CowVariant.CODEC).networkSynchronized(CowVariant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 451 */     CHICKEN_VARIANT = register("chicken/variant", paramBuilder -> paramBuilder.persistent(EitherHolder.codec(Registries.CHICKEN_VARIANT, ChickenVariant.CODEC)).networkSynchronized(EitherHolder.streamCodec(Registries.CHICKEN_VARIANT, ChickenVariant.STREAM_CODEC)));
/*     */ 
/*     */ 
/*     */     
/* 455 */     ZOMBIE_NAUTILUS_VARIANT = register("zombie_nautilus/variant", paramBuilder -> paramBuilder.persistent(EitherHolder.codec(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.CODEC)).networkSynchronized(EitherHolder.streamCodec(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.STREAM_CODEC)));
/*     */ 
/*     */ 
/*     */     
/* 459 */     FROG_VARIANT = register("frog/variant", paramBuilder -> paramBuilder.persistent(FrogVariant.CODEC).networkSynchronized(FrogVariant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 463 */     HORSE_VARIANT = register("horse/variant", paramBuilder -> paramBuilder.persistent(Variant.CODEC).networkSynchronized(Variant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 467 */     PAINTING_VARIANT = register("painting/variant", paramBuilder -> paramBuilder.persistent(PaintingVariant.CODEC).networkSynchronized(PaintingVariant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 471 */     LLAMA_VARIANT = register("llama/variant", paramBuilder -> paramBuilder.persistent(Llama.Variant.CODEC).networkSynchronized(Llama.Variant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 475 */     AXOLOTL_VARIANT = register("axolotl/variant", paramBuilder -> paramBuilder.persistent(Axolotl.Variant.CODEC).networkSynchronized(Axolotl.Variant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 479 */     CAT_VARIANT = register("cat/variant", paramBuilder -> paramBuilder.persistent(CatVariant.CODEC).networkSynchronized(CatVariant.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 483 */     CAT_COLLAR = register("cat/collar", paramBuilder -> paramBuilder.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 487 */     SHEEP_COLOR = register("sheep/color", paramBuilder -> paramBuilder.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */     
/* 491 */     SHULKER_COLOR = register("shulker/color", paramBuilder -> paramBuilder.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/*     */   }
/*     */   public static final DataComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT; public static final DataComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT; public static final DataComponentType<ArmorTrim> TRIM; public static final DataComponentType<DebugStickState> DEBUG_STICK_STATE; public static final DataComponentType<TypedEntityData<EntityType<?>>> ENTITY_DATA; public static final DataComponentType<CustomData> BUCKET_ENTITY_DATA; public static final DataComponentType<TypedEntityData<BlockEntityType<?>>> BLOCK_ENTITY_DATA; public static final DataComponentType<InstrumentComponent> INSTRUMENT; public static final DataComponentType<ProvidesTrimMaterial> PROVIDES_TRIM_MATERIAL; public static final DataComponentType<OminousBottleAmplifier> OMINOUS_BOTTLE_AMPLIFIER; public static final DataComponentType<JukeboxPlayable> JUKEBOX_PLAYABLE; public static final DataComponentType<TagKey<BannerPattern>> PROVIDES_BANNER_PATTERNS; public static final DataComponentType<List<ResourceKey<Recipe<?>>>> RECIPES; public static final DataComponentType<LodestoneTracker> LODESTONE_TRACKER; public static final DataComponentType<FireworkExplosion> FIREWORK_EXPLOSION; public static final DataComponentType<Fireworks> FIREWORKS; public static final DataComponentType<ResolvableProfile> PROFILE; public static final DataComponentType<Identifier> NOTE_BLOCK_SOUND; public static final DataComponentType<BannerPatternLayers> BANNER_PATTERNS; public static final DataComponentType<DyeColor> BASE_COLOR; public static final DataComponentType<PotDecorations> POT_DECORATIONS; public static final DataComponentType<ItemContainerContents> CONTAINER; public static final DataComponentType<BlockItemStateProperties> BLOCK_STATE; public static final DataComponentType<Bees> BEES;
/*     */   public static final DataComponentType<LockCode> LOCK;
/*     */   public static final DataComponentType<SeededContainerLoot> CONTAINER_LOOT;
/* 496 */   public static final DataComponentMap COMMON_ITEM_COMPONENTS = DataComponentMap.builder()
/* 497 */     .<Integer>set(MAX_STACK_SIZE, Integer.valueOf(64))
/* 498 */     .<ItemLore>set(LORE, ItemLore.EMPTY)
/* 499 */     .<ItemEnchantments>set(ENCHANTMENTS, ItemEnchantments.EMPTY)
/* 500 */     .<Integer>set(REPAIR_COST, Integer.valueOf(0))
/* 501 */     .<UseEffects>set(USE_EFFECTS, UseEffects.DEFAULT)
/* 502 */     .<ItemAttributeModifiers>set(ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)
/* 503 */     .<Rarity>set(RARITY, Rarity.COMMON)
/* 504 */     .set(BREAK_SOUND, SoundEvents.ITEM_BREAK)
/* 505 */     .<TooltipDisplay>set(TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT)
/* 506 */     .<SwingAnimation>set(SWING_ANIMATION, SwingAnimation.DEFAULT)
/* 507 */     .build(); public static final DataComponentType<Holder<SoundEvent>> BREAK_SOUND; public static final DataComponentType<Holder<VillagerType>> VILLAGER_VARIANT; public static final DataComponentType<Holder<WolfVariant>> WOLF_VARIANT; public static final DataComponentType<Holder<WolfSoundVariant>> WOLF_SOUND_VARIANT; public static final DataComponentType<DyeColor> WOLF_COLLAR; public static final DataComponentType<Fox.Variant> FOX_VARIANT; public static final DataComponentType<Salmon.Variant> SALMON_SIZE; public static final DataComponentType<Parrot.Variant> PARROT_VARIANT; public static final DataComponentType<TropicalFish.Pattern> TROPICAL_FISH_PATTERN; public static final DataComponentType<DyeColor> TROPICAL_FISH_BASE_COLOR; public static final DataComponentType<DyeColor> TROPICAL_FISH_PATTERN_COLOR; public static final DataComponentType<MushroomCow.Variant> MOOSHROOM_VARIANT; public static final DataComponentType<Rabbit.Variant> RABBIT_VARIANT; public static final DataComponentType<Holder<PigVariant>> PIG_VARIANT; public static final DataComponentType<Holder<CowVariant>> COW_VARIANT; public static final DataComponentType<EitherHolder<ChickenVariant>> CHICKEN_VARIANT; public static final DataComponentType<EitherHolder<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT; public static final DataComponentType<Holder<FrogVariant>> FROG_VARIANT; public static final DataComponentType<Variant> HORSE_VARIANT; public static final DataComponentType<Holder<PaintingVariant>> PAINTING_VARIANT; public static final DataComponentType<Llama.Variant> LLAMA_VARIANT; public static final DataComponentType<Axolotl.Variant> AXOLOTL_VARIANT; public static final DataComponentType<Holder<CatVariant>> CAT_VARIANT; public static final DataComponentType<DyeColor> CAT_COLLAR; public static final DataComponentType<DyeColor> SHEEP_COLOR; public static final DataComponentType<DyeColor> SHULKER_COLOR;
/*     */   
/*     */   public static DataComponentType<?> bootstrap(Registry<DataComponentType<?>> paramRegistry) {
/* 510 */     return CUSTOM_DATA;
/*     */   }
/*     */   
/*     */   private static <T> DataComponentType<T> register(String paramString, UnaryOperator<DataComponentType.Builder<T>> paramUnaryOperator) {
/* 514 */     return (DataComponentType<T>)Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, paramString, ((DataComponentType.Builder)paramUnaryOperator.apply(DataComponentType.builder())).build());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\DataComponents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */