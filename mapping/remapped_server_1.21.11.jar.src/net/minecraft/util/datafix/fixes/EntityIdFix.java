/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public class EntityIdFix extends DataFix {
/*     */   public EntityIdFix(Schema paramSchema, boolean paramBoolean) {
/*  15 */     super(paramSchema, paramBoolean);
/*     */   } private static final Map<String, String> ID_MAP;
/*     */   static {
/*  18 */     ID_MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*     */           paramHashMap.put("AreaEffectCloud", "minecraft:area_effect_cloud");
/*     */           paramHashMap.put("ArmorStand", "minecraft:armor_stand");
/*     */           paramHashMap.put("Arrow", "minecraft:arrow");
/*     */           paramHashMap.put("Bat", "minecraft:bat");
/*     */           paramHashMap.put("Blaze", "minecraft:blaze");
/*     */           paramHashMap.put("Boat", "minecraft:boat");
/*     */           paramHashMap.put("CaveSpider", "minecraft:cave_spider");
/*     */           paramHashMap.put("Chicken", "minecraft:chicken");
/*     */           paramHashMap.put("Cow", "minecraft:cow");
/*     */           paramHashMap.put("Creeper", "minecraft:creeper");
/*     */           paramHashMap.put("Donkey", "minecraft:donkey");
/*     */           paramHashMap.put("DragonFireball", "minecraft:dragon_fireball");
/*     */           paramHashMap.put("ElderGuardian", "minecraft:elder_guardian");
/*     */           paramHashMap.put("EnderCrystal", "minecraft:ender_crystal");
/*     */           paramHashMap.put("EnderDragon", "minecraft:ender_dragon");
/*     */           paramHashMap.put("Enderman", "minecraft:enderman");
/*     */           paramHashMap.put("Endermite", "minecraft:endermite");
/*     */           paramHashMap.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
/*     */           paramHashMap.put("FallingSand", "minecraft:falling_block");
/*     */           paramHashMap.put("Fireball", "minecraft:fireball");
/*     */           paramHashMap.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
/*     */           paramHashMap.put("Ghast", "minecraft:ghast");
/*     */           paramHashMap.put("Giant", "minecraft:giant");
/*     */           paramHashMap.put("Guardian", "minecraft:guardian");
/*     */           paramHashMap.put("Horse", "minecraft:horse");
/*     */           paramHashMap.put("Husk", "minecraft:husk");
/*     */           paramHashMap.put("Item", "minecraft:item");
/*     */           paramHashMap.put("ItemFrame", "minecraft:item_frame");
/*     */           paramHashMap.put("LavaSlime", "minecraft:magma_cube");
/*     */           paramHashMap.put("LeashKnot", "minecraft:leash_knot");
/*     */           paramHashMap.put("MinecartChest", "minecraft:chest_minecart");
/*     */           paramHashMap.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
/*     */           paramHashMap.put("MinecartFurnace", "minecraft:furnace_minecart");
/*     */           paramHashMap.put("MinecartHopper", "minecraft:hopper_minecart");
/*     */           paramHashMap.put("MinecartRideable", "minecraft:minecart");
/*     */           paramHashMap.put("MinecartSpawner", "minecraft:spawner_minecart");
/*     */           paramHashMap.put("MinecartTNT", "minecraft:tnt_minecart");
/*     */           paramHashMap.put("Mule", "minecraft:mule");
/*     */           paramHashMap.put("MushroomCow", "minecraft:mooshroom");
/*     */           paramHashMap.put("Ozelot", "minecraft:ocelot");
/*     */           paramHashMap.put("Painting", "minecraft:painting");
/*     */           paramHashMap.put("Pig", "minecraft:pig");
/*     */           paramHashMap.put("PigZombie", "minecraft:zombie_pigman");
/*     */           paramHashMap.put("PolarBear", "minecraft:polar_bear");
/*     */           paramHashMap.put("PrimedTnt", "minecraft:tnt");
/*     */           paramHashMap.put("Rabbit", "minecraft:rabbit");
/*     */           paramHashMap.put("Sheep", "minecraft:sheep");
/*     */           paramHashMap.put("Shulker", "minecraft:shulker");
/*     */           paramHashMap.put("ShulkerBullet", "minecraft:shulker_bullet");
/*     */           paramHashMap.put("Silverfish", "minecraft:silverfish");
/*     */           paramHashMap.put("Skeleton", "minecraft:skeleton");
/*     */           paramHashMap.put("SkeletonHorse", "minecraft:skeleton_horse");
/*     */           paramHashMap.put("Slime", "minecraft:slime");
/*     */           paramHashMap.put("SmallFireball", "minecraft:small_fireball");
/*     */           paramHashMap.put("SnowMan", "minecraft:snowman");
/*     */           paramHashMap.put("Snowball", "minecraft:snowball");
/*     */           paramHashMap.put("SpectralArrow", "minecraft:spectral_arrow");
/*     */           paramHashMap.put("Spider", "minecraft:spider");
/*     */           paramHashMap.put("Squid", "minecraft:squid");
/*     */           paramHashMap.put("Stray", "minecraft:stray");
/*     */           paramHashMap.put("ThrownEgg", "minecraft:egg");
/*     */           paramHashMap.put("ThrownEnderpearl", "minecraft:ender_pearl");
/*     */           paramHashMap.put("ThrownExpBottle", "minecraft:xp_bottle");
/*     */           paramHashMap.put("ThrownPotion", "minecraft:potion");
/*     */           paramHashMap.put("Villager", "minecraft:villager");
/*     */           paramHashMap.put("VillagerGolem", "minecraft:villager_golem");
/*     */           paramHashMap.put("Witch", "minecraft:witch");
/*     */           paramHashMap.put("WitherBoss", "minecraft:wither");
/*     */           paramHashMap.put("WitherSkeleton", "minecraft:wither_skeleton");
/*     */           paramHashMap.put("WitherSkull", "minecraft:wither_skull");
/*     */           paramHashMap.put("Wolf", "minecraft:wolf");
/*     */           paramHashMap.put("XPOrb", "minecraft:xp_orb");
/*     */           paramHashMap.put("Zombie", "minecraft:zombie");
/*     */           paramHashMap.put("ZombieHorse", "minecraft:zombie_horse");
/*     */           paramHashMap.put("ZombieVillager", "minecraft:zombie_villager");
/*     */         });
/*     */   }
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  98 */     TaggedChoice.TaggedChoiceType taggedChoiceType1 = getInputSchema().findChoiceType(References.ENTITY);
/*  99 */     TaggedChoice.TaggedChoiceType taggedChoiceType2 = getOutputSchema().findChoiceType(References.ENTITY);
/*     */     
/* 101 */     Type type1 = getInputSchema().getType(References.ITEM_STACK);
/* 102 */     Type type2 = getOutputSchema().getType(References.ITEM_STACK);
/*     */     
/* 104 */     return TypeRewriteRule.seq(
/* 105 */         convertUnchecked("item stack entity name hook converter", type1, type2), 
/* 106 */         fixTypeEverywhere("EntityIdFix", (Type)taggedChoiceType1, (Type)taggedChoiceType2, paramDynamicOps -> ()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */