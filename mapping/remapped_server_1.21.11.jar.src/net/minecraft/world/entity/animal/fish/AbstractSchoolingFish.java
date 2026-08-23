/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ public abstract class AbstractSchoolingFish extends AbstractFish {
/*     */   private AbstractSchoolingFish leader;
/*  17 */   private int schoolSize = 1;
/*     */   
/*     */   public AbstractSchoolingFish(EntityType<? extends AbstractSchoolingFish> paramEntityType, Level paramLevel) {
/*  20 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  25 */     super.registerGoals();
/*     */     
/*  27 */     this.goalSelector.addGoal(5, (Goal)new FollowFlockLeaderGoal(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/*  32 */     return getMaxSchoolSize();
/*     */   }
/*     */   
/*     */   public int getMaxSchoolSize() {
/*  36 */     return super.getMaxSpawnClusterSize();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRandomSwim() {
/*  41 */     return !isFollower();
/*     */   }
/*     */   
/*     */   public boolean isFollower() {
/*  45 */     return (this.leader != null && this.leader.isAlive());
/*     */   }
/*     */   
/*     */   public AbstractSchoolingFish startFollowing(AbstractSchoolingFish paramAbstractSchoolingFish) {
/*  49 */     this.leader = paramAbstractSchoolingFish;
/*  50 */     paramAbstractSchoolingFish.addFollower();
/*     */     
/*  52 */     return paramAbstractSchoolingFish;
/*     */   }
/*     */   
/*     */   public void stopFollowing() {
/*  56 */     this.leader.removeFollower();
/*  57 */     this.leader = null;
/*     */   }
/*     */   
/*     */   private void addFollower() {
/*  61 */     this.schoolSize++;
/*     */   }
/*     */   
/*     */   private void removeFollower() {
/*  65 */     this.schoolSize--;
/*     */   }
/*     */   
/*     */   public boolean canBeFollowed() {
/*  69 */     return (hasFollowers() && this.schoolSize < getMaxSchoolSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  74 */     super.tick();
/*     */ 
/*     */     
/*  77 */     if (hasFollowers() && (level()).random.nextInt(200) == 1) {
/*  78 */       List list = level().getEntitiesOfClass(getClass(), getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/*  79 */       if (list.size() <= 1) {
/*  80 */         this.schoolSize = 1;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasFollowers() {
/*  86 */     return (this.schoolSize > 1);
/*     */   }
/*     */   
/*     */   public boolean inRangeOfLeader() {
/*  90 */     return (distanceToSqr((Entity)this.leader) <= 121.0D);
/*     */   }
/*     */   
/*     */   public void pathToLeader() {
/*  94 */     if (isFollower()) {
/*  95 */       getNavigation().moveTo((Entity)this.leader, 1.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addFollowers(Stream<? extends AbstractSchoolingFish> paramStream) {
/* 100 */     paramStream.limit((getMaxSchoolSize() - this.schoolSize)).filter(paramAbstractSchoolingFish -> (paramAbstractSchoolingFish != this)).forEach(paramAbstractSchoolingFish -> paramAbstractSchoolingFish.startFollowing(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 105 */     super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */     
/* 107 */     if (paramSpawnGroupData == null) {
/* 108 */       paramSpawnGroupData = new SchoolSpawnGroupData(this);
/*     */     } else {
/* 110 */       startFollowing(((SchoolSpawnGroupData)paramSpawnGroupData).leader);
/*     */     } 
/*     */     
/* 113 */     return paramSpawnGroupData;
/*     */   }
/*     */   
/*     */   public static class SchoolSpawnGroupData implements SpawnGroupData {
/*     */     public final AbstractSchoolingFish leader;
/*     */     
/*     */     public SchoolSpawnGroupData(AbstractSchoolingFish param1AbstractSchoolingFish) {
/* 120 */       this.leader = param1AbstractSchoolingFish;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\fish\AbstractSchoolingFish.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */