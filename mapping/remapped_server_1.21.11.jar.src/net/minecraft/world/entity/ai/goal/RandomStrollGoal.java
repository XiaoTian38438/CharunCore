/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class RandomStrollGoal
/*    */   extends Goal
/*    */ {
/*    */   public static final int DEFAULT_INTERVAL = 120;
/*    */   protected final PathfinderMob mob;
/*    */   protected double wantedX;
/*    */   protected double wantedY;
/*    */   protected double wantedZ;
/*    */   protected final double speedModifier;
/*    */   protected int interval;
/*    */   protected boolean forceTrigger;
/*    */   private final boolean checkNoActionTime;
/*    */   
/*    */   public RandomStrollGoal(PathfinderMob paramPathfinderMob, double paramDouble) {
/* 23 */     this(paramPathfinderMob, paramDouble, 120);
/*    */   }
/*    */   
/*    */   public RandomStrollGoal(PathfinderMob paramPathfinderMob, double paramDouble, int paramInt) {
/* 27 */     this(paramPathfinderMob, paramDouble, paramInt, true);
/*    */   }
/*    */   
/*    */   public RandomStrollGoal(PathfinderMob paramPathfinderMob, double paramDouble, int paramInt, boolean paramBoolean) {
/* 31 */     this.mob = paramPathfinderMob;
/* 32 */     this.speedModifier = paramDouble;
/* 33 */     this.interval = paramInt;
/* 34 */     this.checkNoActionTime = paramBoolean;
/* 35 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 40 */     if (this.mob.hasControllingPassenger()) {
/* 41 */       return false;
/*    */     }
/* 43 */     if (!this.forceTrigger) {
/* 44 */       if (this.checkNoActionTime && this.mob.getNoActionTime() >= 100) {
/* 45 */         return false;
/*    */       }
/* 47 */       if (this.mob.getRandom().nextInt(reducedTickDelay(this.interval)) != 0) {
/* 48 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 52 */     Vec3 vec3 = getPosition();
/*    */     
/* 54 */     if (vec3 == null) {
/* 55 */       return false;
/*    */     }
/*    */     
/* 58 */     this.wantedX = vec3.x;
/* 59 */     this.wantedY = vec3.y;
/* 60 */     this.wantedZ = vec3.z;
/* 61 */     this.forceTrigger = false;
/* 62 */     return true;
/*    */   }
/*    */   
/*    */   protected Vec3 getPosition() {
/* 66 */     return DefaultRandomPos.getPos(this.mob, 10, 7);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 71 */     return (!this.mob.getNavigation().isDone() && !this.mob.hasControllingPassenger());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 76 */     this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 81 */     this.mob.getNavigation().stop();
/* 82 */     super.stop();
/*    */   }
/*    */   
/*    */   public void trigger() {
/* 86 */     this.forceTrigger = true;
/*    */   }
/*    */   
/*    */   public void setInterval(int paramInt) {
/* 90 */     this.interval = paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\RandomStrollGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */