/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public interface DamageTypes
/*     */ {
/*  10 */   public static final ResourceKey<DamageType> IN_FIRE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("in_fire"));
/*  11 */   public static final ResourceKey<DamageType> CAMPFIRE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("campfire"));
/*  12 */   public static final ResourceKey<DamageType> LIGHTNING_BOLT = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("lightning_bolt"));
/*  13 */   public static final ResourceKey<DamageType> ON_FIRE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("on_fire"));
/*  14 */   public static final ResourceKey<DamageType> LAVA = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("lava"));
/*  15 */   public static final ResourceKey<DamageType> HOT_FLOOR = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("hot_floor"));
/*  16 */   public static final ResourceKey<DamageType> IN_WALL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("in_wall"));
/*  17 */   public static final ResourceKey<DamageType> CRAMMING = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("cramming"));
/*  18 */   public static final ResourceKey<DamageType> DROWN = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("drown"));
/*  19 */   public static final ResourceKey<DamageType> STARVE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("starve"));
/*  20 */   public static final ResourceKey<DamageType> CACTUS = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("cactus"));
/*  21 */   public static final ResourceKey<DamageType> FALL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("fall"));
/*  22 */   public static final ResourceKey<DamageType> ENDER_PEARL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("ender_pearl"));
/*  23 */   public static final ResourceKey<DamageType> FLY_INTO_WALL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("fly_into_wall"));
/*  24 */   public static final ResourceKey<DamageType> FELL_OUT_OF_WORLD = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("out_of_world"));
/*  25 */   public static final ResourceKey<DamageType> GENERIC = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("generic"));
/*  26 */   public static final ResourceKey<DamageType> MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("magic"));
/*  27 */   public static final ResourceKey<DamageType> WITHER = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("wither"));
/*  28 */   public static final ResourceKey<DamageType> DRAGON_BREATH = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("dragon_breath"));
/*  29 */   public static final ResourceKey<DamageType> DRY_OUT = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("dry_out"));
/*  30 */   public static final ResourceKey<DamageType> SWEET_BERRY_BUSH = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("sweet_berry_bush"));
/*  31 */   public static final ResourceKey<DamageType> FREEZE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("freeze"));
/*  32 */   public static final ResourceKey<DamageType> STALAGMITE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("stalagmite"));
/*  33 */   public static final ResourceKey<DamageType> FALLING_BLOCK = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("falling_block"));
/*  34 */   public static final ResourceKey<DamageType> FALLING_ANVIL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("falling_anvil"));
/*  35 */   public static final ResourceKey<DamageType> FALLING_STALACTITE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("falling_stalactite"));
/*  36 */   public static final ResourceKey<DamageType> STING = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("sting"));
/*  37 */   public static final ResourceKey<DamageType> MOB_ATTACK = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("mob_attack"));
/*  38 */   public static final ResourceKey<DamageType> MOB_ATTACK_NO_AGGRO = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("mob_attack_no_aggro"));
/*  39 */   public static final ResourceKey<DamageType> PLAYER_ATTACK = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("player_attack"));
/*  40 */   public static final ResourceKey<DamageType> SPEAR = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("spear"));
/*  41 */   public static final ResourceKey<DamageType> ARROW = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("arrow"));
/*  42 */   public static final ResourceKey<DamageType> TRIDENT = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("trident"));
/*  43 */   public static final ResourceKey<DamageType> MOB_PROJECTILE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("mob_projectile"));
/*  44 */   public static final ResourceKey<DamageType> SPIT = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("spit"));
/*  45 */   public static final ResourceKey<DamageType> WIND_CHARGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("wind_charge"));
/*  46 */   public static final ResourceKey<DamageType> FIREWORKS = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("fireworks"));
/*  47 */   public static final ResourceKey<DamageType> FIREBALL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("fireball"));
/*  48 */   public static final ResourceKey<DamageType> UNATTRIBUTED_FIREBALL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("unattributed_fireball"));
/*  49 */   public static final ResourceKey<DamageType> WITHER_SKULL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("wither_skull"));
/*  50 */   public static final ResourceKey<DamageType> THROWN = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("thrown"));
/*  51 */   public static final ResourceKey<DamageType> INDIRECT_MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("indirect_magic"));
/*  52 */   public static final ResourceKey<DamageType> THORNS = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("thorns"));
/*  53 */   public static final ResourceKey<DamageType> EXPLOSION = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("explosion"));
/*  54 */   public static final ResourceKey<DamageType> PLAYER_EXPLOSION = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("player_explosion"));
/*  55 */   public static final ResourceKey<DamageType> SONIC_BOOM = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("sonic_boom"));
/*  56 */   public static final ResourceKey<DamageType> BAD_RESPAWN_POINT = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("bad_respawn_point"));
/*  57 */   public static final ResourceKey<DamageType> OUTSIDE_BORDER = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("outside_border"));
/*  58 */   public static final ResourceKey<DamageType> GENERIC_KILL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("generic_kill"));
/*  59 */   public static final ResourceKey<DamageType> MACE_SMASH = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("mace_smash"));
/*     */   
/*     */   static void bootstrap(BootstrapContext<DamageType> paramBootstrapContext) {
/*  62 */     paramBootstrapContext.register(IN_FIRE, new DamageType("inFire", 0.1F, DamageEffects.BURNING));
/*  63 */     paramBootstrapContext.register(CAMPFIRE, new DamageType("inFire", 0.1F, DamageEffects.BURNING));
/*  64 */     paramBootstrapContext.register(LIGHTNING_BOLT, new DamageType("lightningBolt", 0.1F));
/*  65 */     paramBootstrapContext.register(ON_FIRE, new DamageType("onFire", 0.0F, DamageEffects.BURNING));
/*  66 */     paramBootstrapContext.register(LAVA, new DamageType("lava", 0.1F, DamageEffects.BURNING));
/*  67 */     paramBootstrapContext.register(HOT_FLOOR, new DamageType("hotFloor", 0.1F, DamageEffects.BURNING));
/*  68 */     paramBootstrapContext.register(IN_WALL, new DamageType("inWall", 0.0F));
/*  69 */     paramBootstrapContext.register(CRAMMING, new DamageType("cramming", 0.0F));
/*  70 */     paramBootstrapContext.register(DROWN, new DamageType("drown", 0.0F, DamageEffects.DROWNING));
/*  71 */     paramBootstrapContext.register(STARVE, new DamageType("starve", 0.0F));
/*  72 */     paramBootstrapContext.register(CACTUS, new DamageType("cactus", 0.1F));
/*  73 */     paramBootstrapContext.register(FALL, new DamageType("fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F, DamageEffects.HURT, DeathMessageType.FALL_VARIANTS));
/*  74 */     paramBootstrapContext.register(ENDER_PEARL, new DamageType("fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F, DamageEffects.HURT, DeathMessageType.FALL_VARIANTS));
/*  75 */     paramBootstrapContext.register(FLY_INTO_WALL, new DamageType("flyIntoWall", 0.0F));
/*  76 */     paramBootstrapContext.register(FELL_OUT_OF_WORLD, new DamageType("outOfWorld", 0.0F));
/*  77 */     paramBootstrapContext.register(GENERIC, new DamageType("generic", 0.0F));
/*  78 */     paramBootstrapContext.register(MAGIC, new DamageType("magic", 0.0F));
/*  79 */     paramBootstrapContext.register(WITHER, new DamageType("wither", 0.0F));
/*  80 */     paramBootstrapContext.register(DRAGON_BREATH, new DamageType("dragonBreath", 0.0F));
/*  81 */     paramBootstrapContext.register(DRY_OUT, new DamageType("dryout", 0.1F));
/*  82 */     paramBootstrapContext.register(SWEET_BERRY_BUSH, new DamageType("sweetBerryBush", 0.1F, DamageEffects.POKING));
/*  83 */     paramBootstrapContext.register(FREEZE, new DamageType("freeze", 0.0F, DamageEffects.FREEZING));
/*  84 */     paramBootstrapContext.register(STALAGMITE, new DamageType("stalagmite", 0.0F));
/*  85 */     paramBootstrapContext.register(FALLING_BLOCK, new DamageType("fallingBlock", 0.1F));
/*  86 */     paramBootstrapContext.register(FALLING_ANVIL, new DamageType("anvil", 0.1F));
/*  87 */     paramBootstrapContext.register(FALLING_STALACTITE, new DamageType("fallingStalactite", 0.1F));
/*  88 */     paramBootstrapContext.register(STING, new DamageType("sting", 0.1F));
/*  89 */     paramBootstrapContext.register(MOB_ATTACK, new DamageType("mob", 0.1F));
/*  90 */     paramBootstrapContext.register(MOB_ATTACK_NO_AGGRO, new DamageType("mob", 0.1F));
/*  91 */     paramBootstrapContext.register(PLAYER_ATTACK, new DamageType("player", 0.1F));
/*  92 */     paramBootstrapContext.register(SPEAR, new DamageType("spear", 0.1F));
/*  93 */     paramBootstrapContext.register(ARROW, new DamageType("arrow", 0.1F));
/*  94 */     paramBootstrapContext.register(TRIDENT, new DamageType("trident", 0.1F));
/*  95 */     paramBootstrapContext.register(MOB_PROJECTILE, new DamageType("mob", 0.1F));
/*  96 */     paramBootstrapContext.register(SPIT, new DamageType("mob", 0.1F));
/*  97 */     paramBootstrapContext.register(FIREWORKS, new DamageType("fireworks", 0.1F));
/*  98 */     paramBootstrapContext.register(UNATTRIBUTED_FIREBALL, new DamageType("onFire", 0.1F, DamageEffects.BURNING));
/*  99 */     paramBootstrapContext.register(FIREBALL, new DamageType("fireball", 0.1F, DamageEffects.BURNING));
/* 100 */     paramBootstrapContext.register(WITHER_SKULL, new DamageType("witherSkull", 0.1F));
/* 101 */     paramBootstrapContext.register(THROWN, new DamageType("thrown", 0.1F));
/* 102 */     paramBootstrapContext.register(INDIRECT_MAGIC, new DamageType("indirectMagic", 0.0F));
/* 103 */     paramBootstrapContext.register(THORNS, new DamageType("thorns", 0.1F, DamageEffects.THORNS));
/* 104 */     paramBootstrapContext.register(EXPLOSION, new DamageType("explosion", DamageScaling.ALWAYS, 0.1F));
/* 105 */     paramBootstrapContext.register(PLAYER_EXPLOSION, new DamageType("explosion.player", DamageScaling.ALWAYS, 0.1F));
/* 106 */     paramBootstrapContext.register(SONIC_BOOM, new DamageType("sonic_boom", DamageScaling.ALWAYS, 0.0F));
/* 107 */     paramBootstrapContext.register(BAD_RESPAWN_POINT, new DamageType("badRespawnPoint", DamageScaling.ALWAYS, 0.1F, DamageEffects.HURT, DeathMessageType.INTENTIONAL_GAME_DESIGN));
/* 108 */     paramBootstrapContext.register(OUTSIDE_BORDER, new DamageType("outsideBorder", 0.0F));
/* 109 */     paramBootstrapContext.register(GENERIC_KILL, new DamageType("genericKill", 0.0F));
/* 110 */     paramBootstrapContext.register(WIND_CHARGE, new DamageType("mob", 0.1F));
/* 111 */     paramBootstrapContext.register(MACE_SMASH, new DamageType("mace_smash", 0.1F));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\damagesource\DamageTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */