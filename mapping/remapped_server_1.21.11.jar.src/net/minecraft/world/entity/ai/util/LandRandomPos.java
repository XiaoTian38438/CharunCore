/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.ToDoubleFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class LandRandomPos
/*    */ {
/*    */   public static Vec3 getPos(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2) {
/* 13 */     Objects.requireNonNull(paramPathfinderMob); return getPos(paramPathfinderMob, paramInt1, paramInt2, paramPathfinderMob::getWalkTargetValue);
/*    */   }
/*    */   
/*    */   public static Vec3 getPos(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2, ToDoubleFunction<BlockPos> paramToDoubleFunction) {
/* 17 */     boolean bool = GoalUtils.mobRestricted(paramPathfinderMob, paramInt1);
/*    */     
/* 19 */     return RandomPos.generateRandomPos(() -> { BlockPos blockPos1 = RandomPos.generateRandomDirection(paramPathfinderMob.getRandom(), paramInt1, paramInt2); BlockPos blockPos2 = generateRandomPosTowardDirection(paramPathfinderMob, paramInt1, paramBoolean, blockPos1); return (blockPos2 == null) ? null : movePosUpOutOfSolid(paramPathfinderMob, blockPos2); }paramToDoubleFunction);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Vec3 getPosTowards(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2, Vec3 paramVec3) {
/* 32 */     Vec3 vec3 = paramVec3.subtract(paramPathfinderMob.getX(), paramPathfinderMob.getY(), paramPathfinderMob.getZ());
/* 33 */     boolean bool = GoalUtils.mobRestricted(paramPathfinderMob, paramInt1);
/*    */     
/* 35 */     return getPosInDirection(paramPathfinderMob, 0.0D, paramInt1, paramInt2, vec3, bool);
/*    */   }
/*    */   
/*    */   public static Vec3 getPosAway(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2, Vec3 paramVec3) {
/* 39 */     return getPosAway(paramPathfinderMob, 0.0D, paramInt1, paramInt2, paramVec3);
/*    */   }
/*    */   public static Vec3 getPosAway(PathfinderMob paramPathfinderMob, double paramDouble1, double paramDouble2, int paramInt, Vec3 paramVec3) {
/* 42 */     Vec3 vec3 = paramPathfinderMob.position().subtract(paramVec3);
/* 43 */     if (vec3.length() == 0.0D) {
/* 44 */       vec3 = new Vec3(paramPathfinderMob.getRandom().nextDouble() - 0.5D, 0.0D, paramPathfinderMob.getRandom().nextDouble() - 0.5D);
/*    */     }
/* 46 */     boolean bool = GoalUtils.mobRestricted(paramPathfinderMob, paramDouble2);
/*    */     
/* 48 */     return getPosInDirection(paramPathfinderMob, paramDouble1, paramDouble2, paramInt, vec3, bool);
/*    */   }
/*    */   
/*    */   private static Vec3 getPosInDirection(PathfinderMob paramPathfinderMob, double paramDouble1, double paramDouble2, int paramInt, Vec3 paramVec3, boolean paramBoolean) {
/* 52 */     return RandomPos.generateRandomPos(paramPathfinderMob, () -> {
/*    */           BlockPos blockPos1 = RandomPos.generateRandomDirectionWithinRadians(paramPathfinderMob.getRandom(), paramDouble1, paramDouble2, paramInt, 0, paramVec3.x, paramVec3.z, 1.5707963705062866D);
/*    */           if (blockPos1 == null) {
/*    */             return null;
/*    */           }
/*    */           BlockPos blockPos2 = generateRandomPosTowardDirection(paramPathfinderMob, paramDouble2, paramBoolean, blockPos1);
/*    */           return (blockPos2 == null) ? null : movePosUpOutOfSolid(paramPathfinderMob, blockPos2);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BlockPos movePosUpOutOfSolid(PathfinderMob paramPathfinderMob, BlockPos paramBlockPos) {
/* 68 */     paramBlockPos = RandomPos.moveUpOutOfSolid(paramBlockPos, paramPathfinderMob.level().getMaxY(), paramBlockPos -> GoalUtils.isSolid(paramPathfinderMob, paramBlockPos));
/* 69 */     if (GoalUtils.isWater(paramPathfinderMob, paramBlockPos) || GoalUtils.hasMalus(paramPathfinderMob, paramBlockPos)) {
/* 70 */       return null;
/*    */     }
/* 72 */     return paramBlockPos;
/*    */   }
/*    */   
/*    */   public static BlockPos generateRandomPosTowardDirection(PathfinderMob paramPathfinderMob, double paramDouble, boolean paramBoolean, BlockPos paramBlockPos) {
/* 76 */     BlockPos blockPos = RandomPos.generateRandomPosTowardDirection(paramPathfinderMob, paramDouble, paramPathfinderMob.getRandom(), paramBlockPos);
/* 77 */     if (GoalUtils.isOutsideLimits(blockPos, paramPathfinderMob) || GoalUtils.isRestricted(paramBoolean, paramPathfinderMob, blockPos) || GoalUtils.isNotStable(paramPathfinderMob.getNavigation(), blockPos)) {
/* 78 */       return null;
/*    */     }
/*    */     
/* 81 */     return blockPos;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\a\\util\LandRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */