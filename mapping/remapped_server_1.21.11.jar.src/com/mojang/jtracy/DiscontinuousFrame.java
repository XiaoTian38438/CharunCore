/*    */ package com.mojang.jtracy;
/*    */ 
/*    */ public class DiscontinuousFrame {
/*  4 */   static final DiscontinuousFrame UNAVAILABLE = new DiscontinuousFrame(0L);
/*    */   
/*    */   private final long id;
/*    */   
/*    */   DiscontinuousFrame(long paramLong) {
/*  9 */     this.id = paramLong;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() {
/* 17 */     if (this != UNAVAILABLE) {
/* 18 */       TracyBindings.markFrameStart(this.id);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void end() {
/* 27 */     if (this != UNAVAILABLE)
/* 28 */       TracyBindings.markFrameEnd(this.id); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\jtracy\DiscontinuousFrame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */