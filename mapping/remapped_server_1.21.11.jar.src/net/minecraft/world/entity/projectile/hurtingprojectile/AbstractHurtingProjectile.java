/*     */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*     */ 
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractHurtingProjectile
/*     */   extends Projectile {
/*     */   public static final double INITAL_ACCELERATION_POWER = 0.1D;
/*     */   public static final double DEFLECTION_SCALE = 0.5D;
/*  24 */   public double accelerationPower = 0.1D;
/*     */   
/*     */   protected AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> paramEntityType, Level paramLevel) {
/*  27 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   protected AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> paramEntityType, double paramDouble1, double paramDouble2, double paramDouble3, Level paramLevel) {
/*  31 */     this(paramEntityType, paramLevel);
/*  32 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */   
/*     */   public AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> paramEntityType, double paramDouble1, double paramDouble2, double paramDouble3, Vec3 paramVec3, Level paramLevel) {
/*  36 */     this(paramEntityType, paramLevel);
/*  37 */     snapTo(paramDouble1, paramDouble2, paramDouble3, getYRot(), getXRot());
/*  38 */     reapplyPosition();
/*  39 */     assignDirectionalMovement(paramVec3, this.accelerationPower);
/*     */   }
/*     */   
/*     */   public AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> paramEntityType, LivingEntity paramLivingEntity, Vec3 paramVec3, Level paramLevel) {
/*  43 */     this(paramEntityType, paramLivingEntity.getX(), paramLivingEntity.getY(), paramLivingEntity.getZ(), paramVec3, paramLevel);
/*  44 */     setOwner((Entity)paramLivingEntity);
/*  45 */     setRot(paramLivingEntity.getYRot(), paramLivingEntity.getXRot());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {}
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/*  54 */     double d = getBoundingBox().getSize() * 4.0D;
/*  55 */     if (Double.isNaN(d)) {
/*  56 */       d = 4.0D;
/*     */     }
/*  58 */     d *= 64.0D;
/*  59 */     return (paramDouble < d * d);
/*     */   }
/*     */   
/*     */   protected ClipContext.Block getClipType() {
/*  63 */     return ClipContext.Block.COLLIDER;
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     Vec3 vec3;
/*  68 */     Entity entity = getOwner();
/*     */     
/*  70 */     applyInertia();
/*     */     
/*  72 */     if (!level().isClientSide() && ((entity != null && entity.isRemoved()) || !level().hasChunkAt(blockPosition()))) {
/*  73 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/*  77 */     HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector((Entity)this, this::canHitEntity, getClipType());
/*     */ 
/*     */     
/*  80 */     if (hitResult.getType() != HitResult.Type.MISS) {
/*  81 */       vec3 = hitResult.getLocation();
/*     */     } else {
/*  83 */       vec3 = position().add(getDeltaMovement());
/*     */     } 
/*     */     
/*  86 */     ProjectileUtil.rotateTowardsMovement((Entity)this, 0.2F);
/*  87 */     setPos(vec3);
/*     */     
/*  89 */     applyEffectsFromBlocks();
/*  90 */     super.tick();
/*     */     
/*  92 */     if (shouldBurn()) {
/*  93 */       igniteForSeconds(1.0F);
/*     */     }
/*     */     
/*  96 */     if (hitResult.getType() != HitResult.Type.MISS && isAlive())
/*     */     {
/*     */       
/*  99 */       hitTargetOrDeflectSelf(hitResult);
/*     */     }
/*     */     
/* 102 */     createParticleTrail();
/*     */   }
/*     */   private void applyInertia() {
/*     */     float f;
/* 106 */     Vec3 vec31 = getDeltaMovement();
/* 107 */     Vec3 vec32 = position();
/*     */     
/* 109 */     if (isInWater()) {
/* 110 */       for (byte b = 0; b < 4; b++) {
/* 111 */         float f1 = 0.25F;
/* 112 */         level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, vec32.x - vec31.x * 0.25D, vec32.y - vec31.y * 0.25D, vec32.z - vec31.z * 0.25D, vec31.x, vec31.y, vec31.z);
/*     */       } 
/* 114 */       f = getLiquidInertia();
/*     */     } else {
/* 116 */       f = getInertia();
/*     */     } 
/*     */     
/* 119 */     setDeltaMovement(vec31.add(vec31.normalize().scale(this.accelerationPower)).scale(f));
/*     */   }
/*     */   
/*     */   private void createParticleTrail() {
/* 123 */     ParticleOptions particleOptions = getTrailParticle();
/* 124 */     Vec3 vec3 = position();
/* 125 */     if (particleOptions != null) {
/* 126 */       level().addParticle(particleOptions, vec3.x, vec3.y + 0.5D, vec3.z, 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHitEntity(Entity paramEntity) {
/* 137 */     return (super.canHitEntity(paramEntity) && !paramEntity.noPhysics);
/*     */   }
/*     */   
/*     */   protected boolean shouldBurn() {
/* 141 */     return true;
/*     */   }
/*     */   
/*     */   protected ParticleOptions getTrailParticle() {
/* 145 */     return (ParticleOptions)ParticleTypes.SMOKE;
/*     */   }
/*     */   
/*     */   protected float getInertia() {
/* 149 */     return 0.95F;
/*     */   }
/*     */   
/*     */   protected float getLiquidInertia() {
/* 153 */     return 0.8F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 158 */     super.addAdditionalSaveData(paramValueOutput);
/* 159 */     paramValueOutput.putDouble("acceleration_power", this.accelerationPower);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 164 */     super.readAdditionalSaveData(paramValueInput);
/* 165 */     this.accelerationPower = paramValueInput.getDoubleOr("acceleration_power", 0.1D);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLightLevelDependentMagicValue() {
/* 170 */     return 1.0F;
/*     */   }
/*     */   
/*     */   private void assignDirectionalMovement(Vec3 paramVec3, double paramDouble) {
/* 174 */     setDeltaMovement(paramVec3.normalize().scale(paramDouble));
/* 175 */     this.needsSync = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDeflection(boolean paramBoolean) {
/* 180 */     super.onDeflection(paramBoolean);
/* 181 */     if (paramBoolean) {
/* 182 */       this.accelerationPower = 0.1D;
/*     */     } else {
/* 184 */       this.accelerationPower *= 0.5D;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\AbstractHurtingProjectile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */