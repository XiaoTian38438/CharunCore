/*    */ package com.mojang.authlib.yggdrasil.response;public final class ErrorResponse extends Record { @SerializedName("path")
/*    */   private final String path; @SerializedName("error")
/*    */   @Nullable
/*    */   private final String error; @SerializedName("errorMessage")
/*    */   @Nullable
/*    */   private final String errorMessage;
/*    */   @SerializedName("details")
/*    */   @Nullable
/*    */   private final Map<String, Object> details;
/*    */   
/* 11 */   public ErrorResponse(String paramString1, @Nullable String paramString2, @Nullable String paramString3, @Nullable Map<String, Object> paramMap) { this.path = paramString1; this.error = paramString2; this.errorMessage = paramString3; this.details = paramMap; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/ErrorResponse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 11 */     //   #11	-> 0 } @SerializedName("path") public String path() { return this.path; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/ErrorResponse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/ErrorResponse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 11 */     //   #11	-> 0 } @SerializedName("error") @Nullable public String error() { return this.error; } @SerializedName("errorMessage") @Nullable public String errorMessage() { return this.errorMessage; } @SerializedName("details") @Nullable public Map<String, Object> details() { return this.details; }
/*    */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\ErrorResponse.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */