/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WallClimberNavigation
/*    */   extends GroundPathNavigation
/*    */ {
/*    */   private BlockPos pathToPosition;
/*    */   
/*    */   public WallClimberNavigation(Mob paramMob, Level paramLevel) {
/* 22 */     super(paramMob, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   public Path createPath(BlockPos paramBlockPos, int paramInt) {
/* 27 */     this.pathToPosition = paramBlockPos;
/* 28 */     return super.createPath(paramBlockPos, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public Path createPath(Entity paramEntity, int paramInt) {
/* 33 */     this.pathToPosition = paramEntity.blockPosition();
/* 34 */     return super.createPath(paramEntity, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean moveTo(Entity paramEntity, double paramDouble) {
/* 39 */     Path path = createPath(paramEntity, 0);
/* 40 */     if (path != null) {
/* 41 */       return moveTo(path, paramDouble);
/*    */     }
/* 43 */     this.pathToPosition = paramEntity.blockPosition();
/* 44 */     this.speedModifier = paramDouble;
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 51 */     if (isDone()) {
/* 52 */       if (this.pathToPosition != null)
/*    */       {
/* 54 */         if (this.pathToPosition.closerToCenterThan((Position)this.mob.position(), this.mob.getBbWidth()) || (this.mob.getY() > this.pathToPosition.getY() && BlockPos.containing(this.pathToPosition.getX(), this.mob.getY(), this.pathToPosition.getZ()).closerToCenterThan((Position)this.mob.position(), this.mob.getBbWidth()))) {
/* 55 */           this.pathToPosition = null;
/*    */         } else {
/* 57 */           this.mob.getMoveControl().setWantedPosition(this.pathToPosition.getX(), this.pathToPosition.getY(), this.pathToPosition.getZ(), this.speedModifier);
/*    */         } 
/*    */       }
/*    */       return;
/*    */     } 
/* 62 */     super.tick();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\navigation\WallClimberNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */