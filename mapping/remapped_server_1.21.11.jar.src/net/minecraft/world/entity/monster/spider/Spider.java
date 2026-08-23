/*     */ package net.minecraft.world.entity.monster.spider;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.skeleton.Skeleton;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Spider extends Monster {
/*  48 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Spider.class, EntityDataSerializers.BYTE); private static final float SPIDER_SPECIAL_EFFECT_CHANCE = 0.1F;
/*     */   
/*     */   public Spider(EntityType<? extends Spider> paramEntityType, Level paramLevel) {
/*  51 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  56 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*     */     
/*  58 */     this.goalSelector.addGoal(2, (Goal)new AvoidEntityGoal((PathfinderMob)this, Armadillo.class, 6.0F, 1.0D, 1.2D, paramLivingEntity -> !((Armadillo)paramLivingEntity).isScared()));
/*     */     
/*  60 */     this.goalSelector.addGoal(3, (Goal)new LeapAtTargetGoal((Mob)this, 0.4F));
/*  61 */     this.goalSelector.addGoal(4, (Goal)new SpiderAttackGoal(this));
/*     */     
/*  63 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.8D));
/*  64 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  65 */     this.goalSelector.addGoal(6, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  67 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
/*  68 */     this.targetSelector.addGoal(2, (Goal)new SpiderTargetGoal<>(this, Player.class));
/*  69 */     this.targetSelector.addGoal(3, (Goal)new SpiderTargetGoal<>(this, IronGolem.class));
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level paramLevel) {
/*  74 */     return (PathNavigation)new WallClimberNavigation((Mob)this, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  79 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  81 */     paramBuilder.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  86 */     super.tick();
/*     */     
/*  88 */     if (!level().isClientSide())
/*     */     {
/*     */       
/*  91 */       setClimbing(this.horizontalCollision);
/*     */     }
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  96 */     return Monster.createMonsterAttributes()
/*  97 */       .add(Attributes.MAX_HEALTH, 16.0D)
/*  98 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 103 */     return SoundEvents.SPIDER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 108 */     return SoundEvents.SPIDER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 113 */     return SoundEvents.SPIDER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 118 */     playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onClimbable() {
/* 127 */     return isClimbing();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void makeStuckInBlock(BlockState paramBlockState, Vec3 paramVec3) {
/* 133 */     if (!paramBlockState.is(Blocks.COBWEB)) {
/* 134 */       super.makeStuckInBlock(paramBlockState, paramVec3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance paramMobEffectInstance) {
/* 140 */     if (paramMobEffectInstance.is(MobEffects.POISON)) {
/* 141 */       return false;
/*     */     }
/* 143 */     return super.canBeAffected(paramMobEffectInstance);
/*     */   }
/*     */   
/*     */   public boolean isClimbing() {
/* 147 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setClimbing(boolean paramBoolean) {
/* 151 */     byte b = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 152 */     if (paramBoolean) {
/* 153 */       b = (byte)(b | 0x1);
/*     */     } else {
/* 155 */       b = (byte)(b & 0xFFFFFFFE);
/*     */     } 
/* 157 */     this.entityData.set(DATA_FLAGS_ID, Byte.valueOf(b));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 162 */     paramSpawnGroupData = super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */     
/* 164 */     RandomSource randomSource = paramServerLevelAccessor.getRandom();
/* 165 */     if (randomSource.nextInt(100) == 0) {
/* 166 */       Skeleton skeleton = (Skeleton)EntityType.SKELETON.create(level(), EntitySpawnReason.JOCKEY);
/* 167 */       if (skeleton != null) {
/* 168 */         skeleton.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 169 */         skeleton.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, null);
/* 170 */         skeleton.startRiding((Entity)this, false, false);
/*     */       } 
/*     */     } 
/*     */     
/* 174 */     if (paramSpawnGroupData == null) {
/* 175 */       paramSpawnGroupData = new SpiderEffectsGroupData();
/*     */       
/* 177 */       if (paramServerLevelAccessor.getDifficulty() == Difficulty.HARD && randomSource.nextFloat() < 0.1F * paramDifficultyInstance.getSpecialMultiplier()) {
/* 178 */         ((SpiderEffectsGroupData)paramSpawnGroupData).setRandomEffect(randomSource);
/*     */       }
/*     */     } 
/* 181 */     if (paramSpawnGroupData instanceof SpiderEffectsGroupData) { SpiderEffectsGroupData spiderEffectsGroupData = (SpiderEffectsGroupData)paramSpawnGroupData;
/* 182 */       Holder<MobEffect> holder = spiderEffectsGroupData.effect;
/* 183 */       if (holder != null) {
/* 184 */         addEffect(new MobEffectInstance(holder, -1));
/*     */       } }
/*     */ 
/*     */     
/* 188 */     return paramSpawnGroupData;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getVehicleAttachmentPoint(Entity paramEntity) {
/* 193 */     if (paramEntity.getBbWidth() <= getBbWidth()) {
/* 194 */       return new Vec3(0.0D, 0.3125D * getScale(), 0.0D);
/*     */     }
/* 196 */     return super.getVehicleAttachmentPoint(paramEntity);
/*     */   }
/*     */   
/*     */   public static class SpiderEffectsGroupData
/*     */     implements SpawnGroupData
/*     */   {
/*     */     public Holder<MobEffect> effect;
/*     */     
/*     */     public void setRandomEffect(RandomSource param1RandomSource) {
/* 205 */       int i = param1RandomSource.nextInt(5);
/* 206 */       if (i <= 1) {
/* 207 */         this.effect = MobEffects.SPEED;
/* 208 */       } else if (i <= 2) {
/* 209 */         this.effect = MobEffects.STRENGTH;
/* 210 */       } else if (i <= 3) {
/* 211 */         this.effect = MobEffects.REGENERATION;
/* 212 */       } else if (i <= 4) {
/* 213 */         this.effect = MobEffects.INVISIBILITY;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpiderAttackGoal extends MeleeAttackGoal {
/*     */     public SpiderAttackGoal(Spider param1Spider) {
/* 220 */       super((PathfinderMob)param1Spider, 1.0D, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 225 */       return (super.canUse() && !this.mob.isVehicle());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 230 */       float f = this.mob.getLightLevelDependentMagicValue();
/* 231 */       if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
/* 232 */         this.mob.setTarget(null);
/* 233 */         return false;
/*     */       } 
/* 235 */       return super.canContinueToUse();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
/*     */     public SpiderTargetGoal(Spider param1Spider, Class<T> param1Class) {
/* 241 */       super((Mob)param1Spider, param1Class, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 246 */       float f = this.mob.getLightLevelDependentMagicValue();
/* 247 */       if (f >= 0.5F) {
/* 248 */         return false;
/*     */       }
/*     */       
/* 251 */       return super.canUse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\spider\Spider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */