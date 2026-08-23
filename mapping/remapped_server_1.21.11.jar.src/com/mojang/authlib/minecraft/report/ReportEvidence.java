/*   */ package com.mojang.authlib.minecraft.report;
/*   */ 
/*   */ public final class ReportEvidence extends Record {
/*   */   @SerializedName("messages")
/*   */   private final List<ReportChatMessage> messages;
/*   */   
/* 7 */   public ReportEvidence(List<ReportChatMessage> paramList) { this.messages = paramList; } @SerializedName("messages") public List<ReportChatMessage> messages() { return this.messages; }
/*   */ 
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/minecraft/report/ReportEvidence;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/minecraft/report/ReportEvidence;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */   }
/*   */   
/*   */   public final boolean equals(Object paramObject) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/minecraft/report/ReportEvidence;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\report\ReportEvidence.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */