/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EntityUUIDFix extends AbstractUUIDFix {
/*  16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  18 */   private static final Set<String> ABSTRACT_HORSES = Sets.newHashSet();
/*  19 */   private static final Set<String> TAMEABLE_ANIMALS = Sets.newHashSet();
/*  20 */   private static final Set<String> ANIMALS = Sets.newHashSet();
/*  21 */   private static final Set<String> MOBS = Sets.newHashSet();
/*  22 */   private static final Set<String> LIVING_ENTITIES = Sets.newHashSet();
/*  23 */   private static final Set<String> PROJECTILES = Sets.newHashSet();
/*     */   
/*     */   static {
/*  26 */     ABSTRACT_HORSES.add("minecraft:donkey");
/*  27 */     ABSTRACT_HORSES.add("minecraft:horse");
/*  28 */     ABSTRACT_HORSES.add("minecraft:llama");
/*  29 */     ABSTRACT_HORSES.add("minecraft:mule");
/*  30 */     ABSTRACT_HORSES.add("minecraft:skeleton_horse");
/*  31 */     ABSTRACT_HORSES.add("minecraft:trader_llama");
/*  32 */     ABSTRACT_HORSES.add("minecraft:zombie_horse");
/*  33 */     TAMEABLE_ANIMALS.add("minecraft:cat");
/*  34 */     TAMEABLE_ANIMALS.add("minecraft:parrot");
/*  35 */     TAMEABLE_ANIMALS.add("minecraft:wolf");
/*  36 */     ANIMALS.add("minecraft:bee");
/*  37 */     ANIMALS.add("minecraft:chicken");
/*  38 */     ANIMALS.add("minecraft:cow");
/*  39 */     ANIMALS.add("minecraft:fox");
/*  40 */     ANIMALS.add("minecraft:mooshroom");
/*  41 */     ANIMALS.add("minecraft:ocelot");
/*  42 */     ANIMALS.add("minecraft:panda");
/*  43 */     ANIMALS.add("minecraft:pig");
/*  44 */     ANIMALS.add("minecraft:polar_bear");
/*  45 */     ANIMALS.add("minecraft:rabbit");
/*  46 */     ANIMALS.add("minecraft:sheep");
/*  47 */     ANIMALS.add("minecraft:turtle");
/*  48 */     ANIMALS.add("minecraft:hoglin");
/*  49 */     MOBS.add("minecraft:bat");
/*  50 */     MOBS.add("minecraft:blaze");
/*  51 */     MOBS.add("minecraft:cave_spider");
/*  52 */     MOBS.add("minecraft:cod");
/*  53 */     MOBS.add("minecraft:creeper");
/*  54 */     MOBS.add("minecraft:dolphin");
/*  55 */     MOBS.add("minecraft:drowned");
/*  56 */     MOBS.add("minecraft:elder_guardian");
/*  57 */     MOBS.add("minecraft:ender_dragon");
/*  58 */     MOBS.add("minecraft:enderman");
/*  59 */     MOBS.add("minecraft:endermite");
/*  60 */     MOBS.add("minecraft:evoker");
/*  61 */     MOBS.add("minecraft:ghast");
/*  62 */     MOBS.add("minecraft:giant");
/*  63 */     MOBS.add("minecraft:guardian");
/*  64 */     MOBS.add("minecraft:husk");
/*  65 */     MOBS.add("minecraft:illusioner");
/*  66 */     MOBS.add("minecraft:magma_cube");
/*  67 */     MOBS.add("minecraft:pufferfish");
/*  68 */     MOBS.add("minecraft:zombified_piglin");
/*  69 */     MOBS.add("minecraft:salmon");
/*  70 */     MOBS.add("minecraft:shulker");
/*  71 */     MOBS.add("minecraft:silverfish");
/*  72 */     MOBS.add("minecraft:skeleton");
/*  73 */     MOBS.add("minecraft:slime");
/*  74 */     MOBS.add("minecraft:snow_golem");
/*  75 */     MOBS.add("minecraft:spider");
/*  76 */     MOBS.add("minecraft:squid");
/*  77 */     MOBS.add("minecraft:stray");
/*  78 */     MOBS.add("minecraft:tropical_fish");
/*  79 */     MOBS.add("minecraft:vex");
/*  80 */     MOBS.add("minecraft:villager");
/*  81 */     MOBS.add("minecraft:iron_golem");
/*  82 */     MOBS.add("minecraft:vindicator");
/*  83 */     MOBS.add("minecraft:pillager");
/*  84 */     MOBS.add("minecraft:wandering_trader");
/*  85 */     MOBS.add("minecraft:witch");
/*  86 */     MOBS.add("minecraft:wither");
/*  87 */     MOBS.add("minecraft:wither_skeleton");
/*  88 */     MOBS.add("minecraft:zombie");
/*  89 */     MOBS.add("minecraft:zombie_villager");
/*  90 */     MOBS.add("minecraft:phantom");
/*  91 */     MOBS.add("minecraft:ravager");
/*  92 */     MOBS.add("minecraft:piglin");
/*  93 */     LIVING_ENTITIES.add("minecraft:armor_stand");
/*  94 */     PROJECTILES.add("minecraft:arrow");
/*  95 */     PROJECTILES.add("minecraft:dragon_fireball");
/*  96 */     PROJECTILES.add("minecraft:firework_rocket");
/*  97 */     PROJECTILES.add("minecraft:fireball");
/*  98 */     PROJECTILES.add("minecraft:llama_spit");
/*  99 */     PROJECTILES.add("minecraft:small_fireball");
/* 100 */     PROJECTILES.add("minecraft:snowball");
/* 101 */     PROJECTILES.add("minecraft:spectral_arrow");
/* 102 */     PROJECTILES.add("minecraft:egg");
/* 103 */     PROJECTILES.add("minecraft:ender_pearl");
/* 104 */     PROJECTILES.add("minecraft:experience_bottle");
/* 105 */     PROJECTILES.add("minecraft:potion");
/* 106 */     PROJECTILES.add("minecraft:trident");
/* 107 */     PROJECTILES.add("minecraft:wither_skull");
/*     */   }
/*     */   
/*     */   public EntityUUIDFix(Schema paramSchema) {
/* 111 */     super(paramSchema, References.ENTITY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 116 */     return fixTypeEverywhereTyped("EntityUUIDFixes", getInputSchema().getType(this.typeReference), paramTyped -> {
/*     */           paramTyped = paramTyped.update(DSL.remainderFinder(), EntityUUIDFix::updateEntityUUID);
/*     */           for (String str : ABSTRACT_HORSES) {
/*     */             paramTyped = updateNamedChoice(paramTyped, str, EntityUUIDFix::updateAnimalOwner);
/*     */           }
/*     */           for (String str : TAMEABLE_ANIMALS) {
/*     */             paramTyped = updateNamedChoice(paramTyped, str, EntityUUIDFix::updateAnimalOwner);
/*     */           }
/*     */           for (String str : ANIMALS) {
/*     */             paramTyped = updateNamedChoice(paramTyped, str, EntityUUIDFix::updateAnimal);
/*     */           }
/*     */           for (String str : MOBS) {
/*     */             paramTyped = updateNamedChoice(paramTyped, str, EntityUUIDFix::updateMob);
/*     */           }
/*     */           for (String str : LIVING_ENTITIES) {
/*     */             paramTyped = updateNamedChoice(paramTyped, str, EntityUUIDFix::updateLivingEntity);
/*     */           }
/*     */           for (String str : PROJECTILES) {
/*     */             paramTyped = updateNamedChoice(paramTyped, str, EntityUUIDFix::updateProjectile);
/*     */           }
/*     */           paramTyped = updateNamedChoice(paramTyped, "minecraft:bee", EntityUUIDFix::updateHurtBy);
/*     */           paramTyped = updateNamedChoice(paramTyped, "minecraft:zombified_piglin", EntityUUIDFix::updateHurtBy);
/*     */           paramTyped = updateNamedChoice(paramTyped, "minecraft:fox", EntityUUIDFix::updateFox);
/*     */           paramTyped = updateNamedChoice(paramTyped, "minecraft:item", EntityUUIDFix::updateItem);
/*     */           paramTyped = updateNamedChoice(paramTyped, "minecraft:shulker_bullet", EntityUUIDFix::updateShulkerBullet);
/*     */           paramTyped = updateNamedChoice(paramTyped, "minecraft:area_effect_cloud", EntityUUIDFix::updateAreaEffectCloud);
/*     */           paramTyped = updateNamedChoice(paramTyped, "minecraft:zombie_villager", EntityUUIDFix::updateZombieVillager);
/*     */           paramTyped = updateNamedChoice(paramTyped, "minecraft:evoker_fangs", EntityUUIDFix::updateEvokerFangs);
/*     */           return updateNamedChoice(paramTyped, "minecraft:piglin", EntityUUIDFix::updatePiglin);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updatePiglin(Dynamic<?> paramDynamic) {
/* 150 */     return paramDynamic.update("Brain", paramDynamic -> paramDynamic.update("memories", ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updateEvokerFangs(Dynamic<?> paramDynamic) {
/* 161 */     return replaceUUIDLeastMost(paramDynamic, "OwnerUUID", "Owner").orElse(paramDynamic);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateZombieVillager(Dynamic<?> paramDynamic) {
/* 165 */     return replaceUUIDLeastMost(paramDynamic, "ConversionPlayer", "ConversionPlayer").orElse(paramDynamic);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateAreaEffectCloud(Dynamic<?> paramDynamic) {
/* 169 */     return replaceUUIDLeastMost(paramDynamic, "OwnerUUID", "Owner").orElse(paramDynamic);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateShulkerBullet(Dynamic<?> paramDynamic) {
/* 173 */     paramDynamic = replaceUUIDMLTag(paramDynamic, "Owner", "Owner").orElse(paramDynamic);
/* 174 */     return replaceUUIDMLTag(paramDynamic, "Target", "Target").orElse(paramDynamic);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateItem(Dynamic<?> paramDynamic) {
/* 178 */     paramDynamic = replaceUUIDMLTag(paramDynamic, "Owner", "Owner").orElse(paramDynamic);
/* 179 */     return replaceUUIDMLTag(paramDynamic, "Thrower", "Thrower").orElse(paramDynamic);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateFox(Dynamic<?> paramDynamic) {
/* 183 */     Optional optional = paramDynamic.get("TrustedUUIDs").result().map(paramDynamic2 -> paramDynamic1.createList(paramDynamic2.asStream().map(())));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     return (Dynamic)DataFixUtils.orElse(optional.map(paramDynamic2 -> paramDynamic1.remove("TrustedUUIDs").set("Trusted", paramDynamic2)), paramDynamic);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updateHurtBy(Dynamic<?> paramDynamic) {
/* 197 */     return replaceUUIDString(paramDynamic, "HurtBy", "HurtBy").orElse(paramDynamic);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateAnimalOwner(Dynamic<?> paramDynamic) {
/* 201 */     Dynamic<?> dynamic = updateAnimal(paramDynamic);
/* 202 */     return replaceUUIDString(dynamic, "OwnerUUID", "Owner").orElse(dynamic);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateAnimal(Dynamic<?> paramDynamic) {
/* 206 */     Dynamic<?> dynamic = updateMob(paramDynamic);
/* 207 */     return replaceUUIDLeastMost(dynamic, "LoveCause", "LoveCause").orElse(dynamic);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateMob(Dynamic<?> paramDynamic) {
/* 211 */     return updateLivingEntity(paramDynamic).update("Leash", paramDynamic -> (Dynamic)replaceUUIDLeastMost(paramDynamic, "UUID", "UUID").orElse(paramDynamic));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Dynamic<?> updateLivingEntity(Dynamic<?> paramDynamic) {
/* 217 */     return paramDynamic.update("Attributes", paramDynamic2 -> paramDynamic1.createList(paramDynamic2.asStream().map(())));
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
/*     */   private static Dynamic<?> updateProjectile(Dynamic<?> paramDynamic) {
/* 229 */     return (Dynamic)DataFixUtils.orElse(paramDynamic.get("OwnerUUID").result().map(paramDynamic2 -> paramDynamic1.remove("OwnerUUID").set("Owner", paramDynamic2)), paramDynamic);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Dynamic<?> updateEntityUUID(Dynamic<?> paramDynamic) {
/* 235 */     return replaceUUIDLeastMost(paramDynamic, "UUID", "UUID").orElse(paramDynamic);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */