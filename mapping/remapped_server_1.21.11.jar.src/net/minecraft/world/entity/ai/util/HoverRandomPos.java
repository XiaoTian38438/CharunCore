/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class HoverRandomPos
/*    */ {
/*    */   public static Vec3 getPos(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2, double paramDouble1, double paramDouble2, float paramFloat, int paramInt3, int paramInt4) {
/* 10 */     boolean bool = GoalUtils.mobRestricted(paramPathfinderMob, paramInt1);
/*    */     
/* 12 */     return RandomPos.generateRandomPos(paramPathfinderMob, () -> {
/*    */           BlockPos blockPos1 = RandomPos.generateRandomDirectionWithinRadians(paramPathfinderMob.getRandom(), 0.0D, paramInt1, paramInt2, 0, paramDouble1, paramDouble2, paramFloat);
/*    */           
/*    */           if (blockPos1 == null) {
/*    */             return null;
/*    */           }
/*    */           
/*    */           BlockPos blockPos2 = LandRandomPos.generateRandomPosTowardDirection(paramPathfinderMob, paramInt1, paramBoolean, blockPos1);
/*    */           if (blockPos2 == null) {
/*    */             return null;
/*    */           }
/*    */           blockPos2 = RandomPos.moveUpToAboveSolid(blockPos2, paramPathfinderMob.getRandom().nextInt(paramInt3 - paramInt4 + 1) + paramInt4, paramPathfinderMob.level().getMaxY(), ());
/* 24 */           return (GoalUtils.isWater(paramPathfinderMob, blockPos2) || GoalUtils.hasMalus(paramPathfinderMob, blockPos2)) ? null : blockPos2;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\a\\util\HoverRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */