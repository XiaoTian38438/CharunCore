/*     */ package net.minecraft.world.entity.animal;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class Animal extends AgeableMob {
/*     */   protected static final int PARENT_AGE_AFTER_BREEDING = 6000;
/*     */   private static final int DEFAULT_IN_LOVE_TIME = 0;
/*  49 */   private int inLove = 0;
/*     */   private EntityReference<ServerPlayer> loveCause;
/*     */   
/*     */   protected Animal(EntityType<? extends Animal> paramEntityType, Level paramLevel) {
/*  53 */     super(paramEntityType, paramLevel);
/*  54 */     setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
/*  55 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAnimalAttributes() {
/*  59 */     return Mob.createMobAttributes()
/*  60 */       .add(Attributes.TEMPT_RANGE, 10.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/*  65 */     if (getAge() != 0) {
/*  66 */       this.inLove = 0;
/*     */     }
/*  68 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  73 */     super.aiStep();
/*     */     
/*  75 */     if (getAge() != 0) {
/*  76 */       this.inLove = 0;
/*     */     }
/*     */     
/*  79 */     if (this.inLove > 0) {
/*  80 */       this.inLove--;
/*  81 */       if (this.inLove % 10 == 0) {
/*  82 */         double d1 = this.random.nextGaussian() * 0.02D;
/*  83 */         double d2 = this.random.nextGaussian() * 0.02D;
/*  84 */         double d3 = this.random.nextGaussian() * 0.02D;
/*  85 */         level().addParticle((ParticleOptions)ParticleTypes.HEART, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), d1, d2, d3);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actuallyHurt(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/*  92 */     resetLove();
/*  93 */     super.actuallyHurt(paramServerLevel, paramDamageSource, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/*  98 */     if (paramLevelReader.getBlockState(paramBlockPos.below()).is(Blocks.GRASS_BLOCK)) {
/*  99 */       return 10.0F;
/*     */     }
/* 101 */     return paramLevelReader.getPathfindingCostFromLightLevels(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 106 */     super.addAdditionalSaveData(paramValueOutput);
/* 107 */     paramValueOutput.putInt("InLove", this.inLove);
/* 108 */     EntityReference.store(this.loveCause, paramValueOutput, "LoveCause");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 113 */     super.readAdditionalSaveData(paramValueInput);
/* 114 */     this.inLove = paramValueInput.getIntOr("InLove", 0);
/* 115 */     this.loveCause = EntityReference.read(paramValueInput, "LoveCause");
/*     */   }
/*     */   
/*     */   public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 119 */     boolean bool = (EntitySpawnReason.ignoresLightRequirements(paramEntitySpawnReason) || isBrightEnoughToSpawn((BlockAndTintGetter)paramLevelAccessor, paramBlockPos)) ? true : false;
/* 120 */     return (paramLevelAccessor.getBlockState(paramBlockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && bool);
/*     */   }
/*     */   
/*     */   protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter paramBlockAndTintGetter, BlockPos paramBlockPos) {
/* 124 */     return (paramBlockAndTintGetter.getRawBrightness(paramBlockPos, 0) > 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 129 */     return 120;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double paramDouble) {
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getBaseExperienceReward(ServerLevel paramServerLevel) {
/* 139 */     return 1 + this.random.nextInt(3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 146 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 147 */     if (isFood(itemStack)) {
/* 148 */       int i = getAge();
/* 149 */       if (paramPlayer instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramPlayer; if (i == 0 && canFallInLove())
/* 150 */         { usePlayerItem(paramPlayer, paramInteractionHand, itemStack);
/* 151 */           setInLove((Player)serverPlayer);
/* 152 */           playEatingSound();
/* 153 */           return (InteractionResult)InteractionResult.SUCCESS_SERVER; }  }
/* 154 */        if (isBaby()) {
/* 155 */         usePlayerItem(paramPlayer, paramInteractionHand, itemStack);
/*     */         
/* 157 */         ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
/* 158 */         playEatingSound();
/* 159 */         return (InteractionResult)InteractionResult.SUCCESS;
/* 160 */       }  if (level().isClientSide()) {
/* 161 */         return (InteractionResult)InteractionResult.CONSUME;
/*     */       }
/*     */     } 
/*     */     
/* 165 */     return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playEatingSound() {}
/*     */   
/*     */   public boolean canFallInLove() {
/* 172 */     return (this.inLove <= 0);
/*     */   }
/*     */   
/*     */   public void setInLove(Player paramPlayer) {
/* 176 */     this.inLove = 600;
/*     */     
/* 178 */     if (paramPlayer instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramPlayer;
/* 179 */       this.loveCause = EntityReference.of((UniquelyIdentifyable)serverPlayer); }
/*     */ 
/*     */     
/* 182 */     level().broadcastEntityEvent((Entity)this, (byte)18);
/*     */   }
/*     */   
/*     */   public void setInLoveTime(int paramInt) {
/* 186 */     this.inLove = paramInt;
/*     */   }
/*     */   
/*     */   public int getInLoveTime() {
/* 190 */     return this.inLove;
/*     */   }
/*     */   
/*     */   public ServerPlayer getLoveCause() {
/* 194 */     return (ServerPlayer)EntityReference.get(this.loveCause, level(), ServerPlayer.class);
/*     */   }
/*     */   
/*     */   public boolean isInLove() {
/* 198 */     return (this.inLove > 0);
/*     */   }
/*     */   
/*     */   public void resetLove() {
/* 202 */     this.inLove = 0;
/*     */   }
/*     */   
/*     */   public boolean canMate(Animal paramAnimal) {
/* 206 */     if (paramAnimal == this) {
/* 207 */       return false;
/*     */     }
/* 209 */     if (paramAnimal.getClass() != getClass()) {
/* 210 */       return false;
/*     */     }
/* 212 */     return (isInLove() && paramAnimal.isInLove());
/*     */   }
/*     */   
/*     */   public void spawnChildFromBreeding(ServerLevel paramServerLevel, Animal paramAnimal) {
/* 216 */     AgeableMob ageableMob = getBreedOffspring(paramServerLevel, paramAnimal);
/* 217 */     if (ageableMob == null) {
/*     */       return;
/*     */     }
/* 220 */     ageableMob.setBaby(true);
/* 221 */     ageableMob.snapTo(getX(), getY(), getZ(), 0.0F, 0.0F);
/*     */     
/* 223 */     finalizeSpawnChildFromBreeding(paramServerLevel, paramAnimal, ageableMob);
/* 224 */     paramServerLevel.addFreshEntityWithPassengers((Entity)ageableMob);
/*     */   }
/*     */   
/*     */   public void finalizeSpawnChildFromBreeding(ServerLevel paramServerLevel, Animal paramAnimal, AgeableMob paramAgeableMob) {
/* 228 */     Optional.<ServerPlayer>ofNullable(getLoveCause())
/* 229 */       .or(() -> Optional.ofNullable(paramAnimal.getLoveCause()))
/* 230 */       .ifPresent(paramServerPlayer -> {
/*     */           paramServerPlayer.awardStat(Stats.ANIMALS_BRED);
/*     */           
/*     */           CriteriaTriggers.BRED_ANIMALS.trigger(paramServerPlayer, this, paramAnimal, paramAgeableMob);
/*     */         });
/* 235 */     setAge(6000);
/* 236 */     paramAnimal.setAge(6000);
/* 237 */     resetLove();
/* 238 */     paramAnimal.resetLove();
/*     */     
/* 240 */     paramServerLevel.broadcastEntityEvent((Entity)this, (byte)18);
/*     */     
/* 242 */     if (((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_DROPS)).booleanValue()) {
/* 243 */       paramServerLevel.addFreshEntity((Entity)new ExperienceOrb((Level)paramServerLevel, getX(), getY(), getZ(), getRandom().nextInt(7) + 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 249 */     if (paramByte == 18) {
/* 250 */       for (byte b = 0; b < 7; b++) {
/* 251 */         double d1 = this.random.nextGaussian() * 0.02D;
/* 252 */         double d2 = this.random.nextGaussian() * 0.02D;
/* 253 */         double d3 = this.random.nextGaussian() * 0.02D;
/* 254 */         level().addParticle((ParticleOptions)ParticleTypes.HEART, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), d1, d2, d3);
/*     */       } 
/*     */     } else {
/* 257 */       super.handleEntityEvent(paramByte);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity paramLivingEntity) {
/* 263 */     Direction direction = getMotionDirection();
/* 264 */     if (direction.getAxis() == Direction.Axis.Y) {
/* 265 */       return super.getDismountLocationForPassenger(paramLivingEntity);
/*     */     }
/*     */     
/* 268 */     int[][] arrayOfInt = DismountHelper.offsetsForDirection(direction);
/* 269 */     BlockPos blockPos = blockPosition();
/* 270 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 272 */     for (UnmodifiableIterator<Pose> unmodifiableIterator = paramLivingEntity.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) { Pose pose = unmodifiableIterator.next();
/* 273 */       AABB aABB = paramLivingEntity.getLocalBoundsForPose(pose);
/*     */       
/* 275 */       for (int[] arrayOfInt1 : arrayOfInt) {
/* 276 */         mutableBlockPos.set(blockPos.getX() + arrayOfInt1[0], blockPos.getY(), blockPos.getZ() + arrayOfInt1[1]);
/*     */         
/* 278 */         double d = level().getBlockFloorHeight((BlockPos)mutableBlockPos);
/* 279 */         if (DismountHelper.isBlockFloorValid(d)) {
/*     */ 
/*     */ 
/*     */           
/* 283 */           Vec3 vec3 = Vec3.upFromBottomCenterOf((Vec3i)mutableBlockPos, d);
/* 284 */           if (DismountHelper.canDismountTo((CollisionGetter)level(), paramLivingEntity, aABB.move(vec3))) {
/* 285 */             paramLivingEntity.setPose(pose);
/* 286 */             return vec3;
/*     */           } 
/*     */         } 
/*     */       }  }
/*     */     
/* 291 */     return super.getDismountLocationForPassenger(paramLivingEntity);
/*     */   }
/*     */   
/*     */   public abstract boolean isFood(ItemStack paramItemStack);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\Animal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */