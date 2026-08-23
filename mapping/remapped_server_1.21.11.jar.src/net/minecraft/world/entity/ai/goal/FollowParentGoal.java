/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ 
/*    */ public class FollowParentGoal
/*    */   extends Goal {
/*    */   public static final int HORIZONTAL_SCAN_RANGE = 8;
/*    */   public static final int VERTICAL_SCAN_RANGE = 4;
/*    */   public static final int DONT_FOLLOW_IF_CLOSER_THAN = 3;
/*    */   private final Animal animal;
/*    */   private Animal parent;
/*    */   private final double speedModifier;
/*    */   private int timeToRecalcPath;
/*    */   
/*    */   public FollowParentGoal(Animal paramAnimal, double paramDouble) {
/* 18 */     this.animal = paramAnimal;
/* 19 */     this.speedModifier = paramDouble;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 24 */     if (this.animal.getAge() >= 0) {
/* 25 */       return false;
/*    */     }
/*    */     
/* 28 */     List list = this.animal.level().getEntitiesOfClass(this.animal.getClass(), this.animal.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
/*    */     
/* 30 */     Animal animal = null;
/* 31 */     double d = Double.MAX_VALUE;
/* 32 */     for (Animal animal1 : list) {
/* 33 */       if (animal1.getAge() < 0) {
/*    */         continue;
/*    */       }
/* 36 */       double d1 = this.animal.distanceToSqr((Entity)animal1);
/* 37 */       if (d1 > d) {
/*    */         continue;
/*    */       }
/* 40 */       d = d1;
/* 41 */       animal = animal1;
/*    */     } 
/*    */     
/* 44 */     if (animal == null) {
/* 45 */       return false;
/*    */     }
/* 47 */     if (d < 9.0D) {
/* 48 */       return false;
/*    */     }
/* 50 */     this.parent = animal;
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 56 */     if (this.animal.getAge() >= 0) {
/* 57 */       return false;
/*    */     }
/* 59 */     if (!this.parent.isAlive()) {
/* 60 */       return false;
/*    */     }
/* 62 */     double d = this.animal.distanceToSqr((Entity)this.parent);
/* 63 */     if (d < 9.0D || d > 256.0D) {
/* 64 */       return false;
/*    */     }
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 71 */     this.timeToRecalcPath = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 76 */     this.parent = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 81 */     if (--this.timeToRecalcPath > 0) {
/*    */       return;
/*    */     }
/* 84 */     this.timeToRecalcPath = adjustedTickDelay(10);
/* 85 */     this.animal.getNavigation().moveTo((Entity)this.parent, this.speedModifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\FollowParentGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */