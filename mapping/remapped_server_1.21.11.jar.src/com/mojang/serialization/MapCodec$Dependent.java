/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.function.BiFunction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Dependent<O, E>
/*     */   extends MapCodec<O>
/*     */ {
/*     */   private final MapCodec<E> initialInstance;
/*     */   private final Function<O, Pair<E, MapCodec<E>>> splitter;
/*     */   private final MapCodec<O> codec;
/*     */   private final BiFunction<O, E, O> combiner;
/*     */   
/*     */   public Dependent(MapCodec<O> paramMapCodec, MapCodec<E> paramMapCodec1, Function<O, Pair<E, MapCodec<E>>> paramFunction, BiFunction<O, E, O> paramBiFunction) {
/* 224 */     this.initialInstance = paramMapCodec1;
/* 225 */     this.splitter = paramFunction;
/* 226 */     this.codec = paramMapCodec;
/* 227 */     this.combiner = paramBiFunction;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 232 */     return Stream.concat(this.codec.keys(paramDynamicOps), this.initialInstance.keys(paramDynamicOps));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> DataResult<O> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 237 */     return this.codec.decode(paramDynamicOps, paramMapLike).flatMap(paramObject -> ((MapCodec)((Pair)this.splitter.apply((O)paramObject)).getSecond()).decode(paramDynamicOps, paramMapLike).map(()).setLifecycle(Lifecycle.experimental()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> RecordBuilder<T> encode(O paramO, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 244 */     this.codec.encode(paramO, paramDynamicOps, paramRecordBuilder);
/* 245 */     Pair pair = this.splitter.apply(paramO);
/* 246 */     ((MapCodec<Object>)pair.getSecond()).encode(pair.getFirst(), paramDynamicOps, paramRecordBuilder);
/* 247 */     return paramRecordBuilder.setLifecycle(Lifecycle.experimental());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapCodec$Dependent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */