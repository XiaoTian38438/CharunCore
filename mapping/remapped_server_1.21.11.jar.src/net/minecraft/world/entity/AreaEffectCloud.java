/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class AreaEffectCloud
/*     */   extends Entity
/*     */   implements TraceableEntity
/*     */ {
/*     */   private static final int TIME_BETWEEN_APPLICATIONS = 5;
/*  34 */   private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.FLOAT);
/*  35 */   private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.BOOLEAN);
/*  36 */   private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.PARTICLE);
/*     */   
/*     */   private static final float MAX_RADIUS = 32.0F;
/*     */   
/*     */   private static final int DEFAULT_AGE = 0;
/*     */   
/*     */   private static final int DEFAULT_DURATION_ON_USE = 0;
/*     */   
/*     */   private static final float DEFAULT_RADIUS_ON_USE = 0.0F;
/*     */   
/*     */   private static final float DEFAULT_RADIUS_PER_TICK = 0.0F;
/*     */   private static final float DEFAULT_POTION_DURATION_SCALE = 1.0F;
/*     */   private static final float MINIMAL_RADIUS = 0.5F;
/*     */   private static final float DEFAULT_RADIUS = 3.0F;
/*     */   public static final float DEFAULT_WIDTH = 6.0F;
/*     */   public static final float HEIGHT = 0.5F;
/*     */   public static final int INFINITE_DURATION = -1;
/*     */   public static final int DEFAULT_LINGERING_DURATION = 600;
/*     */   private static final int DEFAULT_WAIT_TIME = 20;
/*     */   private static final int DEFAULT_REAPPLICATION_DELAY = 20;
/*  56 */   private static final ColorParticleOption DEFAULT_PARTICLE = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1);
/*     */   
/*     */   private ParticleOptions customParticle;
/*  59 */   private PotionContents potionContents = PotionContents.EMPTY;
/*  60 */   private float potionDurationScale = 1.0F;
/*  61 */   private final Map<Entity, Integer> victims = Maps.newHashMap();
/*  62 */   private int duration = -1;
/*  63 */   private int waitTime = 20;
/*  64 */   private int reapplicationDelay = 20;
/*  65 */   private int durationOnUse = 0;
/*  66 */   private float radiusOnUse = 0.0F;
/*  67 */   private float radiusPerTick = 0.0F;
/*     */   private EntityReference<LivingEntity> owner;
/*     */   
/*     */   public AreaEffectCloud(EntityType<? extends AreaEffectCloud> paramEntityType, Level paramLevel) {
/*  71 */     super(paramEntityType, paramLevel);
/*  72 */     this.noPhysics = true;
/*     */   }
/*     */   
/*     */   public AreaEffectCloud(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3) {
/*  76 */     this(EntityType.AREA_EFFECT_CLOUD, paramLevel);
/*  77 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  82 */     paramBuilder.define(DATA_RADIUS, Float.valueOf(3.0F));
/*  83 */     paramBuilder.define(DATA_WAITING, Boolean.valueOf(false));
/*  84 */     paramBuilder.define(DATA_PARTICLE, DEFAULT_PARTICLE);
/*     */   }
/*     */   
/*     */   public void setRadius(float paramFloat) {
/*  88 */     if (!level().isClientSide()) {
/*  89 */       getEntityData().set(DATA_RADIUS, Float.valueOf(Mth.clamp(paramFloat, 0.0F, 32.0F)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshDimensions() {
/*  95 */     double d1 = getX();
/*  96 */     double d2 = getY();
/*  97 */     double d3 = getZ();
/*  98 */     super.refreshDimensions();
/*  99 */     setPos(d1, d2, d3);
/*     */   }
/*     */   
/*     */   public float getRadius() {
/* 103 */     return ((Float)getEntityData().get(DATA_RADIUS)).floatValue();
/*     */   }
/*     */   
/*     */   public void setPotionContents(PotionContents paramPotionContents) {
/* 107 */     this.potionContents = paramPotionContents;
/* 108 */     updateParticle();
/*     */   }
/*     */   
/*     */   public void setCustomParticle(ParticleOptions paramParticleOptions) {
/* 112 */     this.customParticle = paramParticleOptions;
/* 113 */     updateParticle();
/*     */   }
/*     */   
/*     */   public void setPotionDurationScale(float paramFloat) {
/* 117 */     this.potionDurationScale = paramFloat;
/*     */   }
/*     */   
/*     */   private void updateParticle() {
/* 121 */     if (this.customParticle != null) {
/* 122 */       this.entityData.set(DATA_PARTICLE, this.customParticle);
/*     */     } else {
/* 124 */       int i = ARGB.opaque(this.potionContents.getColor());
/* 125 */       this.entityData.set(DATA_PARTICLE, ColorParticleOption.create(DEFAULT_PARTICLE.getType(), i));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addEffect(MobEffectInstance paramMobEffectInstance) {
/* 130 */     setPotionContents(this.potionContents.withEffectAdded(paramMobEffectInstance));
/*     */   }
/*     */   
/*     */   public ParticleOptions getParticle() {
/* 134 */     return (ParticleOptions)getEntityData().get(DATA_PARTICLE);
/*     */   }
/*     */   
/*     */   protected void setWaiting(boolean paramBoolean) {
/* 138 */     getEntityData().set(DATA_WAITING, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   public boolean isWaiting() {
/* 142 */     return ((Boolean)getEntityData().get(DATA_WAITING)).booleanValue();
/*     */   }
/*     */   
/*     */   public int getDuration() {
/* 146 */     return this.duration;
/*     */   }
/*     */   
/*     */   public void setDuration(int paramInt) {
/* 150 */     this.duration = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 155 */     super.tick();
/* 156 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 157 */       serverTick(serverLevel); }
/*     */     else
/* 159 */     { clientTick(); }
/*     */   
/*     */   } private void clientTick() {
/*     */     int i;
/*     */     float f2;
/* 164 */     boolean bool = isWaiting();
/* 165 */     float f1 = getRadius();
/*     */     
/* 167 */     if (bool && this.random.nextBoolean()) {
/*     */       return;
/*     */     }
/* 170 */     ParticleOptions particleOptions = getParticle();
/*     */ 
/*     */ 
/*     */     
/* 174 */     if (bool) {
/* 175 */       i = 2;
/* 176 */       f2 = 0.2F;
/*     */     } else {
/* 178 */       i = Mth.ceil(3.1415927F * f1 * f1);
/* 179 */       f2 = f1;
/*     */     } 
/*     */     
/* 182 */     for (byte b = 0; b < i; b++) {
/* 183 */       float f3 = this.random.nextFloat() * 6.2831855F;
/* 184 */       float f4 = Mth.sqrt(this.random.nextFloat()) * f2;
/* 185 */       double d1 = getX() + (Mth.cos(f3) * f4);
/* 186 */       double d2 = getY();
/* 187 */       double d3 = getZ() + (Mth.sin(f3) * f4);
/*     */       
/* 189 */       if (particleOptions.getType() == ParticleTypes.ENTITY_EFFECT) {
/* 190 */         if (bool && this.random.nextBoolean()) {
/* 191 */           level().addAlwaysVisibleParticle((ParticleOptions)DEFAULT_PARTICLE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } else {
/* 193 */           level().addAlwaysVisibleParticle(particleOptions, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         }
/*     */       
/* 196 */       } else if (bool) {
/* 197 */         level().addAlwaysVisibleParticle(particleOptions, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */       } else {
/* 199 */         level().addAlwaysVisibleParticle(particleOptions, d1, d2, d3, (0.5D - this.random.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.random.nextDouble()) * 0.15D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void serverTick(ServerLevel paramServerLevel) {
/* 206 */     if (this.duration != -1 && this.tickCount - this.waitTime >= this.duration) {
/* 207 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/* 211 */     boolean bool1 = isWaiting();
/* 212 */     boolean bool2 = (this.tickCount < this.waitTime);
/* 213 */     if (bool1 != bool2) {
/* 214 */       setWaiting(bool2);
/*     */     }
/* 216 */     if (bool2) {
/*     */       return;
/*     */     }
/*     */     
/* 220 */     float f = getRadius();
/* 221 */     if (this.radiusPerTick != 0.0F) {
/* 222 */       f += this.radiusPerTick;
/* 223 */       if (f < 0.5F) {
/* 224 */         discard();
/*     */         return;
/*     */       } 
/* 227 */       setRadius(f);
/*     */     } 
/*     */     
/* 230 */     if (this.tickCount % 5 == 0) {
/* 231 */       this.victims.entrySet().removeIf(paramEntry -> (this.tickCount >= ((Integer)paramEntry.getValue()).intValue()));
/*     */       
/* 233 */       if (!this.potionContents.hasEffects()) {
/* 234 */         this.victims.clear();
/*     */       } else {
/* 236 */         ArrayList arrayList = new ArrayList();
/* 237 */         Objects.requireNonNull(arrayList); this.potionContents.forEachEffect(arrayList::add, this.potionDurationScale);
/*     */         
/* 239 */         List list = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox());
/* 240 */         if (!list.isEmpty()) {
/* 241 */           for (LivingEntity livingEntity : list) {
/* 242 */             Objects.requireNonNull(livingEntity); if (this.victims.containsKey(livingEntity) || !livingEntity.isAffectedByPotions() || arrayList.stream().noneMatch(livingEntity::canBeAffected)) {
/*     */               continue;
/*     */             }
/* 245 */             double d1 = livingEntity.getX() - getX();
/* 246 */             double d2 = livingEntity.getZ() - getZ();
/* 247 */             double d3 = d1 * d1 + d2 * d2;
/* 248 */             if (d3 <= (f * f)) {
/* 249 */               this.victims.put(livingEntity, Integer.valueOf(this.tickCount + this.reapplicationDelay));
/* 250 */               for (MobEffectInstance mobEffectInstance : arrayList) {
/* 251 */                 if (((MobEffect)mobEffectInstance.getEffect().value()).isInstantenous()) {
/* 252 */                   ((MobEffect)mobEffectInstance.getEffect().value()).applyInstantenousEffect(paramServerLevel, this, getOwner(), livingEntity, mobEffectInstance.getAmplifier(), 0.5D); continue;
/*     */                 } 
/* 254 */                 livingEntity.addEffect(new MobEffectInstance(mobEffectInstance), this);
/*     */               } 
/*     */               
/* 257 */               if (this.radiusOnUse != 0.0F) {
/* 258 */                 f += this.radiusOnUse;
/* 259 */                 if (f < 0.5F) {
/* 260 */                   discard();
/*     */                   return;
/*     */                 } 
/* 263 */                 setRadius(f);
/*     */               } 
/* 265 */               if (this.durationOnUse != 0 && this.duration != -1) {
/* 266 */                 this.duration += this.durationOnUse;
/* 267 */                 if (this.duration <= 0) {
/* 268 */                   discard();
/*     */                   return;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getRadiusOnUse() {
/* 280 */     return this.radiusOnUse;
/*     */   }
/*     */   
/*     */   public void setRadiusOnUse(float paramFloat) {
/* 284 */     this.radiusOnUse = paramFloat;
/*     */   }
/*     */   
/*     */   public float getRadiusPerTick() {
/* 288 */     return this.radiusPerTick;
/*     */   }
/*     */   
/*     */   public void setRadiusPerTick(float paramFloat) {
/* 292 */     this.radiusPerTick = paramFloat;
/*     */   }
/*     */   
/*     */   public int getDurationOnUse() {
/* 296 */     return this.durationOnUse;
/*     */   }
/*     */   
/*     */   public void setDurationOnUse(int paramInt) {
/* 300 */     this.durationOnUse = paramInt;
/*     */   }
/*     */   
/*     */   public int getWaitTime() {
/* 304 */     return this.waitTime;
/*     */   }
/*     */   
/*     */   public void setWaitTime(int paramInt) {
/* 308 */     this.waitTime = paramInt;
/*     */   }
/*     */   
/*     */   public void setOwner(LivingEntity paramLivingEntity) {
/* 312 */     this.owner = EntityReference.of(paramLivingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity getOwner() {
/* 317 */     return EntityReference.getLivingEntity(this.owner, level());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 322 */     this.tickCount = paramValueInput.getIntOr("Age", 0);
/* 323 */     this.duration = paramValueInput.getIntOr("Duration", -1);
/* 324 */     this.waitTime = paramValueInput.getIntOr("WaitTime", 20);
/* 325 */     this.reapplicationDelay = paramValueInput.getIntOr("ReapplicationDelay", 20);
/* 326 */     this.durationOnUse = paramValueInput.getIntOr("DurationOnUse", 0);
/* 327 */     this.radiusOnUse = paramValueInput.getFloatOr("RadiusOnUse", 0.0F);
/* 328 */     this.radiusPerTick = paramValueInput.getFloatOr("RadiusPerTick", 0.0F);
/* 329 */     setRadius(paramValueInput.getFloatOr("Radius", 3.0F));
/* 330 */     this.owner = EntityReference.read(paramValueInput, "Owner");
/*     */     
/* 332 */     setCustomParticle(paramValueInput.read("custom_particle", ParticleTypes.CODEC).orElse(null));
/* 333 */     setPotionContents(paramValueInput.read("potion_contents", PotionContents.CODEC).orElse(PotionContents.EMPTY));
/*     */     
/* 335 */     this.potionDurationScale = paramValueInput.getFloatOr("potion_duration_scale", 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 340 */     paramValueOutput.putInt("Age", this.tickCount);
/* 341 */     paramValueOutput.putInt("Duration", this.duration);
/* 342 */     paramValueOutput.putInt("WaitTime", this.waitTime);
/* 343 */     paramValueOutput.putInt("ReapplicationDelay", this.reapplicationDelay);
/* 344 */     paramValueOutput.putInt("DurationOnUse", this.durationOnUse);
/* 345 */     paramValueOutput.putFloat("RadiusOnUse", this.radiusOnUse);
/* 346 */     paramValueOutput.putFloat("RadiusPerTick", this.radiusPerTick);
/* 347 */     paramValueOutput.putFloat("Radius", getRadius());
/*     */     
/* 349 */     paramValueOutput.storeNullable("custom_particle", ParticleTypes.CODEC, this.customParticle);
/*     */     
/* 351 */     EntityReference.store(this.owner, paramValueOutput, "Owner");
/*     */     
/* 353 */     if (!this.potionContents.equals(PotionContents.EMPTY)) {
/* 354 */       paramValueOutput.store("potion_contents", PotionContents.CODEC, this.potionContents);
/*     */     }
/* 356 */     if (this.potionDurationScale != 1.0F) {
/* 357 */       paramValueOutput.putFloat("potion_duration_scale", this.potionDurationScale);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 363 */     if (DATA_RADIUS.equals(paramEntityDataAccessor)) {
/* 364 */       refreshDimensions();
/*     */     }
/* 366 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction() {
/* 371 */     return PushReaction.IGNORE;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDimensions(Pose paramPose) {
/* 376 */     return EntityDimensions.scalable(getRadius() * 2.0F, 0.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 381 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> paramDataComponentType) {
/* 386 */     if (paramDataComponentType == DataComponents.POTION_CONTENTS) {
/* 387 */       return castComponentValue((DataComponentType)paramDataComponentType, this.potionContents);
/*     */     }
/* 389 */     if (paramDataComponentType == DataComponents.POTION_DURATION_SCALE) {
/* 390 */       return castComponentValue((DataComponentType)paramDataComponentType, Float.valueOf(this.potionDurationScale));
/*     */     }
/*     */     
/* 393 */     return super.get(paramDataComponentType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/* 398 */     applyImplicitComponentIfPresent(paramDataComponentGetter, DataComponents.POTION_CONTENTS);
/* 399 */     applyImplicitComponentIfPresent(paramDataComponentGetter, DataComponents.POTION_DURATION_SCALE);
/* 400 */     super.applyImplicitComponents(paramDataComponentGetter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> paramDataComponentType, T paramT) {
/* 405 */     if (paramDataComponentType == DataComponents.POTION_CONTENTS) {
/* 406 */       setPotionContents(castComponentValue(DataComponents.POTION_CONTENTS, paramT));
/* 407 */       return true;
/*     */     } 
/* 409 */     if (paramDataComponentType == DataComponents.POTION_DURATION_SCALE) {
/* 410 */       setPotionDurationScale(((Float)castComponentValue(DataComponents.POTION_DURATION_SCALE, paramT)).floatValue());
/* 411 */       return true;
/*     */     } 
/*     */     
/* 414 */     return super.applyImplicitComponent(paramDataComponentType, paramT);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\AreaEffectCloud.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */