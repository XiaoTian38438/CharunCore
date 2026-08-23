/*    */ package com.mojang.authlib.exceptions;
/*    */ public class MinecraftClientException extends RuntimeException {
/*    */   protected final ErrorType type;
/*    */   
/*    */   public enum ErrorType {
/*  6 */     SERVICE_UNAVAILABLE,
/*  7 */     HTTP_ERROR,
/*  8 */     JSON_ERROR;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected MinecraftClientException(ErrorType paramErrorType, String paramString) {
/* 14 */     super(paramString);
/* 15 */     this.type = paramErrorType;
/*    */   }
/*    */   
/*    */   public MinecraftClientException(ErrorType paramErrorType, String paramString, Throwable paramThrowable) {
/* 19 */     super(paramString, paramThrowable);
/* 20 */     this.type = paramErrorType;
/*    */   }
/*    */   
/*    */   public ErrorType getType() {
/* 24 */     return this.type;
/*    */   }
/*    */   
/*    */   public AuthenticationException toAuthenticationException() {
/* 28 */     if (this.type == ErrorType.SERVICE_UNAVAILABLE) {
/* 29 */       return new AuthenticationUnavailableException();
/*    */     }
/* 31 */     return new AuthenticationException(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\exceptions\MinecraftClientException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */