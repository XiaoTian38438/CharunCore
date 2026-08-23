/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrayList;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteList;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaOps
/*     */   implements DynamicOps<Object>
/*     */ {
/*  30 */   public static final JavaOps INSTANCE = new JavaOps();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object empty() {
/*  37 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object emptyMap() {
/*  42 */     return Map.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object emptyList() {
/*  47 */     return List.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> paramDynamicOps, Object paramObject) {
/*  52 */     if (paramObject == null) {
/*  53 */       return paramDynamicOps.empty();
/*     */     }
/*  55 */     if (paramObject instanceof Map) {
/*  56 */       return (U)convertMap(paramDynamicOps, paramObject);
/*     */     }
/*  58 */     if (paramObject instanceof ByteList) { ByteList byteList = (ByteList)paramObject;
/*  59 */       return paramDynamicOps.createByteList(ByteBuffer.wrap(byteList.toByteArray())); }
/*     */     
/*  61 */     if (paramObject instanceof IntList) { IntList intList = (IntList)paramObject;
/*  62 */       return paramDynamicOps.createIntList(intList.intStream()); }
/*     */     
/*  64 */     if (paramObject instanceof LongList) { LongList longList = (LongList)paramObject;
/*  65 */       return paramDynamicOps.createLongList(longList.longStream()); }
/*     */     
/*  67 */     if (paramObject instanceof List) {
/*  68 */       return (U)convertList(paramDynamicOps, paramObject);
/*     */     }
/*  70 */     if (paramObject instanceof String) { String str = (String)paramObject;
/*  71 */       return paramDynamicOps.createString(str); }
/*     */     
/*  73 */     if (paramObject instanceof Boolean) { Boolean bool = (Boolean)paramObject;
/*  74 */       return paramDynamicOps.createBoolean(bool.booleanValue()); }
/*     */     
/*  76 */     if (paramObject instanceof Byte) { Byte byte_ = (Byte)paramObject;
/*  77 */       return paramDynamicOps.createByte(byte_.byteValue()); }
/*     */     
/*  79 */     if (paramObject instanceof Short) { Short short_ = (Short)paramObject;
/*  80 */       return paramDynamicOps.createShort(short_.shortValue()); }
/*     */     
/*  82 */     if (paramObject instanceof Integer) { Integer integer = (Integer)paramObject;
/*  83 */       return paramDynamicOps.createInt(integer.intValue()); }
/*     */     
/*  85 */     if (paramObject instanceof Long) { Long long_ = (Long)paramObject;
/*  86 */       return paramDynamicOps.createLong(long_.longValue()); }
/*     */     
/*  88 */     if (paramObject instanceof Float) { Float float_ = (Float)paramObject;
/*  89 */       return paramDynamicOps.createFloat(float_.floatValue()); }
/*     */     
/*  91 */     if (paramObject instanceof Double) { Double double_ = (Double)paramObject;
/*  92 */       return paramDynamicOps.createDouble(double_.doubleValue()); }
/*     */     
/*  94 */     if (paramObject instanceof Number) { Number number = (Number)paramObject;
/*  95 */       return paramDynamicOps.createNumeric(number); }
/*     */     
/*  97 */     throw new IllegalStateException("Don't know how to convert " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Number> getNumberValue(Object paramObject) {
/* 102 */     if (paramObject instanceof Number) { Number number = (Number)paramObject;
/* 103 */       return DataResult.success(number); }
/*     */     
/* 105 */     return DataResult.error(() -> "Not a number: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createNumeric(Number paramNumber) {
/* 110 */     return paramNumber;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createByte(byte paramByte) {
/* 115 */     return Byte.valueOf(paramByte);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createShort(short paramShort) {
/* 120 */     return Short.valueOf(paramShort);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createInt(int paramInt) {
/* 125 */     return Integer.valueOf(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createLong(long paramLong) {
/* 130 */     return Long.valueOf(paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createFloat(float paramFloat) {
/* 135 */     return Float.valueOf(paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createDouble(double paramDouble) {
/* 140 */     return Double.valueOf(paramDouble);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Boolean> getBooleanValue(Object paramObject) {
/* 145 */     if (paramObject instanceof Boolean) { Boolean bool = (Boolean)paramObject;
/* 146 */       return DataResult.success(bool); }
/*     */     
/* 148 */     return DataResult.error(() -> "Not a boolean: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createBoolean(boolean paramBoolean) {
/* 153 */     return Boolean.valueOf(paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> getStringValue(Object paramObject) {
/* 158 */     if (paramObject instanceof String) { String str = (String)paramObject;
/* 159 */       return DataResult.success(str); }
/*     */     
/* 161 */     return DataResult.error(() -> "Not a string: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createString(String paramString) {
/* 166 */     return paramString;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Object> mergeToList(Object paramObject1, Object paramObject2) {
/* 171 */     if (paramObject1 == empty()) {
/* 172 */       return DataResult.success(List.of(paramObject2));
/*     */     }
/* 174 */     if (paramObject1 instanceof List) { List list = (List)paramObject1;
/* 175 */       if (list.isEmpty()) {
/* 176 */         return DataResult.success(List.of(paramObject2));
/*     */       }
/* 178 */       return DataResult.success(ImmutableList.builder().addAll(list).add(paramObject2).build()); }
/*     */     
/* 180 */     return DataResult.error(() -> "Not a list: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Object> mergeToList(Object paramObject, List<Object> paramList) {
/* 185 */     if (paramObject == empty()) {
/* 186 */       return DataResult.success(paramList);
/*     */     }
/* 188 */     if (paramObject instanceof List) { List list = (List)paramObject;
/* 189 */       if (paramList.isEmpty()) {
/* 190 */         return DataResult.success(list);
/*     */       }
/* 192 */       if (list.isEmpty()) {
/* 193 */         return DataResult.success(paramList);
/*     */       }
/* 195 */       return DataResult.success(ImmutableList.builder().addAll(list).addAll(paramList).build()); }
/*     */     
/* 197 */     return DataResult.error(() -> "Not a list: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Object> mergeToMap(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 202 */     if (paramObject1 == empty()) {
/* 203 */       return DataResult.success(Map.of(paramObject2, paramObject3));
/*     */     }
/* 205 */     if (paramObject1 instanceof Map) { Map map = (Map)paramObject1;
/* 206 */       if (map.isEmpty()) {
/* 207 */         return DataResult.success(Map.of(paramObject2, paramObject3));
/*     */       }
/* 209 */       ImmutableMap.Builder builder = ImmutableMap.builderWithExpectedSize(map.size() + 1);
/* 210 */       builder.putAll(map);
/* 211 */       builder.put(paramObject2, paramObject3);
/* 212 */       return DataResult.success(builder.buildKeepingLast()); }
/*     */     
/* 214 */     return DataResult.error(() -> "Not a map: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Object> mergeToMap(Object paramObject, Map<Object, Object> paramMap) {
/* 219 */     if (paramObject == empty()) {
/* 220 */       return DataResult.success(paramMap);
/*     */     }
/* 222 */     if (paramObject instanceof Map) { Map map = (Map)paramObject;
/* 223 */       if (paramMap.isEmpty()) {
/* 224 */         return DataResult.success(map);
/*     */       }
/* 226 */       if (map.isEmpty()) {
/* 227 */         return DataResult.success(paramMap);
/*     */       }
/* 229 */       ImmutableMap.Builder builder = ImmutableMap.builderWithExpectedSize(map.size() + paramMap.size());
/* 230 */       builder.putAll(map);
/* 231 */       builder.putAll(paramMap);
/* 232 */       return DataResult.success(builder.buildKeepingLast()); }
/*     */     
/* 234 */     return DataResult.error(() -> "Not a map: " + String.valueOf(paramObject));
/*     */   }
/*     */   
/*     */   private static Map<Object, Object> mapLikeToMap(MapLike<Object> paramMapLike) {
/* 238 */     return (Map<Object, Object>)paramMapLike.entries().collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Object> mergeToMap(Object paramObject, MapLike<Object> paramMapLike) {
/* 243 */     if (paramObject == empty()) {
/* 244 */       return DataResult.success(mapLikeToMap(paramMapLike));
/*     */     }
/* 246 */     if (paramObject instanceof Map) { Map map = (Map)paramObject;
/* 247 */       if (map.isEmpty()) {
/* 248 */         return DataResult.success(mapLikeToMap(paramMapLike));
/*     */       }
/*     */       
/* 251 */       Iterator iterator = paramMapLike.entries().iterator();
/* 252 */       if (!iterator.hasNext()) {
/* 253 */         return DataResult.success(map);
/*     */       }
/*     */       
/* 256 */       ImmutableMap.Builder builder = ImmutableMap.builderWithExpectedSize(map.size());
/* 257 */       builder.putAll(map);
/*     */       
/* 259 */       iterator.forEachRemaining(paramPair -> paramBuilder.put(paramPair.getFirst(), paramPair.getSecond()));
/* 260 */       return DataResult.success(builder.buildKeepingLast()); }
/*     */     
/* 262 */     return DataResult.error(() -> "Not a map: " + String.valueOf(paramObject));
/*     */   }
/*     */   
/*     */   private static Stream<Pair<Object, Object>> getMapEntries(Map<?, ?> paramMap) {
/* 266 */     return paramMap.entrySet().stream().map(paramEntry -> Pair.of(paramEntry.getKey(), paramEntry.getValue()));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Pair<Object, Object>>> getMapValues(Object paramObject) {
/* 271 */     if (paramObject instanceof Map) { Map<?, ?> map = (Map)paramObject;
/* 272 */       return DataResult.success(getMapEntries(map)); }
/*     */     
/* 274 */     return DataResult.error(() -> "Not a map: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<Object, Object>>> getMapEntries(Object paramObject) {
/* 279 */     if (paramObject instanceof Map) { Map map = (Map)paramObject;
/* 280 */       Objects.requireNonNull(map); return DataResult.success(map::forEach); }
/*     */     
/* 282 */     return DataResult.error(() -> "Not a map: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createMap(Stream<Pair<Object, Object>> paramStream) {
/* 287 */     return paramStream.collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<MapLike<Object>> getMap(Object paramObject) {
/* 292 */     if (paramObject instanceof Map) { final Map map = (Map)paramObject;
/* 293 */       return DataResult.success(new MapLike()
/*     */           {
/*     */             @Nullable
/*     */             public Object get(Object param1Object)
/*     */             {
/* 298 */               return map.get(param1Object);
/*     */             }
/*     */ 
/*     */             
/*     */             @Nullable
/*     */             public Object get(String param1String) {
/* 304 */               return map.get(param1String);
/*     */             }
/*     */ 
/*     */             
/*     */             public Stream<Pair<Object, Object>> entries() {
/* 309 */               return JavaOps.getMapEntries(map);
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 314 */               return "MapLike[" + String.valueOf(map) + "]";
/*     */             }
/*     */           }); }
/*     */ 
/*     */     
/* 319 */     return DataResult.error(() -> "Not a map: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createMap(Map<Object, Object> paramMap) {
/* 324 */     return paramMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Object>> getStream(Object paramObject) {
/* 329 */     if (paramObject instanceof List) { List list = (List)paramObject;
/* 330 */       return DataResult.success(list.stream().map(paramObject -> paramObject)); }
/*     */     
/* 332 */     return DataResult.error(() -> "Not an list: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<Consumer<Object>>> getList(Object paramObject) {
/* 337 */     if (paramObject instanceof List) { List list = (List)paramObject;
/* 338 */       Objects.requireNonNull(list); return DataResult.success(list::forEach); }
/*     */     
/* 340 */     return DataResult.error(() -> "Not an list: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createList(Stream<Object> paramStream) {
/* 345 */     return paramStream.toList();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<ByteBuffer> getByteBuffer(Object paramObject) {
/* 350 */     if (paramObject instanceof ByteList) { ByteList byteList = (ByteList)paramObject;
/* 351 */       return DataResult.success(ByteBuffer.wrap(byteList.toByteArray())); }
/*     */     
/* 353 */     return DataResult.error(() -> "Not a byte list: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createByteList(ByteBuffer paramByteBuffer) {
/* 359 */     ByteBuffer byteBuffer = paramByteBuffer.duplicate().clear();
/* 360 */     ByteArrayList byteArrayList = new ByteArrayList();
/* 361 */     byteArrayList.size(byteBuffer.capacity());
/* 362 */     byteBuffer.get(0, byteArrayList.elements(), 0, byteArrayList.size());
/* 363 */     return byteArrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<IntStream> getIntStream(Object paramObject) {
/* 368 */     if (paramObject instanceof IntList) { IntList intList = (IntList)paramObject;
/* 369 */       return DataResult.success(intList.intStream()); }
/*     */     
/* 371 */     return DataResult.error(() -> "Not an int list: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createIntList(IntStream paramIntStream) {
/* 376 */     return IntArrayList.toList(paramIntStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<LongStream> getLongStream(Object paramObject) {
/* 381 */     if (paramObject instanceof LongList) { LongList longList = (LongList)paramObject;
/* 382 */       return DataResult.success(longList.longStream()); }
/*     */     
/* 384 */     return DataResult.error(() -> "Not a long list: " + String.valueOf(paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createLongList(LongStream paramLongStream) {
/* 389 */     return LongArrayList.toList(paramLongStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object remove(Object paramObject, String paramString) {
/* 394 */     if (paramObject instanceof Map) { Map<?, ?> map = (Map)paramObject;
/* 395 */       LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>(map);
/* 396 */       linkedHashMap.remove(paramString);
/* 397 */       return Map.copyOf(linkedHashMap); }
/*     */     
/* 399 */     return paramObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<Object> mapBuilder() {
/* 404 */     return new FixedMapBuilder(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 409 */     return "Java";
/*     */   }
/*     */   
/*     */   private static final class FixedMapBuilder<T> extends RecordBuilder.AbstractUniversalBuilder<T, ImmutableMap.Builder<T, T>> {
/*     */     public FixedMapBuilder(DynamicOps<T> param1DynamicOps) {
/* 414 */       super(param1DynamicOps);
/*     */     }
/*     */ 
/*     */     
/*     */     protected ImmutableMap.Builder<T, T> initBuilder() {
/* 419 */       return ImmutableMap.builder();
/*     */     }
/*     */ 
/*     */     
/*     */     protected ImmutableMap.Builder<T, T> append(T param1T1, T param1T2, ImmutableMap.Builder<T, T> param1Builder) {
/* 424 */       return param1Builder.put(param1T1, param1T2);
/*     */     }
/*     */ 
/*     */     
/*     */     protected DataResult<T> build(ImmutableMap.Builder<T, T> param1Builder, T param1T) {
/* 429 */       ImmutableMap immutableMap = param1Builder.buildKeepingLast();
/* 430 */       return ops().mergeToMap(param1T, (Map<T, T>)immutableMap);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\JavaOps.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */