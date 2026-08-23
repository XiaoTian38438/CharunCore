/*    */ package com.mojang.jtracy;
/*    */ 
/*    */ public class GpuContext {
/*  4 */   static final GpuContext UNAVAILABLE = new GpuContext(0);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   GpuContext(int paramInt) {
/*  9 */     this.id = paramInt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GpuContext setName(String paramString) {
/* 19 */     if (this != UNAVAILABLE) {
/* 20 */       TracyBindings.setGpuContextName(this.id, paramString);
/*    */     }
/* 22 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void beginZone(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2) {
/* 42 */     if (this != UNAVAILABLE) {
/* 43 */       TracyBindings.beginGpuZone(this.id, paramInt1, paramString1, paramString2, paramString3, paramInt2);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void endZone(int paramInt) {
/* 60 */     if (this != UNAVAILABLE) {
/* 61 */       TracyBindings.endGpuZone(this.id, paramInt);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void submitQueryTimestamp(int paramInt, long paramLong) {
/* 72 */     if (this != UNAVAILABLE)
/* 73 */       TracyBindings.submitQueryTimestamp(this.id, paramInt, paramLong); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\jtracy\GpuContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */