/*     */ package net.minecraft.world.entity.monster.breeze;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.debug.DebugBreezeInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Breeze
/*     */   extends Monster
/*     */ {
/*     */   private static final int SLIDE_PARTICLES_AMOUNT = 20;
/*     */   private static final int IDLE_PARTICLES_AMOUNT = 1;
/*     */   private static final int JUMP_DUST_PARTICLES_AMOUNT = 20;
/*     */   private static final int JUMP_TRAIL_PARTICLES_AMOUNT = 3;
/*     */   private static final int JUMP_TRAIL_DURATION_TICKS = 5;
/*     */   private static final int JUMP_CIRCLE_DISTANCE_Y = 10;
/*     */   private static final float FALL_DISTANCE_SOUND_TRIGGER_THRESHOLD = 3.0F;
/*     */   private static final int WHIRL_SOUND_FREQUENCY_MIN = 1;
/*     */   private static final int WHIRL_SOUND_FREQUENCY_MAX = 80;
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  54 */     return Mob.createMobAttributes()
/*  55 */       .add(Attributes.MOVEMENT_SPEED, 0.6299999952316284D)
/*  56 */       .add(Attributes.MAX_HEALTH, 30.0D)
/*  57 */       .add(Attributes.FOLLOW_RANGE, 24.0D)
/*  58 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */ 
/*     */   
/*  62 */   public AnimationState idle = new AnimationState();
/*  63 */   public AnimationState slide = new AnimationState();
/*  64 */   public AnimationState slideBack = new AnimationState();
/*  65 */   public AnimationState longJump = new AnimationState();
/*  66 */   public AnimationState shoot = new AnimationState();
/*  67 */   public AnimationState inhale = new AnimationState();
/*     */   
/*  69 */   private int jumpTrailStartedTick = 0;
/*  70 */   private int soundTick = 0; private static final ProjectileDeflection PROJECTILE_DEFLECTION;
/*     */   static {
/*  72 */     PROJECTILE_DEFLECTION = ((paramProjectile, paramEntity, paramRandomSource) -> {
/*     */         paramEntity.level().playSound(null, paramEntity, SoundEvents.BREEZE_DEFLECT, paramEntity.getSoundSource(), 1.0F, 1.0F);
/*     */         ProjectileDeflection.REVERSE.deflect(paramProjectile, paramEntity, paramRandomSource);
/*     */       });
/*     */   }
/*     */   public Breeze(EntityType<? extends Monster> paramEntityType, Level paramLevel) {
/*  78 */     super(paramEntityType, paramLevel);
/*  79 */     setPathfindingMalus(PathType.DANGER_TRAPDOOR, -1.0F);
/*  80 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/*  81 */     this.xpReward = 10;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> paramDynamic) {
/*  86 */     return BreezeAi.makeBrain(this, brainProvider().makeBrain(paramDynamic));
/*     */   }
/*     */ 
/*     */   
/*     */   public Brain<Breeze> getBrain() {
/*  91 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Breeze> brainProvider() {
/*  96 */     return Brain.provider(BreezeAi.MEMORY_TYPES, BreezeAi.SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 101 */     if (level().isClientSide() && DATA_POSE.equals(paramEntityDataAccessor)) {
/*     */       
/* 103 */       resetAnimations();
/*     */       
/* 105 */       Pose pose = getPose();
/* 106 */       switch (pose) { case SHOOTING:
/* 107 */           this.shoot.startIfStopped(this.tickCount); break;
/* 108 */         case INHALING: this.inhale.startIfStopped(this.tickCount); break;
/* 109 */         case SLIDING: this.slide.startIfStopped(this.tickCount);
/*     */           break; }
/*     */     
/*     */     } 
/* 113 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*     */   }
/*     */   
/*     */   private void resetAnimations() {
/* 117 */     this.shoot.stop();
/* 118 */     this.idle.stop();
/* 119 */     this.inhale.stop();
/* 120 */     this.longJump.stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 125 */     Pose pose = getPose();
/* 126 */     switch (pose) { case SLIDING:
/* 127 */         emitGroundParticles(20); break;
/*     */       case SHOOTING: case INHALING: case STANDING:
/* 129 */         resetJumpTrail().emitGroundParticles(1 + getRandom().nextInt(1)); break;
/*     */       case LONG_JUMPING:
/* 131 */         this.longJump.startIfStopped(this.tickCount);
/* 132 */         emitJumpTrailParticles();
/*     */         break; }
/*     */ 
/*     */     
/* 136 */     this.idle.startIfStopped(this.tickCount);
/*     */     
/* 138 */     if (pose != Pose.SLIDING && this.slide.isStarted()) {
/* 139 */       this.slideBack.start(this.tickCount);
/* 140 */       this.slide.stop();
/*     */     } 
/*     */     
/* 143 */     this.soundTick = (this.soundTick == 0) ? this.random.nextIntBetweenInclusive(1, 80) : (this.soundTick - 1);
/* 144 */     if (this.soundTick == 0) {
/* 145 */       playWhirlSound();
/*     */     }
/*     */     
/* 148 */     super.tick();
/*     */   }
/*     */   
/*     */   public Breeze resetJumpTrail() {
/* 152 */     this.jumpTrailStartedTick = 0;
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   public void emitJumpTrailParticles() {
/* 157 */     if (++this.jumpTrailStartedTick > 5) {
/*     */       return;
/*     */     }
/*     */     
/* 161 */     BlockState blockState = !getInBlockState().isAir() ? getInBlockState() : getBlockStateOn();
/* 162 */     Vec3 vec31 = getDeltaMovement();
/* 163 */     Vec3 vec32 = position().add(vec31).add(0.0D, 0.10000000149011612D, 0.0D);
/*     */     
/* 165 */     for (byte b = 0; b < 3; b++) {
/* 166 */       level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, blockState), vec32.x, vec32.y, vec32.z, 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   public void emitGroundParticles(int paramInt) {
/* 171 */     if (isPassenger()) {
/*     */       return;
/*     */     }
/*     */     
/* 175 */     Vec3 vec31 = getBoundingBox().getCenter();
/* 176 */     Vec3 vec32 = new Vec3(vec31.x, (position()).y, vec31.z);
/*     */     
/* 178 */     BlockState blockState = !getInBlockState().isAir() ? getInBlockState() : getBlockStateOn();
/*     */     
/* 180 */     if (blockState.getRenderShape() == RenderShape.INVISIBLE) {
/*     */       return;
/*     */     }
/*     */     
/* 184 */     for (byte b = 0; b < paramInt; b++) {
/* 185 */       level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, blockState), vec32.x, vec32.y, vec32.z, 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playAmbientSound() {
/* 192 */     if (getTarget() != null && onGround()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 197 */     level().playLocalSound((Entity)this, getAmbientSound(), getSoundSource(), 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void playWhirlSound() {
/* 201 */     float f1 = 0.7F + 0.4F * this.random.nextFloat();
/* 202 */     float f2 = 0.8F + 0.2F * this.random.nextFloat();
/*     */     
/* 204 */     level().playLocalSound((Entity)this, SoundEvents.BREEZE_WHIRL, getSoundSource(), f2, f1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ProjectileDeflection deflection(Projectile paramProjectile) {
/* 209 */     if (paramProjectile.getType() == EntityType.BREEZE_WIND_CHARGE || paramProjectile.getType() == EntityType.WIND_CHARGE) {
/* 210 */       return ProjectileDeflection.NONE;
/*     */     }
/*     */     
/* 213 */     return getType().is(EntityTypeTags.DEFLECTS_PROJECTILES) ? PROJECTILE_DEFLECTION : ProjectileDeflection.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 218 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 223 */     return SoundEvents.BREEZE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 228 */     return SoundEvents.BREEZE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 233 */     return onGround() ? SoundEvents.BREEZE_IDLE_GROUND : SoundEvents.BREEZE_IDLE_AIR;
/*     */   }
/*     */   
/*     */   public Optional<LivingEntity> getHurtBy() {
/* 237 */     return getBrain().getMemory(MemoryModuleType.HURT_BY)
/* 238 */       .map(DamageSource::getEntity)
/* 239 */       .filter(paramEntity -> paramEntity instanceof LivingEntity)
/* 240 */       .map(paramEntity -> (LivingEntity)paramEntity);
/*     */   }
/*     */   
/*     */   public boolean withinInnerCircleRange(Vec3 paramVec3) {
/* 244 */     Vec3 vec3 = blockPosition().getCenter();
/* 245 */     return paramVec3.closerThan(vec3, 4.0D, 10.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 250 */     ProfilerFiller profilerFiller = Profiler.get();
/* 251 */     profilerFiller.push("breezeBrain");
/* 252 */     getBrain().tick(paramServerLevel, (LivingEntity)this);
/*     */     
/* 254 */     profilerFiller.popPush("breezeActivityUpdate");
/* 255 */     BreezeAi.updateActivity(this);
/* 256 */     profilerFiller.pop();
/*     */     
/* 258 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackType(EntityType<?> paramEntityType) {
/* 263 */     return (paramEntityType == EntityType.PLAYER || paramEntityType == EntityType.IRON_GOLEM);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 268 */     return 30;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeadRotSpeed() {
/* 273 */     return 25;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getFiringYPosition() {
/* 278 */     return getY() + (getBbHeight() / 2.0F) + 0.30000001192092896D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInvulnerableTo(ServerLevel paramServerLevel, DamageSource paramDamageSource) {
/* 283 */     return (paramDamageSource.getEntity() instanceof Breeze || super.isInvulnerableTo(paramServerLevel, paramDamageSource));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFluidJumpThreshold() {
/* 289 */     return getEyeHeight();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(double paramDouble, float paramFloat, DamageSource paramDamageSource) {
/* 295 */     if (paramDouble > 3.0D) {
/* 296 */       playSound(SoundEvents.BREEZE_LAND, 1.0F, 1.0F);
/*     */     }
/* 298 */     return super.causeFallDamage(paramDouble, paramFloat, paramDamageSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 303 */     return Entity.MovementEmission.EVENTS;
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity getTarget() {
/* 308 */     return getTargetFromBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerDebugValues(ServerLevel paramServerLevel, DebugValueSource.Registration paramRegistration) {
/* 313 */     super.registerDebugValues(paramServerLevel, paramRegistration);
/* 314 */     paramRegistration.register(DebugSubscriptions.BREEZES, () -> new DebugBreezeInfo(getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).map(Entity::getId), getBrain().getMemory(MemoryModuleType.BREEZE_JUMP_TARGET)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\breeze\Breeze.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */