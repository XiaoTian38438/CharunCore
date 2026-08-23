/*    */ package com.mojang.serialization.codecs;
/*    */ public final class XorCodec<F, S> extends Record implements Codec<Either<F, S>> { private final Codec<F> first;
/*    */   private final Codec<S> second;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/serialization/codecs/XorCodec;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/codecs/XorCodec;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */   }
/*    */   
/* 13 */   public XorCodec(Codec<F> paramCodec, Codec<S> paramCodec1) { this.first = paramCodec; this.second = paramCodec1; } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/serialization/codecs/XorCodec;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 13 */     //   #13	-> 0 } public Codec<F> first() { return this.first; } public Codec<S> second() { return this.second; }
/*    */   
/*    */   public <T> DataResult<Pair<Either<F, S>, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 16 */     DataResult<Pair<Either<F, S>, T>> dataResult1 = this.first.decode(paramDynamicOps, paramT).map(paramPair -> paramPair.mapFirst(Either::left));
/* 17 */     DataResult<Pair<Either<F, S>, T>> dataResult2 = this.second.decode(paramDynamicOps, paramT).map(paramPair -> paramPair.mapFirst(Either::right));
/* 18 */     Optional<Pair> optional = dataResult1.result();
/* 19 */     Optional optional1 = dataResult2.result();
/* 20 */     if (optional.isPresent() && optional1.isPresent()) {
/* 21 */       return DataResult.error(() -> "Both alternatives read successfully, can not pick the correct one; first: " + String.valueOf(paramOptional1.get()) + " second: " + String.valueOf(paramOptional2.get()), optional.get());
/*    */     }
/* 23 */     if (optional.isPresent()) {
/* 24 */       return dataResult1;
/*    */     }
/* 26 */     if (optional1.isPresent()) {
/* 27 */       return dataResult2;
/*    */     }
/* 29 */     return dataResult1.apply2((paramPair1, paramPair2) -> paramPair2, dataResult2);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Either<F, S> paramEither, DynamicOps<T> paramDynamicOps, T paramT) {
/* 34 */     return (DataResult<T>)paramEither.map(paramObject2 -> this.first.encode(paramObject2, paramDynamicOps, paramObject1), paramObject2 -> this.second.encode(paramObject2, paramDynamicOps, paramObject1));
/*    */   } }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\XorCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */