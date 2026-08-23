/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.SpearUseGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class ZombifiedPiglin extends Zombie implements NeutralMob {
/*  47 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.ZOMBIFIED_PIGLIN.getDimensions().scale(0.5F).withEyeHeight(0.97F);
/*     */   
/*  49 */   private static final Identifier SPEED_MODIFIER_ATTACKING_ID = Identifier.withDefaultNamespace("attacking");
/*  50 */   private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_ID, 0.05D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*  52 */   private static final UniformInt FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
/*     */   
/*     */   private int playFirstAngerSoundIn;
/*  55 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   
/*     */   private long persistentAngerEndTime;
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   private static final int ALERT_RANGE_Y = 10;
/*  60 */   private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);
/*     */   private int ticksUntilNextAlert;
/*     */   
/*     */   public ZombifiedPiglin(EntityType<? extends ZombifiedPiglin> paramEntityType, Level paramLevel) {
/*  64 */     super((EntityType)paramEntityType, paramLevel);
/*  65 */     setPathfindingMalus(PathType.LAVA, 8.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addBehaviourGoals() {
/*  70 */     this.goalSelector.addGoal(1, (Goal)new SpearUseGoal(this, 1.0D, 1.0D, 10.0F, 2.0F));
/*  71 */     this.goalSelector.addGoal(2, (Goal)new ZombieAttackGoal(this, 1.0D, false));
/*  72 */     this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*     */     
/*  74 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[0])).setAlertOthers(new Class[0]));
/*  75 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/*  76 */     this.targetSelector.addGoal(3, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  80 */     return Zombie.createAttributes()
/*  81 */       .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0D)
/*  82 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
/*  83 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose paramPose) {
/*  88 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(paramPose);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean convertsInWater() {
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/*  98 */     AttributeInstance attributeInstance = getAttribute(Attributes.MOVEMENT_SPEED);
/*  99 */     if (isAngry()) {
/* 100 */       if (!isBaby() && !attributeInstance.hasModifier(SPEED_MODIFIER_ATTACKING_ID)) {
/* 101 */         attributeInstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
/*     */       }
/* 103 */       maybePlayFirstAngerSound();
/* 104 */     } else if (attributeInstance.hasModifier(SPEED_MODIFIER_ATTACKING_ID)) {
/* 105 */       attributeInstance.removeModifier(SPEED_MODIFIER_ATTACKING_ID);
/*     */     } 
/*     */     
/* 108 */     updatePersistentAnger(paramServerLevel, true);
/* 109 */     if (getTarget() != null) {
/* 110 */       maybeAlertOthers();
/*     */     }
/*     */     
/* 113 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */   
/*     */   private void maybePlayFirstAngerSound() {
/* 117 */     if (this.playFirstAngerSoundIn > 0) {
/* 118 */       this.playFirstAngerSoundIn--;
/* 119 */       if (this.playFirstAngerSoundIn == 0) {
/* 120 */         playAngerSound();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeAlertOthers() {
/* 130 */     if (this.ticksUntilNextAlert > 0) {
/* 131 */       this.ticksUntilNextAlert--;
/*     */       return;
/*     */     } 
/* 134 */     if (getSensing().hasLineOfSight((Entity)getTarget())) {
/* 135 */       alertOthers();
/*     */     }
/* 137 */     this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
/*     */   }
/*     */   
/*     */   private void alertOthers() {
/* 141 */     double d = getAttributeValue(Attributes.FOLLOW_RANGE);
/* 142 */     AABB aABB = AABB.unitCubeFromLowerCorner(position()).inflate(d, 10.0D, d);
/* 143 */     level().getEntitiesOfClass(ZombifiedPiglin.class, aABB, EntitySelector.NO_SPECTATORS).stream()
/* 144 */       .filter(paramZombifiedPiglin -> (paramZombifiedPiglin != this))
/* 145 */       .filter(paramZombifiedPiglin -> (paramZombifiedPiglin.getTarget() == null))
/* 146 */       .filter(paramZombifiedPiglin -> !paramZombifiedPiglin.isAlliedTo((Entity)getTarget()))
/* 147 */       .forEach(paramZombifiedPiglin -> paramZombifiedPiglin.setTarget(getTarget()));
/*     */   }
/*     */   
/*     */   private void playAngerSound() {
/* 151 */     playSound(SoundEvents.ZOMBIFIED_PIGLIN_ANGRY, getSoundVolume() * 2.0F, getVoicePitch() * 1.8F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTarget(LivingEntity paramLivingEntity) {
/* 156 */     if (getTarget() == null && paramLivingEntity != null) {
/*     */ 
/*     */       
/* 159 */       this.playFirstAngerSoundIn = FIRST_ANGER_SOUND_DELAY.sample(this.random);
/* 160 */       this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
/*     */     } 
/* 162 */     super.setTarget(paramLivingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 167 */     setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
/*     */   }
/*     */   
/*     */   public static boolean checkZombifiedPiglinSpawnRules(EntityType<ZombifiedPiglin> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 171 */     return (paramLevelAccessor.getDifficulty() != Difficulty.PEACEFUL && !paramLevelAccessor.getBlockState(paramBlockPos.below()).is(Blocks.NETHER_WART_BLOCK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader paramLevelReader) {
/* 176 */     return (paramLevelReader.isUnobstructed((Entity)this) && !paramLevelReader.containsAnyLiquid(getBoundingBox()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 181 */     super.addAdditionalSaveData(paramValueOutput);
/* 182 */     addPersistentAngerSaveData(paramValueOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 187 */     super.readAdditionalSaveData(paramValueInput);
/* 188 */     readPersistentAngerSaveData(level(), paramValueInput);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerEndTime(long paramLong) {
/* 193 */     this.persistentAngerEndTime = paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPersistentAngerEndTime() {
/* 198 */     return this.persistentAngerEndTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerTarget(EntityReference<LivingEntity> paramEntityReference) {
/* 203 */     this.persistentAngerTarget = paramEntityReference;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 208 */     return isAngry() ? SoundEvents.ZOMBIFIED_PIGLIN_ANGRY : SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 213 */     return SoundEvents.ZOMBIFIED_PIGLIN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 218 */     return SoundEvents.ZOMBIFIED_PIGLIN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public void populateDefaultEquipmentSlots(RandomSource paramRandomSource, DifficultyInstance paramDifficultyInstance) {
/* 223 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((paramRandomSource.nextInt(20) == 0) ? (ItemLike)Items.GOLDEN_SPEAR : (ItemLike)Items.GOLDEN_SWORD));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeReinforcementsChance() {
/* 228 */     getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityReference<LivingEntity> getPersistentAngerTarget() {
/* 233 */     return this.persistentAngerTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPreventingPlayerRest(ServerLevel paramServerLevel, Player paramPlayer) {
/* 238 */     return isAngryAt((LivingEntity)paramPlayer, paramServerLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel paramServerLevel, ItemStack paramItemStack) {
/* 243 */     return canHoldItem(paramItemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\zombie\ZombifiedPiglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */