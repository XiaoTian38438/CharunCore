/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EitherMapCodec<F, S>
/*    */   extends MapCodec<Either<F, S>>
/*    */ {
/*    */   private final MapCodec<F> first;
/*    */   private final MapCodec<S> second;
/*    */   
/*    */   public EitherMapCodec(MapCodec<F> paramMapCodec, MapCodec<S> paramMapCodec1) {
/* 21 */     this.first = paramMapCodec;
/* 22 */     this.second = paramMapCodec1;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Either<F, S>> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 27 */     DataResult<Either<F, S>> dataResult1 = this.first.decode(paramDynamicOps, paramMapLike).map(Either::left);
/* 28 */     if (dataResult1.isSuccess()) {
/* 29 */       return dataResult1;
/*    */     }
/* 31 */     DataResult<Either<F, S>> dataResult2 = this.second.decode(paramDynamicOps, paramMapLike).map(Either::right);
/* 32 */     if (dataResult2.isSuccess()) {
/* 33 */       return dataResult2;
/*    */     }
/* 35 */     return dataResult1.apply2((paramEither1, paramEither2) -> paramEither2, dataResult2);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(Either<F, S> paramEither, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 40 */     return (RecordBuilder<T>)paramEither.map(paramObject -> this.first.encode(paramObject, paramDynamicOps, paramRecordBuilder), paramObject -> this.second.encode(paramObject, paramDynamicOps, paramRecordBuilder));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 48 */     if (this == paramObject) {
/* 49 */       return true;
/*    */     }
/* 51 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 52 */       return false;
/*    */     }
/* 54 */     EitherMapCodec eitherMapCodec = (EitherMapCodec)paramObject;
/* 55 */     return (Objects.equals(this.first, eitherMapCodec.first) && Objects.equals(this.second, eitherMapCodec.second));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 60 */     return Objects.hash(new Object[] { this.first, this.second });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return "EitherMapCodec[" + String.valueOf(this.first) + ", " + String.valueOf(this.second) + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 70 */     return Stream.concat(this.first.keys(paramDynamicOps), this.second.keys(paramDynamicOps));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\EitherMapCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */