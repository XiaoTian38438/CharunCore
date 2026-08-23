/*     */ package net.minecraft.world.entity.monster;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Endermite extends Monster {
/*     */   private static final int MAX_LIFE = 2400;
/*  33 */   private int life = 0; private static final int DEFAULT_LIFE = 0;
/*     */   
/*     */   public Endermite(EntityType<? extends Endermite> paramEntityType, Level paramLevel) {
/*  36 */     super((EntityType)paramEntityType, paramLevel);
/*  37 */     this.xpReward = 3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  42 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*  43 */     this.goalSelector.addGoal(1, (Goal)new ClimbOnTopOfPowderSnowGoal((Mob)this, level()));
/*  44 */     this.goalSelector.addGoal(2, (Goal)new MeleeAttackGoal(this, 1.0D, false));
/*  45 */     this.goalSelector.addGoal(3, (Goal)new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  46 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  47 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  49 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*  50 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  54 */     return Monster.createMonsterAttributes()
/*  55 */       .add(Attributes.MAX_HEALTH, 8.0D)
/*  56 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/*  57 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/*  62 */     return Entity.MovementEmission.EVENTS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  67 */     return SoundEvents.ENDERMITE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  72 */     return SoundEvents.ENDERMITE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  77 */     return SoundEvents.ENDERMITE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  82 */     playSound(SoundEvents.ENDERMITE_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  87 */     super.readAdditionalSaveData(paramValueInput);
/*  88 */     this.life = paramValueInput.getIntOr("Lifetime", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  93 */     super.addAdditionalSaveData(paramValueOutput);
/*  94 */     paramValueOutput.putInt("Lifetime", this.life);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 100 */     this.yBodyRot = getYRot();
/*     */     
/* 102 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYBodyRot(float paramFloat) {
/* 107 */     setYRot(paramFloat);
/* 108 */     super.setYBodyRot(paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 113 */     super.aiStep();
/*     */     
/* 115 */     if (level().isClientSide()) {
/* 116 */       for (byte b = 0; b < 2; b++) {
/* 117 */         level().addParticle((ParticleOptions)ParticleTypes.PORTAL, getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
/*     */       }
/*     */     } else {
/* 120 */       if (!isPersistenceRequired()) {
/* 121 */         this.life++;
/*     */       }
/*     */       
/* 124 */       if (this.life >= 2400) {
/* 125 */         discard();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean checkEndermiteSpawnRules(EntityType<Endermite> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 131 */     if (!checkAnyLightMonsterSpawnRules((EntityType)paramEntityType, paramLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource)) {
/* 132 */       return false;
/*     */     }
/*     */     
/* 135 */     if (EntitySpawnReason.isSpawner(paramEntitySpawnReason)) {
/* 136 */       return true;
/*     */     }
/*     */     
/* 139 */     Player player = paramLevelAccessor.getNearestPlayer(paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, 5.0D, true);
/* 140 */     return (player == null);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Endermite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */