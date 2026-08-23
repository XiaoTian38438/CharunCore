/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.advancements.criterion.AnyBlockInteractionTrigger;
/*     */ import net.minecraft.advancements.criterion.BeeNestDestroyedTrigger;
/*     */ import net.minecraft.advancements.criterion.BredAnimalsTrigger;
/*     */ import net.minecraft.advancements.criterion.BrewedPotionTrigger;
/*     */ import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
/*     */ import net.minecraft.advancements.criterion.ChanneledLightningTrigger;
/*     */ import net.minecraft.advancements.criterion.ConstructBeaconTrigger;
/*     */ import net.minecraft.advancements.criterion.ConsumeItemTrigger;
/*     */ import net.minecraft.advancements.criterion.CuredZombieVillagerTrigger;
/*     */ import net.minecraft.advancements.criterion.DefaultBlockInteractionTrigger;
/*     */ import net.minecraft.advancements.criterion.DistanceTrigger;
/*     */ import net.minecraft.advancements.criterion.EffectsChangedTrigger;
/*     */ import net.minecraft.advancements.criterion.EnchantedItemTrigger;
/*     */ import net.minecraft.advancements.criterion.EnterBlockTrigger;
/*     */ import net.minecraft.advancements.criterion.EntityHurtPlayerTrigger;
/*     */ import net.minecraft.advancements.criterion.FallAfterExplosionTrigger;
/*     */ import net.minecraft.advancements.criterion.FilledBucketTrigger;
/*     */ import net.minecraft.advancements.criterion.FishingRodHookedTrigger;
/*     */ import net.minecraft.advancements.criterion.ImpossibleTrigger;
/*     */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemDurabilityTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
/*     */ import net.minecraft.advancements.criterion.KilledByArrowTrigger;
/*     */ import net.minecraft.advancements.criterion.KilledTrigger;
/*     */ import net.minecraft.advancements.criterion.LevitationTrigger;
/*     */ import net.minecraft.advancements.criterion.LightningStrikeTrigger;
/*     */ import net.minecraft.advancements.criterion.LootTableTrigger;
/*     */ import net.minecraft.advancements.criterion.PickedUpItemTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerHurtEntityTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerTrigger;
/*     */ import net.minecraft.advancements.criterion.RecipeCraftedTrigger;
/*     */ import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
/*     */ import net.minecraft.advancements.criterion.ShotCrossbowTrigger;
/*     */ import net.minecraft.advancements.criterion.SlideDownBlockTrigger;
/*     */ import net.minecraft.advancements.criterion.SpearMobsTrigger;
/*     */ import net.minecraft.advancements.criterion.StartRidingTrigger;
/*     */ import net.minecraft.advancements.criterion.SummonedEntityTrigger;
/*     */ import net.minecraft.advancements.criterion.TameAnimalTrigger;
/*     */ import net.minecraft.advancements.criterion.TargetBlockTrigger;
/*     */ import net.minecraft.advancements.criterion.TradeTrigger;
/*     */ import net.minecraft.advancements.criterion.UsedEnderEyeTrigger;
/*     */ import net.minecraft.advancements.criterion.UsedTotemTrigger;
/*     */ import net.minecraft.advancements.criterion.UsingItemTrigger;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CriteriaTriggers
/*     */ {
/*  56 */   public static final Codec<CriterionTrigger<?>> CODEC = BuiltInRegistries.TRIGGER_TYPES.byNameCodec();
/*     */   
/*  58 */   public static final ImpossibleTrigger IMPOSSIBLE = register("impossible", new ImpossibleTrigger());
/*  59 */   public static final KilledTrigger PLAYER_KILLED_ENTITY = register("player_killed_entity", new KilledTrigger());
/*  60 */   public static final KilledTrigger ENTITY_KILLED_PLAYER = register("entity_killed_player", new KilledTrigger());
/*  61 */   public static final EnterBlockTrigger ENTER_BLOCK = register("enter_block", new EnterBlockTrigger());
/*  62 */   public static final InventoryChangeTrigger INVENTORY_CHANGED = register("inventory_changed", new InventoryChangeTrigger());
/*  63 */   public static final RecipeUnlockedTrigger RECIPE_UNLOCKED = register("recipe_unlocked", new RecipeUnlockedTrigger());
/*  64 */   public static final PlayerHurtEntityTrigger PLAYER_HURT_ENTITY = register("player_hurt_entity", new PlayerHurtEntityTrigger());
/*  65 */   public static final EntityHurtPlayerTrigger ENTITY_HURT_PLAYER = register("entity_hurt_player", new EntityHurtPlayerTrigger());
/*  66 */   public static final EnchantedItemTrigger ENCHANTED_ITEM = register("enchanted_item", new EnchantedItemTrigger());
/*  67 */   public static final FilledBucketTrigger FILLED_BUCKET = register("filled_bucket", new FilledBucketTrigger());
/*  68 */   public static final BrewedPotionTrigger BREWED_POTION = register("brewed_potion", new BrewedPotionTrigger());
/*  69 */   public static final ConstructBeaconTrigger CONSTRUCT_BEACON = register("construct_beacon", new ConstructBeaconTrigger());
/*  70 */   public static final UsedEnderEyeTrigger USED_ENDER_EYE = register("used_ender_eye", new UsedEnderEyeTrigger());
/*  71 */   public static final SummonedEntityTrigger SUMMONED_ENTITY = register("summoned_entity", new SummonedEntityTrigger());
/*  72 */   public static final BredAnimalsTrigger BRED_ANIMALS = register("bred_animals", new BredAnimalsTrigger());
/*  73 */   public static final PlayerTrigger LOCATION = register("location", new PlayerTrigger());
/*  74 */   public static final PlayerTrigger SLEPT_IN_BED = register("slept_in_bed", new PlayerTrigger());
/*  75 */   public static final CuredZombieVillagerTrigger CURED_ZOMBIE_VILLAGER = register("cured_zombie_villager", new CuredZombieVillagerTrigger());
/*  76 */   public static final TradeTrigger TRADE = register("villager_trade", new TradeTrigger());
/*  77 */   public static final ItemDurabilityTrigger ITEM_DURABILITY_CHANGED = register("item_durability_changed", new ItemDurabilityTrigger());
/*  78 */   public static final LevitationTrigger LEVITATION = register("levitation", new LevitationTrigger());
/*  79 */   public static final ChangeDimensionTrigger CHANGED_DIMENSION = register("changed_dimension", new ChangeDimensionTrigger());
/*  80 */   public static final PlayerTrigger TICK = register("tick", new PlayerTrigger());
/*  81 */   public static final TameAnimalTrigger TAME_ANIMAL = register("tame_animal", new TameAnimalTrigger());
/*  82 */   public static final ItemUsedOnLocationTrigger PLACED_BLOCK = register("placed_block", new ItemUsedOnLocationTrigger());
/*  83 */   public static final ConsumeItemTrigger CONSUME_ITEM = register("consume_item", new ConsumeItemTrigger());
/*  84 */   public static final EffectsChangedTrigger EFFECTS_CHANGED = register("effects_changed", new EffectsChangedTrigger());
/*  85 */   public static final UsedTotemTrigger USED_TOTEM = register("used_totem", new UsedTotemTrigger());
/*  86 */   public static final DistanceTrigger NETHER_TRAVEL = register("nether_travel", new DistanceTrigger());
/*  87 */   public static final FishingRodHookedTrigger FISHING_ROD_HOOKED = register("fishing_rod_hooked", new FishingRodHookedTrigger());
/*  88 */   public static final ChanneledLightningTrigger CHANNELED_LIGHTNING = register("channeled_lightning", new ChanneledLightningTrigger());
/*  89 */   public static final ShotCrossbowTrigger SHOT_CROSSBOW = register("shot_crossbow", new ShotCrossbowTrigger());
/*  90 */   public static final SpearMobsTrigger SPEAR_MOBS_TRIGGER = register("spear_mobs", new SpearMobsTrigger());
/*  91 */   public static final KilledByArrowTrigger KILLED_BY_ARROW = register("killed_by_arrow", new KilledByArrowTrigger());
/*  92 */   public static final PlayerTrigger RAID_WIN = register("hero_of_the_village", new PlayerTrigger());
/*  93 */   public static final PlayerTrigger RAID_OMEN = register("voluntary_exile", new PlayerTrigger());
/*  94 */   public static final SlideDownBlockTrigger HONEY_BLOCK_SLIDE = register("slide_down_block", new SlideDownBlockTrigger());
/*  95 */   public static final BeeNestDestroyedTrigger BEE_NEST_DESTROYED = register("bee_nest_destroyed", new BeeNestDestroyedTrigger());
/*  96 */   public static final TargetBlockTrigger TARGET_BLOCK_HIT = register("target_hit", new TargetBlockTrigger());
/*  97 */   public static final ItemUsedOnLocationTrigger ITEM_USED_ON_BLOCK = register("item_used_on_block", new ItemUsedOnLocationTrigger());
/*  98 */   public static final DefaultBlockInteractionTrigger DEFAULT_BLOCK_USE = register("default_block_use", new DefaultBlockInteractionTrigger());
/*  99 */   public static final AnyBlockInteractionTrigger ANY_BLOCK_USE = register("any_block_use", new AnyBlockInteractionTrigger());
/* 100 */   public static final LootTableTrigger GENERATE_LOOT = register("player_generates_container_loot", new LootTableTrigger());
/* 101 */   public static final PickedUpItemTrigger THROWN_ITEM_PICKED_UP_BY_ENTITY = register("thrown_item_picked_up_by_entity", new PickedUpItemTrigger());
/* 102 */   public static final PickedUpItemTrigger THROWN_ITEM_PICKED_UP_BY_PLAYER = register("thrown_item_picked_up_by_player", new PickedUpItemTrigger());
/* 103 */   public static final PlayerInteractTrigger PLAYER_INTERACTED_WITH_ENTITY = register("player_interacted_with_entity", new PlayerInteractTrigger());
/* 104 */   public static final PlayerInteractTrigger PLAYER_SHEARED_EQUIPMENT = register("player_sheared_equipment", new PlayerInteractTrigger());
/* 105 */   public static final StartRidingTrigger START_RIDING_TRIGGER = register("started_riding", new StartRidingTrigger());
/* 106 */   public static final LightningStrikeTrigger LIGHTNING_STRIKE = register("lightning_strike", new LightningStrikeTrigger());
/* 107 */   public static final UsingItemTrigger USING_ITEM = register("using_item", new UsingItemTrigger());
/* 108 */   public static final DistanceTrigger FALL_FROM_HEIGHT = register("fall_from_height", new DistanceTrigger());
/* 109 */   public static final DistanceTrigger RIDE_ENTITY_IN_LAVA_TRIGGER = register("ride_entity_in_lava", new DistanceTrigger());
/* 110 */   public static final KilledTrigger KILL_MOB_NEAR_SCULK_CATALYST = register("kill_mob_near_sculk_catalyst", new KilledTrigger());
/* 111 */   public static final ItemUsedOnLocationTrigger ALLAY_DROP_ITEM_ON_BLOCK = register("allay_drop_item_on_block", new ItemUsedOnLocationTrigger());
/* 112 */   public static final PlayerTrigger AVOID_VIBRATION = register("avoid_vibration", new PlayerTrigger());
/* 113 */   public static final RecipeCraftedTrigger RECIPE_CRAFTED = register("recipe_crafted", new RecipeCraftedTrigger());
/* 114 */   public static final RecipeCraftedTrigger CRAFTER_RECIPE_CRAFTED = register("crafter_recipe_crafted", new RecipeCraftedTrigger());
/* 115 */   public static final FallAfterExplosionTrigger FALL_AFTER_EXPLOSION = register("fall_after_explosion", new FallAfterExplosionTrigger());
/*     */   
/*     */   private static <T extends CriterionTrigger<?>> T register(String paramString, T paramT) {
/* 118 */     return (T)Registry.register(BuiltInRegistries.TRIGGER_TYPES, paramString, paramT);
/*     */   }
/*     */   
/*     */   public static CriterionTrigger<?> bootstrap(Registry<CriterionTrigger<?>> paramRegistry) {
/* 122 */     return (CriterionTrigger<?>)IMPOSSIBLE;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\CriteriaTriggers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */