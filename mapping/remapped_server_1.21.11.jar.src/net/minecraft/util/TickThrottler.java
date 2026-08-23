/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class TickThrottler {
/*    */   private final int incrementStep;
/*    */   private final int threshold;
/*    */   private int count;
/*    */   
/*    */   public TickThrottler(int paramInt1, int paramInt2) {
/*  9 */     this.incrementStep = paramInt1;
/* 10 */     this.threshold = paramInt2;
/*    */   }
/*    */   
/*    */   public void increment() {
/* 14 */     this.count += this.incrementStep;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     if (this.count > 0) {
/* 19 */       this.count--;
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isUnderThreshold() {
/* 24 */     return (this.count < this.threshold);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\TickThrottler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */