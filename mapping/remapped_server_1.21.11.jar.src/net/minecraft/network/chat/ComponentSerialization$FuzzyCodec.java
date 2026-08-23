/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapDecoder;
/*     */ import com.mojang.serialization.MapEncoder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Function;
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
/*     */ class FuzzyCodec<T>
/*     */   extends MapCodec<T>
/*     */ {
/*     */   private final Collection<MapCodec<? extends T>> codecs;
/*     */   private final Function<T, ? extends MapEncoder<? extends T>> encoderGetter;
/*     */   
/*     */   public FuzzyCodec(Collection<MapCodec<? extends T>> paramCollection, Function<T, ? extends MapEncoder<? extends T>> paramFunction) {
/* 127 */     this.codecs = paramCollection;
/* 128 */     this.encoderGetter = paramFunction;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <S> DataResult<T> decode(DynamicOps<S> paramDynamicOps, MapLike<S> paramMapLike) {
/* 134 */     for (MapDecoder mapDecoder : this.codecs) {
/* 135 */       DataResult<T> dataResult = mapDecoder.decode(paramDynamicOps, paramMapLike);
/* 136 */       if (dataResult.result().isPresent()) {
/* 137 */         return dataResult;
/*     */       }
/*     */     } 
/*     */     
/* 141 */     return DataResult.error(() -> "No matching codec found");
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> RecordBuilder<S> encode(T paramT, DynamicOps<S> paramDynamicOps, RecordBuilder<S> paramRecordBuilder) {
/* 146 */     MapEncoder mapEncoder = this.encoderGetter.apply(paramT);
/* 147 */     return mapEncoder.encode(paramT, paramDynamicOps, paramRecordBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <S> Stream<S> keys(DynamicOps<S> paramDynamicOps) {
/* 154 */     return this.codecs.stream().flatMap(paramMapCodec -> paramMapCodec.keys(paramDynamicOps)).distinct();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 159 */     return "FuzzyCodec[" + String.valueOf(this.codecs) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\ComponentSerialization$FuzzyCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */