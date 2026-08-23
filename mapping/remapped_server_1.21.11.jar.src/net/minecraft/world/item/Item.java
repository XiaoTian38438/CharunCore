/*     */ package net.minecraft.world.item;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.inventory.ClickAction;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.item.component.Consumable;
/*     */ import net.minecraft.world.item.component.Consumables;
/*     */ import net.minecraft.world.item.component.DamageResistant;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.item.component.PiercingWeapon;
/*     */ import net.minecraft.world.item.component.ProvidesTrimMaterial;
/*     */ import net.minecraft.world.item.component.SwingAnimation;
/*     */ import net.minecraft.world.item.component.Tool;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.component.UseCooldown;
/*     */ import net.minecraft.world.item.component.UseEffects;
/*     */ import net.minecraft.world.item.component.UseRemainder;
/*     */ import net.minecraft.world.item.component.Weapon;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.item.enchantment.Enchantable;
/*     */ import net.minecraft.world.item.enchantment.Repairable;
/*     */ import net.minecraft.world.item.equipment.ArmorMaterial;
/*     */ import net.minecraft.world.item.equipment.ArmorType;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Item implements FeatureElement, ItemLike {
/*     */   public static final Codec<Holder<Item>> CODEC;
/*     */   
/*     */   static {
/* 100 */     CODEC = BuiltInRegistries.ITEM.holderByNameCodec().validate(paramHolder -> paramHolder.is((Holder)Items.AIR.builtInRegistryHolder()) ? DataResult.error(()) : DataResult.success(paramHolder));
/*     */   }
/*     */   
/* 103 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Item>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ITEM);
/*     */   
/* 105 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 106 */   public static final Map<Block, Item> BY_BLOCK = Maps.newHashMap();
/*     */   
/* 108 */   public static final Identifier BASE_ATTACK_DAMAGE_ID = Identifier.withDefaultNamespace("base_attack_damage");
/* 109 */   public static final Identifier BASE_ATTACK_SPEED_ID = Identifier.withDefaultNamespace("base_attack_speed");
/*     */   
/*     */   public static final int DEFAULT_MAX_STACK_SIZE = 64;
/*     */   
/*     */   public static final int ABSOLUTE_MAX_STACK_SIZE = 99;
/*     */   
/*     */   public static final int MAX_BAR_WIDTH = 13;
/*     */   
/*     */   protected static final int APPROXIMATELY_INFINITE_USE_DURATION = 72000;
/*     */   
/* 119 */   private final Holder.Reference<Item> builtInRegistryHolder = BuiltInRegistries.ITEM.createIntrusiveHolder(this); private final DataComponentMap components; private final Item craftingRemainingItem; protected final String descriptionId; private final FeatureFlagSet requiredFeatures;
/*     */   
/*     */   public static int getId(Item paramItem) {
/* 122 */     return (paramItem == null) ? 0 : BuiltInRegistries.ITEM.getId(paramItem);
/*     */   }
/*     */   
/*     */   public static Item byId(int paramInt) {
/* 126 */     return (Item)BuiltInRegistries.ITEM.byId(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Item byBlock(Block paramBlock) {
/* 132 */     return BY_BLOCK.getOrDefault(paramBlock, Items.AIR);
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
/*     */   public Item(Properties paramProperties) {
/* 144 */     this.descriptionId = paramProperties.effectiveDescriptionId();
/* 145 */     this.components = paramProperties.buildAndValidateComponents((Component)Component.translatable(this.descriptionId), paramProperties.effectiveModel());
/* 146 */     this.craftingRemainingItem = paramProperties.craftingRemainingItem;
/* 147 */     this.requiredFeatures = paramProperties.requiredFeatures;
/*     */     
/* 149 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 150 */       String str = getClass().getSimpleName();
/* 151 */       if (!str.endsWith("Item")) {
/* 152 */         LOGGER.error("Item classes should end with Item and {} doesn't.", str);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Holder.Reference<Item> builtInRegistryHolder() {
/* 162 */     return this.builtInRegistryHolder;
/*     */   }
/*     */   
/*     */   public DataComponentMap components() {
/* 166 */     return this.components;
/*     */   }
/*     */   
/*     */   public int getDefaultMaxStackSize() {
/* 170 */     return ((Integer)this.components.getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue();
/*     */   }
/*     */   public static class Properties { private static final DependantName<Item, String> BLOCK_DESCRIPTION_ID; private static final DependantName<Item, String> ITEM_DESCRIPTION_ID;
/*     */     static {
/* 174 */       BLOCK_DESCRIPTION_ID = (param1ResourceKey -> Util.makeDescriptionId("block", param1ResourceKey.identifier()));
/* 175 */       ITEM_DESCRIPTION_ID = (param1ResourceKey -> Util.makeDescriptionId("item", param1ResourceKey.identifier()));
/*     */     }
/* 177 */     private final DataComponentMap.Builder components = DataComponentMap.builder().addAll(DataComponents.COMMON_ITEM_COMPONENTS);
/*     */     Item craftingRemainingItem;
/* 179 */     FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */     private ResourceKey<Item> id;
/* 181 */     private DependantName<Item, String> descriptionId = ITEM_DESCRIPTION_ID;
/* 182 */     private final DependantName<Item, Identifier> model = ResourceKey::identifier;
/*     */     
/*     */     public Properties food(FoodProperties param1FoodProperties) {
/* 185 */       return food(param1FoodProperties, Consumables.DEFAULT_FOOD);
/*     */     }
/*     */     
/*     */     public Properties food(FoodProperties param1FoodProperties, Consumable param1Consumable) {
/* 189 */       return component(DataComponents.FOOD, param1FoodProperties).component(DataComponents.CONSUMABLE, param1Consumable);
/*     */     }
/*     */     
/*     */     public Properties usingConvertsTo(Item param1Item) {
/* 193 */       return component(DataComponents.USE_REMAINDER, new UseRemainder(new ItemStack(param1Item)));
/*     */     }
/*     */     
/*     */     public Properties useCooldown(float param1Float) {
/* 197 */       return component(DataComponents.USE_COOLDOWN, new UseCooldown(param1Float));
/*     */     }
/*     */     
/*     */     public Properties stacksTo(int param1Int) {
/* 201 */       return component(DataComponents.MAX_STACK_SIZE, Integer.valueOf(param1Int));
/*     */     }
/*     */     
/*     */     public Properties durability(int param1Int) {
/* 205 */       component(DataComponents.MAX_DAMAGE, Integer.valueOf(param1Int));
/* 206 */       component(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1));
/* 207 */       component(DataComponents.DAMAGE, Integer.valueOf(0));
/* 208 */       return this;
/*     */     }
/*     */     
/*     */     public Properties craftRemainder(Item param1Item) {
/* 212 */       this.craftingRemainingItem = param1Item;
/* 213 */       return this;
/*     */     }
/*     */     
/*     */     public Properties rarity(Rarity param1Rarity) {
/* 217 */       return component(DataComponents.RARITY, param1Rarity);
/*     */     }
/*     */     
/*     */     public Properties fireResistant() {
/* 221 */       return component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_FIRE));
/*     */     }
/*     */     
/*     */     public Properties jukeboxPlayable(ResourceKey<JukeboxSong> param1ResourceKey) {
/* 225 */       return component(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder<>(param1ResourceKey)));
/*     */     }
/*     */     
/*     */     public Properties enchantable(int param1Int) {
/* 229 */       return component(DataComponents.ENCHANTABLE, new Enchantable(param1Int));
/*     */     }
/*     */     
/*     */     public Properties repairable(Item param1Item) {
/* 233 */       return component(DataComponents.REPAIRABLE, new Repairable((HolderSet)HolderSet.direct(new Holder[] { (Holder)param1Item.builtInRegistryHolder() })));
/*     */     }
/*     */ 
/*     */     
/*     */     public Properties repairable(TagKey<Item> param1TagKey) {
/* 238 */       HolderGetter holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ITEM);
/* 239 */       return component(DataComponents.REPAIRABLE, new Repairable((HolderSet)holderGetter.getOrThrow(param1TagKey)));
/*     */     }
/*     */     
/*     */     public Properties equippable(EquipmentSlot param1EquipmentSlot) {
/* 243 */       return component(DataComponents.EQUIPPABLE, Equippable.builder(param1EquipmentSlot).build());
/*     */     }
/*     */     
/*     */     public Properties equippableUnswappable(EquipmentSlot param1EquipmentSlot) {
/* 247 */       return component(DataComponents.EQUIPPABLE, Equippable.builder(param1EquipmentSlot).setSwappable(false).build());
/*     */     }
/*     */     
/*     */     public Properties tool(ToolMaterial param1ToolMaterial, TagKey<Block> param1TagKey, float param1Float1, float param1Float2, float param1Float3) {
/* 251 */       return param1ToolMaterial.applyToolProperties(this, param1TagKey, param1Float1, param1Float2, param1Float3);
/*     */     }
/*     */     
/*     */     public Properties pickaxe(ToolMaterial param1ToolMaterial, float param1Float1, float param1Float2) {
/* 255 */       return tool(param1ToolMaterial, BlockTags.MINEABLE_WITH_PICKAXE, param1Float1, param1Float2, 0.0F);
/*     */     }
/*     */     
/*     */     public Properties axe(ToolMaterial param1ToolMaterial, float param1Float1, float param1Float2) {
/* 259 */       return tool(param1ToolMaterial, BlockTags.MINEABLE_WITH_AXE, param1Float1, param1Float2, 5.0F);
/*     */     }
/*     */     
/*     */     public Properties hoe(ToolMaterial param1ToolMaterial, float param1Float1, float param1Float2) {
/* 263 */       return tool(param1ToolMaterial, BlockTags.MINEABLE_WITH_HOE, param1Float1, param1Float2, 0.0F);
/*     */     }
/*     */     
/*     */     public Properties shovel(ToolMaterial param1ToolMaterial, float param1Float1, float param1Float2) {
/* 267 */       return tool(param1ToolMaterial, BlockTags.MINEABLE_WITH_SHOVEL, param1Float1, param1Float2, 0.0F);
/*     */     }
/*     */     
/*     */     public Properties sword(ToolMaterial param1ToolMaterial, float param1Float1, float param1Float2) {
/* 271 */       return param1ToolMaterial.applySwordProperties(this, param1Float1, param1Float2);
/*     */     }
/*     */     
/*     */     public Properties spear(ToolMaterial param1ToolMaterial, float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7, float param1Float8, float param1Float9) {
/* 275 */       return durability(param1ToolMaterial.durability())
/* 276 */         .repairable(param1ToolMaterial.repairItems())
/* 277 */         .enchantable(param1ToolMaterial.enchantmentValue())
/* 278 */         .component(DataComponents.DAMAGE_TYPE, new EitherHolder(DamageTypes.SPEAR))
/* 279 */         .<KineticWeapon>component(DataComponents.KINETIC_WEAPON, new KineticWeapon(10, (int)(param1Float3 * 20.0F), 
/*     */ 
/*     */             
/* 282 */             KineticWeapon.Condition.ofAttackerSpeed((int)(param1Float4 * 20.0F), param1Float5), 
/* 283 */             KineticWeapon.Condition.ofAttackerSpeed((int)(param1Float6 * 20.0F), param1Float7), 
/* 284 */             KineticWeapon.Condition.ofRelativeSpeed((int)(param1Float8 * 20.0F), param1Float9), 0.38F, param1Float2, 
/*     */ 
/*     */             
/* 287 */             Optional.of((param1ToolMaterial == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_USE : SoundEvents.SPEAR_USE), 
/* 288 */             Optional.of((param1ToolMaterial == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)))
/*     */         
/* 290 */         .<PiercingWeapon>component(DataComponents.PIERCING_WEAPON, new PiercingWeapon(true, false, 
/*     */ 
/*     */             
/* 293 */             Optional.of((param1ToolMaterial == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_ATTACK : SoundEvents.SPEAR_ATTACK), 
/* 294 */             Optional.of((param1ToolMaterial == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)))
/*     */         
/* 296 */         .<AttackRange>component(DataComponents.ATTACK_RANGE, new AttackRange(2.0F, 4.5F, 2.0F, 6.5F, 0.125F, 0.5F))
/* 297 */         .<Float>component(DataComponents.MINIMUM_ATTACK_CHARGE, Float.valueOf(1.0F))
/* 298 */         .<SwingAnimation>component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int)(param1Float1 * 20.0F)))
/*     */ 
/*     */ 
/*     */         
/* 302 */         .attributes(ItemAttributeModifiers.builder()
/* 303 */           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (0.0F + param1ToolMaterial.attackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 304 */           .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, (1.0F / param1Float1) - 4.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 305 */           .build())
/* 306 */         .<UseEffects>component(DataComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
/* 307 */         .component(DataComponents.WEAPON, new Weapon(1));
/*     */     }
/*     */     
/*     */     public Properties spawnEgg(EntityType<?> param1EntityType) {
/* 311 */       return component(DataComponents.ENTITY_DATA, TypedEntityData.of(param1EntityType, new CompoundTag()));
/*     */     }
/*     */     
/*     */     public Properties humanoidArmor(ArmorMaterial param1ArmorMaterial, ArmorType param1ArmorType) {
/* 315 */       return durability(param1ArmorType.getDurability(param1ArmorMaterial.durability()))
/* 316 */         .attributes(param1ArmorMaterial.createAttributes(param1ArmorType))
/* 317 */         .enchantable(param1ArmorMaterial.enchantmentValue())
/* 318 */         .<Equippable>component(DataComponents.EQUIPPABLE, Equippable.builder(param1ArmorType.getSlot()).setEquipSound(param1ArmorMaterial.equipSound()).setAsset(param1ArmorMaterial.assetId()).build())
/* 319 */         .repairable(param1ArmorMaterial.repairIngredient());
/*     */     }
/*     */     
/*     */     public Properties wolfArmor(ArmorMaterial param1ArmorMaterial) {
/* 323 */       return durability(ArmorType.BODY.getDurability(param1ArmorMaterial.durability()))
/* 324 */         .attributes(param1ArmorMaterial.createAttributes(ArmorType.BODY))
/* 325 */         .repairable(param1ArmorMaterial.repairIngredient())
/* 326 */         .<Equippable>component(DataComponents.EQUIPPABLE, 
/* 327 */           Equippable.builder(EquipmentSlot.BODY)
/* 328 */           .setEquipSound(param1ArmorMaterial.equipSound())
/* 329 */           .setAsset(param1ArmorMaterial.assetId())
/* 330 */           .setAllowedEntities((HolderSet)HolderSet.direct(new Holder[] { (Holder)EntityType.WOLF.builtInRegistryHolder()
/* 331 */               })).setCanBeSheared(true)
/* 332 */           .setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_UNEQUIP_WOLF))
/* 333 */           .build())
/*     */         
/* 335 */         .<Holder.Reference>component(DataComponents.BREAK_SOUND, SoundEvents.WOLF_ARMOR_BREAK)
/* 336 */         .stacksTo(1);
/*     */     }
/*     */     
/*     */     public Properties horseArmor(ArmorMaterial param1ArmorMaterial) {
/* 340 */       HolderGetter holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ENTITY_TYPE);
/* 341 */       return attributes(param1ArmorMaterial.createAttributes(ArmorType.BODY))
/* 342 */         .<Equippable>component(DataComponents.EQUIPPABLE, 
/* 343 */           Equippable.builder(EquipmentSlot.BODY)
/* 344 */           .setEquipSound((Holder)SoundEvents.HORSE_ARMOR)
/* 345 */           .setAsset(param1ArmorMaterial.assetId())
/* 346 */           .setAllowedEntities((HolderSet)holderGetter.getOrThrow(EntityTypeTags.CAN_WEAR_HORSE_ARMOR))
/* 347 */           .setDamageOnHurt(false)
/* 348 */           .setCanBeSheared(true)
/* 349 */           .setShearingSound((Holder)SoundEvents.HORSE_ARMOR_UNEQUIP)
/* 350 */           .build())
/*     */         
/* 352 */         .stacksTo(1);
/*     */     }
/*     */     
/*     */     public Properties nautilusArmor(ArmorMaterial param1ArmorMaterial) {
/* 356 */       HolderGetter holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ENTITY_TYPE);
/* 357 */       return attributes(param1ArmorMaterial.createAttributes(ArmorType.BODY))
/* 358 */         .<Equippable>component(DataComponents.EQUIPPABLE, 
/* 359 */           Equippable.builder(EquipmentSlot.BODY)
/* 360 */           .setEquipSound((Holder)SoundEvents.ARMOR_EQUIP_NAUTILUS)
/* 361 */           .setAsset(param1ArmorMaterial.assetId())
/* 362 */           .setAllowedEntities((HolderSet)holderGetter.getOrThrow(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR))
/* 363 */           .setDamageOnHurt(false)
/* 364 */           .setEquipOnInteract(true)
/* 365 */           .setCanBeSheared(true)
/* 366 */           .setShearingSound((Holder)SoundEvents.ARMOR_UNEQUIP_NAUTILUS)
/* 367 */           .build())
/*     */         
/* 369 */         .stacksTo(1);
/*     */     }
/*     */     
/*     */     public Properties trimMaterial(ResourceKey<TrimMaterial> param1ResourceKey) {
/* 373 */       return component(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(param1ResourceKey));
/*     */     }
/*     */     
/*     */     public Properties requiredFeatures(FeatureFlag... param1VarArgs) {
/* 377 */       this.requiredFeatures = FeatureFlags.REGISTRY.subset(param1VarArgs);
/* 378 */       return this;
/*     */     }
/*     */     
/*     */     public Properties setId(ResourceKey<Item> param1ResourceKey) {
/* 382 */       this.id = param1ResourceKey;
/* 383 */       return this;
/*     */     }
/*     */     
/*     */     public Properties overrideDescription(String param1String) {
/* 387 */       this.descriptionId = DependantName.fixed(param1String);
/* 388 */       return this;
/*     */     }
/*     */     
/*     */     public Properties useBlockDescriptionPrefix() {
/* 392 */       this.descriptionId = BLOCK_DESCRIPTION_ID;
/* 393 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Properties useItemDescriptionPrefix() {
/* 400 */       this.descriptionId = ITEM_DESCRIPTION_ID;
/* 401 */       return this;
/*     */     }
/*     */     
/*     */     protected String effectiveDescriptionId() {
/* 405 */       return (String)this.descriptionId.get(Objects.<ResourceKey>requireNonNull(this.id, "Item id not set"));
/*     */     }
/*     */     
/*     */     public Identifier effectiveModel() {
/* 409 */       return (Identifier)this.model.get(Objects.<ResourceKey>requireNonNull(this.id, "Item id not set"));
/*     */     }
/*     */     
/*     */     public <T> Properties component(DataComponentType<T> param1DataComponentType, T param1T) {
/* 413 */       this.components.set(param1DataComponentType, param1T);
/* 414 */       return this;
/*     */     }
/*     */     
/*     */     public Properties attributes(ItemAttributeModifiers param1ItemAttributeModifiers) {
/* 418 */       return component(DataComponents.ATTRIBUTE_MODIFIERS, param1ItemAttributeModifiers);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     DataComponentMap buildAndValidateComponents(Component param1Component, Identifier param1Identifier) {
/* 425 */       DataComponentMap dataComponentMap = this.components.set(DataComponents.ITEM_NAME, param1Component).set(DataComponents.ITEM_MODEL, param1Identifier).build();
/*     */       
/* 427 */       if (dataComponentMap.has(DataComponents.DAMAGE) && ((Integer)dataComponentMap.getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue() > 1) {
/* 428 */         throw new IllegalStateException("Item cannot have both durability and be stackable");
/*     */       }
/* 430 */       return dataComponentMap;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUseTick(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack, int paramInt) {}
/*     */ 
/*     */   
/*     */   public void onDestroyed(ItemEntity paramItemEntity) {}
/*     */   
/*     */   public boolean canDestroyBlock(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, LivingEntity paramLivingEntity) {
/* 441 */     Tool tool = (Tool)paramItemStack.get(DataComponents.TOOL);
/* 442 */     if (tool != null && !tool.canDestroyBlocksInCreative())
/* 443 */     { if (paramLivingEntity instanceof Player) { Player player = (Player)paramLivingEntity; if (!(player.getAbilities()).instabuild); return false; }
/*     */        }
/* 445 */     else { return true; }
/*     */   
/*     */   }
/*     */   
/*     */   public Item asItem() {
/* 450 */     return this;
/*     */   }
/*     */   
/*     */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 454 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public float getDestroySpeed(ItemStack paramItemStack, BlockState paramBlockState) {
/* 458 */     Tool tool = (Tool)paramItemStack.get(DataComponents.TOOL);
/* 459 */     return (tool != null) ? tool.getMiningSpeed(paramBlockState) : 1.0F;
/*     */   }
/*     */   
/*     */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 463 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*     */     
/* 465 */     Consumable consumable = (Consumable)itemStack.get(DataComponents.CONSUMABLE);
/* 466 */     if (consumable != null) {
/* 467 */       return consumable.startConsuming((LivingEntity)paramPlayer, itemStack, paramInteractionHand);
/*     */     }
/*     */     
/* 470 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 471 */     if (equippable != null && equippable.swappable()) {
/* 472 */       return equippable.swapWithEquipmentSlot(itemStack, paramPlayer);
/*     */     }
/*     */     
/* 475 */     if (itemStack.has(DataComponents.BLOCKS_ATTACKS)) {
/* 476 */       paramPlayer.startUsingItem(paramInteractionHand);
/* 477 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/*     */     
/* 480 */     KineticWeapon kineticWeapon = (KineticWeapon)itemStack.get(DataComponents.KINETIC_WEAPON);
/* 481 */     if (kineticWeapon != null) {
/* 482 */       paramPlayer.startUsingItem(paramInteractionHand);
/* 483 */       kineticWeapon.makeSound((Entity)paramPlayer);
/* 484 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/*     */     
/* 487 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public ItemStack finishUsingItem(ItemStack paramItemStack, Level paramLevel, LivingEntity paramLivingEntity) {
/* 491 */     Consumable consumable = (Consumable)paramItemStack.get(DataComponents.CONSUMABLE);
/* 492 */     if (consumable != null) {
/* 493 */       return consumable.onConsume(paramLevel, paramLivingEntity, paramItemStack);
/*     */     }
/* 495 */     return paramItemStack;
/*     */   }
/*     */   
/*     */   public boolean isBarVisible(ItemStack paramItemStack) {
/* 499 */     return paramItemStack.isDamaged();
/*     */   }
/*     */   
/*     */   public int getBarWidth(ItemStack paramItemStack) {
/* 503 */     return Mth.clamp(Math.round(13.0F - paramItemStack.getDamageValue() * 13.0F / paramItemStack.getMaxDamage()), 0, 13);
/*     */   }
/*     */   
/*     */   public int getBarColor(ItemStack paramItemStack) {
/* 507 */     int i = paramItemStack.getMaxDamage();
/* 508 */     float f = Math.max(0.0F, (i - paramItemStack.getDamageValue()) / i);
/*     */ 
/*     */     
/* 511 */     return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean overrideStackedOnOther(ItemStack paramItemStack, Slot paramSlot, ClickAction paramClickAction, Player paramPlayer) {
/* 518 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean overrideOtherStackedOnMe(ItemStack paramItemStack1, ItemStack paramItemStack2, Slot paramSlot, ClickAction paramClickAction, Player paramPlayer, SlotAccess paramSlotAccess) {
/* 525 */     return false;
/*     */   }
/*     */   
/*     */   public float getAttackDamageBonus(Entity paramEntity, float paramFloat, DamageSource paramDamageSource) {
/* 529 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DamageSource getItemDamageSource(LivingEntity paramLivingEntity) {
/* 537 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void hurtEnemy(ItemStack paramItemStack, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void postHurtEnemy(ItemStack paramItemStack, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {}
/*     */ 
/*     */   
/*     */   public boolean mineBlock(ItemStack paramItemStack, Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, LivingEntity paramLivingEntity) {
/* 550 */     Tool tool = (Tool)paramItemStack.get(DataComponents.TOOL);
/* 551 */     if (tool == null) {
/* 552 */       return false;
/*     */     }
/*     */     
/* 555 */     if (!paramLevel.isClientSide() && paramBlockState.getDestroySpeed((BlockGetter)paramLevel, paramBlockPos) != 0.0F && 
/* 556 */       tool.damagePerBlock() > 0) {
/* 557 */       paramItemStack.hurtAndBreak(tool.damagePerBlock(), paramLivingEntity, EquipmentSlot.MAINHAND);
/*     */     }
/*     */     
/* 560 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isCorrectToolForDrops(ItemStack paramItemStack, BlockState paramBlockState) {
/* 564 */     Tool tool = (Tool)paramItemStack.get(DataComponents.TOOL);
/* 565 */     return (tool != null && tool.isCorrectForDrops(paramBlockState));
/*     */   }
/*     */   
/*     */   public InteractionResult interactLivingEntity(ItemStack paramItemStack, Player paramPlayer, LivingEntity paramLivingEntity, InteractionHand paramInteractionHand) {
/* 569 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 574 */     return BuiltInRegistries.ITEM.wrapAsHolder(this).getRegisteredName();
/*     */   }
/*     */   
/*     */   public final ItemStack getCraftingRemainder() {
/* 578 */     return (this.craftingRemainingItem == null) ? ItemStack.EMPTY : new ItemStack(this.craftingRemainingItem);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void inventoryTick(ItemStack paramItemStack, ServerLevel paramServerLevel, Entity paramEntity, EquipmentSlot paramEquipmentSlot) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCraftedBy(ItemStack paramItemStack, Player paramPlayer) {
/* 588 */     onCraftedPostProcess(paramItemStack, paramPlayer.level());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftedPostProcess(ItemStack paramItemStack, Level paramLevel) {}
/*     */   
/*     */   public ItemUseAnimation getUseAnimation(ItemStack paramItemStack) {
/* 595 */     Consumable consumable = (Consumable)paramItemStack.get(DataComponents.CONSUMABLE);
/* 596 */     if (consumable != null) {
/* 597 */       return consumable.animation();
/*     */     }
/* 599 */     if (paramItemStack.has(DataComponents.BLOCKS_ATTACKS)) {
/* 600 */       return ItemUseAnimation.BLOCK;
/*     */     }
/* 602 */     if (paramItemStack.has(DataComponents.KINETIC_WEAPON)) {
/* 603 */       return ItemUseAnimation.SPEAR;
/*     */     }
/* 605 */     return ItemUseAnimation.NONE;
/*     */   }
/*     */   
/*     */   public int getUseDuration(ItemStack paramItemStack, LivingEntity paramLivingEntity) {
/* 609 */     Consumable consumable = (Consumable)paramItemStack.get(DataComponents.CONSUMABLE);
/* 610 */     if (consumable != null) {
/* 611 */       return consumable.consumeTicks();
/*     */     }
/* 613 */     if (paramItemStack.has(DataComponents.BLOCKS_ATTACKS) || paramItemStack.has(DataComponents.KINETIC_WEAPON)) {
/* 614 */       return 72000;
/*     */     }
/* 616 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean releaseUsing(ItemStack paramItemStack, Level paramLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 623 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void appendHoverText(ItemStack paramItemStack, TooltipContext paramTooltipContext, TooltipDisplay paramTooltipDisplay, Consumer<Component> paramConsumer, TooltipFlag paramTooltipFlag) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<TooltipComponent> getTooltipImage(ItemStack paramItemStack) {
/* 634 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public final String getDescriptionId() {
/* 639 */     return this.descriptionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Component getName() {
/* 646 */     return (Component)this.components.getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
/*     */   }
/*     */   
/*     */   public Component getName(ItemStack paramItemStack) {
/* 650 */     return (Component)paramItemStack.getComponents().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
/*     */   }
/*     */   
/*     */   public boolean isFoil(ItemStack paramItemStack) {
/* 654 */     return paramItemStack.isEnchanted();
/*     */   }
/*     */   
/*     */   protected static BlockHitResult getPlayerPOVHitResult(Level paramLevel, Player paramPlayer, ClipContext.Fluid paramFluid) {
/* 658 */     Vec3 vec31 = paramPlayer.getEyePosition();
/*     */     
/* 660 */     Vec3 vec32 = vec31.add(paramPlayer.calculateViewVector(paramPlayer.getXRot(), paramPlayer.getYRot()).scale(paramPlayer.blockInteractionRange()));
/*     */     
/* 662 */     return paramLevel.clip(new ClipContext(vec31, vec32, ClipContext.Block.OUTLINE, paramFluid, (Entity)paramPlayer));
/*     */   }
/*     */   
/*     */   public boolean useOnRelease(ItemStack paramItemStack) {
/* 666 */     return false;
/*     */   }
/*     */   
/*     */   public ItemStack getDefaultInstance() {
/* 670 */     return new ItemStack(this);
/*     */   }
/*     */   
/*     */   public boolean canFitInsideContainerItems() {
/* 674 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public FeatureFlagSet requiredFeatures() {
/* 679 */     return this.requiredFeatures;
/*     */   }
/*     */   
/*     */   public boolean shouldPrintOpWarning(ItemStack paramItemStack, Player paramPlayer) {
/* 683 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class null
/*     */     implements TooltipContext
/*     */   {
/*     */     public HolderLookup.Provider registries() {
/* 695 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public float tickRate() {
/* 700 */       return 20.0F;
/*     */     }
/*     */ 
/*     */     
/*     */     public MapItemSavedData mapData(MapId param1MapId) {
/* 705 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isPeaceful()
/*     */     {
/* 710 */       return false; } } public static interface TooltipContext { public static final TooltipContext EMPTY = new TooltipContext() { public HolderLookup.Provider registries() { return null; } public float tickRate() { return 20.0F; } public MapItemSavedData mapData(MapId param2MapId) { return null; } public boolean isPeaceful() { return false; }
/*     */          }
/*     */     ;
/*     */     
/*     */     HolderLookup.Provider registries();
/*     */     
/*     */     float tickRate();
/*     */     
/*     */     MapItemSavedData mapData(MapId param1MapId);
/*     */     
/*     */     boolean isPeaceful();
/*     */     
/*     */     static TooltipContext of(final Level level) {
/* 723 */       if (level == null) {
/* 724 */         return EMPTY;
/*     */       }
/*     */       
/* 727 */       return new TooltipContext()
/*     */         {
/*     */           public HolderLookup.Provider registries() {
/* 730 */             return (HolderLookup.Provider)level.registryAccess();
/*     */           }
/*     */ 
/*     */           
/*     */           public float tickRate() {
/* 735 */             return level.tickRateManager().tickrate();
/*     */           }
/*     */ 
/*     */           
/*     */           public MapItemSavedData mapData(MapId param2MapId) {
/* 740 */             return level.getMapData(param2MapId);
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean isPeaceful() {
/* 745 */             return (level.getDifficulty() == Difficulty.PEACEFUL);
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     static TooltipContext of(final HolderLookup.Provider registries) {
/* 751 */       return new TooltipContext()
/*     */         {
/*     */           public HolderLookup.Provider registries() {
/* 754 */             return registries;
/*     */           }
/*     */ 
/*     */           
/*     */           public float tickRate() {
/* 759 */             return 20.0F;
/*     */           }
/*     */ 
/*     */           
/*     */           public MapItemSavedData mapData(MapId param2MapId) {
/* 764 */             return null;
/*     */           }
/*     */           
/*     */           public boolean isPeaceful()
/*     */           {
/* 769 */             return false; } }; } } class null implements TooltipContext { public HolderLookup.Provider registries() { return (HolderLookup.Provider)level.registryAccess(); } public float tickRate() { return level.tickRateManager().tickrate(); } public MapItemSavedData mapData(MapId param1MapId) { return level.getMapData(param1MapId); } public boolean isPeaceful() { return (level.getDifficulty() == Difficulty.PEACEFUL); } } class null implements TooltipContext { public boolean isPeaceful() { return false; }
/*     */ 
/*     */     
/*     */     public HolderLookup.Provider registries() {
/*     */       return registries;
/*     */     }
/*     */     
/*     */     public float tickRate() {
/*     */       return 20.0F;
/*     */     }
/*     */     
/*     */     public MapItemSavedData mapData(MapId param1MapId) {
/*     */       return null;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\Item.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */