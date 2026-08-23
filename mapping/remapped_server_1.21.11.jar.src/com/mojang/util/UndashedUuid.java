/*    */ package com.mojang.util;
/*    */ 
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class UndashedUuid {
/*    */   public static UUID fromString(String paramString) {
/*  7 */     if (paramString.indexOf('-') != -1) {
/*  8 */       throw new IllegalArgumentException("Invalid undashed UUID string: " + paramString);
/*    */     }
/* 10 */     return fromStringLenient(paramString);
/*    */   }
/*    */   
/*    */   public static UUID fromStringLenient(String paramString) {
/* 14 */     return UUID.fromString(paramString.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
/*    */   }
/*    */   
/*    */   public static String toString(UUID paramUUID) {
/* 18 */     return paramUUID.toString().replace("-", "");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojan\\util\UndashedUuid.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */