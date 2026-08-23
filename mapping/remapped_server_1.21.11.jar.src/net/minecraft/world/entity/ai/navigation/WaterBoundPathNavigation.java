/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*    */ import net.minecraft.world.level.pathfinder.PathFinder;
/*    */ import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WaterBoundPathNavigation
/*    */   extends PathNavigation {
/*    */   public WaterBoundPathNavigation(Mob paramMob, Level paramLevel) {
/* 15 */     super(paramMob, paramLevel);
/*    */   }
/*    */   private boolean allowBreaching;
/*    */   
/*    */   protected PathFinder createPathFinder(int paramInt) {
/* 20 */     this.allowBreaching = (this.mob.getType() == EntityType.DOLPHIN);
/* 21 */     this.nodeEvaluator = (NodeEvaluator)new SwimNodeEvaluator(this.allowBreaching);
/*    */     
/* 23 */     this.nodeEvaluator.setCanPassDoors(false);
/* 24 */     return new PathFinder(this.nodeEvaluator, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canUpdatePath() {
/* 29 */     return (this.allowBreaching || this.mob.isInLiquid());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getTempMobPos() {
/* 34 */     return new Vec3(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   protected double getGroundY(Vec3 paramVec3) {
/* 39 */     return paramVec3.y;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canMoveDirectly(Vec3 paramVec31, Vec3 paramVec32) {
/* 44 */     return isClearForMovementBetween(this.mob, paramVec31, paramVec32, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStableDestination(BlockPos paramBlockPos) {
/* 49 */     return !this.level.getBlockState(paramBlockPos).isSolidRender();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCanFloat(boolean paramBoolean) {}
/*    */ 
/*    */   
/*    */   public boolean canNavigateGround() {
/* 58 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\navigation\WaterBoundPathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */