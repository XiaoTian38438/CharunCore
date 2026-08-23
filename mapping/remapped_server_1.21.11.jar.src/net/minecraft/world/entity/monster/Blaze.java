/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Blaze
/*     */   extends Monster {
/*  34 */   private float allowedHeightOffset = 0.5F;
/*     */   
/*     */   private int nextHeightOffsetChangeTick;
/*  37 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Blaze.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   public Blaze(EntityType<? extends Blaze> paramEntityType, Level paramLevel) {
/*  40 */     super((EntityType)paramEntityType, paramLevel);
/*     */     
/*  42 */     setPathfindingMalus(PathType.WATER, -1.0F);
/*  43 */     setPathfindingMalus(PathType.LAVA, 8.0F);
/*  44 */     setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
/*  45 */     setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
/*  46 */     this.xpReward = 10;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  51 */     this.goalSelector.addGoal(4, new BlazeAttackGoal(this));
/*  52 */     this.goalSelector.addGoal(5, (Goal)new MoveTowardsRestrictionGoal(this, 1.0D));
/*  53 */     this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
/*  54 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  55 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  57 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*  58 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  62 */     return Monster.createMonsterAttributes()
/*  63 */       .add(Attributes.ATTACK_DAMAGE, 6.0D)
/*  64 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
/*  65 */       .add(Attributes.FOLLOW_RANGE, 48.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  70 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  72 */     paramBuilder.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  77 */     return SoundEvents.BLAZE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  82 */     return SoundEvents.BLAZE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  87 */     return SoundEvents.BLAZE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLightLevelDependentMagicValue() {
/*  92 */     return 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  98 */     if (!onGround() && (getDeltaMovement()).y < 0.0D) {
/*  99 */       setDeltaMovement(getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
/*     */     }
/*     */     
/* 102 */     if (level().isClientSide()) {
/* 103 */       if (this.random.nextInt(24) == 0 && !isSilent()) {
/* 104 */         level().playLocalSound(getX() + 0.5D, getY() + 0.5D, getZ() + 0.5D, SoundEvents.BLAZE_BURN, getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
/*     */       }
/* 106 */       for (byte b = 0; b < 2; b++) {
/* 107 */         level().addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
/*     */       }
/*     */     } 
/*     */     
/* 111 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSensitiveToWater() {
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 121 */     this.nextHeightOffsetChangeTick--;
/* 122 */     if (this.nextHeightOffsetChangeTick <= 0) {
/* 123 */       this.nextHeightOffsetChangeTick = 100;
/* 124 */       this.allowedHeightOffset = (float)this.random.triangle(0.5D, 6.891D);
/*     */     } 
/*     */     
/* 127 */     LivingEntity livingEntity = getTarget();
/* 128 */     if (livingEntity != null && livingEntity.getEyeY() > getEyeY() + this.allowedHeightOffset && canAttack(livingEntity)) {
/* 129 */       Vec3 vec3 = getDeltaMovement();
/* 130 */       setDeltaMovement(getDeltaMovement().add(0.0D, (0.30000001192092896D - vec3.y) * 0.30000001192092896D, 0.0D));
/* 131 */       this.needsSync = true;
/*     */     } 
/*     */     
/* 134 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnFire() {
/* 139 */     return isCharged();
/*     */   }
/*     */   
/*     */   private boolean isCharged() {
/* 143 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   void setCharged(boolean paramBoolean) {
/* 147 */     byte b = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 148 */     if (paramBoolean) {
/* 149 */       b = (byte)(b | 0x1);
/*     */     } else {
/* 151 */       b = (byte)(b & 0xFFFFFFFE);
/*     */     } 
/* 153 */     this.entityData.set(DATA_FLAGS_ID, Byte.valueOf(b));
/*     */   }
/*     */   
/*     */   private static class BlazeAttackGoal extends Goal {
/*     */     private final Blaze blaze;
/*     */     private int attackStep;
/*     */     private int attackTime;
/*     */     private int lastSeen;
/*     */     
/*     */     public BlazeAttackGoal(Blaze param1Blaze) {
/* 163 */       this.blaze = param1Blaze;
/*     */       
/* 165 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 170 */       LivingEntity livingEntity = this.blaze.getTarget();
/* 171 */       return (livingEntity != null && livingEntity.isAlive() && this.blaze.canAttack(livingEntity));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 176 */       this.attackStep = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 181 */       this.blaze.setCharged(false);
/* 182 */       this.lastSeen = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 187 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 192 */       this.attackTime--;
/*     */       
/* 194 */       LivingEntity livingEntity = this.blaze.getTarget();
/*     */       
/* 196 */       if (livingEntity == null) {
/*     */         return;
/*     */       }
/*     */       
/* 200 */       boolean bool = this.blaze.getSensing().hasLineOfSight((Entity)livingEntity);
/*     */       
/* 202 */       if (bool) {
/* 203 */         this.lastSeen = 0;
/*     */       } else {
/* 205 */         this.lastSeen++;
/*     */       } 
/*     */       
/* 208 */       double d = this.blaze.distanceToSqr((Entity)livingEntity);
/*     */       
/* 210 */       if (d < 4.0D) {
/* 211 */         if (!bool) {
/*     */           return;
/*     */         }
/*     */         
/* 215 */         if (this.attackTime <= 0) {
/* 216 */           this.attackTime = 20;
/* 217 */           this.blaze.doHurtTarget(getServerLevel((Entity)this.blaze), (Entity)livingEntity);
/*     */         } 
/* 219 */         this.blaze.getMoveControl().setWantedPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0D);
/* 220 */       } else if (d < getFollowDistance() * getFollowDistance() && bool) {
/* 221 */         double d1 = livingEntity.getX() - this.blaze.getX();
/* 222 */         double d2 = livingEntity.getY(0.5D) - this.blaze.getY(0.5D);
/* 223 */         double d3 = livingEntity.getZ() - this.blaze.getZ();
/*     */         
/* 225 */         if (this.attackTime <= 0) {
/* 226 */           this.attackStep++;
/* 227 */           if (this.attackStep == 1) {
/* 228 */             this.attackTime = 60;
/* 229 */             this.blaze.setCharged(true);
/* 230 */           } else if (this.attackStep <= 4) {
/* 231 */             this.attackTime = 6;
/*     */           } else {
/* 233 */             this.attackTime = 100;
/* 234 */             this.attackStep = 0;
/* 235 */             this.blaze.setCharged(false);
/*     */           } 
/*     */           
/* 238 */           if (this.attackStep > 1) {
/* 239 */             double d4 = Math.sqrt(Math.sqrt(d)) * 0.5D;
/*     */             
/* 241 */             if (!this.blaze.isSilent()) {
/* 242 */               this.blaze.level().levelEvent(null, 1018, this.blaze.blockPosition(), 0);
/*     */             }
/* 244 */             for (byte b = 0; b < 1; b++) {
/* 245 */               Vec3 vec3 = new Vec3(this.blaze.getRandom().triangle(d1, 2.297D * d4), d2, this.blaze.getRandom().triangle(d3, 2.297D * d4));
/* 246 */               SmallFireball smallFireball = new SmallFireball(this.blaze.level(), (LivingEntity)this.blaze, vec3.normalize());
/* 247 */               smallFireball.setPos(smallFireball.getX(), this.blaze.getY(0.5D) + 0.5D, smallFireball.getZ());
/* 248 */               this.blaze.level().addFreshEntity((Entity)smallFireball);
/*     */             } 
/*     */           } 
/*     */         } 
/* 252 */         this.blaze.getLookControl().setLookAt((Entity)livingEntity, 10.0F, 10.0F);
/*     */       }
/* 254 */       else if (this.lastSeen < 5) {
/* 255 */         this.blaze.getMoveControl().setWantedPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0D);
/*     */       } 
/*     */ 
/*     */       
/* 259 */       super.tick();
/*     */     }
/*     */     
/*     */     private double getFollowDistance() {
/* 263 */       return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Blaze.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */