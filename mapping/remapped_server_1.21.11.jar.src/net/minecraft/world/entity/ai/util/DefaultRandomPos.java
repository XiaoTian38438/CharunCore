/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultRandomPos
/*    */ {
/*    */   public static Vec3 getPos(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2) {
/* 12 */     boolean bool = GoalUtils.mobRestricted(paramPathfinderMob, paramInt1);
/*    */     
/* 14 */     return RandomPos.generateRandomPos(paramPathfinderMob, () -> {
/*    */           BlockPos blockPos = RandomPos.generateRandomDirection(paramPathfinderMob.getRandom(), paramInt1, paramInt2);
/*    */           return generateRandomPosTowardDirection(paramPathfinderMob, paramInt1, paramBoolean, blockPos);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public static Vec3 getPosTowards(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2, Vec3 paramVec3, double paramDouble) {
/* 22 */     Vec3 vec3 = paramVec3.subtract(paramPathfinderMob.getX(), paramPathfinderMob.getY(), paramPathfinderMob.getZ());
/* 23 */     boolean bool = GoalUtils.mobRestricted(paramPathfinderMob, paramInt1);
/*    */     
/* 25 */     return RandomPos.generateRandomPos(paramPathfinderMob, () -> {
/*    */           BlockPos blockPos = RandomPos.generateRandomDirectionWithinRadians(paramPathfinderMob.getRandom(), 0.0D, paramInt1, paramInt2, 0, paramVec3.x, paramVec3.z, paramDouble);
/*    */           return (blockPos == null) ? null : generateRandomPosTowardDirection(paramPathfinderMob, paramInt1, paramBoolean, blockPos);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Vec3 getPosAway(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2, Vec3 paramVec3) {
/* 37 */     Vec3 vec3 = paramPathfinderMob.position().subtract(paramVec3);
/* 38 */     boolean bool = GoalUtils.mobRestricted(paramPathfinderMob, paramInt1);
/*    */     
/* 40 */     return RandomPos.generateRandomPos(paramPathfinderMob, () -> {
/*    */           BlockPos blockPos = RandomPos.generateRandomDirectionWithinRadians(paramPathfinderMob.getRandom(), 0.0D, paramInt1, paramInt2, 0, paramVec3.x, paramVec3.z, 1.5707963705062866D);
/*    */           return (blockPos == null) ? null : generateRandomPosTowardDirection(paramPathfinderMob, paramInt1, paramBoolean, blockPos);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static BlockPos generateRandomPosTowardDirection(PathfinderMob paramPathfinderMob, int paramInt, boolean paramBoolean, BlockPos paramBlockPos) {
/* 51 */     BlockPos blockPos = RandomPos.generateRandomPosTowardDirection(paramPathfinderMob, paramInt, paramPathfinderMob.getRandom(), paramBlockPos);
/* 52 */     if (GoalUtils.isOutsideLimits(blockPos, paramPathfinderMob) || GoalUtils.isRestricted(paramBoolean, paramPathfinderMob, blockPos) || GoalUtils.isNotStable(paramPathfinderMob.getNavigation(), blockPos) || GoalUtils.hasMalus(paramPathfinderMob, blockPos)) {
/* 53 */       return null;
/*    */     }
/*    */     
/* 56 */     return blockPos;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\a\\util\DefaultRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */