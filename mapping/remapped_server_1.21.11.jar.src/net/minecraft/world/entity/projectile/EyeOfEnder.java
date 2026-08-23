/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EyeOfEnder
/*     */   extends Entity implements ItemSupplier {
/*     */   private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
/*     */   private static final float TOO_FAR_SIGNAL_HEIGHT = 8.0F;
/*     */   private static final float TOO_FAR_DISTANCE = 12.0F;
/*  28 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(EyeOfEnder.class, EntityDataSerializers.ITEM_STACK);
/*     */   
/*     */   private Vec3 target;
/*     */   private int life;
/*     */   private boolean surviveAfterDeath;
/*     */   
/*     */   public EyeOfEnder(EntityType<? extends EyeOfEnder> paramEntityType, Level paramLevel) {
/*  35 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public EyeOfEnder(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3) {
/*  39 */     this(EntityType.EYE_OF_ENDER, paramLevel);
/*     */     
/*  41 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack paramItemStack) {
/*  45 */     if (paramItemStack.isEmpty()) {
/*  46 */       getEntityData().set(DATA_ITEM_STACK, getDefaultItem());
/*     */     } else {
/*  48 */       getEntityData().set(DATA_ITEM_STACK, paramItemStack.copyWithCount(1));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem() {
/*  54 */     return (ItemStack)getEntityData().get(DATA_ITEM_STACK);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  59 */     paramBuilder.define(DATA_ITEM_STACK, getDefaultItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/*  64 */     if (this.tickCount < 2 && paramDouble < 12.25D) {
/*  65 */       return false;
/*     */     }
/*  67 */     double d = getBoundingBox().getSize() * 4.0D;
/*  68 */     if (Double.isNaN(d)) {
/*  69 */       d = 4.0D;
/*     */     }
/*  71 */     d *= 64.0D;
/*  72 */     return (paramDouble < d * d);
/*     */   }
/*     */   
/*     */   public void signalTo(Vec3 paramVec3) {
/*  76 */     Vec3 vec3 = paramVec3.subtract(position());
/*     */     
/*  78 */     double d = vec3.horizontalDistance();
/*  79 */     if (d > 12.0D) {
/*  80 */       this.target = position().add(vec3.x / d * 12.0D, 8.0D, vec3.z / d * 12.0D);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  86 */       this.target = paramVec3;
/*     */     } 
/*     */     
/*  89 */     this.life = 0;
/*  90 */     this.surviveAfterDeath = (this.random.nextInt(5) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  95 */     super.tick();
/*     */     
/*  97 */     Vec3 vec3 = position().add(getDeltaMovement());
/*  98 */     if (!level().isClientSide() && this.target != null) {
/*  99 */       setDeltaMovement(updateDeltaMovement(getDeltaMovement(), vec3, this.target));
/*     */     }
/*     */     
/* 102 */     if (level().isClientSide()) {
/* 103 */       Vec3 vec31 = vec3.subtract(getDeltaMovement().scale(0.25D));
/* 104 */       spawnParticles(vec31, getDeltaMovement());
/*     */     } 
/*     */     
/* 107 */     setPos(vec3);
/*     */     
/* 109 */     if (!level().isClientSide()) {
/* 110 */       this.life++;
/* 111 */       if (this.life > 80 && !level().isClientSide()) {
/* 112 */         playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
/* 113 */         discard();
/* 114 */         if (this.surviveAfterDeath) {
/* 115 */           level().addFreshEntity((Entity)new ItemEntity(level(), getX(), getY(), getZ(), getItem()));
/*     */         } else {
/* 117 */           level().levelEvent(2003, blockPosition(), 0);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnParticles(Vec3 paramVec31, Vec3 paramVec32) {
/* 124 */     if (isInWater()) {
/* 125 */       for (byte b = 0; b < 4; b++) {
/* 126 */         level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, paramVec31.x, paramVec31.y, paramVec31.z, paramVec32.x, paramVec32.y, paramVec32.z);
/*     */       }
/*     */     } else {
/* 129 */       level().addParticle((ParticleOptions)ParticleTypes.PORTAL, paramVec31.x + this.random
/* 130 */           .nextDouble() * 0.6D - 0.3D, paramVec31.y - 0.5D, paramVec31.z + this.random
/*     */           
/* 132 */           .nextDouble() * 0.6D - 0.3D, paramVec32.x, paramVec32.y, paramVec32.z);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Vec3 updateDeltaMovement(Vec3 paramVec31, Vec3 paramVec32, Vec3 paramVec33) {
/* 139 */     Vec3 vec3 = new Vec3(paramVec33.x - paramVec32.x, 0.0D, paramVec33.z - paramVec32.z);
/* 140 */     double d1 = vec3.length();
/* 141 */     double d2 = Mth.lerp(0.0025D, paramVec31.horizontalDistance(), d1);
/* 142 */     double d3 = paramVec31.y;
/* 143 */     if (d1 < 1.0D) {
/* 144 */       d2 *= 0.8D;
/* 145 */       d3 *= 0.8D;
/*     */     } 
/* 147 */     double d4 = (paramVec32.y - paramVec31.y < paramVec33.y) ? 1.0D : -1.0D;
/* 148 */     return vec3.scale(d2 / d1)
/* 149 */       .add(0.0D, d3 + (d4 - d3) * 0.015D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 154 */     paramValueOutput.store("Item", ItemStack.CODEC, getItem());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 159 */     setItem(paramValueInput.read("Item", ItemStack.CODEC).orElse(getDefaultItem()));
/*     */   }
/*     */   
/*     */   private ItemStack getDefaultItem() {
/* 163 */     return new ItemStack((ItemLike)Items.ENDER_EYE);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLightLevelDependentMagicValue() {
/* 168 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 178 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\EyeOfEnder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */