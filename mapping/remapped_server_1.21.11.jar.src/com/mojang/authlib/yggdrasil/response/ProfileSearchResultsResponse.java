/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class ProfileSearchResultsResponse extends Record {
/*    */   private final List<NameAndId> profiles;
/*    */   
/* 12 */   public ProfileSearchResultsResponse(List<NameAndId> paramList) { this.profiles = paramList; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/ProfileSearchResultsResponse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 12 */     //   #12	-> 0 } public List<NameAndId> profiles() { return this.profiles; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/ProfileSearchResultsResponse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/ProfileSearchResultsResponse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 13 */     //   #12	-> 0 } public static final Type LIST_TYPE = TypeToken.getParameterized(List.class, new Type[] { NameAndId.class }).getType();
/*    */   
/*    */   public static class Serializer
/*    */     implements JsonDeserializer<ProfileSearchResultsResponse> {
/*    */     public ProfileSearchResultsResponse deserialize(JsonElement param1JsonElement, Type param1Type, JsonDeserializationContext param1JsonDeserializationContext) throws JsonParseException {
/* 18 */       return new ProfileSearchResultsResponse((List<NameAndId>)param1JsonDeserializationContext.deserialize(param1JsonElement, ProfileSearchResultsResponse.LIST_TYPE));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\ProfileSearchResultsResponse.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */