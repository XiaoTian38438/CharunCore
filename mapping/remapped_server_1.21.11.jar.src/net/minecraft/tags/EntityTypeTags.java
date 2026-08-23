/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public interface EntityTypeTags {
/*  8 */   public static final TagKey<EntityType<?>> SKELETONS = create("skeletons");
/*  9 */   public static final TagKey<EntityType<?>> ZOMBIES = create("zombies");
/* 10 */   public static final TagKey<EntityType<?>> RAIDERS = create("raiders");
/* 11 */   public static final TagKey<EntityType<?>> UNDEAD = create("undead");
/* 12 */   public static final TagKey<EntityType<?>> BURN_IN_DAYLIGHT = create("burn_in_daylight");
/* 13 */   public static final TagKey<EntityType<?>> BEEHIVE_INHABITORS = create("beehive_inhabitors");
/* 14 */   public static final TagKey<EntityType<?>> ARROWS = create("arrows");
/* 15 */   public static final TagKey<EntityType<?>> IMPACT_PROJECTILES = create("impact_projectiles");
/* 16 */   public static final TagKey<EntityType<?>> POWDER_SNOW_WALKABLE_MOBS = create("powder_snow_walkable_mobs");
/* 17 */   public static final TagKey<EntityType<?>> AXOLOTL_ALWAYS_HOSTILES = create("axolotl_always_hostiles");
/* 18 */   public static final TagKey<EntityType<?>> AXOLOTL_HUNT_TARGETS = create("axolotl_hunt_targets");
/* 19 */   public static final TagKey<EntityType<?>> FREEZE_IMMUNE_ENTITY_TYPES = create("freeze_immune_entity_types");
/* 20 */   public static final TagKey<EntityType<?>> FREEZE_HURTS_EXTRA_TYPES = create("freeze_hurts_extra_types");
/* 21 */   public static final TagKey<EntityType<?>> CAN_BREATHE_UNDER_WATER = create("can_breathe_under_water");
/* 22 */   public static final TagKey<EntityType<?>> FROG_FOOD = create("frog_food");
/* 23 */   public static final TagKey<EntityType<?>> FALL_DAMAGE_IMMUNE = create("fall_damage_immune");
/* 24 */   public static final TagKey<EntityType<?>> DISMOUNTS_UNDERWATER = create("dismounts_underwater");
/* 25 */   public static final TagKey<EntityType<?>> NON_CONTROLLING_RIDER = create("non_controlling_rider");
/* 26 */   public static final TagKey<EntityType<?>> DEFLECTS_PROJECTILES = create("deflects_projectiles");
/* 27 */   public static final TagKey<EntityType<?>> CAN_TURN_IN_BOATS = create("can_turn_in_boats");
/* 28 */   public static final TagKey<EntityType<?>> ILLAGER = create("illager");
/* 29 */   public static final TagKey<EntityType<?>> AQUATIC = create("aquatic");
/* 30 */   public static final TagKey<EntityType<?>> ARTHROPOD = create("arthropod");
/* 31 */   public static final TagKey<EntityType<?>> IGNORES_POISON_AND_REGEN = create("ignores_poison_and_regen");
/* 32 */   public static final TagKey<EntityType<?>> INVERTED_HEALING_AND_HARM = create("inverted_healing_and_harm");
/* 33 */   public static final TagKey<EntityType<?>> WITHER_FRIENDS = create("wither_friends");
/* 34 */   public static final TagKey<EntityType<?>> ILLAGER_FRIENDS = create("illager_friends");
/* 35 */   public static final TagKey<EntityType<?>> NOT_SCARY_FOR_PUFFERFISH = create("not_scary_for_pufferfish");
/* 36 */   public static final TagKey<EntityType<?>> SENSITIVE_TO_IMPALING = create("sensitive_to_impaling");
/* 37 */   public static final TagKey<EntityType<?>> SENSITIVE_TO_BANE_OF_ARTHROPODS = create("sensitive_to_bane_of_arthropods");
/* 38 */   public static final TagKey<EntityType<?>> SENSITIVE_TO_SMITE = create("sensitive_to_smite");
/* 39 */   public static final TagKey<EntityType<?>> NO_ANGER_FROM_WIND_CHARGE = create("no_anger_from_wind_charge");
/* 40 */   public static final TagKey<EntityType<?>> IMMUNE_TO_OOZING = create("immune_to_oozing");
/* 41 */   public static final TagKey<EntityType<?>> IMMUNE_TO_INFESTED = create("immune_to_infested");
/* 42 */   public static final TagKey<EntityType<?>> REDIRECTABLE_PROJECTILE = create("redirectable_projectile");
/* 43 */   public static final TagKey<EntityType<?>> BOAT = create("boat");
/* 44 */   public static final TagKey<EntityType<?>> CAN_EQUIP_SADDLE = create("can_equip_saddle");
/* 45 */   public static final TagKey<EntityType<?>> CAN_EQUIP_HARNESS = create("can_equip_harness");
/* 46 */   public static final TagKey<EntityType<?>> CAN_WEAR_HORSE_ARMOR = create("can_wear_horse_armor");
/* 47 */   public static final TagKey<EntityType<?>> CAN_WEAR_NAUTILUS_ARMOR = create("can_wear_nautilus_armor");
/* 48 */   public static final TagKey<EntityType<?>> FOLLOWABLE_FRIENDLY_MOBS = create("followable_friendly_mobs");
/* 49 */   public static final TagKey<EntityType<?>> CANNOT_BE_PUSHED_ONTO_BOATS = create("cannot_be_pushed_onto_boats");
/* 50 */   public static final TagKey<EntityType<?>> ACCEPTS_IRON_GOLEM_GIFT = create("accepts_iron_golem_gift");
/* 51 */   public static final TagKey<EntityType<?>> CANDIDATE_FOR_IRON_GOLEM_GIFT = create("candidate_for_iron_golem_gift");
/* 52 */   public static final TagKey<EntityType<?>> NAUTILUS_HOSTILES = create("nautilus_hostiles");
/* 53 */   public static final TagKey<EntityType<?>> CAN_FLOAT_WHILE_RIDDEN = create("can_float_while_ridden");
/*    */   
/*    */   private static TagKey<EntityType<?>> create(String paramString) {
/* 56 */     return TagKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\EntityTypeTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */