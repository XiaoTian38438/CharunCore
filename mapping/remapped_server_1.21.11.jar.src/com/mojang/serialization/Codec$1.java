/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
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
/*    */   implements Codec<A>
/*    */ {
/*    */   public <T> DataResult<T> encode(A paramA, DynamicOps<T> paramDynamicOps, T paramT) {
/* 43 */     return Codec.this.encode(paramA, paramDynamicOps, paramT).setLifecycle(lifecycle);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 48 */     return Codec.this.decode(paramDynamicOps, paramT).setLifecycle(lifecycle);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return Codec.this.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Codec$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */