/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PointCharge
/*    */ {
/*    */   private final BlockPos pos;
/*    */   private final double charge;
/*    */   
/*    */   public PointCharge(BlockPos paramBlockPos, double paramDouble) {
/* 15 */     this.pos = paramBlockPos;
/* 16 */     this.charge = paramDouble;
/*    */   }
/*    */   
/*    */   public double getPotentialChange(BlockPos paramBlockPos) {
/* 20 */     double d = this.pos.distSqr((Vec3i)paramBlockPos);
/* 21 */     if (d == 0.0D)
/*    */     {
/* 23 */       return Double.POSITIVE_INFINITY;
/*    */     }
/* 25 */     return this.charge / Math.sqrt(d);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\PotentialCalculator$PointCharge.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */