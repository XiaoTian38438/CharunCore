/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Date;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class IpBanListEntry
/*    */   extends BanListEntry<String>
/*    */ {
/*    */   public IpBanListEntry(String paramString) {
/* 11 */     this(paramString, null, null, null, null);
/*    */   }
/*    */   
/*    */   public IpBanListEntry(String paramString1, Date paramDate1, String paramString2, Date paramDate2, String paramString3) {
/* 15 */     super(paramString1, paramDate1, paramString2, paramDate2, paramString3);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDisplayName() {
/* 20 */     return (Component)Component.literal(String.valueOf(getUser()));
/*    */   }
/*    */   
/*    */   public IpBanListEntry(JsonObject paramJsonObject) {
/* 24 */     super(createIpInfo(paramJsonObject), paramJsonObject);
/*    */   }
/*    */   
/*    */   private static String createIpInfo(JsonObject paramJsonObject) {
/* 28 */     return paramJsonObject.has("ip") ? paramJsonObject.get("ip").getAsString() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject paramJsonObject) {
/* 33 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 36 */     paramJsonObject.addProperty("ip", getUser());
/* 37 */     super.serialize(paramJsonObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\IpBanListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */