/*    */ package com.mojang.serialization;
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
/*    */ class null
/*    */   implements Encoder<A>
/*    */ {
/*    */   public <T> DataResult<T> encode(A paramA, DynamicOps<T> paramDynamicOps, T paramT) {
/* 53 */     return Encoder.this.<T>encode(paramA, paramDynamicOps, paramT).setLifecycle(lifecycle);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return Encoder.this.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Encoder$3.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */