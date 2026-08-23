/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
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
/*    */   extends MapDecoder.Implementation<B>
/*    */ {
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 75 */     return MapDecoder.this.keys(paramDynamicOps);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<B> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 80 */     return MapDecoder.this.decode(paramDynamicOps, paramMapLike).flatMap(paramObject -> ((DataResult)paramFunction.apply(paramObject)).map(Function.identity()));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 85 */     return MapDecoder.this.toString() + "[flatMapped]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapDecoder$3.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */