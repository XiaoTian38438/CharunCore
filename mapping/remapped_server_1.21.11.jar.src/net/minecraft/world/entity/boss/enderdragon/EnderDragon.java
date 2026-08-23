/*     */ package net.minecraft.world.entity.boss.enderdragon;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.pathfinder.BinaryHeap;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EnderDragon
/*     */   extends Mob
/*     */   implements Enemy
/*     */ {
/*  60 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  62 */   public static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(EnderDragon.class, EntityDataSerializers.INT);
/*     */   
/*  64 */   private static final TargetingConditions CRYSTAL_DESTROY_TARGETING = TargetingConditions.forCombat().range(64.0D);
/*     */   
/*     */   private static final int GROWL_INTERVAL_MIN = 200;
/*     */   
/*     */   private static final int GROWL_INTERVAL_MAX = 400;
/*     */   private static final float SITTING_ALLOWED_DAMAGE_PERCENTAGE = 0.25F;
/*     */   private static final String DRAGON_DEATH_TIME_KEY = "DragonDeathTime";
/*     */   private static final String DRAGON_PHASE_KEY = "DragonPhase";
/*     */   private static final int DEFAULT_DEATH_TIME = 0;
/*  73 */   public final DragonFlightHistory flightHistory = new DragonFlightHistory();
/*     */   
/*     */   private final EnderDragonPart[] subEntities;
/*     */   
/*     */   public final EnderDragonPart head;
/*     */   private final EnderDragonPart neck;
/*     */   private final EnderDragonPart body;
/*     */   private final EnderDragonPart tail1;
/*     */   private final EnderDragonPart tail2;
/*     */   private final EnderDragonPart tail3;
/*     */   private final EnderDragonPart wing1;
/*     */   private final EnderDragonPart wing2;
/*     */   public float oFlapTime;
/*     */   public float flapTime;
/*     */   public boolean inWall;
/*  88 */   public int dragonDeathTime = 0;
/*     */   
/*     */   public float yRotA;
/*     */   
/*     */   public EndCrystal nearestCrystal;
/*     */   private EndDragonFight dragonFight;
/*  94 */   private BlockPos fightOrigin = BlockPos.ZERO;
/*     */   private final EnderDragonPhaseManager phaseManager;
/*  96 */   private int growlTime = 100;
/*     */   private float sittingDamageReceived;
/*  98 */   private final Node[] nodes = new Node[24];
/*  99 */   private final int[] nodeAdjacency = new int[24];
/* 100 */   private final BinaryHeap openSet = new BinaryHeap();
/*     */   
/*     */   public EnderDragon(EntityType<? extends EnderDragon> paramEntityType, Level paramLevel) {
/* 103 */     super(EntityType.ENDER_DRAGON, paramLevel);
/*     */     
/* 105 */     this.head = new EnderDragonPart(this, "head", 1.0F, 1.0F);
/* 106 */     this.neck = new EnderDragonPart(this, "neck", 3.0F, 3.0F);
/* 107 */     this.body = new EnderDragonPart(this, "body", 5.0F, 3.0F);
/* 108 */     this.tail1 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 109 */     this.tail2 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 110 */     this.tail3 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 111 */     this.wing1 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
/* 112 */     this.wing2 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
/*     */     
/* 114 */     this.subEntities = new EnderDragonPart[] { this.head, this.neck, this.body, this.tail1, this.tail2, this.tail3, this.wing1, this.wing2 };
/*     */     
/* 116 */     setHealth(getMaxHealth());
/*     */     
/* 118 */     this.noPhysics = true;
/*     */     
/* 120 */     this.phaseManager = new EnderDragonPhaseManager(this);
/*     */   }
/*     */   
/*     */   public void setDragonFight(EndDragonFight paramEndDragonFight) {
/* 124 */     this.dragonFight = paramEndDragonFight;
/*     */   }
/*     */   
/*     */   public void setFightOrigin(BlockPos paramBlockPos) {
/* 128 */     this.fightOrigin = paramBlockPos;
/*     */   }
/*     */   
/*     */   public BlockPos getFightOrigin() {
/* 132 */     return this.fightOrigin;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 136 */     return Mob.createMobAttributes()
/* 137 */       .add(Attributes.MAX_HEALTH, 200.0D)
/* 138 */       .add(Attributes.CAMERA_DISTANCE, 16.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlapping() {
/* 143 */     float f1 = Mth.cos((this.flapTime * 6.2831855F));
/* 144 */     float f2 = Mth.cos((this.oFlapTime * 6.2831855F));
/*     */     
/* 146 */     return (f2 <= -0.3F && f1 >= -0.3F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onFlap() {
/* 151 */     if (level().isClientSide() && !isSilent()) {
/* 152 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_FLAP, getSoundSource(), 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 158 */     super.defineSynchedData(paramBuilder);
/* 159 */     paramBuilder.define(DATA_PHASE, Integer.valueOf(EnderDragonPhase.HOVERING.getId()));
/*     */   }
/*     */   
/*     */   public void aiStep() {
/*     */     ServerLevel serverLevel;
/* 164 */     processFlappingMovement();
/*     */     
/* 166 */     if (level().isClientSide()) {
/* 167 */       setHealth(getHealth());
/*     */       
/* 169 */       if (!isSilent() && 
/* 170 */         !this.phaseManager.getCurrentPhase().isSitting() && --this.growlTime < 0) {
/* 171 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_GROWL, getSoundSource(), 2.5F, 0.8F + this.random.nextFloat() * 0.3F, false);
/* 172 */         this.growlTime = 200 + this.random.nextInt(200);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 177 */     if (this.dragonFight == null) { Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel1 = (ServerLevel)level1;
/* 178 */         EndDragonFight endDragonFight = serverLevel1.getDragonFight();
/* 179 */         if (endDragonFight != null && getUUID().equals(endDragonFight.getDragonUUID())) {
/* 180 */           this.dragonFight = endDragonFight;
/*     */         } }
/*     */        }
/*     */     
/* 184 */     this.oFlapTime = this.flapTime;
/*     */     
/* 186 */     if (isDeadOrDying()) {
/* 187 */       float f1 = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 188 */       float f2 = (this.random.nextFloat() - 0.5F) * 4.0F;
/* 189 */       float f3 = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 190 */       level().addParticle((ParticleOptions)ParticleTypes.EXPLOSION, getX() + f1, getY() + 2.0D + f2, getZ() + f3, 0.0D, 0.0D, 0.0D);
/*     */       
/*     */       return;
/*     */     } 
/* 194 */     checkCrystals();
/*     */     
/* 196 */     Vec3 vec31 = getDeltaMovement();
/* 197 */     float f = 0.2F / ((float)vec31.horizontalDistance() * 10.0F + 1.0F);
/* 198 */     f *= (float)Math.pow(2.0D, vec31.y);
/* 199 */     if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 200 */       this.flapTime += 0.1F;
/* 201 */     } else if (this.inWall) {
/* 202 */       this.flapTime += f * 0.5F;
/*     */     } else {
/* 204 */       this.flapTime += f;
/*     */     } 
/*     */     
/* 207 */     setYRot(Mth.wrapDegrees(getYRot()));
/*     */     
/* 209 */     if (isNoAi()) {
/* 210 */       this.flapTime = 0.5F;
/*     */       
/*     */       return;
/*     */     } 
/* 214 */     this.flightHistory.record(getY(), getYRot());
/*     */     
/* 216 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 217 */     else { this.interpolation.interpolate();
/*     */       
/* 219 */       this.phaseManager.getCurrentPhase().doClientTick();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 272 */       if (!level().isClientSide())
/* 273 */         applyEffectsFromBlocks();  }  DragonPhaseInstance dragonPhaseInstance = this.phaseManager.getCurrentPhase(); dragonPhaseInstance.doServerTick(serverLevel); if (this.phaseManager.getCurrentPhase() != dragonPhaseInstance) { dragonPhaseInstance = this.phaseManager.getCurrentPhase(); dragonPhaseInstance.doServerTick(serverLevel); }  Vec3 vec32 = dragonPhaseInstance.getFlyTargetLocation(); if (vec32 != null) { double d1 = vec32.x - getX(); double d2 = vec32.y - getY(); double d3 = vec32.z - getZ(); double d4 = d1 * d1 + d2 * d2 + d3 * d3; float f1 = dragonPhaseInstance.getFlySpeed(); double d5 = Math.sqrt(d1 * d1 + d3 * d3); if (d5 > 0.0D) d2 = Mth.clamp(d2 / d5, -f1, f1);  setDeltaMovement(getDeltaMovement().add(0.0D, d2 * 0.01D, 0.0D)); setYRot(Mth.wrapDegrees(getYRot())); Vec3 vec33 = vec32.subtract(getX(), getY(), getZ()).normalize(); Vec3 vec34 = (new Vec3(Mth.sin((getYRot() * 0.017453292F)), (getDeltaMovement()).y, -Mth.cos((getYRot() * 0.017453292F)))).normalize(); float f2 = Math.max(((float)vec34.dot(vec33) + 0.5F) / 1.5F, 0.0F); if (Math.abs(d1) > 9.999999747378752E-6D || Math.abs(d3) > 9.999999747378752E-6D) { float f5 = Mth.clamp(Mth.wrapDegrees(180.0F - (float)Mth.atan2(d1, d3) * 57.295776F - getYRot()), -50.0F, 50.0F); this.yRotA *= 0.8F; this.yRotA += f5 * dragonPhaseInstance.getTurnSpeed(); setYRot(getYRot() + this.yRotA * 0.1F); }  float f3 = (float)(2.0D / (d4 + 1.0D)); float f4 = 0.06F; moveRelative(0.06F * (f2 * f3 + 1.0F - f3), new Vec3(0.0D, 0.0D, -1.0D)); if (this.inWall) { move(MoverType.SELF, getDeltaMovement().scale(0.800000011920929D)); } else { move(MoverType.SELF, getDeltaMovement()); }  Vec3 vec35 = getDeltaMovement().normalize(); double d6 = 0.8D + 0.15D * (vec35.dot(vec34) + 1.0D) / 2.0D; setDeltaMovement(getDeltaMovement().multiply(d6, 0.9100000262260437D, d6)); }  if (!level().isClientSide()) applyEffectsFromBlocks();
/*     */   
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
/*     */   private void tickPart(EnderDragonPart paramEnderDragonPart, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 353 */     paramEnderDragonPart.setPos(getX() + paramDouble1, getY() + paramDouble2, getZ() + paramDouble3);
/*     */   }
/*     */   
/*     */   private float getHeadYOffset() {
/* 357 */     if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 358 */       return -1.0F;
/*     */     }
/* 360 */     DragonFlightHistory.Sample sample1 = this.flightHistory.get(5);
/* 361 */     DragonFlightHistory.Sample sample2 = this.flightHistory.get(0);
/* 362 */     return (float)(sample1.y() - sample2.y());
/*     */   }
/*     */   
/*     */   private void checkCrystals() {
/* 366 */     if (this.nearestCrystal != null) {
/* 367 */       if (this.nearestCrystal.isRemoved()) {
/* 368 */         this.nearestCrystal = null;
/* 369 */       } else if (this.tickCount % 10 == 0 && 
/* 370 */         getHealth() < getMaxHealth()) {
/* 371 */         setHealth(getHealth() + 1.0F);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 376 */     if (this.random.nextInt(10) == 0) {
/* 377 */       List list = level().getEntitiesOfClass(EndCrystal.class, getBoundingBox().inflate(32.0D));
/*     */       
/* 379 */       EndCrystal endCrystal = null;
/* 380 */       double d = Double.MAX_VALUE;
/* 381 */       for (EndCrystal endCrystal1 : list) {
/* 382 */         double d1 = endCrystal1.distanceToSqr((Entity)this);
/* 383 */         if (d1 < d) {
/* 384 */           d = d1;
/* 385 */           endCrystal = endCrystal1;
/*     */         } 
/*     */       } 
/*     */       
/* 389 */       this.nearestCrystal = endCrystal;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void knockBack(ServerLevel paramServerLevel, List<Entity> paramList) {
/* 394 */     double d1 = ((this.body.getBoundingBox()).minX + (this.body.getBoundingBox()).maxX) / 2.0D;
/* 395 */     double d2 = ((this.body.getBoundingBox()).minZ + (this.body.getBoundingBox()).maxZ) / 2.0D;
/*     */     
/* 397 */     for (Entity entity : paramList) {
/* 398 */       if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 399 */         double d3 = entity.getX() - d1;
/* 400 */         double d4 = entity.getZ() - d2;
/* 401 */         double d5 = Math.max(d3 * d3 + d4 * d4, 0.1D);
/* 402 */         entity.push(d3 / d5 * 4.0D, 0.20000000298023224D, d4 / d5 * 4.0D);
/* 403 */         if (!this.phaseManager.getCurrentPhase().isSitting() && livingEntity.getLastHurtByMobTimestamp() < entity.tickCount - 2) {
/* 404 */           DamageSource damageSource = damageSources().mobAttack((LivingEntity)this);
/* 405 */           entity.hurtServer(paramServerLevel, damageSource, 5.0F);
/* 406 */           EnchantmentHelper.doPostAttackEffects(paramServerLevel, entity, damageSource);
/*     */         }  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   private void hurt(ServerLevel paramServerLevel, List<Entity> paramList) {
/* 413 */     for (Entity entity : paramList) {
/* 414 */       if (entity instanceof LivingEntity) {
/* 415 */         DamageSource damageSource = damageSources().mobAttack((LivingEntity)this);
/* 416 */         entity.hurtServer(paramServerLevel, damageSource, 10.0F);
/* 417 */         EnchantmentHelper.doPostAttackEffects(paramServerLevel, entity, damageSource);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private float rotWrap(double paramDouble) {
/* 423 */     return (float)Mth.wrapDegrees(paramDouble);
/*     */   }
/*     */   
/*     */   private boolean checkWalls(ServerLevel paramServerLevel, AABB paramAABB) {
/* 427 */     int i = Mth.floor(paramAABB.minX);
/* 428 */     int j = Mth.floor(paramAABB.minY);
/* 429 */     int k = Mth.floor(paramAABB.minZ);
/* 430 */     int m = Mth.floor(paramAABB.maxX);
/* 431 */     int n = Mth.floor(paramAABB.maxY);
/* 432 */     int i1 = Mth.floor(paramAABB.maxZ);
/* 433 */     boolean bool1 = false;
/* 434 */     boolean bool2 = false;
/* 435 */     for (int i2 = i; i2 <= m; i2++) {
/* 436 */       for (int i3 = j; i3 <= n; i3++) {
/* 437 */         for (int i4 = k; i4 <= i1; i4++) {
/* 438 */           BlockPos blockPos = new BlockPos(i2, i3, i4);
/* 439 */           BlockState blockState = paramServerLevel.getBlockState(blockPos);
/* 440 */           if (!blockState.isAir() && !blockState.is(BlockTags.DRAGON_TRANSPARENT))
/*     */           {
/* 442 */             if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() || blockState.is(BlockTags.DRAGON_IMMUNE)) {
/* 443 */               bool1 = true;
/*     */             } else {
/* 445 */               bool2 = (paramServerLevel.removeBlock(blockPos, false) || bool2) ? true : false;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 451 */     if (bool2) {
/*     */ 
/*     */ 
/*     */       
/* 455 */       BlockPos blockPos = new BlockPos(i + this.random.nextInt(m - i + 1), j + this.random.nextInt(n - j + 1), k + this.random.nextInt(i1 - k + 1));
/*     */       
/* 457 */       paramServerLevel.levelEvent(2008, blockPos, 0);
/*     */     } 
/*     */     
/* 460 */     return bool1;
/*     */   }
/*     */   
/*     */   public boolean hurt(ServerLevel paramServerLevel, EnderDragonPart paramEnderDragonPart, DamageSource paramDamageSource, float paramFloat) {
/* 464 */     if (this.phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.DYING) {
/* 465 */       return false;
/*     */     }
/*     */     
/* 468 */     paramFloat = this.phaseManager.getCurrentPhase().onHurt(paramDamageSource, paramFloat);
/*     */     
/* 470 */     if (paramEnderDragonPart != this.head) {
/* 471 */       paramFloat = paramFloat / 4.0F + Math.min(paramFloat, 1.0F);
/*     */     }
/*     */     
/* 474 */     if (paramFloat < 0.01F) {
/* 475 */       return false;
/*     */     }
/*     */     
/* 478 */     if (paramDamageSource.getEntity() instanceof Player || paramDamageSource.is(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
/* 479 */       float f = getHealth();
/* 480 */       reallyHurt(paramServerLevel, paramDamageSource, paramFloat);
/*     */       
/* 482 */       if (isDeadOrDying() && !this.phaseManager.getCurrentPhase().isSitting()) {
/* 483 */         setHealth(1.0F);
/* 484 */         this.phaseManager.setPhase(EnderDragonPhase.DYING);
/*     */       } 
/*     */       
/* 487 */       if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 488 */         this.sittingDamageReceived = this.sittingDamageReceived + f - getHealth();
/*     */         
/* 490 */         if (this.sittingDamageReceived > 0.25F * getMaxHealth()) {
/* 491 */           this.sittingDamageReceived = 0.0F;
/* 492 */           this.phaseManager.setPhase(EnderDragonPhase.TAKEOFF);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 497 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 502 */     return hurt(paramServerLevel, this.body, paramDamageSource, paramFloat);
/*     */   }
/*     */   
/*     */   protected void reallyHurt(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 506 */     super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public void kill(ServerLevel paramServerLevel) {
/* 511 */     remove(Entity.RemovalReason.KILLED);
/* 512 */     gameEvent((Holder)GameEvent.ENTITY_DIE);
/*     */     
/* 514 */     if (this.dragonFight != null) {
/* 515 */       this.dragonFight.updateDragon(this);
/* 516 */       this.dragonFight.setDragonKilled(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tickDeath() {
/* 522 */     if (this.dragonFight != null) {
/* 523 */       this.dragonFight.updateDragon(this);
/*     */     }
/*     */     
/* 526 */     this.dragonDeathTime++;
/* 527 */     if (this.dragonDeathTime >= 180 && this.dragonDeathTime <= 200) {
/* 528 */       float f1 = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 529 */       float f2 = (this.random.nextFloat() - 0.5F) * 4.0F;
/* 530 */       float f3 = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 531 */       level().addParticle((ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, getX() + f1, getY() + 2.0D + f2, getZ() + f3, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */     
/* 534 */     char c = 'Ǵ';
/* 535 */     if (this.dragonFight != null && !this.dragonFight.hasPreviouslyKilledDragon()) {
/* 536 */       c = '⻠';
/*     */     }
/*     */     
/* 539 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 540 */       if (this.dragonDeathTime > 150 && this.dragonDeathTime % 5 == 0 && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_DROPS)).booleanValue()) {
/* 541 */         ExperienceOrb.award(serverLevel, position(), Mth.floor(c * 0.08F));
/*     */       }
/* 543 */       if (this.dragonDeathTime == 1 && !isSilent()) {
/* 544 */         serverLevel.globalLevelEvent(1028, blockPosition(), 0);
/*     */       } }
/*     */ 
/*     */     
/* 548 */     Vec3 vec3 = new Vec3(0.0D, 0.10000000149011612D, 0.0D);
/* 549 */     move(MoverType.SELF, vec3);
/* 550 */     for (EnderDragonPart enderDragonPart : this.subEntities) {
/* 551 */       enderDragonPart.setOldPosAndRot();
/* 552 */       enderDragonPart.setPos(enderDragonPart.position().add(vec3));
/*     */     } 
/*     */     
/* 555 */     if (this.dragonDeathTime == 200) { Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/* 556 */         if (((Boolean)serverLevel.getGameRules().get(GameRules.MOB_DROPS)).booleanValue()) {
/* 557 */           ExperienceOrb.award(serverLevel, position(), Mth.floor(c * 0.2F));
/*     */         }
/* 559 */         if (this.dragonFight != null) {
/* 560 */           this.dragonFight.setDragonKilled(this);
/*     */         }
/* 562 */         remove(Entity.RemovalReason.KILLED);
/* 563 */         gameEvent((Holder)GameEvent.ENTITY_DIE); }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   public int findClosestNode() {
/* 569 */     if (this.nodes[0] == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 575 */       for (byte b = 0; b < 24; b++) {
/* 576 */         int i, j; byte b1 = 5;
/* 577 */         byte b2 = b;
/*     */ 
/*     */ 
/*     */         
/* 581 */         if (b < 12) {
/* 582 */           i = Mth.floor(60.0F * Mth.cos((2.0F * (-3.1415927F + 0.2617994F * b2))));
/* 583 */           j = Mth.floor(60.0F * Mth.sin((2.0F * (-3.1415927F + 0.2617994F * b2))));
/* 584 */         } else if (b < 20) {
/* 585 */           b2 -= 12;
/* 586 */           i = Mth.floor(40.0F * Mth.cos((2.0F * (-3.1415927F + 0.3926991F * b2))));
/* 587 */           j = Mth.floor(40.0F * Mth.sin((2.0F * (-3.1415927F + 0.3926991F * b2))));
/* 588 */           b1 += 10;
/*     */         } else {
/* 590 */           b2 -= 20;
/* 591 */           i = Mth.floor(20.0F * Mth.cos((2.0F * (-3.1415927F + 0.7853982F * b2))));
/* 592 */           j = Mth.floor(20.0F * Mth.sin((2.0F * (-3.1415927F + 0.7853982F * b2))));
/*     */         } 
/*     */ 
/*     */         
/* 596 */         int k = Math.max(73, level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(i, 0, j)).getY() + b1);
/*     */         
/* 598 */         this.nodes[b] = new Node(i, k, j);
/*     */       } 
/*     */       
/* 601 */       this.nodeAdjacency[0] = 6146;
/* 602 */       this.nodeAdjacency[1] = 8197;
/* 603 */       this.nodeAdjacency[2] = 8202;
/* 604 */       this.nodeAdjacency[3] = 16404;
/* 605 */       this.nodeAdjacency[4] = 32808;
/* 606 */       this.nodeAdjacency[5] = 32848;
/* 607 */       this.nodeAdjacency[6] = 65696;
/* 608 */       this.nodeAdjacency[7] = 131392;
/* 609 */       this.nodeAdjacency[8] = 131712;
/* 610 */       this.nodeAdjacency[9] = 263424;
/* 611 */       this.nodeAdjacency[10] = 526848;
/* 612 */       this.nodeAdjacency[11] = 525313;
/*     */       
/* 614 */       this.nodeAdjacency[12] = 1581057;
/* 615 */       this.nodeAdjacency[13] = 3166214;
/* 616 */       this.nodeAdjacency[14] = 2138120;
/* 617 */       this.nodeAdjacency[15] = 6373424;
/* 618 */       this.nodeAdjacency[16] = 4358208;
/* 619 */       this.nodeAdjacency[17] = 12910976;
/* 620 */       this.nodeAdjacency[18] = 9044480;
/* 621 */       this.nodeAdjacency[19] = 9706496;
/*     */       
/* 623 */       this.nodeAdjacency[20] = 15216640;
/* 624 */       this.nodeAdjacency[21] = 13688832;
/* 625 */       this.nodeAdjacency[22] = 11763712;
/* 626 */       this.nodeAdjacency[23] = 8257536;
/*     */     } 
/*     */     
/* 629 */     return findClosestNode(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   public int findClosestNode(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 633 */     float f = 10000.0F;
/* 634 */     byte b1 = 0;
/* 635 */     Node node = new Node(Mth.floor(paramDouble1), Mth.floor(paramDouble2), Mth.floor(paramDouble3));
/* 636 */     byte b2 = 0;
/*     */     
/* 638 */     if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0)
/*     */     {
/* 640 */       b2 = 12;
/*     */     }
/*     */     
/* 643 */     for (byte b3 = b2; b3 < 24; b3++) {
/* 644 */       if (this.nodes[b3] != null) {
/* 645 */         float f1 = this.nodes[b3].distanceToSqr(node);
/* 646 */         if (f1 < f) {
/* 647 */           f = f1;
/* 648 */           b1 = b3;
/*     */         } 
/*     */       } 
/*     */     } 
/* 652 */     return b1;
/*     */   }
/*     */   
/*     */   public Path findPath(int paramInt1, int paramInt2, Node paramNode) {
/* 656 */     for (byte b1 = 0; b1 < 24; b1++) {
/* 657 */       Node node = this.nodes[b1];
/* 658 */       node.closed = false;
/* 659 */       node.f = 0.0F;
/* 660 */       node.g = 0.0F;
/* 661 */       node.h = 0.0F;
/* 662 */       node.cameFrom = null;
/* 663 */       node.heapIdx = -1;
/*     */     } 
/*     */     
/* 666 */     Node node1 = this.nodes[paramInt1];
/* 667 */     Node node2 = this.nodes[paramInt2];
/*     */     
/* 669 */     node1.g = 0.0F;
/* 670 */     node1.h = node1.distanceTo(node2);
/* 671 */     node1.f = node1.h;
/*     */     
/* 673 */     this.openSet.clear();
/* 674 */     this.openSet.insert(node1);
/*     */     
/* 676 */     Node node3 = node1;
/*     */     
/* 678 */     byte b2 = 0;
/* 679 */     if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0)
/*     */     {
/* 681 */       b2 = 12;
/*     */     }
/*     */     
/* 684 */     while (!this.openSet.isEmpty()) {
/* 685 */       Node node = this.openSet.pop();
/*     */       
/* 687 */       if (node.equals(node2)) {
/* 688 */         if (paramNode != null) {
/* 689 */           paramNode.cameFrom = node2;
/* 690 */           node2 = paramNode;
/*     */         } 
/* 692 */         return reconstructPath(node1, node2);
/*     */       } 
/*     */       
/* 695 */       if (node.distanceTo(node2) < node3.distanceTo(node2)) {
/* 696 */         node3 = node;
/*     */       }
/* 698 */       node.closed = true;
/*     */       
/* 700 */       byte b3 = 0; byte b4;
/* 701 */       for (b4 = 0; b4 < 24; b4++) {
/* 702 */         if (this.nodes[b4] == node) {
/* 703 */           b3 = b4;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 708 */       for (b4 = b2; b4 < 24; b4++) {
/* 709 */         if ((this.nodeAdjacency[b3] & 1 << b4) > 0) {
/* 710 */           Node node4 = this.nodes[b4];
/*     */           
/* 712 */           if (!node4.closed) {
/*     */ 
/*     */ 
/*     */             
/* 716 */             float f = node.g + node.distanceTo(node4);
/* 717 */             if (!node4.inOpenSet() || f < node4.g) {
/* 718 */               node4.cameFrom = node;
/* 719 */               node4.g = f;
/* 720 */               node4.h = node4.distanceTo(node2);
/* 721 */               if (node4.inOpenSet()) {
/* 722 */                 this.openSet.changeCost(node4, node4.g + node4.h);
/*     */               } else {
/* 724 */                 node4.f = node4.g + node4.h;
/* 725 */                 this.openSet.insert(node4);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 732 */     if (node3 == node1) {
/* 733 */       return null;
/*     */     }
/* 735 */     LOGGER.debug("Failed to find path from {} to {}", Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
/* 736 */     if (paramNode != null) {
/* 737 */       paramNode.cameFrom = node3;
/* 738 */       node3 = paramNode;
/*     */     } 
/* 740 */     return reconstructPath(node1, node3);
/*     */   }
/*     */   
/*     */   private Path reconstructPath(Node paramNode1, Node paramNode2) {
/* 744 */     ArrayList<Node> arrayList = Lists.newArrayList();
/* 745 */     Node node = paramNode2;
/* 746 */     arrayList.add(0, node);
/* 747 */     while (node.cameFrom != null) {
/* 748 */       node = node.cameFrom;
/* 749 */       arrayList.add(0, node);
/*     */     } 
/* 751 */     return new Path(arrayList, new BlockPos(paramNode2.x, paramNode2.y, paramNode2.z), true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 756 */     super.addAdditionalSaveData(paramValueOutput);
/* 757 */     paramValueOutput.putInt("DragonPhase", this.phaseManager.getCurrentPhase().getPhase().getId());
/* 758 */     paramValueOutput.putInt("DragonDeathTime", this.dragonDeathTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 763 */     super.readAdditionalSaveData(paramValueInput);
/* 764 */     paramValueInput.getInt("DragonPhase").ifPresent(paramInteger -> this.phaseManager.setPhase(EnderDragonPhase.getById(paramInteger.intValue())));
/*     */ 
/*     */     
/* 767 */     this.dragonDeathTime = paramValueInput.getIntOr("DragonDeathTime", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkDespawn() {}
/*     */ 
/*     */   
/*     */   public EnderDragonPart[] getSubEntities() {
/* 775 */     return this.subEntities;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 780 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 785 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 790 */     return SoundEvents.ENDER_DRAGON_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 795 */     return SoundEvents.ENDER_DRAGON_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 800 */     return 5.0F;
/*     */   }
/*     */   public Vec3 getHeadLookVector(float paramFloat) {
/*     */     Vec3 vec3;
/* 804 */     DragonPhaseInstance dragonPhaseInstance = this.phaseManager.getCurrentPhase();
/* 805 */     EnderDragonPhase enderDragonPhase = dragonPhaseInstance.getPhase();
/*     */ 
/*     */     
/* 808 */     if (enderDragonPhase == EnderDragonPhase.LANDING || enderDragonPhase == EnderDragonPhase.TAKEOFF) {
/* 809 */       BlockPos blockPos = level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.fightOrigin));
/* 810 */       float f1 = Math.max((float)Math.sqrt(blockPos.distToCenterSqr((Position)position())) / 4.0F, 1.0F);
/* 811 */       float f2 = 6.0F / f1;
/*     */       
/* 813 */       float f3 = getXRot();
/* 814 */       float f4 = 1.5F;
/* 815 */       setXRot(-f2 * 1.5F * 5.0F);
/*     */       
/* 817 */       vec3 = getViewVector(paramFloat);
/* 818 */       setXRot(f3);
/* 819 */     } else if (dragonPhaseInstance.isSitting()) {
/* 820 */       float f1 = getXRot();
/* 821 */       float f2 = 1.5F;
/* 822 */       setXRot(-45.0F);
/*     */       
/* 824 */       vec3 = getViewVector(paramFloat);
/* 825 */       setXRot(f1);
/*     */     } else {
/* 827 */       vec3 = getViewVector(paramFloat);
/*     */     } 
/*     */     
/* 830 */     return vec3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrystalDestroyed(ServerLevel paramServerLevel, EndCrystal paramEndCrystal, BlockPos paramBlockPos, DamageSource paramDamageSource) {
/*     */     Player player;
/* 836 */     Entity entity = paramDamageSource.getEntity(); if (entity instanceof Player) { Player player1 = (Player)entity;
/* 837 */       player = player1; }
/*     */     else
/* 839 */     { player = paramServerLevel.getNearestPlayer(CRYSTAL_DESTROY_TARGETING, paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ()); }
/*     */ 
/*     */     
/* 842 */     if (paramEndCrystal == this.nearestCrystal) {
/* 843 */       hurt(paramServerLevel, this.head, damageSources().explosion(paramEndCrystal, (Entity)player), 10.0F);
/*     */     }
/*     */     
/* 846 */     this.phaseManager.getCurrentPhase().onCrystalDestroyed(paramEndCrystal, paramBlockPos, paramDamageSource, player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 851 */     if (DATA_PHASE.equals(paramEntityDataAccessor) && level().isClientSide()) {
/* 852 */       this.phaseManager.setPhase(EnderDragonPhase.getById(((Integer)getEntityData().get(DATA_PHASE)).intValue()));
/*     */     }
/*     */     
/* 855 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*     */   }
/*     */   
/*     */   public EnderDragonPhaseManager getPhaseManager() {
/* 859 */     return this.phaseManager;
/*     */   }
/*     */   
/*     */   public EndDragonFight getDragonFight() {
/* 863 */     return this.dragonFight;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addEffect(MobEffectInstance paramMobEffectInstance, Entity paramEntity) {
/* 868 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRide(Entity paramEntity) {
/* 873 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUsePortal(boolean paramBoolean) {
/* 878 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket paramClientboundAddEntityPacket) {
/* 883 */     super.recreateFromPacket(paramClientboundAddEntityPacket);
/* 884 */     EnderDragonPart[] arrayOfEnderDragonPart = getSubEntities();
/* 885 */     for (byte b = 0; b < arrayOfEnderDragonPart.length; b++) {
/* 886 */       arrayOfEnderDragonPart[b].setId(b + paramClientboundAddEntityPacket.getId() + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canAttack(LivingEntity paramLivingEntity) {
/* 893 */     return paramLivingEntity.canBeSeenAsEnemy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected float sanitizeScale(float paramFloat) {
/* 899 */     return 1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\EnderDragon.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */