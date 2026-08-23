/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Date;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class UserBanListEntry
/*    */   extends BanListEntry<NameAndId>
/*    */ {
/* 10 */   private static final Component MESSAGE_UNKNOWN_USER = (Component)Component.translatable("commands.banlist.entry.unknown");
/*    */   
/*    */   public UserBanListEntry(NameAndId paramNameAndId) {
/* 13 */     this(paramNameAndId, null, null, null, null);
/*    */   }
/*    */   
/*    */   public UserBanListEntry(NameAndId paramNameAndId, Date paramDate1, String paramString1, Date paramDate2, String paramString2) {
/* 17 */     super(paramNameAndId, paramDate1, paramString1, paramDate2, paramString2);
/*    */   }
/*    */   
/*    */   public UserBanListEntry(JsonObject paramJsonObject) {
/* 21 */     super(NameAndId.fromJson(paramJsonObject), paramJsonObject);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject paramJsonObject) {
/* 26 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 29 */     getUser().appendTo(paramJsonObject);
/* 30 */     super.serialize(paramJsonObject);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDisplayName() {
/* 35 */     NameAndId nameAndId = getUser();
/* 36 */     return (nameAndId != null) ? (Component)Component.literal(nameAndId.name()) : MESSAGE_UNKNOWN_USER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\UserBanListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */