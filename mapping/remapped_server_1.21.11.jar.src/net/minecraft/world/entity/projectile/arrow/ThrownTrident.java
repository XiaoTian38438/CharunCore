/*     */ package net.minecraft.world.entity.projectile.arrow;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ThrownTrident
/*     */   extends AbstractArrow
/*     */ {
/*  33 */   private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BYTE);
/*  34 */   private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float WATER_INERTIA = 0.99F;
/*     */   
/*     */   private static final boolean DEFAULT_DEALT_DAMAGE = false;
/*     */   private boolean dealtDamage = false;
/*     */   public int clientSideReturnTridentTickCount;
/*     */   
/*     */   public ThrownTrident(EntityType<? extends ThrownTrident> paramEntityType, Level paramLevel) {
/*  43 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public ThrownTrident(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*  47 */     super(EntityType.TRIDENT, paramLivingEntity, paramLevel, paramItemStack, (ItemStack)null);
/*  48 */     this.entityData.set(ID_LOYALTY, Byte.valueOf(getLoyaltyFromItem(paramItemStack)));
/*  49 */     this.entityData.set(ID_FOIL, Boolean.valueOf(paramItemStack.hasFoil()));
/*     */   }
/*     */   
/*     */   public ThrownTrident(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/*  53 */     super(EntityType.TRIDENT, paramDouble1, paramDouble2, paramDouble3, paramLevel, paramItemStack, paramItemStack);
/*  54 */     this.entityData.set(ID_LOYALTY, Byte.valueOf(getLoyaltyFromItem(paramItemStack)));
/*  55 */     this.entityData.set(ID_FOIL, Boolean.valueOf(paramItemStack.hasFoil()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  60 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  62 */     paramBuilder.define(ID_LOYALTY, Byte.valueOf((byte)0));
/*  63 */     paramBuilder.define(ID_FOIL, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  68 */     if (this.inGroundTime > 4) {
/*  69 */       this.dealtDamage = true;
/*     */     }
/*     */     
/*  72 */     Entity entity = getOwner();
/*  73 */     byte b = ((Byte)this.entityData.get(ID_LOYALTY)).byteValue();
/*     */     
/*  75 */     if (b > 0 && (this.dealtDamage || isNoPhysics()) && entity != null) {
/*  76 */       if (!isAcceptibleReturnOwner()) {
/*  77 */         Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (this.pickup == AbstractArrow.Pickup.ALLOWED)
/*  78 */             spawnAtLocation(serverLevel, getPickupItem(), 0.1F);  }
/*     */         
/*  80 */         discard();
/*     */       } else {
/*  82 */         if (!(entity instanceof Player) && position().distanceTo(entity.getEyePosition()) < entity.getBbWidth() + 1.0D) {
/*  83 */           discard();
/*     */           return;
/*     */         } 
/*  86 */         setNoPhysics(true);
/*  87 */         Vec3 vec3 = entity.getEyePosition().subtract(position());
/*  88 */         setPosRaw(getX(), getY() + vec3.y * 0.015D * b, getZ());
/*  89 */         double d = 0.05D * b;
/*  90 */         setDeltaMovement(getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d)));
/*     */         
/*  92 */         if (this.clientSideReturnTridentTickCount == 0) {
/*  93 */           playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
/*     */         }
/*     */         
/*  96 */         this.clientSideReturnTridentTickCount++;
/*     */       } 
/*     */     }
/*     */     
/* 100 */     super.tick();
/*     */   }
/*     */   
/*     */   private boolean isAcceptibleReturnOwner() {
/* 104 */     Entity entity = getOwner();
/* 105 */     if (entity == null || !entity.isAlive()) {
/* 106 */       return false;
/*     */     }
/* 108 */     if (entity instanceof net.minecraft.server.level.ServerPlayer && entity.isSpectator()) {
/* 109 */       return false;
/*     */     }
/* 111 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isFoil() {
/* 115 */     return ((Boolean)this.entityData.get(ID_FOIL)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntityHitResult findHitEntity(Vec3 paramVec31, Vec3 paramVec32) {
/* 120 */     if (this.dealtDamage) {
/* 121 */       return null;
/*     */     }
/* 123 */     return super.findHitEntity(paramVec31, paramVec32);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Collection<EntityHitResult> findHitEntities(Vec3 paramVec31, Vec3 paramVec32) {
/* 128 */     EntityHitResult entityHitResult = findHitEntity(paramVec31, paramVec32);
/* 129 */     if (entityHitResult != null) {
/* 130 */       return List.of(entityHitResult);
/*     */     }
/* 132 */     return List.of();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/* 137 */     Entity entity1 = paramEntityHitResult.getEntity();
/* 138 */     float f = 8.0F;
/*     */     
/* 140 */     Entity entity2 = getOwner();
/* 141 */     DamageSource damageSource = damageSources().trident((Entity)this, (entity2 == null) ? (Entity)this : entity2);
/* 142 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 143 */       f = EnchantmentHelper.modifyDamage(serverLevel, getWeaponItem(), entity1, damageSource, f); }
/*     */ 
/*     */     
/* 146 */     this.dealtDamage = true;
/*     */     
/* 148 */     if (entity1.hurtOrSimulate(damageSource, f)) {
/* 149 */       if (entity1.getType() == EntityType.ENDERMAN) {
/*     */         return;
/*     */       }
/* 152 */       level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 153 */         EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverLevel, entity1, damageSource, getWeaponItem(), paramItem -> kill(paramServerLevel)); }
/*     */       
/* 155 */       if (entity1 instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity1;
/* 156 */         doKnockback(livingEntity, damageSource);
/* 157 */         doPostHurtEffects(livingEntity); }
/*     */     
/*     */     } 
/* 160 */     deflect(ProjectileDeflection.REVERSE, entity1, this.owner, false);
/* 161 */     setDeltaMovement(getDeltaMovement().multiply(0.02D, 0.2D, 0.02D));
/*     */     
/* 163 */     playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void hitBlockEnchantmentEffects(ServerLevel paramServerLevel, BlockHitResult paramBlockHitResult, ItemStack paramItemStack) {
/* 168 */     Vec3 vec3 = paramBlockHitResult.getBlockPos().clampLocationWithin(paramBlockHitResult.getLocation());
/* 169 */     Entity entity = getOwner(); LivingEntity livingEntity = (LivingEntity)entity; EnchantmentHelper.onHitBlock(paramServerLevel, paramItemStack, (entity instanceof LivingEntity) ? livingEntity : null, (Entity)this, null, vec3, paramServerLevel.getBlockState(paramBlockHitResult.getBlockPos()), paramItem -> kill(paramServerLevel));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getWeaponItem() {
/* 174 */     return getPickupItemStackOrigin();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean tryPickup(Player paramPlayer) {
/* 179 */     return (super.tryPickup(paramPlayer) || (isNoPhysics() && ownedBy((Entity)paramPlayer) && paramPlayer.getInventory().add(getPickupItem())));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getDefaultPickupItem() {
/* 184 */     return new ItemStack((ItemLike)Items.TRIDENT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDefaultHitGroundSoundEvent() {
/* 189 */     return SoundEvents.TRIDENT_HIT_GROUND;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player paramPlayer) {
/* 194 */     if (ownedBy((Entity)paramPlayer) || getOwner() == null) {
/* 195 */       super.playerTouch(paramPlayer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 201 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/* 203 */     this.dealtDamage = paramValueInput.getBooleanOr("DealtDamage", false);
/*     */     
/* 205 */     this.entityData.set(ID_LOYALTY, Byte.valueOf(getLoyaltyFromItem(getPickupItemStackOrigin())));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 210 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 212 */     paramValueOutput.putBoolean("DealtDamage", this.dealtDamage);
/*     */   }
/*     */   
/*     */   private byte getLoyaltyFromItem(ItemStack paramItemStack) {
/* 216 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 217 */       return (byte)Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverLevel, paramItemStack, (Entity)this), 0, 127); }
/*     */     
/* 219 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tickDespawn() {
/* 224 */     byte b = ((Byte)this.entityData.get(ID_LOYALTY)).byteValue();
/*     */     
/* 226 */     if (this.pickup != AbstractArrow.Pickup.ALLOWED || b <= 0) {
/* 227 */       super.tickDespawn();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getWaterInertia() {
/* 233 */     return 0.99F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRender(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 238 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\arrow\ThrownTrident.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */