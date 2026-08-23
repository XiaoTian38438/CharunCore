/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.advancements.criterion.ChanneledLightningTrigger;
/*     */ import net.minecraft.advancements.criterion.DamagePredicate;
/*     */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.DistancePredicate;
/*     */ import net.minecraft.advancements.criterion.DistanceTrigger;
/*     */ import net.minecraft.advancements.criterion.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.EntitySubPredicate;
/*     */ import net.minecraft.advancements.criterion.FallAfterExplosionTrigger;
/*     */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
/*     */ import net.minecraft.advancements.criterion.KilledByArrowTrigger;
/*     */ import net.minecraft.advancements.criterion.KilledTrigger;
/*     */ import net.minecraft.advancements.criterion.LightningBoltPredicate;
/*     */ import net.minecraft.advancements.criterion.LightningStrikeTrigger;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.LootTableTrigger;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.PlayerHurtEntityTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerPredicate;
/*     */ import net.minecraft.advancements.criterion.PlayerTrigger;
/*     */ import net.minecraft.advancements.criterion.RecipeCraftedTrigger;
/*     */ import net.minecraft.advancements.criterion.ShotCrossbowTrigger;
/*     */ import net.minecraft.advancements.criterion.SlideDownBlockTrigger;
/*     */ import net.minecraft.advancements.criterion.SpearMobsTrigger;
/*     */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*     */ import net.minecraft.advancements.criterion.SummonedEntityTrigger;
/*     */ import net.minecraft.advancements.criterion.TagPredicate;
/*     */ import net.minecraft.advancements.criterion.TargetBlockTrigger;
/*     */ import net.minecraft.advancements.criterion.TradeTrigger;
/*     */ import net.minecraft.advancements.criterion.UsedTotemTrigger;
/*     */ import net.minecraft.advancements.criterion.UsingItemTrigger;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.JukeboxPlayablePredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ComparatorBlock;
/*     */ import net.minecraft.world.level.block.CopperBulbBlock;
/*     */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.PotDecorations;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.slf4j.Logger;
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
/*     */ public class VanillaAdventureAdvancements
/*     */   implements AdvancementSubProvider
/*     */ {
/* 143 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int DISTANCE_FROM_BOTTOM_TO_TOP = 384;
/*     */   
/*     */   private static final int Y_COORDINATE_AT_TOP = 320;
/*     */   
/*     */   private static final int Y_COORDINATE_AT_BOTTOM = -64;
/*     */   
/*     */   private static final int BEDROCK_THICKNESS = 5;
/* 152 */   private static final Map<MobCategory, Set<EntityType<?>>> EXCEPTIONS_BY_EXPECTED_CATEGORIES = Map.of(MobCategory.MONSTER, 
/* 153 */       Set.of(EntityType.GIANT, EntityType.ILLUSIONER, EntityType.WARDEN));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   private static final List<EntityType<?>> MOBS_TO_KILL = Arrays.asList((EntityType<?>[])new EntityType[] { EntityType.BLAZE, EntityType.BOGGED, EntityType.BREEZE, EntityType.CAMEL_HUSK, EntityType.CAVE_SPIDER, EntityType.CREAKING, EntityType.CREEPER, EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.ENDER_DRAGON, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.PARCHED, EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.WITHER, EntityType.ZOGLIN, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIE_NAUTILUS });
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
/*     */   private static Criterion<LightningStrikeTrigger.TriggerInstance> fireCountAndBystander(MinMaxBounds.Ints paramInts, Optional<EntityPredicate> paramOptional) {
/* 206 */     return LightningStrikeTrigger.TriggerInstance.lightningStrike(
/* 207 */         Optional.of(EntityPredicate.Builder.entity()
/* 208 */           .distance(DistancePredicate.absolute(MinMaxBounds.Doubles.atMost(30.0D)))
/* 209 */           .subPredicate((EntitySubPredicate)LightningBoltPredicate.blockSetOnFire(paramInts))
/* 210 */           .build()), paramOptional);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Criterion<UsingItemTrigger.TriggerInstance> lookAtThroughItem(EntityPredicate.Builder paramBuilder, ItemPredicate.Builder paramBuilder1) {
/* 216 */     return UsingItemTrigger.TriggerInstance.lookingAt(
/* 217 */         EntityPredicate.Builder.entity().subPredicate(
/* 218 */           (EntitySubPredicate)PlayerPredicate.Builder.player().setLookingAt(paramBuilder)
/*     */           
/* 220 */           .build()), paramBuilder1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(HolderLookup.Provider paramProvider, Consumer<AdvancementHolder> paramConsumer) {
/* 228 */     HolderLookup.RegistryLookup registryLookup1 = paramProvider.lookupOrThrow(Registries.ENTITY_TYPE);
/* 229 */     HolderLookup.RegistryLookup registryLookup2 = paramProvider.lookupOrThrow(Registries.ITEM);
/* 230 */     HolderLookup.RegistryLookup registryLookup3 = paramProvider.lookupOrThrow(Registries.BLOCK);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     AdvancementHolder advancementHolder1 = Advancement.Builder.advancement().display((ItemLike)Items.MAP, (Component)Component.translatable("advancements.adventure.root.title"), (Component)Component.translatable("advancements.adventure.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"), AdvancementType.TASK, false, false, false).requirements(AdvancementRequirements.Strategy.OR).addCriterion("killed_something", KilledTrigger.TriggerInstance.playerKilledEntity()).addCriterion("killed_by_something", KilledTrigger.TriggerInstance.entityKilledPlayer()).save(paramConsumer, "adventure/root");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 243 */     AdvancementHolder advancementHolder2 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Blocks.RED_BED, (Component)Component.translatable("advancements.adventure.sleep_in_bed.title"), (Component)Component.translatable("advancements.adventure.sleep_in_bed.description"), null, AdvancementType.TASK, true, true, false).addCriterion("slept_in_bed", PlayerTrigger.TriggerInstance.sleptInBed()).save(paramConsumer, "adventure/sleep_in_bed");
/*     */     
/* 245 */     createAdventuringTime(paramProvider, paramConsumer, advancementHolder2, MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     AdvancementHolder advancementHolder3 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.EMERALD, (Component)Component.translatable("advancements.adventure.trade.title"), (Component)Component.translatable("advancements.adventure.trade.description"), null, AdvancementType.TASK, true, true, false).addCriterion("traded", TradeTrigger.TriggerInstance.tradedWithVillager()).save(paramConsumer, "adventure/trade");
/*     */     
/* 253 */     Advancement.Builder.advancement()
/* 254 */       .parent(advancementHolder3)
/* 255 */       .display((ItemLike)Items.EMERALD, (Component)Component.translatable("advancements.adventure.trade_at_world_height.title"), (Component)Component.translatable("advancements.adventure.trade_at_world_height.description"), null, AdvancementType.TASK, true, true, false)
/* 256 */       .addCriterion("trade_at_world_height", TradeTrigger.TriggerInstance.tradedWithVillager(EntityPredicate.Builder.entity().located(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atLeast(319.0D)))))
/* 257 */       .save(paramConsumer, "adventure/trade_at_world_height");
/*     */     
/* 259 */     AdvancementHolder advancementHolder4 = createMonsterHunterAdvancement(advancementHolder1, paramConsumer, (HolderGetter<EntityType<?>>)registryLookup1, validateMobsToKill(MOBS_TO_KILL, (HolderLookup<EntityType<?>>)registryLookup1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     AdvancementHolder advancementHolder5 = Advancement.Builder.advancement().parent(advancementHolder4).display((ItemLike)Items.BOW, (Component)Component.translatable("advancements.adventure.shoot_arrow.title"), (Component)Component.translatable("advancements.adventure.shoot_arrow.description"), null, AdvancementType.TASK, true, true, false).addCriterion("shot_arrow", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntityWithDamage(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypeTags.ARROWS))))).save(paramConsumer, "adventure/shoot_arrow");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 271 */     AdvancementHolder advancementHolder6 = Advancement.Builder.advancement().parent(advancementHolder4).display((ItemLike)Items.TRIDENT, (Component)Component.translatable("advancements.adventure.throw_trident.title"), (Component)Component.translatable("advancements.adventure.throw_trident.description"), null, AdvancementType.TASK, true, true, false).addCriterion("shot_trident", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntityWithDamage(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.TRIDENT))))).save(paramConsumer, "adventure/throw_trident");
/*     */     
/* 273 */     Advancement.Builder.advancement()
/* 274 */       .parent(advancementHolder6)
/* 275 */       .display((ItemLike)Items.TRIDENT, (Component)Component.translatable("advancements.adventure.very_very_frightening.title"), (Component)Component.translatable("advancements.adventure.very_very_frightening.description"), null, AdvancementType.TASK, true, true, false)
/* 276 */       .addCriterion("struck_villager", ChanneledLightningTrigger.TriggerInstance.channeledLightning(new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.VILLAGER)
/* 277 */           })).save(paramConsumer, "adventure/very_very_frightening");
/*     */     
/* 279 */     Advancement.Builder.advancement()
/* 280 */       .parent(advancementHolder3)
/* 281 */       .display((ItemLike)Blocks.CARVED_PUMPKIN, (Component)Component.translatable("advancements.adventure.summon_iron_golem.title"), (Component)Component.translatable("advancements.adventure.summon_iron_golem.description"), null, AdvancementType.GOAL, true, true, false)
/* 282 */       .addCriterion("summoned_golem", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.IRON_GOLEM)))
/* 283 */       .save(paramConsumer, "adventure/summon_iron_golem");
/*     */     
/* 285 */     Advancement.Builder.advancement()
/* 286 */       .parent(advancementHolder5)
/* 287 */       .display((ItemLike)Items.ARROW, (Component)Component.translatable("advancements.adventure.sniper_duel.title"), (Component)Component.translatable("advancements.adventure.sniper_duel.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 288 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 289 */       .addCriterion("killed_skeleton", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.SKELETON).distance(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(50.0D))), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))))
/* 290 */       .save(paramConsumer, "adventure/sniper_duel");
/*     */     
/* 292 */     Advancement.Builder.advancement()
/* 293 */       .parent(advancementHolder4)
/* 294 */       .display((ItemLike)Items.TOTEM_OF_UNDYING, (Component)Component.translatable("advancements.adventure.totem_of_undying.title"), (Component)Component.translatable("advancements.adventure.totem_of_undying.description"), null, AdvancementType.GOAL, true, true, false)
/* 295 */       .addCriterion("used_totem", UsedTotemTrigger.TriggerInstance.usedTotem((HolderGetter)registryLookup2, (ItemLike)Items.TOTEM_OF_UNDYING))
/* 296 */       .save(paramConsumer, "adventure/totem_of_undying");
/*     */     
/* 298 */     Advancement.Builder.advancement()
/* 299 */       .parent(advancementHolder4)
/* 300 */       .display((ItemLike)Items.IRON_SPEAR, (Component)Component.translatable("advancements.adventure.spear_many_mobs.title"), (Component)Component.translatable("advancements.adventure.spear_many_mobs.description"), null, AdvancementType.GOAL, true, true, false)
/* 301 */       .addCriterion("spear_many_mobs", SpearMobsTrigger.TriggerInstance.spearMobs(5))
/* 302 */       .save(paramConsumer, "adventure/spear_many_mobs");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 308 */     AdvancementHolder advancementHolder7 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.ol_betsy.title"), (Component)Component.translatable("advancements.adventure.ol_betsy.description"), null, AdvancementType.TASK, true, true, false).addCriterion("shot_crossbow", ShotCrossbowTrigger.TriggerInstance.shotCrossbow((HolderGetter)registryLookup2, (ItemLike)Items.CROSSBOW)).save(paramConsumer, "adventure/ol_betsy");
/*     */     
/* 310 */     Advancement.Builder.advancement()
/* 311 */       .parent(advancementHolder7)
/* 312 */       .display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.whos_the_pillager_now.title"), (Component)Component.translatable("advancements.adventure.whos_the_pillager_now.description"), null, AdvancementType.TASK, true, true, false)
/* 313 */       .addCriterion("kill_pillager", KilledByArrowTrigger.TriggerInstance.crossbowKilled((HolderGetter)registryLookup2, new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PILLAGER)
/* 314 */           })).save(paramConsumer, "adventure/whos_the_pillager_now");
/*     */     
/* 316 */     Advancement.Builder.advancement()
/* 317 */       .parent(advancementHolder7)
/* 318 */       .display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.two_birds_one_arrow.title"), (Component)Component.translatable("advancements.adventure.two_birds_one_arrow.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 319 */       .rewards(AdvancementRewards.Builder.experience(65))
/* 320 */       .addCriterion("two_birds", KilledByArrowTrigger.TriggerInstance.crossbowKilled((HolderGetter)registryLookup2, new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PHANTOM), EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PHANTOM)
/* 321 */           })).save(paramConsumer, "adventure/two_birds_one_arrow");
/*     */     
/* 323 */     Advancement.Builder.advancement()
/* 324 */       .parent(advancementHolder7)
/* 325 */       .display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.arbalistic.title"), (Component)Component.translatable("advancements.adventure.arbalistic.description"), null, AdvancementType.CHALLENGE, true, true, true)
/* 326 */       .rewards(AdvancementRewards.Builder.experience(85))
/* 327 */       .addCriterion("arbalistic", KilledByArrowTrigger.TriggerInstance.crossbowKilled((HolderGetter)registryLookup2, MinMaxBounds.Ints.exactly(5)))
/* 328 */       .save(paramConsumer, "adventure/arbalistic");
/*     */     
/* 330 */     HolderLookup.RegistryLookup registryLookup4 = paramProvider.lookupOrThrow(Registries.BANNER_PATTERN);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 335 */     AdvancementHolder advancementHolder8 = Advancement.Builder.advancement().parent(advancementHolder1).display(Raid.getOminousBannerInstance((HolderGetter)registryLookup4), (Component)Component.translatable("advancements.adventure.voluntary_exile.title"), (Component)Component.translatable("advancements.adventure.voluntary_exile.description"), null, AdvancementType.TASK, true, true, true).addCriterion("voluntary_exile", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypeTags.RAIDERS).equipment(EntityEquipmentPredicate.captainPredicate((HolderGetter)registryLookup2, (HolderGetter)registryLookup4)))).save(paramConsumer, "adventure/voluntary_exile");
/*     */     
/* 337 */     Advancement.Builder.advancement()
/* 338 */       .parent(advancementHolder8)
/* 339 */       .display(Raid.getOminousBannerInstance((HolderGetter)registryLookup4), (Component)Component.translatable("advancements.adventure.hero_of_the_village.title"), (Component)Component.translatable("advancements.adventure.hero_of_the_village.description"), null, AdvancementType.CHALLENGE, true, true, true)
/* 340 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 341 */       .addCriterion("hero_of_the_village", PlayerTrigger.TriggerInstance.raidWon())
/* 342 */       .save(paramConsumer, "adventure/hero_of_the_village");
/*     */     
/* 344 */     Advancement.Builder.advancement()
/* 345 */       .parent(advancementHolder1)
/* 346 */       .display((ItemLike)Blocks.HONEY_BLOCK.asItem(), (Component)Component.translatable("advancements.adventure.honey_block_slide.title"), (Component)Component.translatable("advancements.adventure.honey_block_slide.description"), null, AdvancementType.TASK, true, true, false)
/* 347 */       .addCriterion("honey_block_slide", SlideDownBlockTrigger.TriggerInstance.slidesDownBlock(Blocks.HONEY_BLOCK))
/* 348 */       .save(paramConsumer, "adventure/honey_block_slide");
/*     */     
/* 350 */     Advancement.Builder.advancement()
/* 351 */       .parent(advancementHolder5)
/* 352 */       .display((ItemLike)Blocks.TARGET.asItem(), (Component)Component.translatable("advancements.adventure.bullseye.title"), (Component)Component.translatable("advancements.adventure.bullseye.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 353 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 354 */       .addCriterion("bullseye", TargetBlockTrigger.TriggerInstance.targetHit(MinMaxBounds.Ints.exactly(15), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().distance(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(30.0D)))))))
/* 355 */       .save(paramConsumer, "adventure/bullseye");
/*     */     
/* 357 */     Advancement.Builder.advancement()
/* 358 */       .parent(advancementHolder2)
/* 359 */       .display((ItemLike)Items.LEATHER_BOOTS, (Component)Component.translatable("advancements.adventure.walk_on_powder_snow_with_leather_boots.title"), (Component)Component.translatable("advancements.adventure.walk_on_powder_snow_with_leather_boots.description"), null, AdvancementType.TASK, true, true, false)
/* 360 */       .addCriterion("walk_on_powder_snow_with_leather_boots", PlayerTrigger.TriggerInstance.walkOnBlockWithEquipment((HolderGetter)registryLookup3, (HolderGetter)registryLookup2, Blocks.POWDER_SNOW, Items.LEATHER_BOOTS))
/* 361 */       .save(paramConsumer, "adventure/walk_on_powder_snow_with_leather_boots");
/*     */     
/* 363 */     Advancement.Builder.advancement()
/* 364 */       .parent(advancementHolder1)
/* 365 */       .display((ItemLike)Items.LIGHTNING_ROD, (Component)Component.translatable("advancements.adventure.lightning_rod_with_villager_no_fire.title"), (Component)Component.translatable("advancements.adventure.lightning_rod_with_villager_no_fire.description"), null, AdvancementType.TASK, true, true, false)
/* 366 */       .addCriterion("lightning_rod_with_villager_no_fire", fireCountAndBystander(MinMaxBounds.Ints.exactly(0), Optional.of(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.VILLAGER).build())))
/* 367 */       .save(paramConsumer, "adventure/lightning_rod_with_villager_no_fire");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 373 */     AdvancementHolder advancementHolder9 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.SPYGLASS, (Component)Component.translatable("advancements.adventure.spyglass_at_parrot.title"), (Component)Component.translatable("advancements.adventure.spyglass_at_parrot.description"), null, AdvancementType.TASK, true, true, false).addCriterion("spyglass_at_parrot", lookAtThroughItem(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PARROT), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.SPYGLASS }))).save(paramConsumer, "adventure/spyglass_at_parrot");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 379 */     AdvancementHolder advancementHolder10 = Advancement.Builder.advancement().parent(advancementHolder9).display((ItemLike)Items.SPYGLASS, (Component)Component.translatable("advancements.adventure.spyglass_at_ghast.title"), (Component)Component.translatable("advancements.adventure.spyglass_at_ghast.description"), null, AdvancementType.TASK, true, true, false).addCriterion("spyglass_at_ghast", lookAtThroughItem(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.GHAST), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.SPYGLASS }))).save(paramConsumer, "adventure/spyglass_at_ghast");
/*     */     
/* 381 */     Advancement.Builder.advancement()
/* 382 */       .parent(advancementHolder2)
/* 383 */       .display((ItemLike)Items.JUKEBOX, (Component)Component.translatable("advancements.adventure.play_jukebox_in_meadows.title"), (Component)Component.translatable("advancements.adventure.play_jukebox_in_meadows.description"), null, AdvancementType.TASK, true, true, false)
/* 384 */       .addCriterion("play_jukebox_in_meadows", 
/* 385 */         ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
/* 386 */           LocationPredicate.Builder.location()
/* 387 */           .setBiomes((HolderSet)HolderSet.direct(new Holder[] { (Holder)paramProvider.lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.MEADOW)
/* 388 */               }, )).setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.JUKEBOX
/* 389 */               })), ItemPredicate.Builder.item()
/* 390 */           .withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.JUKEBOX_PLAYABLE, (DataComponentPredicate)JukeboxPlayablePredicate.any()).build())))
/*     */ 
/*     */       
/* 393 */       .save(paramConsumer, "adventure/play_jukebox_in_meadows");
/*     */     
/* 395 */     Advancement.Builder.advancement()
/* 396 */       .parent(advancementHolder10)
/* 397 */       .display((ItemLike)Items.SPYGLASS, (Component)Component.translatable("advancements.adventure.spyglass_at_dragon.title"), (Component)Component.translatable("advancements.adventure.spyglass_at_dragon.description"), null, AdvancementType.TASK, true, true, false)
/* 398 */       .addCriterion("spyglass_at_dragon", lookAtThroughItem(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.ENDER_DRAGON), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.SPYGLASS
/* 399 */             }))).save(paramConsumer, "adventure/spyglass_at_dragon");
/*     */     
/* 401 */     Advancement.Builder.advancement()
/* 402 */       .parent(advancementHolder1)
/* 403 */       .display((ItemLike)Items.WATER_BUCKET, (Component)Component.translatable("advancements.adventure.fall_from_world_height.title"), (Component)Component.translatable("advancements.adventure.fall_from_world_height.description"), null, AdvancementType.TASK, true, true, false)
/* 404 */       .addCriterion("fall_from_world_height", 
/* 405 */         DistanceTrigger.TriggerInstance.fallFromHeight(
/* 406 */           EntityPredicate.Builder.entity().located(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atMost(-59.0D))), 
/* 407 */           DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(379.0D)), 
/* 408 */           LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atLeast(319.0D))))
/*     */ 
/*     */       
/* 411 */       .save(paramConsumer, "adventure/fall_from_world_height");
/*     */     
/* 413 */     Advancement.Builder.advancement()
/* 414 */       .parent(advancementHolder4)
/* 415 */       .display((ItemLike)Blocks.SCULK_CATALYST, (Component)Component.translatable("advancements.adventure.kill_mob_near_sculk_catalyst.title"), (Component)Component.translatable("advancements.adventure.kill_mob_near_sculk_catalyst.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 416 */       .addCriterion("kill_mob_near_sculk_catalyst", KilledTrigger.TriggerInstance.playerKilledEntityNearSculkCatalyst())
/* 417 */       .save(paramConsumer, "adventure/kill_mob_near_sculk_catalyst");
/*     */     
/* 419 */     Advancement.Builder.advancement()
/* 420 */       .parent(advancementHolder1)
/* 421 */       .display((ItemLike)Blocks.SCULK_SENSOR, (Component)Component.translatable("advancements.adventure.avoid_vibration.title"), (Component)Component.translatable("advancements.adventure.avoid_vibration.description"), null, AdvancementType.TASK, true, true, false)
/* 422 */       .addCriterion("avoid_vibration", PlayerTrigger.TriggerInstance.avoidVibration())
/* 423 */       .save(paramConsumer, "adventure/avoid_vibration");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 428 */     AdvancementHolder advancementHolder11 = respectingTheRemnantsCriterions((HolderGetter<Item>)registryLookup2, Advancement.Builder.advancement()).parent(advancementHolder1).display((ItemLike)Items.BRUSH, (Component)Component.translatable("advancements.adventure.salvage_sherd.title"), (Component)Component.translatable("advancements.adventure.salvage_sherd.description"), null, AdvancementType.TASK, true, true, false).save(paramConsumer, "adventure/salvage_sherd");
/*     */     
/* 430 */     Advancement.Builder.advancement()
/* 431 */       .parent(advancementHolder11)
/* 432 */       .display(
/* 433 */         DecoratedPotBlockEntity.createDecoratedPotItem(new PotDecorations(Optional.empty(), Optional.of(Items.HEART_POTTERY_SHERD), Optional.empty(), Optional.of(Items.EXPLORER_POTTERY_SHERD))), 
/* 434 */         (Component)Component.translatable("advancements.adventure.craft_decorated_pot_using_only_sherds.title"), 
/* 435 */         (Component)Component.translatable("advancements.adventure.craft_decorated_pot_using_only_sherds.description"), null, AdvancementType.TASK, true, true, false)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 442 */       .addCriterion("pot_crafted_using_only_sherds", 
/* 443 */         RecipeCraftedTrigger.TriggerInstance.craftedItem(
/* 444 */           ResourceKey.create(Registries.RECIPE, Identifier.withDefaultNamespace("decorated_pot")), 
/* 445 */           List.of(
/* 446 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS), 
/* 447 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS), 
/* 448 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS), 
/* 449 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS))))
/*     */ 
/*     */ 
/*     */       
/* 453 */       .save(paramConsumer, "adventure/craft_decorated_pot_using_only_sherds");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 458 */     AdvancementHolder advancementHolder12 = craftingANewLook(Advancement.Builder.advancement()).parent(advancementHolder1).display(new ItemStack((ItemLike)Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE), (Component)Component.translatable("advancements.adventure.trim_with_any_armor_pattern.title"), (Component)Component.translatable("advancements.adventure.trim_with_any_armor_pattern.description"), null, AdvancementType.TASK, true, true, false).save(paramConsumer, "adventure/trim_with_any_armor_pattern");
/*     */     
/* 460 */     smithingWithStyle(Advancement.Builder.advancement())
/* 461 */       .parent(advancementHolder12)
/* 462 */       .display(new ItemStack((ItemLike)Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE), (Component)Component.translatable("advancements.adventure.trim_with_all_exclusive_armor_patterns.title"), (Component)Component.translatable("advancements.adventure.trim_with_all_exclusive_armor_patterns.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 463 */       .rewards(AdvancementRewards.Builder.experience(150))
/* 464 */       .save(paramConsumer, "adventure/trim_with_all_exclusive_armor_patterns");
/*     */     
/* 466 */     Advancement.Builder.advancement()
/* 467 */       .parent(advancementHolder1)
/* 468 */       .display((ItemLike)Items.CHISELED_BOOKSHELF, (Component)Component.translatable("advancements.adventure.read_power_from_chiseled_bookshelf.title"), (Component)Component.translatable("advancements.adventure.read_power_from_chiseled_bookshelf.description"), null, AdvancementType.TASK, true, true, false)
/* 469 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 470 */       .addCriterion("chiseled_bookshelf", placedBlockReadByComparator((HolderGetter<Block>)registryLookup3, Blocks.CHISELED_BOOKSHELF))
/* 471 */       .addCriterion("comparator", placedComparatorReadingBlock((HolderGetter<Block>)registryLookup3, Blocks.CHISELED_BOOKSHELF))
/* 472 */       .save(paramConsumer, "adventure/read_power_of_chiseled_bookshelf");
/*     */     
/* 474 */     Advancement.Builder.advancement()
/* 475 */       .parent(advancementHolder1)
/* 476 */       .display((ItemLike)Items.ARMADILLO_SCUTE, (Component)Component.translatable("advancements.adventure.brush_armadillo.title"), (Component)Component.translatable("advancements.adventure.brush_armadillo.description"), null, AdvancementType.TASK, true, true, false)
/* 477 */       .addCriterion("brush_armadillo", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
/* 478 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.BRUSH
/* 479 */             }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.ARMADILLO)))))
/*     */       
/* 481 */       .save(paramConsumer, "adventure/brush_armadillo");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 487 */     AdvancementHolder advancementHolder13 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Blocks.CHISELED_TUFF, (Component)Component.translatable("advancements.adventure.minecraft_trials_edition.title"), (Component)Component.translatable("advancements.adventure.minecraft_trials_edition.description"), null, AdvancementType.TASK, true, true, false).addCriterion("minecraft_trials_edition", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)paramProvider.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.TRIAL_CHAMBERS)))).save(paramConsumer, "adventure/minecraft_trials_edition");
/*     */     
/* 489 */     Advancement.Builder.advancement()
/* 490 */       .parent(advancementHolder13)
/* 491 */       .display((ItemLike)Items.COPPER_BULB, (Component)Component.translatable("advancements.adventure.lighten_up.title"), (Component)Component.translatable("advancements.adventure.lighten_up.description"), null, AdvancementType.TASK, true, true, false)
/* 492 */       .addCriterion("lighten_up", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.OXIDIZED_COPPER_BULB, Blocks.WEATHERED_COPPER_BULB, Blocks.EXPOSED_COPPER_BULB, Blocks.WAXED_OXIDIZED_COPPER_BULB, Blocks.WAXED_WEATHERED_COPPER_BULB, Blocks.WAXED_EXPOSED_COPPER_BULB }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CopperBulbBlock.LIT, true))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, (ItemLike[])VanillaHusbandryAdvancements.WAX_SCRAPING_TOOLS)))
/* 493 */       .save(paramConsumer, "adventure/lighten_up");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 499 */     AdvancementHolder advancementHolder14 = Advancement.Builder.advancement().parent(advancementHolder13).display((ItemLike)Items.TRIAL_KEY, (Component)Component.translatable("advancements.adventure.under_lock_and_key.title"), (Component)Component.translatable("advancements.adventure.under_lock_and_key.description"), null, AdvancementType.TASK, true, true, false).addCriterion("under_lock_and_key", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.VAULT }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)VaultBlock.OMINOUS, false))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.TRIAL_KEY }))).save(paramConsumer, "adventure/under_lock_and_key");
/*     */     
/* 501 */     Advancement.Builder.advancement()
/* 502 */       .parent(advancementHolder14)
/* 503 */       .display((ItemLike)Items.OMINOUS_TRIAL_KEY, (Component)Component.translatable("advancements.adventure.revaulting.title"), (Component)Component.translatable("advancements.adventure.revaulting.description"), null, AdvancementType.GOAL, true, true, false)
/* 504 */       .addCriterion("revaulting", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.VAULT }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)VaultBlock.OMINOUS, true))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.OMINOUS_TRIAL_KEY
/* 505 */             }))).save(paramConsumer, "adventure/revaulting");
/*     */     
/* 507 */     Advancement.Builder.advancement()
/* 508 */       .parent(advancementHolder13)
/* 509 */       .display((ItemLike)Items.WIND_CHARGE, (Component)Component.translatable("advancements.adventure.blowback.title"), (Component)Component.translatable("advancements.adventure.blowback.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 510 */       .rewards(AdvancementRewards.Builder.experience(40))
/* 511 */       .addCriterion("blowback", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.BREEZE), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.BREEZE_WIND_CHARGE))))
/* 512 */       .save(paramConsumer, "adventure/blowback");
/*     */     
/* 514 */     Advancement.Builder.advancement()
/* 515 */       .parent(advancementHolder1)
/* 516 */       .display((ItemLike)Items.CRAFTER, (Component)Component.translatable("advancements.adventure.crafters_crafting_crafters.title"), (Component)Component.translatable("advancements.adventure.crafters_crafting_crafters.description"), null, AdvancementType.TASK, true, true, false)
/* 517 */       .addCriterion("crafter_crafted_crafter", RecipeCraftedTrigger.TriggerInstance.crafterCraftedItem(ResourceKey.create(Registries.RECIPE, Identifier.withDefaultNamespace("crafter"))))
/* 518 */       .save(paramConsumer, "adventure/crafters_crafting_crafters");
/*     */     
/* 520 */     Advancement.Builder.advancement()
/* 521 */       .parent(advancementHolder1)
/* 522 */       .display((ItemLike)Items.LODESTONE, (Component)Component.translatable("advancements.adventure.use_lodestone.title"), (Component)Component.translatable("advancements.adventure.use_lodestone.description"), null, AdvancementType.TASK, true, true, false)
/* 523 */       .addCriterion("use_lodestone", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.LODESTONE })), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.COMPASS
/* 524 */             }))).save(paramConsumer, "adventure/use_lodestone");
/*     */     
/* 526 */     Advancement.Builder.advancement()
/* 527 */       .parent(advancementHolder13)
/* 528 */       .display((ItemLike)Items.WIND_CHARGE, (Component)Component.translatable("advancements.adventure.who_needs_rockets.title"), (Component)Component.translatable("advancements.adventure.who_needs_rockets.description"), null, AdvancementType.TASK, true, true, false)
/* 529 */       .addCriterion("who_needs_rockets", FallAfterExplosionTrigger.TriggerInstance.fallAfterExplosion(DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(7.0D)), EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.WIND_CHARGE)))
/* 530 */       .save(paramConsumer, "adventure/who_needs_rockets");
/*     */     
/* 532 */     Advancement.Builder.advancement()
/* 533 */       .parent(advancementHolder13)
/* 534 */       .display((ItemLike)Items.MACE, (Component)Component.translatable("advancements.adventure.overoverkill.title"), (Component)Component.translatable("advancements.adventure.overoverkill.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 535 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 536 */       .addCriterion("overoverkill", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntityWithDamage(DamagePredicate.Builder.damageInstance().dealtDamage(MinMaxBounds.Doubles.atLeast(100.0D)).type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_MACE_SMASH)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PLAYER).equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.MACE
/* 537 */                     }))))))).save(paramConsumer, "adventure/overoverkill");
/*     */     
/* 539 */     Advancement.Builder.advancement()
/* 540 */       .parent(advancementHolder1)
/* 541 */       .display((ItemLike)Blocks.CREAKING_HEART, (Component)Component.translatable("advancements.adventure.heart_transplanter.title"), (Component)Component.translatable("advancements.adventure.heart_transplanter.description"), null, AdvancementType.TASK, true, true, false)
/* 542 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 543 */       .addCriterion("place_creaking_heart_dormant", ItemUsedOnLocationTrigger.TriggerInstance.placedBlockWithProperties(Blocks.CREAKING_HEART, (Property)BlockStateProperties.CREAKING_HEART_STATE, (Comparable)CreakingHeartState.DORMANT))
/* 544 */       .addCriterion("place_creaking_heart_awake", ItemUsedOnLocationTrigger.TriggerInstance.placedBlockWithProperties(Blocks.CREAKING_HEART, (Property)BlockStateProperties.CREAKING_HEART_STATE, (Comparable)CreakingHeartState.AWAKE))
/* 545 */       .addCriterion("place_pale_oak_log", placedBlockActivatesCreakingHeart((HolderGetter<Block>)registryLookup3, BlockTags.PALE_OAK_LOGS))
/* 546 */       .save(paramConsumer, "adventure/heart_transplanter");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AdvancementHolder createMonsterHunterAdvancement(AdvancementHolder paramAdvancementHolder, Consumer<AdvancementHolder> paramConsumer, HolderGetter<EntityType<?>> paramHolderGetter, List<EntityType<?>> paramList) {
/* 554 */     AdvancementHolder advancementHolder = addMobsToKill(Advancement.Builder.advancement(), paramHolderGetter, paramList).parent(paramAdvancementHolder).display((ItemLike)Items.IRON_SWORD, (Component)Component.translatable("advancements.adventure.kill_a_mob.title"), (Component)Component.translatable("advancements.adventure.kill_a_mob.description"), null, AdvancementType.TASK, true, true, false).requirements(AdvancementRequirements.Strategy.OR).save(paramConsumer, "adventure/kill_a_mob");
/*     */     
/* 556 */     addMobsToKill(Advancement.Builder.advancement(), paramHolderGetter, paramList)
/* 557 */       .parent(advancementHolder)
/* 558 */       .display((ItemLike)Items.DIAMOND_SWORD, (Component)Component.translatable("advancements.adventure.kill_all_mobs.title"), (Component)Component.translatable("advancements.adventure.kill_all_mobs.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 559 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 560 */       .save(paramConsumer, "adventure/kill_all_mobs");
/*     */     
/* 562 */     return advancementHolder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedBlockReadByComparator(HolderGetter<Block> paramHolderGetter, Block paramBlock) {
/* 571 */     LootItemCondition.Builder[] arrayOfBuilder = (LootItemCondition.Builder[])ComparatorBlock.FACING.getPossibleValues().stream().map(paramDirection -> { StatePropertiesPredicate.Builder builder = StatePropertiesPredicate.Builder.properties().hasProperty((Property)ComparatorBlock.FACING, (Comparable)paramDirection); BlockPredicate.Builder builder1 = BlockPredicate.Builder.block().of(paramHolderGetter, new Block[] { Blocks.COMPARATOR }).setProperties(builder); return LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(builder1), new BlockPos(paramDirection.getOpposite().getUnitVec3i())); }).toArray(paramInt -> new LootItemCondition.Builder[paramInt]);
/*     */     
/* 573 */     return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(new LootItemCondition.Builder[] {
/* 574 */           (LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock), 
/* 575 */           (LootItemCondition.Builder)AnyOfCondition.anyOf(arrayOfBuilder)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedComparatorReadingBlock(HolderGetter<Block> paramHolderGetter, Block paramBlock) {
/* 585 */     LootItemCondition.Builder[] arrayOfBuilder = (LootItemCondition.Builder[])ComparatorBlock.FACING.getPossibleValues().stream().map(paramDirection -> { StatePropertiesPredicate.Builder builder = StatePropertiesPredicate.Builder.properties().hasProperty((Property)ComparatorBlock.FACING, (Comparable)paramDirection); LootItemBlockStatePropertyCondition.Builder builder1 = (new LootItemBlockStatePropertyCondition.Builder(Blocks.COMPARATOR)).setProperties(builder); LootItemCondition.Builder builder2 = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(paramHolderGetter, new Block[] { paramBlock })), new BlockPos(paramDirection.getUnitVec3i())); return AllOfCondition.allOf(new LootItemCondition.Builder[] { (LootItemCondition.Builder)builder1, builder2 }); }).toArray(paramInt -> new LootItemCondition.Builder[paramInt]);
/*     */     
/* 587 */     return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(new LootItemCondition.Builder[] {
/* 588 */           (LootItemCondition.Builder)AnyOfCondition.anyOf(arrayOfBuilder)
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedBlockActivatesCreakingHeart(HolderGetter<Block> paramHolderGetter, TagKey<Block> paramTagKey) {
/* 608 */     LootItemCondition.Builder[] arrayOfBuilder = (LootItemCondition.Builder[])Stream.<Direction>of(Direction.values()).map(paramDirection -> { StatePropertiesPredicate.Builder builder = StatePropertiesPredicate.Builder.properties().hasProperty((Property)CreakingHeartBlock.AXIS, (Comparable)paramDirection.getAxis()); BlockPredicate.Builder builder1 = BlockPredicate.Builder.block().of(paramHolderGetter, paramTagKey).setProperties(builder); Vec3i vec3i = paramDirection.getUnitVec3i(); LootItemCondition.Builder builder2 = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(builder1)); LootItemCondition.Builder builder3 = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(paramHolderGetter, new Block[] { Blocks.CREAKING_HEART }).setProperties(builder)), new BlockPos(vec3i)); LootItemCondition.Builder builder4 = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(builder1), new BlockPos(vec3i.multiply(2))); return AllOfCondition.allOf(new LootItemCondition.Builder[] { builder2, builder3, builder4 }); }).toArray(paramInt -> new LootItemCondition.Builder[paramInt]);
/*     */     
/* 610 */     return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(new LootItemCondition.Builder[] {
/* 611 */           (LootItemCondition.Builder)AnyOfCondition.anyOf(arrayOfBuilder)
/*     */         });
/*     */   }
/*     */   
/*     */   private static Advancement.Builder smithingWithStyle(Advancement.Builder paramBuilder) {
/* 616 */     paramBuilder.requirements(AdvancementRequirements.Strategy.AND);
/*     */     
/* 618 */     Set<Item> set = Set.of(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE);
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
/* 629 */     VanillaRecipeProvider.smithingTrims().filter(paramTrimTemplate -> paramSet.contains(paramTrimTemplate.template())).forEach(paramTrimTemplate -> paramBuilder.addCriterion("armor_trimmed_" + String.valueOf(paramTrimTemplate.recipeId().identifier()), RecipeCraftedTrigger.TriggerInstance.craftedItem(paramTrimTemplate.recipeId())));
/*     */ 
/*     */ 
/*     */     
/* 633 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder craftingANewLook(Advancement.Builder paramBuilder) {
/* 637 */     paramBuilder.requirements(AdvancementRequirements.Strategy.OR);
/*     */     
/* 639 */     VanillaRecipeProvider.smithingTrims().map(VanillaRecipeProvider.TrimTemplate::recipeId).forEach(paramResourceKey -> paramBuilder.addCriterion("armor_trimmed_" + String.valueOf(paramResourceKey.identifier()), RecipeCraftedTrigger.TriggerInstance.craftedItem(paramResourceKey)));
/*     */ 
/*     */ 
/*     */     
/* 643 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder respectingTheRemnantsCriterions(HolderGetter<Item> paramHolderGetter, Advancement.Builder paramBuilder) {
/* 647 */     List<Pair> list = List.of(
/* 648 */         Pair.of("desert_pyramid", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY)), 
/* 649 */         Pair.of("desert_well", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY)), 
/* 650 */         Pair.of("ocean_ruin_cold", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY)), 
/* 651 */         Pair.of("ocean_ruin_warm", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY)), 
/* 652 */         Pair.of("trail_ruins_rare", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE)), 
/* 653 */         Pair.of("trail_ruins_common", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON)));
/*     */     
/* 655 */     list.forEach(paramPair -> paramBuilder.addCriterion((String)paramPair.getFirst(), (Criterion)paramPair.getSecond()));
/*     */     
/* 657 */     String str = "has_sherd";
/* 658 */     paramBuilder.addCriterion("has_sherd", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of(paramHolderGetter, ItemTags.DECORATED_POT_SHERDS) }));
/*     */     
/* 660 */     paramBuilder.requirements(new AdvancementRequirements(List.of(list
/* 661 */             .stream().map(Pair::getFirst).toList(), 
/* 662 */             List.of("has_sherd"))));
/*     */ 
/*     */     
/* 665 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   protected static void createAdventuringTime(HolderLookup.Provider paramProvider, Consumer<AdvancementHolder> paramConsumer, AdvancementHolder paramAdvancementHolder, MultiNoiseBiomeSourceParameterList.Preset paramPreset) {
/* 669 */     addBiomes(Advancement.Builder.advancement(), paramProvider, paramPreset.usedBiomes().toList())
/* 670 */       .parent(paramAdvancementHolder)
/* 671 */       .display((ItemLike)Items.DIAMOND_BOOTS, (Component)Component.translatable("advancements.adventure.adventuring_time.title"), (Component)Component.translatable("advancements.adventure.adventuring_time.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 672 */       .rewards(AdvancementRewards.Builder.experience(500))
/* 673 */       .save(paramConsumer, "adventure/adventuring_time");
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addMobsToKill(Advancement.Builder paramBuilder, HolderGetter<EntityType<?>> paramHolderGetter, List<EntityType<?>> paramList) {
/* 677 */     paramList.forEach(paramEntityType -> paramBuilder.addCriterion(BuiltInRegistries.ENTITY_TYPE.getKey(paramEntityType).toString(), KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(paramHolderGetter, paramEntityType))));
/* 678 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   protected static Advancement.Builder addBiomes(Advancement.Builder paramBuilder, HolderLookup.Provider paramProvider, List<ResourceKey<Biome>> paramList) {
/* 682 */     HolderLookup.RegistryLookup registryLookup = paramProvider.lookupOrThrow(Registries.BIOME);
/* 683 */     for (ResourceKey<Biome> resourceKey : paramList) {
/* 684 */       paramBuilder.addCriterion(resourceKey.identifier().toString(), PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome((Holder)registryLookup.getOrThrow(resourceKey))));
/*     */     }
/* 686 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   private static List<EntityType<?>> validateMobsToKill(List<EntityType<?>> paramList, HolderLookup<EntityType<?>> paramHolderLookup) {
/* 690 */     ArrayList<String> arrayList = new ArrayList();
/* 691 */     Set<EntityType<?>> set = Set.copyOf(paramList);
/*     */ 
/*     */     
/* 694 */     Set set1 = (Set)set.stream().map(EntityType::getCategory).collect(Collectors.toSet());
/* 695 */     Sets.SetView setView1 = Sets.symmetricDifference(EXCEPTIONS_BY_EXPECTED_CATEGORIES
/* 696 */         .keySet(), set1);
/*     */ 
/*     */     
/* 699 */     if (!setView1.isEmpty()) {
/* 700 */       arrayList.add("Found EntityType with MobCategory only in either expected exceptions or kill_all_mobs advancement: " + (String)setView1
/* 701 */           .stream().map(Object::toString).sorted().collect(Collectors.joining(", ")));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 706 */     Sets.SetView setView2 = Sets.intersection((Set)EXCEPTIONS_BY_EXPECTED_CATEGORIES
/* 707 */         .values().stream().flatMap(Collection::stream).collect(Collectors.toSet()), set);
/*     */ 
/*     */ 
/*     */     
/* 711 */     if (!setView2.isEmpty()) {
/* 712 */       arrayList.add("Found EntityType in both expected exceptions and kill_all_mobs advancement: " + (String)setView2
/* 713 */           .stream().map(Object::toString).sorted().collect(Collectors.joining(", ")));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 720 */     Objects.requireNonNull(set);
/* 721 */     Map map = (Map)paramHolderLookup.listElements().map(Holder.Reference::value).filter(Predicate.not(set::contains)).collect(Collectors.groupingBy(EntityType::getCategory, Collectors.toSet()));
/*     */     
/* 723 */     EXCEPTIONS_BY_EXPECTED_CATEGORIES.forEach((paramMobCategory, paramSet) -> {
/*     */           Sets.SetView setView = Sets.difference((Set)paramMap.getOrDefault(paramMobCategory, Set.of()), paramSet);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           if (!setView.isEmpty()) {
/*     */             paramList.add(String.format(Locale.ROOT, "Found (new?) EntityType with MobCategory %s which are in neither expected exceptions nor kill_all_mobs advancement: %s", new Object[] { paramMobCategory, setView.stream().map(Object::toString).sorted().collect(Collectors.joining(", ")) }));
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 737 */     if (!arrayList.isEmpty()) {
/* 738 */       Objects.requireNonNull(LOGGER); arrayList.forEach(LOGGER::error);
/* 739 */       throw new IllegalStateException("Found inconsistencies with kill_all_mobs advancement");
/*     */     } 
/*     */     
/* 742 */     return paramList;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\advancements\packs\VanillaAdventureAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */