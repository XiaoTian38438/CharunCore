/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ShulkerBullet
/*     */   extends Projectile
/*     */ {
/*     */   private static final double SPEED = 0.15D;
/*     */   private EntityReference<Entity> finalTarget;
/*     */   private Direction currentMoveDirection;
/*     */   private int flightSteps;
/*     */   private double targetDeltaX;
/*     */   private double targetDeltaY;
/*     */   private double targetDeltaZ;
/*     */   
/*     */   public ShulkerBullet(EntityType<? extends ShulkerBullet> paramEntityType, Level paramLevel) {
/*  50 */     super((EntityType)paramEntityType, paramLevel);
/*     */     
/*  52 */     this.noPhysics = true;
/*     */   }
/*     */   
/*     */   public ShulkerBullet(Level paramLevel, LivingEntity paramLivingEntity, Entity paramEntity, Direction.Axis paramAxis) {
/*  56 */     this(EntityType.SHULKER_BULLET, paramLevel);
/*  57 */     setOwner((Entity)paramLivingEntity);
/*     */     
/*  59 */     Vec3 vec3 = paramLivingEntity.getBoundingBox().getCenter();
/*  60 */     snapTo(vec3.x, vec3.y, vec3.z, getYRot(), getXRot());
/*     */     
/*  62 */     this.finalTarget = EntityReference.of((UniquelyIdentifyable)paramEntity);
/*     */     
/*  64 */     this.currentMoveDirection = Direction.UP;
/*  65 */     selectNextMoveDirection(paramAxis, paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/*  70 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  75 */     super.addAdditionalSaveData(paramValueOutput);
/*  76 */     if (this.finalTarget != null) {
/*  77 */       paramValueOutput.store("Target", UUIDUtil.CODEC, this.finalTarget.getUUID());
/*     */     }
/*  79 */     paramValueOutput.storeNullable("Dir", Direction.LEGACY_ID_CODEC, this.currentMoveDirection);
/*  80 */     paramValueOutput.putInt("Steps", this.flightSteps);
/*  81 */     paramValueOutput.putDouble("TXD", this.targetDeltaX);
/*  82 */     paramValueOutput.putDouble("TYD", this.targetDeltaY);
/*  83 */     paramValueOutput.putDouble("TZD", this.targetDeltaZ);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  88 */     super.readAdditionalSaveData(paramValueInput);
/*  89 */     this.flightSteps = paramValueInput.getIntOr("Steps", 0);
/*  90 */     this.targetDeltaX = paramValueInput.getDoubleOr("TXD", 0.0D);
/*  91 */     this.targetDeltaY = paramValueInput.getDoubleOr("TYD", 0.0D);
/*  92 */     this.targetDeltaZ = paramValueInput.getDoubleOr("TZD", 0.0D);
/*  93 */     this.currentMoveDirection = paramValueInput.read("Dir", Direction.LEGACY_ID_CODEC).orElse(null);
/*  94 */     this.finalTarget = EntityReference.read(paramValueInput, "Target");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {}
/*     */ 
/*     */   
/*     */   private Direction getMoveDirection() {
/* 102 */     return this.currentMoveDirection;
/*     */   }
/*     */   
/*     */   private void setMoveDirection(Direction paramDirection) {
/* 106 */     this.currentMoveDirection = paramDirection;
/*     */   }
/*     */   
/*     */   private void selectNextMoveDirection(Direction.Axis paramAxis, Entity paramEntity) {
/*     */     BlockPos blockPos;
/* 111 */     double d1 = 0.5D;
/* 112 */     if (paramEntity == null) {
/* 113 */       blockPos = blockPosition().below();
/*     */     } else {
/* 115 */       d1 = paramEntity.getBbHeight() * 0.5D;
/* 116 */       blockPos = BlockPos.containing(paramEntity.getX(), paramEntity.getY() + d1, paramEntity.getZ());
/*     */     } 
/*     */     
/* 119 */     double d2 = blockPos.getX() + 0.5D;
/* 120 */     double d3 = blockPos.getY() + d1;
/* 121 */     double d4 = blockPos.getZ() + 0.5D;
/*     */     
/* 123 */     Direction direction = null;
/* 124 */     if (!blockPos.closerToCenterThan((Position)position(), 2.0D)) {
/* 125 */       BlockPos blockPos1 = blockPosition();
/* 126 */       ArrayList<Direction> arrayList = Lists.newArrayList();
/*     */       
/* 128 */       if (paramAxis != Direction.Axis.X) {
/* 129 */         if (blockPos1.getX() < blockPos.getX() && level().isEmptyBlock(blockPos1.east())) {
/* 130 */           arrayList.add(Direction.EAST);
/* 131 */         } else if (blockPos1.getX() > blockPos.getX() && level().isEmptyBlock(blockPos1.west())) {
/* 132 */           arrayList.add(Direction.WEST);
/*     */         } 
/*     */       }
/* 135 */       if (paramAxis != Direction.Axis.Y) {
/* 136 */         if (blockPos1.getY() < blockPos.getY() && level().isEmptyBlock(blockPos1.above())) {
/* 137 */           arrayList.add(Direction.UP);
/* 138 */         } else if (blockPos1.getY() > blockPos.getY() && level().isEmptyBlock(blockPos1.below())) {
/* 139 */           arrayList.add(Direction.DOWN);
/*     */         } 
/*     */       }
/* 142 */       if (paramAxis != Direction.Axis.Z) {
/* 143 */         if (blockPos1.getZ() < blockPos.getZ() && level().isEmptyBlock(blockPos1.south())) {
/* 144 */           arrayList.add(Direction.SOUTH);
/* 145 */         } else if (blockPos1.getZ() > blockPos.getZ() && level().isEmptyBlock(blockPos1.north())) {
/* 146 */           arrayList.add(Direction.NORTH);
/*     */         } 
/*     */       }
/*     */       
/* 150 */       direction = Direction.getRandom(this.random);
/* 151 */       if (arrayList.isEmpty()) {
/* 152 */         byte b = 5;
/* 153 */         while (!level().isEmptyBlock(blockPos1.relative(direction)) && b > 0) {
/* 154 */           direction = Direction.getRandom(this.random);
/* 155 */           b--;
/*     */         } 
/*     */       } else {
/* 158 */         direction = arrayList.get(this.random.nextInt(arrayList.size()));
/*     */       } 
/*     */       
/* 161 */       d2 = getX() + direction.getStepX();
/* 162 */       d3 = getY() + direction.getStepY();
/* 163 */       d4 = getZ() + direction.getStepZ();
/*     */     } 
/*     */     
/* 166 */     setMoveDirection(direction);
/*     */     
/* 168 */     double d5 = d2 - getX();
/* 169 */     double d6 = d3 - getY();
/* 170 */     double d7 = d4 - getZ();
/*     */     
/* 172 */     double d8 = Math.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
/* 173 */     if (d8 == 0.0D) {
/* 174 */       this.targetDeltaX = 0.0D;
/* 175 */       this.targetDeltaY = 0.0D;
/* 176 */       this.targetDeltaZ = 0.0D;
/*     */     } else {
/* 178 */       this.targetDeltaX = d5 / d8 * 0.15D;
/* 179 */       this.targetDeltaY = d6 / d8 * 0.15D;
/* 180 */       this.targetDeltaZ = d7 / d8 * 0.15D;
/*     */     } 
/*     */     
/* 183 */     this.needsSync = true;
/* 184 */     this.flightSteps = 10 + this.random.nextInt(5) * 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkDespawn() {
/* 189 */     if (level().getDifficulty() == Difficulty.PEACEFUL) {
/* 190 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/* 196 */     return 0.04D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 201 */     super.tick();
/*     */     
/* 203 */     Entity entity = !level().isClientSide() ? EntityReference.getEntity(this.finalTarget, level()) : null;
/* 204 */     HitResult hitResult = null;
/* 205 */     if (!level().isClientSide()) {
/* 206 */       if (entity == null) {
/* 207 */         this.finalTarget = null;
/*     */       }
/*     */       
/* 210 */       if (entity != null && entity.isAlive() && (!(entity instanceof net.minecraft.world.entity.player.Player) || !entity.isSpectator())) {
/* 211 */         this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
/* 212 */         this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
/* 213 */         this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
/*     */         
/* 215 */         Vec3 vec31 = getDeltaMovement();
/* 216 */         setDeltaMovement(vec31.add((this.targetDeltaX - vec31.x) * 0.2D, (this.targetDeltaY - vec31.y) * 0.2D, (this.targetDeltaZ - vec31.z) * 0.2D));
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 222 */         applyGravity();
/*     */       } 
/*     */       
/* 225 */       hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*     */     } 
/*     */     
/* 228 */     Vec3 vec3 = getDeltaMovement();
/* 229 */     setPos(position().add(vec3));
/* 230 */     applyEffectsFromBlocks();
/* 231 */     if (this.portalProcess != null && this.portalProcess.isInsidePortalThisTick()) {
/* 232 */       handlePortal();
/*     */     }
/*     */     
/* 235 */     if (hitResult != null && isAlive() && hitResult.getType() != HitResult.Type.MISS) {
/* 236 */       hitTargetOrDeflectSelf(hitResult);
/*     */     }
/*     */     
/* 239 */     ProjectileUtil.rotateTowardsMovement(this, 0.5F);
/*     */     
/* 241 */     if (level().isClientSide()) {
/* 242 */       level().addParticle((ParticleOptions)ParticleTypes.END_ROD, getX() - vec3.x, getY() - vec3.y + 0.15D, getZ() - vec3.z, 0.0D, 0.0D, 0.0D);
/* 243 */     } else if (entity != null) {
/* 244 */       if (this.flightSteps > 0) {
/* 245 */         this.flightSteps--;
/* 246 */         if (this.flightSteps == 0) {
/* 247 */           selectNextMoveDirection((this.currentMoveDirection == null) ? null : this.currentMoveDirection.getAxis(), entity);
/*     */         }
/*     */       } 
/*     */       
/* 251 */       if (this.currentMoveDirection != null) {
/*     */         
/* 253 */         BlockPos blockPos = blockPosition();
/* 254 */         Direction.Axis axis = this.currentMoveDirection.getAxis();
/* 255 */         if (level().loadedAndEntityCanStandOn(blockPos.relative(this.currentMoveDirection), this)) {
/* 256 */           selectNextMoveDirection(axis, entity);
/*     */         } else {
/* 258 */           BlockPos blockPos1 = entity.blockPosition();
/* 259 */           if ((axis == Direction.Axis.X && blockPos.getX() == blockPos1.getX()) || (axis == Direction.Axis.Z && blockPos
/* 260 */             .getZ() == blockPos1.getZ()) || (axis == Direction.Axis.Y && blockPos
/* 261 */             .getY() == blockPos1.getY()))
/*     */           {
/* 263 */             selectNextMoveDirection(axis, entity);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAffectedByBlocks() {
/* 273 */     return !isRemoved();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHitEntity(Entity paramEntity) {
/* 278 */     return (super.canHitEntity(paramEntity) && !paramEntity.noPhysics);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnFire() {
/* 283 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/* 288 */     return (paramDouble < 16384.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLightLevelDependentMagicValue() {
/* 293 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/* 298 */     super.onHitEntity(paramEntityHitResult);
/* 299 */     Entity entity1 = paramEntityHitResult.getEntity();
/* 300 */     Entity entity2 = getOwner();
/* 301 */     LivingEntity livingEntity = (entity2 instanceof LivingEntity) ? (LivingEntity)entity2 : null;
/* 302 */     DamageSource damageSource = damageSources().mobProjectile(this, livingEntity);
/* 303 */     boolean bool = entity1.hurtOrSimulate(damageSource, 4.0F);
/* 304 */     if (bool) {
/* 305 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 306 */         EnchantmentHelper.doPostAttackEffects(serverLevel, entity1, damageSource); }
/*     */       
/* 308 */       if (entity1 instanceof LivingEntity) { LivingEntity livingEntity1 = (LivingEntity)entity1;
/* 309 */         livingEntity1.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200), (Entity)MoreObjects.firstNonNull(entity2, this)); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult paramBlockHitResult) {
/* 316 */     super.onHitBlock(paramBlockHitResult);
/* 317 */     ((ServerLevel)level()).sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
/* 318 */     playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private void destroy() {
/* 322 */     discard();
/* 323 */     level().gameEvent((Holder)GameEvent.ENTITY_DAMAGE, position(), GameEvent.Context.of(this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult paramHitResult) {
/* 328 */     super.onHit(paramHitResult);
/* 329 */     destroy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 334 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtClient(DamageSource paramDamageSource) {
/* 339 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 344 */     playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
/* 345 */     paramServerLevel.sendParticles((ParticleOptions)ParticleTypes.CRIT, getX(), getY(), getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
/* 346 */     destroy();
/* 347 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket paramClientboundAddEntityPacket) {
/* 352 */     super.recreateFromPacket(paramClientboundAddEntityPacket);
/* 353 */     setDeltaMovement(paramClientboundAddEntityPacket.getMovement());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\ShulkerBullet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */