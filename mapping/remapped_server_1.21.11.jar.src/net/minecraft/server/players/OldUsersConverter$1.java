/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.mojang.authlib.ProfileLookupCallback;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.server.MinecraftServer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements ProfileLookupCallback
/*    */ {
/*    */   public void onProfileLookupSucceeded(String paramString, UUID paramUUID) {
/* 80 */     NameAndId nameAndId = new NameAndId(paramUUID, paramString);
/* 81 */     server.services().nameToIdCache().add(nameAndId);
/* 82 */     String[] arrayOfString = (String[])userMap.get(nameAndId.name().toLowerCase(Locale.ROOT));
/* 83 */     if (arrayOfString == null) {
/* 84 */       OldUsersConverter.LOGGER.warn("Could not convert user banlist entry for {}", nameAndId.name());
/* 85 */       throw new OldUsersConverter.ConversionError("Profile not in the conversionlist");
/*    */     } 
/*    */     
/* 88 */     Date date1 = (arrayOfString.length > 1) ? OldUsersConverter.parseDate(arrayOfString[1], null) : null;
/* 89 */     String str1 = (arrayOfString.length > 2) ? arrayOfString[2] : null;
/* 90 */     Date date2 = (arrayOfString.length > 3) ? OldUsersConverter.parseDate(arrayOfString[3], null) : null;
/* 91 */     String str2 = (arrayOfString.length > 4) ? arrayOfString[4] : null;
/* 92 */     bans.add(new UserBanListEntry(nameAndId, date1, str1, date2, str2));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onProfileLookupFailed(String paramString, Exception paramException) {
/* 97 */     OldUsersConverter.LOGGER.warn("Could not lookup user banlist entry for {}", paramString, paramException);
/* 98 */     if (!(paramException instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException))
/* 99 */       throw new OldUsersConverter.ConversionError("Could not request user " + paramString + " from backend systems", paramException); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\OldUsersConverter$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */