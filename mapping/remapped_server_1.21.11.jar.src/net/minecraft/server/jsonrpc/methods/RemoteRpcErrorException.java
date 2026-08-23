/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public class RemoteRpcErrorException
/*    */   extends RuntimeException {
/*    */   private final JsonElement id;
/*    */   private final JsonObject error;
/*    */   
/*    */   public RemoteRpcErrorException(JsonElement paramJsonElement, JsonObject paramJsonObject) {
/* 12 */     this.id = paramJsonElement;
/* 13 */     this.error = paramJsonObject;
/*    */   }
/*    */   
/*    */   private JsonObject getError() {
/* 17 */     return this.error;
/*    */   }
/*    */   
/*    */   private JsonElement getId() {
/* 21 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\methods\RemoteRpcErrorException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */