/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import java.net.IDN;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class TextureUrlChecker {
/* 11 */   private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   private static final List<String> ALLOWED_DOMAINS = List.of(".minecraft.net", ".mojang.com");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   private static final List<String> BLOCKED_DOMAINS = List.of("bugs.mojang.com", "education.minecraft.net", "feedback.minecraft.net");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isAllowedTextureDomain(String paramString) {
/*    */     URI uRI;
/*    */     try {
/* 31 */       uRI = (new URI(paramString)).normalize();
/* 32 */     } catch (URISyntaxException uRISyntaxException) {
/* 33 */       return false;
/*    */     } 
/*    */     
/* 36 */     String str1 = uRI.getScheme();
/* 37 */     if (str1 == null || !ALLOWED_SCHEMES.contains(str1)) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     String str2 = uRI.getHost();
/* 42 */     if (str2 == null) {
/* 43 */       return false;
/*    */     }
/* 45 */     String str3 = IDN.toUnicode(str2);
/* 46 */     String str4 = str3.toLowerCase(Locale.ROOT);
/* 47 */     if (!str4.equals(str3)) {
/* 48 */       return false;
/*    */     }
/* 50 */     return (isDomainOnList(str3, ALLOWED_DOMAINS) && !isDomainOnList(str3, BLOCKED_DOMAINS));
/*    */   }
/*    */   
/*    */   private static boolean isDomainOnList(String paramString, List<String> paramList) {
/* 54 */     for (String str : paramList) {
/* 55 */       if (paramString.endsWith(str)) {
/* 56 */         return true;
/*    */       }
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\TextureUrlChecker.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */