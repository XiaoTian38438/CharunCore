/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ public final class PairCodec<F, S>
/*    */   implements Codec<Pair<F, S>>
/*    */ {
/*    */   private final Codec<F> first;
/*    */   private final Codec<S> second;
/*    */   
/*    */   public PairCodec(Codec<F> paramCodec, Codec<S> paramCodec1) {
/* 17 */     this.first = paramCodec;
/* 18 */     this.second = paramCodec1;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<Pair<F, S>, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 23 */     return this.first.decode(paramDynamicOps, paramT).flatMap(paramPair -> this.second.decode(paramDynamicOps, paramPair.getSecond()).map(()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Pair<F, S> paramPair, DynamicOps<T> paramDynamicOps, T paramT) {
/* 32 */     return this.second.encode(paramPair.getSecond(), paramDynamicOps, paramT)
/* 33 */       .flatMap(paramObject -> this.first.encode(paramPair.getFirst(), paramDynamicOps, paramObject));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 38 */     if (this == paramObject) {
/* 39 */       return true;
/*    */     }
/* 41 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 42 */       return false;
/*    */     }
/* 44 */     PairCodec pairCodec = (PairCodec)paramObject;
/* 45 */     return (Objects.equals(this.first, pairCodec.first) && Objects.equals(this.second, pairCodec.second));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 50 */     return Objects.hash(new Object[] { this.first, this.second });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "PairCodec[" + String.valueOf(this.first) + ", " + String.valueOf(this.second) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\PairCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */