/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public class UserWhiteListEntry extends StoredUserEntry<NameAndId> {
/*    */   public UserWhiteListEntry(NameAndId paramNameAndId) {
/*  7 */     super(paramNameAndId);
/*    */   }
/*    */   
/*    */   public UserWhiteListEntry(JsonObject paramJsonObject) {
/* 11 */     super(NameAndId.fromJson(paramJsonObject));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject paramJsonObject) {
/* 16 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 19 */     getUser().appendTo(paramJsonObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\UserWhiteListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */