/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.AreaEffectCloud;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SwellGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.animal.feline.Ocelot;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class Creeper
/*     */   extends Monster {
/*  48 */   private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.INT);
/*  49 */   private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
/*  50 */   private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_IGNITED = false;
/*     */   
/*     */   private static final boolean DEFAULT_POWERED = false;
/*     */   private static final short DEFAULT_MAX_SWELL = 30;
/*     */   private static final byte DEFAULT_EXPLOSION_RADIUS = 3;
/*     */   private int oldSwell;
/*     */   private int swell;
/*  59 */   private int maxSwell = 30;
/*  60 */   private int explosionRadius = 3;
/*     */   private boolean droppedSkulls;
/*     */   
/*     */   public Creeper(EntityType<? extends Creeper> paramEntityType, Level paramLevel) {
/*  64 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  69 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*  70 */     this.goalSelector.addGoal(2, (Goal)new SwellGoal(this));
/*  71 */     this.goalSelector.addGoal(3, (Goal)new AvoidEntityGoal(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
/*  72 */     this.goalSelector.addGoal(3, (Goal)new AvoidEntityGoal(this, Cat.class, 6.0F, 1.0D, 1.2D));
/*  73 */     this.goalSelector.addGoal(4, (Goal)new MeleeAttackGoal(this, 1.0D, false));
/*  74 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal(this, 0.8D));
/*  75 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  76 */     this.goalSelector.addGoal(6, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  78 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  79 */     this.targetSelector.addGoal(2, (Goal)new HurtByTargetGoal(this, new Class[0]));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  83 */     return Monster.createMonsterAttributes()
/*  84 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxFallDistance() {
/*  89 */     if (getTarget() == null) {
/*  90 */       return getComfortableFallDistance(0.0F);
/*     */     }
/*     */     
/*  93 */     return getComfortableFallDistance(getHealth() - 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(double paramDouble, float paramFloat, DamageSource paramDamageSource) {
/*  98 */     boolean bool = super.causeFallDamage(paramDouble, paramFloat, paramDamageSource);
/*     */     
/* 100 */     this.swell += (int)(paramDouble * 1.5D);
/* 101 */     if (this.swell > this.maxSwell - 5) {
/* 102 */       this.swell = this.maxSwell - 5;
/*     */     }
/* 104 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 109 */     super.defineSynchedData(paramBuilder);
/*     */     
/* 111 */     paramBuilder.define(DATA_SWELL_DIR, Integer.valueOf(-1));
/* 112 */     paramBuilder.define(DATA_IS_POWERED, Boolean.valueOf(false));
/* 113 */     paramBuilder.define(DATA_IS_IGNITED, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 118 */     super.addAdditionalSaveData(paramValueOutput);
/* 119 */     paramValueOutput.putBoolean("powered", isPowered());
/* 120 */     paramValueOutput.putShort("Fuse", (short)this.maxSwell);
/* 121 */     paramValueOutput.putByte("ExplosionRadius", (byte)this.explosionRadius);
/* 122 */     paramValueOutput.putBoolean("ignited", isIgnited());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 127 */     super.readAdditionalSaveData(paramValueInput);
/* 128 */     this.entityData.set(DATA_IS_POWERED, Boolean.valueOf(paramValueInput.getBooleanOr("powered", false)));
/* 129 */     this.maxSwell = paramValueInput.getShortOr("Fuse", (short)30);
/* 130 */     this.explosionRadius = paramValueInput.getByteOr("ExplosionRadius", (byte)3);
/* 131 */     if (paramValueInput.getBooleanOr("ignited", false)) {
/* 132 */       ignite();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 138 */     if (isAlive()) {
/* 139 */       this.oldSwell = this.swell;
/*     */ 
/*     */       
/* 142 */       if (isIgnited()) {
/* 143 */         setSwellDir(1);
/*     */       }
/*     */       
/* 146 */       int i = getSwellDir();
/* 147 */       if (i > 0 && this.swell == 0) {
/* 148 */         playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
/* 149 */         gameEvent((Holder)GameEvent.PRIME_FUSE);
/*     */       } 
/* 151 */       this.swell += i;
/* 152 */       if (this.swell < 0) {
/* 153 */         this.swell = 0;
/*     */       }
/* 155 */       if (this.swell >= this.maxSwell) {
/* 156 */         this.swell = this.maxSwell;
/* 157 */         explodeCreeper();
/*     */       } 
/*     */     } 
/* 160 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTarget(LivingEntity paramLivingEntity) {
/* 165 */     if (paramLivingEntity instanceof net.minecraft.world.entity.animal.goat.Goat) {
/*     */       return;
/*     */     }
/*     */     
/* 169 */     super.setTarget(paramLivingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 174 */     return SoundEvents.CREEPER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 179 */     return SoundEvents.CREEPER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean killedEntity(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, DamageSource paramDamageSource) {
/* 184 */     if (shouldDropLoot(paramServerLevel) && isPowered() && !this.droppedSkulls) {
/* 185 */       paramLivingEntity.dropFromLootTable(paramServerLevel, paramDamageSource, false, BuiltInLootTables.CHARGED_CREEPER, paramItemStack -> {
/*     */             paramLivingEntity.spawnAtLocation(paramServerLevel, paramItemStack);
/*     */             this.droppedSkulls = true;
/*     */           });
/*     */     }
/* 190 */     return super.killedEntity(paramServerLevel, paramLivingEntity, paramDamageSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel paramServerLevel, Entity paramEntity) {
/* 195 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPowered() {
/* 199 */     return ((Boolean)this.entityData.get(DATA_IS_POWERED)).booleanValue();
/*     */   }
/*     */   
/*     */   public float getSwelling(float paramFloat) {
/* 203 */     return Mth.lerp(paramFloat, this.oldSwell, this.swell) / (this.maxSwell - 2);
/*     */   }
/*     */   
/*     */   public int getSwellDir() {
/* 207 */     return ((Integer)this.entityData.get(DATA_SWELL_DIR)).intValue();
/*     */   }
/*     */   
/*     */   public void setSwellDir(int paramInt) {
/* 211 */     this.entityData.set(DATA_SWELL_DIR, Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel paramServerLevel, LightningBolt paramLightningBolt) {
/* 216 */     super.thunderHit(paramServerLevel, paramLightningBolt);
/* 217 */     this.entityData.set(DATA_IS_POWERED, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 222 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 223 */     if (itemStack.is(ItemTags.CREEPER_IGNITERS)) {
/* 224 */       SoundEvent soundEvent = itemStack.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
/* 225 */       level().playSound((Entity)paramPlayer, getX(), getY(), getZ(), soundEvent, getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
/* 226 */       if (!level().isClientSide()) {
/* 227 */         ignite();
/* 228 */         if (!itemStack.isDamageableItem()) {
/* 229 */           itemStack.shrink(1);
/*     */         } else {
/* 231 */           itemStack.hurtAndBreak(1, (LivingEntity)paramPlayer, paramInteractionHand.asEquipmentSlot());
/*     */         } 
/*     */       } 
/* 234 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 237 */     return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */   }
/*     */   
/*     */   private void explodeCreeper() {
/* 241 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 242 */       float f = isPowered() ? 2.0F : 1.0F;
/* 243 */       this.dead = true;
/* 244 */       serverLevel.explode((Entity)this, getX(), getY(), getZ(), this.explosionRadius * f, Level.ExplosionInteraction.MOB);
/* 245 */       spawnLingeringCloud();
/* 246 */       triggerOnDeathMobEffects(serverLevel, Entity.RemovalReason.KILLED);
/* 247 */       discard(); }
/*     */   
/*     */   }
/*     */   
/*     */   private void spawnLingeringCloud() {
/* 252 */     Collection collection = getActiveEffects();
/* 253 */     if (!collection.isEmpty()) {
/* 254 */       AreaEffectCloud areaEffectCloud = new AreaEffectCloud(level(), getX(), getY(), getZ());
/* 255 */       areaEffectCloud.setRadius(2.5F);
/* 256 */       areaEffectCloud.setRadiusOnUse(-0.5F);
/* 257 */       areaEffectCloud.setWaitTime(10);
/* 258 */       areaEffectCloud.setDuration(300);
/* 259 */       areaEffectCloud.setPotionDurationScale(0.25F);
/* 260 */       areaEffectCloud.setRadiusPerTick(-areaEffectCloud.getRadius() / areaEffectCloud.getDuration());
/* 261 */       for (MobEffectInstance mobEffectInstance : collection) {
/* 262 */         areaEffectCloud.addEffect(new MobEffectInstance(mobEffectInstance));
/*     */       }
/* 264 */       level().addFreshEntity((Entity)areaEffectCloud);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isIgnited() {
/* 269 */     return ((Boolean)this.entityData.get(DATA_IS_IGNITED)).booleanValue();
/*     */   }
/*     */   
/*     */   public void ignite() {
/* 273 */     this.entityData.set(DATA_IS_IGNITED, Boolean.valueOf(true));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Creeper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */