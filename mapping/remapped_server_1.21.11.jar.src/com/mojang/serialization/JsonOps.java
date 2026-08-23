/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public class JsonOps
/*     */   implements DynamicOps<JsonElement>
/*     */ {
/*  26 */   public static final JsonOps INSTANCE = new JsonOps(false);
/*  27 */   public static final JsonOps COMPRESSED = new JsonOps(true);
/*     */   
/*     */   private final boolean compressed;
/*     */   
/*     */   protected JsonOps(boolean paramBoolean) {
/*  32 */     this.compressed = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement empty() {
/*  37 */     return (JsonElement)JsonNull.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement emptyMap() {
/*  42 */     return (JsonElement)new JsonObject();
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement emptyList() {
/*  47 */     return (JsonElement)new JsonArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> paramDynamicOps, JsonElement paramJsonElement) {
/*  52 */     if (paramJsonElement instanceof JsonObject) {
/*  53 */       return (U)convertMap(paramDynamicOps, paramJsonElement);
/*     */     }
/*  55 */     if (paramJsonElement instanceof JsonArray) {
/*  56 */       return (U)convertList(paramDynamicOps, paramJsonElement);
/*     */     }
/*  58 */     if (paramJsonElement instanceof JsonNull) {
/*  59 */       return paramDynamicOps.empty();
/*     */     }
/*  61 */     JsonPrimitive jsonPrimitive = paramJsonElement.getAsJsonPrimitive();
/*  62 */     if (jsonPrimitive.isString()) {
/*  63 */       return paramDynamicOps.createString(jsonPrimitive.getAsString());
/*     */     }
/*  65 */     if (jsonPrimitive.isBoolean()) {
/*  66 */       return paramDynamicOps.createBoolean(jsonPrimitive.getAsBoolean());
/*     */     }
/*  68 */     BigDecimal bigDecimal = jsonPrimitive.getAsBigDecimal();
/*     */     try {
/*  70 */       long l = bigDecimal.longValueExact();
/*  71 */       if ((byte)(int)l == l) {
/*  72 */         return paramDynamicOps.createByte((byte)(int)l);
/*     */       }
/*  74 */       if ((short)(int)l == l) {
/*  75 */         return paramDynamicOps.createShort((short)(int)l);
/*     */       }
/*  77 */       if ((int)l == l) {
/*  78 */         return paramDynamicOps.createInt((int)l);
/*     */       }
/*  80 */       return paramDynamicOps.createLong(l);
/*  81 */     } catch (ArithmeticException arithmeticException) {
/*  82 */       double d = bigDecimal.doubleValue();
/*  83 */       if ((float)d == d) {
/*  84 */         return paramDynamicOps.createFloat((float)d);
/*     */       }
/*  86 */       return paramDynamicOps.createDouble(d);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Number> getNumberValue(JsonElement paramJsonElement) {
/*  92 */     if (paramJsonElement instanceof JsonPrimitive) {
/*  93 */       if (paramJsonElement.getAsJsonPrimitive().isNumber()) {
/*  94 */         return DataResult.success(paramJsonElement.getAsNumber());
/*     */       }
/*  96 */       if (this.compressed && paramJsonElement.getAsJsonPrimitive().isString()) {
/*     */         try {
/*  98 */           return DataResult.success(Integer.valueOf(Integer.parseInt(paramJsonElement.getAsString())));
/*  99 */         } catch (NumberFormatException numberFormatException) {
/* 100 */           return DataResult.error(() -> "Not a number: " + String.valueOf(paramNumberFormatException) + " " + String.valueOf(paramJsonElement));
/*     */         } 
/*     */       }
/*     */     } 
/* 104 */     return DataResult.error(() -> "Not a number: " + String.valueOf(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement createNumeric(Number paramNumber) {
/* 109 */     return (JsonElement)new JsonPrimitive(paramNumber);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Boolean> getBooleanValue(JsonElement paramJsonElement) {
/* 114 */     if (paramJsonElement instanceof JsonPrimitive && paramJsonElement.getAsJsonPrimitive().isBoolean()) {
/* 115 */       return DataResult.success(Boolean.valueOf(paramJsonElement.getAsBoolean()));
/*     */     }
/* 117 */     return DataResult.error(() -> "Not a boolean: " + String.valueOf(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement createBoolean(boolean paramBoolean) {
/* 122 */     return (JsonElement)new JsonPrimitive(Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> getStringValue(JsonElement paramJsonElement) {
/* 127 */     if (paramJsonElement instanceof JsonPrimitive && (
/* 128 */       paramJsonElement.getAsJsonPrimitive().isString() || (paramJsonElement.getAsJsonPrimitive().isNumber() && this.compressed))) {
/* 129 */       return DataResult.success(paramJsonElement.getAsString());
/*     */     }
/*     */     
/* 132 */     return DataResult.error(() -> "Not a string: " + String.valueOf(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement createString(String paramString) {
/* 137 */     return (JsonElement)new JsonPrimitive(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<JsonElement> mergeToList(JsonElement paramJsonElement1, JsonElement paramJsonElement2) {
/* 142 */     if (!(paramJsonElement1 instanceof JsonArray) && paramJsonElement1 != empty()) {
/* 143 */       return DataResult.error(() -> "mergeToList called with not a list: " + String.valueOf(paramJsonElement), paramJsonElement1);
/*     */     }
/*     */     
/* 146 */     JsonArray jsonArray = new JsonArray();
/* 147 */     if (paramJsonElement1 != empty()) {
/* 148 */       jsonArray.addAll(paramJsonElement1.getAsJsonArray());
/*     */     }
/* 150 */     jsonArray.add(paramJsonElement2);
/* 151 */     return (DataResult)DataResult.success(jsonArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<JsonElement> mergeToList(JsonElement paramJsonElement, List<JsonElement> paramList) {
/* 156 */     if (!(paramJsonElement instanceof JsonArray) && paramJsonElement != empty()) {
/* 157 */       return DataResult.error(() -> "mergeToList called with not a list: " + String.valueOf(paramJsonElement), paramJsonElement);
/*     */     }
/*     */     
/* 160 */     if (paramList.isEmpty()) {
/* 161 */       if (paramJsonElement == empty()) {
/* 162 */         return DataResult.success(emptyList());
/*     */       }
/* 164 */       return DataResult.success(paramJsonElement);
/*     */     } 
/*     */     
/* 167 */     JsonArray jsonArray = new JsonArray();
/* 168 */     if (paramJsonElement != empty()) {
/* 169 */       jsonArray.addAll(paramJsonElement.getAsJsonArray());
/*     */     }
/* 171 */     Objects.requireNonNull(jsonArray); paramList.forEach(jsonArray::add);
/* 172 */     return (DataResult)DataResult.success(jsonArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<JsonElement> mergeToMap(JsonElement paramJsonElement1, JsonElement paramJsonElement2, JsonElement paramJsonElement3) {
/* 177 */     if (!(paramJsonElement1 instanceof JsonObject) && paramJsonElement1 != empty()) {
/* 178 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(paramJsonElement), paramJsonElement1);
/*     */     }
/* 180 */     if (!(paramJsonElement2 instanceof JsonPrimitive) || (!paramJsonElement2.getAsJsonPrimitive().isString() && !this.compressed)) {
/* 181 */       return DataResult.error(() -> "key is not a string: " + String.valueOf(paramJsonElement), paramJsonElement1);
/*     */     }
/*     */     
/* 184 */     JsonObject jsonObject = new JsonObject();
/* 185 */     if (paramJsonElement1 != empty()) {
/* 186 */       paramJsonElement1.getAsJsonObject().entrySet().forEach(paramEntry -> paramJsonObject.add((String)paramEntry.getKey(), (JsonElement)paramEntry.getValue()));
/*     */     }
/* 188 */     jsonObject.add(paramJsonElement2.getAsString(), paramJsonElement3);
/*     */     
/* 190 */     return (DataResult)DataResult.success(jsonObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<JsonElement> mergeToMap(JsonElement paramJsonElement, MapLike<JsonElement> paramMapLike) {
/* 195 */     if (!(paramJsonElement instanceof JsonObject) && paramJsonElement != empty()) {
/* 196 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(paramJsonElement), paramJsonElement);
/*     */     }
/*     */     
/* 199 */     Iterator iterator = paramMapLike.entries().iterator();
/* 200 */     if (!iterator.hasNext()) {
/* 201 */       if (paramJsonElement == empty()) {
/* 202 */         return DataResult.success(emptyMap());
/*     */       }
/* 204 */       return DataResult.success(paramJsonElement);
/*     */     } 
/* 206 */     JsonObject jsonObject = new JsonObject();
/* 207 */     if (paramJsonElement != empty()) {
/* 208 */       paramJsonElement.getAsJsonObject().entrySet().forEach(paramEntry -> paramJsonObject.add((String)paramEntry.getKey(), (JsonElement)paramEntry.getValue()));
/*     */     }
/*     */     
/* 211 */     ArrayList arrayList = Lists.newArrayList();
/*     */     
/* 213 */     iterator.forEachRemaining(paramPair -> {
/*     */           JsonElement jsonElement = (JsonElement)paramPair.getFirst();
/*     */           
/*     */           if (!(jsonElement instanceof JsonPrimitive) || (!jsonElement.getAsJsonPrimitive().isString() && !this.compressed)) {
/*     */             paramList.add(jsonElement);
/*     */             return;
/*     */           } 
/*     */           paramJsonObject.add(jsonElement.getAsString(), (JsonElement)paramPair.getSecond());
/*     */         });
/* 222 */     if (!arrayList.isEmpty()) {
/* 223 */       return (DataResult)DataResult.error(() -> "some keys are not strings: " + String.valueOf(paramList), jsonObject);
/*     */     }
/*     */     
/* 226 */     return (DataResult)DataResult.success(jsonObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Pair<JsonElement, JsonElement>>> getMapValues(JsonElement paramJsonElement) {
/* 231 */     if (!(paramJsonElement instanceof JsonObject)) {
/* 232 */       return DataResult.error(() -> "Not a JSON object: " + String.valueOf(paramJsonElement));
/*     */     }
/* 234 */     return DataResult.success(paramJsonElement.getAsJsonObject().entrySet().stream().map(paramEntry -> Pair.of(new JsonPrimitive((String)paramEntry.getKey()), (paramEntry.getValue() instanceof JsonNull) ? null : paramEntry.getValue())));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<JsonElement, JsonElement>>> getMapEntries(JsonElement paramJsonElement) {
/* 239 */     if (!(paramJsonElement instanceof JsonObject)) {
/* 240 */       return DataResult.error(() -> "Not a JSON object: " + String.valueOf(paramJsonElement));
/*     */     }
/* 242 */     return DataResult.success(paramBiConsumer -> {
/*     */           for (Map.Entry entry : paramJsonElement.getAsJsonObject().entrySet()) {
/*     */             paramBiConsumer.accept(createString((String)entry.getKey()), (entry.getValue() instanceof JsonNull) ? null : (JsonElement)entry.getValue());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<MapLike<JsonElement>> getMap(JsonElement paramJsonElement) {
/* 251 */     if (!(paramJsonElement instanceof JsonObject)) {
/* 252 */       return DataResult.error(() -> "Not a JSON object: " + String.valueOf(paramJsonElement));
/*     */     }
/* 254 */     final JsonObject object = paramJsonElement.getAsJsonObject();
/* 255 */     return DataResult.success(new MapLike<JsonElement>()
/*     */         {
/*     */           @Nullable
/*     */           public JsonElement get(JsonElement param1JsonElement) {
/* 259 */             JsonElement jsonElement = object.get(param1JsonElement.getAsString());
/* 260 */             if (jsonElement instanceof JsonNull) {
/* 261 */               return null;
/*     */             }
/* 263 */             return jsonElement;
/*     */           }
/*     */ 
/*     */           
/*     */           @Nullable
/*     */           public JsonElement get(String param1String) {
/* 269 */             JsonElement jsonElement = object.get(param1String);
/* 270 */             if (jsonElement instanceof JsonNull) {
/* 271 */               return null;
/*     */             }
/* 273 */             return jsonElement;
/*     */           }
/*     */ 
/*     */           
/*     */           public Stream<Pair<JsonElement, JsonElement>> entries() {
/* 278 */             return object.entrySet().stream().map(param1Entry -> Pair.of(new JsonPrimitive((String)param1Entry.getKey()), param1Entry.getValue()));
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 283 */             return "MapLike[" + String.valueOf(object) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement createMap(Stream<Pair<JsonElement, JsonElement>> paramStream) {
/* 290 */     JsonObject jsonObject = new JsonObject();
/* 291 */     paramStream.forEach(paramPair -> paramJsonObject.add(((JsonElement)paramPair.getFirst()).getAsString(), (JsonElement)paramPair.getSecond()));
/* 292 */     return (JsonElement)jsonObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<JsonElement>> getStream(JsonElement paramJsonElement) {
/* 297 */     if (paramJsonElement instanceof JsonArray) {
/* 298 */       return DataResult.success(StreamSupport.stream(paramJsonElement.getAsJsonArray().spliterator(), false).map(paramJsonElement -> (paramJsonElement instanceof JsonNull) ? null : paramJsonElement));
/*     */     }
/* 300 */     return DataResult.error(() -> "Not a json array: " + String.valueOf(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<Consumer<JsonElement>>> getList(JsonElement paramJsonElement) {
/* 305 */     if (paramJsonElement instanceof JsonArray) {
/* 306 */       return DataResult.success(paramConsumer -> {
/*     */             for (JsonElement jsonElement : paramJsonElement.getAsJsonArray()) {
/*     */               paramConsumer.accept((jsonElement instanceof JsonNull) ? null : jsonElement);
/*     */             }
/*     */           });
/*     */     }
/* 312 */     return DataResult.error(() -> "Not a json array: " + String.valueOf(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement createList(Stream<JsonElement> paramStream) {
/* 317 */     JsonArray jsonArray = new JsonArray();
/* 318 */     Objects.requireNonNull(jsonArray); paramStream.forEach(jsonArray::add);
/* 319 */     return (JsonElement)jsonArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement remove(JsonElement paramJsonElement, String paramString) {
/* 324 */     if (paramJsonElement instanceof JsonObject) {
/* 325 */       JsonObject jsonObject = new JsonObject();
/* 326 */       paramJsonElement.getAsJsonObject().entrySet().stream().filter(paramEntry -> !Objects.equals(paramEntry.getKey(), paramString)).forEach(paramEntry -> paramJsonObject.add((String)paramEntry.getKey(), (JsonElement)paramEntry.getValue()));
/* 327 */       return (JsonElement)jsonObject;
/*     */     } 
/* 329 */     return paramJsonElement;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 334 */     return "JSON";
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<JsonElement> listBuilder() {
/* 339 */     return new ArrayBuilder();
/*     */   }
/*     */   
/*     */   private static final class ArrayBuilder implements ListBuilder<JsonElement> { private ArrayBuilder() {
/* 343 */       this.builder = DataResult.success(new JsonArray(), Lifecycle.stable());
/*     */     }
/*     */     private DataResult<JsonArray> builder;
/*     */     public DynamicOps<JsonElement> ops() {
/* 347 */       return JsonOps.INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<JsonElement> add(JsonElement param1JsonElement) {
/* 352 */       this.builder = this.builder.map(param1JsonArray -> {
/*     */             param1JsonArray.add(param1JsonElement);
/*     */             return param1JsonArray;
/*     */           });
/* 356 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<JsonElement> add(DataResult<JsonElement> param1DataResult) {
/* 361 */       this.builder = this.builder.apply2stable((param1JsonArray, param1JsonElement) -> { param1JsonArray.add(param1JsonElement); return param1JsonArray; }param1DataResult);
/*     */ 
/*     */ 
/*     */       
/* 365 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<JsonElement> withErrorsFrom(DataResult<?> param1DataResult) {
/* 370 */       this.builder = this.builder.flatMap(param1JsonArray -> param1DataResult.map(()));
/* 371 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<JsonElement> mapError(UnaryOperator<String> param1UnaryOperator) {
/* 376 */       this.builder = this.builder.mapError(param1UnaryOperator);
/* 377 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<JsonElement> build(JsonElement param1JsonElement) {
/* 382 */       DataResult<?> dataResult = this.builder.flatMap(param1JsonArray -> {
/*     */             if (!(param1JsonElement instanceof JsonArray) && param1JsonElement != ops().empty()) {
/*     */               return DataResult.error((), param1JsonElement);
/*     */             }
/*     */             
/*     */             JsonArray jsonArray = new JsonArray();
/*     */             
/*     */             if (param1JsonElement != ops().empty()) {
/*     */               jsonArray.addAll(param1JsonElement.getAsJsonArray());
/*     */             }
/*     */             jsonArray.addAll(param1JsonArray);
/*     */             return DataResult.success(jsonArray, Lifecycle.stable());
/*     */           });
/* 395 */       this.builder = DataResult.success(new JsonArray(), Lifecycle.stable());
/* 396 */       return (DataResult)dataResult;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean compressMaps() {
/* 402 */     return this.compressed;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<JsonElement> mapBuilder() {
/* 407 */     return new JsonRecordBuilder();
/*     */   }
/*     */   
/*     */   private class JsonRecordBuilder extends RecordBuilder.AbstractStringBuilder<JsonElement, JsonObject> {
/*     */     protected JsonRecordBuilder() {
/* 412 */       super(JsonOps.this);
/*     */     }
/*     */ 
/*     */     
/*     */     protected JsonObject initBuilder() {
/* 417 */       return new JsonObject();
/*     */     }
/*     */ 
/*     */     
/*     */     protected JsonObject append(String param1String, JsonElement param1JsonElement, JsonObject param1JsonObject) {
/* 422 */       param1JsonObject.add(param1String, param1JsonElement);
/* 423 */       return param1JsonObject;
/*     */     }
/*     */ 
/*     */     
/*     */     protected DataResult<JsonElement> build(JsonObject param1JsonObject, JsonElement param1JsonElement) {
/* 428 */       if (param1JsonElement == null || param1JsonElement instanceof JsonNull) {
/* 429 */         return (DataResult)DataResult.success(param1JsonObject);
/*     */       }
/* 431 */       if (param1JsonElement instanceof JsonObject) {
/* 432 */         JsonObject jsonObject = new JsonObject();
/* 433 */         for (Map.Entry entry : param1JsonElement.getAsJsonObject().entrySet()) {
/* 434 */           jsonObject.add((String)entry.getKey(), (JsonElement)entry.getValue());
/*     */         }
/* 436 */         for (Map.Entry entry : param1JsonObject.entrySet()) {
/* 437 */           jsonObject.add((String)entry.getKey(), (JsonElement)entry.getValue());
/*     */         }
/* 439 */         return (DataResult)DataResult.success(jsonObject);
/*     */       } 
/* 441 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(param1JsonElement), param1JsonElement);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\JsonOps.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */