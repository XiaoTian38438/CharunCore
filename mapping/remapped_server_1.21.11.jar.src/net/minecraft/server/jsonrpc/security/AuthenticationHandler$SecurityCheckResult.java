/*     */ package net.minecraft.server.jsonrpc.security;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SecurityCheckResult
/*     */ {
/*     */   private final boolean allowed;
/*     */   private final String reason;
/*     */   private final boolean tokenSentInSecWebsocketProtocol;
/*     */   
/*     */   private SecurityCheckResult(boolean paramBoolean1, String paramString, boolean paramBoolean2) {
/* 175 */     this.allowed = paramBoolean1;
/* 176 */     this.reason = paramString;
/* 177 */     this.tokenSentInSecWebsocketProtocol = paramBoolean2;
/*     */   }
/*     */   
/*     */   public static SecurityCheckResult allowed() {
/* 181 */     return new SecurityCheckResult(true, null, false);
/*     */   }
/*     */   
/*     */   public static SecurityCheckResult allowed(boolean paramBoolean) {
/* 185 */     return new SecurityCheckResult(true, null, paramBoolean);
/*     */   }
/*     */   
/*     */   public static SecurityCheckResult denied(String paramString) {
/* 189 */     return new SecurityCheckResult(false, paramString, false);
/*     */   }
/*     */   
/* 192 */   public boolean isAllowed() { return this.allowed; }
/* 193 */   public String getReason() { return this.reason; } public boolean isTokenSentInSecWebsocketProtocol() {
/* 194 */     return this.tokenSentInSecWebsocketProtocol;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\security\AuthenticationHandler$SecurityCheckResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */