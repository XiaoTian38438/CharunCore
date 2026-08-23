/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public abstract class StoredUserEntry<T>
/*    */ {
/*    */   private final T user;
/*    */   
/*    */   public StoredUserEntry(T paramT) {
/* 10 */     this.user = paramT;
/*    */   }
/*    */   
/*    */   public T getUser() {
/* 14 */     return this.user;
/*    */   }
/*    */   
/*    */   boolean hasExpired() {
/* 18 */     return false;
/*    */   }
/*    */   
/*    */   protected abstract void serialize(JsonObject paramJsonObject);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\StoredUserEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */