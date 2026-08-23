/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
/*    */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*    */ import net.minecraft.world.level.pathfinder.PathFinder;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class AmphibiousPathNavigation extends PathNavigation {
/*    */   public AmphibiousPathNavigation(Mob paramMob, Level paramLevel) {
/* 12 */     super(paramMob, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   protected PathFinder createPathFinder(int paramInt) {
/* 17 */     this.nodeEvaluator = (NodeEvaluator)new AmphibiousNodeEvaluator(false);
/* 18 */     return new PathFinder(this.nodeEvaluator, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canUpdatePath() {
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getTempMobPos() {
/* 28 */     return new Vec3(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   protected double getGroundY(Vec3 paramVec3) {
/* 33 */     return paramVec3.y;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canMoveDirectly(Vec3 paramVec31, Vec3 paramVec32) {
/* 38 */     if (this.mob.isInLiquid()) {
/* 39 */       return isClearForMovementBetween(this.mob, paramVec31, paramVec32, false);
/*    */     }
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStableDestination(BlockPos paramBlockPos) {
/* 46 */     return !this.level.getBlockState(paramBlockPos.below()).isAir();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCanFloat(boolean paramBoolean) {}
/*    */ 
/*    */   
/*    */   public boolean canNavigateGround() {
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\navigation\AmphibiousPathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */