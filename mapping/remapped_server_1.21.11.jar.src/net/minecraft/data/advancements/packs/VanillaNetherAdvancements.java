/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.advancements.criterion.BrewedPotionTrigger;
/*     */ import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
/*     */ import net.minecraft.advancements.criterion.ConstructBeaconTrigger;
/*     */ import net.minecraft.advancements.criterion.ContextAwarePredicate;
/*     */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.criterion.DistancePredicate;
/*     */ import net.minecraft.advancements.criterion.DistanceTrigger;
/*     */ import net.minecraft.advancements.criterion.EffectsChangedTrigger;
/*     */ import net.minecraft.advancements.criterion.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemDurabilityTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
/*     */ import net.minecraft.advancements.criterion.KilledTrigger;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.LootTableTrigger;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.MobEffectsPredicate;
/*     */ import net.minecraft.advancements.criterion.PickedUpItemTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerTrigger;
/*     */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*     */ import net.minecraft.advancements.criterion.SummonedEntityTrigger;
/*     */ import net.minecraft.advancements.criterion.TagPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VanillaNetherAdvancements
/*     */   implements AdvancementSubProvider
/*     */ {
/*     */   public void generate(HolderLookup.Provider paramProvider, Consumer<AdvancementHolder> paramConsumer) {
/*  69 */     HolderLookup.RegistryLookup registryLookup1 = paramProvider.lookupOrThrow(Registries.ENTITY_TYPE);
/*  70 */     HolderLookup.RegistryLookup registryLookup2 = paramProvider.lookupOrThrow(Registries.ITEM);
/*  71 */     HolderLookup.RegistryLookup registryLookup3 = paramProvider.lookupOrThrow(Registries.BLOCK);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     AdvancementHolder advancementHolder1 = Advancement.Builder.advancement().display((ItemLike)Blocks.RED_NETHER_BRICKS, (Component)Component.translatable("advancements.nether.root.title"), (Component)Component.translatable("advancements.nether.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/nether"), AdvancementType.TASK, false, false, false).addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER)).save(paramConsumer, "nether/root");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     AdvancementHolder advancementHolder2 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.FIRE_CHARGE, (Component)Component.translatable("advancements.nether.return_to_sender.title"), (Component)Component.translatable("advancements.nether.return_to_sender.description"), null, AdvancementType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(50)).addCriterion("killed_ghast", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.GHAST), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.FIREBALL)))).save(paramConsumer, "nether/return_to_sender");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     AdvancementHolder advancementHolder3 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Blocks.NETHER_BRICKS, (Component)Component.translatable("advancements.nether.find_fortress.title"), (Component)Component.translatable("advancements.nether.find_fortress.description"), null, AdvancementType.TASK, true, true, false).addCriterion("fortress", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)paramProvider.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.FORTRESS)))).save(paramConsumer, "nether/find_fortress");
/*     */     
/*  91 */     Advancement.Builder.advancement()
/*  92 */       .parent(advancementHolder1)
/*  93 */       .display((ItemLike)Items.MAP, (Component)Component.translatable("advancements.nether.fast_travel.title"), (Component)Component.translatable("advancements.nether.fast_travel.description"), null, AdvancementType.CHALLENGE, true, true, false)
/*  94 */       .rewards(AdvancementRewards.Builder.experience(100))
/*  95 */       .addCriterion("travelled", DistanceTrigger.TriggerInstance.travelledThroughNether(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(7000.0D))))
/*  96 */       .save(paramConsumer, "nether/fast_travel");
/*     */     
/*  98 */     Advancement.Builder.advancement()
/*  99 */       .parent(advancementHolder2)
/* 100 */       .display((ItemLike)Items.GHAST_TEAR, (Component)Component.translatable("advancements.nether.uneasy_alliance.title"), (Component)Component.translatable("advancements.nether.uneasy_alliance.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 101 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 102 */       .addCriterion("killed_ghast", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.GHAST).located(LocationPredicate.Builder.inDimension(Level.OVERWORLD))))
/* 103 */       .save(paramConsumer, "nether/uneasy_alliance");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     AdvancementHolder advancementHolder4 = Advancement.Builder.advancement().parent(advancementHolder3).display((ItemLike)Blocks.WITHER_SKELETON_SKULL, (Component)Component.translatable("advancements.nether.get_wither_skull.title"), (Component)Component.translatable("advancements.nether.get_wither_skull.description"), null, AdvancementType.TASK, true, true, false).addCriterion("wither_skull", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.WITHER_SKELETON_SKULL })).save(paramConsumer, "nether/get_wither_skull");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     AdvancementHolder advancementHolder5 = Advancement.Builder.advancement().parent(advancementHolder4).display((ItemLike)Items.NETHER_STAR, (Component)Component.translatable("advancements.nether.summon_wither.title"), (Component)Component.translatable("advancements.nether.summon_wither.description"), null, AdvancementType.TASK, true, true, false).addCriterion("summoned", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.WITHER))).save(paramConsumer, "nether/summon_wither");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     AdvancementHolder advancementHolder6 = Advancement.Builder.advancement().parent(advancementHolder3).display((ItemLike)Items.BLAZE_ROD, (Component)Component.translatable("advancements.nether.obtain_blaze_rod.title"), (Component)Component.translatable("advancements.nether.obtain_blaze_rod.description"), null, AdvancementType.TASK, true, true, false).addCriterion("blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.BLAZE_ROD })).save(paramConsumer, "nether/obtain_blaze_rod");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     AdvancementHolder advancementHolder7 = Advancement.Builder.advancement().parent(advancementHolder5).display((ItemLike)Blocks.BEACON, (Component)Component.translatable("advancements.nether.create_beacon.title"), (Component)Component.translatable("advancements.nether.create_beacon.description"), null, AdvancementType.TASK, true, true, false).addCriterion("beacon", ConstructBeaconTrigger.TriggerInstance.constructedBeacon(MinMaxBounds.Ints.atLeast(1))).save(paramConsumer, "nether/create_beacon");
/*     */     
/* 129 */     Advancement.Builder.advancement()
/* 130 */       .parent(advancementHolder7)
/* 131 */       .display((ItemLike)Blocks.BEACON, (Component)Component.translatable("advancements.nether.create_full_beacon.title"), (Component)Component.translatable("advancements.nether.create_full_beacon.description"), null, AdvancementType.GOAL, true, true, false)
/* 132 */       .addCriterion("beacon", ConstructBeaconTrigger.TriggerInstance.constructedBeacon(MinMaxBounds.Ints.exactly(4)))
/* 133 */       .save(paramConsumer, "nether/create_full_beacon");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     AdvancementHolder advancementHolder8 = Advancement.Builder.advancement().parent(advancementHolder6).display((ItemLike)Items.POTION, (Component)Component.translatable("advancements.nether.brew_potion.title"), (Component)Component.translatable("advancements.nether.brew_potion.description"), null, AdvancementType.TASK, true, true, false).addCriterion("potion", BrewedPotionTrigger.TriggerInstance.brewedPotion()).save(paramConsumer, "nether/brew_potion");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     AdvancementHolder advancementHolder9 = Advancement.Builder.advancement().parent(advancementHolder8).display((ItemLike)Items.MILK_BUCKET, (Component)Component.translatable("advancements.nether.all_potions.title"), (Component)Component.translatable("advancements.nether.all_potions.description"), null, AdvancementType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(100)).addCriterion("all_effects", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MobEffects.SPEED).and(MobEffects.SLOWNESS).and(MobEffects.STRENGTH).and(MobEffects.JUMP_BOOST).and(MobEffects.REGENERATION).and(MobEffects.FIRE_RESISTANCE).and(MobEffects.WATER_BREATHING).and(MobEffects.INVISIBILITY).and(MobEffects.NIGHT_VISION).and(MobEffects.WEAKNESS).and(MobEffects.POISON).and(MobEffects.SLOW_FALLING).and(MobEffects.RESISTANCE).and(MobEffects.OOZING).and(MobEffects.INFESTED).and(MobEffects.WIND_CHARGED).and(MobEffects.WEAVING))).save(paramConsumer, "nether/all_potions");
/*     */     
/* 148 */     Advancement.Builder.advancement()
/* 149 */       .parent(advancementHolder9)
/* 150 */       .display((ItemLike)Items.BUCKET, (Component)Component.translatable("advancements.nether.all_effects.title"), (Component)Component.translatable("advancements.nether.all_effects.description"), null, AdvancementType.CHALLENGE, true, true, true)
/* 151 */       .rewards(AdvancementRewards.Builder.experience(1000))
/* 152 */       .addCriterion("all_effects", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MobEffects.SPEED).and(MobEffects.SLOWNESS).and(MobEffects.STRENGTH).and(MobEffects.JUMP_BOOST).and(MobEffects.REGENERATION).and(MobEffects.FIRE_RESISTANCE).and(MobEffects.WATER_BREATHING).and(MobEffects.INVISIBILITY).and(MobEffects.NIGHT_VISION).and(MobEffects.WEAKNESS).and(MobEffects.POISON).and(MobEffects.WITHER).and(MobEffects.HASTE).and(MobEffects.MINING_FATIGUE).and(MobEffects.LEVITATION).and(MobEffects.GLOWING).and(MobEffects.ABSORPTION).and(MobEffects.HUNGER).and(MobEffects.NAUSEA).and(MobEffects.RESISTANCE).and(MobEffects.SLOW_FALLING).and(MobEffects.CONDUIT_POWER).and(MobEffects.DOLPHINS_GRACE).and(MobEffects.BLINDNESS).and(MobEffects.BAD_OMEN).and(MobEffects.HERO_OF_THE_VILLAGE).and(MobEffects.DARKNESS).and(MobEffects.OOZING).and(MobEffects.INFESTED).and(MobEffects.WIND_CHARGED).and(MobEffects.WEAVING).and(MobEffects.TRIAL_OMEN).and(MobEffects.RAID_OMEN).and(MobEffects.BREATH_OF_THE_NAUTILUS)))
/* 153 */       .save(paramConsumer, "nether/all_effects");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     AdvancementHolder advancementHolder10 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.ANCIENT_DEBRIS, (Component)Component.translatable("advancements.nether.obtain_ancient_debris.title"), (Component)Component.translatable("advancements.nether.obtain_ancient_debris.description"), null, AdvancementType.TASK, true, true, false).addCriterion("ancient_debris", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.ANCIENT_DEBRIS })).save(paramConsumer, "nether/obtain_ancient_debris");
/*     */     
/* 161 */     Advancement.Builder.advancement()
/* 162 */       .parent(advancementHolder10)
/* 163 */       .display((ItemLike)Items.NETHERITE_CHESTPLATE, (Component)Component.translatable("advancements.nether.netherite_armor.title"), (Component)Component.translatable("advancements.nether.netherite_armor.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 164 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 165 */       .addCriterion("netherite_armor", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.NETHERITE_HELMET, (ItemLike)Items.NETHERITE_CHESTPLATE, (ItemLike)Items.NETHERITE_LEGGINGS, (ItemLike)Items.NETHERITE_BOOTS
/* 166 */           })).save(paramConsumer, "nether/netherite_armor");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     AdvancementHolder advancementHolder11 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.CRYING_OBSIDIAN, (Component)Component.translatable("advancements.nether.obtain_crying_obsidian.title"), (Component)Component.translatable("advancements.nether.obtain_crying_obsidian.description"), null, AdvancementType.TASK, true, true, false).addCriterion("crying_obsidian", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.CRYING_OBSIDIAN })).save(paramConsumer, "nether/obtain_crying_obsidian");
/*     */     
/* 174 */     Advancement.Builder.advancement()
/* 175 */       .parent(advancementHolder11)
/* 176 */       .display((ItemLike)Items.RESPAWN_ANCHOR, (Component)Component.translatable("advancements.nether.charge_respawn_anchor.title"), (Component)Component.translatable("advancements.nether.charge_respawn_anchor.description"), null, AdvancementType.TASK, true, true, false)
/* 177 */       .addCriterion("charge_respawn_anchor", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.RESPAWN_ANCHOR }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)RespawnAnchorBlock.CHARGE, 4))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Blocks.GLOWSTONE
/* 178 */             }))).save(paramConsumer, "nether/charge_respawn_anchor");
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
/* 189 */     AdvancementHolder advancementHolder12 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.WARPED_FUNGUS_ON_A_STICK, (Component)Component.translatable("advancements.nether.ride_strider.title"), (Component)Component.translatable("advancements.nether.ride_strider.description"), null, AdvancementType.TASK, true, true, false).addCriterion("used_warped_fungus_on_a_stick", ItemDurabilityTrigger.TriggerInstance.changedDurability(Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().vehicle(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.STRIDER)))), Optional.of(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.WARPED_FUNGUS_ON_A_STICK }).build()), MinMaxBounds.Ints.ANY)).save(paramConsumer, "nether/ride_strider");
/*     */     
/* 191 */     Advancement.Builder.advancement()
/* 192 */       .parent(advancementHolder12)
/* 193 */       .display((ItemLike)Items.WARPED_FUNGUS_ON_A_STICK, (Component)Component.translatable("advancements.nether.ride_strider_in_overworld_lava.title"), (Component)Component.translatable("advancements.nether.ride_strider_in_overworld_lava.description"), null, AdvancementType.TASK, true, true, false)
/* 194 */       .addCriterion("ride_entity_distance", DistanceTrigger.TriggerInstance.rideEntityInLava(
/* 195 */           EntityPredicate.Builder.entity().located(LocationPredicate.Builder.inDimension(Level.OVERWORLD)).vehicle(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.STRIDER)), 
/* 196 */           DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(50.0D))))
/*     */       
/* 198 */       .save(paramConsumer, "nether/ride_strider_in_overworld_lava");
/*     */     
/* 200 */     VanillaAdventureAdvancements.addBiomes(Advancement.Builder.advancement(), paramProvider, MultiNoiseBiomeSourceParameterList.Preset.NETHER.usedBiomes().toList())
/* 201 */       .parent(advancementHolder12)
/* 202 */       .display((ItemLike)Items.NETHERITE_BOOTS, (Component)Component.translatable("advancements.nether.explore_nether.title"), (Component)Component.translatable("advancements.nether.explore_nether.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 203 */       .rewards(AdvancementRewards.Builder.experience(500))
/* 204 */       .save(paramConsumer, "nether/explore_nether");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     AdvancementHolder advancementHolder13 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.POLISHED_BLACKSTONE_BRICKS, (Component)Component.translatable("advancements.nether.find_bastion.title"), (Component)Component.translatable("advancements.nether.find_bastion.description"), null, AdvancementType.TASK, true, true, false).addCriterion("bastion", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)paramProvider.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.BASTION_REMNANT)))).save(paramConsumer, "nether/find_bastion");
/*     */     
/* 212 */     Advancement.Builder.advancement()
/* 213 */       .parent(advancementHolder13)
/* 214 */       .display((ItemLike)Blocks.CHEST, (Component)Component.translatable("advancements.nether.loot_bastion.title"), (Component)Component.translatable("advancements.nether.loot_bastion.description"), null, AdvancementType.TASK, true, true, false)
/* 215 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 216 */       .addCriterion("loot_bastion_other", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_OTHER))
/* 217 */       .addCriterion("loot_bastion_treasure", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_TREASURE))
/* 218 */       .addCriterion("loot_bastion_hoglin_stable", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_HOGLIN_STABLE))
/* 219 */       .addCriterion("loot_bastion_bridge", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_BRIDGE))
/* 220 */       .save(paramConsumer, "nether/loot_bastion");
/*     */     
/* 222 */     ContextAwarePredicate contextAwarePredicate = ContextAwarePredicate.create(new LootItemCondition[] {
/* 223 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().head(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build(), 
/* 224 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().chest(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build(), 
/* 225 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().legs(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build(), 
/* 226 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().feet(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build()
/*     */         });
/*     */     
/* 229 */     Advancement.Builder.advancement()
/* 230 */       .parent(advancementHolder1)
/* 231 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 232 */       .display((ItemLike)Items.GOLD_INGOT, (Component)Component.translatable("advancements.nether.distract_piglin.title"), (Component)Component.translatable("advancements.nether.distract_piglin.description"), null, AdvancementType.TASK, true, true, false)
/* 233 */       .addCriterion("distract_piglin", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByEntity(contextAwarePredicate, 
/*     */           
/* 235 */           Optional.of(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.PIGLIN_LOVED).build()), 
/* 236 */           Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(false)))))))
/*     */       
/* 238 */       .addCriterion("distract_piglin_directly", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
/* 239 */           Optional.of(contextAwarePredicate), 
/* 240 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)PiglinAi.BARTERING_ITEM
/* 241 */             }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(false)))))))
/*     */       
/* 243 */       .save(paramConsumer, "nether/distract_piglin");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\advancements\packs\VanillaNetherAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */