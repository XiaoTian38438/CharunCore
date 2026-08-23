/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public abstract class AgeableMob
/*     */   extends PathfinderMob {
/*  19 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final int BABY_START_AGE = -24000;
/*     */   
/*     */   private static final int FORCED_AGE_PARTICLE_TICKS = 40;
/*     */   
/*     */   protected static final int DEFAULT_AGE = 0;
/*     */   protected static final int DEFAULT_FORCED_AGE = 0;
/*  27 */   protected int age = 0;
/*  28 */   protected int forcedAge = 0;
/*     */   protected int forcedAgeTimer;
/*     */   
/*     */   protected AgeableMob(EntityType<? extends AgeableMob> paramEntityType, Level paramLevel) {
/*  32 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/*  37 */     if (paramSpawnGroupData == null) {
/*  38 */       paramSpawnGroupData = new AgeableMobGroupData(true);
/*     */     }
/*     */     
/*  41 */     AgeableMobGroupData ageableMobGroupData = (AgeableMobGroupData)paramSpawnGroupData;
/*     */     
/*  43 */     if (ageableMobGroupData.isShouldSpawnBaby() && ageableMobGroupData.getGroupSize() > 0 && paramServerLevelAccessor.getRandom().nextFloat() <= ageableMobGroupData.getBabySpawnChance()) {
/*  44 */       setAge(-24000);
/*     */     }
/*     */     
/*  47 */     ageableMobGroupData.increaseGroupSizeByOne();
/*     */     
/*  49 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob);
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  56 */     super.defineSynchedData(paramBuilder);
/*  57 */     paramBuilder.define(DATA_BABY_ID, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean canBreed() {
/*  61 */     return false;
/*     */   }
/*     */   
/*     */   public int getAge() {
/*  65 */     if (level().isClientSide()) {
/*  66 */       return ((Boolean)this.entityData.get(DATA_BABY_ID)).booleanValue() ? -1 : 1;
/*     */     }
/*  68 */     return this.age;
/*     */   }
/*     */ 
/*     */   
/*     */   public void ageUp(int paramInt, boolean paramBoolean) {
/*  73 */     int i = getAge();
/*  74 */     int j = i;
/*  75 */     i += paramInt * 20;
/*  76 */     if (i > 0) {
/*  77 */       i = 0;
/*     */     }
/*  79 */     int k = i - j;
/*  80 */     setAge(i);
/*  81 */     if (paramBoolean) {
/*  82 */       this.forcedAge += k;
/*  83 */       if (this.forcedAgeTimer == 0) {
/*  84 */         this.forcedAgeTimer = 40;
/*     */       }
/*     */     } 
/*  87 */     if (getAge() == 0) {
/*  88 */       setAge(this.forcedAge);
/*     */     }
/*     */   }
/*     */   
/*     */   public void ageUp(int paramInt) {
/*  93 */     ageUp(paramInt, false);
/*     */   }
/*     */   
/*     */   public void setAge(int paramInt) {
/*  97 */     int i = getAge();
/*  98 */     this.age = paramInt;
/*     */     
/* 100 */     if ((i < 0 && paramInt >= 0) || (i >= 0 && paramInt < 0)) {
/* 101 */       this.entityData.set(DATA_BABY_ID, Boolean.valueOf((paramInt < 0)));
/* 102 */       ageBoundaryReached();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 108 */     super.addAdditionalSaveData(paramValueOutput);
/* 109 */     paramValueOutput.putInt("Age", getAge());
/* 110 */     paramValueOutput.putInt("ForcedAge", this.forcedAge);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 115 */     super.readAdditionalSaveData(paramValueInput);
/* 116 */     setAge(paramValueInput.getIntOr("Age", 0));
/* 117 */     this.forcedAge = paramValueInput.getIntOr("ForcedAge", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 122 */     if (DATA_BABY_ID.equals(paramEntityDataAccessor)) {
/* 123 */       refreshDimensions();
/*     */     }
/* 125 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 130 */     super.aiStep();
/*     */     
/* 132 */     if (level().isClientSide()) {
/* 133 */       if (this.forcedAgeTimer > 0) {
/* 134 */         if (this.forcedAgeTimer % 4 == 0) {
/* 135 */           level().addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/*     */         }
/* 137 */         this.forcedAgeTimer--;
/*     */       } 
/* 139 */     } else if (isAlive()) {
/* 140 */       int i = getAge();
/* 141 */       if (i < 0) {
/* 142 */         i++;
/* 143 */         setAge(i);
/* 144 */       } else if (i > 0) {
/* 145 */         i--;
/* 146 */         setAge(i);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 152 */     if (!isBaby() && 
/* 153 */       isPassenger()) {
/* 154 */       Entity entity = getVehicle(); if (entity instanceof AbstractBoat) { AbstractBoat abstractBoat = (AbstractBoat)entity;
/* 155 */         if (!abstractBoat.hasEnoughSpaceFor(this))
/* 156 */           stopRiding();  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isBaby() {
/* 162 */     return (getAge() < 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaby(boolean paramBoolean) {
/* 167 */     setAge(paramBoolean ? -24000 : 0);
/*     */   }
/*     */   
/*     */   public static int getSpeedUpSecondsWhenFeeding(int paramInt) {
/* 171 */     return (int)((paramInt / 20) * 0.1F);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getForcedAge() {
/* 176 */     return this.forcedAge;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getForcedAgeTimer() {
/* 181 */     return this.forcedAgeTimer;
/*     */   }
/*     */   
/*     */   public static class AgeableMobGroupData implements SpawnGroupData {
/*     */     private int groupSize;
/*     */     private final boolean shouldSpawnBaby;
/*     */     private final float babySpawnChance;
/*     */     
/*     */     public AgeableMobGroupData(boolean param1Boolean, float param1Float) {
/* 190 */       this.shouldSpawnBaby = param1Boolean;
/* 191 */       this.babySpawnChance = param1Float;
/*     */     }
/*     */     
/*     */     public AgeableMobGroupData(boolean param1Boolean) {
/* 195 */       this(param1Boolean, 0.05F);
/*     */     }
/*     */     
/*     */     public AgeableMobGroupData(float param1Float) {
/* 199 */       this(true, param1Float);
/*     */     }
/*     */     
/*     */     public int getGroupSize() {
/* 203 */       return this.groupSize;
/*     */     }
/*     */     
/*     */     public void increaseGroupSizeByOne() {
/* 207 */       this.groupSize++;
/*     */     }
/*     */     
/*     */     public boolean isShouldSpawnBaby() {
/* 211 */       return this.shouldSpawnBaby;
/*     */     }
/*     */     
/*     */     public float getBabySpawnChance() {
/* 215 */       return this.babySpawnChance;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\AgeableMob.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */