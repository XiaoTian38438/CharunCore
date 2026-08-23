/*     */ package com.mojang.serialization.codecs;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapEncoder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends MapCodec<O>
/*     */ {
/*     */   public <T> DataResult<O> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 107 */     return builder.decoder.decode(paramDynamicOps, paramMapLike);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> RecordBuilder<T> encode(O paramO, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 112 */     return ((MapEncoder)builder.encoder.apply(paramO)).encode(paramO, paramDynamicOps, paramRecordBuilder);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 117 */     return builder.decoder.keys(paramDynamicOps);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 122 */     return "RecordCodec[" + String.valueOf(builder.decoder) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\RecordCodecBuilder$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */