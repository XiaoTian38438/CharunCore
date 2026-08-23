/*     */ package net.minecraft.world.entity.projectile.hurtingprojectile.windcharge;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.ItemSupplier;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.SimpleExplosionDamageCalculator;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractWindCharge
/*     */   extends AbstractHurtingProjectile
/*     */   implements ItemSupplier {
/*  30 */   public static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = (ExplosionDamageCalculator)new SimpleExplosionDamageCalculator(true, false, 
/*     */ 
/*     */       
/*  33 */       Optional.empty(), BuiltInRegistries.BLOCK
/*  34 */       .get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
/*     */   
/*     */   public static final double JUMP_SCALE = 0.25D;
/*     */ 
/*     */   
/*     */   public AbstractWindCharge(EntityType<? extends AbstractWindCharge> paramEntityType, Level paramLevel) {
/*  40 */     super(paramEntityType, paramLevel);
/*  41 */     this.accelerationPower = 0.0D;
/*     */   }
/*     */   
/*     */   public AbstractWindCharge(EntityType<? extends AbstractWindCharge> paramEntityType, Level paramLevel, Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3) {
/*  45 */     super(paramEntityType, paramDouble1, paramDouble2, paramDouble3, paramLevel);
/*  46 */     setOwner(paramEntity);
/*  47 */     this.accelerationPower = 0.0D;
/*     */   }
/*     */   
/*     */   AbstractWindCharge(EntityType<? extends AbstractWindCharge> paramEntityType, double paramDouble1, double paramDouble2, double paramDouble3, Vec3 paramVec3, Level paramLevel) {
/*  51 */     super(paramEntityType, paramDouble1, paramDouble2, paramDouble3, paramVec3, paramLevel);
/*  52 */     this.accelerationPower = 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB makeBoundingBox(Vec3 paramVec3) {
/*  57 */     float f1 = getType().getDimensions().width() / 2.0F;
/*  58 */     float f2 = getType().getDimensions().height();
/*  59 */     float f3 = 0.15F;
/*     */     
/*  61 */     return new AABB(paramVec3.x - f1, paramVec3.y - 0.15000000596046448D, paramVec3.z - f1, paramVec3.x + f1, paramVec3.y - 0.15000000596046448D + f2, paramVec3.z + f1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCollideWith(Entity paramEntity) {
/*  66 */     if (paramEntity instanceof AbstractWindCharge) {
/*  67 */       return false;
/*     */     }
/*  69 */     return super.canCollideWith(paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHitEntity(Entity paramEntity) {
/*  74 */     if (paramEntity instanceof AbstractWindCharge) {
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     if (paramEntity.getType() == EntityType.END_CRYSTAL) {
/*  79 */       return false;
/*     */     }
/*     */     
/*  82 */     return super.canHitEntity(paramEntity);
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/*     */     ServerLevel serverLevel;
/*  87 */     super.onHitEntity(paramEntityHitResult);
/*  88 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/*     */     
/*  92 */     Entity entity2 = getOwner(); LivingEntity livingEntity2 = (LivingEntity)entity2, livingEntity1 = (entity2 instanceof LivingEntity) ? livingEntity2 : null;
/*  93 */     Entity entity1 = paramEntityHitResult.getEntity();
/*     */     
/*  95 */     if (livingEntity1 != null) {
/*  96 */       livingEntity1.setLastHurtMob(entity1);
/*     */     }
/*     */     
/*  99 */     DamageSource damageSource = damageSources().windCharge((Entity)this, livingEntity1);
/* 100 */     if (entity1.hurtServer(serverLevel, damageSource, 1.0F) && 
/* 101 */       entity1 instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity1;
/* 102 */       EnchantmentHelper.doPostAttackEffects(serverLevel, (Entity)livingEntity, damageSource); }
/*     */ 
/*     */     
/* 105 */     explode(position());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void push(double paramDouble1, double paramDouble2, double paramDouble3) {}
/*     */ 
/*     */   
/*     */   protected abstract void explode(Vec3 paramVec3);
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult paramBlockHitResult) {
/* 117 */     super.onHitBlock(paramBlockHitResult);
/*     */     
/* 119 */     if (!level().isClientSide()) {
/* 120 */       Vec3i vec3i = paramBlockHitResult.getDirection().getUnitVec3i();
/* 121 */       Vec3 vec31 = Vec3.atLowerCornerOf(vec3i).multiply(0.25D, 0.25D, 0.25D);
/* 122 */       Vec3 vec32 = paramBlockHitResult.getLocation().add(vec31);
/*     */       
/* 124 */       explode(vec32);
/* 125 */       discard();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult paramHitResult) {
/* 131 */     super.onHit(paramHitResult);
/* 132 */     if (!level().isClientSide()) {
/* 133 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldBurn() {
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem() {
/* 144 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected float getInertia() {
/* 151 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getLiquidInertia() {
/* 156 */     return getInertia();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ParticleOptions getTrailParticle() {
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 166 */     if (!level().isClientSide() && getBlockY() > level().getMaxY() + 30) {
/* 167 */       explode(position());
/* 168 */       discard();
/*     */     } else {
/* 170 */       super.tick();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\windcharge\AbstractWindCharge.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */