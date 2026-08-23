/*     */ package com.mojang.serialization.codecs;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapDecoder;
/*     */ import com.mojang.serialization.MapEncoder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public final class RecordCodecBuilder<O, F>
/*     */   implements App<RecordCodecBuilder.Mu<O>, F>
/*     */ {
/*     */   private final Function<O, F> getter;
/*     */   private final Function<O, MapEncoder<F>> encoder;
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> unbox(App<Mu<O>, F> paramApp) {
/*  30 */     return (RecordCodecBuilder)paramApp;
/*     */   }
/*     */   
/*     */   private final MapDecoder<F> decoder;
/*     */   
/*     */   public static final class Mu<O> implements K1 {}
/*     */   
/*     */   private RecordCodecBuilder(Function<O, F> paramFunction, Function<O, MapEncoder<F>> paramFunction1, MapDecoder<F> paramMapDecoder) {
/*  38 */     this.getter = paramFunction;
/*  39 */     this.encoder = paramFunction1;
/*  40 */     this.decoder = paramMapDecoder;
/*     */   }
/*     */   
/*     */   public static <O> Instance<O> instance() {
/*  44 */     return new Instance<>();
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> of(Function<O, F> paramFunction, String paramString, Codec<F> paramCodec) {
/*  48 */     return of(paramFunction, paramCodec.fieldOf(paramString));
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> of(Function<O, F> paramFunction, MapCodec<F> paramMapCodec) {
/*  52 */     return new RecordCodecBuilder<>(paramFunction, paramObject -> paramMapCodec, (MapDecoder<F>)paramMapCodec);
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> point(F paramF) {
/*  56 */     return new RecordCodecBuilder<>(paramObject2 -> paramObject1, paramObject -> Encoder.empty(), Decoder.unit(paramF));
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> stable(F paramF) {
/*  60 */     return point(paramF, Lifecycle.stable());
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> deprecated(F paramF, int paramInt) {
/*  64 */     return point(paramF, Lifecycle.deprecated(paramInt));
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> point(F paramF, Lifecycle paramLifecycle) {
/*  68 */     return new RecordCodecBuilder<>(paramObject2 -> paramObject1, paramObject -> Encoder.empty().withLifecycle(paramLifecycle), Decoder.unit(paramF).withLifecycle(paramLifecycle));
/*     */   }
/*     */   
/*     */   public static <O> Codec<O> create(Function<Instance<O>, ? extends App<Mu<O>, O>> paramFunction) {
/*  72 */     return build(paramFunction.apply(instance())).codec();
/*     */   }
/*     */   
/*     */   public static <O> MapCodec<O> mapCodec(Function<Instance<O>, ? extends App<Mu<O>, O>> paramFunction) {
/*  76 */     return build(paramFunction.apply(instance()));
/*     */   }
/*     */   
/*     */   public <E> RecordCodecBuilder<O, E> dependent(Function<O, E> paramFunction, final MapEncoder<E> encoder, final Function<? super F, ? extends MapDecoder<E>> decoderGetter) {
/*  80 */     return new RecordCodecBuilder(paramFunction, paramObject -> paramMapEncoder, (MapDecoder<F>)new MapDecoder.Implementation<E>()
/*     */         {
/*     */ 
/*     */           
/*     */           public <T> DataResult<E> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike)
/*     */           {
/*  86 */             return RecordCodecBuilder.this.decoder.decode(param1DynamicOps, param1MapLike).map(decoderGetter).flatMap(param1MapDecoder -> param1MapDecoder.decode(param1DynamicOps, param1MapLike).map(Function.identity()));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*  91 */             return encoder.keys(param1DynamicOps);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/*  96 */             return "Dependent[" + String.valueOf(encoder) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static <O> MapCodec<O> build(App<Mu<O>, O> paramApp) {
/* 103 */     final RecordCodecBuilder<O, O> builder = unbox(paramApp);
/* 104 */     return new MapCodec<O>()
/*     */       {
/*     */         public <T> DataResult<O> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/* 107 */           return builder.decoder.decode(param1DynamicOps, param1MapLike);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(O param1O, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/* 112 */           return ((MapEncoder)builder.encoder.apply(param1O)).encode(param1O, param1DynamicOps, param1RecordBuilder);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 117 */           return builder.decoder.keys(param1DynamicOps);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 122 */           return "RecordCodec[" + String.valueOf(builder.decoder) + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static final class Instance<O> implements Applicative<Mu<O>, Instance.Mu<O>> {
/*     */     private static final class Mu<O> implements Applicative.Mu {}
/*     */     
/*     */     public <A> App<RecordCodecBuilder.Mu<O>, A> stable(A param1A) {
/* 131 */       return RecordCodecBuilder.stable(param1A);
/*     */     }
/*     */     
/*     */     public <A> App<RecordCodecBuilder.Mu<O>, A> deprecated(A param1A, int param1Int) {
/* 135 */       return RecordCodecBuilder.deprecated(param1A, param1Int);
/*     */     }
/*     */     
/*     */     public <A> App<RecordCodecBuilder.Mu<O>, A> point(A param1A, Lifecycle param1Lifecycle) {
/* 139 */       return RecordCodecBuilder.point(param1A, param1Lifecycle);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> App<RecordCodecBuilder.Mu<O>, A> point(A param1A) {
/* 144 */       return RecordCodecBuilder.point(param1A);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, R> Function<App<RecordCodecBuilder.Mu<O>, A>, App<RecordCodecBuilder.Mu<O>, R>> lift1(App<RecordCodecBuilder.Mu<O>, Function<A, R>> param1App) {
/* 149 */       return param1App2 -> {
/*     */           final RecordCodecBuilder<?, ?> f = RecordCodecBuilder.unbox(param1App1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           final RecordCodecBuilder<?, ?> a = RecordCodecBuilder.unbox(param1App2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return new RecordCodecBuilder<>((), (), (MapDecoder<R>)new MapDecoder.Implementation<R>()
/*     */               {
/*     */                 public <T> DataResult<R> decode(DynamicOps<T> param2DynamicOps, MapLike<T> param2MapLike)
/*     */                 {
/* 183 */                   return a.decoder.decode(param2DynamicOps, param2MapLike).flatMap(param2Object -> param2RecordCodecBuilder.decoder.decode(param2DynamicOps, param2MapLike).map(()));
/*     */                 }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*     */                 public <T> Stream<T> keys(DynamicOps<T> param2DynamicOps) {
/* 192 */                   return Stream.concat(a.decoder.keys(param2DynamicOps), f.decoder.keys(param2DynamicOps));
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 197 */                   return String.valueOf(f.decoder) + " * " + String.valueOf(f.decoder);
/*     */                 }
/*     */               });
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <A, B, R> App<RecordCodecBuilder.Mu<O>, R> ap2(App<RecordCodecBuilder.Mu<O>, BiFunction<A, B, R>> param1App, App<RecordCodecBuilder.Mu<O>, A> param1App1, App<RecordCodecBuilder.Mu<O>, B> param1App2) {
/* 206 */       final RecordCodecBuilder<O, BiFunction<A, B, R>> function = RecordCodecBuilder.unbox(param1App);
/* 207 */       final RecordCodecBuilder<O, A> fa = RecordCodecBuilder.unbox(param1App1);
/* 208 */       final RecordCodecBuilder<O, B> fb = RecordCodecBuilder.unbox(param1App2);
/*     */       
/* 210 */       return new RecordCodecBuilder<>(param1Object -> ((BiFunction)param1RecordCodecBuilder1.getter.apply(param1Object)).apply(param1RecordCodecBuilder2.getter.apply(param1Object), param1RecordCodecBuilder3.getter.apply(param1Object)), param1Object -> {
/*     */             final MapEncoder fEncoder = param1RecordCodecBuilder1.encoder.apply(param1Object);
/*     */             
/*     */             final MapEncoder aEncoder = param1RecordCodecBuilder2.encoder.apply(param1Object);
/*     */             
/*     */             final Object aFromO = param1RecordCodecBuilder2.getter.apply(param1Object);
/*     */             final MapEncoder bEncoder = param1RecordCodecBuilder3.encoder.apply(param1Object);
/*     */             final Object bFromO = param1RecordCodecBuilder3.getter.apply(param1Object);
/*     */             return (MapEncoder)new MapEncoder.Implementation<R>()
/*     */               {
/*     */                 public <T> RecordBuilder<T> encode(R param2R, DynamicOps<T> param2DynamicOps, RecordBuilder<T> param2RecordBuilder)
/*     */                 {
/* 222 */                   fEncoder.encode((param2Object2, param2Object3) -> param2Object1, param2DynamicOps, param2RecordBuilder);
/* 223 */                   aEncoder.encode(aFromO, param2DynamicOps, param2RecordBuilder);
/* 224 */                   bEncoder.encode(bFromO, param2DynamicOps, param2RecordBuilder);
/* 225 */                   return param2RecordBuilder;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public <T> Stream<T> keys(DynamicOps<T> param2DynamicOps) {
/* 230 */                   return Stream.<Stream>of(new Stream[] { this.val$fEncoder
/* 231 */                         .keys(param2DynamicOps), this.val$aEncoder
/* 232 */                         .keys(param2DynamicOps), this.val$bEncoder
/* 233 */                         .keys(param2DynamicOps)
/* 234 */                       }).flatMap(Function.identity());
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 239 */                   return String.valueOf(fEncoder) + " * " + String.valueOf(fEncoder) + " * " + String.valueOf(aEncoder);
/*     */                 }
/*     */               };
/*     */           }(MapDecoder<R>)new MapDecoder.Implementation<R>()
/*     */           {
/*     */             public <T> DataResult<R> decode(DynamicOps<T> param2DynamicOps, MapLike<T> param2MapLike)
/*     */             {
/* 246 */               return DataResult.unbox(DataResult.instance().ap2((App)function.decoder
/* 247 */                     .decode(param2DynamicOps, param2MapLike), (App)fa.decoder
/* 248 */                     .decode(param2DynamicOps, param2MapLike), (App)fb.decoder
/* 249 */                     .decode(param2DynamicOps, param2MapLike)));
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public <T> Stream<T> keys(DynamicOps<T> param2DynamicOps) {
/* 255 */               return Stream.<Stream>of(new Stream[] { this.val$function.decoder
/* 256 */                     .keys(param2DynamicOps), this.val$fa.decoder
/* 257 */                     .keys(param2DynamicOps), this.val$fb.decoder
/* 258 */                     .keys(param2DynamicOps)
/* 259 */                   }).flatMap(Function.identity());
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 264 */               return String.valueOf(function.decoder) + " * " + String.valueOf(function.decoder) + " * " + String.valueOf(fa.decoder);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T1, T2, T3, R> App<RecordCodecBuilder.Mu<O>, R> ap3(App<RecordCodecBuilder.Mu<O>, Function3<T1, T2, T3, R>> param1App, App<RecordCodecBuilder.Mu<O>, T1> param1App1, App<RecordCodecBuilder.Mu<O>, T2> param1App2, App<RecordCodecBuilder.Mu<O>, T3> param1App3) {
/* 272 */       final RecordCodecBuilder<O, Function3<T1, T2, T3, R>> function = RecordCodecBuilder.unbox(param1App);
/* 273 */       final RecordCodecBuilder<O, T1> f1 = RecordCodecBuilder.unbox(param1App1);
/* 274 */       final RecordCodecBuilder<O, T2> f2 = RecordCodecBuilder.unbox(param1App2);
/* 275 */       final RecordCodecBuilder<O, T3> f3 = RecordCodecBuilder.unbox(param1App3);
/*     */       
/* 277 */       return new RecordCodecBuilder<>(param1Object -> ((Function3)param1RecordCodecBuilder1.getter.apply(param1Object)).apply(param1RecordCodecBuilder2.getter.apply(param1Object), param1RecordCodecBuilder3.getter.apply(param1Object), param1RecordCodecBuilder4.getter.apply(param1Object)), param1Object -> {
/*     */             final MapEncoder fEncoder = param1RecordCodecBuilder1.encoder.apply(param1Object);
/*     */             
/*     */             final MapEncoder e1 = param1RecordCodecBuilder2.encoder.apply(param1Object);
/*     */             
/*     */             final Object v1 = param1RecordCodecBuilder2.getter.apply(param1Object);
/*     */             
/*     */             final MapEncoder e2 = param1RecordCodecBuilder3.encoder.apply(param1Object);
/*     */             
/*     */             final Object v2 = param1RecordCodecBuilder3.getter.apply(param1Object);
/*     */             
/*     */             final MapEncoder e3 = param1RecordCodecBuilder4.encoder.apply(param1Object);
/*     */             
/*     */             final Object v3 = param1RecordCodecBuilder4.getter.apply(param1Object);
/*     */             return (MapEncoder)new MapEncoder.Implementation<R>()
/*     */               {
/*     */                 public <T> RecordBuilder<T> encode(R param2R, DynamicOps<T> param2DynamicOps, RecordBuilder<T> param2RecordBuilder)
/*     */                 {
/* 295 */                   fEncoder.encode((param2Object2, param2Object3, param2Object4) -> param2Object1, param2DynamicOps, param2RecordBuilder);
/* 296 */                   e1.encode(v1, param2DynamicOps, param2RecordBuilder);
/* 297 */                   e2.encode(v2, param2DynamicOps, param2RecordBuilder);
/* 298 */                   e3.encode(v3, param2DynamicOps, param2RecordBuilder);
/* 299 */                   return param2RecordBuilder;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public <T> Stream<T> keys(DynamicOps<T> param2DynamicOps) {
/* 304 */                   return Stream.<Stream>of(new Stream[] { this.val$fEncoder
/* 305 */                         .keys(param2DynamicOps), this.val$e1
/* 306 */                         .keys(param2DynamicOps), this.val$e2
/* 307 */                         .keys(param2DynamicOps), this.val$e3
/* 308 */                         .keys(param2DynamicOps)
/* 309 */                       }).flatMap(Function.identity());
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 314 */                   return String.valueOf(fEncoder) + " * " + String.valueOf(fEncoder) + " * " + String.valueOf(e1) + " * " + String.valueOf(e2);
/*     */                 }
/*     */               };
/*     */           }(MapDecoder<R>)new MapDecoder.Implementation<R>()
/*     */           {
/*     */             public <T> DataResult<R> decode(DynamicOps<T> param2DynamicOps, MapLike<T> param2MapLike)
/*     */             {
/* 321 */               return DataResult.unbox(DataResult.instance().ap3((App)function.decoder
/* 322 */                     .decode(param2DynamicOps, param2MapLike), (App)f1.decoder
/* 323 */                     .decode(param2DynamicOps, param2MapLike), (App)f2.decoder
/* 324 */                     .decode(param2DynamicOps, param2MapLike), (App)f3.decoder
/* 325 */                     .decode(param2DynamicOps, param2MapLike)));
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public <T> Stream<T> keys(DynamicOps<T> param2DynamicOps) {
/* 331 */               return Stream.<Stream>of(new Stream[] { this.val$function.decoder
/* 332 */                     .keys(param2DynamicOps), this.val$f1.decoder
/* 333 */                     .keys(param2DynamicOps), this.val$f2.decoder
/* 334 */                     .keys(param2DynamicOps), this.val$f3.decoder
/* 335 */                     .keys(param2DynamicOps)
/* 336 */                   }).flatMap(Function.identity());
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 341 */               return String.valueOf(function.decoder) + " * " + String.valueOf(function.decoder) + " * " + String.valueOf(f1.decoder) + " * " + String.valueOf(f2.decoder);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T1, T2, T3, T4, R> App<RecordCodecBuilder.Mu<O>, R> ap4(App<RecordCodecBuilder.Mu<O>, Function4<T1, T2, T3, T4, R>> param1App, App<RecordCodecBuilder.Mu<O>, T1> param1App1, App<RecordCodecBuilder.Mu<O>, T2> param1App2, App<RecordCodecBuilder.Mu<O>, T3> param1App3, App<RecordCodecBuilder.Mu<O>, T4> param1App4) {
/* 349 */       final RecordCodecBuilder<O, Function4<T1, T2, T3, T4, R>> function = RecordCodecBuilder.unbox(param1App);
/* 350 */       final RecordCodecBuilder<O, T1> f1 = RecordCodecBuilder.unbox(param1App1);
/* 351 */       final RecordCodecBuilder<O, T2> f2 = RecordCodecBuilder.unbox(param1App2);
/* 352 */       final RecordCodecBuilder<O, T3> f3 = RecordCodecBuilder.unbox(param1App3);
/* 353 */       final RecordCodecBuilder<O, T4> f4 = RecordCodecBuilder.unbox(param1App4);
/*     */       
/* 355 */       return new RecordCodecBuilder<>(param1Object -> ((Function4)param1RecordCodecBuilder1.getter.apply(param1Object)).apply(param1RecordCodecBuilder2.getter.apply(param1Object), param1RecordCodecBuilder3.getter.apply(param1Object), param1RecordCodecBuilder4.getter.apply(param1Object), param1RecordCodecBuilder5.getter.apply(param1Object)), param1Object -> {
/*     */             final MapEncoder fEncoder = param1RecordCodecBuilder1.encoder.apply(param1Object);
/*     */             
/*     */             final MapEncoder e1 = param1RecordCodecBuilder2.encoder.apply(param1Object);
/*     */             
/*     */             final Object v1 = param1RecordCodecBuilder2.getter.apply(param1Object);
/*     */             
/*     */             final MapEncoder e2 = param1RecordCodecBuilder3.encoder.apply(param1Object);
/*     */             
/*     */             final Object v2 = param1RecordCodecBuilder3.getter.apply(param1Object);
/*     */             
/*     */             final MapEncoder e3 = param1RecordCodecBuilder4.encoder.apply(param1Object);
/*     */             
/*     */             final Object v3 = param1RecordCodecBuilder4.getter.apply(param1Object);
/*     */             
/*     */             final MapEncoder e4 = param1RecordCodecBuilder5.encoder.apply(param1Object);
/*     */             final Object v4 = param1RecordCodecBuilder5.getter.apply(param1Object);
/*     */             return (MapEncoder)new MapEncoder.Implementation<R>()
/*     */               {
/*     */                 public <T> RecordBuilder<T> encode(R param2R, DynamicOps<T> param2DynamicOps, RecordBuilder<T> param2RecordBuilder)
/*     */                 {
/* 376 */                   fEncoder.encode((param2Object2, param2Object3, param2Object4, param2Object5) -> param2Object1, param2DynamicOps, param2RecordBuilder);
/* 377 */                   e1.encode(v1, param2DynamicOps, param2RecordBuilder);
/* 378 */                   e2.encode(v2, param2DynamicOps, param2RecordBuilder);
/* 379 */                   e3.encode(v3, param2DynamicOps, param2RecordBuilder);
/* 380 */                   e4.encode(v4, param2DynamicOps, param2RecordBuilder);
/* 381 */                   return param2RecordBuilder;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public <T> Stream<T> keys(DynamicOps<T> param2DynamicOps) {
/* 386 */                   return Stream.<Stream>of(new Stream[] { this.val$fEncoder
/* 387 */                         .keys(param2DynamicOps), this.val$e1
/* 388 */                         .keys(param2DynamicOps), this.val$e2
/* 389 */                         .keys(param2DynamicOps), this.val$e3
/* 390 */                         .keys(param2DynamicOps), this.val$e4
/* 391 */                         .keys(param2DynamicOps)
/* 392 */                       }).flatMap(Function.identity());
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 397 */                   return String.valueOf(fEncoder) + " * " + String.valueOf(fEncoder) + " * " + String.valueOf(e1) + " * " + String.valueOf(e2) + " * " + String.valueOf(e3);
/*     */                 }
/*     */               };
/*     */           }(MapDecoder<R>)new MapDecoder.Implementation<R>()
/*     */           {
/*     */             public <T> DataResult<R> decode(DynamicOps<T> param2DynamicOps, MapLike<T> param2MapLike)
/*     */             {
/* 404 */               return DataResult.unbox(DataResult.instance().ap4((App)function.decoder
/* 405 */                     .decode(param2DynamicOps, param2MapLike), (App)f1.decoder
/* 406 */                     .decode(param2DynamicOps, param2MapLike), (App)f2.decoder
/* 407 */                     .decode(param2DynamicOps, param2MapLike), (App)f3.decoder
/* 408 */                     .decode(param2DynamicOps, param2MapLike), (App)f4.decoder
/* 409 */                     .decode(param2DynamicOps, param2MapLike)));
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public <T> Stream<T> keys(DynamicOps<T> param2DynamicOps) {
/* 415 */               return Stream.<Stream>of(new Stream[] { this.val$function.decoder
/* 416 */                     .keys(param2DynamicOps), this.val$f1.decoder
/* 417 */                     .keys(param2DynamicOps), this.val$f2.decoder
/* 418 */                     .keys(param2DynamicOps), this.val$f3.decoder
/* 419 */                     .keys(param2DynamicOps), this.val$f4.decoder
/* 420 */                     .keys(param2DynamicOps)
/* 421 */                   }).flatMap(Function.identity());
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 426 */               return String.valueOf(function.decoder) + " * " + String.valueOf(function.decoder) + " * " + String.valueOf(f1.decoder) + " * " + String.valueOf(f2.decoder) + " * " + String.valueOf(f3.decoder);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T, R> App<RecordCodecBuilder.Mu<O>, R> map(final Function<? super T, ? extends R> getter, App<RecordCodecBuilder.Mu<O>, T> param1App) {
/* 434 */       RecordCodecBuilder<O, T> recordCodecBuilder = RecordCodecBuilder.unbox(param1App);
/* 435 */       Function<O, T> function = recordCodecBuilder.getter;
/* 436 */       return new RecordCodecBuilder<>(function
/* 437 */           .andThen(getter), param1Object -> new MapEncoder.Implementation<R>()
/*     */           {
/* 439 */             private final MapEncoder<T> encoder = unbox.encoder.apply(o);
/*     */ 
/*     */             
/*     */             public <U> RecordBuilder<U> encode(R param2R, DynamicOps<U> param2DynamicOps, RecordBuilder<U> param2RecordBuilder) {
/* 443 */               return this.encoder.encode(getter.apply(o), param2DynamicOps, param2RecordBuilder);
/*     */             }
/*     */ 
/*     */             
/*     */             public <U> Stream<U> keys(DynamicOps<U> param2DynamicOps) {
/* 448 */               return this.encoder.keys(param2DynamicOps);
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 453 */               return String.valueOf(this.encoder) + "[mapped]";
/*     */             }
/* 456 */           }recordCodecBuilder.decoder.map(getter));
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Mu<O> implements Applicative.Mu {}
/*     */   
/*     */   class null extends MapEncoder.Implementation<R> {
/*     */     public <T> RecordBuilder<T> encode(R param1R, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/*     */       fEnc.encode(param1Object2 -> param1Object1, param1DynamicOps, param1RecordBuilder);
/*     */       aEnc.encode(aFromO, param1DynamicOps, param1RecordBuilder);
/*     */       return param1RecordBuilder;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*     */       return Stream.concat(aEnc.keys(param1DynamicOps), fEnc.keys(param1DynamicOps));
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return String.valueOf(fEnc) + " * " + String.valueOf(fEnc);
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends MapDecoder.Implementation<R> {
/*     */     public <T> DataResult<R> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/*     */       return a.decoder.decode(param1DynamicOps, param1MapLike).flatMap(param1Object -> param1RecordCodecBuilder.decoder.decode(param1DynamicOps, param1MapLike).map(()));
/*     */     }
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*     */       return Stream.concat(a.decoder.keys(param1DynamicOps), f.decoder.keys(param1DynamicOps));
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return String.valueOf(f.decoder) + " * " + String.valueOf(f.decoder);
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends MapEncoder.Implementation<R> {
/*     */     public <T> RecordBuilder<T> encode(R param1R, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/*     */       fEncoder.encode((param1Object2, param1Object3) -> param1Object1, param1DynamicOps, param1RecordBuilder);
/*     */       aEncoder.encode(aFromO, param1DynamicOps, param1RecordBuilder);
/*     */       bEncoder.encode(bFromO, param1DynamicOps, param1RecordBuilder);
/*     */       return param1RecordBuilder;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*     */       return Stream.<Stream>of(new Stream[] { this.val$fEncoder.keys(param1DynamicOps), this.val$aEncoder.keys(param1DynamicOps), this.val$bEncoder.keys(param1DynamicOps) }).flatMap(Function.identity());
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return String.valueOf(fEncoder) + " * " + String.valueOf(fEncoder) + " * " + String.valueOf(aEncoder);
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends MapDecoder.Implementation<R> {
/*     */     public <T> DataResult<R> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/*     */       return DataResult.unbox(DataResult.instance().ap2((App)function.decoder.decode(param1DynamicOps, param1MapLike), (App)fa.decoder.decode(param1DynamicOps, param1MapLike), (App)fb.decoder.decode(param1DynamicOps, param1MapLike)));
/*     */     }
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*     */       return Stream.<Stream>of(new Stream[] { this.val$function.decoder.keys(param1DynamicOps), this.val$fa.decoder.keys(param1DynamicOps), this.val$fb.decoder.keys(param1DynamicOps) }).flatMap(Function.identity());
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return String.valueOf(function.decoder) + " * " + String.valueOf(function.decoder) + " * " + String.valueOf(fa.decoder);
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends MapEncoder.Implementation<R> {
/*     */     public <T> RecordBuilder<T> encode(R param1R, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/*     */       fEncoder.encode((param1Object2, param1Object3, param1Object4) -> param1Object1, param1DynamicOps, param1RecordBuilder);
/*     */       e1.encode(v1, param1DynamicOps, param1RecordBuilder);
/*     */       e2.encode(v2, param1DynamicOps, param1RecordBuilder);
/*     */       e3.encode(v3, param1DynamicOps, param1RecordBuilder);
/*     */       return param1RecordBuilder;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*     */       return Stream.<Stream>of(new Stream[] { this.val$fEncoder.keys(param1DynamicOps), this.val$e1.keys(param1DynamicOps), this.val$e2.keys(param1DynamicOps), this.val$e3.keys(param1DynamicOps) }).flatMap(Function.identity());
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return String.valueOf(fEncoder) + " * " + String.valueOf(fEncoder) + " * " + String.valueOf(e1) + " * " + String.valueOf(e2);
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends MapDecoder.Implementation<R> {
/*     */     public <T> DataResult<R> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/*     */       return DataResult.unbox(DataResult.instance().ap3((App)function.decoder.decode(param1DynamicOps, param1MapLike), (App)f1.decoder.decode(param1DynamicOps, param1MapLike), (App)f2.decoder.decode(param1DynamicOps, param1MapLike), (App)f3.decoder.decode(param1DynamicOps, param1MapLike)));
/*     */     }
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*     */       return Stream.<Stream>of(new Stream[] { this.val$function.decoder.keys(param1DynamicOps), this.val$f1.decoder.keys(param1DynamicOps), this.val$f2.decoder.keys(param1DynamicOps), this.val$f3.decoder.keys(param1DynamicOps) }).flatMap(Function.identity());
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return String.valueOf(function.decoder) + " * " + String.valueOf(function.decoder) + " * " + String.valueOf(f1.decoder) + " * " + String.valueOf(f2.decoder);
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends MapEncoder.Implementation<R> {
/*     */     public <T> RecordBuilder<T> encode(R param1R, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/*     */       fEncoder.encode((param1Object2, param1Object3, param1Object4, param1Object5) -> param1Object1, param1DynamicOps, param1RecordBuilder);
/*     */       e1.encode(v1, param1DynamicOps, param1RecordBuilder);
/*     */       e2.encode(v2, param1DynamicOps, param1RecordBuilder);
/*     */       e3.encode(v3, param1DynamicOps, param1RecordBuilder);
/*     */       e4.encode(v4, param1DynamicOps, param1RecordBuilder);
/*     */       return param1RecordBuilder;
/*     */     }
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*     */       return Stream.<Stream>of(new Stream[] { this.val$fEncoder.keys(param1DynamicOps), this.val$e1.keys(param1DynamicOps), this.val$e2.keys(param1DynamicOps), this.val$e3.keys(param1DynamicOps), this.val$e4.keys(param1DynamicOps) }).flatMap(Function.identity());
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return String.valueOf(fEncoder) + " * " + String.valueOf(fEncoder) + " * " + String.valueOf(e1) + " * " + String.valueOf(e2) + " * " + String.valueOf(e3);
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends MapDecoder.Implementation<R> {
/*     */     public <T> DataResult<R> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/*     */       return DataResult.unbox(DataResult.instance().ap4((App)function.decoder.decode(param1DynamicOps, param1MapLike), (App)f1.decoder.decode(param1DynamicOps, param1MapLike), (App)f2.decoder.decode(param1DynamicOps, param1MapLike), (App)f3.decoder.decode(param1DynamicOps, param1MapLike), (App)f4.decoder.decode(param1DynamicOps, param1MapLike)));
/*     */     }
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*     */       return Stream.<Stream>of(new Stream[] { this.val$function.decoder.keys(param1DynamicOps), this.val$f1.decoder.keys(param1DynamicOps), this.val$f2.decoder.keys(param1DynamicOps), this.val$f3.decoder.keys(param1DynamicOps), this.val$f4.decoder.keys(param1DynamicOps) }).flatMap(Function.identity());
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return String.valueOf(function.decoder) + " * " + String.valueOf(function.decoder) + " * " + String.valueOf(f1.decoder) + " * " + String.valueOf(f2.decoder) + " * " + String.valueOf(f3.decoder);
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends MapEncoder.Implementation<R> {
/*     */     private final MapEncoder<T> encoder;
/*     */     
/*     */     null() {
/*     */       this.encoder = unbox.encoder.apply(o);
/*     */     }
/*     */     
/*     */     public <U> RecordBuilder<U> encode(R param1R, DynamicOps<U> param1DynamicOps, RecordBuilder<U> param1RecordBuilder) {
/*     */       return this.encoder.encode(getter.apply(o), param1DynamicOps, param1RecordBuilder);
/*     */     }
/*     */     
/*     */     public <U> Stream<U> keys(DynamicOps<U> param1DynamicOps) {
/*     */       return this.encoder.keys(param1DynamicOps);
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return String.valueOf(this.encoder) + "[mapped]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\RecordCodecBuilder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */