/*    */ package com.mojang.jtracy;
/*    */ 
/*    */ public class Plot {
/*  4 */   static final Plot UNAVAILABLE = new Plot(0L);
/*    */   
/*    */   private final long handle;
/*    */   
/*    */   Plot(long paramLong) {
/*  9 */     this.handle = paramLong;
/*    */   }
/*    */   
/*    */   public void setValue(double paramDouble) {
/* 13 */     if (this != UNAVAILABLE)
/* 14 */       TracyBindings.plotValue(this.handle, paramDouble); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\jtracy\Plot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */