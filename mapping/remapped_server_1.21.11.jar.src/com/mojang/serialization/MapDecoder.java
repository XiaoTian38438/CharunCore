/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface MapDecoder<A>
/*     */   extends Keyable
/*     */ {
/*     */   default <T> DataResult<A> compressedDecode(DynamicOps<T> paramDynamicOps, T paramT) {
/*  20 */     if (paramDynamicOps.compressMaps()) {
/*  21 */       Optional<Consumer<Consumer>> optional = paramDynamicOps.getList(paramT).result();
/*     */       
/*  23 */       if (!optional.isPresent()) {
/*  24 */         return DataResult.error(() -> "Input is not a list");
/*     */       }
/*     */       
/*  27 */       final KeyCompressor<T> compressor = compressor(paramDynamicOps);
/*  28 */       final ArrayList entries = new ArrayList();
/*  29 */       Objects.requireNonNull(arrayList); ((Consumer<Consumer>)optional.get()).accept(arrayList::add);
/*     */       
/*  31 */       MapLike<T> mapLike = new MapLike<T>()
/*     */         {
/*     */           @Nullable
/*     */           public T get(T param1T) {
/*  35 */             return entries.get(compressor.compress(param1T));
/*     */           }
/*     */ 
/*     */           
/*     */           @Nullable
/*     */           public T get(String param1String) {
/*  41 */             return entries.get(compressor.compress(param1String));
/*     */           }
/*     */ 
/*     */           
/*     */           public Stream<Pair<T, T>> entries() {
/*  46 */             return IntStream.range(0, entries.size()).<Pair<T, T>>mapToObj(param1Int -> Pair.of(param1KeyCompressor.decompress(param1Int), param1List.get(param1Int))).filter(param1Pair -> (param1Pair.getSecond() != null));
/*     */           }
/*     */         };
/*  49 */       return decode(paramDynamicOps, mapLike);
/*     */     } 
/*     */     
/*  52 */     return paramDynamicOps.getMap(paramT).setLifecycle(Lifecycle.stable()).flatMap(paramMapLike -> decode(paramDynamicOps, paramMapLike));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default Decoder<A> decoder() {
/*  58 */     return new Decoder<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*  61 */           return MapDecoder.this.compressedDecode(param1DynamicOps, param1T).map(param1Object2 -> Pair.of(param1Object2, param1Object1));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  66 */           return MapDecoder.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <B> MapDecoder<B> flatMap(final Function<? super A, ? extends DataResult<? extends B>> function) {
/*  72 */     return new Implementation<B>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*  75 */           return MapDecoder.this.keys(param1DynamicOps);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<B> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/*  80 */           return MapDecoder.this.decode(param1DynamicOps, param1MapLike).flatMap(param1Object -> ((DataResult)param1Function.apply(param1Object)).map(Function.identity()));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  85 */           return MapDecoder.this.toString() + "[flatMapped]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <B> MapDecoder<B> map(final Function<? super A, ? extends B> function) {
/*  91 */     return new Implementation<B>()
/*     */       {
/*     */         public <T> DataResult<B> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/*  94 */           return MapDecoder.this.decode(param1DynamicOps, param1MapLike).map(function);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/*  99 */           return MapDecoder.this.keys(param1DynamicOps);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 104 */           return MapDecoder.this.toString() + "[mapped]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <E> MapDecoder<E> ap(final MapDecoder<Function<? super A, ? extends E>> decoder) {
/* 110 */     return new Implementation<E>()
/*     */       {
/*     */         public <T> DataResult<E> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/* 113 */           return MapDecoder.this.decode(param1DynamicOps, param1MapLike).flatMap(param1Object -> param1MapDecoder.decode(param1DynamicOps, param1MapLike).map(()));
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 120 */           return Stream.concat(MapDecoder.this.keys(param1DynamicOps), decoder.keys(param1DynamicOps));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 125 */           return decoder.toString() + " * " + decoder.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default MapDecoder<A> withLifecycle(final Lifecycle lifecycle) {
/* 131 */     return new Implementation<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 134 */           return MapDecoder.this.keys(param1DynamicOps);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/* 139 */           return MapDecoder.this.<T>decode(param1DynamicOps, param1MapLike).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 144 */           return MapDecoder.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   <T> DataResult<A> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike);
/*     */   
/*     */   <T> KeyCompressor<T> compressor(DynamicOps<T> paramDynamicOps);
/*     */   
/*     */   public static abstract class Implementation<A> extends CompressorHolder implements MapDecoder<A> {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapDecoder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */