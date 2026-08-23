/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ public final class PairMapCodec<F, S>
/*    */   extends MapCodec<Pair<F, S>>
/*    */ {
/*    */   private final MapCodec<F> first;
/*    */   private final MapCodec<S> second;
/*    */   
/*    */   public PairMapCodec(MapCodec<F> paramMapCodec, MapCodec<S> paramMapCodec1) {
/* 20 */     this.first = paramMapCodec;
/* 21 */     this.second = paramMapCodec1;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<F, S>> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 26 */     return this.first.decode(paramDynamicOps, paramMapLike).flatMap(paramObject -> this.second.decode(paramDynamicOps, paramMapLike).map(()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(Pair<F, S> paramPair, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 35 */     return this.first.encode(paramPair.getFirst(), paramDynamicOps, this.second.encode(paramPair.getSecond(), paramDynamicOps, paramRecordBuilder));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 40 */     if (this == paramObject) {
/* 41 */       return true;
/*    */     }
/* 43 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 44 */       return false;
/*    */     }
/* 46 */     PairMapCodec pairMapCodec = (PairMapCodec)paramObject;
/* 47 */     return (Objects.equals(this.first, pairMapCodec.first) && Objects.equals(this.second, pairMapCodec.second));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 52 */     return Objects.hash(new Object[] { this.first, this.second });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "PairMapCodec[" + String.valueOf(this.first) + ", " + String.valueOf(this.second) + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 62 */     return Stream.concat(this.first.keys(paramDynamicOps), this.second.keys(paramDynamicOps));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\PairMapCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */