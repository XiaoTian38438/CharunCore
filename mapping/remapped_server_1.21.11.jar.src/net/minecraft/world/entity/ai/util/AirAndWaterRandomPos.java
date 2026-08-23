/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class AirAndWaterRandomPos
/*    */ {
/*    */   public static Vec3 getPos(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 10 */     boolean bool = GoalUtils.mobRestricted(paramPathfinderMob, paramInt1);
/*    */     
/* 12 */     return RandomPos.generateRandomPos(paramPathfinderMob, () -> generateRandomPos(paramPathfinderMob, paramInt1, paramInt2, paramInt3, paramDouble1, paramDouble2, paramDouble3, paramBoolean));
/*    */   }
/*    */   
/*    */   public static BlockPos generateRandomPos(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean) {
/* 16 */     BlockPos blockPos1 = RandomPos.generateRandomDirectionWithinRadians(paramPathfinderMob.getRandom(), 0.0D, paramInt1, paramInt2, paramInt3, paramDouble1, paramDouble2, paramDouble3);
/* 17 */     if (blockPos1 == null) {
/* 18 */       return null;
/*    */     }
/*    */     
/* 21 */     BlockPos blockPos2 = RandomPos.generateRandomPosTowardDirection(paramPathfinderMob, paramInt1, paramPathfinderMob.getRandom(), blockPos1);
/* 22 */     if (GoalUtils.isOutsideLimits(blockPos2, paramPathfinderMob) || GoalUtils.isRestricted(paramBoolean, paramPathfinderMob, blockPos2)) {
/* 23 */       return null;
/*    */     }
/*    */     
/* 26 */     blockPos2 = RandomPos.moveUpOutOfSolid(blockPos2, paramPathfinderMob.level().getMaxY(), paramBlockPos -> GoalUtils.isSolid(paramPathfinderMob, paramBlockPos));
/* 27 */     if (GoalUtils.hasMalus(paramPathfinderMob, blockPos2)) {
/* 28 */       return null;
/*    */     }
/*    */     
/* 31 */     return blockPos2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\a\\util\AirAndWaterRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */