/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RandomSwimmingGoal
/*    */   extends RandomStrollGoal {
/*    */   public RandomSwimmingGoal(PathfinderMob paramPathfinderMob, double paramDouble, int paramInt) {
/* 10 */     super(paramPathfinderMob, paramDouble, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getPosition() {
/* 15 */     return BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 7);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\RandomSwimmingGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */