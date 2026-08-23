/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.Strictness;
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.Item;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GsonHelper
/*     */ {
/*  39 */   private static final Gson GSON = (new GsonBuilder()).create();
/*     */   
/*     */   public static boolean isStringValue(JsonObject paramJsonObject, String paramString) {
/*  42 */     if (!isValidPrimitive(paramJsonObject, paramString)) {
/*  43 */       return false;
/*     */     }
/*  45 */     return paramJsonObject.getAsJsonPrimitive(paramString).isString();
/*     */   }
/*     */   
/*     */   public static boolean isStringValue(JsonElement paramJsonElement) {
/*  49 */     if (!paramJsonElement.isJsonPrimitive()) {
/*  50 */       return false;
/*     */     }
/*  52 */     return paramJsonElement.getAsJsonPrimitive().isString();
/*     */   }
/*     */   
/*     */   public static boolean isNumberValue(JsonObject paramJsonObject, String paramString) {
/*  56 */     if (!isValidPrimitive(paramJsonObject, paramString)) {
/*  57 */       return false;
/*     */     }
/*  59 */     return paramJsonObject.getAsJsonPrimitive(paramString).isNumber();
/*     */   }
/*     */   
/*     */   public static boolean isNumberValue(JsonElement paramJsonElement) {
/*  63 */     if (!paramJsonElement.isJsonPrimitive()) {
/*  64 */       return false;
/*     */     }
/*  66 */     return paramJsonElement.getAsJsonPrimitive().isNumber();
/*     */   }
/*     */   
/*     */   public static boolean isBooleanValue(JsonObject paramJsonObject, String paramString) {
/*  70 */     if (!isValidPrimitive(paramJsonObject, paramString)) {
/*  71 */       return false;
/*     */     }
/*  73 */     return paramJsonObject.getAsJsonPrimitive(paramString).isBoolean();
/*     */   }
/*     */   
/*     */   public static boolean isBooleanValue(JsonElement paramJsonElement) {
/*  77 */     if (!paramJsonElement.isJsonPrimitive()) {
/*  78 */       return false;
/*     */     }
/*  80 */     return paramJsonElement.getAsJsonPrimitive().isBoolean();
/*     */   }
/*     */   
/*     */   public static boolean isArrayNode(JsonObject paramJsonObject, String paramString) {
/*  84 */     if (!isValidNode(paramJsonObject, paramString)) {
/*  85 */       return false;
/*     */     }
/*  87 */     return paramJsonObject.get(paramString).isJsonArray();
/*     */   }
/*     */   
/*     */   public static boolean isObjectNode(JsonObject paramJsonObject, String paramString) {
/*  91 */     if (!isValidNode(paramJsonObject, paramString)) {
/*  92 */       return false;
/*     */     }
/*  94 */     return paramJsonObject.get(paramString).isJsonObject();
/*     */   }
/*     */   
/*     */   public static boolean isValidPrimitive(JsonObject paramJsonObject, String paramString) {
/*  98 */     if (!isValidNode(paramJsonObject, paramString)) {
/*  99 */       return false;
/*     */     }
/* 101 */     return paramJsonObject.get(paramString).isJsonPrimitive();
/*     */   }
/*     */   
/*     */   public static boolean isValidNode(JsonObject paramJsonObject, String paramString) {
/* 105 */     if (paramJsonObject == null) {
/* 106 */       return false;
/*     */     }
/* 108 */     return (paramJsonObject.get(paramString) != null);
/*     */   }
/*     */   
/*     */   public static JsonElement getNonNull(JsonObject paramJsonObject, String paramString) {
/* 112 */     JsonElement jsonElement = paramJsonObject.get(paramString);
/* 113 */     if (jsonElement == null || jsonElement.isJsonNull()) {
/* 114 */       throw new JsonSyntaxException("Missing field " + paramString);
/*     */     }
/* 116 */     return jsonElement;
/*     */   }
/*     */   
/*     */   public static String convertToString(JsonElement paramJsonElement, String paramString) {
/* 120 */     if (paramJsonElement.isJsonPrimitive()) {
/* 121 */       return paramJsonElement.getAsString();
/*     */     }
/* 123 */     throw new JsonSyntaxException("Expected " + paramString + " to be a string, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getAsString(JsonObject paramJsonObject, String paramString) {
/* 128 */     if (paramJsonObject.has(paramString)) {
/* 129 */       return convertToString(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 131 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a string");
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,_,!null->!null;_,_,null->_")
/*     */   public static String getAsString(JsonObject paramJsonObject, String paramString1, String paramString2) {
/* 137 */     if (paramJsonObject.has(paramString1)) {
/* 138 */       return convertToString(paramJsonObject.get(paramString1), paramString1);
/*     */     }
/* 140 */     return paramString2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Holder<Item> convertToItem(JsonElement paramJsonElement, String paramString) {
/* 145 */     if (paramJsonElement.isJsonPrimitive()) {
/* 146 */       String str = paramJsonElement.getAsString();
/* 147 */       return (Holder<Item>)BuiltInRegistries.ITEM.get(Identifier.parse(str))
/* 148 */         .orElseThrow(() -> new JsonSyntaxException("Expected " + paramString1 + " to be an item, was unknown string '" + paramString2 + "'"));
/*     */     } 
/* 150 */     throw new JsonSyntaxException("Expected " + paramString + " to be an item, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Holder<Item> getAsItem(JsonObject paramJsonObject, String paramString) {
/* 155 */     if (paramJsonObject.has(paramString)) {
/* 156 */       return convertToItem(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 158 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find an item");
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,_,!null->!null;_,_,null->_")
/*     */   public static Holder<Item> getAsItem(JsonObject paramJsonObject, String paramString, Holder<Item> paramHolder) {
/* 164 */     if (paramJsonObject.has(paramString)) {
/* 165 */       return convertToItem(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 167 */     return paramHolder;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean convertToBoolean(JsonElement paramJsonElement, String paramString) {
/* 172 */     if (paramJsonElement.isJsonPrimitive()) {
/* 173 */       return paramJsonElement.getAsBoolean();
/*     */     }
/* 175 */     throw new JsonSyntaxException("Expected " + paramString + " to be a Boolean, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean getAsBoolean(JsonObject paramJsonObject, String paramString) {
/* 180 */     if (paramJsonObject.has(paramString)) {
/* 181 */       return convertToBoolean(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 183 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a Boolean");
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean getAsBoolean(JsonObject paramJsonObject, String paramString, boolean paramBoolean) {
/* 188 */     if (paramJsonObject.has(paramString)) {
/* 189 */       return convertToBoolean(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 191 */     return paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public static double convertToDouble(JsonElement paramJsonElement, String paramString) {
/* 196 */     if (paramJsonElement.isJsonPrimitive() && paramJsonElement.getAsJsonPrimitive().isNumber()) {
/* 197 */       return paramJsonElement.getAsDouble();
/*     */     }
/* 199 */     throw new JsonSyntaxException("Expected " + paramString + " to be a Double, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getAsDouble(JsonObject paramJsonObject, String paramString) {
/* 204 */     if (paramJsonObject.has(paramString)) {
/* 205 */       return convertToDouble(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 207 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a Double");
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getAsDouble(JsonObject paramJsonObject, String paramString, double paramDouble) {
/* 212 */     if (paramJsonObject.has(paramString)) {
/* 213 */       return convertToDouble(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 215 */     return paramDouble;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float convertToFloat(JsonElement paramJsonElement, String paramString) {
/* 220 */     if (paramJsonElement.isJsonPrimitive() && paramJsonElement.getAsJsonPrimitive().isNumber()) {
/* 221 */       return paramJsonElement.getAsFloat();
/*     */     }
/* 223 */     throw new JsonSyntaxException("Expected " + paramString + " to be a Float, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getAsFloat(JsonObject paramJsonObject, String paramString) {
/* 228 */     if (paramJsonObject.has(paramString)) {
/* 229 */       return convertToFloat(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 231 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a Float");
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getAsFloat(JsonObject paramJsonObject, String paramString, float paramFloat) {
/* 236 */     if (paramJsonObject.has(paramString)) {
/* 237 */       return convertToFloat(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 239 */     return paramFloat;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long convertToLong(JsonElement paramJsonElement, String paramString) {
/* 244 */     if (paramJsonElement.isJsonPrimitive() && paramJsonElement.getAsJsonPrimitive().isNumber()) {
/* 245 */       return paramJsonElement.getAsLong();
/*     */     }
/* 247 */     throw new JsonSyntaxException("Expected " + paramString + " to be a Long, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getAsLong(JsonObject paramJsonObject, String paramString) {
/* 252 */     if (paramJsonObject.has(paramString)) {
/* 253 */       return convertToLong(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 255 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a Long");
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getAsLong(JsonObject paramJsonObject, String paramString, long paramLong) {
/* 260 */     if (paramJsonObject.has(paramString)) {
/* 261 */       return convertToLong(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 263 */     return paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int convertToInt(JsonElement paramJsonElement, String paramString) {
/* 268 */     if (paramJsonElement.isJsonPrimitive() && paramJsonElement.getAsJsonPrimitive().isNumber()) {
/* 269 */       return paramJsonElement.getAsInt();
/*     */     }
/* 271 */     throw new JsonSyntaxException("Expected " + paramString + " to be a Int, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getAsInt(JsonObject paramJsonObject, String paramString) {
/* 276 */     if (paramJsonObject.has(paramString)) {
/* 277 */       return convertToInt(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 279 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a Int");
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getAsInt(JsonObject paramJsonObject, String paramString, int paramInt) {
/* 284 */     if (paramJsonObject.has(paramString)) {
/* 285 */       return convertToInt(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 287 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte convertToByte(JsonElement paramJsonElement, String paramString) {
/* 292 */     if (paramJsonElement.isJsonPrimitive() && paramJsonElement.getAsJsonPrimitive().isNumber()) {
/* 293 */       return paramJsonElement.getAsByte();
/*     */     }
/* 295 */     throw new JsonSyntaxException("Expected " + paramString + " to be a Byte, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte getAsByte(JsonObject paramJsonObject, String paramString) {
/* 300 */     if (paramJsonObject.has(paramString)) {
/* 301 */       return convertToByte(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 303 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a Byte");
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte getAsByte(JsonObject paramJsonObject, String paramString, byte paramByte) {
/* 308 */     if (paramJsonObject.has(paramString)) {
/* 309 */       return convertToByte(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 311 */     return paramByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static char convertToCharacter(JsonElement paramJsonElement, String paramString) {
/* 316 */     if (paramJsonElement.isJsonPrimitive() && paramJsonElement.getAsJsonPrimitive().isNumber()) {
/* 317 */       return paramJsonElement.getAsCharacter();
/*     */     }
/* 319 */     throw new JsonSyntaxException("Expected " + paramString + " to be a Character, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static char getAsCharacter(JsonObject paramJsonObject, String paramString) {
/* 324 */     if (paramJsonObject.has(paramString)) {
/* 325 */       return convertToCharacter(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 327 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a Character");
/*     */   }
/*     */ 
/*     */   
/*     */   public static char getAsCharacter(JsonObject paramJsonObject, String paramString, char paramChar) {
/* 332 */     if (paramJsonObject.has(paramString)) {
/* 333 */       return convertToCharacter(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 335 */     return paramChar;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal convertToBigDecimal(JsonElement paramJsonElement, String paramString) {
/* 340 */     if (paramJsonElement.isJsonPrimitive() && paramJsonElement.getAsJsonPrimitive().isNumber()) {
/* 341 */       return paramJsonElement.getAsBigDecimal();
/*     */     }
/* 343 */     throw new JsonSyntaxException("Expected " + paramString + " to be a BigDecimal, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAsBigDecimal(JsonObject paramJsonObject, String paramString) {
/* 348 */     if (paramJsonObject.has(paramString)) {
/* 349 */       return convertToBigDecimal(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 351 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a BigDecimal");
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAsBigDecimal(JsonObject paramJsonObject, String paramString, BigDecimal paramBigDecimal) {
/* 356 */     if (paramJsonObject.has(paramString)) {
/* 357 */       return convertToBigDecimal(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 359 */     return paramBigDecimal;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger convertToBigInteger(JsonElement paramJsonElement, String paramString) {
/* 364 */     if (paramJsonElement.isJsonPrimitive() && paramJsonElement.getAsJsonPrimitive().isNumber()) {
/* 365 */       return paramJsonElement.getAsBigInteger();
/*     */     }
/* 367 */     throw new JsonSyntaxException("Expected " + paramString + " to be a BigInteger, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger getAsBigInteger(JsonObject paramJsonObject, String paramString) {
/* 372 */     if (paramJsonObject.has(paramString)) {
/* 373 */       return convertToBigInteger(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 375 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a BigInteger");
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger getAsBigInteger(JsonObject paramJsonObject, String paramString, BigInteger paramBigInteger) {
/* 380 */     if (paramJsonObject.has(paramString)) {
/* 381 */       return convertToBigInteger(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 383 */     return paramBigInteger;
/*     */   }
/*     */ 
/*     */   
/*     */   public static short convertToShort(JsonElement paramJsonElement, String paramString) {
/* 388 */     if (paramJsonElement.isJsonPrimitive() && paramJsonElement.getAsJsonPrimitive().isNumber()) {
/* 389 */       return paramJsonElement.getAsShort();
/*     */     }
/* 391 */     throw new JsonSyntaxException("Expected " + paramString + " to be a Short, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static short getAsShort(JsonObject paramJsonObject, String paramString) {
/* 396 */     if (paramJsonObject.has(paramString)) {
/* 397 */       return convertToShort(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 399 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a Short");
/*     */   }
/*     */ 
/*     */   
/*     */   public static short getAsShort(JsonObject paramJsonObject, String paramString, short paramShort) {
/* 404 */     if (paramJsonObject.has(paramString)) {
/* 405 */       return convertToShort(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 407 */     return paramShort;
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonObject convertToJsonObject(JsonElement paramJsonElement, String paramString) {
/* 412 */     if (paramJsonElement.isJsonObject()) {
/* 413 */       return paramJsonElement.getAsJsonObject();
/*     */     }
/* 415 */     throw new JsonSyntaxException("Expected " + paramString + " to be a JsonObject, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonObject getAsJsonObject(JsonObject paramJsonObject, String paramString) {
/* 420 */     if (paramJsonObject.has(paramString)) {
/* 421 */       return convertToJsonObject(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 423 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a JsonObject");
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,_,!null->!null;_,_,null->_")
/*     */   public static JsonObject getAsJsonObject(JsonObject paramJsonObject1, String paramString, JsonObject paramJsonObject2) {
/* 429 */     if (paramJsonObject1.has(paramString)) {
/* 430 */       return convertToJsonObject(paramJsonObject1.get(paramString), paramString);
/*     */     }
/* 432 */     return paramJsonObject2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonArray convertToJsonArray(JsonElement paramJsonElement, String paramString) {
/* 437 */     if (paramJsonElement.isJsonArray()) {
/* 438 */       return paramJsonElement.getAsJsonArray();
/*     */     }
/* 440 */     throw new JsonSyntaxException("Expected " + paramString + " to be a JsonArray, was " + getType(paramJsonElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonArray getAsJsonArray(JsonObject paramJsonObject, String paramString) {
/* 445 */     if (paramJsonObject.has(paramString)) {
/* 446 */       return convertToJsonArray(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 448 */     throw new JsonSyntaxException("Missing " + paramString + ", expected to find a JsonArray");
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,_,!null->!null;_,_,null->_")
/*     */   public static JsonArray getAsJsonArray(JsonObject paramJsonObject, String paramString, JsonArray paramJsonArray) {
/* 454 */     if (paramJsonObject.has(paramString)) {
/* 455 */       return convertToJsonArray(paramJsonObject.get(paramString), paramString);
/*     */     }
/* 457 */     return paramJsonArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> T convertToObject(JsonElement paramJsonElement, String paramString, JsonDeserializationContext paramJsonDeserializationContext, Class<? extends T> paramClass) {
/* 462 */     if (paramJsonElement != null) {
/* 463 */       return (T)paramJsonDeserializationContext.deserialize(paramJsonElement, paramClass);
/*     */     }
/* 465 */     throw new JsonSyntaxException("Missing " + paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> T getAsObject(JsonObject paramJsonObject, String paramString, JsonDeserializationContext paramJsonDeserializationContext, Class<? extends T> paramClass) {
/* 470 */     if (paramJsonObject.has(paramString)) {
/* 471 */       return convertToObject(paramJsonObject.get(paramString), paramString, paramJsonDeserializationContext, paramClass);
/*     */     }
/* 473 */     throw new JsonSyntaxException("Missing " + paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,_,!null,_,_->!null;_,_,null,_,_->_")
/*     */   public static <T> T getAsObject(JsonObject paramJsonObject, String paramString, T paramT, JsonDeserializationContext paramJsonDeserializationContext, Class<? extends T> paramClass) {
/* 479 */     if (paramJsonObject.has(paramString)) {
/* 480 */       return convertToObject(paramJsonObject.get(paramString), paramString, paramJsonDeserializationContext, paramClass);
/*     */     }
/* 482 */     return paramT;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getType(JsonElement paramJsonElement) {
/* 487 */     String str = StringUtils.abbreviateMiddle(String.valueOf(paramJsonElement), "...", 10);
/* 488 */     if (paramJsonElement == null) {
/* 489 */       return "null (missing)";
/*     */     }
/* 491 */     if (paramJsonElement.isJsonNull()) {
/* 492 */       return "null (json)";
/*     */     }
/* 494 */     if (paramJsonElement.isJsonArray()) {
/* 495 */       return "an array (" + str + ")";
/*     */     }
/* 497 */     if (paramJsonElement.isJsonObject()) {
/* 498 */       return "an object (" + str + ")";
/*     */     }
/* 500 */     if (paramJsonElement.isJsonPrimitive()) {
/* 501 */       JsonPrimitive jsonPrimitive = paramJsonElement.getAsJsonPrimitive();
/* 502 */       if (jsonPrimitive.isNumber()) {
/* 503 */         return "a number (" + str + ")";
/*     */       }
/* 505 */       if (jsonPrimitive.isBoolean()) {
/* 506 */         return "a boolean (" + str + ")";
/*     */       }
/*     */     } 
/* 509 */     return str;
/*     */   }
/*     */   
/*     */   public static <T> T fromJson(Gson paramGson, Reader paramReader, Class<T> paramClass) {
/*     */     try {
/* 514 */       JsonReader jsonReader = new JsonReader(paramReader);
/* 515 */       jsonReader.setStrictness(Strictness.STRICT);
/* 516 */       Object object = paramGson.getAdapter(paramClass).read(jsonReader);
/* 517 */       if (object == null) {
/* 518 */         throw new JsonParseException("JSON data was null or empty");
/*     */       }
/* 520 */       return (T)object;
/* 521 */     } catch (IOException iOException) {
/* 522 */       throw new JsonParseException(iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T> T fromNullableJson(Gson paramGson, Reader paramReader, TypeToken<T> paramTypeToken) {
/*     */     try {
/* 528 */       JsonReader jsonReader = new JsonReader(paramReader);
/* 529 */       jsonReader.setStrictness(Strictness.STRICT);
/* 530 */       return (T)paramGson.getAdapter(paramTypeToken).read(jsonReader);
/* 531 */     } catch (IOException iOException) {
/* 532 */       throw new JsonParseException(iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T> T fromJson(Gson paramGson, Reader paramReader, TypeToken<T> paramTypeToken) {
/* 537 */     T t = (T)fromNullableJson(paramGson, paramReader, (TypeToken)paramTypeToken);
/* 538 */     if (t == null) {
/* 539 */       throw new JsonParseException("JSON data was null or empty");
/*     */     }
/* 541 */     return t;
/*     */   }
/*     */   
/*     */   public static <T> T fromNullableJson(Gson paramGson, String paramString, TypeToken<T> paramTypeToken) {
/* 545 */     return fromNullableJson(paramGson, new StringReader(paramString), paramTypeToken);
/*     */   }
/*     */   
/*     */   public static <T> T fromJson(Gson paramGson, String paramString, Class<T> paramClass) {
/* 549 */     return fromJson(paramGson, new StringReader(paramString), paramClass);
/*     */   }
/*     */   
/*     */   public static JsonObject parse(String paramString) {
/* 553 */     return parse(new StringReader(paramString));
/*     */   }
/*     */   
/*     */   public static JsonObject parse(Reader paramReader) {
/* 557 */     return fromJson(GSON, paramReader, JsonObject.class);
/*     */   }
/*     */   
/*     */   public static JsonArray parseArray(String paramString) {
/* 561 */     return parseArray(new StringReader(paramString));
/*     */   }
/*     */   
/*     */   public static JsonArray parseArray(Reader paramReader) {
/* 565 */     return fromJson(GSON, paramReader, JsonArray.class);
/*     */   }
/*     */   
/*     */   public static String toStableString(JsonElement paramJsonElement) {
/* 569 */     StringWriter stringWriter = new StringWriter();
/* 570 */     JsonWriter jsonWriter = new JsonWriter(stringWriter);
/*     */     try {
/* 572 */       writeValue(jsonWriter, paramJsonElement, Comparator.naturalOrder());
/* 573 */     } catch (IOException iOException) {
/*     */       
/* 575 */       throw new AssertionError(iOException);
/*     */     } 
/* 577 */     return stringWriter.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeValue(JsonWriter paramJsonWriter, JsonElement paramJsonElement, Comparator<String> paramComparator) throws IOException {
/* 584 */     if (paramJsonElement == null || paramJsonElement.isJsonNull()) {
/* 585 */       paramJsonWriter.nullValue();
/* 586 */     } else if (paramJsonElement.isJsonPrimitive()) {
/* 587 */       JsonPrimitive jsonPrimitive = paramJsonElement.getAsJsonPrimitive();
/* 588 */       if (jsonPrimitive.isNumber()) {
/* 589 */         paramJsonWriter.value(jsonPrimitive.getAsNumber());
/* 590 */       } else if (jsonPrimitive.isBoolean()) {
/* 591 */         paramJsonWriter.value(jsonPrimitive.getAsBoolean());
/*     */       } else {
/* 593 */         paramJsonWriter.value(jsonPrimitive.getAsString());
/*     */       } 
/* 595 */     } else if (paramJsonElement.isJsonArray()) {
/* 596 */       paramJsonWriter.beginArray();
/* 597 */       for (JsonElement jsonElement : paramJsonElement.getAsJsonArray()) {
/* 598 */         writeValue(paramJsonWriter, jsonElement, paramComparator);
/*     */       }
/* 600 */       paramJsonWriter.endArray();
/* 601 */     } else if (paramJsonElement.isJsonObject()) {
/* 602 */       paramJsonWriter.beginObject();
/* 603 */       for (Map.Entry<String, JsonElement> entry : sortByKeyIfNeeded(paramJsonElement.getAsJsonObject().entrySet(), paramComparator)) {
/* 604 */         paramJsonWriter.name((String)entry.getKey());
/* 605 */         writeValue(paramJsonWriter, (JsonElement)entry.getValue(), paramComparator);
/*     */       } 
/* 607 */       paramJsonWriter.endObject();
/*     */     } else {
/* 609 */       throw new IllegalArgumentException("Couldn't write " + String.valueOf(paramJsonElement.getClass()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Collection<Map.Entry<String, JsonElement>> sortByKeyIfNeeded(Collection<Map.Entry<String, JsonElement>> paramCollection, Comparator<String> paramComparator) {
/* 614 */     if (paramComparator == null) {
/* 615 */       return paramCollection;
/*     */     }
/* 617 */     ArrayList<Map.Entry<String, JsonElement>> arrayList = new ArrayList<>(paramCollection);
/* 618 */     arrayList.sort((Comparator)Map.Entry.comparingByKey(paramComparator));
/* 619 */     return arrayList;
/*     */   }
/*     */   
/*     */   public static boolean encodesLongerThan(JsonElement paramJsonElement, int paramInt) {
/*     */     try {
/* 624 */       Streams.write(paramJsonElement, new JsonWriter(Streams.writerForAppendable(new CountedAppendable(paramInt))));
/* 625 */     } catch (IllegalStateException illegalStateException) {
/* 626 */       return true;
/* 627 */     } catch (IOException iOException) {
/* 628 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 630 */     return false;
/*     */   }
/*     */   
/*     */   private static class CountedAppendable implements Appendable {
/*     */     private int totalCount;
/*     */     private final int limit;
/*     */     
/*     */     public CountedAppendable(int param1Int) {
/* 638 */       this.limit = param1Int;
/*     */     }
/*     */     
/*     */     private Appendable accountChars(int param1Int) {
/* 642 */       this.totalCount += param1Int;
/* 643 */       if (this.totalCount > this.limit) {
/* 644 */         throw new IllegalStateException("Character count over limit: " + this.totalCount + " > " + this.limit);
/*     */       }
/* 646 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Appendable append(CharSequence param1CharSequence) {
/* 651 */       return accountChars(param1CharSequence.length());
/*     */     }
/*     */ 
/*     */     
/*     */     public Appendable append(CharSequence param1CharSequence, int param1Int1, int param1Int2) {
/* 656 */       return accountChars(param1Int2 - param1Int1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Appendable append(char param1Char) {
/* 661 */       return accountChars(1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\GsonHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */