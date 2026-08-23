/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
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
/*     */ public interface DynamicOps<T>
/*     */ {
/*     */   default T emptyMap() {
/*  26 */     return createMap((Map<T, T>)ImmutableMap.of());
/*     */   }
/*     */   
/*     */   default T emptyList() {
/*  30 */     return createList(Stream.empty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Number getNumberValue(T paramT, Number paramNumber) {
/*  38 */     return getNumberValue(paramT).result().orElse(paramNumber);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default T createByte(byte paramByte) {
/*  44 */     return createNumeric(Byte.valueOf(paramByte));
/*     */   }
/*     */   
/*     */   default T createShort(short paramShort) {
/*  48 */     return createNumeric(Short.valueOf(paramShort));
/*     */   }
/*     */   
/*     */   default T createInt(int paramInt) {
/*  52 */     return createNumeric(Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   default T createLong(long paramLong) {
/*  56 */     return createNumeric(Long.valueOf(paramLong));
/*     */   }
/*     */   
/*     */   default T createFloat(float paramFloat) {
/*  60 */     return createNumeric(Float.valueOf(paramFloat));
/*     */   }
/*     */   
/*     */   default T createDouble(double paramDouble) {
/*  64 */     return createNumeric(Double.valueOf(paramDouble));
/*     */   }
/*     */   
/*     */   default DataResult<Boolean> getBooleanValue(T paramT) {
/*  68 */     return getNumberValue(paramT).map(paramNumber -> Boolean.valueOf((paramNumber.byteValue() != 0)));
/*     */   }
/*     */   
/*     */   default T createBoolean(boolean paramBoolean) {
/*  72 */     return createByte((byte)(paramBoolean ? 1 : 0));
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
/*     */   default DataResult<T> mergeToList(T paramT, List<T> paramList) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic success : (Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */     //   4: astore_3
/*     */     //   5: aload_2
/*     */     //   6: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   11: astore #4
/*     */     //   13: aload #4
/*     */     //   15: invokeinterface hasNext : ()Z
/*     */     //   20: ifeq -> 50
/*     */     //   23: aload #4
/*     */     //   25: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   30: astore #5
/*     */     //   32: aload_3
/*     */     //   33: aload_0
/*     */     //   34: aload #5
/*     */     //   36: <illegal opcode> apply : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Ljava/util/function/Function;
/*     */     //   41: invokeinterface flatMap : (Ljava/util/function/Function;)Lcom/mojang/serialization/DataResult;
/*     */     //   46: astore_3
/*     */     //   47: goto -> 13
/*     */     //   50: aload_3
/*     */     //   51: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #85	-> 0
/*     */     //   #87	-> 5
/*     */     //   #88	-> 32
/*     */     //   #89	-> 47
/*     */     //   #90	-> 50
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
/*     */   default DataResult<T> mergeToMap(T paramT, Map<T, T> paramMap) {
/*  99 */     return mergeToMap(paramT, MapLike.forMap(paramMap, this));
/*     */   }
/*     */   
/*     */   default DataResult<T> mergeToMap(T paramT, MapLike<T> paramMapLike) {
/* 103 */     AtomicReference<DataResult<T>> atomicReference = new AtomicReference(DataResult.success(paramT));
/*     */     
/* 105 */     paramMapLike.entries().forEach(paramPair -> paramAtomicReference.setPlain(((DataResult)paramAtomicReference.getPlain()).flatMap(())));
/*     */ 
/*     */     
/* 108 */     return atomicReference.getPlain();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default DataResult<T> mergeToPrimitive(T paramT1, T paramT2) {
/* 115 */     if (!Objects.equals(paramT1, empty())) {
/* 116 */       return DataResult.error(() -> "Do not know how to append a primitive value " + String.valueOf(paramObject1) + " to " + String.valueOf(paramObject2), paramT2);
/*     */     }
/* 118 */     return DataResult.success(paramT2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(T paramT) {
/* 124 */     return getMapValues(paramT).map(paramStream -> ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default DataResult<MapLike<T>> getMap(T paramT) {
/* 130 */     return getMapValues(paramT).flatMap(paramStream -> {
/*     */           try {
/*     */             return DataResult.success(MapLike.forMap((Map<?, ?>)paramStream.collect(Pair.toMap()), this));
/* 133 */           } catch (IllegalStateException illegalStateException) {
/*     */             return DataResult.error(());
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   default T createMap(Map<T, T> paramMap) {
/* 140 */     return createMap(paramMap.entrySet().stream().map(paramEntry -> Pair.of(paramEntry.getKey(), paramEntry.getValue())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default DataResult<Consumer<Consumer<T>>> getList(T paramT) {
/* 146 */     return getStream(paramT).map(paramStream -> {
/*     */           Objects.requireNonNull(paramStream);
/*     */           return paramStream::forEach;
/*     */         });
/*     */   }
/*     */   default DataResult<ByteBuffer> getByteBuffer(T paramT) {
/* 152 */     return getStream(paramT).flatMap(paramStream -> {
/*     */           List<T> list = (List)paramStream.collect(Collectors.toList());
/*     */           if (list.stream().allMatch(())) {
/*     */             ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[list.size()]);
/*     */             for (byte b = 0; b < list.size(); b++) {
/*     */               byteBuffer.put(b, ((Number)getNumberValue(list.get(b)).result().get()).byteValue());
/*     */             }
/*     */             return DataResult.success(byteBuffer);
/*     */           } 
/*     */           return DataResult.error(());
/*     */         });
/*     */   }
/*     */   
/*     */   default T createByteList(ByteBuffer paramByteBuffer) {
/* 166 */     return createList(IntStream.range(0, paramByteBuffer.capacity()).mapToObj(paramInt -> createByte(paramByteBuffer.get(paramInt))));
/*     */   }
/*     */   
/*     */   default DataResult<IntStream> getIntStream(T paramT) {
/* 170 */     return getStream(paramT).flatMap(paramStream -> {
/*     */           List list = paramStream.toList();
/*     */           return list.stream().allMatch(()) ? DataResult.success(list.stream().mapToInt(())) : DataResult.error(());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default T createIntList(IntStream paramIntStream) {
/* 180 */     return createList(paramIntStream.mapToObj(this::createInt));
/*     */   }
/*     */   
/*     */   default DataResult<LongStream> getLongStream(T paramT) {
/* 184 */     return getStream(paramT).flatMap(paramStream -> {
/*     */           List list = paramStream.toList();
/*     */           return list.stream().allMatch(()) ? DataResult.success(list.stream().mapToLong(())) : DataResult.error(());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default T createLongList(LongStream paramLongStream) {
/* 194 */     return createList(paramLongStream.mapToObj(this::createLong));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean compressMaps() {
/* 200 */     return false;
/*     */   }
/*     */   
/*     */   default DataResult<T> get(T paramT, String paramString) {
/* 204 */     return getGeneric(paramT, createString(paramString));
/*     */   }
/*     */   
/*     */   default DataResult<T> getGeneric(T paramT1, T paramT2) {
/* 208 */     return getMap(paramT1).flatMap(paramMapLike -> (DataResult)Optional.ofNullable(paramMapLike.get(paramObject1)).map(DataResult::success).orElseGet(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default T set(T paramT1, String paramString, T paramT2) {
/* 216 */     return mergeToMap(paramT1, createString(paramString), paramT2).result().orElse(paramT1);
/*     */   }
/*     */ 
/*     */   
/*     */   default T update(T paramT, String paramString, Function<T, T> paramFunction) {
/* 221 */     return get(paramT, paramString).<T>map(paramObject2 -> set((T)paramObject1, paramString, paramFunction.apply(paramObject2))).result().orElse(paramT);
/*     */   }
/*     */   
/*     */   default T updateGeneric(T paramT1, T paramT2, Function<T, T> paramFunction) {
/* 225 */     return getGeneric(paramT1, paramT2).<T>flatMap(paramObject3 -> mergeToMap((T)paramObject1, (T)paramObject2, paramFunction.apply(paramObject3))).result().orElse(paramT1);
/*     */   }
/*     */   
/*     */   default ListBuilder<T> listBuilder() {
/* 229 */     return new ListBuilder.Builder<>(this);
/*     */   }
/*     */   
/*     */   default RecordBuilder<T> mapBuilder() {
/* 233 */     return new RecordBuilder.MapBuilder<>(this);
/*     */   }
/*     */   
/*     */   default <E> Function<E, DataResult<T>> withEncoder(Encoder<E> paramEncoder) {
/* 237 */     return paramObject -> paramEncoder.encodeStart(this, paramObject);
/*     */   }
/*     */   
/*     */   default <E> Function<T, DataResult<Pair<E, T>>> withDecoder(Decoder<E> paramDecoder) {
/* 241 */     return paramObject -> paramDecoder.decode(this, paramObject);
/*     */   }
/*     */   
/*     */   default <E> Function<T, DataResult<E>> withParser(Decoder<E> paramDecoder) {
/* 245 */     return paramObject -> paramDecoder.parse(this, paramObject);
/*     */   }
/*     */   
/*     */   default <U> U convertList(DynamicOps<U> paramDynamicOps, T paramT) {
/* 249 */     return paramDynamicOps.createList(((Stream)getStream(paramT).result().orElse(Stream.empty())).map(paramObject -> convertTo(paramDynamicOps, paramObject)));
/*     */   }
/*     */   
/*     */   default <U> U convertMap(DynamicOps<U> paramDynamicOps, T paramT) {
/* 253 */     return paramDynamicOps.createMap(((Stream)getMapValues(paramT).result().orElse(Stream.empty())).map(paramPair -> Pair.of(convertTo(paramDynamicOps, paramPair.getFirst()), convertTo(paramDynamicOps, paramPair.getSecond()))));
/*     */   }
/*     */   
/*     */   T empty();
/*     */   
/*     */   <U> U convertTo(DynamicOps<U> paramDynamicOps, T paramT);
/*     */   
/*     */   DataResult<Number> getNumberValue(T paramT);
/*     */   
/*     */   T createNumeric(Number paramNumber);
/*     */   
/*     */   DataResult<String> getStringValue(T paramT);
/*     */   
/*     */   T createString(String paramString);
/*     */   
/*     */   DataResult<T> mergeToList(T paramT1, T paramT2);
/*     */   
/*     */   DataResult<T> mergeToMap(T paramT1, T paramT2, T paramT3);
/*     */   
/*     */   DataResult<Stream<Pair<T, T>>> getMapValues(T paramT);
/*     */   
/*     */   T createMap(Stream<Pair<T, T>> paramStream);
/*     */   
/*     */   DataResult<Stream<T>> getStream(T paramT);
/*     */   
/*     */   T createList(Stream<T> paramStream);
/*     */   
/*     */   T remove(T paramT, String paramString);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\DynamicOps.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */