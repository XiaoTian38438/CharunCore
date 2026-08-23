/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Ravager
/*     */   extends Raider {
/*     */   private static final Predicate<Entity> ROAR_TARGET_WITH_GRIEFING;
/*     */   
/*     */   static {
/*  51 */     ROAR_TARGET_WITH_GRIEFING = (paramEntity -> (!(paramEntity instanceof Ravager) && paramEntity.isAlive()));
/*     */     
/*  53 */     ROAR_TARGET_WITHOUT_GRIEFING = (paramEntity -> (ROAR_TARGET_WITH_GRIEFING.test(paramEntity) && !paramEntity.getType().equals(EntityType.ARMOR_STAND)));
/*     */     
/*  55 */     ROAR_TARGET_ON_CLIENT = (paramLivingEntity -> (!(paramLivingEntity instanceof Ravager) && paramLivingEntity.isAlive() && paramLivingEntity.isLocalInstanceAuthoritative()));
/*     */   }
/*     */   private static final Predicate<Entity> ROAR_TARGET_WITHOUT_GRIEFING;
/*     */   private static final Predicate<LivingEntity> ROAR_TARGET_ON_CLIENT;
/*     */   private static final double BASE_MOVEMENT_SPEED = 0.3D;
/*     */   private static final double ATTACK_MOVEMENT_SPEED = 0.35D;
/*     */   private static final int STUNNED_COLOR = 8356754;
/*     */   private static final float STUNNED_COLOR_BLUE = 0.57254905F;
/*     */   private static final float STUNNED_COLOR_GREEN = 0.5137255F;
/*     */   private static final float STUNNED_COLOR_RED = 0.49803922F;
/*     */   public static final int ATTACK_DURATION = 10;
/*     */   public static final int STUN_DURATION = 40;
/*     */   private static final int DEFAULT_ATTACK_TICK = 0;
/*     */   private static final int DEFAULT_STUN_TICK = 0;
/*     */   private static final int DEFAULT_ROAR_TICK = 0;
/*  70 */   private int attackTick = 0;
/*  71 */   private int stunnedTick = 0;
/*  72 */   private int roarTick = 0;
/*     */   
/*     */   public Ravager(EntityType<? extends Ravager> paramEntityType, Level paramLevel) {
/*  75 */     super(paramEntityType, paramLevel);
/*     */     
/*  77 */     this.xpReward = 20;
/*     */     
/*  79 */     setPathfindingMalus(PathType.LEAVES, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  84 */     super.registerGoals();
/*     */     
/*  86 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  87 */     this.goalSelector.addGoal(4, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.0D, true));
/*  88 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.4D));
/*  89 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  90 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*     */     
/*  92 */     this.targetSelector.addGoal(2, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  93 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  94 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, true, (paramLivingEntity, paramServerLevel) -> !paramLivingEntity.isBaby()));
/*  95 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateControlFlags() {
/* 100 */     boolean bool1 = (!(getControllingPassenger() instanceof Mob) || getControllingPassenger().getType().is(EntityTypeTags.RAIDERS)) ? true : false;
/* 101 */     boolean bool2 = !(getVehicle() instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat) ? true : false;
/* 102 */     this.goalSelector.setControlFlag(Goal.Flag.MOVE, bool1);
/* 103 */     this.goalSelector.setControlFlag(Goal.Flag.JUMP, (bool1 && bool2));
/* 104 */     this.goalSelector.setControlFlag(Goal.Flag.LOOK, bool1);
/* 105 */     this.goalSelector.setControlFlag(Goal.Flag.TARGET, bool1);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 109 */     return Monster.createMonsterAttributes()
/* 110 */       .add(Attributes.MAX_HEALTH, 100.0D)
/* 111 */       .add(Attributes.MOVEMENT_SPEED, 0.3D)
/* 112 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
/* 113 */       .add(Attributes.ATTACK_DAMAGE, 12.0D)
/* 114 */       .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
/* 115 */       .add(Attributes.FOLLOW_RANGE, 32.0D)
/* 116 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 121 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 123 */     paramValueOutput.putInt("AttackTick", this.attackTick);
/* 124 */     paramValueOutput.putInt("StunTick", this.stunnedTick);
/* 125 */     paramValueOutput.putInt("RoarTick", this.roarTick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 130 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/* 132 */     this.attackTick = paramValueInput.getIntOr("AttackTick", 0);
/* 133 */     this.stunnedTick = paramValueInput.getIntOr("StunTick", 0);
/* 134 */     this.roarTick = paramValueInput.getIntOr("RoarTick", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 139 */     return SoundEvents.RAVAGER_CELEBRATE;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 144 */     return 45;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 149 */     super.aiStep();
/*     */     
/* 151 */     if (!isAlive()) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     if (isImmobile()) {
/* 156 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
/*     */     } else {
/* 158 */       double d1 = (getTarget() != null) ? 0.35D : 0.3D;
/* 159 */       double d2 = getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
/* 160 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.lerp(0.1D, d2, d1));
/*     */     } 
/*     */     
/* 163 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 164 */       if (this.horizontalCollision && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 165 */         boolean bool = false;
/* 166 */         AABB aABB = getBoundingBox().inflate(0.2D);
/* 167 */         for (BlockPos blockPos : BlockPos.betweenClosed(Mth.floor(aABB.minX), Mth.floor(aABB.minY), Mth.floor(aABB.minZ), Mth.floor(aABB.maxX), Mth.floor(aABB.maxY), Mth.floor(aABB.maxZ))) {
/* 168 */           BlockState blockState = serverLevel.getBlockState(blockPos);
/* 169 */           Block block = blockState.getBlock();
/* 170 */           if (block instanceof net.minecraft.world.level.block.LeavesBlock) {
/* 171 */             bool = (serverLevel.destroyBlock(blockPos, true, (Entity)this) || bool) ? true : false;
/*     */           }
/*     */         } 
/*     */         
/* 175 */         if (!bool && onGround()) {
/* 176 */           jumpFromGround();
/*     */         }
/*     */       }  }
/*     */ 
/*     */     
/* 181 */     if (this.roarTick > 0) {
/* 182 */       this.roarTick--;
/*     */       
/* 184 */       if (this.roarTick == 10) {
/* 185 */         roar();
/*     */       }
/*     */     } 
/* 188 */     if (this.attackTick > 0) {
/* 189 */       this.attackTick--;
/*     */     }
/* 191 */     if (this.stunnedTick > 0) {
/* 192 */       this.stunnedTick--;
/* 193 */       stunEffect();
/*     */       
/* 195 */       if (this.stunnedTick == 0) {
/* 196 */         playSound(SoundEvents.RAVAGER_ROAR, 1.0F, 1.0F);
/* 197 */         this.roarTick = 20;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stunEffect() {
/* 203 */     if (this.random.nextInt(6) == 0) {
/* 204 */       double d1 = getX() - getBbWidth() * Math.sin((this.yBodyRot * 0.017453292F)) + this.random.nextDouble() * 0.6D - 0.3D;
/* 205 */       double d2 = getY() + getBbHeight() - 0.3D;
/* 206 */       double d3 = getZ() + getBbWidth() * Math.cos((this.yBodyRot * 0.017453292F)) + this.random.nextDouble() * 0.6D - 0.3D;
/*     */       
/* 208 */       level().addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.49803922F, 0.5137255F, 0.57254905F), d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isImmobile() {
/* 214 */     return (super.isImmobile() || this.attackTick > 0 || this.stunnedTick > 0 || this.roarTick > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLineOfSight(Entity paramEntity) {
/* 219 */     if (this.stunnedTick > 0 || this.roarTick > 0) {
/* 220 */       return false;
/*     */     }
/* 222 */     return super.hasLineOfSight(paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void blockedByItem(LivingEntity paramLivingEntity) {
/* 227 */     if (this.roarTick == 0) {
/* 228 */       if (this.random.nextDouble() < 0.5D) {
/* 229 */         this.stunnedTick = 40;
/* 230 */         playSound(SoundEvents.RAVAGER_STUNNED, 1.0F, 1.0F);
/* 231 */         level().broadcastEntityEvent((Entity)this, (byte)39);
/*     */         
/* 233 */         paramLivingEntity.push((Entity)this);
/*     */       } else {
/* 235 */         strongKnockback((Entity)paramLivingEntity);
/*     */       } 
/* 237 */       paramLivingEntity.hurtMarked = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void roar() {
/* 242 */     if (isAlive()) { Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 243 */         Predicate<Entity> predicate = ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() ? ROAR_TARGET_WITH_GRIEFING : ROAR_TARGET_WITHOUT_GRIEFING;
/* 244 */         List list = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D), predicate);
/* 245 */         for (LivingEntity livingEntity : list) {
/* 246 */           if (!(livingEntity instanceof net.minecraft.world.entity.monster.illager.AbstractIllager)) {
/* 247 */             livingEntity.hurtServer(serverLevel, damageSources().mobAttack((LivingEntity)this), 6.0F);
/*     */           }
/*     */           
/* 250 */           if (!(livingEntity instanceof Player)) {
/* 251 */             strongKnockback((Entity)livingEntity);
/*     */           }
/*     */         } 
/*     */         
/* 255 */         gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 256 */         serverLevel.broadcastEntityEvent((Entity)this, (byte)69); }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyRoarKnockbackClient() {
/* 264 */     List list = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D), ROAR_TARGET_ON_CLIENT);
/* 265 */     for (LivingEntity livingEntity : list) {
/* 266 */       strongKnockback((Entity)livingEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void strongKnockback(Entity paramEntity) {
/* 272 */     double d1 = paramEntity.getX() - getX();
/* 273 */     double d2 = paramEntity.getZ() - getZ();
/* 274 */     double d3 = Math.max(d1 * d1 + d2 * d2, 0.001D);
/* 275 */     paramEntity.push(d1 / d3 * 4.0D, 0.2D, d2 / d3 * 4.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 280 */     if (paramByte == 4) {
/* 281 */       this.attackTick = 10;
/* 282 */       playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
/* 283 */     } else if (paramByte == 39) {
/* 284 */       this.stunnedTick = 40;
/* 285 */     } else if (paramByte == 69) {
/* 286 */       addRoarParticleEffects();
/* 287 */       applyRoarKnockbackClient();
/*     */     } 
/* 289 */     super.handleEntityEvent(paramByte);
/*     */   }
/*     */   
/*     */   private void addRoarParticleEffects() {
/* 293 */     Vec3 vec3 = getBoundingBox().getCenter();
/* 294 */     for (byte b = 0; b < 40; b++) {
/* 295 */       double d1 = this.random.nextGaussian() * 0.2D;
/* 296 */       double d2 = this.random.nextGaussian() * 0.2D;
/* 297 */       double d3 = this.random.nextGaussian() * 0.2D;
/* 298 */       level().addParticle((ParticleOptions)ParticleTypes.POOF, vec3.x, vec3.y, vec3.z, d1, d2, d3);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getAttackTick() {
/* 303 */     return this.attackTick;
/*     */   }
/*     */   
/*     */   public int getStunnedTick() {
/* 307 */     return this.stunnedTick;
/*     */   }
/*     */   
/*     */   public int getRoarTick() {
/* 311 */     return this.roarTick;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel paramServerLevel, Entity paramEntity) {
/* 316 */     this.attackTick = 10;
/* 317 */     paramServerLevel.broadcastEntityEvent((Entity)this, (byte)4);
/* 318 */     playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
/*     */     
/* 320 */     return super.doHurtTarget(paramServerLevel, paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 325 */     return SoundEvents.RAVAGER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 330 */     return SoundEvents.RAVAGER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 335 */     return SoundEvents.RAVAGER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 340 */     playSound(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader paramLevelReader) {
/* 345 */     return !paramLevelReader.containsAnyLiquid(getBoundingBox());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel paramServerLevel, int paramInt, boolean paramBoolean) {}
/*     */ 
/*     */   
/*     */   public boolean canBeLeader() {
/* 354 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AABB getAttackBoundingBox(double paramDouble) {
/* 360 */     AABB aABB = super.getAttackBoundingBox(paramDouble);
/* 361 */     return aABB.deflate(0.05D, 0.0D, 0.05D);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Ravager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */