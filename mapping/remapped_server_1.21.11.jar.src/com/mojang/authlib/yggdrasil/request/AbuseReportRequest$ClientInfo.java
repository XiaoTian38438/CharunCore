/*    */ package com.mojang.authlib.yggdrasil.request;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ 
/*    */ public final class ClientInfo extends Record {
/*    */   @SerializedName("clientVersion")
/*    */   private final String clientVersion;
/*    */   @SerializedName("locale")
/*    */   private final String locale;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$ClientInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$ClientInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */   }
/*    */   
/*    */   public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$ClientInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */   }
/*    */   
/*    */   public ClientInfo(String paramString1, String paramString2)
/*    */   {
/* 25 */     this.clientVersion = paramString1; this.locale = paramString2; } @SerializedName("clientVersion") public String clientVersion() { return this.clientVersion; } @SerializedName("locale") public String locale() { return this.locale; }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\request\AbuseReportRequest$ClientInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */