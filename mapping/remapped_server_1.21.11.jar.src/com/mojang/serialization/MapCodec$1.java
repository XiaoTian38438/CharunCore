/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Objects;
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
/*    */ class null
/*    */   extends MapCodec<A>
/*    */ {
/*    */   private static final String COMPRESSED_VALUE_KEY = "value";
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 31 */     return Stream.of(paramDynamicOps.createString("value"));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<A> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 36 */     if (paramDynamicOps.compressMaps()) {
/* 37 */       T t = paramMapLike.get("value");
/* 38 */       if (t == null) {
/* 39 */         return DataResult.error(() -> "Missing value");
/*    */       }
/* 41 */       return codec.parse(paramDynamicOps, t);
/*    */     } 
/* 43 */     return codec.parse(paramDynamicOps, paramDynamicOps.createMap(paramMapLike.entries()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(A paramA, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 48 */     DataResult<T> dataResult = codec.encodeStart(paramDynamicOps, paramA);
/* 49 */     if (paramDynamicOps.compressMaps()) {
/* 50 */       return paramRecordBuilder.add("value", dataResult);
/*    */     }
/* 52 */     Objects.requireNonNull(paramDynamicOps); DataResult<?> dataResult1 = dataResult.flatMap(paramDynamicOps::getMap);
/* 53 */     return dataResult1.<RecordBuilder<T>>map(paramMapLike -> {
/*    */           paramMapLike.entries().forEach(());
/*    */           return paramRecordBuilder;
/* 56 */         }).result().orElseGet(() -> paramRecordBuilder.withErrorsFrom(paramDataResult));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapCodec$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */