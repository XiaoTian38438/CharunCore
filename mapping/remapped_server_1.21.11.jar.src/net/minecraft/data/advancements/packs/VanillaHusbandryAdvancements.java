/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.criterion.BeeNestDestroyedTrigger;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.advancements.criterion.BredAnimalsTrigger;
/*     */ import net.minecraft.advancements.criterion.ConsumeItemTrigger;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.EffectsChangedTrigger;
/*     */ import net.minecraft.advancements.criterion.EnchantmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.FilledBucketTrigger;
/*     */ import net.minecraft.advancements.criterion.FishingRodHookedTrigger;
/*     */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.PickedUpItemTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.criterion.StartRidingTrigger;
/*     */ import net.minecraft.advancements.criterion.TameAnimalTrigger;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.EnchantmentsPredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VanillaHusbandryAdvancements
/*     */   implements AdvancementSubProvider
/*     */ {
/*  76 */   public static final List<EntityType<?>> BREEDABLE_ANIMALS = List.of((EntityType<?>[])new EntityType[] { EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.SHEEP, EntityType.COW, EntityType.MOOSHROOM, EntityType.PIG, EntityType.CHICKEN, EntityType.WOLF, EntityType.OCELOT, EntityType.RABBIT, EntityType.LLAMA, EntityType.CAT, EntityType.PANDA, EntityType.FOX, EntityType.BEE, EntityType.HOGLIN, EntityType.STRIDER, EntityType.GOAT, EntityType.AXOLOTL, EntityType.CAMEL, EntityType.ARMADILLO, EntityType.NAUTILUS });
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
/* 103 */   public static final List<EntityType<?>> INDIRECTLY_BREEDABLE_ANIMALS = List.of(EntityType.TURTLE, EntityType.FROG, EntityType.SNIFFER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   private static final Item[] FISH = new Item[] { Items.COD, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.SALMON };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   private static final Item[] FISH_BUCKETS = new Item[] { Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   private static final Item[] EDIBLE_ITEMS = new Item[] { Items.APPLE, Items.MUSHROOM_STEW, Items.BREAD, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.COOKED_COD, Items.COOKED_SALMON, Items.COOKIE, Items.MELON_SLICE, Items.BEEF, Items.COOKED_BEEF, Items.CHICKEN, Items.COOKED_CHICKEN, Items.ROTTEN_FLESH, Items.SPIDER_EYE, Items.CARROT, Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO, Items.GOLDEN_CARROT, Items.PUMPKIN_PIE, Items.RABBIT, Items.COOKED_RABBIT, Items.RABBIT_STEW, Items.MUTTON, Items.COOKED_MUTTON, Items.CHORUS_FRUIT, Items.BEETROOT, Items.BEETROOT_SOUP, Items.DRIED_KELP, Items.SUSPICIOUS_STEW, Items.SWEET_BERRIES, Items.HONEY_BOTTLE, Items.GLOW_BERRIES };
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
/* 166 */   public static final Item[] WAX_SCRAPING_TOOLS = new Item[] { Items.WOODEN_AXE, Items.GOLDEN_AXE, Items.STONE_AXE, Items.COPPER_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE };
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Comparator<Holder.Reference<?>> HOLDER_KEY_COMPARATOR;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 176 */     HOLDER_KEY_COMPARATOR = Comparator.comparing(paramReference -> paramReference.key().identifier());
/*     */   }
/*     */   
/*     */   public void generate(HolderLookup.Provider paramProvider, Consumer<AdvancementHolder> paramConsumer) {
/* 180 */     HolderLookup.RegistryLookup registryLookup1 = paramProvider.lookupOrThrow(Registries.ENTITY_TYPE);
/* 181 */     HolderLookup.RegistryLookup registryLookup2 = paramProvider.lookupOrThrow(Registries.ITEM);
/* 182 */     HolderLookup.RegistryLookup registryLookup3 = paramProvider.lookupOrThrow(Registries.BLOCK);
/* 183 */     HolderLookup.RegistryLookup registryLookup4 = paramProvider.lookupOrThrow(Registries.FROG_VARIANT);
/* 184 */     HolderLookup.RegistryLookup registryLookup5 = paramProvider.lookupOrThrow(Registries.CAT_VARIANT);
/* 185 */     HolderLookup.RegistryLookup registryLookup6 = paramProvider.lookupOrThrow(Registries.WOLF_VARIANT);
/*     */     
/* 187 */     HolderLookup.RegistryLookup registryLookup7 = paramProvider.lookupOrThrow(Registries.ENCHANTMENT);
/*     */ 
/*     */ 
/*     */     
/* 191 */     AdvancementHolder advancementHolder1 = Advancement.Builder.advancement().display((ItemLike)Blocks.HAY_BLOCK, (Component)Component.translatable("advancements.husbandry.root.title"), (Component)Component.translatable("advancements.husbandry.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/husbandry"), AdvancementType.TASK, false, false, false).addCriterion("consumed_item", ConsumeItemTrigger.TriggerInstance.usedItem()).save(paramConsumer, "husbandry/root");
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
/* 204 */     AdvancementHolder advancementHolder2 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.WHEAT, (Component)Component.translatable("advancements.husbandry.plant_seed.title"), (Component)Component.translatable("advancements.husbandry.plant_seed.description"), null, AdvancementType.TASK, true, true, false).requirements(AdvancementRequirements.Strategy.OR).addCriterion("wheat", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.WHEAT)).addCriterion("pumpkin_stem", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.PUMPKIN_STEM)).addCriterion("melon_stem", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.MELON_STEM)).addCriterion("beetroots", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.BEETROOTS)).addCriterion("nether_wart", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.NETHER_WART)).addCriterion("torchflower", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.TORCHFLOWER_CROP)).addCriterion("pitcher_pod", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.PITCHER_CROP)).save(paramConsumer, "husbandry/plant_seed");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     AdvancementHolder advancementHolder3 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.WHEAT, (Component)Component.translatable("advancements.husbandry.breed_an_animal.title"), (Component)Component.translatable("advancements.husbandry.breed_an_animal.description"), null, AdvancementType.TASK, true, true, false).requirements(AdvancementRequirements.Strategy.OR).addCriterion("bred", BredAnimalsTrigger.TriggerInstance.bredAnimals()).save(paramConsumer, "husbandry/breed_an_animal");
/*     */     
/* 213 */     createBreedAllAnimalsAdvancement(advancementHolder3, paramConsumer, (HolderGetter<EntityType<?>>)registryLookup1, BREEDABLE_ANIMALS.stream(), INDIRECTLY_BREEDABLE_ANIMALS.stream());
/*     */     
/* 215 */     addFood(Advancement.Builder.advancement(), (HolderGetter<Item>)registryLookup2)
/* 216 */       .parent(advancementHolder2)
/* 217 */       .display((ItemLike)Items.APPLE, (Component)Component.translatable("advancements.husbandry.balanced_diet.title"), (Component)Component.translatable("advancements.husbandry.balanced_diet.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 218 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 219 */       .save(paramConsumer, "husbandry/balanced_diet");
/*     */     
/* 221 */     Advancement.Builder.advancement()
/* 222 */       .parent(advancementHolder2)
/* 223 */       .display((ItemLike)Items.NETHERITE_HOE, (Component)Component.translatable("advancements.husbandry.netherite_hoe.title"), (Component)Component.translatable("advancements.husbandry.netherite_hoe.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 224 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 225 */       .addCriterion("netherite_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.NETHERITE_HOE
/* 226 */           })).save(paramConsumer, "husbandry/obtain_netherite_hoe");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     AdvancementHolder advancementHolder4 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.LEAD, (Component)Component.translatable("advancements.husbandry.tame_an_animal.title"), (Component)Component.translatable("advancements.husbandry.tame_an_animal.description"), null, AdvancementType.TASK, true, true, false).addCriterion("tamed_animal", TameAnimalTrigger.TriggerInstance.tamedAnimal()).save(paramConsumer, "husbandry/tame_an_animal");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 238 */     AdvancementHolder advancementHolder5 = addFish(Advancement.Builder.advancement(), (HolderGetter<Item>)registryLookup2).parent(advancementHolder1).requirements(AdvancementRequirements.Strategy.OR).display((ItemLike)Items.FISHING_ROD, (Component)Component.translatable("advancements.husbandry.fishy_business.title"), (Component)Component.translatable("advancements.husbandry.fishy_business.description"), null, AdvancementType.TASK, true, true, false).save(paramConsumer, "husbandry/fishy_business");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 244 */     AdvancementHolder advancementHolder6 = addFishBuckets(Advancement.Builder.advancement(), (HolderGetter<Item>)registryLookup2).parent(advancementHolder5).requirements(AdvancementRequirements.Strategy.OR).display((ItemLike)Items.PUFFERFISH_BUCKET, (Component)Component.translatable("advancements.husbandry.tactical_fishing.title"), (Component)Component.translatable("advancements.husbandry.tactical_fishing.description"), null, AdvancementType.TASK, true, true, false).save(paramConsumer, "husbandry/tactical_fishing");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     AdvancementHolder advancementHolder7 = Advancement.Builder.advancement().parent(advancementHolder6).requirements(AdvancementRequirements.Strategy.OR).addCriterion(BuiltInRegistries.ITEM.getKey(Items.AXOLOTL_BUCKET).getPath(), FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.AXOLOTL_BUCKET }))).display((ItemLike)Items.AXOLOTL_BUCKET, (Component)Component.translatable("advancements.husbandry.axolotl_in_a_bucket.title"), (Component)Component.translatable("advancements.husbandry.axolotl_in_a_bucket.description"), null, AdvancementType.TASK, true, true, false).save(paramConsumer, "husbandry/axolotl_in_a_bucket");
/*     */     
/* 253 */     Advancement.Builder.advancement()
/* 254 */       .parent(advancementHolder7)
/* 255 */       .addCriterion("kill_axolotl_target", EffectsChangedTrigger.TriggerInstance.gotEffectsFrom(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.AXOLOTL)))
/* 256 */       .display((ItemLike)Items.TROPICAL_FISH_BUCKET, (Component)Component.translatable("advancements.husbandry.kill_axolotl_target.title"), (Component)Component.translatable("advancements.husbandry.kill_axolotl_target.description"), null, AdvancementType.TASK, true, true, false)
/* 257 */       .save(paramConsumer, "husbandry/kill_axolotl_target");
/*     */     
/* 259 */     addCatVariants(Advancement.Builder.advancement(), (HolderLookup<CatVariant>)registryLookup5)
/* 260 */       .parent(advancementHolder4)
/* 261 */       .display((ItemLike)Items.COD, (Component)Component.translatable("advancements.husbandry.complete_catalogue.title"), (Component)Component.translatable("advancements.husbandry.complete_catalogue.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 262 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 263 */       .save(paramConsumer, "husbandry/complete_catalogue");
/*     */     
/* 265 */     addTamedWolfVariants(Advancement.Builder.advancement(), (HolderLookup<WolfVariant>)registryLookup6)
/* 266 */       .parent(advancementHolder4)
/* 267 */       .display((ItemLike)Items.BONE, (Component)Component.translatable("advancements.husbandry.whole_pack.title"), (Component)Component.translatable("advancements.husbandry.whole_pack.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 268 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 269 */       .save(paramConsumer, "husbandry/whole_pack");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     AdvancementHolder advancementHolder8 = Advancement.Builder.advancement().parent(advancementHolder1).addCriterion("safely_harvest_honey", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, BlockTags.BEEHIVES)).setSmokey(true), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.GLASS_BOTTLE }))).display((ItemLike)Items.HONEY_BOTTLE, (Component)Component.translatable("advancements.husbandry.safely_harvest_honey.title"), (Component)Component.translatable("advancements.husbandry.safely_harvest_honey.description"), null, AdvancementType.TASK, true, true, false).save(paramConsumer, "husbandry/safely_harvest_honey");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     AdvancementHolder advancementHolder9 = Advancement.Builder.advancement().parent(advancementHolder8).display((ItemLike)Items.HONEYCOMB, (Component)Component.translatable("advancements.husbandry.wax_on.title"), (Component)Component.translatable("advancements.husbandry.wax_on.description"), null, AdvancementType.TASK, true, true, false).addCriterion("wax_on", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, ((BiMap)HoneycombItem.WAXABLES.get()).keySet())), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.HONEYCOMB }))).save(paramConsumer, "husbandry/wax_on");
/*     */     
/* 283 */     Advancement.Builder.advancement()
/* 284 */       .parent(advancementHolder9)
/* 285 */       .display((ItemLike)Items.STONE_AXE, (Component)Component.translatable("advancements.husbandry.wax_off.title"), (Component)Component.translatable("advancements.husbandry.wax_off.description"), null, AdvancementType.TASK, true, true, false)
/* 286 */       .addCriterion("wax_off", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, ((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).keySet())), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, (ItemLike[])WAX_SCRAPING_TOOLS)))
/* 287 */       .save(paramConsumer, "husbandry/wax_off");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 293 */     AdvancementHolder advancementHolder10 = Advancement.Builder.advancement().parent(advancementHolder1).addCriterion(BuiltInRegistries.ITEM.getKey(Items.TADPOLE_BUCKET).getPath(), FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.TADPOLE_BUCKET }))).display((ItemLike)Items.TADPOLE_BUCKET, (Component)Component.translatable("advancements.husbandry.tadpole_in_a_bucket.title"), (Component)Component.translatable("advancements.husbandry.tadpole_in_a_bucket.description"), null, AdvancementType.TASK, true, true, false).save(paramConsumer, "husbandry/tadpole_in_a_bucket");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     AdvancementHolder advancementHolder11 = addLeashedFrogVariants((HolderGetter<EntityType<?>>)registryLookup1, (HolderGetter<Item>)registryLookup2, (HolderLookup<FrogVariant>)registryLookup4, Advancement.Builder.advancement()).parent(advancementHolder10).display((ItemLike)Items.LEAD, (Component)Component.translatable("advancements.husbandry.leash_all_frog_variants.title"), (Component)Component.translatable("advancements.husbandry.leash_all_frog_variants.description"), null, AdvancementType.TASK, true, true, false).save(paramConsumer, "husbandry/leash_all_frog_variants");
/*     */     
/* 300 */     Advancement.Builder.advancement()
/* 301 */       .parent(advancementHolder11)
/* 302 */       .display((ItemLike)Items.VERDANT_FROGLIGHT, (Component)Component.translatable("advancements.husbandry.froglights.title"), (Component)Component.translatable("advancements.husbandry.froglights.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 303 */       .addCriterion("froglights", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.OCHRE_FROGLIGHT, (ItemLike)Items.PEARLESCENT_FROGLIGHT, (ItemLike)Items.VERDANT_FROGLIGHT
/* 304 */           })).save(paramConsumer, "husbandry/froglights");
/*     */     
/* 306 */     Advancement.Builder.advancement()
/* 307 */       .parent(advancementHolder1)
/* 308 */       .addCriterion("silk_touch_nest", 
/* 309 */         BeeNestDestroyedTrigger.TriggerInstance.destroyedBeeNest(Blocks.BEE_NEST, 
/*     */           
/* 311 */           ItemPredicate.Builder.item()
/* 312 */           .withComponents(DataComponentMatchers.Builder.components()
/* 313 */             .partial(DataComponentPredicates.ENCHANTMENTS, (DataComponentPredicate)EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate((Holder)registryLookup7.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))).build()), 
/*     */           
/* 315 */           MinMaxBounds.Ints.exactly(3)))
/*     */ 
/*     */       
/* 318 */       .display((ItemLike)Blocks.BEE_NEST, (Component)Component.translatable("advancements.husbandry.silk_touch_nest.title"), (Component)Component.translatable("advancements.husbandry.silk_touch_nest.description"), null, AdvancementType.TASK, true, true, false)
/* 319 */       .save(paramConsumer, "husbandry/silk_touch_nest");
/*     */     
/* 321 */     Advancement.Builder.advancement()
/* 322 */       .parent(advancementHolder1)
/* 323 */       .display((ItemLike)Items.OAK_BOAT, (Component)Component.translatable("advancements.husbandry.ride_a_boat_with_a_goat.title"), (Component)Component.translatable("advancements.husbandry.ride_a_boat_with_a_goat.description"), null, AdvancementType.TASK, true, true, false)
/* 324 */       .addCriterion("ride_a_boat_with_a_goat", 
/* 325 */         StartRidingTrigger.TriggerInstance.playerStartsRiding(
/* 326 */           EntityPredicate.Builder.entity().vehicle(
/* 327 */             EntityPredicate.Builder.entity()
/* 328 */             .of((HolderGetter)registryLookup1, EntityTypeTags.BOAT)
/* 329 */             .passenger(
/* 330 */               EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.GOAT)))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 335 */       .save(paramConsumer, "husbandry/ride_a_boat_with_a_goat");
/*     */     
/* 337 */     Advancement.Builder.advancement()
/* 338 */       .parent(advancementHolder1)
/* 339 */       .display((ItemLike)Items.GLOW_INK_SAC, (Component)Component.translatable("advancements.husbandry.make_a_sign_glow.title"), (Component)Component.translatable("advancements.husbandry.make_a_sign_glow.description"), null, AdvancementType.TASK, true, true, false)
/* 340 */       .addCriterion("make_a_sign_glow", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, BlockTags.ALL_SIGNS)), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.GLOW_INK_SAC
/* 341 */             }))).save(paramConsumer, "husbandry/make_a_sign_glow");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 347 */     AdvancementHolder advancementHolder12 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.COOKIE, (Component)Component.translatable("advancements.husbandry.allay_deliver_item_to_player.title"), (Component)Component.translatable("advancements.husbandry.allay_deliver_item_to_player.description"), null, AdvancementType.TASK, true, true, true).addCriterion("allay_deliver_item_to_player", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByPlayer(Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.ALLAY))))).save(paramConsumer, "husbandry/allay_deliver_item_to_player");
/*     */     
/* 349 */     Advancement.Builder.advancement()
/* 350 */       .parent(advancementHolder12)
/* 351 */       .display((ItemLike)Items.NOTE_BLOCK, (Component)Component.translatable("advancements.husbandry.allay_deliver_cake_to_note_block.title"), (Component)Component.translatable("advancements.husbandry.allay_deliver_cake_to_note_block.description"), null, AdvancementType.TASK, true, true, true)
/* 352 */       .addCriterion("allay_deliver_cake_to_note_block", ItemUsedOnLocationTrigger.TriggerInstance.allayDropItemOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.NOTE_BLOCK })), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.CAKE
/* 353 */             }))).save(paramConsumer, "husbandry/allay_deliver_cake_to_note_block");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     AdvancementHolder advancementHolder13 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.SNIFFER_EGG, (Component)Component.translatable("advancements.husbandry.obtain_sniffer_egg.title"), (Component)Component.translatable("advancements.husbandry.obtain_sniffer_egg.description"), null, AdvancementType.TASK, true, true, true).addCriterion("obtain_sniffer_egg", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.SNIFFER_EGG })).save(paramConsumer, "husbandry/obtain_sniffer_egg");
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
/* 370 */     AdvancementHolder advancementHolder14 = Advancement.Builder.advancement().parent(advancementHolder13).display((ItemLike)Items.TORCHFLOWER_SEEDS, (Component)Component.translatable("advancements.husbandry.feed_snifflet.title"), (Component)Component.translatable("advancements.husbandry.feed_snifflet.description"), null, AdvancementType.TASK, true, true, true).addCriterion("feed_snifflet", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.SNIFFER_FOOD), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.SNIFFER).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(true))))))).save(paramConsumer, "husbandry/feed_snifflet");
/*     */     
/* 372 */     Advancement.Builder.advancement()
/* 373 */       .parent(advancementHolder14)
/* 374 */       .display((ItemLike)Items.PITCHER_POD, (Component)Component.translatable("advancements.husbandry.plant_any_sniffer_seed.title"), (Component)Component.translatable("advancements.husbandry.plant_any_sniffer_seed.description"), null, AdvancementType.TASK, true, true, true)
/* 375 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 376 */       .addCriterion("torchflower", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.TORCHFLOWER_CROP))
/* 377 */       .addCriterion("pitcher_pod", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.PITCHER_CROP))
/* 378 */       .save(paramConsumer, "husbandry/plant_any_sniffer_seed");
/*     */     
/* 380 */     Advancement.Builder.advancement()
/* 381 */       .parent(advancementHolder4)
/* 382 */       .display((ItemLike)Items.SHEARS, (Component)Component.translatable("advancements.husbandry.remove_wolf_armor.title"), (Component)Component.translatable("advancements.husbandry.remove_wolf_armor.description"), null, AdvancementType.TASK, true, true, false)
/* 383 */       .addCriterion("remove_wolf_armor", PlayerInteractTrigger.TriggerInstance.equipmentSheared(
/* 384 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.WOLF_ARMOR
/* 385 */             }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.WOLF)))))
/* 386 */       .save(paramConsumer, "husbandry/remove_wolf_armor");
/*     */     
/* 388 */     Advancement.Builder.advancement()
/* 389 */       .parent(advancementHolder4)
/* 390 */       .display((ItemLike)Items.WOLF_ARMOR, (Component)Component.translatable("advancements.husbandry.repair_wolf_armor.title"), (Component)Component.translatable("advancements.husbandry.repair_wolf_armor.description"), null, AdvancementType.TASK, true, true, false)
/* 391 */       .addCriterion("repair_wolf_armor", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
/* 392 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.ARMADILLO_SCUTE
/* 393 */             }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.WOLF).equipment(
/* 394 */                 EntityEquipmentPredicate.Builder.equipment().body(
/* 395 */                   ItemPredicate.Builder.item()
/* 396 */                   .of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.WOLF_ARMOR
/* 397 */                     }).withComponents(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.DAMAGE, Integer.valueOf(0))).build())))))))
/*     */ 
/*     */ 
/*     */       
/* 401 */       .save(paramConsumer, "husbandry/repair_wolf_armor");
/*     */     
/* 403 */     Advancement.Builder.advancement()
/* 404 */       .parent(advancementHolder1)
/* 405 */       .display((ItemLike)Items.DRIED_GHAST, (Component)Component.translatable("advancements.husbandry.place_dried_ghast_in_water.title"), (Component)Component.translatable("advancements.husbandry.place_dried_ghast_in_water.description"), null, AdvancementType.TASK, true, true, false)
/* 406 */       .addCriterion("place_dried_ghast_in_water", ItemUsedOnLocationTrigger.TriggerInstance.placedBlockWithProperties(Blocks.DRIED_GHAST, (Property)BlockStateProperties.WATERLOGGED, true))
/* 407 */       .save(paramConsumer, "husbandry/place_dried_ghast_in_water");
/*     */   }
/*     */   
/*     */   public static AdvancementHolder createBreedAllAnimalsAdvancement(AdvancementHolder paramAdvancementHolder, Consumer<AdvancementHolder> paramConsumer, HolderGetter<EntityType<?>> paramHolderGetter, Stream<EntityType<?>> paramStream1, Stream<EntityType<?>> paramStream2) {
/* 411 */     return addBreedable(Advancement.Builder.advancement(), paramStream1, paramHolderGetter, paramStream2)
/* 412 */       .parent(paramAdvancementHolder)
/* 413 */       .display((ItemLike)Items.GOLDEN_CARROT, (Component)Component.translatable("advancements.husbandry.breed_all_animals.title"), (Component)Component.translatable("advancements.husbandry.breed_all_animals.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 414 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 415 */       .save(paramConsumer, "husbandry/bred_all_animals");
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addLeashedFrogVariants(HolderGetter<EntityType<?>> paramHolderGetter, HolderGetter<Item> paramHolderGetter1, HolderLookup<FrogVariant> paramHolderLookup, Advancement.Builder paramBuilder) {
/* 419 */     sortedVariants(paramHolderLookup).forEach(paramReference -> paramBuilder.addCriterion(paramReference.key().identifier().toString(), PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(paramHolderGetter1, new ItemLike[] { (ItemLike)Items.LEAD }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(paramHolderGetter2, EntityType.FROG).components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.FROG_VARIANT, paramReference)).build()))))));
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
/* 432 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   private static <T> Stream<Holder.Reference<T>> sortedVariants(HolderLookup<T> paramHolderLookup) {
/* 436 */     return (Stream)paramHolderLookup.listElements().sorted(HOLDER_KEY_COMPARATOR);
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addFood(Advancement.Builder paramBuilder, HolderGetter<Item> paramHolderGetter) {
/* 440 */     for (Item item : EDIBLE_ITEMS) {
/* 441 */       paramBuilder.addCriterion(BuiltInRegistries.ITEM.getKey(item).getPath(), ConsumeItemTrigger.TriggerInstance.usedItem(paramHolderGetter, (ItemLike)item));
/*     */     }
/* 443 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addBreedable(Advancement.Builder paramBuilder, Stream<EntityType<?>> paramStream1, HolderGetter<EntityType<?>> paramHolderGetter, Stream<EntityType<?>> paramStream2) {
/* 447 */     paramStream1.forEach(paramEntityType -> paramBuilder.addCriterion(EntityType.getKey(paramEntityType).toString(), BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(paramHolderGetter, paramEntityType))));
/*     */ 
/*     */     
/* 450 */     paramStream2.forEach(paramEntityType -> paramBuilder.addCriterion(EntityType.getKey(paramEntityType).toString(), BredAnimalsTrigger.TriggerInstance.bredAnimals(Optional.of(EntityPredicate.Builder.entity().of(paramHolderGetter, paramEntityType).build()), Optional.of(EntityPredicate.Builder.entity().of(paramHolderGetter, paramEntityType).build()), Optional.empty())));
/*     */ 
/*     */     
/* 453 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addFishBuckets(Advancement.Builder paramBuilder, HolderGetter<Item> paramHolderGetter) {
/* 457 */     for (Item item : FISH_BUCKETS) {
/* 458 */       paramBuilder.addCriterion(BuiltInRegistries.ITEM.getKey(item).getPath(), FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of(paramHolderGetter, new ItemLike[] { (ItemLike)item })));
/*     */     } 
/* 460 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addFish(Advancement.Builder paramBuilder, HolderGetter<Item> paramHolderGetter) {
/* 464 */     for (Item item : FISH) {
/* 465 */       paramBuilder.addCriterion(BuiltInRegistries.ITEM.getKey(item).getPath(), FishingRodHookedTrigger.TriggerInstance.fishedItem(Optional.empty(), Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(paramHolderGetter, new ItemLike[] { (ItemLike)item }).build())));
/*     */     } 
/* 467 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addCatVariants(Advancement.Builder paramBuilder, HolderLookup<CatVariant> paramHolderLookup) {
/* 471 */     sortedVariants(paramHolderLookup).forEach(paramReference -> paramBuilder.addCriterion(paramReference.key().identifier().toString(), TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.CAT_VARIANT, paramReference)).build()))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 480 */     return paramBuilder;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addTamedWolfVariants(Advancement.Builder paramBuilder, HolderLookup<WolfVariant> paramHolderLookup) {
/* 484 */     sortedVariants(paramHolderLookup).forEach(paramReference -> paramBuilder.addCriterion(paramReference.key().identifier().toString(), TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.WOLF_VARIANT, paramReference)).build()))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 493 */     return paramBuilder;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\advancements\packs\VanillaHusbandryAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */