/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public abstract class Projectile
/*     */   extends Entity
/*     */   implements TraceableEntity
/*     */ {
/*     */   private static final boolean DEFAULT_LEFT_OWNER = false;
/*     */   private static final boolean DEFAULT_HAS_BEEN_SHOT = false;
/*     */   protected EntityReference<Entity> owner;
/*     */   private boolean leftOwner = false;
/*     */   private boolean leftOwnerChecked;
/*     */   private boolean hasBeenShot = false;
/*     */   private Entity lastDeflectedBy;
/*     */   
/*     */   protected Projectile(EntityType<? extends Projectile> paramEntityType, Level paramLevel) {
/*  54 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   protected void setOwner(EntityReference<Entity> paramEntityReference) {
/*  58 */     this.owner = paramEntityReference;
/*     */   }
/*     */   
/*     */   public void setOwner(Entity paramEntity) {
/*  62 */     setOwner(EntityReference.of((UniquelyIdentifyable)paramEntity));
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getOwner() {
/*  67 */     return EntityReference.getEntity(this.owner, level());
/*     */   }
/*     */   
/*     */   public Entity getEffectSource() {
/*  71 */     return (Entity)MoreObjects.firstNonNull(getOwner(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  76 */     EntityReference.store(this.owner, paramValueOutput, "Owner");
/*  77 */     if (this.leftOwner) {
/*  78 */       paramValueOutput.putBoolean("LeftOwner", true);
/*     */     }
/*  80 */     paramValueOutput.putBoolean("HasBeenShot", this.hasBeenShot);
/*     */   }
/*     */   
/*     */   protected boolean ownedBy(Entity paramEntity) {
/*  84 */     return (this.owner != null && this.owner.matches((UniquelyIdentifyable)paramEntity));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  89 */     setOwner(EntityReference.read(paramValueInput, "Owner"));
/*  90 */     this.leftOwner = paramValueInput.getBooleanOr("LeftOwner", false);
/*  91 */     this.hasBeenShot = paramValueInput.getBooleanOr("HasBeenShot", false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void restoreFrom(Entity paramEntity) {
/*  96 */     super.restoreFrom(paramEntity);
/*  97 */     if (paramEntity instanceof Projectile) { Projectile projectile = (Projectile)paramEntity;
/*  98 */       this.owner = projectile.owner; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 104 */     if (!this.hasBeenShot) {
/* 105 */       gameEvent((Holder)GameEvent.PROJECTILE_SHOOT, getOwner());
/* 106 */       this.hasBeenShot = true;
/*     */     } 
/*     */     
/* 109 */     checkLeftOwner();
/* 110 */     super.tick();
/* 111 */     this.leftOwnerChecked = false;
/*     */   }
/*     */   
/*     */   protected void checkLeftOwner() {
/* 115 */     if (!this.leftOwner && !this.leftOwnerChecked) {
/* 116 */       this.leftOwner = isOutsideOwnerCollisionRange();
/* 117 */       this.leftOwnerChecked = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isOutsideOwnerCollisionRange() {
/* 122 */     Entity entity = getOwner();
/* 123 */     if (entity != null) {
/* 124 */       AABB aABB = getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D);
/* 125 */       return entity.getRootVehicle().getSelfAndPassengers()
/* 126 */         .filter(EntitySelector.CAN_BE_PICKED)
/* 127 */         .noneMatch(paramEntity -> paramAABB.intersects(paramEntity.getBoundingBox()));
/*     */     } 
/* 129 */     return true;
/*     */   }
/*     */   
/*     */   public Vec3 getMovementToShoot(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {
/* 133 */     return (new Vec3(paramDouble1, paramDouble2, paramDouble3)).normalize().add(this.random
/* 134 */         .triangle(0.0D, 0.0172275D * paramFloat2), this.random
/* 135 */         .triangle(0.0D, 0.0172275D * paramFloat2), this.random
/* 136 */         .triangle(0.0D, 0.0172275D * paramFloat2))
/* 137 */       .scale(paramFloat1);
/*     */   }
/*     */   
/*     */   public void shoot(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {
/* 141 */     Vec3 vec3 = getMovementToShoot(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
/* 142 */     setDeltaMovement(vec3);
/* 143 */     this.needsSync = true;
/*     */     
/* 145 */     double d = vec3.horizontalDistance();
/*     */     
/* 147 */     setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 57.2957763671875D));
/* 148 */     setXRot((float)(Mth.atan2(vec3.y, d) * 57.2957763671875D));
/* 149 */     this.yRotO = getYRot();
/* 150 */     this.xRotO = getXRot();
/*     */   }
/*     */   
/*     */   public void shootFromRotation(Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
/* 154 */     float f1 = -Mth.sin((paramFloat2 * 0.017453292F)) * Mth.cos((paramFloat1 * 0.017453292F));
/* 155 */     float f2 = -Mth.sin(((paramFloat1 + paramFloat3) * 0.017453292F));
/* 156 */     float f3 = Mth.cos((paramFloat2 * 0.017453292F)) * Mth.cos((paramFloat1 * 0.017453292F));
/* 157 */     shoot(f1, f2, f3, paramFloat4, paramFloat5);
/*     */     
/* 159 */     Vec3 vec3 = paramEntity.getKnownMovement();
/* 160 */     setDeltaMovement(getDeltaMovement().add(vec3.x, 
/*     */           
/* 162 */           paramEntity.onGround() ? 0.0D : vec3.y, vec3.z));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onAboveBubbleColumn(boolean paramBoolean, BlockPos paramBlockPos) {
/* 169 */     double d = paramBoolean ? -0.03D : 0.1D;
/* 170 */     setDeltaMovement(getDeltaMovement().add(0.0D, d, 0.0D));
/* 171 */     sendBubbleColumnParticles(level(), paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInsideBubbleColumn(boolean paramBoolean) {
/* 176 */     double d = paramBoolean ? -0.03D : 0.06D;
/* 177 */     setDeltaMovement(getDeltaMovement().add(0.0D, d, 0.0D));
/* 178 */     resetFallDistance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Projectile> T spawnProjectileFromRotation(ProjectileFactory<T> paramProjectileFactory, ServerLevel paramServerLevel, ItemStack paramItemStack, LivingEntity paramLivingEntity, float paramFloat1, float paramFloat2, float paramFloat3) {
/* 187 */     return spawnProjectile(paramProjectileFactory.create(paramServerLevel, paramLivingEntity, paramItemStack), paramServerLevel, paramItemStack, paramProjectile -> paramProjectile.shootFromRotation((Entity)paramLivingEntity, paramLivingEntity.getXRot(), paramLivingEntity.getYRot(), paramFloat1, paramFloat2, paramFloat3));
/*     */   }
/*     */   
/*     */   public static <T extends Projectile> T spawnProjectileUsingShoot(ProjectileFactory<T> paramProjectileFactory, ServerLevel paramServerLevel, ItemStack paramItemStack, LivingEntity paramLivingEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {
/* 191 */     return spawnProjectile(paramProjectileFactory.create(paramServerLevel, paramLivingEntity, paramItemStack), paramServerLevel, paramItemStack, paramProjectile -> paramProjectile.shoot(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2));
/*     */   }
/*     */   
/*     */   public static <T extends Projectile> T spawnProjectileUsingShoot(T paramT, ServerLevel paramServerLevel, ItemStack paramItemStack, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {
/* 195 */     return spawnProjectile(paramT, paramServerLevel, paramItemStack, paramProjectile2 -> paramProjectile1.shoot(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2));
/*     */   }
/*     */   
/*     */   public static <T extends Projectile> T spawnProjectile(T paramT, ServerLevel paramServerLevel, ItemStack paramItemStack) {
/* 199 */     return spawnProjectile(paramT, paramServerLevel, paramItemStack, paramProjectile -> {
/*     */         
/*     */         });
/*     */   } public static <T extends Projectile> T spawnProjectile(T paramT, ServerLevel paramServerLevel, ItemStack paramItemStack, Consumer<T> paramConsumer) {
/* 203 */     paramConsumer.accept(paramT);
/* 204 */     paramServerLevel.addFreshEntity((Entity)paramT);
/*     */     
/* 206 */     paramT.applyOnProjectileSpawned(paramServerLevel, paramItemStack);
/*     */     
/* 208 */     return paramT;
/*     */   }
/*     */   
/*     */   public void applyOnProjectileSpawned(ServerLevel paramServerLevel, ItemStack paramItemStack) {
/* 212 */     EnchantmentHelper.onProjectileSpawned(paramServerLevel, paramItemStack, this, paramItem -> { 
/* 213 */         }); Projectile projectile = this; if (projectile instanceof AbstractArrow) { AbstractArrow abstractArrow = (AbstractArrow)projectile;
/* 214 */       ItemStack itemStack = abstractArrow.getWeaponItem();
/* 215 */       if (itemStack != null && !itemStack.isEmpty() && !paramItemStack.getItem().equals(itemStack.getItem())) {
/* 216 */         Objects.requireNonNull(abstractArrow); EnchantmentHelper.onProjectileSpawned(paramServerLevel, itemStack, this, abstractArrow::onItemBreak);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   protected ProjectileDeflection hitTargetOrDeflectSelf(HitResult paramHitResult) {
/* 222 */     if (paramHitResult.getType() == HitResult.Type.ENTITY)
/* 223 */     { EntityHitResult entityHitResult = (EntityHitResult)paramHitResult;
/* 224 */       Entity entity = entityHitResult.getEntity();
/* 225 */       ProjectileDeflection projectileDeflection = entity.deflection(this);
/* 226 */       if (projectileDeflection != ProjectileDeflection.NONE) {
/* 227 */         if (entity != this.lastDeflectedBy && deflect(projectileDeflection, entity, this.owner, false)) {
/* 228 */           this.lastDeflectedBy = entity;
/*     */         }
/* 230 */         return projectileDeflection;
/*     */       }  }
/* 232 */     else if (shouldBounceOnWorldBorder() && paramHitResult instanceof BlockHitResult) { BlockHitResult blockHitResult = (BlockHitResult)paramHitResult; if (blockHitResult.isWorldBorderHit()) {
/* 233 */         ProjectileDeflection projectileDeflection = ProjectileDeflection.REVERSE;
/* 234 */         if (deflect(projectileDeflection, (Entity)null, this.owner, false)) {
/* 235 */           setDeltaMovement(getDeltaMovement().scale(0.2D));
/* 236 */           return projectileDeflection;
/*     */         } 
/*     */       }  }
/*     */     
/* 240 */     onHit(paramHitResult);
/* 241 */     return ProjectileDeflection.NONE;
/*     */   }
/*     */   
/*     */   protected boolean shouldBounceOnWorldBorder() {
/* 245 */     return false;
/*     */   }
/*     */   
/*     */   public boolean deflect(ProjectileDeflection paramProjectileDeflection, Entity paramEntity, EntityReference<Entity> paramEntityReference, boolean paramBoolean) {
/* 249 */     paramProjectileDeflection.deflect(this, paramEntity, this.random);
/* 250 */     if (!level().isClientSide()) {
/* 251 */       setOwner(paramEntityReference);
/* 252 */       onDeflection(paramBoolean);
/*     */     } 
/* 254 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDeflection(boolean paramBoolean) {}
/*     */ 
/*     */   
/*     */   protected void onItemBreak(Item paramItem) {}
/*     */   
/*     */   protected void onHit(HitResult paramHitResult) {
/* 264 */     HitResult.Type type = paramHitResult.getType();
/* 265 */     if (type == HitResult.Type.ENTITY) {
/* 266 */       EntityHitResult entityHitResult = (EntityHitResult)paramHitResult;
/* 267 */       Entity entity = entityHitResult.getEntity();
/* 268 */       if (entity.getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE) && entity instanceof Projectile) {
/* 269 */         Projectile projectile = (Projectile)entity;
/*     */         
/* 271 */         projectile.deflect(ProjectileDeflection.AIM_DEFLECT, getOwner(), this.owner, true);
/*     */       } 
/* 273 */       onHitEntity(entityHitResult);
/* 274 */       level().gameEvent((Holder)GameEvent.PROJECTILE_LAND, paramHitResult.getLocation(), GameEvent.Context.of(this, null));
/* 275 */     } else if (type == HitResult.Type.BLOCK) {
/* 276 */       BlockHitResult blockHitResult = (BlockHitResult)paramHitResult;
/* 277 */       onHitBlock(blockHitResult);
/* 278 */       BlockPos blockPos = blockHitResult.getBlockPos();
/* 279 */       level().gameEvent((Holder)GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Context.of(this, level().getBlockState(blockPos)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {}
/*     */   
/*     */   protected void onHitBlock(BlockHitResult paramBlockHitResult) {
/* 287 */     BlockState blockState = level().getBlockState(paramBlockHitResult.getBlockPos());
/* 288 */     blockState.onProjectileHit(level(), blockState, paramBlockHitResult, this);
/*     */   }
/*     */   
/*     */   protected boolean canHitEntity(Entity paramEntity) {
/* 292 */     if (!paramEntity.canBeHitByProjectile()) {
/* 293 */       return false;
/*     */     }
/* 295 */     Entity entity = getOwner();
/* 296 */     return (entity == null || this.leftOwner || !entity.isPassengerOfSameVehicle(paramEntity));
/*     */   }
/*     */   
/*     */   protected void updateRotation() {
/* 300 */     Vec3 vec3 = getDeltaMovement();
/* 301 */     double d = vec3.horizontalDistance();
/*     */     
/* 303 */     setXRot(lerpRotation(this.xRotO, (float)(Mth.atan2(vec3.y, d) * 57.2957763671875D)));
/* 304 */     setYRot(lerpRotation(this.yRotO, (float)(Mth.atan2(vec3.x, vec3.z) * 57.2957763671875D)));
/*     */   }
/*     */   
/*     */   protected static float lerpRotation(float paramFloat1, float paramFloat2) {
/* 308 */     while (paramFloat2 - paramFloat1 < -180.0F) {
/* 309 */       paramFloat1 -= 360.0F;
/*     */     }
/* 311 */     while (paramFloat2 - paramFloat1 >= 180.0F) {
/* 312 */       paramFloat1 += 360.0F;
/*     */     }
/* 314 */     return Mth.lerp(0.2F, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity paramServerEntity) {
/* 319 */     Entity entity = getOwner();
/* 320 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket(this, paramServerEntity, (entity == null) ? 0 : entity.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket paramClientboundAddEntityPacket) {
/* 325 */     super.recreateFromPacket(paramClientboundAddEntityPacket);
/* 326 */     Entity entity = level().getEntity(paramClientboundAddEntityPacket.getData());
/* 327 */     if (entity != null) {
/* 328 */       setOwner(entity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mayInteract(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 334 */     Entity entity = getOwner();
/* 335 */     if (entity instanceof net.minecraft.world.entity.player.Player) {
/* 336 */       return entity.mayInteract(paramServerLevel, paramBlockPos);
/*     */     }
/* 338 */     return (entity == null || ((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue());
/*     */   }
/*     */   
/*     */   public boolean mayBreak(ServerLevel paramServerLevel) {
/* 342 */     return (getType().is(EntityTypeTags.IMPACT_PROJECTILES) && ((Boolean)paramServerLevel.getGameRules().get(GameRules.PROJECTILES_CAN_BREAK_BLOCKS)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 347 */     return getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPickRadius() {
/* 352 */     return isPickable() ? 1.0F : 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleDoubleImmutablePair calculateHorizontalHurtKnockbackDirection(LivingEntity paramLivingEntity, DamageSource paramDamageSource) {
/* 358 */     double d1 = (getDeltaMovement()).x;
/* 359 */     double d2 = (getDeltaMovement()).z;
/* 360 */     return DoubleDoubleImmutablePair.of(d1, d2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDimensionChangingDelay() {
/* 365 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 370 */     if (!isInvulnerableToBase(paramDamageSource)) {
/* 371 */       markHurt();
/*     */     }
/*     */     
/* 374 */     return false;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ProjectileFactory<T extends Projectile> {
/*     */     T create(ServerLevel param1ServerLevel, LivingEntity param1LivingEntity, ItemStack param1ItemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\Projectile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */