/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WaterAvoidingRandomStrollGoal
/*    */   extends RandomStrollGoal
/*    */ {
/*    */   public static final float PROBABILITY = 0.001F;
/*    */   protected final float probability;
/*    */   
/*    */   public WaterAvoidingRandomStrollGoal(PathfinderMob paramPathfinderMob, double paramDouble) {
/* 14 */     this(paramPathfinderMob, paramDouble, 0.001F);
/*    */   }
/*    */   
/*    */   public WaterAvoidingRandomStrollGoal(PathfinderMob paramPathfinderMob, double paramDouble, float paramFloat) {
/* 18 */     super(paramPathfinderMob, paramDouble);
/* 19 */     this.probability = paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getPosition() {
/* 24 */     if (this.mob.isInWater()) {
/*    */       
/* 26 */       Vec3 vec3 = LandRandomPos.getPos(this.mob, 15, 7);
/* 27 */       return (vec3 == null) ? super.getPosition() : vec3;
/*    */     } 
/* 29 */     if (this.mob.getRandom().nextFloat() >= this.probability) {
/* 30 */       return LandRandomPos.getPos(this.mob, 10, 7);
/*    */     }
/* 32 */     return super.getPosition();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\WaterAvoidingRandomStrollGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */