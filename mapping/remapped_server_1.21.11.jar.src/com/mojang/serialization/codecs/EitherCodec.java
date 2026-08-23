/*    */ package com.mojang.serialization.codecs;
/*    */ public final class EitherCodec<F, S> extends Record implements Codec<Either<F, S>> { private final Codec<F> first; private final Codec<S> second;
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/serialization/codecs/EitherCodec;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/codecs/EitherCodec;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */   }
/*    */   
/* 11 */   public EitherCodec(Codec<F> paramCodec, Codec<S> paramCodec1) { this.first = paramCodec; this.second = paramCodec1; } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/serialization/codecs/EitherCodec;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 11 */     //   #11	-> 0 } public Codec<F> first() { return this.first; } public Codec<S> second() { return this.second; }
/*    */   
/*    */   public <T> DataResult<Pair<Either<F, S>, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 14 */     DataResult<Pair<Either<F, S>, T>> dataResult1 = this.first.decode(paramDynamicOps, paramT).map(paramPair -> paramPair.mapFirst(Either::left));
/* 15 */     if (dataResult1.isSuccess()) {
/* 16 */       return dataResult1;
/*    */     }
/* 18 */     DataResult<Pair<Either<F, S>, T>> dataResult2 = this.second.decode(paramDynamicOps, paramT).map(paramPair -> paramPair.mapFirst(Either::right));
/* 19 */     if (dataResult2.isSuccess()) {
/* 20 */       return dataResult2;
/*    */     }
/* 22 */     if (dataResult1.hasResultOrPartial()) {
/* 23 */       return dataResult1;
/*    */     }
/* 25 */     if (dataResult2.hasResultOrPartial()) {
/* 26 */       return dataResult2;
/*    */     }
/* 28 */     return DataResult.error(() -> "Failed to parse either. First: " + ((DataResult.Error)paramDataResult1.error().orElseThrow()).message() + "; Second: " + ((DataResult.Error)paramDataResult2.error().orElseThrow()).message());
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Either<F, S> paramEither, DynamicOps<T> paramDynamicOps, T paramT) {
/* 33 */     return (DataResult<T>)paramEither.map(paramObject2 -> this.first.encode(paramObject2, paramDynamicOps, paramObject1), paramObject2 -> this.second.encode(paramObject2, paramDynamicOps, paramObject1));
/*    */   } }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\EitherCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */