/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.criterion.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityTypePredicate;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.MovementPredicate;
/*     */ import net.minecraft.advancements.criterion.TagPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ConstantFloat;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.util.valueproviders.UniformFloat;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.effects.AddValue;
/*     */ import net.minecraft.world.item.enchantment.effects.AllOf;
/*     */ import net.minecraft.world.item.enchantment.effects.ApplyEntityImpulse;
/*     */ import net.minecraft.world.item.enchantment.effects.ApplyExhaustion;
/*     */ import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.ChangeItemDamage;
/*     */ import net.minecraft.world.item.enchantment.effects.DamageEntity;
/*     */ import net.minecraft.world.item.enchantment.effects.DamageImmunity;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.ExplodeEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.Ignite;
/*     */ import net.minecraft.world.item.enchantment.effects.MultiplyValue;
/*     */ import net.minecraft.world.item.enchantment.effects.PlaySoundEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.RemoveBinomial;
/*     */ import net.minecraft.world.item.enchantment.effects.ReplaceDisk;
/*     */ import net.minecraft.world.item.enchantment.effects.SetValue;
/*     */ import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.SummonEntityEffect;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.EnchantmentActiveCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.MatchTool;
/*     */ import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Enchantments
/*     */ {
/*  94 */   public static final ResourceKey<Enchantment> PROTECTION = key("protection");
/*  95 */   public static final ResourceKey<Enchantment> FIRE_PROTECTION = key("fire_protection");
/*  96 */   public static final ResourceKey<Enchantment> FEATHER_FALLING = key("feather_falling");
/*  97 */   public static final ResourceKey<Enchantment> BLAST_PROTECTION = key("blast_protection");
/*  98 */   public static final ResourceKey<Enchantment> PROJECTILE_PROTECTION = key("projectile_protection");
/*  99 */   public static final ResourceKey<Enchantment> RESPIRATION = key("respiration");
/* 100 */   public static final ResourceKey<Enchantment> AQUA_AFFINITY = key("aqua_affinity");
/* 101 */   public static final ResourceKey<Enchantment> THORNS = key("thorns");
/* 102 */   public static final ResourceKey<Enchantment> DEPTH_STRIDER = key("depth_strider");
/* 103 */   public static final ResourceKey<Enchantment> FROST_WALKER = key("frost_walker");
/* 104 */   public static final ResourceKey<Enchantment> BINDING_CURSE = key("binding_curse");
/* 105 */   public static final ResourceKey<Enchantment> SOUL_SPEED = key("soul_speed");
/* 106 */   public static final ResourceKey<Enchantment> SWIFT_SNEAK = key("swift_sneak");
/*     */ 
/*     */   
/* 109 */   public static final ResourceKey<Enchantment> SHARPNESS = key("sharpness");
/* 110 */   public static final ResourceKey<Enchantment> SMITE = key("smite");
/* 111 */   public static final ResourceKey<Enchantment> BANE_OF_ARTHROPODS = key("bane_of_arthropods");
/* 112 */   public static final ResourceKey<Enchantment> KNOCKBACK = key("knockback");
/* 113 */   public static final ResourceKey<Enchantment> FIRE_ASPECT = key("fire_aspect");
/* 114 */   public static final ResourceKey<Enchantment> LOOTING = key("looting");
/* 115 */   public static final ResourceKey<Enchantment> SWEEPING_EDGE = key("sweeping_edge");
/*     */ 
/*     */   
/* 118 */   public static final ResourceKey<Enchantment> EFFICIENCY = key("efficiency");
/* 119 */   public static final ResourceKey<Enchantment> SILK_TOUCH = key("silk_touch");
/* 120 */   public static final ResourceKey<Enchantment> UNBREAKING = key("unbreaking");
/* 121 */   public static final ResourceKey<Enchantment> FORTUNE = key("fortune");
/*     */ 
/*     */   
/* 124 */   public static final ResourceKey<Enchantment> POWER = key("power");
/* 125 */   public static final ResourceKey<Enchantment> PUNCH = key("punch");
/* 126 */   public static final ResourceKey<Enchantment> FLAME = key("flame");
/* 127 */   public static final ResourceKey<Enchantment> INFINITY = key("infinity");
/*     */ 
/*     */   
/* 130 */   public static final ResourceKey<Enchantment> LUCK_OF_THE_SEA = key("luck_of_the_sea");
/* 131 */   public static final ResourceKey<Enchantment> LURE = key("lure");
/*     */ 
/*     */   
/* 134 */   public static final ResourceKey<Enchantment> LOYALTY = key("loyalty");
/* 135 */   public static final ResourceKey<Enchantment> IMPALING = key("impaling");
/* 136 */   public static final ResourceKey<Enchantment> RIPTIDE = key("riptide");
/* 137 */   public static final ResourceKey<Enchantment> CHANNELING = key("channeling");
/*     */ 
/*     */   
/* 140 */   public static final ResourceKey<Enchantment> MULTISHOT = key("multishot");
/* 141 */   public static final ResourceKey<Enchantment> QUICK_CHARGE = key("quick_charge");
/* 142 */   public static final ResourceKey<Enchantment> PIERCING = key("piercing");
/*     */ 
/*     */   
/* 145 */   public static final ResourceKey<Enchantment> DENSITY = key("density");
/* 146 */   public static final ResourceKey<Enchantment> BREACH = key("breach");
/* 147 */   public static final ResourceKey<Enchantment> WIND_BURST = key("wind_burst");
/*     */ 
/*     */   
/* 150 */   public static final ResourceKey<Enchantment> LUNGE = key("lunge");
/*     */ 
/*     */   
/* 153 */   public static final ResourceKey<Enchantment> MENDING = key("mending");
/* 154 */   public static final ResourceKey<Enchantment> VANISHING_CURSE = key("vanishing_curse");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<Enchantment> paramBootstrapContext) {
/* 157 */     HolderGetter holderGetter1 = paramBootstrapContext.lookup(Registries.DAMAGE_TYPE);
/* 158 */     HolderGetter holderGetter2 = paramBootstrapContext.lookup(Registries.ENCHANTMENT);
/* 159 */     HolderGetter holderGetter3 = paramBootstrapContext.lookup(Registries.ITEM);
/* 160 */     HolderGetter holderGetter4 = paramBootstrapContext.lookup(Registries.BLOCK);
/* 161 */     HolderGetter holderGetter5 = paramBootstrapContext.lookup(Registries.ENTITY_TYPE);
/*     */ 
/*     */     
/* 164 */     register(paramBootstrapContext, PROTECTION, 
/* 165 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 10, 4, Enchantment.dynamicCost(1, 11), Enchantment.dynamicCost(12, 11), 1, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR
/* 166 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
/* 167 */         .withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(
/*     */             
/* 169 */             LevelBasedValue.perLevel(1.0F)), 
/* 170 */           DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)))));
/*     */ 
/*     */     
/* 173 */     register(paramBootstrapContext, FIRE_PROTECTION, 
/* 174 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 5, 4, Enchantment.dynamicCost(10, 8), Enchantment.dynamicCost(18, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR
/* 175 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
/* 176 */         .withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(
/*     */             
/* 178 */             LevelBasedValue.perLevel(2.0F)), 
/* 179 */           (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] {
/* 180 */               DamageSourceCondition.hasDamageSource(
/* 181 */                 DamageSourcePredicate.Builder.damageType()
/* 182 */                 .tag(TagPredicate.is(DamageTypeTags.IS_FIRE))
/* 183 */                 .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)))
/*     */ 
/*     */ 
/*     */             
/* 187 */             })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 189 */             Identifier.withDefaultNamespace("enchantment.fire_protection"), Attributes.BURNING_TIME, LevelBasedValue.perLevel(-0.15F), AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
/*     */ 
/*     */     
/* 192 */     register(paramBootstrapContext, FEATHER_FALLING, 
/* 193 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE), 5, 4, Enchantment.dynamicCost(5, 6), Enchantment.dynamicCost(11, 6), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR
/* 194 */             })).withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(
/*     */             
/* 196 */             LevelBasedValue.perLevel(3.0F)), 
/* 197 */           DamageSourceCondition.hasDamageSource(
/* 198 */             DamageSourcePredicate.Builder.damageType()
/* 199 */             .tag(TagPredicate.is(DamageTypeTags.IS_FALL))
/* 200 */             .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)))));
/*     */ 
/*     */ 
/*     */     
/* 204 */     register(paramBootstrapContext, BLAST_PROTECTION, 
/* 205 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 2, 4, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(13, 8), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR
/* 206 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
/* 207 */         .withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(
/*     */             
/* 209 */             LevelBasedValue.perLevel(2.0F)), 
/* 210 */           DamageSourceCondition.hasDamageSource(
/* 211 */             DamageSourcePredicate.Builder.damageType()
/* 212 */             .tag(TagPredicate.is(DamageTypeTags.IS_EXPLOSION))
/* 213 */             .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))))
/*     */ 
/*     */         
/* 216 */         .withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 218 */             Identifier.withDefaultNamespace("enchantment.blast_protection"), Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, LevelBasedValue.perLevel(0.15F), AttributeModifier.Operation.ADD_VALUE)));
/*     */ 
/*     */     
/* 221 */     register(paramBootstrapContext, PROJECTILE_PROTECTION, 
/* 222 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 5, 4, Enchantment.dynamicCost(3, 6), Enchantment.dynamicCost(9, 6), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR
/* 223 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
/* 224 */         .withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(
/*     */             
/* 226 */             LevelBasedValue.perLevel(2.0F)), 
/* 227 */           DamageSourceCondition.hasDamageSource(
/* 228 */             DamageSourcePredicate.Builder.damageType()
/* 229 */             .tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))
/* 230 */             .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     register(paramBootstrapContext, RESPIRATION, 
/* 236 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(40, 10), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.HEAD
/* 237 */             })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 239 */             Identifier.withDefaultNamespace("enchantment.respiration"), Attributes.OXYGEN_BONUS, LevelBasedValue.perLevel(1.0F), AttributeModifier.Operation.ADD_VALUE)));
/*     */ 
/*     */     
/* 242 */     register(paramBootstrapContext, AQUA_AFFINITY, 
/* 243 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE), 2, 1, Enchantment.constantCost(1), Enchantment.constantCost(41), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.HEAD
/* 244 */             })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 246 */             Identifier.withDefaultNamespace("enchantment.aqua_affinity"), Attributes.SUBMERGED_MINING_SPEED, LevelBasedValue.perLevel(4.0F), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)));
/*     */ 
/*     */     
/* 249 */     register(paramBootstrapContext, THORNS, 
/* 250 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), (HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE), 1, 3, Enchantment.dynamicCost(10, 20), Enchantment.dynamicCost(60, 20), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.ANY
/* 251 */             })).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.VICTIM, EnchantmentTarget.ATTACKER, 
/*     */ 
/*     */ 
/*     */           
/* 255 */           AllOf.entityEffects(new EnchantmentEntityEffect[] {
/* 256 */               (EnchantmentEntityEffect)new DamageEntity(LevelBasedValue.constant(1.0F), LevelBasedValue.constant(5.0F), (Holder)holderGetter1.getOrThrow(DamageTypes.THORNS)), (EnchantmentEntityEffect)new ChangeItemDamage(
/* 257 */                 LevelBasedValue.constant(2.0F))
/*     */             
/* 259 */             }), LootItemRandomChanceCondition.randomChance(
/* 260 */             (NumberProvider)EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     register(paramBootstrapContext, DEPTH_STRIDER, 
/* 266 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(25, 10), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.FEET
/* 267 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE))
/* 268 */         .withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 270 */             Identifier.withDefaultNamespace("enchantment.depth_strider"), Attributes.WATER_MOVEMENT_EFFICIENCY, LevelBasedValue.perLevel(0.33333334F), AttributeModifier.Operation.ADD_VALUE)));
/*     */ 
/*     */     
/* 273 */     register(paramBootstrapContext, FROST_WALKER, 
/* 274 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE), 2, 2, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(25, 10), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.FEET
/* 275 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE))
/* 276 */         .<DamageImmunity>withEffect(EnchantmentEffectComponents.DAMAGE_IMMUNITY, DamageImmunity.INSTANCE, 
/*     */ 
/*     */           
/* 279 */           DamageSourceCondition.hasDamageSource(
/* 280 */             DamageSourcePredicate.Builder.damageType()
/* 281 */             .tag(TagPredicate.is(DamageTypeTags.BURN_FROM_STEPPING))
/* 282 */             .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))))
/*     */ 
/*     */         
/* 285 */         .withEffect(EnchantmentEffectComponents.LOCATION_CHANGED, new ReplaceDisk(new LevelBasedValue.Clamped(
/*     */ 
/*     */               
/* 288 */               LevelBasedValue.perLevel(3.0F, 1.0F), 0.0F, 16.0F), 
/* 289 */             LevelBasedValue.constant(1.0F), new Vec3i(0, -1, 0), 
/*     */             
/* 291 */             Optional.of(
/* 292 */               BlockPredicate.allOf(new BlockPredicate[] {
/* 293 */                   BlockPredicate.matchesTag(new Vec3i(0, 1, 0), BlockTags.AIR), 
/* 294 */                   BlockPredicate.matchesBlocks(new Block[] { Blocks.WATER
/* 295 */                     }), BlockPredicate.matchesFluids(new Fluid[] { (Fluid)Fluids.WATER
/* 296 */                     }), BlockPredicate.unobstructed()
/*     */ 
/*     */                 
/* 299 */                 })), (BlockStateProvider)BlockStateProvider.simple(Blocks.FROSTED_ICE), 
/* 300 */             Optional.of(GameEvent.BLOCK_PLACE)), 
/*     */           
/* 302 */           (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] {
/* 303 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnGround(Boolean.valueOf(true)))), 
/* 304 */               InvertedLootItemCondition.invert(
/* 305 */                 LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
/* 306 */                   .vehicle(EntityPredicate.Builder.entity())))
/*     */             })));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     register(paramBootstrapContext, BINDING_CURSE, 
/* 313 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.EQUIPPABLE_ENCHANTABLE), 1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR
/* 314 */             })).withEffect(EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 323 */     EntityPredicate.Builder builder = EntityPredicate.Builder.entity().periodicTick(5).flags(EntityFlagsPredicate.Builder.flags().setIsFlying(Boolean.valueOf(false)).setOnGround(Boolean.valueOf(true))).moving(MovementPredicate.horizontalSpeed(MinMaxBounds.Doubles.atLeast(9.999999747378752E-6D))).movementAffectedBy(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(holderGetter4, BlockTags.SOUL_SPEED_BLOCKS)));
/*     */     
/* 325 */     AllOfCondition.Builder builder1 = AllOfCondition.allOf(new LootItemCondition.Builder[] {
/* 326 */           InvertedLootItemCondition.invert(
/* 327 */             LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
/* 328 */               .vehicle(EntityPredicate.Builder.entity()))), 
/*     */ 
/*     */           
/* 331 */           (LootItemCondition.Builder)AnyOfCondition.anyOf(new LootItemCondition.Builder[] {
/* 332 */               (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] {
/* 333 */                   EnchantmentActiveCheck.enchantmentActiveCheck(), 
/*     */                   
/* 335 */                   LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setIsFlying(Boolean.valueOf(false)))), 
/* 336 */                   (LootItemCondition.Builder)AnyOfCondition.anyOf(new LootItemCondition.Builder[] {
/* 337 */                       LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().movementAffectedBy(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(holderGetter4, BlockTags.SOUL_SPEED_BLOCKS)))), 
/* 338 */                       LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnGround(Boolean.valueOf(false))).build())
/*     */                     
/*     */                     })
/* 341 */                 }), (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] {
/* 342 */                   EnchantmentActiveCheck.enchantmentInactiveCheck(), 
/* 343 */                   LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
/* 344 */                     .movementAffectedBy(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(holderGetter4, BlockTags.SOUL_SPEED_BLOCKS)))
/* 345 */                     .flags(EntityFlagsPredicate.Builder.flags().setIsFlying(Boolean.valueOf(false))))
/*     */                 })
/*     */             })
/*     */         });
/*     */ 
/*     */     
/* 351 */     register(paramBootstrapContext, SOUL_SPEED, 
/* 352 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE), 1, 3, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(25, 10), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.FEET
/* 353 */             })).withEffect(EnchantmentEffectComponents.LOCATION_CHANGED, 
/*     */           
/* 355 */           AllOf.locationBasedEffects(new EnchantmentLocationBasedEffect[] {
/* 356 */               (EnchantmentLocationBasedEffect)new EnchantmentAttributeEffect(Identifier.withDefaultNamespace("enchantment.soul_speed"), Attributes.MOVEMENT_SPEED, LevelBasedValue.perLevel(0.0405F, 0.0105F), AttributeModifier.Operation.ADD_VALUE), (EnchantmentLocationBasedEffect)new EnchantmentAttributeEffect(
/* 357 */                 Identifier.withDefaultNamespace("enchantment.soul_speed"), Attributes.MOVEMENT_EFFICIENCY, LevelBasedValue.constant(1.0F), AttributeModifier.Operation.ADD_VALUE)
/*     */ 
/*     */ 
/*     */             
/* 361 */             }), (LootItemCondition.Builder)builder1).withEffect(EnchantmentEffectComponents.LOCATION_CHANGED, new ChangeItemDamage(
/*     */             
/* 363 */             LevelBasedValue.constant(1.0F)), 
/* 364 */           (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] {
/* 365 */               LootItemRandomChanceCondition.randomChance((NumberProvider)EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.constant(0.04F))), 
/* 366 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
/* 367 */                 .flags(EntityFlagsPredicate.Builder.flags().setOnGround(Boolean.valueOf(true)))
/* 368 */                 .movementAffectedBy(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(holderGetter4, BlockTags.SOUL_SPEED_BLOCKS))))
/*     */ 
/*     */ 
/*     */             
/* 372 */             })).withEffect(EnchantmentEffectComponents.TICK, new SpawnParticlesEffect((ParticleOptions)ParticleTypes.SOUL, 
/*     */             
/* 374 */             SpawnParticlesEffect.inBoundingBox(), SpawnParticlesEffect.offsetFromEntityPosition(0.1F), SpawnParticlesEffect.movementScaled(-0.2F), SpawnParticlesEffect.fixedVelocity((FloatProvider)ConstantFloat.of(0.1F)), (FloatProvider)ConstantFloat.of(1.0F)), 
/* 375 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, builder))
/*     */         
/* 377 */         .withEffect(EnchantmentEffectComponents.TICK, new PlaySoundEffect(
/*     */             
/* 379 */             List.of(SoundEvents.SOUL_ESCAPE), (FloatProvider)ConstantFloat.of(0.6F), (FloatProvider)UniformFloat.of(0.6F, 1.0F)), 
/* 380 */           (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] {
/* 381 */               LootItemRandomChanceCondition.randomChance(0.35F), 
/* 382 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, builder)
/*     */             })));
/*     */ 
/*     */     
/* 386 */     register(paramBootstrapContext, SWIFT_SNEAK, 
/* 387 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE), 1, 3, Enchantment.dynamicCost(25, 25), Enchantment.dynamicCost(75, 25), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.LEGS
/* 388 */             })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 390 */             Identifier.withDefaultNamespace("enchantment.swift_sneak"), Attributes.SNEAKING_SPEED, LevelBasedValue.perLevel(0.15F), AttributeModifier.Operation.ADD_VALUE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 395 */     register(paramBootstrapContext, SHARPNESS, 
/* 396 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE), (HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 10, 5, Enchantment.dynamicCost(1, 11), Enchantment.dynamicCost(21, 11), 1, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 397 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 398 */         .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(
/*     */             
/* 400 */             LevelBasedValue.perLevel(1.0F, 0.5F))));
/*     */ 
/*     */     
/* 403 */     register(paramBootstrapContext, SMITE, 
/* 404 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.WEAPON_ENCHANTABLE), (HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 5, 5, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 405 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 406 */         .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(
/*     */             
/* 408 */             LevelBasedValue.perLevel(2.5F)), 
/* 409 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(holderGetter5, EntityTypeTags.SENSITIVE_TO_SMITE)))));
/*     */ 
/*     */     
/* 412 */     register(paramBootstrapContext, BANE_OF_ARTHROPODS, 
/* 413 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.WEAPON_ENCHANTABLE), (HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 5, 5, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 414 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 415 */         .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(
/*     */             
/* 417 */             LevelBasedValue.perLevel(2.5F)), 
/* 418 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/*     */             
/* 420 */             EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(holderGetter5, EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS))))
/*     */ 
/*     */         
/* 423 */         .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, new ApplyMobEffect(
/*     */ 
/*     */ 
/*     */             
/* 427 */             (HolderSet)HolderSet.direct(new Holder[] { MobEffects.SLOWNESS }, ), LevelBasedValue.constant(1.5F), LevelBasedValue.perLevel(1.5F, 0.5F), LevelBasedValue.constant(3.0F), LevelBasedValue.constant(3.0F)), 
/* 428 */           (LootItemCondition.Builder)LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/*     */             
/* 430 */             EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(holderGetter5, EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)))
/* 431 */           .and(
/* 432 */             DamageSourceCondition.hasDamageSource(
/* 433 */               DamageSourcePredicate.Builder.damageType().isDirect(true)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 438 */     register(paramBootstrapContext, KNOCKBACK, 
/* 439 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 5, 2, Enchantment.dynamicCost(5, 20), Enchantment.dynamicCost(55, 20), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 440 */             })).withEffect(EnchantmentEffectComponents.KNOCKBACK, new AddValue(
/*     */             
/* 442 */             LevelBasedValue.perLevel(1.0F))));
/*     */ 
/*     */     
/* 445 */     register(paramBootstrapContext, FIRE_ASPECT, 
/* 446 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.FIRE_ASPECT_ENCHANTABLE), (HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 2, 2, Enchantment.dynamicCost(10, 20), Enchantment.dynamicCost(60, 20), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 447 */             })).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, new Ignite(
/*     */ 
/*     */ 
/*     */             
/* 451 */             LevelBasedValue.perLevel(4.0F)), 
/* 452 */           DamageSourceCondition.hasDamageSource(
/* 453 */             DamageSourcePredicate.Builder.damageType().isDirect(true))));
/*     */ 
/*     */ 
/*     */     
/* 457 */     register(paramBootstrapContext, LOOTING, 
/* 458 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 459 */             })).withEffect(EnchantmentEffectComponents.EQUIPMENT_DROPS, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, new AddValue(
/*     */ 
/*     */ 
/*     */             
/* 463 */             LevelBasedValue.perLevel(0.01F)), 
/* 464 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(holderGetter5, EntityType.PLAYER)))));
/*     */ 
/*     */     
/* 467 */     register(paramBootstrapContext, SWEEPING_EDGE, 
/* 468 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.SWEEPING_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(5, 9), Enchantment.dynamicCost(20, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 469 */             })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 471 */             Identifier.withDefaultNamespace("enchantment.sweeping_edge"), Attributes.SWEEPING_DAMAGE_RATIO, new LevelBasedValue.Fraction(LevelBasedValue.perLevel(1.0F), LevelBasedValue.perLevel(2.0F, 1.0F)), AttributeModifier.Operation.ADD_VALUE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 476 */     register(paramBootstrapContext, EFFICIENCY, 
/* 477 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MINING_ENCHANTABLE), 10, 5, Enchantment.dynamicCost(1, 10), Enchantment.dynamicCost(51, 10), 1, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 478 */             })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 480 */             Identifier.withDefaultNamespace("enchantment.efficiency"), Attributes.MINING_EFFICIENCY, new LevelBasedValue.LevelsSquared(1.0F), AttributeModifier.Operation.ADD_VALUE)));
/*     */ 
/*     */     
/* 483 */     register(paramBootstrapContext, SILK_TOUCH, 
/* 484 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE), 1, 1, Enchantment.constantCost(15), Enchantment.constantCost(65), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 485 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.MINING_EXCLUSIVE))
/* 486 */         .withEffect(EnchantmentEffectComponents.BLOCK_EXPERIENCE, new SetValue(
/*     */             
/* 488 */             LevelBasedValue.constant(0.0F))));
/*     */ 
/*     */     
/* 491 */     register(paramBootstrapContext, UNBREAKING, 
/* 492 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE), 5, 3, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(55, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.ANY
/*     */             
/* 494 */             })).withEffect(EnchantmentEffectComponents.ITEM_DAMAGE, new RemoveBinomial(new LevelBasedValue.Fraction(
/*     */               
/* 496 */               LevelBasedValue.perLevel(2.0F), LevelBasedValue.perLevel(10.0F, 5.0F))), 
/* 497 */           MatchTool.toolMatches(ItemPredicate.Builder.item().of(holderGetter3, ItemTags.ARMOR_ENCHANTABLE)))
/*     */         
/* 499 */         .withEffect(EnchantmentEffectComponents.ITEM_DAMAGE, new RemoveBinomial(new LevelBasedValue.Fraction(
/*     */               
/* 501 */               LevelBasedValue.perLevel(1.0F), LevelBasedValue.perLevel(2.0F, 1.0F))), 
/* 502 */           InvertedLootItemCondition.invert(
/* 503 */             MatchTool.toolMatches(ItemPredicate.Builder.item().of(holderGetter3, ItemTags.ARMOR_ENCHANTABLE)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 508 */     register(paramBootstrapContext, FORTUNE, 
/* 509 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 510 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.MINING_EXCLUSIVE)));
/*     */ 
/*     */ 
/*     */     
/* 514 */     register(paramBootstrapContext, POWER, 
/* 515 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.BOW_ENCHANTABLE), 10, 5, Enchantment.dynamicCost(1, 10), Enchantment.dynamicCost(16, 10), 1, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 516 */             })).withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(
/*     */             
/* 518 */             LevelBasedValue.perLevel(1.0F, 0.5F)), 
/* 519 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, EntityPredicate.Builder.entity().of(holderGetter5, EntityTypeTags.ARROWS).build())));
/*     */ 
/*     */     
/* 522 */     register(paramBootstrapContext, PUNCH, 
/* 523 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.BOW_ENCHANTABLE), 2, 2, Enchantment.dynamicCost(12, 20), Enchantment.dynamicCost(37, 20), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 524 */             })).withEffect(EnchantmentEffectComponents.KNOCKBACK, new AddValue(
/*     */             
/* 526 */             LevelBasedValue.perLevel(1.0F)), 
/* 527 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, EntityPredicate.Builder.entity().of(holderGetter5, EntityTypeTags.ARROWS).build())));
/*     */ 
/*     */     
/* 530 */     register(paramBootstrapContext, FLAME, 
/* 531 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.BOW_ENCHANTABLE), 2, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 532 */             })).withEffect(EnchantmentEffectComponents.PROJECTILE_SPAWNED, new Ignite(
/*     */             
/* 534 */             LevelBasedValue.constant(100.0F))));
/*     */ 
/*     */     
/* 537 */     register(paramBootstrapContext, INFINITY, 
/* 538 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.BOW_ENCHANTABLE), 1, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 539 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.BOW_EXCLUSIVE))
/* 540 */         .withEffect(EnchantmentEffectComponents.AMMO_USE, new SetValue(
/*     */             
/* 542 */             LevelBasedValue.constant(0.0F)), 
/* 543 */           MatchTool.toolMatches(ItemPredicate.Builder.item().of(holderGetter3, new ItemLike[] { (ItemLike)Items.ARROW }))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 548 */     register(paramBootstrapContext, LUCK_OF_THE_SEA, 
/* 549 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.FISHING_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 550 */             })).withEffect(EnchantmentEffectComponents.FISHING_LUCK_BONUS, new AddValue(
/*     */             
/* 552 */             LevelBasedValue.perLevel(1.0F))));
/*     */ 
/*     */     
/* 555 */     register(paramBootstrapContext, LURE, 
/* 556 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.FISHING_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 557 */             })).withEffect(EnchantmentEffectComponents.FISHING_TIME_REDUCTION, new AddValue(
/*     */             
/* 559 */             LevelBasedValue.perLevel(5.0F))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 564 */     register(paramBootstrapContext, LOYALTY, 
/* 565 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE), 5, 3, Enchantment.dynamicCost(12, 7), Enchantment.constantCost(50), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 566 */             })).withEffect(EnchantmentEffectComponents.TRIDENT_RETURN_ACCELERATION, new AddValue(
/*     */             
/* 568 */             LevelBasedValue.perLevel(1.0F))));
/*     */ 
/*     */     
/* 571 */     register(paramBootstrapContext, IMPALING, 
/* 572 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE), 2, 5, Enchantment.dynamicCost(1, 8), Enchantment.dynamicCost(21, 8), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 573 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 574 */         .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(
/*     */             
/* 576 */             LevelBasedValue.perLevel(2.5F)), 
/* 577 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(holderGetter5, EntityTypeTags.SENSITIVE_TO_IMPALING)).build())));
/*     */ 
/*     */     
/* 580 */     register(paramBootstrapContext, RIPTIDE, 
/* 581 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(17, 7), Enchantment.constantCost(50), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.HAND
/* 582 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.RIPTIDE_EXCLUSIVE))
/* 583 */         .withSpecialEffect(EnchantmentEffectComponents.TRIDENT_SPIN_ATTACK_STRENGTH, new AddValue(
/*     */             
/* 585 */             LevelBasedValue.perLevel(1.5F, 0.75F)))
/*     */         
/* 587 */         .withSpecialEffect(EnchantmentEffectComponents.TRIDENT_SOUND, 
/*     */           
/* 589 */           List.of(SoundEvents.TRIDENT_RIPTIDE_1, SoundEvents.TRIDENT_RIPTIDE_2, SoundEvents.TRIDENT_RIPTIDE_3)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 597 */     register(paramBootstrapContext, LUNGE, 
/* 598 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.LUNGE_ENCHANTABLE), 5, 3, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.HAND
/* 599 */             })).withEffect(EnchantmentEffectComponents.POST_PIERCING_ATTACK, 
/*     */           
/* 601 */           AllOf.entityEffects(new EnchantmentEntityEffect[] {
/*     */               
/* 603 */               (EnchantmentEntityEffect)new ChangeItemDamage(new LevelBasedValue.Constant(1.0F)), (EnchantmentEntityEffect)new ApplyExhaustion(LevelBasedValue.perLevel(4.0F)), (EnchantmentEntityEffect)new ApplyEntityImpulse(new Vec3(0.0D, 0.0D, 1.0D), new Vec3(1.0D, 0.0D, 1.0D), 
/* 604 */                 LevelBasedValue.perLevel(0.458F)), (EnchantmentEntityEffect)new PlaySoundEffect(
/* 605 */                 List.of(SoundEvents.LUNGE_1, SoundEvents.LUNGE_2, SoundEvents.LUNGE_3), (FloatProvider)ConstantFloat.of(1.0F), (FloatProvider)ConstantFloat.of(1.0F))
/*     */             
/* 607 */             }), (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] {
/* 608 */               InvertedLootItemCondition.invert(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().vehicle(EntityPredicate.Builder.entity()))), 
/* 609 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setIsFallFlying(Boolean.valueOf(false)))), 
/* 610 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setIsInWater(Boolean.valueOf(false))))
/*     */             })));
/*     */ 
/*     */ 
/*     */     
/* 615 */     register(paramBootstrapContext, CHANNELING, 
/* 616 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE), 1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 617 */             })).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, 
/*     */ 
/*     */ 
/*     */           
/* 621 */           AllOf.entityEffects(new EnchantmentEntityEffect[] {
/* 622 */               (EnchantmentEntityEffect)new SummonEntityEffect((HolderSet)HolderSet.direct(new Holder[] { (Holder)EntityType.LIGHTNING_BOLT.builtInRegistryHolder() }, ), false), (EnchantmentEntityEffect)new PlaySoundEffect(
/* 623 */                 List.of(SoundEvents.TRIDENT_THUNDER), (FloatProvider)ConstantFloat.of(5.0F), (FloatProvider)ConstantFloat.of(1.0F))
/*     */             
/* 625 */             }), (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] {
/* 626 */               (LootItemCondition.Builder)WeatherCheck.weather().setThundering(true), 
/* 627 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().located(LocationPredicate.Builder.location().setCanSeeSky(true))), 
/* 628 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, EntityPredicate.Builder.entity().of(holderGetter5, EntityType.TRIDENT))
/*     */ 
/*     */             
/* 631 */             })).withEffect(EnchantmentEffectComponents.HIT_BLOCK, 
/*     */           
/* 633 */           AllOf.entityEffects(new EnchantmentEntityEffect[] {
/* 634 */               (EnchantmentEntityEffect)new SummonEntityEffect((HolderSet)HolderSet.direct(new Holder[] { (Holder)EntityType.LIGHTNING_BOLT.builtInRegistryHolder() }, ), false), (EnchantmentEntityEffect)new PlaySoundEffect(
/* 635 */                 List.of(SoundEvents.TRIDENT_THUNDER), (FloatProvider)ConstantFloat.of(5.0F), (FloatProvider)ConstantFloat.of(1.0F))
/*     */             
/* 637 */             }), (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] {
/* 638 */               (LootItemCondition.Builder)WeatherCheck.weather().setThundering(true), 
/* 639 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().of(holderGetter5, EntityType.TRIDENT)), 
/* 640 */               LocationCheck.checkLocation(LocationPredicate.Builder.location().setCanSeeSky(true).setBlock(BlockPredicate.Builder.block().of(holderGetter4, BlockTags.LIGHTNING_RODS)))
/*     */             })));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 646 */     register(paramBootstrapContext, MULTISHOT, 
/* 647 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE), 2, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 648 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.CROSSBOW_EXCLUSIVE))
/* 649 */         .withEffect(EnchantmentEffectComponents.PROJECTILE_COUNT, new AddValue(
/*     */             
/* 651 */             LevelBasedValue.perLevel(2.0F)))
/*     */         
/* 653 */         .withEffect(EnchantmentEffectComponents.PROJECTILE_SPREAD, new AddValue(
/*     */             
/* 655 */             LevelBasedValue.perLevel(10.0F))));
/*     */ 
/*     */     
/* 658 */     register(paramBootstrapContext, QUICK_CHARGE, 
/* 659 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE), 5, 3, Enchantment.dynamicCost(12, 20), Enchantment.constantCost(50), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.OFFHAND
/* 660 */             })).withSpecialEffect(EnchantmentEffectComponents.CROSSBOW_CHARGE_TIME, new AddValue(
/*     */             
/* 662 */             LevelBasedValue.perLevel(-0.25F)))
/*     */         
/* 664 */         .withSpecialEffect(EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS, 
/*     */           
/* 666 */           List.of(new CrossbowItem.ChargingSounds(
/* 667 */               Optional.of(SoundEvents.CROSSBOW_QUICK_CHARGE_1), Optional.empty(), Optional.of(SoundEvents.CROSSBOW_LOADING_END)), new CrossbowItem.ChargingSounds(
/* 668 */               Optional.of(SoundEvents.CROSSBOW_QUICK_CHARGE_2), Optional.empty(), Optional.of(SoundEvents.CROSSBOW_LOADING_END)), new CrossbowItem.ChargingSounds(
/* 669 */               Optional.of(SoundEvents.CROSSBOW_QUICK_CHARGE_3), Optional.empty(), Optional.of(SoundEvents.CROSSBOW_LOADING_END)))));
/*     */ 
/*     */ 
/*     */     
/* 673 */     register(paramBootstrapContext, PIERCING, 
/* 674 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE), 10, 4, Enchantment.dynamicCost(1, 10), Enchantment.constantCost(50), 1, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 675 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.CROSSBOW_EXCLUSIVE))
/* 676 */         .withEffect(EnchantmentEffectComponents.PROJECTILE_PIERCING, new AddValue(
/*     */             
/* 678 */             LevelBasedValue.perLevel(1.0F))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 683 */     register(paramBootstrapContext, DENSITY, 
/* 684 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MACE_ENCHANTABLE), 5, 5, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 685 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 686 */         .withEffect(EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK, new AddValue(
/*     */             
/* 688 */             LevelBasedValue.perLevel(0.5F))));
/*     */ 
/*     */     
/* 691 */     register(paramBootstrapContext, BREACH, 
/* 692 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MACE_ENCHANTABLE), 2, 4, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 693 */             })).exclusiveWith((HolderSet<Enchantment>)holderGetter2.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 694 */         .withEffect(EnchantmentEffectComponents.ARMOR_EFFECTIVENESS, new AddValue(
/*     */             
/* 696 */             LevelBasedValue.perLevel(-0.15F))));
/*     */ 
/*     */     
/* 699 */     register(paramBootstrapContext, WIND_BURST, 
/* 700 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.MACE_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND
/* 701 */             })).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.ATTACKER, new ExplodeEffect(false, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 707 */             Optional.empty(), 
/* 708 */             Optional.of(LevelBasedValue.lookup(List.of(Float.valueOf(1.2F), Float.valueOf(1.75F), Float.valueOf(2.2F)), LevelBasedValue.perLevel(1.5F, 0.35F))), holderGetter4
/* 709 */             .get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()), Vec3.ZERO, 
/*     */             
/* 711 */             LevelBasedValue.constant(3.5F), false, Level.ExplosionInteraction.TRIGGER, (ParticleOptions)ParticleTypes.GUST_EMITTER_SMALL, (ParticleOptions)ParticleTypes.GUST_EMITTER_LARGE, 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 716 */             WeightedList.of(), (Holder)SoundEvents.WIND_CHARGE_BURST), 
/*     */ 
/*     */           
/* 719 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, 
/*     */             
/* 721 */             EntityPredicate.Builder.entity()
/* 722 */             .flags(EntityFlagsPredicate.Builder.flags()
/* 723 */               .setIsFlying(Boolean.valueOf(false)))
/*     */             
/* 725 */             .moving(
/* 726 */               MovementPredicate.fallDistance(MinMaxBounds.Doubles.atLeast(1.5D))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 733 */     register(paramBootstrapContext, MENDING, 
/* 734 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE), 2, 1, Enchantment.dynamicCost(25, 25), Enchantment.dynamicCost(75, 25), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.ANY
/* 735 */             })).withEffect(EnchantmentEffectComponents.REPAIR_WITH_XP, new MultiplyValue(
/*     */             
/* 737 */             LevelBasedValue.constant(2.0F))));
/*     */ 
/*     */     
/* 740 */     register(paramBootstrapContext, VANISHING_CURSE, 
/* 741 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)holderGetter3.getOrThrow(ItemTags.VANISHING_ENCHANTABLE), 1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.ANY
/* 742 */             })).withEffect(EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void register(BootstrapContext<Enchantment> paramBootstrapContext, ResourceKey<Enchantment> paramResourceKey, Enchantment.Builder paramBuilder) {
/* 749 */     paramBootstrapContext.register(paramResourceKey, paramBuilder.build(paramResourceKey.identifier()));
/*     */   }
/*     */   
/*     */   private static ResourceKey<Enchantment> key(String paramString) {
/* 753 */     return ResourceKey.create(Registries.ENCHANTMENT, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\Enchantments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */