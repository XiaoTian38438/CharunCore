/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.codecs.CompoundListCodec;
/*     */ import com.mojang.serialization.codecs.DispatchedMapCodec;
/*     */ import com.mojang.serialization.codecs.EitherCodec;
/*     */ import com.mojang.serialization.codecs.EitherMapCodec;
/*     */ import com.mojang.serialization.codecs.ListCodec;
/*     */ import com.mojang.serialization.codecs.OptionalFieldCodec;
/*     */ import com.mojang.serialization.codecs.PairCodec;
/*     */ import com.mojang.serialization.codecs.PairMapCodec;
/*     */ import com.mojang.serialization.codecs.PrimitiveCodec;
/*     */ import com.mojang.serialization.codecs.SimpleMapCodec;
/*     */ import com.mojang.serialization.codecs.UnboundedMapCodec;
/*     */ import com.mojang.serialization.codecs.XorCodec;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Codec<A>
/*     */   extends Encoder<A>, Decoder<A>
/*     */ {
/*     */   default Codec<A> withLifecycle(final Lifecycle lifecycle) {
/*  40 */     return new Codec<A>()
/*     */       {
/*     */         public <T> DataResult<T> encode(A param1A, DynamicOps<T> param1DynamicOps, T param1T) {
/*  43 */           return Codec.this.encode(param1A, param1DynamicOps, param1T).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*  48 */           return Codec.this.decode(param1DynamicOps, param1T).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  53 */           return Codec.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default Codec<A> stable() {
/*  59 */     return withLifecycle(Lifecycle.stable());
/*     */   }
/*     */   
/*     */   default Codec<A> deprecated(int paramInt) {
/*  63 */     return withLifecycle(Lifecycle.deprecated(paramInt));
/*     */   }
/*     */   
/*     */   static <A> Codec<A> of(Encoder<A> paramEncoder, Decoder<A> paramDecoder) {
/*  67 */     return of(paramEncoder, paramDecoder, "Codec[" + String.valueOf(paramEncoder) + " " + String.valueOf(paramDecoder) + "]");
/*     */   }
/*     */   
/*     */   static <A> Codec<A> of(final Encoder<A> encoder, final Decoder<A> decoder, final String name) {
/*  71 */     return new Codec<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*  74 */           return decoder.decode(param1DynamicOps, param1T);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<T> encode(A param1A, DynamicOps<T> param1DynamicOps, T param1T) {
/*  79 */           return encoder.encode(param1A, param1DynamicOps, param1T);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  84 */           return name;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <A> MapCodec<A> of(MapEncoder<A> paramMapEncoder, MapDecoder<A> paramMapDecoder) {
/*  90 */     return of(paramMapEncoder, paramMapDecoder, () -> "MapCodec[" + String.valueOf(paramMapEncoder) + " " + String.valueOf(paramMapDecoder) + "]");
/*     */   }
/*     */   
/*     */   static <A> MapCodec<A> of(final MapEncoder<A> encoder, final MapDecoder<A> decoder, final Supplier<String> name) {
/*  94 */     return new MapCodec<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*  97 */           return Stream.concat(encoder.keys(param1DynamicOps), decoder.keys(param1DynamicOps));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/* 102 */           return decoder.decode(param1DynamicOps, param1MapLike);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(A param1A, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/* 107 */           return encoder.encode(param1A, param1DynamicOps, param1RecordBuilder);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 112 */           return name.get();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <F, S> Codec<Pair<F, S>> pair(Codec<F> paramCodec, Codec<S> paramCodec1) {
/* 118 */     return (Codec<Pair<F, S>>)new PairCodec(paramCodec, paramCodec1);
/*     */   }
/*     */   
/*     */   static <F, S> Codec<Either<F, S>> either(Codec<F> paramCodec, Codec<S> paramCodec1) {
/* 122 */     return (Codec<Either<F, S>>)new EitherCodec(paramCodec, paramCodec1);
/*     */   }
/*     */   
/*     */   static <F, S> Codec<Either<F, S>> xor(Codec<F> paramCodec, Codec<S> paramCodec1) {
/* 126 */     return (Codec<Either<F, S>>)new XorCodec(paramCodec, paramCodec1);
/*     */   }
/*     */   
/*     */   static <T> Codec<T> withAlternative(Codec<T> paramCodec, Codec<? extends T> paramCodec1) {
/* 130 */     return paramCodec.withAlternative(paramCodec1);
/*     */   }
/*     */   
/*     */   default Codec<A> withAlternative(Codec<? extends A> paramCodec) {
/* 134 */     return either(this, paramCodec)
/*     */ 
/*     */       
/* 137 */       .xmap(Either::unwrap, Either::left);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, U> Codec<T> withAlternative(Codec<T> paramCodec, Codec<U> paramCodec1, Function<U, T> paramFunction) {
/* 144 */     return paramCodec.withAlternative(paramCodec1, paramFunction);
/*     */   }
/*     */   
/*     */   default <U> Codec<A> withAlternative(Codec<U> paramCodec, Function<U, A> paramFunction) {
/* 148 */     return either(this, paramCodec)
/*     */ 
/*     */       
/* 151 */       .xmap(paramEither -> paramEither.map((), paramFunction), Either::left);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <F, S> MapCodec<Pair<F, S>> mapPair(MapCodec<F> paramMapCodec, MapCodec<S> paramMapCodec1) {
/* 158 */     return (MapCodec<Pair<F, S>>)new PairMapCodec(paramMapCodec, paramMapCodec1);
/*     */   }
/*     */   
/*     */   static <F, S> MapCodec<Either<F, S>> mapEither(MapCodec<F> paramMapCodec, MapCodec<S> paramMapCodec1) {
/* 162 */     return (MapCodec<Either<F, S>>)new EitherMapCodec(paramMapCodec, paramMapCodec1);
/*     */   }
/*     */   
/*     */   static <E> Codec<List<E>> list(Codec<E> paramCodec) {
/* 166 */     return list(paramCodec, 0, 2147483647);
/*     */   }
/*     */   
/*     */   static <E> Codec<List<E>> list(Codec<E> paramCodec, int paramInt1, int paramInt2) {
/* 170 */     return (Codec<List<E>>)new ListCodec(paramCodec, paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   static <K, V> Codec<List<Pair<K, V>>> compoundList(Codec<K> paramCodec, Codec<V> paramCodec1) {
/* 174 */     return (Codec<List<Pair<K, V>>>)new CompoundListCodec(paramCodec, paramCodec1);
/*     */   }
/*     */   
/*     */   static <K, V> SimpleMapCodec<K, V> simpleMap(Codec<K> paramCodec, Codec<V> paramCodec1, Keyable paramKeyable) {
/* 178 */     return new SimpleMapCodec(paramCodec, paramCodec1, paramKeyable);
/*     */   }
/*     */   
/*     */   static <K, V> UnboundedMapCodec<K, V> unboundedMap(Codec<K> paramCodec, Codec<V> paramCodec1) {
/* 182 */     return new UnboundedMapCodec(paramCodec, paramCodec1);
/*     */   }
/*     */   
/*     */   static <K, V> Codec<Map<K, V>> dispatchedMap(Codec<K> paramCodec, Function<K, Codec<? extends V>> paramFunction) {
/* 186 */     return (Codec<Map<K, V>>)new DispatchedMapCodec(paramCodec, paramFunction);
/*     */   }
/*     */   
/*     */   static <E> Codec<E> stringResolver(Function<E, String> paramFunction, Function<String, E> paramFunction1) {
/* 190 */     return STRING.flatXmap(paramString -> (DataResult)Optional.ofNullable(paramFunction.apply(paramString)).map(DataResult::success).orElseGet(()), paramObject -> (DataResult)Optional.<String>ofNullable(paramFunction.apply(paramObject)).map(DataResult::success).orElseGet(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <F> MapCodec<Optional<F>> optionalField(String paramString, Codec<F> paramCodec, boolean paramBoolean) {
/* 197 */     return (MapCodec<Optional<F>>)new OptionalFieldCodec(paramString, paramCodec, paramBoolean);
/*     */   }
/*     */   
/*     */   static <A> Codec<A> recursive(String paramString, Function<Codec<A>, Codec<A>> paramFunction) {
/* 201 */     return new RecursiveCodec<>(paramString, paramFunction);
/*     */   }
/*     */   
/*     */   static <A> Codec<A> lazyInitialized(Supplier<Codec<A>> paramSupplier) {
/* 205 */     return new RecursiveCodec<>(paramSupplier.toString(), paramCodec -> (Codec)paramSupplier.get());
/*     */   }
/*     */   
/*     */   public static class RecursiveCodec<T> implements Codec<T> {
/*     */     private final String name;
/*     */     private final Supplier<Codec<T>> wrapped;
/*     */     
/*     */     private RecursiveCodec(String param1String, Function<Codec<T>, Codec<T>> param1Function) {
/* 213 */       this.name = param1String;
/* 214 */       this.wrapped = (Supplier<Codec<T>>)Suppliers.memoize(() -> (Codec)param1Function.apply(this));
/*     */     }
/*     */ 
/*     */     
/*     */     public <S> DataResult<Pair<T, S>> decode(DynamicOps<S> param1DynamicOps, S param1S) {
/* 219 */       return ((Codec)this.wrapped.get()).decode(param1DynamicOps, param1S);
/*     */     }
/*     */ 
/*     */     
/*     */     public <S> DataResult<S> encode(T param1T, DynamicOps<S> param1DynamicOps, S param1S) {
/* 224 */       return ((Codec<T>)this.wrapped.get()).encode(param1T, param1DynamicOps, param1S);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 229 */       return "RecursiveCodec[" + this.name + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   default Codec<List<A>> listOf() {
/* 234 */     return list(this);
/*     */   }
/*     */   
/*     */   default Codec<List<A>> listOf(int paramInt1, int paramInt2) {
/* 238 */     return list(this, paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   default Codec<List<A>> sizeLimitedListOf(int paramInt) {
/* 242 */     return listOf(0, paramInt);
/*     */   }
/*     */   
/*     */   default <S> Codec<S> xmap(Function<? super A, ? extends S> paramFunction, Function<? super S, ? extends A> paramFunction1) {
/* 246 */     return of(comap(paramFunction1), map(paramFunction), toString() + "[xmapped]");
/*     */   }
/*     */   
/*     */   default <S> Codec<S> comapFlatMap(Function<? super A, ? extends DataResult<? extends S>> paramFunction, Function<? super S, ? extends A> paramFunction1) {
/* 250 */     return of(comap(paramFunction1), flatMap(paramFunction), toString() + "[comapFlatMapped]");
/*     */   }
/*     */   
/*     */   default <S> Codec<S> flatComapMap(Function<? super A, ? extends S> paramFunction, Function<? super S, ? extends DataResult<? extends A>> paramFunction1) {
/* 254 */     return of(flatComap(paramFunction1), map(paramFunction), toString() + "[flatComapMapped]");
/*     */   }
/*     */   
/*     */   default <S> Codec<S> flatXmap(Function<? super A, ? extends DataResult<? extends S>> paramFunction, Function<? super S, ? extends DataResult<? extends A>> paramFunction1) {
/* 258 */     return of(flatComap(paramFunction1), flatMap(paramFunction), toString() + "[flatXmapped]");
/*     */   }
/*     */ 
/*     */   
/*     */   default MapCodec<A> fieldOf(String paramString) {
/* 263 */     return MapCodec.of(super
/* 264 */         .fieldOf(paramString), super
/* 265 */         .fieldOf(paramString), () -> "Field[" + paramString + ": " + toString() + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default MapCodec<Optional<A>> optionalFieldOf(String paramString) {
/* 271 */     return optionalField(paramString, this, false);
/*     */   }
/*     */   
/*     */   default MapCodec<A> optionalFieldOf(String paramString, A paramA) {
/* 275 */     return optionalFieldOf(paramString, paramA, false);
/*     */   }
/*     */   
/*     */   default MapCodec<A> optionalFieldOf(String paramString, A paramA, Lifecycle paramLifecycle) {
/* 279 */     return optionalFieldOf(paramString, Lifecycle.experimental(), paramA, paramLifecycle);
/*     */   }
/*     */ 
/*     */   
/*     */   default MapCodec<A> optionalFieldOf(String paramString, Lifecycle paramLifecycle1, A paramA, Lifecycle paramLifecycle2) {
/* 284 */     return optionalFieldOf(paramString, paramLifecycle1, paramA, paramLifecycle2, false);
/*     */   }
/*     */   
/*     */   default MapCodec<Optional<A>> lenientOptionalFieldOf(String paramString) {
/* 288 */     return optionalField(paramString, this, true);
/*     */   }
/*     */   
/*     */   default MapCodec<A> lenientOptionalFieldOf(String paramString, A paramA) {
/* 292 */     return optionalFieldOf(paramString, paramA, true);
/*     */   }
/*     */   
/*     */   default MapCodec<A> lenientOptionalFieldOf(String paramString, A paramA, Lifecycle paramLifecycle) {
/* 296 */     return lenientOptionalFieldOf(paramString, Lifecycle.experimental(), paramA, paramLifecycle);
/*     */   }
/*     */   
/*     */   default MapCodec<A> lenientOptionalFieldOf(String paramString, Lifecycle paramLifecycle1, A paramA, Lifecycle paramLifecycle2) {
/* 300 */     return optionalFieldOf(paramString, paramLifecycle1, paramA, paramLifecycle2, true);
/*     */   }
/*     */   
/*     */   private MapCodec<A> optionalFieldOf(String paramString, A paramA, boolean paramBoolean) {
/* 304 */     return optionalField(paramString, this, paramBoolean).xmap(paramOptional -> paramOptional.orElse(paramObject), paramObject2 -> Objects.equals(paramObject2, paramObject1) ? Optional.empty() : Optional.<Object>of(paramObject2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MapCodec<A> optionalFieldOf(String paramString, Lifecycle paramLifecycle1, A paramA, Lifecycle paramLifecycle2, boolean paramBoolean) {
/* 312 */     return optionalField(paramString, this, paramBoolean).stable().flatXmap(paramOptional -> (DataResult)paramOptional.map(()).orElse(DataResult.success(paramObject, paramLifecycle2)), paramObject2 -> Objects.equals(paramObject2, paramObject1) ? DataResult.success(Optional.empty(), paramLifecycle1) : DataResult.success(Optional.of(paramObject2), paramLifecycle2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Codec<A> mapResult(final ResultFunction<A> function) {
/* 325 */     return new Codec<A>()
/*     */       {
/*     */         public <T> DataResult<T> encode(A param1A, DynamicOps<T> param1DynamicOps, T param1T) {
/* 328 */           return function.coApply(param1DynamicOps, param1A, Codec.this.encode(param1A, param1DynamicOps, param1T));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/* 333 */           return function.apply(param1DynamicOps, param1T, Codec.this.decode(param1DynamicOps, param1T));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 338 */           return String.valueOf(Codec.this) + "[mapResult " + String.valueOf(Codec.this) + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default Codec<A> orElse(Consumer<String> paramConsumer, A paramA) {
/* 344 */     return orElse(DataFixUtils.consumerToFunction(paramConsumer), paramA);
/*     */   }
/*     */   
/*     */   default Codec<A> orElse(final UnaryOperator<String> onError, final A value) {
/* 348 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> param1DynamicOps, T param1T, DataResult<Pair<A, T>> param1DataResult) {
/* 351 */             return DataResult.success(param1DataResult.mapError(onError).result().orElseGet(() -> Pair.of(param1Object1, param1Object2)));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, DataResult<T> param1DataResult) {
/* 356 */             return param1DataResult.mapError(onError);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 361 */             return "OrElse[" + String.valueOf(onError) + " " + String.valueOf(value) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   default Codec<A> orElseGet(Consumer<String> paramConsumer, Supplier<? extends A> paramSupplier) {
/* 367 */     return orElseGet(DataFixUtils.consumerToFunction(paramConsumer), paramSupplier);
/*     */   } public static interface ResultFunction<A> {
/*     */     <T> DataResult<Pair<A, T>> apply(DynamicOps<T> param1DynamicOps, T param1T, DataResult<Pair<A, T>> param1DataResult); <T> DataResult<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, DataResult<T> param1DataResult); }
/*     */   default Codec<A> orElseGet(final UnaryOperator<String> onError, final Supplier<? extends A> value) {
/* 371 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> param1DynamicOps, T param1T, DataResult<Pair<A, T>> param1DataResult) {
/* 374 */             return DataResult.success(param1DataResult.mapError(onError).result().orElseGet(() -> Pair.of(param1Supplier.get(), param1Object)));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, DataResult<T> param1DataResult) {
/* 379 */             return param1DataResult.mapError(onError);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 384 */             return "OrElseGet[" + String.valueOf(onError) + " " + String.valueOf(value.get()) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   default Codec<A> orElse(final A value) {
/* 390 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> param1DynamicOps, T param1T, DataResult<Pair<A, T>> param1DataResult) {
/* 393 */             return DataResult.success(param1DataResult.result().orElseGet(() -> Pair.of(param1Object1, param1Object2)));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, DataResult<T> param1DataResult) {
/* 398 */             return param1DataResult;
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 403 */             return "OrElse[" + String.valueOf(value) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   default Codec<A> orElseGet(final Supplier<? extends A> value) {
/* 409 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> param1DynamicOps, T param1T, DataResult<Pair<A, T>> param1DataResult) {
/* 412 */             return DataResult.success(param1DataResult.result().orElseGet(() -> Pair.of(param1Supplier.get(), param1Object)));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, DataResult<T> param1DataResult) {
/* 417 */             return param1DataResult;
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 422 */             return "OrElseGet[" + String.valueOf(value.get()) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   default Codec<A> promotePartial(Consumer<String> paramConsumer) {
/* 429 */     return of(this, super.promotePartial(paramConsumer));
/*     */   }
/*     */   
/*     */   default <E> Codec<E> dispatch(Function<? super E, ? extends A> paramFunction, Function<? super A, ? extends MapCodec<? extends E>> paramFunction1) {
/* 433 */     return dispatch("type", paramFunction, paramFunction1);
/*     */   }
/*     */   
/*     */   default <E> Codec<E> dispatch(String paramString, Function<? super E, ? extends A> paramFunction, Function<? super A, ? extends MapCodec<? extends E>> paramFunction1) {
/* 437 */     return fieldOf(paramString).dispatch(paramFunction, paramFunction1);
/*     */   }
/*     */   
/*     */   default <E> Codec<E> dispatchStable(Function<? super E, ? extends A> paramFunction, Function<? super A, ? extends MapCodec<? extends E>> paramFunction1) {
/* 441 */     return fieldOf("type").dispatchStable(paramFunction, paramFunction1);
/*     */   }
/*     */   
/*     */   default <E> Codec<E> partialDispatch(String paramString, Function<? super E, ? extends DataResult<? extends A>> paramFunction, Function<? super A, ? extends DataResult<? extends MapCodec<? extends E>>> paramFunction1) {
/* 445 */     return fieldOf(paramString).partialDispatch(paramFunction, paramFunction1);
/*     */   }
/*     */   
/*     */   default <E> MapCodec<E> dispatchMap(Function<? super E, ? extends A> paramFunction, Function<? super A, ? extends MapCodec<? extends E>> paramFunction1) {
/* 449 */     return dispatchMap("type", paramFunction, paramFunction1);
/*     */   }
/*     */   
/*     */   default <E> MapCodec<E> dispatchMap(String paramString, Function<? super E, ? extends A> paramFunction, Function<? super A, ? extends MapCodec<? extends E>> paramFunction1) {
/* 453 */     return fieldOf(paramString).dispatchMap(paramFunction, paramFunction1);
/*     */   }
/*     */   
/*     */   default Codec<A> validate(Function<A, DataResult<A>> paramFunction) {
/* 457 */     return flatXmap(paramFunction, paramFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   static <N extends Number & Comparable<N>> Function<N, DataResult<N>> checkRange(N paramN1, N paramN2) {
/* 462 */     return paramNumber3 -> 
/* 463 */       (((Comparable<Number>)paramNumber3).compareTo(paramNumber1) >= 0 && ((Comparable<Number>)paramNumber3).compareTo(paramNumber2) <= 0) ? DataResult.success(paramNumber3) : DataResult.error(());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Codec<Integer> intRange(int paramInt1, int paramInt2) {
/* 471 */     Function<Integer, DataResult<Integer>> function = checkRange(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
/* 472 */     return INT.flatXmap(function, function);
/*     */   }
/*     */   
/*     */   static Codec<Float> floatRange(float paramFloat1, float paramFloat2) {
/* 476 */     Function<Float, DataResult<Float>> function = checkRange(Float.valueOf(paramFloat1), Float.valueOf(paramFloat2));
/* 477 */     return FLOAT.flatXmap(function, function);
/*     */   }
/*     */   
/*     */   static Codec<Double> doubleRange(double paramDouble1, double paramDouble2) {
/* 481 */     Function<Double, DataResult<Double>> function = checkRange(Double.valueOf(paramDouble1), Double.valueOf(paramDouble2));
/* 482 */     return DOUBLE.flatXmap(function, function);
/*     */   }
/*     */   
/*     */   static Codec<String> string(int paramInt1, int paramInt2) {
/* 486 */     return STRING.validate(paramString -> {
/*     */           int i = paramString.length();
/*     */           return (i < paramInt1) ? DataResult.error(()) : ((i > paramInt2) ? DataResult.error(()) : DataResult.success(paramString));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Codec<String> sizeLimitedString(int paramInt) {
/* 499 */     return string(0, paramInt);
/*     */   }
/*     */   
/* 502 */   public static final PrimitiveCodec<Boolean> BOOL = new PrimitiveCodec<Boolean>()
/*     */     {
/*     */       public <T> DataResult<Boolean> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 505 */         return param1DynamicOps
/* 506 */           .getBooleanValue(param1T);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, Boolean param1Boolean) {
/* 511 */         return param1DynamicOps.createBoolean(param1Boolean.booleanValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 516 */         return "Bool";
/*     */       }
/*     */     };
/*     */   
/* 520 */   public static final PrimitiveCodec<Byte> BYTE = new PrimitiveCodec<Byte>()
/*     */     {
/*     */       public <T> DataResult<Byte> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 523 */         return param1DynamicOps
/* 524 */           .getNumberValue(param1T)
/* 525 */           .map(Number::byteValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, Byte param1Byte) {
/* 530 */         return param1DynamicOps.createByte(param1Byte.byteValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 535 */         return "Byte";
/*     */       }
/*     */     };
/*     */   
/* 539 */   public static final PrimitiveCodec<Short> SHORT = new PrimitiveCodec<Short>()
/*     */     {
/*     */       public <T> DataResult<Short> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 542 */         return param1DynamicOps
/* 543 */           .getNumberValue(param1T)
/* 544 */           .map(Number::shortValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, Short param1Short) {
/* 549 */         return param1DynamicOps.createShort(param1Short.shortValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 554 */         return "Short";
/*     */       }
/*     */     };
/*     */   
/* 558 */   public static final PrimitiveCodec<Integer> INT = new PrimitiveCodec<Integer>()
/*     */     {
/*     */       public <T> DataResult<Integer> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 561 */         return param1DynamicOps
/* 562 */           .getNumberValue(param1T)
/* 563 */           .map(Number::intValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, Integer param1Integer) {
/* 568 */         return param1DynamicOps.createInt(param1Integer.intValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 573 */         return "Int";
/*     */       }
/*     */     };
/*     */   
/* 577 */   public static final PrimitiveCodec<Long> LONG = new PrimitiveCodec<Long>()
/*     */     {
/*     */       public <T> DataResult<Long> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 580 */         return param1DynamicOps
/* 581 */           .getNumberValue(param1T)
/* 582 */           .map(Number::longValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, Long param1Long) {
/* 587 */         return param1DynamicOps.createLong(param1Long.longValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 592 */         return "Long";
/*     */       }
/*     */     };
/*     */   
/* 596 */   public static final PrimitiveCodec<Float> FLOAT = new PrimitiveCodec<Float>()
/*     */     {
/*     */       public <T> DataResult<Float> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 599 */         return param1DynamicOps
/* 600 */           .getNumberValue(param1T)
/* 601 */           .map(Number::floatValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, Float param1Float) {
/* 606 */         return param1DynamicOps.createFloat(param1Float.floatValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 611 */         return "Float";
/*     */       }
/*     */     };
/*     */   
/* 615 */   public static final PrimitiveCodec<Double> DOUBLE = new PrimitiveCodec<Double>()
/*     */     {
/*     */       public <T> DataResult<Double> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 618 */         return param1DynamicOps
/* 619 */           .getNumberValue(param1T)
/* 620 */           .map(Number::doubleValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, Double param1Double) {
/* 625 */         return param1DynamicOps.createDouble(param1Double.doubleValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 630 */         return "Double";
/*     */       }
/*     */     };
/*     */   
/* 634 */   public static final PrimitiveCodec<String> STRING = new PrimitiveCodec<String>()
/*     */     {
/*     */       public <T> DataResult<String> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 637 */         return param1DynamicOps
/* 638 */           .getStringValue(param1T);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, String param1String) {
/* 643 */         return param1DynamicOps.createString(param1String);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 648 */         return "String";
/*     */       }
/*     */     };
/*     */   
/* 652 */   public static final PrimitiveCodec<ByteBuffer> BYTE_BUFFER = new PrimitiveCodec<ByteBuffer>()
/*     */     {
/*     */       public <T> DataResult<ByteBuffer> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 655 */         return param1DynamicOps
/* 656 */           .getByteBuffer(param1T);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, ByteBuffer param1ByteBuffer) {
/* 661 */         return param1DynamicOps.createByteList(param1ByteBuffer);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 666 */         return "ByteBuffer";
/*     */       }
/*     */     };
/*     */   
/* 670 */   public static final PrimitiveCodec<IntStream> INT_STREAM = new PrimitiveCodec<IntStream>()
/*     */     {
/*     */       public <T> DataResult<IntStream> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 673 */         return param1DynamicOps
/* 674 */           .getIntStream(param1T);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, IntStream param1IntStream) {
/* 679 */         return param1DynamicOps.createIntList(param1IntStream);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 684 */         return "IntStream";
/*     */       }
/*     */     };
/*     */   
/* 688 */   public static final PrimitiveCodec<LongStream> LONG_STREAM = new PrimitiveCodec<LongStream>()
/*     */     {
/*     */       public <T> DataResult<LongStream> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 691 */         return param1DynamicOps
/* 692 */           .getLongStream(param1T);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> param1DynamicOps, LongStream param1LongStream) {
/* 697 */         return param1DynamicOps.createLongList(param1LongStream);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 702 */         return "LongStream";
/*     */       }
/*     */     };
/*     */   
/* 706 */   public static final Codec<Dynamic<?>> PASSTHROUGH = new Codec<Dynamic<?>>()
/*     */     {
/*     */       public <T> DataResult<Pair<Dynamic<?>, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/* 709 */         return DataResult.success(Pair.of(new Dynamic<>(param1DynamicOps, param1T), param1DynamicOps.empty()));
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> DataResult<T> encode(Dynamic<?> param1Dynamic, DynamicOps<T> param1DynamicOps, T param1T) {
/* 714 */         if (param1Dynamic.getValue() == param1Dynamic.getOps().empty())
/*     */         {
/* 716 */           return DataResult.success(param1T, Lifecycle.experimental());
/*     */         }
/*     */         
/* 719 */         T t = (T)param1Dynamic.<Object>convert((DynamicOps)param1DynamicOps).getValue();
/* 720 */         if (param1T == param1DynamicOps.empty())
/*     */         {
/* 722 */           return DataResult.success(t, Lifecycle.experimental());
/*     */         }
/*     */         
/* 725 */         DataResult dataResult = param1DynamicOps.getMap(t).flatMap(param1MapLike -> param1DynamicOps.mergeToMap(param1Object, param1MapLike));
/* 726 */         return dataResult.result().map(DataResult::success).orElseGet(() -> {
/*     */               DataResult dataResult = param1DynamicOps.getStream(param1Object1).flatMap(());
/*     */               return dataResult.result().map(DataResult::success).orElseGet(());
/*     */             });
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public String toString() {
/* 736 */         return "passthrough";
/*     */       }
/*     */     };
/*     */   
/* 740 */   public static final MapCodec<Unit> EMPTY = MapCodec.unit(Unit.INSTANCE);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Codec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */