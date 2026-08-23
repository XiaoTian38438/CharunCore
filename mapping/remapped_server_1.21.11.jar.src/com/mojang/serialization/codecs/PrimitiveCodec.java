/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PrimitiveCodec<A>
/*    */   extends Codec<A>
/*    */ {
/*    */   default <T> DataResult<Pair<A, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 17 */     return read(paramDynamicOps, paramT).map(paramObject -> Pair.of(paramObject, paramDynamicOps.empty()));
/*    */   }
/*    */ 
/*    */   
/*    */   default <T> DataResult<T> encode(A paramA, DynamicOps<T> paramDynamicOps, T paramT) {
/* 22 */     return paramDynamicOps.mergeToPrimitive(paramT, write(paramDynamicOps, paramA));
/*    */   }
/*    */   
/*    */   <T> DataResult<A> read(DynamicOps<T> paramDynamicOps, T paramT);
/*    */   
/*    */   <T> T write(DynamicOps<T> paramDynamicOps, A paramA);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\PrimitiveCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */