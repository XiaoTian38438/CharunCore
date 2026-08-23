/*    */ package com.mojang.authlib.minecraft.report;
/*    */ public final class AbuseReport extends Record { @SerializedName("opinionComments")
/*    */   private final String opinionComments; @SerializedName("reason")
/*    */   @Nullable
/*    */   private final String reason; @SerializedName("evidence")
/*    */   @Nullable
/*    */   private final ReportEvidence evidence;
/*  8 */   public AbuseReport(String paramString1, @Nullable String paramString2, @Nullable ReportEvidence paramReportEvidence, @Nullable String paramString3, ReportedEntity paramReportedEntity, Instant paramInstant) { this.opinionComments = paramString1; this.reason = paramString2; this.evidence = paramReportEvidence; this.skinUrl = paramString3; this.reportedEntity = paramReportedEntity; this.createdTime = paramInstant; } @SerializedName("skinUrl") @Nullable private final String skinUrl; @SerializedName("reportedEntity") private final ReportedEntity reportedEntity; @SerializedName("createdTime") private final Instant createdTime; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/minecraft/report/AbuseReport;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0 } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/minecraft/report/AbuseReport;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/minecraft/report/AbuseReport;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  8 */     //   #8	-> 0 } @SerializedName("opinionComments") public String opinionComments() { return this.opinionComments; } @SerializedName("reason") @Nullable public String reason() { return this.reason; } @SerializedName("evidence") @Nullable public ReportEvidence evidence() { return this.evidence; } @SerializedName("skinUrl") @Nullable public String skinUrl() { return this.skinUrl; } @SerializedName("reportedEntity") public ReportedEntity reportedEntity() { return this.reportedEntity; } @SerializedName("createdTime") public Instant createdTime() { return this.createdTime; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AbuseReport name(String paramString, ReportedEntity paramReportedEntity, Instant paramInstant) {
/* 23 */     return new AbuseReport(paramString, null, null, null, paramReportedEntity, paramInstant);
/*    */   }
/*    */   
/*    */   public static AbuseReport skin(String paramString1, String paramString2, @Nullable String paramString3, ReportedEntity paramReportedEntity, Instant paramInstant) {
/* 27 */     return new AbuseReport(paramString1, paramString2, null, paramString3, paramReportedEntity, paramInstant);
/*    */   }
/*    */   
/*    */   public static AbuseReport chat(String paramString1, String paramString2, ReportEvidence paramReportEvidence, ReportedEntity paramReportedEntity, Instant paramInstant) {
/* 31 */     return new AbuseReport(paramString1, paramString2, paramReportEvidence, null, paramReportedEntity, paramInstant);
/*    */   } }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\report\AbuseReport.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */