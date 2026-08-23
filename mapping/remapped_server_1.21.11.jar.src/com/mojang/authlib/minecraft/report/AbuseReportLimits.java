/*    */ package com.mojang.authlib.minecraft.report;public final class AbuseReportLimits extends Record { private final int maxOpinionCommentsLength; private final int maxReportedMessageCount; private final int maxEvidenceMessageCount; private final int leadingContextMessageCount; private final int trailingContextMessageCount;
/*    */   
/*  3 */   public AbuseReportLimits(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) { this.maxOpinionCommentsLength = paramInt1; this.maxReportedMessageCount = paramInt2; this.maxEvidenceMessageCount = paramInt3; this.leadingContextMessageCount = paramInt4; this.trailingContextMessageCount = paramInt5; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/minecraft/report/AbuseReportLimits;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  3 */     //   #3	-> 0 } public int maxOpinionCommentsLength() { return this.maxOpinionCommentsLength; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/minecraft/report/AbuseReportLimits;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/minecraft/report/AbuseReportLimits;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  3 */     //   #3	-> 0 } public int maxReportedMessageCount() { return this.maxReportedMessageCount; } public int maxEvidenceMessageCount() { return this.maxEvidenceMessageCount; } public int leadingContextMessageCount() { return this.leadingContextMessageCount; } public int trailingContextMessageCount() { return this.trailingContextMessageCount; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 10 */   public static final AbuseReportLimits DEFAULTS = new AbuseReportLimits(1000, 4, 40, 9, 0); }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\report\AbuseReportLimits.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */