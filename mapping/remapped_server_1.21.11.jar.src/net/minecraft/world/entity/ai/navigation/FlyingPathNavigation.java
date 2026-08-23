/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
/*    */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.level.pathfinder.PathFinder;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FlyingPathNavigation extends PathNavigation {
/*    */   public FlyingPathNavigation(Mob paramMob, Level paramLevel) {
/* 15 */     super(paramMob, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   protected PathFinder createPathFinder(int paramInt) {
/* 20 */     this.nodeEvaluator = (NodeEvaluator)new FlyNodeEvaluator();
/* 21 */     return new PathFinder(this.nodeEvaluator, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canMoveDirectly(Vec3 paramVec31, Vec3 paramVec32) {
/* 26 */     return isClearForMovementBetween(this.mob, paramVec31, paramVec32, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canUpdatePath() {
/* 31 */     return ((canFloat() && this.mob.isInLiquid()) || !this.mob.isPassenger());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getTempMobPos() {
/* 36 */     return this.mob.position();
/*    */   }
/*    */ 
/*    */   
/*    */   public Path createPath(Entity paramEntity, int paramInt) {
/* 41 */     return createPath(paramEntity.blockPosition(), paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 46 */     this.tick++;
/*    */     
/* 48 */     if (this.hasDelayedRecomputation) {
/* 49 */       recomputePath();
/*    */     }
/*    */     
/* 52 */     if (isDone()) {
/*    */       return;
/*    */     }
/*    */     
/* 56 */     if (canUpdatePath()) {
/* 57 */       followThePath();
/* 58 */     } else if (this.path != null && !this.path.isDone()) {
/* 59 */       Vec3 vec31 = this.path.getNextEntityPos((Entity)this.mob);
/* 60 */       if (this.mob.getBlockX() == Mth.floor(vec31.x) && this.mob.getBlockY() == Mth.floor(vec31.y) && this.mob.getBlockZ() == Mth.floor(vec31.z)) {
/* 61 */         this.path.advance();
/*    */       }
/*    */     } 
/*    */     
/* 65 */     if (isDone()) {
/*    */       return;
/*    */     }
/* 68 */     Vec3 vec3 = this.path.getNextEntityPos((Entity)this.mob);
/*    */     
/* 70 */     this.mob.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, this.speedModifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStableDestination(BlockPos paramBlockPos) {
/* 75 */     return this.level.getBlockState(paramBlockPos).entityCanStandOn((BlockGetter)this.level, paramBlockPos, (Entity)this.mob);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canNavigateGround() {
/* 80 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\navigation\FlyingPathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */