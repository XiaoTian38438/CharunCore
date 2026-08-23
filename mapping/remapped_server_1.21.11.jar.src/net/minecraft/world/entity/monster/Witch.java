/*     */ package net.minecraft.world.entity.monster;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Witch extends Raider implements RangedAttackMob {
/*  48 */   private static final Identifier SPEED_MODIFIER_DRINKING_ID = Identifier.withDefaultNamespace("drinking");
/*  49 */   private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(SPEED_MODIFIER_DRINKING_ID, -0.25D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*  51 */   private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(Witch.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private int usingTime;
/*     */   
/*     */   private NearestHealableRaiderTargetGoal<Raider> healRaidersGoal;
/*     */   private NearestAttackableWitchTargetGoal<Player> attackPlayersGoal;
/*     */   
/*     */   public Witch(EntityType<? extends Witch> paramEntityType, Level paramLevel) {
/*  59 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  64 */     super.registerGoals();
/*     */ 
/*     */     
/*  67 */     this.healRaidersGoal = new NearestHealableRaiderTargetGoal(this, Raider.class, true, (paramLivingEntity, paramServerLevel) -> (hasActiveRaid() && paramLivingEntity.getType() != EntityType.WITCH));
/*  68 */     this.attackPlayersGoal = new NearestAttackableWitchTargetGoal(this, Player.class, 10, true, false, null);
/*     */     
/*  70 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*  71 */     this.goalSelector.addGoal(2, (Goal)new RangedAttackGoal(this, 1.0D, 60, 10.0F));
/*  72 */     this.goalSelector.addGoal(2, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  73 */     this.goalSelector.addGoal(3, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  74 */     this.goalSelector.addGoal(3, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  76 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class }));
/*  77 */     this.targetSelector.addGoal(2, (Goal)this.healRaidersGoal);
/*  78 */     this.targetSelector.addGoal(3, (Goal)this.attackPlayersGoal);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  83 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  85 */     paramBuilder.define(DATA_USING_ITEM, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  90 */     return SoundEvents.WITCH_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  95 */     return SoundEvents.WITCH_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 100 */     return SoundEvents.WITCH_DEATH;
/*     */   }
/*     */   
/*     */   public void setUsingItem(boolean paramBoolean) {
/* 104 */     getEntityData().set(DATA_USING_ITEM, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   public boolean isDrinkingPotion() {
/* 108 */     return ((Boolean)getEntityData().get(DATA_USING_ITEM)).booleanValue();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 112 */     return Monster.createMonsterAttributes()
/* 113 */       .add(Attributes.MAX_HEALTH, 26.0D)
/* 114 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 119 */     if (!level().isClientSide() && isAlive()) {
/* 120 */       this.healRaidersGoal.decrementCooldown();
/*     */       
/* 122 */       if (this.healRaidersGoal.getCooldown() <= 0) {
/* 123 */         this.attackPlayersGoal.setCanAttack(true);
/*     */       } else {
/* 125 */         this.attackPlayersGoal.setCanAttack(false);
/*     */       } 
/*     */       
/* 128 */       if (isDrinkingPotion()) {
/* 129 */         if (this.usingTime-- <= 0) {
/* 130 */           setUsingItem(false);
/* 131 */           ItemStack itemStack = getMainHandItem();
/* 132 */           setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*     */           
/* 134 */           PotionContents potionContents = (PotionContents)itemStack.get(DataComponents.POTION_CONTENTS);
/* 135 */           if (itemStack.is(Items.POTION) && potionContents != null) {
/* 136 */             potionContents.forEachEffect(this::addEffect, ((Float)itemStack.getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue());
/*     */           }
/* 138 */           gameEvent((Holder)GameEvent.DRINK);
/* 139 */           getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING.id());
/*     */         } 
/*     */       } else {
/* 142 */         Holder holder = null;
/*     */         
/* 144 */         if (this.random.nextFloat() < 0.15F && isEyeInFluid(FluidTags.WATER) && !hasEffect(MobEffects.WATER_BREATHING)) {
/* 145 */           holder = Potions.WATER_BREATHING;
/* 146 */         } else if (this.random.nextFloat() < 0.15F && (isOnFire() || (getLastDamageSource() != null && getLastDamageSource().is(DamageTypeTags.IS_FIRE))) && !hasEffect(MobEffects.FIRE_RESISTANCE)) {
/* 147 */           holder = Potions.FIRE_RESISTANCE;
/* 148 */         } else if (this.random.nextFloat() < 0.05F && getHealth() < getMaxHealth()) {
/* 149 */           holder = Potions.HEALING;
/* 150 */         } else if (this.random.nextFloat() < 0.5F && getTarget() != null && !hasEffect(MobEffects.SPEED) && getTarget().distanceToSqr((Entity)this) > 121.0D) {
/* 151 */           holder = Potions.SWIFTNESS;
/*     */         } 
/*     */         
/* 154 */         if (holder != null) {
/* 155 */           setItemSlot(EquipmentSlot.MAINHAND, PotionContents.createItemStack(Items.POTION, holder));
/* 156 */           this.usingTime = getMainHandItem().getUseDuration((LivingEntity)this);
/* 157 */           setUsingItem(true);
/* 158 */           if (!isSilent()) {
/* 159 */             level().playSound(null, getX(), getY(), getZ(), SoundEvents.WITCH_DRINK, getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
/*     */           }
/* 161 */           AttributeInstance attributeInstance = getAttribute(Attributes.MOVEMENT_SPEED);
/* 162 */           attributeInstance.removeModifier(SPEED_MODIFIER_DRINKING_ID);
/* 163 */           attributeInstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
/*     */         } 
/*     */       } 
/*     */       
/* 167 */       if (this.random.nextFloat() < 7.5E-4F) {
/* 168 */         level().broadcastEntityEvent((Entity)this, (byte)15);
/*     */       }
/*     */     } 
/*     */     
/* 172 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 177 */     return SoundEvents.WITCH_CELEBRATE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 182 */     if (paramByte == 15) {
/* 183 */       for (byte b = 0; b < this.random.nextInt(35) + 10; b++) {
/* 184 */         level().addParticle((ParticleOptions)ParticleTypes.WITCH, getX() + this.random.nextGaussian() * 0.12999999523162842D, (getBoundingBox()).maxY + 0.5D + this.random.nextGaussian() * 0.12999999523162842D, getZ() + this.random.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
/*     */       }
/*     */     } else {
/* 187 */       super.handleEntityEvent(paramByte);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getDamageAfterMagicAbsorb(DamageSource paramDamageSource, float paramFloat) {
/* 193 */     paramFloat = super.getDamageAfterMagicAbsorb(paramDamageSource, paramFloat);
/*     */     
/* 195 */     if (paramDamageSource.getEntity() == this) {
/* 196 */       paramFloat = 0.0F;
/*     */     }
/* 198 */     if (paramDamageSource.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
/* 199 */       paramFloat *= 0.15F;
/*     */     }
/*     */     
/* 202 */     return paramFloat;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity paramLivingEntity, float paramFloat) {
/* 207 */     if (isDrinkingPotion()) {
/*     */       return;
/*     */     }
/*     */     
/* 211 */     Vec3 vec3 = paramLivingEntity.getDeltaMovement();
/* 212 */     double d1 = paramLivingEntity.getX() + vec3.x - getX();
/* 213 */     double d2 = paramLivingEntity.getEyeY() - 1.100000023841858D - getY();
/* 214 */     double d3 = paramLivingEntity.getZ() + vec3.z - getZ();
/* 215 */     double d4 = Math.sqrt(d1 * d1 + d3 * d3);
/* 216 */     Holder holder = Potions.HARMING;
/*     */ 
/*     */     
/* 219 */     if (paramLivingEntity instanceof Raider) {
/* 220 */       if (paramLivingEntity.getHealth() <= 4.0F) {
/* 221 */         holder = Potions.HEALING;
/*     */       } else {
/* 223 */         holder = Potions.REGENERATION;
/*     */       } 
/* 225 */       setTarget(null);
/*     */     }
/* 227 */     else if (d4 >= 8.0D && !paramLivingEntity.hasEffect(MobEffects.SLOWNESS)) {
/* 228 */       holder = Potions.SLOWNESS;
/* 229 */     } else if (paramLivingEntity.getHealth() >= 8.0F && !paramLivingEntity.hasEffect(MobEffects.POISON)) {
/* 230 */       holder = Potions.POISON;
/* 231 */     } else if (d4 <= 3.0D && !paramLivingEntity.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
/* 232 */       holder = Potions.WEAKNESS;
/*     */     } 
/*     */ 
/*     */     
/* 236 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 237 */       ItemStack itemStack = PotionContents.createItemStack(Items.SPLASH_POTION, holder);
/* 238 */       Projectile.spawnProjectileUsingShoot(net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion::new, serverLevel, itemStack, (LivingEntity)this, d1, d2 + d4 * 0.2D, d3, 0.75F, 8.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     if (!isSilent()) {
/* 246 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.WITCH_THROW, getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel paramServerLevel, int paramInt, boolean paramBoolean) {}
/*     */ 
/*     */   
/*     */   public boolean canBeLeader() {
/* 256 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Witch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */