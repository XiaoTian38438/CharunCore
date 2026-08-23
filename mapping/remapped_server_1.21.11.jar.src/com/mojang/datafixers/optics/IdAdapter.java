/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ class IdAdapter<S, T>
/*    */   implements Adapter<S, T, S, T>
/*    */ {
/*  6 */   static final IdAdapter<?, ?> INSTANCE = new IdAdapter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public S from(S paramS) {
/* 13 */     return paramS;
/*    */   }
/*    */ 
/*    */   
/*    */   public T to(T paramT) {
/* 18 */     return paramT;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 23 */     return "id";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\IdAdapter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */