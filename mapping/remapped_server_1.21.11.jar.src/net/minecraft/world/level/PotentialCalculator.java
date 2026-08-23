/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ 
/*    */ public class PotentialCalculator
/*    */ {
/*    */   private static class PointCharge {
/*    */     private final BlockPos pos;
/*    */     private final double charge;
/*    */     
/*    */     public PointCharge(BlockPos param1BlockPos, double param1Double) {
/* 15 */       this.pos = param1BlockPos;
/* 16 */       this.charge = param1Double;
/*    */     }
/*    */     
/*    */     public double getPotentialChange(BlockPos param1BlockPos) {
/* 20 */       double d = this.pos.distSqr((Vec3i)param1BlockPos);
/* 21 */       if (d == 0.0D)
/*    */       {
/* 23 */         return Double.POSITIVE_INFINITY;
/*    */       }
/* 25 */       return this.charge / Math.sqrt(d);
/*    */     }
/*    */   }
/*    */   
/* 29 */   private final List<PointCharge> charges = Lists.newArrayList();
/*    */   
/*    */   public void addCharge(BlockPos paramBlockPos, double paramDouble) {
/* 32 */     if (paramDouble != 0.0D) {
/* 33 */       this.charges.add(new PointCharge(paramBlockPos, paramDouble));
/*    */     }
/*    */   }
/*    */   
/*    */   public double getPotentialEnergyChange(BlockPos paramBlockPos, double paramDouble) {
/* 38 */     if (paramDouble == 0.0D) {
/* 39 */       return 0.0D;
/*    */     }
/* 41 */     double d = 0.0D;
/* 42 */     for (PointCharge pointCharge : this.charges) {
/* 43 */       d += pointCharge.getPotentialChange(paramBlockPos);
/*    */     }
/* 45 */     return d * paramDouble;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\PotentialCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */