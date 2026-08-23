/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.ambient.Bat;
/*     */ import net.minecraft.world.entity.animal.allay.Allay;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.entity.animal.bee.Bee;
/*     */ import net.minecraft.world.entity.animal.camel.Camel;
/*     */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*     */ import net.minecraft.world.entity.animal.cow.Cow;
/*     */ import net.minecraft.world.entity.animal.dolphin.Dolphin;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.entity.animal.equine.SkeletonHorse;
/*     */ import net.minecraft.world.entity.animal.equine.ZombieHorse;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.animal.feline.Ocelot;
/*     */ import net.minecraft.world.entity.animal.fish.AbstractFish;
/*     */ import net.minecraft.world.entity.animal.fox.Fox;
/*     */ import net.minecraft.world.entity.animal.frog.Frog;
/*     */ import net.minecraft.world.entity.animal.frog.Tadpole;
/*     */ import net.minecraft.world.entity.animal.goat.Goat;
/*     */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.golem.SnowGolem;
/*     */ import net.minecraft.world.entity.animal.happyghast.HappyGhast;
/*     */ import net.minecraft.world.entity.animal.nautilus.Nautilus;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
/*     */ import net.minecraft.world.entity.animal.panda.Panda;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ import net.minecraft.world.entity.animal.pig.Pig;
/*     */ import net.minecraft.world.entity.animal.polarbear.PolarBear;
/*     */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*     */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*     */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*     */ import net.minecraft.world.entity.animal.squid.GlowSquid;
/*     */ import net.minecraft.world.entity.animal.squid.Squid;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.entity.monster.Blaze;
/*     */ import net.minecraft.world.entity.monster.Creeper;
/*     */ import net.minecraft.world.entity.monster.ElderGuardian;
/*     */ import net.minecraft.world.entity.monster.EnderMan;
/*     */ import net.minecraft.world.entity.monster.Endermite;
/*     */ import net.minecraft.world.entity.monster.Ghast;
/*     */ import net.minecraft.world.entity.monster.Giant;
/*     */ import net.minecraft.world.entity.monster.Guardian;
/*     */ import net.minecraft.world.entity.monster.MagmaCube;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.Ravager;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.entity.monster.Silverfish;
/*     */ import net.minecraft.world.entity.monster.Strider;
/*     */ import net.minecraft.world.entity.monster.Vex;
/*     */ import net.minecraft.world.entity.monster.Witch;
/*     */ import net.minecraft.world.entity.monster.Zoglin;
/*     */ import net.minecraft.world.entity.monster.breeze.Breeze;
/*     */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.illager.Evoker;
/*     */ import net.minecraft.world.entity.monster.illager.Illusioner;
/*     */ import net.minecraft.world.entity.monster.illager.Pillager;
/*     */ import net.minecraft.world.entity.monster.illager.Vindicator;
/*     */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinBrute;
/*     */ import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
/*     */ import net.minecraft.world.entity.monster.skeleton.Bogged;
/*     */ import net.minecraft.world.entity.monster.skeleton.Parched;
/*     */ import net.minecraft.world.entity.monster.spider.CaveSpider;
/*     */ import net.minecraft.world.entity.monster.spider.Spider;
/*     */ import net.minecraft.world.entity.monster.warden.Warden;
/*     */ import net.minecraft.world.entity.monster.zombie.Drowned;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DefaultAttributes {
/*  94 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  96 */   private static final Map<EntityType<? extends LivingEntity>, AttributeSupplier> SUPPLIERS = (Map<EntityType<? extends LivingEntity>, AttributeSupplier>)ImmutableMap.builder()
/*  97 */     .put(EntityType.ALLAY, Allay.createAttributes().build())
/*  98 */     .put(EntityType.ARMADILLO, Armadillo.createAttributes().build())
/*  99 */     .put(EntityType.ARMOR_STAND, ArmorStand.createAttributes().build())
/* 100 */     .put(EntityType.AXOLOTL, Axolotl.createAttributes().build())
/* 101 */     .put(EntityType.BAT, Bat.createAttributes().build())
/* 102 */     .put(EntityType.BEE, Bee.createAttributes().build())
/* 103 */     .put(EntityType.BLAZE, Blaze.createAttributes().build())
/* 104 */     .put(EntityType.BOGGED, Bogged.createAttributes().build())
/* 105 */     .put(EntityType.CAT, Cat.createAttributes().build())
/* 106 */     .put(EntityType.CAMEL, Camel.createAttributes().build())
/* 107 */     .put(EntityType.CAMEL_HUSK, Camel.createAttributes().build())
/* 108 */     .put(EntityType.CAVE_SPIDER, CaveSpider.createCaveSpider().build())
/* 109 */     .put(EntityType.CHICKEN, Chicken.createAttributes().build())
/* 110 */     .put(EntityType.COD, AbstractFish.createAttributes().build())
/* 111 */     .put(EntityType.COPPER_GOLEM, CopperGolem.createAttributes().build())
/* 112 */     .put(EntityType.COW, Cow.createAttributes().build())
/* 113 */     .put(EntityType.CREAKING, Creaking.createAttributes().build())
/* 114 */     .put(EntityType.CREEPER, Creeper.createAttributes().build())
/* 115 */     .put(EntityType.DOLPHIN, Dolphin.createAttributes().build())
/* 116 */     .put(EntityType.DONKEY, AbstractChestedHorse.createBaseChestedHorseAttributes().build())
/* 117 */     .put(EntityType.DROWNED, Drowned.createAttributes().build())
/* 118 */     .put(EntityType.ELDER_GUARDIAN, ElderGuardian.createAttributes().build())
/* 119 */     .put(EntityType.ENDERMAN, EnderMan.createAttributes().build())
/* 120 */     .put(EntityType.ENDERMITE, Endermite.createAttributes().build())
/* 121 */     .put(EntityType.ENDER_DRAGON, EnderDragon.createAttributes().build())
/* 122 */     .put(EntityType.EVOKER, Evoker.createAttributes().build())
/* 123 */     .put(EntityType.BREEZE, Breeze.createAttributes().build())
/* 124 */     .put(EntityType.FOX, Fox.createAttributes().build())
/* 125 */     .put(EntityType.FROG, Frog.createAttributes().build())
/* 126 */     .put(EntityType.GHAST, Ghast.createAttributes().build())
/* 127 */     .put(EntityType.HAPPY_GHAST, HappyGhast.createAttributes().build())
/* 128 */     .put(EntityType.GIANT, Giant.createAttributes().build())
/* 129 */     .put(EntityType.GLOW_SQUID, GlowSquid.createAttributes().build())
/* 130 */     .put(EntityType.GOAT, Goat.createAttributes().build())
/* 131 */     .put(EntityType.GUARDIAN, Guardian.createAttributes().build())
/* 132 */     .put(EntityType.HOGLIN, Hoglin.createAttributes().build())
/* 133 */     .put(EntityType.HORSE, AbstractHorse.createBaseHorseAttributes().build())
/* 134 */     .put(EntityType.HUSK, Zombie.createAttributes().build())
/* 135 */     .put(EntityType.ILLUSIONER, Illusioner.createAttributes().build())
/* 136 */     .put(EntityType.IRON_GOLEM, IronGolem.createAttributes().build())
/* 137 */     .put(EntityType.LLAMA, Llama.createAttributes().build())
/* 138 */     .put(EntityType.MAGMA_CUBE, MagmaCube.createAttributes().build())
/* 139 */     .put(EntityType.MANNEQUIN, LivingEntity.createLivingAttributes().build())
/* 140 */     .put(EntityType.MOOSHROOM, Cow.createAttributes().build())
/* 141 */     .put(EntityType.MULE, AbstractChestedHorse.createBaseChestedHorseAttributes().build())
/* 142 */     .put(EntityType.NAUTILUS, Nautilus.createAttributes().build())
/* 143 */     .put(EntityType.OCELOT, Ocelot.createAttributes().build())
/* 144 */     .put(EntityType.PANDA, Panda.createAttributes().build())
/* 145 */     .put(EntityType.PARCHED, Parched.createAttributes().build())
/* 146 */     .put(EntityType.PARROT, Parrot.createAttributes().build())
/* 147 */     .put(EntityType.PHANTOM, Monster.createMonsterAttributes().build())
/* 148 */     .put(EntityType.PIG, Pig.createAttributes().build())
/* 149 */     .put(EntityType.PIGLIN, Piglin.createAttributes().build())
/* 150 */     .put(EntityType.PIGLIN_BRUTE, PiglinBrute.createAttributes().build())
/* 151 */     .put(EntityType.PILLAGER, Pillager.createAttributes().build())
/* 152 */     .put(EntityType.PLAYER, Player.createAttributes().build())
/* 153 */     .put(EntityType.POLAR_BEAR, PolarBear.createAttributes().build())
/* 154 */     .put(EntityType.PUFFERFISH, AbstractFish.createAttributes().build())
/* 155 */     .put(EntityType.RABBIT, Rabbit.createAttributes().build())
/* 156 */     .put(EntityType.RAVAGER, Ravager.createAttributes().build())
/* 157 */     .put(EntityType.SALMON, AbstractFish.createAttributes().build())
/* 158 */     .put(EntityType.SHEEP, Sheep.createAttributes().build())
/* 159 */     .put(EntityType.SHULKER, Shulker.createAttributes().build())
/* 160 */     .put(EntityType.SILVERFISH, Silverfish.createAttributes().build())
/* 161 */     .put(EntityType.SKELETON, AbstractSkeleton.createAttributes().build())
/* 162 */     .put(EntityType.SKELETON_HORSE, SkeletonHorse.createAttributes().build())
/* 163 */     .put(EntityType.SLIME, Monster.createMonsterAttributes().build())
/* 164 */     .put(EntityType.SNIFFER, Sniffer.createAttributes().build())
/* 165 */     .put(EntityType.SNOW_GOLEM, SnowGolem.createAttributes().build())
/* 166 */     .put(EntityType.SPIDER, Spider.createAttributes().build())
/* 167 */     .put(EntityType.SQUID, Squid.createAttributes().build())
/* 168 */     .put(EntityType.STRAY, AbstractSkeleton.createAttributes().build())
/* 169 */     .put(EntityType.STRIDER, Strider.createAttributes().build())
/* 170 */     .put(EntityType.TADPOLE, Tadpole.createAttributes().build())
/* 171 */     .put(EntityType.TRADER_LLAMA, Llama.createAttributes().build())
/* 172 */     .put(EntityType.TROPICAL_FISH, AbstractFish.createAttributes().build())
/* 173 */     .put(EntityType.TURTLE, Turtle.createAttributes().build())
/* 174 */     .put(EntityType.VEX, Vex.createAttributes().build())
/* 175 */     .put(EntityType.VILLAGER, Villager.createAttributes().build())
/* 176 */     .put(EntityType.VINDICATOR, Vindicator.createAttributes().build())
/* 177 */     .put(EntityType.WARDEN, Warden.createAttributes().build())
/* 178 */     .put(EntityType.WANDERING_TRADER, Mob.createMobAttributes().build())
/* 179 */     .put(EntityType.WITCH, Witch.createAttributes().build())
/* 180 */     .put(EntityType.WITHER, WitherBoss.createAttributes().build())
/* 181 */     .put(EntityType.WITHER_SKELETON, AbstractSkeleton.createAttributes().build())
/* 182 */     .put(EntityType.WOLF, Wolf.createAttributes().build())
/* 183 */     .put(EntityType.ZOGLIN, Zoglin.createAttributes().build())
/* 184 */     .put(EntityType.ZOMBIE, Zombie.createAttributes().build())
/* 185 */     .put(EntityType.ZOMBIE_HORSE, ZombieHorse.createAttributes().build())
/* 186 */     .put(EntityType.ZOMBIE_NAUTILUS, ZombieNautilus.createAttributes().build())
/* 187 */     .put(EntityType.ZOMBIE_VILLAGER, Zombie.createAttributes().build())
/* 188 */     .put(EntityType.ZOMBIFIED_PIGLIN, ZombifiedPiglin.createAttributes().build())
/* 189 */     .build();
/*     */   
/*     */   public static AttributeSupplier getSupplier(EntityType<? extends LivingEntity> paramEntityType) {
/* 192 */     return SUPPLIERS.get(paramEntityType);
/*     */   }
/*     */   
/*     */   public static boolean hasSupplier(EntityType<?> paramEntityType) {
/* 196 */     return SUPPLIERS.containsKey(paramEntityType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void validate() {
/* 203 */     Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE); BuiltInRegistries.ENTITY_TYPE.stream().filter(paramEntityType -> (paramEntityType.getCategory() != MobCategory.MISC)).filter(paramEntityType -> !hasSupplier(paramEntityType)).map(BuiltInRegistries.ENTITY_TYPE::getKey)
/* 204 */       .forEach(paramIdentifier -> Util.logAndPauseIfInIde("Entity " + String.valueOf(paramIdentifier) + " has no attributes"));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\attributes\DefaultAttributes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */