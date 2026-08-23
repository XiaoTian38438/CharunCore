/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class SmoothDouble {
/*    */   private double targetValue;
/*    */   private double remainingValue;
/*    */   private double lastAmount;
/*    */   
/*    */   public double getNewDeltaValue(double paramDouble1, double paramDouble2) {
/*  9 */     this.targetValue += paramDouble1;
/*    */     
/* 11 */     double d1 = this.targetValue - this.remainingValue;
/*    */     
/* 13 */     double d2 = Mth.lerp(0.5D, this.lastAmount, d1);
/*    */ 
/*    */     
/* 16 */     double d3 = Math.signum(d1);
/* 17 */     if (d3 * d1 > d3 * this.lastAmount) {
/* 18 */       d1 = d2;
/*    */     }
/*    */     
/* 21 */     this.lastAmount = d2;
/* 22 */     this.remainingValue += d1 * paramDouble2;
/*    */     
/* 24 */     return d1 * paramDouble2;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 28 */     this.targetValue = 0.0D;
/* 29 */     this.remainingValue = 0.0D;
/* 30 */     this.lastAmount = 0.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\SmoothDouble.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */