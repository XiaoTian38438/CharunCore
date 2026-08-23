/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DamageSources {
/*     */   private final Registry<DamageType> damageTypes;
/*     */   private final DamageSource inFire;
/*     */   private final DamageSource campfire;
/*     */   private final DamageSource lightningBolt;
/*     */   private final DamageSource onFire;
/*     */   private final DamageSource lava;
/*     */   private final DamageSource hotFloor;
/*     */   private final DamageSource inWall;
/*     */   private final DamageSource cramming;
/*     */   private final DamageSource drown;
/*     */   private final DamageSource starve;
/*     */   private final DamageSource cactus;
/*     */   private final DamageSource fall;
/*     */   private final DamageSource enderPearl;
/*     */   private final DamageSource flyIntoWall;
/*     */   private final DamageSource fellOutOfWorld;
/*     */   private final DamageSource generic;
/*     */   private final DamageSource magic;
/*     */   private final DamageSource wither;
/*     */   private final DamageSource dragonBreath;
/*     */   private final DamageSource dryOut;
/*     */   private final DamageSource sweetBerryBush;
/*     */   private final DamageSource freeze;
/*     */   private final DamageSource stalagmite;
/*     */   private final DamageSource outsideBorder;
/*     */   private final DamageSource genericKill;
/*     */   
/*     */   public DamageSources(RegistryAccess paramRegistryAccess) {
/*  47 */     this.damageTypes = paramRegistryAccess.lookupOrThrow(Registries.DAMAGE_TYPE);
/*  48 */     this.inFire = source(DamageTypes.IN_FIRE);
/*  49 */     this.campfire = source(DamageTypes.CAMPFIRE);
/*  50 */     this.lightningBolt = source(DamageTypes.LIGHTNING_BOLT);
/*  51 */     this.onFire = source(DamageTypes.ON_FIRE);
/*  52 */     this.lava = source(DamageTypes.LAVA);
/*  53 */     this.hotFloor = source(DamageTypes.HOT_FLOOR);
/*  54 */     this.inWall = source(DamageTypes.IN_WALL);
/*  55 */     this.cramming = source(DamageTypes.CRAMMING);
/*  56 */     this.drown = source(DamageTypes.DROWN);
/*  57 */     this.starve = source(DamageTypes.STARVE);
/*  58 */     this.cactus = source(DamageTypes.CACTUS);
/*  59 */     this.fall = source(DamageTypes.FALL);
/*  60 */     this.enderPearl = source(DamageTypes.ENDER_PEARL);
/*  61 */     this.flyIntoWall = source(DamageTypes.FLY_INTO_WALL);
/*  62 */     this.fellOutOfWorld = source(DamageTypes.FELL_OUT_OF_WORLD);
/*  63 */     this.generic = source(DamageTypes.GENERIC);
/*  64 */     this.magic = source(DamageTypes.MAGIC);
/*  65 */     this.wither = source(DamageTypes.WITHER);
/*  66 */     this.dragonBreath = source(DamageTypes.DRAGON_BREATH);
/*  67 */     this.dryOut = source(DamageTypes.DRY_OUT);
/*  68 */     this.sweetBerryBush = source(DamageTypes.SWEET_BERRY_BUSH);
/*  69 */     this.freeze = source(DamageTypes.FREEZE);
/*  70 */     this.stalagmite = source(DamageTypes.STALAGMITE);
/*  71 */     this.outsideBorder = source(DamageTypes.OUTSIDE_BORDER);
/*  72 */     this.genericKill = source(DamageTypes.GENERIC_KILL);
/*     */   }
/*     */   
/*     */   private DamageSource source(ResourceKey<DamageType> paramResourceKey) {
/*  76 */     return new DamageSource((Holder<DamageType>)this.damageTypes.getOrThrow(paramResourceKey));
/*     */   }
/*     */   
/*     */   private DamageSource source(ResourceKey<DamageType> paramResourceKey, Entity paramEntity) {
/*  80 */     return new DamageSource((Holder<DamageType>)this.damageTypes.getOrThrow(paramResourceKey), paramEntity);
/*     */   }
/*     */   
/*     */   private DamageSource source(ResourceKey<DamageType> paramResourceKey, Entity paramEntity1, Entity paramEntity2) {
/*  84 */     return new DamageSource((Holder<DamageType>)this.damageTypes.getOrThrow(paramResourceKey), paramEntity1, paramEntity2);
/*     */   }
/*     */   
/*     */   public DamageSource inFire() {
/*  88 */     return this.inFire;
/*     */   }
/*     */   
/*     */   public DamageSource campfire() {
/*  92 */     return this.campfire;
/*     */   }
/*     */   
/*     */   public DamageSource lightningBolt() {
/*  96 */     return this.lightningBolt;
/*     */   }
/*     */   
/*     */   public DamageSource onFire() {
/* 100 */     return this.onFire;
/*     */   }
/*     */   
/*     */   public DamageSource lava() {
/* 104 */     return this.lava;
/*     */   }
/*     */   
/*     */   public DamageSource hotFloor() {
/* 108 */     return this.hotFloor;
/*     */   }
/*     */   
/*     */   public DamageSource inWall() {
/* 112 */     return this.inWall;
/*     */   }
/*     */   
/*     */   public DamageSource cramming() {
/* 116 */     return this.cramming;
/*     */   }
/*     */   
/*     */   public DamageSource drown() {
/* 120 */     return this.drown;
/*     */   }
/*     */   
/*     */   public DamageSource starve() {
/* 124 */     return this.starve;
/*     */   }
/*     */   
/*     */   public DamageSource cactus() {
/* 128 */     return this.cactus;
/*     */   }
/*     */   
/*     */   public DamageSource fall() {
/* 132 */     return this.fall;
/*     */   }
/*     */   
/*     */   public DamageSource enderPearl() {
/* 136 */     return this.enderPearl;
/*     */   }
/*     */   
/*     */   public DamageSource flyIntoWall() {
/* 140 */     return this.flyIntoWall;
/*     */   }
/*     */   
/*     */   public DamageSource fellOutOfWorld() {
/* 144 */     return this.fellOutOfWorld;
/*     */   }
/*     */   
/*     */   public DamageSource generic() {
/* 148 */     return this.generic;
/*     */   }
/*     */   
/*     */   public DamageSource magic() {
/* 152 */     return this.magic;
/*     */   }
/*     */   
/*     */   public DamageSource wither() {
/* 156 */     return this.wither;
/*     */   }
/*     */   
/*     */   public DamageSource dragonBreath() {
/* 160 */     return this.dragonBreath;
/*     */   }
/*     */   
/*     */   public DamageSource dryOut() {
/* 164 */     return this.dryOut;
/*     */   }
/*     */   
/*     */   public DamageSource sweetBerryBush() {
/* 168 */     return this.sweetBerryBush;
/*     */   }
/*     */   
/*     */   public DamageSource freeze() {
/* 172 */     return this.freeze;
/*     */   }
/*     */   
/*     */   public DamageSource stalagmite() {
/* 176 */     return this.stalagmite;
/*     */   }
/*     */   
/*     */   public DamageSource fallingBlock(Entity paramEntity) {
/* 180 */     return source(DamageTypes.FALLING_BLOCK, paramEntity);
/*     */   }
/*     */   
/*     */   public DamageSource anvil(Entity paramEntity) {
/* 184 */     return source(DamageTypes.FALLING_ANVIL, paramEntity);
/*     */   }
/*     */   
/*     */   public DamageSource fallingStalactite(Entity paramEntity) {
/* 188 */     return source(DamageTypes.FALLING_STALACTITE, paramEntity);
/*     */   }
/*     */   
/*     */   public DamageSource sting(LivingEntity paramLivingEntity) {
/* 192 */     return source(DamageTypes.STING, (Entity)paramLivingEntity);
/*     */   }
/*     */   
/*     */   public DamageSource mobAttack(LivingEntity paramLivingEntity) {
/* 196 */     return source(DamageTypes.MOB_ATTACK, (Entity)paramLivingEntity);
/*     */   }
/*     */   
/*     */   public DamageSource noAggroMobAttack(LivingEntity paramLivingEntity) {
/* 200 */     return source(DamageTypes.MOB_ATTACK_NO_AGGRO, (Entity)paramLivingEntity);
/*     */   }
/*     */   
/*     */   public DamageSource playerAttack(Player paramPlayer) {
/* 204 */     return source(DamageTypes.PLAYER_ATTACK, (Entity)paramPlayer);
/*     */   }
/*     */   
/*     */   public DamageSource arrow(AbstractArrow paramAbstractArrow, Entity paramEntity) {
/* 208 */     return source(DamageTypes.ARROW, (Entity)paramAbstractArrow, paramEntity);
/*     */   }
/*     */   
/*     */   public DamageSource trident(Entity paramEntity1, Entity paramEntity2) {
/* 212 */     return source(DamageTypes.TRIDENT, paramEntity1, paramEntity2);
/*     */   }
/*     */ 
/*     */   
/*     */   public DamageSource mobProjectile(Entity paramEntity, LivingEntity paramLivingEntity) {
/* 217 */     return source(DamageTypes.MOB_PROJECTILE, paramEntity, (Entity)paramLivingEntity);
/*     */   }
/*     */   
/*     */   public DamageSource spit(Entity paramEntity, LivingEntity paramLivingEntity) {
/* 221 */     return source(DamageTypes.SPIT, paramEntity, (Entity)paramLivingEntity);
/*     */   }
/*     */   
/*     */   public DamageSource windCharge(Entity paramEntity, LivingEntity paramLivingEntity) {
/* 225 */     return source(DamageTypes.WIND_CHARGE, paramEntity, (Entity)paramLivingEntity);
/*     */   }
/*     */   
/*     */   public DamageSource fireworks(FireworkRocketEntity paramFireworkRocketEntity, Entity paramEntity) {
/* 229 */     return source(DamageTypes.FIREWORKS, (Entity)paramFireworkRocketEntity, paramEntity);
/*     */   }
/*     */   
/*     */   public DamageSource fireball(Fireball paramFireball, Entity paramEntity) {
/* 233 */     if (paramEntity == null) {
/* 234 */       return source(DamageTypes.UNATTRIBUTED_FIREBALL, (Entity)paramFireball);
/*     */     }
/* 236 */     return source(DamageTypes.FIREBALL, (Entity)paramFireball, paramEntity);
/*     */   }
/*     */   
/*     */   public DamageSource witherSkull(WitherSkull paramWitherSkull, Entity paramEntity) {
/* 240 */     return source(DamageTypes.WITHER_SKULL, (Entity)paramWitherSkull, paramEntity);
/*     */   }
/*     */   
/*     */   public DamageSource thrown(Entity paramEntity1, Entity paramEntity2) {
/* 244 */     return source(DamageTypes.THROWN, paramEntity1, paramEntity2);
/*     */   }
/*     */   
/*     */   public DamageSource indirectMagic(Entity paramEntity1, Entity paramEntity2) {
/* 248 */     return source(DamageTypes.INDIRECT_MAGIC, paramEntity1, paramEntity2);
/*     */   }
/*     */   
/*     */   public DamageSource thorns(Entity paramEntity) {
/* 252 */     return source(DamageTypes.THORNS, paramEntity);
/*     */   }
/*     */   
/*     */   public DamageSource explosion(Explosion paramExplosion) {
/* 256 */     return (paramExplosion != null) ? explosion(paramExplosion.getDirectSourceEntity(), (Entity)paramExplosion.getIndirectSourceEntity()) : explosion(null, null);
/*     */   }
/*     */   
/*     */   public DamageSource explosion(Entity paramEntity1, Entity paramEntity2) {
/* 260 */     return source((paramEntity2 != null && paramEntity1 != null) ? DamageTypes.PLAYER_EXPLOSION : DamageTypes.EXPLOSION, paramEntity1, paramEntity2);
/*     */   }
/*     */   
/*     */   public DamageSource sonicBoom(Entity paramEntity) {
/* 264 */     return source(DamageTypes.SONIC_BOOM, paramEntity);
/*     */   }
/*     */   
/*     */   public DamageSource badRespawnPointExplosion(Vec3 paramVec3) {
/* 268 */     return new DamageSource((Holder<DamageType>)this.damageTypes.getOrThrow(DamageTypes.BAD_RESPAWN_POINT), paramVec3);
/*     */   }
/*     */   
/*     */   public DamageSource outOfBorder() {
/* 272 */     return this.outsideBorder;
/*     */   }
/*     */   
/*     */   public DamageSource genericKill() {
/* 276 */     return this.genericKill;
/*     */   }
/*     */   
/*     */   public DamageSource mace(Entity paramEntity) {
/* 280 */     return source(DamageTypes.MACE_SMASH, paramEntity);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\damagesource\DamageSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */