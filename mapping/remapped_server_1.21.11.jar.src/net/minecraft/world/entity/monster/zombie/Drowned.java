/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Drowned extends Zombie implements RangedAttackMob {
/*     */   public static final float NAUTILUS_SHELL_CHANCE = 0.03F;
/*     */   private static final float ZOMBIE_NAUTILUS_JOCKEY_CHANCE = 0.5F;
/*     */   boolean searchingForLand;
/*     */   
/*     */   public Drowned(EntityType<? extends Drowned> paramEntityType, Level paramLevel) {
/*  71 */     super((EntityType)paramEntityType, paramLevel);
/*  72 */     this.moveControl = new DrownedMoveControl(this);
/*     */     
/*  74 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  78 */     return Zombie.createAttributes()
/*  79 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level paramLevel) {
/*  84 */     return (PathNavigation)new AmphibiousPathNavigation((Mob)this, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addBehaviourGoals() {
/*  89 */     this.goalSelector.addGoal(1, new DrownedGoToWaterGoal((PathfinderMob)this, 1.0D));
/*  90 */     this.goalSelector.addGoal(2, (Goal)new DrownedTridentAttackGoal(this, 1.0D, 40, 10.0F));
/*  91 */     this.goalSelector.addGoal(2, (Goal)new DrownedAttackGoal(this, 1.0D, false));
/*  92 */     this.goalSelector.addGoal(5, (Goal)new DrownedGoToBeachGoal(this, 1.0D));
/*  93 */     this.goalSelector.addGoal(6, new DrownedSwimUpGoal(this, 1.0D, level().getSeaLevel()));
/*  94 */     this.goalSelector.addGoal(7, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0D));
/*     */     
/*  96 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Drowned.class })).setAlertOthers(new Class[] { ZombifiedPiglin.class }));
/*  97 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, (paramLivingEntity, paramServerLevel) -> okTarget(paramLivingEntity)));
/*  98 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false));
/*  99 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/* 100 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Axolotl.class, true, false));
/* 101 */     this.targetSelector.addGoal(5, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 106 */     paramSpawnGroupData = super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */     
/* 108 */     if (getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() && 
/* 109 */       paramServerLevelAccessor.getRandom().nextFloat() < 0.03F) {
/* 110 */       setItemSlot(EquipmentSlot.OFFHAND, new ItemStack((ItemLike)Items.NAUTILUS_SHELL));
/* 111 */       setGuaranteedDrop(EquipmentSlot.OFFHAND);
/*     */     } 
/*     */ 
/*     */     
/* 115 */     if ((paramEntitySpawnReason == EntitySpawnReason.NATURAL || paramEntitySpawnReason == EntitySpawnReason.STRUCTURE) && 
/* 116 */       getMainHandItem().is(Items.TRIDENT) && paramServerLevelAccessor
/* 117 */       .getRandom().nextFloat() < 0.5F && 
/* 118 */       !isBaby() && 
/* 119 */       !paramServerLevelAccessor.getBiome(blockPosition()).is(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
/* 120 */       ZombieNautilus zombieNautilus = (ZombieNautilus)EntityType.ZOMBIE_NAUTILUS.create(level(), EntitySpawnReason.JOCKEY);
/* 121 */       if (zombieNautilus != null) {
/* 122 */         if (paramEntitySpawnReason == EntitySpawnReason.STRUCTURE) {
/* 123 */           zombieNautilus.setPersistenceRequired();
/*     */         }
/* 125 */         zombieNautilus.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 126 */         zombieNautilus.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, null);
/* 127 */         startRiding((Entity)zombieNautilus, false, false);
/* 128 */         paramServerLevelAccessor.addFreshEntity((Entity)zombieNautilus);
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     return paramSpawnGroupData;
/*     */   }
/*     */   
/*     */   public static boolean checkDrownedSpawnRules(EntityType<Drowned> paramEntityType, ServerLevelAccessor paramServerLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 136 */     if (!paramServerLevelAccessor.getFluidState(paramBlockPos.below()).is(FluidTags.WATER) && !EntitySpawnReason.isSpawner(paramEntitySpawnReason)) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     Holder holder = paramServerLevelAccessor.getBiome(paramBlockPos);
/*     */ 
/*     */     
/* 143 */     boolean bool = (paramServerLevelAccessor.getDifficulty() != Difficulty.PEACEFUL && (EntitySpawnReason.ignoresLightRequirements(paramEntitySpawnReason) || isDarkEnoughToSpawn(paramServerLevelAccessor, paramBlockPos, paramRandomSource)) && (EntitySpawnReason.isSpawner(paramEntitySpawnReason) || paramServerLevelAccessor.getFluidState(paramBlockPos).is(FluidTags.WATER))) ? true : false;
/*     */     
/* 145 */     if (bool && (EntitySpawnReason.isSpawner(paramEntitySpawnReason) || paramEntitySpawnReason == EntitySpawnReason.REINFORCEMENT))
/* 146 */       return true; 
/* 147 */     if (holder.is(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
/* 148 */       return (paramRandomSource.nextInt(15) == 0 && bool);
/*     */     }
/* 150 */     return (paramRandomSource.nextInt(40) == 0 && isDeepEnoughToSpawn((LevelAccessor)paramServerLevelAccessor, paramBlockPos) && bool);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDeepEnoughToSpawn(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 155 */     return (paramBlockPos.getY() < paramLevelAccessor.getSeaLevel() - 5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 160 */     if (isInWater()) {
/* 161 */       return SoundEvents.DROWNED_AMBIENT_WATER;
/*     */     }
/* 163 */     return SoundEvents.DROWNED_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 168 */     if (isInWater()) {
/* 169 */       return SoundEvents.DROWNED_HURT_WATER;
/*     */     }
/* 171 */     return SoundEvents.DROWNED_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 176 */     if (isInWater()) {
/* 177 */       return SoundEvents.DROWNED_DEATH_WATER;
/*     */     }
/* 179 */     return SoundEvents.DROWNED_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getStepSound() {
/* 184 */     return SoundEvents.DROWNED_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 189 */     return SoundEvents.DROWNED_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSpawnInLiquids() {
/* 194 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource paramRandomSource, DifficultyInstance paramDifficultyInstance) {
/* 199 */     if (paramRandomSource.nextFloat() > 0.9D) {
/* 200 */       int i = paramRandomSource.nextInt(16);
/* 201 */       if (i < 10) {
/* 202 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.TRIDENT));
/*     */       } else {
/* 204 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.FISHING_ROD));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack paramItemStack1, ItemStack paramItemStack2, EquipmentSlot paramEquipmentSlot) {
/* 211 */     if (paramItemStack2.is(Items.NAUTILUS_SHELL)) {
/* 212 */       return false;
/*     */     }
/*     */     
/* 215 */     return super.canReplaceCurrentItem(paramItemStack1, paramItemStack2, paramEquipmentSlot);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean convertsInWater() {
/* 220 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader paramLevelReader) {
/* 225 */     return paramLevelReader.isUnobstructed((Entity)this);
/*     */   }
/*     */   
/*     */   public boolean okTarget(LivingEntity paramLivingEntity) {
/* 229 */     if (paramLivingEntity != null) {
/* 230 */       if (level().isBrightOutside() && !paramLivingEntity.isInWater()) {
/* 231 */         return false;
/*     */       }
/*     */       
/* 234 */       return true;
/*     */     } 
/* 236 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 241 */     return !isSwimming();
/*     */   }
/*     */   
/*     */   boolean wantsToSwim() {
/* 245 */     if (this.searchingForLand) {
/* 246 */       return true;
/*     */     }
/*     */     
/* 249 */     LivingEntity livingEntity = getTarget();
/* 250 */     if (livingEntity != null && livingEntity.isInWater()) {
/* 251 */       return true;
/*     */     }
/*     */     
/* 254 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 paramVec3, double paramDouble1, boolean paramBoolean, double paramDouble2) {
/* 259 */     if (isUnderWater() && wantsToSwim()) {
/* 260 */       moveRelative(0.01F, paramVec3);
/* 261 */       move(MoverType.SELF, getDeltaMovement());
/*     */       
/* 263 */       setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */     } else {
/* 265 */       super.travelInWater(paramVec3, paramDouble1, paramBoolean, paramDouble2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSwimming() {
/* 271 */     if (!level().isClientSide()) {
/* 272 */       setSwimming((isEffectiveAi() && isUnderWater() && wantsToSwim()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVisuallySwimming() {
/* 278 */     return (isSwimming() && !isPassenger());
/*     */   }
/*     */   
/*     */   protected boolean closeToNextPos() {
/* 282 */     Path path = getNavigation().getPath();
/* 283 */     if (path != null) {
/* 284 */       BlockPos blockPos = path.getTarget();
/* 285 */       if (blockPos != null) {
/* 286 */         double d = distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ());
/* 287 */         if (d < 4.0D) {
/* 288 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 292 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity paramLivingEntity, float paramFloat) {
/* 297 */     ItemStack itemStack1 = getMainHandItem();
/* 298 */     ItemStack itemStack2 = itemStack1.is(Items.TRIDENT) ? itemStack1 : new ItemStack((ItemLike)Items.TRIDENT);
/* 299 */     ThrownTrident thrownTrident = new ThrownTrident(level(), (LivingEntity)this, itemStack2);
/*     */     
/* 301 */     double d1 = paramLivingEntity.getX() - getX();
/* 302 */     double d2 = paramLivingEntity.getY(0.3333333333333333D) - thrownTrident.getY();
/* 303 */     double d3 = paramLivingEntity.getZ() - getZ();
/* 304 */     double d4 = Math.sqrt(d1 * d1 + d3 * d3);
/* 305 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 306 */       Projectile.spawnProjectileUsingShoot((Projectile)thrownTrident, serverLevel, itemStack2, d1, d2 + d4 * 0.20000000298023224D, d3, 1.6F, (14 - 
/*     */ 
/*     */ 
/*     */           
/* 310 */           level().getDifficulty().getId() * 4)); }
/*     */     
/* 312 */     playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
/*     */   }
/*     */ 
/*     */   
/*     */   public TagKey<Item> getPreferredWeaponType() {
/* 317 */     return ItemTags.DROWNED_PREFERRED_WEAPONS;
/*     */   }
/*     */   
/*     */   public void setSearchingForLand(boolean paramBoolean) {
/* 321 */     this.searchingForLand = paramBoolean;
/*     */   }
/*     */   
/*     */   private static class DrownedTridentAttackGoal extends RangedAttackGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedTridentAttackGoal(RangedAttackMob param1RangedAttackMob, double param1Double, int param1Int, float param1Float) {
/* 328 */       super(param1RangedAttackMob, param1Double, param1Int, param1Float);
/* 329 */       this.drowned = (Drowned)param1RangedAttackMob;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 334 */       return (super.canUse() && this.drowned.getMainHandItem().is(Items.TRIDENT));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 339 */       super.start();
/* 340 */       this.drowned.setAggressive(true);
/* 341 */       this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 346 */       super.stop();
/* 347 */       this.drowned.stopUsingItem();
/* 348 */       this.drowned.setAggressive(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedSwimUpGoal extends Goal {
/*     */     private final Drowned drowned;
/*     */     private final double speedModifier;
/*     */     private final int seaLevel;
/*     */     private boolean stuck;
/*     */     
/*     */     public DrownedSwimUpGoal(Drowned param1Drowned, double param1Double, int param1Int) {
/* 359 */       this.drowned = param1Drowned;
/* 360 */       this.speedModifier = param1Double;
/* 361 */       this.seaLevel = param1Int;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 366 */       return (!this.drowned.level().isBrightOutside() && this.drowned.isInWater() && this.drowned.getY() < (this.seaLevel - 2));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 371 */       return (canUse() && !this.stuck);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 376 */       if (this.drowned.getY() < (this.seaLevel - 1) && (this.drowned.getNavigation().isDone() || this.drowned.closeToNextPos())) {
/*     */         
/* 378 */         Vec3 vec3 = DefaultRandomPos.getPosTowards((PathfinderMob)this.drowned, 4, 8, new Vec3(this.drowned.getX(), (this.seaLevel - 1), this.drowned.getZ()), 1.5707963705062866D);
/*     */         
/* 380 */         if (vec3 == null) {
/* 381 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 385 */         this.drowned.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 391 */       this.drowned.setSearchingForLand(true);
/* 392 */       this.stuck = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 397 */       this.drowned.setSearchingForLand(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedGoToBeachGoal
/*     */     extends MoveToBlockGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedGoToBeachGoal(Drowned param1Drowned, double param1Double) {
/* 406 */       super((PathfinderMob)param1Drowned, param1Double, 8, 2);
/* 407 */       this.drowned = param1Drowned;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 412 */       return (super.canUse() && !this.drowned.level().isBrightOutside() && this.drowned.isInWater() && this.drowned.getY() >= (this.drowned.level().getSeaLevel() - 3));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 417 */       return super.canContinueToUse();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader param1LevelReader, BlockPos param1BlockPos) {
/* 422 */       BlockPos blockPos = param1BlockPos.above();
/* 423 */       if (!param1LevelReader.isEmptyBlock(blockPos) || !param1LevelReader.isEmptyBlock(blockPos.above())) {
/* 424 */         return false;
/*     */       }
/*     */       
/* 427 */       return param1LevelReader.getBlockState(param1BlockPos).entityCanStandOn((BlockGetter)param1LevelReader, param1BlockPos, (Entity)this.drowned);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 432 */       this.drowned.setSearchingForLand(false);
/* 433 */       super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 438 */       super.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedGoToWaterGoal extends Goal {
/*     */     private final PathfinderMob mob;
/*     */     private double wantedX;
/*     */     private double wantedY;
/*     */     private double wantedZ;
/*     */     private final double speedModifier;
/*     */     private final Level level;
/*     */     
/*     */     public DrownedGoToWaterGoal(PathfinderMob param1PathfinderMob, double param1Double) {
/* 451 */       this.mob = param1PathfinderMob;
/* 452 */       this.speedModifier = param1Double;
/* 453 */       this.level = param1PathfinderMob.level();
/* 454 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 459 */       if (!this.level.isBrightOutside()) {
/* 460 */         return false;
/*     */       }
/* 462 */       if (this.mob.isInWater()) {
/* 463 */         return false;
/*     */       }
/*     */       
/* 466 */       Vec3 vec3 = getWaterPos();
/* 467 */       if (vec3 == null) {
/* 468 */         return false;
/*     */       }
/* 470 */       this.wantedX = vec3.x;
/* 471 */       this.wantedY = vec3.y;
/* 472 */       this.wantedZ = vec3.z;
/* 473 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 478 */       return !this.mob.getNavigation().isDone();
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 483 */       this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*     */     }
/*     */     
/*     */     private Vec3 getWaterPos() {
/* 487 */       RandomSource randomSource = this.mob.getRandom();
/* 488 */       BlockPos blockPos = this.mob.blockPosition();
/*     */       
/* 490 */       for (byte b = 0; b < 10; b++) {
/* 491 */         BlockPos blockPos1 = blockPos.offset(randomSource.nextInt(20) - 10, 2 - randomSource.nextInt(8), randomSource.nextInt(20) - 10);
/*     */         
/* 493 */         if (this.level.getBlockState(blockPos1).is(Blocks.WATER)) {
/* 494 */           return Vec3.atBottomCenterOf((Vec3i)blockPos1);
/*     */         }
/*     */       } 
/* 497 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedAttackGoal extends ZombieAttackGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedAttackGoal(Drowned param1Drowned, double param1Double, boolean param1Boolean) {
/* 505 */       super(param1Drowned, param1Double, param1Boolean);
/* 506 */       this.drowned = param1Drowned;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 511 */       return (super.canUse() && this.drowned.okTarget(this.drowned.getTarget()));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 516 */       return (super.canContinueToUse() && this.drowned.okTarget(this.drowned.getTarget()));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedMoveControl extends MoveControl {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedMoveControl(Drowned param1Drowned) {
/* 524 */       super((Mob)param1Drowned);
/* 525 */       this.drowned = param1Drowned;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 530 */       LivingEntity livingEntity = this.drowned.getTarget();
/* 531 */       if (this.drowned.wantsToSwim() && this.drowned.isInWater()) {
/* 532 */         if ((livingEntity != null && livingEntity.getY() > this.drowned.getY()) || this.drowned.searchingForLand)
/*     */         {
/* 534 */           this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
/*     */         }
/*     */         
/* 537 */         if (this.operation != MoveControl.Operation.MOVE_TO || this.drowned.getNavigation().isDone()) {
/* 538 */           this.drowned.setSpeed(0.0F);
/*     */           
/*     */           return;
/*     */         } 
/* 542 */         double d1 = this.wantedX - this.drowned.getX();
/* 543 */         double d2 = this.wantedY - this.drowned.getY();
/* 544 */         double d3 = this.wantedZ - this.drowned.getZ();
/* 545 */         double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
/* 546 */         d2 /= d4;
/*     */         
/* 548 */         float f1 = (float)(Mth.atan2(d3, d1) * 57.2957763671875D) - 90.0F;
/* 549 */         this.drowned.setYRot(rotlerp(this.drowned.getYRot(), f1, 90.0F));
/* 550 */         this.drowned.yBodyRot = this.drowned.getYRot();
/*     */         
/* 552 */         float f2 = (float)(this.speedModifier * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 553 */         float f3 = Mth.lerp(0.125F, this.drowned.getSpeed(), f2);
/* 554 */         this.drowned.setSpeed(f3);
/* 555 */         this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(f3 * d1 * 0.005D, f3 * d2 * 0.1D, f3 * d3 * 0.005D));
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 561 */         if (!this.drowned.onGround()) {
/* 562 */           this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
/*     */         }
/* 564 */         super.tick();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void rideTick() {
/* 571 */     super.rideTick();
/*     */     
/* 573 */     Entity entity = getControlledVehicle(); if (entity instanceof PathfinderMob) { PathfinderMob pathfinderMob = (PathfinderMob)entity;
/* 574 */       this.yBodyRot = pathfinderMob.yBodyRot; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel paramServerLevel, ItemStack paramItemStack) {
/* 580 */     if (paramItemStack.is(ItemTags.SPEARS)) {
/* 581 */       return false;
/*     */     }
/* 583 */     return super.wantsToPickUp(paramServerLevel, paramItemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\zombie\Drowned.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */