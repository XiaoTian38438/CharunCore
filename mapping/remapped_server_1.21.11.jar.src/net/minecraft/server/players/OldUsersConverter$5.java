/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import java.io.File;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements ProfileLookupCallback
/*     */ {
/*     */   public void onProfileLookupSucceeded(String paramString, UUID paramUUID) {
/* 310 */     NameAndId nameAndId = new NameAndId(paramUUID, paramString);
/* 311 */     server.services().nameToIdCache().add(nameAndId);
/* 312 */     movePlayerFile(worldNewPlayerDirectory, getFileNameForProfile(paramString), paramUUID.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProfileLookupFailed(String paramString, Exception paramException) {
/* 317 */     OldUsersConverter.LOGGER.warn("Could not lookup user uuid for {}", paramString, paramException);
/* 318 */     if (paramException instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException) {
/* 319 */       String str = getFileNameForProfile(paramString);
/* 320 */       movePlayerFile(unknownPlayerDirectory, str, str);
/*     */     } else {
/* 322 */       throw new OldUsersConverter.ConversionError("Could not request user " + paramString + " from backend systems", paramException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void movePlayerFile(File paramFile, String paramString1, String paramString2) {
/* 327 */     File file1 = new File(worldPlayerDirectory, paramString1 + ".dat");
/* 328 */     File file2 = new File(paramFile, paramString2 + ".dat");
/* 329 */     OldUsersConverter.ensureDirectoryExists(paramFile);
/* 330 */     if (!file1.renameTo(file2)) {
/* 331 */       throw new OldUsersConverter.ConversionError("Could not convert file for " + paramString1);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getFileNameForProfile(String paramString) {
/* 336 */     String str = null;
/* 337 */     for (String str1 : names) {
/* 338 */       if (str1 != null && str1.equalsIgnoreCase(paramString)) {
/* 339 */         str = str1;
/*     */         break;
/*     */       } 
/*     */     } 
/* 343 */     if (str == null) {
/* 344 */       throw new OldUsersConverter.ConversionError("Could not find the filename for " + paramString + " anymore");
/*     */     }
/* 346 */     return str;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\OldUsersConverter$5.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */