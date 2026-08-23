/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
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
/*    */   implements Decoder<B>
/*    */ {
/*    */   public <T> DataResult<Pair<B, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 49 */     return Decoder.this.decode(paramDynamicOps, paramT).flatMap(paramPair -> ((DataResult)paramFunction.apply(paramPair.getFirst())).map(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return Decoder.this.toString() + "[flatMapped]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Decoder$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */