/*    */ package com.mojang.authlib.properties;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Serializer
/*    */   implements JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap>
/*    */ {
/*    */   public PropertyMap deserialize(JsonElement paramJsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {
/* 35 */     ImmutableMultimap.Builder builder = ImmutableMultimap.builder();
/*    */     
/* 37 */     if (paramJsonElement instanceof JsonObject) { JsonObject jsonObject = (JsonObject)paramJsonElement;
/* 38 */       for (Map.Entry entry : jsonObject.entrySet()) {
/* 39 */         if (entry.getValue() instanceof JsonArray) {
/* 40 */           for (JsonElement jsonElement : entry.getValue()) {
/* 41 */             builder.put(entry.getKey(), new Property((String)entry.getKey(), jsonElement.getAsString()));
/*    */           }
/*    */         }
/*    */       }  }
/* 45 */     else if (paramJsonElement instanceof JsonArray) { JsonArray jsonArray = (JsonArray)paramJsonElement;
/* 46 */       for (JsonElement jsonElement : jsonArray) {
/* 47 */         if (jsonElement instanceof JsonObject) { JsonObject jsonObject = (JsonObject)jsonElement;
/* 48 */           String str1 = jsonObject.getAsJsonPrimitive("name").getAsString();
/* 49 */           String str2 = jsonObject.getAsJsonPrimitive("value").getAsString();
/*    */           
/* 51 */           if (jsonObject.has("signature")) {
/* 52 */             builder.put(str1, new Property(str1, str2, jsonObject.getAsJsonPrimitive("signature").getAsString())); continue;
/*    */           } 
/* 54 */           builder.put(str1, new Property(str1, str2)); }
/*    */       
/*    */       }  }
/*    */ 
/*    */ 
/*    */     
/* 60 */     return new PropertyMap((Multimap<String, Property>)builder.build());
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement serialize(PropertyMap paramPropertyMap, Type paramType, JsonSerializationContext paramJsonSerializationContext) {
/* 65 */     JsonArray jsonArray = new JsonArray();
/*    */     
/* 67 */     for (Property property : paramPropertyMap.values()) {
/* 68 */       JsonObject jsonObject = new JsonObject();
/*    */       
/* 70 */       jsonObject.addProperty("name", property.name());
/* 71 */       jsonObject.addProperty("value", property.value());
/*    */       
/* 73 */       String str = property.signature();
/* 74 */       if (str != null) {
/* 75 */         jsonObject.addProperty("signature", str);
/*    */       }
/*    */       
/* 78 */       jsonArray.add((JsonElement)jsonObject);
/*    */     } 
/*    */     
/* 81 */     return (JsonElement)jsonArray;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\properties\PropertyMap$Serializer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */