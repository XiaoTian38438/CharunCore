/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Serializer
/*    */   implements JsonDeserializer<ProfileSearchResultsResponse>
/*    */ {
/*    */   public ProfileSearchResultsResponse deserialize(JsonElement paramJsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {
/* 18 */     return new ProfileSearchResultsResponse((List<NameAndId>)paramJsonDeserializationContext.deserialize(paramJsonElement, ProfileSearchResultsResponse.LIST_TYPE));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\ProfileSearchResultsResponse$Serializer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */