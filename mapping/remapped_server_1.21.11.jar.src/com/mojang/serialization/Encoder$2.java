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
/* 39 */     return ((DataResult)function.apply(paramB)).flatMap(paramObject2 -> Encoder.this.encode(paramObject2, paramDynamicOps, paramObject1));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return Encoder.this.toString() + "[flatComapped]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Encoder$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */