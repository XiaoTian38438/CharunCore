/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.kinds.ListBox;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DynamicLike<T>
/*     */ {
/*     */   protected final DynamicOps<T> ops;
/*     */   
/*     */   public DynamicLike(DynamicOps<T> paramDynamicOps) {
/*  27 */     this.ops = paramDynamicOps;
/*     */   }
/*     */   
/*     */   public DynamicOps<T> getOps() {
/*  31 */     return this.ops;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> DataResult<List<U>> asListOpt(Function<Dynamic<T>, U> paramFunction) {
/*  50 */     return asStreamOpt().map(paramStream -> (List)paramStream.map(paramFunction).collect(Collectors.toList()));
/*     */   }
/*     */   
/*     */   public <K, V> DataResult<Map<K, V>> asMapOpt(Function<Dynamic<T>, K> paramFunction, Function<Dynamic<T>, V> paramFunction1) {
/*  54 */     return asMapOpt().map(paramStream -> {
/*     */           ImmutableMap.Builder builder = ImmutableMap.builder();
/*     */           paramStream.forEach(());
/*     */           return (Map)builder.build();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A> DataResult<A> read(Decoder<? extends A> paramDecoder) {
/*  64 */     return decode(paramDecoder).map(Pair::getFirst);
/*     */   }
/*     */   
/*     */   public <E> DataResult<List<E>> readList(Decoder<E> paramDecoder) {
/*  68 */     return asStreamOpt()
/*  69 */       .map(paramStream -> (List)paramStream.map(()).collect(Collectors.toList()))
/*  70 */       .flatMap(paramList -> DataResult.unbox(ListBox.flip(DataResult.instance(), paramList)));
/*     */   }
/*     */   
/*     */   public <E> DataResult<List<E>> readList(Function<? super Dynamic<?>, ? extends DataResult<? extends E>> paramFunction) {
/*  74 */     return asStreamOpt()
/*  75 */       .map(paramStream -> (List)paramStream.map(paramFunction).map(()).collect(Collectors.toList()))
/*  76 */       .flatMap(paramList -> DataResult.unbox(ListBox.flip(DataResult.instance(), paramList)));
/*     */   }
/*     */   
/*     */   public <K, V> DataResult<List<Pair<K, V>>> readMap(Decoder<K> paramDecoder, Decoder<V> paramDecoder1) {
/*  80 */     return asMapOpt()
/*  81 */       .map(paramStream -> (List)paramStream.map(()).collect(Collectors.toList()))
/*  82 */       .flatMap(paramList -> DataResult.unbox(ListBox.flip(DataResult.instance(), paramList)));
/*     */   }
/*     */   
/*     */   public <K, V> DataResult<List<Pair<K, V>>> readMap(Decoder<K> paramDecoder, Function<K, Decoder<V>> paramFunction) {
/*  86 */     return asMapOpt()
/*  87 */       .map(paramStream -> (List)paramStream.map(()).collect(Collectors.toList()))
/*  88 */       .flatMap(paramList -> DataResult.unbox(ListBox.flip(DataResult.instance(), paramList)));
/*     */   }
/*     */   
/*     */   public <R> DataResult<R> readMap(DataResult<R> paramDataResult, Function3<R, Dynamic<T>, Dynamic<T>, DataResult<R>> paramFunction3) {
/*  92 */     return asMapOpt().flatMap(paramStream -> {
/*     */           AtomicReference<DataResult> atomicReference = new AtomicReference<>(paramDataResult);
/*     */           paramStream.forEach(());
/*     */           return atomicReference.getPlain();
/*     */         });
/*     */   }
/*     */   
/*     */   public Number asNumber(Number paramNumber) {
/* 100 */     return asNumber().result().orElse(paramNumber);
/*     */   }
/*     */   
/*     */   public int asInt(int paramInt) {
/* 104 */     return asNumber(Integer.valueOf(paramInt)).intValue();
/*     */   }
/*     */   
/*     */   public long asLong(long paramLong) {
/* 108 */     return asNumber(Long.valueOf(paramLong)).longValue();
/*     */   }
/*     */   
/*     */   public float asFloat(float paramFloat) {
/* 112 */     return asNumber(Float.valueOf(paramFloat)).floatValue();
/*     */   }
/*     */   
/*     */   public double asDouble(double paramDouble) {
/* 116 */     return asNumber(Double.valueOf(paramDouble)).doubleValue();
/*     */   }
/*     */   
/*     */   public byte asByte(byte paramByte) {
/* 120 */     return asNumber(Byte.valueOf(paramByte)).byteValue();
/*     */   }
/*     */   
/*     */   public short asShort(short paramShort) {
/* 124 */     return asNumber(Short.valueOf(paramShort)).shortValue();
/*     */   }
/*     */   
/*     */   public boolean asBoolean(boolean paramBoolean) {
/* 128 */     return ((Boolean)asBoolean().result().orElse(Boolean.valueOf(paramBoolean))).booleanValue();
/*     */   }
/*     */   
/*     */   public String asString(String paramString) {
/* 132 */     return asString().result().orElse(paramString);
/*     */   }
/*     */   
/*     */   public Stream<Dynamic<T>> asStream() {
/* 136 */     return asStreamOpt().result().orElseGet(Stream::empty);
/*     */   }
/*     */   
/*     */   public ByteBuffer asByteBuffer() {
/* 140 */     return asByteBufferOpt().result().orElseGet(() -> ByteBuffer.wrap(new byte[0]));
/*     */   }
/*     */   
/*     */   public IntStream asIntStream() {
/* 144 */     return asIntStreamOpt().result().orElseGet(IntStream::empty);
/*     */   }
/*     */   
/*     */   public LongStream asLongStream() {
/* 148 */     return asLongStreamOpt().result().orElseGet(LongStream::empty);
/*     */   }
/*     */   
/*     */   public <U> List<U> asList(Function<Dynamic<T>, U> paramFunction) {
/* 152 */     return asListOpt(paramFunction).result().orElseGet(ImmutableList::of);
/*     */   }
/*     */   
/*     */   public <K, V> Map<K, V> asMap(Function<Dynamic<T>, K> paramFunction, Function<Dynamic<T>, V> paramFunction1) {
/* 156 */     return asMapOpt(paramFunction, paramFunction1).result().orElseGet(ImmutableMap::of);
/*     */   }
/*     */   
/*     */   public T getElement(String paramString, T paramT) {
/* 160 */     return getElement(paramString).result().orElse(paramT);
/*     */   }
/*     */   
/*     */   public T getElementGeneric(T paramT1, T paramT2) {
/* 164 */     return getElementGeneric(paramT1).result().orElse(paramT2);
/*     */   }
/*     */   
/*     */   public Dynamic<T> emptyList() {
/* 168 */     return new Dynamic<>(this.ops, this.ops.emptyList());
/*     */   }
/*     */   
/*     */   public Dynamic<T> emptyMap() {
/* 172 */     return new Dynamic<>(this.ops, this.ops.emptyMap());
/*     */   }
/*     */   
/*     */   public Dynamic<T> createNumeric(Number paramNumber) {
/* 176 */     return new Dynamic<>(this.ops, this.ops.createNumeric(paramNumber));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createByte(byte paramByte) {
/* 180 */     return new Dynamic<>(this.ops, this.ops.createByte(paramByte));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createShort(short paramShort) {
/* 184 */     return new Dynamic<>(this.ops, this.ops.createShort(paramShort));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createInt(int paramInt) {
/* 188 */     return new Dynamic<>(this.ops, this.ops.createInt(paramInt));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createLong(long paramLong) {
/* 192 */     return new Dynamic<>(this.ops, this.ops.createLong(paramLong));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createFloat(float paramFloat) {
/* 196 */     return new Dynamic<>(this.ops, this.ops.createFloat(paramFloat));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createDouble(double paramDouble) {
/* 200 */     return new Dynamic<>(this.ops, this.ops.createDouble(paramDouble));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createBoolean(boolean paramBoolean) {
/* 204 */     return new Dynamic<>(this.ops, this.ops.createBoolean(paramBoolean));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createString(String paramString) {
/* 208 */     return new Dynamic<>(this.ops, this.ops.createString(paramString));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createList(Stream<? extends Dynamic<?>> paramStream) {
/* 212 */     return new Dynamic<>(this.ops, this.ops.createList(paramStream.map(paramDynamic -> paramDynamic.cast(this.ops))));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createMap(Map<? extends Dynamic<?>, ? extends Dynamic<?>> paramMap) {
/* 216 */     ImmutableMap.Builder builder = ImmutableMap.builder();
/* 217 */     for (Map.Entry<? extends Dynamic<?>, ? extends Dynamic<?>> entry : paramMap.entrySet()) {
/* 218 */       builder.put(((Dynamic)entry.getKey()).cast(this.ops), ((Dynamic)entry.getValue()).cast(this.ops));
/*     */     }
/* 220 */     return new Dynamic<>(this.ops, this.ops.createMap((Map<T, T>)builder.build()));
/*     */   }
/*     */   
/*     */   public Dynamic<?> createByteList(ByteBuffer paramByteBuffer) {
/* 224 */     return new Dynamic(this.ops, this.ops.createByteList(paramByteBuffer));
/*     */   }
/*     */   
/*     */   public Dynamic<?> createIntList(IntStream paramIntStream) {
/* 228 */     return new Dynamic(this.ops, this.ops.createIntList(paramIntStream));
/*     */   }
/*     */   
/*     */   public Dynamic<?> createLongList(LongStream paramLongStream) {
/* 232 */     return new Dynamic(this.ops, this.ops.createLongList(paramLongStream));
/*     */   }
/*     */   
/*     */   public abstract DataResult<Number> asNumber();
/*     */   
/*     */   public abstract DataResult<String> asString();
/*     */   
/*     */   public abstract DataResult<Boolean> asBoolean();
/*     */   
/*     */   public abstract DataResult<Stream<Dynamic<T>>> asStreamOpt();
/*     */   
/*     */   public abstract DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt();
/*     */   
/*     */   public abstract DataResult<ByteBuffer> asByteBufferOpt();
/*     */   
/*     */   public abstract DataResult<IntStream> asIntStreamOpt();
/*     */   
/*     */   public abstract DataResult<LongStream> asLongStreamOpt();
/*     */   
/*     */   public abstract OptionalDynamic<T> get(String paramString);
/*     */   
/*     */   public abstract DataResult<T> getGeneric(T paramT);
/*     */   
/*     */   public abstract DataResult<T> getElement(String paramString);
/*     */   
/*     */   public abstract DataResult<T> getElementGeneric(T paramT);
/*     */   
/*     */   public abstract <A> DataResult<Pair<A, T>> decode(Decoder<? extends A> paramDecoder);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\DynamicLike.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */