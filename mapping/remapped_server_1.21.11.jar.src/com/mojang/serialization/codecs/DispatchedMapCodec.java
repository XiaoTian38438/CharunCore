/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ 
/*    */ public final class DispatchedMapCodec<K, V> extends Record implements Codec<Map<K, V>> {
/*    */   private final Codec<K> keyCodec;
/*    */   private final Function<K, Codec<? extends V>> valueCodecFunction;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/serialization/codecs/DispatchedMapCodec;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/codecs/DispatchedMapCodec;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */   }
/*    */   
/* 20 */   public DispatchedMapCodec(Codec<K> paramCodec, Function<K, Codec<? extends V>> paramFunction) { this.keyCodec = paramCodec; this.valueCodecFunction = paramFunction; } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/serialization/codecs/DispatchedMapCodec;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 20 */     //   #20	-> 0 } public Codec<K> keyCodec() { return this.keyCodec; } public Function<K, Codec<? extends V>> valueCodecFunction() { return this.valueCodecFunction; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Map<K, V> paramMap, DynamicOps<T> paramDynamicOps, T paramT) {
/* 26 */     RecordBuilder recordBuilder = paramDynamicOps.mapBuilder();
/* 27 */     for (Map.Entry<K, V> entry : paramMap.entrySet()) {
/* 28 */       recordBuilder.add(this.keyCodec.encodeStart(paramDynamicOps, entry.getKey()), encodeValue(this.valueCodecFunction.apply((K)entry.getKey()), entry.getValue(), paramDynamicOps));
/*    */     }
/* 30 */     return recordBuilder.build(paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   private <T, V2 extends V> DataResult<T> encodeValue(Codec<V2> paramCodec, V paramV, DynamicOps<T> paramDynamicOps) {
/* 35 */     return paramCodec.encodeStart(paramDynamicOps, paramV);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 40 */     return paramDynamicOps.getMap(paramT).flatMap(paramMapLike -> {
/*    */           Object2ObjectArrayMap object2ObjectArrayMap = new Object2ObjectArrayMap();
/*    */           Stream.Builder<?> builder = Stream.builder();
/*    */           DataResult dataResult = (DataResult)paramMapLike.entries().reduce(DataResult.success(Unit.INSTANCE, Lifecycle.stable()), (), ());
/*    */           Pair pair = Pair.of(ImmutableMap.copyOf((Map)object2ObjectArrayMap), paramObject);
/*    */           Object object = paramDynamicOps.createMap(builder.build());
/*    */           return dataResult.map(()).setPartial(pair).mapError(());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private <T> DataResult<Unit> parseEntry(DataResult<Unit> paramDataResult, DynamicOps<T> paramDynamicOps, Pair<T, T> paramPair, Map<K, V> paramMap, Stream.Builder<Pair<T, T>> paramBuilder) {
/* 58 */     DataResult dataResult1 = this.keyCodec.parse(paramDynamicOps, paramPair.getFirst());
/* 59 */     DataResult dataResult2 = dataResult1.map(this.valueCodecFunction).flatMap(paramCodec -> paramCodec.parse(paramDynamicOps, paramPair.getSecond()).map(Function.identity()));
/* 60 */     DataResult dataResult3 = dataResult1.apply2stable(Pair::of, dataResult2);
/*    */     
/* 62 */     Optional<Pair> optional = dataResult3.resultOrPartial();
/* 63 */     if (optional.isPresent()) {
/* 64 */       Object object1 = ((Pair)optional.get()).getFirst();
/* 65 */       Object object2 = ((Pair)optional.get()).getSecond();
/* 66 */       if (paramMap.putIfAbsent((K)object1, (V)object2) != null) {
/* 67 */         paramBuilder.add(paramPair);
/* 68 */         return paramDataResult.apply2stable((paramUnit, paramObject) -> paramUnit, DataResult.error(() -> "Duplicate entry for key: '" + String.valueOf(paramObject) + "'"));
/*    */       } 
/*    */     } 
/* 71 */     if (dataResult3.isError()) {
/* 72 */       paramBuilder.add(paramPair);
/*    */     }
/*    */     
/* 75 */     return paramDataResult.apply2stable((paramUnit, paramPair) -> paramUnit, dataResult3);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\DispatchedMapCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */