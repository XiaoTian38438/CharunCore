/*    */ package com.mojang.authlib.minecraft.client;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.authlib.exceptions.MinecraftClientException;
/*    */ import com.mojang.authlib.properties.PropertyMap;
/*    */ import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
/*    */ import com.mojang.util.ByteBufferTypeAdapter;
/*    */ import com.mojang.util.InstantTypeAdapter;
/*    */ import com.mojang.util.UUIDTypeAdapter;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.time.Instant;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ObjectMapper
/*    */ {
/*    */   private final Gson gson;
/*    */   
/*    */   public ObjectMapper(Gson paramGson) {
/* 24 */     this.gson = Objects.<Gson>requireNonNull(paramGson);
/*    */   }
/*    */   
/*    */   public <T> T readValue(String paramString, Class<T> paramClass) {
/*    */     try {
/* 29 */       return (T)this.gson.fromJson(paramString, paramClass);
/* 30 */     } catch (JsonParseException jsonParseException) {
/* 31 */       throw new MinecraftClientException(MinecraftClientException.ErrorType.JSON_ERROR, "Failed to read value " + paramString, jsonParseException);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String writeValueAsString(Object paramObject) {
/*    */     try {
/* 37 */       return this.gson.toJson(paramObject);
/* 38 */     } catch (RuntimeException runtimeException) {
/* 39 */       throw new MinecraftClientException(MinecraftClientException.ErrorType.JSON_ERROR, "Failed to write value", runtimeException);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static ObjectMapper create() {
/* 44 */     return new ObjectMapper((new GsonBuilder())
/* 45 */         .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
/* 46 */         .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
/* 47 */         .registerTypeHierarchyAdapter(ByteBuffer.class, (new ByteBufferTypeAdapter()).nullSafe())
/* 48 */         .registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer())
/* 49 */         .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
/* 50 */         .registerTypeAdapter(ProfileSearchResultsResponse.class, new ProfileSearchResultsResponse.Serializer())
/* 51 */         .create());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\client\ObjectMapper.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */