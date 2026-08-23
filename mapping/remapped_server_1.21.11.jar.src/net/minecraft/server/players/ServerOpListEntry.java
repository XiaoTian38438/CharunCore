/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*    */ import net.minecraft.server.permissions.PermissionLevel;
/*    */ 
/*    */ public class ServerOpListEntry extends StoredUserEntry<NameAndId> {
/*    */   private final LevelBasedPermissionSet permissions;
/*    */   private final boolean bypassesPlayerLimit;
/*    */   
/*    */   public ServerOpListEntry(NameAndId paramNameAndId, LevelBasedPermissionSet paramLevelBasedPermissionSet, boolean paramBoolean) {
/* 12 */     super(paramNameAndId);
/* 13 */     this.permissions = paramLevelBasedPermissionSet;
/* 14 */     this.bypassesPlayerLimit = paramBoolean;
/*    */   }
/*    */   
/*    */   public ServerOpListEntry(JsonObject paramJsonObject) {
/* 18 */     super(NameAndId.fromJson(paramJsonObject));
/* 19 */     PermissionLevel permissionLevel = paramJsonObject.has("level") ? PermissionLevel.byId(paramJsonObject.get("level").getAsInt()) : PermissionLevel.ALL;
/* 20 */     this.permissions = LevelBasedPermissionSet.forLevel(permissionLevel);
/* 21 */     this.bypassesPlayerLimit = (paramJsonObject.has("bypassesPlayerLimit") && paramJsonObject.get("bypassesPlayerLimit").getAsBoolean());
/*    */   }
/*    */   
/*    */   public LevelBasedPermissionSet permissions() {
/* 25 */     return this.permissions;
/*    */   }
/*    */   
/*    */   public boolean getBypassesPlayerLimit() {
/* 29 */     return this.bypassesPlayerLimit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject paramJsonObject) {
/* 34 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 37 */     getUser().appendTo(paramJsonObject);
/* 38 */     paramJsonObject.addProperty("level", Integer.valueOf(this.permissions.level().id()));
/* 39 */     paramJsonObject.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.bypassesPlayerLimit));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\ServerOpListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */