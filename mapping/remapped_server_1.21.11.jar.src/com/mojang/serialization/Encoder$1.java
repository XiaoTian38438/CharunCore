/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import java.util.function.Function;
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
/*    */   implements Encoder<B>
/*    */ {
/*    */   public <T> DataResult<T> encode(B paramB, DynamicOps<T> paramDynamicOps, T paramT) {
/* 25 */     return Encoder.this.encode(function.apply(paramB), paramDynamicOps, paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 30 */     return Encoder.this.toString() + "[comapped]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Encoder$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */