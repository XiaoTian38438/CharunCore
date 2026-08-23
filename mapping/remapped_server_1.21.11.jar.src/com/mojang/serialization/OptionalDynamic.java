/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OptionalDynamic<T>
/*     */   extends DynamicLike<T>
/*     */ {
/*     */   private final DataResult<Dynamic<T>> delegate;
/*     */   
/*     */   public OptionalDynamic(DynamicOps<T> paramDynamicOps, DataResult<Dynamic<T>> paramDataResult) {
/*  19 */     super(paramDynamicOps);
/*  20 */     this.delegate = paramDataResult;
/*     */   }
/*     */   
/*     */   public DataResult<Dynamic<T>> get() {
/*  24 */     return this.delegate;
/*     */   }
/*     */   
/*     */   public Optional<Dynamic<T>> result() {
/*  28 */     return this.delegate.result();
/*     */   }
/*     */   
/*     */   public <U> DataResult<U> map(Function<? super Dynamic<T>, U> paramFunction) {
/*  32 */     return this.delegate.map(paramFunction);
/*     */   }
/*     */   
/*     */   public <U> DataResult<U> flatMap(Function<? super Dynamic<T>, ? extends DataResult<U>> paramFunction) {
/*  36 */     return this.delegate.flatMap(paramFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Number> asNumber() {
/*  41 */     return flatMap(DynamicLike::asNumber);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> asString() {
/*  46 */     return flatMap(DynamicLike::asString);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Boolean> asBoolean() {
/*  51 */     return flatMap(DynamicLike::asBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Dynamic<T>>> asStreamOpt() {
/*  56 */     return flatMap(DynamicLike::asStreamOpt);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt() {
/*  61 */     return flatMap(DynamicLike::asMapOpt);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<ByteBuffer> asByteBufferOpt() {
/*  66 */     return flatMap(DynamicLike::asByteBufferOpt);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<IntStream> asIntStreamOpt() {
/*  71 */     return flatMap(DynamicLike::asIntStreamOpt);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<LongStream> asLongStreamOpt() {
/*  76 */     return flatMap(DynamicLike::asLongStreamOpt);
/*     */   }
/*     */ 
/*     */   
/*     */   public OptionalDynamic<T> get(String paramString) {
/*  81 */     return new OptionalDynamic(this.ops, this.delegate.flatMap(paramDynamic -> (paramDynamic.get(paramString)).delegate));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> getGeneric(T paramT) {
/*  86 */     return flatMap(paramDynamic -> paramDynamic.getGeneric(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> getElement(String paramString) {
/*  91 */     return flatMap(paramDynamic -> paramDynamic.getElement(paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> getElementGeneric(T paramT) {
/*  96 */     return flatMap(paramDynamic -> paramDynamic.getElementGeneric(paramObject));
/*     */   }
/*     */   
/*     */   public Dynamic<T> orElseEmptyMap() {
/* 100 */     return result().orElseGet(this::emptyMap);
/*     */   }
/*     */   
/*     */   public Dynamic<T> orElseEmptyList() {
/* 104 */     return result().orElseGet(this::emptyList);
/*     */   }
/*     */   
/*     */   public <V> DataResult<V> into(Function<? super Dynamic<T>, ? extends V> paramFunction) {
/* 108 */     return this.delegate.map(paramFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> DataResult<Pair<A, T>> decode(Decoder<? extends A> paramDecoder) {
/* 113 */     return this.delegate.flatMap(paramDynamic -> paramDynamic.decode(paramDecoder));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\OptionalDynamic.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */