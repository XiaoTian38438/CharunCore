/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Dynamic<T>
/*     */   extends DynamicLike<T>
/*     */ {
/*     */   private final T value;
/*     */   
/*     */   public Dynamic(DynamicOps<T> paramDynamicOps) {
/*  26 */     this(paramDynamicOps, paramDynamicOps.empty());
/*     */   }
/*     */   
/*     */   public Dynamic(DynamicOps<T> paramDynamicOps, @Nullable T paramT) {
/*  30 */     super(paramDynamicOps);
/*  31 */     this.value = (paramT == null) ? paramDynamicOps.empty() : paramT;
/*     */   }
/*     */   
/*     */   public T getValue() {
/*  35 */     return this.value;
/*     */   }
/*     */   
/*     */   public Dynamic<T> map(Function<? super T, ? extends T> paramFunction) {
/*  39 */     return new Dynamic(this.ops, paramFunction.apply(this.value));
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> Dynamic<U> castTyped(DynamicOps<U> paramDynamicOps) {
/*  44 */     if (!Objects.equals(this.ops, paramDynamicOps)) {
/*  45 */       throw new IllegalStateException("Dynamic type doesn't match");
/*     */     }
/*  47 */     return this;
/*     */   }
/*     */   
/*     */   public <U> U cast(DynamicOps<U> paramDynamicOps) {
/*  51 */     return castTyped(paramDynamicOps).getValue();
/*     */   }
/*     */   
/*     */   public OptionalDynamic<T> merge(Dynamic<?> paramDynamic) {
/*  55 */     DataResult<T> dataResult = this.ops.mergeToList(this.value, paramDynamic.cast(this.ops));
/*  56 */     return new OptionalDynamic<>(this.ops, dataResult.map(paramObject -> new Dynamic(this.ops, (T)paramObject)));
/*     */   }
/*     */   
/*     */   public OptionalDynamic<T> merge(Dynamic<?> paramDynamic1, Dynamic<?> paramDynamic2) {
/*  60 */     DataResult<T> dataResult = this.ops.mergeToMap(this.value, paramDynamic1.cast(this.ops), paramDynamic2.cast(this.ops));
/*  61 */     return new OptionalDynamic<>(this.ops, dataResult.map(paramObject -> new Dynamic(this.ops, (T)paramObject)));
/*     */   }
/*     */   
/*     */   public DataResult<Map<Dynamic<T>, Dynamic<T>>> getMapValues() {
/*  65 */     return this.ops.getMapValues(this.value).map(paramStream -> {
/*     */           ImmutableMap.Builder builder = ImmutableMap.builder();
/*     */           paramStream.forEach(());
/*     */           return (Map)builder.build();
/*     */         });
/*     */   }
/*     */   
/*     */   public Dynamic<T> updateMapValues(Function<Pair<Dynamic<?>, Dynamic<?>>, Pair<Dynamic<?>, Dynamic<?>>> paramFunction) {
/*  73 */     return (Dynamic<T>)DataFixUtils.orElse(getMapValues().map(paramMap -> (Map)paramMap.entrySet().stream().map(()).collect(Pair.toMap()))
/*     */ 
/*     */         
/*  76 */         .map(this::createMap).result(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Number> asNumber() {
/*  81 */     return this.ops.getNumberValue(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> asString() {
/*  86 */     return this.ops.getStringValue(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Boolean> asBoolean() {
/*  91 */     return this.ops.getBooleanValue(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Dynamic<T>>> asStreamOpt() {
/*  96 */     return this.ops.getStream(this.value).map(paramStream -> paramStream.map(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt() {
/* 101 */     return this.ops.getMapValues(this.value).map(paramStream -> paramStream.map(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<ByteBuffer> asByteBufferOpt() {
/* 106 */     return this.ops.getByteBuffer(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<IntStream> asIntStreamOpt() {
/* 111 */     return this.ops.getIntStream(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<LongStream> asLongStreamOpt() {
/* 116 */     return this.ops.getLongStream(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public OptionalDynamic<T> get(String paramString) {
/* 121 */     return new OptionalDynamic<>(this.ops, this.ops.getMap(this.value).flatMap(paramMapLike -> {
/*     */             T t = (T)paramMapLike.get(paramString);
/*     */             return (t == null) ? DataResult.error(()) : DataResult.success(new Dynamic(this.ops, t));
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataResult<T> getGeneric(T paramT) {
/* 132 */     return this.ops.getGeneric(this.value, paramT);
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public Dynamic<T> remove(String paramString) {
/* 137 */     return map(paramObject -> this.ops.remove((T)paramObject, paramString));
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public Dynamic<T> set(String paramString, Dynamic<?> paramDynamic) {
/* 142 */     return map(paramObject -> this.ops.set((T)paramObject, paramString, (T)paramDynamic.cast(this.ops)));
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public Dynamic<T> update(String paramString, Function<Dynamic<?>, Dynamic<?>> paramFunction) {
/* 147 */     return map(paramObject -> this.ops.update((T)paramObject, paramString, ()));
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public Dynamic<T> updateGeneric(T paramT, Function<T, T> paramFunction) {
/* 152 */     return map(paramObject2 -> this.ops.updateGeneric((T)paramObject2, (T)paramObject1, paramFunction));
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public Dynamic<T> setFieldIfPresent(String paramString, Optional<? extends Dynamic<?>> paramOptional) {
/* 157 */     if (paramOptional.isEmpty()) {
/* 158 */       return this;
/*     */     }
/* 160 */     return set(paramString, paramOptional.get());
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public Dynamic<T> renameField(String paramString1, String paramString2) {
/* 165 */     return renameAndFixField(paramString1, paramString2, UnaryOperator.identity());
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public Dynamic<T> replaceField(String paramString1, String paramString2, Optional<? extends Dynamic<?>> paramOptional) {
/* 170 */     return remove(paramString1).setFieldIfPresent(paramString2, paramOptional);
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public Dynamic<T> renameAndFixField(String paramString1, String paramString2, UnaryOperator<Dynamic<?>> paramUnaryOperator) {
/* 175 */     return remove(paramString1).setFieldIfPresent(paramString2, get(paramString1).result().map(paramUnaryOperator));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> getElement(String paramString) {
/* 180 */     return getElementGeneric(this.ops.createString(paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> getElementGeneric(T paramT) {
/* 185 */     return this.ops.getGeneric(this.value, paramT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 190 */     if (this == paramObject) {
/* 191 */       return true;
/*     */     }
/* 193 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 194 */       return false;
/*     */     }
/* 196 */     Dynamic dynamic = (Dynamic)paramObject;
/* 197 */     return (Objects.equals(this.ops, dynamic.ops) && Objects.equals(this.value, dynamic.value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 202 */     int i = this.value.hashCode();
/* 203 */     i = 31 * i + this.ops.hashCode();
/* 204 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 209 */     return String.format("%s[%s]", new Object[] { this.ops, this.value });
/*     */   }
/*     */   
/*     */   public <R> Dynamic<R> convert(DynamicOps<R> paramDynamicOps) {
/* 213 */     return new Dynamic(paramDynamicOps, convert(this.ops, paramDynamicOps, this.value));
/*     */   }
/*     */   
/*     */   public <V> V into(Function<? super Dynamic<T>, ? extends V> paramFunction) {
/* 217 */     return paramFunction.apply(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> DataResult<Pair<A, T>> decode(Decoder<? extends A> paramDecoder) {
/* 222 */     return paramDecoder.<T>decode(this.ops, this.value).map(paramPair -> paramPair.mapFirst(Function.identity()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S, T> T convert(DynamicOps<S> paramDynamicOps, DynamicOps<T> paramDynamicOps1, S paramS) {
/* 227 */     if (Objects.equals(paramDynamicOps, paramDynamicOps1)) {
/* 228 */       return (T)paramS;
/*     */     }
/*     */     
/* 231 */     return paramDynamicOps.convertTo(paramDynamicOps1, paramS);
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public static Dynamic<?> copyField(Dynamic<?> paramDynamic1, String paramString1, Dynamic<?> paramDynamic2, String paramString2) {
/* 236 */     return copyAndFixField(paramDynamic1, paramString1, paramDynamic2, paramString2, UnaryOperator.identity());
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public static <T> Dynamic<?> copyAndFixField(Dynamic<T> paramDynamic, String paramString1, Dynamic<?> paramDynamic1, String paramString2, UnaryOperator<Dynamic<T>> paramUnaryOperator) {
/* 241 */     Optional<Dynamic<T>> optional = paramDynamic.get(paramString1).result();
/* 242 */     if (optional.isPresent()) {
/* 243 */       return paramDynamic1.set(paramString2, paramUnaryOperator.apply(optional.get()));
/*     */     }
/* 245 */     return paramDynamic1;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Dynamic.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */