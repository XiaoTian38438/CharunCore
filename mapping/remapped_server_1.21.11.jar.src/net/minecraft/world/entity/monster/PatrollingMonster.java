/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class PatrollingMonster extends Monster {
/*     */   private static final boolean DEFAULT_PATROL_LEADER = false;
/*     */   private static final boolean DEFAULT_PATROLLING = false;
/*     */   private BlockPos patrolTarget;
/*     */   private boolean patrolLeader = false;
/*     */   private boolean patrolling = false;
/*     */   
/*     */   protected PatrollingMonster(EntityType<? extends PatrollingMonster> paramEntityType, Level paramLevel) {
/*  37 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  42 */     super.registerGoals();
/*  43 */     this.goalSelector.addGoal(4, new LongDistancePatrolGoal<>(this, 0.7D, 0.595D));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  48 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/*  50 */     paramValueOutput.storeNullable("patrol_target", BlockPos.CODEC, this.patrolTarget);
/*     */     
/*  52 */     paramValueOutput.putBoolean("PatrolLeader", this.patrolLeader);
/*  53 */     paramValueOutput.putBoolean("Patrolling", this.patrolling);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  58 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/*  60 */     this.patrolTarget = paramValueInput.read("patrol_target", BlockPos.CODEC).orElse(null);
/*     */     
/*  62 */     this.patrolLeader = paramValueInput.getBooleanOr("PatrolLeader", false);
/*  63 */     this.patrolling = paramValueInput.getBooleanOr("Patrolling", false);
/*     */   }
/*     */   
/*     */   public boolean canBeLeader() {
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/*  74 */     if (paramEntitySpawnReason != EntitySpawnReason.PATROL && paramEntitySpawnReason != EntitySpawnReason.EVENT && paramEntitySpawnReason != EntitySpawnReason.STRUCTURE && 
/*  75 */       paramServerLevelAccessor.getRandom().nextFloat() < 0.06F && canBeLeader()) {
/*  76 */       this.patrolLeader = true;
/*     */     }
/*     */ 
/*     */     
/*  80 */     if (isPatrolLeader()) {
/*  81 */       setItemSlot(EquipmentSlot.HEAD, Raid.getOminousBannerInstance((HolderGetter)registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)));
/*  82 */       setDropChance(EquipmentSlot.HEAD, 2.0F);
/*     */     } 
/*     */     
/*  85 */     if (paramEntitySpawnReason == EntitySpawnReason.PATROL) {
/*  86 */       this.patrolling = true;
/*     */     }
/*     */     
/*  89 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */   
/*     */   public static boolean checkPatrollingMonsterSpawnRules(EntityType<? extends PatrollingMonster> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  93 */     if (paramLevelAccessor.getBrightness(LightLayer.BLOCK, paramBlockPos) > 8) {
/*  94 */       return false;
/*     */     }
/*     */     
/*  97 */     return checkAnyLightMonsterSpawnRules((EntityType)paramEntityType, paramLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double paramDouble) {
/* 102 */     return (!this.patrolling || paramDouble > 16384.0D);
/*     */   }
/*     */   
/*     */   public void setPatrolTarget(BlockPos paramBlockPos) {
/* 106 */     this.patrolTarget = paramBlockPos;
/* 107 */     this.patrolling = true;
/*     */   }
/*     */   
/*     */   public BlockPos getPatrolTarget() {
/* 111 */     return this.patrolTarget;
/*     */   }
/*     */   
/*     */   public boolean hasPatrolTarget() {
/* 115 */     return (this.patrolTarget != null);
/*     */   }
/*     */   
/*     */   public void setPatrolLeader(boolean paramBoolean) {
/* 119 */     this.patrolLeader = paramBoolean;
/* 120 */     this.patrolling = true;
/*     */   }
/*     */   
/*     */   public boolean isPatrolLeader() {
/* 124 */     return this.patrolLeader;
/*     */   }
/*     */   
/*     */   public boolean canJoinPatrol() {
/* 128 */     return true;
/*     */   }
/*     */   
/*     */   public void findPatrolTarget() {
/* 132 */     this.patrolTarget = blockPosition().offset(-500 + this.random.nextInt(1000), 0, -500 + this.random.nextInt(1000));
/* 133 */     this.patrolling = true;
/*     */   }
/*     */   
/*     */   protected boolean isPatrolling() {
/* 137 */     return this.patrolling;
/*     */   }
/*     */   
/*     */   protected void setPatrolling(boolean paramBoolean) {
/* 141 */     this.patrolling = paramBoolean;
/*     */   }
/*     */   
/*     */   public static class LongDistancePatrolGoal<T extends PatrollingMonster>
/*     */     extends Goal {
/*     */     private static final int NAVIGATION_FAILED_COOLDOWN = 200;
/*     */     private final T mob;
/*     */     private final double speedModifier;
/*     */     private final double leaderSpeedModifier;
/*     */     private long cooldownUntil;
/*     */     
/*     */     public LongDistancePatrolGoal(T param1T, double param1Double1, double param1Double2) {
/* 153 */       this.mob = param1T;
/* 154 */       this.speedModifier = param1Double1;
/* 155 */       this.leaderSpeedModifier = param1Double2;
/* 156 */       this.cooldownUntil = -1L;
/* 157 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 162 */       boolean bool = (this.mob.level().getGameTime() < this.cooldownUntil) ? true : false;
/* 163 */       return (this.mob.isPatrolling() && this.mob.getTarget() == null && !this.mob.hasControllingPassenger() && this.mob.hasPatrolTarget() && !bool);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void stop() {}
/*     */ 
/*     */     
/*     */     public void tick() {
/* 176 */       boolean bool = this.mob.isPatrolLeader();
/* 177 */       PathNavigation pathNavigation = this.mob.getNavigation();
/* 178 */       if (pathNavigation.isDone()) {
/* 179 */         List<PatrollingMonster> list = findPatrolCompanions();
/* 180 */         if (this.mob.isPatrolling() && list.isEmpty()) {
/* 181 */           this.mob.setPatrolling(false);
/* 182 */         } else if (!bool || !this.mob.getPatrolTarget().closerToCenterThan((Position)this.mob.position(), 10.0D)) {
/* 183 */           Vec3 vec31 = Vec3.atBottomCenterOf((Vec3i)this.mob.getPatrolTarget());
/*     */ 
/*     */           
/* 186 */           Vec3 vec32 = this.mob.position();
/* 187 */           Vec3 vec33 = vec32.subtract(vec31);
/*     */           
/* 189 */           vec31 = vec33.yRot(90.0F).scale(0.4D).add(vec31);
/*     */           
/* 191 */           Vec3 vec34 = vec31.subtract(vec32).normalize().scale(10.0D).add(vec32);
/* 192 */           BlockPos blockPos = BlockPos.containing((Position)vec34);
/* 193 */           blockPos = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockPos);
/*     */           
/* 195 */           if (!pathNavigation.moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), bool ? this.leaderSpeedModifier : this.speedModifier)) {
/*     */             
/* 197 */             moveRandomly();
/* 198 */             this.cooldownUntil = this.mob.level().getGameTime() + 200L;
/* 199 */           } else if (bool) {
/* 200 */             for (PatrollingMonster patrollingMonster : list) {
/* 201 */               patrollingMonster.setPatrolTarget(blockPos);
/*     */             }
/*     */           } 
/*     */         } else {
/* 205 */           this.mob.findPatrolTarget();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private List<PatrollingMonster> findPatrolCompanions() {
/* 211 */       return this.mob.level().getEntitiesOfClass(PatrollingMonster.class, this.mob.getBoundingBox().inflate(16.0D), param1PatrollingMonster -> (param1PatrollingMonster.canJoinPatrol() && !param1PatrollingMonster.is((Entity)this.mob)));
/*     */     }
/*     */     
/*     */     private boolean moveRandomly() {
/* 215 */       RandomSource randomSource = this.mob.getRandom();
/* 216 */       BlockPos blockPos = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + randomSource.nextInt(16), 0, -8 + randomSource.nextInt(16)));
/* 217 */       return this.mob.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.speedModifier);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\PatrollingMonster.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */