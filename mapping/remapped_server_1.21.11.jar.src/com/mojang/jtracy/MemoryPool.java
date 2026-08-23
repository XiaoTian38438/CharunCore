/*    */ package com.mojang.jtracy;
/*    */ 
/*    */ public class MemoryPool {
/*  4 */   static final MemoryPool UNAVAILABLE = new MemoryPool(0L);
/*    */   
/*    */   private final long id;
/*    */   
/*    */   MemoryPool(long paramLong) {
/*  9 */     this.id = paramLong;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void malloc(long paramLong, int paramInt) {
/* 16 */     if (this != UNAVAILABLE) {
/* 17 */       TracyBindings.mallocNamed(this.id, paramLong, paramInt);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void free(long paramLong) {
/* 25 */     if (this != UNAVAILABLE)
/* 26 */       TracyBindings.freeNamed(this.id, paramLong); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\jtracy\MemoryPool.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */