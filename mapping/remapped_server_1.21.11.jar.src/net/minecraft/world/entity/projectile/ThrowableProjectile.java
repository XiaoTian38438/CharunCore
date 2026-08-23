/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class ThrowableProjectile
/*     */   extends Projectile {
/*     */   protected ThrowableProjectile(EntityType<? extends ThrowableProjectile> paramEntityType, Level paramLevel) {
/*  17 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
/*     */   protected ThrowableProjectile(EntityType<? extends ThrowableProjectile> paramEntityType, double paramDouble1, double paramDouble2, double paramDouble3, Level paramLevel) {
/*  21 */     this(paramEntityType, paramLevel);
/*  22 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/*  27 */     if (this.tickCount < 2 && paramDouble < 12.25D) {
/*  28 */       return false;
/*     */     }
/*  30 */     double d = getBoundingBox().getSize() * 4.0D;
/*  31 */     if (Double.isNaN(d)) {
/*  32 */       d = 4.0D;
/*     */     }
/*  34 */     d *= 64.0D;
/*  35 */     return (paramDouble < d * d);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUsePortal(boolean paramBoolean) {
/*  40 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     Vec3 vec3;
/*  45 */     handleFirstTickBubbleColumn();
/*     */     
/*  47 */     applyGravity();
/*  48 */     applyInertia();
/*     */     
/*  50 */     HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*     */ 
/*     */     
/*  53 */     if (hitResult.getType() != HitResult.Type.MISS) {
/*  54 */       vec3 = hitResult.getLocation();
/*     */     } else {
/*  56 */       vec3 = position().add(getDeltaMovement());
/*     */     } 
/*     */     
/*  59 */     setPos(vec3);
/*  60 */     updateRotation();
/*  61 */     applyEffectsFromBlocks();
/*  62 */     super.tick();
/*     */     
/*  64 */     if (hitResult.getType() != HitResult.Type.MISS && isAlive())
/*     */     {
/*     */       
/*  67 */       hitTargetOrDeflectSelf(hitResult); } 
/*     */   }
/*     */   
/*     */   private void applyInertia() {
/*     */     float f;
/*  72 */     Vec3 vec31 = getDeltaMovement();
/*  73 */     Vec3 vec32 = position();
/*     */     
/*  75 */     if (isInWater()) {
/*  76 */       for (byte b = 0; b < 4; b++) {
/*  77 */         float f1 = 0.25F;
/*  78 */         level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, vec32.x - vec31.x * 0.25D, vec32.y - vec31.y * 0.25D, vec32.z - vec31.z * 0.25D, vec31.x, vec31.y, vec31.z);
/*     */       } 
/*  80 */       f = 0.8F;
/*     */     } else {
/*  82 */       f = 0.99F;
/*     */     } 
/*     */     
/*  85 */     setDeltaMovement(vec31.scale(f));
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleFirstTickBubbleColumn() {
/*  90 */     if (this.firstTick) {
/*  91 */       for (BlockPos blockPos : BlockPos.betweenClosed(getBoundingBox())) {
/*  92 */         BlockState blockState = level().getBlockState(blockPos);
/*  93 */         if (blockState.is(Blocks.BUBBLE_COLUMN)) {
/*  94 */           blockState.entityInside(level(), blockPos, this, InsideBlockEffectApplier.NOOP, true);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/* 102 */     return 0.03D;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\ThrowableProjectile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */