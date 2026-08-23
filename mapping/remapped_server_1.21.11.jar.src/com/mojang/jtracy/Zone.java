/*    */ package com.mojang.jtracy;
/*    */ 
/*    */ public class Zone implements AutoCloseable {
/*  4 */   static final Zone UNAVAILABLE = new Zone(0);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   Zone(int paramInt) {
/*  9 */     this.id = paramInt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Zone addText(String paramString) {
/* 20 */     if (this != UNAVAILABLE) {
/* 21 */       TracyBindings.addZoneText(this.id, paramString);
/*    */     }
/* 23 */     return this;
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
/*    */   public Zone setColor(int paramInt) {
/* 37 */     if (this != UNAVAILABLE) {
/* 38 */       TracyBindings.setZoneColor(this.id, paramInt);
/*    */     }
/* 40 */     return this;
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
/*    */   public Zone addValue(long paramLong) {
/* 53 */     if (this != UNAVAILABLE) {
/* 54 */       TracyBindings.addZoneValue(this.id, paramLong);
/*    */     }
/* 56 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 61 */     if (this != UNAVAILABLE)
/* 62 */       TracyBindings.endZone(this.id); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\jtracy\Zone.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */