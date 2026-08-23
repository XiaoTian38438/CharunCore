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
/* 63 */     return Decoder.this.decode(paramDynamicOps, paramT).map(paramPair -> paramPair.mapFirst(paramFunction));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return Decoder.this.toString() + "[mapped]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Decoder$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */