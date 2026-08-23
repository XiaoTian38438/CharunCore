/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public class EntityTypeTagsProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {
/*    */   public EntityTypeTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 13 */     super(paramPackOutput, Registries.ENTITY_TYPE, paramCompletableFuture, paramEntityType -> paramEntityType.builtInRegistryHolder().key());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider paramProvider) {
/* 18 */     tag(EntityTypeTags.SKELETONS).add((EntityType<?>[])new EntityType[] { EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON, EntityType.SKELETON_HORSE, EntityType.BOGGED, EntityType.PARCHED });
/* 19 */     tag(EntityTypeTags.ZOMBIES).add((EntityType<?>[])new EntityType[] { EntityType.ZOMBIE_HORSE, EntityType.CAMEL_HUSK, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOGLIN, EntityType.DROWNED, EntityType.HUSK, EntityType.ZOMBIE_NAUTILUS });
/* 20 */     tag(EntityTypeTags.RAIDERS).add((EntityType<?>[])new EntityType[] { EntityType.EVOKER, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.WITCH });
/* 21 */     tag(EntityTypeTags.UNDEAD).addTag(EntityTypeTags.SKELETONS).addTag(EntityTypeTags.ZOMBIES).add(EntityType.WITHER).add(EntityType.PHANTOM);
/* 22 */     tag(EntityTypeTags.BURN_IN_DAYLIGHT).add((EntityType<?>[])new EntityType[] { EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON, EntityType.BOGGED }).add((Object[])new EntityType[] { EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER, EntityType.DROWNED, EntityType.ZOMBIE_NAUTILUS }).add(EntityType.PHANTOM);
/* 23 */     tag(EntityTypeTags.BEEHIVE_INHABITORS).add(EntityType.BEE);
/* 24 */     tag(EntityTypeTags.ARROWS).add((EntityType<?>[])new EntityType[] { EntityType.ARROW, EntityType.SPECTRAL_ARROW });
/* 25 */     tag(EntityTypeTags.IMPACT_PROJECTILES).addTag(EntityTypeTags.ARROWS).add(EntityType.FIREWORK_ROCKET).add((Object[])new EntityType[] { EntityType.SNOWBALL, EntityType.FIREBALL, EntityType.SMALL_FIREBALL, EntityType.EGG, EntityType.TRIDENT, EntityType.DRAGON_FIREBALL, EntityType.WITHER_SKULL, EntityType.WIND_CHARGE, EntityType.BREEZE_WIND_CHARGE });
/* 26 */     tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add((EntityType<?>[])new EntityType[] { EntityType.RABBIT, EntityType.ENDERMITE, EntityType.SILVERFISH, EntityType.FOX });
/* 27 */     tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS).add((EntityType<?>[])new EntityType[] { EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SALMON, EntityType.COD, EntityType.SQUID, EntityType.GLOW_SQUID, EntityType.TADPOLE });
/* 28 */     tag(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES).add((EntityType<?>[])new EntityType[] { EntityType.DROWNED, EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN });
/* 29 */     tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add((EntityType<?>[])new EntityType[] { EntityType.STRAY, EntityType.POLAR_BEAR, EntityType.SNOW_GOLEM, EntityType.WITHER });
/* 30 */     tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add((EntityType<?>[])new EntityType[] { EntityType.STRIDER, EntityType.BLAZE, EntityType.MAGMA_CUBE });
/* 31 */     tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER).addTag(EntityTypeTags.UNDEAD).add((Object[])new EntityType[] { EntityType.AXOLOTL, EntityType.FROG, EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.TURTLE, EntityType.GLOW_SQUID, EntityType.COD, EntityType.PUFFERFISH, EntityType.SALMON, EntityType.SQUID, EntityType.TROPICAL_FISH, EntityType.TADPOLE, EntityType.ARMOR_STAND, EntityType.COPPER_GOLEM, EntityType.NAUTILUS });
/* 32 */     tag(EntityTypeTags.FROG_FOOD).add((EntityType<?>[])new EntityType[] { EntityType.SLIME, EntityType.MAGMA_CUBE });
/* 33 */     tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add((EntityType<?>[])new EntityType[] { EntityType.COPPER_GOLEM, EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.SHULKER, EntityType.ALLAY, EntityType.BAT, EntityType.BEE, EntityType.BLAZE, EntityType.CAT, EntityType.CHICKEN, EntityType.GHAST, EntityType.HAPPY_GHAST, EntityType.PHANTOM, EntityType.MAGMA_CUBE, EntityType.OCELOT, EntityType.PARROT, EntityType.WITHER, EntityType.BREEZE });
/* 34 */     tag(EntityTypeTags.DISMOUNTS_UNDERWATER).add((EntityType<?>[])new EntityType[] { EntityType.CAMEL, EntityType.CHICKEN, EntityType.DONKEY, EntityType.HAPPY_GHAST, EntityType.HORSE, EntityType.LLAMA, EntityType.MULE, EntityType.PIG, EntityType.RAVAGER, EntityType.SPIDER, EntityType.STRIDER, EntityType.TRADER_LLAMA, EntityType.ZOMBIE_HORSE });
/* 35 */     tag(EntityTypeTags.NON_CONTROLLING_RIDER).add((EntityType<?>[])new EntityType[] { EntityType.SLIME, EntityType.MAGMA_CUBE });
/* 36 */     tag(EntityTypeTags.ILLAGER).add(EntityType.EVOKER).add(EntityType.ILLUSIONER).add(EntityType.PILLAGER).add(EntityType.VINDICATOR);
/* 37 */     tag(EntityTypeTags.AQUATIC).add(EntityType.TURTLE).add(EntityType.AXOLOTL).add(EntityType.GUARDIAN).add(EntityType.ELDER_GUARDIAN).add(EntityType.COD).add(EntityType.PUFFERFISH).add(EntityType.SALMON).add(EntityType.TROPICAL_FISH).add(EntityType.DOLPHIN).add(EntityType.SQUID).add(EntityType.GLOW_SQUID).add(EntityType.TADPOLE).add(EntityType.NAUTILUS).add(EntityType.ZOMBIE_NAUTILUS);
/* 38 */     tag(EntityTypeTags.ARTHROPOD).add(EntityType.BEE).add(EntityType.ENDERMITE).add(EntityType.SILVERFISH).add(EntityType.SPIDER).add(EntityType.CAVE_SPIDER);
/* 39 */     tag(EntityTypeTags.IGNORES_POISON_AND_REGEN).addTag(EntityTypeTags.UNDEAD);
/* 40 */     tag(EntityTypeTags.INVERTED_HEALING_AND_HARM).addTag(EntityTypeTags.UNDEAD);
/* 41 */     tag(EntityTypeTags.WITHER_FRIENDS).addTag(EntityTypeTags.UNDEAD);
/* 42 */     tag(EntityTypeTags.ILLAGER_FRIENDS).addTag(EntityTypeTags.ILLAGER);
/*    */     
/* 44 */     tag(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH).add(EntityType.TURTLE).add(EntityType.GUARDIAN).add(EntityType.ELDER_GUARDIAN).add(EntityType.COD).add(EntityType.PUFFERFISH).add(EntityType.SALMON).add(EntityType.TROPICAL_FISH).add(EntityType.DOLPHIN).add(EntityType.SQUID).add(EntityType.GLOW_SQUID).add(EntityType.TADPOLE).add(EntityType.NAUTILUS).add(EntityType.ZOMBIE_NAUTILUS);
/* 45 */     tag(EntityTypeTags.SENSITIVE_TO_IMPALING).addTag(EntityTypeTags.AQUATIC);
/* 46 */     tag(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS).addTag(EntityTypeTags.ARTHROPOD);
/* 47 */     tag(EntityTypeTags.SENSITIVE_TO_SMITE).addTag(EntityTypeTags.UNDEAD);
/* 48 */     tag(EntityTypeTags.REDIRECTABLE_PROJECTILE).add((EntityType<?>[])new EntityType[] { EntityType.FIREBALL, EntityType.WIND_CHARGE, EntityType.BREEZE_WIND_CHARGE });
/* 49 */     tag(EntityTypeTags.DEFLECTS_PROJECTILES).add(EntityType.BREEZE);
/* 50 */     tag(EntityTypeTags.CAN_TURN_IN_BOATS).add(EntityType.BREEZE);
/* 51 */     tag(EntityTypeTags.NO_ANGER_FROM_WIND_CHARGE).add((EntityType<?>[])new EntityType[] { EntityType.BREEZE, EntityType.SKELETON, EntityType.BOGGED, EntityType.STRAY, EntityType.ZOMBIE, EntityType.HUSK, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SLIME });
/* 52 */     tag(EntityTypeTags.IMMUNE_TO_INFESTED).add(EntityType.SILVERFISH);
/* 53 */     tag(EntityTypeTags.IMMUNE_TO_OOZING).add(EntityType.SLIME);
/* 54 */     tag(EntityTypeTags.BOAT).add((EntityType<?>[])new EntityType[] { EntityType.OAK_BOAT, EntityType.SPRUCE_BOAT, EntityType.BIRCH_BOAT, EntityType.JUNGLE_BOAT, EntityType.ACACIA_BOAT, EntityType.CHERRY_BOAT, EntityType.DARK_OAK_BOAT, EntityType.PALE_OAK_BOAT, EntityType.MANGROVE_BOAT, EntityType.BAMBOO_RAFT });
/* 55 */     tag(EntityTypeTags.CAN_EQUIP_SADDLE).add((EntityType<?>[])new EntityType[] { EntityType.HORSE, EntityType.SKELETON_HORSE, EntityType.ZOMBIE_HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.PIG, EntityType.STRIDER, EntityType.CAMEL, EntityType.CAMEL_HUSK, EntityType.NAUTILUS, EntityType.ZOMBIE_NAUTILUS });
/* 56 */     tag(EntityTypeTags.CAN_EQUIP_HARNESS).add(EntityType.HAPPY_GHAST);
/* 57 */     tag(EntityTypeTags.CAN_WEAR_HORSE_ARMOR).add(EntityType.HORSE).add(EntityType.ZOMBIE_HORSE);
/* 58 */     tag(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR).add((EntityType<?>[])new EntityType[] { EntityType.NAUTILUS, EntityType.ZOMBIE_NAUTILUS });
/* 59 */     tag(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS).add((EntityType<?>[])new EntityType[] { EntityType.ARMADILLO, EntityType.BEE, EntityType.CAMEL, EntityType.CAT, EntityType.CHICKEN, EntityType.COW, EntityType.DONKEY, EntityType.FOX, EntityType.GOAT, EntityType.HAPPY_GHAST, EntityType.HORSE, EntityType.SKELETON_HORSE, EntityType.LLAMA, EntityType.MULE, EntityType.OCELOT, EntityType.PANDA, EntityType.PARROT, EntityType.PIG, EntityType.POLAR_BEAR, EntityType.RABBIT, EntityType.SHEEP, EntityType.SNIFFER, EntityType.STRIDER, EntityType.VILLAGER, EntityType.WOLF });
/* 60 */     tag(EntityTypeTags.CANNOT_BE_PUSHED_ONTO_BOATS).add(EntityType.PLAYER).add(EntityType.ELDER_GUARDIAN).add(EntityType.COD).add(EntityType.PUFFERFISH).add(EntityType.SALMON).add(EntityType.TROPICAL_FISH).add(EntityType.DOLPHIN).add(EntityType.SQUID).add(EntityType.GLOW_SQUID).add(EntityType.TADPOLE).add(EntityType.CREAKING).add(EntityType.NAUTILUS).add(EntityType.ZOMBIE_NAUTILUS);
/* 61 */     tag(EntityTypeTags.ACCEPTS_IRON_GOLEM_GIFT).add(EntityType.COPPER_GOLEM);
/* 62 */     tag(EntityTypeTags.CANDIDATE_FOR_IRON_GOLEM_GIFT).add(EntityType.VILLAGER).addTag(EntityTypeTags.ACCEPTS_IRON_GOLEM_GIFT);
/* 63 */     tag(EntityTypeTags.NAUTILUS_HOSTILES).add(EntityType.PUFFERFISH);
/* 64 */     tag(EntityTypeTags.CAN_FLOAT_WHILE_RIDDEN).add((EntityType<?>[])new EntityType[] { EntityType.HORSE, EntityType.ZOMBIE_HORSE, EntityType.MULE, EntityType.DONKEY, EntityType.CAMEL, EntityType.CAMEL_HUSK });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\EntityTypeTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */