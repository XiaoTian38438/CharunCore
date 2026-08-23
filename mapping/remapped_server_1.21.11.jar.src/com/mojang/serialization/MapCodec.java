/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.codecs.KeyDispatchCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MapCodec<A>
/*     */   extends CompressorHolder
/*     */   implements MapDecoder<A>, MapEncoder<A>
/*     */ {
/*     */   public static <A> MapCodec<A> assumeMapUnsafe(final Codec<A> codec) {
/*  26 */     return new MapCodec<A>()
/*     */       {
/*     */         private static final String COMPRESSED_VALUE_KEY = "value";
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*  31 */           return Stream.of(param1DynamicOps.createString("value"));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/*  36 */           if (param1DynamicOps.compressMaps()) {
/*  37 */             T t = param1MapLike.get("value");
/*  38 */             if (t == null) {
/*  39 */               return DataResult.error(() -> "Missing value");
/*     */             }
/*  41 */             return codec.parse(param1DynamicOps, t);
/*     */           } 
/*  43 */           return codec.parse(param1DynamicOps, param1DynamicOps.createMap(param1MapLike.entries()));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(A param1A, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/*  48 */           DataResult<T> dataResult = codec.encodeStart(param1DynamicOps, param1A);
/*  49 */           if (param1DynamicOps.compressMaps()) {
/*  50 */             return param1RecordBuilder.add("value", dataResult);
/*     */           }
/*  52 */           Objects.requireNonNull(param1DynamicOps); DataResult<?> dataResult1 = dataResult.flatMap(param1DynamicOps::getMap);
/*  53 */           return dataResult1.<RecordBuilder<T>>map(param1MapLike -> {
/*     */                 param1MapLike.entries().forEach(());
/*     */                 return param1RecordBuilder;
/*  56 */               }).result().orElseGet(() -> param1RecordBuilder.withErrorsFrom(param1DataResult));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public final <O> RecordCodecBuilder<O, A> forGetter(Function<O, A> paramFunction) {
/*  62 */     return RecordCodecBuilder.of(paramFunction, this);
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> of(MapEncoder<A> paramMapEncoder, MapDecoder<A> paramMapDecoder) {
/*  66 */     return of(paramMapEncoder, paramMapDecoder, () -> "MapCodec[" + String.valueOf(paramMapEncoder) + " " + String.valueOf(paramMapDecoder) + "]");
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> of(final MapEncoder<A> encoder, final MapDecoder<A> decoder, final Supplier<String> name) {
/*  70 */     return new MapCodec<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*  73 */           return Stream.concat(encoder.keys(param1DynamicOps), decoder.keys(param1DynamicOps));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/*  78 */           return decoder.decode(param1DynamicOps, param1MapLike);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(A param1A, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/*  83 */           return encoder.encode(param1A, param1DynamicOps, param1RecordBuilder);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  88 */           return name.get();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> recursive(String paramString, Function<Codec<A>, MapCodec<A>> paramFunction) {
/*  94 */     return new RecursiveMapCodec<>(paramString, paramFunction);
/*     */   }
/*     */   
/*     */   private static class RecursiveMapCodec<A> extends MapCodec<A> {
/*     */     private final String name;
/*     */     private final Supplier<MapCodec<A>> wrapped;
/*     */     
/*     */     private RecursiveMapCodec(String param1String, Function<Codec<A>, MapCodec<A>> param1Function) {
/* 102 */       this.name = param1String;
/* 103 */       this.wrapped = (Supplier<MapCodec<A>>)Suppliers.memoize(() -> (MapCodec)param1Function.apply(codec()));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> RecordBuilder<T> encode(A param1A, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/* 108 */       return ((MapCodec<A>)this.wrapped.get()).encode(param1A, param1DynamicOps, param1RecordBuilder);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/* 113 */       return ((MapCodec<A>)this.wrapped.get()).decode(param1DynamicOps, param1MapLike);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 118 */       return ((MapCodec)this.wrapped.get()).keys(param1DynamicOps);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 123 */       return "RecursiveMapCodec[" + this.name + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   public MapCodec<A> fieldOf(String paramString) {
/* 128 */     return codec().fieldOf(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public MapCodec<A> withLifecycle(final Lifecycle lifecycle) {
/* 133 */     return new MapCodec<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 136 */           return MapCodec.this.keys(param1DynamicOps);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/* 141 */           return MapCodec.this.decode(param1DynamicOps, param1MapLike).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(A param1A, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/* 146 */           return MapCodec.this.encode(param1A, param1DynamicOps, param1RecordBuilder).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 151 */           return MapCodec.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   public static final class MapCodecCodec<A> extends Record implements Codec<A> { private final MapCodec<A> codec;
/* 156 */     public MapCodecCodec(MapCodec<A> param1MapCodec) { this.codec = param1MapCodec; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/MapCodec$MapCodecCodec;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 156 */       //   #156	-> 0 } public MapCodec<A> codec() { return this.codec; } public final boolean equals(Object param1Object) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/serialization/MapCodec$MapCodecCodec;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #156	-> 0
/*     */     } public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/* 159 */       return this.codec.compressedDecode(param1DynamicOps, param1T).map(param1Object2 -> Pair.of(param1Object2, param1Object1));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> DataResult<T> encode(A param1A, DynamicOps<T> param1DynamicOps, T param1T) {
/* 164 */       return this.codec.encode(param1A, param1DynamicOps, this.codec.compressedBuilder(param1DynamicOps)).build(param1T);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 169 */       return this.codec.toString();
/*     */     } }
/*     */ 
/*     */   
/*     */   public Codec<A> codec() {
/* 174 */     return new MapCodecCodec<>(this);
/*     */   }
/*     */   
/*     */   public MapCodec<A> stable() {
/* 178 */     return withLifecycle(Lifecycle.stable());
/*     */   }
/*     */   
/*     */   public MapCodec<A> deprecated(int paramInt) {
/* 182 */     return withLifecycle(Lifecycle.deprecated(paramInt));
/*     */   }
/*     */   
/*     */   public <S> MapCodec<S> xmap(Function<? super A, ? extends S> paramFunction, Function<? super S, ? extends A> paramFunction1) {
/* 186 */     return of(comap(paramFunction1), map(paramFunction), () -> toString() + "[xmapped]");
/*     */   }
/*     */   
/*     */   public <S> MapCodec<S> flatXmap(Function<? super A, ? extends DataResult<? extends S>> paramFunction, Function<? super S, ? extends DataResult<? extends A>> paramFunction1) {
/* 190 */     return Codec.of(flatComap(paramFunction1), flatMap(paramFunction), () -> toString() + "[flatXmapped]");
/*     */   }
/*     */   
/*     */   public MapCodec<A> validate(Function<A, DataResult<A>> paramFunction) {
/* 194 */     return flatXmap(paramFunction, paramFunction);
/*     */   }
/*     */   
/*     */   public <E> MapCodec<A> dependent(MapCodec<E> paramMapCodec, Function<A, Pair<E, MapCodec<E>>> paramFunction, BiFunction<A, E, A> paramBiFunction) {
/* 198 */     return new Dependent<>(this, paramMapCodec, paramFunction, paramBiFunction);
/*     */   }
/*     */   
/*     */   public <E> Codec<E> dispatch(Function<? super E, ? extends A> paramFunction, Function<? super A, ? extends MapCodec<? extends E>> paramFunction1) {
/* 202 */     return partialDispatch(paramFunction.andThen(DataResult::success), paramFunction1.andThen(DataResult::success));
/*     */   }
/*     */   
/*     */   public <E> Codec<E> dispatchStable(Function<? super E, ? extends A> paramFunction, Function<? super A, ? extends MapCodec<? extends E>> paramFunction1) {
/* 206 */     return partialDispatch(paramObject -> DataResult.success(paramFunction.apply(paramObject), Lifecycle.stable()), paramObject -> DataResult.success(paramFunction.apply(paramObject), Lifecycle.stable()));
/*     */   }
/*     */   
/*     */   public <E> Codec<E> partialDispatch(Function<? super E, ? extends DataResult<? extends A>> paramFunction, Function<? super A, ? extends DataResult<? extends MapCodec<? extends E>>> paramFunction1) {
/* 210 */     return (new KeyDispatchCodec(this, paramFunction, paramFunction1)).codec();
/*     */   }
/*     */   
/*     */   public <E> MapCodec<E> dispatchMap(Function<? super E, ? extends A> paramFunction, Function<? super A, ? extends MapCodec<? extends E>> paramFunction1) {
/* 214 */     return (MapCodec<E>)new KeyDispatchCodec(this, paramFunction.andThen(DataResult::success), paramFunction1.andThen(DataResult::success));
/*     */   }
/*     */   
/*     */   private static class Dependent<O, E> extends MapCodec<O> {
/*     */     private final MapCodec<E> initialInstance;
/*     */     private final Function<O, Pair<E, MapCodec<E>>> splitter;
/*     */     private final MapCodec<O> codec;
/*     */     private final BiFunction<O, E, O> combiner;
/*     */     
/*     */     public Dependent(MapCodec<O> param1MapCodec, MapCodec<E> param1MapCodec1, Function<O, Pair<E, MapCodec<E>>> param1Function, BiFunction<O, E, O> param1BiFunction) {
/* 224 */       this.initialInstance = param1MapCodec1;
/* 225 */       this.splitter = param1Function;
/* 226 */       this.codec = param1MapCodec;
/* 227 */       this.combiner = param1BiFunction;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 232 */       return Stream.concat(this.codec.keys(param1DynamicOps), this.initialInstance.keys(param1DynamicOps));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> DataResult<O> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/* 237 */       return this.codec.decode(param1DynamicOps, param1MapLike).flatMap(param1Object -> ((MapCodec)((Pair)this.splitter.apply((O)param1Object)).getSecond()).decode(param1DynamicOps, param1MapLike).map(()).setLifecycle(Lifecycle.experimental()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> RecordBuilder<T> encode(O param1O, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/* 244 */       this.codec.encode(param1O, param1DynamicOps, param1RecordBuilder);
/* 245 */       Pair pair = this.splitter.apply(param1O);
/* 246 */       ((MapCodec<Object>)pair.getSecond()).encode(pair.getFirst(), param1DynamicOps, param1RecordBuilder);
/* 247 */       return param1RecordBuilder.setLifecycle(Lifecycle.experimental());
/*     */     }
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
/*     */   public MapCodec<A> mapResult(final ResultFunction<A> function) {
/* 261 */     return new MapCodec<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 264 */           return MapCodec.this.keys(param1DynamicOps);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(A param1A, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/* 269 */           return function.coApply(param1DynamicOps, param1A, MapCodec.this.encode(param1A, param1DynamicOps, param1RecordBuilder));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/* 274 */           return function.apply(param1DynamicOps, param1MapLike, MapCodec.this.decode(param1DynamicOps, param1MapLike));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 279 */           return String.valueOf(MapCodec.this) + "[mapResult " + String.valueOf(MapCodec.this) + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElse(Consumer<String> paramConsumer, A paramA) {
/* 285 */     return orElse(DataFixUtils.consumerToFunction(paramConsumer), paramA);
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElse(final UnaryOperator<String> onError, final A value) {
/* 289 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<A> apply(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike, DataResult<A> param1DataResult) {
/* 292 */             return DataResult.success(param1DataResult.mapError(onError).result().orElse((A)value));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, RecordBuilder<T> param1RecordBuilder) {
/* 297 */             return param1RecordBuilder.mapError(onError);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 302 */             return "OrElse[" + String.valueOf(onError) + " " + String.valueOf(value) + "]";
/*     */           }
/*     */         });
/*     */   } public static interface ResultFunction<A> {
/*     */     <T> DataResult<A> apply(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike, DataResult<A> param1DataResult); <T> RecordBuilder<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, RecordBuilder<T> param1RecordBuilder); }
/*     */   public MapCodec<A> orElseGet(Consumer<String> paramConsumer, Supplier<? extends A> paramSupplier) {
/* 308 */     return orElseGet(DataFixUtils.consumerToFunction(paramConsumer), paramSupplier);
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElseGet(final UnaryOperator<String> onError, final Supplier<? extends A> value) {
/* 312 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<A> apply(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike, DataResult<A> param1DataResult) {
/* 315 */             return DataResult.success(param1DataResult.mapError(onError).result().orElseGet(value));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, RecordBuilder<T> param1RecordBuilder) {
/* 320 */             return param1RecordBuilder.mapError(onError);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 325 */             return "OrElseGet[" + String.valueOf(onError) + " " + String.valueOf(value.get()) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElse(final A value) {
/* 331 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<A> apply(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike, DataResult<A> param1DataResult) {
/* 334 */             return DataResult.success(param1DataResult.result().orElse((A)value));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, RecordBuilder<T> param1RecordBuilder) {
/* 339 */             return param1RecordBuilder;
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 344 */             return "OrElse[" + String.valueOf(value) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElseGet(final Supplier<? extends A> value) {
/* 350 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<A> apply(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike, DataResult<A> param1DataResult) {
/* 353 */             return DataResult.success(param1DataResult.result().orElseGet(value));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, RecordBuilder<T> param1RecordBuilder) {
/* 358 */             return param1RecordBuilder;
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 363 */             return "OrElseGet[" + String.valueOf(value.get()) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public MapCodec<A> setPartial(final Supplier<A> value) {
/* 369 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<A> apply(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike, DataResult<A> param1DataResult) {
/* 372 */             return param1DataResult.setPartial(value);
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, RecordBuilder<T> param1RecordBuilder) {
/* 377 */             return param1RecordBuilder;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public String toString() {
/* 383 */             return "SetPartial[" + String.valueOf(value) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> unit(A paramA) {
/* 389 */     return unit(() -> paramObject);
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> unit(final Supplier<A> value) {
/* 393 */     return new MapCodec<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 396 */           return Stream.empty();
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/* 401 */           return DataResult.success(value.get());
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(A param1A, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/* 406 */           return param1RecordBuilder;
/*     */         }
/*     */ 
/*     */         
/*     */         public Codec<A> codec() {
/* 411 */           return unitCodec(value);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 416 */           return "Unit[" + String.valueOf(value.get()) + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <A> Codec<A> unitCodec(A paramA) {
/* 422 */     return unitCodec(() -> paramObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A> Codec<A> unitCodec(final Supplier<A> value) {
/* 430 */     return new Codec<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T)
/*     */         {
/* 434 */           DataResult dataResult = (DataResult)(param1DynamicOps.compressMaps() ? param1DynamicOps.getList(param1T) : param1DynamicOps.getMap(param1T));
/* 435 */           return dataResult.map(param1Object2 -> Pair.of(param1Supplier.get(), param1Object1));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public <T> DataResult<T> encode(A param1A, DynamicOps<T> param1DynamicOps, T param1T) {
/* 441 */           return param1DynamicOps.mergeToMap(param1T, MapLike.empty());
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 446 */           return "Unit[" + String.valueOf(value.get()) + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public abstract <T> Stream<T> keys(DynamicOps<T> paramDynamicOps);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */