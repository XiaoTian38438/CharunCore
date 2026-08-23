/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ public class InsecurePublicKeyException extends RuntimeException {
/*    */   public InsecurePublicKeyException(String paramString) {
/*  5 */     super(paramString);
/*    */   }
/*    */   
/*    */   public static class MissingException extends InsecurePublicKeyException {
/*    */     public MissingException(String param1String) {
/* 10 */       super(param1String);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class InvalidException extends InsecurePublicKeyException {
/*    */     public InvalidException(String param1String) {
/* 16 */       super(param1String);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\InsecurePublicKeyException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */