/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonRPCUtils
/*    */ {
/*    */   public static final String JSON_RPC_VERSION = "2.0";
/*    */   public static final String OPEN_RPC_VERSION = "1.3.2";
/*    */   
/*    */   public static JsonObject createSuccessResult(JsonElement paramJsonElement1, JsonElement paramJsonElement2) {
/* 18 */     JsonObject jsonObject = new JsonObject();
/* 19 */     jsonObject.addProperty("jsonrpc", "2.0");
/* 20 */     jsonObject.add("id", paramJsonElement1);
/* 21 */     jsonObject.add("result", paramJsonElement2);
/* 22 */     return jsonObject;
/*    */   }
/*    */   
/*    */   public static JsonObject createRequest(Integer paramInteger, Identifier paramIdentifier, List<JsonElement> paramList) {
/* 26 */     JsonObject jsonObject = new JsonObject();
/* 27 */     jsonObject.addProperty("jsonrpc", "2.0");
/* 28 */     if (paramInteger != null) {
/* 29 */       jsonObject.addProperty("id", paramInteger);
/*    */     }
/* 31 */     jsonObject.addProperty("method", paramIdentifier.toString());
/* 32 */     if (!paramList.isEmpty()) {
/* 33 */       JsonArray jsonArray = new JsonArray(paramList.size());
/* 34 */       for (JsonElement jsonElement : paramList) {
/* 35 */         jsonArray.add(jsonElement);
/*    */       }
/* 37 */       jsonObject.add("params", (JsonElement)jsonArray);
/*    */     } 
/* 39 */     return jsonObject;
/*    */   }
/*    */   
/*    */   public static JsonObject createError(JsonElement paramJsonElement, String paramString1, int paramInt, String paramString2) {
/* 43 */     JsonObject jsonObject1 = new JsonObject();
/* 44 */     jsonObject1.addProperty("jsonrpc", "2.0");
/* 45 */     jsonObject1.add("id", paramJsonElement);
/* 46 */     JsonObject jsonObject2 = new JsonObject();
/* 47 */     jsonObject2.addProperty("code", Integer.valueOf(paramInt));
/* 48 */     jsonObject2.addProperty("message", paramString1);
/* 49 */     if (paramString2 != null && !paramString2.isBlank()) {
/* 50 */       jsonObject2.addProperty("data", paramString2);
/*    */     }
/* 52 */     jsonObject1.add("error", (JsonElement)jsonObject2);
/* 53 */     return jsonObject1;
/*    */   }
/*    */   
/*    */   public static JsonElement getRequestId(JsonObject paramJsonObject) {
/* 57 */     return paramJsonObject.get("id");
/*    */   }
/*    */   
/*    */   public static String getMethodName(JsonObject paramJsonObject) {
/* 61 */     return GsonHelper.getAsString(paramJsonObject, "method", null);
/*    */   }
/*    */   
/*    */   public static JsonElement getParams(JsonObject paramJsonObject) {
/* 65 */     return paramJsonObject.get("params");
/*    */   }
/*    */   
/*    */   public static JsonElement getResult(JsonObject paramJsonObject) {
/* 69 */     return paramJsonObject.get("result");
/*    */   }
/*    */   
/*    */   public static JsonObject getError(JsonObject paramJsonObject) {
/* 73 */     return GsonHelper.getAsJsonObject(paramJsonObject, "error", null);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\JsonRPCUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */