/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ 
/*    */ public class TryFindWaterGoal extends Goal {
/*    */   private final PathfinderMob mob;
/*    */   
/*    */   public TryFindWaterGoal(PathfinderMob paramPathfinderMob) {
/* 12 */     this.mob = paramPathfinderMob;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 17 */     return (this.mob.onGround() && !this.mob.level().getFluidState(this.mob.blockPosition()).is(FluidTags.WATER));
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 22 */     BlockPos blockPos = null;
/*    */     
/* 24 */     Iterable iterable = BlockPos.betweenClosed(
/* 25 */         Mth.floor(this.mob.getX() - 2.0D), 
/* 26 */         Mth.floor(this.mob.getY() - 2.0D), 
/* 27 */         Mth.floor(this.mob.getZ() - 2.0D), 
/* 28 */         Mth.floor(this.mob.getX() + 2.0D), this.mob
/* 29 */         .getBlockY(), 
/* 30 */         Mth.floor(this.mob.getZ() + 2.0D));
/*    */ 
/*    */     
/* 33 */     for (BlockPos blockPos1 : iterable) {
/* 34 */       if (this.mob.level().getFluidState(blockPos1).is(FluidTags.WATER)) {
/* 35 */         blockPos = blockPos1;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 40 */     if (blockPos != null)
/* 41 */       this.mob.getMoveControl().setWantedPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\TryFindWaterGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */