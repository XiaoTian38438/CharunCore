/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends MapCodec<A>
/*     */ {
/*     */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/*  97 */     return Stream.concat(encoder.keys(paramDynamicOps), decoder.keys(paramDynamicOps));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> DataResult<A> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 102 */     return decoder.decode(paramDynamicOps, paramMapLike);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> RecordBuilder<T> encode(A paramA, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 107 */     return encoder.encode(paramA, paramDynamicOps, paramRecordBuilder);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return name.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Codec$3.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */