/*     */ package net.minecraft.world.entity.projectile;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LlamaSpit extends Projectile {
/*     */   public LlamaSpit(EntityType<? extends LlamaSpit> paramEntityType, Level paramLevel) {
/*  23 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public LlamaSpit(Level paramLevel, Llama paramLlama) {
/*  27 */     this(EntityType.LLAMA_SPIT, paramLevel);
/*  28 */     setOwner((Entity)paramLlama);
/*  29 */     setPos(paramLlama.getX() - (paramLlama.getBbWidth() + 1.0F) * 0.5D * Mth.sin((paramLlama.yBodyRot * 0.017453292F)), paramLlama.getEyeY() - 0.10000000149011612D, paramLlama.getZ() + (paramLlama.getBbWidth() + 1.0F) * 0.5D * Mth.cos((paramLlama.yBodyRot * 0.017453292F)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/*  34 */     return 0.06D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  39 */     super.tick();
/*     */     
/*  41 */     Vec3 vec3 = getDeltaMovement();
/*  42 */     HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*  43 */     hitTargetOrDeflectSelf(hitResult);
/*     */     
/*  45 */     double d1 = getX() + vec3.x;
/*  46 */     double d2 = getY() + vec3.y;
/*  47 */     double d3 = getZ() + vec3.z;
/*     */     
/*  49 */     updateRotation();
/*     */     
/*  51 */     float f = 0.99F;
/*     */     
/*  53 */     if (level().getBlockStates(getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
/*  54 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/*  58 */     if (isInWater()) {
/*  59 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/*  63 */     setDeltaMovement(vec3.scale(0.9900000095367432D));
/*  64 */     applyGravity();
/*     */     
/*  66 */     setPos(d1, d2, d3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/*  71 */     super.onHitEntity(paramEntityHitResult);
/*  72 */     Entity entity = getOwner(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/*  73 */       entity = paramEntityHitResult.getEntity();
/*  74 */       DamageSource damageSource = damageSources().spit(this, livingEntity);
/*  75 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  76 */         if (entity.hurtServer(serverLevel, damageSource, 1.0F)) {
/*  77 */           EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*     */         } }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult paramBlockHitResult) {
/*  85 */     super.onHitBlock(paramBlockHitResult);
/*     */     
/*  87 */     if (!level().isClientSide()) {
/*  88 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {}
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket paramClientboundAddEntityPacket) {
/*  98 */     super.recreateFromPacket(paramClientboundAddEntityPacket);
/*  99 */     Vec3 vec3 = paramClientboundAddEntityPacket.getMovement();
/*     */     
/* 101 */     for (byte b = 0; b < 7; b++) {
/* 102 */       double d = 0.4D + 0.1D * b;
/* 103 */       level().addParticle((ParticleOptions)ParticleTypes.SPIT, getX(), getY(), getZ(), vec3.x * d, vec3.y, vec3.z * d);
/*     */     } 
/*     */     
/* 106 */     setDeltaMovement(vec3);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\LlamaSpit.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */