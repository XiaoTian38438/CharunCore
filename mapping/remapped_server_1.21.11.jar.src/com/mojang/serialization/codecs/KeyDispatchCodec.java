/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapDecoder;
/*    */ import com.mojang.serialization.MapEncoder;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class KeyDispatchCodec<K, V>
/*    */   extends MapCodec<V> {
/*    */   private static final String COMPRESSED_VALUE_KEY = "value";
/*    */   private final MapCodec<K> keyCodec;
/*    */   private final Function<? super V, ? extends DataResult<? extends K>> type;
/*    */   private final Function<? super K, ? extends DataResult<? extends MapDecoder<? extends V>>> decoder;
/*    */   private final Function<? super V, ? extends DataResult<? extends MapEncoder<V>>> encoder;
/*    */   
/*    */   protected KeyDispatchCodec(MapCodec<K> paramMapCodec, Function<? super V, ? extends DataResult<? extends K>> paramFunction, Function<? super K, ? extends DataResult<? extends MapDecoder<? extends V>>> paramFunction1, Function<? super V, ? extends DataResult<? extends MapEncoder<V>>> paramFunction2) {
/* 22 */     this.keyCodec = paramMapCodec;
/* 23 */     this.type = paramFunction;
/* 24 */     this.decoder = paramFunction1;
/* 25 */     this.encoder = paramFunction2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public KeyDispatchCodec(MapCodec<K> paramMapCodec, Function<? super V, ? extends DataResult<? extends K>> paramFunction, Function<? super K, ? extends DataResult<? extends MapCodec<? extends V>>> paramFunction1) {
/* 32 */     this(paramMapCodec, paramFunction, (Function)paramFunction1, paramObject -> getCodec(paramFunction1, paramFunction2, paramObject));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<V> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 37 */     return this.keyCodec.decode(paramDynamicOps, paramMapLike).flatMap(paramObject -> ((DataResult)this.decoder.apply((K)paramObject)).flatMap(()));
/*    */   }
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
/*    */   public <T> RecordBuilder<T> encode(V paramV, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 53 */     DataResult dataResult1 = this.encoder.apply(paramV);
/* 54 */     DataResult dataResult2 = this.type.apply(paramV);
/*    */     
/* 56 */     RecordBuilder<T> recordBuilder = paramRecordBuilder.withErrorsFrom(dataResult1).withErrorsFrom(dataResult2);
/* 57 */     if (dataResult1.isError() || dataResult2.isError()) {
/* 58 */       return recordBuilder;
/*    */     }
/*    */     
/* 61 */     MapEncoder mapEncoder = (MapEncoder)dataResult1.getOrThrow();
/* 62 */     Object object = dataResult2.getOrThrow();
/*    */     
/* 64 */     if (paramDynamicOps.compressMaps()) {
/* 65 */       return this.keyCodec.encode(object, paramDynamicOps, recordBuilder)
/* 66 */         .add("value", mapEncoder.encoder().encodeStart(paramDynamicOps, paramV));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 71 */     RecordBuilder recordBuilder1 = mapEncoder.encode(paramV, paramDynamicOps, recordBuilder);
/* 72 */     return this.keyCodec.encode(object, paramDynamicOps, recordBuilder1);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 77 */     return Stream.concat(this.keyCodec
/* 78 */         .keys(paramDynamicOps), 
/* 79 */         Stream.of((T)paramDynamicOps.createString("value")));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <K, V> DataResult<? extends MapEncoder<V>> getCodec(Function<? super V, ? extends DataResult<? extends K>> paramFunction, Function<? super K, ? extends DataResult<? extends MapEncoder<? extends V>>> paramFunction1, V paramV) {
/* 85 */     return ((DataResult)paramFunction.apply(paramV))
/* 86 */       .flatMap(paramObject -> ((DataResult)paramFunction.apply(paramObject)).map(Function.identity()))
/* 87 */       .map(paramMapEncoder -> paramMapEncoder);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     return "KeyDispatchCodec[" + this.keyCodec.toString() + " " + String.valueOf(this.type) + " " + String.valueOf(this.decoder) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\KeyDispatchCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */