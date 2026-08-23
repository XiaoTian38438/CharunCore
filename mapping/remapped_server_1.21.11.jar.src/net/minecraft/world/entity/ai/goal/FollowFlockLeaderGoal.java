/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
/*    */ 
/*    */ public class FollowFlockLeaderGoal
/*    */   extends Goal
/*    */ {
/*    */   private static final int INTERVAL_TICKS = 200;
/*    */   private final AbstractSchoolingFish mob;
/*    */   private int timeToRecalcPath;
/*    */   private int nextStartTick;
/*    */   
/*    */   public FollowFlockLeaderGoal(AbstractSchoolingFish paramAbstractSchoolingFish) {
/* 17 */     this.mob = paramAbstractSchoolingFish;
/* 18 */     this.nextStartTick = nextStartTick(paramAbstractSchoolingFish);
/*    */   }
/*    */   
/*    */   protected int nextStartTick(AbstractSchoolingFish paramAbstractSchoolingFish) {
/* 22 */     return reducedTickDelay(200 + paramAbstractSchoolingFish.getRandom().nextInt(200) % 20);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 27 */     if (this.mob.hasFollowers()) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     if (this.mob.isFollower()) {
/* 32 */       return true;
/*    */     }
/*    */     
/* 35 */     if (this.nextStartTick > 0) {
/* 36 */       this.nextStartTick--;
/* 37 */       return false;
/*    */     } 
/*    */     
/* 40 */     this.nextStartTick = nextStartTick(this.mob);
/*    */     
/* 42 */     Predicate predicate = paramAbstractSchoolingFish -> (paramAbstractSchoolingFish.canBeFollowed() || !paramAbstractSchoolingFish.isFollower());
/* 43 */     List list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
/*    */     
/* 45 */     AbstractSchoolingFish abstractSchoolingFish = (AbstractSchoolingFish)DataFixUtils.orElse(list.stream().filter(AbstractSchoolingFish::canBeFollowed).findAny(), this.mob);
/*    */     
/* 47 */     abstractSchoolingFish.addFollowers(list.stream().filter(paramAbstractSchoolingFish -> !paramAbstractSchoolingFish.isFollower()));
/*    */     
/* 49 */     return this.mob.isFollower();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 54 */     return (this.mob.isFollower() && this.mob.inRangeOfLeader());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 59 */     this.timeToRecalcPath = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 64 */     this.mob.stopFollowing();
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 69 */     if (--this.timeToRecalcPath > 0) {
/*    */       return;
/*    */     }
/* 72 */     this.timeToRecalcPath = adjustedTickDelay(10);
/*    */     
/* 74 */     this.mob.pathToLeader();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\FollowFlockLeaderGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */