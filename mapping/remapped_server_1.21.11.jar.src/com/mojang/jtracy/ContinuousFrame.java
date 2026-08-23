/*    */ package com.mojang.jtracy;
/*    */ 
/*    */ public class ContinuousFrame {
/*  4 */   static final ContinuousFrame UNAVAILABLE = new ContinuousFrame(0L);
/*    */   
/*    */   private final long id;
/*    */   
/*    */   ContinuousFrame(long paramLong) {
/*  9 */     this.id = paramLong;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void mark() {
/* 16 */     if (this != UNAVAILABLE)
/* 17 */       TracyBindings.markFrame(this.id); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\jtracy\ContinuousFrame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */