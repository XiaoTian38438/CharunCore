/*    */ package net.minecraft.world.entity.ai.util;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GoalUtils {
/*    */   public static boolean hasGroundPathNavigation(Mob paramMob) {
/* 13 */     return paramMob.getNavigation().canNavigateGround();
/*    */   }
/*    */   
/*    */   public static boolean mobRestricted(PathfinderMob paramPathfinderMob, double paramDouble) {
/* 17 */     return (paramPathfinderMob.hasHome() && paramPathfinderMob.getHomePosition().closerToCenterThan((Position)paramPathfinderMob.position(), paramPathfinderMob.getHomeRadius() + paramDouble + 1.0D));
/*    */   }
/*    */   
/*    */   public static boolean isOutsideLimits(BlockPos paramBlockPos, PathfinderMob paramPathfinderMob) {
/* 21 */     return paramPathfinderMob.level().isOutsideBuildHeight(paramBlockPos.getY());
/*    */   }
/*    */   
/*    */   public static boolean isRestricted(boolean paramBoolean, PathfinderMob paramPathfinderMob, BlockPos paramBlockPos) {
/* 25 */     return (paramBoolean && !paramPathfinderMob.isWithinHome(paramBlockPos));
/*    */   }
/*    */   
/*    */   public static boolean isRestricted(boolean paramBoolean, PathfinderMob paramPathfinderMob, Vec3 paramVec3) {
/* 29 */     return (paramBoolean && !paramPathfinderMob.isWithinHome(paramVec3));
/*    */   }
/*    */   
/*    */   public static boolean isNotStable(PathNavigation paramPathNavigation, BlockPos paramBlockPos) {
/* 33 */     return !paramPathNavigation.isStableDestination(paramBlockPos);
/*    */   }
/*    */   
/*    */   public static boolean isWater(PathfinderMob paramPathfinderMob, BlockPos paramBlockPos) {
/* 37 */     return paramPathfinderMob.level().getFluidState(paramBlockPos).is(FluidTags.WATER);
/*    */   }
/*    */   
/*    */   public static boolean hasMalus(PathfinderMob paramPathfinderMob, BlockPos paramBlockPos) {
/* 41 */     return (paramPathfinderMob.getPathfindingMalus(WalkNodeEvaluator.getPathTypeStatic((Mob)paramPathfinderMob, paramBlockPos)) != 0.0F);
/*    */   }
/*    */   
/*    */   public static boolean isSolid(PathfinderMob paramPathfinderMob, BlockPos paramBlockPos) {
/* 45 */     return paramPathfinderMob.level().getBlockState(paramBlockPos).isSolid();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\a\\util\GoalUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */