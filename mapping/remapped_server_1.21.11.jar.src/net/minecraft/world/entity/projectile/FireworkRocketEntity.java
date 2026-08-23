/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.item.component.Fireworks;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class FireworkRocketEntity
/*     */   extends Projectile implements ItemSupplier {
/*  41 */   private static final EntityDataAccessor<ItemStack> DATA_ID_FIREWORKS_ITEM = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.ITEM_STACK);
/*  42 */   private static final EntityDataAccessor<OptionalInt> DATA_ATTACHED_TO_TARGET = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
/*  43 */   private static final EntityDataAccessor<Boolean> DATA_SHOT_AT_ANGLE = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int DEFAULT_LIFE = 0;
/*     */   private static final int DEFAULT_LIFE_TIME = 0;
/*     */   private static final boolean DEFAULT_SHOT_AT_ANGLE = false;
/*  48 */   private int life = 0;
/*  49 */   private int lifetime = 0;
/*     */   private LivingEntity attachedToEntity;
/*     */   
/*     */   public FireworkRocketEntity(EntityType<? extends FireworkRocketEntity> paramEntityType, Level paramLevel) {
/*  53 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/*  57 */     super(EntityType.FIREWORK_ROCKET, paramLevel);
/*  58 */     this.life = 0;
/*     */     
/*  60 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */     
/*  62 */     this.entityData.set(DATA_ID_FIREWORKS_ITEM, paramItemStack.copy());
/*  63 */     int i = 1;
/*  64 */     Fireworks fireworks = (Fireworks)paramItemStack.get(DataComponents.FIREWORKS);
/*  65 */     if (fireworks != null) {
/*  66 */       i += fireworks.flightDuration();
/*     */     }
/*  68 */     setDeltaMovement(this.random
/*  69 */         .triangle(0.0D, 0.002297D), 0.05D, this.random
/*     */         
/*  71 */         .triangle(0.0D, 0.002297D));
/*     */ 
/*     */     
/*  74 */     this.lifetime = 10 * i + this.random.nextInt(6) + this.random.nextInt(7);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level paramLevel, Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/*  78 */     this(paramLevel, paramDouble1, paramDouble2, paramDouble3, paramItemStack);
/*  79 */     setOwner(paramEntity);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level paramLevel, ItemStack paramItemStack, LivingEntity paramLivingEntity) {
/*  83 */     this(paramLevel, (Entity)paramLivingEntity, paramLivingEntity.getX(), paramLivingEntity.getY(), paramLivingEntity.getZ(), paramItemStack);
/*  84 */     this.entityData.set(DATA_ATTACHED_TO_TARGET, OptionalInt.of(paramLivingEntity.getId()));
/*  85 */     this.attachedToEntity = paramLivingEntity;
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level paramLevel, ItemStack paramItemStack, double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean) {
/*  89 */     this(paramLevel, paramDouble1, paramDouble2, paramDouble3, paramItemStack);
/*  90 */     this.entityData.set(DATA_SHOT_AT_ANGLE, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level paramLevel, ItemStack paramItemStack, Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean) {
/*  94 */     this(paramLevel, paramItemStack, paramDouble1, paramDouble2, paramDouble3, paramBoolean);
/*  95 */     setOwner(paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 100 */     paramBuilder.define(DATA_ID_FIREWORKS_ITEM, getDefaultItem());
/* 101 */     paramBuilder.define(DATA_ATTACHED_TO_TARGET, OptionalInt.empty());
/* 102 */     paramBuilder.define(DATA_SHOT_AT_ANGLE, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/* 107 */     return (paramDouble < 4096.0D && !isAttachedToEntity());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRender(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 112 */     return (super.shouldRender(paramDouble1, paramDouble2, paramDouble3) && !isAttachedToEntity());
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     HitResult hitResult;
/* 117 */     super.tick();
/*     */ 
/*     */     
/* 120 */     if (isAttachedToEntity()) {
/* 121 */       if (this.attachedToEntity == null) {
/* 122 */         ((OptionalInt)this.entityData.get(DATA_ATTACHED_TO_TARGET)).ifPresent(paramInt -> {
/*     */               Entity entity = level().getEntity(paramInt);
/*     */               if (entity instanceof LivingEntity) {
/*     */                 this.attachedToEntity = (LivingEntity)entity;
/*     */               }
/*     */             });
/*     */       }
/* 129 */       if (this.attachedToEntity != null) {
/*     */         Vec3 vec3;
/* 131 */         if (this.attachedToEntity.isFallFlying()) {
/* 132 */           Vec3 vec31 = this.attachedToEntity.getLookAngle();
/* 133 */           double d1 = 1.5D;
/* 134 */           double d2 = 0.1D;
/*     */           
/* 136 */           Vec3 vec32 = this.attachedToEntity.getDeltaMovement();
/* 137 */           this.attachedToEntity.setDeltaMovement(vec32.add(vec31.x * 0.1D + (vec31.x * 1.5D - vec32.x) * 0.5D, vec31.y * 0.1D + (vec31.y * 1.5D - vec32.y) * 0.5D, vec31.z * 0.1D + (vec31.z * 1.5D - vec32.z) * 0.5D));
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 142 */           vec3 = this.attachedToEntity.getHandHoldingItemAngle(Items.FIREWORK_ROCKET);
/*     */         } else {
/* 144 */           vec3 = Vec3.ZERO;
/*     */         } 
/* 146 */         setPos(this.attachedToEntity.getX() + vec3.x, this.attachedToEntity.getY() + vec3.y, this.attachedToEntity.getZ() + vec3.z);
/*     */         
/* 148 */         setDeltaMovement(this.attachedToEntity.getDeltaMovement());
/*     */       } 
/* 150 */       hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*     */     } else {
/* 152 */       if (!isShotAtAngle()) {
/*     */         
/* 154 */         double d = this.horizontalCollision ? 1.0D : 1.15D;
/* 155 */         setDeltaMovement(getDeltaMovement().multiply(d, 1.0D, d).add(0.0D, 0.04D, 0.0D));
/*     */       } 
/* 157 */       Vec3 vec3 = getDeltaMovement();
/* 158 */       hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*     */       
/* 160 */       move(MoverType.SELF, vec3);
/* 161 */       applyEffectsFromBlocks();
/* 162 */       setDeltaMovement(vec3);
/*     */     } 
/*     */     
/* 165 */     if (!this.noPhysics && isAlive() && hitResult.getType() != HitResult.Type.MISS) {
/* 166 */       hitTargetOrDeflectSelf(hitResult);
/* 167 */       this.needsSync = true;
/*     */     } 
/*     */     
/* 170 */     updateRotation();
/*     */     
/* 172 */     if (this.life == 0 && !isSilent()) {
/* 173 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F);
/*     */     }
/*     */     
/* 176 */     this.life++;
/* 177 */     if (level().isClientSide() && this.life % 2 < 2) {
/* 178 */       level().addParticle((ParticleOptions)ParticleTypes.FIREWORK, getX(), getY(), getZ(), this.random.nextGaussian() * 0.05D, -(getDeltaMovement()).y * 0.5D, this.random.nextGaussian() * 0.05D);
/*     */     }
/* 180 */     if (this.life > this.lifetime) { Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 181 */         explode(serverLevel); }
/*     */        }
/*     */   
/*     */   }
/*     */   private void explode(ServerLevel paramServerLevel) {
/* 186 */     paramServerLevel.broadcastEntityEvent(this, (byte)17);
/* 187 */     gameEvent((Holder)GameEvent.EXPLODE, getOwner());
/* 188 */     dealExplosionDamage(paramServerLevel);
/* 189 */     discard();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/* 194 */     super.onHitEntity(paramEntityHitResult);
/* 195 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 196 */       explode(serverLevel); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult paramBlockHitResult) {
/* 202 */     BlockPos blockPos = new BlockPos((Vec3i)paramBlockHitResult.getBlockPos());
/* 203 */     level().getBlockState(blockPos).entityInside(level(), blockPos, this, InsideBlockEffectApplier.NOOP, true);
/* 204 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (hasExplosion())
/* 205 */         explode(serverLevel);  }
/*     */     
/* 207 */     super.onHitBlock(paramBlockHitResult);
/*     */   }
/*     */   
/*     */   private boolean hasExplosion() {
/* 211 */     return !getExplosions().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   private void dealExplosionDamage(ServerLevel paramServerLevel) {
/* 216 */     float f = 0.0F;
/* 217 */     List<FireworkExplosion> list = getExplosions();
/* 218 */     if (!list.isEmpty()) {
/* 219 */       f = 5.0F + (list.size() * 2);
/*     */     }
/* 221 */     if (f > 0.0F) {
/* 222 */       if (this.attachedToEntity != null) {
/* 223 */         this.attachedToEntity.hurtServer(paramServerLevel, damageSources().fireworks(this, getOwner()), 5.0F + (list.size() * 2));
/*     */       }
/*     */       
/* 226 */       double d = 5.0D;
/* 227 */       Vec3 vec3 = position();
/* 228 */       List list1 = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(5.0D));
/* 229 */       for (LivingEntity livingEntity : list1) {
/* 230 */         if (livingEntity == this.attachedToEntity) {
/*     */           continue;
/*     */         }
/* 233 */         if (distanceToSqr((Entity)livingEntity) > 25.0D) {
/*     */           continue;
/*     */         }
/*     */         
/* 237 */         boolean bool = false;
/* 238 */         for (byte b = 0; b < 2; b++) {
/* 239 */           Vec3 vec31 = new Vec3(livingEntity.getX(), livingEntity.getY(0.5D * b), livingEntity.getZ());
/* 240 */           BlockHitResult blockHitResult = level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
/* 241 */           if (blockHitResult.getType() == HitResult.Type.MISS) {
/* 242 */             bool = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 246 */         if (bool) {
/* 247 */           float f1 = f * (float)Math.sqrt((5.0D - distanceTo((Entity)livingEntity)) / 5.0D);
/* 248 */           livingEntity.hurtServer(paramServerLevel, damageSources().fireworks(this, getOwner()), f1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isAttachedToEntity() {
/* 255 */     return ((OptionalInt)this.entityData.get(DATA_ATTACHED_TO_TARGET)).isPresent();
/*     */   }
/*     */   
/*     */   public boolean isShotAtAngle() {
/* 259 */     return ((Boolean)this.entityData.get(DATA_SHOT_AT_ANGLE)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 264 */     if (paramByte == 17 && level().isClientSide()) {
/* 265 */       Vec3 vec3 = getDeltaMovement();
/* 266 */       level().createFireworks(getX(), getY(), getZ(), vec3.x, vec3.y, vec3.z, getExplosions());
/*     */     } 
/* 268 */     super.handleEntityEvent(paramByte);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 273 */     super.addAdditionalSaveData(paramValueOutput);
/* 274 */     paramValueOutput.putInt("Life", this.life);
/* 275 */     paramValueOutput.putInt("LifeTime", this.lifetime);
/* 276 */     paramValueOutput.store("FireworksItem", ItemStack.CODEC, getItem());
/* 277 */     paramValueOutput.putBoolean("ShotAtAngle", ((Boolean)this.entityData.get(DATA_SHOT_AT_ANGLE)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 282 */     super.readAdditionalSaveData(paramValueInput);
/* 283 */     this.life = paramValueInput.getIntOr("Life", 0);
/* 284 */     this.lifetime = paramValueInput.getIntOr("LifeTime", 0);
/*     */     
/* 286 */     this.entityData.set(DATA_ID_FIREWORKS_ITEM, paramValueInput.read("FireworksItem", ItemStack.CODEC).orElse(getDefaultItem()));
/*     */     
/* 288 */     this.entityData.set(DATA_SHOT_AT_ANGLE, Boolean.valueOf(paramValueInput.getBooleanOr("ShotAtAngle", false)));
/*     */   }
/*     */   
/*     */   private List<FireworkExplosion> getExplosions() {
/* 292 */     ItemStack itemStack = (ItemStack)this.entityData.get(DATA_ID_FIREWORKS_ITEM);
/* 293 */     Fireworks fireworks = (Fireworks)itemStack.get(DataComponents.FIREWORKS);
/* 294 */     return (fireworks != null) ? fireworks.explosions() : List.<FireworkExplosion>of();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem() {
/* 299 */     return (ItemStack)this.entityData.get(DATA_ID_FIREWORKS_ITEM);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 304 */     return false;
/*     */   }
/*     */   
/*     */   private static ItemStack getDefaultItem() {
/* 308 */     return new ItemStack((ItemLike)Items.FIREWORK_ROCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleDoubleImmutablePair calculateHorizontalHurtKnockbackDirection(LivingEntity paramLivingEntity, DamageSource paramDamageSource) {
/* 313 */     double d1 = (paramLivingEntity.position()).x - (position()).x;
/* 314 */     double d2 = (paramLivingEntity.position()).z - (position()).z;
/* 315 */     return DoubleDoubleImmutablePair.of(d1, d2);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\FireworkRocketEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */