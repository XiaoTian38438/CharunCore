/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.item.component.Consumable;
/*     */ import net.minecraft.world.item.component.Consumables;
/*     */ import net.minecraft.world.item.component.DamageResistant;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.item.component.PiercingWeapon;
/*     */ import net.minecraft.world.item.component.ProvidesTrimMaterial;
/*     */ import net.minecraft.world.item.component.SwingAnimation;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.component.UseCooldown;
/*     */ import net.minecraft.world.item.component.UseEffects;
/*     */ import net.minecraft.world.item.component.UseRemainder;
/*     */ import net.minecraft.world.item.component.Weapon;
/*     */ import net.minecraft.world.item.enchantment.Enchantable;
/*     */ import net.minecraft.world.item.enchantment.Repairable;
/*     */ import net.minecraft.world.item.equipment.ArmorMaterial;
/*     */ import net.minecraft.world.item.equipment.ArmorType;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Properties
/*     */ {
/*     */   private static final DependantName<Item, String> BLOCK_DESCRIPTION_ID;
/*     */   private static final DependantName<Item, String> ITEM_DESCRIPTION_ID;
/*     */   
/*     */   static {
/* 174 */     BLOCK_DESCRIPTION_ID = (paramResourceKey -> Util.makeDescriptionId("block", paramResourceKey.identifier()));
/* 175 */     ITEM_DESCRIPTION_ID = (paramResourceKey -> Util.makeDescriptionId("item", paramResourceKey.identifier()));
/*     */   }
/* 177 */   private final DataComponentMap.Builder components = DataComponentMap.builder().addAll(DataComponents.COMMON_ITEM_COMPONENTS);
/*     */   Item craftingRemainingItem;
/* 179 */   FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */   private ResourceKey<Item> id;
/* 181 */   private DependantName<Item, String> descriptionId = ITEM_DESCRIPTION_ID;
/* 182 */   private final DependantName<Item, Identifier> model = ResourceKey::identifier;
/*     */   
/*     */   public Properties food(FoodProperties paramFoodProperties) {
/* 185 */     return food(paramFoodProperties, Consumables.DEFAULT_FOOD);
/*     */   }
/*     */   
/*     */   public Properties food(FoodProperties paramFoodProperties, Consumable paramConsumable) {
/* 189 */     return component(DataComponents.FOOD, paramFoodProperties).component(DataComponents.CONSUMABLE, paramConsumable);
/*     */   }
/*     */   
/*     */   public Properties usingConvertsTo(Item paramItem) {
/* 193 */     return component(DataComponents.USE_REMAINDER, new UseRemainder(new ItemStack(paramItem)));
/*     */   }
/*     */   
/*     */   public Properties useCooldown(float paramFloat) {
/* 197 */     return component(DataComponents.USE_COOLDOWN, new UseCooldown(paramFloat));
/*     */   }
/*     */   
/*     */   public Properties stacksTo(int paramInt) {
/* 201 */     return component(DataComponents.MAX_STACK_SIZE, Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   public Properties durability(int paramInt) {
/* 205 */     component(DataComponents.MAX_DAMAGE, Integer.valueOf(paramInt));
/* 206 */     component(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1));
/* 207 */     component(DataComponents.DAMAGE, Integer.valueOf(0));
/* 208 */     return this;
/*     */   }
/*     */   
/*     */   public Properties craftRemainder(Item paramItem) {
/* 212 */     this.craftingRemainingItem = paramItem;
/* 213 */     return this;
/*     */   }
/*     */   
/*     */   public Properties rarity(Rarity paramRarity) {
/* 217 */     return component(DataComponents.RARITY, paramRarity);
/*     */   }
/*     */   
/*     */   public Properties fireResistant() {
/* 221 */     return component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_FIRE));
/*     */   }
/*     */   
/*     */   public Properties jukeboxPlayable(ResourceKey<JukeboxSong> paramResourceKey) {
/* 225 */     return component(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder<>(paramResourceKey)));
/*     */   }
/*     */   
/*     */   public Properties enchantable(int paramInt) {
/* 229 */     return component(DataComponents.ENCHANTABLE, new Enchantable(paramInt));
/*     */   }
/*     */   
/*     */   public Properties repairable(Item paramItem) {
/* 233 */     return component(DataComponents.REPAIRABLE, new Repairable((HolderSet)HolderSet.direct(new Holder[] { (Holder)paramItem.builtInRegistryHolder() })));
/*     */   }
/*     */ 
/*     */   
/*     */   public Properties repairable(TagKey<Item> paramTagKey) {
/* 238 */     HolderGetter holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ITEM);
/* 239 */     return component(DataComponents.REPAIRABLE, new Repairable((HolderSet)holderGetter.getOrThrow(paramTagKey)));
/*     */   }
/*     */   
/*     */   public Properties equippable(EquipmentSlot paramEquipmentSlot) {
/* 243 */     return component(DataComponents.EQUIPPABLE, Equippable.builder(paramEquipmentSlot).build());
/*     */   }
/*     */   
/*     */   public Properties equippableUnswappable(EquipmentSlot paramEquipmentSlot) {
/* 247 */     return component(DataComponents.EQUIPPABLE, Equippable.builder(paramEquipmentSlot).setSwappable(false).build());
/*     */   }
/*     */   
/*     */   public Properties tool(ToolMaterial paramToolMaterial, TagKey<Block> paramTagKey, float paramFloat1, float paramFloat2, float paramFloat3) {
/* 251 */     return paramToolMaterial.applyToolProperties(this, paramTagKey, paramFloat1, paramFloat2, paramFloat3);
/*     */   }
/*     */   
/*     */   public Properties pickaxe(ToolMaterial paramToolMaterial, float paramFloat1, float paramFloat2) {
/* 255 */     return tool(paramToolMaterial, BlockTags.MINEABLE_WITH_PICKAXE, paramFloat1, paramFloat2, 0.0F);
/*     */   }
/*     */   
/*     */   public Properties axe(ToolMaterial paramToolMaterial, float paramFloat1, float paramFloat2) {
/* 259 */     return tool(paramToolMaterial, BlockTags.MINEABLE_WITH_AXE, paramFloat1, paramFloat2, 5.0F);
/*     */   }
/*     */   
/*     */   public Properties hoe(ToolMaterial paramToolMaterial, float paramFloat1, float paramFloat2) {
/* 263 */     return tool(paramToolMaterial, BlockTags.MINEABLE_WITH_HOE, paramFloat1, paramFloat2, 0.0F);
/*     */   }
/*     */   
/*     */   public Properties shovel(ToolMaterial paramToolMaterial, float paramFloat1, float paramFloat2) {
/* 267 */     return tool(paramToolMaterial, BlockTags.MINEABLE_WITH_SHOVEL, paramFloat1, paramFloat2, 0.0F);
/*     */   }
/*     */   
/*     */   public Properties sword(ToolMaterial paramToolMaterial, float paramFloat1, float paramFloat2) {
/* 271 */     return paramToolMaterial.applySwordProperties(this, paramFloat1, paramFloat2);
/*     */   }
/*     */   
/*     */   public Properties spear(ToolMaterial paramToolMaterial, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9) {
/* 275 */     return durability(paramToolMaterial.durability())
/* 276 */       .repairable(paramToolMaterial.repairItems())
/* 277 */       .enchantable(paramToolMaterial.enchantmentValue())
/* 278 */       .component(DataComponents.DAMAGE_TYPE, new EitherHolder(DamageTypes.SPEAR))
/* 279 */       .<KineticWeapon>component(DataComponents.KINETIC_WEAPON, new KineticWeapon(10, (int)(paramFloat3 * 20.0F), 
/*     */ 
/*     */           
/* 282 */           KineticWeapon.Condition.ofAttackerSpeed((int)(paramFloat4 * 20.0F), paramFloat5), 
/* 283 */           KineticWeapon.Condition.ofAttackerSpeed((int)(paramFloat6 * 20.0F), paramFloat7), 
/* 284 */           KineticWeapon.Condition.ofRelativeSpeed((int)(paramFloat8 * 20.0F), paramFloat9), 0.38F, paramFloat2, 
/*     */ 
/*     */           
/* 287 */           Optional.of((paramToolMaterial == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_USE : SoundEvents.SPEAR_USE), 
/* 288 */           Optional.of((paramToolMaterial == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)))
/*     */       
/* 290 */       .<PiercingWeapon>component(DataComponents.PIERCING_WEAPON, new PiercingWeapon(true, false, 
/*     */ 
/*     */           
/* 293 */           Optional.of((paramToolMaterial == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_ATTACK : SoundEvents.SPEAR_ATTACK), 
/* 294 */           Optional.of((paramToolMaterial == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)))
/*     */       
/* 296 */       .<AttackRange>component(DataComponents.ATTACK_RANGE, new AttackRange(2.0F, 4.5F, 2.0F, 6.5F, 0.125F, 0.5F))
/* 297 */       .<Float>component(DataComponents.MINIMUM_ATTACK_CHARGE, Float.valueOf(1.0F))
/* 298 */       .<SwingAnimation>component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int)(paramFloat1 * 20.0F)))
/*     */ 
/*     */ 
/*     */       
/* 302 */       .attributes(ItemAttributeModifiers.builder()
/* 303 */         .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (0.0F + paramToolMaterial.attackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 304 */         .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, (1.0F / paramFloat1) - 4.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 305 */         .build())
/* 306 */       .<UseEffects>component(DataComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
/* 307 */       .component(DataComponents.WEAPON, new Weapon(1));
/*     */   }
/*     */   
/*     */   public Properties spawnEgg(EntityType<?> paramEntityType) {
/* 311 */     return component(DataComponents.ENTITY_DATA, TypedEntityData.of(paramEntityType, new CompoundTag()));
/*     */   }
/*     */   
/*     */   public Properties humanoidArmor(ArmorMaterial paramArmorMaterial, ArmorType paramArmorType) {
/* 315 */     return durability(paramArmorType.getDurability(paramArmorMaterial.durability()))
/* 316 */       .attributes(paramArmorMaterial.createAttributes(paramArmorType))
/* 317 */       .enchantable(paramArmorMaterial.enchantmentValue())
/* 318 */       .<Equippable>component(DataComponents.EQUIPPABLE, Equippable.builder(paramArmorType.getSlot()).setEquipSound(paramArmorMaterial.equipSound()).setAsset(paramArmorMaterial.assetId()).build())
/* 319 */       .repairable(paramArmorMaterial.repairIngredient());
/*     */   }
/*     */   
/*     */   public Properties wolfArmor(ArmorMaterial paramArmorMaterial) {
/* 323 */     return durability(ArmorType.BODY.getDurability(paramArmorMaterial.durability()))
/* 324 */       .attributes(paramArmorMaterial.createAttributes(ArmorType.BODY))
/* 325 */       .repairable(paramArmorMaterial.repairIngredient())
/* 326 */       .<Equippable>component(DataComponents.EQUIPPABLE, 
/* 327 */         Equippable.builder(EquipmentSlot.BODY)
/* 328 */         .setEquipSound(paramArmorMaterial.equipSound())
/* 329 */         .setAsset(paramArmorMaterial.assetId())
/* 330 */         .setAllowedEntities((HolderSet)HolderSet.direct(new Holder[] { (Holder)EntityType.WOLF.builtInRegistryHolder()
/* 331 */             })).setCanBeSheared(true)
/* 332 */         .setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_UNEQUIP_WOLF))
/* 333 */         .build())
/*     */       
/* 335 */       .<Holder.Reference>component(DataComponents.BREAK_SOUND, SoundEvents.WOLF_ARMOR_BREAK)
/* 336 */       .stacksTo(1);
/*     */   }
/*     */   
/*     */   public Properties horseArmor(ArmorMaterial paramArmorMaterial) {
/* 340 */     HolderGetter holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ENTITY_TYPE);
/* 341 */     return attributes(paramArmorMaterial.createAttributes(ArmorType.BODY))
/* 342 */       .<Equippable>component(DataComponents.EQUIPPABLE, 
/* 343 */         Equippable.builder(EquipmentSlot.BODY)
/* 344 */         .setEquipSound((Holder)SoundEvents.HORSE_ARMOR)
/* 345 */         .setAsset(paramArmorMaterial.assetId())
/* 346 */         .setAllowedEntities((HolderSet)holderGetter.getOrThrow(EntityTypeTags.CAN_WEAR_HORSE_ARMOR))
/* 347 */         .setDamageOnHurt(false)
/* 348 */         .setCanBeSheared(true)
/* 349 */         .setShearingSound((Holder)SoundEvents.HORSE_ARMOR_UNEQUIP)
/* 350 */         .build())
/*     */       
/* 352 */       .stacksTo(1);
/*     */   }
/*     */   
/*     */   public Properties nautilusArmor(ArmorMaterial paramArmorMaterial) {
/* 356 */     HolderGetter holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ENTITY_TYPE);
/* 357 */     return attributes(paramArmorMaterial.createAttributes(ArmorType.BODY))
/* 358 */       .<Equippable>component(DataComponents.EQUIPPABLE, 
/* 359 */         Equippable.builder(EquipmentSlot.BODY)
/* 360 */         .setEquipSound((Holder)SoundEvents.ARMOR_EQUIP_NAUTILUS)
/* 361 */         .setAsset(paramArmorMaterial.assetId())
/* 362 */         .setAllowedEntities((HolderSet)holderGetter.getOrThrow(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR))
/* 363 */         .setDamageOnHurt(false)
/* 364 */         .setEquipOnInteract(true)
/* 365 */         .setCanBeSheared(true)
/* 366 */         .setShearingSound((Holder)SoundEvents.ARMOR_UNEQUIP_NAUTILUS)
/* 367 */         .build())
/*     */       
/* 369 */       .stacksTo(1);
/*     */   }
/*     */   
/*     */   public Properties trimMaterial(ResourceKey<TrimMaterial> paramResourceKey) {
/* 373 */     return component(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(paramResourceKey));
/*     */   }
/*     */   
/*     */   public Properties requiredFeatures(FeatureFlag... paramVarArgs) {
/* 377 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(paramVarArgs);
/* 378 */     return this;
/*     */   }
/*     */   
/*     */   public Properties setId(ResourceKey<Item> paramResourceKey) {
/* 382 */     this.id = paramResourceKey;
/* 383 */     return this;
/*     */   }
/*     */   
/*     */   public Properties overrideDescription(String paramString) {
/* 387 */     this.descriptionId = DependantName.fixed(paramString);
/* 388 */     return this;
/*     */   }
/*     */   
/*     */   public Properties useBlockDescriptionPrefix() {
/* 392 */     this.descriptionId = BLOCK_DESCRIPTION_ID;
/* 393 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties useItemDescriptionPrefix() {
/* 400 */     this.descriptionId = ITEM_DESCRIPTION_ID;
/* 401 */     return this;
/*     */   }
/*     */   
/*     */   protected String effectiveDescriptionId() {
/* 405 */     return (String)this.descriptionId.get(Objects.<ResourceKey>requireNonNull(this.id, "Item id not set"));
/*     */   }
/*     */   
/*     */   public Identifier effectiveModel() {
/* 409 */     return (Identifier)this.model.get(Objects.<ResourceKey>requireNonNull(this.id, "Item id not set"));
/*     */   }
/*     */   
/*     */   public <T> Properties component(DataComponentType<T> paramDataComponentType, T paramT) {
/* 413 */     this.components.set(paramDataComponentType, paramT);
/* 414 */     return this;
/*     */   }
/*     */   
/*     */   public Properties attributes(ItemAttributeModifiers paramItemAttributeModifiers) {
/* 418 */     return component(DataComponents.ATTRIBUTE_MODIFIERS, paramItemAttributeModifiers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataComponentMap buildAndValidateComponents(Component paramComponent, Identifier paramIdentifier) {
/* 425 */     DataComponentMap dataComponentMap = this.components.set(DataComponents.ITEM_NAME, paramComponent).set(DataComponents.ITEM_MODEL, paramIdentifier).build();
/*     */     
/* 427 */     if (dataComponentMap.has(DataComponents.DAMAGE) && ((Integer)dataComponentMap.getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue() > 1) {
/* 428 */       throw new IllegalStateException("Item cannot have both durability and be stackable");
/*     */     }
/* 430 */     return dataComponentMap;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\Item$Properties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */