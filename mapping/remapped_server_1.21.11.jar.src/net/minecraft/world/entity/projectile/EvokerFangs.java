/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ public class EvokerFangs
/*     */   extends Entity
/*     */   implements TraceableEntity
/*     */ {
/*     */   public static final int ATTACK_DURATION = 20;
/*     */   public static final int LIFE_OFFSET = 2;
/*     */   public static final int ATTACK_TRIGGER_TICKS = 14;
/*     */   private static final int DEFAULT_WARMUP_DELAY = 0;
/*  30 */   private int warmupDelayTicks = 0;
/*     */   private boolean sentSpikeEvent;
/*  32 */   private int lifeTicks = 22;
/*     */   
/*     */   private boolean clientSideAttackStarted;
/*     */   private EntityReference<LivingEntity> owner;
/*     */   
/*     */   public EvokerFangs(EntityType<? extends EvokerFangs> paramEntityType, Level paramLevel) {
/*  38 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public EvokerFangs(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat, int paramInt, LivingEntity paramLivingEntity) {
/*  42 */     this(EntityType.EVOKER_FANGS, paramLevel);
/*  43 */     this.warmupDelayTicks = paramInt;
/*  44 */     setOwner(paramLivingEntity);
/*  45 */     setYRot(paramFloat * 57.295776F);
/*  46 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {}
/*     */ 
/*     */   
/*     */   public void setOwner(LivingEntity paramLivingEntity) {
/*  54 */     this.owner = EntityReference.of((UniquelyIdentifyable)paramLivingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity getOwner() {
/*  59 */     return EntityReference.getLivingEntity(this.owner, level());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  64 */     this.warmupDelayTicks = paramValueInput.getIntOr("Warmup", 0);
/*  65 */     this.owner = EntityReference.read(paramValueInput, "Owner");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  70 */     paramValueOutput.putInt("Warmup", this.warmupDelayTicks);
/*  71 */     EntityReference.store(this.owner, paramValueOutput, "Owner");
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  76 */     super.tick();
/*     */     
/*  78 */     if (level().isClientSide()) {
/*  79 */       if (this.clientSideAttackStarted) {
/*  80 */         this.lifeTicks--;
/*  81 */         if (this.lifeTicks == 14) {
/*  82 */           for (byte b = 0; b < 12; b++) {
/*  83 */             double d1 = getX() + (this.random.nextDouble() * 2.0D - 1.0D) * getBbWidth() * 0.5D;
/*  84 */             double d2 = getY() + 0.05D + this.random.nextDouble();
/*  85 */             double d3 = getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * getBbWidth() * 0.5D;
/*  86 */             double d4 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
/*  87 */             double d5 = 0.3D + this.random.nextDouble() * 0.3D;
/*  88 */             double d6 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
/*  89 */             level().addParticle((ParticleOptions)ParticleTypes.CRIT, d1, d2 + 1.0D, d3, d4, d5, d6);
/*     */           }
/*     */         
/*     */         }
/*     */       } 
/*  94 */     } else if (--this.warmupDelayTicks < 0) {
/*  95 */       if (this.warmupDelayTicks == -8) {
/*     */         
/*  97 */         List list = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0.2D, 0.0D, 0.2D));
/*  98 */         for (LivingEntity livingEntity : list) {
/*  99 */           dealDamageTo(livingEntity);
/*     */         }
/*     */       } 
/* 102 */       if (!this.sentSpikeEvent) {
/* 103 */         level().broadcastEntityEvent(this, (byte)4);
/* 104 */         this.sentSpikeEvent = true;
/*     */       } 
/* 106 */       if (--this.lifeTicks < 0) {
/* 107 */         discard();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void dealDamageTo(LivingEntity paramLivingEntity) {
/* 114 */     LivingEntity livingEntity = getOwner();
/* 115 */     if (!paramLivingEntity.isAlive() || paramLivingEntity.isInvulnerable() || paramLivingEntity == livingEntity) {
/*     */       return;
/*     */     }
/* 118 */     if (livingEntity == null) {
/* 119 */       paramLivingEntity.hurt(damageSources().magic(), 6.0F);
/*     */     } else {
/* 121 */       if (livingEntity.isAlliedTo((Entity)paramLivingEntity)) {
/*     */         return;
/*     */       }
/* 124 */       DamageSource damageSource = damageSources().indirectMagic(this, (Entity)livingEntity);
/* 125 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (paramLivingEntity.hurtServer(serverLevel, damageSource, 6.0F)) {
/* 126 */           EnchantmentHelper.doPostAttackEffects(serverLevel, (Entity)paramLivingEntity, damageSource);
/*     */         } }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 133 */     super.handleEntityEvent(paramByte);
/*     */     
/* 135 */     if (paramByte == 4) {
/* 136 */       this.clientSideAttackStarted = true;
/* 137 */       if (!isSilent()) {
/* 138 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.EVOKER_FANGS_ATTACK, getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getAnimationProgress(float paramFloat) {
/* 144 */     if (!this.clientSideAttackStarted) {
/* 145 */       return 0.0F;
/*     */     }
/* 147 */     int i = this.lifeTicks - 2;
/* 148 */     if (i <= 0) {
/* 149 */       return 1.0F;
/*     */     }
/* 151 */     return 1.0F - (i - paramFloat) / 20.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 156 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\EvokerFangs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */