/*    */ package com.mojang.authlib.exceptions;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.response.ErrorResponse;
/*    */ import java.util.Optional;
/*    */ import java.util.StringJoiner;
/*    */ import javax.annotation.Nullable;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class MinecraftClientHttpException
/*    */   extends MinecraftClientException {
/*    */   public static final int UNAUTHORIZED = 401;
/*    */   public static final int FORBIDDEN = 403;
/*    */   private final int status;
/*    */   @Nullable
/*    */   private final ErrorResponse response;
/*    */   
/*    */   public MinecraftClientHttpException(int paramInt) {
/* 18 */     super(MinecraftClientException.ErrorType.HTTP_ERROR, getErrorMessage(paramInt, (ErrorResponse)null));
/* 19 */     this.status = paramInt;
/* 20 */     this.response = null;
/*    */   }
/*    */   
/*    */   public MinecraftClientHttpException(int paramInt, ErrorResponse paramErrorResponse) {
/* 24 */     super(MinecraftClientException.ErrorType.HTTP_ERROR, getErrorMessage(paramInt, paramErrorResponse));
/* 25 */     this.status = paramInt;
/* 26 */     this.response = paramErrorResponse;
/*    */   }
/*    */   
/*    */   public int getStatus() {
/* 30 */     return this.status;
/*    */   }
/*    */   
/*    */   public Optional<ErrorResponse> getResponse() {
/* 34 */     return Optional.ofNullable(this.response);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 39 */     return (new StringJoiner(", ", MinecraftClientHttpException.class.getSimpleName() + "[", "]"))
/* 40 */       .add("type=" + String.valueOf(this.type))
/* 41 */       .add("status=" + this.status)
/* 42 */       .add("response=" + String.valueOf(this.response))
/* 43 */       .toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthenticationException toAuthenticationException() {
/* 48 */     if (hasError("ForbiddenOperationException"))
/* 49 */       return new InvalidCredentialsException(getMessage()); 
/* 50 */     if (hasError("multiplayer.access.banned"))
/* 51 */       return new UserBannedException(); 
/* 52 */     if (hasError("FORCED_USERNAME_CHANGE"))
/* 53 */       return new ForcedUsernameChangeException(); 
/* 54 */     if (hasError("InsufficientPrivilegesException")) {
/* 55 */       return new InsufficientPrivilegesException(getMessage(), this);
/*    */     }
/*    */     
/* 58 */     if (this.status == 401) {
/* 59 */       return new InvalidCredentialsException(getMessage(), this);
/*    */     }
/*    */     
/* 62 */     if (this.status >= 500) {
/* 63 */       return new AuthenticationUnavailableException(getMessage(), this);
/*    */     }
/*    */     
/* 66 */     return new AuthenticationException(getMessage(), this);
/*    */   }
/*    */   
/*    */   private Optional<String> getError() {
/* 70 */     return getResponse()
/* 71 */       .<String>map(ErrorResponse::error)
/* 72 */       .filter(StringUtils::isNotEmpty);
/*    */   }
/*    */   
/*    */   private static String getErrorMessage(int paramInt, ErrorResponse paramErrorResponse) {
/*    */     String str;
/* 77 */     if (paramErrorResponse != null) {
/* 78 */       if (StringUtils.isNotEmpty(paramErrorResponse.errorMessage())) {
/* 79 */         str = paramErrorResponse.errorMessage();
/* 80 */       } else if (StringUtils.isNotEmpty(paramErrorResponse.error())) {
/* 81 */         str = paramErrorResponse.error();
/*    */       } else {
/* 83 */         str = "Status: " + paramInt;
/*    */       } 
/*    */     } else {
/* 86 */       str = "Status: " + paramInt;
/*    */     } 
/* 88 */     return str;
/*    */   }
/*    */   
/*    */   private boolean hasError(String paramString) {
/* 92 */     return getError()
/* 93 */       .filter(paramString2 -> paramString2.equalsIgnoreCase(paramString1))
/* 94 */       .isPresent();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\exceptions\MinecraftClientHttpException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */