/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
/*    */ import net.minecraft.world.entity.ai.util.HoverRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WaterAvoidingRandomFlyingGoal
/*    */   extends WaterAvoidingRandomStrollGoal
/*    */ {
/*    */   public WaterAvoidingRandomFlyingGoal(PathfinderMob paramPathfinderMob, double paramDouble) {
/* 12 */     super(paramPathfinderMob, paramDouble);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getPosition() {
/* 17 */     Vec3 vec31 = this.mob.getViewVector(0.0F);
/*    */     
/* 19 */     byte b = 8;
/* 20 */     Vec3 vec32 = HoverRandomPos.getPos(this.mob, 8, 7, vec31.x, vec31.z, 1.5707964F, 3, 1);
/* 21 */     if (vec32 != null) {
/* 22 */       return vec32;
/*    */     }
/*    */ 
/*    */     
/* 26 */     return AirAndWaterRandomPos.getPos(this.mob, 8, 4, -2, vec31.x, vec31.z, 1.5707963705062866D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\WaterAvoidingRandomFlyingGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */