/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class AirRandomPos
/*    */ {
/*    */   public static Vec3 getPosTowards(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2, int paramInt3, Vec3 paramVec3, double paramDouble) {
/* 10 */     Vec3 vec3 = paramVec3.subtract(paramPathfinderMob.getX(), paramPathfinderMob.getY(), paramPathfinderMob.getZ());
/* 11 */     boolean bool = GoalUtils.mobRestricted(paramPathfinderMob, paramInt1);
/*    */     
/* 13 */     return RandomPos.generateRandomPos(paramPathfinderMob, () -> {
/*    */           BlockPos blockPos = AirAndWaterRandomPos.generateRandomPos(paramPathfinderMob, paramInt1, paramInt2, paramInt3, paramVec3.x, paramVec3.z, paramDouble, paramBoolean);
/* 15 */           return (blockPos == null || GoalUtils.isWater(paramPathfinderMob, blockPos)) ? null : blockPos;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\a\\util\AirRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */